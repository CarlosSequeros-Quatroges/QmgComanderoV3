package es.quatroges.qgestpv_v3.datos.listas.mesas;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import es.quatroges.qgestpv_v3.datos.listas.lineaVentas.ClaseLineaVentas;

@SuppressWarnings("unchecked")
public class ClaseSubMesas implements Parcelable {
    public enum  enEstados {
        libre,abierta,tiquet
    }

    public int submesa;
    public enEstados estado;
    public boolean pagos;
    public int pax;
    public Double importe;
    public Double coste;
    public double efectivo;
    public double tarjeta;
    public double ctacasa;
    public double credito;
    public boolean errorTransmision;
    public String nfactura;
    public ArrayList<ClaseLineaVentas> lineasVentas;
    public int paxTraspaso;
    public int codenl;
    public String habitacion ;
    public String pension;
    public String descPension;
    public String tipoPension;
    public String descTipoPension;

    public boolean aplicaPension;
    public String horaApertura;
    public String tipoPensionAplicada;
    public String horarioPensionAplicado;

    public ClaseSubMesas(int submesa, enEstados estado , boolean pagos, int pax, int codenl, String habitacion,
        String pension, String descPension, String tipoPension, String descTipoPension, boolean aplicaPension, String horaApertura,
        String tipoPensionAplicada, String horarioPensionAplicado                 ){
        this.submesa = submesa;
        this.estado = estado;
        this.pagos = pagos;
        this.pax = pax;
        this.codenl = codenl;
        this.importe= 0.00;
        this.coste   = 0.00;
        this.efectivo = 0.00;
        this.tarjeta = 0.00;
        this.ctacasa = 0.00;
        this.credito = 0.00;
        this.errorTransmision =false;
        this.lineasVentas = new ArrayList<>();
        this.nfactura = "";
        this.paxTraspaso = 0;
        this.habitacion = habitacion;
        this.pension = pension;
        this.descPension = descPension;
        this.tipoPension = tipoPension;
        this.descTipoPension = descTipoPension;
        this.aplicaPension = aplicaPension;
        this.horaApertura = horaApertura;
        this.tipoPensionAplicada = tipoPensionAplicada;
        this.horarioPensionAplicado = horarioPensionAplicado;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.submesa);
        dest.writeString(this.estado.name());
        dest.writeByte((byte)(this.pagos ? 1 : 0));
        dest.writeInt(this.pax);
        dest.writeInt(this.codenl);
        dest.writeDouble(this.importe);
        dest.writeDouble(this.coste);
        dest.writeDouble(this.efectivo);
        dest.writeDouble(this.tarjeta);
        dest.writeDouble(this.ctacasa);
        dest.writeDouble(this.credito);
        dest.writeByte((byte) (errorTransmision ? 1 : 0));
        dest.writeList(this.lineasVentas);
        dest.writeString(this.nfactura);
        dest.writeInt(this.paxTraspaso);
        dest.writeString(this.habitacion);
        dest.writeString(this.pension);
        dest.writeString(this.descPension);
        dest.writeString(this.tipoPension);
        dest.writeString(this.descTipoPension);
        dest.writeByte((byte) (aplicaPension ? 1 : 0));
        dest.writeString(this.horaApertura);
        dest.writeString(this.tipoPensionAplicada);
        dest.writeString(this.horarioPensionAplicado);

    }
    public void readFromParcel(Parcel in){
        this.submesa= in.readInt();

        String tmp = in.readString();
        if (tmp.equals(enEstados.libre.toString()))
            this.estado = enEstados.libre;
        else if (tmp.equals(enEstados.abierta.toString()))
            this.estado = enEstados.abierta;
        else if (tmp.equals(enEstados.tiquet.toString()))
            this.estado = enEstados.tiquet;
        else
            this.estado = enEstados.libre;

        this.pagos = in.readByte() != 0;

        this.pax = in.readInt();
        this.codenl = in.readInt();
        this.importe = in.readDouble();
        this.coste = in.readDouble();
        this.efectivo= in.readDouble();
        this.tarjeta = in.readDouble();
        this.ctacasa = in.readDouble();
        this.credito = in.readDouble();
        this.errorTransmision = in.readByte() != 0;
        this.lineasVentas = in.readArrayList(ClaseLineaVentas.class.getClassLoader());
        this.nfactura = in.readString();
        this.paxTraspaso = in.readInt();
        this.habitacion = in.readString();
        this.pension = in.readString();
        this.descPension = in.readString();
        this.tipoPension = in.readString();
        this.descTipoPension = in.readString();
        this.aplicaPension = in.readByte() != 0;
        this.horaApertura = in.readString();
        this.tipoPensionAplicada = in.readString();
        this.horarioPensionAplicado= in.readString();

    }

    protected ClaseSubMesas(Parcel in){
        super();
        readFromParcel(in);
    }

    protected  static final  Creator<ClaseSubMesas> CREATOR = new Creator<ClaseSubMesas>(){
        public ClaseSubMesas createFromParcel (Parcel in){
            return new ClaseSubMesas(in);
        }
        public ClaseSubMesas[] newArray(int size){
            return new ClaseSubMesas[size];
        }

    };

}
