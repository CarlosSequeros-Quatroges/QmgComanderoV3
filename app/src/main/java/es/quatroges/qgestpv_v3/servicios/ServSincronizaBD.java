package es.quatroges.qgestpv_v3.servicios;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import es.quatroges.qgestpv_v3.ActivityInicio;
import es.quatroges.qgestpv_v3.R;
import es.quatroges.qgestpv_v3.basedatos.ClaseBaseDatos;
import es.quatroges.qgestpv_v3.configuracion.ClaseCondicionesVenta;
import es.quatroges.qgestpv_v3.configuracion.ClaseConfiguracion;
import es.quatroges.qgestpv_v3.datos.ActualizaPax;
import es.quatroges.qgestpv_v3.datos.EstadoMesas;
import es.quatroges.qgestpv_v3.datos.Facturas;
import es.quatroges.qgestpv_v3.datos.GrabaLineasVenta;
import es.quatroges.qgestpv_v3.datos.LineasVenta;
import es.quatroges.qgestpv_v3.datos.ResGrabaLineas;
import es.quatroges.qgestpv_v3.datos.ResGrabaLineasSubmesas;
import es.quatroges.qgestpv_v3.datos.listas.facturas.ClaseFacturaCabecera;
import es.quatroges.qgestpv_v3.datos.listas.facturas.ClaseFacturas;
import es.quatroges.qgestpv_v3.datos.PensionesSubmesa;
import es.quatroges.qgestpv_v3.datos.roomRegistrosCRC;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;
import es.quatroges.qgestpv_v3.webservice.APIService;
import es.quatroges.qgestpv_v3.webservice.ClaseServicioWeb;
import es.quatroges.qgestpv_v3.webservice.RequestActualizaPaxWS;
import es.quatroges.qgestpv_v3.webservice.RequestGrabaLineasWS;
import es.quatroges.qgestpv_v3.webservice.RequestPagaMesaWS;
import es.quatroges.qgestpv_v3.webservice.RequestRecuperaRegistrosWS;
import es.quatroges.qgestpv_v3.webservice.RequestTraspasaMesaWS;
import es.quatroges.qgestpv_v3.webservice.RespuestaBaseWS;
import es.quatroges.qgestpv_v3.webservice.RespuestaCreditoWS;
import es.quatroges.qgestpv_v3.webservice.RespuestaEstadoMesasWS;
import es.quatroges.qgestpv_v3.webservice.RespuestaFacturaWS;
import es.quatroges.qgestpv_v3.webservice.RespuestaFacturasWS;
import es.quatroges.qgestpv_v3.webservice.RespuestaGrabaLineasWS;
import es.quatroges.qgestpv_v3.webservice.RespuestaLineasMesaWS;
import es.quatroges.qgestpv_v3.webservice.RespuestaPagaMesaWS;
import es.quatroges.qgestpv_v3.webservice.RespuestaPedirCuentaWS;
import es.quatroges.qgestpv_v3.webservice.RespuestaPensionWS;
import es.quatroges.qgestpv_v3.webservice.RespuestaRegistrosMD5WS;
import es.quatroges.qgestpv_v3.webservice.RespuestaSincro;
import es.quatroges.qgestpv_v3.webservice.RespuestaTestjsonWS;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServSincronizaBD extends IntentService {

    //region VARIABLES Y CONSTANTES
    private static final String ACTION_GETTESTJSON = "es.quatroges.qgestpv_v1.action.GETTESTJSON";

    public static final String ACTION_ERROR_DOWNLOAD_APP="es.quatroges.qMGTickets.ERROR_DOWNLOAD_APP";


    public static final int ACTION_GRABANDO_REGISTROS =101;
    public static final int ACTION_BORRANDO_REGISTROS=102;
    public static final int ACTION_DESCARGANDO_REGISTROS=103;
    public static final int ACTION_ACTUALIZANDO_OPERACIONESTPV =4;
    public static final int ACTION_ACTUALIZANDO_LOGS =5;


    private static final String ACTION_GETMD5= "es.quatroges.qMGTickets.action.GETMD5";
    public static final String ACTION_OK_MD5="es.quatroges.qMGTickets.OK_MD5";
    public static final String ACTION_ERROR_MD5="es.quatroges.qMGTickets.ERROR_MD5";

    private static final String ACTION_GETTABLA= "es.quatroges.qMGTickets.action.GETTABLA";
    public static final String ACTION_OK_TABLA="es.quatroges.qMGTickets.OK_TABLA";
    public static final String ACTION_ERROR_TABLA="es.quatroges.qMGTickets.ERROR_TABLA";


    private static final String ACTION_GETTABLAS= "es.quatroges.qgestpv_v1.action.GETTABLAS";
    private static final String ACTION_GETESTADOMESAS= "es.quatroges.qgestpv_v1.action.GETESTADOMESAS";
    private static final String ACTION_GETLINEASMESA= "es.quatroges.qgestpv_v1.action.GETLINEASMESA";

    private static final String ACTION_GETPENSION = "es.quatroges.qgestpv_v1.action.GETPENSION";
    private static final String ACTION_PUTLINEASMESA= "es.quatroges.qgestpv_v1.action.PUTLINEASMESA";
    private static final String ACTION_PEDIRCUENTA= "es.quatroges.qgestpv_v1.action.PEDIRCUENTA";
    private static final String ACTION_ACTUALIZAPAX= "es.quatroges.qgestpv_v1.action.ACTUALIZAPAX";
    private static final String ACTION_TRASPASAMESA= "es.quatroges.qgestpv_v1.action.TRASPASAMESA";
    private static final String ACTION_PAGAMESA= "es.quatroges.qgestpv_v1.action.PAGAMESA";
    private static final String ACTION_CREDITO_TAGID= "es.quatroges.qgestpv_v1.action.CREDITO_TAGID";
    private static final String ACTION_CREDITO_ROOM= "es.quatroges.qgestpv_v1.action.CREDITO_ROOM";
    private static final String ACTION_SIGUE_PLATO= "es.quatroges.qgestpv_v1.action.SIGUE_PLATO";
    private static final String ACTION_RECUPERA_FACTURAS = "es.quatroges.qgestpv_v1.action.RECUPERA_FACTURAS";
    private static final String ACTION_RECUPERA_FACTURA = "es.quatroges.qgestpv_v1.action.RECUPERA_FACTURA";
    private static final String ACTION_IMPRIME_FACTURA = "es.quatroges.qgestpv_v1.action.IMPRIME_FACTURA";

    private static final String ACTION_GETVERSIONES = "es.quatroges.qgestpv_v1.action.GETVERSIONES";
    public static final String ACTION_OK_VERSIONES="es.quatroges.qgestpv_v1.action.OK_VERSIONES";
    public static final String ACTION_ERROR_VERSIONES ="es.quatroges.qgestpv_v1.action.ERROR_VERSIONES";


    public static final String ACTION_OK_USUARIOS="es.quatroges.qgestpv_v1.action.OK_USUARIOS";
    public static final String ACTION_OK_TPVS="es.quatroges.qgestpv_v1.action.OK_TPVS";
    public static final String ACTION_OK_NOMMESAS="es.quatroges.qgestpv_v1.action.OK_NOMMESAS";
    public static final String ACTION_OK_CABECERAS="es.quatroges.qgestpv_v1.action.OK_CABECERAS";
    public static final String ACTION_OK_PRODUCTOS="es.quatroges.qgestpv_v1.action.OK_PRODUCTOS";
    public static final String ACTION_OK_CLIENTESCTACASA="es.quatroges.qgestpv_v1.action.OK_CLIENTESCTACASA";
    public static final String ACTION_OK_ALERGENOS="es.quatroges.qgestpv_v1.action.OK_ALERGENOS";
    public static final String ACTION_OK_ETIQUETAS="es.quatroges.qgestpv_v1.action.OK_ETIQUETAS";

    public static final String ACTION_OK_TESTJSON ="es.quatroges.qgestpv_v1.action.OK_TESTJSON";

    public static final String ACTION_OK_ESTADOMESAS="es.quatroges.qgestpv_v1.action.OK_ESTADOMESAS";
    public static final String ACTION_OK_LINEASVENTA ="es.quatroges.qgestpv_v1.action.OK_LINEASMESA";

    public static final String ACTION_OK_PENSION="es.quatroges.qgestpv_v1.action.OK_PENSION";
    public static final String ACTION_OK_GRABALINEASVENTA ="es.quatroges.qgestpv_v1.action.OK_GRABALINEASMESA";
    public static final String ACTION_OK_PEDIRCUENTA="es.quatroges.qgestpv_v1.action.OK_PEDIRCUENTA";
    public static final String ACTION_OK_ACTUALIZAPAX="es.quatroges.qgestpv_v1.action.OK_ACTUALIZAPAX";
    public static final String ACTION_OK_TRASPASAMESA="es.quatroges.qgestpv_v1.action.OK_TRASPASAMESA";
    public static final String ACTION_OK_PAGAMESA="es.quatroges.qgestpv_v1.action.OK_PAGAMESA";
    public static final String ACTION_OK_CREDITO="es.quatroges.qgestpv_v1.action.OK_CREDITO";
    public static final String ACTION_OK_SIGUEPLATO="es.quatroges.qgestpv_v1.action.OK_SIGUEPLATO";
    public static final String ACTION_OK_RECUPERAFACTURAS="es.quatroges.qgestpv_v1.action.OK_RECUPERAFACTURAS";
    public static final String ACTION_OK_RECUPERAFACTURA="es.quatroges.qgestpv_v1.action.OK_RECUPERAFACTURA";
    public static final String ACTION_OK_IMPRIMEFACTURA="es.quatroges.qgestpv_v1.action.OK_IMPRIMEFACTURA";

    public static final String ACTION_ERROR_TESTJSON="es.quatroges.qgestpv_v1.action.ERROR_TESTJSON";


    public static final String ACTION_ERROR_USUARIOS="es.quatroges.qgestpv_v1.action.ERROR_USUARIOS";
    public static final String ACTION_ERROR_TPVS="es.quatroges.qgestpv_v1.action.ERROR_TPVS";
    public static final String ACTION_ERROR_NOMMESAS="es.quatroges.qgestpv_v1.action.ERROR_NOMMESAS";
    public static final String ACTION_ERROR_CABECERAS="es.quatroges.qgestpv_v1.action.ERROR_CABECERAS";
    public static final String ACTION_ERROR_PRODUCTOS="es.quatroges.qgestpv_v1.action.ERROR_PRODUCTOS";
    public static final String ACTION_ERROR_CLIENTESCTACASA="es.quatroges.qgestpv_v1.action.ERROR_CLIENTESCTACASA";
    public static final String ACTION_ERROR_ALERGENOS="es.quatroges.qgestpv_v1.action.ERROR_ALERGENOS";
    public static final String ACTION_ERROR_ETIQUETAS="es.quatroges.qgestpv_v1.action.ERROR_ETIQUETAS";

    public static final String ACTION_ERROR_ESTADOMESAS="es.quatroges.qgestpv_v1.action.ERROR_ESTADOMESAS";
    public static final String ACTION_ERROR_LINEASVENTA ="es.quatroges.qgestpv_v1.action.ERROR_LINEASMESAS";

    public static final String ACTION_ERROR_PENSION ="es.quatroges.qgestpv_v1.action.ERROR_PENSION";
    public static final String ACTION_ERROR_GRABALINEASVENTA ="es.quatroges.qgestpv_v1.action.ERROR_GRABALINEASMESAS";
    public static final String ACTION_ERROR_PEDIRCUENTA="es.quatroges.qgestpv_v1.action.ERROR_PEDIRCUENTA";
    public static final String ACTION_ERROR_ACTUALIZAPAX="es.quatroges.qgestpv_v1.action.ERROR_ACTUALIZAPAX";
    public static final String ACTION_ERROR_TRASPASAMESA="es.quatroges.qgestpv_v1.action.ERROR_TRASPASAMESA";
    public static final String ACTION_ERROR_PAGAMESA="es.quatroges.qgestpv_v1.action.ERROR_PAGAMESA";
    public static final String ACTION_ERROR_CREDITO="es.quatroges.qgestpv_v1.action.ERROR_CREDITO";
    public static final String ACTION_ERROR_SIGUEPLATO="es.quatroges.qgestpv_v1.action.ERROR_SIGUEPLATO";
    public static final String ACTION_ERROR_RECUPERAFACTURAS="es.quatroges.qgestpv_v1.action.ERROR_RECUPERAFACTURAS";
    public static final String ACTION_ERROR_RECUPERAFACTURA="es.quatroges.qgestpv_v1.action.ERROR_RECUPERAFACTURA";
    public static final String ACTION_ERROR_IMPRIMEFACTURA="es.quatroges.qgestpv_v1.action.ERROR_IMPRIMEFACTURA";

    public static final String ACTION_ERROR_BLOQUEADO ="es.quatroges.qgestpv_v1.action.ERROR_BLOQUEADO";



    private static final String EXTRA_DISPOSITIVO = "es.quatroges.qgestpv_v1.extra.DISPOSITIVO";
    private static final String EXTRA_TABLA= "es.quatroges.qgestpv_v1.extra.TABLA";
    private static final String EXTRA_CODIGOS= "es.quatroges.qgestpv_v1.extra.CODIGOS";


    private static final String EXTRA_CODEMP = "es.quatroges.qgestpv_v1.extra.CODEMP";
    private static final String EXTRA_IDTABLET = "es.quatroges.qgestpv_v1.extra.IDTABLET";
    private static final String EXTRA_TPV = "es.quatroges.qgestpv_v1.extra.TPV";
    private static final String EXTRA_MESA = "es.quatroges.qgestpv_v1.extra.MESA";
    private static final String EXTRA_SUBMESA = "es.quatroges.qgestpv_v1.extra.SUBMESA";
    private static final String EXTRA_ACTUALIZAPAX = "es.quatroges.qgestpv_v1.extra.ACTUALIZAPAX";
    private static final String EXTRA_NFACTURA= "es.quatroges.qgestpv_v1.extra.NFACTURA";
    private static final String EXTRA_LINEASGRABAR= "es.quatroges.qgestpv_v1.extra.LINEASGRABAR";
    private static final String EXTRA_MESADEST= "es.quatroges.qgestpv_v1.extra.MESADEST";
    private static final String EXTRA_SUBMESAS = "es.quatroges.qgestpv_v1.extra.SUBMESAS";

    private static final String EXTRA_NRESERVA = "es.quatroges.qgestpv_v1.extra.NRESERVA";
    private static final String EXTRA_FORMAPAGO= "es.quatroges.qgestpv_v1.extra.FORMAPAGO";
    private static final String EXTRA_IMPORTE = "es.quatroges.qgestpv_v1.extra.IMPORTE";
    private static final String EXTRA_IGIC = "es.quatroges.qgestpv_v1.extra.IGIC";
    private static final String EXTRA_EFECTIVO = "es.quatroges.qgestpv_v1.extra.EFECTIVO";
    private static final String EXTRA_ENTREGADO= "es.quatroges.qgestpv_v1.extra.ENTREGADO";
    private static final String EXTRA_CAMBIO = "es.quatroges.qgestpv_v1.extra.CAMBIO";
    private static final String EXTRA_TARJETA = "es.quatroges.qgestpv_v1.extra.TARJETA";
    private static final String EXTRA_CUENTACASA = "es.quatroges.qgestpv_v1.extra.CUENTACASA";
    private static final String EXTRA_CREDITO = "es.quatroges.qgestpv_v1.extra.CREDITO";
    private static final String EXTRA_CODCLICASA = "es.quatroges.qgestpv_v1.extra.CODCLICASA";
    private static final String EXTRA_CODUSU= "es.quatroges.qgestpv_v1.extra.CODUSU";

    private static final String EXTRA_TAGID= "es.quatroges.qgestpv_v1.extra.TAGID";
    private static final String EXTRA_ROOM= "es.quatroges.qgestpv_v1.extra.ROOM";
    private static final String EXTRA_CODEMP_EXT= "es.quatroges.qgestpv_v1.extra.CODEMP_EXT";

    private static final String EXTRA_FIRMA= "es.quatroges.qgestpv_v1.extra.FIRMA";
    private static final String EXTRA_APTO= "es.quatroges.qgestpv_v1.extra.APTO";
    private static final String EXTRA_IMPRIMIR= "es.quatroges.qgestpv_v1.extra.IMPRIMIR";
    private static final String EXTRA_CAMARERO= "es.quatroges.qgestpv_v1.extra.CAMARERO";

    private static final String EXTRA_SIGUEPLATO= "es.quatroges.qgestpv_v1.extra.SIGUEPLATO";
    private static final String EXTRA_PAX = "es.quatroges.qgestpv_v1.extra.PAX";
    private static final String EXTRA_CODENL = "es.quatroges.qgestpv_v1.extra.CODENL";

    private static Handler handlerNotificaWS = null;
    private static Activity activity = null;

    private static Context context = null;

    private static ArrayList<Integer> registros;
    private static ArrayList<Integer> registrosDel;
    private static ArrayList<Rangos> rangosDel;
    private static UltimaSync ultimaSync;



    private static ClaseConfiguracion config;

    public static void setConfig(ClaseConfiguracion config) {
        ServSincronizaBD.config = config;
    }

    public ServSincronizaBD() {
        super("sincronizaBD");
        ultimaSync = UltimaSync.getInstance();
    }
    //endregion

    //region METODOS GENERALES Y ACCESO A API FUNCIONES  SINCRO TABLAS OFFLINE
    public static void startActionGetEstadoJson(Context context, Handler handler, Activity activity) {
        Intent intent = new Intent(context, ServSincronizaBD.class);
        intent.setAction(ACTION_GETTESTJSON);
        context.startService(intent);
        ServSincronizaBD.handlerNotificaWS = handler;
        ServSincronizaBD.activity = activity;
        ServSincronizaBD.context = context;

    }

    public static void startActionSincronizaTabla(Context context, String dispositivo,  String codemp,String tabla, Handler handler, Activity activity) {
        ServSincronizaBD.context = context;
        ServSincronizaBD.activity = activity;
        ServSincronizaBD.handlerNotificaWS = handler;

        if (!ultimaSync.tabla.equals(tabla) )
            ultimaSync = new UltimaSync(tabla,0,-1,0,0,0,0, false);


        if (registros == null )
            registros = new ArrayList<>();

        if (registrosDel == null )
            registrosDel = new ArrayList<>();

        if (rangosDel== null )
            rangosDel= new ArrayList<>();

        registros.clear();
        registrosDel.clear();
        rangosDel.clear();

        Intent intent = new Intent(context, ServSincronizaBD.class);
        intent.setAction(ACTION_GETMD5);
        intent.putExtra(EXTRA_DISPOSITIVO, dispositivo);
        intent.putExtra(EXTRA_CODEMP, codemp);
        intent.putExtra(EXTRA_TABLA, tabla);
        context.startService(intent);

    }


    public static void startActionRecuperaTabla(Context context, String tabla, String dispositivo, String codemp, ArrayList<Integer> $codigos, Handler handler, Activity activity) {
        ServSincronizaBD.context = context;
        ServSincronizaBD.activity = activity;
        ServSincronizaBD.handlerNotificaWS = handler;

        Intent intent = new Intent(context, ServSincronizaBD.class);
        intent.setAction(ACTION_GETTABLA);
        intent.putExtra(EXTRA_TABLA, tabla);
        intent.putExtra(EXTRA_DISPOSITIVO, dispositivo);
        intent.putExtra(EXTRA_CODEMP, codemp);
        intent.putExtra(EXTRA_CODIGOS, $codigos);
        context.startService(intent);

    }
//endregion
    //region METODOS GENERALES Y ACCESO A API FUNCIONES ONLINE


    public static void startActionGetEstadoMesas(Context context, String codemp, String idTablet, String tpv, Handler handler, Activity activity) {
        Intent intent = new Intent(context, ServSincronizaBD.class);
        intent.setAction(ACTION_GETESTADOMESAS);
        intent.putExtra(EXTRA_CODEMP, codemp);
        intent.putExtra(EXTRA_IDTABLET, idTablet);
        intent.putExtra(EXTRA_TPV, tpv);
        context.startService(intent);
        ServSincronizaBD.handlerNotificaWS = handler;
        ServSincronizaBD.activity = activity;
        ServSincronizaBD.context = context;
    }

    public static void startActionGetLineasMesa(Context context, String codemp, String idTablet, String tpv, String mesa, String room,Handler handler, Activity activity) {
        Intent intent = new Intent(context, ServSincronizaBD.class);
        intent.setAction(ACTION_GETLINEASMESA);
        intent.putExtra(EXTRA_CODEMP, codemp);
        intent.putExtra(EXTRA_IDTABLET, idTablet);
        intent.putExtra(EXTRA_TPV, tpv);
        intent.putExtra(EXTRA_MESA, mesa);
        intent.putExtra(EXTRA_ROOM, room);
        context.startService(intent);
        ServSincronizaBD.handlerNotificaWS = handler;
        ServSincronizaBD.activity = activity;
        ServSincronizaBD.context = context;

    }

    public static void startActionGetPension(Context context, String codemp, String idTablet, String room, String codenl,Handler handler, Activity activity) {
        Intent intent = new Intent(context, ServSincronizaBD.class);
        intent.setAction(ACTION_GETPENSION);
        intent.putExtra(EXTRA_CODEMP, codemp);
        intent.putExtra(EXTRA_IDTABLET, idTablet);
        intent.putExtra(EXTRA_ROOM, room);
        intent.putExtra(EXTRA_CODENL, codenl);

        context.startService(intent);
        ServSincronizaBD.handlerNotificaWS = handler;
        ServSincronizaBD.activity = activity;
        ServSincronizaBD.context = context;

    }


    public static void startActionEnviaLineasMesa(Context context, String codemp, String idTablet, String tpv, String mesa, ArrayList<ActualizaPax>submesas, ArrayList<GrabaLineasVenta> lineasgrabar,String imprimir, String camarero,Handler handler, Activity activity) {
        Intent intent = new Intent(context, ServSincronizaBD.class);
        intent.setAction(ACTION_PUTLINEASMESA);
        intent.putExtra(EXTRA_CODEMP, codemp);
        intent.putExtra(EXTRA_IDTABLET, idTablet);
        intent.putExtra(EXTRA_TPV, tpv);
        intent.putExtra(EXTRA_MESA,mesa);
        intent.putParcelableArrayListExtra(EXTRA_ACTUALIZAPAX,submesas);
        intent.putParcelableArrayListExtra(EXTRA_LINEASGRABAR,lineasgrabar);
        intent.putExtra(EXTRA_IMPRIMIR,imprimir);
        intent.putExtra(EXTRA_CAMARERO,camarero);
        context.startService(intent);
        ServSincronizaBD.handlerNotificaWS = handler;
        ServSincronizaBD.activity = activity;
        ServSincronizaBD.context = context;

    }

    public static void startActionActualizaPax(Context context, String codemp, String idTablet, ArrayList<ActualizaPax>submesas,  Handler handler, Activity activity) {
        Intent intent = new Intent(context, ServSincronizaBD.class);
        intent.setAction(ACTION_ACTUALIZAPAX);
        intent.putExtra(EXTRA_CODEMP, codemp);
        intent.putExtra(EXTRA_IDTABLET, idTablet);
        intent.putParcelableArrayListExtra(EXTRA_ACTUALIZAPAX,submesas);
        context.startService(intent);

        ServSincronizaBD.handlerNotificaWS = handler;
        ServSincronizaBD.activity = activity;
        ServSincronizaBD.context = context;

    }

    public static void startActionSiguePlato(Context context, String codemp, String idTablet, String tpv, String mesa, int orden, int pax,Handler handler, Activity activity) {
        Intent intent = new Intent(context, ServSincronizaBD.class);
        intent.setAction(ACTION_SIGUE_PLATO);
        intent.putExtra(EXTRA_CODEMP, codemp);
        intent.putExtra(EXTRA_IDTABLET, idTablet);
        intent.putExtra(EXTRA_TPV, tpv);
        intent.putExtra(EXTRA_MESA, mesa);
        intent.putExtra(EXTRA_SIGUEPLATO, String.valueOf(orden));
        intent.putExtra(EXTRA_PAX, String.valueOf(pax));

        context.startService(intent);

        ServSincronizaBD.handlerNotificaWS = handler;
        ServSincronizaBD.activity = activity;
        ServSincronizaBD.context = context;

    }

    public static void startActionTraspasaMesa(Context context, String codemp, String idTablet, String tpv, String mesa, ArrayList<String>submesas, String mesadest, Handler handler, Activity activity) {
        Intent intent = new Intent(context, ServSincronizaBD.class);
        intent.setAction(ACTION_TRASPASAMESA);
        intent.putExtra(EXTRA_CODEMP, codemp);
        intent.putExtra(EXTRA_IDTABLET, idTablet);
        intent.putExtra(EXTRA_TPV, tpv);
        intent.putExtra(EXTRA_MESA, mesa);
        intent.putStringArrayListExtra (EXTRA_SUBMESAS,submesas);
        intent.putExtra(EXTRA_MESADEST, mesadest);
        context.startService(intent);

        ServSincronizaBD.handlerNotificaWS = handler;
        ServSincronizaBD.activity = activity;
        ServSincronizaBD.context = context;
    }

    public static void startActionPedirCuenta(Context context, String codemp, String idTablet, String tpv, String mesa, String submesa,
                    String nfactura, String codusu, String imprimir,Handler handler, Activity activity) {
        Intent intent = new Intent(context, ServSincronizaBD.class);
        intent.setAction(ACTION_PEDIRCUENTA);
        intent.putExtra(EXTRA_CODEMP, codemp);
        intent.putExtra(EXTRA_IDTABLET, idTablet);
        intent.putExtra(EXTRA_TPV, tpv);
        intent.putExtra(EXTRA_MESA, mesa);
        intent.putExtra(EXTRA_SUBMESA, submesa);
        intent.putExtra(EXTRA_NFACTURA, nfactura);
        intent.putExtra(EXTRA_CODUSU, codusu);
        intent.putExtra(EXTRA_IMPRIMIR, imprimir);
        context.startService(intent);

        ServSincronizaBD.handlerNotificaWS = handler;
        ServSincronizaBD.activity = activity;
        ServSincronizaBD.context = context;
    }

    public static void startActionCreditoTagID(Context context, String codemp, String idTablet, String codemp_ext,String tagID, Handler handler, Activity activity) {
        Intent intent = new Intent(context, ServSincronizaBD.class);
        intent.setAction(ACTION_CREDITO_TAGID);
        intent.putExtra(EXTRA_CODEMP, codemp);
        intent.putExtra(EXTRA_IDTABLET, idTablet);
        intent.putExtra(EXTRA_TAGID, tagID);
        intent.putExtra(EXTRA_CODEMP_EXT, codemp_ext);

        context.startService(intent);

        ServSincronizaBD.handlerNotificaWS = handler;
        ServSincronizaBD.activity = activity;
        ServSincronizaBD.context = context;
    }

    public static void startActionCreditoRoom(Context context, String codemp, String idTablet,String codemp_ext, String room , Handler handler, Activity activity) {
        Intent intent = new Intent(context, ServSincronizaBD.class);
        intent.setAction(ACTION_CREDITO_ROOM);
        intent.putExtra(EXTRA_CODEMP, codemp);
        intent.putExtra(EXTRA_IDTABLET, idTablet);
        intent.putExtra(EXTRA_ROOM,room);
        intent.putExtra(EXTRA_CODEMP_EXT,codemp_ext);

        context.startService(intent);

        ServSincronizaBD.handlerNotificaWS = handler;
        ServSincronizaBD.activity = activity;
        ServSincronizaBD.context = context;
    }




    public static void startActionPagaMesa(Context context, String codemp, String idTablet,String codusu, String tpv, String mesa, String submesa,String codemp_ext, String nreserva, String formapago,
                String importe ,String igic, String efectivo, String tarjeta, String cuentacasa, String credito, String  nfactura, String  codclicasa,String entregado,String cambio,
                String firma, String apto, String imprimir,Handler handler, Activity activity) {
        Intent intent = new Intent(context, ServSincronizaBD.class);
        intent.setAction(ACTION_PAGAMESA);
        intent.putExtra(EXTRA_CODEMP, codemp);
        intent.putExtra(EXTRA_IDTABLET, idTablet);
        intent.putExtra(EXTRA_CODUSU, codusu);
        intent.putExtra(EXTRA_TPV, tpv);
        intent.putExtra(EXTRA_MESA, mesa);
        intent.putExtra(EXTRA_SUBMESA, submesa);
        intent.putExtra(EXTRA_CODEMP_EXT, codemp_ext);
        intent.putExtra(EXTRA_NRESERVA, nreserva);
        intent.putExtra(EXTRA_FORMAPAGO, formapago);
        intent.putExtra(EXTRA_IMPORTE, importe);
        intent.putExtra(EXTRA_IGIC, igic);
        intent.putExtra(EXTRA_EFECTIVO, efectivo);
        intent.putExtra(EXTRA_ENTREGADO, entregado);
        intent.putExtra(EXTRA_CAMBIO, cambio);
        intent.putExtra(EXTRA_TARJETA, tarjeta);
        intent.putExtra(EXTRA_CUENTACASA, cuentacasa);
        intent.putExtra(EXTRA_CREDITO, credito);
        intent.putExtra(EXTRA_NFACTURA, nfactura);
        intent.putExtra(EXTRA_CODCLICASA, codclicasa);
        intent.putExtra(EXTRA_FIRMA,firma);
        intent.putExtra(EXTRA_APTO,apto);
        intent.putExtra(EXTRA_IMPRIMIR,imprimir);

        context.startService(intent);

        ServSincronizaBD.handlerNotificaWS = handler;
        ServSincronizaBD.activity = activity;
        ServSincronizaBD.context = context;
    }

    public static void startActionRecuperaFacturas(Context context, String codemp, String idTablet, String tpv, Handler handler, Activity activity) {
        Intent intent = new Intent(context, ServSincronizaBD.class);
        intent.setAction(ACTION_RECUPERA_FACTURAS);
        intent.putExtra(EXTRA_CODEMP, codemp);
        intent.putExtra(EXTRA_IDTABLET, idTablet);
        intent.putExtra(EXTRA_TPV, tpv);

        context.startService(intent);

        ServSincronizaBD.handlerNotificaWS = handler;
        ServSincronizaBD.activity = activity;
        ServSincronizaBD.context = context;
    }

    public static void startActionRecuperaFactura(Context context, String codemp, String idTablet, String codenl, Handler handler, Activity activity) {
        Intent intent = new Intent(context, ServSincronizaBD.class);
        intent.setAction(ACTION_RECUPERA_FACTURA);
        intent.putExtra(EXTRA_CODEMP, codemp);
        intent.putExtra(EXTRA_IDTABLET, idTablet);
        intent.putExtra(EXTRA_CODENL, codenl);

        context.startService(intent);

        ServSincronizaBD.handlerNotificaWS = handler;
        ServSincronizaBD.activity = activity;
        ServSincronizaBD.context = context;
    }

    public static void startActionImprimeFactura(Context context, String codemp, String idTablet, String codenl, Handler handler, Activity activity) {
        Intent intent = new Intent(context, ServSincronizaBD.class);
        intent.setAction(ACTION_IMPRIME_FACTURA);
        intent.putExtra(EXTRA_CODEMP, codemp);
        intent.putExtra(EXTRA_IDTABLET, idTablet);
        intent.putExtra(EXTRA_CODENL, codenl);

        context.startService(intent);

        ServSincronizaBD.handlerNotificaWS = handler;
        ServSincronizaBD.activity = activity;
        ServSincronizaBD.context = context;
    }

    //endregion

    //region HANDLERS SERVICIO WEB
    public static void setHandlerNotificaWS(Handler handler) {
        if (handlerNotificaWS != null) handlerNotificaWS = null;
        handlerNotificaWS= handler;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GETTESTJSON.equals(action)) {
                handleActionGetTestJson();
            }

            if (ACTION_GETMD5.equals(action)) {
                final String dispositivo = intent.getStringExtra(EXTRA_DISPOSITIVO);
                final String codemp = intent.getStringExtra(EXTRA_CODEMP);
                final String tabla = intent.getStringExtra(EXTRA_TABLA);
                handleActionGetMD5(dispositivo, codemp,tabla);
            }
            if (ACTION_GETTABLA.equals(action)) {
                final String dispositivo = intent.getStringExtra(EXTRA_DISPOSITIVO);
                final String codemp = intent.getStringExtra(EXTRA_CODEMP);
                final ArrayList<String> codigos = (ArrayList<String>) intent.getSerializableExtra(EXTRA_CODIGOS);

                final String tabla = intent.getStringExtra(EXTRA_TABLA);
                handleActionGetTabla(dispositivo, codemp,codigos, tabla);
            }



            if (ACTION_GETESTADOMESAS.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_CODEMP);
                final String param2 = intent.getStringExtra(EXTRA_IDTABLET);
                final String param3 = intent.getStringExtra(EXTRA_TPV);
                handleActionGetEstadoMesas(param1, param2, param3);
            }
            if (ACTION_GETLINEASMESA.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_CODEMP);
                final String param2 = intent.getStringExtra(EXTRA_IDTABLET);
                final String param3 = intent.getStringExtra(EXTRA_TPV);
                final String param4 = intent.getStringExtra(EXTRA_MESA);
                final String param5 = intent.getStringExtra(EXTRA_ROOM);
                handleActionGetLineasMesa(param1, param2, param3,param4,param5);
            }
            if (ACTION_GETPENSION.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_CODEMP);
                final String param2 = intent.getStringExtra(EXTRA_IDTABLET);
                final String param3 = intent.getStringExtra(EXTRA_ROOM);
                final String param4 = intent.getStringExtra(EXTRA_CODENL);

                handleActionGetPension(param1, param2, param3,param4);
            }
            if (ACTION_PUTLINEASMESA.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_CODEMP);
                final String param2 = intent.getStringExtra(EXTRA_IDTABLET);
                final String param3 = intent.getStringExtra(EXTRA_TPV);
                final String param4 = intent.getStringExtra(EXTRA_MESA);
                final ArrayList<ActualizaPax> param5 = intent.getParcelableArrayListExtra(EXTRA_ACTUALIZAPAX);
                final ArrayList<GrabaLineasVenta> param6 = intent.getParcelableArrayListExtra(EXTRA_LINEASGRABAR);
                final String param7 = intent.getStringExtra(EXTRA_IMPRIMIR);
                final String param8 = intent.getStringExtra(EXTRA_CAMARERO);

                handleActionPutLineasMesa(param1, param2, param3,param4,param5,param6,param7, param8);
            }
            if (ACTION_PEDIRCUENTA.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_CODEMP);
                final String param2 = intent.getStringExtra(EXTRA_IDTABLET);
                final String param3 = intent.getStringExtra(EXTRA_TPV);
                final String param4 = intent.getStringExtra(EXTRA_MESA);
                final String param5 = intent.getStringExtra(EXTRA_SUBMESA);
                final String param6 = intent.getStringExtra(EXTRA_NFACTURA);
                final String param7 = intent.getStringExtra(EXTRA_CODUSU);
                final String param8 = intent.getStringExtra(EXTRA_IMPRIMIR);
                handleActionPedirCuenta(param1, param2, param3,param4, param5,param6, param7,param8);
            }
            if (ACTION_ACTUALIZAPAX.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_CODEMP);
                final String param2 = intent.getStringExtra(EXTRA_IDTABLET);
                final ArrayList<ActualizaPax> param3 = intent.getParcelableArrayListExtra(EXTRA_ACTUALIZAPAX);
                handleActionActualizaPax(param1, param2, param3);
            }

            if (ACTION_TRASPASAMESA.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_CODEMP);
                final String param2 = intent.getStringExtra(EXTRA_IDTABLET);
                final String param3 = intent.getStringExtra(EXTRA_TPV);
                final String param4 = intent.getStringExtra(EXTRA_MESA);
                final ArrayList<String> param5 = intent.getStringArrayListExtra(EXTRA_SUBMESAS);
                final String param6 = intent.getStringExtra(EXTRA_MESADEST);
                handleActionTraspasaMesa(param1, param2, param3,param4,param5,param6);
            }

            if (ACTION_PAGAMESA.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_CODEMP);
                final String param2 = intent.getStringExtra(EXTRA_IDTABLET);
                final String param3 = intent.getStringExtra(EXTRA_TPV);
                final String param4 = intent.getStringExtra(EXTRA_MESA);
                final String param5 = intent.getStringExtra(EXTRA_SUBMESA);
                final String param6 = intent.getStringExtra(EXTRA_CODEMP_EXT);
                final String param7 = intent.getStringExtra(EXTRA_NRESERVA);
                final String param8 = intent.getStringExtra(EXTRA_FORMAPAGO);
                final String param9 = intent.getStringExtra(EXTRA_IMPORTE);
                final String param10 = intent.getStringExtra(EXTRA_IGIC);
                final String param11 = intent.getStringExtra(EXTRA_EFECTIVO);
                final String param12 = intent.getStringExtra(EXTRA_TARJETA);
                final String param13 = intent.getStringExtra(EXTRA_CUENTACASA);
                final String param14 = intent.getStringExtra(EXTRA_CREDITO);
                final String param15 = intent.getStringExtra(EXTRA_NFACTURA);
                final String param16 = intent.getStringExtra(EXTRA_CODCLICASA);
                final String param17 = intent.getStringExtra(EXTRA_CODUSU);
                final String param18 = intent.getStringExtra(EXTRA_ENTREGADO);
                final String param19 = intent.getStringExtra(EXTRA_CAMBIO);
                final String param20 = intent.getStringExtra(EXTRA_FIRMA);
                final String param21 = intent.getStringExtra(EXTRA_APTO);
                final String param22 = intent.getStringExtra(EXTRA_IMPRIMIR);

                handleActionPagaMesa(param1, param2, param3,param4,param5,param6,param7,param8,param9,param10,param11,param12,param13,param14,param15,param16,param17,param18,param19,param20, param21,param22);
            }

            if (ACTION_CREDITO_TAGID.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_CODEMP);
                final String param2 = intent.getStringExtra(EXTRA_IDTABLET);
                final String param3 = intent.getStringExtra(EXTRA_TAGID);
                final String param4 = intent.getStringExtra(EXTRA_CODEMP_EXT);

                handleActionCreditoTagID(param1, param2, param3,param4);
            }

            if (ACTION_CREDITO_ROOM.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_CODEMP);
                final String param2 = intent.getStringExtra(EXTRA_IDTABLET);
                final String param3 = intent.getStringExtra(EXTRA_ROOM);
                final String param4 = intent.getStringExtra(EXTRA_CODEMP_EXT);

                handleActionCreditoRoom(param1, param2, param3,param4);
            }

            if (ACTION_SIGUE_PLATO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_CODEMP);
                final String param2 = intent.getStringExtra(EXTRA_IDTABLET);
                final String param3 = intent.getStringExtra(EXTRA_TPV);
                final String param4 = intent.getStringExtra(EXTRA_MESA);
                final String param5 = intent.getStringExtra(EXTRA_SIGUEPLATO);
                final String param6 = intent.getStringExtra(EXTRA_PAX);

                handleActionSiguePlato(param1, param2, param3,param4,param5,param6);
            }

            if (ACTION_RECUPERA_FACTURAS.equals(action)){
                final String param1 = intent.getStringExtra(EXTRA_CODEMP);
                final String param2 = intent.getStringExtra(EXTRA_IDTABLET);
                final String param3 = intent.getStringExtra(EXTRA_TPV);

                handleActionRecuperaFacturas(param1, param2, param3);

            }

            if (ACTION_RECUPERA_FACTURA.equals(action)){
                final String param1 = intent.getStringExtra(EXTRA_CODEMP);
                final String param2 = intent.getStringExtra(EXTRA_IDTABLET);
                final String param3 = intent.getStringExtra(EXTRA_CODENL);

                handleActionRecuperaFactura(param1, param2, param3);

            }

            if (ACTION_IMPRIME_FACTURA.equals(action)){
                final String param1 = intent.getStringExtra(EXTRA_CODEMP);
                final String param2 = intent.getStringExtra(EXTRA_IDTABLET);
                final String param3 = intent.getStringExtra(EXTRA_CODENL);

                handleActionImprimeFactura(param1, param2, param3);

            }

        }
    }

    private void handleActionGetTestJson() {
        recuperaTestJsonWS();
    }

    private void handleActionGetMD5(String dispositivo,  String codemp, String tabla) {
        recuperaRegistrosMD5WS(dispositivo,codemp,tabla);
    }

    private void handleActionGetTabla(String dispositivo,  String codemp, ArrayList<String> codigos, String tabla) {
        recuperaCamposTablaWS(codemp,dispositivo,tabla.toLowerCase(),codigos);
    }


    private void handleActionGetEstadoMesas(String codemp, String idTablet,String tpv) {
        recuperaEstadoMesas(codemp,idTablet, tpv);
    }

    private void handleActionGetLineasMesa(String codemp, String idTablet,String tpv, String mesa, String room) {
        recuperaLineasMesa(codemp,idTablet, tpv,mesa,room);
    }

    private void handleActionGetPension(String codemp, String idTablet, String room, String codenl) {
        recuperaPension(codemp,idTablet,room, codenl);
    }

    private void handleActionPutLineasMesa(String codemp, String idTablet,String tpv,String mesa,ArrayList<ActualizaPax> submesas,ArrayList<GrabaLineasVenta> lineasVentas, String imprimir, String camarero) {
        grabaLineasMesa(codemp,idTablet, tpv,mesa,submesas, lineasVentas, imprimir,camarero);
    }

    private void handleActionActualizaPax(String codemp, String idTablet,ArrayList<ActualizaPax> submesas ) {
        actualizarPax(codemp,idTablet, submesas);
    }

    private void handleActionSiguePlato(String codemp, String idTablet,String tpv, String mesa, String orden, String pax) {
        avisoSeguirPlato(codemp,idTablet, tpv,mesa,orden, pax);
    }

    private void handleActionTraspasaMesa(String codemp, String idTablet,String tpv, String mesa, ArrayList<String> submesas, String mesadest ) {
        traspasaMesa(codemp,idTablet,tpv,mesa,submesas,mesadest);
    }

    private void handleActionPedirCuenta(String codemp, String idTablet,String tpv, String mesa, String submesa,String nfactura, String codusu, String imprimir) {
        pedirCuenta(codemp,idTablet, tpv,mesa,submesa,nfactura, codusu, imprimir);
    }

    private void handleActionCreditoTagID(String codemp, String idTablet,String tagID,String codemp_ext) {
        recuperaCredigoTagID(codemp,idTablet, tagID, codemp_ext);
    }

    private void handleActionCreditoRoom(String codemp, String idTablet,String room,String codemp_ext) {
        recuperaCredigoRoom(codemp,idTablet, room, codemp_ext);
    }

    private void handleActionPagaMesa(String codemp, String idTablet,String tpv, String mesa, String submesa,String codemp_ext, String nreserva, String formapago, String importe, String igic,
                                      String efectivo, String tarjeta, String cuentacasa, String credito, String nfactura, String codclicasa, String codusu, String entregado,
                                      String cambio, String firma, String apto,String imprimir) {

        pagaMesa(codemp,idTablet,tpv,mesa,submesa,codemp_ext, nreserva, formapago, importe, igic,
                efectivo, tarjeta, cuentacasa, credito, nfactura, codclicasa, codusu,entregado, cambio, firma, apto, imprimir);
    }

    private void handleActionRecuperaFacturas(String codemp, String idTablet,String tpv) {
        recuperaFacturas(codemp,idTablet, tpv);
    }

    private void handleActionRecuperaFactura(String codemp, String idTablet,String codenl) {
        recuperaFactura(codemp,idTablet, codenl);
    }
    private void handleActionImprimeFactura(String codemp, String idTablet,String codenl) {
        imprimeFactura(codemp,idTablet, codenl);
    }
    //endregion



    //region FUNCIONES SINCRONIZA TABLAS OFFLINE
    private void recuperaRegistrosMD5WS(final String dispositivo, final String codemp, final String tabla    ){

        if(!ultimaSync.tabla.equalsIgnoreCase(tabla)){
            ultimaSync.iniciaValores(tabla,0,-1,0,0,0,0,false);
        }


        if (activity != null && handlerNotificaWS != null) activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Message mensaje = handlerNotificaWS.obtainMessage(ACTION_DESCARGANDO_REGISTROS);
                Bundle bundle = new Bundle();
                bundle.putString("tabla", tabla);
                bundle.putInt("reg",ultimaSync.offset);
                bundle.putInt("regs",ultimaSync.totalAnterior);
                mensaje.setData(bundle);
                handlerNotificaWS.sendMessage(mensaje);



            }
        });

        APIService apiService = ClaseServicioWeb.getAPIService(false, config.connectionTO,config.rwTO2,config.rwTO2, ActivityInicio.latenciaTest.getUltima());

        apiService.recuperaRegistrosMD5(dispositivo,codemp,tabla,ultimaSync.offset).enqueue(new Callback<RespuestaRegistrosMD5WS>() {
            @Override
            public void onResponse(Call<RespuestaRegistrosMD5WS> call, Response<RespuestaRegistrosMD5WS> response) {
                int errnum =Integer.valueOf(response.body().getErrnum());
                if (errnum != 0) {
                    Intent bcIntent = new Intent();
                    bcIntent.setPackage(context.getPackageName());
                    bcIntent.setAction(ACTION_ERROR_MD5);
                    bcIntent.putExtra("tabla",tabla);
                    context.sendBroadcast(bcIntent);
                    return;
                }

                final int total = response.body().getTotalregs();
                int max = response.body().getMaxid();
                int min = response.body().getMinid();


                // REGISTROS REMOTOS DESCARGADOS
                final List<roomRegistrosCRC> md5s = response.body().getCupos();


                LinkedHashMap<Integer,String> wsMD5 = (md5s != null && !md5s.isEmpty() )? md5s.stream()
                        .collect(Collectors.toMap(
                                roomRegistrosCRC::getIdfila,
                                roomRegistrosCRC::getCrc,
                                (existing, replacement) -> replacement,
                                LinkedHashMap::new)):new LinkedHashMap<Integer, String>();

                //REGISTROS LOCALES RECUPERADOS
                ClaseBaseDatos roomBD = ClaseBaseDatos.getDatabase(context,null,activity);
                List<roomRegistrosCRC> tMD5 = null;
                try {
                    if (md5s != null &&  md5s.size() > 0 ) {
                        tMD5 =roomBD.recuperaMD5(tabla.toLowerCase(),md5s.get(0).getIdfila(), md5s.get(md5s.size()-1).getIdfila());
                    }
                }
                catch (ExecutionException | InterruptedException e1){
                    Log.d("ServSincronizaBF","recuperaRegsitrosMD%WS onResponse:  "+e1.getMessage());
                }

                LinkedHashMap<Integer,String> localMD5 = ( tMD5 != null && !tMD5.isEmpty()  )? tMD5.stream()
                        .collect(Collectors.toMap(
                                roomRegistrosCRC::getIdfila,
                                roomRegistrosCRC::getCrc,
                                (existing, replacement) -> replacement,
                                LinkedHashMap::new)):new LinkedHashMap<Integer, String>();

                // borrar registros entre ultimoID +1 y idfila[0]
                if (md5s == null || md5s.size() == 0) {
                    borraTabla(tabla);
                }
                else if (md5s.size() > 0  && (md5s.get(0).getIdfila()- (ultimaSync.ultimoID +1))>0) {
                    rangosDel.add(new Rangos(ultimaSync.ultimoID + 1, md5s.get(0).getIdfila()-1));
                }

                // busco registros nuevos o modificados
                if (md5s != null ) {

                    registros = wsMD5.entrySet().stream().filter(
                            entry -> !localMD5.containsKey(entry.getKey())
                                    ||  !Objects.equals(localMD5.get(entry.getKey()),entry.getValue()))
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toCollection(ArrayList::new));

                }

                // busco registros no existentes
                registrosDel = localMD5.keySet().stream()
                        .filter(id -> !wsMD5.containsKey(id))
                        .collect(Collectors.toCollection(ArrayList::new));

                boolean ultimo = false;

                if (md5s != null && (ultimaSync.offset + md5s.size() <total) ){
                    ultimaSync.offsetAnterior = ultimaSync.getOffset();
                    ultimaSync.totalAnterior = total;

                    ultimaSync.setTabla(tabla);
                    ultimaSync.setOffset(ultimaSync.offset+md5s.size());
                    ultimaSync.setMaxID(max);
                    ultimaSync.setUltimoID(md5s.get(md5s.size() -1).getIdfila());
                    ultimaSync.setTotal(total);
                }
                else {
                    ultimaSync.offsetAnterior = ultimaSync.getOffset();
                    ultimaSync.totalAnterior = total;
                    ultimaSync.setTabla(tabla);
                    ultimaSync.setMaxID(-1);

                    if (md5s != null) {
                        ultimaSync.setUltimoID(md5s.get(md5s.size() - 1).getIdfila());
                    }
                    else {
                        ultimaSync.setUltimoID(0);
                    }
                    ultimaSync.setOffset(0);
                    ultimaSync.setTotal(0);
                    ultimaSync.finalizado =  true;


                    ultimo = true;
                    rangosDel.add(new Rangos(max+1,99999999));
                }

                if (!rangosDel.isEmpty())
                    borraRangos(tabla,rangosDel);
                if (!registrosDel.isEmpty())
                    borraRegistros(tabla,registrosDel);

                if (!registros.isEmpty()) {
                    startActionRecuperaTabla(context, tabla,dispositivo,  codemp, registros, handlerNotificaWS, activity);
                }
                else if (!ultimo){
                    if (activity != null && handlerNotificaWS != null) activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Message mensaje = handlerNotificaWS.obtainMessage(ACTION_GRABANDO_REGISTROS);
                            Bundle bundle = new Bundle();
                            bundle.putInt("pos", ultimaSync.offset);
                            bundle.putInt("nregistros", total);
                            bundle.putInt("max", max);
                            bundle.putBoolean("ultimo", true);
                            bundle.putString("tabla",tabla);
                            mensaje.setData(bundle);
                            handlerNotificaWS.sendMessage(mensaje);
                        }
                    });
                }
                else {
                    Intent bcIntent = new Intent();
                    bcIntent.setPackage(context.getPackageName());

                    bcIntent.setAction(ACTION_OK_MD5);
                    bcIntent.putExtra("tabla",tabla);
                    context.sendBroadcast(bcIntent);
                }
            }

            @Override
            public void onFailure(Call<RespuestaRegistrosMD5WS> call, Throwable t) {
                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.setAction(ACTION_ERROR_MD5);
                bcIntent.putExtra("tabla",tabla);
                context.sendBroadcast(bcIntent);
            }
        });
    }

    private void recuperaCamposTablaWS(String codemp, String idTablet,final String tabla,  final ArrayList<String> codigos){
        RequestRecuperaRegistrosWS request = prepararRequest(codemp, idTablet, tabla, codigos);
        ejecutarRecuperacionWS(getResponseApiService(request),tabla);
    }

    //endregion

    //region FUNCIONES ONLINE
    private void recuperaTestJsonWS(){
        APIService apiService = ClaseServicioWeb.getAPIService(false, config.connectionTO, config.rwTO1, config.rwTO1, ActivityInicio.latenciaTest.getUltima());
        apiService.recuperaTestJSON ().enqueue(new Callback<RespuestaTestjsonWS>() {
            @Override
            public void onResponse(Call<RespuestaTestjsonWS> call, Response<RespuestaTestjsonWS> response) {
                String estado = response.body().getEstado();
                String fecha = response.body().getFecha();
                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.setAction(ACTION_OK_TESTJSON);
                context.sendBroadcast(bcIntent);
            }

            @Override
            public void onFailure(Call<RespuestaTestjsonWS> call, Throwable t) {
                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.setAction(ACTION_ERROR_TESTJSON);
                context.sendBroadcast(bcIntent);
            }
        });
    }

    private void recuperaEstadoMesas(String codemp, String idTtablet, String tpv){
        APIService apiService = ClaseServicioWeb.getAPIService(false, config.connectionTO, config.rwTO2, config.rwTO2, ActivityInicio.latenciaTest.getUltima());

        final Long timeini = Calendar.getInstance().getTimeInMillis();
        apiService.recuperaEstadoMesas(codemp,idTtablet, tpv).enqueue(new Callback<RespuestaEstadoMesasWS>() {
            @Override
            public void onResponse(Call<RespuestaEstadoMesasWS> call, Response<RespuestaEstadoMesasWS> response) {

                if (response != null && response.body() != null) {

                    if (! response.body().getErrnum().equals("0")) {
                        Intent bcIntent = new Intent();
                        bcIntent.setPackage(context.getPackageName());

                        bcIntent.setAction(ACTION_ERROR_BLOQUEADO);
                        bcIntent.putExtra("mensaje",response.body().getErrdesc());
                        context.sendBroadcast(bcIntent);
                        return;
                    }

                    ClaseCondicionesVenta.pedir_pax = response.body().getPedir_pax();
                    List<EstadoMesas> tmesas = response.body().getMesas();
                    ArrayList<EstadoMesas> mesas = new ArrayList<>();
                    mesas.clear();
                    if (tmesas != null) {
                        for (EstadoMesas tmesa : tmesas) {
                            mesas.add(tmesa);
                        }
                    }

                    ActivityInicio.latenciaRecuperaMesas.add(response.raw().sentRequestAtMillis(),response.raw().receivedResponseAtMillis(),true);
                    if (context != null) {
                        Intent bcIntent = new Intent();
                        bcIntent.setPackage(context.getPackageName());

                        bcIntent.setAction(ACTION_OK_ESTADOMESAS);
                        bcIntent.putParcelableArrayListExtra("mesas", mesas);
                        context.sendBroadcast(bcIntent);
                    }
                }
                else {
                    Intent bcIntent = new Intent();
                    bcIntent.setPackage(context.getPackageName());

                    bcIntent.setAction(ACTION_ERROR_ESTADOMESAS);
                    context.sendBroadcast(bcIntent);

                }
            }

            @Override
            public void onFailure(Call<RespuestaEstadoMesasWS> call, Throwable t) {

                final Long timefin= Calendar.getInstance().getTimeInMillis();
                ActivityInicio.latenciaRecuperaMesas.add(timeini,timefin,false);

                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.setAction(ACTION_ERROR_ESTADOMESAS);
                context.sendBroadcast(bcIntent);
            }
        });
    }

    private void recuperaLineasMesa(String codemp, String idTtablet, String tpv,String mesa, String room){
        APIService apiService = ClaseServicioWeb.getAPIService(false, config.connectionTO, config.rwTO4, config.rwTO4, ActivityInicio.latenciaTest.getUltima());

        final Long timeini = Calendar.getInstance().getTimeInMillis();

        apiService.recuperaLineasMesa(codemp,idTtablet, tpv,mesa, room ).enqueue(new Callback<RespuestaLineasMesaWS>() {

            @Override
            public void onResponse(Call<RespuestaLineasMesaWS> call, Response<RespuestaLineasMesaWS> response) {
                ActivityInicio.latenciaRecuperaComanda.add(response.raw().sentRequestAtMillis(),response.raw().receivedResponseAtMillis(),true);
                if (response != null && response.body() != null && response.isSuccessful()) {
                    if (!response.body().getErrnum().equals("0")) {
                        Intent bcIntent = new Intent();
                        bcIntent.setPackage(context.getPackageName());

                        bcIntent.setAction(ACTION_ERROR_BLOQUEADO);
                        bcIntent.putExtra("mensaje", response.body().getErrdesc());
                        context.sendBroadcast(bcIntent);
                        return;
                    }

                    List<LineasVenta> tlineas = response.body().getLineas();
                    ArrayList<LineasVenta> lineas = new ArrayList<>();
                    lineas.clear();
                    if (tlineas != null) {
                        for (LineasVenta tlinea : tlineas) {
                            tlinea.setNfactura(tlinea.getNfactura().trim());
                            lineas.add(tlinea);
                        }
                    }

                    List<PensionesSubmesa> tPensiones = response.body().getPensiones();
                    ArrayList<PensionesSubmesa> pensiones = new ArrayList<>();
                    pensiones.clear();
                    if (tPensiones != null ){
                        for (PensionesSubmesa tPension : tPensiones){
                            pensiones.add(tPension);
                        }
                    }

                    Intent bcIntent = new Intent();
                    bcIntent.setAction(ACTION_OK_LINEASVENTA);
                    bcIntent.setPackage(context.getPackageName());

                    bcIntent.putParcelableArrayListExtra("lineas", lineas);
                    bcIntent.putParcelableArrayListExtra("pensiones", pensiones);
                    context.sendBroadcast(bcIntent);
                }
                else {
                    Intent bcIntent = new Intent();
                    bcIntent.setPackage(context.getPackageName());

                    bcIntent.setAction(ACTION_ERROR_LINEASVENTA);
                    context.sendBroadcast(bcIntent);
                }

            }

            @Override
            public void onFailure(Call<RespuestaLineasMesaWS> call, Throwable t) {
                final Long timefin= Calendar.getInstance().getTimeInMillis();
                ActivityInicio.latenciaRecuperaComanda.add(timeini,timefin,false);

                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.setAction(ACTION_ERROR_LINEASVENTA);
                context.sendBroadcast(bcIntent);
            }
        });
    }

    private void recuperaPension(String codemp, String idTtablet, String room, String codenl){
        APIService apiService = ClaseServicioWeb.getAPIService(false, config.connectionTO, config.rwTO4, config.rwTO4, ActivityInicio.latenciaTest.getUltima());

        final Long timeini = Calendar.getInstance().getTimeInMillis();

        apiService.recuperaPension(codemp,idTtablet, room, codenl ).enqueue(new Callback<RespuestaPensionWS>() {

            @Override
            public void onResponse(Call<RespuestaPensionWS> call, Response<RespuestaPensionWS> response) {
                ActivityInicio.latenciaRecuperaComanda.add(response.raw().sentRequestAtMillis(),response.raw().receivedResponseAtMillis(),true);
                if (response != null && response.body() != null && response.isSuccessful()) {
                    if (!response.body().getErrnum().equals("0")) {
                        Intent bcIntent = new Intent();
                        bcIntent.setPackage(context.getPackageName());

                        bcIntent.setAction(ACTION_ERROR_BLOQUEADO);
                        bcIntent.putExtra("mensaje", response.body().getErrdesc());
                        context.sendBroadcast(bcIntent);
                        return;
                    }

                    String pension = response.body().getPension();
                    String desc_pension = response.body().getDesc_pension();
                    String tipo_pension = response.body().getTipo_pension();
                    String desc_tipo_pension = response.body().getTipo_pension();
                    Intent bcIntent = new Intent();
                    bcIntent.setPackage(context.getPackageName());

                    bcIntent.setAction(ACTION_OK_PENSION);
                    bcIntent.putExtra("pension", pension);
                    bcIntent.putExtra("desc_pension", desc_pension);
                    bcIntent.putExtra("tipo_pension", tipo_pension);
                    bcIntent.putExtra("desc_tipo_pension", desc_tipo_pension);
                    context.sendBroadcast(bcIntent);
                }
                else {
                    Intent bcIntent = new Intent();
                    bcIntent.setPackage(context.getPackageName());

                    bcIntent.setAction(ACTION_ERROR_PENSION);
                    context.sendBroadcast(bcIntent);
                }

            }

            @Override
            public void onFailure(Call<RespuestaPensionWS> call, Throwable t) {
                final Long timefin= Calendar.getInstance().getTimeInMillis();
                ActivityInicio.latenciaRecuperaComanda.add(timeini,timefin,false);

                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.setAction(ACTION_ERROR_PENSION);
                context.sendBroadcast(bcIntent);
            }
        });
    }


    private void grabaLineasMesa(String codemp, String idTtablet, String tpv,String mesa, ArrayList<ActualizaPax> submesas, ArrayList<GrabaLineasVenta> lineasVentas, String imprimir, String camarero){
        APIService apiService = ClaseServicioWeb.getAPIService(false, config.connectionTO, config.rwTO5, config.rwTO5, ActivityInicio.latenciaTest.getUltima());
        RequestGrabaLineasWS requestGrabaLineasWS = new RequestGrabaLineasWS();
        requestGrabaLineasWS.setCodigoEmpresa(codemp);
        requestGrabaLineasWS.setIdTablet(idTtablet);
        requestGrabaLineasWS.setTpv(tpv);
        requestGrabaLineasWS.setMesa(mesa);
        requestGrabaLineasWS.setSubmesas(submesas);
        requestGrabaLineasWS.setLineas(lineasVentas);
        requestGrabaLineasWS.setImprimir(imprimir);
        requestGrabaLineasWS.setCamarero(camarero);


        Log.d("ServSincronizaBD","descuento: "+lineasVentas.get(0).getDescuento());
        final Long timeini = Calendar.getInstance().getTimeInMillis();

        apiService.grabaLineas(requestGrabaLineasWS).enqueue(new Callback<RespuestaGrabaLineasWS>() {

            @Override
            public void onResponse(Call<RespuestaGrabaLineasWS> call, Response<RespuestaGrabaLineasWS> response) {
                ActivityInicio.latenciaEnviaComanda.add(response.raw().sentRequestAtMillis(),response.raw().receivedResponseAtMillis(),true);

                List<ResGrabaLineasSubmesas> tsubmesas = response.body().getSubmesas();
                ArrayList<ResGrabaLineasSubmesas> submesas  = new ArrayList<>();
                submesas.clear();
                if (tsubmesas != null){
                    for (ResGrabaLineasSubmesas tsubmesa: tsubmesas){
                        submesas.add(tsubmesa);
                    }
                }

                List<ResGrabaLineas> tresultados= response.body().getResultados();
                ArrayList<ResGrabaLineas> resultados = new ArrayList<>();
                resultados.clear();
                if (tresultados!= null) {
                    for (ResGrabaLineas tresultado : tresultados) {
                        resultados.add(tresultado);
                    }
                }
                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.setAction(ACTION_OK_GRABALINEASVENTA);
                bcIntent.putParcelableArrayListExtra("resultados",resultados);
                bcIntent.putParcelableArrayListExtra("submesas",submesas);
                context.sendBroadcast(bcIntent);

            }

            @Override
            public void onFailure(Call<RespuestaGrabaLineasWS> call, Throwable t) {
                final Long timefin= Calendar.getInstance().getTimeInMillis();
                ActivityInicio.latenciaEnviaComanda.add(timeini,timefin,false);

                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.setAction(ACTION_ERROR_GRABALINEASVENTA);
                context.sendBroadcast(bcIntent);
            }
        });
    }

    private void actualizarPax(String codemp, String idTtablet, ArrayList<ActualizaPax> submesas) {
        APIService apiService = ClaseServicioWeb.getAPIService(false, config.connectionTO, config.rwTO2, config.rwTO2, ActivityInicio.latenciaTest.getUltima());
        RequestActualizaPaxWS requestActualizaPaxWS = new RequestActualizaPaxWS();
        requestActualizaPaxWS.setCodigoEmpresa(codemp);
        requestActualizaPaxWS.setIdTablet(idTtablet);
        requestActualizaPaxWS.setPax(submesas);

        apiService.actualizaPax(requestActualizaPaxWS).enqueue(new Callback<RespuestaBaseWS>() {

            @Override
            public void onResponse(Call<RespuestaBaseWS> call, Response<RespuestaBaseWS> response) {
                RespuestaBaseWS res = response.body();

                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                if (res.getErrnum().equals("0"))
                    bcIntent.setAction(ACTION_OK_ACTUALIZAPAX);
                else
                    bcIntent.setAction(ACTION_ERROR_ACTUALIZAPAX);

                context.sendBroadcast(bcIntent);

            }

            @Override
            public void onFailure(Call<RespuestaBaseWS> call, Throwable t) {
                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.setAction(ACTION_ERROR_ACTUALIZAPAX);
                context.sendBroadcast(bcIntent);
            }
        });
    }

    private void avisoSeguirPlato(String codemp, String idTtablet, String tpv,String mesa,String  orden, String pax){
        APIService apiService = ClaseServicioWeb.getAPIService(false, config.connectionTO, config.rwTO2, config.rwTO2, ActivityInicio.latenciaTest.getUltima());
        apiService.avisoSeguirPlato(codemp,idTtablet, tpv,mesa,orden, pax).enqueue(new Callback<RespuestaBaseWS>() {
            @Override
            public void onResponse(Call<RespuestaBaseWS> call, Response<RespuestaBaseWS> response) {
                RespuestaBaseWS res = response.body();

                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.setAction(ACTION_OK_SIGUEPLATO);

                context.sendBroadcast(bcIntent);

            }

            @Override
            public void onFailure(Call<RespuestaBaseWS> call, Throwable t) {
                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.setAction(ACTION_ERROR_SIGUEPLATO);
                context.sendBroadcast(bcIntent);
            }
        });
    }

    private void traspasaMesa(String codemp, String idTtablet,String tpv, String mesa, ArrayList<String> submesas, String mesadest) {
        APIService apiService = ClaseServicioWeb.getAPIService(false, config.connectionTO, config.rwTO2, config.rwTO2, ActivityInicio.latenciaTest.getUltima());
        RequestTraspasaMesaWS requestTraspasaMesaWS = new RequestTraspasaMesaWS();
        requestTraspasaMesaWS.setCodigoEmpresa(codemp);
        requestTraspasaMesaWS.setIdTablet(idTtablet);
        requestTraspasaMesaWS.setTpv(tpv);
        requestTraspasaMesaWS.setMesa(mesa);
        requestTraspasaMesaWS.setSubmesas(submesas);
        requestTraspasaMesaWS.setMesadest(mesadest);


        apiService.traspasaMesa(requestTraspasaMesaWS).enqueue(new Callback<RespuestaBaseWS>() {

            @Override
            public void onResponse(Call<RespuestaBaseWS> call, Response<RespuestaBaseWS> response) {
                RespuestaBaseWS res = response.body();

                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                if (res.getErrnum().equals("0"))
                    bcIntent.setAction(ACTION_OK_TRASPASAMESA);
                else
                    bcIntent.setAction(ACTION_ERROR_TRASPASAMESA);

                context.sendBroadcast(bcIntent);

            }

            @Override
            public void onFailure(Call<RespuestaBaseWS> call, Throwable t) {
                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.setAction(ACTION_ERROR_TRASPASAMESA);
                context.sendBroadcast(bcIntent);
            }
        });
    }

    private void pedirCuenta(String codemp, String idTtablet, String tpv,String mesa, String submesa,String nfactura, String codusu, String imprimir){
        APIService apiService = ClaseServicioWeb.getAPIService(false, config.connectionTO, config.rwTO5, config.rwTO5, ActivityInicio.latenciaTest.getUltima());

        final Long timeini = Calendar.getInstance().getTimeInMillis();

        apiService.pedirCuenta(codemp,idTtablet, tpv,mesa,submesa, nfactura,codusu, imprimir).enqueue(new Callback<RespuestaPedirCuentaWS>() {
            @Override
            public void onResponse(Call<RespuestaPedirCuentaWS> call, Response<RespuestaPedirCuentaWS> response) {
                RespuestaPedirCuentaWS res = response.body();

                ActivityInicio.latenciaPideCuenta.add(response.raw().sentRequestAtMillis(),response.raw().receivedResponseAtMillis(),true);

                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                if (res.getErrnum().equals("0")) {
                    bcIntent.setAction(ACTION_OK_PEDIRCUENTA);
                    bcIntent.putExtra("nfactura",res.getNfactura());
                    bcIntent.putExtra("pago_fecha_hora",res.getPago_fecha_hora());
                    bcIntent.putExtra("habitacion",res.getHabitacion());

                }
                else
                    bcIntent.setAction(ACTION_ERROR_PEDIRCUENTA);

                context.sendBroadcast(bcIntent);

            }

            @Override
            public void onFailure(Call<RespuestaPedirCuentaWS> call, Throwable t) {
                final Long timefin= Calendar.getInstance().getTimeInMillis();
                ActivityInicio.latenciaPideCuenta.add(timeini,timefin,false);

                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.setAction(ACTION_ERROR_PEDIRCUENTA);
                context.sendBroadcast(bcIntent);
            }
        });
    }

    private void recuperaCredigoTagID(String codemp, String idTtablet, final String tagID, String codemp_ext){
        APIService apiService = ClaseServicioWeb.getAPIService(false, config.connectionTO, config.rwTO3, config.rwTO3, ActivityInicio.latenciaTest.getUltima());
        apiService.recuperaCreditoTagID(codemp,idTtablet, tagID,codemp_ext).enqueue(new Callback<RespuestaCreditoWS>() {
            @Override
            public void onResponse(Call<RespuestaCreditoWS> call, Response<RespuestaCreditoWS> response) {
                if (response != null && response.body() != null) {

                    Log.d("ServSincronizaBD","respuesta recupera cupos");

                    Intent bcIntent = new Intent();
                    bcIntent.setPackage(context.getPackageName());

                    bcIntent.putExtra("errnum", response.body().getErrnum());
                    bcIntent.putExtra("errdesc", response.body().getErrdesc());
                    bcIntent.putExtra("habitacion", response.body().getHabitacion());
                    bcIntent.putExtra("codemp_ext", response.body().getCodemp_ext());
                    bcIntent.putExtra("nreserva", response.body().getNreserva());
                    bcIntent.putExtra("nombre", response.body().getNombre());
                    bcIntent.putExtra("salida", response.body().getSalida());
                    bcIntent.putExtra("okSalida", response.body().getOkSalida());
                    bcIntent.putExtra("credito", response.body().getCredito());
                    bcIntent.putExtra("pendientePago", response.body().getPendientePago());
                    bcIntent.putExtra("pension", response.body().getPension());
                    bcIntent.putExtra("servicio", response.body().getServicio());

                    if (Integer.valueOf(response.body().getErrnum()) > 0   && Integer.valueOf(response.body().getErrnum()) <= 1000  ){
                        bcIntent.putExtra("errnum",response.body().getErrnum());
                        bcIntent.putExtra("errdesc",response.body().getErrdesc()+"\r\n\r\ntagID enviado: "+tagID+"");
                        bcIntent.setAction(ACTION_ERROR_CREDITO);
                    }else {
                        bcIntent.setAction(ACTION_OK_CREDITO);
                    }

                    context.sendBroadcast(bcIntent);

                }
                else {
                    Intent bcIntent = new Intent();
                    bcIntent.setPackage(context.getPackageName());

                    bcIntent.putExtra("errnum","1");
                    bcIntent.putExtra("errdesc",getResources().getString(R.string.strErrorCredito));
                    bcIntent.setAction(ACTION_ERROR_CREDITO);
                    context.sendBroadcast(bcIntent);

                }
            }

            @Override
            public void onFailure(Call<RespuestaCreditoWS> call, Throwable t) {
                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.putExtra("errnum","1");
                bcIntent.putExtra("errdesc",getResources().getString(R.string.strErrorCredito));
                bcIntent.setAction(ACTION_ERROR_CREDITO);
                context.sendBroadcast(bcIntent);
            }
        });
    }

    private void recuperaCredigoRoom(String codemp, String idTtablet, String room, String codemp_ext){
        APIService apiService = ClaseServicioWeb.getAPIService(false, config.connectionTO, config.rwTO3, config.rwTO3, ActivityInicio.latenciaTest.getUltima());
        apiService.recuperaCreditoRoom(codemp,idTtablet, room,codemp_ext).enqueue(new Callback<RespuestaCreditoWS>() {
            @Override
            public void onResponse(Call<RespuestaCreditoWS> call, Response<RespuestaCreditoWS> response) {
                if (response != null && response.body() != null) {

                    Log.d("ServSincronizaBD","respuesta recupera cupos");

                    Intent bcIntent = new Intent();
                    bcIntent.setPackage(context.getPackageName());

                    bcIntent.putExtra("errnum", response.body().getErrnum());
                    bcIntent.putExtra("errdesc", response.body().getErrdesc());
                    bcIntent.putExtra("habitacion", response.body().getHabitacion());
                    bcIntent.putExtra("codemp_ext", response.body().getCodemp_ext());
                    bcIntent.putExtra("nreserva", response.body().getNreserva());
                    bcIntent.putExtra("nombre", response.body().getNombre());
                    bcIntent.putExtra("salida", response.body().getSalida());
                    bcIntent.putExtra("okSalida", response.body().getOkSalida());
                    bcIntent.putExtra("credito", response.body().getCredito());
                    bcIntent.putExtra("pendientePago", response.body().getPendientePago());
                    bcIntent.putExtra("pension", response.body().getPension());
                    bcIntent.putExtra("servicio", response.body().getServicio());

                    if (Integer.valueOf(response.body().getErrnum()) > 0   && Integer.valueOf(response.body().getErrnum()) <= 1000  ){
                        bcIntent.putExtra("errnum",response.body().getErrnum());
                        bcIntent.putExtra("errdesc",response.body().getErrdesc());
                        bcIntent.setAction(ACTION_ERROR_CREDITO);
                    }else {
                        bcIntent.setAction(ACTION_OK_CREDITO);
                    }

                    context.sendBroadcast(bcIntent);

                }
                else {
                    Intent bcIntent = new Intent();
                    bcIntent.setPackage(context.getPackageName());

                    bcIntent.putExtra("errnum","1");
                    bcIntent.putExtra("errdesc",getResources().getString(R.string.strErrorCredito));
                    bcIntent.setAction(ACTION_ERROR_CREDITO);
                    context.sendBroadcast(bcIntent);

                }
            }

            @Override
            public void onFailure(Call<RespuestaCreditoWS> call, Throwable t) {
                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.putExtra("errnum","1");
                bcIntent.putExtra("errdesc",getResources().getString(R.string.strErrorCredito));
                bcIntent.setAction(ACTION_ERROR_CREDITO);
                context.sendBroadcast(bcIntent);
            }
        });
    }


    private void pagaMesa(String codemp, String idTtablet,String tpv, String mesa, String submesa,String codemp_ext,String nreserva, String formapago, String importe, String igic,
                          String efectivo, String tarjeta, String cuentacasa, String credito, String nfactura, String codclicasa, String codusu, String entregado,
                          String cambio, String firma,String apto, String imprimir) {

        APIService apiService = ClaseServicioWeb.getAPIService(false, config.connectionTO, config.rwTO5, config.rwTO5, ActivityInicio.latenciaTest.getUltima());
        RequestPagaMesaWS requestPagaMesaWS = new RequestPagaMesaWS();
        requestPagaMesaWS.setCodigoEmpresa(codemp);
        requestPagaMesaWS.setIdTablet(idTtablet);
        requestPagaMesaWS.setTpv(tpv);
        requestPagaMesaWS.setMesa(mesa);
        requestPagaMesaWS.setSubmesa(submesa);
        requestPagaMesaWS.setCodemp_ext(codemp_ext);
        requestPagaMesaWS.setNreserva(nreserva);
        requestPagaMesaWS.setFormapago(formapago);
        requestPagaMesaWS.setImporte(importe);
        requestPagaMesaWS.setIgic(igic);
        requestPagaMesaWS.setEfectivo(efectivo);
        requestPagaMesaWS.setTarjeta(tarjeta);
        requestPagaMesaWS.setCuentacasa(cuentacasa);
        requestPagaMesaWS.setCredito(credito);
        requestPagaMesaWS.setNfactura(nfactura);
        requestPagaMesaWS.setCodclicasa(codclicasa);
        requestPagaMesaWS.setCodclicasa(codclicasa);
        requestPagaMesaWS.setCodusu(codusu);
        requestPagaMesaWS.setEntregado(entregado);
        requestPagaMesaWS.setCambio(cambio);

        requestPagaMesaWS.setFirma(firma);
        requestPagaMesaWS.setApto(apto);
        requestPagaMesaWS.setImprimir(imprimir);

        final Long timeini = Calendar.getInstance().getTimeInMillis();

        apiService.pagaMesa(requestPagaMesaWS).enqueue(new Callback<RespuestaPagaMesaWS>() {

            @Override
            public void onResponse(Call<RespuestaPagaMesaWS> call, Response<RespuestaPagaMesaWS> response) {
                RespuestaPagaMesaWS res = response.body();
                ActivityInicio.latenciaPagaCuenta.add(response.raw().sentRequestAtMillis(),response.raw().receivedResponseAtMillis(),true);

                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                if (res.getErrnum().equals("0")) {
                    bcIntent.setAction(ACTION_OK_PAGAMESA);
                    bcIntent.putExtra("nfactura",res.getNfactura());
                    bcIntent.putExtra("pago_fecha_hora",res.getPago_fecha_hora());
                    bcIntent.putExtra("formapago",formapago);
                    bcIntent.putExtra("efectivo",efectivo);
                    bcIntent.putExtra("tarjeta",tarjeta);
                    bcIntent.putExtra("cuentacasa",cuentacasa);
                    bcIntent.putExtra("credito",credito);
                    bcIntent.putExtra("codclicasa",codclicasa);
                    bcIntent.putExtra("entregado",entregado);
                    bcIntent.putExtra("cambio",cambio);
                    bcIntent.putExtra("firma",firma);
                    bcIntent.putExtra("apto",apto);

                }
                else {
                    bcIntent.setAction(ACTION_ERROR_PAGAMESA);
                    bcIntent.putExtra("errnum",res.getErrnum());
                    bcIntent.putExtra("errdesc",res.getErrdesc());
                }
                context.sendBroadcast(bcIntent);

            }

            @Override
            public void onFailure(Call<RespuestaPagaMesaWS> call, Throwable t) {
                final Long timefin= Calendar.getInstance().getTimeInMillis();
                ActivityInicio.latenciaPagaCuenta.add(timeini,timefin,false);

                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.setAction(ACTION_ERROR_PAGAMESA);
                context.sendBroadcast(bcIntent);
            }
        });
    }

    private void recuperaFacturas(String codemp, String idTtablet, String tpv){
        APIService apiService = ClaseServicioWeb.getAPIService(false, config.connectionTO, config.rwTO2, config.rwTO2, ActivityInicio.latenciaTest.getUltima());
        apiService.recuperaFacturas(codemp,idTtablet, tpv).enqueue(new Callback<RespuestaFacturasWS>() {
            @Override
            public void onResponse(Call<RespuestaFacturasWS> call, Response<RespuestaFacturasWS> response) {
                List<Facturas> tfacturas= response.body().getfacturas();
                ArrayList<ClaseFacturas> facturas = new ArrayList<>();
                facturas.clear();
                if (tfacturas != null) {
                    for (Facturas tfactura : tfacturas) {
                        facturas.add (new ClaseFacturas(tfactura.getNfactura(),tfactura.getMesa(),tfactura.getSubmesa(),tfactura.getFormapago(),tfactura.getCodenl(),tfactura.getFecha(),tfactura.getHora() ));
                    }
                }

                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.setAction(ACTION_OK_RECUPERAFACTURAS);
                bcIntent.putParcelableArrayListExtra("facturas",facturas);
                context.sendBroadcast(bcIntent);

            }

            @Override
            public void onFailure(Call<RespuestaFacturasWS> call, Throwable t) {
                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.setAction(ACTION_ERROR_RECUPERAFACTURAS);
                context.sendBroadcast(bcIntent);
            }
        });
    }

    private void recuperaFactura(String codemp, String idTtablet, String codenl){

        final String fCodenl = codenl;

        APIService apiService = ClaseServicioWeb.getAPIService(false, config.connectionTO, config.rwTO3, config.rwTO3, ActivityInicio.latenciaTest.getUltima());
        apiService.recuperaFactura(codemp,idTtablet, codenl).enqueue(new Callback<RespuestaFacturaWS>() {
            @Override
            public void onResponse(Call<RespuestaFacturaWS> call, Response<RespuestaFacturaWS> response) {
                ClaseFacturaCabecera cabecera = response.body().getCabecera();
                List<LineasVenta> tlineas= response.body().getLineas();
                String firma = response.body().getFirma();

                ArrayList<LineasVenta> lineas = new ArrayList<>();
                lineas.clear();
                if (tlineas != null) {
                    for (LineasVenta tlinea : tlineas) {
                        tlinea.setNfactura(tlinea.getNfactura().trim());
                        lineas.add(tlinea);
                    }
                }

                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.setAction(ACTION_OK_RECUPERAFACTURA);
                bcIntent.putExtra("codenl", fCodenl);
                bcIntent.putExtra("empresa",response.body().getEmpresa());
                bcIntent.putExtra("cif",response.body().getCif());
                bcIntent.putExtra("mesa",response.body().getMesa());
                bcIntent.putExtra("submesa",response.body().getSubmesa());
                bcIntent.putExtra("codusu",response.body().getCodusu());
                bcIntent.putParcelableArrayListExtra("lineas",lineas);
                bcIntent.putExtra("cabecera",cabecera);
                bcIntent.putExtra("firma",firma);
                context.sendBroadcast(bcIntent);

            }

            @Override
            public void onFailure(Call<RespuestaFacturaWS> call, Throwable t) {
                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.setAction(ACTION_ERROR_RECUPERAFACTURA);
                context.sendBroadcast(bcIntent);
            }
        });
    }

    private void imprimeFactura(String codemp, String idTtablet, String codenl){
        APIService apiService = ClaseServicioWeb.getAPIService(false, config.connectionTO, config.rwTO3, config.rwTO3, ActivityInicio.latenciaTest.getUltima());
        apiService.imprimeFactura(codemp,idTtablet, codenl).enqueue(new Callback<RespuestaBaseWS>() {
            @Override
            public void onResponse(Call<RespuestaBaseWS> call, Response<RespuestaBaseWS> response) {

                RespuestaBaseWS res = response.body();

                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.setAction(ACTION_OK_IMPRIMEFACTURA);
                context.sendBroadcast(bcIntent);
            }

            @Override
            public void onFailure(Call<RespuestaBaseWS> call, Throwable t) {
                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.setAction(ACTION_ERROR_IMPRIMEFACTURA);
                context.sendBroadcast(bcIntent);
            }
        });
    }

    //endregion


    //region FUNCIONES SOBRE   BBDD
    private void borraTabla(final String tabla) {
        if (activity != null && handlerNotificaWS != null) activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Message mensaje = handlerNotificaWS.obtainMessage(ACTION_BORRANDO_REGISTROS);
                Bundle bundle = new Bundle();
                bundle.putString("tabla",tabla);
                mensaje.setData(bundle);
                handlerNotificaWS.sendMessage(mensaje);
            }
        });
        ClaseBaseDatos roomBD = ClaseBaseDatos.getDatabase(activity.getBaseContext(),handlerNotificaWS,activity);

        try{
            roomBD.borraTabla(tabla.toLowerCase());
        }
        catch (ExecutionException e1){}
        catch (InterruptedException e2){}

    }

    private void borraRangos(final String tabla, ArrayList<Rangos> rangos) {
        if (activity != null && handlerNotificaWS != null) activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Message mensaje = handlerNotificaWS.obtainMessage(ACTION_BORRANDO_REGISTROS);
                Bundle bundle = new Bundle();
                bundle.putString("tabla",tabla);
                mensaje.setData(bundle);
                handlerNotificaWS.sendMessage(mensaje);
            }
        });

        if (rangos != null && rangos.size() >0 ){
            ClaseBaseDatos roomBD = ClaseBaseDatos.getDatabase(activity.getBaseContext(),handlerNotificaWS,activity);
            for (Rangos rango: rangos){
                try{
                    roomBD.borraRango(tabla.toLowerCase(),rango.getDesde(),rango.getHasta());
                }
                catch (ExecutionException e1){}
                catch (InterruptedException e2){}


            }
        }
    }

    private void borraRegistros(final String tabla,ArrayList<Integer> registrosDel){
        if (activity != null) activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (handlerNotificaWS != null) {
                    Message mensaje = handlerNotificaWS.obtainMessage(ACTION_BORRANDO_REGISTROS);
                    Bundle bundle = new Bundle();
                    bundle.putString("tabla",tabla);
                    mensaje.setData(bundle);
                    handlerNotificaWS.sendMessage(mensaje);
                }
            }
        });
        if (registrosDel != null && registrosDel.size() >0 ) {

            ClaseBaseDatos roomBD = ClaseBaseDatos.getDatabase(activity.getBaseContext(), handlerNotificaWS, activity);
            for (Integer registro : registrosDel) {
                try {
                    roomBD.borraRegistro(tabla, registro);
                }
                catch (ExecutionException e1) {
                }
                catch (InterruptedException e2) {
                }
            }

        }
    }

    protected <T> void  guardaRegistros(List<T> registros){
        ClaseBaseDatos roomBD = ClaseBaseDatos.getDatabase(activity.getBaseContext(),handlerNotificaWS,activity);
        int n = 0;
        boolean ultimo = false;

        if (registros != null){
            for (T  registro: registros){
                if (n == registros.size() -1)
                    ultimo = true;
                roomBD.upsetTabla(  registro, ultimaSync.total,ultimaSync.offsetAnterior +n,ultimaSync.maxID,ultimo);
                n++;
            }
        }

    }



    //endregion

    //region UPDATE APK



    public static String descargaAPK(String file, Context context){
        ServSincronizaBD.context = context;
        ClaseUtils.DescargaAPK.mostrarDialogo(true,"Descargando nueva versión","por favor espere",context);

        new DownloadFile().execute(file);
        return "";
    }

    public static class DownloadFile extends AsyncTask<String, String, String> {

        public DownloadFile(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }


        protected String doInBackground(String... file) {
            int count;
            File filePath = null;
            try {

                URL conUrl = new URL(config.url +"download/"+file[0]);

                URLConnection conection = conUrl.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(conUrl.openStream(), 8192);

                // Output stream to write file
                filePath = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) ,file[0]);

                if (filePath.exists()){
                    return filePath.getAbsolutePath();
                }

                OutputStream output = new FileOutputStream(filePath);
                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }


                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());

                bcIntent.setAction(ACTION_ERROR_DOWNLOAD_APP);
                bcIntent.putExtra("error",e.getMessage());
                context.sendBroadcast(bcIntent);
                return "Error descargando nueva apk.\r\nError: "+e.getMessage();
            }

            return filePath.getAbsolutePath();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            // Update progress
            super.onProgressUpdate(values);
            ClaseUtils.DescargaAPK.getProgressDialog().setProgress(Integer.parseInt(values[0]));
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Removes the progress bar
            ClaseUtils.DescargaAPK.cerrarDialogo();
            //     delegate.downloadAppResult(result);
            if (!result.toUpperCase().startsWith("ERROR")) {


                //instalar la app descargada
                Uri uri = null;
                File file = new File(result);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        //uri = FileProvider.getUriForFile(context,context.getApplicationContext().getPackageName()+".fileprovider",new File(result));
                        uri = FileProvider.getUriForFile(context,context.getApplicationContext().getPackageName() + ".fileprovider", file);
                        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        context.startActivity(intent);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else {
                    uri = Uri.fromFile(file);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            }


        }

    }


    //endregion


    //region GENERICO
    interface ExtractorLista<R,T> {
        List<T> extraer(R respuesta);
    }


    private <T> Call<T> getResponseApiService(RequestRecuperaRegistrosWS request ) {
        APIService apiService = ClaseServicioWeb.getAPIService(false, config.connectionTO, config.rwTO2, config.rwTO2, ActivityInicio.latenciaTest.getUltima());
        switch (request.getTabla()) {
            case "usuarios":  return (Call<T>) apiService.recuperaUsuarios(request);
            case "tpvs":  return (Call<T>) apiService.recuperaTPVS(request);
            case "nom_mesas":  return (Call<T>) apiService.recuperaNomMesas(request);
            case "alergenos": return (Call<T>) apiService.recuperaAlergenos(request);
            case "etiquetas": return (Call<T>) apiService.recuperaEtiquetas(request);
            case "clientesctacasa": return (Call<T>) apiService.recuperaClientesCtaCasa(request);
            case "cabeceras": return (Call<T>) apiService.recuperaCabeceras(request);
            case "productos": return (Call<T>) apiService.recuperaProductos(request);
            case "familias": return (Call<T>) apiService.recuperaFamilias(request);
            case "subfamilias": return (Call<T>) apiService.recuperaSubfamilias(request);
            case "hora_comidas": return (Call<T>) apiService.recuperaHoraComidas(request);
            default:
                throw new IllegalArgumentException("Tabla no soportada: " + request.getTabla());
        }
    }

    private RequestRecuperaRegistrosWS prepararRequest(String codemp, String idDispositivo, String tabla, ArrayList<String> cods) {
        RequestRecuperaRegistrosWS req = new RequestRecuperaRegistrosWS();
        req.setCodemp(codemp);
        req.setIdentificador(idDispositivo);
        req.setTabla(tabla);
        req.setCodigos(cods);
        return req;
    }

    private <T, U extends RespuestaSincro<T>> void ejecutarRecuperacionWS(
            Call<U> call,
            final String nombreTabla) {

        call.enqueue(new Callback<U>() {
            @Override
            public void onResponse(Call<U> call, Response<U> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<T> room = response.body().getListaRegistros();

                    if (room != null && !room.isEmpty()) {
                        // Llama a tu función genérica que ya tienes
                        guardaRegistros(room);
                    } else {
                        enviarBroadcastSincro(nombreTabla, ACTION_OK_TABLA);
                    }
                } else {
                    enviarBroadcastSincro(nombreTabla, ACTION_ERROR_TABLA);
                }
            }

            @Override
            public void onFailure(Call<U> call, Throwable t) {
                enviarBroadcastSincro(nombreTabla, ACTION_ERROR_TABLA);
            }
        });
    }

    private void enviarBroadcastSincro(String tabla, String accion) {
        Intent bcIntent = new Intent();
        bcIntent.setPackage(context.getPackageName());
        bcIntent.setAction(accion);
        bcIntent.putExtra("tabla", tabla);
        context.sendBroadcast(bcIntent);

    }
//endregion

    private static class UltimaSync{
        private static UltimaSync INSTANCE;

        String tabla;
        int maxID;
        int ultimoID;
        int offset;
        int total;
        int offsetAnterior;
        int totalAnterior;
        boolean finalizado;

        public static UltimaSync getInstance(){
            if (INSTANCE == null) {
                synchronized (ClaseBaseDatos.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new UltimaSync("",0,-1,0,0,0,0, false);
                    }
                }
            }
            return INSTANCE;
        }


        public UltimaSync(String tabla,int maxID, int ultimoID, int offset, int total, int offsetAnterior, int totalAnterior, boolean finalizado) {
            this.maxID = maxID;
            this.tabla = tabla;
            this.ultimoID = ultimoID;
            this.offset = offset;
            this.offsetAnterior = offsetAnterior;
            this.total = totalAnterior;
            this.totalAnterior = total;
            this.finalizado = finalizado;

        }

        public void iniciaValores(String tabla, int maxID, int ultimoID, int offset, int total, int offsetAnterior, int totalAnterior, boolean finalizado) {
            this.maxID = maxID;
            this.tabla = tabla;
            this.ultimoID = ultimoID;
            this.offset = offset;
            this.offsetAnterior = offsetAnterior;
            this.total = totalAnterior;
            this.totalAnterior = total;
            this.finalizado = finalizado;

        }

        public  void fromJson(String json){
            Gson gson = new Gson();
            INSTANCE = gson.fromJson(json,UltimaSync.class);
            int a = 1 ;
        }

        public String toJson(){
            Gson gson = new Gson();
            String json = gson.toJson(this);
            return  json;
        }
        public String getTabla() {
            return tabla;
        }

        public void setTabla(String tabla) {
            this.tabla = tabla;
        }

        public int getMaxID() {
            return maxID;
        }

        public void setMaxID(int maxID) {
            this.maxID = maxID;
        }

        public int getUltimoID() {
            return ultimoID;
        }

        public void setUltimoID(int ultimoID) {
            this.ultimoID = ultimoID;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getOffsetAnterior() {
            return offsetAnterior;
        }

        public void setOffsetAnterior(int offsetAnterior) {
            this.offsetAnterior = offsetAnterior;
        }

        public int getTotalAnterior() {
            return totalAnterior;
        }

        public void setTotalAnterior(int totalAnterior) {
            this.totalAnterior = totalAnterior;
        }

        public boolean isFinalizado() {
            return finalizado;
        }

        public void setFinalizado(boolean finalizado) {
            this.finalizado = finalizado;
        }
    }

    private class Rangos {
        private int desde;
        private int hasta;

        public Rangos(int desde, int hasta) {
            this.desde = desde;
            this.hasta = hasta;
        }

        public int getDesde() {
            return desde;
        }

        public void setDesde(int desde) {
            this.desde = desde;
        }

        public int getHasta() {
            return hasta;
        }

        public void setHasta(int hasta) {
            this.hasta = hasta;
        }
    }

}
