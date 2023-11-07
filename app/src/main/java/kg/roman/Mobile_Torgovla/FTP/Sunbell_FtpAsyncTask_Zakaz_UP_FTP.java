package kg.roman.Mobile_Torgovla.FTP;

/**
 * Created by Roman on 26.04.2018.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import kg.roman.Mobile_Torgovla.R;

public class Sunbell_FtpAsyncTask_Zakaz_UP_FTP extends AsyncTask<Sunbell_FtpAction, Void, Boolean> {

    public OutputStream out;
    public SharedPreferences sp;
    public FileInputStream in;
    public Exception exception;
    public String PEREM_WORK_DISTR;

    /**
     * progress dialog to show user that the backup is processing.
     */
    private ProgressDialog dialog;
    /**
     * application context.
     */
    private Context context;

    protected void onPreExecute() {
        // this.dialog.setMessage(context.getString(R.string.up_ftp_data_load));
        this.dialog.setMessage(context.getString(R.string.up_ftp_data_load));
        this.dialog.show();  // вызов диалога
        this.dialog.cancel();

    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if (success) {
            Toast.makeText(context, context.getString(R.string.up_ftp_data_load), Toast.LENGTH_SHORT).show();
            Log.e("FTP", "Загрузка данных_TRUE: " + success);

        } else {
            Toast.makeText(context, context.getString(R.string.up_ftp_data_error), Toast.LENGTH_LONG).show();
            Log.e("FTP", "Загрузка данных_FALSE: " + success);

        }
    }

    public Sunbell_FtpAsyncTask_Zakaz_UP_FTP(Context context) {
        this.context = context;
        dialog = new ProgressDialog(context);
    }

    protected Boolean doInBackground(Sunbell_FtpAction... actions) {
        FTPClient ftp = new FTPClient();
        Sunbell_FtpAction a = actions[0];
        try {
            sp = PreferenceManager.getDefaultSharedPreferences(context);
            PEREM_WORK_DISTR = sp.getString("PEREM_WORK_DISTR", "0");                 //чтение данных: имя папки с данными (01_WDay)

            ftp.connect(a.connection.server, a.connection.port);
            ftp.login(a.connection.username, a.connection.password);
            boolean success = false;

            String path_phobe_db = Environment.getExternalStorageDirectory().toString() + "/Price/" + PEREM_WORK_DISTR + "/" + a.fileName;
            // 22.10.2022  String path_phobe_db = "/sdcard/Price/" + PEREM_WORK_DISTR + "/" + a.fileName;
            //   String path_phobe_db = context.getDatabasePath(a.fileName).getAbsolutePath();        // путь базы данных на телефоне
            //   String path_phobe_db = context.getDatabasePath(a.fileName).getAbsolutePath();        // путь базы данных на телефоне
            String path_server_db = a.remoteFilename;                                           // путь базы данных на FTP
            FileInputStream in = new FileInputStream(new File(path_phobe_db));
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            success = ftp.storeFile(path_server_db, in);
            a.success = success;
            in.close();
            ftp.disconnect();
            Log.e("FTP", "Загрузка данных_TRUE");
            Log.e("FTP", "PUTTR^ "+path_phobe_db);
            return success;
        } catch (Exception x) {
            this.exception = x;
            a.success = false;
            String path_phobe_db = Environment.getExternalStorageDirectory().toString() + "/Price/" + PEREM_WORK_DISTR + "/" + a.fileName;


            Log.e("FTP", "Загрузка данных_FALSE");
            Log.e("FTP", "PUT^ "+path_phobe_db);
            // storage/emulated/0/Price/05_WDay/MT_out_order_sunbell_(marshrut-401)_2022-10-21.xml XIAOMI
            ///storage/emulated/0/Price/05_WDay/MT_out_order_sunbell_(marshrut-401)_2022-10-21.xml SAMSUNG
            return false;
        }
    }
}

