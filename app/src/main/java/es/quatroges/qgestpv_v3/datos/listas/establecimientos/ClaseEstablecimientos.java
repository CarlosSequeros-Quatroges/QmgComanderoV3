package es.quatroges.qgestpv_v3.datos.listas.establecimientos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaseEstablecimientos implements Parcelable {
    @SerializedName("codemp")
    @Expose
    public String codigo;
    @SerializedName("nombre")
    @Expose
    public String nombre;

    public ClaseEstablecimientos(String codigo, String nombre) {
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

    protected ClaseEstablecimientos(Parcel in){
        super();
        readFromParcel(in);
    }

    protected  static final  Creator<ClaseEstablecimientos> CREATOR = new Creator<ClaseEstablecimientos>(){
        public ClaseEstablecimientos createFromParcel (Parcel in){
            return new ClaseEstablecimientos(in);
        }
        public ClaseEstablecimientos[] newArray(int size){
            return new ClaseEstablecimientos[size];
        }

    };



}
