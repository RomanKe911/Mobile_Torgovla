package kg.roman.Mobile_Torgovla.Permission;

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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kg.roman.Mobile_Torgovla.FTP.FtpAction;
import kg.roman.Mobile_Torgovla.FTP.FtpConnection;
import kg.roman.Mobile_Torgovla.FTP.ListAdapterAde_Pref_Ftp;
import kg.roman.Mobile_Torgovla.FTP.ListAdapterSimple_Pref_Ftp;

import kg.roman.Mobile_Torgovla.DB_NewSV.DbContract_GroupID;
import kg.roman.Mobile_Torgovla.DB_NewSV.DbContract_Image;
import kg.roman.Mobile_Torgovla.DB_NewSV.DbContract_Nomeclature;
import kg.roman.Mobile_Torgovla.DB_NewSV.DbContract_Ostatok;
import kg.roman.Mobile_Torgovla.FTP.FtpAsyncTask_Single_Conn_AllDB;
import kg.roman.Mobile_Torgovla.FTP.FtpAsyncTask_Single_Conn_UpServ;
import kg.roman.Mobile_Torgovla.R;


/**
 * Created by Roman on 06.03.2017.
 */
public class PrefActivity extends PreferenceActivity {

    public ArrayList<ListAdapterSimple_Pref_Ftp> ftp_connect = new ArrayList<ListAdapterSimple_Pref_Ftp>();
    public ListAdapterAde_Pref_Ftp adapterPriceClients;
    CheckBoxPreference chb1, chb2, chb3;
    PreferenceCategory categ2;

    public ListPreference
            listPreference_all1, listPreference_all2,
            listPreference_prod1, listPreference_prod2,
            listPreference_rens1, listPreference_rens2, listPreference_ftp;
    public Preference preference_up1, preference_up2,
            preference_up3, preference_up4, preference_up5,
            preference_ver, preference_phone, button_UP_Ostatki, button_UP_Price, button_Db,
            preference_youvers, preference_update, preference_progrmmer,
            preference_server_port, button_image, button_Nomeclature, button_Ostatok, button_Brends, button_RN, button_SOS;
    public SwitchPreference switchPreference_ost, switchPreference_bag, switchPreference_ostatok;
    public ProgressDialog pDialog;
    public SharedPreferences.Editor ed;
    public SharedPreferences sp;
    public String ftp_con_serv, ftp_con_login, ftp_con_pass, ftp_con_put;
    //  public StartProgressDialog mt;
   // public String ftp_put, ftp_server, ftp_login, ftp_password, ftp_port;
    public String[] array_params;
    public String[] array_region;

    public String[] mass_SK, mass_brends_price, mass_simvol_kod, mass_brends;
    public String[] massD;
    public String perem_1;
    public String[][] mass_Sk1, mass_Sk2, mass_Sk3, mass_Sk4, mass_Sk5, mass_all;
    public EditTextPreference preference_server_name, preference_server_login,
            preference_server_password;

    public Integer params;
    public Integer Summa_ost, ost, count;
    public Calendar localCalendar = Calendar.getInstance();
    public Integer thisdata, thismonth, thisyear, thisminyte, thishour, thissecond;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now;

    public Context context_Activity;
    public String mass[][];
    public String mass_ID[][];
    //  public Cursor cursor_ID;
    public Cursor cursor_ID, cursor;
    public ContentValues localContentValues;
    public String table_name, newName, brends;
    public int count_k;
    int[] integers = null;
    public int max_count = 0;
    public int counter = 0;
    public Double perm, summa;
    public String version_nomer, name_pack;
    public Integer version_code, version_release;

    public String PEREM_Agent, PEREM_KAgent, PEREM_KAgent_UID, PEREM_Vrema, PEREM_Data, PEREM_RNKod, PEREM_TYPE;
    public String DATABASE_NAME_MONTH, DATABASE_NAME_DATA, BaseTableName_Reg;
    public String PARAMS_KON_NAME, PARAMS_KON_KOD, PARAMS_KOD_SUMMA, REGIONS, DB_UP, PARAMS_KOD_DATA_UP, NAME_AGENT, NAME_KLIENTS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
        context_Activity = PrefActivity.this;
        ftp_con_serv = "176.123.246.244";
        ftp_con_login = "sunbell_siberica";
        ftp_con_pass = "Roman911NFS";





        chb1 = (CheckBoxPreference) findPreference("chb1");
        chb2 = (CheckBoxPreference) findPreference("chb2");
        chb3 = (CheckBoxPreference) findPreference("chb3");
        listPreference_all1 = (ListPreference) findPreference("list_all_nal");
        listPreference_all2 = (ListPreference) findPreference("list_all_kons");
        listPreference_prod1 = (ListPreference) findPreference("list_splat_nal");
        listPreference_prod2 = (ListPreference) findPreference("list_splat_kons");
        listPreference_rens1 = (ListPreference) findPreference("list_rens_nal");
        listPreference_rens2 = (ListPreference) findPreference("list_rens_kons");
        listPreference_ftp = (ListPreference) findPreference("list_preference_ftp");

        preference_up2 = (Preference) findPreference("buttonkey_2");
        preference_up3 = (Preference) findPreference("buttonkey_3");
        preference_up4 = (Preference) findPreference("buttonkey_4");
        preference_up5 = (Preference) findPreference("buttonkey_5");


        button_image = (Preference) findPreference("button_image_up");
        button_Db = (Preference) findPreference("button_db");
        button_RN = (Preference) findPreference("button_rn");
        button_SOS = (Preference) findPreference("button_sos");
        button_Nomeclature = (Preference) findPreference("button_nomeclt");
        button_Ostatok = (Preference) findPreference("button_ostatok");
        button_Brends = (Preference) findPreference("button_brends");
        button_UP_Price = (Preference) findPreference("button_price");
        button_UP_Ostatki = (Preference) findPreference("button_ostatki");
        switchPreference_ost = (SwitchPreference) findPreference("switch_preference_1");
        switchPreference_bag = (SwitchPreference) findPreference("switch_preference_2");
        switchPreference_ostatok = (SwitchPreference) findPreference("switch_visible_ostatok");

        BuildConfig_This();


        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        ed = sp.edit();
        DATABASE_NAME_MONTH = sp.getString("DATABASE_NAME_MONTH", "0");
        DATABASE_NAME_DATA = sp.getString("DATABASE_NAME_DATA", "0");
        BaseTableName_Reg = sp.getString("REGIONS", "0");
        PARAMS_KON_NAME = sp.getString("PARAMS_KON_NAME", "0");
        PARAMS_KON_KOD = sp.getString("PARAMS_KON_KOD", "0");
        PARAMS_KOD_SUMMA = sp.getString("PARAMS_KOD_SUMMA", "0");
        REGIONS = sp.getString("REGIONS", "0");
        PARAMS_KOD_DATA_UP = sp.getString("PARAMS_KOD_DATA_UP", "0");
        NAME_AGENT = sp.getString("NAME_AGENT", "0");
        PEREM_Agent = sp.getString("PEREM_Agent", "0");
        DB_UP = "base_" + REGIONS + "_rn_data.db3";
        Log.e("Constant=", REGIONS);

        Calendare_This_Data();
        Calendate_New();
        Update_Date_Summary();

        switchPreference_ost.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (switchPreference_ost.isChecked()) {
                    // switchPreference_ost.setSummaryOn("полный список");
                    switchPreference_bag.setChecked(false);
                    Log.e("PrfAct_SkladUp=", "On");
                    ed.putBoolean("SwitchPreference_ost", true);
                } else {
                    // switchPreference_ost.setSummaryOff("только остатки");
                    switchPreference_bag.setChecked(true);
                    Log.e("PrfAct_SkladUp=", "Off");
                    ed.putBoolean("SwitchPreference_ost", false);
                }
                return false;
            }
        });

        switchPreference_ostatok.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (switchPreference_ostatok.isChecked()) {
                    Log.e("SWPR_Ostatok=", "On");
                    switchPreference_ostatok.setSummary("только остатки");
                    ed.putBoolean("SwitchPreference_ost", true);
                    ed.commit();


                } else {
                    Log.e("SWPR_Ostatok=", "Off");
                    switchPreference_ostatok.setSummary("полная номеклатура");
                    ed.putBoolean("SwitchPreference_ost", false);
                    ed.commit();
                }
                return false;
            }
        });




        // Заполнение данными
        button_Db.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    Calendate_New();

                    button_Db.setSummary(this_data_now);
                    ed.putString("Data_update_db_nomkl", button_Db.getSummary().toString()); // передача даты обновления
                    ed.putString("Data_update_ftp_out", "suncape_all_db.db3"); // передача имя файла для отправки
                    ed.commit();
                    Loading_All_DB(); // Обновление базы номенклатуры
                    Log.e("PrfAct", "Обновление завершенно!");
                }
                catch (Exception e)
                {
                    Log.e("PrfAct", getClass().toString() + "Ошибка в коде!, Загрузка базы номнкл");
                }

                return false;
            }
        });

        button_RN.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                massD = new String[2];
                massD[0] = sp.getString("Data_ftp_out_1", "0");
                massD[1] = sp.getString("Data_ftp_out_2", "0");
                Log.e("PrfAct", massD[0] + " " + massD[1]);
                return false;
            }
        });

        // Заполнение данными
        button_SOS.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    Calendate_New();
                    Constant();
                    button_SOS.setSummary(this_data_now);
                    ed = sp.edit();
                    ed.putString("Data_ftp_out_1", "suncape_all_db.db3"); // передача имя файла для отправки
                    ed.putString("Data_ftp_out_2", "suncape_rn_db.db3"); // передача имя файла для отправки
                    ed.putString("Data_ftp_out_3", PEREM_Agent); // передача имя файла для отправки
                    ed.putString("Data_update_sos", button_SOS.getSummary().toString()); // передача имя файла для отправки
                    ed.commit();
                    Loading_SOS();
                    Log.e("PrfAct", "Обновление завершенно!"+PEREM_Agent);
                }
                catch (Exception e)
                {
                    Log.e("PrfAct", getClass().toString() + "Ошибка в коде!, Резерв");
                }

                return false;
            }
        });



        button_image.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    Calendate_New();
                    ListView_Work_Table();
                    ProgressDialog_massage();
                    // ListView_Adapter_Image();
                    ed = sp.edit();
                    button_image.setSummary(this_data_now);
                    ed.putString("Data_update_image", button_image.getSummary().toString()); // передача даты обновления
                    ed.commit();
                    Log.e("PrfAct", "Обновление завершенно!");


                } catch (Exception e) {
                    Log.e("PrefActivity=", getClass().toString() + "Ошибка в коде!");
                }

                return false;
            }
        });

        button_Nomeclature.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                ListView_Adapter_Nomeclature_Work_Table();
                ListView_Adapter_Nomeclature();
                return false;
            }
        });

        button_Ostatok.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    ListView_Adapter_Combine_Sklad();
                } catch (Exception e) {
                    Log.e("PrfAct_SkladUp=", getClass().toString() + "Ошибка в коде!");
                }
                return false;
            }
        });

        button_Brends.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    ListView_Adapter_Nomeclature_Next();
                } catch (Exception e) {
                    Log.e("PrfAct_Brends=", getClass().toString() + "Ошибка в коде!");
                }

                return false;
            }
        });

        button_UP_Price.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    ListView_Adapter_Update_Price();
                    ListView_Adapter_Update_Sklad();
                } catch (Exception e) {
                    Log.e("PrfAct_SkladUp=", getClass().toString() + "Ошибка в коде!");
                }
                return false;
            }
        });

        button_UP_Ostatki.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    ListView_Adapter_Update_Sklad();
                } catch (Exception e) {
                   Log.e("PrfAct_SkladUp=", getClass().toString() + "Ошибка в коде!");
                }
                return false;
            }
        });

        //    button_image = (Preference) findPreference("button_image");

        preference_ver = (Preference) findPreference("version");
        preference_phone = (Preference) findPreference("phone");
        preference_youvers = (Preference) findPreference("you_version");
        preference_update = (Preference) findPreference("update");
        preference_progrmmer = (Preference) findPreference("programmer");

        preference_server_name = (EditTextPreference) findPreference("button_server");
        preference_server_port = (Preference) findPreference("button_port");
        preference_server_login = (EditTextPreference) findPreference("button_login");
        preference_server_password = (EditTextPreference) findPreference("button_password");


        listPreference_all1.setEntries(R.array.list_ty_entries);
        listPreference_all1.setEntryValues(R.array.list_ty_entryValues);
        listPreference_all2.setEntries(R.array.list_ty_entries);
        listPreference_all2.setEntryValues(R.array.list_ty_entryValues);

        listPreference_prod1.setEntries(R.array.ty_name2);
        listPreference_prod1.setEntryValues(R.array.ty_params2);
        listPreference_prod2.setEntries(R.array.ty_name2);
        listPreference_prod2.setEntryValues(R.array.ty_params2);


        chb1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (chb1.isChecked()) {
                    listPreference_all1.setEntries(R.array.list_ty_entries);
                    listPreference_all1.setEntryValues(R.array.list_ty_entryValues);
                    listPreference_all2.setEntries(R.array.list_ty_entries);
                    listPreference_all2.setEntryValues(R.array.list_ty_entryValues);
                } else {
                    listPreference_all1.setEntries(R.array.no_ty1);
                    listPreference_all1.setEntryValues(R.array.no_ty2);
                    listPreference_all1.setValueIndex(0);
                    listPreference_all2.setEntries(R.array.no_ty1);
                    listPreference_all2.setEntryValues(R.array.no_ty2);
                    listPreference_all2.setValueIndex(0);
                }
                return false;
            }
        });

        chb2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (chb2.isChecked()) {
                    listPreference_prod1.setEntries(R.array.ty_name2);
                    listPreference_prod1.setEntryValues(R.array.ty_params2);
                    listPreference_prod2.setEntries(R.array.ty_name2);
                    listPreference_prod2.setEntryValues(R.array.ty_params2);
                } else {
                    listPreference_prod1.setEntries(R.array.no_ty1);
                    listPreference_prod1.setEntryValues(R.array.no_ty2);
                    listPreference_prod1.setValueIndex(0);
                    listPreference_prod2.setEntries(R.array.no_ty1);
                    listPreference_prod2.setEntryValues(R.array.no_ty2);
                    listPreference_prod2.setValueIndex(0);
                }
                return false;
            }
        });
        PacksInfo();
        preference_ver.setSummary("Nomer: " + version_nomer + ", " + "Pack: " + name_pack);

        preference_youvers.setSummary("API: " + Build.VERSION.SDK_INT + "\n"
                + "Android: " + Build.VERSION.RELEASE + "\n"
                + "Brand: " + Build.BRAND + "\n"
                + "Model: " + Build.MODEL);

        preference_phone.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + preference_phone.getSummary() + ""));
                startActivity(intent);
                return false;
            }
        });

        array_region = getResources().getStringArray(R.array.mass_regions);
        array_params = getResources().getStringArray(R.array.params_region);
        // sp = PreferenceManager.getDefaultSharedPreferences(this);
        for (int i = 1; i < 3; i++) {
            if (sp.getString("programmer", "0").equals(array_region[i])) {
                preference_progrmmer.setSummary(array_params[i]);
            }
        }
       // ListPtreference_FTP();
        adapterPriceClients = new ListAdapterAde_Pref_Ftp(context_Activity, ftp_connect);



        listPreference_ftp.setEntryValues(R.array.mass_regions);


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
        calendar.set(Integer.parseInt(this_rn_year), Integer.valueOf(this_rn_month)-1, Integer.valueOf(this_rn_day));
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd:MM:yyyy");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateString_NOW = dateFormat1.format(calendar.getTime());
        String dateString_WORK = dateFormat2.format(calendar.getTime());
        this_data_now = dateString_NOW+" "+this_rn_vrema;
        Log.e("WJ_FormaL2:", "!DataStart:" + dateString_NOW);
        //  Log.e("WJ_FormaL2:", "!DataEnd:" + dateString2);

    } // Загрузка даты и время

    protected void ListView_Adapter_Image() {

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
        String query_ID = "SELECT " + DbContract_GroupID.TableUser._ID + ", "
                + DbContract_GroupID.TableUser.COLUMN_BRENDS + ", "
                + DbContract_GroupID.TableUser.COLUMN_PER_KOD + ", "
                + DbContract_GroupID.TableUser.COLUMN_PREF + ", "
                + DbContract_GroupID.TableUser.COLUMN_WORK + ", "
                + DbContract_GroupID.TableUser.COLUMN_GR_TYPE + " FROM base7_Brends_ID";

        cursor_ID = db.rawQuery(query_ID, null);
        mass_ID = new String[cursor_ID.getCount()][5];

        cursor_ID.moveToFirst();
        while (cursor_ID.isAfterLast() == false) {
            String brends = cursor_ID.getString(cursor_ID.getColumnIndexOrThrow(DbContract_GroupID.TableUser.COLUMN_BRENDS));
            String kod = cursor_ID.getString(cursor_ID.getColumnIndexOrThrow(DbContract_GroupID.TableUser.COLUMN_PER_KOD));
            String pref = cursor_ID.getString(cursor_ID.getColumnIndexOrThrow(DbContract_GroupID.TableUser.COLUMN_PREF));
            String work = cursor_ID.getString(cursor_ID.getColumnIndexOrThrow(DbContract_GroupID.TableUser.COLUMN_WORK));
            mass_ID[cursor_ID.getPosition()][2] = kod;
            mass_ID[cursor_ID.getPosition()][3] = pref;
            mass_ID[cursor_ID.getPosition()][4] = work;
            Log.e("Do=", "Загрузка картинок");
            cursor_ID.moveToNext();
        }
        cursor_ID.close();


        for (int i = 0; i < mass_ID.length; i++) {
            String query = " SELECT base1_v8.koduid, base1_v8.kod, base1_v8.name, base2_sveta.kod_univ FROM base1_v8 " +
                    "LEFT JOIN base2_sveta ON base1_v8.name = base2_sveta.name " +
                    "WHERE base2_sveta.kod_univ LIKE '" + mass_ID[i][2] + "%'";
            cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            localContentValues = new ContentValues();
            while (cursor.isAfterLast() == false) {
                String koduid = cursor.getString(cursor.getColumnIndexOrThrow(DbContract_Image.TableUser.COLUMN_KODUID));
                String kod = cursor.getString(cursor.getColumnIndexOrThrow(DbContract_Image.TableUser.COLUMN_KOD));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DbContract_Image.TableUser.COLUMN_NAME));
                if (mass_ID[i][4].equals("true")) {
                    //  String image = cursor.getString(cursor.getColumnIndex("base2"));

                    localContentValues.put(DbContract_Image.TableUser.COLUMN_KODUID, koduid);
                    localContentValues.put(DbContract_Image.TableUser.COLUMN_KOD, kod);
                    localContentValues.put(DbContract_Image.TableUser.COLUMN_NAME, name);
                    if (kod.contains("Ц")) {
                        newName = "";
                        newName = mass_ID[i][3] + "_c" + kod.substring(1, kod.length());
                        localContentValues.put(DbContract_Image.TableUser.COLUMN_IMAGE, newName);
                        db.insert("base6_image", null, localContentValues);
                        Log.d("New String", "ОБНОВЛЕННО " + newName);
                    } else if (kod.contains("С")) {
                        newName = "";
                        newName = mass_ID[i][3] + "_ck" + kod.substring(2, kod.length());
                        localContentValues.put(DbContract_Image.TableUser.COLUMN_IMAGE, newName);
                        db.insert("base6_image", null, localContentValues);
                        Log.d("New String", "ОБНОВЛЕННО " + newName);
                    } else if (kod.contains("К")) {
                        newName = "";
                        newName = mass_ID[i][3] + "_kp" + kod.substring(2, kod.length());
                        localContentValues.put(DbContract_Image.TableUser.COLUMN_IMAGE, newName);
                        db.insert("base6_image", null, localContentValues);
                        Log.d("New String", "ОБНОВЛЕННО " + newName);
                    } else if (kod.contains("Р")) {
                        newName = "";
                        newName = mass_ID[i][3] + "_pk" + kod.substring(2, kod.length());
                        localContentValues.put(DbContract_Image.TableUser.COLUMN_IMAGE, newName);
                        db.insert("base6_image", null, localContentValues);
                        Log.d("New String", "ОБНОВЛЕННО " + newName);
                    } else {
                        newName = "";
                        newName = mass_ID[i][3] + "_" + kod.toLowerCase();
                        localContentValues.put(DbContract_Image.TableUser.COLUMN_IMAGE, newName);
                        db.insert("base6_image", null, localContentValues);
                        Log.d("New String", "ОБНОВЛЕННО " + newName);
                    }
                    Log.e("mas1=", mass_ID[i][2] + ", " + name + ", Image=" + newName);
                    Log.e("mas2=", mass_ID[i][3] + ", " + name + ", Image=" + newName);
                    cursor.moveToNext();
                } else {
                    Log.e("mas1=", mass_ID[i][2] + ", " + name + ", Image=" + newName);
                    Log.e("mas2=", mass_ID[i][3] + ", " + name + ", Image=" + newName);
                    cursor.moveToNext();
                }
            }
        }
        cursor.close();
        db.close();
    } // Обновление таблица с кодом для Image

    protected void ListView_Work_Table() {

        SQLiteDatabase db_sqlite = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);

        final String DELETE_TABLE = "DROP TABLE base6_image";
        db_sqlite.execSQL(DELETE_TABLE);
        Log.e("ListView_Adapter_Image=", "Таблица base6_image удалена!");

        final String CREATE_TABLE = "CREATE TABLE if not exists base6_image" +
                "(" + DbContract_GroupID.TableUser._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + DbContract_Image.TableUser.COLUMN_KODUID + " TEXT, "
                + DbContract_Image.TableUser.COLUMN_KOD + " TEXT, "
                + DbContract_Image.TableUser.COLUMN_NAME + " TEXT, "
                + DbContract_Image.TableUser.COLUMN_IMAGE + " TEXT, " +
                "FOREIGN KEY (name)  REFERENCES base1_v8 (name))";

        Log.e("ListView_Adapter_Image=", "Таблица base6_image создана!");
        db_sqlite.execSQL(CREATE_TABLE);
        db_sqlite.close();
    }  // Очистка таблицы и создание раздела заново

    protected void ProgressDialog_massage() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Загрузка продуктов. Подождите...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setProgress(0);
        pDialog.setMax(100);
        pDialog.show();


        new Thread(new Runnable() {
            @Override
            public void run() {

                SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
                String query_ID = "SELECT " + DbContract_GroupID.TableUser.COLUMN_BRENDS + ", "
                        + DbContract_GroupID.TableUser.COLUMN_PER_KOD + ", "
                        + DbContract_GroupID.TableUser.COLUMN_PREF + ", "
                        + DbContract_GroupID.TableUser.COLUMN_WORK + ", "
                        + DbContract_GroupID.TableUser.COLUMN_GR_TYPE + " FROM base7_Brends_ID";

                cursor_ID = db.rawQuery(query_ID, null);
                mass_ID = new String[cursor_ID.getCount()][5];

                max_count = cursor_ID.getCount();
                perm = Double.valueOf(100 / max_count);
                summa = 0.0;

                cursor_ID.moveToFirst();
                while (cursor_ID.isAfterLast() == false) {
                    String brends = cursor_ID.getString(cursor_ID.getColumnIndexOrThrow(DbContract_GroupID.TableUser.COLUMN_BRENDS));
                    String kod = cursor_ID.getString(cursor_ID.getColumnIndexOrThrow(DbContract_GroupID.TableUser.COLUMN_PER_KOD));
                    String pref = cursor_ID.getString(cursor_ID.getColumnIndexOrThrow(DbContract_GroupID.TableUser.COLUMN_PREF));
                    String work = cursor_ID.getString(cursor_ID.getColumnIndexOrThrow(DbContract_GroupID.TableUser.COLUMN_WORK));
                    mass_ID[cursor_ID.getPosition()][2] = kod;
                    mass_ID[cursor_ID.getPosition()][3] = pref;
                    mass_ID[cursor_ID.getPosition()][4] = work;
                    Log.e("Do=", "Загрузка картинок");
                    cursor_ID.moveToNext();
                }
                cursor_ID.close();

                for (int i = 0; i < mass_ID.length; i++) {
                    String query = " SELECT base1_v8.koduid, base1_v8.kod, base1_v8.name, base2_sveta.kod_univ FROM base1_v8 " +
                            "LEFT JOIN base2_sveta ON base1_v8.name = base2_sveta.name " +
                            "WHERE base2_sveta.kod_univ LIKE '" + mass_ID[i][2] + "%'";
                    cursor = db.rawQuery(query, null);
                    cursor.moveToFirst();
                    localContentValues = new ContentValues();

                    while (cursor.isAfterLast() == false) {
                        String koduid = cursor.getString(cursor.getColumnIndex(DbContract_Image.TableUser.COLUMN_KODUID));
                        String kod = cursor.getString(cursor.getColumnIndex(DbContract_Image.TableUser.COLUMN_KOD));
                        String name = cursor.getString(cursor.getColumnIndex(DbContract_Image.TableUser.COLUMN_NAME));
                        if (mass_ID[i][4].equals("true")) {
                            localContentValues.put(DbContract_Image.TableUser.COLUMN_KODUID, koduid);
                            localContentValues.put(DbContract_Image.TableUser.COLUMN_KOD, kod);
                            localContentValues.put(DbContract_Image.TableUser.COLUMN_NAME, name);
                            String prf = kod.substring(0, 1);
                            if (kod.contains("Ц")) {
                                newName = "";
                                newName = mass_ID[i][3] + "_c" + kod.substring(1, kod.length());
                                localContentValues.put(DbContract_Image.TableUser.COLUMN_IMAGE, newName);
                                db.insert("base6_image", null, localContentValues);
                                Log.d("New String", "ОБНОВЛЕННО " + newName);
                            } else if (kod.contains("С")) {
                                newName = "";
                                newName = mass_ID[i][3] + "_ck" + kod.substring(2, kod.length());
                                localContentValues.put(DbContract_Image.TableUser.COLUMN_IMAGE, newName);
                                db.insert("base6_image", null, localContentValues);
                                Log.d("New String", "ОБНОВЛЕННО " + newName);
                            } else if (prf.contains("К")) {
                                newName = "";
                                newName = mass_ID[i][3] + "_kp" + kod.substring(2, kod.length());
                                localContentValues.put(DbContract_Image.TableUser.COLUMN_IMAGE, newName);
                                db.insert("base6_image", null, localContentValues);
                                Log.d("New String", "ОБНОВЛЕННО " + newName);
                            } else if (prf.contains("Р")) {
                                newName = "";
                                newName = mass_ID[i][3] + "_pk" + kod.substring(2, kod.length());
                                localContentValues.put(DbContract_Image.TableUser.COLUMN_IMAGE, newName);
                                db.insert("base6_image", null, localContentValues);
                                Log.d("New String", "ОБНОВЛЕННО " + newName);
                            } else {
                                newName = "";
                                newName = mass_ID[i][3] + "_" + kod.toLowerCase();
                                localContentValues.put(DbContract_Image.TableUser.COLUMN_IMAGE, newName);
                                db.insert("base6_image", null, localContentValues);
                                Log.d("New String", "ОБНОВЛЕННО " + newName);
                            }
                            Log.e("mas1=", mass_ID[i][2] + ", " + name + ", Image=" + newName);
                            Log.e("mas2=", mass_ID[i][3] + ", " + name + ", Image=" + newName);
                            if (cursor.getPosition() == 1) {
                                pDialog.setProgress(summa.intValue());
                                summa = summa + perm;
                            }
                            cursor.moveToNext();
                        } else {
                            Log.e("mas1=", mass_ID[i][2] + ", " + name + ", Image=" + newName);
                            Log.e("mas2=", mass_ID[i][3] + ", " + name + ", Image=" + newName);
                            if (cursor.getPosition() == 1) {
                                pDialog.setProgress(summa.intValue());
                                summa = summa + perm;
                            }
                            cursor.moveToNext();
                        }
                    }
                }
                cursor.close();
                db.close();


                pDialog.dismiss();
            }
        }).start();
    }  // Загрузка с прогрессом

    protected void ListView_Adapter_Combine_Sklad() {

        SQLiteDatabase db_sqlite = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);

        final String CREATE_TABLE = "CREATE TABLE if not exists base42_ost_osnv" +
                "(" + DbContract_Nomeclature.TableUser._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + DbContract_Ostatok.TableUser.COLUMN_DATA + " TEXT, "
                + DbContract_Ostatok.TableUser.COLUMN_COUNT + " TEXT, "
                + DbContract_Ostatok.TableUser.COLUMN_KODUID + " TEXT, "
                + DbContract_Ostatok.TableUser.COLUMN_SKLAD + " TEXT);";

        Log.e("ListView_Adapter_Image=", "Таблица base42_ost_osnv создана!");
        db_sqlite.execSQL(CREATE_TABLE);
        final String DELETE_TABLE = "DROP TABLE base42_ost_osnv";
        db_sqlite.execSQL(DELETE_TABLE);
        Log.e("ListView_Adapter_Image=", "Таблица base42_ost_osnv удалена!");
        db_sqlite.execSQL(CREATE_TABLE);
        Log.e("ListView_Adapter_Image=", "Таблица base42_ost_osnv создана!");
        db_sqlite.close();


        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
        String query = " SELECT base4_ost.data, base4_ost.koduid, base4_ost.sklad, SUM(base4_ost.count) AS 'count'\n" +
                " FROM base4_ost\n" +
                " WHERE base4_ost.sklad !='1EB83A85-67D4-46A2-A9DE-14B419A8DB65' " +
                "AND base4_ost.sklad !='EA661E8F-90D1-48BF-A530-5998FB65BFDD' GROUP BY base4_ost.koduid;";

        cursor = db.rawQuery(query, null);
        ContentValues localContentValues = new ContentValues();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String data = cursor.getString(cursor.getColumnIndex(DbContract_Ostatok.TableUser.COLUMN_DATA));
            String count = cursor.getString(cursor.getColumnIndex(DbContract_Ostatok.TableUser.COLUMN_COUNT));
            String koduid = cursor.getString(cursor.getColumnIndex(DbContract_Ostatok.TableUser.COLUMN_KODUID));
            String sklad = cursor.getString(cursor.getColumnIndex(DbContract_Ostatok.TableUser.COLUMN_SKLAD));

            Log.e("Base42_osnv=", koduid + ", " + count);
            localContentValues.put(DbContract_Ostatok.TableUser.COLUMN_DATA, data);
            localContentValues.put(DbContract_Ostatok.TableUser.COLUMN_COUNT, count);
            localContentValues.put(DbContract_Ostatok.TableUser.COLUMN_KODUID, koduid);
            localContentValues.put(DbContract_Ostatok.TableUser.COLUMN_SKLAD, sklad);

            db.insert("base42_ost_osnv", null, localContentValues);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }  // Создание базы остатков для основного склада


    protected void ListView_Adapter_Nomeclature() {

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
        String query_ID = "SELECT " + DbContract_GroupID.TableUser.COLUMN_BRENDS + ", "
                + DbContract_GroupID.TableUser.COLUMN_PER_KOD + ", "
                + DbContract_GroupID.TableUser.COLUMN_PREF + ", "
                + DbContract_GroupID.TableUser.COLUMN_WORK + ", "
                + DbContract_GroupID.TableUser.COLUMN_GR_TYPE + " FROM base7_Brends_ID";

        cursor_ID = db.rawQuery(query_ID, null);
        mass_ID = new String[cursor_ID.getCount()][5];

        cursor_ID.moveToFirst();
        while (cursor_ID.isAfterLast() == false) {
            String brends = cursor_ID.getString(cursor_ID.getColumnIndex(DbContract_GroupID.TableUser.COLUMN_BRENDS));
            String kod = cursor_ID.getString(cursor_ID.getColumnIndex(DbContract_GroupID.TableUser.COLUMN_PER_KOD));
            String pref = cursor_ID.getString(cursor_ID.getColumnIndex(DbContract_GroupID.TableUser.COLUMN_PREF));
            String work = cursor_ID.getString(cursor_ID.getColumnIndex(DbContract_GroupID.TableUser.COLUMN_WORK));
            mass_ID[cursor_ID.getPosition()][1] = brends;
            mass_ID[cursor_ID.getPosition()][2] = kod;
            mass_ID[cursor_ID.getPosition()][3] = pref;
            mass_ID[cursor_ID.getPosition()][4] = work;
            Log.e("Do=", "Загрузка картинок");
            cursor_ID.moveToNext();
        }
        cursor_ID.close();


        // SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
       /* String query = " SELECT base1_v8.kod, base1_v8.name, base2_sveta.cena, base1_v8.strih, base2_sveta.kod_univ, " +
                "base1_v8.koduid, base6_image.image, base2_sveta.p_group, base9_kolbox.kolbox, base9_kolbox.work" +
                "        FROM  base1_v8" +
                "        LEFT JOIN base2_sveta ON base1_v8.name = base2_sveta.name" +
                "        LEFT JOIN base3_uid ON base1_v8.name = base3_uid.name" +
                "        LEFT JOIN base6_image ON base1_v8.name = base6_image.name" +
                "        LEFT JOIN base9_kolbox ON base1_v8.name = base9_kolbox.name";*/
    /*    String query2 = " SELECT base4_ost.data, SUM(base4_ost.count) AS 'count', base4_ost.koduid, base4_ost.sklad, base1_v8.name, " +
                "base2_sveta.kod_univ, base1_v8.strih, base2_sveta.cena, base6_image.image\n" +
                "        FROM base3_uid\n" +
                "        LEFT JOIN base2_sveta ON base2_sveta.name = base1_v8.name\n" +
                "        LEFT JOIN base3_uid ON base1_v8.name = base3_uid.name" +
                "        LEFT JOIN base6_image ON base1_v8.name = base6_image.name\n" +
                "        LEFT JOIN base9_kolbox ON base1_v8.name = base9_kolbox.name\n" +

                "        LEFT JOIN base4_ost ON base4_ost.koduid = base3_uid.koduid\n" +
                "        LEFT JOIN base1_v8 ON base1_v8.name = base3_uid.name\n" +


                "        WHERE base4_ost.sklad !='1EB83A85-67D4-46A2-A9DE-14B419A8DB65' GROUP BY base1_v8.name;";

        String query = " SELECT base1_v8.kod, base1_v8.name, SUM(base4_ost.count) AS 'count', base2_sveta.cena, base1_v8.strih, base2_sveta.kod_univ, " +
                "base1_v8.koduid, base6_image.image, base2_sveta.p_group, base9_kolbox.kolbox, base9_kolbox.work" +
                "        FROM  base1_v8" +
                "        LEFT JOIN base2_sveta ON base1_v8.name = base2_sveta.name" +
                "        LEFT JOIN base3_uid ON base1_v8.name = base3_uid.name" +
                "        LEFT JOIN base6_image ON base1_v8.name = base6_image.name" +
                "        LEFT JOIN base4_ost ON base4_ost.koduid = base3_uid.koduid\n" +
                "        LEFT JOIN base9_kolbox ON base1_v8.name = base9_kolbox.name" +
                "        WHERE base4_ost.sklad !='1EB83A85-67D4-46A2-A9DE-14B419A8DB65' " +
                "AND base4_ost.sklad !='EA661E8F-90D1-48BF-A530-5998FB65BFDD'" +
                "AND base2_sveta.kod_univ LIKE('" + mass_ID[i][2] + "%')GROUP BY base2_sveta.kod_univ;";      */

        String query = " SELECT base1_v8.kod, base1_v8.name, base2_sveta.cena, base1_v8.strih, base2_sveta.kod_univ, \n" +
                "                base1_v8.koduid, base42_ost_osnv.count, base6_image.image, base2_sveta.p_group, \n" +
                "                base9_kolbox.kolbox, base9_kolbox.work\n" +
                "FROM  base1_v8 \n" +
                "LEFT JOIN base2_sveta ON base1_v8.name = base2_sveta.name\n" +
                "LEFT JOIN base3_uid ON base1_v8.name = base3_uid.name\n" +
                "LEFT JOIN base42_ost_osnv ON base3_uid.koduid = base42_ost_osnv.koduid\n" +
                "LEFT JOIN base6_image ON base1_v8.name = base6_image.name\n" +
                "LEFT JOIN base9_kolbox ON base2_sveta.kod_univ = base9_kolbox.kod_univ WHERE base2_sveta.kod_univ !=' '";

        cursor = db.rawQuery(query, null);
        ContentValues localContentValues = new ContentValues();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String kod = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KOD));
            String name = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_NAME));
            String cena = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_CENA));
            String strih = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_STRIH));
            String koduniv = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KODUNIV));
            String koduid = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KODUID));
            String ostatok = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_OSTATOK));
            String image = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_IMAGE));
            String group = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_GROUP));
            String kolbox = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KOLBOX));
            String work = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_WORK));

            Log.e("Base10=", name + ", " + cena + "," + kod + "," + strih + "," + koduniv + "," + koduid.trim() + "," + group);
            localContentValues.put(DbContract_Nomeclature.TableUser.COLUMN_KOD, kod);
            localContentValues.put(DbContract_Nomeclature.TableUser.COLUMN_NAME, name);
            localContentValues.put(DbContract_Nomeclature.TableUser.COLUMN_KOLBOX, kolbox);
            localContentValues.put(DbContract_Nomeclature.TableUser.COLUMN_CENA, cena);
            localContentValues.put(DbContract_Nomeclature.TableUser.COLUMN_IMAGE, image);
            localContentValues.put(DbContract_Nomeclature.TableUser.COLUMN_STRIH, strih);
            localContentValues.put(DbContract_Nomeclature.TableUser.COLUMN_KODUNIV, koduniv);
            localContentValues.put(DbContract_Nomeclature.TableUser.COLUMN_KODUID, koduid.trim());
            localContentValues.put(DbContract_Nomeclature.TableUser.COLUMN_GROUP, group);
            localContentValues.put(DbContract_Nomeclature.TableUser.COLUMN_OSTATOK, ostatok);
            localContentValues.put(DbContract_Nomeclature.TableUser.COLUMN_WORK, work);

            db.insert("base10_nomeclature", null, localContentValues);
            cursor.moveToNext();

        }


        cursor.close();
        db.close();
    }

    protected void ListView_Adapter_Nomeclature_Next() {

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
        String query_ID = "SELECT " + DbContract_GroupID.TableUser.COLUMN_BRENDS + ", "
                + DbContract_GroupID.TableUser.COLUMN_PER_KOD + ", "
                + DbContract_GroupID.TableUser.COLUMN_PREF + ", "
                + DbContract_GroupID.TableUser.COLUMN_WORK + ", "
                + DbContract_GroupID.TableUser.COLUMN_GR_TYPE + " FROM base7_Brends_ID";

        cursor_ID = db.rawQuery(query_ID, null);
        mass_ID = new String[cursor_ID.getCount()][5];

        cursor_ID.moveToFirst();
        while (cursor_ID.isAfterLast() == false) {
            String brends = cursor_ID.getString(cursor_ID.getColumnIndex(DbContract_GroupID.TableUser.COLUMN_BRENDS));
            String kod = cursor_ID.getString(cursor_ID.getColumnIndex(DbContract_GroupID.TableUser.COLUMN_PER_KOD));
            String pref = cursor_ID.getString(cursor_ID.getColumnIndex(DbContract_GroupID.TableUser.COLUMN_PREF));
            String work = cursor_ID.getString(cursor_ID.getColumnIndex(DbContract_GroupID.TableUser.COLUMN_WORK));
            mass_ID[cursor_ID.getPosition()][1] = brends;
            mass_ID[cursor_ID.getPosition()][2] = kod;
            mass_ID[cursor_ID.getPosition()][3] = pref;
            mass_ID[cursor_ID.getPosition()][4] = work;
            Log.e("Do=", "Загрузка картинок");
            cursor_ID.moveToNext();
        }
        cursor_ID.close();

        for (int i = 0; i < mass_ID.length; i++) {

            String query_up = " SELECT base10_nomeclature.kod, base10_nomeclature.name, " +
                    "base10_nomeclature.kolbox, base10_nomeclature.cena, " +
                    "base10_nomeclature.image, base10_nomeclature.strih, " +
                    "base10_nomeclature.kod_univ, \n" +
                    "         base10_nomeclature.koduid, base10_nomeclature.p_group, " +
                    "base10_nomeclature.brends, base10_nomeclature.count, " +
                    "base10_nomeclature.work \n" +
                    "                FROM base10_nomeclature\n" +
                    "                WHERE base10_nomeclature.kod_univ LIKE ('" + mass_ID[i][2] + "%')";


            cursor = db.rawQuery(query_up, null);
            ContentValues localContentValues2 = new ContentValues();
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                Log.e("Base10=", "Update_ " + mass_ID[i][1]);
                String kod = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KOD));
                String name = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_NAME));
                String cena = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_CENA));
                String strih = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_STRIH));
                String koduniv = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KODUNIV));
                String koduid = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KODUID));
                String ostatok = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_OSTATOK));
                String image = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_IMAGE));
                String group = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_GROUP));
                String kolbox = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KOLBOX));
                String work = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_WORK));

                localContentValues2.put(DbContract_Nomeclature.TableUser.COLUMN_KOD, kod);
                localContentValues2.put(DbContract_Nomeclature.TableUser.COLUMN_NAME, name);
                localContentValues2.put(DbContract_Nomeclature.TableUser.COLUMN_KOLBOX, kolbox);
                localContentValues2.put(DbContract_Nomeclature.TableUser.COLUMN_CENA, cena);
                localContentValues2.put(DbContract_Nomeclature.TableUser.COLUMN_IMAGE, image);
                localContentValues2.put(DbContract_Nomeclature.TableUser.COLUMN_STRIH, strih);
                localContentValues2.put(DbContract_Nomeclature.TableUser.COLUMN_KODUNIV, koduniv);
                localContentValues2.put(DbContract_Nomeclature.TableUser.COLUMN_KODUID, koduid);
                localContentValues2.put(DbContract_Nomeclature.TableUser.COLUMN_GROUP, group);
                localContentValues2.put(DbContract_Nomeclature.TableUser.COLUMN_OSTATOK, ostatok);
                localContentValues2.put(DbContract_Nomeclature.TableUser.COLUMN_WORK, work);
                localContentValues2.put(DbContract_Nomeclature.TableUser.COLUMN_BRENDS, mass_ID[i][1]);
                String[] arrayOfString = new String[1];
                arrayOfString[0] = koduid;
                db.update("base10_nomeclature", localContentValues2, "koduid = ?", arrayOfString);
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();
    } // Занесение название бренда в ноеклатуру

    protected void ListView_Adapter_Nomeclature_Work_Table() {
        SQLiteDatabase db_sqlite = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);

        final String CREATE_TABLE = "CREATE TABLE if not exists base10_nomeclature" +
                "(" + DbContract_Nomeclature.TableUser.COLUMN_BRENDS + " TEXT , "
                + DbContract_Nomeclature.TableUser.COLUMN_GROUP + " TEXT, "
                + DbContract_Nomeclature.TableUser.COLUMN_KOD + " TEXT, "
                + DbContract_Nomeclature.TableUser.COLUMN_NAME + " TEXT, "
                + DbContract_Nomeclature.TableUser.COLUMN_KOLBOX + " TEXT, "
                + DbContract_Nomeclature.TableUser.COLUMN_OSTATOK + " TEXT, "
                + DbContract_Nomeclature.TableUser.COLUMN_CENA + " TEXT, "
                + DbContract_Nomeclature.TableUser.COLUMN_IMAGE + " TEXT, "
                + DbContract_Nomeclature.TableUser.COLUMN_STRIH + " TEXT, "
                + DbContract_Nomeclature.TableUser.COLUMN_KODUNIV + " TEXT, "
                + DbContract_Nomeclature.TableUser.COLUMN_WORK + " TEXT, "
                + DbContract_Nomeclature.TableUser.COLUMN_KODUID + " TEXT PRIMARY KEY);";

        Log.e("ListView_Adapter_Image=", "Таблица base10_nomeclature создана!");
        db_sqlite.execSQL(CREATE_TABLE);


        final String DELETE_TABLE = "DROP TABLE base10_nomeclature";
        db_sqlite.execSQL(DELETE_TABLE);
        Log.e("ListView_Adapter_Image=", "Таблица base10_nomeclature удалена!");
        db_sqlite.execSQL(CREATE_TABLE);
        Log.e("ListView_Adapter_Image=", "Таблица base10_nomeclature создана!");
        db_sqlite.close();
    }  // Очистка таблицы и создание раздела заново

    protected void ListView_Adapter_Update_Price() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);

        String query_up = " SELECT base10_nomeclature.kod, base10_nomeclature.name, " +
                "base10_nomeclature.kolbox, base5_price.cena, base10_nomeclature.image, " +
                "base10_nomeclature.strih, base10_nomeclature.kod_univ,\n" +
                "base10_nomeclature.koduid, base10_nomeclature.p_group, " +
                "base10_nomeclature.brends, base10_nomeclature.count, " +
                "base10_nomeclature.work \n" +
                "FROM base10_nomeclature\n" +
                "LEFT JOIN base5_price ON base10_nomeclature.koduid = base5_price.koduid";

        cursor = db.rawQuery(query_up, null);
        ContentValues localContentValuesUP = new ContentValues();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String cena = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_CENA));
            String koduid = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KODUID));
            localContentValuesUP.put(DbContract_Nomeclature.TableUser.COLUMN_KODUID, koduid);
            localContentValuesUP.put(DbContract_Nomeclature.TableUser.COLUMN_CENA, cena);
            String[] arrayOfString = new String[1];
            arrayOfString[0] = koduid;
            Log.e("Update_Price=", koduid + ", Обновлена цена!");
            db.update("base10_nomeclature", localContentValuesUP, "koduid = ?", arrayOfString);
            cursor.moveToNext();
        }

        cursor.close();
        db.close();


    } // Обновление цен

    protected void ListView_Adapter_Update_Sklad() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);

        String query_up = " SELECT base10_nomeclature.kod, base10_nomeclature.name, " +
                "base10_nomeclature.kolbox, base10_nomeclature.cena, base10_nomeclature.image, " +
                "base10_nomeclature.strih, base10_nomeclature.kod_univ,\n" +
                "base10_nomeclature.koduid, base10_nomeclature.p_group, " +
                "base10_nomeclature.brends, base42_ost_osnv.count, " +
                "base10_nomeclature.work \n" +
                "FROM base10_nomeclature\n" +
                "LEFT JOIN base42_ost_osnv ON base10_nomeclature.koduid = base42_ost_osnv.koduid";

        cursor = db.rawQuery(query_up, null);
        ContentValues localContentValuesUP = new ContentValues();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String ostatok = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_OSTATOK));
            String koduid = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KODUID));
            localContentValuesUP.put(DbContract_Nomeclature.TableUser.COLUMN_KODUID, koduid);
            localContentValuesUP.put(DbContract_Nomeclature.TableUser.COLUMN_OSTATOK, ostatok);
            String[] arrayOfString = new String[1];
            arrayOfString[0] = koduid;
            Log.e("Update_Ostatok=", koduid + ", Обновлена остаток!");
            db.update("base10_nomeclature", localContentValuesUP, "koduid = ?", arrayOfString);
            cursor.moveToNext();
        }

        cursor.close();
        db.close();


    } // Обновление остатков


    protected void Loading_All_DB() {
        // Скачивание остальных баз
        ftp_con_put = "/Server/Nomeklature/suncape_all_db.db3";
        FtpConnection c = new FtpConnection();
        c.server = ftp_con_serv;
        c.port = 21;
        c.username = ftp_con_login;
        c.password = ftp_con_pass;

        FtpAction a = new FtpAction();
        a.success = true;
        a.connection = c;
        a.remoteFilename = ftp_con_put;

        new FtpAsyncTask_Single_Conn_AllDB(PrefActivity.this).execute(a);
    }

    protected void Loading_SOS()
    {
        ftp_con_put = "/Server/SOS/suncape_all_db.db3";
        FtpConnection c = new FtpConnection();
        c.server = ftp_con_serv;
        c.port = 21;
        c.username = ftp_con_login;
        c.password = ftp_con_pass;

        FtpAction a = new FtpAction();
        a.success = true;
        a.connection = c;
        a.remoteFilename = ftp_con_put;

        new FtpAsyncTask_Single_Conn_UpServ(PrefActivity.this).execute(a);
    }

    protected void ListPtreference_FTP() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("logins.db3", MODE_PRIVATE, null);

        String query_up = "SELECT ftp_connect.ftp_login, ftp_connect.ftp_password, " +
                "ftp_connect.ftp_port, ftp_connect.ftp_server, ftp_connect.ftp_put\n" +
                "FROM ftp_connect";

        cursor = db.rawQuery(query_up, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String server = cursor.getString(cursor.getColumnIndex("ftp_server"));
            String port = cursor.getString(cursor.getColumnIndex("ftp_port"));
            String login = cursor.getString(cursor.getColumnIndex("ftp_login"));
            String password = cursor.getString(cursor.getColumnIndex("ftp_password"));
            String put = cursor.getString(cursor.getColumnIndex("ftp_put"));
            ftp_connect.add(new ListAdapterSimple_Pref_Ftp(server, port, login, password, put));
            cursor.moveToNext();
        }

        cursor.close();
        db.close();


    } // Обновление остатков

    @Override
    public void onResume() {
        super.onResume();
        preference_server_name.setSummary(preference_server_name.getText());
        preference_server_login.setSummary(preference_server_login.getText());
        preference_server_password.setSummary(preference_server_password.getText());
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

    protected void Update_Date_Summary()
    {
        // Дата последнего обновления данных IMAGE
        if (!sp.getString("Data_update_image", "0").isEmpty()) {
            button_image.setSummary(sp.getString("Data_update_image", "0"));
        } else button_image.setSummary("нет данных об обновлении");

        // Дата последнего обновления данных Номенклатуры
        if (!sp.getString("Data_update_db_nomkl", "0").isEmpty()) {
            button_Db.setSummary(sp.getString("Data_update_db_nomkl", "0"));
        } else button_Db.setSummary("нет данных об обновлении");

        // Дата последнего резервного копирования
        if (!sp.getString("Data_update_sos", "0").isEmpty()) {
            button_SOS.setSummary(sp.getString("Data_update_sos", "0"));
        } else button_SOS.setSummary("нет данных об обновлении");


    }

    protected void BuildConfig_This() {

        Integer api = Build.VERSION.SDK_INT;
        String android = Build.VERSION.RELEASE;
        String Brand = Build.BRAND;
        String Model = Build.MODEL;
        String Model2 = Build.DEVICE;
        String Model3 = Build.DISPLAY;
        String Model4 = Build.PRODUCT;
        String Model5 = Build.TYPE;
        String Model6 = Build.USER;
        Long Model7 = Build.TIME;
        String Model8 = Build.BOARD;
        String[] Model9 = Build.SUPPORTED_64_BIT_ABIS;
        String[] Model91 = Build.SUPPORTED_32_BIT_ABIS;
        String[] Model92 = Build.SUPPORTED_ABIS;

        String Models1 = Build.ID;
        String Models2 = Build.TAGS;
        Integer Models3 = Build.VERSION.PREVIEW_SDK_INT;
        String Models4 = Build.VERSION.INCREMENTAL;
 // PPR1.180610.011.A730FXXU7CTE1
        Log.e(this.getLocalClassName(), api.toString());
        Log.e(this.getLocalClassName(), android);
        Log.e(this.getLocalClassName(), Brand);
        Log.e(this.getLocalClassName(), Model);
        Log.e(this.getLocalClassName(), Model2);
        Log.e(this.getLocalClassName(), Model3);
        Log.e(this.getLocalClassName(), Model4);
        Log.e(this.getLocalClassName(), Model5);
        Log.e(this.getLocalClassName(), Model6);
        Log.e(this.getLocalClassName(), Model7.toString());
        Log.e(this.getLocalClassName(), Model8);
        Log.e(this.getLocalClassName(), Models1);
        Log.e(this.getLocalClassName(), Models2);
        Log.e(this.getLocalClassName(), Models3.toString());
        Log.e(this.getLocalClassName(), Models4);
        for (int i=0; i<Model9.length; i++)
        {
            Log.e(this.getLocalClassName(), Model9[i]);
            Log.e(this.getLocalClassName(), Model91[i]);
            Log.e(this.getLocalClassName(), Model92[i]);
        }


    }


    protected void PacksInfo() {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version_nomer = pInfo.versionName;            // Номер версии приложения
            name_pack = pInfo.packageName;                // Имя пакета
            version_code = pInfo.versionCode;             // Номер версии кода
            version_release = pInfo.baseRevisionCode;     // Version Code
            Log.e(this.getLocalClassName(), version_nomer);
            Log.e(this.getLocalClassName(), name_pack);
            Log.e(this.getLocalClassName(), version_code.toString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    protected void Constant() {
        ftp_con_serv = "176.123.246.244";
        ftp_con_login = "sunbell_siberica";
        ftp_con_pass = "Roman911NFS";

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        DATABASE_NAME_MONTH = sp.getString("DATABASE_NAME_MONTH", "0");
        DATABASE_NAME_DATA = sp.getString("DATABASE_NAME_DATA", "0");
        BaseTableName_Reg = sp.getString("REGIONS", "0");
        PARAMS_KON_NAME = sp.getString("PARAMS_KON_NAME", "0");
        PARAMS_KON_KOD = sp.getString("PARAMS_KON_KOD", "0");
        PARAMS_KOD_SUMMA = sp.getString("PARAMS_KOD_SUMMA", "0");
        REGIONS = sp.getString("REGIONS", "0");
        PARAMS_KOD_DATA_UP = sp.getString("PARAMS_KOD_DATA_UP", "0");
        NAME_AGENT = sp.getString("NAME_AGENT", "0");
        DB_UP = "base_" + REGIONS + "_rn_data.db3";
    }


}
