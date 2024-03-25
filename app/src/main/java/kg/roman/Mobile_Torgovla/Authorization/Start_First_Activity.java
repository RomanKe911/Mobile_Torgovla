package kg.roman.Mobile_Torgovla.Authorization;

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
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_Login;
import kg.roman.Mobile_Torgovla.ClassNew.Pair;
import kg.roman.Mobile_Torgovla.ImagePack.ImagePack_R_Adapter;
import kg.roman.Mobile_Torgovla.ImagePack.MyViewModel;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Login;
import kg.roman.Mobile_Torgovla.MT_BackUp.Async_ViewModel_BackUp_toFTP;
import kg.roman.Mobile_Torgovla.MT_FTP.PreferencesWrite;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.Setting.Setting_Start;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Users;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Users_ResourceParser;
import kg.roman.Mobile_Torgovla.databinding.MtSplashBinding;

public class Start_First_Activity extends AppCompatActivity {

    ////// Обновление структуры 31.01.2024
    private MtSplashBinding binding;
    PackageInfo pInfo = null;
    String logeTAG = "StartFirstActivity";
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplication().getBaseContext();
        binding = MtSplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.icon_logotoolbar);
        getSupportActionBar().setTitle("Мобильная торговля");
        getSupportActionBar().setSubtitle(getActionBarSubTitle());

        InternetConnect();             // проврка подключения к интернету и файлов
        Permissino_Up();               // получение разрешения на создание файлов
        CreateNewFiles_and_Direct();   // Функция создания каталогов и папок
        Permision_Write_Constant();    // Сохранение постоянных переменных в память настроек


      //  PreferenceAdmin();
    }

    ////// Настройки меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getSupportActionBar().hide();
        getMenuInflater().inflate(R.menu.main, menu);
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
            try {
                Intent intent_prefActivity = new Intent(context, Setting_Start.class);
                startActivity(intent_prefActivity);
                finish();
            } catch (Exception e) {
                Toast.makeText(context, "Ошибка входа в настройки", Toast.LENGTH_SHORT).show();
                Log.e(this.getLocalClassName(), "Ошибка входа в настройки");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /// функция для подстроки ActionBar
    protected String getActionBarSubTitle() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            Calendar calendar = Calendar.getInstance();
            stringBuilder.append("ver.")
                    .append(pInfo.versionName)
                    .append("; code.")
                    .append(pInfo.versionCode)
                    .append("; 2018-")
                    .append(calendar.get(Calendar.YEAR));

        } catch (Exception e) {
            Log.e(logeTAG, "Ошибка загрузки ToolBar");
        }

        return stringBuilder.toString();
    }

    /// Функция создания каталогов и папок
    protected void CreateNewFiles_and_Direct() {
        String[] mass_distr = getResources().getStringArray(R.array.mass_distr_files);
        File file_put = new File("/sdcard/" + File.separator + "Price");
        if (!file_put.exists()) {
            file_put.mkdir();
            Toast.makeText(this, "Создана папка каталога...", Toast.LENGTH_LONG).show();
        }
        for (int i = 0; i < mass_distr.length; i++) {
            File file_reg = new File("/sdcard/Price/" + File.separator + mass_distr[i]);
            if (!file_reg.exists()) {
                file_reg.mkdir();
                Toast.makeText(this, "файл: " + mass_distr[i] + " успешно создан", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Проверка на сущ. файлов MTW_Users
    protected boolean Exists_File_XML() {
        String file_xml = getDatabasePath("MTW_In_Users.xml").getAbsolutePath(); // путь к databases
        File file_put = new File(file_xml);
        if (file_put.exists()) {
            //file_put.mkdir();
            binding.imgDataXml.setImageResource(R.drawable.image_start_xml);
            Log.e(logeTAG, "XML: файл Users есть в базе");
            return true;
        } else {
            binding.imgDataXml.setImageResource(R.mipmap.ic_error);
            Log.e(logeTAG, "XML: файл Users нет в базе");
            return false;
        }
    }

    // Проверка на сущ. файлов SQL
    protected boolean Exists_File_DB3() {
        String file_db3 = getDatabasePath("sunbell_const_db.db3").getAbsolutePath(); // путь к databases
        File file_put = new File(file_db3);
        if (file_put.exists()) {
            //file_put.mkdir();
            binding.imgDataSql.setImageResource(R.drawable.image_start_sql);
            Log.e(logeTAG, "SQLite: есть база данных");
            return true;
        } else {
            binding.imgDataSql.setImageResource(R.mipmap.ic_error);
            Log.e(logeTAG, "SQLite: есть база данных");
            return false;
        }
    }

    // Проверка на подулючение к интернету и проверка всех параметров перед входом
    protected void InternetConnect() {
        Async_ViewModel_MTWUsers model = new ViewModelProvider(this).get(Async_ViewModel_MTWUsers.class);
        model.getStatusInternet().observe(this, statusInternet ->
        {
            boolean b_xml = false, b_sql = false, b_net = false;
            StringBuilder stringBuilderLoge = new StringBuilder();

            if (statusInternet.booleanValue() == true) {
                b_net = true;
                binding.imgDataInternet.setImageResource(R.drawable.image_start_internet);
            } else {
                binding.imgDataInternet.setImageResource(R.mipmap.ic_error);
                Log.e(logeTAG, "Internet: Нет подключения к сети интернет!");
                stringBuilderLoge.append("Internet: Нет подключения к сети интернет!").append("\n");
            }
            if (Exists_File_XML())
                b_xml = true;
            else {
                stringBuilderLoge.append("XML: в базе нет файлов").append("\n");
                binding.imgDataXml.setImageResource(R.mipmap.ic_error);
            }


            if (Exists_File_DB3())
                b_sql = true;
            else {
                stringBuilderLoge.append("SQL: в базе нет файлов").append("\n");
                binding.imgDataSql.setImageResource(R.mipmap.ic_error);
            }

            if (b_xml && b_sql && b_net) {
                binding.tvwStartLog.setVisibility(View.GONE);
                binding.tvwStartTitle.setVisibility(View.GONE);
                model.getLoadingStatus().observe(this, status ->
                {
                    if (status.booleanValue() == true) {
                        Intent intent = new Intent(this, Start_Second_Activity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                model.execute();
            } else binding.tvwStartLog.setText(stringBuilderLoge);
        });
        model.executeInternet();


    }

    // Получение разрешения на создание файловой системы
    private void Permissino_Up() {
               /* Для создания файлов на версиях Android N нужно получить разрешеник:
                В Android M permissions нужно также проверять во время выполнения.
                Поэтому нужно сначала посмотреть , получили ли мы разрешение от пользователя.
                Если да - выполняем работу; нет - просим разрешение:*/
        Integer NUMBER_OF_REQUEST = 23401;
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



    /////  Запись данных в настройки
    private void Permision_Write_Constant()
    {
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        editor = mSettings.edit();
        editor.remove("setting_TY_TypeRelise");
        editor.putInt("setting_TY_TypeRelise", 0);

        editor.putString("PEREM_DB3_CONST", "sunbell_const_db.db3");      // База данных работа с константами
        editor.putString("PEREM_DB3_BASE", "sunbell_base_db.db3");        // База данных работа с товаром
        editor.putString("PEREM_DB3_BaseRN", "sunbell_rn_db.db3");        // База данных работа с товаром
        editor.putString("PEREM_ANDROID_ID_ADMIN", "d5781f21963ff5");     // Ключ админа
        editor.commit();

        String Android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        editor.putString("PEREM_ANDROID_ID", Android_id.substring(0, Android_id.length() - 2));   //Универсальный код для входа

        PreferencesWrite preferencesWrite = new PreferencesWrite(context);
        Log.e(logeTAG, "Код Admin: "+preferencesWrite.PEREM_ANDROID_ID_ADMIN);
        Log.e(logeTAG, "Код User: "+preferencesWrite.PEREM_ANDROID_ID);

       // editor.putString("PEREM_ANDROID_ID_ADMIN", "d5781f21963ff5");                             //Универсальный код для Admin
       // editor.putString("PEREM_ISNAME_SPINNER", "AITORIZ_ACTIVITY");                             //  разметка для Spinner
        editor.commit();
    }


    private void PreferenceAdmin() {
        try {
            PreferencesWrite preferencesWrite = new PreferencesWrite(getApplication().getBaseContext());
            SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_CONST, MODE_PRIVATE, null);
            String query = "SELECT * FROM const_agents\n" +
                    "WHERE uid_name = 'A8BA1F48-C7E1-497B-B74A-D86426684712';";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {

                editor = mSettings.edit();
                String kod_mobile = cursor.getString(cursor.getColumnIndexOrThrow("kod_mobile"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                editor.putString("PEREM_ANDROID_ID_ADMIN", kod_mobile);
                editor.commit();
            }
            cursor.close();
            db.close();
        }
        catch (Exception e)
        {
            Log.e(logeTAG, "Error: Ошибка данныз об администраторе");
        }

    }









    ////////////////////////////////////////////////////////////////////


    protected void New_File() {
        File file_put = new File("/sdcard/" + File.separator + "Price");
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
            File file_reg = new File("/sdcard/Price/" + File.separator + Distr);
            if (!file_reg.exists()) {
                file_reg.mkdir();
                Toast.makeText(this, "Файл успешно создан", Toast.LENGTH_LONG).show();
            }
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }  // Проверка создание папок для хранения XLS
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
                String file_db = context.getDatabasePath("MTW_In_Users.xml").getAbsolutePath(); // путь к databases
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
                    Cursor cursor = db.rawQuery(query_up, null);
                    ContentValues localContentValues = new ContentValues();
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

                Log.e(logeTAG, "Ошибка данных: не верная загрузка файла MTW_User.xml");
                // Toast.makeText(context_Activity, "Ошибка данных: не верная загрузка файла MTW_User.xml", Toast.LENGTH_SHORT).show();
            }
            TimeUnit.SECONDS.sleep(1);
            // pDialog.dismiss();
        }

    }  // Синхронизация файлов для всех складов
    // Константы для работы
    protected Pair Exclusive_Load(String w_uid, String w_mobile) {
        String list_brends = "";
        String list_subbrends = "";
        if (w_uid.equals("F3D1057E-1E77-4AB6-84B8-B80C31089931") | w_mobile.equals("8412b9d6c3af1")) {
            if (w_uid.equals("F3D1057E-1E77-4AB6-84B8-B80C31089931") | w_mobile.equals("8412b9d6c3af1")) {
                String new_kod_mobile = "8412b9d6c3af1 ";
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



