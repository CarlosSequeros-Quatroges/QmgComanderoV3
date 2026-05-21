package es.quatroges.qgestpv_v3.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.Facturas;


public class RespuestaTablaWS extends RespuestaBaseWS {
    @SerializedName("nfacturas")
    @Expose
    private Integer nfacturas;
    @SerializedName("facturas")
    @Expose
    private List<Facturas> facturas = null;


    public Integer getNfacturas() {
        return nfacturas;
    }

    public void setNfacturas(Integer nfacturas) {
        this.nfacturas = nfacturas;
    }

    public List<Facturas> getfacturas() {
        return facturas;
    }

    public void setfacturas(List<Facturas> facturas) {
        this.facturas = facturas;
    }
}
