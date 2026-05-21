package es.quatroges.qgestpv_v3.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.Productos;

public class RespuestaProductosWS extends RespuestaBaseWS
        implements  RespuestaSincro<Productos>{

    @SerializedName("filas")
    @Expose
    private List<Productos> productos = null;

    public List<Productos> getProductos() {
        return productos;
    }

    public void setProductos(List<Productos> productos) {
        this.productos = productos;
    }
    @Override
    public List<Productos> getListaRegistros() {
        return this.productos;
    }
}
