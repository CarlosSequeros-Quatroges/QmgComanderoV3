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

import es.quatroges.qgestpv_v3.adapters.RvAdapterAlergenos;
import es.quatroges.qgestpv_v3.utils.ClaseItemFiltro;

public class FragmentDialogFiltroAlergenos extends Fragment {

    private static ArrayList<ClaseItemFiltro> alergenos;
    private static RvAdapterAlergenos adapterAlergenos;

    public static ArrayList<ClaseItemFiltro> getAlergenos() {
        return alergenos;
    }

    public interface InterfaceFrgDlgFiltAler {
        ArrayList<ClaseItemFiltro> flCargaDatosAlergenos();
    }

    InterfaceFrgDlgFiltAler interfaceFrgDlgFiltAler;

    public FragmentDialogFiltroAlergenos() {

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // check if parent Fragment implements listener
        if (getParentFragment() instanceof FragmentDialogFiltroAlergenos.InterfaceFrgDlgFiltAler) {
            interfaceFrgDlgFiltAler = (FragmentDialogFiltroAlergenos.InterfaceFrgDlgFiltAler) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment must implement OnChildFragmentInteractionListener");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_filtro_alergenos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         alergenos = interfaceFrgDlgFiltAler.flCargaDatosAlergenos();

        RecyclerView rvAlergenos;


        rvAlergenos = view.findViewById(R.id.rvAlergenos);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        rvAlergenos.setLayoutManager(gridLayoutManager);

        adapterAlergenos= new RvAdapterAlergenos(getContext(), alergenos) ;
        rvAlergenos.setAdapter(adapterAlergenos);

        ImageView ivBorrar = view.findViewById(R.id.ivBorrarAlergenos);
        ivBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(ClaseItemFiltro alergeno: alergenos){
                    alergeno.checked= false;
                }
                adapterAlergenos.notifyDataSetChanged();
            }
        });

    }

    public static FragmentDialogFiltroAlergenos newInstance(String text){
        FragmentDialogFiltroAlergenos f = new FragmentDialogFiltroAlergenos();
        Bundle b = new Bundle();
        b.putString("msg",text);
        f.setArguments(b);
        return f;
    }
}