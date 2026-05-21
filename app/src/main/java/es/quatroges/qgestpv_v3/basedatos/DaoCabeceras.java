package es.quatroges.qgestpv_v3.basedatos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.Cabeceras;
import es.quatroges.qgestpv_v3.datos.roomRegistrosCRC;

@Dao
public interface DaoCabeceras {
    
    @Query("select count(*) from cabeceras")
    Integer recuperaCount();

    @Query("select min(codigo) from cabeceras")
    Integer recuperaMin();

    @Query("select max(codigo) from cabeceras")
    Integer recuperaMax();

    @Query ("select * from cabeceras")
    List<Cabeceras> recuperaTodo();

    @Query ("select codigo as idfila,ifnull(md5,'')  as crc from cabeceras where codigo between :min and :max")
    List<roomRegistrosCRC> recuperaMD5(int min, int max);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRegistro(Cabeceras cabecera);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateRegistro(Cabeceras cabecera);

    @Query("delete from  cabeceras where codigo = :idfila")
    void borraRegistro(int idfila);

    @Query("delete from  cabeceras")
    void borraTodo();

    @Query("delete from  cabeceras where codigo between :min and :max")
    void borraRango(int min, int max);


    @Query ("select * from cabeceras where tmenu=:tmenu")
    List<Cabeceras> recuperaCabecerasTPV(int tmenu);
}
