package es.quatroges.qgestpv_v3.datos.listas.clientesCtaCasa;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.FragmentFormaPago;
import es.quatroges.qgestpv_v3.R;

public class RvAdapterClientesCtaCasa extends RecyclerView.Adapter<RvAdapterClientesCtaCasa.ClientesCtaCasaViewHolder> {

    private FragmentFormaPago.InterfaceFormaPago interfaceFormaPago;

    ArrayList<ClaseClientesCtaCasa> listaClientesCtaCasa;
    Context context;
    FragmentFormaPago fragmentFormaPago;


    public RvAdapterClientesCtaCasa(Context context, ArrayList<ClaseClientesCtaCasa> clientesCtaCasa, FragmentFormaPago fragmentFormaPago){
        this.context = context;
        this.listaClientesCtaCasa = clientesCtaCasa;
        this.interfaceFormaPago = (FragmentFormaPago.InterfaceFormaPago)context;
        this.fragmentFormaPago = fragmentFormaPago;
    }

    public void actualiza() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (listaClientesCtaCasa != null )
            return listaClientesCtaCasa.size();
        else
            return  0;
    }

    @NonNull
    @Override
    public ClientesCtaCasaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.clientes_cta_casa_cardview, viewGroup, false);
        ClientesCtaCasaViewHolder pvh = new ClientesCtaCasaViewHolder(v, fragmentFormaPago.getActivity());

        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ClientesCtaCasaViewHolder clientesCtaCasaViewHolder, final  int i) {
        clientesCtaCasaViewHolder.nombre.setText(listaClientesCtaCasa.get(i).nombre);

        if (fragmentFormaPago.getClienteCtaCasaSel() == i){
            clientesCtaCasaViewHolder.fondo.setBackgroundResource(R.color.elementoSel);
        }
        else {
            clientesCtaCasaViewHolder.fondo.setBackgroundResource(R.color.cvBorde);
        }


        clientesCtaCasaViewHolder.fondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentFormaPago.setClienteCtaCasaSel(i);
                notifyDataSetChanged();
               // interfaceFormaPago.onClickClientesCtaCasa(i);

            }
        });
    }



    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ClientesCtaCasaViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView nombre;
        LinearLayout fondo;
        LinearLayout laySel;

        ClientesCtaCasaViewHolder(View itemView, final Activity activity) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            nombre = itemView.findViewById(R.id.nombre);
            fondo = itemView.findViewById(R.id.fondo);
 //           laySel = itemView.findViewById(R.id.laySel);
        }
    }
}