package es.quatroges.qgestpv_v3.datos.listas.facturas;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public class ClaseFacturas implements Parcelable, Comparable<ClaseFacturas> {
    public static  enum enOrden {
        factura,mesa,pago,hora
    }

    public static enOrden  orden;
    public static boolean asc;

    public int nfactura;

    public int mesa;

    public  int submesa;

    public String formapago;

    public int codenl;

    public  String fecha;

    public  String hora;
    public String descripcion;

    public ClaseFacturas(int nfactura, int mesa, int submesa, String formapago, int codenl, String fecha, String hora) {
        this.nfactura = nfactura;
        this.mesa = mesa;
        this.submesa = submesa;
        this.formapago = formapago;
        this.codenl = codenl;
        this.fecha = fecha;
        this.hora = hora;
        this.descripcion = "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.nfactura);
        dest.writeInt(this.mesa);
        dest.writeInt(this.submesa);
        dest.writeString(this.formapago);
        dest.writeInt(this.codenl);
        dest.writeString(this.fecha);
        dest.writeString(this.hora);
    }
    public void readFromParcel(Parcel in){
        this.nfactura = in.readInt();
        this.mesa = in.readInt();
        this.submesa = in.readInt();
        this.formapago = in.readString();
        this.codenl = in.readInt();
        this.fecha = in.readString();
        this.hora = in.readString();

    }

    protected ClaseFacturas(Parcel in){
        super();
        readFromParcel(in);
    }

    public  static final Parcelable.Creator<ClaseFacturas> CREATOR = new Parcelable.Creator<ClaseFacturas>(){
        public ClaseFacturas createFromParcel (Parcel in){return new ClaseFacturas(in); }
        public ClaseFacturas[] newArray(int size){ return new ClaseFacturas[size]; }

    };

    @Override
    public int compareTo(ClaseFacturas claseFacturas) {
        int  res;
        Log.d("ClaseFacturas", "compareTo: " + claseFacturas.formapago + " - "+ this.formapago);

        try {
            if (ClaseFacturas.orden == enOrden.hora) {
                res = -1;
                String fechahora1 = ClaseUtils.cambiaFormato(this.fecha, "dd/MM/yyyy", "yyyy-MM-dd") + " " + this.hora;
                String fechahora2 = ClaseUtils.cambiaFormato(claseFacturas.fecha, "dd/MM/yyyy", "yyyy-MM-dd") + " " + claseFacturas.hora;

                //res = this.hora.compareTo(claseFacturas.hora);
                res = fechahora1.compareTo(fechahora2);
                if (ClaseFacturas.asc)
                    return res;
                else
                    return -1 * res;


            } else if (ClaseFacturas.orden == enOrden.pago) {
                String cadena = "EHZXCRP";
                if (this.formapago == null || !cadena.contains(this.formapago.toUpperCase())) {
                    return -0;
                }
                if (claseFacturas.formapago == null || !cadena.contains(claseFacturas.formapago.toUpperCase())) {
                    return -0;
                }

                res = -1;
                try {
                    Log.d("ClaseFacturas", "compareTo: 1");
                    if (cadena.indexOf(this.formapago) > cadena.indexOf(claseFacturas.formapago)) {
                        res = 1;
                    }
                    Log.d("ClaseFacturas", "compareTo: 2");

                    if (ClaseFacturas.asc) {
                        return res;
                    }
                    else {
                        return -1 * res;
                    }
                }
                catch (IllegalArgumentException e1) {
                    return -0;
                }

            } else if (ClaseFacturas.orden == enOrden.mesa) {
                res = -1;
                String mesa1, mesa2;
                if (this.descripcion != null && this.descripcion.trim().length() > 0) {
                    mesa1 = this.descripcion;
                } else {
                    mesa1 = String.valueOf(this.mesa);
                }
                if (claseFacturas.descripcion != null && claseFacturas.descripcion.trim().length() > 0) {
                    mesa2 = claseFacturas.descripcion;
                } else {
                    mesa2 = String.valueOf(claseFacturas.mesa);
                }
                mesa1 += "-" + String.valueOf(this.submesa);
                mesa2 += "-" + String.valueOf(claseFacturas.submesa);

                res = mesa1.compareTo(mesa2);
                if (res == 0) {
                    res = this.hora.compareTo(claseFacturas.hora);
                }

                if (ClaseFacturas.asc)
                    return res;
                else
                    return -1 * res;
            } else {
                res = -1;
                if (this.nfactura > claseFacturas.nfactura) res = 1;
                if (ClaseFacturas.asc)
                    return res;
                else
                    return -1 * res;

            }
        }
        catch (Exception ex) {
            return -1;
        }
    }
}
