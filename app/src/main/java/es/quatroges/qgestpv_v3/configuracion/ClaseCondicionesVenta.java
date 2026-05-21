package es.quatroges.qgestpv_v3.configuracion;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.datos.listas.establecimientos.ClaseEstablecimientos;

public class ClaseCondicionesVenta {
    private static final String PREFS_NAME = "qmg_condiciones_venta";
    private static final String PREFS_KEY = "condiciones_venta_json";

    public static Double igic;
    public static String tipo_igic;
    public static String tipo_cuentacasa;
    public static String pedir_clave;
    public static String orden_platos;
    public static String pedir_pax;
    public static String pedir_firma;

    public static ArrayList<ClaseEstablecimientos> establecimientos;

    public ClaseCondicionesVenta(String codemp, String empresa) {
        igic = 0.00;
        tipo_igic = "E";
        tipo_cuentacasa = "G";
        pedir_clave = "N";
        orden_platos = "N";
        pedir_pax = "N";
        pedir_firma = "N";
        establecimientos = new ArrayList<>();
        establecimientos.add(new ClaseEstablecimientos(codemp,empresa));
    }

    public static boolean cargarPreferencias(Context context, String codemp, String empresa) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String json = prefs.getString(PREFS_KEY, null);
            if (json == null || json.isEmpty()) return false;

            CondicionesCache cache = new Gson().fromJson(json, CondicionesCache.class);
            if (cache == null) return false;

            if (cache.igic != null) igic = cache.igic;
            if (cache.tipo_igic != null) tipo_igic = cache.tipo_igic;
            if (cache.tipo_cuentacasa != null) tipo_cuentacasa = cache.tipo_cuentacasa;
            if (cache.pedir_clave != null) pedir_clave = cache.pedir_clave;
            if (cache.orden_platos != null) orden_platos = cache.orden_platos;
            if (cache.pedir_pax != null) pedir_pax = cache.pedir_pax;
            if (cache.pedir_firma != null) pedir_firma = cache.pedir_firma;

            if (cache.establecimientos != null && !cache.establecimientos.isEmpty()) {
                establecimientos = cache.establecimientos;
            } else if (establecimientos == null || establecimientos.isEmpty()) {
                establecimientos = new ArrayList<>();
                establecimientos.add(new ClaseEstablecimientos(codemp, empresa));
            }
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static void guardarPreferencias(Context context) {
        try {
            CondicionesCache cache = new CondicionesCache();
            cache.igic = igic;
            cache.tipo_igic = tipo_igic;
            cache.tipo_cuentacasa = tipo_cuentacasa;
            cache.pedir_clave = pedir_clave;
            cache.orden_platos = orden_platos;
            cache.pedir_pax = pedir_pax;
            cache.pedir_firma = pedir_firma;
            cache.establecimientos = establecimientos;

            String json = new Gson().toJson(cache);
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            prefs.edit().putString(PREFS_KEY, json).apply();
        } catch (Exception ignored) {
        }
    }

    private static class CondicionesCache {
        Double igic;
        String tipo_igic;
        String tipo_cuentacasa;
        String pedir_clave;
        String orden_platos;
        String pedir_pax;
        String pedir_firma;
        ArrayList<ClaseEstablecimientos> establecimientos;
    }
}
