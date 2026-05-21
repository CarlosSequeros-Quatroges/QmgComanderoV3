package es.quatroges.qgestpv_v3.datos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Facturas {
    @SerializedName("nfactura")
    @Expose
    private int nfactura;

    @SerializedName("mesa")
    @Expose
    private int mesa;

    @SerializedName("submesa")
    @Expose
    private int submesa;

    @SerializedName("formapago")
    @Expose
    private String formapago;

    @SerializedName("codenl")
    @Expose
    private int codenl;

    @SerializedName("fecha")
    @Expose
    private String fecha;

    @SerializedName("hora")
    @Expose
    private String hora;

    public int getNfactura() {
        return nfactura;
    }

    public void setNfactura(int nfactura) {
        this.nfactura = nfactura;
    }

    public int getMesa() {
        return mesa;
    }

    public void setMesa(int mesa) {
        this.mesa = mesa;
    }

    public int getSubmesa() {
        return submesa;
    }

    public void setSubmesa(int submesa) {
        this.submesa = submesa;
    }

    public String getFormapago() {
        return formapago;
    }

    public void setFormapago(String formapago) {
        this.formapago = formapago;
    }

    public int getCodenl() {
        return codenl;
    }

    public void setCodenl(int codenl) {
        this.codenl = codenl;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Facturas(int nfactura, int mesa, int submesa, String formapago, int codenl, String fecha, String hora) {
        this.nfactura = nfactura;
        this.mesa = mesa;
        this.submesa = submesa;
        this.formapago = formapago;
        this.codenl = codenl;
        this.fecha = fecha;
        this.hora = hora;
    }

    public Facturas() {
        this.nfactura = 0;
        this.mesa = 0;
        this.submesa = 0;
        this.formapago = "";
        this.codenl = 0;
        this.fecha = "";
        this.hora = "";
    }
}
