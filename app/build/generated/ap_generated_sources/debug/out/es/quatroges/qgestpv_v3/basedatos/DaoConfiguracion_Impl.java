package es.quatroges.qgestpv_v3.basedatos;

import androidx.annotation.NonNull;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import es.quatroges.qgestpv_v3.datos.Configuracion;
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
public final class DaoConfiguracion_Impl implements DaoConfiguracion {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Configuracion> __insertAdapterOfConfiguracion;

  private final EntityDeleteOrUpdateAdapter<Configuracion> __updateAdapterOfConfiguracion;

  public DaoConfiguracion_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfConfiguracion = new EntityInsertAdapter<Configuracion>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `configuracion` (`parametro`,`valor`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Configuracion entity) {
        if (entity.getParametro() == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.getParametro());
        }
        if (entity.getValor() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getValor());
        }
      }
    };
    this.__updateAdapterOfConfiguracion = new EntityDeleteOrUpdateAdapter<Configuracion>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR IGNORE `configuracion` SET `parametro` = ?,`valor` = ? WHERE `parametro` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Configuracion entity) {
        if (entity.getParametro() == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.getParametro());
        }
        if (entity.getValor() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getValor());
        }
        if (entity.getParametro() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getParametro());
        }
      }
    };
  }

  @Override
  public long insertConfiguracion(final Configuracion configuracion) {
    return DBUtil.performBlocking(__db, false, true, (_connection) -> {
      return __insertAdapterOfConfiguracion.insertAndReturnId(_connection, configuracion);
    });
  }

  @Override
  public void updateConfiguracion(final Configuracion configuracion) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfConfiguracion.handle(_connection, configuracion);
      return null;
    });
  }

  @Override
  public Integer recuperaConfiguracionCount() {
    final String _sql = "select count(*) from Configuracion";
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
  public List<Configuracion> recuperaConfiguracion() {
    final String _sql = "select * from Configuracion";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfParametro = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "parametro");
        final int _columnIndexOfValor = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "valor");
        final List<Configuracion> _result = new ArrayList<Configuracion>();
        while (_stmt.step()) {
          final Configuracion _item;
          _item = new Configuracion();
          final String _tmpParametro;
          if (_stmt.isNull(_columnIndexOfParametro)) {
            _tmpParametro = null;
          } else {
            _tmpParametro = _stmt.getText(_columnIndexOfParametro);
          }
          _item.setParametro(_tmpParametro);
          final String _tmpValor;
          if (_stmt.isNull(_columnIndexOfValor)) {
            _tmpValor = null;
          } else {
            _tmpValor = _stmt.getText(_columnIndexOfValor);
          }
          _item.setValor(_tmpValor);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void borraConfiguracion() {
    final String _sql = "delete from  configuracion";
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
