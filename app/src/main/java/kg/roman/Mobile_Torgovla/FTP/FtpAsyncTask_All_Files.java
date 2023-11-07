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
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import kg.roman.Mobile_Torgovla.R;

public class FtpAsyncTask_All_Files extends AsyncTask<FtpAction, Void, Boolean> {

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
    public String[] mass;

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

    public FtpAsyncTask_All_Files(Context context) {
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

            FTPFile[] files = ftp.listFiles();

// iterates over the files and prints details for each
            DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (FTPFile file : files) {
                String details = file.getName();
                if (file.isDirectory()) {
                    details = "[" + details + "]";
                }
                details += "\t\t" + file.getSize();
                details += "\t\t" + dateFormater.format(file.getTimestamp().getTime());
                System.out.println(details);
                Log.e("FTP", "File..."+details);
                success = true;
            }

            ftp.logout();
            ftp.disconnect();

            return success;
        } catch (Exception x) {
            this.exception = x;
            a.success = false;
            Log.e("FTP", "Error...");
            return false;
        }
    }

}