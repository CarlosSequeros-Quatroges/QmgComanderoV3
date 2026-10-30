package es.quatroges.qgestpv_v3.webservice;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.quatroges.qgestpv_v3.BuildConfig;
import okhttp3.Dns;

/**
 * Resolucion local de nombres para las llamadas a la API, equivalente al fichero hosts del PC.
 *
 * Uso (solo desarrollo): fichero hosts.txt en la carpeta de la app, Android/data/&lt;paquete&gt;/files
 * (la misma de los conf-*.json), con una entrada por linea en cualquiera de estos formatos:
 *   r0848.qmgreservas.quatrogesvpn.com=192.168.1.52
 *   192.168.1.52  r0848.qmgreservas.quatrogesvpn.com   (formato hosts clasico)
 * Las lineas vacias o que empiezan por # se ignoran. El fichero se relee cuando cambia.
 *
 * La URL configurada sigue usando el nombre, asi que SNI, certificado y cabecera Host se mantienen y un proxy
 * que enruta por nombre funciona igual que desde el PC. Los nombres que no esten en el fichero se resuelven por DNS.
 *
 * Solo esta activo en compilaciones de depuracion (BuildConfig.DEBUG): en release siempre se usa el DNS del sistema.
 */
public final class HostsLocales implements Dns {

    private static final String TAG = "HostsLocales";
    public static final String FICHERO = "hosts.txt";
    private static final boolean SOLO_DEBUG = true;

    private static final HostsLocales INSTANCIA = new HostsLocales();

    private File fichero;
    private long ultimaModificacion = -1;
    private Map<String, List<InetAddress>> tabla = Collections.emptyMap();

    private HostsLocales() {
    }

    public static HostsLocales getInstance() {
        return INSTANCIA;
    }

    /** Indica donde buscar hosts.txt. Llamar una vez al arrancar con el contexto de aplicacion. */
    public static void inicializar(Context context) {
        File dir = context.getExternalFilesDir(null);
        if (dir == null) dir = context.getFilesDir();
        INSTANCIA.fichero = new File(dir, FICHERO);
        INSTANCIA.ultimaModificacion = -1;
        INSTANCIA.recargarSiCambio();
        Log.i(TAG, INSTANCIA.activo()
                ? "Resolucion local activa: " + INSTANCIA.fichero.getAbsolutePath() + " (" + INSTANCIA.tabla.size() + " nombres)"
                : "Resolucion local inactiva (sin " + INSTANCIA.fichero.getAbsolutePath() + ")");
    }

    public boolean activo() {
        return (!SOLO_DEBUG || BuildConfig.DEBUG) && fichero != null && !tabla.isEmpty();
    }

    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        if (SOLO_DEBUG && !BuildConfig.DEBUG) return Dns.SYSTEM.lookup(hostname);

        recargarSiCambio();
        List<InetAddress> locales = tabla.get(hostname.toLowerCase(Locale.ROOT));
        if (locales != null && !locales.isEmpty()) {
            Log.i(TAG, hostname + " -> " + locales.get(0).getHostAddress() + " (hosts.txt)");
            return locales;
        }
        return Dns.SYSTEM.lookup(hostname);
    }

    /** Relee hosts.txt si ha cambiado desde la ultima vez (o ha aparecido / desaparecido). */
    private synchronized void recargarSiCambio() {
        if (fichero == null) return;
        long mod = fichero.exists() ? fichero.lastModified() : 0;
        if (mod == ultimaModificacion) return;
        ultimaModificacion = mod;

        Map<String, List<InetAddress>> nueva = new HashMap<>();
        if (fichero.exists()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fichero), StandardCharsets.UTF_8))) {
                String linea;
                int n = 0;
                while ((linea = br.readLine()) != null) {
                    n++;
                    String l = linea.trim();
                    int comentario = l.indexOf('#');
                    if (comentario >= 0) l = l.substring(0, comentario).trim();
                    if (l.isEmpty()) continue;

                    String[] partes = l.split("[=\\s]+");
                    if (partes.length < 2) {
                        Log.w(TAG, "hosts.txt linea " + n + " ignorada: " + linea);
                        continue;
                    }
                    // admite "nombre=ip", "nombre ip" e "ip nombre"
                    String a = partes[0], b = partes[1];
                    String ip = esIp(a) ? a : b;
                    String nombre = esIp(a) ? b : a;
                    if (!esIp(ip)) {
                        Log.w(TAG, "hosts.txt linea " + n + " sin IP valida: " + linea);
                        continue;
                    }
                    try {
                        InetAddress dir = InetAddress.getByName(ip); // literal: no consulta DNS
                        List<InetAddress> lista = new ArrayList<>(1);
                        lista.add(InetAddress.getByAddress(nombre, dir.getAddress()));
                        nueva.put(nombre.toLowerCase(Locale.ROOT), lista);
                    } catch (UnknownHostException e) {
                        Log.w(TAG, "hosts.txt linea " + n + " IP no valida: " + ip);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "No se ha podido leer " + fichero.getAbsolutePath(), e);
            }
        }
        tabla = nueva;
        Log.i(TAG, "hosts.txt recargado: " + nueva.size() + " nombres");
    }

    private static boolean esIp(String s) {
        return s.matches("\\d{1,3}(\\.\\d{1,3}){3}") || (s.contains(":") && s.matches("[0-9a-fA-F:.]+"));
    }
}
