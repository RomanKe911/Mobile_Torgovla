package kg.roman.Mobile_Torgovla.MT_MyClassSetting;


import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.TreeMap;

import kg.roman.Mobile_Torgovla.ImagePack.ImagePack_R_Simple;
import kg.roman.Mobile_Torgovla.ImagePack.ListAdapterSimple_Ftp_Image;


//public class FTPWebhost extends FtpConnection {
public class FTPWebhost {
    //// https://commons.apache.org/proper/commons-net/apidocs/org/apache/commons/net/SocketClient.html
    // Общие параметры подключения FTP
    public String ftp_server, ftp_user_name, ftp_password;
    public int ftp_port;
    // Параметры для получения даты
    public String put_toFiles, name_File, put_toFTP; // имя и путь к файлу

    // Параметры для получения размера каталога
    public String put_toCatalogFiles;

    SimpleDateFormat sdf1;
    FTPFile dataFile;
    long[] dirInfo_db3;

    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;


    // Получение списка фалов из папки сервера
    public void getListFiles() {
        try {
            FTPClient ftpClient = new FTPClient();
            FtpConnectData connectData = new FtpConnectData();
            ftpClient.connect(connectData.server_name, connectData.port);
            ftpClient.login(connectData.server_username, connectData.server_password);
            ftpClient.enterLocalPassiveMode();

            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    // получение даты файла
    public String getDataFile(String put_toFiles, String toNameFile) {
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(ftp_server, ftp_port);
            ftpClient.login(ftp_user_name, ftp_password);
            ftpClient.enterLocalPassiveMode();
            dataFile = ftpClient.mdtmFile(put_toFiles + name_File);
            sdf1 = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
            Log.e("Method and Description", "listDirectories()=" + ftpClient.listDirectories().toString());

            FTPFile[] ftpFiles_list_XML = ftpClient.listFiles(put_toFiles); // "/MT_Sunbell_Karakol/MTW_SOS/"
            for (FTPFile ftpFile_XML : ftpFiles_list_XML) {
                String file_server_xml = put_toFiles + ftpFile_XML.getName(); // путь на сервере
                if (ftpFile_XML.getName().length() > 3) {
                    String nameAgent = "bezmenova_natalija_petrovna.db3";
                    String name2 = ftpFile_XML.getName().substring(36);
                   /* Log.e("SOS", "Строка1:"+nameAgent);
                    Log.e("SOS", "Строка1:"+name2);*/
                    // 14:11:2022 09:25:53_sunbell_base_db_bezmenova_natalija_petrovna.db3
                    if (nameAgent.equals(ftpFile_XML.getName().substring(36))) {
                        Log.e("SOS", "Есть совпадение Наталия");
                        Log.e("SOS", "Строка1:" + nameAgent);
                        Log.e("SOS", "Строка1:" + name2);
                        Log.e("SOS", "Строка1:" + ftpFile_XML.getName().substring(0, 36));
                    }
                   /* else
                    {
                        Log.e("SOS", "совпадений нет");
                        Log.e("SOS", "Строка2:"+nameAgent);
                        Log.e("SOS", "Строка2:"+name2);
                    }*/
                }
                // else Log.e("Method and Description", "нет файлов=" + file_server_xml);

                //Log.e("Method and Description", "listFiles()=" + file_server_xml);


            }

         /*   Log.e("Новый файла...", "ДАТА1: " + dataFile);
            Log.e("Новый файла...", "Бишкек: " + sdf1.format(dataFile.getTimestamp().getTime()));*/


            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return sdf1.format(dataFile.getTimestamp().getTime());
    }


    public String getFilesSize(String put_toFilesFTP) {
        /*
         *  FTP-функция: для получение размера файлов из директории 'put_toFiles' (/MT_Sunbell_Karakol/Image)
         *  Пути и настройки находятся в классе FtpConnectData
         *  возвращает строку формата: файлов: ---, общий размер: ---
         * */
        StringBuilder stringBuilder = new StringBuilder();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    FTPClient ftpClient = new FTPClient();
                    FtpConnectData connectData = new FtpConnectData();
                    ftpClient.connect(connectData.server_name, connectData.port);
                    ftpClient.login(connectData.server_username, connectData.server_password);
                    ftpClient.enterLocalPassiveMode();

                    long[] dirInfo_db3 = calculateDirectoryInfo(ftpClient, put_toFilesFTP, "");
/*                    Log.e("Путь=", " = " + put_toFilesFTP);
                    Log.e("Дирикторий=", " = " + dirInfo_db3[0]);
                    Log.e("Файлов=", " = " + dirInfo_db3[1]);
                    Log.e("Размер!=", " = " + dirInfo_db3[2] + " байт");*/
                    //    Log.e("Файлов=", " = " + dirInfo_db3[1]);
                    if (dirInfo_db3[1] >= 1)
                        stringBuilder.append(getLongToDoubleFileSize(dirInfo_db3[2]));
                    else
                        stringBuilder.append("файлов: ").append(dirInfo_db3[1]).append(", обший размер: ").append(getLongToDoubleFileSize(dirInfo_db3[2]));

                    ftpClient.logout();
                    ftpClient.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
        try {
            Thread thread = new Thread(runnable);
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();

    }


    // получение расширение файлов
    public String getFileExtension(boolean b_locationFileServer) {
        // Путь к данным для скачивания; true - данные на сервере, false - данные на телефоне
        Map<String, Integer> extension = new HashMap<String, Integer>();
        String return_getFileExtension = "";
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(ftp_server, ftp_port);
            ftpClient.login(ftp_user_name, ftp_password);
            ftpClient.enterLocalPassiveMode();

            int i1 = 1, i2 = 1, i3 = 1, i4 = 1;

            if (b_locationFileServer) {
                FTPFile[] fileXML = ftpClient.listFiles(put_toFiles);
                String[] fileXMLName = ftpClient.listNames(put_toFiles);
                int putInt = put_toFiles.length();
                Log.e("Files", "кол-во файлов:" + fileXML.length);
                Log.e("Char", "символы: " + putInt);
                for (int i = 0; i < fileXML.length; i++) {
                    String mm = fileXMLName[i].substring(putInt + 1);
                    //  Log.e("Names", "имя:" + fileXMLName[i]);
                    Log.e("Names", "имя:" + mm);
                }
                for (FTPFile ftpFile_XML : fileXML) {
                    String extensionFile = ftpFile_XML.getName().substring(ftpFile_XML.getName().lastIndexOf('.'));
                    Log.e("Files", "расширение:" + extensionFile);
                    switch (extensionFile) {
                        case ".xml":
                            extension.put("XML", i1++);
                            break;
                        case ".db3":
                            extension.put("SQL", i2++);
                            break;
                        case ".png":
                            extension.put("PNG", i3++);
                            break;
                        case ".jpg":
                            extension.put("JPG", i4++);
                            break;
                        default:
                            break;
                    }
                }
            } else {
                File file = new File(put_toFiles);
                File fileList[] = file.listFiles();
                Log.e("Files", "кол-во файлов:" + fileList.length);
                for (File file_list : fileList) {
                    String extensionFile = file_list.getName().substring(file_list.getName().lastIndexOf('.'));
                    Log.e("Files", "расширение:" + extensionFile);
                    switch (extensionFile) {
                        case ".xml":
                            extension.put("XML", i1++);
                            break;
                        case ".db3":
                            extension.put("SQL", i2++);
                            break;
                        case ".png":
                            extension.put("PNG", i3++);
                            break;
                        case ".jpg":
                            extension.put("JPG", i4++);
                            break;
                        default:
                            break;
                    }
                }
            }
            Log.e("MAPA", "///// Список расширений:");
            for (Map.Entry<String, Integer> item : extension.entrySet()) {
                System.out.printf("Key: %s  Value: %s \n", item.getKey(), item.getValue());
                Log.e("Files", "ключ: " + item.getKey() + ", значение: " + item.getValue());
                return_getFileExtension += (item.getKey() + ": " + item.getValue() + " файлов\n");
            }
            Log.e("MAPA", "///// КОНЕЦ");
            ;
            // return_getFileExtension = "XML: "+extension.get("XML")+" файлов";
            //+extension.values()+" файлов";
            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return return_getFileExtension;
    }


    // Отправить файл(ы) на FTP
    public void getFileToFTP(String put_toFilesSTART, String put_toFilesEND, Boolean first_file) {
        Runnable runnable = () -> {
            try {
                FTPClient ftpClient = new FTPClient();
                FtpConnectData connectData = new FtpConnectData();
                ftpClient.connect(connectData.server_name, connectData.port);
                ftpClient.login(connectData.server_username, connectData.server_password);
                ftpClient.enterLocalPassiveMode();

                Log.e("FTP", "Путь старта: " + put_toFilesSTART);
                Log.e("FTP", "Путь конца: " + put_toFilesEND);

                if (first_file) {
                    FileInputStream in = new FileInputStream(new File(put_toFilesSTART));    // Путь к файлам для отправки
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    ftpClient.enterLocalPassiveMode();
                    ftpClient.storeFile(put_toFilesEND, in);                                 // Конечный путь куда отправить файлы
                } else {
                    File file = new File(put_toFilesSTART);
                    File fileList[] = file.listFiles();

                    for (File file_XML : fileList) {
                        Log.e("FTP", "FILE: " + file.listFiles().length);
                        Log.e("Files", "Файлы" + file_XML.getName());
                        String f = file_XML.getName();

                        FileInputStream in = new FileInputStream(new File(put_toFilesSTART + file_XML.getName()));  // Путь к файлам для отправки
                        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                        ftpClient.enterLocalPassiveMode();
                        ftpClient.storeFile(put_toFilesEND + file_XML.getName(), in);  // Конечный путь куда отправить файлы

                        Log.e("Files", "ftpClient1: " + ftpClient.listNames());
                        Log.e("Files", "ftpClient2: " + ftpClient.listFiles());
                        Log.e("Files", "ftpClient3: " + ftpClient.getConnectTimeout());
                        Log.e("Files", "ftpClient4: " + ftpClient.getLocalPort());
                        Log.e("Files", "ftpClient5: " + ftpClient.getRemotePort());
                        Log.e("Files", "ftpClient6: " + ftpClient.getSoTimeout());

                    }
                }





         /*   FTPFile[] ftpFiles_list_XML = ftpClient.listFiles(put_toFilesSTART);
            for (FTPFile ftpFile_XML : ftpFiles_list_XML) {
                Log.e("FTP", "Файлы" + ftpFile_XML.getName());

                String file_sFTP_xml = put_toFiles + ftpFile_XML.getName();                                                         // путь на сервере
                String file_Phone = Environment.getExternalStorageDirectory().toString() + "/Price/XML/" + ftpFile_XML.getName();
                ;   // путь к базам данных на телефоне
                String file_Phone2 = "/sdcard/Price/Image/" + ftpFile_XML.getName();                                                // путь к базам данных на телефоне
                String file_server_xml = put_toFiles + ftpFile_XML.getName();


                // String path_phobe_db = Environment.getExternalStorageDirectory().toString() + "/Price/" + PEREM_WORK_DISTR + "/" + a.fileName;
                // 22.10.2022  String path_phobe_db = "/sdcard/Price/" + PEREM_WORK_DISTR + "/" + a.fileName;
                //   String path_phobe_db = context.getDatabasePath(a.fileName).getAbsolutePath();        // путь базы данных на телефоне
                //   String path_phobe_db = context.getDatabasePath(a.fileName).getAbsolutePath();        // путь базы данных на телефоне
                //String path_server_db = a.remoteFilename;                                           // путь базы данных на FTP
                FileInputStream in = new FileInputStream(new File(put_toFilesSTART + ftpFile_XML.getName()));  // Путь к файлам для отправки
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                ftpClient.storeFile(put_toFilesEND + ftpFile_XML.getName(), in);  // Конечный путь куда отправить файлы


            }
*/
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException ex) {
                ex.printStackTrace();
                Log.e("FTP", "FTP: ftpWebhost; " + ex);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();


    }

    // Скачать файл(ы) с FTP
    public boolean getFileToPhone(String put_toFilesFTP, String put_toFilesPhone, Context context, Boolean first_file) {
        String logeTAG = "FTPtoPhone";
        Boolean multiFile = false;
        FTPClient ftpClient = new FTPClient();
        FtpConnectData connectData = new FtpConnectData();
        try {
            ftpClient.connect(connectData.server_name, connectData.port);
            ftpClient.login(connectData.server_username, connectData.server_password);
            ftpClient.enterLocalPassiveMode();
            Log.e(logeTAG, "Путь ftp-сервера: " + put_toFilesFTP);
            Log.e(logeTAG, "Путь на телефоне: " + put_toFilesPhone);

            if (first_file) {
                Log.e(logeTAG, "Скачивание одного файла....");
                OutputStream outputStream = new FileOutputStream(new File(put_toFilesPhone));  // путь куда сохранить данные
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                ftpClient.retrieveFile(put_toFilesFTP, outputStream);                          // путь откуда скачать данные
                outputStream.close();

            } else {
                Log.e("FTP", "Пакетное скачивание");
                FTPFile[] ftpFiles_list_XML = ftpClient.listFiles(put_toFilesFTP);
                for (FTPFile ftpFile_XML : ftpFiles_list_XML) {

                    String putFILES = Environment.MEDIA_MOUNTED;                             // путь: к файлам    (/data/user/0/kg.roman.Mobile_Torgovla/files)
                    String putDB = context.getDatabasePath(ftpFile_XML.getName()).getAbsolutePath();    // путь: к databases (/data/user/0/kg.roman.Mobile_Torgovla/databases/)

                    String file_sFTP_xml = put_toFiles + ftpFile_XML.getName();                                                         // путь на сервере
                    String file_Phone = Environment.getExternalStorageDirectory().toString() + "/Price/XML/";   // путь к базам данных на телефоне
                    String file_Phone2 = "/sdcard/Price/Image/" + ftpFile_XML.getName();                                                // путь к базам данных на телефоне
                    //   String file_server_xml = put_toFiles + ftpFile_XML.getName();
                    //    File file = new File(Environment.getExternalStorageDirectory().toString() + "/Price/XML/", ftpFile_XML.getName());
                    //File file = new File(getApplication().getDatabasePath("").getAbsolutePath());


                    String file_server_xml = put_toFilesFTP + "/" + ftpFile_XML.getName();
                    //  File file = new File(put_toFilesPhone, ftpFile_XML.getName());

                    File file = new File(context.getApplicationContext().getDatabasePath(ftpFile_XML.getName()).getAbsolutePath());

                    Log.e(logeTAG, "путь на FTP: " + file_server_xml);
                    Log.e(logeTAG, "путь на Phone: " + file.getPath());
                    if (ftpFile_XML.isFile()) {
                        // код для скачивания файла с FTP
                        //OutputStream outputStream = new FileOutputStream(new File(file_Phone+ ftpFile_XML.getName()));  // путь куда сохранить данные
                        OutputStream outputStream = new FileOutputStream(file.getPath());  // путь куда сохранить данные
                        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                        ftpClient.enterLocalPassiveMode();
                        // ftp: забрать файл из этой директ, в эту директ
                        ftpClient.retrieveFile(file_server_xml, outputStream);
                        outputStream.close();
                    }
                }
            }


            Log.e(logeTAG, "Скачивание окончено!");
            multiFile = true;
            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e(logeTAG, "ошибка скачивания файла, проверьте данные!");
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (first_file == true) {
            File file = new File(put_toFilesPhone);
            return file.isFile() & (file.length() > 0);
        } else
            return multiFile;

    }


    // Скачать файл(ы) с FTP
    public void getFile_FTPToPhone(String put_toFilesSTART, String put_toFilesEND, Context
            context, Boolean first_file) {
        try {
            FTPClient ftpClient = new FTPClient();
            FtpConnectData connectData = new FtpConnectData();
            ftpClient.connect(connectData.server_name, connectData.port);
            ftpClient.login(connectData.server_username, connectData.server_password);
            ftpClient.enterLocalPassiveMode();

            Log.e("FTP", "Путь старта: " + put_toFilesSTART);
            Log.e("FTP", "Путь конца: " + put_toFilesEND);

            if (first_file) {
                Log.e("FTP", "Скачивание одного файла ");
                OutputStream outputStream = new FileOutputStream(new File(put_toFilesEND));  // путь куда сохранить данные
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                ftpClient.retrieveFile(put_toFilesSTART, outputStream);                          // путь откуда скачать данные
                outputStream.close();

            } else {
                Log.e("FTP", "Пакетное скачивание");
                FTPFile[] ftpFiles_list_XML = ftpClient.listFiles(put_toFiles);
                for (FTPFile ftpFile_XML : ftpFiles_list_XML) {

                    String putFILES = Environment.MEDIA_MOUNTED;                             // путь: к файлам    (/data/user/0/kg.roman.Mobile_Torgovla/files)
                    File files = new File(context.getFilesDir().getAbsolutePath());                       // путь: к файлам    (/data/user/0/kg.roman.Mobile_Torgovla/files)
                    String putDB = context.getDatabasePath(ftpFile_XML.getName()).getAbsolutePath();    // путь: к databases (/data/user/0/kg.roman.Mobile_Torgovla/databases/)

                    String file_sFTP_xml = put_toFiles + ftpFile_XML.getName();                                                         // путь на сервере
                    String file_Phone = Environment.getExternalStorageDirectory().toString() + "/Price/XML/";   // путь к базам данных на телефоне
                    String file_Phone2 = "/sdcard/Price/Image/" + ftpFile_XML.getName();                                                // путь к базам данных на телефоне
                    String file_server_xml = put_toFiles + ftpFile_XML.getName();

                    File file = new File(Environment.getExternalStorageDirectory().toString() + "/Price/XML/", ftpFile_XML.getName());
                    if (ftpFile_XML.isFile()) {
                        // код для скачивания файла с FTP
                        //OutputStream outputStream = new FileOutputStream(new File(file_Phone+ ftpFile_XML.getName()));  // путь куда сохранить данные
                        OutputStream outputStream = new FileOutputStream(file);  // путь куда сохранить данные
                        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                        ftpClient.enterLocalPassiveMode();
                        // ftp: забрать файл из этой директ, в эту директ
                        ftpClient.retrieveFile(file_server_xml, outputStream);
                        outputStream.close();
                    }
                }
            }


            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    ///// Получение списка файлов рез. копирования по агенту и филиалу
    // public HashSet<String> getListFile_SOSDB(String nameAgent, String putFile) {
//    public Pair<ArrayList<Long>, ArrayList<String>> getListFile_BackUp(String nameAgent, String putFile) {
    public ArrayList<String> getListFile_BackUp(String nameAgent, String putFile) {
        ArrayList<String> arrayList_FileName = new ArrayList<>();
        ArrayList<Long> arrayList_FileData = new ArrayList<>();
        TreeMap<Long, String> treeMap = new TreeMap<>();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // Подключение к FTP
                    FTPClient ftpClient = new FTPClient();
                    FtpConnectData ftpConnectData = new FtpConnectData();
                    ftpClient.connect(ftpConnectData.server_name, ftpConnectData.port);
                    ftpClient.login(ftpConnectData.server_username, ftpConnectData.server_password);
                    ftpClient.enterLocalPassiveMode();
                    // Подключение к списку файлов на FTP


                    FTPFile[] ftpFiles_list_XML = ftpClient.listFiles(putFile);
                    for (FTPFile ftpFile_XML : ftpFiles_list_XML) {
                        if (ftpFile_XML.getName().length() > 3) {

                            String data = ftpFile_XML.getName().substring(0, 10);
                            String vrema = ftpFile_XML.getName().substring(11, 19);
                            String strBASEDB = ftpFile_XML.getName().substring(36);   // sunbell_base_db
                            String strCONSTDB = ftpFile_XML.getName().substring(37);  // sunbell_const_db
                            String strRNDB = ftpFile_XML.getName().substring(34);     // sunbell_rn_db
                            ftpFile_XML.getTimestamp().getTimeInMillis();

                            if (ftpFile_XML.getName().contains(nameAgent)) {
                                // Log.e("File: ", "File... "+ftpFile_XML.getName());
                                arrayList_FileName.add(ftpFile_XML.getName());
                                Log.e("FTP", ftpFile_XML.getName());
                               /* arrayList_FileData.add(ftpFile_XML.getTimestamp().getTimeInMillis());
                                treeMap.put(ftpFile_XML.getTimestamp().getTimeInMillis(), ftpFile_XML.getName());*/
                            }

                        }
                        // 01:11:2022 09:46:50_sunbell_base_db_sagitova_anna.db3
                        // 01:11:2022 09:46:50_sunbell_const_db_sagitova_anna.db3
                        // 01:11:2022 09:46:50_sunbell_rn_db_sagitova_anna.db3
                    }
                    ftpClient.logout();
                    ftpClient.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //     return new Pair<>(arrayList_FileData, arrayList_FileName);
        return arrayList_FileName;
    }


    // Конвертер строки даты и времени в формат записи
    protected String getData_Vrema(String w_data, String w_vrema) {
        DateFormat tt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        int v_sec, v_mm, v_hours;
        int d_day, d_month, d_year;
        // Дата 12:01:2023
        // Время 12:16:08
        v_sec = Integer.valueOf(w_vrema.substring(6));
        v_mm = Integer.valueOf(w_vrema.substring(3, 5));
        v_hours = Integer.valueOf(w_vrema.substring(0, 2));

        d_day = Integer.valueOf(w_data.substring(0, 2));
        d_month = Integer.valueOf(w_data.substring(3, 5)) - 1;
        d_year = Integer.valueOf(w_data.substring(6));
        calendar.set(d_year, d_month, d_day, v_hours, v_mm, v_sec);
        return tt.format(calendar.getTime());
    }

    //// Пример Java FTP - вычисление общего количества подкаталогов, файлов и размера каталога
    public static long[] calculateDirectoryInfo(FTPClient ftpClient, String parentDir, String
            currentDir) throws IOException {
        long[] info = new long[3];
        long totalSize = 0;
        int totalDirs = 0;
        int totalFiles = 0;

        String dirToList = parentDir;
        if (!currentDir.equals("")) {
            //   dirToList += "/" + currentDir;
            dirToList += currentDir;
            // Новинка
            FTPFile dataFile = ftpClient.mdtmFile(dirToList);
        }

        try {
            FTPFile[] subFiles = ftpClient.listFiles(dirToList);

            if (subFiles != null && subFiles.length > 0) {
                for (FTPFile aFile : subFiles) {
                    String currentFileName = aFile.getName();
                    if (currentFileName.equals(".") || currentFileName.equals("..")) {
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

    ////// Проверка соединения с FTP сервером, с возвращаемым типом <Строка состояния, логическое true или false>
    public Pair<String, Boolean> getFTP_TestConnect() {
        final boolean[] isConnectFtp = {false};
        final String logeTag = "FTP_TestConnect";
        final String[] isConnectFtpText = {""};
        Runnable ftpRun = new Runnable() {
            @Override
            public void run() {
                Log.e(logeTag, "Проверка соединения с сервером.......");
                FTPClient ftpClient = new FTPClient();
                FtpConnectData ftpConnectData = new FtpConnectData();
                try {
                    ftpClient.connect(ftpConnectData.server_name, ftpConnectData.port);
                    ftpClient.login(ftpConnectData.server_username, ftpConnectData.server_password);
                    int replyCode = ftpClient.getReplyCode();
                    boolean isConnect = false;
                    String isConnectText = "";
                    if (FTPReply.isPositiveCompletion(replyCode))
                        isConnect = true;
                    else
                        isConnect = false;

                    String[] replies = ftpClient.getReplyStrings();
                    if (replies != null && replies.length > 0) {
                        for (String aReply : replies) {
                            // Log.e("LIST", "SERVER: " + aReply);
                            isConnectText = "SERVER: " + aReply;
                        }
                    }
                    if (isConnect) {
                        isConnectFtp[0] = true;
                        isConnectFtpText[0] = "Удачное содиенение с FTP-сервером \n" + isConnectText;
                        Log.e(logeTag, "Удачное содиенение с FTP-сервером " + isConnectText);
                    } else {
                        isConnectFtp[0] = false;
                        isConnectFtpText[0] = "OOPS, ошибка подключения к FTP-серверу \n" + isConnectText;
                        Log.e(logeTag, "OOPS, ошибка подключения к FTP-серверу " + isConnectText);
                    }

                    // SERVER: 230 User sunbell_siberica logged in
                    // SERVER: 530 Login incorrect.

                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (ftpClient.isConnected()) {
                            ftpClient.logout();
                            ftpClient.disconnect();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        Thread tr = new Thread(ftpRun);
        tr.start();
        try {
            tr.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        return new Pair(isConnectFtpText[0], isConnectFtp[0]);
    }

    //////////// Тест на подключение к интернету
    public boolean getInternetConnect(Context context) {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null)
            return false;
        else
            return true;
    }


    /// Получение данных о не существующих фалов
    public List<String> getFileNotImageFTP() {
        List<String> listFilesNoPhone = new ArrayList<>();
        listFilesNoPhone.clear();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                FTPClient ftpClient = new FTPClient();
                try {

                    ftpClient.connect(ftp_server, ftp_port);
                    ftpClient.login(ftp_user_name, ftp_password);
                    ftpClient.enterLocalPassiveMode();
                    // Unable to decode stream: java.io.FileNotFoundException: content:/com.sec.android.app.myfiles.FileProvider/device_storage/Android/data/kg.roman.Mobile_Torgovla/files/bx_bx000007.png (No such file or directory)

                    ////////////////////////////////////////////////////////////////////////////
                    // Путь у файлам Image FTP Пример строки:(/MT_Sunbell_Karakol/Image/)
                    FTPFile[] fileXMLName = ftpClient.listFiles(put_toFTP);
                    // Создание Множества, списка файлов
                    Map<Integer, String> ListSetFtp = new HashMap<>();
                    Log.e("LISTFTP", "Файлы: " + fileXMLName.length);

                    if (fileXMLName.length > 0) // проверка на пустой
                    {
                        for (int i = 0; i < fileXMLName.length; i++) {
                            if (fileXMLName[i].getType() == 0)
                                ListSetFtp.put(i, fileXMLName[i].getName());
                            Log.e("LIST_FTP: ", "Файлы на ftp: " + fileXMLName[i].getName());
                        }
                    } else {
                        ListSetFtp.clear();
                        Log.e("LIST_FTP_NO: ", "Нет файлов на сервере!");
                    }
                    ////////////////////////////////////////////////////////////////////////////


                    ////////////////////////////////////////////////////////////////////////////
                    // Путь у файлам Image Phone
                    String file_Phone = Environment.getExternalStorageDirectory().toString() + "/Price/XML/";
                    // File file = new File(file_Phone);
                    File file = new File(put_toFiles);
                    // Создание Множества, списка файлов
                    Map<Integer, String> ListSetPhone = new HashMap<>();
                    if (file.exists()) {
                        for (int i = 0; i < file.list().length; i++) {
                            ListSetPhone.put(i, file.list()[i]);
                            Log.e("LIST_PHONE: ", "Файлы на телефоне: " + file.list()[i]);
                        }
                    } else {
                        ListSetPhone.clear();
                        Log.e("LIST_PHONE_NO: ", "На телефоне нет файлов");
                    }
                    ////////////////////////////////////////////////////////////////////////////


                    if (!ListSetFtp.isEmpty()) {
                        if (!ListSetPhone.isEmpty()) {
                            for (Map.Entry<Integer, String> item : ListSetFtp.entrySet()) {
                                if (!ListSetPhone.containsValue(item.getValue())) {
                                    listFilesNoPhone.add(item.getValue());
                                    Log.e("LIST_LOAD: ", item.getValue());
                                }
                            }
                            if (listFilesNoPhone.isEmpty())
                                listFilesNoPhone.add("нет новых картинок");
                        } else {
                            for (Map.Entry<Integer, String> item : ListSetFtp.entrySet()) {
                                listFilesNoPhone.add(item.getValue());
                                Log.e("LIST_LOAD: ", item.getValue());
                            }
                        }
                    } else {
                        listFilesNoPhone.clear();
                        listFilesNoPhone.add("нет файлов на сервере, проверьте соединение или обратитесь к специалисту!");
                        Log.e("LISTSFTP_NULL", "нет файлов на сервере, проверьте соединение или обратитесь к специалисту!");
                    }



                  /*  if (ListSetPhone.isEmpty()) {
                        Log.e("LISTSPHONE_NULL", "нет файлов на телефоне");
                    }


                    // Получение списка файлов которых нет на телефоне
                    Log.e("LISTFTP", "список FTP");
                    if (!ListSetFtp.isEmpty() && !ListSetPhone.isEmpty()) {

                        for (Map.Entry<Integer, String> item : ListSetFtp.entrySet()) {
                            if (!ListSetPhone.containsValue(item.getValue())) {
                                listFilesNoPhone.add(item.getValue());
                                Log.e("LIST_LOAD: ", item.getValue());
                            }
                        }
                        if (listFilesNoPhone.isEmpty()) {
                            listFilesNoPhone.clear();
                            listFilesNoPhone.add("нет новых картинок");
                            Log.e("LISTSNO", "нет новых картинок");
                        }
                    } else {
                        listFilesNoPhone.clear();
                        listFilesNoPhone.add("Все картинки заполнены или нет доступа к ресурсам!");
                        Log.e("LIST", "Нет данных для сравнения");
                    }

                    */


                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (ftpClient.isConnected()) {
                            ftpClient.logout();
                            ftpClient.disconnect();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        Log.e("ERROR", "Ошибка данных");
                    }
                }
            }
        };

        Thread getFileNoImage = new Thread(runnable);
        getFileNoImage.start();
        try {
            getFileNoImage.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("ERROR", "Ошибка потока");
        }
        return listFilesNoPhone;
    }

    public boolean getExistFileToFTP(String fileName) {
        final boolean[] isFile = {false};
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                FTPClient ftpClient = new FTPClient();
                FtpConnectData connectData = new FtpConnectData();
                try {
                    ftpClient.connect(connectData.server_name, connectData.port);
                    ftpClient.login(connectData.server_username, connectData.server_password);
                    ftpClient.enterLocalPassiveMode();
                    InputStream inputStream = ftpClient.retrieveFileStream(fileName);
                    int returnCode = ftpClient.getReplyCode();
                    if (inputStream != null || returnCode != 550) {
                        Log.e("FTPWEB", "файл существует");
                        isFile[0] = true;
                    } else {
                        Log.e("FTPWEB", "файл не существует");
                        isFile[0] = false;
                    }
                } catch (Exception e) {
                    Log.e("getExistFileToFTP", "Runn Error: " + e);
                } finally {
                    try {
                        if (ftpClient.isConnected()) {
                            ftpClient.logout();
                            ftpClient.disconnect();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };

        Thread getFileNoImage = new Thread(runnable);
        getFileNoImage.start();
        return isFile[0];
    }


    /*
            Log.e("FTPWEB", "geMake:" + ftpClient.makeDirectory(fileName));

            FTPFile[] fileXMLName3 = ftpClient.listFiles("/MT_Sunbell_Karakol/03_WDay/");
            for (FTPFile d : fileXMLName3) {
                Log.e("FTPWEB", "getExistFileToFTPWork:" + d.getName());
                if (d.getName().equals(fileName)) {
                    Log.e("FTPWEB", "getExistFileToFTPWork: есть совападения" + d.getName());
                    Log.e("FTPWEB", "getExistFileToFTPWork: есть совападения" + fileName);
                }

            }

            FTPFile fileXMLName = ftpClient.mdtmFile(fileName);
            Log.e("FTPWEB", "getExistFileToFTPmdtm: " + fileXMLName.getName());
            Log.e("FTPWEB", "getExistFileToFTPFile: " + fileXMLName.isFile());
            Log.e("FTPWEB", "getExistFileToFTPDirt: " + fileXMLName.isDirectory());





            String fileXMLName2 = ftpClient.mdtmFile(fileName).getName();
            Log.e("FTPWEB", "getExistFileToFTP2: " + fileXMLName2);

            if (fileName.equals(fileXMLName2)) {
                Log.e("FTPWEB", "getExistFileToFTP2: есть совпадения");
            } else
                Log.e("FTPWEB", "getExistFileToFTP2: нет совпадения");

*/

    //// Скачивание файлов которых нет в дириктории
    public boolean getLoadingFileImage(String put_toFilesSTART, String
            put_toFilesEND, List<String> w_list, ProgressBar horiz) {
        //put_toFilesSTART - путь откуда скачивать файл
        // put_toFilesEND - путь конечной директории
        // w_list - список файлов для скачивания
        // progressBar - активированный progressBar для отображения процесса

        final int[] k = {0};
        final double[] prmin = {0};
        final double prmax = w_list.size();
        final double pr = 100 / prmax;
        final boolean encRunnable[] = {false};
        // final Handler handler;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                FTPClient ftpClient = new FTPClient();
                FtpConnectData ftpConnectData = new FtpConnectData();
                try {
                    ftpClient.connect(ftpConnectData.server_name, ftpConnectData.port);
                    ftpClient.login(ftpConnectData.server_username, ftpConnectData.server_password);
                    ftpClient.enterLocalPassiveMode();
                    Log.e("FTP", "Пакетное скачивание");
                    Log.e("LIST: ", "Start: " + put_toFilesSTART);
                    Log.e("LIST: ", "END: " + put_toFilesEND);
                    k[0] = 0;
                    while (k[0] < w_list.size()) {
                        prmin[0] = +prmin[0] + pr;
                        Log.e("LIST_Yes: ", "Files: " + w_list.get(k[0]));
                        Log.e("FTP", "Скачивание одного файла ");
                        // OutputStream outputStream = new FileOutputStream(new File(put_toFilesEND + "/Image/" + w_list.get(k)));  // путь к папке files приложения
                        OutputStream outputStream = new FileOutputStream(new File(put_toFilesEND + w_list.get(k[0])));  // путь к папке files приложения
                        // OutputStream outputStream = new FileOutputStream(new File(put_toFilesEND + w_list.get(k)));  // путь к папке files приложения
                        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                        ftpClient.enterLocalPassiveMode();
                        ftpClient.retrieveFile(put_toFilesSTART + "/" + w_list.get(k[0]), outputStream);
                        outputStream.close();
                        k[0]++;
                    }
                    Log.e("FTP", "Пакетное скачивание закончилось");

                    Log.e("BOOLEAN: ", "TRUE");
                    encRunnable[0] = true;


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("ERROR: ", "Ошибка");
                } finally {
                    try {
                        if (ftpClient.isConnected()) {
                            ftpClient.logout();
                            ftpClient.disconnect();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }

        };
        Thread tr = new Thread(runnable);
        tr.start();

      /* try {
            tr.join();


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
             horiz.post(new Runnable() {
                                public void run() {
                                    Log.e("Progress: ", "progress: GONE" + prmin[0]);
                                    horiz.setVisibility(View.GONE);
                                }
                            });
        }*/


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (encRunnable[0]) {
                    horiz.setVisibility(View.VISIBLE);
                    Log.e("Hanler", "VISIBLE");
                    encRunnable[0] = true;
                } else {
                    horiz.setVisibility(View.INVISIBLE);
                    Log.e("Hanler", "INVISIBLE");
                    encRunnable[0] = false;
                }
            }
        }, 1000);


        return encRunnable[0];
    }

    public ArrayList<ListAdapterSimple_Ftp_Image> getListCatalogy() {
        ArrayList<ListAdapterSimple_Ftp_Image> ftp_image = new ArrayList<ListAdapterSimple_Ftp_Image>();

        Runnable ftpRun = new Runnable() {
            @Override
            public void run() {

                String w_brends = "", w_fileImage, w_dataUpdate = "";
                Long w_fileSize = 0L;
                int w_fileCount = 0;
                FTPClient ftpClient = new FTPClient();

                try {
                    ftpClient.connect(ftp_server, ftp_port);
                    ftpClient.login(ftp_user_name, ftp_password);
                    ftpClient.enterLocalPassiveMode();
                    FTPFile[] fileList = ftpClient.listFiles(put_toFiles);

                    for (FTPFile file : fileList) {
                        if (file.isDirectory()) {
                            if (file.getName().length() > 3) {
                                w_brends = file.getName();
                                w_dataUpdate = file.getTimestamp().getTime().toString();
                                w_fileCount = 0;
                                w_fileSize = 0L;
                                FTPFile[] fileListBrends = ftpClient.listFiles(put_toFiles + "/" + w_brends);
                                for (FTPFile filesCount : fileListBrends) {
                                    if (filesCount.isFile()) {
                                        w_fileCount++;
                                        w_fileSize = w_fileSize + filesCount.getSize();

                                    }
                                }
                                Log.e("Files: ", "Бренд> " + w_brends);
                                Log.e("Files: ", "Дата обновления> " + w_dataUpdate);
                                Log.e("Files: ", "Кол-во> " + w_fileCount);
                                Log.e("Files: ", "Размер> " + getLongToDoubleFileSize(w_fileSize));
                                Log.e("Files: ", "Картинка> " + "logo_" + w_brends.toLowerCase() + ".png");
                                if (w_brends.equals("Icons")) {
                                    w_brends = "Icons";
                                }
                                Random rd = new Random(); // creating Random object
                                ftp_image.add(new ListAdapterSimple_Ftp_Image(w_brends, "logo_" + w_brends.toLowerCase() + ".png", getLongToDoubleFileSize(w_fileSize), "" + w_fileCount + "", w_dataUpdate, rd.nextBoolean()));
                            }

                        }
                    }


                } catch (IOException ex) {
                    ex.printStackTrace();
                    Log.e("ERROR: ", "Ошибка" + ex);
                } finally {
                    try {
                        if (ftpClient.isConnected()) {
                            ftpClient.logout();
                            ftpClient.disconnect();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        Log.e("ERROR: ", "Ошибка закрытия FTP" + ex);
                    }
                }
            }
        };
        Thread tr = new Thread(ftpRun);
        tr.start();
        try {
            tr.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        return ftp_image;
    }

    public ArrayList<ImagePack_R_Simple> getListCatalogyImage() {
        ArrayList<ImagePack_R_Simple> ftp_image_new = new ArrayList<ImagePack_R_Simple>();
        Runnable ftpRun = new Runnable() {
            @Override
            public void run() {

                String w_brends = "", w_fileImage, w_dataUpdate = "";
                Long w_fileSize = 0L;
                int w_fileCount = 0;
                FTPClient ftpClient = new FTPClient();
                try {
                    ftpClient.connect(ftp_server, ftp_port);
                    ftpClient.login(ftp_user_name, ftp_password);
                    ftpClient.enterLocalPassiveMode();
                    FTPFile[] fileList = ftpClient.listFiles(put_toFiles);

                    for (FTPFile file : fileList) {
                        if (file.isDirectory()) {
                            if (file.getName().length() > 3) {
                                w_brends = file.getName();
                               /* w_dataUpdate = file.getTimestamp().getTime().toString();
                                Log.e("TIME DATE","1"+file.getTimestamp().getTime() );
                                Log.e("TIME DATE", "2"+file.getTimestamp().getTimeInMillis() );
                                Log.e("TIME DATE", "3"+getFullTime(file.getTimestamp().getTimeInMillis()));*/
                                w_dataUpdate = getFullTime(file.getTimestamp().getTimeInMillis());

                                w_fileCount = 0;
                                w_fileSize = 0L;
                                FTPFile[] fileListBrends = ftpClient.listFiles(put_toFiles + "/" + w_brends);
                                for (FTPFile filesCount : fileListBrends) {
                                    if (filesCount.isFile()) {
                                        w_fileCount++;
                                        w_fileSize = w_fileSize + filesCount.getSize();

                                    }
                                }
                                Log.e("Files: ", "Бренд> " + w_brends);
                                Log.e("Files: ", "Дата обновления> " + w_dataUpdate);
                                Log.e("Files: ", "Кол-во> " + w_fileCount);
                                Log.e("Files: ", "Размер> " + getLongToDoubleFileSize(w_fileSize));
                                Log.e("Files: ", "Картинка> " + "logo_" + w_brends.toLowerCase() + ".png");
                                if (w_brends.equals("Icons")) {
                                    w_brends = "Icons";
                                }
                                Random rd = new Random(); // creating Random object
                                Random click = new Random(); // creating Random object
                                ftp_image_new.add(new ImagePack_R_Simple(
                                        w_brends,
                                        "logo_" + w_brends.toLowerCase() + ".png",
                                        getLongToDoubleFileSize(w_fileSize),
                                        w_fileCount,
                                        0L,
                                        file.getTimestamp().getTimeInMillis(),
                                        click.nextBoolean()));
                            }

                        }
                    }
                } catch (UnknownHostException es) {
                    es.printStackTrace();
                    Log.e("ERROR: ", es + "// Нет подключения к интернету");
                    ftp_image_new.clear();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Log.e("ERROR: ", "Ошибка" + ex);
                } finally {
                    try {
                        if (ftpClient.isConnected()) {
                            ftpClient.logout();
                            ftpClient.disconnect();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        Log.e("ERROR: ", "Ошибка закрытия FTP" + ex);
                    }
                }
            }
        };
        Thread tr = new Thread(ftpRun);
        tr.start();
        try {
            tr.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }


        return ftp_image_new;
    }


    ///////////////------  Получение списка префикос с FTP для сравнения
    public HashSet<String> getListBrendImage(String brend) {
        HashSet<String> brends = new HashSet<String>();
        Runnable ftpRun = new Runnable() {
            @Override
            public void run() {

                String w_brends = "", w_fileImage, w_dataUpdate = "";
                Long w_fileSize = 0L;
                int w_fileCount = 0;
                FTPClient ftpClient = new FTPClient();
                FtpConnectData connectData = new FtpConnectData();

                try {
                    ftpClient.connect(connectData.server_name, connectData.port);
                    ftpClient.login(connectData.server_username, connectData.server_password);
                    ftpClient.enterLocalPassiveMode();
                    FTPFile[] fileList = ftpClient.listFiles(connectData.put_toFtpImageSunbell + "/" + brend + "/");
                    for (FTPFile file : fileList) {
                        if (file.getName().length() > 3)
                            brends.add(file.getName());
                    }
                } catch (UnknownHostException es) {
                    es.printStackTrace();
                    Log.e("ERROR: ", es + "// Нет подключения к интернету");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Log.e("ERROR: ", "Ошибка" + ex);
                } finally {
                    try {
                        if (ftpClient.isConnected()) {
                            ftpClient.logout();
                            ftpClient.disconnect();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        Log.e("ERROR: ", "Ошибка закрытия FTP" + ex);
                    }
                }
            }
        };
        Thread tr = new Thread(ftpRun);
        tr.start();
        try {
            tr.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        return brends;
    }


    public HashMap<String, Long> getListBrendImage_Size(String brend, Context context) {
        HashMap<String, Long> brends = new HashMap<String, Long>();
        Runnable ftpRun = new Runnable() {
            @Override
            public void run() {

                String w_brends = "", w_fileImage, w_dataUpdate = "";
                Long w_fileSize = 0L;
                int w_fileCount = 0;
                FTPClient ftpClient = new FTPClient();
                FtpConnectData connectData = new FtpConnectData();

                try {
                    ftpClient.connect(connectData.server_name, connectData.port);
                    ftpClient.login(connectData.server_username, connectData.server_password);
                    ftpClient.enterLocalPassiveMode();
                    FTPFile[] fileList = ftpClient.listFiles(connectData.put_toFTPforRegions(context) + "/" + brend + "/");
                    for (FTPFile file : fileList) {
                        if (file.getName().length() > 3)
                            brends.put(file.getName(), file.getTimestamp().getTimeInMillis() + connectData.ftp_timezone);
                    }
                } catch (UnknownHostException es) {
                    es.printStackTrace();
                    Log.e("ERROR: ", es + "// Нет подключения к интернету");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Log.e("ERROR: ", "Ошибка" + ex);
                } finally {
                    try {
                        if (ftpClient.isConnected()) {
                            ftpClient.logout();
                            ftpClient.disconnect();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        Log.e("ERROR: ", "Ошибка закрытия FTP" + ex);
                    }
                }
            }
        };
        Thread tr = new Thread(ftpRun);
        tr.start();
        try {
            tr.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        return brends;
    }


    private String getFullTime(long timeInMillis) {
        final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        // final Calendar c = Calendar.getInstance();
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeInMillis);
        c.setTimeZone(TimeZone.getDefault());
        //Log.e("TimeZone: ", format.format(c.getTime()));
        return format.format(c.getTime());
    }


    private void getTest() {
        Runnable ftpRun = new Runnable() {
            @Override
            public void run() {
                FTPClient ftpClient = new FTPClient();
                try {
                    ftpClient.connect(ftp_server, ftp_port);
                    ftpClient.login(ftp_user_name, ftp_password);
                    ftpClient.enterLocalPassiveMode();

                    String str = "/MT_Sunbell_Karakol/Image/";
                    String str2 = "/MT_Sunbell_Karakol/Image/";
                    String[] fileXMLName2 = ftpClient.listNames(str);
                    FTPFile[] fileXMLName3 = ftpClient.listFiles(put_toFiles);


                   /* for (int i = 0; i < fileXMLName2.length; i++) {
                        Log.e("LISTFTP", "список: " + fileXMLName2[i]);
                    }*/
                    Log.e("Messege", "ТЕСТ2");
                    for (int i = 0; i < fileXMLName3.length; i++) {
                      /*  Log.e("LISTFTP[]", "список1: " + fileXMLName3[i].getName());  // вывод имени файла с расширением
                        Log.e("LISTFTP[]", "список2: " + fileXMLName3[i].getLink());
                        Log.e("LISTFTP[]", "список3: " + fileXMLName3[i].getGroup());
                        Log.e("LISTFTP[]", "список4: " + fileXMLName3[i].getRawListing());
                        Log.e("LISTFTP[]", "список5: " + fileXMLName3[i].getSize());               // вывод размера файла в байтах
                        Log.e("LISTFTP[]", "список6: " + fileXMLName3[i].getTimestamp().getTime()); // вывод даты и времени создания файла
                        Log.e("LISTFTP[]", "список6_1: " + fileXMLName3[i].getTimestamp().get(Calendar.DAY_OF_MONTH)); // вывод даты и времени создания файла
                        Log.e("LISTFTP[]", "список6_1: " + fileXMLName3[i].getTimestamp().get(Calendar.DATE)); // вывод даты и времени создания файла
                        Log.e("LISTFTP[]", "список6_1: " + fileXMLName3[i].getTimestamp().get(Calendar.HOUR_OF_DAY)); // вывод даты и времени создания файла
                        Log.e("LISTFTP[]", "список7: " + fileXMLName3[i].getType());  // 0-файл, 1-каталог директрии
                        Log.e("LISTFTP[]", "список8: " + fileXMLName3[i].getUser());
                        Log.e("LISTFTP[]", "список9: " + fileXMLName3[i].getHardLinkCount());
                        Log.e("LISTFTP[]", "списокEND: " + fileXMLName3);

                        if (fileXMLName3[i].getType() != 1) {
                            Log.e("LISTFTP[]", "Файл");
                        } else Log.e("LISTFTP[]", "не файл");
                        */

                        Log.e("FTPGeter", "_" + fileXMLName3[i].getRawListing());


                        if (fileXMLName3[i].isDirectory() && fileXMLName3[i].getName().length() > 3) {
                            Log.e("FTPD", "Каталог: " + "/////////");
                            Log.e("FTPD", "Каталог: " + fileXMLName3[i].getName());
                            if (fileXMLName3[i].isUnknown()) Log.e("FTPD", "Unknow: true");
                            if (fileXMLName3[i].isValid()) Log.e("FTPD", "Valid: true");
                            if (fileXMLName3[i].isSymbolicLink()) Log.e("FTPD", "Symb: true");
                            if (fileXMLName3[i].isDirectory()) Log.e("FTPD", "Dirk: true");
                            if (fileXMLName3[i].isFile()) Log.e("FTPD", "File: true");
                            Log.e("FTPD", "Каталог: " + "/////////\n");
                        }
                        /*Log.e("FTP", "КаталогBoolean: " + fileXMLName3[i].isDirectory());
                        Log.e("FTP", "КаталогType: " + fileXMLName3[i].DIRECTORY_TYPE);
                        if (fileXMLName3[i].getType() == 1) {
                            Log.e("FTP", "Каталог: " + fileXMLName3[i].getName());
                        }*/
                    }

                    Log.e("FTP: ", "Список каталогов" + ftpClient.listDirectories());
                    Log.e("FTP: ", "Список файлов в каталогах" + ftpClient.listFiles());


                    //FTPFile[] files = ftpClient.listFiles();

// iterates over the files and prints details for each
                    DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                   /* FTPFile[] files = ftpClient.listDirectories("/MT_Sunbell_Karakol/Image");
                    for (FTPFile file : files) {
                        String details = file.getName();
                        if (file.isDirectory()) {
                            details = "[" + details + "]";
                        }
                        details += "\t\t" + file.getSize();
                        details += "\t\t" + dateFormater.format(file.getTimestamp().getTime());
                        System.out.println(details);
                        Log.e("FTP: ", "Пример" + details);
                    }*/


                    FTPFile[] filesCat = ftpClient.listDirectories("/MT_Sunbell_Karakol/Image");
                    for (FTPFile files : filesCat) {
                        String details = files.getName();
                        if (files.isDirectory()) {
                            details = "[" + details + "]";
                            if (details.length() > 4)
                                Log.e("FTP: ", "Каталог: " + details);
                        }
                    }

                    String remoteFilePath = "/MT_Sunbell_Karakol/Image/Firm_Tradegof/TZMO";
                    FTPFile ftpFile = ftpClient.mlistFile(remoteFilePath);
                    if (ftpFile != null) {
                        String name = ftpFile.getName();

                        DateFormat df_data = new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat df_vrema = new SimpleDateFormat("HH:mm:ss");
                        String this_rn_data = df_data.format(ftpFile.getTimestamp().getTime());
                        String this_rn_vrema = df_vrema.format(ftpFile.getTimestamp().getTime());


                        Timestamp timestamps2 = new Timestamp(System.currentTimeMillis());
                        Long this_rn_stamp = ftpFile.getTimestamp().getTime().getTime();
                        Log.e("FTP", "time: " + timestamps2.getTime());

                        Log.e("FTPDataFRP: ", "...: " + this_rn_data + "__" + this_rn_vrema + "--" + this_rn_stamp);

                        long size = ftpFile.getSize();
                        String timestamp = ftpFile.getTimestamp().getTime().toString();
                        int timestamp2 = ftpFile.getTimestamp().get(Calendar.DATE);
                        int data = ftpFile.getTimestamp().getTime().getDate();
                        int vrema = ftpFile.getTimestamp().get(Calendar.MONTH);
                        String type = ftpFile.isDirectory() ? "Directory" : "File";

                       /* System.out.println("Name: " + name);
                        System.out.println("Size: " + size);
                        System.out.println("Type: " + type);
                        System.out.println("Timestamp: " + timestamp);*/
                        Log.e("FTPData: ", "...: " + timestamp2);
                        Log.e("FTPData: ", "...: " + data + "_" + vrema);
                        Log.e("FTP: ", "...: " + name + "_" + size + "_" + type + "_" + timestamp);
                        Log.e("FTP_Data: ", "...: " + timestamp);
                    } else {
                        Log.e("FTP: ", "The specified file/directory may not exist!");
                    }



                  /*
                    String[] files1 = ftpClient.listNames();
                    if (files != null && files1.length > 0) {
                        for (String aFile: files1) {
                            Log.e("FTP: ", "Пример1" + aFile);
                        }
                    }*/


                } catch (IOException ex) {
                    ex.printStackTrace();
                    Log.e("ERROR: ", "Ошибка" + ex);
                } finally {
                    try {
                        if (ftpClient.isConnected()) {
                            ftpClient.logout();
                            ftpClient.disconnect();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        Thread tr = new Thread(ftpRun);
        tr.start();
    }


    ////////// функция работы с данными размера файлов
    protected String getLongToDoubleFileSize(Long w_long) {
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

}



 /*Log.e("Messege", "ТЕСТ1");
         String str = "/MT_Sunbell_Karakol/Image/Barhim/";
         String str2 = "/MT_Sunbell_Karakol/Image/Barhim/";
         String[] fileXMLName2 = ftpClient.listNames(str);
         FTPFile[] fileXMLName3 = ftpClient.listFiles(str2);



         for (int i = 0; i < fileXMLName2.length; i++) {
                Log.e("LISTFTP", "список: " + fileXMLName2[i]);
            }
            Log.e("Messege", "ТЕСТ2");
            for (int i = 0; i < fileXMLName3.length; i++) {
                Log.e("LISTFTP[]", "список1: " + fileXMLName3[i].getName());  // вывод имени файла с расширением
                Log.e("LISTFTP[]", "список2: " + fileXMLName3[i].getLink());
                Log.e("LISTFTP[]", "список3: " + fileXMLName3[i].getGroup());
                Log.e("LISTFTP[]", "список4: " + fileXMLName3[i].getRawListing());
                Log.e("LISTFTP[]", "список5: " + fileXMLName3[i].getSize());               // вывод размера файла в байтах
                Log.e("LISTFTP[]", "список6: " + fileXMLName3[i].getTimestamp().getTime()); // вывод даты и времени создания файла
                Log.e("LISTFTP[]", "список6_1: " + fileXMLName3[i].getTimestamp().get(Calendar.DAY_OF_MONTH)); // вывод даты и времени создания файла
                Log.e("LISTFTP[]", "список6_1: " + fileXMLName3[i].getTimestamp().get(Calendar.DATE)); // вывод даты и времени создания файла
                Log.e("LISTFTP[]", "список6_1: " + fileXMLName3[i].getTimestamp().get(Calendar.HOUR_OF_DAY)); // вывод даты и времени создания файла
                Log.e("LISTFTP[]", "список7: " + fileXMLName3[i].getType());  // 0-файл, 1-каталог директрии
                Log.e("LISTFTP[]", "список8: " + fileXMLName3[i].getUser());
                Log.e("LISTFTP[]", "список9: " + fileXMLName3[i].getHardLinkCount());
                Log.e("LISTFTP[]", "списокEND: " + fileXMLName3);

                if (fileXMLName3[i].getType() != 1) {
                    Log.e("LISTFTP[]", "Файл");
                } else Log.e("LISTFTP[]", "не файл");
            }*/






/*
            // Log.e("LISTFTP", "список FTP");
            for (int i = 0; i < fileXMLName.length; i++) {
                Log.e("LISTFTP", "список: " + fileXMLName[i].substring(kj + 1));
                //  listftp.add(fileXMLName[i].substring(kj + 1));
                ListSetFtp.put(i, fileXMLName[i].substring(kj + 1));
            }
            for (int i = 0; i < str.length; i++) {
                Log.e("LISTPHONE", "список: " + str[i]);
                //  listphone.add(str[i]);
                ListSetPhone.put(i, str[i]);
            }*/
//  Log.e("LISTFTP__", "_" + );
// List<String> listftp = new ArrayList<>();
// List<String> listphone = new ArrayList<>();

            /*Log.e("LISTPHONE", "список PHONE");
            for (int i=0; i<ListSetPhone.size(); i++)
            {
                if (!ListSetFtp.containsValue(ListSetPhone.get(i)))
                {
                   // System.out.println("_"+ListSetPhone.get(i));
                    Log.e("LISTPHONE__", "_"+ListSetPhone.get(i));
                }// else Log.e("LISTPHONENo__", "_"+ListSetPhone.get(i));
            }*/



/*
    public void getLoadingFileImage(String put_toFilesSTART, String put_toFilesEND, List<String> w_list, ProgressBar horiz) {

        final Handler h;
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    // устанавливаем подключение
                    h.sendEmptyMessage(STATUS_CONNECTING);
                    TimeUnit.SECONDS.sleep(2);

                    // установлено
                    h.sendEmptyMessage(STATUS_CONNECTED);

                    // выполняется какая-то работа
                    TimeUnit.SECONDS.sleep(3);

                    // разрываем подключение
                    h.sendEmptyMessage(STATUS_NONE);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

        */
/*
 * put_toFilesSTART - путь откуда скачивать файл
 * put_toFilesEND - путь конечной директории
 * w_list - список файлов для скачивания
 * progressBar - активированный progressBar для отображения процесса
 * *//*

        final int[] k = {0};
        final double[] prmin = {0};
        final double prmax = w_list.size();
        final double pr = 100 / prmax;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                h.sendEmptyMessage(STATUS_CONNECTING);
                FTPClient ftpClient = new FTPClient();
                FtpConnectData ftpConnectData = new FtpConnectData();
                try {
                    ftpClient.connect(ftpConnectData.server_name, ftpConnectData.port);
                    ftpClient.login(ftpConnectData.server_username, ftpConnectData.server_password);
                    ftpClient.enterLocalPassiveMode();
                    Log.e("FTP", "Пакетное скачивание");
                    Log.e("LIST: ", "Start: " + put_toFilesSTART);
                    Log.e("LIST: ", "END: " + put_toFilesEND);
                    k[0] = 0;
                    while (k[0] < w_list.size()) {
                        prmin[0] = +prmin[0] + pr;
                        Log.e("LIST_Yes: ", "Files: " + w_list.get(k[0]));
                        Log.e("FTP", "Скачивание одного файла ");
                        // OutputStream outputStream = new FileOutputStream(new File(put_toFilesEND + "/Image/" + w_list.get(k)));  // путь к папке files приложения
                        OutputStream outputStream = new FileOutputStream(new File(put_toFilesEND + w_list.get(k[0])));  // путь к папке files приложения
                        // OutputStream outputStream = new FileOutputStream(new File(put_toFilesEND + w_list.get(k)));  // путь к папке files приложения
                        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                        ftpClient.enterLocalPassiveMode();
                        ftpClient.retrieveFile(put_toFilesSTART + "/" + w_list.get(k[0]), outputStream);
                        outputStream.close();
                        k[0]++;
                        // myHandler.sendMessage(myHandler.obtainMessage());

                        //  progressBarHoriz.setProgress(Double.valueOf(prmin[0]).intValue());
                        //  progressBarHoriz.incrementProgressBy(Double.valueOf(pr).intValue());
*/
/*                        horiz.post(new Runnable() {
                            public void run() {
                                Log.e("Progress: ", "progress: " + prmin[0]);
                                if (prmin[0] < 100) horiz.setVisibility(View.VISIBLE);
                                else horiz.setVisibility(View.GONE);
                            }
                        });*//*

                    }

                    // progressBarHoriz.setMax(100);
                    //  progressBar.setVisibility(View.INVISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("ERROR: ", "Ошибка");
                } finally {
                    try {
                        if (ftpClient.isConnected()) {
                            ftpClient.logout();
                            ftpClient.disconnect();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };


        Thread tr = new Thread(runnable);
        tr.start();

        final int STATUS_NONE = 0; // нет подключения
        final int STATUS_CONNECTED = 1; // подключено
        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case STATUS_NONE:
                        Log.e("Go", "Not connected");
                        horiz.setVisibility(View.INVISIBLE);
                        break;
                    case STATUS_CONNECTED:
                        Log.e("Go", "Connected");
                        horiz.setVisibility(View.VISIBLE);
                        break;
                }
            };
        };
        h.sendEmptyMessage(STATUS_NONE);



*/
/*        try {
            tr.join();
            horiz.setVisibility(View.GONE);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*//*



    }*/


    /*SimpleDateFormat simpleDateFormat_display = new SimpleDateFormat("dd.MM.yyyy HH.mm");
    SimpleDateFormat simpleDateFormat_db = new SimpleDateFormat("yyyy-MM-dd HH.mm");
    //simpleDateFormat.format(dataFile.getTimestamp().getTime());
                        Log.e("Список файлов:", "File: " + ftpFile_XML.getName());
                        Log.e("Список файлов:", "Размер: "+getFilesSize(ftpConnectData.put_toFTPBackUp+ftpFile_XML.getName()));
                        Log.e("Список файлов:", "Дату: " + simpleDateFormat_db.format(ftpFile_XML.getTimestamp().getTime()));
                        Log.e("Список файлов:", "Дату2: " + getFullTime(ftpFile_XML.getTimestamp().getTimeInMillis()));*/