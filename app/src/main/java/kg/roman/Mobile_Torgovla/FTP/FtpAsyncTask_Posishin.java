package kg.roman.Mobile_Torgovla.FTP;

/**
 * Created by Roman on 26.04.2018.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import kg.roman.Mobile_Torgovla.R;

public class FtpAsyncTask_Posishin extends AsyncTask<FtpAction, Void, Boolean> {

    private static final String TAG = "MyApp";
    private Exception exception;
    public OutputStream out;

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

    public FtpAsyncTask_Posishin(Context context) {
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

            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            Log.v(TAG, "Список всех файлов......");

            FTPFile[] filesArray = ftp.listFiles("/Server/Nomeklature/");
            //System.out.println("файлов: " + filesArray.length);
            for (FTPFile f : filesArray) {
                if (f.isDirectory()) {
                    System.out.println("Folder.....: " + f);
                    Log.v(TAG, "Folder..." + f);
                    // String data = context.getDatabasePath(f.getName()).getAbsolutePath(); // путь в базам данных на телефоне
                } else if (f.isFile()) {
                    System.out.println("File........: " + f.getName());
                    Log.v(TAG, "File..." + f.getName());
                    String data = context.getDatabasePath(f.getName()).getAbsolutePath(); // путь в базам данных на телефоне
                    //String data = "/sdcard/"+f.getName();
                    out = new FileOutputStream(new File(data));
                    //success = ftp.retrieveFile(a.remoteFilename, out);
                    success = ftp.retrieveFile("/Server/Nomeklature/"+f.getName(), out);
                }
            }
            out.close();
            a.success = success;
            ftp.disconnect();

            //File rootFolder = Environment.getExternalStorageDirectory();



/*
            // Загрузка данных на сервер
            FileInputStream in = new FileInputStream(new File(data));
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            success = ftp.storeFile(a.remoteFilename, in);
            in.close();
            a.success = success;
            ftp.disconnect();
*/
/*
            String data = context.getDatabasePath("ostatki_db.db3").getAbsolutePath(); // путь в базам данных на телефоне
            // Скачивание данных с сервер
            //  String data = "/data/user/0/kg.roman911.sunbell_shtrih_kod/databases/ostatki2.db3";
            OutputStream out = new FileOutputStream(new File(data));
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            success = ftp.retrieveFile(a.remoteFilename, out);
            out.close();
            a.success = success;
            ftp.disconnect();
*/

/*
            // Загрузка базы на сервер
            String data = context.getDatabasePath("bella5_db.db3").getAbsolutePath(); // путь в базам данных на телефоне
            FileInputStream in = new FileInputStream(new File(data));
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            success = ftp.storeFile(a.remoteFilename, in);
            in.close();
            a.success = success;
            ftp.disconnect();
*/




/*
скачивание файла с сервера FTP
            boolean success = false;
            String data = "/sdcard/cat2_db.jpg";
            OutputStream out = new FileOutputStream(new File(data));
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            success = ftp.retrieveFile(a.remoteFilename, out);
            out.close();
            a.success = success;
            ftp.disconnect();
*/


/*
загрузка файла из телефона
File destinationFile = new File(Environment.getExternalStorageDirectory()+ dir + fileName);  //путь

            boolean success = false;
            String data = "/sdcard/cat2_db.jpg";
            FileInputStream in = new FileInputStream(new File(data));
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            success = ftp.storeFile(a.remoteFilename, in);
            in.close();
            a.success = success;
            ftp.disconnect();
*/



            /*ftp.enterLocalPassiveMode(); // important!
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            String data = "/sdcard/vivekm4a.m4a";

            OutputStream out = new FileOutputStream(new File(data));
            boolean result = ftp.retrieveFile("vivekm4a.m4a", out);
            out.close();
            if (result) Log.v("download result", "succeeded");
            ftp.logout();
            ftp.disconnect();*/


            return success;
        } catch (Exception x) {
            this.exception = x;

            a.success = false;

            return false;
        }
    }
}
