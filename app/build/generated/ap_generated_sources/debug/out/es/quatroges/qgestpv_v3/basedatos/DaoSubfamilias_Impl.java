package es.quatroges.qgestpv_v3.basedatos;

import androidx.annotation.NonNull;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import es.quatroges.qgestpv_v3.datos.Subfamilias;
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
public final class DaoSubfamilias_Impl implements DaoSubfamilias {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Subfamilias> __insertAdapterOfSubfamilias;

  private final EntityDeleteOrUpdateAdapter<Subfamilias> __updateAdapterOfSubfamilias;

  public DaoSubfamilias_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfSubfamilias = new EntityInsertAdapter<Subfamilias>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `subfamilias` (`codsub`,`descripcion`,`extras`,`md5`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Subfamilias entity) {
        statement.bindLong(1, entity.getCodsub());
        if (entity.getDescripcion() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getDescripcion());
        }
        if (entity.getExtras() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getExtras());
        }
        if (entity.getMd5() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getMd5());
        }
      }
    };
    this.__updateAdapterOfSubfamilias = new EntityDeleteOrUpdateAdapter<Subfamilias>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR IGNORE `subfamilias` SET `codsub` = ?,`descripcion` = ?,`extras` = ?,`md5` = ? WHERE `codsub` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Subfamilias entity) {
        statement.bindLong(1, entity.getCodsub());
        if (entity.getDescripcion() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getDescripcion());
        }
        if (entity.getExtras() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getExtras());
        }
        if (entity.getMd5() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getMd5());
        }
        statement.bindLong(5, entity.getCodsub());
      }
    };
  }

  @Override
  public long insertRegistro(final Subfamilias subfamilias) {
    return DBUtil.performBlocking(__db, false, true, (_connection) -> {
      return __insertAdapterOfSubfamilias.insertAndReturnId(_connection, subfamilias);
    });
  }

  @Override
  public void updateRegistro(final Subfamilias subfamilias) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfSubfamilias.handle(_connection, subfamilias);
      return null;
    });
  }

  @Override
  public Integer recuperaCount() {
    final String _sql = "select count(*) from subfamilias";
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
    final String _sql = "select min(codsub) from subfamilias";
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
    final String _sql = "select max(codsub) from subfamilias";
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
  public List<Subfamilias> recuperaTodo() {
    final String _sql = "select * from subfamilias";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfCodsub = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codsub");
        final int _columnIndexOfDescripcion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "descripcion");
        final int _columnIndexOfExtras = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "extras");
        final int _columnIndexOfMd5 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "md5");
        final List<Subfamilias> _result = new ArrayList<Subfamilias>();
        while (_stmt.step()) {
          final Subfamilias _item;
          _item = new Subfamilias();
          final int _tmpCodsub;
          _tmpCodsub = (int) (_stmt.getLong(_columnIndexOfCodsub));
          _item.setCodsub(_tmpCodsub);
          final String _tmpDescripcion;
          if (_stmt.isNull(_columnIndexOfDescripcion)) {
            _tmpDescripcion = null;
          } else {
            _tmpDescripcion = _stmt.getText(_columnIndexOfDescripcion);
          }
          _item.setDescripcion(_tmpDescripcion);
          final String _tmpExtras;
          if (_stmt.isNull(_columnIndexOfExtras)) {
            _tmpExtras = null;
          } else {
            _tmpExtras = _stmt.getText(_columnIndexOfExtras);
          }
          _item.setExtras(_tmpExtras);
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
    final String _sql = "select codsub as idfila,ifnull(md5,'')  as crc from subfamilias where codsub between ? and ?";
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
  public void borraRegistro(final int idfila) {
    final String _sql = "delete from  subfamilias where codsub = ?";
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
    final String _sql = "delete from  subfamilias";
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
    final String _sql = "delete from  subfamilias where codsub between ? and ?";
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
