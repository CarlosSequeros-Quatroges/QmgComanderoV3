package es.quatroges.qgestpv_v3.bluetooth;


import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import es.quatroges.qgestpv_v3.R;

public class DialogosPrinter {

    //region "dialogos"

    public static class Aviso {
        private static AlertDialog aviso;
        private static DialogInterface.OnClickListener onclick;
        private static int resId;

        public static void setResId(int resId) {
            Aviso.resId = resId;
        }
        public static void setOnclick(DialogInterface.OnClickListener onclick) {
            Aviso.onclick = onclick;
        }

        public static void dismiss() {
            aviso.dismiss();
        }

        public static void mostrarAviso(String titulo, String mensaje, Context context) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(titulo);
            builder.setMessage(mensaje);
            builder.setNeutralButton(context.getResources().getString(R.string.strAceptar), onclick);

            aviso = builder.create();
            aviso.setCancelable(false);
            aviso.setCanceledOnTouchOutside(false);
            if (resId != -1) {
                aviso.setIcon(resId);
            }
            aviso.show();

        }

    }

    //endregion


}
