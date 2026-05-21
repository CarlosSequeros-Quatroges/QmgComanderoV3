package es.quatroges.qgestpv_v3.webservice;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestPagaMesaWS {

    @SerializedName("codigoEmpresa")
    @Expose
    private String codigoEmpresa;

    @SerializedName("IDTablet")
    @Expose
    private String idTablet;

    @SerializedName("tpv")
    @Expose
    private String tpv;

    @SerializedName("mesa")
    @Expose
    private String mesa;

    @SerializedName("submesa")
    @Expose
    private String  submesa;

    @SerializedName("codemp_ext")
    @Expose
    private String codemp_ext;

    @SerializedName("nreserva")
    @Expose
    private String nreserva;

    @SerializedName("formapago")
    @Expose
    private String  formapago;

    @SerializedName("importe")
    @Expose
    private String  importe;

    @SerializedName("igic")
    @Expose
    private String  igic;

    @SerializedName("efectivo")
    @Expose
    private String  efectivo;

    @SerializedName("entregado")
    @Expose
    private String  entregado;

    @SerializedName("cambio")
    @Expose
    private String  cambio;

    @SerializedName("tarjeta")
    @Expose
    private String  tarjeta;

    @SerializedName("cuentacasa")
    @Expose
    private String  cuentacasa;

    @SerializedName("credito")
    @Expose
    private String  credito;

    @SerializedName("nfactura")
    @Expose
    private String  nfactura;

    @SerializedName("codclicasa")
    @Expose
    private String  codclicasa;

    @SerializedName("codusu")
    @Expose
    private String  codusu;

    @SerializedName("firma")
    @Expose
    private String   firma;

    @SerializedName("apto")
    @Expose
    private String   apto;

    @SerializedName("imprimir")
    @Expose
    private String   imprimir;

    public String getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(String codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public String getIdTablet() {
        return idTablet;
    }

    public void setIdTablet(String idTablet) {
        this.idTablet = idTablet;
    }

    public String getTpv() {
        return tpv;
    }

    public void setTpv(String tpv) {
        this.tpv = tpv;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public String getSubmesa() {
        return submesa;
    }

    public void setSubmesa(String submesa) {
        this.submesa = submesa;
    }

    public String getCodemp_ext() {
        return codemp_ext;
    }

    public void setCodemp_ext(String codemp_ext) {
        this.codemp_ext = codemp_ext;
    }

    public String getNreserva() {
        return nreserva;
    }

    public void setNreserva(String nreserva) {
        this.nreserva = nreserva;
    }

    public String getFormapago() {
        return formapago;
    }

    public void setFormapago(String formapago) {
        this.formapago = formapago;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getIgic() {
        return igic;
    }

    public void setIgic(String igic) {
        this.igic = igic;
    }

    public String getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(String efectivo) {
        this.efectivo = efectivo;
    }

    public String getEntregado() {
        return entregado;
    }

    public void setEntregado(String entregado) {
        this.entregado = entregado;
    }

    public String getCambio() {
        return cambio;
    }

    public void setCambio(String cambio) {
        this.cambio = cambio;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public String getCuentacasa() {
        return cuentacasa;
    }

    public void setCuentacasa(String cuentacasa) {
        this.cuentacasa = cuentacasa;
    }

    public String getCredito() {
        return credito;
    }

    public void setCredito(String credito) {
        this.credito = credito;
    }

    public String getNfactura() {
        return nfactura;
    }

    public void setNfactura(String nfactura) {
        this.nfactura = nfactura;
    }

    public String getCodclicasa() {
        return codclicasa;
    }

    public void setCodclicasa(String codclicasa) {
        this.codclicasa = codclicasa;
    }

    public String getCodusu() {
        return codusu;
    }

    public void setCodusu(String codusu) {
        this.codusu = codusu;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getApto() {
        return apto;
    }

    public void setApto(String apto) {
        this.apto = apto;
    }

    public String getImprimir() {
        return imprimir;
    }

    public void setImprimir(String imprimir) {
        this.imprimir = imprimir;
    }
}

