package es.quatroges.qgestpv_v3.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by carlos on 30/12/2015.
 */
public class BTDriverSCREEN {

    private static final String TAG = "BluetoothChatService";
    private final Handler mHandler;
    private static Context context;

    private static int mState;
    public static final int STATE_STOP = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;

    private static BluetoothDevice mmDevice;
    private static String bufferTexto;

    @SuppressLint({"NewApi"})
    public BTDriverSCREEN(Context context, Handler handler) {
        mState = STATE_STOP;
        this.mHandler = handler;
        bufferTexto = "";
        BTDriverSCREEN.context = context;
    }

    public synchronized void setState(int state) {
        Log.d("BluetoothChatService", "setState() " + mState + " -> " + state);
        mState = state;
        this.mHandler.obtainMessage(1, state, -1).sendToTarget();
    }

    public synchronized int getState() {
        return mState;
    }

    public synchronized String getBufferTexto() {
        bufferTexto = "<style>\n" +
                "body {\n" +
                "  font-family: monospace;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>" +
                bufferTexto +
                "</body>";
        return bufferTexto;
    }

    public synchronized void setBufferTexto(String bufferTexto) {
        BTDriverSCREEN.bufferTexto = "";
    }

    public synchronized void connect(BluetoothDevice device) {
        mmDevice = device;
        Message msg = this.mHandler.obtainMessage(4);
        Bundle bundle = new Bundle();

        bundle.putString("device_name","pantalla");
        msg.setData(bundle);
        this.mHandler.sendMessage(msg);
        this.setState(STATE_CONNECTED);
        bufferTexto = "";
    }

    //public synchronized void connectSocket(BluetoothSocket socket, BluetoothDevice device) {
    public synchronized void connectSocket() {

        Log.d(TAG, "connectSocket");

        Message msg = this.mHandler.obtainMessage(4);
        Bundle bundle = new Bundle();

        /*
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        bundle.putString("device_name", mmDevice.getName());
         */
        bundle.putString("device_name", "pantalla");
        msg.setData(bundle);
        this.mHandler.sendMessage(msg);
        this.setState(ClaseBluetoothPrintConstantes.STATE_CONNECTED_SOCKET);
        bufferTexto = "";
    }

    public synchronized void disconnectSocket() {
        Message msg = this.mHandler.obtainMessage(4);
        Bundle bundle = new Bundle();
        /*
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        bundle.putString("device_name", mmDevice.getName());
         */
        bundle.putString("device_name", "pantalla");
        msg.setData(bundle);
        this.mHandler.sendMessage(msg);
        this.setState(ClaseBluetoothPrintConstantes.STATE_CONNECTED);
        bufferTexto = "";

    }


    public synchronized void stop() {
        Log.d("BluetoothChatService", "stop");
        this.setState(STATE_STOP);
    }

    public static void BT_Write(byte[] out) {
        if(mState == ClaseBluetoothPrintConstantes.STATE_CONNECTED_SOCKET) {
            bufferTexto += new String(out);;
        }
    }

    public static boolean InitPrinter() {
        bufferTexto = "";
        return true;
    }

    public static void WakeUpPritner() {
    }

    public static void Begin() {
        WakeUpPritner();
        InitPrinter();
    }

    public static void CR() {
        byte[] cmd = new byte[]{(byte)10};
        bufferTexto += new String(cmd);
    }


}
