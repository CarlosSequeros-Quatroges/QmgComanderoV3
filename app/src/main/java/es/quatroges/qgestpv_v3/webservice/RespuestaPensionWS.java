package es.quatroges.qgestpv_v3.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RespuestaPensionWS extends RespuestaBaseWS {
    @SerializedName("pension")
    @Expose
    private String  pension = null;

    @SerializedName("desc_pension")
    @Expose
    private String  desc_pension = null;

    @SerializedName("tipo_pension")
    @Expose
    private String  tipo_pension = null;

    @SerializedName("desc_tipo_pension")
    @Expose
    private String  desc_tipo_pension = null;

    public String getPension() {
        return pension;
    }

    public void setPension(String pension) {
        this.pension = pension;
    }

    public String getDesc_pension() {
        return desc_pension;
    }

    public void setDesc_pension(String desc_pension) {
        this.desc_pension = desc_pension;
    }

    public String getTipo_pension() {
        return tipo_pension;
    }

    public void setTipo_pension(String tipo_pension) {
        this.tipo_pension = tipo_pension;
    }

    public String getDesc_tipo_pension() {
        return desc_tipo_pension;
    }

    public void setDesc_tipo_pension(String desc_tipo_pension) {
        this.desc_tipo_pension = desc_tipo_pension;
    }
}
