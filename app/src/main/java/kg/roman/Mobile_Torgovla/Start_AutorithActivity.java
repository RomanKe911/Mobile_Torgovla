package kg.roman.Mobile_Torgovla;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_Login;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Login;
import kg.roman.Mobile_Torgovla.Permission.PrefActivity_Splash;
import kg.roman.Mobile_Torgovla.Permission.PrefActivity_Splash_New;
import kg.roman.Mobile_Torgovla.Work_Journal.WJ_Global_Activity;
import kg.roman.Mobile_Torgovla.TEST.WJ_Ros_Torg;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_File_Const;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_File_Const_ResourceParser;

public class Start_AutorithActivity extends AppCompatActivity {
    public ArrayList<ListAdapterSimple_Login> login_aut = new ArrayList<ListAdapterSimple_Login>();
    public ListAdapterAde_Login adapterPriceClients;
    public SharedPreferences sp;
    public SharedPreferences.Editor ed;

    public Spinner spinner;
    public ImageView imageView, imageView_logo;
    public EditText editText_password;
    public Button button_aut;
    public TextView tvw_UID, tvw_region, tvw_type, tvw_cena, tvw_sklad, tvw_region_put;

    public String text_password, selected_name_image;
    public String[][] array_logins;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now;
    public String COLUMN_NAME = "name";
    public String COLUMN_CENA = "cena";
    public String table_name;
    public String[] mass_agents;
    public String selected, query, brends_visible;

    private Handler mHandler = new Handler();
    public Context context, context_Activity;
    public int i = 1, j = 1, k = 1, kol;
    public String TAG = "";

    public String PEREM_Agent, PEREM_Type, PEREM_DST_ContrAg, UID_Agent, myDB;
    public String PEREM_DISTR, Image_User, PEREM_PUT_UPDATE_FTP;
    public String PEREM_ANDROID_ID_ADMIN, PEREM_ANDROID_ID, PEREM_DB3_CONST,
            PEREM_DB3_BASE, PEREM_DB3_RN, PEREM_ISNAME_SPINNER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt_avtosizac);
        context_Activity = Start_AutorithActivity.this;
        MultiDex.install(this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon_image);
        getSupportActionBar().setTitle("Мобильная торговля");
        getSupportActionBar().setSubtitle("Авторизация");
        TAG = this.getLocalClassName();

        editText_password = findViewById(R.id.editText);
        editText_password.requestFocus();
        editText_password.setFocusable(true);
        spinner = findViewById(R.id.spinner);
        button_aut = findViewById(R.id.btn_aut_next);
        tvw_region = findViewById(R.id.tvw_aut_marshrut);
        tvw_type = findViewById(R.id.tvw_aut_user);
        tvw_UID = findViewById(R.id.tvw_aut_uid);
        tvw_cena = findViewById(R.id.tvw_aut_cena);
        tvw_sklad = findViewById(R.id.tvw_aut_sklad);
        tvw_region_put = findViewById(R.id.tvw_aut_put_region);
        imageView = (ImageView) findViewById(R.id.imageViewVISIBLE_AUT);
        imageView_logo = (ImageView) findViewById(R.id.imageView_aut_logo);
        text_password = "8888";


        try {
            Cal_Date();
            Constanta_Read();
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка данных констант!", Toast.LENGTH_SHORT).show();
            Log.e(this.getLocalClassName(), "Ошибка данных констант!");
        }

        try {
            login_aut.clear();
            PEREM_ISNAME_SPINNER = sp.getString("PEREM_ISNAME_SPINNER", "0");
            DB_Spinner_Write(PEREM_ANDROID_ID);  // Загрузка данных из базы SQlite
            adapterPriceClients = new ListAdapterAde_Login(Start_AutorithActivity.this, login_aut);
            adapterPriceClients.notifyDataSetChanged();
            spinner.setAdapter(adapterPriceClients);
            Log.e(this.getLocalClassName(), "PEREM_ISNAME_SPINNER");
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка данных Spinner", Toast.LENGTH_SHORT).show();
            Log.e(this.getLocalClassName(), "Ошибка данных Spinner");
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                // text_password = "";
                editText_password.setText("");
                editText_password.clearFocus();
                editText_password.requestFocus();
                editText_password.setFocusable(true);
                showInputMethod();
                selected = ((TextView) itemSelected.findViewById(R.id.textView_login)).getText().toString();
                selected_name_image = ((TextView) itemSelected.findViewById(R.id.tvw_sp_name_image)).getText().toString();
                DB_Spinner_Select(selected);

                try {

                } catch (Exception e) {
                    Toast.makeText(context_Activity, "Ошибка выбора Spinner", Toast.LENGTH_SHORT).show();
                    Log.e(Start_AutorithActivity.this.getLocalClassName(), "Ошибка выбора Spinner");
                    hideInputMethod();
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        try {
            kol = 0;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (kol) {
                        case 0:
                            kol = 1;
                            imageView.setImageResource(R.drawable.user_preview_down);
                            editText_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            editText_password.setSelection(editText_password.getText().length());
                            break;
                        case 1:
                            kol = 0;
                            imageView.setImageResource(R.drawable.user_preview_up);
                            editText_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            editText_password.setSelection(editText_password.getText().length());
                            break;
                    }
                }
            });
        } catch (Exception e) {
            Log.e(this.getLocalClassName(), "Ошибка загрузки данных!");
        }


        editText_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                /*if (editText_password.getText().toString().equals(text_password)) {
                    button_aut.setBackground(getResources().getDrawable(R.drawable.wj_office_selector_button_right));

                } else
                    button_aut.setBackground(getResources().getDrawable(R.drawable.office_right_black));*/
            }
        });


        button_aut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    /*sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
                    String listValue_all = sp.getString("list_preference_ftp", "0");
                    Log.e(AutorithActivity.this.getLocalClassName(), listValue_all);
                    switch (listValue_all)
                    {
                        case "Каракол" : Log.e(AutorithActivity.this.getLocalClassName(), "MTW_KARAKOL");break;
                        case "Ош" : Log.e(AutorithActivity.this.getLocalClassName(), "MTW_ОШ");break;
                    }*/


                    if (editText_password.getText().toString().equals(text_password) & spinner.getCount() > 0) {
                        Toast.makeText(Start_AutorithActivity.this, PEREM_PUT_UPDATE_FTP, Toast.LENGTH_SHORT).show();
                        Log.e("FTP_date ", "FTP =" + PEREM_PUT_UPDATE_FTP);
                        if (!PEREM_PUT_UPDATE_FTP.equals("1")) {
                            Toast.makeText(Start_AutorithActivity.this, "Доступ открыт", Toast.LENGTH_SHORT).show();
                            hideInputMethod(); // Тип отображение данных
                            Intent intent = new Intent(context_Activity, WJ_Global_Activity.class);
                            startActivity(intent);
                            finish();
                            Log.e("FTP_date ", "FTP =" + PEREM_PUT_UPDATE_FTP);
                        } else
                            Toast.makeText(context_Activity, "выберите путь к данным на сервере", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context_Activity, "нет активных пользователей!", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(context_Activity, "Ошибка, вход не выполнен", Toast.LENGTH_SHORT).show();
                    Log.e(Start_AutorithActivity.this.getLocalClassName(), "Ошибка, вход не выполнен");
                }

            }
        });

        // Open_Files_MTW_XML(PEREM_Android_ID); // Загрузка данных из файла XML
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getSupportActionBar().hide();
        getMenuInflater().inflate(R.menu.menu_autoris, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        try {
            switch (id) {
                case R.id.menu_new_agent: {
                    Intent intent = new Intent(context_Activity, Start_Registracia_New.class);
                    startActivity(intent);
                    finish();

                }
                break;
                case R.id.menu_preferenc: {
                    Intent intent_prefActivity = new Intent(context_Activity, PrefActivity_Splash.class);
                    startActivity(intent_prefActivity);

                }
                break;
                case R.id.menu_preferenc_new: {
                    Intent intent_prefActivity = new Intent(context_Activity, PrefActivity_Splash_New.class);
                    startActivity(intent_prefActivity);
                }
                break;
            }
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка вход в настройки", Toast.LENGTH_SHORT).show();
            Log.e(this.getLocalClassName(), "Ошибка вход в настройки");
        }



      /*  if (id == R.id.action_settings) {



            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            Log.e("LOG ", "onResume");
            Constanta_Read();
            login_aut.clear();
            DB_Spinner_Write(PEREM_ANDROID_ID);  // Загрузка данных из базы SQlite
            adapterPriceClients = new ListAdapterAde_Login(Start_AutorithActivity.this, login_aut);
            adapterPriceClients.notifyDataSetChanged();
            spinner.setAdapter(adapterPriceClients);
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка данных Spinner", Toast.LENGTH_SHORT).show();
            Log.e(this.getLocalClassName(), "Ошибка данных Spinner");
        }

    }

    // Загрузка данных в Spinner
    protected void DB_Spinner_Write(String kod_android) {
        SQLiteDatabase db_insert = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
        if (kod_android.equals(PEREM_ANDROID_ID_ADMIN)) {
            query = "SELECT * FROM const_agents WHERE kod_mobile > 0";
        } else query = "SELECT * FROM const_agents WHERE kod_mobile = '" + kod_android + "'";

        final Cursor cursor = db_insert.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String uid = cursor.getString(cursor.getColumnIndexOrThrow("uid_name"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String region = cursor.getString(cursor.getColumnIndexOrThrow("region"));
            String kod_mobile = cursor.getString(cursor.getColumnIndexOrThrow("kod_mobile"));
            String type_user = cursor.getString(cursor.getColumnIndexOrThrow("type_user"));
            String cena = cursor.getString(cursor.getColumnIndexOrThrow("cena"));
            String sklad = cursor.getString(cursor.getColumnIndexOrThrow("skald"));
            Image_Type_User(type_user);

            if (kod_android.equals(kod_mobile) | kod_android.equals(PEREM_ANDROID_ID_ADMIN)) {
                /*Log.e("ID", kod_android);
                Log.e("name", name);*/

                login_aut.add(new ListAdapterSimple_Login(name, Image_User));
                //mageView_logo.setImageResource(getD);
                //tvw_UID.setText(uid);
                tvw_region.setText(region);
                tvw_type.setText(type_user);
                tvw_cena.setText(cena);
                String[] mass_name_region, mass_name_region_put;
                mass_name_region = getResources().getStringArray(R.array.mass_Region_Put_Name);
                mass_name_region_put = getResources().getStringArray(R.array.mass_Region_Put_Value);
                for (int i = 0; i < mass_name_region_put.length; i++) {
                    if (PEREM_PUT_UPDATE_FTP.equals(mass_name_region_put[i])) {
                        tvw_region_put.setText(mass_name_region[i]);
                    } //else tvw_region_put.setText("Нету данных");
                }
                tvw_sklad.setText(sklad);


                cursor.moveToNext();
            } else {
                Log.e("Error ", "Нету данных!");
                cursor.moveToNext();
            }
        }


        cursor.close();
        db_insert.close();
    }

    // При выборе данных в Spinner
    protected void DB_Spinner_Select(String spinner_select) {
        try {
            SQLiteDatabase db_insert = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
            String query = "SELECT * FROM const_agents WHERE name = '" + spinner_select + "'";
            final Cursor cursor = db_insert.rawQuery(query, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String uid = cursor.getString(cursor.getColumnIndexOrThrow("uid_name"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String region = cursor.getString(cursor.getColumnIndexOrThrow("region"));
                String uid_region = cursor.getString(cursor.getColumnIndexOrThrow("uid_region"));
                String cena = cursor.getString(cursor.getColumnIndexOrThrow("cena"));
                String sklad = cursor.getString(cursor.getColumnIndexOrThrow("skald"));
                String uid_sklad = cursor.getString(cursor.getColumnIndexOrThrow("uid_sklad"));
                String kod_mobile = cursor.getString(cursor.getColumnIndexOrThrow("kod_mobile"));
                String type_real = cursor.getString(cursor.getColumnIndexOrThrow("type_real"));
                String type_user = cursor.getString(cursor.getColumnIndexOrThrow("type_user"));
                String vis_category = cursor.getString(cursor.getColumnIndexOrThrow("user_brends"));

                Image_Type_User(type_user);
                tvw_UID.setText(uid);
                tvw_region.setText(region);
                tvw_type.setText(type_user);
                tvw_cena.setText(cena);
                tvw_sklad.setText(sklad);
                Constanta_Write(uid, name, kod_mobile, region, uid_region, cena, sklad, uid_sklad, type_real, type_user, vis_category);
                Log.e("Доступные бренды: ", vis_category);
                //Toast.makeText(context_Activity, "Доступные бренды:" + vis_category, Toast.LENGTH_SHORT).show();
                cursor.moveToNext();
            }
            cursor.close();
            db_insert.close();
        } catch (Exception e) {
            Log.e(TAG, "Ошибка данных, списка доступных брендов!");
            Toast.makeText(context_Activity, "Ошибка данных, списка доступных брендов!", Toast.LENGTH_SHORT).show();
        }
    }

    // Картинки для иконок пользователя
    protected void Image_Type_User(String type) {
        switch (type) {
            case "Агент":
                Image_User = "img_agt";
                break;
            case "Экспедитор":
                Image_User = "img_expd";
                break;
            case "Зав.складом":
                Image_User = "img_skl";
                break;
            case "Водитель":
                Image_User = "img_driver";
                break;
            case "Оператор":
                Image_User = "img_opert";
                break;
            case "Бухгалтер":
                Image_User = "img_money";
                break;
            default:
                Image_User = "img_null";
                break;
        }

        Uri imagePath_phone = Uri.parse("android.resource://kg.roman.Mobile_Torgovla/drawable/" + Image_User);
        imageView_logo.setImageURI(imagePath_phone);
        Picasso.get() //передаем контекст приложения
                .load(imagePath_phone)    //Путь к файлу
                .error(R.drawable.logo_sunbell)
                .into(imageView_logo);
    }

    // Загрузка даты и время
    protected void Cal_Date() {
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

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY: {
                Log.e("AUTOR_WORK_DAY:", "Понедельник");
                PEREM_DISTR = "01_WDay";
            }
            break;
            case Calendar.TUESDAY: {
                Log.e("AUTOR_WORK_DAY:", "Вторник");
                PEREM_DISTR = "02_WDay";
            }
            break;
            case Calendar.WEDNESDAY: {
                Log.e("AUTOR_WORK_DAY::", "Среда");
                PEREM_DISTR = "03_WDay";
            }
            break;
            case Calendar.THURSDAY: {
                Log.e("AUTOR_WORK_DAY::", "Четверг");
                PEREM_DISTR = "04_WDay";
            }
            break;
            case Calendar.FRIDAY: {
                Log.e("AUTOR_WORK_DAY::", "Пятница");
                PEREM_DISTR = "05_WDay";
            }
            break;
            case Calendar.SATURDAY: {
                Log.e("AUTOR_WORK_DAY::", "Суббота");
                PEREM_DISTR = "06_WDay";
            }
            break;
            case Calendar.SUNDAY: {
                Log.e("AUTOR_WORK_DAY::", "Воскресенье");
                PEREM_DISTR = "07_WDay";
            }
            break;
            default:
                break;
        }


        //  Log.e("WJ_FormaL2:", "!DataEnd:" + dateString2);

    }

    // Константы для чтения
    protected void Constanta_Read() {
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        PEREM_ANDROID_ID_ADMIN = sp.getString("PEREM_ANDROID_ID_ADMIN", "0");
        PEREM_ANDROID_ID = sp.getString("PEREM_ANDROID_ID", "0");
        PEREM_DB3_CONST = sp.getString("PEREM_DB3_CONST", "0");
        PEREM_DB3_BASE = sp.getString("PEREM_DB3_BASE", "0");
        PEREM_DB3_RN = sp.getString("PEREM_DB3_RN", "0");
        PEREM_PUT_UPDATE_FTP = sp.getString("list_preference_ftp", "0");
    }

    // Константы для записи
    protected void Constanta_Write(String uid, String name, String kod_mobile,
                                   String region, String uid_region, String cena, String sklad,
                                   String uid_sklad, String type_real, String type_user, String vis_brends) {
        ed = sp.edit();
        ed.putString("PEREM_AG_UID", uid);                                          // передача кода агента (A8BA1F48-C7E1-497B-B74A-D86426684712)
        ed.putString("PEREM_AG_NAME", name);                                        // передача кода агента (Имя пользователя)
        ed.putString("PEREM_AG_REGION", region);                                    // маршруты для привязки к контагентам
        ed.putString("PEREM_AG_UID_REGION", uid_region.trim());                     // uid маршруты для привязки к контагентам
        ed.putString("PEREM_AG_CENA", cena);                                        // цены для агентов
        ed.putString("PEREM_AG_SKLAD", sklad);                                      // склады для агентов
        ed.putString("PEREM_AG_UID_SKLAD", uid_sklad.trim());                       // UID склады для агентов
        ed.putString("PEREM_AG_TYPE_REAL", type_real);                              // выбор типа торгового агента 1-OSDO или 2-PRES
        ed.putString("PEREM_AG_TYPE_USER", type_user);                              // тип учетной записи агент или экспедитор
        ed.putString("PEREM_WORK_DISTR", PEREM_DISTR);                              // имя папки с данными (01_WDay)
        ed.putString("PEREM_KOD_MOBILE", kod_mobile);                               // имя папки с данными (01_WDay)
        ed.putString("PEREM_KOD_UID_KODRN", uid.substring(9, uid.length() - 23));    // уникальный код для накладной
        ed.putString("PEREM_KOD_BRENDS_VISIBLE", vis_brends);                         // список брендов для загрузки

       /* ed.putString("PEREM_DIALOG_UID", "");  // имя папки с данными (01_WDay)
        ed.putString("PEREM_DIALOG_DATA_START", "");  // имя папки с данными (01_WDay)
        ed.putString("PEREM_DIALOG_DATA_END", "");  // имя папки с данными (01_WDay)*/

        ed.commit();


       /* Log.e("PEREM_AG_UID", uid);
        Log.e("PEREM_AG_NAME", name);
        Log.e("PEREM_AG_REGION", region);
        Log.e("PEREM_AG_UID_REGION", uid_region);
        Log.e("PEREM_AG_CENA", cena);
        Log.e("PEREM_AG_SKLAD", sklad);
        Log.e("PEREM_AG_UID_SKLAD", uid_sklad);
        Log.e("PEREM_AG_TYPE_REAL", type_real);
        Log.e("PEREM_AG_TYPE_USER", type_user);
        Log.e("PEREM_WORK_DISTR", PEREM_DISTR);
        Log.e("PEREM_KOD_MOBILE", kod_mobile);*/
    }


    protected void Type() {
        switch (tvw_type.getText().toString()) {
            case "admin": {
                hideInputMethod(); // Тип отображение данных
                Intent intent = new Intent(context_Activity, WJ_Global_Activity.class);
                startActivity(intent);
                finish();
            }
            break;
            case "Агент": {
                hideInputMethod();// Тип отображение данных
                Intent intent = new Intent(context_Activity, WJ_Global_Activity.class);
                startActivity(intent);
                finish();
            }
            break;
            case "driver": {
                hideInputMethod();// Тип отображение данных
                Intent intent = new Intent(context_Activity, WJ_Global_Activity.class);
                startActivity(intent);
                finish();
            }
            break;
            case "rosn": {
                hideInputMethod();// Тип отображение данных
                Intent intent = new Intent(context_Activity, WJ_Ros_Torg.class);
                startActivity(intent);
                finish();
            }
            break;
            default:
                break;
        }
    }

    protected void DB_Agents() {
        SQLiteDatabase db_insert = getBaseContext().openOrCreateDatabase("suncape_const_db.db3", MODE_PRIVATE, null);
        String query = "SELECT const_logins.UID, const_logins.name, const_logins.s_name FROM const_logins";
        final Cursor cursor = db_insert.rawQuery(query, null);
        cursor.moveToFirst();
        mass_agents = new String[10];
        while (cursor.isAfterLast() == false) {
            String UID = cursor.getString(cursor.getColumnIndexOrThrow("UID"));
            mass_agents[cursor.getPosition()] = UID;
            cursor.moveToNext();
        }
        cursor.close();
        db_insert.close();
    }

    protected void ListAdapet_Log() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_const_db.db3", MODE_PRIVATE, null);
        String query = "SELECT const_logins._id, const_logins.name, const_logins.s_name, " +
                "const_logins.password, const_logins.region,  const_logins.telefon, " +
                "const_logins.image, const_logins.kod_mobile, const_logins.type, const_logins.UID, " +
                "const_logins.Name_Distr \n" +
                "FROM const_logins";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        array_logins = new String[100][100];
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String s_name = cursor.getString(cursor.getColumnIndexOrThrow("s_name"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            String region = cursor.getString(cursor.getColumnIndexOrThrow("region"));
            String telefon = cursor.getString(cursor.getColumnIndexOrThrow("telefon"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            String kod_mobile = cursor.getString(cursor.getColumnIndexOrThrow("kod_mobile"));
            String UID = cursor.getString(cursor.getColumnIndexOrThrow("UID"));
            String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
            //  imageView_Login.setImageResource(R.mipmap.ic_error);
            array_logins[i][j] = s_name + " " + name;
            j++;
            array_logins[i][j] = password;
            j++;
            array_logins[i][j] = region;
            array_logins[i][7] = type;
            array_logins[i][6] = kod_mobile;
            if (!UID.isEmpty()) {
                array_logins[i][8] = UID;
                Log.e(this.getLocalClassName(), "Есть данные ");
            } else Log.e(this.getLocalClassName(), "Нет данных");

            int resID = getResources().getIdentifier("user_man", "drawable", getPackageName());
            login_aut.add(new ListAdapterSimple_Login(s_name + " " + name, image));
            cursor.moveToNext();
            i++;
            j = 1;
        }
        cursor.close();
        db.close();
    }

    protected void Loading_Agent_Name() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
        String query = "SELECT *FROM const_logins";

        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String Agent_Kod = cursor.getString(cursor.getColumnIndexOrThrow("UID"));
            String Name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String Name_S = cursor.getString(cursor.getColumnIndexOrThrow("s_name"));
            String Type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            String Distr = cursor.getString(cursor.getColumnIndexOrThrow("Name_Distr"));
            String DST_CONTRAG = cursor.getString(cursor.getColumnIndexOrThrow("region"));
            if (UID_Agent.equals(Agent_Kod)) {
                PEREM_Agent = Name_S + "_" + Name;
                PEREM_Type = Type;
                myDB = Distr;
                //PEREM_Distr = Distr;
                PEREM_DST_ContrAg = DST_CONTRAG;
            }
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

    }

    protected void Open_Files_MTW_XML(String kod_android) {
        try {
            String file_db = Start_AutorithActivity.this.getDatabasePath(".xml").getAbsolutePath(); // путь к databases
            InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = parserFactory.newPullParser();
            xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xpp.setInput(istream, null);

            MTW_File_Const_ResourceParser parser = new MTW_File_Const_ResourceParser();
            int eventType = xpp.getEventType();
            if (parser.parse(xpp)) {
                for (MTW_File_Const price : parser.getXml_const()) {
                    if (kod_android.equals(price.getKod_mobile())) {
                        Log.e("MTW_Const ", price.getUid_user());
                        Log.e("MTW_Const ", price.getName());
                        Log.e("MTW_Const ", price.getPass());
                        Log.e("MTW_Const ", price.getRegion());
                        Log.e("MTW_Const ", price.getUid_region());
                        Log.e("MTW_Const ", price.getCena());
                        Log.e("MTW_Const ", price.getSklad());
                        Log.e("MTW_Const ", price.getKod_mobile());
                        Log.e("MTW_Const ", price.getType_real());
                        Log.e("MTW_Const ", price.getType_user());
                    }
                }
            }
        } catch (Exception e) {

            Log.e("Err..", "rrr");
        }
    }

    /**
     * прячем программную клавиатуру
     */
    protected void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText_password.getWindowToken(), 0);
        }
    }

    /**
     * показываем программную клавиатуру
     */
    protected void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private Runnable mShowInputMethodTask = new Runnable() {
        public void run() {
            // showInputMethodForQuery();
        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // если окно в фокусе, то ждем еще немного и показываем клавиатуру
            mHandler.postDelayed(mShowInputMethodTask, 0);
        }
    }


    public static String toTranslit(String src) {
        String[] f = {"А", "Б", "В", "Г", "Д", "Е", "Ё", "Ж", "З", "И", "Й", "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф", "Х", "Ч", "Ц", "Ш", "Щ", "Э", "Ю", "Я", "Ы", "Ъ", "Ь", "а", "б", "в", "г", "д", "е", "ё", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п", "р", "с", "т", "у", "ф", "х", "ч", "ц", "ш", "щ", "э", "ю", "я", "ы", "ъ", "ь"};
        String[] t = {"A", "B", "V", "G", "D", "E", "Jo", "Zh", "Z", "I", "J", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ch", "C", "Sh", "Csh", "E", "Ju", "Ja", "Y", "`", "'", "a", "b", "v", "g", "d", "e", "jo", "zh", "z", "i", "j", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ch", "c", "sh", "csh", "e", "ju", "ja", "y", "`", "'"};

        String res = "";

        for (int i = 0; i < src.length(); ++i) {
            String add = src.substring(i, i + 1);
            for (int j = 0; j < f.length; j++) {
                if (f[j].equals(add)) {
                    add = t[j];
                    break;
                }
            }
            res += add;
        }

        return res;
    }


}