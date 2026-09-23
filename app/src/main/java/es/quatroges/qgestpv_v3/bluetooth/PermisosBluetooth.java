package es.quatroges.qgestpv_v3.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Permisos necesarios para buscar y conectar impresoras Bluetooth según la versión de Android.
 *
 * - Android 12+ (API 31): BLUETOOTH_SCAN y BLUETOOTH_CONNECT (permisos de ejecución).
 * - Android 8 a 11 (API 26-30): BLUETOOTH y BLUETOOTH_ADMIN se conceden al instalar,
 *   pero el escaneo exige ACCESS_FINE_LOCATION concedido en ejecución.
 */
public final class PermisosBluetooth {

    public static final int REQUEST_PERMISOS_BT = 4101;

    private PermisosBluetooth() {
    }

    /** Permisos de ejecución que hay que pedir en esta versión de Android. */
    public static String[] permisosNecesarios() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return new String[]{
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
            };
        }
        return new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    }

    /** true si todos los permisos de ejecución necesarios están concedidos. */
    public static boolean tienePermisos(Context context) {
        if (context == null) return false;
        for (String p : permisosNecesarios()) {
            if (ContextCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Permiso para conectar (getBondedDevices, getName, sockets, enable/disable).
     * En Android < 12 no existe BLUETOOTH_CONNECT y basta con el permiso de instalación.
     */
    public static boolean tienePermisoConectar(Context context) {
        if (context == null) return false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return true;
        return ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Permiso para escanear (startDiscovery, cancelDiscovery, isDiscovering).
     * En Android < 12 el escaneo necesita ubicación precisa en ejecución.
     */
    public static boolean tienePermisoEscanear(Context context) {
        if (context == null) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Lanza el diálogo del sistema para pedir los permisos que falten.
     * @return true si ya estaban concedidos (no se ha pedido nada).
     */
    public static boolean solicitarSiFaltan(Activity activity) {
        if (tienePermisos(activity)) return true;
        ActivityCompat.requestPermissions(activity, permisosNecesarios(), REQUEST_PERMISOS_BT);
        return false;
    }

    /** Evalúa el resultado de onRequestPermissionsResult para REQUEST_PERMISOS_BT. */
    public static boolean todosConcedidos(int[] grantResults) {
        if (grantResults == null || grantResults.length == 0) return false;
        for (int r : grantResults) {
            if (r != PackageManager.PERMISSION_GRANTED) return false;
        }
        return true;
    }
}
