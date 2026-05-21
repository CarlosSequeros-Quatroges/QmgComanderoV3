package es.quatroges.qgestpv_v3.webservice;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RespuestaCreditoWS extends  RespuestaBaseWS{

    @SerializedName("codemp_ext")
    @Expose
    private String codemp_ext;

    @SerializedName("habitacion")
    @Expose
    private String habitacion;

    @SerializedName("nreserva")
    @Expose
    private String nreserva;

    @SerializedName("nombre")
    @Expose
    private String nombre;

    @SerializedName("salida")
    @Expose
    private String salida;

    @SerializedName("okSalida")
    @Expose
    private Boolean okSalida;

    @SerializedName("credito")
    @Expose
    private String credito;

    @SerializedName("pendientePago")
    @Expose
    private String pendientePago;

    @SerializedName("pension")
    @Expose
    private String pension;

    @SerializedName("servicio")
    @Expose
    private String servicio;

    public String getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(String habitacion) {
        this.habitacion = habitacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSalida() {
        return salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }

    public Boolean getOkSalida() {
        return okSalida;
    }

    public void setOkSalida(Boolean okSalida) {
        this.okSalida = okSalida;
    }

    public String getCredito() {
        return credito;
    }

    public void setCredito(String credito) {
        this.credito = credito;
    }

    public String getPendientePago() {
        return pendientePago;
    }

    public void setPendientePago(String pendientePago) {
        this.pendientePago = pendientePago;
    }

    public String getNreserva() {
        return nreserva;
    }

    public void setNreserva(String nreserva) {
        this.nreserva = nreserva;
    }

    public String getPension() {
        return pension;
    }

    public void setPension(String pension) {
        this.pension = pension;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getCodemp_ext() {
        return codemp_ext;
    }

    public void setCodemp_ext(String codemp_ext) {
        this.codemp_ext = codemp_ext;
    }
}

