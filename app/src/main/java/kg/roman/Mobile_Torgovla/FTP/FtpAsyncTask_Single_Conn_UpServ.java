package kg.roman.Mobile_Torgovla.FTP;

/**
 * Created by Roman on 26.04.2018.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import kg.roman.Mobile_Torgovla.R;

public class FtpAsyncTask_Single_Conn_UpServ extends AsyncTask<FtpAction, Void, Boolean> {

    private static final String TAG = "MyApp";
    private Exception exception;
    public OutputStream out;
    public File sdPath, sdPath_UP;
    public String FTP_PUT_RN, data_put;
    public SharedPreferences sp;
    public String[] mass_load;
    public FileInputStream in;
    public String REGIONS;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now,
            this_data_work_now, this_vrema_work_now;
    public String[] mass;
    public Boolean success;

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

    public FtpAsyncTask_Single_Conn_UpServ(Context context) {
        this.context = context;
        dialog = new ProgressDialog(context);
    }

    protected Boolean doInBackground(FtpAction... actions) {
        Calendate_New();
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        mass = new String[2];
        mass[0] = sp.getString("Data_ftp_out_1", "0");
        mass[1] = sp.getString("Data_ftp_out_2", "0");
        REGIONS = sp.getString("REGIONS", "0");

        //  mass = new String[]{"suncape_all_db.db3", "suncape_rn_db.db3"};
        Log.e("PrfAct", mass[0] + " " + mass[1]+" "+REGIONS);

        FTPClient ftp = new FTPClient();
        FtpAction a = actions[0];
        try {
            ftp.connect(a.connection.server, a.connection.port);
            ftp.login(a.connection.username, a.connection.password);

            for (int i = 0; i < mass.length; i++) {

                String path_phobe_db = context.getDatabasePath(mass[i]).getAbsolutePath();  // путь базы данных на телефоне
                String path_server_db = "/Server/SOS/" + mass[i] + "(" + REGIONS+" "+this_data_now + ").db3";
                success = false;
                FileInputStream in = new FileInputStream(new File(path_phobe_db));
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
                ftp.enterLocalPassiveMode();
                success = ftp.storeFile(path_server_db, in);
                in.close();
                a.success = success;

             /*   File fl = new File(path_phobe_db);
                if (fl.length() >= 1024) {
                    Double d = Double.valueOf((fl.length()/1024));
                    String AUTO_SUMMA_FORMAT1 = new DecimalFormat("#00.00").format(d/1024);
                    Log.e("Размер", (fl.length() / 1023) + "Kb");
                    if (fl.length() >= 1046529) {
                        Double d = Double.valueOf((fl.length()/1024));
                   // Double d = ln.doubleValue();

                        String AUTO_SUMMA_FORMAT1 = new DecimalFormat("#00.00").format(d/1024);
                        Log.e("Размер", AUTO_SUMMA_FORMAT1 + "Mb");


                    }
                }
                Log.e("Размер", (fl.length()) + "b");
*/
            }
            ftp.disconnect();




          /*  boolean success = false;
            in = new FileInputStream(new File(path_phobe_db));  // откуда загружать файлы отправить файлы
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            success = ftp.storeFile(a.remoteFilename, in); // куда отправлять файлы
            //success = ftp.storeFile(a.remoteFilename, in);
            in.close();
            a.success = success;
            ftp.disconnect();*/


            //   String data = "/sdcard/cat2_db.jpg";



           /* Calendate_New();
            sp = PreferenceManager.getDefaultSharedPreferences(context);

            mass[1] = sp.getString("Data_update_ftp_out_1", "0");
            mass[2] = sp.getString("Data_update_ftp_out_2", "0");
            Log.e("PrfAct", mass[1]+" "+mass[2]);*/

         /*   for (int i=0; i < mass.length; i++)
            {
                String path_phobe_db = context.getDatabasePath(mass[i]).getAbsolutePath();  // путь базы данных на телефоне
                String path_server_db = "/Server/SOS/" + mass[i];
                Log.e("PrfAct", mass[i]);
                Log.e("PrfAct", path_server_db+"  "+path_phobe_db);
                in = new FileInputStream(new File(path_phobe_db));
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
                ftp.enterLocalPassiveMode();
                success = ftp.storeFile(path_server_db, in);
                a.success = success;
            }*/


            // String path_server_db = "/Server/Forma_Zakaz/Klients/" + DATABASE_KLIENTS + ".db3";            // путь базы данных на сервере
            /*String path_start = Environment.getExternalStorageDirectory().toString() + "/Price/XLS_"+REGIONS+"/";
            String path_end = "/Server/Forma_Zakaz/XLS_"+REGIONS+"/";*/


            return success;
        } catch (Exception x) {
            this.exception = x;

            a.success = false;

            return false;
        }
    }

    protected void Calendate_New() {
        DateFormat df_data = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df_vrema = new SimpleDateFormat("HH:mm:ss");
        DateFormat df_year = new SimpleDateFormat("yyyy");
        DateFormat df_month = new SimpleDateFormat("MM");
        DateFormat df_day = new SimpleDateFormat("dd");

        this_rn_data = df_data.format(Calendar.getInstance().getTime());
        this_rn_vrema = df_vrema.format(Calendar.getInstance().getTime());
        this_rn_year = df_year.format(Calendar.getInstance().getTime());
        this_rn_month = df_month.format(Calendar.getInstance().getTime());
        this_rn_day = df_day.format(Calendar.getInstance().getTime());

        // this_data_now = this_rn_day + "-" + this_rn_month + "-" + this_rn_year;  // Формат для отображения

        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(this_rn_year), Integer.valueOf(this_rn_month) - 1, Integer.valueOf(this_rn_day));
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("dd:MM:yyyy");
        String dateString_NOW = dateFormat1.format(calendar.getTime());
        String dateString_WORK = dateFormat2.format(calendar.getTime());
        String dateString_NOW_Ostatok = dateFormat3.format(calendar.getTime());
        this_data_now = dateString_NOW;
        this_data_work_now = dateString_WORK;
        this_vrema_work_now = this_rn_vrema;
        this_data_now = dateString_NOW_Ostatok + " " + this_rn_vrema;

        //  Log.e("WJ_FormaL2:", "!DataEnd:" + dateString2);

    } // Загрузка даты и время
}