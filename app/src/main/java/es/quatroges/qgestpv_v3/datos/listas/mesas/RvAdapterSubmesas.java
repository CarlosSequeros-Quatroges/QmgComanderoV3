package es.quatroges.qgestpv_v3.datos.listas.mesas;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.FragmentSubmesas;
import es.quatroges.qgestpv_v3.R;

public class RvAdapterSubmesas extends RecyclerView.Adapter<RvAdapterSubmesas.CategoriaViewHolder> {



    private FragmentSubmesas.InterfaceSubmesas interfaceSubmesas;

    static ArrayList<ClaseSubMesas> listaSubmesas;
    Context context;
    FragmentSubmesas fragmentSubmesas;

    public RvAdapterSubmesas(Context context, ArrayList<ClaseSubMesas> submesas, FragmentSubmesas fragmentSubmesas){
        this.context = context;
        this.listaSubmesas = null;
        if (submesas != null) {
            this.listaSubmesas = new ArrayList<ClaseSubMesas>(submesas);
        }

        this.interfaceSubmesas = (FragmentSubmesas.InterfaceSubmesas)context;
        this.fragmentSubmesas = fragmentSubmesas;
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
        CategoriaViewHolder pvh = new CategoriaViewHolder(v, fragmentSubmesas.getActivity());

        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoriaViewHolder categoriaViewHolder,final  int i) {

        categoriaViewHolder.cv.setVisibility(View.VISIBLE);
        if(listaSubmesas.get(i).lineasVentas.size() < 1 && i < (listaSubmesas.size()-1))
            categoriaViewHolder.cv.setVisibility(View.GONE);

        if (listaSubmesas.get(i).estado== ClaseSubMesas.enEstados.abierta ){
            categoriaViewHolder.fondo.setBackgroundResource(R.color.mesaLibre);
            categoriaViewHolder.numero.setText(Integer.toString(listaSubmesas.get(i).submesa));
        }
        else if (listaSubmesas.get(i).estado == ClaseSubMesas.enEstados.tiquet) {
            categoriaViewHolder.fondo.setBackgroundResource(R.color.mesaOcupada);
            categoriaViewHolder.numero.setText(String.valueOf(listaSubmesas.get(i).submesa));
        }

        if (fragmentSubmesas.submesaSel == i){
            categoriaViewHolder.laySel.setBackgroundResource(R.color.elementoSel);
        }
        else {
            categoriaViewHolder.laySel.setBackgroundResource(R.color.cvBorde);
        }

        categoriaViewHolder.ivErrTransmision.setVisibility(View.GONE);
        if (listaSubmesas.get(i).errorTransmision == true) {
            categoriaViewHolder.ivErrTransmision.setVisibility(View.VISIBLE);
            categoriaViewHolder.ivErrTransmision.setBackground(context.getDrawable(R.drawable.error));
        }
        else if (listaSubmesas.get(i).estado == ClaseSubMesas.enEstados.tiquet ) {
            categoriaViewHolder.ivErrTransmision.setVisibility(View.VISIBLE);
            categoriaViewHolder.ivErrTransmision.setBackground(context.getDrawable(R.drawable.bar_tiquet));
        }

        categoriaViewHolder.fondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                interfaceSubmesas.onClickSubmesa(listaSubmesas.get(i).submesa);
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
        ImageView ivErrTransmision;


        CategoriaViewHolder(View itemView,final Activity activity) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            numero = itemView.findViewById(R.id.numero);
            fondo = itemView.findViewById(R.id.fondo);
            laySel = itemView.findViewById(R.id.laySel);
            ivErrTransmision = itemView.findViewById(R.id.ivErrTransmision);
        }



    }


}