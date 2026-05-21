package es.quatroges.qgestpv_v3.basedatos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.Nom_Mesas;
import es.quatroges.qgestpv_v3.datos.roomRegistrosCRC;

@Dao
public interface DaoNomMesas {


    @Query("select count(*) from Nom_Mesas")
    Integer recuperaCount();

    @Query("select min(rowid) from Nom_Mesas")
    Integer recuperaMin();

    @Query("select max(rowid) from Nom_Mesas")
    Integer recuperaMax();

    @Query ("select * from Nom_Mesas")
    List<Nom_Mesas> recuperaTodo();

    @Query ("select rowid as idfila,ifnull(md5,'') as crc  from Nom_Mesas where rowid between :min and :max")
    List<roomRegistrosCRC> recuperaMD5(int  min, int  max);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRegistro(Nom_Mesas nomMesa);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateRegistro(Nom_Mesas nomMesa);

    @Query("delete from  Nom_Mesas where rowid = :idfila")
    void borraRegistro(int idfila);

    @Query("delete from  Nom_Mesas")
    void borraTodo();

    @Query("delete from  Nom_Mesas where rowid between :min and :max")
    void borraRango(int min, int max);
}
