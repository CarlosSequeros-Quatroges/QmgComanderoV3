package es.quatroges.qgestpv_v3.datos.listas.tpvs;

import android.os.Parcel;
import android.os.Parcelable;

public class ClaseTPVs implements Parcelable {
    public String codtpv;
    public String descripcion;
    public int tmenu;
    public int numeroMesas;

    public ClaseTPVs(String codtpv, String descripcion, int tmenu,int numeroMesas) {
        this.codtpv = codtpv;
        this.descripcion = descripcion;
        this.tmenu = tmenu;
        this.numeroMesas = numeroMesas;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.codtpv);
        dest.writeString(this.descripcion);
        dest.writeInt(this.tmenu);
        dest.writeInt(this.numeroMesas);
    }
    public void readFromParcel(Parcel in){
        this.codtpv = in.readString();
        this.descripcion = in.readString();
        this.tmenu = in.readInt();
        this.numeroMesas = in.readInt();

    }

    protected ClaseTPVs(Parcel in){
        super();
        readFromParcel(in);
    }

    protected  static final  Creator<ClaseTPVs> CREATOR = new Creator<ClaseTPVs>(){
        public ClaseTPVs createFromParcel (Parcel in){
            return new ClaseTPVs(in);
        }
        public ClaseTPVs[] newArray(int size){
            return new ClaseTPVs[size];
        }

    };


    /*
    public static ArrayList<ClaseTPVs> recuperaTPVs(){

        ArrayList<ClaseTPVs>  listaTPVs= new ArrayList<>();
        listaTPVs.clear();
        listaTPVs.add(new ClaseTPVs(1,"BAR PISCINA"));
        listaTPVs.add(new ClaseTPVs(2,"RESTAURANTE"));
        listaTPVs.add(new ClaseTPVs(3,"SPA"));
        return listaTPVs;
    }
    */
}
