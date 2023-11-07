package kg.roman.Mobile_Torgovla.Work_Journal;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Kassa;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Kassa;
import kg.roman.Mobile_Torgovla.R;

public class WJ_Otchet_Kassa extends AppCompatActivity {

    public ArrayList<ListAdapterSimple_Kassa> kassa = new ArrayList<ListAdapterSimple_Kassa>();
    public ListAdapterAde_Kassa adapterPriceClients;

    public Context context_Activity;
    public SharedPreferences sp;
    public TextView tvw_summa;
    public ListView lst_sp;
    public String PEREM_Agent, PEREM_KAgent, PEREM_KAgent_UID, PEREM_Vrema, PEREM_Data, PEREM_RNKod, Summa, PEREM_UID, PEREM_Adress;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now;
    public Spinner spinner;
    public String[][] mass;
    public String[] mass2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_otch_kassa);

        context_Activity = WJ_Otchet_Kassa.this;
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        PEREM_Agent = sp.getString("PEREM_Agent", "0");
        Calendare();

        tvw_summa = (TextView) findViewById(R.id.tvw_text_aks_summa);
        lst_sp = (ListView) findViewById(R.id.lsvw_otchet);
        spinner = findViewById(R.id.spinner_uid);




        ListAdapter_Spinner();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mass2);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                kassa.clear();
                List_adapter(mass2[i]);
                List_adapter_summa(mass2[i]);
                adapterPriceClients = new ListAdapterAde_Kassa(context_Activity, kassa);
                adapterPriceClients.notifyDataSetChanged();
                lst_sp.setAdapter(adapterPriceClients);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    protected void List_adapter(String k_agent) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("otcheti_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base_otchet_debet.name, base_otchet_debet.agent, SUM(base_otchet_debet.debet) AS 'New_debet'\n" +
                "FROM base_otchet_debet\n" +
                "WHERE base_otchet_debet.data LIKE \"%" + this_data_now + "%\"" +
                "GROUP BY base_otchet_debet.name;";

        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String debet = cursor.getString(cursor.getColumnIndex("New_debet"));
            String agent = cursor.getString(cursor.getColumnIndex("agent"));
            if (k_agent.equals(agent))
            {
                kassa.add(new ListAdapterSimple_Kassa(name, debet, agent));
                cursor.moveToNext();
            } else cursor.moveToNext();

        }
        cursor.close();
        db.close();
    }

    protected void List_adapter_summa(String k_agent) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("otcheti_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base_otchet_debet.name, base_otchet_debet.debet, SUM(base_otchet_debet.debet) AS 'Itogo'\n" +
                "FROM base_otchet_debet\n" +
                "WHERE base_otchet_debet.data LIKE \"%" + this_data_now + "%\" AND base_otchet_debet.agent LIKE \"%"+k_agent+"%\"";

        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String debet = cursor.getString(cursor.getColumnIndex("Itogo"));
     //  String Format = new DecimalFormat("#00.00").format(debet);
        tvw_summa.setText(debet);

        cursor.close();
        db.close();
    }

    private void Calendare() {
        DateFormat df_data = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df_vrema = new SimpleDateFormat("HH:mm:ss");
        DateFormat df_year = new SimpleDateFormat("yyyy");
        DateFormat df_month = new SimpleDateFormat("MM");
        DateFormat df_day = new SimpleDateFormat("dd");

        this_rn_data = df_data.format(Calendar.getInstance().getTime());
        this_rn_vrema = df_vrema.format(Calendar.getInstance().getTime());
        this_rn_year = df_year.format(Calendar.getInstance().getTime());
        this_rn_month = df_month.format(Calendar.getInstance().getTime());
        this_rn_day = df_day.format(Calendar.getInstance().getTime());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(this_rn_year), Integer.valueOf(this_rn_month) - 1, Integer.valueOf(this_rn_day));
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd:MM:yyyy");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateString_NOW = dateFormat1.format(calendar.getTime());
        String dateString_WORK = dateFormat2.format(calendar.getTime());
        this_data_now = dateString_WORK;
        Log.e(this.getLocalClassName(), "Дата" + dateString_NOW);
    }

    protected void ListAdapter_Spinner() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("otcheti_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base_agent.name_agent, base_agent.uid_agent\n" +
                "FROM base_agent";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        mass = new String[cursor.getCount()][cursor.getCount()];
        mass2 = new String[cursor.getCount()];
        while (cursor.isAfterLast() == false) {
            String uid_agent = cursor.getString(cursor.getColumnIndex("uid_agent"));
            String name_agent = cursor.getString(cursor.getColumnIndex("name_agent"));
            mass[cursor.getPosition()][0] = uid_agent;
            mass[cursor.getPosition()][1] = name_agent;
            mass2[cursor.getPosition()] = name_agent;
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }
}
