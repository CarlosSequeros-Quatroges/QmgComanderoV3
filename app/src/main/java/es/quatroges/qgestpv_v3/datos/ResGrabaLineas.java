package es.quatroges.qgestpv_v3.datos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResGrabaLineas implements Parcelable {

    @SerializedName("mesa")
    @Expose
    private int mesa;

    @SerializedName("submesa")
    @Expose
    private int submesa;

    @SerializedName("nlinea")
    @Expose
    private int nlinea;

    @SerializedName("error")
    @Expose
    private int error;

    @SerializedName("descripcion")
    @Expose
    private String descripcion;

    public ResGrabaLineas() {

    }

    public ResGrabaLineas(int mesa,int submesa, int nlinea, int error, String descripcion) {
        this.mesa = mesa;
        this.submesa = submesa;
        this. nlinea = nlinea;
        this.error = error;
        this.descripcion = descripcion;
    }


    public int getMesa() {
        return mesa;
    }

    public void setMesa(int mesa) {
        this.mesa = mesa;
    }

    public int getSubmesa() {
        return submesa;
    }

    public void setSubmesa(int submesa) {
        this.submesa = submesa;
    }

    public int getNlinea() {
        return nlinea;
    }

    public void setNlinea(int nlinea) {
        this.nlinea = nlinea;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(mesa);
        dest.writeInt(submesa);
        dest.writeInt(nlinea);
        dest.writeInt(error);
        dest.writeString(descripcion);
    }

    public void readFromParcel(Parcel in){
        this.mesa= in.readInt();
        this.submesa= in.readInt();
        this.nlinea= in.readInt();
        this.error= in.readInt();
        this.descripcion = in.readString();
    }

    protected ResGrabaLineas(Parcel in){
        super();
        readFromParcel(in);
    }

    public static final  Creator<ResGrabaLineas> CREATOR = new Creator<ResGrabaLineas>(){
        public ResGrabaLineas createFromParcel (Parcel in){
            return new ResGrabaLineas(in);
        }
        public ResGrabaLineas[] newArray(int size){return new ResGrabaLineas[size];}
    };
}
