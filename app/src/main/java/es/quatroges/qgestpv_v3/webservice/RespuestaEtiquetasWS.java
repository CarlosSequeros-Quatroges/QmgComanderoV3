package es.quatroges.qgestpv_v3.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

import es.quatroges.qgestpv_v3.datos.Etiquetas;

public class RespuestaEtiquetasWS extends RespuestaBaseWS implements  RespuestaSincro<Etiquetas> {
    @SerializedName("filas")
    @Expose
    private List<Etiquetas> etiquetas = null;


    public List<Etiquetas> getetiquetas() {
        return etiquetas;
    }

    @Override
    public List<Etiquetas> getListaRegistros() {
        return this.etiquetas;
    }
}
