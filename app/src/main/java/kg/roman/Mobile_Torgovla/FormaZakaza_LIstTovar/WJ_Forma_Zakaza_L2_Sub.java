package kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Suncape_Forma;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Suncape_Forma;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.CalendarThis;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.PreferencesWrite;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.Preferences_MTSetting;
import kg.roman.Mobile_Torgovla.R;


public class WJ_Forma_Zakaza_L2_Sub extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ArrayList<ListAdapterSimple_Suncape_Forma> product_str = new ArrayList<>();
    public ListAdapterAde_Suncape_Forma adapterPriceClients;

    public NavigationView navigationView;
    public Menu menu;
    public SubMenu topChannelMenu;
    public Toolbar toolbar;
    public androidx.appcompat.app.AlertDialog.Builder dialog;
    public View navHeader;
    public TextView dg_tw_name, dg_tw_koduid, dg_tw_koduniv, dg_tw_cena, dg_tw_ostatok, dg_tw_kol, dg_tw_summa, dg_tw_summa_sk, dg_tw_kolbox, dg_tw_kolbox_org;
    public EditText dg_ed_skidka, dg_ed_editkol;

    public Button btn_down, btn_up, button_ok, button_cancel;
    public View localView;

    public RadioGroup radioGroup_local;
    public RadioButton radioGroup_one, radioGroup_much, radioGroup_edit;

    public Calendar localCalendar = Calendar.getInstance();
    public Integer checked_group, kol_box_info, max_box;
    public Integer thisdata, thismonth, thisyear, thisminyte, thishour, thissecond;
    public Integer perem_int_summa, perem_int_ostatok, perem_int_cena, perem_int_kol, perem_kol_group_one, perem_int_kolbox;
    public Double dg_tw_summa_sk_DOUBLE;
    public Double TY, TY_Kons, Doub_Cena, Doub_Kons, Doub_Nal;

    public String table_name, name_group, id_st, name, kod, select_image;
    public String Cena_All, Cena_Nal, Cena_Kons, cena;
    public String thisdata_st, thismonth_st, thisminyte_st, thishour_st, thissecond_st;
    public String lst_tw_name, lst_tw_kod, lst_tw_cena, lst_tw_ostatok, lst_tw_kolbox, lst_tw_koduid,
            lst_tw_uid_sklad, lst_tw_name_sklad;
    public String kol_group_one, kol_group_much;


    // 01.2024 новые параметры, переделывание
    String logeTAG = "Forma_Zakaza_F2Sub";
    //  private MtNomenclaturaContentBinding binding;   нужно разобраться с inding lkz ActionBAr
    public SharedPreferences mSettings;
    public SharedPreferences.Editor ed;
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    public Context context_Activity = WJ_Forma_Zakaza_L2_Sub.this;
    public String mSettings_subBrends;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    String wCodeOrder;
    Preferences_MTSetting preferencesMtSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Bella); // (for Custom theme)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt_spr_nomenclatura);
/*        binding = MtNomenclaturaContentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());*/

        recyclerView = findViewById(R.id.nomenclatura_RecyclearView);
        progressBar = findViewById(R.id.nomenclatura_ProgressBar);
        //  progressBar2 = findViewById(R.id.nomenclatura_ProgressBarMT);


        mSettings = getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        mSettings_subBrends = mSettings.getString("sp_BREND", "0");


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(mSettings_subBrends);
        getSupportActionBar().setIcon(R.drawable.user_pda);

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

        preferencesMtSetting = new Preferences_MTSetting();
        wCodeOrder = preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getCodeOrder());


        // Ошибки при загрузке 07.11.2023

        Navigation_Menu(mSettings_subBrends);           // Normal
        Navigation_Title_Image();    // Normal, Нужно обдумать новую логику, много кода

        Log.e("TEST", "select_Brends=" + mSettings_subBrends + ".");
        ed = mSettings.edit();
        ed.putString("PEREM_SELECT_BRENDS", id_st);
        ed.commit();
        id_st = topChannelMenu.getItem(0).toString();
        Log.e("TEST", "select_subBrends=" + id_st + ".");


        //Navigation_Group_Name();


        //  Toast.makeText(context_Activity, "Группа= " + id_st, Toast.LENGTH_SHORT).show();
        //Log.e("Nomen=", "id_st=" + id_st + ".");
  /*      product_str.clear();
        Loading_Db_Nomencalture();
        adapterPriceClients = new ListAdapterAde_Suncape_Forma(context_Activity, product_str);
        adapterPriceClients.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients);
*/


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.e("TEST", "onNavigationItemSelected");
        int id = item.getItemId();
        id_st = item.getTitle().toString();

        getSupportActionBar().setTitle(mSettings_subBrends);
        getSupportActionBar().setSubtitle(id_st);
        table_name = "table_" + mSettings_subBrends;
        try {
            //Navigation_Group_Name();
            Navigation_Menu(mSettings_subBrends);
            product_str.clear();

            RecyclerView_Adapter_ViewHolder_Nomeclatura.OnStateClickListener stateClickListener = new RecyclerView_Adapter_ViewHolder_Nomeclatura.OnStateClickListener() {
                @Override
                public void onStateClick(ListAdapterSimple_Suncape_Forma clientClick, int position) {
                    Log.e("TEST", "Click" + clientClick.getName());
                    ClickList(clientClick);
                }
            };
            Async_ViewModel_Nomenclature model;
            model = new ViewModelProvider(this).get(Async_ViewModel_Nomenclature.class);
            model.getValues(id_st).observe(this, list_simple ->
            {
                RecyclerView_Adapter_ViewHolder_Nomeclatura adapter;
                // ArrayList<ListAdapterSimple_Klients> list_clients = new ArrayList<>();
                adapter = new RecyclerView_Adapter_ViewHolder_Nomeclatura(getBaseContext(), list_simple, stateClickListener);
                recyclerView.setAdapter(adapter);
                adapter.notifyItemChanged(0, 0);
                // Log.e("ViewHolder", ftp_image_newRun.toString());

            });
            model.getLoadingStatus().observe(this, status ->
            {
                if (status == true) {
                    progressBar.setVisibility(View.VISIBLE);
                    //  progressBar2.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    //  progressBar2.setVisibility(View.INVISIBLE);
                }
            });
            model.execute();



            /*adapterPriceClients = new ListAdapterAde_Suncape_Forma(context_Activity, product_str);
            adapterPriceClients.notifyDataSetChanged();
            listView.setAdapter(adapterPriceClients);*/
            //  Toast.makeText(context_Activity, "Данные:" + id + ", " + id_st + ", " + table_name + ", " + name_group, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Нет данных", Toast.LENGTH_SHORT).show();
            Log.e("onNavigationItemSelected", "нет данных");
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_nomenclature);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void Navigation_Menu(String selectBrends) {
        Log.e(logeTAG, "Navigation_Menu: Start");

        try {
            String brend_new, subBrend_new;
            String ovvr_selectBrends = selectBrends.toLowerCase().trim();
            PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);

            Log.e(logeTAG, "t1=" + ovvr_selectBrends);
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_BASE, MODE_PRIVATE, null);
            String query = "SELECT brends, subbrends FROM base_in_brends_id\n" +
                    "LEFT JOIN base_in_brends_sub_id ON base_in_brends_id.kod = base_in_brends_sub_id.parent_kod\n" +
                    "WHERE trim(LOWER(brends)) == '" + ovvr_selectBrends + "'";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            Menu m = navigationView.getMenu();
            m.clear();
            topChannelMenu = m.addSubMenu(preferencesWrite.SelectMenu_forBrends);
            while (!cursor.isAfterLast()) {
                String brend = cursor.getString(cursor.getColumnIndexOrThrow("brends")); // код клиента
                String sub_brends = cursor.getString(cursor.getColumnIndexOrThrow("subbrends")); // код клиента

                brend_new = brend.substring(0, 1) + brend.substring(1).toLowerCase();
                subBrend_new = sub_brends.substring(0, 1) + sub_brends.substring(1).toLowerCase();
                Log.e(logeTAG, "t1=" + ovvr_selectBrends + ", t2=" + brend_new + ", t3=" + subBrend_new);


                if ((brend.toLowerCase().trim()).equals(ovvr_selectBrends))
                    topChannelMenu.add(subBrend_new).setIcon(R.drawable.ic_cost);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.e(logeTAG, "ошибка заполнения списка подгруппы бренда");
            Toast.makeText(context_Activity, "ошибка заполнения списка подгруппы бренда", Toast.LENGTH_SHORT).show();
        }

        Log.e(logeTAG, "Navigation_Menu: END");
    }  // Программное создание меню

    //////////////////////////////////  02.2024
    //// Поиск картинки для брендов в номенклатуре
    protected Drawable IconsForBrends(String pref) {
        // setIcon(Drawable.createFromPath(getFilesDir().getAbsolutePath()+"/Icons/icons_image_for_icons.png"));
        String filename = "icons_logo_menu_" + pref + ".png";
        //Log.e(logeTAG, "fileName"+filename);
        File file = new File(getFilesDir().getAbsolutePath() + "/Icons/" + filename);
        if (file.exists())
            return Drawable.createFromPath(file.getPath());
        else {
            Resources res = getResources();
            Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.no_image, null);
            return drawable;
        }
    }


    protected void Navigation_Title_Image() {
        Log.e("TEST", "Navigation_Menu_Image: Start");
        navHeader = navigationView.getHeaderView(0);
        Log.e("Brends=", mSettings_subBrends);
        getSupportActionBar().setIcon(0);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(context_Activity, R.color.gray));
        CreateInterfaceforBrends myInterface = new CreateInterfaceforBrends(context_Activity, mSettings_subBrends, navHeader, toolbar, window);
        myInterface.firmaTradeGof();
        myInterface.firmaSunbell();
        myInterface.firmaFamaly();
        myInterface.firmaLavr();

        /*        LinearLayout linearLayout = (LinearLayout) navHeader.findViewById(R.id.lineary_header);
        imageView_header = (ImageView) navHeader.findViewById(R.id.nav_header_nomenclature);
        textView_header1 = (TextView) navHeader.findViewById(R.id.tvw_nav_headerTitle);
        textView_header2 = (TextView) navHeader.findViewById(R.id.tvw_nav_headerText);
        textView_header2 = (TextView) navHeader.findViewById(R.id.tvw_nav_headerText);
        TextView tvw_headerPref = (TextView) navHeader.findViewById(R.id.tvw_nav_headerPref);
        textView_header1.setTextSize(12);
        textView_header2.setTextSize(10);
        tvw_headerPref.setVisibility(View.GONE);
        linearLayout_header = (LinearLayout) navHeader.findViewById(R.id.lineary_header);*/


/*
        switch (mSettings_subBrends) {
            // TradeGof
            case "Bella": {
*//*               toolbar.setBackgroundColor(getResources().getColor(R.color.bella_colorPrimary));
                getSupportActionBar().setIcon(R.drawable.logo_bella);
                imageView_header.setImageResource(R.drawable.logo_bella);
                textView_header1.setText(R.string.header_bella_title_1);*//*
         *//*              textView_header2.setText(R.string.header_bella_title_2);
                linearLayout.setBackground(getDrawable(R.drawable.side_nav_bar_bella));*//*

                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_bella));
                // для API <21
                //   linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_bella));
            }
            break;
            case "Viero (тиссью груп)": {
*//*                toolbar.setBackgroundColor(getResources().getColor(R.color.viero_colorPrimary));
                getSupportActionBar().setIcon(R.drawable.icon_menu_viero_2);
                imageView_header.setImageResource(R.drawable.icon_menu_viero_2);
                textView_header1.setText(R.string.header_viero_title_1);
                textView_header2.setText(R.string.header_viero_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_bella));
                // для API <21
                //   linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_viero));*//*
            }
            break;
            case "Yokosun": {
*//*                toolbar.setBackgroundColor(getResources().getColor(R.color.yokosun_colorPrimary));
                getSupportActionBar().setIcon(R.drawable.icon_menu_yokosun);
                imageView_header.setImageResource(R.drawable.icon_menu_yokosun);
                textView_header1.setText(R.string.header_yokosun_title_1);
                textView_header2.setText(R.string.header_yokosun_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_bella));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_yokosun));*//*
            }
            break;
            case "President": {
     *//*           toolbar.setBackgroundColor(getResources().getColor(R.color.president_colorPrimary));
                getSupportActionBar().setIcon(R.drawable.icon_menu_president);
                imageView_header.setImageResource(R.drawable.icon_menu_president);
                textView_header1.setText(R.string.header_president_title_1);
                textView_header2.setText(R.string.header_president_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_bella));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_president));*//*
            }
            break;

            case "Cussons": {
*//*                toolbar.setBackgroundColor(getResources().getColor(R.color.cussons_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_pzcussons);
                imageView_header.setImageResource(R.mipmap.logo_pzcussons);
                textView_header1.setText(R.string.header_cussons_title_1);
                textView_header2.setText(R.string.header_cussons_title_2);
                // для API 21>
                // linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_cussons));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_cussons));*//*
            }
            break;
            case "Pcc": {
*//*                toolbar.setBackgroundColor(getResources().getColor(R.color.pcc_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_flo);
                imageView_header.setImageResource(R.mipmap.logo_flo);
                textView_header1.setText(R.string.header_pcc_title_1);
                textView_header2.setText(R.string.header_pcc_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_pcc));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_pcc));*//*
            }
            break;
            case "Plushe": {
*//*                toolbar.setBackgroundColor(getResources().getColor(R.color.plushe_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.ic_plushe);
                imageView_header.setImageResource(R.mipmap.ic_plushe);
                textView_header1.setText(R.string.header_plushe_1);
                textView_header2.setText(R.string.header_plushe_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_plushe));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_plushe));*//*
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
*//*                toolbar.setBackgroundColor(getResources().getColor(R.color.sofidel_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_sofidel);
                imageView_header.setImageResource(R.mipmap.logo_sofidel);
                textView_header1.setText(R.string.header_sofidel_title_1);
                textView_header2.setText(R.string.header_sofidel_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_sofidel));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_sofidel));*//*
            }
            break;
            case "Драмерс": {
*//*                toolbar.setBackgroundColor(getResources().getColor(R.color.dramers_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_brait);
                imageView_header.setImageResource(R.mipmap.logo_brait);
                textView_header1.setText(R.string.header_dramers_title_1);
                textView_header2.setText(R.string.header_dramers_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_dramers));*//*
            }
            break;
            case "Первое решение": {
*//*                toolbar.setBackgroundColor(getResources().getColor(R.color.agafia_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_agafia);
                imageView_header.setImageResource(R.mipmap.logo_agafia);
                textView_header1.setText(R.string.header_one_work);
                textView_header2.setText(R.string.header_one_work_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_agadia));*//*
            }
            break;
            case "Пкф сонца": {
*//*                toolbar.setBackgroundColor(getResources().getColor(R.color.dramers_colorAccent));
                getSupportActionBar().setIcon(R.mipmap.logo_soncy);
                imageView_header.setImageResource(R.mipmap.logo_soncy);
                textView_header1.setText(R.string.header_pkf_title_1);
                textView_header2.setText(R.string.header_pkf_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_soncy));*//*
            }
            break;
            case "Свобода": {
*//*                toolbar.setBackgroundColor(getResources().getColor(R.color.rens_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_svoboda);
                imageView_header.setImageResource(R.mipmap.logo_svoboda);
                textView_header1.setText(R.string.header_svoboda_title_1);
                textView_header2.setText(R.string.header_svoboda_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_svoboda));*//*
            }
            break;
            case "Lacalut": {
*//*                toolbar.setBackgroundColor(getResources().getColor(R.color.pkf_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_lacalute);
                imageView_header.setImageResource(R.mipmap.logo_lacalute);
                textView_header1.setText(R.string.header_lacalute_title_1);
                textView_header2.setText(R.string.header_lacalute_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_lacalute));*//*
            }
            break;
            case "Ооо \"биг\"": {
*//*                toolbar.setBackgroundColor(getResources().getColor(R.color.big_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_big);
                imageView_header.setImageResource(R.mipmap.logo_big);
                textView_header1.setText(R.string.header_big_title_1);
                textView_header2.setText(R.string.header_big_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_big));*//*
            }
            break;
            case "Ооо \"наука, техника, медицина\"": {
*//*                toolbar.setBackgroundColor(getResources().getColor(R.color.ntm_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_ntm);
                imageView_header.setImageResource(R.mipmap.logo_ntm);
                textView_header1.setText(R.string.header_nauka_title_1);
                textView_header2.setText(R.string.header_nauka_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_ntm));*//*
            }
            break;
            case "Ontex tuketim urunleri": {
*//*                toolbar.setBackgroundColor(getResources().getColor(R.color.ontex_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_ontex);
                imageView_header.setImageResource(R.mipmap.logo_ontex);
                textView_header1.setText(R.string.header_ontex_title_1);
                textView_header2.setText(R.string.header_ontex_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_ontex));*//*
            }
            break;
            case "Halk hijyenik urunler": {
*//*                toolbar.setBackgroundColor(getResources().getColor(R.color.halk_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_halk);
                imageView_header.setImageResource(R.mipmap.logo_halk);
                textView_header1.setText(R.string.header_halk_title_1);
                textView_header2.setText(R.string.header_halk_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_halk));*//*
            }
            break;
            case "Ооо \"коттон клаб\"": {
*//*                toolbar.setBackgroundColor(getResources().getColor(R.color.cotton_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_cotton);
                imageView_header.setImageResource(R.mipmap.logo_cotton);
                textView_header1.setText(R.string.header_cottonclub_title_1);
                textView_header2.setText(R.string.header_cottonclub_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_cotton));*//*
            }
            break;
            case "Ооо \"пк \"уфа пак\"": {
*//*                toolbar.setBackgroundColor(getResources().getColor(R.color.ufa_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_ufa);
                imageView_header.setImageResource(R.mipmap.logo_ufa);
                textView_header1.setText(R.string.header_ufapack_title_1);
                textView_header2.setText(R.string.header_ufapack_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_ufapack));*//*
            }
            break;


            // Старые бренды
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
                Toast.makeText(context_Activity, "Графическая оболочка не готова!", Toast.LENGTH_SHORT).show();
                break;
        }

        Log.e("TEST", "Navigation_Menu_Image: END");*/

    }  // Загрузка визуального оформления для меню по брендам

    protected void ClickList(ListAdapterSimple_Suncape_Forma tovar) {

        try {
            lst_tw_name = tovar.name;
            lst_tw_kod = tovar.kod_univ;
            lst_tw_koduid = tovar.kod_uid;

            lst_tw_name_sklad = tovar.name_sklad;
            lst_tw_uid_sklad = tovar.uid_sklad;
            lst_tw_kolbox = tovar.kol;
            lst_tw_cena = tovar.cena;
            lst_tw_ostatok = tovar.getOstatki();


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
            Fun_Messeger_Panel(perem_int_cena, perem_int_ostatok, perem_int_kolbox, lst_tw_name_sklad, lst_tw_uid_sklad);

            Log.e("WJ_FormaLSINT:", perem_int_kol + ", " + perem_int_cena + ", " + perem_int_ostatok + ", " + Format);
        } else
            Toast.makeText(context_Activity, "не достаточно товара", Toast.LENGTH_SHORT).show();

    }

    protected void Adapter_Price() {
        // 07.11.2023 проверка, оключенно
       /* adapterPriceClients = new ListAdapterAde_Suncape_Forma(context_Activity, product_str);
        adapterPriceClients.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients);*/
     /*   listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Заполнение из карточик товара
                try {
                    lst_tw_name = ((TextView) view.findViewById(R.id.textView_forma_name)).getText().toString();
                    lst_tw_kod = ((TextView) view.findViewById(R.id.textView_forma_kod)).getText().toString();
                    lst_tw_koduid = ((TextView) view.findViewById(R.id.text_uid_new)).getText().toString();

                    lst_tw_name_sklad = ((TextView) view.findViewById(R.id.textView_Name_Sklad)).getText().toString();
                    lst_tw_uid_sklad = ((TextView) view.findViewById(R.id.textView_UID_Sklad)).getText().toString();

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
                    Fun_Messeger_Panel(perem_int_cena, perem_int_ostatok, perem_int_kolbox, lst_tw_name_sklad, lst_tw_uid_sklad);

                    Log.e("WJ_FormaLSINT:", perem_int_kol + ", " + perem_int_cena + ", " + perem_int_ostatok + ", " + Format);
                } else
                    Toast.makeText(context_Activity, "не достаточно товара", Toast.LENGTH_SHORT).show();
            }
        });
*/

    }


    protected void Navigation_Group_Name() {

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("logins.db3", MODE_PRIVATE, null);
        String query = "SELECT table_all_brends._id, table_all_brends.brends, " +
                "table_all_brends.name_new, table_all_brends.name_db, table_all_brends.kod_uid\n" +
                "FROM table_all_brends";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
            String brends = cursor.getString(cursor.getColumnIndexOrThrow("brends")); // код клиента
            String name_new = cursor.getString(cursor.getColumnIndexOrThrow("name_new"));
            String name_db = cursor.getString(cursor.getColumnIndexOrThrow("name_db"));
            String koduid = cursor.getString(cursor.getColumnIndexOrThrow("kod_uid"));
            if (id_st.equals(name_new) & mSettings_subBrends.equals(brends))
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

    protected void Loading_Db_Nomencalture() throws InterruptedException {
        Log.e("TEST", "Loading_Db_Nomencalture START");

       /* Thread myThread = new Thread(new MyThread(), "MyThread");
        myThread.start();*/

/*        WJ_Forma_Zakaza_L2_Sub.MyAsyncTask_Sync asyncTask = new WJ_Forma_Zakaza_L2_Sub.MyAsyncTask_Sync();
        asyncTask.execute();*/


        Log.e("TEST", "Loading_Db_Nomencalture END");
    }  // Загрузка номенклатуры по брендам и по группам


    protected void Params() {
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        String listValue_all = mSettings.getString("list_all_nal", "0");
        String listValue2_all = mSettings.getString("list_all_kons", "0");

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

/*    protected void Cena_for_DB() {
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

    }  // Вычисление цен(своя, скидка за нал и конс)*/


    protected void Fun_Messeger_Panel(final Integer cena, final Integer ostatok, final Integer box,
                                      final String m_sklad_name, final String m_sklad_uid) {
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
                    Write_Table_RN(dg_tw_koduid.getText().toString(), dg_tw_koduniv.getText().toString(),
                            dg_tw_name.getText().toString(), dg_tw_kol.getText().toString(),
                            dg_tw_cena.getText().toString(), dg_tw_summa.getText().toString(),
                            dg_ed_skidka.getText().toString(), dg_tw_summa_sk_DOUBLE, select_image,
                            m_sklad_name, m_sklad_uid);
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
            PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_BASE, MODE_PRIVATE, null);
            String query = "SELECT base_in_nomeclature.name, base_in_nomeclature.kod_univ, base_in_image.kod_image\n" +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                    "WHERE base_in_nomeclature.kod_univ = '" + uid + "'";

            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String image = cursor.getString(cursor.getColumnIndexOrThrow("kod_image"));
            String koduid = cursor.getString(cursor.getColumnIndexOrThrow("kod_univ"));
            select_image = image;
            cursor.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка загрузки картинок!", Toast.LENGTH_SHORT).show();
            Log.e(logeTAG, "Ошибка загрузки картинок!");
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


    protected void Write_Table_RN(final String kod_uid, final String koduniv, final String name,
                                  final String kol, final String cena, final String summa,
                                  final String skidka, final Double summaSK, final String image,
                                  final String sklad_name, final String sklad_uid) {
        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        CalendarThis calendarThis = new CalendarThis();
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        // Заполнения карточки товара
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("Kod_RN", wCodeOrder);
        localContentValues.put("Vrema", calendarThis.getThis_DateFormatVrema);
        localContentValues.put("Data", calendarThis.getThis_DateFormatSqlDB);
        localContentValues.put("Kod_Univ", koduniv);
        localContentValues.put("koduid", kod_uid);
        localContentValues.put("Name", name);
        localContentValues.put("Kol", kol);
        localContentValues.put("Cena", cena);
        localContentValues.put("Summa", summa);
        localContentValues.put("Image", image);
        localContentValues.put("aks_pref", "ty");
        localContentValues.put("aks_name", "aks_name");
        localContentValues.put("sklad_name", sklad_name);
        localContentValues.put("sklad_uid", sklad_uid);

        localContentValues.put("Skidka", createNewParamsSale(preferencesWrite, cena, summa).getSale());
        localContentValues.put("Cena_SK", createNewParamsSale(preferencesWrite, cena, summa).getPriceSale());
        localContentValues.put("Itogo ", createNewParamsSale(preferencesWrite, cena, summa).getItogoSale());

        /*
        *         if (!preferencesWrite.Setting_MT_K_AG_Sale.isEmpty() && Integer.parseInt(preferencesWrite.Setting_MT_K_AG_Sale) > 0) {
            if (Double.parseDouble(summa) >= Double.parseDouble(preferencesWrite.Setting_TY_SaleMinSumSale)) {

            }
            String Format_summa = new DecimalFormat("#00.00").format(summaSK).replace(",", ".");

            Double db1 = Double.parseDouble(skidka), db2 = Double.parseDouble(cena), db3;
            db3 = db2 - (db2 * (db1 / 100));
            Log.e("WJZakS2=", db1 + " " + db2 + " " + db3);
            localContentValues.put("Skidka", skidka);
            localContentValues.put("Cena_SK", db3.toString());
            localContentValues.put("Itogo", Format_summa.replaceAll(",", "."));
        } else {
            localContentValues.put("Skidka", "0");
            localContentValues.put("Cena_SK", "0");
            localContentValues.put("Itogo", summa);
        }*/

        // Проверка сходства позиций
        String query_Search = "SELECT Kod_RN, Kod_Univ " +
                "FROM '" + SelectRN() + "'WHERE Kod_RN = '" + wCodeOrder + "'";
        Cursor cursor = db.rawQuery(query_Search, null);
        cursor.moveToFirst();
        int k = 0;
        while (!cursor.isAfterLast()) {
            String kod_univ = cursor.getString(cursor.getColumnIndexOrThrow("Kod_Univ"));
            String kod_rn = cursor.getString(cursor.getColumnIndexOrThrow("Kod_RN"));
            if (koduniv.equals(kod_univ)) {
                k++;
                cursor.moveToNext();
            } else cursor.moveToNext();

        }

        if (k == 0) {
            db.insert(SelectRN(), null, localContentValues);
            Toast.makeText(context_Activity, "Товар добавлен!", Toast.LENGTH_SHORT).show();
            cursor.moveToNext();
        } else Toast.makeText(context_Activity, "Товар уже есть!", Toast.LENGTH_SHORT).show();

        localContentValues.clear();
        cursor.close();
        db.close();


        try {

        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка записи данных!", Toast.LENGTH_SHORT).show();
            Log.e(logeTAG, "Ошибка записи данных!" + e);
        }


    }

    /////////////////   01.2024
    /////// Перезаполнение

    protected String SelectRN() {
        if (preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getStatusOrder()).equals("Edit"))
            return "base_RN_Edit";
        else return "base_RN";
    }

    protected newWriteSale createNewParamsSale(PreferencesWrite preferencesWrite, String w_Price, String w_Sum) {
        newWriteSale newWrite = new newWriteSale(0, 0, 0);

        int sale, saleSetting = preferencesMtSetting.readSettingInt(context_Activity, preferencesMtSetting.getSaleCount());
        double priceSale, itogoSale;
        if (saleSetting == 0) {
            if (Double.parseDouble(w_Sum) >= Double.parseDouble(preferencesWrite.Setting_TY_SaleMinSumSale)) {
                sale = Integer.parseInt(preferencesWrite.Setting_TY_Sale);
                priceSale = Double.parseDouble(w_Price) - (Double.parseDouble(w_Price) * ((double) sale / 100));
                itogoSale = Double.parseDouble(w_Sum) - (Double.parseDouble(w_Sum) * ((double) sale / 100));
            } else {
                sale = 0;
                priceSale = Double.parseDouble(w_Price);
                itogoSale = Double.parseDouble(w_Sum);
            }

        } else {
            sale = saleSetting;
            priceSale = Double.parseDouble(w_Price) - (Double.parseDouble(w_Price) * ((double) sale / 100));
            itogoSale = Double.parseDouble(w_Sum) - (Double.parseDouble(w_Sum) * ((double) sale / 100));
        }
        newWrite.setSale(sale);
        newWrite.setPriceSale(priceSale);
        newWrite.setItogoSale(itogoSale);
        // return new newWriteSale(sale, priceSale, itogoSale);
        return newWrite;
    }

    static class newWriteSale {
        private int Sale;
        private double priceSale;
        private double ItogoSale;

        newWriteSale(int w_Sale, double w_PriceSale, double w_ItogoSale) {
            this.Sale = w_Sale;
            this.priceSale = w_PriceSale;
            this.ItogoSale = w_ItogoSale;
        }

        public int getSale() {
            return Sale;
        }

        public void setSale(int sale) {
            this.Sale = sale;
        }

        public void setPriceSale(double priceSale) {
            this.priceSale = priceSale;
        }

        public void setItogoSale(double ItogoSale) {
            this.ItogoSale = ItogoSale;
        }

        public String getPriceSale() {
            return new DecimalFormat("#00.00").format(priceSale).replace(",", ".");
        }

        public String getItogoSale() {
            return new DecimalFormat("#00.00").format(ItogoSale).replace(",", ".");
        }
    }


}

/*    /// Работа с потоками

    private class MyAsyncTask_Sync extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК=", "Начало потока");
*//*            progressBarOR.setVisibility(View.VISIBLE);
            product_str.clear();
            adapterPriceClients = new ListAdapterAde_Suncape_Forma(context_Activity, product_str);
            adapterPriceClients.notifyDataSetChanged();
            listView.setAdapter(adapterPriceClients);*//*
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            //pDialog.setMessage("Синхронизация цен. Подождите...");
            // pDialog.setProgress(values[0]);
            Log.e("ПОТОК=", "поток работает" + values);
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
            progressBar.setVisibility(View.INVISIBLE);
            Log.e("TEST", "Loading_Db_Nomencalture ListView");
            if (!product_str.isEmpty()) {
                adapterPriceClients = new ListAdapterAde_Suncape_Forma(context_Activity, product_str);
                adapterPriceClients.notifyDataSetChanged();
                // listView.setAdapter(adapterPriceClients);

            } else
                Toast.makeText(context_Activity, "В данной категории нет товара", Toast.LENGTH_SHORT).show();


            //  pDialog.dismiss();
        }

        private void getFloor() throws InterruptedException {
            //  pDialog.setMessage("Загрузка продуктов. Подождите...");
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
            String query;
            if (perem_switch_group_sql) {
                query = "SELECT base_in_nomeclature.name, base_in_nomeclature.brends, base_in_nomeclature.p_group, base_in_nomeclature.kod, base_in_image.kod_image, \n" +
                        "base_in_nomeclature.kolbox, base_in_ostatok.count, base_in_ostatok.sklad_uid, base_in_ostatok.sklad_uid, const_sklad.sklad_name,\n" +
                        "base_in_price.price, base_in_nomeclature.strih, base_in_nomeclature.kod_univ, base_in_nomeclature.koduid\n" +
                        "FROM base_in_nomeclature\n" +
                        "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                        "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                        "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                        "LEFT JOIN base_group_sql ON base_in_nomeclature.koduid = base_group_sql.uid_name\n" +
                        "LEFT JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid\n" +
                        "WHERE count > 0\n" +
                        "ORDER BY base_in_nomeclature.brends, base_in_nomeclature.p_group, base_group_sql.type_group ASC;";
                //  "GROUP BY base_in_nomeclature.name\n" +
            } else {
                query = "SELECT base_in_nomeclature.name, base_in_nomeclature.brends, " +
                        "base_in_nomeclature.p_group, base_in_nomeclature.kod, base_in_image.kod_image, \n" +
                        "base_in_nomeclature.kolbox, base_in_ostatok.count, base_in_ostatok.sklad_uid, " +
                        "base_in_ostatok.sklad_uid, const_sklad.sklad_name,\n" +
                        "base_in_price.price, base_in_nomeclature.strih, base_in_nomeclature.kod_univ, base_in_nomeclature.koduid\n" +
                        "FROM base_in_nomeclature\n" +
                        "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                        "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                        "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                        "LEFT JOIN base_group_sql ON base_in_nomeclature.koduid = base_group_sql.uid_name\n" +
                        "LEFT JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid\n" +
                        "WHERE count > 0\n" +
                        "ORDER BY base_in_nomeclature.brends, base_in_nomeclature.p_group, base_group_sql.type_group ASC;";

                //  "GROUP BY base_in_nomeclature.name\n" +
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
                String uid_sklad = cursor.getString(cursor.getColumnIndexOrThrow("sklad_uid"));
                String name_sklad = cursor.getString(cursor.getColumnIndexOrThrow("sklad_name"));
                String brends = brend.substring(0, 1) + brend.substring(1).toLowerCase();

                String sub_brends = p_group.substring(0, 1) + p_group.substring(1).toLowerCase();
                String ostatok;
                if (count != (null)) {
                    ostatok = count + "шт";
                } else ostatok = "закончился";

                Params();
                Cena_for_DB();
                if (brends.equals(mSettings_subBrends)) {
                    if (id_st.equals(sub_brends)) {
                        if (image != null) {
                            Log.e("Loading1", "name= " + name);
                            product_str.add(new ListAdapterSimple_Suncape_Forma(koduid, kod_univ, name, kolbox, cena, Cena_Nal, strih, ostatok, image, name_sklad, uid_sklad));
                            cursor.moveToNext();
                        } else {
                            Log.e("Loading2", "name= " + name);
                            // Log.e("Sub=", p_group + ", " + sub_brends);
                            product_str.add(new ListAdapterSimple_Suncape_Forma(koduid, kod_univ, name, kolbox, cena, Cena_Nal, strih, ostatok, "no_image", name_sklad, uid_sklad));
                            cursor.moveToNext();
                        }
                    } else cursor.moveToNext();
                } else cursor.moveToNext();
            }
            cursor.close();
            db.close();

        }  // Синхронизация файлов для всех складов

    }*/
