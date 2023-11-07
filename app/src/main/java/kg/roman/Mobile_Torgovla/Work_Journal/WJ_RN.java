package kg.roman.Mobile_Torgovla.Work_Journal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import kg.roman.Mobile_Torgovla.Permission.PrefActivity;
import kg.roman.Mobile_Torgovla.R;

public class WJ_RN extends AppCompatActivity {


    public String PEREM_Agent, PEREM_KAgent, PEREM_KAgent_UID, PEREM_Vrema, PEREM_Data, PEREM_RNKod, Summa;
    public SharedPreferences sp;
    public Context context_Activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_forma_zakaz_scan);
       /* toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        context_Activity = WJ_RN.this;

        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        PEREM_Agent = sp.getString("PEREM_Agent", "0");
        PEREM_KAgent = sp.getString("PEREM_KAgent", "0");
        PEREM_KAgent_UID = sp.getString("PEREM_KAgent_UID", "0");
        PEREM_Vrema = sp.getString("PEREM_Vrema", "0");
        PEREM_Data = sp.getString("PEREM_Data", "0");
        PEREM_RNKod = sp.getString("PEREM_RNKod", "0");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.user_pda);
        getSupportActionBar().setTitle("Сканер штрих-кода");
        // getSupportActionBar().setSubtitle("Аптеки");


    }

    @Override
    public void onBackPressed() {

        // super.onBackPressed();
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
    }

}
