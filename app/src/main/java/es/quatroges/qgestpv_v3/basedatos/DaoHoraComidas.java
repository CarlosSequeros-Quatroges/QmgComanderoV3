package es.quatroges.qgestpv_v3.basedatos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.Hora_Comidas;
import es.quatroges.qgestpv_v3.datos.roomRegistrosCRC;

@Dao
public interface DaoHoraComidas {
    
    @Query("select count(*) from Hora_Comidas")
    Integer recuperaCount();

    @Query("select min(codigo) from Hora_Comidas")
    Integer recuperaMin();

    @Query("select max(codigo) from Hora_Comidas")
    Integer recuperaMax();

    @Query ("select * from Hora_Comidas")
    List<Hora_Comidas> recuperaTodo();

    @Query ("select codigo as idfila,ifnull(md5,'')  as crc from Hora_Comidas where codigo between :min and :max")
    List<roomRegistrosCRC> recuperaMD5(int min, int max);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRegistro(Hora_Comidas horaComidas);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateRegistro(Hora_Comidas horaComidas);

    @Query("delete from  Hora_Comidas where codigo = :idfila")
    void borraRegistro(int idfila);

    @Query("delete from  Hora_Comidas")
    void borraTodo();

    @Query("delete from  Hora_Comidas where codigo between :min and :max")
    void borraRango(int min, int max);

    @Query ("select * from Hora_Comidas where codtpv = :codtpv")
    List<Hora_Comidas> recuperaHoraComidasTPV(int codtpv);

    }



