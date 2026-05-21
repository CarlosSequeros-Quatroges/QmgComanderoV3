package es.quatroges.qgestpv_v3;

import static android.view.View.VISIBLE;
import static es.quatroges.qgestpv_v3.adapters.RvAdapterDetalleNotas.TIPO_EXTRA_CON;
import static es.quatroges.qgestpv_v3.adapters.RvAdapterDetalleNotas.TIPO_EXTRA_SIN;
import static es.quatroges.qgestpv_v3.utils.ClaseItemExtra.ESTADO_NADA;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import es.quatroges.qgestpv_v3.adapters.RvAdapterDetalleNotas;
import es.quatroges.qgestpv_v3.datos.listas.lineaVentas.ClaseLineaVentas;
import es.quatroges.qgestpv_v3.utils.ClaseItemExtra;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public class FragmentDetalleVenta extends Fragment {

    FragmentActivity fragmentActivity;
    private static Context context;
    private static ClaseLineaVentas detalleVenta;
    private static double precioOriginal;
    private static Handler hEventos;
    private boolean editar;
    private static  boolean moviendoArticulo;

    private static TextView tvDescripcion, tvTotal, tvHH, tvExtraNotas;
    private static EditText  etPrecio, etUnidades;
    private static androidx.appcompat.widget.AppCompatImageView ivUdMenos, ivUdMas, ivElimina, ivMueveSubmesa, ivAceptar, ivCancelar, ivHH, ivTipo,
            ivBebida,ivEntrante, ivPrimero, ivSegundo,ivPostre, ivCalculaPrecio, ivExtras;
    private RecyclerView rvResumenNotas;
    private static  RvAdapterDetalleNotas adapterDetalleNotas;
    private final ArrayList<RvAdapterDetalleNotas.ItemDetalleNota> itemsResumenNotas = new ArrayList<>();
    private LinearLayout  layOrden;
    private RelativeLayout rlyDatosUds;
    private static TextWatcher twNota, twPrecio, twUnidades;

    private int maxUnidades;
    private boolean isHappyHour;
    private static boolean estadoFragment;


    private static FragmentDetalleVenta fragmentDetalleVenta;
    private static boolean oculto;
    private enum enCampo  {
        nota, precio, cantidad;
    }
    private enCampo campo;
    private static boolean cambiosPendientes;

    private static View tecladoView;

    private static ClaseUtils.PideExtras dlgExtras;
    private boolean actualizandoNotas;


    public static boolean isCambiosPendientes() {
        return cambiosPendientes;
    }

    public static void mover_a_Submesa() {
        interfaceDetalleVenta.recuperaListaSubmesas();
        mueveSubmesa();
    }

    public void actualizaDetalleVenta() {
        if (interfaceDetalleVenta != null && ! moviendoArticulo){


            Bundle bundle = interfaceDetalleVenta.cargaDetalleVenta();

            layOrden.setVisibility(VISIBLE);
            if (bundle.getString("ordenplatos").toUpperCase().equals("N"))
                layOrden.setVisibility(View.GONE);


            detalleVenta = bundle.getParcelable("item");
            precioOriginal = bundle.getDouble("precio_original");
            editar = bundle.getBoolean("editar");

            isHappyHour = interfaceDetalleVenta.isHappyHour();
            maxUnidades = 99;

            if (detalleVenta != null) {
                if (!isHappyHour && detalleVenta.happyhour.toLowerCase().equals("s") )
                    maxUnidades = detalleVenta.cantidad;
            }
            ivAceptar.setVisibility(View.INVISIBLE);
            ivCancelar.setBackground(context.getDrawable(R.drawable.arrow));
            ivCancelar.setRotation((float)90);
            cargaDatos(detalleVenta, editar);
            cambiosPendientes = false;
        }
    }

    public static void estadoFragmentActivo(boolean estado){
        FragmentDetalleVenta.estadoFragment = estado;
    }


    public interface InterfaceDetalleVenta {
        Bundle cargaDetalleVenta();
        boolean isHappyHour();
        void eliminaLinea(boolean borrar, ClaseLineaVentas item);
        void actualizaLinea(ClaseLineaVentas detalleVenta);
        void ocultaElementos(boolean visible);
        ArrayList<Integer> recuperaListaSubmesas();
        boolean añadeSubmesa();
        void mueveLineaSubmesa(ClaseLineaVentas linea, int submesa);
    }

    public static InterfaceDetalleVenta interfaceDetalleVenta;


    public static FragmentDetalleVenta newInstance() {
        if (fragmentDetalleVenta == null )fragmentDetalleVenta= new FragmentDetalleVenta();
        return fragmentDetalleVenta;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Toast.makeText(fragmentActivity, "detalle visible "+String.valueOf(hidden), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.fragmentActivity = (FragmentActivity)context;
            this.context = context;
            interfaceDetalleVenta = (InterfaceDetalleVenta) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentDetalleVenta.estadoFragment= false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        oculto =false;
        moviendoArticulo = false;


        final View rootView = inflater.inflate(R.layout.detalleventa_fragment,container,false);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (FragmentDetalleVenta.estadoFragment == false) {
                    return;
                }

                Rect r = new Rect();
                //r will be populated with the coordinates of your view that area still visible.
                rootView.getWindowVisibleDisplayFrame(r);


                if (fragmentDetalleVenta != null && fragmentDetalleVenta.getView().getGlobalVisibleRect(new Rect())) {

                    int heightDiff = rootView.getRootView().getHeight() - (r.bottom - r.top);
                    if (heightDiff > 400) { // if more than 100 pixels, its probably a keyboard...
                        if (! oculto) {
                            if (campo != null && campo.equals(enCampo.nota))
                                rlyDatosUds.setVisibility(View.GONE);

                            interfaceDetalleVenta.ocultaElementos(false);
                            oculto = true;

                        }
                    } else {
                        if (oculto) {
                            rlyDatosUds.setVisibility(View.VISIBLE);
                            interfaceDetalleVenta.ocultaElementos(true);
                            oculto = false;
                        }
                    }
                }
            }
        });

        cargaObjetos(rootView);
        ivAceptar.setVisibility(View.INVISIBLE);
        ivCancelar.setBackground(context.getDrawable(R.drawable.arrow));
        ivCancelar.setRotation((float)90);

        Bundle bundle= interfaceDetalleVenta.cargaDetalleVenta();
        detalleVenta = bundle.getParcelable("item");
        editar = bundle.getBoolean("editar");

        if (detalleVenta == null) detalleVenta = new ClaseLineaVentas(0,0,0,"0",0,0.0,0.0,0.0,0.0,"","0","N","",ClaseUtils.enEstado.transmitida,"","",0, ClaseUtils.enEstado.transmitida, "N","", "N",1, false,"",0,0,"");
        cargaDatos(detalleVenta,editar);

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getParentFragmentManager().setFragmentResultListener(
                "pide_extras_result",
                getViewLifecycleOwner(),
                (requestKey, result) -> {
                    boolean cerrado = result.getBoolean("cerrado", false);
                    if (cerrado && dlgExtras != null) {

                        ArrayList<ClaseItemExtra> extras = new ArrayList<>();
                        ArrayList<ClaseItemExtra> extrasDialogo = dlgExtras.getExtras();
                        if (extrasDialogo != null) {
                            for (ClaseItemExtra extra : extrasDialogo) {
                                if (extra != null && extra.estadoExtra != ESTADO_NADA) {
                                    extras.add(extra);
                                }
                            }
                        }
                        detalleVenta.extras = dlgExtras.getExtras();

                        refrescaResumenExtras(detalleVenta.extras);
                    }
                }
        );
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

    private void cargaDatos(final ClaseLineaVentas detalleVenta, final boolean editar) {

        if (hEventos != null) {
            hEventos.removeCallbacksAndMessages(null);
        }
        desactivaEventos();
        hEventos = new Handler();
        hEventos.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (! cambiosPendientes ) ivAceptar.setVisibility(View.INVISIBLE);
                ivUdMas.setVisibility(View.VISIBLE);
                ivUdMenos.setVisibility(View.VISIBLE);
                ivElimina.setVisibility(View.VISIBLE);
                ivMueveSubmesa.setVisibility(VISIBLE);
                ivTipo.setVisibility(VISIBLE);

                ivBebida.setBackgroundDrawable(context.getDrawable(R.drawable.bar_bebida_sel));
                ivEntrante.setBackgroundDrawable(context.getDrawable(R.drawable.bar_entrante1));
                ivPrimero.setBackgroundDrawable(context.getDrawable(R.drawable.bar_primero1));
                ivSegundo.setBackgroundDrawable(context.getDrawable(R.drawable.bar_comida1));
                ivPostre.setBackgroundDrawable(context.getDrawable(R.drawable.bar_postre1));
                if (detalleVenta !=  null){
                    switch (detalleVenta.orden_platos){
                        case ClaseUtils.ORDEN_COMANDA_ENTRANTE:
                            ivBebida.setBackgroundDrawable(context.getDrawable(R.drawable.bar_bebida1));
                            ivPrimero.setBackgroundDrawable(context.getDrawable(R.drawable.bar_entrante_sel));
                            break;
                        case ClaseUtils.ORDEN_COMANDA_PRIMERO:
                            ivBebida.setBackgroundDrawable(context.getDrawable(R.drawable.bar_bebida1));
                            ivPrimero.setBackgroundDrawable(context.getDrawable(R.drawable.bar_primero_sel));
                            break;
                        case ClaseUtils.ORDEN_COMANDA_SEGUNDO:
                            ivBebida.setBackgroundDrawable(context.getDrawable(R.drawable.bar_bebida1));
                            ivSegundo.setBackgroundDrawable(context.getDrawable(R.drawable.bar_comida_sel));
                            break;
                        case ClaseUtils.ORDEN_COMANDA_POSTRE:
                            ivBebida.setBackgroundDrawable(context.getDrawable(R.drawable.bar_bebida1));
                            ivPostre.setBackgroundDrawable(context.getDrawable(R.drawable.bar_postre_sel));
                            break;
                    }
                }
                else{
                    ivBebida.setBackgroundDrawable(context.getDrawable(R.drawable.bar_bebida1));
                }


                if (ClaseUtils.modelo == ClaseUtils.enModel.phone)
                    ivCancelar.setVisibility(View.VISIBLE);
                else
                    ivCancelar.setVisibility(View.GONE);


                etPrecio.setEnabled(false);
                etPrecio.setBackgroundColor(ContextCompat.getColor(context,R.color.textoAzul));
                etPrecio.setTextColor(ContextCompat.getColor(context,R.color.textoBlanco));
                ivCalculaPrecio.setVisibility(View.INVISIBLE);

                if (detalleVenta != null && detalleVenta.mprecio.trim().toUpperCase().equals("S")) {
                    etPrecio.setEnabled(true);
                    etPrecio.setBackgroundColor(ContextCompat.getColor(context,R.color.fondoTextoEdit));
                    etPrecio.setTextColor(ContextCompat.getColor(context,R.color.negro));
                    ivCalculaPrecio.setVisibility(VISIBLE);

                }

                if (detalleVenta != null) {
                    etUnidades.setText(Integer.toString(detalleVenta.cantidad));
                    tvDescripcion.setText(detalleVenta.descripcion);
                    etPrecio.setText(ClaseUtils.double2string(detalleVenta.peuros,2));
                    tvTotal.setText(ClaseUtils.double2string(detalleVenta.teuros,2));
                    if (detalleVenta.tieneExtras()) {
                        refrescaResumenExtras(detalleVenta.extras);
                    }
                    if (detalleVenta.happyhour.toLowerCase().equals("s")) {
                        ivHH.setVisibility(View.VISIBLE);
                        tvHH.setVisibility(View.VISIBLE);
                    }
                    else {
                        ivHH.setVisibility(View.INVISIBLE);
                        tvHH.setVisibility(View.INVISIBLE);
                    }
                    if (detalleVenta.cantidad >= maxUnidades) {
                        ivUdMas.setVisibility(View.INVISIBLE);
                    }
                    else {
                        ivUdMas.setVisibility(View.VISIBLE);
                    }
                    if (detalleVenta.tipo.toUpperCase().trim().equals("B"))
                        ivTipo.setBackgroundDrawable(context.getDrawable(R.drawable.navmenu_bebida));
                    else
                        ivTipo.setBackgroundDrawable(context.getDrawable(R.drawable.navmenu_comida));

                    if (detalleVenta.estado == ClaseUtils.enEstado.transmitida)
                        ivUdMas.setVisibility(View.INVISIBLE);

                    ivBebida.setVisibility(VISIBLE);
                    ivEntrante.setVisibility(VISIBLE);
                    ivPrimero.setVisibility(View.VISIBLE);
                    ivSegundo.setVisibility(View.VISIBLE);
                    ivPostre.setVisibility(View.VISIBLE);
                    cambiaOrdenPlato(detalleVenta.orden_platos,false);

                }
                else {
                    etUnidades.setText("");
                    tvDescripcion.setText("");
                    etPrecio.setText("0.00");
                    tvTotal.setText("0.00");
                    ivHH.setVisibility(View.INVISIBLE);
                    tvHH.setVisibility(View.INVISIBLE);
                    ivUdMas.setVisibility(View.INVISIBLE);
                    ivUdMenos.setVisibility(View.INVISIBLE);
                    ivAceptar.setVisibility(View.INVISIBLE);
                    ivCancelar.setVisibility(View.INVISIBLE);
                    ivElimina.setVisibility(View.INVISIBLE);
                    ivMueveSubmesa.setVisibility(View.INVISIBLE);
                    ivTipo.setVisibility(View.INVISIBLE);
                    ivBebida.setVisibility(View.INVISIBLE);
                    ivEntrante.setVisibility(View.INVISIBLE);
                    ivPrimero.setVisibility(View.INVISIBLE);
                    ivSegundo.setVisibility(View.INVISIBLE);
                    ivPostre.setVisibility(View.INVISIBLE);
                }

                if (editar) {
                    hEventos = new Handler();
                    hEventos.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            activaEventos();
                        }
                    }, 200);
                }
                else {
                    ivUdMas.setVisibility(View.INVISIBLE);
                    ivUdMenos.setVisibility(View.INVISIBLE);
                    ivElimina.setVisibility(View.INVISIBLE);
                    ivMueveSubmesa.setVisibility(View.INVISIBLE);
                    ivCancelar.setVisibility(View.INVISIBLE);
                    etPrecio.setEnabled(false);
                    ivCalculaPrecio.setVisibility(View.INVISIBLE);
                }

            }
        },50);


    }

    private void activaEventos()  {

        ivElimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mostrarTeclado(false,null);
                rlyDatosUds.setVisibility(View.VISIBLE);
                oculto = false;

                if (detalleVenta.estado != ClaseUtils.enEstado.eliminar) {
                    ClaseUtils.AvisoResultado aviso = new ClaseUtils.AvisoResultado();
                    aviso.setTitulo(getResources().getString(R.string.alert_strAviso));
                    aviso.setMensaje(getResources().getString(R.string.detalle_strEliminarLinea));
                    aviso.setBotonTrue(getResources().getString(R.string.strSi));
                    aviso.setBotonFalse(getResources().getString(R.string.strNo));
                    aviso.setOnclick(clickEliminaLinea);
                    aviso.setContext(context);
                    aviso.execute();
                }

            }
        });

        ivUdMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarTeclado(false,null);
                rlyDatosUds.setVisibility(View.VISIBLE);
                oculto = false;

                int incremento = 1;
                int udPrecio;
                if (detalleVenta.happyhour.toLowerCase().equals("s"))
                    incremento = 2;

                if ((detalleVenta.cantidad + incremento) > maxUnidades )  {
                    Toast.makeText(context,"No se puede incrementar. Fuera de Happy Hour",Toast.LENGTH_LONG).show();
                }
                else {
                    detalleVenta.cantidad += incremento;

                    udPrecio = detalleVenta.cantidad;
                    if (detalleVenta.happyhour.toLowerCase().equals("s"))
                        udPrecio = detalleVenta.cantidad /2;

                    detalleVenta.teuros = udPrecio* detalleVenta.peuros;
                    detalleVenta.tcoste = udPrecio* detalleVenta.pcoste;

                    cargaDatos(detalleVenta,editar);
                    ivAceptar.setVisibility(View.VISIBLE);
                    ivCancelar.setVisibility(VISIBLE);
                    ivCancelar.setBackground(context.getDrawable(R.drawable.cancelar));
                    ivCancelar.setRotation((float)0);

                    cambiosPendientes = true;
                }

            }
        });

        ivUdMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarTeclado(false,null);
                rlyDatosUds.setVisibility(View.VISIBLE);
                oculto = false;

                int udPrecio;
                int incremento = 1;

                if (detalleVenta.happyhour.toLowerCase().equals("s"))
                    incremento = 2;

                if (detalleVenta.cantidad > incremento){
                    detalleVenta.cantidad -= incremento;

                    udPrecio = detalleVenta.cantidad;
                    if (detalleVenta.happyhour.toLowerCase().equals("s"))
                        udPrecio = detalleVenta.cantidad /2;

                    detalleVenta.teuros = udPrecio * detalleVenta.peuros;
                    detalleVenta.tcoste = udPrecio * detalleVenta.pcoste;

                    cargaDatos(detalleVenta,editar);
                    ivAceptar.setVisibility(View.VISIBLE);
                    ivCancelar.setVisibility(VISIBLE);
                    ivCancelar.setBackground(context.getDrawable(R.drawable.cancelar));
                    ivCancelar.setRotation((float)0);

                    cambiosPendientes = true;
                }
            }
        });

        ivCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarTeclado(false,null);
                rlyDatosUds.setVisibility(View.VISIBLE);
                oculto = false;
                cambiosPendientes = false;
                moviendoArticulo = false;


                if (interfaceDetalleVenta != null) {
                    detalleVenta = null;
                    Bundle bundle= interfaceDetalleVenta.cargaDetalleVenta();
                    detalleVenta = bundle.getParcelable("item");
                    editar = bundle.getBoolean("editar");
                    cargaDatos(detalleVenta,editar);
                    ivAceptar.setVisibility(View.INVISIBLE);
                    if (ClaseUtils.modelo == ClaseUtils.enModel.phone) {
                        ivCancelar.setBackground(context.getDrawable(R.drawable.arrow));
                        ivCancelar.setRotation((float) 90);
                    }
                    else
                        ivCancelar.setVisibility(View.INVISIBLE);
                }
            }
        });

        ivAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarTeclado(false,null);
                rlyDatosUds.setVisibility(View.VISIBLE);
                oculto = false;
                if (interfaceDetalleVenta != null) {
                    detalleVenta.imprimir = "S";
                    interfaceDetalleVenta.actualizaLinea(detalleVenta);
                    ivCancelar.setBackground(context.getDrawable(R.drawable.arrow));
                    ivCancelar.setRotation((float)90);
                    ivAceptar.setVisibility(View.INVISIBLE);
                }
            }
        });

        if (ivExtras != null) {
            ivExtras.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mostrarTeclado(false, null);
                    rlyDatosUds.setVisibility(View.VISIBLE);
                    oculto = false;

                    if (ClaseUtils.PideExtras.pideExtras != null && ClaseUtils.PideExtras.pideExtras.isVisible()) {
                        return;
                    }


                    dlgExtras = ClaseUtils.PideExtras.newInstance(context);
                    dlgExtras.setExtras(detalleVenta.extras);
                    dlgExtras.show(getParentFragmentManager(), "dialog_pide_extras");
                }
            });
        }

        etPrecio.addTextChangedListener(twPrecio);

        etPrecio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getId() == R.id.etPrecio && hasFocus && v.getVisibility() == VISIBLE){
                    oculto = true;
                    campo = enCampo.precio;
                    mostrarTeclado(true,etPrecio);
                    etPrecio.selectAll();
                }
                else {
                    mostrarTeclado(false,null);
                    rlyDatosUds.setVisibility(View.VISIBLE);
                    oculto = false;
                }
            }
        });

        ivCalculaPrecio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClaseUtils.CalculaPrecioManual aviso =  new ClaseUtils.CalculaPrecioManual();
                aviso.setBotonTrue(context.getResources().getString(R.string.strAceptar));
                aviso.setBotonFalse(context.getResources().getString(R.string.strCancelar));
                aviso.setOnclick(clickCambiaPrecio);
                aviso.setContext(context);
                aviso.setPrecioOriginal(precioOriginal);
                aviso.execute();
            }
        });

        etUnidades.addTextChangedListener(twUnidades);
        etUnidades.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getId() == R.id.etUnidades && hasFocus && v.getVisibility() == VISIBLE){
                    oculto = false;
                    campo = enCampo.cantidad;
                    mostrarTeclado(true,etUnidades);
                    etUnidades.selectAll();
                }
                else{
                    mostrarTeclado(false,null);
                    rlyDatosUds.setVisibility(VISIBLE);
                    oculto = false;
                }

            }
        });


        ivMueveSubmesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarTeclado(false,null);
                rlyDatosUds.setVisibility(View.VISIBLE);
                oculto = false;


                ArrayList<Integer> listaSubmesas = interfaceDetalleVenta.recuperaListaSubmesas();
                if (listaSubmesas == null || listaSubmesas.size() <1){
                    ClaseUtils.AvisoResultado aviso =  new ClaseUtils.AvisoResultado();
                    aviso.setTitulo(getResources().getString(R.string.alert_strAviso));
                    aviso.setMensaje(getResources().getString(R.string.detalle_strCrearSubmesa));
                    aviso.setBotonTrue(getResources().getString(R.string.strAceptar));
                    aviso.setBotonFalse(getResources().getString(R.string.strCancelar));
                    aviso.setOnclick(clickAñadeSubmesa);
                    aviso.setContext(context);
                    aviso.execute();

                    return;
                }

                mueveSubmesa();
            }
        });

        ivBebida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiaOrdenPlato(ClaseUtils.ORDEN_COMANDA_BEBIDA,true);
            }
        });
        ivEntrante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiaOrdenPlato(ClaseUtils.ORDEN_COMANDA_ENTRANTE,true);
            }
        });
        ivPrimero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiaOrdenPlato(ClaseUtils.ORDEN_COMANDA_PRIMERO,true);
            }
        });
        ivSegundo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiaOrdenPlato(ClaseUtils.ORDEN_COMANDA_SEGUNDO,true);
            }
        });
        ivPostre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiaOrdenPlato(ClaseUtils.ORDEN_COMANDA_POSTRE,true);
            }
        });


    }
    private void cambiaOrdenPlato (int orden, boolean carga ){

        ivBebida.setBackgroundDrawable(context.getDrawable(R.drawable.bar_bebida_sel));
        ivEntrante.setBackgroundDrawable(context.getDrawable(R.drawable.bar_entrante1));
        ivPrimero.setBackgroundDrawable(context.getDrawable(R.drawable.bar_primero1));
        ivSegundo.setBackgroundDrawable(context.getDrawable(R.drawable.bar_comida1));
        ivPostre.setBackgroundDrawable(context.getDrawable(R.drawable.bar_postre1));
        switch (orden){
            case ClaseUtils.ORDEN_COMANDA_ENTRANTE:
                ivBebida.setBackgroundDrawable(context.getDrawable(R.drawable.bar_bebida1));
                ivEntrante.setBackgroundDrawable(context.getDrawable(R.drawable.bar_entrante_sel));
                break;
            case ClaseUtils.ORDEN_COMANDA_PRIMERO:
                ivBebida.setBackgroundDrawable(context.getDrawable(R.drawable.bar_bebida1));
                ivPrimero.setBackgroundDrawable(context.getDrawable(R.drawable.bar_primero_sel));
                break;
            case ClaseUtils.ORDEN_COMANDA_SEGUNDO:
                ivBebida.setBackgroundDrawable(context.getDrawable(R.drawable.bar_bebida1));
                ivSegundo.setBackgroundDrawable(context.getDrawable(R.drawable.bar_comida_sel));
                break;
            case ClaseUtils.ORDEN_COMANDA_POSTRE:
                ivBebida.setBackgroundDrawable(context.getDrawable(R.drawable.bar_bebida1));
                ivPostre.setBackgroundDrawable(context.getDrawable(R.drawable.bar_postre_sel));
                break;
        }

        if (carga) {
            detalleVenta.orden_platos = orden;

            cargaDatos(detalleVenta, editar);
            ivAceptar.setVisibility(View.VISIBLE);
            ivCancelar.setVisibility(VISIBLE);
            ivCancelar.setBackground(context.getDrawable(R.drawable.cancelar));
            ivCancelar.setRotation((float) 0);
            cambiosPendientes = true;

        }

    }
    private void cargaObjetos(View rootView) {
        ivUdMenos = rootView.findViewById(R.id.ivUdMenos);
        ivUdMas = rootView.findViewById(R.id.ivUdMas);
        ivElimina= rootView.findViewById(R.id.ivEliminar);
        ivMueveSubmesa = rootView.findViewById(R.id.ivMueveSubmesa);
        ivAceptar = rootView.findViewById(R.id.ivAceptar);
        ivCancelar  = rootView.findViewById(R.id.ivCancelar);
        ivHH = rootView.findViewById(R.id.ivHH);
        ivTipo = rootView.findViewById(R.id.ivTipo);

        ivBebida = rootView.findViewById(R.id.ivBebida);
        ivEntrante = rootView.findViewById(R.id.ivEntrante);
        ivPrimero  = rootView.findViewById(R.id.ivPrimero);
        ivSegundo = rootView.findViewById(R.id.ivSegundo);
        ivPostre = rootView.findViewById(R.id.ivPostre);

        etUnidades = rootView.findViewById(R.id.etUnidades);

        tvDescripcion = rootView.findViewById(R.id.tvDescripcion);

        etPrecio = rootView.findViewById(R.id.etPrecio);
        ivCalculaPrecio = rootView.findViewById(R.id.ivCalculaPrecio);
        tvTotal= rootView.findViewById(R.id.tvTotal);

        tvHH = rootView.findViewById(R.id.tvHH);

        rvResumenNotas = rootView.findViewById(R.id.rvResumenNotas);
        adapterDetalleNotas = new RvAdapterDetalleNotas(itemsResumenNotas);
        if (rvResumenNotas != null) {
            rvResumenNotas.setLayoutManager(new LinearLayoutManager(context));
            rvResumenNotas.setNestedScrollingEnabled(false);
            rvResumenNotas.setAdapter(adapterDetalleNotas);
        }

        layOrden = rootView.findViewById(R.id.layOrden);
        layOrden.setVisibility(View.GONE);

        rlyDatosUds = rootView.findViewById(R.id.layDatosUds);

        ivExtras = rootView.findViewById(R.id.ivExtras);
        tvExtraNotas = rootView.findViewById(R.id.tvExtraNotas);
        tvExtraNotas.setText("Extras / Notas ( 0 )");


        twPrecio =  new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int udPrecio;
                if (detalleVenta != null) {
                    try {
                        detalleVenta.peuros = Double.valueOf(etPrecio.getText().toString().replace(",", "."));
                    }
                    catch (Exception e){
                        detalleVenta.peuros = 0;
                    }
                    udPrecio = detalleVenta.cantidad;
                    if (detalleVenta.happyhour.toLowerCase().equals("s"))
                        udPrecio = detalleVenta.cantidad / 2;

                    detalleVenta.teuros = udPrecio * detalleVenta.peuros;
                    detalleVenta.tcoste = udPrecio * detalleVenta.pcoste;

                    tvTotal.setText(ClaseUtils.double2string(detalleVenta.teuros, 2));

                }
                ivAceptar.setVisibility(View.VISIBLE);
                ivCancelar.setVisibility(VISIBLE);
                ivCancelar.setBackground(context.getDrawable(R.drawable.cancelar));
                ivCancelar.setRotation((float)0);

                cambiosPendientes = true;

            }
        };

        twUnidades = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (detalleVenta == null ) return;

                int cantidad= detalleVenta.cantidad;
                try {
                    cantidad =Integer.valueOf(etUnidades.getText().toString());
                }
                catch (Exception e ){
                    cantidad = detalleVenta.cantidad;
                }
                if ((cantidad) > maxUnidades )  {
                    Toast.makeText(context,"No se puede incrementar. Fuera de Happy Hour",Toast.LENGTH_LONG).show();
                }
                else {
                    detalleVenta.cantidad = cantidad;

                    int udPrecio = detalleVenta.cantidad;
                    if (detalleVenta.happyhour.toLowerCase().equals("s"))
                        udPrecio = detalleVenta.cantidad /2;

                    detalleVenta.teuros = udPrecio* detalleVenta.peuros;
                    detalleVenta.tcoste = udPrecio * detalleVenta.pcoste;

                    cargaDatos(detalleVenta,editar);
                    ivAceptar.setVisibility(View.VISIBLE);
                    ivCancelar.setVisibility(VISIBLE);
                    ivCancelar.setBackground(context.getDrawable(R.drawable.cancelar));
                    ivCancelar.setRotation((float)0);

                    cambiosPendientes = true;
                }

            }
        };

    }



    private void refrescaResumenExtras(ArrayList<ClaseItemExtra> extrasLinea) {
        ArrayList<RvAdapterDetalleNotas.ItemDetalleNota> items = new ArrayList<>();

        if (extrasLinea != null) {
            for (ClaseItemExtra extra : extrasLinea) {
                if (extra == null ) {
                    continue;
                }
                //notas
                if (extra.tipo != null && extra.tipo.equalsIgnoreCase("N") && extra.nota != null && !extra.nota.trim().isEmpty()) {

                    items.add(new RvAdapterDetalleNotas.ItemDetalleNota(
                            RvAdapterDetalleNotas.TIPO_NOTA,
                            extra.nota.trim(),
                            extra.estado
                    ));

                }
                //extras
                else {
                    String textoExtra = extra.nota.trim();
                    int tipoExtra = TIPO_EXTRA_CON;
                    if (textoExtra.toUpperCase(Locale.ROOT).startsWith("SIN ")) {
                        tipoExtra = TIPO_EXTRA_SIN;
                        textoExtra = textoExtra.substring(4).trim();
                    } else if (textoExtra.toUpperCase(Locale.ROOT).startsWith("CON ")) {
                        textoExtra = textoExtra.substring(4).trim();
                    }
                    items.add(new RvAdapterDetalleNotas.ItemDetalleNota(
                            tipoExtra,
                            textoExtra,
                            extra.estado
                    ));
                }
            }
        }

        if (adapterDetalleNotas != null) {
            adapterDetalleNotas.setItems(items);
        }
        ajustaAltoResumenNotas(items.size());

        int n = items.size();
        tvExtraNotas.setText("Extras / Notas ( "+String.valueOf(n)+" )");

        if ( extrasLinea.stream().anyMatch(e -> e.estado != ClaseUtils.enEstado.transmitida) ) {
            ivAceptar.setVisibility(View.VISIBLE);
            ivCancelar.setVisibility(VISIBLE);
            ivCancelar.setBackground(context.getDrawable(R.drawable.cancelar));
            ivCancelar.setRotation((float) 0);
            cambiosPendientes = true;
        }
    }

    private String horaActual() {
        return new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    private void ajustaAltoResumenNotas(int totalItems) {
        if (rvResumenNotas == null) {
            return;
        }

        float density = getResources().getDisplayMetrics().density;
        int altoFila = (int) (44 * density);
        int altoMinimo = (int) (90 * density);
        int altoMaximo = (int) (260 * density);
        int altoNecesario = totalItems <= 0 ? altoMinimo : (totalItems * altoFila);
        int altoFinal = Math.max(altoMinimo, Math.min(altoNecesario, altoMaximo));

        ViewGroup.LayoutParams params = rvResumenNotas.getLayoutParams();
        if (params != null && params.height != altoFinal) {
            params.height = altoFinal;
            rvResumenNotas.setLayoutParams(params);
        }

        rvResumenNotas.setNestedScrollingEnabled(altoNecesario > altoMaximo);
    }

    private void desactivaEventos() {
        ivElimina.setOnClickListener(null);
        ivUdMas.setOnClickListener(null);
        ivUdMenos.setOnClickListener(null);
        ivCancelar.setOnClickListener(null);
        ivAceptar.setOnClickListener(null);
        ivMueveSubmesa.setOnClickListener(null);
        etPrecio.setOnFocusChangeListener(null);
        ivCalculaPrecio.setOnClickListener(null);
        etPrecio.removeTextChangedListener(twPrecio);
        etUnidades.setOnFocusChangeListener(null);
        etUnidades.removeTextChangedListener(twUnidades);

        ivBebida.setOnClickListener(null);
        ivEntrante.setOnClickListener(null);
        ivPrimero.setOnClickListener(null);
        ivSegundo.setOnClickListener(null);
        ivPostre.setOnClickListener(null);

    }

    private void mostrarTeclado(boolean mostrar, View view)  {
        if (mostrar){
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            tecladoView = view;
        }
        else {
            if (tecladoView != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(tecladoView.getWindowToken(), 0);
            }
            tecladoView = null;

        }
    }

    private static void mueveSubmesa(){
        if (cambiosPendientes) {
            ClaseUtils.Aviso.mostrarAviso(context.getResources().getString(R.string.alert_strError),
                    context.getResources().getString(R.string.alert_strCambiosPendientesAviso),context);
            return;
        }

        moviendoArticulo = true;

        ClaseUtils.PideUnidades aviso =  new ClaseUtils.PideUnidades();
        aviso.setTitulo(context.getResources().getString(R.string.alert_strAviso));
        aviso.setMensaje(context.getResources().getString(R.string.detalle_strMueveSubmesa));
        aviso.setBotonTrue(context.getResources().getString(R.string.strAceptar));
        aviso.setBotonFalse(context.getResources().getString(R.string.strCancelar));
        aviso.setOnclick(clickMueveSubmesa);
        aviso.setContext(context);
        ArrayList<Integer> listaSubmesas = interfaceDetalleVenta.recuperaListaSubmesas();
        aviso.setSubmesas(listaSubmesas);
        aviso.execute(detalleVenta.cantidad);


    }

    private static void eliminaLinea() {
        if (interfaceDetalleVenta != null) {
            interfaceDetalleVenta.eliminaLinea(false, detalleVenta);
        }

    }

    static DialogInterface.OnClickListener clickAñadeSubmesa=  new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                if (interfaceDetalleVenta.añadeSubmesa()) {
                    interfaceDetalleVenta.recuperaListaSubmesas();
                    mueveSubmesa();
                }
            }
        }
    };

    static DialogInterface.OnClickListener clickMueveSubmesa=  new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            moviendoArticulo = false;

            if (which == DialogInterface.BUTTON_POSITIVE) {
                int unidades =  ClaseUtils.PideUnidades.getUnidades();
                if (unidades == 0){
                    return ;
                }
                int submesa = ClaseUtils.PideUnidades.getSubmesaSel();
                if (unidades > detalleVenta.cantidad)
                    unidades = detalleVenta.cantidad;

                ClaseLineaVentas nuevaLinea = (ClaseLineaVentas) detalleVenta.clone();

                if (unidades < detalleVenta.cantidad){
                    detalleVenta.cantidad -= unidades;
                    detalleVenta.teuros = detalleVenta.cantidad * detalleVenta.peuros;
                    detalleVenta.tcoste = detalleVenta.cantidad * detalleVenta.pcoste;

                    nuevaLinea.cantidad = unidades;
                    nuevaLinea.teuros = nuevaLinea.cantidad * nuevaLinea.peuros;
                    nuevaLinea.tcoste = nuevaLinea.cantidad * nuevaLinea.pcoste;

                    nuevaLinea.estado = ClaseUtils.enEstado.anadir;
                    nuevaLinea.codigo = 0;
                    nuevaLinea.submesa = submesa;
                    if (detalleVenta.estado == ClaseUtils.enEstado.transmitida) {
                        nuevaLinea.imprimir = "N";
                        detalleVenta.imprimir = "N";
                    }
                    else {
                        nuevaLinea.imprimir = "S";
                        detalleVenta.imprimir = "S";
                    }



                    interfaceDetalleVenta.actualizaLinea(detalleVenta);
                    interfaceDetalleVenta.mueveLineaSubmesa(nuevaLinea,submesa);
                }
                else
                {
                    if (nuevaLinea.estado == ClaseUtils.enEstado.transmitida)
                        nuevaLinea.estado = ClaseUtils.enEstado.actualizar;

                    if (detalleVenta.estado == ClaseUtils.enEstado.transmitida){
                        nuevaLinea.imprimir ="N";
                        detalleVenta.imprimir = "N";
                    }
                    else {
                        nuevaLinea.imprimir = "S";
                        detalleVenta.imprimir = "S";
                    }
                    nuevaLinea.submesa = submesa;
                    interfaceDetalleVenta.mueveLineaSubmesa(nuevaLinea,submesa);
                    interfaceDetalleVenta.eliminaLinea(true, detalleVenta);
                }

            }
        }
    };

    static DialogInterface.OnClickListener clickEliminaLinea=  new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                eliminaLinea();
            }
        }
    };

    static DialogInterface.OnClickListener clickCambiaPrecio = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == DialogInterface.BUTTON_POSITIVE){
                Double precio = ClaseUtils.CalculaPrecioManual.getPrecioFinal();
                etPrecio.setText( ClaseUtils.double2string(precio,2));
                detalleVenta.peuros =Double.parseDouble( ClaseUtils.double2string(precio,2).replace(",","."));

                switch(ClaseUtils.CalculaPrecioManual.getOpcion()){
                    case 1: // % descuento
                        detalleVenta.descuento = String.valueOf(ClaseUtils.CalculaPrecioManual.getPorcentaje() )+ " % de descuento";
                        break;
                    case 2:
                        detalleVenta.descuento = "Precio para  "+ String.valueOf(ClaseUtils.CalculaPrecioManual.getPeso() )+ " gramos";
                        break;
                    case 3:
                        detalleVenta.descuento = "Descuento de "+ String.valueOf(ClaseUtils.CalculaPrecioManual.getImporteDto() )+ " €";
                        break;
                }
            }
        }
    };


}

