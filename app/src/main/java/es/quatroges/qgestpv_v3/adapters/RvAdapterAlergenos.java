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

public class RvAdapterAlergenos extends RecyclerView.Adapter<RvAdapterAlergenos.CategoriaViewHolder> {

    static ArrayList<ClaseItemFiltro> listaItemAlergenos;
    Context context;

    public RvAdapterAlergenos(Context context, ArrayList<ClaseItemFiltro> listaAlergenos){
        this.context = context;
        this.listaItemAlergenos = null;
        if (listaAlergenos != null) {
            this.listaItemAlergenos = new ArrayList<ClaseItemFiltro>(listaAlergenos);
        }

    }


    @Override
    public int getItemCount() {
        if (listaItemAlergenos != null )
            return listaItemAlergenos.size();
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

        categoriaViewHolder.chkItemFiltro.setChecked(listaItemAlergenos.get(i).checked );
        categoriaViewHolder.chkItemFiltro.setText(listaItemAlergenos.get(i).descripcion);
        
        categoriaViewHolder.chkItemFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaItemAlergenos.get(i).checked = ! listaItemAlergenos.get(i).checked ;
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