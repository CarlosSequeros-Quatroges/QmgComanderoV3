package es.quatroges.qgestpv_v3.datos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import es.quatroges.qgestpv_v3.utils.ClaseUtils;

@Entity (tableName = "alergenos")
public class Alergenos {
    @SerializedName("codigo")
    @Expose
    @NonNull
    @PrimaryKey
    private int codigo;

    @SerializedName("descripcion")
    @Expose
    private String descripcion;

    @Ignore
    private boolean checked;

    private String md5;

    public Alergenos(){

    }

    @Ignore
    public Alergenos(int codigo, String descripcion){
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.md5 = ClaseUtils.md5(String.valueOf(codigo)+descripcion);

    }

    @NonNull
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(@NonNull int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void calculaMD5() {
        md5 = ClaseUtils.md5(String.valueOf(codigo) +  descripcion);
    }

}
