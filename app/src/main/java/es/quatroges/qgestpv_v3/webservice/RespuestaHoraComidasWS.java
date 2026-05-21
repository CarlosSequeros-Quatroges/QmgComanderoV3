package es.quatroges.qgestpv_v3.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.Hora_Comidas;

public class RespuestaHoraComidasWS extends RespuestaBaseWS
        implements  RespuestaSincro<Hora_Comidas>
{
    @SerializedName("filas")
    @Expose
    private List<Hora_Comidas> horaComidas = null;

    public List<Hora_Comidas> getHoraComidas() {
        return horaComidas;
    }

    public void setHoraComidas(List<Hora_Comidas> horaComidas) {
        this.horaComidas = horaComidas;
    }

    @Override
    public List<Hora_Comidas> getListaRegistros() {
        return this.horaComidas;
    }
}
