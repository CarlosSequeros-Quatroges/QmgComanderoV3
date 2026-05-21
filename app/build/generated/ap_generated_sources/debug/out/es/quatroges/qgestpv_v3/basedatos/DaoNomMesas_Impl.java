package es.quatroges.qgestpv_v3.basedatos;

import androidx.annotation.NonNull;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import es.quatroges.qgestpv_v3.datos.Nom_Mesas;
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
public final class DaoNomMesas_Impl implements DaoNomMesas {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Nom_Mesas> __insertAdapterOfNom_Mesas;

  private final EntityDeleteOrUpdateAdapter<Nom_Mesas> __updateAdapterOfNom_Mesas;

  public DaoNomMesas_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfNom_Mesas = new EntityInsertAdapter<Nom_Mesas>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `nom_mesas` (`rowid`,`codtpv`,`numero`,`descripcion`,`grupo`,`md5`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Nom_Mesas entity) {
        if (entity.getRowid() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getRowid());
        }
        if (entity.getCodtpv() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getCodtpv());
        }
        statement.bindLong(3, entity.getNumero());
        if (entity.getDescripcion() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getDescripcion());
        }
        statement.bindLong(5, entity.getGrupo());
        if (entity.getMd5() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getMd5());
        }
      }
    };
    this.__updateAdapterOfNom_Mesas = new EntityDeleteOrUpdateAdapter<Nom_Mesas>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR IGNORE `nom_mesas` SET `rowid` = ?,`codtpv` = ?,`numero` = ?,`descripcion` = ?,`grupo` = ?,`md5` = ? WHERE `rowid` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Nom_Mesas entity) {
        if (entity.getRowid() == null) {
          statement.bindNull(1);
        } else {
          statement.bindLong(1, entity.getRowid());
        }
        if (entity.getCodtpv() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getCodtpv());
        }
        statement.bindLong(3, entity.getNumero());
        if (entity.getDescripcion() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getDescripcion());
        }
        statement.bindLong(5, entity.getGrupo());
        if (entity.getMd5() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getMd5());
        }
        if (entity.getRowid() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getRowid());
        }
      }
    };
  }

  @Override
  public long insertRegistro(final Nom_Mesas nomMesa) {
    return DBUtil.performBlocking(__db, false, true, (_connection) -> {
      return __insertAdapterOfNom_Mesas.insertAndReturnId(_connection, nomMesa);
    });
  }

  @Override
  public void updateRegistro(final Nom_Mesas nomMesa) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfNom_Mesas.handle(_connection, nomMesa);
      return null;
    });
  }

  @Override
  public Integer recuperaCount() {
    final String _sql = "select count(*) from Nom_Mesas";
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
    final String _sql = "select min(rowid) from Nom_Mesas";
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
    final String _sql = "select max(rowid) from Nom_Mesas";
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
  public List<Nom_Mesas> recuperaTodo() {
    final String _sql = "select * from Nom_Mesas";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfRowid = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "rowid");
        final int _columnIndexOfCodtpv = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codtpv");
        final int _columnIndexOfNumero = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "numero");
        final int _columnIndexOfDescripcion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "descripcion");
        final int _columnIndexOfGrupo = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "grupo");
        final int _columnIndexOfMd5 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "md5");
        final List<Nom_Mesas> _result = new ArrayList<Nom_Mesas>();
        while (_stmt.step()) {
          final Nom_Mesas _item;
          _item = new Nom_Mesas();
          final Integer _tmpRowid;
          if (_stmt.isNull(_columnIndexOfRowid)) {
            _tmpRowid = null;
          } else {
            _tmpRowid = (int) (_stmt.getLong(_columnIndexOfRowid));
          }
          _item.setRowid(_tmpRowid);
          final String _tmpCodtpv;
          if (_stmt.isNull(_columnIndexOfCodtpv)) {
            _tmpCodtpv = null;
          } else {
            _tmpCodtpv = _stmt.getText(_columnIndexOfCodtpv);
          }
          _item.setCodtpv(_tmpCodtpv);
          final int _tmpNumero;
          _tmpNumero = (int) (_stmt.getLong(_columnIndexOfNumero));
          _item.setNumero(_tmpNumero);
          final String _tmpDescripcion;
          if (_stmt.isNull(_columnIndexOfDescripcion)) {
            _tmpDescripcion = null;
          } else {
            _tmpDescripcion = _stmt.getText(_columnIndexOfDescripcion);
          }
          _item.setDescripcion(_tmpDescripcion);
          final int _tmpGrupo;
          _tmpGrupo = (int) (_stmt.getLong(_columnIndexOfGrupo));
          _item.setGrupo(_tmpGrupo);
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
    final String _sql = "select rowid as idfila,ifnull(md5,'') as crc  from Nom_Mesas where rowid between ? and ?";
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
    final String _sql = "delete from  Nom_Mesas where rowid = ?";
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
    final String _sql = "delete from  Nom_Mesas";
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
    final String _sql = "delete from  Nom_Mesas where rowid between ? and ?";
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
