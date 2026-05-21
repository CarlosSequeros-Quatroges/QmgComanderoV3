package es.quatroges.qgestpv_v3.basedatos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.quatroges.qgestpv_v3.datos.Productos;
import es.quatroges.qgestpv_v3.datos.roomRegistrosCRC;

@Dao
public interface DaoProductos {
    
    @Query("select count(*) from productos")
    Integer recuperaCount();

    @Query("select min(rowid) from productos")
    Integer recuperaMin();

    @Query("select max(rowid) from productos")
    Integer recuperaMax();

    @Query ("select * from productos")
    List<Productos> recuperaTodo();

    @Query ("select rowid as idfila,ifnull(md5,'') as crc  from productos where rowid between :min and :max")
    List<roomRegistrosCRC> recuperaMD5(int min, int max);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRegistro(Productos producto);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateRegistro(Productos productos);

    @Query("delete from  productos where rowid = :idfila")
    void borraRegistro(int idfila);

    @Query("delete from  productos")
    void borraTodo();

    @Query("delete from  productos where rowid between :min and :max")
    void borraRango(int min, int max);
    
    
    @Query ("select count(*) from Productos where  tmenu = :tmenu and cabecera = :cabecera and etiquetas like '%|' || :etiqueta || '|%'")
    Integer recuperaProductosCabeceraEtiquetaCount(int tmenu, int cabecera, int etiqueta);

    @Query ("select count(*) from Productos where  tmenu = :tmenu and etiquetas like '%|' || :etiqueta || '|%'")
    Integer recuperaProductosEtiquetaCount(int tmenu, int etiqueta);

    @Query ("select * from Productos where tmenu = :tmenu and cabecera = :cabecera")
    List<Productos> recuperaProductosCabecera(int tmenu, int cabecera);

    @Query ("select * from Productos where tmenu = :tmenu ")
    List<Productos> recuperaTodosProductosCabecera(int tmenu);

    @Query ("select * from Productos where codmenu = :codmenu and tmenu = :tmenu limit 1" )
    Productos recuperaProducto(String codmenu, int tmenu);



}
