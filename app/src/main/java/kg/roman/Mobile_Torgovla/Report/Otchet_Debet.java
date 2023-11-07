package kg.roman.Mobile_Torgovla.Report;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Debet;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_WJ_Otchet;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Debet;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_WJ_Otchet;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.Work_Journal.WJ_Global_Activity;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Price;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Price_ResourceParser;

public class Otchet_Debet extends AppCompatActivity {

    public ArrayList<ListAdapterSimple_Debet> debet = new ArrayList<ListAdapterSimple_Debet>();
    public ListAdapterAde_Debet adapterPriceClients;

    public String mass_kod[][];
    public Context context_Activity;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now;
    public TextView textView_summa;
    public ListView listview_otchet;
    public SharedPreferences.Editor ed;
    public SharedPreferences sp;
    public String PEREM_FTP_SERV, PEREM_FTP_LOGIN, PEREM_FTP_PASS, PEREM_FTP_DISTR_XML, PEREM_FTP_DISTR_db3,
            PEREM_IMAGE_PUT_SDCARD, PEREM_IMAGE_PUT_PHONE;
    public String PEREM_MAIL_LOGIN, PEREM_MAIL_PASS, PEREM_MAIL_START, PEREM_MAIL_END,
            PEREM_DB3_CONST, PEREM_DB3_BASE, PEREM_DB3_RN, PEREM_ANDROID_ID_ADMIN, PEREM_ANDROID_ID;
    public String PEREM_AG_UID, PEREM_AG_NAME, PEREM_AG_REGION, PEREM_AG_UID_REGION, PEREM_AG_CENA,
            PEREM_AG_SKLAD, PEREM_AG_UID_SKLAD, PEREM_AG_TYPE_REAL, PEREM_AG_TYPE_USER,
            PEREM_WORK_DISTR, PEREM_KOD_MOBILE, PEREM_FTP_PUT;
    public String Save_dialog_up_, grb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_otch_kassa);
        context_Activity = Otchet_Debet.this;
        Constanta_Read();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon_image);
        getSupportActionBar().setTitle("Задолжности по контрагентам");

        listview_otchet = findViewById(R.id.lsvw_otchet);
        textView_summa = findViewById(R.id.tvw_otch_summa);

        try {
            if (PEREM_ANDROID_ID.equals(PEREM_ANDROID_ID_ADMIN))
            {
                debet.clear();
                Loading_Debet_All();
                adapterPriceClients = new ListAdapterAde_Debet(context_Activity, debet);
                adapterPriceClients.notifyDataSetChanged();
                listview_otchet.setAdapter(adapterPriceClients);
            }
            else
            {
                debet.clear();
                Loading_Debet();
                adapterPriceClients = new ListAdapterAde_Debet(context_Activity, debet);
                adapterPriceClients.notifyDataSetChanged();
                listview_otchet.setAdapter(adapterPriceClients);
            }

        } catch (Exception e) {
            Log.e(context_Activity.getPackageName(), "Загрузка долгов");
        }


    }

    // Синхронизация цен
    private class MyAsyncTask_Sync_Debet extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
           // pDialog.setMessage("Синхронизация цен. Подождите...");
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
           // pDialog.setProgress(0);
            //  pDialog.dismiss();
        }

        private void getFloor() throws InterruptedException {
           // pDialog.setMessage("Загрузка продуктов. Подождите...");


            try {


            } catch (Exception e) {

                Log.e("Err..", "rrr");
            }


        }  // Синхронизация файлов для всех складов

    }

    // Консигнации по агенту
    protected void Loading_Debet() {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
            String query = "SELECT * FROM otchet_debet\n" +
                    "WHERE d_agent_uid = '" + PEREM_AG_UID + "' AND d_summa > 0 AND d_kontr_uid != '' " +
                    "ORDER BY d_kontr_name ASC";

            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            Double aut_summa = 0.0;
            while (cursor.isAfterLast() == false) {
                String client = cursor.getString(cursor.getColumnIndexOrThrow("d_kontr_name"));
                String client_uid = cursor.getString(cursor.getColumnIndexOrThrow("d_kontr_uid"));
                String summa = cursor.getString(cursor.getColumnIndexOrThrow("d_summa")).replaceAll(",", ".");
                aut_summa = aut_summa + Double.parseDouble(summa);
                String new_summa = new DecimalFormat("#00.00").format(Double.parseDouble(summa)).replaceAll(",", ".");
                debet.add(new ListAdapterSimple_Debet(client, client_uid, new_summa, ""));
                cursor.moveToNext();
            }
            cursor.close();
            db.close();

            textView_summa.setText(new DecimalFormat("#00.00").format(aut_summa).replaceAll("," , "."));
        } catch (Exception e) {
            Log.e("DEBET ", "Ошибка по задолжностям");
            Toast.makeText(context_Activity, "Ошибка по задолжностям", Toast.LENGTH_SHORT).show();
        }
    }

    // Консигнации все
    protected void Loading_Debet_All() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT * FROM otchet_debet\n" +
                "WHERE d_summa > 0 AND d_kontr_uid != '' " +
                "ORDER BY d_kontr_name ASC";

        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Double aut_summa = 0.0;
        while (cursor.isAfterLast() == false) {
            String client = cursor.getString(cursor.getColumnIndexOrThrow("d_kontr_name"));
            String client_uid = cursor.getString(cursor.getColumnIndexOrThrow("d_kontr_uid"));
            String summa = cursor.getString(cursor.getColumnIndexOrThrow("d_summa")).replaceAll(",", ".");
            aut_summa = aut_summa + Double.parseDouble(summa);
            String new_summa = new DecimalFormat("#00.00").format(Double.parseDouble(summa)).replaceAll(",", ".");
            debet.add(new ListAdapterSimple_Debet(client, client_uid, new_summa, ""));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        textView_summa.setText(new DecimalFormat("#00.00").format(aut_summa).replaceAll("," , "."));

        try {

        } catch (Exception e) {
            Log.e("DEBET ", "Ошибка по задолжностям");
            Toast.makeText(context_Activity, "Ошибка по задолжностям", Toast.LENGTH_SHORT).show();
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
        PEREM_FTP_PUT = sp.getString("list_preference_ftp", "0");                 //чтение данных:

        for (int i = 0; i < getResources().getStringArray(R.array.mass_for_update_data).length; i++) {
            Save_dialog_up_ = sp.getString("Save_dialog_up_" + i, "0");
        }
    }


}