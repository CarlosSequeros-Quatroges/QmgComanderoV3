package es.quatroges.qgestpv_v3;


import android.app.Activity;
import android.content.Context;
import android.graphics.Insets;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import es.quatroges.qgestpv_v3.utils.ClaseUtils;


/**
 * Created by carlos on 07/07/2015.
 */
public class FragmentConfigParametros extends Fragment {
    private static final String TAG = "FragmentConfigWs";

    private static EditText etEmpresa, etCodigo,etTimerFragment;
    private static TextView  tvResolucion, tvLayout;
    private static CheckBox chkDemo,chkCargaLineasVenta, chkClaveCamareros, chkPax,
                            chkEfectivo,chkTarjeta,chkEfeTar,chkCuentacasa,chkCredito,chkPension, chkMantenerVenta,
                            chkMostrarProductosAuto, chkMantenerProductos, chkCompacto;


    private static Context context = null;

    private static boolean iniciado;
    public interface ActivityCommunicator{
        public Bundle parametrosGetDatos();
    }

    ActivityCommunicator activityCommunicator;


    public Bundle MandaDatos() {
        if (! iniciado) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean("demo", chkDemo.isChecked());
        bundle.putString("empresa", etEmpresa.getText().toString().trim());
        bundle.putString("codigo", etCodigo.getText().toString().trim());

        bundle.putBoolean("cargaLineasVenta", chkCargaLineasVenta.isChecked());
        bundle.putBoolean("claveCamareros", chkClaveCamareros.isChecked());
        bundle.putBoolean("pax", chkPax.isChecked());
        bundle.putBoolean("mostrarProductosAuto",chkMostrarProductosAuto.isChecked());
        bundle.putBoolean("mantenerProductos",chkMantenerProductos.isChecked());

        bundle.putBoolean("pagoEfectivo",chkEfectivo.isChecked());
        bundle.putBoolean("pagoTarjeta",chkTarjeta.isChecked());
        bundle.putBoolean("pagoEfeTar",chkEfeTar.isChecked());
        bundle.putBoolean("pagoCuentacasa",chkCuentacasa.isChecked());
        bundle.putBoolean("pagoCredito",chkCredito.isChecked());
        bundle.putBoolean("pagoPension",chkPension.isChecked());
        bundle.putBoolean("mantenerVenta",chkMantenerVenta.isChecked());
        bundle.putBoolean("compacto",chkCompacto.isChecked());

        int ms = 5;
        try {
            ms = Integer.parseInt(etTimerFragment.getText().toString().trim());
        }
        catch (NumberFormatException ne){}

        bundle.putInt("timerFragment",ms);

        return bundle;
    }

    public static FragmentConfigParametros newInstance() {
        FragmentConfigParametros fragment= new FragmentConfigParametros();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityCommunicator = (ActivityCommunicator) activity;
        context = activity.getBaseContext();
        iniciado = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_config_parametros, container, false);
        etEmpresa=rootView.findViewById(R.id.etEmpresa);
        etCodigo=rootView.findViewById(R.id.etCodigo);
        etTimerFragment=rootView.findViewById(R.id.etTimerFragment);

        chkDemo = rootView.findViewById(R.id.chkDemo);
        chkCargaLineasVenta = rootView.findViewById(R.id.chkCargaLineasVenta);
        chkClaveCamareros = rootView.findViewById(R.id.chkClaveCamareros);
        chkPax = rootView.findViewById(R.id.chkPax);
        chkMostrarProductosAuto = rootView.findViewById(R.id.chkMostrarProductos);
        chkMantenerProductos = rootView.findViewById(R.id.chkMantenerProductos);
        chkCompacto = rootView.findViewById(R.id.chkCompacto);

        chkEfectivo = rootView.findViewById(R.id.chkEfectivo);
        chkTarjeta = rootView.findViewById(R.id.chkTarjeta);
        chkEfeTar = rootView.findViewById(R.id.chkEfetar);
        chkCuentacasa = rootView.findViewById(R.id.chkCuentacasa);
        chkCredito = rootView.findViewById(R.id.chkCredito);
        chkPension = rootView.findViewById(R.id.chkPension);

        chkMantenerVenta =rootView.findViewById(R.id.chkMantenerVenta);

        tvResolucion = rootView.findViewById(R.id.tvResolucion);
        tvLayout = rootView.findViewById(R.id.tvLayout);

        Point size = getScreenSize();

        tvResolucion.setText("Resolución: " +String.valueOf(size.x)+"x"+String.valueOf(size.y)+"("+String.valueOf(ClaseUtils.px2dp(size.x,getActivity()))+"x"+String.valueOf(ClaseUtils.px2dp(size.y,getActivity()))+"dpi)");
        tvLayout.setText("Layout: "+context.getResources().getString(R.string.sw));


        Bundle bdatos = activityCommunicator.parametrosGetDatos();

        etEmpresa.setText(bdatos.getString("empresa"));
        etCodigo.setText(bdatos.getString("codigo"));
        etTimerFragment.setText(String.valueOf(bdatos.getInt("timerFragment",5)));

        chkDemo.setChecked(bdatos.getBoolean("demo"));
        chkCargaLineasVenta.setChecked(bdatos.getBoolean("cargaLineasVenta"));
        chkClaveCamareros.setChecked(bdatos.getBoolean("claveCamareros"));
        chkPax.setChecked(bdatos.getBoolean("pax"));

        chkMostrarProductosAuto.setChecked(bdatos.getBoolean("mostrarProductosAuto"));
        chkMantenerProductos.setChecked(bdatos.getBoolean("mantenerProductos"));
        chkCompacto.setChecked(bdatos.getBoolean("compacto"));

        chkEfectivo.setChecked(bdatos.getBoolean("pagoEfectivo",true));
        chkTarjeta.setChecked(bdatos.getBoolean("pagoTarjeta",true));
        chkEfeTar.setChecked(bdatos.getBoolean("pagoEfeTar",false));
        chkCuentacasa.setChecked(bdatos.getBoolean("pagoCuentacasa",false));
        chkCredito.setChecked(bdatos.getBoolean("pagoCredito",false));
        chkPension.setChecked(bdatos.getBoolean("pagoPension",false));

        chkMantenerVenta.setChecked(bdatos.getBoolean("mantenerVenta",false));
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        iniciado = true;
        return rootView;
    }

    public Point getScreenSize() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = getActivity().getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            int x =  windowMetrics.getBounds().width() - insets.left - insets.right;
            int y =  windowMetrics.getBounds().height() - insets.top - insets.bottom;
            return  new Point(x,y);

        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int x =  displayMetrics.widthPixels;
            int y =  displayMetrics.heightPixels;
            return new Point(x,y);

        }
    }

}
