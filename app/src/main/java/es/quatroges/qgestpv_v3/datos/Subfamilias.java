package es.quatroges.qgestpv_v3.datos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import es.quatroges.qgestpv_v3.utils.ClaseUtils;

@Entity (tableName = "subfamilias")
public class Subfamilias {
    @SerializedName("codsub")
    @Expose
    @NonNull
    @PrimaryKey
    private int codsub;

    @SerializedName("descripcion")
    @Expose
    private String descripcion;

    @SerializedName("extras")
    @Expose
    private String extras;



    private String md5;

    public Subfamilias(){

    }

    @Ignore
    public Subfamilias(int codfam, String descripcion, String extras){
        this.codsub = codsub;
        this.descripcion = descripcion;
        this.extras = extras;
        //this.md5 = ClaseUtils.md5(String.valueOf(codigo)+descripcion);
        this.calculaMD5();

    }

    public int getCodsub() {
        return codsub;
    }

    public void setCodsub(int codsub) {
        this.codsub = codsub;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void calculaMD5() {
        md5 = ClaseUtils.md5(String.valueOf(codsub) +  descripcion+extras);
    }

}
