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
import android.view.SubMenu;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import kg.roman.Mobile_Torgovla.Permission.PrefActivity_Nomeclatura;
import kg.roman.Mobile_Torgovla.R;


public class SPR_Nomenclature_Brends extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Context context_Activity;
    public NavigationView navigationView;
    public Menu menu;
    public Toolbar toolbar;
    public String[] mass_menu_product;
    public SharedPreferences.Editor ed;
    public SharedPreferences sp;
    public String PEREM_FTP_SERV, PEREM_FTP_LOGIN, PEREM_FTP_PASS, PEREM_FTP_DISTR_XML, PEREM_FTP_DISTR_db3,
            PEREM_IMAGE_PUT_SDCARD, PEREM_IMAGE_PUT_PHONE;
    public String PEREM_MAIL_LOGIN, PEREM_MAIL_PASS, PEREM_MAIL_START, PEREM_MAIL_END,
            PEREM_DB3_CONST, PEREM_DB3_BASE, PEREM_DB3_RN, PEREM_ANDROID_ID_ADMIN, PEREM_ANDROID_ID;
    public String PEREM_AG_UID, PEREM_AG_NAME, PEREM_AG_REGION, PEREM_AG_UID_REGION, PEREM_AG_CENA,
            PEREM_AG_SKLAD, PEREM_AG_UID_SKLAD, PEREM_AG_TYPE_REAL, PEREM_AG_TYPE_USER,
            PEREM_WORK_DISTR, PEREM_KOD_MOBILE, PEREM_KOD_BRENDS_VISIBLE;
    public String Save_dialog_up_;
    public Boolean switch_keys_all_brends, switch_keys_catigory;
    public SubMenu topChannelMenu_01, topChannelMenu_02;
    public SubMenu[] subMenus;
    public String[][] mass_submenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt_spr_nomenclatura);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.e("Class_Name: ", "|" + this.getLocalClassName());  // имя используемого класса

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.office_title_forma_2);
        getSupportActionBar().setTitle("Номенклатура");
        getSupportActionBar().setSubtitle("");

        DrawerLayout drawer = findViewById(R.id.drawer_layout_nomenclature);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view_nomenclature);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.menu_nomeclature_brends);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        context_Activity = SPR_Nomenclature_Brends.this;
        Constanta_Read();

        try {
            Navigation_Menu();
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка номенклатуры", Toast.LENGTH_SHORT).show();
            Log.e("Error...", "Ошибка номенклатуры");
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent intent3 = new Intent(context_Activity, PrefActivity_Nomeclatura.class);
            startActivity(intent3);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String id_st = item.getTitle().toString();
        Log.e("Brends ", id_st);
        getSupportActionBar().setSubtitle(id_st);
        ed = sp.edit();
        ed.putString("PEREM_SELECT_BRENDS", id_st);
        ed.commit();
        Intent intent = new Intent(context_Activity, SPR_Nomenclature_Brends_SubBrends.class);
        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_nomenclature);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void Navigation_Menu() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);

        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        Menu m = navigationView.getMenu();
        m.clear();
        topChannelMenu_01 = m.addSubMenu("TradeGof");
        topChannelMenu_02 = m.addSubMenu("Sunbell");
       /* topChannelMenu1 = m.addSubMenu("TradeGof");
        topChannelMenu2 = m.addSubMenu("Sunbell (категория-1)");
        topChannelMenu3 = m.addSubMenu("Sunbell (категория-2)");
        topChannelMenu4 = m.addSubMenu("Sunbell (категория-3)");
        topChannelMenu5 = m.addSubMenu("Sunbell (категория-4)");*/
       /* subMenus = new SubMenu[2];
        subMenus[0] = m.addSubMenu("TradeGof");
        subMenus[1] = m.addSubMenu("Sunbell");*/
        try {
            // Отбор по настройкам
            if (sp.getBoolean("switch_preference_sunbell_all_brends", true)) {
                if (sp.getBoolean("switch_preference_firma", true)) {
                    // Условие все для всех + деление по фирмам
                    Log.e("SWITCH", "Условие все для всех + деление по фирмам");
                    Switch_ALL_Firma(m);
                } else {
                    // Условие все для всех
                    Log.e("SWITCH", "Условие все для всех");
                    Switch_ALL(m);
                }
            } else {
                if (sp.getBoolean("switch_preference_firma", true)) {
                    // Условие для агентов из доступного списка + деление по фирмам
                    Log.e("SWITCH", "Все бренды открытые для агентов + деление по фирмам");
                    Decoder_Prefix_Firma(m);
                } else {
                    // Условие для агентов из доступного списка
                    Log.e("SWITCH", "Все бренды открытые для агентов");
                    Decoder_Prefix(m);
                }
            }
        } catch (Exception e) {
            Log.e(this.getLocalClassName(), "Произошла ошибка, заполнения номенклатуры!");
            Toast.makeText(this, "Произошла ошибка, заполнения номенклатуры!", Toast.LENGTH_LONG).show();
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
        PEREM_KOD_BRENDS_VISIBLE = sp.getString("PEREM_KOD_BRENDS_VISIBLE", "0");                 //чтение данных:


        // отображение всех брендов (Все для всех)
        if (sp.getBoolean("switch_preference_sunbell_all_brends", true)) {
            switch_keys_all_brends = false;
            Log.e("check_box_brends", "TR? Бренда, заводские: ");
        } else {
            switch_keys_all_brends = true;
            Log.e("check_box_brends", "FL, Бренда, пользоы ");
        }
        // сортировать по категориям
        if (sp.getBoolean("switch_preference_sunbell_catg", true)) {
            switch_keys_catigory = true;
            Log.e("SWITCH", "Нажата");
        } else {
            switch_keys_catigory = false;
            Log.e("SWITCH", "выкл");
        }

        Set<String> hs = sp.getStringSet("multi_select_list_preference_brends", new HashSet<String>());
        Log.e("Настройки: ", "Бренды: = (" + sp.getStringSet("multi_select_list_preference_brends", new HashSet<String>()) + ")");
        Log.e("Массив: ", "" + hs.size());
        mass_menu_product = new String[hs.size()];
        Iterator<String> intr = hs.iterator();
        int m = 0;
        while (intr.hasNext()) {
            mass_menu_product[m] = intr.next();
            Log.e("Массив: ", mass_menu_product[m]);
            m++;
        }

        if (hs.size() > 0) {
            Log.e("Бренды", "Есть данные");
        } else Log.e("Бренды", "Все бренды");

        for (int i = 0; i < getResources().getStringArray(R.array.mass_for_update_data).length; i++) {
            Save_dialog_up_ = sp.getString("Save_dialog_up_" + i, "0");
        }
    }










    // Условие №1 (Все из всех)
    protected void Switch_ALL(Menu m) {
        subMenus = new SubMenu[1];
        subMenus[0] = m.addSubMenu("Номенклатура");
        SQLiteDatabase db_menu = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query_menu = "SELECT * FROM base_in_brends_id\n" +
                "ORDER BY brends;";
        final Cursor cursor_menu = db_menu.rawQuery(query_menu, null);
        cursor_menu.moveToFirst();
        while (!cursor_menu.isAfterLast()) {
            String kod = cursor_menu.getString(cursor_menu.getColumnIndexOrThrow("kod"));                   // столбец
            String prefic = cursor_menu.getString(cursor_menu.getColumnIndexOrThrow("prefic"));             // столбец
            String group_type = cursor_menu.getString(cursor_menu.getColumnIndexOrThrow("group_type"));     // столбец
            String brends = cursor_menu.getString(cursor_menu.getColumnIndexOrThrow("brends"));             // столбец с брендом
            Image_Icon_Brends_Switch(prefic, 0, brends);
            cursor_menu.moveToNext();
        }
        cursor_menu.close();
        db_menu.close();
    }
    // Условие №1.2 (Все из всех) + деление по фирмам
    protected void Switch_ALL_Firma(Menu m) {
        SQLiteDatabase db_menu = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
        String query_menu = "SELECT * FROM const_group_name;";
        final Cursor cursor_menu = db_menu.rawQuery(query_menu, null);
        cursor_menu.moveToFirst();
        subMenus = new SubMenu[cursor_menu.getCount()];
        mass_submenu = new String[cursor_menu.getCount()][2];
        while (!cursor_menu.isAfterLast()) {
            String id = cursor_menu.getString(cursor_menu.getColumnIndexOrThrow("group_id"));            // столбец
            String name = cursor_menu.getString(cursor_menu.getColumnIndexOrThrow("group_name"));        // столбец
            String group_p = cursor_menu.getString(cursor_menu.getColumnIndexOrThrow("group_parent"));   // столбец
            mass_submenu[cursor_menu.getPosition()][0] = id;
            mass_submenu[cursor_menu.getPosition()][1] = name;
            subMenus[cursor_menu.getPosition()] = m.addSubMenu(name);
            Log.e("MENU", "Списов оглавлений_" + cursor_menu.getPosition() + ") " + name);
            cursor_menu.moveToNext();
        }
        cursor_menu.close();
        Sunbell_Switch_Category();
        db_menu.close();
    }

    // Условие №2 все бренды доступные для агентов
    protected void Decoder_Prefix(Menu m) {
        String tt = PEREM_KOD_BRENDS_VISIBLE;                                                                                          // Список доступных брендов
        if (tt.equals("/ALL/")) {
            Switch_ALL(m);
        } else {
            int kol = PEREM_KOD_BRENDS_VISIBLE.replaceAll(",", "").replaceAll("/", "").length() / 2;  // количество символов
            String[] mass_new_pref = tt.replaceAll("/", "").toLowerCase().split("[,]+");
            String[] mass_name_brends = new String[kol];
            // mass_new_pref = tt.replaceAll("/", "").toLowerCase().split("[,]+");
// mass_new_pref[i] = tt.split("[,]").toString();
            for (String value : mass_new_pref) Log.e("Deco ", "Массив^ " + value);
            subMenus = new SubMenu[1];
            subMenus[0] = m.addSubMenu("Номенклатура");
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
            String query = "SELECT * FROM base_in_brends_id\n" +
                    "ORDER BY group_type;";
            final Cursor cursor = db.rawQuery(query, null);
            for (int k = 0; k < mass_new_pref.length; k++) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String prefic = cursor.getString(cursor.getColumnIndexOrThrow("prefic"));             // столбец
                    String brends = cursor.getString(cursor.getColumnIndexOrThrow("brends"));             // столбец с брендом
                    if (!prefic.equals(null)) {
                        if (mass_new_pref[k].equals(prefic)) {
                            mass_name_brends[k] = brends;
                            Log.e("DecoS ", "Условие: " + mass_new_pref[k] + "__" + prefic + "/ " + brends);
                        }
                    } else {
                        mass_name_brends[k] = "Err";
                        cursor.moveToLast();
                    }
                    cursor.moveToNext();
                }
            }
            cursor.close();
            db.close();

            for (int s = 0; s < mass_new_pref.length; s++) {
                Image_Icon_Brends_Switch(mass_new_pref[s], 0, mass_name_brends[s]);
            }
        }
    }
    // Условие №2.2 все бренды доступные для агентов + деление по фирмам
    protected void Decoder_Prefix_Firma(Menu m) {
       /* String tt = PEREM_KOD_BRENDS_VISIBLE;
        String[] mass_new_pref = new String[tt.replaceAll(",", "").replaceAll("/", "").length() / 2];
        String[] mass_name_brends = new String[tt.replaceAll(",", "").replaceAll("/", "").length() / 2];
        mass_new_pref = tt.replaceAll("/", "").toLowerCase().split("[,]+");*/


        String tt = PEREM_KOD_BRENDS_VISIBLE;                                                                                          // Список доступных брендов
        if (tt.equals("/ALL/")) {
            Switch_ALL_Firma(m);
        } else {
            int kol = PEREM_KOD_BRENDS_VISIBLE.replaceAll(",", "").replaceAll("/", "").length() / 2;  // количество символов
            String[] mass_new_pref = tt.replaceAll("/", "").toLowerCase().split("[,]+");
            String[] mass_name_brends = new String[kol];
            // mass_new_pref[i] = tt.split("[,]").toString();

            for (String value : mass_new_pref) Log.e("Deco ", "Массив^ " + value);
            // Image_Icon_Brends_Switch(mass_new_pref[s], 0, mass_name_brends[s]);
       /* subMenus = new SubMenu[1];
        subMenus[0] = m.addSubMenu("Номенклатура123");*/

            SQLiteDatabase db_2 = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
            String query_2 = "SELECT * FROM const_group_name;";
            final Cursor cusrsor_2 = db_2.rawQuery(query_2, null);
            cusrsor_2.moveToFirst();
            subMenus = new SubMenu[cusrsor_2.getCount()];
            mass_submenu = new String[cusrsor_2.getCount()][2];
            while (!cusrsor_2.isAfterLast()) {
                String id = cusrsor_2.getString(cusrsor_2.getColumnIndexOrThrow("group_id"));            // столбец
                String name = cusrsor_2.getString(cusrsor_2.getColumnIndexOrThrow("group_name"));        // столбец
                String group_p = cusrsor_2.getString(cusrsor_2.getColumnIndexOrThrow("group_parent"));   // столбец
                mass_submenu[cusrsor_2.getPosition()][0] = id;
                mass_submenu[cusrsor_2.getPosition()][1] = name;
                subMenus[cusrsor_2.getPosition()] = m.addSubMenu(name);
                Log.e("MENU", "Списов оглавлений_" + cusrsor_2.getPosition() + ") " + name);
                cusrsor_2.moveToNext();
            }
            cusrsor_2.close();
            db_2.close();

            SQLiteDatabase db_menu = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
            String query_menu = "SELECT * FROM base_in_brends_id\n" +
                    "ORDER BY group_type;";
            final Cursor cursor_menu = db_menu.rawQuery(query_menu, null);
            for (int k = 0; k < mass_new_pref.length; k++) {
                cursor_menu.moveToFirst();
                while (!cursor_menu.isAfterLast()) {
                    String prefic = cursor_menu.getString(cursor_menu.getColumnIndexOrThrow("prefic"));             // столбец
                    String brends = cursor_menu.getString(cursor_menu.getColumnIndexOrThrow("brends"));             // столбец с брендом

                    if (!prefic.equals(null)) {

                        if (mass_new_pref[k].equals(prefic)) {
                            mass_name_brends[k] = brends;
                            Log.e("Deco ", "Условие: " + mass_new_pref[k] + "__" + prefic + "/ " + brends);
                        }
                        // else mass_name_brends[k] = "NULL";
                    } else {
                        mass_name_brends[k] = "Err";
                        cursor_menu.moveToLast();
                    }
                    cursor_menu.moveToNext();
                }
            }
            cursor_menu.close();
            db_menu.close();

            for (int s = 0; s < mass_new_pref.length; s++) {
                if (mass_name_brends[s] != null) {
                    Log.e("Deco1 ", "Массивы: " + mass_new_pref[s] + "___" + mass_name_brends[s]);
                } else
                    Log.e("Deco2 ", "Массивы: " + mass_new_pref[s] + "___" + mass_name_brends[s]);
            }

            SQLiteDatabase db_new = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
            String query_new = "SELECT * FROM base_in_brends_id\n" +
                    "ORDER BY group_type;";
            final Cursor cursor_new = db_new.rawQuery(query_new, null);
            for (int i = 0; i < mass_submenu.length; i++) {
                cursor_new.moveToFirst();
                while (!cursor_new.isAfterLast()) {
                    String kod = cursor_new.getString(cursor_new.getColumnIndexOrThrow("kod"));                   // столбец
                    String prefic = cursor_new.getString(cursor_new.getColumnIndexOrThrow("prefic"));             // столбец
                    String group_type = cursor_new.getString(cursor_new.getColumnIndexOrThrow("group_type"));     // столбец
                    String brends = cursor_new.getString(cursor_new.getColumnIndexOrThrow("brends"));             // столбец с брендом
                    if (mass_submenu[i][0].equals(group_type)) {
                        for (int l = 0; l < mass_new_pref.length; l++) {
                            if (mass_new_pref[l].equals(prefic)) {
                                Image_Icon_Brends_Switch(prefic, i, brends);
                            }
                        }
                    }
                    cursor_new.moveToNext();
                }
            }
            cursor_new.close();
            db_new.close();
        }
    }

    // Сортировка меню по группам (для Sunbell) по фирмам
    protected void Sunbell_Switch_Category() {
        SQLiteDatabase db_menu = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query_menu = "SELECT * FROM base_in_brends_id\n" +
                "ORDER BY group_type;";
        final Cursor cursor_menu = db_menu.rawQuery(query_menu, null);
        for (int i = 0; i < mass_submenu.length; i++) {
            cursor_menu.moveToFirst();
            while (cursor_menu.isAfterLast() == false) {
                String kod = cursor_menu.getString(cursor_menu.getColumnIndexOrThrow("kod"));                   // столбец
                String prefic = cursor_menu.getString(cursor_menu.getColumnIndexOrThrow("prefic"));             // столбец
                String group_type = cursor_menu.getString(cursor_menu.getColumnIndexOrThrow("group_type"));     // столбец
                String brends = cursor_menu.getString(cursor_menu.getColumnIndexOrThrow("brends"));             // столбец с брендом
                if (mass_submenu[i][0].equals(group_type)) {
                    Image_Icon_Brends_Switch(prefic, i, brends);
                }
                cursor_menu.moveToNext();
            }
        }
        cursor_menu.close();
        db_menu.close();
    }
    //  Заполнение категорий по префиксу именем и картинкой
    protected void Image_Icon_Brends_Switch(String w_prefix, int i, String w_name) {
        switch (w_prefix) {
            //////////// SUNBELL
            case "cs":  // Cussons
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_pz_cussons);
                break;
            case "hu":  // HALK HIJYENIK URUNLER
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_nice_lady);
                break;
            case "lc":  // Lacalute
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_lacalut);
                break;
            case "ot":  // Ontex
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_ontex);
                break;
            case "pc":  // PCC
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_pcc);
                break;
            case "vk":  // Villa_krim
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_villa_krim);
                break;
            case "dr":  // Драмерс
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_brait);
                break;
            case "kd":  // Kodak
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_kodak);
                break;
            case "bg":  // БиГ
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_big);
                break;
            case "nt":  // Наука и техника
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_ntm);
                break;
            case "ie":  // Палл инвест
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_polinvest);
                break;
            case "fn":  // Фараон
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_pharaon);
                break;
            case "tg":  // ТОРГХИМ
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_njk);
                break;
            case "ns":  // Первое решение
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_ns);
                break;
            case "pf":  // ПКФ СОНЦА
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_pkf_conca);
                break;
            case "sv":  // Свобода
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_svoboda);
                break;
            case "ps":  // PLUSHE
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_plushe);
                break;
            case "sm":  // SAMARELA
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_samarela);
                break;
            case "sp":  // SPLAT
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_splat);
                break;
            case "zn":  // ZONIN
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_zonin);
                break;
            case "sr":  // ОсОО "ЭРГОПАК"
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_sarantis);
                break;
            case "tr":  // ТЫМЛАТСКИЙ РЫБОКОМБИНАТ
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_t_fish);
                break;
            case "sh":  // Шенли
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_secret_lan);
                break;
            case "ct":  // CottonClub
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_cotton_club);
                break;
            case "yp":  // УФАПАКС
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_ufapack);
                break;

            //////////// TRADEGOF
            case "bl":  // Bella
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_bella);
                break;
            case "pd":  // President
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_president);
                break;
            case "ys":  // Yokosun
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_yokosun);
                break;
            case "vr":  // Viero
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_viero_2);
                break;
            case "dc":  // ДНС
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_dnc);
                break;

            default:
                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.ic_cost);
                break;
        }
    }
















    // Условие №3 (Все бренды выбранные для агента)
    protected void Switch_All_Brends_For_Agents(Menu m) {
        subMenus = new SubMenu[1];
        subMenus[0] = m.addSubMenu("Номенклатура");
        SQLiteDatabase db_menu = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query_menu = "SELECT * FROM base_in_brends_id\n" +
                "ORDER BY group_type;";
        final Cursor cursor_menu = db_menu.rawQuery(query_menu, null);
        cursor_menu.moveToFirst();
        while (!cursor_menu.isAfterLast()) {
            String kod = cursor_menu.getString(cursor_menu.getColumnIndexOrThrow("kod"));                   // столбец
            String prefic = cursor_menu.getString(cursor_menu.getColumnIndexOrThrow("prefic"));             // столбец
            String group_type = cursor_menu.getString(cursor_menu.getColumnIndexOrThrow("group_type"));     // столбец
            String brends = cursor_menu.getString(cursor_menu.getColumnIndexOrThrow("brends"));             // столбец с брендом
            Image_Icon_Brends_Switch(prefic, 0, brends);
            cursor_menu.moveToNext();
        }
        cursor_menu.close();
        db_menu.close();
    }

    // Сортировка меню по группам (для Sunbell) по фирмам
    protected void Sunbell_Switch_Category2(String[] w_mass) {
        SQLiteDatabase db_menu = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query_menu = "SELECT * FROM base_in_brends_id\n" +
                "ORDER BY brends;";
        final Cursor cursor_menu = db_menu.rawQuery(query_menu, null);
        cursor_menu.moveToFirst();
        for (int i = 0; i < w_mass.length; i++) {
            cursor_menu.moveToFirst();
            while (!cursor_menu.isAfterLast()) {
                String kod = cursor_menu.getString(cursor_menu.getColumnIndexOrThrow("kod"));                   // столбец
                String prefic = cursor_menu.getString(cursor_menu.getColumnIndexOrThrow("prefic"));             // столбец
                String group_type = cursor_menu.getString(cursor_menu.getColumnIndexOrThrow("group_type"));     // столбец
                String brends = cursor_menu.getString(cursor_menu.getColumnIndexOrThrow("brends"));             // столбец с брендом
                Log.e("FIRMA", w_mass[i] + "__" + group_type);
                if (w_mass[i].equals(group_type)) {
                    Image_Icon_Brends_Switch(prefic, i, brends);
                }
                cursor_menu.moveToNext();
            }
        }
        cursor_menu.close();
        db_menu.close();
    }



}
