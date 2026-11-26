package es.quatroges.qgestpv_v3;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import es.quatroges.qgestpv_v3.datos.listas.mesas.ClaseMesas;
import es.quatroges.qgestpv_v3.datos.listas.mesas.ClaseSubMesas;
import es.quatroges.qgestpv_v3.datos.listas.mesas.RvAdapterMesas;
import es.quatroges.qgestpv_v3.datos.listas.mesas.RvAdapterPedidos;

public class FragmentMesas extends Fragment
        implements  SearchView.OnQueryTextListener  {

    FragmentActivity fragmentActivity;
    Context context;
    private TreeMap<Integer, ClaseMesas> listaMesas;
    public int mesaSel;
    public boolean pidePax;
    public boolean pideRoom;

    private RecyclerView rvMesas;

    private androidx.appcompat.widget.SearchView svMesa;
    private RvAdapterMesas adapterMesas;
    private RvAdapterPedidos adapterPedidos;

    /** pestañas MESAS / PEDIDOS: tipo de estado que se muestra (ClaseMesas.TIPO_ESTADO_*) */
    private TabLayout tlTipoMesas;
    /** icono vibrando mientras haya pedidos web nuevos sin ver */
    private View ivAvisoPedidos;
    private String tipoEstado = ClaseMesas.TIPO_ESTADO_MESAS;
    private boolean ajustandoTab;
    private int nMesasVisibles;
    /** columnas de la rejilla de mesas (los pedidos van a una tarjeta por fila) */
    private int columnasMesas = 2;


    public enum enEstado {
        seleccion, datos;
    }
    public FragmentMesas.enEstado estado;

    public void filtrarItem(String mesa){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                quitaFocoAdapter();
                filtra(mesa);

            }
        }, 50);
        //adapterMesas.setQuitaFocoSvMesa(true);
        filtra("");

    }
    public void swMesaClearFocus(){
        svMesa.clearFocus();
    }

    public void actualizaMesas(boolean refresca) {
        if (interfaceMesas != null){
            Bundle bundle = interfaceMesas.cargaMesas(refresca);
            mesaSel = bundle.getInt("mesaSel",1);
            listaMesas= (TreeMap<Integer, ClaseMesas>) bundle.getSerializable("mesas");
            pidePax = bundle.getBoolean("pidePax",false);
            pideRoom = bundle.getBoolean("pideRoom",false);
            rvMesas.setAdapter(null);

            sincronizaTab(bundle.getString("tipoEstado", ClaseMesas.TIPO_ESTADO_MESAS));
            ArrayList<ClaseMesas> tmpListMesas = mesasDelTipo(listaMesas);
            ordenaLista(tmpListMesas);

            rvMesas.setAdapter(creaAdapter(tmpListMesas));
            estado = FragmentMesas.enEstado.seleccion;

            svMesa.setQuery("",false);
            svMesa.clearFocus();

            muestraSearch();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mostrarSearch();
                }
            },500);

        }
    }



    /** orden de la pestaña activa: pedidos nuevos primero y los mas recientes arriba; mesas por estado y numero */
    private void ordenaLista(ArrayList<ClaseMesas> lista) {
        if (ClaseMesas.TIPO_ESTADO_PEDIDOS.equals(tipoEstado)) {
            ordenaPedidos(lista);
        } else {
            ordenaMesas(lista);
        }
    }

    /** pedidos: aviso_web = S (nuevos sin ver) primero, despues por fecha y hora del pedido descendente y por numero */
    private static void ordenaPedidos(ArrayList<ClaseMesas> pedidos) {
        Collections.sort(pedidos, new Comparator<ClaseMesas>() {
            @Override
            public int compare(ClaseMesas a, ClaseMesas b) {
                if (a.avisoWeb != b.avisoWeb) return a.avisoWeb ? -1 : 1;
                String fa = a.fechaHoraPedido == null ? "" : a.fechaHoraPedido;
                String fb = b.fechaHoraPedido == null ? "" : b.fechaHoraPedido;
                int porFecha = fb.compareTo(fa);
                if (porFecha != 0) return porFecha;
                return Integer.compare(b.mesa, a.mesa);
            }
        });
    }

    /** mesas con tiquet primero, luego abiertas, luego libres; dentro de cada grupo por numero */
    private static void ordenaMesas(ArrayList<ClaseMesas> mesas) {
        Collections.sort(mesas, new Comparator<ClaseMesas>() {
            @Override
            public int compare(ClaseMesas claseMesas, ClaseMesas t1) {
                if (claseMesas.tickets && ! t1.tickets )  return -1;
                if (claseMesas.tickets && t1.tickets) return Integer.valueOf(claseMesas.mesa).compareTo(t1.mesa);
                if (t1.tickets == true) return 1;
                if (claseMesas.abiertas == true && ! t1.abiertas) return -1;
                if (claseMesas.abiertas && t1.abiertas) return Integer.valueOf(claseMesas.mesa).compareTo(t1.mesa);
                if (t1.abiertas == true) return 1;
                return Integer.valueOf(claseMesas.mesa).compareTo(t1.mesa);
            }
        });
    }

    /**
     * Refresco automatico del estado de las mesas: vuelve a pintar la lista con los datos actuales sin cambiar de pantalla,
     * conservando el filtro de busqueda y la posicion de scroll. No hace nada si hay una edicion de pax/habitacion abierta.
     */
    public void refrescaEstadoMesas() {
        if (interfaceMesas == null || rvMesas == null || rvMesas.getAdapter() == null || estado != enEstado.seleccion) return;
        Bundle bundle = interfaceMesas.cargaMesas(true);
        listaMesas = (TreeMap<Integer, ClaseMesas>) bundle.getSerializable("mesas");
        sincronizaTab(bundle.getString("tipoEstado", ClaseMesas.TIPO_ESTADO_MESAS));
        ArrayList<ClaseMesas> tmpListMesas = mesasDelTipo(listaMesas);
        ordenaLista(tmpListMesas);

        CharSequence filtro = svMesa != null ? svMesa.getQuery() : null;
        Parcelable scroll = rvMesas.getLayoutManager() != null ? rvMesas.getLayoutManager().onSaveInstanceState() : null;
        rvMesas.setAdapter(creaAdapter(tmpListMesas));
        if (filtro != null && filtro.length() > 0) filtra(filtro);
        if (scroll != null && rvMesas.getLayoutManager() != null) rvMesas.getLayoutManager().onRestoreInstanceState(scroll);
    }

    /** Mesas de la pestaña activa: las del tpv (numero < 1000) en MESAS, los pedidos (>= 1000) en PEDIDOS.
     *  De paso actualiza el recuento de las pestañas: mesas abiertas o con cuenta pedida, y numero de pedidos. */
    private ArrayList<ClaseMesas> mesasDelTipo(TreeMap<Integer, ClaseMesas> mesas) {
        ArrayList<ClaseMesas> lista = new ArrayList<>();
        boolean pedidos = ClaseMesas.TIPO_ESTADO_PEDIDOS.equals(tipoEstado);
        int nAbiertas = 0;
        int nPedidos = 0;
        int nPedidosNuevos = 0;
        if (mesas != null) {
            for (Map.Entry<Integer, ClaseMesas> entry : mesas.entrySet()) {
                ClaseMesas mesa = entry.getValue();
                if (mesa.esPedido()) {
                    nPedidos++;
                    if (mesa.avisoWeb) nPedidosNuevos++;
                } else if (mesa.abiertas || mesa.tickets) {
                    nAbiertas++;
                }
                if (mesa.esPedido() == pedidos) lista.add(mesa);
            }
        }
        nMesasVisibles = lista.size();
        actualizaTitulosTabs(nAbiertas, nPedidos);
        muestraAvisoPedidos(nPedidosNuevos > 0);
        return lista;
    }

    /** Icono de aviso vibrando mientras haya pedidos web nuevos (aviso_web = S); oculto y parado si no hay. */
    private void muestraAvisoPedidos(boolean hayNuevos) {
        if (ivAvisoPedidos == null || context == null) return;
        if (hayNuevos) {
            if (ivAvisoPedidos.getVisibility() != View.VISIBLE || ivAvisoPedidos.getAnimation() == null) {
                ivAvisoPedidos.setVisibility(View.VISIBLE);
                Animation vibracion = AnimationUtils.loadAnimation(context, R.anim.vibracion);
                ivAvisoPedidos.startAnimation(vibracion);
            }
        } else {
            ivAvisoPedidos.clearAnimation();
            ivAvisoPedidos.setVisibility(View.GONE);
        }
    }

    private void actualizaTitulosTabs(int nAbiertas, int nPedidos) {
        if (tlTipoMesas == null || context == null) return;
        TabLayout.Tab tabMesas = tlTipoMesas.getTabAt(0);
        TabLayout.Tab tabPedidos = tlTipoMesas.getTabAt(1);
        if (tabMesas != null) tabMesas.setText(context.getString(R.string.mesas_strTabMesasN, nAbiertas));
        if (tabPedidos != null) tabPedidos.setText(context.getString(R.string.mesas_strTabPedidosN, nPedidos));
    }

    /** Adaptador de la pestaña activa: tarjetas de mesa en rejilla o tarjetas de pedido a una por fila. */
    private RecyclerView.Adapter<?> creaAdapter(ArrayList<ClaseMesas> lista) {
        boolean pedidos = ClaseMesas.TIPO_ESTADO_PEDIDOS.equals(tipoEstado);
        if (rvMesas != null && rvMesas.getLayoutManager() instanceof GridLayoutManager) {
            ((GridLayoutManager) rvMesas.getLayoutManager()).setSpanCount(pedidos ? 1 : columnasMesas);
        }
        if (pedidos) {
            adapterMesas = null;
            adapterPedidos = new RvAdapterPedidos(context, lista, this);
            return adapterPedidos;
        }
        adapterPedidos = null;
        adapterMesas = new RvAdapterMesas(context, lista, this);
        return adapterMesas;
    }

    private Filter filtroAdapter() {
        if (adapterPedidos != null) return adapterPedidos.getFilter();
        if (adapterMesas != null) return adapterMesas.getFilter();
        return null;
    }

    private void filtra(CharSequence texto) {
        Filter filtro = filtroAdapter();
        if (filtro != null) filtro.filter(texto);
    }

    private void quitaFocoAdapter() {
        if (adapterPedidos != null) adapterPedidos.setQuitaFocoSvMesa(true);
        if (adapterMesas != null) adapterMesas.setQuitaFocoSvMesa(true);
    }

    private void configuraTabs() {
        if (tlTipoMesas == null) return;
        tlTipoMesas.clearOnTabSelectedListeners();
        tlTipoMesas.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (ajustandoTab) return;
                String tipo = tab.getPosition() == 1 ? ClaseMesas.TIPO_ESTADO_PEDIDOS : ClaseMesas.TIPO_ESTADO_MESAS;
                if (tipo.equals(tipoEstado)) return;
                tipoEstado = tipo;
                if (interfaceMesas != null) interfaceMesas.onCambiaTipoEstado(tipo);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    /** Alinea la pestaña con el tipo de estado que tiene la actividad, sin disparar una nueva peticion. */
    private void sincronizaTab(String tipo) {
        tipoEstado = tipo == null || tipo.isEmpty() ? ClaseMesas.TIPO_ESTADO_MESAS : tipo;
        if (tlTipoMesas == null) return;
        int pos = ClaseMesas.TIPO_ESTADO_PEDIDOS.equals(tipoEstado) ? 1 : 0;
        TabLayout.Tab tab = tlTipoMesas.getTabAt(pos);
        if (tab != null && tlTipoMesas.getSelectedTabPosition() != pos) {
            ajustandoTab = true;
            tab.select();
            ajustandoTab = false;
        }
    }

    public interface InterfaceMesas {
        Bundle cargaMesas(boolean refresca);
        void onClickMesa(int i,  int pax, String habitacion);
        /** el usuario cambia de pestaña (MESAS / PEDIDOS): se pide al servidor el estado de ese tipo */
        void onCambiaTipoEstado(String tipoEstado);
        void onLongClickMesa(int i);
        void actualizaPax(ArrayList<ClaseSubMesas> subMesas, int mesaSel);
    }

    InterfaceMesas interfaceMesas;


    public FragmentMesas newInstance() {
        FragmentMesas MesasFragment = new FragmentMesas();
        return MesasFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.fragmentActivity = (FragmentActivity)context;
            this.context = context;
            interfaceMesas = (InterfaceMesas) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.mesas_fragment,container,false);

        rvMesas = rootView. findViewById(R.id.rvMesas);
        //rvMesas.setHasFixedSize(true);
        estado = FragmentMesas.enEstado.seleccion;

        svMesa = rootView.findViewById(R.id.svMesa);
        tlTipoMesas = rootView.findViewById(R.id.tlTipoMesas);
        ivAvisoPedidos = rootView.findViewById(R.id.ivAvisoPedidos);
        configuraTabs();


        View view  = View.inflate(context,R.layout.mesas_cardview,null);
        view.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        float vpWidth = view.getMeasuredWidth()/displayMetrics.density;
        int cols = (int)( dpWidth /vpWidth);
        if (cols <= 1) cols = 2;
        columnasMesas = cols;


        GridLayoutManager llmMesas = new GridLayoutManager(context,cols, GridLayoutManager.VERTICAL, false);
        rvMesas.setLayoutManager(llmMesas);

        Bundle bundle = interfaceMesas.cargaMesas (false);
        pidePax = bundle.getBoolean("pidePax",false);
        pideRoom = bundle.getBoolean("pideRoom",false);
        mesaSel = bundle.getInt("productoSel",1);
        listaMesas= (TreeMap<Integer, ClaseMesas>) bundle.getSerializable("mesas");

        sincronizaTab(bundle.getString("tipoEstado", ClaseMesas.TIPO_ESTADO_MESAS));
        ArrayList<ClaseMesas> tmpListMesas = mesasDelTipo(listaMesas);
        ordenaLista(tmpListMesas);

        rvMesas.setAdapter(creaAdapter(tmpListMesas));

        setupSearchView();

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

    private void setupSearchView() {
        svMesa.setIconifiedByDefault(false);
        svMesa.setOnQueryTextListener(this);
        svMesa.setSubmitButtonEnabled(true);
        svMesa.setQueryHint(context.getResources().getString(R.string.svMesas));
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        filtra(s);
        return false;
    }

    private void mostrarSearch(){
        RecyclerView.LayoutManager layout = rvMesas.getLayoutManager();
        int min = ((LinearLayoutManager) layout).findFirstCompletelyVisibleItemPosition();
        int max = ((LinearLayoutManager) layout).findLastCompletelyVisibleItemPosition();
        if (listaMesas != null) {
            if (min <= 0 && max >= nMesasVisibles - 1)
                svMesa.setVisibility(View.VISIBLE);
            else
                svMesa.setVisibility(View.VISIBLE);
        }
    }

    private void muestraSearch() {
        RecyclerView.LayoutManager layout = rvMesas.getLayoutManager();
        int min = ((GridLayoutManager) layout).findFirstCompletelyVisibleItemPosition();
        int max = ((GridLayoutManager) layout).findLastCompletelyVisibleItemPosition();

        if (listaMesas != null) {
            if ((min <= 0) && (max >= nMesasVisibles-1)) {
                svMesa.setVisibility(View.GONE);
            }
            else{
                svMesa.setQuery("", false);
                svMesa.clearFocus();
                svMesa.setVisibility(View.VISIBLE);
            }
        }
        else
            svMesa.setVisibility(View.GONE);

    }
}
