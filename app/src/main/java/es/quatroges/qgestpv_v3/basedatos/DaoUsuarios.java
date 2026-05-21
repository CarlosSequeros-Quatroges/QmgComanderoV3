package es.quatroges.qgestpv_v3.basedatos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.Usuarios;
import es.quatroges.qgestpv_v3.datos.roomRegistrosCRC;

@Dao
public interface DaoUsuarios {
    @Query("select count(*) from usuarios")
    Integer recuperaCount();

    @Query("select min(codigo) from usuarios")
    Integer recuperaMin();

    @Query("select max(codigo) from usuarios")
    Integer recuperaMax();

    @Query ("select * from usuarios")
    List<Usuarios> recuperaTodo();

    @Query ("select codigo as idfila,ifnull(md5,'') as crc from usuarios where codigo between :min and :max")
    List<roomRegistrosCRC> recuperaMD5(int min, int max);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRegistro(Usuarios usuario);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateRegistro(Usuarios usuario);

    @Query("delete from  usuarios where codigo = :idfila")
    void borraRegistro(Integer idfila);

    @Query("delete from  usuarios")
    void borraTodo();

    @Query("delete from  usuarios where codigo between :min and  :max")
    void borraRango(int min, int max);

}
