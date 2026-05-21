package es.quatroges.qgestpv_v3.datos.listas.cabeceras;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.FragmentCabeceras;
import es.quatroges.qgestpv_v3.R;

public class RvAdapterCabeceras extends RecyclerView.Adapter<RvAdapterCabeceras.CategoriaViewHolder>
implements Filterable {


    private  FragmentCabeceras.InterfaceCabeceras interfaceCabeceras;

    static ArrayList<ClaseCabeceras> listaCabeceras, listaCabecerasFiltradas;
    Context context;
    FragmentCabeceras fragmentCabeceras;

    public RvAdapterCabeceras(Context context, ArrayList<ClaseCabeceras> cabeceras, FragmentCabeceras fragmentCabeceras){
        this.context = context;
        this.listaCabeceras = null;
        this.listaCabecerasFiltradas = null;
        if (cabeceras != null ) {
            this.listaCabeceras = new ArrayList<ClaseCabeceras>(cabeceras);
            this.listaCabecerasFiltradas = new ArrayList<ClaseCabeceras>(cabeceras);

        }

        this.interfaceCabeceras= (FragmentCabeceras.InterfaceCabeceras)context;
        this.fragmentCabeceras = fragmentCabeceras;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listaCabecerasFiltradas= listaCabeceras;
                } else {
                    ArrayList<ClaseCabeceras> filteredList = new ArrayList<>();
                    for (ClaseCabeceras row : listaCabeceras) {
                        if(String.valueOf(row.descripcion).toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(row);
                        }
                    }

                    listaCabecerasFiltradas= filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listaCabecerasFiltradas;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listaCabecerasFiltradas= (ArrayList<ClaseCabeceras>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        if (listaCabecerasFiltradas != null )
            return listaCabecerasFiltradas.size();
        else
            return  0;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cabeceras_cardview, viewGroup, false);
        CategoriaViewHolder pvh = new CategoriaViewHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder categoriaViewHolder,final  int i) {
        categoriaViewHolder.descripcion.setText(listaCabecerasFiltradas.get(i).descripcion);

        if (fragmentCabeceras.cabeceraSel == i){
            categoriaViewHolder.fondo.setBackgroundResource(R.color.elementoSel);
        }
        else {
            categoriaViewHolder.fondo.setBackgroundResource(R.color.transparente);
        }


        categoriaViewHolder.fondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                interfaceCabeceras.onClickCabecera(listaCabecerasFiltradas.get(i).codigo);
                fragmentCabeceras.cancelTimer();
                fragmentCabeceras.reduceCabeceras(i);
                interfaceCabeceras.fc_ocultaElementos(true);
                notifyDataSetChanged();

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
        TextView precio;
        FrameLayout fondo;

        CategoriaViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            descripcion = itemView.findViewById(R.id.descripcion);
            precio = itemView.findViewById(R.id.precio);
            fondo = itemView.findViewById(R.id.fondo);

        }


    }


}