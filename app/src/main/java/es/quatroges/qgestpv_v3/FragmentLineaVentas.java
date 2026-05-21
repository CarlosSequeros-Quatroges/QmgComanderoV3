package es.quatroges.qgestpv_v3;

import static es.quatroges.qgestpv_v3.utils.ClaseUtils.ORDEN_COMANDA_ENTRANTE;
import static es.quatroges.qgestpv_v3.utils.ClaseUtils.ORDEN_COMANDA_POSTRE;
import static es.quatroges.qgestpv_v3.utils.ClaseUtils.ORDEN_COMANDA_PRIMERO;
import static es.quatroges.qgestpv_v3.utils.ClaseUtils.ORDEN_COMANDA_SEGUNDO;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.configuracion.ClaseCondicionesVenta;
import es.quatroges.qgestpv_v3.datos.listas.lineaVentas.ClaseLineaVentas;
import es.quatroges.qgestpv_v3.datos.listas.lineaVentas.RvAdapterLineaVentas;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;
import es.quatroges.qgestpv_v3.utils.OnSwipeTouchListener;

public class FragmentLineaVentas extends Fragment {

    FragmentActivity fragmentActivity;
    Context context;




    private static ArrayList<ClaseLineaVentas> listaLineaVentas;
    public static int lineaSel;
    public static double importe;
    public static double coste;
    public static boolean isHH;
    private static int ordenSel;

    private static boolean fragmentAmpliado;
    private static RecyclerView rvLineaVentas;
    private static RvAdapterLineaVentas adapterLineaVentas;
    private ItemTouchHelper itemTouchHelper;
    private int dragFromPos = RecyclerView.NO_POSITION;
    private int dragToPos = RecyclerView.NO_POSITION;
    private int dragOrdenTarget = Integer.MIN_VALUE;

    //quito ivBebida. las bebidas siempre son bebidas
    private androidx.appcompat.widget.AppCompatImageView ivMin, ivMax, ivSetHH,ivSync, ivEnviarComanda,
            ivEntrante,ivPrimero,ivSegundo,ivPostre, ivSiguePlato, ivVerComanda;
    private TextView tvTotal;
    private static LinearLayout layOrdenplatos;



    public void setOrdenSel(int ordenSel) {
        FragmentLineaVentas.ordenSel = ordenSel;

        //ivBebida.setBackgroundDrawable(context.getDrawable(R.drawable.bar_bebida_sel));
        ivEntrante.setBackgroundDrawable(context.getDrawable(R.drawable.bar_entrante1));
        ivPrimero.setBackgroundDrawable(context.getDrawable(R.drawable.bar_primero1));
        ivSegundo.setBackgroundDrawable(context.getDrawable(R.drawable.bar_comida1));
        ivPostre.setBackgroundDrawable(context.getDrawable(R.drawable.bar_postre1));

        switch (ordenSel){
            case ORDEN_COMANDA_ENTRANTE:
                //ivBebida.setBackgroundDrawable(context.getDrawable(R.drawable.bar_bebida1));
                ivEntrante.setBackgroundDrawable(context.getDrawable(R.drawable.bar_entrante_sel));
                break;
            case ORDEN_COMANDA_PRIMERO:
                //ivBebida.setBackgroundDrawable(context.getDrawable(R.drawable.bar_bebida1));
                ivPrimero.setBackgroundDrawable(context.getDrawable(R.drawable.bar_primero_sel));
                break;
            case ORDEN_COMANDA_SEGUNDO:
                //ivBebida.setBackgroundDrawable(context.getDrawable(R.drawable.bar_bebida1));
                ivSegundo.setBackgroundDrawable(context.getDrawable(R.drawable.bar_comida_sel));
                break;
            case ORDEN_COMANDA_POSTRE:
                //ivBebida.setBackgroundDrawable(context.getDrawable(R.drawable.bar_bebida1));
                ivPostre.setBackgroundDrawable(context.getDrawable(R.drawable.bar_postre_sel));
                break;

        }
    }

    public void actualizaLineaVentas(boolean refresca) {
        if (interfaceLineaVentas != null){
            Bundle bundle = interfaceLineaVentas.cargaLineaVentas(refresca);
            lineaSel = bundle.getInt("lineaSel",1);
            listaLineaVentas = bundle.getParcelableArrayList("lineaVentas");
            importe = bundle.getDouble("importe");
            coste = bundle.getDouble("coste");

            rvLineaVentas.setAdapter(null);
            adapterLineaVentas = new RvAdapterLineaVentas(context,listaLineaVentas,this) ;

            rvLineaVentas.setAdapter(adapterLineaVentas);
            tvTotal.setText("Total: "+ClaseUtils.double2string(importe,2)+" € ");
            if (! ClaseCondicionesVenta.tipo_cuentacasa.toUpperCase().equals("G"))
                tvTotal.setText(tvTotal.getText().toString()+ " ("+ClaseUtils.double2string(coste,2)+"€)");

            ivSync.setVisibility(View.INVISIBLE);


            if (interfaceLineaVentas.lineasPendientes())
                ivEnviarComanda.setVisibility(View.VISIBLE);
            else
                ivEnviarComanda.setVisibility(View.INVISIBLE);

            if (lineaSel> -1) {
                int posicionAdapter = adapterLineaVentas.getAdapterPositionForLinea(lineaSel);
                if (posicionAdapter != RecyclerView.NO_POSITION) {
                    rvLineaVentas.scrollToPosition(posicionAdapter);
                }
            }

            layOrdenplatos.setVisibility(View.VISIBLE);
            if (bundle.getString("ordenplatos").toUpperCase().equals("N"))
                layOrdenplatos.setVisibility(View.GONE);

            ivSiguePlato.setVisibility(View.GONE);
            if ( refresca == false && listaLineaVentas.size() > 0) {
                ivSiguePlato.setVisibility(View.VISIBLE);
            }


        }
    }


    public interface InterfaceLineaVentas {
        Bundle cargaLineaVentas(boolean refresca);
        void onClickLineaVenta(int i);
        void setHappyHour(boolean hh);
        void onClickEditaVenta(int i);
        boolean lineasPendientes();
        void onClickEnviarComanda();
        void onClickSincronizaComanda();
        void onClickOrden(int orden);
        void onClickSiguePlato();
        void onClickVerComanda();
    }

    InterfaceLineaVentas interfaceLineaVentas;


    public FragmentLineaVentas newInstance() {
        FragmentLineaVentas categoriasFragment = new FragmentLineaVentas();
        return categoriasFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.fragmentActivity = (FragmentActivity)context;
            this.context = context;
            interfaceLineaVentas = (InterfaceLineaVentas) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.lineaventas_fragment,container,false);

        rvLineaVentas = rootView. findViewById(R.id.rvLineaVentas);
        ivMin = rootView.findViewById(R.id.ivMin);
        ivMax = rootView.findViewById(R.id.ivMax);
        ivSetHH = rootView.findViewById(R.id.ivSetHH);
        ivSync = rootView.findViewById(R.id.ivSync);
        ivEnviarComanda = rootView.findViewById(R.id.ivEnviarComanda);
        //ivBebida= rootView.findViewById(R.id.ivBebida);
        ivEntrante= rootView.findViewById(R.id.ivEntrante);
        ivPrimero= rootView.findViewById(R.id.ivPrimero);
        ivSegundo= rootView.findViewById(R.id.ivSegundo );
        ivPostre= rootView.findViewById(R.id.ivPostre);
        ivSiguePlato = rootView.findViewById(R.id.ivSiguePlato1);
        ivVerComanda = rootView.findViewById(R.id.ivVerComanda);


        FragmentLineaVentas.ordenSel = ordenSel;
        //ivBebida.setBackgroundDrawable(context.getDrawable(R.drawable.bar_bebida_sel));
        ivEntrante.setBackgroundDrawable(context.getDrawable(R.drawable.bar_entrante_sel));
        ivPrimero.setBackgroundDrawable(context.getDrawable(R.drawable.bar_primero1));
        ivSegundo.setBackgroundDrawable(context.getDrawable(R.drawable.bar_comida1));
        ivPostre.setBackgroundDrawable(context.getDrawable(R.drawable.bar_postre1));

        setOrdenSel(ordenSel);

        layOrdenplatos = rootView.findViewById(R.id.layOrdenPlatos);
        layOrdenplatos.setVisibility(View.GONE);


        tvTotal = rootView.findViewById(R.id.tvTotal);

        //rvLineaVentas.setHasFixedSize(true);

        fragmentAmpliado = false;
        LinearLayoutManager llmCategorias = new LinearLayoutManager( context,LinearLayoutManager.VERTICAL,false);

        rvLineaVentas.setLayoutManager(llmCategorias);


        Bundle bundle = interfaceLineaVentas.cargaLineaVentas(true);

        listaLineaVentas = bundle.getParcelableArrayList("lineaVentas");
        if (listaLineaVentas == null) listaLineaVentas = new ArrayList<>();

        adapterLineaVentas = new RvAdapterLineaVentas(context, listaLineaVentas, this) ;
        rvLineaVentas.setAdapter(adapterLineaVentas);
        inicializaDragAndDrop();

        ivSiguePlato.setVisibility(View.VISIBLE);
        if (listaLineaVentas.size() < 1) {
            ivSiguePlato.setVisibility(View.GONE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rvLineaVentas.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    mostrarMinMax();
                }
            });
        }
        else {
            rvLineaVentas.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    mostrarMinMax();
                }
            });
        }

        rvLineaVentas.setOnTouchListener(new OnSwipeTouchListener(context){
            @Override
            public void onSwipeDown() {
            }

        });

        isHH = false;
        ivSetHH.setAlpha((float) 0.5);
        ivSetHH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHH){
                    isHH = false;
                    ivSetHH.setAlpha((float) 0.5);
                }
                else{
                    isHH = true;
                    ivSetHH.setAlpha((float) 1.0);
                }
                if (interfaceLineaVentas != null) interfaceLineaVentas.setHappyHour(isHH);

            }
        });

        ivEnviarComanda.setVisibility(View.INVISIBLE);
        ivEnviarComanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceLineaVentas.onClickEnviarComanda();
            }
        });

        ivSync.setVisibility(View.INVISIBLE);
        ivSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceLineaVentas.onClickSincronizaComanda();
            }
        });

        /*
        ivBebida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceLineaVentas.onClickOrden(ORDEN_COMANDA_BEBIDA);
            }
        });

         */

        ivEntrante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceLineaVentas.onClickOrden(ORDEN_COMANDA_ENTRANTE);
            }
        });

        ivPrimero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceLineaVentas.onClickOrden(ORDEN_COMANDA_PRIMERO);
            }
        });

        ivSegundo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceLineaVentas.onClickOrden(ORDEN_COMANDA_SEGUNDO);
            }
        });

        ivPostre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceLineaVentas.onClickOrden(ORDEN_COMANDA_POSTRE);
            }
        });

        ivSiguePlato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceLineaVentas.onClickSiguePlato();
            }
        });

        ivVerComanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaceLineaVentas.onClickVerComanda();
            }
        });

        inicializaDropTargetsExternos();
        return rootView;

    }

    private void inicializaDragAndDrop() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {


            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                if (adapterLineaVentas == null) return false;
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                if (dragFromPos == RecyclerView.NO_POSITION) dragFromPos = from;
                dragToPos = to;

                return adapterLineaVentas.onItemMove(from, to);
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public boolean isLongPressDragEnabled() { return false; }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                if (adapterLineaVentas != null && dragFromPos != RecyclerView.NO_POSITION) {
                    boolean cambiado = false;
                    if (dragToPos != RecyclerView.NO_POSITION) {
                        cambiado = adapterLineaVentas.onItemDrop(dragFromPos, dragToPos);
                    }
                    if (cambiado)  {

                        if (interfaceLineaVentas.lineasPendientes())
                            ivEnviarComanda.setVisibility(View.VISIBLE);
                        else
                            ivEnviarComanda.setVisibility(View.INVISIBLE);

                    }
                }
                limpiaAnimacionDropTargets();
                dragFromPos = RecyclerView.NO_POSITION;
                dragToPos = RecyclerView.NO_POSITION;
                dragOrdenTarget = Integer.MIN_VALUE;
            }

        };

        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvLineaVentas);
    }

    private void actualizaAnimacionDropTarget(int ordenTarget) {
        animaTarget(ivEntrante, ordenTarget == ORDEN_COMANDA_ENTRANTE, R.drawable.bar_entrante_draw, R.drawable.bar_entrante1);
        animaTarget(ivPrimero, ordenTarget == ORDEN_COMANDA_PRIMERO, R.drawable.bar_primero_draw, R.drawable.bar_primero1);
        animaTarget(ivSegundo, ordenTarget == ORDEN_COMANDA_SEGUNDO, R.drawable.bar_comida_draw, R.drawable.bar_comida1);
        animaTarget(ivPostre, ordenTarget == ORDEN_COMANDA_POSTRE, R.drawable.bar_postre_draw, R.drawable.bar_postre1);
    }

    private void limpiaAnimacionDropTargets() {
        actualizaAnimacionDropTarget(Integer.MIN_VALUE);
        interfaceLineaVentas.onClickOrden(ordenSel);
    }

    private void animaTarget(androidx.appcompat.widget.AppCompatImageView v, boolean activo, int drawableSel, int drawableNormal) {
        v.setBackgroundDrawable(context.getDrawable(activo ? drawableSel : drawableNormal));
        v.animate()
                .scaleX(activo ? 1.4f : 1.0f)
                .scaleY(activo ? 1.4f : 1.0f)
                .alpha(activo ? 1.0f : 0.85f)
                .setDuration(120)
                .start();
    }

    private void inicializaDropTargetsExternos() {
        ivEntrante.setOnDragListener(creaDropListener(ORDEN_COMANDA_ENTRANTE));
        ivPrimero.setOnDragListener(creaDropListener(ORDEN_COMANDA_PRIMERO));
        ivSegundo.setOnDragListener(creaDropListener(ORDEN_COMANDA_SEGUNDO));
        ivPostre.setOnDragListener(creaDropListener(ORDEN_COMANDA_POSTRE));
    }

    private View.OnDragListener creaDropListener(final int ordenDestino) {
        return (v, event) -> {
            switch (event.getAction()) {
                case android.view.DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case android.view.DragEvent.ACTION_DRAG_ENTERED:
                    actualizaAnimacionDropTarget(ordenDestino);
                    dragOrdenTarget = ordenDestino;
                    return true;
                case android.view.DragEvent.ACTION_DRAG_EXITED:
                    limpiaAnimacionDropTargets();
                    dragOrdenTarget = Integer.MIN_VALUE;
                    return true;
                case android.view.DragEvent.ACTION_DROP:
                    Object localState = event.getLocalState();
                    if (adapterLineaVentas != null && localState instanceof String) {
                        try {
                            int fromReal = Integer.parseInt((String) localState);
                            if (adapterLineaVentas.onItemDropToOrdenFromRealIndex(fromReal, ordenDestino)) {
                                if (interfaceLineaVentas.lineasPendientes()) ivEnviarComanda.setVisibility(View.VISIBLE);
                                else ivEnviarComanda.setVisibility(View.INVISIBLE);
                            }
                        } catch (NumberFormatException ignored) {}
                    }
                    limpiaAnimacionDropTargets();
                    dragOrdenTarget = Integer.MIN_VALUE;
                    return true;
                case android.view.DragEvent.ACTION_DRAG_ENDED:
                    limpiaAnimacionDropTargets();
                    dragOrdenTarget = Integer.MIN_VALUE;
                    return true;
                default:
                    return false;
            }
        };
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.fragmentActivity = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void mostrarMinMax() {
        RecyclerView.LayoutManager layout = rvLineaVentas.getLayoutManager();
        if (layout  instanceof LinearLayoutManager){
            boolean muestro = false;

            int min = ((LinearLayoutManager) layout).findFirstCompletelyVisibleItemPosition();
            int max = ((LinearLayoutManager) layout).findLastCompletelyVisibleItemPosition();

            if (min == 0)
                ivMin.setVisibility(View.INVISIBLE);
            else {
                ivMin.setVisibility(View.VISIBLE);
                muestro = true;
            }

            if (max == listaLineaVentas.size()-1)
                ivMax.setVisibility(View.INVISIBLE);
            else {
                ivMax.setVisibility(View.VISIBLE);
                muestro  = true;
            }


        }
    }


}
