package kg.roman.Mobile_Torgovla.Spravochnik;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_Login;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Login;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Otchet_Ostatok;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Otchet_Ostatok;
import kg.roman.Mobile_Torgovla.R;

public class SPR_Ostatok_Old extends AppCompatActivity {
    public ArrayList<ListAdapterSimple_Otchet_Ostatok> adapter_listview = new ArrayList<ListAdapterSimple_Otchet_Ostatok>();
    public ListAdapterAde_Otchet_Ostatok adapterPriceClients;

    public ArrayList<ListAdapterSimple_Login> add_spinner_brends = new ArrayList<ListAdapterSimple_Login>();
    public ListAdapterAde_Login adapterPriceClients_spinner_brends;

    public ArrayList<ListAdapterSimple_Login> add_spinner_sklad = new ArrayList<ListAdapterSimple_Login>();
    public ListAdapterAde_Login adapterPriceClients_spinner_sklad;

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
    public String Save_dialog_up_;

    public String query, sklad_on, SKLAD_WORK, BASE_WORK, TYPE_SKLAD;
    public String[] mass_brends, mass_sklad_name, mass_sklad_kod;
    public String[] OS_brends, OS_Name, OS_Uid, OS_LogName;

    public Button button;
    public Spinner spinner_sklad, spinner_brends;
    public ListView listView;
    public ArrayAdapter<String> adapter_brends, adapter_sklad;
    public String[][] mass, mass_sklad;
    public String[] mass_sklad_work_uid;
    public Cursor cursor_ID, cursor;

    public String Log_Data, sklad, brends, sklad_uid;
    public String ost_sklad, ost_brends, ost_uid_skald;
    public ProgressBar progressBar;
    private ImageView imageView_list;

    /*sklad = "Все";
    brends = "Все";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt_spr_ostatok_golovnoy);
        context_Activity = SPR_Ostatok_Old.this;
        Log_Data = "Ошибка остатков: ";
        // Константы для чтения
        Constanta_Read();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon_image);
        getSupportActionBar().setTitle("Остатки товара по брендам и складам");
      //  getSupportActionBar().setSubtitle("Остатки на " + sp.getString("Sync_Data_0", "0"));

        spinner_sklad = findViewById(R.id.spinner_sklad);
        spinner_brends = findViewById(R.id.spinner_brends);
        button = findViewById(R.id.button_load);
        listView = findViewById(R.id.listview_sklad);
        progressBar = findViewById(R.id.progressBar_list_up);

        TYPE_SKLAD = "Склад основной";
        SKLAD_WORK = "base_in_ostatok";
        BASE_WORK = PEREM_DB3_BASE;
        Log.e(Log_Data, BASE_WORK);
        SQLite_Work_Sklad_UID();
        SQLIte_Data_Sklad();
        SQLite_Data_Brends();
        Loading_Spinner_Sklad();
        Loading_Spinner_Brends();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                ost_sklad = sklad;
                ost_brends = brends;
                ost_uid_skald = sklad_uid;

                Log.e("Старт", ost_sklad + "," + ost_brends + "," + ost_uid_skald);

                adapter_listview.clear();
                switch (TYPE_SKLAD) {
                    case "Склад основной": {
                        SKLAD_WORK = "base_in_ostatok";
                        BASE_WORK = PEREM_DB3_BASE;
                        adapter_listview.clear();
                        Loading_ListView(ost_sklad, ost_brends, ost_uid_skald);
                        adapterPriceClients = new ListAdapterAde_Otchet_Ostatok(context_Activity, adapter_listview);
                        adapterPriceClients.notifyDataSetChanged();
                        listView.setAdapter(adapterPriceClients);

                    }
                    break;
                    case "Склад головной": {
                        TYPE_SKLAD = "Склад головной";
                        SKLAD_WORK = "base_in_ostatok_golovnoy";
                        BASE_WORK = "sunbell_ostatki_filial.db3";
                        adapter_listview.clear();
                        Loading_ListView_Bishkek(ost_sklad, ost_brends, ost_uid_skald);
                        adapterPriceClients = new ListAdapterAde_Otchet_Ostatok(context_Activity, adapter_listview);
                        adapterPriceClients.notifyDataSetChanged();
                        listView.setAdapter(adapterPriceClients);
                    }
                    break;
                }

              /*  adapterPriceClients = new ListAdapterAde_Otchet_Ostatok(context_Activity, adapter_listview);
                adapterPriceClients.notifyDataSetChanged();
                listView.setAdapter(adapterPriceClients);*/

               /* try {
                    SPR_Ostatok.MyAsyncTask_Sync_Users asyncTask = new SPR_Ostatok.MyAsyncTask_Sync_Users(); // для Бишкека
                    asyncTask.execute();
                } catch (Exception e) {
                    Toast.makeText(context_Activity, Log_Data + "нет данных для загрузки!", Toast.LENGTH_LONG).show();
                    Log.e(Log_Data, "нет данных для загрузки!");
                }*/

            }
        });

    }


    // Обработка данных по рабочим складам
    protected void SQLite_Work_Sklad_UID() {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(BASE_WORK, MODE_PRIVATE, null);
            String query = "SELECT DISTINCT sklad_uid  FROM " + SKLAD_WORK + "";
            final Cursor cursor = db.rawQuery(query, null);
            mass_sklad_work_uid = new String[cursor.getCount()];
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String sklad_uid = cursor.getString(cursor.getColumnIndex("sklad_uid")); // код клиента
                mass_sklad_work_uid[cursor.getPosition()] = sklad_uid;
                Log.e("sklad_uid=", sklad_uid);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();

        } catch (Exception e) {
            Toast.makeText(context_Activity, Log_Data + "отсутствует активные склады", Toast.LENGTH_LONG).show();
            Log.e(Log_Data, "отсутствует активные склады!");
        }
    }

    // Обработка данных из базы для SpinnerSkald
    protected void SQLIte_Data_Sklad() {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
            String query = "SELECT DISTINCT * FROM const_sklad \n" +
                    "GROUP BY sklad_uid\n" +
                    "ORDER BY sklad_name";
            final Cursor cursor = db.rawQuery(query, null);
            mass_sklad = new String[mass_sklad_work_uid.length + 1][2];
            mass_sklad_name = new String[mass_sklad_work_uid.length + 1];
            mass_sklad[0][0] = "Все";
            mass_sklad_name[0] = "Все";
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String sklad_name = cursor.getString(cursor.getColumnIndex("sklad_name"));   // имя склада
                String sklad_uid = cursor.getString(cursor.getColumnIndex("sklad_uid"));     // имя uid-склада

                for (int s = 0; s < mass_sklad_work_uid.length; s++) {
                    if (sklad_uid.equals(mass_sklad_work_uid[s])) {
                        mass_sklad[s + 1][0] = sklad_name;
                        mass_sklad[s + 1][1] = sklad_uid;
                        mass_sklad_name[s + 1] = sklad_name;
                        Log.e("Spinner1=", sklad_name);
                    }
                }
                cursor.moveToNext();
            }
            cursor.close();
            db.close();

            /*adapter_sklad = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, mass_sklad_name);
            adapter_sklad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            adapter_sklad.notifyDataSetChanged();
            spinner_sklad.setAdapter(adapter_sklad);*/

            add_spinner_sklad.clear();
            for (int i = 0; i < mass_sklad_name.length; i++) {
                add_spinner_sklad.add(new ListAdapterSimple_Login(mass_sklad_name[i], mass_sklad[i][1]));
            }
            adapterPriceClients_spinner_sklad = new ListAdapterAde_Login(context_Activity, add_spinner_sklad);
            adapterPriceClients_spinner_sklad.notifyDataSetChanged();
            spinner_sklad.setAdapter(adapterPriceClients_spinner_sklad);

        } catch (Exception e) {
            Toast.makeText(context_Activity, Log_Data + "отсутствует база складов!", Toast.LENGTH_LONG).show();
            Log.e(Log_Data, "отсутствует база складов!");
        }
    }

    // Обработка данных из базы для SpinnerBrends
    protected void SQLite_Data_Brends() {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(BASE_WORK, MODE_PRIVATE, null);
            String query = "SELECT * FROM base_in_nomeclature GROUP BY brends ORDER BY brends";
            final Cursor cursor = db.rawQuery(query, null);
            mass_brends = new String[cursor.getCount() + 1];
            cursor.moveToFirst();
            mass_brends[0] = "Все";
            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(cursor.getColumnIndex("brends")); // код клиента
                mass_brends[cursor.getPosition() + 1] = name;
                cursor.moveToNext();
            }
            cursor.close();
            db.close();

            /*adapter_brends = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mass_brends);
            adapter_brends.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapter_brends.notifyDataSetChanged();
            spinner_brends.setAdapter(adapter_brends);*/

            add_spinner_brends.clear();
            for (int i = 0; i < mass_brends.length; i++) {
                add_spinner_brends.add(new ListAdapterSimple_Login(mass_brends[i], ""));
            }
            adapterPriceClients_spinner_brends = new ListAdapterAde_Login(context_Activity, add_spinner_brends);
            adapterPriceClients_spinner_brends.notifyDataSetChanged();
            spinner_brends.setAdapter(adapterPriceClients_spinner_brends);
        } catch (Exception e) {
            Toast.makeText(context_Activity, Log_Data + "отсутствует база товара!", Toast.LENGTH_LONG).show();
            Log.e(Log_Data, "отсутствует база товара!");
        }
    }

    // Обработка нажатие SpinnerSkald
    protected void Loading_Spinner_Sklad() {
        try {
            spinner_sklad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView tv = view.findViewById(R.id.textView_login);
                    TextView uid = view.findViewById(R.id.tvw_sp_name_image);
                    sklad = tv.getText().toString();
                    sklad_uid = uid.getText().toString();
                    Log.e("sklad=", sklad + sklad_uid);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(context_Activity, Log_Data + "не чего не выбрано!", Toast.LENGTH_LONG).show();
            Log.e(Log_Data, "не чего не выбрано!");
        }

    }

    // Обработка нажатие SpinnerBrends
    protected void Loading_Spinner_Brends() {
        try {
            spinner_brends.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView tv = view.findViewById(R.id.textView_login);
                    brends = tv.getText().toString();
                    Log.e("brends=", mass_brends[position]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (Exception e) {
            Toast.makeText(context_Activity, Log_Data + "не чего не выбрано!", Toast.LENGTH_LONG).show();
            Log.e(Log_Data, "не чего не выбрано!");
        }
    }

    // Обработка нажатие ListView
    protected void Loading_ListView(String sp_sklad, String sp_brends, String sp_uid_skald) {
        ost_sklad = sp_sklad;
        ost_brends = sp_brends;
        ost_uid_skald = sp_uid_skald;

        try {
            String perem = "Все";
            Log.e("Ost= ", ost_sklad + ", " + ost_brends + ", " + perem);
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(BASE_WORK, MODE_PRIVATE, null);
            if (ost_sklad.equals(perem) & ost_brends.equals(perem)) {
                // Выборка остатков Все из Всех
                query = "SELECT base_in_nomeclature.brends, base_in_nomeclature.p_group, " +
                        "base_in_nomeclature.name, base_in_ostatok.count, base_in_price.price, " +
                        "base_in_image.kod_image, base_in_ostatok.sklad_uid\n" +
                        "FROM base_in_nomeclature\n" +
                        "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                        "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                        "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                        "WHERE base_in_ostatok.count >0";
            } else if (ost_sklad.equals(perem) & !ost_brends.equals(perem)) {
                // Выборка остатков по брендам
                query = "SELECT base_in_nomeclature.brends, base_in_nomeclature.p_group, " +
                        "base_in_nomeclature.name, base_in_ostatok.count, base_in_price.price, " +
                        "base_in_image.kod_image, base_in_ostatok.sklad_uid\n" +
                        "FROM base_in_nomeclature\n" +
                        "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                        "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                        "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                        "WHERE base_in_ostatok.count >0 AND base_in_nomeclature.brends = '" + ost_brends + "' ";
            } else if (!ost_sklad.equals(perem) & ost_brends.equals(perem)) {
                // Выборка остатков по складам
                query = "SELECT base_in_nomeclature.brends, base_in_nomeclature.p_group, " +
                        "base_in_nomeclature.name, base_in_ostatok.count, base_in_price.price, " +
                        "base_in_image.kod_image, base_in_ostatok.sklad_uid\n" +
                        "FROM base_in_nomeclature\n" +
                        "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                        "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                        "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                        "WHERE base_in_ostatok.count >0 AND base_in_ostatok.sklad_uid = '" + ost_uid_skald + "' ";
            } else {
                // Выборка остатков по складам и по брендам
                query = "SELECT base_in_nomeclature.brends, base_in_nomeclature.p_group, " +
                        "base_in_nomeclature.name, base_in_ostatok.count, base_in_price.price, " +
                        "base_in_image.kod_image, base_in_ostatok.sklad_uid\n" +
                        "FROM base_in_nomeclature\n" +
                        "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                        "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                        "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                        "WHERE base_in_ostatok.count >0 " +
                        "AND base_in_ostatok.sklad_uid = '" + ost_uid_skald + "' " +
                        "AND base_in_nomeclature.brends = '" + ost_brends + "'";
            }
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                String brends = cursor.getString(cursor.getColumnIndex("brends"));
                String p_group = cursor.getString(cursor.getColumnIndex("p_group"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String sklad_name = cursor.getString(cursor.getColumnIndex("sklad_name"));
                String price = cursor.getString(cursor.getColumnIndex("price"));
                String count = cursor.getString(cursor.getColumnIndex("count"));
                String kolbox = cursor.getString(cursor.getColumnIndex("kolbox"));
                String image = cursor.getString(cursor.getColumnIndex("kod_image"));

                for (int k = 0; k < mass_sklad.length; k++) {
                    if (sklad.equals(mass_sklad[k][1])) {
                        sklad_on = mass_sklad[k][0];
                    }
                }
                adapter_listview.add(new ListAdapterSimple_Otchet_Ostatok(name, brends, p_group, sklad_name, price+"c", count+"шт", kolbox+"шт", "", image));
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
            progressBar.setVisibility(View.GONE);
        } catch (Exception e) {

            Log.e("Err..", "rrr");
        }
    }

    // Обработка нажатие ListView
    protected void Loading_ListView_Bishkek(String sp_sklad, String sp_brends, String sp_uid_skald) {
        ost_sklad = sp_sklad;
        ost_brends = sp_brends;
        ost_uid_skald = sp_uid_skald;

        try {
            String perem = "Все";
            Log.e("Ost= ", ost_sklad + ", " + ost_brends + ", " + perem);
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(BASE_WORK, MODE_PRIVATE, null);

            if (ost_sklad.equals(perem) & ost_brends.equals(perem)) {
                // Выборка остатков Все из Всех
                query = "SELECT * FROM base_in_ostatok_golovnoy";
            } else if (ost_sklad.equals(perem) & !ost_brends.equals(perem)) {
                // Выборка остатков по брендам
                query = "SELECT * FROM base_in_ostatok_golovnoy WHERE brends = '" + ost_brends + "' ";

            } else if (!ost_sklad.equals(perem) & ost_brends.equals(perem)) {
                // Выборка остатков по складам
                query = "SELECT * FROM base_in_ostatok_golovnoy WHERE sklad_uid = '" + ost_uid_skald + "' ";
            } else {
                // Выборка остатков по складам и по брендам
                query = "SELECT * FROM base_in_ostatok_golovnoy WHERE sklad_uid = '" + ost_uid_skald + "' AND brends = '" + ost_brends + "'";
            }
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String brends = cursor.getString(cursor.getColumnIndex("brends"));
                String p_group = cursor.getString(cursor.getColumnIndex("p_group"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String sklad_name = cursor.getString(cursor.getColumnIndex("sklad_name"));
                String price = cursor.getString(cursor.getColumnIndex("price"));
                String count = cursor.getString(cursor.getColumnIndex("count"));
                String kolbox = cursor.getString(cursor.getColumnIndex("kolbox"));
                String image = cursor.getString(cursor.getColumnIndex("kod_image"));
                for (int k = 0; k < mass_sklad.length; k++) {
                    if (sklad.equals(mass_sklad[k][1])) {
                        sklad_on = mass_sklad[k][0];
                    }
                }
                adapter_listview.add(new ListAdapterSimple_Otchet_Ostatok(name, brends, p_group, sklad_name, price, count, kolbox, "", image));
             //   adapter_listview.add(new ListAdapterSimple_Otchet_Ostatok(name, count + " шт", brends, cena + " с", image, sklad_on));
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
            progressBar.setVisibility(View.GONE);
        } catch (Exception e) {

            Log.e("Err..", "rrr");
        }
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

        for (int i = 0; i < getResources().getStringArray(R.array.mass_for_update_data).length; i++) {
            Save_dialog_up_ = sp.getString("Save_dialog_up_" + i, "0");
        }
    }


    public void Loading_Data_Brends() {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
            String query = "SELECT const_brends_ostatok.brends, const_brends_ostatok.name_id, const_brends_ostatok.work\n" +
                    "FROM const_brends_ostatok\n" +
                    "WHERE  const_brends_ostatok.work = 'true'";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            OS_brends = new String[cursor.getCount()];
            while (cursor.isAfterLast() == false) {
                String brends = cursor.getString(cursor.getColumnIndex("brends")); // код клиента
                OS_brends[cursor.getPosition()] = brends;
                Log.e("True: ", OS_brends[cursor.getPosition()]);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
        } catch (Exception e) {

        }

    }

    protected void Loading_Data_Sklad() {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
            String query = "SELECT const_sklad.sklad_name, const_sklad.sklad_uid, const_sklad.log_name, const_sklad.work\n" +
                    "FROM const_sklad\n" +
                    "WHERE  const_sklad.work = 'true'";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            OS_Uid = new String[cursor.getCount()];
            OS_Name = new String[cursor.getCount()];
            OS_LogName = new String[cursor.getCount()];
            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(cursor.getColumnIndex("sklad_name")); // код клиента
                String uid = cursor.getString(cursor.getColumnIndex("sklad_uid")); // код клиента
                String work = cursor.getString(cursor.getColumnIndex("work")); // код клиента
                String LogName = cursor.getString(cursor.getColumnIndex("log_name")); // код клиента
                OS_Name[cursor.getPosition()] = name;
                OS_Uid[cursor.getPosition()] = uid;
                OS_LogName[cursor.getPosition()] = LogName;
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
        } catch (Exception e) {

        }

    }


    protected void Loading_ListAdapter()
    {
        SQLite_Work_Sklad_UID();
        SQLIte_Data_Sklad();
        SQLite_Data_Brends();
        Loading_Spinner_Sklad();
        Loading_Spinner_Brends();
        adapter_listview.clear();

        ost_sklad = sklad;
        ost_brends = brends;
        ost_uid_skald = sklad_uid;

        Loading_ListView(ost_sklad, ost_brends, ost_uid_skald);
        adapterPriceClients = new ListAdapterAde_Otchet_Ostatok(context_Activity, adapter_listview);
        adapterPriceClients.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ostatki_single, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
           /* case R.id.menu_ost_sync: {
                Log.e(Log_Data, "Склад основной");
                TYPE_SKLAD = "Склад основной";
                SKLAD_WORK = "base_in_ostatok";
                BASE_WORK = PEREM_DB3_BASE;
                item.setChecked(true);
                Loading_ListAdapter();
            }
            break;*/
            case R.id.menu_ost_update: {
                Log.e(Log_Data, "Склад головной");
                TYPE_SKLAD = "Склад головной";
                SKLAD_WORK = "base_in_ostatok_golovnoy";
                BASE_WORK = "sunbell_ostatki_filial.db3";
                item.setChecked(true);
                Loading_ListAdapter();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyAsyncTask_Sync_Users extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК=", "Начало потока");
            adapter_listview.clear();
            adapterPriceClients = new ListAdapterAde_Otchet_Ostatok(context_Activity, adapter_listview);
            adapterPriceClients.notifyDataSetChanged();
            listView.setAdapter(adapterPriceClients);
            progressBar.setVisibility(View.VISIBLE);
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
            progressBar.setVisibility(View.GONE);
            adapterPriceClients = new ListAdapterAde_Otchet_Ostatok(context_Activity, adapter_listview);
            adapterPriceClients.notifyDataSetChanged();
            listView.setAdapter(adapterPriceClients);
        }

        private void getFloor() throws InterruptedException {
            Loading_ListView(ost_sklad, ost_brends, ost_uid_skald);

            try {


            } catch (Exception e) {

                Log.e("Err..", "rrr");
            }

            TimeUnit.SECONDS.sleep(5);
            // pDialog.dismiss();
        }
    }


}
