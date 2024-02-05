package kg.roman.Mobile_Torgovla.FormaZakaza;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import kg.roman.Mobile_Torgovla.ImagePack.ImagePack_R_Adapter;
import kg.roman.Mobile_Torgovla.ImagePack.ImagePack_R_Simple;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Klients;
import kg.roman.Mobile_Torgovla.MT_BackUp.RecyclerView_Simple_BackUp;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.Work_Journal.WJ_New_Contragent;
import kg.roman.Mobile_Torgovla.databinding.MtWjFormaKlientsBinding;
import kg.roman.Mobile_Torgovla.databinding.PermUpdateImageBinding;

public class WJ_Forma_Zakaza_L1 extends AppCompatActivity {

    public ArrayList<ListAdapterSimple_Klients> klients = new ArrayList<ListAdapterSimple_Klients>();
    public ListAdapterAde_Klients adapterPriceClients;
    public Button button;
    public Context context_Activity;
    public Integer thisdata, thismonth, thisyear, thisminyte, thishour, thissecond;
    public String thisdata_st;
    public Calendar localCalendar = Calendar.getInstance();

    public String myTABLE, table_name, mass, thismonth_rn;
    public String PEREM_debet, data_base;
    public String DATABASE_REGIONS;
    public String ftp_put, ftp_server, login, password;

    private EditText queryEditText;
    private Handler mHandler = new Handler();
    public String image_var, name_adress, Ident_Mon, Ident_Year;

    public String agent, name_klients, name_uid, Log_Text_Error;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now;
    public Integer resID;
    public ListView listView;
    public SharedPreferences sp;
    public SharedPreferences.Editor editor;

    public String PEREM_FTP_SERV, PEREM_FTP_LOGIN, PEREM_FTP_PASS, PEREM_FTP_DISTR_XML, PEREM_FTP_DISTR_db3,
            PEREM_IMAGE_PUT_SDCARD, PEREM_IMAGE_PUT_PHONE;
    public String PEREM_MAIL_LOGIN, PEREM_MAIL_PASS, PEREM_MAIL_START, PEREM_MAIL_END,
            PEREM_DB3_CONST, PEREM_DB3_BASE, PEREM_DB3_RN, PEREM_ANDROID_ID_ADMIN, PEREM_ANDROID_ID;
    public String PEREM_AG_UID, PEREM_AG_NAME, PEREM_AG_REGION, PEREM_AG_UID_REGION, PEREM_AG_CENA,
            PEREM_AG_SKLAD, PEREM_AG_UID_SKLAD, PEREM_AG_TYPE_REAL, PEREM_AG_TYPE_USER,
            PEREM_WORK_DISTR, PEREM_KOD_MOBILE, PEREM_KOD_UID_KODRN, PEREM_K_AG_Data;
    public String PEREM_KLIENT_UID, PEREM_DIALOG_UID, PEREM_DIALOG_DATA_START, PEREM_DIALOG_DATA_END, PEREM_DISPLAY_START, PEREM_DISPLAY_END;
    public FloatingActionButton floatingActionButton;
    public ProgressBar progressBar;
    private String LOGEName = "Thread";


    /*07/11/2023
     *  Загрузка картинки для точки: нужно создать новый вариант для загрузки данных
     *  Переопределить префикся для созадниея накладной
     *
     * */
    // 16/01/2023
    private MtWjFormaKlientsBinding binding;
    String logeTAG = "Forma_Zakaza_F1";
    Async_ViewModel_Clients model;
    private SharedPreferences mSettings;
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    public RecyclerView_Adapter_ViewHolder_Clients adapter_clients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.mt_wj_forma_klients);
        binding = MtWjFormaKlientsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_icons_1);
        getSupportActionBar().setSubtitle("Рабочая дата: " + CreateThisTime().first);
        context_Activity = WJ_Forma_Zakaza_L1.this;
        name_klients = "";
        name_adress = "";

        model = new ViewModelProvider(this).get(Async_ViewModel_Clients.class);
        model.getValues().observe(this, list_clients ->
        {

            RecyclerView_Adapter_ViewHolder_Clients.OnStateClickListener stateClickListener = new RecyclerView_Adapter_ViewHolder_Clients.OnStateClickListener() {
                @Override
                public void onStateClick(ListAdapterSimple_Klients clientClick, int position) {
                    Toast.makeText(getApplicationContext(), "контрагент: "+ clientClick.name,
                            Toast.LENGTH_SHORT).show();
                   // Log.e(logeTAG, "onClick: Constsdfdsf" + clientClick.name);


                    name_klients = clientClick.getName();
                    name_uid = clientClick.getUID();
                    name_adress = clientClick.getAdress().replaceAll("\\s+$", "");
                    // Toast.makeText(context_Activity, "Клиент: " + name_klients, Toast.LENGTH_SHORT).show();
/*                    String summa_debet = ((TextView) view.findViewById(R.id.select_client_credit)).getText().toString();
                    TextView title_summa_debet = ((TextView) view.findViewById(R.id.text_title_debet_contagent));
                    TextView text_summa_debet = ((TextView) view.findViewById(R.id.select_client_credit));
                    title_summa_debet.setVisibility(View.VISIBLE);
                    text_summa_debet.setVisibility(View.VISIBLE);*/

                }
            };
            // ArrayList<ListAdapterSimple_Klients> list_clients = new ArrayList<>();
            adapter_clients = new RecyclerView_Adapter_ViewHolder_Clients(getBaseContext(), list_clients, stateClickListener);
            binding.FormaClietnsRecyclerView.setAdapter(adapter_clients);
            adapter_clients.notifyItemChanged(0, 0);
            // Log.e("ViewHolder", ftp_image_newRun.toString());

        });
        model.getLoadingStatus().observe(this, status ->
        {
            if (status == true) {
                binding.FormaClietnsProgressBar.setVisibility(View.VISIBLE);
            } else binding.FormaClietnsProgressBar.setVisibility(View.INVISIBLE);
        });
        model.execute();

        binding.FormaClietnsRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name_klients = ((TextView) view.findViewById(R.id.select_client_name)).getText().toString();
                name_uid = ((TextView) view.findViewById(R.id.select_client_uid)).getText().toString();
                name_adress = ((TextView) view.findViewById(R.id.select_client_adress)).getText().toString().replaceAll("\\s+$", "");
                Toast.makeText(context_Activity, "Клиент: " + name_klients, Toast.LENGTH_SHORT).show();
                String summa_debet = ((TextView) view.findViewById(R.id.select_client_credit)).getText().toString();
                TextView title_summa_debet = ((TextView) view.findViewById(R.id.text_title_debet_contagent));
                TextView text_summa_debet = ((TextView) view.findViewById(R.id.select_client_credit));
                title_summa_debet.setVisibility(View.VISIBLE);
                text_summa_debet.setVisibility(View.VISIBLE);
            }
        });

        Log.e("Форма заказа:", "идентификатор:" + Loading_Kod_RN(CreateThisTime().second)); // Создание идентификатора накладной


        binding.FormaClietnsFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                Log.e("Переменные для записи:", "Name_Order - " + new_identRN);
                Log.e("Переменные для записи:", "Name_Ag - " + mSettings_agentName);
                Log.e("Переменные для записи:", "UID_Ag - " + mSettings_agentUID);
                Log.e("Переменные для записи:", "Name_C - " + name_klients);
                Log.e("Переменные для записи:", "UID_C - " + name_uid);
                Log.e("Переменные для записи:", "Data - " + CreateThisTime().first);
                //  Log.e("Переменные для записи:", "CREDIT - " +);
                Log.e("Переменные для записи:", "SKLAD - " + mSettings_agentSklad);
                Log.e("Переменные для записи:", "UID_Sklad - " + mSettings_agentSkladUID);
                Log.e("Переменные для записи:", "Cena_Price - " + mSettings_agentCena);
                //   Log.e("Переменные для записи:", "Coment - " +);
                //   Log.e("Переменные для записи:", "Cena_Nds - " +);*/

                String new_identRN = Loading_Kod_RN(CreateThisTime().second);
                mSettings = context_Activity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                String mSettings_agentName = mSettings.getString("PEREM_AG_NAME", "_"); // получение из preference: имя агента
                String mSettings_agentUID = mSettings.getString("PEREM_AG_UID", "_"); // получение из preference: uid код агента
                String mSettings_agentSklad = mSettings.getString("PEREM_AG_SKLAD", "_"); // получение из preference: uid код агента
                String mSettings_agentSkladUID = mSettings.getString("PEREM_AG_UID_SKLAD", "_"); // получение из preference: uid код агента
                String mSettings_agentCena = mSettings.getString("PEREM_AG_CENA", "_"); // получение из preference: uid код агента
                DateFormat df_data = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat df_vrema = new SimpleDateFormat("HH:mm:ss");
                this_rn_data = df_data.format(Calendar.getInstance().getTime());
                this_rn_vrema = df_vrema.format(Calendar.getInstance().getTime());


                if (!name_klients.isEmpty()) {
                    // name_uid, name_klients, name_adress, new_identRN, this_rn_data, this_rn_vrema, ""
                    Constanta_Write(name_uid, name_klients, name_adress, new_identRN, this_rn_data, this_rn_vrema, "");
                    Log.e(logeTAG, name_uid + ", " + name_klients + ", " + name_adress + ", " + new_identRN + ", " + this_rn_data + ", " + this_rn_vrema);
                   // 01/2024 Toast.makeText(context_Activity, "Последий код:" + new_identRN, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza_L2.class);
                    startActivity(intent);
                    finish();
                    Log.e("Переменные для записи:", "Name_Order - " + new_identRN);
                    Log.e("Переменные для записи:", "Name_Ag - " + mSettings_agentName);
                    Log.e("Переменные для записи:", "UID_Ag - " + mSettings_agentUID);
                    Log.e("Переменные для записи:", "Name_C - " + name_klients);
                    Log.e("Переменные для записи:", "UID_C - " + name_uid);
                    Log.e("Переменные для записи:", "Data - " + CreateThisTime().first);
                    //  Log.e("Переменные для записи:", "CREDIT - " +);
                    Log.e("Переменные для записи:", "SKLAD - " + mSettings_agentSklad);
                    Log.e("Переменные для записи:", "UID_Sklad - " + mSettings_agentSkladUID);
                    Log.e("Переменные для записи:", "Cena_Price - " + mSettings_agentCena);
                    //   Log.e("Переменные для записи:", "Coment - " +);
                    //   Log.e("Переменные для записи:", "Cena_Nds - " +);
                } else {
                    Toast.makeText(context_Activity, "Выберите клиента", Toast.LENGTH_LONG).show();

                    Snackbar.make(view, "Выберите клиента", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });


/*
        binding.FormaClietnsFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name_klients.isEmpty()) {
                    // name_uid, name_klients, name_adress, new_identRN, this_rn_data, this_rn_vrema, ""
                    Constanta_Write(name_uid, name_klients, name_adress, new_identRN, this_rn_data, this_rn_vrema, "");
                    Log.e(logeTAG, name_uid + ", " + name_klients + ", " + name_adress + ", " + new_identRN + ", " + this_rn_data + ", " + this_rn_vrema);
                    Toast.makeText(context_Activity, "Последий код:" + new_identRN, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza_L2.class);
                    startActivity(intent);
                    finish();
                    Log.e("Переменные для записи:", "Name_Order - " + new_identRN);
                    Log.e("Переменные для записи:", "Name_Ag - " + mSettings_agentName);
                    Log.e("Переменные для записи:", "UID_Ag - " + mSettings_agentUID);
                    Log.e("Переменные для записи:", "Name_C - " + name_klients);
                    Log.e("Переменные для записи:", "UID_C - " + name_uid);
                    Log.e("Переменные для записи:", "Data - " + CreateThisTime().first);
                    //  Log.e("Переменные для записи:", "CREDIT - " +);
                    Log.e("Переменные для записи:", "SKLAD - " + mSettings_agentSklad);
                    Log.e("Переменные для записи:", "UID_Sklad - " + mSettings_agentSkladUID);
                    Log.e("Переменные для записи:", "Cena_Price - " + mSettings_agentCena);
                    //   Log.e("Переменные для записи:", "Coment - " +);
                    //   Log.e("Переменные для записи:", "Cena_Nds - " +);
                } else {
                    Toast.makeText(context_Activity, "Выберите клиента", Toast.LENGTH_LONG).show();

                    Snackbar.make(view, "Выберите клиента", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });*/


    }


    ///// Всплывающая строка состояния
    protected void SnackbarOverride(String text) {
        Snackbar.make(binding.FormaClietnsRecyclerView, text, Snackbar.LENGTH_SHORT)
                .show();
    }


    // Вызов метода при выходе назад
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza.class);
        startActivity(intent);
        finish();
    }


    // Загрузка базы данных в ListView из DB3
    protected void Loading_Base_Klients() {
        try {


        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка загрузки базы в Listview", Toast.LENGTH_SHORT).show();
            Log.e(Log_Text_Error, "Ошибка загрузки базы в Listview");
        }

    }


    protected void SQL_Debet_List(String l_uid) {
        try {
            SQLiteDatabase db_debet = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
            String query_debet = "SELECT * FROM otchet_debet WHERE d_kontr_uid = '" + l_uid + "' AND d_summa > 0;";
            final Cursor cursor_debet = db_debet.rawQuery(query_debet, null);
            cursor_debet.moveToFirst();
            if (cursor_debet.getCount() > 0 & cursor_debet != null) {
                String summa = cursor_debet.getString(cursor_debet.getColumnIndexOrThrow("d_summa")); // код клиента
                PEREM_debet = summa;
            } else PEREM_debet = "нет задолжностей";
            cursor_debet.close();
            db_debet.close();
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка задолжностей", Toast.LENGTH_SHORT).show();
            Log.e(Log_Text_Error, "Ошибка задолжностей");
            PEREM_debet = "ОШИБКА ЗАГРУЗКА ДОЛГОВ";
        }

    }


    // меню менеджер ????
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getSupportActionBar().hide();
        getMenuInflater().inflate(R.menu.menu_forma_clients, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                Toast.makeText(context_Activity, "В разарботке", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("Поле поиска.1.", newText);
                adapter_clients.getFilter().filter(newText);
/*                binding.FormaClietnsRecyclerView.setAdapter(adapter_clients);
                adapter_clients.notifyItemChanged(0, 0);*/
                return false;
            }
        });



/*        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // посимвольный поиск
                Log.e("Поле поиска.1.", newText);
                adapter_clients.getFilter().filter(newText);
                binding.FormaClietnsRecyclerView.setAdapter(adapter_clients);
                adapter_clients.notifyItemChanged(0, 0);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // срабатывание после нажатия на кнопку поиска
                // TextView textView=(TextView)findViewById(R.id.search);
                //  textView.setText(query);
                Log.e("Поле поиска.2.", query);
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);*/
        return super.onCreateOptionsMenu(menu);

    }

    // меню менеджер
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.search: {

                // Log.e("Поле поиска..", "_"+item.getItemId());
               /* klients.clear();
                Loading_Base_Klients();
                adapterPriceClients = new ListAdapterAde_Klients(WJ_Forma_Zakaza_L1.this, klients);
                adapterPriceClients.notifyDataSetChanged();
                listView.setAdapter(adapterPriceClients);*/

              /*  Intent intent = getIntent();
                if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                    //Берем строку запроса из экстры
                    String query = intent.getStringExtra(SearchManager.QUERY);
                    //Выполняем поиск
                   // showResults(query);
                }*/


            }
            break;
            case R.id.menu_sync_up: {
                Intent intent_prefActivity = new Intent(context_Activity, WJ_New_Contragent.class);
                intent_prefActivity.putExtra("Layout_INTENT_BACK", "WJ_Forma_Zalaza_L1");
                startActivity(intent_prefActivity);
                finish();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }


    ////////////////////////  01.2024 переработка данных

    /////// Создание переменной кода накладной
    protected String Loading_Kod_RN(String this_data) {
        Log.e(Log_Text_Error, "рабочая дата" + this_data);

        mSettings = context_Activity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String mSettings_UID_KODRN = mSettings.getString("PEREM_KOD_UID_KODRN", "_"); // получение из preference: унив код для накладной
        String mSettings_baseName_RN = mSettings.getString("PEREM_DB3_RN", "_");  // получение из preference: название базы с накладными
        String returnKodRN = "";
        String monthRN = "";

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4);


        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(mSettings_baseName_RN, MODE_PRIVATE, null);
        //07/04/2022 измение в выборе товара   String query = "SELECT base_RN.Kod_RN, base_RN.Data FROM base_RN ORDER BY base_RN.Data ASC";
        //  String query = "SELECT base_RN.Kod_RN, base_RN.Data FROM base_RN  WHERE base_RN.Data = '" + this_data + "' ORDER BY base_RN.Kod_RN ASC;";
        String query = "SELECT base_RN_All.kod_rn, base_RN_All.data FROM base_RN_All ORDER BY base_RN_All.data ASC;";
        final Cursor cursor = db.rawQuery(query, null);
        String kod_univ;
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            kod_univ = cursor.getString(cursor.getColumnIndexOrThrow("kod_rn"));
        } else
            kod_univ = "RnTTTTGOD21_0000";

        String[] mass_month = getResources().getStringArray(R.array.mass_month);
        for (int i = 1; i <= 12; i++)
            if (month == i)
                monthRN = mass_month[i - 1];


        Integer k;
        if (kod_univ != null & cursor.getCount() > 0 & kod_univ.length() < 16)
            kod_univ = "RnTTTTGOD21_0000";


        if (monthRN.equals(kod_univ.substring(6, kod_univ.length() - 7)))
            k = Integer.parseInt(kod_univ.substring(12)) + 1;
        else
            k = 1;

        String iden = "000";
        String iden2 = "00";
        String iden3 = "0";
        // String iden4 = "0";
        if (k < 10)
            returnKodRN = "Rn" + mSettings_UID_KODRN + monthRN + year + "_" + iden + k;
        else if (k < 100)
            returnKodRN = "Rn" + mSettings_UID_KODRN + monthRN + year + "_" + iden2 + k;
        else if (k < 1000)
            returnKodRN = "Rn" + mSettings_UID_KODRN + monthRN + year + "_" + iden3 + k;
        else if (k < 10000)
            returnKodRN = "Rn" + mSettings_UID_KODRN + monthRN + year + "_" + k;
        // else if (k < 100000) KOD_RN = "Rn" + Ident_Mon + Ident_Year + "_" + k;
        // Log.e("Новый код накладной:", PEREM_FORMA_KOD_RN);
        cursor.close();
        db.close();


        try {


        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка созадние кодировки накладной!", Toast.LENGTH_SHORT).show();
            Log.e(Log_Text_Error, "Ошибка созадние кодировки накладной!");
        }

        return returnKodRN; // вернуть номер для накладной
    }

    /////// Получение даты и времени для дисплеия и работы с данными
    protected Pair<String, String> CreateThisTime() {
        // Текущее время
        Date currentDate = new Date();
        // Форматирование времени как "год.месяц.день"
        DateFormat dateFormat_display = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText_display = dateFormat_display.format(currentDate);
        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat_display = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText_display = timeFormat_display.format(currentDate);
        String dateDisplayFormat = dateText_display;
        //display 16.01.2024  (18:06:17)

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        String dateDataFormat = dateText + " " + timeText;
        //date 2022-04-14 18:06:17

        // формат pair(дата для дисплея, дата для данных)
        return new Pair<>(dateDisplayFormat, dateDataFormat);
    }

    /////// Запись данных для новой активити
    protected void Constanta_Write(String kag_uid, String kag_name, String kag_adress, String kag_kodrn, String kag_data, String kag_vrema, String kag_gps) {
        editor = mSettings.edit();
        editor.putString("PEREM_K_AG_NAME", kag_name);              //запись данных: имя контраегнта
        editor.putString("PEREM_K_AG_UID", kag_uid);                //запись данных: uid контрагента
        editor.putString("PEREM_K_AG_ADRESS", kag_adress);          //запись данных: адрес контрагента
        editor.putString("PEREM_K_AG_KodRN", kag_kodrn);            //запись данных: код накладной
        editor.putString("PEREM_K_AG_Data", kag_data);              //запись данных: время создание накладной
        editor.putString("PEREM_K_AG_Data_WORK", this_data_now);    //запись данных: время создание накладной
        editor.putString("PEREM_K_AG_Vrema", kag_vrema);            //запись данных: дата создание накладной
        editor.putString("PEREM_K_AG_GPS", kag_gps);                //запись данных: координаты gps
        editor.putString("PEREM_CLICK_TY", "false");
        editor.commit();
    }

}


