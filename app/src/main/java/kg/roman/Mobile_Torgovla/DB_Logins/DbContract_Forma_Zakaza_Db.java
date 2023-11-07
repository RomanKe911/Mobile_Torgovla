package kg.roman.Mobile_Torgovla.DB_Logins;

import android.provider.BaseColumns;

/**
 * Created by admin on 12/13/2015.
 */
public final class DbContract_Forma_Zakaza_Db {
    public DbContract_Forma_Zakaza_Db() {

    }

    public static abstract class TableUser implements BaseColumns {
        public static final String TABLE_NAME = "base_january";
        public static final String COLUMN_KOD = "kod";
        public static final String COLUMN_DATA = "data";
        public static final String COLUMN_VREMA = "vrema";
        public static final String COLUMN_KLIENT = "klient";
        public static final String COLUMN_KOD_UNIV = "kod_univ";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CENA = "cena";
        public static final String COLUMN_KOL = "kol";
        public static final String COLUMN_SUMMA = "summa";
    }


    public static final String SQL_CREATE_TABLE_USER = "CREATE TABLE " + TableUser.TABLE_NAME + "(" +
            TableUser._ID + " INTEGER PRIMARY KEY," +
            TableUser.COLUMN_KOD + " TEXT NOT NULL," +
            TableUser.COLUMN_DATA + " TEXT NOT NULL," +
            TableUser.COLUMN_VREMA + " TEXT NOT NULL," +
            TableUser.COLUMN_KLIENT + " TEXT NOT NULL," +
            TableUser.COLUMN_KOD_UNIV + " TEXT NOT NULL," +
            TableUser.COLUMN_NAME + " TEXT NOT NULL," +
            TableUser.COLUMN_CENA + " TEXT NOT NULL," +
            TableUser.COLUMN_KOL + " TEXT NOT NULL," +
            TableUser.COLUMN_SUMMA + " TEXT NOT NULL);";

    public static final String SQL_DELETE_DB = "DROP TABLE IF EXISTS " + TableUser.TABLE_NAME + ";";
}
