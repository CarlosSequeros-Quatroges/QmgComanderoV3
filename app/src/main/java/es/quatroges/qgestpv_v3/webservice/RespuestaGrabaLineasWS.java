package es.quatroges.qgestpv_v3.webservice;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.ResGrabaLineas;
import es.quatroges.qgestpv_v3.datos.ResGrabaLineasSubmesas;

public class RespuestaGrabaLineasWS extends RespuestaBaseWS {

    @SerializedName("resultados")
    @Expose
    private List<ResGrabaLineas> resultados =null;

    @SerializedName("submesas")
    @Expose
    private List<ResGrabaLineasSubmesas> submesas =null;

    public List<ResGrabaLineas> getResultados() {
        return resultados;
    }

    public void setResultados(List<ResGrabaLineas> resultados) {
        this.resultados = resultados;
    }

    public List<ResGrabaLineasSubmesas> getSubmesas() {
        return submesas;
    }

    public void setSubmesas(List<ResGrabaLineasSubmesas> submesas) {
        this.submesas = submesas;
    }
}

