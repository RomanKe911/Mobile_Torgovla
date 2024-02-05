package kg.roman.Mobile_Torgovla.FormaZakazaNew;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.Work_Journal.WJ_Global_Activity;
import kg.roman.Mobile_Torgovla.databinding.WjFormaZakaza2023Binding;

public class WJ_FormaZakaza_2023 extends AppCompatActivity {

    private WjFormaZakaza2023Binding binding;

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
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_wj_forma_zakaza2023);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        //Toast.makeText(getBaseContext(), "Привет", Toast.LENGTH_SHORT).show();
    }

    // кнопка назад
    public void onBackPressed() {
        Intent intent_prefActivity = new Intent(getBaseContext(), WJ_Global_Activity.class);
        startActivity(intent_prefActivity);
        finish();
    }

}