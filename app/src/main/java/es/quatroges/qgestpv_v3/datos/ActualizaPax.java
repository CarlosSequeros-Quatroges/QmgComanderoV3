package es.quatroges.qgestpv_v3.datos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActualizaPax implements Parcelable {

    @SerializedName("submesa")
    @Expose
    private int submesa;

    @SerializedName("codenl")
    @Expose
    private int codenl;

    @SerializedName("npax")
    @Expose
    private int npax;

    @SerializedName("habitacion")
    @Expose
    private String habitacion;


    public ActualizaPax(int submesa, int codenl, int npax, String habitacion) {
        this.submesa = submesa;
        this.codenl= codenl;
        this.npax = npax;
        this.habitacion = habitacion;
    }

    public ActualizaPax() {
        this.submesa = 0;
        this.codenl = 0;
        this.npax = 0;
        this.habitacion = "";
    }

    public int getSubmesa() {
        return submesa;
    }

    public void setSubmesa(int submesa) {
        this.submesa = submesa;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(submesa);
        dest.writeInt(codenl);
        dest.writeInt(npax);
        dest.writeString(habitacion);
    }

    public void readFromParcel(Parcel in){
        this.submesa = in.readInt();
        this.codenl = in.readInt();
        this.npax = in.readInt();
        this.habitacion = in.readString();
    }

    protected ActualizaPax(Parcel in){
        super();
        readFromParcel(in);
    }

    public static final  Creator<ActualizaPax> CREATOR = new Creator<ActualizaPax>(){
        public ActualizaPax createFromParcel (Parcel in){
            return new ActualizaPax(in);
        }
        public ActualizaPax[] newArray(int size){return new ActualizaPax[size];}
    };
}
