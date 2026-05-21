package es.quatroges.qgestpv_v3.basedatos;

import androidx.annotation.NonNull;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import es.quatroges.qgestpv_v3.datos.Productos;
import es.quatroges.qgestpv_v3.datos.roomRegistrosCRC;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class DaoProductos_Impl implements DaoProductos {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Productos> __insertAdapterOfProductos;

  private final EntityDeleteOrUpdateAdapter<Productos> __updateAdapterOfProductos;

  public DaoProductos_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfProductos = new EntityInsertAdapter<Productos>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `productos` (`rowid`,`codmenu`,`tmenu`,`cabecera`,`descripcion`,`euros`,`costo`,`mprecios`,`abrevia`,`aplicar_hh`,`tipo`,`aplicar_ti`,`notas`,`alergenos`,`etiquetas`,`orden`,`orden_platos`,`codfam`,`codsub`,`es_extra`,`ver_extra`,`pensiones`,`md5`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Productos entity) {
        if (entity.getRowid() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getRowid());
        }
        if (entity.getCodmenu() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getCodmenu());
        }
        statement.bindLong(3, entity.getTmenu());
        statement.bindLong(4, entity.getCabecera());
        if (entity.getDescripcion() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getDescripcion());
        }
        if (entity.getEuros() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getEuros());
        }
        if (entity.getCosto() == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.getCosto());
        }
        if (entity.getMprecios() == null) {
          statement.bindNull(8);
        } else {
          statement.bindText(8, entity.getMprecios());
        }
        if (entity.getAbrevia() == null) {
          statement.bindNull(9);
        } else {
          statement.bindText(9, entity.getAbrevia());
        }
        if (entity.getAplicar_hh() == null) {
          statement.bindNull(10);
        } else {
          statement.bindText(10, entity.getAplicar_hh());
        }
        if (entity.getTipo() == null) {
          statement.bindNull(11);
        } else {
          statement.bindText(11, entity.getTipo());
        }
        if (entity.getAplicar_ti() == null) {
          statement.bindNull(12);
        } else {
          statement.bindText(12, entity.getAplicar_ti());
        }
        if (entity.getNotas() == null) {
          statement.bindNull(13);
        } else {
          statement.bindText(13, entity.getNotas());
        }
        if (entity.getAlergenos() == null) {
          statement.bindNull(14);
        } else {
          statement.bindText(14, entity.getAlergenos());
        }
        if (entity.getEtiquetas() == null) {
          statement.bindNull(15);
        } else {
          statement.bindText(15, entity.getEtiquetas());
        }
        if (entity.getOrden() == null) {
          statement.bindNull(16);
        } else {
          statement.bindText(16, entity.getOrden());
        }
        if (entity.getOrden_platos() == null) {
          statement.bindNull(17);
        } else {
          statement.bindText(17, entity.getOrden_platos());
        }
        if (entity.getCodfam() == null) {
          statement.bindNull(18);
        } else {
          statement.bindText(18, entity.getCodfam());
        }
        if (entity.getCodsub() == null) {
          statement.bindNull(19);
        } else {
          statement.bindText(19, entity.getCodsub());
        }
        if (entity.getEs_extra() == null) {
          statement.bindNull(20);
        } else {
          statement.bindText(20, entity.getEs_extra());
        }
        if (entity.getVer_extra() == null) {
          statement.bindNull(21);
        } else {
          statement.bindText(21, entity.getVer_extra());
        }
        if (entity.getPensiones() == null) {
          statement.bindNull(22);
        } else {
          statement.bindText(22, entity.getPensiones());
        }
        if (entity.getMd5() == null) {
          statement.bindNull(23);
        } else {
          statement.bindText(23, entity.getMd5());
        }
      }
    };
    this.__updateAdapterOfProductos = new EntityDeleteOrUpdateAdapter<Productos>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR IGNORE `productos` SET `rowid` = ?,`codmenu` = ?,`tmenu` = ?,`cabecera` = ?,`descripcion` = ?,`euros` = ?,`costo` = ?,`mprecios` = ?,`abrevia` = ?,`aplicar_hh` = ?,`tipo` = ?,`aplicar_ti` = ?,`notas` = ?,`alergenos` = ?,`etiquetas` = ?,`orden` = ?,`orden_platos` = ?,`codfam` = ?,`codsub` = ?,`es_extra` = ?,`ver_extra` = ?,`pensiones` = ?,`md5` = ? WHERE `rowid` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Productos entity) {
        if (entity.getRowid() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getRowid());
        }
        if (entity.getCodmenu() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getCodmenu());
        }
        statement.bindLong(3, entity.getTmenu());
        statement.bindLong(4, entity.getCabecera());
        if (entity.getDescripcion() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getDescripcion());
        }
        if (entity.getEuros() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getEuros());
        }
        if (entity.getCosto() == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.getCosto());
        }
        if (entity.getMprecios() == null) {
          statement.bindNull(8);
        } else {
          statement.bindText(8, entity.getMprecios());
        }
        if (entity.getAbrevia() == null) {
          statement.bindNull(9);
        } else {
          statement.bindText(9, entity.getAbrevia());
        }
        if (entity.getAplicar_hh() == null) {
          statement.bindNull(10);
        } else {
          statement.bindText(10, entity.getAplicar_hh());
        }
        if (entity.getTipo() == null) {
          statement.bindNull(11);
        } else {
          statement.bindText(11, entity.getTipo());
        }
        if (entity.getAplicar_ti() == null) {
          statement.bindNull(12);
        } else {
          statement.bindText(12, entity.getAplicar_ti());
        }
        if (entity.getNotas() == null) {
          statement.bindNull(13);
        } else {
          statement.bindText(13, entity.getNotas());
        }
        if (entity.getAlergenos() == null) {
          statement.bindNull(14);
        } else {
          statement.bindText(14, entity.getAlergenos());
        }
        if (entity.getEtiquetas() == null) {
          statement.bindNull(15);
        } else {
          statement.bindText(15, entity.getEtiquetas());
        }
        if (entity.getOrden() == null) {
          statement.bindNull(16);
        } else {
          statement.bindText(16, entity.getOrden());
        }
        if (entity.getOrden_platos() == null) {
          statement.bindNull(17);
        } else {
          statement.bindText(17, entity.getOrden_platos());
        }
        if (entity.getCodfam() == null) {
          statement.bindNull(18);
        } else {
          statement.bindText(18, entity.getCodfam());
        }
        if (entity.getCodsub() == null) {
          statement.bindNull(19);
        } else {
          statement.bindText(19, entity.getCodsub());
        }
        if (entity.getEs_extra() == null) {
          statement.bindNull(20);
        } else {
          statement.bindText(20, entity.getEs_extra());
        }
        if (entity.getVer_extra() == null) {
          statement.bindNull(21);
        } else {
          statement.bindText(21, entity.getVer_extra());
        }
        if (entity.getPensiones() == null) {
          statement.bindNull(22);
        } else {
          statement.bindText(22, entity.getPensiones());
        }
        if (entity.getMd5() == null) {
          statement.bindNull(23);
        } else {
          statement.bindText(23, entity.getMd5());
        }
        if (entity.getRowid() == null) {
          statement.bindNull(24);
        } else {
          statement.bindLong(24, entity.getRowid());
        }
      }
    };
  }

  @Override
  public long insertRegistro(final Productos producto) {
    return DBUtil.performBlocking(__db, false, true, (_connection) -> {
      return __insertAdapterOfProductos.insertAndReturnId(_connection, producto);
    });
  }

  @Override
  public void updateRegistro(final Productos productos) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfProductos.handle(_connection, productos);
      return null;
    });
  }

  @Override
  public Integer recuperaCount() {
    final String _sql = "select count(*) from productos";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final Integer _result;
        if (_stmt.step()) {
          final Integer _tmp;
          if (_stmt.isNull(0)) {
            _tmp = null;
          } else {
            _tmp = (int) (_stmt.getLong(0));
          }
          _result = _tmp;
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Integer recuperaMin() {
    final String _sql = "select min(rowid) from productos";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final Integer _result;
        if (_stmt.step()) {
          final Integer _tmp;
          if (_stmt.isNull(0)) {
            _tmp = null;
          } else {
            _tmp = (int) (_stmt.getLong(0));
          }
          _result = _tmp;
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Integer recuperaMax() {
    final String _sql = "select max(rowid) from productos";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final Integer _result;
        if (_stmt.step()) {
          final Integer _tmp;
          if (_stmt.isNull(0)) {
            _tmp = null;
          } else {
            _tmp = (int) (_stmt.getLong(0));
          }
          _result = _tmp;
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<Productos> recuperaTodo() {
    final String _sql = "select * from productos";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfRowid = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "rowid");
        final int _columnIndexOfCodmenu = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codmenu");
        final int _columnIndexOfTmenu = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tmenu");
        final int _columnIndexOfCabecera = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cabecera");
        final int _columnIndexOfDescripcion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "descripcion");
        final int _columnIndexOfEuros = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "euros");
        final int _columnIndexOfCosto = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "costo");
        final int _columnIndexOfMprecios = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "mprecios");
        final int _columnIndexOfAbrevia = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "abrevia");
        final int _columnIndexOfAplicarHh = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "aplicar_hh");
        final int _columnIndexOfTipo = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tipo");
        final int _columnIndexOfAplicarTi = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "aplicar_ti");
        final int _columnIndexOfNotas = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "notas");
        final int _columnIndexOfAlergenos = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "alergenos");
        final int _columnIndexOfEtiquetas = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "etiquetas");
        final int _columnIndexOfOrden = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "orden");
        final int _columnIndexOfOrdenPlatos = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "orden_platos");
        final int _columnIndexOfCodfam = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codfam");
        final int _columnIndexOfCodsub = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codsub");
        final int _columnIndexOfEsExtra = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "es_extra");
        final int _columnIndexOfVerExtra = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "ver_extra");
        final int _columnIndexOfPensiones = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "pensiones");
        final int _columnIndexOfMd5 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "md5");
        final List<Productos> _result = new ArrayList<Productos>();
        while (_stmt.step()) {
          final Productos _item;
          _item = new Productos();
          final Integer _tmpRowid;
          if (_stmt.isNull(_columnIndexOfRowid)) {
            _tmpRowid = null;
          } else {
            _tmpRowid = (int) (_stmt.getLong(_columnIndexOfRowid));
          }
          _item.setRowid(_tmpRowid);
          final String _tmpCodmenu;
          if (_stmt.isNull(_columnIndexOfCodmenu)) {
            _tmpCodmenu = null;
          } else {
            _tmpCodmenu = _stmt.getText(_columnIndexOfCodmenu);
          }
          _item.setCodmenu(_tmpCodmenu);
          final int _tmpTmenu;
          _tmpTmenu = (int) (_stmt.getLong(_columnIndexOfTmenu));
          _item.setTmenu(_tmpTmenu);
          final int _tmpCabecera;
          _tmpCabecera = (int) (_stmt.getLong(_columnIndexOfCabecera));
          _item.setCabecera(_tmpCabecera);
          final String _tmpDescripcion;
          if (_stmt.isNull(_columnIndexOfDescripcion)) {
            _tmpDescripcion = null;
          } else {
            _tmpDescripcion = _stmt.getText(_columnIndexOfDescripcion);
          }
          _item.setDescripcion(_tmpDescripcion);
          final String _tmpEuros;
          if (_stmt.isNull(_columnIndexOfEuros)) {
            _tmpEuros = null;
          } else {
            _tmpEuros = _stmt.getText(_columnIndexOfEuros);
          }
          _item.setEuros(_tmpEuros);
          final String _tmpCosto;
          if (_stmt.isNull(_columnIndexOfCosto)) {
            _tmpCosto = null;
          } else {
            _tmpCosto = _stmt.getText(_columnIndexOfCosto);
          }
          _item.setCosto(_tmpCosto);
          final String _tmpMprecios;
          if (_stmt.isNull(_columnIndexOfMprecios)) {
            _tmpMprecios = null;
          } else {
            _tmpMprecios = _stmt.getText(_columnIndexOfMprecios);
          }
          _item.setMprecios(_tmpMprecios);
          final String _tmpAbrevia;
          if (_stmt.isNull(_columnIndexOfAbrevia)) {
            _tmpAbrevia = null;
          } else {
            _tmpAbrevia = _stmt.getText(_columnIndexOfAbrevia);
          }
          _item.setAbrevia(_tmpAbrevia);
          final String _tmpAplicar_hh;
          if (_stmt.isNull(_columnIndexOfAplicarHh)) {
            _tmpAplicar_hh = null;
          } else {
            _tmpAplicar_hh = _stmt.getText(_columnIndexOfAplicarHh);
          }
          _item.setAplicar_hh(_tmpAplicar_hh);
          final String _tmpTipo;
          if (_stmt.isNull(_columnIndexOfTipo)) {
            _tmpTipo = null;
          } else {
            _tmpTipo = _stmt.getText(_columnIndexOfTipo);
          }
          _item.setTipo(_tmpTipo);
          final String _tmpAplicar_ti;
          if (_stmt.isNull(_columnIndexOfAplicarTi)) {
            _tmpAplicar_ti = null;
          } else {
            _tmpAplicar_ti = _stmt.getText(_columnIndexOfAplicarTi);
          }
          _item.setAplicar_ti(_tmpAplicar_ti);
          final String _tmpNotas;
          if (_stmt.isNull(_columnIndexOfNotas)) {
            _tmpNotas = null;
          } else {
            _tmpNotas = _stmt.getText(_columnIndexOfNotas);
          }
          _item.setNotas(_tmpNotas);
          final String _tmpAlergenos;
          if (_stmt.isNull(_columnIndexOfAlergenos)) {
            _tmpAlergenos = null;
          } else {
            _tmpAlergenos = _stmt.getText(_columnIndexOfAlergenos);
          }
          _item.setAlergenos(_tmpAlergenos);
          final String _tmpEtiquetas;
          if (_stmt.isNull(_columnIndexOfEtiquetas)) {
            _tmpEtiquetas = null;
          } else {
            _tmpEtiquetas = _stmt.getText(_columnIndexOfEtiquetas);
          }
          _item.setEtiquetas(_tmpEtiquetas);
          final String _tmpOrden;
          if (_stmt.isNull(_columnIndexOfOrden)) {
            _tmpOrden = null;
          } else {
            _tmpOrden = _stmt.getText(_columnIndexOfOrden);
          }
          _item.setOrden(_tmpOrden);
          final String _tmpOrden_platos;
          if (_stmt.isNull(_columnIndexOfOrdenPlatos)) {
            _tmpOrden_platos = null;
          } else {
            _tmpOrden_platos = _stmt.getText(_columnIndexOfOrdenPlatos);
          }
          _item.setOrden_platos(_tmpOrden_platos);
          final String _tmpCodfam;
          if (_stmt.isNull(_columnIndexOfCodfam)) {
            _tmpCodfam = null;
          } else {
            _tmpCodfam = _stmt.getText(_columnIndexOfCodfam);
          }
          _item.setCodfam(_tmpCodfam);
          final String _tmpCodsub;
          if (_stmt.isNull(_columnIndexOfCodsub)) {
            _tmpCodsub = null;
          } else {
            _tmpCodsub = _stmt.getText(_columnIndexOfCodsub);
          }
          _item.setCodsub(_tmpCodsub);
          final String _tmpEs_extra;
          if (_stmt.isNull(_columnIndexOfEsExtra)) {
            _tmpEs_extra = null;
          } else {
            _tmpEs_extra = _stmt.getText(_columnIndexOfEsExtra);
          }
          _item.setEs_extra(_tmpEs_extra);
          final String _tmpVer_extra;
          if (_stmt.isNull(_columnIndexOfVerExtra)) {
            _tmpVer_extra = null;
          } else {
            _tmpVer_extra = _stmt.getText(_columnIndexOfVerExtra);
          }
          _item.setVer_extra(_tmpVer_extra);
          final String _tmpPensiones;
          if (_stmt.isNull(_columnIndexOfPensiones)) {
            _tmpPensiones = null;
          } else {
            _tmpPensiones = _stmt.getText(_columnIndexOfPensiones);
          }
          _item.setPensiones(_tmpPensiones);
          final String _tmpMd5;
          if (_stmt.isNull(_columnIndexOfMd5)) {
            _tmpMd5 = null;
          } else {
            _tmpMd5 = _stmt.getText(_columnIndexOfMd5);
          }
          _item.setMd5(_tmpMd5);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<roomRegistrosCRC> recuperaMD5(final int min, final int max) {
    final String _sql = "select rowid as idfila,ifnull(md5,'') as crc  from productos where rowid between ? and ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, min);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, max);
        final int _columnIndexOfIdfila = 0;
        final int _columnIndexOfCrc = 1;
        final List<roomRegistrosCRC> _result = new ArrayList<roomRegistrosCRC>();
        while (_stmt.step()) {
          final roomRegistrosCRC _item;
          _item = new roomRegistrosCRC();
          final int _tmpIdfila;
          _tmpIdfila = (int) (_stmt.getLong(_columnIndexOfIdfila));
          _item.setIdfila(_tmpIdfila);
          final String _tmpCrc;
          if (_stmt.isNull(_columnIndexOfCrc)) {
            _tmpCrc = null;
          } else {
            _tmpCrc = _stmt.getText(_columnIndexOfCrc);
          }
          _item.setCrc(_tmpCrc);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Integer recuperaProductosCabeceraEtiquetaCount(final int tmenu, final int cabecera,
      final int etiqueta) {
    final String _sql = "select count(*) from Productos where  tmenu = ? and cabecera = ? and etiquetas like '%|' || ? || '|%'";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, tmenu);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, cabecera);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, etiqueta);
        final Integer _result;
        if (_stmt.step()) {
          final Integer _tmp;
          if (_stmt.isNull(0)) {
            _tmp = null;
          } else {
            _tmp = (int) (_stmt.getLong(0));
          }
          _result = _tmp;
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Integer recuperaProductosEtiquetaCount(final int tmenu, final int etiqueta) {
    final String _sql = "select count(*) from Productos where  tmenu = ? and etiquetas like '%|' || ? || '|%'";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, tmenu);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, etiqueta);
        final Integer _result;
        if (_stmt.step()) {
          final Integer _tmp;
          if (_stmt.isNull(0)) {
            _tmp = null;
          } else {
            _tmp = (int) (_stmt.getLong(0));
          }
          _result = _tmp;
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<Productos> recuperaProductosCabecera(final int tmenu, final int cabecera) {
    final String _sql = "select * from Productos where tmenu = ? and cabecera = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, tmenu);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, cabecera);
        final int _columnIndexOfRowid = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "rowid");
        final int _columnIndexOfCodmenu = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codmenu");
        final int _columnIndexOfTmenu = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tmenu");
        final int _columnIndexOfCabecera = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cabecera");
        final int _columnIndexOfDescripcion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "descripcion");
        final int _columnIndexOfEuros = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "euros");
        final int _columnIndexOfCosto = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "costo");
        final int _columnIndexOfMprecios = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "mprecios");
        final int _columnIndexOfAbrevia = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "abrevia");
        final int _columnIndexOfAplicarHh = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "aplicar_hh");
        final int _columnIndexOfTipo = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tipo");
        final int _columnIndexOfAplicarTi = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "aplicar_ti");
        final int _columnIndexOfNotas = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "notas");
        final int _columnIndexOfAlergenos = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "alergenos");
        final int _columnIndexOfEtiquetas = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "etiquetas");
        final int _columnIndexOfOrden = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "orden");
        final int _columnIndexOfOrdenPlatos = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "orden_platos");
        final int _columnIndexOfCodfam = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codfam");
        final int _columnIndexOfCodsub = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codsub");
        final int _columnIndexOfEsExtra = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "es_extra");
        final int _columnIndexOfVerExtra = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "ver_extra");
        final int _columnIndexOfPensiones = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "pensiones");
        final int _columnIndexOfMd5 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "md5");
        final List<Productos> _result = new ArrayList<Productos>();
        while (_stmt.step()) {
          final Productos _item;
          _item = new Productos();
          final Integer _tmpRowid;
          if (_stmt.isNull(_columnIndexOfRowid)) {
            _tmpRowid = null;
          } else {
            _tmpRowid = (int) (_stmt.getLong(_columnIndexOfRowid));
          }
          _item.setRowid(_tmpRowid);
          final String _tmpCodmenu;
          if (_stmt.isNull(_columnIndexOfCodmenu)) {
            _tmpCodmenu = null;
          } else {
            _tmpCodmenu = _stmt.getText(_columnIndexOfCodmenu);
          }
          _item.setCodmenu(_tmpCodmenu);
          final int _tmpTmenu;
          _tmpTmenu = (int) (_stmt.getLong(_columnIndexOfTmenu));
          _item.setTmenu(_tmpTmenu);
          final int _tmpCabecera;
          _tmpCabecera = (int) (_stmt.getLong(_columnIndexOfCabecera));
          _item.setCabecera(_tmpCabecera);
          final String _tmpDescripcion;
          if (_stmt.isNull(_columnIndexOfDescripcion)) {
            _tmpDescripcion = null;
          } else {
            _tmpDescripcion = _stmt.getText(_columnIndexOfDescripcion);
          }
          _item.setDescripcion(_tmpDescripcion);
          final String _tmpEuros;
          if (_stmt.isNull(_columnIndexOfEuros)) {
            _tmpEuros = null;
          } else {
            _tmpEuros = _stmt.getText(_columnIndexOfEuros);
          }
          _item.setEuros(_tmpEuros);
          final String _tmpCosto;
          if (_stmt.isNull(_columnIndexOfCosto)) {
            _tmpCosto = null;
          } else {
            _tmpCosto = _stmt.getText(_columnIndexOfCosto);
          }
          _item.setCosto(_tmpCosto);
          final String _tmpMprecios;
          if (_stmt.isNull(_columnIndexOfMprecios)) {
            _tmpMprecios = null;
          } else {
            _tmpMprecios = _stmt.getText(_columnIndexOfMprecios);
          }
          _item.setMprecios(_tmpMprecios);
          final String _tmpAbrevia;
          if (_stmt.isNull(_columnIndexOfAbrevia)) {
            _tmpAbrevia = null;
          } else {
            _tmpAbrevia = _stmt.getText(_columnIndexOfAbrevia);
          }
          _item.setAbrevia(_tmpAbrevia);
          final String _tmpAplicar_hh;
          if (_stmt.isNull(_columnIndexOfAplicarHh)) {
            _tmpAplicar_hh = null;
          } else {
            _tmpAplicar_hh = _stmt.getText(_columnIndexOfAplicarHh);
          }
          _item.setAplicar_hh(_tmpAplicar_hh);
          final String _tmpTipo;
          if (_stmt.isNull(_columnIndexOfTipo)) {
            _tmpTipo = null;
          } else {
            _tmpTipo = _stmt.getText(_columnIndexOfTipo);
          }
          _item.setTipo(_tmpTipo);
          final String _tmpAplicar_ti;
          if (_stmt.isNull(_columnIndexOfAplicarTi)) {
            _tmpAplicar_ti = null;
          } else {
            _tmpAplicar_ti = _stmt.getText(_columnIndexOfAplicarTi);
          }
          _item.setAplicar_ti(_tmpAplicar_ti);
          final String _tmpNotas;
          if (_stmt.isNull(_columnIndexOfNotas)) {
            _tmpNotas = null;
          } else {
            _tmpNotas = _stmt.getText(_columnIndexOfNotas);
          }
          _item.setNotas(_tmpNotas);
          final String _tmpAlergenos;
          if (_stmt.isNull(_columnIndexOfAlergenos)) {
            _tmpAlergenos = null;
          } else {
            _tmpAlergenos = _stmt.getText(_columnIndexOfAlergenos);
          }
          _item.setAlergenos(_tmpAlergenos);
          final String _tmpEtiquetas;
          if (_stmt.isNull(_columnIndexOfEtiquetas)) {
            _tmpEtiquetas = null;
          } else {
            _tmpEtiquetas = _stmt.getText(_columnIndexOfEtiquetas);
          }
          _item.setEtiquetas(_tmpEtiquetas);
          final String _tmpOrden;
          if (_stmt.isNull(_columnIndexOfOrden)) {
            _tmpOrden = null;
          } else {
            _tmpOrden = _stmt.getText(_columnIndexOfOrden);
          }
          _item.setOrden(_tmpOrden);
          final String _tmpOrden_platos;
          if (_stmt.isNull(_columnIndexOfOrdenPlatos)) {
            _tmpOrden_platos = null;
          } else {
            _tmpOrden_platos = _stmt.getText(_columnIndexOfOrdenPlatos);
          }
          _item.setOrden_platos(_tmpOrden_platos);
          final String _tmpCodfam;
          if (_stmt.isNull(_columnIndexOfCodfam)) {
            _tmpCodfam = null;
          } else {
            _tmpCodfam = _stmt.getText(_columnIndexOfCodfam);
          }
          _item.setCodfam(_tmpCodfam);
          final String _tmpCodsub;
          if (_stmt.isNull(_columnIndexOfCodsub)) {
            _tmpCodsub = null;
          } else {
            _tmpCodsub = _stmt.getText(_columnIndexOfCodsub);
          }
          _item.setCodsub(_tmpCodsub);
          final String _tmpEs_extra;
          if (_stmt.isNull(_columnIndexOfEsExtra)) {
            _tmpEs_extra = null;
          } else {
            _tmpEs_extra = _stmt.getText(_columnIndexOfEsExtra);
          }
          _item.setEs_extra(_tmpEs_extra);
          final String _tmpVer_extra;
          if (_stmt.isNull(_columnIndexOfVerExtra)) {
            _tmpVer_extra = null;
          } else {
            _tmpVer_extra = _stmt.getText(_columnIndexOfVerExtra);
          }
          _item.setVer_extra(_tmpVer_extra);
          final String _tmpPensiones;
          if (_stmt.isNull(_columnIndexOfPensiones)) {
            _tmpPensiones = null;
          } else {
            _tmpPensiones = _stmt.getText(_columnIndexOfPensiones);
          }
          _item.setPensiones(_tmpPensiones);
          final String _tmpMd5;
          if (_stmt.isNull(_columnIndexOfMd5)) {
            _tmpMd5 = null;
          } else {
            _tmpMd5 = _stmt.getText(_columnIndexOfMd5);
          }
          _item.setMd5(_tmpMd5);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<Productos> recuperaTodosProductosCabecera(final int tmenu) {
    final String _sql = "select * from Productos where tmenu = ? ";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, tmenu);
        final int _columnIndexOfRowid = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "rowid");
        final int _columnIndexOfCodmenu = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codmenu");
        final int _columnIndexOfTmenu = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tmenu");
        final int _columnIndexOfCabecera = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cabecera");
        final int _columnIndexOfDescripcion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "descripcion");
        final int _columnIndexOfEuros = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "euros");
        final int _columnIndexOfCosto = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "costo");
        final int _columnIndexOfMprecios = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "mprecios");
        final int _columnIndexOfAbrevia = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "abrevia");
        final int _columnIndexOfAplicarHh = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "aplicar_hh");
        final int _columnIndexOfTipo = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tipo");
        final int _columnIndexOfAplicarTi = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "aplicar_ti");
        final int _columnIndexOfNotas = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "notas");
        final int _columnIndexOfAlergenos = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "alergenos");
        final int _columnIndexOfEtiquetas = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "etiquetas");
        final int _columnIndexOfOrden = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "orden");
        final int _columnIndexOfOrdenPlatos = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "orden_platos");
        final int _columnIndexOfCodfam = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codfam");
        final int _columnIndexOfCodsub = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codsub");
        final int _columnIndexOfEsExtra = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "es_extra");
        final int _columnIndexOfVerExtra = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "ver_extra");
        final int _columnIndexOfPensiones = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "pensiones");
        final int _columnIndexOfMd5 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "md5");
        final List<Productos> _result = new ArrayList<Productos>();
        while (_stmt.step()) {
          final Productos _item;
          _item = new Productos();
          final Integer _tmpRowid;
          if (_stmt.isNull(_columnIndexOfRowid)) {
            _tmpRowid = null;
          } else {
            _tmpRowid = (int) (_stmt.getLong(_columnIndexOfRowid));
          }
          _item.setRowid(_tmpRowid);
          final String _tmpCodmenu;
          if (_stmt.isNull(_columnIndexOfCodmenu)) {
            _tmpCodmenu = null;
          } else {
            _tmpCodmenu = _stmt.getText(_columnIndexOfCodmenu);
          }
          _item.setCodmenu(_tmpCodmenu);
          final int _tmpTmenu;
          _tmpTmenu = (int) (_stmt.getLong(_columnIndexOfTmenu));
          _item.setTmenu(_tmpTmenu);
          final int _tmpCabecera;
          _tmpCabecera = (int) (_stmt.getLong(_columnIndexOfCabecera));
          _item.setCabecera(_tmpCabecera);
          final String _tmpDescripcion;
          if (_stmt.isNull(_columnIndexOfDescripcion)) {
            _tmpDescripcion = null;
          } else {
            _tmpDescripcion = _stmt.getText(_columnIndexOfDescripcion);
          }
          _item.setDescripcion(_tmpDescripcion);
          final String _tmpEuros;
          if (_stmt.isNull(_columnIndexOfEuros)) {
            _tmpEuros = null;
          } else {
            _tmpEuros = _stmt.getText(_columnIndexOfEuros);
          }
          _item.setEuros(_tmpEuros);
          final String _tmpCosto;
          if (_stmt.isNull(_columnIndexOfCosto)) {
            _tmpCosto = null;
          } else {
            _tmpCosto = _stmt.getText(_columnIndexOfCosto);
          }
          _item.setCosto(_tmpCosto);
          final String _tmpMprecios;
          if (_stmt.isNull(_columnIndexOfMprecios)) {
            _tmpMprecios = null;
          } else {
            _tmpMprecios = _stmt.getText(_columnIndexOfMprecios);
          }
          _item.setMprecios(_tmpMprecios);
          final String _tmpAbrevia;
          if (_stmt.isNull(_columnIndexOfAbrevia)) {
            _tmpAbrevia = null;
          } else {
            _tmpAbrevia = _stmt.getText(_columnIndexOfAbrevia);
          }
          _item.setAbrevia(_tmpAbrevia);
          final String _tmpAplicar_hh;
          if (_stmt.isNull(_columnIndexOfAplicarHh)) {
            _tmpAplicar_hh = null;
          } else {
            _tmpAplicar_hh = _stmt.getText(_columnIndexOfAplicarHh);
          }
          _item.setAplicar_hh(_tmpAplicar_hh);
          final String _tmpTipo;
          if (_stmt.isNull(_columnIndexOfTipo)) {
            _tmpTipo = null;
          } else {
            _tmpTipo = _stmt.getText(_columnIndexOfTipo);
          }
          _item.setTipo(_tmpTipo);
          final String _tmpAplicar_ti;
          if (_stmt.isNull(_columnIndexOfAplicarTi)) {
            _tmpAplicar_ti = null;
          } else {
            _tmpAplicar_ti = _stmt.getText(_columnIndexOfAplicarTi);
          }
          _item.setAplicar_ti(_tmpAplicar_ti);
          final String _tmpNotas;
          if (_stmt.isNull(_columnIndexOfNotas)) {
            _tmpNotas = null;
          } else {
            _tmpNotas = _stmt.getText(_columnIndexOfNotas);
          }
          _item.setNotas(_tmpNotas);
          final String _tmpAlergenos;
          if (_stmt.isNull(_columnIndexOfAlergenos)) {
            _tmpAlergenos = null;
          } else {
            _tmpAlergenos = _stmt.getText(_columnIndexOfAlergenos);
          }
          _item.setAlergenos(_tmpAlergenos);
          final String _tmpEtiquetas;
          if (_stmt.isNull(_columnIndexOfEtiquetas)) {
            _tmpEtiquetas = null;
          } else {
            _tmpEtiquetas = _stmt.getText(_columnIndexOfEtiquetas);
          }
          _item.setEtiquetas(_tmpEtiquetas);
          final String _tmpOrden;
          if (_stmt.isNull(_columnIndexOfOrden)) {
            _tmpOrden = null;
          } else {
            _tmpOrden = _stmt.getText(_columnIndexOfOrden);
          }
          _item.setOrden(_tmpOrden);
          final String _tmpOrden_platos;
          if (_stmt.isNull(_columnIndexOfOrdenPlatos)) {
            _tmpOrden_platos = null;
          } else {
            _tmpOrden_platos = _stmt.getText(_columnIndexOfOrdenPlatos);
          }
          _item.setOrden_platos(_tmpOrden_platos);
          final String _tmpCodfam;
          if (_stmt.isNull(_columnIndexOfCodfam)) {
            _tmpCodfam = null;
          } else {
            _tmpCodfam = _stmt.getText(_columnIndexOfCodfam);
          }
          _item.setCodfam(_tmpCodfam);
          final String _tmpCodsub;
          if (_stmt.isNull(_columnIndexOfCodsub)) {
            _tmpCodsub = null;
          } else {
            _tmpCodsub = _stmt.getText(_columnIndexOfCodsub);
          }
          _item.setCodsub(_tmpCodsub);
          final String _tmpEs_extra;
          if (_stmt.isNull(_columnIndexOfEsExtra)) {
            _tmpEs_extra = null;
          } else {
            _tmpEs_extra = _stmt.getText(_columnIndexOfEsExtra);
          }
          _item.setEs_extra(_tmpEs_extra);
          final String _tmpVer_extra;
          if (_stmt.isNull(_columnIndexOfVerExtra)) {
            _tmpVer_extra = null;
          } else {
            _tmpVer_extra = _stmt.getText(_columnIndexOfVerExtra);
          }
          _item.setVer_extra(_tmpVer_extra);
          final String _tmpPensiones;
          if (_stmt.isNull(_columnIndexOfPensiones)) {
            _tmpPensiones = null;
          } else {
            _tmpPensiones = _stmt.getText(_columnIndexOfPensiones);
          }
          _item.setPensiones(_tmpPensiones);
          final String _tmpMd5;
          if (_stmt.isNull(_columnIndexOfMd5)) {
            _tmpMd5 = null;
          } else {
            _tmpMd5 = _stmt.getText(_columnIndexOfMd5);
          }
          _item.setMd5(_tmpMd5);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Productos recuperaProducto(final String codmenu, final int tmenu) {
    final String _sql = "select * from Productos where codmenu = ? and tmenu = ? limit 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (codmenu == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, codmenu);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, tmenu);
        final int _columnIndexOfRowid = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "rowid");
        final int _columnIndexOfCodmenu = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codmenu");
        final int _columnIndexOfTmenu = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tmenu");
        final int _columnIndexOfCabecera = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cabecera");
        final int _columnIndexOfDescripcion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "descripcion");
        final int _columnIndexOfEuros = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "euros");
        final int _columnIndexOfCosto = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "costo");
        final int _columnIndexOfMprecios = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "mprecios");
        final int _columnIndexOfAbrevia = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "abrevia");
        final int _columnIndexOfAplicarHh = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "aplicar_hh");
        final int _columnIndexOfTipo = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tipo");
        final int _columnIndexOfAplicarTi = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "aplicar_ti");
        final int _columnIndexOfNotas = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "notas");
        final int _columnIndexOfAlergenos = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "alergenos");
        final int _columnIndexOfEtiquetas = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "etiquetas");
        final int _columnIndexOfOrden = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "orden");
        final int _columnIndexOfOrdenPlatos = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "orden_platos");
        final int _columnIndexOfCodfam = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codfam");
        final int _columnIndexOfCodsub = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codsub");
        final int _columnIndexOfEsExtra = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "es_extra");
        final int _columnIndexOfVerExtra = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "ver_extra");
        final int _columnIndexOfPensiones = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "pensiones");
        final int _columnIndexOfMd5 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "md5");
        final Productos _result;
        if (_stmt.step()) {
          _result = new Productos();
          final Integer _tmpRowid;
          if (_stmt.isNull(_columnIndexOfRowid)) {
            _tmpRowid = null;
          } else {
            _tmpRowid = (int) (_stmt.getLong(_columnIndexOfRowid));
          }
          _result.setRowid(_tmpRowid);
          final String _tmpCodmenu;
          if (_stmt.isNull(_columnIndexOfCodmenu)) {
            _tmpCodmenu = null;
          } else {
            _tmpCodmenu = _stmt.getText(_columnIndexOfCodmenu);
          }
          _result.setCodmenu(_tmpCodmenu);
          final int _tmpTmenu;
          _tmpTmenu = (int) (_stmt.getLong(_columnIndexOfTmenu));
          _result.setTmenu(_tmpTmenu);
          final int _tmpCabecera;
          _tmpCabecera = (int) (_stmt.getLong(_columnIndexOfCabecera));
          _result.setCabecera(_tmpCabecera);
          final String _tmpDescripcion;
          if (_stmt.isNull(_columnIndexOfDescripcion)) {
            _tmpDescripcion = null;
          } else {
            _tmpDescripcion = _stmt.getText(_columnIndexOfDescripcion);
          }
          _result.setDescripcion(_tmpDescripcion);
          final String _tmpEuros;
          if (_stmt.isNull(_columnIndexOfEuros)) {
            _tmpEuros = null;
          } else {
            _tmpEuros = _stmt.getText(_columnIndexOfEuros);
          }
          _result.setEuros(_tmpEuros);
          final String _tmpCosto;
          if (_stmt.isNull(_columnIndexOfCosto)) {
            _tmpCosto = null;
          } else {
            _tmpCosto = _stmt.getText(_columnIndexOfCosto);
          }
          _result.setCosto(_tmpCosto);
          final String _tmpMprecios;
          if (_stmt.isNull(_columnIndexOfMprecios)) {
            _tmpMprecios = null;
          } else {
            _tmpMprecios = _stmt.getText(_columnIndexOfMprecios);
          }
          _result.setMprecios(_tmpMprecios);
          final String _tmpAbrevia;
          if (_stmt.isNull(_columnIndexOfAbrevia)) {
            _tmpAbrevia = null;
          } else {
            _tmpAbrevia = _stmt.getText(_columnIndexOfAbrevia);
          }
          _result.setAbrevia(_tmpAbrevia);
          final String _tmpAplicar_hh;
          if (_stmt.isNull(_columnIndexOfAplicarHh)) {
            _tmpAplicar_hh = null;
          } else {
            _tmpAplicar_hh = _stmt.getText(_columnIndexOfAplicarHh);
          }
          _result.setAplicar_hh(_tmpAplicar_hh);
          final String _tmpTipo;
          if (_stmt.isNull(_columnIndexOfTipo)) {
            _tmpTipo = null;
          } else {
            _tmpTipo = _stmt.getText(_columnIndexOfTipo);
          }
          _result.setTipo(_tmpTipo);
          final String _tmpAplicar_ti;
          if (_stmt.isNull(_columnIndexOfAplicarTi)) {
            _tmpAplicar_ti = null;
          } else {
            _tmpAplicar_ti = _stmt.getText(_columnIndexOfAplicarTi);
          }
          _result.setAplicar_ti(_tmpAplicar_ti);
          final String _tmpNotas;
          if (_stmt.isNull(_columnIndexOfNotas)) {
            _tmpNotas = null;
          } else {
            _tmpNotas = _stmt.getText(_columnIndexOfNotas);
          }
          _result.setNotas(_tmpNotas);
          final String _tmpAlergenos;
          if (_stmt.isNull(_columnIndexOfAlergenos)) {
            _tmpAlergenos = null;
          } else {
            _tmpAlergenos = _stmt.getText(_columnIndexOfAlergenos);
          }
          _result.setAlergenos(_tmpAlergenos);
          final String _tmpEtiquetas;
          if (_stmt.isNull(_columnIndexOfEtiquetas)) {
            _tmpEtiquetas = null;
          } else {
            _tmpEtiquetas = _stmt.getText(_columnIndexOfEtiquetas);
          }
          _result.setEtiquetas(_tmpEtiquetas);
          final String _tmpOrden;
          if (_stmt.isNull(_columnIndexOfOrden)) {
            _tmpOrden = null;
          } else {
            _tmpOrden = _stmt.getText(_columnIndexOfOrden);
          }
          _result.setOrden(_tmpOrden);
          final String _tmpOrden_platos;
          if (_stmt.isNull(_columnIndexOfOrdenPlatos)) {
            _tmpOrden_platos = null;
          } else {
            _tmpOrden_platos = _stmt.getText(_columnIndexOfOrdenPlatos);
          }
          _result.setOrden_platos(_tmpOrden_platos);
          final String _tmpCodfam;
          if (_stmt.isNull(_columnIndexOfCodfam)) {
            _tmpCodfam = null;
          } else {
            _tmpCodfam = _stmt.getText(_columnIndexOfCodfam);
          }
          _result.setCodfam(_tmpCodfam);
          final String _tmpCodsub;
          if (_stmt.isNull(_columnIndexOfCodsub)) {
            _tmpCodsub = null;
          } else {
            _tmpCodsub = _stmt.getText(_columnIndexOfCodsub);
          }
          _result.setCodsub(_tmpCodsub);
          final String _tmpEs_extra;
          if (_stmt.isNull(_columnIndexOfEsExtra)) {
            _tmpEs_extra = null;
          } else {
            _tmpEs_extra = _stmt.getText(_columnIndexOfEsExtra);
          }
          _result.setEs_extra(_tmpEs_extra);
          final String _tmpVer_extra;
          if (_stmt.isNull(_columnIndexOfVerExtra)) {
            _tmpVer_extra = null;
          } else {
            _tmpVer_extra = _stmt.getText(_columnIndexOfVerExtra);
          }
          _result.setVer_extra(_tmpVer_extra);
          final String _tmpPensiones;
          if (_stmt.isNull(_columnIndexOfPensiones)) {
            _tmpPensiones = null;
          } else {
            _tmpPensiones = _stmt.getText(_columnIndexOfPensiones);
          }
          _result.setPensiones(_tmpPensiones);
          final String _tmpMd5;
          if (_stmt.isNull(_columnIndexOfMd5)) {
            _tmpMd5 = null;
          } else {
            _tmpMd5 = _stmt.getText(_columnIndexOfMd5);
          }
          _result.setMd5(_tmpMd5);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void borraRegistro(final int idfila) {
    final String _sql = "delete from  productos where rowid = ?";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, idfila);
        _stmt.step();
        return null;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void borraTodo() {
    final String _sql = "delete from  productos";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        _stmt.step();
        return null;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void borraRango(final int min, final int max) {
    final String _sql = "delete from  productos where rowid between ? and ?";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, min);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, max);
        _stmt.step();
        return null;
      } finally {
        _stmt.close();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
