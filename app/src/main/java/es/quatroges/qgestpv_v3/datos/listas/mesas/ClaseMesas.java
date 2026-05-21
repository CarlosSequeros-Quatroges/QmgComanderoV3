package es.quatroges.qgestpv_v3.datos.listas.mesas;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.TreeMap;

import es.quatroges.qgestpv_v3.datos.Nom_Mesas;
import es.quatroges.qgestpv_v3.datos.listas.tpvs.ClaseTPVs;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;

@SuppressWarnings("unchecked")
public class ClaseMesas implements Parcelable {

    public int mesa;
    public String descripcion;
    public int grupo;
    public int pax;
    public boolean abiertas;
    public boolean tickets;
    public double importe;
    public double coste;
    public int nSubmesas;
    public boolean actualizaPax;

    public ArrayList<ClaseSubMesas> submesas;

    public ClaseMesas(int mesa, String descripcion, int grupo){
        this.mesa = mesa;
        this.descripcion  =descripcion;
        this.grupo = grupo;
        this.pax = 0;
        this.abiertas = false;
        this.tickets = false;
        this.importe = 0;
        this.coste= 0;
        this.nSubmesas = 1;
        this.submesas = new ArrayList<ClaseSubMesas>();
        //String horaApertura = ClaseUtils.now("HH:mm:ss");

        this.submesas.add(new ClaseSubMesas(1,ClaseSubMesas.enEstados.abierta,false,0,0,"","","","","", false,"","",""));
        this.actualizaPax = false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mesa);
        dest.writeString(this.descripcion);
        dest.writeInt(this.grupo);
        dest.writeInt(this.pax);
        dest.writeByte((byte) (this.abiertas ? 1 : 0));
        dest.writeByte((byte) (this.tickets ? 1 : 0));
        dest.writeDouble(this.importe);
        dest.writeDouble(this.coste );
        dest.writeList(this.submesas);
        dest.writeByte((byte) (this.actualizaPax ? 1 : 0));

    }
    public void readFromParcel(Parcel in){
        this.mesa = in.readInt();
        this.descripcion =in.readString();
        this.grupo = in.readInt();
        this.pax = in.readInt();
        this.abiertas= in.readByte() != 0;
        this.tickets = in.readByte() != 0;
        this.importe = in.readDouble();
        this.coste= in.readDouble();
        this.submesas = in.readArrayList(Integer.class.getClassLoader());
        this.actualizaPax= in.readByte() != 0;

    }

    protected ClaseMesas(Parcel in){
        super();
        readFromParcel(in);
    }

    protected  static final  Creator<ClaseMesas> CREATOR = new Creator<ClaseMesas>(){
        public ClaseMesas createFromParcel (Parcel in){
            return new ClaseMesas(in);
        }
        public ClaseMesas[] newArray(int size){
            return new ClaseMesas[size];
        }

    };

    public static TreeMap<Integer ,ClaseMesas> recuperaMesas(ClaseTPVs tpv, TreeMap<String, Nom_Mesas> nomMesas){

        TreeMap<Integer ,ClaseMesas> listaMesas= new TreeMap<>();
        listaMesas.clear();
        if(tpv != null) {
            for (int t = 1; t <= tpv.numeroMesas; t++) {
                String desc ="M"+String.valueOf(t);
                int grupo =1;
                if (nomMesas.containsKey(tpv.codtpv+"-"+String.valueOf(t))) {
                    desc = nomMesas.get(tpv.codtpv + "-" + String.valueOf(t)).getDescripcion();
                    grupo = nomMesas.get(tpv.codtpv + "-" + String.valueOf(t)).getGrupo();
                }
                listaMesas.put(t, new ClaseMesas(t,desc,grupo));
            }
        }
        return listaMesas;
    }
/*
    @Override
    public int compareTo(ClaseMesas claseMesas) {
        if (tickets == true) {
            return  1;
        }
        else if (claseMesas.tickets == true) {
            return -1;
        }
        else if (abiertas == true) {
            return  1;
        }
        else if (claseMesas.abiertas == true) {
            return -1;
        }
        return 0;
    }
    *
 */
}
