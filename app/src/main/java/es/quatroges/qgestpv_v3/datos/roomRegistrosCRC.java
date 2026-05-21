package es.quatroges.qgestpv_v3.datos;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class roomRegistrosCRC {

    @SerializedName("idfila")
    @Expose
    private int idfila;

    @SerializedName("crc")
    @Expose
    private String crc;

    public roomRegistrosCRC() {
    }

    public int getIdfila() {
        return idfila;
    }

    public void setIdfila(int idfila) {
        this.idfila = idfila;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    @Entity(tableName = "versiontablas", primaryKeys = {"tabla"})
    public static class Versiontablas implements Parcelable {

        @SerializedName("tabla")
        @Expose
        @NonNull
        private String tabla;

        @SerializedName("version")
        @Expose
        private String version;

        @Expose(deserialize = false, serialize = false)
        private String updated;

        public Versiontablas() {
        }

        @Ignore
        public Versiontablas(@NonNull String tabla, String version, String updated) {
            this.tabla = tabla;
            this.version = version;
            this.updated = updated;
        }

        @NonNull
        public String getTabla() {
            return tabla;
        }

        public void setTabla(@NonNull String tabla) {
            this.tabla = tabla;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        // parcelable
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(tabla);
            dest.writeString(version);
            dest.writeString(updated);
        }

        public void readFromParcel(Parcel in){
            this.tabla= in.readString();
            this.version= in.readString();
            this.updated = in.readString();
        }

        protected Versiontablas (Parcel in){
            super();
            readFromParcel(in);
        }

        public static final  Creator<Versiontablas> CREATOR = new Creator<Versiontablas>(){
            public Versiontablas createFromParcel (Parcel in){
                return new Versiontablas(in);
            }
            public Versiontablas[] newArray(int size){return new Versiontablas[size];}
        };
    }
}
