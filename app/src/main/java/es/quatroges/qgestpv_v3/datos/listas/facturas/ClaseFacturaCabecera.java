package es.quatroges.qgestpv_v3.datos.listas.facturas;

import android.os.Parcel;
import android.os.Parcelable;

public class ClaseFacturaCabecera implements Parcelable {

    public int nfactura;
    public double teuros;
    public  double tigic;
    public String formapago;
    public double efectivo;
    public double tarjeta;
    public double credito;
    public double ctacasa;
    public double totalCtaCasa;
    public String tipoCtaCasa;
    public String habitacion;
    public String pago_fecha_hora;

    public ClaseFacturaCabecera(int nfactura, double teuros, double tigic, String formapago, double efectivo, double tarjeta, double credito, double ctacasa, String habitacion, String pago_fecha_hora) {
        this.nfactura = nfactura;
        this.teuros = teuros;
        this.tigic = tigic;
        this.formapago = formapago;
        this.efectivo = efectivo;
        this.tarjeta = tarjeta;
        this.credito = credito;
        this.ctacasa = ctacasa;
        this.totalCtaCasa = 0;
        this.tipoCtaCasa = "G";
        this.habitacion = "";
        this.pago_fecha_hora ="";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.nfactura);
        dest.writeDouble(this.teuros);
        dest.writeDouble(this.tigic);
        dest.writeString(this.formapago);
        dest.writeDouble(this.efectivo);
        dest.writeDouble(this.tarjeta);
        dest.writeDouble(this.credito);
        dest.writeDouble(this.ctacasa);
        dest.writeDouble(this.totalCtaCasa);
        dest.writeString(this.habitacion);
        dest.writeString(this.pago_fecha_hora);
    }
    public void readFromParcel(Parcel in){
        this.nfactura = in.readInt();
        this.teuros = in.readDouble();
        this.tigic = in.readDouble();
        this.formapago = in.readString();
        this.efectivo = in.readDouble();
        this.tarjeta = in.readDouble();
        this.credito = in.readDouble();
        this.ctacasa = in.readDouble();
        this.totalCtaCasa = in.readDouble();
        this.habitacion = in.readString();
        this.pago_fecha_hora =in.readString();
    }

    protected ClaseFacturaCabecera(Parcel in){
        super();
        readFromParcel(in);
    }

    public  static final Creator<ClaseFacturaCabecera> CREATOR = new Creator<ClaseFacturaCabecera>(){
        public ClaseFacturaCabecera createFromParcel (Parcel in){return new ClaseFacturaCabecera(in); }
        public ClaseFacturaCabecera[] newArray(int size){ return new ClaseFacturaCabecera[size]; }

    };


}
