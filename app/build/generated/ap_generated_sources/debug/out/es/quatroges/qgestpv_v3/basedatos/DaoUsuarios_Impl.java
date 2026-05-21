package es.quatroges.qgestpv_v3.basedatos;

import androidx.annotation.NonNull;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import es.quatroges.qgestpv_v3.datos.Usuarios;
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
public final class DaoUsuarios_Impl implements DaoUsuarios {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Usuarios> __insertAdapterOfUsuarios;

  private final EntityDeleteOrUpdateAdapter<Usuarios> __updateAdapterOfUsuarios;

  public DaoUsuarios_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfUsuarios = new EntityInsertAdapter<Usuarios>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `usuarios` (`codigo`,`nombre`,`clave`,`ntarjeta`,`md5`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Usuarios entity) {
        statement.bindLong(1, entity.getCodigo());
        if (entity.getNombre() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getNombre());
        }
        if (entity.getClave() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getClave());
        }
        if (entity.getNtarjeta() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getNtarjeta());
        }
        if (entity.getMd5() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getMd5());
        }
      }
    };
    this.__updateAdapterOfUsuarios = new EntityDeleteOrUpdateAdapter<Usuarios>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR IGNORE `usuarios` SET `codigo` = ?,`nombre` = ?,`clave` = ?,`ntarjeta` = ?,`md5` = ? WHERE `codigo` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Usuarios entity) {
        statement.bindLong(1, entity.getCodigo());
        if (entity.getNombre() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getNombre());
        }
        if (entity.getClave() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getClave());
        }
        if (entity.getNtarjeta() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getNtarjeta());
        }
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
  public long insertRegistro(final Usuarios usuario) {
    return DBUtil.performBlocking(__db, false, true, (_connection) -> {
      return __insertAdapterOfUsuarios.insertAndReturnId(_connection, usuario);
    });
  }

  @Override
  public void updateRegistro(final Usuarios usuario) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfUsuarios.handle(_connection, usuario);
      return null;
    });
  }

  @Override
  public Integer recuperaCount() {
    final String _sql = "select count(*) from usuarios";
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
    final String _sql = "select min(codigo) from usuarios";
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
    final String _sql = "select max(codigo) from usuarios";
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
  public List<Usuarios> recuperaTodo() {
    final String _sql = "select * from usuarios";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfCodigo = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codigo");
        final int _columnIndexOfNombre = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "nombre");
        final int _columnIndexOfClave = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "clave");
        final int _columnIndexOfNtarjeta = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "ntarjeta");
        final int _columnIndexOfMd5 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "md5");
        final List<Usuarios> _result = new ArrayList<Usuarios>();
        while (_stmt.step()) {
          final Usuarios _item;
          _item = new Usuarios();
          final int _tmpCodigo;
          _tmpCodigo = (int) (_stmt.getLong(_columnIndexOfCodigo));
          _item.setCodigo(_tmpCodigo);
          final String _tmpNombre;
          if (_stmt.isNull(_columnIndexOfNombre)) {
            _tmpNombre = null;
          } else {
            _tmpNombre = _stmt.getText(_columnIndexOfNombre);
          }
          _item.setNombre(_tmpNombre);
          final String _tmpClave;
          if (_stmt.isNull(_columnIndexOfClave)) {
            _tmpClave = null;
          } else {
            _tmpClave = _stmt.getText(_columnIndexOfClave);
          }
          _item.setClave(_tmpClave);
          final String _tmpNtarjeta;
          if (_stmt.isNull(_columnIndexOfNtarjeta)) {
            _tmpNtarjeta = null;
          } else {
            _tmpNtarjeta = _stmt.getText(_columnIndexOfNtarjeta);
          }
          _item.setNtarjeta(_tmpNtarjeta);
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
    final String _sql = "select codigo as idfila,ifnull(md5,'') as crc from usuarios where codigo between ? and ?";
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
  public void borraRegistro(final Integer idfila) {
    final String _sql = "delete from  usuarios where codigo = ?";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (idfila == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, idfila);
        }
        _stmt.step();
        return null;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void borraTodo() {
    final String _sql = "delete from  usuarios";
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
    final String _sql = "delete from  usuarios where codigo between ? and  ?";
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
