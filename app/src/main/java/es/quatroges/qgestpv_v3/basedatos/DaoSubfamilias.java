package es.quatroges.qgestpv_v3.basedatos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.Subfamilias;
import es.quatroges.qgestpv_v3.datos.roomRegistrosCRC;

@Dao
public interface DaoSubfamilias {
    
    @Query("select count(*) from subfamilias")
    Integer recuperaCount();

    @Query("select min(codsub) from subfamilias")
    Integer recuperaMin();

    @Query("select max(codsub) from subfamilias")
    Integer recuperaMax();

    @Query ("select * from subfamilias")
    List<Subfamilias> recuperaTodo();

    @Query ("select codsub as idfila,ifnull(md5,'')  as crc from subfamilias where codsub between :min and :max")
    List<roomRegistrosCRC> recuperaMD5(int min, int max);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRegistro(Subfamilias subfamilias);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateRegistro(Subfamilias subfamilias);

    @Query("delete from  subfamilias where codsub = :idfila")
    void borraRegistro(int idfila);

    @Query("delete from  subfamilias")
    void borraTodo();

    @Query("delete from  subfamilias where codsub between :min and :max")
    void borraRango(int min, int max);

    }
