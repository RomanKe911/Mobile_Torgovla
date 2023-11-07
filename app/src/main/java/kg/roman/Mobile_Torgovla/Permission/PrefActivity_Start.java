package kg.roman.Mobile_Torgovla.Permission;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import kg.roman.Mobile_Torgovla.FTP.FtpAction;
import kg.roman.Mobile_Torgovla.FTP.FtpAsyncTask_Single_Conn_AllDB;
import kg.roman.Mobile_Torgovla.FTP.FtpConnection;
import kg.roman.Mobile_Torgovla.FTP.ListAdapterAde_Pref_Ftp;
import kg.roman.Mobile_Torgovla.FTP.ListAdapterSimple_Pref_Ftp;
import kg.roman.Mobile_Torgovla.R;


/**
 * Created by Roman on 06.03.2017.
 */
public class PrefActivity_Start extends PreferenceActivity {

    public ArrayList<ListAdapterSimple_Pref_Ftp> ftp_connect = new ArrayList<ListAdapterSimple_Pref_Ftp>();
    public ListAdapterAde_Pref_Ftp adapterPriceClients;
    public SharedPreferences.Editor ed;
    public SharedPreferences sp;

    public Calendar localCalendar = Calendar.getInstance();
    public Integer thisdata, thismonth, thisyear, thisminyte, thishour, thissecond;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now;

    public Context context_Activity;
    public Cursor cursor;
    public Cursor cursor_ost;
    public ProgressDialog pDialog;

    public String ftp_con_serv, ftp_con_login, ftp_con_pass, ftp_con_put;
    public Preference button_agents, button_kagents, button_suncape, button_Update;

    public Preference button_ostatki_update, button_ostatki_sync;
    public Preference button_price_update, button_price_sync, button_load_RN;
    public String name_db, data_ostatok;
    public String[][] mass;
    public ContentValues localContentValues, localContentValues_Osn, localContentValues_Region, localContentValues_All;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_start);
        context_Activity = PrefActivity_Start.this;
        ftp_con_serv = "176.123.246.244";
        ftp_con_login = "sunbell_siberica";
        ftp_con_pass = "Roman911NFS";

        button_agents = (Preference) findPreference("button_logins");
        button_kagents = (Preference) findPreference("button_kagents");
        button_suncape = (Preference) findPreference("button_suncape");
        button_Update = (Preference) findPreference("button_Update");
        button_load_RN = (Preference) findPreference("button_load_RN");


        button_ostatki_update = (Preference) findPreference("button_ostatok_update");
        button_ostatki_sync = (Preference) findPreference("button_ostatok_sync");

        button_price_update = (Preference) findPreference("button_price_update");
        button_price_sync = (Preference) findPreference("button_price_sync");

        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        ed = sp.edit();
        data_ostatok = "";
        //  Calendate_New();
        Update_Date_Summary();


      /*  name_db = "otcheti_db";
        ed.putString("Data_update_ftp_out", name_db);
        Loading_Debet_Update();
        ed.commit();*/

        button_load_RN.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Calendate_New();
                name_db = "suncape_rn_db";
                ed.putString("Data_update_ftp_out", name_db);
                Loading_Ostatok_Update();
                button_suncape.setSummary(this_data_now);
                ed.putString("Data_update_db_loading_RN", button_load_RN.getSummary().toString()); // обновление номеклатуры
                ed.commit();
                return false;
            }
        });


        button_agents.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Calendate_New();
                name_db = "logins";
                ed.putString("Data_update_ftp_out", name_db);
                Loading_Ostatok_Update();
                button_agents.setSummary(this_data_now);
                ed.putString("Data_update_db_agents", button_agents.getSummary().toString()); // обновление агентов
                ed.commit();
                return false;
            }
        });
        button_kagents.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Calendate_New();
                name_db = "logins";
                ed.putString("Data_update_ftp_out", name_db);
                Loading_Ostatok_Update();
                button_kagents.setSummary(this_data_now);
                ed.putString("Data_update_db_kagents", button_kagents.getSummary().toString()); // обновление контрагентов
                ed.commit();
                return false;
            }
        });
        button_suncape.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //    Update_Ostatki();
                Calendate_New();
                name_db = "suncape_all_db";
                ed.putString("Data_update_ftp_out", name_db);
                Loading_Ostatok_Update();
                button_suncape.setSummary(this_data_now);
                ed.putString("Data_update_db_suncape", button_suncape.getSummary().toString()); // обновление номеклатуры
                ed.commit();
                return false;
            }
        });


        button_ostatki_update.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Calendate_New();
                name_db = "ostatki_db";
                ed.putString("Data_update_ftp_out", name_db);
                Loading_Ostatok_Update();
                button_ostatki_update.setSummary(this_data_now);
                ed.putString("Data_update_db_ostatok", button_ostatki_update.getSummary().toString()); // обновление остатков
                ed.commit();
                return false;
            }
        });     // Обработка остатков с сервера
        button_price_update.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Calendate_New();
                name_db = "price_db";
                ed.putString("Data_update_ftp_out", name_db);
                Loading_Price_Update();
                button_price_update.setSummary(this_data_now);
                ed.putString("Data_update_db_price", button_price_update.getSummary().toString()); // обновление остатков
                ed.commit();
                return false;
            }
        });       // Обработка цен с сервера
        button_ostatki_sync.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    Calendate_New();
                    pDialog = new ProgressDialog(context_Activity);
                    pDialog.setMessage("Загрузка продуктов.");
                    pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    pDialog.setProgress(0);
                    pDialog.setMax(100);
                    pDialog.show();
                    MyAsyncTask_Sync_Ostatki asyncTask = new MyAsyncTask_Sync_Ostatki();
                    asyncTask.execute();
                    Loading_Ostatok_Sync();
                }
                catch (Exception e)
                {
                    Log.e("Pref", "Ошибка в сихронизации остатков!");
                    Toast.makeText(context_Activity, "Ошибка в сихронизации остатков!", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });       // Синхронизация остатков
        button_price_sync.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    Calendate_New();
                    pDialog = new ProgressDialog(context_Activity);
                    pDialog.setMessage("Загрузка продуктов.");
                    pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    pDialog.setProgress(0);
                    pDialog.setMax(100);
                    pDialog.show();
                    MyAsyncTask_Sync_Price asyncTask = new MyAsyncTask_Sync_Price();
                    asyncTask.execute();
                }
                catch (Exception e)
                {
                    Log.e("Pref", "Ошибка в сихронизации цен!");
                    Toast.makeText(context_Activity, "Ошибка в сихронизации цен!", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });         // Синхронизация цен



    }

    protected void Calendare_This_Data() {  // получаем данные времени и имя месяца
        thisdata = Integer.valueOf(localCalendar.get(Calendar.DAY_OF_MONTH));
        thismonth = Integer.valueOf(1 + localCalendar.get(Calendar.MONTH));
        thisyear = Integer.valueOf(localCalendar.get(Calendar.YEAR));
        thissecond = Integer.valueOf(localCalendar.get(Calendar.SECOND));
        thisminyte = Integer.valueOf(localCalendar.get(Calendar.MINUTE));
        thishour = Integer.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY));
    }  // Календарная дата, месяц, год

    protected void Calendate_New() {
        /*
         SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
         String currentDateandTime = sdf.format(new Date());

         Time today = new Time(Time.getCurrentTimezone());
         today.setToNow();
         DateFormat data_this = new SimpleDateFormat("dd.MM.yyyy");
           DateFormat vrema_this = new SimpleDateFormat("HH:mm:ss");
         */
        DateFormat df_data = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df_vrema = new SimpleDateFormat("HH:mm:ss");
        DateFormat df_year = new SimpleDateFormat("yyyy");
        DateFormat df_month = new SimpleDateFormat("MM");
        DateFormat df_day = new SimpleDateFormat("dd");

        this_rn_data = df_data.format(Calendar.getInstance().getTime());
        this_rn_vrema = df_vrema.format(Calendar.getInstance().getTime());
        this_rn_year = df_year.format(Calendar.getInstance().getTime());
        this_rn_month = df_month.format(Calendar.getInstance().getTime());
        this_rn_day = df_day.format(Calendar.getInstance().getTime());

        // this_data_now = this_rn_day + "-" + this_rn_month + "-" + this_rn_year;  // Формат для отображения

        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(this_rn_year), Integer.valueOf(this_rn_month) - 1, Integer.valueOf(this_rn_day));
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd:MM:yyyy");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateString_NOW = dateFormat1.format(calendar.getTime());
        String dateString_WORK = dateFormat2.format(calendar.getTime());
        this_data_now = dateString_NOW + " " + this_rn_vrema;
        Log.e("WJ_FormaL2:", "!DataStart:" + dateString_NOW);
        //  Log.e("WJ_FormaL2:", "!DataEnd:" + dateString2);

    } // Загрузка даты и время

    protected void Loading_Ostatok_Update() {
        ftp_con_put = "/Server/Data_Db/" + name_db + ".db3";
        FtpConnection c = new FtpConnection();
        c.server = ftp_con_serv;
        c.port = 21;
        c.username = ftp_con_login;
        c.password = ftp_con_pass;

        FtpAction a = new FtpAction();
        a.success = true;
        a.connection = c;
        a.remoteFilename = ftp_con_put;

        new FtpAsyncTask_Single_Conn_AllDB(PrefActivity_Start.this).execute(a);
    }  // Скачивание базы остатков

    protected void Loading_Price_Update() {
        ftp_con_put = "/Server/Data_Db/" + name_db + ".db3";
        FtpConnection c = new FtpConnection();
        c.server = ftp_con_serv;
        c.port = 21;
        c.username = ftp_con_login;
        c.password = ftp_con_pass;

        FtpAction a = new FtpAction();
        a.success = true;
        a.connection = c;
        a.remoteFilename = ftp_con_put;

        new FtpAsyncTask_Single_Conn_AllDB(PrefActivity_Start.this).execute(a);
    }  // Скачивание базы цены

    protected void Loading_Debet_Update() {
        ftp_con_put = "/Server/Data_Db/otcheti_db.db3";
        FtpConnection c = new FtpConnection();
        c.server = ftp_con_serv;
        c.port = 21;
        c.username = ftp_con_login;
        c.password = ftp_con_pass;

        FtpAction a = new FtpAction();
        a.success = true;
        a.connection = c;
        a.remoteFilename = ftp_con_put;

        new FtpAsyncTask_Single_Conn_AllDB(PrefActivity_Start.this).execute(a);
    }  // Скачивание




    protected void Loading_Ostatok_Sync() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("ostatki_db.db3", MODE_PRIVATE, null);

        String query_up = "SELECT base4_ost.data, base4_ost.koduid, base4_ost.sklad, base4_ost.count\n" +
                "FROM base4_ost;";

        cursor = db.rawQuery(query_up, null);

        cursor.moveToPosition(2);
        String data = cursor.getString(cursor.getColumnIndexOrThrow("data"));

        String month = data.substring(0, data.indexOf('/'));
        String day = data.substring(data.indexOf('/') + 1, data.lastIndexOf('/'));
        String year = data.substring(data.lastIndexOf('/') + 1);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(year), Integer.valueOf(month) - 1, Integer.valueOf(day));
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
        String dateString_NOW = dateFormat1.format(calendar.getTime());

        data_ostatok = dateString_NOW;
        cursor.close();
        db.close();
    }  // Синхронизация данных остатков

    private class MyAsyncTask_Sync_Ostatki extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            pDialog.setMessage("Синхронизация остатков. " + values[0] + "/2 Подождите...");
            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                for (int i = 0; i <= 2; i++) {
                    switch (i) {
                        case 1: {
                            publishProgress(i);
                            getFloor();  // Синхронизация файлов для всех складов
                        }
                        break;
                        case 2: {
                            publishProgress(i);
                            getFloor2(); // Синхронизация с основной базой
                        }
                        break;
                        case 3:
                            break;
                    }
                }
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
            Log.e("ПОТОК=", "Конец потока");
            pDialog.setProgress(0);
            button_ostatki_sync.setSummary("Дата обновление: " + this_data_now + "\r\nОстатки на: " + data_ostatok);

            ed.putString("Data_update_db_ostatok_data", "Дата обновление: " + this_data_now + "\r\nОстатки на: " + data_ostatok); // обновление остатков
            ed.commit();

            //  pDialog.dismiss();
        }

        private void getFloor() throws InterruptedException {
            pDialog.setMessage("Загрузка продуктов. 1/2 Подождите...");
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase("ostatki_db.db3", MODE_PRIVATE, null);
            db.delete("base41_all", null, null);
            db.delete("base42_osn", null, null);
            db.delete("base43_region", null, null);
           /* String query_up = "SELECT base4_ost.data, base4_ost.koduid, " +
                    "base4_ost.sklad, SUM(base4_ost.count) AS 'count'\n" +
                    " FROM base4_ost\n" +
                    " WHERE base4_ost.sklad !='1EB83A85-67D4-46A2-A9DE-14B419A8DB65' " +
                    "GROUP BY base4_ost.koduid;";*/

            String query_up = "SELECT base4_ost.data, base4_ost.koduid, " +
                    "base4_ost.sklad, base4_ost.count" +
                    " FROM base4_ost\n" +
                    " WHERE base4_ost.sklad !='1EB83A85-67D4-46A2-A9DE-14B419A8DB65';";

            cursor = db.rawQuery(query_up, null);
            localContentValues_Osn = new ContentValues();
            localContentValues_Region = new ContentValues();
            localContentValues_All = new ContentValues();
              /*  BigDecimal f1 = new BigDecimal(0.0);
                BigDecimal pointOne = new BigDecimal(0.1);
                for (int i = 1; i <= 11; i++) {
                    f1 = f1.add(pointOne);
                }*/
            BigDecimal f1 = new BigDecimal(0.0);
            BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(cursor.getCount()));

            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
                String count = cursor.getString(cursor.getColumnIndexOrThrow("count"));
                String koduid = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));
                String sklad = cursor.getString(cursor.getColumnIndexOrThrow("sklad"));

                if (sklad.equals("EA661E8F-90D1-48BF-A530-5998FB65BFDD")) {
                    Log.e("База регион=", koduid + ", Обновлено");
                    localContentValues_Region.put("data", data);
                    localContentValues_Region.put("count", count);
                    localContentValues_Region.put("koduid", koduid);
                    localContentValues_Region.put("sklad", sklad);
                    db.insert("base43_region", null, localContentValues_Region);
                }
                if (!sklad.equals("EA661E8F-90D1-48BF-A530-5998FB65BFDD")) {
                    Log.e("База основная=", koduid + ", Обновлено!");
                    localContentValues_Osn.put("data", data);
                    localContentValues_Osn.put("count", count);
                    localContentValues_Osn.put("koduid", koduid);
                    localContentValues_Osn.put("sklad", sklad);
                    db.insert("base42_osn", null, localContentValues_Osn);
                }
                Log.e("База полная=", koduid + ", Обновлено!");
                localContentValues_All.put("data", data);
                localContentValues_All.put("count", count);
                localContentValues_All.put("koduid", koduid);
                localContentValues_All.put("sklad", sklad);
                db.insert("base41_all", null, localContentValues_All);

                f1 = f1.add(pointOne);
                pDialog.setProgress(f1.intValue());
                cursor.moveToNext();
            }

            cursor.close();
            db.close();
            TimeUnit.SECONDS.sleep(1);
        }  // Синхронизация файлов для всех складов

        private void getFloor2() throws InterruptedException {
            SQLiteDatabase db_ost = getBaseContext().openOrCreateDatabase("ostatki_db.db3", MODE_PRIVATE, null);
            SQLiteDatabase db_sunc = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);

            db_sunc.delete("base4_ost", null, null);

           /* String query_base_ost = "SELECT base42_osn.data, base42_osn.koduid, base42_osn.sklad, base42_osn.count" +
                    " FROM base42_osn";*/

           /* String query_base_ost = "SELECT base4_ost.data, base4_ost.koduid, " +
                    "base4_ost.sklad, SUM(base4_ost.count) AS 'count'\n" +
                    " FROM base4_ost\n" +
                    " WHERE base4_ost.sklad !='1EB83A85-67D4-46A2-A9DE-14B419A8DB65' " +
                    "GROUP BY base4_ost.koduid;";   */
            String query_base_ost = "SELECT base4_ost.data, base4_ost.koduid, base4_ost.sklad, base4_ost.count\n" +
                    "FROM base4_ost\n" +
                    "WHERE base4_ost.sklad !='1EB83A85-67D4-46A2-A9DE-14B419A8DB65'";

            Cursor cursor_2 = db_ost.rawQuery(query_base_ost, null);

            ContentValues localContentValues = new ContentValues();

            BigDecimal f2 = new BigDecimal(0.0);
            BigDecimal pointTwo = new BigDecimal(100 / Double.valueOf(cursor_2.getCount()));
            // cursor_2.moveToPosition(1);
            cursor_2.moveToFirst();

            while (cursor_2.isAfterLast() == false) {
                String data = cursor_2.getString(cursor_2.getColumnIndexOrThrow("data"));
                String count = cursor_2.getString(cursor_2.getColumnIndexOrThrow("count"));
                String koduid = cursor_2.getString(cursor_2.getColumnIndexOrThrow("koduid"));
                String sklad = cursor_2.getString(cursor_2.getColumnIndexOrThrow("sklad"));

                localContentValues.put("data", data);
                localContentValues.put("count", count);
                localContentValues.put("koduid", koduid);
                localContentValues.put("sklad", sklad);

                Log.e("Update=", koduid + count + ", Обновлено");
                db_sunc.insert("base4_ost", null, localContentValues);

                f2 = f2.add(pointTwo);
                pDialog.setProgress(f2.intValue());
                cursor_2.moveToNext();
            }

            cursor_2.close();
            db_ost.close();
            db_sunc.close();

            pDialog.dismiss();
            TimeUnit.SECONDS.sleep(1);
        } // Синхронизация с основной базой
    }

    private class MyAsyncTask_Sync_Price extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            pDialog.setMessage("Синхронизация цен. Подождите...");
            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                publishProgress(1);
                getFloor();  // Синхронизация файлов для всех складов
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
            Log.e("ПОТОК=", "Конец потока");
            pDialog.setProgress(0);
            button_price_sync.setSummary("Дата обновление: " + this_data_now + "\r\nЦены на: " + data_ostatok);

            ed.putString("Data_update_db_price_sync", "Дата обновление: " + this_data_now + "\r\nЦены на: " + this_data_now); // обновление остатков
            ed.commit();

            //  pDialog.dismiss();
        }

        private void getFloor() throws InterruptedException {
            pDialog.setMessage("Загрузка продуктов. Подождите...");
            SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
            SQLiteDatabase db_second = getBaseContext().openOrCreateDatabase("price_db.db3", MODE_PRIVATE, null);

            db_prev.delete("base5_price", null, null);

            String query = "SELECT base5_price.koduid, base5_price.cena\n" +
                    "FROM base5_price;";
            cursor = db_second.rawQuery(query, null);
            localContentValues = new ContentValues();
            BigDecimal f1 = new BigDecimal(0.0);
            BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(cursor.getCount()));
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String koduid = cursor.getString(cursor.getColumnIndex("koduid"));
                String cena = cursor.getString(cursor.getColumnIndex("cena"));

                Log.e("База полная=", koduid + ", Обновлено!");
                localContentValues.put("koduid", koduid );
                localContentValues.put("cena", cena);
                db_prev.insert("base5_price", null, localContentValues);
                f1 = f1.add(pointOne);
                pDialog.setProgress(f1.intValue());
                cursor.moveToNext();
            }
            pDialog.dismiss();
            TimeUnit.SECONDS.sleep(1);
            cursor.close();
            db_prev.close();
            db_second.close();

        }  // Синхронизация файлов для всех складов

    }










    protected void Loading_Price_Sync() {
        SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
        SQLiteDatabase db_second = getBaseContext().openOrCreateDatabase("price_db.db3", MODE_PRIVATE, null);

        db_prev.delete("base5_price", null, null);

        String query = "SELECT base5_price.koduid, base5_price.cena\n" +
                "FROM base5_price;";
        cursor = db_second.rawQuery(query, null);
        localContentValues = new ContentValues();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String koduid = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));
            String cena = cursor.getString(cursor.getColumnIndexOrThrow("cena"));

            Log.e("База полная=", koduid + ", Обновлено!");
            localContentValues.put("koduid", koduid );
            localContentValues.put("cena", cena);
            db_prev.insert("base5_price", null, localContentValues);
            cursor.moveToNext();
        }

        cursor.close();
        db_prev.close();
        db_second.close();



    }  // Синхронизация данных цены

    protected void Update_Date_Summary() {
        // Дата последнего обновления данных Агентов
        if (!sp.getString("Data_update_db_agents", "0").isEmpty()) {
            button_agents.setSummary(sp.getString("Data_update_db_agents", "0"));
        } else button_agents.setSummary("нет данных об обновлении");

        // Дата последнего обновления данных Контрагентов
        if (!sp.getString("Data_update_db_kagents", "0").isEmpty()) {
            button_kagents.setSummary(sp.getString("Data_update_db_kagents", "0"));
        } else button_kagents.setSummary("нет данных об обновлении");

        // Дата последнего обновления данных Номенклатура
        if (!sp.getString("Data_update_db_suncape", "0").isEmpty()) {
            button_suncape.setSummary(sp.getString("Data_update_db_suncape", "0"));
        } else button_suncape.setSummary("нет данных об обновлении");

        // Дата последнего обновления данных Остатков
        if (!sp.getString("Data_update_db_ostatok", "0").isEmpty()) {
            button_ostatki_update.setSummary(sp.getString("Data_update_db_ostatok", "0"));
        } else button_ostatki_update.setSummary("нет данных об обновлении");

        // Дата последнего обновления данных Остатков дата
        if (!sp.getString("Data_update_db_ostatok_data", "0").isEmpty()) {
            button_ostatki_sync.setSummary(sp.getString("Data_update_db_ostatok_data", "0"));
        } else button_ostatki_sync.setSummary("нет данных об обновлении");



        // Дата последнего обновления данных Цен
        if (!sp.getString("Data_update_db_price", "0").isEmpty()) {
            button_price_update.setSummary(sp.getString("Data_update_db_price", "0"));
        } else button_price_update.setSummary("нет данных об обновлении");

        // Дата последней синхронизации Цен
        if (!sp.getString("Data_update_db_price_sync", "0").isEmpty()) {
            button_price_sync.setSummary(sp.getString("Data_update_db_price_sync", "0"));
        } else button_price_sync.setSummary("нет данных об обновлении");


        // Дата последней скачивания RN
        if (!sp.getString("Data_update_db_loading_RN", "0").isEmpty()) {
            button_load_RN.setSummary(sp.getString("Data_update_db_loading_RN", "0"));
        } else button_load_RN.setSummary("нет данных об обновлении");

    }

    protected void ProgressDialog_Sync_Ostatok() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Загрузка продуктов. 1/2 Подождите...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setProgress(0);
        pDialog.setMax(100);
        pDialog.show();


        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = getBaseContext().openOrCreateDatabase("ostatki_db.db3", MODE_PRIVATE, null);
                db.delete("base41_all", null, null);
                db.delete("base42_osn", null, null);
                db.delete("base43_region", null, null);
                String query_up = "SELECT base4_ost.data, base4_ost.koduid, " +
                        "base4_ost.sklad, SUM(base4_ost.count) AS 'count'\n" +
                        " FROM base4_ost\n" +
                        " WHERE base4_ost.sklad !='1EB83A85-67D4-46A2-A9DE-14B419A8DB65' " +
                        "GROUP BY base4_ost.koduid;";

                cursor = db.rawQuery(query_up, null);
                localContentValues_Osn = new ContentValues();
                localContentValues_Region = new ContentValues();
                localContentValues_All = new ContentValues();
              /*  BigDecimal f1 = new BigDecimal(0.0);
                BigDecimal pointOne = new BigDecimal(0.1);
                for (int i = 1; i <= 11; i++) {
                    f1 = f1.add(pointOne);
                }*/
                BigDecimal f1 = new BigDecimal(0.0);
                BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(cursor.getCount()));

                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    String data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
                    String count = cursor.getString(cursor.getColumnIndexOrThrow("count"));
                    String koduid = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));
                    String sklad = cursor.getString(cursor.getColumnIndexOrThrow("sklad"));

                    if (sklad.equals("EA661E8F-90D1-48BF-A530-5998FB65BFDD")) {
                        Log.e("База регион=", koduid + ", Обновлено");
                        localContentValues_Region.put("data", data);
                        localContentValues_Region.put("count", count);
                        localContentValues_Region.put("koduid", koduid);
                        localContentValues_Region.put("sklad", sklad);
                        db.insert("base43_region", null, localContentValues_Region);
                    }
                    if (!sklad.equals("EA661E8F-90D1-48BF-A530-5998FB65BFDD")) {
                        Log.e("База основная=", koduid + ", Обновлено!");
                        localContentValues_Osn.put("data", data);
                        localContentValues_Osn.put("count", count);
                        localContentValues_Osn.put("koduid", koduid);
                        localContentValues_Osn.put("sklad", sklad);
                        db.insert("base42_osn", null, localContentValues_Osn);
                    }

                    Log.e("База полная=", koduid + ", Обновлено!");
                    localContentValues_All.put("data", data);
                    localContentValues_All.put("count", count);
                    localContentValues_All.put("koduid", koduid);
                    localContentValues_All.put("sklad", sklad);
                    db.insert("base41_all", null, localContentValues_All);

                    f1 = f1.add(pointOne);
                    pDialog.setProgress(f1.intValue());
                    cursor.moveToNext();
                }

                cursor.close();
                db.close();

                pDialog.dismiss();
            }
        }).start();

    }  // Загрузка с прогрессом

    protected void ProgressDialog_Sync_Ostatok_To_Suncape() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Загрузка продуктов 2/2. Подождите...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setProgress(0);
        pDialog.setMax(100);
        pDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                SQLiteDatabase db_ost = getBaseContext().openOrCreateDatabase("ostatki_db.db3", MODE_PRIVATE, null);
                SQLiteDatabase db_sunc = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);

                db_sunc.delete("base4_ost", null, null);

                String query_base_ost = "SELECT base42_osn.data, base42_osn.koduid, base42_osn.sklad, base42_osn.count" +
                        " FROM base42_osn";

                Cursor cursor_2 = db_ost.rawQuery(query_base_ost, null);

                ContentValues localContentValues = new ContentValues();

                BigDecimal f2 = new BigDecimal(0.0);
                BigDecimal pointTwo = new BigDecimal(100 / Double.valueOf(cursor_2.getCount()));
                // cursor_2.moveToPosition(1);
                cursor_2.moveToFirst();

                while (cursor_2.isAfterLast() == false) {
                    String data = cursor_2.getString(cursor_2.getColumnIndexOrThrow("data"));
                    String count = cursor_2.getString(cursor_2.getColumnIndexOrThrow("count"));
                    String koduid = cursor_2.getString(cursor_2.getColumnIndexOrThrow("koduid"));
                    String sklad = cursor_2.getString(cursor_2.getColumnIndexOrThrow("sklad"));

                    localContentValues.put("data", data);
                    localContentValues.put("count", count);
                    localContentValues.put("koduid", koduid);
                    localContentValues.put("sklad", sklad);

                    Log.e("Update=", koduid + count + ", Обновлено");
                    db_sunc.insert("base4_ost", null, localContentValues);

                    f2 = f2.add(pointTwo);
                    pDialog.setProgress(f2.intValue());
                    cursor_2.moveToNext();
                }

                cursor_2.close();
                db_ost.close();
                db_sunc.close();


                pDialog.dismiss();
            }
        }).start();

    }  // Загрузка с прогрессом

    protected void Update_Ostatki() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("ostatki_db.db3", MODE_PRIVATE, null);

        /*
        * 1EB83A85-67D4-46A2-A9DE-14B419A8DB65 оптовый бишкек
        * 54B85B19-444C-44EB-B737-54846EE868A2	склад Каракол (SPLAT)
         63F85378-43D1-4419-9163-4F53E1DCBFE8	склад Каракол (Корзина)
         9107FD60-C949-4672-9FA5-7B179164DFF3	склад Каракол (КОСМЕТИКА)
         ABD5FCEC-0DF6-4992-97C6-F53FB5BC8969	склад Каракол (Основной)
         * 9028DC90-0B5E-4A27-8BEC-725054134D13	склад Каракол (Оптовый)
         * CC176571-7485-4A0E-B817-BA9BD633C1C6	склад Каракол (Продукты)
           EA661E8F-90D1-48BF-A530-5998FB65BFDD	склад Каракол (Регион)

*/
       /* String query_up = "SELECT base4_ost.data, base4_ost.koduid, " +
                "base4_ost.sklad, SUM(base4_ost.count) AS 'count'\n" +
                " FROM base4_ost\n" +
                " WHERE base4_ost.sklad !='1EB83A85-67D4-46A2-A9DE-14B419A8DB65' " +
                "AND base4_ost.sklad !='EA661E8F-90D1-48BF-A530-5998FB65BFDD' " +
                "GROUP BY base4_ost.koduid;";*/

        String query_up = "SELECT base4_ost.data, base4_ost.koduid, " +
                "base4_ost.sklad, SUM(base4_ost.count) AS 'count'\n" +
                " FROM base4_ost\n" +
                " WHERE base4_ost.sklad !='1EB83A85-67D4-46A2-A9DE-14B419A8DB65' " +
                "GROUP BY base4_ost.koduid;";

        cursor = db.rawQuery(query_up, null);

        localContentValues_Osn = new ContentValues();
        localContentValues_Region = new ContentValues();
        localContentValues_All = new ContentValues();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String data = cursor.getString(cursor.getColumnIndex("data"));
            String count = cursor.getString(cursor.getColumnIndex("count"));
            String koduid = cursor.getString(cursor.getColumnIndex("koduid"));
            String sklad = cursor.getString(cursor.getColumnIndex("sklad"));

            Log.e("Условие=", data_ostatok + "Пустой");
            if (sklad.equals("EA661E8F-90D1-48BF-A530-5998FB65BFDD")) {
                Log.e("База регион=", koduid + ", Обновлено");
                localContentValues_Region.put("data", data);
                localContentValues_Region.put("count", count);
                localContentValues_Region.put("koduid", koduid);
                localContentValues_Region.put("sklad", sklad);
                db.insert("base43_region", null, localContentValues_Region);
            }
            if (!sklad.equals("EA661E8F-90D1-48BF-A530-5998FB65BFDD")) {
                Log.e("База основная=", koduid + ", Обновлено!");
                localContentValues_Osn.put("data", data);
                localContentValues_Osn.put("count", count);
                localContentValues_Osn.put("koduid", koduid);
                localContentValues_Osn.put("sklad", sklad);
                db.insert("base42_osn", null, localContentValues_Osn);
            }

            Log.e("База полная=", koduid + ", Обновлено!");
            localContentValues_All.put("data", data);
            localContentValues_All.put("count", count);
            localContentValues_All.put("koduid", koduid);
            localContentValues_All.put("sklad", sklad);
            db.insert("base41_all", null, localContentValues_All);


            cursor.moveToNext();
        }

        cursor.close();
        db.close();
    }

    protected void Sync_Nomk_Suncape() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("ostatki_db.db3", MODE_PRIVATE, null);
        SQLiteDatabase db1 = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
        db1.delete("base4_ost", null, null);

        String query_base_ost = "SELECT base42_osn.data, base42_osn.koduid, base42_osn.sklad, base42_osn.count" +
                " FROM base42_osn";
        ContentValues localContentValues = new ContentValues();
        Cursor cursor = db.rawQuery(query_base_ost, null);
        cursor.moveToPosition(1);
        while (cursor.isAfterLast() == false) {
            String data = cursor.getString(cursor.getColumnIndex("data"));
            String count = cursor.getString(cursor.getColumnIndex("count"));
            String koduid = cursor.getString(cursor.getColumnIndex("koduid"));
            String sklad = cursor.getString(cursor.getColumnIndex("sklad"));

            localContentValues.put("data", data);
            localContentValues.put("count", count);
            localContentValues.put("koduid", koduid);
            localContentValues.put("sklad", sklad);
            cursor.moveToNext();
            Log.e("Update=", koduid + count + ", Обновлено");
            db1.insert("base4_ost", null, localContentValues);
        }

        cursor.close();
        db.close();
        db1.close();
    }

    protected void Sync_Ostatok() {
        SQLiteDatabase db_ost = getBaseContext().openOrCreateDatabase("ostatki_db.db3", MODE_PRIVATE, null);
        SQLiteDatabase db_sunc = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);

        final String DELETE_TABLE = "DROP TABLE base4_ost";
        db_sunc.execSQL(DELETE_TABLE);

        final String DELETE_Create = "CREATE TABLE base4_ost (\n" +
                "    data   TEXT,\n" +
                "    count  TEXT,\n" +
                "    koduid TEXT,\n" +
                "    sklad  TEXT,\n" +
                "    FOREIGN KEY (\n" +
                "        koduid\n" +
                "    )\n" +
                "    REFERENCES base3_uid (koduid) \n" +
                ");";
        db_sunc.execSQL(DELETE_Create);

        String query_base_ost = "SELECT base42_osn.data, base42_osn.koduid, base42_osn.sklad, base42_osn.count" +
                " FROM base42_osn";

        Cursor cursor_2 = db_ost.rawQuery(query_base_ost, null);

        ContentValues localContentValues = new ContentValues();

        BigDecimal f2 = new BigDecimal(0.0);
        BigDecimal pointTwo = new BigDecimal(100 / Double.valueOf(cursor_2.getCount()));
        cursor_2.moveToPosition(1);
        // cursor_2.moveToFirst();

        while (cursor_2.isAfterLast() == false) {
            String data = cursor_2.getString(cursor_2.getColumnIndex("data"));
            String count = cursor_2.getString(cursor_2.getColumnIndex("count"));
            String koduid = cursor_2.getString(cursor_2.getColumnIndex("koduid"));
            String sklad = cursor_2.getString(cursor_2.getColumnIndex("sklad"));

            localContentValues.put("data", data);
            localContentValues.put("count", count);
            localContentValues.put("koduid", koduid);
            localContentValues.put("sklad", sklad);

            Log.e("Update=", koduid + count + ", Обновлено");
            db_sunc.insert("base4_ost", null, localContentValues);

            f2 = f2.add(pointTwo);
            pDialog.setProgress(f2.intValue());
            cursor_2.moveToNext();
        }

        cursor_2.close();
        db_ost.close();
        db_sunc.close();
    }





    protected void Loading_File_FTP_Const()
    {

    }


}