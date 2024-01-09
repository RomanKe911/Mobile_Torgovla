package kg.roman.Mobile_Torgovla.ImagePack;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import kg.roman.Mobile_Torgovla.MT_FTP.FtpConnectData;

public class MyViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> isStarted = new MutableLiveData<>(false);
    private MutableLiveData<Integer> value;
    SharedPreferences sp;

    public MyViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
    }

    public LiveData<Integer> getValue() {
        if (value == null) {
            value = new MutableLiveData<Integer>(0);
        }
        return value;
    }

    private MutableLiveData<ArrayList<ImagePack_R_Simple>> ftp_image_newRun;

    public LiveData<ArrayList<ImagePack_R_Simple>> getValues() {
        if (ftp_image_newRun == null) {
            ftp_image_newRun = new MutableLiveData<ArrayList<ImagePack_R_Simple>>();
        }
        return ftp_image_newRun;
    }

    public void execute() {

        if (!isStarted.getValue()) {
            isStarted.postValue(true);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    for (int i = value.getValue(); i <= 10; i++) {
                        try {
                            value.postValue(i);
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }


    public void execute2() {
        ArrayList<ImagePack_R_Simple> ftp_image_new = new ArrayList<ImagePack_R_Simple>();
        Log.e("StartTh1", "execute2: " + isStarted.getValue());
        Context context = getApplication().getApplicationContext();

        sp = PreferenceManager.getDefaultSharedPreferences(context);
        //   MyTimeZone();

        if (!isStarted.getValue()) {
            isStarted.postValue(true);
            Log.e("StartTh2", "execute2: " + isStarted.getValue());
            Runnable ftpRun = new Runnable() {
                @Override
                public void run() {
                    String w_brends = "", w_fileImage, w_dataUpdate = "";
                    Long w_fileSize = 0L;
                    int w_fileCount = 0;
                    int w_fileValue = 0;
                    FTPClient ftpClient = new FTPClient();
                    FtpConnectData connectData = new FtpConnectData();
                    try {
                        ftpClient.connect(connectData.server_name, connectData.port);
                        ftpClient.login(connectData.server_username, connectData.server_password);
                        ftpClient.enterLocalPassiveMode();
                        FTPFile[] fileList = ftpClient.listFiles(connectData.put_toFTPforRegions(context));
                        Log.e("PUT: ", "Путь: " + connectData.put_toFTPforRegions(context));


                        Log.e("ConTime", "Время: " + ftpClient.getConnectTimeout());
                        for (FTPFile file : fileList) {
                            Log.e("Files: ", "Кол-во папок> " + fileList.length);
                            if (file.isDirectory()) {
                                if (file.getName().length() > 3) {
                                    w_brends = file.getName();
                                    // w_dataUpdate = file.getTimestamp().getTime().toString();
                                    //   Log.e("TIME DATE", "3_" + file.getTimestamp().getTimeInMillis());
                                    long dateUpdate = file.getTimestamp().getTimeInMillis() + connectData.ftp_timezone;
                                    w_dataUpdate = getFullTime(file.getTimestamp().getTimeInMillis() + connectData.ftp_timezone);


                                    w_fileCount = 0;
                                    w_fileSize = 0L;
                                    FTPFile[] fileListBrends = ftpClient.listFiles(connectData.put_toFTPforRegions(context) + "/" + w_brends);
                                    for (FTPFile filesCount : fileListBrends) {
                                        if (filesCount.isFile()) {
                                            w_fileCount++;
                                            w_fileSize = w_fileSize + filesCount.getSize();

                                        }
                                    }
                                    w_fileImage = "logo_" + w_brends.toLowerCase().substring(3) + ".png";

                                    Log.e("Files: ", "Бренд> " + w_brends);
                                    Log.e("Files: ", "Дата обновления> " + w_dataUpdate);
                                    Log.e("Files: ", "Кол-во> " + w_fileCount);
                                    Log.e("Files: ", "Размер> " + getLongToDoubleFileSize(w_fileSize));
                                    Log.e("Files: ", "Картинка> " + w_fileImage);
                                    if (w_brends.equals("zz_Icons")) {
                                        w_brends = "zz_Icons";
                                    }
                                    Random rd = new Random(); // creating Random object
                                    Random click = new Random(); // creating Random object
                                    w_fileValue++;
                                    // value.postValue(w_fileValue);

                                    // value.getValue(w_fileValue);
                                    long dataSaveUpdate;


                                    String keyString = "SAVEDATA_" + w_brends.toLowerCase().trim();

                                    if (sp.contains(keyString)) {
                                        Log.e("PREF: ", "Есть настройки:" + keyString);
                                        dataSaveUpdate = sp.getLong("SAVEDATA_" + w_brends.toLowerCase().trim(), 0L);
                                        Log.e("PREF: ", "Дата" +getFullTime(dataSaveUpdate));
                                    } else
                                    {
                                        Log.e("PREF: ", "Нет настроек");
                                        dataSaveUpdate = 0L;
                                    }

                                    ftp_image_new.add(new ImagePack_R_Simple(
                                            w_brends,
                                            w_fileImage,
                                            getLongToDoubleFileSize(w_fileSize),
                                            w_fileCount,
                                            dataSaveUpdate,
                                            dateUpdate,
                                            click.nextBoolean()));

                                    ftp_image_newRun.postValue(ftp_image_new);
                                }

                            }
                        }
                    } catch (UnknownHostException es) {
                        es.printStackTrace();
                        Log.e("ERROR: ", es + "// Нет подключения к интернету");
                        ftp_image_new.clear();
                        ftp_image_newRun.postValue(ftp_image_new);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        Log.e("ERROR: ", "Ошибка" + ex);
                    } finally {
                        try {
                            if (ftpClient.isConnected()) {
                                Log.e("ConTime", "ВремяFinaly: " + ftpClient.getConnectTimeout());
                                ftpClient.logout();
                                ftpClient.disconnect();
                                isStarted.postValue(false);

                                Log.e("StartTh2", "execute2: " + isStarted.getValue());
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            Log.e("ERROR: ", "Ошибка закрытия FTP" + ex);
                        }
                    }
                }
            };
            Thread thread = new Thread(ftpRun);
            thread.start();
        }
    }

    ////////// функция работы с данными размера файлов
    private String getLongToDoubleFileSize(Long w_long) {
        int kByte = 1024;
        int mByte = 1048576;
        int gByte = 1073741824;
        String returnString = "";
        Double returnDouble = 0.0;
        if (w_long != 0) {
            if (w_long > gByte) {
                returnDouble = w_long.doubleValue() / gByte;
                returnString = new DecimalFormat("#0.00").format(returnDouble) + " Гбайт";
            } else if (w_long > mByte) {
                returnDouble = w_long.doubleValue() / mByte;
                returnString = new DecimalFormat("#0.00").format(returnDouble) + " Мбайт";
            } else {
                returnDouble = w_long.doubleValue() / kByte;
                returnString = new DecimalFormat("#0.00").format(returnDouble) + " Кбайт";
            }
            return returnString;
        } else return "нет файлов";


    }

    ////////// функция обработки даты и времени файла
    private String getFullTime(long timeInMillis) {
        final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        // final Calendar c = Calendar.getInstance();
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeInMillis);
        c.setTimeZone(TimeZone.getDefault());
        Log.e("TimeZone: ", format.format(c.getTime()));
        return format.format(c.getTime());
    }





    protected void MyTimeZone(long timeInMillis) {
        final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
        Date dateKg = new Date();
        dateKg.setTime(timeInMillis);
        String date_KG = format.format(dateKg);
        Log.e("TIMES: ", "MyDate" + date_KG);
    }



 /*   public HomeViewModel(Application application) {
        super(application);
        some_string = new MutableLiveData<>();
        Context context = getApplication().getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        some_string.setValue("<your value here>"));
    }*/

}