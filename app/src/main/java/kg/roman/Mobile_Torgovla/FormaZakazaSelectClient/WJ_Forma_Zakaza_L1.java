package kg.roman.Mobile_Torgovla.FormaZakazaSelectClient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import kg.roman.Mobile_Torgovla.FormaZakazaStart.WJ_Forma_Zakaza;
import kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar.RecyclerView_Adapter_ViewHolder_Clients;
import kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar.WJ_Forma_Zakaza_L2;
import kg.roman.Mobile_Torgovla.MT_FTP.CalendarThis;
import kg.roman.Mobile_Torgovla.MT_FTP.PreferencesWrite;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.databinding.MtWjFormaKlientsBinding;

public class WJ_Forma_Zakaza_L1 extends AppCompatActivity {
    public String name_klients, name_adress, name_uid;
    // 02.02.2024
    MtWjFormaKlientsBinding binding;
    String logeTAG = "FormaZakazaL1";
    Context context_Activity;
    Async_ViewModel_Clients model;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    RecyclerView_Adapter_ViewHolder_Clients adapter_clients;
    CalendarThis calendarThis = new CalendarThis();
    ////////////////////////  01.2024 переработка данных
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.mt_wj_forma_klients);
        binding = MtWjFormaKlientsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.icon_toolbar_forma_zakaz);
        getSupportActionBar().setSubtitle("Рабочая дата: " + calendarThis.getThis_DateFormatDisplay);


        context_Activity = WJ_Forma_Zakaza_L1.this;
        name_klients = "";
        name_adress = "";

        model = new ViewModelProvider(this).get(Async_ViewModel_Clients.class);
        model.getValues().observe(this, list_clients ->
        {

            RecyclerView_Adapter_ViewHolder_Clients.OnStateClickListener stateClickListener = (clientClick, position) -> {
                Toast.makeText(context_Activity, "контрагент: " + clientClick.name,
                        Toast.LENGTH_SHORT).show();
                name_klients = clientClick.getName();
                name_uid = clientClick.getUID();
                name_adress = clientClick.getAdress().replaceAll("\\s+$", "");
            };
            adapter_clients = new RecyclerView_Adapter_ViewHolder_Clients(getBaseContext(), list_clients, stateClickListener);
            binding.FormaClietnsRecyclerView.setAdapter(adapter_clients);
            adapter_clients.notifyItemChanged(0, 0);

        });
        model.getLoadingStatus().observe(this, status ->
        {
            if (status == true) {
                binding.FormaClientsProgressBar.setVisibility(View.VISIBLE);
            } else binding.FormaClientsProgressBar.setVisibility(View.INVISIBLE);
        });
        model.execute();

        binding.FormaClietnsRecyclerView.setOnClickListener(view -> {
            name_klients = ((TextView) view.findViewById(R.id.select_client_name)).getText().toString();
            name_uid = ((TextView) view.findViewById(R.id.select_client_uid)).getText().toString();
            name_adress = ((TextView) view.findViewById(R.id.select_client_adress)).getText().toString().replaceAll("\\s+$", "");

/*            String summa_debet = ((TextView) view.findViewById(R.id.select_client_credit)).getText().toString();
            TextView title_summa_debet = ((TextView) view.findViewById(R.id.text_title_debet_contagent));
            TextView text_summa_debet = ((TextView) view.findViewById(R.id.select_client_credit));
            title_summa_debet.setVisibility(View.VISIBLE);
            text_summa_debet.setVisibility(View.VISIBLE);*/
        });

        // Log.e(logeTAG, "идентификатор2:" + Loading_Kod_RN(calendarThis.getThis_DateFormatSqlDB)); // Создание идентификатора накладной


        binding.floatingActionButton.setOnClickListener(view -> {
            PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
            String new_identRN = Loading_Kod_RN(calendarThis.getThis_DateFormatAllSqlDB);
            String mSettings_agentName = preferencesWrite.Setting_AG_NAME;                       // получение из preference: имя агента
            String mSettings_agentUID = preferencesWrite.Setting_AG_UID;                         // получение из preference: uid код агента
            String mSettings_agentSklad = preferencesWrite.Setting_AG_SKLAD;                     // получение из preference: склад
            String mSettings_agentSkladUID = preferencesWrite.Setting_AG_UID_SKLAD;              // получение из preference: uid - склада
            String mSettings_agentCena = preferencesWrite.Setting_AG_CENA;                       // получение из preference: сена для агентов

            if (!name_klients.isEmpty()) {
                Constanta_Write(name_uid, name_klients, name_adress, new_identRN, calendarThis.getThis_DateFormatSqlDB, calendarThis.getThis_DateFormatVrema, "");
                Log.e(logeTAG, name_uid + ", " + name_klients + ", " + name_adress + ", " + new_identRN + ", " + calendarThis.getThis_DateFormatSqlDB + ", " + calendarThis.getThis_DateFormatVrema);
                // 01/2024 Toast.makeText(context_Activity, "Последий код:" + new_identRN, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza_L2.class);
                startActivity(intent);
                finish();
                Log.e(logeTAG, "Write: " + "Name_Order - " + new_identRN);
                Log.e(logeTAG, "Write: " + "Name_Ag - " + mSettings_agentName);
                Log.e(logeTAG, "Write: " + "UID_Ag - " + mSettings_agentUID);
                Log.e(logeTAG, "Write: " + "Name_C - " + name_klients);
                Log.e(logeTAG, "Write: " + "UID_C - " + name_uid);
                Log.e(logeTAG, "Write: " + "Data - " + calendarThis.getThis_DateFormatXML);
                Log.e(logeTAG, "Write: " + "CREDIT - ");
                Log.e(logeTAG, "Write: " + "SKLAD - " + mSettings_agentSklad);
                Log.e(logeTAG, "Write: " + "UID_Sklad - " + mSettings_agentSkladUID);
                Log.e(logeTAG, "Write: " + "Cena_Price - " + mSettings_agentCena);
                Log.e(logeTAG, "Write: " + "Coment - ");
                Log.e(logeTAG, "Write: " + "Cena_Nds - ");
            } else
                SnackbarOverride("нет активных клиентов, выберите контрагента из списка");
        });
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
    // меню менеджер ????
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getSupportActionBar().hide();
        getMenuInflater().inflate(R.menu.menu_forma_clients, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        menuItem.setOnMenuItemClickListener(item -> {
            SearchView searchView = (SearchView) menuItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.e("Поле поиска: ", newText);
                    adapter_clients.getFilter().filter(newText);
                    return false;
                }
            });

            return true;
        });
        return super.onCreateOptionsMenu(menu);

    }

    protected String Loading_Kod_RN(String this_data) {
        Log.e(logeTAG, "Forma_1: рабочая дата " + this_data);

        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        String returnKodRN = "";
        String monthRN = "";
        try {
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH) + 1;
            String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4);

            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
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


            int k;
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
                returnKodRN = "Rn" + preferencesWrite.Setting_MT_KodUIDKodRN + monthRN + year + "_" + iden + k; // получение из preference: унив код для накладной
            else if (k < 100)
                returnKodRN = "Rn" + preferencesWrite.Setting_MT_KodUIDKodRN + monthRN + year + "_" + iden2 + k;
            else if (k < 1000)
                returnKodRN = "Rn" + preferencesWrite.Setting_MT_KodUIDKodRN + monthRN + year + "_" + iden3 + k;
            else if (k < 10000)
                returnKodRN = "Rn" + preferencesWrite.Setting_MT_KodUIDKodRN + monthRN + year + "_" + k;
            // else if (k < 100000) KOD_RN = "Rn" + Ident_Mon + Ident_Year + "_" + k;
            // Log.e("Новый код накладной:", PEREM_FORMA_KOD_RN);
            cursor.close();
            db.close();

        } catch (Exception e) {
            SnackbarOverride("Ошибка созадние кодировки накладной!");
            Log.e(logeTAG, "Ошибка созадние кодировки накладной!");
        }

        return returnKodRN; // вернуть номер для накладной
    }


    /////// Запись данных для новой активити
    protected void Constanta_Write(String kag_uid, String kag_name, String kag_adress, String kag_kodrn, String kag_data, String kag_vrema, String kag_gps) {
        try {
            mSettings = context_Activity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            editor = mSettings.edit();
            editor.putString("PEREM_K_AG_NAME", kag_name);              //запись данных: имя контраегнта
            editor.putString("PEREM_K_AG_UID", kag_uid);                //запись данных: uid контрагента
            editor.putString("PEREM_K_AG_ADRESS", kag_adress);          //запись данных: адрес контрагента
            editor.putString("PEREM_K_AG_KodRN", kag_kodrn);            //запись данных: код накладной
            editor.putString("PEREM_K_AG_Data", kag_data);              //запись данных: время создание накладной
            editor.putString("PEREM_K_AG_Data_WORK", calendarThis.getThis_DateFormatXML);    //запись данных: время создание накладной
            editor.putString("PEREM_K_AG_Vrema", kag_vrema);            //запись данных: дата создание накладной
            editor.putString("PEREM_K_AG_GPS", kag_gps);                //запись данных: координаты gps
            editor.putString("PEREM_CLICK_TY", "false");

/*            editor.putString("Setting_MT_K_AG_NAME", kag_name);              //запись данных: имя контраегнта
            editor.putString("Setting_MT_K_AG_UID", kag_uid);                //запись данных: uid контрагента
            editor.putString("Setting_MT_K_AG_ADRESS", kag_adress);          //запись данных: адрес контрагента
            editor.putString("Setting_MT_K_AG_KodRN", kag_kodrn);            //запись данных: код накладной
            editor.putString("Setting_MT_K_AG_Data", kag_data);              //запись данных: время создание накладной
            editor.putString("Setting_MT_K_AG_Data_WORK", calendarThis.getThis_DateFormatDisplay);    //запись данных: время создание накладной
            editor.putString("Setting_MT_K_AG_Vrema", kag_vrema);            //запись данных: дата создание накладной
            editor.putString("Setting_MT_K_AG_GPS", kag_gps);                //запись данных: координаты gps
            editor.putString("PEREM_CLICK_TY", "false");*/
            editor.commit();
        } catch (Exception e) {
            SnackbarOverride("Ошибка, сохранения параметров");
            Log.e(logeTAG, "Ошибка, сохранения параметров");
        }
    }
}


