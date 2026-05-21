package es.quatroges.qgestpv_v3.adapters;

import static android.view.View.GONE;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.R;
import es.quatroges.qgestpv_v3.utils.ClaseItemExtra;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public class RvAdapterNotas extends RecyclerView.Adapter<RvAdapterNotas.CategoriaViewHolder> {

    public interface InterfaceOnClickNota {
        void onClickNota(int position, ClaseItemExtra nota);
    }

    static ArrayList<ClaseItemExtra> listaNotas;
    Context context;
    private int posicionSeleccionada = -1;
    private InterfaceOnClickNota interfaceOnClickNota;

    public RvAdapterNotas(Context context, ArrayList<ClaseItemExtra> notas, InterfaceOnClickNota interfaceOnClickNota) {
        this.context = context;
        this.interfaceOnClickNota = interfaceOnClickNota;
        this.listaNotas = notas;
        if (this.listaNotas == null) {
            this.listaNotas = new ArrayList<>();
        }
    }

    @Override
    public int getItemCount() {
        if (listaNotas != null) {
            return listaNotas.size();
        }
        return 0;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notas_cardview, viewGroup, false);
        CategoriaViewHolder pvh = new CategoriaViewHolder(v, ((Activity) context));
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoriaViewHolder holder, int i) {
        holder.tvNota.setText(listaNotas.get(i).nota);
        if (i == posicionSeleccionada) {
            holder.layNota.setBackgroundResource(R.color.elementoSel);
        } else {
            holder.layNota.setBackgroundResource(R.color.lineaVenta);
        }

        if (listaNotas.get(i).estado == ClaseUtils.enEstado.eliminar) {
            holder.tvNota.setVisibility(GONE);
        }

        holder.layNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posicion = holder.getBindingAdapterPosition();
                if (posicion == RecyclerView.NO_POSITION) {
                    return;
                }
                posicionSeleccionada = posicion;
                notifyDataSetChanged();
                if (interfaceOnClickNota != null) {
                    interfaceOnClickNota.onClickNota(posicion, listaNotas.get(posicion));
                }
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setPosicionSeleccionada(int posicionSeleccionada) {
        this.posicionSeleccionada = posicionSeleccionada;
        notifyDataSetChanged();
    }

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNota;
        LinearLayout layNota;

        CategoriaViewHolder(View itemView, final Activity activity) {
            super(itemView);
            tvNota = itemView.findViewById(R.id.tvNota);
            layNota = itemView.findViewById(R.id.layNota);
        }
    }
}
