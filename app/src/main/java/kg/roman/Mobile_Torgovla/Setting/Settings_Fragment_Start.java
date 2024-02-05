package kg.roman.Mobile_Torgovla.Setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kg.roman.Mobile_Torgovla.MT_FTP.FTPWebhost;
import kg.roman.Mobile_Torgovla.R;


public class Settings_Fragment_Start extends PreferenceFragmentCompat {

    ListPreference listPreference_ftpConnect, listPreference_path;
    Preference preference_LoadDataFile;
    Async_ViewModel_Setiing model_setting;
    String logeTAG = "SettingStart";
    Context context;
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    public SharedPreferences.Editor editor;
    SharedPreferences mSettings;
    String putFTPgetFiles;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        addPreferencesFromResource(R.xml.setting_start);
        context = this.getActivity();
        mSettings = getActivity().getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        listPreference_ftpConnect = findPreference("setting_ftpIP");  // ip-адрес
        listPreference_path = findPreference("setting_ftpPathData");  // путь: /MT_Sunbell_Karakol/
        preference_LoadDataFile = findPreference("setting_LoadDataFile"); // описание
        FirstData_forDisplay(); // отображения описания под пунктами
        putFTPgetFiles = mSettings.getString("setting_ftpPathData", "null");
        Log.e(logeTAG, "onCreatePreferences: " + putFTPgetFiles);

        listPreference_ftpConnect.setOnPreferenceChangeListener((preference, newValue) -> {
            for (int i = 0; i < getResources().getStringArray(R.array.mass_ftp_server_con_value).length; i++)
                if (newValue.equals(getResources().getStringArray(R.array.mass_ftp_server_con_value)[i]))
                    listPreference_ftpConnect.setSummary(getResources().getStringArray(R.array.mass_ftp_server_con)[i]);
            return true;
        });


        listPreference_path.setOnPreferenceChangeListener((preference, newValue) -> {
            for (int i = 0; i < getResources().getStringArray(R.array.mass_Region_Put_Value).length; i++)
                if (newValue.equals(getResources().getStringArray(R.array.mass_Region_Put_Value)[i]))
                    listPreference_path.setSummary(getResources().getStringArray(R.array.mass_Region_Put_Name)[i]);
            return true;
        });

        preference_LoadDataFile.setOnPreferenceClickListener(preference -> {
            try {
                FTPWebhost ftpWebhost = new FTPWebhost();
                if (ftpWebhost.getInternetConnect(context)) {
                    Toast.makeText(context, "Интернет активен!", Toast.LENGTH_LONG).show();
                    if (ftpWebhost.getFTP_TestConnect().second) {
                        Toast.makeText(context, ftpWebhost.getFTP_TestConnect().first, Toast.LENGTH_SHORT).show(); // Коннект FTP

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder
                                .append("Данные на: ").append(CreateThisTime()).append("\n")
                                .append("XML: ").append(ftpWebhost.getFilesSize(putFTPgetFiles + "MTW_Data")).append("\n")
                                .append("БД: ").append(ftpWebhost.getFilesSize(putFTPgetFiles + "SqliteDB"));
                        preference.setSummary(stringBuilder);


                        model_setting = new ViewModelProvider(this).get(Async_ViewModel_Setiing.class);
                        model_setting.getMessegeStatus().observe(this, messege ->
                        {
                            if (messege.equals("END")) {
                                Toast.makeText(context, "Скачивание файлов завершенно, перезагрузите приложение", Toast.LENGTH_SHORT).show();
                            }

                        });
                        model_setting.execute();

                        editor = mSettings.edit();
                        editor.putString("setting_LoadDataFile", stringBuilder.toString());    //запись данных даты и времени
                        editor.commit();

                    } else
                        Toast.makeText(context, ftpWebhost.getFTP_TestConnect().first, Toast.LENGTH_SHORT).show(); // // Нет Коннект FTP
                } else
                    Toast.makeText(context, "Нет доступа к сети интернет!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(context, "ошибка, не возможно скачать файлы", Toast.LENGTH_SHORT).show();
                Log.e(logeTAG, "ошибка, не возможно скачать фалйы");
            }

            return false;
        });

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(mSettings.getString("setting_ftpIP", "null")).append("\n")
                .append(mSettings.getString("setting_ftpPathData", "null")).append("\n")
                .append(mSettings.getString("setting_LoadDataFile", "null"));
        Log.e(logeTAG, stringBuilder.toString());
    }


    protected void FirstData_forDisplay() {
        if (!listPreference_ftpConnect.getSummary().equals("null"))
            listPreference_ftpConnect.setSummary(listPreference_ftpConnect.getEntry());

        if (!listPreference_path.getSummary().equals("null"))
            listPreference_path.setSummary(listPreference_path.getEntry());

        if (!preference_LoadDataFile.getSummary().equals("null"))
            preference_LoadDataFile.setSummary(mSettings.getString("setting_LoadDataFile", "null"));
    }

    protected String CreateThisTime() {
        // Текущее время
        Date currentDate = new Date();
        // Форматирование времени как "год.месяц.день"
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        return dateText + " " + timeText;
    }
}
