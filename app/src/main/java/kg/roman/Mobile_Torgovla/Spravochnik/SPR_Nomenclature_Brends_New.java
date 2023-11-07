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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Suncape;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Suncape;
import kg.roman.Mobile_Torgovla.Permission.PrefActivity_Nomeclatura;
import kg.roman.Mobile_Torgovla.R;

import static java.lang.Boolean.FALSE;


public class SPR_Nomenclature_Brends_New extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public ArrayList<ListAdapterSimple_Suncape> product_str = new ArrayList<ListAdapterSimple_Suncape>();
    public ListAdapterAde_Suncape adapterPriceClients;

    public Context context_Activity;
    public NavigationView navigationView;
    public Menu menu;
    public Toolbar toolbar;
    public MenuItem nav_menuItem;
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
    public Boolean pref_checkbox_brends;
    public SubMenu topChannelMenu1, topChannelMenu2, topChannelMenu3, topChannelMenu4, topChannelMenu5, topChannelMenu_01, topChannelMenu_02;

    public String Name_new, SName_new, w_kodUNIV = "";
    public SubMenu topChannelMenu;
    public int navigation_kol_down = 0;
    public String query, kod_next_perents = "", kod_prev_perents = "";
    public String[] topMenu_mass_name, topMenu_mass_kod;
    public View navHeader;
    public ImageView imageView_header;
    public TextView textView_header1, textView_header2;
    public LinearLayout linearLayout_header;
    public String PEREM_SELECT_BRENDS;
    public Boolean perem_switch_group_sql, perem_switch_full_tovar;
    public String ostatok;
    public String Cena_For;
    public String Cena_All, Cena_Nal, Cena_Kons, cena;
    public Double TY, TY_Kons;
    public Double Doub_Cena, Doub_Kons, Doub_Nal;
    public ListView listView;
    public Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt_spr_nomenclatura);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.e("Class_Name: ", "|" + this.getLocalClassName());  // имя используемого класса
        context_Activity = SPR_Nomenclature_Brends_New.this;

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.office_title_forma_2);
        getSupportActionBar().setTitle("Номенклатура");
        getSupportActionBar().setSubtitle("");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_nomenclature);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view_nomenclature);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.menu_nomeclature_brends);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        navigation_kol_down = 1;
        Constanta_Read();

        listView = (ListView) findViewById(R.id.ListView_Klients);

        try {
            Navigation_Menu();
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка номенклатуры", Toast.LENGTH_SHORT).show();
            Log.e("Error...", "Ошибка номенклатуры");
        }

    }

    @Override
    public void onBackPressed() {
        //finish();
        navigation_kol_down--;
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.menu_nomeclature_brends);
        navigationView.setNavigationItemSelectedListener(this);
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        Navigation_Menu_SubBrends(sp.getString("PEREM_SELECT_BRENDS", "0"), "", navigation_kol_down);
        Toast.makeText(context_Activity, "Нажата кнопка назад!", Toast.LENGTH_SHORT).show();
        if (navigation_kol_down == 0) {
            Log.e("BACK", "DOWN" + navigation_kol_down);
            super.onBackPressed();
        } else Log.e("BACK_NO", "DOWN" + navigation_kol_down);

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String id_st = item.getTitle().toString();
        // Log.e("Brends ", id_st);
        getSupportActionBar().setSubtitle(id_st);
        ed = sp.edit();
        ed.putString("PEREM_SELECT_BRENDS", id_st);
        ed.commit();
        /*Intent intent = new Intent(context_Activity, SPR_Nomenclature_Brends_SubBrends.class);
        startActivity(intent);*/

        /// 11.09.2022

        navigation_kol_down++;
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.menu_nomeclature_brends);
        navigationView.setNavigationItemSelectedListener(this);
        // Поиск кода для след. структуру
        for (int k = 0; k < topMenu_mass_name.length; k++) {
            // переводи к единой строке(все пробулы и нижний регистр)
            String menu_id = id_st.replaceAll(" ", "").toLowerCase();
            String menu_id_new = topMenu_mass_name[k].replaceAll(" ", "").toLowerCase();
            if (menu_id.equals(menu_id_new)) {
                kod_next_perents = topMenu_mass_kod[k];
                Navigation_Menu_SubBrends(topMenu_mass_name[k], topMenu_mass_kod[k], navigation_kol_down);
                Navigation_Title_Image();
            } else kod_next_perents = "END";
        }
        /// 11.09.2022
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

        topChannelMenu1 = m.addSubMenu("TradeGof");
        topChannelMenu2 = m.addSubMenu("Sunbell (категория-1)");
        topChannelMenu3 = m.addSubMenu("Sunbell (категория-2)");
        topChannelMenu4 = m.addSubMenu("Sunbell (категория-3)");
        topChannelMenu5 = m.addSubMenu("Sunbell (категория-4)");

        try {
            String query = "SELECT * FROM base_in_brends_id";
            final Cursor cursor = db.rawQuery(query, null);
            topMenu_mass_name = new String[cursor.getCount()];
            topMenu_mass_kod = new String[cursor.getCount()];
            if (pref_checkbox_brends.booleanValue() == true) {
                Log.e("Условие3 ", "ДА");
                if (mass_menu_product.length > 0) {
                    Log.e("Условие1 ", "ДА");
                    cursor.moveToFirst();
                    while (cursor.isAfterLast() == false) {
                        String name = cursor.getString(cursor.getColumnIndexOrThrow("brends"));         // столбец с брендом
                        String kod = cursor.getString(cursor.getColumnIndexOrThrow("kod"));            // столбец с кодом
                        String group = cursor.getString(cursor.getColumnIndexOrThrow("group_type"));    // столбец с код группы
                        String pref = cursor.getString(cursor.getColumnIndexOrThrow("prefic"));         // столбец с префиксом группы
                        Decoder_Prefix(pref);
                        for (int i = 0; i < mass_menu_product.length; i++) {
                            if (pref.contains(mass_menu_product[i])) {
                                //  Log.e("Multi_Case ", mass_menu_product[i] + "__" + pref + "__" + i);
                                if (sp.getBoolean("switch_preference_sunbell_catg", true))
                                    Sunbell_Switch_Category(name, group, kod, cursor.getPosition());
                                else Sunbell_Switch_Category_NoSwitch(name, group);
                            }
                        }
                        cursor.moveToNext();
                    }
                } else {
                    Log.e("Условие2 ", "ДА");
                    cursor.moveToFirst();
                    while (cursor.isAfterLast() == false) {
                        String name = cursor.getString(cursor.getColumnIndexOrThrow("brends"));         // столбец с брендом
                        String kod = cursor.getString(cursor.getColumnIndexOrThrow("kod"));            // столбец с кодом
                        String group = cursor.getString(cursor.getColumnIndexOrThrow("group_type"));    // столбец с префиксом группы
                        String pref = cursor.getString(cursor.getColumnIndexOrThrow("prefic"));         // столбец с префиксом группы
                        Log.e("MASS2= ", "_" + cursor.getCount());
                        Decoder_Prefix(pref);
                        if (sp.getBoolean("switch_preference_sunbell_catg", true))
                            Sunbell_Switch_Category(name, group, kod, cursor.getPosition());
                        else Sunbell_Switch_Category_NoSwitch(name, group);
                        cursor.moveToNext();
                    }
                }
            } else {
                Log.e("Условие4 ", "ДА");
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("brends"));        // столбец с брендом
                    String kod = cursor.getString(cursor.getColumnIndexOrThrow("kod"));            // столбец с кодом
                    String group = cursor.getString(cursor.getColumnIndexOrThrow("group_type"));   // столбец с префиксом группы
                    String pref = cursor.getString(cursor.getColumnIndexOrThrow("prefic"));   // столбец с префиксом группы
                    Log.e("MASS3= ", "_" + cursor.getCount());
                    Decoder_Prefix(pref);
                    if (PEREM_KOD_BRENDS_VISIBLE.contains(pref.toUpperCase())) {
                        if (sp.getBoolean("switch_preference_sunbell_catg", true))
                            Sunbell_Switch_Category(name, group, kod, cursor.getPosition());
                        else Sunbell_Switch_Category_NoSwitch(name, group);
                    } else if (PEREM_KOD_BRENDS_VISIBLE.equals("/ALL/")) {
                        if (sp.getBoolean("switch_preference_sunbell_catg", true))
                            Sunbell_Switch_Category(name, group, kod, cursor.getPosition());
                        else Sunbell_Switch_Category_NoSwitch(name, group);
                    }
                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(this.getLocalClassName(), "Произошла ошибка, заполнения номенклатуры!");
            Toast.makeText(this, "Произошла ошибка, заполнения номенклатуры!", Toast.LENGTH_LONG).show();
        }

        db.close();

        for (int k = 0; k < topMenu_mass_kod.length; k++) {
            Log.e("Структура_1 : ", "name->" + topMenu_mass_name[k] + " kod->" + topMenu_mass_kod[k]);
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


        if (sp.getBoolean("check_box_brends", true) == true) {
            pref_checkbox_brends = true;
            Log.e("check_box_brends", "TR? Бренда, заводские: ");
        } else {
            pref_checkbox_brends = false;
            Log.e("check_box_brends", "FL, Бренда, пользоы ");
        }

        Set<String> hs = sp.getStringSet("multi_select_list_preference_brends", new HashSet<String>());
        // Log.e("Настройки: ", "Бренды: = (" + sp.getStringSet("multi_select_list_preference_brends", new HashSet<String>()) + ")");
        //  Log.e("Массив: ", "" + hs.size());
        mass_menu_product = new String[hs.size()];
        Iterator<String> intr = hs.iterator();
        int m = 0;
        while (intr.hasNext()) {
            mass_menu_product[m] = intr.next();
            // Log.e("Массив: ", mass_menu_product[m]);
            m++;

        }


        if (hs.size() > 0) {
            Log.e("Бренды", "Есть данные");
        } else Log.e("Бренды", "Все бренды");

        for (int i = 0; i < getResources().getStringArray(R.array.mass_for_update_data).length; i++) {
            Save_dialog_up_ = sp.getString("Save_dialog_up_" + i, "0");
        }
    }

    protected void Decoder_Prefix(String w_prefix) {
        String tt = PEREM_KOD_BRENDS_VISIBLE;
        Integer ff = tt.codePointCount(0, tt.length());
        //  Log.e("PREFIX ", "" + ff);

        for (int i = 0; i < tt.length(); i++) {
            if (tt.charAt(i) == '/') {
                // Log.e("Позиция: ", "" + i);
            }
        }
        // Log.e("Верх: ", w_prefix.toLowerCase());
        // Log.e("Низ: ", w_prefix.toUpperCase());
        if (tt.contains(w_prefix.toUpperCase())) {
            Log.e("Позиция1 ", "" + w_prefix.toLowerCase());
        } //else Log.e("Позиция: ", "нет совпадений");
    }

    // Сортировка меню по группам (для Sunbell)
    protected void Sunbell_Switch_Category(String w_name, String w_group, String w_kod, int w_mass_int) {
        switch (w_group) {
            case "gr_1": {
                topChannelMenu1.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.logo_bella_small);
                topMenu_mass_name[w_mass_int] = w_name.substring(0, 1) + w_name.substring(1).toLowerCase();
                topMenu_mass_kod[w_mass_int] = w_kod;
            }
            break;
            case "gr_2": {
                topChannelMenu2.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.ic_cost_2);
                topMenu_mass_name[w_mass_int] = w_name.substring(0, 1) + w_name.substring(1).toLowerCase();
                topMenu_mass_kod[w_mass_int] = w_kod;
            }
            break;
            case "gr_3": {
                topChannelMenu3.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.ic_cost_3);
                topMenu_mass_name[w_mass_int] = w_name.substring(0, 1) + w_name.substring(1).toLowerCase();
                topMenu_mass_kod[w_mass_int] = w_kod;
            }

            break;
            case "gr_4": {
                topChannelMenu4.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.ic_cost_4);
                topMenu_mass_name[w_mass_int] = w_name.substring(0, 1) + w_name.substring(1).toLowerCase();
                topMenu_mass_kod[w_mass_int] = w_kod;
            }

            break;
            case "gr_5": {
                topChannelMenu5.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.ic_cost);
                topMenu_mass_name[w_mass_int] = w_name.substring(0, 1) + w_name.substring(1).toLowerCase();
                topMenu_mass_kod[w_mass_int] = w_kod;
            }

            break;
            default: {
                topChannelMenu1.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.ic_cost);
                topMenu_mass_name[w_mass_int] = w_name.substring(0, 1) + w_name.substring(1).toLowerCase();
                topMenu_mass_kod[w_mass_int] = w_kod;
            }
            break;
        }
    }

    protected void Sunbell_Switch_Category_NoSwitch(String w_name, String w_group) {
        switch (w_group) {
            case "gr_1":
                topChannelMenu_01.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.ic_cost);
                break;
            default:
                topChannelMenu_02.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.ic_cost);
                break;
        }
    }

    // Загрузка визуального оформления для меню по брендам
    protected void Navigation_Title_Image() {
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        PEREM_SELECT_BRENDS = sp.getString("PEREM_SELECT_BRENDS", "0");
        navHeader = navigationView.getHeaderView(0);
        imageView_header = (ImageView) navHeader.findViewById(R.id.nav_header_nomenclature);
        textView_header1 = (TextView) navHeader.findViewById(R.id.textView_title_1);
        textView_header2 = (TextView) navHeader.findViewById(R.id.textView_title_2);
        linearLayout_header = (LinearLayout) navHeader.findViewById(R.id.lineary_header);


        switch (PEREM_SELECT_BRENDS) {
            case "Bella": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.bella_colorPrimary));
                getSupportActionBar().setIcon(R.drawable.logo_bella);
                imageView_header.setImageResource(R.drawable.logo_bella);
                textView_header1.setText(R.string.header_bella_title_1);
                textView_header2.setText(R.string.header_bella_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_bella));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_bella));
            }
            break;

            case "Cussons": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.cussons_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_pzcussons);
                imageView_header.setImageResource(R.mipmap.logo_pzcussons);
                textView_header1.setText(R.string.header_cussons_title_1);
                textView_header2.setText(R.string.header_cussons_title_2);
                // для API 21>
                // linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_cussons));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_cussons));
            }
            break;

            case "Pcc": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.pcc_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_flo);
                imageView_header.setImageResource(R.mipmap.logo_flo);
                textView_header1.setText(R.string.header_pcc_title_1);
                textView_header2.setText(R.string.header_pcc_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_pcc));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_pcc));
            }
            break;
            case "Plushe": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.plushe_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.ic_plushe);
                imageView_header.setImageResource(R.mipmap.ic_plushe);
                textView_header1.setText(R.string.header_plushe_1);
                textView_header2.setText(R.string.header_plushe_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_plushe));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_plushe));
            }
            break;

            case "Samarela": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.samarela_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_samarela);
                imageView_header.setImageResource(R.mipmap.logo_samarela);
                textView_header1.setText(R.string.header_samarela_title_1);
                textView_header2.setText(R.string.header_samarela_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_samarela));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_samarela));
            }
            break;
            case "Sofidel": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.sofidel_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_sofidel);
                imageView_header.setImageResource(R.mipmap.logo_sofidel);
                textView_header1.setText(R.string.header_sofidel_title_1);
                textView_header2.setText(R.string.header_sofidel_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_sofidel));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_sofidel));
            }
            break;

            case "Splat": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.splat_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_splat);
                imageView_header.setImageResource(R.drawable.logo_splat);
                textView_header1.setText(R.string.header_splat_title_1);
                textView_header2.setText(R.string.header_splat_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_splat));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_splat));
            }
            break;
            case "Драмерс": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.dramers_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_brait);
                imageView_header.setImageResource(R.mipmap.logo_brait);
                textView_header1.setText(R.string.header_dramers_title_1);
                textView_header2.setText(R.string.header_dramers_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_dramers));
            }
            break;
            case "Первое решение": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.agafia_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_agafia);
                imageView_header.setImageResource(R.mipmap.logo_agafia);
                textView_header1.setText(R.string.header_one_work);
                textView_header2.setText(R.string.header_one_work_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_agadia));
            }
            break;
            case "Пкф сонца": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.dramers_colorAccent));
                getSupportActionBar().setIcon(R.mipmap.logo_soncy);
                imageView_header.setImageResource(R.mipmap.logo_soncy);
                textView_header1.setText(R.string.header_pkf_title_1);
                textView_header2.setText(R.string.header_pkf_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_soncy));
            }
            break;
            case "Свобода": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.rens_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_svoboda);
                imageView_header.setImageResource(R.mipmap.logo_svoboda);
                textView_header1.setText(R.string.header_svoboda_title_1);
                textView_header2.setText(R.string.header_svoboda_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_svoboda));
            }
            break;
            case "Lacalut": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.pkf_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_lacalute);
                imageView_header.setImageResource(R.mipmap.logo_lacalute);
                textView_header1.setText(R.string.header_lacalute_title_1);
                textView_header2.setText(R.string.header_lacalute_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_lacalute));
            }
            break;
            ///sdfdsfsdfsdfsdfsdf/


            case "Zonin": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.splat_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.ic_zonin);
                imageView_header.setImageResource(R.drawable.logo_zonin);
                textView_header1.setText(R.string.header_zonin_title_1);
                textView_header2.setText(R.string.header_zonin_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_splat));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_splat));
            }
            break;

            case "Villa krim": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.splat_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.ic_villa_krym);
                imageView_header.setImageResource(R.drawable.logo_villa_krim);
                textView_header1.setText(R.string.header_villa_krim_title_1);
                textView_header2.setText(R.string.header_villa_krim_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_splat));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_splat));
            }
            break;

            case "Кубанские продукты": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.splat_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.ic_stoev);
                imageView_header.setImageResource(R.drawable.logo_stoev);
                textView_header1.setText(R.string.header_kyban_1);
                textView_header2.setText(R.string.header_kyban_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_splat));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_splat));
            }
            break;

            case "Тд морское содружество": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.splat_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.ic_menu_logo_search);
                imageView_header.setImageResource(R.drawable.logo_splat);
                textView_header1.setText(R.string.header_sea_title_1);
                textView_header2.setText(R.string.header_sea_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_splat));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_splat));
            }
            break;
            case "Sarantis": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.dramers_colorAccent));
                getSupportActionBar().setIcon(R.mipmap.logo_sarantis);
                imageView_header.setImageResource(R.drawable.logo_sarantis);
                textView_header1.setText(R.string.header_sarantis_title_1);
                textView_header2.setText(R.string.header_sarantis_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_splat));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_dramers));
            }
            break;
            default:
                Toast.makeText(context_Activity, "Графическая оболочка еще не готова!", Toast.LENGTH_SHORT).show();
                break;
        }

    }


    // 11.09.2022
    protected void Navigation_Menu_SubBrends(String w_select_name, String w_select_kod, int w_kol) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        switch (w_kol) {
            case 2: {
                Log.e("Структура 2 уровня: ", "уровень: " + w_kol + "|" + w_select_name + "|" + w_select_kod);
                query = "SELECT base_in_brends_sub_id.kod, base_in_brends_sub_id.parent_kod, base_in_brends_sub_id.subbrends FROM base_in_brends_id\n" +
                        "LEFT JOIN base_in_brends_sub_id ON base_in_brends_id.kod = base_in_brends_sub_id.parent_kod\n" +
                        "LEFT JOIN base_in_brends_sub_id_2 ON base_in_brends_sub_id.kod = base_in_brends_sub_id_2.parent_kod\n" +
                        "WHERE base_in_brends_sub_id.parent_kod = '" + w_select_kod + "'\n" +
                        "GROUP BY base_in_brends_sub_id.subbrends;";
                cursor = db.rawQuery(query, null);
                cursor.moveToFirst();
                topMenu_mass_name = new String[cursor.getCount()];
                topMenu_mass_kod = new String[cursor.getCount()];
                Menu m = navigationView.getMenu();
                m.clear();
                topChannelMenu = m.addSubMenu(w_select_name);
                w_kodUNIV = w_select_kod + "/";
            }
            break;
            case 3: {
                Log.e("Структура 3 уровня: ", "уровень: " + w_kol + "|" + w_select_name + "|" + w_select_kod);
                query = "SELECT base_in_brends_sub_id_2.kod, base_in_brends_sub_id_2.parent_kod, base_in_brends_sub_id_2.subbrends FROM base_in_brends_id\n" +
                        "LEFT JOIN base_in_brends_sub_id ON base_in_brends_id.kod = base_in_brends_sub_id.parent_kod\n" +
                        "LEFT JOIN base_in_brends_sub_id_2 ON base_in_brends_sub_id.kod = base_in_brends_sub_id_2.parent_kod\n" +
                        "WHERE base_in_brends_sub_id_2.parent_kod = '" + w_select_kod + "'\n" +
                        "GROUP BY base_in_brends_sub_id_2.subbrends;";
                cursor = db.rawQuery(query, null);
                cursor.moveToFirst();
                topMenu_mass_name = new String[cursor.getCount()];
                topMenu_mass_kod = new String[cursor.getCount()];
                Menu m = navigationView.getMenu();
                m.clear();
                topChannelMenu = m.addSubMenu(w_select_name);
                w_kodUNIV = w_kodUNIV + w_select_kod + "/";
            }
            break;
            default:
                Log.e("Конец структуры ", "уровень: " + w_kol + "|" + w_select_name + "|" + w_select_kod);
                w_kodUNIV = w_kodUNIV + w_select_kod + "/";
                product_str.clear();
                Loading_Db_Nomencalture("BELLA ТАМПОНЫ");
                adapterPriceClients = new ListAdapterAde_Suncape(context_Activity, product_str);
                adapterPriceClients.notifyDataSetChanged();
                listView.setAdapter(adapterPriceClients);

                Log.e("Код ", w_kodUNIV);
                break;
        }
        if (cursor.getCount()>0)
        {
            while (cursor.isAfterLast() == false) {
                switch (w_kol) {
                    case 2: {
                        /*  Name_new = name.substring(0, 1) + name.substring(1).toLowerCase();
                        SName_new = s_name.substring(0, 1) + s_name.substring(1).toLowerCase();
                        topMenu_mass_name[cursor.getPosition()] = SName_new;
                        topMenu_mass_kod[cursor.getPosition()] = kod;*/

                        String name = cursor.getString(cursor.getColumnIndex("subbrends"));          // имя подгруппы
                        String kod = cursor.getString(cursor.getColumnIndex("kod"));                 // код подгруппы
                        String parent_kod = cursor.getString(cursor.getColumnIndex("parent_kod"));   // код родителя
                        topChannelMenu.add(name).setIcon(R.drawable.ic_cost);
                        topMenu_mass_name[cursor.getPosition()] = name;
                        topMenu_mass_kod[cursor.getPosition()] = kod;
                        Log.e("menu: ", "name->" + name + " kod->" + kod);
                    }
                    break;
                    case 3: {
                        String name = cursor.getString(cursor.getColumnIndex("subbrends"));          // имя подгруппы
                        String kod = cursor.getString(cursor.getColumnIndex("kod"));                 // код подгруппы
                        String parent_kod = cursor.getString(cursor.getColumnIndex("parent_kod"));   // код родителя

                        topChannelMenu.add(name).setIcon(R.drawable.ic_cost);
                        topMenu_mass_name[cursor.getPosition()] = name;
                        topMenu_mass_kod[cursor.getPosition()] = kod;
                        Log.e("menu2: ", "name->" + name + " kod->" + kod);
                    }
                    break;
                    default: {
                        Log.e("DEFAULT: ", "Конец");
                    }
                    break;
                }


                cursor.moveToNext();
            }
        }
        else
        {
            Log.e("Конец структуры ", "уровень: " + w_kol + "|" + w_select_name + "|" + w_select_kod);
            w_kodUNIV = w_kodUNIV + w_select_kod + "/";
            product_str.clear();
            Loading_Db_Nomencalture("BELLA ТАМПОНЫ");
            adapterPriceClients = new ListAdapterAde_Suncape(context_Activity, product_str);
            adapterPriceClients.notifyDataSetChanged();
            listView.setAdapter(adapterPriceClients);

            Log.e("Код ", w_kodUNIV);
        }

        cursor.close();
        db.close();

    }

    // Загрузка номенклатуры по брендам и по группам(Сортировка)
    protected void Loading_Db_Nomencalture(String subBrends) {
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        perem_switch_group_sql = sp.getBoolean("switch_preference_group_sql", FALSE);            //
        perem_switch_full_tovar = sp.getBoolean("switch_preference_full_tovar", FALSE);          //
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
            String query;
            //  "WHERE count > 0\n" +   w_kodUNIV
            if (perem_switch_group_sql) {
                query = "SELECT base_in_nomeclature.name, base_in_nomeclature.brends, " +
                        "base_in_nomeclature.p_group, base_in_nomeclature.kod, base_in_image.kod_image, " +
                        "base_in_nomeclature.kolbox, SUM(base_in_ostatok.count) AS 'count', " +
                        "base_in_price.price, base_in_nomeclature.strih, base_in_nomeclature.kod_univ, base_in_nomeclature.koduid\n" +
                        "FROM base_in_nomeclature\n" +
                        "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                        "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                        "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                        "LEFT JOIN base_group_sql ON base_in_nomeclature.koduid = base_group_sql.uid_name\n" +
                        "WHERE base_in_nomeclature.kod_univ LIKE '%" + w_kodUNIV + "%'\n" +
                        "GROUP BY base_in_nomeclature.name\n" +
                        "ORDER BY base_in_nomeclature.brends, base_in_nomeclature.p_group, base_group_sql.type_group ASC;";
            } else {
                query = "SELECT base_in_nomeclature.name, base_in_nomeclature.brends, " +
                        "base_in_nomeclature.p_group, base_in_nomeclature.kod, base_in_image.kod_image, " +
                        "base_in_nomeclature.kolbox, SUM(base_in_ostatok.count) AS 'count', " +
                        "base_in_price.price, base_in_nomeclature.strih, base_in_nomeclature.kod_univ, base_in_nomeclature.koduid\n" +
                        "FROM base_in_nomeclature\n" +
                        "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                        "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                        "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                        "WHERE base_in_nomeclature.kod_univ LIKE '%" + w_kodUNIV + "%'\n" +
                        "GROUP BY base_in_nomeclature.name\n" +
                        "ORDER BY base_in_nomeclature.brends, base_in_nomeclature.p_group;";
            }

            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String brend = cursor.getString(cursor.getColumnIndexOrThrow("brends"));
                String p_group = cursor.getString(cursor.getColumnIndexOrThrow("p_group"));
                String kod = cursor.getString(cursor.getColumnIndexOrThrow("kod"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String kolbox = cursor.getString(cursor.getColumnIndexOrThrow("kolbox"));
                String count = cursor.getString(cursor.getColumnIndexOrThrow("count"));
                cena = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("kod_image"));
                String strih = cursor.getString(cursor.getColumnIndexOrThrow("strih"));
                String kod_univ = cursor.getString(cursor.getColumnIndexOrThrow("kod_univ"));
                String koduid = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));

                /*String brends = brend.substring(0, 1) + brend.substring(1).toLowerCase();
                String sub_brends = p_group.substring(0, 1) + p_group.substring(1).toLowerCase();*/

                String brends = brend.replaceAll(" ", "").toLowerCase();
                String sub_brends = p_group.replaceAll(" ", "").toLowerCase();
                PEREM_SELECT_BRENDS = PEREM_SELECT_BRENDS.replaceAll(" ", "").toLowerCase();

                Log.e("LISTVIEW ", "Loading_Db_Nomencalture1: " + brends);
                Log.e("LISTVIEW ", "Loading_Db_Nomencalture2: " + PEREM_SELECT_BRENDS);
                Log.e("LISTVIEW ", "Loading_Db_Nomencalture3: " + sub_brends);
                Log.e("LISTVIEW ", "Loading_Db_Nomencalture4: " + cursor.getCount());
                Log.e("LENGHT ", "KOL1: " + kod_univ.length());
                Log.e("LENGHT ", "KOL2: " + w_kodUNIV.length());
                if (kod_univ.length() <= w_kodUNIV.length() + 8) {
                    product_str.add(new ListAdapterSimple_Suncape(kod_univ, name, kolbox, cena, Cena_Nal, strih, ostatok, "no_image"));
                    cursor.moveToNext();
                } else cursor.moveToNext();


            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка номенклатуры: загрузка данных!", Toast.LENGTH_SHORT).show();
            Log.e("Error...", "Ошибка номенклатуры: загрузка данных!");
        }

    }

    // Торговые условия (цены)
    protected void Params() {
        try {
            sp = PreferenceManager.getDefaultSharedPreferences(this);
            String listValue_all = sp.getString("list_all_nal", "0");
            String listValue2_all = sp.getString("list_all_kons", "0");

            if (listValue_all.equals("0")) {
                TY = Double.parseDouble(listValue_all);

            } else {
                TY = Double.parseDouble(listValue_all) / 100;
            }

            if (listValue2_all.equals("0")) {
                TY_Kons = Double.parseDouble(listValue2_all);

            } else {
                TY_Kons = Double.parseDouble(listValue2_all) / 100;
            }
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка ценообразования!", Toast.LENGTH_SHORT).show();
            Log.e("Error...", "Ошибка ценообразования!");
        }


    }

    protected void Cena_for_DB() {
        try {
            if (cena.equals(null)) {
                Cena_For = "0";
            } else {

                Cena_For = cena;
            }
        } catch (Exception e) {
            //   Log.e("Цена", "не верная цена");
        }

        Doub_Cena = Double.parseDouble(Cena_For);
        String Format1 = new DecimalFormat("#00.00").format(Doub_Cena);
        Cena_All = Format1;

        if (TY > 0.0) {
            Doub_Nal = Double.parseDouble(Cena_For) - (Double.parseDouble(Cena_For) * TY);
            String Format2 = new DecimalFormat("#00.00").format(Doub_Nal);
            Cena_Nal = Format2;
        } else {
            Doub_Nal = Double.parseDouble(Cena_For);
            String Format2 = new DecimalFormat("#00.00").format(Doub_Nal);
            Cena_Nal = Format2;
        }

        if (TY_Kons > 0.0) {
            Doub_Kons = Double.parseDouble(Cena_For) - (Double.parseDouble(Cena_For) * TY_Kons);
            String Format3 = new DecimalFormat("#00.00").format(Doub_Kons);
            Cena_Kons = Format3;
        } else {
            Doub_Kons = Double.parseDouble(Cena_For);
            String Format3 = new DecimalFormat("#00.00").format(Doub_Kons);
            Cena_Kons = Format3;
        }
    }


}
