package es.quatroges.qgestpv_v3.webservice;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.Usuarios;

public class RespuestaUsuariosWS extends RespuestaBaseWS
        implements  RespuestaSincro<Usuarios>	{

        @SerializedName("filas")
        @Expose
        private List<Usuarios> usuarios = null;


        public List<Usuarios> getUsuarios() {
            return usuarios;
        }

        public void setUsuarios(List<Usuarios> usuarios) {
            this.usuarios = usuarios;
        }

    @Override
    public List<Usuarios> getListaRegistros() {
        return this.usuarios;
    }

}

