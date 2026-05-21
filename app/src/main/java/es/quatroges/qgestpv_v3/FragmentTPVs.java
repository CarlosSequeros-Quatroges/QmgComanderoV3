package es.quatroges.qgestpv_v3;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.datos.listas.tpvs.ClaseTPVs;
import es.quatroges.qgestpv_v3.datos.listas.tpvs.RvAdapterTPVs;

public class FragmentTPVs extends Fragment{

    FragmentActivity fragmentActivity;
    Context context;
    private ArrayList<ClaseTPVs> listaTPVs;
    public int tpvSel;
    private RecyclerView rvTPVs;

    public void actualizaTPVs() {
        if (interfaceTPVs != null){
            Bundle bundle = interfaceTPVs.cargaTPVs();
            tpvSel = bundle.getInt("tpvSel",0);
            listaTPVs= bundle.getParcelableArrayList("tpvs");

            rvTPVs.setAdapter(null);
            RvAdapterTPVs adapterTPVs = new RvAdapterTPVs(context, listaTPVs, this) ;
            rvTPVs.setAdapter(adapterTPVs);
        }
    }


    public interface InterfaceTPVs{
        Bundle cargaTPVs();
        void onClickTPV(int i);
    }

    InterfaceTPVs interfaceTPVs;


    public FragmentTPVs newInstance() {
        FragmentTPVs MesasFragment = new FragmentTPVs();
        return MesasFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.fragmentActivity = (FragmentActivity)context;
            this.context = context;
            interfaceTPVs = (InterfaceTPVs) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.tpv_fragment,container,false);

        rvTPVs = rootView. findViewById(R.id.rvTPVs);

        View view  = View.inflate(context,R.layout.tpv_cardview,null);
        view.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        float vpWidth = view.getMeasuredWidth()/displayMetrics.density;
        int cols = (int)( dpWidth /vpWidth);
        if (cols <= 1) cols = 2;


        GridLayoutManager llmMesas = new GridLayoutManager(context,cols, GridLayoutManager.VERTICAL, false);
        rvTPVs.setLayoutManager(llmMesas);

        Bundle bundle = interfaceTPVs.cargaTPVs ();
        tpvSel = bundle.getInt("tpvSel",0);
        listaTPVs= bundle.getParcelableArrayList("tpvs");

        RvAdapterTPVs adapterTPVs = new RvAdapterTPVs(context, listaTPVs, this) ;
        rvTPVs.setAdapter(adapterTPVs);

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
