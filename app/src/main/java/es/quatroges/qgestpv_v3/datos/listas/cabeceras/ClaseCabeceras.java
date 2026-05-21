package es.quatroges.qgestpv_v3.datos.listas.cabeceras;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.R;

public class ClaseCabeceras  implements Parcelable {
    public int codigo;
    public String descripcion;
    public int color;

    public ClaseCabeceras(int codigo, String descripcion, int color) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.color = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.codigo);
        dest.writeInt(this.color);
        dest.writeString(this.descripcion);
    }
    public void readFromParcel(Parcel in){
        this.codigo = in.readInt();
        this.color = in.readInt();
        this.descripcion = in.readString();
    }

    protected ClaseCabeceras(Parcel in){
        super();
        readFromParcel(in);
    }

    protected  static final  Parcelable.Creator<ClaseCabeceras> CREATOR = new Parcelable.Creator<ClaseCabeceras>(){
        public ClaseCabeceras createFromParcel (Parcel in){
            return new ClaseCabeceras(in);
        }
        public ClaseCabeceras[] newArray(int size){
            return new ClaseCabeceras[size];
        }

    };


    public static ArrayList<ClaseCabeceras> recuperaCabeceras(int tpv, Context context){
        int rojo = ContextCompat.getColor(context,R.color.rojo);
        int  verde = ContextCompat.getColor(context,R.color.verde);
        int azul = ContextCompat.getColor(context,R.color.textoAzul);


        ArrayList<ClaseCabeceras>  listaCabeceras = new ArrayList<>();
        listaCabeceras.clear();
        listaCabeceras.add(new ClaseCabeceras(1,"BEBIDAS",rojo));
        listaCabeceras.add(new ClaseCabeceras(2,"SNACKS",verde));
        listaCabeceras.add(new ClaseCabeceras(3,"BEBIDAS2",azul));
        listaCabeceras.add(new ClaseCabeceras(4,"SNACKS2",rojo));
        listaCabeceras.add(new ClaseCabeceras(5,"BEBIDAS3",verde));
        listaCabeceras.add(new ClaseCabeceras(6,"SNACKS3",azul));
        return listaCabeceras;
    }
}
