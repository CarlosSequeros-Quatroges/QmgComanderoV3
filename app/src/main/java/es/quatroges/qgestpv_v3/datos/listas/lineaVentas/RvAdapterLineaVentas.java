package es.quatroges.qgestpv_v3.datos.listas.lineaVentas;

import static es.quatroges.qgestpv_v3.utils.ClaseUtils.ORDEN_COMANDA_BEBIDA;
import static es.quatroges.qgestpv_v3.utils.ClaseUtils.ORDEN_COMANDA_ENTRANTE;
import static es.quatroges.qgestpv_v3.utils.ClaseUtils.ORDEN_COMANDA_POSTRE;
import static es.quatroges.qgestpv_v3.utils.ClaseUtils.ORDEN_COMANDA_PRIMERO;
import static es.quatroges.qgestpv_v3.utils.ClaseUtils.ORDEN_COMANDA_SEGUNDO;

import android.content.Context;
import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import es.quatroges.qgestpv_v3.FragmentLineaVentas;
import es.quatroges.qgestpv_v3.R;
import es.quatroges.qgestpv_v3.utils.ClaseItemExtra;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public class RvAdapterLineaVentas extends RecyclerView.Adapter<RvAdapterLineaVentas.CategoriaViewHolder> {



    private  static FragmentLineaVentas.InterfaceLineaVentas interfaceLineaVentas;

    static ArrayList<ClaseLineaVentas> listaLineaVentas;
    static Context context;
    static FragmentLineaVentas fragmentLineaVentas;
    private final ArrayList<Integer> posicionesAgrupadas = new ArrayList<>();

    public RvAdapterLineaVentas(Context context, ArrayList<ClaseLineaVentas> listaLineaVentas, FragmentLineaVentas fragmentLineaVentas){
        this.context = context;
        this.listaLineaVentas= listaLineaVentas;
        this.interfaceLineaVentas= (FragmentLineaVentas.InterfaceLineaVentas) context;
        this.fragmentLineaVentas= fragmentLineaVentas;
        preparaListaAgrupada();
    }

    @Override
    public int getItemCount() {
        return posicionesAgrupadas.size();
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lineaventas_cardview, viewGroup, false);
        CategoriaViewHolder pvh = new CategoriaViewHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder categoriaViewHolder,final  int i) {
        final int indiceReal = getIndiceReal(i);
        if (indiceReal < 0 || listaLineaVentas == null || indiceReal >= listaLineaVentas.size()) {
            return;
        }

        ClaseLineaVentas lineaVenta = listaLineaVentas.get(indiceReal);

        categoriaViewHolder.cv.setEnabled(true);
        categoriaViewHolder.cv.setAlpha((float) 1.0);

        categoriaViewHolder.cantidad.setText(Integer.toString(lineaVenta.cantidad));
        categoriaViewHolder.descripcion.setText(lineaVenta.descripcion);
        categoriaViewHolder.peuros.setText(ClaseUtils.double2string(lineaVenta.peuros,2));
        double importeExtras = calculaImporteExtras(lineaVenta);
        if (importeExtras > 0) {
            categoriaViewHolder.pextras.setVisibility(View.VISIBLE);
            categoriaViewHolder.pextras.setText(ClaseUtils.double2string(importeExtras,2));
        } else {
            categoriaViewHolder.pextras.setVisibility(View.GONE);
        }

        categoriaViewHolder.teuros.setText(ClaseUtils.double2string(lineaVenta.teuros+ lineaVenta.cantidad* importeExtras,2));

        if (esInicioGrupo(i)) {
            categoriaViewHolder.tvOrdenGrupo.setVisibility(View.VISIBLE);
            categoriaViewHolder.tvOrdenGrupo.setText(ordenPlato2Desc(lineaVenta.orden_platos));
        }
        else {
            categoriaViewHolder.tvOrdenGrupo.setVisibility(View.GONE);
        }


        if (lineaVenta.estado == ClaseUtils.enEstado.actualizar)
            categoriaViewHolder.ivEstado.setBackgroundDrawable(context.getDrawable(R.drawable.sync));
        else if (lineaVenta.estado == ClaseUtils.enEstado.anadir)
            categoriaViewHolder.ivEstado.setBackgroundDrawable(context.getDrawable(R.drawable.anadir));
        else if (lineaVenta.estado == ClaseUtils.enEstado.eliminar) {
            categoriaViewHolder.ivEstado.setBackgroundDrawable(context.getDrawable(R.drawable.cancelar));
            categoriaViewHolder.cv.setEnabled(false);
            categoriaViewHolder.cv.setAlpha((float) 0.4);
        }
        else
            categoriaViewHolder.ivEstado.setBackgroundDrawable(context.getDrawable(R.drawable.aceptar));

        if (lineaVenta.resultadoEnvio == ClaseUtils.enEstado.error) {
            categoriaViewHolder.ivErrTransmision.setVisibility(View.VISIBLE);
            categoriaViewHolder.ivErrTransmision.setBackgroundDrawable(context.getDrawable(R.drawable.error));
        }
        else {
            categoriaViewHolder.ivErrTransmision.setVisibility(View.GONE);
        }


        if (fragmentLineaVentas.lineaSel == indiceReal)
            categoriaViewHolder.cv.setBackgroundResource(R.color.lineaVentaSel);
        else
            categoriaViewHolder.cv.setBackgroundResource(R.color.lineaVenta);



        if (lineaVenta.happyhour.toUpperCase().equals("S"))
            categoriaViewHolder.ivHappyHour.setVisibility(View.VISIBLE);
        else
            categoriaViewHolder.ivHappyHour.setVisibility(View.GONE);

        if (lineaVenta.tieneExtras())
            categoriaViewHolder.ivObservaciones.setVisibility(View.VISIBLE);
        else
            categoriaViewHolder.ivObservaciones.setVisibility(View.INVISIBLE);

        if (!lineaVenta.pension.equalsIgnoreCase("")) {
            categoriaViewHolder.ivPension.setVisibility(View.VISIBLE);
        }
        else {
            categoriaViewHolder.ivPension.setVisibility(View.INVISIBLE);
        }
        //en vez de mostrar comida / bebida, muestro el orden de plato
        //if (listaLineaVentas.get(i).tipo.toUpperCase().trim().equals("B"))
        //    categoriaViewHolder.ivTipo.setBackgroundDrawable(context.getDrawable(R.drawable.navmenu_bebida));
        //else{

            //controlar si orden plato no encaja con tipo (comidas y bebidas)

            switch (lineaVenta.orden_platos){
                case ORDEN_COMANDA_BEBIDA:
                    categoriaViewHolder.ivTipo.setBackgroundDrawable(context.getDrawable(R.drawable.navmenu_bebida));
                    break;
                case ORDEN_COMANDA_ENTRANTE:
                    categoriaViewHolder.ivTipo.setBackgroundDrawable(context.getDrawable(R.drawable.navmenu_entrante));
                    break;
                case ORDEN_COMANDA_PRIMERO:
                    categoriaViewHolder.ivTipo.setBackgroundDrawable(context.getDrawable(R.drawable.navmenu_primero));
                    break;
                case ORDEN_COMANDA_SEGUNDO:
                    categoriaViewHolder.ivTipo.setBackgroundDrawable(context.getDrawable(R.drawable.navmenu_comida));
                    break;
                case ORDEN_COMANDA_POSTRE:
                    categoriaViewHolder.ivTipo.setBackgroundDrawable(context.getDrawable(R.drawable.navmenu_postre));
                    break;
                default :
                    categoriaViewHolder.ivTipo.setBackgroundDrawable(context.getDrawable(R.drawable.navmenu_entrante));
                    break;

            }

        //}


        categoriaViewHolder.layDatosVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentLineaVentas.lineaSel != indiceReal) {
                    fragmentLineaVentas.lineaSel = indiceReal;
                    notifyDataSetChanged();
                    interfaceLineaVentas.onClickLineaVenta(indiceReal);
                }
            }
        });

        categoriaViewHolder.layDatosVenta.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String dragData = Integer.toString(indiceReal);
                ClipData data = ClipData.newPlainText("linea_index", dragData);
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(data, shadowBuilder, dragData, 0);
                return true;
            }
        });


        categoriaViewHolder.descripcion.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                fragmentLineaVentas.lineaSel = indiceReal;
                notifyDataSetChanged();
                interfaceLineaVentas.onClickEditaVenta(indiceReal);
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
        TextView tvOrdenGrupo;
        TextView cantidad;
        TextView descripcion;
        TextView peuros;
        TextView teuros;
        TextView pextras;

        androidx.appcompat.widget.AppCompatImageView ivHappyHour, ivObservaciones, ivEstado, ivTipo, ivErrTransmision, ivPension;

        FrameLayout fondo;
        LinearLayout layDatosVenta;

        CategoriaViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            tvOrdenGrupo = itemView.findViewById(R.id.tvOrdenGrupo);
            cantidad = itemView.findViewById(R.id.cantidad);
            descripcion = itemView.findViewById(R.id.descripcion);
            peuros = itemView.findViewById(R.id.peuros);
            teuros = itemView.findViewById(R.id.teuros);
            pextras = itemView.findViewById(R.id.pextras);

            ivHappyHour = itemView.findViewById(R.id.ivHH);
            ivObservaciones= itemView.findViewById(R.id.ivObs);
            ivEstado= itemView.findViewById(R.id.ivEstado);
            ivTipo= itemView.findViewById(R.id.ivTipo);
            ivErrTransmision= itemView.findViewById(R.id.ivErrTransmision);
            ivPension= itemView.findViewById(R.id.ivPension);


            fondo = itemView.findViewById(R.id.fondo);
            layDatosVenta = itemView.findViewById(R.id.layDatosVenta);

        }


    }


    private void preparaListaAgrupada() {
        posicionesAgrupadas.clear();
        if (listaLineaVentas == null) {
            return;
        }

        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < listaLineaVentas.size(); i++) {
            indices.add(i);
        }

        Collections.sort(indices, new Comparator<Integer>() {
            @Override
            public int compare(Integer indice1, Integer indice2) {
                ClaseLineaVentas linea1 = listaLineaVentas.get(indice1);
                ClaseLineaVentas linea2 = listaLineaVentas.get(indice2);

                if (linea1.orden_platos == linea2.orden_platos) {
                    return Integer.compare(indice1, indice2);
                }
                return Integer.compare(linea1.orden_platos, linea2.orden_platos);
            }
        });

        posicionesAgrupadas.addAll(indices);
    }

    private int getIndiceReal(int posicionAdapter) {
        if (posicionAdapter < 0 || posicionAdapter >= posicionesAgrupadas.size()) {
            return -1;
        }
        return posicionesAgrupadas.get(posicionAdapter);
    }

    private boolean esInicioGrupo(int posicionAdapter) {
        if (posicionAdapter == 0) {
            return true;
        }

        int indiceActual = getIndiceReal(posicionAdapter);
        int indiceAnterior = getIndiceReal(posicionAdapter - 1);
        if (indiceActual < 0 || indiceAnterior < 0) {
            return false;
        }

        int ordenActual = listaLineaVentas.get(indiceActual).orden_platos;
        int ordenAnterior = listaLineaVentas.get(indiceAnterior).orden_platos;
        return ordenActual != ordenAnterior;
    }

    public int getAdapterPositionForLinea(int indiceRealLinea) {
        for (int i = 0; i < posicionesAgrupadas.size(); i++) {
            if (posicionesAgrupadas.get(i) == indiceRealLinea) {
                return i;
            }
        }
        return RecyclerView.NO_POSITION;
    }

    public boolean onItemMove(int fromAdapterPos, int toAdapterPos) {
        return getIndiceReal(fromAdapterPos) >= 0 && getIndiceReal(toAdapterPos) >= 0;
    }

    public boolean onItemDrop(int fromAdapterPos, int toAdapterPos) {
        int fromReal = getIndiceReal(fromAdapterPos);
        int toReal = getIndiceReal(toAdapterPos);
        if (fromReal < 0 || toReal < 0 || listaLineaVentas == null) {
            return false;
        }

        ClaseLineaVentas origen = listaLineaVentas.get(fromReal);
        ClaseLineaVentas destino = listaLineaVentas.get(toReal);

        //no se puede mover comida a bebidas y viceversa
        if (! origen.tipo.equalsIgnoreCase(destino.tipo)) {
            return false;
        }


        int ordenDestino = listaLineaVentas.get(toReal).orden_platos;

        // If drop lands on same group, try closest different group in drag direction.
        if (ordenDestino == origen.orden_platos) {
            int ordenAlternativo = buscaOrdenAlternativoEnDireccion(toAdapterPos, fromAdapterPos < toAdapterPos, origen.orden_platos);
            if (ordenAlternativo != Integer.MIN_VALUE) {
                ordenDestino = ordenAlternativo;
            }


        }

        if (origen.orden_platos != ordenDestino) {
            origen.orden_platos = ordenDestino;
            if (origen.estado == ClaseUtils.enEstado.transmitida) {
                origen.estado = ClaseUtils.enEstado.actualizar;
            }
        }

        fragmentLineaVentas.lineaSel = fromReal;
        preparaListaAgrupada();
        notifyDataSetChanged();
        interfaceLineaVentas.onClickLineaVenta(fromReal);
        return true;
    }


    public boolean onItemDropToOrdenFromRealIndex(int fromReal, int ordenDestino) {
        if (fromReal < 0 || listaLineaVentas == null || fromReal >= listaLineaVentas.size()) {
            return false;
        }

        ClaseLineaVentas origen = listaLineaVentas.get(fromReal);

        //las bebidas no se pueden reordenar
        if (origen.tipo.equalsIgnoreCase("B")) {
            return false;
        }
        if (origen.orden_platos != ordenDestino) {
            origen.orden_platos = ordenDestino;
            if (origen.estado == ClaseUtils.enEstado.transmitida) {
                origen.estado = ClaseUtils.enEstado.actualizar;
            }
        }

        fragmentLineaVentas.lineaSel = fromReal;
        preparaListaAgrupada();
        notifyDataSetChanged();
        interfaceLineaVentas.onClickLineaVenta(fromReal);
        return true;
    }

    private int buscaOrdenAlternativoEnDireccion(int desdePosAdapter, boolean haciaAbajo, int ordenActual) {
        if (listaLineaVentas == null || posicionesAgrupadas.isEmpty()) {
            return Integer.MIN_VALUE;
        }

        if (haciaAbajo) {
            for (int p = desdePosAdapter + 1; p < posicionesAgrupadas.size(); p++) {
                int real = getIndiceReal(p);
                if (real >= 0) {
                    int orden = listaLineaVentas.get(real).orden_platos;
                    if (orden != ordenActual) return orden;
                }
            }
        } else {
            for (int p = desdePosAdapter - 1; p >= 0; p--) {
                int real = getIndiceReal(p);
                if (real >= 0) {
                    int orden = listaLineaVentas.get(real).orden_platos;
                    if (orden != ordenActual) return orden;
                }
            }
        }

        return Integer.MIN_VALUE;
    }

    private String ordenPlato2Desc(int orden) {
        switch (orden) {
            case ORDEN_COMANDA_BEBIDA:
                return "Bebidas";
            case ORDEN_COMANDA_ENTRANTE:
                return "Entrantes";
            case ORDEN_COMANDA_PRIMERO:
                return "Primeros";
            case ORDEN_COMANDA_SEGUNDO:
                return "Segundos";
            case ORDEN_COMANDA_POSTRE:
                return "Postres";
            default:
                return "Otros";
        }
    }

    private double calculaImporteExtras(ClaseLineaVentas lineaVenta) {
        if (lineaVenta == null || lineaVenta.extras == null || lineaVenta.extras.isEmpty()) {
            return 0;
        }

        double totalExtras = 0;
        for (es.quatroges.qgestpv_v3.utils.ClaseItemExtra extra : lineaVenta.extras) {
            if (extra == null || extra.precio == null || extra.precio.trim().isEmpty() || extra.estadoExtra != ClaseItemExtra.ESTADO_CON) {
                continue;
            }

            try {
                double pExtra =  Double.parseDouble(extra.precio.replace(",", ".").trim());
                totalExtras +=pExtra;

            } catch (NumberFormatException ignored) {
            }
        }
        return totalExtras;
    }

}
