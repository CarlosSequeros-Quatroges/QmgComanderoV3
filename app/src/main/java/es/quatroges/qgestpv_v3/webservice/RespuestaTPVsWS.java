package es.quatroges.qgestpv_v3.webservice;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.Tpvs;

public class RespuestaTPVsWS extends  RespuestaBaseWS
        implements  RespuestaSincro<Tpvs>

	{





    @SerializedName("filas")
    @Expose
    private List<Tpvs> tpvs = null;


    public List<Tpvs> getTpvs() {
        return tpvs;
    }

    public void setTpvs(List<Tpvs> tpvs) {
        this.tpvs = tpvs;
    }

        @Override
        public List<Tpvs> getListaRegistros() {
            return this.tpvs;
        }

}

