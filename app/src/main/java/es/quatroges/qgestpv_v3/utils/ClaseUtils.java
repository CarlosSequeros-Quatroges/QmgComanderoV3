package es.quatroges.qgestpv_v3.utils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import es.quatroges.qgestpv_v3.FragmentDialogFacturaCabecera;
import es.quatroges.qgestpv_v3.FragmentDialogFacturaLineas;
import es.quatroges.qgestpv_v3.FragmentDialogExtras;
import es.quatroges.qgestpv_v3.FragmentDialogFiltroAlergenos;
import es.quatroges.qgestpv_v3.FragmentDialogFiltroEtiquetas;
import es.quatroges.qgestpv_v3.FragmentDialogFiltroOrdenPlatos;
import es.quatroges.qgestpv_v3.FragmentDialogNotas;
import es.quatroges.qgestpv_v3.R;
import es.quatroges.qgestpv_v3.adapters.RvAdapterAlergenos;
import es.quatroges.qgestpv_v3.adapters.RvAdapterEtiquetas;
import es.quatroges.qgestpv_v3.adapters.RvAdapterMesas;
import es.quatroges.qgestpv_v3.adapters.RvAdapterResumenComanda;
import es.quatroges.qgestpv_v3.adapters.RvAdapterSubmesas;
import es.quatroges.qgestpv_v3.adapters.RvAdapterSubmesasPax;
import es.quatroges.qgestpv_v3.adapters.VpAdapterTabFactura;
import es.quatroges.qgestpv_v3.adapters.VpAdapterTabFiltros;
import es.quatroges.qgestpv_v3.adapters.VpAdapterTabPideExtras;
import es.quatroges.qgestpv_v3.datos.Hora_Comidas;
import es.quatroges.qgestpv_v3.datos.listas.cabeceras.ClaseCabeceras;
import es.quatroges.qgestpv_v3.datos.listas.facturas.ClaseFacturaCabecera;
import es.quatroges.qgestpv_v3.datos.listas.lineaVentas.ClaseLineaVentas;
import es.quatroges.qgestpv_v3.datos.listas.mesas.ClaseMesas;
import es.quatroges.qgestpv_v3.datos.listas.mesas.ClaseSubMesas;
import es.quatroges.qgestpv_v3.datos.listas.productos.ClaseProductos;

public class ClaseUtils {

    public static final int ORDEN_COMANDA_ENTRANTE = 1;
    public static final int ORDEN_COMANDA_PRIMERO = 2;
    public static final int ORDEN_COMANDA_SEGUNDO= 3;
    public static final int ORDEN_COMANDA_POSTRE= 4;
    public static final int ORDEN_COMANDA_BEBIDA= 0;

    public static enum enModel {
        phone, tab;
    }
    public static enModel modelo;

    public static enum enTipoTiquet {
        cuenta, tiquet, copia_tiquet;
    }
    public static enTipoTiquet tipoTiquet;

    public static enum enEstado {
        transmitida, actualizar, anadir, eliminar, error;
    }



    //region "utilidades"
    public static final String md5(final String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return ""; // Impossibru!
        }
    }

    public static String double2string(double numero, int decimales) {
        return String.format("%."+String.valueOf(decimales)+"f", numero);
    }

    public static String padLeft(String cadena, String caracter, int longitud){
        return String.format("%"+String.valueOf(longitud)+"s",cadena).replace(" ",caracter);
    }
    public static String padRight(String cadena, String caracter, int longitud){
        return String.format("%-"+String.valueOf(longitud)+"s",cadena).replace(" ",caracter);
    }

    public static String maxLength(String cadena, int longitud){
        if (cadena.length() <= longitud) {
            return cadena;
        }
        return cadena.substring(0,longitud-1);
    }



    public static String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";
        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    public static Bitmap escalarBitmap(Bitmap bitmap,int ancho, int alto){
        if (bitmap != null) {
            bitmap= Bitmap.createScaledBitmap(bitmap, ancho, alto, true);
        }
        return bitmap;
    }

    public static Bitmap bitmapToGrayScale(Bitmap bitmap){
       Bitmap bmpGrayscale = bitmap.copy(Bitmap.Config.RGB_565,false);
        return bmpGrayscale;

    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap base64ToBitmap(String bmpB64) {
        byte[] decodeString = Base64.decode(bmpB64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);

        return bitmap;
    }

    public static int px2dp(int px, Activity activity) {
        return (int) (px/activity.getResources().getDisplayMetrics().density);
    }

    //endregion

    //region "utilidades fecha"
    public static final String DATE_FORMAT_NOW = "dd/MM/yyyy";
    public static final String DIA_FORMAT = "dd";
    public static final String MES_FORMAT = "MM";
    public static final String ANO_FORMAT = "yyyy";

    public static String cambiaFormato(String fecha, String formato1, String formato2) {
        SimpleDateFormat sdfFormato1 =  new SimpleDateFormat(formato1, Locale.getDefault());
        SimpleDateFormat sdfFormato2 =  new SimpleDateFormat(formato2,Locale.getDefault());

        Date date= null;
        try {
            date = (Date) sdfFormato1.parse(fecha);
            return sdfFormato2.format(date);
        }
        catch (Exception e){
            return "";
        }

    }

    public static String now(String formato) {

        Calendar cal = Calendar.getInstance();
        String tmpFormato = DATE_FORMAT_NOW;
        if (! formato.equals("")) tmpFormato = formato;

        SimpleDateFormat sdf = new SimpleDateFormat(tmpFormato,Locale.US);
        return sdf.format(cal.getTime());

    }

    //endregion

    //region LATENCIAS


    //endregion

    //region "dialogos"
    public static class PideClave extends AsyncTask<Integer,Void,Void> {

        private static AlertDialog aviso;
        private static Context context;

        private static DialogInterface.OnClickListener onclick;
        private static DialogInterface.OnClickListener onSelect;

        private static String titulo,mensaje, botonTrue,botonFalse;
        private static EditText etClave;
        private static TextView tvCodigo;
        private static String clave;
        private static String codigo;

        public static String getCodigo() {
            return codigo;
        }

        public static void setCodigo(String codigo) {
            PideClave.codigo = codigo;
        }

        public static String getClave() {
            clave = etClave.getText().toString();
            return clave;
        }

        public static void setClave(String clave) {
            PideClave.clave = clave;
        }

        public static void setContext(Context context) {
            PideClave.context = context;
        }

        public static void setTitulo(String titulo) {
            PideClave.titulo = titulo;
        }

        public static void setBotonTrue(String botonTrue) {
            PideClave.botonTrue = botonTrue;
        }

        public static void setBotonFalse(String botonFalse) {
            PideClave.botonFalse = botonFalse;
        }

        public static void setOnclick(DialogInterface.OnClickListener onclick) {
            PideClave.onclick = onclick;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(titulo);


            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_pideclave,null);
            etClave = view.findViewById(R.id.etClave);
            tvCodigo = view.findViewById(R.id.tvCodigo);

            builder.setView(view);
            builder.setPositiveButton(botonTrue, onclick);
            builder.setNegativeButton(botonFalse, onclick);


            etClave.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final boolean focus = hasFocus;
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mostrarTeclado(focus);
                        }
                    }, 300);
                }
            });

            aviso = builder.create();
            aviso.setCancelable(false);
            aviso.setCanceledOnTouchOutside(false);
            aviso.show();
        }

        @Override
        protected void onPostExecute(Void v) {
            clave =etClave.getText().toString().trim();
            super.onPostExecute(v);
        }

        @Override
        protected Void doInBackground(Integer... parametros ) {
            etClave.setText(clave);
            String tCodigo = tvCodigo.getText().toString();
            tCodigo = tCodigo.replace("[codigo]",codigo);
            tvCodigo.setText(tCodigo);

            return null;
        }

        private void mostrarTeclado(boolean mostrar)  {
            if (mostrar){
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etClave, InputMethodManager.SHOW_IMPLICIT);
            }
            else {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etClave.getWindowToken(), 0);

            }
        }
    }

    public static class ProgressDialogo{
        private static ProgressDialog progressDialog;
        private static Thread thread;
        private static boolean stop;

        public static  void mostrarDialogo(boolean mostrar, String titulo, String mensaje, Context context) {
            if (mostrar) {
                if (progressDialog == null)
                    progressDialog = new ProgressDialog(context);
                progressDialog.setMax(100);
                progressDialog.setMessage(mensaje);
                progressDialog.setTitle(titulo);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                stop = false;

                if (thread != null) thread = null;

                thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            do {
                                while (progressDialog.getProgress() <= progressDialog.getMax() && !stop) {
                                    Thread.sleep(200);
                                    handleProgress.sendMessage(handleProgress.obtainMessage());
                                }
                                progressDialog.setProgress(0);
                            }while(! stop);
                            thread = null;

                        }
                        catch(InterruptedException ie){}
                        catch (Exception e){}
                    }
                };

                thread.start();

            }
            else {
                if (progressDialog != null)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 100);
                stop = true;

            }

        }
        public static void cerrarDialogo(){
            if (progressDialog != null)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 100);
            stop = true;
        }

        private static Handler handleProgress = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressDialog.incrementProgressBy(1);
            }
        };
    }

    public static class Aviso {
        private static AlertDialog aviso;

        public static void mostrarAviso(String titulo, String mensaje, Context context) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(titulo);
            builder.setMessage(mensaje);
            builder.setNeutralButton(context.getResources().getString(R.string.strAceptar), null);

            aviso = builder.create();
            aviso.setCancelable(false);
            aviso.setCanceledOnTouchOutside(false);
            aviso.show();

        }

    }

    public static class AvisoToast {
        private static AlertDialog aviso;

        public static void mostrarAviso(String titulo, String mensaje, Context context, int  milis) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(titulo);
            builder.setMessage(mensaje);
            builder.setNeutralButton(context.getResources().getString(R.string.strAceptar), null);

            aviso = builder.create();
            aviso.setCancelable(false);
            aviso.setCanceledOnTouchOutside(false);
            aviso.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (aviso != null) {
                        aviso.dismiss();
                    }
                }
            }, milis);

        }

    }

    public static class AvisoResultado extends AsyncTask<Void,Integer,Void> {

        private static AlertDialog aviso;
        private static Context context;
        private static DialogInterface.OnClickListener onclick;
        private static  String titulo, mensaje, botonTrue,botonFalse;

        public static void setContext(Context context) {
            AvisoResultado.context = context;
        }

        public static void setTitulo(String titulo) {
            AvisoResultado.titulo = titulo;
        }

        public static void setMensaje(String mensaje) {
            AvisoResultado.mensaje = mensaje;
        }

        public static void setBotonTrue(String botonTrue) {
            AvisoResultado.botonTrue = botonTrue;
        }

        public static void setBotonFalse(String botonFalse) {
            AvisoResultado.botonFalse = botonFalse;
        }

        public static void setOnclick(DialogInterface.OnClickListener onclick) {
            AvisoResultado.onclick = onclick;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(titulo);
            builder.setMessage(mensaje);

            if (botonTrue !=null  && botonTrue != null  &&   !botonTrue.equals("") && !botonFalse.equals("")){
                builder.setPositiveButton(botonTrue, onclick);
                builder.setNegativeButton(botonFalse, onclick);
            }
            else {
                String tboton = botonTrue;
                if (tboton.equals("")) {
                    tboton = botonFalse;
                }
                builder.setNeutralButton(tboton, onclick);

            }

            aviso = builder.create();
            aviso.setCancelable(false);
            aviso.setCanceledOnTouchOutside(false);
            aviso.show();
        }
        public static void cerrar() {
            if (aviso != null ){
                aviso.dismiss();
            }
        }

        @Override
        protected void onPostExecute(Void  v) {
            super.onPostExecute(v);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

    public static class PideUnidades extends AsyncTask<Integer,Void,Void> {

        private static AlertDialog aviso;
        private static Context context;

        private static DialogInterface.OnClickListener onclick;

        private static String titulo,mensaje, botonTrue,botonFalse;
        private static EditText etUnidades;
        private static int unidades;
        private static ArrayList<Integer> submesas;
        private static int submesaSel;
        private static RecyclerView rvSubmesas;
        private static RvAdapterSubmesas adapterSubmesas;

        public static int getSubmesaSel() {
            return submesaSel;
        }

        public static void setSubmesaSel(int submesaSel) {
            PideUnidades.submesaSel = submesas.get(submesaSel);
        }

        public static void setSubmesas(ArrayList<Integer> submesas) {
            PideUnidades.submesas = submesas;
        }

        public static int getUnidades() {
            unidades = 0;
            if (etUnidades != null)
                try {
                    unidades = Integer.parseInt(etUnidades.getText().toString().trim());
                }
                catch (Exception e){
                    unidades = 0;
                }

            return unidades;
        }

        public static void setContext(Context context) {
            PideUnidades.context = context;
        }

        public static void setTitulo(String titulo) {
            PideUnidades.titulo = titulo;
        }

        public static void setMensaje(String mensaje) {
            PideUnidades.mensaje = mensaje;
        }

        public static void setBotonTrue(String botonTrue) {
            PideUnidades.botonTrue = botonTrue;
        }

        public static void setBotonFalse(String botonFalse) {
            PideUnidades.botonFalse = botonFalse;
        }

        public static void setOnclick(DialogInterface.OnClickListener onclick) {
            PideUnidades.onclick = onclick;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(titulo);


            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_pideunidades,null);
            etUnidades = view.findViewById(R.id.etUnidades);
            rvSubmesas = view.findViewById(R.id.rvSubmesas);

            View tview  = View.inflate(context,R.layout.submesas_cardview,null);
            tview.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

            RecyclerView rc = view.findViewById(R.id.rvSubmesas);
            rc.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

            float cvWidth = rc.getMeasuredWidth()/ displayMetrics.density;
            float vpWidth = tview.getMeasuredWidth()/displayMetrics.density;
            int cols = (int)( cvWidth /vpWidth);
            if (cols <= 1) cols = 2;


            GridLayoutManager llmMesas = new GridLayoutManager(context,cols, GridLayoutManager.VERTICAL, false);
            rvSubmesas.setLayoutManager(llmMesas);

            if (submesas.size() > 0)submesaSel = submesas.get(0);
            adapterSubmesas = new RvAdapterSubmesas(context, submesas) ;
            rvSubmesas.setAdapter(adapterSubmesas);

            builder.setView(view);


            if (!botonTrue.equals("") && !botonFalse.equals("")){
                builder.setPositiveButton(botonTrue, onclick);
                builder.setNegativeButton(botonFalse, onclick);
            }
            else {
                String tboton = botonTrue;
                if (tboton.equals(""))
                    tboton = botonFalse;
                builder.setNeutralButton(tboton, onclick);
            }

            etUnidades.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final boolean focus = hasFocus;
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mostrarTeclado(focus);
                        }
                    }, 300);
                }
            });

            aviso = builder.create();
            aviso.setCancelable(false);
            aviso.setCanceledOnTouchOutside(false);
            aviso.show();
        }

        @Override
        protected void onPostExecute(Void v) {
            unidades =Integer.valueOf(etUnidades.getText().toString().trim());
            super.onPostExecute(v);
        }

        @Override
        protected Void doInBackground(Integer... parametros ) {
            etUnidades.setText(String.valueOf(parametros[0]));
            etUnidades.selectAll();
            return null;
        }

        private void mostrarTeclado(boolean mostrar)  {
            if (mostrar){
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etUnidades, InputMethodManager.SHOW_IMPLICIT);
            }
            else {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etUnidades.getWindowToken(), 0);

            }
        }
    }

    public static class PidePaxSubmesa extends AsyncTask<Integer,Void,Void> {

        public static final int TIPO_DIALOGO_AÑADIR_PAX = 0;
        public static final int TIPO_DIALOGO_MOVER_PAX = 1;
        public static final int TIPO_DIALOGO_ORGANIZA_PAX = 2;


        private static AlertDialog aviso;
        private static Context context;

        private static DialogInterface.OnClickListener onclick;
        private static DialogInterface.OnClickListener onSelect;

        private static String titulo,mensaje, botonTrue,botonFalse;
        private static EditText etPax, etRoom;
        private static TextView tvPaxMover;
        private static ImageView ivPaxUp, ivPaxDown;
        private static LinearLayout layNewPax, layMovePax, layNewpax_pax, layNewPax_room;

        private static int paxAdd, paxMove;
        private static String room;
        private static ArrayList<ClaseSubMesas> submesas;
        private static RecyclerView rvSubmesas;
        private static RvAdapterSubmesasPax adapterSubmesas;
        private static int subMesaSel;
        private static int  tipo;
        private static boolean mover_uds;


        public static boolean isMover_uds() {
            return mover_uds;
        }

        public static void setMover_uds(boolean mover_uds) {
            PidePaxSubmesa.mover_uds = mover_uds;
        }

        public static void setRoom(String room ) {
            PidePaxSubmesa.room = room;
        }
        public static String getRoom(){
            return PidePaxSubmesa.room == null ? "": PidePaxSubmesa.room;
        }

        public static int getPaxAdd() {
            paxAdd = 0;
            if (etPax!= null)
                paxAdd = Integer.parseInt(etPax.getText().toString().trim());

            return paxAdd;
        }

        public static int getPaxMove() {
            paxMove =0;
            if (tvPaxMover != null)
                    paxMove = Integer.parseInt(tvPaxMover.getText().toString().trim());

            return paxMove;
        }

        public static void setSubMesaSel(int subMesaSel, int uds) {
            if (tipo == TIPO_DIALOGO_AÑADIR_PAX || tipo == TIPO_DIALOGO_MOVER_PAX) {
                if (uds > 0) {  // quito pax de submesa y añado a nueva
                    if (submesas.get(subMesaSel).pax > 1) {
                        submesas.get(subMesaSel).pax -= uds;
                        submesas.get(subMesaSel).paxTraspaso += uds;
                        paxMove += uds;
                        tvPaxMover.setText(String.valueOf(paxMove));
                    }
                } else {
                    if (submesas.get(subMesaSel).paxTraspaso > 0) {
                        submesas.get(subMesaSel).pax -= uds;
                        submesas.get(subMesaSel).paxTraspaso += uds;
                        paxMove += uds;
                        tvPaxMover.setText(String.valueOf(paxMove));
                    }
                }
            } else if (tipo == TIPO_DIALOGO_ORGANIZA_PAX) {
                if (uds > 0) {
                    submesas.get(subMesaSel).pax += uds;
                    submesas.get(subMesaSel).paxTraspaso -= uds;
                } else {
                    if (submesas.get(subMesaSel).pax > Math.abs(uds)) {
                        submesas.get(subMesaSel).pax -= Math.abs(uds);
                        submesas.get(subMesaSel).paxTraspaso += Math.abs(uds);
                    }
                }
            }
        }

        public static void setSubmesas(ArrayList<ClaseSubMesas> submesas) {
            PidePaxSubmesa.submesas = submesas;
        }

        public static void setContext(Context context) {
            PidePaxSubmesa.context = context;
        }

        public static void setTitulo(String titulo) {
            PidePaxSubmesa.titulo = titulo;
        }

        public static void setMensaje(String mensaje) {
        PidePaxSubmesa.mensaje = mensaje;
        }

        public static void setBotonTrue(String botonTrue) {
            PidePaxSubmesa.botonTrue = botonTrue;
        }

        public static void setBotonFalse(String botonFalse) {
            PidePaxSubmesa.botonFalse = botonFalse;
        }

        public static void setOnclick(DialogInterface.OnClickListener onclick) {
            PidePaxSubmesa.onclick = onclick;
        }

        public static void setTipo(int tipo) {
            PidePaxSubmesa.tipo = tipo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(titulo);


            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_pidepax,null);
            etPax = view.findViewById(R.id.etPax);
            etRoom = view.findViewById(R.id.etRoom);
            tvPaxMover = view.findViewById(R.id.tvPaxMover);
            ivPaxDown = view.findViewById(R.id.ivPaxDown);
            ivPaxUp = view.findViewById(R.id.ivPaxUp);
            layNewPax = view.findViewById(R.id.layNewPax);
            layNewPax_room = view.findViewById(R.id.layNewPax_room);
            layNewpax_pax = view.findViewById(R.id.layNewPax_pax);
            layMovePax = view.findViewById(R.id.layMovePax);

            rvSubmesas = view.findViewById(R.id.rvSubmesas);

            LinearLayoutManager llmMesas = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
            rvSubmesas.setLayoutManager(llmMesas);

            adapterSubmesas = new RvAdapterSubmesasPax(context,submesas) ;
            rvSubmesas.setAdapter(adapterSubmesas);

            builder.setView(view);

            etPax.setText(String.valueOf(paxAdd));
            etRoom.setText(room);

            if (!botonTrue.equals("") && !botonFalse.equals("")){
                builder.setPositiveButton(botonTrue, onclick);
                builder.setNegativeButton(botonFalse, onclick);
            }
            else {
                String tboton = botonTrue;
                if (tboton.equals(""))
                    tboton = botonFalse;
                builder.setNeutralButton(tboton, onclick);
            }

            layNewPax.setVisibility(VISIBLE);
            layNewpax_pax.setVisibility(VISIBLE);
            layNewPax_room.setVisibility(VISIBLE);

            layMovePax.setVisibility(VISIBLE);

            if (tipo == TIPO_DIALOGO_AÑADIR_PAX) {
                ivPaxUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        paxAdd +=1 ;
                        etPax.setText(String.valueOf(paxAdd));
                    }
                });

                ivPaxDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (paxAdd > 0){
                            paxAdd -=1 ;
                            etPax.setText(String.valueOf(paxAdd));
                        }
                    }
                });

                etPax.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        final boolean focus = hasFocus;
                        Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mostrarTeclado(focus);
                            }
                        }, 300);
                    }
                });


            }
            else {
                layNewpax_pax.setVisibility(GONE);
            }

            etRoom.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    room  = etRoom.getText().toString().trim().equals("") ? "":etRoom.getText().toString().trim();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            if (tipo == TIPO_DIALOGO_ORGANIZA_PAX){
                layMovePax.setVisibility(GONE);

                layNewPax_room.setVisibility(GONE);
            }
            etPax.requestFocus();

            aviso = builder.create();
            aviso.setCancelable(false);
            aviso.setCanceledOnTouchOutside(false);
            aviso.show();


        }

        @Override
        protected void onPostExecute(Void v) {
            paxAdd =Integer.valueOf(etPax.getText().toString().trim());
            room  = etRoom.getText().toString().trim().equals("") ? "":etRoom.getText().toString().trim();

            super.onPostExecute(v);
        }

        @Override
        protected Void doInBackground(Integer... parametros ) {
            paxAdd = parametros[0];
            paxMove = 0;
            if (tipo == TIPO_DIALOGO_AÑADIR_PAX) {
                room = "";
            }
            etPax.setText(String.valueOf(parametros[0]));
            etPax.selectAll();
            tvPaxMover.setText("0");
            etRoom.setText(room);
            return null;
        }

        private void mostrarTeclado(boolean mostrar)  {
            if (mostrar){
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etPax, InputMethodManager.SHOW_IMPLICIT);
            }
            else {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etPax.getWindowToken(), 0);

            }
        }


    }

    public static class PideRoom extends AsyncTask<Integer,Void,Void> {



        private static AlertDialog aviso;
        private static Context context;

        private static DialogInterface.OnClickListener onclick;
        private static DialogInterface.OnClickListener onSelect;

        private static String titulo,mensaje, botonTrue,botonFalse;
        private static EditText etRoom;
        private static ImageView ivExterno;
        private static LinearLayout layNewRoom;
        private static String room;

        public static void setRoom(String room ) { PideRoom.room = room; }
        public static String getRoom(){
            return room == null ? "":room;
        }


        public static void setContext(Context context) {
            PideRoom.context = context;
        }

        public static void setTitulo(String titulo) {
            PideRoom.titulo = titulo;
        }

        public static void setMensaje(String mensaje) {
            PideRoom.mensaje = mensaje;
        }

        public static void setBotonTrue(String botonTrue) {
            PideRoom.botonTrue = botonTrue;
        }

        public static void setBotonFalse(String botonFalse) {
            PideRoom.botonFalse = botonFalse;
        }

        public static void setOnclick(DialogInterface.OnClickListener onclick) {
            PideRoom.onclick = onclick;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(titulo);


            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_pideroom,null);
            etRoom = view.findViewById(R.id.etRoom);
            ivExterno = view.findViewById(R.id.ivExterno);

            layNewRoom = view.findViewById(R.id.layNewRoom);

            LinearLayoutManager llmMesas = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
            builder.setView(view);

            etRoom.setText(room);

            if (!botonTrue.equals("") && !botonFalse.equals("")){
                builder.setPositiveButton(botonTrue, onclick);
                builder.setNegativeButton(botonFalse, onclick);
            }
            else {
                String tboton = botonTrue;
                if (tboton.equals(""))
                    tboton = botonFalse;
                builder.setNeutralButton(tboton, onclick);
            }

            ivExterno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    room = "EXT";
                    etRoom.setText(room);
                }
            });

            etRoom.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    room  = etRoom.getText().toString().trim().equals("") ? "":etRoom.getText().toString().trim();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etRoom.requestFocus();

            aviso = builder.create();
            aviso.setCancelable(false);
            aviso.setCanceledOnTouchOutside(false);
            aviso.show();


        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
        }

        @Override
        protected Void doInBackground(Integer... parametros ) {
            room = "";
            etRoom.setText(room);
            return null;
        }

        private void mostrarTeclado(boolean mostrar)  {
            if (mostrar){
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etRoom, InputMethodManager.SHOW_IMPLICIT);
            }
            else {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etRoom.getWindowToken(), 0);

            }
        }


    }

    public static class TraspasaMesa extends AsyncTask<Integer,Void,Void> implements  SearchView.OnQueryTextListener {

        public static final int MOVER_MESA = 0;
        public static final int MOVER_SUBMESA = 1;

        private static AlertDialog aviso;
        private static Context context;
        private androidx.appcompat.widget.SearchView svMesa;

        private static DialogInterface.OnClickListener onclick;

        private static String titulo,botonFalse, botonTrue;

        private static TreeMap<Integer,ClaseMesas> mesas;
        private static RecyclerView rvMesas;
        private static RvAdapterMesas adapterMesas;
        private static int mesaSel;
        private static int mesaActual;
        private static int tipo;

        public static int getTipo() { return tipo; }

        public static void setTipo(int tipo) {
            TraspasaMesa.tipo = tipo;
        }

        public static TreeMap<Integer,ClaseMesas> getMesas() {
            return mesas;
        }

        public static void setMesas(TreeMap<Integer,ClaseMesas> mesas) {
            TraspasaMesa.mesas = mesas;
        }


        public static int getMesaSel() {
            return mesaSel;
        }

        public static void setMesaSel(int mesaSel) {
            TraspasaMesa.mesaSel = mesaSel;
        }

        public static int getMesaActual() {
            return mesaActual;
        }

        public static void setMesaActual(int mesaActual) {
            TraspasaMesa.mesaActual = mesaActual;
        }

        public static void setContext(Context context) {
            TraspasaMesa.context = context;
        }

        public static void setTitulo(String titulo) {
            TraspasaMesa.titulo = titulo;
        }


        public static void setBotonFalse(String botonFalse) {
            TraspasaMesa.botonFalse = botonFalse;
        }
        public static void setBotonTrue(String botonTrue) {
            TraspasaMesa.botonTrue = botonTrue;
        }

        public static void setOnclick(DialogInterface.OnClickListener onclick) {
            TraspasaMesa.onclick = onclick;
        }

        public void filtrarItem(String mesa){
            adapterMesas.setQuitaFocoSvMesa(true);
            adapterMesas.getFilter().filter(mesa);
        }

        public void swMesaClearFocus(){
            svMesa.clearFocus();
        }

        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            adapterMesas.getFilter().filter(s);
            return false;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(titulo);

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_traspasa_mesa,null);

            rvMesas = view.findViewById(R.id.rvMesas);
            svMesa = view.findViewById(R.id.svMesa);

            view.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

            DisplayMetrics displayMetrics =  context.getResources().getDisplayMetrics();
            int cols;
            float width = displayMetrics.xdpi;
            if  (width >= 600 )
                cols = 12;
            else if (width >= 480 )
                cols = 5;
            else
                cols = 4;

            GridLayoutManager llmMesas = new GridLayoutManager(context,cols,GridLayoutManager.VERTICAL,false);
            rvMesas.setLayoutManager(llmMesas);

            adapterMesas = new RvAdapterMesas(context,mesas,mesaActual,this) ;
            rvMesas.setAdapter(adapterMesas);

            svMesa.setQuery("",false);
            svMesa.clearFocus();

            setupSearchView();

            muestraSearch();


            builder.setView(view);


            if (!botonFalse.equals("") ){
                builder.setNegativeButton(botonFalse, onclick);
            }
            if (!botonTrue.equals("") ){
                builder.setPositiveButton(botonTrue, onclick);
            }

            aviso = builder.create();
            aviso.setCancelable(false);
            aviso.setCanceledOnTouchOutside(false);
            aviso.show();


        }

        @Override
        protected void onPostExecute(Void v) {
//            paxAdd =Integer.valueOf(etPax.getText().toString().trim());
            super.onPostExecute(v);
        }

        @Override
        protected Void doInBackground(Integer... parametros ) {
            return null;
        }

        private void setupSearchView() {
            svMesa.setIconifiedByDefault(false);
            svMesa.setOnQueryTextListener(this);
            svMesa.setSubmitButtonEnabled(true);
            svMesa.setQueryHint(context.getResources().getString(R.string.svMesas));
        }



        private void muestraSearch() {
            RecyclerView.LayoutManager layout = rvMesas.getLayoutManager();
            int min = ((GridLayoutManager) layout).findFirstCompletelyVisibleItemPosition();
            int max = ((GridLayoutManager) layout).findLastCompletelyVisibleItemPosition();

            if (mesas!= null) {
                if ((min <= 0) && (max >= mesas.size()-1)) {
                    svMesa.setVisibility(GONE);
                }
                else{
                    svMesa.setQuery("", false);
                    svMesa.clearFocus();
                    svMesa.setVisibility(VISIBLE);
                }
            }
            else
                svMesa.setVisibility(GONE);

        }

    }

    public static class PideFirma extends AsyncTask<Integer,Void,Void> {

        private AlertDialog.Builder builder;

        private SignaturePad mSignaturePad;

        private static AlertDialog aviso;
        private static Context context;

        private static DialogInterface.OnClickListener onclick;

        private static String titulo,fecha, habitacion, nombre, importe;
        private static TextView tvFecha, tvHabitacion, tvNombre, tvImporte;
        private static ImageView ivAceptar, ivBorrar, ivCancelar;

        private static View.OnClickListener clickAceptar, clickCancelar;
        public void cerrar(){
            aviso.dismiss();
        }

        public Bitmap getFirma(){
            return mSignaturePad.getSignatureBitmap();
        }

        public void setContext(Context context) {
            PideFirma.context = context;
        }

        public void setTitulo(String titulo) {
            PideFirma.titulo = titulo;
        }

        public void setFecha(String fecha) {
            PideFirma.fecha = fecha;
        }

        public void setHabitacion(String habitacion) {
            PideFirma.habitacion = habitacion;
        }

        public void setNombre(String nombre) {
            PideFirma.nombre = nombre;
        }

        public void setImporte(String importe) {
            PideFirma.importe = importe;
        }

        public void setAceptarOnclick (View.OnClickListener click){
            clickAceptar = click;
        }

        public void setCancelarOnclick (View.OnClickListener click){
            clickCancelar = click;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            builder = new AlertDialog.Builder(context);
            builder.setTitle(titulo);


            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_pidefirma,null);
            tvFecha = view.findViewById(R.id.tvFecha);
            tvHabitacion = view.findViewById(R.id.tvHabitacion);
            tvNombre = view.findViewById(R.id.tvNombre);
            tvImporte = view.findViewById(R.id.tvImporte);


            mSignaturePad = view.findViewById(R.id.signature_pad);
            mSignaturePad.setPenColor(Color.BLACK);
            mSignaturePad.setVelocityFilterWeight((float) 0.9);
            mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
                @Override
                public void onStartSigning() {

                }

                @Override
                public void onSigned() {
                    ivAceptar.setEnabled(true);
                }

                @Override
                public void onClear() {
                    ivAceptar.setEnabled(false);
                }
            });

            builder.setView(view);
            builder.setNeutralButton(null,null);
            builder.setPositiveButton(null,null);
            builder.setNegativeButton(null,null);

            tvFecha.setText(fecha);
            tvHabitacion.setText(habitacion);
            tvNombre.setText(nombre);
            tvImporte.setText(importe+" €");

            ivAceptar = view.findViewById(R.id.ivAceptar);
            ivBorrar = view.findViewById(R.id.ivBorrar);
            ivCancelar = view.findViewById(R.id.ivCancelar);

            ivAceptar.setOnClickListener(clickAceptar);
            ivCancelar.setOnClickListener(clickCancelar);
            ivBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSignaturePad.clear();
                }
            });

            ivAceptar.setEnabled( false);

            aviso = builder.create();
            aviso.setCancelable(false);
            aviso.setCanceledOnTouchOutside(false);
            aviso.show();


        }

        @Override
        protected void onPostExecute(Void v) {
            //clave =etClave.getText().toString().trim();
            super.onPostExecute(v);
        }

        @Override
        protected Void doInBackground(Integer... parametros ) {
            return null;
        }

    }

    public static class SiguePlatos extends AsyncTask<Integer,Void,Void> {

        private static AlertDialog aviso;
        private static Context context;

        private static DialogInterface.OnClickListener onclick;

        private static String titulo,mensaje, botonFalse;
        private static int orden;
        private static ArrayList<Integer> tipos;
        private static androidx.appcompat.widget.AppCompatImageView ivBebida,ivEntrante, ivPrimero, ivSegundo, ivPostre;



        public static void setContext(Context context) {
            SiguePlatos.context = context;
        }

        public static void setTitulo(String titulo) {
            SiguePlatos.titulo = titulo;
        }

        public static int getOrden() {
            return orden;
        }

        public static void setTipos(ArrayList<Integer> tipos) {
            SiguePlatos.tipos = tipos;
        }

        public static void setBotonFalse(String botonFalse) {
            SiguePlatos.botonFalse = botonFalse;
        }

        public static void setOnclick(DialogInterface.OnClickListener onclick) {
            SiguePlatos.onclick = onclick;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(titulo);


            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_sigueplato,null);

            ivBebida = view.findViewById(R.id.ivBebida);
            ivEntrante = view.findViewById(R.id.ivEntrante);
            ivPrimero = view.findViewById(R.id.ivPrimero);
            ivSegundo = view.findViewById(R.id.ivSegundo);
            ivPostre = view.findViewById(R.id.ivPostre);

            ivBebida.setVisibility(GONE);
            ivEntrante.setVisibility(GONE);
            ivPrimero.setVisibility(GONE);
            ivSegundo.setVisibility(GONE);
            ivPostre.setVisibility(GONE);

            if (tipos.contains(0)) ivBebida.setVisibility(VISIBLE);
            if (tipos.contains(1)) ivEntrante.setVisibility(VISIBLE);
            if (tipos.contains(2)) ivPrimero.setVisibility(VISIBLE);
            if (tipos.contains(3)) ivSegundo.setVisibility(VISIBLE);
            if (tipos.contains(4)) ivPostre.setVisibility(VISIBLE);


            ivBebida.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {
                                                  orden = 0;
                                                  aviso.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
                                              }
                                          }
            );
            ivEntrante.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        orden = 1;
                        aviso.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
                    }
                }
            );
            ivPrimero.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 orden = 2;
                                                 aviso.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
                                             }
                                         }
            );
            ivSegundo.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 orden = 3;
                                                 aviso.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
                                             }
                                         }
            );
            ivPostre.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 orden = 4;
                                                 aviso.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
                                             }
                                         }
            );



            builder.setView(view);
            builder.setPositiveButton("Avisar",onclick);
            builder.setNegativeButton(botonFalse, onclick);


            aviso = builder.create();
            aviso.setCancelable(false);
            aviso.setCanceledOnTouchOutside(false);
            aviso.show();
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
        }

        @Override
        protected Void doInBackground(Integer... parametros ) {
            aviso.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(View.INVISIBLE);
            return null;
        }

    }

    public static class ResumenComanda extends AsyncTask<Integer,Void,Void> {

        private static AlertDialog aviso;
        private static Context context;

        private static DialogInterface.OnClickListener onclick;

        private static String titulo;
        private static ArrayList<ClaseLineaVentas> listaLineaVentas;

        private static TextView tvUds, tvTotal;
        private static RecyclerView rvLineaVentas;
        private static RvAdapterResumenComanda adapterResumenLineaVentas;

        public static void setListaLineaVentas(ArrayList<ClaseLineaVentas> listaLineaVentas) {
            ResumenComanda.listaLineaVentas = listaLineaVentas;
        }

        public static void setContext(Context context) {
            ResumenComanda.context = context;
        }

        public static void setTitulo(String titulo) {
            ResumenComanda.titulo = titulo;
        }

        public static void setOnclick(DialogInterface.OnClickListener onclick) {
            ResumenComanda.onclick = onclick;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(titulo);


            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_resumencomanda,null);

            tvUds = view.findViewById(R.id.tvUds);
            tvTotal = view.findViewById(R.id.tvTotal);

            rvLineaVentas = view.findViewById(R.id.rvLineaVentas);
            GridLayoutManager llmLineaVentas = new GridLayoutManager(context,1,GridLayoutManager.VERTICAL,false);
            rvLineaVentas.setLayoutManager(llmLineaVentas);

            int uds = 0;
            double suma = 0;
            for (ClaseLineaVentas linea: listaLineaVentas){
                uds += linea.cantidad;
                suma += linea.teuros;
            }
            tvTotal.setText(ClaseUtils.double2string(suma,2));
            tvUds.setText(String.valueOf(uds));

            adapterResumenLineaVentas= new RvAdapterResumenComanda(context, listaLineaVentas) ;
            rvLineaVentas.setAdapter(adapterResumenLineaVentas);

            builder.setView(view);
            builder.setNeutralButton("Volver", onclick);


            aviso = builder.create();
            aviso.setCancelable(false);
            aviso.setCanceledOnTouchOutside(false);
            aviso.show();
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
        }

        @Override
        protected Void doInBackground(Integer... parametros ) {
            return null;
        }

    }

    public static class FiltarPlatos
            extends androidx.fragment.app.DialogFragment
            implements
            FragmentDialogFiltroAlergenos.InterfaceFrgDlgFiltAler,
            FragmentDialogFiltroEtiquetas.InterfaceFrgDlgFiltEtiq,
            FragmentDialogFiltroOrdenPlatos.InterfaceFrgDlgFiltOrden        {

        ViewPager viewPager;
        TabLayout tlTabs;

        public interface InterfaceUtilsFiltraPlatos {
            void onCLickVolverFiltraPlatos();
        }

        InterfaceUtilsFiltraPlatos interfaceUtilsFiltraPlatos;

        public static FragmentManager fm;
        public static FiltarPlatos filtarPlatos;

        private static ArrayList<ClaseItemFiltro> alergenos;
        private static ArrayList<ClaseItemFiltro> etiquetas;
        private static ArrayList<ClaseItemFiltro> ordenPlatos;

        public static ArrayList<ClaseItemFiltro> getAlergenos() {
            return alergenos;
        }

        public  void setAlergenos(ArrayList<ClaseItemFiltro> alergenos) {
            FiltarPlatos.alergenos = alergenos;
        }

        public static ArrayList<ClaseItemFiltro> getEtiquetas() {
            return etiquetas;
        }

        public  void setEtiquetas(ArrayList<ClaseItemFiltro> etiquetas) {
            FiltarPlatos.etiquetas = etiquetas;
        }

        public static ArrayList<ClaseItemFiltro> getOrdenPlatos() {
            return ordenPlatos;
        }

        public  void setOrdenPlatos(ArrayList<ClaseItemFiltro> ordenPlatos) {
            FiltarPlatos.ordenPlatos = ordenPlatos;
        }

        ImageView ivVolver;


        @Override
        public void onStart() {
            super.onStart();
            Dialog dialog  = getDialog();
            if (dialog != null){
                int ancho = ViewGroup.LayoutParams.MATCH_PARENT;
                int alto = ViewGroup.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setLayout(ancho,alto);
            }
        }

        @Override
        public ArrayList<ClaseItemFiltro> flCargaDatosAlergenos() {
            return alergenos;
        }

        @Override
        public ArrayList<ClaseItemFiltro> flCargaDatosEtiquetas() {
            return etiquetas;
        }

        @Override
        public ArrayList<ClaseItemFiltro> flCargaDatosOrdenPlatos() {

            return ordenPlatos;
        }

        public static FiltarPlatos newInstance(Context context){

            FiltarPlatos filtarPlatos = new FiltarPlatos();
            FiltarPlatos.filtarPlatos = filtarPlatos;
            filtarPlatos.interfaceUtilsFiltraPlatos = (InterfaceUtilsFiltraPlatos) context;
            return filtarPlatos;
        }


        @Override
        public void show(@NonNull FragmentManager manager, @Nullable String tag) {
            super.show(manager, tag);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.dialog_filtros, container,
                    false);
            getDialog().setTitle("");
            // Do something else
            return rootView;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            this.fm = getChildFragmentManager();
            viewPager = view.findViewById(R.id.vpPager);
            tlTabs = view.findViewById(R.id.tlTabs);
            tlTabs.addTab(tlTabs.newTab().setText("Etiquetas"));
            tlTabs.addTab(tlTabs.newTab().setText("Orden Platos"));
            tlTabs.addTab(tlTabs.newTab().setText("Alergenos"));


            viewPager.setAdapter(new VpAdapterTabFiltros(fm));
            ivVolver = view.findViewById(R.id.ivVolver);

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlTabs));
            tlTabs.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

            ivVolver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (FiltarPlatos.filtarPlatos.isVisible()) {
                        FiltarPlatos.filtarPlatos.dismiss();
                        FiltarPlatos.filtarPlatos = null;
                    }
                    interfaceUtilsFiltraPlatos.onCLickVolverFiltraPlatos();
                }
            });
        }
    }

    public static class PideExtras
            extends androidx.fragment.app.DialogFragment
            implements
            FragmentDialogExtras.InterfaceFrgDlgExtras,
            FragmentDialogNotas.InterfaceFrgDlgNotas {

        ViewPager viewPager;
        TabLayout tlTabs;


        public static FragmentManager fm;
        public static PideExtras pideExtras;

        private static ArrayList<ClaseItemExtra> extras;

        public static ArrayList<ClaseItemExtra> getExtras() {

            //return extras;
            return FragmentDialogNotas.getNotas();
        }

        public void setExtras(ArrayList<ClaseItemExtra> extras) {
            PideExtras.extras = extras;
        }


        ImageView ivVolver;

        @Override
        public void onStart() {
            super.onStart();
            Dialog dialog = getDialog();
            if (dialog != null) {
                int ancho = ViewGroup.LayoutParams.MATCH_PARENT;
                int alto = ViewGroup.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setLayout(ancho, alto);
                dialog.getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        }

        @Override
        public ArrayList<ClaseItemExtra> exCargaDatosExtras() {
            if (extras == null) {
                return new ArrayList<>();
            }

            return extras.stream().filter(e -> e.tipo.equalsIgnoreCase("E")).collect(Collectors.toCollection(ArrayList::new));

        }

        @Override
        public ArrayList<ClaseItemExtra> ntCargaDatosNotas() {
            if (extras == null) {
                return new ArrayList<>();
            }

            return extras.stream().filter(e -> e.tipo.equalsIgnoreCase("N")).collect(Collectors.toCollection(ArrayList::new));
        }

        public static PideExtras newInstance(Context context) {
            PideExtras pideExtras = new PideExtras();
            PideExtras.pideExtras = pideExtras;
            return pideExtras;
        }

        @Override
        public void show(@NonNull FragmentManager manager, @Nullable String tag) {
            super.show(manager, tag);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.dialog_pideextras, container, false);
            getDialog().setTitle("");
            return rootView;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            this.fm = getChildFragmentManager();
            View vPreview = view.findViewById(R.id.incPreviewExtras);
            if (vPreview != null) {
                vPreview.setVisibility(View.GONE);
            }
            viewPager = view.findViewById(R.id.vpPager);
            tlTabs = view.findViewById(R.id.tlTabs);
            tlTabs.addTab(tlTabs.newTab().setText("Extras"));
            tlTabs.addTab(tlTabs.newTab().setText("Notas"));

            viewPager.setAdapter(new VpAdapterTabPideExtras(fm));
            ivVolver = view.findViewById(R.id.ivVolver);

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlTabs));
            tlTabs.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

            ivVolver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (PideExtras.pideExtras != null && PideExtras.pideExtras.isVisible()) {
                        PideExtras.pideExtras.dismiss();
                        PideExtras.pideExtras = null;
                    }

                    Bundle result = new Bundle();
                    result.putBoolean("cerrado",true);
                    getParentFragmentManager().setFragmentResult("pide_extras_result",result);
                    dismiss();
                }
            });
        }
    }

    public static class VerFactura extends androidx.fragment.app.DialogFragment
            implements FragmentDialogFacturaCabecera.InterfaceFrgDlgFacCabecera,
            FragmentDialogFacturaLineas.InterfaceFrgDlgFacLineas {
        ViewPager viewPager;
        TabLayout tlTabs;

        public static FragmentManager fm;
        public static VerFactura verFactura;
        static String empresa;
        static String cif;
        static String mesa;
        static String tpv;
        static String igic;
        static String camarero;
        static ClaseFacturaCabecera cabecera;
        static String firma;

        public interface InterfaceVerFactura {
            void vfImprimir();
        }
        InterfaceVerFactura interfaceVerFactura;

        private static ArrayList<ClaseLineaVentas> listaLineaVentas;


        public static void setEmpresa(String empresa) {
            VerFactura.empresa = empresa;
        }

        public static void setCif(String cif) {
            VerFactura.cif = cif;
        }

        public static void setMesa(String mesa) {
            VerFactura.mesa = mesa;
        }

        public static void setTpv(String tpv) {
            VerFactura.tpv = tpv;
        }

        public static void setIgic(String igic) {
            VerFactura.igic = igic;
        }

        public static void setCamarero(String camarero) {
            VerFactura.camarero = camarero;
        }

        public  void setCabecera(ClaseFacturaCabecera cabecera) {
            VerFactura.cabecera = cabecera;
        }

        public  void setFirma(String firma) {
            VerFactura.firma = firma;
        }

        public static void setListaLineaVentas(ArrayList<ClaseLineaVentas> listaLineaVentas) {
            VerFactura.listaLineaVentas = listaLineaVentas;
        }

        public void setInterfaceVerFactura(InterfaceVerFactura interfaceVerFactura) {
            this.interfaceVerFactura = interfaceVerFactura;
        }

        ImageView ivVolver, ivImprimir;


        @Override
        public Bundle cargaDatos() {
            Bundle bundle = new Bundle();
            bundle.putString("empresa", empresa);
            bundle.putString("cif", cif);
            bundle.putString("camarero", camarero);
            bundle.putString("tpv", tpv);
            bundle.putString("mesa", mesa);
            bundle.putString("igic", igic);
            bundle.putString("firma", firma);
            bundle.putParcelable("cabecera",cabecera);

            return bundle;
        }

        @Override
        public void onStart() {
            super.onStart();
            Dialog dialog  = getDialog();
            if (dialog != null){
                int ancho = ViewGroup.LayoutParams.MATCH_PARENT;
                int alto = ViewGroup.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setLayout(ancho,alto);
            }
        }

        @Override
        public ArrayList<ClaseLineaVentas> flCargaDatos() {
            return listaLineaVentas;
        }

        public static VerFactura newInstance(){

            VerFactura verFactura=new VerFactura();
            VerFactura.verFactura = verFactura;
            return  verFactura;
        }


        @Override
        public void show(@NonNull FragmentManager manager, @Nullable String tag) {
            super.show(manager, tag);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.dialog_factura, container,
                    false);
            getDialog().setTitle("");
            // Do something else
            return rootView;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            this.fm = getChildFragmentManager();
            viewPager = view.findViewById(R.id.vpPager);
            tlTabs = view.findViewById(R.id.tlTabs);
            tlTabs.addTab(tlTabs.newTab().setText("Detalle"));
            tlTabs.addTab(tlTabs.newTab().setText("Resumen"));

            viewPager.setAdapter(new VpAdapterTabFactura(fm));
            ivImprimir = view.findViewById(R.id.ivImprimir);
            ivVolver = view.findViewById(R.id.ivVolver);

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlTabs));
            tlTabs.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

            ivVolver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (VerFactura.verFactura.isVisible()) {
                        VerFactura.verFactura.dismiss();
                        VerFactura.verFactura = null;
                    }
                }
            });
            ivImprimir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    interfaceVerFactura.vfImprimir();
                }
            });
        }

        public static void cerrar() {
            if (verFactura != null) {
                verFactura.dismiss();
            }
        }
    }


    public static class VerInfoProducto extends AsyncTask<Integer,Void,Void> {

        private static AlertDialog aviso;
        private static Context context;

        private static DialogInterface.OnClickListener onclick;

        private static String titulo;
        private static ArrayList<ClaseItemFiltro> listaAlergenos, listaEtiquetas;
        private static ClaseProductos producto;
        private static ClaseCabeceras cabecera;

        private static TextView tvCabecera, tvDescripcion,tvPrecio,tvNota;
        androidx.appcompat.widget.AppCompatImageView ivOrdenPlato, ivTipo;
        private static RecyclerView rvAlergenos,rvEtiquetas;

        private static RvAdapterAlergenos adapterAlergenos;
        private static RvAdapterEtiquetas adapterEtiquetas;


        public static void setContext(Context context) {
            VerInfoProducto.context = context;
        }

        public static void setTitulo(String titulo) {
            VerInfoProducto.titulo = titulo;
        }

        public static void setProducto(ClaseProductos producto) {
            VerInfoProducto.producto = producto;
        }

        public static void setCabecera(ClaseCabeceras cabecera) {
            VerInfoProducto.cabecera = cabecera;
        }

        public static void setOnclick(DialogInterface.OnClickListener onclick) {
            VerInfoProducto.onclick = onclick;
        }

        public static void setListaAlergenos(ArrayList<ClaseItemFiltro> listaAlergenos) {
            VerInfoProducto.listaAlergenos = listaAlergenos;
        }

        public static void setListaEtiquetas(ArrayList<ClaseItemFiltro> listaEtiquetas) {
            VerInfoProducto.listaEtiquetas = listaEtiquetas;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(titulo);


            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_productoinfo,null);

            tvCabecera = view.findViewById(R.id.tvCabecera);
            tvDescripcion = view.findViewById(R.id.tvDescripcion);
            tvPrecio = view.findViewById(R.id.tvPrecio);
            tvNota = view.findViewById(R.id.tvNota);
            ivOrdenPlato = view.findViewById(R.id.ivOrdenPlato);
            ivTipo = view.findViewById(R.id.ivTipo);


            rvAlergenos = view.findViewById(R.id.rvAlergenos);
            rvEtiquetas = view.findViewById(R.id.rvEtiquetas);

            GridLayoutManager glmAlergenos = new GridLayoutManager(context,1,GridLayoutManager.VERTICAL,false);
            rvAlergenos.setLayoutManager(glmAlergenos);

            GridLayoutManager glmEtiquetas = new GridLayoutManager(context,1,GridLayoutManager.VERTICAL,false);
            rvEtiquetas.setLayoutManager(glmEtiquetas);

            tvCabecera.setText(cabecera.descripcion);
            tvDescripcion.setText(producto.descripcion.toUpperCase());
            tvPrecio.setText(producto.euros);
            tvNota.setText(producto.notas);
            tvNota.setMovementMethod(new ScrollingMovementMethod());
            ivOrdenPlato.setVisibility(VISIBLE);
            switch (producto.orden_platos){
                case 0:
                    ivOrdenPlato.setBackgroundDrawable(context.getDrawable(R.drawable.navmenu_bebida));
                    break;
                case 1:
                    ivOrdenPlato.setBackgroundDrawable(context.getDrawable(R.drawable.navmenu_entrante));
                    break;
                case 2:
                    ivOrdenPlato.setBackgroundDrawable(context.getDrawable(R.drawable.navmenu_primero));
                    break;
                case 3:
                    ivOrdenPlato.setBackgroundDrawable(context.getDrawable(R.drawable.navmenu_comida));
                    break;
                case 4:
                    ivOrdenPlato.setBackgroundDrawable(context.getDrawable(R.drawable.navmenu_postre));
                    break;
                default:
                    ivOrdenPlato.setVisibility(View.INVISIBLE);
            }

            adapterAlergenos= new RvAdapterAlergenos(context, listaAlergenos) ;
            rvAlergenos.setAdapter(adapterAlergenos);

            adapterEtiquetas= new RvAdapterEtiquetas(context, listaEtiquetas) ;
            rvEtiquetas.setAdapter(adapterEtiquetas);

            builder.setView(view);
            builder.setNeutralButton("Volver", onclick);


            aviso = builder.create();
            aviso.setCancelable(false);
            aviso.setCanceledOnTouchOutside(false);
            aviso.show();
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
        }

        @Override
        protected Void doInBackground(Integer... parametros ) {
            return null;
        }

    }


    public static class VerInfoPension extends AsyncTask<Integer,Void,Void> {

        private static AlertDialog aviso;
        private static Context context;

        private static DialogInterface.OnClickListener onclick;

        private static String titulo;
        private static ClaseSubMesas subMesa;
        private static List<Hora_Comidas> listaHoraComidas;

        private static TextView tvPension, tvTipoPension, tvHorarioApertura, tvAplicaPension, tvPensionAplicada, tvHorarioAplicado, tvHorarioDesayuno, tvHorarioAlmuerzo, tvHorarioCena;


        public static void setContext(Context context) {
            VerInfoPension.context = context;
        }

        public static void setTitulo(String titulo) {
            VerInfoPension.titulo = titulo;
        }

        public static void setSubMesa(ClaseSubMesas subMesa) {
            VerInfoPension.subMesa = subMesa;
        }

        public static void setListaHoraComidas(List<Hora_Comidas> listaHoraComidas) {
            VerInfoPension.listaHoraComidas = listaHoraComidas;
        }

        public static void setOnclick(DialogInterface.OnClickListener onclick) {
            VerInfoPension.onclick = onclick;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(titulo);


            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_pensioninfo,null);

            tvPension =  view.findViewById(R.id.tvPension);
            tvTipoPension =  view.findViewById(R.id.tvTipoPension);
            tvHorarioApertura =  view.findViewById(R.id.tvHoraApertura);
            tvAplicaPension = view.findViewById(R.id.tvAplicaPension);
            tvPensionAplicada =  view.findViewById(R.id.tvPensionAplicada);
            tvHorarioAplicado = view.findViewById(R.id.tvHorarioAplicado);
            tvHorarioDesayuno = view.findViewById(R.id.tvHorarioDesayuno);
            tvHorarioAlmuerzo =  view.findViewById(R.id.tvHorarioAlmuerzo);
            tvHorarioCena = view.findViewById(R.id.tvHorarioCena);



            tvPension.setText(subMesa.descPension+" ("+subMesa.pension+")");
            tvTipoPension.setText( subMesa.descTipoPension+" ("+subMesa.tipoPension+")");
            tvHorarioApertura.setText(Html.fromHtml("<i><b>Hora comanda:</b>"+subMesa.horaApertura+"</i>",Html.FROM_HTML_MODE_LEGACY));
            tvAplicaPension.setText(Html.fromHtml("<i><b>Aplica Pension:</b>" + (subMesa.aplicaPension ? " Sí" : " No")+"</i>",Html.FROM_HTML_MODE_LEGACY));
            tvPensionAplicada.setText(Html.fromHtml("<i><b>Pase Aplicado:</b> "+ subMesa.tipoPensionAplicada+"</i>",Html.FROM_HTML_MODE_LEGACY));
            tvHorarioAplicado.setText(Html.fromHtml("<i><b>Horario aplicado:</b> "+subMesa.horarioPensionAplicado+"</i>",Html.FROM_HTML_MODE_LEGACY));

            tvHorarioDesayuno.setVisibility(GONE);
            tvHorarioAlmuerzo.setVisibility(GONE);
            tvHorarioCena.setVisibility(GONE);

            if (listaHoraComidas != null ) {
                for (Hora_Comidas hora : listaHoraComidas) {
                    if (hora.getTipo().equalsIgnoreCase("D")) {
                        tvHorarioDesayuno.setText("Pase desayuno: " +hora.getDesde_hora() + " a " + hora.getHasta_hora());
                        tvHorarioDesayuno.setVisibility(VISIBLE);
                    } else if (hora.getTipo().equalsIgnoreCase("A")) {
                        tvHorarioAlmuerzo.setText("Pase almuerzo: "+hora.getDesde_hora() + " a " + hora.getHasta_hora());
                        tvHorarioAlmuerzo.setVisibility(VISIBLE);
                    } else if (hora.getTipo().equalsIgnoreCase("C")) {
                        tvHorarioCena.setText("Pase cena: "+hora.getDesde_hora() + " a " + hora.getHasta_hora());
                        tvHorarioCena.setVisibility(VISIBLE);
                    }

                }
            }

            builder.setView(view);
            builder.setNeutralButton("Volver", onclick);

            aviso = builder.create();
            aviso.setCancelable(false);
            aviso.setCanceledOnTouchOutside(false);
            aviso.show();
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
        }

        @Override
        protected Void doInBackground(Integer... parametros ) {
            return null;
        }

    }


    public static class CalculaPrecioManual extends AsyncTask<Double,Void,Void> {

        private static AlertDialog aviso;
        private static Context context;

        private static DialogInterface.OnClickListener onclick;

        private static TextWatcher twCalculaPrecio;

        private static RadioButton rbPorcentaje, rbPeso, rbImporte;
        private static TextView tvPrecioOriginal, tvPrecioFinal;
        private static EditText etPorcentaje, etPeso, etImporte;

        private static String botonTrue,botonFalse;
        private static Double  precioOriginal;
        private static Double  precioFinal;
        private static int opcion;
        private static int porcentaje;
        private static int peso;
        private static Double importeDto;


        public static void setContext(Context context) {
            CalculaPrecioManual.context = context;
        }

        public static void setBotonTrue(String botonTrue) {
            CalculaPrecioManual.botonTrue = botonTrue;
        }

        public static void setBotonFalse(String botonFalse) {
            CalculaPrecioManual.botonFalse = botonFalse;
        }

        public static void setPrecioOriginal(Double precioOriginal) {
            CalculaPrecioManual.precioOriginal = precioOriginal;
        }

        public static Double getPrecioFinal() {
            return precioFinal;
        }

        public static int getOpcion() {
            return opcion;
        }

        public static int getPorcentaje() {
            return porcentaje;
        }

        public static int getPeso() {
            return peso;
        }

        public static Double getImporteDto() {
            return importeDto;
        }

        public static void setOnclick(DialogInterface.OnClickListener onclick) {
            CalculaPrecioManual.onclick = onclick;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_calculaprecio,null);

            tvPrecioOriginal = view.findViewById(R.id.tvPrecioOriginal);
            tvPrecioFinal = view.findViewById(R.id.tvPrecioFinal);

            rbPorcentaje = view.findViewById(R.id.rbPorcentaje);
            rbPeso = view.findViewById(R.id.rbPeso);
            rbImporte = view.findViewById(R.id.rbImporte);

            etPorcentaje = view.findViewById(R.id.etPorcentaje);
            etPeso = view.findViewById(R.id.etPeso);
            etImporte = view.findViewById(R.id.etImporte);

            builder.setView(view);


            if (!botonTrue.equals("") && !botonFalse.equals("")){
                builder.setPositiveButton(botonTrue, onclick);
                builder.setNegativeButton(botonFalse, onclick);
            }
            else {
                String tboton = botonTrue;
                if (tboton.equals(""))
                    tboton = botonFalse;
                builder.setNeutralButton(tboton, onclick);
            }

            etPorcentaje.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(final View v, boolean hasFocus) {
                    final boolean focus = hasFocus;
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mostrarTeclado(focus,v);
                        }
                    }, 300);
                }
            });

            etPeso.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(final View v, boolean hasFocus) {
                    final boolean focus = hasFocus;
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mostrarTeclado(focus,v);
                        }
                    }, 300);
                }
            });

            etImporte.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(final View v, boolean hasFocus) {
                    final boolean focus = hasFocus;
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mostrarTeclado(focus,v);
                        }
                    }, 300);
                }
            });

            rbPorcentaje.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rbPeso.setChecked(false);
                    rbImporte.setChecked(false);
                    etPorcentaje.selectAll();
                    etPorcentaje.setFocusable(true);
                    etPorcentaje.requestFocus();
                    calculaPrecioFinal();
                }
            });

            rbPeso.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rbPorcentaje.setChecked(false);
                    rbImporte.setChecked(false);
                    etPeso.selectAll();
                    etPeso.setFocusable(true);
                    etPeso.requestFocus();
                    calculaPrecioFinal();
                }
            });

            rbImporte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rbPorcentaje.setChecked(false);
                    rbPeso.setChecked(false);
                    etImporte.selectAll();
                    etImporte.setFocusable(true);
                    etImporte.requestFocus();
                    calculaPrecioFinal();
                }
            });


            twCalculaPrecio =  new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    calculaPrecioFinal();
                }
            };

            etPorcentaje.addTextChangedListener(twCalculaPrecio);
            etPeso.addTextChangedListener(twCalculaPrecio);
            etImporte.addTextChangedListener(twCalculaPrecio);
            rbPorcentaje.setChecked(true);

            String tmp = context.getResources().getString(R.string.dialog_strCalculaPrecio_preciooriginal);
            tmp = tmp.replace("[precio_original]",ClaseUtils.double2string(precioOriginal,2));
            tvPrecioOriginal.setText(tmp);
            etPorcentaje.setText("0");
            etPeso.setText("1000");
            etImporte.setText("0.00");
            calculaPrecioFinal();

            etPorcentaje.selectAll();
            etPorcentaje.setFocusable(true);
            etPorcentaje.requestFocus();

            aviso = builder.create();
            aviso.setCancelable(false);
            aviso.setCanceledOnTouchOutside(false);
            aviso.show();
        }

        @Override
        protected void onPostExecute(Void v) {


            super.onPostExecute(v);
        }

        @Override
        protected Void doInBackground(Double... parametros ) {

            return null;
        }

        private void mostrarTeclado(boolean mostrar, View v)  {
            if (mostrar){
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
            }
            else {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }

        private void calculaPrecioFinal() {
            String tmp = "";
            if (rbPorcentaje.isChecked()) {
                tmp = etPorcentaje.getText().toString();

                if (tmp == null || tmp.trim().equals(""))
                    tmp = "0";

                Double dto = Double.parseDouble(tmp);
                porcentaje = dto.intValue();
                dto = dto/100;
                precioFinal = precioOriginal * (1-dto);

            }
            else if (rbPeso.isChecked()){
                tmp = etPeso.getText().toString();

                if (tmp == null || tmp.trim().equals(""))
                    tmp = "0";

                peso = Integer.parseInt(tmp);
                precioFinal = precioOriginal * peso/1000;
            }
            else {
                tmp = etImporte.getText().toString();

                if (tmp == null || tmp.trim().equals(""))
                    tmp = "0.00";
                importeDto = Double.parseDouble(tmp);
                precioFinal = precioOriginal - importeDto;
            }

            if (precioFinal < 0 )
                precioFinal = 0.00;

            tmp = context.getResources().getString(R.string.dialog_strCalculaPrecio_preciofinal);
            tmp = tmp.replace("[precio_final]",ClaseUtils.double2string(precioFinal,2));
            tvPrecioFinal.setText(tmp);

            if (rbPorcentaje.isChecked())
                opcion = 1;
            else if (rbPeso.isChecked())
                opcion = 2;
            else
                opcion = 3;

        }


    }

    public static class VerTiquet extends AsyncTask<Integer,Void,Void> {

        private static AlertDialog aviso;
        private static Context context;

        private static WebView wbTiquet;
        private static DialogInterface.OnClickListener onclick;

        private static String titulo,mensaje;

        public static  boolean isVisible(){
            if (aviso == null) return false;

            return aviso.isShowing();
        }

        public static void setContext(Context context) {
            VerTiquet.context = context;
        }

        public static void setTitulo(String titulo) {
            VerTiquet.titulo = titulo;
        }

        public static void setMensaje(String mensaje) {
            VerTiquet.mensaje = mensaje;
        }

        public static void setOnclick(DialogInterface.OnClickListener onclick) {
            VerTiquet.onclick = onclick;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(titulo);


            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_tiquet,null);

            wbTiquet = view.findViewById(R.id.wvTiquet);
            wbTiquet.loadDataWithBaseURL(null,mensaje,"text/html","utf-8",null);

            builder.setView(view);
            builder.setNeutralButton("Aceptar",onclick);

            aviso = builder.create();
            aviso.setCancelable(false);
            aviso.setCanceledOnTouchOutside(false);
        aviso.show();
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
        }

        @Override
        protected Void doInBackground(Integer... parametros ) {
            return null;
        }

    }


    public static class DescargaAPK {
        private static ProgressDialog progressDialog;
        private static Thread thread;
        private static boolean stop;

        public static ProgressDialog getProgressDialog() {
            return progressDialog;
        }

        public static void mostrarDialogo(boolean mostrar, String titulo, String mensaje, Context context) {
            if (mostrar) {
                if (progressDialog == null)
                    progressDialog = new ProgressDialog(context);
                progressDialog.setMax(100);
                progressDialog.setMessage(mensaje);
                progressDialog.setTitle(titulo);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                stop = false;

                if (thread != null) thread = null;

                thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            do {
                                while (progressDialog.getProgress() <= progressDialog.getMax()) {
                                    Thread.sleep(200);
                                }
                            } while (!stop);
                            thread = null;

                        } catch (InterruptedException ie) {
                        } catch (Exception e) {
                        }
                        progressDialog = null;
                    }
                };

                thread.start();
                progressDialog.show();

            } else {
                if (progressDialog != null)
                    progressDialog.dismiss();
                stop = true;

            }

        }

        public static void cerrarDialogo() {
            if (progressDialog != null)
                progressDialog.dismiss();
            stop = true;
        }

        private static Handler handleProgress = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressDialog.incrementProgressBy(1);
            }
        };
    }


    //endregion

    //region "Install ID"
    public static class Installation {
        private static String sID = null;
        private static final String INSTALLATION = "INSTALLATION";

        public synchronized static String id(Context context) {
            if (sID == null) {
                File installation = new File(context.getFilesDir(), INSTALLATION);
                try {
                    if (!installation.exists())
                        writeInstallationFile(installation);
                    sID = readInstallationFile(installation);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return sID;
        }

        private static String readInstallationFile(File installation) throws IOException {
            RandomAccessFile f = new RandomAccessFile(installation, "r");
            byte[] bytes = new byte[(int) f.length()];
            f.readFully(bytes);
            f.close();
            return new String(bytes);
        }

        private static void writeInstallationFile(File installation) throws IOException {
            FileOutputStream out = new FileOutputStream(installation);
            String id = UUID.randomUUID().toString();
            out.write(id.getBytes());
            out.close();
        }
    }
    //endregion
}
