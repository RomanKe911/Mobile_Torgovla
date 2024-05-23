package kg.roman.Mobile_Torgovla.FormaZakazaStart;


import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.MT_MyClassSetting.CalendarThis;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.PreferencesWrite;

interface InterfaceEditor {
    String editorCopy(String newKod);

    void editorEdit();

    void editorScreen();

    //// Обработка удаления накладной из базы данных
    void editorDelete(String clientUID, String kodRN, String clientSum);
}

public class EditorFormaZakaza implements InterfaceEditor {
    String logeTAG = "Editor";
    SQLiteDatabase db;
    Context contextThis;

    public EditorFormaZakaza(Context context) {
        this.contextThis = context;
    }

    @Override
    public String editorCopy(String newKODRN) {
        //// создание копии заказа с новым кодом накладной
        String messegeText = "null";
        try {
            PreferencesWrite preferencesWrite = new PreferencesWrite(contextThis);
            SQLiteDatabase db = contextThis.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
            CalendarThis calendarThis = new CalendarThis();
            ContentValues contentValuesInsert = new ContentValues();
            //// Создаем копию таблицы товара
            String queryPosition = "SELECT * FROM base_RN WHERE Kod_RN = '" + preferencesWrite.Select_OldKodRN + "';";
            Cursor cursor = db.rawQuery(queryPosition, null);
            cursor.moveToFirst();
            contentValuesInsert.clear();
            while (!cursor.isAfterLast()) {
                contentValuesInsert.put("Kod_RN", newKODRN);
                contentValuesInsert.put("Vrema", calendarThis.getThis_DateFormatVrema);
                contentValuesInsert.put("Data", calendarThis.getThis_DateFormatSqlDB);
                contentValuesInsert.put("Kod_Univ", cursor.getString(cursor.getColumnIndexOrThrow("Kod_Univ")));
                contentValuesInsert.put("koduid", cursor.getString(cursor.getColumnIndexOrThrow("koduid")));
                contentValuesInsert.put("Image", cursor.getString(cursor.getColumnIndexOrThrow("Image")));
                contentValuesInsert.put("Name", cursor.getString(cursor.getColumnIndexOrThrow("Name")));
                contentValuesInsert.put("Kol", cursor.getString(cursor.getColumnIndexOrThrow("Kol")));
                contentValuesInsert.put("Cena", cursor.getString(cursor.getColumnIndexOrThrow("Cena")));
                contentValuesInsert.put("Summa", cursor.getString(cursor.getColumnIndexOrThrow("Summa")));
                contentValuesInsert.put("Skidka", "0");
                contentValuesInsert.put("Cena_SK", cursor.getString(cursor.getColumnIndexOrThrow("Cena")));
                contentValuesInsert.put("Itogo", cursor.getString(cursor.getColumnIndexOrThrow("Summa")));
                contentValuesInsert.put("aks_pref", cursor.getString(cursor.getColumnIndexOrThrow("aks_pref")));
                contentValuesInsert.put("aks_name", cursor.getString(cursor.getColumnIndexOrThrow("aks_name")));
                contentValuesInsert.put("sklad_name", cursor.getString(cursor.getColumnIndexOrThrow("sklad_name")));
                contentValuesInsert.put("sklad_uid", cursor.getString(cursor.getColumnIndexOrThrow("sklad_uid")));
                db.insert("base_RN", null, contentValuesInsert);
                cursor.moveToNext();
            }
            Log.e(logeTAG, "Создана копия таблици позиций заказа");
            cursor.close();
            db.close();
        } catch (SQLException sqlException) {
            Log.e(logeTAG, "Error: " + sqlException);
            messegeText = "Error: " + sqlException;
        } catch (Exception e) {
            Log.e(logeTAG, "Error: " + e);
            messegeText = "Error: " + e;
        }
        return messegeText;
    }

    @Override
    public void editorEdit() {

    }

    @Override
    public void editorScreen() {

    }


    @Override
    public void editorDelete(String clientUID, String kodRN, String clientSum) {
        PreferencesWrite preferencesWrite = new PreferencesWrite(contextThis);

        //// Вычистание из задолжности удаленую накладную
        SQLiteDatabase db = contextThis.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        db.execSQL("UPDATE otchet_debet SET d_summa = d_summa-'" + clientSum + "' WHERE d_kontr_uid= (SELECT d_kontr_uid FROM otchet_debet WHERE d_kontr_uid = '" + clientUID + "');");

        //// Добавление товара на склад из удаленной наклданой
        db = contextThis.openOrCreateDatabase(preferencesWrite.PEREM_DB3_BASE, MODE_PRIVATE, null);
        ArrayList<ListTovar> strings = new ArrayList<>(createListTovar(kodRN));
        for (ListTovar listTovar : strings) {
            int tovarCountSklad = selectTovarCount(listTovar.tovarUID, listTovar.tovarSklad);
            if (tovarCountSklad != 0) {
                db.execSQL("UPDATE base_in_ostatok SET count = count +'" + listTovar.tovarCount + "' " +
                        "WHERE nomenclature_uid = (SELECT nomenclature_uid FROM base_in_ostatok " +
                        "WHERE nomenclature_uid = '" + listTovar.tovarUID + "' AND sklad_uid = '" + listTovar.tovarSklad + "');");
            } else
                db.execSQL("INSERT INTO base_in_ostatok (data, sklad_uid, nomenclature_uid, name, count)" +
                        " VALUES ('" + listTovar.date + "', '" + listTovar.tovarSklad + "', '" + listTovar.tovarUID + "', '" + listTovar.tovarName + "', '" + listTovar.tovarCount + "')");

        }
        //// Удаление накладных из базы
        db = contextThis.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        db.execSQL("DELETE FROM base_RN_ALL WHERE kod_rn='" + kodRN + "';");
        db.execSQL("DELETE FROM base_RN WHERE Kod_RN = '" + kodRN + "';");
        db.close();
    }

    //// Возвращает список позиций удаляемого заказа
    protected ArrayList<ListTovar> createListTovar(String kodRN) {
        ArrayList<ListTovar> list = new ArrayList<>();
        PreferencesWrite preferencesWrite = new PreferencesWrite(contextThis);
        CalendarThis calendarThis = new CalendarThis();
        SQLiteDatabase db = contextThis.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT * FROM base_RN WHERE Kod_RN= '" + kodRN + "' ORDER BY Name;";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String tovarUID = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));
            int tovarCount = cursor.getInt(cursor.getColumnIndexOrThrow("Kol"));
            String tovarSkladUID = cursor.getString(cursor.getColumnIndexOrThrow("sklad_uid"));
            String tovarName = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
            list.add(new ListTovar(calendarThis.getThis_DateFormatCountTovar, tovarUID, tovarCount, tovarSkladUID, tovarName));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return list;
    }

    //// Возвращает кол-во товара на складе
    protected int selectTovarCount(String tovarUID, String skladUID) {
        int count;
        PreferencesWrite preferencesWrite = new PreferencesWrite(contextThis);
        SQLiteDatabase db = contextThis.openOrCreateDatabase(preferencesWrite.PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT count FROM base_in_ostatok WHERE nomenclature_uid = '" + tovarUID + "' AND sklad_uid = '" + skladUID + "';";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0)
            count = cursor.getInt(cursor.getColumnIndexOrThrow("count"));
        else count = 0;
        cursor.close();
        db.close();
        return count;
    }

    class ListTovar {
        public String date;
        public String tovarSklad;

        public String tovarUID;
        public String tovarName;
        public int tovarCount;

        public ListTovar(String w_date, String w_tovarUID, int w_tovarCount, String w_tovarSklad, String w_tovarName) {
            this.date = w_date;
            this.tovarUID = w_tovarUID;
            this.tovarCount = w_tovarCount;
            this.tovarSklad = w_tovarSklad;
            this.tovarName = w_tovarName;
        }
    }

    class ListTovarCopy {
        public String kod_rn, vrema, date,
                kodUniv, kodUID, image, name,
                count, price, summa, skidka,
                priceSale, itogo, aksPref,
                aksName, skladUID, skladNAame;
        public ListTovarCopy(String wkodRN, String wTime, String wDate,
                             String wkodUNIV, String wkodUID, String wImage, String wName,
                             String wCount, String wPrice, String wSum, String wSale,
                             String wpriceSale, String wItogo,
                             String waksPref, String waskName, String wskladUID, String wskladName) {
            this.kod_rn = wkodRN;
            this.vrema = wTime;
            this.date = wDate;
            this.kodUniv = wkodUNIV;
            this.kodUID = wkodUID;
            this.image = wImage;
            this.name = wName;
            this.count = wCount;
            this.price = wPrice;
            this.summa = wSum;
            this.skidka = wSale;
            this.priceSale = wpriceSale;
            this.itogo = wItogo;
            this.aksPref = waksPref;
            this.aksName = waskName;
            this.skladUID = wskladUID;
            this.skladNAame = wskladName;

        }
    }



/*        //// Создаем копию шапки заказа
        String queryAll = "SELECT * FROM base_RN_All WHERE Kod_RN = '" + thisKODRN + "';";
        cursor = db.rawQuery(queryAll, null);
        cursor.moveToFirst();
        contentValuesInsert.clear();
        while (!cursor.isAfterLast()) {
            contentValuesInsert.put("kod_rn", newKODRN);
            contentValuesInsert.put("agent_name", cursor.getString(cursor.getColumnIndexOrThrow("agent_name")));
            contentValuesInsert.put("agent_uid", cursor.getString(cursor.getColumnIndexOrThrow("agent_uid")));
            contentValuesInsert.put("k_agn_name", cursor.getString(cursor.getColumnIndexOrThrow("k_agn_name")));
            contentValuesInsert.put("k_agn_uid", cursor.getString(cursor.getColumnIndexOrThrow("k_agn_uid")));
            contentValuesInsert.put("k_agn_adress", cursor.getString(cursor.getColumnIndexOrThrow("k_agn_adress")));
            contentValuesInsert.put("data_xml", calendarThis.getThis_DateFormatXML);
            contentValuesInsert.put("credit", cursor.getString(cursor.getColumnIndexOrThrow("credit")));
            contentValuesInsert.put("sklad", cursor.getString(cursor.getColumnIndexOrThrow("sklad")));
            contentValuesInsert.put("sklad_uid", cursor.getString(cursor.getColumnIndexOrThrow("sklad_uid")));
            contentValuesInsert.put("cena_price", cursor.getString(cursor.getColumnIndexOrThrow("cena_price")));
            contentValuesInsert.put("credite_date", cursor.getString(cursor.getColumnIndexOrThrow("credite_date")));
            contentValuesInsert.put("skidka_title", cursor.getString(cursor.getColumnIndexOrThrow("skidka_title")));
            contentValuesInsert.put("coment", cursor.getString(cursor.getColumnIndexOrThrow("coment")));
            contentValuesInsert.put("uslov_nds", cursor.getString(cursor.getColumnIndexOrThrow("uslov_nds")));

            contentValuesInsert.put("vrema", calendarThis.getThis_DateFormatVrema);
            contentValuesInsert.put("data", calendarThis.getThis_DateFormatSqlDB);
            contentValuesInsert.put("data_up", cursor.getString(cursor.getColumnIndexOrThrow("data_up")));
            contentValuesInsert.put("summa", cursor.getString(cursor.getColumnIndexOrThrow("summa")));
            contentValuesInsert.put("skidka", cursor.getString(cursor.getColumnIndexOrThrow("skidka")));
            contentValuesInsert.put("itogo", cursor.getString(cursor.getColumnIndexOrThrow("itogo")));
            contentValuesInsert.put("status", "false");
            contentValuesInsert.put("debet_new", cursor.getString(cursor.getColumnIndexOrThrow("debet_new")));
            db.insert("base_RN_All", null, contentValuesInsert);
            cursor.moveToNext();
        }
        Log.e(logeTAG, "Создана копия таблици позиций заказа");
        cursor.close();*/

}