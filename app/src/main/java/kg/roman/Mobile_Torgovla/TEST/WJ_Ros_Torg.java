package kg.roman.Mobile_Torgovla.TEST;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import kg.roman.Mobile_Torgovla.FTP.FtpAction;
import kg.roman.Mobile_Torgovla.FTP.FtpAsyncTask_RN_Up;
import kg.roman.Mobile_Torgovla.FTP.FtpConnection;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Ros_RN;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Excel;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Ros_RN;
import kg.roman.Mobile_Torgovla.MailSenderClass;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.Spravochnik.SPR_Ostatok_Single;

public class WJ_Ros_Torg extends AppCompatActivity {


    public ArrayList<ListAdapterSimple_Ros_RN> adapter_rn = new ArrayList<ListAdapterSimple_Ros_RN>();
    public ListAdapterAde_Ros_RN adapterPriceClients;
    public Button button_filter, button_add, button_ost, button_up, button_excel;
    public ListView listView;
    public Context context_Activity;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    public String sklad_name;
    public TextView name, cena, kod_univ, strih, ostatok, textView_summa;
    public SharedPreferences sp;
    public SharedPreferences.Editor ed;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_filter, this_data;
    public String this_data_rn, this_data_subtitle;
    public String intent_Data, intent_Kod_RN;
    Calendar Calendar_Name = Calendar.getInstance();

    public ArrayList<ListAdapterSimple_Excel> excell = new ArrayList();
    public File fileName;
    public FileOutputStream fos;
    public File sdPath;
    public OutputStream out;
    public String name_put, PEREM_UID;
    public String from, REGIONS, attach, text, title, where;
    public String new_write, ftp_con_serv, ftp_con_login, ftp_con_pass, ftp_con_put, name_db, data_ostatok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ros_torg);
        setContentView(R.layout.activity_ros_torg);

        context_Activity = WJ_Ros_Torg.this;
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.user_pda);
        getSupportActionBar().setTitle("Розничные продажи");
        Calendare();
        getSupportActionBar().setSubtitle(this_data_subtitle);

        button_filter = (Button) findViewById(R.id.btn_str);
        button_add = (Button) findViewById(R.id.btn_add);
        button_ost = (Button) findViewById(R.id.btn_ost);
        button_up = (Button) findViewById(R.id.btn_up);
        button_excel = (Button) findViewById(R.id.btn_excel);
        listView = (ListView) findViewById(R.id.list_strih_zakaz);
        textView_summa = (TextView) findViewById(R.id.tvw_autosumma);

        adapter_rn.clear();
        Loading_DB_This_Data();
        Loading_DB_AutoSumma(this_data_rn, this_data_rn);
        adapterPriceClients = new ListAdapterAde_Ros_RN(context_Activity, adapter_rn);
        adapterPriceClients.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients);

        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        PEREM_UID = sp.getString("UID_AGENTS", "0");
        DB_Excel_Put(PEREM_UID);
        name_put = "XLS_Rosn";
        sklad_name = "63F85378-43D1-4419-9163-4F53E1DCBFE8"; // 1 склад Каракол (Корзина) склад для розницы
        ftp_con_serv = "176.123.246.244";
        ftp_con_login = "sunbell_siberica";
        ftp_con_pass = "Roman911NFS";
        REGIONS = sp.getString("REGIONS", "0");

        button_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendare_Data();
            }
        });

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context_Activity, WJ_Ros_Torg_2.class);
                startActivity(intent);
                finish();

            }
        });

        button_ost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context_Activity, SPR_Ostatok_Single.class);
                startActivity(intent);
            }
        });

        button_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_write = "Розничные продажи!!!";
                new_write = (new_write + "\n");
                new_write = new_write + "Дата выгрузки: " + this_data;
                new_write = (new_write + "\n");
                new_write = new_write + "Файл: Rosn_" + this_data + ", на сумму: " + textView_summa.getText().toString();

                Mail();
                WJ_Ros_Torg.sender_mail_async async_sending = new WJ_Ros_Torg.sender_mail_async();
                async_sending.execute();
                Loading_1();
                Toast.makeText(context_Activity, "Выгрузка завершенна!", Toast.LENGTH_SHORT).show();
            }
        });

        button_excel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Writing_To_Exsel();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                intent_Data = ((TextView) view.findViewById(R.id.tvw_ros_data)).getText().toString();
                intent_Kod_RN = ((TextView) view.findViewById(R.id.tvw_ros_RN)).getText().toString();
                Intent intent = new Intent(context_Activity, WJ_Ros_Print.class);
                intent.putExtra("Ros_Data", intent_Data);
                intent.putExtra("Ros_KodRn", intent_Kod_RN);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        Log.e("Resume", "Yes!");
        /*sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        ed = sp.edit();
        PEREM_DATA_START = sp.getString("PEREM_DATA_START", "0");
        PEREM_DATA_STOP = sp.getString("PEREM_DATA_STOP", "0");
        adapter_rn.clear();
        Loading_DB_Filter(PEREM_DATA_START, PEREM_DATA_STOP);
        Loading_DB_AutoSumma(PEREM_DATA_START, PEREM_DATA_STOP);
        adapterPriceClients = new ListAdapterAde_Ros_RN(context_Activity, adapter_rn);
        adapterPriceClients.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients);*/
        super.onResume();
    }

    protected void Calendare_Data() {

        final Calendar c = Calendar.getInstance();
        int mYear, mMonth, mDay;
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        Calendar_Name = Calendar.getInstance();
        Calendar_Name.set(Integer.parseInt(this_rn_year), Integer.valueOf(this_rn_month) - 1, Integer.valueOf(this_rn_day));
        SimpleDateFormat dateFormat_filter = new SimpleDateFormat("yyyy-MM-dd");  // формат для сортировки даты
        String Data_filter_now = dateFormat_filter.format(Calendar_Name.getTime());
        this_data_filter = Data_filter_now;


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                        String dateString_start = dateFormat2.format(calendar.getTime());
                        String dateString_end = this_data_filter;

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String dateString_start_view = dateFormat.format(calendar.getTime());

                        getSupportActionBar().setSubtitle("Рабочая дата: " + dateString_start_view + " по " + dateString_end);
                        Log.e(WJ_Ros_Torg.this.getLocalClassName(), "DataStart:" + dateString_start);
                        Log.e(WJ_Ros_Torg.this.getLocalClassName(), "DataEnd" + dateString_end);

                        /*ed = sp.edit();
                        ed.putString("PEREM_DATA_START", dateString_start);  // передача кода агента
                        ed.putString("PEREM_DATA_STOP", dateString_start_view);  // передача кода агента
                        ed.commit();*/

                        adapter_rn.clear();
                        Loading_DB_Filter(dateString_start, dateString_end);
                        Loading_DB_AutoSumma(dateString_start, dateString_end);
                        adapterPriceClients = new ListAdapterAde_Ros_RN(context_Activity, adapter_rn);
                        adapterPriceClients.notifyDataSetChanged();
                        listView.setAdapter(adapterPriceClients);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    protected void Loading_DB_This_Data() {

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("rosnica_db.db3", MODE_PRIVATE, null);
        String query = "SELECT kod_RN, SUM(summa) AS itogo, data, vrema\n" +
                "FROM t5_prodaji\n" +
                "WHERE data = '" + this_data + "'\n" +
                "GROUP BY kod_RN;";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String kod_rn = cursor.getString(cursor.getColumnIndex("kod_RN"));
            String data = cursor.getString(cursor.getColumnIndex("data"));
            String vrema = cursor.getString(cursor.getColumnIndex("vrema"));
            String summa = cursor.getString(cursor.getColumnIndex("itogo"));
            adapter_rn.add(new ListAdapterSimple_Ros_RN(kod_rn, data, vrema, summa));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    protected void Loading_DB_Filter(String data_1, String data_2) {

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("rosnica_db.db3", MODE_PRIVATE, null);
        String query_data = "SELECT kod_RN, SUM(summa) AS itogo, data, vrema\n" +
                "FROM t5_prodaji\n" +
                "WHERE data BETWEEN '" + data_1 + "' AND '" + data_2 + "'\n" +
                "GROUP BY kod_RN;";

        final Cursor cursor_data = db.rawQuery(query_data, null);
        cursor_data.moveToFirst();
        while (cursor_data.isAfterLast() == false) {
            String kod_rn = cursor_data.getString(cursor_data.getColumnIndex("kod_RN"));
            String data = cursor_data.getString(cursor_data.getColumnIndex("data"));
            String vrema = cursor_data.getString(cursor_data.getColumnIndex("vrema"));
            String summa = cursor_data.getString(cursor_data.getColumnIndex("itogo"));
            adapter_rn.add(new ListAdapterSimple_Ros_RN(kod_rn, data, vrema, summa));
            cursor_data.moveToNext();
        }
        cursor_data.close();
        db.close();
    }

    protected void Loading_DB_AutoSumma(String data_1, String data_2) {

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("rosnica_db.db3", MODE_PRIVATE, null);

        String query_summa = "SELECT SUM(summa) AS itogo\n" +
                "FROM t5_prodaji\n" +
                "WHERE data BETWEEN '" + data_1 + "' AND '" + data_2 + "';\n";
        final Cursor cursor_summa = db.rawQuery(query_summa, null);
        cursor_summa.moveToFirst();
        String itogo = (cursor_summa.getString(cursor_summa.getColumnIndex("itogo")));
        textView_summa.setText(itogo + "c");
        cursor_summa.close();
        db.close();
    }

    private void Calendare() {
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

        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(this_rn_year), Integer.valueOf(this_rn_month) - 1, Integer.valueOf(this_rn_day));
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd:MM:yyyy");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat_this = new SimpleDateFormat("yyyy-MM-dd");
        this_data_rn = dateFormat_this.format(calendar.getTime());
        this_data_subtitle = dateFormat1.format(calendar.getTime());
        this_data = dateFormat2.format(calendar.getTime());
        Log.e("Data_", this_data_rn);
    }


    private static void createSheetHeader(HSSFSheet paramHSSFSheet, int paramInt, ListAdapterSimple_Excel paramListAdapterSimple_Excel) {
        // Создание столбцов для Excel
        HSSFRow localHSSFRow = paramHSSFSheet.createRow(paramInt);
        localHSSFRow.createCell(0).setCellValue(paramListAdapterSimple_Excel.getKod());
        localHSSFRow.createCell(1).setCellValue(paramListAdapterSimple_Excel.getName());
        localHSSFRow.createCell(2).setCellValue(paramListAdapterSimple_Excel.getCena().doubleValue());
        localHSSFRow.createCell(3).setCellValue(paramListAdapterSimple_Excel.getKol().intValue());
    }

    public List<ListAdapterSimple_Excel> fillData() {

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("rosnica_db.db3", MODE_PRIVATE, null);
        String query = "SELECT data, kod_RN, kod_univ, name, cena, kol_vo\n" +
                "FROM t5_prodaji\n" +
                "WHERE data = '" + this_data + "'";

        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String kod_rn = cursor.getString(cursor.getColumnIndex("kod_RN"));
            String kod_univ = cursor.getString(cursor.getColumnIndex("kod_univ"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            Double cena = cursor.getDouble(cursor.getColumnIndex("cena"));
            Integer kol = cursor.getInt(cursor.getColumnIndex("kol_vo"));
            excell.add(new ListAdapterSimple_Excel(kod_univ, name, cena, kol));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return excell;
    }

    public void Writing_To_Exsel() {
        HSSFWorkbook localHSSFWorkbook = new HSSFWorkbook();
        HSSFSheet localHSSFSheet = localHSSFWorkbook.createSheet("Заказ");
        List localList = fillData();
        int i = 0;
        localHSSFSheet.createRow(0);
        Iterator localIterator = localList.iterator();
        while (localIterator.hasNext()) {
            ListAdapterSimple_Excel localListAdapterSimple_Excel = (ListAdapterSimple_Excel) localIterator.next();
            i++;
            createSheetHeader(localHSSFSheet, i, localListAdapterSimple_Excel);
        }
        this.fos = null;
        if (!Environment.getExternalStorageState().equals("mounted"))
            Toast.makeText(getApplicationContext(), "SD-сущ", Toast.LENGTH_LONG).show();
        this.sdPath = Environment.getExternalStorageDirectory();
        this.sdPath = new File(this.sdPath.getAbsolutePath() + "/Price/" + name_put + "/");

        if (!new File(getSDcardPath()).exists())
            new File(getSDcardPath()).mkdirs();
        this.fileName = new File(this.sdPath, "Rosn_" + this_data + ".xls");

        try {
            this.fos = new FileOutputStream(this.fileName);
            localHSSFWorkbook.write(this.fos);
            localHSSFWorkbook.close();
            Toast.makeText(getApplicationContext(), "Excel файл успешно создан!", Toast.LENGTH_LONG).show();
            return;
        } catch (IOException localIOException) {
            while (true)
                localIOException.printStackTrace();
        }
    }

    private String getSDcardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    protected void DB_Excel_Put(String uid) {
        SQLiteDatabase db_insert = getBaseContext().openOrCreateDatabase("suncape_const_db.db3", MODE_PRIVATE, null);
        String query = "SELECT const_logins.name, const_logins.s_name, const_logins.UID, const_logins.Name_Distr " +
                "FROM const_logins " +
                "WHERE const_logins.UID LIKE ('%" + uid + "%')";
        final Cursor cursor = db_insert.rawQuery(query, null);
        cursor.moveToFirst();
        String Name_Distr = cursor.getString(cursor.getColumnIndex("Name_Distr"));
        name_put = Name_Distr;
        cursor.close();
        db_insert.close();
    }


    protected void Mail() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


    }

    private class sender_mail_async extends AsyncTask<Object, String, Boolean> {
        ProgressDialog WaitingDialog;

        @Override
        protected void onPreExecute() {
            WaitingDialog = ProgressDialog.show(context_Activity, "Выгрузка заказа", "Выполняется загрузка, подождите...", true);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            WaitingDialog.dismiss();
            Toast.makeText(context_Activity, "Заказ успешно отправлен!!!", Toast.LENGTH_LONG).show();
            //((Activity)mainContext).finish();
        }

        @Override
        protected Boolean doInBackground(Object... params) {

            try {
                title = "Розничные продажи за : " + this_data;
                text = new_write;

                from = "kerkin911@mail.ru";
                where = "kerkin911@gmail.com";
                attach = "";
                MailSenderClass sender = new MailSenderClass("kerkin911@mail.ru", "microlab_LG901480", "465", "smtp.mail.ru");

                sender.sendMail(title, text, from, where, attach);
            } catch (Exception e) {
                Toast.makeText(context_Activity, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
            }

            return false;
        }
    }

    protected void Loading_1() {
// подключение к ftp-server
        ftp_con_put = "/Server/Forma_Zakaz/";
        FtpConnection c = new FtpConnection();
        c.server = ftp_con_serv;
        c.port = 21;
        c.username = ftp_con_login;
        c.password = ftp_con_pass;

        FtpAction a = new FtpAction();
        a.success = true;
        a.connection = c;
        // a.remoteFilename = ftp_put;
        new FtpAsyncTask_RN_Up(context_Activity).execute(a);

    }


}
