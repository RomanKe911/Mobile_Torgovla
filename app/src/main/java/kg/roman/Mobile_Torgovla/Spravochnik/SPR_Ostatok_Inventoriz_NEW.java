package kg.roman.Mobile_Torgovla.Spravochnik;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_Login;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Login;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Ostatok_Invert;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Otchet_Ostatok;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Ostatok_Invert;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Otchet_Ostatok;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.Work_Journal.WJ_Forma_Zakaza_L2;

public class SPR_Ostatok_Inventoriz_NEW extends AppCompatActivity {


    public ArrayList<ListAdapterSimple_Otchet_Ostatok> adapter_listview = new ArrayList<ListAdapterSimple_Otchet_Ostatok>();
    public ListAdapterAde_Otchet_Ostatok adapterPriceClients;

    public ArrayList<ListAdapterSimple_Login> add_spinner_sklad = new ArrayList<ListAdapterSimple_Login>();
    public ListAdapterAde_Login adapterPriceClients_spinner_sklad;

    public ArrayList<ListAdapterSimple_Login> add_spinner_brends = new ArrayList<ListAdapterSimple_Login>();
    public ListAdapterAde_Login adapterPriceClients_spinner_brends;

    public ArrayList<ListAdapterSimple_Login> add_spinner_group = new ArrayList<ListAdapterSimple_Login>();
    public ListAdapterAde_Login adapterPriceClients_spinner_group;

    public Context context_Activity;
    public SharedPreferences sp;
    public SharedPreferences.Editor ed;
    public String PEREM_FTP_SERV, PEREM_FTP_LOGIN, PEREM_FTP_PASS, PEREM_FTP_DISTR_XML, PEREM_FTP_DISTR_db3,
            PEREM_IMAGE_PUT_SDCARD, PEREM_IMAGE_PUT_PHONE;
    public String PEREM_MAIL_LOGIN, PEREM_MAIL_PASS, PEREM_MAIL_START, PEREM_MAIL_END,
            PEREM_DB3_CONST, PEREM_DB3_BASE, PEREM_DB3_RN, PEREM_ANDROID_ID_ADMIN, PEREM_ANDROID_ID;
    public String PEREM_AG_UID, PEREM_AG_NAME, PEREM_AG_REGION, PEREM_AG_UID_REGION, PEREM_AG_CENA,
            PEREM_AG_SKLAD, PEREM_AG_UID_SKLAD, PEREM_AG_TYPE_REAL, PEREM_AG_TYPE_USER,
            PEREM_WORK_DISTR, PEREM_KOD_MOBILE;

    public Spinner spinner_sklad, spinner_brends, spinner_group;
    public String this_data_display, this_data_db, this_vrema_db, this_rn_year, this_rn_month, this_rn_day;
    public String pref_otchet_invert, index_string;
    public String sp_sklad = "Все", sp_brends = "Все", sp_sub_brends = "Все", Log_Data, brends;
    public FloatingActionButton floatingActionButton;
    public String[] mass_sp_brends, mass_sp_subBrends;
    public String[][] mass_sp_sklad;
    public String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spr_otchet_inventariz);
        context_Activity = SPR_Ostatok_Inventoriz_NEW.this;
        Log.e("Class_Name: ", "|" + this.getLocalClassName());  // имя используемого класса

        spinner_sklad = findViewById(R.id.spinner_invert_sklad);
        spinner_brends = findViewById(R.id.spinner_invert_brends);
        spinner_group = findViewById(R.id.spinner_invert_subbrends);
        floatingActionButton = findViewById(R.id.floatingActionButton_go);

        Constanta_Read();
        Calendate_New();
        // Month_Prefix_Mass(15, "jun");

        ed = sp.edit();
        ed.putString("PEREM_ISNAME_SPINNER", "OSTATOK_ACTIVITY");  // запись переменой для отображения списка
        ed.commit();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon_image);
        getSupportActionBar().setTitle("Инвентаризация");
        getSupportActionBar().setSubtitle(" остатки на " + "5555");

        mass_sp_brends = new String[0];
        mass_sp_subBrends = new String[0];
        mass_sp_sklad = new String[0][0];

        SQLite_Work_Sklad();
        SQLite_Work_Brends();


        if (mass_sp_subBrends.length > 0) {
            SQLite_Work_Groups(mass_sp_subBrends[spinner_brends.getSelectedItemPosition()]);
        } else Log.e("group=", "NULL");

        spinner_brends.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = view.findViewById(R.id.textView_login);
                brends = tv.getText().toString();
                SQLite_Work_Groups(brends);
                Log.e("brends=", brends);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

       /* Log.e("sklad=", "" + spinner_sklad.getSelectedItemPosition());
        Log.e("sklad=", "" + mass_sp_sklad[spinner_sklad.getSelectedItemPosition()][0]);
        Log.e("brends=", "" + spinner_brends.getSelectedItemPosition());
         Snackbar.make(view, "Выберите клиента", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sk, sk_uid = "Все", br = "Все", gr = "Все";
                sk = mass_sp_sklad[spinner_sklad.getSelectedItemPosition()][0];
                sk_uid = mass_sp_sklad[spinner_sklad.getSelectedItemPosition()][1];
                br = mass_sp_brends[spinner_brends.getSelectedItemPosition()];
                if (mass_sp_subBrends.length > 0) {
                    gr = mass_sp_subBrends[spinner_group.getSelectedItemPosition()];
                } else gr = "Все";
                DATA_UP(sk_uid, br, gr, view);
            }
        });
    }


    // Константы для чтения
    protected void Constanta_Read() {
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        PEREM_FTP_SERV = sp.getString("PEREM_FTP_SERV", "0");                    //чтение данных: имя сервера
        PEREM_FTP_LOGIN = sp.getString("PEREM_FTP_LOGIN", "0");                  //чтение данных: имя логин
        PEREM_FTP_PASS = sp.getString("PEREM_FTP_PASS", "0");                    //чтение данных: имя пароль
        PEREM_FTP_DISTR_XML = sp.getString("PEREM_FTP_DISTR_XML", "0");          //чтение данных: путь к файлам XML
        PEREM_FTP_DISTR_db3 = sp.getString("PEREM_FTP_DISTR_db3", "0");          //чтение данных: путь к файлам DB3
        PEREM_IMAGE_PUT_SDCARD = sp.getString("PEREM_IMAGE_PUT_SDCARD", "0");    // путь картинок на телефоне /sdcard/Price/Image/
        PEREM_IMAGE_PUT_PHONE = sp.getString("PEREM_IMAGE_PUT_PHONE", "0");      // Путь картинок в др. приложении android.resource://kg.price_list.roman_kerkin.sunbell_price/drawable/

        PEREM_MAIL_LOGIN = sp.getString("PEREM_MAIL_LOGIN", "0");                //чтение данных: для почты логин
        PEREM_MAIL_PASS = sp.getString("PEREM_MAIL_PASS", "0");                  //чтение данных: для почты пароль
        PEREM_MAIL_START = sp.getString("PEREM_MAIL_START", "0");                //чтение данных: для почты от кого
        PEREM_MAIL_END = sp.getString("PEREM_MAIL_END", "0");                    //чтение данных: для почты от кому
        PEREM_DB3_CONST = sp.getString("PEREM_DB3_CONST", "0");                  //чтение данных: Путь к базам данных с константами
        PEREM_DB3_BASE = sp.getString("PEREM_DB3_BASE", "0");                    //чтение данных: Путь к базам данных с товаром
        PEREM_DB3_RN = sp.getString("PEREM_DB3_RN", "0");                        //чтение данных: Путь к базам данных с накладными
        PEREM_ANDROID_ID_ADMIN = sp.getString("PEREM_ANDROID_ID_ADMIN", "0");    //чтение данных: Универсальный номер для админа
        PEREM_ANDROID_ID = sp.getString("PEREM_ANDROID_ID", "0");                //чтение данных: Универсальный номер пользователя


        PEREM_AG_UID = sp.getString("PEREM_AG_UID", "0");                         //чтение данных: передача кода агента (A8BA1F48-C7E1-497B-B74A-D86426684712)
        PEREM_AG_NAME = sp.getString("PEREM_AG_NAME", "0");                       //чтение данных: передача кода агента (Имя пользователя)
        PEREM_AG_REGION = sp.getString("PEREM_AG_REGION", "0");                   //чтение данных: маршруты для привязки к контагентам
        PEREM_AG_UID_REGION = sp.getString("PEREM_AG_UID_REGION", "0");           //чтение данных: uid маршруты для привязки к контагентам
        PEREM_AG_CENA = sp.getString("PEREM_AG_CENA", "0");                       //чтение данных: цены для агентов
        PEREM_AG_SKLAD = sp.getString("PEREM_AG_SKLAD", "0");                     //чтение данных: склады для агентов
        PEREM_AG_UID_SKLAD = sp.getString("PEREM_AG_UID_SKLAD", "0");             //чтение данных: UID склады для агентов
        PEREM_AG_TYPE_REAL = sp.getString("PEREM_AG_TYPE_REAL", "0");             //чтение данных: выбор типа торгового агента 1-OSDO или 2-PRES
        PEREM_AG_TYPE_USER = sp.getString("PEREM_AG_TYPE_USER", "0");             //чтение данных: тип учетной записи агент или экспедитор
        PEREM_WORK_DISTR = sp.getString("PEREM_WORK_DISTR", "0");                 //чтение данных: имя папки с данными (01_WDay)
        PEREM_KOD_MOBILE = sp.getString("PEREM_KOD_MOBILE", "0");                 //чтение данных:
    }

    // Константы для записи
    protected void Constanta_Write(String sp_sklad, String sp_brends, String sp_sub_brends) {
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        ed = sp.edit();
        ed.putString("PEREM_OTCHET_INVERTARZ_BREND", sp_brends);          //
        ed.putString("PEREM_OTCHET_INVERTARZ_SUBBREND", sp_sub_brends);   //
        ed.putString("PEREM_OTCHET_INVERTARZ_SKLAD", sp_sklad);           //
        ed.putString("PEREM_OTCHET_INVERTARZ_NEW_RN", "");           //
        ed.putString("PEREM_OTCHET_INVERTARZ_NAME_TABLE", pref_otchet_invert);           //
        ed.commit();
    }

    protected void DATA_UP(String w_uidsklad, String w_brend, String w_subbrends, View w_view) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        if (w_subbrends.equals("Все")) {
            query = "SELECT base_in_nomeclature.brends, base_in_nomeclature.p_group, const_sklad.sklad_uid\n" +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "LEFT JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid\n" +
                    "WHERE base_in_ostatok.sklad_uid = '" + w_uidsklad + "'\n" +
                    "AND base_in_nomeclature.brends = '" + w_brend + "';";
        } else {
            query = "SELECT base_in_nomeclature.brends, base_in_nomeclature.p_group, const_sklad.sklad_uid\n" +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "LEFT JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid\n" +
                    "WHERE base_in_ostatok.sklad_uid = '" + w_uidsklad + "'\n" +
                    "AND base_in_nomeclature.brends = '" + w_brend + "'\n" +
                    "AND base_in_nomeclature.p_group = '" + w_subbrends + "'";
        }
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            if (!w_uidsklad.equals("Все") & !w_brend.equals("Все")) {
                Write_New_Table_Otvhet(w_uidsklad, w_brend, w_subbrends);      // Создать таблицу для отчета (*нету)
                Create_Table_New_Data(pref_otchet_invert);
                Write_Table_New_Data(pref_otchet_invert, w_uidsklad, w_brend, w_subbrends);
                Constanta_Write(w_uidsklad, w_brend, w_subbrends);             // Записать данные для отбора
                Intent intent = new Intent(context_Activity, SPR_Ostatok_Inventoriz.class);
                startActivity(intent);
                finish();
            } else Snackbar.make(w_view, "выберите все данные", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            Snackbar.make(w_view, "пустые поля", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }


    }

    // Обработка данных список рабочих складов:
    protected void SQLite_Work_Sklad() {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
            String query = "SELECT DISTINCT const_sklad.sklad_name, base_in_ostatok.sklad_uid " +
                    "FROM base_in_ostatok\n" +
                    "LEFT JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid;";
            final Cursor cursor = db.rawQuery(query, null);
            mass_sp_sklad = new String[cursor.getCount() + 1][2];
            mass_sp_sklad[0][0] = "Все";
            mass_sp_sklad[0][1] = "ALL";
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String sklad_uid = cursor.getString(cursor.getColumnIndexOrThrow("sklad_uid")); // код клиента
                String sklad_name = cursor.getString(cursor.getColumnIndexOrThrow("sklad_name")); // код клиента
                mass_sp_sklad[cursor.getPosition() + 1][0] = sklad_name;
                mass_sp_sklad[cursor.getPosition() + 1][1] = sklad_uid;
                cursor.moveToNext();
            }
            cursor.close();
            db.close();

            add_spinner_sklad.clear();
            for (int i = 0; i < mass_sp_sklad.length; i++) {
                //Log.e("массив складов: ", mass_sp_sklad[i][0] + "__" + mass_sp_sklad[i][1]);
                add_spinner_sklad.add(new ListAdapterSimple_Login(mass_sp_sklad[i][0], mass_sp_sklad[i][1], "склады"));
            }
            adapterPriceClients_spinner_sklad = new ListAdapterAde_Login(context_Activity, add_spinner_sklad);
            adapterPriceClients_spinner_sklad.notifyDataSetChanged();
            spinner_sklad.setAdapter(adapterPriceClients_spinner_sklad);

        } catch (Exception e) {
            Toast.makeText(context_Activity, Log_Data + "отсутствует активные склады", Toast.LENGTH_LONG).show();
            Log.e(Log_Data, "отсутствует активные склады!");
        }
    }

    // Обработка данных загрузка брендов
    protected void SQLite_Work_Brends() {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
            String query = "SELECT DISTINCT base_in_nomeclature.brends, base_in_nomeclature.name, base_in_ostatok.count " +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "WHERE base_in_ostatok.count > 0 GROUP BY brends";
            final Cursor cursor = db.rawQuery(query, null);
            mass_sp_brends = new String[cursor.getCount() + 1];
            mass_sp_brends[0] = "Все";
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name")); // код клиента
                String brends = cursor.getString(cursor.getColumnIndexOrThrow("brends")); // код клиента
                mass_sp_brends[cursor.getPosition() + 1] = brends;
                //  Log.e("массив брендов: ", brends);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();


            add_spinner_brends.clear();
            for (int i = 0; i < mass_sp_brends.length; i++) {
                add_spinner_brends.add(new ListAdapterSimple_Login(mass_sp_brends[i], "", "бренды"));
            }
            adapterPriceClients_spinner_brends = new ListAdapterAde_Login(context_Activity, add_spinner_brends);
            adapterPriceClients_spinner_brends.notifyDataSetChanged();
            spinner_brends.setAdapter(adapterPriceClients_spinner_brends);


        } catch (Exception e) {
            Toast.makeText(context_Activity, Log_Data + "отсутствует массив брендов", Toast.LENGTH_LONG).show();
            Log.e(Log_Data, "отсутствует массив брендов!");
        }
    }

    // Обработка данных загрузка группы товаров из брендов
    protected void SQLite_Work_Groups(String sp_brends) {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
            String query = "SELECT DISTINCT base_in_nomeclature.brends, base_in_nomeclature.p_group, base_in_nomeclature.name, base_in_ostatok.count FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "WHERE base_in_ostatok.count > 0 AND base_in_nomeclature.brends = '" + sp_brends + "'" +
                    "GROUP BY p_group \n" +
                    "ORDER BY brends;";
            final Cursor cursor = db.rawQuery(query, null);
            mass_sp_subBrends = new String[cursor.getCount() + 1];
            mass_sp_subBrends[0] = "Все";
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String p_group = cursor.getString(cursor.getColumnIndexOrThrow("p_group")); // код клиента
                mass_sp_subBrends[cursor.getPosition() + 1] = p_group;
                Log.e("массив группы брендов: ", p_group);
                Log.e("GROUP ", p_group);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();

            add_spinner_group.clear();
            for (int i = 0; i < mass_sp_subBrends.length; i++) {
                add_spinner_group.add(new ListAdapterSimple_Login(mass_sp_subBrends[i], "", "группы"));
            }
            adapterPriceClients_spinner_group = new ListAdapterAde_Login(context_Activity, add_spinner_group);
            adapterPriceClients_spinner_group.notifyDataSetChanged();
            spinner_group.setAdapter(adapterPriceClients_spinner_group);

        } catch (Exception e) {
            Toast.makeText(context_Activity, Log_Data + "отсутствует массив брендов подгрупп", Toast.LENGTH_LONG).show();
            Log.e(Log_Data, "отсутствует массив брендов подгрупп!");
        }
    }


    protected void Write_New_Table_Otvhet(String sp_sklad, String sp_brends, String sp_sub_brends) {
        //  Запсиь в таблицу(журнал) оглавления
        String[] mass = getResources().getStringArray(R.array.mouth_array_text);
        String[] mass_pref = getResources().getStringArray(R.array.mouth_array_text_db);
        for (int i = 0; i < mass.length; i++) {
            if ((i) == Integer.valueOf(this_rn_month)) {
                Month_Prefix_Mass(0, mass_pref[i - 1]);
            }
        }


        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("sunbell_otchet_db.db3", MODE_PRIVATE, null);
        String query = "SELECT * FROM all_otchet;";
        final Cursor cursor = db.rawQuery(query, null);
        ContentValues localContentValues = new ContentValues();
        cursor.moveToLast();
        localContentValues.put("otch_data_db", this_data_db);
        localContentValues.put("otch_data_display", this_data_display);
        localContentValues.put("otch_vrema", this_vrema_db);
        localContentValues.put("otch_nomer", pref_otchet_invert);
        localContentValues.put("otch_name", "Инвентаризация");
        localContentValues.put("otch_sklad", sp_sklad);
        localContentValues.put("otch_brends", sp_brends);
        localContentValues.put("otch_subbrends", sp_sub_brends);
        db.insert("all_otchet", null, localContentValues);
        cursor.close();
        db.close();
    }

    protected void Month_Prefix_Mass(int w_index, String w_index_month) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("sunbell_otchet_db.db3", MODE_PRIVATE, null);
        String query = "SELECT * FROM all_otchet\n" +
                "WHERE otch_nomer  LIKE '%" + w_index_month + "%'\n" +
                "ORDER BY otch_nomer ASC;";
        index_string = "";
        final Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            String otch_nomer = cursor.getString(cursor.getColumnIndexOrThrow("otch_nomer")); // код клиента
            w_index = Integer.valueOf(otch_nomer.substring(otch_nomer.length() - 3, otch_nomer.length())) + 1;
            Log.e("PREF: ", "" + w_index);
            if (w_index < 10) {
                index_string = "00" + w_index;
            } else if (w_index < 100) {
                index_string = "0" + w_index;
            } else if (w_index < 999) {
                index_string = "" + w_index;
            } else {
                Toast.makeText(context_Activity, "Вы достигли максимального значении!", Toast.LENGTH_LONG).show();
                Log.e(this.getLocalClassName(), "Вы достигли максимального значении!");
            }
        } else {
            index_string = "001";
        }
        cursor.close();
        db.close();

        pref_otchet_invert = "otch_invnt_" + w_index_month + index_string;
        Log.e("MONTH: ", pref_otchet_invert);
    }

    protected void Calendate_New() {
        DateFormat df_data_display = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat df_data_db = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df_vrema = new SimpleDateFormat("HH:mm:ss");
        DateFormat df_year = new SimpleDateFormat("yyyy");
        DateFormat df_month = new SimpleDateFormat("MM");
        DateFormat df_day = new SimpleDateFormat("dd");

        this_data_display = df_data_display.format(Calendar.getInstance().getTime());
        this_data_db = df_data_db.format(Calendar.getInstance().getTime());
        this_vrema_db = df_vrema.format(Calendar.getInstance().getTime());
        this_rn_year = df_year.format(Calendar.getInstance().getTime());
        this_rn_month = df_month.format(Calendar.getInstance().getTime());
        this_rn_day = df_day.format(Calendar.getInstance().getTime());

        Log.e("MONTH: ", "" + Integer.valueOf(this_rn_month));
        Log.e("DATA2: ", df_data_db.format(Calendar.getInstance().getTime()));

       /* this_data_display = this_rn_day + "." + this_rn_month + "." + this_rn_year;
        this_data_db = this_rn_day + "." + this_rn_month + "." + this_rn_year;*/
    }


    protected void Write_Table_New_Data(String w_nomer_rn, String sp_sklad, String sp_brends, String sp_sub_brends) {
        SQLiteDatabase db_read = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        SQLiteDatabase db_write = getBaseContext().openOrCreateDatabase("sunbell_otchet_db.db3", MODE_PRIVATE, null);
        if (sp_sub_brends.equals("Все")) {
            query = "SELECT base_in_nomeclature.name,  base_in_nomeclature.koduid, " +
                    "base_in_price.price, base_in_ostatok.count, base_in_image.kod_image, " +
                    "base_in_nomeclature.kolbox, base_in_nomeclature.strih\n" +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                    "WHERE base_in_ostatok.count > 0\n" +
                    "AND base_in_ostatok.sklad_uid = '"+sp_sklad+"'\n" +
                    "AND base_in_nomeclature.brends = '"+sp_brends+"';";
        } else {
            query = "SELECT base_in_nomeclature.name, base_in_nomeclature.koduid, " +
                    "base_in_price.price, base_in_ostatok.count, base_in_image.kod_image, " +
                    "base_in_nomeclature.kolbox, base_in_nomeclature.strih\n" +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                    "WHERE base_in_ostatok.count > 0\n" +
                    "AND base_in_ostatok.sklad_uid = '"+sp_sklad+"'\n" +
                    "AND base_in_nomeclature.brends = '"+sp_brends+"'\n" +
                    "AND base_in_nomeclature.p_group = '"+sp_sub_brends+"';";
        }


        final Cursor cursor = db_read.rawQuery(query, null);
        ContentValues localContentValues = new ContentValues();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String koduid = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));
            String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
            String ostatok = cursor.getString(cursor.getColumnIndexOrThrow("count"));
            String kod_image = cursor.getString(cursor.getColumnIndexOrThrow("kod_image"));
            String kolbox = cursor.getString(cursor.getColumnIndexOrThrow("kolbox"));
            String strih = cursor.getString(cursor.getColumnIndexOrThrow("strih"));

            localContentValues.put("name", name);
            localContentValues.put("name_uid", koduid);
            localContentValues.put("cena", price);
            localContentValues.put("ostatok", ostatok);
            localContentValues.put("ostatok_new", ostatok);
            localContentValues.put("itogo_invert", "--");
            localContentValues.put("image", kod_image);
            localContentValues.put("kol_box", kolbox);
            localContentValues.put("strih", strih);
            db_write.insert(w_nomer_rn, null, localContentValues);
            cursor.moveToNext();
        }
        cursor.close();
        db_read.close();
        db_write.close();
    }

    protected void Create_Table_New_Data(String w_name_table) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("sunbell_otchet_db.db3", MODE_PRIVATE, null);
        String create = "CREATE TABLE " + w_name_table + " (image TEXT, kol_box TEXT, strih TEXT, name TEXT, name_uid TEXT, cena TEXT, ostatok TEXT, ostatok_new TEXT, itogo_invert TEXT);";
        final Cursor cursor = db.rawQuery(create, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            cursor.moveToNext();
        }
        Toast.makeText(context_Activity, "Создана таблица " + w_name_table, Toast.LENGTH_SHORT).show();
        cursor.close();
        db.close();
    }  // Создание новой таблицы для данных инвентаризации


}


