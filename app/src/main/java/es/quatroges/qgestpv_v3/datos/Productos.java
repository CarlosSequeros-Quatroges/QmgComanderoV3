package es.quatroges.qgestpv_v3.datos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import es.quatroges.qgestpv_v3.utils.ClaseUtils;

@Entity (tableName = "productos")
public class Productos {

    @SerializedName("rowid")
    @Expose
    @NonNull
    @PrimaryKey
    private Integer rowid;

    @SerializedName("codmenu")
    @Expose
    @NonNull
    private String codmenu;

    @SerializedName("tmenu")
    @Expose
    @NonNull
    private int tmenu;

    @SerializedName("cabecera")
    @Expose
    @NonNull
    private int cabecera;

    @SerializedName("descripcion")
    @Expose
    private String descripcion;

    @SerializedName("euros")
    @Expose
    private String euros;

    @SerializedName("costo")
    @Expose
    private String costo;

    @SerializedName("mprecios")
    @Expose
    private String mprecios;

    @SerializedName("abrevia")
    @Expose
    private String abrevia;

    @SerializedName("aplicar_hh")
    @Expose
    private String aplicar_hh;

    @SerializedName("tipo")
    @Expose
    private String tipo;

    @SerializedName("aplicar_ti")
    @Expose
    private String aplicar_ti;

    @SerializedName("notas")
    @Expose
    private String notas;

    @SerializedName("alergenos")
    @Expose
    private String alergenos;

    @SerializedName("etiquetas")
    @Expose
    private String etiquetas;

    @SerializedName("orden")
    @Expose
    private String orden;

    @SerializedName("orden_platos")
    @Expose
    private String orden_platos;

//** nuevos campos - migracion de tabla
    @SerializedName("codfam")
    @Expose
    private String codfam;

    @SerializedName("codsub")
    @Expose
    private String codsub;

    @SerializedName("es_extra")
    @Expose
    private String es_extra;

    @SerializedName("ver_extra")
    @Expose
    private String ver_extra;

    @SerializedName("pensiones")
    @Expose
    private String pensiones;;


    private String md5;

    public Productos() {
    }

    @Ignore
    public Productos(Integer rowid, @NonNull String codmenu, @NonNull int tmenu, @NonNull int cabecera, String descripcion, String euros, String costo, String mprecios, String abrevia, String aplicar_hh, String tipo, String aplicar_ti,
                     String notas, String alergenos, String etiquetas, String orden,String orden_platos, String codfam, String codsub, String es_extra, String ver_extra, String pensiones) {
        this.rowid =rowid;
        this.codmenu = codmenu;
        this.tmenu = tmenu;
        this.cabecera = cabecera;
        this.descripcion = descripcion;
        this.euros = euros;
        this.costo = costo;
        this.mprecios = mprecios;
        this.abrevia = abrevia;
        this.aplicar_hh = aplicar_hh;
        this.tipo = tipo;
        this.aplicar_ti = aplicar_ti;
        this.notas = notas;
        this.alergenos = alergenos;
        this.etiquetas = etiquetas;
        this.orden = orden;
        this.orden_platos = orden_platos;

        this.codfam = codfam;
        this.codsub = codsub;
        this.es_extra = es_extra;
        this.ver_extra = ver_extra;
        this.pensiones = pensiones;
        this.calculaMD5();


        //this.md5 = ClaseUtils.md5(String.valueOf(codmenu)+String.valueOf(tmenu)+String.valueOf(cabecera)+descripcion+euros+mprecios+abrevia+aplicar_hh+tipo+aplicar_ti+notas+alergenos+etiquetas+String.valueOf(orden)+String.valueOf(orden_platos)+
        //        String.valueOf(codfam)+String.valueOf(codsub)+es_extra+ver_extra+pensiones);
    }

    @NonNull
    public Integer getRowid() {
        return rowid;
    }

    public void setRowid(@NonNull Integer rowid) {
        this.rowid = rowid;
    }

    @NonNull
    public String getCodmenu() {
        return codmenu;
    }

    public void setCodmenu(@NonNull String codmenu) {
        this.codmenu = codmenu;
    }

    @NonNull
    public int getTmenu() {
        return tmenu;
    }

    public void setTmenu(@NonNull int tmenu) {
        this.tmenu = tmenu;
    }

    @NonNull
    public int getCabecera() {
        return cabecera;
    }

    public void setCabecera(@NonNull int cabecera) {
        this.cabecera = cabecera;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEuros() {
        return euros;
    }

    public void setEuros(String euros) {
        this.euros = euros;
    }

    public String getCosto() {
        if (costo == null)
            costo = "0.00";
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getMprecios() {
        return mprecios;
    }

    public void setMprecios(String mprecios) {
        this.mprecios = mprecios;
    }

    public String getAbrevia() {
        return abrevia;
    }

    public void setAbrevia(String abrevia) {
        this.abrevia = abrevia;
    }

    public String getAplicar_hh() {
        return aplicar_hh;
    }

    public void setAplicar_hh(String aplicar_hh) {
        this.aplicar_hh = aplicar_hh;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAplicar_ti() {
        return aplicar_ti;
    }

    public void setAplicar_ti(String aplicar_ti) {
        this.aplicar_ti = aplicar_ti;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getAlergenos() {
        return alergenos;
    }

    public void setAlergenos(String alergenos) {
        this.alergenos = alergenos;
    }

    public String getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(String etiquetas) {
        this.etiquetas = etiquetas;
    }

   public String getOrden() {
       try {
           return orden;
       }
       catch (Exception e) {
           return "0";
       }

   }

    public void setOrden(String orden) {
        this.orden =orden;
    }

    public String getOrden_platos() {
        try {
            return orden_platos;
        }
        catch (Exception e) {
            return "0";
        }
    }

    public void setOrden_platos(String  orden_platos) {
        this.orden_platos = orden_platos;
    }

    public String getCodfam() {
        return codfam;
    }

    public void setCodfam(String codfam) {
        this.codfam = codfam;
    }

    public String getCodsub() {
        return codsub;
    }

    public void setCodsub(String codsub) {
        this.codsub = codsub;
    }

    public String getEs_extra() {
        return es_extra;
    }

    public void setEs_extra(String es_extra) {
        this.es_extra = es_extra;
    }

    public String getVer_extra() {
        return ver_extra;
    }

    public void setVer_extra(String ver_extra) {
        this.ver_extra = ver_extra;
    }

    public String getPensiones() {
        return pensiones;
    }

    public void setPensiones(String pensiones) {
        this.pensiones = pensiones;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void calculaMD5() {
        md5 = ClaseUtils.md5(String.valueOf(codmenu)+String.valueOf(tmenu)+String.valueOf(cabecera)+descripcion+euros+costo+mprecios+abrevia+aplicar_hh+tipo+aplicar_ti+notas+alergenos+etiquetas+String.valueOf(orden)+String.valueOf(orden_platos)+String.valueOf(codfam)+String.valueOf(codsub)+es_extra+ver_extra+pensiones);
    }

}
