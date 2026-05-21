package es.quatroges.qgestpv_v3;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import java.util.Locale;

/**
 * Created by carlos on 07/07/2015.
 */
public class FragmentConfigBd extends Fragment {
    private static final String TAG = "FragmentConfigBd";

    private static  boolean iniciado;

    private RadioGroup rgBDAccion;
    private CheckBox chkNoBorrarConfig, chkSincronizaAuto;
    private static String accion;

    public static boolean isNoBorraConfig() {
        return noBorraConfig;
    }

    public static void setNoBorraConfig(boolean noBorraConfig) {
        FragmentConfigBd.noBorraConfig = noBorraConfig;
    }

    public static boolean isSincronizaAuto() {
        return sincronizaAuto;
    }

    public static void setSincronizaAuto(boolean sincronizaAuto) {
        FragmentConfigBd.sincronizaAuto = sincronizaAuto;
    }

    private static boolean noBorraConfig, sincronizaAuto;
    View rootView = null;


    public static String getAccion() {
        return accion;
    }

    public static void setAccion(String accion) {
        FragmentConfigBd.accion = accion;
    }



    public interface ActivityCommunicator{
        public Bundle bdGetDatos();
    }

    private static ActivityCommunicator activityCommunicator;

    public Bundle MandaDatos() {
        if (!iniciado){
            return  null;
        }

        Bundle bundle = new Bundle();
        bundle.putString("bdaccion", getAccion());
        bundle.putBoolean("noborraconfig", isNoBorraConfig());
        bundle.putBoolean("sincronizaauto", isSincronizaAuto());

        return bundle;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityCommunicator = (ActivityCommunicator) activity;
        iniciado = false;

    }

    public static FragmentConfigBd newInstance() {
        FragmentConfigBd  fragment= new FragmentConfigBd();

        setAccion("");
        setNoBorraConfig(true);
        setSincronizaAuto(true);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setAccion("");
        setNoBorraConfig(true);
        Bundle bundle = activityCommunicator.bdGetDatos();
        boolean valor = bundle.getBoolean("sincronizaauto",true);
        setSincronizaAuto(valor);
        rootView = inflater.inflate(R.layout.fragment_config_bd, container, false);

        rgBDAccion = rootView.findViewById(R.id.rgBDAccion);

        rgBDAccion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = rootView.findViewById(checkedId);
                setAccion(rb.getTag().toString().toUpperCase(Locale.getDefault()));
            }
        });

        chkNoBorrarConfig = rootView.findViewById(R.id.chkNoBorrarConfig);
        chkNoBorrarConfig.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setNoBorraConfig(b);
            }
        });

        chkSincronizaAuto = rootView.findViewById(R.id.chkSincronizaAuto);
        chkSincronizaAuto.setChecked(isSincronizaAuto());
        chkSincronizaAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setSincronizaAuto(b);
            }
        });


        iniciado = true;
        return rootView;
    }


}
