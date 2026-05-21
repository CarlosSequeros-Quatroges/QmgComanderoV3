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

import es.quatroges.qgestpv_v3.adapters.RvAdapterEtiquetas;
import es.quatroges.qgestpv_v3.utils.ClaseItemFiltro;

public class FragmentDialogFiltroEtiquetas extends Fragment {
    private static  ArrayList<ClaseItemFiltro> etiquetas;
    private static RvAdapterEtiquetas adapterEtiquetas;

    public static ArrayList<ClaseItemFiltro> getEtiquetas() {
        return etiquetas;
    }

    public interface InterfaceFrgDlgFiltEtiq {
        ArrayList<ClaseItemFiltro> flCargaDatosEtiquetas();
    }

    InterfaceFrgDlgFiltEtiq interfaceFrgDlgFiltEtiq;

    public FragmentDialogFiltroEtiquetas() {

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // check if parent Fragment implements listener
        if (getParentFragment() instanceof InterfaceFrgDlgFiltEtiq) {
            interfaceFrgDlgFiltEtiq = (InterfaceFrgDlgFiltEtiq) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment must implement OnChildFragmentInteractionListener");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_filtro_etiquetas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       etiquetas = interfaceFrgDlgFiltEtiq.flCargaDatosEtiquetas();

        RecyclerView rvEtiquetas;


        rvEtiquetas = view.findViewById(R.id.rvEtiquetas);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        rvEtiquetas.setLayoutManager(gridLayoutManager);

        adapterEtiquetas= new RvAdapterEtiquetas(getContext(), etiquetas) ;
        rvEtiquetas.setAdapter(adapterEtiquetas);

        ImageView ivBorrar = view.findViewById(R.id.ivBorrarEtiquetas);
        ivBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(ClaseItemFiltro etiqueta: etiquetas){
                    etiqueta.checked= false;
                }
                adapterEtiquetas.notifyDataSetChanged();
            }
        });

    }

    public static FragmentDialogFiltroEtiquetas newInstance(String text){
        FragmentDialogFiltroEtiquetas f = new FragmentDialogFiltroEtiquetas();
        Bundle b = new Bundle();
        b.putString("msg",text);
        f.setArguments(b);
        return f;
    }
}