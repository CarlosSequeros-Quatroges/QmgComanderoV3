package es.quatroges.qgestpv_v3;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static es.quatroges.qgestpv_v3.basedatos.ClaseBaseDatos.ACTION_GRABANDO_ALERGENOS;
import static es.quatroges.qgestpv_v3.basedatos.ClaseBaseDatos.ACTION_GRABANDO_CABECERAS;
import static es.quatroges.qgestpv_v3.basedatos.ClaseBaseDatos.ACTION_GRABANDO_ETIQUETAS;
import static es.quatroges.qgestpv_v3.basedatos.ClaseBaseDatos.ACTION_GRABANDO_NOMMESAS;
import static es.quatroges.qgestpv_v3.basedatos.ClaseBaseDatos.ACTION_GRABANDO_PRODUCTOS;
import static es.quatroges.qgestpv_v3.basedatos.ClaseBaseDatos.ACTION_GRABANDO_TPVS;
import static es.quatroges.qgestpv_v3.basedatos.ClaseBaseDatos.ACTION_GRABANDO_USUARIOS;
import static es.quatroges.qgestpv_v3.bluetooth.ClaseBluetoothPrintConstantes.MODEL_SCREEN;
import static es.quatroges.qgestpv_v3.servicios.ServSincronizaBD.ACTION_BORRANDO_REGISTROS;
import static es.quatroges.qgestpv_v3.servicios.ServSincronizaBD.ACTION_DESCARGANDO_REGISTROS;
import static es.quatroges.qgestpv_v3.servicios.ServSincronizaBD.ACTION_GRABANDO_REGISTROS;
import static es.quatroges.qgestpv_v3.utils.ClaseUtils.ORDEN_COMANDA_ENTRANTE;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.gcacace.signaturepad.BuildConfig;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import es.quatroges.qgestpv_v3.basedatos.ClaseBaseDatos;
import es.quatroges.qgestpv_v3.bluetooth.ClaseBluetooth;
import es.quatroges.qgestpv_v3.bluetooth.ClaseBluetoothMsgTypes;
import es.quatroges.qgestpv_v3.bluetooth.ClaseBluetoothPrintConstantes;
import es.quatroges.qgestpv_v3.configuracion.ClaseCondicionesVenta;
import es.quatroges.qgestpv_v3.configuracion.ClaseConfiguracion;
import es.quatroges.qgestpv_v3.configuracion.ClaseUltimoEstadoApp;
import es.quatroges.qgestpv_v3.datos.ActualizaPax;
import es.quatroges.qgestpv_v3.datos.Alergenos;
import es.quatroges.qgestpv_v3.datos.CabeceraTiquet;
import es.quatroges.qgestpv_v3.datos.Cabeceras;
import es.quatroges.qgestpv_v3.datos.ClientesCtaCasa;
import es.quatroges.qgestpv_v3.datos.Configuracion;
import es.quatroges.qgestpv_v3.datos.EstadoMesas;
import es.quatroges.qgestpv_v3.datos.Etiquetas;
import es.quatroges.qgestpv_v3.datos.GrabaLineasVenta;
import es.quatroges.qgestpv_v3.datos.Hora_Comidas;
import es.quatroges.qgestpv_v3.datos.LineasVenta;
import es.quatroges.qgestpv_v3.datos.Nom_Mesas;
import es.quatroges.qgestpv_v3.datos.OrdenPlatos;
import es.quatroges.qgestpv_v3.datos.PensionesSubmesa;
import es.quatroges.qgestpv_v3.datos.Productos;
import es.quatroges.qgestpv_v3.datos.ResGrabaLineas;
import es.quatroges.qgestpv_v3.datos.ResGrabaLineasSubmesas;
import es.quatroges.qgestpv_v3.datos.Tpvs;
import es.quatroges.qgestpv_v3.datos.Usuarios;
import es.quatroges.qgestpv_v3.datos.listas.cabeceras.ClaseCabeceras;
import es.quatroges.qgestpv_v3.datos.listas.clientesCtaCasa.ClaseClientesCtaCasa;
import es.quatroges.qgestpv_v3.datos.listas.establecimientos.ClaseEstablecimientos;
import es.quatroges.qgestpv_v3.datos.listas.facturas.ClaseFacturaCabecera;
import es.quatroges.qgestpv_v3.datos.listas.facturas.ClaseFacturas;
import es.quatroges.qgestpv_v3.datos.listas.lineaVentas.ClaseLineaVentas;
import es.quatroges.qgestpv_v3.datos.listas.lineaVentas.LineaVentaMapper;
import es.quatroges.qgestpv_v3.datos.listas.mesas.ClaseMesas;
import es.quatroges.qgestpv_v3.datos.listas.mesas.ClaseSubMesas;
import es.quatroges.qgestpv_v3.datos.listas.productos.ClaseProductos;
import es.quatroges.qgestpv_v3.datos.listas.tpvs.ClaseTPVs;
import es.quatroges.qgestpv_v3.datos.listas.users.ClaseUsers;
import es.quatroges.qgestpv_v3.nfc.ClaseNFC;
import es.quatroges.qgestpv_v3.servicios.ServSincronizaBD;
import es.quatroges.qgestpv_v3.utils.ClaseItemExtra;
import es.quatroges.qgestpv_v3.utils.ClaseItemFiltro;
import es.quatroges.qgestpv_v3.utils.ClaseLatencia;
import es.quatroges.qgestpv_v3.utils.ClaseOnTouch;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;
import es.quatroges.qgestpv_v3.webservice.APIService;
import es.quatroges.qgestpv_v3.webservice.ClaseServicioWeb;
import es.quatroges.qgestpv_v3.webservice.RespuestaTestjsonWS;
import es.quatroges.qgestpv_v3.webservice.RespuestaValidarUpdateWS;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityInicio extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        FragmentCabeceras.InterfaceCabeceras,
        FragmentProductos.InterfaceProductos,
        FragmentMesas.InterfaceMesas,
        FragmentSubmesas.InterfaceSubmesas,
        FragmentLineaVentas.InterfaceLineaVentas,
        FragmentDetalleVenta.InterfaceDetalleVenta,
        FragmentTPVs.InterfaceTPVs,
        FragmentUsers.InterfaceUsers,
        FragmentFormaPago.InterfaceFormaPago,
        FragmentFacturas.InterfaceFacturas,
        ClaseUtils.FiltarPlatos.InterfaceUtilsFiltraPlatos {

    public static ActivityInicio activityInicio;
    private static final int REQUEST_APP_CONFIGURE = 1;
    private static final String TAG = "ActivityInicio";

    private static boolean nofinalizar;
    private enum enAccionesWS {
        test, validar, sincronizar, sincronizarbg,
        getEstadoMesas, getLineasMesa, enviaLineasMesa,
        actualizaPax, siguePlato, traspasaMesa,
        pedirCuenta, creditoTagID, creditoRoom, pagaMesa,
        recuperaFacturas, recuperaFactura, imprimeFactura
    }

    private static enAccionesWS ultimaAccionWs;
    private static int reintentosWS;
    private static Context context;


    private static ArgActualizaPax argActualizaPax;
    private static ArgTraspasaMesa argTraspasaMesa;
    private static ArgEnviaLineasMesa argEnviaLineasMesa;
    private static ArgSiguePlato argSiguePlato;
    private static ArgCreditoTagID argCreditoTagID;
    private static ArgCreditoRoom argCreditoRoom;
    private static ArgPagaMesa argPagaMesa;
    private static ArgRecuperaFactura argRecuperaFactura;
    private static ArgImprimeFactura argImprimeFactura;


    private static boolean updateUsers = false;
    private static boolean updateTPVS = false;
    private static boolean updateNomMesas = false;

    public enum enEstado {
        iniciando, user, tpv, cargafacturas, facturas, cargamesas, mostrarmesas, cargalineas, venta, cuenta, pago, traspasa, traspasa_sub, cerrar, salir
    }


    private static ArrayList<String> tablasSync;
    private static ArrayList<String> tablasSyncBackGround;
    private static boolean conexionValidada;

    private static String estadoConexion;

    //variables BT
    private static int estadoBT;
    private static Handler hndDesconectaSocket;


    private static boolean boolOnCreate;

    private static ArrayList<ClaseUsers> listaUsers;
    private static ClaseUsers user;
    private static int userSel;

    private static ArrayList<ClaseTPVs> listaTPVs;
    private static ClaseTPVs tpv;
    private static int tpvSel;

    private static ArrayList<ClaseCabeceras> listaCabeceras;
    private static ClaseCabeceras cabecera;
    private static int cabeceraSel;

    private static ArrayList<ClaseProductos> listaProductos;
    private static ClaseProductos producto;
    private static int productoSel;
    private static ArrayList<Alergenos> listaAlergenos;
    private static ArrayList<Etiquetas> listaEtiquetas;
    private static ArrayList<OrdenPlatos> listaOrdenPlatos;


    private static TreeMap<Integer, ClaseMesas> listaMesas;
    private static ClaseMesas mesa;
    private static int mesaSel, actualizaPax_mesasel, traspasaMesa_mesasel;

    private static TreeMap<String, Nom_Mesas> listaNombreMesas;


    private static ClaseSubMesas submesa;
    private static int subMesaSel;
    private static int submesaSelPendiente;

    private static int ordenSel;

    private static ArrayList<ClaseFacturas> listaFacturas;


    private static ArrayList<ClaseLineaVentas> listaLineaVentas;
    private static ClaseLineaVentas lineaVenta;
    private static int lineaSel;
    private static int lineaSelPendiente;

    private static boolean recalculaPension;

    private static ArrayList<ClaseClientesCtaCasa> listaClientesCtaCasa;
    private static int clienteCtaCasaSel;
    private static FragmentInicio fragmentInicio;
    private static FragmentUsers fragmentUsers;
    private static FragmentTPVs fragmentTPVs;
    private static FragmentFacturas fragmentFacturas;
    private static FragmentCabeceras fragmentCabeceras;
    private static FragmentProductos fragmentProductos;
    private static FragmentMesas fragmentMesas;
    private static FragmentSubmesas fragmentSubmesas;
    private static FragmentLineaVentas fragmentLineaVentas;
    private static FragmentDetalleVenta fragmentDetalleVenta;
    private static FragmentFormaPago fragmentFormaPago;

    private static LinearLayout layFrgInicio, layFrgUsers, layFrgTPVs, layFrgFacturas, layFrgCabeceras, layFrgProductos, layFrgMesas, layFrgVentas, layFrgFormaPago,
            layFrgLineaVentas, layFrgDetalleVenta;

    private static ConstraintLayout layCompacto;

    private static TextView tvBarUser, tvBarTPV, tvBarMesa, tvBarPax, tvMenuTpv, tvMenuUser, tvCabecera, tvBarRoom, tvLatencia, tvBarPension;
    private static ImageView ivAddSubmesa, ivBarPax, ivCabeceras, ivPlatos, ivBarRoom, ivAplicaPension;

    private static LinearLayout layProgress;
    private static TextView tvProgress;
    private static ProgressBar pbProgress;

    private NavigationView navigationView;
    private static Menu toolBarMenu;
    private static Menu navigationMenu;
    private static MenuItem mesasMenu;
    private MenuItem tpvMenu;

    private static enEstado estado;
    private static enEstado ultimoEstado;

    private static ClaseUltimoEstadoApp ultimoEstadoApp;
    private static enEstado estadoPendiente;

    private boolean isHappyHour;

    private static ClaseConfiguracion configuracion;
    private static ClaseBaseDatos baseDatos;

    private static Handler hdDelay;
    private static Handler hSyncDatos;

    private static Handler hActualizaDetalle;

    public static ClaseLatencia latenciaTest, latenciaRecuperaMesas,latenciaRecuperaComanda, latenciaEnviaComanda, latenciaPideCuenta, latenciaPagaCuenta;

    private static String appfile;

    //region inteface fragmentUsers
    @Override
    public Bundle cargaUsers() {
        Bundle bundle = new Bundle();

        bundle.putBoolean("claveCamareros", ClaseCondicionesVenta.pedir_clave.equals("S") ? true : false);
        bundle.putInt("userSel", userSel);
        bundle.putParcelableArrayList("users", listaUsers);
        return bundle;
    }

    @Override
    public void onClickUser(int i) {
        ultimoEstadoApp.setEstado(enEstado.user);
        ultimoEstadoApp.setUsuario(i);
        ultimoEstadoApp.setTpv(-1);
        guardaUltimoEstadoApp();
        seleccionaUsuario(i);
    }

    private void seleccionaUsuario(int i){
        if (listaUsers.isEmpty() || listaUsers.size() <= i) return;

        userSel = i;

        user = listaUsers.get(i);
        ultimoEstado = estado;
        estado = enEstado.tpv;
        cambiaEstado(estado);
        if (ClaseUtils.modelo == ClaseUtils.enModel.tab)
            tvBarUser.setText(user.nombre.toUpperCase());
        tvMenuUser.setText(user.nombre.toUpperCase());

        if (navigationMenu.findItem(R.id.nav_logout) != null)
            navigationMenu.findItem(R.id.nav_logout).setVisible(true);
        if (navigationMenu.findItem(R.id.nav_user) != null)
            navigationMenu.findItem(R.id.nav_user).setVisible(false);

    }
    //endregion

    //region interface fragmentTPVS
    @Override
    public Bundle cargaTPVs() {
        Bundle bundle = new Bundle();
        bundle.putInt("tpvSel", tpvSel);
        bundle.putParcelableArrayList("tpvs", listaTPVs);
        return bundle;
    }

    @Override
    public void onClickTPV(int i) {

        ultimoEstadoApp.setEstado(enEstado.tpv);
        ultimoEstadoApp.setUsuario(userSel);
        ultimoEstadoApp.setTpv(i);
        guardaUltimoEstadoApp();
        seleccionaTPV(i);

    }

    private void seleccionaTPV(int i){
        tpvSel = i;
        tpv = listaTPVs.get(i);
        ultimoEstado = estado;
        estado = enEstado.cargamesas;
        cambiaEstado(estado);
        if (ClaseUtils.modelo == ClaseUtils.enModel.tab)
            tvBarTPV.setText(tpv.codtpv.toUpperCase());
        tvMenuTpv.setText("(" + tpv.descripcion.toUpperCase() + ")");
    }
    //endregion

    //region Interface fragmentFacturas

    @Override
    public Bundle cargaFacturas() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("facturas", listaFacturas);
        return bundle;
    }

    @Override
    public void onClickFactura(int codenl) {
        argRecuperaFactura = ArgRecuperaFactura.getINSTANCE();
        argRecuperaFactura.setCodenl(codenl);

        ultimaAccionWs = enAccionesWS.recuperaFactura;
        reintentosWS = 0;
        conectaWS(ultimaAccionWs, argRecuperaFactura);
    }

    @Override
    public void onClickVolver() {
        ultimoEstado = estado;
        estado = estadoPendiente;
        cambiaEstado(estado);
    }

    //endregion

    //region Interface fragmentMesas
    @Override
    public Bundle cargaMesas(boolean refresca) {
        //  if (! refresca) listaMesas= ClaseMesas.recuperaMesas(tpv);
        // llamar a servicio web y actualizar mesas

        Bundle bundle = new Bundle();
        bundle.putInt("mesaSel", mesaSel);
        bundle.putBoolean("pidePax", configuracion.pax);
        bundle.putBoolean("pideRoom", configuracion.pax);
        bundle.putSerializable("mesas", listaMesas);
        return bundle;
    }

    @Override
    public void onClickMesa(int i, int pax, String habitacion) {
        seleccionaMesa(i, pax, habitacion);
    }

    @Override
    public void onLongClickMesa(int i) {
        boolean pagar = false;
        mesa = listaMesas.get(i);
        mesaSel = i;
        submesa = mesa.submesas.get(0);
        subMesaSel = 0;
        for (int t = 0; t < mesa.submesas.size();t++){
            if (mesa.submesas.get(t).estado.equals(ClaseSubMesas.enEstados.tiquet)){
                submesa = mesa.submesas.get(t);
                subMesaSel = t;
                pagar = true;
                break;
            }
        }

        tvBarMesa.setText(mesa.descripcion + "-" + Integer.toString(submesa.submesa));
        tvBarPax.setText(Integer.toString(submesa.pax));
        tvBarRoom.setText(submesa.habitacion);
        muestraPension(submesa);

        if (pagar) {
            ultimoEstado = enEstado.mostrarmesas;
            estado = enEstado.pago;
            cambiaEstado(estado);
        }
    }

    public static void seleccionaMesa(int i, int pax, String habitacion) {
        mesa = listaMesas.get(i);
        mesaSel = i;

        submesa = mesa.submesas.get(0);
        subMesaSel = 0;

        if (mesaSel != -1) {
            mesa = listaMesas.get(mesaSel);
            mesa.pax = pax;
            if (mesa.submesas.size() == 1) {
                mesa.submesas.get(0).pax = pax;
                mesa.submesas.get(0).habitacion = habitacion;
            }
            for (ClaseSubMesas subMesa : mesa.submesas) {

                if (subMesa.estado == ClaseSubMesas.enEstados.abierta) mesa.abiertas = true;
                if (subMesa.estado == ClaseSubMesas.enEstados.tiquet) mesa.tickets = true;
                if (subMesa.horaApertura.equalsIgnoreCase("") ) subMesa.horaApertura = ClaseUtils.now("HH:mm:ss");
            }
            listaMesas.remove(mesaSel);

            listaMesas.put(mesaSel, mesa);

            ultimoEstado = enEstado.mostrarmesas;
            estado = enEstado.cargalineas;
            cambiaEstado(estado);
            tvBarMesa.setText(mesa.descripcion + "-" + Integer.toString(submesa.submesa));

            tvBarPax.setText("");
            tvBarRoom.setText("");

            muestraPension(null);


            if (mesa.submesas != null && mesa.submesas.size() > 0 ){
                tvBarPax.setText(Integer.toString(mesa.submesas.get(0).pax));
                tvBarRoom.setText(mesa.submesas.get(0).habitacion);
                muestraPension(mesa.submesas.get(0));

                if (mesa.submesas.get(0).aplicaPension){
                    ivAplicaPension.setImageResource(R.drawable.aceptar);
                }

            }




            if (fragmentMesas != null) fragmentMesas.actualizaMesas(true);
            if (fragmentSubmesas != null) fragmentSubmesas.actualizaSubmesas();
        }

    }

    @Override
    public void actualizaPax(ArrayList<ClaseSubMesas> subMesas, int mesaSel) {
        actualizaPax_mesasel = mesaSel;
        ArrayList<ActualizaPax> submesasPax = new ArrayList<>();
        for (ClaseSubMesas submesa : subMesas) {
            ActualizaPax submesaPax = new ActualizaPax();
            submesaPax.setCodenl(submesa.codenl);
            submesaPax.setSubmesa(submesa.submesa);
            submesaPax.setNpax(submesa.pax);
            submesaPax.setHabitacion(submesa.habitacion);
            submesasPax.add(submesaPax);


        }
        argActualizaPax = ArgActualizaPax.getINSTANCE();
        argActualizaPax.setSubmesasPax(submesasPax);
        ultimaAccionWs = enAccionesWS.actualizaPax;
        reintentosWS = 0;
        conectaWS(ultimaAccionWs, argActualizaPax);
    }

    //endregion

    //region Interface Submesas

    @Override
    public Bundle cargaSubmesas() {
        Bundle bundle = new Bundle();
        bundle.putInt("submesaSel", subMesaSel);
        if (mesa == null || mesa.submesas == null)
            bundle.putParcelableArrayList("submesas", new ArrayList<ClaseSubMesas>());
        else
            bundle.putParcelableArrayList("submesas", mesa.submesas);

        return bundle;
    }

    @Override
    public void onClickSubmesa(int i) {
        submesaSelPendiente = i;
        lineaSelPendiente = -1;
        estadoPendiente = enEstado.venta;

        if (!compruebaCambiosLineaPendientes()) {
            cambiaSubmesa(i);
        }
    }


    //endregion

    //region Interface fragmentCabeceras
    @Override
    public Bundle cargaCabeceras() {
        Bundle bundle = new Bundle();
        bundle.putInt("cabeceraSel", cabeceraSel);
        bundle.putInt("milisg", configuracion.timerFragment * 1000);
        bundle.putParcelableArrayList("cabeceras", listaCabeceras);
        return bundle;
    }

    @Override
    public void onClickCabecera(int i) {
        cabeceraSel = -1;
        for (ClaseCabeceras cabecera : listaCabeceras) {
            if (cabecera.codigo == i) {
                cabeceraSel = listaCabeceras.indexOf(cabecera);
                break;
            }
        }

        if (cabeceraSel != -1) {
            cabecera = listaCabeceras.get(cabeceraSel);
            if (tvCabecera != null) {
                tvCabecera.setText(cabecera.descripcion);
            }
            //   tvCabecera.setText(cabecera.descripcion);

            if (fragmentProductos != null) {
                cargaEtiquetas(); // al cambiar de cabecera limpio las etiquetas seleccionadas
                fragmentProductos.actualizaProductos(true);
                fragmentProductos.productoSel = -1;
            }

            if (fragmentCabeceras != null) fragmentCabeceras.cabeceraSel = cabeceraSel;

            if (configuracion.mostrarProductosAuto) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (fragmentProductos != null) fragmentProductos.ampliarProductos();
                    }
                }, 200);
            }
            if (configuracion.compacto) {
                layCompacto.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void fc_ocultaElementos(boolean ocultar) {
        if (estado == enEstado.venta)

            if (!ocultar) {
                if (layCompacto != null && layCompacto.getVisibility() == VISIBLE)
                    layCompacto.setVisibility(View.GONE);

                if (layFrgCabeceras != null && layFrgCabeceras.getVisibility() == VISIBLE && configuracion.compacto) {
                    layFrgCabeceras.setVisibility(View.GONE);
                }
                if (fragmentCabeceras != null) fragmentCabeceras.posicionaCabecera();


                if (layFrgProductos != null && layFrgProductos.getVisibility() == VISIBLE)
                    layFrgProductos.setVisibility(View.GONE);
                if (fragmentProductos != null) fragmentProductos.posicionaProducto();


                if (layFrgVentas != null && layFrgVentas.getVisibility() == VISIBLE) {
                    layFrgVentas.setVisibility(View.GONE);
                    if (fragmentDetalleVenta != null)
                        fragmentDetalleVenta.estadoFragmentActivo(false);
                }

            } else {
                if (layCompacto != null && layCompacto.getVisibility() == View.GONE && configuracion.compacto) {
                    layCompacto.setVisibility(VISIBLE);
                    layFrgCabeceras.setVisibility(View.GONE);
                    layFrgProductos.setVisibility(View.GONE);
                }

                if (layFrgCabeceras != null && layFrgCabeceras.getVisibility() == View.GONE && !configuracion.compacto)
                    layFrgCabeceras.setVisibility(VISIBLE);
                if (fragmentCabeceras != null) fragmentCabeceras.posicionaCabecera();


                if (layFrgProductos != null && layFrgProductos.getVisibility() == View.GONE && !configuracion.compacto)
                    layFrgProductos.setVisibility(VISIBLE);
                if (fragmentProductos != null) fragmentProductos.posicionaProducto();

                if (layFrgVentas != null && layFrgVentas.getVisibility() == View.GONE) {
                    layFrgVentas.setVisibility(VISIBLE);
                    if (fragmentDetalleVenta != null)
                        fragmentDetalleVenta.estadoFragmentActivo(true);
                }
            }


    }
    //endregion

    //region Interface fragmentProductos
    @Override
    public Bundle cargaProductos() {
        if (tpv != null && cabecera != null) {
            cargaProductosCabeceraBD(tpv.tmenu, cabecera.codigo);
        }
        Bundle bundle = new Bundle();
        bundle.putInt("productoSel", productoSel);
        bundle.putInt("milisg", configuracion.timerFragment * 1000);
        bundle.putParcelableArrayList("productos", listaProductos);

        ArrayList<Integer> filtroAlergenos = new ArrayList<>();
        if (listaAlergenos != null) {
            for (Alergenos alergenos : listaAlergenos) {
                if (alergenos.isChecked())
                    filtroAlergenos.add(alergenos.getCodigo());
            }
        }
        bundle.putIntegerArrayList("alergenos", filtroAlergenos);

        ArrayList<Integer> filtroEtiquetas = new ArrayList<>();
        if (listaEtiquetas != null) {
            for (Etiquetas etiqueta : listaEtiquetas) {
                if (etiqueta.isChecked())
                    filtroEtiquetas.add(etiqueta.getCodigo());
            }
        }
        bundle.putIntegerArrayList("etiquetas", filtroEtiquetas);

        ArrayList<Integer> filtroOrden = new ArrayList<>();
        if (listaOrdenPlatos != null) {
            for (OrdenPlatos ordenPlato : listaOrdenPlatos) {
                if (ordenPlato.isChecked())
                    filtroOrden.add(ordenPlato.getCodigo());
            }
        }
        bundle.putIntegerArrayList("orden_platos", filtroOrden);


        return bundle;
    }

    @Override
    public void onClickProducto(String i) {

        if (submesa.estado == ClaseSubMesas.enEstados.tiquet) {
            Toast.makeText(this, "Submesa cerrada. No se puede modificar", Toast.LENGTH_LONG).show();
            return;
        }

        productoSel = -1;
        for (ClaseProductos producto : listaProductos) {
            if (producto.codmenu == i) {
                productoSel = listaProductos.indexOf(producto);
                break;
            }
        }

        if (productoSel != -1) {
            producto = listaProductos.get(productoSel);
            añadeProducto(producto, fragmentLineaVentas.isHH);
            if (fragmentLineaVentas != null) fragmentLineaVentas.actualizaLineaVentas(true);

            // condicionar a sw_480
            //String tamaño = context.getResources().getString(R.string.sw);
            //if (tamaño.equalsIgnoreCase("sw_480")){

            // introducimos un retardo despues de añadir, para evitar que si selecciono muy rápido se solapen varios eventos y haga un crash
            // is tengo un retardo anterior, lo cancelo, por lo que siempre se ejecutará el del último producto
            if (hActualizaDetalle != null) {
                hActualizaDetalle.removeCallbacksAndMessages(null);
                hActualizaDetalle = null;
            }

            hActualizaDetalle = new Handler();
            hActualizaDetalle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (fragmentDetalleVenta != null) fragmentDetalleVenta.actualizaDetalleVenta();
                    //hActualizaDetalle = null;
                }
            }, 300);
            //}


            if (fragmentSubmesas != null) fragmentSubmesas.actualizaSubmesas();
            if (fragmentProductos != null) {
                // fragmentProductos.productoSel = productoSel;
            }
            if (configuracion.compacto && !configuracion.mantenerProductos) {
                if (layCompacto != null) layCompacto.setVisibility(VISIBLE);
                if (layFrgProductos != null) layFrgProductos.setVisibility(View.GONE);
                if (layFrgCabeceras != null) layFrgCabeceras.setVisibility(View.GONE);
                if (layFrgLineaVentas != null) layFrgLineaVentas.setVisibility(VISIBLE);
                if (layFrgDetalleVenta != null) layFrgDetalleVenta.setVisibility(VISIBLE);

            }


        }

    }

    @Override
    public void onLongCLickProductos(String i) {
        for (ClaseProductos tmpProducto : listaProductos) {
            if (tmpProducto.codmenu == i) {
                ClaseProductos productoInfo = tmpProducto;
                ClaseCabeceras cabeceraInfo = listaCabeceras.get(0);

                for (ClaseCabeceras tmpCabecera : listaCabeceras) {
                    if (tmpCabecera.codigo == tmpProducto.cabecera) {
                        cabeceraInfo = tmpCabecera;
                    }
                }

                ArrayList<ClaseItemFiltro> tmpAlergenos = new ArrayList<>();
                for (Alergenos tmpAlergeno : listaAlergenos) {
                    if (productoInfo.alergenos.contains("|" + tmpAlergeno.getCodigo() + "|")) {
                        tmpAlergenos.add(new ClaseItemFiltro(tmpAlergeno.getCodigo(), tmpAlergeno.getDescripcion(), true));
                    }
                }

                ArrayList<ClaseItemFiltro> tmpEtiquetas = new ArrayList<>();
                for (Etiquetas tmpEtiqueta : listaEtiquetas) {
                    if (productoInfo.etiquetas.contains("|" + tmpEtiqueta.getCodigo() + "|")) {
                        tmpEtiquetas.add(new ClaseItemFiltro(tmpEtiqueta.getCodigo(), tmpEtiqueta.getDescripcion(), true));
                    }
                }


                ClaseUtils.VerInfoProducto aviso = new ClaseUtils.VerInfoProducto();
                aviso.setTitulo(context.getResources().getString(R.string.dialog_strInfoProducto));
                aviso.setOnclick(dlgOnClickNada);
                aviso.setContext(context);
                aviso.setProducto(productoInfo);
                aviso.setCabecera(cabeceraInfo);
                aviso.setListaAlergenos(tmpAlergenos);
                aviso.setListaEtiquetas(tmpEtiquetas);
                aviso.execute(0);

                break;
            }
        }

    }

    @Override
    public void fp_ocultaElementos(boolean ocultar) {
        if (estado == enEstado.venta) {


            if (!ocultar) {
                if (layCompacto != null && layCompacto.getVisibility() == VISIBLE)
                    layCompacto.setVisibility(View.GONE);

                if (layFrgCabeceras != null && layFrgCabeceras.getVisibility() == VISIBLE)
                    layFrgCabeceras.setVisibility(View.GONE);

                if (fragmentCabeceras != null) fragmentCabeceras.posicionaCabecera();


                //if (layFrgProductos != null && layFrgProductos.getVisibility() == View.GONE && configuracion.compacto)
                if (layFrgProductos != null && layFrgProductos.getVisibility() == View.GONE)
                    layFrgProductos.setVisibility(VISIBLE);
                if (fragmentProductos != null) fragmentProductos.posicionaProducto();


                if (layFrgVentas != null && layFrgVentas.getVisibility() == VISIBLE) {
                    layFrgVentas.setVisibility(View.GONE);
                    if (fragmentDetalleVenta != null)
                        fragmentDetalleVenta.estadoFragmentActivo(false);
                }

            } else {


                if (layCompacto != null && layCompacto.getVisibility() == View.GONE && configuracion.compacto) {
                    layCompacto.setVisibility(VISIBLE);
                    layFrgCabeceras.setVisibility(View.GONE);
                    layFrgProductos.setVisibility(View.GONE);
                }

                if (layFrgCabeceras != null && layFrgCabeceras.getVisibility() == View.GONE && !configuracion.compacto)
                    layFrgCabeceras.setVisibility(VISIBLE);

                if (fragmentCabeceras != null) fragmentCabeceras.posicionaCabecera();

                if (layFrgProductos != null && layFrgProductos.getVisibility() == VISIBLE && !configuracion.compacto)
                    layFrgProductos.setVisibility(VISIBLE);
                if (fragmentProductos != null) fragmentProductos.posicionaProducto();

                if (layFrgVentas != null && layFrgVentas.getVisibility() == View.GONE) {
                    layFrgVentas.setVisibility(VISIBLE);
                    if (fragmentDetalleVenta != null)
                        fragmentDetalleVenta.estadoFragmentActivo(false);
                }
            }
        }
    }

    @Override
    public int getComandaNLineas() {
        int uds = 0;
        for (ClaseLineaVentas linea : listaLineaVentas) {
            uds += linea.cantidad;
        }
        return uds;
    }

    @Override
    public void onClickFiltrar() {

        ClaseUtils.FiltarPlatos aviso = ClaseUtils.FiltarPlatos.newInstance(context);
        FragmentManager fm = getSupportFragmentManager();


        ArrayList<ClaseItemFiltro> filtros = new ArrayList<>();
        for (Alergenos alergeno : listaAlergenos) {
            filtros.add(new ClaseItemFiltro(alergeno.getCodigo(), alergeno.getDescripcion(), alergeno.isChecked()));
        }
        Collections.sort(filtros);

        ArrayList<ClaseItemFiltro> filtros1 = new ArrayList<>();
        for (Etiquetas etiqueta : listaEtiquetas) {
            filtros1.add(new ClaseItemFiltro(etiqueta.getCodigo(), etiqueta.getDescripcion(), etiqueta.isChecked()));
        }
        Collections.sort(filtros1);

        ArrayList<ClaseItemFiltro> filtros2 = new ArrayList<>();
        for (OrdenPlatos ordenPlato : listaOrdenPlatos) {
            filtros2.add(new ClaseItemFiltro(ordenPlato.getCodigo(), ordenPlato.getDescripcion(), ordenPlato.isChecked()));
        }


        aviso.setAlergenos(filtros);
        aviso.setEtiquetas(filtros1);
        aviso.setOrdenPlatos(filtros2);

        aviso.show(fm, "dialog");


    }

    //endregion

    //region Interface fragmentLineaVentas
    @Override
    public Bundle cargaLineaVentas(boolean refresca) {
        if (!refresca) {

            //listaLineaVentas = ClaseLineaVentas.recuperaLineaVentas(tpvSel, mesaSel,subMesaSel);
            if (submesa != null & submesa.lineasVentas != null)
                listaLineaVentas = submesa.lineasVentas;
            else
                listaLineaVentas = new ArrayList<>();


            if (listaLineaVentas != null && listaLineaVentas.size() > 0) {
                lineaVenta = listaLineaVentas.get(0);
                lineaSel = 0;
            } else {
                lineaVenta = null;
                lineaSel = -1;
            }
        }

        double importe = 0.00;
        double importeExtras = 0.00;
        double coste = 0.00;
        if (listaLineaVentas != null) {
            for (ClaseLineaVentas linea : listaLineaVentas) {
                importe += linea.teuros;
                coste += linea.tcoste;


                for (ClaseItemExtra extra: linea.extras){
                    if (extra.tipo.equalsIgnoreCase("E") && extra.estadoExtra == ClaseItemExtra.ESTADO_CON){
                        double precio = Double.parseDouble(extra.precio);
                        importeExtras += linea.cantidad*precio;
                    }
                }
            }
        }
        if (submesa != null) {
            submesa.lineasVentas = listaLineaVentas;
            if (submesa.importe != importe)
                submesa.importe = importe;
            if (submesa.coste != coste)
                submesa.coste = coste;
        }

        recalculaPension = false;

        Bundle bundle = new Bundle();
        bundle.putInt("lineaSel", lineaSel);
        bundle.putDouble("importe", importe);
        bundle.putDouble("importeExtras", importeExtras);
        bundle.putDouble("coste", coste);
        bundle.putParcelableArrayList("lineaVentas", listaLineaVentas);
        bundle.putString("ordenplatos", ClaseCondicionesVenta.orden_platos);
        return bundle;
    }

    @Override
    public void onClickLineaVenta(int i) {
        submesaSelPendiente = -1;
        lineaSelPendiente = i;
        estadoPendiente = enEstado.venta;

        if (!compruebaCambiosLineaPendientes()) {
            lineaSel = i;
            cambiaLineaVenta();
        }

    }

    @Override
    public void onClickEditaVenta(int i) {
        submesaSelPendiente = -1;
        lineaSelPendiente = i;
        estadoPendiente = enEstado.venta;

        if (!compruebaCambiosLineaPendientes()) {
            lineaSel = i;
            cambiaLineaVenta();
        }
        cambiaLineaVenta();
        cambiaVistaDetalle(true);
    }

    @Override
    public void setHappyHour(boolean hh) {
        isHappyHour = hh;
    }

    @Override
    public void onClickEnviarComanda() {
        ArrayList<GrabaLineasVenta> lineasVentas = new ArrayList<>();
        ArrayList<ActualizaPax> submesas = new ArrayList<>();
        for (ClaseSubMesas submesa : mesa.submesas) {
            submesas.add(new ActualizaPax(submesa.submesa, submesa.codenl, submesa.pax, submesa.habitacion));

            int nlinea = 0;
            for (ClaseLineaVentas linea : submesa.lineasVentas) {

                if (linea.estado.equals(ClaseUtils.enEstado.actualizar) ||
                        linea.estado.equals(ClaseUtils.enEstado.anadir) ||
                        linea.estado.equals(ClaseUtils.enEstado.eliminar)) {

                    GrabaLineasVenta tlinea = LineaVentaMapper.toGraba(linea);

                    lineasVentas.add(tlinea);

                }
                nlinea += 1;

            }
        }

        /*
        //imprimir comanda
        if (configuracion.comandaapp) {
            imprimirComanda(lineasVentas);
        }

         */

        argEnviaLineasMesa = ArgEnviaLineasMesa.getINSTANCE();
        argEnviaLineasMesa.setLineasVentas(lineasVentas);
        argEnviaLineasMesa.setSubmesas(submesas);

        ultimaAccionWs = enAccionesWS.enviaLineasMesa;
        reintentosWS = 0;
        conectaWS(ultimaAccionWs, argEnviaLineasMesa);
    }

    @Override
    public void onClickSincronizaComanda() {
        ultimoEstado = enEstado.venta;
        estado = enEstado.cargalineas;
        cambiaEstado(estado);
    }

    @Override
    public void onClickOrden(int orden) {
        ordenSel = orden;
        if (fragmentLineaVentas != null)
            fragmentLineaVentas.setOrdenSel(orden);
    }

    @Override
    public void onClickSiguePlato() {
        // VER QUÉ PLATOS TENEMOS EN LA LISTA
        if (listaLineaVentas.size() < 1) {
            return;
        }

        ArrayList<Integer> tipos = new ArrayList<Integer>();
        for (ClaseLineaVentas linea : listaLineaVentas) {
            if (!tipos.contains(linea.orden_platos)) {
                tipos.add(linea.orden_platos);
            }
        }
        if (tipos.size() > 0) {
            ClaseUtils.SiguePlatos aviso = new ClaseUtils.SiguePlatos();
            aviso.setTipos(tipos);
            aviso.setTitulo(context.getResources().getString(R.string.dialog_strSiguePlatos));
            aviso.setBotonFalse(context.getResources().getString(R.string.strCancelar));
            aviso.setOnclick(dlgOnClickSiguePlato);

            aviso.setContext(context);
            aviso.execute(0);
        }
    }

    @Override
    public void onClickVerComanda() {
        if (listaLineaVentas.size() < 1) {
            return;
        }

        ClaseUtils.ResumenComanda aviso = new ClaseUtils.ResumenComanda();
        aviso.setTitulo(context.getResources().getString(R.string.dialog_strResumenComanda));
        aviso.setListaLineaVentas(listaLineaVentas);
        aviso.setOnclick(dlgOnClickNada);

        aviso.setContext(context);
        aviso.execute(0);

    }
    //endregion

    //region interface fragmentDetalleVentas
    @Override
    public Bundle cargaDetalleVenta() {
        if (!configuracion.mantenerProductos || (fragmentProductos != null && !fragmentProductos.isFragmentAmpliado())) {
            cambiaVistaDetalle(false);
        }

        if (listaLineaVentas != null && lineaSel > -1 && listaLineaVentas.size() > lineaSel) {
            ClaseLineaVentas item = (ClaseLineaVentas) listaLineaVentas.get(lineaSel).clone();
            Bundle bundle = new Bundle();
            bundle.putString("ordenplatos", ClaseCondicionesVenta.orden_platos);
            bundle.putParcelable("item", item);
            if (submesa.estado == ClaseSubMesas.enEstados.tiquet)
                bundle.putBoolean("editar", false);
            else
                bundle.putBoolean("editar", true);

            double precioOriginal = item.peuros;
            for (ClaseProductos tmpProducto : listaProductos) {
                if (tmpProducto.tmenu == item.tmenu && tmpProducto.codmenu.equals(item.codmenu)) {
                    precioOriginal = Double.parseDouble(tmpProducto.euros.replace(",", "."));
                    break;
                }
            }

            bundle.putDouble("precio_original", precioOriginal);

            return bundle;
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("ordenplatos", ClaseCondicionesVenta.orden_platos);
            bundle.putParcelable("item", null);
            bundle.putBoolean("editar", false);
            return bundle;
        }
    }

    @Override
    public void eliminaLinea(boolean borrar, ClaseLineaVentas  item) {

        int nMesa = item.mesa;
        int nSubMesa = item.submesa;

        ClaseMesas mesaAnt = listaMesas.get(nMesa);
        if (mesaAnt == null ) return;

        ClaseSubMesas subMesaAnt = null;
        for (ClaseSubMesas tSubMesa: mesaAnt.submesas) {
            if (tSubMesa.submesa ==nSubMesa) {
                subMesaAnt = tSubMesa;
                break;
            }
        }
        if (subMesaAnt == null) return;

        ArrayList<ClaseLineaVentas> lineas = subMesaAnt.lineasVentas;

        AtomicInteger index = new AtomicInteger(-1);
        int tLineaSel = lineas.stream()
                .filter( l -> {
                    index.getAndIncrement();
                    return l.codigo == item.codigo;
                })
                .map(user -> index.get())
                .findFirst().orElse(-1);

        if (tLineaSel == -1) return;
        lineas.remove(tLineaSel);

        if (!borrar) {
            if (item.estado == ClaseUtils.enEstado.transmitida ||
                    item.estado == ClaseUtils.enEstado.actualizar ||
                    item.estado == ClaseUtils.enEstado.eliminar) {
                item.estado = ClaseUtils.enEstado.eliminar;
                item.imprimir = "S";
                lineas.add(tLineaSel, item);
            }
        }

        if (listaLineaVentas.size() > 0) {
            if (lineaSel >= listaLineaVentas.size()) {
                lineaSel = listaLineaVentas.size() - 1;
                lineaVenta = listaLineaVentas.get(lineaSel);
            }
        } else {
            lineaSel = -1;
            lineaVenta = null;
        }

        if (fragmentLineaVentas != null) fragmentLineaVentas.actualizaLineaVentas(true);
        if (fragmentDetalleVenta != null) fragmentDetalleVenta.actualizaDetalleVenta();
        if (fragmentSubmesas != null) fragmentSubmesas.actualizaSubmesas();

    }

    @Override
    public void actualizaLinea(ClaseLineaVentas detalleVenta) {
        ClaseLineaVentas item = listaLineaVentas.get(lineaSel);

        item.cantidad = detalleVenta.cantidad;
        item.peuros = detalleVenta.peuros;
        item.teuros = detalleVenta.teuros;
        item.pcoste = detalleVenta.pcoste;
        item.tcoste = detalleVenta.tcoste;

        item.imprimir = detalleVenta.imprimir;
        item.nota = "";
        item.extras = LineaVentaMapper.copyExtras(detalleVenta.extras);

        item.descuento = detalleVenta.descuento;

        item.orden_platos = detalleVenta.orden_platos;
        if (item.estado == ClaseUtils.enEstado.transmitida)
            item.estado = ClaseUtils.enEstado.actualizar;


        if (fragmentLineaVentas != null) fragmentLineaVentas.actualizaLineaVentas(true);
        if (fragmentDetalleVenta != null) fragmentDetalleVenta.actualizaDetalleVenta();
        if (fragmentSubmesas != null) fragmentSubmesas.actualizaSubmesas();

        cambiaVistaDetalle(false);
    }

    @Override
    public boolean isHappyHour() {
        return isHappyHour;
    }

    @Override
    public void ocultaElementos(boolean visible) {
        if (estado == enEstado.venta)
            if (!visible) {
                if (layCompacto != null && layCompacto.getVisibility() == VISIBLE)
                    layCompacto.setVisibility(View.GONE);

                if (layFrgCabeceras != null && layFrgCabeceras.getVisibility() == VISIBLE)
                    layFrgCabeceras.setVisibility(View.GONE);

                if (layFrgProductos != null && layFrgProductos.getVisibility() == VISIBLE)
                    layFrgProductos.setVisibility(View.GONE);
            } else {
                if (layCompacto != null && layCompacto.getVisibility() == View.GONE && configuracion.compacto)
                    layCompacto.setVisibility(VISIBLE);

                if (layFrgCabeceras != null && layFrgCabeceras.getVisibility() == View.GONE && !configuracion.compacto)
                    layFrgCabeceras.setVisibility(VISIBLE);

                if (layFrgProductos != null && layFrgProductos.getVisibility() == View.GONE && !configuracion.compacto)
                    layFrgProductos.setVisibility(VISIBLE);


            }

    }

    @Override
    public ArrayList<Integer> recuperaListaSubmesas() {
        ArrayList<Integer> listaSubmesas = new ArrayList<>();
        if (mesa != null && mesa.submesas != null) {
            for (ClaseSubMesas tsubmesa : mesa.submesas) {
                if (tsubmesa.submesa != submesa.submesa) {// no añado la submesa activa
                    if (tsubmesa.estado == ClaseSubMesas.enEstados.abierta)
                        listaSubmesas.add(tsubmesa.submesa);
                }
            }
        }
        return listaSubmesas;
    }

    @Override
    public boolean añadeSubmesa() {
        if (estado == enEstado.venta) {
            if (configuracion.pax == true) {


                ClaseUtils.PidePaxSubmesa aviso = new ClaseUtils.PidePaxSubmesa();
                aviso.setTitulo(context.getResources().getString(R.string.alert_strAviso));
                aviso.setMensaje(context.getResources().getString(R.string.dialog_strTituloPidePax));
                aviso.setBotonTrue(context.getResources().getString(R.string.strAceptar));
                aviso.setBotonFalse(context.getResources().getString(R.string.strCancelar));
                aviso.setOnclick(dlgOnClickPidePax);
                aviso.setTipo(ClaseUtils.PidePaxSubmesa.TIPO_DIALOGO_MOVER_PAX);
                aviso.setRoom(mesa.submesas.get(subMesaSel).habitacion);
                aviso.setContext(context);
                aviso.setSubmesas(listaMesas.get(mesaSel).submesas);
                aviso.setMover_uds(true);
                aviso.execute(0);
                return false;
            } else {
                mesa.nSubmesas += 1;

                String horaApertura = ClaseUtils.now("HH:mm:ss");


                mesa.submesas.add(new ClaseSubMesas(mesa.nSubmesas, ClaseSubMesas.enEstados.abierta, false, 0, 0,submesa.habitacion, submesa.pension,submesa.descPension,submesa.tipoPension,submesa.descTipoPension, false, horaApertura,"",""));
                if (fragmentSubmesas != null) fragmentSubmesas.actualizaSubmesas();
                return true;
            }
        }
        return false;
    }

    @Override
    public void mueveLineaSubmesa(ClaseLineaVentas linea, int nueva_submesa) {
        for (ClaseSubMesas submesa : mesa.submesas) {
            if (submesa.submesa == nueva_submesa) {
                if (linea.estado == ClaseUtils.enEstado.transmitida) {
                    linea.estado = ClaseUtils.enEstado.actualizar;
                }

                //VALORAR PENSION

                if (!linea.pension.equalsIgnoreCase("") && !submesa.aplicaPension) {
                    linea.pension = "";
                    linea.teuros = linea.peuros * linea.cantidad;
                }
                else if (linea.pension.equalsIgnoreCase("") && submesa.aplicaPension){
                    //ver si el producto tiene pension. por defecto se paga
                    linea.pension = "";
                    linea.teuros = linea.peuros * linea.cantidad;

                    try {
                        if (linea.tmenu < 1 ) linea.tmenu = tpv.tmenu;
                        Productos producto = baseDatos.getProducto(linea.codmenu,linea.tmenu);
                        if (producto != null && producto.getPensiones().contains("|"+submesa.pension+"|")) {
                            linea.pension = submesa.pension;
                            linea.teuros = 0.00;
                        }
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                submesa.lineasVentas.add(linea);
            break;
            }
        }
    }

    @Override
    public boolean lineasPendientes() {
        return getLineasPendientesEnvio();
    }

    @Override
    public ArrayList<ClaseItemExtra> recuperaSubfamiliaExtras(ClaseLineaVentas linea) {
        Productos producto= null;
        ArrayList<ClaseItemExtra> extras = new ArrayList<>();

        try {
            producto = baseDatos.getProducto(linea.codmenu,linea.tmenu);
            if (producto != null && producto.getCodsub() != null && ! producto.getCodsub().isEmpty()){
                String tExtras = baseDatos.appGetRecuperaSubFamiliaExtras(Integer.parseInt(producto.getCodsub()));
                if (tExtras != null && ! tExtras.isEmpty() ) {
                    while (tExtras.startsWith("|")) {
                        tExtras = tExtras.substring(1);
                    }
                    while (tExtras.endsWith("|")) {
                        tExtras = tExtras.substring(0, tExtras.length() - 1);
                    }
                    ArrayList<String> codmenus = new ArrayList<>(Arrays.asList(tExtras.split("\\|")));

                    List<Productos> productos = baseDatos.appGetRecuperaProductosExtras(codmenus,linea.tmenu);
                    extras =  ClaseItemExtra.fromProductos(productos);
                }
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return extras;
    }


    //endregion

    //region interface fragmentFormaPago




    @Override
    public Bundle cargaClientesCtaCasa() {
        cargaClientesCtaCasaBD();
        Bundle bundle = new Bundle();
        bundle.putInt("clienteCtaCasaSel", clienteCtaCasaSel);
        bundle.putParcelableArrayList("clientesCtaCasa", listaClientesCtaCasa);
        return bundle;
    }

    public Bundle cargaEstablecimientos() {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("establecimientos", ClaseCondicionesVenta.establecimientos);
        return bundle;
    }

    @Override
    public Bundle cargaFormasPago() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("pagoEfectivo", configuracion.pagoEfectivo);
        bundle.putBoolean("pagoTarjeta", configuracion.pagoTarjeta);
        bundle.putBoolean("pagoEfeTar", configuracion.pagoEfeTar);
        bundle.putBoolean("pagoCuentacasa", configuracion.pagoCuentacasa);
        bundle.putBoolean("pagoCredito", configuracion.pagoCredito);
        return bundle;
    }

    @Override
    public double cargaImporte() {
        return mesa.submesas.get(subMesaSel).importe;
    }

    @Override
    public double cargaCoste() {
        if (ClaseCondicionesVenta.tipo_cuentacasa.toUpperCase().equals("G"))
            return 0.00;
        else
            return mesa.submesas.get(subMesaSel).coste;
    }

    @Override
    public String cargaRoom() {
        String room =  submesa.habitacion;
        if (room == null) {
            room = "";
        }
        return room;
    }

    @Override
    public String cargaPension() {
        String pension =  submesa.pension;
        if (pension == null) {
            pension = "";
        }
        return pension;
    }


    @Override
    public void onClickClientesCtaCasa(int i) {
        //clienteCtaCasaSel =i;
        //clienteCtaCasa = listaClientesCtaCasa.get(i);
        //estado = enEstado.cargamesas;
        //cambiaEstado(estado);
    }

    @Override
    public void onClickAccionPagar(boolean pagar, Bundle datos) {
        if (pagar) {
            // realizar el pago
            //    clienteCtaCasa = listaClientesCtaCasa.get(fragmentFormaPago.getClienteCtaCasaSel());

            String codemp_ext = datos.getString("codemp_ext");
            String nreserva = datos.getString("nreserva");
            String formapago = datos.getString("formapago");
            String importe = datos.getString("importe");
            String igic = datos.getString("igic");

            String efectivo = datos.getString("efectivo");
            String entregado = datos.getString("entregado");
            String cambio = datos.getString("cambio");
            String tarjeta = datos.getString("tarjeta");
            String cuentacasa = datos.getString("cuentacasa");
            String credito = datos.getString("credito");

            String codclicasa = datos.getString("codclicasa");

            String nfactura = mesa.submesas.get(subMesaSel).nfactura;

            String codusu = String.valueOf(listaUsers.get(userSel).codigo);

            String firma = datos.getString("firma");
            String apto = datos.getString("apto");

            if (apto.equals("")){
                if (!listaMesas.get(mesaSel).submesas.get(subMesaSel).habitacion.equals("")) {
                    apto = listaMesas.get(mesaSel).submesas.get(subMesaSel).habitacion;
                }
            }

            argPagaMesa = ArgPagaMesa.getINSTANCE();
            argPagaMesa.setCodemp_ext(codemp_ext);
            argPagaMesa.setNreserva(nreserva);
            argPagaMesa.setFormapago(formapago);
            argPagaMesa.setImporte(importe);
            argPagaMesa.setIgic(igic);
            argPagaMesa.setEfectivo(efectivo);
            argPagaMesa.setEntregado(entregado);
            argPagaMesa.setCambio(cambio);
            argPagaMesa.setTarjeta(tarjeta);
            argPagaMesa.setCuentacasa(cuentacasa);
            argPagaMesa.setCredito(credito);
            argPagaMesa.setCodclicasa(codclicasa);
            argPagaMesa.setNfactura(nfactura);
            argPagaMesa.setCodusu(codusu);
            argPagaMesa.setFirma(firma);
            argPagaMesa.setApto(apto);
            String imprimir = "N";
            if (configuracion.cuentatpv){
                imprimir = "S";
            }
            argPagaMesa.setImprimir(imprimir);

            Log.d("pagar mesa", "pago mesa, cargo argumentos. usuario " + codusu);

            ultimaAccionWs = enAccionesWS.pagaMesa;
            reintentosWS = 0;
            conectaWS(ultimaAccionWs, argPagaMesa);

            // pruebas. el cambio de mesa lo hace el resultado de la llamada a la API para grabar el pago
            //estado = enEstado.cargamesas;
            //cambiaEstado(estado);

        } else {
            enEstado tmp = ultimoEstado;
            ultimoEstado = estado;
            estado = tmp;
            cambiaEstado(estado);
        }
    }

    @Override
    public void recuperaCreditoTagID(final String tagID, String codemp) {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                argCreditoTagID = ArgCreditoTagID.getINSTANCE();
                argCreditoTagID.setTagID(tagID);
                argCreditoTagID.setCodemp(codemp);
                ultimaAccionWs = enAccionesWS.creditoTagID;
                reintentosWS = 0;
                conectaWS(ultimaAccionWs, argCreditoTagID);
            }
        }, 1000);

    }

    @Override
    public void recuperaCreditoRoom(final String room, String codemp) {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                argCreditoRoom = ArgCreditoRoom.getINSTANCE();
                argCreditoRoom.setRoom(room);
                argCreditoRoom.setCodemp(codemp);
                ultimaAccionWs = enAccionesWS.creditoRoom;
                reintentosWS = 0;
                conectaWS(ultimaAccionWs, argCreditoRoom);
            }
        }, 1000);

    }

    //endregion


//region Interface CLaseutils.FiltrarPlatosNew

    @Override
    public void onCLickVolverFiltraPlatos() {
        ArrayList<ClaseItemFiltro> fAlergenos = ClaseUtils.FiltarPlatos.getAlergenos();
        for (ClaseItemFiltro item : fAlergenos) {
            for (Alergenos alergeno : listaAlergenos) {
                if (alergeno.getCodigo() == item.codigo) {
                    alergeno.setChecked(item.checked);
                    continue;
                }
            }
        }

        ArrayList<ClaseItemFiltro> fEtiquetas = ClaseUtils.FiltarPlatos.getEtiquetas();
        for (ClaseItemFiltro item : fEtiquetas) {
            for (Etiquetas etiqueta : listaEtiquetas) {
                if (etiqueta.getCodigo() == item.codigo) {
                    etiqueta.setChecked(item.checked);
                    continue;
                }
            }
        }

        ArrayList<ClaseItemFiltro> fOrdenPlatos = ClaseUtils.FiltarPlatos.getOrdenPlatos();
        for (ClaseItemFiltro item : fOrdenPlatos) {
            for (OrdenPlatos ordenPlato : listaOrdenPlatos) {
                if (ordenPlato.getCodigo() == item.codigo) {
                    ordenPlato.setChecked(item.checked);
                    continue;
                }
            }
        }

        if (fragmentProductos != null) {
            fragmentProductos.actualizaProductos(!configuracion.mostrarProductosAuto);
        }
    }

    //endregion

    //region CONEXION SERVICIO WEB


    private static void actualizaLatenciaTest(){
        String err = "";
        if (! latenciaTest.isCorrecto()) {
            err = " (*)";
        }

        tvLatencia.setText("Conexión: "+String.valueOf(latenciaTest.getUltima())+"/" +String.valueOf(latenciaTest.getMedia()) + "/"+String.valueOf(latenciaTest.getMaxima())+" ms"+err);

        tvLatencia.setTextColor(Color.GREEN);
        if (latenciaTest.getMedia() > 500 ) {
            tvLatencia.setTextColor(Color.YELLOW);
        }
        if (latenciaTest.getMedia() > 1500){
            tvLatencia.setTextColor(Color.RED);
        }
    }

    public static <T> void conectaWS(enAccionesWS accion, final T args) {

        hSyncDatos.removeCallbacksAndMessages(null);
        hdDelay = new Handler();
        hdDelay.postDelayed(new Runnable() {
            @Override
            public void run() {
                testWS(ultimaAccionWs, args);
            }
        }, 200);
    }

    private static <T> void testWS(final enAccionesWS accion, final T args) {
        try {

            //compruebo condiciones de conexión a internet
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(context.CONNECTIVITY_SERVICE);
            boolean conexionOK = true;
            try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                    conexionOK = capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
                } else {
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    if (networkInfo == null || !networkInfo.isConnected()) {
                        conexionOK = false;

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!conexionOK) {
                if (toolBarMenu != null) {
                    MenuItem item = toolBarMenu.findItem(R.id.action_conexion);
                    if (item != null) item.setIcon(R.drawable.bar_wifi_gris);
                }

                avisoFalloConexion();
                return;

            }

            final Long timeini = Calendar.getInstance().getTimeInMillis();
            APIService apiService = ClaseServicioWeb.getAPIService(false, configuracion.connectionTO, configuracion.rwTO1, configuracion.rwTO1, latenciaTest.getUltima());
            apiService.recuperaTestJSON().enqueue(new Callback<RespuestaTestjsonWS>() {
                @Override
                public void onResponse(Call<RespuestaTestjsonWS> call, Response<RespuestaTestjsonWS> response) {

                    latenciaTest.add( response.raw().sentRequestAtMillis(),response.raw().receivedResponseAtMillis(),response.raw().isSuccessful() );
                    actualizaLatenciaTest();

                    if (toolBarMenu != null) {
                        MenuItem item = toolBarMenu.findItem(R.id.action_conexion);
                        if (item != null) {
                            if (response == null || response.body() == null) {
                                item.setIcon(R.drawable.bar_wifi_rojo);
                            } else if (response.body().getEstado().equalsIgnoreCase("OK")) {
                                item.setIcon(R.drawable.bar_wifi_verde);
                            } else {
                                item.setIcon(R.drawable.bar_wifi_rojo);
                            }
                        }
                    }

                    reintentosWS = 0;

                    String mensajeError = context.getResources().getString(R.string.strErrorWsErrorAction);
                    boolean errorPeticion = false;


                    switch (accion) {
                        case test:
                            hSyncDatos.postDelayed(checkConexion, 2000);
                            break;

                        case validar:
                            validarWS(false, false);
                            break;

                        case sincronizar:
                            actualizaTablas();
                            break;

                        case sincronizarbg:
                            actualizaTablasBG();
                            break;

                        case getEstadoMesas:
                            if (! ClaseUtils.VerTiquet.isVisible()) {
                                ClaseUtils.ProgressDialogo.mostrarDialogo(true, context.getResources().getString(R.string.progress_strRecuperaMesas), context.getResources().getString(R.string.progress_strEspera), context);
                            }
                            try {
                                ServSincronizaBD.startActionGetEstadoMesas(context, configuracion.codigo, configuracion.deviceID, tpv.codtpv, handlerNotificaWS, (Activity) context);
                            } catch (Exception ex) {
                                errorPeticion = true;
                            }
                            break;

                        case getLineasMesa:
                            if (! ClaseUtils.VerTiquet.isVisible()) {
                                ClaseUtils.ProgressDialogo.mostrarDialogo(true, context.getResources().getString(R.string.progress_strRecuperaLineas), context.getResources().getString(R.string.progress_strEspera), context);
                            }
                            try {
                                String room = "";
                                try {
                                    if (mesa.submesas != null && mesa.submesas.size() > 0  && mesa.submesas.get(0).habitacion.length() > 0){
                                        room = mesa.submesas.get(0).habitacion;
                                    }
                                }
                                catch (Exception ex1) {
                                    room = "";
                                }

                                    ServSincronizaBD.startActionGetLineasMesa(context, configuracion.codigo, configuracion.deviceID, tpv.codtpv, String.valueOf(mesa.mesa), room, handlerNotificaWS, (Activity) context);
                            } catch (Exception ex) {
                                errorPeticion = true;
                            }
                            break;

                        case enviaLineasMesa:
                            ClaseUtils.ProgressDialogo.mostrarDialogo(true, context.getResources().getString(R.string.progress_strEnviarLineas), context.getResources().getString(R.string.progress_strEspera), context);
                            try {
                                String imprimir = "N";
                                if (configuracion.comandatpv) {
                                    imprimir = "S";
                                }




                                ServSincronizaBD.startActionEnviaLineasMesa(context, configuracion.codigo, configuracion.deviceID, tpv.codtpv, String.valueOf(mesa.mesa), ((ArgEnviaLineasMesa) args).submesas, ((ArgEnviaLineasMesa) args).lineasVentas, imprimir,user.nombre.toUpperCase(), handlerNotificaWS, (Activity) context);
                            } catch (Exception ex) {
                                errorPeticion = true;
                            }
                            break;

                        case actualizaPax:
                            ClaseUtils.ProgressDialogo.mostrarDialogo(true, context.getResources().getString(R.string.progress_strActualizaPax), context.getResources().getString(R.string.progress_strEspera), context);
                            try {
                                ServSincronizaBD.startActionActualizaPax(context, configuracion.codigo, configuracion.deviceID, ((ArgActualizaPax) args).submesasPax, handlerNotificaWS, (Activity) context);
                            } catch (Exception ex) {
                                errorPeticion = true;
                            }
                            break;

                        case siguePlato:
                            ClaseUtils.ProgressDialogo.mostrarDialogo(true, ((ArgSiguePlato) args).aviso, context.getResources().getString(R.string.progress_strEspera), context);
                            try {
                                ServSincronizaBD.startActionSiguePlato(context, configuracion.codigo, configuracion.deviceID, tpv.codtpv, String.valueOf(mesa.mesa) + "-" + mesa.descripcion, ClaseUtils.SiguePlatos.getOrden(), mesa.pax, handlerNotificaWS, (Activity) context);
                            } catch (Exception ex) {
                                errorPeticion = true;
                            }
                            break;

                        case traspasaMesa:
                            try {
                                ServSincronizaBD.startActionTraspasaMesa(context, configuracion.codigo, configuracion.deviceID, tpv.codtpv, String.valueOf(mesaSel), ((ArgTraspasaMesa) args).submesas, String.valueOf(traspasaMesa_mesasel), handlerNotificaWS, (Activity) context);
                            } catch (Exception ex) {
                                errorPeticion = true;
                            }
                            break;

                        case pedirCuenta:
                            ClaseUtils.ProgressDialogo.mostrarDialogo(true, context.getResources().getString(R.string.progress_strPedirCuenta), context.getResources().getString(R.string.progress_strEspera), context);
                            try {
                                String imprimir = "N";
                                if (configuracion.cuentaptetpv) {
                                    imprimir = "S";
                                }
                                ServSincronizaBD.startActionPedirCuenta(context, configuracion.codigo, configuracion.deviceID, tpv.codtpv, String.valueOf(mesa.mesa), String.valueOf(submesa.submesa), submesa.nfactura, String.valueOf(listaUsers.get(userSel).codigo),imprimir, handlerNotificaWS, (Activity) context);
                            } catch (Exception ex) {
                                errorPeticion = true;
                            }
                            break;

                        case creditoTagID:
                            try {
                                ServSincronizaBD.startActionCreditoTagID(context, configuracion.codigo, configuracion.deviceID, ((ArgCreditoTagID) args).codemp, ((ArgCreditoTagID) args).tagID, handlerNotificaWS, (Activity) context);
                            } catch (Exception ex) {
                                errorPeticion = true;
                            }
                            break;

                        case creditoRoom:
                            try {
                                ServSincronizaBD.startActionCreditoRoom(context, configuracion.codigo, configuracion.deviceID, ((ArgCreditoRoom) args).codemp, ((ArgCreditoRoom) args).room, handlerNotificaWS, (Activity) context);
                            } catch (Exception ex) {
                                errorPeticion = true;
                            }
                            break;

                        case pagaMesa:
                            ClaseUtils.ProgressDialogo.mostrarDialogo(true, context.getResources().getString(R.string.progress_strPagarCuenta), context.getResources().getString(R.string.progress_strEspera), context);
                            try {
                                ArgPagaMesa argpm = (ArgPagaMesa) args;
                                ServSincronizaBD.startActionPagaMesa(context, configuracion.codigo, configuracion.deviceID, argpm.codusu, tpv.codtpv, String.valueOf(mesaSel),
                                        String.valueOf(mesa.submesas.get(subMesaSel).submesa),argpm.codemp_ext, argpm.nreserva, argpm.formapago, argpm.importe, argpm.igic, argpm.efectivo, argpm.tarjeta, argpm.cuentacasa, argpm.credito,
                                        argpm.nfactura, argpm.codclicasa, argpm.entregado, argpm.cambio, argpm.firma, argpm.apto, argpm.imprimir, handlerNotificaWS, (Activity) context);
                            } catch (Exception ex) {
                                errorPeticion = true;
                            }
                            break;

                        case recuperaFacturas:
                            ClaseUtils.ProgressDialogo.mostrarDialogo(true, context.getResources().getString(R.string.progress_strRecueperaFacturas), context.getResources().getString(R.string.progress_strEspera), context);
                            try {
                                ServSincronizaBD.startActionRecuperaFacturas(context, configuracion.codigo, configuracion.deviceID, tpv.codtpv, handlerNotificaWS, (Activity) context);
                            } catch (Exception ex) {
                                errorPeticion = true;
                            }
                            break;

                        case recuperaFactura:
                            ClaseUtils.ProgressDialogo.mostrarDialogo(true, context.getResources().getString(R.string.progress_strRecueperaFactura), context.getResources().getString(R.string.progress_strEspera), context);
                            try {
                                ServSincronizaBD.startActionRecuperaFactura(context, configuracion.codigo, configuracion.deviceID, String.valueOf(((ArgRecuperaFactura) args).codenl), handlerNotificaWS, (Activity) context);
                            } catch (Exception ex) {
                                errorPeticion = true;
                            }
                            break;

                        case imprimeFactura:
                            ClaseUtils.ProgressDialogo.mostrarDialogo(true, ((ArgImprimeFactura) args).aviso, context.getResources().getString(R.string.progress_strEspera), context);
                            try {
                                ServSincronizaBD.startActionImprimeFactura(context, configuracion.codigo, configuracion.deviceID, ((ArgImprimeFactura) args).codenl, handlerNotificaWS, (Activity) context);
                            } catch (Exception ex) {
                                errorPeticion = true;
                            }
                            break;
                    }

                    if (errorPeticion) {
                        ClaseUtils.ProgressDialogo.cerrarDialogo();
                        ClaseUtils.AvisoResultado aviso = new ClaseUtils.AvisoResultado();
                        aviso.setTitulo(context.getResources().getString(R.string.alert_strAviso));
                        aviso.setMensaje(mensajeError);
                        aviso.setBotonTrue(context.getResources().getString(R.string.strReintentar));
                        aviso.setBotonFalse(context.getResources().getString(R.string.strCancelar));
                        aviso.setOnclick(dlgOnclickErrorWs);
                        aviso.setContext(context);
                        aviso.execute();

                    } else {
                        errorPeticion = false;
                    }

                }

                @Override
                public void onFailure(Call<RespuestaTestjsonWS> call, Throwable t) {
                    if (toolBarMenu != null) {
                        MenuItem item = toolBarMenu.findItem(R.id.action_conexion);
                        if (item != null) item.setIcon(R.drawable.bar_wifi_gris);
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            avisoFalloConexion();
                        }
                    }, 2000);

                    final Long timefin= Calendar.getInstance().getTimeInMillis();
                    latenciaTest.add(timeini,timefin,false);
                    actualizaLatenciaTest();
                }
            });
        } catch (Exception e) {
            if (toolBarMenu != null) {
                MenuItem item = toolBarMenu.findItem(R.id.action_conexion);
                if (item != null) {
                    item.setIcon(R.drawable.bar_wifi_rojo);
                }
            }

            estadoConexion = String.valueOf(e.getMessage());
            Toast.makeText(context, estadoConexion, Toast.LENGTH_LONG).show();

        }
    }

    private static void avisoFalloConexion() {
        ClaseUtils.ProgressDialogo.cerrarDialogo();
        if (reintentosWS < 5) {
            switch (ultimaAccionWs) {
                case test:
                case validar:
                case sincronizar:
                case sincronizarbg:
                case getEstadoMesas:
                case recuperaFacturas:
                case getLineasMesa:
                case pedirCuenta:
                    conectaWS(ultimaAccionWs, null);
                    break;
                case actualizaPax:
                    conectaWS(ultimaAccionWs, argActualizaPax);
                    break;
                case traspasaMesa:
                    conectaWS(ultimaAccionWs, argTraspasaMesa);
                    break;
                case enviaLineasMesa:
                    conectaWS(ultimaAccionWs, argEnviaLineasMesa);
                    break;
                case siguePlato:
                    conectaWS(ultimaAccionWs, argSiguePlato);
                    break;
                case creditoTagID:
                    conectaWS(ultimaAccionWs, argCreditoTagID);
                    break;
                case creditoRoom:
                    conectaWS(ultimaAccionWs, argCreditoRoom);
                    break;
                case pagaMesa:
                    conectaWS(ultimaAccionWs, argPagaMesa);
                    break;
                case recuperaFactura:
                    conectaWS(ultimaAccionWs, argRecuperaFactura);
                    break;
                case imprimeFactura:
                    conectaWS(ultimaAccionWs, argImprimeFactura);
                    break;
            }
        }
        else if (reintentosWS == 5) {
            ClaseUtils.ProgressDialogo.cerrarDialogo();
            ClaseUtils.AvisoResultado aviso = new ClaseUtils.AvisoResultado();
            aviso.setTitulo(context.getResources().getString(R.string.alert_strAviso));
            aviso.setMensaje(context.getResources().getString(R.string.strErrorWsTest));
            aviso.setBotonTrue(context.getResources().getString(R.string.strReintentar));
            aviso.setBotonFalse(context.getResources().getString(R.string.strCancelar));
            aviso.setOnclick(dlgOnclickErrorWs);
            aviso.setContext(context);
            aviso.execute();
        } else {
            if (reintentosWS < 10) { //5*2 10 sg
                hSyncDatos.postDelayed(checkConexion, 1000);
            } else { // 7*3 21sg
                hSyncDatos.postDelayed(checkConexion, 2000);
            }
        }
        reintentosWS += 1;
        if (reintentosWS > 20) {
            reintentosWS = 0;
        }

        estado = ultimoEstado;
    }

    private static void validarWS(final boolean aviso, final boolean force) {
        try {
            ClaseUtils.ProgressDialogo.mostrarDialogo(true, context.getResources().getString(R.string.progress_strValidar), context.getResources().getString(R.string.progress_strEspera), context);

            APIService apiService = ClaseServicioWeb.getAPIService(force, configuracion.connectionTO, configuracion.rwTO1, configuracion.rwTO1, latenciaTest.getUltima());

            long lVersionCode = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                lVersionCode = context.getPackageManager().getPackageInfo(context.getPackageName(),0).getLongVersionCode();
            }
            else {
                PackageInfo packageInfo =  context.getPackageManager().getPackageInfo(context.getPackageName(),0);
                int iVersion = packageInfo.versionCode;
                lVersionCode = Long.parseLong(String.valueOf(iVersion));
            }
            int versionCode = Integer.valueOf(String.valueOf(lVersionCode));
            String appcode = String.valueOf( versionCode);

            apiService.validarUpdate(configuracion.codigo, configuracion.deviceID, appcode).enqueue(new Callback<RespuestaValidarUpdateWS>() {
                @Override
                public void onResponse(Call<RespuestaValidarUpdateWS> call, Response<RespuestaValidarUpdateWS> response) {
                    if (! response.isSuccessful()) {
                        if (toolBarMenu != null) {
                            MenuItem item = toolBarMenu.findItem(R.id.action_conexion);
                            if (item != null) item.setIcon(R.drawable.bar_wifi_gris);
                        }
                        ClaseUtils.ProgressDialogo.cerrarDialogo();
                        ClaseUtils.AvisoResultado aviso1 = new ClaseUtils.AvisoResultado();
                        aviso1.setTitulo(context.getResources().getString(R.string.alert_strAviso));
                        aviso1.setMensaje(context.getResources().getString(R.string.strErrorWsValidar));
                        aviso1.setBotonTrue(context.getResources().getString(R.string.strReintentar));
                        aviso1.setBotonFalse(context.getResources().getString(R.string.strCancelar));

                        aviso1.setOnclick(dlgOnclickErrorWs);
                        aviso1.setContext(context);
                        aviso1.execute();
                        return;
                    }

                    else if (! response.body().getErrnum().equals("0") ){
                        if (toolBarMenu != null) {
                            MenuItem item = toolBarMenu.findItem(R.id.action_conexion);
                            if (item != null) item.setIcon(R.drawable.bar_wifi_rojo);
                        }
                        ClaseUtils.ProgressDialogo.cerrarDialogo();
                        ClaseUtils.AvisoResultado aviso1 = new ClaseUtils.AvisoResultado();
                        aviso1.setTitulo(context.getResources().getString(R.string.alert_strAviso));
                        aviso1.setMensaje(context.getResources().getString(R.string.strErrorWsNoValidado) + "\r\n" + response.body().getErrdesc());
                        aviso1.setBotonTrue(context.getResources().getString(R.string.strAceptar));
                        aviso1.setBotonFalse("");
                        aviso1.setOnclick(dlgOnclickSalir);
                        aviso1.setContext(context);
                        aviso1.execute();
                        return;
                    }

                    if (response.body().getIgic() == null) response.body().setIgic("0.00");

                    if (response.body().getTipo_igic() != null)
                        ClaseCondicionesVenta.tipo_igic = response.body().getTipo_igic();
                    if (response.body().getIgic() != null)
                        ClaseCondicionesVenta.igic = Double.parseDouble(response.body().getIgic().replace(",", "."));
                    if (response.body().getTipoCuentaCasa() != null)
                        ClaseCondicionesVenta.tipo_cuentacasa = response.body().getTipoCuentaCasa();
                    if (response.body().getPedirclave() != null)
                        ClaseCondicionesVenta.pedir_clave = response.body().getPedirclave();
                    if (response.body().getOrdenplatos() != null)
                        ClaseCondicionesVenta.orden_platos = response.body().getOrdenplatos();
                    if (response.body().getPedir_firma() != null)
                        ClaseCondicionesVenta.pedir_firma = response.body().getPedir_firma();

                    if (ClaseCondicionesVenta.establecimientos == null) {
                        ClaseCondicionesVenta.establecimientos = new ArrayList<>();
                        ClaseCondicionesVenta.establecimientos.add(new ClaseEstablecimientos(configuracion.codigo, configuracion.empresa));
                    }


                    if (response.body().getHoteles_aux() != null && response.body().getHoteles_aux().size() > 0 ) {
                        for (ClaseEstablecimientos establecimiento: response.body().getHoteles_aux()) {
                            boolean existe = false;
                            for (ClaseEstablecimientos tmp: ClaseCondicionesVenta.establecimientos){
                                if (tmp.codigo.equals(establecimiento.codigo)){
                                    existe = true;
                                    break;
                                }
                            }
                            if (! existe) {
                                ClaseCondicionesVenta.establecimientos.add(new ClaseEstablecimientos(establecimiento.codigo, establecimiento.nombre));
                            }
                        }
                    }

                    ClaseCondicionesVenta.guardarPreferencias(context);

                    // verifica actualiziaciones
                    String new_appfile = response.body().getAppfile();
                    if (! new_appfile.equals("")) {
                        appfile = new_appfile;
                        String appName ="";
                        try {
                            appName = context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
                        } catch (PackageManager.NameNotFoundException e) {
                            appName = BuildConfig.VERSION_NAME;
                            throw new RuntimeException(e);
                        }

                        if (! appfile .equals(appName))
                            if (toolBarMenu !=  null){
                                MenuItem item = toolBarMenu.findItem(R.id.action_update);
                                if (item != null){
                                    item.setVisible(true);
                                }
                            }
                    }



                    if (toolBarMenu != null) {
                        MenuItem item = toolBarMenu.findItem(R.id.action_conexion);
                        conexionValidada = true;
                        if (item != null) {
                            if (response == null || response.body() == null) {
                                item.setIcon(R.drawable.bar_wifi_rojo);
                                conexionValidada = false;
                            } else if (Integer.valueOf(response.body().getErrnum()) == 0) {
                                item.setIcon(R.drawable.bar_wifi_verde);
                            } else {
                                item.setIcon(R.drawable.bar_wifi_rojo);
                            }
                        }
                    }

                    ClaseUtils.ProgressDialogo.cerrarDialogo();


                    hSyncDatos.postDelayed(checkConexion, 200);
                    estadoConexion = String.valueOf(response.body().getErrnum() + ", " + response.body().getErrdesc());
                    if (aviso) {
                        Toast.makeText(context, estadoConexion, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<RespuestaValidarUpdateWS> call, Throwable t) {
                    if (toolBarMenu != null) {
                        MenuItem item = toolBarMenu.findItem(R.id.action_conexion);
                        if (item != null) item.setIcon(R.drawable.bar_wifi_gris);
                    }
                    ClaseUtils.ProgressDialogo.cerrarDialogo();
                    ClaseUtils.AvisoResultado aviso1 = new ClaseUtils.AvisoResultado();
                    aviso1.setTitulo(context.getResources().getString(R.string.alert_strAviso));
                    aviso1.setMensaje(context.getResources().getString(R.string.strErrorWsValidar));
                    aviso1.setBotonTrue(context.getResources().getString(R.string.strReintentar));
                    aviso1.setBotonFalse(context.getResources().getString(R.string.strCancelar));
                    aviso1.setOnclick(dlgOnclickErrorWs);
                    aviso1.setContext(context);
                    aviso1.execute();

                }
            });
        } catch (Exception e) {
            if (toolBarMenu != null) {
                MenuItem item = toolBarMenu.findItem(R.id.action_conexion);
                if (item != null) {
                    item.setIcon(R.drawable.bar_wifi_rojo);
                }
            }

            estadoConexion = String.valueOf(e.getMessage());
            Toast.makeText(context, estadoConexion, Toast.LENGTH_LONG).show();

        }

    }

    private static void actualizaTablas() {


        layProgress.setVisibility(VISIBLE);
        pbProgress.setProgress(0);
        pbProgress.setMax(100);
        tvProgress.setText("");
        if (tablasSync.size() > 0) {
            String tabla = "";

            tabla = tablasSync.get(0);

            pbProgress.setVisibility(VISIBLE);

            ClaseUtils.ProgressDialogo.mostrarDialogo(true, context.getResources().getString(R.string.progress_strSincronizar) + " '" + tabla + "'", context.getResources().getString(R.string.progress_strEspera), context);
            ServSincronizaBD.startActionSincronizaTabla(context, configuracion.deviceID, configuracion.codigo, tabla, handlerNotificaWS, (Activity) context);
        } else {
            if (tablasSync.size() == 0 && tablasSyncBackGround.size() == 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layProgress.setVisibility(View.GONE);

                    }
                }, 5000);


            }
            ClaseUtils.ProgressDialogo.cerrarDialogo();
            hSyncDatos.postDelayed(checkConexion, 2000);

        }
    }

    private static void actualizaTablasBG() {


        layProgress.setVisibility(VISIBLE);
        pbProgress.setProgress(0);
        pbProgress.setMax(100);
        tvProgress.setText("");

        if (tablasSyncBackGround.size() > 0) {
            String tabla = "";
            tabla = tablasSyncBackGround.get(0);

            pbProgress.setVisibility(VISIBLE);
            ClaseUtils.ProgressDialogo.mostrarDialogo(true, context.getResources().getString(R.string.progress_strSincronizar) + " '" + tabla + "'", context.getResources().getString(R.string.progress_strEspera), context);
            ServSincronizaBD.startActionSincronizaTabla(context, configuracion.deviceID, configuracion.codigo, tabla, handlerNotificaWS, (Activity) context);
        } else {
            if (tablasSync.size() == 0 && tablasSyncBackGround.size() == 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layProgress.setVisibility(View.GONE);

                    }
                }, 5000);

            }
            ClaseUtils.ProgressDialogo.cerrarDialogo();
            hSyncDatos.postDelayed(checkConexion, 2000);

        }

    }

    private static Runnable checkConexion = new Runnable() {
        @Override
        public void run() {
            hSyncDatos.removeCallbacks(checkConexion);
            if (toolBarMenu != null) {

                if (toolBarMenu.findItem(R.id.action_conexion) != null) {
                    toolBarMenu.findItem(R.id.action_conexion).setIcon(R.drawable.bar_wifi_azul);
                }
            }


            if (!conexionValidada) {
                ultimaAccionWs = enAccionesWS.validar;
                conectaWS(ultimaAccionWs, null);
            } else if (tablasSync.size() > 0) {
                ultimaAccionWs = enAccionesWS.sincronizar;
                conectaWS(ultimaAccionWs, null);
            } else if (tablasSyncBackGround.size() > 0) {
                ultimaAccionWs = enAccionesWS.sincronizarbg;
                conectaWS(ultimaAccionWs, null);
            } else {
                if (estado.equals(enEstado.iniciando)) {
                    ultimoEstado = estado;
                    estado = enEstado.user;
                    cambiaEstado(estado);
                }
                ultimaAccionWs = enAccionesWS.test;
                conectaWS(ultimaAccionWs, null);
            }
        }
    };
    //endregion

    //region BROADCAST RECEIVERS

    private static SyncTablasReceiver syncTablasReceiver = new SyncTablasReceiver();

    private void registraBroadcastReceivers() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ServSincronizaBD.ACTION_ERROR_BLOQUEADO);

        intentFilter.addAction(ServSincronizaBD.ACTION_OK_MD5);
        intentFilter.addAction(ServSincronizaBD.ACTION_ERROR_MD5);
        intentFilter.addAction(ServSincronizaBD.ACTION_OK_TABLA);
        intentFilter.addAction(ServSincronizaBD.ACTION_ERROR_TABLA);


        intentFilter.addAction(ServSincronizaBD.ACTION_OK_ESTADOMESAS);
        intentFilter.addAction(ServSincronizaBD.ACTION_ERROR_ESTADOMESAS);
        intentFilter.addAction(ServSincronizaBD.ACTION_OK_LINEASVENTA);
        intentFilter.addAction(ServSincronizaBD.ACTION_ERROR_LINEASVENTA);
        intentFilter.addAction(ServSincronizaBD.ACTION_OK_PENSION);
        intentFilter.addAction(ServSincronizaBD.ACTION_ERROR_PENSION);
        intentFilter.addAction(ServSincronizaBD.ACTION_OK_GRABALINEASVENTA);
        intentFilter.addAction(ServSincronizaBD.ACTION_ERROR_GRABALINEASVENTA);
        intentFilter.addAction(ServSincronizaBD.ACTION_OK_PEDIRCUENTA);
        intentFilter.addAction(ServSincronizaBD.ACTION_ERROR_PEDIRCUENTA);
        intentFilter.addAction(ServSincronizaBD.ACTION_OK_ACTUALIZAPAX);
        intentFilter.addAction(ServSincronizaBD.ACTION_ERROR_ACTUALIZAPAX);
        intentFilter.addAction(ServSincronizaBD.ACTION_OK_TRASPASAMESA);
        intentFilter.addAction(ServSincronizaBD.ACTION_ERROR_TRASPASAMESA);
        intentFilter.addAction(ServSincronizaBD.ACTION_OK_PAGAMESA);
        intentFilter.addAction(ServSincronizaBD.ACTION_ERROR_PAGAMESA);
        intentFilter.addAction(ServSincronizaBD.ACTION_OK_CREDITO);
        intentFilter.addAction(ServSincronizaBD.ACTION_ERROR_CREDITO);
        intentFilter.addAction(ServSincronizaBD.ACTION_OK_SIGUEPLATO);
        intentFilter.addAction(ServSincronizaBD.ACTION_ERROR_SIGUEPLATO);
        intentFilter.addAction(ServSincronizaBD.ACTION_OK_RECUPERAFACTURAS);
        intentFilter.addAction(ServSincronizaBD.ACTION_ERROR_RECUPERAFACTURAS);
        intentFilter.addAction(ServSincronizaBD.ACTION_OK_RECUPERAFACTURA);
        intentFilter.addAction(ServSincronizaBD.ACTION_ERROR_RECUPERAFACTURA);
        intentFilter.addAction(ServSincronizaBD.ACTION_OK_IMPRIMEFACTURA);
        intentFilter.addAction(ServSincronizaBD.ACTION_ERROR_IMPRIMEFACTURA);

        //context.registerReceiver(syncTablasReceiver, intentFilter, RECEIVER_NOT_EXPORTED);
        ContextCompat.registerReceiver(context,syncTablasReceiver,intentFilter,ContextCompat.RECEIVER_NOT_EXPORTED);

    }

    private static void actualizaListasFragments() {
        if (updateUsers || updateTPVS || updateNomMesas) {
            hdDelay = new Handler();
            hdDelay.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (updateUsers) {
                        cargaUsuariosBD();
                        if (fragmentUsers != null) fragmentUsers.actualizaUsers();
                    }
                    if (updateTPVS) {
                        cargaTpvsBD();
                        if (fragmentTPVs != null) fragmentTPVs.actualizaTPVs();
                    }
                    if (updateNomMesas) {
                        cargaNomMesasBD();
                        listaMesas = ClaseMesas.recuperaMesas(tpv, listaNombreMesas);
                        if (fragmentMesas != null) fragmentMesas.actualizaMesas(true);
                    }

                }
            }, 500);
        }
    }

    private void desRegistraBroadcastReceivers() {
        unregisterReceiver(syncTablasReceiver);
    }

    public static class SyncTablasReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {

            int delay = 2000;
            int resultadoWS = 0; //0 error. 1 OK. 2. pasa automaticamente a un nuevo estado
            String mensaje = "";
            boolean cerrar = false;

            if (intent.getAction().equals(ServSincronizaBD.ACTION_ERROR_DOWNLOAD_APP)) {
                mensaje = context.getString(R.string.strErrorWsDownloadApp)+". "+intent.getExtras().getString("error");
                resultadoWS = 4;
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_ERROR_BLOQUEADO)) {
                mensaje = context.getString(R.string.strErrorWsNoValidado) + "\r\n" + intent.getExtras().getString("mensaje");
                resultadoWS = 3;
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_ERROR_MD5)) {
                String tabla = "";
                if (tablasSync.size() > 0) {
                    tabla = tablasSync.get(0);
                } else if (tablasSyncBackGround.size() > 0) {
                    tabla = tablasSyncBackGround.get(0);
                }

                mensaje = context.getString(R.string.strErrorWsMD5) + " tabla '" + tabla + "'";
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_ERROR_TABLA)) {
                String tabla = "";
                if (tablasSync.size() > 0) {
                    tabla = tablasSync.get(0);
                } else if (tablasSyncBackGround.size() > 0) {
                    tabla = tablasSyncBackGround.get(0);
                }
                mensaje = context.getString(R.string.strErrorWsTabla) + " tabla '" + tabla + "'";
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_ERROR_ESTADOMESAS)) {
                mensaje = context.getString(R.string.strErrorWsMesas);
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_ERROR_LINEASVENTA)) {
                mensaje = context.getString(R.string.strErrorWsLineasVenta);
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_ERROR_PENSION)) {
                mensaje = context.getString(R.string.strErrorRecuperaPension);
                recalculaPension = false;
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_ERROR_GRABALINEASVENTA)) {
                mensaje = context.getString(R.string.strErrorWsGrabaLineasVenta);
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_ERROR_ACTUALIZAPAX)) {
                mensaje = context.getString(R.string.strErrorActualizaPax);
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_ERROR_TRASPASAMESA)) {
                mensaje = context.getString(R.string.strErrorTraspasaMesa);
                cerrar = true;
                if (fragmentSubmesas != null) fragmentSubmesas.actualizaSubmesas();
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_ERROR_SIGUEPLATO)) {
                mensaje = context.getString(R.string.strErrorSiguePlato);
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_ERROR_PEDIRCUENTA)) {
                mensaje = context.getString(R.string.strErrorPedirCuenta);
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_ERROR_CREDITO)) {
                mensaje = context.getString(R.string.strErrorCredito) + "\n" + intent.getExtras().getString("errdesc");
                cerrar = true;

                if (fragmentFormaPago != null)
                    fragmentFormaPago.cargaCredito(intent, false);
                Log.d("ActivityInicio", "Broadcast error_credito");

                if (intent.getExtras().getString("errnum") != null && !intent.getExtras().getString("errnum").equals("1")) {
                    ClaseUtils.ProgressDialogo.cerrarDialogo();
                    ClaseUtils.Aviso.mostrarAviso(context.getResources().getString(R.string.alert_strAviso), mensaje, context);
                    resultadoWS = 1; // se da por correcto. no reintentar
                }
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_ERROR_PAGAMESA)) {
                mensaje = context.getString(R.string.strErrorPagaMesa);
                if (Integer.parseInt(intent.getStringExtra("errnum"))>=10000){
                    mensaje = "No es posible facturar \r\n"+intent.getStringExtra("errdesc");
                }
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_ERROR_RECUPERAFACTURAS)) {
                mensaje = context.getString(R.string.strErrorRecuperaFacturas);
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_ERROR_RECUPERAFACTURA)) {
                mensaje = context.getString(R.string.strErrorRecuperaFactura);
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_ERROR_IMPRIMEFACTURA)) {
                mensaje = context.getString(R.string.strErrorImprimeFactura);
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_OK_MD5)) {
                String tabla = intent.getExtras().getString("tabla", "");
                pbProgress.setProgress(pbProgress.getMax());
                tvProgress.setText(tabla + " sincronizada");

                if (tablasSync.contains(tabla)) {
                    tablasSync.remove(tabla);
                } else if (tablasSyncBackGround.contains(tabla)) {
                    tablasSyncBackGround.remove(tabla);
                }

                if (tablasSync.size() == 0 && tablasSyncBackGround.size() == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            layProgress.setVisibility(View.GONE);
                            grabaFechaSincronizacion(ClaseUtils.now("yyMMddHHmmss"));
                        }
                    }, 5000);
                    cerrar = true;

                    actualizaListasFragments();
                }
                delay = 200;
                resultadoWS = 1;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_OK_TABLA)) {
                String tabla = intent.getExtras().getString("tabla", "---");
                boolean ultimo = intent.getExtras().getBoolean("ultimo", false);

                if (ultimo) {
                    pbProgress.setProgress(pbProgress.getMax());
                    tvProgress.setText(tabla + " sincronizada");

                    if (tablasSync.contains(tabla)) {
                        tablasSync.remove(tabla);
                    } else if (tablasSyncBackGround.contains(tabla)) {
                        tablasSyncBackGround.remove(tabla);
                    }


                    if (tablasSync.size() == 0 && tablasSyncBackGround.size() == 0) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                layProgress.setVisibility(View.GONE);
                                grabaFechaSincronizacion(ClaseUtils.now("yyMMddHHmmss"));
                            }
                        }, 5000);
                        cerrar = true;

                        actualizaListasFragments();
                    }
                }
                delay = 200;
                resultadoWS = 1;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_OK_ESTADOMESAS)) {
                cargaEstadoMesas(intent);

                if (estado == enEstado.traspasa || estado == enEstado.traspasa_sub) {

                    seleccionaMesa(traspasaMesa_mesasel, listaMesas.get(traspasaMesa_mesasel).pax, "");
                } else {
                    ultimoEstado = estado;
                    estado = enEstado.mostrarmesas;
                    cambiaEstado(estado);
                }
                resultadoWS = 1;
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_OK_LINEASVENTA)) {
                cargaLineasVenta(intent);

                if (mesa.submesas.size() > 0 && mesa.submesas.get(0).pension != null) {
                    muestraPension(mesa.submesas.get(0));
                }

                ultimoEstado = estado;
                estado = enEstado.venta;
                cambiaEstado(estado);
                resultadoWS = 1;
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_OK_PENSION)) {
                mesa.submesas.get(subMesaSel).pension = intent.getStringExtra("pension");
                mesa.submesas.get(subMesaSel).descPension = intent.getStringExtra("desc_pension");
                mesa.submesas.get(subMesaSel).tipoPension = intent.getStringExtra("tipo_pension");
                mesa.submesas.get(subMesaSel).descTipoPension = intent.getStringExtra("desc_tipo_pension");
                calculaAplicaPension(mesa.submesas.get(subMesaSel));
                if (recalculaPension) {
                    recalculaPension = false;
                    recalculaPension(mesa.submesas.get(subMesaSel));
                }

                muestraPension(mesa.submesas.get(subMesaSel));

                ultimoEstado = estado;
                estado = enEstado.venta;
                cambiaEstado(estado);
                resultadoWS = 1;
                cerrar = true;            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_OK_GRABALINEASVENTA)) {

                String rooms = "";
                ClaseUtils.ProgressDialogo.cerrarDialogo();
                ArrayList<ResGrabaLineasSubmesas> submesas = intent.getParcelableArrayListExtra("submesas");
                if (submesas != null) {
                    for (ResGrabaLineasSubmesas tsubmesa : submesas) {


                        for (ClaseSubMesas submesa : mesa.submesas) {
                            if (submesa.submesa == tsubmesa.getSubmesa()) {
                                submesa.codenl = tsubmesa.getCodenl();
                                if (! submesa.habitacion.equals("")) {
                                    if (! rooms.equals("")) {
                                        rooms += ",";
                                    }
                                    rooms += submesa.habitacion;
                                }
                                continue;
                            }
                        }
                    }
                }

                if (!procesaLineasVenta(intent)) {


                    if (fragmentLineaVentas != null)
                        fragmentLineaVentas.actualizaLineaVentas(false);
                    if (fragmentDetalleVenta != null) fragmentDetalleVenta.actualizaDetalleVenta();
                    if (fragmentSubmesas != null) fragmentSubmesas.actualizaSubmesas();

                } else {
                    // si está configurado mantenerse en la mesa para otras acciones, llamar a sincronizalineas. si no a planning de mesas
                    ultimoEstado = enEstado.venta;
                    if (configuracion.mantenerVenta == true) {
                        estado = enEstado.cargalineas;
                    } else {// else
                        estado = enEstado.cargamesas;
                    }
                    cambiaEstado(estado);
                }

                //imprimir comanda
                if (configuracion.comandaapp) {

                    imprimirComanda(argEnviaLineasMesa.getLineasVentas(),rooms);
                }

                resultadoWS = 1;
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_OK_ACTUALIZAPAX)) {
                int pax = 0;
                for (ClaseSubMesas submesa : listaMesas.get(actualizaPax_mesasel).submesas) {
                    submesa.paxTraspaso = 0;
                    pax += submesa.pax;
                }

                tvBarPax.setText(String.valueOf(submesa.pax));
                tvBarMesa.setText(mesa.descripcion + "-" + Integer.toString(submesa.submesa));
                tvBarRoom.setText(submesa.habitacion);
                muestraPension(submesa);

                if (fragmentMesas != null) fragmentMesas.actualizaMesas(false);
                resultadoWS = 1;
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_OK_SIGUEPLATO)) {
                resultadoWS = 1;
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_OK_TRASPASAMESA)) {

                listaMesas = ClaseMesas.recuperaMesas(tpv, listaNombreMesas);

                mesaSel = traspasaMesa_mesasel;
                mesa = listaMesas.get(mesaSel);
                tvBarMesa.setText(mesa.descripcion + "-" + Integer.toString(submesa.submesa));

                ultimaAccionWs = enAccionesWS.getLineasMesa;
                reintentosWS = 0;
                conectaWS(ultimaAccionWs, null);
                resultadoWS = 2;
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_OK_PEDIRCUENTA)) {

                ClaseUtils.ProgressDialogo.cerrarDialogo();

                String nfactura = intent.getExtras().getString("nfactura","");
                String pago_fecha_hora = intent.getExtras().getString("pago_fecha_hora","");
                String habitacion = intent.getExtras().getString("habitacion","");

                CabeceraTiquet cabeceraTiquet = new CabeceraTiquet();
                cabeceraTiquet.setRazon("PRINCIPADO S.L.");
                cabeceraTiquet.setCif("B35073311");
                cabeceraTiquet.setTPV(tpv.descripcion);
                cabeceraTiquet.setHora(ClaseUtils.now("dd/MM/yyyy HH:mm:ss"));
                cabeceraTiquet.setMesa(mesa.descripcion.length() == 0 ? String.valueOf(mesa.mesa) : mesa.descripcion);

                cabeceraTiquet.setTipoTiquet(ClaseUtils.enTipoTiquet.cuenta);
                cabeceraTiquet.setNfactura(nfactura);
                cabeceraTiquet.setPago_fecha_hora( pago_fecha_hora);
                cabeceraTiquet.setFormapago("N");
                cabeceraTiquet.setApto(habitacion);

                imprimirCuenta(cabeceraTiquet  );

                ultimoEstado = enEstado.venta;
                listaMesas.get(mesaSel).submesas.get(subMesaSel).estado = ClaseSubMesas.enEstados.tiquet;
                fragmentSubmesas.actualizaSubmesas();

                if (configuracion.mantenerVenta == true) {
                    estado = enEstado.cargalineas;
                } else {// else
                    estado = enEstado.cargamesas;
                }
                cambiaEstado(estado);

                resultadoWS = 1;
                cerrar = true;
            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_OK_CREDITO)) {
                boolean resultado = true;
                ClaseUtils.ProgressDialogo.cerrarDialogo();
                if (Integer.valueOf(intent.getStringExtra("errnum")) > 1000) {
                    ClaseUtils.Aviso.mostrarAviso(context.getResources().getString(R.string.alert_strError),
                            intent.getStringExtra("errdesc"), context);
                }
                switch (Integer.valueOf(intent.getStringExtra("errnum"))) {
                    case 0:
                    case 1002:
                    case 1004:
                        resultado = true;
                        break;
                    default:
                        resultado = false;

                }

                if (fragmentFormaPago != null)
                    fragmentFormaPago.cargaCredito(intent, resultado);

                Log.d("ActivityInicio", "Broadcast ok_credito");

                resultadoWS = 1;
                cerrar = true;

            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_OK_PAGAMESA)) {

                ClaseUtils.ProgressDialogo.cerrarDialogo();


                String nfactura = intent.getExtras().getString("nfactura","");
                String pago_fecha_hora = intent.getExtras().getString("pago_fecha_hora","");
                String formapago = intent.getExtras().getString("formapago","E");
                String efectivo = intent.getExtras().getString("efectivo","0.00");
                String tarjeta = intent.getExtras().getString("tarjeta","0.00");
                String cuentacasa= intent.getExtras().getString("cuentacasa","0.00");
                String credito = intent.getExtras().getString("credito","0.00");
                String codclicasa = intent.getExtras().getString("codclicasa","0");
                String entregado = intent.getExtras().getString("entregado","0.00");
                String cambio = intent.getExtras().getString("cambio","0.00");
                String firma = intent.getExtras().getString("firma","");
                String apto = intent.getExtras().getString("apto","");

                CabeceraTiquet cabeceraTiquet = new CabeceraTiquet();
                cabeceraTiquet.setRazon("PRINCIPADO S.L.");
                cabeceraTiquet.setCif("B35073311");
                cabeceraTiquet.setTPV(tpv.descripcion);
                cabeceraTiquet.setHora(ClaseUtils.now("dd/MM/yyyy HH:mm:ss"));
                cabeceraTiquet.setMesa(mesa.descripcion.length() == 0 ? String.valueOf(mesa.mesa) : mesa.descripcion);

                cabeceraTiquet.setTipoTiquet(ClaseUtils.enTipoTiquet.tiquet);
                cabeceraTiquet.setNfactura(nfactura);
                cabeceraTiquet.setPago_fecha_hora(pago_fecha_hora);
                cabeceraTiquet.setFormapago(formapago);
                cabeceraTiquet.setEfectivo(efectivo);
                cabeceraTiquet.setTarjeta(tarjeta);
                cabeceraTiquet.setCuentacasa(cuentacasa);
                cabeceraTiquet.setCredito(credito);
                cabeceraTiquet.setCodclicasa(codclicasa);
                cabeceraTiquet.setEntregado(entregado);
                cabeceraTiquet.setCambio(cambio);
                cabeceraTiquet.setFirma(firma);
                cabeceraTiquet.setApto(apto);

                imprimirCuenta(cabeceraTiquet );

                ultimoEstado = estado;
                estado = enEstado.cargamesas;
                cambiaEstado(estado);
                resultadoWS = 1;
                cerrar = true;

            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_OK_RECUPERAFACTURAS)) {

                cargaFacturas(intent);
                ultimoEstado = estado;
                estado = enEstado.facturas;
                cambiaEstado(estado);

                Log.d("ActivityInicio", "Broadcast ok_recuperafacturas");
                resultadoWS = 1;
                cerrar = true;

            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_OK_RECUPERAFACTURA)) {

                ClaseUtils.ProgressDialogo.cerrarDialogo();
                ClaseUtils.VerFactura aviso = ClaseUtils.VerFactura.newInstance();
                FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();

                final ClaseFacturaCabecera facturaCabecera = intent.getParcelableExtra("cabecera");
                String firma = intent.getStringExtra("firma");

                aviso.setEmpresa(intent.getStringExtra("empresa"));
                aviso.setCif(intent.getStringExtra("cif"));

                 String mesa = intent.getStringExtra("mesa") + "-" + intent.getStringExtra("submesa");
                if (listaNombreMesas.containsKey(tpv.codtpv + "-" + intent.getStringExtra("mesa"))) {
                    mesa = listaNombreMesas.get(tpv.codtpv + "-" + intent.getStringExtra("mesa")).getDescripcion() + "-" + intent.getStringExtra("submesa");
                }
                aviso.setMesa(mesa);

                String nombreTpv = tpv.descripcion;
                aviso.setTpv(nombreTpv);
                aviso.setIgic(ClaseUtils.double2string(ClaseCondicionesVenta.igic, 2));
                String codusu = intent.getStringExtra("codusu");
                String camarero = codusu;

                for (ClaseUsers user : listaUsers) {
                    if (user.codigo == Integer.valueOf(codusu)) {
                        camarero = user.nombre;
                        break;
                    }
                }
                aviso.setCamarero(camarero);
                aviso.setCabecera(facturaCabecera);
                aviso.setFirma(firma);
                ArrayList<LineasVenta> lineas = intent.getParcelableArrayListExtra("lineas");
                ArrayList<ClaseLineaVentas> lineasVenta = new ArrayList<>();

                facturaCabecera.tipoCtaCasa = ClaseCondicionesVenta.tipo_cuentacasa;
                facturaCabecera.totalCtaCasa = 0.00;

                for (LineasVenta linea : lineas) {
                    // si es coste, precios al coste y total coste. Si es gratis precios (pvp - coste -0) y total 0.00
                    if (facturaCabecera.formapago.equalsIgnoreCase("C") && facturaCabecera.tipoCtaCasa.equalsIgnoreCase("C")) {
                        facturaCabecera.totalCtaCasa += linea.getTcoste();
                        linea.setPeuros(linea.getPcoste());
                        linea.setTeuros(linea.getTcoste());
                    } else if (facturaCabecera.formapago.equalsIgnoreCase("C") && facturaCabecera.tipoCtaCasa.equalsIgnoreCase("G")) {
                        // invitación se muestra el tiquet tal cual inlcuido total, pero desglose a 0
                        linea.setTeuros(linea.getPeuros() * linea.getCantidad());
                        facturaCabecera.totalCtaCasa += linea.getTeuros();
                    }

                    ClaseLineaVentas lineaventa = LineaVentaMapper.fromRest(linea, 0);

                    lineasVenta.add(lineaventa);


                }

                final String tEmpresa = intent.getStringExtra("empresa");
                final String tCif = intent.getStringExtra("cif");
                final String tMesa = mesa;
                final String tNombreTpv = nombreTpv;
                final String tCamarero = camarero;
                final ArrayList<ClaseLineaVentas> tLineasVenta = new ArrayList<>(lineasVenta);
                final ClaseFacturaCabecera tFacturaCabecera = facturaCabecera;


                aviso.setListaLineaVentas(lineasVenta);

                final String codenl = intent.getStringExtra("codenl");
                aviso.setInterfaceVerFactura(new ClaseUtils.VerFactura.InterfaceVerFactura() {
                    @Override
                    public void vfImprimir() {

                        ClaseUtils.VerFactura.cerrar();

                        if (configuracion.cuentaapp) {
                            imprimirCopiaCuenta(tEmpresa, tCif, tNombreTpv, tMesa, tCamarero, tFacturaCabecera, tLineasVenta);
                        }
                        if (configuracion.cuentatpv) {

                            String strAviso = context.getResources().getString(R.string.alert_strAviso);

                            argImprimeFactura = ArgImprimeFactura.getINSTANCE();
                            argImprimeFactura.setCodenl(codenl);
                            argImprimeFactura.setAviso(strAviso);

                            ultimaAccionWs = enAccionesWS.imprimeFactura;
                            reintentosWS = 0;
                            conectaWS(ultimaAccionWs, argImprimeFactura);

                        }

                    }
                });

                aviso.show(fm, "dialog");
                Log.d("ActivityInicio", "Broadcast ok_recuperafactura");
                resultadoWS = 1;
                cerrar = true;

            } else if (intent.getAction().equals(ServSincronizaBD.ACTION_OK_IMPRIMEFACTURA)) {
                Log.d("ActivityInicio", "Broadcast ok_imprimefacturas");
                resultadoWS = 1;
                cerrar = true;
            }


            if (cerrar) {
                ClaseUtils.ProgressDialogo.cerrarDialogo();
            }


            if (resultadoWS == 0) {
                ClaseUtils.ProgressDialogo.cerrarDialogo();
                ClaseUtils.AvisoResultado aviso = new ClaseUtils.AvisoResultado();
                aviso.setTitulo(context.getResources().getString(R.string.alert_strAviso));
                aviso.setMensaje(mensaje);
                aviso.setBotonTrue(context.getResources().getString(R.string.strReintentar));
                aviso.setBotonFalse(context.getResources().getString(R.string.strCancelar));
                aviso.setOnclick(dlgOnclickErrorWs);
                aviso.setContext(context);
                aviso.execute();
            }
            else if (resultadoWS == 1) {
                hSyncDatos.postDelayed(checkConexion, delay);
            }
            else if (resultadoWS == 2) {
                //no hago nada. se ha mandado otra conexion al servicio web y esperar respuesta
            }
            else if (resultadoWS == 3 ) {
                ClaseUtils.ProgressDialogo.cerrarDialogo();
                ClaseUtils.AvisoResultado aviso1 = new ClaseUtils.AvisoResultado();
                aviso1.setTitulo(context.getResources().getString(R.string.alert_strAviso));
                aviso1.setMensaje(mensaje);
                aviso1.setBotonTrue(context.getResources().getString(R.string.strAceptar));
                aviso1.setBotonFalse("");
                aviso1.setOnclick(dlgOnclickSalir);
                aviso1.setContext(context);
                aviso1.execute();
                return;

            }
            else if (resultadoWS == 4){
                ClaseUtils.ProgressDialogo.cerrarDialogo();
                ClaseUtils.Aviso.mostrarAviso(context.getResources().getString(R.string.alert_strAviso),mensaje,context);
            }


        }
    }

    //endregion

    //region HANDLER NOTIFICACIONES GRABACION DATOS SERVICIO WEB


    private static Handler handlerNotificaWS = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            boolean isprogress = false;
            boolean isultimo = false;

            int pos = 0;
            int nregistros = 0;
            int max = 0;
            String tabla = "";
            Bundle bundle = null;
            switch (msg.what) {
                case ACTION_DESCARGANDO_REGISTROS:
                    bundle = msg.getData();
                    int reg = bundle.getInt("reg");
                    int regs = bundle.getInt("regs");
                    tabla = bundle.getString("tabla", "---");
                    break;

                case ACTION_BORRANDO_REGISTROS:
                    bundle = msg.getData();
                    tabla = bundle.getString("tabla", "---");
                    break;

                case ACTION_GRABANDO_REGISTROS:

                    bundle = msg.getData();
                    pos = bundle.getInt("pos", 0);
                    nregistros = bundle.getInt("nregistros",0);
                    max = bundle.getInt("max", 0);
                    isultimo = bundle.getBoolean("ultimo", false);
                    tabla = bundle.getString("tabla", "---");
                    if (isultimo && tabla.equalsIgnoreCase("usuarios")) {
                        updateUsers = true;
                    }
                    if (isultimo && tabla.equalsIgnoreCase("tpvs")) {
                        updateTPVS = true;
                    }
                    if (isultimo && tabla.equalsIgnoreCase("nom_mesas")) {
                        updateNomMesas = true;
                    }
                    isprogress = true;
                    break;


                case ACTION_GRABANDO_USUARIOS:
                    bundle = msg.getData();
                    pos = bundle.getInt("pos", 0);
                    max = bundle.getInt("max", 0);
                    tabla = "Grabando usuarios";
                    updateUsers = true;
                    isprogress = true;
                    break;
                case ACTION_GRABANDO_TPVS:
                    bundle = msg.getData();
                    pos = bundle.getInt("pos", 0);
                    max = bundle.getInt("max", 0);
                    tabla = "Grabando tpvs";
                    updateTPVS = true;
                    isprogress = true;
                    break;
                case ACTION_GRABANDO_NOMMESAS:
                    bundle = msg.getData();
                    pos = bundle.getInt("pos", 0);
                    max = bundle.getInt("max", 0);
                    tabla = "Grabando nom_mesas";
                    updateNomMesas = true;
                    isprogress = true;
                    break;
                case ACTION_GRABANDO_CABECERAS:
                    bundle = msg.getData();
                    pos = bundle.getInt("pos", 0);
                    max = bundle.getInt("max", 0);
                    tabla = "Grabando cabeceras";
                    isprogress = true;
                    break;
                case ACTION_GRABANDO_PRODUCTOS:
                    bundle = msg.getData();
                    pos = bundle.getInt("pos", 0);
                    max = bundle.getInt("max", 0);
                    tabla = "Grabando productos";
                    isprogress = true;
                    break;
                case ACTION_GRABANDO_ALERGENOS:
                    bundle = msg.getData();
                    pos = bundle.getInt("pos", 0);
                    max = bundle.getInt("max", 0);
                    tabla = "Grabando alergenos";
                    isprogress = true;
                    break;
                case ACTION_GRABANDO_ETIQUETAS:
                    bundle = msg.getData();
                    pos = bundle.getInt("pos", 0);
                    max = bundle.getInt("max", 0);
                    tabla = "Grabando etiquetas";
                    isprogress = true;
                    break;


            }
            if (isprogress) {
                pbProgress.setMax(max);
                pbProgress.setProgress(pos);
                tvProgress.setText(tabla + " " + pos + "/" + max);
                if (hdDelay != null) {
                    hdDelay = null;
                }
            }

            if (isultimo) {
                if (isultimo && (pos < (nregistros - 1))) {
                    isultimo = false;
                }

                Intent bcIntent = new Intent();
                bcIntent.setPackage(context.getPackageName());
                bcIntent.setAction(ServSincronizaBD.ACTION_OK_TABLA);
                bcIntent.putExtra("tabla", tabla);
                bcIntent.putExtra("ultimo", isultimo);
                context.sendBroadcast(bcIntent);
            }

        }
    };


    //endregion


    @Override
    protected void onRestart() {
        super.onRestart();
        ClaseBluetooth.setHandlerNotificaBT(handlerNotificaBT);

        if (hndDesconectaSocket != null) hndDesconectaSocket = null;
        hndDesconectaSocket = new Handler();
        hndDesconectaSocket.postDelayed(new Runnable() {
            @Override
            public void run() {
                ClaseBluetooth.desconectarSocketBT();
                hndDesconectaSocket = null;
            }
        }, 10000);

        new ClaseBluetooth().enableTimer(10000, true);
        try {
            setIconoBT(ClaseBluetooth.checkPrinterStatus(), ClaseBluetooth.getPrinterError());
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInicio = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
         if (! nofinalizar) finalizarApp();
        activityInicio = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        desRegistraBroadcastReceivers();
        hSyncDatos.removeCallbacksAndMessages(null);

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null ) nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registraBroadcastReceivers();
        reintentosWS = 0;
        hSyncDatos.postDelayed(checkConexion, 500);

        ClaseBluetooth.setHandlerNotificaBT(handlerNotificaBT);
        nofinalizar = false;

        if (latenciaTest == null){
            latenciaTest = new ClaseLatencia();
        }
        if (latenciaRecuperaMesas == null){
            latenciaRecuperaMesas = new ClaseLatencia();
        }
        if (latenciaRecuperaComanda == null){
            latenciaRecuperaComanda = new ClaseLatencia();
        }
        if (latenciaEnviaComanda == null){
            latenciaEnviaComanda = new ClaseLatencia();
        }
        if (latenciaPideCuenta == null){
            latenciaPideCuenta = new ClaseLatencia();
        }
        if (latenciaPagaCuenta == null){
            latenciaPagaCuenta = new ClaseLatencia();
        }

        ClaseNFC nfc = new ClaseNFC(this,this);

        //recupera ultimo estado
        recuperaUltimoEstadoApp();
        if (ultimoEstadoApp.getEstado() == enEstado.user || ultimoEstadoApp.getEstado() == enEstado.tpv){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    estado = enEstado.user;
                    seleccionaUsuario(ultimoEstadoApp.getUsuario());

                    if (ultimoEstadoApp.getEstado() == enEstado.tpv) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                estado = enEstado.tpv;
                                seleccionaTPV(ultimoEstadoApp.getTpv());
                            }
                        }, 200);
                    }
                }
            }, 200);
        }


    }

    private static void finalizarApp() {
        if (hndDesconectaSocket != null) {
            hndDesconectaSocket.removeCallbacksAndMessages(null);
            hndDesconectaSocket = null;
        }
        ClaseBluetooth.set_imprimiendo(false);
        ClaseBluetooth.detener();
        handlerNotificaBT = null;

        guardaUltimoEstadoApp();

        //android.os.Process.killProcess(android.os.Process.myPid());
        if (activityInicio != null) activityInicio.finish();
        System.exit(0);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityInicio.context = this;
        activityInicio = this;
        nofinalizar = false;
        appfile = "";
        recalculaPension = false;

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    salir();
                }
            }
        });


        boolOnCreate = true;

        conexionValidada = false;
        estadoConexion = "";

        switch (getResources().getString(R.string.device).trim().toLowerCase()) {
            case "phone":
                ClaseUtils.modelo = ClaseUtils.enModel.phone;
                break;
            case "tab":
                ClaseUtils.modelo = ClaseUtils.enModel.tab;
                break;
        }

//        new ClaseCondicionesVenta(configuracion.codigo, configuracion.empresa);

        setContentView(R.layout.ini_activity_inicio);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);


        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        tvMenuUser = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvMenuUser);

        navigationMenu = navigationView.getMenu();
        tpvMenu = navigationMenu.findItem(R.id.nav_tpv);
        LinearLayout viewTpvMenu = (LinearLayout) tpvMenu.getActionView();
        tvMenuTpv = viewTpvMenu.findViewById(R.id.tvMenuTpv);

        mesasMenu = navigationMenu.findItem(R.id.nav_mesas_acciones);

        if (mesasMenu != null) ((MenuItem) mesasMenu).setVisible(false);

        if (navigationMenu.findItem(R.id.nav_logout) != null)
            navigationMenu.findItem(R.id.nav_logout).setVisible(false);

        layFrgInicio = findViewById(R.id.layFrgInicio);
        layFrgUsers = findViewById(R.id.layFrgUsers);
        layFrgTPVs = findViewById(R.id.layFrgTPVs);
        layFrgFacturas = findViewById(R.id.layFrgFacturas);
        layCompacto = findViewById(R.id.layCompacto);
        layFrgCabeceras = findViewById(R.id.layFrgCabeceras);
        layFrgProductos = findViewById(R.id.layFrgProductos);
        layFrgMesas = findViewById(R.id.layFrgMesas);
        layFrgVentas = findViewById(R.id.layFrgVentas);
        layFrgFormaPago = findViewById(R.id.layFrgFormaPago);
        layFrgLineaVentas = findViewById(R.id.layFrgLineaVentas);
        layFrgDetalleVenta = findViewById(R.id.layFrgDetalleVenta);


        if (ClaseUtils.modelo == ClaseUtils.enModel.tab) {
            tvBarUser = findViewById(R.id.tvBarUser);
            tvBarTPV = findViewById(R.id.tvBarTpv);
        }

        tvCabecera = findViewById(R.id.tvCabecera);

        tvBarMesa = findViewById(R.id.tvBarMesa);

        tvBarPax = findViewById(R.id.tvBarPax);
        tvBarRoom = findViewById(R.id.tvBarRoom);
        tvBarPension = findViewById(R.id.tvBarPension);
        ivAplicaPension = findViewById(R.id.ivAplicaPension);
        ivAplicaPension.setOnClickListener( (View ) -> {
            ClaseUtils.VerInfoPension aviso = new ClaseUtils.VerInfoPension();
            aviso.setTitulo(context.getResources().getString(R.string.dialog_strInfoProducto));
            aviso.setOnclick(dlgOnClickNada);
            aviso.setContext(context);
            aviso.setSubMesa(submesa);

            List<Hora_Comidas> horaComidas = null;
            try {
                horaComidas = baseDatos.getHoraComidasTPV(Integer.valueOf(tpv.codtpv));
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            aviso.setListaHoraComidas(horaComidas);
            aviso.execute(0);

        });


        ivBarPax = findViewById(R.id.ivBarPax);
        ivBarPax.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (estado == enEstado.venta || estado == enEstado.cargalineas) {

                    //cuando hago long click sobre el icono pax de la barra superior dentro de venta o cargalineas. no añade pax. solo reordena
                    //OK
                    ClaseUtils.PidePaxSubmesa aviso = new ClaseUtils.PidePaxSubmesa();
                    aviso.setTitulo(context.getResources().getString(R.string.alert_strAviso));
                    aviso.setMensaje(context.getResources().getString(R.string.dialog_strTituloPidePax));
                    aviso.setBotonTrue(context.getResources().getString(R.string.strAceptar));
                    aviso.setBotonFalse(context.getResources().getString(R.string.strCancelar));
                    aviso.setOnclick(onClickOrganizaPax);
                    aviso.setTipo(ClaseUtils.PidePaxSubmesa.TIPO_DIALOGO_ORGANIZA_PAX);

                    aviso.setContext(context);
                    aviso.setSubmesas(listaMesas.get(mesaSel).submesas);
                    aviso.setMover_uds(false);
                    aviso.execute(0);
                }
                return true;
            }
        });

        ivBarRoom = findViewById(R.id.ivBarRoom);
        ivBarRoom.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (submesa.estado == ClaseSubMesas.enEstados.tiquet ) {
                    ClaseUtils.Aviso aviso = new ClaseUtils.Aviso();
                    aviso.mostrarAviso("Aviso","Cuenta ya impresa.\r\nNo es posible cambiar habitación",context);
                    return false;
                }
                if (estado == enEstado.venta || estado == enEstado.cargalineas) {

                    ClaseUtils.PideRoom aviso = new ClaseUtils.PideRoom();
                    aviso.setTitulo(context.getResources().getString(R.string.alert_strAviso));
                    aviso.setMensaje(context.getResources().getString(R.string.dialog_strTituloPideRoom));
                    aviso.setBotonTrue(context.getResources().getString(R.string.strAceptar));
                    aviso.setBotonFalse(context.getResources().getString(R.string.strCancelar));
                    aviso.setOnclick(dlgOnClickPideRoom);

                    aviso.setContext(context);
                    aviso.execute();
                }
                return false;
            }
        });

        ivAddSubmesa = findViewById(R.id.ivAddSubmesa);
        ivCabeceras = findViewById(R.id.ivCabeceras);
        ivPlatos = findViewById(R.id.ivPlatos);

        ivAddSubmesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (estado == enEstado.venta) {
                    // si la última submesa esta vacia no añade nuevas
                    if (mesa.submesas.get(mesa.submesas.size() - 1).lineasVentas.size() == 0) {
                        ClaseUtils.Aviso.mostrarAviso(getResources().getString(R.string.alert_strAviso),
                                context.getResources().getString(R.string.dialog_strNoAnadeSubmesa), context);
                        return;
                    }
                    // si hay que pedir pax, mostrar dialogo
                    if (configuracion.pax) {
                        //CUANDO DESDE VENTAS PULSO AÑADIR SUBMESA. DEBE PEDIR HABITACION, NUEVOS PAX Y TRASPASO DE PAX
                        //ok


                        ClaseUtils.PidePaxSubmesa aviso = new ClaseUtils.PidePaxSubmesa();
                        aviso.setTitulo(context.getResources().getString(R.string.alert_strAviso));
                        aviso.setMensaje(context.getResources().getString(R.string.dialog_strTituloPidePax));
                        aviso.setBotonTrue(context.getResources().getString(R.string.strAceptar));
                        aviso.setBotonFalse(context.getResources().getString(R.string.strCancelar));
                        aviso.setOnclick(dlgOnClickPidePax);
                        aviso.setTipo(ClaseUtils.PidePaxSubmesa.TIPO_DIALOGO_AÑADIR_PAX);

                        aviso.setContext(context);
                        aviso.setSubmesas(listaMesas.get(mesaSel).submesas);

                        aviso.setMover_uds(false);
                        int pax  = 0;
                        if (mesa.submesas.get(subMesaSel).estado == ClaseSubMesas.enEstados.tiquet){
                            aviso.setRoom(mesa.submesas.get(subMesaSel).habitacion);
                                    pax = mesa.submesas.get(subMesaSel).pax;
                        }
                        aviso.execute(pax);
                    } else {
                        añadirSubmesaPax(0, true,"","");
                    }
                }

            }
        });

        ivCabeceras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragmentCabeceras != null) {
                    fragmentCabeceras.ampliarCabeceras();
                }
                if (layFrgCabeceras.getVisibility() == View.GONE)
                    layFrgCabeceras.setVisibility(VISIBLE);
            }
        });

        ivPlatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (configuracion.mostrarProductosAuto){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (fragmentProductos != null) fragmentProductos.ampliarProductos();
                    }
                }, 600);
                //}
                if (configuracion.compacto) {
                    layCompacto.setVisibility(View.GONE);

                }

            }
        });


        layProgress = findViewById(R.id.layProgresoDatos);
        layProgress.setVisibility(View.GONE);
        tvProgress = findViewById(R.id.tvProgress);
        tvProgress.setText("");
        pbProgress = findViewById(R.id.pbProgress);
        pbProgress.setProgress(0);

        tvLatencia = findViewById(R.id.tvLatencia);
        tvLatencia.setText("Conexión: 0/0 ms");
        tvLatencia.setTextColor(Color.WHITE);


        baseDatos = ClaseBaseDatos.getDatabase(this, handlerNotificaWS, this);
        configuracion = new ClaseConfiguracion(this);
        cargaConfiguracion();

        new ClaseCondicionesVenta(configuracion.codigo, configuracion.empresa);
        ClaseCondicionesVenta.cargarPreferencias(this, configuracion.codigo, configuracion.empresa);
        String tmp = ClaseCondicionesVenta.orden_platos;

        new ServSincronizaBD();
        ServSincronizaBD.setConfig(configuracion);
        Log.v("On Create", "datos cargados");

        if (latenciaTest == null){
            latenciaTest = new ClaseLatencia();
        }
        if (latenciaRecuperaMesas == null){
            latenciaRecuperaMesas = new ClaseLatencia();
        }
        if (latenciaRecuperaComanda == null){
            latenciaRecuperaComanda = new ClaseLatencia();
        }
        if (latenciaEnviaComanda == null){
            latenciaEnviaComanda = new ClaseLatencia();
        }
        if (latenciaPideCuenta == null){
            latenciaPideCuenta = new ClaseLatencia();
        }
        if (latenciaPagaCuenta == null){
            latenciaPagaCuenta = new ClaseLatencia();
        }

        tvLatencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String test =            "Test               "+String.valueOf(latenciaTest.getUltima())+"/"+String.valueOf(latenciaTest.getMedia())+"/"+String.valueOf(latenciaTest.getMaxima())+"\r\n";
                String recuperaMesas =   "Recupera Mesas     "+String.valueOf(latenciaRecuperaMesas.getUltima())+"/"+String.valueOf(latenciaRecuperaMesas.getMedia())+"/"+String.valueOf(latenciaRecuperaMesas.getMaxima())+"\r\n";
                String recuperaComanda = "Recupera Comanda   "+String.valueOf(latenciaRecuperaComanda.getUltima())+"/"+String.valueOf(latenciaRecuperaComanda.getMedia())+"/"+String.valueOf(latenciaRecuperaComanda.getMaxima())+"\r\n";
                String enviaComanda =    "Envia Comanda      "+String.valueOf(latenciaEnviaComanda.getUltima())+"/"+String.valueOf(latenciaEnviaComanda.getMedia())+"/"+String.valueOf(latenciaEnviaComanda.getMaxima())+"\r\n";
                String pedirCuenta =     "Pedir Cuenta       "+String.valueOf(latenciaPideCuenta.getUltima())+"/"+String.valueOf(latenciaPideCuenta.getMedia())+"/"+String.valueOf(latenciaPideCuenta.getMaxima())+"\r\n";
                String pagarMesa =       "Pagar Mesa         "+String.valueOf(latenciaPagaCuenta.getUltima())+"/"+String.valueOf(latenciaPagaCuenta.getMedia())+"/"+String.valueOf(latenciaPagaCuenta.getMaxima())+"\r\n";

                ClaseUtils.Aviso.mostrarAviso("Latencias", test + recuperaMesas+ recuperaComanda+enviaComanda+pedirCuenta+pagarMesa, context);
            }
        });

        String endpoint = configuracion.url;
        if (!endpoint.endsWith("/") && !configuracion.api.startsWith("/"))
            endpoint += "/";

        endpoint += configuracion.api;

        if (!endpoint.endsWith("/"))
            endpoint += "/";

        ClaseServicioWeb.setBaseUrl(endpoint);
        reintentosWS = 0;

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(configuracion.empresa + "(" + configuracion.codigo + ")");

        iniciaListas();

        if (savedInstanceState == null) {
            cargaFragments();
        }

        ultimoEstadoApp = new ClaseUltimoEstadoApp();

        ultimoEstado = enEstado.iniciando;
        estado = enEstado.iniciando;
        cambiaEstado(estado);
        isHappyHour = false;


        registraBroadcastReceivers();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layFrgLineaVentas.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
            layFrgDetalleVenta.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        }


        DisplayMetrics dm = getResources().getDisplayMetrics();
        float a = dm.xdpi;
        cargaOrdenPlatos();

        // timer de refresco de base de datos
        hSyncDatos = new Handler();
        hSyncDatos.postDelayed(checkConexion, 500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolOnCreate = false;
            }
        }, 1000);


        if (ClaseBluetooth.getApplicationContext() == null) {

            if (configuracion.btMac.equalsIgnoreCase("00:00:00:00:00:00")
                    && !configuracion.btModelo.equalsIgnoreCase(getResources().getString(R.string.strConfigBTModel_IPDA045))) {
                configuracion.btModelo = getResources().getString(R.string.strConfigBTModel_SCREEN);
            }

            new ClaseBluetooth(this, getApplicationContext(), handlerNotificaBT, configuracion.btModelo);

            if (ClaseBluetooth.iniciaBlueToothAdapter(true) == null) {
                ClaseUtils.AvisoToast.mostrarAviso("Aviso",context.getResources().getText(R.string.strErrorBTNoDisponible).toString(),context,2000);
            } else
                ClaseBluetooth.setRemoteDevice(configuracion.btMac);
        }
        setIconoBT(ClaseBluetooth.checkPrinterStatus(), ClaseBluetooth.getPrinterError());
        estadoBT = ClaseBluetooth.checkPrinterStatus();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.activity_inicio, menu);


        setIconoBT(ClaseBluetooth.checkPrinterStatus(), ClaseBluetooth.getPrinterError());

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final View v = findViewById(R.id.action_printer);
                if (v != null) {
                    v.setOnTouchListener(new ClaseOnTouch(getApplicationContext()) {
                        @Override
                        public void onLongCLick() {
                            String estado = "";
                            if (ClaseBluetooth.checkPrinterStatus() == ClaseBluetoothPrintConstantes.STATE_IDLE) {
                                estado = "Impresora no iniciada";
                            } else if (ClaseBluetooth.checkPrinterStatus() == ClaseBluetoothPrintConstantes.STATE_CONNECTING) {
                                estado = "Conectando impresora";
                            } else if (ClaseBluetooth.checkPrinterStatus() == ClaseBluetoothPrintConstantes.STATE_CONNECTED) {
                                estado = ClaseBluetooth.getPrinterError();
                                if (estado.equalsIgnoreCase("")) {
                                    estado = "Impresora conectada";
                                }
                            } else if (ClaseBluetooth.checkPrinterStatus() == ClaseBluetoothPrintConstantes.STATE_DISCONNECTED) {
                                estado = "Impresora desconectada";
                            } else if (ClaseBluetooth.checkPrinterStatus() == ClaseBluetoothPrintConstantes.STATE_CONNECTING_ERROR) {
                                estado = "Error conectando con impresora";
                            } else if (ClaseBluetooth.checkPrinterStatus() == ClaseBluetoothPrintConstantes.STATE_CONNECTED_SOCKET) {
                                estado = "Conexión con impresora activa";
                            } else if (ClaseBluetooth.checkPrinterStatus() == ClaseBluetoothPrintConstantes.STATE_DISCONNECTING) {
                                estado = "Desconectando impresora";
                            }

                            Toast.makeText(context, estado, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onClick() {

                            //testPrint();
                            ClaseBluetooth.set_forzarTest(true);
                            ClaseBluetooth.restartTimer();
                        }
                    });

                }
            }
        });

        toolBarMenu = menu;

        MenuItem item = toolBarMenu.findItem(R.id.action_update);
        if (item != null){
            item.setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_update) {
            ServSincronizaBD.descargaAPK(appfile,this);
        }

        if (id == R.id.action_settings) {
            //desconecto BT
            if (hndDesconectaSocket != null) {
                hndDesconectaSocket.removeCallbacksAndMessages(null);
                hndDesconectaSocket = null;
            }
            iniciarConfiguracion();

        } else if (id == R.id.action_conexion) {
            item.setIcon(R.drawable.bar_wifi_azul);
            ultimaAccionWs = enAccionesWS.test;
            reintentosWS = 0;
            conectaWS(enAccionesWS.test, null);

        } else if (id == R.id.action_sincronizar) {
            conexionValidada = false;
            grabaFechaSincronizacion("200101000000");
            cargaTablasSync();
            cargaTablasSyncBackGround();
        }


        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        submesaSelPendiente = -1;
        lineaSelPendiente = -1;

        if (id == R.id.nav_salir) {
            estadoPendiente = enEstado.salir;
            if (!compruebaCambiosLineaPendientes() && !compruebaLineaPendientesEnvio())
                salir();
        } else if (id == R.id.nav_user) {
            ultimoEstado = estado;
            estado = enEstado.user;
            cambiaEstado(estado);
        } else if (id == R.id.nav_logout) {
            estadoPendiente = enEstado.cerrar;
            if (!compruebaCambiosLineaPendientes() && !compruebaLineaPendientesEnvio()) {
                logout();
            }
        } else if (id == R.id.nav_tpv) {
            estadoPendiente = enEstado.tpv;
            if (!compruebaCambiosLineaPendientes() && !compruebaLineaPendientesEnvio()) {
                ultimoEstadoApp.setEstado(enEstado.user);
                ultimoEstadoApp.setTpv(-1);
                guardaUltimoEstadoApp();

                irTpv();
            }
        } else if (id == R.id.nav_facturas) {
            if (estado != enEstado.facturas) {
                estadoPendiente = estado;
                irFacturas();
            }
        } else if (id == R.id.nav_mesas) {
            estadoPendiente = enEstado.cargamesas;
            if (!compruebaCambiosLineaPendientes() && !compruebaLineaPendientesEnvio()) {
                irCargamesas();
            }
        }  // submenu de mesas
        else if (id == R.id.nav_mesa_traspasa) {
            estadoPendiente = enEstado.venta;
            if (!compruebaCambiosLineaPendientes() && !compruebaLineaPendientesEnvio() && !compruebaMesaVacia()) {
                irTraspasaMesa();
            }
        } else if (id == R.id.nav_mesa_traspasa_sub) {
            estadoPendiente = enEstado.venta;
            if (!compruebaCambiosLineaPendientes() && !compruebaLineaPendientesEnvio() && !compruebaSubmesaVacia()) {
                irTraspasaSubMesa();
            }
        } else if (id == R.id.nav_mesa_cuenta) {
            estadoPendiente = enEstado.venta;
            if (!compruebaCambiosLineaPendientes() && !compruebaLineaPendientesEnvio()) {
                ultimoEstado = estado;
                estado = enEstado.cuenta;
                cambiaEstado(estado);
            }
        } else if (id == R.id.nav_mesa_cobrar) {
            estadoPendiente = enEstado.venta;
            if (!compruebaCambiosLineaPendientes() && !compruebaLineaPendientesEnvio()) {
                ultimoEstado = enEstado.venta;
                estado = enEstado.pago;
                cambiaEstado(estado);
            }
        } else if (id == R.id.nav_acercade) {
            String cadena = getResources().getString(R.string.acercade_strMensaje);

            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                String code = "";
                String name = packageInfo.versionName;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    code = String.valueOf(packageInfo.getLongVersionCode());
                } else {
                    code = String.valueOf(packageInfo.versionCode);
                }

                cadena = cadena.replace("[code]", code);
                cadena = cadena.replace("[version]", name);
                cadena = cadena.replace("[appkey]", configuracion.deviceID);

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            ClaseUtils.Aviso.mostrarAviso(getResources().getString(R.string.alert_strAcercade),
                    cadena, context);
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void cargaConfiguracion() {
        try {
            List<Configuracion> parametros = baseDatos.getConfiguracion();
            Log.v("cargaConfiguracion", "Finalizada la carga de base de datos");
            if (parametros != null && parametros.size() > 0) {
                for (Configuracion parametro : parametros) {
                    switch (parametro.getParametro().toLowerCase()) {
                        case "demo":
                            configuracion.demo = Boolean.valueOf(parametro.getValor().toLowerCase());
                            break;
                        case "empresa":
                            configuracion.empresa = parametro.getValor();
                            break;
                        case "codigo":
                            configuracion.codigo = parametro.getValor();
                            break;
                        case "carga_lineas_venta":
                            configuracion.cargaLineasVenta = Boolean.valueOf(parametro.getValor().toLowerCase());
                            break;
                        case "clave_camareros":
                            configuracion.claveCamareros = Boolean.valueOf(parametro.getValor().toLowerCase());
                            break;
                        case "pax":
                            configuracion.pax = Boolean.valueOf(parametro.getValor().toLowerCase());
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
                        case "timer_fragment":
                            configuracion.timerFragment = Integer.valueOf(parametro.getValor());
                            break;

                        case "url":
                            configuracion.url = parametro.getValor();
                            if (!configuracion.url.endsWith("/"))
                                configuracion.url += "/";
                            if (!configuracion.url.toLowerCase().startsWith("http://") && ! configuracion.url.toLowerCase().startsWith(("https://")))
                                configuracion.url = "http://" + configuracion.url;
                            break;
                        case "api":
                            configuracion.api = parametro.getValor();
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

                        case "sincronizaauto":
                            configuracion.sincronizaAuto = Boolean.valueOf(parametro.getValor().toLowerCase());
                            break;
                        case "btmac":
                            configuracion.btMac = parametro.getValor().toUpperCase(Locale.ROOT);
                            break;
                        case "btmodelo":
                            configuracion.btModelo = parametro.getValor().toLowerCase(Locale.ROOT);
                            break;
                        case "btname":
                            configuracion.btName = parametro.getValor().toLowerCase(Locale.ROOT);
                            break;
                        case "comandaapp":
                            configuracion.comandaapp = !parametro.getValor().toLowerCase(Locale.ROOT).equals("true")? false:true;
                            break;
                        case "comandatpv":
                            configuracion.comandatpv = !parametro.getValor().toLowerCase(Locale.ROOT).equals("true")? false:true;
                            break;
                        case "cuentapteapp":
                            configuracion.cuentapteapp = !parametro.getValor().toLowerCase(Locale.ROOT).equals("true")? false:true;
                            break;
                        case "cuentaptetpv":
                            configuracion.cuentaptetpv = !parametro.getValor().toLowerCase(Locale.ROOT).equals("true")? false:true;
                            break;
                        case "cuentaapp":
                            configuracion.cuentaapp = !parametro.getValor().toLowerCase(Locale.ROOT).equals("true")? false:true;
                            break;
                        case "cuentatpv":
                            configuracion.cuentatpv =! parametro.getValor().toLowerCase(Locale.ROOT).equals("true")? false:true;
                            break;

                    }
                }

                if (configuracion.btModelo.equals("pantalla")){
                    configuracion.btMac = "00:00:00:00:00";
                    configuracion.btName = "PANTALLA";
                }
            }
        } catch (ExecutionException ee) {
        } catch (InterruptedException ie) {
        }

    }


    private void iniciaListas() {
        if (configuracion.sincronizaAuto) {
            cargaTablasSync();
            cargaTablasSyncBackGround();
        }
        else {
            tablasSync = new ArrayList<>();
            tablasSync.clear();
            tablasSyncBackGround = new ArrayList<>();
            tablasSyncBackGround.clear();
        }

        cargaUsuariosBD();
        cargaTpvsBD();
        cargaNomMesasBD();

    }

    private void cargaFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragmentManager.findFragmentByTag("INICIO") == null) {
            fragmentInicio = new FragmentInicio();
            fragmentTransaction.add(R.id.frgInicio, fragmentInicio, "INICIO");
        }


        if (fragmentManager.findFragmentByTag("USERS") == null) {
            fragmentUsers = new FragmentUsers();
            fragmentTransaction.add(R.id.frgUsers, fragmentUsers, "USERS");
        }

        if (fragmentManager.findFragmentByTag("TPVS") == null) {
            fragmentTPVs = new FragmentTPVs();
            fragmentTransaction.add(R.id.frgTPVs, fragmentTPVs, "TPVS");
        }

        if (fragmentManager.findFragmentByTag("FACTURAS") == null) {
            fragmentFacturas = new FragmentFacturas();
            fragmentTransaction.add(R.id.frgFacturas, fragmentFacturas, "FACTURAS");
        }


        if (fragmentManager.findFragmentByTag("CABECERAS") == null) {
            fragmentCabeceras = FragmentCabeceras.newInstance(configuracion.timerFragment * 1000);
            fragmentTransaction.add(R.id.frgCabeceras, fragmentCabeceras, "CABECERAS");
        }

        if (fragmentManager.findFragmentByTag("PRODUCTOS") == null) {
            fragmentProductos = FragmentProductos.newInstance(configuracion.timerFragment * 1000, !configuracion.mantenerProductos);
            fragmentTransaction.add(R.id.frgProductos, fragmentProductos, "PRODUCTOS");
        }

        if (fragmentManager.findFragmentByTag("MESAS") == null) {
            fragmentMesas = new FragmentMesas();
            fragmentTransaction.add(R.id.frgMesas, fragmentMesas, "MESAS");
        }

        if (fragmentManager.findFragmentByTag("SUBMESAS") == null) {
            fragmentSubmesas = new FragmentSubmesas();
            fragmentTransaction.add(R.id.frgSubmesas, fragmentSubmesas, "SUBMESAS");
        }

        if (fragmentManager.findFragmentByTag("LINEAVENTAS") == null) {
            fragmentLineaVentas = new FragmentLineaVentas();
            fragmentTransaction.add(R.id.frgLineaVentas, fragmentLineaVentas, "LINEAVENTAS");
        }

        if (fragmentManager.findFragmentByTag("DETALLEVENTA") == null) {
            fragmentDetalleVenta = FragmentDetalleVenta.newInstance();
            fragmentTransaction.add(R.id.frgDetalleVenta, fragmentDetalleVenta, "DETALLEVENTA");
        }

        if (fragmentManager.findFragmentByTag("FORMADEPAGO") == null) {
            fragmentFormaPago = new FragmentFormaPago();
            fragmentTransaction.add(R.id.frgFormaPago, fragmentFormaPago, "FORMADEPAGO");
        }

        fragmentTransaction.commitNow();

    }

    private static void cambiaEstado(enEstado estado) {

        switch (estado) {
            case iniciando:
                ultimoEstado = enEstado.iniciando;
                userSel = -1;
                user = null;
                tpvSel = -1;
                tpv = null;
                mesaSel = -1;
                mesa = null;

                if (ClaseUtils.modelo == ClaseUtils.enModel.tab) {
                    tvBarUser.setText("");
                    tvBarTPV.setText("");
                }
                tvMenuUser.setText("");
                tvMenuTpv.setText("");
                tvBarMesa.setText("");
                tvBarPax.setText("");
                tvBarRoom.setText("");
                muestraPension(null);

                if (navigationMenu != null)
                    ((MenuItem) navigationMenu.findItem(R.id.nav_tpv)).setEnabled(false);
                if (navigationMenu != null)
                    ((MenuItem) navigationMenu.findItem(R.id.nav_facturas)).setEnabled(false);
                if (navigationMenu != null)
                    ((MenuItem) navigationMenu.findItem(R.id.nav_mesas)).setEnabled(false);
                if (mesasMenu != null) ((MenuItem) mesasMenu).setVisible(false);

                mostrarFragment(estado);
                break;
            case user:
                userSel = -1;
                user = null;
                tpvSel = -1;
                tpv = null;
                mesaSel = -1;
                mesa = null;

                if (ClaseUtils.modelo == ClaseUtils.enModel.tab) {
                    tvBarUser.setText("");
                    tvBarTPV.setText("");
                }
                tvMenuUser.setText("");
                tvMenuTpv.setText("");
                tvBarMesa.setText("");
                tvBarPax.setText("");
                tvBarRoom.setText("");
                muestraPension(null);

                if (navigationMenu != null)
                    ((MenuItem) navigationMenu.findItem(R.id.nav_tpv)).setEnabled(false);
                if (navigationMenu != null)
                    ((MenuItem) navigationMenu.findItem(R.id.nav_facturas)).setEnabled(false);
                if (navigationMenu != null)
                    ((MenuItem) navigationMenu.findItem(R.id.nav_mesas)).setEnabled(false);
                if (mesasMenu != null) ((MenuItem) mesasMenu).setVisible(false);

                mostrarFragment(estado);
                if (fragmentUsers != null) fragmentUsers.actualizaUsers();


                break;
            case tpv:
                mesaSel = -1;
                mesa = null;

                if (ClaseUtils.modelo == ClaseUtils.enModel.tab) tvBarTPV.setText("");
                tvMenuTpv.setText("");
                tvBarMesa.setText("");
                tvBarPax.setText("");
                tvBarRoom.setText("");
                muestraPension(null);

                if (navigationMenu != null)
                    ((MenuItem) navigationMenu.findItem(R.id.nav_tpv)).setEnabled(true);

                if (navigationMenu != null)
                    ((MenuItem) navigationMenu.findItem(R.id.nav_facturas)).setEnabled(false);
                if (navigationMenu != null)
                    ((MenuItem) navigationMenu.findItem(R.id.nav_mesas)).setEnabled(false);
                if (mesasMenu != null) ((MenuItem) mesasMenu).setVisible(false);

                mostrarFragment(estado);
                if (fragmentTPVs != null) fragmentTPVs.actualizaTPVs();

                break;

            case cargamesas:
                listaMesas = ClaseMesas.recuperaMesas(tpv, listaNombreMesas);
                if (! ClaseUtils.VerTiquet.isVisible()) {
                    ClaseUtils.ProgressDialogo.mostrarDialogo(true, context.getResources().getString(R.string.progress_strRecuperaMesas), context.getResources().getString(R.string.progress_strEspera), context);
                }
                ultimaAccionWs = enAccionesWS.getEstadoMesas;
                reintentosWS = 0;
                conectaWS(ultimaAccionWs, null);
                break;

            case mostrarmesas:
                ultimoEstado = enEstado.mostrarmesas;
                mostrarFragment(estado);
                boolean refresca = false;
                if (mesaSel >= 0) refresca = true;
                if (fragmentMesas != null) fragmentMesas.actualizaMesas(refresca);
                tvBarMesa.setText("");
                tvBarPax.setText("");
                tvBarRoom.setText("");
                muestraPension(null);

                if (navigationMenu != null)
                    ((MenuItem) navigationMenu.findItem(R.id.nav_tpv)).setEnabled(true);
                if (navigationMenu != null)
                    ((MenuItem) navigationMenu.findItem(R.id.nav_facturas)).setEnabled(true);
                if (navigationMenu != null)
                    ((MenuItem) navigationMenu.findItem(R.id.nav_mesas)).setEnabled(true);

                if (mesasMenu != null) ((MenuItem) mesasMenu).setVisible(false);

                break;

            case cargalineas:
                if (! ClaseUtils.VerTiquet.isVisible()) {
                    ClaseUtils.ProgressDialogo.mostrarDialogo(true, context.getResources().getString(R.string.progress_strRecuperaLineas), context.getResources().getString(R.string.progress_strEspera), context);
                }
                ultimaAccionWs = enAccionesWS.getLineasMesa;
                reintentosWS = 0;
                conectaWS(ultimaAccionWs, null);
                break;

            case venta:
            ultimoEstado = enEstado.venta;
                cargaCabecerasTpvBD(tpv.tmenu);
                cabeceraSel = -1;
                cabecera = null;
                if (listaCabeceras.size() > 0) {
                    cabeceraSel = 0;
                    cabecera = listaCabeceras.get(0);
                    if (tvCabecera != null) tvCabecera.setText(cabecera.descripcion);

                    cargaProductosCabeceraBD(tpv.tmenu, cabecera.codigo);
                }
                productoSel = -1;
                producto = null;

                cargaEtiquetas();
                cargaAlergenos();
                cargaOrdenPlatos();

                if (mesasMenu != null) ((MenuItem) mesasMenu).setVisible(true);

                if (mesa.submesas.get(subMesaSel) == null || mesa.submesas.get(subMesaSel).lineasVentas.size() < 1 ) {
                    for (int t = 0;t < mesa.submesas.size(); t++) {
                        if (mesa.submesas.get(t).lineasVentas.size() < 1 && t <(mesa.submesas.size() -1 )){
                            continue;
                        }
                        subMesaSel = t;
                    }
                }

                submesa = mesa.submesas.get(subMesaSel);
                muestraPension(submesa);

                tvBarPax.setText(Integer.toString(submesa.pax));
                tvBarRoom.setText(submesa.habitacion);
                tvBarMesa.setText(mesa.descripcion + "-" + Integer.toString(submesa.submesa));



                mostrarFragment(estado);

                if (fragmentCabeceras != null) fragmentCabeceras.actualizaCabeceras();
                if (fragmentProductos != null) fragmentProductos.actualizaProductos(true);
                if (fragmentSubmesas != null) fragmentSubmesas.actualizaSubmesas();

                ordenSel = ORDEN_COMANDA_ENTRANTE;
                if (fragmentLineaVentas != null) {
                    fragmentLineaVentas.setOrdenSel(ORDEN_COMANDA_ENTRANTE);
                    fragmentLineaVentas.actualizaLineaVentas(false);
                }
                if (fragmentDetalleVenta != null) fragmentDetalleVenta.actualizaDetalleVenta();


                break;

            case cuenta:


                ultimaAccionWs = enAccionesWS.pedirCuenta;
                reintentosWS = 0;
                conectaWS(ultimaAccionWs, null);
                break;

            case pago:
                mostrarFragment(estado);
                if (fragmentFormaPago != null) fragmentFormaPago.actualizaFormaPago();

                break;

            case traspasa:
                ClaseUtils.TraspasaMesa aviso = new ClaseUtils.TraspasaMesa();
                aviso.setTitulo(context.getResources().getString(R.string.alert_strCambioMesa) + " " + mesa.descripcion);
                aviso.setBotonFalse(context.getResources().getString(R.string.strCancelar));
                aviso.setBotonTrue(context.getResources().getString(R.string.strAceptar));
                aviso.setOnclick(onClickTraspasa);
                aviso.setTipo(ClaseUtils.TraspasaMesa.MOVER_MESA);

                aviso.setContext(context);
                aviso.setMesas(listaMesas);
                aviso.setMesaActual(mesa.mesa);
                aviso.execute(0);

                break;
            case traspasa_sub:
                aviso = new ClaseUtils.TraspasaMesa();
                aviso.setTitulo(context.getResources().getString(R.string.alert_strCambioMesa) + " " + mesa.descripcion + " - " + String.valueOf(submesa.submesa));
                aviso.setBotonFalse(context.getResources().getString(R.string.strCancelar));
                aviso.setBotonTrue(context.getResources().getString(R.string.strAceptar));
                aviso.setOnclick(onClickTraspasa);
                aviso.setTipo(ClaseUtils.TraspasaMesa.MOVER_SUBMESA);

                aviso.setContext(context);
                aviso.setMesas(listaMesas);
                aviso.setMesaActual(mesa.mesa);
                aviso.execute(0);

                break;

            case cargafacturas:
                ultimaAccionWs = enAccionesWS.recuperaFacturas;
                reintentosWS = 0;
                conectaWS(ultimaAccionWs, null);
                break;

            case facturas:
                mostrarFragment(estado);
                if (fragmentFacturas != null) fragmentFacturas.actualizaFacturas();
                break;

        }
    }

    private static void mostrarFragment(enEstado estado) {
        layFrgInicio.setVisibility(View.GONE);
        layFrgUsers.setVisibility(View.GONE);
        layFrgTPVs.setVisibility(View.GONE);
        layFrgFacturas.setVisibility(View.GONE);
        layFrgMesas.setVisibility(View.GONE);
        layCompacto.setVisibility(View.GONE);
        layFrgProductos.setVisibility(View.GONE);
        layFrgCabeceras.setVisibility(View.GONE);
        layFrgVentas.setVisibility(View.GONE);
        layFrgFormaPago.setVisibility(View.GONE);


        if (fragmentDetalleVenta != null) fragmentDetalleVenta.estadoFragmentActivo(false);
        switch (estado) {
            case iniciando:
                layFrgInicio.setVisibility(VISIBLE);
                break;
            case user:
                layFrgUsers.setVisibility(VISIBLE);
                break;
            case tpv:
                layFrgTPVs.setVisibility(VISIBLE);
                break;
            case facturas:
                layFrgFacturas.setVisibility(VISIBLE);
                break;
            case mostrarmesas:
                layFrgMesas.setVisibility(VISIBLE);
                break;
            case venta:
                if (configuracion.compacto) {
                    layCompacto.setVisibility(VISIBLE);
                } else {
                    layFrgCabeceras.setVisibility(VISIBLE);
                    layFrgProductos.setVisibility(VISIBLE);
                }


                layFrgVentas.setVisibility(VISIBLE);
                if (fragmentDetalleVenta != null) fragmentDetalleVenta.estadoFragmentActivo(true);
                break;
            case pago:
                layFrgFormaPago.setVisibility(VISIBLE);
                break;

            case traspasa:

                break;

        }

    }

    private void añadeProducto(ClaseProductos producto, boolean hh) {
        boolean duplicado = false;
        String happyHour = "N";
        int uds = 1;
        if (hh && producto.aplicar_hh.trim().toUpperCase().equals("S")) {
            happyHour = "S";
            uds = 2;
        }

        for (ClaseLineaVentas tmpLineaVenta : listaLineaVentas) {

            // busca duplicados
            if (tmpLineaVenta.codmenu == producto.codmenu && tmpLineaVenta.happyhour.equals(happyHour) &&
                    tmpLineaVenta.estado != ClaseUtils.enEstado.eliminar && tmpLineaVenta.estado != ClaseUtils.enEstado.transmitida &&
                    !tmpLineaVenta.tieneExtras() && tmpLineaVenta.tipo.trim().toUpperCase().equals("B")) {

                // solo permito incrementar lineas que no estén marcadas para eliminar, transmitidas o con nota y que sean bebida


                int pos = listaLineaVentas.indexOf(tmpLineaVenta);
                listaLineaVentas.remove(tmpLineaVenta);

                tmpLineaVenta.cantidad += uds;
                tmpLineaVenta.teuros = tmpLineaVenta.teuros + tmpLineaVenta.peuros; // sumo 1 ud aunque sea hh
                tmpLineaVenta.tcoste = tmpLineaVenta.tcoste + tmpLineaVenta.pcoste;

                if (tmpLineaVenta.estado == ClaseUtils.enEstado.transmitida)
                    tmpLineaVenta.estado = ClaseUtils.enEstado.actualizar;
                else if (tmpLineaVenta.estado == ClaseUtils.enEstado.anadir)
                    tmpLineaVenta.estado = ClaseUtils.enEstado.anadir;
                else if (tmpLineaVenta.estado == ClaseUtils.enEstado.actualizar)
                    tmpLineaVenta.estado = ClaseUtils.enEstado.actualizar;

                tmpLineaVenta.imprimir = "S";

                listaLineaVentas.add(pos, tmpLineaVenta);
                duplicado = true;
                lineaSel = pos;
                break;
            }
        }
        if (!duplicado) {
            Double teuros = Double.parseDouble(producto.euros.replace(",", "."));
            String pensionAplicada = "";

            if (submesa.aplicaPension && producto.pensiones.contains("|"+submesa.pension+"|")) {
                teuros = 0.00;
                pensionAplicada = submesa.pension;
            }
            teuros = uds * teuros;

            Double tcoste = Double.parseDouble(producto.costo.replace(",", "."));
            tcoste = uds * tcoste;

            //los tipo ="B" solo pueden ser bebidas en orden_platos
            if (producto.tipo.equalsIgnoreCase("B") ) {
                producto.orden_platos = 0;
            }
            else {
                //los tipo "C" no pueden ser orden_platos 0. esto solo bebidas. pasan a otros o al que esté asignando en ese momento
                if (producto.orden_platos == 0) {
                        producto.orden_platos = ordenSel;
                }
            }

            int tmpOrden = ordenSel;
            if (producto.orden_platos == 0 ) {  // si es bebida se respeta. Las el resto de platos coge la seleccion
                tmpOrden = producto.orden_platos;
            }
            else if(ordenSel == 0 ){ //si estoy tomando bebidas, y el plato es otra cosa mantenine su orden
                tmpOrden = producto.orden_platos;
            }

            int nlinea = 0;
            if (listaLineaVentas.size() > 0) {
                try {
                    nlinea =  listaLineaVentas.stream().max(Comparator.comparing(ClaseLineaVentas::getLinea_app)).get().linea_app;
                    nlinea += 1;
                }
                catch (Exception e){
                    nlinea = 0;
                }


            }

            String camarero = "0";
            if (user != null) {
                camarero = String.valueOf(user.codigo);
            }
            lineaVenta = new ClaseLineaVentas(0, mesaSel, mesa.submesas.get(subMesaSel).submesa, producto.codmenu, uds,
                    Double.parseDouble(producto.euros.replace(",", ".")), teuros, Double.parseDouble(producto.costo.replace(",", ".")), tcoste,
                    producto.descripcion, tpv.codtpv, happyHour, "", ClaseUtils.enEstado.anadir, camarero, producto.tipo, producto.tmenu, ClaseUtils.enEstado.transmitida, producto.mprecios, "", "S", tmpOrden, false, "",0,nlinea, pensionAplicada);
            listaLineaVentas.add(lineaVenta);
            lineaSel = listaLineaVentas.size() - 1;
        }
    }

    public static void cargaUsuariosBD() {
        listaUsers = new ArrayList<>();

        List<Usuarios> usuarios = null;
        try {
            usuarios = baseDatos.appGetRecuperaUsuarios();
            if (usuarios != null && usuarios.size() > 0) {
                for (Usuarios usuario : usuarios) {
                    listaUsers.add(new ClaseUsers(Integer.valueOf(usuario.getCodigo()), usuario.getNombre(), usuario.getClave()));
                }
            }
        } catch (ExecutionException ee) {
        } catch (InterruptedException ie) {
        }
    }

    private static void cargaTpvsBD() {
        listaTPVs = new ArrayList<>();

        List<Tpvs> tpvs = null;
        try {
            tpvs = baseDatos.appGetRecuperaTpvs();
            if (tpvs != null && tpvs.size() > 0) {
                for (Tpvs tpv : tpvs) {
                    listaTPVs.add(new ClaseTPVs(tpv.getCodtpv(), tpv.getDescripcion(), tpv.getTmenu(), tpv.getNumeroMesas()));
                }
            }
        } catch (ExecutionException ee) {
        } catch (InterruptedException ie) {
        }
    }

    private static void cargaNomMesasBD() {
        listaNombreMesas = new TreeMap<>();

        List<Nom_Mesas> nomMesas = null;
        try {
            nomMesas = baseDatos.appGetRecuperaNomMesas();
            if (nomMesas != null && nomMesas.size() > 0) {
                for (Nom_Mesas nomMesa : nomMesas) {
                    listaNombreMesas.put(nomMesa.getCodtpv() + "-" + String.valueOf(nomMesa.getNumero()), nomMesa);
                }
            }
        } catch (ExecutionException ee) {
        } catch (InterruptedException ie) {
        }
    }

    private static void cargaEtiquetas() {
        List<Etiquetas> etiquetas = null;
        try {
            etiquetas = baseDatos.appGetRecuperaEtiquetas();
        } catch (ExecutionException ee) {
        } catch (InterruptedException ie) {
        }
        listaEtiquetas = new ArrayList<>();
        if (etiquetas != null) {
            int items = 0;
            for (Etiquetas etiqueta : etiquetas) {
                try {
                    items = baseDatos.getProductosEtiquetaCount(tpv.tmenu, cabecera.codigo, etiqueta.getCodigo());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    items = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    items = 0;
                }
                etiqueta.setChecked(false);
                if (items > 0)
                    listaEtiquetas.add(etiqueta);
            }
        }
    }


    private static void cargaAlergenos() {
        List<Alergenos> alergenos = null;
        try {
            alergenos = baseDatos.appGetRecuperaAlergenos();
        } catch (ExecutionException ee) {
        } catch (InterruptedException ie) {
        }
        listaAlergenos = new ArrayList<>();
        if (alergenos != null) {
            for (Alergenos alergeno : alergenos) {
                alergeno.setChecked(false);
                listaAlergenos.add(alergeno);
            }
        }
    }

    private static void cargaOrdenPlatos() {
        listaOrdenPlatos = new ArrayList<>();
        listaOrdenPlatos.clear();
        listaOrdenPlatos.add(new OrdenPlatos(0, "BEBIDAS"));
        listaOrdenPlatos.add(new OrdenPlatos(1, "ENTRANTES"));
        listaOrdenPlatos.add(new OrdenPlatos(2, "PRIMEROS"));
        listaOrdenPlatos.add(new OrdenPlatos(3, "SEGUNDOS"));
        listaOrdenPlatos.add(new OrdenPlatos(4, "POSTRES"));
    }


    private static void cargaCabecerasTpvBD(int tmenu) {
        listaCabeceras = new ArrayList<>();

        List<Cabeceras> cabeceras = null;

        List<Integer> color = new ArrayList<>();

        color.add(ContextCompat.getColor(context, R.color.rojo));
        color.add(ContextCompat.getColor(context, R.color.verde));
        color.add(ContextCompat.getColor(context, R.color.textoAzul));
        int cont = 0;

        try {
            cabeceras = baseDatos.getCabecerasTpv(tmenu);
            if (cabeceras != null && cabeceras.size() > 0) {
                for (Cabeceras cabecera : cabeceras) {
                    listaCabeceras.add(new ClaseCabeceras(cabecera.getPos(), cabecera.getDescripcion(), color.get(cont)));
                    cont += 1;
                    if (cont == 3) cont = 0;
                }
            }
        } catch (ExecutionException ee) {
        } catch (InterruptedException ie) {
        }
        listaCabeceras.add(0, new ClaseCabeceras(-1, "TODO", color.get(cont)));
    }


    private static void cargaProductosCabeceraBD(int tmenu, int cabecera) {
        listaProductos = new ArrayList<>();


        List<Productos> productos = null;

        List<Integer> color = new ArrayList<>();
        color.add(ContextCompat.getColor(context, R.color.rojo));
        color.add(ContextCompat.getColor(context, R.color.verde));
        color.add(ContextCompat.getColor(context, R.color.textoAzul));
        int cont = 0;

        try {
            productos = baseDatos.getProductosCabecera(tmenu, cabecera);
            if (productos != null && productos.size() > 0) {
                for (Productos producto : productos) {
                    try {
                        if (producto.getEuros().equalsIgnoreCase("")) {
                            producto.setEuros("0.00");
                        }
                        if (producto.getCosto().equalsIgnoreCase("")) {
                            producto.setCosto("0.00");
                        }

                        listaProductos.add(new ClaseProductos(producto.getCodmenu(), producto.getTmenu(), producto.getCabecera(),
                                producto.getDescripcion(), ClaseUtils.double2string(Double.valueOf(producto.getEuros()), 2), ClaseUtils.double2string(Double.valueOf(producto.getCosto()), 2)
                                , producto.getMprecios(), producto.getAbrevia(), producto.getAplicar_hh(), producto.getTipo(),
                                producto.getAplicar_ti(), color.get(cont), producto.getNotas(), producto.getAlergenos(), producto.getEtiquetas(), Integer.valueOf(producto.getOrden()), Integer.valueOf(producto.getOrden_platos()),
                                producto.getCodfam(), producto.getCodsub(), producto.getEs_extra(), producto.getVer_extra(), producto.getPensiones()));
                        cont += 1;
                        if (cont == 3) cont = 0;
                    } catch (Exception ex) {
                        String a = "";
                    }
                    ;

                }
            }
        } catch (ExecutionException ee) {
        } catch (InterruptedException ie) {
        }

        Collections.sort(listaProductos);
        int a = listaProductos.size();
    }

    private void cargaClientesCtaCasaBD() {
        listaClientesCtaCasa = new ArrayList<>();

        List<ClientesCtaCasa> clientesCtaCasa = null;
        try {
            clientesCtaCasa = baseDatos.appGetRecuperaClientesCtaCasa();
            if (clientesCtaCasa != null && clientesCtaCasa.size() > 0) {
                for (ClientesCtaCasa clienteCtaCasa : clientesCtaCasa) {
                    listaClientesCtaCasa.add(new ClaseClientesCtaCasa(clienteCtaCasa.getCodcli(), clienteCtaCasa.getNombre()));
                }
            }
        } catch (ExecutionException ee) {
        } catch (InterruptedException ie) {
        }
    }



    private static void iniciarConfiguracion() {

        ClaseBluetooth.set_ultimoEstadoBT(estadoBT);
        Intent inten = new Intent(context, ActivityConfig.class);
        ((Activity) context).startActivityForResult(inten, REQUEST_APP_CONFIGURE);
        nofinalizar = true;
    }

    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case ClaseBluetooth.REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Log.i(TAG, "onActivityResult: resultCode==OK");
                    Log.i(TAG, "onActivityResult: starting setupComm()...");
                } else {
                    Log.d(TAG, "onActivityResult: BT not enabled");
                }
                new ClaseBluetooth().enableTimer(10000, true);
                break;

            case REQUEST_APP_CONFIGURE:
                if (resultCode == 1 || resultCode == 2) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (resultCode == 2) {
                                conexionValidada = false;
                                grabaFechaSincronizacion("200101000000");
                            }
                            cargaConfiguracion();


                            Log.v("result mostrarAuto", String.valueOf(configuracion.mostrarProductosAuto));
                            Log.v("result mantener", String.valueOf(configuracion.mantenerProductos));
                            Log.v("result compacto", String.valueOf(configuracion.compacto));

                            Log.v("activityInicio", "Finalizada la carga de base de datos");
                            String endpoint = configuracion.url;
                            if (!endpoint.endsWith("/") && !configuracion.api.startsWith("/")) {
                                endpoint += "/";
                            }

                            endpoint += configuracion.api;

                            if (!endpoint.endsWith("/")) {
                                endpoint += "/";
                            }

                            ServSincronizaBD.setConfig(configuracion);

                            ClaseServicioWeb.setBaseUrl(endpoint);
                            reintentosWS = 0;
                            validarWS(true, true);

                            iniciaListas();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (fragmentCabeceras != null) {
                                        fragmentCabeceras.actualizaCabeceras();
                                        fragmentCabeceras.setTimer(configuracion.timerFragment * 1000);
                                    }
                                    if (fragmentProductos != null) {
                                        fragmentProductos.actualizaProductos(true);
                                        fragmentProductos.setTimer(configuracion.timerFragment * 1000, !configuracion.mantenerProductos);
                                    }
                                    if (fragmentMesas != null) {
                                        fragmentMesas.actualizaMesas(true);
                                    }
                                    if (resultCode == 2) {
                                        if (fragmentUsers != null) fragmentUsers.actualizaUsers();
                                        if (fragmentTPVs != null) fragmentTPVs.actualizaTPVs();
                                        if (fragmentMesas != null)
                                            fragmentMesas.actualizaMesas(true);

                                        ultimoEstado = estado;
                                        estado = enEstado.user;
                                        cambiaEstado(estado);
                                    }
                                    ;
                                }

                                ;
                            }, 300);

                        }

                        ;
                    }, 500);

                }
                ;

                break;
            case FragmentFormaPago.REQUEST_NFC_ENABLE:
                fragmentFormaPago.updateIconNFC();
                break;
        }

        nofinalizar = false;
    }

    private void cambiaVistaDetalle(boolean visible) {


        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) layFrgLineaVentas.getLayoutParams();
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) layFrgDetalleVenta.getLayoutParams();


        if (ClaseUtils.modelo == ClaseUtils.enModel.phone) {

            if (visible) {
                params1.weight = 0;
                params2.weight = 12;
                if (estado == enEstado.venta) {
                    if (layCompacto != null) layCompacto.setVisibility(View.GONE);
                    if (layFrgCabeceras != null) layFrgCabeceras.setVisibility(View.GONE);
                    if (layFrgProductos != null) layFrgProductos.setVisibility(View.GONE);
                }
            } else {
                params1.weight = 12;
                params2.weight = 0;
                if (estado == enEstado.venta) {

                    if (layCompacto != null && configuracion.compacto)
                        layCompacto.setVisibility(VISIBLE);

                    if (layFrgCabeceras != null && !configuracion.compacto)
                        layFrgCabeceras.setVisibility(VISIBLE);
                    if (layFrgCabeceras != null && configuracion.compacto)
                        layFrgCabeceras.setVisibility(View.GONE);

                    if (layFrgProductos != null && !configuracion.compacto)
                        layFrgProductos.setVisibility(VISIBLE);

                }
            }
        } else {
            params1.weight = 7;
            params2.weight = 5;
            if (estado == enEstado.venta) {

                if (layCompacto != null && configuracion.compacto)
                    layCompacto.setVisibility(VISIBLE);
                if (layFrgCabeceras != null && !configuracion.compacto)
                    layFrgCabeceras.setVisibility(VISIBLE);
                if (layFrgCabeceras != null && configuracion.compacto)
                    layFrgCabeceras.setVisibility(View.GONE);

                if (layFrgProductos != null && !configuracion.compacto)
                    layFrgProductos.setVisibility(VISIBLE);
                if (layFrgProductos != null && configuracion.compacto)
                    layFrgProductos.setVisibility(View.GONE);
            }
        }
        layFrgDetalleVenta.setLayoutParams(params2);
        layFrgLineaVentas.setLayoutParams(params1);

    }

    private static void salir() {


        if (estado == enEstado.pago) {
            ClaseUtils.Aviso.mostrarAviso(context.getResources().getString(R.string.alert_strAviso),
                    context.getResources().getString(R.string.alert_strAvisoNoSalir), context);
            return;
        } else if (estado == enEstado.venta || estado == enEstado.cargalineas || estado == enEstado.traspasa || estado == enEstado.traspasa_sub) {
            estadoPendiente = enEstado.cargamesas;
            if (!compruebaCambiosLineaPendientes() && !compruebaLineaPendientesEnvio()) {
                irCargamesas();
            }
            return;
        }

        ClaseUtils.ProgressDialogo.cerrarDialogo();
        ClaseUtils.AvisoResultado aviso = new ClaseUtils.AvisoResultado();
        aviso.setTitulo(context.getResources().getString(R.string.alert_strAviso));
        aviso.setMensaje(context.getResources().getString(R.string.alert_strSalir));
        aviso.setBotonTrue(context.getResources().getString(R.string.strAceptar));
        aviso.setBotonFalse(context.getResources().getString(R.string.strCancelar));
        aviso.setOnclick(dlgOnclickSalir);
        aviso.setContext(context);
        aviso.execute();

    }

    private static void logout() {

        if (navigationMenu.findItem(R.id.nav_logout) != null)
            navigationMenu.findItem(R.id.nav_logout).setVisible(false);
        if (navigationMenu.findItem(R.id.nav_user) != null)
            navigationMenu.findItem(R.id.nav_user).setVisible(true);

        ultimoEstadoApp.setEstado(enEstado.iniciando);
        ultimoEstadoApp.setUsuario(-1);
        ultimoEstadoApp.setTpv(-1);
        guardaUltimoEstadoApp();


        ultimoEstado = estado;
        estado = enEstado.user;
        cambiaEstado(estado);
    }

    private static void irTpv() {
        ultimoEstado = estado;
        estado = enEstado.tpv;
        cambiaEstado(estado);
    }

    private static void irFacturas() {
        ultimoEstado = estado;
        estado = enEstado.cargafacturas;
        cambiaEstado(estado);
    }

    private static void irCargamesas() {
        ultimoEstado = estado;
        estado = enEstado.cargamesas;
        cambiaEstado(estado);
    }

    private static void irTraspasaMesa() {

        ultimoEstado = enEstado.venta;
        estado = enEstado.traspasa;
        cambiaEstado(estado);
    }

    private static void irTraspasaSubMesa() {
        ultimoEstado = enEstado.venta;
        estado = enEstado.traspasa_sub;
        cambiaEstado(estado);
    }


    private static void cambiaSubmesa(int submesaSelPendiente) {
        subMesaSel = -1;
        for (ClaseSubMesas submesa : mesa.submesas) {
            if (submesa.submesa == submesaSelPendiente) {
                subMesaSel = mesa.submesas.indexOf(submesa);
                break;
            }
        }
        if (subMesaSel != -1) {
            submesa = mesa.submesas.get(subMesaSel);
            if (fragmentSubmesas != null) fragmentSubmesas.actualizaSubmesas();
            if (fragmentLineaVentas != null) fragmentLineaVentas.actualizaLineaVentas(false);
            if (fragmentDetalleVenta != null) fragmentDetalleVenta.actualizaDetalleVenta();
            tvBarMesa.setText(mesa.descripcion + "-" + Integer.toString(submesa.submesa));
            tvBarPax.setText(String.valueOf(submesa.pax));
            tvBarRoom.setText(submesa.habitacion);
            muestraPension(submesa);

        }
    }

    private static void cambiaLineaVenta() {
        lineaVenta = listaLineaVentas.get(lineaSel);
        if (fragmentDetalleVenta != null) fragmentDetalleVenta.actualizaDetalleVenta();
        if (fragmentSubmesas != null) fragmentSubmesas.actualizaSubmesas();
    }

    private static void cargaEstadoMesas(Intent intent) {
        double importe;
        int pax;
        ClaseUtils.ProgressDialogo.cerrarDialogo();
        ArrayList<EstadoMesas> mesas = intent.getParcelableArrayListExtra("mesas");

        int umesa = 0;
        int usubmesa = 0;

        for (EstadoMesas estadoMesa : mesas) {
            ClaseMesas mesa = listaMesas.get(estadoMesa.getMesa());
            if (mesa == null) {
                continue;
            }

            importe = 0;
            pax = 0;
            boolean encontrada = false;
            String habitacion = estadoMesa.getHabitacion() == null ? "": estadoMesa.getHabitacion();

            if (estadoMesa.getMesa() != umesa ){
                mesa.pax = 0;
                mesa.importe = 0.0;
                umesa = estadoMesa.getMesa();
                usubmesa = 0;
            }

            if (mesa != null && mesa.submesas != null && mesa.submesas.size() > 0) {
                for (ClaseSubMesas subMesa : mesa.submesas) {


                    if (subMesa.submesa == estadoMesa.getSubmesa()) {
                        if (estadoMesa.getSubmesa() != usubmesa){
                            subMesa.pax = 0;
                            subMesa.importe = 0.0;
                            usubmesa = estadoMesa.getSubmesa();
                        }

                        if (estadoMesa.getEstado().equals("A")) {
                            subMesa.estado = ClaseSubMesas.enEstados.abierta;
                            subMesa.importe += Double.valueOf(estadoMesa.getImporte());
                            subMesa.pax += Integer.valueOf(estadoMesa.getNpax());
                            subMesa.habitacion =habitacion;
                            subMesa.codenl = Integer.valueOf(estadoMesa.getCodenl());
                            subMesa.nfactura = estadoMesa.getNfactura();
                            mesa.abiertas = true;
                            subMesa.horaApertura = estadoMesa.getApertura();
                            importe = Double.valueOf(estadoMesa.getImporte());
                            pax = Integer.valueOf(estadoMesa.getNpax());
                        }
                        if (estadoMesa.getEstado().equals("F")) {
                            subMesa.estado = ClaseSubMesas.enEstados.tiquet;
                            subMesa.importe += Double.valueOf(estadoMesa.getImporte());
                            subMesa.pax += Integer.valueOf(estadoMesa.getNpax());
                            subMesa.habitacion =habitacion;
                            subMesa.codenl = Integer.valueOf(estadoMesa.getCodenl());
                            subMesa.nfactura = estadoMesa.getNfactura();
                            subMesa.horaApertura = estadoMesa.getApertura();
                            mesa.tickets = true;
                            importe = Double.valueOf(estadoMesa.getImporte());
                            pax =Integer.valueOf(estadoMesa.getNpax());
                        }
                        encontrada = true;
                        break;
                    }
                }
            }

            if (!encontrada) {  // la han podido crear en otro terminal

                ClaseSubMesas newSubMesa = new ClaseSubMesas(estadoMesa.getSubmesa(), ClaseSubMesas.enEstados.abierta, false, estadoMesa.getNpax(), estadoMesa.getCodenl(), estadoMesa.getHabitacion(), estadoMesa.getPension(),"","","", false, estadoMesa.getApertura(),"","");

                if (estadoMesa.getEstado().equals("A")) {
                    newSubMesa.estado = ClaseSubMesas.enEstados.abierta;
                    newSubMesa.importe = Double.valueOf(estadoMesa.getImporte());
                    newSubMesa.pax = Integer.valueOf(estadoMesa.getNpax());
                    newSubMesa.codenl = Integer.valueOf(estadoMesa.getCodenl());
                    newSubMesa.nfactura = estadoMesa.getNfactura();
                    newSubMesa.habitacion = habitacion;
                    newSubMesa.horaApertura = estadoMesa.getApertura();
                    mesa.abiertas = true;
                    importe = Double.valueOf(estadoMesa.getImporte());
                    pax = Integer.valueOf(estadoMesa.getNpax());
                }
                if (estadoMesa.getEstado().equals("F")) {
                    newSubMesa.estado = ClaseSubMesas.enEstados.tiquet;
                    newSubMesa.importe = Double.valueOf(estadoMesa.getImporte());
                    newSubMesa.pax = Integer.valueOf(estadoMesa.getNpax());
                    newSubMesa.habitacion = habitacion;
                    newSubMesa.codenl = Integer.valueOf(estadoMesa.getCodenl());
                    newSubMesa.nfactura = estadoMesa.getNfactura();
                    newSubMesa.horaApertura = estadoMesa.getApertura();
                    mesa.tickets = true;
                    importe = Double.valueOf(estadoMesa.getImporte());
                    pax =Integer.valueOf(estadoMesa.getNpax());

                }

                if (mesa != null && mesa.submesas != null)
                    mesa.submesas.add(newSubMesa);
            }
            mesa.nSubmesas = mesa.submesas.size();
            mesa.importe += importe;
            mesa.pax += pax;

        }
        return;
    }


    private static void cargaLineasVenta(Intent intent) {
        ClaseUtils.ProgressDialogo.cerrarDialogo();

        String horaApertura = intent.getStringExtra("hora_apertura");
        ArrayList<LineasVenta> lineas = intent.getParcelableArrayListExtra("lineas");
        ArrayList<PensionesSubmesa> pensiones = intent.getParcelableArrayListExtra("pensiones");


        for (ClaseSubMesas submesa : mesa.submesas) {
            String nfactura = "";
            String tpension = "";
            String tDescPension = "";
            String tTipoPension = "";
            String tDescTipoPension = "";
            String tHoraApertua = "";

            ArrayList<ClaseLineaVentas> lineasSubmesa = new ArrayList<>();
            for (LineasVenta linea : lineas) {
                if (linea.getSubmesa() == submesa.submesa) {

                    //al recuperar las lineas, cantidad anterior = cantidad.
                    ClaseLineaVentas lineaSubmesa = LineaVentaMapper.fromRest(linea, linea.getCantidad());

                    lineasSubmesa.add(lineaSubmesa);
                    nfactura = lineaSubmesa.nfactura;
                }
            }

            for (PensionesSubmesa pension: pensiones) {
                if (pension.getSubmesa() == submesa.submesa) {
                    tpension = pension.getPension();
                    tDescPension = pension.getDescPension();
                    tTipoPension = pension.getTipoPension();
                    tDescTipoPension = pension.getDescTipoPension();
                    tHoraApertua = pension.getHora_apertura();
                    break;
                }
            }

            submesa.lineasVentas = lineasSubmesa;
            submesa.nfactura = nfactura;
            submesa.pension = tpension;
            submesa.descPension = tDescPension;
            submesa.tipoPension = tTipoPension;
            submesa.descTipoPension = tDescTipoPension;
            if (tHoraApertua != null) submesa.horaApertura = tHoraApertua;

            calculaAplicaPension(submesa);

        }

    }

    private static void cargaFacturas(Intent intent) {
        ClaseUtils.ProgressDialogo.cerrarDialogo();
        listaFacturas = intent.getParcelableArrayListExtra("facturas");
        if (listaFacturas != null) {
            for (ClaseFacturas factura : listaFacturas) {
                String desc;
                try {
                    desc = listaNombreMesas.get(String.valueOf(tpv.codtpv) + "-" + String.valueOf(factura.mesa)).getDescripcion();
                } catch (Exception e) {
                    desc = "";
                }
                if (desc != null && desc.trim().length() > 0)
                    factura.descripcion = desc;
            }
        }
        ClaseFacturas.orden = ClaseFacturas.enOrden.factura;
        ClaseFacturas.asc = false;
        int a = 0;

    }

    private static boolean procesaLineasVenta(Intent intent) {
        ClaseUtils.ProgressDialogo.cerrarDialogo();
        ArrayList<ResGrabaLineas> lineas = intent.getParcelableArrayListExtra("resultados");
        boolean hayError = true;

        for (ClaseSubMesas submesa : mesa.submesas) {
            submesa.errorTransmision = false;

            for (int t = 0; t < submesa.lineasVentas.size(); t++) {
                if (submesa.lineasVentas.get(t).estado != ClaseUtils.enEstado.transmitida) {
                    for (ResGrabaLineas linea : lineas) {
                        if (linea.getNlinea() == t) {
                            if (linea.getError() == 0) {
                                submesa.lineasVentas.get(t).estado = ClaseUtils.enEstado.transmitida;
                                submesa.lineasVentas.get(t).resultadoEnvio = ClaseUtils.enEstado.transmitida;
                            } else {
                                submesa.errorTransmision = true;
                                submesa.lineasVentas.get(t).resultadoEnvio = ClaseUtils.enEstado.error;
                                hayError = false;
                            }
                        }
                    }
                }
            }
        }

        return hayError;
    }

    private static boolean compruebaCambiosLineaPendientes() {
        if (fragmentDetalleVenta != null && fragmentDetalleVenta.isCambiosPendientes()) {
            ClaseUtils.ProgressDialogo.cerrarDialogo();
            ClaseUtils.AvisoResultado aviso = new ClaseUtils.AvisoResultado();
            aviso.setTitulo(context.getResources().getString(R.string.alert_strAviso));
            aviso.setMensaje(context.getResources().getString(R.string.alert_strCambiosPendientes));
            aviso.setBotonTrue(context.getResources().getString(R.string.strSi));
            aviso.setBotonFalse(context.getResources().getString(R.string.strNo));
            if (estado == enEstado.traspasa || estado == enEstado.traspasa_sub) {
                aviso.setMensaje(context.getResources().getString(R.string.alert_strNoTraspasoMesa));
                aviso.setBotonTrue("");
                aviso.setBotonFalse(context.getResources().getString(R.string.strAceptar));
            }
            aviso.setOnclick(dlgOnclickDescartarCambiosLinea);
            aviso.setContext(context);
            aviso.execute();
            return true;
        }
        return false;

    }

    private static boolean compruebaLineaPendientesEnvio() {
        if (getLineasPendientesEnvio()) {
            ClaseUtils.ProgressDialogo.cerrarDialogo();
            ClaseUtils.AvisoResultado aviso = new ClaseUtils.AvisoResultado();
            aviso.setTitulo(context.getResources().getString(R.string.alert_strAviso));
            aviso.setMensaje(context.getResources().getString(R.string.alert_strLineasPendienteEnvio));
            aviso.setBotonTrue(context.getResources().getString(R.string.strSi));
            aviso.setBotonFalse(context.getResources().getString(R.string.strNo));
            if (estado == enEstado.traspasa || estado == enEstado.traspasa_sub) {
                aviso.setMensaje(context.getResources().getString(R.string.alert_strNoTraspasoMesa));
                aviso.setBotonTrue("");
                aviso.setBotonFalse(context.getResources().getString(R.string.strAceptar));
            }
            aviso.setOnclick(dlgOnclickDescartarCambiosLinea);
            aviso.setContext(context);
            aviso.execute();
            return true;
        }
        return false;

    }

    private static boolean compruebaMesaVacia() {
        if (getLineasMesa() < 1) {
            ClaseUtils.ProgressDialogo.cerrarDialogo();
            ClaseUtils.AvisoResultado aviso = new ClaseUtils.AvisoResultado();
            aviso.setTitulo(context.getResources().getString(R.string.alert_strAviso));
            aviso.setMensaje(context.getResources().getString(R.string.alert_strNoTraspasoMesa_vacia));
            aviso.setBotonTrue("");
            aviso.setBotonFalse(context.getResources().getString(R.string.strAceptar));
            aviso.setOnclick(dlgOnclickDescartarCambiosLinea);
            aviso.setContext(context);
            aviso.execute();
            return true;
        }
        return false;

    }

    private static boolean compruebaSubmesaVacia() {
        if (getLineasSubmesa() < 1) {
            ClaseUtils.ProgressDialogo.cerrarDialogo();
            ClaseUtils.AvisoResultado aviso = new ClaseUtils.AvisoResultado();
            aviso.setTitulo(context.getResources().getString(R.string.alert_strAviso));
            aviso.setMensaje(context.getResources().getString(R.string.alert_strNoTraspasoSubMesa_vacia));
            aviso.setBotonTrue("");
            aviso.setBotonFalse(context.getResources().getString(R.string.strAceptar));
            aviso.setOnclick(dlgOnclickDescartarCambiosLinea);
            aviso.setContext(context);
            aviso.execute();
            return true;
        }
        return false;

    }


    private static boolean getLineasPendientesEnvio() {
        if (mesa != null && mesa.submesas != null) {
            for (ClaseSubMesas tSubMesas : mesa.submesas) {
                for (ClaseLineaVentas linea : tSubMesas.lineasVentas) {
                    if (linea.estado == ClaseUtils.enEstado.anadir || linea.estado == ClaseUtils.enEstado.actualizar ||
                            linea.estado == ClaseUtils.enEstado.eliminar) {
                        return true;
                    }

                }
            }
        }
        return false;
    }

    private static int getLineasMesa() {
        if (mesa != null) {
            int lineas = 0;
            for (ClaseSubMesas submesa : mesa.submesas) {
                lineas += submesa.lineasVentas.size();
            }
            return lineas;
        } else
            return 0;
    }

    private static int getLineasSubmesa() {
        if (submesa != null) {
            return submesa.lineasVentas.size();
        } else
            return 0;
    }

    static DialogInterface.OnClickListener dlgOnclickTiquetCuenta = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            ultimoEstado = enEstado.venta;
            listaMesas.get(mesaSel).submesas.get(subMesaSel).estado = ClaseSubMesas.enEstados.tiquet;
            fragmentSubmesas.actualizaSubmesas();
            if (configuracion.mantenerVenta == true) {
                estado = enEstado.cargalineas;
            } else {// else
                estado = enEstado.cargamesas;
            }
            cambiaEstado(estado);
        }
    };

    static DialogInterface.OnClickListener dlgOnclickTiquetPagar= new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            ultimoEstado = enEstado.venta;
            listaMesas.get(mesaSel).submesas.get(subMesaSel).estado = ClaseSubMesas.enEstados.tiquet;
            fragmentSubmesas.actualizaSubmesas();
            if (configuracion.mantenerVenta == true) {
                estado = enEstado.cargalineas;
            } else {// else
                estado = enEstado.cargamesas;
            }
            cambiaEstado(estado);
        }
    };


    static  DialogInterface.OnClickListener dlgOnclickSalir = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE || which == DialogInterface.BUTTON_NEUTRAL) {

                ultimoEstadoApp = new ClaseUltimoEstadoApp();
                guardaUltimoEstadoApp();

                finalizarApp();
            }
        }
    };

    static DialogInterface.OnClickListener dlgOnclickErrorWs = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                reintentosWS = 0;
                switch (ultimaAccionWs) {
                    case test:
                    case validar:
                    case sincronizar:
                    case sincronizarbg:
                    case getEstadoMesas:
                    case recuperaFacturas:
                    case getLineasMesa:
                    case pedirCuenta:
                        conectaWS(ultimaAccionWs, null);
                        break;
                    case actualizaPax:
                        conectaWS(ultimaAccionWs, argActualizaPax);
                        break;
                    case traspasaMesa:
                        conectaWS(ultimaAccionWs, argTraspasaMesa);
                        break;
                    case enviaLineasMesa:
                        conectaWS(ultimaAccionWs, argEnviaLineasMesa);
                        break;
                    case siguePlato:
                        conectaWS(ultimaAccionWs, argSiguePlato);
                        break;
                    case creditoTagID:
                        conectaWS(ultimaAccionWs, argCreditoTagID);
                        break;
                    case creditoRoom:
                        conectaWS(ultimaAccionWs, argCreditoRoom);
                        break;
                    case pagaMesa:
                        conectaWS(ultimaAccionWs, argPagaMesa);
                        break;
                    case recuperaFactura:
                        conectaWS(ultimaAccionWs, argRecuperaFactura);
                        break;
                    case imprimeFactura:
                        conectaWS(ultimaAccionWs, argImprimeFactura);
                        break;
                }
            } else {
                if (ultimaAccionWs.equals(enAccionesWS.sincronizar) || ultimaAccionWs.equals(enAccionesWS.sincronizarbg)) {
                    layProgress.setVisibility(View.GONE);
                } else if (ultimaAccionWs.equals(enAccionesWS.creditoRoom) || ultimaAccionWs.equals(enAccionesWS.creditoTagID)) {
                    if (fragmentFormaPago != null) {
                        Intent intent = new Intent();
                        intent.putExtra("errnum", "1");
                        fragmentFormaPago.cargaCredito(intent, false);
                    }
                }
                hSyncDatos.postDelayed(checkConexion, 2000);
            }
        }
    };

    static DialogInterface.OnClickListener dlgOnclickDescartarCambiosLinea = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                switch (estadoPendiente) {
                    case salir:
                        salir();
                        break;
                    case cerrar:
                        logout();
                        break;
                    case tpv:
                        irTpv();
                        break;
                    case cargamesas:
                        irCargamesas();
                        break;
                    case venta:
                        if (lineaSelPendiente > -1) {
                            lineaSel = lineaSelPendiente;
                            cambiaLineaVenta();
                        }
                        if (submesaSelPendiente > -1) {
                            cambiaSubmesa(submesaSelPendiente);

                        }
                        break;
                }
            } else {
                // boton neutro cuando no puedo traspasar mesa. me quedo en venta
                estado = enEstado.venta;
                if (fragmentLineaVentas != null) fragmentLineaVentas.actualizaLineaVentas(false);
                if (fragmentSubmesas != null) fragmentSubmesas.actualizaSubmesas();
            }

        }
    };


    static DialogInterface.OnClickListener dlgOnClickSiguePlato = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == DialogInterface.BUTTON_POSITIVE) {
                String strAviso = context.getResources().getString(R.string.progress_strAvisoSiguePlato);
                switch (ClaseUtils.SiguePlatos.getOrden()) {
                    case 0:
                        strAviso = strAviso.replace("[orden]", "'Bebidas'");
                        break;
                    case 1:
                        strAviso = strAviso.replace("[orden]", "'Entrantes'");
                        break;
                    case 2:
                        strAviso = strAviso.replace("[orden]", "'Primeros'");
                        break;
                    case 3:
                        strAviso = strAviso.replace("[orden]", "'Segundos'");
                        break;
                    case 4:
                        strAviso = strAviso.replace("[orden]", "'Postres'");
                        break;

                }

                ultimaAccionWs = enAccionesWS.siguePlato;

                argSiguePlato = ArgSiguePlato.getINSTANCE();
                argSiguePlato.setAviso(strAviso);
                reintentosWS = 0;
                conectaWS(ultimaAccionWs, argSiguePlato);

            } else {
            }
        }
    };

    static DialogInterface.OnClickListener dlgOnClickNada = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
        }
    };

    static DialogInterface.OnClickListener dlgOnClickPidePax = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                int pax, paxmover;
                pax = ClaseUtils.PidePaxSubmesa.getPaxAdd();
                paxmover = ClaseUtils.PidePaxSubmesa.getPaxMove();
                String room = ClaseUtils.PidePaxSubmesa.getRoom();

                if (pax == 0 && paxmover == 0) { // CANCELAR. NO SE HAN SELECCIONADO PAX
                    pax = 0;
                    for (ClaseSubMesas submesa : listaMesas.get(mesaSel).submesas) {
                        if (submesa.paxTraspaso > 0) {
                            submesa.pax += submesa.paxTraspaso;
                            submesa.paxTraspaso = 0;
                        }
                        pax += submesa.pax;
                    }
                } else {
                    boolean actualizar = !ClaseUtils.PidePaxSubmesa.isMover_uds();
                    añadirSubmesaPax(pax + paxmover, actualizar,room,"");

                    pax = 0;
                    for (ClaseSubMesas submesa : listaMesas.get(mesaSel).submesas) {
                        pax += submesa.pax;
                    }
                    if (fragmentDetalleVenta != null && ClaseUtils.PidePaxSubmesa.isMover_uds()) {
                        fragmentDetalleVenta.mover_a_Submesa();
                    }
                }
                listaMesas.get(mesaSel).pax = pax; // totaliza pax en la mesa
                subMesaSel = mesa.submesas.size()-1;
                submesa = mesa.submesas.get(subMesaSel);

                tvBarPax.setText(String.valueOf(submesa.pax));
                tvBarRoom.setText(submesa.habitacion);
                muestraPension(submesa);


                //SOLICITAR PENSIÓN DE ESTA SUBMESA
                if (! ClaseUtils.VerTiquet.isVisible()) {
                    ClaseUtils.ProgressDialogo.mostrarDialogo(true, context.getResources().getString(R.string.progress_strRecuperaPension), context.getResources().getString(R.string.progress_strEspera), context);
                }
                try {
                    ServSincronizaBD.startActionGetPension(context, configuracion.codigo, configuracion.deviceID,submesa.habitacion,String.valueOf(submesa.codenl), handlerNotificaWS, (Activity) context);
                } catch (Exception ex) {
                    ClaseUtils.ProgressDialogo.cerrarDialogo();
                    ClaseUtils.AvisoToast.mostrarAviso("Aviso",context.getResources().getText(R.string.strErrorRecuperaPension).toString(),context,2000);

                }

            } else { //boton cancelar
                int pax = 0;
                for (ClaseSubMesas submesa : listaMesas.get(mesaSel).submesas) {
                    if (submesa.paxTraspaso > 0) {
                        submesa.pax += submesa.paxTraspaso;
                        submesa.paxTraspaso = 0;
                    }
                    pax += submesa.pax;

                }
                listaMesas.get(mesaSel).pax = pax; // totaliza pax en la mesa
                tvBarPax.setText(String.valueOf(submesa.pax));
                tvBarRoom.setText(submesa.habitacion);
                muestraPension(submesa);

            }
        }
    };

    static DialogInterface.OnClickListener dlgOnClickPideRoom = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                String room = ClaseUtils.PideRoom.getRoom();
                submesa.habitacion = room;
                tvBarRoom.setText(submesa.habitacion);
                //SOLICITAR PENSIÓN DE ESTA SUBMESA
                if (! ClaseUtils.VerTiquet.isVisible()) {
                    ClaseUtils.ProgressDialogo.mostrarDialogo(true, context.getResources().getString(R.string.progress_strRecuperaPension), context.getResources().getString(R.string.progress_strEspera), context);
                }

                try {

                    recalculaPension = true;
                    ServSincronizaBD.startActionGetPension(context, configuracion.codigo, configuracion.deviceID,submesa.habitacion, String.valueOf(submesa.codenl), handlerNotificaWS, (Activity) context);
                } catch (Exception ex) {
                    recalculaPension = false;
                    ClaseUtils.ProgressDialogo.cerrarDialogo();
                    ClaseUtils.AvisoToast.mostrarAviso("Aviso",context.getResources().getText(R.string.strErrorRecuperaPension).toString(),context,2000);

                }

            }
        }
    };


    static DialogInterface.OnClickListener dlgOnClickPideClave = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //if (which == DialogInterface.BUTTON_POSITIVE) {
            String clave = ClaseUtils.PideClave.getClave().toUpperCase().trim();
            String codigo = ClaseUtils.PideClave.getCodigo().toUpperCase().trim();
            int sum1 = 0;
            int sum2 = 0;
            for (int t = 0; t < codigo.length(); t++) {
                if ((t % 2) == 0) {
                    sum1 += Integer.valueOf(codigo.substring(t, t + 1));
                } else {
                    sum2 += Integer.valueOf(codigo.substring(t, t + 1));
                }
            }
            int sum3 = 2 * (sum1 + sum2);
            String ok = String.valueOf(sum1) + String.valueOf(sum2) + String.valueOf(sum3);
            if (ok.equals(clave)) {
                iniciarConfiguracion();
            } else {
                ClaseUtils.Aviso.mostrarAviso(context.getResources().getString(R.string.alert_strAviso),
                        context.getResources().getString(R.string.dialog_strErrorCodigoAcceso), context);
                return;

            }
            //}
        }
    };


    static private void añadirSubmesaPax(int pax, boolean actualizaSel, String habitacion, String pension) {
        mesa.nSubmesas += 1;
        String horaApertura = ClaseUtils.now("HH:mm:ss");
        mesa.submesas.add(new ClaseSubMesas(mesa.nSubmesas, ClaseSubMesas.enEstados.abierta, false, pax, 0, habitacion, pension,"","","", false, horaApertura,"",""));
        if (actualizaSel) {
            subMesaSel = mesa.submesas.size() - 1;
            submesa = mesa.submesas.get(subMesaSel);
            if (fragmentLineaVentas != null)
                fragmentLineaVentas.actualizaLineaVentas(false);
            if (fragmentDetalleVenta != null)
                fragmentDetalleVenta.actualizaDetalleVenta();
        }
        if (fragmentSubmesas != null) fragmentSubmesas.actualizaSubmesas();
        tvBarMesa.setText(mesa.descripcion + "-" + Integer.toString(submesa.submesa));
        tvBarPax.setText(Integer.toString(submesa.pax));
        tvBarRoom.setText(submesa.habitacion);
        muestraPension(submesa);

    }


    DialogInterface.OnClickListener onClickOrganizaPax = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {

                int pax = 0;
                for (ClaseSubMesas submesa : listaMesas.get(mesaSel).submesas) {
                    pax += submesa.pax;
                }

                tvBarPax.setText(String.valueOf(submesa.pax));
                tvBarRoom.setText(submesa.habitacion);
                muestraPension(submesa);

                listaMesas.get(mesaSel).pax = pax; // totaliza pax en la mesa
                actualizaPax(listaMesas.get(mesaSel).submesas, listaMesas.get(mesaSel).mesa);

            } else {
                int pax = 0;
                for (ClaseSubMesas submesa : listaMesas.get(mesaSel).submesas) {
                    submesa.pax += submesa.paxTraspaso;
                    submesa.paxTraspaso = 0;
                    pax += submesa.pax;
                }
                listaMesas.get(mesaSel).pax = pax; // totaliza pax en la mesa
            }
        }
    };

    static DialogInterface.OnClickListener onClickTraspasa = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                ClaseUtils.ProgressDialogo.mostrarDialogo(true, context.getResources().getString(R.string.progress_strTraspasaMesa), context.getResources().getString(R.string.progress_strEspera), context);
                ArrayList<String> submesas = new ArrayList<>();
                if (ClaseUtils.TraspasaMesa.getTipo() == ClaseUtils.TraspasaMesa.MOVER_MESA) {
                    for (ClaseSubMesas submesa : mesa.submesas) {
                        submesas.add(String.valueOf(submesa.submesa));
                    }
                } else {
                    submesas.add(String.valueOf(mesa.submesas.get(subMesaSel).submesa));
                }
                traspasaMesa_mesasel = ClaseUtils.TraspasaMesa.getMesaSel();

                argTraspasaMesa = ArgTraspasaMesa.getINSTANCE();
                argTraspasaMesa.setSubmesas(submesas);

                ultimaAccionWs = enAccionesWS.traspasaMesa;
                reintentosWS = 0;
                conectaWS(ultimaAccionWs, argTraspasaMesa);

            } else {
                estado = estadoPendiente;
                if (fragmentLineaVentas != null) fragmentLineaVentas.actualizaLineaVentas(false);
                if (fragmentSubmesas != null) fragmentSubmesas.actualizaSubmesas();


            }
        }
    };


//region ARGUMENTOS FUNCIONES SERVICIO WEB ONLNE

    public static class ArgActualizaPax {

        private static ArgActualizaPax INSTANCE;

        public static ArgActualizaPax getINSTANCE() {
            if (INSTANCE == null) {
                synchronized (ArgActualizaPax.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new ArgActualizaPax();
                    }
                }
            }
            return INSTANCE;
        }

        public ArgActualizaPax() {
        }

        ArrayList<ActualizaPax> submesasPax;

        public ArrayList<ActualizaPax> getSubmesasPax() {
            return submesasPax;
        }

        public void setSubmesasPax(ArrayList<ActualizaPax> submesasPax) {
            this.submesasPax = submesasPax;
        }

    }

    public static class ArgTraspasaMesa {
        private static ArgTraspasaMesa INSTANCE;

        public static ArgTraspasaMesa getINSTANCE() {
            if (INSTANCE == null) {
                synchronized (ArgTraspasaMesa.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new ArgTraspasaMesa();
                    }
                }
            }
            return INSTANCE;
        }

        public ArgTraspasaMesa() {
        }

        ArrayList<String> submesas;

        public ArrayList<String> getSubmesas() {
            return submesas;
        }

        public void setSubmesas(ArrayList<String> submesas) {
            this.submesas = submesas;
        }

    }

    public static class ArgEnviaLineasMesa {
        private static ArgEnviaLineasMesa INSTANCE;

        public static ArgEnviaLineasMesa getINSTANCE() {
            if (INSTANCE == null) {
                synchronized (ArgEnviaLineasMesa.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new ArgEnviaLineasMesa();
                    }
                }
            }
            return INSTANCE;
        }

        public ArgEnviaLineasMesa() {
        }

        ArrayList<GrabaLineasVenta> lineasVentas;
        ArrayList<ActualizaPax> submesas;

        public ArrayList<GrabaLineasVenta> getLineasVentas() {
            return lineasVentas;
        }

        public void setLineasVentas(ArrayList<GrabaLineasVenta> lineasVentas) {
            this.lineasVentas = lineasVentas;
        }

        public ArrayList<ActualizaPax> getSubmesas() {
            return submesas;
        }

        public void setSubmesas(ArrayList<ActualizaPax> submesas) {
            this.submesas = submesas;
        }

    }

    public static class ArgSiguePlato {
        private static ArgSiguePlato INSTANCE;

        public static ArgSiguePlato getINSTANCE() {
            if (INSTANCE == null) {
                synchronized (ArgSiguePlato.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new ArgSiguePlato();
                    }
                }
            }
            return INSTANCE;
        }

        public ArgSiguePlato() {
        }

        String aviso;

        public String getAviso() {
            return aviso;
        }

        public void setAviso(String aviso) {
            this.aviso = aviso;
        }

    }

    public static class ArgCreditoTagID {
        private static ArgCreditoTagID INSTANCE;

        public static ArgCreditoTagID getINSTANCE() {
            if (INSTANCE == null) {
                synchronized (ArgCreditoTagID.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new ArgCreditoTagID();
                    }
                }
            }
            return INSTANCE;
        }

        public ArgCreditoTagID() {
        }

        String tagID;
        String codemp;

        public String getTagID() {
            return tagID;
        }

        public void setTagID(String tagID) {
            this.tagID = tagID;
        }

        public String getCodemp() {
            return codemp;
        }

        public void setCodemp(String codemp) {
            this.codemp = codemp;
        }
    }

    public static class ArgCreditoRoom {
        private static ArgCreditoRoom INSTANCE;

        public static ArgCreditoRoom getINSTANCE() {
            if (INSTANCE == null) {
                synchronized (ArgCreditoRoom.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new ArgCreditoRoom();
                    }
                }
            }
            return INSTANCE;
        }

        public ArgCreditoRoom() {
        }

        String room;
        String codemp;

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        public String getCodemp() {
            return codemp;
        }

        public void setCodemp(String codemp) {
            this.codemp = codemp;
        }
    }

    public static class ArgPagaMesa {
        private static ArgPagaMesa INSTANCE;

        public static ArgPagaMesa getINSTANCE() {
            if (INSTANCE == null) {
                synchronized (ArgPagaMesa.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new ArgPagaMesa();
                    }
                }
            }
            return INSTANCE;
        }

        public ArgPagaMesa() {
        }

        String codemp_ext;
        String nreserva;
        String formapago;
        String importe;
        String igic;

        String efectivo;
        String entregado;
        String cambio;
        String tarjeta;
        String cuentacasa;
        String credito;

        String codclicasa;

        String nfactura;

        String codusu;

        String firma;
        String apto;
        String imprimir;

        public String getCodemp_ext() {
            return codemp_ext;
        }

        public void setCodemp_ext(String codemp_ext) {
            this.codemp_ext = codemp_ext;
        }

        public String getNreserva() {
            return nreserva;
        }

        public void setNreserva(String nreserva) {
            this.nreserva = nreserva;
        }

        public String getFormapago() {
            return formapago;
        }

        public void setFormapago(String formapago) {
            this.formapago = formapago;
        }

        public String getImporte() {
            return importe;
        }

        public void setImporte(String importe) {
            this.importe = importe;
        }

        public String getIgic() {
            return igic;
        }

        public void setIgic(String igic) {
            this.igic = igic;
        }

        public String getEfectivo() {
            return efectivo;
        }

        public void setEfectivo(String efectivo) {
            this.efectivo = efectivo;
        }

        public String getEntregado() {
            return entregado;
        }

        public void setEntregado(String entregado) {
            this.entregado = entregado;
        }

        public String getCambio() {
            return cambio;
        }

        public void setCambio(String cambio) {
            this.cambio = cambio;
        }

        public String getTarjeta() {
            return tarjeta;
        }

        public void setTarjeta(String tarjeta) {
            this.tarjeta = tarjeta;
        }

        public String getCuentacasa() {
            return cuentacasa;
        }

        public void setCuentacasa(String cuentacasa) {
            this.cuentacasa = cuentacasa;
        }

        public String getCredito() {
            return credito;
        }

        public void setCredito(String credito) {
            this.credito = credito;
        }

        public String getCodclicasa() {
            return codclicasa;
        }

        public void setCodclicasa(String codclicasa) {
            this.codclicasa = codclicasa;
        }

        public String getNfactura() {
            return nfactura;
        }

        public void setNfactura(String nfactura) {
            this.nfactura = nfactura;
        }

        public String getCodusu() {
            return codusu;
        }

        public void setCodusu(String codusu) {
            this.codusu = codusu;
        }

        public String getFirma() {
            return firma;
        }

        public void setFirma(String firma) {
            this.firma = firma;
        }

        public String getApto() {
            return apto;
        }

        public void setApto(String apto) {
            this.apto = apto;
        }

        public String getImprimir() {
            return imprimir;
        }

        public void setImprimir(String imprimir) {
            this.imprimir = imprimir;
        }
    }

    public static class ArgRecuperaFactura {
        private static ArgRecuperaFactura INSTANCE;

        public static ArgRecuperaFactura getINSTANCE() {
            if (INSTANCE == null) {
                synchronized (ArgRecuperaFactura.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new ArgRecuperaFactura();
                    }
                }
            }
            return INSTANCE;
        }

        public ArgRecuperaFactura() {
        }

        int codenl;

        public int getCodenl() {
            return codenl;
        }

        public void setCodenl(int codenl) {
            this.codenl = codenl;
        }

    }

    public static class ArgImprimeFactura {
        private static ArgImprimeFactura INSTANCE;

        public static ArgImprimeFactura getINSTANCE() {
            if (INSTANCE == null) {
                synchronized (ArgImprimeFactura.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new ArgImprimeFactura();
                    }
                }
            }
            return INSTANCE;
        }

        public ArgImprimeFactura() {
        }

        String codenl;
        String aviso;

        public String getCodenl() {
            return codenl;
        }

        public void setCodenl(String codenl) {
            this.codenl = codenl;
        }

        public String getAviso() {
            return aviso;
        }

        public void setAviso(String aviso) {
            this.aviso = aviso;
        }

    }

//endregion

    //region SHARED PREFERENCES - CARGA TABLAS SINCRONIZAR

    private static void recuperaUltimoEstadoApp() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("estado_app", context.MODE_PRIVATE);
        String ultimo_estado = sharedPreferences.getString("ultimo_estado", "");
        if (ultimo_estado.equals("")){
            ultimoEstadoApp = new ClaseUltimoEstadoApp();
        }
        else {
            try {
                ultimoEstadoApp = new Gson().fromJson(ultimo_estado,ClaseUltimoEstadoApp.class);
            }
            catch (Exception ex){
                ultimoEstadoApp = new ClaseUltimoEstadoApp();
            }
        }
    }

    private static void guardaUltimoEstadoApp() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("estado_app", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String ultimo_estado = new Gson().toJson(ultimoEstadoApp);
        editor.putString("ultimo_estado", ultimo_estado);
        editor.commit();
    }


    private static void grabaFechaSincronizacion(String fecha) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sincronizacion", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ultimasincronizacion", fecha);
        editor.commit();
    }

    private static String recuperaaFechaSincronizacion() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sincronizacion", context.MODE_PRIVATE);
        String ultimaSync = sharedPreferences.getString("ultimasincronizacion", "220101000000");
        return ultimaSync;
    }

    private static boolean hayQueSincronizar() {
        String ultimaSync = recuperaaFechaSincronizacion();
        String actual = ClaseUtils.now("yyMMddHHmmss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss");
        try {
            Date dUltima = simpleDateFormat.parse(ultimaSync);
            Date dActual = simpleDateFormat.parse(actual);
            long diferencia = dActual.getTime() - dUltima.getTime();
            long horas = TimeUnit.MILLISECONDS.toHours(diferencia);
            if (horas > 3) {
                return true;
            }
            return false;

        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }

    }

    public static void cargaTablasSync() {
        tablasSync = new ArrayList<>();
        tablasSync.clear();
        boolean actualiza = false;


        if (hayQueSincronizar()) {

            updateUsers = false;
            updateTPVS = false;

            tablasSync.add("usuarios");
            tablasSync.add("tpvs");
            tablasSync.add("alergenos");
            tablasSync.add("etiquetas");
            tablasSync.add("clientesctacasa");
            tablasSync.add("familias");
            tablasSync.add("subfamilias");
            tablasSync.add("hora_comidas");
        }
    }

    public static void cargaTablasSyncBackGround() {

        tablasSyncBackGround = new ArrayList<>();
        tablasSyncBackGround.clear();
        if (hayQueSincronizar()) {
            tablasSyncBackGround.add("nom_mesas");
            tablasSyncBackGround.add("cabeceras");
            tablasSyncBackGround.add("productos");

            updateNomMesas = false;

        }

    }


//endregion

//region PRINTER


    private static void setIconoBT(int status, String estadoError) {

        if (toolBarMenu != null) {
            switch (status) {
                case ClaseBluetoothPrintConstantes.STATE_CONNECTED:
                    new ClaseBluetooth().enableTimer(30000, false);
                    if (estadoError.trim().equalsIgnoreCase("")) {
                        toolBarMenu.findItem(R.id.action_printer).setIcon(R.drawable.bar_printer_verde);
                    } else {
                        toolBarMenu.findItem(R.id.action_printer).setIcon(R.drawable.bar_printer_aviso);
                    }
                    break;
                case ClaseBluetoothPrintConstantes.STATE_CONNECTING:
                    toolBarMenu.findItem(R.id.action_printer).setIcon(R.drawable.bar_printer_azul);
                    break;
                case ClaseBluetoothPrintConstantes.STATE_DISCONNECTING:
                    toolBarMenu.findItem(R.id.action_printer).setIcon(R.drawable.bar_printer_azul);
                    break;
                case ClaseBluetoothPrintConstantes.STATE_LISTEN:
                    toolBarMenu.findItem(R.id.action_printer).setIcon(R.drawable.bar_printer_gris);
                    break;
                case ClaseBluetoothPrintConstantes.STATE_IDLE:
                    toolBarMenu.findItem(R.id.action_printer).setIcon(R.drawable.bar_printer_gris);
                    break;
                case ClaseBluetoothPrintConstantes.STATE_DISCONNECTED:
                    new ClaseBluetooth().enableTimer(10000, false);
                    toolBarMenu.findItem(R.id.action_printer).setIcon(R.drawable.bar_printer_gris);
                    break;
                case ClaseBluetoothPrintConstantes.STATE_CONNECTED_SOCKET:
                    new ClaseBluetooth().enableTimer(10000, false);
                    toolBarMenu.findItem(R.id.action_printer).setIcon(R.drawable.bar_printer_amarilla);
                    break;
            }
        }
    }

    private static Handler handlerNotificaBT = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case ClaseBluetoothMsgTypes.MESSAGE_STATE_CHANGE:
                    Bundle bundle = msg.getData();
                    int status = bundle.getInt("state");
                    estadoBT = status;
                    setIconoBT(status, ClaseBluetooth.getPrinterError());
                    break;
                case ClaseBluetoothMsgTypes.MESSAGE_SEND_STATE:
                    setIconoBT(ClaseBluetooth.checkPrinterStatus(), ClaseBluetooth.getPrinterError());
                    break;
                case ClaseBluetoothMsgTypes.MESSAGE_READ:
                    if (msg.arg1 == 1) {
                        String estado = "BT:";
                        if (ClaseBluetooth.isEstadoPapel()) estado += " P";
                        else estado += " NP";
                        if (ClaseBluetooth.getEstadoBateriaLow()) estado += " LB";
                        else estado += "";
                        estado += " " + String.valueOf(ClaseBluetooth.getEstadoNivelBateria()) + "V";
                        if (ClaseBluetooth.isEstadoOverheat()) estado += " OH";
                        else estado += "";
                    }
                    break;

                case ClaseBluetoothMsgTypes.MESSAGE_REQUEST_ENABLE_BT:
                    if (boolOnCreate) {
                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        ((Activity) context).startActivityForResult(enableIntent, ClaseBluetooth.REQUEST_ENABLE_BT);
                        nofinalizar = true;
                    } else {
                        if (ClaseBluetooth.get_modelo() != MODEL_SCREEN && !ClaseBluetooth.getBlueToothAdapter().isEnabled()) {

                            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                ClaseUtils.Aviso.mostrarAviso("Aviso...",context.getResources().getText(R.string.strConsultaAvisoPermisoBluetooth).toString(),context);
                                return;
                            }
                            ClaseBluetooth.getBlueToothAdapter().enable();
                        }
                        new ClaseBluetooth().enableTimer(2000, false);
                    }
                    break;
            }
        }
    };

    private static void conectarSocketImpresora() {
        // conectar la impresora
        if (ClaseBluetooth.checkPrinterStatus() == ClaseBluetoothPrintConstantes.STATE_CONNECTED){
            int intentos = 10;
            try {
                do {
                    ClaseBluetooth.conectarSocketBT();
                    Thread.sleep(200);
                    if (ClaseBluetooth.checkPrinterStatus() == ClaseBluetoothPrintConstantes.STATE_CONNECTED_SOCKET) {
                        Log.d(TAG, "Intentos: " + String.valueOf(intentos));
                        intentos = 0;
                    }
                    else
                        intentos--;

                } while (intentos > 0);
            }
            catch(InterruptedException e){}

            SystemClock.sleep(500);
        }
    }

       private void testPrint() {
        conectarSocketImpresora();

        if (ClaseBluetooth.checkPrinterConectado() == false){
            String texto = getResources().getString(R.string.strVentasAvisoImpresoraNoDisponible).replace("\\n","\n");
            String txtCen = getResources().getString(R.string.strAceptar);
            ClaseUtils.Aviso.mostrarAviso("Error",texto,context);
            return;
        }

        if (!ClaseBluetooth.getPrinterError().equalsIgnoreCase("")) {
            String texto = "Error de impresora '"+ClaseBluetooth.getPrinterError()+"'";
            String txtCen = getResources().getString(R.string.strAceptar);
            ClaseUtils.Aviso.mostrarAviso("Error",texto,context);
            return;
        }

        ClaseBluetooth.set_imprimiendo(true);
        ClaseBluetooth.imprimeTiquetTest(context);
    }
    private static void imprimirComanda(ArrayList<GrabaLineasVenta> lineasVentas, String rooms) {

        conectarSocketImpresora();

        if (ClaseBluetooth.checkPrinterConectado() == false){
            String texto = context.getResources().getString(R.string.strVentasAvisoImpresoraNoDisponible).replace("\\n","\n");
            String txtCen = context.getResources().getString(R.string.strAceptar);
            ClaseUtils.Aviso.mostrarAviso("Error",texto,context);
            return;
        }

        if (!ClaseBluetooth.getPrinterError().equalsIgnoreCase("")) {
            String texto = "Error de impresora '"+ClaseBluetooth.getPrinterError()+"'";
            String txtCen = context.getResources().getString(R.string.strAceptar);
            ClaseUtils.Aviso.mostrarAviso("Error",texto,context);
            return;
        }

        ClaseBluetooth.set_imprimiendo(true);
        CabeceraTiquet cabeceraTiquet = new CabeceraTiquet();
        cabeceraTiquet.setRazon("PRINCIPADO S.L.");
        cabeceraTiquet.setCif("B35073311");
        cabeceraTiquet.setTPV(tpv.descripcion);
        cabeceraTiquet.setHora(ClaseUtils.now("dd/MM/yyyy HH:mm:ss"));
        cabeceraTiquet.setMesa( mesa.descripcion.length() == 0? String.valueOf(mesa.mesa):mesa.descripcion);
        if (configuracion.pax) {
            cabeceraTiquet.setPax(String.valueOf(mesa.pax));
        }
        cabeceraTiquet.setApto(rooms);

        ClaseBluetooth.imprimeComanda(cabeceraTiquet, lineasVentas);

        if (ClaseBluetooth.get_modelo() == MODEL_SCREEN) {
            String texto = ClaseBluetooth.getBTdriverSCREEN().getBufferTexto();

            ClaseUtils.VerTiquet aviso = new ClaseUtils.VerTiquet();
            aviso.setTitulo("Comanda");
            aviso.setContext(context);
            aviso.setMensaje(texto);
            aviso.execute(0);

        }

    }

    private static void imprimirCuenta(CabeceraTiquet cabeceraTiquet) {

        if (
            (cabeceraTiquet.getTipoTiquet().equals(ClaseUtils.enTipoTiquet.cuenta) && configuracion.cuentapteapp) ||
            (cabeceraTiquet.getTipoTiquet().equals(ClaseUtils.enTipoTiquet.cuenta) && configuracion.cuentapteapp) ||
            (cabeceraTiquet.getTipoTiquet().equals(ClaseUtils.enTipoTiquet.tiquet) && configuracion.cuentaapp) ||
            (cabeceraTiquet.getTipoTiquet().equals(ClaseUtils.enTipoTiquet.copia_tiquet) && configuracion.cuentaapp)

        ){

            conectarSocketImpresora();

            if (ClaseBluetooth.checkPrinterConectado() == false) {
                String texto = context.getResources().getString(R.string.strVentasAvisoImpresoraNoDisponible).replace("\\n", "\n");
                String txtCen = context.getResources().getString(R.string.strAceptar);
                ClaseUtils.Aviso.mostrarAviso("Error", texto, context);
                return;
            }

            if (!ClaseBluetooth.getPrinterError().equalsIgnoreCase("")) {
                String texto = "Error de impresora '" + ClaseBluetooth.getPrinterError() + "'";
                String txtCen = context.getResources().getString(R.string.strAceptar);
                ClaseUtils.Aviso.mostrarAviso("Error", texto, context);
                return;
            }

            ClaseBluetooth.imprimeCuenta(cabeceraTiquet, submesa, user);
            if (ClaseBluetooth.get_modelo() == MODEL_SCREEN) {
                String texto = ClaseBluetooth.getBTdriverSCREEN().getBufferTexto();

                ClaseUtils.VerTiquet aviso = new ClaseUtils.VerTiquet();
                aviso.setTitulo("Cuenta");
                aviso.setContext(context);
                aviso.setMensaje(texto);
                aviso.execute(0);

            }
        }
    }

    private static void imprimirCopiaCuenta(String empresa, String cif,String tpv, String mesa,String camarero,ClaseFacturaCabecera cabecera, ArrayList<ClaseLineaVentas> lineasVentas) {

        if (configuracion.cuentaapp){

            conectarSocketImpresora();

            if (ClaseBluetooth.checkPrinterConectado() == false) {
                String texto = context.getResources().getString(R.string.strVentasAvisoImpresoraNoDisponible).replace("\\n", "\n");
                String txtCen = context.getResources().getString(R.string.strAceptar);
                ClaseUtils.Aviso.mostrarAviso("Error", texto, context);
                return;
            }

            if (!ClaseBluetooth.getPrinterError().equalsIgnoreCase("")) {
                String texto = "Error de impresora '" + ClaseBluetooth.getPrinterError() + "'";
                String txtCen = context.getResources().getString(R.string.strAceptar);
                ClaseUtils.Aviso.mostrarAviso("Error", texto, context);
                return;
            }

            ClaseBluetooth.imprimeCopiaCuenta(empresa,cif,tpv,mesa,camarero,cabecera,lineasVentas);
            if (ClaseBluetooth.get_modelo() == MODEL_SCREEN) {
                String texto = ClaseBluetooth.getBTdriverSCREEN().getBufferTexto();

                ClaseUtils.VerTiquet aviso = new ClaseUtils.VerTiquet();
                aviso.setTitulo("Cuenta");
                aviso.setContext(context);
                aviso.setMensaje(texto);
                aviso.execute(0);
            }

        }
    }

    //endregion


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            byte[] a = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            int b = a.length;
        }
    }

    private static  void muestraPension(ClaseSubMesas submesa) {
        ivAplicaPension.setImageResource(R.drawable.pension_no);
        tvBarPension.setText("");
        ivAplicaPension.setVisibility(INVISIBLE);
        tvBarPension.setVisibility(INVISIBLE);
        if (submesa == null || submesa.pension.trim().equalsIgnoreCase("")) {
            return;
        }

        ivAplicaPension.setVisibility(VISIBLE);
        tvBarPension.setVisibility(VISIBLE);
        tvBarPension.setText(submesa.pension);

        if ( submesa.aplicaPension){
            switch (submesa.tipoPensionAplicada.toUpperCase()) {
                case "D":
                    ivAplicaPension.setImageResource(R.drawable.pension_desayuno);
                    break;
                case "A":
                    ivAplicaPension.setImageResource(R.drawable.pension_almuerzo);
                    break;
                case "C":
                    ivAplicaPension.setImageResource(R.drawable.pension_cena);
                    break;
            }
        }


    }

    private static void calculaAplicaPension(ClaseSubMesas submesa) {
        List<Hora_Comidas> horarios = null;
        try {
            horarios = baseDatos.getHoraComidasTPV(Integer.valueOf(tpv.codtpv));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        submesa.aplicaPension = false;
        submesa.tipoPensionAplicada = "";
        submesa.horarioPensionAplicado = "";
        if (horarios != null) {
            for (Hora_Comidas horario : horarios) {
                if (submesa.tipoPension.contains(horario.getTipo())) {
                    if (submesa.horaApertura.compareTo(horario.getDesde_hora())> 0  && submesa.horaApertura.compareTo(horario.getHasta_hora()) <0) {
                        submesa.aplicaPension = true;
                        submesa.tipoPensionAplicada = horario.getTipo();
                        submesa.horarioPensionAplicado = horario.getDesde_hora() + " - "+ horario.getHasta_hora();
                        break;
                    }
                }
            }
        }

    }

    private static void recalculaPension(ClaseSubMesas submesa) {
        boolean hayCambios = false;
        for ( ClaseLineaVentas lineaVentas :submesa.lineasVentas) {
            String tpension = lineaVentas.pension;
            lineaVentas.pension = "";
            lineaVentas.teuros = lineaVentas.peuros * lineaVentas.cantidad;

            try {
                    if (lineaVentas.tmenu < 1 ) lineaVentas.tmenu = tpv.tmenu;
                    Productos producto = baseDatos.getProducto(lineaVentas.codmenu,lineaVentas.tmenu);

                    if (producto != null && producto.getPensiones() != null &&  submesa.pension != null && producto.getPensiones().contains("|"+submesa.pension+"|")) {
                        lineaVentas.pension = submesa.pension;
                        lineaVentas.teuros = 0.00;
                }
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (!lineaVentas.pension.equalsIgnoreCase(tpension)){
                hayCambios = true;
                if (lineaVentas.estado == ClaseUtils.enEstado.transmitida) {
                    lineaVentas.estado = ClaseUtils.enEstado.actualizar;
                    lineaVentas.imprimir = "N";
                }

            }
        }

        if (hayCambios) {
            if (fragmentLineaVentas != null) fragmentLineaVentas.actualizaLineaVentas(true);
            if (fragmentDetalleVenta != null) fragmentDetalleVenta.actualizaDetalleVenta();
            if (fragmentSubmesas != null) fragmentSubmesas.actualizaSubmesas();

        }

    }
}



