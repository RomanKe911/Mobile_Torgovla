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

public class FtpAsyncTask_RN_Up_Excel extends AsyncTask<FtpAction, Void, Boolean> {

    private static final String TAG = "MyApp";
    private Exception exception;
    public OutputStream out;
    public File sdPath, sdPath_UP;
    public String FTP_PUT_RN, data_put;
    public SharedPreferences sp;
    public String[] mass_load;
    public FileInputStream inp;
    public FileInputStream in;
    public String REGIONS;

    /**
     * progress dialog to show user that the backup is processing.
     */
    private ProgressDialog dialog;
    /**
     * application context.
     */
    private Context context;

    protected void onPreExecute() {
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

        } else {
            Toast.makeText(context, context.getString(R.string.up_ftp_data_error), Toast.LENGTH_SHORT).show();

        }
    }

    public FtpAsyncTask_RN_Up_Excel(Context context) {
        this.context = context;
        dialog = new ProgressDialog(context);
    }

    protected Boolean doInBackground(FtpAction... actions) {
        FTPClient ftp = new FTPClient();
        FtpAction a = actions[0];
        try {
            ftp.connect(a.connection.server, a.connection.port);
            ftp.login(a.connection.username, a.connection.password);
            boolean success = false;

            sp = PreferenceManager.getDefaultSharedPreferences(context);
            REGIONS = sp.getString("REGIONS", "0");

            String path_start = Environment.getExternalStorageDirectory().toString() + "/Price/XLS/";
            String path_end = "/Server/Forma_Zakaz/XLS/";

            //  data_put = Environment.getExternalStorageDirectory().toString() + "/Price/XLS/OctRN0015.xls";
            File file = new File(path_start);
            File filesArray[] = file.listFiles(); // Путь к нужным файлам
            for (File f : filesArray) {
                if (f.isDirectory()) {
                    System.out.println("Folder xls.....: " + f);
                    Log.v(TAG, "Folder xls..." + f);
                    // String data = context.getDatabasePath(f.getName()).getAbsolutePath(); // путь в базам данных на телефоне
                } else if (f.isFile()) {
                    System.out.println("File xls........: " + f.getName());
                    Log.v(TAG, "File xls..." + f.getName());

                    in = new FileInputStream(new File(path_start + f.getName()));  // откуда загружать файлы отправить файлы
                    ftp.setFileType(FTP.BINARY_FILE_TYPE);
                    ftp.enterLocalPassiveMode();
                    success = ftp.storeFile(path_end + f.getName(), in); // куда отправлять файлы
                    //success = ftp.storeFile(a.remoteFilename, in);
                    a.success = success;
                }
            }

            in.close();
            ftp.disconnect();
            return success;
        } catch (Exception x) {
            this.exception = x;
            a.success = false;
            return false;
        }
    }

}