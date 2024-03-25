package kg.roman.Mobile_Torgovla.FormaZakazaStart;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.leinardi.android.speeddial.SpeedDialActionItem;

import org.xmlpull.v1.XmlSerializer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_List_RN_Table;
import kg.roman.Mobile_Torgovla.FormaZakazaSelectClient.WJ_Forma_Zakaza_L1;
import kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar.DialogFragment_DeletePosition;
import kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar.RemovableDeletePosition;
import kg.roman.Mobile_Torgovla.MT_FTP.CalendarThis;
import kg.roman.Mobile_Torgovla.MT_FTP.PreferencesWrite;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.Work_Journal.WJ_Global_Activity;
import kg.roman.Mobile_Torgovla.databinding.WjFormaZakazaBinding;

public class WJ_Forma_Zakaza extends AppCompatActivity
        implements RemovableStatusZakaz, RemovableMessege, RemovableStatusFilter, RemovableStatusFilterClient, RemovableDeletePosition {
    public ArrayList<ListAdapterSimple_List_RN_Table> listZakaz = new ArrayList<>();
    public RecyclerView_Adapter_ViewHolder_Zakaz adapterZakaz;
    public XmlSerializer serializer;

    ////////////  02.2024
    public WjFormaZakazaBinding binding;
    String logeTAG = "WjFormaZakaza";
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    CalendarThis calendars = new CalendarThis();
    PreferencesWrite preferencesWrite;
    public Context context_Activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = WjFormaZakazaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context_Activity = getApplication().getBaseContext();
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.icon_toolbar_forma_zakaz);
        // getSupportActionBar().setTitle("Форма заказа");
        getSupportActionBar().setSubtitle(calendars.CalendarDayOfWeek().second + " " + calendars.getThis_DateFormatDisplay);
        mSettings = getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        editor = mSettings.edit();
        editor.putString("PEREM_WORK_DISTR", calendars.CalendarDayOfWeek().first);  // обновление записи рабочей даты
        editor.commit();

        ///////////////// 02.2024

        //// Очистка всех фильтров
        clearPreferenceFilters();

        //// Проверка на создание таблиц для группировки брендов
        createTableforBrends();

        // Создание меню кнопки FAB
        createNewSpeedDial();

        //// Обновление данных в list через Swipe
        swipeRefreshLayoutList();

        //// Загрузить данные в List
        UpdateListView();

        //// Обработка нажатия на list
        // SelectedListView();

        binding.RecyclerViewListTovar.setOnClickListener(v -> ToastOverride("В разработке, доступно с версии 4.6.3"));


        //// Удаление позиуии
        swipeDeletePosition();

    }

    //////////////////////////////////////////////////// 02.2024
    //// Создание меню менеджер
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_forma_rn, menu);
        return true;
    }

    //// Работа с пунктами меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        try {
            switch (id) {
                //// Сгрупировать заказы по имени и сумме
                case R.id.menu_filter_toGroup: {
                    editor = mSettings.edit();
                    editor.putBoolean("setting_Filters_Group", true);   // состояние фильтра по клиентам
                    editor.commit();
                    UpdateListView();
                }
                break;
                //// Фильтрация данных по дате
                case R.id.menu_filter_toDate: {
                    DialogFragment_FilterToDate dialog = new DialogFragment_FilterToDate();
                    dialog.show(getSupportFragmentManager(), "customFilterDate");
                }
                break;
                //// Фильтрация данных по клиенту
                case R.id.menu_filter_toClietns: {
                    DialogFragment_FilterToClients dialog = new DialogFragment_FilterToClients();
                    dialog.show(getSupportFragmentManager(), "customFilterClients");
                }
                break;
                //// Сброс всех фильтров
                case R.id.menu_filter_toClear: {
                    SnackbarOverride("все фильтры сброшены");
                    clearPreferenceFilters();
                    UpdateListView();
                }
                break;
            }

        } catch (Exception e) {
            SnackbarOverride("Ошибка, фильтрации данных");
            Log.e(logeTAG, "Ошибка, фильтрации данных, " + e);
        }
        return super.onOptionsItemSelected(item);
    }

    //// Обработка перезодания экрана
    @Override
    protected void onResume() {
        super.onResume();
        try {
            Log.e(logeTAG, "onResume");
            UpdateListView();
        } catch (Exception e) {
            Log.e(logeTAG, "Ошибка: onResume, " + e);
        }
    }


    // кнопка назад
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent_prefActivity = new Intent(context_Activity, WJ_Global_Activity.class);
        startActivity(intent_prefActivity);
        finish();
    }


    //// Очистка всех фильтров
    protected void clearPreferenceFilters() {
        ///// Очистка всех фильтров
        editor = mSettings.edit();
        editor.putBoolean("setting_Filters_Group", false);                          // очистка сгрупировки данных
        editor.putBoolean("setting_Filters_Cliets", false);                         // очистка состояния фильтра по клиентам
        editor.putString("setting_Filters_Cliets_Name", null);                      // очистка параметров слиента
        editor.putString("setting_Filters_Cliets_NameUID", null);                   // очистка параметров слиента-UID
        editor.putBoolean("setting_Filters_Date", false);                           // очистка состояние фильтра по дате
        editor.putString("setting_Filters_DateStart", null);                        // очистка начальной даты
        editor.putString("setting_Filters_DateEnd", null);                          // очистка конечной даты
        editor.commit();
    }

    //// Обновление данных в list через Swipe
    protected void swipeRefreshLayoutList() {
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swiperefreshListView);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            //// Загрузить данные в List
            UpdateListView();
            swipeRefreshLayout.setRefreshing(true);
            // ждем 2 секунды и прячем прогресс
            new Handler().postDelayed(() -> {
                // Отменяем анимацию обновления
                swipeRefreshLayout.setRefreshing(false);
                // говорим о том, что собираемся закончить
            }, 2000);

        });
    }


    // Создание нового меню вместа floatingActionButton
    protected void createNewSpeedDial() {
        List<SpeedDialActionItem> speedDialList = new ArrayList<>();

        speedDialList.add(new SpeedDialActionItem.Builder(R.id.speedDial_Messege, R.drawable.icons_speedial_messege)
                .setLabel(getString(R.string.speedDial_Text_Messege))
                .setLabelColor(Color.BLACK)
                .setLabelBackgroundColor(getResources().getColor(R.color.SeedBarBackground))
                .setFabImageTintColor(Color.WHITE)
                .create());
        speedDialList.add(new SpeedDialActionItem.Builder(R.id.speedDial_forXMLOtchet, R.drawable.icons_speedial_report)
                .setLabel(getString(R.string.speedDial_Text_forXMLOtchet))
                .setLabelColor(Color.BLACK)
                .setLabelBackgroundColor(getResources().getColor(R.color.SeedBarBackground))
                .setFabImageTintColor(Color.WHITE)
                .create());
        speedDialList.add(new SpeedDialActionItem.Builder(R.id.speedDial_forAvtoSumma, R.drawable.icons_speedial_sigma)
                .setLabel(getString(R.string.speedDial_Text_forAvtoSumma))
                .setLabelColor(Color.BLACK)
                .setLabelBackgroundColor(getResources().getColor(R.color.SeedBarBackground))
                .setFabImageTintColor(Color.WHITE)
                .create());
        speedDialList.add(new SpeedDialActionItem.Builder(R.id.speedDial_Sale, R.drawable.icons_speedial_sale)
                .setLabel(getString(R.string.speedDial_Text_Sale))
                .setLabelColor(Color.BLACK)
                .setLabelBackgroundColor(getResources().getColor(R.color.SeedBarBackground))
                .setFabImageTintColor(Color.WHITE)
                .create());
        speedDialList.add(new SpeedDialActionItem.Builder(R.id.speedDial_Create, R.drawable.icons_speedial_create)
                .setLabel(getString(R.string.speedDial_Text_Create))
                .setLabelColor(Color.BLACK)
                .setLabelBackgroundColor(getResources().getColor(R.color.SeedBarBackground))
                .setFabImageTintColor(Color.WHITE)
                .create());

        binding.FormaSpeedDial.addAllActionItems(speedDialList);

        binding.FormaSpeedDial.setOnActionSelectedListener(actionItem -> {
            switch (actionItem.getId()) {
                /// Создать новый заказ
                case R.id.speedDial_Create: {
                    ToastOverride("Новый заказ");
                    Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza_L1.class);
                    startActivity(intent);
                    finish();
                }
                break;
                /// Создать новый заказ по акции, доступен в версии 4.6.3
                case R.id.speedDial_Sale: {
                    ToastOverride("В разработке(доступно c версии 4.6.3), не доступен для вашего региона");
                }
                break;
                /// Отправить отчет супервайзеру, доступен в версии 4.6.3
                case R.id.speedDial_forXMLOtchet: {
                    ToastOverride("В разработке(доступно c версии 4.6.3), отчет для супервайзера");

/*                    File dowl = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    //  Uri uriToImage = Uri.parse("android.resource://your.app.package/" + R.raw.playcat);
                    Log.e(logeTAG, "Create: " + dowl.getPath() + "/myPDF.pdf");*/


///                    File file = new File(Environment.getExternalStorageDirectory().toString() + "/Price/XML/myPDF.pdf");

/*                    File files1 = new File(getApplication().getFilesDir().getAbsolutePath());
                    String file_files1 = files1.getPath() + "/myPDF.pdf";
                    Uri URIS = Uri.parse("kg.roman.Mobile_Torgovla/files/myPDF.pdf");

                    Log.e(logeTAG, "CreateImage: " + URIS);

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setType("application/pdf");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, URIS);
                    startActivity(Intent.createChooser(shareIntent, "Отчет за день"));*/
                }
                break;
                /// Выгрузка заказа оператору
                case R.id.speedDial_Messege: {
                    //ToastOverride("Отпра заказа оператору!");
                    binding.FormaProgressBar.setVisibility(View.VISIBLE);
                    Log.e(logeTAG, "Старт ProgressBar");
                    DialogFragment_MessegeOperator dialog = new DialogFragment_MessegeOperator();
                    Bundle args = new Bundle();
                    args.putString("selectMessege", "Zakaz");
                    dialog.setArguments(args);
                    dialog.show(getSupportFragmentManager(), "dialogMessegeOperator");
                }
                break;
                /// Отправить отчет подсчет автосуммы за день
                case R.id.speedDial_forAvtoSumma: {
                    ToastOverride("Общая сумма за день или период");
                    DialogFragment_MessegeOperator dialog = new DialogFragment_MessegeOperator();
                    Bundle args = new Bundle();
                    args.putString("selectMessege", "AvtoSum");
                    dialog.setArguments(args);
                    dialog.show(getSupportFragmentManager(), "dialogMessegeOperator");
                }
                break;
            }
            return false;
        });
    }

    protected void createTableforBrends() {
        try {
            Log.e(logeTAG, "Проверка на сущ. таблиц");
            preferencesWrite = new PreferencesWrite(context_Activity);
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_BASE, MODE_PRIVATE, null);
            SQLiteDatabase dbConst = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_CONST, MODE_PRIVATE, null);

            db.execSQL("CREATE TABLE IF NOT EXISTS write_group_brends (brends_name TEXT NOT NULL, brends_kod TEXT NOT NULL, brends_prefix TEXT, brends_group TEXT);");
            db.execSQL("CREATE TABLE IF NOT EXISTS write_group_name (group_id TEXT NOT NULL, group_name TEXT, group_parent TEXT);");

            Cursor cursor = db.rawQuery("SELECT * FROM write_group_brends", null);
            if (cursor.getCount() == 0) {
                Cursor cursor1 = dbConst.rawQuery("SELECT * FROM const_group_brends", null);
                ContentValues contentValues = new ContentValues();
                cursor1.moveToFirst();
                while (!cursor1.isAfterLast()) {
                    String brends_name = cursor1.getString(cursor1.getColumnIndexOrThrow("brends_name"));
                    String brends_kod = cursor1.getString(cursor1.getColumnIndexOrThrow("brends_kod"));
                    String brends_prefix = cursor1.getString(cursor1.getColumnIndexOrThrow("brends_prefix"));
                    String brends_group = cursor1.getString(cursor1.getColumnIndexOrThrow("brends_group"));
                    contentValues.put("brends_name", brends_name);
                    contentValues.put("brends_kod", brends_kod);
                    contentValues.put("brends_prefix", brends_prefix);
                    contentValues.put("brends_group", brends_group);
                    db.insert("write_group_brends", null, contentValues);
                    cursor1.moveToNext();
                }
                cursor1.close();
            }
            cursor.close();

            cursor = db.rawQuery("SELECT * FROM write_group_name", null);
            Log.e(logeTAG, "getCount(): " + cursor.getCount());
            if (cursor.getCount() == 0) {
                String queryINSERT = "INSERT INTO write_group_name (group_id, group_name, group_parent) \n" +
                        "VALUES \n" +
                        "('gr1','TradeGof','TradeGof'), \n" +
                        "('gr2','Family Distrb','TradeGof'), \n" +
                        "('gr3','Sunbell','Sunbell'),\n" +
                        "('gr4','ЛАВР','Sunbell'),\n" +
                        "('new','новый бренд','no brends')";
                db.execSQL(queryINSERT);
            }
            cursor.close();
            dbConst.close();
            db.close();

        } catch (SQLiteException sqLiteException) {
            Log.e(logeTAG, "ошибка SQLITE: " + sqLiteException);
        } catch (Exception e) {
            Log.e(logeTAG, "таблица уже существует" + e);
        }


    }

    public String CreateNameFile_BackUp(String stringName) {
        char[] chars_rus = {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ч', 'ц', 'ш', 'щ', 'э', 'ю', 'я', 'ы', 'ъ', 'ь'};
        String[] string_eng = {"a", "b", "v", "g", "d", "e", "io", "zh", "z", "i", "i", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ch", "c", "sh", "csh", "e", "ju", "ja", "i", "", ""};
        StringBuilder newName = new StringBuilder();
        StringBuilder newNameNumber = new StringBuilder();
        if (!stringName.isEmpty() || !stringName.equals(" ")) {
            String newAgentName = stringName.toLowerCase().replaceAll(" ", "_");
            for (Character agent : newAgentName.toCharArray()) {
                for (int i = 0; i < chars_rus.length; i++)
                    if (chars_rus[i] == agent)
                        newName.append(string_eng[i]);
                if (Character.isDigit(agent))
                    newNameNumber.append(agent);
                if (agent.toString().matches("[a-z]"))
                    newName.append(agent);
                if (agent == '_')
                    newName.append("_");
            }
            newName.append(newNameNumber);
        } else {
            Log.e("CreateNameFile_BackUp", "не возможно создать строку, не верный формат данных");
            // Snackbar.make(binding.dbBaseRecyclerView, "не возможно создать строку, не верный формат данных", Snackbar.LENGTH_SHORT).show();
            //String string1 = stringName.replaceFirst(" ", "_").replaceAll(" ", "");
            //  String stringWork = stringName.toLowerCase().replaceAll("[^a-zа-я0-9]", "");
        }

        editor = mSettings.edit();
        editor.putString("setting_NameFileToFTP", newName.toString());   // состояние фильтра по клиентам
        editor.commit();

        return newName.toString();
    }

    // Обработка нажатия на ListView
    protected void SelectedListView() {
/*        binding.FormaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                local_text_kod_rn = ((TextView) view.findViewById(R.id.tvw_rn_RN)).getText().toString();
                local_text_client_uid = ((TextView) view.findViewById(R.id.tvw_rn_K_UID)).getText().toString();
                local_text_debet = ((TextView) view.findViewById(R.id.tvw_rn_debet)).getText().toString();
                local_text_itogo = ((TextView) view.findViewById(R.id.tvw_rn_Itogo)).getText().toString();
                local_text_data = ((TextView) view.findViewById(R.id.tvw_rn_Data)).getText().toString();
                local_text_status_rn = ((TextView) view.findViewById(R.id.text_status)).getText().toString();
                local_text_skidka_rn = ((TextView) view.findViewById(R.id.tvw_rn_skidka)).getText().toString();


                builder = new AlertDialog.Builder(context_Activity);
                builder.setTitle("Выберите тип операции...");
                builder.setIcon(R.drawable.office_title_forma_zakaz);
                dialo_forma.clear();
                mass_dialog = getResources().getStringArray(R.array.mass_for_dialof_forma);

                for (int i = 0; i < mass_dialog.length; i++) {
                    dialo_forma.add(new ListAdapterSimple_Dialog_Forma(mass_dialog[i], ""));
                }
                adapterPriceClients_dialog_forma = new ListAdapterAde_Dialog_Forma(context_Activity, dialo_forma);
                adapterPriceClients_dialog_forma.notifyDataSetChanged();

                builder.setAdapter(adapterPriceClients_dialog_forma, (dialog, which) -> {
                    Log.e("Tag", "Selected item on position " + mass_dialog[which]);
                    switch (mass_dialog[which]) {
                        case "Просмотр": {
                            Intent intent = new Intent(context_Activity, WJ_Dialog_RN_Screen.class);
                            intent.putExtra("RN", local_text_kod_rn);
                            intent.putExtra("Data", local_text_data);
                            editor = mSettings.edit();
                            editor.putString("PEREM_READ_KODRN", local_text_kod_rn);  // передача кода конечной даты
                            editor.putString("PEREM_TYPE_MENU", "Просмотр");  // передача кода конечной даты
                            editor.commit();
                            startActivity(intent);
                            finish();
                        }
                        break;
                        case "Редактировать": {
                            try {
                                if (local_text_status_rn.equals("false")) {
                                    Intent intent = new Intent(context_Activity, WJ_Dialog_RN_Edit.class);
                                    intent.putExtra("ReWrite_RN", local_text_kod_rn);
                                    intent.putExtra("ReWrite_Summa", local_text_itogo);
                                    intent.putExtra("ReWrite_Summa_Debet", local_text_debet);
                                    intent.putExtra("ReWrite_Skidka", local_text_skidka_rn);
                                    intent.putExtra("ReWrite_Client_UID", local_text_client_uid);
                                    editor = mSettings.edit();
                                    editor.putString("PEREM_READ_KODRN", local_text_kod_rn);  // передача кода конечной даты
                                    editor.putString("PEREM_TYPE_MENU", "Редактировать");  // передача кода конечной даты
                                    editor.commit();
                                    startActivity(intent);
                                    finish();
                                } else
                                    SnackbarOverride("редактирование не возможно, данные уже в обработке, обратитесь к оператору!");
                            } catch (Exception e) {
                                Log.e(logeTAG, "Ошибка: ошибка пункта редактирования!");
                                SnackbarOverride("Ошибка: ошибка пункта редактирования!");
                            }


                        }
                        break;
                        case "Удалить": {
                            try {
                                if (local_text_status_rn.equals("false")) {
                                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context_Activity);
                                    builder.setMessage("Удалить текущий заказ?")
                                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id1) {
                                                    Delete_RN_BASE(local_text_kod_rn);
                                                    zakaz.clear();
                                                    UpdateListView();
                                                    adapterPriceClients_RN = new ListAdapterAde_List_RN_Table(WJ_Forma_Zakaza.this, zakaz);
                                                    adapterPriceClients_RN.notifyDataSetChanged();
                                                    binding.FormaList.setAdapter(adapterPriceClients_RN);
                                                    Debet_ReWrite(local_text_client_uid, local_text_itogo);
                                                }
                                            })
                                            .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id1) {
                                                }
                                            });
                                    builder.show();
                                } else
                                    SnackbarOverride("удаление не возможно, данные уже в обработке, обратитесь к оператору!");
                            } catch (Exception e) {
                                Log.e(logeTAG, "Ошибка: ошибка удаления данных!");
                                SnackbarOverride("Ошибка: ошибка удаления данных!");
                            }

                        }
                        break;
                    }
                });
                dialog = builder.create();
                dialog.show();

            }
        });*/
    }

    // Класс для отображения состояния: нижняя строка
    protected void SnackbarOverride(String text) {
        Snackbar.make(binding.FormaConstraintLayout, text, Snackbar.LENGTH_SHORT)
                .show();
    }

    // Класс для отображения состояния: всплывающее окно
    protected void ToastOverride(String text) {
        Toast.makeText(context_Activity, text, Toast.LENGTH_SHORT).show();
    }


    /// Обработка нажатия на List
    protected RecyclerView_Adapter_ViewHolder_Zakaz.OnStateClickListener clickableButtonList() {
        RecyclerView_Adapter_ViewHolder_Zakaz.OnStateClickListener stateClickListener = (clientClick, position) -> {
            // ToastOverride("В разработке, доступно с версии 4.6.3");
            ToastOverride("В разработке, доступно с версии 4.6.3");
        };
        return stateClickListener;
    }


    //// Загрузка List
    protected void UpdateListView() {
        listZakaz.clear();
        Loading_List_Filter();
        adapterZakaz = new RecyclerView_Adapter_ViewHolder_Zakaz(getBaseContext(), listZakaz, clickableButtonList());
        binding.RecyclerViewListTovar.setAdapter(adapterZakaz);
        //  adapter.notifyItemChanged(0, 0);
        adapterZakaz.notifyDataSetChanged();
    }






/*    preferencesWrite = new PreferencesWrite(context_Activity);
        if (!preferencesWrite.Setting_FiltersSelectGroup) {
*//*           zakaz.clear();
           Log.e(logeTAG, "Group Null" + preferencesWrite.Setting_FiltersSelectGroup);
           Loading_List_Filter();
           adapterPriceClients_RN = new ListAdapterAde_List_RN_Table(context_Activity, zakaz);
           binding.RecyclerViewListTovar.setAdapter(adapterPriceClients_RN);
           adapterPriceClients_RN.notifyDataSetChanged();*//*



    }

    *//*       else {
               zakaz.clear();
               Log.e(logeTAG, "Group UP" + preferencesWrite.Setting_FiltersSelectGroup);
               Loading_List_FilterGroup();
               adapterPriceClients_RN = new ListAdapterAde_List_RN_Table(context_Activity, zakaz);
               binding.FormaList.setAdapter(adapterPriceClients_RN);
               adapterPriceClients_RN.notifyDataSetChanged();
           }*/


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
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {

              //  return super.getSwipeThreshold(viewHolder);
                return 0.7f;
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
                Log.e(logeTAG, "position " + pos);
                Log.e(logeTAG, "direction " + direction);
                Log.e(logeTAG, "viewHolder " + viewHolder);
                Log.e(logeTAG, "viewHolderView " + viewHolder.itemView.getId());

                DialogFragment_DeletePosition dialog = new DialogFragment_DeletePosition();
                Bundle args = new Bundle();
                args.putInt("deletePosition", pos);
                //args.putString("itogo", "");
                dialog.setArguments(args);

                dialog.show(getSupportFragmentManager(), "zakazDelete");

            }



            @Override
            public void onChildDraw(@NonNull Canvas c,
                                    @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                ColorDrawable mBackground = null;
                mBackground = new ColorDrawable(context_Activity.getResources().getColor(R.color.SeedBarBackground));
                Drawable mIcon = ContextCompat.getDrawable(context_Activity, R.drawable.delete);

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    float width = (float) viewHolder.itemView.getWidth();
                    float alpha = 1.0f - Math.abs(dX) / width;
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);

                    Log.e(logeTAG, "dX " + dX);
                    Log.e(logeTAG, "dY " + dY);
                    Log.e(logeTAG, "isCyr " + isCurrentlyActive);
                    Log.e(logeTAG, "ViewID " + viewHolder.itemView.getId());
                    Log.e(logeTAG, "recyclerView " + recyclerView.getAdapter().getItemCount());
                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                            actionState, isCurrentlyActive);
                }


/*                View itemView = viewHolder.itemView;
                int backgroundCornerOffset = 25; //so mBackground is behind the rounded corners of itemView

                int iconMargin = (itemView.getHeight() - mIcon.getIntrinsicHeight()) / 2;
                int iconTop = itemView.getTop() + (itemView.getHeight() - mIcon.getIntrinsicHeight()) / 2;
                int iconBottom = iconTop + mIcon.getIntrinsicHeight();

                if (dX > 0) { // Swiping to the right
                    int iconLeft = itemView.getLeft() + iconMargin;
                    int iconRight = iconLeft + mIcon.getIntrinsicWidth();
                    mIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                    mBackground.setBounds(itemView.getLeft(), itemView.getTop(),
                            itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
                } else if (dX < 0) { // Swiping to the left
                    int iconLeft = itemView.getRight() - iconMargin - mIcon.getIntrinsicWidth();
                    int iconRight = itemView.getRight() - iconMargin;
                    mIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                    mBackground.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                            itemView.getTop(), itemView.getRight(), itemView.getBottom());
                } else { // view is unSwiped
                    mIcon.setBounds(0, 0, 0, 0);
                    mBackground.setBounds(0, 0, 0, 0);
                }

                mBackground.draw(c);
                mIcon.draw(c);*/


                if (dX < 0) {
                    ///   MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
                  // getDefaultUIUtil().onDraw(c, recyclerView, myViewHolder, dX/4, dY, actionState, isCurrentlyActive);
                }

            }


        };



        mItemTouchHelper = new ItemTouchHelper(simpleCallback);
        mItemTouchHelper.attachToRecyclerView(binding.RecyclerViewListTovar);
    }

    @Override
    public void restartAdapterDeletePosition(boolean statusDeletePosition, int selectPosition) {
        Log.e(logeTAG, "Удаление заказа" + listZakaz.get(selectPosition).getKodrn());
        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        try {
            if (statusDeletePosition) {

                //int position = viewHolder.getAdapterPosition();
                SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
                db.delete("base_RN_All", "kod_rn = ?", new String[]{listZakaz.get(selectPosition).getKodrn()});
                db.delete("base_RN", "Kod_RN = ?", new String[]{listZakaz.get(selectPosition).getKodrn()});
                db.close();

                listZakaz.remove(selectPosition);
                adapterZakaz = new RecyclerView_Adapter_ViewHolder_Zakaz(getBaseContext(), listZakaz, null);
                binding.RecyclerViewListTovar.setAdapter(adapterZakaz);
                adapterZakaz.notifyItemChanged(selectPosition, 0);

            } else {
                Log.e(logeTAG, "ОТМЕНА УДАЛЕНИЯ");
                adapterZakaz = new RecyclerView_Adapter_ViewHolder_Zakaz(getBaseContext(), listZakaz, null);
                binding.RecyclerViewListTovar.setAdapter(adapterZakaz);
                adapterZakaz.notifyItemChanged(selectPosition, 0);
            }


        } catch (Exception e) {
            Log.e(logeTAG, "ОТМЕНА УДАЛЕНИЯ");
            SnackbarOverride("Ошибка: удаления позиции "+e);
        }



/*        try {
            SQLiteDatabase db = context_Activity.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
            String query_delete_RN = "DELETE FROM base_RN WHERE kod_rn = '" +  + "'";
            String query_delete_RN_ALL = "DELETE FROM base_RN_All WHERE kod_rn = '" + name_rn + "'";

            final Cursor cursor_rn = db.rawQuery(query_delete_RN, null);
            cursor_rn.moveToFirst();
            while (cursor_rn.isAfterLast() == false) {
                cursor_rn.moveToNext();
            }
            cursor_rn.close();

            final Cursor cursor_rn_all = db.rawQuery(query_delete_RN_ALL, null);
            cursor_rn_all.moveToFirst();
            while (cursor_rn_all.isAfterLast() == false) {
                cursor_rn_all.moveToNext();
            }
            cursor_rn_all.close();
            db.close();

        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка удаления данных!", Toast.LENGTH_SHORT).show();
            Log.e("Удаление!", "Ошибка удаления данных!");
        }*/

    }


    //// Загрузка List с фильтрами
    protected void Loading_List_Filter() {
        try {
            PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
            Log.e(logeTAG, "Cliets " + preferencesWrite.Setting_FiltersSelectClients);
            Log.e(logeTAG, "Date " + preferencesWrite.Setting_FiltersSelectDate);
            Log.e(logeTAG, "Group " + preferencesWrite.Setting_FiltersSelectGroup);
            String query = "";

            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
            FiltersFormaZakaza filtersFormaZakaza = new FiltersFormaZakaza(context_Activity);

            Log.e(logeTAG, "Loading_List_Filter");


/*            if (preferencesWrite.PEREM_ANDROID_ID_ADMIN.equals("d5781f21963ff5"))
                query = filtersFormaZakaza.filterAdmin();
            else {

            }*/

            //// Данные без фильтров
            query = filtersFormaZakaza.filterNull();
            //// Фильриция по Клиенту
            if (preferencesWrite.Setting_FiltersSelectClients)
                query = filtersFormaZakaza.filterClient();
            //// Фильтрация по дате
            if (preferencesWrite.Setting_FiltersSelectDate)
                query = filtersFormaZakaza.filterDate();
            //// Фильтрицмя по дате и клиентам
            if (preferencesWrite.Setting_FiltersSelectDate && preferencesWrite.Setting_FiltersSelectClients)
                query = filtersFormaZakaza.filterAll();


            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String Kod_RN = cursor.getString(cursor.getColumnIndexOrThrow("kod_rn"));
                String Klients = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_name"));
                String UID_Klients = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_uid"));
                String Adress = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_adress"));
                String Vrema = cursor.getString(cursor.getColumnIndexOrThrow("vrema"));
                String Data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
                String Summa = cursor.getString(cursor.getColumnIndexOrThrow("summa"));
                String Itogo = cursor.getString(cursor.getColumnIndexOrThrow("itogo"));
                String skidka = cursor.getString(cursor.getColumnIndexOrThrow("skidka")); // добавить обработку скидки
                String data_up = cursor.getString(cursor.getColumnIndexOrThrow("data_up")); // добавить обработку дата отгрузки
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status")); // добавить обработку дата отгрузки
                String debet = cursor.getString(cursor.getColumnIndexOrThrow("debet_new")); // добавить обработку дата отгрузки
                String sklad = cursor.getString(cursor.getColumnIndexOrThrow("sklad")); // добавить обработку дата отгрузки
                listZakaz.add(new ListAdapterSimple_List_RN_Table(Kod_RN, Klients, UID_Klients, Vrema, Data, Summa, Itogo, Adress, skidka, debet, status, sklad));
                cursor.moveToNext();
            }
            Log.e(logeTAG, "zakaz обновлен");
            cursor.close();
            db.close();
        } catch (SQLiteException sqLiteException) {
            Log.e(logeTAG, "Loading_List_Filter: " + sqLiteException);
        } catch (Exception ex) {
            Log.e(logeTAG, "Exception: " + ex);
        }
    }

    protected void Loading_List_FilterGroup() {
        try {
            PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
            Log.e(logeTAG, "Cliets " + preferencesWrite.Setting_FiltersSelectClients);
            Log.e(logeTAG, "Date " + preferencesWrite.Setting_FiltersSelectDate);
            Log.e(logeTAG, "Group " + preferencesWrite.Setting_FiltersSelectGroup);

            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
            FiltersFormaZakaza filtersFormaZakaza = new FiltersFormaZakaza(context_Activity);

            Log.e(logeTAG, "Loading_List_FilterGroup: " + filtersFormaZakaza.filterGroup());

            String query = filtersFormaZakaza.filterGroup();

            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String agentName = cursor.getString(cursor.getColumnIndexOrThrow("agent_name"));
                String agentUID = cursor.getString(cursor.getColumnIndexOrThrow("agent_uid"));
                String clientUID = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_uid"));
                String clientName = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_name"));
                String clientAdress = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_adress"));
                String Date = cursor.getString(cursor.getColumnIndexOrThrow("data"));
                String Itogo = cursor.getString(cursor.getColumnIndexOrThrow("itogo"));
                listZakaz.add(new ListAdapterSimple_List_RN_Table("", clientName, clientUID, "", Date, "", Itogo, clientAdress, "", "", "", ""));
                cursor.moveToNext();
            }
            Log.e(logeTAG, "zakaz обновлен");
            cursor.close();
            db.close();
        } catch (SQLiteException sqLiteException) {
            Log.e(logeTAG, "Loading_List_Filter: " + sqLiteException);
        } catch (Exception ex) {
            Log.e(logeTAG, "Exception: " + ex);
        }
    }


    ////////////////  Работа с интерфейсом
    /*                    Bundle args = new Bundle();
                    args.putString("filters", binding.toString());
                    dialog.setArguments(args);*/

    /*        query = "SELECT * FROM base_RN_All\n" +
                "WHERE agent_uid = '" + agentUID + "' " +
                "AND base_RN_All.data BETWEEN '" + calendars.getThis_DateFormatSqlDB + "' " + "AND '" + calendars.getThis_DateFormatSqlDB + "'";*/

    /*            query = "SELECT * FROM base_RN_All\n" +
                    "WHERE agent_uid = '" + agentUID + "'" +
                    "AND k_agn_uid = '" + preferencesWrite.Setting_Filters_Clients_UID + "' " +
                    "AND base_RN_All.data BETWEEN '" + calendars.getThis_DateFormatSqlDB + "' AND '" + calendars.getThis_DateFormatSqlDB + "'";*/

    /*            query = "SELECT * FROM base_RN_All\n" +
                    "WHERE agent_uid = '" + agentUID + "'" +
                    "AND base_RN_All.data BETWEEN '" + preferencesWrite.Setting_Filters_DataStart + "' AND '" + preferencesWrite.Setting_Filters_DataEND + "'";*/
    /*            query = "SELECT * FROM base_RN_All\n" +
                    "WHERE agent_uid = '" + agentUID + "'" +
                    "AND k_agn_uid = '" + preferencesWrite.Setting_Filters_Clients_UID + "' " +
                    "AND base_RN_All.data BETWEEN '" + preferencesWrite.Setting_Filters_DataStart + "' AND '" + preferencesWrite.Setting_Filters_DataEND + "'";*/



/*
    @Override
    public void progressBarBool(boolean statusAdapter) {
        try {
            if (statusAdapter) {
                Log.e(logeTAG, "True1, обнавляем List и вкл ProgressBar");
                CreateDataList();
                binding.FormaProgressBar.setVisibility(View.INVISIBLE);

            } else {
                Log.e(logeTAG, "True2, обнавляем List и откл ProgressBar");
                //  binding.FormaProgressBar.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e(logeTAG, "ОТМЕНА УДАЛЕНИЯ");
            SnackbarOverride("Ошибка, удаления позиции");
        }

    }
*/


    @Override
    public void RemovableProgressBar(boolean progressBarBool) {
        // CreateDataList();
        Log.e(logeTAG, "Perm ProgressBar: " + progressBarBool);
        if (progressBarBool)
            binding.FormaProgressBar.setVisibility(View.VISIBLE);
        else {
            binding.FormaProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void RemovableMessege(String textMessege) {
        Log.e(logeTAG, "Messege: (" + textMessege + ")");
        if (!textMessege.equals("") || textMessege != null)
            ToastOverride(textMessege);
    }

    @Override
    public void RemovableStatusFilter(boolean filterStatus) {
        if (filterStatus) {
            Log.e(logeTAG, "Активирован Фильтр Дата and update ListView");
            UpdateListView();
        }
    }

    @Override
    public void RemovableStatusFilterMessege(String filterMessege) {
        Toast.makeText(context_Activity, filterMessege, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void statusZakaz(boolean status) {
        if (status) {
            Log.e(logeTAG, "Обновляем StatusZakaz and update ListView");
            sqlUpdateStatusZakaz();
        }
    }


    @Override
    public void RemovableStatusFilterClient(boolean filterStatus) {
        if (filterStatus) {
            Log.e(logeTAG, "Активирован Фильтр Клиент and update ListView");
            UpdateListView();
        }
    }


    /////////////////////////////////////////////////////////////////

    protected void Loading_RN_AutoSumma(final String data_start, final String data_end,
                                        final String uid_klient) {
        String
                dialog_summa,
                dialog_Itogo,
                dialog_kol;
        String query_SUMMA = "", query_KolTT = "";
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        Log.e("PARAM ", data_start + " " + data_end + " " + uid_klient);

        if (data_start.equals(data_end) & uid_klient.equals("All")) {
            query_SUMMA = "SELECT base_RN_All.agent_name, SUM(base_RN_All.summa) AS 'summa', SUM(base_RN_All.itogo) AS 'itogo'\n" +
                    "FROM base_RN_All\n" +
                    "WHERE base_RN_All.data = '" + data_end + "'";
            query_KolTT = "SELECT DISTINCT base_RN_All.k_agn_name\n" +
                    "FROM base_RN_All\n" +
                    "WHERE base_RN_All.data = '" + data_end + "'";
        } else if (!data_start.equals(data_end) & uid_klient.equals("All")) {
            Log.e(WJ_Forma_Zakaza.this.getLocalClassName(), "TYPE-2");
            query_SUMMA = "SELECT base_RN_All.agent_name, SUM(base_RN_All.summa) AS 'summa', SUM(base_RN_All.itogo) AS 'itogo'\n" +
                    "FROM base_RN_All \n" +
                    "WHERE base_RN_All.data BETWEEN '" + data_start + "' AND '" + data_end + "'";
            query_KolTT = "SELECT DISTINCT base_RN_All.k_agn_name \n" +
                    "FROM base_RN_All \n" +
                    "WHERE base_RN_All.data BETWEEN '" + data_start + "' AND '" + data_end + "'";
        } else if (data_start.equals(data_end) & !uid_klient.equals("All")) {

            Log.e(WJ_Forma_Zakaza.this.getLocalClassName(), "TYPE-3");
            query_SUMMA = "SELECT base_RN_All.agent_name, SUM(base_RN_All.summa) AS 'summa', SUM(base_RN_All.itogo) AS 'itogo' \n" +
                    "FROM base_RN_All \n" +
                    "WHERE base_RN_All.k_agn_uid = '" + uid_klient + "' AND base_RN_All.data = '" + data_end + "';";

            query_KolTT = "SELECT base_RN_All.agent_name, SUM(base_RN_All.summa) AS 'summa', SUM(base_RN_All.itogo) AS 'itogo' " +
                    "FROM base_RN_All " +
                    "WHERE base_RN_All.k_agn_uid  = '" + uid_klient + "' AND base_RN_All.data = '" + data_end + "';";
        } else if (!data_start.equals(data_end) & !uid_klient.equals("All")) {
            Log.e(WJ_Forma_Zakaza.this.getLocalClassName(), "TYPE-4");
            query_SUMMA = "SELECT base_RN_All.agent_name, SUM(base_RN_All.summa) AS 'summa', SUM(base_RN_All.itogo) AS 'itogo' \n" +
                    "FROM base_RN_All \n" +
                    "WHERE base_RN_All.k_agn_uid = '" + uid_klient + "' AND base_RN_All.data BETWEEN '" + data_start + "' AND '" + data_end + "';";

            query_KolTT = "SELECT base_RN_All.agent_name, SUM(base_RN_All.summa) AS 'summa', SUM(base_RN_All.itogo) AS 'itogo' " +
                    "FROM base_RN_All " +
                    "WHERE base_RN_All.k_agn_uid = '" + uid_klient + "' AND base_RN_All.data BETWEEN '" + data_start + "' AND '" + data_end + "';";
        }

        final Cursor cursor = db.rawQuery(query_SUMMA, null);
        cursor.moveToFirst();
        String Summa = cursor.getString(cursor.getColumnIndexOrThrow("summa"));
        String Itogo = cursor.getString(cursor.getColumnIndexOrThrow("itogo"));
        if (Summa != null) {
            String perem_forma_summa, perem_forma_itogo;
            perem_forma_summa = new DecimalFormat("#00.00").format(Double.parseDouble(Summa));
            perem_forma_itogo = new DecimalFormat("#00.00").format(Double.parseDouble(Itogo));
            dialog_summa = perem_forma_summa;
            dialog_Itogo = perem_forma_itogo;
        }
        cursor.close();
        final Cursor cursor_TT = db.rawQuery(query_KolTT, null);
        cursor_TT.moveToFirst();
        if (cursor_TT.getCount() > 0) {
            dialog_kol = String.valueOf(cursor_TT.getCount());
        }
        cursor_TT.close();
        db.close();
    }

    protected void XML_Array_KodRN(String w_agent_uid, String Data_Start, String Data_End) {

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        /*String query = "SELECT base_RN_All.kod_rn, base_RN_All.k_agn_name, base_RN_All.k_agn_uid, base_RN_All.data\n" +
                "FROM base_RN_All\n" +
                "WHERE base_RN_All.data BETWEEN '" + Data_Start + "' AND '" + Data_End + "';";*/

        String query = "SELECT base_RN_All.kod_rn, base_RN_All.k_agn_name, " +
                "base_RN_All.k_agn_uid, base_RN_All.data, base_RN_All.agent_name, base_RN_All.agent_uid\n" +
                "FROM base_RN_All\n" +
                "WHERE base_RN_All.data BETWEEN '" + Data_Start + "' AND '" + Data_End + "' " +
                "AND base_RN_All.agent_uid = '" + w_agent_uid + "';";
        final Cursor cursor = db.rawQuery(query, null);
        String[] mass_kod_rn = new String[cursor.getCount()];
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String Kod_RN = cursor.getString(cursor.getColumnIndexOrThrow("kod_rn"));
            mass_kod_rn[cursor.getPosition()] = Kod_RN;
            Log.e("Mass ", Kod_RN);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    protected void XML_Array_Name(String Array_kod_rn, String Data_Start, String Data_End) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT base_RN_All.kod_rn, base_RN_All.k_agn_name, base_RN_All.k_agn_uid, base_RN_All.data\n" +
                "FROM base_RN_All\n" +
                "WHERE base_RN_All.kod_rn = '" + Array_kod_rn + "' AND base_RN_All.data BETWEEN '" + Data_Start + "' AND '" + Data_End + "';";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String Kod_RN = cursor.getString(cursor.getColumnIndexOrThrow("kod_rn"));
            String Klients = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_name"));
            String UID_Klients = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_uid"));
            String Data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
            XML_Array(Kod_RN, Klients, UID_Klients, Data);
            // Log.e(this.getLocalClassName(), Kod_RN + "_" + Klients + "_" + UID_Klients + "_" + Data);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    protected void XML_Array(String kod_Rn, String S_name, String uid, String data) {
        try {
            // Log.e(this.getLocalClassName(), kod_Rn + " " + S_name);
            serializer.startTag(null, "Array_Order");
            serializer.startTag(null, "SyncTableNomenclatura");

            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);

            String query_TITLE = "SELECT * FROM base_RN_All WHERE base_RN_All.kod_rn = '" + kod_Rn + "';";
            Cursor cursor = db.rawQuery(query_TITLE, null);
            cursor.moveToFirst();
            String sql_kod_rn = cursor.getString(cursor.getColumnIndexOrThrow("kod_rn"));
            String sql_agent_name = cursor.getString(cursor.getColumnIndexOrThrow("agent_name"));
            String sql_agent_uid = cursor.getString(cursor.getColumnIndexOrThrow("agent_uid"));
            String sql_k_agn_uid = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_uid"));
            String sql_k_agn_name = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_name"));
            String sql_data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
            String sql_data_xml = cursor.getString(cursor.getColumnIndexOrThrow("data_xml"));
            String sql_credit = cursor.getString(cursor.getColumnIndexOrThrow("credit"));
            String sql_sklad = cursor.getString(cursor.getColumnIndexOrThrow("sklad"));
            String sql_sklad_uid = cursor.getString(cursor.getColumnIndexOrThrow("sklad_uid"));
            String sql_cena_price = cursor.getString(cursor.getColumnIndexOrThrow("cena_price"));
            String sql_coment = cursor.getString(cursor.getColumnIndexOrThrow("coment"));
            String sql_uslov_nds = cursor.getString(cursor.getColumnIndexOrThrow("uslov_nds"));
            String sql_skidka_title = cursor.getString(cursor.getColumnIndexOrThrow("skidka_title"));
            String sql_data_credite = cursor.getString(cursor.getColumnIndexOrThrow("credite_date"));

            String sql_rn_data = cursor.getString(cursor.getColumnIndexOrThrow("data_xml"));
            String sql_rn_vrema = cursor.getString(cursor.getColumnIndexOrThrow("vrema"));
            Log.e("String_XML ", sql_data + "_" + sql_data_xml);

            // Заполнение Формы
            serializer.startTag(null, "Nomer_Order");
            serializer.text(sql_kod_rn);
            serializer.endTag(null, "Nomer_Order");

            serializer.startTag(null, "Order_Date");
            serializer.text(sql_rn_data);
            serializer.endTag(null, "Order_Date");

            serializer.startTag(null, "Order_Vrema");
            serializer.text(sql_rn_vrema);
            serializer.endTag(null, "Order_Vrema");

            serializer.startTag(null, "NAME_AG");
            serializer.text(sql_agent_name);
            serializer.endTag(null, "NAME_AG");
            serializer.startTag(null, "UID_AG");
            serializer.text(sql_agent_uid);
            serializer.endTag(null, "UID_AG");
            serializer.startTag(null, "UID_C");
            serializer.text(sql_k_agn_uid);
            serializer.endTag(null, "UID_C");
            serializer.startTag(null, "NAME_C");
            serializer.text(sql_k_agn_name);
            serializer.endTag(null, "NAME_C");
            serializer.startTag(null, "DATA");
            serializer.text(sql_data);
            serializer.endTag(null, "DATA");
            serializer.startTag(null, "CREDIT");
            serializer.text(sql_credit);  // Консигнация, Перечислением, Взаимозачет
            serializer.endTag(null, "CREDIT");
            serializer.startTag(null, "SKLAD");
            serializer.text(sql_sklad);
            serializer.endTag(null, "SKLAD");
            serializer.startTag(null, "UID_SKLAD");
            serializer.text(sql_sklad_uid);
            serializer.endTag(null, "UID_SKLAD");
            serializer.startTag(null, "CENA_PRICE");
            serializer.text(sql_cena_price); // Цена брака, Цена в розницу, Цена для народных
            serializer.endTag(null, "CENA_PRICE");
            serializer.startTag(null, "Coment");
            serializer.text(sql_coment); // Дата отгрузки 04.12.2021; скидка 10%
            serializer.endTag(null, "Coment");
            serializer.startTag(null, "CENA_NDS");
            serializer.text(sql_uslov_nds); // Дата отгрузки 04.12.2021; скидка 10%
            serializer.endTag(null, "CENA_NDS");
            serializer.startTag(null, "SKIDKA");
            serializer.text(sql_skidka_title); // Скидка
            serializer.endTag(null, "SKIDKA");
            serializer.startTag(null, "DNEI_KONSIGN");
            serializer.text(sql_data_credite); // Дней конс
            serializer.endTag(null, "DNEI_KONSIGN");
            cursor.close();

            String query = "SELECT base_RN_All.kod_rn, base_RN_All.k_agn_name, " +
                    "base_RN_All.k_agn_uid, base_RN_All.data, base_RN.Name,  " +
                    "base_RN.koduid, base_RN.Kod_Univ, base_RN.Kol, base_RN.Cena, base_RN.Summa, " +
                    "base_RN.skidka, base_RN.itogo\n" +
                    "FROM base_RN_All\n" +
                    "JOIN base_RN ON base_RN_All.kod_rn=base_RN.Kod_RN\n" +
                    "WHERE base_RN_All.kod_rn = '" + kod_Rn + "'";
            cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                String kod_univ = cursor.getString(cursor.getColumnIndexOrThrow("Kod_Univ"));
                String kol = cursor.getString(cursor.getColumnIndexOrThrow("Kol"));
                String cena = cursor.getString(cursor.getColumnIndexOrThrow("Cena"));
                String summa = cursor.getString(cursor.getColumnIndexOrThrow("Summa"));
                String kod_uid = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));

                // Первая строка заказа
                serializer.startTag(null, "POS");
                serializer.startTag(null, "NAME");
                serializer.text(name);
                Log.e(this.getLocalClassName(), name);
                serializer.endTag(null, "NAME");
                serializer.startTag(null, "NAME_UID");
                serializer.text(kod_uid);
                serializer.endTag(null, "NAME_UID");
                serializer.startTag(null, "KODUNIV");
                serializer.text(kod_univ);
                serializer.endTag(null, "KODUNIV");
                serializer.startTag(null, "KOL_COUNT");
                serializer.text(kol);
                serializer.endTag(null, "KOL_COUNT");
                serializer.startTag(null, "PRICE");
                serializer.text(cena);
                serializer.endTag(null, "PRICE");
                serializer.startTag(null, "SUMMA");
                serializer.text(summa);
                serializer.endTag(null, "SUMMA");
                serializer.endTag(null, "POS");
                // Конец строки
                cursor.moveToNext();
            }

            cursor.close();
            db.close();
            serializer.endTag(null, "SyncTableNomenclatura");
            serializer.endTag(null, "Array_Order");


        } catch (Exception e) {
            Log.e("WJ_END:", "Ошибка: создание отчета!");
            Toast.makeText(context_Activity, "Ошибка: создание отчета!", Toast.LENGTH_SHORT).show();
        }

    }


    // Удаление данных: номер накладной из базы
    protected void Delete_RN_BASE(String name_rn) {
        try {
            SQLiteDatabase db = context_Activity.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
            String query_delete_RN = "DELETE FROM base_RN WHERE kod_rn = '" + name_rn + "'";
            String query_delete_RN_ALL = "DELETE FROM base_RN_All WHERE kod_rn = '" + name_rn + "'";

            final Cursor cursor_rn = db.rawQuery(query_delete_RN, null);
            cursor_rn.moveToFirst();
            while (cursor_rn.isAfterLast() == false) {
                cursor_rn.moveToNext();
            }
            cursor_rn.close();

            final Cursor cursor_rn_all = db.rawQuery(query_delete_RN_ALL, null);
            cursor_rn_all.moveToFirst();
            while (cursor_rn_all.isAfterLast() == false) {
                cursor_rn_all.moveToNext();
            }
            cursor_rn_all.close();
            db.close();

        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка удаления данных!", Toast.LENGTH_SHORT).show();
            Log.e("Удаление!", "Ошибка удаления данных!");
        }
    }

    // Обновление статуса выгрузки данных
    protected void Update_Status(String data_ddb) {
        try {
            SQLiteDatabase db = context_Activity.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
            String query = "UPDATE base_RN_All SET status = 'true' WHERE data = '" + data_ddb + "'";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                cursor.moveToNext();
            }
            cursor.close();
            db.close();

        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка удаления данных!", Toast.LENGTH_SHORT).show();
            Log.e("Удаление!", "Ошибка удаления данных!");
        }
    }


    // Обновление статуса выгрузки данных
    protected void sqlUpdateStatusZakaz() {
        try {
            preferencesWrite = new PreferencesWrite(context_Activity);
            String qrUIDAgent = preferencesWrite.Setting_AG_UID,
                    qrUIDClient = preferencesWrite.Setting_Filters_Clients_UID,
                    qrDataThis = calendars.getThis_DateFormatSqlDB,
                    qrDataStart = preferencesWrite.Setting_Filters_DataStart,
                    qrDataEND = preferencesWrite.Setting_Filters_DataEND;
            String query;
            SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
            if (!preferencesWrite.Setting_FiltersSelectDate)
                if (!preferencesWrite.Setting_FiltersSelectClients)
                    query = "UPDATE base_RN_All SET status = 'true'\n" +
                            "WHERE data = '" + qrDataThis + "';";
                else query = "UPDATE base_RN_All SET status = 'true'\n" +
                        "WHERE k_agn_uid = '" + qrUIDClient + "' AND data = '" + qrDataThis + "';";
            else if (!preferencesWrite.Setting_FiltersSelectClients)
                query = "UPDATE base_RN_All SET status = 'true'\n" +
                        "WHERE data BETWEEN '" + qrDataStart + "' AND '" + qrDataEND + "';";
            else query = "UPDATE base_RN_All SET status = 'true'\n" +
                        "WHERE k_agn_uid = '" + qrUIDClient + "' AND data BETWEEN '" + qrDataStart + "' AND '" + qrDataEND + "';";
            Log.e(logeTAG, "Quert: " + query);


            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
                cursor.moveToNext();
            cursor.close();
            db.close();
            Log.e(logeTAG, "Статус заказа изменен");
            UpdateListView();
        } catch (Exception e) {
            Log.e(logeTAG, "Ошибка удаления данных! " + e);
        }
    }


    // Синхронизация файлов для всех складов Юишкек
/*    private class MyAsyncTask_Sync_New_XML extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            pDialog.setMessage("Создание отчет по заказам. Подождите...");
            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                publishProgress(1);
                getFloor();  // Синхронизация
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
            Log.e("Дата=", calendars.getThis_DateFormatDisplay);
            pDialog.setProgress(0);
            pDialog.dismiss();
            Toast.makeText(context_Activity, "Отчет успешно создан!", Toast.LENGTH_SHORT).show();
            Update_Status(calendars.getThis_DateFormatDisplay);
            onResume();

            // ListAdapet_Internet_Load();
            Mail();
            WJ_Forma_Zakaza.sender_mail_async async_sending = new WJ_Forma_Zakaza.sender_mail_async();
            async_sending.execute();
        }

        private void getFloor() throws InterruptedException {
            pDialog.setMessage("Загрузка продуктов. Подождите...");


            try {
                String str_Android_ID = Settings.Secure.getString(context_Activity.getContentResolver(), Settings.Secure.ANDROID_ID);
                Log.e("Android_ID^ ", Settings.Secure.getString(context_Activity.getContentResolver(), Settings.Secure.ANDROID_ID));
                Log.e("Android_ID^ ", str_Android_ID.substring(0, str_Android_ID.length() - 2));
                mSettings = getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
               *//* if (PEREM_ANDROID_ID.equals(str_Android_ID.substring(0, str_Android_ID.length() - 2))) {
                    Log.e("File", "TT " + sp.getString("PEREM_DIALOG_DATA_START", "0"));

                    if (!sp.getString("PEREM_DIALOG_DATA_START", "0").isEmpty()) {
                        Log.e("File", "не пустой");
                        XML_Data_Start = Summa_Data_Start;
                        XML_Data_End = Summa_Data_End;
                        Log.e("File1", PEREM_AG_UID + "__" + XML_Data_Start + " " + XML_Data_End);
                        XML_Array_KodRN(PEREM_AG_UID, XML_Data_Start, XML_Data_End);

                    } else {
                        XML_Data_Start = this_data_filter;
                        XML_Data_End = this_data_filter;
                        XML_Array_KodRN(PEREM_AG_UID, XML_Data_Start, XML_Data_End);
                        Log.e("File2", XML_Data_Start + " " + XML_Data_End);
                    }

                } else XML_Array_KodRN(PEREM_AG_UID, this_data_filter, this_data_filter);*//*
                // XML_Array_KodRN(this_data_filter, this_data_filter);

               *//* XML_Data_Start = this_data_filter;
                XML_Data_End = this_data_filter;
                XML_Array_KodRN(PEREM_AG_UID, XML_Data_Start, XML_Data_End);
                Log.e("for Array^ ", XML_Data_Start + " " + XML_Data_End);
                Log.e("for Array^ ", Dialog_Data_Start + " " + Dialog_Data_End);*//*
                try {
                    //String name_file = PEREM_NAME_AGENT + "_" + this_data_filter;
                    //String name_file = "NEWuserData";
                    Boolean bool_filter = false;
                    String name_file_sd;
                    Log.e("FileSetting", "Data:" + mSettings.getString("PEREM_DIALOG_DATA_START", "0"));
                    Log.e("FileSetting", "DataStart:" + Summa_Data_Start);
                    Log.e("FileSetting", "DataEnd:" + Summa_Data_End);

                    if (!mSettings.getString("PEREM_DIALOG_DATA_START", "0").isEmpty()) {
                        Log.e("File", "не пустойDaata ");
                        XML_Data_Start = Summa_Data_Start;
                        XML_Data_End = Summa_Data_End;
                        bool_filter = true;
                       // Log.e(logeTAG, "File1: " + PEREM_AG_UID + "__" + XML_Data_Start + " " + XML_Data_End);

                      //  XML_Array_KodRN(PEREM_AG_UID, XML_Data_Start, XML_Data_End);

                    } else {
                        XML_Data_Start = calendars.getThis_DateFormatSqlDB;
                        XML_Data_End = calendars.getThis_DateFormatSqlDB;
                        bool_filter = false;
                       // XML_Array_KodRN(PEREM_AG_UID, XML_Data_Start, XML_Data_End);
                        Log.e(logeTAG, "File2: " + XML_Data_Start + " " + XML_Data_End);
                    }

                    String[] f = {"а", "б", "в", "г", "д", "е", "ё", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п", "р", "с", "т", "у", "ф", "х", "ч", "ц", "ш", "щ", "э", "ю", "я", "ы", "ъ", "ь"};
                    String[] t = {"a", "b", "v", "g", "d", "e", "io", "zh", "z", "i", "i", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ch", "c", "sh", "csh", "e", "ju", "ja", "i", "", ""};

                    String res = "";
                   // String name_old = PEREM_AG_NAME.toLowerCase().replaceAll(" ", "_");


     *//*               for (int i = 0; i < name_old.length(); ++i) {
                        String add = name_old.substring(i, i + 1);
                        for (int j = 0; j < f.length; j++) {
                            if (f[j].equals(add)) {
                                add = t[j];
                                break;
                            }
                        }
                        res += add;
                    }*//*
                    Log.e("new^ ", res);

                    String name_file_db = "MTW_out_order";
                    String file_put_db = WJ_Forma_Zakaza.this.getDatabasePath(name_file_db + ".xml").getAbsolutePath();  // путь к databases
                    Log.e(logeTAG, "FORMA_XML_OUT: " + file_put_db);
                    if (bool_filter.booleanValue() == false) {
                        name_file_sd = ("MT_out_order_" + res + "_" + XML_Data_Start + ".xml").replaceAll("[/:*?<>]", "");
                    } else {
                        name_file_sd = ("MT_out_order_" + res + "_" + XML_Data_Start + "_" + XML_Data_End + ".xml").replaceAll("[/:*?<>]", "");
                    }


                    // String name_file_sd = "MTW_out_order_" + PEREM_AG_UID.substring(0, 6) + XML_Data_Start + ".xml";
                    XML_NEW_NAME = name_file_sd;
                   // String file_db_sd = Environment.getExternalStorageDirectory() + "/Price/" + PEREM_WORK_DISTR + "/" + name_file_sd;


                    File newxmlfile_db = new File(file_put_db);
                   // File newxmlfile_sd = new File(file_db_sd);

                    try {
                        newxmlfile_db.createNewFile();
                      //  newxmlfile_sd.createNewFile();
                    } catch (IOException e) {
                        Log.e("IOException", "Exception in create new File(");
                    }
                    FileOutputStream fileos = null;
                    FileOutputStream fileos2 = null;
                    try {
                        fileos = new FileOutputStream(newxmlfile_db);
                      // fileos2 = new FileOutputStream(newxmlfile_sd);

                    } catch (FileNotFoundException e) {
                        Log.e("FileNotFoundException", e.toString());
                    }

                    serializer = Xml.newSerializer();
                    serializer.setOutput(fileos, "UTF-8");
                    serializer.startDocument(null, Boolean.valueOf(true));
                    serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

                    serializer.startTag(null, "XML_Array");

                    for (int i = 0; i < mass_kod_rn.length; i++) {
                        Log.e("FileNotFound ", mass_kod_rn[i] + " " + XML_Data_Start + " " + XML_Data_End);
                        XML_Array_Name(mass_kod_rn[i], XML_Data_Start, XML_Data_End);
                    }
                    serializer.endTag(null, "XML_Array");

                    serializer.endDocument();

                    serializer.flush();
                    serializer.setOutput(fileos2, "UTF-8");
                    serializer.startDocument(null, Boolean.valueOf(true));
                    serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

                    serializer.startTag(null, "XML_Array");

                    for (int i = 0; i < mass_kod_rn.length; i++) {
                        Log.e("FileNotFound ", mass_kod_rn[i] + " " + XML_Data_Start + " " + XML_Data_End);
                        XML_Array_Name(mass_kod_rn[i], XML_Data_Start, XML_Data_End);
                    }
                    serializer.endTag(null, "XML_Array");

                    serializer.endDocument();
                    serializer.flush();

                    fileos.close();
                    fileos2.close();

                } catch (Exception e) {

                }


            } catch (Exception e) {

                Log.e("Err..", "rrr");
            }

            TimeUnit.SECONDS.sleep(1);
            pDialog.dismiss();


          *//*  SQLiteDatabase db = getBaseContext().openOrCreateDatabase("ostatki_db.db3", MODE_PRIVATE, null);
            SQLiteDatabase db_sunc = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
            db_sunc.delete("base4_ost", null, null);
            String query_up = "SELECT base4_ost.data, base4_ost.koduid, " +
                    "base4_ost.sklad, base4_ost.count" +
                    " FROM base4_ost\n" +
                    " WHERE base4_ost.sklad !='" + PEREM_AG_UID_SKLAD + "';";

            cursor = db.rawQuery(query_up, null);
            localContentValues = new ContentValues();

            BigDecimal f1 = new BigDecimal(0.0);
            BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(cursor.getCount()));

            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String data = cursor.getString(cursor.getColumnIndex("data"));
                String sklad_uid = cursor.getString(cursor.getColumnIndex("sklad_uid"));
                String name_uid = cursor.getString(cursor.getColumnIndex("name_uid"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String count = cursor.getString(cursor.getColumnIndex("count"));

                localContentValues.put("data", data);
                localContentValues.put("sklad_uid", sklad_uid);
                localContentValues.put("name_uid", name_uid);
                localContentValues.put("name", name);
                localContentValues.put("count", count);
                db_sunc.insert("base4_ost", null, localContentValues);
                db.insert("base4_ost", null, localContentValues);

                f1 = f1.add(pointOne);
                pDialog.setProgress(f1.intValue());
                cursor.moveToNext();*//*
        }


    }*/

/*    protected void Mail() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


    }

    private class sender_mail_async extends AsyncTask<Object, String, Boolean> {
        ProgressDialog WaitingDialog;

        @Override
        protected void onPreExecute() {
            WaitingDialog = ProgressDialog.show(context_Activity, "Выгрузка заказа", "Выполняется загрузка, подождите...", true);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            WaitingDialog.dismiss();
            WaitingDialog.cancel();
            Toast.makeText(context_Activity, "Заказ успешно отправлен!!!", Toast.LENGTH_LONG).show();
            //Loading_FTP_Zakaz(XML_NEW_NAME, preferencesWrite.PEREM_FTP_PathData + PEREM_WORK_DISTR + "/" + XML_NEW_NAME);

            int k = 0;
            try {
                String pathFilePhone = getApplication().getDatabasePath("MTW_out_order.xml").getAbsolutePath();
                //String pathFileFTP = preferencesWrite.PEREM_FTP_PathData + PEREM_WORK_DISTR + "/" + XML_NEW_NAME;
               // String pathFileFTP = preferencesWrite.PEREM_FTP_PathData + PEREM_WORK_DISTR + "/MTW_out_order_" + calendars.getThis_DateFormatSqlDB + ".xml";
                Log.e(logeTAG, "ФайлI: " + pathFilePhone);
              //  Log.e(logeTAG, "ПутьI: " + pathFileFTP);
                FTPWebhost ftpWebhost = new FTPWebhost();
              //  ftpWebhost.getFileToFTP(pathFilePhone, pathFileFTP, true);
                //((Activity)mainContext).finish();

                k++;
                Log.e(logeTAG, "ПутьK: " + k);
            } catch (Exception e) {
                // Toast.makeText(context_Activity, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
                Log.e(logeTAG, "FTP: ftpWebhost");
            }


        }

        @Override
        protected Boolean doInBackground(Object... params) {
            try {
             //   SQLiteDatabase db_sqlite = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
                final String query = "SELECT * FROM const_mail";
                final Cursor cursor = db_sqlite.rawQuery(query, null);
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    String region = cursor.getString(cursor.getColumnIndexOrThrow("region"));
                    String mail_where = cursor.getString(cursor.getColumnIndexOrThrow("mail_where"));
                    String mail = cursor.getString(cursor.getColumnIndexOrThrow("operator_mail"));
                    String mail_pass = cursor.getString(cursor.getColumnIndexOrThrow("operator_mail_pass"));
                    Log.e("FTP: ", region + "__" + mail_where);
                    if (region.equals(preferencesWrite.PEREM_FTP_PathData)) {
                        data_mail_from = mail;
                        data_mail_where = mail_where;
                        data_mail_login = mail;
                        data_mail_pass = mail_pass;
                    }
                    cursor.moveToNext();
                }
                cursor.close();
                db_sqlite.close();

                title = "Агент: " + PEREM_AG_NAME;
                text = new_write;
                from = data_mail_from;
                where = data_mail_where;
                attach = "";

                MailSenderClass sender = new MailSenderClass(data_mail_login, data_mail_pass, "465", "smtp.gmail.com");   // Null
                sender.sendMail(title, text, from, where, attach);
                // MailSenderClass sender = new MailSenderClass(PEREM_MAIL_LOGIN, PEREM_MAIL_PASS, "465", "smtp.mail.ru");

                //  MailSenderClass sender = new MailSenderClass("kerkin911@gmail.com", "muvodoutmhkbnqxi", "465", "smtp.gmail.com"); // WORK
                //   MailSenderClass sender = new MailSenderClass("kerkin911@mail.ru", "7qjc5agFqqgKJ7dTCuzf", "465", "smtp.mail.ru"); // WORK
                //    MailSenderClass sender = new MailSenderClass("RomanK911@yandex.ru", "ygkvnfxbkwpjhwxd", "587", "smtp.yandex.ru");                  // NULL
                //  MailSenderClass sender = new MailSenderClass("bishkek@sunbell.webhost.kg", "microlab_LG901480", "465", "mail.sunbell.webhost.kg");   // Null
                //  MailSenderClass sender = new MailSenderClass("sunbellagents@gmail.com", "fyczcoexpaspsham", "465", "smtp.gmail.com");   // Null

               *//* from = "bishkek@sunbell.webhost.kg";
                where = PEREM_MAIL_END;
                attach = "";
                MailSenderClass sender = new MailSenderClass("bishkek@sunbell.webhost.kg", "microlab_LG901480", "465", "mail.sunbell-kg.webhost.kg");*//*

                //  sender.sendMail(title, text, from, where, attach);
            } catch (Exception e) {
                Toast.makeText(context_Activity, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
            }

            return false;
        }
    }*/





/*                    dialog_cancel = new androidx.appcompat.app.AlertDialog.Builder(context_Activity);
                    dialog_cancel.setTitle("Отправить заказы оператору?");
                    dialog_cancel.setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                            model = new ViewModelProvider(WJ_Forma_Zakaza.this).get(Async_ViewModel_Zakaz_forFTP.class);
                            model_mail = new ViewModelProvider(WJ_Forma_Zakaza.this).get(Async_ViewModel_MailMessege.class);


                            model.getLoadingStatus().observe(WJ_Forma_Zakaza.this, status ->
                            {
                                Log.e(logeTAG, "status: " + status);
                                if (status != true) {
                                    binding.FormaProgressBar.setVisibility(View.INVISIBLE);
                                    // Log.e(logeTAG, "status False ");
                                    Toast.makeText(context_Activity, "Отчет успешно создан!", Toast.LENGTH_SHORT).show();
                                    Update_Status(calendars.getThis_DateFormatDisplay);
                                    onResume();

                                    ListAdapet_Internet_Load();
                                    Mail();
                                    WJ_Forma_Zakaza.sender_mail_async async_sending = new WJ_Forma_Zakaza.sender_mail_async();
                                    async_sending.execute();

                                    String pathFilePhone = getApplication().getDatabasePath("MTW_out_order.xml").getAbsolutePath();
                                    //String pathFileFTP = preferencesWrite.PEREM_FTP_PathData + PEREM_WORK_DISTR + "/" + XML_NEW_NAME;


                                    FtpConnectData ftpConnectData = new FtpConnectData();
                                    String pathFileFTP = preferencesWrite.PEREM_FTP_PathData
                                            + PEREM_WORK_DISTR
                                            + "/MTW_out_order_" + ftpConnectData.CreateNameFile_BackUp(preferencesWrite.Setting_AG_NAME)
                                            + calendars.getThis_DateFormatSqlDB + ".xml";
                                    Log.e(logeTAG, "ФайлI: " + pathFilePhone);
                                    Log.e(logeTAG, "ПутьI: " + pathFileFTP);
                                    FTPWebhost ftpWebhost = new FTPWebhost();
                                    ftpWebhost.getFileToFTP(pathFilePhone, pathFileFTP, true);

                                    model_mail.getMessegeStatus().observe(WJ_Forma_Zakaza.this, statusM ->
                                    {
                                        Log.e(logeTAG, "MAIL: " + statusM);
                                    });
                                    model_mail.execute();


                                } else {
                                    //  Log.e(logeTAG, "status True ");
                                    binding.FormaProgressBar.setVisibility(View.VISIBLE);
                                }
                            });


                            model.getMessegeStatus().observe(WJ_Forma_Zakaza.this, status ->
                            {
                                // Log.e(logeTAG, "XML_NEW_NAME "+list.toString());
                                XML_NEW_NAME = status.toString();
                            });

                            model.execute();


                        }

                    }).setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        }
                    });
                    dialog_cancel.show();*/

    protected void Debet_ReWrite(String d_kod_rn, String d_itogo) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT * FROM otchet_debet WHERE d_kontr_uid = '" + d_kod_rn + "'";
        final Cursor cursor = db.rawQuery(query, null);
        ContentValues localContentValues = new ContentValues();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String client = cursor.getString(cursor.getColumnIndexOrThrow("d_kontr_uid"));
            String debet = cursor.getString(cursor.getColumnIndexOrThrow("d_summa"));
            Double debet_old, debet_new, debet_db;
            debet_db = Double.parseDouble(debet);
            debet_old = Double.parseDouble(d_itogo);

            debet_new = debet_db - debet_old;
            if (debet_new != 0.0) {
                String Format = new DecimalFormat("#00.00").format(debet_new).replace(",", ".");
                localContentValues.put("d_summa", Format);
                String[] arrayOfString = new String[1];
                arrayOfString[0] = client;
                db.update("otchet_debet", localContentValues, "d_kontr_uid = ?", new String[]{client});
            } else {
                db.delete("otchet_debet", "d_kontr_uid = ?", new String[]{client});
            }

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }






















/*    protected void ListAdapet_Internet_Load() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        Log.e(this.getLocalClassName(), calendars.getThis_DateFormatSqlDB);

        String query_count = "SELECT DISTINCT k_agn_name FROM base_RN_All WHERE data = '" + calendars.getThis_DateFormatSqlDB + "'";
        final Cursor cursor_count = db.rawQuery(query_count, null);
        cursor_count.moveToFirst();
        if (cursor_count.getCount() > 0) {
            count_tt = cursor_count.getCount();
        } else count_tt = 0;
        cursor_count.close();

        String query_itogo = "SELECT SUM(summa) AS day_summa, SUM(itogo) AS day_itogo FROM base_RN_All\n" +
                "WHERE data = '" + calendars.getThis_DateFormatSqlDB + "'";
        final Cursor cursor_itogo = db.rawQuery(query_itogo, null);
        cursor_itogo.moveToFirst();
        if (cursor_itogo.getCount() > 0) {
            trade_itogo = cursor_itogo.getString(cursor_itogo.getColumnIndexOrThrow("day_itogo"));
            ;
        } else trade_itogo = "0.0";
        cursor_itogo.close();


        String query = "SELECT base_RN_All.data, base_RN_All.kod_rn, base_RN_All.k_agn_name, " +
                "base_RN_All.k_agn_uid, base_RN_All.k_agn_adress, base_RN_All.skidka, base_RN_All.summa, base_RN_All.itogo\n" +
                "FROM base_RN_All\n" +
                "WHERE base_RN_All.agent_uid = '" + PEREM_AG_UID + "' AND base_RN_All.data LIKE '" + calendars.getThis_DateFormatSqlDB + "'";

        new_write = ("");
        new_write = (new_write + "");
        new_write = (new_write + "Дата загрузки: " + calendars.getThis_DateFormatDisplay + " | " + calendars.getThis_DateFormatSqlDB);
        new_write = (new_write + "\n");
        new_write = (new_write + "Количество точек: " + count_tt + ", Общая сумма продаж: " + trade_itogo + "");
        //  new_write = (new_write + "Дата загрузки: " + this_data_work_now);
        new_write = (new_write + "\n");
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String kod_rn = cursor.getString(cursor.getColumnIndexOrThrow("kod_rn"));
            String klients = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_name"));
            String Adress = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_adress"));
            String Summa = cursor.getString(cursor.getColumnIndexOrThrow("summa"));
            String Itogo = cursor.getString(cursor.getColumnIndexOrThrow("itogo"));
            String Skidka = cursor.getString(cursor.getColumnIndexOrThrow("skidka"));
            new_write = (new_write + "\nНомер накладной: " + kod_rn);
            new_write = (new_write + "\nКонтрагент: " + klients);
            new_write = (new_write + "\nАдрес: " + Adress);
            new_write = (new_write + "\nСумма: " + Summa);
            new_write = (new_write + "\nСкидка: " + Skidka + "%");
            new_write = (new_write + "\nИтого: " + Itogo);
            new_write = (new_write + "\n");
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }*/

/*    // Константы для чтения
    protected void Constanta_Read() {
        mSettings = getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        PEREM_FTP_SERV = mSettings.getString("PEREM_FTP_SERV", "0");                    //чтение данных: имя сервера
        PEREM_FTP_LOGIN = mSettings.getString("PEREM_FTP_LOGIN", "0");                  //чтение данных: имя логин
        PEREM_FTP_PASS = mSettings.getString("PEREM_FTP_PASS", "0");                    //чтение данных: имя пароль
        PEREM_FTP_DISTR_XML = mSettings.getString("PEREM_FTP_DISTR_XML", "0");          //чтение данных: путь к файлам XML
        PEREM_FTP_DISTR_db3 = mSettings.getString("PEREM_FTP_DISTR_db3", "0");          //чтение данных: путь к файлам DB3
        PEREM_IMAGE_PUT_SDCARD = mSettings.getString("PEREM_IMAGE_PUT_SDCARD", "0");    // путь картинок на телефоне /sdcard/Price/Image/
        PEREM_IMAGE_PUT_PHONE = mSettings.getString("PEREM_IMAGE_PUT_PHONE", "0");      // Путь картинок в др. приложении android.resource://kg.price_list.roman_kerkin.sunbell_price/drawable/

        PEREM_MAIL_LOGIN = mSettings.getString("PEREM_MAIL_LOGIN", "0");                //чтение данных: для почты логин
        PEREM_MAIL_PASS = mSettings.getString("PEREM_MAIL_PASS", "0");                  //чтение данных: для почты пароль
        PEREM_MAIL_START = mSettings.getString("PEREM_MAIL_START", "0");                //чтение данных: для почты от кого
        PEREM_MAIL_END = mSettings.getString("PEREM_MAIL_END", "0");                    //чтение данных: для почты от кому
        PEREM_DB3_CONST = mSettings.getString("PEREM_DB3_CONST", "0");                  //чтение данных: Путь к базам данных с константами
        PEREM_DB3_BASE = mSettings.getString("PEREM_DB3_BASE", "0");                    //чтение данных: Путь к базам данных с товаром
        //PEREM_DB3_RN = mSettings.getString("PEREM_DB3_BaseRN", "0");                        //чтение данных: Путь к базам данных с накладными
        PEREM_ANDROID_ID_ADMIN = mSettings.getString("PEREM_ANDROID_ID_ADMIN", "0");    //чтение данных: Универсальный номер для админа
        PEREM_ANDROID_ID = mSettings.getString("PEREM_ANDROID_ID", "0");                //чтение данных: Универсальный номер пользователя


        PEREM_AG_UID = mSettings.getString("PEREM_AG_UID", "0");                         //чтение данных: передача кода агента (A8BA1F48-C7E1-497B-B74A-D86426684712)
        PEREM_AG_NAME = mSettings.getString("PEREM_AG_NAME", "0");                       //чтение данных: передача кода агента (Имя пользователя)
        PEREM_AG_REGION = mSettings.getString("PEREM_AG_REGION", "0");                   //чтение данных: маршруты для привязки к контагентам
        PEREM_AG_UID_REGION = mSettings.getString("PEREM_AG_UID_REGION", "0");           //чтение данных: uid маршруты для привязки к контагентам
        PEREM_AG_CENA = mSettings.getString("PEREM_AG_CENA", "0");                       //чтение данных: цены для агентов
        PEREM_AG_SKLAD = mSettings.getString("PEREM_AG_SKLAD", "0");                     //чтение данных: склады для агентов
        PEREM_AG_UID_SKLAD = mSettings.getString("PEREM_AG_UID_SKLAD", "0");             //чтение данных: UID склады для агентов
        PEREM_AG_TYPE_REAL = mSettings.getString("PEREM_AG_TYPE_REAL", "0");             //чтение данных: выбор типа торгового агента 1-OSDO или 2-PRES
        PEREM_AG_TYPE_USER = mSettings.getString("PEREM_AG_TYPE_USER", "0");             //чтение данных: тип учетной записи агент или экспедитор
        PEREM_WORK_DISTR = mSettings.getString("PEREM_WORK_DISTR", "0");                 //чтение данных: имя папки с данными (01_WDay)
        PEREM_KOD_MOBILE = mSettings.getString("PEREM_KOD_MOBILE", "0");                 //чтение данных:
        PEREM_KOD_UID_KODRN = mSettings.getString("PEREM_KOD_UID_KODRN", "0");           //чтение данных: уникальный код для накладной


        PEREM_KLIENT_UID = mSettings.getString("PEREM_KLIENT_UID", "0");                 //чтение данных: передача кода выбранного uid клиента
        PEREM_DIALOG_UID = mSettings.getString("PEREM_DIALOG_UID", "0");                 //чтение данных: передача кода выбранного uid клиента
        PEREM_DIALOG_DATA_START = mSettings.getString("PEREM_DIALOG_DATA_START", "0");   //чтение данных: передача кода начальной даты
        PEREM_DIALOG_DATA_END = mSettings.getString("PEREM_DIALOG_DATA_END", "0");       //чтение данных: передача кода конечной даты
        PEREM_DISPLAY_START = mSettings.getString("PEREM_DISPLAY_START", "0");           //чтение данных: передача кода для димплея начальной даты
        PEREM_DISPLAY_END = mSettings.getString("PEREM_DISPLAY_END", "0");                //чтение данных: передача кода для димплея конечной даты


        PEREM_DIALOG_UID = mSettings.getString("PEREM_DIALOG_UID", "0");
        PEREM_DIALOG_DATA_START = mSettings.getString("PEREM_DIALOG_DATA_START", "0");
        PEREM_DIALOG_DATA_END = mSettings.getString("PEREM_DIALOG_DATA_END", "0");
        PEREM_FTP_PUT = mSettings.getString("list_preference_ftp", "0");               //чтение данных:

        if (mSettings.getString("PEREM_DIALOG_DATA_START", "0").isEmpty()) {
            Summa_Data_Start = calendars.getThis_DateFormatSqlDB;
        } else Summa_Data_Start = mSettings.getString("PEREM_DIALOG_DATA_START", "0");

        if (mSettings.getString("PEREM_DIALOG_DATA_END", "0").isEmpty()) {
            Summa_Data_End = calendars.getThis_DateFormatSqlDB;
        } else Summa_Data_End = mSettings.getString("PEREM_DIALOG_DATA_END", "0");

        if (mSettings.getString("PEREM_DISPLAY_START", "0").isEmpty()) {
            Summa_Data_Display_Start = calendars.getThis_DateFormatDisplay;
        } else Summa_Data_Display_Start = mSettings.getString("PEREM_DISPLAY_START", "0");

        if (mSettings.getString("PEREM_DISPLAY_END", "0").isEmpty()) {
            Summa_Data_Display_End = calendars.getThis_DateFormatDisplay;
        } else Summa_Data_Display_End = mSettings.getString("PEREM_DISPLAY_END", "0");

        if (mSettings.getString("PEREM_DIALOG_UID", "0").isEmpty()) {
            Summa_UID = "All";
        } else Summa_UID = mSettings.getString("PEREM_DIALOG_UID", "0");
    }*/

    /*           if (id == R.id.menu_filter) {
                dialog_kol = "0";
                dialog_summa = "0.0";
                dialog_Itogo = "0.0";
                Data_Filtr_Start_Work = "";
                Data_Filtr_End_Work = "";
                Dialog_Data_UID_Klients = "";
                checkBox_Data_Clic = false;
                checkBox_Klients_Clic = false;

                final ConstraintLayout constraintLayout;
                localView = LayoutInflater.from(context_Activity).inflate(R.layout.mt_dialog_filter_work_table, null);
                spinner = localView.findViewById(R.id.spinner_filter_klients);
                btn_data_start = localView.findViewById(R.id.btn_filter_data_start);
                btn_data_end = localView.findViewById(R.id.btn_filter_data_end);
                checkBox_client = localView.findViewById(R.id.checkBox_client);
                checkBox_Data = localView.findViewById(R.id.checkBox_Data);
                constraintLayout = localView.findViewById(R.id.constr_layout);
                tvw_error = localView.findViewById(R.id.tvw_error);

                spinner.setVisibility(View.GONE);
                constraintLayout.setVisibility(View.GONE);
                tvw_error.setVisibility(View.GONE);
                UID_Klient = "";
                checkBox_client.setChecked(false);
                checkBox_Data.setChecked(false);

                checkBox_client.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b == true) {
                            spinner.setVisibility(View.VISIBLE);
                            checkBox_Klients_Clic = true;
                            spinner_filters.clear();
                            Loading_Data_Spinner();
                            adapterPriceClients = new ListAdapterAde_Spinner_Filter(context_Activity, spinner_filters);
                            adapterPriceClients.notifyDataSetChanged();
                            spinner.setAdapter(adapterPriceClients);
                        } else {
                            spinner.setVisibility(View.GONE);
                            checkBox_Klients_Clic = false;
                            spinner_filters.clear();
                            spinner_filters.add(new ListAdapterSimple_Spinner_Filter("", "", ""));
                            adapterPriceClients = new ListAdapterAde_Spinner_Filter(context_Activity, spinner_filters);
                            adapterPriceClients.notifyDataSetChanged();
                            spinner.setAdapter(adapterPriceClients);

                        }

                    }
                });

                checkBox_Data.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b == true) {
                            constraintLayout.setVisibility(View.VISIBLE);
                            checkBox_Data_Clic = true;
                            Data_Filtr_End_Work = calendars.getThis_DateFormatSqlDB;
                            btn_data_end.setText(calendars.getThis_DateFormatDisplay);
                            Data_Filtr_Start_Work = calendars.getThis_DateFormatSqlDB;
                            Data_Filtr_End_Work = calendars.getThis_DateFormatSqlDB;
                            Data_Filtr_End = calendars.getThis_DateFormatDisplay;
                        } else {
                            constraintLayout.setVisibility(View.GONE);
                            checkBox_Data_Clic = false;
                            Data_Filtr_Start = "начальная дата...";
                            btn_data_start.setText(Data_Filtr_Start);
                            Data_Filtr_End = "конечная дата...";
                            btn_data_end.setText(Data_Filtr_End);
                        }
                    }
                });

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String k_agent = ((TextView) view.findViewById(R.id.tvw_filter_name)).getText().toString();
                        UID_Klient = ((TextView) view.findViewById(R.id.tvw_filter_uid)).getText().toString();
                        Toast.makeText(context_Activity, k_agent + "\n" + UID_Klient, Toast.LENGTH_SHORT).show();
                        editor = mSettings.edit();
                        editor.putString("PEREM_DIALOG_UID", UID_Klient);  // передача кода выбранного uid клиента
                        editor.putString("PEREM_DIALOG_INT", String.valueOf(i));  // передача кода начальной даты
                        editor.putString("PEREM_DISPLAY_START", calendars.getThis_DateFormatDisplay);  // передача кода начальной даты
                        editor.putString("PEREM_DISPLAY_END", calendars.getThis_DateFormatDisplay);  // передача кода конечной даты
                        editor.commit();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                btn_data_start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(context_Activity,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(year, monthOfYear, dayOfMonth);
                                        dateFormat_work = new SimpleDateFormat("yyyy-MM-dd");
                                        dateFormat_display = new SimpleDateFormat("dd-MM-yyyy");
                                        Data_Filtr_Start_Work = dateFormat_work.format(calendar.getTime());
                                        Data_Filtr_Start = dateFormat_display.format(calendar.getTime());

                                        btn_data_start.setText(Data_Filtr_Start);
                                        if (checkBox_Data.isChecked()) {
                                            if (Data_Filtr_Start_Work.compareTo(Data_Filtr_End_Work) <= 0) {
                                                tvw_error.setVisibility(View.GONE);
                                                boolean_tvw_error = false;
                                                Log.e("Return", "Верный формат дат");
                                            } else {
                                                tvw_error.setVisibility(View.VISIBLE);
                                                boolean_tvw_error = true;
                                                Log.e("Return", "Не верный формат дат");
                                            }
                                        }

                                        Log.e("Выбор даты1: ", Data_Filtr_Start);
                                        Log.e("Выбор даты2: ", Data_Filtr_End);
                                        Log.e("Выбор даты3: ", "__" + Data_Filtr_Start_Work);
                                        Log.e("Выбор даты4: ", "__" + Data_Filtr_End_Work);*//*

                                    }
                                }, calendars.Year, calendars.Month, calendars.Day);

                            datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Отмена1",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (which == DialogInterface.BUTTON_NEGATIVE) {
                                                dialog.cancel();
                                                Log.e("Return", "Нажато нет");
                                            }
                                        }
                                    });


                            datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                                Log.e("Return", "Нажато Ок");
                                            }
                                        }
                                    });
                        datePickerDialog.show();
                    }
                });

                btn_data_end.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(context_Activity,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(year, monthOfYear, dayOfMonth);
                                        dateFormat_work = new SimpleDateFormat("yyyy-MM-dd");
                                        dateFormat_display = new SimpleDateFormat("dd-MM-yyyy");
                                        Data_Filtr_End_Work = dateFormat_work.format(calendar.getTime());
                                        Data_Filtr_End = dateFormat_display.format(calendar.getTime());
                                        btn_data_end.setText(Data_Filtr_End);
                                        if (checkBox_Data.isChecked()) {
                                            if (Data_Filtr_Start_Work.compareTo(Data_Filtr_End_Work) <= 0) {
                                                tvw_error.setVisibility(View.GONE);
                                                boolean_tvw_error = false;
                                                Log.e("Return", "Верный формат дат");
                                            } else {
                                                tvw_error.setVisibility(View.VISIBLE);
                                                boolean_tvw_error = true;
                                                Log.e("Return", "Не верный формат дат");
                                            }
                                        }

                                    }
                                }, calendars.Year, calendars.Month, calendars.Day);
                        datePickerDialog.show();
                    }
                });


                android.app.AlertDialog.Builder localBuilder = new android.app.AlertDialog.Builder(context_Activity);
                localBuilder.setView(localView);
                localBuilder.setTitle("Фильтр данных");
                localBuilder.setCancelable(false).setIcon(R.drawable.office_filter).setPositiveButton(" ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        try {
                            Dialog_Data_UID_Klients = "All";
                            Dialog_Data_Start = calendars.getThis_DateFormatSqlDB;
                            Dialog_Data_End = calendars.getThis_DateFormatSqlDB;

                            if (boolean_tvw_error.booleanValue()) {
                                Dialog_Data_Start = calendars.getThis_DateFormatSqlDB;
                                Dialog_Data_End = calendars.getThis_DateFormatSqlDB;
                            } else {
                                Dialog_Data_Start = Data_Filtr_Start_Work;
                                Dialog_Data_End = Data_Filtr_End_Work;
                            }

                            if (!checkBox_Data.isChecked()) {
                                Dialog_Data_Start = calendars.getThis_DateFormatSqlDB;
                                Dialog_Data_End = calendars.getThis_DateFormatSqlDB;
                            }

                            if (checkBox_client.isChecked()) {
                                Dialog_Data_UID_Klients = UID_Klient;
                            } else
                                Dialog_Data_UID_Klients = "All";

                            editor = mSettings.edit();
                            editor.putString("PEREM_DIALOG_UID", Dialog_Data_UID_Klients);  // передача кода выбранного uid клиента
                            editor.putString("PEREM_DIALOG_DATA_START", Dialog_Data_Start);  // передача кода начальной даты
                            editor.putString("PEREM_DIALOG_DATA_END", Dialog_Data_End);  // передача кода конечной даты
                            editor.putString("PEREM_DISPLAY_START", Data_Filtr_Start);  // передача кода начальной даты
                            editor.putString("PEREM_DISPLAY_END", Data_Filtr_End);  // передача кода конечной даты
                            editor.commit();

                            zakaz.clear();
                            Loading_List_Filter(Dialog_Data_Start, Dialog_Data_End, Dialog_Data_UID_Klients);
                            Log.e("Выбор: ", Dialog_Data_Start);
                            Log.e("Выбор: ", Dialog_Data_End);
                            // getSupportActionBar().setSubtitle("Данные с " + Data_Filtr_Start + " по \n" + Data_Filtr_End);
                            adapterPriceClients_RN = new ListAdapterAde_List_RN_Table(WJ_Forma_Zakaza.this, zakaz);
                            adapterPriceClients_RN.notifyDataSetChanged();
                            binding.FormaList.setAdapter(adapterPriceClients_RN);
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


                android.app.AlertDialog localAlertDialog = localBuilder.create();
                localAlertDialog.show();

                button_ok = localAlertDialog.getButton(-1);
                button_ok.setCompoundDrawablesWithIntrinsicBounds(R.drawable.button_ok, 0, 0, 0);
                button_cancel = localAlertDialog.getButton(-2);
                button_cancel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_close, 0);
            }*/



    /*        editor.putString("PEREM_DIALOG_UID", "All");  // передача кода выбранного uid клиента
        editor.putString("PEREM_DIALOG_DATA_START", calendars.getThis_DateFormatSqlDB);  // передача кода начальной даты
        editor.putString("PEREM_DIALOG_DATA_END", calendars.getThis_DateFormatSqlDB);    // передача кода конечной даты
        editor.putString("PEREM_DISPLAY_START", calendars.getThis_DateFormatDisplay);            // передача кода начальной даты
        editor.putString("PEREM_DISPLAY_END", calendars.getThis_DateFormatDisplay);              // передача кода конечной даты*/

}
