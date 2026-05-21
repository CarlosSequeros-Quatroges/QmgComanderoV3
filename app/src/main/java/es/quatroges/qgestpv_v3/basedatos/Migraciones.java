package es.quatroges.qgestpv_v3.basedatos;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migraciones {
    public static final int DATABASE_VERSION = 7;

    public static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Crear tabla familias
            database.execSQL("CREATE TABLE IF NOT EXISTS `familias` (`codfam` INTEGER NOT NULL, `descripcion` TEXT, `md5` TEXT, PRIMARY KEY(`codfam`))");
            
            // Crear tabla subfamilias
            database.execSQL("CREATE TABLE IF NOT EXISTS `subfamilias` (`codsub` INTEGER NOT NULL, `descripcion` TEXT, `extras` TEXT, `md5` TEXT, PRIMARY KEY(`codsub`))");
            
            // Crear tabla horacomidas
            database.execSQL("CREATE TABLE IF NOT EXISTS `hora_comidas` (`codigo` INTEGER NOT NULL, `desde_hora` TEXT, `hasta_hora` TEXT, `tipo` TEXT, `codtpv` TEXT, `md5` TEXT, PRIMARY KEY(`codigo`))");
            
            // Añadir campos a la tabla productos
            database.execSQL("ALTER TABLE `productos` ADD COLUMN `codfam` TEXT");
            database.execSQL("ALTER TABLE `productos` ADD COLUMN `codsub` TEXT");
            database.execSQL("ALTER TABLE `productos` ADD COLUMN `es_extra` TEXT");
            database.execSQL("ALTER TABLE `productos` ADD COLUMN `ver_extra` TEXT");
            database.execSQL("ALTER TABLE `productos` ADD COLUMN `pensiones` TEXT");
        }
    };
}
