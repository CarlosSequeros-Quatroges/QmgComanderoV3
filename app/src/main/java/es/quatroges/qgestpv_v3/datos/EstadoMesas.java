package es.quatroges.qgestpv_v3.datos;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EstadoMesas  implements Parcelable {
    @SerializedName("mesa")
    @Expose
    private int mesa;

    @SerializedName("submesa")
    @Expose
    private int  submesa;

    @SerializedName("estado")
    @Expose
    private String estado;

    @SerializedName("importe")
    @Expose
    private String importe;

    @SerializedName("codenl")
    @Expose
    private int codenl;

    @SerializedName("npax")
    @Expose
    private int npax;

    @SerializedName("habitacion")
    @Expose
    private String habitacion;

    @SerializedName("nfactura")
    @Expose
    private String nfactura;

    @SerializedName("pension")
    @Expose
    private String pension;


    @SerializedName("apertura")
    @Expose
    private String apertura;


    public EstadoMesas(){

    }

    @Ignore
    public EstadoMesas(int mesa, int submesa, String estado, String importe, int codenl, int npax, String habitacion , String nfactura, String pension , String apertura){
        this.mesa= mesa;
        this.submesa= submesa;
        this.estado= estado;
        this.importe = importe;
        this.codenl = codenl;
        this.npax = npax;
        this.habitacion = habitacion;
        this.nfactura = nfactura;
        this.pension = pension;
        this.apertura = apertura;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public int getCodenl() {
        return codenl;
    }

    public void setCodenl(int codenl) {
        this.codenl = codenl;
    }

    public int getNpax() {
        return npax;
    }

    public void setNpax(int npax) {
        this.npax = npax;
    }

    public String getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(String habitacion) {
        this.habitacion = habitacion;
    }

    public String getNfactura() {
        return nfactura;
    }

    public void setNfactura(String nfactura) {
        this.nfactura = nfactura;
    }

    public String getPension() {
        return pension;
    }

    public void setPension(String pension) {
        this.pension = pension;
    }

    public String getApertura() {
        return apertura;
    }

    public void setApertura(String apertura) {
        this.apertura = apertura;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mesa);
        dest.writeInt(submesa);
        dest.writeString(estado);
        dest.writeString(importe);
        dest.writeInt(codenl);
        dest.writeInt(npax);
        dest.writeString(habitacion);
        dest.writeString(nfactura);
        dest.writeString(pension);
        dest.writeString(apertura);
    }

    public void readFromParcel(Parcel in){
        this.mesa= in.readInt();
        this.submesa= in.readInt();
        this.estado= in.readString();
        this.importe=in.readString();
        this.codenl = in.readInt();
        this.npax = in.readInt();
        this.habitacion = in.readString();
        this.nfactura= in.readString();
        this.pension= in.readString();
        this.apertura= in.readString();
    }

    protected EstadoMesas(Parcel in){
        super();
        readFromParcel(in);
    }

    public static final  Parcelable.Creator<EstadoMesas> CREATOR = new Parcelable.Creator<EstadoMesas>(){
        public EstadoMesas createFromParcel (Parcel in){
            return new EstadoMesas(in);
        }
        public EstadoMesas[] newArray(int size){return new EstadoMesas[size];}
    };
}
