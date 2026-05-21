package es.quatroges.qgestpv_v3.webservice;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.ActualizaPax;
import es.quatroges.qgestpv_v3.datos.GrabaLineasVenta;

public class RequestGrabaLineasWS {

    @SerializedName("codigoEmpresa")
    @Expose
    private String codigoEmpresa;

    @SerializedName("IDTablet")
    @Expose
    private String idTablet;

    @SerializedName("tpv")
    @Expose
    private String tpv;

    @SerializedName("mesa")
    @Expose
    private String mesa;

    @SerializedName("submesas")
    @Expose
    private List<ActualizaPax> submesas;

    @SerializedName("lineas")
    @Expose
    private List<GrabaLineasVenta> lineas =null;

    @SerializedName("imprimir")
    @Expose
    private String imprimir;

    @SerializedName("camarero")
    @Expose
    private String camarero;


    public String getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(String codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public String getIdTablet() {
        return idTablet;
    }

    public void setIdTablet(String idTablet) {
        this.idTablet = idTablet;
    }

    public String getTpv() {
        return tpv;
    }

    public void setTpv(String tpv) {
        this.tpv = tpv;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public List<ActualizaPax> getSubmesas() {
        return submesas;
    }

    public void setSubmesas(List<ActualizaPax> submesas) {
        this.submesas = submesas;
    }

    public List<GrabaLineasVenta> getLineas() {
        return lineas;
    }

    public void setLineas(List<GrabaLineasVenta> lineas) {
        this.lineas = lineas;
    }

    public String getImprimir() {
        return imprimir;
    }

    public void setImprimir(String imprimir) {
        this.imprimir = imprimir;
    }

    public String getCamarero() {
        return camarero;
    }

    public void setCamarero(String camarero) {
        this.camarero = camarero;
    }
}

