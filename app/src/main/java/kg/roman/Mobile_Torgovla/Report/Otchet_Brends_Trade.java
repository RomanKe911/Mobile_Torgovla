package kg.roman.Mobile_Torgovla.Report;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterAde_List_RN_Table;
import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_List_RN_Table;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_WJ_Otchet;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_WJ_Otchet;
import kg.roman.Mobile_Torgovla.R;

public class Otchet_Brends_Trade extends AppCompatActivity {

    public ArrayList<ListAdapterSimple_WJ_Otchet> otchet = new ArrayList<ListAdapterSimple_WJ_Otchet>();
    public ListAdapterAde_WJ_Otchet adapterPriceClients;

    public String mass_kod[][];
    public Context context_Activity;
    public Calendar calendat_for_otchet = Calendar.getInstance();
    public int mYear, mMonth, mDay;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now;
    public TextView textView_summa, textView_kol, textView_itogo, textView_cal_start, textView_cal_end;
    public Button button_otchet, button_cal_start, button_cal_end;
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

    public String dateFormat_filter_db, dateFormat_filter_display, data_select_start, data_select_end;
    public String dateFormat_start_db, dateFormat_start_display;
    public String dateFormat_end_db, dateFormat_end_display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_otch_prodaji);

        context_Activity = Otchet_Brends_Trade.this;
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon_image);
        getSupportActionBar().setTitle("Продажи по брендам");

        button_otchet = (findViewById(R.id.button_filter));
        button_cal_start = findViewById(R.id.btn_cal_start);
        button_cal_end = findViewById(R.id.btn_cal_end);
        listview_otchet = (findViewById(R.id.lsvw_otchet));

        textView_summa = findViewById(R.id.tvw_otch_summa);
        textView_kol = findViewById(R.id.tvw_otch_kol);
        textView_itogo = findViewById(R.id.tvw_otch_itogo);
        textView_cal_start = findViewById(R.id.tvw_otch_cal_start);
        textView_cal_end = findViewById(R.id.tvw_otch_cal_end);

        /*                     Loading_Mass_Brends();
                        */

        Constanta_Read();
        Calendate_New();
        textView_cal_start.setText(dateFormat_filter_display);
        textView_cal_end.setText(dateFormat_filter_display);
        dateFormat_start_db = dateFormat_filter_db;
        dateFormat_end_db = dateFormat_filter_db;

        button_cal_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context_Activity,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                calendat_for_otchet.set(year, monthOfYear, dayOfMonth);

                                SimpleDateFormat dateFormat_filter_db_format = new SimpleDateFormat("yyyy-MM-dd");       // формат даты для поиска в базе
                                dateFormat_start_db = dateFormat_filter_db_format.format(calendat_for_otchet.getTime());

                                SimpleDateFormat dateFormat_filter_display_format = new SimpleDateFormat("dd.MM.yyyy");  // формат для экрана даты
                                dateFormat_start_display = dateFormat_filter_display_format.format(calendat_for_otchet.getTime());
                                textView_cal_start.setText(dateFormat_start_display);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        button_cal_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context_Activity,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                calendat_for_otchet.set(year, monthOfYear, dayOfMonth);

                                SimpleDateFormat dateFormat_filter_db_format = new SimpleDateFormat("yyyy-MM-dd");       // формат даты для поиска в базе
                                dateFormat_end_db = dateFormat_filter_db_format.format(calendat_for_otchet.getTime());

                                SimpleDateFormat dateFormat_filter_display_format = new SimpleDateFormat("dd.MM.yyyy");  // формат для экрана даты
                                dateFormat_end_display = dateFormat_filter_display_format.format(calendat_for_otchet.getTime());
                                textView_cal_end.setText(dateFormat_end_display);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        button_otchet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Calendate_New();
                    if (dateFormat_start_db.compareTo(dateFormat_end_db) <= 0) {
                        Log.e("Return", "Верный формат дат");
                        otchet.clear();
                        List(dateFormat_start_db, dateFormat_end_db);
                        Loading_Kod_Brends();
                        Loading_Mass_Brends();
                        adapterPriceClients = new ListAdapterAde_WJ_Otchet(context_Activity, otchet);
                        adapterPriceClients.notifyDataSetChanged();
                        listview_otchet.setAdapter(adapterPriceClients);
                    } else {
                        Toast.makeText(context_Activity, "Неправильно задан период!", Toast.LENGTH_SHORT).show();
                        Log.e("Return", "Не верный формат дат");
                        otchet.clear();
                        adapterPriceClients = new ListAdapterAde_WJ_Otchet(context_Activity, otchet);
                        adapterPriceClients.notifyDataSetChanged();
                        listview_otchet.setAdapter(adapterPriceClients);
                    }

                } catch (Exception e) {
                    Log.e(context_Activity.getPackageName(), "Обработка данных дат!");
                    Toast.makeText(context_Activity, "нет данных для отображения или нет рабочей таблицы!!!", Toast.LENGTH_SHORT).show();
                }


            }

        });

    }

    // Обработка данных: сопоставлени и создание массива
    protected void Loading_Mass_Brends() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT * FROM base_in_nomeclature";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        mass_kod = new String[cursor.getCount()][2];
        while (cursor.isAfterLast() == false) {
            String brends = cursor.getString(cursor.getColumnIndex("brends"));
            String koduid = cursor.getString(cursor.getColumnIndex("koduid"));
            mass_kod[cursor.getPosition()][0] = brends;
            mass_kod[cursor.getPosition()][1] = koduid;

            //  Log.e(WJ_Otchet_Prodaj.this.getLocalClassName(), mass_kod[cursor.getPosition()][0] + " " + mass_kod[cursor.getPosition()][1]);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    // Обработка данных в базе
    protected void List(final String data_start, final String data_end) {
        try {
            SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
            db_prev.delete("otchet_brends_otgruz", null, null);
            db_prev.close();
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
            String query = "SELECT base_RN_All.kod_rn, base_RN_All.data_up, base_RN.Name, " +
                    "base_RN.koduid, base_RN.Kol, base_RN.Summa, base_RN.Itogo " +
                    "FROM base_RN_All\n" +
                    "INNER JOIN base_RN ON base_RN_All.kod_rn = base_RN.Kod_RN\n" +
                    "WHERE base_RN_All.Data BETWEEN '" + data_start + "' AND '" + data_end + "'";

            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            ContentValues localContentValues = new ContentValues();

            Double aut_summa = 0.0, aut_itogo = 0.0, aut_kol = 0.0;
            while (cursor.isAfterLast() == false) {
                String koduid = cursor.getString(cursor.getColumnIndex("koduid"));
                String kol = cursor.getString(cursor.getColumnIndex("Kol"));
                String summa = cursor.getString(cursor.getColumnIndex("Summa"));
                String itogo = cursor.getString(cursor.getColumnIndex("Itogo"));
                for (int i = 0; i < mass_kod.length; i++) {
                    if (koduid.equals(mass_kod[i][1])) {
                        Log.e(Otchet_Brends_Trade.this.getLocalClassName(), mass_kod[i][0] + "__" + mass_kod[i][1]);
                        localContentValues.put("brends", mass_kod[i][0]);
                        localContentValues.put("kol", kol);
                        localContentValues.put("summa", summa);
                        localContentValues.put("itogo", itogo);
                        db.insert("otchet_brends_otgruz", null, localContentValues);

                        aut_summa = aut_summa + Double.parseDouble(summa);
                        aut_kol = aut_kol + Double.parseDouble(kol);
                        aut_itogo = aut_itogo + Double.parseDouble(itogo);
                    }
                }
                cursor.moveToNext();
            }
            textView_summa.setText(new DecimalFormat("#00.00").format(aut_summa));
            textView_kol.setText(new DecimalFormat("#").format(aut_kol));
            textView_itogo.setText(new DecimalFormat("#00.00").format(aut_itogo));
            cursor.close();
            db.close();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // Обработка данных: поиск кода бренда
    protected void Loading_Kod_Brends() {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
            String query = "SELECT DISTINCT brends, sum(kol) as 'kol', sum(summa) as 'summa', sum(itogo) as 'itogo' FROM otchet_brends_otgruz \n" +
                    "GROUP BY brends;";

            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String brends = cursor.getString(cursor.getColumnIndex("brends"));
                String kol = cursor.getString(cursor.getColumnIndex("kol"));
                String summa = cursor.getString(cursor.getColumnIndex("summa"));
                String itogo = cursor.getString(cursor.getColumnIndex("itogo"));
                otchet.add(new ListAdapterSimple_WJ_Otchet(brends, kol + " шт", summa + " c", itogo + " c"));
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Загрузка даты и время
    protected void Calendate_New() {
        mYear = calendat_for_otchet.get(Calendar.YEAR);
        mMonth = calendat_for_otchet.get(Calendar.MONTH);
        mDay = calendat_for_otchet.get(Calendar.DAY_OF_MONTH);

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

        SimpleDateFormat dateFormat_filter_db_format = new SimpleDateFormat("yyyy-MM-dd");       // формат даты для поиска в базе
        dateFormat_filter_db = dateFormat_filter_db_format.format(calendar.getTime());

        SimpleDateFormat dateFormat_filter_display_format = new SimpleDateFormat("dd.MM.yyyy");  // формат для экрана даты
        dateFormat_filter_display = dateFormat_filter_display_format.format(calendar.getTime());

        data_select_start = dateFormat_filter_db;
        data_select_end = dateFormat_filter_db;


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