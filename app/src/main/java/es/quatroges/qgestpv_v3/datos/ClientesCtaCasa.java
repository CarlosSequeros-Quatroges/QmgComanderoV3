package es.quatroges.qgestpv_v3.datos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import es.quatroges.qgestpv_v3.utils.ClaseUtils;

@Entity (tableName = "clt_art")
public class ClientesCtaCasa {
    @SerializedName("rowid")
    @Expose
    @NonNull
    @PrimaryKey
    private Integer rowid;

    @SerializedName("codcli")
    @Expose
    @NonNull
    private String codcli;

    @SerializedName("nombre")
    @Expose
    private String nombre;

    private String md5;

    public ClientesCtaCasa(){

    }

    @Ignore
    public ClientesCtaCasa(Integer rowid, String codcli, String nombre){
        this.rowid = rowid;
        this.codcli = codcli;
        this.nombre = nombre;
        this.md5 = ClaseUtils.md5(String.valueOf(codcli)+nombre);
    }

    @NonNull
    public Integer getRowid() {
        return rowid;
    }

    public void setRowid(@NonNull Integer rowid) {
        this.rowid = rowid;
    }

    @NonNull
    public String getCodcli() {
        return codcli;
    }

    public void setCodcli(@NonNull String codcli) {
        this.codcli = codcli;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void calculaMD5(){
        this.md5 = ClaseUtils.md5(String.valueOf(this.codcli)+this.nombre);
    }
}
