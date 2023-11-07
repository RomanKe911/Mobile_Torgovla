package kg.roman.Mobile_Torgovla.DB_Logins;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 12/13/2015.
 */
public class DbHelper_Forma_Zakaza_Db extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "forma_zakaza_2019_db";
    public DbHelper_Forma_Zakaza_Db(Context ctx){
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DbContract_Forma_Zakaza_Db.SQL_CREATE_TABLE_USER);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(DbContract_Forma_Zakaza_Db.SQL_DELETE_DB);
        onCreate(db);
    }
}
