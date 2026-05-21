package es.quatroges.qgestpv_v3.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.R;
import es.quatroges.qgestpv_v3.utils.ClaseItemFiltro;

//import androidx.core.app.INotificationSideChannel;

public class RvAdapterEtiquetas extends RecyclerView.Adapter<RvAdapterEtiquetas.CategoriaViewHolder> {

    static ArrayList<ClaseItemFiltro> listaItemEtiquetas;
    Context context;

    public RvAdapterEtiquetas(Context context, ArrayList<ClaseItemFiltro> listaetiquetas){
        this.context = context;
        this.listaItemEtiquetas = null;
        if (listaetiquetas != null) {
            this.listaItemEtiquetas = new ArrayList<ClaseItemFiltro>(listaetiquetas);
        }

    }


    @Override
    public int getItemCount() {
        if (listaItemEtiquetas != null )
            return listaItemEtiquetas.size();
        else
            return 0;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filtros_cardview, viewGroup, false);
        CategoriaViewHolder pvh = new CategoriaViewHolder(v, ((Activity) context));

        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoriaViewHolder categoriaViewHolder,final  int i) {

        categoriaViewHolder.chkItemFiltro.setChecked(listaItemEtiquetas.get(i).checked);
        categoriaViewHolder.chkItemFiltro.setText(listaItemEtiquetas.get(i).descripcion);


        categoriaViewHolder.chkItemFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaItemEtiquetas.get(i).checked = ! listaItemEtiquetas.get(i).checked ;
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder{
        CheckBox chkItemFiltro;

        CategoriaViewHolder(View itemView,final Activity activity) {
            super(itemView);
            chkItemFiltro = itemView.findViewById(R.id.chkItemFiltro);
        }



    }


}