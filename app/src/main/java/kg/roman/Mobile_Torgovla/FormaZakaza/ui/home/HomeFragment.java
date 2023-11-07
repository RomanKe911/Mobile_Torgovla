package kg.roman.Mobile_Torgovla.FormaZakaza.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.SnackbarContentLayout;

import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.FormaZakaza.ListRecycleAdapterAde_Klients;
import kg.roman.Mobile_Torgovla.FormaZakaza.SelectContrAgent;
import kg.roman.Mobile_Torgovla.FormaZakaza.SelectContrAgetnAdapter;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.FormaZakaza.StateAdapter;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Klients;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Klients;
import kg.roman.Mobile_Torgovla.FormaZakaza.State;
import kg.roman.Mobile_Torgovla.Work_Journal.WJ_Forma_Zakaza_L1;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final TextView textView = binding.textHome;
        final RecyclerView recyclerView = binding.recyclerView;
        final Button button = binding.button7;

        Log.e("Home", "Привет клиент");


        Constanta_Read();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                states.clear();
                StateAdapter adapter = new StateAdapter(getActivity(), states);
                recyclerView.setAdapter(adapter);
                setInitialData();
                adapter = new StateAdapter(getActivity(), states);
                // устанавливаем для списка адапте
                recyclerView.setAdapter(adapter);
                Log.e("LOGE", "1=" + states.get(0).getName());
                Log.e("LOGE", "2=" + states.get(1).getName());
                Log.e("LOGE", "3=" + states.get(2).getName());
                Toast.makeText(getActivity().getBaseContext(), "Привет", Toast.LENGTH_SHORT).show();


                ListRecycleAdapterAde_Klients.OnClickListenerClient stateClickListener = new ListRecycleAdapterAde_Klients.OnClickListenerClient() {
                    @Override
                    public void onClientClick(ListAdapterSimple_Klients clientClick, int position) {
                        Toast.makeText(getContext(), "Был выбран пункт " + clientClick.getName(),
                                Toast.LENGTH_SHORT).show();

                    }
                };







               /* ListRecycleAdapterAde_Klients.OnClickListenerClient stateClickListener = new ListRecycleAdapterAde_Klients.OnClickListenerClient() {
                    @Override
                    public void onClientClick(ListAdapterSimple_Klients clientClick, int position) {
                        Toast.makeText(getContext(), "Был выбран пункт " + clientClick.getName(),
                                Toast.LENGTH_SHORT).show();

                    }

                };*/


             /*   klients.clear();
                Loading_Base_Klients();
                adapterPriceClients = new ListRecycleAdapterAde_Klients(getActivity().getBaseContext(), klients, stateClickListener);
                adapterPriceClients.notifyDataSetChanged();
                recyclerView.setAdapter(adapterPriceClients);*/
            }
        });

        client.clear();
        Loading_Base_Klients();
        SelectContrAgetnAdapter.OnClickListenerClient onClickListenerClient = new SelectContrAgetnAdapter.OnClickListenerClient() {
            @Override
            public void onClientClick(SelectContrAgent clientClick, int position) {
                Toast.makeText(getContext(), "выбранClick= " + clientClick.getClientName(), Toast.LENGTH_SHORT).show();
            }
        };
        adapter_SelectClient = new SelectContrAgetnAdapter(getContext(), client, onClickListenerClient);
        adapter_SelectClient.notifyDataSetChanged();
        binding.recyclerView.setAdapter(adapter_SelectClient);

      /*  binding.recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textView1 = ((TextView) v.findViewById(R.id.select_client_name)).getText().toString();
                Toast.makeText(v.getContext(), "выбран= "+textView1, Toast.LENGTH_SHORT).show();
            }
        });*/


        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setInitialData() {
        states.add(new State("Бразилия", "Бразилиа", R.drawable.icon_menu_bella, false));
        states.add(new State("Аргентина", "Буэнос-Айрес", R.drawable.icon_info, false));
        states.add(new State("Колумбия", "Богота", R.drawable.icon_korsin, false));
        states.add(new State("Уругвай", "Монтевидео", R.drawable.icon_menu_big, false));
        states.add(new State("Чили", "Сантьяго", R.drawable.icon_menu_kodak, false));
        states.add(new State("Колумбия", "Сантьяго", R.drawable.icon_menu_kodak, false));
        states.add(new State("Чили", "Сантьяго", R.drawable.icon_menu_kodak, false));
        states.add(new State("Уругвай", "Сантьяго", R.drawable.icon_menu_kodak, false));
        states.add(new State("Чили", "Сантьяго", R.drawable.icon_menu_kodak, false));
        states.add(new State("Уругвай", "Сантьяго", R.drawable.icon_menu_kodak, false));
        states.add(new State("Колумбия", "Сантьяго", R.drawable.icon_menu_kodak, false));
        states.add(new State("Уругвай", "Сантьяго", R.drawable.icon_menu_kodak, false));
        states.add(new State("Уругвай", "Сантьяго", R.drawable.icon_menu_kodak, false));
        states.add(new State("Чили", "Сантьяго", R.drawable.icon_menu_kodak, false));
        states.add(new State("Колумбия", "Сантьяго", R.drawable.icon_menu_kodak, false));
    }

    protected void Loading_Base_Klients() {
        try {
            SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
            String query = "SELECT * FROM const_contragents WHERE uid_agent = '" + PEREM_AG_UID + "' AND roaduid = '" + PEREM_AG_UID_REGION + "'";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();


            while (cursor.isAfterLast() == false) {
                String uid = cursor.getString(cursor.getColumnIndexOrThrow("uid_k_agent")); // код клиента
                String name = cursor.getString(cursor.getColumnIndexOrThrow("k_agent"));
                String adress = cursor.getString(cursor.getColumnIndexOrThrow("adress"));
                String roadname = cursor.getString(cursor.getColumnIndexOrThrow("roadname")); // имя группы
                String roadUid = cursor.getString(cursor.getColumnIndexOrThrow("roaduid")); // код папки группы
                //  String userUid = cursor.getString(cursor.getColumnIndex("")); // код агента
                //  int resID = getResources().getIdentifier(IMAGE, "drawable", getPackageName());
                image_var = name.substring(0, 2);
                String name_syb = name.substring(0, 3);
                switch (name_syb) {
                    case "маг":
                        resID = getResources().getIdentifier("user_shop_01", "drawable", getActivity().getPackageName());
                        break; // магазин
                    case "+ма":
                        resID = getResources().getIdentifier("user_shop_01", "drawable", getActivity().getPackageName());
                        break; // магазин
                    case "++м":
                        resID = getResources().getIdentifier("user_shop_01", "drawable", getActivity().getPackageName());
                        break; // магазин
                    case "мар":
                        resID = getResources().getIdentifier("user_shop_04", "drawable", getActivity().getPackageName());
                        break; // магазин
                    case "апт":
                        resID = getResources().getIdentifier("user_shop_02", "drawable", getActivity().getPackageName());
                        break; // аптека
                    case "ко":
                        resID = getResources().getIdentifier("user_shop_03", "drawable", getActivity().getPackageName());
                        break; // контейнер
                    case "пав":
                        resID = getResources().getIdentifier("user_shop_03", "drawable", getActivity().getPackageName());
                        break; // контейнер
                    case "р-к":
                        resID = getResources().getIdentifier("user_shop_03", "drawable", getActivity().getPackageName());
                        break; // контейнер
                    case "го":
                        resID = getResources().getIdentifier("user_shop_06", "drawable", getActivity().getPackageName());
                        break; // гостиница
                    case "с. ":
                        resID = getResources().getIdentifier("user_shop_07", "drawable", getActivity().getPackageName());
                        break; // гостиница
                    case "г. ":
                        resID = getResources().getIdentifier("user_shop_08", "drawable", getActivity().getPackageName());
                        break; // гостиница
                    default:
                        resID = getResources().getIdentifier("user_shop_05", "drawable", getActivity().getPackageName());
                        break; // контейнер
                }
                SQL_Debet_List(uid);
                //klients.add(new ListAdapterSimple_Klients(name, uid, adress, resID, PEREM_debet));

                client.add(new SelectContrAgent(name, uid, adress, PEREM_debet, resID));
                cursor.moveToNext();

            }
            cursor.close();
            db.close();

        } catch (Exception e) {
            Toast.makeText(getContext(), "Ошибка загрузки базы в Listview", Toast.LENGTH_SHORT).show();
            Log.e("ERROR", "Ошибка загрузки базы в Listview");
        }

    }

    protected void SQL_Debet_List(String l_uid) {
        try {
            SQLiteDatabase db_debet = getActivity().getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
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
            Toast.makeText(getContext(), "Ошибка задолжностей", Toast.LENGTH_SHORT).show();
            Log.e("ERROR", "Ошибка задолжностей");
            PEREM_debet = "ОШИБКА ЗАГРУЗКА ДОЛГОВ";
        }

    }


    protected void Constanta_Read() {
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        PEREM_DB3_CONST = sp.getString("PEREM_DB3_CONST", "0");                  //чтение данных: Путь к базам данных с константами
        PEREM_DB3_RN = sp.getString("PEREM_DB3_RN", "0");                        //чтение данных: Путь к базам данных с накладными
        PEREM_AG_UID = sp.getString("PEREM_AG_UID", "0");                         //чтение данных: передача кода агента (A8BA1F48-C7E1-497B-B74A-D86426684712)
        PEREM_AG_UID_REGION = sp.getString("PEREM_AG_UID_REGION", "0");           //чтение данных: uid маршруты для привязки к контагентам
    }
}