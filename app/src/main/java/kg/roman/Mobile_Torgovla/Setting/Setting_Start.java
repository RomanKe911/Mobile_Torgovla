package kg.roman.Mobile_Torgovla.Setting;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import kg.roman.Mobile_Torgovla.R;

public class Setting_Start extends AppCompatActivity {

    public androidx.appcompat.app.AlertDialog.Builder dialog_cancel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new Settings_Fragment_Start())
                .commit();
    }
}
