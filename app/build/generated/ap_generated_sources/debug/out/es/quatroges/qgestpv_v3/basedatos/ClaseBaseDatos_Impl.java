package es.quatroges.qgestpv_v3.basedatos;

import androidx.annotation.NonNull;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenDelegate;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class ClaseBaseDatos_Impl extends ClaseBaseDatos {
  private volatile DaoConfiguracion _daoConfiguracion;

  private volatile DaoUsuarios _daoUsuarios;

  private volatile DaoTpvs _daoTpvs;

  private volatile DaoCabeceras _daoCabeceras;

  private volatile DaoProductos _daoProductos;

  private volatile DaoClientesCtaCasa _daoClientesCtaCasa;

  private volatile DaoNomMesas _daoNomMesas;

  private volatile DaoAlergenos _daoAlergenos;

  private volatile DaoEtiquetas _daoEtiquetas;

  private volatile DaoFamilias _daoFamilias;

  private volatile DaoSubfamilias _daoSubfamilias;

  private volatile DaoHoraComidas _daoHoraComidas;

  @Override
  @NonNull
  protected RoomOpenDelegate createOpenDelegate() {
    final RoomOpenDelegate _openDelegate = new RoomOpenDelegate(7, "296feb7376708a509bf9c8cd562a3baf", "b8848fe96028e23393aa2f537da9b975") {
      @Override
      public void createAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `configuracion` (`parametro` TEXT NOT NULL, `valor` TEXT, PRIMARY KEY(`parametro`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `usuarios` (`codigo` INTEGER NOT NULL, `nombre` TEXT, `clave` TEXT, `ntarjeta` TEXT, `md5` TEXT, PRIMARY KEY(`codigo`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `tpvs` (`rowid` INTEGER NOT NULL, `codtpv` TEXT NOT NULL, `descripcion` TEXT, `numero_mesas` INTEGER NOT NULL, `tmenu` INTEGER NOT NULL, `pedir_pax` TEXT, `md5` TEXT, PRIMARY KEY(`rowid`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `nom_mesas` (`rowid` INTEGER NOT NULL, `codtpv` TEXT NOT NULL, `numero` INTEGER NOT NULL, `descripcion` TEXT, `grupo` INTEGER NOT NULL, `md5` TEXT, PRIMARY KEY(`rowid`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `cabeceras` (`codigo` INTEGER NOT NULL, `tmenu` INTEGER NOT NULL, `descripcion` TEXT, `pos` INTEGER NOT NULL, `md5` TEXT, PRIMARY KEY(`codigo`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `productos` (`rowid` INTEGER NOT NULL, `codmenu` TEXT NOT NULL, `tmenu` INTEGER NOT NULL, `cabecera` INTEGER NOT NULL, `descripcion` TEXT, `euros` TEXT, `costo` TEXT, `mprecios` TEXT, `abrevia` TEXT, `aplicar_hh` TEXT, `tipo` TEXT, `aplicar_ti` TEXT, `notas` TEXT, `alergenos` TEXT, `etiquetas` TEXT, `orden` TEXT, `orden_platos` TEXT, `codfam` TEXT, `codsub` TEXT, `es_extra` TEXT, `ver_extra` TEXT, `pensiones` TEXT, `md5` TEXT, PRIMARY KEY(`rowid`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `clt_art` (`rowid` INTEGER NOT NULL, `codcli` TEXT NOT NULL, `nombre` TEXT, `md5` TEXT, PRIMARY KEY(`rowid`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `alergenos` (`codigo` INTEGER NOT NULL, `descripcion` TEXT, `md5` TEXT, PRIMARY KEY(`codigo`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `etiquetas` (`codigo` INTEGER NOT NULL, `descripcion` TEXT, `md5` TEXT, PRIMARY KEY(`codigo`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `familias` (`codfam` INTEGER NOT NULL, `descripcion` TEXT, `md5` TEXT, PRIMARY KEY(`codfam`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `subfamilias` (`codsub` INTEGER NOT NULL, `descripcion` TEXT, `extras` TEXT, `md5` TEXT, PRIMARY KEY(`codsub`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `hora_comidas` (`codigo` INTEGER NOT NULL, `desde_hora` TEXT, `hasta_hora` TEXT, `tipo` TEXT, `codtpv` TEXT, `md5` TEXT, PRIMARY KEY(`codigo`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '296feb7376708a509bf9c8cd562a3baf')");
      }

      @Override
      public void dropAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `configuracion`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `usuarios`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `tpvs`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `nom_mesas`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `cabeceras`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `productos`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `clt_art`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `alergenos`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `etiquetas`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `familias`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `subfamilias`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `hora_comidas`");
      }

      @Override
      public void onCreate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      public void onOpen(@NonNull final SQLiteConnection connection) {
        internalInitInvalidationTracker(connection);
      }

      @Override
      public void onPreMigrate(@NonNull final SQLiteConnection connection) {
        DBUtil.dropFtsSyncTriggers(connection);
      }

      @Override
      public void onPostMigrate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      @NonNull
      public RoomOpenDelegate.ValidationResult onValidateSchema(
          @NonNull final SQLiteConnection connection) {
        final Map<String, TableInfo.Column> _columnsConfiguracion = new HashMap<String, TableInfo.Column>(2);
        _columnsConfiguracion.put("parametro", new TableInfo.Column("parametro", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConfiguracion.put("valor", new TableInfo.Column("valor", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysConfiguracion = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesConfiguracion = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoConfiguracion = new TableInfo("configuracion", _columnsConfiguracion, _foreignKeysConfiguracion, _indicesConfiguracion);
        final TableInfo _existingConfiguracion = TableInfo.read(connection, "configuracion");
        if (!_infoConfiguracion.equals(_existingConfiguracion)) {
          return new RoomOpenDelegate.ValidationResult(false, "configuracion(es.quatroges.qgestpv_v3.datos.Configuracion).\n"
                  + " Expected:\n" + _infoConfiguracion + "\n"
                  + " Found:\n" + _existingConfiguracion);
        }
        final Map<String, TableInfo.Column> _columnsUsuarios = new HashMap<String, TableInfo.Column>(5);
        _columnsUsuarios.put("codigo", new TableInfo.Column("codigo", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("nombre", new TableInfo.Column("nombre", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("clave", new TableInfo.Column("clave", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("ntarjeta", new TableInfo.Column("ntarjeta", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("md5", new TableInfo.Column("md5", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysUsuarios = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesUsuarios = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsuarios = new TableInfo("usuarios", _columnsUsuarios, _foreignKeysUsuarios, _indicesUsuarios);
        final TableInfo _existingUsuarios = TableInfo.read(connection, "usuarios");
        if (!_infoUsuarios.equals(_existingUsuarios)) {
          return new RoomOpenDelegate.ValidationResult(false, "usuarios(es.quatroges.qgestpv_v3.datos.Usuarios).\n"
                  + " Expected:\n" + _infoUsuarios + "\n"
                  + " Found:\n" + _existingUsuarios);
        }
        final Map<String, TableInfo.Column> _columnsTpvs = new HashMap<String, TableInfo.Column>(7);
        _columnsTpvs.put("rowid", new TableInfo.Column("rowid", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTpvs.put("codtpv", new TableInfo.Column("codtpv", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTpvs.put("descripcion", new TableInfo.Column("descripcion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTpvs.put("numero_mesas", new TableInfo.Column("numero_mesas", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTpvs.put("tmenu", new TableInfo.Column("tmenu", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTpvs.put("pedir_pax", new TableInfo.Column("pedir_pax", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTpvs.put("md5", new TableInfo.Column("md5", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysTpvs = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesTpvs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTpvs = new TableInfo("tpvs", _columnsTpvs, _foreignKeysTpvs, _indicesTpvs);
        final TableInfo _existingTpvs = TableInfo.read(connection, "tpvs");
        if (!_infoTpvs.equals(_existingTpvs)) {
          return new RoomOpenDelegate.ValidationResult(false, "tpvs(es.quatroges.qgestpv_v3.datos.Tpvs).\n"
                  + " Expected:\n" + _infoTpvs + "\n"
                  + " Found:\n" + _existingTpvs);
        }
        final Map<String, TableInfo.Column> _columnsNomMesas = new HashMap<String, TableInfo.Column>(6);
        _columnsNomMesas.put("rowid", new TableInfo.Column("rowid", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNomMesas.put("codtpv", new TableInfo.Column("codtpv", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNomMesas.put("numero", new TableInfo.Column("numero", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNomMesas.put("descripcion", new TableInfo.Column("descripcion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNomMesas.put("grupo", new TableInfo.Column("grupo", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNomMesas.put("md5", new TableInfo.Column("md5", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysNomMesas = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesNomMesas = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNomMesas = new TableInfo("nom_mesas", _columnsNomMesas, _foreignKeysNomMesas, _indicesNomMesas);
        final TableInfo _existingNomMesas = TableInfo.read(connection, "nom_mesas");
        if (!_infoNomMesas.equals(_existingNomMesas)) {
          return new RoomOpenDelegate.ValidationResult(false, "nom_mesas(es.quatroges.qgestpv_v3.datos.Nom_Mesas).\n"
                  + " Expected:\n" + _infoNomMesas + "\n"
                  + " Found:\n" + _existingNomMesas);
        }
        final Map<String, TableInfo.Column> _columnsCabeceras = new HashMap<String, TableInfo.Column>(5);
        _columnsCabeceras.put("codigo", new TableInfo.Column("codigo", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCabeceras.put("tmenu", new TableInfo.Column("tmenu", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCabeceras.put("descripcion", new TableInfo.Column("descripcion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCabeceras.put("pos", new TableInfo.Column("pos", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCabeceras.put("md5", new TableInfo.Column("md5", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysCabeceras = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesCabeceras = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCabeceras = new TableInfo("cabeceras", _columnsCabeceras, _foreignKeysCabeceras, _indicesCabeceras);
        final TableInfo _existingCabeceras = TableInfo.read(connection, "cabeceras");
        if (!_infoCabeceras.equals(_existingCabeceras)) {
          return new RoomOpenDelegate.ValidationResult(false, "cabeceras(es.quatroges.qgestpv_v3.datos.Cabeceras).\n"
                  + " Expected:\n" + _infoCabeceras + "\n"
                  + " Found:\n" + _existingCabeceras);
        }
        final Map<String, TableInfo.Column> _columnsProductos = new HashMap<String, TableInfo.Column>(23);
        _columnsProductos.put("rowid", new TableInfo.Column("rowid", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("codmenu", new TableInfo.Column("codmenu", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("tmenu", new TableInfo.Column("tmenu", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("cabecera", new TableInfo.Column("cabecera", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("descripcion", new TableInfo.Column("descripcion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("euros", new TableInfo.Column("euros", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("costo", new TableInfo.Column("costo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("mprecios", new TableInfo.Column("mprecios", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("abrevia", new TableInfo.Column("abrevia", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("aplicar_hh", new TableInfo.Column("aplicar_hh", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("tipo", new TableInfo.Column("tipo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("aplicar_ti", new TableInfo.Column("aplicar_ti", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("notas", new TableInfo.Column("notas", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("alergenos", new TableInfo.Column("alergenos", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("etiquetas", new TableInfo.Column("etiquetas", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("orden", new TableInfo.Column("orden", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("orden_platos", new TableInfo.Column("orden_platos", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("codfam", new TableInfo.Column("codfam", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("codsub", new TableInfo.Column("codsub", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("es_extra", new TableInfo.Column("es_extra", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("ver_extra", new TableInfo.Column("ver_extra", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("pensiones", new TableInfo.Column("pensiones", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("md5", new TableInfo.Column("md5", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysProductos = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesProductos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProductos = new TableInfo("productos", _columnsProductos, _foreignKeysProductos, _indicesProductos);
        final TableInfo _existingProductos = TableInfo.read(connection, "productos");
        if (!_infoProductos.equals(_existingProductos)) {
          return new RoomOpenDelegate.ValidationResult(false, "productos(es.quatroges.qgestpv_v3.datos.Productos).\n"
                  + " Expected:\n" + _infoProductos + "\n"
                  + " Found:\n" + _existingProductos);
        }
        final Map<String, TableInfo.Column> _columnsCltArt = new HashMap<String, TableInfo.Column>(4);
        _columnsCltArt.put("rowid", new TableInfo.Column("rowid", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCltArt.put("codcli", new TableInfo.Column("codcli", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCltArt.put("nombre", new TableInfo.Column("nombre", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCltArt.put("md5", new TableInfo.Column("md5", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysCltArt = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesCltArt = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCltArt = new TableInfo("clt_art", _columnsCltArt, _foreignKeysCltArt, _indicesCltArt);
        final TableInfo _existingCltArt = TableInfo.read(connection, "clt_art");
        if (!_infoCltArt.equals(_existingCltArt)) {
          return new RoomOpenDelegate.ValidationResult(false, "clt_art(es.quatroges.qgestpv_v3.datos.ClientesCtaCasa).\n"
                  + " Expected:\n" + _infoCltArt + "\n"
                  + " Found:\n" + _existingCltArt);
        }
        final Map<String, TableInfo.Column> _columnsAlergenos = new HashMap<String, TableInfo.Column>(3);
        _columnsAlergenos.put("codigo", new TableInfo.Column("codigo", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlergenos.put("descripcion", new TableInfo.Column("descripcion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlergenos.put("md5", new TableInfo.Column("md5", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysAlergenos = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesAlergenos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAlergenos = new TableInfo("alergenos", _columnsAlergenos, _foreignKeysAlergenos, _indicesAlergenos);
        final TableInfo _existingAlergenos = TableInfo.read(connection, "alergenos");
        if (!_infoAlergenos.equals(_existingAlergenos)) {
          return new RoomOpenDelegate.ValidationResult(false, "alergenos(es.quatroges.qgestpv_v3.datos.Alergenos).\n"
                  + " Expected:\n" + _infoAlergenos + "\n"
                  + " Found:\n" + _existingAlergenos);
        }
        final Map<String, TableInfo.Column> _columnsEtiquetas = new HashMap<String, TableInfo.Column>(3);
        _columnsEtiquetas.put("codigo", new TableInfo.Column("codigo", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEtiquetas.put("descripcion", new TableInfo.Column("descripcion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEtiquetas.put("md5", new TableInfo.Column("md5", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysEtiquetas = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesEtiquetas = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEtiquetas = new TableInfo("etiquetas", _columnsEtiquetas, _foreignKeysEtiquetas, _indicesEtiquetas);
        final TableInfo _existingEtiquetas = TableInfo.read(connection, "etiquetas");
        if (!_infoEtiquetas.equals(_existingEtiquetas)) {
          return new RoomOpenDelegate.ValidationResult(false, "etiquetas(es.quatroges.qgestpv_v3.datos.Etiquetas).\n"
                  + " Expected:\n" + _infoEtiquetas + "\n"
                  + " Found:\n" + _existingEtiquetas);
        }
        final Map<String, TableInfo.Column> _columnsFamilias = new HashMap<String, TableInfo.Column>(3);
        _columnsFamilias.put("codfam", new TableInfo.Column("codfam", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFamilias.put("descripcion", new TableInfo.Column("descripcion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFamilias.put("md5", new TableInfo.Column("md5", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysFamilias = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesFamilias = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFamilias = new TableInfo("familias", _columnsFamilias, _foreignKeysFamilias, _indicesFamilias);
        final TableInfo _existingFamilias = TableInfo.read(connection, "familias");
        if (!_infoFamilias.equals(_existingFamilias)) {
          return new RoomOpenDelegate.ValidationResult(false, "familias(es.quatroges.qgestpv_v3.datos.Familias).\n"
                  + " Expected:\n" + _infoFamilias + "\n"
                  + " Found:\n" + _existingFamilias);
        }
        final Map<String, TableInfo.Column> _columnsSubfamilias = new HashMap<String, TableInfo.Column>(4);
        _columnsSubfamilias.put("codsub", new TableInfo.Column("codsub", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubfamilias.put("descripcion", new TableInfo.Column("descripcion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubfamilias.put("extras", new TableInfo.Column("extras", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubfamilias.put("md5", new TableInfo.Column("md5", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysSubfamilias = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesSubfamilias = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSubfamilias = new TableInfo("subfamilias", _columnsSubfamilias, _foreignKeysSubfamilias, _indicesSubfamilias);
        final TableInfo _existingSubfamilias = TableInfo.read(connection, "subfamilias");
        if (!_infoSubfamilias.equals(_existingSubfamilias)) {
          return new RoomOpenDelegate.ValidationResult(false, "subfamilias(es.quatroges.qgestpv_v3.datos.Subfamilias).\n"
                  + " Expected:\n" + _infoSubfamilias + "\n"
                  + " Found:\n" + _existingSubfamilias);
        }
        final Map<String, TableInfo.Column> _columnsHoraComidas = new HashMap<String, TableInfo.Column>(6);
        _columnsHoraComidas.put("codigo", new TableInfo.Column("codigo", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHoraComidas.put("desde_hora", new TableInfo.Column("desde_hora", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHoraComidas.put("hasta_hora", new TableInfo.Column("hasta_hora", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHoraComidas.put("tipo", new TableInfo.Column("tipo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHoraComidas.put("codtpv", new TableInfo.Column("codtpv", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHoraComidas.put("md5", new TableInfo.Column("md5", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysHoraComidas = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesHoraComidas = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHoraComidas = new TableInfo("hora_comidas", _columnsHoraComidas, _foreignKeysHoraComidas, _indicesHoraComidas);
        final TableInfo _existingHoraComidas = TableInfo.read(connection, "hora_comidas");
        if (!_infoHoraComidas.equals(_existingHoraComidas)) {
          return new RoomOpenDelegate.ValidationResult(false, "hora_comidas(es.quatroges.qgestpv_v3.datos.Hora_Comidas).\n"
                  + " Expected:\n" + _infoHoraComidas + "\n"
                  + " Found:\n" + _existingHoraComidas);
        }
        return new RoomOpenDelegate.ValidationResult(true, null);
      }
    };
    return _openDelegate;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final Map<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final Map<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "configuracion", "usuarios", "tpvs", "nom_mesas", "cabeceras", "productos", "clt_art", "alergenos", "etiquetas", "familias", "subfamilias", "hora_comidas");
  }

  @Override
  public void clearAllTables() {
    super.performClear(false, "configuracion", "usuarios", "tpvs", "nom_mesas", "cabeceras", "productos", "clt_art", "alergenos", "etiquetas", "familias", "subfamilias", "hora_comidas");
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final Map<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(DaoConfiguracion.class, DaoConfiguracion_Impl.getRequiredConverters());
    _typeConvertersMap.put(DaoUsuarios.class, DaoUsuarios_Impl.getRequiredConverters());
    _typeConvertersMap.put(DaoTpvs.class, DaoTpvs_Impl.getRequiredConverters());
    _typeConvertersMap.put(DaoCabeceras.class, DaoCabeceras_Impl.getRequiredConverters());
    _typeConvertersMap.put(DaoProductos.class, DaoProductos_Impl.getRequiredConverters());
    _typeConvertersMap.put(DaoClientesCtaCasa.class, DaoClientesCtaCasa_Impl.getRequiredConverters());
    _typeConvertersMap.put(DaoNomMesas.class, DaoNomMesas_Impl.getRequiredConverters());
    _typeConvertersMap.put(DaoAlergenos.class, DaoAlergenos_Impl.getRequiredConverters());
    _typeConvertersMap.put(DaoEtiquetas.class, DaoEtiquetas_Impl.getRequiredConverters());
    _typeConvertersMap.put(DaoFamilias.class, DaoFamilias_Impl.getRequiredConverters());
    _typeConvertersMap.put(DaoSubfamilias.class, DaoSubfamilias_Impl.getRequiredConverters());
    _typeConvertersMap.put(DaoHoraComidas.class, DaoHoraComidas_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final Set<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public DaoConfiguracion configuracionDAO() {
    if (_daoConfiguracion != null) {
      return _daoConfiguracion;
    } else {
      synchronized(this) {
        if(_daoConfiguracion == null) {
          _daoConfiguracion = new DaoConfiguracion_Impl(this);
        }
        return _daoConfiguracion;
      }
    }
  }

  @Override
  public DaoUsuarios usuariosDao() {
    if (_daoUsuarios != null) {
      return _daoUsuarios;
    } else {
      synchronized(this) {
        if(_daoUsuarios == null) {
          _daoUsuarios = new DaoUsuarios_Impl(this);
        }
        return _daoUsuarios;
      }
    }
  }

  @Override
  public DaoTpvs tpvsDao() {
    if (_daoTpvs != null) {
      return _daoTpvs;
    } else {
      synchronized(this) {
        if(_daoTpvs == null) {
          _daoTpvs = new DaoTpvs_Impl(this);
        }
        return _daoTpvs;
      }
    }
  }

  @Override
  public DaoCabeceras cabecerasDAO() {
    if (_daoCabeceras != null) {
      return _daoCabeceras;
    } else {
      synchronized(this) {
        if(_daoCabeceras == null) {
          _daoCabeceras = new DaoCabeceras_Impl(this);
        }
        return _daoCabeceras;
      }
    }
  }

  @Override
  public DaoProductos productosDAO() {
    if (_daoProductos != null) {
      return _daoProductos;
    } else {
      synchronized(this) {
        if(_daoProductos == null) {
          _daoProductos = new DaoProductos_Impl(this);
        }
        return _daoProductos;
      }
    }
  }

  @Override
  public DaoClientesCtaCasa clientesCtaCasaDAO() {
    if (_daoClientesCtaCasa != null) {
      return _daoClientesCtaCasa;
    } else {
      synchronized(this) {
        if(_daoClientesCtaCasa == null) {
          _daoClientesCtaCasa = new DaoClientesCtaCasa_Impl(this);
        }
        return _daoClientesCtaCasa;
      }
    }
  }

  @Override
  public DaoNomMesas nomMesasDao() {
    if (_daoNomMesas != null) {
      return _daoNomMesas;
    } else {
      synchronized(this) {
        if(_daoNomMesas == null) {
          _daoNomMesas = new DaoNomMesas_Impl(this);
        }
        return _daoNomMesas;
      }
    }
  }

  @Override
  public DaoAlergenos alergenosDao() {
    if (_daoAlergenos != null) {
      return _daoAlergenos;
    } else {
      synchronized(this) {
        if(_daoAlergenos == null) {
          _daoAlergenos = new DaoAlergenos_Impl(this);
        }
        return _daoAlergenos;
      }
    }
  }

  @Override
  public DaoEtiquetas etiquetasDao() {
    if (_daoEtiquetas != null) {
      return _daoEtiquetas;
    } else {
      synchronized(this) {
        if(_daoEtiquetas == null) {
          _daoEtiquetas = new DaoEtiquetas_Impl(this);
        }
        return _daoEtiquetas;
      }
    }
  }

  @Override
  public DaoFamilias familiasDao() {
    if (_daoFamilias != null) {
      return _daoFamilias;
    } else {
      synchronized(this) {
        if(_daoFamilias == null) {
          _daoFamilias = new DaoFamilias_Impl(this);
        }
        return _daoFamilias;
      }
    }
  }

  @Override
  public DaoSubfamilias subfamiliasDao() {
    if (_daoSubfamilias != null) {
      return _daoSubfamilias;
    } else {
      synchronized(this) {
        if(_daoSubfamilias == null) {
          _daoSubfamilias = new DaoSubfamilias_Impl(this);
        }
        return _daoSubfamilias;
      }
    }
  }

  @Override
  public DaoHoraComidas horaComidasDao() {
    if (_daoHoraComidas != null) {
      return _daoHoraComidas;
    } else {
      synchronized(this) {
        if(_daoHoraComidas == null) {
          _daoHoraComidas = new DaoHoraComidas_Impl(this);
        }
        return _daoHoraComidas;
      }
    }
  }
}
