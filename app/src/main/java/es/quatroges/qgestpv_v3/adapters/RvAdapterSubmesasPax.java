package es.quatroges.qgestpv_v3.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.R;
import es.quatroges.qgestpv_v3.datos.listas.mesas.ClaseSubMesas;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public class RvAdapterSubmesasPax extends RecyclerView.Adapter<RvAdapterSubmesasPax.CategoriaViewHolder> {

    static ArrayList<ClaseSubMesas> listaSubmesas;
    Context context;

    public RvAdapterSubmesasPax(Context context, ArrayList<ClaseSubMesas> submesas){
        this.context = context;
        this.listaSubmesas = null;
        if (submesas != null) {
            this.listaSubmesas = new ArrayList<ClaseSubMesas>(submesas);
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

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.submesas_pax_cardview, viewGroup, false);
        CategoriaViewHolder pvh = new CategoriaViewHolder(v, ((Activity) context));

        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoriaViewHolder categoriaViewHolder, @SuppressLint("RecyclerView") final  int i) {

        if (listaSubmesas.get(i).estado == ClaseSubMesas.enEstados.tiquet||
            listaSubmesas.get(i).pax <1){
            if (ClaseUtils.PidePaxSubmesa.isMover_uds()) {
                categoriaViewHolder.fondo.setVisibility(View.GONE);
                return;
            }
            else {
                categoriaViewHolder.ivPaxDown.setVisibility(View.GONE);
            }

        }
        else {
            categoriaViewHolder.ivPaxDown.setVisibility(View.VISIBLE);
        }

        categoriaViewHolder.fondo.setBackgroundResource(R.color.mesaLibre);
        categoriaViewHolder.numero.setText(Integer.toString(listaSubmesas.get(i).submesa));
        categoriaViewHolder.pax.setText(Integer.toString(listaSubmesas.get(i).pax));
        String room = listaSubmesas.get(i).habitacion == null ? "":listaSubmesas.get(i).habitacion;
        categoriaViewHolder.etRoom.setText(room);

        categoriaViewHolder.etRoom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start,int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
               listaSubmesas.get(i).habitacion = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        categoriaViewHolder.ivPaxDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClaseUtils.PidePaxSubmesa.setSubMesaSel(i,-1);
                notifyDataSetChanged();
            }
        });

        categoriaViewHolder.ivPaxUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClaseUtils.PidePaxSubmesa.setSubMesaSel(i,1);
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
        TextView numero, pax;
        EditText etRoom;
        LinearLayout fondo,laySel;
        ImageView ivPaxDown, ivPaxUp;


        CategoriaViewHolder(View itemView,final Activity activity) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            numero = itemView.findViewById(R.id.numero);
            pax  = itemView.findViewById(R.id.pax);
            etRoom  = itemView.findViewById(R.id.etRoom);
            fondo = itemView.findViewById(R.id.fondo);
            laySel = itemView.findViewById(R.id.laySel);
            ivPaxDown = itemView.findViewById(R.id.ivPaxDown);
            ivPaxUp = itemView.findViewById(R.id.ivPaxUp);

        }



    }


}