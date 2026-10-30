package es.quatroges.qgestpv_v3;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.util.Log;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import es.quatroges.qgestpv_v3.adapters.AdapterTabsPagerConfiguracion;
import es.quatroges.qgestpv_v3.basedatos.ClaseBaseDatos;
import es.quatroges.qgestpv_v3.configuracion.ClaseConfiguracion;
import es.quatroges.qgestpv_v3.configuracion.ConfiguracionJson;
import es.quatroges.qgestpv_v3.datos.Configuracion;

/**
 * Created by carlos on 07/07/2015.
 */
public class ActivityConfig extends AppCompatActivity implements
                            FragmentConfigParametros.ActivityCommunicator,
                            FragmentConfigWS.ActivityCommunicator,
                            FragmentConfigBd.ActivityCommunicator,
                            FragmentConfigPrinter.ActivityCommunicator {

    // control de versión movil (pager) o tablet (fragments)
    private ViewPager2 viewPager;
    private AdapterTabsPagerConfiguracion mAdapter;
    private TabLayout tabLayout;
    // Tab titles
    private String[] tabs = {"Parámetros","Servicio Web", "Base Datos", "Impresora"};


    private LinearLayout  frg1, frg2,fragments;
    private TextView tvFrg1, tvFrg2;
    private ImageView ivFrg1,ivFrg2;

    private ClaseConfiguracion configuracion;
    private ClaseBaseDatos bd;

    private static final String TAG = "ActivityConfig";
    /** true si se ha cargado una configuracion desde fichero o QR: al salir hay que avisar a ActivityInicio aunque se cancele */
    private boolean configuracionImportada = false;
    /** pestaña que estaba activa al importar, para volver a ella tras recrear la pantalla (estatico: sobrevive a recreate()) */
    private static int pestanaTrasImportar = -1;

    /**
     * Selector de archivos del sistema para cargar un conf-*.json desde Descargas u otra carpeta.
     * Desde Android 11 la app no puede listar ficheros de Descargas que no haya creado ella; el selector no necesita permisos.
     */
    private final ActivityResultLauncher<Intent> selectorFichero = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), resultado -> {
        if (resultado.getResultCode() != RESULT_OK || resultado.getData() == null || resultado.getData().getData() == null) return;
        importarUri(resultado.getData().getData());
    });

    /** Lector de QR: el contenido es el mismo JSON que genera la exportacion */
    private final ActivityResultLauncher<ScanOptions> lectorQR = registerForActivityResult(new ScanContract(), resultado -> {
        if (resultado.getContents() == null) {
            Toast.makeText(this, R.string.config_qr_cancelado, Toast.LENGTH_SHORT).show();
            return;
        }
        importarJson(resultado.getContents());
    });

    // INTERFACES DATOS
    @Override
    public Bundle parametrosGetDatos() {
        Bundle bundle = new Bundle();
        bundle.putString("demo", String.valueOf(configuracion.demo));
        bundle.putString("empresa", configuracion.empresa);
        bundle.putString("codigo", configuracion.codigo);
        bundle.putBoolean("cargaLineasVenta", configuracion.cargaLineasVenta);
        bundle.putBoolean("claveCamareros", configuracion.claveCamareros);
        bundle.putBoolean("pax", configuracion.pax);
        bundle.putInt("timerFragment", configuracion.timerFragment);
        bundle.putBoolean("pagoEfectivo",configuracion.pagoEfectivo);
        bundle.putBoolean("pagoTarjeta",configuracion.pagoTarjeta);
        bundle.putBoolean("pagoEfeTar",configuracion.pagoEfeTar);
        bundle.putBoolean("pagoCuentacasa",configuracion.pagoCuentacasa);
        bundle.putBoolean("pagoCredito",configuracion.pagoCredito);
        bundle.putBoolean("pagoPension",configuracion.pagoPension);
        bundle.putBoolean("mantenerVenta",configuracion.mantenerVenta);
        bundle.putBoolean("mostrarProductosAuto",configuracion.mostrarProductosAuto);
        bundle.putBoolean("mantenerProductos",configuracion.mantenerProductos);
        bundle.putBoolean("compacto",configuracion.compacto);

        return bundle;
    }

    @Override
    public Bundle wsGetDatos() {
        Bundle bundle = new Bundle();
        bundle.putString("url", configuracion.url);
        bundle.putString("api", configuracion.api);
        bundle.putString("deviceid",configuracion.deviceID);

        bundle.putInt("tocon",configuracion.connectionTO);
        bundle.putInt("torw1",configuracion.rwTO1);
        bundle.putInt("torw2",configuracion.rwTO2);
        bundle.putInt("torw3",configuracion.rwTO3);
        bundle.putInt("torw4",configuracion.rwTO4);
        bundle.putInt("torw5",configuracion.rwTO5);

        return bundle;
    }

    @Override
    public Bundle bdGetDatos() {

        Bundle bundle = new Bundle();
        bundle.putBoolean("sincronizaauto",configuracion.sincronizaAuto);
        return bundle;
    }

    @Override
    public Bundle printerGetDatos() {
        Bundle bundle = new Bundle();
        bundle.putString("btmodelo", configuracion.btModelo);
        bundle.putString("btmac", configuracion.btMac);
        bundle.putString("btname", configuracion.btName);
        bundle.putBoolean("comandaapp", configuracion.comandaapp);
        bundle.putBoolean("comandatpv", configuracion.comandatpv);
        bundle.putBoolean("cuentapteapp", configuracion.cuentapteapp);
        bundle.putBoolean("cuentaptetpv", configuracion.cuentaptetpv);
        bundle.putBoolean("cuentaapp", configuracion.cuentaapp);
        bundle.putBoolean("cuentatpv", configuracion.cuentatpv);

        return  bundle;
    }

    // FIN DE INTERFACES




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return accionMenu(item.getItemId());
    }

    /** Acciones de la cabecera (compartidas por la toolbar y el menu de opciones). */
    private boolean accionMenu(int id) {
        if (id == R.id.action_cancelar) {
            // si se importo una configuracion, la base de datos ya cambio: ActivityInicio debe recargarla igualmente
            setResult(configuracionImportada ? 1 : 0);
            finish();
        }
        else if (id == R.id.action_guardar) {
            int resultado = guardarConfiguracion();
            setResult(resultado == 0 && configuracionImportada ? 1 : resultado);
            finish();
        }
        else if (id == R.id.action_exportar) {
            exportarConfiguracion();
        }
        else if (id == R.id.action_importar) {
            elegirConfiguracionAImportar();
        }
        else if (id == R.id.action_mostrar_qr) {
            mostrarQrConfiguracion();
        }
        else if (id == R.id.action_leer_qr) {
            ScanOptions opciones = new ScanOptions();
            opciones.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
            opciones.setPrompt(getString(R.string.config_qr_prompt));
            opciones.setBeepEnabled(false);
            opciones.setOrientationLocked(true);
            lectorQR.launch(opciones);
        }
        return true;
    }

    /**
     * Muestra la configuracion guardada como codigo QR para que otro terminal la lea con "Leer configuracion de un QR".
     * El contenido es el JSON compacto de la tabla de configuracion (el mismo formato que exporta/importa).
     */
    private void mostrarQrConfiguracion() {
        try {
            String json = ConfiguracionJson.jsonActual(bd);

            DisplayMetrics dm = getResources().getDisplayMetrics();
            int lado = (int) (Math.min(dm.widthPixels, dm.heightPixels) * 0.85);

            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.MARGIN, 1);
            Bitmap qr = new BarcodeEncoder().encodeBitmap(json, BarcodeFormat.QR_CODE, lado, lado, hints);

            ImageView imagen = new ImageView(this);
            imagen.setImageBitmap(qr);
            imagen.setBackgroundColor(Color.WHITE);
            int margen = (int) (16 * dm.density);
            imagen.setPadding(margen, margen, margen, margen);
            imagen.setAdjustViewBounds(true);

            TextView ayuda = new TextView(this);
            ayuda.setText(R.string.config_qr_ayuda);
            ayuda.setGravity(Gravity.CENTER);
            ayuda.setPadding(margen, margen / 2, margen, margen / 2);

            LinearLayout contenedor = new LinearLayout(this);
            contenedor.setOrientation(LinearLayout.VERTICAL);
            contenedor.setGravity(Gravity.CENTER_HORIZONTAL);
            contenedor.addView(imagen);
            contenedor.addView(ayuda);

            String empresa = configuracion != null && configuracion.empresa != null ? configuracion.empresa : "";
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.config_qr_titulo, empresa))
                    .setView(contenedor)
                    .setPositiveButton(R.string.config_cerrar, null)
                    .show();
        } catch (Exception e) {
            Log.e(TAG, "Error generando el QR de configuracion", e);
            Toast.makeText(this, getString(R.string.config_error_qr) + "\n" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /** Guarda la configuracion almacenada en conf-actual.json dentro de la carpeta de la app. */
    private void exportarConfiguracion() {
        try {
            File fichero = ConfiguracionJson.exportar(this, bd);
            Toast.makeText(this, getString(R.string.config_exportada, fichero.getAbsolutePath()), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(TAG, "Error exportando la configuracion", e);
            Toast.makeText(this, getString(R.string.config_error_exportar) + "\n" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Muestra los conf-*.json de la carpeta de la app (se pierden al desinstalar) y una ultima entrada para buscar
     * en Descargas u otra carpeta con el selector del sistema (sobreviven a la reinstalacion).
     */
    private void elegirConfiguracionAImportar() {
        final List<File> ficheros = ConfiguracionJson.listar(this);
        if (ficheros.isEmpty()) {
            abrirSelectorFichero();
            return;
        }
        String[] nombres = new String[ficheros.size() + 1];
        for (int i = 0; i < ficheros.size(); i++) nombres[i] = ficheros.get(i).getName();
        nombres[ficheros.size()] = getString(R.string.config_buscar_fichero);

        new AlertDialog.Builder(this)
                .setTitle(R.string.config_elegir_fichero)
                .setItems(nombres, (dialog, which) -> {
                    if (which < ficheros.size()) importarFichero(ficheros.get(which));
                    else abrirSelectorFichero();
                })
                .setNegativeButton(R.string.config_cancelar, null)
                .show();
    }

    /** Abre el selector de archivos del sistema situado en Descargas. */
    private void abrirSelectorFichero() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"application/json", "text/plain", "application/octet-stream"});
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI,
                DocumentsContract.buildDocumentUri("com.android.externalstorage.documents", "primary:Download"));
        try {
            selectorFichero.launch(intent);
        } catch (Exception e) {
            Log.e(TAG, "No se ha podido abrir el selector de archivos", e);
            Toast.makeText(this, getString(R.string.config_error_importar) + "\n" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /** Carga un fichero elegido con el selector del sistema (URI de contenido). */
    private void importarUri(Uri uri) {
        try (InputStream is = getContentResolver().openInputStream(uri)) {
            if (is == null) throw new IOException("No se puede leer " + uri);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int n;
            while ((n = is.read(buf)) != -1) bos.write(buf, 0, n);
            int parametros = ConfiguracionJson.importarJson(bd, new String(bos.toByteArray(), StandardCharsets.UTF_8));
            configuracionCargada(parametros);
        } catch (Exception e) {
            Log.e(TAG, "Error importando " + uri, e);
            Toast.makeText(this, getString(R.string.config_error_importar) + "\n" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void importarFichero(File fichero) {
        try {
            int n = ConfiguracionJson.importarFichero(bd, fichero);
            configuracionCargada(n);
        } catch (Exception e) {
            Log.e(TAG, "Error importando " + fichero.getName(), e);
            Toast.makeText(this, getString(R.string.config_error_importar) + "\n" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void importarJson(String json) {
        try {
            int n = ConfiguracionJson.importarJson(bd, json);
            configuracionCargada(n);
        } catch (Exception e) {
            Log.e(TAG, "Error importando configuracion desde QR", e);
            Toast.makeText(this, getString(R.string.config_error_importar) + "\n" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /** Tras cargar en la base de datos, se vuelve a crear la pantalla para que las pestañas muestren los valores nuevos. */
    private void configuracionCargada(int parametros) {
        configuracionImportada = true;
        pestanaTrasImportar = viewPager != null ? viewPager.getCurrentItem() : -1;
        Toast.makeText(this, getString(R.string.config_importada, parametros), Toast.LENGTH_LONG).show();
        recreate();
    }

    @Override
    protected void onCreate (Bundle savedInstanceState){
        boolean importada = savedInstanceState != null && savedInstanceState.getBoolean("configuracionImportada", false);
        // Tras importar se descarta el estado guardado: si no, Android restaura el texto de los campos y las casillas
        // por encima de los valores recien cargados en la base de datos y la pantalla seguiria mostrando los antiguos.
        super.onCreate(importada ? null : savedInstanceState);
        configuracionImportada = importada;
        setContentView(R.layout.cfg_activity_config);
        Toolbar toolbar = findViewById(R.id.toolbar);



        bd = ClaseBaseDatos.getDatabase(this,null,this);
        configuracion = new ClaseConfiguracion(this);
        cargaConfiguracion();

        // Initilization
        toolbar.setTitle("Configuración");
        toolbar.inflateMenu(R.menu.activity_config);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return accionMenu(item.getItemId());
            }
        });

        viewPager = findViewById(R.id.pager);
        mAdapter = new AdapterTabsPagerConfiguracion(this);
        mAdapter.addFragment(FragmentConfigParametros.newInstance());
        mAdapter.addFragment(FragmentConfigWS.newInstance());
        mAdapter.addFragment(FragmentConfigBd.newInstance());
        mAdapter.addFragment(FragmentConfigPrinter.newInstance());

        viewPager.setAdapter(mAdapter);

        tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout,viewPager,(tab,position)-> {
            tab.setText(tabs[position]);
        }).attach();

        if (pestanaTrasImportar >= 0) {
            viewPager.setCurrentItem(pestanaTrasImportar, false);
            pestanaTrasImportar = -1;
        }


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Tras importar no se restaura la jerarquia de vistas: los fragmentos ya se han creado de nuevo con los
        // valores importados y ViewPager2 intentaria recuperar fragmentos que ya no existen.
        if (configuracionImportada) return;
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("configuracionImportada", configuracionImportada);
    }

    private int guardarConfiguracion()
    {
        int resultado  = 0;
        String bdAccion;
        boolean noBorraConfig  = true;

        FragmentConfigParametros fragmentConfigParametros = (FragmentConfigParametros) mAdapter.getFragment(0);
        FragmentConfigWS fragmentConfigWS = (FragmentConfigWS) mAdapter.getFragment(1);
        FragmentConfigBd fragmentConfigBd = (FragmentConfigBd) mAdapter.getFragment(2);
        FragmentConfigPrinter fragmentConfigPrinter = (FragmentConfigPrinter) mAdapter.getFragment(3);


        Bundle bDatosParametros = fragmentConfigParametros.MandaDatos();
        Bundle bDatosWS = fragmentConfigWS.MandaDatos();
        Bundle bDatosBD = fragmentConfigBd.MandaDatos();
        Bundle bDatosPrinter = fragmentConfigPrinter.MandaDatos();


        bd.guardarConfiguracion(bDatosParametros, bDatosWS, bDatosPrinter, bDatosBD);
        resultado = 1;

        if ( bDatosBD != null ) {
            bdAccion = bDatosBD.getString("bdaccion");
            noBorraConfig = bDatosBD.getBoolean("noborraconfig");

            if (bdAccion != null) {
                if (bdAccion.equals("BORRAR")) {
                    bd.borrarTablas(noBorraConfig);
                    resultado = 2;

                } else if (bdAccion.equals("BD2SD")) {
                    try {
                        bd.backup(this);
                        resultado = 1;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (bdAccion.equals("SD2BD")) {
                    try {
                        bd.restore(this);
                        resultado = 1;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return	resultado;
    }





    private void cargaConfiguracion(){
        try {

            List<Configuracion> parametros =  bd.getConfiguracion();
            if (parametros!= null && parametros.size() > 0 ) {
                for (Configuracion parametro: parametros){
                    switch (parametro.getParametro().toLowerCase()){
//parametros
                        case "demo":
                            if (parametro.getValor().toLowerCase().equals("true"))
                                configuracion.demo = true;
                            else
                                configuracion.demo =false;
                            break;
                        case "empresa":
                            configuracion.empresa = parametro.getValor();
                            break;
                        case "codigo":
                            configuracion.codigo= parametro.getValor();
                            break;
                        case "carga_lineas_venta":
                            if (parametro.getValor().toLowerCase().equals("true"))
                                configuracion.cargaLineasVenta = true;
                            else
                                configuracion.cargaLineasVenta =false;
                            break;
                        case "clave_camareros":
                            if (parametro.getValor().toLowerCase().equals("true"))
                                configuracion.claveCamareros = true;
                            else
                                configuracion.claveCamareros =false;
                            break;
                        case "pax":
                            if (parametro.getValor().toLowerCase().equals("true"))
                                configuracion.pax= true;
                            else
                                configuracion.pax=false;
                            break;
                        case "pagoefectivo":
                            configuracion.pagoEfectivo = Boolean.valueOf(parametro.getValor().toLowerCase());
                            break;
                        case "pagotarjeta":
                            configuracion.pagoTarjeta = Boolean.valueOf(parametro.getValor().toLowerCase());
                            break;
                        case "pagoefetar":
                            configuracion.pagoEfeTar = Boolean.valueOf(parametro.getValor().toLowerCase());
                            break;
                        case "pagocuentacasa":
                            configuracion.pagoCuentacasa = Boolean.valueOf(parametro.getValor().toLowerCase());
                            break;
                        case "pagocredito":
                            configuracion.pagoCredito = Boolean.valueOf(parametro.getValor().toLowerCase());
                            break;
                        case "pagopension":
                            configuracion.pagoPension = Boolean.valueOf(parametro.getValor().toLowerCase());
                            break;
                        case "timer_fragment":
                            configuracion.timerFragment = Integer.valueOf(parametro.getValor());
                            break;
                        case "mantenerventa":
                            configuracion.mantenerVenta = Boolean.valueOf(parametro.getValor().toLowerCase());
                            break;
                        case "mostrarproductosauto":
                            configuracion.mostrarProductosAuto = Boolean.valueOf(parametro.getValor().toLowerCase());
                            break;
                        case "mantenerproductos":
                            configuracion.mantenerProductos = Boolean.valueOf(parametro.getValor().toLowerCase());
                            break;
                        case "compacto":
                            configuracion.compacto = Boolean.valueOf(parametro.getValor().toLowerCase());
                            break;

//servicio web
                        case "url":
                            configuracion.url= parametro.getValor();
                            if (! configuracion.url.endsWith(("/")))
                                configuracion.url+= "/";
                            break;
                        case "api":
                            configuracion.api= parametro.getValor();
                            break;
                        case "tocon":
                            configuracion.connectionTO = Integer.valueOf(parametro.getValor().toLowerCase());
                            break;
                        case "torw1":
                            configuracion.rwTO1 = Integer.valueOf(parametro.getValor().toLowerCase());
                            break;
                        case "torw2":
                            configuracion.rwTO2 = Integer.valueOf(parametro.getValor().toLowerCase());
                            break;
                        case "torw3":
                            configuracion.rwTO3 = Integer.valueOf(parametro.getValor().toLowerCase());
                            break;
                        case "torw4":
                            configuracion.rwTO4 = Integer.valueOf(parametro.getValor().toLowerCase());
                            break;
                        case "torw5":
                            configuracion.rwTO5 = Integer.valueOf(parametro.getValor().toLowerCase());
                            break;
//base de datos
                        case "sincronizaauto":
                            configuracion.sincronizaAuto = Boolean.valueOf(parametro.getValor().toLowerCase());
                            break;
//impresora
                        case "btmac":
                            configuracion.btMac = parametro.getValor().toLowerCase(Locale.ROOT);
                            break;
                        case "btmodelo":
                            configuracion.btModelo = parametro.getValor().toLowerCase(Locale.ROOT);
                            break;
                        case "btname":
                            configuracion.btName = parametro.getValor().toLowerCase(Locale.ROOT);
                            break;
                        case "comandaapp":
                            configuracion.comandaapp = parametro.getValor().toLowerCase(Locale.ROOT).equals("false")? false:true;
                            break;
                        case "comandatpv":
                            configuracion.comandatpv = parametro.getValor().toLowerCase(Locale.ROOT).equals("false")? false:true;
                            break;
                        case "cuentapteapp":
                            configuracion.cuentapteapp = parametro.getValor().toLowerCase(Locale.ROOT).equals("false")? false:true;
                            break;
                        case "cuentaptetpv":
                            configuracion.cuentaptetpv = parametro.getValor().toLowerCase(Locale.ROOT).equals("false")? false:true;
                            break;
                        case "cuentaapp":
                            configuracion.cuentaapp = parametro.getValor().toLowerCase(Locale.ROOT).equals("false")? false:true;
                            break;
                        case "cuentatpv":
                            configuracion.cuentatpv = parametro.getValor().toLowerCase(Locale.ROOT).equals("false")? false:true;
                            break;
                    }
                }
            }
        }
        catch (ExecutionException ee){}
        catch (InterruptedException ie){}

    }

}


