package es.quatroges.qgestpv_v3.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class ClaseItemFiltro  implements Parcelable , Comparable<ClaseItemFiltro>{
    public int codigo;
    public String descripcion;
    public boolean checked;

    public ClaseItemFiltro(int codigo, String descripcion, boolean checked) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.checked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.codigo);
        dest.writeString(this.descripcion);
        dest.writeByte((byte) (checked ? 1: 0));
    }
    public void readFromParcel(Parcel in){
        this.codigo = in.readInt();
        this.descripcion = in.readString();
        this.checked = in.readByte() != 0;
    }

    protected ClaseItemFiltro(Parcel in){
        super();
        readFromParcel(in);
    }

    protected  static final  Parcelable.Creator<ClaseItemFiltro> CREATOR = new Parcelable.Creator<ClaseItemFiltro>(){
        public ClaseItemFiltro createFromParcel (Parcel in){
            return new ClaseItemFiltro(in);
        }
        public ClaseItemFiltro[] newArray(int size){
            return new ClaseItemFiltro[size];
        }

    };

    @Override
    public int compareTo(ClaseItemFiltro claseItemFiltro) {
        return this.descripcion.toLowerCase().compareTo(claseItemFiltro.descripcion.toLowerCase());
    }
}

