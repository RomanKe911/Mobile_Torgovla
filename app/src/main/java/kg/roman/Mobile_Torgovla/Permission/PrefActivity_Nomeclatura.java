package kg.roman.Mobile_Torgovla.Permission;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;

import kg.roman.Mobile_Torgovla.R;

import static java.lang.Boolean.FALSE;


/**
 * Created by Roman on 06.03.2017.
 */
public class PrefActivity_Nomeclatura extends PreferenceActivity {
    public Context context_Activity;
    public CheckBoxPreference chb1;
    public ListPreference listPreference_all1, listPreference_all2;
    public SwitchPreference switchPreference_ostatok, switchPreference_group_sql;
    public SharedPreferences sp;
    public SharedPreferences.Editor ed;
    public Preference info_1, info_2, info_3, info_4, info_5, info_6, phone;
    public Boolean switchPreference_boolean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_nomeclatura);
        context_Activity = PrefActivity_Nomeclatura.this;
        chb1 = (CheckBoxPreference) findPreference("chb1");
        listPreference_all1 = (ListPreference) findPreference("list_all_nal");
        listPreference_all2 = (ListPreference) findPreference("list_all_kons");
        switchPreference_ostatok = (SwitchPreference) findPreference("switch_preference_full_tovar");
        switchPreference_group_sql = (SwitchPreference) findPreference("switch_preference_group_sql");
        info_1 = (Preference) findPreference("version");
        info_2 = (Preference) findPreference("v_kod");
        info_3 = (Preference) findPreference("min_version");
        info_4 = (Preference) findPreference("kod_act");
        info_5 = (Preference) findPreference("you_version");
        info_6 = (Preference) findPreference("programmer");
        phone = (Preference) findPreference("phone");
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);

        switchPreference_boolean = sp.getBoolean("SwitchPreference_ost", false);
        if (!switchPreference_boolean) {
            //navigate user to info activity
            //  switchPreference_ostatok.setChecked(false);
            switchPreference_ostatok.setSummary("полная номенклатура");
        } else {
            //navigate user to Main activity
            // switchPreference_ostatok.setChecked(true);
            switchPreference_ostatok.setSummary("только остатки");
        }

        PackInfo();
        Preferenc_Save();

        switchPreference_ostatok.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if (switchPreference_ostatok.isChecked()) {
                    Log.e("Swich", "нет");
                    switchPreference_ostatok.setChecked(false);
                    switchPreference_ostatok.setSummary("полная номенклатура");
                } else {
                    Log.e("Swich", "Да");
                    switchPreference_ostatok.setChecked(true);
                    switchPreference_ostatok.setSummary("только остатки");
                }
                return false;
            }
        });

        switchPreference_group_sql.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                //  ed = sp.edit();
                if (switchPreference_group_sql.isChecked()) {
                    switchPreference_group_sql.setChecked(false);
                    switchPreference_group_sql.setSummary("cтандартное отображение");
                    //     ed.putBoolean("SwitchPreference_ost", false);
                } else {
                    switchPreference_group_sql.setChecked(true);
                    switchPreference_group_sql.setSummary("сортировка по группам");
                    //    ed.putBoolean("SwitchPreference_ost", true);
                }
                //   ed.commit();
                return false;
            }
        });

     /*  switchPreference_ostatok.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (switchPreference_ostatok.isChecked()) {
                    Log.e("SWPR_Ostatok=", "On");
                    switchPreference_ostatok.setSummary("только остатки");

                    ed.putBoolean("SwitchPreference_ost", true);

                } else {
                    Log.e("SWPR_Ostatok=", "Off");
                    switchPreference_ostatok.setSummary("полная номеклатура");
                    ed = sp.edit();

                    ed.commit();
                }

                return false;
            }
        });*/
        listPreference_all1.setEntries(R.array.list_ty_entries);
        listPreference_all1.setEntryValues(R.array.list_ty_entryValues);
        listPreference_all2.setEntries(R.array.list_ty_entries);
        listPreference_all2.setEntryValues(R.array.list_ty_entryValues);


        chb1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (chb1.isChecked()) {
                    listPreference_all1.setEntries(R.array.list_ty_entries);
                    listPreference_all1.setEntryValues(R.array.list_ty_entryValues);
                    listPreference_all2.setEntries(R.array.list_ty_entries);
                    listPreference_all2.setEntryValues(R.array.list_ty_entryValues);
                } else {
                    listPreference_all1.setEntries(R.array.no_ty1);
                    listPreference_all1.setEntryValues(R.array.no_ty2);
                    listPreference_all1.setValueIndex(0);
                    listPreference_all2.setEntries(R.array.no_ty1);
                    listPreference_all2.setEntryValues(R.array.no_ty2);
                    listPreference_all2.setValueIndex(0);
                }
                return false;
            }
        });


    }

    protected void PackInfo() {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            info_1.setSummary(pInfo.versionName);
            info_2.setSummary("Код: " + pInfo.versionCode + ", Ревиз: " + pInfo.baseRevisionCode);
            info_3.setSummary("API 21: Android 5.0 (Lollipop)");
            info_4.setSummary("Бренд: " + Build.BRAND + "\nМарка: " + Build.MODEL);
            info_6.setSummary("Kerkin Roman");
            switch (android.os.Build.VERSION.SDK_INT) {
                case Build.VERSION_CODES.LOLLIPOP:
                    info_5.setSummary("Версия Android 5.0 Lollipop(SDK 21)");
                    break;
                case Build.VERSION_CODES.LOLLIPOP_MR1:
                    info_5.setSummary("Версия Android 5.1 Lollipop(SDK 22)");
                    break;
                case Build.VERSION_CODES.M:
                    info_5.setSummary("Версия Android 6.0 Marshmallow(SDK 23)");
                    break;
                case Build.VERSION_CODES.N:
                    info_5.setSummary("Версия Android 7.0 Nougat(SDK 24)");
                    break;
                case Build.VERSION_CODES.N_MR1:
                    info_5.setSummary("Версия Android 7.1 Nougat(SDK 25)");
                    break;
                case Build.VERSION_CODES.O:
                    info_5.setSummary("Android 8.0 Oreo (SDK 26)");
                    break;
                case Build.VERSION_CODES.O_MR1:
                    info_5.setSummary("Android 8.1 Oreo (SDK 27)");
                    break;
                case Build.VERSION_CODES.P:
                    info_5.setSummary("Версия Android 9 Pie(SDK 28)");
                    break;
                case Build.VERSION_CODES.Q:
                    info_5.setSummary("Версия Android 10 (SDK 29)");
                    break;
            }

            phone.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.getSummary() + ""));
                    startActivity(intent);
                    return false;
                }
            });


            Log.e(this.getLocalClassName(), "Info личных данных");
        } catch (Exception e) {
            Log.e(this.getLocalClassName(), "Ошибка личных данных!");
        }


    }

    protected void Preferenc_Save() {
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        ed = sp.edit();

        if (sp.getBoolean("switch_preference_group_sql", FALSE))
            switchPreference_group_sql.setSummary("сортировка по группам");
        else switchPreference_group_sql.setSummary("cтандартное отображение");

        if (sp.getBoolean("switch_preference_full_tovar", FALSE))
            switchPreference_ostatok.setSummary("только остатки");
        else  switchPreference_ostatok.setSummary("полная номенклатура");

    }


}
