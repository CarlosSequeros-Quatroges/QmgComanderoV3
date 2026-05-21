package es.quatroges.qgestpv_v3.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.Cabeceras;

public class RespuestaCabecerasWS extends RespuestaBaseWS
        implements  RespuestaSincro<Cabeceras>

{
    @SerializedName("filas")
    @Expose
    private List<Cabeceras> cabeceras = null;

    public List<Cabeceras> getCabeceras() {
        return cabeceras;
    }

    public void setCabeceras(List<Cabeceras> cabeceras) {
        this.cabeceras = cabeceras;
    }

    @Override
    public List<Cabeceras> getListaRegistros() {
        return this.cabeceras;
    }
}
