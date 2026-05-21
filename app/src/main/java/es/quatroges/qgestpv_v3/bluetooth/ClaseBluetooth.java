package es.quatroges.qgestpv_v3.bluetooth;

import static es.quatroges.qgestpv_v3.bluetooth.ClaseBluetoothPrintConstantes.ALIGN_CENTER;
import static es.quatroges.qgestpv_v3.bluetooth.ClaseBluetoothPrintConstantes.ALIGN_LEFT;
import static es.quatroges.qgestpv_v3.bluetooth.ClaseBluetoothPrintConstantes.ALIGN_RIGTH;
import static es.quatroges.qgestpv_v3.bluetooth.ClaseBluetoothPrintConstantes.ESTILO_BOLD;
import static es.quatroges.qgestpv_v3.bluetooth.ClaseBluetoothPrintConstantes.ESTILO_NORMAL;
import static es.quatroges.qgestpv_v3.bluetooth.ClaseBluetoothPrintConstantes.FUENTE_BIG;
import static es.quatroges.qgestpv_v3.bluetooth.ClaseBluetoothPrintConstantes.FUENTE_NORMAL;
import static es.quatroges.qgestpv_v3.bluetooth.ClaseBluetoothPrintConstantes.FUENTE_SMALL;
import static es.quatroges.qgestpv_v3.bluetooth.ClaseBluetoothPrintConstantes.MODEL_IPDA045;
import static es.quatroges.qgestpv_v3.bluetooth.ClaseBluetoothPrintConstantes.MODEL_SCREEN;
import static es.quatroges.qgestpv_v3.bluetooth.ClaseBluetoothPrintConstantes.STATE_CONNECTED_SOCKET;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import es.quatroges.qgestpv_v3.R;
import es.quatroges.qgestpv_v3.configuracion.ClaseCondicionesVenta;
import es.quatroges.qgestpv_v3.datos.CabeceraTiquet;
import es.quatroges.qgestpv_v3.datos.GrabaLineasVenta;
import es.quatroges.qgestpv_v3.datos.listas.facturas.ClaseFacturaCabecera;
import es.quatroges.qgestpv_v3.datos.listas.lineaVentas.ClaseLineaVentas;
import es.quatroges.qgestpv_v3.datos.listas.mesas.ClaseSubMesas;
import es.quatroges.qgestpv_v3.datos.listas.users.ClaseUsers;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;
import es.quatroges.qgestpv_v3.utils.EncodeUtils;


public class ClaseBluetooth {

	public static final String CMD_NEGRITA_E = new String(new byte[]{(byte) 27, (byte) 69, (byte) 1});
	public static final String CMD_NEGRITA_D = new String(new byte[]{(byte) 27, (byte) 69, (byte) 0});
	public static final String CMD_UNDERLINE_E = new String(new byte[]{(byte) 27, (byte) 45, (byte) 1});
	public static final String CMD_UNDERLINE_D = new String(new byte[]{(byte) 27, (byte) 45, (byte) 0});
	public static final String CMD_SIZEDOBLEWIDTH = new String(new byte[]{(byte) 29, (byte) 33, (byte) 16});
	public static final String CMD_SIZEDOBLEHEIGHT = new String(new byte[]{(byte) 29, (byte) 33, (byte) 1});
	public static final String CMD_SIZEDOBLEW_H = new String(new byte[]{(byte) 29, (byte) 33, (byte) 17});
	public static final String CMD_SIZENORMAL = new String(new byte[]{(byte) 29, (byte) 33, (byte) 0});
	public static final String CMD_SMALLFONT_E = new String(new byte[]{(byte) 27, (byte) 33, (byte) 1});
	public static final String CMD_SMALLFONT_D = new String(new byte[]{(byte) 27, (byte) 33, (byte) 0});
	public static final String CMD_BLACKREVERSE_E = new String(new byte[]{(byte) 29, (byte) 66, (byte) 1});
	public static final String CMD_BLACKREVERSE_D = new String(new byte[]{(byte) 29, (byte) 66, (byte) 0});

	public static final int REQUEST_SCAN_DEVICE = 1;
	public static final int REQUEST_ENABLE_BT = 2;

	private static final String TAG = "ClaseBlueTooth";

	// clases para modelos de impresora soportados
	private static BTDriverSM5802 _BTdriverSM5802;
	private static BTDriverSCREEN _BTdriverSCREEN;
	private static BTDriverIPDA045 _BTdriverIPDA045;


	private static int _modelo = ClaseBluetoothPrintConstantes.MODEL_SM5802;

	private static Context context;
	private static Context applicationContext;

	public static Context getApplicationContext() {
		return applicationContext;
	}

	private static boolean _primeraConexion = false;
	private static BluetoothAdapter _BluetoothAdapter = null;
	private static String _remoteDevice;
	private static String _remoteMAC;
	private static boolean _imprimiendo = false;
	private static boolean _forzarTest = false;
	private static boolean _conectando = false;
	private static boolean _noDesconectarBT = false;
	private static boolean _btBroadcasReceiverRegistrado = false;

	private static boolean _estadoBateriaLow = false;
	private static float _estadoNivelBateria = (float) 13.0;
	private static boolean _estadoOverheat = false;
	private static boolean _estadoPapel = false;

	private static long ultimo_milis = -1;

	private static Handler handlerNotificaBT = null;
	private static Handler handlerNotificaOperacionPaytef = null;
	private static Timer timerBT;
	private static TimerBT_Task timerBT_Task;

	private static int _ultimoEstadoBT;

	// variables para volver a buscar el dispositivo

	//revisar
	private static ArrayList<String> mPairedDevicesArrayAdapter;
	private static ArrayList<String> mNewDevicesArrayAdapter;
	//fin  variables para volver a buscar el dispositivo

	// GETTERS  - SETTERS


	public static boolean is_primeraConexion() {
		return _primeraConexion;
	}

	public static void set_primeraConexion(boolean _primeraConexion) {
		ClaseBluetooth._primeraConexion = _primeraConexion;
	}

	public static int get_modelo() {
		return _modelo;
	}

	public static void set_modelo(String strModelo) {
		new ClaseBluetooth().configuraModelo(strModelo);
	}

	public static Boolean get_imprimiendo() {
		return _imprimiendo;
	}

	public static void set_imprimiendo(Boolean _imprimiendo) {
		ClaseBluetooth._imprimiendo = _imprimiendo;
		/*
		if (!_imprimiendo ){
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
				 if(! ClaseBluetooth._imprimiendo)	desconectarSocketBT();
				}
			}, 20000);
		}
		*/
	}

	public static void set_forzarTest(boolean _forzarTest) {
		ClaseBluetooth._forzarTest = _forzarTest;
	}

	public static Boolean get_conectando() {
		return _conectando;
	}

	public static void set_conectando(Boolean _conectando) {
		ClaseBluetooth._conectando = _conectando;
	}

	public static boolean getEstadoBateriaLow() {
		return _estadoBateriaLow;
	}

	public static void setEstadoBateriaLow(boolean estadoBateriaLow) {
		ClaseBluetooth._estadoBateriaLow = estadoBateriaLow;
	}

	public static float getEstadoNivelBateria() {
		return _estadoNivelBateria;
	}

	public static void setEstadoNivelBateria(float estadoNivelBateria) {
		ClaseBluetooth._estadoNivelBateria = estadoNivelBateria;
	}

	public static boolean isEstadoOverheat() {
		return _estadoOverheat;
	}

	public static void setEstadoOverheat(boolean estadoOverheat) {
		ClaseBluetooth._estadoOverheat = estadoOverheat;
	}

	public static boolean isEstadoPapel() {
		return _estadoPapel;
	}

	public static void setEstadoPapel(boolean estadoPapel) {
		ClaseBluetooth._estadoPapel = estadoPapel;
	}

	public static void setHandlerNotificaBT(Handler handler) {
		if (handlerNotificaBT != null) handlerNotificaBT = null;
		handlerNotificaBT = handler;
	}

	public static void setHandlerNotificaOperacionPaytef(Handler handlerNotificaOperacionPaytef) {
		ClaseBluetooth.handlerNotificaOperacionPaytef = handlerNotificaOperacionPaytef;
	}

	public static void setRemoteDevice(String remoteDevice) {
		_remoteDevice = remoteDevice;
	}

	public static int get_ultimoEstadoBT() {
		return _ultimoEstadoBT;
	}

	public static void set_ultimoEstadoBT(int _ultimoEstadoBT) {
		ClaseBluetooth._ultimoEstadoBT = _ultimoEstadoBT;
	}

	public static BTDriverSM5802 getBTdriverSM5802() {
		return _BTdriverSM5802;
	}

	public static BTDriverSCREEN getBTdriverSCREEN() {
		return _BTdriverSCREEN;
	}

	public static BTDriverIPDA045 get_BTdriverIPDA045() {
		return _BTdriverIPDA045;
	}


	// FIN GETTERS  - SETTERS

	public ClaseBluetooth() {

	}

	public ClaseBluetooth(Context context, Context applicationContext, Handler handler, String modelo) {

		if (ClaseBluetooth.context != null) ClaseBluetooth.context = null;
		ClaseBluetooth.context = context;
		if (ClaseBluetooth.applicationContext != null) ClaseBluetooth.applicationContext = null;
		ClaseBluetooth.applicationContext = applicationContext;

		if (handlerNotificaBT != null) handlerNotificaBT = null;
		handlerNotificaBT = handler;
		android.util.Log.e(TAG, "super...");

		_imprimiendo = false;
		_forzarTest = false;

		_primeraConexion = true;

		configuraModelo(modelo);
		setEstadoBateriaLow(false);
		setEstadoNivelBateria((float) 13.0);
		setEstadoPapel(true);
		setEstadoOverheat(false);

		if (timerBT == null) {
			timerBT = new Timer();
			timerBT_Task = new TimerBT_Task();

			timerBT.schedule(timerBT_Task, 1000, 10000);

		}

		registraBTReceiver(applicationContext);

	}

	public static void restartTimer() {
		detenerTimer();
		enableTimer(10000, true);

	}

	public static void configuraModelo(String modelo) {
		if (modelo == null)
			modelo = context.getResources().getString(R.string.strConfigBTModel_SM5802BL);

		if (modelo.equalsIgnoreCase(context.getResources().getString(R.string.strConfigBTModel_SM5802BL))) {
			_modelo = ClaseBluetoothPrintConstantes.MODEL_SM5802;
			registraBTReceiver(applicationContext);
		} else if (modelo.equalsIgnoreCase(context.getResources().getString(R.string.strConfigBTModel_IPDA045))) {
			_modelo = ClaseBluetoothPrintConstantes.MODEL_IPDA045;
		} else {
			_modelo = ClaseBluetoothPrintConstantes.MODEL_SCREEN;
		}

	}

	public static void pararBTPrintService() {

		switch (_modelo) {
			case ClaseBluetoothPrintConstantes.MODEL_SM5802:
				if (_BTdriverSM5802 != null) _BTdriverSM5802.stop();
				break;
			case ClaseBluetoothPrintConstantes.MODEL_SCREEN:
				if (_BTdriverSCREEN != null) _BTdriverSCREEN.stop();
				break;
			case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
				if (_BTdriverIPDA045 != null) _BTdriverIPDA045.stop();
				break;

		}
	}

	public static void finalizaBTPrintService() {
		switch (_modelo) {
			case ClaseBluetoothPrintConstantes.MODEL_SM5802:
				_BTdriverSM5802 = null;
				break;
			case ClaseBluetoothPrintConstantes.MODEL_SCREEN:
				_BTdriverSCREEN = null;
				break;
			case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
				_BTdriverIPDA045 = null;
				break;
		}
	}

	public static void enableTimer(long milis, boolean force) {
		detenerTimer();

		if (timerBT == null) {
			timerBT = new Timer();
			timerBT_Task = new TimerBT_Task();
		}

		if (timerBT_Task != null) {
			if (force) {
				timerBT.schedule(timerBT_Task, 1000, milis);
			} else {
				if (milis != ultimo_milis) timerBT.schedule(timerBT_Task, 1000, milis);
				else timerBT.schedule(timerBT_Task, milis, milis);
			}
		}

		ultimo_milis = milis;
	}

	public static void detenerTimer() {
		if (timerBT != null) {
			timerBT.cancel();
			timerBT_Task.cancel();
			timerBT_Task = null;
			timerBT = null;
		}
	}

	public static void detener() {
		switch (_modelo) {
			case ClaseBluetoothPrintConstantes.MODEL_SM5802:
				if (_BTdriverSM5802 != null) {
					pararBTPrintService();
					finalizaBTPrintService();
				}
				break;
			case ClaseBluetoothPrintConstantes.MODEL_SCREEN:
				if (_BTdriverSCREEN != null) {
					// revisar
					pararBTPrintService();
					finalizaBTPrintService();
				}
				break;
			case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
				if (_BTdriverIPDA045 != null) {
					// revisar
					pararBTPrintService();
					finalizaBTPrintService();
				}
				break;
		}
		new ClaseBluetooth().detenerTimer();
		desRegistraBTReceiver(applicationContext);
	}

	public static BluetoothAdapter getBlueToothAdapter() {
		return _BluetoothAdapter;
	}


	public static BluetoothAdapter iniciaBlueToothAdapter(boolean iniciaDispositivo) {
		if (_BluetoothAdapter == null) {
			_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		}
		if (iniciaDispositivo) _remoteDevice = "";

		switch (_modelo) {
			case ClaseBluetoothPrintConstantes.MODEL_SM5802:
				if (_BTdriverSM5802 != null) {
					_BTdriverSM5802.stop();
					_BTdriverSM5802 = null;
				}
				break;
			case ClaseBluetoothPrintConstantes.MODEL_SCREEN:
				if (_BTdriverSCREEN != null) {
					_BTdriverSCREEN.stop();
					_BTdriverSCREEN = null;
				}
				break;

			case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
				if (_BTdriverIPDA045 != null) {
					_BTdriverIPDA045.stop();
					_BTdriverIPDA045 = null;
				}
				break;
		}
		return _BluetoothAdapter;
	}

	public static void setupComm() {
		Log.d(TAG, "setupComm()");
		switch (_modelo) {
			case ClaseBluetoothPrintConstantes.MODEL_SM5802:
				if (_BTdriverSM5802 == null)
					_BTdriverSM5802 = new BTDriverSM5802(context, mHandler);
				if (_BTdriverSM5802 == null)
					Log.e(TAG, "_BTPrintService init() failed");
				else
					connectToDevice();
				break;
			case ClaseBluetoothPrintConstantes.MODEL_SCREEN:
				if (_BTdriverSCREEN == null)
					_BTdriverSCREEN = new BTDriverSCREEN(context, mHandler);
				if (_BTdriverSCREEN == null)
					Log.e(TAG, "_BTPrintService init() failed");
				else
					connectToDevice();
				break;

			case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
				if (_BTdriverIPDA045 == null)
					_BTdriverIPDA045 = new BTDriverIPDA045(context, mHandler);
				if (_BTdriverIPDA045 == null)
					Log.e(TAG, "_BTPrintService init() failed");
				else
					connectToDevice();
				break;
		}

	}

	static void connectToDevice() {
		connectToDevice(true);
	}

	static void connectToDevice(boolean desconectar) {

		String remote = _remoteDevice;
		if (remote.length() == 0) {
			remote = "00:00:00:00:00";
		};

		switch (_modelo) {
			case ClaseBluetoothPrintConstantes.MODEL_SM5802:
				if (_BTdriverSM5802.getState() == ClaseBluetoothPrintConstantes.STATE_CONNECTED) {
					_BTdriverSM5802.stop();
					setConnectState(ClaseBluetoothPrintConstantes.STATE_LISTEN);
				}
				break;
			case ClaseBluetoothPrintConstantes.MODEL_SCREEN:
				if (_BTdriverSCREEN.getState() == BTDriverSCREEN.STATE_CONNECTED) {
					_BTdriverSCREEN.stop();
					setConnectState(ClaseBluetoothPrintConstantes.STATE_LISTEN);
				}
				break;
			case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
				if (_BTdriverIPDA045.getState() == BTDriverIPDA045.STATE_CONNECTED) {
					_BTdriverIPDA045.stop();
					setConnectState(ClaseBluetoothPrintConstantes.STATE_LISTEN);
				}
				break;
		}


		String sMacAddr = remote;
		if (sMacAddr.contains(":") == false && sMacAddr.length() == 12) {
			char[] cAddr = new char[17];

			for (int i = 0, j = 0; i < 12; i += 2) {
				sMacAddr.getChars(i, i + 2, cAddr, j);
				j += 2;
				if (j < 17) {
					cAddr[j++] = ':';
				}
			}
			sMacAddr = new String(cAddr);
		}

		_remoteMAC = sMacAddr;

		BluetoothDevice device;
		try {
			device = _BluetoothAdapter.getRemoteDevice(sMacAddr);
		} catch (Exception e) {
			device = null;
		}

		if (device != null || _modelo == MODEL_SCREEN || _modelo == MODEL_IPDA045 ) {  // entro si no es null o si es un modelo que no lleva mac
			addLog("connecting to " + sMacAddr);
			//boolean resultado = true;
			switch (_modelo) {
				case ClaseBluetoothPrintConstantes.MODEL_SM5802:
					_BTdriverSM5802.connect(device);
					break;
				case ClaseBluetoothPrintConstantes.MODEL_SCREEN:
					_BTdriverSCREEN.connect(device);
					break;
				case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
					_BTdriverIPDA045.connect(device);
					break;
			}

		} else {
			addLog("unknown remote device!");
		}

	}

	static void setConnectState(Integer iState) {

		switch (iState) {
			case ClaseBluetoothPrintConstantes.STATE_CONNECTED:
				addLog("connected...");
				set_conectando(false);
				break;
			case ClaseBluetoothPrintConstantes.STATE_DISCONNECTED:
				addLog("disconnected...");
				set_conectando(false);
				break;
			case ClaseBluetoothPrintConstantes.STATE_CONNECTING:
				addLog("connecting...");
				break;
			case ClaseBluetoothPrintConstantes.STATE_LISTEN:
				addLog("listening...");
				set_conectando(false);
				break;
			case ClaseBluetoothPrintConstantes.STATE_IDLE:
				addLog("state none");
				set_conectando(false);
				break;
			case ClaseBluetoothPrintConstantes.STATE_CONNECTING_ERROR:
				addLog("connecting_error...");
				set_conectando(false);
				break;
			case ClaseBluetoothPrintConstantes.STATE_CONNECTED_SOCKET:
				addLog("connected_socket...");
				set_conectando(false);
				break;
			case ClaseBluetoothPrintConstantes.STATE_DISCONNECTING:
				addLog("disconnecting...");
				set_conectando(false);
				break;

			default:
				addLog("unknown state var " + iState.toString());
		}

		if (handlerNotificaBT != null) {
			Message msg = handlerNotificaBT.obtainMessage(ClaseBluetoothMsgTypes.MESSAGE_STATE_CHANGE);
			Bundle bundle = new Bundle();
			bundle.putInt(ClaseBluetoothMsgTypes.STATE, iState);
			msg.setData(bundle);
			handlerNotificaBT.sendMessage(msg);
		}
	}

	private static final Handler mHandler = new Handler() {
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
				case ClaseBluetoothMsgTypes.MESSAGE_STATE_CHANGE:
					Bundle bundle = msg.getData();
					int status = msg.arg1;

					Log.i(TAG, "handleMessage: MESSAGE_STATE_CHANGE: " + msg.arg1);  //arg1 was not used! by BTPrintFile

					switch (status) {
						case ClaseBluetoothPrintConstantes.STATE_CONNECTED:
							setConnectState(ClaseBluetoothPrintConstantes.STATE_CONNECTED);
							_noDesconectarBT = false;
							break;
						case STATE_CONNECTED_SOCKET:
							setConnectState(STATE_CONNECTED_SOCKET);
							switch (_modelo) {
								case ClaseBluetoothPrintConstantes.MODEL_SM5802:
									// este modelo no tiene lectura de status
									break;
								case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
									break;
							}
							break;
						case ClaseBluetoothPrintConstantes.STATE_CONNECTING:
							setConnectState(ClaseBluetoothPrintConstantes.STATE_CONNECTING);
							break;
						case ClaseBluetoothPrintConstantes.STATE_DISCONNECTING:
							setConnectState(ClaseBluetoothPrintConstantes.STATE_DISCONNECTING);
							break;

						case ClaseBluetoothPrintConstantes.STATE_LISTEN:
							addLog("handleMessage: STATE_LISTEN");
							setConnectState(ClaseBluetoothPrintConstantes.STATE_LISTEN);
							break;
						case ClaseBluetoothPrintConstantes.STATE_IDLE:
							addLog("handleMessage: STATE_NONE: not connectSocket");
							setConnectState(ClaseBluetoothPrintConstantes.STATE_IDLE);
							break;
						case ClaseBluetoothPrintConstantes.STATE_DISCONNECTED:
							addLog("handleMessage: STATE_DISCONNECTED");
							setConnectState(ClaseBluetoothPrintConstantes.STATE_DISCONNECTED);
							break;
						case ClaseBluetoothPrintConstantes.STATE_CONNECTING_ERROR:
							switch (_modelo) {
								case ClaseBluetoothPrintConstantes.MODEL_SM5802:
									if (_BTdriverSM5802 != null) {
										_BTdriverSM5802.stop();
										_BTdriverSM5802 = null;
									}
									break;
								case MODEL_SCREEN:
									if (_BTdriverSCREEN != null) {
										_BTdriverSCREEN.stop();
										_BTdriverSCREEN = null;
									}
									break;
								case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
									if (_BTdriverIPDA045 != null) {
										_BTdriverIPDA045.stop();
										_BTdriverIPDA045 = null;
									}
									break;
							}
							if (_BluetoothAdapter != null) {

								if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
									//dialog comprobar permisos BT
									return;
								}
								_BluetoothAdapter.disable();

								Handler h = new Handler();
								h.postDelayed(new Runnable() {
									@Override
									public void run() {
										if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
											//dialog comprobar permisos BT
											return;
										}
										_BluetoothAdapter.disable();

										SystemClock.sleep(500);
										conectarImpresoraBT();
									}
								}, 100);

							}


					}
					break;
				case ClaseBluetoothMsgTypes.MESSAGE_WRITE:
					break;
				case ClaseBluetoothMsgTypes.MESSAGE_READ:
					Bundle bundle1 = msg.getData();
					byte[] bEstado = bundle1.getByteArray("estado");
					float voltaje = (float) ((bEstado[0] * 256 + bEstado[1]) / 10.0);
					if (voltaje < 20)   setEstadoNivelBateria(voltaje);
					else setEstadoNivelBateria((float) -1.0);
					if ((bEstado[2] & 4) != 0) setEstadoPapel(false);
					else setEstadoPapel(true);
					if ((bEstado[2] & 8) != 0) setEstadoBateriaLow(true);
					else setEstadoBateriaLow(false);
					if ((bEstado[2] & 64) != 0) setEstadoOverheat(true);
					else setEstadoOverheat(false);

					if (handlerNotificaBT != null) {
						Message msg1 = handlerNotificaBT.obtainMessage(ClaseBluetoothMsgTypes.MESSAGE_READ);
						msg1.arg1 = 1;
						handlerNotificaBT.sendMessage(msg1);
					}

					break;
				case ClaseBluetoothMsgTypes.MESSAGE_DEVICE_NAME:
					break;
				case ClaseBluetoothMsgTypes.MESSAGE_TOAST:
					Log.i(TAG, "handleMessage: TOAST: " + msg.getData().getString(ClaseBluetoothMsgTypes.TOAST));
					addLog(msg.getData().getString(ClaseBluetoothMsgTypes.TOAST));

					switch (_modelo) {
						case ClaseBluetoothPrintConstantes.MODEL_SM5802:
							if (_BTdriverSM5802 != null) {
								setConnectState(ClaseBluetoothPrintConstantes.STATE_DISCONNECTED);
							}
							break;
						case MODEL_SCREEN:
							if (_BTdriverSCREEN != null) {
								setConnectState(ClaseBluetoothPrintConstantes.STATE_DISCONNECTED);
							}
							break;
						case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
							if (_BTdriverIPDA045 != null) {
								setConnectState(ClaseBluetoothPrintConstantes.STATE_DISCONNECTED);
							}
							break;
					}
					break;
				case ClaseBluetoothMsgTypes.MESSAGE_INFO:
					addLog(msg.getData().getString(ClaseBluetoothMsgTypes.INFO));
					String s = msg.getData().getString(ClaseBluetoothMsgTypes.INFO);
					if (s.length() == 0)
						s = String.format("int: %i" + msg.getData().getInt(ClaseBluetoothMsgTypes.INFO));
					Log.i(TAG, "handleMessage: INFO: " + s);
					break;
			}
		}
	};


	public static void conectarImpresoraBT() {

		if (  getBlueToothAdapter() != null || _modelo == MODEL_SCREEN) {
			set_conectando(true);
			if ((_modelo == MODEL_SCREEN && getBTdriverSCREEN() != null) && !getBlueToothAdapter().isEnabled()) {
				Log.i(TAG, "solicita enable BT");

				if (handlerNotificaBT != null) {
					if (timerBT != null) timerBT.cancel();
					if (timerBT_Task != null) timerBT_Task.cancel();
					timerBT_Task = null;
					timerBT = null;

					Message msg = handlerNotificaBT.obtainMessage(ClaseBluetoothMsgTypes.MESSAGE_REQUEST_ENABLE_BT);
					handlerNotificaBT.sendMessage(msg);
				}
			} else {

				if (checkPrinterStatus() == ClaseBluetoothPrintConstantes.STATE_DISCONNECTED ||
					checkPrinterStatus() == ClaseBluetoothPrintConstantes.STATE_IDLE ||
					checkPrinterStatus() == ClaseBluetoothPrintConstantes.STATE_LISTEN) {
					setupComm();
				}

			}
		}
	}

	public static void desconectarImpresoraBT() {

		if (getBlueToothAdapter() != null) {

			if (_modelo == ClaseBluetoothPrintConstantes.MODEL_SM5802) {
				if (_BTdriverSM5802 != null) _BTdriverSM5802.stop();
			} else if (_modelo == ClaseBluetoothPrintConstantes.MODEL_IPDA045) {
				if (_BTdriverIPDA045 != null) _BTdriverIPDA045.stop();
			} else if (_modelo == ClaseBluetoothPrintConstantes.MODEL_SCREEN) {
				if (_BTdriverSCREEN != null) _BTdriverSCREEN.stop();
			}
		}
	}

	public static void conectarSocketBT() {
		if ((_modelo == MODEL_SCREEN || getBlueToothAdapter() != null) &&
				checkPrinterStatus() == ClaseBluetoothPrintConstantes.STATE_CONNECTED) {

			switch (_modelo) {
				case ClaseBluetoothPrintConstantes.MODEL_SM5802:
					_BTdriverSM5802.connectSocket();
					break;
				case ClaseBluetoothPrintConstantes.MODEL_SCREEN:
					_BTdriverSCREEN.connectSocket();
					break;
				case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
					_BTdriverIPDA045.connectSocket();
					break;
			}
		}
	}

	public static void desconectarSocketBT() {
		if (getBlueToothAdapter() != null) {

			if (ClaseBluetooth.checkPrinterStatus() == ClaseBluetoothPrintConstantes.STATE_CONNECTED_SOCKET) {

				new ClaseBluetooth().detenerTimer();

				if (_modelo == ClaseBluetoothPrintConstantes.MODEL_SM5802) {
					_noDesconectarBT = true;
					_BTdriverSM5802.disconnectSocket();
				} else if (_modelo == ClaseBluetoothPrintConstantes.MODEL_IPDA045) {
					_BTdriverIPDA045.disconnectSocket();
				} else if (_modelo == ClaseBluetoothPrintConstantes.MODEL_SCREEN) {
					_BTdriverSCREEN.disconnectSocket();
				}

				Log.d(TAG, "BT fin desconectarSocketBT");

				new ClaseBluetooth().enableTimer(10000, false);
			}
		}
	}

	public static void setPrinterStatus(int status) {

		switch (_modelo) {
			case ClaseBluetoothPrintConstantes.MODEL_SM5802:
				if (_BTdriverSM5802 != null)
					try {
						_BTdriverSM5802.setState(status);

					} catch (Exception e) {
					}
				break;
			case ClaseBluetoothPrintConstantes.MODEL_SCREEN:
				if (_BTdriverSCREEN != null)
					try {
						_BTdriverSCREEN.setState(status);
					} catch (Exception e) {
					}
				break;
			case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
				if (_BTdriverIPDA045 != null)
					try {
						_BTdriverIPDA045.setState(status);
					} catch (Exception e) {
					}
				break;
		}
	}

	public static int checkPrinterStatus() {
		int estado = ClaseBluetoothPrintConstantes.STATE_IDLE;

		switch (_modelo) {
			case ClaseBluetoothPrintConstantes.MODEL_SM5802:
				if (_BTdriverSM5802 != null)
					try {
						estado = _BTdriverSM5802.getState();
					} catch (Exception e) {
					}
				break;

			case ClaseBluetoothPrintConstantes.MODEL_SCREEN:
				if (_BTdriverSCREEN != null)
					try {
						estado = _BTdriverSCREEN.getState();
					} catch (Exception e) {
					}
				break;
			case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
				if (_BTdriverIPDA045 != null)
					try {
						estado = _BTdriverIPDA045.getState();
					} catch (Exception e) {
					}
				break;
		}
		return estado;
	}

	public static String getPrinterError() {
		String estado = "";

		switch (_modelo) {
			case ClaseBluetoothPrintConstantes.MODEL_SM5802:
				break;
			case ClaseBluetoothPrintConstantes.MODEL_SCREEN:
				break;
			case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
				break;
		}
		return estado;
	}

	public static boolean checkPrinterConectado() {
		boolean estado = true;

		switch (_modelo) {
			case ClaseBluetoothPrintConstantes.MODEL_SM5802:
				if (_BTdriverSM5802 != null)
					try {
						if (_BTdriverSM5802.getState() != STATE_CONNECTED_SOCKET)
							estado = false;
					} catch (Exception e) {
					}
				break;

			case ClaseBluetoothPrintConstantes.MODEL_SCREEN:
				if (_BTdriverSCREEN != null)
					try {
						if (_BTdriverSCREEN.getState() != STATE_CONNECTED_SOCKET)
							estado = false;
					} catch (Exception e) {
					}
				break;
			case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
				if (_BTdriverIPDA045 != null)
					try {
						if (_BTdriverIPDA045.getState() != STATE_CONNECTED_SOCKET)
							estado = false;
					} catch (Exception e) {
					}
				break;
		}
		return estado;
	}

	public static boolean checkPrinterDriverNull() {
		boolean estado = true;

		switch (_modelo) {
			case ClaseBluetoothPrintConstantes.MODEL_SM5802:
				if (_BTdriverSM5802 != null) estado = false;
				break;
			case ClaseBluetoothPrintConstantes.MODEL_SCREEN:
				if (_BTdriverSCREEN != null) estado = false;
				break;
			case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
				if (_BTdriverIPDA045 != null) estado = false;
				break;
		}
		return estado;
	}


	private static void iniciaPrinter() {
		switch (_modelo) {
			case ClaseBluetoothPrintConstantes.MODEL_SM5802:
				if (_BTdriverSM5802.getState() == STATE_CONNECTED_SOCKET)
					_BTdriverSM5802.Begin();
				break;
			case ClaseBluetoothPrintConstantes.MODEL_SCREEN:
				_BTdriverSCREEN.Begin();
				break;
			case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
				_BTdriverIPDA045.iniciaListaDatos();
				_BTdriverIPDA045.Begin();
				break;
		}
	}

	private static void imprimeSaltoLinea(int lineas, boolean force) {

		for (int t = 0; t < lineas; t++) {
			switch (_modelo) {
				case ClaseBluetoothPrintConstantes.MODEL_SM5802:
					if (_BTdriverSM5802.getState() == STATE_CONNECTED_SOCKET) {
						if (force) {
							_BTdriverSM5802.CR();
						}
					}
					break;
				case ClaseBluetoothPrintConstantes.MODEL_SCREEN:
					if (_BTdriverSCREEN.getState() == STATE_CONNECTED_SOCKET) {
						//_BTdriverSCREEN.BT_Write("<br>".getBytes());
						//_BTdriverSCREEN.CR();
					}
					break;
				case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
					if (_BTdriverIPDA045.getState() == STATE_CONNECTED_SOCKET) {
						_BTdriverIPDA045.CR();
					}
					break;
			}
		}
	}

	private static String hmtlSaltoLinea(int lineas) {
		return "<br>";
	}

	private static void imprimeImagen(Bitmap bitmap) {

		if (checkPrinterConectado() == true) {
			switch (_modelo) {
				case ClaseBluetoothPrintConstantes.MODEL_SM5802:
					break;
				case ClaseBluetoothPrintConstantes.MODEL_SCREEN:
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
					byte[] byteFormat = stream.toByteArray();
					String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
					String html = "<img style='display:block; margin-left:auto;margin-right:auto' src=\"data:image/png;base64," + imgString + "\"/><br>";
					_BTdriverSCREEN.BT_Write(html.getBytes());
					break;
				case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
					_BTdriverIPDA045.printBitmap(bitmap);
					break;
			}
		}
	}

	private static String htmlImagen(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteFormat = stream.toByteArray();
		String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
		String html = "<img align='center' src=\"data:image/png;base64," + imgString + "\"/><br>";
		return html;
	}


	private static void imprimeArrayByte(byte[] data, StringBuffer sb, int fuente, int estilo, int align) {

		String tmp = "";
		if (data == null && sb.toString().length() > 0) data = sb.toString().getBytes();

		if (checkPrinterConectado() == true) {
			switch (_modelo) {
				case ClaseBluetoothPrintConstantes.MODEL_SM5802:
					_BTdriverSM5802.BT_Write(data);
					_BTdriverSM5802.CR();
					break;
				case ClaseBluetoothPrintConstantes.MODEL_SCREEN:
					tmp = htmlArrayByte(data, null, fuente, estilo, align, ClaseBluetoothPrintConstantes.MODEL_SCREEN);
					_BTdriverSCREEN.BT_Write(tmp.getBytes());
					break;
				case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
					_BTdriverIPDA045.BT_Write(sb.toString(), fuente, estilo, align);
					break;
			}
		}
	}

	private static String htmlArrayByte(byte[] data, StringBuffer sb, int fuente, int estilo, int align, int modelo) {
		if (data == null && sb.toString().length() > 0) data = sb.toString().getBytes();

		String tmp = new String(data, StandardCharsets.UTF_8);
		String tag1 = "";
		String tag2 = "";
		String style = "";

		if (modelo == ClaseBluetoothPrintConstantes.MODEL_SCREEN) {
			tmp = tmp.replace(Character.toString((char) 10), "<br>");
		}
		if (align == ALIGN_LEFT) {
			style = " style='margin:0em; ' ";
		} else if (align == ALIGN_CENTER) {
			style = " style='margin:0em; text-align:center'";
		} else if (align == ALIGN_RIGTH) {
			style = " style='margin:0em; text-align:right'";
		}

		if (fuente == FUENTE_NORMAL && estilo == ESTILO_NORMAL) {
			tag1 = "<div" + style + ">";
			tag2 = "</div>";
		} else if (fuente == FUENTE_NORMAL && estilo == ESTILO_BOLD) {
			tag1 = "<div" + style + "><b>";
			tag2 = "</b></div>";
		}
		if (fuente == FUENTE_SMALL && estilo == ESTILO_NORMAL) {
			tag1 = "<div" + style + "><small>";
			tag2 = "</small></div>";
		} else if (fuente == FUENTE_SMALL && estilo == ESTILO_BOLD) {
			tag1 = "<div" + style + "><small>";
			tag2 = "</small></div>";
		} else if (fuente == FUENTE_BIG && estilo == ESTILO_NORMAL) {
			tag1 = "<div" + style + "><big>";
			tag2 = "</big></div>";
		} else if (fuente == FUENTE_BIG && estilo == ESTILO_BOLD) {
			tag1 = "<div" + style + "><big>";
			tag2 = "</big></div>";
		}

		tmp = tag1 + tmp + tag2;

		return tmp;
	}


	public static void addLog(String s) {
		Log.d(TAG, s);
	}


	private static void pausa(int milis) {
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public static String imprimeComanda(CabeceraTiquet cabeceraTiquet, ArrayList<GrabaLineasVenta> lineasVentas) {
		if (checkPrinterConectado() == false) {

			log("Please connect first!");
			return "ERR_CON"; //does not match file pattern for a print file
		}

		StringBuffer sb = new StringBuffer();

		try {

			sb.append("<b>"+cabeceraTiquet.getRazon()+"</b><br>");
			sb.append("<b>"+cabeceraTiquet.getCif()+"</b><br>");
			sb.append("<b>"+cabeceraTiquet.getTPV()+"</b><br>");
			sb.append(cabeceraTiquet.getHora()+"<br>");
			sb.append("Mesa: "+cabeceraTiquet.getMesa()+"<br>");
			sb.append("Pax: "+cabeceraTiquet.getPax()+"<br>");
			sb.append("Habitación: "+cabeceraTiquet.getApto()+"<br>");
			sb.append( ClaseUtils.padLeft("","-",32) +"<br>");

			//nueos (estado = 'anadir'),  bebidas (orden_plato = 0)
			ArrayList<GrabaLineasVenta> tmpLineas = null;
			try {
			 tmpLineas = new ArrayList<GrabaLineasVenta>( lineasVentas.stream().filter(
						linea -> "anadir".equals(linea.getEstado().toLowerCase())
						&& linea.getOrden_platos() == 0).collect(Collectors.toList()));
			}
			catch (Exception e){
				tmpLineas = new ArrayList<GrabaLineasVenta>();
			}

			if (tmpLineas != null && tmpLineas.size() > 0 ) {
				sb.append("<b>---------- BEBIDAS -------------</b><br>");
				for (GrabaLineasVenta tmp : tmpLineas) {
					sb.append( ClaseUtils.padLeft(String.valueOf(tmp.getCantidad())," ",3) + "       " +ClaseUtils.maxLength(tmp.getDescripcion(),20) + "<br>");
					if (tmp.getNota() != null && !tmp.getNota().equals("")) {
						sb.append("   " + tmp.getNota().substring(0,28) + "<br>");
					}
				}
				sb.append("<br>");
			}

			//nuevos (estado = 'anadir'),  entrantes (orden_plato = 1)  si orden == 5(sin orden) se considera entrante
			try {
				tmpLineas =  new ArrayList<GrabaLineasVenta>( lineasVentas.stream().filter(
							linea -> "anadir".equals(linea.getEstado().toLowerCase())
							&& (linea.getOrden_platos() == 1 || linea.getOrden_platos() == 5)).collect(Collectors.toList()));
			}
			catch (Exception e){
				tmpLineas = new ArrayList<GrabaLineasVenta>();
			}

			if (tmpLineas != null && tmpLineas.size() > 0 ) {
				sb.append("<b>---------- ENTRANTES -----------</b><br>");
				for (GrabaLineasVenta tmp : tmpLineas) {
					sb.append( ClaseUtils.padLeft(String.valueOf(tmp.getCantidad())," ",3) + "       " + ClaseUtils.maxLength(tmp.getDescripcion(),20)  + "<br>");
					if (tmp.getNota() != null && !tmp.getNota().equals("")) {
						sb.append("   " + tmp.getNota().substring(0,28) + "<br>");
					}
				}
				sb.append("<br>");
			}

			//nuevos (estado = 'anadir'),  primeros (orden_plato = 2)
			try {
				tmpLineas = new ArrayList<GrabaLineasVenta>( lineasVentas.stream().filter(
							linea -> "anadir".equals(linea.getEstado().toLowerCase())
							&& linea.getOrden_platos() == 2).collect(Collectors.toList()));
			}
			catch (Exception e){
				tmpLineas = new ArrayList<GrabaLineasVenta>();
			}

			if (tmpLineas != null && tmpLineas.size() > 0 ) {
				sb.append("<b>---------- PRIMEROS ------------</b><br>");
				for (GrabaLineasVenta tmp : tmpLineas) {
					sb.append( ClaseUtils.padLeft(String.valueOf(tmp.getCantidad())," ",3) + "       " + ClaseUtils.maxLength(tmp.getDescripcion(),20)  + "<br>");
					if (tmp.getNota() != null && !tmp.getNota().equals("")) {
						sb.append("   " + tmp.getNota().substring(0,28) + "<br>");
					}
				}
				sb.append("<br>");
			}

			//nuevos (estado = 'anadir'),  SEGUNDOS (orden_plato = 3)
			try {
				tmpLineas = new ArrayList<GrabaLineasVenta>(lineasVentas.stream().filter(
							linea -> "anadir".equals(linea.getEstado().toLowerCase())
							&& linea.getOrden_platos() == 3).collect(Collectors.toList()));
			}
			catch (Exception e){
				tmpLineas = new ArrayList<GrabaLineasVenta>();
			}

			if (tmpLineas != null && tmpLineas.size() > 0 ) {
				sb.append("<b>--------- SEGUNDOS ------------</b><br>");
				for (GrabaLineasVenta tmp : tmpLineas) {
					sb.append( ClaseUtils.padLeft(String.valueOf(tmp.getCantidad())," ",3) + "       " + ClaseUtils.maxLength(tmp.getDescripcion(),20)  + "<br>");
					if (tmp.getNota() != null && !tmp.getNota().equals("")) {
						sb.append("   " + tmp.getNota().substring(0,28) + "<br>");
					}
				}
				sb.append("<br>");
			}

			//nuevos (estado = 'anadir'),  POSTRES (orden_plato = 4)
			try {
				tmpLineas =  new ArrayList<GrabaLineasVenta>( lineasVentas.stream().filter(
							linea -> "anadir".equals(linea.getEstado().toLowerCase())
							&& linea.getOrden_platos() == 4).collect(Collectors.toList()));
			}
			catch (Exception e){
				tmpLineas = new ArrayList<GrabaLineasVenta>();
			}


			if (tmpLineas != null && tmpLineas.size() > 0 ) {
				sb.append("<b>---------- POSTRES -------------</b><br>");
				for (GrabaLineasVenta tmp : tmpLineas) {
					sb.append( ClaseUtils.padLeft(String.valueOf(tmp.getCantidad())," ",3) + "       " + ClaseUtils.maxLength(tmp.getDescripcion(),20)  + "<br>");
					if (tmp.getNota() != null && !tmp.getNota().equals("")) {
						sb.append("   " + tmp.getNota().substring(0,28) + "<br>");
					}
				}
				sb.append("<br>");
			}

			//cambios (estado = 'actualizar')
			try {
				tmpLineas =  new ArrayList<GrabaLineasVenta>( lineasVentas.stream().filter(
							linea -> "actualizar".equals(linea.getEstado().toLowerCase())).collect(Collectors.toList()));
			}
			catch (Exception e){
				tmpLineas = new ArrayList<GrabaLineasVenta>();
			}

			if (tmpLineas != null && tmpLineas.size() > 0 ) {
				sb.append("<b>--------- CAMBIOS -------------</b><br>");
				for (GrabaLineasVenta tmp : tmpLineas) {
					sb.append(ClaseUtils.padLeft(String.valueOf(tmp.getCantidad())," ",3)+
							  ClaseUtils.padRight("("+tmp.getCantidad_ant()+")"," ",7)+" "+
							  ClaseUtils.maxLength(tmp.getDescripcion(),20) +"<br>" );
					if (tmp.getNota() != null && !tmp.getNota().equals("")) {
						sb.append(tmp.getNota() + "<br>");
					}
				}
				sb.append("<br>");
			}


			//cambios (estado = 'eliminar')
			try {
				tmpLineas =  new ArrayList<GrabaLineasVenta>( lineasVentas.stream().filter(
						linea -> "eliminar".equals(linea.getEstado().toLowerCase())).collect(Collectors.toList()));
			}
			catch (Exception e){
				tmpLineas = new ArrayList<GrabaLineasVenta>();
			}


			if (tmpLineas != null && tmpLineas.size() > 0 ) {
				sb.append("<b>---------- CANCELACIONES -------</b><br>");
				for (GrabaLineasVenta tmp : tmpLineas) {
					sb.append( ClaseUtils.padLeft(String.valueOf(tmp.getCantidad())," ",3) + "       " + ClaseUtils.maxLength(tmp.getDescripcion(),20)  + "<br>");

				}
				sb.append("<br>");
			}


			//buscaCaracteresControl(sb,false);
			limpiaCaracteres(sb);

			int readCount = 0;
			readCount = sb.length();
			if (readCount > 0) {

                /*
				iniciaPrinter();
				imprimeSaltoLinea(1, false);

				log("imprimo " + readCount + "bytes");

				byte[] bufOut = new byte[readCount];
				bufOut = sb.toString().getBytes();

				imprimeArrayByte(bufOut, sb, FUENTE_NORMAL, ESTILO_NORMAL, 0);

				imprimeSaltoLinea(4, true);

				startPrint(2);
                */

				imprimeDatos(sb,false);
 				imprimeSaltoLinea(4,true);
				new Thread() {
					@Override
					public void run() {
						super.run();
						ClaseBluetooth.startPrint(1);
					}
				}.start();

			}
			log(String.format("printed " + String.valueOf(readCount) + " bytes"));

		} catch (Exception e) {
			Log.e(TAG, "Exception in printFile: " + e.getMessage());

		}

		return "";
	}

	public static String imprimeCuenta(CabeceraTiquet cabeceraTiquet, ClaseSubMesas submesa, ClaseUsers user) {
		if (checkPrinterConectado() == false) {

			log("Please connect first!");
			return "ERR_CON"; //does not match file pattern for a print file
		}

		StringBuffer sb = new StringBuffer();

		try {

			sb.append("<b>"+cabeceraTiquet.getRazon()+"</b><br>");
			sb.append("<b>CIF: "+cabeceraTiquet.getCif()+"</b><br>");
			sb.append("<b>"+cabeceraTiquet.getTPV()+"</b><br>");
			sb.append("<sn>Camarero: "+user.nombre+"</sn><br>");
			sb.append("<sn>Mesa: "+cabeceraTiquet.getMesa()+"-"+submesa.submesa+"</sn><br>");
			sb.append("<sn>Habitación: " + cabeceraTiquet.getApto() + "</sn><br>");
			sb.append("<sn>Fra.Simpilificada : "+cabeceraTiquet.getNfactura()+"</sn><br>");
			sb.append("<sn>"+cabeceraTiquet.getPago_fecha_hora()+"</sn><br>");
			sb.append("<sn>"+ ClaseUtils.padLeft("","-",32) +"</sn><br>");

			//nueos (estado = 'anadir'),  bebidas (orden_plato = 0)

			if (submesa.lineasVentas != null && submesa.lineasVentas.size() > 0 ) {
				for (ClaseLineaVentas tmp : submesa.lineasVentas) {

					sb.append("<sn>"+ ClaseUtils.padLeft(String.valueOf(tmp.cantidad),"#",3) + " " +
							ClaseUtils.padRight(ClaseUtils.maxLength(tmp.descripcion,20) ,"#",20) + " "+
							ClaseUtils.padLeft( ClaseUtils.double2string(tmp.teuros,2),"#",6)+"</sn><br>");

				}
			}
			sb.append("<sn>"+ ClaseUtils.padLeft("","-",32) +"</sn><br>");

			//comprobar tipo de igic A,I,D,E
			double total = 0.0;


			if (ClaseCondicionesVenta.tipo_igic.toLowerCase().equals("e")){
				sb.append("<b> ---- IGIC EXENTO ----------</b><br>");
				total = submesa.importe;
			}
			else if (ClaseCondicionesVenta.tipo_igic.toLowerCase().equals("i")){
				sb.append("<b> ---- IGIC INCLUIDO --------</b><br>");
				total = submesa.importe;
			}
			else {
				//igic desglosado
				double porcentaje = ClaseCondicionesVenta.igic;
				double base = 0.0;
				double igic = 0.0;
				if (ClaseCondicionesVenta.tipo_igic.toLowerCase().equals("d")){
					base = submesa.importe /( (100+porcentaje)/100);
					base = Double.parseDouble(ClaseUtils.double2string(base,2).replace(",","."));
					igic = submesa.importe - base;
					total = submesa.importe;
				}
				else{
					//igic añadido
					base = submesa.importe;
					igic = base * (porcentaje/100);
					igic = Double.parseDouble(ClaseUtils.double2string(igic,2).replace(",","."));
					total = base + igic;
				}
				sb.append("<sn>"+ClaseUtils.padLeft("BASE","#",9)+
						ClaseUtils.padLeft("%IGIC","#",9)+
						ClaseUtils.padLeft("IMP.IGIC","#",14)+"</sn><br>");

				sb.append("<sn>"+ ClaseUtils.padLeft(ClaseUtils.double2string(base,2),"#",9)+
						ClaseUtils.padLeft(ClaseUtils.double2string(porcentaje,2),"#",9)+
						ClaseUtils.padLeft(ClaseUtils.double2string(igic,2),"#",14)+"</sn><br>");

			}


			sb.append("<sn>"+ ClaseUtils.padLeft("","-",32) +"</sn><br>");
			sb.append("<sn>"+ ClaseUtils.padLeft("TOTAL EUROS"," ",20)+ClaseUtils.padLeft( ClaseUtils.double2string(total,2),"#",12)+"</sn><br>");

			//forma de pago
			if ("n".contains(cabeceraTiquet.getFormapago().toLowerCase())) {
				sb.append("<sn>"+ ClaseUtils.padLeft("","-",32) +"</sn><br>");
				sb.append("<b> ---- PENDIENTE DE PAGO ----</b><br>");
			}
			if ("ez".contains(cabeceraTiquet.getFormapago().toLowerCase())) {
				sb.append("<sn>"+ ClaseUtils.padLeft("Efectivo","#",20)+ClaseUtils.padLeft( cabeceraTiquet.getEfectivo(),"#",12)+"</sn><br>");
				sb.append("<sn>"+ ClaseUtils.padLeft("Entregado","#",22)+ClaseUtils.padLeft( cabeceraTiquet.getEntregado() ,"#",10)+"</sn><br>");
				sb.append("<sn>"+ClaseUtils.padLeft("Cambio","#",22)+ClaseUtils.padLeft( cabeceraTiquet.getCambio(),"#",10)+"</sn><br>");
			}
			if ("htz".contains(cabeceraTiquet.getFormapago().toLowerCase())) {
				sb.append("<sn>"+ ClaseUtils.padLeft("Tarjeta","#",20)+ClaseUtils.padLeft( cabeceraTiquet.getTarjeta(),"#",12)+"</sn><br>");
			}
			if ("c".contains(cabeceraTiquet.getFormapago().toLowerCase())) {
				sb.append("<sn>"+ ClaseUtils.padLeft("Cuenta casa ("+cabeceraTiquet.getCodclicasa()+")","#",20)+ClaseUtils.padLeft( cabeceraTiquet.getTarjeta(),"#",12)+"</sn><br>");
			}
			if ("f".contains(cabeceraTiquet.getFormapago().toLowerCase())) {
				sb.append("<sn>"+ ClaseUtils.padLeft("Pension cliente","#",32)+"</sn><br>");
			}
			if ("r".contains(cabeceraTiquet.getFormapago().toLowerCase())) {
				sb.append("<sn>"+ ClaseUtils.padLeft("Credito apto "+cabeceraTiquet.getApto(),"#",20)+ClaseUtils.padLeft( cabeceraTiquet.getTarjeta(),"#",12)+"</sn><br>");
			}

			sb.append("<sn>"+ ClaseUtils.padLeft("","-",32) +"</sn><br>");
			if (ClaseCondicionesVenta.tipo_igic.toLowerCase().equals("e")) {
				sb.append("<sn>COMERCIANTE MINORISTA</sn><br>");
			}
			sb.append("<sn>"+cabeceraTiquet.getHora()+"</sn><br>");

			//buscaCaracteresControl(sb,false);
			limpiaCaracteres(sb);

			int readCount = 0;
			readCount = sb.length();
			if (readCount > 0) {

				/*
				iniciaPrinter();

				imprimeSaltoLinea(1, false);

				log("imprimo " + readCount + "bytes");

				byte[] bufOut = new byte[readCount];
				bufOut = sb.toString().getBytes();

				imprimeArrayByte(bufOut, sb, FUENTE_NORMAL, ESTILO_NORMAL, 0);

				//prueba
				//BTDriverSM5802.printImageBMP(cabeceraTiquet.getFirma());
				//


				imprimeSaltoLinea(4, true);

				startPrint(2);
*/

				imprimeDatos(sb,false);
				imprimeSaltoLinea(4,true);
				new Thread() {
					@Override
					public void run() {
						super.run();
						ClaseBluetooth.startPrint(1);
					}
				}.start();

			}
			log(String.format("printed " + String.valueOf(readCount) + " bytes"));

		} catch (Exception e) {
			Log.e(TAG, "Exception in printFile: " + e.getMessage());

		}

		return "";
	}

	public static String imprimeCopiaCuenta(String empresa, String cif,String tpv, String mesa,String camarero,ClaseFacturaCabecera cabecera, ArrayList<ClaseLineaVentas> lineasVentas) {
		if (checkPrinterConectado() == false) {

			log("Please connect first!");
			return "ERR_CON"; //does not match file pattern for a print file
		}

		StringBuffer sb = new StringBuffer();

		try {
			sb.append("<b>"+empresa+"</b><br>");
			sb.append("<b>CIF: "+cif+"</b><br>");
			sb.append("<b>"+tpv+"</b><br>"); //tpv
			sb.append("Camarero: "+camarero+"<br>");
			sb.append("Mesa: "+mesa+"<br>");
			sb.append("<sn>Habitación: " + cabecera.habitacion + "</sn><br>");
			sb.append("Fra.Simpilificada : "+cabecera.nfactura+"<br>");
			sb.append("Fecha: "+cabecera.pago_fecha_hora+"<br>");
			sb.append( ClaseUtils.padLeft("","-",32) +"<br>");

			//nueos (estado = 'anadir'),  bebidas (orden_plato = 0)

			if (lineasVentas != null && lineasVentas.size() > 0 ) {
				for (ClaseLineaVentas tmp : lineasVentas) {

					sb.append( ClaseUtils.padLeft(String.valueOf(tmp.cantidad)," ",3) + " " +
							ClaseUtils.padRight(ClaseUtils.maxLength(tmp.descripcion,20) ," ",20) + " "+
							ClaseUtils.padLeft( ClaseUtils.double2string(tmp.teuros,2)," ",6)+"<br>");

				}
			}
			sb.append( ClaseUtils.padLeft("","-",32) +"<br>");

			//comprobar tipo de igic A,I,D,E
			double total = 0.0;


			if (ClaseCondicionesVenta.tipo_igic.toLowerCase().equals("e")){
				sb.append("<b> ---- IGIC EXENTO ----------</b><br>");
				total = cabecera.teuros;
			}
			else if (ClaseCondicionesVenta.tipo_igic.toLowerCase().equals("i")){
				sb.append("<b> ---- IGIC INCLUIDO --------</b><br>");
				total = cabecera.teuros;
			}
			else {
				//igic desglosado
				double porcentaje = ClaseCondicionesVenta.igic;
				double base = 0.0;
				double igic = 0.0;
				if (ClaseCondicionesVenta.tipo_igic.toLowerCase().equals("d")){
					igic = cabecera.tigic;
					total = cabecera.teuros;
					base =total - igic;
				}
				else{
					//igic añadido
					base = cabecera.teuros;
					igic = cabecera.tigic;
					total = base + igic;
				}
				sb.append(ClaseUtils.padLeft("BASE"," ",9)+
						ClaseUtils.padLeft("%IGIC"," ",9)+
						ClaseUtils.padLeft("IMP.IGIC"," ",14)+"<br>");

				sb.append(ClaseUtils.padLeft(ClaseUtils.double2string(base,2)," ",9)+
						ClaseUtils.padLeft(ClaseUtils.double2string(porcentaje,2)," ",9)+
						ClaseUtils.padLeft(ClaseUtils.double2string(igic,2)," ",14)+"<br>");

			}


			sb.append( ClaseUtils.padLeft("","-",32) +"<br>");
			sb.append( ClaseUtils.padLeft("TOTAL EUROS"," ",20)+ClaseUtils.padLeft( ClaseUtils.double2string(total,2)," ",12)+"<br>");

			//forma de pago
			sb.append( ClaseUtils.padLeft("Efectivo"," ",20)+ClaseUtils.padLeft( ClaseUtils.double2string(cabecera.efectivo,2)," ",12)+"<br>");
			sb.append( ClaseUtils.padLeft("Tarjeta"," ",20)+ClaseUtils.padLeft( ClaseUtils.double2string(cabecera.tarjeta,2)," ",12)+"<br>");
			sb.append( ClaseUtils.padLeft("Cuenta casa "," ",20)+ClaseUtils.padLeft( ClaseUtils.double2string(cabecera.ctacasa,2)," ",12)+"<br>");
			sb.append( ClaseUtils.padLeft("Credito apto "," ",20)+ClaseUtils.padLeft( ClaseUtils.double2string(cabecera.credito,2)," ",12)+"<br>");

			sb.append( ClaseUtils.padLeft("","-",32) +"<br>");
			if (ClaseCondicionesVenta.tipo_igic.toLowerCase().equals("e")) {
				sb.append("COMERCIANTE MINORISTA<br>");
			}

			//buscaCaracteresControl(sb,false);
			limpiaCaracteres(sb);

			int readCount = 0;
			readCount = sb.length();
			if (readCount > 0) {

				/*
				iniciaPrinter();

				imprimeSaltoLinea(1, false);

				log("imprimo " + readCount + "bytes");

				byte[] bufOut = new byte[readCount];
				bufOut = sb.toString().getBytes();

				imprimeArrayByte(bufOut, sb, FUENTE_NORMAL, ESTILO_NORMAL, 0);

				//prueba
				//BTDriverSM5802.printImageBMP(cabeceraTiquet.getFirma());
				//


				imprimeSaltoLinea(4, true);

				startPrint(2);
*/

				imprimeDatos(sb,false);
				imprimeSaltoLinea(4,true);
				new Thread() {
					@Override
					public void run() {
						super.run();
						ClaseBluetooth.startPrint(1);
					}
				}.start();

			}
			log(String.format("printed " + String.valueOf(readCount) + " bytes"));

		} catch (Exception e) {
			Log.e(TAG, "Exception in printFile: " + e.getMessage());

		}

		return "";
	}


	public static String imprimeTiquetTest(Context context) {
		log("imprimiendo");

		if (checkPrinterConectado() == false) {
			log("Please connect first!");
			return "ERR_CON"; //does not match file pattern for a print file
		}

		log("conectado a la impresora");


		StringBuffer sb = new StringBuffer();

		try {

			sb.append("<b>Prueba de impresion</b><br>");
			sb.append(ClaseUtils.now("dd/MM/yyyy HH:mm:ss"));

			buscaCaracteresControl(sb,false);
			limpiaCaracteres(sb);

			int readCount = 0;
			readCount = sb.length();
			if (readCount > 0) {

				iniciaPrinter();


				imprimeSaltoLinea(1, false);

				log("imprimo " + readCount + "bytes");

				byte[] bufOut = new byte[readCount];
				bufOut = sb.toString().getBytes();

				imprimeArrayByte(bufOut, sb, FUENTE_NORMAL, ESTILO_NORMAL, 0);

				imprimeSaltoLinea(4, true);

				startPrint(2);


			}
			log(String.format("printed " + String.valueOf(readCount) + " bytes"));

		} catch (Exception e) {
			Log.e(TAG, "Exception in printFile: " + e.getMessage());

		}
		return "";
	}

	public static void imprimirCadena(String cadena, int fuente, int estilo) {
		iniciaPrinter();

		switch (_modelo) {
			case ClaseBluetoothPrintConstantes.MODEL_SM5802:
				imprimeSaltoLinea(1, false);
				break;
		}

		StringBuffer sb = new StringBuffer();
		sb.append(cadena + "\n");

		imprimeArrayByte(sb.toString().getBytes(), sb, fuente, estilo, 0);
		new Thread() {
			@Override
			public void run() {
				super.run();
				ClaseBluetooth.startPrint(2);
			}

		}.start();

	}

	public static void startPrint(int lineas) {
		int intentos = 0;
		switch (_modelo) {
			case ClaseBluetoothPrintConstantes.MODEL_SM5802:
				break;
			case ClaseBluetoothPrintConstantes.MODEL_SCREEN:
				break;
			case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
				_BTdriverIPDA045.startPrint();
				pausa(100);
				intentos = 30;
				while (intentos > 0) {
					pausa(100);
					intentos -= 1;
				}
				Log.d(TAG, "Finalizada la impresion....");
				break;
	}
	}

	static class TimerBT_Task extends TimerTask {

		@Override
		public void run() {
			if (_imprimiendo && !_forzarTest) {
				Log.i(TAG, "no verifica conexion BT. Está imprimiendo");
				return;
			}
			_forzarTest = false;
			Log.i(TAG, "verifica conexion BT");

			switch (_modelo) {
				case ClaseBluetoothPrintConstantes.MODEL_SM5802:

					conectarImpresoraBT();
					break;
				case ClaseBluetoothPrintConstantes.MODEL_SCREEN:
					if (_BTdriverSCREEN == null) {
						conectarImpresoraBT();
					} else if (_BTdriverSCREEN.getState() != BTDriverSCREEN.STATE_CONNECTED) {
						conectarImpresoraBT();
					}
					break;
				case ClaseBluetoothPrintConstantes.MODEL_IPDA045:
					if (_BTdriverIPDA045 == null) {
						conectarImpresoraBT();
					} else if (_BTdriverIPDA045.getState() != BTDriverIPDA045.STATE_CONNECTED) {
						conectarImpresoraBT();
					} else {
						_BTdriverIPDA045.getPrinterStatus();
						new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
							@Override
							public void run() {
								int printerStatus = BTDriverIPDA045.mPrinterState;
								Log.i(TAG, "Estado de impresora: " + printerStatus);
								switch (printerStatus) {
									case BTDriverIPDA045.PRINTER_NORMAL:
										break;
									case BTDriverIPDA045.PRINTER_PAPERLESS:
										if (Looper.myLooper() == null) {
											Looper.prepare();
										}
										DialogosPrinter.Aviso aviso = new DialogosPrinter.Aviso();
										aviso.setResId(-1);
										aviso.setOnclick(new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialogInterface, int i) {
												DialogosPrinter.Aviso.dismiss();
											}
										});
										aviso.mostrarAviso("Aviso", "IMPRESORA SIN PAPEL", context);
										Log.i(TAG, "Salgo del loop");
										break;
									case BTDriverIPDA045.PRINTER_THP_HIGH_TEMPERATURE:
										break;
									case BTDriverIPDA045.PRINTER_MOTOR_HIGH_TEMPERATURE:
										;
										break;
									case BTDriverIPDA045.PRINTER_IS_BUSY:
										break;
									case BTDriverIPDA045.PRINTER_ERROR_UNKNOWN:
										break;
								}

							}
						}, 200);
					}
					break;
			}

			Log.i(TAG, "finalizo timerBT");
		}
	}


	static void log(String msg) {
		Log.d(TAG, msg);
	}


	private static void guardaBitmap(Bitmap bitmap, String file) {
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/es.quatroges.qMGTickets";
		File dir = new File(path);
		if (!dir.exists() || !dir.isDirectory()) {
			dir.mkdirs();
		}
		File f = new File(path + "/" + file + ".png");
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
			f.createNewFile();
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());
			fo.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// funciones para volver a buscar el dispositivo//
	public static final void buscarBT() {
		new ClaseBluetooth().detenerTimer();

		ClaseUtils.AvisoToast.mostrarAviso("BlueTooth",context.getResources().getText(R.string.strBTImpresoraBuscando).toString(),context,1000);

		// Register for broadcasts when a device is discovered
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		context.registerReceiver(mReceiver, filter);

		// Register for broadcasts when discovery has finished
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		context.registerReceiver(mReceiver, filter);
		iniciaBlueToothAdapter(false);
		initDeviceList();
		doDiscovery();

	}

	private static void initDeviceList() {

		// Get a set of currently paired devices
		Set<BluetoothDevice> pairedDevices = null;

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R && ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
			ClaseUtils.Aviso.mostrarAviso("Aviso...",context.getResources().getText(R.string.strConsultaAvisoPermisoBluetooth).toString(),context);
		} else {
			pairedDevices = getBlueToothAdapter().getBondedDevices();
		}


		mPairedDevicesArrayAdapter = new ArrayList<String>();
		mPairedDevicesArrayAdapter.clear();
		mNewDevicesArrayAdapter = new ArrayList<String>();
		mNewDevicesArrayAdapter.clear();

		// If there are paired devices, add each one to the ArrayAdapter
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				mPairedDevicesArrayAdapter.add(device.getAddress());
			}

		}

	}

	private static void doDiscovery() {
		addLog("doDiscovery()");


		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R && ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
			ClaseUtils.Aviso.mostrarAviso("Aviso...",context.getResources().getText(R.string.strConsultaAvisoPermisoBluetooth).toString(),context);
			return;
		}

		if (getBlueToothAdapter().isDiscovering()) {
			getBlueToothAdapter().cancelDiscovery();
		}

		getBlueToothAdapter().startDiscovery();

		addLog("Discovery() started");
		if (mPairedDevicesArrayAdapter.contains(_remoteDevice)) {
			stopDiscovery(context.getResources().getText(R.string.strBTImpresoraEncontrada).toString());

			new ClaseBluetooth().enableTimer(10000, true);
		}
	}


	private static void stopDiscovery(String texto) {

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R && ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
			ClaseUtils.Aviso.mostrarAviso("Aviso...",context.getResources().getText(R.string.strConsultaAvisoPermisoBluetooth).toString(),context);
			return;
		}


		if (getBlueToothAdapter() != null) {
			if (getBlueToothAdapter().isDiscovering()) {
				addLog("stop discovery requested");
				getBlueToothAdapter().cancelDiscovery();
			}
		}
		context.unregisterReceiver(mReceiver);

		ClaseUtils.AvisoToast.mostrarAviso("Aviso",texto,context,1000);

		new ClaseBluetooth().enableTimer(10000, true);
	}

	private static final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
				ClaseUtils.Aviso.mostrarAviso("Aviso...",context.getResources().getText(R.string.strConsultaAvisoPermisoBluetooth).toString(),context);
				return;
			}



			String action = intent.getAction();
			addLog("Discovery BroadcastReceiver()");

			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				addLog("ACTION_FOUND");
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				// If it's already paired, skip it, because it's been listed already
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					addLog("adding new bonded device: " + device.getName() + " " + device.getAddress());
					mNewDevicesArrayAdapter.add(device.getAddress());

					if (mNewDevicesArrayAdapter.contains(_remoteDevice)) {
						stopDiscovery(context.getResources().getText(R.string.strBTImpresoraNoEncontrada).toString());
					}
				}
				// When discovery is finished, change the Activity title
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				stopDiscovery(context.getResources().getText(R.string.strBTImpresoraNoEncontrada).toString());

				//setProgressBarIndeterminateVisibility(false);
				//setTitle(R.string.strBTSelectDevice);
				if (mNewDevicesArrayAdapter.size() == 0) {
					String noDevices = context.getResources().getText(R.string.strBTNoEncontrados).toString();
					mNewDevicesArrayAdapter.add(noDevices);
				}

			}
		}
	};

	// brodcastr receiber bluetooth
	private static final BroadcastReceiver mBTReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			BluetoothDevice tmp = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			Log.d(TAG,"hay extras" + tmp.getAddress());

			if (tmp.getAddress().equalsIgnoreCase( _remoteDevice )) {

				if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
					Log.d(TAG, "BT conectado");
					setPrinterStatus(ClaseBluetoothPrintConstantes.STATE_CONNECTED);

				} else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
					Log.d(TAG, "BT desconectado");
					if (!_noDesconectarBT)
						setPrinterStatus(ClaseBluetoothPrintConstantes.STATE_DISCONNECTED);


				} else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
					Log.d(TAG, "BT pide desconectar");
					if (!_noDesconectarBT)
						setPrinterStatus(ClaseBluetoothPrintConstantes.STATE_DISCONNECTED);

				} else {
					Log.d(TAG, "BT pide otra cosa");

				}
			}

			_noDesconectarBT = false;
		}


	};

	public static void registraBTReceiver(Context applicationContext){
		Log.d(TAG,"BT registrar BTBroadcastReceiver");

			IntentFilter filter = new IntentFilter();
			filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
			filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
			filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);

			try {
				applicationContext.registerReceiver(mBTReceiver, filter);
				Log.d(TAG,"BT registrar BTBroadcastReceiver. Registrado");

			} catch (IllegalArgumentException e) {
				Log.d(TAG,"BT registrar BTBroadcastReceiver. Excepcion...");
				Log.d(TAG, e.toString());
			}

			_btBroadcasReceiverRegistrado = true;

	}

	public static  void desRegistraBTReceiver(Context applicationContext){
		try {
			if (! _btBroadcasReceiverRegistrado){
				Log.d(TAG, "BT desregistrar BTBroadcastReceiver. No esta registrado");
			}
			else {
				Log.d(TAG, "BT desregistrar BTBroadcastReceiver");
				applicationContext.unregisterReceiver(mBTReceiver);
			}
		} catch (IllegalArgumentException e) {
			Log.d(TAG,"BT desregistrar BTBroadcastReceiver. Excepcion....");
			Log.d(TAG, e.toString());

		}
		_btBroadcasReceiverRegistrado = false;

	}

	private static void buscaCaracteresControl(StringBuffer sb, boolean vistaPrevia) {
		if (vistaPrevia) {
			EncodeUtils.sbReplace(sb, "<b>", "");
			EncodeUtils.sbReplace(sb, "</b>", "");
			EncodeUtils.sbReplace(sb, "<u>", "");
			EncodeUtils.sbReplace(sb, "</u>", "");
			EncodeUtils.sbReplace(sb, "<sw>", "");
			EncodeUtils.sbReplace(sb, "<swb>", "");
			EncodeUtils.sbReplace(sb, "<sh>", "");
			EncodeUtils.sbReplace(sb, "<swh>", "");
			EncodeUtils.sbReplace(sb, "<sn>", "");
			EncodeUtils.sbReplace(sb, "</sn>", "");
			EncodeUtils.sbReplace(sb, "<snb>", "");
			EncodeUtils.sbReplace(sb, "</snb>", "");
			EncodeUtils.sbReplace(sb, "<br>", "");
			EncodeUtils.sbReplace(sb, "</br>", "");
			EncodeUtils.sbReplace(sb, "<sf>", "");
			EncodeUtils.sbReplace(sb, "</sf>", "");
			EncodeUtils.sbReplace(sb, "<sfb>", "");
			EncodeUtils.sbReplace(sb, "</sfb>", "");
			return;
		}
		switch (_modelo) {
			case ClaseBluetoothPrintConstantes.MODEL_SM5802:
				//busca caracteres control
				EncodeUtils.sbReplace(sb, "<b>", CMD_NEGRITA_E);
				EncodeUtils.sbReplace(sb, "</b>", CMD_NEGRITA_D);
				EncodeUtils.sbReplace(sb, "<u>", CMD_UNDERLINE_E);
				EncodeUtils.sbReplace(sb, "</u>", CMD_UNDERLINE_D);
				EncodeUtils.sbReplace(sb, "<sw>", CMD_SIZEDOBLEWIDTH);
				EncodeUtils.sbReplace(sb, "<sh>", CMD_SIZEDOBLEHEIGHT);
				EncodeUtils.sbReplace(sb, "<swh>", CMD_SIZEDOBLEW_H);
				EncodeUtils.sbReplace(sb, "<sn>", CMD_SIZENORMAL);
				//EncodeUtils.sbReplace(sb, "<br>", CMD_BLACKREVERSE_E);
				//EncodeUtils.sbReplace(sb, "</br>", CMD_BLACKREVERSE_D);
				EncodeUtils.sbReplace(sb, "<br>", (Character.toString((char)10)));

				EncodeUtils.sbReplace(sb, "<sf>", CMD_SMALLFONT_E);
				EncodeUtils.sbReplace(sb, "</sf>", CMD_SMALLFONT_D);
				break;


			case ClaseBluetoothPrintConstantes.MODEL_SCREEN:
				//busca caracteres control
				EncodeUtils.sbReplace(sb, "<b>", "");
				EncodeUtils.sbReplace(sb, "</b>", "");
				EncodeUtils.sbReplace(sb, "<u>", "");
				EncodeUtils.sbReplace(sb, "</u>", "");
				EncodeUtils.sbReplace(sb, "<sw>", "");
				EncodeUtils.sbReplace(sb, "<sh>", "");
				EncodeUtils.sbReplace(sb, "<swh>", "");
				EncodeUtils.sbReplace(sb, "<sn>", "");
				EncodeUtils.sbReplace(sb, "<br>", "");
				EncodeUtils.sbReplace(sb, "</br>", "");
				EncodeUtils.sbReplace(sb, "<sf>", "");
				EncodeUtils.sbReplace(sb, "</sf>", "");
				break;
		}


	}

	private static void limpiaCaracteres(StringBuffer sb) {
		EncodeUtils.sbReplace(sb, "á", "a");
		EncodeUtils.sbReplace(sb, "é", "e");
		EncodeUtils.sbReplace(sb, "í", "i");
		EncodeUtils.sbReplace(sb, "ó", "o");
		EncodeUtils.sbReplace(sb, "ú", "u");

		EncodeUtils.sbReplace(sb, "Á", "A");
		EncodeUtils.sbReplace(sb, "É", "E");
		EncodeUtils.sbReplace(sb, "Í", "I");
		EncodeUtils.sbReplace(sb, "Ó", "O");
		EncodeUtils.sbReplace(sb, "Ú", "U");

		EncodeUtils.sbReplace(sb, "ä", "a");
		EncodeUtils.sbReplace(sb, "ë", "e");
		EncodeUtils.sbReplace(sb, "ï", "i");
		EncodeUtils.sbReplace(sb, "ö", "o");
		EncodeUtils.sbReplace(sb, "ü", "u");

		EncodeUtils.sbReplace(sb, "Á", "A");
		EncodeUtils.sbReplace(sb, "Ë", "E");
		EncodeUtils.sbReplace(sb, "Ï", "I");
		EncodeUtils.sbReplace(sb, "Ö", "O");
		EncodeUtils.sbReplace(sb, "Ü", "U");

		EncodeUtils.sbReplace(sb, "ñ", "n");
		EncodeUtils.sbReplace(sb, "Ñ", "N");


	}



	/*
	private static  void condicionaCampos(StringBuffer sb, ItemDatosTiquet datos, String ventaOrigenPrecio) {
		if (datos.isCopiaLiquidacion())
			EncodeUtils.sbReplace(sb, "{*original}", "");
		else
			EncodeUtils.sbReplace(sb, "{*copia}", "");


		if (ventaOrigenPrecio.toLowerCase().trim().equals("s") || datos.getVentaOrigen().trim().toLowerCase().equals("n") ) {
			if (datos.getFormapago().equalsIgnoreCase("E"))
				EncodeUtils.sbReplace(sb, "{*tarjeta}", "");
			else
				EncodeUtils.sbReplace(sb, "{*efectivo}", "");
		}
		else {
			EncodeUtils.sbReplace(sb, "{*tarjeta}", "");
			EncodeUtils.sbReplace(sb, "{*efectivo}", "");
		}

		if (datos.getCodigoTiquet().getContpaquete() == 0) {
			EncodeUtils.sbReplace(sb, "{*paquete}\r\n", "");
			EncodeUtils.sbReplace(sb, "{*paquete}", "");
		}

		if (datos.getOferta().trim().equalsIgnoreCase("")) {
			EncodeUtils.sbReplace(sb, "{*oferta}\r\n", "");
			EncodeUtils.sbReplace(sb, "{*oferta}", "");
		}

		if (!datos.isAnulado()) {
			EncodeUtils.sbReplace(sb, "{*anulado}\r\n", "");
			EncodeUtils.sbReplace(sb, "{*anulado}", "");
		}

		if (!datos.isSinFecha())
			EncodeUtils.sbReplace(sb, "{*fechaventa}", "");
		else
			EncodeUtils.sbReplace(sb, "{*fechaexcursion}", "");


		if (!datos.isSinTraslado()) {
			EncodeUtils.sbReplace(sb, "{*sintraslado}", "");
			EncodeUtils.sbReplace(sb, "{*puntoencuentro}", "");
		} else
			EncodeUtils.sbReplace(sb, "{*puntorecogida}", "");

		if (!datos.isSinFecha()) EncodeUtils.sbReplace(sb, "{*sinfecha}", "");

		if (datos.getPagoBus().equalsIgnoreCase("N")) EncodeUtils.sbReplace(sb, "{*pagobus}", "");



		if (Integer.valueOf(datos.getUdAd1()) == 0 && ! datos.getdPreAd1().trim().contains("[PF]") ) {
			EncodeUtils.sbReplace(sb, "{*linea1}{*linea1d}\r\n", "");
			EncodeUtils.sbReplace(sb, "{*linea1}\r\n", "");
		}
		if (Integer.valueOf(datos.getUdAd2()) == 0 &&  ! datos.getdPreAd2().trim().contains("[PF]") ){
			EncodeUtils.sbReplace(sb, "{*linea2}{*linea2d}\r\n", "");
			EncodeUtils.sbReplace(sb, "{*linea2}\r\n", "");
		}
		if (Integer.valueOf(datos.getUdNin1()) == 0 && ! datos.getdPreNin1().trim().contains("[PF]") ){
			EncodeUtils.sbReplace(sb, "{*linea3}{*linea3d}\r\n", "");
			EncodeUtils.sbReplace(sb, "{*linea3}\r\n", "");
		}
		if (Integer.valueOf(datos.getUdNin2()) == 0 && ! datos.getdPreNin2().trim().contains("[PF]") ) {
			EncodeUtils.sbReplace(sb, "{*linea4}{*linea4d}\r\n", "");
			EncodeUtils.sbReplace(sb, "{*linea4}\r\n", "");
		}

		if (Integer.valueOf(datos.getBebes()) == 0) EncodeUtils.sbReplace(sb, "{*linea5}\r\n", "");

		if (datos.getdPreAd1().trim().equalsIgnoreCase(""))EncodeUtils.sbReplace(sb, "{*linea1d}", "");
		if (datos.getdPreAd2().trim().equalsIgnoreCase(""))EncodeUtils.sbReplace(sb, "{*linea2d}", "");
		if (datos.getdPreNin1().trim().equalsIgnoreCase(""))EncodeUtils.sbReplace(sb, "{*linea3d}", "");
		if (datos.getdPreNin2().trim().equalsIgnoreCase(""))EncodeUtils.sbReplace(sb, "{*linea4d}", "");

	}

	private static  void condicionaCamposTPV(StringBuffer sb, Respuesta.DatosVenta datosVenta, boolean original, int tipo) {

		if (tipo == RECIBO_VENTA) {
			EncodeUtils.sbReplace(sb,"{!ticket}","");
		}

		if (! datosVenta.tipoLectura.trim().equalsIgnoreCase("4") && ! datosVenta.tipoLectura.trim().equalsIgnoreCase("5")) {
			EncodeUtils.sbReplace(sb,"logonfc","");
		}
		//cvm: 1-> operacion con PIN. no pedir firma
		//cvm: 2-> operacion contacless. no pedir firma
		//cvm: 3-> firma capturada por el pinpad. no pedir firma
		//cvm: 4->  pedir firma
		if (datosVenta.cvm.equalsIgnoreCase("1")){   //operacion pin. no firma
			if (! original)
				EncodeUtils.sbReplace(sb,"{!operacion_pin}","");

			EncodeUtils.sbReplace(sb,"{!operacion_cless}","");
			EncodeUtils.sbReplace(sb,"{!operacion_firma_pinpad}","");
			EncodeUtils.sbReplace(sb,"{!firma}","");
			EncodeUtils.sbReplace(sb,"[campofirma]","");
		}
		else if (datosVenta.cvm.equalsIgnoreCase("2")){	//operacion contacless. no firma
			if (! original)
				EncodeUtils.sbReplace(sb,"{!operacion_cless}","");

			EncodeUtils.sbReplace(sb,"{!operacion_pin}","");
			EncodeUtils.sbReplace(sb,"{!operacion_firma_pinpad}","");
			EncodeUtils.sbReplace(sb,"{!firma}","");
			EncodeUtils.sbReplace(sb,"[campofirma]","");
		}
		else if (datosVenta.cvm.equalsIgnoreCase("3")){	// operación firmada en pinpad
			if (! original)
				EncodeUtils.sbReplace(sb,"{!operacion_firma_pinpad}","");

			EncodeUtils.sbReplace(sb,"{!operacion_cless}","");
			EncodeUtils.sbReplace(sb,"{!operacion_pin}","");
			EncodeUtils.sbReplace(sb,"{!firma}","");
			EncodeUtils.sbReplace(sb,"[campofirma]","");
		}
		else if(datosVenta.cvm.equalsIgnoreCase("4")){ // es necesario firmar recibo
			EncodeUtils.sbReplace(sb,"{!operacion_firma_pinpad}","");
			EncodeUtils.sbReplace(sb,"{!operacion_cless}","");
			EncodeUtils.sbReplace(sb,"{!operacion_pin}","");

			if (! original) {
				EncodeUtils.sbReplace(sb,"{!firma}","");
				EncodeUtils.sbReplace(sb,"[campofirma]","");
			}
		}
		else  {
			EncodeUtils.sbReplace(sb,"{!operacion_cless}","");
			EncodeUtils.sbReplace(sb,"{!operacion_pin}","");
			EncodeUtils.sbReplace(sb,"{!operacion_firma_pinpad}","");
			EncodeUtils.sbReplace(sb,"{!firma}","");
			EncodeUtils.sbReplace(sb,"[campofirma]","");
		}

		if (original){
			EncodeUtils.sbReplace(sb,"{!recibo_cliente}","");
		}
		else {
			EncodeUtils.sbReplace(sb,"{!recibo_estable}","");
		}

		if (datosVenta.red == null || datosVenta.red.trim().equalsIgnoreCase("")){
			EncodeUtils.sbReplace(sb,"{!red}","");

		}
	}



	private static void reemplazaCampos(StringBuffer sb, HashMap<String, String> datos) {

		for (Map.Entry<String, String> dato : datos.entrySet()) {
			if (dato.getKey().startsWith("*")) {
				EncodeUtils.sbReplace(sb, "{" + dato.getKey() + "}", dato.getValue());
			}
		}

		for (Map.Entry<String, String> dato : datos.entrySet()) {
			if (dato.getKey().startsWith("!")) {
				if (dato.getKey().startsWith("!adultos") || dato.getKey().startsWith("!ninos") || dato.getKey().startsWith("!bebes")) {

					String tmp = EncodeUtils.strPadRight(dato.getValue(), 11 , "#");
					EncodeUtils.sbReplace(sb, "{" + dato.getKey() + "}", tmp);
				}
				else {
					EncodeUtils.sbReplace(sb, "{" + dato.getKey() + "}", dato.getValue());
				}
			}
		}

	}

	private static void reemplazaCamposVenta(StringBuffer sb, ItemDatosTiquet datos, HashMap<String, String> datosPlantilla, String ventaOrigenPrecio) {

		String[] diasSemana ="Mon,Tue,Wed,Thu,Fri,Sat,Sun".split(",");
		try {
			diasSemana = datosPlantilla.get("!diassemana").split(",");
		}
		catch (Exception e) {}

		String delimIni = "";
		String delimFin = "";

		EncodeUtils.sbReplace(sb, "[adultos1]", datos.getUdAd1());
		EncodeUtils.sbReplace(sb, "[adultos2]", datos.getUdAd2());
		EncodeUtils.sbReplace(sb, "[ninos1]", datos.getUdNin1());
		EncodeUtils.sbReplace(sb, "[ninos2]", datos.getUdNin2());
		EncodeUtils.sbReplace(sb, "[bebes]", datos.getBebes());

		if (ventaOrigenPrecio.toLowerCase().trim().equals("s") || datos.getVentaOrigen().trim().toLowerCase().equals("n") ) {
			EncodeUtils.sbReplace(sb, "[precioadulto1]", EncodeUtils.strPadLeft(datos.getPreAd1(), 7, "#"));
			EncodeUtils.sbReplace(sb, "[precioadulto2]", EncodeUtils.strPadLeft(datos.getPreAd2(), 7, "#"));
			EncodeUtils.sbReplace(sb, "[precionino1]", EncodeUtils.strPadLeft(datos.getPreNin1(), 7, "#"));
			EncodeUtils.sbReplace(sb, "[precionino2]", EncodeUtils.strPadLeft(datos.getPreNin2(), 7, "#"));

			EncodeUtils.sbReplace(sb, "[totaladulto1]", EncodeUtils.strPadLeft(datos.getTotAd1(), 7, "#"));
			EncodeUtils.sbReplace(sb, "[totaladulto2]", EncodeUtils.strPadLeft(datos.getTotAd2(), 7, "#"));
			EncodeUtils.sbReplace(sb, "[totalnino1]", EncodeUtils.strPadLeft(datos.getTotNin1(), 7, "#"));
			EncodeUtils.sbReplace(sb, "[totalnino2]", EncodeUtils.strPadLeft(datos.getTotNin2(), 7, "#"));

			EncodeUtils.sbReplace(sb, "[total]", EncodeUtils.strPadLeft(datos.getTotal(), 7, "#"));

		}
		else {
			EncodeUtils.sbReplace(sb, "[precioadulto1]", EncodeUtils.strPadLeft("", 7, "#"));
			EncodeUtils.sbReplace(sb, "[precioadulto2]", EncodeUtils.strPadLeft("", 7, "#"));
			EncodeUtils.sbReplace(sb, "[precionino1]", EncodeUtils.strPadLeft("", 7, "#"));
			EncodeUtils.sbReplace(sb, "[precionino2]", EncodeUtils.strPadLeft("", 7, "#"));

			EncodeUtils.sbReplace(sb, "[totaladulto1]", EncodeUtils.strPadLeft("", 7, "#"));
			EncodeUtils.sbReplace(sb, "[totaladulto2]", EncodeUtils.strPadLeft("", 7, "#"));
			EncodeUtils.sbReplace(sb, "[totalnino1]", EncodeUtils.strPadLeft("", 7, "#"));
			EncodeUtils.sbReplace(sb, "[totalnino2]", EncodeUtils.strPadLeft("", 7, "#"));

			EncodeUtils.sbReplace(sb, "[total]", EncodeUtils.strPadLeft("", 7, "#"));

		}



		if (datos.getdPreAd1().contains("[PF]")) {
			delimIni = "<b>";
			delimFin = "</b>";
		}
		EncodeUtils.sbReplace(sb, "[descprecioadulto1]", delimIni + datos.getdPreAd1()+ delimFin);

		delimIni = "";
		delimFin = "";
		if (datos.getdPreAd1().contains("[PF]")) {
			delimIni = "<b>";
			delimFin = "</b>";
		}
		EncodeUtils.sbReplace(sb, "[descprecioadulto2]", delimIni + datos.getdPreAd2()+ delimFin);

		delimIni = "";
		delimFin = "";
		if (datos.getdPreAd1().contains("[PF]")) {
			delimIni = "<b>";
			delimFin = "</b>";
		}
		EncodeUtils.sbReplace(sb, "[descprecionino1]", delimIni + datos.getdPreNin1()+ delimFin);

		delimIni = "";
		delimFin = "";
		if (datos.getdPreAd1().contains("[PF]")) {
			delimIni = "<b>";
			delimFin = "</b>";
		}
		EncodeUtils.sbReplace(sb, "[descprecionino2]", delimIni + datos.getdPreNin2()+ delimFin);


		EncodeUtils.sbReplace(sb, "[nombrecliente]", datos.getNombreCliente());
		EncodeUtils.sbReplace(sb, "[nombrehotel]", datos.getHotel());
		EncodeUtils.sbReplace(sb, "[habitacion]", datos.getHabitacion());
		EncodeUtils.sbReplace(sb, "[nombrepunto]", datos.getPuntoRecogida());
		EncodeUtils.sbReplace(sb, "[hora]", datos.getHoraRecogida());
		EncodeUtils.sbReplace(sb, "[observaciones]", datos.getObservaciones());
		EncodeUtils.sbReplace(sb, "[idioma]", datos.getIdioma());
		EncodeUtils.sbReplace(sb, "[bonoproveedor]", datos.getBonoPro());


		EncodeUtils.sbReplace(sb, "[fechaimpresion]", DateUtils.now("dd/MM/yyyy HH:mm:ss"));
		EncodeUtils.sbReplace(sb, "[ttoo]", datos.getTtoo());
		EncodeUtils.sbReplace(sb, "[guia]", datos.getGuia());
		String fecha = DateUtils.cambiaFormato(datos.getFecha(), "yyyy-MM-dd", "dd/MM/yyyy");
		EncodeUtils.sbReplace(sb, "[fechaexcursion]", fecha);
		EncodeUtils.sbReplace(sb, "[horaexcursion]", datos.getHora_exc());


		EncodeUtils.sbReplace(sb, "[diasem_exc]", DateUtils.DiaSemanaStr(fecha, context, diasSemana));

		fecha = DateUtils.cambiaFormato(datos.getFechaVenta(), "yyyy-MM-dd", "dd/MM/yyyy");
		EncodeUtils.sbReplace(sb, "[fechaventa]", fecha);
		EncodeUtils.sbReplace(sb, "[diasem_venta]", DateUtils.DiaSemanaStr(fecha, context, diasSemana));

		EncodeUtils.sbReplace(sb, "[codigoexcursion]", datos.getCodexc());
		EncodeUtils.sbReplace(sb, "[nombreexcursion]", datos.getExcursion());
		EncodeUtils.sbReplace(sb, "[codigopaquete]", String.valueOf(datos.getCodigoTiquet().getCodpaquete()));
		EncodeUtils.sbReplace(sb, "[nombrepaquete]", datos.getCodigoTiquet().getNombrepaquete());


		String tmpCadena = datos.getCodigoTiquet().getCodigo();
		if (datos.getCodigoTiquet().getContpaquete() > 0) {
			tmpCadena += "/" + String.valueOf(datos.getCodigoTiquet().getContpaquete());
		}
		EncodeUtils.sbReplace(sb, "[ticket]", tmpCadena);
		EncodeUtils.sbReplace(sb, "[venta]",datos.getCodigoTiquet().getGrupoVenta());

		EncodeUtils.sbReplace(sb, "[oferta]", datos.getOferta());

		EncodeUtils.sbReplace(sb,"[ST]","");
		EncodeUtils.sbReplace(sb,"[SF]","");
		EncodeUtils.sbReplace(sb,"[PF]","");

	}

*/




	private static StringBuffer imprimeDatos(StringBuffer sb, boolean vistaPrevia){
		StringBuffer sbPreview = new StringBuffer();
		sbPreview.append("");
		if (getBTdriverSCREEN() != null) {
			getBTdriverSCREEN().setBufferTexto("");
		}

		String a= "";
		int modelo = ClaseBluetooth.get_modelo();

		Node node = null;
		int fuente = FUENTE_NORMAL;
		int estilo = ESTILO_NORMAL;
		int align = 0; // left
		String tag_node1 = "";
		String texto = "";

		Document doc = Jsoup.parse(sb.toString());
		Element element = doc.getElementsByTag("body").first();
		for ( Node tnode : element.childNodes() ) {
			try {
				node = tnode;
				a = node.toString();
				tag_node1 = ((Element) node).tagName();
				texto =((Element) node).text();
				align = ALIGN_LEFT;

				if ( ((Element) node).attributes().hasKey("align") ){
					if ( ((Element) node).attributes().get("align").equalsIgnoreCase("center"))
						align = ALIGN_CENTER;
					if ( ((Element) node).attributes().get("align").equalsIgnoreCase("right"))
						align = ALIGN_RIGTH;
				}

				if (a.trim().length() > 0 &&
						( ((Element) node).text().trim().length() > 0 ||a.trim().toLowerCase().contains("br")  ) ){

					switch ( tag_node1){
						case "sn":
							fuente = FUENTE_NORMAL;
							estilo = ESTILO_NORMAL;
							break;
						case "b":
							fuente = FUENTE_NORMAL;
							estilo = ESTILO_BOLD;
							break;
						case "snb":
							fuente = FUENTE_NORMAL;
							estilo = ESTILO_BOLD;
							break;
						case "sf":
							fuente = FUENTE_SMALL;
							estilo = ESTILO_NORMAL;
							break;
						case "sfb":
							fuente = FUENTE_SMALL;
							estilo = ESTILO_BOLD;
							break;
						case "sw":
							fuente = FUENTE_BIG;
							estilo = ESTILO_NORMAL;
							break;
						case "swb":
							fuente = FUENTE_BIG;
							estilo = ESTILO_BOLD;
							break;

					}

					if (!vistaPrevia && modelo != MODEL_SCREEN ) {
						texto = texto.replace("\\n", Character.toString((char) 10)).replace("#", " ");
					}
					else {
						 texto = texto.replace("\\n", Character.toString((char) 10)).replace("#", "&nbsp");

					}

					if (tag_node1.equalsIgnoreCase("br")){
						if (! vistaPrevia)imprimeSaltoLinea(1,false);
						//sbPreview.append(hmtlSaltoLinea(1)); //si es vista previa no hay que añadir <br>
					}
					else {
						if (! vistaPrevia)  imprimeArrayByte(texto.getBytes(), new StringBuffer().append(texto), fuente, estilo, align);
						sbPreview.append(htmlArrayByte(texto.getBytes(), new StringBuffer().append(texto), fuente, estilo, align, modelo));
					}

				}
			}
			catch (Exception e){
				if (a.trim().length() > 0 ){
					a = a.replace("\n", "").replace("\r", "").trim();
					a = a.replace("#", " ");
					if (! vistaPrevia)  imprimeArrayByte(a.getBytes(),new StringBuffer().append(a),FUENTE_NORMAL,ESTILO_NORMAL, 0);
					sbPreview.append(a+"\r\n");
				}
			}
		}
		if (!vistaPrevia) {
			sbPreview = new StringBuffer();
			sbPreview.append("");
		}
			return sbPreview;

	}


}



