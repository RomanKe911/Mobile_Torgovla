package kg.roman.Mobile_Torgovla;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterAde_List_Zalaz_Content;
import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_List_Zakaz_Content;
import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterAde_List_RN_Table;
import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_List_RN_Table;

import kg.roman.Mobile_Torgovla.FTP.FtpAction;
import kg.roman.Mobile_Torgovla.FTP.FtpAsyncTask_RN_Up;
import kg.roman.Mobile_Torgovla.FTP.FtpConnection;

import kg.roman.Mobile_Torgovla.Permission.PrefActivity;
import kg.roman.Mobile_Torgovla.Spravochnik.SPR_Debet;
import kg.roman.Mobile_Torgovla.Spravochnik.SPR_Kalkulator;
import kg.roman.Mobile_Torgovla.Spravochnik.SPR_Nomenclature_Brends;
import kg.roman.Mobile_Torgovla.Spravochnik.SPR_Ostatok_Single;
import kg.roman.Mobile_Torgovla.Spravochnik.SPR_Printer;
import kg.roman.Mobile_Torgovla.Spravochnik.SPR_Srtih_Activity;
import kg.roman.Mobile_Torgovla.FormaZakazaSelectClient.WJ_Forma_Zakaza_L1;
import kg.roman.Mobile_Torgovla.Work_Journal.WJ_Global_Activity;
import kg.roman.Mobile_Torgovla.Work_Journal.WJ_Dialog_RN_Screen;

public class Work_Table_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public ArrayList<ListAdapterSimple_List_Zakaz_Content> list_zakaz = new ArrayList<ListAdapterSimple_List_Zakaz_Content>();
    public ListAdapterAde_List_Zalaz_Content adapterPriceClients;

    public ArrayList<ListAdapterSimple_List_RN_Table> zakaz = new ArrayList<ListAdapterSimple_List_RN_Table>();
    public ListAdapterAde_List_RN_Table adapterPriceClients_RN;

    public TextView tvw_data_start, tvw_data_end;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now, this_data_work_now, this_vrema_work_now;
    Calendar dateAndTime = Calendar.getInstance();
    public String thisMONTH, thisDay;

    int year;
    int month;
    int day;

    public TextView textView;
    public Integer thisdata, thismonth, thisyear;
    public String Base;
    public Integer DIALOG_DATE, DIALOG_DATE_LOAD;
    public Toolbar toolbar;
    public ListView listView;
    public Calendar localCalendar = Calendar.getInstance();
    public String data_base, kod_RN;
    public EditText editText_kol;
    public Context context_Activity;
    Context mainContext;

    public Button button_ok;
    public Button button_up, button_down;
    public Button button_Go;
    public Button button_cancel;
    public String text_kod, text_name, text_cena, text_box;

    public String kkmyDB, table_name, mass, thismonth_rn;
    public String[] mass_RN;
    public String[] mass_month;
    public String[] mass_load;
    public String index_rn_1, index_rn_2, index_rn_3, name_rn, name_klients_rn;
    public Integer selectedCount;
    public String DATABASE_NAME_MONTH, DATABASE_NAME_DATA, BaseTableName_Reg;
    public SharedPreferences sp;
    public String PARAMS_KON_NAME, PARAMS_KON_KOD, PARAMS_KOD_SUMMA, REGIONS,
            data_base_table, DB_UP, PARAMS_KOD_DATA_UP, NAME_AGENT, NAME_KLIENTS;
    public TextView textView_TT, textView_Summa, textView_Itogo;

    public File fileName;
    public FileOutputStream fos;
    public File sdPath, sdPath_UP;
    public String new_write;
    String title;
    String where;
    private String mailhost = "smtp.gmail.com";
    private String mailhost2 = "smtp.mail.ru";
    public String ftp_put, ftp_server, login, password;
    public SharedPreferences.Editor ed;

    // android.content.Context mainContext;
    public String PEREM_Agent, PEREM_KAgent, PEREM_KAgent_UID, PEREM_Vrema, PEREM_Data, PEREM_RNKod, Summa;
    String from, PARAMS;
    String attach, text;
    public String local_text;
    public View localView;
    public androidx.appcompat.app.AlertDialog.Builder dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_table_user);

        context_Activity = Work_Table_Activity.this;
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        PEREM_Agent = sp.getString("PEREM_Agent", "0");
        PEREM_KAgent = sp.getString("PEREM_KAgent", "0");
        PEREM_KAgent_UID = sp.getString("PEREM_KAgent_UID", "0");
        PEREM_Vrema = sp.getString("PEREM_Vrema", "0");
        PEREM_Data = sp.getString("PEREM_Data", "0");
        PEREM_RNKod = sp.getString("PEREM_RNKod", "0");


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Рабочий стол");
        getSupportActionBar().setSubtitle(this_data_now);

        tvw_data_start = (TextView) findViewById(R.id.textView_this_Date_first);
        tvw_data_end = (TextView) findViewById(R.id.textView_this_Date_end);

        textView_TT = (TextView) findViewById(R.id.textView_dr_Kons_Kol);
        textView_Summa = (TextView) findViewById(R.id.textView_dr_Summa_Kons);
        textView_Itogo = (TextView) findViewById(R.id.textView_Param_Itogo);
        listView = (ListView) findViewById(R.id.ListView_Data);

        Calendate_New();
        tvw_data_start.setText(this_data_now);
        tvw_data_end.setText(this_data_now);


        zakaz.clear();
        Loading_RN(this_rn_data, this_rn_data);
        Loading_RN_AutoSumma(this_rn_data, this_rn_data);
        adapterPriceClients_RN = new ListAdapterAde_List_RN_Table(Work_Table_Activity.this, zakaz);
        adapterPriceClients_RN.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients_RN);


        Constant();

      /*  Calendare_This_Data();
        Create_Table_RN();*/


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                local_text = ((TextView) view.findViewById(R.id.tvw_rn_RN)).getText().toString();
                Toast.makeText(context_Activity, local_text, Toast.LENGTH_SHORT).show();
                Log.e(Work_Table_Activity.this.getLocalClassName(), local_text);

                ed = sp.edit();
                ed.putString("PEREM_LISTVIEW_ID", local_text);
                ed.commit();

                Intent intent = new Intent(context_Activity, WJ_Dialog_RN_Screen.class);
                startActivity(intent);
                finish();

                //  view = LayoutInflater.from(context_Activity).inflate(R.layout.list_zakaz_content, null);

             /*   TextView tbw = view.findViewById(R.id.cont_kod);
                Log.e(Work_Table_Activity.this.getLocalClassName(), tbw.getText().toString());*/

                //  String lst_tw_name = ((TextView) view.findViewById(R.id.cont_kod)).getText().toString();
                //  Log.e(Work_Table_Activity.this.getLocalClassName(), lst_tw_name);



            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            Calendate_New();
            tvw_data_start.setText(this_data_now);
            tvw_data_end.setText(this_data_now);
            zakaz.clear();
            Loading_RN(this_rn_data, this_rn_data);
            Loading_RN_AutoSumma(this_rn_data, this_rn_data);
            adapterPriceClients_RN = new ListAdapterAde_List_RN_Table(Work_Table_Activity.this, zakaz);
            adapterPriceClients_RN.notifyDataSetChanged();
            listView.setAdapter(adapterPriceClients_RN);

           /* Calendare_This_Data();
            Create_Table();
            list_zakaz.clear();
            ListInsert_Up();
            ListAdapet_Zakaz_Content();
            ListAdapet_Summa_TT();
            adapterPriceClients = new ListAdapterAde_List_Zalaz_Content(Work_Table_Activity.this, list_zakaz);
            adapterPriceClients.notifyDataSetChanged();
            listView.setAdapter(adapterPriceClients);*/
        } catch (Exception e) {
            Toast.makeText(Work_Table_Activity.this, "Выберите рабочую дату", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent_prefActivity = new Intent(context_Activity, PrefActivity.class);
            startActivity(intent_prefActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_gr1_table1: {  //Форма заказа
                Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza_L1.class);
                startActivity(intent);
                finish();
            }
            break;

            case R.id.nav_gr1_table2: { //Расходные накладные
                Calendare_Data();
            }
            break;
            case R.id.nav_gr1_table3: { //Взаиморасчеты
                Intent intent = new Intent(context_Activity, SPR_Debet.class);
                startActivity(intent);
                // Toast.makeText(context_Activity, "Временно не доступно!", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.nav_gr1_table3_1: { //Приходный ордер
                /*Intent intent = new Intent(context_Activity, Forma_Cash_Up.class);
                startActivity(intent);*/
                // Toast.makeText(context_Activity, "Временно не доступно!", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.nav_gr1_table4: { //Активности
               Intent intent = new Intent(context_Activity, WJ_Global_Activity.class);
                startActivity(intent);
                Toast.makeText(context_Activity, "ПРинтер", Toast.LENGTH_SHORT).show();
            }
            break;

            case R.id.nav_gr2_table1: { //Номенклатура
                Intent intent_forma = new Intent(Work_Table_Activity.this, SPR_Nomenclature_Brends.class);
                startActivity(intent_forma);
            }
            break;
            case R.id.nav_gr2_table5: { //Штрих-код
                Intent intent = new Intent(context_Activity, SPR_Srtih_Activity.class);
               // Intent intent = new Intent(context_Activity, SPR_Strih_Kod_Search.class);
                startActivity(intent);
            }
            break;

            case R.id.nav_gr2_table6: { //Остатки пользователя
                Intent intent = new Intent(context_Activity, SPR_Ostatok_Single.class);
                startActivity(intent);
            }
            break;

            case R.id.nav_gr2_table2: { //Клиенты
              /*  Intent intent_forma = new Intent(context_Activity, SPR_ContrAgents.class);
                startActivity(intent_forma);*/
            }
            break;
            case R.id.nav_gr2_table3: { //Выставленность
                Toast.makeText(context_Activity, "Временно не доступно!", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.nav_gr2_table4: { //Калькулятор
                Intent intent_forma = new Intent(Work_Table_Activity.this, SPR_Kalkulator.class);
                startActivity(intent_forma);
            }
            break;
            case R.id.nav_gr2_table7: { //Принтер
                Intent intent_forma = new Intent(Work_Table_Activity.this, SPR_Printer.class);
                startActivity(intent_forma);
            }
            break;

            case R.id.nav_gr3_table1: { //Выгрузка
                //Loading_UP_DATA();

                ListAdapet_Internet_Load();
                Mail();
                sender_mail_async async_sending = new sender_mail_async();
                async_sending.execute();
                Loading_1();
                Toast.makeText(context_Activity, "Выгрузка завершенна!", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.nav_gr3_table2: { //Загрузка
                Toast.makeText(context_Activity, "Временно не доступно!", Toast.LENGTH_SHORT).show();
            }
            break;

            case R.id.menu1_5: {
                DIALOG_DATE = Integer.valueOf(Calendar.DATE);
                PARAMS = "UP";
                textView.setText(thisdata + "." + thismonth + "." + thisyear);
                showDialog(DIALOG_DATE.intValue());
            }
            break;

            default:
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void Loading_UP_DATA() {
        try {
            Loading_1();
            ListAdapet_Internet_Load();
            Mail();
            sender_mail_async async_sending = new sender_mail_async();
            async_sending.execute();
            Toast.makeText(context_Activity, "Выгрузка завершенна!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка выгрузка данных", Toast.LENGTH_SHORT).show();
            Log.e(this.getLocalClassName(), "Ошибка выгрузка данных");
        }


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
                title = "Агент: " + PEREM_Agent;
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
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        REGIONS = sp.getString("REGIONS", "0");
        Log.e("Exsel", REGIONS);
        Toast.makeText(context_Activity, REGIONS, Toast.LENGTH_SHORT).show();

        ftp_server = "176.123.246.244";
        login = "sunbell_siberica";
        password = "Roman911NFS";
        // ftp_put = "/Server/Forma_Zakaz/OctRN0011.xls";
        ftp_put = "/Server/Forma_Zakaz/";

        FtpConnection c = new FtpConnection();
        c.server = ftp_server;
        c.port = 21;
        c.username = login;
        c.password = password;

        FtpAction a = new FtpAction();
        a.success = true;
        a.connection = c;
        // a.remoteFilename = ftp_put;

        new FtpAsyncTask_RN_Up(Work_Table_Activity.this).execute(a);

    }

    protected void Constant() {
        ftp_server = "176.123.246.244";
        login = "sunbell_siberica";
        password = "Roman911NFS";

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        DATABASE_NAME_MONTH = sp.getString("DATABASE_NAME_MONTH", "0");
        DATABASE_NAME_DATA = sp.getString("DATABASE_NAME_DATA", "0");
        BaseTableName_Reg = sp.getString("REGIONS", "0");
        PARAMS_KON_NAME = sp.getString("PARAMS_KON_NAME", "0");
        PARAMS_KON_KOD = sp.getString("PARAMS_KON_KOD", "0");
        PARAMS_KOD_SUMMA = sp.getString("PARAMS_KOD_SUMMA", "0");
        REGIONS = sp.getString("REGIONS", "0");
        PARAMS_KOD_DATA_UP = sp.getString("PARAMS_KOD_DATA_UP", "0");
        NAME_AGENT = sp.getString("NAME_AGENT", "0");
        DB_UP = "base_" + REGIONS + "_rn_data.db3";
    }

    public void onBackPressed() {
        dialog = new androidx.appcompat.app.AlertDialog.Builder(context_Activity);
        dialog.setTitle("Вы хотите выйти из программы?");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                finish();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
            }
        });
        dialog.show();
    }

    protected void Mail() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        /*
        localIntent = new Intent(android.content.Intent.ACTION_SEND);
        localIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"kerkin911@mail.ru"});
        localIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Агент: " + NAME_AGENT);
        localIntent.putExtra(Intent.EXTRA_TEXT, new_write.toString());
        localIntent.setType("plain/text");
        startActivity(Intent.createChooser(localIntent, "Отправка заказа..."));*/


        /*for (int i = 0; i<mass_load.length; i++)
        {

        }*/







        /*sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + "/" + "DCIM/e2.xls");
        File localFile = Environment.getExternalStorageDirectory();
          sdPath_UP = new File(localFile.getAbsolutePath() + "/" + "Price");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        new_write = ("----------" + "yut" + "----------");
        new_write = (new_write + "\nТорговая точка: ");
        new_write = (new_write + "\nСумма: " + "c");
        new_write = (new_write + "\nСкидка: ");
        new_write = (new_write + "\nИтого: " + "c");
        new_write = (new_write + "\nДата доставки: " + this.thisdata + "." + (1 + this.thismonth.intValue()) + "." + this.thisyear);
        localIntent = new Intent(android.content.Intent.ACTION_SEND);
        localIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"kerkin911@mail.ru"});
        localIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Форма заказ(Экспедитор:Роман)");
        localIntent.putExtra(android.content.Intent.EXTRA_TEXT, new_write.toString());
        localIntent.setType("plain/text");
       // localIntent.putExtra(Intent.EXTRA_STREAM,Uri.parse("file://" + sdPath_UP));
        startActivity(Intent.createChooser(localIntent, "Отправка письма..."));*/


        //localIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse(("file://" + sdPath_UP + "/OctRN0005xls")+("file://" + sdPath_UP + "/OctRN0006.xls")));





      /*  localIntent.putExtra("android.intent.extra.STREAM", Uri.parse("file://" + sdPath_UP + "/e2.xls"));
        localIntent.putExtra("android.intent.extra.STREAM", Uri.parse("file://" + sdPath_UP + "/Oct01.xls"));*/

       /* for (int i=1; i<=mass_load.length-1; i++) {
            localIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse("file://" + sdPath_UP + "/"+mass_load[i]+".xls"));
        }*/
        /*for (int i=1; i<=mass_load.length-1; i++) {
            localIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse("file://" + sdPath_UP + "/" + mass_load[i] + ".xls"));
        }*/

        // for (int i=1; i<=mass_load.length-1; i++)
        // localIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse("file://"  + sdPath_UP + "/OctRN0006.xls"));

       /* for (int i=1; i<=mass_load.length-1; i++)
        localIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory() + "/Price/"+mass_load[i]+".xls"));*/

        // Uri imagePath_phone = Uri.parse("android.resource://kg.price_list.roman_kerkin.sunbell_price/drawable/" + objects.get(pos).getImage());
        // Uri uri_path = Uri.parse(context.getPackageName()+"/drawable/"+objects.get(pos).getImage());
        //   localIntent.putExtra("android.intent.extra.STREAM", Uri.parse("android.resource://com.example.roman.sunbell_forma_for_zakaz/" + "/sdcard/DCIM/e2.xls"));
        //  localIntent.putExtra("android.intent.extra.STREAM", Uri.parse("file://" + sdPath + "/e2.xls"));

        //
       /* localIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse("file://"  + Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/e2.xls"));
        localIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse(FileProvider.getUriForFile( context_Activity,"com.example.roman.sunbell_forma_for_zakaz" + "/DCIM/e2.xls"));*/
        // localIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse(FileProvider.getUriForFile(context_Activity, "com.example.roman.sunbell_forma_for_zakaz", uri_path)));


        //  localIntent.putExtra("android.intent.extra.STREAM", Uri.parse(Environment.getExternalStorageDirectory()+ "/" + "DCIM" + "/e2.xls"));

        // localIntent.putExtra("android.intent.extra.STREAM", Uri.parse(sdPath + "/OctRN0015.xls"));

       /* for (int i=1; i<=mass_load.length-1; i++)
        {
           // localIntent.putExtra("android.intent.extra.STREAM", Uri.parse("file://" + sdPath + "/"+mass_load[i]));
          //  localIntent.putExtra("android.intent.extra.STREAM", Uri.parse(sdPath + "/"+mass_load[i]+".xls\""));
            localIntent.putExtra("android.intent.extra.STREAM", Uri.parse(sdPath + "/OctRN0015.xls"));
        }
       // localIntent.putExtra("android.intent.extra.STREAM", Uri.parse("file://" + sdPath + "/e2.xls"));*/

         /* ArrayList<Uri> uris = new ArrayList<Uri>();
        //String[] filePaths = new String[] {"file://" + sdPath_UP + "/OctRN0005xls", "file://" + sdPath_UP + "/OctRN0006xls"};
        String[] filePaths = new String[] {"sdcard/cat2_db.jpg", "sdcard/cat5_db.jpg"};
        for (String file : filePaths)
        {
            File fileIn = new File(file);
            Uri u = Uri.fromFile(fileIn);
            uris.add(u);
        }
        localIntent.putParcelableArrayListExtra(android.content.Intent.EXTRA_STREAM, uris);*/


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
        String dateString_NOW = dateFormat1.format(calendar.getTime());
        String dateString_WORK = dateFormat2.format(calendar.getTime());
        this_data_now = dateString_NOW;
        this_data_work_now = dateString_WORK;
        this_vrema_work_now = this_rn_vrema;
        Log.e(this.getLocalClassName(), "Рабочий день на:" + dateString_NOW);
        //  Log.e("WJ_FormaL2:", "!DataEnd:" + dateString2);

    } // Загрузка даты и время


    protected void Loading_RN(final String data_start, final String data_end) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_rn_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base_RN_All.Agent, base_RN_All.Kod_RN, base_RN_All.Klients, " +
                "base_RN_All.UID_Klients, base_RN_All.Vrema, base_RN_All.Data, base_RN_All.Summa, base_RN_All.Summa_2 " +
                "FROM base_RN_All; ";

        String query_V = "SELECT base_RN_All.Agent, base_RN_All.Kod_RN, base_RN_All.Klients, " +
                "base_RN_All.UID_Klients, base_RN_All.Vrema, base_RN_All.Data, base_RN_All.Summa, base_RN_All.Summa_2 " +
                "FROM base_RN_All " +
                "WHERE base_RN_All.Data LIKE'%" + this_rn_data + "%' " +
                "ORDER BY base_RN_All.Data;";

        String query_SORT = "SELECT base_RN_All.Agent, base_RN_All.Kod_RN, base_RN_All.Klients, " +
                "base_RN_All.UID_Klients, base_RN_All.Vrema, base_RN_All.Data, base_RN_All.Adress, " +
                "base_RN_All.Summa, base_RN_All.Summa_2\n" +
                "FROM base_RN_All " +
                "WHERE base_RN_All.Agent LIKE '%" + PEREM_Agent + "%' " +
                "AND base_RN_All.Data BETWEEN \"" + data_start + "\" AND \"" + data_end + "\"";

        String query_SUMMA = "SELECT base_RN_All.Agent, base_RN_All.Kod_RN, " +
                "base_RN_All.Klients, base_RN_All.UID_Klients, base_RN_All.Vrema, " +
                "base_RN_All.Data, SUM(base_RN_All.Summa) AS \"Summa\", SUM(base_RN_All.Summa_2) AS \"Summa_2\"\n" +
                "FROM base_RN_All " +
                "WHERE base_RN_All.Agent LIKE '%" + PEREM_Agent + "%' AND base_RN_All.Data BETWEEN \"" + data_start + "\" AND \"" + data_end + "\"";

        final Cursor cursor = db.rawQuery(query_SORT, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String Agent = cursor.getString(cursor.getColumnIndex("Agent"));
            String Kod_RN = cursor.getString(cursor.getColumnIndex("Kod_RN"));
            String Klients = cursor.getString(cursor.getColumnIndex("Klients"));
            String UID_Klients = cursor.getString(cursor.getColumnIndex("UID_Klients"));
            String Vrema = cursor.getString(cursor.getColumnIndex("Vrema"));
            String Data = cursor.getString(cursor.getColumnIndex("Data"));
            String Summa = cursor.getString(cursor.getColumnIndex("Summa"));
            String Itogo = cursor.getString(cursor.getColumnIndex("Summa_2"));
            String Adress = cursor.getString(cursor.getColumnIndex("Adress"));
            String skidka = cursor.getString(cursor.getColumnIndex("skidka")); // добавить обработку скидки
            String status = cursor.getString(cursor.getColumnIndex("status")); // добавить обработку скидки
            String debet= cursor.getString(cursor.getColumnIndex("debet_new")); // добавить обработку скидки
            String sklad= cursor.getString(cursor.getColumnIndex("sklad")); // добавить обработку скидки
            String skladUID= cursor.getString(cursor.getColumnIndex("sklad_uid")); // добавить обработку скидки
            zakaz.add(new ListAdapterSimple_List_RN_Table(Kod_RN, Klients, UID_Klients, Vrema, Data, Summa, Itogo, Adress, skidka, debet, status, sklad, skladUID));
            cursor.moveToNext();
        }

        cursor.close();
        db.close();
    }

    protected void Loading_RN_AutoSumma(final String data_start, final String data_end) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_rn_db.db3", MODE_PRIVATE, null);

        String query_SUMMA = "SELECT base_RN_All.Agent, base_RN_All.Kod_RN, " +
                "base_RN_All.Klients, base_RN_All.UID_Klients, base_RN_All.Vrema, base_RN_All.Adress, " +
                "base_RN_All.Data, SUM(base_RN_All.Summa) AS \"Summa\", SUM(base_RN_All.Summa_2) AS \"Summa_2\"\n" +
                "FROM base_RN_All " +
                "WHERE base_RN_All.Agent LIKE '%" + PEREM_Agent + "%' AND base_RN_All.Data BETWEEN \"" + data_start + "\" AND \"" + data_end + "\"";

        final Cursor cursor = db.rawQuery(query_SUMMA, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String Summa = cursor.getString(cursor.getColumnIndex("Summa"));
            String Itogo = cursor.getString(cursor.getColumnIndex("Summa_2"));
            // zakaz.add(new ListAdapterSimple_List_RN_Table(Kod_RN, Klients, UID_Klients, Vrema, Data, Summa, Itogo));
            if (Summa != null) {
                textView_Summa.setText(Summa);
                textView_Itogo.setText(Itogo);
            } else {
                textView_Summa.setText("0.0");
                textView_Itogo.setText("0.0");
            }

            cursor.moveToNext();
        }

        String query_KolTT = "SELECT DISTINCT base_RN_All.Klients " +
                "FROM base_RN_All " +
                "WHERE base_RN_All.Agent LIKE '%" + PEREM_Agent + "%' AND base_RN_All.Data BETWEEN \"" + data_start + "\" AND \"" + data_end + "\"";

        final Cursor cursor_TT = db.rawQuery(query_KolTT, null);
        cursor_TT.moveToFirst();
        while (cursor_TT.isAfterLast() == false) {
            String Klients = cursor_TT.getString(cursor_TT.getColumnIndex("Klients"));

            cursor_TT.moveToNext();
        }
        if (cursor_TT.getCount() > 0) {
            textView_TT.setText(String.valueOf(cursor_TT.getCount()));
        } else textView_TT.setText("0");

        /*Log.e("WJ_FormaL2:", "TT:" + cursor_TT.getPosition());
        Log.e("WJ_FormaL2:", "TT2:" + cursor_TT.getCount());*/


        cursor_TT.close();
        cursor.close();
        db.close();
    }

    // установка начальных даты и времени
    private void setInitialDateTime() {
        tvw_data_start.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_NUMERIC_DATE));

        Date currentTime = Calendar.getInstance().getTime();

        DateFormat df_year = new SimpleDateFormat("yyyy");
        DateFormat df_month = new SimpleDateFormat("MM");
        DateFormat df_day = new SimpleDateFormat("dd");
        Log.e(this.getLocalClassName(), "New=" + df_year.format(currentTime) + "," + df_month.format(currentTime) + "," + df_day.format(currentTime));

        String monthRN, dayRN;
        if (Integer.valueOf(dateAndTime.get(Calendar.DAY_OF_MONTH)) < 10) {
            dayRN = "0" + dateAndTime.get(Calendar.DAY_OF_MONTH);

        } else dayRN = "" + (dateAndTime.get(Calendar.DAY_OF_MONTH));

        if (Integer.valueOf(dateAndTime.get(Calendar.MONTH)) < 10) {
            monthRN = "0" + dateAndTime.get(Calendar.MONTH + 1);

        } else monthRN = "" + (dateAndTime.get(Calendar.MONTH + 1));

        Log.e(this.getLocalClassName(), "RN:" + dateAndTime.get(Calendar.YEAR) + "-" + monthRN + "-" + dayRN);

       /* Log.e("WJ_FormaL2:", "RN3:" + DateUtils.FORMAT_SHOW_YEAR + "-" + DateUtils.FORMAT_ABBREV_MONTH + "-" + DateUtils.FORMAT_NUMERIC_DATE);
        Log.e("WJ_FormaL2:", "RN_New:" + dateAndTime.get(Integer.parseInt(df_year.format(year))) + "-" + (dateAndTime.get(Calendar.MONTH)) + "-" + dateAndTime.get(Calendar.DAY_OF_MONTH));*/
        //   Log.e("WJ_FormaL2:", "RN:"+dateAndTime.get(Calendar.YEAR)+"-"+(dateAndTime.get(Calendar.MONTH))+"-"+dateAndTime.get(Calendar.DAY_OF_MONTH));
        //    tvw_data_start.setText(DateUtils.formatDateTime(context_Activity, dateAndTime.getTimeInMillis(), Integer.valueOf(this_rn_year) | Integer.valueOf(this_rn_month) | Integer.valueOf(this_rn_day)));
    }


    protected void Calendare_Data() {

        final Calendar c = Calendar.getInstance();
        int mYear, mMonth, mDay;
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

                        String dateString = dateFormat.format(calendar.getTime());
                        String dateString_start = dateFormat2.format(calendar.getTime());
                        String dateString_end = this_rn_data;
                        Log.e(Work_Table_Activity.this.getLocalClassName(), "DataStart:" + dateString_start);
                        Log.e(Work_Table_Activity.this.getLocalClassName(), "DataEnd" + dateString_end);

                        tvw_data_start.setText(dateString);
                        tvw_data_end.setText(this_data_now);


                        zakaz.clear();
                        Loading_RN(dateString_start, dateString_end);
                        Loading_RN_AutoSumma(dateString_start, dateString_end);
                        adapterPriceClients_RN = new ListAdapterAde_List_RN_Table(Work_Table_Activity.this, zakaz);
                        adapterPriceClients_RN.notifyDataSetChanged();
                        listView.setAdapter(adapterPriceClients_RN);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    protected void ListAdapet_Internet_Load() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_rn_db.db3", MODE_PRIVATE, null);
          /*      String query = "SELECT base_RN_All.Data, base_RN_All.Agent, base_RN_All.Kod_RN, base_RN_All.Klients, base_RN_All.Adress, base_RN_All.Summa, base_RN_All.Summa_2\n" +
                "FROM base_RN_All\n" +
                "WHERE base_RN_All.Agent LIKE '%Керкин_Роман%'";*/
    /*    String query = "SELECT base_RN_All.Data, base_RN_All.Agent, base_RN_All.Kod_RN, base_RN_All.Klients, base_RN_All.Adress, base_RN_All.Summa, base_RN_All.Summa_2\n" +
                "FROM base_RN_All\n" +
                "WHERE base_RN_All.Agent LIKE '%Керкин_Роман%' AND base_RN_All.Data LIKE '%" + this_data_work_now + "%'";*/
        Log.e(this.getLocalClassName(), this_data_work_now);


        String query = "SELECT base_RN_All.Data, base_RN_All.Agent, base_RN_All.Kod_RN, base_RN_All.Klients, base_RN_All.Adress, base_RN_All.Summa, base_RN_All.Summa_2\n" +
                "FROM base_RN_All\n" +
                "WHERE base_RN_All.Agent LIKE '%" + PEREM_Agent + "%' AND base_RN_All.Data LIKE '%" + this_data_work_now + "%'";

        new_write = ("");
        new_write = (new_write + "");
        new_write = (new_write + "Дата загрузки: " + this_data_now + " | " + this_vrema_work_now);
        //  new_write = (new_write + "Дата загрузки: " + this_data_work_now);
        new_write = (new_write + "\n");
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String kod_rn = cursor.getString(cursor.getColumnIndex("Kod_RN"));
            String klients = cursor.getString(cursor.getColumnIndex("Klients"));
            String Adress = cursor.getString(cursor.getColumnIndex("Adress"));
            String Summa = cursor.getString(cursor.getColumnIndex("Summa"));
            String Itogo = cursor.getString(cursor.getColumnIndex("Summa_2"));

            new_write = (new_write + "\nКод: " + kod_rn);
            new_write = (new_write + "\nКлиент:" + klients + ", Адрес: " + Adress + ", Сумма: " + Summa + ", Итого: " + Itogo);
            new_write = (new_write + "\n");
            cursor.moveToNext();
        }
        cursor.close();
        db.close();



     /*   selectedCount = cursor.getCount();
        mass_load = new String[selectedCount + 2];

        String query = "SELECT " + DbContract_Date.TableUser._ID + ", "
                + DbContract_Date.TableUser.COLUMN_KODRN + ", "
                + DbContract_Date.TableUser.COLUMN_NAME_T + ", "
                + DbContract_Date.TableUser.COLUMN_SUMMA + ", "
                + DbContract_Date.TableUser.COLUMN_SKIDKA + ", "
                + DbContract_Date.TableUser.COLUMN_DATA + ", "
                + DbContract_Date.TableUser.COLUMN_SUMMA_K + " FROM " + data_base_table;
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        selectedCount = cursor.getCount();
        mass_load = new String[selectedCount + 2];
        while (cursor.isAfterLast() == false) {
            String kod = cursor.getString(cursor.getColumnIndex(DbContract_Date.TableUser.COLUMN_KODRN));
            String name = cursor.getString(cursor.getColumnIndex(DbContract_Date.TableUser.COLUMN_NAME_T));
            String summa = cursor.getString(cursor.getColumnIndex(DbContract_Date.TableUser.COLUMN_SUMMA));
            String skidka = cursor.getString(cursor.getColumnIndex(DbContract_Date.TableUser.COLUMN_SKIDKA));
            String itogo = cursor.getString(cursor.getColumnIndex(DbContract_Date.TableUser.COLUMN_SUMMA_K));
            String data = cursor.getString(cursor.getColumnIndex(DbContract_Date.TableUser.COLUMN_DATA));
            mass_load[cursor.getPosition()] = kod;
            new_write = (new_write + "\nКод: " + kod);
            new_write = (new_write + "\nКлиент:" + name + ", Сумма:" + summa);
            new_write = (new_write + "\nДата поставки: " + data);
            new_write = (new_write + "\n");
            cursor.moveToNext();
        }
        cursor.close();
        db.close();*/
    }


}

