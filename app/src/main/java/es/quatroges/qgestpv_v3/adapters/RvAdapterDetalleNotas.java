package es.quatroges.qgestpv_v3.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.R;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public class RvAdapterDetalleNotas extends RecyclerView.Adapter<RvAdapterDetalleNotas.NotaViewHolder> {

    public static final int TIPO_EXTRA_CON = 1;
    public static final int TIPO_EXTRA_SIN = 2;
    public static final int TIPO_NOTA = 3;

    public static class ItemDetalleNota {
        public final int tipo;
        public final String texto;
        public final ClaseUtils.enEstado estado;

        public ItemDetalleNota(int tipo, String texto) {
            this(tipo, texto, ClaseUtils.enEstado.transmitida);
        }

        public ItemDetalleNota(int tipo, String texto, ClaseUtils.enEstado estado) {
            this.tipo = tipo;
            this.texto = texto;
            this.estado = estado != null ? estado : ClaseUtils.enEstado.transmitida;
        }
    }

    private final ArrayList<ItemDetalleNota> items;

    public RvAdapterDetalleNotas(ArrayList<ItemDetalleNota> items) {
        this.items = items == null ? new ArrayList<>() : items;
    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detalleventa_nota_item, parent, false);
        return new NotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        ItemDetalleNota item = items.get(position);
        holder.tvTextoNota.setText(item.texto);
        if (item.tipo == TIPO_EXTRA_CON ) {
            holder.ivTipoNota.setImageResource(R.drawable.bar_extras_con);
            holder.tvTextoNota.setText("CON "+item.texto);
        }
        else if (item.tipo == TIPO_EXTRA_SIN) {
            holder.ivTipoNota.setImageResource(R.drawable.bar_extras_sin);
            holder.tvTextoNota.setText("SIN "+item.texto);
        }
        else {
            holder.ivTipoNota.setImageResource(android.R.drawable.ic_menu_edit);
        }

        if (item.estado == ClaseUtils.enEstado.actualizar) {
            holder.ivEstado.setBackgroundResource(R.drawable.sync);
        } else if (item.estado == ClaseUtils.enEstado.anadir) {
            holder.ivEstado.setBackgroundResource(R.drawable.anadir);
        } else if (item.estado == ClaseUtils.enEstado.eliminar) {
            holder.ivEstado.setBackgroundResource(R.drawable.cancelar);
        } else {
            holder.ivEstado.setBackgroundResource(R.drawable.aceptar);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<ItemDetalleNota> nuevosItems) {
        items.clear();
        if (nuevosItems != null) {
            items.addAll(nuevosItems);
        }
        notifyDataSetChanged();
    }

    static class NotaViewHolder extends RecyclerView.ViewHolder {
         AppCompatImageView ivTipoNota;
         AppCompatImageView ivEstado;
         TextView tvTextoNota;

        NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTipoNota = itemView.findViewById(R.id.ivTipoNota);
            ivEstado = itemView.findViewById(R.id.ivEstado);
            tvTextoNota = itemView.findViewById(R.id.tvTextoNota);
        }
    }
}
