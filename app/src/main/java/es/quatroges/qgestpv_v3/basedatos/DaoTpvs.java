package es.quatroges.qgestpv_v3.basedatos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.Tpvs;
import es.quatroges.qgestpv_v3.datos.roomRegistrosCRC;

@Dao
public interface DaoTpvs {
    @Query("select count(*) from tpvs")
    Integer recuperaCount();

    @Query("select min(rowid) from tpvs")
    Integer recuperaMin();

    @Query("select max(rowid) from tpvs")
    Integer recuperaMax();

    @Query ("select * from tpvs")
    List<Tpvs> recuperaTodo();

    @Query ("select rowid as idfila ,ifnull(md5,'') as crc  from tpvs where rowid between :min and :max")
    List<roomRegistrosCRC> recuperaMD5(int min, int max);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRegistro(Tpvs tpv);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateRegistro(Tpvs tpv);

    @Query("delete from  tpvs where rowid = :idfila")
    void borraRegistro(int idfila);

    @Query("delete from  tpvs")
    void borraTodo();

    @Query("delete from  tpvs where rowid between :min and  :max")
    void borraRango(int min, int max);

}
