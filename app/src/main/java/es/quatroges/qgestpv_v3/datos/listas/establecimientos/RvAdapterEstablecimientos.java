package es.quatroges.qgestpv_v3.datos.listas.establecimientos;

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

public class RvAdapterEstablecimientos extends RecyclerView.Adapter<RvAdapterEstablecimientos.EstablecimientosViewHolder> {

    private FragmentFormaPago.InterfaceFormaPago interfaceFormaPago;

    ArrayList<ClaseEstablecimientos> listaEstblecimientos;
    Context context;
    FragmentFormaPago fragmentFormaPago;


    public RvAdapterEstablecimientos(Context context, ArrayList<ClaseEstablecimientos> establecimientos, FragmentFormaPago fragmentFormaPago){
        this.context = context;
        this.listaEstblecimientos = establecimientos;
        this.interfaceFormaPago = (FragmentFormaPago.InterfaceFormaPago)context;
        this.fragmentFormaPago = fragmentFormaPago;
    }

    public void actualiza() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (listaEstblecimientos != null )
            return listaEstblecimientos.size();
        else
            return  0;
    }

    @NonNull
    @Override
    public EstablecimientosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.clientes_cta_casa_cardview, viewGroup, false);
        EstablecimientosViewHolder pvh = new EstablecimientosViewHolder(v, fragmentFormaPago.getActivity());

        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final EstablecimientosViewHolder establecimientosViewHolder, final  int i) {
        establecimientosViewHolder.nombre.setText(listaEstblecimientos.get(i).nombre);

        if (fragmentFormaPago.getEstablecimientoSel() == i){
            establecimientosViewHolder.fondo.setBackgroundResource(R.color.elementoSel);
        }
        else {
            establecimientosViewHolder.fondo.setBackgroundResource(R.color.cvBorde);
        }


        establecimientosViewHolder.fondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentFormaPago.setEstablecimientoSel(i);
                notifyDataSetChanged();
            }
        });
    }



    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class EstablecimientosViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView nombre;
        LinearLayout fondo;

        EstablecimientosViewHolder(View itemView, final Activity activity) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            nombre = itemView.findViewById(R.id.nombre);
            fondo = itemView.findViewById(R.id.fondo);
        }
    }
}