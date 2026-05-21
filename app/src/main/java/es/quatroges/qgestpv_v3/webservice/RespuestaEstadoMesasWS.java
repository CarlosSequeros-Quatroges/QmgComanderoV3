package es.quatroges.qgestpv_v3.webservice;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.EstadoMesas;

public class RespuestaEstadoMesasWS extends RespuestaBaseWS {
    @SerializedName("pedir_pax")
    @Expose
    private String pedir_pax;

    @SerializedName("nmesas")
    @Expose
    private Integer nmesas;
    @SerializedName("mesas")
    @Expose
    private List<EstadoMesas> mesas= null;

    public Integer getNmesas() {
        return nmesas;
    }

    public void setNmesas(Integer nmesas) {
        this.nmesas = nmesas;
    }

    public List<EstadoMesas> getMesas() {
        return mesas;
    }

    public void setMesas(List<EstadoMesas> mesas) {
        this.mesas = mesas;
    }

    public String getPedir_pax() {
        return pedir_pax;
    }

    public void setPedir_pax(String pedir_pax) {
        this.pedir_pax = pedir_pax;
    }
}

