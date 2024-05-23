package kg.roman.Mobile_Torgovla.Setting;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;

import kg.roman.Mobile_Torgovla.MT_MyClassSetting.FTPWebhost;
import kg.roman.Mobile_Torgovla.Permission.PermissionUtils;
import kg.roman.Mobile_Torgovla.R;


public class Settings_Fragment extends PreferenceFragmentCompat {

    ListPreference listPreference_ftpConnect, listPreference_path,
            listPreference_trade_status, listPreference_SaleStandart, listPreference_SaleforFarm, listPreference_MinSumTrade;
    Preference preference_LoadDataFile, preferenceMyID;;
    Preference preferenceClear, preferencesImageManager, preferences_ImageManagerIcons;
    SwitchPreference switchPreference_ImageOld, switchPreference_ImagePhone,
            switchPreference_ImageSD, switchPreference_Permission;
    SwitchPreference switchPreference_BrendsAll, switchPreference_BrendsAgents,
            switchPreference_BrendsFirms, switchPreference_BrendsManual, switchPreference_SortedBy, switchPreference_MailMessege;
    MultiSelectListPreference multiSelectListPreference_BrendsManual;
    Preference editTextPreferenceMinSumSale;
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

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mSettings = getActivity().getApplication().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        preferenceMyID = findPreference("PEREM_ANDROID_ID");
        preferenceMyID.setSummary("["+mSettings.getString("PEREM_ANDROID_ID", "")+"]");





        Preference_FTPConnects();           // Подключение к FTP
        Preference_TradeSale();             // Настройки для торговых условий
        SwitchPreference_Image();           // База картинок(переключатели доступа к картинкам)
        SwitchPreference_Nomenclature();    // Номенклатура
        Preference_Clears();                // Очистка данных

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
        switchPreference_MailMessege = findPreference("key_ftpMailMessege"); // настройки для отправки сообщения

        switchPreference_MailMessege.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue.equals(true))
                switchPreference_MailMessege.setSummary("настройки из файла");
            else switchPreference_MailMessege.setSummary("настройки из приложения");

            return true;
        });


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

    protected void Preference_TradeSale() {
        listPreference_trade_status = findPreference("preference_TradeStatus");        // тип торгвых условий
        listPreference_MinSumTrade = findPreference("preference_ListMinSumTrade");     // мин. сумма для оформления заказа
        editTextPreferenceMinSumSale = findPreference("preference_MinSumSale");        // Сумма для получения скидки
        listPreference_SaleStandart = findPreference("preference_ListSaleStandart");   // скидка стандартная
        listPreference_SaleforFarm = findPreference("preference_ListSaleFarm");        // скидка для фарма и сетей


        // listPreference_trade_status.setSummary(mSettings.getString("preference_TradeStatus", ""));
        listPreference_trade_status.setOnPreferenceChangeListener((preference, newValue) -> {
            for (int i = 0; i < getResources().getStringArray(R.array.mass_type_ty_value).length; i++)
                if (newValue.equals(getResources().getStringArray(R.array.mass_type_ty_value)[i]))
                    listPreference_trade_status.setSummary(getResources().getStringArray(R.array.mass_type_ty_title)[i]);

            if (newValue.equals("random_ty")) {
                editTextPreferenceMinSumSale.setVisible(false);
                listPreference_SaleStandart.setVisible(false);
                listPreference_SaleforFarm.setVisible(false);

            } else {
                editTextPreferenceMinSumSale.setVisible(true);
                listPreference_SaleStandart.setVisible(true);
                listPreference_SaleforFarm.setVisible(true);
            }
            return true;
        });
        listPreference_MinSumTrade.setOnPreferenceChangeListener((preference, newValue) -> {
            for (int i = 1; i < getResources().getStringArray(R.array.list_MinSummaTrade).length; i++)
                if (newValue.equals(getResources().getStringArray(R.array.list_MinSummaTrade)))
                    listPreference_MinSumTrade.setSummary(getResources().getStringArray(R.array.list_MinSummaTrade_Count)[0]);
            return true;
        });


        editTextPreferenceMinSumSale.setSummary(mSettings.getString("preference_MinSumSale", "") + "c");
        editTextPreferenceMinSumSale.setOnPreferenceChangeListener((preference, newValue) -> {
            preference.setSummary(newValue.toString() + "c");
            return true;
        });
        androidx.preference.EditTextPreference editTextPreferenceMinSumSale = getPreferenceManager().findPreference("preference_MinSumSale");
        editTextPreferenceMinSumSale.setOnBindEditTextListener(editText -> {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
            editText.setSelection(editText.getText().length());
        });

        listPreference_SaleStandart.setOnPreferenceChangeListener((preference, newValue) -> {

            if (newValue.equals("0"))
                listPreference_SaleStandart.setSummary(getResources().getStringArray(R.array.list_ty_entryValues)[0]);

            for (int i = 1; i < getResources().getStringArray(R.array.list_ty_entries).length; i++)
                if (newValue.equals(getResources().getStringArray(R.array.list_ty_entries)))
                    listPreference_SaleStandart.setSummary(getResources().getStringArray(R.array.list_ty_entryValues)[0]);
            return true;
        });
        listPreference_SaleforFarm.setOnPreferenceChangeListener((preference, newValue) -> {

            if (newValue.equals("0"))
                listPreference_SaleforFarm.setSummary(getResources().getStringArray(R.array.list_ty_entryValues)[0]);

            for (int i = 1; i < getResources().getStringArray(R.array.list_ty_entries).length; i++)
                if (newValue.equals(getResources().getStringArray(R.array.list_ty_entries)[i]))
                    listPreference_SaleforFarm.setSummary(getResources().getStringArray(R.array.list_ty_entryValues)[i]);
            return true;
        });
    }

    // База картинок
    protected void SwitchPreference_Image() {
        switchPreference_ImageOld = findPreference("key_settingImageOld");      // путь к директории картинок старая версия
        switchPreference_ImagePhone = findPreference("key_settingImagePhone");  // путь к директории картинок новая в телефоне
        switchPreference_ImageSD = findPreference("key_settingImageSDCard");    // путь к директории картинок на SD-карте
        switchPreference_Permission = findPreference("key_SwitchPreferencePermission");    // путь к директории картинок на SD-карте
        editor = mSettings.edit();


        switchPreference_ImageOld.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue.equals(true)) {
                switchPreference_ImageOld.setChecked(true);
                switchPreference_ImagePhone.setChecked(false);
                switchPreference_ImageSD.setChecked(false);

                editor.putBoolean("key_settingImageOld", true);
                editor.putBoolean("key_settingImagePhone", false);
                editor.putBoolean("key_settingImageSDCard", false);
            }
            return false;
        });

        switchPreference_ImagePhone.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue.equals(true)) {
                switchPreference_ImageOld.setChecked(false);
                switchPreference_ImagePhone.setChecked(true);
                switchPreference_ImageSD.setChecked(false);

                editor.putBoolean("key_settingImageOld", false);
                editor.putBoolean("key_settingImagePhone", true);
                editor.putBoolean("key_settingImageSDCard", false);
            }
            return false;
        });

        switchPreference_ImageSD.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue.equals(true)) {
                switchPreference_ImageOld.setChecked(false);
                switchPreference_ImagePhone.setChecked(false);
                switchPreference_ImageSD.setChecked(true);

                editor.putBoolean("key_settingImageOld", false);
                editor.putBoolean("key_settingImagePhone", false);
                editor.putBoolean("key_settingImageSDCard", true);
            }
            return false;
        });


        if (PermissionUtils.hasPermissions(getActivity())) {
            Log.e(logeTAG, "Разрешение получено");
            switchPreference_Permission.setSummary("Разрешение получено");
            switchPreference_Permission.setChecked(true);
            switchPreference_Permission.setEnabled(false);
            editor.putBoolean("switch_preference_data", true);
        } else {
            switchPreference_Permission.setEnabled(true);
            switchPreference_Permission.setChecked(false);
            switchPreference_Permission.setSummary("Разрешение не предоставлено");
            editor.putBoolean("switch_preference_data", false);
        }

        editor.commit();

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
                switchPreference_BrendsFirms.setChecked(false);
            } else {
                switchPreference_BrendsAgents.setChecked(true);
            }
            return true;
        });

        switchPreference_BrendsAgents.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue.equals(true)) {
                switchPreference_BrendsAll.setChecked(false);
                switchPreference_BrendsManual.setChecked(false);
                switchPreference_BrendsFirms.setChecked(false);
            } else {
                switchPreference_BrendsAll.setChecked(true);
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

    protected void Preference_Clears()
    {
        preferenceClear = findPreference("setting_ClearPrederence");       // Очистить данные
        preferencesImageManager = findPreference("setting_ClearImageProduct");       // Очистить данные
        preferences_ImageManagerIcons = findPreference("setting_ClearImageIcons");       // Очистить данные


        preferenceClear.setOnPreferenceClickListener(preference -> {
            DialogFragment_DeletePreference dialog = new DialogFragment_DeletePreference();
            Bundle args = new Bundle();
            args.putString("typeClear", "clearPreference");
            dialog.setArguments(args);
            dialog.show(getActivity().getSupportFragmentManager(), "DialogClearPreference");
            return false;
        });

        preferencesImageManager.setOnPreferenceClickListener(preference -> {
            DialogFragment_DeletePreference dialog = new DialogFragment_DeletePreference();
            Bundle args = new Bundle();
            args.putString("typeClear", "clearImageManager");
            dialog.setArguments(args);
            dialog.show(getActivity().getSupportFragmentManager(), "DialogClearPreference");
            return false;
        });

        preferences_ImageManagerIcons.setOnPreferenceClickListener(preference -> {
            DialogFragment_DeletePreference dialog = new DialogFragment_DeletePreference();
            Bundle args = new Bundle();
            args.putString("typeClear", "clearImageManagerIcons");
            dialog.setArguments(args);
            dialog.show(getActivity().getSupportFragmentManager(), "DialogClearPreference");
            return false;
        });
    }


}





