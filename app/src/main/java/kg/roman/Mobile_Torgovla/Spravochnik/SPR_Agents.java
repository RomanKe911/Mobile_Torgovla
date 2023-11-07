package kg.roman.Mobile_Torgovla.Spravochnik;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Agent;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Agent;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.Work_Journal.WJ_Global_Activity;

public class SPR_Agents extends AppCompatActivity {

    public ArrayList<ListAdapterSimple_Agent> agent = new ArrayList<ListAdapterSimple_Agent>();
    public ListAdapterAde_Agent adapterPriceClients;
    public Context context_Activity;
    public ListView listView;
    public Integer resID;
    public String str;
    public Toolbar toolbar;
    public ArrayAdapter<String> adapter;

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
        setContentView(R.layout.mt_spr_sotrudniki);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.office_title_user);
        getSupportActionBar().setTitle("Sunbell");
        getSupportActionBar().setSubtitle("Сотрудники");
        context_Activity = SPR_Agents.this;
        Constanta_Read();
        listView = (ListView) findViewById(R.id.lv_agent);
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
            String query = "SELECT * FROM const_agents ORDER BY name ASC;";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String uid_name = cursor.getString(cursor.getColumnIndexOrThrow("uid_name"));           // код клиента
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));                   // код клиента
                String type_user = cursor.getString(cursor.getColumnIndexOrThrow("type_user"));         // код клиента
                String region = cursor.getString(cursor.getColumnIndexOrThrow("region"));               // код клиента
                String uid_region = cursor.getString(cursor.getColumnIndexOrThrow("uid_region"));       // код клиента
                if (uid_region != null) {

                    if (type_user != null) {
                        switch (type_user) {
                            case "Агент": {
                                str = "Торговый агент";
                                resID = getResources().getIdentifier("img_agt", "drawable", getPackageName());
                            }
                            break;
                            case "Экспедитор":
                                str = "Экспедитор";
                                resID = getResources().getIdentifier("img_expd", "drawable", getPackageName());
                                break;
                            case "Зав.складом":
                                str = "Зав.складом";
                                resID = getResources().getIdentifier("img_skl", "drawable", getPackageName());
                                break;
                            case "Водитель":
                                str = "Водитель";
                                resID = getResources().getIdentifier("img_driver", "drawable", getPackageName());
                                break;
                            case "Оператор":
                                str = "Оператор";
                                resID = getResources().getIdentifier("img_opert", "drawable", getPackageName());
                                break;
                            case "Бухгалтер":
                                str = "Бухгалтер";
                                resID = getResources().getIdentifier("img_money", "drawable", getPackageName());
                                break;
                            default: {
                                str = "_-_-_";
                                resID = getResources().getIdentifier("img_null", "drawable", getPackageName());
                            }
                            break;
                        }
                        agent.add(new ListAdapterSimple_Agent(name, uid_name, str, resID, region));
                        cursor.moveToNext();
                    } else cursor.moveToNext();
                }
            }
            cursor.close();
            db.close();
            adapterPriceClients = new ListAdapterAde_Agent(context_Activity, agent);
            adapterPriceClients.notifyDataSetChanged();
            listView.setAdapter(adapterPriceClients);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("Agent^ ", "Номер:" + i);
                }
            });
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка вывода данных!", Toast.LENGTH_SHORT).show();
            Log.e("SPR_Agents", "Ошибка вывода данных!");
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
    }
}
