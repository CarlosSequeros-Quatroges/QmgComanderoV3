package es.quatroges.qgestpv_v3;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.adapters.RvAdapterResumenComanda;
import es.quatroges.qgestpv_v3.datos.listas.lineaVentas.ClaseLineaVentas;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public class FragmentDialogFacturaLineas extends Fragment {

    public interface InterfaceFrgDlgFacLineas {
        ArrayList<ClaseLineaVentas> flCargaDatos();
    }

    InterfaceFrgDlgFacLineas interfaceFrgDlgFacLineas;

    public FragmentDialogFacturaLineas() {

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // check if parent Fragment implements listener
        if (getParentFragment() instanceof FragmentDialogFacturaLineas.InterfaceFrgDlgFacLineas) {
            interfaceFrgDlgFacLineas = (FragmentDialogFacturaLineas.InterfaceFrgDlgFacLineas) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment must implement OnChildFragmentInteractionListener");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_resumencomanda, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<ClaseLineaVentas> lineas = interfaceFrgDlgFacLineas.flCargaDatos();

        TextView tvUds, tvTotal;
        RecyclerView rvLineaVentas;
        RvAdapterResumenComanda adapterResumenLineaVentas;
        tvUds = view.findViewById(R.id.tvUds);
        tvTotal = view.findViewById(R.id.tvTotal);

        rvLineaVentas = view.findViewById(R.id.rvLineaVentas);
        GridLayoutManager llmLineaVentas = new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false);
        rvLineaVentas.setLayoutManager(llmLineaVentas);

        int uds = 0;
        double suma = 0;
        for (ClaseLineaVentas linea: lineas){
            uds += linea.cantidad;
            suma += linea.teuros;
        }
        tvTotal.setText(ClaseUtils.double2string(suma,2));
        tvUds.setText(String.valueOf(uds));

        adapterResumenLineaVentas= new RvAdapterResumenComanda(getContext(), lineas) ;
        rvLineaVentas.setAdapter(adapterResumenLineaVentas);

    }

    public static FragmentDialogFacturaLineas newInstance(String text){
        FragmentDialogFacturaLineas f = new FragmentDialogFacturaLineas();
        Bundle b = new Bundle();
        b.putString("msg",text);
        f.setArguments(b);
        return f;
    }
}