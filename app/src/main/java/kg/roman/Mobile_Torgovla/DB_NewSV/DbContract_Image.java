package kg.roman.Mobile_Torgovla.DB_NewSV;

import android.provider.BaseColumns;

/**
 * Created by admin on 12/13/2015.
 */
public final class DbContract_Image {
    public DbContract_Image() {

    }

    public static abstract class TableUser implements BaseColumns {
        public static final String TABLE_NAME = "base5_image";
        public static final String COLUMN_KODUID = "koduid";
        public static final String COLUMN_KOD = "kod";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IMAGE = "image";
    }

    public static final String SQL_CREATE_TABLE_USER = "CREATE TABLE " + TableUser.TABLE_NAME + "(" +
            TableUser._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TableUser.COLUMN_KODUID + " TEXT," +
            TableUser.COLUMN_KOD + " TEXT," +
            TableUser.COLUMN_NAME + " TEXT," +
            TableUser.COLUMN_IMAGE+" TEXT);";

    public static final String SQL_DELETE_DB = "DROP TABLE IF EXISTS " + TableUser.TABLE_NAME + ";";
}

