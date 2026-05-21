package es.quatroges.qgestpv_v3.datos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.utils.ClaseItemExtra;

public class GrabaLineasVenta implements Parcelable {
    @SerializedName("nlinea")
    @Expose
    private int nlinea;

    @SerializedName("codigo")
    @Expose
    private int codigo;

    @SerializedName("mesa")
    @Expose
    private int mesa;

    @SerializedName("submesa")
    @Expose
    private int submesa;

    @SerializedName("codmenu")
    @Expose
    private String codmenu;

    @SerializedName("cantidad")
    @Expose
    private int cantidad;

    @SerializedName("cantidad_ant")
    @Expose
    private int cantidad_ant;


    @SerializedName("peuros")
    @Expose
    private String peuros;

    @SerializedName("teuros")
    @Expose
    private  String teuros;

    @SerializedName("descripcion")
    @Expose
    private  String descripcion;

    @SerializedName("codtpv")
    @Expose
    private  String codtpv;

    @SerializedName("happy_hour")
    @Expose
    private  String happy_hour;

    private transient String nota;

    @SerializedName("codusu")
    @Expose
    private  String codusu;

    @SerializedName("tipo")
    @Expose
    private  String tipo;

    @SerializedName("tmenu")
    @Expose
    private  int tmenu;

    @SerializedName("estado")
    @Expose
    private  String estado;

    @SerializedName("mprecio")
    @Expose
    private  String mprecio;

    @Expose(deserialize = false)
    private String imprimir;

    @SerializedName("orden_platos")
    @Expose
    private  int orden_platos;

    @SerializedName("pcoste")
    @Expose
    private String pcoste;

    @SerializedName("tcoste")
    @Expose
    private  String tcoste;

    @SerializedName("descuento")
    @Expose
    private  String descuento;

    @SerializedName("pension")
    @Expose
    private  String pension;

    @SerializedName("extras")
    @Expose
    private ArrayList<ClaseItemExtra> extras;


    public GrabaLineasVenta(int nlinea, int codigo, int mesa, int submesa, String codmenu, int cantidad, String peuros, String teuros, String descripcion, String codtpv, String happy_hour, String nota, String codusu, String tipo, int tmenu, String estado, String mprecio, String impirimir, int orden_platos, String pcoste, String tcoste, String descuento, int cantidad_ant, String pension ) {
        this.nlinea = nlinea;
        this.codigo = codigo;
        this.mesa = mesa;
        this.submesa = submesa;
        this.codmenu = codmenu;
        this.cantidad = cantidad;
        this.peuros = peuros;
        this.teuros = teuros;
        this.descripcion = descripcion;
        this.codtpv = codtpv;
        this.happy_hour = happy_hour;
        this.nota = nota;
        this.codusu = codusu;
        this.tipo = tipo;
        this.tmenu = tmenu;
        this.estado = estado;
        this.mprecio = mprecio;
        this.imprimir = impirimir;
        this.orden_platos = orden_platos;
        this.pcoste = pcoste;
        this.tcoste = tcoste;
        this.descuento = descuento;
        this.cantidad_ant = cantidad_ant;
        this.pension = pension;
        this.extras = new ArrayList<>();
    }

    public GrabaLineasVenta() {
        this.nlinea = 0;
        this.codigo = 0;
        this.mesa = 0;
        this.submesa = 0;
        this.codmenu = "";
        this.cantidad = 0;
        this.peuros = "0,00";
        this.teuros = "0,00";
        this.descripcion = "";
        this.codtpv = "";
        this.happy_hour = "";
        this.nota = "";
        this.codusu = "";
        this.tipo = "";
        this.tmenu = 0;
        this.estado = "";
        this.mprecio = "N";
        this.imprimir = "S";
        this.orden_platos = 1;
        this.pcoste = "0,00";
        this.tcoste = "0,00";
        this.descuento = "";
        this.cantidad_ant = 0;
        this.pension = "";
        this.extras = new ArrayList<>();
    }

    public int getNlinea() {
        return nlinea;
    }

    public void setNlinea(int nlinea) {
        this.nlinea = nlinea;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getMesa() {
        return mesa;
    }

    public void setMesa(int mesa) {
        this.mesa = mesa;
    }

    public int getSubmesa() {
        return submesa;
    }

    public void setSubmesa(int submesa) {
        this.submesa = submesa;
    }

    public String getCodmenu() {
        return codmenu;
    }

    public void setCodmenu(String codmenu) {
        this.codmenu = codmenu;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getPeuros() {
        return peuros;
    }

    public void setPeuros(String peuros) {
        this.peuros = peuros;
    }

    public String  getTeuros() {
        return teuros;
    }

    public void setTeuros(String teuros) {
        this.teuros = teuros;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodtpv() {
        return codtpv;
    }

    public void setCodtpv(String codtpv) {
        this.codtpv = codtpv;
    }

    public String getHappy_hour() {
        return happy_hour;
    }

    public void setHappy_hour(String happy_hour) {
        this.happy_hour = happy_hour;
    }

    public String getNota() {
        if (extras != null && !extras.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ClaseItemExtra extra : extras) {
                if (extra == null || extra.nota == null || extra.nota.trim().length() == 0) {
                    continue;
                }
                if (sb.length() > 0) {
                    sb.append("\n");
                }
                sb.append(extra.nota.trim());
            }
            if (sb.length() > 0) {
                return sb.toString();
            }
        }
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getCodusu() {
        return codusu;
    }

    public void setCodusu(String codusu) {
        this.codusu = codusu;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getTmenu() {
        return tmenu;
    }

    public void setTmenu(int tmenu) {
        this.tmenu = tmenu;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMprecio() {
        return mprecio;
    }

    public void setMprecio(String mprecio) {
        this.mprecio = mprecio;
    }

    public String getImprimir() {
        return imprimir;
    }

    public void setImprimir(String imprimir) {
        this.imprimir = imprimir;
    }

    public int getOrden_platos() {
        return orden_platos;
    }

    public void setOrden_platos(int orden_platos) {
        this.orden_platos = orden_platos;
    }

    public String getPcoste() {
        return pcoste;
    }

    public void setPcoste(String pcoste) {
        this.pcoste = pcoste;
    }

    public String getTcoste() {
        return tcoste;
    }

    public void setTcoste(String tcoste) {
        this.tcoste = tcoste;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public int getCantidad_ant() {
        return cantidad_ant;
    }

    public void setCantidad_ant(int cantidad_ant) {
        this.cantidad_ant = cantidad_ant;
    }

    public String getPension() {
        return pension;
    }

    public void setPension(String pension) {
        this.pension = pension;
    }

    public ArrayList<ClaseItemExtra> getExtras() {
        return extras;
    }

    public void setExtras(ArrayList<ClaseItemExtra> extras) {
        this.extras = extras != null ? extras : new ArrayList<>();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(nlinea);
        dest.writeInt(codigo);
        dest.writeInt(mesa);
        dest.writeInt(submesa);
        dest.writeString(codmenu);
        dest.writeInt(cantidad);
        dest.writeString(peuros);
        dest.writeString(teuros);
        dest.writeString(descripcion);
        dest.writeString(codtpv);
        dest.writeString(happy_hour);
        dest.writeString(nota);
        dest.writeString(codusu);
        dest.writeString(tipo);
        dest.writeInt(tmenu);
        dest.writeString(estado);
        dest.writeString(mprecio);
        dest.writeString(imprimir);
        dest.writeInt(orden_platos);
        dest.writeString(pcoste);
        dest.writeString(tcoste);
        dest.writeString(descuento);
        dest.writeInt(cantidad_ant);
        dest.writeString(pension);
        dest.writeTypedList(extras);
    }

    public void readFromParcel(Parcel in){
        this.nlinea = in.readInt();
        this.codigo = in.readInt();
        this.mesa= in.readInt();
        this.submesa= in.readInt();
        this.codmenu = in.readString();
        this.cantidad= in.readInt();
        this.peuros= in.readString();
        this.teuros = in.readString();
        this.descripcion = in.readString();
        this.codtpv = in.readString();
        this.happy_hour = in.readString();
        this.nota = in.readString();
        this.codusu = in.readString();
        this.tipo = in.readString();
        this.tmenu= in.readInt();
        this.estado = in.readString();
        this.mprecio = in.readString();
        this.imprimir = in.readString();
        this.orden_platos = in.readInt();
        this.pcoste= in.readString();
        this.tcoste= in.readString();
        this.descuento = in.readString();
        this.cantidad_ant = in.readInt();
        this.pension = in.readString();
        this.extras = in.createTypedArrayList(ClaseItemExtra.CREATOR);
        if (this.extras == null) {
            this.extras = new ArrayList<>();
        }
    }

    protected GrabaLineasVenta(Parcel in){
        super();
        readFromParcel(in);
    }

    public static final  Creator<GrabaLineasVenta> CREATOR = new Creator<GrabaLineasVenta>(){
        public GrabaLineasVenta createFromParcel (Parcel in){
            return new GrabaLineasVenta(in);
        }
        public GrabaLineasVenta[] newArray(int size){return new GrabaLineasVenta[size];}
    };
}
