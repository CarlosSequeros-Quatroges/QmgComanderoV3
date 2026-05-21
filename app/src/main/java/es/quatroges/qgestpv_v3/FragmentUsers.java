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

import es.quatroges.qgestpv_v3.datos.listas.users.ClaseUsers;
import es.quatroges.qgestpv_v3.datos.listas.users.RvAdapterUsers;

public class FragmentUsers extends Fragment {

    FragmentActivity fragmentActivity;
    Context context;
    private ArrayList<ClaseUsers> listaUsers;
    public int userSel;
    public boolean claveCamarero;
    private RecyclerView rvUsers;

    public enum enEstado {
        seleccion, clave;
    }
    public enEstado estado;

    public void actualizaUsers() {
        if (interfaceUsers != null){
            Bundle bundle = interfaceUsers.cargaUsers();
            claveCamarero = bundle.getBoolean("claveCamareros",false);
            userSel = bundle.getInt("userSel",0);
            listaUsers= bundle.getParcelableArrayList("users");
            if (rvUsers != null) {
                rvUsers.setAdapter(null);
                RvAdapterUsers adapterUsers = new RvAdapterUsers(context, listaUsers, this);
                rvUsers.setAdapter(adapterUsers);

                estado = enEstado.seleccion;
            }
        }
    }


    public interface InterfaceUsers{
        Bundle cargaUsers();
        void onClickUser(int i);
    }

    InterfaceUsers interfaceUsers;


    public FragmentUsers newInstance() {
        FragmentUsers MesasFragment = new FragmentUsers();
        return MesasFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.fragmentActivity = (FragmentActivity)context;
            this.context = context;
            interfaceUsers = (InterfaceUsers) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.users_fragment,container,false);

        rvUsers = rootView. findViewById(R.id.rvUsers);
        //rvUsers.setHasFixedSize(true);
        estado = enEstado.seleccion;


        View view  = View.inflate(context,R.layout.user_cardview,null);
        view.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        float vpWidth = view.getMeasuredWidth()/displayMetrics.density;
        int cols = (int)( dpWidth /vpWidth);
        if (cols <= 1) cols = 2;

        GridLayoutManager llmUsers = new GridLayoutManager(context,cols, GridLayoutManager.VERTICAL, false);
        rvUsers.setLayoutManager(llmUsers);

        Bundle bundle = interfaceUsers.cargaUsers ();
        claveCamarero = bundle.getBoolean("claveCamareros",false);
        userSel = bundle.getInt("userSel",0);
        listaUsers= bundle.getParcelableArrayList("users");

        RvAdapterUsers adapterUsers = new RvAdapterUsers(context, listaUsers, this) ;
        rvUsers.setAdapter(adapterUsers);

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
