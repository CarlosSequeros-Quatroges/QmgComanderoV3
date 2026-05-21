package es.quatroges.qgestpv_v3.webservice;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.ActualizaPax;

public class RequestActualizaPaxWS {

    @SerializedName("codigoEmpresa")
    @Expose
    private String codigoEmpresa;

    @SerializedName("IDTablet")
    @Expose
    private String idTablet;


    @SerializedName("pax")
    @Expose
    private List<ActualizaPax> pax=null;


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


    public List<ActualizaPax> getPax() {
        return pax;
    }

    public void setPax(List<ActualizaPax> pax) {
        this.pax = pax;
    }
}

