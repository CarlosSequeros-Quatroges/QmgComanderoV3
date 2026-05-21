package es.quatroges.qgestpv_v3.datos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import es.quatroges.qgestpv_v3.utils.ClaseUtils;

@Entity (tableName = "nom_mesas")
public class Nom_Mesas {
    @SerializedName("rowid")
    @Expose
    @NonNull
    @PrimaryKey
    private Integer rowid;

    @SerializedName("codtpv")
    @Expose
    @NonNull
    private String codtpv;

    @SerializedName("numero")
    @Expose
    @NonNull
    private int numero;

    @SerializedName("descripcion")
    @Expose
    private String descripcion;


    @SerializedName("grupo")
    @Expose
    private int grupo;

    private String md5;

    public Nom_Mesas(){

    }

    @Ignore
    public Nom_Mesas(Integer rowid, String codtpv, int numero, String descripcion, int grupo ){
        this.rowid = rowid;
        this.codtpv= codtpv;
        this.numero = numero;
        this.descripcion = descripcion;
        this.grupo = grupo;
        this.md5 = ClaseUtils.md5(codtpv+String.valueOf(numero)+descripcion+String.valueOf(grupo));

    }

    @NonNull
    public Integer getRowid() {
        return rowid;
    }

    public void setRowid(@NonNull Integer rowid) {
        this.rowid = rowid;
    }

    @NonNull
    public String getCodtpv() {
        return codtpv;
    }

    public void setCodtpv(@NonNull String codtpv) {
        this.codtpv = codtpv;
    }

    @NonNull
    public int getNumero() {
        return numero;
    }

    public void setNumero(@NonNull int numero) {
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getGrupo() {
        return grupo;
    }

    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void calculaMD5(){
        this.md5 = ClaseUtils.md5(this.codtpv+String.valueOf(this.numero)+this.descripcion+String.valueOf(this.grupo));
    }

}
