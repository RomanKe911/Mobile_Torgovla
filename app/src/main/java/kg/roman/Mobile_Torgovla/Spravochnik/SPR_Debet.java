package kg.roman.Mobile_Torgovla.Spravochnik;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Debet;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Debet;
import kg.roman.Mobile_Torgovla.R;

public class SPR_Debet extends AppCompatActivity {

    public ArrayList<ListAdapterSimple_Debet> debet = new ArrayList<ListAdapterSimple_Debet>();
    public ListAdapterAde_Debet adapterPriceClients_debet;
    public Context context, context_Activity;
    public String UID_AGENTS, Agent_Type, k_agent_Name;
    public ListView listView;
    public String perem_agent;
    public Spinner spinner;
    public String[][] mass;
    public String[] mass2;
    public String thisdata, StartActivity;
    public Button button_ok, button_Go, button_cancel, Button;
    public View localView;
    public TextView Name, UID;
    public TextView Debet;
    public EditText Edit;
    public String uid, summa_debet, Format_Double, name_debet, agent_uid;
    public Double perem;
    public Integer int_spinner;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now, this_data_work_now, this_vrema_work_now;

    public SharedPreferences.Editor ed;
    public SharedPreferences sp;
    public String PEREM_FTP_SERV, PEREM_FTP_LOGIN, PEREM_FTP_PASS, PEREM_FTP_DISTR_XML, PEREM_FTP_DISTR_db3,
            PEREM_IMAGE_PUT_SDCARD, PEREM_IMAGE_PUT_PHONE;
    public String PEREM_MAIL_LOGIN, PEREM_MAIL_PASS, PEREM_MAIL_START, PEREM_MAIL_END,
            PEREM_DB3_CONST, PEREM_DB3_BASE, PEREM_DB3_RN, PEREM_ANDROID_ID_ADMIN, PEREM_ANDROID_ID;
    public String PEREM_AG_UID, PEREM_AG_NAME, PEREM_AG_REGION, PEREM_AG_UID_REGION, PEREM_AG_CENA,
            PEREM_AG_SKLAD, PEREM_AG_UID_SKLAD, PEREM_AG_TYPE_REAL, PEREM_AG_TYPE_USER,
            PEREM_WORK_DISTR, PEREM_KOD_MOBILE;
    public String Save_dialog_up_, grb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spr_debet);
        context_Activity = SPR_Debet.this;
        Constanta_Read();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon_image);
        getSupportActionBar().setTitle("Консигнации");
        getSupportActionBar().setSubtitle("по агенту: " + PEREM_AG_NAME);

        listView = findViewById(R.id.listview_cagent);

        debet.clear();
        ListAdapter_Spinner();
        adapterPriceClients_debet = new ListAdapterAde_Debet(context_Activity, debet);
        adapterPriceClients_debet.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients_debet);

        // ListAdapter_ThisData();
        // Calendate_New();
    }


    protected void ListAdapter_All(Integer i) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("otcheti_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base_debet.kod_uid, base_debet.data, base_debet.debet, base_contragents.uid_expdr, base_contragents.uid_agent, base_contragents.k_agent\n" +
                "FROM base_debet\n" +
                "LEFT JOIN base_contragents ON base_debet.kod_uid = base_contragents.uid_k_agent\n" +
                "WHERE (base_contragents.uid_agent LIKE '%" + mass[i][0] + "%' AND base_debet.debet > 0)\n" +
                "OR (base_contragents.uid_expdr LIKE '%" + mass[i][0] + "%' AND base_debet.debet > 0)";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String K_Agent = cursor.getString(cursor.getColumnIndexOrThrow("k_agent"));
            String UID_K_AGENT = cursor.getString(cursor.getColumnIndexOrThrow("kod_uid"));
            String UID_AGENT = cursor.getString(cursor.getColumnIndexOrThrow("uid_agent"));
            // String Debet = cursor.getString(cursor.getColumnIndex("debet")).replace("0.0", "0").replace("0,0", "0");
            String Debet = cursor.getString(cursor.getColumnIndexOrThrow("debet"));
            Log.e("Debet", Debet);
            Log.e("Debet1", Debet.replace("00.00", "0"));
            Log.e("Debet2", Debet.replace("00.00", "0").replace("00,00", "0"));
            //  String Agent = cursor.getString(cursor.getColumnIndex("uid_agent"));
            debet.add(new ListAdapterSimple_Debet(K_Agent, UID_K_AGENT, Debet, perem_agent));
            Log.e(this.getLocalClassName(), K_Agent + UID_K_AGENT + UID_AGENT + Debet);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    protected void ListAdapter_Klients(Integer i) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("otcheti_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base_contragents.uid_k_agent, base_contragents.k_agent, base_contragents.adress, base_contragents.roadname, base_contragents.uid_agent\n" +
                "FROM base_contragents\n" +
                "WHERE (base_contragents.uid_agent LIKE '%" + mass[i][0] + "%') OR (base_contragents.uid_expdr LIKE '%" + mass[i][0] + "%');";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String K_Agent = cursor.getString(cursor.getColumnIndexOrThrow("k_agent"));
            String UID_K_AGENT = cursor.getString(cursor.getColumnIndexOrThrow("uid_k_agent"));
            String UID_AGENT = cursor.getString(cursor.getColumnIndexOrThrow("uid_agent"));
            debet.add(new ListAdapterSimple_Debet(K_Agent, UID_K_AGENT, "0", perem_agent));
            Log.e(this.getLocalClassName(), K_Agent + UID_K_AGENT + UID_AGENT + Debet);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    protected void ListAdapter_Case() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("otcheti_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base_debet.kod_uid, base_debet.data, base_debet.debet, " +
                "base_contragents.uid_agent, base_contragents.k_agent\n" +
                "FROM base_debet\n" +
                "LEFT JOIN base_contragents ON base_debet.kod_uid = base_contragents.uid_k_agent\n" +
                "WHERE base_contragents.uid_agent LIKE '" + UID_AGENTS + "' AND base_debet.debet > 0";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String K_Agent = cursor.getString(cursor.getColumnIndexOrThrow("k_agent"));
            String UID_K_AGENT = cursor.getString(cursor.getColumnIndexOrThrow("kod_uid"));
            String Debet = cursor.getString(cursor.getColumnIndexOrThrow("debet"));
            String Agent = cursor.getString(cursor.getColumnIndexOrThrow("uid_agent"));
            if (UID_AGENTS.equals(UID_K_AGENT)) {
                switch (Agent) {
                    case "A8BA1F48-C7E1-497B-B74A-D86426684712":
                        perem_agent = "Керкин Роман";
                        debet.add(new ListAdapterSimple_Debet(K_Agent, UID_K_AGENT, Debet, perem_agent));
                        cursor.moveToNext();
                        break;
                    case "C611B483-547F-41B2-9DF0-050C34682012":
                        perem_agent = "Керкина Елена";
                        debet.add(new ListAdapterSimple_Debet(K_Agent, UID_K_AGENT, Debet, perem_agent));
                        cursor.moveToNext();
                        break;
                    case "FC29F24F-24AE-488D-88B0-7B9BFD7A75A3":
                        perem_agent = "Игнатенко Раиса";
                        debet.add(new ListAdapterSimple_Debet(K_Agent, UID_K_AGENT, Debet, perem_agent));
                        cursor.moveToNext();
                        break;
                    default:
                        break;
                }

            } else
                cursor.moveToNext();

        }
        cursor.close();
        db.close();
    }

    protected void ListAdapter_Spinner() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT * FROM base_in_debet WHERE debet > 0 AND agent_uid = '" + PEREM_AG_UID + "' AND d_kontr_uid !=''";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
            String agent_name = cursor.getString(cursor.getColumnIndexOrThrow("agent_name"));
            String agent_uid = cursor.getString(cursor.getColumnIndexOrThrow("agent_uid"));
            String count = cursor.getString(cursor.getColumnIndexOrThrow("debet"));
            String klient_name = cursor.getString(cursor.getColumnIndexOrThrow("klient_name"));
            String klient_uid = cursor.getString(cursor.getColumnIndexOrThrow("klient_uid"));

            debet.add(new ListAdapterSimple_Debet(klient_name, klient_uid, count, agent_name));

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    protected void ListAdapter_ThisData() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT base_debet.data, base_debet.debet, base_debet.kod_uid\n" +
                "FROM base_debet";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
        thisdata = data.replace('/', '.');  // формат месяц/день/год
        cursor.close();
        db.close();
    }

    protected void ListAdapter_DeleteDebet(String Perent_Uid, String Perent_Debet) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("otcheti_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base_debet.data, base_debet.debet, base_debet.kod_uid\n" +
                "FROM base_debet\n" +
                "WHERE base_debet.kod_uid LIKE '%" + Perent_Uid + "%'";

        ContentValues localContentValuesUpdate = new ContentValues();
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String debet = cursor.getString(cursor.getColumnIndexOrThrow("debet")).replace(",", ".");
        String kod_uid = cursor.getString(cursor.getColumnIndexOrThrow("kod_uid"));
        Double sum = Double.parseDouble(debet) - Double.parseDouble(Perent_Debet);
        if (sum > 0) {
            Format_Double = new DecimalFormat("#00.00").format(sum);
        } else Format_Double = "0";

        Log.e("Debet", Format_Double);
        localContentValuesUpdate.put("debet", Format_Double);
        String[] arrayOfString = new String[1];
        arrayOfString[0] = kod_uid;
        db.update("base_debet", localContentValuesUpdate, "kod_uid = ?", new String[]{kod_uid});
        cursor.close();
        db.close();
    }

    protected void ListAdapter_InsertDebet(String Ins_Name, String Ins_Uid, String Ins_Debet) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("otcheti_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base_otchet_debet.data, base_otchet_debet.agent, base_otchet_debet.debet, base_otchet_debet.kod_uid\n" +
                "FROM base_otchet_debet";
        ContentValues localContentValuesInsert = new ContentValues();
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToLast();
        localContentValuesInsert.put("name", Ins_Name);
        localContentValuesInsert.put("data", this_data_work_now);
        localContentValuesInsert.put("agent", k_agent_Name);
        localContentValuesInsert.put("kod_uid", Ins_Uid);
        localContentValuesInsert.put("debet", Ins_Debet);
        db.insert("base_otchet_debet", null, localContentValuesInsert);

        cursor.close();
        db.close();
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
        //2020-10-12
        // this_data_now = this_rn_day + "-" + this_rn_month + "-" + this_rn_year;  // Формат для отображения

        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(this_rn_year), Integer.valueOf(this_rn_month) - 1, Integer.valueOf(this_rn_day));
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateString_NOW = dateFormat1.format(calendar.getTime());
        String dateString_WORK = dateFormat2.format(calendar.getTime());
        this_data_now = dateString_NOW;
        this_data_work_now = dateString_WORK;
        this_vrema_work_now = this_rn_vrema;
        Log.e(this.getLocalClassName(), "Рабочий день на:" + dateString_NOW);
        //  Log.e("WJ_FormaL2:", "!DataEnd:" + dateString2);

    } // Загрузка даты и время

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

}
