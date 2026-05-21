package es.quatroges.qgestpv_v3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.bluetooth.ClaseBluetooth;

/**
 * Created by carlos on 07/07/2015.
 */
public class FragmentConfigPrinter extends Fragment {
    private static final String TAG = "FragmentConfigPrinter";
    private static TextView tBTMac, tBTName;

    private static CheckBox chkComandaApp, chkComandaTpv, chkCuentapteApp, chkCuentapteTpv, chkCuentaApp, chkCuentaTpv;
    private static Spinner spinBTModelo;

    private Button bScan;

    private boolean bDescubrirBTIniciado = false;

    private static boolean inicado;

    public interface ActivityCommunicator{
        public Bundle printerGetDatos();
    }

    ActivityCommunicator activityCommunicator;

    private ClaseBluetooth bt;
    private int timeOut = 0;

    public Bundle MandaDatos() {

        if (! inicado) {
            return null;
        }

        Bundle bundle = new Bundle();
        bundle.putString("btmodelo", spinBTModelo.getSelectedItem().toString());
        bundle.putString("btmac", tBTMac.getText().toString().substring(this.getResources().getText(R.string.strBTMac).length()));
        bundle.putString("btname", tBTName.getText().toString().substring(this.getResources().getText(R.string.strBTName).length()));

        boolean comandaApp = chkComandaApp.isChecked();
        boolean comandaTpv = chkComandaTpv.isChecked();
        boolean cuentaPteApp = chkCuentapteApp.isChecked();
        boolean cuentaPteTpv = chkCuentapteTpv.isChecked();
        boolean cuentaApp = chkCuentaApp.isChecked();
        boolean cuentaTpv = chkCuentaTpv.isChecked();

        //  permitimos que no se imprima nada
        /*
        if (! comandaApp && ! comandaTpv) {
            comandaTpv = true;
        }
        if (! cuentaPteApp && ! cuentaPteTpv) {
            cuentaPteTpv = true;
        }
        if (! cuentaApp && ! cuentaTpv) {
            cuentaTpv = true;
        }
        */

        bundle.putBoolean("comandaapp",comandaApp);
        bundle.putBoolean("comandatpv",comandaTpv);
        bundle.putBoolean("cuentapteapp",cuentaPteApp);
        bundle.putBoolean("cuentaptetpv",cuentaPteTpv);
        bundle.putBoolean("cuentaapp",cuentaApp);
        bundle.putBoolean("cuentatpv",cuentaTpv);

        return bundle;
    }

    public static FragmentConfigPrinter newInstance() {
        FragmentConfigPrinter  fragment= new FragmentConfigPrinter();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityCommunicator = (ActivityCommunicator) activity;
        inicado = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_config_printer, container, false);

        bDescubrirBTIniciado = false;

        tBTName = rootView.findViewById(R.id.tBTName);
        tBTMac = rootView.findViewById(R.id.tBTMac);

        bScan = rootView.findViewById(R.id.bScan);

        spinBTModelo = rootView.findViewById(R.id.spinBTModelo);

        chkComandaApp = rootView.findViewById(R.id.chkComandaAPP);
        chkComandaTpv = rootView.findViewById(R.id.chkComandaTPV);
        chkCuentapteApp = rootView.findViewById(R.id.chkCuentaPdteAPP);
        chkCuentapteTpv = rootView.findViewById(R.id.chkCuentaPdteTPV);
        chkCuentaApp = rootView.findViewById(R.id.chkCuentaAPP);
        chkCuentaTpv = rootView.findViewById(R.id.chkCuentaTPV);

        Bundle bdatos = activityCommunicator.printerGetDatos();

        tBTName.setText(this.getResources().getText(R.string.strBTName) + bdatos.getString("btname"));
        tBTMac.setText(this.getResources().getText(R.string.strBTMac) + bdatos.getString("btmac"));

        chkComandaApp.setChecked(bdatos.getBoolean("comandaapp"));
        chkComandaTpv.setChecked(bdatos.getBoolean("comandatpv"));
        chkCuentapteApp.setChecked(bdatos.getBoolean("cuentapteapp"));
        chkCuentapteTpv.setChecked(bdatos.getBoolean("cuentaptetpv"));
        chkCuentaApp.setChecked(bdatos.getBoolean("cuentaapp"));
        chkCuentaTpv.setChecked(bdatos.getBoolean("cuentatpv"));

        bScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciaDescubrirBT();
            }
        });


        ArrayList<String> btModelos = new ArrayList<String>();
        btModelos.add(getActivity().getBaseContext().getResources().getString(R.string.strConfigBTModel_SM5802BL));
        btModelos.add(getActivity().getBaseContext().getResources().getString(R.string.strConfigBTModel_IPDA045));
        btModelos.add(getActivity().getBaseContext().getResources().getString(R.string.strConfigBTModel_SCREEN));


        ArrayAdapter<String> adapterBtModelos = new ArrayAdapter<String>(
                getActivity().getBaseContext(),android.R.layout.simple_spinner_dropdown_item, btModelos);
        spinBTModelo.setAdapter(adapterBtModelos);

        if (btModelos.indexOf(bdatos.getString("btmodelo").toUpperCase()) >= 0) {
            spinBTModelo.setSelection(btModelos.indexOf(bdatos.getString("btmodelo").toUpperCase()));
        }

        inicado = true;
        return rootView;
    }


    private void iniciaDescubrirBT() {
        if (bDescubrirBTIniciado)
            return;
        bDescubrirBTIniciado= true;
        // Launch the DeviceListActivity to see devices and do scan
        Intent serverIntent = new Intent(getActivity().getBaseContext(), ActivityBTDeviceList.class);
        startActivityForResult(serverIntent, ClaseBluetooth.REQUEST_SCAN_DEVICE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {

            case ClaseBluetooth.REQUEST_SCAN_DEVICE:
                addLog("onActivityResult: requestCode==REQUEST_SCAN_DEVICE");
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    addLog("resultCode==OK");
                    // Get the device MAC address
                    String address = data.getExtras().getString(ActivityBTDeviceList.EXTRA_DEVICE_ADDRESS);
                    addLog("onActivityResult: got device=" + address);
                    // Get the device Name
                    String name = data.getExtras().getString(ActivityBTDeviceList.EXTRA_DEVICE_NAME);
                    addLog("onActivityResult: got device name=" + name);

                    tBTName.setText(this.getResources().getText(R.string.strBTName)+name);
                    tBTMac.setText(this.getResources().getText(R.string.strBTMac)+address);
                }
                bDescubrirBTIniciado = false;
                break;

        }
    }


    public void addLog(String s) {
        Log.d(TAG, s);
    }



}
