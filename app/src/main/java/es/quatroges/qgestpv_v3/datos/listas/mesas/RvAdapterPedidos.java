package es.quatroges.qgestpv_v3.datos.listas.mesas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.FragmentMesas;
import es.quatroges.qgestpv_v3.R;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;

/**
 * Tarjetas de la pestaña PEDIDOS (mesas >= 1000 que envia el servidor): numero de pedido, estado, punto de venta web,
 * forma de pago e importe. Un pedido no tiene pax ni habitacion ni la edicion de datos de apertura de las mesas:
 * al pulsar se abre directamente y la pulsacion larga con cuenta pedida lleva al cobro, igual que en las mesas.
 */
public class RvAdapterPedidos extends RecyclerView.Adapter<RvAdapterPedidos.PedidoViewHolder> implements Filterable {

    private final FragmentMesas.InterfaceMesas interfaceMesas;
    private final FragmentMesas fragmentMesas;
    private final Context context;
    private final ArrayList<ClaseMesas> listaPedidos;
    private ArrayList<ClaseMesas> listaFiltrada;
    private boolean quitaFocoSvMesa;

    public RvAdapterPedidos(Context context, ArrayList<ClaseMesas> pedidos, FragmentMesas fragmentMesas) {
        this.context = context;
        this.listaPedidos = pedidos != null ? new ArrayList<>(pedidos) : new ArrayList<ClaseMesas>();
        this.listaFiltrada = new ArrayList<>(this.listaPedidos);
        this.interfaceMesas = (FragmentMesas.InterfaceMesas) context;
        this.fragmentMesas = fragmentMesas;
        this.quitaFocoSvMesa = false;
    }

    public void setQuitaFocoSvMesa(boolean quitaFocoSvMesa) {
        this.quitaFocoSvMesa = quitaFocoSvMesa;
    }

    private static boolean contiene(String valor, String texto) {
        return valor != null && valor.toUpperCase().contains(texto);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String texto = charSequence == null ? "" : charSequence.toString().trim().toUpperCase();
                ArrayList<ClaseMesas> filtrados;
                if (texto.isEmpty()) {
                    filtrados = listaPedidos;
                } else {
                    filtrados = new ArrayList<>();
                    for (ClaseMesas pedido : listaPedidos) {
                        if (String.valueOf(pedido.mesa).contains(texto) || contiene(pedido.descripcion, texto)
                                || contiene(pedido.codptoWeb, texto) || contiene(pedido.fpagoWeb, texto)) {
                            filtrados.add(pedido);
                        }
                    }
                }
                FilterResults resultado = new FilterResults();
                resultado.values = filtrados;
                return resultado;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults.values instanceof ArrayList<?>) {
                    listaFiltrada = (ArrayList<ClaseMesas>) filterResults.values;
                    notifyDataSetChanged();
                    if (quitaFocoSvMesa) fragmentMesas.swMesaClearFocus();
                    quitaFocoSvMesa = false;
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        return listaFiltrada == null ? 0 : listaFiltrada.size();
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pedidos_cardview, viewGroup, false);
        return new PedidoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, @SuppressLint("RecyclerView") int i) {
        final ClaseMesas pedido = listaFiltrada.get(i);

        holder.numero.setText(String.valueOf(pedido.mesa));
        holder.tvCodptoWeb.setText(pedido.codptoWeb == null ? "" : pedido.codptoWeb);
        holder.tvFpagoWeb.setText(pedido.fpagoWeb == null ? "" : pedido.fpagoWeb);
        holder.importe.setText(ClaseUtils.double2string(pedido.importe, 2));

        // un pedido solo existe si el servidor lo envia abierto o con cuenta pedida; libre no deberia darse
        holder.fondo.setBackgroundResource(pedido.abiertas || pedido.tickets ? R.color.mesaOcupada : R.color.mesaLibre);
        holder.ivEstado.setVisibility(pedido.tickets ? View.VISIBLE : View.INVISIBLE);
        holder.laySel.setBackgroundResource(fragmentMesas.mesaSel == pedido.mesa ? R.color.elementoSel : R.color.cvBorde);

        holder.fondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentMesas.estado != FragmentMesas.enEstado.seleccion) return;
                String habitacion = "";
                if (pedido.submesas != null && pedido.submesas.size() > 0 && pedido.submesas.get(0).habitacion != null) {
                    habitacion = pedido.submesas.get(0).habitacion;
                }
                interfaceMesas.onClickMesa(pedido.mesa, pedido.pax, habitacion);
            }
        });

        holder.fondo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (pedido.tickets) {
                    interfaceMesas.onLongClickMesa(pedido.mesa);
                    return true;
                }
                return false;
            }
        });
    }

    public static class PedidoViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        LinearLayout fondo, laySel;
        TextView numero, tvCodptoWeb, tvFpagoWeb, importe;
        AppCompatImageView ivMesa, ivEstado;

        PedidoViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            fondo = itemView.findViewById(R.id.fondo);
            laySel = itemView.findViewById(R.id.laySel);
            numero = itemView.findViewById(R.id.numero);
            tvCodptoWeb = itemView.findViewById(R.id.tvCodptoWeb);
            tvFpagoWeb = itemView.findViewById(R.id.tvFpagoWeb);
            importe = itemView.findViewById(R.id.importe);
            ivMesa = itemView.findViewById(R.id.ivMesa);
            ivEstado = itemView.findViewById(R.id.ivEstado);
        }
    }
}
