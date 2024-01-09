package kg.roman.Mobile_Torgovla.Work_Journal;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Klients;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Klients;
import kg.roman.Mobile_Torgovla.R;

public class WJ_Forma_Zakaza_L1 extends AppCompatActivity {

    public ArrayList<ListAdapterSimple_Klients> klients = new ArrayList<ListAdapterSimple_Klients>();
    public ListAdapterAde_Klients adapterPriceClients;
    public Button button;
    public Context context_Activity;
    public Integer thisdata, thismonth, thisyear, thisminyte, thishour, thissecond;
    public String thisdata_st;
    public Calendar localCalendar = Calendar.getInstance();

    public String myTABLE, table_name, mass, thismonth_rn;
    public String[] mass_month;
    public String PEREM_debet, data_base;
    public String DATABASE_REGIONS;
    public String ftp_put, ftp_server, login, password;

    private EditText queryEditText;
    private Handler mHandler = new Handler();
    public String image_var, name_adress, Ident_Mon, Ident_Year;

    public String agent, name_klients, name_uid, Log_Text_Error;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now;
    public Integer resID;
    public ListView listView;
    public SharedPreferences sp;
    public SharedPreferences.Editor ed;
    public String PEREM_FORMA_KOD_RN;
    public String PEREM_FTP_SERV, PEREM_FTP_LOGIN, PEREM_FTP_PASS, PEREM_FTP_DISTR_XML, PEREM_FTP_DISTR_db3,
            PEREM_IMAGE_PUT_SDCARD, PEREM_IMAGE_PUT_PHONE;
    public String PEREM_MAIL_LOGIN, PEREM_MAIL_PASS, PEREM_MAIL_START, PEREM_MAIL_END,
            PEREM_DB3_CONST, PEREM_DB3_BASE, PEREM_DB3_RN, PEREM_ANDROID_ID_ADMIN, PEREM_ANDROID_ID;
    public String PEREM_AG_UID, PEREM_AG_NAME, PEREM_AG_REGION, PEREM_AG_UID_REGION, PEREM_AG_CENA,
            PEREM_AG_SKLAD, PEREM_AG_UID_SKLAD, PEREM_AG_TYPE_REAL, PEREM_AG_TYPE_USER,
            PEREM_WORK_DISTR, PEREM_KOD_MOBILE, PEREM_KOD_UID_KODRN, PEREM_K_AG_Data;
    public String PEREM_KLIENT_UID, PEREM_DIALOG_UID, PEREM_DIALOG_DATA_START, PEREM_DIALOG_DATA_END, PEREM_DISPLAY_START, PEREM_DISPLAY_END;
    public FloatingActionButton floatingActionButton;
    public ProgressBar progressBar;
    private String LOGEName = "Thread";


    /*07/11/2023
     *  Загрузка картинки для точки: нужно создать новый вариант для загрузки данных
     *  Переопределить префикся для созадниея накладной
     *
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt_wj_forma_klients);
        context_Activity = WJ_Forma_Zakaza_L1.this;
        name_klients = "";
        name_adress = "";
        Log_Text_Error = "ERR_WJ_F_Z_L1: ";
        Calendate_New();
        Constanta_Read();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_icons_1);
        // getSupportActionBar().setTitle("Заказы покупателей");
        getSupportActionBar().setSubtitle("Рабочая дата: " + this_data_now);

        button = findViewById(R.id.list_add);
        queryEditText = findViewById(R.id.editText_search);
        listView = findViewById(R.id.ListView_List_Klients_Debet);
        progressBar = findViewById(R.id.progressBarLoadingClient);
        floatingActionButton = findViewById(R.id.floatingActionButton_next_2);


        if (!klients.isEmpty()) {
            queryEditText.setText("");
            queryEditText.clearFocus();
          /*  queryEditText.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    //Когда пользователь вводит какой-нибудь текст:
                    // Activity_Sort.this.adapterPriceClients_Search.getFilter().filter(cs);
                    if (!adapterPriceClients.isEmpty())
                        adapterPriceClients.getFilter().filter(cs);
                    //MainActivity.this.adapterPriceClients.getFilter().filter(cs);
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                }

                @Override
                public void afterTextChanged(Editable arg0) {
                }
            });*/
        }


        try {
            WJ_Forma_Zakaza_L1.MyAsyncTask_SyncLoadingListView asyncTask = new WJ_Forma_Zakaza_L1.MyAsyncTask_SyncLoadingListView();
            asyncTask.execute();


        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка поиска данных", Toast.LENGTH_SHORT).show();
            Log.e(Log_Text_Error, "Ошибка поиска данных");
        }


        Loading_Kod_RN(PEREM_K_AG_Data);  // Создание идентификатора накладной
        Log.e("Форма заказа:", "Последий код:" + PEREM_FORMA_KOD_RN);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name_klients.isEmpty()) {
                    Constanta_Write(name_uid, name_klients, name_adress, PEREM_FORMA_KOD_RN, this_rn_data, this_rn_vrema, "");
                    Log.e("WJ_Forma_Zakaza_L1=", name_uid + ", " + name_klients + ", " + name_adress + ", " + PEREM_FORMA_KOD_RN + ", " + this_rn_data + ", " + this_rn_vrema);
                    Toast.makeText(context_Activity, "Последий код:" + PEREM_FORMA_KOD_RN, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza_L2.class);
                    startActivity(intent);
                    finish();
                    Log.e("Переменные для записи:", "Name_Order - " + PEREM_FORMA_KOD_RN);
                    Log.e("Переменные для записи:", "Name_Ag - " + PEREM_AG_NAME);
                    Log.e("Переменные для записи:", "UID_Ag - " + PEREM_AG_UID);
                    Log.e("Переменные для записи:", "Name_C - " + name_klients);
                    Log.e("Переменные для записи:", "UID_C - " + name_uid);
                    Log.e("Переменные для записи:", "Data - " + this_data_now);
                    //  Log.e("Переменные для записи:", "CREDIT - " +);
                    Log.e("Переменные для записи:", "SKLAD - " + PEREM_AG_SKLAD);
                    Log.e("Переменные для записи:", "UID_Sklad - " + PEREM_AG_UID_SKLAD);
                    Log.e("Переменные для записи:", "Cena_Price - " + PEREM_AG_CENA);
                    //   Log.e("Переменные для записи:", "Coment - " +);
                    //   Log.e("Переменные для записи:", "Cena_Nds - " +);
                } else {
                    // Toast.makeText(context_Activity, "Выберите клиента", Toast.LENGTH_LONG).show();

                    Snackbar.make(view, "Выберите клиента", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

        // Обработка изключенмя: загрузка Listview
        try {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    name_klients = ((TextView) view.findViewById(R.id.select_client_name)).getText().toString();
                    name_uid = ((TextView) view.findViewById(R.id.select_client_uid)).getText().toString();
                    name_adress = ((TextView) view.findViewById(R.id.select_client_adress)).getText().toString().replaceAll("\\s+$", "");
                    Toast.makeText(context_Activity, "Клиент: " + name_klients, Toast.LENGTH_SHORT).show();
                    String summa_debet = ((TextView) view.findViewById(R.id.select_client_credit)).getText().toString();
                    TextView title_summa_debet = ((TextView) view.findViewById(R.id.text_title_debet_contagent));
                    TextView text_summa_debet = ((TextView) view.findViewById(R.id.select_client_credit));
                    title_summa_debet.setVisibility(View.VISIBLE);
                    text_summa_debet.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка загрузка Listview", Toast.LENGTH_SHORT).show();
            Log.e(Log_Text_Error, "Ошибка загрузка Listview");
        }

        // Обработка изключенмя: обработка нажатия кнопки
        try {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!name_klients.isEmpty()) {
                        Constanta_Write(name_uid, name_klients, name_adress, PEREM_FORMA_KOD_RN, this_rn_data, this_rn_vrema, "");
                        Log.e("WJ_Forma_Zakaza_L1=", name_uid + ", " + name_klients + ", " + name_adress + ", " + PEREM_FORMA_KOD_RN + ", " + this_rn_data + ", " + this_rn_vrema);
                        Toast.makeText(context_Activity, "Последий код:" + PEREM_FORMA_KOD_RN, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza_L2.class);
                        startActivity(intent);
                        finish();
                        Log.e("Переменные для записи:", "Name_Order - " + PEREM_FORMA_KOD_RN);
                        Log.e("Переменные для записи:", "Name_Ag - " + PEREM_AG_NAME);
                        Log.e("Переменные для записи:", "UID_Ag - " + PEREM_AG_UID);
                        Log.e("Переменные для записи:", "Name_C - " + name_klients);
                        Log.e("Переменные для записи:", "UID_C - " + name_uid);
                        Log.e("Переменные для записи:", "Data - " + this_data_now);
                        //  Log.e("Переменные для записи:", "CREDIT - " +);
                        Log.e("Переменные для записи:", "SKLAD - " + PEREM_AG_SKLAD);
                        Log.e("Переменные для записи:", "UID_Sklad - " + PEREM_AG_UID_SKLAD);
                        Log.e("Переменные для записи:", "Cena_Price - " + PEREM_AG_CENA);
                        //   Log.e("Переменные для записи:", "Coment - " +);
                        //   Log.e("Переменные для записи:", "Cena_Nds - " +);
                    } else
                        Toast.makeText(context_Activity, "Выберите клиента", Toast.LENGTH_LONG).show();

                }
            });


        } catch (Exception e) {
            Log.e(Log_Text_Error, "Ошибка заполнения данных!");
            Toast.makeText(context_Activity, "Ошибка заполнения данных!", Toast.LENGTH_LONG).show();
        }
    }


    // Вызов метода при выходе назад
    public void onBackPressed() {
        Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza.class);
        startActivity(intent);
        finish();
    }

    // Создание потока для загрузки списка контрагентов
    private class MyAsyncTask_SyncLoadingListView extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Вызывается в начале потока
            Log.e(LOGEName, "Начало потока");

            progressBar.setVisibility(View.VISIBLE);
            klients.clear();
            adapterPriceClients = new ListAdapterAde_Klients(WJ_Forma_Zakaza_L1.this, klients);
            adapterPriceClients.notifyDataSetChanged();
            listView.setAdapter(adapterPriceClients);
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            //pDialog.setMessage("Синхронизация цен. Подождите...");
            // pDialog.setProgress(values[0]);
            Log.e(LOGEName, "поток работает" + values);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Для создания сложных потоков
            publishProgress(1);

            try {
                getFloor();  // функция для работы с потоками
            } catch (InterruptedException e) {
                Log.e(LOGEName, "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Вызывается в конце потока
            Log.e(LOGEName, "Конец потока");


            progressBar.setVisibility(View.INVISIBLE);
            Log.e("TEST", "Loading_Client ListView");
            adapterPriceClients = new ListAdapterAde_Klients(WJ_Forma_Zakaza_L1.this, klients);
            adapterPriceClients.notifyDataSetChanged();
            listView.setAdapter(adapterPriceClients);

        }

        private void getFloor() throws InterruptedException {
            Log.e(LOGEName, "Загрузка потока данными");
            //  pDialog.setMessage("Загрузка продуктов. Подождите...");
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
            String query = "SELECT * FROM const_contragents WHERE uid_agent = '" + PEREM_AG_UID + "' AND roaduid = '" + PEREM_AG_UID_REGION + "'";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String uid = cursor.getString(cursor.getColumnIndexOrThrow("uid_k_agent")); // код клиента
                String name = cursor.getString(cursor.getColumnIndexOrThrow("k_agent"));
                String adress = cursor.getString(cursor.getColumnIndexOrThrow("adress"));
                String roadname = cursor.getString(cursor.getColumnIndexOrThrow("roadname")); // имя группы
                String roadUid = cursor.getString(cursor.getColumnIndexOrThrow("roaduid")); // код папки группы
                //  String userUid = cursor.getString(cursor.getColumnIndex("")); // код агента
                //  int resID = getResources().getIdentifier(IMAGE, "drawable", getPackageName());
                image_var = name.substring(0, 2);
                String name_syb = name.substring(0, 3);
                switch (name_syb) {
                    case "маг":
                        resID = getResources().getIdentifier("user_shop_01", "drawable", getPackageName());
                        break; // магазин
                    case "+ма":
                        resID = getResources().getIdentifier("user_shop_01", "drawable", getPackageName());
                        break; // магазин
                    case "++м":
                        resID = getResources().getIdentifier("user_shop_01", "drawable", getPackageName());
                        break; // магазин
                    case "мар":
                        resID = getResources().getIdentifier("user_shop_04", "drawable", getPackageName());
                        break; // магазин
                    case "апт":
                        resID = getResources().getIdentifier("user_shop_02", "drawable", getPackageName());
                        break; // аптека
                    case "ко":
                        resID = getResources().getIdentifier("user_shop_03", "drawable", getPackageName());
                        break; // контейнер
                    case "пав":
                        resID = getResources().getIdentifier("user_shop_03", "drawable", getPackageName());
                        break; // контейнер
                    case "р-к":
                        resID = getResources().getIdentifier("user_shop_03", "drawable", getPackageName());
                        break; // контейнер
                    case "го":
                        resID = getResources().getIdentifier("user_shop_06", "drawable", getPackageName());
                        break; // гостиница
                    case "с. ":
                        resID = getResources().getIdentifier("user_shop_07", "drawable", getPackageName());
                        break; // гостиница
                    case "г. ":
                        resID = getResources().getIdentifier("user_shop_08", "drawable", getPackageName());
                        break; // гостиница
                    default:
                        resID = getResources().getIdentifier("user_shop_05", "drawable", getPackageName());
                        break; // контейнер
                }
                SQL_Debet_List(uid);
                klients.add(new ListAdapterSimple_Klients(name, uid, adress, resID, PEREM_debet));
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
            Log.e(LOGEName, "Загрузка потока данными, конец");
        }

    }

    // Загрузка базы данных в ListView из DB3
    protected void Loading_Base_Klients() {
        try {



        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка загрузки базы в Listview", Toast.LENGTH_SHORT).show();
            Log.e(Log_Text_Error, "Ошибка загрузки базы в Listview");
        }

    }

    /* */


    // Загрузка даты и время
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

        this_data_now = this_rn_day + "." + this_rn_month + "." + this_rn_year;
        Ident_Year = this_rn_year;
        //textView_data.setText(this_data_now);
    }

    protected void SQL_Debet_List(String l_uid) {
        try {
            SQLiteDatabase db_debet = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
            String query_debet = "SELECT * FROM otchet_debet WHERE d_kontr_uid = '" + l_uid + "' AND d_summa > 0;";
            final Cursor cursor_debet = db_debet.rawQuery(query_debet, null);
            cursor_debet.moveToFirst();
            if (cursor_debet.getCount() > 0 & cursor_debet != null) {
                String summa = cursor_debet.getString(cursor_debet.getColumnIndexOrThrow("d_summa")); // код клиента
                PEREM_debet = summa;
            } else PEREM_debet = "нет задолжностей";
            cursor_debet.close();
            db_debet.close();
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка задолжностей", Toast.LENGTH_SHORT).show();
            Log.e(Log_Text_Error, "Ошибка задолжностей");
            PEREM_debet = "ОШИБКА ЗАГРУЗКА ДОЛГОВ";
        }

    }


    // Создание переменной кода накладной
    // 07/11/2023 переопределить префиксы (Январь, Июль)
    protected void Loading_Kod_RN(String this_data) {
        Log.e(Log_Text_Error, "рабочая дата" + this_data);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
        //07/04/2022 измение в выборе товара   String query = "SELECT base_RN.Kod_RN, base_RN.Data FROM base_RN ORDER BY base_RN.Data ASC";
        //  String query = "SELECT base_RN.Kod_RN, base_RN.Data FROM base_RN  WHERE base_RN.Data = '" + this_data + "' ORDER BY base_RN.Kod_RN ASC;";
        String query = "SELECT base_RN_All.kod_rn, base_RN_All.data FROM base_RN_All ORDER BY base_RN_All.data ASC;";
        final Cursor cursor = db.rawQuery(query, null);
        String kod_univ;
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            kod_univ = cursor.getString(cursor.getColumnIndexOrThrow("kod_rn"));
        } else {
            kod_univ = "RnTTTTGOD21_0000";
        }

        mass_month = getResources().getStringArray(R.array.mass_month);
        for (int i = 1; i <= 12; i++) {
            if (Integer.valueOf(this_rn_month) == i) {
                thismonth_rn = mass_month[i - 1];
                Log.e("Месяц=", "i=" + i + ", " + thismonth_rn);
            }
        }

        Integer k;
        if (kod_univ != null & cursor.getCount() > 0 & kod_univ.length() < 16) {
            kod_univ = "RnTTTTGOD21_0000";
        }

        if (thismonth_rn.equals(kod_univ.substring(6, kod_univ.length() - 7))) {
            k = Integer.parseInt(kod_univ.substring(12)) + 1;
            Log.e("WJ_FormaL2:", "True=" + k);
        } else {
            k = 1;
        }

        Ident_Mon = thismonth_rn;
        Ident_Year = this_rn_year.substring(2, 4);
        String iden = "000";
        String iden2 = "00";
        String iden3 = "0";
        // String iden4 = "0";
        if (k < 10)
            PEREM_FORMA_KOD_RN = "Rn" + PEREM_KOD_UID_KODRN + Ident_Mon + Ident_Year + "_" + iden + k;
        else if (k < 100)
            PEREM_FORMA_KOD_RN = "Rn" + PEREM_KOD_UID_KODRN + Ident_Mon + Ident_Year + "_" + iden2 + k;
        else if (k < 1000)
            PEREM_FORMA_KOD_RN = "Rn" + PEREM_KOD_UID_KODRN + Ident_Mon + Ident_Year + "_" + iden3 + k;
        else if (k < 10000)
            PEREM_FORMA_KOD_RN = "Rn" + PEREM_KOD_UID_KODRN + Ident_Mon + Ident_Year + "_" + k;
        // else if (k < 100000) KOD_RN = "Rn" + Ident_Mon + Ident_Year + "_" + k;
        // Log.e("Новый код накладной:", PEREM_FORMA_KOD_RN);
        cursor.close();
        db.close();
        try {

            // cursor.moveToFirst();
            //Log.e("WJ_FormaL2:", "R1=" + kod_univ);
            // Log.e("МесяцOLD PREF=", kod_univ.substring(2, kod_univ.length() - 7));
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка созадние кодировки накладной!", Toast.LENGTH_SHORT).show();
            Log.e(Log_Text_Error, "Ошибка созадние кодировки накладной!");
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
        PEREM_KOD_UID_KODRN = sp.getString("PEREM_KOD_UID_KODRN", "0");           //чтение данных: уникальный код для накладной


        PEREM_KLIENT_UID = sp.getString("PEREM_KLIENT_UID", "0");                 //чтение данных: передача кода выбранного uid клиента
        PEREM_DIALOG_UID = sp.getString("PEREM_DIALOG_UID", "0");                 //чтение данных: передача кода выбранного uid клиента
        PEREM_DIALOG_DATA_START = sp.getString("PEREM_DIALOG_DATA_START", "0");   //чтение данных: передача кода начальной даты
        PEREM_DIALOG_DATA_END = sp.getString("PEREM_DIALOG_DATA_END", "0");       //чтение данных: передача кода конечной даты
        PEREM_DISPLAY_START = sp.getString("PEREM_DISPLAY_START", "0");           //чтение данных: передача кода для димплея начальной даты
        PEREM_DISPLAY_END = sp.getString("PEREM_DISPLAY_END", "0");                //чтение данных: передача кода для димплея конечной даты
        PEREM_DISPLAY_END = sp.getString("PEREM_DISPLAY_END", "0");                //чтение данных: передача кода для димплея конечной даты

        PEREM_K_AG_Data = sp.getString("PEREM_K_AG_Data", "0");                //чтение данных: передача кода для димплея конечной даты
    }

    // Константы для записи данных
    protected void Constanta_Write(String kag_uid, String kag_name, String kag_adress, String kag_kodrn, String kag_data, String kag_vrema, String kag_gps) {
        ed = sp.edit();
        ed.putString("PEREM_K_AG_NAME", kag_name);          //запись данных: имя контраегнта
        ed.putString("PEREM_K_AG_UID", kag_uid);            //запись данных: uid контрагента
        ed.putString("PEREM_K_AG_ADRESS", kag_adress);      //запись данных: адрес контрагента
        ed.putString("PEREM_K_AG_KodRN", kag_kodrn);        //запись данных: код накладной
        ed.putString("PEREM_K_AG_Data", kag_data);          //запись данных: время создание накладной
        ed.putString("PEREM_K_AG_Data_WORK", this_data_now);          //запись данных: время создание накладной
        ed.putString("PEREM_K_AG_Vrema", kag_vrema);        //запись данных: дата создание накладной
        ed.putString("PEREM_K_AG_GPS", kag_gps);            //запись данных: координаты gps
        ed.putString("PEREM_CLICK_TY", "false");
        ed.commit();

        /*PEREM_K_AG_NAME = sp.getString("PEREM_K_AG_NAME", "0");          //чтение данных: имя контраегнта
        PEREM_K_AG_UID = sp.getString("PEREM_K_AG_UID", "0");            //чтение данных: uid контрагента
        PEREM_K_AG_ADRESS = sp.getString("PEREM_K_AG_ADRESS", "0");      //чтение данных: адрес контрагент
        PEREM_K_AG_KodRN= sp.getString("PEREM_K_AG_KodRN", "0");         //чтение данных: код накладной
        PEREM_K_AG_Data= sp.getString("PEREM_K_AG_Data", "0");           //чтение данных: время создание н
        PEREM_K_AG_Vrema = sp.getString("PEREM_K_AG_Vrema", "0");        //чтение данных: дата создание на
        PEREM_K_AG_GPS= sp.getString("PEREM_K_AG_GPS", "0");             //чтение данных: координаты gps*/
    }


    protected void Loading_Agent() // Загрузка Ф.И. Агента
    {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("logins.db3", MODE_PRIVATE, null);

        String query = "SELECT login.name, login.s_name, login.UID FROM login";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name")); // код клиента
            String s_name = cursor.getString(cursor.getColumnIndexOrThrow("s_name"));
            String uid = cursor.getString(cursor.getColumnIndexOrThrow("UID"));
            if (uid.equals(PEREM_AG_UID)) {
                agent = s_name + "_" + name;
                cursor.moveToNext();
            } else cursor.moveToNext();


        }
        cursor.close();
        db.close();


    }

    protected void Calendare_This_Data() // Загрузка даты и время
    {
        // получаем данные времени и имя месяца
        //localCalendar.clear();
        thisdata = Integer.valueOf(localCalendar.get(Calendar.DAY_OF_MONTH));
        thismonth = Integer.valueOf(1 + localCalendar.get(Calendar.MONTH));
        thisyear = Integer.valueOf(localCalendar.get(Calendar.YEAR));
        thissecond = Integer.valueOf(localCalendar.get(Calendar.SECOND));
        thisminyte = Integer.valueOf(localCalendar.get(Calendar.MINUTE));
        thishour = Integer.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY));


        if (thisdata < 10) {
            thisdata_st = String.format("%02d", thisdata);
        } else thisdata_st = thisdata.toString();

        this_rn_data = thisdata_st + "." + thismonth + "." + thisyear;
        this_rn_vrema = thishour + ":" + thisminyte + ":" + thissecond;
        Toast.makeText(context_Activity, thisdata_st, Toast.LENGTH_SHORT).show();


        //  textView_data.setText(thisdata_st + "." + thismonth + "." + thisyear);
        data_base = thisdata_st + "_" + thismonth + "_" + thisyear;


        myTABLE = DATABASE_REGIONS + "_" + thismonth_rn + "_" + thisyear;
    }

    // меню менеджер ????
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getSupportActionBar().hide();
        getMenuInflater().inflate(R.menu.menu_forma_clients, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        // 07/11/2023 временно закрыто
        /*klients.clear();
        Loading_Base_Klients();
        adapterPriceClients = new ListAdapterAde_Klients(WJ_Forma_Zakaza_L1.this, klients);
        adapterPriceClients.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients);*/


        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // посимвольный поиск
                Log.e("Поле поиска.1.", newText);
                adapterPriceClients.getFilter().filter(newText);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // срабатывание после нажатия на кнопку поиска
                // TextView textView=(TextView)findViewById(R.id.search);
                //  textView.setText(query);
                Log.e("Поле поиска.2.", query);
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return true;

    }

    // меню менеджер
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.search: {

                // Log.e("Поле поиска..", "_"+item.getItemId());
               /* klients.clear();
                Loading_Base_Klients();
                adapterPriceClients = new ListAdapterAde_Klients(WJ_Forma_Zakaza_L1.this, klients);
                adapterPriceClients.notifyDataSetChanged();
                listView.setAdapter(adapterPriceClients);*/

              /*  Intent intent = getIntent();
                if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                    //Берем строку запроса из экстры
                    String query = intent.getStringExtra(SearchManager.QUERY);
                    //Выполняем поиск
                   // showResults(query);
                }*/


            }
            break;
            case R.id.menu_sync_up: {
                Intent intent_prefActivity = new Intent(context_Activity, WJ_New_Contragent.class);
                intent_prefActivity.putExtra("Layout_INTENT_BACK", "WJ_Forma_Zalaza_L1");
                startActivity(intent_prefActivity);
                finish();
            }
            break;
        }
        return super.onOptionsItemSelected(item);


    }


}


