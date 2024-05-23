package kg.roman.Mobile_Torgovla.Spravochnik;

import android.app.ProgressDialog;
import android.content.ContentValues;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_Login;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Login;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Otchet_Ostatok;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Otchet_Ostatok;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.FtpConnectData;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Nomenclatures;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Nomenclatures_ResourceParser;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Ostatok;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Ostatok_ResourceParser;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Price;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Price_ResourceParser;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Skald_ResourceParser;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Sklad;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_SubBrends;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_SubBrends_ResourceParser;

import static kg.roman.Mobile_Torgovla.Permission.PrefActivity_Splash.calculateDirectoryInfo;

public class SPR_Ostatok_Golovnoy extends AppCompatActivity {
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
    public String Save_dialog_up_, query, SKLAD_WORK, TYPE_SKLAD;
    public Spinner spinner_sklad, spinner_brends, spinner_group;
    public ListView listView;
    public Cursor cursor;
    public String Log_Data, sklad, groups, brends, sklad_uid, new_data_ostatok;
    public String ost_sklad, ost_brends, ost_group, ost_uid_skald;
    public ProgressBar progressBar;
    public String[] mass_sp_brends, mass_sp_subBrends, mass_update_files, mass, mass_breds_group;
    public String[][] mass_sp_sklad;

    public ProgressDialog pDialog;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now, this_data_work_now, this_vrema_work_now;
    public String files_size, files_kol;
    public OutputStream outputStream;
    public Boolean syns_end = true;
    public String BASE_DB_OSTATKI_BISHKEK = "sunbell_ostatki_filial.db3", grb;
    public ContentValues localContentValues;
    public ContentValues localContentValues_Brends, localContentValues_Breds_ID, localContentValues_sub_Breds_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt_spr_ostatok);
        context_Activity = SPR_Ostatok_Golovnoy.this;
        Log.e("Class_Name: ", "|" + this.getLocalClassName());  // имя используемого класса
        // Константы для чтения
        Constanta_Read();
        Loading_Data_Ostatok();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon_image);
        getSupportActionBar().setTitle("Остатки товара");
        getSupportActionBar().setSubtitle("  остатки на " + new_data_ostatok);

        spinner_sklad = findViewById(R.id.spinner_sklad);
        spinner_brends = findViewById(R.id.spinner_brends);
        spinner_group = findViewById(R.id.spinner_group);
        listView = findViewById(R.id.listview_sklad);
        progressBar = findViewById(R.id.progressBar_list_up);

        mass_sp_brends = new String[0];
        mass_sp_subBrends = new String[0];
        mass_sp_sklad = new String[0][0];

        TYPE_SKLAD = "Склад основной";
        SKLAD_WORK = "base_in_ostatok";

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
        Log.e("brends=", "" + spinner_brends.getSelectedItemPosition());*/
    }

    // Обработка данных список рабочих складов:
    protected void SQLite_Work_Sklad() {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(BASE_DB_OSTATKI_BISHKEK, MODE_PRIVATE, null);
            String query = "SELECT DISTINCT base_in_sklad.sklad_name, base_in_ostatok.sklad_uid " +
                    "FROM base_in_ostatok\n" +
                    "LEFT JOIN base_in_sklad ON base_in_ostatok.sklad_uid = base_in_sklad.sklad_uid;";
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
                Log.e("массив складов: ", mass_sp_sklad[i][0] + "__" + mass_sp_sklad[i][1]);
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
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(BASE_DB_OSTATKI_BISHKEK, MODE_PRIVATE, null);
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
                Log.e("массив брендов: ", brends);
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
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(BASE_DB_OSTATKI_BISHKEK, MODE_PRIVATE, null);
            String query = "SELECT DISTINCT base_in_nomeclature.brends, base_in_nomeclature.p_group, base_in_nomeclature.name, " +
                    "base_in_ostatok.count FROM base_in_nomeclature\n" +
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

    // Обработка нажатие ListView
    protected void Loading_ListView(String sp_sklad, String sp_uid_skald, String sp_brends, String sp_group) {
        ost_sklad = sp_sklad;
        ost_brends = sp_brends;
        ost_group = sp_group;
        ost_uid_skald = sp_uid_skald;
        String perem = "Все";
        Log.e("Ost= ", ost_sklad + ", " + ost_brends + ", " + ost_group);
        // Все из всех
        // Все, Бренд, Все
        // Все, Бренд, Группа
        // Склад, Бренд, Все
        // Склад, Бренд, Группа

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(BASE_DB_OSTATKI_BISHKEK, MODE_PRIVATE, null);
        if (ost_sklad.equals(perem) & ost_brends.equals(perem) & ost_group.equals(perem)) {
            // Выборка остатков Все из Всех
            query = "SELECT base_in_nomeclature.brends, base_in_nomeclature.p_group, " +
                    "base_in_nomeclature.name, base_in_nomeclature.kolbox, base_in_price.price, " +
                    "base_in_ostatok.count, base_in_image.kod_image, base_in_sklad.sklad_name, base_in_ostatok.sklad_uid\n" +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                    "LEFT JOIN base_in_sklad ON base_in_ostatok.sklad_uid = base_in_sklad.sklad_uid \n" +
                    "WHERE base_in_ostatok.count > 0 ";
        } else if (ost_sklad.equals(perem) & !ost_brends.equals(perem) & ost_group.equals(perem)) {
            // Выборка остатков по брендам и группам (Все, Бренд, Все)
            query = "SELECT base_in_nomeclature.brends, base_in_nomeclature.p_group, " +
                    "base_in_nomeclature.name, base_in_nomeclature.kolbox, base_in_price.price, " +
                    "base_in_ostatok.count, base_in_image.kod_image, base_in_sklad.sklad_name, base_in_ostatok.sklad_uid\n" +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                    "LEFT JOIN base_in_sklad ON base_in_ostatok.sklad_uid = base_in_sklad.sklad_uid \n" +
                    "WHERE base_in_ostatok.count > 0 AND base_in_nomeclature.brends = '" + ost_brends + "';";
        } else if (ost_sklad.equals(perem) & !ost_brends.equals(perem) & !ost_group.equals(perem)) {
            // Выборка остатков по брендам и группам (Все, Бренд, Группа)
            query = "SELECT base_in_nomeclature.brends, base_in_nomeclature.p_group, " +
                    "base_in_nomeclature.name, base_in_nomeclature.kolbox, base_in_price.price, " +
                    "base_in_ostatok.count, base_in_image.kod_image, base_in_sklad.sklad_name, base_in_ostatok.sklad_uid\n" +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                    "LEFT JOIN base_in_sklad ON base_in_ostatok.sklad_uid = base_in_sklad.sklad_uid \n" +
                    "WHERE base_in_ostatok.count > 0 AND base_in_nomeclature.brends = '" + ost_brends + "' AND base_in_nomeclature.p_group = '" + ost_group + "';";
        } else if (!ost_sklad.equals(perem) & ost_brends.equals(perem) & ost_group.equals(perem)) {
            // Выборка остатков по складам (Склад, Все, Все)
            query = "SELECT base_in_nomeclature.brends, base_in_nomeclature.p_group, " +
                    "base_in_nomeclature.name, base_in_nomeclature.kolbox, base_in_price.price, " +
                    "base_in_ostatok.count, base_in_image.kod_image, base_in_sklad.sklad_name, base_in_ostatok.sklad_uid\n" +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                    "LEFT JOIN base_in_sklad ON base_in_ostatok.sklad_uid = base_in_sklad.sklad_uid \n" +
                    "WHERE base_in_ostatok.count > 0 AND base_in_ostatok.sklad_uid = '" + ost_uid_skald + "'";
        } else if (!ost_sklad.equals(perem) & !ost_brends.equals(perem) & ost_group.equals(perem)) {
            // Выборка остатков по складам брендам и группам  (Склад, Бренд, Все)
            query = "SELECT base_in_nomeclature.brends, base_in_nomeclature.p_group, " +
                    "base_in_nomeclature.name, base_in_nomeclature.kolbox, base_in_price.price, " +
                    "base_in_ostatok.count, base_in_image.kod_image, base_in_sklad.sklad_name, base_in_ostatok.sklad_uid\n" +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                    "LEFT JOIN base_in_sklad ON base_in_ostatok.sklad_uid = base_in_sklad.sklad_uid \n" +
                    "WHERE base_in_ostatok.count > 0 " +
                    "AND base_in_ostatok.sklad_uid = '" + ost_uid_skald + "'" +
                    "AND base_in_nomeclature.brends = '" + ost_brends + "';";
        } else {
            // Выборка остатков по складам брендам и группам  (Склад, Бренд, Группа)
            query = "SELECT base_in_nomeclature.brends, base_in_nomeclature.p_group, " +
                    "base_in_nomeclature.name, base_in_nomeclature.kolbox, base_in_price.price, " +
                    "base_in_ostatok.count, base_in_image.kod_image, base_in_sklad.sklad_name, " +
                    "base_in_ostatok.sklad_uid\n" +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                    "LEFT JOIN base_in_sklad ON base_in_ostatok.sklad_uid = base_in_sklad.sklad_uid\n" +
                    "WHERE base_in_ostatok.count > 0 \n" +
                    "AND base_in_ostatok.sklad_uid = '" + ost_uid_skald + "'\n" +
                    "AND base_in_nomeclature.brends = '" + ost_brends + "'\n" +
                    "AND base_in_nomeclature.p_group = '" + ost_group + "';";
        }
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String brends = cursor.getString(cursor.getColumnIndexOrThrow("brends"));
            String p_group = cursor.getString(cursor.getColumnIndexOrThrow("p_group"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String sklad_name = cursor.getString(cursor.getColumnIndexOrThrow("sklad_name"));
            String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
            String count = cursor.getString(cursor.getColumnIndexOrThrow("count"));
            String kolbox = cursor.getString(cursor.getColumnIndexOrThrow("kolbox"));
            String image = cursor.getString(cursor.getColumnIndexOrThrow("kod_image"));
          /*  for (int k = 0; k < mass_sp_sklad.length; k++) {
                if (sklad.equals(mass_sp_sklad[k][1])) {
                    sklad_on = mass_sp_sklad[k][0];
                }
            }*/

            int new_del, new_del_ost;
            String new_kolbox;
            new_del = Integer.parseInt(count) / Integer.parseInt(kolbox);
            new_del_ost = Integer.parseInt(count) % Integer.parseInt(kolbox);
            //Log.e("DELETE ", "|" + new_del + "|" + new_del_ost);
            if (new_del > 0) {
                if (new_del_ost == 0) {
                    new_kolbox = "(" + new_del + "кор.)";
                } else  new_kolbox = "(" + new_del + "кор. + " + new_del_ost + "шт.)";
            } else new_kolbox = "(" + new_del_ost + "шт.)";

            adapter_listview.add(new ListAdapterSimple_Otchet_Ostatok(name, brends, p_group, sklad_name, price + "c", count + "шт", kolbox + "шт", new_kolbox, image));
            //*****  adapter_listview.add(new ListAdapterSimple_Otchet_Ostatok(name, count + " шт", brends, cena + " с", image, sklad_on));

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        progressBar.setVisibility(View.GONE);


        try {

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

    // Установка дата остатков
    protected void Loading_Data_Ostatok() {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(BASE_DB_OSTATKI_BISHKEK, MODE_PRIVATE, null);
            String query = "SELECT DISTINCT data FROM base_in_ostatok";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            String ost_data, ost_month, ost_yaer;
            while (cursor.isAfterLast() == false) {
                String data = cursor.getString(cursor.getColumnIndexOrThrow("data"));   // дата остатков 'месяц/день/год'  7/4/2022
                ost_data = data.substring(data.indexOf('/') + 1, data.lastIndexOf('/'));
                ost_month = data.substring(0, data.indexOf('/'));
                ost_yaer = data.substring(data.lastIndexOf('/') + 1);
                if (ost_data.length() == 1) {
                    ost_data = "0" + ost_data;
                }
                if (ost_month.length() == 1) {
                    ost_month = "0" + ost_month;
                }
          /*  Log.e("символ1", ost_data + "|" + ost_data.length());
            Log.e("символ2", ost_month + "|" + ost_month.length());
            Log.e("символ3", ost_yaer);*/
                new_data_ostatok = ost_data + "." + ost_month + "." + ost_yaer;
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.e("Ошибка", "Не верный формат даты!");
            Toast.makeText(context_Activity, "Не верный формат даты!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ostatki_golov, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_ost_sync: {
                // Loading_Dialog_Message();
                Loading_Dialog_Message();
                mass_update_files = new String[]{""};
                mass_update_files = new String[]{"MTW_In_Price.xml", "MTW_In_ResidueGoodsPR.xml", "MTW_In_Nomenclatures.xml", "MTW_In_Brends.xml"};
                SPR_Ostatok_Golovnoy.MyAsyncTask_Update_File_MTW_BIshkek asyncTask = new SPR_Ostatok_Golovnoy.MyAsyncTask_Update_File_MTW_BIshkek();
                asyncTask.execute();
            }
            break;
            case R.id.menu_ost_sync2: {
                Loading_Dialog_Message();
                SPR_Ostatok_Golovnoy.MyAsyncTask_Sync_Ostatki_Golovnoy asyncTask2 = new SPR_Ostatok_Golovnoy.MyAsyncTask_Sync_Ostatki_Golovnoy();
                asyncTask2.execute();
                syns_end = false;
            }
            break;
            case R.id.menu_ost_update: {
                String sk, sk_uid, br, gr;
                sk = mass_sp_sklad[spinner_sklad.getSelectedItemPosition()][0];
                sk_uid = mass_sp_sklad[spinner_sklad.getSelectedItemPosition()][1];
                br = mass_sp_brends[spinner_brends.getSelectedItemPosition()];
                if (mass_sp_subBrends.length > 0) {
                    gr = mass_sp_subBrends[spinner_group.getSelectedItemPosition()];
                } else gr = "Все";

                adapter_listview.clear();
                Loading_ListView(sk, sk_uid, br, gr);
                adapterPriceClients = new ListAdapterAde_Otchet_Ostatok(context_Activity, adapter_listview);
                adapterPriceClients.notifyDataSetChanged();
                listView.setAdapter(adapterPriceClients);
                progressBar.setVisibility(View.VISIBLE);
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
            Loading_ListView(ost_sklad, ost_brends, ost_group, ost_uid_skald);

            try {


            } catch (Exception e) {

                Log.e("Err..", "rrr");
            }

            TimeUnit.SECONDS.sleep(5);
            // pDialog.dismiss();
        }
    }

    protected void Loading_Dialog_Message() {
        pDialog = new ProgressDialog(context_Activity);
        pDialog.setMessage("Загрузка файлов с сервера...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setProgress(0);
        pDialog.setMax(100);
        pDialog.show();
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

        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(this_rn_year), Integer.valueOf(this_rn_month) - 1, Integer.valueOf(this_rn_day));
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("dd:MM:yyyy");
        String dateString_NOW = dateFormat1.format(calendar.getTime());
        String dateString_WORK = dateFormat2.format(calendar.getTime());
        String dateString_NOW_Ostatok = dateFormat3.format(calendar.getTime());
        this_data_now = dateString_NOW;
        this_data_work_now = dateString_WORK;
        this_vrema_work_now = this_rn_vrema;
        this_data_now = dateString_NOW_Ostatok + " " + this_rn_vrema;
    }

    //////////////////////////////////////////////////////////////////////////////////  ДЛЯ ФИЛИАЛОВ
    // Обновление базы остатков для филиалов
    private class MyAsyncTask_Update_File_MTW_BIshkek extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();

            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            pDialog.setMessage("Обновление файлов. Подождите...");

            pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                getFloor();  // Синхронизация файлов для всех складов
                publishProgress(1);
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
            Toast.makeText(context_Activity, "Загруженно: " + files_kol + " " + files_size, Toast.LENGTH_SHORT).show();
            pDialog.setProgress(0);
            pDialog.cancel();
            pDialog.dismiss();
        }

        private void getFloor() throws InterruptedException {
            pDialog.setMessage("Обновление файлов. Подождите...");
            try {

                FTPClient ftpClient = new FTPClient();
                FtpConnectData connectData = new FtpConnectData();
                String server = connectData.server_name;
                int port = connectData.port;
                String user = connectData.server_username;
                String pass = connectData.server_password;

                try {
                    ftpClient.connect(server, port);
                    ftpClient.login(user, pass);
                    ftpClient.enterLocalPassiveMode();
                    String remoteDirPath_XML = "/MT_Sunbell_Bishkek/MTW_Data";

                    long[] dirInfo = calculateDirectoryInfo(ftpClient, remoteDirPath_XML, "");
                    System.out.println("Total dirs: " + dirInfo[0]);
                    System.out.println("Total files: " + dirInfo[1]);
                    System.out.println("Total size: " + dirInfo[2]);
                    Log.e("Дирикторий=", " = " + dirInfo[0]);
                    Log.e("Файлов=", " = " + dirInfo[1]);
                    Log.e("Размер=", " = " + dirInfo[2] + " байт");
                    Log.e("Размер=", " = " + dirInfo[2] / 1024 + " кбайт");
                    Log.e("Размер=", " = " + dirInfo[2] / 1048576 + " мбайт");
                    files_kol = dirInfo[1] + "файлов";
                    files_size = dirInfo[2] / 1048576 + " мбайт";

                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(dirInfo[1]));

                    FTPFile[] ftpFiles_list_XML = ftpClient.listFiles(remoteDirPath_XML);
                    for (FTPFile ftpFile_XML : ftpFiles_list_XML) {
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                        String file_server_xml = "/MT_Sunbell_Bishkek/MTW_Data/" + ftpFile_XML.getName(); // путь на сервере
                        if (ftpFile_XML.isFile()) {
                            for (int i = 0; i < mass_update_files.length; i++) {
                                if (ftpFile_XML.getName().equals(mass_update_files[i])) {
                                    String file_db = SPR_Ostatok_Golovnoy.this.getDatabasePath("B_" + mass_update_files[i]).getAbsolutePath(); // путь к databases
                                    // кода для скачивания файла с FTP
                                    outputStream = new FileOutputStream(new File(file_db));  // путь куда сохранить данные
                                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                                    ftpClient.enterLocalPassiveMode();
                                    // ftp: забрать файл из этой директ, в эту директ
                                    ftpClient.retrieveFile(file_server_xml, outputStream);
                                    outputStream.close();
                                }
                            }
                        }
                    }

                    String remoteDirPath_SQL = PEREM_FTP_DISTR_db3;
                    long[] dirInfo2 = calculateDirectoryInfo(ftpClient, remoteDirPath_SQL, "");

                    BigDecimal f2 = new BigDecimal(0.0);
                    BigDecimal pointOne2 = new BigDecimal(100 / Double.valueOf(dirInfo2[1]));


                    FTPFile[] ftpFiles_list_SQL = ftpClient.listFiles(remoteDirPath_SQL);
                    for (FTPFile ftpFile_SQL : ftpFiles_list_SQL) {

                        f2 = f2.add(pointOne2);
                        pDialog.setProgress(f2.intValue());

                        String file_server_sql = remoteDirPath_SQL + "/sunbell_ostatki_filial.db3"; // путь на сервере
                        Log.e("Путь FTP=", remoteDirPath_SQL + "/sunbell_ostatki_filial.db3");

                        if (ftpFile_SQL.isFile()) {
                            String file_db = SPR_Ostatok_Golovnoy.this.getDatabasePath("sunbell_ostatki_filial.db3").getAbsolutePath(); // путь к databases
                            Log.e("Путь DB=", file_db);
                            outputStream = new FileOutputStream(new File(file_db));  // путь куда сохранить данные
                            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                            ftpClient.enterLocalPassiveMode();
                            // ftp: забрать файл из этой директ, в эту директ
                            ftpClient.retrieveFile(file_server_sql, outputStream);
                            outputStream.close();
                        }
                    }


                    ftpClient.logout();
                    ftpClient.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    if (ftpClient.isConnected()) {
                        try {
                            ftpClient.logout();
                            ftpClient.disconnect();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }


            } catch (Exception e) {
                // Toast.makeText(context_Activity, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
            }

        }  // Синхронизация файлов для всех складов

    }

    // Полная синхронизация данных для остатков филиалы
    private class MyAsyncTask_Sync_Ostatki_Golovnoy extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();
            Log.e("Sunc_Start= ", "обработка -> MTW_In_Brends");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            mass = getResources().getStringArray(R.array.mass_all_sync_Bishkek);
            pDialog.setMessage(mass[values[0]] + " \n" + values[0] + "/7 Подождите...");
            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {

                for (int i = 0; i < 8; i++) {
                    switch (i) {
                        case 1: {
                            publishProgress(i);
                            getFloor_MTW_Brends();  // Синхронизация Структуры
                        }
                        break;
                        case 2: {
                            publishProgress(i);
                            getFloor_MTW_In_Nomenclatures(); // Синхронизация номенклатуры
                        }
                        break;
                        case 3: {
                            publishProgress(i);
                            getFloor_Image(); // Синхронизация картинок
                        }
                        break;
                        case 4: {
                            publishProgress(i);
                            getFloor_MTW_In_ResidueGoodsPR(); // Синхронизация остатки
                        }
                        break;
                        case 5: {
                            publishProgress(i);
                            getFloor_MTW_In_Price(); // Синхронизация цены
                        }
                        break;
                        case 6: {
                            publishProgress(i);
                            getFloor_MTW_In_Warehouse(); // Синхронизация складов
                        }
                        break;
                        case 7: {
                            publishProgress(i);
                            getFloor_New_Base(); // Синхронизация цены
                        }
                        break;
                       /* case 8: {
                            publishProgress(i);
                            getFloor_MTW_In_CustomersDebet(); // Синхронизация долгов
                        }
                        break;*/
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
            Log.e("Sunc_End=", "MTW обработаны!");
            Toast.makeText(context_Activity, "Обработка данных окончена!", Toast.LENGTH_LONG).show();
            syns_end = true;
            pDialog.setProgress(0);
            pDialog.dismiss();
        }

        private void getFloor_MTW_Brends() throws InterruptedException {
            pDialog.setMessage("Обработка данных. 1/8 Подождите...");
            SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(BASE_DB_OSTATKI_BISHKEK, MODE_PRIVATE, null);
            db_prev.delete("base_in_brends_id", null, null);
            db_prev.delete("base_in_brends_sub_id", null, null);
            String query = "SELECT * FROM base_in_brends_id;";
            cursor = db_prev.rawQuery(query, null);
            localContentValues_Breds_ID = new ContentValues();
            localContentValues_sub_Breds_ID = new ContentValues();
            cursor.moveToFirst();
            try {
                String file_db = SPR_Ostatok_Golovnoy.this.getDatabasePath("B_MTW_In_Brends.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);

                MTW_SubBrends_ResourceParser parser = new MTW_SubBrends_ResourceParser();
                if (parser.parse(xpp)) {
                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getSubBrends().size()));
                    mass_breds_group = getResources().getStringArray(R.array.mass_breds_group);
                    for (MTW_SubBrends subBrends : parser.getSubBrends()) {

                        if (subBrends.getParents_kod().equals("TRUE")) {
                            grb = "gr_1";
                            localContentValues_Breds_ID.put("brends", subBrends.getName());
                            localContentValues_Breds_ID.put("kod", subBrends.getKod());
                            localContentValues_Breds_ID.put("parent_kod", subBrends.getParents_kod());
                            localContentValues_Breds_ID.put("prefic", subBrends.getPrefic().toLowerCase());
                            for (int i = 0; i < mass_breds_group.length; i++) {
                                if (subBrends.getKod().equals(mass_breds_group[i])) {
                                    grb = "gr_2";
                                }
                            }

                            localContentValues_Breds_ID.put("group_type", grb);
                            db_prev.insert("base_in_brends_id", null, localContentValues_Breds_ID);
                            cursor.moveToNext();

                        } else {
                            localContentValues_sub_Breds_ID.put("subbrends", subBrends.getName());
                            localContentValues_sub_Breds_ID.put("kod", subBrends.getKod());
                            localContentValues_sub_Breds_ID.put("parent_kod", subBrends.getParents_kod());
                            db_prev.insert("base_in_brends_sub_id", null, localContentValues_sub_Breds_ID);
                            cursor.moveToNext();
                        }

                        Log.e("B_Brends..", subBrends.getName() + " ." + subBrends.getPrefic() + "");
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());

                    }
                }
                cursor.close();
                db_prev.close();
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации структуры!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации структуры!");
            }

        }   // Группы

        private void getFloor_MTW_In_Nomenclatures() throws InterruptedException {

            SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(BASE_DB_OSTATKI_BISHKEK, MODE_PRIVATE, null);
            db_prev.delete("base_in_nomeclature", null, null);
            String query = "SELECT * FROM base_in_nomeclature;";
            cursor = db_prev.rawQuery(query, null);
            localContentValues = new ContentValues();
            cursor.moveToFirst();
            try {
                String file_db = SPR_Ostatok_Golovnoy.this.getDatabasePath("B_MTW_In_Nomenclatures.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы

                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                MTW_Nomenclatures_ResourceParser parser = new MTW_Nomenclatures_ResourceParser();
                parserFactory.setNamespaceAware(true);
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                if (parser.parse(xpp)) {
                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getTovar().size()));
                    for (MTW_Nomenclatures tovar : parser.getTovar()) {

                        /*if (!tovar.getBrends().equals("")) {

                        } else {
                            cursor.moveToNext();
                        }*/
                        localContentValues.put("brends", tovar.getBrends());
                        localContentValues.put("p_group", tovar.getP_group());
                        localContentValues.put("kod", tovar.getKod());
                        localContentValues.put("name", tovar.getName());
                        localContentValues.put("kolbox", tovar.getKolbox());
                        localContentValues.put("cena", tovar.getCena());
                        localContentValues.put("strih", tovar.getStrih());
                        localContentValues.put("koduid", tovar.getKoduid().trim());

                        if (tovar.getKod_univ().length() > 25) {
                            localContentValues.put("kod_univ", tovar.getKod_univ());
                            db_prev.insert("base_in_nomeclature", null, localContentValues);
                            Log.e("B_Tovar..", tovar.getBrends() + " " + tovar.getP_group() + "");
                            f1 = f1.add(pointOne);
                            pDialog.setProgress(f1.intValue());
                            cursor.moveToNext();
                        } else cursor.moveToNext();


                       /* 0A117DC7-93F0-4D61-9DDF-72CCDD39B1C6
                        4163C3D7-9757-4F90-9F9B-389B0F2213E8    *
                        00000001/00000238/BL000606*/







                       /* if (!tovar.getBrends().equals("")) {
                            localContentValues.put("subbrends", subBrends.getSubbrends());
                            localContentValues.put("kod", subBrends.getKod());
                            localContentValues.put("parents_kod", subBrends.getParents_kod());
                            db_prev.insert("base8_Group_ID", null, localContentValues);
                            Log.e("UIDYY..", subBrends.getSubbrends() + " ." + subBrends.getParents_kod() + "");
                            f1 = f1.add(pointOne);
                            pDialog.setProgress(f1.intValue());
                            cursor.moveToNext();
                        } else {
                            localContentValues.put("subbrends", subBrends.getSubbrends());
                            localContentValues.put("kod", subBrends.getKod());
                            localContentValues.put("parents_kod", subBrends.getParents_kod());
                            db_prev.insert("base7_Brends_ID", null, localContentValues);
                            Log.e("TRUE..", subBrends.getSubbrends() + " ." + subBrends.getParents_kod() + "");
                            cursor.moveToNext();
                        }*/


                    }
                }
                TimeUnit.SECONDS.sleep(1);
                cursor.close();
                db_prev.close();

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации номенклатуры!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации номенклатуры!");
            }

        }

        private void getFloor_Image() throws InterruptedException {
            try {
                SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(BASE_DB_OSTATKI_BISHKEK, MODE_PRIVATE, null);
                db_prev.delete("base_in_image", null, null);

                String query = "SELECT base_in_nomeclature.name, base_in_nomeclature.koduid," +
                        "base_in_nomeclature.kod,   lower(base_in_brends_id.prefic || '_' || base_in_nomeclature.kod) AS 'kod_image' \n" +
                        "FROM base_in_nomeclature\n" +
                        "LEFT JOIN base_in_brends_id ON base_in_brends_id.kod=substr(base_in_nomeclature.kod_univ, 1, 8)" +
                        "WHERE kod_image > 0";
                cursor = db_prev.rawQuery(query, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();
                BigDecimal f1 = new BigDecimal(0.0);
                BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(cursor.getCount()));
                while (cursor.isAfterLast() == false) {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String koduid = cursor.getString(cursor.getColumnIndex("koduid"));
                    String kod = cursor.getString(cursor.getColumnIndex("kod"));
                    String kod_image = cursor.getString(cursor.getColumnIndex("kod_image"));
                    localContentValues.put("name", name);
                    localContentValues.put("koduid", koduid);
                    localContentValues.put("kod", kod);
                    localContentValues.put("kod_image", kod_image.replace('ц', 'c'));
                    db_prev.insert("base_in_image", null, localContentValues);
                    cursor.moveToNext();
                    // Log.e("UIDYY..", name + " ." + kod_image);
                    f1 = f1.add(pointOne);
                    pDialog.setProgress(f1.intValue());
                }
                TimeUnit.SECONDS.sleep(1);
                cursor.close();
                db_prev.close();

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации складов!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации складов!");
            }

        }

        private void getFloor_MTW_In_ResidueGoodsPR() throws InterruptedException {
            try {
                SQLiteDatabase db = getBaseContext().openOrCreateDatabase(BASE_DB_OSTATKI_BISHKEK, MODE_PRIVATE, null);
                db.delete("base_in_ostatok", null, null);
                String query_up = "SELECT * FROM base_in_ostatok";
                cursor = db.rawQuery(query_up, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();
                String file_db = SPR_Ostatok_Golovnoy.this.getDatabasePath("B_MTW_In_ResidueGoodsPR.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                MTW_Ostatok_ResourceParser parser = new MTW_Ostatok_ResourceParser();
                int eventType = xpp.getEventType();
                if (parser.parse(xpp)) {

                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getOstatok().size()));


                    for (MTW_Ostatok ostatok : parser.getOstatok()) {
                        localContentValues.put("data", ostatok.getData());
                        localContentValues.put("sklad_uid", ostatok.getSklad_uid());
                        localContentValues.put("nomenclature_uid", ostatok.getName_uid());
                        localContentValues.put("name", ostatok.getName());
                        localContentValues.put("count", ostatok.getCount());
                        db.insert("base_in_ostatok", null, localContentValues);
                        Log.e("SYNC..", ostatok.getData() + " " + ostatok.getName() + " " + ostatok.getCount());
                        cursor.moveToNext();
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                    }

                    cursor.close();
                    db.close();
                }

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации остатков!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации остатков!");
            }

        }

        private void getFloor_MTW_In_Price() throws InterruptedException {
            try {
                SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(BASE_DB_OSTATKI_BISHKEK, MODE_PRIVATE, null);
                db_prev.delete("base_in_price", null, null);
                String query = "SELECT * FROM base_in_price;";
                cursor = db_prev.rawQuery(query, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();

                String file_db = SPR_Ostatok_Golovnoy.this.getDatabasePath("B_MTW_In_Price.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                MTW_Price_ResourceParser parser = new MTW_Price_ResourceParser();
                int eventType = xpp.getEventType();
                if (parser.parse(xpp)) {
                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getPrice().size()));
                    for (MTW_Price price : parser.getPrice()) {
                        Log.e("UIDYY..", price.getUid() + " Cena.." + price.getCena());
                        localContentValues.put("nomenclature_uid", price.getUid());
                        localContentValues.put("price", price.getCena());
                        db_prev.insert("base_in_price", null, localContentValues);
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                        cursor.moveToNext();
                    }
                }

                TimeUnit.SECONDS.sleep(1);
                cursor.close();
                db_prev.close();

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации цен!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации цен!");
            }

        }

        private void getFloor_New_Base() throws InterruptedException {
            try {
                SQLiteDatabase db = getBaseContext().openOrCreateDatabase(BASE_DB_OSTATKI_BISHKEK, MODE_PRIVATE, null);
                db.delete("base_in_ostatok_golovnoy", null, null);

                String query_up = "SELECT base_in_nomeclature.brends, base_in_nomeclature.p_group, " +
                        "base_in_nomeclature.name, base_in_ostatok.count, base_in_price.price, \n" +
                        "base_in_image.kod_image, base_in_ostatok.sklad_uid\n" +
                        "FROM base_in_nomeclature\n" +
                        "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                        "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                        "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                        "WHERE base_in_ostatok.count >0";

                cursor = db.rawQuery(query_up, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();
                BigDecimal f1 = new BigDecimal(0.0);
                BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(cursor.getCount()));

                while (cursor.isAfterLast() == false) {
                    localContentValues.put("brends", cursor.getString(cursor.getColumnIndex("brends")));
                    localContentValues.put("p_brends", cursor.getString(cursor.getColumnIndex("p_group")));
                    localContentValues.put("name", cursor.getString(cursor.getColumnIndex("name")));
                    localContentValues.put("count", cursor.getString(cursor.getColumnIndex("count")));
                    localContentValues.put("price", cursor.getString(cursor.getColumnIndex("price")));
                    localContentValues.put("kod_image", cursor.getString(cursor.getColumnIndex("kod_image")));
                    localContentValues.put("sklad_uid", cursor.getString(cursor.getColumnIndex("sklad_uid")));
                    db.insert("base_in_ostatok_golovnoy", null, localContentValues);
                    cursor.moveToNext();

                    f1 = f1.add(pointOne);
                    pDialog.setProgress(f1.intValue());
                }
                cursor.close();
                db.close();

                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации цен!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации цен!");
            }

        }

        private void getFloor_MTW_In_Warehouse() throws InterruptedException {
            try {
                SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(BASE_DB_OSTATKI_BISHKEK, MODE_PRIVATE, null);
                db_prev.delete("base_in_sklad", null, null);
                String query = "SELECT * FROM base_in_sklad;";
                cursor = db_prev.rawQuery(query, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();

                String file_db = SPR_Ostatok_Golovnoy.this.getDatabasePath("MTW_In_Warehouse.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                MTW_Skald_ResourceParser parser = new MTW_Skald_ResourceParser();
                if (parser.parse(xpp)) {
                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getSklads().size()));
                    for (MTW_Sklad sklad : parser.getSklads()) {

                        localContentValues.put("sklad_uid", sklad.getSklad_uid());
                        localContentValues.put("sklad_name", sklad.getSklad_name());
                        db_prev.insert("base_in_sklad", null, localContentValues);
                        cursor.moveToNext();
                        Log.e("Sklads..", sklad.getSklad_name() + " ." + sklad.getSklad_uid() + "");
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                    }
                }

                TimeUnit.SECONDS.sleep(1);
                cursor.close();
                db_prev.close();
            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации складов!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации складов!");
            }

        }  // Склады

    }


    protected void SYNC() {
        // Toast.makeText(context_Activity, which, Toast.LENGTH_SHORT).show();
        //Обновление файлов: "Все файлы XML"


        Calendate_New();


        try {

        } catch (Exception e) {
            Log.e("Pref", "Ошибка: Ежедневное обновление!");
            Toast.makeText(context_Activity, "Ошибка: Ежедневное обновление!", Toast.LENGTH_SHORT).show();

        }
    }

}
