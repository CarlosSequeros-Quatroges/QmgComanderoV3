package es.quatroges.qgestpv_v3.datos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import es.quatroges.qgestpv_v3.utils.ClaseUtils;

@Entity (tableName = "cabeceras")
public class Cabeceras {
    @SerializedName("codigo")
    @Expose
    @NonNull
    @PrimaryKey
    private int codigo;

    @SerializedName("tmenu")
    @Expose
    private int tmenu;

    @SerializedName("descripcion")
    @Expose
    private String descripcion;

    @SerializedName("pos")
    @Expose
    private int pos;

    private String md5;

    public Cabeceras(){

    }

    @Ignore
    public Cabeceras(int codigo, int tmenu, int pos, String descripcion){
        this.codigo = codigo;
        this.tmenu= tmenu;
        this.pos = pos;
        this.descripcion = descripcion;
        this.md5 = ClaseUtils.md5(String.valueOf(codigo)+String .valueOf(tmenu)+String.valueOf(pos)+descripcion);

    }

    @NonNull
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(@NonNull int codigo) {
        this.codigo = codigo;
    }

    public int getTmenu() {
        return tmenu;
    }

    public void setTmenu(int tmenu) {
        this.tmenu = tmenu;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void calculaMD5() {
        md5 = ClaseUtils.md5(String.valueOf(codigo) + String.valueOf(tmenu) + String.valueOf(pos) + descripcion);
    }

}
