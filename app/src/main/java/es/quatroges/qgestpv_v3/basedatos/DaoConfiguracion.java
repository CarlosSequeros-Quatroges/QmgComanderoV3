package es.quatroges.qgestpv_v3.basedatos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.Configuracion;

@Dao
public interface DaoConfiguracion {
    @Query ("select count(*) from Configuracion")
    Integer recuperaConfiguracionCount();

    @Query ("select * from Configuracion")
    List<Configuracion> recuperaConfiguracion();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    long insertConfiguracion(Configuracion configuracion);

    @Update (onConflict = OnConflictStrategy.IGNORE)
    void updateConfiguracion(Configuracion configuracion);

    @Query("delete from  configuracion")
    void borraConfiguracion();


}
