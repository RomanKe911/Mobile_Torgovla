package kg.roman.Mobile_Torgovla.Spravochnik;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_Login;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Klients;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Login;
import kg.roman.Mobile_Torgovla.FormaZakaza.ListAdapterSimple_Klients;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.Work_Journal.WJ_New_Contragent;

public class SPR_Cnt_Agent extends AppCompatActivity {

    public ArrayList<ListAdapterSimple_Klients> klients = new ArrayList<ListAdapterSimple_Klients>();
    public ListAdapterAde_Klients adapterPriceClients;

    public ArrayList<ListAdapterSimple_Login> add_spinner_sklad = new ArrayList<ListAdapterSimple_Login>();
    public ListAdapterAde_Login adapterPriceClients_spinner_sklad;

    public Context context_Activity;
    public Spinner spinner;
    public ListView listView;
    public String[] mass_name;
    public String[][] mass_uid;
    public Integer resID;
    public TextView textView;
    public String kol;
    public String query;

    public SharedPreferences sp;
    public SharedPreferences.Editor ed;
    public String PEREM_FTP_SERV, PEREM_FTP_LOGIN, PEREM_FTP_PASS, PEREM_FTP_DISTR_XML, PEREM_FTP_DISTR_db3,
            PEREM_IMAGE_PUT_SDCARD, PEREM_IMAGE_PUT_PHONE;
    public String PEREM_MAIL_LOGIN, PEREM_MAIL_PASS, PEREM_MAIL_START, PEREM_MAIL_END,
            PEREM_DB3_CONST, PEREM_DB3_BASE, PEREM_DB3_RN, PEREM_ANDROID_ID_ADMIN, PEREM_ANDROID_ID;
    public String PEREM_AG_UID, PEREM_AG_NAME, PEREM_AG_REGION, PEREM_AG_UID_REGION, PEREM_AG_CENA,
            PEREM_AG_SKLAD, PEREM_AG_UID_SKLAD, PEREM_AG_TYPE_REAL, PEREM_AG_TYPE_USER,
            PEREM_WORK_DISTR, PEREM_KOD_MOBILE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt_spr_contragent);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.office_title_group);
        getSupportActionBar().setTitle("Sunbell");
        getSupportActionBar().setSubtitle("Контрагенты");
        context_Activity = SPR_Cnt_Agent.this;
        spinner = (Spinner) findViewById(R.id.spinner_uid);
        listView = (ListView) findViewById(R.id.listview_cagent);
        textView = (TextView) findViewById(R.id.tvw_kolcagn);
        Constanta_Read();
        // Выборка данных из базы: Агенты
        Loading_SQL_Agets();
        try {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("SPR_Cnt_agent ", "позиция" + i);
                }
            });


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    klients.clear();
                    ListAdapter_Spinner(mass_uid[i][0], mass_uid[i][1]);
                    adapterPriceClients = new ListAdapterAde_Klients(context_Activity, klients);
                    adapterPriceClients.notifyDataSetChanged();
                    listView.setAdapter(adapterPriceClients);
                    textView.setText(kol);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка данных: spinner", Toast.LENGTH_SHORT).show();
            Log.e("SPR_Cnt_agent", "Ошибка данных: spinner");
        }

        textView.setText(kol);
    }

    // Обработка выбора данных из спинера
    protected void ListAdapter_Spinner(String uid_agent, String uid_region) {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
            query = "SELECT * FROM const_contragents \n" +
                    "WHERE uid_agent = '" + uid_agent + "' AND roaduid = '" + uid_region + "'\n" +
                    "ORDER BY k_agent ASC";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            kol = String.valueOf(cursor.getCount());
            textView.setText(String.valueOf(cursor.getCount()));

            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(cursor.getColumnIndex("k_agent"));
                String uid = cursor.getString(cursor.getColumnIndex("uid_k_agent"));
                String adress = cursor.getString(cursor.getColumnIndex("adress"));

                String image_var = name.substring(0, 3);
            /*Log.e("SPR_Cnt_agent", name.substring(0, 3));
            Log.e("SPR_Cnt_agent", image_var.toLowerCase());
            Log.e("SPR_Cnt_agent", image_var.toUpperCase());*/
                switch (image_var.toLowerCase()) {
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
                klients.add(new ListAdapterSimple_Klients(name, uid, adress, resID, ""));
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
        }
        catch (Exception e)
        {
            Toast.makeText(context_Activity, "Ошибка в обработке данных: SQL_Agets", Toast.LENGTH_SHORT).show();
            Log.e("Error...", "Ошибка в обработке данных: SQL_Agets");
        }

    }

    // Выборка данных из базы: Агенты
    protected void Loading_SQL_Agets() {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
            query = "SELECT uid_name, name, uid_region, kod_mobile \n" +
                    "FROM const_agents \n" +
                    "WHERE kod_mobile > 0\n" +
                    "ORDER BY name ASC";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            mass_name = new String[cursor.getCount()];
            mass_uid = new String[cursor.getCount()][2];
            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String uid_name = cursor.getString(cursor.getColumnIndex("uid_name"));
                String uid_region = cursor.getString(cursor.getColumnIndex("uid_region"));
                mass_name[cursor.getPosition()] = name;
                mass_uid[cursor.getPosition()][0] = uid_name;
                mass_uid[cursor.getPosition()][1] = uid_region;
                cursor.moveToNext();
            }
            cursor.close();
            db.close();

           /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mass_name);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);*/
            add_spinner_sklad.clear();

            for (int i = 0; i < mass_name.length; i++) {
                add_spinner_sklad.add(new ListAdapterSimple_Login(mass_name[i], ""));
            }
            adapterPriceClients_spinner_sklad = new ListAdapterAde_Login(context_Activity, add_spinner_sklad);
            adapterPriceClients_spinner_sklad.notifyDataSetChanged();
            spinner.setAdapter(adapterPriceClients_spinner_sklad);
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка в обработке данных: SQL_Agets", Toast.LENGTH_SHORT).show();
            Log.e("Error...", "Ошибка в обработке данных: SQL_Agets");
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

        PEREM_KOD_MOBILE = sp.getString("PEREM_KOD_MOBILE", "0");                 //чтение данных:
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_c_agent, menu);
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
            Intent intent_prefActivity = new Intent(context_Activity, WJ_New_Contragent.class);
            intent_prefActivity.putExtra("Layout_INTENT_BACK", "GLOBAL");
            startActivity(intent_prefActivity);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
