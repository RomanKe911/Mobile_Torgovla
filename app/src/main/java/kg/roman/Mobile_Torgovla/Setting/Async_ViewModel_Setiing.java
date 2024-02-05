package kg.roman.Mobile_Torgovla.Setting;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import kg.roman.Mobile_Torgovla.MT_BackUp.RecyclerView_Simple_BackUp;
import kg.roman.Mobile_Torgovla.MT_FTP.FTPWebhost;
import kg.roman.Mobile_Torgovla.MT_FTP.FtpConnectData;
import kg.roman.Mobile_Torgovla.Permission.PrefActivity_Splash;

public class Async_ViewModel_Setiing extends AndroidViewModel {

    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    SharedPreferences mSettings;

    String logeTAG = "ViewModel_Setting";

    public Async_ViewModel_Setiing(@NonNull Application application) {
        super(application);
        // Context context = getApplication().getApplicationContext();
    }


    private MutableLiveData<ArrayList<RecyclerView_Simple_BackUp>> backup_livedata;

    public LiveData<ArrayList<RecyclerView_Simple_BackUp>> getValues() {
        if (backup_livedata == null)
            backup_livedata = new MutableLiveData<>();
        return backup_livedata;
    }

    /////////////////////////
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
        isLoading.postValue(true);
        Toast.makeText(getApplication(), "Подождите, идет скачивание файлов....", Toast.LENGTH_SHORT).show();
        mSettings = getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        Runnable runnable = () -> {
            try {
                FTPWebhost ftpWebhost = new FTPWebhost();
                ftpWebhost.getFileToPhone(mSettings.getString("setting_ftpPathData", "null") + "MTW_Data", "", getApplication(), false);
                ftpWebhost.getFileToPhone(mSettings.getString("setting_ftpPathData", "null") + "SqliteDB", "", getApplication(), false);
                Log.e(logeTAG, "execute: .........");
                Thread.sleep(50);
                // PrefActivity_Splash.this.getDatabasePath(ftpFile_XML.getName()).getAbsolutePath()
            } catch (Exception e) {

            }
            messegeStatus.postValue("END");
            isLoading.postValue(false);
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

}
