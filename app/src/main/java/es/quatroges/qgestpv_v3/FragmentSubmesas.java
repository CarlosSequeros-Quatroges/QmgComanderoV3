package es.quatroges.qgestpv_v3;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.datos.listas.mesas.ClaseSubMesas;
import es.quatroges.qgestpv_v3.datos.listas.mesas.RvAdapterSubmesas;

public class FragmentSubmesas extends Fragment{

    FragmentActivity fragmentActivity;
    Context context;
    private ArrayList<ClaseSubMesas> listaSubmesas;
    public int submesaSel;
    private RecyclerView rvSubmesas;

    public void actualizaSubmesas() {
        if (interfaceSubmesas != null){
            Bundle bundle = interfaceSubmesas.cargaSubmesas();
            submesaSel = bundle.getInt("submesaSel",0);
            listaSubmesas= bundle.getParcelableArrayList("submesas");

            rvSubmesas.setAdapter(null);
            RvAdapterSubmesas adapterSubmesas = new RvAdapterSubmesas(context, listaSubmesas, this) ;
            rvSubmesas.setAdapter(adapterSubmesas);
            if (submesaSel > -1) rvSubmesas.scrollToPosition(submesaSel);

        }
    }


    public interface InterfaceSubmesas{
        Bundle cargaSubmesas();
        void onClickSubmesa(int i);
    }

    InterfaceSubmesas interfaceSubmesas;


    public FragmentSubmesas newInstance() {
        FragmentSubmesas SubmesasFragment = new FragmentSubmesas();
        return SubmesasFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.fragmentActivity = (FragmentActivity)context;
            this.context = context;
            interfaceSubmesas = (InterfaceSubmesas) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.submesas_fragment,container,false);

        rvSubmesas = rootView. findViewById(R.id.rvSubmesas);

        LinearLayoutManager llmMesas = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false);
        rvSubmesas.setLayoutManager(llmMesas);

        Bundle bundle = interfaceSubmesas.cargaSubmesas ();
        submesaSel = bundle.getInt("submesaSel");
        listaSubmesas= bundle.getParcelableArrayList("submesas");

        RvAdapterSubmesas adapterSubmesas = new RvAdapterSubmesas(context, listaSubmesas, this) ;
        rvSubmesas.setAdapter(adapterSubmesas);

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


}
