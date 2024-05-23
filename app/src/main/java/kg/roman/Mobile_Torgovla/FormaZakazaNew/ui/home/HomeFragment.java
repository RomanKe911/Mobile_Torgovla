package kg.roman.Mobile_Torgovla.FormaZakazaNew.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;

import kg.roman.Mobile_Torgovla.FormaZakazaSelectClient.Async_ViewModel_Clients;
import kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar.RecyclerView_Adapter_ViewHolder_Clients;
import kg.roman.Mobile_Torgovla.FormaZakazaNew.ListRecycleAdapterAde_Klients;
import kg.roman.Mobile_Torgovla.FormaZakazaNew.SelectContrAgent;
import kg.roman.Mobile_Torgovla.FormaZakazaNew.SelectContrAgetnAdapter;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.CalendarThis;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.PreferencesWrite;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar.ListAdapterSimple_Klients;
import kg.roman.Mobile_Torgovla.FormaZakazaNew.State;
import kg.roman.Mobile_Torgovla.databinding.FragmentHomeBinding;

//  android:paddingTop="?attr/actionBarSize"
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    public ArrayList<ListAdapterSimple_Klients> klients = new ArrayList<ListAdapterSimple_Klients>();
    public ListRecycleAdapterAde_Klients adapterPriceClients;
    public ArrayList<State> states = new ArrayList<State>();
    public Integer resID;
    public String image_var, PEREM_DB3_RN, PEREM_DB3_CONST, PEREM_AG_UID, PEREM_AG_UID_REGION, PEREM_debet;
    public SharedPreferences sp;
    public SharedPreferences.Editor ed;


    public ArrayList<SelectContrAgent> client = new ArrayList<SelectContrAgent>();
    public SelectContrAgetnAdapter adapter_SelectClient;


    public String name_klients, name_adress, name_uid;
    // 02.02.2024
    //   private FragmentHomeBinding binding;
    String logeTAG = "FormaZakazaL1";
    Context context_Activity;
    Async_ViewModel_Clients model;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    public RecyclerView_Adapter_ViewHolder_Clients adapter_clients;
    CalendarThis calendarThis = new CalendarThis();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ////////////////////////02.2024

        context_Activity = getActivity().getBaseContext();
        name_klients = "";
        name_adress = "";
        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);

        if (preferencesWrite.Setting_MT_K_AG_UID.equals("null")||preferencesWrite.Setting_MT_K_AG_UID.equals("")) {
            model = new ViewModelProvider(this).get(Async_ViewModel_Clients.class);
            model.getValues().observe(getActivity(), list_clients ->
            {

                RecyclerView_Adapter_ViewHolder_Clients.OnStateClickListener stateClickListener = (clientClick, position) -> {
                    Toast.makeText(context_Activity, "контрагент: " + clientClick.name,
                            Toast.LENGTH_SHORT).show();
                    name_klients = clientClick.getName();
                    name_uid = clientClick.getUID();
                    name_adress = clientClick.getAdress().replaceAll("\\s+$", "");
                };


                adapter_clients = new RecyclerView_Adapter_ViewHolder_Clients(getActivity(), list_clients, stateClickListener);
                binding.FormaClietnsRecyclerView.setAdapter(adapter_clients);
                adapter_clients.notifyItemChanged(0, 0);

            });
            model.getLoadingStatus().observe(getActivity(), status ->
            {
                if (status == true) {
                    binding.FormaClientsProgressBar.setVisibility(View.VISIBLE);
                } else binding.FormaClientsProgressBar.setVisibility(View.INVISIBLE);
            });
            model.execute();
        }

        binding.FormaClietnsRecyclerView.setOnClickListener(view -> {
            name_klients = ((TextView) view.findViewById(R.id.select_client_name)).getText().toString();
            name_uid = ((TextView) view.findViewById(R.id.select_client_uid)).getText().toString();
            name_adress = ((TextView) view.findViewById(R.id.select_client_adress)).getText().toString().replaceAll("\\s+$", "");
            Toast.makeText(context_Activity, name_klients, Toast.LENGTH_SHORT).show();

/*            String summa_debet = ((TextView) view.findViewById(R.id.select_client_credit)).getText().toString();
            TextView title_summa_debet = ((TextView) view.findViewById(R.id.text_title_debet_contagent));
            TextView text_summa_debet = ((TextView) view.findViewById(R.id.select_client_credit));
            title_summa_debet.setVisibility(View.VISIBLE);
            text_summa_debet.setVisibility(View.VISIBLE);*/
        });

        return root;
    }


    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu_forma_clients, menu);
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

            SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
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


    protected void SnackbarOverride(String text) {
        Snackbar.make(binding.FormaClietnsRecyclerView, text, Snackbar.LENGTH_SHORT)
                .show();
    }
/*    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }*/


    protected void Constanta_Read() {
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        PEREM_DB3_CONST = sp.getString("PEREM_DB3_CONST", "0");                  //чтение данных: Путь к базам данных с константами
        PEREM_DB3_RN = sp.getString("PEREM_DB3_RN", "0");                        //чтение данных: Путь к базам данных с накладными
        PEREM_AG_UID = sp.getString("PEREM_AG_UID", "0");                         //чтение данных: передача кода агента (A8BA1F48-C7E1-497B-B74A-D86426684712)
        PEREM_AG_UID_REGION = sp.getString("PEREM_AG_UID_REGION", "0");           //чтение данных: uid маршруты для привязки к контагентам
    }

    protected void Constanta_Write(String kag_uid, String kag_name, String kag_adress, String kag_kodrn, String kag_data, String kag_vrema, String kag_gps) {
        try {
            mSettings = context_Activity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            editor = mSettings.edit();
            editor.putString("PEREM_K_AG_NAME", kag_name);              //запись данных: имя контраегнта
            editor.putString("PEREM_K_AG_UID", kag_uid);                //запись данных: uid контрагента
            editor.putString("PEREM_K_AG_ADRESS", kag_adress);          //запись данных: адрес контрагента
            editor.putString("PEREM_K_AG_KodRN", kag_kodrn);            //запись данных: код накладной
            editor.putString("PEREM_K_AG_Data", kag_data);              //запись данных: время создание накладной
            editor.putString("PEREM_K_AG_Data_WORK", calendarThis.getThis_DateFormatDisplay);    //запись данных: время создание накладной
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