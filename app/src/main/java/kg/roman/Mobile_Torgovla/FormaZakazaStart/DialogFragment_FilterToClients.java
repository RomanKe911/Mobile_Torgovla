package kg.roman.Mobile_Torgovla.FormaZakazaStart;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar.Removable;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Spinner_Filter;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Spinner_Filter;
import kg.roman.Mobile_Torgovla.MT_FTP.PreferencesWrite;
import kg.roman.Mobile_Torgovla.R;

public class DialogFragment_FilterToClients extends DialogFragment {
    private RemovableStatusFilterClient removableClient;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        removableClient = (RemovableStatusFilterClient) context;
    }

    public ArrayList<ListAdapterSimple_Spinner_Filter> filters = new ArrayList<>();
    public ListAdapterAde_Spinner_Filter adapterPriceClients;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    String client = "null", client_uid = "null";
    String logeTAG = "DialogFilterClient";

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mSettings = getActivity().getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View localView = getLayoutInflater().inflate(R.layout.dialog_mess_filters_clients, null);
        ListView listView = localView.findViewById(R.id.ListView_Clients);

        filters.clear();
        AdapterListView();
        adapterPriceClients = new ListAdapterAde_Spinner_Filter(getContext(), filters);
        adapterPriceClients.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            client = ((TextView) view.findViewById(R.id.tvw_filter_name)).getText().toString();
            client_uid = ((TextView) view.findViewById(R.id.tvw_filter_uid)).getText().toString();
        });


        return builder
                .setTitle("Фильтрация данных")
                .setMessage("фильтр данных по контрагентам")
                .setIcon(R.drawable.office_filtr_data)
                /*.setView(R.layout.dialog_mess_filters_clients)*/
                .setView(localView)
                .setPositiveButton("OK", (dialog, which) ->
                {
                    if (!client_uid.equals("null")) {
                        editor = mSettings.edit();
                        editor.putString("setting_Filters_Cliets_Name", client);
                        editor.putString("setting_Filters_Cliets_NameUID", client_uid);
                        editor.putBoolean("setting_Filters_Cliets", true);
                        editor.commit();
                        Toast.makeText(getContext(), client, Toast.LENGTH_SHORT).show();
                        removableClient.RemovableStatusFilterClient(true);

                    } else
                    {
                        editor = mSettings.edit();
                        editor.putBoolean("setting_Filters_Cliets", false);
                        editor.commit();
                        removableClient.RemovableStatusFilterClient(false);
                        Toast.makeText(getContext(), "нет данных для фильтра", Toast.LENGTH_SHORT).show();
                    }


                })
                .setNegativeButton("Отмена", null)
                .create();
    }

    protected void AdapterListView() {
        try {
            PreferencesWrite preferencesWrite = new PreferencesWrite(getContext());
            SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_CONST, MODE_PRIVATE, null);
            String query = "SELECT const_contragents.k_agent, const_contragents.adress, const_contragents.uid_k_agent\n" +
                    "FROM const_contragents\n" +
                    "WHERE const_contragents.uid_agent = '" + preferencesWrite.Setting_AG_UID + "'";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("k_agent"));
                String adress = cursor.getString(cursor.getColumnIndexOrThrow("adress"));
                String uid_k_agent = cursor.getString(cursor.getColumnIndexOrThrow("uid_k_agent"));
                filters.add(new ListAdapterSimple_Spinner_Filter(name, adress, uid_k_agent));
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(getContext(), client, Toast.LENGTH_SHORT).show();
            Log.e(logeTAG, "Ошибка, не верная фильтрация по контрагентам");
        }
    }

}