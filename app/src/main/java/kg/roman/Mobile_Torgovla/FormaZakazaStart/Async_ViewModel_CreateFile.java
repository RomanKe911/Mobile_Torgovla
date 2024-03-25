package kg.roman.Mobile_Torgovla.FormaZakazaStart;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.util.Xml;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kg.roman.Mobile_Torgovla.MT_FTP.CalendarThis;
import kg.roman.Mobile_Torgovla.MT_FTP.PreferencesWrite;

public class Async_ViewModel_CreateFile extends AndroidViewModel {

    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    SharedPreferences mSettings;

    String logeTAG = "ViewModel_CreateFile";
    PreferencesWrite preferencesWrite;
    CalendarThis calendars = new CalendarThis();

    // XmlSerializer serializer = Xml.newSerializer();

    public Async_ViewModel_CreateFile(@NonNull Application application) {
        super(application);
        // Context context = getApplication().getApplicationContext();
    }


    /////////////////////////   Поток: создание файла с заказами для отправки на сервер
    private MutableLiveData<String> messegeStatus = new MutableLiveData<>("");

    public LiveData<String> getMessegeStatus() {
        if (messegeStatus == null)
            messegeStatus = new MutableLiveData<>(null);
        return messegeStatus;
    }
    ////////////////////////

    ////////////////////////
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public LiveData<Boolean> getLoadingStatus() {
        if (isLoading == null)
            isLoading = new MutableLiveData<>(false);
        return isLoading;
    }
    ////////////////////////

    public void execute() {
        isLoading.postValue(false);
        Runnable runnable = () -> {
            try {
                preferencesWrite = new PreferencesWrite(getApplication());
                String fileNameBase = newNameFile().first;
                String fileNameSD = newNameFile().second;
                Log.e(logeTAG, "Файл в приложении: " + fileNameBase);
                Create_xmlSerializer(fileNameBase);
                Log.e(logeTAG, "Файл в телефоне: " + fileNameSD);
                Create_xmlSerializer(fileNameSD);
                messegeStatus.postValue("файл с заказом успешно создан");
            } catch (Exception e) {
                Log.e(logeTAG, "Ошибка, serializer " + e);
                messegeStatus.postValue("Ошибка, файлы не могут быть созданы");
            }
            isLoading.postValue(true);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    //// Создание и перевод в латиницу имени агента в имя файла
    protected Pair<String, String> newNameFile() {

        String[] textRus = {"а", "б", "в", "г", "д", "е", "ё", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п", "р", "с", "т", "у", "ф", "х", "ч", "ц", "ш", "щ", "э", "ю", "я", "ы", "ъ", "ь"};
        String[] textEng = {"a", "b", "v", "g", "d", "e", "io", "zh", "z", "i", "i", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ch", "c", "sh", "csh", "e", "ju", "ja", "i", "", ""};

        StringBuilder stringBuilder = new StringBuilder();
        String name_old = preferencesWrite.Setting_AG_NAME.toLowerCase().replaceAll(" ", "_");

        for (int i = 0; i < name_old.length(); ++i) {
            String add = name_old.substring(i, i + 1);
            for (int j = 0; j < textRus.length; j++) {
                if (textRus[j].equals(add)) {
                    add = textEng[j];
                    break;
                }
            }
            stringBuilder.append(add);
        }

        //name_file_sd = ("MT_out_order_" + stringBuilder + "_" + calendars.getThis_DateFormatSqlDB + ".xml").replaceAll("[/:*?<>]", "");
        String putFileBase = getApplication().getDatabasePath("MTW_out_order.xml").getAbsolutePath();  // путь к databases
        String fileName = ("MT_out_order_" + stringBuilder + "_" + calendars.getThis_DateFormatSqlDB + ".xml").replaceAll("[/:*?<>]", "");
        String putFileSD = Environment.getExternalStorageDirectory() + "/Price/" + calendars.CalendarDayOfWeek().first + "/" + fileName;
        // Log.e(logeTAG, "nameFile: (Base:" + putFileBase + " SD: " + putFileSD);
        return new Pair<>(putFileBase, putFileSD);
    }

    protected void Create_xmlSerializer(String putFile) {
        File newXmlFile = new File(putFile);
        try {
            newXmlFile.createNewFile();
        } catch (IOException e) {
            Log.e(logeTAG, "IOException" + "Exception in create new File" + e);
        }

        FileOutputStream fileOutFile = null;
        try {
            fileOutFile = new FileOutputStream(putFile);
        } catch (FileNotFoundException e) {
            Log.e(logeTAG, "FileNotFoundException" + e);
        }


        try {
            XmlSerializer serializer = Xml.newSerializer();
            //// Создание файла на памяти приложения
            serializer.setOutput(fileOutFile, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

            serializer.startTag(null, "XML_Array");

            for (String mass_kod_rn : XML_Array_KodRN())
                XML_Array_Name(mass_kod_rn, serializer);

            //  Log.e(logeTAG, "FileNotFound " + mass_kod_rn + " " + XML_Data_Start + " " + XML_Data_End);
            serializer.endTag(null, "XML_Array");

            serializer.endDocument();
            serializer.flush();
            fileOutFile.close();
        } catch (Exception e) {
            Log.e(logeTAG, "Ошибка, serelise: " + e.toString());
        }

    }

    //// Создание списка с номером накладных
    private List<String> XML_Array_KodRN() {
        List<String> stringList = new ArrayList<>();
        SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String query;
        String qrUIDAgent = preferencesWrite.Setting_AG_UID,
                qrUIDClient = preferencesWrite.Setting_Filters_Clients_UID,
                qrDataThis = calendars.getThis_DateFormatSqlDB,
                qrDataStart = preferencesWrite.Setting_Filters_DataStart,
                qrDataEND = preferencesWrite.Setting_Filters_DataEND;
        if (!preferencesWrite.Setting_FiltersSelectDate)
            if (!preferencesWrite.Setting_FiltersSelectClients)
                //// без фильтров
                query = "SELECT kod_rn, k_agn_name, k_agn_uid, data, agent_name, agent_uid\n" +
                        "FROM base_RN_All\n" +
                        "WHERE agent_uid = '" + qrUIDAgent + "' AND data = '" + qrDataThis + "';";
            else
                //// Запрос с фильтром клиентом
                query = "SELECT kod_rn, k_agn_name, k_agn_uid, data, agent_name, agent_uid\n" +
                        "FROM base_RN_All\n" +
                        "WHERE agent_uid = '" + qrUIDAgent + "' AND k_agn_uid = '" + qrUIDClient + "' AND data = '" + qrDataThis + "';";
        else if (!preferencesWrite.Setting_FiltersSelectClients)
            //// Запрос с фильтрами даты
            query = "SELECT kod_rn, k_agn_name, k_agn_uid, data, agent_name, agent_uid\n" +
                    "FROM base_RN_All\n" +
                    "WHERE agent_uid = '" + qrUIDAgent + "'  AND data BETWEEN '" + qrDataStart + "' AND '" + qrDataEND + "';";
        else
            //// Запрос с всеми фильтрами
            query = "SELECT kod_rn, k_agn_name, k_agn_uid, data, agent_name, agent_uid\n" +
                    "FROM base_RN_All\n" +
                    "WHERE agent_uid = '" + qrUIDAgent + "' AND k_agn_uid = '" + qrUIDClient + "' AND data BETWEEN '" + qrDataStart + "' AND '" + qrDataEND + "';";
        Log.e(logeTAG, "XML_Array_KodRN_Query: " + query);


/*        query = "SELECT base_RN_All.kod_rn, base_RN_All.k_agn_name, " +
                "base_RN_All.k_agn_uid, base_RN_All.data, base_RN_All.agent_name, base_RN_All.agent_uid\n" +
                "FROM base_RN_All\n" +
                "WHERE base_RN_All.data BETWEEN '" + Data_Start + "' AND '" + Data_End + "' " +
                "AND base_RN_All.agent_uid = '" + w_agent_uid + "';";*/
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String Kod_RN = cursor.getString(cursor.getColumnIndexOrThrow("kod_rn"));
            stringList.add(Kod_RN);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return stringList;
    }

    protected void XML_Array_Name(String Array_kod_rn, XmlSerializer serializer) throws IOException {
        SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String query;
        if (!preferencesWrite.Setting_FiltersSelectDate) {
            query = "SELECT kod_rn, k_agn_name, k_agn_uid, data\n" +
                    "FROM base_RN_All\n" +
                    "WHERE kod_rn = '" + Array_kod_rn + "' AND data = '" + calendars.getThis_DateFormatSqlDB + "';";
        } else
            query = "SELECT kod_rn, k_agn_name, k_agn_uid, data\n" +
                    "FROM base_RN_All\n" +
                    "WHERE kod_rn = '" + Array_kod_rn + "' AND data BETWEEN '" + preferencesWrite.Setting_Filters_DataStart + "' AND '" + preferencesWrite.Setting_Filters_DataEND + "';";


        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String Kod_RN = cursor.getString(cursor.getColumnIndexOrThrow("kod_rn"));
            String Klients = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_name"));
            String UID_Klients = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_uid"));
            String Data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
            XML_Array(Kod_RN, Klients, UID_Klients, Data, serializer);
            // Log.e(logeTAG, "XML_Array_Name: " + Kod_RN + "_" + Klients + "_" + UID_Klients + "_" + Data);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    private void XML_Array(String kod_Rn, String S_name, String uid, String data, XmlSerializer serializer) throws IOException {

        // Log.e(this.getLocalClassName(), kod_Rn + " " + S_name);
        serializer.startTag(null, "Array_Order");
        serializer.startTag(null, "SyncTableNomenclatura");

        SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);

        String query_TITLE = "SELECT * FROM base_RN_All WHERE base_RN_All.kod_rn = '" + kod_Rn + "';";
        Cursor cursor = db.rawQuery(query_TITLE, null);
        cursor.moveToFirst();
        String sql_kod_rn = cursor.getString(cursor.getColumnIndexOrThrow("kod_rn"));
        String sql_agent_name = cursor.getString(cursor.getColumnIndexOrThrow("agent_name"));
        String sql_agent_uid = cursor.getString(cursor.getColumnIndexOrThrow("agent_uid"));
        String sql_k_agn_uid = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_uid"));
        String sql_k_agn_name = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_name"));
        String sql_data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
        String sql_data_xml = cursor.getString(cursor.getColumnIndexOrThrow("data_xml"));
        String sql_credit = cursor.getString(cursor.getColumnIndexOrThrow("credit"));
        String sql_sklad = cursor.getString(cursor.getColumnIndexOrThrow("sklad"));
        String sql_sklad_uid = cursor.getString(cursor.getColumnIndexOrThrow("sklad_uid"));
        String sql_cena_price = cursor.getString(cursor.getColumnIndexOrThrow("cena_price"));
        String sql_coment = cursor.getString(cursor.getColumnIndexOrThrow("coment"));
        String sql_uslov_nds = cursor.getString(cursor.getColumnIndexOrThrow("uslov_nds"));
        String sql_skidka_title = cursor.getString(cursor.getColumnIndexOrThrow("skidka_title"));
        String sql_data_credite = cursor.getString(cursor.getColumnIndexOrThrow("credite_date"));

        String sql_rn_data = cursor.getString(cursor.getColumnIndexOrThrow("data_xml"));
        String sql_rn_vrema = cursor.getString(cursor.getColumnIndexOrThrow("vrema"));
        //Log.e(logeTAG, "String_XML " + sql_data + "_" + sql_data_xml + "_ " + sql_kod_rn);

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
        cursor.close();

        String query = "SELECT base_RN_All.kod_rn, base_RN_All.k_agn_name, " +
                "base_RN_All.k_agn_uid, base_RN_All.data, base_RN.Name,  " +
                "base_RN.koduid, base_RN.Kod_Univ, base_RN.Kol, base_RN.Cena, base_RN.Summa, " +
                "base_RN.skidka, base_RN.itogo\n" +
                "FROM base_RN_All\n" +
                "JOIN base_RN ON base_RN_All.kod_rn=base_RN.Kod_RN\n" +
                "WHERE base_RN_All.kod_rn = '" + kod_Rn + "'";
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
            String kod_univ = cursor.getString(cursor.getColumnIndexOrThrow("Kod_Univ"));
            String kol = cursor.getString(cursor.getColumnIndexOrThrow("Kol"));
            String cena = cursor.getString(cursor.getColumnIndexOrThrow("Cena"));
            String summa = cursor.getString(cursor.getColumnIndexOrThrow("Summa"));
            String kod_uid = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));

            // Первая строка заказа
            serializer.startTag(null, "POS");
            serializer.startTag(null, "NAME");
            serializer.text(name);
            serializer.endTag(null, "NAME");
            serializer.startTag(null, "NAME_UID");
            serializer.text(kod_uid);
            serializer.endTag(null, "NAME_UID");
            serializer.startTag(null, "KODUNIV");
            serializer.text(kod_univ);
            serializer.endTag(null, "KODUNIV");
            serializer.startTag(null, "KOL_COUNT");
            serializer.text(kol);
            serializer.endTag(null, "KOL_COUNT");
            serializer.startTag(null, "PRICE");
            serializer.text(cena);
            serializer.endTag(null, "PRICE");
            serializer.startTag(null, "SUMMA");
            serializer.text(summa);
            serializer.endTag(null, "SUMMA");
            serializer.endTag(null, "POS");
            // Конец строки
            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        serializer.endTag(null, "SyncTableNomenclatura");
        serializer.endTag(null, "Array_Order");

        try {

        } catch (Exception e) {
            Log.e(logeTAG, "Ошибка: создание отчета!" + e);
        }

    }








    /*                    Log.e("FileSetting", "Data:" + mSettings.getString("PEREM_DIALOG_DATA_START", "0"));
                    Log.e("FileSetting", "DataStart:" + preferencesWrite.Setting_Filters_DataStart);
                    Log.e("FileSetting", "DataEnd:" + preferencesWrite.Setting_Filters_DataEND);*/
/*                    if (!preferencesWrite.Setting_FiltersSelectDate)
                    {

                    }

                    if (!mSettings.getString("PEREM_DIALOG_DATA_START", "0").isEmpty()) {
                        Log.e(logeTAG, "File " + "не пустойdd");
                        XML_Data_Start =  preferencesWrite.Setting_Filters_DataStart;
                        XML_Data_End = preferencesWrite.Setting_Filters_DataEND;
                        bool_filter = true;
                        XML_Array_KodRN(preferencesWrite.Setting_AG_UID, XML_Data_Start, XML_Data_End);

                        Log.e(logeTAG, "File1: " + preferencesWrite.Setting_AG_UID + "__" + XML_Data_Start + " " + XML_Data_End);

                    } else {
                        XML_Data_Start = calendars.getThis_DateFormatSqlDB;
                        XML_Data_End = calendars.getThis_DateFormatSqlDB;
                        bool_filter = false;
                        XML_Array_KodRN(preferencesWrite.Setting_AG_UID, XML_Data_Start, XML_Data_End);
                        // listRN.contains(XML_Array_KodRN(preferencesWrite.Setting_AG_UID, XML_Data_Start, XML_Data_End));
                        Log.e(logeTAG, "File2" + XML_Data_Start + " " + XML_Data_End);
                          // name_file_sd = ("MT_out_order_RomanKerkin_" + calendars.getThis_DateFormatSqlDB + ".xml").replaceAll("[/:*?<>]", "");
                    }*/


}