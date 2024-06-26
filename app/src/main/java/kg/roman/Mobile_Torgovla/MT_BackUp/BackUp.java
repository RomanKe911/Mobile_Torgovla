package kg.roman.Mobile_Torgovla.MT_BackUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kg.roman.Mobile_Torgovla.MT_MyClassSetting.FTPWebhost;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.FtpConnectData;
import kg.roman.Mobile_Torgovla.MailSenderClass;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.databinding.LayoutDbBackupBinding;


public class BackUp extends AppCompatActivity {
    private LayoutDbBackupBinding binding;
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    SharedPreferences mSettings;
    private Context context;
    public String from, attach, text, title, where;
    String logeTAG = "BackUp";
    Async_ViewModel model;
    Async_ViewModel_BackUp_toFTP model_backup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.layout_db_base_sos);
        binding = LayoutDbBackupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_actionbar_base_backup_circle);
        getSupportActionBar().setTitle("База данных");
        getSupportActionBar().setSubtitle("работа с базами данных");
        context = BackUp.this;

    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base_sos, menu);
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_base_backup: {
                model_backup = new ViewModelProvider(this).get(Async_ViewModel_BackUp_toFTP.class);
                model_backup.getStatus().observe(this, backup_status_connect ->
                {
                    Log.e(logeTAG, "STATUS, " + backup_status_connect);
                    SnackbarOverride(backup_status_connect);
                });
                model_backup.getLoadingStatus().observe(this, status ->
                {
                    if (status == true) {
                        binding.dbBaseProgressBar.setVisibility(View.VISIBLE);
                    } else binding.dbBaseProgressBar.setVisibility(View.INVISIBLE);
                });
                model_backup.execute();
            }
            break;
            case R.id.menu_item_base_null: {
                try {
                    model = new ViewModelProvider(this).get(Async_ViewModel.class);
                    model.getValues().observe(this, backup_list ->
                    {
                        RecyclerView_Adapter_ViewHolder_BackUp backup_adapter = new RecyclerView_Adapter_ViewHolder_BackUp(context, backup_list, null);
                        if (!backup_list.isEmpty()) {

                            binding.dbBaseRecyclerView.setAdapter(backup_adapter);
                            backup_adapter.notifyDataSetChanged();
                            // SnackbarOverride("данные готовы к работе");
                        } else {
                            SnackbarOverride("на сервере нет резервной копии!!!");
                            backup_list.clear();
                            binding.dbBaseRecyclerView.setAdapter(backup_adapter);
                            model.getMessegeStatus().observe(this, messegeStatus -> {
                                Log.e(logeTAG, "Ошибка, " + messegeStatus);
                                SnackbarOverride(messegeStatus);
                            });
                        }

                    });

                    model.getLoadingStatus().observe(this, statis ->
                    {
                        if (statis == true) {
                            binding.dbBaseProgressBar.setVisibility(View.VISIBLE);
                        } else binding.dbBaseProgressBar.setVisibility(View.INVISIBLE);
                    });
                    model.execute();
                } catch (Exception e) {
                    Log.e(logeTAG, "Ошибка, загрузки данных");
                    model.getMessegeStatus().observe(this, messegeStatus -> {
                        Log.e(logeTAG, "Ошибка, " + messegeStatus);
                        SnackbarOverride(messegeStatus);
                    });
                }
            }
            break;

        }
        return super.onOptionsItemSelected(item);
    }


    /// Создание новой строки для сохранения параметров  *******


    ///// Всплывающая строка состояния
    protected void SnackbarOverride(String text) {
        Snackbar.make(binding.dbBaseConstraintLayout, text, Snackbar.LENGTH_SHORT)
                .show();
    }

    //// Получение данных об агенте и его маршруте
    protected Pair<String, String> CreateInfotoAgent() {
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String AgentName = mSettings.getString("PEREM_AG_NAME", "");    // получение имени агента
        String AgentRegion = mSettings.getString("PEREM_AG_REGION", "");    // получение регион(маршрут)
        return new Pair<>(AgentName, AgentRegion);
    }

    //// Получение текущей даты и времени
    protected String CreateThisTime() {
        // Текущее время
        Date currentDate = new Date();
        // Форматирование времени как "год.месяц.день"
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        return dateText + " " + timeText;
    }

    //// Работа с потоком данных, отправка резервных файлов на сервер
    private class AsyncTask_toFTP_BackUp extends AsyncTask<Object, String, Void> {

        @Override
        protected void onPreExecute() {
            //  WaitingDialog = ProgressDialog.show(context_Activity, "Резервное копирование", "Выполняется загрузка, подождите...", true);
            SnackbarOverride("Резервное копирование базы данных");
            Log.e(logeTAG, "onPreExecute: Start");
            // binding.dbBaseProgressBar.setProgress(View.VISIBLE);
            binding.dbBaseProgressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Object... params) {
            try {
                FTPWebhost ftpWebhost = new FTPWebhost();
                if (ftpWebhost.getInternetConnect(context)) {
                    Log.e(logeTAG, "интернет активен");
                } else Log.e(logeTAG, "интернет не активен");


                if (ftpWebhost.getFTP_TestConnect().second) {
                    Log.e(logeTAG, "ftp активен");
                } else Log.e(logeTAG, "ftp не активен");
                // отправка данных на сервер
                    BackUp_File_toFTP();
                   FtpConnectData connectData = new FtpConnectData();
                    title = "Агент: " + CreateInfotoAgent().first;
                    text = CreateMessage_toBackUp();
                     from = connectData.messege_mail_from; // отправитель
                    where = connectData.messege_mail_where; // получатель
                    attach = "";
                    //  Настройки хоста
                    MailSenderClass sender = new MailSenderClass(connectData.messege_mail_sender_user, connectData.server_password, connectData.messege_mail_sender_port, connectData.messege_mail_sender_mailhost);   // Null
                    sender.sendMail(title, text, from, where, attach);
                   /* from = "sunbellagents@gmail.com"; // отправитель
                    where = "kerkin911@gmail.com"; // получатель
                    attach = "";
                    //  Настройки хоста
                    MailSenderClass sender = new MailSenderClass("sunbellagents@gmail.com", "fyczcoexpaspsham", "465", "smtp.gmail.com");   // Null
                    sender.sendMail(title, text, from, where, attach);*/
            } catch (Exception e) {
                Log.e(logeTAG, "Данные не выгруженны");
                Toast.makeText(context, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // WaitingDialog.dismiss();
            //Toast.makeText(context_Activity, "Заказ успешно отправлен!!!", Toast.LENGTH_LONG).show();
            //((Activity)mainContext).finish();
            SnackbarOverride("Данные успешно отправленны");
            Log.e(logeTAG, "onPreExecute: END");
            binding.dbBaseProgressBar.setVisibility(View.INVISIBLE);
        }

    }

    /////// Отправка резервное коипирование файлов
    protected void BackUp_File_toFTP() {
        FTPWebhost webhost = new FTPWebhost();
        FtpConnectData connectData = new FtpConnectData();
        String name_agents = CreateNameFile_BackUp(CreateInfotoAgent().first) + "_" + CreateNameFile_BackUp(CreateInfotoAgent().second);

        for (String st : connectData.mass_file_backup) {
            String pref = CreateThisTime() + "_" + name_agents + "_" + st;
            //File f = new File("/data/data/kg.roman.Mobile_Torgovla/databases/" + st);
            File f = new File(getBaseContext().getDatabasePath(st).getPath());
            webhost.getFileToFTP(f.toString(), connectData.put_toFtpBackUp(context) + pref, true);
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
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

            stringBuilder.append("\n" + "Резервное копирование базы данных..... \n" + "Торговый агент: ")
                    .append(CreateInfotoAgent().first).append("\n")
                    .append("Маршрут: ")
                    .append(CreateInfotoAgent().second).append("\n");
            for (int i = 0; i < getResources().getStringArray(R.array.mass_ver_android).length; i++)
                if (i == Build.VERSION.SDK_INT)
                    stringBuilder.append(String.format(Locale.getDefault(), "Версия Android-> %s, Версия SDK->(%d), %s", Build.VERSION.RELEASE, Build.VERSION.SDK_INT, getResources().getStringArray(R.array.mass_ver_android)[i])).append("\n");
            stringBuilder.append("Версия приложения: ").append(pInfo.versionName).append(", ").append("версия кода:").append(pInfo.versionCode).append("\n")
                    .append("Дата отправки данных: ").append(CreateThisTime()).append("\n");

            for (String nameFiles : getResources().getStringArray(R.array.mass_files_SQLITE_DB))
                stringBuilder.append("файл: ")
                        .append(CreateThisTime())
                        .append("_")
                        .append(nameFiles.substring(0, nameFiles.length() - 4))
                        .append(".db3 \n");
            //  Log.e("Шаблон: ", String.format(Locale.getDefault(), "Версия Android: %s (%d)", Build.VERSION.RELEASE, Build.VERSION.SDK_INT));
        } catch (Exception e) {
            Log.e(this.getLocalClassName(), "Ошибка личных данных!");
        }


        return stringBuilder.toString();
    }

    public String CreateNameFile_BackUp(String stringName) {
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
            // Log.e("CreateNameFile_BackUp", "не возможно создать строку, не верный формат данных");
            Snackbar.make(binding.dbBaseRecyclerView, "не возможно создать строку, не верный формат данных", Snackbar.LENGTH_SHORT).show();
        }
        return newName.toString();
    }

}