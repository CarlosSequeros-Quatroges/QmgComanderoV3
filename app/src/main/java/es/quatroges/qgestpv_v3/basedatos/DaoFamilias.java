package es.quatroges.qgestpv_v3.basedatos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.Cabeceras;
import es.quatroges.qgestpv_v3.datos.Familias;
import es.quatroges.qgestpv_v3.datos.roomRegistrosCRC;

@Dao
public interface DaoFamilias {
    
    @Query("select count(*) from familias")
    Integer recuperaCount();

    @Query("select min(codfam) from familias")
    Integer recuperaMin();

    @Query("select max(codfam) from familias")
    Integer recuperaMax();

    @Query ("select * from familias")
    List<Familias> recuperaTodo();

    @Query ("select codfam as idfila,ifnull(md5,'')  as crc from familias where codfam between :min and :max")
    List<roomRegistrosCRC> recuperaMD5(int min, int max);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRegistro(Familias familias);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateRegistro(Familias familias);

    @Query("delete from  familias where codfam = :idfila")
    void borraRegistro(int idfila);

    @Query("delete from  familias")
    void borraTodo();

    @Query("delete from  familias where codfam between :min and :max")
    void borraRango(int min, int max);

    }
