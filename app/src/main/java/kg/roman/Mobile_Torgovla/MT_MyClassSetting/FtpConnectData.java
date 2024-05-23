package kg.roman.Mobile_Torgovla.MT_MyClassSetting;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class FtpConnectData {
    // Класс настроек для подключения к FTP-серверу

    // Путь, Логин, Пароль
    public String server_name = "ftp.sunbell.webhost.kg";
    public int port = 21;
    public String server_username = "sunbell_siberica";
    public String server_password = "Roman911NFS";
    public long ftp_timezone = 21600000L;  //Часовой пояс: Asia/Bishkek (+6) = 21600000
    public int ftp_backup_sizeFile = 3;
    private SharedPreferences mSettings;
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    public String[] mass_file_backup = {"sunbell_rn_db", "sunbell_base_db", "sunbell_const_db"};

    public Map<String, String> getListMailMessege()
    {
        Map<String, String> list_mail_where = new HashMap<>();
        list_mail_where.put("/MT_Sunbell_Bishkek/", "bishkek@sunbell.webhost.kg");
        list_mail_where.put("/MT_Sunbell_Osh/", "osh@sunbell.webhost.kg");
        list_mail_where.put("/MT_Sunbell_Djala_abad/", "djala-abad@sunbell.webhost.kg");
        list_mail_where.put("/MT_Sunbell_Karakol/", "kerkin911@gmail.com");
        list_mail_where.put("/MT_Sunbell_Cholpon-Ata/", "cholpon-ata@sunbell.webhost.kg");
        return list_mail_where;
    }



    public String messege_mail_from = "sunbellagents@gmail.com";
    public String messege_mail_where = "kerkin911@gmail.com";
    public String messege_mail_sender_user = "sunbellagents@gmail.com";
    public String messege_mail_sender_password = "fyczcoexpaspsham";
    public String messege_mail_sender_port = "465";
    public String messege_mail_sender_mailhost = "smtp.gmail.com";


    // Пути к Ftp-данным для скачивания
    public String put_toFtpImageTradegof = "/MT_Sunbell_Karakol/Image/Firm_Tradegof"; // для фирмы TradeGof
    public String put_toFtpImageSunbell = "/MT_Sunbell_Karakol/Image/Firm_Sunbell"; // для фирмы Sunbell

    protected String loge_TAG = "FtpConnectData";

    // public String put_toFTPBackUp = "/MT_Sunbell_Karakol/MTW_SOS/";

    public String put_toFtpBackUp(Context context) {
        // Путь к файлам резервного копирования /MT_Sunbell_Karakol/MTW_SOS/
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String nameRegion = mSettings.getString("setting_ftpPathData", "").replaceAll("/", "");    // получение имени региона
        String putName = "";
        putName = "/" + nameRegion + "/MTW_SOS/";
        return putName;
    }


    // Путь для Picaso Image картинки
    public String put_toPhoneImage(Context context) {

        Boolean putToOld, putToFiles, putToSDCard;
        String endPutFile = "";

        PreferencesWrite preferencesWrite = new PreferencesWrite(context);
        putToOld = preferencesWrite.Setting_ImageputToOld;
        putToFiles = preferencesWrite.Setting_ImageputToFiles;
        putToSDCard = preferencesWrite.Setting_ImageputToSDCard;



        /*Uri imagePath_OldMT = Uri.parse("android.resource://kg.roman.mobile_torgovla_image/drawable/name_files.png");
        Uri imagePath_OldMTImage = Uri.parse("android.resource://kg.roman.mobile_torgovla_image/drawable/name_files.png");
        File imagePath_SD = new File("/sdcard/Price/Image/namefile.png");//путь к изображению
        File files = new File(context.getFilesDir().getAbsolutePath());
        File file = new File(files.getPath() + "/Icons/namfile.png");       */

        Uri imagePath_OldMT = Uri.parse("android.resource://kg.roman.mobile_torgovla/drawable/");
        File imagePath_SD = new File("/sdcard/Price/Image/");//путь к изображению
        File files = new File(context.getFilesDir().getAbsolutePath());
        File file = new File(files.getPath() + "/Image/");

        Log.e("Progress: ", "t1: " + putToOld + "__t2: " + putToFiles + "__t3:" + putToSDCard);
        if (putToOld) endPutFile = imagePath_OldMT.toString();
        if (putToFiles) endPutFile = file + "/";
        if (putToSDCard) endPutFile = imagePath_SD + "/";
        return endPutFile;
    }

    public String put_toPhoneIcons(Context context) {
        File files = new File(context.getFilesDir().getAbsolutePath());
        return files.getPath() + "/Icons/";
    }


    // Универсальный путь для доступа к Картинкам на сервере FTP для филиалов
    public String put_toFTPforRegions(Context context) {
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String nameRegion = mSettings.getString("setting_ftpPathData", "").replaceAll("/", "");    // получение имени региона
        String nameSklad = mSettings.getString("PEREM_AG_SKLAD", "");    // получение название склада
        String putName = "";
        switch (nameRegion) {
            case "MT_Sunbell_Bishkek":
                putName = "/MT_Sunbell_Karakol/Image/Firm_Sunbell";
                break;
            case "MT_Sunbell_Osh":
                putName = "/MT_Sunbell_Karakol/Image/Firm_Sunbell";
                break;
            case "MT_Sunbell_Djala_abad":
                putName = "/MT_Sunbell_Karakol/Image/Firm_Sunbell";
                break;
            case "MT_Sunbell_Karakol": {
                if (nameSklad.equals("склад Каракол (Tradegof)"))
                    putName = "/MT_Sunbell_Karakol/Image/Firm_Tradegof";
                else putName = "/MT_Sunbell_Karakol/Image/Firm_Sunbell";
            }
            break;
            case "MT_Sunbell_Cholpon-Ata":
                putName = "/MT_Sunbell_Karakol/Image/Firm_Sunbell";
                break;
        }
        return putName;
    }


    //// Конвертация даты файлы из long в String(дата и время) (WORK)
    public String getFullTimeLongToString(long timeInMillis) {
        final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        // final Calendar c = Calendar.getInstance();
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeInMillis + ftp_timezone);
        c.setTimeZone(TimeZone.getDefault());
        // Log.e("TimeZone: ", "Long: " + timeInMillis + " = " + format.format(c.getTime()));
        return format.format(c.getTime());
    }

    //// Конвертация даты файлы из long в String(дата и время) (WORK)
    public String getFullTimeLongToStringFormatBackup(long timeInMillis) {
        final SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");
        // final Calendar c = Calendar.getInstance();
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeInMillis);
        c.setTimeZone(TimeZone.getDefault());
        // Log.e("TimeZone: ", "Long: " + timeInMillis + " = " + format.format(c.getTime()));
        return format.format(c.getTime());
    }


    //// Конвертация даты файлы из String в Long(дата и время) (WORK)
    public Long getFullTimeStringToLong(String timeString) {
        // Формат строки 09:01:2024 09:37:21
        Calendar calendar = new GregorianCalendar();
        long returnLong;
        try {
            String firstString = timeString.substring(0, timeString.indexOf(" "));
            String secondString = timeString.substring(timeString.lastIndexOf(" "));
            int day = Integer.parseInt(firstString.substring(0, firstString.indexOf(":")).trim());
            int month = Integer.parseInt(firstString.substring(firstString.indexOf(":") + 1, firstString.lastIndexOf(":")).trim()) - 1;
            int year = Integer.parseInt(firstString.substring(firstString.lastIndexOf(":") + 1).trim());
            int hour = Integer.parseInt(secondString.substring(0, secondString.indexOf(":")).trim());
            int minute = Integer.parseInt(secondString.substring(secondString.indexOf(":") + 1, secondString.lastIndexOf(":")).trim());
            int second = Integer.parseInt(secondString.substring(secondString.lastIndexOf(":") + 1).trim());
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, second);
            calendar.set(Calendar.MILLISECOND, 0);
            //  Log.e("TimeZoneData: ", "Long: " + calendar.getTimeInMillis() + " Da " + calendar.getTime());
            returnLong = calendar.getTimeInMillis();
        } catch (Exception e) {
            Log.e(loge_TAG, "Ошибка выполнения конвертации даты в формат long");
            returnLong = 0L;
        }
        return returnLong;
    }


    public String CreateNameFile_BackUp(String stringName) {

        char[] chars_rus = {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ч', 'ц', 'ш', 'щ', 'э', 'ю', 'я', 'ы', 'ъ', 'ь'};
        String[] string_eng = {"a", "b", "v", "g", "d", "e", "io", "zh", "z", "i", "i", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ch", "c", "sh", "csh", "e", "ju", "ja", "i", "", ""};
        StringBuilder newName = new StringBuilder();
        StringBuilder newNameNumber = new StringBuilder();

        if (!stringName.isEmpty() | !stringName.equals(" ")) {
            //  String string1 = stringName.replaceFirst(" ", "_").replaceAll(" ", "");
            String string1 = stringName.replaceAll(" ", "_");
            String stringWork = string1.toLowerCase().replaceAll("[^a-zа-я0-9_]", "");
            for (Character agent : stringWork.toCharArray()) {
                for (int i = 0; i < chars_rus.length; i++)
                    if (chars_rus[i] == agent)
                        newName.append(string_eng[i]);
                if (Character.isDigit(agent))
                    newNameNumber.append(agent);
                if (agent.toString().matches("[a-z]"))
                    newName.append(agent);
                if (agent.toString().matches("_"))
                    newName.append(agent);
            }
            newName.append(newNameNumber);
        } else {
            Log.e("CreateNameFile_BackUp", "не возможно создать строку, не верный формат данных");
            // Snackbar.make(constraintLayout, "не возможно создать строку, не верный формат данных", Snackbar.LENGTH_SHORT).show();
        }
        Log.e("CreateNameFile_BackUp", newName.toString());
        return newName.toString();
    }
}
