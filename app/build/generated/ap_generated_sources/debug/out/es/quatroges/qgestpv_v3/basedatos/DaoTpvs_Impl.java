package es.quatroges.qgestpv_v3.basedatos;

import androidx.annotation.NonNull;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import es.quatroges.qgestpv_v3.datos.Tpvs;
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
public final class DaoTpvs_Impl implements DaoTpvs {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Tpvs> __insertAdapterOfTpvs;

  private final EntityDeleteOrUpdateAdapter<Tpvs> __updateAdapterOfTpvs;

  public DaoTpvs_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfTpvs = new EntityInsertAdapter<Tpvs>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `tpvs` (`rowid`,`codtpv`,`descripcion`,`numero_mesas`,`tmenu`,`pedir_pax`,`md5`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Tpvs entity) {
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
        if (entity.getDescripcion() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getDescripcion());
        }
        statement.bindLong(4, entity.getNumeroMesas());
        statement.bindLong(5, entity.getTmenu());
        if (entity.getPedir_pax() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getPedir_pax());
        }
        if (entity.getMd5() == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.getMd5());
        }
      }
    };
    this.__updateAdapterOfTpvs = new EntityDeleteOrUpdateAdapter<Tpvs>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR IGNORE `tpvs` SET `rowid` = ?,`codtpv` = ?,`descripcion` = ?,`numero_mesas` = ?,`tmenu` = ?,`pedir_pax` = ?,`md5` = ? WHERE `rowid` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Tpvs entity) {
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
        if (entity.getDescripcion() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getDescripcion());
        }
        statement.bindLong(4, entity.getNumeroMesas());
        statement.bindLong(5, entity.getTmenu());
        if (entity.getPedir_pax() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getPedir_pax());
        }
        if (entity.getMd5() == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.getMd5());
        }
        if (entity.getRowid() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getRowid());
        }
      }
    };
  }

  @Override
  public long insertRegistro(final Tpvs tpv) {
    return DBUtil.performBlocking(__db, false, true, (_connection) -> {
      return __insertAdapterOfTpvs.insertAndReturnId(_connection, tpv);
    });
  }

  @Override
  public void updateRegistro(final Tpvs tpv) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfTpvs.handle(_connection, tpv);
      return null;
    });
  }

  @Override
  public Integer recuperaCount() {
    final String _sql = "select count(*) from tpvs";
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
    final String _sql = "select min(rowid) from tpvs";
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
    final String _sql = "select max(rowid) from tpvs";
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
  public List<Tpvs> recuperaTodo() {
    final String _sql = "select * from tpvs";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfRowid = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "rowid");
        final int _columnIndexOfCodtpv = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codtpv");
        final int _columnIndexOfDescripcion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "descripcion");
        final int _columnIndexOfNumeroMesas = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "numero_mesas");
        final int _columnIndexOfTmenu = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tmenu");
        final int _columnIndexOfPedirPax = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "pedir_pax");
        final int _columnIndexOfMd5 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "md5");
        final List<Tpvs> _result = new ArrayList<Tpvs>();
        while (_stmt.step()) {
          final Tpvs _item;
          _item = new Tpvs();
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
          final String _tmpDescripcion;
          if (_stmt.isNull(_columnIndexOfDescripcion)) {
            _tmpDescripcion = null;
          } else {
            _tmpDescripcion = _stmt.getText(_columnIndexOfDescripcion);
          }
          _item.setDescripcion(_tmpDescripcion);
          final int _tmpNumeroMesas;
          _tmpNumeroMesas = (int) (_stmt.getLong(_columnIndexOfNumeroMesas));
          _item.setNumeroMesas(_tmpNumeroMesas);
          final int _tmpTmenu;
          _tmpTmenu = (int) (_stmt.getLong(_columnIndexOfTmenu));
          _item.setTmenu(_tmpTmenu);
          final String _tmpPedir_pax;
          if (_stmt.isNull(_columnIndexOfPedirPax)) {
            _tmpPedir_pax = null;
          } else {
            _tmpPedir_pax = _stmt.getText(_columnIndexOfPedirPax);
          }
          _item.setPedir_pax(_tmpPedir_pax);
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
    final String _sql = "select rowid as idfila ,ifnull(md5,'') as crc  from tpvs where rowid between ? and ?";
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
    final String _sql = "delete from  tpvs where rowid = ?";
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
    final String _sql = "delete from  tpvs";
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
    final String _sql = "delete from  tpvs where rowid between ? and  ?";
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
