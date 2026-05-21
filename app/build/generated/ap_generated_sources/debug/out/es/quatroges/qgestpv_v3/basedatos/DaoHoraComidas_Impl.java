package es.quatroges.qgestpv_v3.basedatos;

import androidx.annotation.NonNull;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import es.quatroges.qgestpv_v3.datos.Hora_Comidas;
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
public final class DaoHoraComidas_Impl implements DaoHoraComidas {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Hora_Comidas> __insertAdapterOfHora_Comidas;

  private final EntityDeleteOrUpdateAdapter<Hora_Comidas> __updateAdapterOfHora_Comidas;

  public DaoHoraComidas_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfHora_Comidas = new EntityInsertAdapter<Hora_Comidas>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `hora_comidas` (`codigo`,`desde_hora`,`hasta_hora`,`tipo`,`codtpv`,`md5`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Hora_Comidas entity) {
        statement.bindLong(1, entity.getCodigo());
        if (entity.getDesde_hora() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getDesde_hora());
        }
        if (entity.getHasta_hora() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getHasta_hora());
        }
        if (entity.getTipo() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getTipo());
        }
        if (entity.getCodtpv() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getCodtpv());
        }
        if (entity.getMd5() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getMd5());
        }
      }
    };
    this.__updateAdapterOfHora_Comidas = new EntityDeleteOrUpdateAdapter<Hora_Comidas>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR IGNORE `hora_comidas` SET `codigo` = ?,`desde_hora` = ?,`hasta_hora` = ?,`tipo` = ?,`codtpv` = ?,`md5` = ? WHERE `codigo` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Hora_Comidas entity) {
        statement.bindLong(1, entity.getCodigo());
        if (entity.getDesde_hora() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getDesde_hora());
        }
        if (entity.getHasta_hora() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getHasta_hora());
        }
        if (entity.getTipo() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getTipo());
        }
        if (entity.getCodtpv() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getCodtpv());
        }
        if (entity.getMd5() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getMd5());
        }
        statement.bindLong(7, entity.getCodigo());
      }
    };
  }

  @Override
  public long insertRegistro(final Hora_Comidas horaComidas) {
    return DBUtil.performBlocking(__db, false, true, (_connection) -> {
      return __insertAdapterOfHora_Comidas.insertAndReturnId(_connection, horaComidas);
    });
  }

  @Override
  public void updateRegistro(final Hora_Comidas horaComidas) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfHora_Comidas.handle(_connection, horaComidas);
      return null;
    });
  }

  @Override
  public Integer recuperaCount() {
    final String _sql = "select count(*) from Hora_Comidas";
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
    final String _sql = "select min(codigo) from Hora_Comidas";
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
    final String _sql = "select max(codigo) from Hora_Comidas";
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
  public List<Hora_Comidas> recuperaTodo() {
    final String _sql = "select * from Hora_Comidas";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfCodigo = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codigo");
        final int _columnIndexOfDesdeHora = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "desde_hora");
        final int _columnIndexOfHastaHora = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "hasta_hora");
        final int _columnIndexOfTipo = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tipo");
        final int _columnIndexOfCodtpv = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codtpv");
        final int _columnIndexOfMd5 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "md5");
        final List<Hora_Comidas> _result = new ArrayList<Hora_Comidas>();
        while (_stmt.step()) {
          final Hora_Comidas _item;
          _item = new Hora_Comidas();
          final int _tmpCodigo;
          _tmpCodigo = (int) (_stmt.getLong(_columnIndexOfCodigo));
          _item.setCodigo(_tmpCodigo);
          final String _tmpDesde_hora;
          if (_stmt.isNull(_columnIndexOfDesdeHora)) {
            _tmpDesde_hora = null;
          } else {
            _tmpDesde_hora = _stmt.getText(_columnIndexOfDesdeHora);
          }
          _item.setDesde_hora(_tmpDesde_hora);
          final String _tmpHasta_hora;
          if (_stmt.isNull(_columnIndexOfHastaHora)) {
            _tmpHasta_hora = null;
          } else {
            _tmpHasta_hora = _stmt.getText(_columnIndexOfHastaHora);
          }
          _item.setHasta_hora(_tmpHasta_hora);
          final String _tmpTipo;
          if (_stmt.isNull(_columnIndexOfTipo)) {
            _tmpTipo = null;
          } else {
            _tmpTipo = _stmt.getText(_columnIndexOfTipo);
          }
          _item.setTipo(_tmpTipo);
          final String _tmpCodtpv;
          if (_stmt.isNull(_columnIndexOfCodtpv)) {
            _tmpCodtpv = null;
          } else {
            _tmpCodtpv = _stmt.getText(_columnIndexOfCodtpv);
          }
          _item.setCodtpv(_tmpCodtpv);
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
    final String _sql = "select codigo as idfila,ifnull(md5,'')  as crc from Hora_Comidas where codigo between ? and ?";
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
  public List<Hora_Comidas> recuperaHoraComidasTPV(final int codtpv) {
    final String _sql = "select * from Hora_Comidas where codtpv = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, codtpv);
        final int _columnIndexOfCodigo = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codigo");
        final int _columnIndexOfDesdeHora = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "desde_hora");
        final int _columnIndexOfHastaHora = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "hasta_hora");
        final int _columnIndexOfTipo = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tipo");
        final int _columnIndexOfCodtpv = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "codtpv");
        final int _columnIndexOfMd5 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "md5");
        final List<Hora_Comidas> _result = new ArrayList<Hora_Comidas>();
        while (_stmt.step()) {
          final Hora_Comidas _item;
          _item = new Hora_Comidas();
          final int _tmpCodigo;
          _tmpCodigo = (int) (_stmt.getLong(_columnIndexOfCodigo));
          _item.setCodigo(_tmpCodigo);
          final String _tmpDesde_hora;
          if (_stmt.isNull(_columnIndexOfDesdeHora)) {
            _tmpDesde_hora = null;
          } else {
            _tmpDesde_hora = _stmt.getText(_columnIndexOfDesdeHora);
          }
          _item.setDesde_hora(_tmpDesde_hora);
          final String _tmpHasta_hora;
          if (_stmt.isNull(_columnIndexOfHastaHora)) {
            _tmpHasta_hora = null;
          } else {
            _tmpHasta_hora = _stmt.getText(_columnIndexOfHastaHora);
          }
          _item.setHasta_hora(_tmpHasta_hora);
          final String _tmpTipo;
          if (_stmt.isNull(_columnIndexOfTipo)) {
            _tmpTipo = null;
          } else {
            _tmpTipo = _stmt.getText(_columnIndexOfTipo);
          }
          _item.setTipo(_tmpTipo);
          final String _tmpCodtpv;
          if (_stmt.isNull(_columnIndexOfCodtpv)) {
            _tmpCodtpv = null;
          } else {
            _tmpCodtpv = _stmt.getText(_columnIndexOfCodtpv);
          }
          _item.setCodtpv(_tmpCodtpv);
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
    final String _sql = "delete from  Hora_Comidas where codigo = ?";
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
    final String _sql = "delete from  Hora_Comidas";
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
    final String _sql = "delete from  Hora_Comidas where codigo between ? and ?";
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
