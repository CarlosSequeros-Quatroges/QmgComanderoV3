package es.quatroges.qgestpv_v3.datos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import es.quatroges.qgestpv_v3.utils.ClaseUtils;

@Entity (tableName = "usuarios")
public class Usuarios {
    @SerializedName("codigo")
    @Expose
    @NonNull
    @PrimaryKey
    private int codigo;

    @SerializedName("nombre")
    @Expose
    private String nombre;

    @SerializedName("clave")
    @Expose
    private String clave;

    @SerializedName("ntarjeta")
    @Expose
    private String ntarjeta;

    private String md5;

    public Usuarios(){

    }
    @Ignore
    public Usuarios(int codigo, String nombre, String clave, String ntarjeta){
        this.codigo = codigo;
        this.nombre = nombre;
        this.clave = clave;
        this.ntarjeta = ntarjeta;
        this.md5 = ClaseUtils.md5(String.valueOf(codigo)+nombre+clave+ntarjeta);
    }

    @NonNull
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(@NonNull int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNtarjeta() {
        return ntarjeta;
    }

    public void setNtarjeta(String ntarjeta) {
        this.ntarjeta = ntarjeta;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void calculaMD5(){
        this.md5 = ClaseUtils.md5(String.valueOf(this.codigo)+this.nombre+this.clave+this.ntarjeta);
    }
}
