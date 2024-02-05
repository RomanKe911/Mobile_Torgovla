package kg.roman.Mobile_Torgovla.TEST;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;

import kg.roman.Mobile_Torgovla.R;

public class WJ_Karakil_newRN extends AppCompatActivity {

    public ListView list;
    public Button btn_up, btn_write, btn_load;
    public Context context_Activity;
    public ArrayAdapter<String> adapter;
    public File fileName;
    public File sdPath;
    public OutputStream out;
    public SharedPreferences sp;
    public int cursor_id;
    public String new_rn_kod, new_tovar, new_tovar_kod, new_kol, new_cena, new_summa;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now;
    public XmlSerializer serializer;
    public String XML_NEW_NAME;
    public Cursor XML_Cursor;
    public ProgressDialog pDialog;
    public String[] mass_kod_rn, mass_kod_data;
    public String[] sql_table = {
            "table01_clients_1c_FULL", "table02_start_file", "table03_start_files_new",
            "table04_nomeclature_1c", "table05_end_db_title", "table06_end_db_name"};
    public String sql_name_db = "karakol_new_rn.db3";
    public String messeg_log = "Сообщение Log: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_new_rn);
        context_Activity = WJ_Karakil_newRN.this;

        btn_up = (Button) findViewById(R.id.btn_rn_up);
        btn_write = (Button) findViewById(R.id.btn_rn_write);
        btn_load = (Button) findViewById(R.id.btn_rn_load);

      /*  Write_New_KODUINIV();   ///  Обработка таблицы контрагентов и создание нового UNIV кода
        Write_New_Table_RN();   ///  Запись новой таблицы и удаление пустых символов(пробелов)
        Write_New_Title_RN();   ///  запись шабки накладной
        Write_New_Name_RN       ///  запись тела накладной              */

        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XML_Array_KodRN();  /// создание массива кол-ва нокладных
                Loading_Dialog_Message();
                WJ_Karakil_newRN.MyAsyncTask_New_XML asyncTask = new WJ_Karakil_newRN.MyAsyncTask_New_XML();
                asyncTask.execute();
            }
        });

        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    //......... РАБОЧИЙ КОД ДЛЯ ПЕРЕНЕСЕНИЯ ДАННЫХ ИЗ ОДНОЙ БАЗЫ В ДРУГУЮ ЧЕРЕЗ XML файл...../////

    ///  Обработка таблицы контрагентов и создание нового UNIV кода
    protected void Write_New_KODUINIV() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(sql_name_db, MODE_PRIVATE, null);
        final String query = "SELECT * FROM table01_clients_1c_FULL;";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String new_string;
        ContentValues localContentValuesup = new ContentValues();
        while (cursor.isAfterLast() == false) {
            String client = (cursor.getString(cursor.getColumnIndex("client")));
            String client_adress = (cursor.getString(cursor.getColumnIndex("client_adress")));
            String univ_kod_1 = (cursor.getString(cursor.getColumnIndex("univ_kod_1")));
            String univ_kod_2 = (cursor.getString(cursor.getColumnIndex("univ_kod_2")));
            String univ_kod_3 = (cursor.getString(cursor.getColumnIndex("univ_kod_3")));
            String univ_new = (cursor.getString(cursor.getColumnIndex("univ_new")));
            String uid_clients = (cursor.getString(cursor.getColumnIndex("uid_clients")));
            new_string = univ_kod_1 + "/" + univ_kod_2 + "/" + univ_kod_3.replaceAll(univ_kod_3.substring(2, univ_kod_3.length() - 4), "11");
            localContentValuesup.put("univ_new", new_string);

            db.update(sql_table[0], localContentValuesup, "uid_clients = ?", new String[]{uid_clients});
            cursor.moveToNext();
            /*ДЛЯ НЕИЗВЕСТНОЙ ТОЧКИ
            NAME=магазин, торговая точка
            UID=8215446D-1257-4795-AC71-3250BAA86275
            */
        }
        Log.e(messeg_log, "таблица контрагентов обновлена");
        cursor.close();
        db.close();
    }

    ///  Запись новой таблицы и удаление пустых символов(пробелов)
    protected void Write_New_Table_RN() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(sql_name_db, MODE_PRIVATE, null);
        db.delete(sql_table[2], null, null);

        final String query = "SELECT * FROM table02_start_file;";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        ContentValues localContentValuesup = new ContentValues();
        Log.e("Кол-во строк:", "_" + cursor.getCount());
        BigDecimal f1 = new BigDecimal(0.0);
        BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(cursor.getCount()));

        while (cursor.isAfterLast() == false) {
            String name = (cursor.getString(cursor.getColumnIndex("name"))).replaceAll("\\s+", "");
            localContentValuesup.put("name", name.trim());
            db.insert(sql_table[2], null, localContentValuesup);
            cursor.moveToNext();
            f1 = f1.add(pointOne);
            pDialog.setProgress(f1.intValue());
        }
        Log.e("Конец", "Запись новой таблицы и удаление столбцов");
        cursor.close();
        db.close();
    }

    ///  запись шабки накладной
    protected void Write_New_Title_RN() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(sql_name_db, MODE_PRIVATE, null);
        db.delete(sql_table[4], null, null);
        final String query = "SELECT * FROM table03_start_files_new WHERE (name LIKE '%->>РасходнаяНакладная%') " +
                "OR (name LIKE '%-->Контрагент%') " +
                "OR (name LIKE '%-->СкладскладКаракол(Sunbell)K0000411Склады%') " +
                "OR (name LIKE '%-->ПроцентСкидки%');";
        final Cursor cursor = db.rawQuery(query, null);
        BigDecimal f1 = new BigDecimal(0.0);
        BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(cursor.getCount()));
        cursor.moveToFirst();
        ContentValues localContentValuesup = new ContentValues();
        while (cursor.isAfterLast() == false) {
            String name = (cursor.getString(cursor.getColumnIndex("name")));
            if (name.equals("-->СкладскладКаракол(Sunbell)K0000411Склады")) {
                String new_rn_kod, new_rn_data, new_client_name, new_client_kod, new_rn_skidka;
                cursor_id = cursor.getPosition();

                cursor.moveToPosition(cursor_id - 3);  // номер накладной и дата
                String kod_rn_AND_data = (cursor.getString(cursor.getColumnIndex("name")));
                new_rn_kod = kod_rn_AND_data.substring(21, kod_rn_AND_data.length() - 8);
                new_rn_data = kod_rn_AND_data.substring(31);
                String s_data, s_month, s_year;
                s_data = new_rn_data.substring(0, 2);
                s_month = new_rn_data.substring(3, 5);
                s_year = "20" + new_rn_data.substring(6, 8);
                new_rn_data = s_year + "-" + s_month + "-" + s_data;
                cursor.moveToPosition(cursor_id - 2);  // имя и код клиента
                String client_kod_AND_name = (cursor.getString(cursor.getColumnIndex("name")));
                new_client_name = client_kod_AND_name.substring(13, client_kod_AND_name.length() - 37);
                new_client_kod = client_kod_AND_name.substring(client_kod_AND_name.length() - 37, client_kod_AND_name.length() - 11);
                cursor.moveToPosition(cursor_id - 1);  // скидка накладной
                String skidka = (cursor.getString(cursor.getColumnIndex("name")));
                new_rn_skidka = skidka.substring(16, skidka.length() - 1);

                localContentValuesup.put("kod_rn", new_rn_kod);
                localContentValuesup.put("data", new_rn_data);
                localContentValuesup.put("vrema", "00:00:00");
                localContentValuesup.put("ag_name", "Керкин Роман Максимович");
                localContentValuesup.put("ag_uid", "A8BA1F48-C7E1-497B-B74A-D86426684712");
                localContentValuesup.put("client_name", new_client_name);
                localContentValuesup.put("client_kod", new_client_kod);
                localContentValuesup.put("credit", "Наличными");
                localContentValuesup.put("sklad_name", "склад Каракол (Sunbell)");
                localContentValuesup.put("sklad_uid", "9107FD60-C949-4672-9FA5-7B179164DFF3");
                localContentValuesup.put("cena_price", "Цена для агентов");
                localContentValuesup.put("coment", "Скидка " + new_rn_skidka + "% (нал. расчет);");
                localContentValuesup.put("cena_nds", "0");
                localContentValuesup.put("skidka", new_rn_skidka);
                localContentValuesup.put("day_kons", "0");

                db.insert(sql_table[4], null, localContentValuesup);
                cursor.moveToNext();
            }
            cursor.moveToNext();
            f1 = f1.add(pointOne);
            pDialog.setProgress(f1.intValue());
        }
        Log.e(messeg_log, "запись шабки накладной");
        cursor.close();
        db.close();
    }

    ///  запись товара, univ кода, количества и цены
    protected void Write_New_Name_RN() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(sql_name_db, MODE_PRIVATE, null);
        db.delete(sql_table[5], null, null);
        final String query = "SELECT * FROM table03_start_files_new WHERE (name LIKE '%->>РасходнаяНакладная%') " +
                "OR name LIKE '%-->СкладскладКаракол(Sunbell)K0000411Склады%' " +
                "OR (name LIKE '%-->Товар%') " +
                "OR (name LIKE '%-->Количество%') " +
                "OR (name LIKE '%-->ЦенаБС%')" +
                "OR (name LIKE '%-->СуммаБС%')" +
                "OR (name LIKE '%НСТ%')" +
                "OR (name LIKE '%КНД%')";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        BigDecimal f1 = new BigDecimal(0.0);
        BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(cursor.getCount()));
        ContentValues localContentValuesup = new ContentValues();
        while (cursor.isAfterLast() == false) {
            String name = (cursor.getString(cursor.getColumnIndex("name")));
            if (name.equals("-->СкладскладКаракол(Sunbell)K0000411Склады")) {

                cursor_id = cursor.getPosition();
                cursor.moveToPosition(cursor_id - 1);  // номер накладной и дата
                String kod_rn_AND_data = (cursor.getString(cursor.getColumnIndex("name")));
                new_rn_kod = kod_rn_AND_data.substring(21, kod_rn_AND_data.length() - 8);
                cursor.moveToPosition(cursor_id);  // имя и код товара
                String first_name = (cursor.getString(cursor.getColumnIndex("name")));
                //  Log.e("Код ", new_rn_kod);
                localContentValuesup.put("kod_rn_2", new_rn_kod);
                while (!first_name.equals("КНД")) {
                    cursor.moveToPosition(cursor_id);
                    cursor_id++;
                    first_name = (cursor.getString(cursor.getColumnIndex("name")));

                    if (first_name.equals("НСТ")) {
                        cursor.moveToPosition(cursor.getPosition() + 1);  // номер накладной и дата
                        String tovar = (cursor.getString(cursor.getColumnIndex("name")));
                        new_tovar = tovar.substring(8, tovar.length() - 38);
                        String kod_error = tovar.substring(tovar.length() - 38, tovar.length() - 12);

                        StringBuilder myName = new StringBuilder(kod_error);
                        myName.setCharAt(kod_error.length() - 6, '0');
                        myName.setCharAt(kod_error.length() - 5, '0');
                        new_tovar_kod = myName.toString();
                        //    Log.e("товар ", new_tovar);
                        //   Log.e("код ", new_tovar_kod);
                        cursor.moveToPosition(cursor.getPosition() + 1);  // количестов
                        String second_name = (cursor.getString(cursor.getColumnIndex("name")));
                        new_kol = second_name.substring(13, second_name.length() - 1);
                        //   Log.e("кол-во ", new_kol);
                        // Log.e("кол-во ", second_name + "L"+second_name.length());

                        cursor.moveToPosition(cursor.getPosition() + 1);  // цена прайс
                        String third_name = (cursor.getString(cursor.getColumnIndex("name")));
                        new_cena = third_name.substring(9, third_name.length() - 1);
                        //    Log.e("цена: ", new_cena);

                        cursor.moveToPosition(cursor.getPosition() + 1);  // цена прайс
                        String fourth_name = (cursor.getString(cursor.getColumnIndex("name")));
                        new_summa = fourth_name.substring(10, fourth_name.length() - 1);
                        //  Log.e("сумма: ", new_summa);

                        localContentValuesup.put("name", new_tovar);
                        localContentValuesup.put("kod_univ", new_tovar_kod);
                        localContentValuesup.put("kol", new_kol);
                        localContentValuesup.put("cena", new_cena);
                        localContentValuesup.put("summa", new_summa);
                        db.insert(sql_table[5], null, localContentValuesup);
                    }
                }
            }
            cursor.moveToNext();
            f1 = f1.add(pointOne);
            pDialog.setProgress(f1.intValue());
        }
        Log.e(messeg_log, "запись товара, univ кода, количества и цены");
        cursor.close();
        db.close();
    }

    /// обновлние данных в таблице и занесение имени товара и uid кода
    protected void Write_New_Name_RN_WRITE_UID() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(sql_name_db, MODE_PRIVATE, null);
        final String query = "SELECT table06_end_db_name.kod_univ, table06_end_db_name.kod_uid, " +
                "table04_nomeclature_1c.koduid, table04_nomeclature_1c.name FROM table06_end_db_name\n" +
                "LEFT JOIN table04_nomeclature_1c ON table06_end_db_name.kod_univ = table04_nomeclature_1c.kod_univ\n" +
                "WHERE table04_nomeclature_1c.kod_univ = table06_end_db_name.kod_univ;";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        BigDecimal f1 = new BigDecimal(0.0);
        BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(cursor.getCount()));
        ContentValues localContentValuesup = new ContentValues();
        while (cursor.isAfterLast() == false) {
            String kod_univ = (cursor.getString(cursor.getColumnIndex("kod_univ")));
            String koduid = (cursor.getString(cursor.getColumnIndex("koduid")));
            String name = (cursor.getString(cursor.getColumnIndex("name")));

            localContentValuesup.put("name", name);
            localContentValuesup.put("kod_uid", koduid);
            localContentValuesup.put("kod_univ", kod_univ);
            f1 = f1.add(pointOne);
          //  pDialog.setProgress(f1.intValue());

            db.update(sql_table[5], localContentValuesup, "kod_univ = ?", new String[]{kod_univ});
            cursor.moveToNext();
        }
        Log.e(messeg_log, "обновлние данных в таблице и занесение имени товара и uid кода");
        cursor.close();
        db.close();
    }

    /// обновлние uid в таблице с шабкой RN
    protected void UPDATE_Title_RN() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(sql_name_db, MODE_PRIVATE, null);
        final String query = "SELECT table05_end_db_title.kod_rn, table05_end_db_title.client_name, table05_end_db_title.client_kod, " +
                "table01_clients_1c_FULL.client, table01_clients_1c_FULL.uid_clients FROM table05_end_db_title\n" +
                "LEFT JOIN table01_clients_1c_FULL ON table05_end_db_title.client_kod = table01_clients_1c_FULL.univ_new;";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        BigDecimal f1 = new BigDecimal(0.0);
        BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(cursor.getCount()));
        ContentValues localContentValuesup = new ContentValues();
        while (cursor.isAfterLast() == false) {
            String client_name = (cursor.getString(cursor.getColumnIndex("client_name")));
            String client_uid = (cursor.getString(cursor.getColumnIndex("client_kod")));
            String client_name_new = (cursor.getString(cursor.getColumnIndex("client")));
            String client_uid_new = (cursor.getString(cursor.getColumnIndex("uid_clients")));
            String kod_rn = (cursor.getString(cursor.getColumnIndex("kod_rn")));

            localContentValuesup.put("client_name", client_name_new);
            localContentValuesup.put("client_kod", client_uid_new);
            localContentValuesup.put("kod_rn", kod_rn);
            db.update(sql_table[4], localContentValuesup, "kod_rn = ?", new String[]{kod_rn});

            cursor.moveToNext();
            f1 = f1.add(pointOne);
            pDialog.setProgress(f1.intValue());
        }
        Log.e(messeg_log, "обновлние uid в таблице с шабкой RN");
        cursor.close();
        db.close();
    }

    /// создание массива кол-ва нокладных
    protected void XML_Array_KodRN() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(sql_name_db, MODE_PRIVATE, null);
        String query = "SELECT DISTINCT kod_rn, data FROM table05_end_db_title";
        final Cursor cursor = db.rawQuery(query, null);
        mass_kod_rn = new String[cursor.getCount()];
        mass_kod_data = new String[cursor.getCount()];
        cursor.moveToFirst();
        if (cursor.getCount() > 0 & cursor != null)
            while (cursor.isAfterLast() == false) {
                String Kod_RN = cursor.getString(cursor.getColumnIndex("kod_rn"));
                String data = cursor.getString(cursor.getColumnIndex("data"));
                mass_kod_rn[cursor.getPosition()] = Kod_RN;
                mass_kod_data[cursor.getPosition()] = data;
                cursor.moveToNext();
            }
        Log.e("Конец", "создание массива");
        cursor.close();
        db.close();
    }

    protected void XML_Array(String mass_new_rn) {
        try {
            // Log.e(this.getLocalClassName(), kod_Rn + " " + S_name);
            serializer.startTag(null, "Array_Order");
            serializer.startTag(null, "SyncTableNomenclatura");

            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(sql_name_db, MODE_PRIVATE, null);

            // mass_new_rn = "ФеKR000219";
            String query_TITLE = "SELECT * FROM table05_end_db_title\n" +
                    "WHERE kod_rn = '" + mass_new_rn + "'";
            XML_Cursor = db.rawQuery(query_TITLE, null);
            XML_Cursor.moveToFirst();
            BigDecimal f1 = new BigDecimal(0.0);
            BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(XML_Cursor.getCount()));
            String sql_kod_rn = XML_Cursor.getString(XML_Cursor.getColumnIndex("kod_rn"));
            String sql_agent_name = XML_Cursor.getString(XML_Cursor.getColumnIndex("ag_name"));
            String sql_agent_uid = XML_Cursor.getString(XML_Cursor.getColumnIndex("ag_uid"));
            String sql_k_agn_uid = XML_Cursor.getString(XML_Cursor.getColumnIndex("client_kod"));
            String sql_k_agn_name = XML_Cursor.getString(XML_Cursor.getColumnIndex("client_name"));
            String sql_data = XML_Cursor.getString(XML_Cursor.getColumnIndex("data"));
            String sql_data_xml = XML_Cursor.getString(XML_Cursor.getColumnIndex("data"));
            String sql_credit = XML_Cursor.getString(XML_Cursor.getColumnIndex("credit"));
            String sql_sklad = XML_Cursor.getString(XML_Cursor.getColumnIndex("sklad_name"));
            String sql_sklad_uid = XML_Cursor.getString(XML_Cursor.getColumnIndex("sklad_uid"));
            String sql_cena_price = XML_Cursor.getString(XML_Cursor.getColumnIndex("cena_price"));
            String sql_coment = XML_Cursor.getString(XML_Cursor.getColumnIndex("coment"));
            String sql_uslov_nds = XML_Cursor.getString(XML_Cursor.getColumnIndex("cena_nds"));
            String sql_skidka_title = XML_Cursor.getString(XML_Cursor.getColumnIndex("skidka"));
            String sql_data_credite = XML_Cursor.getString(XML_Cursor.getColumnIndex("day_kons"));

            String sql_rn_data = XML_Cursor.getString(XML_Cursor.getColumnIndex("data"));
            String sql_rn_vrema = XML_Cursor.getString(XML_Cursor.getColumnIndex("vrema"));

            Log.e("String_XML ", sql_data + "_" + sql_data_xml);

            // Заполнение Формы
            serializer.startTag(null, "Nomer_Order");
            serializer.text(sql_kod_rn);
            serializer.endTag(null, "Nomer_Order");

            serializer.startTag(null, "Order_Date");
            serializer.text(sql_rn_data);
            serializer.endTag(null, "Order_Date");

            serializer.startTag(null, "Order_Vrema");
            serializer.text(sql_rn_vrema);
            serializer.endTag(null, "Order_Vrema");

            serializer.startTag(null, "NAME_AG");
            serializer.text(sql_agent_name);
            serializer.endTag(null, "NAME_AG");
            serializer.startTag(null, "UID_AG");
            serializer.text(sql_agent_uid);
            serializer.endTag(null, "UID_AG");
            serializer.startTag(null, "UID_C");
            serializer.text(sql_k_agn_uid);
            serializer.endTag(null, "UID_C");
            serializer.startTag(null, "NAME_C");
            serializer.text(sql_k_agn_name);
            serializer.endTag(null, "NAME_C");
            serializer.startTag(null, "DATA");
            serializer.text(sql_data);
            serializer.endTag(null, "DATA");
            serializer.startTag(null, "CREDIT");
            serializer.text(sql_credit);  // Консигнация, Перечислением, Взаимозачет
            serializer.endTag(null, "CREDIT");
            serializer.startTag(null, "SKLAD");
            serializer.text(sql_sklad);
            serializer.endTag(null, "SKLAD");
            serializer.startTag(null, "UID_SKLAD");
            serializer.text(sql_sklad_uid);
            serializer.endTag(null, "UID_SKLAD");
            serializer.startTag(null, "CENA_PRICE");
            serializer.text(sql_cena_price); // Цена брака, Цена в розницу, Цена для народных
            serializer.endTag(null, "CENA_PRICE");
            serializer.startTag(null, "Coment");
            serializer.text(sql_coment); // Дата отгрузки 04.12.2021; скидка 10%
            serializer.endTag(null, "Coment");
            serializer.startTag(null, "CENA_NDS");
            serializer.text(sql_uslov_nds); // Дата отгрузки 04.12.2021; скидка 10%
            serializer.endTag(null, "CENA_NDS");
            serializer.startTag(null, "SKIDKA");
            serializer.text(sql_skidka_title); // Скидка
            serializer.endTag(null, "SKIDKA");
            serializer.startTag(null, "DNEI_KONSIGN");
            serializer.text(sql_data_credite); // Дней конс
            serializer.endTag(null, "DNEI_KONSIGN");
            String query = "SELECT * FROM table06_end_db_name\n" +
                    "WHERE kod_rn_2 = '" + mass_new_rn + "'";
            XML_Cursor = db.rawQuery(query, null);
            XML_Cursor.moveToFirst();
            while (XML_Cursor.isAfterLast() == false) {

                String sql_pos_name = XML_Cursor.getString(XML_Cursor.getColumnIndex("name"));
                String sql_pos_kod_uid = XML_Cursor.getString(XML_Cursor.getColumnIndex("kod_uid"));
                String sql_pos_kod_univ = XML_Cursor.getString(XML_Cursor.getColumnIndex("kod_univ"));
                String sql_pos_kol = XML_Cursor.getString(XML_Cursor.getColumnIndex("kol"));
                String sql_pos_cena = XML_Cursor.getString(XML_Cursor.getColumnIndex("cena"));
                String sql_pos_summa = XML_Cursor.getString(XML_Cursor.getColumnIndex("summa"));

                serializer.startTag(null, "POS");
                serializer.startTag(null, "NAME");
                serializer.text(sql_pos_name);
                Log.e(this.getLocalClassName(), sql_pos_name);
                serializer.endTag(null, "NAME");
                serializer.startTag(null, "NAME_UID");
                serializer.text(sql_pos_kod_uid);
                serializer.endTag(null, "NAME_UID");
                serializer.startTag(null, "KODUNIV");
                serializer.text(sql_pos_kod_univ);
                serializer.endTag(null, "KODUNIV");
                serializer.startTag(null, "KOL_COUNT");
                serializer.text(sql_pos_kol);
                serializer.endTag(null, "KOL_COUNT");
                serializer.startTag(null, "PRICE");
                serializer.text(sql_pos_cena);
                serializer.endTag(null, "PRICE");
                serializer.startTag(null, "SUMMA");
                serializer.text(sql_pos_summa);
                serializer.endTag(null, "SUMMA");
                serializer.endTag(null, "POS");
                // Конец строки
                XML_Cursor.moveToNext();
                f1 = f1.add(pointOne);
                pDialog.setProgress(f1.intValue());
            }


            XML_Cursor.close();
            db.close();
            serializer.endTag(null, "SyncTableNomenclatura");
            serializer.endTag(null, "Array_Order");

            Log.e("Конец", "создание xml");
        } catch (Exception e) {
            Log.e("WJ_END:", "Ошибка: создание отчета!");
            Toast.makeText(context_Activity, "Ошибка: создание отчета!", Toast.LENGTH_SHORT).show();
        }

    }
    //
    protected void Loading_Dialog_Message() {
        pDialog = new ProgressDialog(context_Activity);
        pDialog.setMessage("Загрузка структуры...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setProgress(0);
        pDialog.setMax(100);
        pDialog.show();
    }

    // Синхронизация файлов для всех складов Юишкек
    private class MyAsyncTask_New_XML extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            pDialog.setMessage("Создание отчет по заказам. Подождите...");
            // pDialog.setMessage(mass[values[0]] + " \n" + values[0] + "/3 Подождите...");
            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
              /*  publishProgress(1);
                getFloor();  // Синхронизация*/
                for (int i = 0; i < 7; i++) {
                    switch (i) {
                        case 0: {
                            publishProgress(i);
                            getFloor_UP0(); // Синхронизация склады
                        }
                        break;
                        case 1: {
                            publishProgress(i);
                            getFloor_UP1(); // Синхронизация остатки
                        }
                        break;
                        case 2: {
                            publishProgress(i);
                            getFloor_UP2(); // Синхронизация цен
                        }
                        break;
                        case 3: {
                            publishProgress(i);
                            getFloor_UP3(); // Синхронизация склады
                        }
                        break;
                        case 4: {
                            publishProgress(i);
                            getFloor_UP4(); // Синхронизация склады
                        }
                        break;
                        case 5: {
                            publishProgress(i);
                            getFloor_UP5(); // Синхронизация склады
                        }
                        break;
                        case 6: {
                            publishProgress(i);
                            getFloor_UP6(); // Синхронизация склады
                        }
                        break;
                    }
                }
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
            Log.e("ПОТОК=", "Конец потока");
            pDialog.setProgress(0);
            pDialog.cancel();
            pDialog.dismiss();
        }

        private void getFloor_UP0() throws InterruptedException {
            Write_New_KODUINIV();
            try {

            } catch (Exception e) {
            }
            TimeUnit.SECONDS.sleep(1);
        }

        private void getFloor_UP1() throws InterruptedException {
            Write_New_Table_RN();
            try {

            } catch (Exception e) {
                Log.e("Ошибка1=", "ошибка потока!");
            }
            TimeUnit.SECONDS.sleep(1);

        }

        private void getFloor_UP2() throws InterruptedException {
            Write_New_Title_RN();
            try {

            } catch (Exception e) {
                Log.e("Ошибка2=", "ошибка потока!");
            }
            TimeUnit.SECONDS.sleep(1);
        }

        private void getFloor_UP3() throws InterruptedException {
            Write_New_Name_RN();
            try {

            } catch (Exception e) {
                Log.e("Ошибка3=", "ошибка потока!");
            }
            TimeUnit.SECONDS.sleep(1);

        }

        private void getFloor_UP4() throws InterruptedException {
            Write_New_Name_RN_WRITE_UID();
            try {

            } catch (Exception e) {
                Log.e("Ошибка4=", "ошибка потока!");
            }
            TimeUnit.SECONDS.sleep(1);

        }

        private void getFloor_UP5() throws InterruptedException {
            UPDATE_Title_RN();
            try {


            } catch (Exception e) {
            }
            TimeUnit.SECONDS.sleep(1);
        }

        private void getFloor_UP6() throws InterruptedException {
            try {

                SQLiteDatabase db = getBaseContext().openOrCreateDatabase(sql_name_db, MODE_PRIVATE, null);
                final String query = "SELECT DISTINCT data FROM table05_end_db_title";
                final Cursor cursor = db.rawQuery(query, null);
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    while (cursor.isAfterLast() == false) {
                        String data = (cursor.getString(cursor.getColumnIndex("data")));

                        String name_file_db = "MTW_out_order_rewrite_db";
                        String file_put_db = WJ_Karakil_newRN.this.getDatabasePath(name_file_db + ".xml").getAbsolutePath();  // путь к databases

                        String name_file_sd = "RN_Data_" + data + ".xml";
                        Log.e("DATA", data);

                        // String name_file_sd = "MTW_out_order_" + PEREM_AG_UID.substring(0, 6) + XML_Data_Start + ".xml";
                        XML_NEW_NAME = name_file_sd;
                        String file_db_sd = Environment.getExternalStorageDirectory().toString() + "/Price/XLS/" + name_file_sd;

                        Log.e("File_put ", file_put_db);
                        Log.e("File_put ", file_db_sd);

                        File newxmlfile_db = new File(file_put_db);
                        File newxmlfile_sd = new File(file_db_sd);

                        try {
                            newxmlfile_db.createNewFile();
                            newxmlfile_sd.createNewFile();
                        } catch (IOException e) {
                            Log.e("IOException", "Exception in create new File(");
                        }
                        FileOutputStream fileos = null;
                        FileOutputStream fileos2 = null;
                        try {
                            fileos = new FileOutputStream(newxmlfile_db);
                            fileos2 = new FileOutputStream(newxmlfile_sd);

                        } catch (FileNotFoundException e) {
                            Log.e("FileNotFoundException", e.toString());
                        }

                        serializer = Xml.newSerializer();
                        serializer.setOutput(fileos, "UTF-8");
                        serializer.startDocument(null, Boolean.valueOf(true));
                        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

                        serializer.startTag(null, "XML_Array");

                        for (int i = 0; i < mass_kod_rn.length; i++) {
                            if (data.equals(mass_kod_data[i])) {
                                XML_Array(mass_kod_rn[i]);
                            }

                        }
                        serializer.endTag(null, "XML_Array");

                        serializer.endDocument();

                        serializer.flush();
                        serializer.setOutput(fileos2, "UTF-8");
                        serializer.startDocument(null, Boolean.valueOf(true));
                        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

                        serializer.startTag(null, "XML_Array");

                        for (int i = 0; i < mass_kod_rn.length; i++) {
                            if (data.equals(mass_kod_data[i])) {
                                XML_Array(mass_kod_rn[i]);
                            }
                        }

                        serializer.endTag(null, "XML_Array");

                        serializer.endDocument();
                        serializer.flush();

                        fileos.close();
                        fileos2.close();

                        cursor.moveToNext();
                    }
                    Log.e("Конец", "Обновленна база");
                }
                cursor.close();
                db.close();
            } catch (Exception e) {
            }
            TimeUnit.SECONDS.sleep(1);
        }

    }


}
