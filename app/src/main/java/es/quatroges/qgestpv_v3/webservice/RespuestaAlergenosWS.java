package es.quatroges.qgestpv_v3.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.Alergenos;
import es.quatroges.qgestpv_v3.datos.Etiquetas;

public class RespuestaAlergenosWS extends RespuestaBaseWS
        implements  RespuestaSincro<Alergenos> {
    @SerializedName("filas")
    @Expose
    private List<Alergenos> alergenos = null;


    public List<Alergenos> getAlergenos() {
        return alergenos;
    }

    public void setAlergenos(List<Alergenos> alergenos) {
        this.alergenos = alergenos;
    }

    @Override
    public List<Alergenos> getListaRegistros() {
        return this.alergenos;
    }
}
