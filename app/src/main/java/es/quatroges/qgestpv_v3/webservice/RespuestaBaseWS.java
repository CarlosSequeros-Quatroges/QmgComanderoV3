package es.quatroges.qgestpv_v3.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RespuestaBaseWS {

    @SerializedName("errnum")
    @Expose
    private String errnum;
    @SerializedName("errdesc")
    @Expose
    private String errdesc;

    public String getErrnum() {
        return errnum;
    }

    public void setErrnum(String errnum) {
        this.errnum = errnum;
    }

    public String getErrdesc() {
        return errdesc;
    }

    public void setErrdesc(String errdesc) {
        this.errdesc = errdesc;
    }

}