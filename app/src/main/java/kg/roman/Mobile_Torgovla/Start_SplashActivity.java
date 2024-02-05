package kg.roman.Mobile_Torgovla;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_Login;
import kg.roman.Mobile_Torgovla.ClassNew.Pair;
import kg.roman.Mobile_Torgovla.DB_Logins.DbHelper_Logins;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Login;
import kg.roman.Mobile_Torgovla.Permission.PermissionUtils;
import kg.roman.Mobile_Torgovla.Permission.PrefActivity_Splash;
import kg.roman.Mobile_Torgovla.Setting.Setting_Start;
import kg.roman.Mobile_Torgovla.Work_Journal.WJ_Global_Activity;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Users;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Users_ResourceParser;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Start_SplashActivity extends AppCompatActivity {

    public ArrayList<ListAdapterSimple_Login> login_aut = new ArrayList<ListAdapterSimple_Login>();
    public ListAdapterAde_Login adapterPriceClients;


    public ImageView img_internet, img_xml, img_db3;
    public ProgressBar progressBar_internet, progressBar_xml, getProgressBar_sql;

    public Context context;
    public Integer NUMBER_OF_REQUEST;
    public TextView textView_log;
    public File file_put, file_reg;
    public Toolbar toolbar;
    public Context context_Activity;
    public String version, name_pack, Android_id, new_kod_mobile, vis_brends;
    public Integer version_code, version_release;
    public SharedPreferences.Editor ed;
    public SharedPreferences sp;
    public boolean Open_Menu;
    public ProgressDialog pDialog;
    public Cursor cursor, cursor_ID;
    public AlertDialog.Builder builder;
    public AlertDialog dialog;
    public ContentValues localContentValues;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now;

    public String[] mass_distr;
    public TextView textView_title;
    public String mail_login, mail_pass, mail_start, mail_end;  // переменные для подключения к почте
    public String db3_Const, db3_Base, db3_RN;  // переменные работы с данными
    public String from, new_write, attach, text, title, where;  // переменные для работы с почтой
    public String ftp_con_serv, ftp_con_login, ftp_con_pass, put_distr_xml, put_distr_db3;   // переменные для работы с сервером
    public Boolean b_net = false, b_xml = false, b_db3 = false;
    public ViewFlipper flipper;
    public Integer number_update_table;
    public String TAG = "";
    private static final int PERMISSION_STORAGE = 101;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt_splash);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        //   MultiDex.install(this);
        /*getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActactivity_splash_2.xmlionBar().setIcon(R.mipmap.ic_ns);*/
        context_Activity = Start_SplashActivity.this;
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        img_internet = (ImageView) findViewById(R.id.img_data_internet);
        img_xml = (ImageView) findViewById(R.id.img_data_xml);
        img_db3 = (ImageView) findViewById(R.id.img_data_sql);
        textView_log = findViewById(R.id.tvw_start_log);
        textView_title = findViewById(R.id.tvw_start_title);
        progressBar_internet = (ProgressBar) findViewById(R.id.progressBar_sql);
        progressBar_xml = (ProgressBar) findViewById(R.id.progressBar_xml);
        getProgressBar_sql = (ProgressBar) findViewById(R.id.progressBar_sql);
        flipper = findViewById(R.id.viewFlipper1);
        TAG = this.getLocalClassName();
        Android_id = Settings.Secure.getString(context_Activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        PacksInfo();
        //ListInsert_Reset_Table();
        Calendate_New();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.user_pda);
        getSupportActionBar().setTitle("Мобильная торговля");
        getSupportActionBar().setSubtitle("форма заказа " + "v." + version + "; 2018-" + this_rn_year);
        Constanta_Write();
        Anim_Start();

       /* ed = sp.edit(); // УДАЛЕНИЕ НЕ НУЖНЫХ НАСТРОЕК
        ed.remove("multi_select_list_preference_1");
        ed.remove("multi_select_list_preference_brends1");
        ed.commit();*/

        try {
            Permissino_Up();  // получение разрешения на создание файлов
            // New_File(); // создание папок если ранее их не было
            New_Files_Work();
            Exists_File_XML();
            Exists_File_DB3();
            runThread();
            Log.e("Boolean", "GG1" + b_net + " " + b_xml + " " + b_db3);
            /*pDialog = new ProgressDialog(context_Activity);
            pDialog.setMessage("Загрузка продуктов...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setProgress(0);
            pDialog.setMax(100);
            pDialog.show();*/
            Start_SplashActivity.MyAsyncTask_Sync_Users asyncTask = new Start_SplashActivity.MyAsyncTask_Sync_Users(); //
            asyncTask.execute();


        } catch (Exception e) {
            Log.e(this.getLocalClassName(), "Произошла ошибка!");
            Toast.makeText(this, "Произошла ошибка!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getSupportActionBar().hide();
        getMenuInflater().inflate(R.menu.main, menu);
       /* if (Open_Menu == true) {

        } else {
            getMenuInflater().inflate(R.menu.main_null, menu);
        }*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ///////////////////////////2024
            Intent intent_prefActivity = new Intent(context_Activity, Setting_Start.class);
            startActivity(intent_prefActivity);
            finish();

/*            Intent intent_prefActivity = new Intent(context_Activity, WJ_Global_Activity.class);
            startActivity(intent_prefActivity);*/



            try {

            } catch (Exception e) {
                Toast.makeText(context_Activity, "Ошибка вход в настройки", Toast.LENGTH_SHORT).show();
                Log.e(this.getLocalClassName(), "Ошибка вход в настройки");
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    protected void Go_To_Programm() {
        Intent intent = new Intent(this, Start_AutorithActivity.class);
        startActivity(intent);
        finish();
    }

    // главный метод для проверки подключения
    public boolean checkInternet() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        // проверка подключения
        if (activeNetwork != null && activeNetwork.isConnected()) {
            try {
                // тест доступности внешнего ресурса
                URL url = new URL("http://www.google.com/");
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setRequestProperty("User-Agent", "test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1000); // Timeout в секундах
                urlc.connect();
                // статус ресурса OK
                if (urlc.getResponseCode() == 200) {
                    //	Toast.makeText(ActivityMain.this, "Есть", Toast.LENGTH_LONG).show();
                    return true;
                }
                // иначе проверка провалилась
                return false;

            } catch (IOException e) {
                Log.d("my_tag", "Ошибка проверки подключения к интернету", e);
                return false;
            }
        }

        return false;
    }

    private void runThread() {
        new Thread() {
            public void run() {
                if (checkInternet()) {
                    Log.e(Start_SplashActivity.this.getLocalClassName(), "Есть подключение");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // progressBar.setVisibility(View.VISIBLE);
                            img_internet.setVisibility(View.VISIBLE);
                            img_internet.setImageResource(R.drawable.image_start_internet);
                            b_net = true;
                            Toast.makeText(Start_SplashActivity.this, "Интернет активен!", Toast.LENGTH_LONG).show();

                            Log.e("Boolean", "GG2" + b_net + " " + b_xml + " " + b_db3);
                            if (b_db3 == true & b_xml == true & b_net == true) {
                                runThread_Start_Activity();
                                textView_log.setVisibility(View.GONE);
                                textView_title.setVisibility(View.GONE);
                            } else
                                Toast.makeText(Start_SplashActivity.this, "Нет нужных данных!", Toast.LENGTH_LONG).show();


                        }
                    });
                } else {
                    Log.e(Start_SplashActivity.this.getLocalClassName(), "Нет подключения");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Start_SplashActivity.this, "Ошибка интернета, данные не обновленны!", Toast.LENGTH_LONG).show();
                            b_net = false;
                            Log.e("Boolean", "GG3" + b_net + " " + b_xml + " " + b_db3);
                            // progressBar.setVisibility(View.VISIBLE);
                            img_internet.setVisibility(View.VISIBLE);
                            img_internet.setImageResource(R.mipmap.ic_error);
                            Open_Menu = true;
                            if (textView_log.length() > 0) {
                                textView_log.setText(textView_log.getText().toString() + "; \nнет подключения к сети \"Интернет\"");
                            } else textView_log.setText("нет подключения к сети \"Интернет\"");

                            Toast.makeText(Start_SplashActivity.this, "Файл не существует", Toast.LENGTH_LONG).show();
                            Log.e("SQLITE_DB", "Файл не существует");
                        }
                    });
                }

            }
        }.start();

    }

    private void runThread_Start_Activity() {
        new Thread() {
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(4);
                    Go_To_Programm();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

    protected void ListInsert_Reset_Table() {
        DbHelper_Logins dbHelper = new DbHelper_Logins(getApplication());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `login` (\n" +
                "  `name` TEXT NOT NULL,\n" +
                "  `s_name` TEXT NOT NULL,\n" +
                "  `password` TEXT NOT NULL,\n" +
                "  `region` TEXT NOT NULL,\n" +
                "  `telefon` TEXT NOT NULL,\n" +
                "  `image`  TEXT NOT NULL,\n" +
                "  `kod_mobile` TEXT NOT NULL,\n" +
                "  `type` TEXT NOT NULL,\n" +
                "   `UID` TEXT NOT NULL) ;";
        db.execSQL(CREATE_TABLE);
        db.close();
        dbHelper.close();
    }

    private void Permissino_Up() {
               /* Для создания файлов на версиях Android N нужно получить разрешеник:
                В Android M permissions нужно также проверять во время выполнения.
                Поэтому нужно сначала посмотреть , получили ли мы разрешение от пользователя.
                Если да - выполняем работу; нет - просим разрешение:*/
        NUMBER_OF_REQUEST = 23401;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int canRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int canWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (canRead != PackageManager.PERMISSION_GRANTED || canWrite != PackageManager.PERMISSION_GRANTED) {

                //Нужно ли нам показывать объяснения , зачем нам нужно это разрешение
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //показываем объяснение
                } else {
                    //просим разрешение
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, NUMBER_OF_REQUEST);
                }
            } else {
                //ваш код
            }
        }

    }

    protected void New_File() {
        file_put = new File("/sdcard/" + File.separator + "Price");
        if (!file_put.exists()) {
            file_put.mkdir();
            Toast.makeText(this, "Файл успешно создан", Toast.LENGTH_LONG).show();
        }


        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_const_db.db3", MODE_PRIVATE, null);
        String query = "SELECT * FROM const_logins";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String Distr = cursor.getString(cursor.getColumnIndexOrThrow("Name_Distr"));
            file_reg = new File("/sdcard/Price/" + File.separator + Distr);
            if (!file_reg.exists()) {
                file_reg.mkdir();
                Toast.makeText(this, "Файл успешно создан", Toast.LENGTH_LONG).show();
            }
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }  // Проверка создание папок для хранения XLS

    protected void New_Files_Work() {
        mass_distr = getResources().getStringArray(R.array.mass_distr_files);
        file_put = new File("/sdcard/" + File.separator + "Price");
        if (!file_put.exists()) {
            file_put.mkdir();
            Toast.makeText(this, "Файл успешно создан", Toast.LENGTH_LONG).show();
        }
        for (int i = 0; i < mass_distr.length; i++) {
            file_reg = new File("/sdcard/Price/" + File.separator + mass_distr[i]);
            if (!file_reg.exists()) {
                file_reg.mkdir();
                Toast.makeText(this, "Файл успешно создан", Toast.LENGTH_LONG).show();
            }

        }
    }

    protected void Exists_File_XML() {

        String file_xml = getDatabasePath("MTW_In_Users.xml").getAbsolutePath(); // путь к databases
        file_put = new File(file_xml);
        if (file_put.exists()) {
            //file_put.mkdir();
            img_xml.setImageResource(R.drawable.image_start_xml);
            b_xml = true;
            Log.e("SQLITE_DB ", "Файл существует");
        } else {
            img_xml.setImageResource(R.mipmap.ic_error);
            b_xml = false;
            Toast.makeText(Start_SplashActivity.this, "Файл не существует", Toast.LENGTH_LONG).show();
            Log.e("SQLITE_DB", "Файл не существует");
            if (textView_log.length() > 0) {
                textView_log.setText(textView_log.getText().toString() + "; \nнет файлов \"XML\"");
            } else textView_log.setText("нет файлов \"XML\"");
        }
    }

    protected void Exists_File_DB3() {
        String file_db3 = getDatabasePath("sunbell_const_db.db3").getAbsolutePath(); // путь к databases
        file_put = new File(file_db3);
        if (file_put.exists()) {
            //file_put.mkdir();
            img_db3.setImageResource(R.drawable.image_start_sql);
            b_db3 = true;
            Log.e("SQLITE_DB ", "Файл существует");
        } else {
            img_db3.setImageResource(R.mipmap.ic_error);
            b_db3 = false;
            if (textView_log.length() > 0) {
                textView_log.setText(textView_log.getText().toString() + "; \nнет базы данных \"Констант\"");
            } else textView_log.setText("нет базы данных \"Констант\"");

            Toast.makeText(Start_SplashActivity.this, "Файл не существует", Toast.LENGTH_LONG).show();
            Log.e("SQLITE_DB", "Файл не существует");
        }
    }


    private class MyAsyncTask_Sync_Users extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            // pDialog.setMessage("Синхронизация остатков. Подождите...");
            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                publishProgress(1);
                getFloor();  // Синхронизация
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
            //progressBar_horis.setProgress(0);
        }
///                  ????????????????????????????????????????????????????????????????????????????
        private void getFloor() throws InterruptedException {
                          /*  BigDecimal f1 = new BigDecimal(0.0);
                BigDecimal pointOne = new BigDecimal(0.1);
                for (int i = 1; i <= 11; i++) {
                    f1 = f1.add(pointOne);
                }*/
            //   pDialog.setMessage("Загрузка продуктов. Подождите...");
            try {
                String file_db = context_Activity.getDatabasePath("MTW_In_Users.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                MTW_Users_ResourceParser parser = new MTW_Users_ResourceParser();
                int eventType = xpp.getEventType();
                if (parser.parse(xpp)) {
                    SQLiteDatabase db = getBaseContext().openOrCreateDatabase("sunbell_const_db.db3", MODE_PRIVATE, null);
                    db.delete("const_agents", null, null);
                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getUsers().size()));
                    String query_up = "SELECT * FROM const_agents";
                    cursor = db.rawQuery(query_up, null);
                    localContentValues = new ContentValues();
                    for (MTW_Users ostatok : parser.getUsers()) {
                      //  Log.e("USER_MTW...", ostatok.getUid_name() + " " + ostatok.getKod_mobile());
                        Log.e("RETURN ||", Exclusive_Load(ostatok.getUid_name(), ostatok.getKod_mobile()).first.toString());
                        localContentValues.put("uid_name", ostatok.getUid_name());
                        localContentValues.put("name", ostatok.getName());
                        localContentValues.put("uid_region", ostatok.getUid_region().trim());
                        localContentValues.put("cena", ostatok.getCena());
                        localContentValues.put("uid_sklad", ostatok.getUid_sklad().trim());
                        localContentValues.put("skald", ostatok.getSklad());
                        localContentValues.put("kod_mobile", ostatok.getKod_mobile());
                        localContentValues.put("type_real", ostatok.getType_real());
                        localContentValues.put("type_user", ostatok.getType_user());
                        localContentValues.put("region", ostatok.getPut_ag());
                        localContentValues.put("user_brends", Exclusive_Load(ostatok.getUid_name(), ostatok.getKod_mobile()).first.toString());
                        db.insert("const_agents", null, localContentValues);
                        cursor.moveToNext();
                        f1 = f1.add(pointOne);
                        // progressBar_horis.setProgress(f1.intValue());
                        // pDialog.setProgress(f1.intValue());
                    }
                    cursor.close();
                    db.close();
                }
            } catch (Exception e) {

                Log.e(TAG, "Ошибка данных: не верная загрузка файла MTW_User.xml");
                // Toast.makeText(context_Activity, "Ошибка данных: не верная загрузка файла MTW_User.xml", Toast.LENGTH_SHORT).show();
            }
            TimeUnit.SECONDS.sleep(1);
            // pDialog.dismiss();
        }

    }  // Синхронизация файлов для всех складов

    protected void PacksInfo() {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;                       // Номер версии приложения
            name_pack = pInfo.packageName;                    // Имя пакета
            version_code = pInfo.versionCode;                 // Номер версии кода
            version_release = pInfo.baseRevisionCode;         //Version Code
            Log.e(this.getLocalClassName(), version);
            Log.e(this.getLocalClassName(), name_pack);
            Log.e(this.getLocalClassName(), version_code.toString());

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    // Константы для работы
    protected void Constanta_Write() {
        ftp_con_serv = "ftp.sunbell.webhost.kg";    // имя сервера     // ОБНОВЛЕНИЕ 12.11.2022(176.123.246.244) доступный 77.235.17.166
        ftp_con_login = "sunbell_siberica";  // имя логин сервера
        ftp_con_pass = "Roman911NFS";        // пароль сервера

        mail_login = "sunbellagents@gmail.com";    // логин почты
        mail_pass = "sunbell-F07iI292E1c";     // пароль почты
        mail_start = "sunbellagents@gmail.com";    // имя почты для отправки
        mail_end = "kerkin911@gmail.com";   // имя почты получателя


        db3_Const = "sunbell_const_db.db3";
        db3_Base = "sunbell_base_db.db3";
        db3_RN = "sunbell_rn_db.db3";


        // put_distr_xml = "/MT_Sunbell_Bishkek/MTW_Data";     // Путь к файлам XML на сервере Бишкек
        // put_distr_db3 = "/MT_Sunbell_Bishkek/SqliteDB";     // Путь к файлам Sqlite на сервере Бишкек


        ed = sp.edit();
        ed.putString("PEREM_FTP_SERV", ftp_con_serv);    // запись данных имя сервера
        ed.putString("PEREM_FTP_LOGIN", ftp_con_login);  //запись данных имя логин
        ed.putString("PEREM_FTP_PASS", ftp_con_pass);    //запись данных имя пароль

        // Путь к файлам Image для брендов
        ed.putString("PEREM_IMAGE_PUT_SDCARD", "/sdcard/Price/Image/");  // путь картинок на телефоне
        ed.putString("PEREM_IMAGE_PUT_PHONE", "android.resource://kg.roman.mobile_torgovla_image/drawable/");  // Путь картинок в др. приложении
        //   ed.putString("PEREM_IMAGE_PUT_PHONE", "android.resource://kg.price_list.roman_kerkin.sunbell_price/drawable/");  // Путь картинок в др. приложении

        ed.putString("PEREM_MAIL_LOGIN", mail_login);    //запись данных имя пароль
        ed.putString("PEREM_MAIL_PASS", mail_pass);    //запись данных имя пароль
        ed.putString("PEREM_MAIL_START", mail_start);    //запись данных имя пароль
        ed.putString("PEREM_MAIL_END", mail_end);    //запись данных имя пароль
        ed.putString("PEREM_DB3_CONST", db3_Const);    //База данных работа с константами
        ed.putString("PEREM_DB3_BASE", db3_Base);    //База данных работа с товаром
        ed.putString("PEREM_DB3_SOS", db3_Base);    //База данных работа с товаром
        ed.putString("PEREM_DB3_RN", db3_RN);    //База данных работа с накладными
        ed.putString("PEREM_ANDROID_ID_ADMIN", "d5781f21963ff5");                       //Универсальный код для Admin
        ed.putString("PEREM_ANDROID_ID", Android_id.substring(0, Android_id.length() - 2));   //Универсальный код для входа
        ed.putString("PEREM_ISNAME_SPINNER", "AITORIZ_ACTIVITY");                     //  разметка для Spinner
        ed.commit();

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            Log.e("Reg", pInfo.versionName);  // Версия приложения
            Log.e("Reg", "Код: " + pInfo.versionCode + ", Ревиз: " + pInfo.baseRevisionCode); // Код и ревизия
            Log.e("Reg", Build.BRAND + "_" + Build.MODEL);  // Бренд и модель (Код для регистрации)
            Log.e("Reg", UUID.randomUUID().toString());  //
            Log.e("Reg", Android_id.substring(0, Android_id.length() - 2));  //
        } catch (Exception e) {

        }


    }

    protected void Calendate_New() {
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

    protected void Anim_Start() {
        final Animation animationFlipIn = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);

        final Animation animationFlipOut = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);

        flipper.setInAnimation(animationFlipIn);
        flipper.setOutAnimation(animationFlipOut);
    }


    protected Pair Exclusive_Load(String w_uid, String w_mobile) {
        String list_brends = "";
        String list_subbrends = "";
        if (w_uid.equals("F3D1057E-1E77-4AB6-84B8-B80C31089931") | w_mobile.equals("8412b9d6c3af1")) {
            if (w_uid.equals("F3D1057E-1E77-4AB6-84B8-B80C31089931") | w_mobile.equals("8412b9d6c3af1")) {
                new_kod_mobile = "8412b9d6c3af1 ";
                Log.e("BREBDS_LIST ", "DATA= " + new_kod_mobile);
            }
            Log.e("BREBDS_LIST ", "DATA= " + w_uid + "" + w_mobile);
        }

        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase("sunbell_const_db.db3", MODE_PRIVATE, null);
            String query = "SELECT name, uid_name, user_brends, user_subbrends FROM const_agents_brends\n" +
                    "WHERE uid_name = '" + w_uid + "'";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
          // Log.e("BREBDS_LIST ", "LIST= " + cursor.getCount());
            if (cursor.getCount() > 0) {
                while (cursor.isAfterLast() == false) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));              // столбец с именем агента
                    String uid = cursor.getString(cursor.getColumnIndexOrThrow("uid_name"));           // столбец с uid-кодом
                    list_brends = cursor.getString(cursor.getColumnIndexOrThrow("user_brends"));   // столбец список
                    list_subbrends = cursor.getString(cursor.getColumnIndexOrThrow("user_subbrends"));   // столбец список
                    //  vis_brends = list_brends;
                    cursor.moveToNext();
                }
            } else {
                //  vis_brends = "/ALL/";
                list_brends = "/ALL/";
                list_subbrends = "/ALL/";
            }
            cursor.close();
            db.close();
        } catch (Exception e) {

        }


       /* switch (w_uid) {
            case "05C91B3D-BD39-4E00-80A9-E3CBEB669317":
                vis_brends = "/BL,DC,VR,PD,YS/";
                break; //Безменова Наталия Петровна
            case "C611B483-547F-41B2-9DF0-050C34682012":
                vis_brends = "/BL,DC,VR,PD,YS/";
                break; // Мухамеджанова Елена Арслановна
            case "A8BA1F48-C7E1-497B-B74A-D86426684712":
                vis_brends = "/BL,DC,VR,PD,YS/";
                break; //  Керкин Роман Максимович

            case "54014B5F-3FDD-4AFB-BD8C-E72289F700D5":
                vis_brends = "/OT,CS,LC,PC,PS,SM,DR,KD,NT,IE,TG,SR,NS,PF,SV,TR,SH,BG,CT,HU/";
                break; //  Бударная Валентина (маршрут-401)
            case "5798C798-014A-497F-9224-1545C5721D76":
                vis_brends = "/OT,CS,LC,PC,PS,SM,DR,KD,NT,IE,TG,SR,NS,PF,SV,TR,SH,BG,CT,HU/";
                break; //  Бударная Валентина (маршрут-402)
            case "B6250C08-3E7E-48E1-A7A2-F5FCEBA30C4F":
                vis_brends = "/OT,CS,LC,PC,PS,SM,DR,KD,NT,IE,TG,SR,NS,PF,SV,TR,SH,BG,CT,HU/";
                break; //  Бударная Валентина (маршрут-403)

            case "AACE3E62-3E1B-4C04-9814-43FC559B1D64":
                vis_brends = "/OT,CS,LC,PC,PS,SM,DR,KD,NT,IE,TG,SR,NS,PF,SV,TR,SH,BG,CT,HU/";
                break; //  Субдилер (регион: Ак-суу)


            default:
                vis_brends = "/ALL/";
                break; // Для остальных
        }*/


        // return list_brends;
        return new Pair(list_brends, list_subbrends);
    }


}



