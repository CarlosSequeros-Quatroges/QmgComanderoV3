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

public class RvAdapterOrdenPlatos extends RecyclerView.Adapter<RvAdapterOrdenPlatos.CategoriaViewHolder> {

    static ArrayList<ClaseItemFiltro> listaItemOrdenPlatos;
    Context context;

    public RvAdapterOrdenPlatos(Context context, ArrayList<ClaseItemFiltro> listaOrdenPlatos){
        this.context = context;
        this.listaItemOrdenPlatos = null;
        if (listaOrdenPlatos != null) {
            this.listaItemOrdenPlatos = new ArrayList<ClaseItemFiltro>(listaOrdenPlatos);
        }

    }


    @Override
    public int getItemCount() {
        if (listaItemOrdenPlatos != null )
            return listaItemOrdenPlatos.size();
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

        categoriaViewHolder.chkItemFiltro.setChecked(listaItemOrdenPlatos.get(i).checked);
        categoriaViewHolder.chkItemFiltro.setText(listaItemOrdenPlatos.get(i).descripcion);


        categoriaViewHolder.chkItemFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaItemOrdenPlatos.get(i).checked = ! listaItemOrdenPlatos.get(i).checked ;
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