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

    /** pestañas MESAS / PEDIDOS: tipo de estado que se muestra (ClaseMesas.TIPO_ESTADO_*) */
    private TabLayout tlTipoMesas;
    private String tipoEstado = ClaseMesas.TIPO_ESTADO_MESAS;
    private boolean ajustandoTab;
    private int nMesasVisibles;


    public enum enEstado {
        seleccion, datos;
    }
    public FragmentMesas.enEstado estado;

    public void filtrarItem(String mesa){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapterMesas.setQuitaFocoSvMesa(true);
                adapterMesas.getFilter().filter(mesa);

            }
        }, 50);
        //adapterMesas.setQuitaFocoSvMesa(true);
        adapterMesas.getFilter().filter("");

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
            ordenaMesas(tmpListMesas);

            adapterMesas = new RvAdapterMesas(context, tmpListMesas, this) ;
            rvMesas.setAdapter(adapterMesas);
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
        if (interfaceMesas == null || rvMesas == null || adapterMesas == null || estado != enEstado.seleccion) return;
        Bundle bundle = interfaceMesas.cargaMesas(true);
        listaMesas = (TreeMap<Integer, ClaseMesas>) bundle.getSerializable("mesas");
        sincronizaTab(bundle.getString("tipoEstado", ClaseMesas.TIPO_ESTADO_MESAS));
        ArrayList<ClaseMesas> tmpListMesas = mesasDelTipo(listaMesas);
        ordenaMesas(tmpListMesas);

        CharSequence filtro = svMesa != null ? svMesa.getQuery() : null;
        Parcelable scroll = rvMesas.getLayoutManager() != null ? rvMesas.getLayoutManager().onSaveInstanceState() : null;
        adapterMesas = new RvAdapterMesas(context, tmpListMesas, this);
        rvMesas.setAdapter(adapterMesas);
        if (filtro != null && filtro.length() > 0) adapterMesas.getFilter().filter(filtro);
        if (scroll != null && rvMesas.getLayoutManager() != null) rvMesas.getLayoutManager().onRestoreInstanceState(scroll);
    }

    /** Mesas de la pestaña activa: las del tpv (numero < 1000) en MESAS, los pedidos (>= 1000) en PEDIDOS. */
    private ArrayList<ClaseMesas> mesasDelTipo(TreeMap<Integer, ClaseMesas> mesas) {
        ArrayList<ClaseMesas> lista = new ArrayList<>();
        boolean pedidos = ClaseMesas.TIPO_ESTADO_PEDIDOS.equals(tipoEstado);
        if (mesas != null) {
            for (Map.Entry<Integer, ClaseMesas> entry : mesas.entrySet()) {
                if (entry.getValue().esPedido() == pedidos) lista.add(entry.getValue());
            }
        }
        nMesasVisibles = lista.size();
        return lista;
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
        configuraTabs();


        View view  = View.inflate(context,R.layout.mesas_cardview,null);
        view.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        float vpWidth = view.getMeasuredWidth()/displayMetrics.density;
        int cols = (int)( dpWidth /vpWidth);
        if (cols <= 1) cols = 2;


        GridLayoutManager llmMesas = new GridLayoutManager(context,cols, GridLayoutManager.VERTICAL, false);
        rvMesas.setLayoutManager(llmMesas);

        Bundle bundle = interfaceMesas.cargaMesas (false);
        pidePax = bundle.getBoolean("pidePax",false);
        pideRoom = bundle.getBoolean("pideRoom",false);
        mesaSel = bundle.getInt("productoSel",1);
        listaMesas= (TreeMap<Integer, ClaseMesas>) bundle.getSerializable("mesas");

        sincronizaTab(bundle.getString("tipoEstado", ClaseMesas.TIPO_ESTADO_MESAS));
        ArrayList<ClaseMesas> tmpListMesas = mesasDelTipo(listaMesas);
        ordenaMesas(tmpListMesas);

        adapterMesas = new RvAdapterMesas(context, tmpListMesas, this) ;
        rvMesas.setAdapter(adapterMesas);

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
        adapterMesas.getFilter().filter(s);
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
