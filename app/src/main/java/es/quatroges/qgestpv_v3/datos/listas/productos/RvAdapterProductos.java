package es.quatroges.qgestpv_v3.datos.listas.productos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
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
import java.util.Collections;

import es.quatroges.qgestpv_v3.FragmentProductos;
import es.quatroges.qgestpv_v3.R;

public class RvAdapterProductos extends RecyclerView.Adapter<RvAdapterProductos.CategoriaViewHolder>
implements Filterable {

    private  FragmentProductos.InterfaceProductos interfaceProductos;

    static ArrayList<ClaseProductos> listaProductos;
    static ArrayList<ClaseProductos> listaProductosFiltrados;
    static boolean ordenaCodigo;
    Context context;
    FragmentProductos fragmentProductos;

    public static void setListaProductos(ArrayList<ClaseProductos> listaProductos) {
        RvAdapterProductos.listaProductos = new ArrayList<ClaseProductos>(listaProductos);
    }

    public void  ordenar(CharSequence query) {
        getFilter().filter(query);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Collections.sort(listaProductosFiltrados);
                notifyDataSetChanged();
            }
        }, 100);

    }

    public RvAdapterProductos(Context context, ArrayList<ClaseProductos> productos, FragmentProductos fragmentProductos){
        this.context = context;
        this.listaProductos = null;
        this.listaProductosFiltrados = null;
        if (productos != null){
            this.listaProductos = new ArrayList<ClaseProductos>(productos);
            this.listaProductosFiltrados = new ArrayList<ClaseProductos>(productos);
        }
        this.interfaceProductos = (FragmentProductos.InterfaceProductos)context;
        this.fragmentProductos = fragmentProductos;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listaProductosFiltrados= listaProductos;
                } else {
                    ArrayList<ClaseProductos> filteredList = new ArrayList<>();
                    for (ClaseProductos row : listaProductos) {
                        if(String.valueOf(row.descripcion).toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(row);
                        }
                    }

                    listaProductosFiltrados= filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listaProductosFiltrados;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listaProductosFiltrados= (ArrayList<ClaseProductos>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        if (listaProductosFiltrados != null )
            return listaProductosFiltrados.size()+6;
        else
            return 0;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.productos_cardview, viewGroup, false);
        CategoriaViewHolder pvh = new CategoriaViewHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder categoriaViewHolder, @SuppressLint("RecyclerView") final  int i) {
        categoriaViewHolder.cv.setVisibility(View.VISIBLE);
        if (i >= listaProductosFiltrados.size()) {
            categoriaViewHolder.cv.setVisibility(View.INVISIBLE);
            return;
        }
        categoriaViewHolder.descripcion.setText(listaProductosFiltrados.get(i).descripcion);
        categoriaViewHolder.precio.setText( listaProductosFiltrados.get(i).euros);
        if (fragmentProductos.productoSel > listaProductos.size() )
            fragmentProductos.productoSel =0;

        // fragmentProductoSel es el indice de la lista filtrada
        if ( fragmentProductos.productoSel >= 0 &&  listaProductos.get(fragmentProductos.productoSel).codmenu.equals(listaProductosFiltrados.get(i).codmenu)){
    categoriaViewHolder.fondo.setBackgroundResource(R.color.elementoSel);
        }
        else {
            categoriaViewHolder.fondo.setBackgroundResource(R.color.cvBorde);
        }

        if (listaProductosFiltrados.get(i).aplicar_hh.trim().toUpperCase().equals("S")){
            categoriaViewHolder.ivHH.setVisibility(View.VISIBLE);
        }
        else{
            categoriaViewHolder.ivHH.setVisibility(View.INVISIBLE);
        }

        categoriaViewHolder.ivOrden_platos.setVisibility(View.VISIBLE);
        switch (listaProductosFiltrados.get(i).orden_platos) {
            case 0:
                categoriaViewHolder.ivOrden_platos.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.navmenu_bebida));
                break;
            case 1:
                categoriaViewHolder.ivOrden_platos.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.navmenu_entrante));
                break;
            case 2:
                categoriaViewHolder.ivOrden_platos.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.navmenu_primero));
                break;
            case 3:
                categoriaViewHolder.ivOrden_platos.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.navmenu_comida));
                break;
            case 4:
                categoriaViewHolder.ivOrden_platos.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.navmenu_postre));
                break;
            case 5:
                categoriaViewHolder.ivOrden_platos.setVisibility(View.GONE);
                break;
        }


        categoriaViewHolder.fondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (i >= listaProductosFiltrados.size() ){
                    return;
                }

                fragmentProductos.productoSel = i;
                for (ClaseProductos item : listaProductos){
                   if (item.codmenu.equals(listaProductosFiltrados.get(i).codmenu)){
                       fragmentProductos.productoSel = listaProductos.indexOf(item);
                       break;
                   }
                }

                interfaceProductos.onClickProducto(listaProductosFiltrados.get(i).codmenu);
                fragmentProductos.cancelTimer();
                fragmentProductos.incrementaUnidades(listaProductosFiltrados.get(i).descripcion);
                if (FragmentProductos.timerActivo){
                    fragmentProductos.reduceProductos(i);
                    interfaceProductos.fp_ocultaElementos(true);
                }
                notifyDataSetChanged();

            }
        });

        categoriaViewHolder.fondo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                interfaceProductos.onLongCLickProductos(listaProductosFiltrados.get(i).codmenu);
                return true;
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
        androidx.appcompat.widget.AppCompatImageView ivHH, ivOrden_platos;



        CategoriaViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            descripcion = itemView.findViewById(R.id.descripcion);
            precio = itemView.findViewById(R.id.precio);
            fondo = itemView.findViewById(R.id.fondo);
            ivHH = itemView.findViewById(R.id.ivHH);
            ivOrden_platos= itemView.findViewById(R.id.ivOrden_platos);
        }


    }


}