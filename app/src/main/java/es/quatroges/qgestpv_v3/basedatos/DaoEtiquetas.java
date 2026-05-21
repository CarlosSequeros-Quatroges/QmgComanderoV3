package es.quatroges.qgestpv_v3.basedatos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.Etiquetas;
import es.quatroges.qgestpv_v3.datos.roomRegistrosCRC;

@Dao
public interface DaoEtiquetas {
    @Query("select count(*) from etiquetas")
    Integer recuperaCount();

    @Query("select min(codigo) from etiquetas")
    Integer recuperaMin();

    @Query("select max(codigo) from etiquetas")
    Integer recuperaMax();

    @Query ("select * from etiquetas")
    List<Etiquetas> recuperaTodo();

    @Query ("select codigo as  idfila,ifnull(md5,'') as crc from etiquetas where codigo between :min and :max")
    List<roomRegistrosCRC> recuperaMD5(int min, int max);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRegistro(Etiquetas etiqueta);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateRegistro(Etiquetas etiqueta);

    @Query("delete from  etiquetas where codigo = :idfila")
    void borraRegistro(int idfila);

    @Query("delete from  etiquetas")
    void borraTodo();

    @Query("delete from  etiquetas where codigo  between :min and :max")
    void borraRango(int min, int max);


}
