package es.quatroges.qgestpv_v3.datos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResGrabaLineasSubmesas implements Parcelable {


    @SerializedName("submesa")
    @Expose
    private int submesa;

    @SerializedName("codenl")
    @Expose
    private int codenl;

    public ResGrabaLineasSubmesas() {

    }

    public ResGrabaLineasSubmesas( int submesa, int codenl) {
        this.submesa = submesa;
        this.codenl = codenl;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(submesa);
        dest.writeInt(codenl);
    }

    public void readFromParcel(Parcel in){
        this.submesa= in.readInt();
        this.codenl= in.readInt();
    }

    protected ResGrabaLineasSubmesas(Parcel in){
        super();
        readFromParcel(in);
    }

    public static final  Creator<ResGrabaLineasSubmesas> CREATOR = new Creator<ResGrabaLineasSubmesas>(){
        public ResGrabaLineasSubmesas createFromParcel (Parcel in){
            return new ResGrabaLineasSubmesas(in);
        }
        public ResGrabaLineasSubmesas[] newArray(int size){return new ResGrabaLineasSubmesas[size];}
    };
}
