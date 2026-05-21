package es.quatroges.qgestpv_v3.basedatos;

import static es.quatroges.qgestpv_v3.basedatos.Migraciones.DATABASE_VERSION;
import static es.quatroges.qgestpv_v3.servicios.ServSincronizaBD.ACTION_GRABANDO_REGISTROS;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import es.quatroges.qgestpv_v3.datos.Alergenos;
import es.quatroges.qgestpv_v3.datos.Cabeceras;
import es.quatroges.qgestpv_v3.datos.ClientesCtaCasa;
import es.quatroges.qgestpv_v3.datos.Configuracion;
import es.quatroges.qgestpv_v3.datos.Etiquetas;
import es.quatroges.qgestpv_v3.datos.Hora_Comidas;
import es.quatroges.qgestpv_v3.datos.Nom_Mesas;
import es.quatroges.qgestpv_v3.datos.Productos;
import es.quatroges.qgestpv_v3.datos.Tpvs;
import es.quatroges.qgestpv_v3.datos.Usuarios;
import es.quatroges.qgestpv_v3.datos.roomRegistrosCRC;
import es.quatroges.qgestpv_v3.datos.Familias;
import es.quatroges.qgestpv_v3.datos.Subfamilias;
import es.quatroges.qgestpv_v3.utils.ClaseItemExtra;


@Database(entities = {Configuracion.class, Usuarios.class, Tpvs.class, Nom_Mesas.class,
        Cabeceras.class, Productos.class, ClientesCtaCasa.class, Alergenos.class, Etiquetas.class,
        Familias.class,Subfamilias.class, Hora_Comidas.class},
        version = DATABASE_VERSION, exportSchema = false)

public abstract class ClaseBaseDatos extends RoomDatabase {
    public abstract DaoConfiguracion configuracionDAO();
    public abstract DaoUsuarios usuariosDao();
    public abstract DaoTpvs tpvsDao();
    public abstract DaoCabeceras cabecerasDAO();
    public abstract DaoProductos productosDAO();
    public abstract DaoClientesCtaCasa clientesCtaCasaDAO();
    public abstract DaoNomMesas nomMesasDao();
    public abstract DaoAlergenos alergenosDao();
    public abstract DaoEtiquetas etiquetasDao();

    public abstract  DaoFamilias familiasDao();
    public abstract  DaoSubfamilias subfamiliasDao();
    public abstract  DaoHoraComidas horaComidasDao();



    private static ClaseBaseDatos INSTANCE;
    private static final String DATABASE_NAME = "qgestpv.db";

    private static Handler handlerNotificaWS = null;
    private static Activity activity = null;
    private static Context context = null;
    public static final int ACTION_GRABANDO_USUARIOS=1;
    public static final int ACTION_GRABANDO_TPVS=2;
    public static final int ACTION_GRABANDO_CABECERAS=3;
    public static final int ACTION_GRABANDO_PRODUCTOS=4;
    public static final int ACTION_GRABANDO_CLIENTESCTACASA=5;
    public static final int ACTION_GRABANDO_NOMMESAS=6;
    public static final int ACTION_GRABANDO_ALERGENOS=7;
    public static final int ACTION_GRABANDO_ETIQUETAS=8;


    public static ClaseBaseDatos getDatabase(final Context context, Handler handler, Activity activity){
         if (INSTANCE == null) {
             synchronized (ClaseBaseDatos.class) {
                 if (INSTANCE == null) {
                     handlerNotificaWS  = handler;
                     ClaseBaseDatos.activity = activity;
                     ClaseBaseDatos.context =context;
                     INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ClaseBaseDatos.class, DATABASE_NAME)
                             .fallbackToDestructiveMigration()
                             .addMigrations(Migraciones.MIGRATION_6_7)
                             .setQueryCallback((sqlQuery, bindArgs )-> {
                                 Log.d("ClaseBaseDatos", "SQL Query: " + sqlQuery + ",\n Args: " + bindArgs);
                             },Executors.newSingleThreadExecutor())
                             .build();
                 }
             }

         }
         return INSTANCE;

     }


    //region Configuracion

    public  Integer getConfiguracionCount() throws ExecutionException,InterruptedException{
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return configuracionDAO().recuperaConfiguracionCount();
            }
        };
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public  List<Configuracion> getConfiguracion() throws ExecutionException,InterruptedException{
        Callable<List<Configuracion>> callable = new Callable<List<Configuracion>>() {
            @Override
            public List<Configuracion> call() throws Exception {
                List<Configuracion> c = configuracionDAO().recuperaConfiguracion();
                return c;
            }
        };
        Future<List<Configuracion>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public  void upsetConfiguracion(final Configuracion configuracion, final int pos, final int max) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                long id = configuracionDAO().insertConfiguracion(configuracion);
                if (id == -1) configuracionDAO().updateConfiguracion(configuracion);
                if (activity != null) activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Message mensaje = handlerNotificaWS.obtainMessage(ACTION_GRABANDO_TPVS);
                        Bundle bundle = new Bundle();
                        bundle.putInt("pos",  pos);
                        bundle.putInt("max",  max);
                        mensaje.setData(bundle);
                        handlerNotificaWS.sendMessage(mensaje);
                    }
                });

                return null;
            }
        }.execute();
    }

    public  void borraTablaConfiguracion() throws ExecutionException,InterruptedException{
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                configuracionDAO().borraConfiguracion();
                return "";
            }
        };
        Future<String> future = Executors.newSingleThreadExecutor().submit(callable);
        future.get();
    }


    //endregion

    //region Cabeceras

    public  List<Cabeceras> getCabecerasTpv(final int tmenu) throws ExecutionException,InterruptedException{
        Callable<List<Cabeceras>> callable = new Callable<List<Cabeceras>>() {
            @Override
            public List<Cabeceras> call() throws Exception {
                return cabecerasDAO().recuperaCabecerasTPV(tmenu);
            }
        };
        Future<List<Cabeceras>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }


    //endregion

    //region Productos

    public  Integer getProductosEtiquetaCount( final int tmenu, final int cabecera,  final int etiqueta) throws ExecutionException,InterruptedException{
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                if (cabecera == -1)
                    return productosDAO().recuperaProductosEtiquetaCount(tmenu, etiqueta);
                else
                    return productosDAO().recuperaProductosCabeceraEtiquetaCount(tmenu,cabecera , etiqueta);
            }
        };
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }


    public  List<Productos> getProductosCabecera(final int tmenu, final int cabecera) throws ExecutionException,InterruptedException{
        Callable<List<Productos>> callable = new Callable<List<Productos>>() {
            @Override
            public List<Productos> call() throws Exception {
                if (cabecera == -1){
                    return productosDAO().recuperaTodosProductosCabecera(tmenu);
                }
                else {
                    return productosDAO().recuperaProductosCabecera(tmenu, cabecera);
                }
            }
        };
        Future<List<Productos>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public  Productos getProducto(final String codmenu, final int tmenu) throws ExecutionException,InterruptedException{
        Callable <Productos> callable = new Callable<Productos>() {
            @Override
            public Productos call() throws Exception {
                    return productosDAO().recuperaProducto(codmenu,tmenu);

            }
        };
        Future<Productos> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    //endregion


    //region Hora_comidas

    public  List<Hora_Comidas> getHoraComidasTPV(final int codtpv) throws ExecutionException,InterruptedException{
        Callable<List<Hora_Comidas>> callable = new Callable<List<Hora_Comidas>>() {
            @Override
            public List<Hora_Comidas> call() throws Exception {
                return horaComidasDao().recuperaHoraComidasTPV(codtpv);
            }
        };
        Future<List<Hora_Comidas>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }


    //endregion


    //region Mantenimiento BBDD
    
    public  void backup(Context context) throws IOException {

        boolean disponible ;
        boolean permisoEscritura;

        disponible = false;
        permisoEscritura= false;

        File dbFile = context.getDatabasePath(DATABASE_NAME);
        FileInputStream fis = null;
        fis = new FileInputStream(dbFile);
        String memoriaSD = Environment.getExternalStorageState();


        if (memoriaSD.equals(Environment.MEDIA_MOUNTED))
        {
            disponible = true;
            permisoEscritura = true;
        }
        else if (memoriaSD.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            disponible = true;
            permisoEscritura = false;
        }

        if (disponible && permisoEscritura) {

            try
            {
                //File d = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/es.quatroges.qgestpv_v1");
                File d = new File(context.getExternalFilesDir(null)+"");
                if (!d.exists()) {
                    try {
                        d.mkdir();
                    }
                    catch (Exception io){
                        String error = io.getMessage();
                    }
                }

                String outFileName = d.getPath() + File.separator +DATABASE_NAME+".db";
                OutputStream output = new FileOutputStream(outFileName);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                output.flush();
                output.close();
                fis.close();
            }
            catch (Exception e)
            {
                String error = e.getMessage();
            }

        }
        else
        {
            if (disponible && (! permisoEscritura)) Toast.makeText(context, "Tarjeta SD sin permiso de escritura",Toast.LENGTH_SHORT).show();
            if (! disponible) Toast.makeText(context, "Tarjeta SD no disponible",Toast.LENGTH_SHORT).show();

        }

    }

    public void restore(Context context) throws IOException {
        boolean disponible ;
        boolean permisoEscritura;

        disponible = false;
        permisoEscritura= false;


        String memoriaSD = Environment.getExternalStorageState();

        if (memoriaSD.equals(Environment.MEDIA_MOUNTED))
        {
            disponible = true;
            permisoEscritura = true;
        }
        else if (memoriaSD.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            disponible = true;
            permisoEscritura = false;
        }

        if (disponible && permisoEscritura) {

            try
            {
                //File d = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/es.mcingesoft.MGeTiquet");
                File d = new File(context.getExternalFilesDir(null)+"");
                if (!d.exists()) d.mkdir();


                final String inFileName = d.getPath() + "/"+DATABASE_NAME ;
                File dbFile = new File(inFileName);
                FileInputStream fis = null;
                fis = new FileInputStream(dbFile);


                String outFileName = context.getDatabasePath(DATABASE_NAME).toString();
                OutputStream output = new FileOutputStream(outFileName);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                output.flush();
                output.close();
                fis.close();
            }
            catch (Exception e)
            {
                Toast.makeText(context, "Error copiando archivo \n" +
                        e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            if (disponible && (! permisoEscritura)) Toast.makeText(context, "Tarjeta SD sin permiso de escritura",Toast.LENGTH_SHORT).show();
            if (! disponible) Toast.makeText(context, "Tarjeta SD no disponible",Toast.LENGTH_SHORT).show();

        }
    }


    public void borrarTablas(boolean noBorraConfig){
        try {
            if (! noBorraConfig){
                borraTablaConfiguracion();
            }
            borraTabla("usuarios");
            borraTabla("tpvs");
            borraTabla("nom_mesas");
            borraTabla("cabeceras");
            borraTabla("productos");
            borraTabla("alergenos");
            borraTabla("etiquetas");
            borraTabla("clientesctacasa");

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void guardarConfiguracion(Bundle bDatosParametros, Bundle bDatosWS,  Bundle bDatosPrinter, Bundle bDatosBBDD){
        //parametros de la aplicacion

        if (bDatosParametros != null) {
            INSTANCE.upsetConfiguracion(new Configuracion("demo", String.valueOf(bDatosParametros.getBoolean("demo", false))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("empresa", bDatosParametros.getString("empresa")), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("codigo", bDatosParametros.getString("codigo")), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("carga_lineas_venta", String.valueOf(bDatosParametros.getBoolean("cargaLineasVenta", false))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("clave_camareros", String.valueOf(bDatosParametros.getBoolean("claveCamareros", false))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("pax", String.valueOf(bDatosParametros.getBoolean("pax", false))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("pagoEfectivo", String.valueOf(bDatosParametros.getBoolean("pagoEfectivo", true))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("pagoTarjeta", String.valueOf(bDatosParametros.getBoolean("pagoTarjeta", true))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("pagoEfeTar", String.valueOf(bDatosParametros.getBoolean("pagoEfeTar", false))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("pagoCuentacasa", String.valueOf(bDatosParametros.getBoolean("pagoCuentacasa", false))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("pagoCredito", String.valueOf(bDatosParametros.getBoolean("pagoCredito", false))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("pagoPension", String.valueOf(bDatosParametros.getBoolean("pagoPension", false))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("mantenerVenta", String.valueOf(bDatosParametros.getBoolean("mantenerVenta", false))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("mostrarProductosAuto", String.valueOf(bDatosParametros.getBoolean("mostrarProductosAuto", false))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("mantenerProductos", String.valueOf(bDatosParametros.getBoolean("mantenerProductos", false))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("compacto", String.valueOf(bDatosParametros.getBoolean("compacto", false))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("timer_fragment", String.valueOf(bDatosParametros.getInt("timerFragment"))), 1, 1);
        }

        if (bDatosWS != null) {
            //parametros servicio web
            INSTANCE.upsetConfiguracion(new Configuracion("url", String.valueOf(bDatosWS.getString("url"))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("api", String.valueOf(bDatosWS.getString("api"))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("tocon", String.valueOf(bDatosWS.getInt("tocon"))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("torw1", String.valueOf(bDatosWS.getInt("torw1"))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("torw2", String.valueOf(bDatosWS.getInt("torw2"))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("torw3", String.valueOf(bDatosWS.getInt("torw3"))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("torw4", String.valueOf(bDatosWS.getInt("torw4"))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("torw5", String.valueOf(bDatosWS.getInt("torw5"))), 1, 1);
        }

        if (bDatosBBDD != null) {
            //parametros bbdd
            INSTANCE.upsetConfiguracion(new Configuracion("sincronizaauto", String.valueOf(bDatosBBDD.getBoolean("sincronizaauto"))),1,1);
        }

        if (bDatosPrinter != null) {
            //parametros impresora
            INSTANCE.upsetConfiguracion(new Configuracion("btmac", String.valueOf(bDatosPrinter.getString("btmac"))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("btmodelo", bDatosPrinter.getString("btmodelo")), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("btname", String.valueOf(bDatosPrinter.getString("btname"))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("comandaapp", String.valueOf(bDatosPrinter.getBoolean("comandaapp"))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("comandatpv", String.valueOf(bDatosPrinter.getBoolean("comandatpv"))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("cuentapteapp", String.valueOf(bDatosPrinter.getBoolean("cuentapteapp"))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("cuentaptetpv", String.valueOf(bDatosPrinter.getBoolean("cuentaptetpv"))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("cuentaapp", String.valueOf(bDatosPrinter.getBoolean("cuentaapp"))), 1, 1);
            INSTANCE.upsetConfiguracion(new Configuracion("cuentatpv", String.valueOf(bDatosPrinter.getBoolean("cuentatpv"))), 1, 1);
        }
    }



    public void cargaTablas(){
            INSTANCE.upsetConfiguracion(new Configuracion("demo","true"),1,2);
            INSTANCE.upsetConfiguracion(new Configuracion("empresa","Hotel Quatroges"),1,2);
            INSTANCE.upsetConfiguracion(new Configuracion("codigo","001"),1,2);
            INSTANCE.upsetConfiguracion(new Configuracion("carga_lineas_venta","false"),1,2);
            INSTANCE.upsetConfiguracion(new Configuracion("clave_camareros","false"),1,2);
            INSTANCE.upsetConfiguracion(new Configuracion("pax","false"),1,2);
            INSTANCE.upsetConfiguracion(new Configuracion("timer_fragment","10"),1,2);
            INSTANCE.upsetConfiguracion(new Configuracion("url","http://comandero.cuatroges.es:8080"),1,2);
            INSTANCE.upsetConfiguracion(new Configuracion("api","mgwres/rest"),1,2);

    }

    // endregion

    //region SINCRONIZA TABLAS - FUNCIONES COMUNES
    public int cuentaRegistros(final String tabla) throws  ExecutionException,InterruptedException{
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Integer registros = 0;
                switch (tabla.toLowerCase()) {
                    case "usuarios":
                        registros = usuariosDao().recuperaCount();
                        break;
                    case "tpvs":
                        registros = tpvsDao().recuperaCount();
                        break;
                    case "nom_mesas":
                        registros = nomMesasDao().recuperaCount();
                        break;
                    case "alergenos":
                        registros = alergenosDao().recuperaCount();
                        break;
                    case "etiquetas":
                        registros = etiquetasDao().recuperaCount();
                        break;
                    case "clientesctacasa":
                        registros = clientesCtaCasaDAO().recuperaCount();
                        break;
                    case "cabeceras":
                        registros = cabecerasDAO().recuperaCount();
                        break;
                    case "productos":
                        registros = productosDAO().recuperaCount();
                        break;
                    case "familias":
                        registros = familiasDao().recuperaCount();
                        break;
                    case "subfamilias":
                        registros = subfamiliasDao().recuperaCount();
                        break;
                    case "hora_comidas":
                        registros = horaComidasDao().recuperaCount();
                        break;

                }
                return  registros;
            }
        };
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<roomRegistrosCRC> recuperaMD5(final String tabla, final int min, final int max) throws ExecutionException,InterruptedException{
        Callable<List<roomRegistrosCRC>> callable = new Callable<List<roomRegistrosCRC>>() {
            @Override
            public List<roomRegistrosCRC> call() throws Exception {
                List<roomRegistrosCRC> lista;
                switch (tabla.toLowerCase()) {
                    case "usuarios":
                        lista =  usuariosDao().recuperaMD5(min,max);
                        return lista;
                    case "tpvs":
                        lista =  tpvsDao().recuperaMD5(min,max);
                        return lista;
                    case "nom_mesas":
                        lista =  nomMesasDao().recuperaMD5(min,max);
                        return lista;
                    case "alergenos":
                        lista =  alergenosDao().recuperaMD5(min,max);
                        return lista;
                    case "etiquetas":
                        lista =  etiquetasDao().recuperaMD5(min,max);
                        return lista;
                    case "clientesctacasa":
                        lista =  clientesCtaCasaDAO().recuperaMD5(min,max);
                        return lista;
                    case "cabeceras":
                        lista =  cabecerasDAO().recuperaMD5(min,max);
                        return lista;
                    case "productos":
                        lista =  productosDAO().recuperaMD5(min,max);
                        return lista;
                    case "familias":
                        lista =  familiasDao().recuperaMD5(min,max);
                        return lista;
                    case "subfamilias":
                        lista =  subfamiliasDao().recuperaMD5(min,max);
                        return lista;
                    case "hora_comidas":
                        lista =  horaComidasDao().recuperaMD5(min,max);
                        return lista;
                    default:
                        return null;
                }
            }
        };
        Future<List<roomRegistrosCRC>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public <T> void  upsetTabla(final T roomTabla, final int nregistros, final int pos, final int max, final boolean ultimo) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
           String clase =  roomTabla.getClass().getSimpleName();
           final String tabla = clase.toLowerCase();
           long p = 0;
           switch (clase.toLowerCase()) {
               case "usuarios":
                   usuariosDao().insertRegistro((Usuarios) roomTabla);
                   break;
               case "tpvs":
                    tpvsDao().insertRegistro((Tpvs) roomTabla);
                   break;
               case "nom_mesas":
                    nomMesasDao().insertRegistro((Nom_Mesas) roomTabla);
                   break;
               case "alergenos":
                    alergenosDao().insertRegistro((Alergenos) roomTabla);
                   break;
               case "etiquetas":
                    etiquetasDao().insertRegistro((Etiquetas) roomTabla);
                   break;
               case "clientesctacasa":
                  p=   clientesCtaCasaDAO().insertRegistro((ClientesCtaCasa) roomTabla);

                   break;
               case "cabeceras":
                    cabecerasDAO().insertRegistro((Cabeceras) roomTabla);
                   break;
               case "productos":
                    productosDAO().insertRegistro((Productos) roomTabla);
                   break;
               case "familias":
                   p =  familiasDao().insertRegistro((Familias) roomTabla);
                   break;
               case "subfamilias":
                    p = subfamiliasDao().insertRegistro((Subfamilias) roomTabla);
                   break;
               case "hora_comidas":
                    p = horaComidasDao().insertRegistro((Hora_Comidas) roomTabla);
                   break;
           }
            if (activity != null && handlerNotificaWS != null) {
                new Handler(Looper.getMainLooper()).post( () -> {
                    Message mensaje = handlerNotificaWS.obtainMessage(ACTION_GRABANDO_REGISTROS);
                    Bundle bundle = new Bundle();
                    bundle.putInt("pos", pos);
                    bundle.putInt("nregistros", nregistros);
                    bundle.putInt("max", max);
                    bundle.putBoolean("ultimo", ultimo);
                    bundle.putString("tabla",tabla);
                    mensaje.setData(bundle);
                    handlerNotificaWS.sendMessage(mensaje);

                });
            }

        });
    }


    public <T> Integer borraRegistro(final String tabla, final Integer  id) throws ExecutionException,InterruptedException{
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                switch (tabla.toLowerCase()){
                    case "usuarios":
                        usuariosDao().borraRegistro(id);
                        break;
                    case "tpvs":
                        tpvsDao().borraRegistro(id);
                        break;
                    case "nom_mesas":
                        nomMesasDao().borraRegistro(id);
                        break;
                    case "alergenos":
                        alergenosDao().borraRegistro(id);
                        break;
                    case "etiquetas":
                        etiquetasDao().borraRegistro(id);
                        break;
                    case "clientesctacasa":
                        clientesCtaCasaDAO().borraRegistro(id);
                        break;
                    case "cabeceras":
                        cabecerasDAO().borraRegistro(id);
                        break;
                    case "productos":
                        productosDAO().borraRegistro(id);
                        break;
                    case "familias":
                        familiasDao().borraRegistro(id);
                        break;
                    case "subfamilias":
                        subfamiliasDao().borraRegistro(id);
                        break;
                    case "hora_comidas":
                        horaComidasDao().borraRegistro(id);
                        break;
                }
                return -1;
            }
        };
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public <T> Integer borraRango(final T roomTabla, final int min, final int max) throws ExecutionException,InterruptedException{
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                String clase =  roomTabla.getClass().getSimpleName();
                switch (clase.toLowerCase()){
                    case "usuarios":
                        usuariosDao().borraRango(min,max);
                        break;
                    case "tpvs":
                        tpvsDao().borraRango(min,max);
                        break;
                    case "nom_mesas":
                        nomMesasDao().borraRango(min,max);
                        break;
                    case "etiquetas":
                        etiquetasDao().borraRango(min,max);
                        break;
                    case "alergenos":
                        alergenosDao().borraRango(min,max);
                        break;
                    case "clientesctacasa":
                        clientesCtaCasaDAO().borraRango(min,max);
                        break;
                    case "cabeceras":
                        cabecerasDAO().borraRango(min,max);
                        break;
                    case "productos":
                        productosDAO().borraRango(min,max);
                        break;
                    case "familias":
                        familiasDao().borraRango(min,max);
                        break;
                    case "subfamilias":
                        subfamiliasDao().borraRango(min,max);
                        break;
                    case "hora_comidas":
                        horaComidasDao().borraRango(min,max);break;
                }
                return -1;
            }
        };
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public <T> Integer borraTabla(final String tabla) throws ExecutionException,InterruptedException{
        final Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                switch (tabla.toLowerCase()){
                    case "usuarios":
                        usuariosDao().borraTodo();
                        break;
                    case "tpvs":
                        tpvsDao().borraTodo();
                        break;
                    case "nom_mesas":
                        nomMesasDao().borraTodo();
                        break;
                    case "etiquetas":
                        etiquetasDao().borraTodo();
                        break;
                    case "alergenos":
                        alergenosDao().borraTodo();
                        break;
                    case "clientesctacasa":
                        clientesCtaCasaDAO().borraTodo();
                        break;
                    case "cabeceras":
                        cabecerasDAO().borraTodo();
                        break;
                    case "productos":
                        productosDAO().borraTodo();
                        break;
                    case "familias":
                        familiasDao().borraTodo();
                        break;
                    case "subfamilias":
                        subfamiliasDao().borraTodo();
                        break;
                    case "hora_comidas":
                        horaComidasDao().borraTodo();
                        break;

                }
                return -1;
            }
        };
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }
    //endregion

    //region FUNCIONES BBDD USO APLICACION
    public List<Usuarios> appGetRecuperaUsuarios() throws ExecutionException,InterruptedException{
        Callable<List<Usuarios>> callable = new Callable<List<Usuarios>>() {
            @Override
            public List<Usuarios> call() throws Exception {
                List<Usuarios> lst=   usuariosDao().recuperaTodo();
                return lst;
            }
        };
        Future<List<Usuarios>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<Tpvs> appGetRecuperaTpvs() throws ExecutionException,InterruptedException{
        Callable<List<Tpvs>> callable = new Callable<List<Tpvs>>() {
            @Override
            public List<Tpvs> call() throws Exception {
                List<Tpvs> lst=   tpvsDao().recuperaTodo();
                return lst;
            }
        };
        Future<List<Tpvs>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<Nom_Mesas> appGetRecuperaNomMesas() throws ExecutionException,InterruptedException{
        Callable<List<Nom_Mesas>> callable = new Callable<List<Nom_Mesas>>() {
            @Override
            public List<Nom_Mesas> call() throws Exception {
                List<Nom_Mesas> lst=   nomMesasDao().recuperaTodo();
                return lst;
            }
        };
        Future<List<Nom_Mesas>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }
    public List<Etiquetas> appGetRecuperaEtiquetas() throws ExecutionException,InterruptedException{
        Callable<List<Etiquetas>> callable = new Callable<List<Etiquetas>>() {
            @Override
            public List<Etiquetas> call() throws Exception {
                List<Etiquetas> lst= etiquetasDao().recuperaTodo();
                return lst;
            }
        };
        Future<List<Etiquetas>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<Alergenos> appGetRecuperaAlergenos() throws ExecutionException,InterruptedException{
        Callable<List<Alergenos>> callable = new Callable<List<Alergenos>>() {
            @Override
            public List<Alergenos> call() throws Exception {
                List<Alergenos> lst= alergenosDao().recuperaTodo();
                return lst;
            }
        };
        Future<List<Alergenos>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }
    
    public List<ClientesCtaCasa> appGetRecuperaClientesCtaCasa() throws ExecutionException,InterruptedException{
        Callable<List<ClientesCtaCasa>> callable = new Callable<List<ClientesCtaCasa>>() {
            @Override
            public List<ClientesCtaCasa> call() throws Exception {
                List<ClientesCtaCasa> lst= clientesCtaCasaDAO().recuperaTodo();
                return lst;
            }
        };
        Future<List<ClientesCtaCasa>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }


    public String appGetRecuperaSubFamiliaExtras(int codsub) throws ExecutionException,InterruptedException{
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                String strExtras= subfamiliasDao().recuperaExtras(codsub);
                return strExtras;
            }
        };
        Future<String> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<Productos> appGetRecuperaProductosExtras(ArrayList<String> listaCodmenu, int tmenu) throws ExecutionException,InterruptedException{
        Callable<List<Productos>> callable = new Callable<List<Productos>>() {
            @Override
            public List<Productos> call() throws Exception {
                return productosDAO().recuperaProductosExtra(listaCodmenu,tmenu);
            }
        };
        Future<List<Productos>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }
    //endregion


}
