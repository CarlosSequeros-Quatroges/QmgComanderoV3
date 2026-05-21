package es.quatroges.qgestpv_v3.basedatos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.Alergenos;
import es.quatroges.qgestpv_v3.datos.roomRegistrosCRC;

@Dao
public interface DaoAlergenos {
    @Query("select count(*) from Alergenos")
    Integer recuperaCount();

    @Query("select min(codigo) from Alergenos")
    Integer recuperaMin();

    @Query("select max(codigo) from Alergenos")
    Integer recuperaMax();

    @Query ("select * from Alergenos")
    List<Alergenos> recuperaTodo();

    @Query ("select codigo as  idfila,ifnull(md5,'')  as crc from Alergenos where codigo between :min and :max")
    List<roomRegistrosCRC> recuperaMD5(int min, int max);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRegistro(Alergenos alergeno);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateRegistro(Alergenos alergeno);

    @Query("delete from  Alergenos where codigo = :idfila")
    void borraRegistro(int idfila);

    @Query("delete from  Alergenos")
    void borraTodo();

    @Query("delete from  Alergenos where codigo between :min and :max")
    void borraRango(int min, int max); 

}
