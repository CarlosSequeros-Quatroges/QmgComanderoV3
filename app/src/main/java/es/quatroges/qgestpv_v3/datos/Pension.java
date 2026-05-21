package es.quatroges.qgestpv_v3.datos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pension  {

    @SerializedName("pension")
    @Expose
    private String pension;
    public Pension(){

    }

    public String getPension() {
        return pension;
    }

    public void setPension(String pension) {
        this.pension = pension;
    }


}
