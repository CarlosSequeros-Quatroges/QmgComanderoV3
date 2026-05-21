package es.quatroges.qgestpv_v3.configuracion;

import es.quatroges.qgestpv_v3.ActivityInicio;

public class ClaseUltimoEstadoApp {
    private ActivityInicio.enEstado estado;
    private int usuario;
    private int tpv;

    public ClaseUltimoEstadoApp() {
        estado = ActivityInicio.enEstado.iniciando;
        usuario = -1;
        tpv = -1;
    }

    public ActivityInicio.enEstado getEstado() {
        return estado;
    }

    public void setEstado(ActivityInicio.enEstado estado) {
        this.estado = estado;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public int getTpv() {
        return tpv;
    }

    public void setTpv(int tpv) {
        this.tpv = tpv;
    }
}
