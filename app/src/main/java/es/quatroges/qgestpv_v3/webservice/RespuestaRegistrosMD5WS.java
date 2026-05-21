package es.quatroges.qgestpv_v3.webservice;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.roomRegistrosCRC;

public class RespuestaRegistrosMD5WS extends  RespuestaBaseWS{
    int totalregs;
    int minid;
    int maxid;

    @SerializedName("registros")
    @Expose
    private List<roomRegistrosCRC> cupos= null;

    public RespuestaRegistrosMD5WS() {
    }


    public int getTotalregs() {
        return totalregs;
    }

    public void setTotalregs(int totalregs) {
        this.totalregs = totalregs;
    }

    public int getMinid() {
        return minid;
    }

    public void setMinid(int minid) {
        this.minid = minid;
    }

    public int getMaxid() {
        return maxid;
    }

    public void setMaxid(int maxid) {
        this.maxid = maxid;
    }

    public List<roomRegistrosCRC> getCupos() {
        return cupos;
    }

    public void setCupos(List<roomRegistrosCRC> cupos) {
        this.cupos = cupos;
    }
}

