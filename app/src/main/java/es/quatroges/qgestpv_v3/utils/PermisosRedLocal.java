package es.quatroges.qgestpv_v3.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Permiso de acceso a la red local (Android 17, API 37).
 *
 * Desde Android 17 las apps que apuntan a la API 37 necesitan ACCESS_LOCAL_NETWORK, un permiso de ejecucion,
 * para abrir conexiones hacia direcciones de la LAN. Sin el, las conexiones al servidor del comandero
 * (p.ej. 192.168.1.x) se descartan en silencio y terminan en timeout de conexion.
 */
public final class PermisosRedLocal {

    public static final int REQUEST_PERMISO_RED_LOCAL = 4102;
    /** Nombre literal para no depender de la constante del SDK en compilaciones anteriores. */
    private static final String ACCESS_LOCAL_NETWORK = "android.permission.ACCESS_LOCAL_NETWORK";
    private static final int API_ANDROID_17 = 37;

    /** true mientras el dialogo del sistema esta en pantalla y aun no hay respuesta. */
    private static volatile boolean solicitando = false;

    private PermisosRedLocal() {
    }

    /** Solo aplica en Android 17 o superior. */
    public static boolean necesario() {
        return Build.VERSION.SDK_INT >= API_ANDROID_17;
    }

    public static boolean tiene(Context context) {
        if (!necesario() || context == null) return true;
        return ContextCompat.checkSelfPermission(context, ACCESS_LOCAL_NETWORK) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Pide el permiso si falta.
     * @return true si ya estaba concedido o no aplica (no se ha pedido nada).
     */
    public static boolean solicitarSiFalta(Activity activity) {
        if (tiene(activity)) return true;
        if (!solicitando) {
            solicitando = true;
            ActivityCompat.requestPermissions(activity, new String[]{ACCESS_LOCAL_NETWORK}, REQUEST_PERMISO_RED_LOCAL);
        }
        return false;
    }

    /** true mientras se espera la respuesta del usuario: conviene no lanzar llamadas al servidor hasta entonces. */
    public static boolean pendienteDeRespuesta() {
        return solicitando;
    }

    /** Procesa el resultado de onRequestPermissionsResult para REQUEST_PERMISO_RED_LOCAL. */
    public static boolean resultado(int[] grantResults) {
        solicitando = false;
        return grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }
}
