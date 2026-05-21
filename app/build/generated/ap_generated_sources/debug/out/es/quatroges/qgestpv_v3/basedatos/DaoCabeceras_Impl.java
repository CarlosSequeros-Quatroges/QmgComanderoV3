package es.quatroges.qgestpv_v3.basedatos;

import androidx.annotation.NonNull;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import es.quatroges.qgestpv_v3.datos.Cabeceras;
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
public final class DaoCabeceras_Impl implements DaoCabeceras {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Cabeceras> __insertAdapterOfCabeceras;

  private final EntityDeleteOrUpdateAdapter<Cabeceras> __updateAdapterOfCabeceras;

  public DaoCabeceras_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfCabeceras = new EntityInsertAdapter<Cabeceras>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `cabeceras` (`codigo`,`tmenu`,`descripcion`,`pos`,`md5`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Cabeceras entity) {
        statement.bindLong(1, entity.getCodigo());
        statement.bindLong(2, entity.getTmenu());
        if (entity.getDescripcion() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getDescripcion());
        }
        statement.bindLong(4, entity.getPos());
        if (entity.getMd5() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getMd5());
        }
      }
    };
    this.__updateAdapterOfCabeceras = new EntityDeleteOrUpdateAdapter<Cabeceras>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR IGNORE `cabeceras` SET `codigo` = ?,`tmenu` = ?,`descripcion` = ?,`pos` = ?,`md5` = ? WHERE `codigo` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Cabeceras entity) {
        statement.bindLong(1, entity.getCodigo());
        statement.bindLong(2, entity.getTmenu());
        if (entity.getDescripcion() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getDescripcion());
        }
        statement.bindLong(4, entity.getPos());
        if (entity.getMd5() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getMd5());
        }
        statement.bindLong(6, entity.getCodigo());
      }
    };
  }

  @Override
  public long insertRegistro(final Cabeceras cabecera) {
    return DBUtil.performBlocking(__db, false, true, (_connection) -> {
      return __insertAdapterOfCabeceras.insertAndReturnId(_connection, cabecera);
    });
  }

  @Override
  public void updateRegistro(final Cabeceras cabecera) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfCabeceras.handle(_connection, cabecera);
      return null;
    });
  }

  @Override
  public Integer recuperaCount() {
    final String _sql = "select count(*) from cabeceras";
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
    final String _sql = "select min(codigo) from cabeceras";
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
    final String _sql = "select max(codigo) from cabeceras";
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
  public List<Cabeceras> recuperaTodo() {
    final String _sql = "select * from cabeceras";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfCodigo = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codigo");
        final int _columnIndexOfTmenu = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tmenu");
        final int _columnIndexOfDescripcion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "descripcion");
        final int _columnIndexOfPos = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "pos");
        final int _columnIndexOfMd5 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "md5");
        final List<Cabeceras> _result = new ArrayList<Cabeceras>();
        while (_stmt.step()) {
          final Cabeceras _item;
          _item = new Cabeceras();
          final int _tmpCodigo;
          _tmpCodigo = (int) (_stmt.getLong(_columnIndexOfCodigo));
          _item.setCodigo(_tmpCodigo);
          final int _tmpTmenu;
          _tmpTmenu = (int) (_stmt.getLong(_columnIndexOfTmenu));
          _item.setTmenu(_tmpTmenu);
          final String _tmpDescripcion;
          if (_stmt.isNull(_columnIndexOfDescripcion)) {
            _tmpDescripcion = null;
          } else {
            _tmpDescripcion = _stmt.getText(_columnIndexOfDescripcion);
          }
          _item.setDescripcion(_tmpDescripcion);
          final int _tmpPos;
          _tmpPos = (int) (_stmt.getLong(_columnIndexOfPos));
          _item.setPos(_tmpPos);
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
    final String _sql = "select codigo as idfila,ifnull(md5,'')  as crc from cabeceras where codigo between ? and ?";
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
  public List<Cabeceras> recuperaCabecerasTPV(final int tmenu) {
    final String _sql = "select * from cabeceras where tmenu=?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, tmenu);
        final int _columnIndexOfCodigo = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codigo");
        final int _columnIndexOfTmenu = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tmenu");
        final int _columnIndexOfDescripcion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "descripcion");
        final int _columnIndexOfPos = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "pos");
        final int _columnIndexOfMd5 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "md5");
        final List<Cabeceras> _result = new ArrayList<Cabeceras>();
        while (_stmt.step()) {
          final Cabeceras _item;
          _item = new Cabeceras();
          final int _tmpCodigo;
          _tmpCodigo = (int) (_stmt.getLong(_columnIndexOfCodigo));
          _item.setCodigo(_tmpCodigo);
          final int _tmpTmenu;
          _tmpTmenu = (int) (_stmt.getLong(_columnIndexOfTmenu));
          _item.setTmenu(_tmpTmenu);
          final String _tmpDescripcion;
          if (_stmt.isNull(_columnIndexOfDescripcion)) {
            _tmpDescripcion = null;
          } else {
            _tmpDescripcion = _stmt.getText(_columnIndexOfDescripcion);
          }
          _item.setDescripcion(_tmpDescripcion);
          final int _tmpPos;
          _tmpPos = (int) (_stmt.getLong(_columnIndexOfPos));
          _item.setPos(_tmpPos);
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
  public void borraRegistro(final int idfila) {
    final String _sql = "delete from  cabeceras where codigo = ?";
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
    final String _sql = "delete from  cabeceras";
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
    final String _sql = "delete from  cabeceras where codigo between ? and ?";
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
