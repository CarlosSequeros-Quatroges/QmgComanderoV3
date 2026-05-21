package es.quatroges.qgestpv_v3.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.Familias;
import es.quatroges.qgestpv_v3.datos.Subfamilias;

public class RespuestaSubFamiliasWS extends RespuestaBaseWS
        implements  RespuestaSincro<Subfamilias>	{
    @SerializedName("filas")
    @Expose
    private List<Subfamilias> subfamilias = null;

    public List<Subfamilias> getSubfamilias() {
        return subfamilias;
    }

    public void setSubfamilias(List<Subfamilias> subfamilias) {
        this.subfamilias = subfamilias;
    }
        @Override
        public List<Subfamilias> getListaRegistros() {
            return this.subfamilias;
        }
}
