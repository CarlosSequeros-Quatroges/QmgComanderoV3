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

import es.quatroges.qgestpv_v3.adapters.RvAdapterExtras;
import es.quatroges.qgestpv_v3.utils.ClaseItemExtra;

public class FragmentDialogExtras extends Fragment {

    private static ArrayList<ClaseItemExtra> extras;
    private static RvAdapterExtras adapterExtras;

    public static ArrayList<ClaseItemExtra> getExtras() {
        return extras;
    }

    public interface InterfaceFrgDlgExtras {
        ArrayList<ClaseItemExtra> exCargaDatosExtras();
    }

    InterfaceFrgDlgExtras interfaceFrgDlgExtras;

    public FragmentDialogExtras() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof InterfaceFrgDlgExtras) {
            interfaceFrgDlgExtras = (InterfaceFrgDlgExtras) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment must implement OnChildFragmentInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_extras, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        extras = interfaceFrgDlgExtras.exCargaDatosExtras();

        RecyclerView rvExtras = view.findViewById(R.id.rvExtras);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        rvExtras.setLayoutManager(gridLayoutManager);

        adapterExtras = new RvAdapterExtras(getContext(), extras);
        rvExtras.setAdapter(adapterExtras);

        ImageView ivBorrar = view.findViewById(R.id.ivBorrarExtras);
        ivBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (extras != null) {
                    for (ClaseItemExtra extra : extras) {
                        extra.estadoExtra = ClaseItemExtra.ESTADO_NADA;
                    }
                }
                adapterExtras.notifyDataSetChanged();
            }
        });
    }

    public static FragmentDialogExtras newInstance(String text) {
        FragmentDialogExtras f = new FragmentDialogExtras();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }
}
