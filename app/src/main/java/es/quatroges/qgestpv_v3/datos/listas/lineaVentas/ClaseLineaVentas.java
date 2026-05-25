package es.quatroges.qgestpv_v3.datos.listas.lineaVentas;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;

import es.quatroges.qgestpv_v3.utils.ClaseItemExtra;
import es.quatroges.qgestpv_v3.utils.ClaseUtils;

public class ClaseLineaVentas implements Parcelable, Cloneable, Comparable<ClaseLineaVentas>{
    public int codigo;
    public int  mesa;
    public int submesa;
    public String codmenu;
    public int cantidad;
    public int cantidad_ant;
    public double peuros;
    public double teuros;
    public double pcoste;
    public double tcoste;
    public String descripcion;

    public String codtpv;
    public String happyhour;
    public String nota;
    public ClaseUtils.enEstado estado;
    public String codusu;
    public String tipo;
    public int tmenu;
    public ClaseUtils.enEstado resultadoEnvio;
    public String mprecio;
    public String nfactura;
    public String imprimir;
    public int orden_platos;
    public boolean linea_total;
    public String descuento;
    public int linea_app;

    public String pension;
    public ArrayList<ClaseItemExtra> extras;

    public ClaseLineaVentas()
    {
        this.codigo = 0;
        this.mesa = 0;
        this.submesa = 0;
        this.codmenu = "";
        this.cantidad = 0;
        this.cantidad_ant = 0;
        this.peuros = 0;
        this.teuros = 0;
        this.descripcion = "";
        this.codtpv = "";
        this.happyhour = "";
        this.nota = "";
        this.estado = null;
        this.codusu = "";
        this.tipo = "";
        this.tmenu = 0;
        this.resultadoEnvio = null;
        this.mprecio = "";
        this.nfactura = "";
        this.imprimir = "";
        this.orden_platos = -1;
        this.pcoste = 0;
        this.tcoste = 0;
        this.linea_total = false;
        this.descuento = "";
        this.linea_app = 0;
        this.pension = "";
        this.extras = new ArrayList<>();

    }


    public ClaseLineaVentas(int codigo, int mesa, int submesa, String codmenu, int cantidad, double peuros, double teuros, double pcoste, double tcoste, String descripcion, String codtpv, String happyhour, String nota, ClaseUtils.enEstado estado, String codusu, String tipo, int tmenu, ClaseUtils.enEstado resultadoEnvio, String mprecio,String nfactura, String imprimir, int orden_platos, boolean linea_total, String descuento, int cantidad_ant, int linea_app, String pension) {
        this.codigo = codigo;
        this.mesa = mesa;
        this.submesa = submesa;
        this.codmenu = codmenu;
        this.cantidad = cantidad;
        this.peuros = peuros;
        this.teuros = teuros;
        this.descripcion = descripcion;
        this.codtpv = codtpv;
        this.happyhour = happyhour;
        this.nota = nota;
        this.estado = estado;
        this.codusu = codusu;
        this.tipo = tipo;
        this.tmenu = tmenu;
        this.resultadoEnvio = resultadoEnvio;
        this.mprecio = mprecio;
        this.nfactura = nfactura;
        this.imprimir = imprimir;
        this.orden_platos = orden_platos;
        this.pcoste = pcoste;
        this.tcoste = tcoste;
        this.linea_total = linea_total;
        this.descuento = descuento;
        this.cantidad_ant = cantidad_ant;
        this.linea_app = linea_app;
        this.pension = pension;
        this.extras = new ArrayList<>();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.codigo);
        dest.writeInt(this.mesa);
        dest.writeInt(this.submesa);
        dest.writeString(this.codmenu);
        dest.writeInt(this.cantidad);
        dest.writeDouble(this.peuros);
        dest.writeDouble(this.teuros);
        dest.writeString(this.descripcion);
        dest.writeString(this.codtpv);
        dest.writeString(this.happyhour);
        dest.writeString(this.nota);
        dest.writeString(this.estado.name());
        dest.writeString(this.codusu);
        dest.writeString(this.tipo);
        dest.writeInt(this.tmenu);
        dest.writeString(this.resultadoEnvio.name());
        dest.writeString(this.mprecio );
        dest.writeString(this.nfactura);
        dest.writeString(this.imprimir);
        dest.writeInt(this.orden_platos);
        dest.writeDouble(this.pcoste);
        dest.writeDouble(this.tcoste);
        dest.writeString(this.descuento);
        dest.writeInt(this.cantidad_ant);
        dest.writeInt(this.linea_app);
        dest.writeString(this.pension);
        dest.writeTypedList(this.extras);
    }

    public void readFromParcel(Parcel in){
        this.codigo = in.readInt();
        this.mesa = in.readInt();
        this.submesa = in.readInt();
        this.codmenu = in.readString();
        this.cantidad = in.readInt();
        this.peuros = in.readDouble();
        this.teuros = in.readDouble();
        this.descripcion = in.readString();
        this.codtpv = in.readString();
        this.happyhour = in.readString();
        this.nota = in.readString();
        String tmp = in.readString();
        switch (tmp.toLowerCase()){
            case "transmitida":
                this.estado = ClaseUtils.enEstado.transmitida;
                break;
            case "actualizar":
                this.estado = ClaseUtils.enEstado.actualizar;
                break;
            case "anadir":
                this.estado = ClaseUtils.enEstado.anadir;
                break;
            case "eliminar":
                this.estado = ClaseUtils.enEstado.eliminar;
                break;
            case "error":
                this.estado = ClaseUtils.enEstado.error;
                break;
        }
        this.codusu = in.readString();
        this.tipo = in.readString();
        this.tmenu = in.readInt();
        tmp = in.readString();
        switch (tmp.toLowerCase()){
            case "transmitida":
                this.resultadoEnvio = ClaseUtils.enEstado.transmitida;
                break;
            case "actualizar":
                this.resultadoEnvio = ClaseUtils.enEstado.actualizar;
                break;
            case "anadir":
                this.resultadoEnvio = ClaseUtils.enEstado.anadir;
                break;
            case "eliminar":
                this.resultadoEnvio = ClaseUtils.enEstado.eliminar;
                break;
            case "error":
                this.resultadoEnvio = ClaseUtils.enEstado.error;
                break;
        }
        this.mprecio = in.readString();
        this.nfactura =in.readString();
        this.imprimir = in.readString();
        this.orden_platos = in.readInt();
        this.pcoste = in.readDouble();
        this.tcoste = in.readDouble();
        this.descuento = in.readString();
        this.cantidad_ant = in.readInt();
        this.linea_app = in.readInt();
        this.pension = in.readString();
        this.extras = in.createTypedArrayList(ClaseItemExtra.CREATOR);
        if (this.extras == null) {
            this.extras = new ArrayList<>();
        }
    }

    protected ClaseLineaVentas(Parcel in){
        super();
        readFromParcel(in);
    }

    protected  static final  Creator<ClaseLineaVentas> CREATOR = new Creator<ClaseLineaVentas>(){
        public ClaseLineaVentas createFromParcel (Parcel in){
            return new ClaseLineaVentas(in);
        }
        public ClaseLineaVentas[] newArray(int size){
            return new ClaseLineaVentas[size];
        }

    };


    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch( CloneNotSupportedException e )
        {
            return null;
        }
    }

    @Override
    public int compareTo(ClaseLineaVentas claseLineaVentas) {
        if (this.orden_platos == claseLineaVentas.orden_platos){
            return this.descripcion.compareTo(claseLineaVentas.descripcion);
        }
        else {
            if (this.orden_platos > claseLineaVentas.orden_platos) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }

    public static ArrayList<ClaseLineaVentas> agrupa(ArrayList<ClaseLineaVentas> lineasVentas) {
        int xorden_plato = -1;
        String xdescripcion = "";
        int cantidad = 0;
        int tCantidadOrden = 0;
        double tPeuros = 0;
        double tTeuros = 0;
        double tPCoste = 0;
        double tTCoste = 0;
        double tTEurosOrden = 0;
        String tpension = "";
        ArrayList<ClaseItemExtra> tExtras = new ArrayList<>();


        ClaseLineaVentas tmp;
        ArrayList<ClaseLineaVentas> gLineasVentas = new ArrayList<>();
        for (ClaseLineaVentas linea : lineasVentas){
            if (xorden_plato == -1 ) {
                xorden_plato = linea.orden_platos;
            }
            if (xdescripcion == "") {
                xdescripcion = linea.descripcion;
            }
            if (linea.orden_platos != xorden_plato || !linea.descripcion.equals(xdescripcion)){
                tmp = new ClaseLineaVentas();
                tmp.orden_platos = xorden_plato;
                tmp.descripcion = xdescripcion;
                tmp.cantidad = cantidad;
                tmp.pension = tpension;
                tmp.extras = tExtras;
                tmp.peuros = tPeuros+ costeExtras(1,tExtras);
                tmp.teuros = tTeuros+ costeExtras(cantidad, tExtras);

                gLineasVentas.add(tmp);

                if (linea.orden_platos != xorden_plato) {
                    tmp = new ClaseLineaVentas();
                    tmp.orden_platos = xorden_plato;
                    tmp.descripcion = "Total "+ClaseLineaVentas.ordenPlato2Desc(xorden_plato)+": "+ tCantidadOrden;
                    tmp.cantidad = tCantidadOrden;
                    tmp.peuros = 0;
                    tmp.teuros = tTEurosOrden;
                    tmp.linea_total = true;
                    gLineasVentas.add(tmp);

                    tCantidadOrden = 0;
                    tTEurosOrden = 0;
                }


                xorden_plato = linea.orden_platos;
                xdescripcion = linea.descripcion;
                cantidad = 0;
                tPeuros = 0;
                tTeuros = 0;
                tPCoste = 0;
                tTCoste = 0;
                tpension = "";
                tExtras = new ArrayList<>();


            }

            cantidad += linea.cantidad;
            tCantidadOrden += linea.cantidad;

            tPeuros = linea.peuros;
            tPCoste = linea.pcoste;
            tTeuros += linea.teuros;
            tTCoste += linea.tcoste;
            tTEurosOrden += linea.teuros+ costeExtras(linea.cantidad, linea.extras);

            tpension = linea.pension;
            tExtras = linea.extras;

        }

        if (cantidad > 0 ) {
             tmp = new ClaseLineaVentas();
             tmp.orden_platos = xorden_plato;
             tmp.descripcion = xdescripcion;
             tmp.cantidad = cantidad;
             tmp.pension = tpension;
             tmp.extras = tExtras;
             tmp.peuros = tPeuros+ costeExtras(1,tExtras);
             tmp.teuros = tTeuros+ costeExtras(cantidad,tExtras);
             gLineasVentas.add(tmp);

             tmp = new ClaseLineaVentas();
             tmp.orden_platos = xorden_plato;
             tmp.descripcion = "Total "+ClaseLineaVentas.ordenPlato2Desc(xorden_plato)+": "+ tCantidadOrden;
             tmp.cantidad = tCantidadOrden;
             tmp.peuros = 0;
             tmp.teuros = tTEurosOrden;
             tmp.linea_total = true;
             gLineasVentas.add(tmp);

        }
        return  gLineasVentas;
    }

    private static String ordenPlato2Desc(int orden){
        if (orden == 1) return "Entrantes";
        else if (orden == 2) return "Primeros";
        else if (orden == 3) return "Segundos";
        else if (orden == 4) return "Postres";
        else return "Bebidas";


    }

    public int getLinea_app() {
        return linea_app;
    }

    public boolean tieneExtras() {
        return extras != null && !extras.isEmpty();
    }

    public static  double costeExtras(int cantidad, ArrayList<ClaseItemExtra> extras) {
        double pExtras = 0.00;
        for (ClaseItemExtra extra: extras){
            if (extra.tipo.equalsIgnoreCase("E") && extra.estadoExtra == ClaseItemExtra.ESTADO_CON){
                pExtras += Double.parseDouble(extra.precio);
            }
        }
        return  cantidad* pExtras;
    }
}
