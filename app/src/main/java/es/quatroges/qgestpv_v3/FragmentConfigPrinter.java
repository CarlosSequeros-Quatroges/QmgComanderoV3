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
import es.quatroges.qgestpv_v3.bluetooth.ClaseBluetoothPrintConstantes;
import es.quatroges.qgestpv_v3.bluetooth.PermisosBluetooth;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;

/**
 * Created by carlos on 07/07/2015.
 */
public class FragmentConfigPrinter extends Fragment {
    private static final String TAG = "FragmentConfigPrinter";
    private static TextView tBTMac, tBTName;

    private static CheckBox chkComandaApp, chkComandaTpv, chkCuentapteApp, chkCuentapteTpv, chkCuentaApp, chkCuentaTpv;
    private static Spinner spinBTModelo;

    private Button bScan;
    private Button bTestPrint;

    private boolean bDescubrirBTIniciado = false;
    private volatile boolean bTestImpresionEnCurso = false;

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
        bTestPrint = rootView.findViewById(R.id.bTestPrint);

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

        bTestPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciaTestImpresion();
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


    /**
     * Imprime un "Hola mundo..." con el modelo y la MAC seleccionados en pantalla (aunque aun no se hayan guardado).
     * Al volver a la pantalla principal, ActivityInicio vuelve a aplicar la impresora configurada.
     */
    private void iniciaTestImpresion() {
        if (bTestImpresionEnCurso) return;

        final Activity activity = getActivity();
        if (activity == null) return;

        final String modelo = spinBTModelo.getSelectedItem().toString();
        final String mac = tBTMac.getText().toString()
                .substring(getResources().getText(R.string.strBTMac).length()).trim();

        if (modelo.equalsIgnoreCase(getString(R.string.strConfigBTModel_SCREEN))) {
            ClaseUtils.Aviso.mostrarAviso("Aviso", getString(R.string.strBTTestSoloImpresora), activity);
            return;
        }

        final boolean necesitaMac = modelo.equalsIgnoreCase(getString(R.string.strConfigBTModel_SM5802BL));
        if (necesitaMac && (mac.length() < 17 || mac.startsWith("00:00:00:00:00"))) {
            ClaseUtils.Aviso.mostrarAviso("Aviso", getString(R.string.strBTTestSinImpresora), activity);
            return;
        }

        // Si faltan permisos se piden ahora; el usuario vuelve a pulsar cuando los conceda
        if (!PermisosBluetooth.solicitarSiFaltan(activity)) return;

        bTestImpresionEnCurso = true;
        bTestPrint.setEnabled(false);
        ClaseUtils.AvisoToast.mostrarAviso("BlueTooth", getString(R.string.strBTTestConectando), activity, 1500);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String error = null;
                try {
                    error = ejecutaTestImpresion(activity, modelo, mac, necesitaMac);
                } catch (Exception e) {
                    Log.e(TAG, "Error en la prueba de impresion", e);
                    error = String.valueOf(e.getMessage());
                } finally {
                    // La prueba deja la impresora desconectada: ActivityInicio reconecta al volver
                    try {
                        ClaseBluetooth.detener();
                    } catch (Exception e) {
                        Log.w(TAG, "Error desconectando tras la prueba", e);
                    }
                }
                final String fError = error;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bTestImpresionEnCurso = false;
                        if (bTestPrint != null) bTestPrint.setEnabled(true);
                        if (activity.isFinishing() || activity.isDestroyed()) return;
                        if (fError == null) {
                            ClaseUtils.AvisoToast.mostrarAviso("BlueTooth", getString(R.string.strBTTestEnviado), activity, 2000);
                        } else {
                            ClaseUtils.Aviso.mostrarAviso("Error", fError, activity);
                        }
                    }
                });
            }
        }).start();
    }

    /** Conecta con la impresora indicada, imprime el texto de prueba y cierra el socket. Devuelve null si todo ha ido bien. */
    private String ejecutaTestImpresion(Activity activity, String modelo, String mac, boolean necesitaMac) throws InterruptedException {
        // La capa Bluetooth es estatica: si nadie la ha inicializado todavia, lo hacemos con esta actividad
        if (ClaseBluetooth.getApplicationContext() == null) {
            new ClaseBluetooth(activity, activity.getApplicationContext(), null, modelo);
        }

        // Paramos el temporizador de reconexion para que no interfiera y arrancamos de cero con el modelo/MAC de pantalla
        ClaseBluetooth.detenerTimer();
        if (ClaseBluetooth.iniciaBlueToothAdapter(true) == null) {
            return getString(R.string.strErrorBTNoDisponible);
        }
        if (necesitaMac && !ClaseBluetooth.getBlueToothAdapter().isEnabled()) {
            return getString(R.string.strBTTestBluetoothApagado);
        }

        ClaseBluetooth.configuraModelo(modelo);
        ClaseBluetooth.setRemoteDevice(mac);
        ClaseBluetooth.conectarImpresoraBT();

        // Espera a que el driver conecte con el dispositivo (maximo 10 s).
        // Si el enlace ya existia no llega ACL_CONNECTED: se comprueba el socket directamente.
        int intentos = 50;
        int estado = ClaseBluetooth.checkPrinterStatus();
        while (intentos-- > 0
                && estado != ClaseBluetoothPrintConstantes.STATE_CONNECTED
                && estado != ClaseBluetoothPrintConstantes.STATE_CONNECTED_SOCKET
                && estado != ClaseBluetoothPrintConstantes.STATE_CONNECTING_ERROR) {
            Thread.sleep(200);
            ClaseBluetooth.confirmaEnlaceSiSocketAbierto();
            estado = ClaseBluetooth.checkPrinterStatus();
        }
        addLog("test impresion: estado tras conectar = " + estado);

        // Con SM5802 esperamos ademas a que el hilo de conexion haya terminado (maximo 5 s)
        if (necesitaMac) {
            intentos = 25;
            while (intentos-- > 0 && !ClaseBluetooth.socketSM5802Conectado()) {
                Thread.sleep(200);
            }
            addLog("test impresion: socket conectado = " + ClaseBluetooth.socketSM5802Conectado());
        }

        // Abre el socket de datos
        intentos = 10;
        while (intentos-- > 0 && ClaseBluetooth.checkPrinterStatus() != ClaseBluetoothPrintConstantes.STATE_CONNECTED_SOCKET) {
            ClaseBluetooth.conectarSocketBT();
            Thread.sleep(200);
        }

        if (ClaseBluetooth.checkPrinterDriverNull() || !ClaseBluetooth.checkPrinterConectado()) {
            return getString(R.string.strVentasAvisoImpresoraNoDisponible).replace("\\n", "\n");
        }

        String resultado;
        ClaseBluetooth.set_imprimiendo(true);
        try {
            resultado = ClaseBluetooth.imprimeTiquetTest(activity, getString(R.string.strBTTestHolaMundo));
        } finally {
            ClaseBluetooth.set_imprimiendo(false);
        }
        if ("ERR_CON".equals(resultado)) {
            return getString(R.string.strVentasAvisoImpresoraNoDisponible).replace("\\n", "\n");
        }

        // pequena espera para que la impresora reciba todo; la desconexion completa la hace detener() en el finally
        Thread.sleep(500);
        return null;
    }

    public void addLog(String s) {
        Log.d(TAG, s);
    }



}
