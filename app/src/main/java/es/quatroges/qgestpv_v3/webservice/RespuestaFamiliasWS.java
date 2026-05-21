package es.quatroges.qgestpv_v3.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.Alergenos;
import es.quatroges.qgestpv_v3.datos.Familias;

public class RespuestaFamiliasWS extends RespuestaBaseWS
        implements  RespuestaSincro<Familias> {
    @SerializedName("filas")
    @Expose
    private List<Familias> familias = null;

    public List<Familias> getFamilias() {
        return familias;
    }

    public void setFamilias(List<Familias> familias) {
        this.familias = familias;
    }
    @Override
    public List<Familias> getListaRegistros() {
        return this.familias;
    }

}
