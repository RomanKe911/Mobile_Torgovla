package kg.roman.Mobile_Torgovla.DB_NewSV;

import android.provider.BaseColumns;

/**
 * Created by admin on 12/13/2015.
 */
public final class DbContract_Nomeclature {
    public DbContract_Nomeclature() {

    }

    public static abstract class TableUser implements BaseColumns {
        public static final String TABLE_NAME = "base1";
        public static final String COLUMN_KOD = "kod";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_KOLBOX = "kolbox";
        public static final String COLUMN_CENA = "cena";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_STRIH = "strih";
        public static final String COLUMN_KODUNIV = "kod_univ";
        public static final String COLUMN_KODUID = "koduid";
        public static final String COLUMN_GROUP = "p_group";
        public static final String COLUMN_BRENDS = "brends";
        public static final String COLUMN_OSTATOK = "count";
        public static final String COLUMN_WORK = "work";
    }


        public static final String SQL_CREATE_TABLE_USER = "CREATE TABLE " + TableUser.TABLE_NAME + "(" +
                TableUser._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TableUser.COLUMN_KOD + " TEXT," +
                TableUser.COLUMN_NAME + " TEXT," +
                TableUser.COLUMN_KOLBOX + " TEXT," +
                TableUser.COLUMN_CENA + " TEXT," +
                TableUser.COLUMN_IMAGE + " TEXT," +
                TableUser.COLUMN_STRIH + " TEXT," +
                TableUser.COLUMN_KODUNIV + " TEXT," +
                TableUser.COLUMN_KODUID + " TEXT PRIMARY KEY," +
                TableUser.COLUMN_OSTATOK + " TEXT," +
                TableUser.COLUMN_BRENDS + " TEXT," +
                TableUser.COLUMN_GROUP + " TEXT," +
                TableUser.COLUMN_WORK + " TEXT);";

    public static final String SQL_DELETE_DB = "DROP TABLE IF EXISTS " + TableUser.TABLE_NAME + ";";
    }

