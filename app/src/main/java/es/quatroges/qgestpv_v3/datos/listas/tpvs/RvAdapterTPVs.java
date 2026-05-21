package es.quatroges.qgestpv_v3.datos.listas.tpvs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.FragmentTPVs;
import es.quatroges.qgestpv_v3.R;

public class RvAdapterTPVs extends RecyclerView.Adapter<RvAdapterTPVs.CategoriaViewHolder> {



    private  FragmentTPVs.InterfaceTPVs interfaceTPVs;

    ArrayList<ClaseTPVs> listaTPVs;
    Context context;
    FragmentTPVs fragmentTPVs;

    public RvAdapterTPVs(Context context, ArrayList<ClaseTPVs> tpvs, FragmentTPVs fragmentTPVs){
        this.context = context;
        this.listaTPVs = tpvs;
        this.interfaceTPVs= (FragmentTPVs.InterfaceTPVs)context;
        this.fragmentTPVs = fragmentTPVs;
    }

    @Override
    public int getItemCount() {
        if (listaTPVs != null )
            return listaTPVs.size();
        else
            return  0;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tpv_cardview, viewGroup, false);
        CategoriaViewHolder pvh = new CategoriaViewHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder categoriaViewHolder,final  int i) {
        categoriaViewHolder.descripcion.setText(listaTPVs.get(i).descripcion);

        if (fragmentTPVs.tpvSel == i){
            categoriaViewHolder.fondo.setBackgroundResource(R.color.elementoSel);
        }
        else {
            categoriaViewHolder.fondo.setBackgroundResource(R.color.cvBorde);
        }


        categoriaViewHolder.fondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if (fragmentTPVs.tpvSel != i) {
                    fragmentTPVs.tpvSel = i;
                    notifyDataSetChanged();
                    interfaceTPVs.onClickTPV(i);
                //}

            }
        });
    }



    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView descripcion;
        FrameLayout fondo;
        LinearLayout laySel;

        CategoriaViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            descripcion = itemView.findViewById(R.id.descripcion);
            fondo = itemView.findViewById(R.id.fondo);
 //           laySel = itemView.findViewById(R.id.laySel);

        }


    }


}