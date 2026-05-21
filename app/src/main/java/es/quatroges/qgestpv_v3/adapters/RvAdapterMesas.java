package es.quatroges.qgestpv_v3.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;
import java.util.TreeMap;

import es.quatroges.qgestpv_v3.R;
import es.quatroges.qgestpv_v3.datos.listas.mesas.ClaseMesas;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public class RvAdapterMesas extends RecyclerView.Adapter<RvAdapterMesas.CategoriaViewHolder> {

    static TreeMap<Integer,ClaseMesas> listaMesas;
    static TreeMap<Integer,ClaseMesas> listaMesasFiltradas;

    static Integer[] keyMesas;
    static ClaseMesas[] descMesas;
    static int mesaActual;
    static boolean quitaFocoSvMesa;
    static ClaseUtils.TraspasaMesa traspasaMesa;
    Context context;

    public RvAdapterMesas(Context context, TreeMap<Integer,ClaseMesas> mesas, int mesaActual, ClaseUtils.TraspasaMesa traspasaMesa){
        this.context = context;
        this.listaMesas = null;
        this.listaMesasFiltradas = null;
        if (mesas != null) {
            this.listaMesas = new TreeMap<Integer, ClaseMesas>(mesas);

            this.listaMesasFiltradas = new TreeMap<Integer,ClaseMesas>(mesas);

            this.keyMesas =  listaMesasFiltradas.keySet().toArray(new Integer[listaMesasFiltradas.size()]);
            this.descMesas =  listaMesasFiltradas.values().toArray(new ClaseMesas[listaMesasFiltradas.size()]);
            this.mesaActual = mesaActual;
        }
        this.traspasaMesa = traspasaMesa;

    }

    public void setQuitaFocoSvMesa(boolean quitaFocoSvMesa) {
        this.quitaFocoSvMesa = quitaFocoSvMesa;
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listaMesasFiltradas= listaMesas;
                } else {
                    TreeMap<Integer,ClaseMesas> filteredList = new TreeMap<>();
                    for (Map.Entry<Integer,ClaseMesas> row: listaMesas.entrySet()) {
                        if(String.valueOf(row.getValue().mesa).contains(charString) ||
                                String.valueOf(row.getValue().descripcion).toUpperCase().contains(charString.toUpperCase())){
                            filteredList.put(row.getKey(),row.getValue());
                        }
                    }

                    listaMesasFiltradas= filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listaMesasFiltradas;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                boolean ok = true;
                if (filterResults.values instanceof TreeMap<?,?>){
                    for (Map.Entry<Integer,ClaseMesas> t:  ((TreeMap<Integer,ClaseMesas>) filterResults.values).entrySet()){
                        if (t.getValue() instanceof  ClaseMesas){

                        }
                        else {
                            ok = false;
                            break;
                        }
                    }
                }
                if (ok ) {
                    listaMesasFiltradas = (TreeMap<Integer, ClaseMesas>) filterResults.values;

                   RvAdapterMesas.keyMesas =  listaMesasFiltradas.keySet().toArray(new Integer[listaMesasFiltradas.size()]);
                   RvAdapterMesas.descMesas =  listaMesasFiltradas.values().toArray(new ClaseMesas[listaMesasFiltradas.size()]);


                    notifyDataSetChanged();
                    if (quitaFocoSvMesa)
                        traspasaMesa.swMesaClearFocus();

                    quitaFocoSvMesa = false;
                }
            }
        };
    }


    @Override
    public int getItemCount() {
        if (listaMesas!= null )
            return listaMesas.size();
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
        if (i >= keyMesas.length) {
            categoriaViewHolder.cv.setVisibility(View.GONE);
            return;
        }
        categoriaViewHolder.cv.setVisibility(View.VISIBLE);
        if (keyMesas[i] == mesaActual)
            categoriaViewHolder.fondo.setBackgroundResource(R.color.mesaActual);
        else if (descMesas[i].abiertas == true || descMesas[i].tickets == true)
            categoriaViewHolder.fondo.setBackgroundResource(R.color.mesaOcupada);

        categoriaViewHolder.numero.setText(descMesas[i].descripcion);

        if (ClaseUtils.TraspasaMesa.getMesaSel() == keyMesas[i]) {
            categoriaViewHolder.laySel.setBackgroundResource(R.color.elementoSel);
        } else {
            categoriaViewHolder.laySel.setBackgroundResource(R.color.cvBorde);
        }

        categoriaViewHolder.fondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyMesas[i] == mesaActual)
                    return;
                else {
                    ClaseUtils.TraspasaMesa.setMesaSel(keyMesas[i]);
                    notifyDataSetChanged();
                }
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