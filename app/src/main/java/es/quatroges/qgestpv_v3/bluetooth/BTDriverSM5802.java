package es.quatroges.qgestpv_v3.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.zj.btsdk.PrintPic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import es.quatroges.qgestpv_v3.utils.ClaseUtils;


/**
 * Created by carlos on 30/12/2015.
 */
public class BTDriverSM5802 {

    private static Context context;
    private static final String TAG = "BTDriverSM5802";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    private final Handler mHandler;
    private static BTDriverSM5802.ConnectThread mConnectThread;
    private static BTDriverSM5802.ConnectedThread mConnectedThread;

    private static BluetoothDevice mmDevice;
    private static BluetoothSocket mmSocket;
    private static boolean mmSocketException;

    public static BluetoothSocket getMmSocket() {
        return mmSocket;
    }

    public static boolean getSocketIsConnected() {
        if (mmSocket != null) {
            return mmSocket.isConnected();
        } else return false;
    }

    private static InputStream mmInStream;
    private static OutputStream mmOutStream;


    private static int mState;

    @SuppressLint({"NewApi"})
    public BTDriverSM5802(Context context, Handler handler) {
        mState = 0;
        this.mHandler = handler;
        this.context = context;
    }

    public synchronized void setState(int state) {
        Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;
        this.mHandler.obtainMessage(1, state, -1).sendToTarget();
    }

    public synchronized int getState() {
        return mState;
    }


    public synchronized boolean connect(BluetoothDevice device) {
        Log.d(TAG, "connect to: " + device);
        if (mState == ClaseBluetoothPrintConstantes.STATE_CONNECTING && this.mConnectThread != null) {
            this.mConnectThread.cancel();
            this.mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        mmDevice = device;
        int intentos = 3;
        do {
            try {
                mConnectThread = new BTDriverSM5802.ConnectThread();
                Log.d(TAG, "BT arranco connect ");
                mConnectThread.start();
                mConnectThread.join(1000);
                if (mmSocket != null)
                    intentos = 0;
                else
                    intentos--;
            } catch (InterruptedException e) {
                intentos--;
            }
        } while (intentos > 0);


        if (mmSocket != null) {
            setState(ClaseBluetoothPrintConstantes.STATE_CONNECTING);
            return true;
        } else {
            setState(ClaseBluetoothPrintConstantes.STATE_DISCONNECTED);
            return false;
        }


    }

    public synchronized void connectSocket() {

        Log.d(TAG, "connectSocket");
        if (this.mConnectThread != null) {
            this.mConnectThread.cancel();
            this.mConnectThread = null;
        }

        if (mmDevice != null && mmSocket != null) {
            mConnectedThread = new BTDriverSM5802.ConnectedThread();
            mConnectedThread.start();
            Message msg = this.mHandler.obtainMessage(4);
            Bundle bundle = new Bundle();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                //pepe aviso bt
                return;
            }
            bundle.putString("device_name", mmDevice.getName());
            msg.setData(bundle);
            this.mHandler.sendMessage(msg);
            this.setState(ClaseBluetoothPrintConstantes.STATE_CONNECTED_SOCKET);
        }
        else{
            setState(ClaseBluetoothPrintConstantes.STATE_DISCONNECTED);
        }
    }

    public synchronized void disconnectSocket() {

        try {
            if (mmSocket != null) mmSocket.close();
        }
        catch (IOException e){

        }

        setState(ClaseBluetoothPrintConstantes.STATE_CONNECTING);
    }


    public synchronized void stop() {
        Log.d(TAG, "stop");

        try {
            if (mmInStream != null) {
                mmInStream.close();
                mmInStream = null;
            }
        }
        catch (IOException e) {}

        try {
            if (mmOutStream != null) {
                mmOutStream.close();
                mmOutStream = null;
            }
        }
        catch (IOException e) {}

        try {
            if (mmSocket != null) {
                mmSocket.close();
                mmSocket = null;
            }
        }
        catch (IOException e) {}

        this.setState(ClaseBluetoothPrintConstantes.STATE_DISCONNECTING);
    }


    public static void BT_Write(byte[] out) {
        if(mState == ClaseBluetoothPrintConstantes.STATE_CONNECTED_SOCKET) {
            BTDriverSM5802.ConnectedThread r = mConnectedThread;
            r.write(out);

        }
    }

    public static void BT_Write(byte[] out, int dataLen) {
        if(mState == ClaseBluetoothPrintConstantes.STATE_CONNECTED_SOCKET) {
            BTDriverSM5802.ConnectedThread r = mConnectedThread;
            r.write(out, dataLen);
        }
    }

    public static void BT_ReadAll() {
        if(mState == ClaseBluetoothPrintConstantes.STATE_CONNECTED_SOCKET) {
            BTDriverSM5802.ConnectedThread r = mConnectedThread;
            r.readAll();
        }
    }

    private void connectionFailed() {
        this.setState(ClaseBluetoothPrintConstantes.STATE_LISTEN);
        Message msg = this.mHandler.obtainMessage(5);
        Bundle bundle = new Bundle();
        bundle.putString("toast", "Unable to connect device");
        msg.setData(bundle);
        this.mHandler.sendMessage(msg);
    }

    private void connectionLost() {
        this.setState(ClaseBluetoothPrintConstantes.STATE_CONNECTED);
        Message msg = this.mHandler.obtainMessage(5);
        Bundle bundle = new Bundle();
        bundle.putString("toast", "Device connection was lost");
        msg.setData(bundle);
        this.mHandler.sendMessage(msg);
    }

    public static boolean IsNoConnection() {
        return mState != 3;
    }

    public static boolean InitPrinter() {
        byte[] combyte = new byte[]{(byte)27, (byte)64};
        if(mState != 3) {
            return false;
        } else {
            BT_Write(combyte);
            return true;
        }
    }

    public static void WakeUpPritner() {
        byte[] b = new byte[3];

        try {
            BT_Write(b);
            Thread.sleep(100L);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static void Begin() {
        WakeUpPritner();
        InitPrinter();
    }


    public static void CR() {
        byte[] cmd = new byte[]{(byte)10};
        BT_Write(cmd);
    }

    public static void printImage(String path) {
        byte[] sendData = null;
        PrintPic pg = new PrintPic();
        pg.initCanvas(256);
        pg.initPaint();
        //pg.drawImage(0, 0, "/mnt/sdcard/icon.jpg");
        pg.drawImage(0, 0, path);

        sendData = pg.printDraw();
        BT_Write(sendData);
    }

    public static void printImageBMP(String bmpB64) {
        byte[] sendData = null;
        es.quatroges.qgestpv_v3.bluetooth.SM5802_utils.PrintPic pg = new es.quatroges.qgestpv_v3.bluetooth.SM5802_utils.PrintPic();
        pg.initCanvas(256);
        pg.initPaint();
        //pg.drawImage(0, 0, "/mnt/sdcard/icon.jpg");
        Bitmap bmp = ClaseUtils.base64ToBitmap(bmpB64);
        pg.drawImageBMP(0, 0, bmp);

        sendData = pg.printDraw();
        BT_Write(sendData);
    }

    public static void StatusInquiry() {

        BT_ReadAll();

        byte[] cmd = new byte[]{(byte)0, (byte)0, (byte)16, (byte)4, (byte)-2, (byte)0, (byte)0, (byte)16, (byte)4, (byte)-1};
        BT_Write(cmd, 10);
    }


    private class ConnectThread extends Thread {

        public   ConnectThread() {
            BluetoothSocket tmp = null;


            try {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    //pepe aviso bt
                    return;
                }
                tmp = mmDevice.createRfcommSocketToServiceRecord(BTDriverSM5802.MY_UUID);
            } catch (IOException var5) {
                Log.e(TAG, "BT create() failed", var5);
            }
            mmSocket = tmp;
            Log.e(TAG, "BT socket ");
        }

        public synchronized void run() {
            Log.i(TAG, "BEGIN mConnectThread");
            this.setName("ConnectThread");
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                //pepe aviso bt
                return;
            }
            BTDriverSM5802.this.mAdapter.cancelDiscovery();

            mmSocketException = false;
            try {
                Log.e(TAG, "BT intento conectar ");
                if (  mmSocket != null )
                    mmSocket.connect();
                else {
                    mmSocketException = true;
                    Log.e(TAG, "BT socketException = true");
                    this.cancel();
                }

            } catch (IOException var5) {
                mmSocketException = true;
                Log.e(TAG, "BT socketException = true");
                connectionFailed();
                this.cancel();
                return;
            }



            synchronized(BTDriverSM5802.this) {
                mConnectThread = null;
            }
            Log.d(TAG,"BT fin run connect...");
            /////BTDriverSM5802.this.connectSocket(mmSocket, mmDevice);
        }

        public void cancel() {
            Log.e(TAG, "BT cancelo threadconnect ");
            try {
                if (mmSocket != null )mmSocket.close();
                mmSocket = null;
                if (mmSocketException  && ClaseBluetooth.is_primeraConexion()){
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        //pepe aviso bt
                        return;
                    }
                    mAdapter.disable();
                    try {
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException e) {}
                    mAdapter.enable();
                }

                ClaseBluetooth.set_primeraConexion(false);

                setState(ClaseBluetoothPrintConstantes.STATE_DISCONNECTED);
            } catch (IOException var2) {
                Log.e(TAG, "BT close() of connect socket failed", var2);
            }

        }
    }

    private class ConnectedThread extends Thread {
        private int e1 = 0;


        public ConnectedThread() {
            Log.d(TAG, "create ConnectedThread");
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException var6) {
                Log.e(TAG, "temp sockets not created", var6);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];

            while(true) {
                try {
                    while(true) {
                        int leidos = 0;

                        if((mmInStream.available()) >= 3) {

                            do {


                            int pendientes = mmInStream.available();
                            Log.i("ChatService. Pendie:",String.valueOf(pendientes));
                            /*for (int e = e1; e < this.mmInStream.available(); ++e) {
                                buffer[e] = (byte) this.mmInStream.read();
                            }*/
                           leidos = mmInStream.read(buffer,e1,pendientes);

                            e1 += leidos;
                            Log.i("ChatService. Leidos:",String.valueOf(leidos)+". e1: "+String.valueOf(e1));
                            } while (mmInStream.available() != 0 );

                            if (e1 >= 3) {
                                e1 = e1 % 3;
                                Log.i("ChatService. Leidos:",String.valueOf(leidos)+". e1: "+String.valueOf(e1));
                                Log.i(TAG, "revBuffer[0]:" + buffer[0] + "  revBuffer[1]:" + buffer[1] + "  revBuffer[2]:" + buffer[2]);
                                //BTDriverRPP200.this.mHandler.obtainMessage(2, BloothPrinterActivity.revBytes, -1, buffer).sendToTarget();
                                Message msg = BTDriverSM5802.this.mHandler.obtainMessage(2);
                                Bundle bundle = new Bundle();
                                bundle.putByteArray("estado", buffer);
                                msg.setData(bundle);
                                BTDriverSM5802.this.mHandler.sendMessage(msg);

                            }


                        }
                        else{
                            try {
                              buffer[e1] = (byte)  mmInStream.read();
                              e1 += 1;
                                if (e1 >= 3) {
                                    e1 = e1 % 3;
                                    Log.i("ChatService. Leidos:",String.valueOf(leidos)+". e1: "+String.valueOf(e1));
                                    Log.i(TAG, "revBuffer[0]:" + buffer[0] + "  revBuffer[1]:" + buffer[1] + "  revBuffer[2]:" + buffer[2]);

                                    Message msg = BTDriverSM5802.this.mHandler.obtainMessage(2);
                                    Bundle bundle = new Bundle();
                                    bundle.putByteArray("estado", buffer);
                                    msg.setData(bundle);
                                    BTDriverSM5802.this.mHandler.sendMessage(msg);

                                }
                            }
                            catch(IOException e) {
                                Log.e(TAG, "disconnected", e);
                                BTDriverSM5802.this.connectionLost();
                                this.cancel();
                                return;
                            }

                        }

                    }
                } catch (IOException var3) {
                    Log.e(TAG, "disconnected", var3);
                    BTDriverSM5802.this.connectionLost();
                    this.cancel();
                    return;
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
                BTDriverSM5802.this.mHandler.obtainMessage(3, -1, -1, buffer).sendToTarget();
            } catch (IOException var3) {
                Log.e(TAG, "Exception during write", var3);

            }

        }

        public void readAll() {

            try {
                   while (mmInStream.available() != 0 )  {
                    mmInStream.read();
                   }
                    e1 = 0;
                 } catch (IOException var3) {
                Log.e(TAG, "Exception during write", var3);

            }

        }

        public void write(byte[] buffer, int dataLen) {
            try {
                for(int e = 0; e < dataLen; ++e) {
                    mmOutStream.write(buffer[e]);
                }

                BTDriverSM5802.this.mHandler.obtainMessage(3, -1, -1, buffer).sendToTarget();
            } catch (IOException var4) {
                Log.e(TAG, "Exception during write", var4);
            }

        }

        public void cancel() {
            Log.e(TAG, "BT cancelo ThreadConnected ");
            try {
                if (mmInStream != null )    mmInStream.close();
                if (mmOutStream != null )    mmOutStream.close();
                if (mmSocket!= null ) {
                    mmSocket.close();
                    int intentos = 3;
                    do {
                        try {
                            Thread.sleep(400);
                            mConnectThread = new BTDriverSM5802.ConnectThread();
                            Log.d(TAG, "BT arranco connect ");
                            try {

                                Thread.sleep(400);
                                if (mConnectThread != null) {
                                    mConnectThread.start();
                                    mConnectThread.join(2000);

                                    Log.d(TAG, "BT fin intento reconectar");
                                    if (mmSocket != null) {
                                        setState(ClaseBluetoothPrintConstantes.STATE_CONNECTED);
                                        intentos = 0;
                                    } else intentos--;
                                } else {
                                    Log.d(TAG, "BT mConnectThread es null. reintento");
                                    intentos--;
                                }


                            } catch (InterruptedException e) {
                                Log.d(TAG, "BT connectedThread cancel. exception e. reintento");
                                intentos--;
                            }
                        }
                        catch (InterruptedException e1){
                            Log.d(TAG, "BT connectedThread cancel. exception e1. reintento");
                            intentos --;
                        }

                    }while (intentos > 0);

                }
                mmInStream = null;
                mmOutStream = null;


            } catch (IOException var2) {
                Log.e(TAG, "close() of connect socket failed", var2);
            }


        }
    }
}
