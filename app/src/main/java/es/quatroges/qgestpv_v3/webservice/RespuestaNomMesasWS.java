package es.quatroges.qgestpv_v3.webservice;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.Nom_Mesas;

public class RespuestaNomMesasWS extends  RespuestaBaseWS
        implements  RespuestaSincro<Nom_Mesas>

	{

    @SerializedName("filas")
    @Expose
    private List<Nom_Mesas> nomMesas = null;

    public List<Nom_Mesas> getNomMesas() {
        return nomMesas;
    }

    public void setNomMesas(List<Nom_Mesas> nomMesas) {
        this.nomMesas = nomMesas;
    }
        @Override
        public List<Nom_Mesas> getListaRegistros() {
            return this.nomMesas;
        }

    }

