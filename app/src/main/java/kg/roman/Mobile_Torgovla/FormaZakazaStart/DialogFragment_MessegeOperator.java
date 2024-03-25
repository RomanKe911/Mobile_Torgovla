package kg.roman.Mobile_Torgovla.FormaZakazaStart;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import kg.roman.Mobile_Torgovla.MT_FTP.CalendarThis;
import kg.roman.Mobile_Torgovla.MT_FTP.PreferencesWrite;
import kg.roman.Mobile_Torgovla.R;

public class DialogFragment_MessegeOperator extends DialogFragment {

    private RemovableMessege remMessege, remProgressBar;
    private RemovableStatusZakaz remUpdate;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        remMessege = (RemovableMessege) context;
        remProgressBar = (RemovableMessege) context;

        remUpdate = (RemovableStatusZakaz) context;
    }

    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    String logeTAG = "DialogMessege";
    CalendarThis calendars = new CalendarThis();
    PreferencesWrite preferencesWrite;
    String messege, messegeArgument;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mSettings = getActivity().getApplication().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        preferencesWrite = new PreferencesWrite(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        messegeArgument = getArguments().getString("selectMessege");

        switch (messegeArgument) {
            case "AvtoSum": {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("дата: ").append(calendars.getThis_DateFormatDisplay).append("\n")
                        .append("-------------------------").append("\n")
                        .append("торговый агент").append("\n")
                        .append(preferencesWrite.Setting_AG_NAME).append("\n")
                        .append("-------------------------").append("\n")
                        .append("кол-во точек: ").append(CountTT()).append("\n")
                        .append("на сумму: ").append(SumTT());
                messege = stringBuilder.toString();
            }
            break;
            case "Zakaz":
                messege = "Вы хотите отправить заказы на обработку оператору?";
                break;
        }

        return builder
                .setTitle("Сообщение...")
                .setMessage(messege)
                .setIcon(R.drawable.office_title_forma_zakaz)
                //  .setView(localView)
                .setPositiveButton("OK", (dialog, which) ->
                {
                    switch (messegeArgument) {
                        case "AvtoSum":
                            Log.e(logeTAG, "кол-во точек: " + CountTT() + ", на сумму: " + SumTT() + "с");
                            break;
                        case "Zakaz": {
                            //// Проверка колв-ва точек
                            if (booleanCountTT()) {
                                //// Создание файла в дерикториях приложения и телефоне
                                CreateFile();
                                SendFTP();
                            } else {
                                ToastOverride("нет активных заказов, создайте новый заказ");
                                remProgressBar.RemovableProgressBar(false);
                            }

                            getActivity().getViewModelStore().clear();
                        }
                        break;
                    }

                })
                .setNegativeButton("Отмена", (dialog, which) ->
                {
                    //// Отмена действий и отмена ProgressBar
                    remProgressBar.RemovableProgressBar(false);
                    Log.e(logeTAG, "PregressBar: отмена");
                    getActivity().getViewModelStore().clear();
                })
                .create();
    }


    //////////////////////   02.2024 для Пункта Автосуммы
    ///// Получение кол-во обработанных точек за период
    protected int CountTT() {
        int countTT;
        SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
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
        SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
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

    //// Проверка кол-во активных точек
    protected boolean booleanCountTT() {
        return CountTT() != 0;
    }


    protected StringBuilder TextMessege() {
        PreferencesWrite preferencesWrite = new PreferencesWrite(getActivity().getBaseContext());
        SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String trade_itogo;
        int count_tt;
        Log.e(logeTAG, calendars.getThis_DateFormatSqlDB);

        String query_count = "SELECT DISTINCT k_agn_name FROM base_RN_All WHERE data = '" + calendars.getThis_DateFormatSqlDB + "'";
        final Cursor cursor_count = db.rawQuery(query_count, null);
        cursor_count.moveToFirst();
        if (cursor_count.getCount() > 0)
            count_tt = cursor_count.getCount();
        else count_tt = 0;
        cursor_count.close();

        String query_itogo = "SELECT SUM(summa) AS day_summa, SUM(itogo) AS day_itogo FROM base_RN_All\n" +
                "WHERE data = '" + calendars.getThis_DateFormatSqlDB + "'";
        final Cursor cursor_itogo = db.rawQuery(query_itogo, null);
        cursor_itogo.moveToFirst();
        if (cursor_itogo.getCount() > 0)
            trade_itogo = cursor_itogo.getString(cursor_itogo.getColumnIndexOrThrow("day_itogo"));
        else trade_itogo = "0.0";
        cursor_itogo.close();


        String query = "SELECT base_RN_All.data, base_RN_All.kod_rn, base_RN_All.k_agn_name, " +
                "base_RN_All.k_agn_uid, base_RN_All.k_agn_adress, base_RN_All.skidka, base_RN_All.summa, base_RN_All.itogo\n" +
                "FROM base_RN_All\n" +
                "WHERE base_RN_All.agent_uid = '" + preferencesWrite.Setting_AG_UID + "' AND base_RN_All.data LIKE '" + calendars.getThis_DateFormatSqlDB + "' " +
                "ORDER BY base_RN_All.k_agn_name";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("").append("Дата загрузки: ")
                .append(calendars.getThis_DateFormatDisplay)
                .append(" | ").append(calendars.getThis_DateFormatSqlDB)
                .append("\n").append("Количество точек: ").append(count_tt).append(", Общая сумма продаж: ").append(trade_itogo).append("\n");
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String kod_rn = cursor.getString(cursor.getColumnIndexOrThrow("kod_rn"));
            String klients = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_name"));
            String Adress = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_adress"));
            String Summa = cursor.getString(cursor.getColumnIndexOrThrow("summa"));
            String Itogo = cursor.getString(cursor.getColumnIndexOrThrow("itogo"));
            String Skidka = cursor.getString(cursor.getColumnIndexOrThrow("skidka"));
            stringBuilder.append("Номер накладной:").append(kod_rn).append("\n")
                    .append("Контрагент: ").append(klients).append("\n")
                    .append("Адрес: ").append(Adress).append("\n")
                    .append("Сумма: ").append(Summa).append("\n")
                    .append("Скидка: ").append(Skidka).append("% ").append("\n")
                    .append("Итого: ").append(Itogo).append("\n");
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return stringBuilder;
    }

    //// Всплывающие окно
    protected void ToastOverride(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    protected void SendFTP() {
        remUpdate.statusZakaz(false);
        Async_ViewModel_MailMessege model_mail = new ViewModelProvider(getActivity()).get(Async_ViewModel_MailMessege.class);
        model_mail.getLoadingStatus().observe(getActivity(), statusProgressBar ->
        {
            Log.e(logeTAG, "ProgressBarMail: " + statusProgressBar);
            if (statusProgressBar) {
                //// Отправка файла на сервер  и отключение ProgressBar
                Log.e(logeTAG, "Заказ успешно отправлен: " + statusProgressBar);
                remProgressBar.RemovableProgressBar(false);
                remUpdate.statusZakaz(true);
            } else remProgressBar.RemovableProgressBar(true);

        });
        model_mail.getMessegeStatus().observe(getActivity(), statusM ->
        {
            if (statusM.length() != 0) {
                Log.e(logeTAG, "Internet: " + statusM);
                remMessege.RemovableMessege(statusM);
                // ToastOverride(statusM);
            }
        });
        model_mail.execute();
    }

    protected void CreateFile() {

        Async_ViewModel_CreateFile modelCreateFile = new ViewModelProvider(getActivity()).get(Async_ViewModel_CreateFile.class);
        modelCreateFile.getLoadingStatus().observe(getActivity(), statusCreateFile ->
        {
            Log.e(logeTAG, "CreateFile: " + statusCreateFile);
            if (statusCreateFile) {
                Log.e(logeTAG, "Файл успешно создан ");

            }

        });
        modelCreateFile.getMessegeStatus().observe(getActivity(), messege ->
        {
            Log.e(logeTAG, "Messege: " + messege);
            if (messege.length() > 0) {
                remMessege.RemovableMessege(messege);
            }

        });
        modelCreateFile.execute();
    }
/*
    //// Проверка состояния файла на сервере
    protected void ExistFileFTP() {
        StringBuilder pathFileFTP = new StringBuilder();
        pathFileFTP.append(preferencesWrite.PEREM_FTP_PathData)
                .append(calendars.CalendarDayOfWeek().first)
                .append("/MT_out_order_")
                .append(preferencesWrite.Setting_NameFileToFtp)
                .append("_")
                .append(calendars.getThis_DateFormatSqlDB)
                .append(".xml");

        Async_ViewModel_UpdateStatusZakaz model_status = new ViewModelProvider(this).get(Async_ViewModel_UpdateStatusZakaz.class);
        model_status.putFIle = pathFileFTP.toString();
        model_status.getLoadingStatus().observe(this, statusUpdate ->
        {
            Log.e(logeTAG, "statusUpdate: " + statusUpdate);
            if (statusUpdate) {
                // UpdateListView();
             //   remUpdate.restartAdapter(true);
            }
        });

        model_status.getMessege().observe(this, messge ->
        {
            if (messge.length() > 0)
                ToastOverride(messge);
        });
        model_status.execute();
    }*/


}


//// Проверка подключения к интернету и отправка файла на сервер

/*
                // Update_Status(calendars.getThis_DateFormatDisplay);
                                // binding.FormaProgressBar.setVisibility(View.VISIBLE);
    private class sender_mail_async extends AsyncTask<Object, String, Boolean> {
        ProgressDialog WaitingDialog;

        @Override
        protected void onPreExecute() {
            WaitingDialog = ProgressDialog.show(getActivity(), "Выгрузка заказа", "Выполняется загрузка, подождите...", true);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            WaitingDialog.dismiss();
            WaitingDialog.cancel();
            //ToastOverride("Заказ успешно отправлен!!!");
            //Loading_FTP_Zakaz(XML_NEW_NAME, preferencesWrite.PEREM_FTP_PathData + PEREM_WORK_DISTR + "/" + XML_NEW_NAME);

            int k = 0;
            try {
                String pathFilePhone = getActivity().getApplication().getDatabasePath("MTW_out_order.xml").getAbsolutePath();
                //String pathFileFTP = preferencesWrite.PEREM_FTP_PathData + PEREM_WORK_DISTR + "/" + XML_NEW_NAME;
                String pathFileFTP = preferencesWrite.PEREM_FTP_PathData + preferencesWrite.Setting_DATA_WORK_DISTR + "/MTW_out_order_" + calendars.getThis_DateFormatSqlDB + ".xml";
                Log.e(logeTAG, "ФайлI: " + pathFilePhone);
                Log.e(logeTAG, "ПутьI: " + pathFileFTP);
                FTPWebhost ftpWebhost = new FTPWebhost();
                ftpWebhost.getFileToFTP(pathFilePhone, pathFileFTP, true);
                //((Activity)mainContext).finish();

                k++;
                Log.e(logeTAG, "ПутьK: " + k);
            } catch (Exception e) {
                // Toast.makeText(context_Activity, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
                Log.e(logeTAG, "FTP: ftpWebhost");
            }


        }

        @Override
        protected Boolean doInBackground(Object... params) {
            try {
                String data_mail_login = "", data_mail_pass = "", data_mail_where = "", data_mail_from = "";

                SQLiteDatabase db_sqlite = getActivity().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_CONST, MODE_PRIVATE, null);
                final String query = "SELECT * FROM const_mail";
                final Cursor cursor = db_sqlite.rawQuery(query, null);
                cursor.moveToFirst();

                while (cursor.isAfterLast() == false) {
                    String region = cursor.getString(cursor.getColumnIndexOrThrow("region"));
                    String mail_where = cursor.getString(cursor.getColumnIndexOrThrow("mail_where"));
                    String mail = cursor.getString(cursor.getColumnIndexOrThrow("operator_mail"));
                    String mail_pass = cursor.getString(cursor.getColumnIndexOrThrow("operator_mail_pass"));
                    Log.e("FTP: ", region + "__" + mail_where);
                    if (region.equals(preferencesWrite.PEREM_FTP_PathData)) {
                        data_mail_from = mail;
                        data_mail_where = mail_where;
                        data_mail_login = mail;
                        data_mail_pass = mail_pass;
                    }
                    cursor.moveToNext();
                }
                cursor.close();
                db_sqlite.close();

                title = "Агент: " + preferencesWrite.Setting_AG_NAME;
                text = messegeForInfoWorkDay().toString();
                from = data_mail_from;
                where = data_mail_where;
                attach = "";
                MailSenderClass sender = new MailSenderClass(data_mail_login, data_mail_pass, "465", "smtp.gmail.com");   // Null
                sender.sendMail(title, text, from, where, attach);
            } catch (Exception e) {
                //  Toast.makeText(getActivity(), "Данные не выгруженны", Toast.LENGTH_SHORT).show();
            }

            return false;
        }
    }*/
/*
    //// Отправка данных оператору
    protected void messegeUp() {
        model = new ViewModelProvider(getActivity()).get(Async_ViewModel_Zakaz_forFTP.class);
        model_mail = new ViewModelProvider(getActivity()).get(Async_ViewModel_MailMessege.class);


        model.getLoadingStatus().observe(getActivity(), status ->
        {

            Log.e(logeTAG, "status: " + status);
            if (status != true) {
                //  binding.FormaProgressBar.setVisibility(View.INVISIBLE);
                // Log.e(logeTAG, "status False ");
                ToastOverride("Отчет успешно создан!");
                Update_Status(calendars.getThis_DateFormatDisplay);
                onResume();
                Mail();
                // WJ_Forma_Zakaza.sender_mail_async async_sending = new WJ_Forma_Zakaza.sender_mail_async();
                DialogFragment_MessegeOperator.sender_mail_async async_sending = new sender_mail_async();
                async_sending.execute();

                String pathFilePhone = getActivity().getApplication().getDatabasePath("MTW_out_order.xml").getAbsolutePath();
                //String pathFileFTP = preferencesWrite.PEREM_FTP_PathData + PEREM_WORK_DISTR + "/" + XML_NEW_NAME;


                FtpConnectData ftpConnectData = new FtpConnectData();
                String pathFileFTP = preferencesWrite.PEREM_FTP_PathData
                        + preferencesWrite.Setting_DATA_WORK_DISTR
                        + "/MTW_out_order_" + ftpConnectData.CreateNameFile_BackUp(preferencesWrite.Setting_AG_NAME)
                        + calendars.getThis_DateFormatSqlDB + ".xml";
                Log.e(logeTAG, "ФайлI: " + pathFilePhone);
                Log.e(logeTAG, "ПутьI: " + pathFileFTP);
                FTPWebhost ftpWebhost = new FTPWebhost();
                ftpWebhost.getFileToFTP(pathFilePhone, pathFileFTP, true);

                model_mail.getMessegeStatus().observe(getActivity(), statusM ->
                {
                    Log.e(logeTAG, "MAIL: " + statusM);
                });
                model_mail.execute();


            } else {
                //  Log.e(logeTAG, "status True ");
                // binding.FormaProgressBar.setVisibility(View.VISIBLE);
            }
        });


        model.getMessegeStatus().observe(getActivity(), status ->
        {
            // Log.e(logeTAG, "XML_NEW_NAME "+list.toString());
            //  XML_NEW_NAME = status.toString();
        });

        model.execute();
    }*/
