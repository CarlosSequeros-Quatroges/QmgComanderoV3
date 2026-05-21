package es.quatroges.qgestpv_v3.datos;

import androidx.annotation.NonNull;

public class OrdenPlatos {
    private int codigo;
    private String descripcion;
    boolean checked;
    public OrdenPlatos(){

    }

    public OrdenPlatos(int codigo, String descripcion){
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.checked = false;
    }

    @NonNull
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(@NonNull int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }


}
