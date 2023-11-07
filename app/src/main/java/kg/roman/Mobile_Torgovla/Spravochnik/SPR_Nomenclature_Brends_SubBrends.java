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

import com.google.android.material.navigation.NavigationView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Suncape;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Suncape;
import kg.roman.Mobile_Torgovla.Permission.PrefActivity_Nomeclatura;
import kg.roman.Mobile_Torgovla.R;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class SPR_Nomenclature_Brends_SubBrends extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ArrayList<ListAdapterSimple_Suncape> product_str = new ArrayList<ListAdapterSimple_Suncape>();
    public ListAdapterAde_Suncape adapterPriceClients;

    public Context context_Activity;
    public NavigationView navigationView;
    public Menu menu;
    public Toolbar toolbar;
    public MenuItem nav_menuItem;
    public String[] mass_menu_priority, mass_menu_product;
    public String PEREM_SELECT_BRENDS;
    public View navHeader;
    public ImageView imageView_header;
    public TextView textView_header1, textView_header2;
    public LinearLayout linearLayout_header;
    public String table_name, name_group;
    public ListView listView;
    public String Cena_All, Cena_ST, Cena_TY, Cena_STTY, Cena_Nal, Cena_Kons, cena;
    public Double TY, TY_Kons, TY_PR, CenaDouble, CenaDouble1;
    public Double Doub_Cena, Doub_Cena_ST, Doub_Cena_TY, Doub_Cena_STTY, Doub_Kons, Doub_Nal;
    public String Name_new, SName_new;
    public SubMenu topChannelMenu;
    public String ostatok;
    public String Cena_For;

    public SharedPreferences.Editor ed;
    public SharedPreferences sp;
    public String PEREM_FTP_SERV, PEREM_FTP_LOGIN, PEREM_FTP_PASS, PEREM_FTP_DISTR_XML, PEREM_FTP_DISTR_db3,
            PEREM_IMAGE_PUT_SDCARD, PEREM_IMAGE_PUT_PHONE;
    public String PEREM_MAIL_LOGIN, PEREM_MAIL_PASS, PEREM_MAIL_START, PEREM_MAIL_END,
            PEREM_DB3_CONST, PEREM_DB3_BASE, PEREM_DB3_RN, PEREM_ANDROID_ID_ADMIN, PEREM_ANDROID_ID;
    public String PEREM_AG_UID, PEREM_AG_NAME, PEREM_AG_REGION, PEREM_AG_UID_REGION, PEREM_AG_CENA,
            PEREM_AG_SKLAD, PEREM_AG_UID_SKLAD, PEREM_AG_TYPE_REAL, PEREM_AG_TYPE_USER,
            PEREM_WORK_DISTR, PEREM_KOD_MOBILE;
    public String Save_dialog_up_;
    public Boolean perem_switch_group_sql, perem_switch_full_tovar, subrends_type = false;
    public String[] const_subbrends;
    public String TAG = "";
    public String query_mass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Bella); // (for Custom theme)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt_spr_nomenclatura);
        context_Activity = SPR_Nomenclature_Brends_SubBrends.this;
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        PEREM_SELECT_BRENDS = sp.getString("PEREM_SELECT_BRENDS", "0");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.ListView_Klients);
        //  Log.e("Class_Name: ", "|" + this.getLocalClassName());  // имя используемого класса
        TAG = this.getLocalClassName();
       /* getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.user_pda);*/
        getSupportActionBar().setTitle(PEREM_SELECT_BRENDS);
        getSupportActionBar().setSubtitle("");
        Constanta_Read();
        Log.e("Class_Name: ", "|" + this.getLocalClassName());  // имя используемого класса

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_nomenclature);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Toast.makeText(context_Activity, "Группа: " + sp_BREND, Toast.LENGTH_LONG).show();
        navigationView = (NavigationView) findViewById(R.id.nav_view_nomenclature);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.menu_nomeclature_brends);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        Write_Mass_Subbrends();
        Navigation_Menu();
        Navigation_Title_Image();

        Log.e(TAG, "_" + perem_switch_full_tovar.booleanValue());
        Log.e(TAG, "_" + perem_switch_group_sql.booleanValue());
    }

    @Override
    public void onBackPressed() {
        // finish();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //id_st = item.getTitle().toString();
        try {
            getSupportActionBar().setSubtitle(item.getTitle().toString());
            product_str.clear();
            Loading_Db_Nomencalture(item.getTitle().toString());
            adapterPriceClients = new ListAdapterAde_Suncape(context_Activity, product_str);
            adapterPriceClients.notifyDataSetChanged();
            listView.setAdapter(adapterPriceClients);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_nomenclature);
            drawer.closeDrawer(GravityCompat.START);
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка номенклатуры: subBrends", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "ERROR_ Ошибка номенклатуры: subBrends");
        }

        return true;
    }

    // Программное создание меню
    protected void Navigation_Menu() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        if (subrends_type.booleanValue() == true)
        {
            String st="";
            // /Ц0004659,Ц0004655,Ц0004710/
            for (int i=0; i<const_subbrends.length; i++)
            {
                st = st+"'"+const_subbrends[i]+"', ";
            }
            Log.e(TAG, "Navigation_Menu: выборка"  +st.substring(0, st.length()-2));
            query_mass = "SELECT base_in_brends_id.brends, base_in_brends_id.kod, base_in_brends_sub_id.subbrends, base_in_brends_sub_id.kod AS 's_kod', base_in_brends_sub_id.parent_kod \n" +
                    "FROM base_in_brends_id\n" +
                    "LEFT JOIN base_in_brends_sub_id ON base_in_brends_id.kod = base_in_brends_sub_id.parent_kod\n" +
                    "WHERE s_kod NOT IN ("+st.substring(0, st.length()-2)+");";
            Log.e(TAG, "Navigation_Menu: выборка" );
            Log.e(TAG, "Navigation_Menu:"+const_subbrends );
        }
        else
        {
            query_mass = "SELECT base_in_brends_id.brends, base_in_brends_id.kod, base_in_brends_sub_id.subbrends, base_in_brends_sub_id.kod AS 's_kod', base_in_brends_sub_id.parent_kod \n" +
                    "FROM base_in_brends_id\n" +
                    "LEFT JOIN base_in_brends_sub_id ON base_in_brends_id.kod = base_in_brends_sub_id.parent_kod;";
            Log.e(TAG, "Navigation_Menu: ВСЕ" );
        }

        final Cursor cursor = db.rawQuery(query_mass, null);
        cursor.moveToFirst();
        Menu m = navigationView.getMenu();
        m.clear();
        topChannelMenu = m.addSubMenu(PEREM_SELECT_BRENDS.toUpperCase());
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("brends")); // код клиента
            String s_name = cursor.getString(cursor.getColumnIndexOrThrow("subbrends")); // код клиента
            String kod_subbrends = cursor.getString(cursor.getColumnIndexOrThrow("s_kod")); // код клиента
            Name_new = name.substring(0, 1) + name.substring(1).toLowerCase();
            SName_new = s_name.substring(0, 1) + s_name.substring(1).toLowerCase();
            /* Log.e(TAG, Name_new);
             Log.e(TAG,  SName_new);*/
            if ((Name_new).equals(PEREM_SELECT_BRENDS)) {
                Image_Icon_Brends_Switch(SName_new);
            }
            cursor.moveToNext();
        }
        cursor.close();
        db.close();


    }


    /*                //   topChannelMenu.add(SName_new).setIcon(R.drawable.ic_cost);
                if (subrends_type.booleanValue() == true) {
                    for (int i = 0; i < const_subbrends.length; i++) {
                        if (!kod_subbrends.equals(const_subbrends[i])) {
                            Log.e(TAG, "W_FALSE");
                            Image_Icon_Brends_Switch(SName_new);
                        }
                    }
                } else {
                    // Если subrends == "ALL"
                    Image_Icon_Brends_Switch(SName_new);
                }*/
    protected void Write_Mass_Subbrends() {
        if (!sp.getBoolean("switch_preference_sunbell_all_brends", TRUE))
        {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
            String query = "SELECT * FROM const_agents_brends " +
                    "WHERE uid_name == '" + PEREM_AG_UID + "';";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            String user_subbrends = cursor.getString(cursor.getColumnIndexOrThrow("user_subbrends"));  //
            if (user_subbrends.equals("/ALL/")) {
                subrends_type = false; // Загрузка полной номенклатуры
            } else {
                subrends_type = true; // Загрузка номенклатуры за минусом выбранных подбрендов
                Log.e(TAG, "SELECT+" + user_subbrends.replaceAll(",", "").replaceAll("/", "").length() / 8);
                int kol_m = user_subbrends.replaceAll(",", "").replaceAll("/", "").length() / 8;
                const_subbrends = new String[kol_m];
                const_subbrends = user_subbrends.replaceAll("/", "").split("[,]+");
                for (int i = 0; i < const_subbrends.length; i++) {
                    Log.e(TAG, "SELECT+" + const_subbrends[i]);
                }
            }
            cursor.close();
            db.close();
        }
        else subrends_type = false;

    }


    protected void Select_Const_Subbrends(String w_uid_name, int w_mass, String w_user_sb) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
        String query = "SELECT * FROM const_agents_brends\n" +
                "WHERE uid_name == '" + w_uid_name + "'";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        const_subbrends = new String[w_mass];
        while (cursor.isAfterLast() == false) {
            String user_subbrends = cursor.getString(cursor.getColumnIndexOrThrow("user_subbrends"));  //
            if (!user_subbrends.equals("/ALL/")) {
            }

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    // Загрузка визуального оформления для меню по брендам
    protected void Navigation_Title_Image() {
        navHeader = navigationView.getHeaderView(0);
        imageView_header = (ImageView) navHeader.findViewById(R.id.nav_header_nomenclature);
        textView_header1 = (TextView) navHeader.findViewById(R.id.textView_title_1);
        textView_header2 = (TextView) navHeader.findViewById(R.id.textView_title_2);
        linearLayout_header = (LinearLayout) navHeader.findViewById(R.id.lineary_header);


        switch (PEREM_SELECT_BRENDS) {
            // TRADEGOF
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
            case "Viero (тиссью груп)": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.viero_colorPrimary));
                getSupportActionBar().setIcon(R.drawable.icon_menu_viero_2);
                imageView_header.setImageResource(R.drawable.icon_menu_viero_2);
                textView_header1.setText(R.string.header_viero_title_1);
                textView_header2.setText(R.string.header_viero_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_bella));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_viero));
            }
            break;
            case "Yokosun": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.yokosun_colorPrimary));
                getSupportActionBar().setIcon(R.drawable.icon_menu_yokosun);
                imageView_header.setImageResource(R.drawable.icon_menu_yokosun);
                textView_header1.setText(R.string.header_yokosun_title_1);
                textView_header2.setText(R.string.header_yokosun_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_bella));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_yokosun));
            }
            break;
            case "President": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.president_colorPrimary));
                getSupportActionBar().setIcon(R.drawable.icon_menu_president);
                imageView_header.setImageResource(R.drawable.icon_menu_president);
                textView_header1.setText(R.string.header_president_title_1);
                textView_header2.setText(R.string.header_president_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_bella));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_president));
            }
            break;

            // SUNBELL
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
            case "Ооо \"биг\"": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.big_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_big);
                imageView_header.setImageResource(R.mipmap.logo_big);
                textView_header1.setText(R.string.header_big_title_1);
                textView_header2.setText(R.string.header_big_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_big));
            }
            break;
            case "Ооо \"наука, техника, медицина\"": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.ntm_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_ntm);
                imageView_header.setImageResource(R.mipmap.logo_ntm);
                textView_header1.setText(R.string.header_nauka_title_1);
                textView_header2.setText(R.string.header_nauka_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_ntm));
            }
            break;
            case "Ontex tuketim urunleri": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.ontex_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_ontex);
                imageView_header.setImageResource(R.mipmap.logo_ontex);
                textView_header1.setText(R.string.header_ontex_title_1);
                textView_header2.setText(R.string.header_ontex_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_ontex));
            }
            break;
            case "Halk hijyenik urunler": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.halk_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_halk);
                imageView_header.setImageResource(R.mipmap.logo_halk);
                textView_header1.setText(R.string.header_halk_title_1);
                textView_header2.setText(R.string.header_halk_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_halk));
            }
            break;
            case "Ооо \"коттон клаб\"": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.cotton_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_cotton);
                imageView_header.setImageResource(R.mipmap.logo_cotton);
                textView_header1.setText(R.string.header_cottonclub_title_1);
                textView_header2.setText(R.string.header_cottonclub_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_cotton));
            }
            break;
            case "Ооо \"пк \"уфа пак\"": {
                toolbar.setBackgroundColor(getResources().getColor(R.color.ufa_colorPrimary));
                getSupportActionBar().setIcon(R.mipmap.logo_ufa);
                imageView_header.setImageResource(R.mipmap.logo_ufa);
                textView_header1.setText(R.string.header_ufapack_title_1);
                textView_header2.setText(R.string.header_ufapack_title_2);
                // для API 21>
                //linearLayout__header.setBackground(getDrawable(R.drawable.side_nav_bar_dramers));
                // для API <21
                linearLayout_header.setBackground(getResources().getDrawable(R.drawable.side_nav_bar_ufapack));
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

    // Загрузка номенклатуры по брендам и по группам(Сортировка)
    protected void Loading_Db_Nomencalture(String subBrends) {
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        perem_switch_group_sql = sp.getBoolean("switch_preference_group_sql", FALSE);            //
        perem_switch_full_tovar = sp.getBoolean("switch_preference_full_tovar", FALSE);          //
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
            String query;
            //  "WHERE count > 0\n" +
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

                String brends = brend.substring(0, 1) + brend.substring(1).toLowerCase();
                String sub_brends = p_group.substring(0, 1) + p_group.substring(1).toLowerCase();

                if (perem_switch_full_tovar)  // только то что в наличии на складе
                {
                    if (count != (null)) {
                        ostatok = count + "шт";
                        Params();
                        Cena_for_DB();
                        if (brends.equals(PEREM_SELECT_BRENDS)) {
                            if (subBrends.equals(sub_brends)) {
                                if (image != null) {
                                    product_str.add(new ListAdapterSimple_Suncape(kod_univ, name, kolbox, cena, Cena_Nal, strih, ostatok, image));
                                    cursor.moveToNext();
                                } else {
                                    product_str.add(new ListAdapterSimple_Suncape(kod_univ, name, kolbox, cena, Cena_Nal, strih, ostatok, "no_image"));
                                    cursor.moveToNext();
                                }
                            } else cursor.moveToNext();
                        } else cursor.moveToNext();
                    } else cursor.moveToNext();
                } else  // полная номенклатура
                {
                    if (count != (null)) {
                        ostatok = count + "шт";
                    } else ostatok = "Закончился";
                    Params();
                    Cena_for_DB();
                    if (brends.equals(PEREM_SELECT_BRENDS)) {
                        if (subBrends.equals(sub_brends)) {
                            if (image != null) {
                                product_str.add(new ListAdapterSimple_Suncape(kod_univ, name, kolbox, cena, Cena_Nal, strih, ostatok, image));
                                cursor.moveToNext();
                            } else {
                                product_str.add(new ListAdapterSimple_Suncape(kod_univ, name, kolbox, cena, Cena_Nal, strih, ostatok, "no_image"));
                                cursor.moveToNext();
                            }
                        } else cursor.moveToNext();
                    } else cursor.moveToNext();
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка номенклатуры: загрузка данных!", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "ERROR, Ошибка номенклатуры: загрузка данных!");
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
            Log.e(TAG, "ERROR, Ошибка ценообразования!");
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

        perem_switch_group_sql = sp.getBoolean("switch_preference_group_sql", FALSE);             //чтение данных: координаты gps
        perem_switch_full_tovar = sp.getBoolean("switch_preference_full_tovar", FALSE);             //чтение данных: координаты gps

        for (int i = 0; i < getResources().getStringArray(R.array.mass_for_update_data).length; i++) {
            Save_dialog_up_ = sp.getString("Save_dialog_up_" + i, "0");
        }
    }

    //  Заполнение категорий по префиксу именем и картинкой
    protected void Image_Icon_Brends_Switch(String w_name) {

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT * FROM base_in_brends_id;";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String brends = cursor.getString(cursor.getColumnIndexOrThrow("brends"));        // столбец
            String prefics = cursor.getString(cursor.getColumnIndexOrThrow("prefic"));     // столбец
            if (PEREM_SELECT_BRENDS.toLowerCase().equals(brends.toLowerCase())) {
                switch (prefics) {
                    //////////// SUNBELL
                    case "cs":  // Cussons
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_pz_cussons);
                        break;
                    case "hu":  // HALK HIJYENIK URUNLER
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_nice_lady);
                        break;
                    case "lc":  // Lacalute
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_lacalut);
                        break;
                    case "ot":  // Ontex
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_ontex);
                        break;
                    case "pc":  // PCC
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_pcc);
                        break;
                    case "vk":  // Villa_krim
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_villa_krim);
                        break;
                    case "dr":  // Драмерс
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_brait);
                        break;
                    case "kd":  // Kodak
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_kodak);
                        break;
                    case "bg":  // БиГ
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_big);
                        break;
                    case "nt":  // Наука и техника
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_ntm);
                        break;
                    case "ie":  // Палл инвест
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_polinvest);
                        break;
                    case "fn":  // Фараон
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_pharaon);
                        break;
                    case "tg":  // ТОРГХИМ
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_njk);
                        break;
                    case "ns":  // Первое решение
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_ns);
                        break;
                    case "pf":  // ПКФ СОНЦА
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_pkf_conca);
                        break;
                    case "sv":  // Свобода
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_svoboda);
                        break;
                    case "ps":  // PLUSHE
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_plushe);
                        break;
                    case "sm":  // SAMARELA
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_samarela);
                        break;
                    case "sp":  // SPLAT
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_splat);
                        break;
                    case "zn":  // ZONIN
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_zonin);
                        break;
                    case "sr":  // ОсОО "ЭРГОПАК"
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_sarantis);
                        break;
                    case "tr":  // ТЫМЛАТСКИЙ РЫБОКОМБИНАТ
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_t_fish);
                        break;
                    case "sh":  // Шенли
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_secret_lan);
                        break;
                    case "ct":  // CottonClub
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_cotton_club);
                        break;
                    case "yp":  // УФАПАКС
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_ufapack);
                        break;

                    //////////// TRADEGOF
                    case "bl":  // Bella
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_bella);
                        break;
                    case "pd":  // President
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_president);
                        break;
                    case "ys":  // Yokosun
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_yokosun);
                        break;
                    case "vr":  // Viero
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_viero_2);
                        break;
                    case "dc":  // ДНС
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_dnc);
                        break;

                    default:
                        topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.ic_cost);
                        break;
                }
            }
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

    /*    String query_sub = "SELECT base_in_brends_id.brends, base_in_brends_id.prefic, base_in_brends_sub_id.subbrends, base_in_brends_sub_id.kod FROM base_in_brends_id\n" +
                "LEFT JOIN base_in_brends_sub_id ON base_in_brends_id.kod = base_in_brends_sub_id.parent_kod\n" +
                "WHERE base_in_brends_id.prefic == '" + prefics_select + "';";
        final Cursor cursor_sub = db.rawQuery(query_sub, null);
        cursor_sub.moveToFirst();
        while (cursor_sub.isAfterLast() == false) {
            String subbrends = cursor_sub.getString(cursor_sub.getColumnIndexOrThrow("subbrends"));     // столбец
            String kod = cursor_sub.getString(cursor_sub.getColumnIndexOrThrow("kod"));     // столбец
            Log.e("TAG", subbrends+"_"+kod );
            switch (kod) {
                //////////// SUNBELL
                case "Ц0000013":  topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_luksja);  break;
                case "00000307":  topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_luksja);  break;
                case "Ц0000012":  topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_luksja);  break;
                case "00000299":  topChannelMenu.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.icon_menu_mf);  break;
            }
            cursor_sub.moveToNext();
        }
        cursor_sub.close();*/

    }
}









/*Doub_Cena = Double.parseDouble(cena);
        String Format1 = new DecimalFormat("#00.00").format(Doub_Cena);
        Cena_All = Format1;

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
        }*/