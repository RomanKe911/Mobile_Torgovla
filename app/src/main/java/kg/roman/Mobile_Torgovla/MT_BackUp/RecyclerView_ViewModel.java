package kg.roman.Mobile_Torgovla.MT_BackUp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.apache.commons.net.ftp.FTPClient;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;

import kg.roman.Mobile_Torgovla.MT_FTP.FTPWebhost;
import kg.roman.Mobile_Torgovla.MT_FTP.FtpConnectData;

public class RecyclerView_ViewModel extends AndroidViewModel {


    public RecyclerView_ViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
    }

    private MutableLiveData<ArrayList<RecyclerView_Simple_BackUp>> backup_livedata;

    public LiveData<ArrayList<RecyclerView_Simple_BackUp>> getValues() {
        if (backup_livedata == null) {
            backup_livedata = new MutableLiveData<ArrayList<RecyclerView_Simple_BackUp>>();
        }
        return backup_livedata;
    }

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public LiveData<Boolean> getLoadingStatus() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<Boolean>(false);
        }
        return isLoading;
    }

    public void execute() {
        ArrayList<RecyclerView_Simple_BackUp> backup_list = new ArrayList<RecyclerView_Simple_BackUp>();
        Log.e("ViewModel: ", "Start");
        isLoading.postValue(true);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {

                    ArrayList<RecyclerView_Simple_BackUp> backup_list = new ArrayList<RecyclerView_Simple_BackUp>();
                    FTPWebhost webhost = new FTPWebhost();
                    FtpConnectData connectData = new FtpConnectData();
/*                    String nameAgent = "bezmenova_natalija_petrovna";
                    String putFile = "/MT_Sunbell_Karakol/MTW_SOS/";*/


                    String nameAgent = "saurova_nazira";
                    String putFile = "/MT_Sunbell_Bishkek/MTW_SOS/";

                    HashSet<String> hashSetFile = webhost.getListFile_BackUp(nameAgent, putFile);
                    HashSet<String> hashSetDataFile = new HashSet<>();
                    hashSetDataFile.stream().sorted();
                    for (String string_data : hashSetFile)
                        hashSetDataFile.add(string_data.substring(0, 10)+" "+string_data.substring(11, 19));

                    for (String dat : hashSetDataFile)
                    {
                        Log.e("File: ", "Data... "+dat);

                        String stringBaseDb = new String(), stringConstDb = new String(), stringRnDb = new String();
                        for (String ste : hashSetFile)
                        {

                            if (ste.contains(dat))
                            {
                                Log.e("File: ", "Put... "+putFile + ste);

                                Log.e("File: ", "File... "+ste);
                                Log.e("File: ", "__"+webhost.getFilesSize(putFile));
                                if (ste.contains("sunbell_base_db"))
                                    stringBaseDb = webhost.getFilesSize(putFile + ste);
                                if (ste.contains("sunbell_const_db"))
                                    stringConstDb = webhost.getFilesSize(putFile + ste);
                                if (ste.contains("sunbell_rn_db"))
                                    stringRnDb = webhost.getFilesSize(putFile + ste);
                            }

                        }
                        if (stringBaseDb.isEmpty()) stringBaseDb ="нет файла";
                        if (stringConstDb.isEmpty()) stringConstDb ="нет файла";
                        if (stringRnDb.isEmpty()) stringRnDb ="нет файла";
                        backup_list.add(new RecyclerView_Simple_BackUp(nameAgent, "2", stringRnDb, stringConstDb, stringBaseDb));
                        backup_livedata.postValue(backup_list);
                        Thread.sleep(100);
                    }





       /*             for(String state : hashSet) {
                        String stringBaseDb = new String(), stringConstDb = new String(), stringRnDb = new String();

                        String data = state.substring(0, 10);
                        if (state.contains(data))
                        {
                            for (String list :hashSet)
                            {
                                if (list.contains("sunbell_base_db"))
                                    stringBaseDb = webhost.getFilesSize(connectData.put_toFTPBackUp + list);
                                if (list.contains("sunbell_const_db"))
                                    stringConstDb = webhost.getFilesSize(connectData.put_toFTPBackUp + list);
                                if (list.contains("sunbell_rn_db"))
                                    stringRnDb = webhost.getFilesSize(connectData.put_toFTPBackUp + list);
                            }
                            backup_list.add(new RecyclerView_Simple_BackUp(nameAgent, "2", stringRnDb, stringConstDb, stringBaseDb));
                            backup_livedata.postValue(backup_list);
                            Thread.sleep(500);
                        }


                    }*/

/*                    int k = 5;
                    for (int i = 0; i < k; i++) {


                        Log.e("ViewModel: ", "loading... "+i);
                    }

                    Log.e("ViewModel: ", "END");*/

                    //   } catch (UnknownHostException es) {


                } catch (Exception es) {
                    es.printStackTrace();
                    Log.e("ERROR: ", es + "// Нет подключения к интернету");
                    backup_list.clear();
                    backup_livedata.postValue(backup_list);
                }

                isLoading.postValue(false);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
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

    /*SimpleDateFormat simpleDateFormat_display = new SimpleDateFormat("dd.MM.yyyy HH.mm");
    SimpleDateFormat simpleDateFormat_db = new SimpleDateFormat("yyyy-MM-dd HH.mm");
    //simpleDateFormat.format(dataFile.getTimestamp().getTime());
                        Log.e("Список файлов:", "File: " + ftpFile_XML.getName());
                        Log.e("Список файлов:", "Размер: "+getFilesSize(ftpConnectData.put_toFTPBackUp+ftpFile_XML.getName()));
                        Log.e("Список файлов:", "Дату: " + simpleDateFormat_db.format(ftpFile_XML.getTimestamp().getTime()));
                        Log.e("Список файлов:", "Дату2: " + getFullTime(ftpFile_XML.getTimestamp().getTimeInMillis()));*/


}