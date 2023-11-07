package kg.roman.Mobile_Torgovla.DB_NewSV;

import android.provider.BaseColumns;

/**
 * Created by admin on 12/13/2015.
 */
public final class DbContract_GroupID {
    public DbContract_GroupID() {

    }

    public static abstract class TableUser implements BaseColumns {
        public static final String TABLE_NAME = "base6";
        public static final String COLUMN_BRENDS = "brends";
        public static final String COLUMN_PER_KOD = "parents_kod";
        public static final String COLUMN_PREF = "prefix";
        public static final String COLUMN_WORK = "work";
        public static final String COLUMN_GR_TYPE = "group_type";

    }

    public static final String SQL_CREATE_TABLE_USER = "CREATE TABLE " + TableUser.TABLE_NAME + "(" +
            TableUser.COLUMN_BRENDS + " TEXT," +
            TableUser.COLUMN_PER_KOD + " TEXT," +
            TableUser.COLUMN_PREF + " TEXT," +
            TableUser.COLUMN_WORK + " TEXT," +
            TableUser.COLUMN_GR_TYPE + " TEXT);";

    public static final String SQL_DELETE_DB = "DROP TABLE IF EXISTS " + TableUser.TABLE_NAME + ";";
}

