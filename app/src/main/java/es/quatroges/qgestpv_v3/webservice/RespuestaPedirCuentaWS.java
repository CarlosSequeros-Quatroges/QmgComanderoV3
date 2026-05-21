package es.quatroges.qgestpv_v3.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RespuestaPedirCuentaWS extends RespuestaBaseWS {
    @SerializedName("nfactura")
    @Expose
    private String  nfactura = null;

    @SerializedName("pago_fecha_hora")
    @Expose
    private String  pago_fecha_hora ="";

    @SerializedName("habitacion")
    @Expose
    private String  habitacion ="";

    public String getNfactura() {
        return nfactura;
    }

    public void setNfactura(String nfactura) {
        this.nfactura = nfactura;
    }

    public String getPago_fecha_hora() {
        return pago_fecha_hora;
    }

    public void setPago_fecha_hora(String pago_fecha_hora) {
        this.pago_fecha_hora = pago_fecha_hora;
    }

    public String getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(String habitacion) {
        this.habitacion = habitacion;
    }
}
