package es.quatroges.qgestpv_v3.bluetooth;

import static es.quatroges.qgestpv_v3.bluetooth.ClaseBluetoothPrintConstantes.ESTILO_BOLD;
import static es.quatroges.qgestpv_v3.bluetooth.ClaseBluetoothPrintConstantes.FUENTE_BIG;
import static es.quatroges.qgestpv_v3.bluetooth.ClaseBluetoothPrintConstantes.FUENTE_SMALL;
import static es.quatroges.qgestpv_v3.bluetooth.ClaseBluetoothPrintConstantes.STATE_DISCONNECTED;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.iposprinter.iposprinterservice.IPosPrinterCallback;
import com.iposprinter.iposprinterservice.IPosPrinterService;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.R;
import es.quatroges.qgestpv_v3.bluetooth.ipad045_utils.Datos;
import es.quatroges.qgestpv_v3.bluetooth.ipad045_utils.HandlerUtils;
import es.quatroges.qgestpv_v3.bluetooth.ipad045_utils.ThreadPoolManager;

/**
 * Created by carlos on 30/12/2015.
 */
public class BTDriverIPDA045 {

    private static final String TAG = "BTDriverIPDA045";
    private static Handler mHandler = null;

    private static int mState;
    public static final int STATE_STOP = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;

    private static BluetoothDevice mmDevice;

    private static  final int FONT_SIZE_SMALL = 24;
    private static final int FONT_SIZE_NORMAL = 24;
    private static final int FONT_SIZE_BIG = 32;

    private static final int ALIGN_LEFT = 0;
    private static final int ALIGN_CENTER = 1;
    private static final int ALIGN_RIGHT = 2;


    private static boolean finImpresion;

    private static Context _context;

    //region "Funciones específicas"

    private static ArrayList<Datos> listaDatos;

    public  void iniciaListaDatos() {
        if (listaDatos == null) {
            listaDatos = new ArrayList<>();
        }
        listaDatos.clear();
    }
    public static int mPrinterState;
    public static final int PRINTER_NORMAL = 0;
    public static final int PRINTER_PAPERLESS = 1;
    public static final int PRINTER_THP_HIGH_TEMPERATURE = 2;
    public static final int PRINTER_MOTOR_HIGH_TEMPERATURE = 3;
    public static final int PRINTER_IS_BUSY = 4;
    public static final int PRINTER_ERROR_UNKNOWN = 5;

    private final String  PRINTER_NORMAL_ACTION = "com.iposprinter.iposprinterservice.NORMAL_ACTION";
    private final String  PRINTER_PAPERLESS_ACTION = "com.iposprinter.iposprinterservice.PAPERLESS_ACTION";
    private final String  PRINTER_PAPEREXISTS_ACTION = "com.iposprinter.iposprinterservice.PAPEREXISTS_ACTION";
    private final String  PRINTER_THP_HIGHTEMP_ACTION = "com.iposprinter.iposprinterservice.THP_HIGHTEMP_ACTION";
    private final String  PRINTER_THP_NORMALTEMP_ACTION = "com.iposprinter.iposprinterservice.THP_NORMALTEMP_ACTION";
    private final String  PRINTER_MOTOR_HIGHTEMP_ACTION = "com.iposprinter.iposprinterservice.MOTOR_HIGHTEMP_ACTION";
    private final String  PRINTER_BUSY_ACTION = "com.iposprinter.iposprinterservice.BUSY_ACTION";
    private final String  PRINTER_CURRENT_TASK_PRINT_COMPLETE_ACTION = "com.iposprinter.iposprinterservice.CURRENT_TASK_PRINT_COMPLETE_ACTION";

    private final int MSG_TEST                               = 1;
    private final int MSG_IS_NORMAL                          = 2;
    private final int MSG_IS_BUSY                            = 3;
    private final int MSG_PAPER_LESS                         = 4;
    private final int MSG_PAPER_EXISTS                       = 5;
    private final int MSG_THP_HIGH_TEMP                      = 6;
    private final int MSG_THP_TEMP_NORMAL                    = 7;
    private final int MSG_MOTOR_HIGH_TEMP                    = 8;
    private final int MSG_MOTOR_HIGH_TEMP_INIT_PRINTER       = 9;
    private final int MSG_CURRENT_TASK_PRINT_COMPLETE     = 10;

    private final int  MULTI_THREAD_LOOP_PRINT  = 1;
    private final int  INPUT_CONTENT_LOOP_PRINT = 2;
    private final int  DEMO_LOOP_PRINT          = 3;
    private final int  PRINT_DRIVER_ERROR_TEST  = 4;
    private final int  DEFAULT_LOOP_PRINT       = 0;

    private int printerStatus = 0;

    private       int  loopPrintFlag            = DEFAULT_LOOP_PRINT;
    private       byte loopContent              = 0x00;
    private       int  printDriverTestCount     = 0;

    private static IPosPrinterService mIPosPrinterService;
    private static IPosPrinterCallback callback = null;
    private static HandlerUtils.MyHandler handler;
    private static boolean esperandoResultado;

    private   void startService(Context context) {

        Intent intent=new Intent();
        intent.setPackage("com.iposprinter.iposprinterservice");
        intent.setAction("com.iposprinter.iposprinterservice.IPosPrintService");
        //startService(intent);
        Log.i(TAG, "bind service");
        try {
            context.bindService(intent, connectService, Context.BIND_AUTO_CREATE);
        }
        catch (Exception e){
            Log.i(TAG, "Excepción bind service");
            Log.i(TAG, "unbind debug  service");

            context.bindService(intent, null, Context.BIND_DEBUG_UNBIND);

            Log.i(TAG, "bind service");

            context.bindService(intent, connectService, Context.BIND_AUTO_CREATE);

        }

    }
    private void setupReceiver() {


        IntentFilter printerStatusFilter = new IntentFilter();
        printerStatusFilter.addAction(PRINTER_NORMAL_ACTION);
        printerStatusFilter.addAction(PRINTER_PAPERLESS_ACTION);
        printerStatusFilter.addAction(PRINTER_PAPEREXISTS_ACTION);
        printerStatusFilter.addAction(PRINTER_THP_HIGHTEMP_ACTION);
        printerStatusFilter.addAction(PRINTER_THP_NORMALTEMP_ACTION);
        printerStatusFilter.addAction(PRINTER_MOTOR_HIGHTEMP_ACTION);
        printerStatusFilter.addAction(PRINTER_BUSY_ACTION);


        Log.i(TAG, "registro listener");
        try {
            _context.registerReceiver(IPosPrinterStatusListener,printerStatusFilter);
        }
        catch(Exception e){
            Log.i(TAG, "Excepcion registro listener");
            Log.i(TAG, "registro null");
            try {
                _context.registerReceiver(null, null);
                Log.i(TAG, "registro listener");
                try {
                    _context.registerReceiver(IPosPrinterStatusListener,printerStatusFilter);
                }
                catch(Exception e1){
                    Log.i(TAG, "Excepcion registro listener");

                }
            }
            catch(Exception e2){
                Log.i(TAG, "Excepcion registro null");

            }

        }

    }

    private void stopIPDA045() {
        _context.unregisterReceiver(IPosPrinterStatusListener);
        _context.unbindService(connectService);
        //handler.removeCallbacksAndMessages(null);
    }

    private  static ServiceConnection connectService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIPosPrinterService = IPosPrinterService.Stub.asInterface(service);
            Log.i(TAG, "Servicio de impresión conectado");

            Log.d("BluetoothChatService", "setState() " + mState + " -> " + STATE_CONNECTED);
            mState = STATE_CONNECTED;
            mHandler.obtainMessage(1, STATE_CONNECTED, -1).sendToTarget();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIPosPrinterService = null;
            Log.i(TAG, "Servicio de impresión desconectado");
            Log.d("BluetoothChatService", "setState() " + mState + " -> " + STATE_CONNECTED);
            mState = STATE_DISCONNECTED;
            mHandler.obtainMessage(1, STATE_DISCONNECTED, -1).sendToTarget();

        }

        @Override
        public void onBindingDied(ComponentName name) {
            mIPosPrinterService = null;
            Log.i(TAG, "onBindingDied");

        }

        @Override
        public void onNullBinding(ComponentName name) {
            Log.i(TAG, "onNullBinding");

        }
    };

    private HandlerUtils.IHandlerIntent iHandlerIntent = new HandlerUtils.IHandlerIntent()
    {
        @Override
        public void handlerIntent(Message msg)
        {
            switch (msg.what)
            {
                case MSG_TEST:
                    break;
                case MSG_IS_NORMAL:
                    if(getPrinterStatus() == PRINTER_NORMAL)
                    {
                    }
                    break;
                case MSG_IS_BUSY:
                    Toast.makeText(_context, R.string.printer_is_working, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_PAPER_LESS:
                    loopPrintFlag = DEFAULT_LOOP_PRINT;
                    Toast.makeText(_context, R.string.out_of_paper, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_PAPER_EXISTS:
                    Toast.makeText(_context, R.string.exists_paper, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_THP_HIGH_TEMP:
                    Toast.makeText(_context, R.string.printer_high_temp_alarm, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_MOTOR_HIGH_TEMP:
                    loopPrintFlag = DEFAULT_LOOP_PRINT;
                    Toast.makeText(_context, R.string.motor_high_temp_alarm, Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessageDelayed(MSG_MOTOR_HIGH_TEMP_INIT_PRINTER, 180000);  //马达高温报警，等待3分钟后复位打印机
                    break;
                case MSG_MOTOR_HIGH_TEMP_INIT_PRINTER:
                    //printerInit();
                    InitPrinter();
                    break;
                case MSG_CURRENT_TASK_PRINT_COMPLETE:
                    Toast.makeText(_context, R.string.printer_current_task_print_complete, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private BroadcastReceiver IPosPrinterStatusListener = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();
            if(action == null)
            {
                Log.d(TAG,"IPosPrinterStatusListener onReceive action = null");
                return;
            }
            Log.d(TAG,"IPosPrinterStatusListener action = "+action);

            if(action.equals(PRINTER_NORMAL_ACTION))
            {
                //handler.sendEmptyMessageDelayed(MSG_IS_NORMAL,0);
                Log.i(TAG,"BROADCASTRECEIVER: MSG_IS_NORMAL");
                mPrinterState =PRINTER_NORMAL;
                finImpresion = true;
                Log.i(TAG,"Final de impresión recibido. IS_NORMAL");
            }
            else if (action.equals(PRINTER_PAPERLESS_ACTION))
            {
                //handler.sendEmptyMessageDelayed(MSG_PAPER_LESS,0);
                Log.i(TAG,"BROADCASTRECEIVER: MSG_PAPER_LESS");
                mPrinterState = PRINTER_PAPERLESS;

            }
            else if (action.equals(PRINTER_BUSY_ACTION))
            {
                //handler.sendEmptyMessageDelayed(MSG_IS_BUSY,0);
                Log.i(TAG,"BROADCASTRECEIVER: MSG_IS_BUSY");
                mPrinterState = PRINTER_IS_BUSY;
            }
            else if (action.equals(PRINTER_PAPEREXISTS_ACTION))
            {
                //handler.sendEmptyMessageDelayed(MSG_PAPER_EXISTS,0);
                Log.i(TAG,"BROADCASTRECEIVER: MSG_PAPER_EXISTS");
            }
            else if (action.equals(PRINTER_THP_HIGHTEMP_ACTION))
            {
                //handler.sendEmptyMessageDelayed(MSG_THP_HIGH_TEMP,0);
                Log.i(TAG,"BROADCASTRECEIVER: MSG_THP_HIGH_TEMP");
                mPrinterState = PRINTER_THP_HIGH_TEMPERATURE;
            }
            else if (action.equals(PRINTER_THP_NORMALTEMP_ACTION))
            {
                //handler.sendEmptyMessageDelayed(MSG_THP_TEMP_NORMAL,0);
                Log.i(TAG,"BROADCASTRECEIVER: MSG_THP_TEMP_NORMAL");

            }
            else if (action.equals(PRINTER_MOTOR_HIGHTEMP_ACTION))  //此时当前任务会继续打印，完成当前任务后，请等待2分钟以上时间，继续下一个打印任务
            {
                //handler.sendEmptyMessageDelayed(MSG_MOTOR_HIGH_TEMP,0);
                Log.i(TAG,"BROADCASTRECEIVER: MSG_MOTOR_HIGH_TEMP");
                mPrinterState = PRINTER_MOTOR_HIGH_TEMPERATURE;
            }
            else if(action.equals(PRINTER_CURRENT_TASK_PRINT_COMPLETE_ACTION))
            {
                //handler.sendEmptyMessageDelayed(MSG_CURRENT_TASK_PRINT_COMPLETE,0);
                Log.i(TAG,"BROADCASTRECEIVER: MSG_CURRENT_TASK_PRINT_COMPLETE");

                finImpresion = true;
            }
            else
            {
                //handler.sendEmptyMessageDelayed(MSG_TEST,0);
                Log.i(TAG,"BROADCASTRECEIVER: MSG_TEST");
            }
        }
    };

    public int getPrinterStatus(){

        Log.i(TAG,"***** printerStatus"+printerStatus);
        try{
            printerStatus = mIPosPrinterService.getPrinterStatus();
        }catch (RemoteException e){
            e.printStackTrace();
        }
        Log.i(TAG,"#### printerStatus"+printerStatus);
        if (printerStatus == 0 ) mPrinterState = 0;
        return  mPrinterState;
    }


    //end region



    public static boolean isFinImpresion() {
        return finImpresion;
    }

    @SuppressLint({"NewApi"})
    public BTDriverIPDA045(Context context, Handler handler) {
        mState = STATE_STOP;
        BTDriverIPDA045.mHandler = handler;
        _context = context;
        //mPrinter = new Printer( context);

        handler = new HandlerUtils.MyHandler(Looper.getMainLooper(),iHandlerIntent);
        callback = new IPosPrinterCallback.Stub() {

            @Override
            public void onRunResult(final boolean isSuccess) throws RemoteException {
                Log.i(TAG, "result:" + isSuccess + "\n");
                esperandoResultado = false;
            }

            @Override
            public void onReturnString(final String value) throws RemoteException {
                Log.i(TAG, "result:" + value + "\n");
                esperandoResultado = false;
            }
        };
        Log.i(TAG,"Start service");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startService(_context);
            }
        }, 500);
        Log.i(TAG,"setup Receiver");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                setupReceiver();
            }
        }, 2000);
        listaDatos = new ArrayList<>();

    }

    public synchronized void setState(int state) {
        Log.d("BluetoothChatService", "setState() " + mState + " -> " + state);
        mState = state;
        this.mHandler.obtainMessage(1, state, -1).sendToTarget();
    }

    public synchronized int getState() {
        return mState;
    }


    public synchronized void connect(BluetoothDevice device) {
        mmDevice = device;
        Message msg = this.mHandler.obtainMessage(4);
        Bundle bundle = new Bundle();
        bundle.putString("device_name", device.getName());
        msg.setData(bundle);
        this.mHandler.sendMessage(msg);
        //this.setState(STATE_CONNECTED);
        this.setState(STATE_CONNECTING);
    }

    //public synchronized void connectSocket(BluetoothSocket socket, BluetoothDevice device) {
    public synchronized void connectSocket() {

        Log.d(TAG, "connectSocket");

            Message msg = this.mHandler.obtainMessage(4);
            Bundle bundle = new Bundle();
            bundle.putString("device_name", mmDevice.getName());
            msg.setData(bundle);
            this.mHandler.sendMessage(msg);
            this.setState(ClaseBluetoothPrintConstantes.STATE_CONNECTED_SOCKET);
    }

    public synchronized void disconnectSocket() {
        Message msg = this.mHandler.obtainMessage(4);
        Bundle bundle = new Bundle();
        bundle.putString("device_name", mmDevice.getName());
        msg.setData(bundle);
        this.mHandler.sendMessage(msg);
        this.setState(ClaseBluetoothPrintConstantes.STATE_CONNECTED);

    }


    public synchronized void stop() {
        Log.d("BluetoothChatService", "stop");
        this.setState(STATE_STOP);
        stopIPDA045();
    }

    public static void BT_Write(String data, int fuente, int estilo, int align) {

        int _font = FONT_SIZE_NORMAL;
        if (fuente == FUENTE_SMALL){
            _font = FONT_SIZE_SMALL;
        }
        else if (fuente == FUENTE_BIG){
            _font = FONT_SIZE_BIG;
        }
        boolean _bold = false;
        if (estilo == ESTILO_BOLD) {
            _bold = true;
        }

        int _align = ALIGN_LEFT;
        if (align == 1) _align = ALIGN_CENTER;
        if (align == 2) _align = ALIGN_RIGHT;

        final int font1 = _font;
        final int align1 = _align;

        if(mState == ClaseBluetoothPrintConstantes.STATE_CONNECTED_SOCKET) {
            if (listaDatos == null){
                listaDatos = new ArrayList<>();
            }
            listaDatos.add(new Datos(data,font1,align1));
        }
    }



    public static boolean InitPrinter() {

        ThreadPoolManager.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {
                try{
                    mIPosPrinterService.printerInit(callback);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        });


        return true;
    }

    public static void WakeUpPritner() {
    }

    public static void Begin() {
        WakeUpPritner();
        InitPrinter();

    }

    public static void CR() {

        //mPrinter.appendStr("\n",FONT_SIZE_NORMAL,Align.LEFT,false);
        if (listaDatos == null){
            listaDatos = new ArrayList<>();
        }
        listaDatos.add(new Datos("\n",FONT_SIZE_NORMAL,ALIGN_LEFT));
    }

    public static void startPrint() {
        BTDriverIPDA045.finImpresion = false;
        ThreadPoolManager.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {
                int a = 0;
                try{
                    for (Datos dato: listaDatos){
                        a+=1;
                        if (dato.getTipo() == Datos.enTipo.TEXTO)
                            mIPosPrinterService.PrintSpecFormatText(dato.getTexto(),"ST",dato.getFont(),dato.getAling(),callback);
                        else if (dato.getTipo() == Datos.enTipo.QR) {
                            mIPosPrinterService.setPrinterPrintAlignment(dato.getAling(),callback);
                            mIPosPrinterService.printQRCode(dato.getQR(),dato.getModule(),dato.getErrorCorrectionLevel(),callback);
                        }
                        else if (dato.getTipo() == Datos.enTipo.BITMAP) {
                            mIPosPrinterService.setPrinterPrintAlignment(dato.getAling(),callback);
                            mIPosPrinterService.printBitmap(dato.getAling(),dato.getBitmapSize(),dato.getBitmap(),callback);
                        }
                    }

                    mIPosPrinterService.printerPerformPrint(160,callback);

                }catch (RemoteException e){
                    e.printStackTrace();
                }
                listaDatos.clear();
                Log.i(TAG,"Lineas impresas: "+ String.valueOf( a));
            }
        });
    }

    public void printBarcode(final String codigo) {
        if (listaDatos == null){
            listaDatos = new ArrayList<>();
        }
        Datos dato = new Datos();
        dato.addQR(codigo,6,2,1);
        listaDatos.add(dato);

    }

    public void printBitmap(final Bitmap bitmap){
        if (listaDatos == null){
            listaDatos = new ArrayList<>();
        }
        Datos dato = new Datos();
        dato.addBitmap(bitmap,4,0);
        listaDatos.add(dato);

    }


}
