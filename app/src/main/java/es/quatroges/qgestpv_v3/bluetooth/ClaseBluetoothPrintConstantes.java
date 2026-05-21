package es.quatroges.qgestpv_v3.bluetooth;


public  class ClaseBluetoothPrintConstantes {

    public static final int STATE_IDLE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connectSocket to a remote device
    public static final int STATE_DISCONNECTED = 4;  // now connectSocket to a remote device
    public static final int STATE_CONNECTING_ERROR = 5;  // now connectSocket to a remote device
    public static final int STATE_CONNECTED_SOCKET = 6;
    public static final int STATE_DISCONNECTING = 7;


    public static final int MODEL_SM5802 = 0;
    public static final int MODEL_RPP200 = 1;
    public static final int MODEL_DPP250BT = 2;
    public static final int MODEL_SCREEN = 3;
    public static final int MODEL_DPP250USB = 4;
    public static final int MODEL_ITOS_CM5 = 5;
    public static final int MODEL_IPDA045 = 6;
    public static final int MODEL_PAYTEF_A920 = 7;

    public static final int RECIBO_VENTA = 0;
    public static final int RECIBO_DEVOLUCION = 1;

    public static final int FUENTE_NORMAL =0;
    public static final int FUENTE_SMALL =1;
    public static final int FUENTE_BIG =2;

    public static final int ESTILO_NORMAL =0;
    public static final int ESTILO_BOLD =1;

    public static final int ALIGN_LEFT =0;
    public static final int ALIGN_CENTER =1;
    public static final int ALIGN_RIGTH =2;

    public static final int PAUSA_RECIBO = 2000;
    public static final int PAUSA_TIQUET = 4000;
    public static final int PAUSA_RECIBO_PAYTEF = 200;
    public static final int PAUSA_TIQUET_PAYTEF = 200;




}
