package es.quatroges.qgestpv_v3;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import java.util.Set;

import es.quatroges.qgestpv_v3.bluetooth.ClaseBluetooth;
import es.quatroges.qgestpv_v3.configuracion.ClaseConfiguracion;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;


/**
 * This Activity appears as a dialog. It lists any paired devices and
 * devices detected in the area after discovery. When a device is chosen
 * by the user, the MAC address of the device is sent back to the parent
 * Activity in the result Intent.
 */
public class ActivityBTDeviceList extends Activity {
    // Debugging
    private static final String TAG = "DeviceListActivity";

    ClaseBluetooth BT;
    Context context;
    // Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRA_DEVICE_NAME = "device_name";

    // Member fields
    //  private BluetoothAdapter mBtAdapter;

    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        addLog("+++OnCreate+++");
        // Setup the window
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_btdevice_list);

        // Set result CANCELED incase the user backs out
        setResult(Activity.RESULT_CANCELED);

        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_btdevice_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_btdevice_name);

        // Find and set up the ListView for paired devices
        ListView pairedListView = findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Find and set up the ListView for newly discovered devices
        ListView newDevicesListView = findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        // Get the local Bluetooth adapter
        BT = new ClaseBluetooth(this, getApplicationContext(), null, ClaseConfiguracion.btModelo);
        BT.iniciaBlueToothAdapter(true);

        initDeviceList();

        // Initialize the button to perform device discovery
        scanButton = findViewById(R.id.button_scan);
        scanButton.setText(getResources().getText(R.string.strBTScan));
        scanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                doDiscovery();
                //v.setVisibility(View.GONE);
            }
        });


        addLog("+++OnCreate+++ DONE");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        addLog("+++OnDestroy+++");

        // Make sure we're not doing discovery anymore
        if (ClaseBluetooth.getBlueToothAdapter() != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                ClaseUtils.Aviso.mostrarAviso("Aviso...",context.getResources().getText(R.string.strConsultaAvisoPermisoBluetooth).toString(),context);
                return;
            }
            ClaseBluetooth.getBlueToothAdapter().cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    void initDeviceList() {
        //empty list first?
        if (mPairedDevicesArrayAdapter.getCount() > 0)
            mPairedDevicesArrayAdapter.clear();
        if (mNewDevicesArrayAdapter.getCount() > 0)
            mNewDevicesArrayAdapter.clear();

        // Get a set of currently paired devices
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ClaseUtils.Aviso.mostrarAviso("Aviso...",context.getResources().getText(R.string.strConsultaAvisoPermisoBluetooth).toString(),context);
            return;
        }
        Set<BluetoothDevice> pairedDevices = ClaseBluetooth.getBlueToothAdapter().getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.strBTNoAsociados).toString();
            mPairedDevicesArrayAdapter.add(noDevices);
        }
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {
        addLog("doDiscovery()");
        initDeviceList();

        if (scanButton.getText().toString().equals(getResources().getText(R.string.strBTDetener))) {
            if (BT.getBlueToothAdapter() != null) {

                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    ClaseUtils.Aviso.mostrarAviso("Aviso...",context.getResources().getText(R.string.strConsultaAvisoPermisoBluetooth).toString(),context);
                    return;
                }
                if (BT.getBlueToothAdapter().isDiscovering()) {
                    addLog("stop discovery requested");
                    BT.getBlueToothAdapter().cancelDiscovery();
                    scanButton.setText(getResources().getText(R.string.strBTScan));
                    return;
                }
            }
        }
        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.strBTEscaneando);

        // Turn on sub-title for new devices
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (BT.getBlueToothAdapter().isDiscovering()) {
            BT.getBlueToothAdapter().cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        BT.getBlueToothAdapter().startDiscovery();
        scanButton.setText(getResources().getText(R.string.strBTDetener));
        addLog("Discovery() started");
    }

    // The on-click listener for all devices in the ListViews
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            addLog("OnItemClickListener()");
            // Cancel discovery because it's costly and we're about to connect
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                ClaseUtils.Aviso.mostrarAviso("Aviso...",context.getResources().getText(R.string.strConsultaAvisoPermisoBluetooth).toString(),context);
                return;
            }
            BT.getBlueToothAdapter().cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            String name = info.substring(0, info.indexOf("\n"));

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            intent.putExtra(EXTRA_DEVICE_NAME, name);

            addLog("setResult=OK");
            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            addLog("Discovery BroadcastReceiver()");

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                addLog("ACTION_FOUND");
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // If it's already paired, skip it, because it's been listed already

                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ClaseUtils.Aviso.mostrarAviso("Aviso...",context.getResources().getText(R.string.strConsultaAvisoPermisoBluetooth).toString(),context);
                    return;
                }
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    addLog("adding new bonded device: " + device.getName() + " " + device.getAddress());
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.strBTSelectDevice);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.strBTNoEncontrados).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
                scanButton.setText(getResources().getText(R.string.strBTScan));
            }
        }
    };
    public void addLog(String s) {
        Log.d(TAG, s );
    }

}
