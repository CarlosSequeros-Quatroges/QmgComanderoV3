package es.quatroges.qgestpv_v3.datos.listas.facturas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.FragmentFacturas;
import es.quatroges.qgestpv_v3.R;

public class RvAdapterFacturas extends RecyclerView.Adapter<RvAdapterFacturas.CategoriaViewHolder>
        implements Filterable {


    private  FragmentFacturas.InterfaceFacturas interfaceFacturas;

    static ArrayList<ClaseFacturas> listaFacturas;
    static ArrayList<ClaseFacturas> listaFacturasFiltradas;
    Context context;
    FragmentFacturas fragmentFacturas;

    public RvAdapterFacturas(Context context, ArrayList<ClaseFacturas> facturas, FragmentFacturas fragmentFacturas){
        this.context = context;
        this.listaFacturas = null;
        if (facturas != null ) {
            this.listaFacturas = new ArrayList<ClaseFacturas>(facturas);
            this.listaFacturasFiltradas = new ArrayList<ClaseFacturas>(facturas);
        }

        this.interfaceFacturas= (FragmentFacturas.InterfaceFacturas)context;
        this.fragmentFacturas = fragmentFacturas;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listaFacturasFiltradas= listaFacturas;
                } else {
                    ArrayList<ClaseFacturas> filteredList = new ArrayList<>();
                    for (ClaseFacturas row : listaFacturas) {
                        if(String.valueOf(row.mesa).contains(charString)  || row.descripcion.toLowerCase().contains(charString)){
                            filteredList.add(row);
                        }
                    }

                    listaFacturasFiltradas= filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listaFacturasFiltradas;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                boolean ok = true;
                if (filterResults.values instanceof ArrayList<?>){
                    for (ClaseFacturas t:  (ArrayList<ClaseFacturas>) filterResults.values){
                        if (t instanceof  ClaseFacturas){

                        }
                        else {
                            ok = false;
                            break;
                        }
                    }
                }
                if (ok ) {
                    listaFacturasFiltradas = (ArrayList<ClaseFacturas>) filterResults.values;
                    notifyDataSetChanged();
                    /*
                    if (quitaFocoSvMesa)
                        fragmentMesas.swMesaClearFocus();

                    quitaFocoSvMesa = false;

                     */
                }
            }
        };
    }


    @Override
    public int getItemCount() {
        if (listaFacturasFiltradas != null )
            return listaFacturasFiltradas.size();
        else
            return  0;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.facturas_cardview, viewGroup, false);
        CategoriaViewHolder pvh = new CategoriaViewHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder categoriaViewHolder,final  int i) {
        categoriaViewHolder.tvNfactura.setText(String.valueOf(listaFacturasFiltradas.get(i).nfactura));
        if (listaFacturasFiltradas.get(i).nfactura < 0 ){
            categoriaViewHolder.tvNfactura.setText("Prof. "+String.valueOf(-1* listaFacturasFiltradas.get(i).nfactura));
        }
        String mesa =String.valueOf(listaFacturasFiltradas.get(i).mesa);
        if (listaFacturasFiltradas.get(i).descripcion != null && listaFacturasFiltradas.get(i).descripcion.trim().length() > 0 )
            mesa = listaFacturasFiltradas.get(i).descripcion;

        categoriaViewHolder.tvMesa.setText(mesa+"-"+String.valueOf(listaFacturasFiltradas.get(i).submesa));
        switch (listaFacturasFiltradas.get(i).formapago.toLowerCase()){
            case "e":
                categoriaViewHolder.tvFormapago.setText("Efe");
                break;
            case "h":
                categoriaViewHolder.tvFormapago.setText("Tar");
                break;
            case "c":
                categoriaViewHolder.tvFormapago.setText("Cta");
                break;
            case "r":
            case "p":
                categoriaViewHolder.tvFormapago.setText("Cre");
                break;
            case "z":
            case "x":
                categoriaViewHolder.tvFormapago.setText("E+T");
                break;
        }
        categoriaViewHolder.tvHora.setText(listaFacturasFiltradas.get(i).hora);

        categoriaViewHolder.ivVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaceFacturas.onClickFactura(listaFacturasFiltradas.get(i).codenl);
            }
        });

    }



    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        androidx.appcompat.widget.AppCompatImageView ivVer;
        TextView tvNfactura, tvMesa, tvFormapago,tvHora;

        CategoriaViewHolder(View itemView) {
            super(itemView);
            tvNfactura = itemView.findViewById(R.id.tvNfactura);
            tvMesa = itemView.findViewById(R.id.tvMesa);
            tvFormapago = itemView.findViewById(R.id.tvFormapago);
            tvHora = itemView.findViewById(R.id.tvHora);
            ivVer = itemView.findViewById(R.id.ivVer);
        }


    }


}