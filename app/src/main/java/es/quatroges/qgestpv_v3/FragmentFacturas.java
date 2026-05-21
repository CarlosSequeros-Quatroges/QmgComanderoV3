package es.quatroges.qgestpv_v3;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import es.quatroges.qgestpv_v3.datos.listas.facturas.ClaseFacturas;
import es.quatroges.qgestpv_v3.datos.listas.facturas.RvAdapterFacturas;

public class FragmentFacturas extends Fragment
        implements  SearchView.OnQueryTextListener{

    FragmentActivity fragmentActivity;
    Context context;




    private static ArrayList<ClaseFacturas> listaFacturas;
    private static RecyclerView rvFacturas;
    private static RvAdapterFacturas adapterFacturas;
    private static TextView tvNfactura,tvMesa,tvPago,tvHora;
    private static ImageView ivVolver;

    private androidx.appcompat.widget.SearchView svMesa;

    public void actualizaFacturas() {
        if (interfaceFacturas != null) {
            ClaseFacturas.orden = ClaseFacturas.enOrden.hora;
            Bundle bundle = interfaceFacturas.cargaFacturas();
            listaFacturas = bundle.getParcelableArrayList("facturas");
            if (listaFacturas == null) listaFacturas = new ArrayList<>();
           cargaViewFacturas();
           formatoCabeceras();

            svMesa.setQuery("",false);
            svMesa.clearFocus();
        }
    }


    public interface InterfaceFacturas {
        Bundle cargaFacturas();
        void onClickFactura(int i);
        void onClickVolver();
    }

    InterfaceFacturas interfaceFacturas;


    public FragmentFacturas newInstance() {
        FragmentFacturas categoriasFragment = new FragmentFacturas();
        return categoriasFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.fragmentActivity = (FragmentActivity)context;
            this.context = context;
            interfaceFacturas = (InterfaceFacturas) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        adapterFacturas.getFilter().filter(s);
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.facturas_fragment,container,false);

        svMesa = rootView.findViewById(R.id.svMesa);


        tvNfactura = rootView.findViewById(R.id.tvNfactura);
        tvMesa = rootView.findViewById(R.id.tvMesa);
        tvPago = rootView.findViewById(R.id.tvFormapago);
        tvHora  = rootView.findViewById(R.id.tvHora);
        rvFacturas = rootView.findViewById(R.id.rvFacturas);
        ivVolver = rootView.findViewById(R.id.ivVolver);

        //rvFacturas.setHasFixedSize(true);

        Bundle bundle = interfaceFacturas.cargaFacturas();

        listaFacturas = bundle.getParcelableArrayList("facturas");
        if (listaFacturas == null) listaFacturas = new ArrayList<>();

        ClaseFacturas.orden = ClaseFacturas.enOrden.hora;
        ClaseFacturas.asc = false;
        cargaViewFacturas();
        formatoCabeceras();

        tvNfactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ClaseFacturas.orden == ClaseFacturas.enOrden.factura){
                    ClaseFacturas.asc = ! ClaseFacturas.asc;
                }
                else {
                    ClaseFacturas.orden = ClaseFacturas.enOrden.factura;
                    ClaseFacturas.asc = false;
                }
                cargaViewFacturas();
                formatoCabeceras();
            }
        });

        tvMesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ClaseFacturas.orden == ClaseFacturas.enOrden.mesa){
                    ClaseFacturas.asc = ! ClaseFacturas.asc;
                }
                else {
                    ClaseFacturas.orden = ClaseFacturas.enOrden.mesa;
                    ClaseFacturas.asc = true;
                }
                cargaViewFacturas();
                formatoCabeceras();
            }
        });

        tvPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ClaseFacturas.orden == ClaseFacturas.enOrden.pago){
                    ClaseFacturas.asc = ! ClaseFacturas.asc;
                }
                else {
                    ClaseFacturas.orden = ClaseFacturas.enOrden.pago;
                    ClaseFacturas.asc = true;
                }
                cargaViewFacturas();
                formatoCabeceras();
            }
        });

        tvHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ClaseFacturas.orden == ClaseFacturas.enOrden.hora){
                    ClaseFacturas.asc = ! ClaseFacturas.asc;
                }
                else {

                    ClaseFacturas.orden = ClaseFacturas.enOrden.hora;
                    ClaseFacturas.asc = false;
                }
                cargaViewFacturas();
                formatoCabeceras();
            }
        });

        ivVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaceFacturas.onClickVolver();
            }
        });

        setupSearchView();
        return rootView;

    }

    private void formatoCabeceras(){
        tvNfactura.setTypeface(null, Typeface.NORMAL);
        tvMesa.setTypeface(null, Typeface.NORMAL);
        tvPago.setTypeface(null, Typeface.NORMAL);
        tvHora.setTypeface(null, Typeface.NORMAL);
        int color = context.getResources().getColor(R.color.textoBlanco);
        tvNfactura.setTextColor( color);
        tvMesa.setTextColor( color);
        tvPago.setTextColor( color);
        tvHora.setTextColor( color);

        int colorSet = context.getResources().getColor(R.color.textoAzul);
        if (!ClaseFacturas.asc)
            colorSet = context.getResources().getColor(R.color.colorAccent);

        if (ClaseFacturas.orden == ClaseFacturas.enOrden.factura){
            tvNfactura.setTypeface(null, Typeface.BOLD);
            tvNfactura.setTextColor( colorSet);

        }
        else if (ClaseFacturas.orden == ClaseFacturas.enOrden.mesa){
            tvMesa.setTypeface(null, Typeface.BOLD);
            tvMesa.setTextColor( colorSet);

        }
        else if (ClaseFacturas.orden == ClaseFacturas.enOrden.pago){
            tvPago.setTypeface(null, Typeface.BOLD);
            tvPago.setTextColor( colorSet);

        }
        else {
            tvHora.setTypeface(null, Typeface.BOLD);
            tvHora.setTextColor( colorSet);

        }



    }

    private void  cargaViewFacturas(){
        try {
            Collections.sort(listaFacturas);
        }
        catch (IllegalArgumentException e){
            Log.d("FragmentFacturas", "cargaViewFacturas: ");
        }
        adapterFacturas = new RvAdapterFacturas(context, listaFacturas, this) ;
        rvFacturas.setAdapter(adapterFacturas);

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
