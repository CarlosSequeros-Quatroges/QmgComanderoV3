package es.quatroges.qgestpv_v3.datos;

import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public class CabeceraTiquet {
    private ClaseUtils.enTipoTiquet tipoTiquet;

    private String razon;
    private String cif;
    private String TPV;

    private String hora;
    private String mesa;
    private String pax;


    //
    private String nfactura;

    private String pago_fecha_hora;
    private String formapago;
    private String efectivo;
    private String tarjeta;
    private String cuentacasa;
    private String credito;
    private String codclicasa;
    private String entregado;
    private String cambio;
    private String firma;
    private String apto;


    public CabeceraTiquet() {
        this.tipoTiquet = ClaseUtils.enTipoTiquet.cuenta;
        this.razon = "";
        this.cif = "";
        this.TPV = "";
        this.hora = "";
        this.mesa = "";
        this.pax = "";

        this.nfactura = "";
        this.pago_fecha_hora = "";
        this.formapago = "N";
        this.efectivo = "0.00";
        this.tarjeta = "0.00";
        this.cuentacasa = "0.00";
        this.credito = "0.00";
        this.codclicasa = "0";
        this.entregado = "0.00";
        this.cambio = "0.00";
        this.firma = "";
        this.apto = "";
    }

    public ClaseUtils.enTipoTiquet getTipoTiquet() {
        return tipoTiquet;
    }

    public void setTipoTiquet(ClaseUtils.enTipoTiquet tipoTiquet) {
        this.tipoTiquet = tipoTiquet;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getTPV() {
        return TPV;
    }

    public void setTPV(String TPV) {
        this.TPV = TPV;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public String getPax() {
        return pax;
    }

    public void setPax(String pax) {
        this.pax = pax;
    }


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

    public String getFormapago() {
        return formapago;
    }

    public void setFormapago(String formapago) {
        this.formapago = formapago;
    }

    public String getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(String efectivo) {
        this.efectivo = efectivo;
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

    public String getCodclicasa() {
        return codclicasa;
    }

    public void setCodclicasa(String codclicasa) {
        this.codclicasa = codclicasa;
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
}
