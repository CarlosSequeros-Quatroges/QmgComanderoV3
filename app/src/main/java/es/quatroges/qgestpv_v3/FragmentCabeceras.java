package es.quatroges.qgestpv_v3;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.datos.listas.cabeceras.ClaseCabeceras;
import es.quatroges.qgestpv_v3.datos.listas.cabeceras.RvAdapterCabeceras;
import es.quatroges.qgestpv_v3.utils.OnSwipeTouchListener;

public class FragmentCabeceras extends Fragment
        implements  SearchView.OnQueryTextListener{

    FragmentActivity fragmentActivity;
    Context context;
    private ArrayList<ClaseCabeceras> listaCabeceras;
    public int cabeceraSel;

    private androidx.appcompat.widget.SearchView svCabecera;
    private RvAdapterCabeceras adapterCabeceras;

    private boolean fragmentAmpliado;

    public  boolean isFragmentAmpliado() {return fragmentAmpliado;}

    private RecyclerView rvCabeceras;
    private androidx.appcompat.widget.AppCompatImageView ivMin, ivMax, ivVerMas;

    public static CountDownTimer timerCabecera;
    public static int milisg;

    public static FragmentCabeceras fragmentCabeceras;

    public void setTimer(int milisg) {
        FragmentCabeceras.milisg = milisg;
        if (FragmentCabeceras.timerCabecera != null ) {
            FragmentCabeceras.timerCabecera.cancel();
            FragmentCabeceras.timerCabecera = null;

        }

    }


    public void posicionaCabecera() {
        rvCabeceras.scrollToPosition(cabeceraSel);
    }

    public void actualizaCabeceras() {
        if (interfaceCabeceras != null){
            Bundle bundle = interfaceCabeceras.cargaCabeceras();
            cabeceraSel = bundle.getInt("cabeceraSel",1);
            milisg = bundle.getInt("milisg",5);
            listaCabeceras = bundle.getParcelableArrayList("cabeceras");
            rvCabeceras.setAdapter(null);
            adapterCabeceras = new RvAdapterCabeceras(context, listaCabeceras, this) ;
            rvCabeceras.setAdapter(adapterCabeceras);

        }
    }

    public void ampliarCabeceras(){
        ampliaCabeceras();
        interfaceCabeceras.fc_ocultaElementos(false);

    }

    public void cancelTimer(){
        if (timerCabecera != null ) {
            timerCabecera.cancel();
        }
    }

    public void resetTimer(){
        if (timerCabecera != null ) {
            timerCabecera.cancel();
        }
        iniciaTimer();
    }

    public interface InterfaceCabeceras {
        Bundle cargaCabeceras();
        void onClickCabecera(int i);
        void fc_ocultaElementos(boolean visible);
    }

    InterfaceCabeceras interfaceCabeceras;


    public static FragmentCabeceras newInstance(int milisg) {
        FragmentCabeceras.milisg = milisg;
        if (fragmentCabeceras == null )fragmentCabeceras= new FragmentCabeceras();
        return fragmentCabeceras;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.fragmentActivity = (FragmentActivity)context;
            this.context = context;
            interfaceCabeceras = (InterfaceCabeceras) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.cabeceras_fragment,container,false);

        rvCabeceras = rootView. findViewById(R.id.rvCabeceras);
        ivMin = rootView.findViewById(R.id.ivMin);
        ivMax = rootView.findViewById(R.id.ivMax);
        ivVerMas = rootView.findViewById(R.id.ivVerMas);

        //rvCabeceras.setHasFixedSize(true);

        svCabecera = rootView.findViewById(R.id.svCabeceras);

        fragmentAmpliado = false;
        LinearLayoutManager llmCategorias = new LinearLayoutManager( context,LinearLayoutManager.HORIZONTAL,false);

        rvCabeceras.setLayoutManager(llmCategorias);
        svCabecera.setVisibility(View.GONE);

        Bundle bundle = interfaceCabeceras.cargaCabeceras();
        cabeceraSel = bundle.getInt("cabeceraSel",1);
        listaCabeceras = bundle.getParcelableArrayList("cabeceras");

        adapterCabeceras = new RvAdapterCabeceras(context, listaCabeceras, this) ;
        rvCabeceras.setAdapter(adapterCabeceras);

        setupSearchView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rvCabeceras.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    mostrarMinMax();
                }
            });
        }
        else {
            rvCabeceras.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    mostrarMinMax();
                }
            });
        }


        rvCabeceras.setOnTouchListener(new OnSwipeTouchListener(context){
            @Override
            public void onSwipeDown() {
                if (!fragmentAmpliado ) {
                    ampliaCabeceras();
                    interfaceCabeceras.fc_ocultaElementos(false);
                }
            }

        });

        ivVerMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (! fragmentAmpliado) {
                    ampliaCabeceras();
                    interfaceCabeceras.fc_ocultaElementos(false);
                }
                else {
                    reduceCabeceras(cabeceraSel);
                    interfaceCabeceras.fc_ocultaElementos(true);

                }
            }

        });

        return rootView;
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

    private void ampliaCabeceras() {
        final RecyclerView.LayoutManager layout = rvCabeceras.getLayoutManager();
        if (layout  instanceof LinearLayoutManager && fragmentAmpliado == false) {



            View view  = View.inflate(context,R.layout.cabeceras_cardview,null);
            view.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            float vpWidth = view.getMeasuredWidth()/displayMetrics.density;
            int cols = (int)( dpWidth /vpWidth);
            if (cols <= 1) cols = 2;

            GridLayoutManager llmCategorias = new GridLayoutManager(context, cols, GridLayoutManager.VERTICAL, false);
            rvCabeceras.setLayoutManager(llmCategorias);
            ivMin.setVisibility(View.INVISIBLE);
            ivMax.setVisibility(View.INVISIBLE);
            ivVerMas.setRotation((float)180);
            fragmentAmpliado = true;
            iniciaTimer();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int min = ((LinearLayoutManager) layout).findFirstCompletelyVisibleItemPosition();
                    int max = ((LinearLayoutManager) layout).findLastCompletelyVisibleItemPosition();

                    if ((min <= 0) && (max >= listaCabeceras.size()-1)) {
                        svCabecera.setVisibility(View.GONE);
                    }
                    else{
                        svCabecera.setQuery("", false);
                        svCabecera.clearFocus();
                        svCabecera.setVisibility(View.VISIBLE);
                    }

                }
            }, 500);
        }
        else {
            reduceCabeceras(cabeceraSel);
        }

    }

    public void reduceCabeceras(int i) {
        //if (fragmentAmpliado == true) {
            fragmentAmpliado = false;
            LinearLayoutManager llmCategorias = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            rvCabeceras.setLayoutManager(llmCategorias);
            if (i != -1 && fragmentAmpliado == true) rvCabeceras.scrollToPosition(i);
            fragmentAmpliado = false;

            ivVerMas.setRotation((float)0);
            svCabecera.setVisibility(View.GONE);
            adapterCabeceras.getFilter().filter("");

            if (timerCabecera != null) timerCabecera.cancel();

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mostrarMinMax();
                }
            });

        //}
    }

    private void mostrarMinMax() {
        RecyclerView.LayoutManager layout = rvCabeceras.getLayoutManager();
        if (layout  instanceof LinearLayoutManager && fragmentAmpliado == false){
            boolean muestro = false;

            int min = ((LinearLayoutManager) layout).findFirstCompletelyVisibleItemPosition();
            int max = ((LinearLayoutManager) layout).findLastCompletelyVisibleItemPosition();

            if (min == 0)
                ivMin.setVisibility(View.INVISIBLE);
            else {
                ivMin.setVisibility(View.VISIBLE);
                muestro = true;
            }

            if (max == listaCabeceras.size()-1)
                ivMax.setVisibility(View.INVISIBLE);
            else {
                ivMax.setVisibility(View.VISIBLE);
                muestro  = true;
            }

            /*
            if (!muestro)
                    ivVerMas.setVisibility(View.INVISIBLE);
                else
                    ivVerMas.setVisibility(View.VISIBLE);
            */

        }
        else {
            ivMax.setVisibility(View.GONE);
            ivMin.setVisibility(View.GONE);
        }
    }

    private void iniciaTimer() {
        if (timerCabecera == null){
            timerCabecera = new CountDownTimer(milisg,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    if  (fragmentAmpliado) {
                        reduceCabeceras(-1);
                        interfaceCabeceras.fc_ocultaElementos(true);
                    }

                    timerCabecera.cancel();
                }

            };
        }
        timerCabecera.start();

    }

    private void setupSearchView() {
        svCabecera.setIconifiedByDefault(false);
        svCabecera.setOnQueryTextListener(this);
        svCabecera.setSubmitButtonEnabled(false);
        svCabecera.setQueryHint(context.getResources().getString(R.string.svCabecera));
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapterCabeceras.getFilter().filter(s);
        resetTimer();
        return false;
    }

}
