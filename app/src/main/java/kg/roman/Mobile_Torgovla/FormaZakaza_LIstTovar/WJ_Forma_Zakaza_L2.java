package kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import kg.roman.Mobile_Torgovla.FormaZakazaStart.WJ_Forma_Zakaza;
import kg.roman.Mobile_Torgovla.FormaZakaza_EndZakaz.WJ_Forma_Zakaza_Dop_Data;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_WJ_Zakaz;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.CalendarThis;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.PreferencesWrite;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.Preferences_MTSetting;
import kg.roman.Mobile_Torgovla.R;

public class WJ_Forma_Zakaza_L2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Removable, RemovableDeletePosition, RemovableEditPosition {

    //////////////// 02.2024
    public ArrayList<ListAdapterSimple_WJ_Zakaz> list_zakaz = new ArrayList<>();
    RecyclerView_Adapter_ViewHolder_ListTovar adapterPriceClients;

    public String[] mass_menu_product;

    public Boolean pref_checkbox_brends;
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    String logeTAG = "WJFormaL2";

    public Toolbar toolbar;
    public RecyclerView recycler;
    public SwipeRefreshLayout swipeRefreshLayout;
    ConstraintLayout constraintLayout;
    TextView textView_Summa, textView_Itogo, textView_Count;
    public NavigationView navigationView;
    PreferencesWrite preferencesWrite;
    Preferences_MTSetting preferencesMtSetting;
    public Context context_Activity;
    String wCodeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        binding = MtWjAppLayoutFormaZakazBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());*/
        setContentView(R.layout.mt_wj_activity_forma_start);

        context_Activity = getBaseContext();
        preferencesWrite = new PreferencesWrite(context_Activity);
        preferencesMtSetting = new Preferences_MTSetting();

        CalendarThis calendarThis = new CalendarThis();
        mSettings = getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        // CreateNewSpeedDial();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getCodeOrder()));
        getSupportActionBar().setSubtitle(calendarThis.getThis_DateFormatAllDisplay);

        wCodeOrder = preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getCodeOrder());


        // Константы для чтения
        Constanta_Read();

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
        Navigation_Menu();

        // ReWrite_KodRN(); // Проверка номера накладной

        //// Создание меню функций
        CreateNewSpeedDial();

        CreateNavigatorMenu();

        recycler = findViewById(R.id.RecyclerView_ListTovar);
        constraintLayout = findViewById(R.id.ConstraintLayoutTovar);

        list_zakaz.clear();
        Loading_Adapter();
        adapterPriceClients = new RecyclerView_Adapter_ViewHolder_ListTovar(getBaseContext(), list_zakaz, OnClickEditPosition());
        recycler.setAdapter(adapterPriceClients);
        adapterPriceClients.notifyItemChanged(0);

        swipeRefreshLayout = findViewById(R.id.swiperefreshTovar);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            list_zakaz.clear();
            Loading_Adapter();
            adapterPriceClients = new RecyclerView_Adapter_ViewHolder_ListTovar(getBaseContext(), list_zakaz, OnClickEditPosition());
            recycler.setAdapter(adapterPriceClients);
            swipeRefreshLayout.setRefreshing(true);
            // ждем 2 секунды и прячем прогресс
            new Handler().postDelayed(() -> {
                // Отменяем анимацию обновления
                swipeRefreshLayout.setRefreshing(false);
                // говорим о том, что собираемся закончить
            }, 2000);

        });

        swipeDeletePosition();

        AvtoSumma();
    }

    // Создание нового меню вместа floatingActionButton
    protected void CreateNewSpeedDial() {
        SpeedDialView speedDialView = findViewById(R.id.speedDial_ListTovar);
        List<SpeedDialActionItem> speedDialList = new ArrayList<>();

        speedDialList.add(new SpeedDialActionItem.Builder(R.id.speedDialFormaListId_EndZakaz, R.drawable.icons_menu_end)
                .setLabel(getString(R.string.speedDialFormsListEndZakaz))
                .setLabelColor(Color.BLACK)
                .setLabelBackgroundColor(getColor(R.color.SeedBarBackground))
                .setFabImageTintColor(Color.WHITE)
                .create());
        speedDialList.add(new SpeedDialActionItem.Builder(R.id.speedDialFormaListId_VoiceHelp, R.drawable.icons_speedial_voice)
                .setLabel(getString(R.string.speedDialFormsListVoiceHelp))
                .setLabelColor(Color.BLACK)
                .setLabelBackgroundColor(getColor(R.color.SeedBarBackground))
                .setFabImageTintColor(Color.WHITE)
                .create());
        speedDialList.add(new SpeedDialActionItem.Builder(R.id.speedDialFormaListId_Favorite, R.drawable.icons_speedial_favority)
                .setLabel(getString(R.string.speedDialFormsListFovarite))
                .setLabelColor(Color.BLACK)
                .setLabelBackgroundColor(getColor(R.color.SeedBarBackground))
                .setFabImageTintColor(Color.WHITE)
                .create());

        speedDialView.addAllActionItems(speedDialList);

        speedDialView.setOnActionSelectedListener(actionItem -> {
            switch (actionItem.getId()) {
                case R.id.speedDialFormaListId_Favorite: {
                    Toast.makeText(context_Activity, "Избранный товар, в разработке(доступно с версии 4.6.2)", Toast.LENGTH_SHORT).show();
                }
                break; /// Создать новый заказ
                case R.id.speedDialFormaListId_VoiceHelp: {
                    Toast.makeText(context_Activity, "Голосовой помощник, в разработке(доступно с версии 4.6.2)", Toast.LENGTH_SHORT).show();
                }
                break; /// Создать новый заказ по акции
                case R.id.speedDialFormaListId_EndZakaz: {
                    Toast.makeText(context_Activity, "Закончить заказ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza_Dop_Data.class);
                    startActivity(intent);
                    finish();
                }
                break;   /// Создать копию выбранного заказа
            }
            return false;
        });
    }

    //// Изменение дизайна NavigatoMenu
    protected void CreateNavigatorMenu() {
        preferencesWrite = new PreferencesWrite(context_Activity);
        View navHeader = navigationView.getHeaderView(0);
/*        ImageView imageView_header = (ImageView) navHeader.findViewById(R.id.nav_header_nomenclature);
        TextView textView_header1 = (TextView) navHeader.findViewById(R.id.tvw_nav_headerTitle);
        TextView textView_header2 = (TextView) navHeader.findViewById(R.id.tvw_nav_headerText);*/
        TextView tvw_headerPref = navHeader.findViewById(R.id.tvw_nav_headerPref);
        //// Отображение состояние номенклатуры
        StringBuilder stringBuilder = new StringBuilder();
        if (preferencesWrite.Setting_Brends_AllBrends)
            stringBuilder.append("полная номенклатура; ");
        if (preferencesWrite.Setting_Brends_AgentsBrends)
            stringBuilder.append("сортировка для агентов; ");
        if (preferencesWrite.Setting_Brends_HandsBrends)
            stringBuilder.append("ручной отбор; ");
        if (preferencesWrite.Setting_Brends_FirmsGroups)
            stringBuilder.append("сортировка по фирмам; ");
        tvw_headerPref.setText(stringBuilder.toString());
        // Все для всех
        // список брендов для агента
        // ручной отбор
        // отображение по фирмам

    }

    @Override
    public void onResume() {
        Log.e("Resume", "Start onResume");
        list_zakaz.clear();
        Loading_Adapter();
        adapterPriceClients = new RecyclerView_Adapter_ViewHolder_ListTovar(getBaseContext(), list_zakaz, OnClickEditPosition());
        recycler.setAdapter(adapterPriceClients);
        adapterPriceClients.notifyDataSetChanged();
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
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_BASE, MODE_PRIVATE, null);
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
        preferencesWrite = new PreferencesWrite(context_Activity);
        preferencesMtSetting = new Preferences_MTSetting();


        if (preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getStatusOrder()).toString().equals("Edit")) {
            SubMenu[] subMenusFirms = new SubMenu[10];
            subMenusFirms[0] = m.addSubMenu(preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getSkladName()));
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_BASE, MODE_PRIVATE, null);
            final Cursor cursor = db.rawQuery("SELECT DISTINCT write_group_brends.brends_prefix, write_group_brends.brends_name," +
                    "write_group_brends.brends_kod FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid =base_in_ostatok.nomenclature_uid\n" +
                    "LEFT JOIN write_group_brends ON base_in_nomeclature.brends =write_group_brends.brends_name\n" +
                    "WHERE base_in_ostatok.sklad_uid = '" + preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getSkladUID()) + "'\n" +
                    "AND write_group_brends.brends_prefix IS NOT NULL " +
                    "ORDER BY brends;", null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("brends_name"));        // столбец с брендом
                String pref = cursor.getString(cursor.getColumnIndexOrThrow("brends_prefix"));      // столбец с префиксом группы
                Log.e(logeTAG, "Name: " + name);
                Log.e(logeTAG, "Pref: " + pref);
                subMenusFirms[0]
                        .add(name.charAt(0) + name.substring(1).toLowerCase())
                        .setIcon(IconsForBrends(pref));
                cursor.moveToNext();
            }
            cursor.close();
            try {

            } catch (Exception e) {
                Log.e(logeTAG, "ошибка структуры все для всех: " + e);
            }
        } else {


            boolean AllBrends = mSettings.getBoolean("key_SwitchPreference_BrendsAll", false);        // Все для всех

            boolean AgentsBrends = mSettings.getBoolean("key_SwitchPreference_BrendsAgents", false);  // Сортировка для агентов
            TreeSet<String> brendsAgents = new TreeSet<>(mSettings.getStringSet("PEREM_BrendsForNomeclature", new TreeSet<>()));

            boolean HandsBrends = mSettings.getBoolean("key_SwitchPreference_BrendsManual", false);   // Ручной отбор
            TreeSet<String> brendsManula = new TreeSet<>(mSettings.getStringSet("key_MultiSelectPreference_BrendsManual", new TreeSet<>()));

            boolean FirmsGroups = mSettings.getBoolean("key_SwitchPreference_BrendsFirms", false);   // Отображение по фирмам


            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_BASE, MODE_PRIVATE, null);
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
                            subMenusFirms[0]
                                    .add(name.charAt(0) + name.substring(1).toLowerCase())
                                    .setIcon(IconsForBrends(pref));
                            cursor.moveToNext();
                        }
                        cursor.close();
                    }
                } catch (Exception e) {
                    Log.e(logeTAG, "ошибка структуры все для всех AllBrends" + e);
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
                    Log.e(logeTAG, "ошибка структуры для агентов" + e);
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


    }

/*    protected Pair<String, String> selectSklad() {
        Pair<String, String> pairSklad = null;
        preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);

        final Cursor cursor = db.rawQuery("SELECT kod_rn, sklad, sklad_uid FROM base_RN_All WHERE kod_rn = '" + wCodeOrder + "';", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            String skladName = cursor.getString(cursor.getColumnIndexOrThrow("sklad"));        // столбец с брендом
            String skladUID = cursor.getString(cursor.getColumnIndexOrThrow("sklad_uid"));        // столбец с префиксом группы
            pairSklad = new Pair<>(skladName, skladUID);
            preferencesMtSetting.writeSettingString(context_Activity, preferencesMtSetting.getSkladName(), skladName);
            preferencesMtSetting.writeSettingString(context_Activity, preferencesMtSetting.getSkladUID(), skladUID);
            editor = mSettings.edit();
            editor.putString("SelectEdit_SkladName", skladName);
            editor.putString("SelectEdit_SkladUID", skladUID);
            editor.commit();
        }
        cursor.close();
        db.close();
        return pairSklad;
    }*/


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
            return ResourcesCompat.getDrawable(res, R.drawable.no_image, null);
        }
    }

    ///// Функция нажатия на позицию, Редактирование
    protected RecyclerView_Adapter_ViewHolder_ListTovar.OnStateClickListener OnClickEditPosition() {
        return (list_zakaz1, position) ->
        {
            DialogFragment_EditPosition dialogEdit = new DialogFragment_EditPosition();
            Bundle args = new Bundle();
            args.putString("uid", list_zakaz1.getUID());
            //args.putString("uid", );
            args.putInt("count", Integer.parseInt(list_zakaz1.getCount()));
            args.putInt("position", position);
            dialogEdit.setArguments(args);


            WindowManager.LayoutParams p = getWindow().getAttributes();
            p.width = ViewGroup.LayoutParams.MATCH_PARENT;
            getWindow().setAttributes(p);


            dialogEdit.show(getSupportFragmentManager(), "DialogEdit");
        };
    }


    //// Функция Swipe, удаление товара по позиции
    protected void swipeDeletePosition() {
        ItemTouchHelper.SimpleCallback simpleCallback;
        ItemTouchHelper mItemTouchHelper;
        simpleCallback = new ItemTouchHelper.SimpleCallback(200, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Log.e(logeTAG, "onMove");
                Toast.makeText(context_Activity, "on Move", Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

/*                    int position = viewHolder.getAdapterPosition();
                    Log.e(logeTAG, "onStart"+list_zakaz.get(position).getName());
                    SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
                    db.delete("base_RN", "koduid = ?", new String[]{list_zakaz.get(position).getUID()});
                    db.close();
                    list_zakaz.remove(position);
                    adapterPriceClients.notifyDataSetChanged();
                    Log.e(logeTAG, "onEND"+list_zakaz.get(position).getName());*/


                int pos = viewHolder.getAdapterPosition();
                Log.e(logeTAG, "Direction" + direction);
                Log.e(logeTAG, "position " + pos);
                Log.e(logeTAG, "direction " + direction);
                DialogFragment_DeletePosition dialog = new DialogFragment_DeletePosition();
                Bundle args = new Bundle();
                args.putInt("deletePosition", pos);
                //args.putString("itogo", "");
                dialog.setArguments(args);

                dialog.show(getSupportFragmentManager(), "customDelete");
            }
        };

        mItemTouchHelper = new ItemTouchHelper(simpleCallback);
        mItemTouchHelper.attachToRecyclerView(recycler);
    }


    // Класс для отображения состояния
    protected void SnackbarOverride(String text) {
        Snackbar.make(constraintLayout, text, Snackbar.LENGTH_SHORT)
                .show();
    }


    @Override
    public void onBackPressed() {
        DialogFragment_CloseZakaz dialogFragment = new DialogFragment_CloseZakaz();
        dialogFragment.show(getSupportFragmentManager(), "dialogMessegeOperator");
    }


    // Загрузка данных в адаптер для экрана
    protected void Loading_Adapter() {
        try {
            PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
            String query = "SELECT Kod_RN, koduid, Vrema, Data, Kod_Univ, Name, " +
                    "SUM(Kol) AS 'Kol', Cena, SUM(Summa) AS 'Summa', Skidka, Image, Cena_SK, " +
                    "SUM(Itogo) AS 'Itogo' " +
                    "FROM '" + tableNameisSelect() + "' " +
                    "WHERE Kod_RN = '" + wCodeOrder + "' " +
                    "GROUP BY Kod_Univ;";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                while (!cursor.isAfterLast()) {
                    String kodUniv = cursor.getString(cursor.getColumnIndexOrThrow("Kod_Univ"));
                    String kodUID = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                    String Count = cursor.getString(cursor.getColumnIndexOrThrow("Kol"));
                    String cena = cursor.getString(cursor.getColumnIndexOrThrow("Cena"));
                    String summa = cursor.getString(cursor.getColumnIndexOrThrow("Summa"));
                    String skidka = cursor.getString(cursor.getColumnIndexOrThrow("Skidka"));
                    String image = cursor.getString(cursor.getColumnIndexOrThrow("Image"));
                    String cena_sk = (cursor.getString(cursor.getColumnIndexOrThrow("Cena_SK")));
                    String itogo = cursor.getString(cursor.getColumnIndexOrThrow("Itogo"));
                    String formatPriceSale;
                    if (Double.parseDouble(cena_sk) > 0 | !cena_sk.equals(null)) {
                        formatPriceSale = new DecimalFormat("#00.00").format(Double.parseDouble(cena_sk));
                    } else formatPriceSale = "";
                    AvtoSumma();
                    list_zakaz.add(new ListAdapterSimple_WJ_Zakaz(name, kodUID, kodUniv, Count, cena, formatPriceSale, summa, skidka, itogo, image));
                    cursor.moveToNext();
                }
            } else
                AvtoSumma();

            cursor.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка обработка адаптера", Toast.LENGTH_SHORT).show();
            Log.e(logeTAG, "Ошибка обработка адаптера");
        }

    }

    //// Выбор таблицы для работы с заказои(Новый или изменение)
    protected String tableNameisSelect() {
        String tableName;
        if (preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getStatusOrder()).equals("Edit")) {
            tableName = "base_RN_Edit";
        } else tableName = "base_RN";
        return tableName;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        String id_st = item.getTitle().toString();
        editor = mSettings.edit();
        editor.putString("sp_BREND", id_st);
        Log.e("NextActivity", "SelectBrends= " + id_st);
        editor.commit();
        Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza_L2_Sub.class);
        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_nomenclature);
        drawer.closeDrawer(GravityCompat.START);
        Log.e("NextActivity", "Переход_01");
        return true;
    }


    protected void Write_Table_RN(final String koduniv, final String name, final String kol, final String cena, final String summa, final String skidka, final Double summaSK) {

        // Calendare_This_Data();
        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db_up = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        CalendarThis calendarThis = new CalendarThis();

        String query = "SELECT Kod_RN, Kod_Univ " +
                "FROM '" + tableNameisSelect() + "' WHERE Kod_RN = '" + wCodeOrder + "'";

        Cursor cursor = db_up.rawQuery(query, null);
        ContentValues localContentValuesUP = new ContentValues();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String kod_univ = cursor.getString(cursor.getColumnIndexOrThrow("Kod_Univ"));
            String kod_rn = cursor.getString(cursor.getColumnIndexOrThrow("Kod_RN"));
            Log.e("Update_Ostatok=", kod_univ + ", " + koduniv);
            Log.e("Update_Ostatok=", wCodeOrder + ", " + kod_rn);

            if (kod_univ.equals(koduniv) & wCodeOrder.equals(kod_rn)) {
                Log.e("IFELSE=", "Yes" + wCodeOrder + ", " + kod_rn + kod_univ + ", " + koduniv);
                localContentValuesUP.put("Kod_RN", wCodeOrder);
                localContentValuesUP.put("Vrema", calendarThis.getThis_DateFormatVrema);
                localContentValuesUP.put("Data", calendarThis.getThis_DateFormatSqlDB);
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

                db_up.update(tableNameisSelect(), localContentValuesUP, "Kod_RN = ? AND Kod_Univ = ?", new String[]{kod_rn, kod_univ});
                //    db_up.update("base_RN", localContentValuesUP, "Kod_RN = ? AND Kod_Univ = ?", arrayOfString);

                cursor.moveToNext();
            } else cursor.moveToNext();

        }

        cursor.close();
        db_up.close();
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


    // Константы для чтения
    protected void Constanta_Read() {
        mSettings = getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.getBoolean("key_SwitchPreference_BrendsAll", true) == true) {
            pref_checkbox_brends = true;
            Log.e("check_box_brends", "TR? Бренда, заводские: ");
        } else {
            pref_checkbox_brends = false;
            Log.e("check_box_brends", "FL, Бренда, пользоы ");
        }

        Set<String> hs = mSettings.getStringSet("key_MultiSelectPreference_BrendsManual", new HashSet<>());
        Log.e("Настройки: ", "Бренды: = (" + mSettings.getStringSet("key_MultiSelectPreference_BrendsManual", new HashSet<>()) + ")");
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

/*    protected void SQL_Sklad_UID_ReWrite(String w_uid) {
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
    }*/

    //// Автосумма товара по номеру накладной
    protected void AvtoSumma() {
        textView_Summa = findViewById(R.id.textView_this_summa);
        textView_Count = findViewById(R.id.textView_this_count);
        textView_Itogo = findViewById(R.id.textView_this_itog);
        textView_Summa.setText("");
        textView_Count.setText("");
        textView_Itogo.setText("");

/*        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db = context_Activity.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String querySumma = "SELECT Kod_RN, SUM(Summa) AS 'AvtoSum' FROM '" + tableNameisSelect() + "' WHERE Kod_RN = '" + wCodeOrder + "';";
        String queryCount = "SELECT Kod_RN, SUM(Kol) AS 'AvtoCount' FROM '" + tableNameisSelect() + "' WHERE Kod_RN = '" + wCodeOrder + "';";
        String queryItogo = "SELECT Kod_RN, SUM(Itogo) AS 'AvtoItogo' FROM '" + tableNameisSelect() + "' WHERE Kod_RN = '" + wCodeOrder + "';";
        Cursor cursor = db.rawQuery(querySumma, null);
        //// Подсчет общей суммы
        cursor.moveToFirst();
        Log.e(logeTAG, "count: " + cursor.getCount());
        if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndexOrThrow("AvtoSum")) != null) {
            String s = cursor.getString(cursor.getColumnIndexOrThrow("AvtoSum"));
            textView_Summa.setText(String.valueOf(Double.parseDouble(s)));
        } else textView_Summa.setText("00.00");
        cursor.close();

        //// Подсчет общего кол-ва
        cursor = db.rawQuery(queryCount, null);
        cursor.moveToFirst();
        Log.e(logeTAG, "count: " + cursor.getCount());
        if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndexOrThrow("AvtoCount")) != null) {
            textView_Count.setText(cursor.getString(cursor.getColumnIndexOrThrow("AvtoCount")) + " шт");
        } else textView_Count.setText("0");

        //// Подсчет общего суммы со скидкой
        cursor = db.rawQuery(queryItogo, null);
        cursor.moveToFirst();
        Log.e(logeTAG, "countIt: " + cursor.getCount());
        if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndexOrThrow("AvtoItogo")) != null) {
            String s = cursor.getString(cursor.getColumnIndexOrThrow("AvtoItogo"));
            textView_Itogo.setText(String.valueOf(Double.parseDouble(s)));
        } else textView_Itogo.setText("00.00");

        cursor.close();
        db.close();*/

        preferencesMtSetting = new Preferences_MTSetting();
        for (Map.Entry<String, String> map : preferencesMtSetting.getAutoSum(context_Activity).entrySet()) {
            if (map.getKey().equals("sum")) textView_Summa.setText(map.getValue());
            if (map.getKey().equals("count")) textView_Count.setText(map.getValue());
            if (map.getKey().equals("itg")) textView_Itogo.setText(map.getValue());
        }

    }

    //// Форматирование строки в формат 00.00
    protected String formatDoubleToString(double text) {
        return new DecimalFormat("#00.00").format(text).replaceAll(",", ".");
    }


    /////////////////////////////////////////   РАБОТА С ИНТЕРФЕЙСОМ    //////////////////////////////////////

    @Override
    public void restartAdapter(boolean status) {
        preferencesWrite = new PreferencesWrite(context_Activity);
        if (status) {
            Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza.class);
            intent.putExtra("preferenceSave", "clear");
            startActivity(intent);
            finish();
        }
    }

    //// Функция возврата значениядля удаления
    @Override
    public void restartAdapterDeletePosition(boolean statusAdapter, int selectPosition) {
        try {
            if (statusAdapter) {
                Log.e(logeTAG, "УДАЛЕНИЕ");
                PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
                //int position = viewHolder.getAdapterPosition();

                SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
                db.delete(tableNameisSelect(), "koduid = ?", new String[]{list_zakaz.get(selectPosition).getUID()});
                db.close();
                list_zakaz.remove(selectPosition);
            } else {
                Log.e(logeTAG, "ОТМЕНА УДАЛЕНИЯ");
            }
            adapterPriceClients.notifyDataSetChanged();
            AvtoSumma();
        } catch (Exception e) {
            Log.e(logeTAG, "ОТМЕНА УДАЛЕНИЯ");
            SnackbarOverride("Ошибка, удаления позиции");
        }


    }

    @Override
    public void deleteThisActivity(boolean statusActivity) {

    }

    //// Функция возврата редактирование кол-ва
    @Override
    public void restartAdapterEditPosition(boolean statusAdapter, int selectPosition, Class_contentData contentData) {
        try {
            if (statusAdapter) {
                Log.e(logeTAG, "Изменено кол-во");
                PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
                //int position = viewHolder.getAdapterPosition();
                SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);

                ContentValues contentValues = new ContentValues();
                int price = Integer.parseInt(list_zakaz.get(selectPosition).getCena());
                int sale = Integer.parseInt(list_zakaz.get(selectPosition).getSkidka());
                int sum = contentData.count * price;

                list_zakaz.get(selectPosition).setCount(String.valueOf(contentData.count));
                list_zakaz.get(selectPosition).setSumma(String.valueOf(sum));
                contentValues.put("Kol", contentData.count);
                contentValues.put("Summa", formatDoubleToString(sum));

/*                Log.e(logeTAG, "mySum: " + sum);
                Log.e(logeTAG, "mySumForma: " + formatDoubleToString(sum));*/

                if (sale == 0) {
                    //// Изменение в карточке товара
                    list_zakaz.get(selectPosition).setItogo(String.valueOf(sum));
                    contentValues.put("Itogo", formatDoubleToString(sum));
                } else {
                    double priceForSale_double, itogo_double;
                    priceForSale_double = price - (price * ((double) sale / 100));
                    itogo_double = sum - (sum * ((double) sale / 100));

                    list_zakaz.get(selectPosition).setItogo(formatDoubleToString(itogo_double));
                    contentValues.put("Cena_SK", formatDoubleToString(priceForSale_double));
                    contentValues.put("Itogo", formatDoubleToString(itogo_double));
                }

                //// Запись в базу данных
                db.update(tableNameisSelect(), contentValues, "koduid = ? AND Kod_RN = ?",
                        new String[]{contentData.uid, wCodeOrder});
                adapterPriceClients.notifyItemChanged(selectPosition, 0);
                db.close();

                AvtoSumma();
            } else {
                Log.e(logeTAG, "ОТМЕНА Изменено");
                //   adapterPriceClients.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e(logeTAG, "ОТМЕНА УДАЛЕНИЯ");
            SnackbarOverride("Ошибка, изменения кол-во");
        }


    }
}
