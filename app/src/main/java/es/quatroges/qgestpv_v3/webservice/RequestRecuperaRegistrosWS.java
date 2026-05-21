package es.quatroges.qgestpv_v3.webservice;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RequestRecuperaRegistrosWS {

    @SerializedName("codemp")
    @Expose
    private String codemp;

    @SerializedName("identificador")
    @Expose
    private String identificador;

    @SerializedName("tabla")
    @Expose
    private String tabla;

    @SerializedName("codigos")
    @Expose
    private ArrayList<String> codigos =null;

    public String getCodemp() {
        return codemp;
    }

    public void setCodemp(String codemp) {
        this.codemp = codemp;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public ArrayList<String> getCodigos() {
        return codigos;
    }

    public void setCodigos(ArrayList<String> codigos) {
        this.codigos = codigos;
    }
}

