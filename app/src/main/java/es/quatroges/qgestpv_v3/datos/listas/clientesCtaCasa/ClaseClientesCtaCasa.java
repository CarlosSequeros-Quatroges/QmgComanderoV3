package es.quatroges.qgestpv_v3.datos.listas.clientesCtaCasa;

import android.os.Parcel;
import android.os.Parcelable;

public class ClaseClientesCtaCasa implements Parcelable {
    public String codigo;
    public String nombre;

    public ClaseClientesCtaCasa(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.codigo);
        dest.writeString(this.nombre);
    }
    public void readFromParcel(Parcel in){
        this.codigo = in.readString();
        this.nombre= in.readString();
    }

    protected ClaseClientesCtaCasa(Parcel in){
        super();
        readFromParcel(in);
    }

    protected  static final  Creator<ClaseClientesCtaCasa> CREATOR = new Creator<ClaseClientesCtaCasa>(){
        public ClaseClientesCtaCasa createFromParcel (Parcel in){
            return new ClaseClientesCtaCasa(in);
        }
        public ClaseClientesCtaCasa[] newArray(int size){
            return new ClaseClientesCtaCasa[size];
        }

    };



}
