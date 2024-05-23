package kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import kg.roman.Mobile_Torgovla.FormaZakazaStart.WJ_Forma_Zakaza;
import kg.roman.Mobile_Torgovla.FormaZakaza_EndZakaz.WJ_Forma_Zakaza_Dop_Data;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Spinner_TY;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_WJ_Zakaz;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Excel;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Spinner_TY;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_WJ_Zakaz;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.PreferencesWrite;
import kg.roman.Mobile_Torgovla.R;

public class WJ_Forma_Zakaza_L2_OLD extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public ArrayList<ListAdapterSimple_WJ_Zakaz> list_zakaz = new ArrayList<ListAdapterSimple_WJ_Zakaz>();
    public ListAdapterAde_WJ_Zakaz adapterPriceClients;

    public ArrayList<ListAdapterSimple_Spinner_TY> spinner_ty = new ArrayList<ListAdapterSimple_Spinner_TY>();
    public ListAdapterAde_Spinner_TY adapterPriceClients_ty;

    public org.apache.poi.sl.draw.geom.Context context;
    public org.apache.poi.sl.draw.geom.Context ctx;

    public ArrayList<ListAdapterSimple_Excel> excell = new ArrayList();
    public File fileName;
    public FileOutputStream fos;
    public File sdPath;
    public OutputStream out;
    public String name_put, new_kod_rn;

    public Context context_Activity;

    public TextView textView_klients, textView_aut_summa, textView_itog, textView_id, textView_kol;
    public TextView dg_tw_name, dg_tw_koduniv, dg_tw_cena, dg_tw_ostatok, dg_tw_kol, dg_tw_summa, dg_tw_summa_sk, dg_tw_kolbox, dg_tw_kolbox_org;

    private Handler mHandler = new Handler();
    public androidx.appcompat.app.AlertDialog.Builder dialog;
    private static final String TAG = "Forma_Edit";
    public Button btn_down, btn_up;

    public View localView, localView_ty;
    public EditText dg_ed_skidka, dg_ed_editkol;
    public RadioGroup radioGroup_local;
    public RadioButton radioGroup_one, radioGroup_much, radioGroup_edit;
    public NavigationView navigationView;
    public Menu menu;
    public Toolbar toolbar;
    public ListView listView;

    public Integer checked_group, kol_box_info, max_box;
    public Integer perem_int_summa, perem_int_ostatok, perem_int_cena, perem_int_kol, perem_kol_group_one, perem_int_kolbox;
    public Double Aut_Summa, Auto_Summa, Itog_Summa, dg_tw_summa_sk_DOUBLE;
    public String ftp_put, ftp_server, login, password, ID_TOVAR, ID_DELETE_POS;
    public String lst_tw_name, lst_tw_kod, lst_tw_cena, lst_tw_ostatok, lst_tw_kolbox, lst_tw_kol, lst_tw_skidka;
    public String kol_group_one, kol_group_much, SKIDKA_TY, Format_cena_sk;
    //  public String PEREM_Agent, PEREM_KAgent, PEREM_KAgent_UID, PEREM_Vrema, PEREM_Data, PEREM_RNKod, Summa, PEREM_UID, PEREM_Adress;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now;

    public String[][] mass_ty, mass_m;
    public String[] mass_menu_product;
    public Spinner spinner;
    public Boolean pref_checkbox_brends;
    public TextView tvw_sp_skidka, tvw_sp_summa, tvw_d_summa, tvw_d_itogo, tvw_d_error;
    public String d_db_summa, kod_int, summa_new_rn = "00.00", itogo_new_rn = "00.00";
    public String button_click_TY = "false";

    public Button button_ok, button_up, button_Go, button_cancel, button_down, position_delete;
    public Button button_add, button_cena_ty, button_discount, button_scan, button_refresh, button_summa, button_delete;
    public Boolean pref_params_1, pref_params_2;

    public SharedPreferences sp;
    public SharedPreferences.Editor ed;
    public String perem_rewrite_rn, peremen_query, Log_Text_Error, PEREM_K_AG_Data_WORK;
    public String PEREM_K_AG_NAME, PEREM_K_AG_UID, PEREM_K_AG_ADRESS, PEREM_K_AG_KodRN, PEREM_K_AG_Data, PEREM_K_AG_Vrema, PEREM_K_AG_GPS;
    public String PEREM_FTP_SERV, PEREM_FTP_LOGIN, PEREM_FTP_PASS, PEREM_FTP_DISTR_XML, PEREM_FTP_DISTR_db3,
            PEREM_IMAGE_PUT_SDCARD, PEREM_IMAGE_PUT_PHONE;
    public String PEREM_MAIL_LOGIN, PEREM_MAIL_PASS, PEREM_MAIL_START, PEREM_MAIL_END,
            PEREM_DB3_CONST, PEREM_DB3_BASE, PEREM_DB3_RN, PEREM_ANDROID_ID_ADMIN, PEREM_ANDROID_ID;
    public String PEREM_AG_UID, PEREM_AG_NAME, PEREM_AG_REGION, PEREM_AG_UID_REGION, PEREM_AG_CENA,
            PEREM_AG_SKLAD, PEREM_AG_UID_SKLAD, PEREM_AG_TYPE_REAL, PEREM_AG_TYPE_USER, PEREM_KOD_BRENDS_VISIBLE,
            PEREM_WORK_DISTR, PEREM_KOD_MOBILE, PEREM_KOD_UID_KODRN, PEREM_DOP_CREDITE_DATE, PEREM_DOP_COMMENT, PEREM_NEW_DEBET_WRITE;
    public String PEREM_KLIENT_UID, PEREM_DIALOG_UID, PEREM_DIALOG_DATA_START, PEREM_DIALOG_DATA_END, PEREM_DISPLAY_START, PEREM_DISPLAY_END;
    public String PEREM_DOP_DATA_UP, PEREM_DOP_NDS, PEREM_DOP_SKIDKA, PEREM_DOP_CREDIT, PEREM_DOP_SUMMA, PEREM_DOP_ITOGO, PEREM_CLICK_TY = "false";
    public SubMenu topChannelMenu1, topChannelMenu2, topChannelMenu3, topChannelMenu4, topChannelMenu5, topChannelMenu6, topChannelMenu_01, topChannelMenu_02;


    public SubMenu[] subMenus;

    //////////////// 02.2024
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    SharedPreferences mSettings;
    String logeTAG = "WJ_FZ_L2";
    public SubMenu topChannelMenu_All;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt_wj_activity_forma_start);
        context_Activity = WJ_Forma_Zakaza_L2_OLD.this;
        Log_Text_Error = "ERR_WJ_F_Z_L2: ";

        Log.e(Log_Text_Error, "запускается меню товара");
        Log.e("Create", "Start onCreate");
        // Константы для чтения
        Constanta_Read();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(PEREM_K_AG_NAME);
        getSupportActionBar().setSubtitle(PEREM_K_AG_KodRN + "_" + PEREM_K_AG_Data + "_" + PEREM_K_AG_Vrema);

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
        Navigation_Menu();

        ReWrite_KodRN(); // Проверка номера накладной
        // Обработка кнопочной панели
        Button_Group();


        textView_aut_summa = (TextView) findViewById(R.id.textView_this_summa);
        textView_itog = (TextView) findViewById(R.id.textView_itog);
        textView_aut_summa.setText("");
        textView_itog.setText("");

        listView = (ListView) findViewById(R.id.ListView_Klients_Tovar);


        Log.e("LOGError", "ожидание");
        try {
            Log.e("LOGError", "запуск потока данных");
            list_zakaz.clear();
            Loading_Adapter();
            Log.e("LOGError", "отображение в ListView");
            adapterPriceClients = new ListAdapterAde_WJ_Zakaz(context_Activity, list_zakaz);
            adapterPriceClients.notifyDataSetChanged();
            listView.setAdapter(adapterPriceClients);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("LOGError", "Ошибка загрузки данных в ListView");
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Заполнение из карточик товара
                lst_tw_name = ((TextView) view.findViewById(R.id.Content_TovarUID)).getText().toString();
                lst_tw_kod = ((TextView) view.findViewById(R.id.wj_cont_kod)).getText().toString();
                lst_tw_kol = ((TextView) view.findViewById(R.id.Content_Count)).getText().toString();
                // lst_tw_skidka = ((TextView) view.findViewById(R.id.cont_edit_kol)).getText().toString();

                SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
              /*  String query = "SELECT base10_nomeclature.name, base10_nomeclature.brends, base10_nomeclature.p_group, " +
                        "base10_nomeclature.kod, SUM(base4_ost.count) AS 'count', base5_price.cena, " +
                        "base4_ost.sklad, base10_nomeclature.image, base10_nomeclature.kolbox, base10_nomeclature.strih, " +
                        "base10_nomeclature.kod_univ, base10_nomeclature.work, base10_nomeclature.koduid\n" +
                        "FROM base10_nomeclature\n" +
                        "LEFT JOIN base4_ost ON base10_nomeclature.koduid = base4_ost.koduid \n" +
                        "LEFT JOIN base5_price ON base10_nomeclature.koduid = base5_price.koduid\n" +
                        "WHERE base4_ost.sklad !='EA661E8F-90D1-48BF-A530-5998FB65BFDD'\n" +
                        "GROUP BY base10_nomeclature.name\n" +
                        "ORDER BY base10_nomeclature.brends, base10_nomeclature.p_group;";*/
                String query = "SELECT base_in_nomeclature.name, base_in_nomeclature.brends, " +
                        "base_in_nomeclature.p_group, base_in_nomeclature.kod, base_in_image.kod_image, " +
                        "base_in_nomeclature.kolbox, SUM(base_in_ostatok.count) AS 'count', base_in_price.price, " +
                        "base_in_nomeclature.strih, base_in_nomeclature.kod_univ, base_in_nomeclature.koduid\n" +
                        "FROM base_in_nomeclature\n" +
                        "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                        "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                        "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                        "GROUP BY base_in_nomeclature.name\n" +
                        "ORDER BY base_in_nomeclature.brends, base_in_nomeclature.p_group;";
                final Cursor cursor = db.rawQuery(query, null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String kolbox = cursor.getString(cursor.getColumnIndexOrThrow("kolbox"));
                    String cena = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                    String shtrih = cursor.getString(cursor.getColumnIndexOrThrow("strih"));
                    String kod_univ = cursor.getString(cursor.getColumnIndexOrThrow("kod_univ"));
                    String koduid = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));
                    String ostatok = cursor.getString(cursor.getColumnIndexOrThrow("count"));
                    try {
                       /* if (pref_params_2 == false) {

                        } else {
                            lst_tw_kolbox = kolbox;
                            lst_tw_cena = cena;
                            lst_tw_ostatok = "999999";
                        }*/
                        if (lst_tw_kod.equals(kod_univ)) {
                            lst_tw_kolbox = kolbox;
                            lst_tw_cena = cena;
                            lst_tw_ostatok = ostatok;
                            Loading_Rename_list(lst_tw_name, lst_tw_kod, lst_tw_kolbox, lst_tw_cena, lst_tw_ostatok, lst_tw_kol);
                        }

                    } catch (Exception e) {
                        Log.e("WJ_FormaLS2:", "Ошибка заполнения карточки!");
                        Toast.makeText(context_Activity, "Ошибка заполнения карточки!", Toast.LENGTH_SHORT).show();

                    }
                    cursor.moveToNext();
                }
                cursor.close();
                db.close();
            }
        });
    }

    @Override
    public void onResume() {
        Log.e("Resume", "Start onResume");
        Update_New_TY_Listview(PEREM_DOP_SKIDKA, PEREM_DOP_SUMMA, PEREM_DOP_ITOGO);
        list_zakaz.clear();
        Loading_Adapter();
        adapterPriceClients = new ListAdapterAde_WJ_Zakaz(context_Activity, list_zakaz);
        adapterPriceClients.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients);
        super.onResume();
    }


    /////// 02.2024

    ///// создание запроса Query для обращения к табл
    protected String QueryForMenu() {

        return "SELECT base_in_brends_id.brends, base_in_brends_id.kod, " +
                "base_in_brends_id.prefic, " +
                "COALESCE(write_group_name.group_id, 'new') AS 'group_id' , \n" +
                "COALESCE(write_group_name.group_name, 'новый бренд') AS 'group_name', " +
                "COALESCE(write_group_name.group_parent, 'no brends')\n" +
                "FROM base_in_brends_id\n" +
                "LEFT JOIN write_group_brends ON base_in_brends_id.kod = write_group_brends.brends_kod\n" +
                "LEFT JOIN write_group_name ON write_group_brends.brends_group = write_group_name.group_id\n" +
                "ORDER BY write_group_name.group_id, base_in_brends_id.brends;";
    }


    /// Функция: список фирм для номенклатуры
    protected TreeMap<String, String> FirmsList() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT * FROM write_group_name ORDER BY group_id;";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        TreeMap<String, String> group_isSet = new TreeMap<>();
        while (!cursor.isAfterLast()) {
            String group_id = cursor.getString(cursor.getColumnIndexOrThrow("group_id"));
            String group_name = cursor.getString(cursor.getColumnIndexOrThrow("group_name"));
            // Log.e("FirmsList", group_id + "__" + group_name);
            group_isSet.put(group_id, group_name);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        return group_isSet;
    }


    ////// Работа с боковым меню
    protected void Navigation_Menu() {
        Menu m = navigationView.getMenu();
        m.clear();
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        boolean AllBrends = mSettings.getBoolean("key_SwitchPreference_BrendsAll", false);        // Все для всех

        boolean AgentsBrends = mSettings.getBoolean("key_SwitchPreference_BrendsAgents", false);  // Сортировка для агентов
        TreeSet<String> brendsAgents = new TreeSet<>(mSettings.getStringSet("PEREM_BrendsForNomeclature", new TreeSet<>()));

        boolean HandsBrends = mSettings.getBoolean("key_SwitchPreference_BrendsManual", false);   // Ручной отбор
        TreeSet<String> brendsManula = new TreeSet<>(mSettings.getStringSet("key_MultiSelectPreference_BrendsManual", new TreeSet<>()));

        boolean FirmsGroups = mSettings.getBoolean("key_SwitchPreference_BrendsFirms", false);   // Отображение по фирмам


        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        final Cursor cursor = db.rawQuery(QueryForMenu(), null);
        SubMenu[] subMenusFirms = new SubMenu[10];

        ////////////  Для настроек все для всех
        if (AllBrends) {
            try {
                Log.e(logeTAG, "структура все для всех!");
                if (FirmsGroups) {
                    Log.e(logeTAG, "по фирмам");
                    int k = 0;
                    for (TreeMap.Entry<String, String> groupID : FirmsList().entrySet()) {
                        cursor.moveToFirst();
                        subMenusFirms[k] = m.addSubMenu(groupID.getValue());
                        while (!cursor.isAfterLast()) {
                            String name = cursor.getString(cursor.getColumnIndexOrThrow("brends"));
                            String pref = cursor.getString(cursor.getColumnIndexOrThrow("prefic"));
                            String group_id = cursor.getString(cursor.getColumnIndexOrThrow("group_id"));
                            if (groupID.getKey().equals(group_id))
                                subMenusFirms[k].add(name.charAt(0) + name.substring(1).toLowerCase()).
                                        setIcon(IconsForBrends(pref));
                            cursor.moveToNext();
                        }
                        k++;
                    }
                    cursor.close();
                } else {
                    ////// Общее меня номенклатуры
                    subMenusFirms[0] = m.addSubMenu("Номенклатура");
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        String name = cursor.getString(cursor.getColumnIndexOrThrow("brends"));        // столбец с брендом
                        String pref = cursor.getString(cursor.getColumnIndexOrThrow("prefic"));        // столбец с префиксом группы
                        subMenusFirms[0].add(name.charAt(0) + name.substring(1).toLowerCase()).
                                setIcon(IconsForBrends(pref));
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
            } catch (Exception e) {
                Log.e(logeTAG, "ошибка структуры все для всех");
            }


        }
        ////////////

        ////////////  Для настроек: для агентов
        if (AgentsBrends) {
            try {
                Log.e(logeTAG, "структура для агентов");
                if (FirmsGroups) {
                    int k = 0;
                    for (TreeMap.Entry<String, String> groupID : FirmsList().entrySet()) {
                        Log.e("FirmsList", groupID.getValue());
                        cursor.moveToFirst();
                        subMenusFirms[k] = m.addSubMenu(groupID.getValue());
                        while (!cursor.isAfterLast()) {
                            String name = cursor.getString(cursor.getColumnIndexOrThrow("brends"));
                            String pref = cursor.getString(cursor.getColumnIndexOrThrow("prefic"));
                            String group_id = cursor.getString(cursor.getColumnIndexOrThrow("group_id"));
                            if (groupID.getKey().equals(group_id))
                                for (String prefPerm : brendsAgents)
                                    if (prefPerm.equalsIgnoreCase(pref))
                                        subMenusFirms[k].add(name.charAt(0) + name.substring(1).toLowerCase()).
                                                setIcon(IconsForBrends(pref));
                            cursor.moveToNext();
                        }
                        k++;
                    }
                    cursor.close();

                } else {
                    cursor.moveToFirst();
                    subMenusFirms[0] = m.addSubMenu("Номенклатура");
                    while (!cursor.isAfterLast()) {
                        String name = cursor.getString(cursor.getColumnIndexOrThrow("brends"));        // столбец с брендом
                        String pref = cursor.getString(cursor.getColumnIndexOrThrow("prefic"));        // столбец с префиксом группы
                        for (String prefPerm : brendsAgents)
                            if (pref.equalsIgnoreCase(prefPerm))
                                subMenusFirms[0].add(name.charAt(0) + name.substring(1).toLowerCase()).
                                        setIcon(IconsForBrends(pref));
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
            } catch (Exception e) {
                Log.e(logeTAG, "ошибка структуры для агентов");
            }


        }

        ////////////  Для настроек: ручного отбора
        if (HandsBrends) {
            Log.e(logeTAG, "ручной отбор");
            try {
                if (FirmsGroups) {
                    int k = 0;
                    for (TreeMap.Entry<String, String> groupID : FirmsList().entrySet()) {
                        cursor.moveToFirst();
                        subMenusFirms[k] = m.addSubMenu(groupID.getValue());
                        while (!cursor.isAfterLast()) {
                            String name = cursor.getString(cursor.getColumnIndexOrThrow("brends"));
                            String pref = cursor.getString(cursor.getColumnIndexOrThrow("prefic"));
                            String group_id = cursor.getString(cursor.getColumnIndexOrThrow("group_id"));
                            if (groupID.getKey().equals(group_id))
                                for (String prefPerm : brendsManula)
                                    if (pref.equalsIgnoreCase(prefPerm))
                                        subMenusFirms[k].add(name.charAt(0) + name.substring(1).toLowerCase()).
                                                setIcon(IconsForBrends(pref));

                            cursor.moveToNext();
                        }
                        k++;
                    }
                    cursor.close();


                } else {
                    ////// Общее меня номенклатуры
                    subMenusFirms[0] = m.addSubMenu("Номенклатура");
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        String name = cursor.getString(cursor.getColumnIndexOrThrow("brends"));        // столбец с брендом
                        String pref = cursor.getString(cursor.getColumnIndexOrThrow("prefic"));        // столбец с префиксом группы
                        for (String prefPerm : brendsManula)
                            if (pref.equalsIgnoreCase(prefPerm))
                                subMenusFirms[0].add(name.charAt(0) + name.substring(1).toLowerCase()).
                                        setIcon(IconsForBrends(pref));
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
            } catch (Exception e) {
                Log.e(logeTAG, "ошибка структуры ручного выбора");
            }


        }
        ////////////////

        db.close();
    }

    //////////////////////////////////  02.2024
    //// Поиск картинки для брендов в номенклатуре
    protected Drawable IconsForBrends(String pref) {
        // setIcon(Drawable.createFromPath(getFilesDir().getAbsolutePath()+"/Icons/icons_image_for_icons.png"));
        String filename = "icons_brend_" + pref + ".png";
        File file = new File(getFilesDir().getAbsolutePath() + "/Icons/" + filename);
        if (file.exists())
            return Drawable.createFromPath(file.getPath());
        else {
            Resources res = getResources();
            Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.no_image, null);
            return drawable;
        }
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        dialog = new androidx.appcompat.app.AlertDialog.Builder(context_Activity);
        dialog.setTitle("Вы хотите отменить заказ?");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Delete_RN_BASE();
                Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza.class);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
            }
        });
        dialog.show();
        // super.onBackPressed();
    }

    // Удаление данных: номер накладной из базы
    protected void Delete_RN_BASE() {
        try {
            PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
            String query = "DELETE FROM base_RN WHERE base_RN.Kod_RN LIKE ('%" + PEREM_K_AG_KodRN + "%')";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                cursor.moveToNext();
            }
            Toast.makeText(context_Activity, "Данные " + PEREM_K_AG_KodRN + " удаленны!", Toast.LENGTH_SHORT).show();
            cursor.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка удаления данных!", Toast.LENGTH_SHORT).show();
            Log.e(Log_Text_Error, "Ошибка удаления данных!");
        }


    }

    // Загрузка данных в адаптер для экрана
    protected void Loading_Adapter() {
        try {
            PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
       /* String query = "SELECT base_RN.Agent, base_RN.Kod_RN, base_RN.Klients, base_RN.UID_Klients, " +
                "base_RN.Vrema, base_RN.Data, base_RN.Kod_Univ, base_RN.Name, SUM(base_RN.Kol) AS \"Kol\", " +
                "base_RN.Cena, SUM(base_RN.Summa) AS \"Summa\", base_RN.Skidka, base_RN.Cena_SK, SUM(base_RN.Itogo) AS \"Itogo\"\n" +
                "FROM base_RN WHERE base_RN.Kod_RN LIKE '%" + PEREM_RNKod + "%' GROUP BY base_RN.Kod_Univ";*/

            String query = "SELECT base_RN.Kod_RN, base_RN.Vrema, base_RN.Data, base_RN.Kod_Univ, " +
                    "base_RN.Name, SUM(base_RN.Kol) AS 'Kol', base_RN.Cena, SUM(base_RN.Summa) AS 'Summa', base_RN.Skidka, base_RN.Image,\n" +
                    "base_RN.Cena_SK, SUM(base_RN.Itogo) AS 'Itogo'\n" +
                    "FROM base_RN WHERE base_RN.Kod_RN LIKE '%" + PEREM_K_AG_KodRN + "%' GROUP BY base_RN.Kod_Univ";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            Auto_Summa = 0.0;
            Itog_Summa = 0.0;
            if (cursor.getCount() > 0) {
                while (!cursor.isAfterLast()) {
                    String kod_univ = cursor.getString(cursor.getColumnIndexOrThrow("Kod_Univ"));
                    String koduid = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                    String Count = cursor.getString(cursor.getColumnIndexOrThrow("Kol"));
                    String cena = cursor.getString(cursor.getColumnIndexOrThrow("Cena"));
                    String summa = cursor.getString(cursor.getColumnIndexOrThrow("Summa"));
                    String skidka = cursor.getString(cursor.getColumnIndexOrThrow("Skidka"));
                    String image = cursor.getString(cursor.getColumnIndexOrThrow("Image"));
                    // String cena_sk = (cursor.getString(cursor.getColumnIndex("Cena_SK"))).replace(",", ".");
                    String cena_sk = (cursor.getString(cursor.getColumnIndexOrThrow("Cena_SK")));
                    String itogo = cursor.getString(cursor.getColumnIndexOrThrow("Itogo"));
                    Auto_Summa = Auto_Summa + Double.parseDouble(summa);
                    Itog_Summa = Itog_Summa + Double.parseDouble(itogo);
                    Log.e("ErrorSumma=", String.valueOf(summa));
                    Log.e("ErrorSumma=", String.valueOf(Aut_Summa));
                    Log.e("WJZakL5=", cena_sk);

                    String Format_summa = new DecimalFormat("#00.00").format(Auto_Summa);
                    String Format_itogo = new DecimalFormat("#00.00").format(Itog_Summa);

                    if (Double.parseDouble(cena_sk) > 0 | !cena_sk.equals(null)) {
                        Format_cena_sk = new DecimalFormat("#00.00").format(Double.parseDouble(cena_sk));
                    } else Format_cena_sk = "";

                    textView_aut_summa.setText(Format_summa.replace(",", "."));
                    textView_itog.setText(Format_itogo.replace(",", "."));

                    list_zakaz.add(new ListAdapterSimple_WJ_Zakaz(name, koduid, kod_univ, Count, cena, Format_cena_sk, summa, skidka, itogo, image));
                    cursor.moveToNext();
                }
            } else {
                Auto_Summa = 0.0;
                Itog_Summa = 0.0;
                String Format_summa = new DecimalFormat("#00.00").format(Auto_Summa);
                String Format_itogo = new DecimalFormat("#00.00").format(Itog_Summa);
                textView_aut_summa.setText(Format_summa.replace(",", "."));
                textView_itog.setText(Format_itogo.replace(",", "."));
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка обработка адаптера", Toast.LENGTH_SHORT).show();
            Log.e(Log_Text_Error, "Ошибка обработка адаптера");
        }

    }

    protected void Loading_Adapter_Refresh() {
        list_zakaz.clear();
        Loading_Adapter();
        adapterPriceClients = new ListAdapterAde_WJ_Zakaz(context_Activity, list_zakaz);
        adapterPriceClients.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients);
        Toast.makeText(context_Activity, "Обновленно!", Toast.LENGTH_SHORT).show();
    }

    protected void Loading_Select_Menu(Menu m) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
        String query = "SELECT * FROM const_name_group\n" +
                "GROUP BY group_parent ";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            while (!cursor.isAfterLast()) {
                String group_id = cursor.getString(cursor.getColumnIndexOrThrow("group_id"));
                String group_name = cursor.getString(cursor.getColumnIndexOrThrow("group_name"));
                String group_parent = cursor.getString(cursor.getColumnIndexOrThrow("group_parent"));

                cursor.moveToNext();
            }
        }
        cursor.close();


        db.close();
    }

    /*@Override
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
            Intent intent3 = new Intent(context_Activity, PrefActivity.class);
            startActivity(intent3);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        String id_st = item.getTitle().toString();
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("sp_BREND", id_st);
        Log.e("NextActivity", "SelectBrends= " + id_st);
        ed.apply();
        Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza_L2_Sub.class);
        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_nomenclature);
        drawer.closeDrawer(GravityCompat.START);
        Log.e("NextActivity", "Переход_01");
        return true;
    }


    protected void Loading_Rename_list(final String name, final String kod_univ, final String kolbox, final String cena, final String ostatok, final String kol_vo) {
        lst_tw_name = name;
        lst_tw_kod = kod_univ;

        lst_tw_kolbox = kolbox;
        lst_tw_cena = cena;
        lst_tw_ostatok = ostatok;

        ed = sp.edit();
        ed.putString("PEREM_CLICK_TY", "false");
        ed.commit();

        Log.e("WJ_FormaLS2:", lst_tw_name + ", " + lst_tw_kod + ", " + lst_tw_kolbox + ", " + lst_tw_cena + ", " + lst_tw_ostatok + ", " + kol_vo);
        // Определение данных в диалог
        localView = LayoutInflater.from(context_Activity).inflate(R.layout.mt_dialog_localview_add, null);
        dg_tw_name = (TextView) localView.findViewById(R.id.tvw_text_aks_name);
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
        perem_kol_group_one = Integer.parseInt(kol_vo);

        dg_tw_name.setText(lst_tw_name);
        dg_tw_koduniv.setText(lst_tw_kod);
        perem_int_cena = Integer.parseInt(lst_tw_cena);
        dg_tw_cena.setText(perem_int_cena.toString());
        perem_int_ostatok = Integer.parseInt(lst_tw_ostatok);
        dg_tw_ostatok.setText(perem_int_ostatok.toString());
        perem_int_kol = perem_kol_group_one;
        dg_tw_kol.setText(perem_int_kol.toString());
        if (!lst_tw_kolbox.isEmpty() | lst_tw_kolbox.equals(" ") | lst_tw_kolbox.equals(null)) {
            perem_int_kolbox = Integer.parseInt(lst_tw_kolbox);
        } else perem_int_kolbox = 12;

        dg_tw_kolbox_org.setText(perem_int_kolbox.toString());
        perem_int_summa = perem_int_cena * perem_kol_group_one;
        String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
        dg_tw_summa.setText(Format);

        if (perem_int_kol > 0) {

            Button_Up(perem_int_cena, perem_int_ostatok, perem_int_kolbox);
            Button_Down(perem_int_cena, perem_int_ostatok, perem_int_kolbox);
            Button_Edit(perem_int_cena, perem_int_ostatok, perem_int_kol);
            Button_Edit_Skidka();
            Fun_Messeger_Panel(perem_int_cena, perem_int_ostatok, perem_int_kolbox, perem_kol_group_one);

            Log.e("WJ_FormaLSINT:", Integer.parseInt(cena) + ", " + Integer.parseInt(ostatok) + ", " + Integer.parseInt(kolbox) + ", " + Format);
        } else
            Toast.makeText(context_Activity, "не достаточно товара", Toast.LENGTH_SHORT).show();
    }


    protected void Fun_Messeger_Panel(final Integer cena, final Integer ostatok, final Integer box, final Integer kolvo) {
        Button_Up(perem_int_cena, perem_int_ostatok, perem_int_kolbox);
        Button_Down(perem_int_cena, perem_int_ostatok, perem_int_kolbox);
        Button_Edit(perem_int_cena, perem_int_ostatok, perem_int_kol);
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
                        perem_kol_group_one = kolvo;
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
                        double db = ostatok.doubleValue() / box.doubleValue();
                        max_box = (int) db;

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
                        dg_ed_editkol.setText(kolvo.toString());
                        // dg_ed_editkol.setText("");
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
                try {
                    int kol_text = Integer.parseInt(dg_tw_kol.getText().toString());
                    if (kol_text > 0) {
                        Write_Table_RN(dg_tw_koduniv.getText().toString(), dg_tw_name.getText().toString(),
                                dg_tw_kol.getText().toString(), dg_tw_cena.getText().toString(),
                                dg_tw_summa.getText().toString(), dg_ed_skidka.getText().toString(), dg_tw_summa_sk_DOUBLE);
                    } else {
                        Toast.makeText(context_Activity, "Товара не достаточно на складе", Toast.LENGTH_SHORT).show();
                    }
                    hideInputMethod();
                } catch (Exception e) {
                    Toast.makeText(context_Activity, "Неправильный ввод данных", Toast.LENGTH_SHORT).show();
                }

            }
        }).setNegativeButton(" ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                //  hideInputMethod();
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
                                double db = ostatok / Double.parseDouble(kol_group_much);
                                //Integer it = db.intValue();
                                int it = (int) db;
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

    protected void Button_Edit(final Integer cena, final Integer ostatok, final Integer kolvo) {

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

    protected void Write_Table_RN(final String koduniv, final String name, final String kol, final String cena, final String summa, final String skidka, final Double summaSK) {

        // Calendare_This_Data();
        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db_up = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);

        String query = "SELECT base_RN.Kod_RN, base_RN.Kod_Univ " +
                "FROM base_RN WHERE base_RN.Kod_RN LIKE '%" + PEREM_K_AG_KodRN + "%'";

        Cursor cursor = db_up.rawQuery(query, null);
        ContentValues localContentValuesUP = new ContentValues();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String kod_univ = cursor.getString(cursor.getColumnIndexOrThrow("Kod_Univ"));
            String kod_rn = cursor.getString(cursor.getColumnIndexOrThrow("Kod_RN"));
            Log.e("Update_Ostatok=", kod_univ + ", " + koduniv);
            Log.e("Update_Ostatok=", PEREM_K_AG_KodRN + ", " + kod_rn);

            if (kod_univ.equals(koduniv) & PEREM_K_AG_KodRN.equals(kod_rn)) {
                Log.e("IFELSE=", "Yes" + PEREM_K_AG_KodRN + ", " + kod_rn + kod_univ + ", " + koduniv);
                localContentValuesUP.put("Kod_RN", PEREM_K_AG_KodRN);
                localContentValuesUP.put("Vrema", PEREM_K_AG_Vrema);
                localContentValuesUP.put("Data", PEREM_K_AG_Data);
                localContentValuesUP.put("Kod_Univ", koduniv);
                localContentValuesUP.put("Name", name);
                localContentValuesUP.put("Kol", kol);
                localContentValuesUP.put("Cena", cena);
                localContentValuesUP.put("Summa", summa);
                if (!skidka.isEmpty()) {
                    String Format_summa = new DecimalFormat("#00.00").format(summaSK);
                    localContentValuesUP.put("Skidka", skidka);

                    Double cena_db = Double.valueOf(cena);
                    Double cena_sk_db = Double.valueOf(skidka);
                    String Format = new DecimalFormat("#00.00").format(cena_db - (cena_db * (cena_sk_db / 100)));
                    localContentValuesUP.put("Cena_Sk", Format.replace(",", "."));

                    localContentValuesUP.put("Itogo", Format_summa.replaceAll(",", "."));
                } else {
                    localContentValuesUP.put("Skidka", "0");
                    localContentValuesUP.put("Cena_Sk", "0");
                    localContentValuesUP.put("Itogo", summa);
                }


                String[] arrayOfString = new String[1];
                String[] arrayOfString2 = new String[1];
                arrayOfString[0] = kod_rn;
                arrayOfString2[0] = kod_univ;

                db_up.update("base_RN", localContentValuesUP, "Kod_RN = ? AND Kod_Univ = ?", new String[]{kod_rn, kod_univ});
                //    db_up.update("base_RN", localContentValuesUP, "Kod_RN = ? AND Kod_Univ = ?", arrayOfString);

                cursor.moveToNext();
            } else cursor.moveToNext();

        }

        cursor.close();
        db_up.close();
    }

    private static void createSheetHeader(HSSFSheet paramHSSFSheet, int paramInt, ListAdapterSimple_Excel paramListAdapterSimple_Excel) {
        // Создание столбцов для Excel
        HSSFRow localHSSFRow = paramHSSFSheet.createRow(paramInt);
        localHSSFRow.createCell(0).setCellValue(paramListAdapterSimple_Excel.getKod());
        localHSSFRow.createCell(1).setCellValue(paramListAdapterSimple_Excel.getName());
        localHSSFRow.createCell(2).setCellValue(paramListAdapterSimple_Excel.getCena().doubleValue());
        localHSSFRow.createCell(3).setCellValue(paramListAdapterSimple_Excel.getKol().intValue());
        localHSSFRow.setHeight(Short.valueOf("50"));
    }

    public List<ListAdapterSimple_Excel> fillData() {

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_rn_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base_RN.Kod_RN, base_RN.Kod_Univ, base_RN.Name, base_RN.Cena, base_RN.Kol " +
                "FROM base_RN WHERE base_RN.Kod_RN LIKE '%" + PEREM_K_AG_KodRN + "%'";

        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String kod_rn = cursor.getString(cursor.getColumnIndexOrThrow("Kod_RN"));
            String kod_univ = cursor.getString(cursor.getColumnIndexOrThrow("Kod_Univ"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
            Double cena = cursor.getDouble(cursor.getColumnIndexOrThrow("Cena"));
            Integer kol = cursor.getInt(cursor.getColumnIndexOrThrow("Kol"));
            excell.add(new ListAdapterSimple_Excel(kod_univ, name, cena, kol));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return excell;
    }


    public void Writing_To_Exsel() {
        HSSFWorkbook localHSSFWorkbook = new HSSFWorkbook();
        HSSFSheet localHSSFSheet = localHSSFWorkbook.createSheet("Заказ");
        List localList = fillData();
        int i = 0;
        localHSSFSheet.createRow(0);
        Iterator localIterator = localList.iterator();
        while (localIterator.hasNext()) {
            ListAdapterSimple_Excel localListAdapterSimple_Excel = (ListAdapterSimple_Excel) localIterator.next();
            i++;
            createSheetHeader(localHSSFSheet, i, localListAdapterSimple_Excel);
        }
        this.fos = null;
        if (!Environment.getExternalStorageState().equals("mounted"))
            Toast.makeText(getApplicationContext(), "SD-сущ", Toast.LENGTH_LONG).show();
        this.sdPath = Environment.getExternalStorageDirectory();
        this.sdPath = new File(this.sdPath.getAbsolutePath() + "/Price/" + name_put + "/");

        if (!new File(getSDcardPath()).exists())
            new File(getSDcardPath()).mkdirs();
        this.fileName = new File(this.sdPath, PEREM_K_AG_KodRN + ".xls");
        try {
            this.fos = new FileOutputStream(this.fileName);
            localHSSFWorkbook.write(this.fos);
            localHSSFWorkbook.close();
            Toast.makeText(getApplicationContext(), "Excel файл успешно создан!", Toast.LENGTH_LONG).show();
            return;
        } catch (IOException localIOException) {
            while (true)
                localIOException.printStackTrace();
        }
    }

    private String getSDcardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    protected void DB_Excel_Put(String uid) {
        SQLiteDatabase db_insert = getBaseContext().openOrCreateDatabase("suncape_const_db.db3", MODE_PRIVATE, null);
        String query = "SELECT const_logins.name, const_logins.s_name, const_logins.UID, const_logins.Name_Distr " +
                "FROM const_logins " +
                "WHERE const_logins.UID LIKE ('%" + uid + "%')";
        final Cursor cursor = db_insert.rawQuery(query, null);
        cursor.moveToFirst();
        String Name_Distr = cursor.getString(cursor.getColumnIndexOrThrow("Name_Distr"));
        name_put = Name_Distr;
        cursor.close();
        db_insert.close();
    }

    protected void Aksia_Delete_Kol() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
        String query = "SELECT brend, kod_univ, kol \n" +
                "FROM base_aksia\n" +
                "GROUP BY kod_uid \n" +
                "ORDER BY name ASC;";
        final Cursor cursor = db.rawQuery(query, null);
        ContentValues localContentValues = new ContentValues();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String koduniv = cursor.getString(cursor.getColumnIndexOrThrow("kod_univ"));
            localContentValues.put("kol", "0");
            String[] arrayOfString = new String[1];
            arrayOfString[0] = koduniv;
            db.update("base_aksia", localContentValues, "kod_univ = ?", new String[]{koduniv});
            Log.e("Aksia_Del =", "Данные по кол удалены!");
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    /**
     * прячем программную клавиатуру
     */
    protected void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            //imm.hideSoftInputFromWindow(editText_password.getWindowToken(), 0);
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


    protected void Update_New_TY_Listview(String new_skidka, String new_summa, String new_itogo) {
        try {
            Log.e("Count= ", new_summa);
            Log.e("Count= ", new_skidka);
            Log.e("Count= ", new_itogo);

            Double d_skidka = Double.parseDouble(new_skidka);
            Double d_summa = Double.parseDouble(new_summa);
            Double Itogo = d_summa - (d_summa * (d_skidka / 100));
            //tvw_d_itogo.setText(new DecimalFormat("#00.00").format(Itogo).replace(",", "."));
            Write_Db_AutoTY(new_skidka);
        } catch (Exception e) {
            Log.e("Ошибка данных", "Нет переменных ТУ");
        }



       /* if (Double.valueOf(new_summa) >= Double.valueOf(tvw_sp_summa.getText().toString())) {
          // tvw_d_error.setVisibility(View.GONE);

        } else {
          //  tvw_d_error.setVisibility(View.VISIBLE);
            Toast.makeText(context_Activity, "Сумма не подходит для ТУ", Toast.LENGTH_SHORT).show();
        }*/
    }

    protected void Write_DB_Zakaz(String w_query, String w_kodrn, String w_sklad_name, String w_sklad_uid, String w_skidka, String w_itogo, String w_debet) {
        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        switch (w_query) {
            case "query_insert": {
                Log.e("Запрос ", w_query);
                SQL_Summa_RN(w_kodrn);
                try {
                    peremen_query = "INSERT INTO base_RN_All (kod_rn, agent_name, agent_uid, k_agn_uid, " +
                            "k_agn_name, data_xml, credit, sklad, sklad_uid, cena_price, coment, " +
                            "uslov_nds, skidka_title, credite_date, k_agn_adress, data, vrema, data_up, summa, skidka, status, debet_new, itogo) " +
                            "VALUES (" +
                            "'" + w_kodrn + "', " +
                            "'" + PEREM_AG_NAME.replaceAll("'", "*") + "', " +
                            "'" + PEREM_AG_UID + "', " +
                            "'" + PEREM_K_AG_UID + "', " +
                            "'" + PEREM_K_AG_NAME.replaceAll("'", "*") + "', " +
                            "'" + PEREM_K_AG_Data_WORK + "', " +
                            "'" + PEREM_DOP_CREDIT + "', " +
                            "'" + w_sklad_name + "', " +
                            "'" + w_sklad_uid + "', " +
                            "'" + PEREM_AG_CENA + "', " +
                            "'" + "Дата отгрузки: " + PEREM_DOP_DATA_UP + "; Cкидка: " + PEREM_DOP_SKIDKA + "%;cmn_" + PEREM_DOP_COMMENT + "', " +
                            "'" + PEREM_DOP_NDS + "', " +
                            "'" + w_skidka + "', " +
                            "'" + PEREM_DOP_CREDITE_DATE + "', " +
                            "'" + PEREM_K_AG_ADRESS + "', " +
                            "'" + PEREM_K_AG_Data + "', " +
                            "'" + PEREM_K_AG_Vrema + "', " +
                            "'" + PEREM_DOP_DATA_UP.replaceAll("-", ".") + "', " +
                            // "'" + textView_aut_summa.getText().toString().replace(",", ".") + "', " +
                            "'" + summa_new_rn.replace(",", ".") + "', " +
                            "'" + PEREM_DOP_SKIDKA + "', " +
                            "'false', " +
                            "'" + w_debet + "', " +
                            //  "'" + textView_itog.getText().toString().replace(",", ".") + "');";
                            "'" + itogo_new_rn.replace(",", ".") + "');";
                } catch (Exception e) {
                    Log.e("WJ_END:", "Ошибка: ошибка записи данных!");
                    Toast.makeText(context_Activity, "Ошибка: ошибка записи данных!", Toast.LENGTH_SHORT).show();
                }

            }
            break;
            case "query_update": {
                Log.e("Запрос ", w_query);
                SQL_Summa_RN(w_kodrn);
                try {
                    peremen_query = "UPDATE base_RN_All SET " +
                            "kod_rn = '" + PEREM_K_AG_KodRN + "',  " +
                            "agent_name = '" + PEREM_AG_NAME.replaceAll("'", "*") + "',  " +
                            "agent_uid = '" + PEREM_AG_UID + "', " +
                            "k_agn_uid = '" + PEREM_K_AG_UID + "', " +
                            "k_agn_name = '" + PEREM_K_AG_NAME.replaceAll("'", "*") + "',  " +
                            "data_xml = '" + PEREM_K_AG_Data_WORK + "',  " +
                            "credit = '" + PEREM_DOP_CREDIT + "',  " +
                            "sklad = '" + PEREM_AG_SKLAD + "',  " +
                            "sklad_uid = '" + PEREM_AG_UID_SKLAD + "',  " +
                            "cena_price = '" + PEREM_AG_CENA + "',  " +
                            "coment = '" + "Дата отгрузки " + PEREM_DOP_DATA_UP + "; скидка " + PEREM_DOP_SKIDKA + "%;cmn_" + PEREM_DOP_COMMENT + "', " +
                            "uslov_nds = '" + PEREM_DOP_NDS + "',  " +
                            "skidka_title = '" + w_skidka + "',  " +
                            "credite_date = '" + PEREM_DOP_CREDITE_DATE + "',  " +
                            "k_agn_adress = '" + PEREM_K_AG_ADRESS + "', " +
                            "data = '" + PEREM_K_AG_Data + "', " +
                            "vrema = '" + PEREM_K_AG_Vrema + "', " +
                            "data_up = '" + PEREM_DOP_DATA_UP.replaceAll("-", ".") + "', " +
                            //   "summa = '" + textView_aut_summa.getText().toString().replace(",", ".") + "',  " +
                            "summa = '" + summa_new_rn.replace(",", ".") + "',  " +
                            "skidka = '" + PEREM_DOP_SKIDKA + "', " +
                            "status = 'false', " +
                            "debet_new = '" + w_debet + "', " +
                            //   "itogo = '" + textView_itog.getText().toString().replace(",", ".") + "'  " +
                            "itogo = '" + itogo_new_rn.replace(",", ".") + "'  " +
                            "WHERE kod_rn = '" + w_kodrn + "'";
                } catch (Exception e) {
                    Log.e("WJ_END:", "Ошибка: ошибка обновления данных!");
                    Toast.makeText(context_Activity, "Ошибка: ошибка обновления данных!", Toast.LENGTH_SHORT).show();
                }

            }
            break;
            default:
                break;
        }

        final Cursor cursor = db.rawQuery(peremen_query, null);
        cursor.moveToLast();
        cursor.close();
        db.close();

        try {

        } catch (Exception e) {
            Log.e("WJ_END:", "Ошибка: ошибка заполнения!");
            Toast.makeText(context_Activity, "Ошибка: ошибка заполнения!", Toast.LENGTH_SHORT).show();
            db.close();
        }


        try {

        } catch (Exception e) {
            Log.e("Error_Query ", "Ошибка занесение данных!");
            Toast.makeText(context_Activity, "Ошибка занесение данных!", Toast.LENGTH_SHORT).show();
        }
      /*  String query = "SELECT base_RN.Agent, base_RN.Kod_RN, base_RN.Vrema, " +
                "base_RN.Data, base_RN.Klients, base_RN.Adress, base_RN.UID_Klients,SUM(base_RN.Summa) AS Summa\n" +
                "FROM base_RN \n" +
                "WHERE base_RN.Kod_RN LIKE '%" + PEREM_K_AG_KodRN + "%'";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        ContentValues localContentValues_2 = new ContentValues();
        while (cursor.isAfterLast() == false) {
            localContentValues_2.put("agent_uid ", PEREM_AG_UID);
            localContentValues_2.put("agent_name", PEREM_AG_NAME);
            localContentValues_2.put("kod_rn", PEREM_K_AG_KodRN);                                                               // Доработать по акции
            localContentValues_2.put("k_agn_name", PEREM_K_AG_NAME);
            localContentValues_2.put("k_agn_adress", PEREM_K_AG_ADRESS);
            localContentValues_2.put("k_agn_uid ", PEREM_K_AG_UID);
            localContentValues_2.put("vrema", PEREM_K_AG_Vrema);
            localContentValues_2.put("data", PEREM_K_AG_Data);
            localContentValues_2.put("data_up", );
            localContentValues_2.put("summa", textView_aut_summa.getText().toString().replace(",", "."));    // Доработать по акции
            localContentValues_2.put("skidka", "%");                                                                           // Доработать по акции
            localContentValues_2.put("itogo", textView_itog.getText().toString().replace(",", "."));        // Доработать по акции
            db.insert("base_RN_All", null, localContentValues_2);
            cursor.moveToNext();
        }
        Log.e("WJZakS1=", PEREM_K_AG_KodRN + ": " + localContentValues_2.toString());
        Toast.makeText(context_Activity, PEREM_K_AG_KodRN + " добавленно! ", Toast.LENGTH_SHORT).show();
        cursor.close();
        db.close();
        Writing_To_Exsel();*/

    }

    protected void Write_Debet_New(String w_d_agent_name, String w_d_agent_uid, String w_d_client_name, String w_d_client_uid, String w_d_debet) {
        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);

        String query = "SELECT * FROM otchet_debet " +
                "WHERE d_kontr_uid = '" + w_d_client_uid + "' AND d_summa > 0;";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            String d_summa = cursor.getString(cursor.getColumnIndexOrThrow("d_summa")).replace(",", ".");
            Double debet_new, debet_old;
            debet_old = Double.parseDouble(d_summa);
            debet_new = Double.parseDouble(w_d_debet) + debet_old;

            peremen_query = "UPDATE otchet_debet SET " +
                    "d_agent_name = '" + w_d_agent_name + "',  " +
                    "d_agent_uid = '" + w_d_agent_uid + "',  " +
                    "d_kontr_name = '" + w_d_client_name + "', " +
                    "d_kontr_uid = '" + w_d_client_uid + "', " +
                    "d_summa = '" + new DecimalFormat("#00.00").format(debet_new).replace(",", ".") + "'  " +
                    "WHERE d_kontr_uid = '" + w_d_client_uid + "'";
            PEREM_NEW_DEBET_WRITE = new DecimalFormat("#00.00").format(debet_new).replace(",", ".");

        } else {
            peremen_query = "INSERT INTO otchet_debet (d_agent_name, d_agent_uid, d_kontr_name, d_kontr_uid, d_summa) " +
                    "VALUES (" +
                    "'" + w_d_agent_name + "', " +
                    "'" + w_d_agent_uid + "', " +
                    "'" + w_d_client_name + "', " +
                    "'" + w_d_client_uid + "', " +
                    "'" + w_d_debet + "');";
            PEREM_NEW_DEBET_WRITE = w_d_debet;
        }

        try {
            final Cursor cursor_new = db.rawQuery(peremen_query, null);
            cursor_new.moveToLast();
            cursor_new.close();
            db.close();
        } catch (Exception e) {
            Log.e("WJ_END:", "Ошибка: ошибка заполнения!");
            Toast.makeText(context_Activity, "Ошибка: ошибка заполнения!", Toast.LENGTH_SHORT).show();
            db.close();
        }

        try {
        } catch (Exception e) {
            Log.e("Error_Query ", "Ошибка занесение данных!");
            Toast.makeText(context_Activity, "Ошибка занесение данных!", Toast.LENGTH_SHORT).show();
        }

    }


    protected void Write_Db_AutoTY(String ty_uskov) {
        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT base_RN._id, base_RN.Kod_RN, base_RN.aks_pref, base_RN.Skidka, " +
                "base_RN.Cena, base_RN.Cena_SK, base_RN.Itogo, base_RN.Kol\n" +
                "FROM base_RN\n" +
                "WHERE base_RN.Kod_RN = '" + PEREM_K_AG_KodRN + "'";
        final Cursor cursor = db.rawQuery(query, null);
        ContentValues localContentValues = new ContentValues();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                String kodrn = cursor.getString(cursor.getColumnIndexOrThrow("Kod_RN"));
                String aks_pref = cursor.getString(cursor.getColumnIndexOrThrow("aks_pref"));
                // String skidka = cursor.getString(cursor.getColumnIndex("Skidka"));
                String cena = cursor.getString(cursor.getColumnIndexOrThrow("Cena"));
                String cena_sk = cursor.getString(cursor.getColumnIndexOrThrow("Cena_SK"));
                String itogo = cursor.getString(cursor.getColumnIndexOrThrow("Itogo"));
                String kol = cursor.getString(cursor.getColumnIndexOrThrow("Kol"));
                if (aks_pref.equals("ty")) {
                    Double d_cena = Double.valueOf(cena);
                    Double d_skidka = Double.valueOf(ty_uskov);
                    Double d_cena_sk = d_cena - (d_cena * (d_skidka / 100));
                    Double d_itogo = d_cena_sk * Double.valueOf(kol);


                    String cena_sk_new = new DecimalFormat("#00.00").format(d_cena_sk);
                    String itogo_new = new DecimalFormat("#00.00").format(d_itogo);
                    localContentValues.put("skidka", ty_uskov);
                    localContentValues.put("Cena_SK", cena_sk_new.replace(",", "."));
                    localContentValues.put("Itogo", itogo_new.replace(",", "."));
                    Log.e("Cena_sk ", cena_sk_new.replace(",", "."));
                    cursor.moveToNext();
                } else cursor.moveToNext();
                String[] arrayOfString = new String[1];
                arrayOfString[0] = id;  // массив строк по которому искать уникальный номер
                db.update("base_RN", localContentValues, "_id = ?", new String[]{id});
            }
        } else Toast.makeText(context_Activity, "Нет товара!", Toast.LENGTH_SHORT).show();

        cursor.close();
        db.close();
    }


    protected void ReWrite_KodRN() {
        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String query_select = "SELECT base_RN_All.kod_rn, base_RN_All.data FROM base_RN_All WHERE base_RN_All.kod_rn LIKE '%" + PEREM_K_AG_KodRN + "'";

        final Cursor cursor = db.rawQuery(query_select, null);
        if (cursor.getCount() > 0) {
            Log.e("NReWrite_KodRN", "Есть совпадений");
            perem_rewrite_rn = "query_update";
        } else {
            Log.e("NReWrite_KodRN", "Нет совпадений");
            perem_rewrite_rn = "query_insert";
        }
        cursor.close();
        db.close();
    }

    protected void Select_TY_Uslov() {
        localView = LayoutInflater.from(context_Activity).inflate(R.layout.mt_list_dialog_checked, null);
        spinner = localView.findViewById(R.id.spk_type_ty);
        //  tvw_d_summa = localView.findViewById(R.id.tvw_fact_summa);
        // tvw_d_itogo = localView.findViewById(R.id.tvw_fact_itogo);
        //  tvw_d_error = localView.findViewById(R.id.tvw_fact_error);

        // localView_ty = LayoutInflater.from(context_Activity).inflate(R.layout.mt_list_spinner_ty, null);


        spinner_ty.clear();
        Constanta_TY();
        adapterPriceClients_ty = new ListAdapterAde_Spinner_TY(context_Activity, spinner_ty);
        adapterPriceClients_ty.notifyDataSetChanged();
        spinner.setAdapter(adapterPriceClients_ty);

        tvw_d_error.setVisibility(View.GONE);
        Loading_DB_Summa_TY();
        tvw_d_summa.setText(d_db_summa);
        tvw_d_itogo.setText("0.0");
        if (Double.valueOf(d_db_summa) < 500) {
            tvw_d_error.setVisibility(View.VISIBLE);
        } else tvw_d_error.setVisibility(View.GONE);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tvw_sp_skidka = view.findViewById(R.id.tvw_ty_skidka);
                tvw_sp_summa = view.findViewById(R.id.tvw_ty_summa);


                Log.e("Count= ", d_db_summa);
                Log.e("Count= ", tvw_sp_skidka.getText().toString());
                Log.e("Count= ", tvw_sp_summa.getText().toString());
                Log.e("Count= ", String.valueOf(i));


                if (Double.valueOf(d_db_summa) >= Double.valueOf(tvw_sp_summa.getText().toString())) {
                    tvw_d_error.setVisibility(View.GONE);
                    Double d_skidka = Double.parseDouble(tvw_sp_skidka.getText().toString());
                    Double d_summa = Double.parseDouble(d_db_summa);
                    Double Itogo = d_summa - (d_summa * (d_skidka / 100));
                    //  tvw_d_itogo.setText(new DecimalFormat("#00.00").format(Itogo).replace(",", "."));
                    Write_Db_AutoTY(tvw_sp_skidka.getText().toString());
                } else {
                    tvw_d_error.setVisibility(View.VISIBLE);
                    Toast.makeText(context_Activity, "Сумма не подходит для ТУ", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e("Count3= ", d_db_summa);
                Log.e("Count3= ", tvw_sp_skidka.getText().toString());
            }
        });

        AlertDialog.Builder localBuilder = new AlertDialog.Builder(context_Activity);
        localBuilder.setView(localView);
        localBuilder.setTitle("Торговые условия");
        localBuilder.setCancelable(false).setIcon(R.drawable.office_icon_selection_ty).setPositiveButton(" ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                try {


                } catch (Exception e) {
                    Toast.makeText(context_Activity, "Ошибка кнопки фильтр!", Toast.LENGTH_SHORT).show();
                    Log.e("File", "Ошибка кнопки фильтр!");
                }

                paramDialogInterface.cancel();


            }
        }).setNegativeButton(" ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

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

    protected void Constanta_TY() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
        String query = "SELECT * FROM const_ty";
        final Cursor cursor = db.rawQuery(query, null);
        mass_ty = new String[cursor.getCount()][3];
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String count_skidka = cursor.getString(cursor.getColumnIndexOrThrow("count_skidka"));
                String type_skidka = cursor.getString(cursor.getColumnIndexOrThrow("type_skidka"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String summa_min = cursor.getString(cursor.getColumnIndexOrThrow("summa_min"));
                String kyrc = cursor.getString(cursor.getColumnIndexOrThrow("kyrc"));
                spinner_ty.add(new ListAdapterSimple_Spinner_TY(count_skidka, summa_min, "ТУ=", type_skidka, status, kyrc));
                cursor.moveToNext();
            }
        } else Toast.makeText(context_Activity, "Нет данных по ТУ", Toast.LENGTH_SHORT).show();
        cursor.close();
        db.close();
    }

    protected void Loading_DB_Summa_TY() {
        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT base_RN.aks_pref, sum(base_RN.Summa) AS 'ty_summa' FROM base_RN\n" +
                "WHERE base_RN.Kod_RN = '" + PEREM_K_AG_KodRN + "'";
        final Cursor cursor = db.rawQuery(query, null);
        Log.e("Count= ", String.valueOf(cursor.getCount()));
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            String ty_summa = cursor.getString(cursor.getColumnIndexOrThrow("ty_summa"));
            if (ty_summa != null) {
                d_db_summa = ty_summa;
            } else d_db_summa = "0.0";
        } else {
            d_db_summa = "0.0";
            Log.e("Count= ", "Нет данных по ТУ");
            Toast.makeText(context_Activity, "Нет данных по ТУ", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        db.close();
    }


    protected void Constanta_Write(String kag_uid, String kag_name, String kag_adress, String kag_kodrn, String kag_data, String kag_vrema, String kag_gps) {
        /*ed = sp.edit();
        ed.putString("PEREM_K_AG_NAME", kag_name);
        ed.putString("PEREM_K_AG_UID", kag_uid);
        ed.putString("PEREM_K_AG_ADRESS", kag_adress);
        ed.putString("PEREM_K_AG_KodRN", kag_kodrn);
        ed.putString("PEREM_K_AG_Data", kag_data);
        ed.putString("PEREM_K_AG_Vrema", kag_vrema);
        ed.putString("PEREM_K_AG_GPS", kag_gps);
        ed.commit();*/
    }


    // Обработка панели кнопок
    protected void Button_Group() {
        button_add = (Button) findViewById(R.id.button_add);
        button_cena_ty = (Button) findViewById(R.id.button_cena_ty);
        button_discount = (Button) findViewById(R.id.button_discount);
        button_scan = (Button) findViewById(R.id.button_scan);
        button_refresh = (Button) findViewById(R.id.button_refresh);
        button_summa = (Button) findViewById(R.id.button_autosumma);
        button_delete = (Button) findViewById(R.id.button_delete);
      /*  Intent intent = new Intent(context_Activity, WJ_Favorites.class);
        startActivity(intent);*/

        // Обработчик кнопки закончить заказ
        try {
            button_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
                    PEREM_CLICK_TY = sp.getString("PEREM_CLICK_TY", "0");                    //чтение данных: имя сервера
                    Log.e("RRR", PEREM_CLICK_TY);

                    if (!textView_aut_summa.getText().toString().equals("") & (PEREM_CLICK_TY.equals("true"))) {
                        dialog = new androidx.appcompat.app.AlertDialog.Builder(context_Activity);
                        dialog.setTitle("Вы хотите оформить заказ?");
                        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                PEREM_NEW_DEBET_WRITE = "0";
                                Write_Debet_New(PEREM_AG_NAME, PEREM_AG_UID, PEREM_K_AG_NAME, PEREM_K_AG_UID, PEREM_DOP_ITOGO);

                                SQL_Sklad_UID_ReWrite(PEREM_K_AG_KodRN);

                                for (int l = 0; l < mass_m.length; l++) {
                                    Log.e("Переменные для записи:", "Код накладной - " + mass_m[l][0]);
                                    //   Write_DB_Zakaz(perem_rewrite_rn, PEREM_K_AG_KodRN, "", "", PEREM_DOP_SKIDKA, "", PEREM_NEW_DEBET_WRITE);

                                    Log.e("Переменные для записи:", "Name_Order - " + PEREM_K_AG_KodRN);
                                    //   Log.e("Переменные для записи:", "Name_Order - " + mass_m[l][0]);
                                    Log.e("Переменные для записи:", "Name_Ag - " + PEREM_AG_NAME);
                                    Log.e("Переменные для записи:", "UID_Ag - " + PEREM_AG_UID);
                                    Log.e("Переменные для записи:", "Name_C - " + PEREM_K_AG_NAME);
                                    Log.e("Переменные для записи:", "UID_C - " + PEREM_K_AG_UID);
                                    Log.e("Переменные для записи:", "Data - " + PEREM_K_AG_Data_WORK);
                                    Log.e("Переменные для записи:", "CREDIT - " + PEREM_DOP_CREDIT);
                                    Log.e("Переменные для записи:", "SKLAD - " + PEREM_AG_SKLAD);
                                    Log.e("Переменные для записи:", "UID_Sklad - " + PEREM_AG_UID_SKLAD);
                                    Log.e("Переменные для записи:", "Cena_Price - " + PEREM_AG_CENA);
                                    Log.e("Переменные для записи:", "Coment - " + "Дата отгрузки " + PEREM_DOP_DATA_UP + "; скидка " + PEREM_DOP_SKIDKA + "%;cmn_', " + PEREM_DOP_COMMENT);
                                    Log.e("Переменные для записи:", "Cena_Nds - " + PEREM_DOP_NDS);

                                    Log.e("Дата поставки: ", PEREM_DOP_DATA_UP);
                                    Log.e("НДС: ", PEREM_DOP_NDS);
                                    Log.e("Скидка: ", PEREM_DOP_SKIDKA);
                                    Log.e("Дней конс: ", PEREM_DOP_CREDITE_DATE);
                                    Log.e("Оплата:", PEREM_DOP_SKIDKA);
                                    Log.e("Сумма: ", PEREM_DOP_SUMMA);
                                    Log.e("Итого: ", PEREM_DOP_ITOGO);

                                    Write_DB_Zakaz(perem_rewrite_rn, mass_m[l][0], mass_m[l][2], mass_m[l][1], PEREM_DOP_SKIDKA, summa_new_rn, PEREM_NEW_DEBET_WRITE);
                                }
                                Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            }
                        });
                        dialog.show();
                    } else {
                        Loading_Adapter_Refresh();
                        Log.e("Кнопка Add...", "Ошибка: Не заполнен заказ или не выбраны условия!");
                        Toast.makeText(context_Activity, "Ошибка: Не заполнен заказ или не выбраны условия!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Log.e("Кнопка Add...", "Ошибка обработки кнопки Add");
        }

        // Обработчик кнопки Торговые условия
        try {
            button_cena_ty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Double.parseDouble(textView_aut_summa.getText().toString()) > 0) {
                        Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza_Dop_Data.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e("Кнопка TY...", "Заказ не заполнен!");
                        Toast.makeText(context_Activity, "Ошибка: выберите товар!", Toast.LENGTH_SHORT).show();
                    }

                    // Select_TY_Uslov();
                    //  Write_Db_AutoTY("");
                }
            });
        } catch (Exception e) {
            Log.e("Кнопка Add...", "Ошибка обработки кнопки Add");
        }

        button_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*ed = sp.edit();
                ed.putString("PEREM_KOD_RN_THIS", PEREM_K_AG_KodRN);
                ed.commit();
                Aksia_Delete_Kol();
                Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza_Akcia.class);
                startActivity(intent);*/
                Toast.makeText(context_Activity, "В разработке!", Toast.LENGTH_SHORT).show();
            }
        });


        button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loading_Adapter_Refresh();
            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new androidx.appcompat.app.AlertDialog.Builder(context_Activity);
                dialog.setTitle("Вы хотите удалить все данные?");
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Delete_RN_BASE();
                        Loading_Adapter_Refresh();
                        Toast.makeText(context_Activity, "Данные удаленны!", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
                dialog.show();
            }
        });

        button_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context_Activity, "В разработке!", Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(context_Activity, WJ_Strih_Kod.class);
                startActivity(intent);
                finish();*/
            }
        });


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

        PEREM_K_AG_NAME = sp.getString("PEREM_K_AG_NAME", "0");          //чтение данных: имя контраегнта
        PEREM_K_AG_UID = sp.getString("PEREM_K_AG_UID", "0");            //чтение данных: uid контрагента
        PEREM_K_AG_ADRESS = sp.getString("PEREM_K_AG_ADRESS", "0");      //чтение данных: адрес контрагент
        PEREM_K_AG_KodRN = sp.getString("PEREM_K_AG_KodRN", "0");         //чтение данных: код накладной
        PEREM_K_AG_Data = sp.getString("PEREM_K_AG_Data", "0");           //чтение данных: время создание н
        PEREM_K_AG_Data_WORK = sp.getString("PEREM_K_AG_Data_WORK", "0");           //чтение данных: время создание н
        PEREM_K_AG_Vrema = sp.getString("PEREM_K_AG_Vrema", "0");        //чтение данных: дата создание на
        PEREM_K_AG_GPS = sp.getString("PEREM_K_AG_GPS", "0");             //чтение данных: координаты gps


        PEREM_DOP_DATA_UP = sp.getString("PEREM_DOP_DATA_UP", "0");               //чтение данных: дата отгрузки товара
        PEREM_DOP_NDS = sp.getString("PEREM_DOP_NDS", "0");                       //чтение данных: выыбор ндс
        PEREM_DOP_SKIDKA = sp.getString("PEREM_DOP_SKIDKA", "0");                 //чтение данных: скидка основное ТУ
        PEREM_DOP_CREDIT = sp.getString("PEREM_DOP_CREDIT", "0");                 //чтение данных: оплата товара
        PEREM_DOP_SUMMA = sp.getString("PEREM_DOP_SUMMA", "0");                   //чтение данных: сумма товар
        PEREM_DOP_ITOGO = sp.getString("PEREM_DOP_ITOGO", "0");                   //чтение данных: итого после ТУ
        PEREM_DOP_CREDITE_DATE = sp.getString("PEREM_DOP_CREDITE_DATE", "0");     //чтение данных: дней конс
        PEREM_DOP_COMMENT = sp.getString("PEREM_DOP_COMMENT", "0");     //чтение данных: дней конс
        PEREM_CLICK_TY = sp.getString("PEREM_CLICK_TY", "0");                     //чтение данных: итого после ТУ
        PEREM_KOD_BRENDS_VISIBLE = sp.getString("PEREM_KOD_BRENDS_VISIBLE", "0");                 //чтение данных:

        if (sp.getBoolean("key_SwitchPreference_BrendsAll", true) == true) {
            pref_checkbox_brends = true;
            Log.e("check_box_brends", "TR? Бренда, заводские: ");
        } else {
            pref_checkbox_brends = false;
            Log.e("check_box_brends", "FL, Бренда, пользоы ");
        }

        Set<String> hs = sp.getStringSet("key_MultiSelectPreference_BrendsManual", new HashSet<String>());
        Log.e("Настройки: ", "Бренды: = (" + sp.getStringSet("key_MultiSelectPreference_BrendsManual", new HashSet<String>()) + ")");
        Log.e("Массив: ", "" + hs.size());
        mass_menu_product = new String[hs.size()];
        Iterator<String> intr = hs.iterator();
        int m = 0;
        while (intr.hasNext()) {
            mass_menu_product[m] = intr.next();
            m++;
        }
        if (hs.size() > 0) {
            Log.e("Бренды", "Есть данные");
        } else Log.e("Бренды", "Все бренды");
    }

    protected void SQL_Sklad_UID_ReWrite(String w_uid) {
        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT DISTINCT Kod_RN, sklad_uid, sklad_name FROM base_RN WHERE Kod_RN = '" + w_uid + "';";
        Log.e("КодRN=", w_uid);
        final Cursor cursor = db.rawQuery(query, null);
        mass_m = new String[cursor.getCount()][3];
        Integer k_rn = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String Kod_RN = cursor.getString(cursor.getColumnIndexOrThrow("Kod_RN"));
                String uid_sklad = cursor.getString(cursor.getColumnIndexOrThrow("sklad_uid"));
                String name_sklad = cursor.getString(cursor.getColumnIndexOrThrow("sklad_name"));
                mass_m[cursor.getPosition()][1] = uid_sklad;
                mass_m[cursor.getPosition()][2] = name_sklad;
                String prefix_kod = Kod_RN.substring(0, Kod_RN.length() - 4);
                Integer number_kod = Integer.parseInt(Kod_RN.substring(12));
                Integer new_number_kod = number_kod + k_rn;
                Log.e("Forma_Error=", "Префикс код: " + prefix_kod);
                Log.e("Forma_Error=", "Номер код: " + number_kod);
                Log.e("Forma_Error=", "Новый код: " + new_number_kod);
                Log.e("Forma_Error=", "Длина кода: " + new_number_kod.toString().length());
                if (new_number_kod.toString().length() == 1) {
                    new_kod_rn = "000" + new_number_kod;
                } else if (new_number_kod.toString().length() == 2) {
                    new_kod_rn = "00" + new_number_kod;
                } else if (new_number_kod.toString().length() == 3) {
                    new_kod_rn = "0" + new_number_kod;
                } else if (new_number_kod.toString().length() == 4) {
                    new_kod_rn = "" + new_number_kod;
                }
                mass_m[cursor.getPosition()][0] = prefix_kod + new_kod_rn;
                Log.e("Forma_Kod=", "Новый код накладной" + new_kod_rn);
                k_rn++;
                cursor.moveToNext();
            }
        }
        cursor.close();

        for (int k = 0; k < mass_m.length; k++) {
            Log.e("Sklad=", mass_m[k][1] + " RN=" + mass_m[k][0]);
        }

        ContentValues localContentValuesUP = new ContentValues();
        String query_new = "SELECT koduid, Kod_RN, sklad_uid FROM base_RN WHERE Kod_RN = '" + w_uid + "';";
        final Cursor cursor_new = db.rawQuery(query_new, null);
        cursor_new.moveToFirst();
        while (cursor_new.isAfterLast() == false) {
            String koduid = cursor_new.getString(cursor_new.getColumnIndexOrThrow("koduid"));
            String uid_sklad = cursor_new.getString(cursor_new.getColumnIndexOrThrow("sklad_uid"));
            String Kod_RN = cursor_new.getString(cursor_new.getColumnIndexOrThrow("Kod_RN"));
            for (int i = 0; i < mass_m.length; i++) {
                if (uid_sklad.equals(mass_m[i][1])) {
                    Log.e("YES Sklad=", mass_m[i][1] + " RN=" + mass_m[i][0]);
                    localContentValuesUP.put("Kod_RN", mass_m[i][0]);
                    db.update("base_RN", localContentValuesUP, "Kod_RN = ? AND koduid = ?", new String[]{Kod_RN, koduid});
                } else Log.e("NET Sklad=", mass_m[i][1] + " RN=" + mass_m[i][0]);
            }
            cursor_new.moveToNext();
        }
        cursor_new.close();
        db.close();
    }


    protected void SQL_Summa_RN(String w_uid) {
        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT SUM (base_RN.Summa) AS 'sum_rn', SUM(base_RN.Itogo) AS 'itogo_rn' FROM base_RN WHERE base_RN.Kod_RN = '" + w_uid + "';";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor != null) {
            summa_new_rn = cursor.getString(cursor.getColumnIndexOrThrow("sum_rn"));
            itogo_new_rn = cursor.getString(cursor.getColumnIndexOrThrow("itogo_rn"));
        }
        cursor.close();
        db.close();
        Log.e("ERROR...", "Накладная:" + w_uid);
        Log.e("ERROR...", "Сумма накладной:" + summa_new_rn);
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
    protected void Sunbell_Switch_Category(String w_name, String w_group) {
        switch (w_group) {
            case "gr_1":
                topChannelMenu1.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.ic_cost);
                break;
            case "gr_2":
                topChannelMenu2.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.ic_cost_2);
                break;
            case "gr_3":
                topChannelMenu3.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.ic_cost_3);
                break;
            case "gr_4":
                topChannelMenu4.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.ic_cost_4);
                break;
            case "gr_5":
                topChannelMenu5.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.ic_cost);
                break;
            default:
                topChannelMenu1.add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).setIcon(R.drawable.ic_cost);
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

    //  Заполнение категорий по префиксу именем и картинкой
    protected SubMenu[] Image_Icon_Brends_Switch(String w_prefix, int i, String w_name, SubMenu[] subMenus) {
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
/*                subMenus[i].add(w_name.substring(0, 1) + w_name.substring(1).toLowerCase()).
                        setIcon(Drawable.createFromPath(getFilesDir().getAbsolutePath()+"/Icons/icons_image_for_icons.png"));*/
                break;
        }
        return subMenus;
    }


}


//
// Loading_Select_Menu(m);
/*        topChannelMenu_01 = m.addSubMenu("TradeGof");
        topChannelMenu_02 = m.addSubMenu("Sunbell");

        topChannelMenu1 = m.addSubMenu("TradeGof");
        topChannelMenu2 = m.addSubMenu("Sunbell (категория-1)");
        topChannelMenu3 = m.addSubMenu("Sunbell (категория-2)");
        topChannelMenu4 = m.addSubMenu("Sunbell (категория-3)");
        topChannelMenu5 = m.addSubMenu("Sunbell (категория-4)");
        topChannelMenu6 = m.addSubMenu("Sunbell (категория-5)");*/
/*
        try {
            String query = "SELECT * FROM base_in_brends_id";
            final Cursor cursor = db.rawQuery(query, null);
            if (pref_checkbox_brends.booleanValue()) {
                if (mass_menu_product.length > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        String name = cursor.getString(cursor.getColumnIndexOrThrow("brends"));        // столбец с брендом
                        String group = cursor.getString(cursor.getColumnIndexOrThrow("group_type"));   // столбец с префиксом группы
                        String pref = cursor.getString(cursor.getColumnIndexOrThrow("prefic"));        // столбец с префиксом группы
                        Decoder_Prefix(pref);
                        for (int i = 0; i < mass_menu_product.length; i++) {
                            if (pref.contains(mass_menu_product[i])) {
                                Log.e("Multi_Case ", mass_menu_product[i] + "__" + pref + "__" + i);
                                if (sp.getBoolean("switch_preference_sunbell_catg", true))
                                    Sunbell_Switch_Category(name, group);
                                else Sunbell_Switch_Category_NoSwitch(name, group);
                            }
                        }
                        cursor.moveToNext();
                    }
                }
            } else {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("brends"));        // столбец с брендом
                    String group = cursor.getString(cursor.getColumnIndexOrThrow("group_type"));   // столбец с префиксом группы
                    String pref = cursor.getString(cursor.getColumnIndexOrThrow("prefic"));   // столбец с префиксом группы
                    Decoder_Prefix(pref);
                    if (PEREM_KOD_BRENDS_VISIBLE.contains(pref.toUpperCase())) {
                        if (sp.getBoolean("switch_preference_sunbell_catg", true))
                            Sunbell_Switch_Category(name, group);
                        else Sunbell_Switch_Category_NoSwitch(name, group);
                    } else if (PEREM_KOD_BRENDS_VISIBLE.equals("/ALL/")) {
                        if (sp.getBoolean("switch_preference_sunbell_catg", true))
                            Sunbell_Switch_Category(name, group);
                        else Sunbell_Switch_Category_NoSwitch(name, group);
                    }
                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(this.getLocalClassName(), "Произошла ошибка, заполнения номенклатуры!");
            Toast.makeText(this, "Произошла ошибка, заполнения номенклатуры!", Toast.LENGTH_LONG).show();
        }*/
