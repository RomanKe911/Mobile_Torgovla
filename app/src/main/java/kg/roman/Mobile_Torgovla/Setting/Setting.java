package kg.roman.Mobile_Torgovla.Setting;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import kg.roman.Mobile_Torgovla.R;

public class Setting extends AppCompatActivity {

    public AlertDialog.Builder dialog_cancel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new Settings_Fragment())
                .commit();
    }
}
