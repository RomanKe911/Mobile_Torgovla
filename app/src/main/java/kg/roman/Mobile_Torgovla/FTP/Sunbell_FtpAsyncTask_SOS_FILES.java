package kg.roman.Mobile_Torgovla.FTP;

/**
 * Created by Roman on 26.04.2018.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import kg.roman.Mobile_Torgovla.R;

public class Sunbell_FtpAsyncTask_SOS_FILES extends AsyncTask<Sunbell_FtpAction, Void, Boolean> {

    public OutputStream out;
    public SharedPreferences sp;
    public FileInputStream in;
    public Exception exception;

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

    public Sunbell_FtpAsyncTask_SOS_FILES(Context context) {
        this.context = context;
        dialog = new ProgressDialog(context);
    }

    protected Boolean doInBackground(Sunbell_FtpAction... actions) {
        FTPClient ftp = new FTPClient();
        Sunbell_FtpAction a = actions[0];
        try {
            ftp.connect(a.connection.server, a.connection.port);
            ftp.login(a.connection.username, a.connection.password);
            boolean success = false;
            String path_phobe_db = context.getDatabasePath(a.fileName).getAbsolutePath();        // путь базы данных на телефоне
            String path_server_db = a.remoteFilename;     // путь базы данных на FTP
            FileInputStream in = new FileInputStream(new File(path_phobe_db));
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            success = ftp.storeFile(path_server_db, in);
            a.success = success;
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

