package es.quatroges.qgestpv_v3.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import es.quatroges.qgestpv_v3.datos.Productos;

public class ClaseItemExtra implements Parcelable {
    public static final int ESTADO_NADA = 0;
    public static final int ESTADO_CON = 1;
    public static final int ESTADO_SIN = 2;

    @SerializedName("codigo")
    @Expose
    public int codigo;

    @SerializedName("nota")
    @Expose
    public String nota;

    @SerializedName("hora")
    @Expose
    public String hora;

    @SerializedName("tipo")
    @Expose
    public String tipo;


    @SerializedName("descripcion")
    @Expose
    public transient String descripcion;
    @SerializedName("peuros")
    @Expose
    public transient String precio;

    @SerializedName("codmenu")
    @Expose
    public transient String codmenu;

    public transient int estadoExtra;  //con sin....
    public ClaseUtils.enEstado estado;

    public ClaseItemExtra() {
        this.codigo = 0;
        this.nota = "";
        this.hora = "";
        this.tipo = "E";
        this.descripcion = "";
        this.precio = "";
        this.estadoExtra = ESTADO_NADA;
        this.estado = ClaseUtils.enEstado.transmitida;

        this.descripcion = "";
        this.precio = "";
        this.codmenu = "";
    }

    //para añadir una nota
    public ClaseItemExtra(int codigo,String tipo,  String nota, String hora, ClaseUtils.enEstado estado) {
        this();
        this.codigo = codigo;
        this.tipo = tipo;
        this.nota = nota;
        this.hora = hora;
        this.estado = estado;

    }

    //para añadir un extra
    public ClaseItemExtra(int codigo,String tipo,  String codmenu, String precio, String descripcion, String hora, ClaseUtils.enEstado estado) {
        this();
        this.codigo = codigo;
        this.tipo = tipo;
        this.codmenu = codmenu;
        this.precio = precio;
        this.descripcion = descripcion;
        this.hora = hora;
        this.estado = estado;

    }


    public ClaseItemExtra(int codigo, String descripcion, String precio, int estadoExtra) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.estadoExtra = estadoExtra;
        this.nota = descripcion;
        this.hora = "";
        this.tipo = "E";
        this.estado = ClaseUtils.enEstado.transmitida;
    }

    public ClaseItemExtra(int codigo, String descripcion, String precio, boolean con) {
        this(codigo, descripcion, precio, con ? ESTADO_CON : ESTADO_SIN);
    }

    public ClaseItemExtra(int codigo, String nota, String hora, String tipo) {
        this.codigo = codigo;
        this.nota = nota;
        this.hora = hora;
        this.tipo = tipo;
        this.descripcion = nota;
        this.precio = "";
        this.estadoExtra = ESTADO_NADA;
        this.estado = ClaseUtils.enEstado.transmitida;
    }

    public ClaseItemExtra(ClaseItemExtra other) {
        this.codigo = other != null  ? other.codigo : 0;
        this.nota = other != null && other.nota != null ? other.nota : "";
        this.hora = other != null && other.hora != null ? other.hora : "";
        this.tipo = other != null && other.tipo != null ? other.tipo : "E";
        this.descripcion = other != null && other.descripcion != null ? other.descripcion : "";
        this.precio = other != null && other.precio != null ? other.precio : "";
        this.estadoExtra = other != null ? other.estadoExtra : ESTADO_NADA;
        this.estado = other != null && other.estado != null ? other.estado : ClaseUtils.enEstado.transmitida;
    }

    public static ClaseItemExtra fromProducto(Productos producto) {
        if (producto == null) {
            return null;
        }

        ClaseItemExtra item = new ClaseItemExtra(0,"E", producto.getCodmenu(),producto.getEuros(),producto.getDescripcion(),ClaseUtils.now("HH:mm:ss"), ClaseUtils.enEstado.transmitida);
        return item;
    }

    public static ArrayList<ClaseItemExtra> fromProductos(List<Productos> productos) {
        ArrayList<ClaseItemExtra> items = new ArrayList<>();
        if (productos == null || productos.isEmpty()) {
            return items;
        }

        for (Productos producto : productos) {
            ClaseItemExtra item = fromProducto(producto);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.codigo);
        dest.writeString(this.nota);
        dest.writeString(this.hora);
        dest.writeString(this.tipo);
        dest.writeString(this.descripcion);
        dest.writeString(this.precio);
        dest.writeInt(this.estadoExtra);
        dest.writeString(this.estado != null ? this.estado.name() : ClaseUtils.enEstado.transmitida.name());

        dest.writeString(this.descripcion);
        dest.writeString(this.precio);
        dest.writeString(this.codmenu);
    }

    public void readFromParcel(Parcel in) {
        this.codigo = in.readInt();
        this.nota = in.readString();
        this.hora = in.readString();
        this.tipo = in.readString();
        this.descripcion = in.readString();
        this.precio = in.readString();
        this.estadoExtra = in.readInt();
        String estadoLeido = in.readString();
        try {
            this.estado = estadoLeido != null ? ClaseUtils.enEstado.valueOf(estadoLeido) : ClaseUtils.enEstado.transmitida;
        } catch (Exception e) {
            this.estado = ClaseUtils.enEstado.transmitida;
        }

        this.descripcion = in.readString();
        this.precio = in.readString();
        this.codmenu = in.readString();
    }

    protected ClaseItemExtra(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Creator<ClaseItemExtra> CREATOR = new Creator<ClaseItemExtra>() {
        public ClaseItemExtra createFromParcel(Parcel in) {
            return new ClaseItemExtra(in);
        }

        public ClaseItemExtra[] newArray(int size) {
            return new ClaseItemExtra[size];
        }
    };
}
