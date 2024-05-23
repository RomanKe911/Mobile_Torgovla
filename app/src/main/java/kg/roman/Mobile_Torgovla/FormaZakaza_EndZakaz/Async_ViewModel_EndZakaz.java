package kg.roman.Mobile_Torgovla.FormaZakaza_EndZakaz;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import kg.roman.Mobile_Torgovla.MT_MyClassSetting.CalendarThis;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.PreferencesWrite;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.Preferences_MTSetting;

public class Async_ViewModel_EndZakaz extends AndroidViewModel {

    String logeTAG = "ViewModel_EndZakaz";

    SharedPreferences mSettings;
    SharedPreferences.Editor editor;

    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";

    TreeMap<String, Pair<String, String>> sqlNewKod = new TreeMap<>();

    public Async_ViewModel_EndZakaz(@NonNull Application application) {
        super(application);
        // Context context = getApplication().getApplicationContext();
    }

    ////////////////////////
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public LiveData<Boolean> getStatus() {
        if (isLoading == null) isLoading = new MutableLiveData<>(false);
        return isLoading;
    }
    ////////////////////////

    public void execute() {
        Runnable runnable = () -> {
            try {
                isLoading.postValue(true);
                PreferencesWrite preferencesWrite = new PreferencesWrite(getApplication().getBaseContext());
                Preferences_MTSetting preferencesMtSetting = new Preferences_MTSetting();

                Log.e(logeTAG, "Статус: " + preferencesMtSetting.readSettingString(getApplication().getBaseContext(), preferencesMtSetting.getCodeOrder()));
                switch (preferencesMtSetting.readSettingString(getApplication().getBaseContext(), preferencesMtSetting.getStatusOrder())) {
                    case "Create": {
                        ///// Деление товара по складам
                        rewriteSQLSklad();
                    }
                    break;
                    case "Edit": {
                        /*
                         * Возвращаем товар на склад
                         * Вычитаем сумму старого заказа
                         * Удаляем старый заказ из базы
                         */
                        edit_NewRN(preferencesWrite);
                        //// Заменяем измененный заказ в базу, меняем шапку заказа
                        update_InsertTableEdit(preferencesMtSetting.readSettingString(getApplication().getBaseContext(), preferencesMtSetting.getCodeOrder()));
                    }
                    break;
                    case "Copy": {
                        ///// Деление товара по складам
                        rewriteSQLSklad();
                    }
                    break;
                }

                try {
                    //// Минусовать товара из остатков
                    selectListTovar(preferencesMtSetting.readSettingString(getApplication().getBaseContext(), preferencesMtSetting.getCodeOrder()));
                } catch (Exception e) {
                    Log.e(logeTAG, "Ошибка не верно минусовали товар:" + e);
                }

                try {
                    /////  Запись товара в базу консигнация
                    writeDebetZakaz();
                } catch (Exception e) {
                    Log.e(logeTAG, "Ошибка не верно минусовали конс:" + e);
                }

                try {
                    //// Очистить настройки переменных
                    clearPermisionZakaz();
                } catch (Exception e) {
                    Log.e(logeTAG, "Ошибка не верная очистка данных" + e);
                }


            } catch (SQLException sqlException) {
                Log.e(logeTAG, "Ошибка sql:" + sqlException);
            } catch (Exception e) {
                Log.e(logeTAG, "Ошибка потока" + e);
            }
            isLoading.postValue(false);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /////  Запись товара в базу консигнация
    protected void writeDebetZakaz() {
        PreferencesWrite preferencesWrite = new PreferencesWrite(getApplication().getBaseContext());
        Preferences_MTSetting preferencesMtSetting = new Preferences_MTSetting();
        Context context = getApplication().getBaseContext();
        String w_d_agent_name, w_d_agent_uid, w_d_client_name, w_d_client_uid, w_d_debet;
        w_d_agent_name = preferencesWrite.Setting_AG_NAME;
        w_d_agent_uid = preferencesWrite.Setting_AG_UID;
        w_d_client_name = preferencesMtSetting.readSettingString(context, preferencesMtSetting.getClientName());
        w_d_client_uid = preferencesMtSetting.readSettingString(context, preferencesMtSetting.getClientUID());
        w_d_debet = preferencesMtSetting.readSettingString(context, preferencesMtSetting.getAutoSum_Itogo());
        SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);

        String query = "SELECT * FROM otchet_debet " + "WHERE d_kontr_uid = '" + w_d_client_uid + "' AND d_summa > 0;";
        final Cursor cursor = db.rawQuery(query, null);
        String peremen_query;
        String PEREM_NEW_DEBET_WRITE = "0";
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            String d_summa = cursor.getString(cursor.getColumnIndexOrThrow("d_summa")).replace(",", ".");
            double debet_new, debet_old;
            debet_old = Double.parseDouble(d_summa);
            debet_new = Double.parseDouble(w_d_debet) + debet_old;

            peremen_query = "UPDATE otchet_debet " +
                    "SET " + "d_agent_name = '" + w_d_agent_name + "',  " +
                    "" + "d_agent_uid = '" + w_d_agent_uid + "',  " +
                    "" + "d_kontr_name = '" + w_d_client_name + "', "
                    + "d_kontr_uid = '" + w_d_client_uid + "', "
                    + "d_summa = '" + new DecimalFormat("#00.00").format(debet_new).replace(",", ".")
                    + "'  " + "WHERE d_kontr_uid = '" + w_d_client_uid + "'";
            PEREM_NEW_DEBET_WRITE = new DecimalFormat("#00.00").format(debet_new).replace(",", ".");

        } else {
            peremen_query = "INSERT INTO otchet_debet (d_agent_name, d_agent_uid, d_kontr_name, d_kontr_uid, d_summa) "
                    + "VALUES (" + "'" + w_d_agent_name + "', " + "'" + w_d_agent_uid + "', " + "'" + w_d_client_name + "', " + "'" + w_d_client_uid + "', " + "'" + w_d_debet + "');";
            PEREM_NEW_DEBET_WRITE = w_d_debet;
        }

        try {
            final Cursor cursor_new = db.rawQuery(peremen_query, null);
            cursor_new.moveToLast();
            cursor_new.close();
            db.close();
        } catch (Exception e) {
            Log.e(logeTAG, "Ошибка: ошибка заполнения!");
            db.close();
        }

    }

    //////  Функция деление заказа по складам
    protected void rewriteSQLSklad() {

        PreferencesWrite preferencesWrite = new PreferencesWrite(getApplication().getBaseContext());
        Preferences_MTSetting preferencesMtSetting = new Preferences_MTSetting();
        String w_CodeOrder = preferencesMtSetting.readSettingString(getApplication().getBaseContext(), preferencesMtSetting.getCodeOrder());
        SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        Log.e(logeTAG, "КодRN=" + w_CodeOrder);
        String query = "SELECT DISTINCT Kod_RN, sklad_uid, sklad_name FROM base_RN WHERE Kod_RN = '" + w_CodeOrder + "';";
        Pair<String, String> dataSkkad;
        int k_rn = 0;
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 1) {
            Log.e(logeTAG, "STATUS = Start INSERT");
            String Kod_RN = cursor.getString(cursor.getColumnIndexOrThrow("Kod_RN"));
            String uid_sklad = cursor.getString(cursor.getColumnIndexOrThrow("sklad_uid"));
            String name_sklad = cursor.getString(cursor.getColumnIndexOrThrow("sklad_name"));
            writeRNAllInsertSingle(Kod_RN, name_sklad, uid_sklad);
            Log.e(logeTAG, "STATUS = End INSERT");
        } else {
            Log.e(logeTAG, "STATUS = Start Update");
            while (!cursor.isAfterLast()) {
                String Kod_RN = cursor.getString(cursor.getColumnIndexOrThrow("Kod_RN"));
                String uid_sklad = cursor.getString(cursor.getColumnIndexOrThrow("sklad_uid"));
                String name_sklad = cursor.getString(cursor.getColumnIndexOrThrow("sklad_name"));
                String prefix_kod = Kod_RN.substring(0, Kod_RN.length() - 4);
                Integer number_kod = Integer.parseInt(Kod_RN.substring(12));
                Integer new_number_kod = number_kod + k_rn;
                Log.e(logeTAG, "Префикс код: " + prefix_kod);
                Log.e(logeTAG, "Номер код: " + number_kod);
                Log.e(logeTAG, "Новый код: " + new_number_kod);
                Log.e(logeTAG, "Длина кода: " + new_number_kod.toString().length());

                dataSkkad = new Pair<>(uid_sklad, name_sklad);
                sqlNewKod.put(prefix_kod + stringNewkod(new_number_kod), dataSkkad);

                Log.e(logeTAG, "Forma_Kod=" + "Новый код накладной" + stringNewkod(new_number_kod));
                k_rn++;
                cursor.moveToNext();
            }
            cursor.close();
            for (Map.Entry<String, Pair<String, String>> item : sqlNewKod.entrySet())
                Log.e(logeTAG, "Sklad=" + item.getKey() + "_" + item.getValue().first + "_" + item.getValue().second);

            ContentValues localContentValuesUP = new ContentValues();
            String query_new = "SELECT koduid, Kod_RN, sklad_uid FROM base_RN WHERE Kod_RN = '" + w_CodeOrder + "';";
            final Cursor cursor_new = db.rawQuery(query_new, null);
            cursor_new.moveToFirst();
            while (!cursor_new.isAfterLast()) {
                String koduid = cursor_new.getString(cursor_new.getColumnIndexOrThrow("koduid"));
                String uid_sklad = cursor_new.getString(cursor_new.getColumnIndexOrThrow("sklad_uid"));
                String Kod_RN = cursor_new.getString(cursor_new.getColumnIndexOrThrow("Kod_RN"));
                for (Map.Entry<String, Pair<String, String>> item : sqlNewKod.entrySet()) {
                    if (uid_sklad.equals(item.getValue().first)) {
                        Log.e(logeTAG, "YES Sklad=" + item.getValue().first + " RN=" + item.getKey());
                        localContentValuesUP.put("Kod_RN", item.getKey());
                        db.update("base_RN", localContentValuesUP, "Kod_RN = ? AND koduid = ?", new String[]{Kod_RN, koduid});
                    } else
                        Log.e(logeTAG, "NET Sklad=" + item.getValue().first + " RN=" + item.getKey());
                }
                cursor_new.moveToNext();
            }
            cursor_new.close();
            for (Map.Entry<String, Pair<String, String>> item : sqlNewKod.entrySet())
                writeRNAllInsertMulti(item.getKey(), item.getValue().second, item.getValue().first);
        }


        db.close();
    }

    //// Заполнить основую шапку по заказу
    protected void writeRNAllInsertSingle(String kodUIDRN, String SkladName, String SkladUID) {
        PreferencesWrite preferencesWrite = new PreferencesWrite(getApplication().getBaseContext());
        Preferences_MTSetting preferencesMtSetting = new Preferences_MTSetting();
        Context context = getApplication().getBaseContext();
        CalendarThis calendarThis = new CalendarThis();
        SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        try {
            String queryStatus = "INSERT INTO base_RN_All (kod_rn, agent_name, agent_uid, k_agn_uid, k_agn_name, data_xml, credit, sklad, sklad_uid, cena_price, coment, uslov_nds, skidka_title, credite_date, k_agn_adress, data, vrema, data_up, summa, skidka, status, debet_new, itogo) VALUES " +
                    /* kod_rn */       "('" + kodUIDRN + "', " +
                    /* agent_name */    "'" + preferencesWrite.Setting_AG_NAME + "', " +
                    /* agent_uid */     "'" + preferencesWrite.Setting_AG_UID + "', " +
                    /* k_agn_uid */     "'" + preferencesMtSetting.readSettingString(context, preferencesMtSetting.getClientUID()) + "', " +
                    /* k_agn_name */    "'" + preferencesMtSetting.readSettingString(context, preferencesMtSetting.getClientName().replaceAll("'", "*")) + "', " +
                    /* data_xml */      "'" + calendarThis.getThis_DateFormatXML + "', " +
                    /* credit */        "'" + preferencesMtSetting.readSettingString(context, preferencesMtSetting.getInfoOrderCredit()) + "', " +
                    /* sklad */         "'" + SkladName + "', " +
                    /* sklad_uid */     "'" + SkladUID + "', " +
                    /* cena_price */    "'" + preferencesWrite.Setting_AG_CENA + "', " +
                    /* coment */        "'" + getCreateComent() + "', " +
                    /* uslov_nds */     "'" + preferencesMtSetting.readSettingInt(context, preferencesMtSetting.getTypeOrder()) + "', " +
                    /* skidka_title */  "'" + SelectTypeSale() + "', " + /////?????????
                    /* credite_date */  "'" + preferencesMtSetting.readSettingInt(context, preferencesMtSetting.getInfoOrderCreditCountDay()) + "', " +
                    /* k_agn_adress */  "'" + preferencesMtSetting.readSettingString(context, preferencesMtSetting.getClientAdress()) + "', " +
                    /* data */          "'" + calendarThis.getThis_DateFormatSqlDB + "', " +
                    /* vrema */         "'" + calendarThis.getThis_DateFormatVrema + "', " +
                    /* data_up */       "'" + preferencesMtSetting.readSettingString(context, preferencesMtSetting.getDateDelivery()) + "', " +
                    /* Summa*/          "'" + SQL_Summa_RN(kodUIDRN).first + "', " +
                    /* skidka */        "'" + SelectTypeSale() + "', " +
                    /* status */        "'false', " +
                    /* debet_new */     "'null', " +
                    /* itogo */        "'" + SQL_Summa_RN(kodUIDRN).second + "');";
            final Cursor cursorStatus = db.rawQuery(queryStatus, null);
            cursorStatus.moveToLast();
            cursorStatus.close();
            db.close();
        } catch (Exception e) {
            Log.e(logeTAG, "Error:" + e);
        }

    }

    protected void writeRNAllInsertMulti(String newKodRN, String skladName, String skladUID) {
        PreferencesWrite preferencesWrite = new PreferencesWrite(getApplication().getBaseContext());
        SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        Preferences_MTSetting preferencesMtSetting = new Preferences_MTSetting();
        Context context = getApplication().getBaseContext();
        CalendarThis calendarThis = new CalendarThis();
        Log.e(logeTAG, "STATUS = Write Insert Multi");
        String queryStatus = "INSERT INTO base_RN_All (kod_rn, agent_name, agent_uid, k_agn_uid, " +
                "k_agn_name, data_xml, credit, sklad, sklad_uid, cena_price, coment, " +
                "uslov_nds, skidka_title, credite_date, k_agn_adress, data, vrema, data_up, summa, skidka, status, debet_new, itogo) " + "VALUES (" +
                /* kod_rn */        "'" + newKodRN + "', " +
                /* agent_name */    "'" + preferencesWrite.Setting_AG_NAME.replaceAll("'", "*") + "', " +
                /* agent_uid */     "'" + preferencesWrite.Setting_AG_UID + "', " +
                /* k_agn_uid */     "'" + preferencesMtSetting.readSettingString(context, preferencesMtSetting.getClientUID()) + "', " +
                /* k_agn_name */    "'" + preferencesMtSetting.readSettingString(context, preferencesMtSetting.getClientName().replaceAll("'", "*")) + "', " +
                /* data_xml */      "'" + preferencesMtSetting.readSettingString(context, preferencesMtSetting.getDateDelivery()) + "', " +
                /* credit */        "'" + preferencesMtSetting.readSettingString(context, preferencesMtSetting.getInfoOrderCredit()) + "', " +
                /* sklad */         "'" + skladName + "', " +
                /* sklad_uid */     "'" + skladUID + "', " +
                /* cena_price */    "'" + preferencesWrite.Setting_AG_CENA + "', " +
                /* coment */        "'" + getCreateComent() + "', " +
                /* uslov_nds */     "'" + preferencesMtSetting.readSettingInt(context, preferencesMtSetting.getTypeOrder()) + "', " +
                /* skidka_title */  "'" + SelectTypeSale() + "', " + /////?????????
                /* credite_date */  "'" + preferencesMtSetting.readSettingInt(context, preferencesMtSetting.getInfoOrderCreditCountDay()) + "', " +
                /* k_agn_adress */  "'" + preferencesMtSetting.readSettingString(context, preferencesMtSetting.getClientAdress()) + "', " +
                /* data */           "'" + calendarThis.getThis_DateFormatSqlDB + "', " +
                /* vrema */         "'" + calendarThis.getThis_DateFormatVrema + "', " +
                /* data_up */       "'" + preferencesMtSetting.readSettingString(context, preferencesMtSetting.getDateDelivery()) + "', " +
                /* summa */         "'" + SQL_Summa_RN(newKodRN).first + "', " +
                /* skidka */        "'" + SelectTypeSale() + "', " +
                /* status */        "'false', " +
                /* debet_new */     "'null', " +
                /* itogo */        "'" + SQL_Summa_RN(newKodRN).second + "');";
        final Cursor cursorStatus = db.rawQuery(queryStatus, null);
        cursorStatus.moveToLast();
        cursorStatus.close();
        db.close();
    }


    //// обработка данных для редактирования заказа
    protected void edit_NewRN(PreferencesWrite preferencesWrite) {
        //// Добавить на склад товар из таблицы BaseRN
        Preferences_MTSetting preferencesMtSetting = new Preferences_MTSetting();
        Context context = getApplication().getBaseContext();
        String wCodeOrder = preferencesMtSetting.readSettingString(context, preferencesMtSetting.getCodeOrder());
        String wClientUID = preferencesMtSetting.readSettingString(context, preferencesMtSetting.getClientUID());
        SQLiteDatabase dbRN = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        SQLiteDatabase dbBASE = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT * FROM base_RN WHERE Kod_RN = '" + wCodeOrder + "';";
        Cursor cursor = dbRN.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String skladUID = cursor.getString(cursor.getColumnIndexOrThrow("sklad_uid"));
            String tovarUID = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));
            String tovarCount = cursor.getString(cursor.getColumnIndexOrThrow("Kol"));
            dbBASE.execSQL("UPDATE base_in_ostatok SET count = count+'" + tovarCount + "' WHERE sklad_uid = '" + skladUID + "' AND nomenclature_uid = '" + tovarUID + "';");
            cursor.moveToNext();
        }
        dbBASE.close();

        //// Вычесть сумму заказа из таблицы долгов
        dbRN.execSQL("UPDATE otchet_debet " +
                "SET d_summa = d_summa- (SELECT SUM(Itogo) FROM base_RN WHERE Kod_RN = '" + wCodeOrder + "') " +
                "WHERE d_kontr_uid = '" + wClientUID + "';");

        cursor = dbRN.rawQuery("SELECT * FROM otchet_debet WHERE d_kontr_uid = '" + wClientUID + "';", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            String summa = cursor.getString(cursor.getColumnIndexOrThrow("d_summa"));
            String clientUID = cursor.getString(cursor.getColumnIndexOrThrow("d_kontr_uid"));
            double d_sum = Double.parseDouble(summa);
            if (d_sum < 0) {
                dbRN.execSQL("UPDATE otchet_debet SET d_summa = '0' WHERE d_kontr_uid = '" + clientUID + "';");
            }
        }

        cursor.close();

        //// Удалить заказ из таблицы BaseRN и записать новые из таблицы BaseRNEdit
        dbRN.execSQL("DELETE FROM base_RN WHERE Kod_RN = '" + wCodeOrder + "';");
        dbRN.close();

        try {

        } catch (SQLException ex) {
            Log.e(logeTAG, "Ошибка SQL(работа со старым заказом):" + ex);
        } catch (Exception e) {
            Log.e(logeTAG, "Ошибка(работа со старым заказом):" + e);
        }
    }

    //// Копируем новый заказ из редактируемой базы
    protected void update_InsertTableEdit(String thisUpdateRN) {
        try {
            PreferencesWrite preferencesWrite = new PreferencesWrite(getApplication().getBaseContext());
            SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
            db.execSQL("INSERT INTO base_RN (Kod_RN, Vrema, Data, Kod_Univ, koduid, Image, Name, Kol, Cena, Summa, Skidka, Cena_SK, Itogo, aks_pref, aks_name, sklad_name, sklad_uid)\n" +
                    "SELECT Kod_RN, Vrema, Data, Kod_Univ, koduid, Image, Name, Kol, Cena, Summa, Skidka, Cena_SK, Itogo, aks_pref, aks_name, sklad_name, sklad_uid FROM base_RN_Edit WHERE Kod_RN = '" + thisUpdateRN + "' ORDER BY Name ASC;");
            db.close();
        } catch (SQLException ex) {
            Log.e(logeTAG, "Ошибка WriteTableSQL:" + ex);
        } catch (Exception e) {
            Log.e(logeTAG, "Ошибка WriteTableSQLE:" + e);
        }

        update_ReWriteTable_RnAll();
    }

    protected void update_ReWriteTable_RnAll() {
        Log.e(logeTAG, "Меняем шапку профеля (base_RN_All)");
        PreferencesWrite preferencesWrite = new PreferencesWrite(getApplication().getBaseContext());
        Preferences_MTSetting preferencesMtSetting = new Preferences_MTSetting();
        Context context = getApplication().getBaseContext();
        CalendarThis calendarThis = new CalendarThis();
        SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);

        String w_DateXML = calendarThis.getThis_DateFormatXML;
        String w_DateFormatSQL = calendarThis.getThis_DateFormatSqlDB;
        String w_DateTime = calendarThis.getThis_DateFormatVrema;
        String w_CreditType = preferencesMtSetting.readSettingString(context, preferencesMtSetting.getInfoOrderCredit());
        String w_CreditDay = String.valueOf(preferencesMtSetting.readSettingInt(context, preferencesMtSetting.getInfoOrderCreditCountDay()));
        String w_DateDelivety = preferencesMtSetting.readSettingString(context, preferencesMtSetting.getDateDelivery());
        String w_Sale = String.valueOf(preferencesMtSetting.readSettingInt(context, preferencesMtSetting.getSaleCount()));
        String w_Comment = preferencesMtSetting.readSettingString(context, preferencesMtSetting.getComment());
        String w_CodeOrder = preferencesMtSetting.readSettingString(context, preferencesMtSetting.getCodeOrder());
        String w_TypeOrder = String.valueOf(preferencesMtSetting.readSettingInt(context, preferencesMtSetting.getTypeOrder()));





        try {
            db.execSQL("UPDATE base_RN_All " +
                    "SET " + "data_xml = '" + w_DateXML + "',  " +
                    "credit = '" + w_CreditType + "',  " +
                    "credite_date = '" + w_CreditDay + "', " +
                    "skidka_title = '" + w_Sale + "', " +
                    "coment = '" + "Дата отгрузки: " + w_DateDelivety + "; Cкидка: " + w_Sale + "%;cmn_" + w_Comment + "', " +
                    "uslov_nds = '" + w_TypeOrder + "', " +
                    "data = '" + w_DateFormatSQL + "', " +
                    "vrema = '" + w_DateTime + "', " +
                    "summa = '" + SQL_Summa_RN(w_CodeOrder).first + "', " +
                    "skidka = '" + w_Sale + "', " +
                    "data_up = '" + w_DateDelivety + "', " +
                    "itogo = '" + SQL_Summa_RN(w_CodeOrder).second + "'" + "WHERE kod_rn = '" + w_CodeOrder + "';");
            db.close();
        } catch (SQLException ex) {
            Log.e(logeTAG, "Ошибка профеляSQL:" + ex);
        } catch (Exception e) {
            Log.e(logeTAG, "Ошибка профеляSQLE:" + e);
        }
        Log.e(logeTAG, "конекц Меняем шапку профеля (base_RN_All)");
    }


    //// Очистка параметров в памяти
    protected void clearPermisionZakaz() {
        mSettings = getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        editor = mSettings.edit();
        //// Сброс данных из настроек
        editor.putString("PEREM_K_AG_NAME", "");
        editor.putString("PEREM_K_AG_UID", "");
        editor.putString("PEREM_K_AG_ADRESS", "");
        editor.putString("PEREM_K_AG_KodRN", "");
        editor.putString("PEREM_K_AG_Data", "");
        editor.putString("PEREM_K_AG_Data_WORK", "");
        editor.putString("PEREM_K_AG_Vrema", "");
        editor.putString("PEREM_K_AG_GPS", "");


        editor.putString("setting_TY_CREDIT", "");
        editor.putString("setting_TY_CREDITE_DATE", "");
        editor.putString("preference_TypeSale", "standart");
        editor.putInt("setting_TY_TypeRelise", 0);
        editor.putString("setting_TY_COMMENT", "");
        editor.putString("setting_TY_DateNextUP", "");
        editor.putString("setting_EndZakaz_Itogo", "");
        editor.commit();

        Preferences_MTSetting preferencesMtSetting = new Preferences_MTSetting();
        preferencesMtSetting.clearSetting(getApplication().getBaseContext());
    }

    ////  список: uid-склад, товар и его код, для вычита остатка
    protected void selectListTovar(String kodRN) {
        ArrayList<Params> arrayList = new ArrayList<>();
        try {
            PreferencesWrite preferencesWrite = new PreferencesWrite(getApplication().getBaseContext());
            SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
            String query = "SELECT Kod_RN, sklad_uid, sklad_name, Name, koduid, Kol FROM base_RN WHERE Kod_RN = '" + kodRN + "'";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String skladUID = cursor.getString(cursor.getColumnIndexOrThrow("sklad_uid"));
                String tovarName = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                String tovarUID = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));
                String tovarCount = cursor.getString(cursor.getColumnIndexOrThrow("Kol"));
                arrayList.add(new Params(skladUID, tovarName, tovarUID, tovarCount));
                cursor.moveToNext();
            }
            cursor.close();
            db.close();

        } catch (Exception e) {
            Log.e(logeTAG, "Ошибка, при заполнении списка товара для обновления остатков");
        }

        rewriteCountOstatok(arrayList);
    }

    //// Минусовать товара из остатков
    protected void rewriteCountOstatok(ArrayList<Params> arrayList) {
        try {
            PreferencesWrite preferencesWrite = new PreferencesWrite(getApplication().getBaseContext());
            SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_BASE, MODE_PRIVATE, null);
            String query = "SELECT * FROM base_in_ostatok";
            final Cursor cursor = db.rawQuery(query, null);
            for (Params list : arrayList) {
                ContentValues localContentValuesUP = new ContentValues();
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String sklad_uid = cursor.getString(cursor.getColumnIndexOrThrow("sklad_uid"));
                    String nomenclature_uid = cursor.getString(cursor.getColumnIndexOrThrow("nomenclature_uid"));
                    String count = cursor.getString(cursor.getColumnIndexOrThrow("count"));
                    if (list.sklad_uid.equals(sklad_uid) && list.tovarUID.equals(nomenclature_uid)) {
                        int endCount = Integer.parseInt(count) - Integer.parseInt(list.tovarCount);
                        Log.e(logeTAG, "Tovar= " + list.tovarName);
                        Log.e(logeTAG, "Count=" + Integer.parseInt(count) + "_" + Integer.parseInt(list.tovarCount) + "_" + endCount);
                        if (endCount > 0) {
                            localContentValuesUP.put("sklad_uid", list.sklad_uid);
                            localContentValuesUP.put("nomenclature_uid", list.tovarUID);
                            localContentValuesUP.put("count", endCount);
                            db.update("base_in_ostatok", localContentValuesUP, "nomenclature_uid = ? AND sklad_uid = ?", new String[]{list.tovarUID, list.sklad_uid});
                        } else
                            db.delete("base_in_ostatok", "nomenclature_uid = ? AND sklad_uid = ?", new String[]{list.tovarUID, list.sklad_uid});
                    }
                    cursor.moveToNext();
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.e(logeTAG, "Ошибка, удаление позиции из остатков");
        }

    }


    protected Pair<String, String> SQL_Summa_RN(String w_uid) {
        PreferencesWrite preferencesWrite = new PreferencesWrite(getApplication().getBaseContext());
        SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT SUM (base_RN.Summa) AS 'sum_rn', SUM(base_RN.Itogo) AS 'itogo_rn' FROM base_RN WHERE Kod_RN = '" + w_uid + "';";
        final Cursor cursor = db.rawQuery(query, null);
        String summa_new_rn = "", itogo_new_rn = "";
        cursor.moveToFirst();
        if (cursor != null) {
            summa_new_rn = cursor.getString(cursor.getColumnIndexOrThrow("sum_rn"));
            itogo_new_rn = cursor.getString(cursor.getColumnIndexOrThrow("itogo_rn"));
        }
        cursor.close();
        db.close();
/*        Log.e(logeTAG, "SQL_Summa_RN.." + "Накладная:" + w_uid);
        Log.e(logeTAG, "SQL_Summa_RN.." + "Сумма накладной:" + summa_new_rn);*/
        return new Pair<>(summa_new_rn.replaceAll(",", "."), itogo_new_rn.replaceAll(",", "."));
    }

    protected String stringNewkod(Integer new_number_kod) {
        String new_kod_rn = "";
        if (new_number_kod.toString().length() == 1) {
            new_kod_rn = "000" + new_number_kod;
        } else if (new_number_kod.toString().length() == 2) {
            new_kod_rn = "00" + new_number_kod;
        } else if (new_number_kod.toString().length() == 3) {
            new_kod_rn = "0" + new_number_kod;
        } else if (new_number_kod.toString().length() == 4) {
            new_kod_rn = "" + new_number_kod;
        }
        return new_kod_rn;
    }

    //// Создание строки для комментариев
    protected String getCreateComent() {
        Preferences_MTSetting prefMtSetting = new Preferences_MTSetting();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Дата отгрузки: ")
                .append(prefMtSetting.readSettingString(getApplication(), prefMtSetting.getDateDelivery()))
                .append("; Cкидка: ")
                .append(SelectTypeSale())
                .append("%;cmn_")
                .append(prefMtSetting.readSettingString(getApplication(), prefMtSetting.getComment()));
        // "Дата отгрузки: " + prefMtSetting.readSettingString(getApplication(), prefMtSetting.getDateDelivery()) + "; Cкидка: " + SelectTypeSale() + "%;cmn_" + )
        return stringBuilder.toString();
    }

/*    protected String Status_Query() {
        String perem_rewrite_rn;
        PreferencesWrite preferencesWrite = new PreferencesWrite(getApplication().getBaseContext());
        SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String query_select = "SELECT base_RN_All.kod_rn, base_RN_All.data FROM base_RN_All WHERE base_RN_All.kod_rn LIKE '%" + preferencesWrite.Setting_MT_K_AG_KodRN + "'";

        final Cursor cursor = db.rawQuery(query_select, null);
        if (cursor.getCount() > 0) {
            Log.e(logeTAG, "NReWrite_KodRN" + "Есть совпадений");
            perem_rewrite_rn = "query_update";
        } else {
            Log.e(logeTAG, "NReWrite_KodRN" + "Нет совпадений");
            perem_rewrite_rn = "query_insert";
        }
        cursor.close();
        db.close();
        return perem_rewrite_rn;
    }*/

    //// Возвращает скидку при условии что сумма больше мин закупа, либо 0
    protected int SelectTypeSale() {
        PreferencesWrite preferencesWrite = new PreferencesWrite(getApplication().getBaseContext());
        Preferences_MTSetting prefsMtSetting = new Preferences_MTSetting();
        double dItogo = Double.parseDouble(prefsMtSetting.readSettingString(getApplication(), prefsMtSetting.getAutoSum_Itogo())),
                dMinSumm = Double.parseDouble(preferencesWrite.Setting_TY_SaleMinSumSale);

        Log.e(logeTAG, "Itogo" + dItogo);
        Log.e(logeTAG, "MinSum" + dMinSumm);
        int sale = 0;
        switch (preferencesWrite.Setting_TY_SaleType) {
            case "standart":
                if (dItogo >= dMinSumm) sale = Integer.parseInt(preferencesWrite.Setting_TY_Sale);
                break;
            case "Farms":
                if (dItogo >= dMinSumm)
                    sale = Integer.parseInt(preferencesWrite.Setting_TY_SaleFarm);
                break;
            case "Random":
                sale = preferencesWrite.Setting_TY_SaleRandom;
                break;
        }
        return sale;
    }

    class Params {
        private String sklad_uid;
        private String tovarName;
        private String tovarUID;
        private String tovarCount;


        public Params(String sklad_uid, String tovarName, String tovarUID, String tovarCount) {

            this.sklad_uid = sklad_uid;
            this.tovarUID = tovarUID;
            this.tovarCount = tovarCount;
            this.tovarName = tovarName;
        }


    }
}