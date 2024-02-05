package kg.roman.Mobile_Torgovla.Setting;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.google.common.collect.Comparators;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import kg.roman.Mobile_Torgovla.MT_FTP.FTPWebhost;
import kg.roman.Mobile_Torgovla.Permission.PermissionUtils;
import kg.roman.Mobile_Torgovla.Permission.PrefActivity_Splash;
import kg.roman.Mobile_Torgovla.R;


public class Settings_Fragment extends PreferenceFragmentCompat {

    ListPreference listPreference_ftpConnect, listPreference_path,
            listPreference_trade_status, listPreference_trade_nal, listPreference_trade_kons;
    Preference preference_LoadDataFile;
    SwitchPreference switchPreference_ImageOld, switchPreference_ImagePhone,
            switchPreference_ImageSD, switchPreference_Permission;
    SwitchPreference switchPreference_BrendsAll, switchPreference_BrendsAgents,
            switchPreference_BrendsFirms, switchPreference_BrendsManual, switchPreference_SortedBy;
    MultiSelectListPreference multiSelectListPreference_BrendsManual;
    Async_ViewModel_Setiing model_setting;
    String logeTAG = "Setting";
    private static final int PERMISSION_STORAGE = 101;
    Context context;
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    public SharedPreferences.Editor editor;
    SharedPreferences mSettings;
    String putFTPgetFiles;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        addPreferencesFromResource(R.xml.setting);
        context = this.getActivity();
        mSettings = getActivity().getApplication().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);


        Preference_FTPConnects();           // Подключение к FTP
        Preference_Trade();                 // Настройки для торговых условий
        SwitchPreference_Image();           // База картинок(переключатели доступа к картинкам)
        SwitchPreference_Nomenclature();    // Номенклатура

        putFTPgetFiles = mSettings.getString("setting_ftpPathData", "null");
        FirstData_forDisplay(); // отображения описания под пунктами
    }


    // Настройки для отображения на экране
    protected void FirstData_forDisplay() {
        if (!listPreference_ftpConnect.getSummary().equals("null"))
            listPreference_ftpConnect.setSummary(listPreference_ftpConnect.getEntry());

        if (!listPreference_path.getSummary().equals("null"))
            listPreference_path.setSummary(listPreference_path.getEntry());

        if (!preference_LoadDataFile.getSummary().equals("null"))
            preference_LoadDataFile.setSummary(mSettings.getString("setting_LoadDataFile", "null"));

        if (!listPreference_trade_status.getSummary().equals("null"))
            listPreference_trade_status.setSummary(listPreference_trade_status.getEntry());
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

    protected void Preference_FTPConnects() {
        listPreference_ftpConnect = findPreference("setting_ftpIP");        // ip-адрес
        listPreference_path = findPreference("setting_ftpPathData");        // путь: /MT_Sunbell_Karakol/
        preference_LoadDataFile = findPreference("setting_LoadDataFile");   // описание


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
    }

    protected void Preference_Trade() {
        listPreference_trade_status = findPreference("preference_TradeStatus");      // торговые условия
        listPreference_trade_nal = findPreference("preference_ListTradeNal");       // скидка за налиный расчет
        listPreference_trade_kons = findPreference("preference_ListTradeKons");     // скидка под конс

        listPreference_trade_status.setOnPreferenceChangeListener((preference, newValue) -> {
            for (int i = 0; i < getResources().getStringArray(R.array.mass_type_ty_value).length; i++)
                if (newValue.equals(getResources().getStringArray(R.array.mass_type_ty_value)[i]))
                    listPreference_trade_status.setSummary(getResources().getStringArray(R.array.mass_type_ty_title)[i]);
            return true;
        });


        listPreference_trade_nal.setOnPreferenceChangeListener((preference, newValue) -> {

            if (newValue.equals("0"))
                listPreference_trade_nal.setSummary(getResources().getStringArray(R.array.list_ty_entryValues)[0]);

            for (int i = 1; i < getResources().getStringArray(R.array.list_ty_entries).length; i++)
                if (newValue.equals(getResources().getStringArray(R.array.list_ty_entries)))
                    listPreference_trade_nal.setSummary(getResources().getStringArray(R.array.list_ty_entryValues)[0]);
            return true;
        });

        listPreference_trade_kons.setOnPreferenceChangeListener((preference, newValue) -> {

            if (newValue.equals("0"))
                listPreference_trade_kons.setSummary(getResources().getStringArray(R.array.list_ty_entryValues)[0]);

            for (int i = 1; i < getResources().getStringArray(R.array.list_ty_entries).length; i++)
                if (newValue.equals(getResources().getStringArray(R.array.list_ty_entries)[i]))
                    listPreference_trade_kons.setSummary(getResources().getStringArray(R.array.list_ty_entryValues)[i]);
            return true;
        });
    }

    // База картинок
    protected void SwitchPreference_Image() {
        switchPreference_ImageOld = findPreference("key_settingImageOld");      // путь к директории картинок старая версия
        switchPreference_ImagePhone = findPreference("key_settingImagePhone");  // путь к директории картинок новая в телефоне
        switchPreference_ImageSD = findPreference("key_settingImageSDCard");    // путь к директории картинок на SD-карте
        switchPreference_Permission = findPreference("key_SwitchPreferencePermission");    // путь к директории картинок на SD-карте

        switchPreference_ImageOld.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue.equals(true)) {
                switchPreference_ImageOld.setChecked(true);
                switchPreference_ImagePhone.setChecked(false);
                switchPreference_ImageSD.setChecked(false);
            }
            return false;
        });

        switchPreference_ImagePhone.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue.equals(true)) {
                switchPreference_ImageOld.setChecked(false);
                switchPreference_ImagePhone.setChecked(true);
                switchPreference_ImageSD.setChecked(false);
            }
            return false;
        });

        switchPreference_ImageSD.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue.equals(true)) {
                switchPreference_ImageOld.setChecked(false);
                switchPreference_ImagePhone.setChecked(false);
                switchPreference_ImageSD.setChecked(true);
            }
            return false;
        });


        editor = mSettings.edit();
        if (PermissionUtils.hasPermissions(getActivity())) {
            Log.e(logeTAG, "Разрешение получено");
            switchPreference_Permission.setSummary("Разрешение получено");
            switchPreference_Permission.setChecked(true);
            switchPreference_Permission.setEnabled(false);
            editor.putBoolean("switch_preference_data", true);
            editor.commit();
        } else {
            switchPreference_Permission.setEnabled(true);
            switchPreference_Permission.setChecked(false);
            switchPreference_Permission.setSummary("Разрешение не предоставлено");
            editor.putBoolean("switch_preference_data", false);
            editor.commit();
        }


        switchPreference_Permission.setOnPreferenceChangeListener((preference, newValue) -> {
            if (PermissionUtils.hasPermissions(context)) return true;
            PermissionUtils.requestPermissions(getActivity(), PERMISSION_STORAGE);
            return true;
        });
    }

    protected void SwitchPreference_Nomenclature() {
        switchPreference_SortedBy = findPreference("key_SwitchPreference_SortedBySQL");               // Программная сортировка товара
        switchPreference_BrendsAll = findPreference("key_SwitchPreference_BrendsAll");               // Отображение всех брендов
        switchPreference_BrendsAgents = findPreference("key_SwitchPreference_BrendsAgents");        // Отображение по готовому списку для агентов
        switchPreference_BrendsFirms = findPreference("key_SwitchPreference_BrendsFirms");           // Отображение по фирмам (Категориям)
        switchPreference_BrendsManual = findPreference("key_SwitchPreference_BrendsManual");         // Ручной отбор брендов
        multiSelectListPreference_BrendsManual = findPreference("key_MultiSelectPreference_BrendsManual");    // Мульти отбор по списку брендов
        CreateMultiSelectList();


        switchPreference_BrendsAll.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue.equals(true)) {
                switchPreference_BrendsAgents.setChecked(false);
                switchPreference_BrendsManual.setChecked(false);
            }
            return true;
        });

        switchPreference_BrendsAgents.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue.equals(true)) {
                switchPreference_BrendsAll.setChecked(false);
                switchPreference_BrendsManual.setChecked(false);
            }
            return true;
        });

        switchPreference_BrendsManual.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue.equals(true)) {
                switchPreference_BrendsAll.setChecked(false);
                switchPreference_BrendsAgents.setChecked(false);
            }
            return true;
        });




    }

    protected void CreateMultiSelectList() {
        SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase("sunbell_base_db.db3", MODE_PRIVATE, null);
        String query = "SELECT brends, kod, prefic FROM base_in_brends_id\n" +
                "ORDER BY brends;";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        TreeMap<String, String> mytree = new TreeMap<>();
        while (cursor.isAfterLast() == false) {
            String brend = cursor.getString(cursor.getColumnIndexOrThrow("brends"));
            String prefics = cursor.getString(cursor.getColumnIndexOrThrow("prefic"));
            mytree.put(brend, prefics);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        multiSelectListPreference_BrendsManual.setEntries(mytree.keySet().toArray(new CharSequence[mytree.keySet().size()]));
        multiSelectListPreference_BrendsManual.setEntryValues(mytree.values().toArray(new CharSequence[mytree.values().size()]));
    }
}




/*       editor = mSettings.edit();
        editor.remove("key_MultiSelectPreference_BrendsManual");
        editor.putStringSet("key_MultiSelectPreference_BrendsManual", mySet);
        editor.putStringSet("key_MultiSelectPreference_BrendsManual", mySet);
        editor.commit();*/
