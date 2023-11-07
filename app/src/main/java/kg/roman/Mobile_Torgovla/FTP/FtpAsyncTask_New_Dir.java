package kg.roman.Mobile_Torgovla.FTP;

/**
 * Created by Roman on 26.04.2018.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import kg.roman.Mobile_Torgovla.R;

public class FtpAsyncTask_New_Dir extends AsyncTask<FtpAction, Void, Boolean> {

    private static final String TAG = "MyApp";
    private Exception exception;
    public OutputStream out;
    public File sdPath, sdPath_UP;
    public String FTP_PUT_RN, data_put;
    public SharedPreferences sp;
    public String[] mass_load;
    public FileInputStream inp;
    public FileInputStream in;

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

    public FtpAsyncTask_New_Dir(Context context) {
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

           // String singleDir = "/Server/Forma_Zakaz/XLS22";
            String singleDir = a.remoteFilename;
            ftp.changeWorkingDirectory(singleDir);

            boolean existed = ftp.changeWorkingDirectory(singleDir);
            if(!existed){
                boolean created = ftp.makeDirectory(singleDir);
                if(created){
                    ftp.changeWorkingDirectory(singleDir);
                }else{
                    return false;
                }
            }else{
                ftp.changeWorkingDirectory(singleDir);
            }

            ftp.disconnect();

            return success;
        } catch (Exception x) {
            this.exception = x;
            a.success = false;
            return false;
        }
    }

}