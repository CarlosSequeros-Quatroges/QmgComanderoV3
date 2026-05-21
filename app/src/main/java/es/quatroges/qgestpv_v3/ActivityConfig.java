package es.quatroges.qgestpv_v3;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import es.quatroges.qgestpv_v3.adapters.AdapterTabsPagerConfiguracion;
import es.quatroges.qgestpv_v3.basedatos.ClaseBaseDatos;
import es.quatroges.qgestpv_v3.configuracion.ClaseConfiguracion;
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
        int id = item.getItemId();

        if (id == R.id.action_cancelar) {
            finish();
            setResult(0);
        }
        else if (id ==  R.id.action_guardar) {
            int resultado =  guardarConfiguracion();
            if (resultado == 1) {
                setResult(1);
            }
            else{
                setResult(0);}
            finish();
        }
        return true;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
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
                int id = item.getItemId();
                if (id ==  R.id.action_cancelar) {
                    finish();
                    setResult(0);
                }
                else if (id == R.id.action_guardar) {
                    int resultado = guardarConfiguracion();
                    setResult(resultado);
                    finish();
                }
                return true;
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


