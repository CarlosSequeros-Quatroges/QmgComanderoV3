package es.quatroges.qgestpv_v3;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import es.quatroges.qgestpv_v3.datos.listas.productos.ClaseProductos;
import es.quatroges.qgestpv_v3.datos.listas.productos.RvAdapterProductos;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;
import es.quatroges.qgestpv_v3.utils.OnSwipeTouchListener;

public class FragmentProductos extends Fragment
implements SearchView.OnQueryTextListener {

    FragmentActivity fragmentActivity;
    Context context;

    private ArrayList<ClaseProductos> listaProductos;
    public int productoSel;
    private boolean ordenCodigo;

    private androidx.appcompat.widget.SearchView svProducto;
    private LinearLayout layUnidades, layFiltros;
    private TextView tvUnidades;

    private RvAdapterProductos adapterProductos;

    private boolean fragmentAmpliado;

    public boolean isFragmentAmpliado() {
        return fragmentAmpliado;
    }

    public void setTimer(int milisg, boolean timerActivo) {
        FragmentProductos.milisg = milisg;
        FragmentProductos.timerActivo = timerActivo;
    }


    private RecyclerView rvProductos;
    private androidx.appcompat.widget.AppCompatImageView ivMin, ivMax, ivVerMas, ivFiltros, ivOrden;

    public static CountDownTimer timerCabecera;
    public static int milisg;
    public static FragmentProductos fragmentProductos;

    public static boolean timerActivo;

    public static int maxProductosLinea;
    public  int comandaUnidades;

    private static ArrayList<Integer> filtroAlergenos;
    private static ArrayList<Integer> filtroEtiquetas;
    private static ArrayList<Integer> filtroOrdenPlatos;

    public static ArrayList<Integer> getFiltroAlergenos() {
        return filtroAlergenos;
    }

    public static void setFiltroAlergenos(ArrayList<Integer> filtroAlergenos) {
        FragmentProductos.filtroAlergenos = filtroAlergenos;
    }

    public static ArrayList<Integer> getFiltroEtiquetas() {
        return filtroEtiquetas;
    }

    public static void setFiltroEtiquetas(ArrayList<Integer> filtroEtiquetas) {
        FragmentProductos.filtroEtiquetas = filtroEtiquetas;
    }

    public static ArrayList<Integer> getFiltroOrdenPlatos() {
        return filtroOrdenPlatos;
    }

    public static void setFiltroOrdenPlatos(ArrayList<Integer> filtroOrdenPlatos) {
        FragmentProductos.filtroOrdenPlatos = filtroOrdenPlatos;
    }

    public void incrementaUnidades(final String producto){
        comandaUnidades+=1;
        if (tvUnidades != null) {
            Animation a = AnimationUtils.loadAnimation(context, R.anim.escala_out);
            Animation b = AnimationUtils.loadAnimation(context, R.anim.fade_out);
            a.reset();
            b.reset();
            AnimationSet set = new AnimationSet(true);
            set.addAnimation(a);
            set.addAnimation(b);

            tvUnidades.clearAnimation();
            tvUnidades.startAnimation(set);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvUnidades.setText("Comanda: "+String.valueOf(comandaUnidades)+" Unidades ("+producto+")");
                }
            }, 300);
        }
    }

    public void posicionaProducto() {
        rvProductos.scrollToPosition(productoSel);
    }

    public void actualizaProductos(boolean reducir) {
        if (interfaceProductos != null){
            Bundle bundle = interfaceProductos.cargaProductos();
            productoSel = bundle.getInt("productoSel",1);
            listaProductos= bundle.getParcelableArrayList("productos");

            filtroAlergenos = bundle.getIntegerArrayList("alergenos");
            filtroEtiquetas = bundle.getIntegerArrayList("etiquetas");
            filtroOrdenPlatos = bundle.getIntegerArrayList("orden_platos");

            ivFiltros.setBackgroundDrawable(context.getDrawable(R.drawable.bar_filtrar));
            if ((filtroAlergenos.size() > 0) || (filtroEtiquetas.size() > 0) || (filtroOrdenPlatos.size() > 0)) {
                filtrarProductos();
                ivFiltros.setBackgroundDrawable(context.getDrawable(R.drawable.bar_filtrar_on));
            }

            CharSequence filtro = svProducto.getQuery();if (filtro.length() == 0) {
                if (listaProductos != null) {
                    Collections.sort(listaProductos);
                }


                milisg = bundle.getInt("milisg", 5);
                rvProductos.setAdapter(null);
                adapterProductos = new RvAdapterProductos(context, listaProductos, this);
                rvProductos.setAdapter(adapterProductos);
                if (reducir) {
                    reduceProductos(productoSel);
                }
            }
            else {
                adapterProductos.setListaProductos(listaProductos);
                adapterProductos.ordenar(filtro);
            }
        }
    }

    public void ampliarProductos(){
        ampliaProductos();
        interfaceProductos.fp_ocultaElementos(false);

    }

    public void cancelTimer(){
        if (timerCabecera != null ) {
            timerCabecera.cancel();
        }
    }

    public void resetTimer(){
        if (timerCabecera != null ) {
            timerCabecera.cancel();
        }
        if (FragmentProductos.timerActivo) {
            iniciaTimer();
        }
    }

    public interface InterfaceProductos {
        Bundle cargaProductos();
        void onClickProducto(String i);
        void fp_ocultaElementos(boolean visible);
        int getComandaNLineas();
        void onClickFiltrar();
        void onLongCLickProductos(String i);
    }

    InterfaceProductos interfaceProductos;


    public static FragmentProductos newInstance(int milis, boolean timerActivo) {
        FragmentProductos.milisg = milis;
        FragmentProductos.timerActivo = timerActivo;
        if (fragmentProductos == null) fragmentProductos = new FragmentProductos();
        return fragmentProductos;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.fragmentActivity = (FragmentActivity)context;
            this.context = context;
            interfaceProductos = (InterfaceProductos) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.productos_fragment,container,false);


        rvProductos = rootView. findViewById(R.id.rvProductos);
        ivMin = rootView.findViewById(R.id.ivMin);
        ivMax = rootView.findViewById(R.id.ivMax);
        ivVerMas = rootView.findViewById(R.id.ivVerMas);
        ivFiltros = rootView.findViewById(R.id.ivFiltros);
        ivOrden = rootView.findViewById(R.id.ivOrden);

        ordenCodigo = true;

        //rvProductos.setHasFixedSize(true);


        layFiltros = rootView.findViewById(R.id.layFiltos);
        svProducto = rootView.findViewById(R.id.svProductos);
        layUnidades = rootView.findViewById(R.id.layUnidades);
        tvUnidades = rootView.findViewById(R.id.tvUnidades);
        fragmentAmpliado = false;

        int filas = 2;
        if (ClaseUtils.modelo == ClaseUtils.enModel.phone) filas = 1;

        View view  = View.inflate(context,R.layout.productos_cardview,null);
        view.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        float vpWidth = view.getMeasuredWidth()/displayMetrics.density;
        int cols = (int)( dpWidth /vpWidth);
        if (cols <= 1) cols = 2;
        maxProductosLinea = cols;

        if (listaProductos != null && listaProductos.size() <= maxProductosLinea) filas = 1;

        GridLayoutManager llmProductos = new GridLayoutManager(context,filas, GridLayoutManager.HORIZONTAL, false);

        rvProductos.setLayoutManager(llmProductos);
        layFiltros.setVisibility(View.VISIBLE);
        layUnidades.setVisibility(View.VISIBLE);

        filtroAlergenos = new ArrayList<>();
        filtroEtiquetas = new ArrayList<>();

        Bundle bundle = interfaceProductos.cargaProductos ();

        productoSel = bundle.getInt("productoSel",1);
        listaProductos= bundle.getParcelableArrayList("productos");

        filtroAlergenos = bundle.getIntegerArrayList("alergenos");
        filtroEtiquetas = bundle.getIntegerArrayList("etiquetas");

        ivOrden.setBackgroundDrawable(context.getDrawable(R.drawable.bar_filtro_123));

        ivFiltros.setBackgroundDrawable(context.getDrawable(R.drawable.bar_filtrar));
        if ((filtroAlergenos.size() > 0 )|| (filtroEtiquetas.size() > 0)) {
            filtrarProductos();
            ivFiltros.setBackgroundDrawable(context.getDrawable(R.drawable.bar_filtrar_on));
        }
        ClaseProductos.ordenCodigo = ClaseProductos.enOrdenProductos.CODIGO;
        if (listaProductos != null) {
            Collections.sort(listaProductos);
        }

        adapterProductos = new RvAdapterProductos(context, listaProductos, this) ;
        rvProductos.setAdapter(adapterProductos);

        setupSearchView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rvProductos.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    mostrarMinMax();
                }
            });
        }
        else {
            rvProductos.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    mostrarMinMax();
                }
            });
        }

        rvProductos.setOnTouchListener(new OnSwipeTouchListener(context){
            @Override
            public void onSwipeDown() {
                if (!fragmentAmpliado ) {
                    ampliaProductos();
                    interfaceProductos.fp_ocultaElementos(false);
                }
            }

        });

        ivVerMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! fragmentAmpliado) {
                    ampliaProductos();
                    interfaceProductos.fp_ocultaElementos(false);
                }
                else {
                    reduceProductos(productoSel);
                    interfaceProductos.fp_ocultaElementos(true);
                }

            }
        });


        ivFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaceProductos.onClickFiltrar();
            }
        });

        ivOrden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ClaseProductos.ordenCodigo == ClaseProductos.enOrdenProductos.CODIGO) {
                    ClaseProductos.ordenCodigo = ClaseProductos.enOrdenProductos.NOMBRE;
                    ivOrden.setBackgroundDrawable(context.getDrawable(R.drawable.bar_filtro_az));
                }
                else if (ClaseProductos.ordenCodigo == ClaseProductos.enOrdenProductos.NOMBRE) {
                    ClaseProductos.ordenCodigo = ClaseProductos.enOrdenProductos.ORDEN_PLATOS;
                    ivOrden.setBackgroundDrawable(context.getDrawable(R.drawable.bar_filtro_orden));

                }
                else if (ClaseProductos.ordenCodigo == ClaseProductos.enOrdenProductos.ORDEN_PLATOS) {
                    ClaseProductos.ordenCodigo = ClaseProductos.enOrdenProductos.CODIGO;
                    ivOrden.setBackgroundDrawable(context.getDrawable(R.drawable.bar_filtro_123));

                }

                adapterProductos.ordenar(svProducto.getQuery());


            }
        });


        return rootView;

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


    private void ampliaProductos() {
        final RecyclerView.LayoutManager layout = rvProductos.getLayoutManager();
        if (layout  instanceof GridLayoutManager && fragmentAmpliado == false) {

            GridLayoutManager llmProductos = new GridLayoutManager(context, maxProductosLinea, GridLayoutManager.VERTICAL, false);

            rvProductos.setLayoutManager(llmProductos);
            ivMin.setVisibility(View.INVISIBLE);
            ivMax.setVisibility(View.INVISIBLE);
            ivVerMas.setRotation((float) 180);
            fragmentAmpliado = true;
            if (FragmentProductos.timerActivo) {
                iniciaTimer();
            }
            comandaUnidades = interfaceProductos.getComandaNLineas();
            tvUnidades.setText("Comanda: "+String.valueOf(comandaUnidades)+" Unidades");

            layFiltros.setVisibility(View.VISIBLE);
            layUnidades.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int min = ((GridLayoutManager) layout).findFirstCompletelyVisibleItemPosition();
                    int max = ((GridLayoutManager) layout).findLastCompletelyVisibleItemPosition();



                    if ((min <= 0) && (max >= listaProductos.size()-1)) {
                       //layFiltros.setVisibility(View.GONE);
                        //layUnidades.setVisibility(View.GONE);
                    }
                    else{
                        svProducto.setQuery("", false);
                        svProducto.clearFocus();
                        //layFiltros.setVisibility(View.VISIBLE);
                        //layUnidades.setVisibility(View.VISIBLE);
                    }

                }
            }, 300);
        }
        else {
            reduceProductos(productoSel);
        }
    }

    public void reduceProductos(int i) {

            int filas = 2;
            if (ClaseUtils.modelo == ClaseUtils.enModel.phone) filas = 1;

            if (listaProductos != null && listaProductos.size() <= maxProductosLinea) filas = 1;

            GridLayoutManager llmProductos = new GridLayoutManager(context,filas, GridLayoutManager.HORIZONTAL, false);
            rvProductos.setLayoutManager(llmProductos);

            if (i != -1 && fragmentAmpliado == true) rvProductos.scrollToPosition(i);

            fragmentAmpliado = false;

            ivVerMas.setRotation((float)0);
            layFiltros.setVisibility(View.GONE);
            layUnidades.setVisibility(View.GONE);
            adapterProductos.getFilter().filter("");

            if (timerCabecera != null) timerCabecera.cancel();

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mostrarMinMax();
                }
            });

    }

    private void mostrarMinMax() {
        RecyclerView.LayoutManager layout = rvProductos.getLayoutManager();
        if (layout  instanceof GridLayoutManager && fragmentAmpliado == false){
            boolean muestro = false;

            int min = ((LinearLayoutManager) layout).findFirstCompletelyVisibleItemPosition();
            int max = ((LinearLayoutManager) layout).findLastCompletelyVisibleItemPosition();

            if (min == 0)
                ivMin.setVisibility(View.INVISIBLE);
            else {
                ivMin.setVisibility(View.VISIBLE);
                muestro = true;
            }
            if (listaProductos != null) {
                if (max == listaProductos.size() - 1)
                    ivMax.setVisibility(View.INVISIBLE);
                else {
                    ivMax.setVisibility(View.VISIBLE);
                    muestro = true;
                }
            }
            else {
                layFiltros.setVisibility(View.GONE);
                layUnidades.setVisibility(View.GONE);
            }

            /*
            if (! muestro)
                ivVerMas.setVisibility(View.INVISIBLE);
            else
                ivVerMas.setVisibility(View.VISIBLE);
            */
        }
        else {
            ivMax.setVisibility(View.GONE);
            ivMin.setVisibility(View.GONE);
        }
    }

    private void iniciaTimer() {
        if (timerCabecera == null) {
            timerCabecera = new CountDownTimer(milisg, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    if (fragmentAmpliado) {
                        reduceProductos(-1);
                        interfaceProductos.fp_ocultaElementos(true);
                    }
                    timerCabecera.cancel();
                }
            };
        }
        timerCabecera.start();
    }

    private void setupSearchView() {
        svProducto.setIconifiedByDefault(false);
        svProducto.setOnQueryTextListener(this);
        svProducto.setSubmitButtonEnabled(false);
        svProducto.setQueryHint(context.getResources().getString(R.string.svProducto));

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapterProductos.getFilter().filter(s);
        resetTimer();
        return false;
    }

    void filtrarProductos() {
        ArrayList<ClaseProductos> tmpProd = new ArrayList<ClaseProductos>(listaProductos);
        for (ClaseProductos producto: tmpProd) {
            // eliminar si contiene el alergeno.
            if (filtroAlergenos.size() > 0 &&   contieneAlergeno(producto)){
                listaProductos.remove(producto);
                continue;
            }
            if (filtroEtiquetas.size() > 0 &&   ! contieneEtiqueta(producto)){
                listaProductos.remove(producto);
                continue;
            }
            if (filtroOrdenPlatos.size() > 0 && ! contieneOrdenPlato(producto)){
                listaProductos.remove(producto);
            }


        }

    }

    boolean contieneAlergeno(ClaseProductos producto){
        for (Integer codigo: filtroAlergenos){
            if (producto.alergenos.contains("|"+codigo.toString()+"|"))
                return true;
        }
        return false;
    }
    boolean contieneEtiqueta(ClaseProductos producto){
        for (Integer codigo: filtroEtiquetas){
            if (producto.etiquetas.contains("|"+codigo.toString()+"|"))
                return true;
        }
        return false;
    }
    boolean contieneOrdenPlato(ClaseProductos producto){
        for (Integer codigo: filtroOrdenPlatos){
            if (producto.orden_platos == codigo)
                return true;
        }
        return false;
    }

}
