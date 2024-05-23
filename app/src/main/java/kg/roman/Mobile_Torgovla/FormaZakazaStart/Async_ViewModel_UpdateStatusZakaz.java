package kg.roman.Mobile_Torgovla.FormaZakazaStart;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.apache.commons.net.ftp.FTPClient;

import java.io.InputStream;

import kg.roman.Mobile_Torgovla.MT_MyClassSetting.CalendarThis;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.FtpConnectData;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.PreferencesWrite;

public class Async_ViewModel_UpdateStatusZakaz extends AndroidViewModel {
    String logeTAG = "ViewModel_UpdateStatus";
    PreferencesWrite preferencesWrite = new PreferencesWrite(getApplication());
    CalendarThis calendars = new CalendarThis();
    String putFIle = "";

    public Async_ViewModel_UpdateStatusZakaz(@NonNull Application application) {
        super(application);
        // Context context = getApplication().getApplicationContext();
    }

    ////////////////////////
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public LiveData<Boolean> getLoadingStatus() {
        if (isLoading == null)
            isLoading = new MutableLiveData<>(false);
        return isLoading;
    }
    ////////////////////////
    private MutableLiveData<String> messegeStatus = new MutableLiveData<>("");
    public LiveData<String> getMessege() {
        if (messegeStatus == null)
            messegeStatus = new MutableLiveData<>(null);
        return messegeStatus;
    }

    public void execute() {
        isLoading.postValue(false);
        FTPClient ftpClient = new FTPClient();
        FtpConnectData connectData = new FtpConnectData();

        Runnable runnable = () -> {
            try {
                preferencesWrite = new PreferencesWrite(getApplication());
                ftpClient.connect(connectData.server_name, connectData.port);
                ftpClient.login(connectData.server_username, connectData.server_password);
                ftpClient.enterLocalPassiveMode();
                InputStream inputStream = ftpClient.retrieveFileStream(putFIle);
                Log.e(logeTAG, "execute: " + putFIle);
                int returnCode = ftpClient.getReplyCode();
                if (inputStream != null || returnCode != 550) {
                    Log.e("FTPWEB", "файл существует");
                    messegeStatus.postValue("файл существует");
                    sqlUpdateStatusZakaz();
                } else {
                    Log.e("FTPWEB", "файл не существует");
                    messegeStatus.postValue("файл не существует");
                }

                isLoading.postValue(true);
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (Exception es) {
                Log.e(logeTAG, "Error: " + es);
            } /*finally {
                try {
                    if (ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }*/
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    // Обновление статуса выгрузки данных
    protected void sqlUpdateStatusZakaz() {
        try {

            String qrUIDAgent = preferencesWrite.Setting_AG_UID,
                    qrUIDClient = preferencesWrite.Setting_Filters_Clients_UID,
                    qrDataThis = calendars.getThis_DateFormatSqlDB,
                    qrDataStart = preferencesWrite.Setting_Filters_DataStart,
                    qrDataEND = preferencesWrite.Setting_Filters_DataEND;
            String query;
            SQLiteDatabase db = getApplication().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
            if (!preferencesWrite.Setting_FiltersSelectDate)
                if (!preferencesWrite.Setting_FiltersSelectClients)
                    query = "UPDATE base_RN_All SET status = 'true'\n" +
                            "WHERE data = '" + qrDataThis + "';";
                else query = "UPDATE base_RN_All SET status = 'true'\n" +
                        "WHERE k_agn_uid = '" + qrUIDClient + "' AND data = '" + qrDataThis + "';";
            else if (!preferencesWrite.Setting_FiltersSelectClients)
                query = "UPDATE base_RN_All SET status = 'true'\n" +
                        "WHERE data BETWEEN '" + qrDataStart + "' AND '" + qrDataEND + "';";
            else query = "UPDATE base_RN_All SET status = 'true'\n" +
                        "WHERE k_agn_uid = '" + qrUIDClient + "' AND data BETWEEN '" + qrDataStart + "' AND '" + qrDataEND + "';";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
                cursor.moveToNext();
            cursor.close();
            db.close();
            Log.e(logeTAG, "Статус заказа изменен");
        } catch (Exception e) {
            Log.e("Удаление!", "Ошибка удаления данных! " + e);
        }
    }
}