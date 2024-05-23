package kg.roman.Mobile_Torgovla.FormaZakazaStart;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import kg.roman.Mobile_Torgovla.MT_MyClassSetting.CalendarThis;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.FTPWebhost;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.FtpConnectData;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.PreferencesWrite;
import kg.roman.Mobile_Torgovla.MailSenderClass;

public class Async_ViewModel_MailMessege extends AndroidViewModel {

    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    String logeTAG = "ViewModel_Mail";
    public String from, attach, text, title, where, new_write;
    CalendarThis calendars = new CalendarThis();
    PreferencesWrite preferencesWrite = new PreferencesWrite(getApplication());

    public Async_ViewModel_MailMessege(@NonNull Application application) {
        super(application);
        // Context context = getApplication().getApplicationContext();
    }

    private MutableLiveData<String> messegeStatus = new MutableLiveData<>("");

    public LiveData<String> getMessegeStatus() {
        if (messegeStatus == null)
            messegeStatus = new MutableLiveData<>("");
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
                FTPWebhost ftpWebhost = new FTPWebhost();
                if (ftpWebhost.getInternetConnect(getApplication()) && ftpWebhost.getFTP_TestConnect().second) {
                    Messege_toMail();
                    // Thread.sleep(500);
                    messegeStatus.postValue("заказ успешно отправлен на сервер");
                } else if (ftpWebhost.getInternetConnect(getApplication()) == false) {
                    Log.e(logeTAG, "интернет не активен");
                    messegeStatus.postValue("нет доступа к интернету");
                } else {
                    Log.e(logeTAG, "ftp не активен");
                    messegeStatus.postValue("нет доступа к серверу");
                }
                Thread.sleep(500);
                fileGoToFTPServer();
                Thread.sleep(500);
                isLoading.postValue(true);
            } catch (Exception e) {
                Log.e(logeTAG, "Ошибка: не возможно подключиться к серверу, проверьте соединение к интренету и доступ к серверу");
                messegeStatus.postValue("Ошибка: не возможно подключиться к серверу, проверьте соединение к интернету и доступ к серверу");
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

/*        Runnable runnable = () -> {
            try {
                PreferencesWrite preferencesWrite = new PreferencesWrite(getApplication().getBaseContext());
                FtpConnectData ftpConnectData = new FtpConnectData();
                String textMessege = TextMessege().toString();
                if (preferencesWrite.Setting_DATA_MailMessege) {
                    SQLiteDatabase db_sqlite = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_CONST, MODE_PRIVATE, null);
                    final String query = "SELECT * FROM const_mail WHERE region == '" + preferencesWrite.PEREM_FTP_PathData + "'";
                    final Cursor cursor = db_sqlite.rawQuery(query, null);
                    cursor.moveToFirst();
                    if (cursor.getCount() > 0) {
                        String region = cursor.getString(cursor.getColumnIndexOrThrow("region"));  /// /MT_Sunbell_Bishkek/
                        String mail_where = cursor.getString(cursor.getColumnIndexOrThrow("mail_where"));  // bishkek@sunbell.webhost.kg
                        String mail = cursor.getString(cursor.getColumnIndexOrThrow("operator_mail"));  // sunbellagents@gmail.com
                        String mail_pass = cursor.getString(cursor.getColumnIndexOrThrow("operator_mail_pass"));  // fyczcoexpaspsham

                        title = "Агент: " + preferencesWrite.Setting_AG_NAME;
                        from = mail;
                        where = mail_where;
                        attach = "";
                        Log.e(logeTAG, TextMessege().toString());
                        MailSenderClass sender = new MailSenderClass(
                                mail,
                                mail_pass,
                                ftpConnectData.messege_mail_sender_port,
                                ftpConnectData.messege_mail_sender_mailhost);
                        sender.sendMail(title, textMessege, from, where, attach);

                    }
                    cursor.close();
                    db_sqlite.close();
                } else
                {
                    for (Map.Entry<String, String> list : ftpConnectData.getListMailMessege().entrySet())
                        if (list.getKey().equals(preferencesWrite.PEREM_FTP_PathData)) {
                            title = "Агент: " + preferencesWrite.Setting_AG_NAME;
                            MailSenderClass sender = new MailSenderClass(
                                    ftpConnectData.messege_mail_sender_user,
                                    ftpConnectData.server_password,
                                    ftpConnectData.messege_mail_sender_port,
                                    ftpConnectData.messege_mail_sender_mailhost);
                            sender.sendMail(title, textMessege, ftpConnectData.messege_mail_sender_user, list.getValue(), attach);
                        }

                    Log.e(logeTAG, TextMessege().toString());
                }
                messegeStatus.postValue(text.toString());
            } catch (Exception e) {
                messegeStatus.postValue("Данные не выгруженны");
            }

            isLoading.postValue(false);
        };
        Thread thread = new Thread(runnable);
        thread.start();*/
    }


    //////////////////////   02.2024 для Пункта Автосуммы
    ///// Получение кол-во обработанных точек за период
    protected int CountTT() {
        int countTT;
        SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String filter_dataStart = preferencesWrite.Setting_Filters_DataStart;
        String filters_dataEND = preferencesWrite.Setting_Filters_DataEND;
        String query_count;
        // if (filter_dataStart.equals("0") || filter_dataStart.equals(null))
        if (!preferencesWrite.Setting_FiltersSelectDate)
            if (!preferencesWrite.Setting_FiltersSelectClients)
                // Если, без фильтрации
                query_count = "SELECT DISTINCT k_agn_name FROM base_RN_All WHERE data = '" + calendars.getThis_DateFormatSqlDB + "';";
            else
                // Если выбран только клиент
                query_count = "SELECT DISTINCT k_agn_name, k_agn_uid FROM base_RN_All WHERE k_agn_uid = '" + preferencesWrite.Setting_Filters_Clients_UID + "' AND data = '" + calendars.getThis_DateFormatSqlDB + "';";
        else {
            if (!preferencesWrite.Setting_FiltersSelectClients)
                // Если, с датой но без контрагтом
                query_count = "SELECT DISTINCT k_agn_name FROM base_RN_All WHERE data BETWEEN '" + filter_dataStart + "' AND '" + filters_dataEND + "';";
            else
                // Если выбрана дата и клиент
                query_count = "SELECT DISTINCT k_agn_name, k_agn_uid FROM base_RN_All WHERE k_agn_uid = '" + preferencesWrite.Setting_Filters_Clients_UID + "' AND data BETWEEN '" + filter_dataStart + "' AND '" + filters_dataEND + "';";
        }

        Cursor cursor = db.rawQuery(query_count, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndexOrThrow("k_agn_name")) != null) {
            countTT = cursor.getCount();
        } else countTT = 0;
        cursor.close();
        db.close();
        return countTT;
    }

    //// Получение общей суммы точек за период
    protected double SumTT() {
        double sumItogo;
        SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String filter_dataStart = preferencesWrite.Setting_Filters_DataStart;
        String filters_dataEND = preferencesWrite.Setting_Filters_DataEND;
        String query;
        //  if (filter_dataStart.equals("0") || filter_dataStart.equals(null))

        if (!preferencesWrite.Setting_FiltersSelectDate) {
            if (!preferencesWrite.Setting_FiltersSelectClients)
                // Если, без фильтрации
                query = "SELECT SUM(itogo) AS day_itogo FROM base_RN_All WHERE data = '" + calendars.getThis_DateFormatSqlDB + "';";
            else
                // Если выбран только клиент
                query = "SELECT k_agn_name, k_agn_uid, SUM(itogo) AS day_itogo FROM base_RN_All WHERE k_agn_uid = '" + preferencesWrite.Setting_Filters_Clients_UID + "' AND data = '" + calendars.getThis_DateFormatSqlDB + "';";
        } else if (!preferencesWrite.Setting_FiltersSelectClients)
            // Если, с датой но без контрагтом
            query = "SELECT SUM(itogo) AS day_itogo FROM base_RN_All WHERE data BETWEEN '" + filter_dataStart + "' AND '" + filters_dataEND + "';";
        else
            // Если выбрана дата и клиент
            query = "SELECT k_agn_name, k_agn_uid, SUM(itogo) AS day_itogo FROM base_RN_All WHERE k_agn_uid = '" + preferencesWrite.Setting_Filters_Clients_UID + "' AND data BETWEEN '" + filter_dataStart + "' AND '" + filters_dataEND + "';";


        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndexOrThrow("day_itogo")) != null) {
            sumItogo = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("day_itogo")));
        } else sumItogo = 0.0;
        cursor.close();

        cursor.close();
        db.close();
        return sumItogo;
    }



    protected void Messege_toMail() {

        try {
            String from, attach, text, title, where;
            FtpConnectData connectData = new FtpConnectData();
            title = "Агент: " + preferencesWrite.Setting_AG_NAME;
            text = messegeForInfoWorkDay().toString();
            from = connectData.messege_mail_from; // отправитель
            where = connectData.messege_mail_where; // получатель
            attach = "";
            //  Настройки хоста
            MailSenderClass sender = new MailSenderClass(connectData.messege_mail_sender_user, connectData.messege_mail_sender_password, connectData.messege_mail_sender_port, connectData.messege_mail_sender_mailhost);   // Null
            sender.sendMail(title, text, from, where, attach);
        } catch (Exception e) {
            Log.e(logeTAG, "Ошибка: данных + Messege_toMail: " + e);
        }


    }
    protected void Mail() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    protected void fileGoToFTPServer() {
        try {
            preferencesWrite = new PreferencesWrite(getApplication().getBaseContext());
            //// Путь файла в телефоне
            String pathFilePhone = getApplication().getDatabasePath("MTW_out_order.xml").getAbsolutePath();
            //// Путь файла на сервере, имя файла(MT_out_order_kerkin_roman_2024-01-31.xml)
            StringBuilder pathFileFTP = new StringBuilder();
            pathFileFTP.append(preferencesWrite.PEREM_FTP_PathData)
                    .append(calendars.CalendarDayOfWeek().first)
                    .append("/MT_out_order_")
                    .append(CreateNameFile_BackUp(preferencesWrite.Setting_AG_NAME))
                    .append("_")
                    .append(calendars.getThis_DateFormatSqlDB)
                    .append(".xml");

            Log.e(logeTAG, "Путь файла в телефоне: " + pathFilePhone);
            Log.e(logeTAG, "Путь файла на сервере: " + pathFileFTP);
            FTPWebhost ftpWebhost = new FTPWebhost();
            ftpWebhost.getFileToFTP(pathFilePhone, pathFileFTP.toString(), true);
        } catch (Exception e) {
            Log.e(logeTAG, "FTP: ftpWebhost; "+e);
        }
    }




    //// Получение информации об загруженных точках
    protected StringBuilder messegeForInfoWorkDay() {
        StringBuilder stringBuilder = new StringBuilder();
        String query;
        SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        if (!preferencesWrite.Setting_FiltersSelectDate)
            if (!preferencesWrite.Setting_FiltersSelectClients)
                //// Поиск данных без фильтрации за рабочий день
                query = "SELECT * FROM base_RN_All\n" +
                        "WHERE base_RN_All.agent_uid = '" + preferencesWrite.Setting_AG_UID + "' AND base_RN_All.data = '" + calendars.getThis_DateFormatSqlDB + "';";
            else
                //// Поиск данных по клиенту за рабочий день
                query = "SELECT * FROM base_RN_All\n" +
                        "WHERE agent_uid = '" + preferencesWrite.Setting_AG_UID + "' AND k_agn_uid = '" + preferencesWrite.Setting_Filters_Clients_UID + "' AND data = '" + calendars.getThis_DateFormatSqlDB + "';";

        else if (!preferencesWrite.Setting_FiltersSelectClients)
            //// Поиск данных за выбранный период
            query = "SELECT * FROM base_RN_All\n" +
                    "WHERE agent_uid = '" + preferencesWrite.Setting_AG_UID + "' AND data BETWEEN '" + preferencesWrite.Setting_Filters_DataStart + "' AND '" + preferencesWrite.Setting_Filters_DataEND + "';";
        else
            query = "SELECT * FROM base_RN_All\n" +
                    "WHERE agent_uid = '" + preferencesWrite.Setting_AG_UID + "' AND k_agn_uid = '" + preferencesWrite.Setting_Filters_Clients_UID + "' AND data BETWEEN '" + preferencesWrite.Setting_Filters_DataStart + "' AND '" + preferencesWrite.Setting_Filters_DataEND + "';";

        stringBuilder.append("Дата загрузки: ").append(calendars.getThis_DateFormatDisplay).append(" | ").append(calendars.getThis_DateFormatSqlDB).append("\n");
        stringBuilder.append("Количество точек: ").append(CountTT()).append(", Общая сумма продаж: ").append(SumTT()).append("\n");

        Log.e(logeTAG, "query:" + query);
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Log.e(logeTAG, "List: список накладных");
            stringBuilder.append("\n");
            stringBuilder.append("Номер накладной: ").append(cursor.getString(cursor.getColumnIndexOrThrow("kod_rn"))).append("\n");
            stringBuilder.append("Контрагент: ").append(cursor.getString(cursor.getColumnIndexOrThrow("k_agn_name"))).append("\n");
            stringBuilder.append("Адрес: ").append(cursor.getString(cursor.getColumnIndexOrThrow("k_agn_adress"))).append("\n");
            stringBuilder.append("Cумма: ").append(cursor.getString(cursor.getColumnIndexOrThrow("summa"))).append("\n");
            stringBuilder.append("Скидка: ").append(cursor.getString(cursor.getColumnIndexOrThrow("skidka"))).append("\n");
            stringBuilder.append("Итого: ").append(cursor.getString(cursor.getColumnIndexOrThrow("itogo"))).append("\n");
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return stringBuilder;
    }

    public String CreateNameFile_BackUp(String stringName) {
        char[] chars_rus = {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ч', 'ц', 'ш', 'щ', 'э', 'ю', 'я', 'ы', 'ъ', 'ь'};
        String[] string_eng = {"a", "b", "v", "g", "d", "e", "io", "zh", "z", "i", "i", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ch", "c", "sh", "csh", "e", "ju", "ja", "i", "", ""};
        StringBuilder newName = new StringBuilder();
        StringBuilder newNameNumber = new StringBuilder();
        if (!stringName.isEmpty() || !stringName.equals(" ")) {
            String newAgentName = stringName.toLowerCase().replaceAll(" ", "_");
            for (Character agent : newAgentName.toCharArray()) {
                for (int i = 0; i < chars_rus.length; i++)
                    if (chars_rus[i] == agent)
                        newName.append(string_eng[i]);
                if (Character.isDigit(agent))
                    newNameNumber.append(agent);
                if (agent.toString().matches("[a-z]"))
                    newName.append(agent);
                if (agent == '_')
                    newName.append("_");
            }
            newName.append(newNameNumber);
        } else {
            Log.e("CreateNameFile_BackUp", "не возможно создать строку, не верный формат данных");
            // Snackbar.make(binding.dbBaseRecyclerView, "не возможно создать строку, не верный формат данных", Snackbar.LENGTH_SHORT).show();
            //String string1 = stringName.replaceFirst(" ", "_").replaceAll(" ", "");
            //  String stringWork = stringName.toLowerCase().replaceAll("[^a-zа-я0-9]", "");
        }
        mSettings = getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        editor = mSettings.edit();
        editor.putString("setting_NameFileToFTP", newName.toString());   // состояние фильтра по клиентам
        editor.commit();

        return newName.toString();
    }



}