package es.quatroges.qgestpv_v3.datos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PensionesSubmesa implements Parcelable {
    @SerializedName("submesa")
    @Expose
    private int submesa;

    @SerializedName("pension")
    @Expose
    private String pension;

    @SerializedName("desc_pension")
    @Expose
    private String descPension;
    @SerializedName("tipo_pension")
    @Expose
    private String tipoPension;
    @SerializedName("desc_tipo_pension")
    @Expose
    private String descTipoPension;

    @SerializedName("hora_apertura")
    @Expose
    private String hora_apertura;


    public PensionesSubmesa(){

    }

    public int getSubmesa() {
        return submesa;
    }

    public void setSubmesa(int submesa) {
        this.submesa = submesa;
    }

    public String getPension() {
        return pension;
    }

    public void setPension(String pension) {
        this.pension = pension;
    }

    public String getDescPension() {
        return descPension;
    }

    public void setDescPension(String descPension) {
        this.descPension = descPension;
    }

    public String getTipoPension() {
        return tipoPension;
    }

    public void setTipoPension(String tipoPension) {
        this.tipoPension = tipoPension;
    }

    public String getDescTipoPension() {
        return descTipoPension;
    }

    public void setDescTipoPension(String descTipoPension) {
        this.descTipoPension = descTipoPension;
    }

    public String getHora_apertura() {
        return hora_apertura;
    }

    public void setHora_apertura(String hora_apertura) {
        this.hora_apertura = hora_apertura;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(submesa);
        dest.writeString(pension);
        dest.writeString(descPension);
        dest.writeString(tipoPension);
        dest.writeString(descTipoPension);
        dest.writeString(hora_apertura);

    }

    public void readFromParcel(Parcel in){
        this.submesa = in.readInt();
        this.pension = in.readString();
        this.descPension = in.readString();
        this.tipoPension = in.readString();
        this.descTipoPension = in.readString();
        this.hora_apertura = in.readString();

    }

    protected PensionesSubmesa(Parcel in){
        super();
        readFromParcel(in);
    }

    public static final  Parcelable.Creator<PensionesSubmesa> CREATOR = new Parcelable.Creator<PensionesSubmesa>(){
        public PensionesSubmesa createFromParcel (Parcel in){
            return new PensionesSubmesa(in);
        }
        public PensionesSubmesa[] newArray(int size){return new PensionesSubmesa[size];}
    };

}
