package kg.roman.Mobile_Torgovla.MT_BackUp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kg.roman.Mobile_Torgovla.MT_FTP.FTPWebhost;
import kg.roman.Mobile_Torgovla.MT_FTP.FtpConnectData;
import kg.roman.Mobile_Torgovla.MailSenderClass;
import kg.roman.Mobile_Torgovla.R;

public class Async_ViewModel_BackUp_toFTP extends AndroidViewModel {

    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    private SharedPreferences mSettings;

    private String logeTAG = "ViewModel_BackUpFTP";

    public Async_ViewModel_BackUp_toFTP(@NonNull Application application) {
        super(application);
        // Context context = getApplication().getApplicationContext();
    }


    private MutableLiveData<String> messegeStatus;

    public LiveData<String> getStatus() {
        if (messegeStatus == null)
            messegeStatus = new MutableLiveData<>();
        return messegeStatus;
    }

    /////////////////////////////
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public LiveData<Boolean> getLoadingStatus() {
        if (isLoading == null)
            isLoading = new MutableLiveData<>(false);
        return isLoading;
    }


    public void execute() {
        isLoading.postValue(true);
        Log.e(logeTAG, "Boolean= TRUE");
        Runnable runnable = () -> {
            try {
                FTPWebhost ftpWebhost = new FTPWebhost();
                if (ftpWebhost.getInternetConnect(getApplication()) && ftpWebhost.getFTP_TestConnect().second) {
                     Messege_toMail();
                   // Thread.sleep(500);
                    messegeStatus.postValue("база данных успешно отправленна на сервер");
                } else if (ftpWebhost.getInternetConnect(getApplication()) == false) {
                    Log.e(logeTAG, "интернет не активен");
                    messegeStatus.postValue("нет доступа к интернету");
                } else {
                    Log.e(logeTAG, "ftp не активен");
                    messegeStatus.postValue("нет доступа к серверу");
                }
                Thread.sleep(50);
                messegeStatus = null;
                isLoading.postValue(false);
            } catch (Exception e) {
                Log.e(logeTAG, "Ошибка: не возможно подключиться к серверу, проверьте соединение к интренету и доступ к серверу");
                messegeStatus.postValue("Ошибка: не возможно подключиться к серверу, проверьте соединение к интренету и доступ к серверу");
            }
        };

        Log.e(logeTAG, "Boolean=FALSE");
        Thread thread = new Thread(runnable);
        thread.start();
    }


    protected void Messege_toMail() {
        try {
            String from, attach, text, title, where;
            BackUp_File_toFTP();
            FtpConnectData connectData = new FtpConnectData();
            title = "Агент: " + CreateInfotoAgent().first;
            text = CreateMessage_toBackUp();
            from = connectData.messege_mail_from; // отправитель
            where = connectData.messege_mail_where; // получатель
            attach = "";
            //  Настройки хоста
            MailSenderClass sender = new MailSenderClass(connectData.messege_mail_sender_user, connectData.messege_mail_sender_password, connectData.messege_mail_sender_port, connectData.messege_mail_sender_mailhost);   // Null
            sender.sendMail(title, text, from, where, attach);

/*            title = "Регистрация КПК: " + this_data_now;
            text = new_write;
            from = "sunbellagents@gmail.com";
            where = "kerkin911@gmail.com";
            attach = "";
            MailSenderClass sender = new MailSenderClass("sunbellagents@gmail.com", "fyczcoexpaspsham", 465, "smtp.gmail.com");    // Mail.ru*/
        } catch (Exception e) {
            Log.e(logeTAG, "Ошибка: данных");
        }

    }

    protected void Mail() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    ///// Создание тела для письма по отчету об резервном копирование
    protected String CreateMessage_toBackUp() {
        StringBuilder stringBuilder = new StringBuilder();
        PackageInfo pInfo = null;
        try {
            pInfo = getApplication().getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);

            stringBuilder.append("\n" + "Резервное копирование базы данных..... \n" + "Торговый агент: ")
                    .append(CreateInfotoAgent().first).append("\n")
                    .append("Маршрут: ")
                    .append(CreateInfotoAgent().second).append("\n");
            for (int i = 0; i < getApplication().getResources().getStringArray(R.array.mass_ver_android).length; i++)
                if (i == Build.VERSION.SDK_INT)
                    stringBuilder.append(String.format(Locale.getDefault(), "Версия Android-> %s, Версия SDK->(%d), %s", Build.VERSION.RELEASE, Build.VERSION.SDK_INT, getApplication().getResources().getStringArray(R.array.mass_ver_android)[i])).append("\n");
            stringBuilder.append("Версия приложения: ").append(pInfo.versionName).append(", ").append("версия кода:").append(pInfo.versionCode).append("\n")
                    .append("Дата отправки данных: ").append(CreateThisTime()).append("\n");

            for (String nameFiles : getApplication().getResources().getStringArray(R.array.mass_files_SQLITE_DB))
                stringBuilder.append("файл: ")
                        .append(CreateThisTime())
                        .append("_")
                        .append(nameFiles.substring(0, nameFiles.length() - 4))
                        .append(".db3 \n");
            //  Log.e("Шаблон: ", String.format(Locale.getDefault(), "Версия Android: %s (%d)", Build.VERSION.RELEASE, Build.VERSION.SDK_INT));
        } catch (Exception e) {
            // Log.e(this.getLocalClassName(), "Ошибка личных данных!");
        }


        return stringBuilder.toString();
    }

    //// Получение данных об агенте и его маршруте
    protected Pair<String, String> CreateInfotoAgent() {
        mSettings = getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String AgentName = mSettings.getString("PEREM_AG_NAME", "");    // получение имени агента
        String AgentRegion = mSettings.getString("PEREM_AG_REGION", "");    // получение регион(маршрут)
        return new Pair<>(AgentName, AgentRegion);
    }

    //// Получение текущей даты и времени
    protected String CreateThisTime() {
        // Текущее время
        Date currentDate = new Date();
        // Форматирование времени как "год.месяц.день"
        DateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        return dateText + " " + timeText;
    }

    /////// Отправка резервное коипирование файлов
    protected void BackUp_File_toFTP() {
        FTPWebhost webhost = new FTPWebhost();
        FtpConnectData connectData = new FtpConnectData();
       // String name_agents = CreateNameFile_BackUp(CreateInfotoAgent().first) + "_" + CreateNameFile_BackUp(CreateInfotoAgent().second);
     //   String name_agents = connectData.CreateNameFile_BackUp(CreateInfotoAgent().first);
        String name_agents = CreateNameFile_BackUp(CreateInfotoAgent().first);
        String thisTime = CreateThisTime();

        for (String st : connectData.mass_file_backup) {
            String pref = thisTime + "_" + st + "_" + name_agents+".db3";
            //File f = new File("/data/data/kg.roman.Mobile_Torgovla/databases/" + st);
            File f = new File(getApplication().getBaseContext().getDatabasePath(st+".db3").getPath());
            webhost.getFileToFTP(f.toString(), connectData.put_toFtpBackUp(getApplication()) + pref, true);
        }
    }

    // Конвертация из rus в eng
/*    public String CreateNameFile_BackUp(String stringName) {
        Log.e("CreateNameFile_BackUp", stringName);
        char[] chars_rus = {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ч', 'ц', 'ш', 'щ', 'э', 'ю', 'я', 'ы', 'ъ', 'ь'};
        String[] string_eng = {"a", "b", "v", "g", "d", "e", "io", "zh", "z", "i", "i", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ch", "c", "sh", "csh", "e", "ju", "ja", "i", "", ""};
        StringBuilder newName = new StringBuilder();
        StringBuilder newNameNumber = new StringBuilder();

        if (!stringName.isEmpty() | !stringName.equals(" ")) {
            String string1 = stringName.replaceFirst(" ", "_").replaceAll(" ", "");
            String stringWork = string1.toLowerCase().replaceAll("[^a-zа-я0-9]", "");
            for (Character agent : stringWork.toCharArray()) {
                for (int i = 0; i < chars_rus.length; i++)
                    if (chars_rus[i] == agent)
                        newName.append(string_eng[i]);
                if (Character.isDigit(agent))
                    newNameNumber.append(agent);
                if (agent.toString().matches("[a-z]"))
                    newName.append(agent);
            }
            newName.append(newNameNumber);
        } else {
            Log.e("CreateNameFile_BackUp", "не возможно создать строку, не верный формат данных");
            // Snackbar.make(binding.dbBaseRecyclerView, "не возможно создать строку, не верный формат данных", Snackbar.LENGTH_SHORT).show();
        }
        return newName.toString();
    }*/

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