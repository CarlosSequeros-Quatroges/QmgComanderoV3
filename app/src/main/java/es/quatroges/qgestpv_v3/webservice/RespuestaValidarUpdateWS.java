package es.quatroges.qgestpv_v3.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.datos.listas.establecimientos.ClaseEstablecimientos;

public class RespuestaValidarUpdateWS extends RespuestaBaseWS {
    @SerializedName("pedirclave")
    @Expose
    private String pedirclave;
    @SerializedName("ordenplatos")
    @Expose
    private String ordenplatos;
    @SerializedName("vta_cli")
    @Expose
    private String tipoCuentaCasa;

    @SerializedName("igic")
    @Expose
    private String igic;

    @SerializedName("tipo_igic")
    @Expose
    private String tipo_igic;

    @SerializedName("pedir_firma")
    @Expose
    private String pedir_firma;

    @SerializedName("appfile")
    @Expose
    private String appfile;

    @SerializedName("hoteles_aux")
    @Expose
    private ArrayList<ClaseEstablecimientos> hoteles_aux;



    public String getPedirclave() {
        return pedirclave;
    }

    public void setPedirclave(String pedirclave) {
        this.pedirclave = pedirclave;
    }

    public String getOrdenplatos() {
        return ordenplatos;
    }

    public void setOrdenplatos(String ordenplatos) {
        this.ordenplatos = ordenplatos;
    }

    public String getIgic() {
        return igic;
    }

    public void setIgic(String igic) {
        this.igic = igic;
    }

    public String getTipo_igic() {
        return tipo_igic;
    }

    public void setTipo_igic(String tipo_igic) {
        this.tipo_igic = tipo_igic;
    }

    public String getTipoCuentaCasa() {
        return tipoCuentaCasa;
    }

    public void setTipoCuentaCasa(String tipoCuentaCasa) {
        this.tipoCuentaCasa = tipoCuentaCasa;
    }

    public String getPedir_firma() {
        return pedir_firma;
    }

    public void setPedir_firma(String pedir_firma) {
        this.pedir_firma = pedir_firma;
    }

    public String getAppfile() {
        return appfile;
    }

    public void setAppfile(String appfile) {
        this.appfile = appfile;
    }

    public ArrayList<ClaseEstablecimientos> getHoteles_aux() {
        return hoteles_aux;
    }

    public void setHoteles_aux(ArrayList<ClaseEstablecimientos> hoteles_aux) {
        this.hoteles_aux = hoteles_aux;
    }
}
