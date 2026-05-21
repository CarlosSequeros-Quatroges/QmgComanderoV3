package es.quatroges.qgestpv_v3.configuracion;

import android.app.Activity;
import android.content.Context;

import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public class ClaseConfiguracion {

    //parametros
    public boolean demo;
    public String empresa;
    public String codigo;
    public boolean cargaLineasVenta;
    public boolean claveCamareros;
    public boolean pax;
    public int timerFragment;
    public boolean pagoEfectivo;
    public boolean pagoTarjeta;
    public boolean pagoCuentacasa;
    public boolean pagoCredito;
    public boolean pagoEfeTar;
    public boolean pagoPension;
    public boolean firma;
    public boolean mantenerVenta;
    public boolean mostrarProductosAuto;
    public boolean mantenerProductos;
    public boolean compacto;

    //servicio web
    public String url;
    public String api;
    public String deviceID;
    public int connectionTO;
    public int  rwTO1;
    public int  rwTO2;
    public int rwTO4;
    public int rwTO3;
    public int rwTO5;

    //BASE DE DATOS
    public boolean sincronizaAuto;

    //IMPRESORA
    public static String btModelo;
    public static String btMac;
    public static String btName;

    public  boolean comandaapp;
    public  boolean comandatpv;
    public  boolean cuentapteapp;
    public  boolean cuentaptetpv;
    public  boolean cuentaapp;
    public  boolean cuentatpv;


    public ClaseConfiguracion(Activity activity) {
        //parametros
        this.empresa = "Apartamentos Quatroges";
        this.codigo = "001";
        this.cargaLineasVenta = true;
        this.claveCamareros = true;
        this.demo = true;
        this.pax = false;
        this.timerFragment =10;
        this.pagoEfectivo = true;
        this.pagoTarjeta = true;
        this.pagoCuentacasa = false;
        this.pagoCredito = false;
        this.pagoEfeTar = false;
        this.pagoPension = false;
        this.firma = false;
        this.mantenerVenta = false;
        this.mostrarProductosAuto = true;
        this.mantenerProductos = true;
        this.compacto = true;

        //servicio web
        this.url = "http://comandero.quatroges.es:8080/";
        this.api = "mgwres/rest";
        this.deviceID = getDeviceID(activity.getApplicationContext());
        this.connectionTO = 1;
        this.rwTO1 = 1;
        this.rwTO2 = 2;
        this.rwTO3 = 3;
        this.rwTO4 = 4;
        this.rwTO5 = 5;

        //base de datos
        this.sincronizaAuto = true;

        //impresora
        this.btMac = "";
        this.btModelo = "";
        this.btName = "";
        this.comandaapp = false;
        this.comandatpv = true;
        this.cuentapteapp = false;
        this.cuentaptetpv = true;
        this.cuentaapp = false;
        this.cuentatpv = true;

    }

    private String getDeviceID(Context context){
        return ClaseUtils.Installation.id(context);
    }

}
