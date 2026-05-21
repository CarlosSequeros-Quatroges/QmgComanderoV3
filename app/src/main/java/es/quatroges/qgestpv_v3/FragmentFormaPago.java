 package es.quatroges.qgestpv_v3;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.View.generateViewId;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import es.quatroges.qgestpv_v3.configuracion.ClaseCondicionesVenta;
import es.quatroges.qgestpv_v3.datos.listas.clientesCtaCasa.ClaseClientesCtaCasa;
import es.quatroges.qgestpv_v3.datos.listas.clientesCtaCasa.RvAdapterClientesCtaCasa;
import es.quatroges.qgestpv_v3.datos.listas.establecimientos.ClaseEstablecimientos;
import es.quatroges.qgestpv_v3.datos.listas.establecimientos.RvAdapterEstablecimientos;
import es.quatroges.qgestpv_v3.nfc.ClaseNFC;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public class FragmentFormaPago extends Fragment {

    private enum  enFormaPago {
        efectivo, tarjeta, cuenta, credito
    }

    FragmentActivity fragmentActivity;
    private static Context context;

    private static ArrayList<ClaseClientesCtaCasa> listaClientesCtaCasa;
    private static int clienteCtaCasaSel;
    private static RecyclerView rvClientesCtaCasa;
    private RvAdapterClientesCtaCasa adapterClientesCtaCasa;
    private static ArrayList<ClaseEstablecimientos> listaEstablecimientos;
    private static int  establecimientoSel;
    private static RecyclerView  rvEstablecimientos;
    private RvAdapterEstablecimientos adapterEstablecimientos;
    private static double importe, coste , igic , base, efectivo, cambio , tarjeta, cuenta, credito;
    private static String  room;
    private static String pension;
    private static boolean efetar, hayDatosPension, okCredito, okSalida;
    private static String tipo_cuentacasa;

    private static CheckBox chEfectivo,chTarjeta,chCuentaCasa,chCredito;
    private static ImageView ivAceptar, ivCancelar, ivBuscaRoom,ivOkSalida,ivOkCredito, ivNfc;
    private static EditText etRoom;
    private static TextView tvImporte,tvTotalCobrar, tvDatRoom,tvDatServicio,tvDatTurno,tvDatNombre,tvDatFechaSalida, tvDatPtePago,tvDatCredito, tvDatDisponible,tvCambio, tvIgic;
    private EditText etEfectivo, etTarjeta, etCuenta, etCredito;

    private LinearLayout layCuentaCasa, layChkCredito,layCredito1, layDatos, layEfectivo,layTarjeta,layCuenta,layCredito, layEstablecimiento;


    private RotateAnimation rotate;

    private static TextWatcher tw;

    private static ClaseNFC nfc;
    private static TagReceiver tagReceiver;
    private static Handler hNFC;
    private static Animation nfcAnimation;
    public static final int REQUEST_NFC_ENABLE = 101;

    private static String nreserva;
    private static String codemp_ext;
    private static String habitacion;
    private static int codEstable;
    private static  ClaseUtils.PideFirma firma;

    public int getClienteCtaCasaSel() {
        return clienteCtaCasaSel;
    }

    public void setClienteCtaCasaSel(int clienteCtaCasaSel) {
        this.clienteCtaCasaSel = clienteCtaCasaSel;
        calculaTotal(false);
    }

    public int getEstablecimientoSel() {
        return establecimientoSel;
    }

    public void setEstablecimientoSel(int establecimientoSel) {
        this.establecimientoSel = establecimientoSel;
    }



    public void actualizaFormaPago() {
        if (interfaceFormaPago != null){
            habilitaBuscar(true);

            Bundle bundle = interfaceFormaPago.cargaClientesCtaCasa();

            Bundle bundleEstable = interfaceFormaPago.cargaEstablecimientos();

            Bundle bundlePago = interfaceFormaPago.cargaFormasPago();

            clienteCtaCasaSel = bundle.getInt("clientesCtaCasaSel",0);
            listaClientesCtaCasa= bundle.getParcelableArrayList("clientesCtaCasa");

            establecimientoSel =0;
            listaEstablecimientos = bundleEstable.getParcelableArrayList("establecimientos");


            importe = interfaceFormaPago.cargaImporte();
            coste = interfaceFormaPago.cargaCoste();
            room = interfaceFormaPago.cargaRoom();
            pension = interfaceFormaPago.cargaPension();
            hayDatosPension = false;
            okCredito = false;
            okSalida = false;

            if (ivNfc != null){
                setIconNFC(false);
                nfc.disableRead();
            }


            tipo_cuentacasa = ClaseCondicionesVenta.tipo_cuentacasa;

            calculaIgic(importe);

            rvClientesCtaCasa.setAdapter(null);
            View view  = View.inflate(context,R.layout.clientes_cta_casa_cardview,null);
            view.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            float vpWidth = view.getMeasuredWidth()/displayMetrics.density;
            int cols = 3;
            if (ClaseUtils.modelo == ClaseUtils.enModel.phone)
                cols = 2;

            GridLayoutManager glmClientesCtaCasa = new GridLayoutManager(context,cols, GridLayoutManager.VERTICAL, false);
            rvClientesCtaCasa.setLayoutManager(glmClientesCtaCasa);
            adapterClientesCtaCasa = new RvAdapterClientesCtaCasa(context, listaClientesCtaCasa, this) ;
            rvClientesCtaCasa.setAdapter(adapterClientesCtaCasa);



            rvEstablecimientos.setAdapter(null);
            View view1  = View.inflate(context,R.layout.establecimientos_cardview,null);
            view1.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

            GridLayoutManager glmEstable = new GridLayoutManager(context,cols, GridLayoutManager.VERTICAL, false);
            rvEstablecimientos.setLayoutManager(glmEstable);
            adapterEstablecimientos = new RvAdapterEstablecimientos(context, listaEstablecimientos, this) ;
            rvEstablecimientos.setAdapter(adapterEstablecimientos);


            iniciaFormaPago(bundlePago);

            if (room != null && ! room.equals("") && !room.equals("EXT")){
                etRoom.setText(room);

                ivAceptar.setAlpha((float) 0.2);
                ivAceptar.setEnabled(false);

                if (ivNfc != null){
                    setIconNFC(false);
                    nfc.disableRead();
                }

                ivBuscaRoom.setBackground(context.getDrawable(R.drawable.sync));

                buscaDatosHabitacion();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etRoom.getWindowToken(), 0);
            }

            habilitaBuscar(true);

        }
    }

    public void cargaCredito(Intent intent, boolean resultado){

        Log.d("FragmentFormaPago","CargaCredito "+ String.valueOf(resultado));
        habilitaBuscar(true);

        int errnum = Integer.valueOf(intent.getStringExtra("errnum"));

        if (resultado || (errnum != 1 && errnum != 1000 && errnum != 1001)) {


            //  0  OK
            //  1 error conecta base datos
            /// 1000  tarjeta no encontrada
            //  1001  reserva fuera de estancia
            //  1002  fecha y hora de cierre excedido
            //  1003  checkin no realizado
            //  1004  checkout no realizado


            String errdesc = intent.getStringExtra("errdesc");
            habitacion = intent.getStringExtra("habitacion");
            if (habitacion == null || habitacion.equalsIgnoreCase("")) habitacion = room;
            String nombre = intent.getStringExtra("nombre");
            String salida = intent.getStringExtra("salida");
            okSalida = intent.getBooleanExtra("okSalida", false);
            String sCredito = intent.getStringExtra("credito");
            String pendientePago = intent.getStringExtra("pendientePago");
            okCredito = false;

            nreserva = intent.getStringExtra("nreserva");
            codemp_ext = intent.getStringExtra("codemp_ext");
            String pension =  intent.getStringExtra("pension");
            String servicio =  intent.getStringExtra("servicio");

            tvDatRoom.setText("Habitación: " + habitacion);

            tvDatServicio.setVisibility(VISIBLE);
            if (pension.equalsIgnoreCase("SOLO ALOJAMIENTO")) {
                tvDatServicio.setVisibility(GONE);
            }
            tvDatServicio.setText("Pensión: " + servicio);
            tvDatTurno.setText("Tipo: " + pension);




            tvDatNombre.setText("Nombre: " + nombre);
            tvDatFechaSalida.setText("Salida: " + salida);
            ivOkSalida.setBackground(context.getDrawable(R.drawable.cancelar));
            if (okSalida)
                ivOkSalida.setBackground(context.getDrawable(R.drawable.aceptar));

            tvDatPtePago.setText("Pendiente de pago: " + pendientePago + " €");
            tvDatCredito.setText("Crédito: " + sCredito + " €");

            double dCredito = Double.parseDouble(sCredito);
            double dPendientePago = Double.parseDouble(pendientePago);
            double dDisponible = dCredito - dPendientePago;
            if (dDisponible < 0.0)
                dDisponible = 0.0;
            tvDatDisponible.setText("Disponible: " + ClaseUtils.double2string(dDisponible, 2) + " €");

            ivOkCredito.setBackground(context.getDrawable(R.drawable.cancelar));
            if (dDisponible > credito) {
                ivOkCredito.setBackground(context.getDrawable(R.drawable.aceptar));
                okCredito = true;
            }

            ivAceptar.setAlpha((float) 0.2);
            ivAceptar.setEnabled(false);

            ivBuscaRoom.setBackground(context.getDrawable(R.drawable.sync_error));
            if (okSalida && okCredito) {
                ivBuscaRoom.setBackground(context.getDrawable(R.drawable.sync_ok));

                if (chCredito.isChecked()) {
                    ivAceptar.setAlpha((float) 1.0);
                    ivAceptar.setEnabled(true);
                }
            }

            switch (errnum){
                case 0:
                case 1002:
                case 1003:
                case 1004:
                    layDatos.setVisibility(View.VISIBLE);
                    break;
                default:
                    layDatos.setVisibility(GONE);
            }

            if (! okCredito) {
                ClaseUtils.Aviso.mostrarAviso(getResources().getString(R.string.alert_strError),
                        getResources().getString(R.string.alert_strErrorCreditoDipsonible), context);

            }
            hayDatosPension = true;
        }
        else {
            layDatos.setVisibility(GONE);
            hayDatosPension = false;
        }

        rotate.cancel();
        rotate.reset();
    }

    public void updateIconNFC() {
        setIconNFC(false);
    }


    public interface InterfaceFormaPago {
        Bundle cargaClientesCtaCasa();
        Bundle cargaEstablecimientos();
        double cargaImporte();
        double cargaCoste();
        Bundle cargaFormasPago();
        void onClickClientesCtaCasa(int i);
        void onClickAccionPagar(boolean pagar, Bundle datos);
        void recuperaCreditoTagID(String tagID, String estabecimiento);
        void recuperaCreditoRoom(String Room, String establecimiento);
        String cargaRoom();
        String cargaPension();
    }


    public static InterfaceFormaPago interfaceFormaPago;


    public FragmentFormaPago newInstance() {
        FragmentFormaPago formaPagoFragment = new FragmentFormaPago();
        return formaPagoFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.fragmentActivity = (FragmentActivity)context;
            this.context = context;
            interfaceFormaPago = (InterfaceFormaPago) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.forma_pago_fragment,container,false);

        nfc = new ClaseNFC(context,(Activity) context);

        chEfectivo = rootView.findViewById(R.id.chEfectivo);
        chTarjeta = rootView.findViewById(R.id.chTarjeta);
        chCuentaCasa = rootView.findViewById(R.id.chCuenta);
        chCredito = rootView.findViewById(R.id.chCredito);

        layEfectivo = rootView.findViewById(R.id.layEfectivo);
        layTarjeta = rootView.findViewById(R.id.layTarjeta);
        layCuenta = rootView.findViewById(R.id.layCuenta);
        layCredito = rootView.findViewById(R.id.layCredito);

        layCuentaCasa = rootView.findViewById(R.id.layCuentaCasa);
        layEstablecimiento = rootView.findViewById(R.id.layEstablecimiento);
        layChkCredito = rootView.findViewById(R.id.layChkCredito);
        layCredito1 = rootView.findViewById(R.id.layCredito1);
        layDatos = rootView.findViewById(R.id.layDatos);

        ivAceptar = rootView.findViewById(R.id.ivAceptar);
        ivCancelar = rootView.findViewById(R.id.ivCancelar);
        ivBuscaRoom = rootView.findViewById(R.id.ivBuscaRoom);
        ivNfc = rootView.findViewById(R.id.ivNFC);
        ivOkSalida = rootView.findViewById(R.id.ivOkSalida);
        ivOkCredito = rootView.findViewById(R.id.ivOkCredito);

        tvImporte = rootView.findViewById(R.id.tvImporte);tvIgic = rootView.findViewById(R.id.tvIgic);
        tvTotalCobrar = rootView.findViewById(R.id.tvTotalCobrar);

        etEfectivo = rootView.findViewById(R.id.etEfectivo);
        etTarjeta = rootView.findViewById(R.id.etTarjeta);
        etCuenta = rootView.findViewById(R.id.etCuenta);
        etCredito = rootView.findViewById(R.id.etCredito);

        tvCambio = rootView.findViewById(R.id.tvCambio);

        etRoom = rootView.findViewById(R.id.etRoom);

        tvDatRoom = rootView.findViewById(R.id.tvDatRoom);
        tvDatServicio = rootView.findViewById(R.id.tvDatServicio);
        tvDatTurno = rootView.findViewById(R.id.tvDatTurno);
        tvDatNombre = rootView.findViewById(R.id.tvDatNombre);
        tvDatFechaSalida = rootView.findViewById(R.id.tvDatSalida);
        tvDatPtePago = rootView.findViewById(R.id.tvDatPtePago);
        tvDatCredito = rootView.findViewById(R.id.tvDatLimiteCredito);
        tvDatDisponible = rootView.findViewById(R.id.tvDatDisponible);

        rotate = new RotateAnimation(0, 1800, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(10000);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());


        nfcAnimation = AnimationUtils.loadAnimation(context.getApplicationContext(),R.anim.blink);



        chEfectivo.setChecked(false);
        chTarjeta.setChecked(false);
        chCuentaCasa.setChecked(false);
        chCredito.setChecked(false);
        ivAceptar.setEnabled(false);
        ivAceptar.setAlpha((float)0.3);

        tvImporte.setText("Importe: 0.00 €");
        tvIgic.setText("Igic: 0.00 €");
        tvTotalCobrar.setText("Total a cobrar: 0.00 €");

        etEfectivo.setText("0.00");
        etTarjeta.setText("0.00");
        etCuenta.setText("0.00");
        etCredito.setText("0.00");


        etRoom.setText("");
        tvDatRoom.setText("");
        tvDatServicio.setText("");
        tvDatTurno.setText("");

        layChkCredito.setVisibility(GONE);
        layCuentaCasa.setVisibility(GONE);
        layEstablecimiento.setVisibility(GONE);
        layDatos.setVisibility(GONE);


        rvClientesCtaCasa = rootView. findViewById(R.id.rvClientesCtaCasa);
        rvEstablecimientos = rootView. findViewById(R.id.rvEstablecimientos);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int cols = (int) dpWidth /200;

        GridLayoutManager glmClientesCtaCasa = new GridLayoutManager(context,cols, GridLayoutManager.VERTICAL, false);
        rvClientesCtaCasa.setLayoutManager(glmClientesCtaCasa);

        Bundle bundle = interfaceFormaPago.cargaClientesCtaCasa ();
        clienteCtaCasaSel = bundle.getInt("clienteCtaCasaSel",0);
        listaClientesCtaCasa= bundle.getParcelableArrayList("clientesCtaCasa");

        adapterClientesCtaCasa = new RvAdapterClientesCtaCasa(context, listaClientesCtaCasa, this) ;
        rvClientesCtaCasa.setAdapter(adapterClientesCtaCasa);

        GridLayoutManager glmEstablecimientos = new GridLayoutManager(context,cols, GridLayoutManager.VERTICAL, false);
        rvEstablecimientos.setLayoutManager(glmEstablecimientos);

        Bundle bundleEstable = interfaceFormaPago.cargaEstablecimientos ();
        establecimientoSel = bundleEstable.getInt("establecimientoSel",0);
        listaEstablecimientos= bundleEstable.getParcelableArrayList("establecimientos");

        adapterEstablecimientos = new RvAdapterEstablecimientos(context, listaEstablecimientos, this) ;
        rvEstablecimientos.setAdapter(adapterEstablecimientos);


        ivBuscaRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pension != null && !pension.equalsIgnoreCase("")) return;

                ivAceptar.setAlpha((float) 0.2);
                ivAceptar.setEnabled(false);

                setIconNFC(false);
                ivBuscaRoom.setBackground(context.getDrawable(R.drawable.sync));

                buscaDatosHabitacion();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etRoom.getWindowToken(), 0);
            }
        });

        ivNfc.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (pension != null && !pension.equalsIgnoreCase("")) return;


                if (nfc.hasNFC() == 2) {
                    habilitaBuscar(false);
                    ivAceptar.setAlpha((float) 0.2);
                    ivAceptar.setEnabled(false);

                    ivBuscaRoom.setBackground(context.getDrawable(R.drawable.sync));


                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction(ClaseNFC.NFC_TAG_READED);

                    if (tagReceiver != null ) {
                        try {
                            context.unregisterReceiver(tagReceiver);
                        }
                        catch (Exception e){}
                        tagReceiver = null;
                    }
                    tagReceiver = new TagReceiver();
                    //context.registerReceiver(tagReceiver, intentFilter);
                    ContextCompat.registerReceiver(context, tagReceiver, intentFilter, ContextCompat.RECEIVER_NOT_EXPORTED);

                    nfc.enableRead();
                    ivNfc.setBackground(context.getDrawable(R.drawable.nfc_read));
                    animateNFC(true);
                    nfc.setNfcReading(true);
                    hNFC = new Handler();
                    hNFC.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            habilitaBuscar(true);
                            animateNFC(false);
                            nfc.setNfcReading(false);
                            try {
                                nfc.disableRead();
                            }
                            catch (Exception e){}
                            setIconNFC(false);
                            nfc.clearTagID();
                        }
                    }, 5000);

                }
                else {
                    if (nfc.hasNFC() == 1){

                        ClaseUtils.AvisoResultado aviso = new ClaseUtils.AvisoResultado();
                        aviso.setTitulo(context.getResources().getString(R.string.dialog_strTituloActivarNFC));
                        aviso.setMensaje(context.getResources().getString(R.string.dialog_strMensajeActivarNFC));
                        aviso.setBotonTrue(context.getResources().getString(R.string.strAceptar));
                        aviso.setBotonFalse(context.getResources().getString(R.string.strCancelar));
                        aviso.setOnclick(onClickPideNFC);
                        aviso.setContext(context);
                        aviso.execute();

                    }


                    habilitaBuscar(true);
                    setIconNFC(false);
                }
                 etRoom.setText("");
                layDatos.setVisibility(GONE);

             }
         }
        );

        ivCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceFormaPago.onClickAccionPagar(false, null);
                nfc.disableRead();
                try {
                    context.unregisterReceiver(tagReceiver);
                }
                catch (Exception e){}

            }
        });

        ivAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClaseCondicionesVenta.pedir_firma.toUpperCase().equals("S") && ( chCredito.isChecked() || chCuentaCasa.isChecked()) ) {
                    firma = new ClaseUtils.PideFirma();

                    firma.setTitulo(context.getResources().getString(R.string.dialog_strTituloFirma));
                    firma.setAceptarOnclick(onClickAceptaFirma);
                    firma.setCancelarOnclick(onClickCancelaFirma);
                    firma.setFecha( ClaseUtils.now("dd MMM yyy"));
                    firma.setHabitacion(tvDatRoom.getText().toString());
                    firma.setNombre(tvDatNombre.getText().toString());
                    firma.setImporte(ClaseUtils.double2string(importe, 2).replace(",", "."));

                    firma.setContext(context);
                    firma.execute();
                }
                else
                    pagar_credito("");
            }
        });

        etRoom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getId() == R.id.etPax && hasFocus && v.getVisibility() == VISIBLE){
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(etRoom, InputMethodManager.SHOW_IMPLICIT);
                }
                else {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etRoom.getWindowToken(), 0);
                }
            }
        });

        etRoom.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    buscaDatosHabitacion();
                    return true;
                }
                return false;
            }
        });

        //activaEventos();

        return rootView;

    }


    private void actualizaImporte(double importe){
        calculaIgic(importe);
        tvIgic.setText("Igic ("+ClaseUtils.double2string(ClaseCondicionesVenta.igic,2)+"% de "+
                ClaseUtils.double2string(base,2)+"€) "+ ClaseUtils.double2string(igic,2)+"€.");


        tvImporte.setText("Total: "+ ClaseUtils.double2string(base+igic,2)+"€.");

    }

    private void calculaIgic (double importe){
        switch (ClaseCondicionesVenta.tipo_igic.toUpperCase()){
            case "A":
                base = importe;
                igic = new BigDecimal(base* ClaseCondicionesVenta.igic/100).setScale(2,RoundingMode.HALF_UP).doubleValue();
                break;
            case "I":
            case "D":
                base =new BigDecimal(importe *100/(100+ ClaseCondicionesVenta.igic )).setScale(2,RoundingMode.HALF_UP).doubleValue();
                igic = importe - base;
                break;
            case "E":
                base = importe;
                igic = 0;
                break;
        }
    }

    private void buscaDatosHabitacion(){
        layDatos.setVisibility(GONE);
        if (! etRoom.getText().toString().trim().equals("")){
            String room = etRoom.getText().toString().trim();
            ivBuscaRoom.startAnimation(rotate);
            etRoom.setText(room);
            habilitaBuscar(false);
            interfaceFormaPago.recuperaCreditoRoom(room,listaEstablecimientos.get(establecimientoSel).codigo);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.fragmentActivity = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void iniciaFormaPago(Bundle bundlePago) {

        efectivo = 0.00;
        tarjeta = 0.00;
        cuenta = 0.00;
        credito = 0.00;

        tvIgic.setText("Igic ("+ClaseUtils.double2string(ClaseCondicionesVenta.igic,2)+"% de "+
                        ClaseUtils.double2string(base,2)+"€) "+ ClaseUtils.double2string(igic,2)+"€.");

        tvImporte.setText("Total: "+ ClaseUtils.double2string(importe,2)+"€.");
        etCredito.setText("0.00");

        etEfectivo.setEnabled(false);
        etEfectivo.setText(ClaseUtils.double2string(efectivo,2));
        chEfectivo.setChecked(false);

        etTarjeta.setEnabled(false);
        etTarjeta.setText(ClaseUtils.double2string(tarjeta,2));
        chTarjeta.setChecked(false);

        etCuenta.setText(ClaseUtils.double2string(cuenta,2));
        chCuentaCasa.setChecked(false);

        etCredito.setText(ClaseUtils.double2string(credito,2));
        chCredito.setChecked(false);


        ivAceptar.setEnabled(false);

        layCuentaCasa.setVisibility(GONE);
        layEstablecimiento.setVisibility(GONE);
        layChkCredito.setVisibility(GONE);

        layEfectivo.setVisibility(VISIBLE);
        layEfectivo.setVisibility(VISIBLE);

        layTarjeta.setVisibility(VISIBLE);
        layCuenta.setVisibility(VISIBLE);
        layCredito.setVisibility(VISIBLE);

        if (! bundlePago.getBoolean("pagoEfectivo",true))
            layEfectivo.setVisibility(GONE);
        if (! bundlePago.getBoolean("pagoTarjeta",true))
            layTarjeta.setVisibility(GONE);
        if (! bundlePago.getBoolean("pagoCuentacasa",true))
            layCuenta.setVisibility(GONE);
        if (! bundlePago.getBoolean("pagoCredito",true))
            layCredito.setVisibility(GONE);
        if ( listaClientesCtaCasa.size() < 1)
            layCuenta.setVisibility(GONE);

        FragmentFormaPago.efetar =  bundlePago.getBoolean("pagoEfeTar",false);

        tw = new TwEfectivo();


        activaEventos();
    }



    private double calculaResto(enFormaPago forma) {

        double total = 0.00;

        efectivo = Double.valueOf(etEfectivo.getText().toString().replace(",","."));
        tarjeta = Double.valueOf(etTarjeta.getText().toString().replace(",","."));
        cuenta = Double.valueOf(etCuenta.getText().toString().replace(",","."));
        credito = Double.valueOf(etCredito.getText().toString().replace(",","."));

        if (!efetar ) { // solo admito una forma de pago efectivo  - tarjeta
            if (forma == enFormaPago.efectivo) total = 0;
            if (forma == enFormaPago.tarjeta)  total = 0;
        }
        else { // se puede pagar parte en efectivo y parte en tarjeta
            if (forma == enFormaPago.efectivo) total += tarjeta;
            if (forma == enFormaPago.tarjeta) total += efectivo;
        }

        if (forma == enFormaPago.cuenta) {
            return (base+igic);
        }

        if (forma == enFormaPago.credito) total = 0;

        return (base+igic) - total;
    }

    private static boolean calculaTotal(boolean aceptar) {

        double total = new BigDecimal(efectivo+tarjeta+cuenta+credito).setScale(2,RoundingMode.HALF_UP).doubleValue();
        double cobrar = new BigDecimal(base+igic).setScale(2,RoundingMode.HALF_UP).doubleValue();
        double diff = new BigDecimal(total - cobrar).setScale(2,RoundingMode.HALF_UP).doubleValue();

        if (diff > 0){
            if (efectivo >= diff)
                cambio = diff;
            else
                cambio = efectivo;
        }
        else
            cambio = 0.00;

        tvCambio.setText("Devolver: "+ ClaseUtils.double2string(cambio,2)+ "€");
        tvTotalCobrar.setText("Entregado: "+ClaseUtils.double2string(total,2)+" €");

        if (chCuentaCasa.isChecked()) {
            if (clienteCtaCasaSel >= 0){
                if (tipo_cuentacasa.toUpperCase().equals("G")) {
                    ivAceptar.setEnabled(true);
                    ivAceptar.setAlpha((float) 1);
                    return true;
                }
                else {
                    if ((total - cambio) == cobrar) {
                        ivAceptar.setEnabled(true);
                        ivAceptar.setAlpha((float) 1);
                        return true;
                    }
                    else {
                        ivAceptar.setEnabled(false);
                        ivAceptar.setAlpha((float) .3);
                        return false;
                    }
                }
            }
            else {
                ivAceptar.setEnabled(false);
                ivAceptar.setAlpha((float) .3);
                return false;
            }
        }
        else if (chCredito.isChecked()) {
            if (!aceptar){
                ivAceptar.setEnabled(false);
                ivAceptar.setAlpha((float) .3);

                habilitaBuscar(true);

                setIconNFC(false);
                ivBuscaRoom.setBackground(context.getDrawable(R.drawable.sync));

                return false;
            }
            else
                return true;
        }
        else {

            if ( new BigDecimal(total - cambio).setScale(2,RoundingMode.HALF_UP).doubleValue() == cobrar) {
                ivAceptar.setEnabled(true);
                ivAceptar.setAlpha((float) 1);
                return true;
            } else {
                ivAceptar.setEnabled(false);
                ivAceptar.setAlpha((float) .3);
                return false;
            }
        }
    }

    private void borraCampos(enFormaPago formaPago){
        if (formaPago != enFormaPago.cuenta){
            chCuentaCasa.setChecked(false);
            etCuenta.setText("0.00");
            //etCuenta.setEnabled(false);
            cuenta = 0;
            layCuentaCasa.setVisibility(GONE);
        }
        if (formaPago != enFormaPago.credito){
            chCredito.setChecked(false);
            etCredito.setText("0.00");
            etCredito.setEnabled(false);
            credito = 0;
            layChkCredito.setVisibility(GONE);
        }
        if (! efetar) {
            if (formaPago != enFormaPago.efectivo){
                chEfectivo.setChecked(false);
                etEfectivo.setText("0.00");
                etEfectivo.setEnabled(false);
                efectivo =0;

            }
            if (formaPago != enFormaPago.tarjeta){
                chTarjeta.setChecked(false);
                etTarjeta.setText("0.00");
                etTarjeta.setEnabled(false);
                tarjeta = 0;
            }
        }
        else {
            if (formaPago != enFormaPago.efectivo && formaPago != enFormaPago.tarjeta){

                chEfectivo.setChecked(false);
                etEfectivo.setText("0.00");
                etEfectivo.setEnabled(false);
                efectivo =0;

                chTarjeta.setChecked(false);
                etTarjeta.setText("0.00");
                etTarjeta.setEnabled(false);
                tarjeta = 0;

            }
        }
    }

    private void desactivaEventos(){
        chEfectivo.setOnClickListener(null);
        etEfectivo.removeTextChangedListener(tw);
        etTarjeta.removeTextChangedListener(tw);

        chTarjeta.setOnClickListener(null);
        chCuentaCasa.setOnClickListener(null);
        chCredito.setOnClickListener(null);
    }

    private void activaEventos() {
        chEfectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desactivaEventos();
                actualizaImporte(importe);
                if (chEfectivo.isChecked()) {
                    efectivo = calculaResto(enFormaPago.efectivo);
                    if (efectivo < 0) efectivo = 0.00;
                    etEfectivo.setText(ClaseUtils.double2string(efectivo,2));
                    etEfectivo.setEnabled(true);
                    etEfectivo.selectAll();
                    etEfectivo.requestFocus();

                    borraCampos(enFormaPago.efectivo);

                }
                else {
                    efectivo = 0.00;
                    etEfectivo.setText("0.00");
                    etEfectivo.setEnabled(false);
                }
                calculaTotal(false);
                activaEventos();
            }
        });


        etEfectivo.addTextChangedListener(tw);
        etTarjeta.addTextChangedListener(tw);

        chTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desactivaEventos();
                actualizaImporte(importe);
                if (chTarjeta.isChecked()) {
                    tarjeta = calculaResto(enFormaPago.tarjeta);
                    if (tarjeta < 0) tarjeta = 0.00;
                    etTarjeta.setText(ClaseUtils.double2string(tarjeta,2));
                    if (efetar) {
                        etTarjeta.setEnabled(true);
                        etTarjeta.selectAll();
                        etTarjeta.requestFocus();
                    }
                    borraCampos(enFormaPago.tarjeta);

                }
                else {
                    tarjeta = 0.00;
                    etTarjeta.setText("0.00");
                    etTarjeta.setEnabled(false);
                }
                calculaTotal(false);
                activaEventos();
            }
        });

        chCuentaCasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desactivaEventos();
                if (chCuentaCasa.isChecked()) {
                    actualizaImporte(coste);

                    cuenta = calculaResto(enFormaPago.cuenta);
                    if (cuenta < 0) cuenta = 0.00;
                    etCuenta.setText(ClaseUtils.double2string(cuenta,2));

                    borraCampos(enFormaPago.cuenta);

                    layCuentaCasa.setVisibility(VISIBLE);
                    clienteCtaCasaSel = -1;

                    adapterClientesCtaCasa.actualiza();

                }
                else {
                    actualizaImporte(importe);
                    cuenta = 0.00;
                    etCuenta.setText("0.00");
                    layCuentaCasa.setVisibility(GONE);
                    clienteCtaCasaSel = -1;
                }
                calculaTotal(false);
                activaEventos();
            }
        });

        chCredito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desactivaEventos();
                actualizaImporte(importe);
                if (chCredito.isChecked()) {
                    credito = calculaResto(enFormaPago.credito);
                    if (credito < 0) credito = 0.00;
                    etCredito.setText(ClaseUtils.double2string(credito,2));

                    borraCampos(enFormaPago.credito);

                    layDatos.setVisibility(GONE);

                    layChkCredito.setVisibility(VISIBLE);

                    habilitaBuscar(true);

                    if (listaEstablecimientos != null  && listaEstablecimientos.size() > 0 ) {
                        layEstablecimiento.setVisibility(VISIBLE);
                    }



                }
                else {
                    credito = 0.00;
                    etCredito.setText("0.00");
                    etCredito.setEnabled(false);
                    layChkCredito.setVisibility(GONE);
                }
                calculaTotal(false);

                if (room != null && !room.equals("")) {
                    etRoom.setText(room);
                    if (hayDatosPension) {
                        layDatos.setVisibility( VISIBLE);

                        ivBuscaRoom.setBackground(context.getDrawable(R.drawable.sync_error));
                        if (okSalida && okCredito) {
                            ivBuscaRoom.setBackground(context.getDrawable(R.drawable.sync_ok));

                            if (chCredito.isChecked()) {
                                ivAceptar.setAlpha((float) 1.0);
                                ivAceptar.setEnabled(true);
                            }
                        }

                    }
                }

                activaEventos();
            }
        });



    }


    private class TwEfectivo implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            calculaTotal(false);
        }

        @Override
        public void afterTextChanged(Editable s) {
            Double tmp= 0.00;
            try {
                tmp = Double.parseDouble(etEfectivo.getText().toString().trim().replace(",","."));
            }
            catch (Exception e){
                tmp = 0.00;
            }

            efectivo = tmp;
            try {
                tmp = Double.parseDouble(etTarjeta.getText().toString().trim().replace(",","."));
            }
            catch (Exception e){
                tmp = 0.00;
            }

            tarjeta = tmp;
            calculaTotal(false);
        }
    }

    private static void setIconNFC(boolean ok ) {
        if (nfcAnimation != null){
        nfcAnimation.cancel();
        nfcAnimation.reset();
        }

        switch (nfc.hasNFC()){
            case 0:
                ivNfc.setVisibility(GONE);
                break;
            case 1:
                ivNfc.setVisibility(VISIBLE);
                ivNfc.setBackground(context.getDrawable(R.drawable.nfc_off));
                break;
            default:
                ivNfc.setVisibility(VISIBLE);
                if (ok)
                    ivNfc.setBackground(context.getDrawable(R.drawable.nfc_read));
                else
                    ivNfc.setBackground(context.getDrawable(R.drawable.nfc_on));
                break;
        }

    }

    public class TagReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            animateNFC(false);
            nfc.setNfcReading(false);
            setIconNFC(true);
            ivBuscaRoom.startAnimation(rotate);
            layDatos.setVisibility(GONE);
            etRoom.setText("");
            interfaceFormaPago.recuperaCreditoTagID(nfc.getTagID(), listaEstablecimientos.get(establecimientoSel).codigo);
            hNFC.removeMessages(0);
            nfc.clearTagID();

        }
    }

   public void animateNFC(boolean animar){
        if (animar)
            ivNfc.startAnimation(nfcAnimation);
        else{
            ivNfc.clearAnimation();
        }

   }

   public static void habilitaBuscar(boolean activo){
        ivNfc.setEnabled(activo);
        ivBuscaRoom.setEnabled(activo);

        if ( pension != null && !pension.equalsIgnoreCase("")) {
            activo = false;
        }
        if (activo){
            ivNfc.setAlpha((float) 1);
            ivBuscaRoom.setAlpha((float) 1);
            etRoom.setEnabled(true);
        }
        else {
            ivNfc.setAlpha((float) .2);
            ivBuscaRoom.setAlpha((float) .2);
            etRoom.setEnabled(false);
        }
   }

    DialogInterface.OnClickListener onClickPideNFC = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                fragmentActivity.startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS),REQUEST_NFC_ENABLE);
            }
        }
    };

    View.OnClickListener onClickAceptaFirma = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            firma.cerrar();
            Bitmap bmp = firma.getFirma();
            Bitmap bmpEscala =ClaseUtils.escalarBitmap(bmp,400,400);
            Bitmap bmpGray = ClaseUtils.bitmapToGrayScale(bmpEscala);
            String strFirma = ClaseUtils.bitmapToBase64(bmpGray);
            pagar_credito(strFirma);

        }
    };

    View.OnClickListener onClickCancelaFirma = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            firma.cerrar();
        }
    };


    public static  void pagar_credito(String strFirma) {
        if (calculaTotal(true)) {
            Bundle datos = new Bundle();

            String formapago = "E";
            String apto = "";

            if (chCredito.isChecked() ) {
                formapago = "R";
                apto = habitacion;
            }
            if (chCuentaCasa.isChecked() ) {
                formapago = "C";
            }
            if (chTarjeta.isChecked() ) {
                formapago = "H";
            }
            if (chEfectivo.isChecked() ) {
                formapago = "E";
            }
            if (chEfectivo.isChecked() && chTarjeta.isChecked() ) {
                formapago = "Z";
            }

            datos.putString("formapago",formapago);

            if (formapago.toUpperCase().equals("C"))
                datos.putString("importe",ClaseUtils.double2string(coste,2).replace(",","."));
            else
                datos.putString("importe",ClaseUtils.double2string(importe,2).replace(",","."));

            datos.putString("igic",ClaseUtils.double2string(igic,2).replace(",","."));

            datos.putString("efectivo",ClaseUtils.double2string ((efectivo - cambio),2).replace(",","."));
            datos.putString("entregado",ClaseUtils.double2string ((efectivo ),2).replace(",","."));
            datos.putString("cambio",ClaseUtils.double2string ((cambio),2).replace(",","."));

            datos.putString("tarjeta",ClaseUtils.double2string(tarjeta,2).replace(",","."));
            datos.putString("cuentacasa",ClaseUtils.double2string(cuenta,2).replace(",","."));
            datos.putString("credito",ClaseUtils.double2string(credito,2).replace(",","."));


            if (chCuentaCasa.isChecked())
                datos.putString("codclicasa",String.valueOf(listaClientesCtaCasa.get(clienteCtaCasaSel).codigo));
            else
                datos.putString("codclicasa","000");

            if (!chCredito.isChecked()){
                nreserva = "";
                codemp_ext = "";
            }

            datos.putString("codemp_ext",codemp_ext);
            datos.putString("nreserva",nreserva);
            datos.putString("firma",strFirma);
            datos.putString("apto",apto);

            interfaceFormaPago.onClickAccionPagar(true, datos);
        }

        nfc.disableRead();
        try {
            context.unregisterReceiver(tagReceiver);
        }
        catch (Exception e){}

    }

    @Override
    public void onResume() {

        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
