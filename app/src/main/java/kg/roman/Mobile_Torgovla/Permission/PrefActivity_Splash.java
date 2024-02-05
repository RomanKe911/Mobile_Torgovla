package kg.roman.Mobile_Torgovla.Permission;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Calendar;

import kg.roman.Mobile_Torgovla.FTP.Sunbell_FtpAction;
import kg.roman.Mobile_Torgovla.FTP.Sunbell_FtpAsyncTask_Users;
import kg.roman.Mobile_Torgovla.FTP.Sunbell_FtpConnection;
import kg.roman.Mobile_Torgovla.MailSenderClass;
import kg.roman.Mobile_Torgovla.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;


/**
 * Created by Roman on 06.03.2017.
 */
public class PrefActivity_Splash extends PreferenceActivity {

    public Calendar localCalendar = Calendar.getInstance();
    public Integer thisdata, thismonth, thisyear, thisminyte, thishour, thissecond;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now;
    public Context context_Activity;
    public Cursor cursor;
    public Preference button_const, button_users;
    public SharedPreferences.Editor ed;
    public SharedPreferences sp;
    public ProgressDialog pDialog;
    public OutputStream outputStream;
    public InputStream inputStreamut;
    public FTPFile ftpFile;
    public Dialog dialog;
    public Preference info_1, info_2, info_3, info_4, info_5, info_6, info_7, phone, preference_up_db, button_id_catigory, preferenceScreen, button_write_new_group;
    public String version, Android_id, files_kol, files_size, files_kol_sql, files_size_sql;
    public String[] mass_MTW, mass_SQL, mass_brends_name, mass_brends_pref;
    public ListPreference listPreference, listPreference_ty, listPreference_ftp_con;
    public MultiSelectListPreference MULTIlistPreference_brends;
    public CheckBoxPreference checkBoxPreference_brends;
    public View activity_view;
    public Integer number_update = 0;


    public String mail_login, mail_pass, mail_start, mail_end;  // переменные для подключения к почте
    public String from, new_write, attach, text, title, where;  // переменные для работы с почтой
    public String ftp_con_serv, ftp_con_login, ftp_con_pass;   // переменные для работы с сервером
    public String PEREM_FTP_SERV, PEREM_FTP_LOGIN, PEREM_FTP_PASS, PEREM_MAIL_LOGIN, PEREM_PUT_REGION,
            PEREM_MAIL_PASS, PEREM_MAIL_START, PEREM_MAIL_END, PEREM_FTP_DISTR_XML, PEREM_FTP_DISTR_db3;
    public String DATA_DIALOG_SPLASH_1, DATA_DIALOG_SPLASH_2, put_distr_xml, put_distr_db3, put_distr_sos;
    public String PEREM_DB3_CONST, PEREM_DB3_BASE, PEREM_DB3_RN;
    public String PEREM_number_update_new, PEREM_number_update_old;
    public String query_new_rows_baseRN_debet_new, query_new_rows_baseRN_status;
    private static final int PERMISSION_STORAGE = 101;
    public SwitchPreference switchPreference_permission;
    public String TAG = "";
    public PreferenceCategory preferenceCategory_FTP, preferenceCategory_TY, preferenceCategory_Nomenclatura, preferenceCategory_All, preferenceCategory_About;

    public SwitchPreference switch_all_brends, switch_for_agent, switch_HandSelected;
    public SwitchPreference switch_OldVersion, switch_Files, switch_SDCard;



    /*public EditTextPreference edit_ip, edit_login, edit_pass;
    edit_ip = (EditTextPreference) findPreference("edit_ip");
    edit_login = (EditTextPreference) findPreference("edit_login");
    edit_pass = (EditTextPreference) findPreference("edit_pass");*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_splash);


        context_Activity = PrefActivity_Splash.this;
        TAG = this.getLocalClassName();
        Constanta_Read();

        Permission_Loading_Start();


        preferenceScreen = (Preference) findPreference("screen");
        switchPreference_permission = (SwitchPreference) findPreference("switch_preference_data");

        Permission_Server();
        Permission_Data_RW();
        Permission_Nomeclatura();
     //   ListPreference_Brends_Adapter();   /////21/01/2024
        Permission_TY();
        Permission_putImgeToPhone();


        preference_up_db = findPreference("button_up_db");
        this.activity_view = this.findViewById(R.id.setting_connect_ftp);


        button_id_catigory = (Preference) findPreference("button_id_catigory");
        button_write_new_group = (Preference) findPreference("button_write_new_group");
        button_id_catigory.setSummary(sp.getString("Data_dialog_sp_2", "0"));
        button_id_catigory.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Calendate_New();
                Constanta_Read();
                pDialog = new ProgressDialog(context_Activity);
                pDialog.setMessage("Загрузка продуктов...");
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setProgress(0);
                pDialog.setMax(100);
                pDialog.show();

                Log.e("Put:", PEREM_FTP_DISTR_XML);
                Log.e("Put:", PEREM_FTP_DISTR_db3);

                PrefActivity_Splash.MyAsyncTask_Sync_FTP_CONST asyncTask = new PrefActivity_Splash.MyAsyncTask_Sync_FTP_CONST();
                asyncTask.execute();

                return false;
            }
        });


        Android_id = Settings.Secure.getString(context_Activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        mass_MTW = getResources().getStringArray(R.array.mass_files_MTW);
        mass_SQL = getResources().getStringArray(R.array.mass_files_SQLITE_DB);


        button_write_new_group.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Write_List_Group_Brends();
                return false;
            }
        });

        info_1 = (Preference) findPreference("version");
        info_2 = (Preference) findPreference("v_kod");
        info_3 = (Preference) findPreference("min_version");
        info_5 = (Preference) findPreference("you_version");
        info_4 = (Preference) findPreference("kodirovka");
        info_6 = (Preference) findPreference("programmer");
        info_7 = (Preference) findPreference("kod_act");
        phone = (Preference) findPreference("phone");
        PackInfo();

        preference_up_db.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                PackageInfo pInfo = null;
                try {
                    pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    Log.e("New.", "Код: " + pInfo.versionCode);
                    Constanta_Read();
                    number_update = Integer.parseInt(PEREM_number_update_old);

                    Log.e("New.", String.valueOf(pInfo.versionCode));
                    Log.e("Old.", String.valueOf(number_update));

                    if (pInfo.versionCode != number_update) {
                        //  if (7 != number_update) {
                        Log.e("New.", "Новыя версия");

                        pDialog = new ProgressDialog(context_Activity);
                        pDialog.setMessage("Добавление новых таблиц...");
                        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        pDialog.setProgress(0);
                        pDialog.setMax(100);
                        pDialog.show();  // Обновление таблиц в базе
                        PrefActivity_Splash.MyAsyncTask_Sync_Update_Create_Table asyncTask = new PrefActivity_Splash.MyAsyncTask_Sync_Update_Create_Table();
                        asyncTask.execute();

                        PEREM_number_update_old = String.valueOf(pInfo.versionCode);
                        ed = sp.edit();
                        ed.putString("PEREM_number_update_old", PEREM_number_update_old);    //запись данных имя пароль
                        ed.commit();
                    } else {
                        Log.e("Old.", "Запись уже есть");
                        Toast.makeText(context_Activity, "Новые таблицы и столбцы уже созданы!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }
                return false;
            }
        });

        button_users = (Preference) findPreference("button_users");
        button_users.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                FTP_Connect_Users("MTW_In_Users.xml", "/MT_Sunbell_Karakol/MTW_Data/");
                return false;
            }
        });





      /*  if (!checkBoxPreference_brends.isChecked()) {
            MULTIlistPreference_brends.setEnabled(false);
        }*/
        //  ListPreference_Brends_Adapter();


    }


    protected void FTP_Connect_Users(String file_name, String file_put) {
        Log.e("UP", file_put);
        Log.e("UP", file_name);
        Sunbell_FtpConnection c = new Sunbell_FtpConnection();
        c.server = PEREM_FTP_SERV;
        c.port = 21;
        c.username = PEREM_FTP_LOGIN;
        c.password = PEREM_FTP_PASS;

        Sunbell_FtpAction a = new Sunbell_FtpAction();
        a.success = true;
        a.connection = c;
        a.fileName = file_name;
        a.remoteFilename = file_put + "/" + file_name;
        new Sunbell_FtpAsyncTask_Users(context_Activity).execute(a);
    }


    protected void PackInfo() {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            info_1.setSummary(pInfo.versionName);
            info_2.setSummary("Код: " + pInfo.versionCode + ", Ревиз: " + pInfo.baseRevisionCode);
            info_3.setSummary("API 21: Android 5.0 (Lollipop)");
            info_4.setSummary("Бренд: " + Build.BRAND + "\nМарка: " + Build.MODEL);
            info_6.setSummary("Kerkin Roman");

            Android_id = Settings.Secure.getString(context_Activity.getContentResolver(), Settings.Secure.ANDROID_ID);

            if (Android_id.length() >= 14) {
                Android_id = Android_id.substring(0, 14);
                Log.e("Kod_Reg|", Android_id + "|, Кол-во симв: " + +Android_id.length());  //
            } else Log.e("Kod_Reg|", Android_id + "|, Кол-во симв: " + +Android_id.length());  //

            info_7.setSummary("/" + Android_id + "/, Кол-во симв: " + Android_id.length());

            switch (android.os.Build.VERSION.SDK_INT) {
                case Build.VERSION_CODES.LOLLIPOP:
                    info_5.setSummary("Версия Android 5.0 Lollipop(SDK 21)");
                    break;
                case Build.VERSION_CODES.LOLLIPOP_MR1:
                    info_5.setSummary("Версия Android 5.1 Lollipop(SDK 22)");
                    break;
                case Build.VERSION_CODES.M:
                    info_5.setSummary("Версия Android 6.0 Marshmallow(SDK 23)");
                    break;
                case Build.VERSION_CODES.N:
                    info_5.setSummary("Версия Android 7.0 Nougat(SDK 24)");
                    break;
                case Build.VERSION_CODES.N_MR1:
                    info_5.setSummary("Версия Android 7.1 Nougat(SDK 25)");
                    break;
                case Build.VERSION_CODES.O:
                    info_5.setSummary("Android 8.0 Oreo (SDK 26)");
                    break;
                case Build.VERSION_CODES.O_MR1:
                    info_5.setSummary("Android 8.1 Oreo (SDK 27)");
                    break;
                case Build.VERSION_CODES.P:
                    info_5.setSummary("Версия Android 9 Pie(SDK 28)");
                    break;
                case Build.VERSION_CODES.Q:
                    info_5.setSummary("Версия Android 10 (SDK 29)");
                    break;
            }

            phone.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.getSummary() + ""));
                    startActivity(intent);
                    return false;
                }
            });


            Log.e(this.getLocalClassName(), "Info личных данных");
        } catch (Exception e) {
            Log.e(this.getLocalClassName(), "Ошибка личных данных!");
        }


    }

    protected void Calendate_New() {


        DateFormat df_data = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df_vrema = new SimpleDateFormat("HH:mm:ss");
        DateFormat df_year = new SimpleDateFormat("yyyy");
        DateFormat df_month = new SimpleDateFormat("MM");
        DateFormat df_day = new SimpleDateFormat("dd");

        this_rn_data = df_data.format(Calendar.getInstance().getTime());
        this_rn_vrema = df_vrema.format(Calendar.getInstance().getTime());
        this_rn_year = df_year.format(Calendar.getInstance().getTime());
        this_rn_month = df_month.format(Calendar.getInstance().getTime());
        this_rn_day = df_day.format(Calendar.getInstance().getTime());

        // this_data_now = this_rn_day + "-" + this_rn_month + "-" + this_rn_year;  // Формат для отображения

        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(this_rn_year), Integer.valueOf(this_rn_month) - 1, Integer.valueOf(this_rn_day));
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd:MM:yyyy");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateString_NOW = dateFormat1.format(calendar.getTime());
        String dateString_WORK = dateFormat2.format(calendar.getTime());
        this_data_now = dateString_NOW + " " + this_rn_vrema;
        Log.e("WJ_FormaL2:", "!DataStart:" + dateString_NOW);
        //  Log.e("WJ_FormaL2:", "!DataEnd:" + dateString2);

    } // Загрузка даты и время

    // Скачиаваник стартовых файлов
    private class MyAsyncTask_Sync_FTP extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();

            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            pDialog.setMessage("обновление данных, подождите...");

            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                getFloor();  // Синхронизация файлов для всех складов
                publishProgress(1);
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
           /* Toast.makeText(context_Activity, "Загруженно на " + this_data_now + ": \nXML:" + files_kol + " " + files_size +
                    "\n БД: " + files_kol_sql + " " + files_size_sql, Toast.LENGTH_SHORT).show();*/

            button_const.setSummary("Данные на: " + this_data_now + ": \nXML: " + files_kol + " " + files_size +
                    "\nБД: " + files_kol_sql + " " + files_size_sql);
            ed = sp.edit();
            ed.putString("Data_dialog_sp_1", button_const.getSummary().toString()); // обновление агентов
            ed.commit();
            pDialog.setProgress(0);
            pDialog.cancel();
            pDialog.dismiss();
        }

        private void getFloor() throws InterruptedException {
            pDialog.setMessage("Загрузка данных, подождите...");
            try {
                FTPClient ftpClient = new FTPClient();
                String server = PEREM_FTP_SERV;
                int port = 21;
                String user = PEREM_FTP_LOGIN;
                String pass = PEREM_FTP_PASS;
                String encoding = System.getProperty("file.encoding");
                //FTPCheckFileExists ftpApp = new FTPCheckFileExists();
                try {
                    ftpClient.connect(server, port);
                    ftpClient.login(user, pass);
                    ///MT_Sunbell_Karakol/MTW_Data/

                    //// Пример Java FTP - вычисление общего количества подкаталогов, файлов и размера каталога
                    ftpClient.enterLocalPassiveMode();
                    String remoteDirPath_XML = PEREM_FTP_DISTR_XML;
                    long[] dirInfo = calculateDirectoryInfo(ftpClient, remoteDirPath_XML, "");
                    System.out.println("Total dirs: " + dirInfo[0]);
                    System.out.println("Total files: " + dirInfo[1]);
                    System.out.println("Total size: " + dirInfo[2]);
                    Log.e("Дирикторий=", " = " + dirInfo[0]);
                    Log.e("Файлов=", " = " + dirInfo[1]);
                    Log.e("Размер=", " = " + dirInfo[2] + " байт");
                    Log.e("Размер=", " = " + dirInfo[2] / 1024 + " кбайт");
                    Log.e("Размер=", " = " + dirInfo[2] / 1048576 + " мбайт");

                    if (dirInfo[1] > 4) {
                        files_kol = dirInfo[1] + "файлов";
                    } else files_kol = dirInfo[1] + "файла";

                    files_size = dirInfo[2] / 1048576 + " мбайт";


                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne1 = new BigDecimal(100 / Double.valueOf(dirInfo[1]));


                  /*  ftpClient.enterLocalPassiveMode();
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                    String remoteFile1 = "/chel/xml/Done/"; //отсюда
                    File downloadFile1 = new File("D:/download"); //сюда все файлы с расширением .res
                    //String path_start = Environment.getExternalStorageDirectory().toString() + "/Price/XLS_KerkinR/";
                    String path_phobe_db = context.getDatabasePath("suncape_const_db.db3").getAbsolutePath();
                    OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(path_phobe_db));
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    ftpClient.enterLocalPassiveMode();
                    ftpClient.retrieveFile(remoteDirPath+"suncape_const_db.db3", outputStream1);
                    outputStream1.close();*/

                    ftpClient.enterLocalPassiveMode();
                    String remoteDirPath_db3 = PEREM_FTP_DISTR_db3;
                    long[] dirInfo_db3 = calculateDirectoryInfo(ftpClient, remoteDirPath_db3, "");
                    System.out.println("Total dirs: " + dirInfo_db3[0]);
                    System.out.println("Total files: " + dirInfo_db3[1]);
                    System.out.println("Total size: " + dirInfo_db3[2]);
                    Log.e("Дирикторий=", " = " + dirInfo_db3[0]);
                    Log.e("Файлов=", " = " + dirInfo_db3[1]);
                    Log.e("Размер=", " = " + dirInfo_db3[2] + " байт");
                    Log.e("Размер=", " = " + dirInfo_db3[2] / 1024 + " кбайт");
                    Log.e("Размер=", " = " + dirInfo_db3[2] / 1048576 + " мбайт");


                    if (dirInfo_db3[1] > 4) {
                        files_kol_sql = dirInfo_db3[1] + " файлов";
                    } else files_kol_sql = dirInfo_db3[1] + " файла";

                    if (Double.valueOf(dirInfo_db3[2] / 1048576) < 1) {
                        files_size_sql = dirInfo_db3[2] / 1024 + " кбайт";
                    } else files_size_sql = dirInfo_db3[2] / 1048576 + " мбайт";


                    BigDecimal f2 = new BigDecimal(0.0);
                    BigDecimal pointOne2 = new BigDecimal(100 / Double.valueOf(dirInfo[1]));


                    FTPFile[] ftpFiles_list_XML = ftpClient.listFiles(remoteDirPath_XML);

                    for (FTPFile ftpFile_XML : ftpFiles_list_XML) {
                        f1 = f1.add(pointOne1);
                        pDialog.setProgress(f1.intValue());
                        String file_server_xml = PEREM_FTP_DISTR_XML + "/" + ftpFile_XML.getName(); // путь на сервере
                        String file_sd = Environment.getExternalStorageDirectory().toString() + "/Price/XLS_KerkinR/";  // путь к базам данных на телефоне
                        String file_db = PrefActivity_Splash.this.getDatabasePath(ftpFile_XML.getName()).getAbsolutePath(); // путь к databases
                        Log.e("PutXML2:", PEREM_FTP_DISTR_XML);
                        Log.e("PutXML:", file_server_xml);

                        if (ftpFile_XML.isFile()) {
                            if (ftpFile_XML.getName().equals(mass_MTW)) ;
                            // кода для скачивания файла с FTP
                            outputStream = new FileOutputStream(new File(file_db));  // путь куда сохранить данные
                            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                            ftpClient.enterLocalPassiveMode();
                            // ftp: забрать файл из этой директ, в эту директ
                            ftpClient.retrieveFile(file_server_xml, outputStream);
                            outputStream.close();
                        }
                    }


                    FTPFile[] ftpFiles_list_db3 = ftpClient.listFiles(remoteDirPath_db3);

                    for (FTPFile ftpFile_db3 : ftpFiles_list_db3) {
                        f2 = f2.add(pointOne2);
                        pDialog.setProgress(f2.intValue());
                        String file_server_sql = PEREM_FTP_DISTR_db3 + "/" + ftpFile_db3.getName(); // путь на сервере
                        String file_sd = Environment.getExternalStorageDirectory().toString() + "/Price/XLS_KerkinR/";  // путь к базам данных на телефоне
                        String file_db = PrefActivity_Splash.this.getDatabasePath(ftpFile_db3.getName()).getAbsolutePath(); // путь к databases
                        Log.e("PutDB3:", file_server_sql);

                        if (ftpFile_db3.isFile()) {
                            if (ftpFile_db3.getName().equals(mass_SQL)) ;
                            // кода для скачивания файла с FTP
                            outputStream = new FileOutputStream(new File(file_db));  // путь куда сохранить данные
                            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                            ftpClient.enterLocalPassiveMode();
                            // ftp: забрать файл из этой директ, в эту директ
                            ftpClient.retrieveFile(file_server_sql, outputStream);
                            outputStream.close();
                        }
                    }




                          /*  inputStreamut = new FileInputStream(new File(file_sd));
                            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                            ftpClient.enterLocalPassiveMode();
                            ftpClient.storeFile(file_server, inputStreamut);
                            inputStreamut.close();*/


                           /* outputStream = new FileOutputStream(new File(data));
                            inputStreamut = new FileInputStream(new File(data));
                            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                            ftpClient.enterLocalPassiveMode();
                             ftpClient.retrieveFile("/Server/Server_Start"+ftpFile.getName(), outputStream);
                            ftpClient.storeFile("/Server/Server_Start/"+ftpFile.getName(), inputStreamut);
ftpClient.enterLocalPassiveMode();
                            outputStream.close();
                            inputStreamut.close();*/


                    ftpClient.logout();
                    ftpClient.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    if (ftpClient.isConnected()) {
                        try {
                            ftpClient.logout();
                            ftpClient.disconnect();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }


            } catch (Exception e) {
                // Toast.makeText(context_Activity, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
            }

        }  // Синхронизация файлов для всех складов

    }

    // Скачиаваник файла для постоянных данных
    private class MyAsyncTask_Sync_FTP_CONST extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();

            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            pDialog.setMessage("обновление данных, подождите...");

            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                getFloor();  // Синхронизация файлов для всех складов
                publishProgress(1);
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
            button_id_catigory.setSummary("Данные на: " + this_data_now + "\nБД: " + files_kol_sql + " " + files_size_sql);
            ed = sp.edit();
            ed.putString("Data_dialog_sp_2", button_id_catigory.getSummary().toString()); // обновление агентов
            ed.commit();
            pDialog.setProgress(0);
            pDialog.cancel();
            pDialog.dismiss();

            //  pDialog.dismiss();
        }

        private void getFloor() throws InterruptedException {
            pDialog.setMessage("Загрузка данных, подождите...");
            try {

                FTPClient ftpClient = new FTPClient();
                String server = PEREM_FTP_SERV;
                int port = 21;
                String user = PEREM_FTP_LOGIN;
                String pass = PEREM_FTP_PASS;


                String encoding = System.getProperty("file.encoding");
                //FTPCheckFileExists ftpApp = new FTPCheckFileExists();

                try {
                    ftpClient.connect(server, port);
                    ftpClient.login(user, pass);
                    ftpClient.enterLocalPassiveMode();
                    String remoteDirPath_DB3 = PEREM_FTP_DISTR_db3 + "/sunbell_const_db.db3";
                    long[] dirInfo = calculateDirectoryInfo(ftpClient, remoteDirPath_DB3, "");
                    System.out.println("Total dirs: " + dirInfo[0]);
                    System.out.println("Total files: " + dirInfo[1]);
                    System.out.println("Total size: " + dirInfo[2]);
                    Log.e("Дирикторий=", " = " + dirInfo[0]);
                    Log.e("Файлов=", " = " + dirInfo[1]);
                    Log.e("Размер=", " = " + dirInfo[2] + " байт");
                    Log.e("Размер=", " = " + dirInfo[2] / 1024 + " кбайт");
                    Log.e("Размер=", " = " + dirInfo[2] / 1048576 + " мбайт");

                    if (dirInfo[1] > 4) {
                        files_kol_sql = dirInfo[1] + " файлов";
                    } else files_kol_sql = dirInfo[1] + " файла";

                    if (Double.valueOf(dirInfo[2] / 1048576) < 1) {
                        files_size_sql = dirInfo[2] / 1024 + " кбайт";
                    } else files_size_sql = dirInfo[2] / 1048576 + " мбайт";

                    files_kol = dirInfo[1] + "файлов";
                    files_size = dirInfo[2] / 1048576 + " мбайт";

                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(dirInfo[1]));

                    String file_server_db3 = PEREM_FTP_DISTR_db3 + "/sunbell_const_db.db3"; // путь на сервере
                    String file_db = PrefActivity_Splash.this.getDatabasePath("sunbell_const_db.db3").getAbsolutePath(); // путь к databases

                    outputStream = new FileOutputStream(new File(file_db));  // путь куда сохранить данные
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    ftpClient.enterLocalPassiveMode();
                    // ftp: забрать файл из этой директ, в эту директ
                    ftpClient.retrieveFile(file_server_db3, outputStream);
                    outputStream.close();

                    ftpClient.logout();
                    ftpClient.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    if (ftpClient.isConnected()) {
                        try {
                            ftpClient.logout();
                            ftpClient.disconnect();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }


            } catch (Exception e) {
                // Toast.makeText(context_Activity, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
            }

        }  // Синхронизация файлов для всех складов

    }

    // Скачиаваник стартовых файлов
    private class MyAsyncTask_Sync_FTP_TEST extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();

            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            pDialog.setMessage("обновление данных, подождите...");

            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                getFloor();  // Синхронизация файлов для всех складов
                publishProgress(1);
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
           /* Toast.makeText(context_Activity, "Загруженно на " + this_data_now + ": \nXML:" + files_kol + " " + files_size +
                    "\n БД: " + files_kol_sql + " " + files_size_sql, Toast.LENGTH_SHORT).show();*/

            button_const.setSummary("Данные на: " + this_data_now + ": \nXML: " + files_kol + " " + files_size +
                    "\nБД: " + files_kol_sql + " " + files_size_sql);
            ed = sp.edit();
            ed.putString("Data_dialog_sp_1", button_const.getSummary().toString()); // обновление агентов
            ed.commit();
            pDialog.setProgress(0);
            pDialog.cancel();
            pDialog.dismiss();

            //  pDialog.dismiss();
        }

        private void getFloor() throws InterruptedException {
            pDialog.setMessage("Загрузка данных, подождите...");
            try {

                FTPClient ftpClient = new FTPClient();
                String server = PEREM_FTP_SERV;
                int port = 21;
                String user = PEREM_FTP_LOGIN;
                String pass = PEREM_FTP_PASS;


                String encoding = System.getProperty("file.encoding");
                //FTPCheckFileExists ftpApp = new FTPCheckFileExists();

                try {
                    ftpClient.connect(server, port);
                    ftpClient.login(user, pass);
                    ///MT_Sunbell_Karakol/MTW_Data/

                    //// Пример Java FTP - вычисление общего количества подкаталогов, файлов и размера каталога
                    ftpClient.enterLocalPassiveMode();
                    String remoteDirPath_XML = PEREM_FTP_DISTR_XML;
                    long[] dirInfo = calculateDirectoryInfo2(ftpClient, remoteDirPath_XML, "");
                    System.out.println("Total dirs: " + dirInfo[0]);
                    System.out.println("Total files: " + dirInfo[1]);
                    System.out.println("Total size: " + dirInfo[2]);
                    Log.e("Дирикторий=", " = " + dirInfo[0]);
                    Log.e("Файлов=", " = " + dirInfo[1]);
                    Log.e("Размер=", " = " + dirInfo[2] + " байт");
                    Log.e("Размер=", " = " + dirInfo[2] / 1024 + " кбайт");
                    Log.e("Размер=", " = " + dirInfo[2] / 1048576 + " мбайт");

                    if (dirInfo[1] > 4) {
                        files_kol = dirInfo[1] + "файлов";
                    } else files_kol = dirInfo[1] + "файла";
                    files_size = dirInfo[2] / 1048576 + " мбайт";

                    Log.e("Дата=", " = " + dirInfo[2] / 1048576 + " мбайт");


                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne1 = new BigDecimal(100 / Double.valueOf(dirInfo[1]));


                  /*  ftpClient.enterLocalPassiveMode();
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                    String remoteFile1 = "/chel/xml/Done/"; //отсюда
                    File downloadFile1 = new File("D:/download"); //сюда все файлы с расширением .res
                    //String path_start = Environment.getExternalStorageDirectory().toString() + "/Price/XLS_KerkinR/";
                    String path_phobe_db = context.getDatabasePath("suncape_const_db.db3").getAbsolutePath();
                    OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(path_phobe_db));
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    ftpClient.enterLocalPassiveMode();
                    ftpClient.retrieveFile(remoteDirPath+"suncape_const_db.db3", outputStream1);
                    outputStream1.close();*/

                    ftpClient.enterLocalPassiveMode();
                    String remoteDirPath_db3 = PEREM_FTP_DISTR_db3;
                    long[] dirInfo_db3 = calculateDirectoryInfo(ftpClient, remoteDirPath_db3, "");
                    System.out.println("Total dirs: " + dirInfo_db3[0]);
                    System.out.println("Total files: " + dirInfo_db3[1]);
                    System.out.println("Total size: " + dirInfo_db3[2]);
                    Log.e("Дирикторий=", " = " + dirInfo_db3[0]);
                    Log.e("Файлов=", " = " + dirInfo_db3[1]);
                    Log.e("Размер=", " = " + dirInfo_db3[2] + " байт");
                    Log.e("Размер=", " = " + dirInfo_db3[2] / 1024 + " кбайт");
                    Log.e("Размер=", " = " + dirInfo_db3[2] / 1048576 + " мбайт");


                    if (dirInfo_db3[1] > 4) {
                        files_kol_sql = dirInfo_db3[1] + " файлов";
                    } else files_kol_sql = dirInfo_db3[1] + " файла";

                    if (Double.valueOf(dirInfo_db3[2] / 1048576) < 1) {
                        files_size_sql = dirInfo_db3[2] / 1024 + " кбайт";
                    } else files_size_sql = dirInfo_db3[2] / 1048576 + " мбайт";


                    BigDecimal f2 = new BigDecimal(0.0);
                    BigDecimal pointOne2 = new BigDecimal(100 / Double.valueOf(dirInfo[1]));


                    FTPFile[] ftpFiles_list_XML = ftpClient.listFiles(remoteDirPath_XML);

                    for (FTPFile ftpFile_XML : ftpFiles_list_XML) {
                        f1 = f1.add(pointOne1);
                        pDialog.setProgress(f1.intValue());
                        String file_server_xml = PEREM_FTP_DISTR_XML + "/" + ftpFile_XML.getName(); // путь на сервере
                        String file_sd = Environment.getExternalStorageDirectory().toString() + "/Price/XLS_KerkinR/";  // путь к базам данных на телефоне
                        String file_db = PrefActivity_Splash.this.getDatabasePath(ftpFile_XML.getName()).getAbsolutePath(); // путь к databases
                        Log.e("PutXML2:", PEREM_FTP_DISTR_XML);
                        Log.e("PutXML:", file_server_xml);

                        if (ftpFile_XML.isFile()) {
                            if (ftpFile_XML.getName().equals(mass_MTW)) ;
                            // кода для скачивания файла с FTP
                            outputStream = new FileOutputStream(new File(file_db));  // путь куда сохранить данные
                            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                            ftpClient.enterLocalPassiveMode();
                            // ftp: забрать файл из этой директ, в эту директ
                            ftpClient.retrieveFile(file_server_xml, outputStream);
                            outputStream.close();
                        }
                    }


                    FTPFile[] ftpFiles_list_db3 = ftpClient.listFiles(remoteDirPath_db3);

                    for (FTPFile ftpFile_db3 : ftpFiles_list_db3) {
                        f2 = f2.add(pointOne2);
                        pDialog.setProgress(f2.intValue());
                        String file_server_sql = PEREM_FTP_DISTR_db3 + "/" + ftpFile_db3.getName(); // путь на сервере
                        String file_sd = Environment.getExternalStorageDirectory().toString() + "/Price/XLS_KerkinR/";  // путь к базам данных на телефоне
                        String file_db = PrefActivity_Splash.this.getDatabasePath(ftpFile_db3.getName()).getAbsolutePath(); // путь к databases
                        Log.e("PutDB3:", file_server_sql);

                        if (ftpFile_db3.isFile()) {
                            if (ftpFile_db3.getName().equals(mass_SQL)) ;
                            // кода для скачивания файла с FTP
                            outputStream = new FileOutputStream(new File(file_db));  // путь куда сохранить данные
                            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                            ftpClient.enterLocalPassiveMode();
                            // ftp: забрать файл из этой директ, в эту директ
                            ftpClient.retrieveFile(file_server_sql, outputStream);
                            outputStream.close();
                        }
                    }




                          /*  inputStreamut = new FileInputStream(new File(file_sd));
                            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                            ftpClient.enterLocalPassiveMode();
                            ftpClient.storeFile(file_server, inputStreamut);
                            inputStreamut.close();*/


                           /* outputStream = new FileOutputStream(new File(data));
                            inputStreamut = new FileInputStream(new File(data));
                            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                            ftpClient.enterLocalPassiveMode();
                             ftpClient.retrieveFile("/Server/Server_Start"+ftpFile.getName(), outputStream);
                            ftpClient.storeFile("/Server/Server_Start/"+ftpFile.getName(), inputStreamut);
ftpClient.enterLocalPassiveMode();
                            outputStream.close();
                            inputStreamut.close();*/


                    ftpClient.logout();
                    ftpClient.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    if (ftpClient.isConnected()) {
                        try {
                            ftpClient.logout();
                            ftpClient.disconnect();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }


            } catch (Exception e) {
                // Toast.makeText(context_Activity, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
            }

        }  // Синхронизация файлов для всех складов

    }

    // Константы для записи
    protected void Constanta_Write(Integer ftp_index) {
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        // PEREM_PUT_REGION = sp.getString("list_preference_ftp", "0");
        PEREM_PUT_REGION = listPreference.getEntryValues()[ftp_index].toString();

       /* Log.e("элемент тэга1", "V"+PEREM_PUT_REGION );
        Log.e("элемент тэга2", "V"+listPreference.getEntry());
        Log.e("элемент тэга3", "V"+listPreference.getEntries()[ftp_index]);
        Log.e("элемент тэга4", "V"+listPreference.getEntryValues()[ftp_index]);*/

        if (!listPreference.getEntryValues().equals("1")) {
          /*  put_distr_xml = "/MT_Sunbell_Karakol/MTW_Data";       // Путь к файлам XML на сервере Каракол
            put_distr_db3 = "/MT_Sunbell_Karakol/SqliteDB";       // Путь к файлам Sqlite на сервере Каракол*/
            put_distr_xml = PEREM_PUT_REGION + "MTW_Data";       // Путь к файлам XML на сервере Каракол
            put_distr_db3 = PEREM_PUT_REGION + "SqliteDB";       // Путь к файлам Sqlite на сервере Каракол
            put_distr_sos = PEREM_PUT_REGION + "MTW_SOS";       // Путь к файлам Sqlite на сервере Каракол
        } else {
            put_distr_xml = "/MT_Sunbell_ZAP";       // Путь к файлам XML на сервере Каракол
            put_distr_db3 = "/MT_Sunbell_ZAP";       // Путь к файлам Sqlite на сервере Каракол
        }


        ed = sp.edit();
        ed.putString("PEREM_ANDROID_ID", Android_id.substring(0, Android_id.length() - 2));    //Код Andoid_ID
        ed.putString("PEREM_ANDROID_ID_ADMIN", "8fa09495388020");                        //Код Andoid_ID Администратор
        ed.putString("PEREM_FTP_DISTR_XML", put_distr_xml);    //запись данных имя пароль
        ed.putString("PEREM_FTP_DISTR_db3", put_distr_db3);    //запись данных имя пароль
        ed.putString("PEREM_FTP_DISTR_sos_files", put_distr_sos);    //запись данных имя пароль
        ed.commit();
    }

    // Константы для чтения
    protected void Constanta_Read() {
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        PEREM_FTP_SERV = sp.getString("PEREM_FTP_SERV", "0");
        PEREM_FTP_LOGIN = sp.getString("PEREM_FTP_LOGIN", "0");
        PEREM_FTP_PASS = sp.getString("PEREM_FTP_PASS", "0");
        PEREM_FTP_DISTR_XML = sp.getString("PEREM_FTP_DISTR_XML", "0");
        PEREM_FTP_DISTR_db3 = sp.getString("PEREM_FTP_DISTR_db3", "0");
        PEREM_MAIL_LOGIN = sp.getString("PEREM_MAIL_LOGIN", "0");
        PEREM_MAIL_PASS = sp.getString("PEREM_MAIL_PASS", "0");
        PEREM_MAIL_START = sp.getString("PEREM_MAIL_START", "0");
        PEREM_MAIL_END = sp.getString("PEREM_MAIL_END", "0");
        DATA_DIALOG_SPLASH_1 = sp.getString("Data_dialog_sp_1", "0");
        DATA_DIALOG_SPLASH_2 = sp.getString("Data_dialog_sp_2", "0");

        PEREM_DB3_CONST = sp.getString("PEREM_DB3_CONST", "0");                  //чтение данных: Путь к базам данных с константами
        PEREM_DB3_BASE = sp.getString("PEREM_DB3_BASE", "0");                    //чтение данных: Путь к базам данных с товаром
        PEREM_DB3_RN = sp.getString("PEREM_DB3_RN", "0");                        //чтение данных: Путь к базам данных с накладными
        PEREM_number_update_new = sp.getString("PEREM_NEW_UPATE_DB_TABLE", "0");                        //чтение данных: Путь к базам данных с накладными
        PEREM_number_update_old = sp.getString("PEREM_number_update_old", "0");                        //чтение данных: Путь к базам данных с накладными
    }

    //// Пример Java FTP - вычисление общего количества подкаталогов, файлов и размера каталога
    public static long[] calculateDirectoryInfo(FTPClient ftpClient, String parentDir, String currentDir) throws IOException {
        long[] info = new long[3];
        long totalSize = 0;
        int totalDirs = 0;
        int totalFiles = 0;

        String dirToList = parentDir;
        if (!currentDir.equals("")) {
            dirToList += "/" + currentDir;
        }

        try {
            FTPFile[] subFiles = ftpClient.listFiles(dirToList);

            if (subFiles != null && subFiles.length > 0) {
                for (FTPFile aFile : subFiles) {
                    String currentFileName = aFile.getName();
                    if (currentFileName.equals(".")
                            || currentFileName.equals("..")) {
                        // skip parent directory and the directory itself
                        continue;
                    }

                    if (aFile.isDirectory()) {
                        totalDirs++;
                        long[] subDirInfo =
                                calculateDirectoryInfo(ftpClient, dirToList, currentFileName);
                        totalDirs += subDirInfo[0];
                        totalFiles += subDirInfo[1];
                        totalSize += subDirInfo[2];
                    } else {
                        totalSize += aFile.getSize();
                        totalFiles++;
                    }
                }
            }

            info[0] = totalDirs;
            info[1] = totalFiles;
            info[2] = totalSize;

            return info;
        } catch (IOException ex) {
            throw ex;
        }
    }


    //// Пример Java FTP - вычисление общего количества подкаталогов, файлов и размера каталога
    public static long[] calculateDirectoryInfo2(FTPClient ftpClient, String parentDir, String currentDir) throws IOException {
        long[] info = new long[4];
        long totalSize = 0;
        int totalDirs = 0;
        int totalFiles = 0;

        String dirToList = parentDir;
        if (!currentDir.equals("")) {
            dirToList += "/" + currentDir;
        }

        try {
            FTPFile[] subFiles = ftpClient.listFiles(dirToList);
            printFileDetails(subFiles);

            if (subFiles != null && subFiles.length > 0) {
                for (FTPFile aFile : subFiles) {
                    String currentFileName = aFile.getName();
                    if (currentFileName.equals(".")
                            || currentFileName.equals("..")) {
                        // skip parent directory and the directory itself
                        continue;
                    }

                    if (aFile.isDirectory()) {
                        totalDirs++;
                        long[] subDirInfo = calculateDirectoryInfo(ftpClient, dirToList, currentFileName);
                        totalDirs += subDirInfo[0];
                        totalFiles += subDirInfo[1];
                        totalSize += subDirInfo[2];
                    } else {
                        totalSize += aFile.getSize();
                        totalFiles++;
                    }
                }
            }

            info[0] = totalDirs;
            info[1] = totalFiles;
            info[2] = totalSize;

            return info;
        } catch (IOException ex) {
            throw ex;
        }
    }

    private static void printFileDetails(FTPFile[] files) {
        DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (FTPFile file : files) {
            String details = file.getName();
            if (file.isDirectory()) {
                details = "[" + details + "]";
            }

            details += "\t\t" + file.getSize();
            details += "\t\t" + dateFormater.format(file.getTimestamp().getTime());
            System.out.println(details);
            // Log.e("FILES", details);
            Log.e("Name", file.getName());
            Log.e("DATA", "_" + dateFormater.format(file.getTimestamp().getTime()));
        }
    }

    private static void printNames(String files[]) {
        if (files != null && files.length > 0) {
            for (String aFile : files) {
                System.out.println(aFile);
                Log.e("FILES", aFile);
            }
        }
    }

    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
                Log.e("SERVER", aReply);
            }
        }
    }


    // Для отправки данных для регистрации
    protected void Mail() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


    }

    private class sender_mail_async extends AsyncTask<Object, String, Boolean> {
        ProgressDialog WaitingDialog;

        @Override
        protected void onPreExecute() {
            WaitingDialog = ProgressDialog.show(context_Activity, "Отправка...", "Выполняется загрузка, подождите...", true);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            WaitingDialog.dismiss();
            Toast.makeText(context_Activity, "Данные успешно отправленны!", Toast.LENGTH_LONG).show();
            //((Activity)mainContext).finish();
        }

        @Override
        protected Boolean doInBackground(Object... params) {

            try {
                title = "Регистрация КПК: " + this_data_now;
                text = new_write;
                from = PEREM_MAIL_START;
                where = PEREM_MAIL_END;
                attach = "";
                MailSenderClass sender = new MailSenderClass(PEREM_MAIL_LOGIN, PEREM_MAIL_PASS, "465", "smtp.mail.ru");

                sender.sendMail(title, text, from, where, attach);
            } catch (Exception e) {
                Toast.makeText(context_Activity, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
            }

            return false;
        }
    }

    // Обновление таблиц и создание если не сущ
    private class MyAsyncTask_Sync_Update_Create_Table extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            // pDialog.setMessage("Синхронизация цен. Подождите...");
            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                publishProgress(1);
                getFloor();  // Синхронизация файлов для всех складов
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
            Log.e("ПОТОК=", "Конец потока");
            pDialog.setProgress(0);
            pDialog.dismiss();
            Toast.makeText(context_Activity, "Таблицы успешно созданы!", Toast.LENGTH_SHORT).show();
            PrefActivity_Splash.MyAsyncTask_Sync_Update_Create_Rows asyncTask = new PrefActivity_Splash.MyAsyncTask_Sync_Update_Create_Rows();
            asyncTask.execute();
        }

        private void getFloor() throws InterruptedException {
            try {
                SQLiteDatabase db_rn = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
                String query_new_table_otchet_brends, query_new_table_otchet_debet, query_new_table_mail_conn, query_new_table_mail_conn_write;

                query_new_table_otchet_brends = "CREATE TABLE IF NOT EXISTS otchet_brends_otgruz (brends TEXT, kol TEXT, summa, itogo);";
                query_new_table_otchet_debet = "CREATE TABLE IF NOT EXISTS otchet_debet (d_agent_name TEXT, d_agent_uid TEXT, d_kontr_name TEXT, d_kontr_uid TEXT, d_summa TEXT);";

                String[] mass_query = new String[]{query_new_table_otchet_brends, query_new_table_otchet_debet};

                BigDecimal f1 = new BigDecimal(0.0);
                BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(mass_query.length));

                for (int q = 0; q < mass_query.length; q++) {
                    final Cursor cursor = db_rn.rawQuery(mass_query[q], null);
                    cursor.moveToFirst();
                    while (cursor.isAfterLast() == false) {
                        cursor.moveToNext();
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                    }
                    cursor.close();
                }
                db_rn.close();

                SQLiteDatabase db_const = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
                {
                    query_new_table_mail_conn = "CREATE TABLE IF NOT EXISTS const_mail (region TEXT, operator_mail TEXT, operator_mail_pass TEXT, mail_where TEXT);";
                    final Cursor cursor = db_const.rawQuery(query_new_table_mail_conn, null);
                    cursor.moveToFirst();
                    while (cursor.isAfterLast() == false) {
                        cursor.moveToNext();
                   /* f1 = f1.add(pointOne);
                    pDialog.setProgress(f1.intValue());*/
                    }
                    cursor.close();
                }


                String[] mass_user = getResources().getStringArray(R.array.mass_Region_Put_Value);
                String[] mass_mail_from = getResources().getStringArray(R.array.mass_mail_where);
                for (int k = 0; k < mass_user.length; k++) {
                    query_new_table_mail_conn_write = "INSERT INTO const_mail " +
                            "(region, operator_mail, operator_mail_pass, mail_where) VALUES " +
                            "('" + mass_user[k] + "', 'sunbellagents@gmail.com', 'fyczcoexpaspsham', '" + mass_mail_from[k] + "');";
                    final Cursor cursor = db_const.rawQuery(query_new_table_mail_conn_write, null);
                    cursor.moveToFirst();
                    while (cursor.isAfterLast() == false) {
                        cursor.moveToNext();
                   /* f1 = f1.add(pointOne);
                    pDialog.setProgress(f1.intValue());*/
                    }
                    cursor.close();
                }
                db_const.close();

            } catch (Exception e) {

                Log.e("Err Create Table..", "rrr");
            }


        }  // Синхронизация файлов для всех складов

    }

    // Обновление таблиц и создание если не сущ
    private class MyAsyncTask_Sync_Update_Create_Rows extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            pDialog.setMessage("Создание новых столбцов, Подождите...");
            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                publishProgress(1);
                getFloor();  // Синхронизация файлов для всех складов
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
            Log.e("ПОТОК=", "Конец потока");
            pDialog.setProgress(0);
            Toast.makeText(context_Activity, "Обновление столбцов завершенно!", Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
        }

        private void getFloor() throws InterruptedException {
            //pDialog.setMessage("Загрузка продуктов. Подождите...");
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
            try {
                query_new_rows_baseRN_debet_new = "ALTER TABLE base_RN_All ADD status TEXT;";
                final Cursor cursor = db.rawQuery(query_new_rows_baseRN_debet_new, null);
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    cursor.moveToNext();
                }
                cursor.close();
            } catch (Exception e) {
                Log.e("Err Create Rows..", "Столбец status уже создан");
            }

            try {
                query_new_rows_baseRN_status = "ALTER TABLE base_RN_All ADD debet_new TEXT;";
                final Cursor cursor = db.rawQuery(query_new_rows_baseRN_status, null);
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    cursor.moveToNext();
                }
                cursor.close();
            } catch (Exception e) {
                Log.e("Err Create Rows..", "Столбец debet уже создан");
            }
            db.close();

        }  // Синхронизация файлов для всех складов

    }


    protected void ListPreference_Brends_Adapter() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT * FROM base_in_brends_id";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            preferenceScreen.setEnabled(true);
            mass_brends_name = new String[cursor.getCount()];
            mass_brends_pref = new String[cursor.getCount()];
            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("brends"));        // столбец с брендом
                String group = cursor.getString(cursor.getColumnIndexOrThrow("group_type"));   // столбец с префиксом группы
                String pref = cursor.getString(cursor.getColumnIndexOrThrow("prefic"));   // столбец с префиксом группы
                mass_brends_name[cursor.getPosition()] = name;
                mass_brends_pref[cursor.getPosition()] = pref;
                cursor.moveToNext();
            }
            MULTIlistPreference_brends.setEntries(mass_brends_name);
            MULTIlistPreference_brends.setEntryValues(mass_brends_pref);
        } else {
            preferenceScreen.setEnabled(false);
            mass_brends_name = new String[1];
            mass_brends_pref = new String[1];
            mass_brends_name[1] = "No Brends";
            mass_brends_pref[1] = "No Brends";
            MULTIlistPreference_brends.setEntries(mass_brends_name);
            MULTIlistPreference_brends.setEntryValues(mass_brends_pref);
        }
        cursor.close();
        db.close();
        try {


        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка, нет базы данных", Toast.LENGTH_SHORT).show();
            Log.e("Error...", "Ошибка, нет базы данных");
            preferenceScreen.setEnabled(false);
            /*MULTIlistPreference_brends.setEntries(mass_brends_name);
            MULTIlistPreference_brends.setEntryValues(mass_brends_pref);*/
        }

    }


    protected void Write_List_Group_Brends() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
        String query = "SELECT * FROM const_group_brends\n" +
                "LEFT JOIN const_group_name ON const_group_brends.brends_group = const_group_name.group_id\n" +
                "ORDER BY group_id;\n";
        final Cursor cursor = db.rawQuery(query, null);
        String[][] mass = new String[cursor.getCount()][2];
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String kod = cursor.getString(cursor.getColumnIndexOrThrow("brends_kod"));
            String group = cursor.getString(cursor.getColumnIndexOrThrow("brends_group"));
            mass[cursor.getPosition()][0] = kod;
            mass[cursor.getPosition()][1] = group;
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        SQLiteDatabase db_w = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query_w = "SELECT * FROM base_in_brends_id;";
        final Cursor cursor_w = db_w.rawQuery(query_w, null);
        ContentValues localContentValues = new ContentValues();
        cursor_w.moveToFirst();
        while (cursor_w.isAfterLast() == false) {
            String kod = cursor_w.getString(cursor_w.getColumnIndexOrThrow("kod"));
            String group = cursor_w.getString(cursor_w.getColumnIndexOrThrow("group_type"));
            for (int i = 0; i < mass.length; i++) {
                if (mass[i][0].equals(kod)) {
                    localContentValues.put("group_type", mass[i][1]);
                    String[] arrayOfString = new String[1];
                    arrayOfString[0] = kod;
                    Log.e("WRITE", "Обновление данных: " + kod + "__" + mass[i][0] + "__" + group);
                    db_w.update("base_in_brends_id", localContentValues, "kod = ?", new String[]{kod});

                } else {
                    Log.e("WRITE", "Нет данных обновления: " + kod + "__" + mass[i][0]);
                }
            }
            cursor_w.moveToNext();
        }
        Log.e("WRITE", "Конец:");
        cursor_w.close();
        db_w.close();
    }


    protected void Permission_Data_RW() {
       /* sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        Boolean wr = sp.getBoolean("switch_preference_data", FALSE);             //чтение данных: координаты gps*/

        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        ed = sp.edit();
        if (PermissionUtils.hasPermissions(this)) {
            Log.e(TAG, "Разрешение получено");
            switchPreference_permission.setSummary("Разрешение получено");
            switchPreference_permission.setChecked(true);
            switchPreference_permission.setEnabled(false);
            ed.putBoolean("switch_preference_data", true);
            ed.commit();
        } else {
            switchPreference_permission.setEnabled(true);
            switchPreference_permission.setChecked(false);
            switchPreference_permission.setSummary("Разрешение не предоставлено");
            ed.putBoolean("switch_preference_data", false);
            ed.commit();
        }

        switchPreference_permission.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
             /*   if (switchPreference_permission.isChecked()) {
                    Log.e(TAG, "Разрешение получено");
                    switchPreference_permission.setSummary("Разрешение получено");
                    ed.putBoolean("switch_preference_data", true);
                    ed.commit();
                } else {
                    Log.e(TAG, "Разрешение не предоставлено");
                    switchPreference_permission.setSummary("Разрешение не предоставлено");
                    ed.putBoolean("switch_preference_data", false);
                    ed.commit();*/

                if (PermissionUtils.hasPermissions(PrefActivity_Splash.this)) return true;
                PermissionUtils.requestPermissions(PrefActivity_Splash.this, PERMISSION_STORAGE);
                return false;
            }
        });
    }

    protected void Permission_Nomeclatura() {
        // Настройки номенклатуры
        //android:key="preference_nomenclatura"
        switch_all_brends = (SwitchPreference) findPreference("switch_preference_sunbell_all_brends");                     // Отображение всех брендов
        switch_for_agent = (SwitchPreference) findPreference("switch_preference_sunbell_catg");                            // Отображение по готовому списку для агентов
        switch_HandSelected = (SwitchPreference) findPreference("check_box_brends");                                       // Отображение по фирмам (Категориям)
        MULTIlistPreference_brends = (MultiSelectListPreference) findPreference("multi_select_list_preference_brends");    // Мульти отбор по списку брендов
        if (switch_HandSelected.isChecked()) MULTIlistPreference_brends.setEnabled(true);
        else MULTIlistPreference_brends.setEnabled(false);


        switch_HandSelected.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Set<String> hs = sp.getStringSet("multi_select_list_preference_brends", new HashSet<String>());
                ed = sp.edit();
                if (switch_HandSelected.isChecked()) {
                    MULTIlistPreference_brends.setEnabled(true);
                    Log.e("Настройки: ", "Бренды: = (" + sp.getStringSet("multi_select_list_preference_brends", new HashSet<String>()) + ")");
                    ed.commit();
                    hs.add(String.valueOf(hs.size() + 1));
                    Log.e("1", "list: " + hs.toString());
                    ed.remove("multi_select_list_preference_brends");
                    ed.commit();
                    ed.putStringSet("multi_select_list_preference_brends", hs);
                    Log.e("2", "saved: " + ed.commit());
                } else {
                   /* mass_brends_name = new String[0];
                    mass_brends_pref = new String[0];
                    MULTIlistPreference_brends.setEntries(mass_brends_name);
                    MULTIlistPreference_brends.setEntryValues(mass_brends_pref);*/

                    ed = sp.edit();
                    ed.putStringSet("multi_select_list_preference_brends", new HashSet<String>()); // передача даты обновления
                    ed.remove("multi_select_list_preference_brends");
                    hs.clear();
                    ed.putStringSet("multi_select_list_preference_brends", hs);
                    MULTIlistPreference_brends.setValues(hs);
                    Log.e("Настройки: ", "Бренды: = (" + sp.getStringSet("multi_select_list_preference_brends", new HashSet<String>()) + ")");
                    ed.commit();
                    MULTIlistPreference_brends.setEnabled(false);
                }

                return false;
            }
        });

        switch_all_brends.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (switch_all_brends.isChecked()) {
                    switch_all_brends.setChecked(true);
                    switch_for_agent.setChecked(false);
                    switch_HandSelected.setChecked(false);
                    MULTIlistPreference_brends.setEnabled(false);
                    Log.e(TAG, "Выбранны все из всех");
                } else {
                    switch_all_brends.setChecked(false);
                    switch_for_agent.setChecked(true);
                    switch_HandSelected.setChecked(false);
                    MULTIlistPreference_brends.setEnabled(false);
                    Log.e(TAG, "Выбранны только для агентов");

                }
                return false;
            }
        });

        switch_for_agent.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (switch_for_agent.isChecked()) {
                    switch_all_brends.setChecked(false);
                    switch_for_agent.setChecked(true);
                    switch_HandSelected.setChecked(false);
                    MULTIlistPreference_brends.setEnabled(false);
                    Log.e(TAG, "Выбранны только для агентов");
                } else {
                    switch_all_brends.setChecked(true);
                    switch_for_agent.setChecked(false);
                    switch_HandSelected.setChecked(false);
                    MULTIlistPreference_brends.setEnabled(false);
                    Log.e(TAG, "Выбранны все из всех");
                }
                return false;
            }
        });

        switch_HandSelected.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (switch_HandSelected.isChecked()) {
                    switch_all_brends.setChecked(false);
                    switch_for_agent.setChecked(false);
                    // Доступ к мультисписку
                    MULTIlistPreference_brends.setEnabled(true);
                } else {
                    switch_all_brends.setChecked(true);
                    switch_for_agent.setChecked(false);
                    MULTIlistPreference_brends.setEnabled(false);
                }
                return false;
            }
        });


        MULTIlistPreference_brends.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.e("MULTI1", " " + preference);
                Log.e("MULTI2", " " + preference.getSummary());
                Log.e("MULTI3", " " + preference.getKey());
                Log.e("MULTI4", " " + preference.getDependency());
                Log.e("MULTI5", " " + preference.getSummary());
                Log.e("MULTI6", " " + preference.getSummary());
                Log.e("MULTI6_1", " " + preference.hasKey());
                Log.e("MULTI6_2", " " + preference.getTitle());
                Log.e("MULTI6_3", " " + preference.getTitle());
                Log.e("MULTI7", " " + MULTIlistPreference_brends.getSummary());
                Log.e("MULTI8", " " + MULTIlistPreference_brends.getEntryValues().toString());
                return false;
            }
        });
    }

    protected void Permission_Server() {
       /*
        <string name="ftp_ipConn_list">77.235.17.166</string>
        <string name="ftp_put_list">/MT_Sunbell_Bishkek/</string>
         */
        listPreference = (ListPreference) findPreference("ftp_put_list");
        listPreference_ftp_con = (ListPreference) findPreference("ftp_ipConn_list");
        button_const = (Preference) findPreference("button_const");


        listPreference.setEntries(R.array.mass_Region_Put_Name);
        listPreference.setEntryValues(R.array.mass_Region_Put_Value);

        listPreference_ftp_con.setEntries(R.array.mass_ftp_server_con);
        listPreference_ftp_con.setEntryValues(R.array.mass_ftp_server_con_value);


        listPreference.setSummary("путь: " + listPreference.getValue());
        listPreference_ftp_con.setSummary("ip-адрес: " + listPreference_ftp_con.getEntry());


        button_const.setSummary(sp.getString("Data_dialog_sp_1", "0"));
        button_const.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Constanta_Read();
                if (!sp.getString("list_preference_ftp", "0").equals("1")) {
                    Calendate_New();
                    Constanta_Read();

                    pDialog = new ProgressDialog(context_Activity);
                    pDialog.setMessage("Загрузка продуктов...");
                    pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    pDialog.setProgress(0);
                    pDialog.setMax(100);
                    pDialog.show();

                    Log.e("Put:", PEREM_FTP_DISTR_XML);
                    Log.e("Put:", PEREM_FTP_DISTR_db3);

                    PrefActivity_Splash.MyAsyncTask_Sync_FTP asyncTask = new PrefActivity_Splash.MyAsyncTask_Sync_FTP();
                    asyncTask.execute();
                } else {
                    Toast.makeText(context_Activity, "Выберите путь к данным на сервере!", Toast.LENGTH_SHORT).show();
                    Log.e("Кнопка TY...", " не выбран путь файлов");
                }

                return false;
            }
        });
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Constanta_Read();
                String stringValue = newValue.toString();
                if (preference instanceof ListPreference) {
                    ListPreference listPreference = (ListPreference) preference;
                    int index = listPreference.findIndexOfValue(stringValue);
                    preference.setSummary(
                            index >= 0
                                    ? "Путь: " + listPreference.getEntryValues()[index]
                                    : "Путь: /MT_Sunbell_ZAP/");
                    Constanta_Write(index);
                  /*  Log.e("Название тэга", "Путь на FTP: " + listPreference.getEntries()[index]);
                    Log.e("элемент тэга", "Путь на FTP: " + listPreference.getEntryValues()[index]);*/
                }
                return true;
            }
        });
        listPreference_ftp_con.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String stringValue = newValue.toString();
                if (preference instanceof ListPreference) {
                    ListPreference listPreference = (ListPreference) preference;
                    int index = listPreference.findIndexOfValue(stringValue);
                    preference.setSummary(
                            index >= 0
                                    ? listPreference.getEntries()[index]
                                    : "По умолчанию");
                }
                return true;
            }
        });

        Log.e(TAG, "PER:" + listPreference_ftp_con.getEntry());
        Log.e(TAG, "PER:" + listPreference_ftp_con.getEntries().toString());
        Log.e(TAG, "PER:" + listPreference_ftp_con.getValue());
        Log.e(TAG, "PER:" + listPreference_ftp_con.getSummary());
    }

    protected void Permission_TY() {
        //<string name="preference_type_ty">standart</string>

        listPreference_ty = (ListPreference) findPreference("preference_type_ty");
        listPreference_ty.setSummary(listPreference_ty.getEntry());
        listPreference_ty.setEntries(R.array.mass_type_ty_title);
        listPreference_ty.setEntryValues(R.array.mass_type_ty_value);

        Log.e(TAG, "PER2:" + listPreference_ty.getEntry());
        Log.e(TAG, "PER2:" + listPreference_ty.getEntries().toString());
        Log.e(TAG, "PER2:" + listPreference_ty.getValue());
        Log.e(TAG, "PER2:" + listPreference_ty.getSummary());


        listPreference_ty.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String stringValue = newValue.toString();
                if (preference instanceof ListPreference) {
                    ListPreference listPreference = (ListPreference) preference;
                    int index = listPreference.findIndexOfValue(stringValue);
                    preference.setSummary(
                            index >= 0
                                    ? listPreference.getEntries()[index]
                                    : "По умолчанию");
                }
                return true;
            }
        });
    }

    /////////////////  НАСТРОЙКИ ДЛЯ ВЫБОРА ПУТИ К БАЗЕ КАРТИНОК
    protected void Permission_putImgeToPhone() {
        switch_OldVersion = (SwitchPreference) findPreference("key_putToOld");
        switch_Files = (SwitchPreference) findPreference("key_putToFiles");
        switch_SDCard = (SwitchPreference) findPreference("key_putToSDCard");

        switch_OldVersion.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (switch_OldVersion.isChecked()) {
                    switch_OldVersion.setChecked(true);
                    switch_Files.setChecked(false);
                    switch_SDCard.setChecked(false);
                }
                return false;
            }
        });

        switch_Files.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (switch_Files.isChecked()) {
                    switch_OldVersion.setChecked(false);
                    switch_Files.setChecked(true);
                    switch_SDCard.setChecked(false);
                }
                return false;
            }
        });

        switch_SDCard.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (switch_SDCard.isChecked()) {
                    switch_OldVersion.setChecked(false);
                    switch_Files.setChecked(false);
                    switch_SDCard.setChecked(true);
                }
                return false;
            }
        });
    }

    protected void Permission_Loading_Start() {
        // preferenceCategory_FTP = (PreferenceCategory) findPreference("preferenc_menu_ftp");
        preferenceCategory_TY = (PreferenceCategory) findPreference("preferenc_menu_ty");
        preferenceCategory_Nomenclatura = (PreferenceCategory) findPreference("preferenc_menu_nomenclatura");
        preferenceCategory_All = (PreferenceCategory) findPreference("preferenc_menu_all");
        preferenceCategory_About = (PreferenceCategory) findPreference("preferenc_menu_about");
        preferenceCategory_All.setEnabled(false);
    }
}



/*
*  checkBoxPreference_brends.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (checkBoxPreference_brends.isChecked()) {
                    ListPreference_Brends_Adapter();
                    MULTIlistPreference_brends.setEnabled(true);
                    ed = sp.edit();
                    Log.e("Настройки: ", "Бренды: = (" + sp.getStringSet("multi_select_list_preference_brends", new HashSet<String>()) + ")");
                    ed.commit();

                    Set<String> hs = sp.getStringSet("multi_select_list_preference_brends", new HashSet<String>());
                    hs.add(String.valueOf(hs.size() + 1));
                    Log.e("1", "list: " + hs.toString());
                    ed.remove("multi_select_list_preference_brends");
                    ed.commit();
                    ed.putStringSet("multi_select_list_preference_brends", hs);
                    Log.e("2", "saved: " + ed.commit());


                } else {
                    mass_brends_name = new String[0];
                    mass_brends_pref = new String[0];
                    MULTIlistPreference_brends.setEntries(mass_brends_name);
                    MULTIlistPreference_brends.setEntryValues(mass_brends_pref);
                    ed = sp.edit();
                    ed.putStringSet("multi_select_list_preference_brends", new HashSet<String>()); // передача даты обновления
                    Log.e("Настройки: ", "Бренды: = (" + sp.getStringSet("multi_select_list_preference_brends", new HashSet<String>()) + ")");
                    ed.commit();
                    MULTIlistPreference_brends.setEnabled(false);
                }
                return false;
            }
        });*/


  /*        button_regist.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                PackageInfo pInfo = null;
                try {

                    pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    Log.e("Reg", pInfo.versionName);  // Версия приложения
                    Log.e("Reg", "Код: " + pInfo.versionCode + ", Ревиз: " + pInfo.baseRevisionCode); // Код и ревизия
                    Log.e("Reg", Build.BRAND + "_" + Build.MODEL);  // Бренд и модель (Код для регистрации)
                    Log.e("Reg", UUID.randomUUID().toString());  //
                    Log.e("Reg", Android_id.substring(0, Android_id.length() - 2));  //

                    String[] mass_versia = getResources().getStringArray(R.array.mass_ver_android);
                    for (int i = 21; i <= 30; i++) {
                        if (android.os.Build.VERSION.SDK_INT == i) {
                            Log.e("Reg Входит", i + String.valueOf(android.os.Build.VERSION.SDK_INT));
                            Log.e("Reg Входит", mass_versia[i]);
                            Calendate_New();
                            ed = sp.edit();
                            button_regist.setSummary("Версия Android подходит для работы; \n" + this_data_now);
                            ed.putString("Data_dialog_sp_2", button_const.getSummary().toString()); // обновление агентов
                            ed.commit();

                            new_write = ("");
                            new_write = (new_write + "Дата:" + this_data_now);
                            new_write = (new_write + "\n");
                            new_write = (new_write + "Добавить новое устройство:");
                            new_write = (new_write + "\n");
                            new_write = (new_write + "Версия Android: " + mass_versia[i]);
                            new_write = (new_write + "\n");
                            new_write = (new_write + "Код: " + Android_id.substring(0, Android_id.length() - 2));


                            Mail();
                            PrefActivity_Splash.sender_mail_async async_sending = new PrefActivity_Splash.sender_mail_async();
                            async_sending.execute();
                            Constanta_Write();
                        } else {
                            /*Log.e("Reg Входит", "Ошибка версии!");
                            button_regist.setSummary("Версия Android не подходит для работы; " + this_data_now);
}
                    }


                            } catch (Exception e) {
                            Toast.makeText(context_Activity, "Ошибка регистрации!", Toast.LENGTH_SHORT).show();
                            Log.e("PrefActivity_Spalsh", "Ошибка регистрации!");
                            }


                            return false;
                            }
                            });*/