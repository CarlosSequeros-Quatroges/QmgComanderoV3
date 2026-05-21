package es.quatroges.qgestpv_v3.webservice;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestTraspasaMesaWS {

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

    @SerializedName("mesadest")
    @Expose
    private String mesadest;

    @SerializedName("submesas")
    @Expose
    private List<String> submesas=null;

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

    public String getMesadest() {
        return mesadest;
    }

    public void setMesadest(String mesadest) {
        this.mesadest = mesadest;
    }

    public List<String> getSubmesas() {
        return submesas;
    }

    public void setSubmesas(List<String> submesas) {
        this.submesas = submesas;
    }
}

