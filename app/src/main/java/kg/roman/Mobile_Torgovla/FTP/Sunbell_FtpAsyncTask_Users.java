package kg.roman.Mobile_Torgovla.FTP;

/**
 * Created by Roman on 26.04.2018.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import kg.roman.Mobile_Torgovla.R;

public class Sunbell_FtpAsyncTask_Users extends AsyncTask<Sunbell_FtpAction, Void, Boolean> {

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

    public Sunbell_FtpAsyncTask_Users(Context context) {
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
            String path_phobe_db = context.getDatabasePath("MTW_In_Users.xml").getAbsolutePath();
            OutputStream out = new FileOutputStream(new File(path_phobe_db));
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            success = ftp.retrieveFile(a.remoteFilename, out);
            a.success = success;
            out.close();
            ftp.disconnect();
            return success;
        } catch (Exception x) {
            this.exception = x;

            a.success = false;

            return false;
        }
    }
}

