package es.quatroges.qgestpv_v3.datos.listas.productos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public class ClaseProductos implements Parcelable, Cloneable, Comparable<ClaseProductos> {
    public enum enOrdenProductos {
        CODIGO,NOMBRE, ORDEN_PLATOS;

    }
    public static enOrdenProductos ordenCodigo;
    public String codmenu;
    public int tmenu;
    public int cabecera;
    public String descripcion;
    public String euros;
    public String costo;
    public String mprecios;
    public String abrevia;
    public String aplicar_hh;
    public String tipo;
    public String aplicar_ti;
    public int color;
    public String notas;
    public String alergenos;
    public String etiquetas;
    public int orden;
    public int orden_platos;

    //añadir campos nuevos
    public String codfam;
    public String codsub;
    public String es_extra;
    public String ver_extra;
    public String pensiones;;





    public ClaseProductos(String codmenu, int tmenu,int cabecera,  String descripcion, String euros, String costo, String mprecios, String abrevia, String aplicar_hh, String tipo, String aplicar_ti, int color,
        String notas, String alergenos, String etiquetas, int orden, int orden_platos,
        String codfam, String codsub, String es_extra, String ver_extra, String pensiones){

        this.codmenu = codmenu;
        this.tmenu = tmenu;
        this.cabecera = cabecera;
        this.descripcion = descripcion;
        this.euros = euros;
        this.costo = costo;
        this.mprecios = mprecios;
        this.abrevia = abrevia;
        this.aplicar_hh = aplicar_hh;
        this.tipo = tipo;
        this.aplicar_ti = aplicar_ti;
        this.color = color;
        this.notas = notas;
        this.alergenos = alergenos;
        this.etiquetas = etiquetas;
        this.orden = orden;
        this.orden_platos = orden_platos;
        this.codfam = codfam;
        this.codsub = codsub;
        this.es_extra = es_extra;
        this.ver_extra = ver_extra;
        this.pensiones = pensiones;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.codmenu);
        dest.writeInt(this.tmenu);
        dest.writeInt(this.cabecera);
        dest.writeString(this.descripcion);
        dest.writeString(this.euros);
        dest.writeString(this.costo);
        dest.writeString(this.mprecios);
        dest.writeString(this.abrevia);
        dest.writeString(this.aplicar_hh);
        dest.writeString(this.tipo);
        dest.writeString(this.aplicar_ti);
        dest.writeInt(this.color);
        dest.writeString(this.notas);
        dest.writeString(this.alergenos);
        dest.writeString(this.etiquetas);
        dest.writeInt(this.orden);
        dest.writeInt(this.orden_platos);
        dest.writeString(this.codfam);
        dest.writeString(this.codsub);
        dest.writeString(this.es_extra);
        dest.writeString(this.ver_extra);
        dest.writeString(this.pensiones);

    }

    public void readFromParcel(Parcel in){
        this.codmenu = in.readString();
        this.tmenu = in.readInt();
        this.cabecera = in.readInt();
        this.descripcion = in.readString();
        this.euros = in.readString();
        this.costo= in.readString();
        this.mprecios = in.readString();
        this.abrevia = in.readString();
        this.aplicar_hh = in.readString();
        this.tipo = in.readString();
        this.aplicar_ti = in.readString();
        this.color = in.readInt();
        this.notas = in.readString();
        this.alergenos = in.readString();
        this.etiquetas = in.readString();
        this.orden = in.readInt();
        this.orden_platos = in.readInt();
        this.codfam = in.readString();
        this.codsub = in.readString();
        this.es_extra = in.readString();
        this.ver_extra = in.readString();
        this.pensiones = in.readString();
    }

    protected ClaseProductos(Parcel in){
        super();
        readFromParcel(in);
    }

    protected  static final  Parcelable.Creator<ClaseProductos> CREATOR = new Parcelable.Creator<ClaseProductos>(){
        public ClaseProductos createFromParcel (Parcel in){
            return new ClaseProductos(in);
        }
        public ClaseProductos[] newArray(int size){
            return new ClaseProductos[size];
        }

    };

    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch( CloneNotSupportedException e )
        {
            return null;
        }
    }

    @Override
    public int compareTo(ClaseProductos claseProductos) {
        String a;
        String b;
        if (ClaseProductos.ordenCodigo == enOrdenProductos.CODIGO) {
            a = ClaseUtils.padLeft(String.valueOf(this.cabecera), "0", 2) +
                    ClaseUtils.padLeft(String.valueOf(this.orden), "0", 6);

            b = ClaseUtils.padLeft(String.valueOf(claseProductos.cabecera), "0", 2) +
                    ClaseUtils.padLeft(String.valueOf(claseProductos.orden), "0", 6);

            return a.compareTo(b);
        }
        else if (ClaseProductos.ordenCodigo == enOrdenProductos.NOMBRE) {

            return this.descripcion.toLowerCase().compareTo(claseProductos.descripcion.toLowerCase());
        }
        else {
            if (this.orden_platos < claseProductos.orden_platos) {
                return -1;
            }
            else if (this.orden_platos > claseProductos.orden_platos) {
                return 1;
            }
            else {
                return this.descripcion.toLowerCase().compareTo(claseProductos.descripcion.toLowerCase());
            }
        }
    }

}
