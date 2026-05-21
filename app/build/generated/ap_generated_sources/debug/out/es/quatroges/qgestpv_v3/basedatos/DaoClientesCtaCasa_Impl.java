package es.quatroges.qgestpv_v3.basedatos;

import androidx.annotation.NonNull;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import es.quatroges.qgestpv_v3.datos.ClientesCtaCasa;
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
public final class DaoClientesCtaCasa_Impl implements DaoClientesCtaCasa {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<ClientesCtaCasa> __insertAdapterOfClientesCtaCasa;

  private final EntityDeleteOrUpdateAdapter<ClientesCtaCasa> __updateAdapterOfClientesCtaCasa;

  public DaoClientesCtaCasa_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfClientesCtaCasa = new EntityInsertAdapter<ClientesCtaCasa>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `clt_art` (`rowid`,`codcli`,`nombre`,`md5`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final ClientesCtaCasa entity) {
        if (entity.getRowid() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getRowid());
        }
        if (entity.getCodcli() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getCodcli());
        }
        if (entity.getNombre() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getNombre());
        }
        if (entity.getMd5() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getMd5());
        }
      }
    };
    this.__updateAdapterOfClientesCtaCasa = new EntityDeleteOrUpdateAdapter<ClientesCtaCasa>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR IGNORE `clt_art` SET `rowid` = ?,`codcli` = ?,`nombre` = ?,`md5` = ? WHERE `rowid` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final ClientesCtaCasa entity) {
        if (entity.getRowid() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getRowid());
        }
        if (entity.getCodcli() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getCodcli());
        }
        if (entity.getNombre() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getNombre());
        }
        if (entity.getMd5() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getMd5());
        }
        if (entity.getRowid() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getRowid());
        }
      }
    };
  }

  @Override
  public long insertRegistro(final ClientesCtaCasa clientesCtaCasa) {
    return DBUtil.performBlocking(__db, false, true, (_connection) -> {
      return __insertAdapterOfClientesCtaCasa.insertAndReturnId(_connection, clientesCtaCasa);
    });
  }

  @Override
  public void updateRegistro(final ClientesCtaCasa clientesCtaCasa) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfClientesCtaCasa.handle(_connection, clientesCtaCasa);
      return null;
    });
  }

  @Override
  public Integer recuperaCount() {
    final String _sql = "select count(*) from clt_art";
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
    final String _sql = "select min(codcli) from clt_art";
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
    final String _sql = "select max(codcli) from clt_art";
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
  public List<ClientesCtaCasa> recuperaTodo() {
    final String _sql = "select * from clt_art";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfRowid = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "rowid");
        final int _columnIndexOfCodcli = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codcli");
        final int _columnIndexOfNombre = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "nombre");
        final int _columnIndexOfMd5 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "md5");
        final List<ClientesCtaCasa> _result = new ArrayList<ClientesCtaCasa>();
        while (_stmt.step()) {
          final ClientesCtaCasa _item;
          _item = new ClientesCtaCasa();
          final Integer _tmpRowid;
          if (_stmt.isNull(_columnIndexOfRowid)) {
            _tmpRowid = null;
          } else {
            _tmpRowid = (int) (_stmt.getLong(_columnIndexOfRowid));
          }
          _item.setRowid(_tmpRowid);
          final String _tmpCodcli;
          if (_stmt.isNull(_columnIndexOfCodcli)) {
            _tmpCodcli = null;
          } else {
            _tmpCodcli = _stmt.getText(_columnIndexOfCodcli);
          }
          _item.setCodcli(_tmpCodcli);
          final String _tmpNombre;
          if (_stmt.isNull(_columnIndexOfNombre)) {
            _tmpNombre = null;
          } else {
            _tmpNombre = _stmt.getText(_columnIndexOfNombre);
          }
          _item.setNombre(_tmpNombre);
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
    final String _sql = "select rowid as idfila,ifnull(md5,'') as crc  from clt_art where rowid between ? and ?";
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
    final String _sql = "delete from  clt_art where rowid = ?";
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
    final String _sql = "delete from  clt_art";
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
    final String _sql = "delete from  clt_art where rowid  between ? and ?";
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
