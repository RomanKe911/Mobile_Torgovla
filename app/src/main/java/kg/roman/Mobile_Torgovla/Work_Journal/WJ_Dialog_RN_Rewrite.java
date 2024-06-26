package kg.roman.Mobile_Torgovla.Work_Journal;

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
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Suncape_Forma;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Suncape_Forma;
import kg.roman.Mobile_Torgovla.Permission.PrefActivity;
import kg.roman.Mobile_Torgovla.R;


public class WJ_Dialog_RN_Rewrite extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ArrayList<ListAdapterSimple_Suncape_Forma> product_str = new ArrayList<ListAdapterSimple_Suncape_Forma>();
    public ListAdapterAde_Suncape_Forma adapterPriceClients;

    public NavigationView navigationView;
    public Menu menu;
    public SubMenu topChannelMenu;
    public Toolbar toolbar;
    public androidx.appcompat.app.AlertDialog.Builder dialog;
    public View navHeader;
    public ListView listView;
    public ImageView imageView_header;
    public TextView textView_header1, textView_header2;
    public TextView dg_tw_name, dg_tw_koduid, dg_tw_koduniv, dg_tw_cena, dg_tw_ostatok, dg_tw_kol, dg_tw_summa, dg_tw_summa_sk, dg_tw_kolbox, dg_tw_kolbox_org;
    public EditText dg_ed_skidka, dg_ed_editkol;
    public LinearLayout linearLayout_header;
    public Button btn_down, btn_up, button_ok, button_Go, button_cancel;
    public View localView;

    public RadioGroup radioGroup_local;
    public RadioButton radioGroup_one, radioGroup_much, radioGroup_edit;

    public Calendar localCalendar = Calendar.getInstance();
    public Context context_Activity;

    public Integer checked_group, kol_box_info, max_box;
    public Integer thisdata, thismonth, thisyear, thisminyte, thishour, thissecond;
    public Integer perem_int_summa, perem_int_ostatok, perem_int_cena, perem_int_kol, perem_kol_group_one, perem_int_kolbox;
    public Double dg_tw_summa_sk_DOUBLE;
    public Double TY, TY_Kons, Doub_Cena, Doub_Kons, Doub_Nal;
    public String Name_new, SName_new, query_nomecl;
    public String table_name, name_group, id_st, name, kod, select_image;
    public String Cena_All, Cena_Nal, Cena_Kons, cena, PEREM_SELECT_BRENDS;
    public String thisdata_st, thismonth_st, thisminyte_st, thishour_st, thissecond_st;
    public String lst_tw_name, lst_tw_kod, lst_tw_cena, lst_tw_ostatok, lst_tw_kolbox, lst_tw_koduid;
    public String kol_group_one, kol_group_much, PEREM_READ_KODRN;

    public Boolean pref_params_1, pref_params_2;
    public SharedPreferences sp;
    public SharedPreferences.Editor ed;
    public String perem_rewrite_rn, peremen_query, Log_Text_Error;

    // public String PEREM_SELECT_BRENDS;
    public String PEREM_K_AG_NAME, PEREM_K_AG_UID, PEREM_K_AG_ADRESS, PEREM_K_AG_KodRN, PEREM_K_AG_Data, PEREM_K_AG_Vrema, PEREM_K_AG_GPS;
    public String PEREM_FTP_SERV, PEREM_FTP_LOGIN, PEREM_FTP_PASS, PEREM_FTP_DISTR_XML, PEREM_FTP_DISTR_db3,
            PEREM_IMAGE_PUT_SDCARD, PEREM_IMAGE_PUT_PHONE;
    public String PEREM_MAIL_LOGIN, PEREM_MAIL_PASS, PEREM_MAIL_START, PEREM_MAIL_END,
            PEREM_DB3_CONST, PEREM_DB3_BASE, PEREM_DB3_RN, PEREM_ANDROID_ID_ADMIN, PEREM_ANDROID_ID;
    public String PEREM_AG_UID, PEREM_AG_NAME, PEREM_AG_REGION, PEREM_AG_UID_REGION, PEREM_AG_CENA,
            PEREM_AG_SKLAD, PEREM_AG_UID_SKLAD, PEREM_AG_TYPE_REAL, PEREM_AG_TYPE_USER,
            PEREM_WORK_DISTR, PEREM_KOD_MOBILE, PEREM_KOD_UID_KODRN;
    public String PEREM_KLIENT_UID, PEREM_DIALOG_UID, PEREM_DIALOG_DATA_START, PEREM_DIALOG_DATA_END, PEREM_DISPLAY_START, PEREM_DISPLAY_END;
    public String postclass_RN, postclass_OLD_SUMMA, postclass_old_kol, postclass_clients_uid, postclass_Summa, postclass_Summa_Debet, postclass_Skidka;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Bella); // (for Custom theme)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt_spr_nomenclatura);

        context_Activity = WJ_Dialog_RN_Rewrite.this;
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        Constanta_Read();
        pref_params_1 = sp.getBoolean("switch_preference_1", false); //только товар, в наличии
        pref_params_2 = sp.getBoolean("switch_preference_2", false); //бесконечные остатки
        PEREM_SELECT_BRENDS = sp.getString("sp_BREND", "0");

        Bundle arguments = getIntent().getExtras();
        postclass_RN = arguments.get("ReWrite_RN").toString();
        postclass_Summa = arguments.get("ReWrite_Summa").toString();
        postclass_Summa_Debet = arguments.get("ReWrite_Summa_Debet").toString();
        postclass_Skidka = arguments.get("ReWrite_Skidka").toString();
        postclass_clients_uid = arguments.get("ReWrite_Client_UID").toString();
        postclass_OLD_SUMMA = postclass_Summa;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.ListView_Klients);
       /* getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.user_pda);*/
        getSupportActionBar().setTitle(PEREM_SELECT_BRENDS);
        //getSupportActionBar().setSubtitle(sp_BREND);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_nomenclature);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view_nomenclature);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.menu_nomeclature_brends);
        navigationView.setNavigationItemSelectedListener(this);

        Navigation_Menu();
        Navigation_Title_Image();
        id_st = topChannelMenu.getItem(0).toString();

        //Navigation_Group_Name();
        Adapter_Price();

        Toast.makeText(context_Activity, "Группа= " + id_st, Toast.LENGTH_SHORT).show();
        Log.e("Номер накл=", PEREM_READ_KODRN + ".");
        product_str.clear();
        Loading_Db_Nomencalture();
        adapterPriceClients = new ListAdapterAde_Suncape_Forma(context_Activity, product_str);
        adapterPriceClients.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients);
    }

    @Override
    public void onBackPressed() {
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent3 = new Intent(context_Activity, PrefActivity.class);
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
        id_st = item.getTitle().toString();

        getSupportActionBar().setTitle(PEREM_SELECT_BRENDS);
        getSupportActionBar().setSubtitle(id_st);
        table_name = "table_" + PEREM_SELECT_BRENDS;
        try {
            //Navigation_Group_Name();
            Navigation_Menu();
            product_str.clear();
            Loading_Db_Nomencalture();
            adapterPriceClients = new ListAdapterAde_Suncape_Forma(context_Activity, product_str);
            adapterPriceClients.notifyDataSetChanged();
            listView.setAdapter(adapterPriceClients);
          //  Toast.makeText(context_Activity, "Данные:" + id + ", " + id_st + ", " + table_name + ", " + name_group, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Нет данных", Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_nomenclature);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void Navigation_Menu() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT base_in_brends_id.brends, base_in_brends_id.kod, " +
                "base_in_brends_sub_id.subbrends, base_in_brends_sub_id.kod, " +
                "base_in_brends_sub_id.parent_kod\n" +
                "FROM base_in_brends_id\n" +
                "LEFT JOIN base_in_brends_sub_id ON base_in_brends_id.kod = base_in_brends_sub_id.parent_kod";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Menu m = navigationView.getMenu();
        m.clear();
        topChannelMenu = m.addSubMenu("Группы");
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndex("brends")); // код клиента
            String s_name = cursor.getString(cursor.getColumnIndex("subbrends")); // код клиента
            Name_new = name.substring(0, 1) + name.substring(1).toLowerCase();
            SName_new = s_name.substring(0, 1) + s_name.substring(1).toLowerCase();
            //Log.e("Name", Name_new + ", " + SName_new);
            if ((Name_new).equals(PEREM_SELECT_BRENDS)) {
                topChannelMenu.add(SName_new).setIcon(R.drawable.ic_cost);
            }
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

    }  // Программное создание меню

    protected void Navigation_Title_Image() {
        navHeader = navigationView.getHeaderView(0);
        imageView_header = (ImageView) navHeader.findViewById(R.id.nav_header_nomenclature);
        textView_header1 = (TextView) navHeader.findViewById(R.id.textView_title_1);
        textView_header2 = (TextView) navHeader.findViewById(R.id.textView_title_2);
        linearLayout_header = (LinearLayout) navHeader.findViewById(R.id.lineary_header);
        Log.e("Brends=", PEREM_SELECT_BRENDS);

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
            default:
                Toast.makeText(context_Activity, "Временно не работает!!!", Toast.LENGTH_SHORT).show();
                break;
        }

    }  // Загрузка визуального оформления для меню по брендам

    protected void Navigation_Group_Name() {

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("logins.db3", MODE_PRIVATE, null);
        String query = "SELECT table_all_brends._id, table_all_brends.brends, " +
                "table_all_brends.name_new, table_all_brends.name_db, table_all_brends.kod_uid\n" +
                "FROM table_all_brends";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String brends = cursor.getString(cursor.getColumnIndex("brends")); // код клиента
            String name_new = cursor.getString(cursor.getColumnIndex("name_new"));
            String name_db = cursor.getString(cursor.getColumnIndex("name_db"));
            String koduid = cursor.getString(cursor.getColumnIndex("kod_uid"));
            if (id_st.equals(name_new) & PEREM_SELECT_BRENDS.equals(brends))
                name_group = name_db;
            cursor.moveToNext();


       /*SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_db.db3", MODE_PRIVATE, null);
        String query = "SELECT " + DbContract_Table_Brends.TableUser._ID + ", "
                + DbContract_Table_Brends.TableUser.COLUMN_KODUID + ", "
                + DbContract_Table_Brends.TableUser.COLUMN_NAME_DB + ", "
                + DbContract_Table_Brends.TableUser.COLUMN_NAME_NEW + ", "
                + DbContract_Table_Brends.TableUser.COLUMN_BRENDS + " FROM table_all_brends";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String id = cursor.getString(cursor.getColumnIndex(DbContract_Table_Brends.TableUser._ID));
            String brends = cursor.getString(cursor.getColumnIndex(DbContract_Table_Brends.TableUser.COLUMN_BRENDS)); // код клиента
            String name_new = cursor.getString(cursor.getColumnIndex(DbContract_Table_Brends.TableUser.COLUMN_NAME_NEW));
            String name_db = cursor.getString(cursor.getColumnIndex(DbContract_Table_Brends.TableUser.COLUMN_NAME_DB));
            String koduid = cursor.getString(cursor.getColumnIndex(DbContract_Table_Brends.TableUser.COLUMN_KODUID));
            if (id_st.equals(name_new) & sp_BREND.equals(brends))
                name_group = name_db;
            cursor.moveToNext();*/

        }
        cursor.close();
        db.close();

    }   // Загрузка подгрупп меню

    protected void Loading_Db_Nomencalture() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT base_in_nomeclature.name, base_in_nomeclature.brends, " +
                "base_in_nomeclature.p_group, base_in_nomeclature.kod, base_in_image.kod_image, " +
                "base_in_nomeclature.kolbox, base_in_ostatok.count, base_in_ostatok.sklad_uid, const_sklad.sklad_name," +
                "base_in_price.price, base_in_nomeclature.strih, base_in_nomeclature.kod_univ, base_in_nomeclature.koduid\n" +
                "FROM base_in_nomeclature\n" +
                "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                "LEFT JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid\n" +
                "WHERE count > 0\n" +
                "GROUP BY base_in_nomeclature.name\n" +
                "ORDER BY base_in_nomeclature.brends, base_in_nomeclature.p_group;";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String brend = cursor.getString(cursor.getColumnIndex("brends"));
            String p_group = cursor.getString(cursor.getColumnIndex("p_group"));
            String kod = cursor.getString(cursor.getColumnIndex("kod"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String kolbox = cursor.getString(cursor.getColumnIndex("kolbox"));
            String count = cursor.getString(cursor.getColumnIndex("count"));
            cena = cursor.getString(cursor.getColumnIndex("price"));
            String image = cursor.getString(cursor.getColumnIndex("kod_image"));
            String strih = cursor.getString(cursor.getColumnIndex("strih"));
            String kod_univ = cursor.getString(cursor.getColumnIndex("kod_univ"));
            String koduid = cursor.getString(cursor.getColumnIndex("koduid"));
            String sklad_uid = cursor.getString(cursor.getColumnIndex("sklad_uid"));
            String sklad_name = cursor.getString(cursor.getColumnIndex("sklad_name"));
            String brends = brend.substring(0, 1) + brend.substring(1).toLowerCase();
            String sub_brends = p_group.substring(0, 1) + p_group.substring(1).toLowerCase();
            String ostatok;
            if (count != (null)) {
                ostatok = count + "шт";
            } else ostatok = "закончился";

            Params();
            Cena_for_DB();
            if (brends.equals(PEREM_SELECT_BRENDS)) {
                if (id_st.equals(sub_brends)) {
                    if (image != null) {
                        // Log.e("Sub=", p_group + ", " + sub_brends);
                        product_str.add(new ListAdapterSimple_Suncape_Forma(koduid, kod_univ, name, kolbox, cena, Cena_Nal, strih, ostatok, image, sklad_name, sklad_uid));
                        cursor.moveToNext();
                    } else {
                        // Log.e("Sub=", p_group + ", " + sub_brends);
                        product_str.add(new ListAdapterSimple_Suncape_Forma(koduid, kod_univ, name, kolbox, cena, Cena_Nal, strih, ostatok, "no_image", sklad_name, sklad_uid));
                        cursor.moveToNext();
                    }
                } else cursor.moveToNext();
            } else cursor.moveToNext();
        }
        cursor.close();
        db.close();


    }  // Загрузка номенклатуры по брендам и по группам

    protected void Params() {
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

    }  // Загрузка тогровых условий

    protected void Cena_for_DB() {
        Doub_Cena = Double.parseDouble(cena);
        // Log.e("Mat_Cena: ", Doub_Cena.toString());
        String Format1 = new DecimalFormat("#00.00").format(Doub_Cena).replace(",", ".");
        Cena_All = Format1;
        //Log.e("Mat_Cena_All: ", Format1);

        if (TY > 0.0) {
            Doub_Nal = Double.parseDouble(cena) - (Double.parseDouble(cena) * TY);
            String Format2 = new DecimalFormat("#00.00").format(Doub_Nal);
            Cena_Nal = Format2;
        } else {
            Doub_Nal = Double.parseDouble(cena);
            String Format2 = new DecimalFormat("#00.00").format(Doub_Nal);
            Cena_Nal = Format2;
        }

        if (TY_Kons > 0.0) {
            Doub_Kons = Double.parseDouble(cena) - (Double.parseDouble(cena) * TY_Kons);
            String Format3 = new DecimalFormat("#00.00").format(Doub_Kons);
            Cena_Kons = Format3;
        } else {
            Doub_Kons = Double.parseDouble(cena);
            String Format3 = new DecimalFormat("#00.00").format(Doub_Kons);
            Cena_Kons = Format3;
        }
        try {

        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка ценообразования", Toast.LENGTH_SHORT).show();
            Log.e(Log_Text_Error, "Ошибка ценообразования");
        }

    }  // Вычисление цен(своя, скидка за нал и конс)

    protected void Adapter_Price() {
        adapterPriceClients = new ListAdapterAde_Suncape_Forma(context_Activity, product_str);
        adapterPriceClients.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Заполнение из карточик товара
                try {
                    lst_tw_name = ((TextView) view.findViewById(R.id.textView_forma_name)).getText().toString();
                    lst_tw_kod = ((TextView) view.findViewById(R.id.textView_forma_kod)).getText().toString();
                    lst_tw_koduid = ((TextView) view.findViewById(R.id.text_uid_new)).getText().toString();
                    if (pref_params_2 == false) {
                        lst_tw_kolbox = ((TextView) view.findViewById(R.id.textView_forma_kolbox)).getText().toString();
                        lst_tw_cena = ((TextView) view.findViewById(R.id.textView_forma_cena)).getText().toString();
                        lst_tw_ostatok = ((TextView) view.findViewById(R.id.textView_forma_ostatok)).getText().toString();
                    } else {
                        lst_tw_kolbox = ((TextView) view.findViewById(R.id.textView_forma_kolbox)).getText().toString();
                        lst_tw_cena = ((TextView) view.findViewById(R.id.textView_forma_cena)).getText().toString();
                        lst_tw_ostatok = "999999";
                    }


                } catch (Exception e) {
                    Log.e("WJ_FormaLS2:", "Ошибка заполнения карточки!");
                    Toast.makeText(context_Activity, "Ошибка заполнения карточки!", Toast.LENGTH_SHORT).show();
                }

                // Определение данных в диалог
                localView = LayoutInflater.from(context_Activity).inflate(R.layout.mt_dialog_localview_add, null);
                dg_tw_name = (TextView) localView.findViewById(R.id.tvw_text_aks_name);
                dg_tw_koduid = (TextView) localView.findViewById(R.id.text_d_koduid);
                dg_tw_koduniv = (TextView) localView.findViewById(R.id.tvw_text_aks_koduid);
                dg_tw_cena = (TextView) localView.findViewById(R.id.tvw_text_aks_cena);
                dg_tw_ostatok = (TextView) localView.findViewById(R.id.tvw_text_aks_ostatok);
                dg_tw_kol = (TextView) localView.findViewById(R.id.tvw_text_aks_kol);
                dg_tw_kolbox = (TextView) localView.findViewById(R.id.tvw_kol_box);
                dg_tw_summa = (TextView) localView.findViewById(R.id.tvw_text_aks_summa);
                dg_tw_summa_sk = (TextView) localView.findViewById(R.id.tvw_Ssumma);
                dg_tw_kolbox_org = (TextView) localView.findViewById(R.id.tvw_kol_box_org);
                dg_ed_skidka = (EditText) localView.findViewById(R.id.ed_text_skidka);
                dg_ed_editkol = (EditText) localView.findViewById(R.id.ed_text_kol);
                btn_up = (Button) localView.findViewById(R.id.btn_add_up);
                btn_down = (Button) localView.findViewById(R.id.btn_add_down);
                radioGroup_local = (RadioGroup) localView.findViewById(R.id.radioGroup_local);
                radioGroup_one = (RadioButton) localView.findViewById(R.id.radioButton_one);
                radioGroup_much = (RadioButton) localView.findViewById(R.id.radioButton_much);
                radioGroup_edit = (RadioButton) localView.findViewById(R.id.radioButton_edit);

                // Заполнение данных в диалог
                dg_ed_editkol.requestFocus();
                dg_ed_editkol.setFocusable(true);
                dg_ed_editkol.setVisibility(View.GONE);
                dg_tw_kol.setVisibility(View.VISIBLE);
                dg_tw_kolbox.setVisibility(View.GONE);
                dg_tw_summa_sk.setVisibility(View.GONE);
                checked_group = null;
                checked_group = R.id.radioButton_one;
                kol_group_one = "1";
                perem_kol_group_one = 1;

                dg_tw_name.setText(lst_tw_name);
                dg_tw_koduniv.setText(lst_tw_kod);
                dg_tw_koduid.setText(lst_tw_koduid);
                perem_int_cena = Integer.parseInt(lst_tw_cena);
                dg_tw_cena.setText(perem_int_cena.toString());
                perem_int_ostatok = Integer.parseInt(lst_tw_ostatok.substring(0, lst_tw_ostatok.length() - 2));
                dg_tw_ostatok.setText(perem_int_ostatok.toString());
                perem_int_kol = perem_kol_group_one;
                dg_tw_kol.setText(perem_int_kol.toString());
                if (!lst_tw_kolbox.isEmpty() | lst_tw_kolbox.equals(" ")) {
                    perem_int_kolbox = Integer.parseInt(lst_tw_kolbox);
                } else perem_int_kolbox = 12;

                dg_tw_kolbox_org.setText(perem_int_kolbox.toString());
                perem_int_summa = perem_int_cena * perem_kol_group_one;
                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                ;
                dg_tw_summa.setText(Format);

                if (perem_int_kol > 0) {

                    Button_Up(perem_int_cena, perem_int_ostatok, perem_int_kolbox);
                    Button_Down(perem_int_cena, perem_int_ostatok, perem_int_kolbox);
                    Button_Edit(perem_int_cena, perem_int_ostatok);
                    Button_Edit_Skidka();
                    Fun_Messeger_Panel(perem_int_cena, perem_int_ostatok, perem_int_kolbox);

                    Log.e("WJ_FormaLSINT:", perem_int_kol + ", " + perem_int_cena + ", " + perem_int_ostatok + ", " + Format);
                } else
                    Toast.makeText(context_Activity, "не достаточно товара", Toast.LENGTH_SHORT).show();
            }
        });


    }

    protected void Fun_Messeger_Panel(final Integer cena, final Integer ostatok, final Integer box) {
        Button_Up(perem_int_cena, perem_int_ostatok, perem_int_kolbox);
        Button_Down(perem_int_cena, perem_int_ostatok, perem_int_kolbox);
        Button_Edit(perem_int_cena, perem_int_ostatok);
        radioGroup_local.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton_one: {
                        dg_tw_kol.setVisibility(View.VISIBLE);
                        dg_ed_editkol.setVisibility(View.GONE);
                        dg_tw_kolbox.setVisibility(View.GONE);
                        btn_up.setVisibility(View.VISIBLE);
                        btn_down.setVisibility(View.VISIBLE);
                        checked_group = null;
                        checked_group = R.id.radioButton_one;
                        perem_kol_group_one = 1;
                        dg_tw_kol.setText(perem_kol_group_one.toString());
                        perem_int_summa = perem_kol_group_one * cena;
                        String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                        ;
                        dg_tw_summa.setText(Format);
                        dg_tw_summa_sk.setVisibility(View.GONE);
                        dg_ed_skidka.setText("");
                        Summa_Skidka();
                        hideInputMethod();
                    }
                    break;
                    case R.id.radioButton_much: {
                        dg_tw_kol.setVisibility(View.VISIBLE);
                        dg_ed_editkol.setVisibility(View.GONE);
                        dg_tw_kolbox.setVisibility(View.VISIBLE);
                        btn_up.setVisibility(View.VISIBLE);
                        btn_down.setVisibility(View.VISIBLE);
                        kol_box_info = 1;
                        dg_tw_kolbox.setText("x" + kol_box_info);
                        checked_group = null;
                        checked_group = R.id.radioButton_much;
                        // kol_group_much = box.replaceAll("[\\D]", "");
                        //  Double db = ostatok / Double.parseDouble(kol_group_much);
                        kol_group_much = box.toString();
                        Double db = ostatok.doubleValue() / box.doubleValue();
                        max_box = db.intValue();

                        if (max_box > 0) {

                            dg_tw_kol.setText(kol_group_much);
                            Toast.makeText(context_Activity, "кол-во кор=" + max_box, Toast.LENGTH_SHORT).show();
                        } else {
                            dg_tw_kol.setText("0");

                        }
                        perem_int_summa = Integer.parseInt(dg_tw_kol.getText().toString()) * cena;
                        String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                        ;
                        dg_tw_summa.setText(Format);
                        dg_tw_summa_sk.setVisibility(View.GONE);
                        dg_ed_skidka.setText("");
                        Summa_Skidka();
                        hideInputMethod();
                    }
                    break;
                    case R.id.radioButton_edit: {
                        dg_tw_kol.setVisibility(View.GONE);
                        dg_ed_editkol.setVisibility(View.VISIBLE);
                        dg_tw_kolbox.setVisibility(View.GONE);
                        btn_up.setVisibility(View.GONE);
                        btn_down.setVisibility(View.GONE);
                        dg_ed_editkol.clearFocus();
                        dg_ed_editkol.requestFocus();
                        dg_ed_editkol.setFocusable(true);
                        dg_ed_editkol.setText("");
                        dg_ed_editkol.setSelection(dg_ed_editkol.getText().length());
                        dg_tw_kol.setText(kol_group_one);
                        perem_int_summa = Integer.parseInt(dg_tw_kol.getText().toString()) * cena;
                        String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                        ;
                        dg_tw_summa.setText(Format);
                        dg_tw_summa_sk.setVisibility(View.GONE);
                        dg_ed_skidka.setText("");
                        Summa_Skidka();
                        showInputMethod();
                    }
                    break;
                    default:
                        break;

                }
            }
        });


        AlertDialog.Builder localBuilder = new AlertDialog.Builder(context_Activity);

        localBuilder.setView(localView);
        localBuilder.setTitle("Добавление товара");
        localBuilder.setCancelable(false).setIcon(R.drawable.icon_korz).setPositiveButton(" ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Integer kol_text = Integer.parseInt(dg_tw_kol.getText().toString());
                if (kol_text > 0) {
                    Search_Image(dg_tw_koduniv.getText().toString());
                    Write_Table_RN(dg_tw_koduid.getText().toString(), dg_tw_koduniv.getText().toString(), dg_tw_name.getText().toString(),
                            dg_tw_kol.getText().toString(), dg_tw_cena.getText().toString(),
                            dg_tw_summa.getText().toString(), postclass_Skidka, dg_tw_summa_sk_DOUBLE, select_image);
                    //  protected void Write_Table_RN(final String koduniv, final String name, final String kol, final String cena, final String summa) {
                    hideInputMethod();
                    //   Update_Ostatki();**

                } else {
                    Toast.makeText(context_Activity, "Товара не достаточно на складе", Toast.LENGTH_SHORT).show();
                }

                try {

                } catch (Exception e) {
                    Toast.makeText(context_Activity, "Неправильный ввод данных", Toast.LENGTH_SHORT).show();
                }

            }
        }).setNegativeButton(" ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                hideInputMethod();
                paramDialogInterface.cancel();

            }
        });
        AlertDialog localAlertDialog = localBuilder.create();
        localAlertDialog.show();
        button_ok = localAlertDialog.getButton(-1);
        button_ok.setCompoundDrawablesWithIntrinsicBounds(R.drawable.button_ok, 0, 0, 0);
        button_cancel = localAlertDialog.getButton(-2);
        button_cancel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_close, 0);


    }

    protected void Button_Up(final Integer cena, final Integer ostatok, final Integer textbox) {
        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dg_tw_kol.getText().toString().equals("") | dg_tw_kol.getText().toString().equals(" ")) {
                    switch (checked_group) {
                        case R.id.radioButton_one: {
                            dg_tw_kol.setText("1");
                            Integer i = Integer.parseInt(dg_tw_kol.getText().toString());
                            i++;
                            Toast.makeText(context_Activity, "Товара на складе" + ostatok, Toast.LENGTH_SHORT).show();
                            if (i <= ostatok) {
                                dg_tw_kol.setText(i.toString());
                                perem_int_summa = cena * i;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                                ;
                                dg_tw_summa.setText(Format);
                                Summa_Skidka();
                            } else
                                Toast.makeText(context_Activity, "не достаточно товара", Toast.LENGTH_SHORT).show();

                        }
                        break;

                        case R.id.radioButton_much: {
                            /// dg_tw_kol.setText(text_box.replaceAll("[\\D]", ""));
                            dg_tw_kol.setText(textbox.toString());
                            Integer i = Integer.parseInt(dg_tw_kol.getText().toString());
                            //  Integer new_kol = Integer.parseInt(text_box.replaceAll("[\\D]", "")) + i;
                            Integer new_kol = textbox + i;
                            dg_tw_kol.setText(new_kol.toString());
                            perem_int_summa = cena * new_kol;
                            String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                            ;
                            dg_tw_summa.setText(Format);
                            kol_box_info++;
                            dg_tw_kolbox.setText("x" + kol_box_info);
                            Summa_Skidka();

                        }
                        break;
                    }
                } else {
                    switch (checked_group) {
                        case R.id.radioButton_one: {
                            Integer i = Integer.parseInt(dg_tw_kol.getText().toString());
                            i++;
                            if (i <= ostatok) {
                                dg_tw_kol.setText(i.toString());
                                perem_int_summa = cena * i;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                                ;
                                dg_tw_summa.setText(Format);
                                Summa_Skidka();
                            } else
                                Toast.makeText(context_Activity, "не достаточно товара", Toast.LENGTH_SHORT).show();
                        }
                        break;

                        case R.id.radioButton_much: {

                            if (kol_box_info < max_box) {
                                Double db = ostatok / Double.parseDouble(kol_group_much);
                                Integer it = db.intValue();
                                Toast.makeText(context_Activity, "кол-во кор=" + it, Toast.LENGTH_SHORT).show();
                                Integer i = Integer.parseInt(dg_tw_kol.getText().toString());
                                //  Integer new_kol = Integer.parseInt(text_box.replaceAll("[\\D]", "")) + i;
                                Integer new_kol = textbox + i;
                                dg_tw_kol.setText(new_kol.toString());
                                perem_int_summa = cena * new_kol;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                                ;
                                dg_tw_summa.setText(Format);
                                kol_box_info++;
                                dg_tw_kolbox.setText("x" + kol_box_info);
                                Summa_Skidka();
                            } else {
                                Toast.makeText(context_Activity, "макс. кол-во", Toast.LENGTH_SHORT).show();
                            }


                        }
                        break;
                    }

                }


            }
        });

    }  // добавления количества

    protected void Button_Down(final Integer cena, final Integer ostatok, final Integer textbox) {
        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dg_tw_kol.getText().toString().equals("") | dg_tw_kol.getText().toString().equals(" ")) {
                    switch (checked_group) {
                        case R.id.radioButton_one: {
                            dg_tw_kol.setText("1");
                            Integer i = Integer.parseInt(dg_tw_kol.getText().toString());
                            if (i > 1) {
                                i--;
                                dg_tw_kol.setText(i.toString());
                                perem_int_summa = cena * i;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                                ;
                                dg_tw_summa.setText(Format);
                                Summa_Skidka();
                            } else
                                Toast.makeText(context_Activity, "Кол-во меньше 1", Toast.LENGTH_SHORT).show();
                        }
                        break;

                        case R.id.radioButton_much: {
                            //  dg_tw_kol.setText(text_box.replaceAll("[\\D]", ""));
                            dg_tw_kol.setText(textbox.toString());
                            Integer i = Integer.parseInt(dg_tw_kol.getText().toString());
                            //   if (i > Integer.parseInt(text_box.replaceAll("[\\D]", ""))) {
                            if (i > textbox) {
                                // Integer new_kol = i - Integer.parseInt(text_box.replaceAll("[\\D]", ""));
                                Integer new_kol = i - textbox;
                                dg_tw_kol.setText(new_kol.toString());
                                perem_int_summa = cena * new_kol;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                                ;
                                dg_tw_summa.setText(Format);
                                kol_box_info--;
                                dg_tw_kolbox.setText("x" + kol_box_info);
                                Summa_Skidka();
                            } else
                                Toast.makeText(context_Activity, "Кол-во меньше кор.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                } else {
                    switch (checked_group) {
                        case R.id.radioButton_one: {
                            Integer i = Integer.parseInt(dg_tw_kol.getText().toString());
                            if (i > 1) {
                                i--;
                                dg_tw_kol.setText(i.toString());
                                perem_int_summa = cena * i;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                                ;
                                dg_tw_summa.setText(Format);
                                Summa_Skidka();
                            } else
                                Toast.makeText(context_Activity, "Кол-во меньше 1", Toast.LENGTH_SHORT).show();
                        }
                        break;

                        case R.id.radioButton_much: {
                            Integer i = Integer.parseInt(dg_tw_kol.getText().toString());
                            //  if (i > Integer.parseInt(text_box.replaceAll("[\\D]", ""))) {
                            if (i > textbox) {
                                //    Integer new_kol = i - Integer.parseInt(text_box.replaceAll("[\\D]", ""));
                                Integer new_kol = i - textbox;
                                dg_tw_kol.setText(new_kol.toString());
                                perem_int_summa = cena * new_kol;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                                ;
                                dg_tw_summa.setText(Format);
                                kol_box_info--;
                                dg_tw_kolbox.setText("x" + kol_box_info);
                                Summa_Skidka();
                            } else
                                Toast.makeText(context_Activity, "Кол-во меньше кор.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }

                }

            }
        });

    }  // убавить количество

    protected void Button_Edit(final Integer cena, final Integer ostatok) {

        dg_ed_editkol.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //  editText_kol.setText("1");
                if (dg_ed_editkol.getText().toString().equals("") | dg_ed_editkol.getText().toString().equals(" ") | dg_ed_editkol.getText().toString().equals("0")) {
                    dg_tw_summa.setText("00.00");
                    dg_tw_summa_sk.setText("00.00");
                    // Summa_Skidka();
                } else if (Integer.parseInt(dg_ed_editkol.getText().toString()) <= ostatok) {
                    //  cena_t = Double.parseDouble(text_cena.substring(0, text_cena.length() - 3));
                    //  perem_cena = Double.parseDouble(text_cena);
                    dg_tw_kol.setText(dg_ed_editkol.getText().toString());
                    perem_int_summa = Integer.parseInt(dg_ed_editkol.getText().toString()) * cena;
                    String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                    ;
                    dg_tw_summa.setText(Format);
                    Summa_Skidka();
                } else {
                    Toast.makeText(context_Activity, "кол-во превышает остаток", Toast.LENGTH_SHORT).show();
                    dg_ed_editkol.setText(ostatok.toString());
                    dg_ed_editkol.setSelection(dg_ed_editkol.getText().length());
                    dg_tw_kol.setText(dg_ed_editkol.getText().toString());
                    perem_int_summa = Integer.parseInt(dg_ed_editkol.getText().toString()) * cena;
                    String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                    ;
                    dg_tw_summa.setText(Format);
                    Summa_Skidka();
                }


            }
        });
    }  // производбный ввод количества

    protected void Button_Edit_Skidka() {

        dg_ed_skidka.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Summa_Skidka();

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }  // скидка на товар

    protected void Summa_Skidka() {
        if (dg_ed_skidka.getText().toString().equals("") | dg_ed_skidka.getText().toString().equals(" ") | dg_ed_skidka.getText().toString().equals("0")) {
            dg_tw_summa_sk.setVisibility(View.GONE);
            dg_tw_summa_sk.setText("00.00");
            dg_tw_summa_sk_DOUBLE = 00.00;
        } else {
            dg_tw_summa_sk.setVisibility(View.VISIBLE);
            Double per_sum = (perem_int_summa).doubleValue();
            Double per_sk = Double.parseDouble(dg_ed_skidka.getText().toString());

            Double per_itg = Double.valueOf(per_sum - (per_sum * (per_sk / 100)));
            dg_tw_summa_sk_DOUBLE = per_itg;
            String Format = new DecimalFormat("#00.00").format(per_itg).replace(",", ".");
            ;
            dg_tw_summa_sk.setText(Format);
            Log.e("WJ_FormaSK:", Format + ", " + per_sum + ", " + per_sk + ", " + per_itg);
        }
    }

    protected void Calendare_This_Data() {  // получаем данные времени и имя месяца
        thisdata = Integer.valueOf(localCalendar.get(Calendar.DAY_OF_MONTH));
        thismonth = Integer.valueOf(1 + localCalendar.get(Calendar.MONTH));
        thisyear = Integer.valueOf(localCalendar.get(Calendar.YEAR));
        thissecond = Integer.valueOf(localCalendar.get(Calendar.SECOND));
        thisminyte = Integer.valueOf(localCalendar.get(Calendar.MINUTE));
        thishour = Integer.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY));


        if (thisdata < 10) {
            thisdata_st = String.format("%02d", thisdata);
        } else thisdata_st = thisdata.toString();

        if (thismonth < 10) {
            thismonth_st = String.format("%02d", thismonth);
        } else thismonth_st = thismonth.toString();

        if (thishour < 10) {
            thishour_st = String.format("%02d", thishour);
        } else thishour_st = thishour.toString();

        if (thisminyte < 10) {
            thisminyte_st = String.format("%02d", thisminyte);
        } else thisminyte_st = thisminyte.toString();

        if (thissecond < 10) {
            thissecond_st = String.format("%02d", thissecond);
        } else thissecond_st = thissecond.toString();
    } // Загрузка даты и время

    protected void Search_Image(String uid) {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
            String query = "SELECT base_in_nomeclature.name, base_in_nomeclature.kod_univ, base_in_image.kod_image\n" +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                    "WHERE base_in_nomeclature.kod_univ = '" + uid + "'";

            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String image = cursor.getString(cursor.getColumnIndex("kod_image"));
            String koduid = cursor.getString(cursor.getColumnIndex("kod_univ"));
            select_image = image;
            cursor.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка загрузки картинок!", Toast.LENGTH_SHORT).show();
            Log.e(Log_Text_Error, "Ошибка загрузки картинок!");
        }

    }


    /**
     * прячем программную клавиатуру
     */
    protected void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(dg_ed_editkol.getWindowToken(), 0);
            // imm.hideSoftInputFromWindow(listView.getWindowToken(), 0);
        }
    }

    /**
     * показываем программную клавиатуру
     */
    protected void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private Runnable mShowInputMethodTask = new Runnable() {
        public void run() {
            // showInputMethodForQuery();
        }
    };

    protected void Write_Table_RN(final String kod_uid, final String koduniv, final String name, final String kol, final String cena, final String summa, final String skidka, final Double summaSK, final String image) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
        // Заполнения карточки товара
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("Kod_RN",  postclass_RN);
        localContentValues.put("Vrema", PEREM_K_AG_Vrema);
        localContentValues.put("Data", PEREM_K_AG_Data);
        localContentValues.put("Kod_Univ", koduniv);
        localContentValues.put("koduid", kod_uid);
        localContentValues.put("Name", name);
        localContentValues.put("Kol", kol);
        localContentValues.put("Cena", cena);
        localContentValues.put("Summa", summa);
        localContentValues.put("Image", image);
        localContentValues.put("aks_pref", "ty");

        if (!skidka.isEmpty()) {
          // String Format_summa = new DecimalFormat("#00.00").format(summaSK).replace(",", ".");

            localContentValues.put("Skidka", skidka);

            Double db1 = Double.parseDouble(skidka), db2 = Double.parseDouble(cena), db3;
            db3 = db2 - (db2 * (db1 / 100));
            localContentValues.put("Cena_SK", db3.toString());
            Log.e("WJZakS2=", db1 + " " + db2 + " " + db3);
          //  Double d_itogo = Double.parseDouble(kol) *Double.parseDouble(cena);
            Double d_itogo = db3 * db1;
            String Format_itogo = new DecimalFormat("#00.00").format(d_itogo).replace(",", ".");
            localContentValues.put("Itogo", Format_itogo);
            ReWrite_Table_RN(kod_uid, kol, cena, skidka, "", Format_itogo);
        } else {
            localContentValues.put("Skidka", "0");
            localContentValues.put("Cena_SK", "0");
            localContentValues.put("Itogo", summa);
            ReWrite_Table_RN(kod_uid, kol, cena, "0", "", "0");
        }

        Log.e("WJZakS2=", localContentValues.toString());




        // Проверка сходства позиций
        String query_Search = "SELECT base_RN.Kod_RN, base_RN.Kod_Univ " +
                "FROM base_RN WHERE base_RN.Kod_RN LIKE '%" + PEREM_K_AG_KodRN + "%'";
        Cursor cursor = db.rawQuery(query_Search, null);
        cursor.moveToFirst();
        Integer k = 0;
        while (cursor.isAfterLast() == false) {
            String kod_univ = cursor.getString(cursor.getColumnIndex("Kod_Univ"));
            String kod_rn = cursor.getString(cursor.getColumnIndex("Kod_RN"));
            if (koduniv.equals(kod_univ)) {
                k++;
                cursor.moveToNext();
            } else cursor.moveToNext();

        }

        if (k == 0) {
            db.insert("base_RN", null, localContentValues);
            Toast.makeText(context_Activity, "Товар добавлен!", Toast.LENGTH_SHORT).show();
            cursor.moveToNext();
        } else Toast.makeText(context_Activity, "Товар уже есть!", Toast.LENGTH_SHORT).show();

        localContentValues.clear();
        cursor.close();
        db.close();


        try {

        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка записи данных!", Toast.LENGTH_SHORT).show();
            Log.e(Log_Text_Error, "Ошибка записи данных!");
        }


    }


    protected void ReWrite_Table_RN(String rw_kodUID, String rw_newKOL, String
            rw_CENA, String rw_SKIDKA, String rw_Ostatok, String rw_old_summa) {
        Log.e("Новые данные: ", rw_kodUID);
        Log.e("Новые данные: ", rw_newKOL);
        Log.e("Новые данные: ", rw_CENA);
        Log.e("Новые данные: ", rw_SKIDKA);
        Log.e("Новые данные: ", rw_Ostatok);

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT * FROM base_RN WHERE Kod_RN = '" + postclass_RN + "' AND koduid = '" + rw_kodUID + "';";

        Cursor cursor = db.rawQuery(query, null);

        ContentValues localContentValuesUP = new ContentValues();
        ContentValues localContentValuesUP_All = new ContentValues();
        ContentValues localContentValuesUP_Debet = new ContentValues();
        cursor.moveToFirst();

        Double d_summa = 0.0, d_itogo = 0.0, d_kol = Double.parseDouble(rw_newKOL),
                d_cena = Double.parseDouble(rw_CENA), d_skidka = Double.parseDouble(rw_SKIDKA);
        d_summa = d_kol * d_cena;
        d_itogo = d_summa - (d_summa * (d_skidka / 100));
        String Format_Summa = new DecimalFormat("#00.00").format(d_summa);
        String Format_Itogo = new DecimalFormat("#00.00").format(d_itogo);
        localContentValuesUP.put("Kol", rw_newKOL);
        localContentValuesUP.put("Summa", Format_Summa.replace(",", "."));
        localContentValuesUP.put("Itogo", Format_Itogo.replace(",", "."));
        db.update("base_RN", localContentValuesUP, "Kod_RN = ? AND koduid = ?", new String[]{postclass_RN, rw_kodUID});
        cursor.close();


        String query_rn_all = "SELECT SUM(Summa) AS 'newsumma', SUM(Itogo) AS 'newitogo' FROM base_RN WHERE Kod_RN = '" + postclass_RN + "';";
        Cursor cursor_rn_All = db.rawQuery(query_rn_all, null);
        cursor_rn_All.moveToFirst();
        String all_summa = cursor_rn_All.getString(cursor_rn_All.getColumnIndex("newsumma"));
        String all_itogo = cursor_rn_All.getString(cursor_rn_All.getColumnIndex("newitogo"));
        localContentValuesUP_All.put("summa", all_summa);
        localContentValuesUP_All.put("itogo", all_itogo);
        localContentValuesUP_All.put("debet_new", all_itogo);
        getSupportActionBar().setSubtitle("Итого: " + all_itogo);
        postclass_OLD_SUMMA = all_itogo;
        db.update("base_RN_All", localContentValuesUP_All, "Kod_RN = ? ", new String[]{postclass_RN});
        cursor_rn_All.close();


        String query_debet_count = "SELECT * FROM otchet_Debet WHERE d_kontr_uid = '" + postclass_clients_uid + "'";
        Cursor cursor_debet_count = db.rawQuery(query_debet_count, null);
        cursor_debet_count.moveToFirst();
        String debet = cursor_debet_count.getString(cursor_debet_count.getColumnIndex("d_summa"));
        Log.e("OLD_SUMMA", rw_old_summa);
        Log.e("NEW_SUMMA", all_itogo);
        if (Double.parseDouble(rw_old_summa) > Double.parseDouble(all_itogo)) {
            Double new_debet = Double.parseDouble(debet) - (Double.parseDouble(rw_old_summa) - Double.parseDouble(all_itogo));
            String Format_New_Debet = new DecimalFormat("#00.00").format(new_debet);
            localContentValuesUP_Debet.put("d_summa", Format_New_Debet.replace(",", "."));
            Log.e("меньше", Format_New_Debet);
        } else {
            Double new_debet = Double.parseDouble(debet) + ((Double.parseDouble(all_itogo) - Double.parseDouble(rw_old_summa)));
            String Format_New_Debet = new DecimalFormat("#00.00").format(new_debet);
            localContentValuesUP_Debet.put("d_summa", Format_New_Debet.replace(",", "."));
            Log.e("больше", Format_New_Debet);
        }
        db.update("otchet_debet", localContentValuesUP_Debet, "d_kontr_uid = ? ", new String[]{postclass_clients_uid});
        cursor_debet_count.close();


        db.close();
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
        PEREM_SELECT_BRENDS = sp.getString("PEREM_SELECT_BRENDS", "0");          //чтение данных: Универсальный номер пользователя


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

        PEREM_K_AG_NAME = sp.getString("PEREM_K_AG_NAME", "0");          //чтение данных: имя контраегнта
        PEREM_K_AG_UID = sp.getString("PEREM_K_AG_UID", "0");            //чтение данных: uid контрагента
        PEREM_K_AG_ADRESS = sp.getString("PEREM_K_AG_ADRESS", "0");      //чтение данных: адрес контрагент
        PEREM_K_AG_KodRN = sp.getString("PEREM_K_AG_KodRN", "0");         //чтение данных: код накладной
        PEREM_K_AG_Data = sp.getString("PEREM_K_AG_Data", "0");           //чтение данных: время создание н
        PEREM_K_AG_Vrema = sp.getString("PEREM_K_AG_Vrema", "0");        //чтение данных: дата создание на
        PEREM_K_AG_GPS = sp.getString("PEREM_K_AG_GPS", "0");             //чтение данных: координаты gps


        PEREM_READ_KODRN = sp.getString("PEREM_READ_KODRN", "0");             //чтение данных: координаты gps
    }
}
