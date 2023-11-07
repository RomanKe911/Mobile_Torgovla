package kg.roman.Mobile_Torgovla.DB_Logins;

import android.provider.BaseColumns;

/**
 * Created by admin on 12/13/2015.
 */
public final class DbContract_Logins {
    public DbContract_Logins() {

    }

    public static abstract class TableUser implements BaseColumns {
        public static final String TABLE_NAME = "bern";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SNAME = "s_name";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_REGION = "region";
        public static final String COLUMN_TELEFON = "telefon";
        public static final String COLUMN_KOD = "kod_mobile";
        public static final String COLUMN_UID = "UID";
        public static final String COLUMN_TYPE = "type";
    }

    public static final String SQL_CREATE_TABLE_USER = "CREATE TABLE " + TableUser.TABLE_NAME + "(" +
            TableUser._ID + " INTEGER PRIMARY KEY," +
            TableUser.COLUMN_NAME + " TEXT NOT NULL," +
            TableUser.COLUMN_SNAME + " TEXT NOT NULL," +
            TableUser.COLUMN_PASSWORD + " TEXT NOT NULL," +
            TableUser.COLUMN_IMAGE + " TEXT NOT NULL," +
            TableUser.COLUMN_REGION + " TEXT NOT NULL," +
            TableUser.COLUMN_TELEFON + " TEXT NOT NULL," +
            TableUser.COLUMN_KOD + " TEXT," +
            TableUser.COLUMN_UID + " TEXT," +
            TableUser.COLUMN_TYPE + " TEXT);";

    public static final String SQL_DELETE_DB = "DROP TABLE IF EXISTS " + TableUser.TABLE_NAME + ";";
}
