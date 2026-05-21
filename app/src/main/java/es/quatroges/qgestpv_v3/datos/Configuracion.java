package es.quatroges.qgestpv_v3.datos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "configuracion")
public class Configuracion {
    @NonNull
    @PrimaryKey
    private String parametro;
    private String valor;

    public Configuracion(){

    }
    @Ignore
    public Configuracion(@NonNull String parametro, String valor) {
        this.parametro = parametro;
        this.valor = valor;
    }

    @NonNull
    public String getParametro() {
        return parametro;
    }

    public void setParametro(@NonNull String parametro) {
        this.parametro = parametro;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
