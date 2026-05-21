package es.quatroges.qgestpv_v3;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.adapters.RvAdapterOrdenPlatos;
import es.quatroges.qgestpv_v3.utils.ClaseItemFiltro;

public class FragmentDialogFiltroOrdenPlatos extends Fragment {

    private static ArrayList<ClaseItemFiltro> ordenPlatos;
    private static  RvAdapterOrdenPlatos adapterOrdenPlatos;

    public static ArrayList<ClaseItemFiltro> getOrdenPlatos() {
        return ordenPlatos;
    }

    public interface InterfaceFrgDlgFiltOrden {
        ArrayList<ClaseItemFiltro> flCargaDatosOrdenPlatos();
    }

    InterfaceFrgDlgFiltOrden interfaceFrgDlgFiltOrden;

    public FragmentDialogFiltroOrdenPlatos() {

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // check if parent Fragment implements listener
        if (getParentFragment() instanceof InterfaceFrgDlgFiltOrden) {
            interfaceFrgDlgFiltOrden = (InterfaceFrgDlgFiltOrden) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment must implement OnChildFragmentInteractionListener");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_filtro_orden_platos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ordenPlatos = interfaceFrgDlgFiltOrden.flCargaDatosOrdenPlatos();

        RecyclerView rvOrdenPlatos;

        rvOrdenPlatos = view.findViewById(R.id.rvOrdenPlatos);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false);
        rvOrdenPlatos.setLayoutManager(gridLayoutManager);


        adapterOrdenPlatos= new RvAdapterOrdenPlatos(getContext(), ordenPlatos) ;
        rvOrdenPlatos.setAdapter(adapterOrdenPlatos);

        ImageView ivBorrar = view.findViewById(R.id.ivBorrarOrdenPlatos);
        ivBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(ClaseItemFiltro ordenPlato: ordenPlatos){
                    ordenPlato.checked= false;
                }
                adapterOrdenPlatos.notifyDataSetChanged();
            }
        });
    }

    public static FragmentDialogFiltroOrdenPlatos newInstance(String text){
        FragmentDialogFiltroOrdenPlatos f = new FragmentDialogFiltroOrdenPlatos();
        Bundle b = new Bundle();
        b.putString("msg",text);
        f.setArguments(b);
        return f;
    }
}