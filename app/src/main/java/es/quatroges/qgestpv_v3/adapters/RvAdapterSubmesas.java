package es.quatroges.qgestpv_v3.adapters;

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

import es.quatroges.qgestpv_v3.R;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public class RvAdapterSubmesas extends RecyclerView.Adapter<RvAdapterSubmesas.CategoriaViewHolder> {

    static ArrayList<Integer> listaSubmesas;
    Context context;

    public RvAdapterSubmesas(Context context, ArrayList<Integer> submesas){
        this.context = context;
        this.listaSubmesas = null;
        if (submesas != null) {
            this.listaSubmesas = new ArrayList<Integer>(submesas);
        }

    }


    @Override
    public int getItemCount() {
        if (listaSubmesas!= null )
            return listaSubmesas.size();
        else
            return 0;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.submesas_cardview, viewGroup, false);
        CategoriaViewHolder pvh = new CategoriaViewHolder(v, ((Activity) context));

        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoriaViewHolder categoriaViewHolder,final  int i) {

            categoriaViewHolder.fondo.setBackgroundResource(R.color.mesaLibre);
            categoriaViewHolder.numero.setText(Integer.toString(listaSubmesas.get(i)));

        if (ClaseUtils.PideUnidades.getSubmesaSel() == listaSubmesas.get(i)){
            categoriaViewHolder.laySel.setBackgroundResource(R.color.elementoSel);
        }
        else {
            categoriaViewHolder.laySel.setBackgroundResource(R.color.cvBorde);
        }

        categoriaViewHolder.fondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClaseUtils.PideUnidades.setSubmesaSel(i);

                notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView numero;
        LinearLayout fondo,laySel;


        CategoriaViewHolder(View itemView,final Activity activity) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            numero = itemView.findViewById(R.id.numero);
            fondo = itemView.findViewById(R.id.fondo);
            laySel = itemView.findViewById(R.id.laySel);

        }



    }


}