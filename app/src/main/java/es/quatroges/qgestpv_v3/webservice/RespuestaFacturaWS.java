package es.quatroges.qgestpv_v3.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.LineasVenta;
import es.quatroges.qgestpv_v3.datos.listas.facturas.ClaseFacturaCabecera;

public class RespuestaFacturaWS extends RespuestaBaseWS {

    @SerializedName("empresa")
    @Expose
    private String empresa = null;
    @SerializedName("cif")
    @Expose
    private String cif = null;
    @SerializedName("mesa")
    @Expose
    private String mesa = null;
    @SerializedName("submesa")
    @Expose
    private String submesa = null;
    @SerializedName("codusu")
    @Expose
    private String codusu = null;


    @SerializedName("cabecera")
    @Expose
    private ClaseFacturaCabecera cabecera = null;
    @SerializedName("nlineas")
    @Expose
    private Integer nlineas;
    @SerializedName("lineas")
    @Expose
    private  List<LineasVenta> lineas;
    @SerializedName("firma")
    @Expose
    private String firma;


    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public String getSubmesa() {
        return submesa;
    }

    public void setSubmesa(String submesa) {
        this.submesa = submesa;
    }

    public String getCodusu() {
        return codusu;
    }

    public void setCodusu(String codusu) {
        this.codusu = codusu;
    }

    public ClaseFacturaCabecera getCabecera() {
        return cabecera;
    }

    public void setCabecera(ClaseFacturaCabecera cabecera) {
        this.cabecera = cabecera;
    }

    public Integer getNlineas() {
        return nlineas;
    }

    public void setNlineas(Integer nlineas) {
        this.nlineas = nlineas;
    }

    public List<LineasVenta> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineasVenta> lineas) {
        this.lineas = lineas;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }
}
