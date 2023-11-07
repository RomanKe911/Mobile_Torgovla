package kg.roman.Mobile_Torgovla.DB_NewSV;

import android.provider.BaseColumns;

/**
 * Created by admin on 12/13/2015.
 */
public final class DbContract_Ostatok {
    public DbContract_Ostatok() {

    }

    public static abstract class TableUser implements BaseColumns {
        public static final String TABLE_NAME = "base4";
        public static final String COLUMN_DATA= "data";
        public static final String COLUMN_COUNT = "count";
        public static final String COLUMN_KODUID = "koduid";
        public static final String COLUMN_SKLAD = "sklad";    }

    public static final String SQL_CREATE_TABLE_USER = "CREATE TABLE " + TableUser.TABLE_NAME + "(" +
            TableUser._ID + " INTEGER PRIMARY KEY," +
            TableUser.COLUMN_DATA + " TEXT," +
            TableUser.COLUMN_COUNT + " TEXT," +
            TableUser.COLUMN_KODUID + " TEXT," +
            TableUser.COLUMN_SKLAD + " TEXT);";

    public static final String SQL_DELETE_DB = "DROP TABLE IF EXISTS " + TableUser.TABLE_NAME + ";";
}

