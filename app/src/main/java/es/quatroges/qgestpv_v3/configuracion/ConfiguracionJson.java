package es.quatroges.qgestpv_v3.configuracion;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import es.quatroges.qgestpv_v3.BuildConfig;
import es.quatroges.qgestpv_v3.basedatos.ClaseBaseDatos;
import es.quatroges.qgestpv_v3.datos.Configuracion;

/**
 * Exportacion e importacion de la tabla de configuracion en formato JSON.
 *
 * Fichero: carpeta propia de la app (Android/data/&lt;paquete&gt;/files), nombre conf-&lt;algo&gt;.json.
 * El mismo JSON puede ir dentro de un codigo QR para cargar una configuracion completa de un vistazo.
 *
 * Formato:
 * {
 *   "app": "QMGComandero", "version": "...", "fecha": "yyyy-MM-dd HH:mm:ss",
 *   "configuracion": { "url": "...", "api": "...", "codigo": "...", ... }
 * }
 * Tambien se acepta un JSON plano con solo el objeto de claves y valores.
 */
public final class ConfiguracionJson {

    private static final String TAG = "ConfiguracionJson";
    public static final String PREFIJO = "conf-";
    public static final String EXTENSION = ".json";
    public static final String FICHERO_ACTUAL = PREFIJO + "actual" + EXTENSION;

    /**
     * Claves ligadas al dispositivo que NO se importan: la impresora emparejada pertenece a cada terminal,
     * no a la configuracion del cliente/servidor que se quiere reproducir.
     */
    private static final Set<String> CLAVES_NO_IMPORTABLES = new HashSet<>(Arrays.asList("btmac", "btname", "btmodelo"));

    private ConfiguracionJson() {
    }

    /** Carpeta donde se guardan y buscan los ficheros de configuracion. */
    public static File carpeta(Context context) {
        File dir = context.getExternalFilesDir(null);
        if (dir == null) dir = context.getFilesDir();
        if (!dir.exists()) dir.mkdirs();
        return dir;
    }

    /** Vuelca la configuracion guardada en la base de datos a conf-actual.json. */
    public static File exportar(Context context, ClaseBaseDatos bd) throws Exception {
        List<Configuracion> parametros = bd.getConfiguracion();

        JSONObject conf = new JSONObject();
        if (parametros != null) {
            for (Configuracion p : parametros) {
                conf.put(p.getParametro(), p.getValor() == null ? "" : p.getValor());
            }
        }

        JSONObject raiz = new JSONObject();
        raiz.put("app", "QMGComandero");
        raiz.put("version", BuildConfig.VERSION_NAME);
        raiz.put("fecha", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT).format(new Date()));
        raiz.put("configuracion", conf);

        File fichero = new File(carpeta(context), FICHERO_ACTUAL);
        try (OutputStream os = new FileOutputStream(fichero)) {
            os.write(raiz.toString(2).getBytes(StandardCharsets.UTF_8));
        }
        Log.i(TAG, "Configuracion exportada a " + fichero.getAbsolutePath() + " (" + conf.length() + " claves)");
        return fichero;
    }

    /** Ficheros conf-*.json de la carpeta de la app, ordenados por nombre. */
    public static List<File> listar(Context context) {
        File[] ficheros = carpeta(context).listFiles((dir, name) ->
                name.toLowerCase(Locale.ROOT).startsWith(PREFIJO) && name.toLowerCase(Locale.ROOT).endsWith(EXTENSION));
        List<File> lista = new ArrayList<>();
        if (ficheros != null) lista.addAll(Arrays.asList(ficheros));
        Collections.sort(lista, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return lista;
    }

    /** Carga un fichero conf-*.json en la base de datos. Devuelve el numero de claves aplicadas. */
    public static int importarFichero(ClaseBaseDatos bd, File fichero) throws Exception {
        byte[] datos;
        try (InputStream is = new FileInputStream(fichero)) {
            datos = leerTodo(is);
        }
        return importarJson(bd, new String(datos, StandardCharsets.UTF_8));
    }

    /**
     * Carga un JSON (fichero o contenido de un QR) en la base de datos.
     * @return numero de claves aplicadas
     * @throws JSONException si el texto no es un JSON de configuracion valido
     */
    public static int importarJson(ClaseBaseDatos bd, String json) throws Exception {
        if (json == null) throw new JSONException("JSON vacio");
        JSONObject raiz = new JSONObject(json.trim());
        JSONObject conf = raiz.has("configuracion") ? raiz.getJSONObject("configuracion") : raiz;

        Map<String, String> valores = new LinkedHashMap<>();
        Iterator<String> claves = conf.keys();
        while (claves.hasNext()) {
            String clave = claves.next();
            if (CLAVES_NO_IMPORTABLES.contains(clave.toLowerCase(Locale.ROOT))) continue;
            Object v = conf.get(clave);
            valores.put(clave, v == null || JSONObject.NULL.equals(v) ? "" : String.valueOf(v));
        }
        if (valores.isEmpty()) throw new JSONException("El JSON no contiene parametros de configuracion");

        bd.importaConfiguracion(valores);
        Log.i(TAG, "Configuracion importada: " + valores.size() + " claves");
        return valores.size();
    }

    /** Texto JSON compacto de la configuracion actual (para mostrarlo o generar un QR). */
    public static String jsonActual(ClaseBaseDatos bd) throws Exception {
        List<Configuracion> parametros = bd.getConfiguracion();
        JSONObject conf = new JSONObject();
        if (parametros != null) {
            for (Configuracion p : parametros) conf.put(p.getParametro(), p.getValor() == null ? "" : p.getValor());
        }
        return conf.toString();
    }

    private static byte[] leerTodo(InputStream is) throws IOException {
        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int n;
        while ((n = is.read(buf)) != -1) bos.write(buf, 0, n);
        return bos.toByteArray();
    }
}
