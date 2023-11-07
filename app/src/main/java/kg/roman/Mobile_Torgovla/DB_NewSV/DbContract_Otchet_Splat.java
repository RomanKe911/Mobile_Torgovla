package kg.roman.Mobile_Torgovla.DB_NewSV;

import android.provider.BaseColumns;

/**
 * Created by admin on 12/13/2015.
 */
public final class DbContract_Otchet_Splat {
    public DbContract_Otchet_Splat() {

    }

    public static abstract class TableUser implements BaseColumns {
        public static final String TABLE_NAME = "base1";
        public static final String COLUMN_DATA = "data";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_KOL = "kol";
        public static final String COLUMN_SUMMA= "summa";
    }


        public static final String SQL_CREATE_TABLE_USER = "CREATE TABLE " + TableUser.TABLE_NAME + "(" +
                TableUser._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TableUser.COLUMN_DATA + " TEXT," +
                TableUser.COLUMN_NAME + " TEXT," +
                TableUser.COLUMN_KOL + " TEXT," +
                TableUser.COLUMN_SUMMA + " TEXT);";

    public static final String SQL_DELETE_DB = "DROP TABLE IF EXISTS " + TableUser.TABLE_NAME + ";";
    }

