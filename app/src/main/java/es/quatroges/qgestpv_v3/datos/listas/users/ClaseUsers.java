package es.quatroges.qgestpv_v3.datos.listas.users;

import android.os.Parcel;
import android.os.Parcelable;

public class ClaseUsers implements Parcelable {
    public int codigo;
    public String nombre;
    public String clave;

    public ClaseUsers(int codigo, String nombre, String clave) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.clave = clave;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.codigo);
        dest.writeString(this.nombre);
        dest.writeString(this.clave);
    }
    public void readFromParcel(Parcel in){
        this.codigo = in.readInt();
        this.nombre= in.readString();
        this.clave = in.readString();
    }

    protected ClaseUsers(Parcel in){
        super();
        readFromParcel(in);
    }

    protected  static final  Creator<ClaseUsers> CREATOR = new Creator<ClaseUsers>(){
        public ClaseUsers createFromParcel (Parcel in){
            return new ClaseUsers(in);
        }
        public ClaseUsers[] newArray(int size){
            return new ClaseUsers[size];
        }

    };


/*    public static ArrayList<ClaseUsers> recuperaUsers(){


        ArrayList<ClaseUsers>  listaUsers= new ArrayList<>();
        listaUsers.clear();
        listaUsers.add(new ClaseUsers(1,"CARLOS",""));
        listaUsers.add(new ClaseUsers(2,"ORLANDO","2345"));
        listaUsers.add(new ClaseUsers(3,"YERAY","3456"));
        listaUsers.add(new ClaseUsers(4,"ALEX","4567"));
        listaUsers.add(new ClaseUsers(5,"PACO","5678"));
        return listaUsers;
    }
 */
}
