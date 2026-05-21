package es.quatroges.qgestpv_v3.datos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import es.quatroges.qgestpv_v3.utils.ClaseUtils;

@Entity (tableName = "hora_comidas")
public class Hora_Comidas {
    @SerializedName("codigo")
    @Expose
    @NonNull
    @PrimaryKey
    private int codigo;

    @SerializedName("desde_hora")
    @Expose
    private String desde_hora;

    @SerializedName("hasta_hora")
    @Expose
    private String hasta_hora;

    @SerializedName("tipo")
    @Expose
    private String tipo;

    @SerializedName("codtpv")
    @Expose
    private String codtpv;



    private String md5;

    public Hora_Comidas(){

    }

    @Ignore
    public Hora_Comidas(int codigo, String desde_hora, String hasta_hora, String tipo, String codtpv){
        this.codigo = codigo;
        this.desde_hora = desde_hora;
        this.hasta_hora = hasta_hora;
        this.tipo = tipo;
        this.codtpv = codtpv;
        this.calculaMD5();
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDesde_hora() {
        return desde_hora;
    }

    public void setDesde_hora(String desde_hora) {
        this.desde_hora = desde_hora;
    }

    public String getHasta_hora() {
        return hasta_hora;
    }

    public void setHasta_hora(String hasta_hora) {
        this.hasta_hora = hasta_hora;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCodtpv() {
        return codtpv;
    }

    public void setCodtpv(String codtpv) {
        this.codtpv = codtpv;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void calculaMD5() {
        md5 = ClaseUtils.md5(String.valueOf(codigo)+desde_hora+hasta_hora+tipo+codtpv);
    }

}
