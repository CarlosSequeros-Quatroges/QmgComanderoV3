package es.quatroges.qgestpv_v3.webservice;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.ClientesCtaCasa;

public class RespuestaClientesCtaCasaWS extends RespuestaBaseWS
        implements  RespuestaSincro<ClientesCtaCasa>

	{

        @SerializedName("filas")
        @Expose
        private List<ClientesCtaCasa> clientes = null;


        public List<ClientesCtaCasa> getClientes() {
            return clientes;
        }

        public void setClientes(List<ClientesCtaCasa> clientes) {
            this.clientes = clientes;
        }


        @Override
        public List<ClientesCtaCasa> getListaRegistros() {
            return this.clientes;
        }

    }

