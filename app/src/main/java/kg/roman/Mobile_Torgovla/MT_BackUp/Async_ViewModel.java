package kg.roman.Mobile_Torgovla.MT_BackUp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import kg.roman.Mobile_Torgovla.MT_MyClassSetting.FTPWebhost;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.FtpConnectData;

public class Async_ViewModel extends AndroidViewModel {

    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    SharedPreferences mSettings;

    String logeTAG = "ViewModel_BackUp";

    public Async_ViewModel(@NonNull Application application) {
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
        ArrayList<RecyclerView_Simple_BackUp> backup_list = new ArrayList<>();
        isLoading.postValue(true);
        Runnable runnable = () -> {
            try {
                FTPWebhost webhost = new FTPWebhost();
                FtpConnectData connectData = new FtpConnectData();

                //  String nameAgent = connectData.CreateNameFile_BackUp("Дерновая Алла");
                //   String putFile = "/MT_Sunbell_Bishkek/MTW_SOS/";
                //  String putFile = "/MT_Sunbell_Bishkek/MTW_SOS/";


                //     String nameAgent = connectData.CreateNameFile_BackUp(CreateInfotoAgent().first);
                String nameAgent = CreateNameFile_BackUp(CreateInfotoAgent().first);
                String putFile = connectData.put_toFtpBackUp(getApplication());

                ArrayList<String> arrayList_NameFile = webhost.getListFile_BackUp(nameAgent, putFile);
                Log.e(logeTAG, "Агент: " + nameAgent + " Путь: " + putFile);

                ArrayList<String> stringBaseDb = new ArrayList<>();
                ArrayList<String> stringConstDb = new ArrayList<>();
                ArrayList<String> stringRnDb = new ArrayList<>();

                for (String fileName : arrayList_NameFile) {
                    if (fileName.contains("sunbell_base_db"))
                        stringBaseDb.add(fileName);
                    if (fileName.contains("sunbell_const_db"))
                        stringConstDb.add(fileName);
                    if (fileName.contains("sunbell_rn_db"))
                        stringRnDb.add(fileName);
                }

                try {
                    if (stringRnDb.size() == 0) {
                        Log.e(logeTAG, "для агента: " + nameAgent + ", нет резервной базы данных");
                        messegeStatus.postValue("для агента: " + nameAgent + ", нет резервной базы данных");
                        backup_list.clear();
                        backup_livedata.postValue(backup_list);
                    }
                    if (stringRnDb.size() < connectData.ftp_backup_sizeFile) {
                        for (int i = 0; i < stringRnDb.size(); i++) {
                            String stringFileData = CreateFileBackUp(stringRnDb, "sunbell_rn_db").get(i) + nameAgent + ".db3";
                            backup_list.add(new RecyclerView_Simple_BackUp(
                                    nameAgent,
                                    CreateInfotoAgent().second,
                                    stringFileData.substring(0, stringFileData.indexOf("_")),
                                    webhost.getFilesSize(putFile + CreateFileBackUp(stringRnDb, "sunbell_rn_db").get(i) + nameAgent + ".db3"),
                                    webhost.getFilesSize(putFile + CreateFileBackUp(stringConstDb, "sunbell_const_db").get(i) + nameAgent + ".db3"),
                                    webhost.getFilesSize(putFile + CreateFileBackUp(stringBaseDb, "sunbell_base_db").get(i) + nameAgent + ".db3")));
                            backup_livedata.postValue(backup_list);
                            Log.e(logeTAG, "FILE: " + putFile + CreateFileBackUp(stringRnDb, nameAgent).get(i) + "sunbell_rn_db.db3");
                        }
                    } else
                        for (int i = 0; i < connectData.ftp_backup_sizeFile; i++) {
                            String stringFileData = CreateFileBackUp(stringRnDb, "sunbell_rn_db").get(i) + nameAgent + ".db3";
                            backup_list.add(new RecyclerView_Simple_BackUp(
                                    nameAgent,
                                    CreateInfotoAgent().second,
                                    stringFileData.substring(0, stringFileData.indexOf("_")),
                                    webhost.getFilesSize(putFile + CreateFileBackUp(stringRnDb, "sunbell_rn_db").get(i) + nameAgent + ".db3"),
                                    webhost.getFilesSize(putFile + CreateFileBackUp(stringConstDb, "sunbell_const_db").get(i) + nameAgent + ".db3"),
                                    webhost.getFilesSize(putFile + CreateFileBackUp(stringBaseDb, "sunbell_base_db").get(i) + nameAgent + ".db3")));
                            backup_livedata.postValue(backup_list);
                        }

                } catch (Exception e) {
                    Log.e(logeTAG, "Ошибка создания списка базы данных!");
                    messegeStatus.postValue("Ошибка создания списка базы данных!");
                }
                Thread.sleep(50);
            } catch (Exception es) {
                es.printStackTrace();
                Log.e(logeTAG, "Ошибка, не возможно отобразить список данных");
                messegeStatus.postValue("Ошибка, не возможно отобразить список данных");
                backup_list.clear();
                backup_livedata.postValue(backup_list);
            }

            isLoading.postValue(false);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    ////////////////  Создание списка для отображения в таблице
    protected ArrayList<String> CreateFileBackUp(ArrayList<String> stringBase, String fileName) {
        FtpConnectData connectData = new FtpConnectData();
        HashSet<Long> hashSet = new HashSet<>();
        ArrayList<Long> arrayList = new ArrayList<>();
        ArrayList<String> returnListSorted = new ArrayList<>();

        for (String strings : stringBase) {
            String firstString = strings.substring(0, strings.indexOf("_"));
            Long firstLong = connectData.getFullTimeStringToLong(firstString);
            hashSet.add(firstLong);
        }
        if (hashSet.size() < connectData.ftp_backup_sizeFile) {
            for (int i = 0; i < hashSet.size(); i++)
                arrayList.add((Long) hashSet.toArray()[i]);
        } else {
            for (int i = 0; i < connectData.ftp_backup_sizeFile; i++) {
                Long fileRemove = hashSet.stream().max(Long::compareTo).get();
                arrayList.add(fileRemove);
                hashSet.remove(fileRemove);
            }
        }
        hashSet.clear();
        arrayList.sort(Collections.reverseOrder());
        for (Long list : arrayList)
            returnListSorted.add(connectData.getFullTimeLongToStringFormatBackup(list) + "_" + fileName + "_");
        return returnListSorted;
    }

    // Получения данных о пользователе: Имя агента, и регион(маршрут)
    protected Pair<String, String> CreateInfotoAgent() {
        mSettings = getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String AgentName = mSettings.getString("PEREM_AG_NAME", "");    // получение имени агента
        String AgentRegion = mSettings.getString("PEREM_AG_REGION", "");    // получение регион(маршрут)
        return new Pair<>(AgentName, AgentRegion);

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
        return newName.toString();
    }
}
// 12:01:2024 09:35:54_sunbell_const_db_muhamedzhanova_elena_arslanovna.db3
// 01:06:2023 13:36:25_sunbell_const_db_muhamedzhanova_elena_arslanovna.db3