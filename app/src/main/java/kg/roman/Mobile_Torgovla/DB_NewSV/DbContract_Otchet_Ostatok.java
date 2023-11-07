package kg.roman.Mobile_Torgovla.DB_NewSV;

import android.provider.BaseColumns;

/**
 * Created by admin on 12/13/2015.
 */
public final class DbContract_Otchet_Ostatok {
    public DbContract_Otchet_Ostatok() {

    }

    public static abstract class TableUser implements BaseColumns {
        public static final String TABLE_NAME = "base1";
        public static final String COLUMN_DATA = "data";
        public static final String COLUMN_COUNT = "count";
        public static final String COLUMN_KODUID = "koduid";
        public static final String COLUMN_SKLAD= "sklad";
        public static final String COLUMN_BRENDS= "brends";
        public static final String COLUMN_NAME= "name";
        public static final String COLUMN_KODUNIV= "kod_univ";
        public static final String COLUMN_CENA= "cena";
        public static final String COLUMN_IMAGE= "image";
    }


        public static final String SQL_CREATE_TABLE_USER = "CREATE TABLE " + TableUser.TABLE_NAME + "(" +
                TableUser._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TableUser.COLUMN_DATA + " TEXT," +
                TableUser.COLUMN_COUNT + " TEXT," +
                TableUser.COLUMN_KODUID + " TEXT," +
                TableUser.COLUMN_SKLAD + " TEXT," +
                TableUser.COLUMN_BRENDS + " TEXT," +
                TableUser.COLUMN_NAME + " TEXT," +
                TableUser.COLUMN_CENA + " TEXT," +
                TableUser.COLUMN_KODUNIV + " TEXT," +
                TableUser.COLUMN_IMAGE + " TEXT);";

    public static final String SQL_DELETE_DB = "DROP TABLE IF EXISTS " + TableUser.TABLE_NAME + ";";
    }

