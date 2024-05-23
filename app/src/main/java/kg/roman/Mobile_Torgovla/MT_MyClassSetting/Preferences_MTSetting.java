package kg.roman.Mobile_Torgovla.MT_MyClassSetting;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

public class Preferences_MTSetting {
    private SharedPreferences.Editor editor;
    private SharedPreferences wSettings;
    private final String APP_PREFERENCES_WriteRN = "MT_SettingforFormaZakaza";

    public void writeSettingInt(Context context, String keySetting, int valueSetting) {
        wSettings = context.getSharedPreferences(APP_PREFERENCES_WriteRN, MODE_PRIVATE);
        editor = wSettings.edit();
        editor.putInt(keySetting, valueSetting);
        editor.commit();
    }

    public void writeSettingString(Context context, String keySetting, String valueSetting) {
        wSettings = context.getSharedPreferences(APP_PREFERENCES_WriteRN, MODE_PRIVATE);
        editor = wSettings.edit();
        editor.putString(keySetting, valueSetting);
        editor.commit();
    }

    public int readSettingInt(Context context, String keySetting) {
        wSettings = context.getSharedPreferences(APP_PREFERENCES_WriteRN, MODE_PRIVATE);
        return wSettings.getInt(keySetting, 0);
    }

    public String readSettingString(Context context, String keySetting) {
        wSettings = context.getSharedPreferences(APP_PREFERENCES_WriteRN, MODE_PRIVATE);
        return wSettings.getString(keySetting, "0");
    }

    public void clearSetting(Context context) {
        wSettings = context.getSharedPreferences(APP_PREFERENCES_WriteRN, MODE_PRIVATE);
        editor = wSettings.edit();
        editor.clear();
        editor.commit();
    }

    public void writeDopData(Context context, String ty_Select, int ty_Sale, int typeOrder, String dateUp, String typePay, int dayPay, String comment)
    {
        wSettings = context.getSharedPreferences(APP_PREFERENCES_WriteRN, MODE_PRIVATE);
        editor = wSettings.edit();
        editor.putString(getTYSelectRadioButton(), ty_Select); // Выбор торговых условий
        editor.putInt(getSaleCount(), ty_Sale);               // размер скидки
        editor.putInt(getTypeOrder(), typeOrder);             // вариант реализации(счет-ф)
        editor.putString(getDateDelivery(), dateUp);          // Дата отгрузки товара
        editor.putString(getInfoOrderCredit(), typePay);      // вид оплаты
        editor.putInt(getInfoOrderCreditCountDay(), dayPay);  // дней консигнаиций
        editor.putString(getComment(), comment);                 // коментарий
        editor.commit();
    }



    //// Создание данных об заказе(Inclaud)
    public HashMap<String, String> getAutoSum(Context context) {
        HashMap<String, String> hashSet = new HashMap<>();
        hashSet.clear();
        PreferencesWrite preferencesWrite = new PreferencesWrite(context);
        SQLiteDatabase db = context.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        hashSet.put("sum", getSummaOrder(context, db));
        hashSet.put("count", getCountOrder(context, db));
        hashSet.put("itg", getItogoOrder(context, db));
        db.close();
        return hashSet;
    }

    public String getSummaOrder(Context context, SQLiteDatabase db) {
        String returnText;
        String querySumma = "SELECT Kod_RN, SUM(Summa) AS 'AvtoSum' FROM '" + SelectTableOrderEdit(context) + "' WHERE Kod_RN = '" + readSettingString(context, getCodeOrder()) + "';";
        Cursor cursor = db.rawQuery(querySumma, null);
        //// Подсчет общей суммы
        cursor.moveToFirst();
        if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndexOrThrow("AvtoSum")) != null) {
            String s = cursor.getString(cursor.getColumnIndexOrThrow("AvtoSum"));
            returnText = String.valueOf(Double.parseDouble(s));
        } else returnText = "00.00";
        cursor.close();
        return returnText;
    }

    public String getCountOrder(Context context, SQLiteDatabase db) {
        String returnText;
        String queryCount = "SELECT Kod_RN, SUM(Kol) AS 'AvtoCount' FROM '" + SelectTableOrderEdit(context) + "' WHERE Kod_RN = '" + readSettingString(context, getCodeOrder()) + "';";
        //// Подсчет общего кол-ва
        Cursor cursor = db.rawQuery(queryCount, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndexOrThrow("AvtoCount")) != null) {
            returnText = cursor.getString(cursor.getColumnIndexOrThrow("AvtoCount")) + " шт";
        } else returnText = "0";
        return returnText;
    }

    public String getItogoOrder(Context context, SQLiteDatabase db) {
        String returnText;
        String queryItogo = "SELECT Kod_RN, SUM(Itogo) AS 'AvtoItogo' FROM '" + SelectTableOrderEdit(context) + "' WHERE Kod_RN = '" + readSettingString(context, getCodeOrder()) + "';";
        //// Подсчет общего суммы со скидкой
        Cursor cursor = db.rawQuery(queryItogo, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndexOrThrow("AvtoItogo")) != null) {
            String s = cursor.getString(cursor.getColumnIndexOrThrow("AvtoItogo"));
            returnText = String.valueOf(Double.parseDouble(s));
        } else returnText = "00.00";
        cursor.close();
        return returnText;
    }

    protected String SelectTableOrderEdit(Context context) {
        if (readSettingString(context, getStatusOrder()).equals("Edit"))
            return "base_RN_Edit";
        else return "base_RN";
    }




    //// Геттеры с ключами к доступу к настройкам
    public String getClientName() {
        return "setting_keyClientName";
    }

    public String getClientUID() {
        return "setting_keyClientUID";
    }

    public String getClientAdress() {
        return "setting_keyClientAdress";
    }

    public String getCodeOrder() {
        return "setting_keyCodeOrder";
    }

    public String getInfoOrderCredit() {
        return "setting_keyCredit";
    }

    public String getInfoOrderCreditCountDay() {
        return "setting_keyCreditCountDay";
    }

    public String getSkladName() {
        return "setting_keySkladName";
    }

    public String getSkladUID() {
        return "setting_keySkladUID";
    }

    public String getSaleCount() {
        return "setting_keySaleCount";
    }

    public String getTypeOrder() {
        return "setting_keyTypeOrder";
    }

    public String getCommentAll() {
        return "setting_keyCommentAll";
    }

    public String getComment() {
        return "setting_keyComment";
    }

    public String getDateDelivery() {
        return "setting_DateDelivery";
    }

    public String getDateWork() {  return "setting_keyDateWorkSQL"; }
    public String getDateWorkXML() {  return "setting_keyDateWorkXML"; }
    public String getDateTime() {  return "setting_keyDateTime"; }


    public String getStatusOrder() {return "setting_keyStatusOrder";}  // Описание статуса заказа (Create, Edit, Copy)

    public String getTYSelectRadioButton() {
        return "setting_SelectTY";
    }

    public String getAutoSum_Itogo() {
        return "setting_keyItogo";
    }

    public String getMyGPS() {
        return "setting_keyGPS";
    }



}
