package es.quatroges.qgestpv_v3.basedatos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.ClientesCtaCasa;
import es.quatroges.qgestpv_v3.datos.roomRegistrosCRC;

@Dao
public interface DaoClientesCtaCasa {
    @Query("select count(*) from clt_art")
    Integer recuperaCount();

    @Query("select min(codcli) from clt_art")
    Integer recuperaMin();

    @Query("select max(codcli) from clt_art")
    Integer recuperaMax();

    @Query ("select * from clt_art")
    List<ClientesCtaCasa> recuperaTodo();

    @Query ("select rowid as idfila,ifnull(md5,'') as crc  from clt_art where rowid between :min and :max")
    List<roomRegistrosCRC> recuperaMD5(int min, int max);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRegistro(ClientesCtaCasa clientesCtaCasa);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateRegistro(ClientesCtaCasa clientesCtaCasa);

    @Query("delete from  clt_art where rowid = :idfila")
    void borraRegistro(int idfila);

    @Query("delete from  clt_art")
    void borraTodo();

    @Query("delete from  clt_art where rowid  between :min and :max")
    void borraRango(int min, int max);

}
