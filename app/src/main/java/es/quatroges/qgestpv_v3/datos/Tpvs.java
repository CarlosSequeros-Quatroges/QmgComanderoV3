package es.quatroges.qgestpv_v3.datos;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import es.quatroges.qgestpv_v3.utils.ClaseUtils;

@Entity (tableName = "tpvs")
public class Tpvs {
    @SerializedName("rowid")
    @Expose
    @NonNull
    @PrimaryKey
    private Integer rowid;

    @SerializedName("codtpv")
    @Expose
    @NonNull
    private String codtpv;

    @SerializedName("descripcion")
    @Expose
    private String descripcion;

    @SerializedName("numero_mesas")
    @Expose
    @ColumnInfo(name = "numero_mesas")
    private int numeroMesas;

    @SerializedName("tmenu")
    @Expose
    private int tmenu;

    @SerializedName("pedir_pax")
    @Expose
    private String pedir_pax;


    private String md5;

    public Tpvs(){

    }

    @Ignore
    public Tpvs( Integer rowid, String codtpv, String descripcion, int tmenu, int numeroMesas){
        this.rowid = rowid;
        this.codtpv= codtpv;
        this.descripcion = descripcion;
        this.tmenu = tmenu;
        this.numeroMesas = numeroMesas;
        this.md5 = ClaseUtils.md5(codtpv+descripcion+String.valueOf(tmenu)+String.valueOf(numeroMesas));

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getNumeroMesas() {
        return numeroMesas;
    }

    public void setNumeroMesas(int numeroMesas) {
        this.numeroMesas = numeroMesas;
    }

    public int getTmenu() {
        return tmenu;
    }

    public void setTmenu(int tmenu) {
        this.tmenu = tmenu;
    }

    public String getPedir_pax() {
        return pedir_pax;
    }

    public void setPedir_pax(String pedir_pax) {
        this.pedir_pax = pedir_pax;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void calculaMD5(){
        this.md5 = ClaseUtils.md5(this.codtpv+this.descripcion+String.valueOf(this.tmenu)+String.valueOf(this.numeroMesas));
    }
}
