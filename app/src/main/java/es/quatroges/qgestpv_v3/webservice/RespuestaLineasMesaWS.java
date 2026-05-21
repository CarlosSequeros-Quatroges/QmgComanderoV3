package es.quatroges.qgestpv_v3.webservice;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.LineasVenta;
import es.quatroges.qgestpv_v3.datos.PensionesSubmesa;

public class RespuestaLineasMesaWS extends RespuestaBaseWS {

    @SerializedName("nlineas")
    @Expose
    private Integer nlineas;
    @SerializedName("lineas")
    @Expose
    private List<LineasVenta> lineas= null;

    @SerializedName("pensiones")
    @Expose
    private List<PensionesSubmesa> pensiones= null;


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

    public List<PensionesSubmesa> getPensiones() {
        return pensiones;
    }

    public void setPensiones(List<PensionesSubmesa> pensiones) {
        this.pensiones = pensiones;
    }

}

