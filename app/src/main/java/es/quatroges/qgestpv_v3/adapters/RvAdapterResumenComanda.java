package es.quatroges.qgestpv_v3.adapters;

import static android.view.View.GONE;
import static es.quatroges.qgestpv_v3.utils.ClaseUtils.ORDEN_COMANDA_BEBIDA;
import static es.quatroges.qgestpv_v3.utils.ClaseUtils.ORDEN_COMANDA_ENTRANTE;
import static es.quatroges.qgestpv_v3.utils.ClaseUtils.ORDEN_COMANDA_POSTRE;
import static es.quatroges.qgestpv_v3.utils.ClaseUtils.ORDEN_COMANDA_PRIMERO;
import static es.quatroges.qgestpv_v3.utils.ClaseUtils.ORDEN_COMANDA_SEGUNDO;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import es.quatroges.qgestpv_v3.R;
import es.quatroges.qgestpv_v3.datos.listas.lineaVentas.ClaseLineaVentas;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public class RvAdapterResumenComanda extends RecyclerView.Adapter<RvAdapterResumenComanda.CategoriaViewHolder> {


    static ArrayList<ClaseLineaVentas> listaLineaVentas;
    static Context context;

    public RvAdapterResumenComanda(Context context, ArrayList<ClaseLineaVentas> listaLineaVentas){
        this.context = context;
        this.listaLineaVentas= null;
        if (listaLineaVentas != null) {
            this.listaLineaVentas = new ArrayList<ClaseLineaVentas>(listaLineaVentas);
            Collections.sort(this.listaLineaVentas);
            this.listaLineaVentas = ClaseLineaVentas.agrupa(this.listaLineaVentas);
            int a = this.listaLineaVentas.size();
        }
    }


    @Override
    public int getItemCount() {
        if (listaLineaVentas!= null )
            return listaLineaVentas.size();
        else
            return  0;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.resumenlineaventas_cardview, viewGroup, false);
        CategoriaViewHolder pvh = new CategoriaViewHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder categoriaViewHolder,final  int i) {
        categoriaViewHolder.cantidad.setText(Integer.toString(listaLineaVentas.get(i).cantidad));
        categoriaViewHolder.descripcion.setText(listaLineaVentas.get(i).descripcion);
        categoriaViewHolder.descripcion.setTypeface(Typeface.DEFAULT);
        categoriaViewHolder.peuros.setText(ClaseUtils.double2string(listaLineaVentas.get(i).peuros,2));
        categoriaViewHolder.teuros.setText(ClaseUtils.double2string(listaLineaVentas.get(i).teuros,2));
        categoriaViewHolder.teuros.setTypeface(Typeface.DEFAULT);

        categoriaViewHolder.cv.setCardBackgroundColor(Color.WHITE);

        if (listaLineaVentas.get(i).linea_total == true){
            categoriaViewHolder.cantidad.setText("");
            categoriaViewHolder.descripcion.setText(listaLineaVentas.get(i).descripcion);
            categoriaViewHolder.descripcion.setTypeface(Typeface.DEFAULT_BOLD);
            categoriaViewHolder.peuros.setText("");
            categoriaViewHolder.teuros.setText(ClaseUtils.double2string(listaLineaVentas.get(i).teuros,2));
            categoriaViewHolder.teuros.setTypeface(Typeface.DEFAULT);
            categoriaViewHolder.cv.setCardBackgroundColor(Color.LTGRAY);
        }

        categoriaViewHolder.ivPension.setVisibility(GONE);
        if (!listaLineaVentas.get(i).pension.equalsIgnoreCase("")) {
            categoriaViewHolder.ivPension.setVisibility(View.VISIBLE);
        }


        switch (listaLineaVentas.get(i).orden_platos){
            case ORDEN_COMANDA_BEBIDA:
                categoriaViewHolder.ivTipo.setBackgroundDrawable(context.getDrawable(R.drawable.navmenu_bebida));
                break;
            case ORDEN_COMANDA_ENTRANTE:
                categoriaViewHolder.ivTipo.setBackgroundDrawable(context.getDrawable(R.drawable.navmenu_entrante));
                break;
            case ORDEN_COMANDA_PRIMERO:
                categoriaViewHolder.ivTipo.setBackgroundDrawable(context.getDrawable(R.drawable.navmenu_primero));
                break;
            case ORDEN_COMANDA_SEGUNDO:
                categoriaViewHolder.ivTipo.setBackgroundDrawable(context.getDrawable(R.drawable.navmenu_comida));
                break;
            case ORDEN_COMANDA_POSTRE:
                categoriaViewHolder.ivTipo.setBackgroundDrawable(context.getDrawable(R.drawable.navmenu_postre));
                break;
            default :
                categoriaViewHolder.ivTipo.setBackgroundDrawable(context.getDrawable(R.drawable.navmenu_entrante));
                break;

        }

    }



    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView cantidad;
        TextView descripcion;
        TextView peuros;
        TextView teuros;
        androidx.appcompat.widget.AppCompatImageView  ivTipo, ivPension;

        CategoriaViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            cantidad = itemView.findViewById(R.id.cantidad);
            descripcion = itemView.findViewById(R.id.descripcion);
            peuros = itemView.findViewById(R.id.peuros);
            teuros = itemView.findViewById(R.id.teuros);

            ivTipo= itemView.findViewById(R.id.ivTipo);
            ivPension= itemView.findViewById(R.id.ivPension);
        }
    }
}