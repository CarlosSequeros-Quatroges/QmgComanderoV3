package es.quatroges.qgestpv_v3.datos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import es.quatroges.qgestpv_v3.utils.ClaseUtils;

@Entity (tableName = "familias")
public class Familias {
    @SerializedName("codfam")
    @Expose
    @NonNull
    @PrimaryKey
    private int codfam;

    @SerializedName("descripcion")
    @Expose
    private String descripcion;


    private String md5;

    public Familias(){

    }

    @Ignore
    public Familias(int codfam, String descripcion){
        this.codfam = codfam;
        this.descripcion = descripcion;
        //this.md5 = ClaseUtils.md5(String.valueOf(codigo)+descripcion);
        this.calculaMD5();

    }

    public int getCodfam() {
        return codfam;
    }

    public void setCodfam(int codfam) {
        this.codfam = codfam;
    }

    @NonNull

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
        md5 = ClaseUtils.md5(String.valueOf(codfam) +  descripcion);
    }

}
