package kg.roman.Mobile_Torgovla.MT_FTP;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.util.Pair;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import kg.roman.Mobile_Torgovla.R;

public class FtpConnectData {
    // Класс настроек для подключения к FTP-серверу

    // Путь, Логин, Пароль
    public String server_name = "ftp.sunbell.webhost.kg";
    public int port = 21;
    public String server_username = "sunbell_siberica";
    public String server_password = "Roman911NFS";
    public long ftp_timezone = 21600000L;  //Часовой пояс: Asia/Bishkek (+6) = 21600000
    private SharedPreferences mSettings;
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    public String[] mass_file_backup = {"sunbell_rn_db.db3", "sunbell_base_db.db3", "sunbell_const_db.db3"};


    // Пути к Ftp-данным для скачивания
    public String put_toFtpImageTradegof = "/MT_Sunbell_Karakol/Image/Firm_Tradegof"; // для фирмы TradeGof
    public String put_toFtpImageSunbell = "/MT_Sunbell_Karakol/Image/Firm_Sunbell"; // для фирмы Sunbell

    public String put_toFTPBackUp = "/MT_Sunbell_Karakol/MTW_SOS/";
    public String put_toFtpBackUp(Context context)
    {
        // Путь к файлам резервного копирования /MT_Sunbell_Karakol/MTW_SOS/
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String nameRegion = mSettings.getString("ftp_put_list", "").replaceAll("/", "");    // получение имени региона
        String putName = "";
        putName = "/"+nameRegion+"/MTW_SOS/";
        return putName;
    }


    // Путь для Picaso Image картинки
    public String put_toPhoneImage(Context context) {
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        Boolean putToOld, putToFiles, putToSDCard;
        String endPutFile = "";

        putToOld = mSettings.getBoolean("key_putToOld", false);  // Переменая номер накладной
        putToFiles = mSettings.getBoolean("key_putToFiles", false);  // Переменая номер накладной
        putToSDCard = mSettings.getBoolean("key_putToSDCard", false);  // Переменая номер накладной

        /*Uri imagePath_OldMT = Uri.parse("android.resource://kg.roman.mobile_torgovla_image/drawable/name_files.png");
        Uri imagePath_OldMTImage = Uri.parse("android.resource://kg.roman.mobile_torgovla_image/drawable/name_files.png");
        File imagePath_SD = new File("/sdcard/Price/Image/namefile.png");//путь к изображению
        File files = new File(context.getFilesDir().getAbsolutePath());
        File file = new File(files.getPath() + "/Icons/namfile.png");       */

        Uri imagePath_OldMT = Uri.parse("android.resource://kg.roman.mobile_torgovla/drawable/");
        File imagePath_SD = new File("/sdcard/Price/Image/");//путь к изображению
        File files = new File(context.getFilesDir().getAbsolutePath());
        File file = new File(files.getPath() + "/Image/");

        Log.e("Progress: ", "t1: " + putToOld+"__t2: "+putToFiles+"__t3:"+putToSDCard);
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
        String nameRegion = mSettings.getString("ftp_put_list", "").replaceAll("/", "");    // получение имени региона
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


    // Пути к Ftp-данным для отправки
}
