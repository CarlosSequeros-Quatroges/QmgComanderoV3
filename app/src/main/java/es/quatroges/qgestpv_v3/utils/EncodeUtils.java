package es.quatroges.qgestpv_v3.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlos on 06/09/2015.
 */
public class EncodeUtils {

    private static final String TAG = "EncodeUtils";
    private static  final String patron = "7W6G8SLBX9K5RVFJDYUA4MTQ3ENZCH2P";
    private static final String MD5 = "MD5";

    public static String getExternalPath(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File d = null;

            try {
                d = context.getExternalFilesDir(null);
                return d.getAbsolutePath();
            }
            catch ( Exception e1) {
            }

        }

        return Environment.getExternalStorageDirectory().getAbsolutePath()+"/es.quatroges.qMGTickets";


    }

    public static String md5(final String s) {

        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String int2base32(int numero){

        String binNumero = Integer.toBinaryString(numero);

        Log.e(TAG, "binNUmero: " + binNumero);

        while (binNumero.length() %5 != 0)  binNumero = "0"+binNumero;

        Log.e(TAG,"binNUmero: "+ binNumero);

        String grupos[] = new String[binNumero.length()/5];

        String resultado = "";

        for (int t = 0 ; t< grupos.length ; t++) {
            grupos[t] = binNumero.substring(t*5,t*5+5);
            int posicion = Integer.parseInt("0"+grupos[t],2);
            resultado += patron.substring(posicion,posicion+1);

            Log.e(TAG,"grupo" +t+": "+grupos[t]);
            Log.e(TAG,"Resultado:"+resultado);
        }


        return resultado;
        //return String.valueOf(numero);
    }

    public static String long2base32(long numero){

        String binNumero = Long.toBinaryString(numero);

        Log.e(TAG, "binNUmero: " + binNumero);

        while (binNumero.length() %5 != 0)  binNumero = "0"+binNumero;

        Log.e(TAG,"binNUmero: "+ binNumero);

        String grupos[] = new String[binNumero.length()/5];

        String resultado = "";

        for (int t = 0 ; t< grupos.length ; t++) {
            grupos[t] = binNumero.substring(t*5,t*5+5);
            int posicion = Integer.parseInt("0"+grupos[t],2);
            resultado += patron.substring(posicion,posicion+1);

            Log.e(TAG,"grupo" +t+": "+grupos[t]);
            Log.e(TAG,"Resultado:"+resultado);
        }


        return resultado;
    }

    public static String base64tostr(String strb64){
        // Receiving side
        byte[] data = Base64.decode(strb64, Base64.DEFAULT);
        String txt = new String(data);
        //String text = new String(data, "UTF-8");

        return txt;
    }

    public static void sbReplace(StringBuffer cadena,String patron,String replace){
        int pos = 0;
        try{
           do {
               pos = cadena.indexOf(patron,pos);
               if (pos != -1) {
                   cadena.replace(pos, pos + patron.length(), replace);
                   pos++;
               }
           } while (pos != -1 && pos < cadena.length());
       }
       catch (Exception e){}

    }

    public static String  strReplace(String cadena,String patron,String replace){
        int pos = 0;
        StringBuffer sb = new StringBuffer();
        sb.append(cadena);
        try{
            do {
                pos = sb.indexOf(patron,pos);
                if (pos != -1) {
                    sb.replace(pos, pos + patron.length(), replace);
                    pos++;
                }
            } while (pos != -1 && pos < sb.length());
            return sb.toString();
        }
        catch (Exception e){
            return cadena;
        }

    }

    public static String strPadLeft(String cadena, int longitud, String  c){
        String tmpCadena = cadena;
        while (tmpCadena.length()< longitud){
            tmpCadena = c + tmpCadena;
        }
        return tmpCadena;
    }

    public static String strPadRight(String cadena, int longitud, String  c){
        String tmpCadena = cadena;
        while (tmpCadena.length()< longitud){
            tmpCadena = tmpCadena+c;
        }
        return tmpCadena;
    }

    public static String replaceSpace(String cadena){
        do {
        int posIni;
        int posFin= 0;
            posIni = cadena.indexOf("[sp", posFin);
            if (posIni == -1) break;
            posFin = cadena.indexOf("]",posIni);
            int espacios =Integer.parseInt(cadena.substring(posIni+3,posFin));
            String tmp = cadena.substring(0,posIni);
            for (int t = 0; t < espacios; t++) {
                tmp += " ";
            }
            tmp += cadena.substring(posFin+1);
            cadena = tmp;
        } while (true);
        return cadena;
    }

    public static String arrayList2string(ArrayList<String> array) {
        String cadena = "";
        if (array.size()> 0) {

            for (String cod : array)
            {
                cadena += cod + "','";
            }
            cadena = "'" +cadena.substring(0,cadena.length()-3)+"'";
        }
        return cadena;
    }

    public static String list2string(List<String> array) {
        String cadena = "";
        if (array.size()> 0) {

            for (String cod : array)
            {
                cadena += cod + "','";
            }
            cadena = "'" +cadena.substring(0,cadena.length()-3)+"'";
        }
        return cadena;
    }

}
