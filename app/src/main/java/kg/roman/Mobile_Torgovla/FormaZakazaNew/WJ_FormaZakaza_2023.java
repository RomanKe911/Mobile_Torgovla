package kg.roman.Mobile_Torgovla.FormaZakazaNew;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar.Removable;
import kg.roman.Mobile_Torgovla.FormaZakazaStart.WJ_Forma_Zakaza;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.Setting.Setting;
import kg.roman.Mobile_Torgovla.databinding.WjFormaZakaza2023Binding;

public class WJ_FormaZakaza_2023 extends AppCompatActivity implements Removable {

    private WjFormaZakaza2023Binding binding;
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    public Context context_Activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = WjFormaZakaza2023Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_wj_forma_zakaza2023);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        context_Activity = getBaseContext();
        WritePreference();



        //Toast.makeText(getBaseContext(), "Привет", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_autoris, menu);
        return true;
    }

    // меню работа с item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        try {
            switch (id) {
                case R.id.menu_setting_start: {
                    Intent intent_prefActivity = new Intent(context_Activity, Setting.class);
                    startActivity(intent_prefActivity);
                }
                break;
            }

        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка вход в настройки", Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }


    // кнопка назад
    public void onBackPressed() {
        Intent intent_prefActivity = new Intent(getBaseContext(), WJ_Forma_Zakaza.class);
        startActivity(intent_prefActivity);
        finish();
    }

    protected void WritePreference()
    {
        mSettings = getApplication().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        editor = mSettings.edit();
        editor.putString("PEREM_K_AG_NAME", "null");              //запись данных: имя контраегнта
        editor.putString("PEREM_K_AG_UID", "null");                //запись данных: uid контрагента
        editor.putString("PEREM_K_AG_ADRESS", "null");          //запись данных: адрес контрагента
        editor.putString("PEREM_K_AG_KodRN", "null");            //запись данных: код накладной
        editor.putString("PEREM_K_AG_Data", "null");              //запись данных: время создание накладной
        editor.putString("PEREM_K_AG_Data_WORK", "null");    //запись данных: время создание накладной
        editor.putString("PEREM_K_AG_Vrema", "null");            //запись данных: дата создание накладной
        editor.putString("PEREM_K_AG_GPS", "null");                //запись данных: координаты gps
        editor.putString("PEREM_CLICK_TY", "false");
        editor.commit();
    }

    @Override
    public void restartAdapter(boolean statusAdapter) {
        if (statusAdapter) {
            Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza.class);
            startActivity(intent);
            // finish();
        }
    }
}