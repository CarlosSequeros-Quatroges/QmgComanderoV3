package es.quatroges.qgestpv_v3.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import es.quatroges.qgestpv_v3.R;
import es.quatroges.qgestpv_v3.utils.ClaseItemExtra;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public class RvAdapterExtras extends RecyclerView.Adapter<RvAdapterExtras.CategoriaViewHolder> {

    static ArrayList<ClaseItemExtra> listaExtras;
    Context context;

    public RvAdapterExtras(Context context, ArrayList<ClaseItemExtra> extras) {
        this.context = context;
        this.listaExtras = extras;
        if (this.listaExtras == null) {
            this.listaExtras = new ArrayList<>();
        }
    }

    @Override
    public int getItemCount() {
        if (listaExtras != null) {
            return listaExtras.size();
        }
        return 0;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.extras_cardview, viewGroup, false);
        CategoriaViewHolder pvh = new CategoriaViewHolder(v, ((Activity) context));
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoriaViewHolder holder, int i) {
        ClaseItemExtra extra = listaExtras.get(i);
        holder.tvDescripcionExtra.setText(extra.descripcion);
        holder.tvPrecioExtra.setText(extra.precio);

        holder.rgConSin.setOnCheckedChangeListener(null);

        if (extra.estadoExtra == ClaseItemExtra.ESTADO_CON) {
            holder.rgConSin.check(R.id.rbConExtra);
        } else if (extra.estadoExtra == ClaseItemExtra.ESTADO_SIN) {
            holder.rgConSin.check(R.id.rbSinExtra);
        } else {
            holder.rgConSin.check(R.id.rbNsExtra);
        }

        holder.rgConSin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int posicion = holder.getBindingAdapterPosition();
                if (posicion == RecyclerView.NO_POSITION) {
                    return;
                }
                ClaseItemExtra item = listaExtras.get(posicion);
                if (checkedId == R.id.rbConExtra) {
                    item.estadoExtra = ClaseItemExtra.ESTADO_CON;
                } else if (checkedId == R.id.rbSinExtra) {
                    item.estadoExtra = ClaseItemExtra.ESTADO_SIN;
                } else {
                    item.estadoExtra = ClaseItemExtra.ESTADO_NADA;
                }
                if (item.codigo == 0 && item.estadoExtra == ClaseItemExtra.ESTADO_NADA) {
                    item.estado = ClaseUtils.enEstado.transmitida;
                }
                else if (item.codigo == 0  && item.estadoExtra != ClaseItemExtra.ESTADO_NADA) {
                    item.estado = ClaseUtils.enEstado.anadir;
                }
                else if (item.codigo != 0 && item.estadoExtra == ClaseItemExtra.ESTADO_NADA) {
                    item.estado = ClaseUtils.enEstado.eliminar;
                }
                else {
                    item.estado = ClaseUtils.enEstado.actualizar;
                }
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescripcionExtra, tvPrecioExtra;
        RadioGroup rgConSin;
        RadioButton rbNs, rbCon, rbSin;

        CategoriaViewHolder(View itemView, final Activity activity) {
            super(itemView);
            tvDescripcionExtra = itemView.findViewById(R.id.tvDescripcionExtra);
            tvPrecioExtra = itemView.findViewById(R.id.tvPrecioExtra);
            rgConSin = itemView.findViewById(R.id.rgConSinExtra);
            rbNs = itemView.findViewById(R.id.rbNsExtra);
            rbCon = itemView.findViewById(R.id.rbConExtra);
            rbSin = itemView.findViewById(R.id.rbSinExtra);
        }
    }
}
