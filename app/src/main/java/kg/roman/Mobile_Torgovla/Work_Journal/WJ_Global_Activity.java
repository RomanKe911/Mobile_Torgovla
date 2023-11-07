package kg.roman.Mobile_Torgovla.Work_Journal;

import android.app.ProgressDialog;
import android.content.ContentValues;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import kg.roman.Mobile_Torgovla.DB_NewSV.DbContract_GroupID;
import kg.roman.Mobile_Torgovla.DB_NewSV.DbContract_Image;
import kg.roman.Mobile_Torgovla.DB_NewSV.DbContract_Nomeclature;
import kg.roman.Mobile_Torgovla.FTP.FtpAction;
import kg.roman.Mobile_Torgovla.FTP.FtpAsyncTask_RN_Up;
import kg.roman.Mobile_Torgovla.FTP.FtpAsyncTask_Single_Conn_UpServ;
import kg.roman.Mobile_Torgovla.FTP.FtpConnection;
import kg.roman.Mobile_Torgovla.FTP.ITTN_Obmen;
import kg.roman.Mobile_Torgovla.FTP.PDF_Create;
import kg.roman.Mobile_Torgovla.FTP.Sunbell_FtpAction;
import kg.roman.Mobile_Torgovla.FTP.Sunbell_FtpAsyncTask;
import kg.roman.Mobile_Torgovla.FTP.Sunbell_FtpAsyncTask_Filial;
import kg.roman.Mobile_Torgovla.FTP.Sunbell_FtpAsyncTask_SOS_FILES;
import kg.roman.Mobile_Torgovla.FTP.Sunbell_FtpConnection;
import kg.roman.Mobile_Torgovla.FTP.UpdateImage;
import kg.roman.Mobile_Torgovla.FormaZakaza.WJ_FormaZakaza_2023;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_DB_Data;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Data;
import kg.roman.Mobile_Torgovla.MailSenderClass;
import kg.roman.Mobile_Torgovla.Permission.PrefActivity;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.Report.Otchet_Brends_Trade;
import kg.roman.Mobile_Torgovla.Report.Otchet_Debet;
import kg.roman.Mobile_Torgovla.Report.Otchet_EveryDay;
import kg.roman.Mobile_Torgovla.Spravochnik.SPR_Agents;
import kg.roman.Mobile_Torgovla.Spravochnik.SPR_Cnt_Agent;
import kg.roman.Mobile_Torgovla.Spravochnik.SPR_Kalkulator;
import kg.roman.Mobile_Torgovla.Spravochnik.SPR_Nomenclature_Brends;
import kg.roman.Mobile_Torgovla.Spravochnik.SPR_Ostatok_Golovnoy;
import kg.roman.Mobile_Torgovla.Spravochnik.SPR_Ostatok_Inventoriz_NEW;
import kg.roman.Mobile_Torgovla.Spravochnik.SPR_Ostatok_Single;
import kg.roman.Mobile_Torgovla.Spravochnik.SPR_Strih_Kod_Search;
import kg.roman.Mobile_Torgovla.TEST.WJ_Forma_Zakaza_Akcia;
import kg.roman.Mobile_Torgovla.TEST.WJ_Fragment;
import kg.roman.Mobile_Torgovla.TEST.WJ_Karakil_newRN;
import kg.roman.Mobile_Torgovla.TEST.WJ_TEST_ADMIN;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_CustomersDebet_ResourceParser;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Customers;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_CustomersDebet;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Customers_ResourceParser;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Nomenclatures;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Nomenclatures_ResourceParser;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Ostatok;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Ostatok_ResourceParser;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Price;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Price_ResourceParser;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Skald_ResourceParser;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Sklad;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_SubBrends;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_SubBrends_ResourceParser;

import static kg.roman.Mobile_Torgovla.Permission.PrefActivity_Splash.calculateDirectoryInfo;

public class WJ_Global_Activity extends AppCompatActivity implements View.OnClickListener {

    public ArrayList<ListAdapterSimple_Data> product_str = new ArrayList<ListAdapterSimple_Data>();
    public ListAdapterAde_DB_Data adapterPriceClients;

    public Button btn_forma, btn_kassa, btn_otchet, btn_ost, btn_kontr, btn_agent, btn_nomencl,
            btn_kalkul, btn_strih, btn_load, btn_update, btn_up;
    public ProgressDialog pDialog;

    public Context context_Activity;
    public Intent intent_start_act;
    public int max_count = 0;
    public Double perm, summa;
    public String PARAMS_KON_NAME, PARAMS_KON_KOD, PARAMS_KOD_SUMMA, REGIONS, DB_UP, PARAMS_KOD_DATA_UP, NAME_AGENT, PEREM_KOD_BRENDS_VISIBLE;
    public String new_write, ftp_con_serv, ftp_con_login, ftp_con_pass, ftp_con_put, name_db, data_ostatok;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now, this_data_work_now, this_vrema_work_now;
    public String PEREM_Agent, PEREM_KAgent, PEREM_KAgent_UID, PEREM_Vrema, PEREM_Data, PEREM_RNKod, PEREM_TYPE;
    public String DATABASE_NAME_MONTH, DATABASE_NAME_DATA, BaseTableName_Reg;
    public String from, PARAMS, attach, text, title, where;
    public String table_name, newName, brends;
    public Integer count_tt;
    public String[] mass, mass_data, mass_breds_group, mass_MTW, mass_SQL, mass_update_files;
    public String mass_ID[][];

    public ContentValues localContentValues, localContentValues_Osn, localContentValues_Region, localContentValues_All;
    public ContentValues localContentValues_Brends, localContentValues_Breds_ID, localContentValues_sub_Breds_ID;
    public androidx.appcompat.app.AlertDialog.Builder dialog_cancel;

    public Cursor cursor, cursor_ID, cursor_sklad, cursor_w;
    public AlertDialog.Builder builder;
    public AlertDialog dialog;
    public String PEREM_Type, PEREM_Distr, PEREM_DST_ContrAg, UID_Agent, myDB;
    public XmlPullParser xpp;

    public SharedPreferences.Editor ed;
    public SharedPreferences sp;
    public String PEREM_FTP_SERV, PEREM_FTP_LOGIN, PEREM_FTP_PASS, PEREM_FTP_DISTR_XML, PEREM_FTP_DISTR_db3, PEREM_FTP_DISTR_sos,
            PEREM_IMAGE_PUT_SDCARD, PEREM_IMAGE_PUT_PHONE;
    public String PEREM_MAIL_LOGIN, PEREM_MAIL_PASS, PEREM_MAIL_START, PEREM_MAIL_END,
            PEREM_DB3_CONST, PEREM_DB3_BASE, PEREM_DB3_RN, PEREM_ANDROID_ID_ADMIN, PEREM_ANDROID_ID;
    public String PEREM_AG_UID, PEREM_AG_NAME, PEREM_AG_REGION, PEREM_AG_UID_REGION, PEREM_AG_CENA,
            PEREM_AG_SKLAD, PEREM_AG_UID_SKLAD, PEREM_AG_TYPE_REAL, PEREM_AG_TYPE_USER,
            PEREM_WORK_DISTR, PEREM_KOD_MOBILE, PEREM_FTP_PUT;
    public String BASE_DB_OSTATKI_BISHKEK = "sunbell_ostatki_filial.db3";
    public String Save_dialog_up_, grb;
    public String data_mail_login, data_mail_pass, data_mail_where, data_mail_from;
    public Boolean syns_end = true;
    public OutputStream outputStream;
    public String files_size, files_kol;


    public String tovar_uid, tovar_count;
    public String new_count_tovar, new_count_sklad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_admin);
        context_Activity = WJ_Global_Activity.this;
        intent_start_act = null;
        Constanta_Read();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon_image);
        getSupportActionBar().setTitle("Рабочий стол");
        getSupportActionBar().setSubtitle(PEREM_AG_NAME);


        ed = sp.edit();
        PEREM_Agent = sp.getString("PEREM_Agent", "0");
        PEREM_KAgent = sp.getString("PEREM_KAgent", "0");
        PEREM_KAgent_UID = sp.getString("PEREM_KAgent_UID", "0");
        PEREM_Vrema = sp.getString("PEREM_Vrema", "0");
        PEREM_Data = sp.getString("PEREM_Data", "0");
        PEREM_RNKod = sp.getString("PEREM_RNKod", "0");
        PEREM_TYPE = sp.getString("PEREM_Type", "0");


        btn_forma = (Button) findViewById(R.id.forma_title_2_forma);
        btn_kassa = (Button) findViewById(R.id.forma_title_2_kassa);
        btn_otchet = (Button) findViewById(R.id.forma_title_2_otchet);
        btn_ost = (Button) findViewById(R.id.forma_title_2_ostatki);

        btn_kontr = (Button) findViewById(R.id.forma_title_2_k_agent2);
        btn_agent = (Button) findViewById(R.id.forma_title_2_agent2);
        btn_nomencl = (Button) findViewById(R.id.forma_title_2_tovar2);

        btn_kalkul = (Button) findViewById(R.id.forma_title_3_kalkul);
        btn_strih = (Button) findViewById(R.id.forma_title_3_shtrih);

        btn_load = (Button) findViewById(R.id.forma_title_4_load);
        btn_update = (Button) findViewById(R.id.forma_title_4_update);
        btn_up = (Button) findViewById(R.id.forma_title_4_up);

        Calendate_New();
        Constant();

        btn_forma.setOnClickListener(this);
        btn_kassa.setOnClickListener(this);
        btn_otchet.setOnClickListener(this);
        btn_ost.setOnClickListener(this);
        btn_kontr.setOnClickListener(this);
        btn_agent.setOnClickListener(this);
        btn_nomencl.setOnClickListener(this);
        btn_kalkul.setOnClickListener(this);
        btn_strih.setOnClickListener(this);
        btn_load.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_up.setOnClickListener(this);

    /*   btn_strih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });*/


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

    public void onBackPressed() {
        dialog_cancel = new androidx.appcompat.app.AlertDialog.Builder(context_Activity);
        dialog_cancel.setTitle("Вы хотите выйти из программы?");
        dialog_cancel.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                finish();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
            }
        });
        dialog_cancel.show();
    }


    @Override
    public void onClick(View v) {
        ed = sp.edit();
        ed.putString("PEREM_FORMA_TYPE_DIALOG", "");  // передача кода конечной даты
        ed.commit();
        if (syns_end == true) {
            switch (v.getId()) {
                ///////////////////////////////////////////////////////////////// Рабочий стол, 1 ряд /////////////////
                // Форма заказа
                case R.id.forma_title_2_forma: {
                    intent_start_act = null;
                    intent_start_act = new Intent(context_Activity, WJ_Forma_Zakaza.class); // Создание новой формы 23.10.2023
                    //  intent_start_act = new Intent(context_Activity, WJ_FormaZakaza_2023.class);
                    startActivity(intent_start_act);
                    Constanta_Write();
                    finish();
                }
                break;

                // Отчет по кассе
                case R.id.forma_title_2_kassa: {
                    intent_start_act = null;
                    ed = sp.edit();
                    ed.putString("PEREM_FORMA_TYPE_DIALOG", "KASSA");  // передача кода конечной даты
                    ed.commit();
                    builder = new AlertDialog.Builder(this);
                    builder.setTitle("Касса...");
                    builder.setIcon(R.drawable.office_title_money);
                    product_str.clear();
                    mass = getResources().getStringArray(R.array.mass_for_kassa);
                    mass_data = new String[mass.length];

                    for (int i = 0; i < mass.length; i++) {
                        product_str.add(new ListAdapterSimple_Data(mass[i], sp.getString("Sync_Data_" + i, "0")));
                    }
                    adapterPriceClients = new ListAdapterAde_DB_Data(context_Activity, product_str);
                    adapterPriceClients.notifyDataSetChanged();

                    builder.setAdapter(adapterPriceClients, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.e("Tag", "Selected item on position " + mass[which]);
                            Kassa_DB(mass[which]);
                        }
                    });
                    dialog = builder.create();
                    dialog.show();
                }
                break;

                // Отчет по фирме
                case R.id.forma_title_2_otchet: {
                    intent_start_act = null;
                    ed = sp.edit();
                    ed.putString("PEREM_FORMA_TYPE_DIALOG", "OTCHET");  // передача кода конечной даты
                    ed.commit();
                    builder = new AlertDialog.Builder(this);
                    builder.setTitle("Журнал отчетов...");
                    builder.setIcon(R.drawable.office_title_forma);
                    product_str.clear();
                    mass = getResources().getStringArray(R.array.mass_for_otchet);
                    mass_data = new String[mass.length];
                    for (int i = 0; i < mass.length; i++) {
                        product_str.add(new ListAdapterSimple_Data(mass[i], sp.getString("Sync_Data_" + i, "0")));
                    }
                    adapterPriceClients = new ListAdapterAde_DB_Data(context_Activity, product_str);
                    adapterPriceClients.notifyDataSetChanged();

                    builder.setAdapter(adapterPriceClients, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.e("Tag", "Selected item on position " + mass[which]);
                            Otchet_DB(mass[which]);
                        }
                    });
                    dialog = builder.create();
                    dialog.show();
                }
                break;

                // Остатки товара по фирме
                case R.id.forma_title_2_ostatki: {
                    intent_start_act = null;
                    ed = sp.edit();
                    ed.putString("PEREM_FORMA_TYPE_DIALOG", "OSTATOK");  // передача кода конечной даты
                    ed.commit();
                    builder = new AlertDialog.Builder(this);
                    builder.setTitle("Остаток...");
                    builder.setIcon(R.drawable.office_title_forma_2);
                    product_str.clear();
                    mass = getResources().getStringArray(R.array.mass_for_ostatok);
                    mass_data = new String[mass.length];

                    for (int i = 0; i < mass.length; i++) {
                        product_str.add(new ListAdapterSimple_Data(mass[i], sp.getString("Sync_Data_" + i, "0")));
                    }
                    adapterPriceClients = new ListAdapterAde_DB_Data(context_Activity, product_str);
                    adapterPriceClients.notifyDataSetChanged();

                    builder.setAdapter(adapterPriceClients, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.e("Tag", "Selected item on position " + mass[which]);
                            Ostatki_DB(mass[which]);
                        }
                    });
                    dialog = builder.create();
                    dialog.show();
                }
                break;


                ///////////////////////////////////////////////////////////////// Рабочий стол, 2 ряд /////////////////
                //Справочник Контрагенты
                case R.id.forma_title_2_k_agent2:
                    intent_start_act = null;
                    intent_start_act = new Intent(context_Activity, SPR_Cnt_Agent.class);
                    //  intent_start_act = new Intent(context_Activity, WJ_New_Contragent.class);
                    startActivity(intent_start_act);
                    ed = sp.edit();
                    ed.putString("PEREM_LIST_ADAPTER_DEBET", "list");
                    ed.commit();

                    break;

                //Справочник сотрудники
                case R.id.forma_title_2_agent2:
                    intent_start_act = null;
                    intent_start_act = new Intent(context_Activity, SPR_Agents.class);
                    startActivity(intent_start_act);
                    break;

                //Справочник Номенклатура
                case R.id.forma_title_2_tovar2:
                    intent_start_act = null;
                    intent_start_act = new Intent(context_Activity, SPR_Nomenclature_Brends.class);
                    // intent_start_act = new Intent(context_Activity, SPR_Nomenclature_Brends_New.class);
                    startActivity(intent_start_act);
                    break;

                ///////////////////////////////////////////////////////////////// Рабочий стол, 3 ряд /////////////////

                // Купюрник
                case R.id.forma_title_3_kalkul: {
                    intent_start_act = null;
                    intent_start_act = new Intent(context_Activity, SPR_Kalkulator.class);
                    startActivity(intent_start_act);

                }
                break;

                // Поиск по штрих=коду
                case R.id.forma_title_3_shtrih: {
                    intent_start_act = null;
                    intent_start_act = new Intent(context_Activity, SPR_Strih_Kod_Search.class);
                    startActivity(intent_start_act);
                }
                break;

                ///////////////////////////////////////////////////////////////// Рабочий стол, 4 ряд /////////////////


                case R.id.forma_title_4_up: //Выгрузить данные на сервер
                    Toast.makeText(context_Activity, "Временно не работает, выгрузка данных производится из \"формы заказа\"", Toast.LENGTH_SHORT).show();
                  /*  try {
                        ListAdapet_Internet_Load();
                        Mail();
                        WJ_Global_Activity.sender_mail_async async_sending = new WJ_Global_Activity.sender_mail_async();
                        async_sending.execute();
                        Loading_FTP_Zakaz("", "");
                        Toast.makeText(context_Activity, "Выгрузка завершенна!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(context_Activity, "Ошибка выгузки данных на сервер!", Toast.LENGTH_SHORT).show();
                        Log.e("Error_Up ", "Ошибка выгузки данных на сервер!");
                    }*/
                    break;

                // Обновление данных с сервера FTP, файлы XML и DB3
                case R.id.forma_title_4_update: //Обновление данных с сервера
                    try {
                        ed = sp.edit();
                        ed.putString("PEREM_FORMA_TYPE_DIALOG", "SYNC");  // передача кода конечной даты
                        ed.commit();
                        builder = new AlertDialog.Builder(this);
                        builder.setTitle("Обновление данных...");
                        builder.setIcon(R.drawable.icons_ftp_up);
                        product_str.clear();
                        mass = getResources().getStringArray(R.array.mass_for_update_data);
                        for (int i = 0; i < mass.length; i++) {
                            product_str.add(new ListAdapterSimple_Data(mass[i], sp.getString("Save_dialog_up_" + i, "0")));
                        }
                        adapterPriceClients = new ListAdapterAde_DB_Data(context_Activity, product_str);
                        adapterPriceClients.notifyDataSetChanged();

                        builder.setAdapter(adapterPriceClients, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Log.e("Tag", "Selected item on position " + mass[which]);
                                Updata_OF_FTP_TO_MT(mass[which]);
                            }
                        });
                        dialog = builder.create();
                        dialog.show();
                    } catch (Exception e) {
                        Toast.makeText(context_Activity, "Ошибка обновления данных!", Toast.LENGTH_SHORT).show();
                        Log.e("Error_Update: ", "Ошибка обновления данных!");
                    }
                    break;

                //  Синхронизирование данных из готовых файлов XML в DB3
                case R.id.forma_title_4_load:
                    try {
                        ed = sp.edit();
                        ed.putString("PEREM_FORMA_TYPE_DIALOG", "UPDATE");  // передача кода конечной даты
                        ed.commit();
                        builder = new AlertDialog.Builder(this);
                        builder.setTitle("Обновление данных...");
                        builder.setIcon(R.drawable.office_title_forma_update);
                        product_str.clear();
                        mass = getResources().getStringArray(R.array.mass_for_sync_data);
                        mass_data = new String[mass.length];
                        for (int i = 0; i < mass.length; i++) {
                            product_str.add(new ListAdapterSimple_Data(mass[i], sp.getString("Sync_Data_" + i, "0")));
                        }
                        adapterPriceClients = new ListAdapterAde_DB_Data(context_Activity, product_str);
                        adapterPriceClients.notifyDataSetChanged();

                        builder.setAdapter(adapterPriceClients, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("Tag", "Selected item on position " + mass[which]);
                                Sync_XML_TO_DB3(mass[which]);
                            }
                        });
                        dialog = builder.create();
                        dialog.show();
                    } catch (Exception e) {
                        Toast.makeText(context_Activity, "Ошибка синхронизации данных!", Toast.LENGTH_SHORT).show();
                        Log.e("Error_Update: ", "Ошибка синхронизации данных!");
                    }

                    break;
            }
        } else {
            Log.e("Pref", "Идет синхронизвация подождите!");
            Toast.makeText(context_Activity, "Идет синхронизвация подождите!", Toast.LENGTH_SHORT).show();
        }


    }
    /*
    Save_dialog_up_ + i



     */

    protected void Loading_Ostatok_Sync() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("ostatki_db.db3", MODE_PRIVATE, null);

        String query_up = "SELECT base4_ost.data, base4_ost.koduid, base4_ost.sklad, base4_ost.count\n" +
                "FROM base4_ost;";

        cursor = db.rawQuery(query_up, null);

        cursor.moveToPosition(2);
        String data = cursor.getString(cursor.getColumnIndexOrThrow("data"));

        String month = data.substring(0, data.indexOf('/'));
        String day = data.substring(data.indexOf('/') + 1, data.lastIndexOf('/'));
        String year = data.substring(data.lastIndexOf('/') + 1);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(year), Integer.valueOf(month) - 1, Integer.valueOf(day));
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
        String dateString_NOW = dateFormat1.format(calendar.getTime());

        data_ostatok = dateString_NOW;
        cursor.close();
        db.close();
    }  // Синхронизация данных остатков

    private class MyAsyncTask_Sync_Akcia extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            pDialog.setMessage("Синхронизация цен. Подождите...");
            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                publishProgress(1);
                getFloor();  // Синхронизация файлов для всех складов
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
            Log.e("ПОТОК=", "Конец потока");
            pDialog.setProgress(0);
            ed.putString("Data_update_db_price_sync", "Дата обновление: " + this_data_now + "\r\nАкции на: " + this_data_now); // обновление остатков
            ed.commit();

            //  pDialog.dismiss();
        }

        private void getFloor() throws InterruptedException {
            pDialog.setMessage("Загрузка продуктов. Подождите...");
            SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
            SQLiteDatabase db_second = getBaseContext().openOrCreateDatabase("aksia_db.db3", MODE_PRIVATE, null);

            db_prev.delete("base_aksia", null, null);

            String query = "SELECT brend, name_aks, name, kod_univ, kod_uid, cena, ostatok, image, par_uls, usl_osdo, usl_pres, skidka\n" +
                    "FROM table_1\n" +
                    "INNER JOIN table_2 ON table_1.catigory=table_2.catigory\n" +
                    "INNER JOIN table_3 ON table_1.catigory=table_3.catigory;";

            cursor = db_second.rawQuery(query, null);
            localContentValues = new ContentValues();
            BigDecimal f1 = new BigDecimal(0.0);
            BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(cursor.getCount()));
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String brend = cursor.getString(cursor.getColumnIndexOrThrow("brend"));
                String name_aks = cursor.getString(cursor.getColumnIndexOrThrow("name_aks"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String kod_univ = cursor.getString(cursor.getColumnIndexOrThrow("kod_univ"));
                String kod_uid = cursor.getString(cursor.getColumnIndexOrThrow("kod_uid"));
                String cena = cursor.getString(cursor.getColumnIndexOrThrow("cena"));
                String ostatok = cursor.getString(cursor.getColumnIndexOrThrow("ostatok"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                String par_uls = cursor.getString(cursor.getColumnIndexOrThrow("par_uls"));
                String usl_osdo = cursor.getString(cursor.getColumnIndexOrThrow("usl_osdo"));
                String usl_pres = cursor.getString(cursor.getColumnIndexOrThrow("usl_pres"));
                String skidka = cursor.getString(cursor.getColumnIndexOrThrow("skidka"));

                Log.e("База полная=", name_aks + ", Обновлено!");
                localContentValues.put("brend", brend);
                localContentValues.put("name_aks", name_aks);
                localContentValues.put("name", name);
                localContentValues.put("kod_univ", kod_univ);
                localContentValues.put("kod_uid", kod_uid);
                localContentValues.put("cena", cena);
                localContentValues.put("ostatok", ostatok);
                localContentValues.put("image", image);
                localContentValues.put("par_uls", par_uls);
                localContentValues.put("usl_osdo", usl_osdo);
                localContentValues.put("usl_pres", usl_pres);
                localContentValues.put("skidka", skidka);
                localContentValues.put("kol", "0");
                db_prev.insert("base_aksia", null, localContentValues);
                f1 = f1.add(pointOne);
                pDialog.setProgress(f1.intValue());
                cursor.moveToNext();
            }
            pDialog.dismiss();
            TimeUnit.SECONDS.sleep(1);
            cursor.close();
            db_prev.close();
            db_second.close();

        }  // Синхронизация файлов для всех складов

    }

    protected void Constant() {
        ftp_con_serv = "176.123.246.244";
        ftp_con_login = "sunbell_siberica";
        ftp_con_pass = "Roman911NFS";

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

        PEREM_Agent = sp.getString("PEREM_Agent", "0"); // передача кода агента (Фамилия_Имя)
        UID_Agent = sp.getString("UID_Agent", "0");     // передача кода агента (A8BA1F48-C7E1-497B-B74A-D86426684712)
        PEREM_Type = sp.getString("PEREM_Type", "0");   // передача кода агента (admin)
        PEREM_Distr = sp.getString("PEREM_DISTR", "0"); // имя папки с данными (XLS_KerkinR)
        PEREM_DST_ContrAg = sp.getString("PEREM_DIS_CONTRAG", "0");  // имя для доступа к контагентам (Торговый агент-1)
        UID_Agent = sp.getString("UID_AGENTS", "0");
        myDB = sp.getString("REGIONS", "0");
    }

    protected void DB_Image_Delete_Create() {

        SQLiteDatabase db_sqlite = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);

        final String DELETE_TABLE = "DROP TABLE base6_image";
        db_sqlite.execSQL(DELETE_TABLE);
        Log.e("ListView_Adapter_Image=", "Таблица base6_image удалена!");

        final String CREATE_TABLE = "CREATE TABLE if not exists base6_image" +
                "(" + DbContract_GroupID.TableUser._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + DbContract_Image.TableUser.COLUMN_KODUID + " TEXT, "
                + DbContract_Image.TableUser.COLUMN_KOD + " TEXT, "
                + DbContract_Image.TableUser.COLUMN_NAME + " TEXT, "
                + DbContract_Image.TableUser.COLUMN_IMAGE + " TEXT, " +
                "FOREIGN KEY (name)  REFERENCES base1_v8 (name))";

        Log.e("ListView_Adapter_Image=", "Таблица base6_image создана!");
        db_sqlite.execSQL(CREATE_TABLE);
        db_sqlite.close();
    }  // Очистка таблицы и создание раздела заново

    protected void ProgressDialog_massage() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Загрузка продуктов. Подождите...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setProgress(0);
        pDialog.setMax(100);
        pDialog.show();


        new Thread(new Runnable() {
            @Override
            public void run() {

                SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
                String query_ID = "SELECT " + DbContract_GroupID.TableUser.COLUMN_BRENDS + ", "
                        + DbContract_GroupID.TableUser.COLUMN_PER_KOD + ", "
                        + DbContract_GroupID.TableUser.COLUMN_PREF + ", "
                        + DbContract_GroupID.TableUser.COLUMN_WORK + ", "
                        + DbContract_GroupID.TableUser.COLUMN_GR_TYPE + " FROM base7_Brends_ID";

                cursor_ID = db.rawQuery(query_ID, null);
                mass_ID = new String[cursor_ID.getCount()][5];

                max_count = cursor_ID.getCount();
                perm = Double.valueOf(100 / max_count);
                summa = 0.0;

                cursor_ID.moveToFirst();
                while (cursor_ID.isAfterLast() == false) {
                    String brends = cursor_ID.getString(cursor_ID.getColumnIndexOrThrow(DbContract_GroupID.TableUser.COLUMN_BRENDS));
                    String kod = cursor_ID.getString(cursor_ID.getColumnIndexOrThrow(DbContract_GroupID.TableUser.COLUMN_PER_KOD));
                    String pref = cursor_ID.getString(cursor_ID.getColumnIndexOrThrow(DbContract_GroupID.TableUser.COLUMN_PREF));
                    String work = cursor_ID.getString(cursor_ID.getColumnIndexOrThrow(DbContract_GroupID.TableUser.COLUMN_WORK));
                    mass_ID[cursor_ID.getPosition()][2] = kod;
                    mass_ID[cursor_ID.getPosition()][3] = pref;
                    mass_ID[cursor_ID.getPosition()][4] = work;
                    Log.e("Do=", "Загрузка картинок");
                    cursor_ID.moveToNext();
                }
                cursor_ID.close();

                for (int i = 0; i < mass_ID.length; i++) {
                    String query = " SELECT base1_v8.koduid, base1_v8.kod, base1_v8.name, base2_sveta.kod_univ FROM base1_v8 " +
                            "LEFT JOIN base2_sveta ON base1_v8.name = base2_sveta.name " +
                            "WHERE base2_sveta.kod_univ LIKE '" + mass_ID[i][2] + "%'";
                    cursor = db.rawQuery(query, null);
                    cursor.moveToFirst();
                    localContentValues = new ContentValues();

                    while (cursor.isAfterLast() == false) {
                        String koduid = cursor.getString(cursor.getColumnIndexOrThrow(DbContract_Image.TableUser.COLUMN_KODUID));
                        String kod = cursor.getString(cursor.getColumnIndexOrThrow(DbContract_Image.TableUser.COLUMN_KOD));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(DbContract_Image.TableUser.COLUMN_NAME));
                        if (mass_ID[i][4].equals("true")) {
                            localContentValues.put(DbContract_Image.TableUser.COLUMN_KODUID, koduid);
                            localContentValues.put(DbContract_Image.TableUser.COLUMN_KOD, kod);
                            localContentValues.put(DbContract_Image.TableUser.COLUMN_NAME, name);
                            String prf = kod.substring(0, 1);
                            if (kod.contains("Ц")) {
                                newName = "";
                                newName = mass_ID[i][3] + "_c" + kod.substring(1, kod.length());
                                localContentValues.put(DbContract_Image.TableUser.COLUMN_IMAGE, newName);
                                db.insert("base6_image", null, localContentValues);
                                Log.d("New String", "ОБНОВЛЕННО " + newName);
                            } else if (kod.contains("С")) {
                                newName = "";
                                newName = mass_ID[i][3] + "_ck" + kod.substring(2, kod.length());
                                localContentValues.put(DbContract_Image.TableUser.COLUMN_IMAGE, newName);
                                db.insert("base6_image", null, localContentValues);
                                Log.d("New String", "ОБНОВЛЕННО " + newName);
                            } else if (prf.contains("К")) {
                                newName = "";
                                newName = mass_ID[i][3] + "_kp" + kod.substring(2, kod.length());
                                localContentValues.put(DbContract_Image.TableUser.COLUMN_IMAGE, newName);
                                db.insert("base6_image", null, localContentValues);
                                Log.d("New String", "ОБНОВЛЕННО " + newName);
                            } else if (prf.contains("Р")) {
                                newName = "";
                                newName = mass_ID[i][3] + "_pk" + kod.substring(2, kod.length());
                                localContentValues.put(DbContract_Image.TableUser.COLUMN_IMAGE, newName);
                                db.insert("base6_image", null, localContentValues);
                                Log.d("New String", "ОБНОВЛЕННО " + newName);
                            } else {
                                newName = "";
                                newName = mass_ID[i][3] + "_" + kod.toLowerCase();
                                localContentValues.put(DbContract_Image.TableUser.COLUMN_IMAGE, newName);
                                db.insert("base6_image", null, localContentValues);
                                Log.d("New String", "ОБНОВЛЕННО " + newName);
                            }
                            Log.e("mas1=", mass_ID[i][2] + ", " + name + ", Image=" + newName);
                            Log.e("mas2=", mass_ID[i][3] + ", " + name + ", Image=" + newName);
                            if (cursor.getPosition() == 1) {
                                pDialog.setProgress(summa.intValue());
                                summa = summa + perm;
                            }
                            cursor.moveToNext();
                        } else {
                            Log.e("mas1=", mass_ID[i][2] + ", " + name + ", Image=" + newName);
                            Log.e("mas2=", mass_ID[i][3] + ", " + name + ", Image=" + newName);
                            if (cursor.getPosition() == 1) {
                                pDialog.setProgress(summa.intValue());
                                summa = summa + perm;
                            }
                            cursor.moveToNext();
                        }
                    }
                }
                cursor.close();
                db.close();


                pDialog.dismiss();
            }
        }).start();
    }  // Загрузка с прогрессом

    protected void ListView_Adapter_Nomeclature_Work_Table() {
        SQLiteDatabase db_sqlite = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);

        final String CREATE_TABLE = "CREATE TABLE if not exists base10_nomeclature" +
                "(" + DbContract_Nomeclature.TableUser.COLUMN_BRENDS + " TEXT , "
                + DbContract_Nomeclature.TableUser.COLUMN_GROUP + " TEXT, "
                + DbContract_Nomeclature.TableUser.COLUMN_KOD + " TEXT, "
                + DbContract_Nomeclature.TableUser.COLUMN_NAME + " TEXT, "
                + DbContract_Nomeclature.TableUser.COLUMN_KOLBOX + " TEXT, "
                + DbContract_Nomeclature.TableUser.COLUMN_OSTATOK + " TEXT, "
                + DbContract_Nomeclature.TableUser.COLUMN_CENA + " TEXT, "
                + DbContract_Nomeclature.TableUser.COLUMN_IMAGE + " TEXT, "
                + DbContract_Nomeclature.TableUser.COLUMN_STRIH + " TEXT, "
                + DbContract_Nomeclature.TableUser.COLUMN_KODUNIV + " TEXT, "
                + DbContract_Nomeclature.TableUser.COLUMN_WORK + " TEXT, "
                + DbContract_Nomeclature.TableUser.COLUMN_KODUID + " TEXT PRIMARY KEY);";

        Log.e("ListView_Adapter_Image=", "Таблица base10_nomeclature создана!");
        db_sqlite.execSQL(CREATE_TABLE);


        final String DELETE_TABLE = "DROP TABLE base10_nomeclature";
        db_sqlite.execSQL(DELETE_TABLE);
        Log.e("ListView_Adapter_Image=", "Таблица base10_nomeclature удалена!");
        db_sqlite.execSQL(CREATE_TABLE);
        Log.e("ListView_Adapter_Image=", "Таблица base10_nomeclature создана!");
        db_sqlite.close();
    }  // Очистка таблицы и создание раздела заново

    protected void Loading_SOS() {
        ftp_con_put = "/MT_Sunbell_Karakol/MTW_SOS/" + PEREM_DB3_RN;
        FtpConnection c = new FtpConnection();
        c.server = ftp_con_serv;
        c.port = 21;
        c.username = ftp_con_login;
        c.password = ftp_con_pass;

        FtpAction a = new FtpAction();
        a.success = true;
        a.connection = c;
        a.remoteFilename = ftp_con_put;

        new FtpAsyncTask_Single_Conn_UpServ(WJ_Global_Activity.this).execute(a);
    }


    protected void Otchet_DB(String which) {
        mass = getResources().getStringArray(R.array.mass_for_otchet);
        String text_log_name = "В разработке";
        for (int i = 0; i < mass.length; i++) {
            if (which.equals(mass[i])) {
                switch (which) {
                    case "Продажи по брендам":            // отчет по продажам товара по брендам по отгрузкам
                    {
                        try {
                            // Toast.makeText(context_Activity, text_log_name, Toast.LENGTH_SHORT).show();
                            intent_start_act = null;
                            intent_start_act = new Intent(context_Activity, Otchet_Brends_Trade.class);
                            startActivity(intent_start_act);

                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка журнала");
                            Toast.makeText(context_Activity, "Ошибка журнала", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case "Отчет за день":            // отчет по продажам товара по брендам по отгрузкам
                    {
                        try {
                            // Toast.makeText(context_Activity, text_log_name, Toast.LENGTH_SHORT).show();
                            intent_start_act = null;
                            intent_start_act = new Intent(context_Activity, Otchet_EveryDay.class);
                            startActivity(intent_start_act);

                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка журнала");
                            Toast.makeText(context_Activity, "Ошибка журнала", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case "Отгрузки по дате": //Синхронизировать остатки
                    {
                        try {
                            Toast.makeText(context_Activity, text_log_name, Toast.LENGTH_SHORT).show();
                            /*intent_start_act = null;
                            intent_start_act = new Intent(context_Activity, WJ_Otchet_Prodaj.class);
                            startActivity(intent_start_act);*/

                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка журнала");
                            Toast.makeText(context_Activity, "Ошибка журнала", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;

                    case "Касса": //Синхронизировать остатки
                    {
                        try {
                            //  Toast.makeText(context_Activity, text_log_name, Toast.LENGTH_SHORT).show();
                            intent_start_act = null;
                            intent_start_act = new Intent(context_Activity, WJ_New_Contragent.class);
                            startActivity(intent_start_act);

                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка журнала");
                            Toast.makeText(context_Activity, "Ошибка журнала", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;

                    case "XML тест": //Синхронизировать остатки
                    {
                        try {
                            Toast.makeText(context_Activity, text_log_name, Toast.LENGTH_SHORT).show();
                            intent_start_act = null;
                            intent_start_act = new Intent(context_Activity, WJ_Karakil_newRN.class);
                            startActivity(intent_start_act);

                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка журнала");
                            Toast.makeText(context_Activity, "Ошибка журнала", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case "Печать тест": //Розница
                    {
                        try {
                            //  Toast.makeText(context_Activity, text_log_name, Toast.LENGTH_SHORT).show();
                            intent_start_act = null;
                            intent_start_act = new Intent(context_Activity, WJ_Smart_Voice.class);
                            startActivity(intent_start_act);

                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка журнала");
                            Toast.makeText(context_Activity, "Ошибка журнала", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;

                    case "Fragment тест": //Розница
                    {
                        try {
                            //  Toast.makeText(context_Activity, text_log_name, Toast.LENGTH_SHORT).show();
                            intent_start_act = null;
                            intent_start_act = new Intent(context_Activity, WJ_Fragment.class);
                            startActivity(intent_start_act);

                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка журнала");
                            Toast.makeText(context_Activity, "Ошибка журнала", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case "Акции тест": //Розница
                    {
                        try {
                            //  Toast.makeText(context_Activity, text_log_name, Toast.LENGTH_SHORT).show();
                            intent_start_act = null;
                            intent_start_act = new Intent(context_Activity, WJ_Forma_Zakaza_Akcia.class);
                            startActivity(intent_start_act);

                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка журнала");
                            Toast.makeText(context_Activity, "Ошибка журнала", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;

                    case "Акции": //Розница
                    {
                        try {

                            //   Toast.makeText(context_Activity, text_log_name, Toast.LENGTH_SHORT).show();

                            intent_start_act = null;
                            intent_start_act = new Intent(context_Activity, WJ_Forma_Zakaza_Akcia.class);
                            startActivity(intent_start_act);


                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка журнала");
                            Toast.makeText(context_Activity, "Ошибка журнала", Toast.LENGTH_SHORT).show();
                        }

                        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
                        String query_select = "SELECT DISTINCT const_sklad.sklad_uid, const_sklad.sklad_name FROM base_in_ostatok \n" +
                                "INNER JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid\n" +
                                "ORDER BY const_sklad.sklad_name;";
                        final Cursor cursor = db.rawQuery(query_select, null);
                        String[][] mass_sklad = new String[cursor.getCount()][2];
                        cursor.moveToFirst();
                        while (cursor.isAfterLast() == false) {
                            String sklad_name = cursor.getString(cursor.getColumnIndexOrThrow("sklad_name"));
                            String sklad_uid = cursor.getString(cursor.getColumnIndexOrThrow("sklad_uid"));
                            if (!sklad_uid.equals(PEREM_AG_UID_SKLAD)) {
                                mass_sklad[cursor.getPosition()][0] = sklad_name;
                                mass_sklad[cursor.getPosition()][1] = sklad_uid;
                               /* Log.e("1)Имя складов:", "-" + sklad_name);
                                Log.e("1)uid складов:", "-" + sklad_uid);*/
                                cursor.moveToNext();
                            } else cursor.moveToNext();

                        }
                        cursor.close();
                        db.close();


                        // tovar_uid = "14E86029-A4F4-4F7B-887E-2C78D10675E6";
                        tovar_uid = "43B3FD03-AB00-42B3-AB9F-960E5FA9614C";
                        tovar_count = "200";
                       /* Log.e("TT", "Имя:" + PEREM_AG_SKLAD);
                        Log.e("TT", "Имя:" + PEREM_AG_UID_SKLAD);*/

                        SQLiteDatabase db_sklad = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
                        String query_sklad = "SELECT * FROM base_in_ostatok\n" +
                                "JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid\n" +
                                "WHERE nomenclature_uid = '" + tovar_uid + "' AND base_in_ostatok.sklad_uid = '" + PEREM_AG_UID_SKLAD + "';";
                        cursor_sklad = db_sklad.rawQuery(query_sklad, null);
                        cursor_sklad.moveToFirst();
                        new_count_tovar = tovar_count;
                        new_count_sklad = "0";
                        if (cursor_sklad.getCount() != 0) {
                            String sklad_name = cursor_sklad.getString(cursor_sklad.getColumnIndexOrThrow("sklad_name"));
                            String sklad_uid = cursor_sklad.getString(cursor_sklad.getColumnIndexOrThrow("sklad_uid"));
                            String tovar_uid = cursor_sklad.getString(cursor_sklad.getColumnIndexOrThrow("nomenclature_uid"));
                            String count = cursor_sklad.getString(cursor_sklad.getColumnIndexOrThrow("count"));
                            new_count_tovar = String.valueOf(Integer.parseInt(tovar_count) - Integer.parseInt(tovar_count));
                            new_count_sklad = String.valueOf(Integer.parseInt(count) - Integer.parseInt(tovar_count));
                            Log.e("Поиск:", "Имя:" + sklad_name);
                            Log.e("Поиск:", "Склад:" + Integer.parseInt(count));
                            Log.e("Поиск:", "новый товар:" + new_count_tovar);
                            Log.e("Поиск:", "новый остаток:" + new_count_sklad);
                        } else {
                            /*Log.e("НТЕ", "Имя:");
                            Log.e("НТЕ", "Склад:");
                            Log.e("НТЕ", "Заказ:" + Integer.parseInt(tovar_count));*/
                            for (int k = 0; k < mass_sklad.length; k++) {
                                String query = "SELECT * FROM base_in_ostatok\n" +
                                        "JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid\n" +
                                        "WHERE nomenclature_uid = '" + tovar_uid + "' AND base_in_ostatok.sklad_uid = '" + mass_sklad[k][1] + "';";
                                cursor_sklad = db_sklad.rawQuery(query, null);
                                cursor_sklad.moveToFirst();

                                if (cursor_sklad.getCount() != 0) {
                                    String sklad_name = cursor_sklad.getString(cursor_sklad.getColumnIndexOrThrow("sklad_name"));
                                    String sklad_uid = cursor_sklad.getString(cursor_sklad.getColumnIndexOrThrow("sklad_uid"));
                                    String tovar_uid = cursor_sklad.getString(cursor_sklad.getColumnIndexOrThrow("nomenclature_uid"));
                                    String count = cursor_sklad.getString(cursor_sklad.getColumnIndexOrThrow("count"));
                                    if (Double.parseDouble(count) >= Double.parseDouble(tovar_count) & Double.parseDouble(new_count_tovar) != 0) {
                                        new_count_tovar = String.valueOf(Integer.parseInt(tovar_count) - Integer.parseInt(tovar_count));
                                        new_count_sklad = String.valueOf(Integer.parseInt(count) - Integer.parseInt(tovar_count));

                                        Log.e("Поиск:", "Имя:" + sklad_name);
                                        Log.e("Поиск:", "Склад:" + Integer.parseInt(count));
                                        Log.e("Поиск:", "новый товар:" + new_count_tovar);
                                        Log.e("Поиск:", "новый остаток:" + new_count_sklad);
                                    }

                                }

                            }


                        }
                    }
                    break;
                    case "XML": //Розница
                    {
                        try {
                            Toast.makeText(context_Activity, text_log_name, Toast.LENGTH_SHORT).show();

                            /*intent_start_act = null;
                            intent_start_act = new Intent(context_Activity, WJ_XML.class);
                            startActivity(intent_start_act);*/

                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка журнала");
                            Toast.makeText(context_Activity, "Ошибка журнала", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case "Администрирование": //Розница
                    {
                        try {
                            Toast.makeText(context_Activity, text_log_name, Toast.LENGTH_SHORT).show();

                            intent_start_act = null;
                            //   intent_start_act = new Intent(context_Activity, ListViewWithCheckboxActivity.class);
                            intent_start_act = new Intent(context_Activity, WJ_TEST_ADMIN.class);
                            startActivity(intent_start_act);

                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка журнала");
                            Toast.makeText(context_Activity, "Ошибка журнала", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;

                    case "Обработка с эттн": //Розница
                    {
                        try {
                            Toast.makeText(context_Activity, text_log_name, Toast.LENGTH_SHORT).show();

                            intent_start_act = null;
                            intent_start_act = new Intent(context_Activity, ITTN_Obmen.class);
                            startActivity(intent_start_act);

                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка журнала");
                            Toast.makeText(context_Activity, "Ошибка журнала", Toast.LENGTH_SHORT).show();
                        }
                    } break;
                    case "Обновление картинок": //Розница
                    {
                        try {
                            Toast.makeText(context_Activity, text_log_name, Toast.LENGTH_SHORT).show();

                            intent_start_act = null;
                            intent_start_act = new Intent(context_Activity, UpdateImage.class);
                            startActivity(intent_start_act);

                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка журнала");
                            Toast.makeText(context_Activity, "Ошибка журнала", Toast.LENGTH_SHORT).show();
                        }
                    } break;

                    case "Создание PDF": //Розница
                    {
                        try {
                            Toast.makeText(context_Activity, text_log_name, Toast.LENGTH_SHORT).show();

                            intent_start_act = null;
                            intent_start_act = new Intent(context_Activity, PDF_Create.class);
                            startActivity(intent_start_act);

                        } catch (Exception e) {
                            Log.e("Pref", "Создание PDF");
                            Toast.makeText(context_Activity, "Ошибка журнала", Toast.LENGTH_SHORT).show();
                        }
                    } break;
                    default:Toast.makeText(context_Activity, "нет выбранного значения", Toast.LENGTH_SHORT).show();break;
                }
            }
        }
    }

    protected void Kassa_DB(String which) {
        mass = getResources().getStringArray(R.array.mass_for_kassa);
        String text_log_name = "В разработке";
        for (int i = 0; i < mass.length; i++) {
            if (which.equals(mass[i])) {
                switch (which) {
                    case "Консигнации":                                  // Консигнации контрагентов
                    {
                        try {
                            intent_start_act = null;
                            intent_start_act = new Intent(context_Activity, Otchet_Debet.class);
                            ed = sp.edit();
                            ed.putString("PEREM_DEBET_CLIENT", "ONE");
                            ed.commit();
                            startActivity(intent_start_act);
                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка журнала");
                            Toast.makeText(context_Activity, "Ошибка журнала", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case "Неоплаченные консигнации":                     //Консигнации контрагентов детальный
                    {
                        try {
                            Toast.makeText(context_Activity, text_log_name, Toast.LENGTH_SHORT).show();
                           /* intent_start_act = null;
                            intent_start_act = new Intent(context_Activity, Otchet_Debet.class);
                            intent_start_act.putExtra("PEREM_DEBET_CLIENT", "ALL");
                            startActivity(intent_start_act);*/
                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка журнала");
                            Toast.makeText(context_Activity, "Ошибка журнала", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case "Возврат консигнаций":                          //Синхронизировать остатки
                    {
                        try {
                            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
                            Toast.makeText(context_Activity, text_log_name, Toast.LENGTH_SHORT).show();
                            String query = "ALTER TABLE base_RN_All ADD status TEXT;";
                            final Cursor cursor = db.rawQuery(query, null);
                            Log.e("Pref", "Добавлен столбец status");
                            cursor.moveToFirst();
                            while (cursor.isAfterLast() == false) {
                                cursor.moveToNext();
                            }
                            cursor.close();
                            db.close();
                         /*   intent_start_act = null;
                            intent_start_act = new Intent(context_Activity, SPR_Debet.class);
                            intent_start_act.putExtra("StarAct_1", "StarAct_1");
                            startActivity(intent_start_act);*/
                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка журнала");
                            Toast.makeText(context_Activity, "Ошибка журнала", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case "Наличный рассчет":                             //Синхронизировать остатки
                    {
                        try {
                            Toast.makeText(context_Activity, text_log_name, Toast.LENGTH_SHORT).show();
                         /*   intent_start_act = null;
                            intent_start_act = new Intent(context_Activity, SPR_Debet.class);
                            intent_start_act.putExtra("StarAct_1", "StarAct_2");
                            startActivity(intent_start_act);*/

                            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
                          /*  String query = "ALTER TABLE base_RN_All ADD status TEXT;";
                            final Cursor cursor = db.rawQuery(query, null);
                            Log.e("Pref", "Добавлен столбец status");
                            cursor.moveToFirst();
                            while (cursor.isAfterLast() == false) {
                                cursor.moveToNext();
                            }
                            cursor.close();*/

                            String query_2 = "ALTER TABLE base_RN_All ADD debet_new TEXT;";
                            final Cursor cursor2 = db.rawQuery(query_2, null);
                            Log.e("Pref", "Добавлен столбец status");
                            cursor2.moveToFirst();
                            while (cursor2.isAfterLast() == false) {
                                cursor2.moveToNext();
                            }
                            cursor2.close();
                            db.close();

                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка журнала");
                            Toast.makeText(context_Activity, "Ошибка журнала", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
            }
        }
    }

    protected void Ostatki_DB(String which) {
        mass = getResources().getStringArray(R.array.mass_for_ostatok);
        String text_log_name = "В разработке";
        for (int i = 0; i < mass.length; i++) {
            if (which.equals(mass[i])) {
                switch (which) {
                    case "Остатки по складу":   // Остатки филиала
                    {
                        try {
                            intent_start_act = null;
                            intent_start_act = new Intent(context_Activity, SPR_Ostatok_Single.class);
                            startActivity(intent_start_act);
                          /*  ed = sp.edit();
                            ed.putString("PEREM_ISNAME_SPINNER", "OSTATOK_ACTIVITY");  // передача кода конечной даты
                            ed.commit();*/
                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка журнала");
                            Toast.makeText(context_Activity, "Ошибка журнала", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case "Остатки по фирме":  // Остатки головного офиса
                    {
                        try {
                            intent_start_act = null;
                            intent_start_act = new Intent(context_Activity, SPR_Ostatok_Golovnoy.class);
                            startActivity(intent_start_act);
                          /*  ed = sp.edit();
                            ed.putString("PEREM_ISNAME_SPINNER", "OSTATOK_ACTIVITY");  // передача кода конечной даты
                            ed.commit();*/
                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка журнала");
                            Toast.makeText(context_Activity, "Ошибка журнала", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case "Инвентаризация":  // Инвентаризация
                    {
                        try {
                            Toast.makeText(context_Activity, text_log_name, Toast.LENGTH_SHORT).show();
                           /* intent_start_act = null;
                            intent_start_act = new Intent(context_Activity, SPR_Ostatok_Inventoriz_NEW.class);
                            startActivity(intent_start_act);*/
                          /*  ed = sp.edit();
                            ed.putString("PEREM_ISNAME_SPINNER", "OSTATOK_ACTIVITY");  // передача кода конечной даты
                            ed.commit();*/
                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка журнала, инвентаризация");
                            Toast.makeText(context_Activity, "Ошибка журнала, инвентаризация", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
            }
        }
    }


    // Синхронизация файлов XML в DB3
    protected void Sync_XML_TO_DB3(String which) {
        mass = getResources().getStringArray(R.array.mass_for_sync_data);
        for (int i = 0; i < mass.length; i++) {
            if (which.equals(mass[i])) {
                switch (which) {
                    case "Ежедневная синхронизация":                      //Ежедневная синхронизация
                    {
                        try {
                            Toast.makeText(context_Activity, which, Toast.LENGTH_SHORT).show();
                            Calendate_New();
                            Loading_Dialog_Message();
                            ed.putString("Sync_Data_" + i, this_data_now); // Сохранить дату синхронизации
                            ed.commit();
                            WJ_Global_Activity.MyAsyncTask_Sync_EveryDay asyncTask = new WJ_Global_Activity.MyAsyncTask_Sync_EveryDay(); // для Бишкека
                            asyncTask.execute();
                            syns_end = false;
                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка: Ежедневное обновление!");
                            Toast.makeText(context_Activity, "Ошибка: Ежедневное обновление!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case "Синхронизация базы товаров":                 //Синхронизация базы товаров
                    {
                        try {
                            Toast.makeText(context_Activity, which, Toast.LENGTH_SHORT).show();
                            Calendate_New();
                            Loading_Dialog_Message();
                            ed.putString("Sync_Data_" + i, this_data_now); // Сохранить дату синхронизации
                            ed.commit();
                            WJ_Global_Activity.MyAsyncTask_Sync_Tovar asyncTask = new WJ_Global_Activity.MyAsyncTask_Sync_Tovar(); // для Бишкека
                            asyncTask.execute();
                            syns_end = false;
                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка: Ежедневное обновление!");
                            Toast.makeText(context_Activity, "Ошибка: Ежедневное обновление!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case "Полная синхронизация баз":                      //Полная синхронизация баз
                    {
                        try {
                            Toast.makeText(context_Activity, which, Toast.LENGTH_SHORT).show();
                            Calendate_New();
                            Loading_Dialog_Message();
                            ed.putString("Sync_Data_" + i, this_data_now); // Сохранить дату синхронизации
                            ed.commit();
                            WJ_Global_Activity.MyAsyncTask_Sync_All asyncTask = new WJ_Global_Activity.MyAsyncTask_Sync_All(); // для Бишкека
                            asyncTask.execute();
                            syns_end = false;
                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка: Ежедневное обновление!");
                            Toast.makeText(context_Activity, "Ошибка: Ежедневное обновление!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                    case "Синхр. остатков(для филиалов)":            //Синхр. остатков(для филиалов)
                    {
                        try {
                            Toast.makeText(context_Activity, which, Toast.LENGTH_SHORT).show();
                            Calendate_New();
                            Toast.makeText(context_Activity, which, Toast.LENGTH_SHORT).show();
                            Loading_Dialog_Message();
                            ed.putString("Sync_Data_" + i, this_data_now);
                            ed.commit();
                            WJ_Global_Activity.MyAsyncTask_Sync_Ostatki_Golovnoy asyncTask = new WJ_Global_Activity.MyAsyncTask_Sync_Ostatki_Golovnoy();
                            asyncTask.execute();
                            syns_end = false;
                        } catch (Exception e) {
                            Log.e("Pref", "Ошибка: Ежедневное обновление!");
                            Toast.makeText(context_Activity, "Ошибка: Ежедневное обновление!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;

                    case "Резервное копирование баз": //Обновить отчеты
                    {
                        Toast.makeText(context_Activity, which, Toast.LENGTH_SHORT).show();
                        ed.putString("Sync_Data_" + i, this_data_now);
                        ed.commit();

                        Calendate_New();
                        Constant();
                        ed = sp.edit();
                        ed.putString("Data_ftp_out_1", "suncape_all_db.db3"); // передача имя файла для отправки
                        ed.putString("Data_ftp_out_2", "suncape_rn_db.db3"); // передача имя файла для отправки
                        ed.commit();
                        // Loading_SOS();

                        String[] f = {"а", "б", "в", "г", "д", "е", "ё", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п", "р", "с", "т", "у", "ф", "х", "ч", "ц", "ш", "щ", "э", "ю", "я", "ы", "ъ", "ь"};
                        String[] t = {"a", "b", "v", "g", "d", "e", "io", "zh", "z", "i", "i", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ch", "c", "sh", "csh", "e", "ju", "ja", "i", "", ""};

                        String res = "";
                        String name_old = PEREM_AG_NAME.toLowerCase().replaceAll(" ", "_");


                        for (int s = 0; s < name_old.length(); ++s) {
                            String add = name_old.substring(s, s + 1);
                            for (int j = 0; j < f.length; j++) {
                                if (f[j].equals(add)) {
                                    add = t[j];
                                    break;
                                }
                            }
                            res += add;
                        }
                        // Log.e("new^ ", res);

                        String[] mass_files_SOS = getResources().getStringArray(R.array.mass_files_SQLITE_DB);

                        for (int q = 0; q < mass_files_SOS.length; q++) {
                            String new_name = mass_files_SOS[q].substring(0, mass_files_SOS[q].length() - 4);
                            //Log.e("NewName", new_name); // Код и ревизия
                            FTP_Connect_Filial_SOS(mass_files_SOS[q], PEREM_FTP_DISTR_sos + "/" + this_data_now + "_" + new_name + "_" + res + ".db3");  // /MT_Sunbell_Karakol
                        }

                        ListAdapet_Internet_Load_SOS();
                        Mail();
                        WJ_Global_Activity.sender_mail_async_SOS async_sending = new WJ_Global_Activity.sender_mail_async_SOS();
                        async_sending.execute();

                        Toast.makeText(context_Activity, "Резервное копирование выполненно!", Toast.LENGTH_SHORT).show();


                    }
                    break;
                }
            }
        }

    }

    // Обновление данных с сервера FTP, файлы XML и DB3
    protected void Updata_OF_FTP_TO_MT(String which) {
        mass = getResources().getStringArray(R.array.mass_for_update_data);
        for (int i = 0; i < mass.length; i++) {
            if (which.equals(mass[i])) {
                switch (which) {
                    case "Ежедневное обновление": {
                        Toast.makeText(context_Activity, which, Toast.LENGTH_SHORT).show();
                        //Обновление файлов: "MTW_In_Price.xml", "MTW_In_ResidueGoodsPR.xml", "MTW_In_CustomersDebet.xml", "MTW_In_PaymentOrders.xml"
                        mass_update_files = new String[]{""};
                        mass_update_files = new String[]{"MTW_In_Price.xml", "MTW_In_ResidueGoodsPR.xml", "MTW_In_CustomersDebet.xml", "MTW_In_PaymentOrders.xml"};

                        Calendate_New();
                        ed.putString("Save_dialog_up_" + i, this_data_now); // Сохранить дату обновление остатков
                        ed.commit();

                        Loading_Dialog_Message();
                        WJ_Global_Activity.MyAsyncTask_Update_File_MTW asyncTask = new WJ_Global_Activity.MyAsyncTask_Update_File_MTW();
                        asyncTask.execute();
                    }
                    break;
                    case "Обновление базы товаров": //Обновить файл MTW_In_Price.xml файл цен
                    {
                        Toast.makeText(context_Activity, which, Toast.LENGTH_SHORT).show();
                        //Обновление файлов: "MTW_In_Price.xml", "MTW_In_ResidueGoodsPR.xml", "MTW_In_CustomersDebet.xml", "MTW_In_PaymentOrders.xml"
                        mass_update_files = new String[]{""};
                        mass_update_files = new String[]{"MTW_In_Price.xml", "MTW_In_ResidueGoodsPR.xml", "MTW_In_Nomenclatures.xml", "MTW_In_Brends.xml"};

                        Calendate_New();
                        ed.putString("Save_dialog_up_" + i, this_data_now); // Сохранить дату обновление остатков
                        ed.commit();

                        Loading_Dialog_Message();
                        WJ_Global_Activity.MyAsyncTask_Update_File_MTW asyncTask = new WJ_Global_Activity.MyAsyncTask_Update_File_MTW();
                        asyncTask.execute();
                    }
                    break;
                    case "Полное обновление файлов": {
                        Toast.makeText(context_Activity, which, Toast.LENGTH_SHORT).show();
                        //Обновление файлов: "Все файлы XML"
                        mass_update_files = new String[]{""};
                        mass_update_files = getResources().getStringArray(R.array.mass_files_MTW);

                        Calendate_New();
                        ed.putString("Save_dialog_up_" + i, this_data_now); // Сохранить дату обновление остатков
                        ed.commit();

                        Loading_Dialog_Message();
                        WJ_Global_Activity.MyAsyncTask_Update_File_MTW asyncTask = new WJ_Global_Activity.MyAsyncTask_Update_File_MTW();
                        asyncTask.execute();
                    }
                    break;
                    case "Обновление базы данных": {
                        Toast.makeText(context_Activity, which, Toast.LENGTH_SHORT).show();
                        //Обновление файлов: "Все файлы SQL"
                        mass_update_files = new String[]{""};
                        mass_update_files = getResources().getStringArray(R.array.mass_files_SQLITE_DB_2);

                        Calendate_New();
                        ed.putString("Save_dialog_up_" + i, this_data_now); // Сохранить дату обновление остатков
                        ed.commit();

                        Loading_Dialog_Message();
                        WJ_Global_Activity.MyAsyncTask_Update_File_SQLite asyncTask = new WJ_Global_Activity.MyAsyncTask_Update_File_SQLite();
                        asyncTask.execute();
                    }
                    break;
                    case "Остатки Бишкек(для филиалов)": {
                        Toast.makeText(context_Activity, which, Toast.LENGTH_SHORT).show();
                        //Обновление файлов: "Все файлы XML"
                        mass_update_files = new String[]{""};
                        mass_update_files = new String[]{"MTW_In_Price.xml", "MTW_In_ResidueGoodsPR.xml", "MTW_In_Nomenclatures.xml", "MTW_In_Brends.xml"};

                        Calendate_New();
                        ed.putString("Save_dialog_up_" + i, this_data_now); // Сохранить дату обновление остатков
                        ed.commit();

                        Loading_Dialog_Message();
                        WJ_Global_Activity.MyAsyncTask_Update_File_MTW_BIshkek asyncTask = new WJ_Global_Activity.MyAsyncTask_Update_File_MTW_BIshkek();
                        asyncTask.execute();
                    }
                    break;

                }
            }
        }

    }

    // Загрузка даты и время
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
    }

    // Константы для чтения
    protected void Constanta_Read() {
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        PEREM_FTP_SERV = sp.getString("PEREM_FTP_SERV", "0");                    //чтение данных: имя сервера
        PEREM_FTP_LOGIN = sp.getString("PEREM_FTP_LOGIN", "0");                  //чтение данных: имя логин
        PEREM_FTP_PASS = sp.getString("PEREM_FTP_PASS", "0");                    //чтение данных: имя пароль
        PEREM_FTP_DISTR_XML = sp.getString("PEREM_FTP_DISTR_XML", "0");          //чтение данных: путь к файлам XML
        PEREM_FTP_DISTR_db3 = sp.getString("PEREM_FTP_DISTR_db3", "0");          //чтение данных: путь к файлам DB3
        PEREM_FTP_DISTR_sos = sp.getString("PEREM_FTP_DISTR_sos_files", "0");                //чтение данных: Универсальный номер пользователя
        PEREM_IMAGE_PUT_SDCARD = sp.getString("PEREM_IMAGE_PUT_SDCARD", "0");    // путь картинок на телефоне /sdcard/Price/Image/
        PEREM_IMAGE_PUT_PHONE = sp.getString("PEREM_IMAGE_PUT_PHONE", "0");      // Путь картинок в др. приложении android.resource://kg.price_list.roman_kerkin.sunbell_price/drawable/

        PEREM_MAIL_LOGIN = sp.getString("PEREM_MAIL_LOGIN", "0");                //чтение данных: для почты логин
        PEREM_MAIL_PASS = sp.getString("PEREM_MAIL_PASS", "0");                  //чтение данных: для почты пароль
        PEREM_MAIL_START = sp.getString("PEREM_MAIL_START", "0");                //чтение данных: для почты от кого
        PEREM_MAIL_END = sp.getString("PEREM_MAIL_END", "0");                    //чтение данных: для почты от кому
        PEREM_DB3_CONST = sp.getString("PEREM_DB3_CONST", "0");                  //чтение данных: Путь к базам данных с константами
        PEREM_DB3_BASE = sp.getString("PEREM_DB3_BASE", "0");                    //чтение данных: Путь к базам данных с товаром
        PEREM_DB3_RN = sp.getString("PEREM_DB3_RN", "0");                        //чтение данных: Путь к базам данных с накладными
        PEREM_ANDROID_ID_ADMIN = sp.getString("PEREM_ANDROID_ID_ADMIN", "0");    //чтение данных: Универсальный номер для админа
        PEREM_ANDROID_ID = sp.getString("PEREM_ANDROID_ID", "0");                //чтение данных: Универсальный номер пользователя
        PEREM_KOD_BRENDS_VISIBLE = sp.getString("PEREM_KOD_BRENDS_VISIBLE", "0");                //чтение данных: Универсальный номер пользователя
        Log.e("PEREM_KOD_BRENDS_VISIB", PEREM_KOD_BRENDS_VISIBLE);


        PEREM_AG_UID = sp.getString("PEREM_AG_UID", "0");                         //чтение данных: передача кода агента (A8BA1F48-C7E1-497B-B74A-D86426684712)
        PEREM_AG_NAME = sp.getString("PEREM_AG_NAME", "0");                       //чтение данных: передача кода агента (Имя пользователя)
        PEREM_AG_REGION = sp.getString("PEREM_AG_REGION", "0");                   //чтение данных: маршруты для привязки к контагентам
        PEREM_AG_UID_REGION = sp.getString("PEREM_AG_UID_REGION", "0");           //чтение данных: uid маршруты для привязки к контагентам
        PEREM_AG_CENA = sp.getString("PEREM_AG_CENA", "0");                       //чтение данных: цены для агентов
        PEREM_AG_SKLAD = sp.getString("PEREM_AG_SKLAD", "0");                     //чтение данных: склады для агентов
        PEREM_AG_UID_SKLAD = sp.getString("PEREM_AG_UID_SKLAD", "0");             //чтение данных: UID склады для агентов
        PEREM_AG_TYPE_REAL = sp.getString("PEREM_AG_TYPE_REAL", "0");             //чтение данных: выбор типа торгового агента 1-OSDO или 2-PRES
        PEREM_AG_TYPE_USER = sp.getString("PEREM_AG_TYPE_USER", "0");             //чтение данных: тип учетной записи агент или экспедитор
        PEREM_WORK_DISTR = sp.getString("PEREM_WORK_DISTR", "0");                 //чтение данных: имя папки с данными (01_WDay)
        PEREM_KOD_MOBILE = sp.getString("PEREM_KOD_MOBILE", "0");                 //чтение данных:
        PEREM_FTP_PUT = sp.getString("list_preference_ftp", "0");                 //чтение данных:

        for (int i = 0; i < getResources().getStringArray(R.array.mass_for_update_data).length; i++) {
            Save_dialog_up_ = sp.getString("Save_dialog_up_" + i, "0");
        }
    }

    // Константы для записи
    protected void Constanta_Write() {
        ed = sp.edit();
        ed.putString("PEREM_KLIENT_UID", "");  // передача кода выбранного uid клиента
        ed.putString("PEREM_DIALOG_UID", "");  // передача кода выбранного uid клиента
        ed.putString("PEREM_DIALOG_DATA_START", "");  // передача кода начальной даты
        ed.putString("PEREM_DIALOG_DATA_END", "");  // передача кода конечной даты
        ed.putString("PEREM_DISPLAY_START", "");  // передача кода начальной даты
        ed.putString("PEREM_DISPLAY_END", "");  // передача кода конечной даты
        ed.putString("PEREM_ISNAME_SPINNER", "OSTATOK_ACTIVITY");  // передача кода конечной даты
        ed.commit();
    }

    //
    protected void Loading_Dialog_Message() {
        pDialog = new ProgressDialog(context_Activity);
        pDialog.setMessage("Загрузка файлов с сервера...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setProgress(0);
        pDialog.setMax(100);
        pDialog.show();
    }

    // Подключение к FTP для скачивание файлов XML и DB3
    protected void FTP_Connect(String file_name, String file_put) {
        Log.e("UP", file_put);
        Log.e("UP", file_name);
        Sunbell_FtpConnection c = new Sunbell_FtpConnection();
        c.server = PEREM_FTP_SERV;
        c.port = 21;
        c.username = PEREM_FTP_LOGIN;
        c.password = PEREM_FTP_PASS;

        Sunbell_FtpAction a = new Sunbell_FtpAction();
        a.success = true;
        a.connection = c;
        a.fileName = file_name;
        a.remoteFilename = file_put + "/" + file_name;
        new Sunbell_FtpAsyncTask(context_Activity).execute(a);
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
                SQLiteDatabase db_sqlite = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
                final String query = "SELECT * FROM const_mail";
                final Cursor cursor = db_sqlite.rawQuery(query, null);
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    String region = cursor.getString(cursor.getColumnIndexOrThrow("region"));
                    String mail = cursor.getString(cursor.getColumnIndexOrThrow("operator_mail"));
                    String mail_pass = cursor.getString(cursor.getColumnIndexOrThrow("operator_mail_pass"));
                    if (region.equals(PEREM_FTP_PUT)) {
                        data_mail_where = mail;
                    }
                    if (region.equals("Mail_Agents")) {
                        data_mail_from = mail;
                        data_mail_login = mail;
                        data_mail_pass = mail_pass;
                    }

                    cursor.moveToNext();
                }
                cursor.close();
                db_sqlite.close();


                title = "Агент: " + PEREM_AG_NAME;
                text = new_write;
                from = data_mail_from;
                where = data_mail_where;
                attach = "";

                MailSenderClass sender = new MailSenderClass(data_mail_login, data_mail_pass, "465", "smtp.gmail.com");   // Null
                sender.sendMail(title, text, from, where, attach);
                // MailSenderClass sender = new MailSenderClass(PEREM_MAIL_LOGIN, PEREM_MAIL_PASS, "465", "smtp.mail.ru");

                //  MailSenderClass sender = new MailSenderClass("kerkin911@gmail.com", "muvodoutmhkbnqxi", "465", "smtp.gmail.com"); // WORK
                //   MailSenderClass sender = new MailSenderClass("kerkin911@mail.ru", "7qjc5agFqqgKJ7dTCuzf", "465", "smtp.mail.ru"); // WORK
                //    MailSenderClass sender = new MailSenderClass("RomanK911@yandex.ru", "ygkvnfxbkwpjhwxd", "587", "smtp.yandex.ru");                  // NULL
                //  MailSenderClass sender = new MailSenderClass("bishkek@sunbell.webhost.kg", "microlab_LG901480", "465", "mail.sunbell.webhost.kg");   // Null
                //  MailSenderClass sender = new MailSenderClass("sunbellagents@gmail.com", "fyczcoexpaspsham", "465", "smtp.gmail.com");   // Null

               /* from = "bishkek@sunbell.webhost.kg";
                where = PEREM_MAIL_END;
                attach = "";
                MailSenderClass sender = new MailSenderClass("bishkek@sunbell.webhost.kg", "microlab_LG901480", "465", "mail.sunbell-kg.webhost.kg");*/

                //  sender.sendMail(title, text, from, where, attach);
            } catch (Exception e) {
                Toast.makeText(context_Activity, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
            }

            return false;
        }
    }

    protected void Loading_FTP_Zakaz(String file_name, String file_put) {
        // подключение к ftp-server
       /* sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        REGIONS = sp.getString("REGIONS", "0");
        Log.e("Exsel", REGIONS);
        Toast.makeText(context_Activity, REGIONS, Toast.LENGTH_SHORT).show();*/

       /* ftp_con_put = "/Server/Forma_Zakaz/";
         a.remoteFilename = ftp_put;*/

        Log.e("UP_Zakaz:", "PUT=" + file_put);
        Log.e("UP_Zakaz:", file_name);

        FtpConnection c = new FtpConnection();
        c.server = PEREM_FTP_SERV;
        c.port = 21;
        c.username = PEREM_FTP_LOGIN;
        c.password = PEREM_FTP_PASS;
        FtpAction a = new FtpAction();
        a.success = true;
        a.connection = c;
        new FtpAsyncTask_RN_Up(context_Activity).execute(a);

    }


    // Подключение к FTP для скачивание файлов XML и DB3 для филиалов
    protected void FTP_Connect_Filial(String file_name, String file_put) {
        Log.e("UP", file_put);
        Log.e("UP", file_name);
        Sunbell_FtpConnection c = new Sunbell_FtpConnection();
        c.server = PEREM_FTP_SERV;
        c.port = 21;
        c.username = PEREM_FTP_LOGIN;
        c.password = PEREM_FTP_PASS;

        Sunbell_FtpAction a = new Sunbell_FtpAction();
        a.success = true;
        a.connection = c;
        a.fileName = file_name;
        a.remoteFilename = file_put + "/" + file_name;
        new Sunbell_FtpAsyncTask_Filial(context_Activity).execute(a);
    }

    protected void ListAdapet_Internet_Load() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
        Log.e(this.getLocalClassName(), this_data_work_now);

        String query_count = "SELECT DISTINCT k_agn_name FROM base_RN_All WHERE data == '" + this_data_work_now + "'";
        final Cursor cursor_count = db.rawQuery(query_count, null);
        cursor_count.moveToFirst();
        if (cursor_count.getCount() > 0) {
            count_tt = cursor_count.getCount();
        } else count_tt = 0;
        cursor_count.close();


        String query = "SELECT base_RN_All.data, base_RN_All.kod_rn, base_RN_All.k_agn_name, " +
                "base_RN_All.k_agn_uid, base_RN_All.k_agn_adress, base_RN_All.skidka, base_RN_All.summa, base_RN_All.itogo\n" +
                "FROM base_RN_All\n" +
                "WHERE base_RN_All.agent_uid = '" + PEREM_AG_UID + "' AND base_RN_All.data LIKE '" + this_data_work_now + "'";

        new_write = ("");
        new_write = (new_write + "");
        new_write = (new_write + "Дата загрузки: " + this_data_now + " | " + this_vrema_work_now);
        new_write = (new_write + "\n");
        new_write = (new_write + "Количество точек: " + count_tt);
        //  new_write = (new_write + "Дата загрузки: " + this_data_work_now);
        new_write = (new_write + "\n");
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String kod_rn = cursor.getString(cursor.getColumnIndexOrThrow("kod_rn"));
            String klients = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_name"));
            String Adress = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_adress"));
            String Summa = cursor.getString(cursor.getColumnIndexOrThrow("summa"));
            String Itogo = cursor.getString(cursor.getColumnIndexOrThrow("itogo"));
            String Skidka = cursor.getString(cursor.getColumnIndexOrThrow("skidka"));
            new_write = (new_write + "\nНомер накладной: " + kod_rn);
            new_write = (new_write + "\nКонтрагент: " + klients);
            new_write = (new_write + "\nАдрес: " + Adress);
            new_write = (new_write + "\nСумма: " + Summa);
            new_write = (new_write + "\nСкидка: " + Skidka + "%");
            new_write = (new_write + "\nИтого: " + Itogo);
            new_write = (new_write + "\n");
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }


    /////////////////////////////////////////////////////////////  РЕЗЕРВНОЕ КОППИРОВАНИЕ БАЗ ДАННЫХ

    // Подключение к FTP для скачивание файлов XML и DB3 для филиалов
    protected void FTP_Connect_Filial_SOS(String file_name, String file_put) {
        // Log.e("UP", file_put);
        // Log.e("UP", file_name);
        Sunbell_FtpConnection c = new Sunbell_FtpConnection();
        c.server = PEREM_FTP_SERV;
        c.port = 21;
        c.username = PEREM_FTP_LOGIN;
        c.password = PEREM_FTP_PASS;

        Sunbell_FtpAction a = new Sunbell_FtpAction();
        a.success = true;
        a.connection = c;
        a.fileName = file_name;
        a.remoteFilename = file_put;
        new Sunbell_FtpAsyncTask_SOS_FILES(context_Activity).execute(a);
    }

    protected void Mail() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


    }

    protected void ListAdapet_Internet_Load_SOS() {
        new_write = ("");
        new_write = (new_write + "");
        new_write = (new_write + "Резервное копирование базы данных от " + PEREM_AG_NAME);
        new_write = (new_write + "\n");
        new_write = (new_write + "Дата загрузки: " + this_data_now);
        new_write = (new_write + "\n");
        String[] mass_files_SOS = getResources().getStringArray(R.array.mass_files_SQLITE_DB);
        for (int i = 0; i < mass_files_SOS.length; i++) {
            String new_name = mass_files_SOS[i].substring(0, mass_files_SOS[i].length() - 4);
            new_write = (new_write + "Имя файла: " + this_data_now + "_" + new_name + ".db3");
            new_write = (new_write + "\n");
        }
    }

    private class sender_mail_async_SOS extends AsyncTask<Object, String, Boolean> {
        ProgressDialog WaitingDialog;

        @Override
        protected void onPreExecute() {
            WaitingDialog = ProgressDialog.show(context_Activity, "Резервное копирование", "Выполняется загрузка, подождите...", true);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            WaitingDialog.dismiss();
            //Toast.makeText(context_Activity, "Заказ успешно отправлен!!!", Toast.LENGTH_LONG).show();
            //((Activity)mainContext).finish();
        }

        @Override
        protected Boolean doInBackground(Object... params) {

            try {
                SQLiteDatabase db_sqlite = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
                final String query = "SELECT * FROM const_mail";
                final Cursor cursor = db_sqlite.rawQuery(query, null);
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    String region = cursor.getString(cursor.getColumnIndexOrThrow("region"));
                    String mail = cursor.getString(cursor.getColumnIndexOrThrow("operator_mail"));
                    String mail_pass = cursor.getString(cursor.getColumnIndexOrThrow("operator_mail_pass"));
                    if (region.equals(PEREM_FTP_PUT)) {
                        data_mail_where = "kerkin911@gmail.com";
                        data_mail_from = mail;
                        data_mail_login = mail;
                        data_mail_pass = mail_pass;
                    }
                    cursor.moveToNext();
                }
                Log.e("Mail..", data_mail_login + data_mail_pass);
                cursor.close();
                db_sqlite.close();


                title = "Агент: " + PEREM_AG_NAME;
                text = new_write;
                from = data_mail_from;
                where = data_mail_where;
                attach = "";

                MailSenderClass sender = new MailSenderClass(data_mail_login, data_mail_pass, "465", "smtp.gmail.com");   // Null
                sender.sendMail(title, text, from, where, attach);
            } catch (Exception e) {
                Toast.makeText(context_Activity, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
            }

            return false;
        }
    }


    ////////////////////////////////////////////////////////////  ОБНОВЛЕНИЕ ДАННЫХ БАЗ И XML ФАЙЛОВ

    // Обновление рабочих файлов с сервера MTW
    private class MyAsyncTask_Update_File_MTW extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();

            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            pDialog.setMessage("Обновление файлов. Подождите...");

            pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                getFloor();  // Синхронизация файлов для всех складов
                publishProgress(1);
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
            Toast.makeText(context_Activity, "Загруженно: " + files_kol + " " + files_size, Toast.LENGTH_SHORT).show();
            pDialog.setProgress(0);
            pDialog.cancel();
            pDialog.dismiss();
        }

        private void getFloor() throws InterruptedException {
            pDialog.setMessage("Обновление файлов. Подождите...");
            try {

                FTPClient ftpClient = new FTPClient();
                String server = PEREM_FTP_SERV;
                int port = 21;
                String user = PEREM_FTP_LOGIN;
                String pass = PEREM_FTP_PASS;

                try {
                    ftpClient.connect(server, port);
                    ftpClient.login(user, pass);
                    ftpClient.enterLocalPassiveMode();
                    String remoteDirPath_XML = PEREM_FTP_DISTR_XML;

                    long[] dirInfo = calculateDirectoryInfo(ftpClient, remoteDirPath_XML, "");
                    System.out.println("Total dirs: " + dirInfo[0]);
                    System.out.println("Total files: " + dirInfo[1]);
                    System.out.println("Total size: " + dirInfo[2]);
                    Log.e("Дирикторий=", " = " + dirInfo[0]);
                    Log.e("Файлов=", " = " + dirInfo[1]);
                    Log.e("Размер=", " = " + dirInfo[2] + " байт");
                    Log.e("Размер=", " = " + dirInfo[2] / 1024 + " кбайт");
                    Log.e("Размер=", " = " + dirInfo[2] / 1048576 + " мбайт");
                    files_kol = dirInfo[1] + "файлов";
                    files_size = dirInfo[2] / 1048576 + " мбайт";

                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(dirInfo[1]));

                    FTPFile[] ftpFiles_list_XML = ftpClient.listFiles(remoteDirPath_XML);
                    for (FTPFile ftpFile_XML : ftpFiles_list_XML) {
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                        Log.e("Путь=", " = " + ftpFile_XML.getName());
                        String file_server_xml = PEREM_FTP_DISTR_XML + "/" + ftpFile_XML.getName(); // путь на сервере
                        if (ftpFile_XML.isFile()) {
                            for (int i = 0; i < mass_update_files.length; i++) {
                                if (ftpFile_XML.getName().equals(mass_update_files[i])) {
                                    String file_db = WJ_Global_Activity.this.getDatabasePath(mass_update_files[i]).getAbsolutePath(); // путь к databases
                                    // кода для скачивания файла с FTP
                                    outputStream = new FileOutputStream(new File(file_db));  // путь куда сохранить данные
                                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                                    ftpClient.enterLocalPassiveMode();
                                    // ftp: забрать файл из этой директ, в эту директ
                                    ftpClient.retrieveFile(file_server_xml, outputStream);
                                    outputStream.close();
                                }
                            }
                        }


                    }

                    ftpClient.logout();
                    ftpClient.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    if (ftpClient.isConnected()) {
                        try {
                            ftpClient.logout();
                            ftpClient.disconnect();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }


            } catch (Exception e) {
                // Toast.makeText(context_Activity, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
            }

        }  // Синхронизация файлов для всех складов

    }

    // Обновление рабочих файлов с сервера SQLite
    private class MyAsyncTask_Update_File_SQLite extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();

            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            pDialog.setMessage("Обновление файлов. Подождите...");

            pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                getFloor();  // Синхронизация файлов для всех складов
                publishProgress(1);
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
            Toast.makeText(context_Activity, "Загруженно: " + files_kol + " " + files_size, Toast.LENGTH_SHORT).show();
            pDialog.setProgress(0);
            pDialog.cancel();
            pDialog.dismiss();
        }

        private void getFloor() throws InterruptedException {
            pDialog.setMessage("Обновление файлов. Подождите...");
            try {

                FTPClient ftpClient = new FTPClient();
                String server = PEREM_FTP_SERV;
                int port = 21;
                String user = PEREM_FTP_LOGIN;
                String pass = PEREM_FTP_PASS;

                try {
                    ftpClient.connect(server, port);
                    ftpClient.login(user, pass);
                    ftpClient.enterLocalPassiveMode();
                    String remoteDirPath_DB3 = PEREM_FTP_DISTR_db3 + "/sunbell_const_db.db3";
                    long[] dirInfo = calculateDirectoryInfo(ftpClient, remoteDirPath_DB3, "");
                    System.out.println("Total dirs: " + dirInfo[0]);
                    System.out.println("Total files: " + dirInfo[1]);
                    System.out.println("Total size: " + dirInfo[2]);
                    Log.e("Дирикторий=", " = " + dirInfo[0]);
                    Log.e("Файлов=", " = " + dirInfo[1]);
                    Log.e("Размер=", " = " + dirInfo[2] + " байт");
                    Log.e("Размер=", " = " + dirInfo[2] / 1024 + " кбайт");
                    Log.e("Размер=", " = " + dirInfo[2] / 1048576 + " мбайт");

                    files_kol = dirInfo[1] + "файлов";
                    files_size = dirInfo[2] / 1048576 + " мбайт";


                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(dirInfo[1]));

                    FTPFile[] ftpFiles_list_DB3 = ftpClient.listFiles(remoteDirPath_DB3);
                    for (FTPFile ftpFile_DB3 : ftpFiles_list_DB3) {
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                        String file_server_db3 = PEREM_FTP_DISTR_db3 + "/" + ftpFile_DB3.getName(); // путь на сервере
                        String file_db = WJ_Global_Activity.this.getDatabasePath(ftpFile_DB3.getName()).getAbsolutePath(); // путь к databases

                        if (ftpFile_DB3.isFile()) {
                            for (int k = 0; k < mass_update_files.length; k++) {
                                if (ftpFile_DB3.getName().equals(mass_update_files[k])) {
                                    Log.e("Дирикторий=", " = " + ftpFile_DB3.getName());
                                    // кода для скачивания файла с FTP
                                    outputStream = new FileOutputStream(new File(file_db));  // путь куда сохранить данные
                                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                                    ftpClient.enterLocalPassiveMode();
                                    // ftp: забрать файл из этой директ, в эту директ
                                    ftpClient.retrieveFile(file_server_db3, outputStream);
                                    outputStream.close();
                                }
                            }

                        }
                    }

                    ftpClient.logout();
                    ftpClient.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    if (ftpClient.isConnected()) {
                        try {
                            ftpClient.logout();
                            ftpClient.disconnect();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }


            } catch (Exception e) {
                // Toast.makeText(context_Activity, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
            }

        }  // Синхронизация файлов для всех складов

    }


    ////////////////////////////////////////////////////////  СИНХРОНИЗАЦИЯ ДАННЫХ БАЗ И XML ФАЙЛОВ/

    // Ежедневная синхронизация
    private class MyAsyncTask_Sync_EveryDay extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {    // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК", "Начало синхронизации");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            mass = getResources().getStringArray(R.array.mass_all_sync_everyday);
            pDialog.setMessage(mass[values[0]] + " \n" + values[0] + "/3 Подождите...");
            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {

                for (int i = 0; i < 4; i++) {
                    switch (i) {
                        case 1: {
                            publishProgress(i);
                            getFloor_MTW_In_ResidueGoodsPR(); // Синхронизация остатки
                        }
                        break;
                        case 2: {
                            publishProgress(i);
                            getFloor_MTW_In_Price(); // Синхронизация цены
                        }
                        break;
                        case 3: {
                            publishProgress(i);
                            getFloor_MTW_In_CustomersDebet(); // Синхронизация склады
                        }
                        break;
                    }
                }
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
            Log.e("Sunc_End=", "MTW обработаны!");
            Toast.makeText(context_Activity, "Обработка данных окончена!", Toast.LENGTH_LONG).show();
            syns_end = true;
            pDialog.setProgress(0);
            pDialog.cancel();
            pDialog.dismiss();
        }

        private void getFloor_MTW_In_ResidueGoodsPR() throws InterruptedException {
            try {
                SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
                db.delete("base_in_ostatok", null, null);
                String query_up = "SELECT * FROM base_in_ostatok";
                cursor = db.rawQuery(query_up, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();
                String file_db = WJ_Global_Activity.this.getDatabasePath("MTW_In_ResidueGoodsPR.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                MTW_Ostatok_ResourceParser parser = new MTW_Ostatok_ResourceParser();
                int eventType = xpp.getEventType();
                if (parser.parse(xpp)) {

                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getOstatok().size()));


                    for (MTW_Ostatok ostatok : parser.getOstatok()) {
                        localContentValues.put("data", ostatok.getData());
                        localContentValues.put("sklad_uid", ostatok.getSklad_uid());
                        localContentValues.put("nomenclature_uid", ostatok.getName_uid());
                        localContentValues.put("name", ostatok.getName());
                        localContentValues.put("count", ostatok.getCount());
                        db.insert("base_in_ostatok", null, localContentValues);
                        Log.e("Ostat..", ostatok.getData() + " " + ostatok.getName() + " " + ostatok.getCount());
                        cursor.moveToNext();
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                    }

                    cursor.close();
                    db.close();
                }

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации остатков!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации остатков!");
            }

        }

        private void getFloor_MTW_In_Price() throws InterruptedException {
            try {
                SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
                db_prev.delete("base_in_price", null, null);
                String query = "SELECT * FROM base_in_price;";
                cursor = db_prev.rawQuery(query, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();

                String file_db = WJ_Global_Activity.this.getDatabasePath("MTW_In_Price.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                MTW_Price_ResourceParser parser = new MTW_Price_ResourceParser();
                int eventType = xpp.getEventType();
                if (parser.parse(xpp)) {
                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getPrice().size()));
                    for (MTW_Price price : parser.getPrice()) {
                        Log.e("Price..", price.getUid() + " Cena.." + price.getCena());
                        localContentValues.put("nomenclature_uid", price.getUid());
                        localContentValues.put("price", price.getCena());
                        db_prev.insert("base_in_price", null, localContentValues);
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                        cursor.moveToNext();
                    }
                }

                TimeUnit.SECONDS.sleep(1);
                cursor.close();
                db_prev.close();

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации цен!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации цен!");
            }

        }

        private void getFloor_MTW_In_CustomersDebet() throws InterruptedException {
            try {
                SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
                db_prev.delete("otchet_debet", null, null);
                String query = "SELECT * FROM otchet_debet;";
                cursor = db_prev.rawQuery(query, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();

                String file_db = WJ_Global_Activity.this.getDatabasePath("MTW_In_CustomersDebet.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                MTW_CustomersDebet_ResourceParser parser = new MTW_CustomersDebet_ResourceParser();
                if (parser.parse(xpp)) {
                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getCustomersDebet().size()));
                    for (MTW_CustomersDebet debet : parser.getCustomersDebet()) {

                        localContentValues.put("d_agent_name", debet.getAgent());
                        localContentValues.put("d_agent_uid", debet.getUid_agent());
                        localContentValues.put("d_kontr_name", debet.getCustomer());
                        localContentValues.put("d_kontr_uid", debet.getUid_customer());
                        localContentValues.put("d_summa", debet.getSum_debet());

                        db_prev.insert("otchet_debet", null, localContentValues);
                        cursor.moveToNext();
                        Log.e("Debet..", debet.getCustomer() + " ." + debet.getSum_debet() + "");
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                    }
                }

                TimeUnit.SECONDS.sleep(1);
                cursor.close();
                db_prev.close();

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации складов!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации складов!");
            }

        }

        private void getFloor_MTW_In_PaymentOrders() throws InterruptedException {
            try {
                SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
                db_prev.delete("base_in_all_debet", null, null);
                String query = "SELECT * FROM base_in_all_debet;";
                cursor = db_prev.rawQuery(query, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();

                String file_db = WJ_Global_Activity.this.getDatabasePath("MTW_In_CustomersDebet.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                MTW_Skald_ResourceParser parser = new MTW_Skald_ResourceParser();
                if (parser.parse(xpp)) {
                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getSklads().size()));
                    for (MTW_Sklad sklad : parser.getSklads()) {

                        localContentValues.put("sklad_uid", sklad.getSklad_uid());
                        localContentValues.put("sklad_name", sklad.getSklad_name());
                        db_prev.insert("const_sklad", null, localContentValues);
                        cursor.moveToNext();
                        Log.e("UIDYY..", sklad.getSklad_name() + " ." + sklad.getSklad_uid() + "");
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                    }
                }

                TimeUnit.SECONDS.sleep(1);
                cursor.close();
                db_prev.close();

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации складов!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации складов!");
            }

        }
    }

    // Товарная часть синхронизация
    private class MyAsyncTask_Sync_Tovar extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {    // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК", "Начало синхронизации");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            mass = getResources().getStringArray(R.array.mass_all_tovar);
            pDialog.setMessage(mass[values[0]] + " \n" + values[0] + "/5 Подождите...");
            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {

                for (int i = 0; i < 6; i++) {
                    switch (i) {
                        case 1: {
                            publishProgress(i);
                            getFloor_MTW_Brends();  // Синхронизация Структуры
                        }
                        break;
                        case 2: {
                            publishProgress(i);
                            getFloor_MTW_In_Nomenclatures(); // Синхронизация номенклатуры
                        }
                        break;
                        case 3: {
                            publishProgress(i);
                            getFloor_Image(); // Синхронизация картинок
                        }
                        break;
                        case 4: {
                            publishProgress(i);
                            getFloor_MTW_In_ResidueGoodsPR(); // Синхронизация остатки
                        }
                        break;
                        case 5: {
                            publishProgress(i);
                            getFloor_MTW_In_Price(); // Синхронизация цены
                        }
                        break;
                       /* case 8: {
                            publishProgress(i);
                            getFloor_MTW_In_CustomersDebet(); // Синхронизация долгов
                        }
                        break;*/
                    }
                }
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
            Log.e("Sunc_End=", "MTW обработаны!");
            Toast.makeText(context_Activity, "Обработка данных окончена!", Toast.LENGTH_LONG).show();
            syns_end = true;
            pDialog.setProgress(0);
            pDialog.dismiss();
            pDialog.cancel();
        }

        private void getFloor_MTW_Brends() throws InterruptedException {
            pDialog.setMessage("Обработка данных. 1/8 Подождите...");
            SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
            db_prev.delete("base_in_brends_id", null, null);
            db_prev.delete("base_in_brends_sub_id", null, null);
            String query = "SELECT * FROM base_in_brends_id;";
            cursor = db_prev.rawQuery(query, null);
            localContentValues_Breds_ID = new ContentValues();
            localContentValues_sub_Breds_ID = new ContentValues();
            cursor.moveToFirst();
            try {
                String file_db = WJ_Global_Activity.this.getDatabasePath("MTW_In_Brends.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);

                MTW_SubBrends_ResourceParser parser = new MTW_SubBrends_ResourceParser();
                if (parser.parse(xpp)) {
                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getSubBrends().size()));
                    mass_breds_group = getResources().getStringArray(R.array.mass_breds_group);
                    for (MTW_SubBrends subBrends : parser.getSubBrends()) {

                        if (subBrends.getParents_kod().equals("TRUE")) {
                            grb = "gr_1";
                            localContentValues_Breds_ID.put("brends", subBrends.getName());
                            localContentValues_Breds_ID.put("kod", subBrends.getKod());
                            localContentValues_Breds_ID.put("parent_kod", subBrends.getParents_kod());
                            localContentValues_Breds_ID.put("prefic", subBrends.getPrefic().toLowerCase());
                            for (int i = 0; i < mass_breds_group.length; i++) {
                                if (subBrends.getKod().equals(mass_breds_group[i])) {
                                    grb = "gr_2";
                                }
                            }

                            localContentValues_Breds_ID.put("group_type", grb);
                            db_prev.insert("base_in_brends_id", null, localContentValues_Breds_ID);
                            cursor.moveToNext();

                        } else {
                            localContentValues_sub_Breds_ID.put("subbrends", subBrends.getName());
                            localContentValues_sub_Breds_ID.put("kod", subBrends.getKod());
                            localContentValues_sub_Breds_ID.put("parent_kod", subBrends.getParents_kod());
                            db_prev.insert("base_in_brends_sub_id", null, localContentValues_sub_Breds_ID);
                            cursor.moveToNext();
                        }

                        Log.e("UIDYY..", subBrends.getName() + " ." + subBrends.getPrefic() + "");
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());

                    }
                }

                cursor.close();
                db_prev.close();

                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации структуры!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации структуры!");
            }

        }

        private void getFloor_MTW_In_Nomenclatures() throws InterruptedException {

            SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
            db_prev.delete("base_in_nomeclature", null, null);
            String query = "SELECT * FROM base_in_nomeclature;";
            cursor = db_prev.rawQuery(query, null);
            localContentValues = new ContentValues();
            cursor.moveToFirst();
            try {
                String file_db = WJ_Global_Activity.this.getDatabasePath("MTW_In_Nomenclatures.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы

                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                MTW_Nomenclatures_ResourceParser parser = new MTW_Nomenclatures_ResourceParser();
                parserFactory.setNamespaceAware(true);
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                if (parser.parse(xpp)) {
                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getTovar().size()));
                    for (MTW_Nomenclatures tovar : parser.getTovar()) {

                        /*if (!tovar.getBrends().equals("")) {

                        } else {
                            cursor.moveToNext();
                        }*/
                        localContentValues.put("brends", tovar.getBrends());
                        localContentValues.put("p_group", tovar.getP_group());
                        localContentValues.put("kod", tovar.getKod());
                        localContentValues.put("name", tovar.getName());
                        localContentValues.put("kolbox", tovar.getKolbox());
                        localContentValues.put("cena", tovar.getCena());
                        localContentValues.put("strih", tovar.getStrih());
                        localContentValues.put("koduid", tovar.getKoduid().trim());

                        if (tovar.getKod_univ().length() > 25) {
                            localContentValues.put("kod_univ", tovar.getKod_univ());
                            db_prev.insert("base_in_nomeclature", null, localContentValues);
                            Log.e("UIDYY..", tovar.getBrends() + " " + tovar.getP_group() + "");
                            f1 = f1.add(pointOne);
                            pDialog.setProgress(f1.intValue());
                            cursor.moveToNext();
                        } else cursor.moveToNext();


                       /* 0A117DC7-93F0-4D61-9DDF-72CCDD39B1C6
                        4163C3D7-9757-4F90-9F9B-389B0F2213E8    *
                        00000001/00000238/BL000606*/







                       /* if (!tovar.getBrends().equals("")) {
                            localContentValues.put("subbrends", subBrends.getSubbrends());
                            localContentValues.put("kod", subBrends.getKod());
                            localContentValues.put("parents_kod", subBrends.getParents_kod());
                            db_prev.insert("base8_Group_ID", null, localContentValues);
                            Log.e("UIDYY..", subBrends.getSubbrends() + " ." + subBrends.getParents_kod() + "");
                            f1 = f1.add(pointOne);
                            pDialog.setProgress(f1.intValue());
                            cursor.moveToNext();
                        } else {
                            localContentValues.put("subbrends", subBrends.getSubbrends());
                            localContentValues.put("kod", subBrends.getKod());
                            localContentValues.put("parents_kod", subBrends.getParents_kod());
                            db_prev.insert("base7_Brends_ID", null, localContentValues);
                            Log.e("TRUE..", subBrends.getSubbrends() + " ." + subBrends.getParents_kod() + "");
                            cursor.moveToNext();
                        }*/


                    }
                }
                TimeUnit.SECONDS.sleep(1);
                cursor.close();
                db_prev.close();

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации номенклатуры!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации номенклатуры!");
            }

        }

        private void getFloor_Image() throws InterruptedException {
            try {
                SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
                db_prev.delete("base_in_image", null, null);

                String query = "SELECT base_in_nomeclature.name, base_in_nomeclature.koduid," +
                        "base_in_nomeclature.kod,   lower(base_in_brends_id.prefic || '_' || base_in_nomeclature.kod) AS 'kod_image' \n" +
                        "FROM base_in_nomeclature\n" +
                        "LEFT JOIN base_in_brends_id ON base_in_brends_id.kod=substr(base_in_nomeclature.kod_univ, 1, 8)" +
                        "WHERE kod_image > 0";
                cursor = db_prev.rawQuery(query, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();
                BigDecimal f1 = new BigDecimal(0.0);
                BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(cursor.getCount()));
                while (cursor.isAfterLast() == false) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String koduid = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));
                    String kod = cursor.getString(cursor.getColumnIndexOrThrow("kod"));
                    String kod_image = cursor.getString(cursor.getColumnIndexOrThrow("kod_image"));
                    localContentValues.put("name", name);
                    localContentValues.put("koduid", koduid);
                    localContentValues.put("kod", kod);
                    localContentValues.put("kod_image", kod_image.replace('ц', 'c'));
                    db_prev.insert("base_in_image", null, localContentValues);
                    cursor.moveToNext();
                    // Log.e("UIDYY..", name + " ." + kod_image);
                    f1 = f1.add(pointOne);
                    pDialog.setProgress(f1.intValue());
                }
                TimeUnit.SECONDS.sleep(1);
                cursor.close();
                db_prev.close();

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации складов!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации складов!");
            }

        }

        private void getFloor_MTW_In_ResidueGoodsPR() throws InterruptedException {
            try {
                SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
                db.delete("base_in_ostatok", null, null);
                String query_up = "SELECT * FROM base_in_ostatok";
                cursor = db.rawQuery(query_up, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();
                String file_db = WJ_Global_Activity.this.getDatabasePath("MTW_In_ResidueGoodsPR.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                MTW_Ostatok_ResourceParser parser = new MTW_Ostatok_ResourceParser();
                int eventType = xpp.getEventType();
                if (parser.parse(xpp)) {

                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getOstatok().size()));


                    for (MTW_Ostatok ostatok : parser.getOstatok()) {
                        localContentValues.put("data", ostatok.getData());
                        localContentValues.put("sklad_uid", ostatok.getSklad_uid());
                        localContentValues.put("nomenclature_uid", ostatok.getName_uid());
                        localContentValues.put("name", ostatok.getName());
                        localContentValues.put("count", ostatok.getCount());
                        db.insert("base_in_ostatok", null, localContentValues);
                        Log.e("SYNC..", ostatok.getData() + " " + ostatok.getName() + " " + ostatok.getCount());
                        cursor.moveToNext();
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                    }

                    cursor.close();
                    db.close();
                }

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации остатков!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации остатков!");
            }

        }

        private void getFloor_MTW_In_Price() throws InterruptedException {
            try {
                SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
                db_prev.delete("base_in_price", null, null);
                String query = "SELECT * FROM base_in_price;";
                cursor = db_prev.rawQuery(query, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();

                String file_db = WJ_Global_Activity.this.getDatabasePath("MTW_In_Price.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                MTW_Price_ResourceParser parser = new MTW_Price_ResourceParser();
                int eventType = xpp.getEventType();
                if (parser.parse(xpp)) {
                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getPrice().size()));
                    for (MTW_Price price : parser.getPrice()) {
                        Log.e("UIDYY..", price.getUid() + " Cena.." + price.getCena());
                        localContentValues.put("nomenclature_uid", price.getUid());
                        localContentValues.put("price", price.getCena());
                        db_prev.insert("base_in_price", null, localContentValues);
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                        cursor.moveToNext();
                    }
                }

                TimeUnit.SECONDS.sleep(1);
                cursor.close();
                db_prev.close();

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации цен!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации цен!");
            }

        }
    }

    // Полная синхронизация данных
    private class MyAsyncTask_Sync_All extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {    // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК", "Начало синхронизации");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            mass = getResources().getStringArray(R.array.mass_all_all);
            pDialog.setMessage(mass[values[0]] + " \n" + values[0] + "/8 Подождите...");
            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                for (int i = 0; i < 9; i++) {
                    switch (i) {
                        case 1: {
                            publishProgress(i);
                            getFloor_MTW_Brends();  // Синхронизация Структуры

                        }
                        break;
                        case 2: {
                            publishProgress(i);
                            getFloor_MTW_In_Nomenclatures(); // Синхронизация номенклатуры
                        }
                        break;
                        case 3: {
                            publishProgress(i);
                            getFloor_Image(); // Синхронизация картинок
                        }
                        break;
                        case 4: {
                            publishProgress(i);
                            getFloor_MTW_In_ResidueGoodsPR(); // Синхронизация остатки
                        }
                        break;
                        case 5: {
                            publishProgress(i);
                            getFloor_MTW_In_Price(); // Синхронизация цены
                        }
                        break;
                        case 6: {
                            publishProgress(i);
                            getFloor_MTW_In_Customers(); // Синхронизация контрагенты
                        }
                        break;
                        case 7: {
                            publishProgress(i);
                            getFloor_MTW_In_Warehouse(); // Синхронизация склады
                        }
                        break;
                        case 8: {
                            publishProgress(i);
                            getFloor_MTW_In_CustomersDebet(); // Синхронизация склады
                        }
                        break;
                       /* case 8: {
                            publishProgress(i);
                            getFloor_MTW_In_CustomersDebet(); // Синхронизация долгов
                        }
                        break;*/
                    }
                }
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
            Log.e("Sunc_End=", "MTW обработаны!");
            Toast.makeText(context_Activity, "Обработка данных окончена!", Toast.LENGTH_LONG).show();
            syns_end = true;
            pDialog.setProgress(0);
            pDialog.dismiss();
            pDialog.cancel();
        }

        private void getFloor_MTW_Brends() throws InterruptedException {
            pDialog.setMessage("Обработка данных. 1/8 Подождите...");
            SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
            db_prev.delete("base_in_brends_id", null, null);
            db_prev.delete("base_in_brends_sub_id", null, null);
            String query = "SELECT * FROM base_in_brends_id;";
            cursor = db_prev.rawQuery(query, null);
            localContentValues_Breds_ID = new ContentValues();
            localContentValues_sub_Breds_ID = new ContentValues();

            SQLiteDatabase db_w = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
            String query_w = "SELECT * FROM const_group_brends;";
            cursor_w = db_w.rawQuery(query_w, null);
            cursor_w.moveToFirst();


            try {
                String file_db = WJ_Global_Activity.this.getDatabasePath("MTW_In_Brends.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);

                MTW_SubBrends_ResourceParser parser = new MTW_SubBrends_ResourceParser();
                if (parser.parse(xpp)) {
                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getSubBrends().size()));
                    // mass_breds_group = getResources().getStringArray(R.array.mass_breds_group);
                    for (MTW_SubBrends subBrends : parser.getSubBrends()) {
                        cursor_w.moveToFirst();
                        if (subBrends.getParents_kod().equals("TRUE")) {
                            // grb = "gr_1";
                            localContentValues_Breds_ID.put("brends", subBrends.getName());
                            localContentValues_Breds_ID.put("kod", subBrends.getKod());
                            localContentValues_Breds_ID.put("parent_kod", subBrends.getParents_kod());
                            localContentValues_Breds_ID.put("prefic", subBrends.getPrefic().toLowerCase());

                            while (cursor_w.isAfterLast() == false) {
                                String kod = cursor_w.getString(cursor_w.getColumnIndexOrThrow("brends_kod"));
                                String group = cursor_w.getString(cursor_w.getColumnIndexOrThrow("brends_group"));
                                if (kod.equals(subBrends.getKod())) {
                                    localContentValues_Breds_ID.put("group_type", group);
                                    cursor_w.moveToLast();
                                } else {
                                    localContentValues_Breds_ID.put("group_type", "g_null");
                                }
                                cursor_w.moveToNext();
                            }

                        /*    for (int i = 0; i < mass_breds_group.length; i++) {
                                if (subBrends.getKod().equals(mass_breds_group[i])) {
                                    grb = "gr_2";
                                }
                            }
                            localContentValues_Breds_ID.put("group_type", grb);*/

                            db_prev.insert("base_in_brends_id", null, localContentValues_Breds_ID);
                            cursor.moveToNext();

                        } else {
                            localContentValues_sub_Breds_ID.put("subbrends", subBrends.getName());
                            localContentValues_sub_Breds_ID.put("kod", subBrends.getKod());
                            localContentValues_sub_Breds_ID.put("parent_kod", subBrends.getParents_kod());
                            db_prev.insert("base_in_brends_sub_id", null, localContentValues_sub_Breds_ID);
                            cursor.moveToNext();
                        }

                        Log.e("Brends..", subBrends.getName() + " ." + subBrends.getPrefic() + "");
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                    }
                }

                cursor_w.close();
                db_w.close();

                cursor.close();
                db_prev.close();

                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации структуры!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации структуры!");
            }


        }

        private void getFloor_MTW_In_Nomenclatures() throws InterruptedException {

            SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
            db_prev.delete("base_in_nomeclature", null, null);
            String query = "SELECT * FROM base_in_nomeclature;";
            cursor = db_prev.rawQuery(query, null);
            localContentValues = new ContentValues();
            cursor.moveToFirst();
            try {
                String file_db = WJ_Global_Activity.this.getDatabasePath("MTW_In_Nomenclatures.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы

                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                MTW_Nomenclatures_ResourceParser parser = new MTW_Nomenclatures_ResourceParser();
                parserFactory.setNamespaceAware(true);
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                if (parser.parse(xpp)) {
                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getTovar().size()));
                    for (MTW_Nomenclatures tovar : parser.getTovar()) {

                        /*if (!tovar.getBrends().equals("")) {

                        } else {
                            cursor.moveToNext();
                        }*/
                        localContentValues.put("brends", tovar.getBrends());
                        localContentValues.put("p_group", tovar.getP_group());
                        localContentValues.put("kod", tovar.getKod());
                        localContentValues.put("name", tovar.getName());
                        localContentValues.put("kolbox", tovar.getKolbox());
                        localContentValues.put("cena", tovar.getCena());
                        localContentValues.put("strih", tovar.getStrih());
                        localContentValues.put("koduid", tovar.getKoduid().trim());

                        if (tovar.getKod_univ().length() > 25) {
                            localContentValues.put("kod_univ", tovar.getKod_univ());
                            db_prev.insert("base_in_nomeclature", null, localContentValues);
                            Log.e("Tovar..", tovar.getBrends() + " " + tovar.getP_group() + "");
                            f1 = f1.add(pointOne);
                            pDialog.setProgress(f1.intValue());
                            cursor.moveToNext();
                        } else cursor.moveToNext();


                       /* 0A117DC7-93F0-4D61-9DDF-72CCDD39B1C6
                        4163C3D7-9757-4F90-9F9B-389B0F2213E8    *
                        00000001/00000238/BL000606*/







                       /* if (!tovar.getBrends().equals("")) {
                            localContentValues.put("subbrends", subBrends.getSubbrends());
                            localContentValues.put("kod", subBrends.getKod());
                            localContentValues.put("parents_kod", subBrends.getParents_kod());
                            db_prev.insert("base8_Group_ID", null, localContentValues);
                            Log.e("UIDYY..", subBrends.getSubbrends() + " ." + subBrends.getParents_kod() + "");
                            f1 = f1.add(pointOne);
                            pDialog.setProgress(f1.intValue());
                            cursor.moveToNext();
                        } else {
                            localContentValues.put("subbrends", subBrends.getSubbrends());
                            localContentValues.put("kod", subBrends.getKod());
                            localContentValues.put("parents_kod", subBrends.getParents_kod());
                            db_prev.insert("base7_Brends_ID", null, localContentValues);
                            Log.e("TRUE..", subBrends.getSubbrends() + " ." + subBrends.getParents_kod() + "");
                            cursor.moveToNext();
                        }*/


                    }
                }
                TimeUnit.SECONDS.sleep(1);
                cursor.close();
                db_prev.close();

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации номенклатуры!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации номенклатуры!");
            }

        }

        private void getFloor_Image() throws InterruptedException {
            try {
                SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
                db_prev.delete("base_in_image", null, null);

                String query = "SELECT base_in_nomeclature.name, base_in_nomeclature.koduid," +
                        "base_in_nomeclature.kod,   lower(base_in_brends_id.prefic || '_' || base_in_nomeclature.kod) AS 'kod_image' \n" +
                        "FROM base_in_nomeclature\n" +
                        "LEFT JOIN base_in_brends_id ON base_in_brends_id.kod=substr(base_in_nomeclature.kod_univ, 1, 8)" +
                        "WHERE kod_image > 0";
                cursor = db_prev.rawQuery(query, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();
                BigDecimal f1 = new BigDecimal(0.0);
                BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(cursor.getCount()));
                while (cursor.isAfterLast() == false) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String koduid = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));
                    String kod = cursor.getString(cursor.getColumnIndexOrThrow("kod"));
                    String kod_image = cursor.getString(cursor.getColumnIndexOrThrow("kod_image"));
                    localContentValues.put("name", name);
                    localContentValues.put("koduid", koduid);
                    localContentValues.put("kod", kod);
                    localContentValues.put("kod_image", kod_image.replace('ц', 'c'));
                    db_prev.insert("base_in_image", null, localContentValues);
                    cursor.moveToNext();
                    Log.e("Image..", name + " ." + kod_image);
                    f1 = f1.add(pointOne);
                    pDialog.setProgress(f1.intValue());
                }
                TimeUnit.SECONDS.sleep(1);
                cursor.close();
                db_prev.close();

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации складов!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации складов!");
            }

        }

        private void getFloor_MTW_In_ResidueGoodsPR() throws InterruptedException {
            try {
                SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
                db.delete("base_in_ostatok", null, null);
                String query_up = "SELECT * FROM base_in_ostatok";
                cursor = db.rawQuery(query_up, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();
                String file_db = WJ_Global_Activity.this.getDatabasePath("MTW_In_ResidueGoodsPR.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                MTW_Ostatok_ResourceParser parser = new MTW_Ostatok_ResourceParser();
                int eventType = xpp.getEventType();
                if (parser.parse(xpp)) {

                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getOstatok().size()));


                    for (MTW_Ostatok ostatok : parser.getOstatok()) {
                        localContentValues.put("data", ostatok.getData());
                        localContentValues.put("sklad_uid", ostatok.getSklad_uid());
                        localContentValues.put("nomenclature_uid", ostatok.getName_uid());
                        localContentValues.put("name", ostatok.getName());
                        localContentValues.put("count", ostatok.getCount());
                        db.insert("base_in_ostatok", null, localContentValues);
                        Log.e("Ostatok..", ostatok.getData() + " " + ostatok.getName() + " " + ostatok.getCount());
                        cursor.moveToNext();
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                    }

                    cursor.close();
                    db.close();
                }

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации остатков!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации остатков!");
            }

        }

        private void getFloor_MTW_In_Price() throws InterruptedException {
            try {
                SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
                db_prev.delete("base_in_price", null, null);
                String query = "SELECT * FROM base_in_price;";
                cursor = db_prev.rawQuery(query, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();

                String file_db = WJ_Global_Activity.this.getDatabasePath("MTW_In_Price.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                MTW_Price_ResourceParser parser = new MTW_Price_ResourceParser();
                int eventType = xpp.getEventType();
                if (parser.parse(xpp)) {
                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getPrice().size()));
                    for (MTW_Price price : parser.getPrice()) {
                        Log.e("Price..", price.getUid() + " Cena.." + price.getCena());
                        localContentValues.put("nomenclature_uid", price.getUid());
                        localContentValues.put("price", price.getCena());
                        db_prev.insert("base_in_price", null, localContentValues);
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                        cursor.moveToNext();
                    }
                }

                TimeUnit.SECONDS.sleep(1);
                cursor.close();
                db_prev.close();

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации цен!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации цен!");
            }

        }

        private void getFloor_MTW_In_Customers() throws InterruptedException {
            try {
                SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
                db_prev.delete("const_contragents", null, null);
                String query = "SELECT * FROM const_contragents;";
                cursor = db_prev.rawQuery(query, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();

                String file_db = WJ_Global_Activity.this.getDatabasePath("MTW_In_Customers.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                MTW_Customers_ResourceParser parser = new MTW_Customers_ResourceParser();
                if (parser.parse(xpp)) {
                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getCustomers().size()));
                    for (MTW_Customers customers : parser.getCustomers()) {
                        localContentValues.put("uid_k_agent", customers.getUid_k_agent());
                        localContentValues.put("k_agent", customers.getK_agent());
                        localContentValues.put("adress", customers.getAdress());
                        localContentValues.put("uid_agent", customers.getUid_agent());
                        localContentValues.put("roaduid", customers.getReaduid());
                        localContentValues.put("roadname", customers.getRoadname());
                        db_prev.insert("const_contragents", null, localContentValues);
                        cursor.moveToNext();
                        Log.e("Clients..", customers.getRoadname() + " ." + customers.getReaduid() + "");
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                    }
                }

                TimeUnit.SECONDS.sleep(1);
                cursor.close();
                db_prev.close();

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации контрагентов!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации контрагентов!");
            }

        }

        private void getFloor_MTW_In_Warehouse() throws InterruptedException {
            try {
                SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
                db_prev.delete("const_sklad", null, null);
                String query = "SELECT * FROM const_sklad;";
                cursor = db_prev.rawQuery(query, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();

                String file_db = WJ_Global_Activity.this.getDatabasePath("MTW_In_Warehouse.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                MTW_Skald_ResourceParser parser = new MTW_Skald_ResourceParser();
                if (parser.parse(xpp)) {
                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getSklads().size()));
                    for (MTW_Sklad sklad : parser.getSklads()) {

                        localContentValues.put("sklad_uid", sklad.getSklad_uid());
                        localContentValues.put("sklad_name", sklad.getSklad_name());
                        db_prev.insert("const_sklad", null, localContentValues);
                        cursor.moveToNext();
                        Log.e("Sklads..", sklad.getSklad_name() + " ." + sklad.getSklad_uid() + "");
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                    }
                }

                TimeUnit.SECONDS.sleep(1);
                cursor.close();
                db_prev.close();
            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации складов!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации складов!");
            }

        }

        private void getFloor_MTW_In_CustomersDebet() throws InterruptedException {
            try {
                SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
                db_prev.delete("otchet_debet", null, null);
                String query = "SELECT * FROM otchet_debet;";
                cursor = db_prev.rawQuery(query, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();

                String file_db = WJ_Global_Activity.this.getDatabasePath("MTW_In_CustomersDebet.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                MTW_CustomersDebet_ResourceParser parser = new MTW_CustomersDebet_ResourceParser();
                if (parser.parse(xpp)) {
                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getCustomersDebet().size()));
                    for (MTW_CustomersDebet debet : parser.getCustomersDebet()) {

                        localContentValues.put("d_agent_name", debet.getAgent());
                        localContentValues.put("d_agent_uid", debet.getUid_agent());
                        localContentValues.put("d_kontr_name", debet.getCustomer());
                        localContentValues.put("d_kontr_uid", debet.getUid_customer());
                        localContentValues.put("d_summa", debet.getSum_debet());
                        db_prev.insert("otchet_debet", null, localContentValues);
                        cursor.moveToNext();
                        Log.e("Debet..", debet.getCustomer() + " ." + debet.getSum_debet() + "");
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                    }
                }
                TimeUnit.SECONDS.sleep(1);
                cursor.close();
                db_prev.close();

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации складов!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации складов!");
            }

        }

        private void getFloor_MTW_In_PaymentOrders() throws InterruptedException {
            pDialog.setMessage("Обработка данных. 7/8 Подождите...");
            try {
                SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
                db_prev.delete("base_in_all_debet", null, null);
                String query = "SELECT * FROM base_in_all_debet;";
                cursor = db_prev.rawQuery(query, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();

                String file_db = WJ_Global_Activity.this.getDatabasePath("MTW_In_CustomersDebet.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                MTW_Skald_ResourceParser parser = new MTW_Skald_ResourceParser();
                if (parser.parse(xpp)) {
                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getSklads().size()));
                    for (MTW_Sklad sklad : parser.getSklads()) {

                        localContentValues.put("sklad_uid", sklad.getSklad_uid());
                        localContentValues.put("sklad_name", sklad.getSklad_name());
                        db_prev.insert("const_sklad", null, localContentValues);
                        cursor.moveToNext();
                        Log.e("UIDYY..", sklad.getSklad_name() + " ." + sklad.getSklad_uid() + "");
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                    }
                }

                TimeUnit.SECONDS.sleep(1);
                cursor.close();
                db_prev.close();

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации складов!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации складов!");
            }

        }
    }

    protected void Write_List_Group_Brends(String w_kod) {

    }


    //////////////////////////////////////////////////////////////////////////////////  ДЛЯ ФИЛИАЛОВ
    // Обновление базы остатков для филиалов
    private class MyAsyncTask_Update_File_MTW_BIshkek extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();

            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            pDialog.setMessage("Обновление файлов. Подождите...");

            pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                getFloor();  // Синхронизация файлов для всех складов
                publishProgress(1);
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
            Toast.makeText(context_Activity, "Загруженно: " + files_kol + " " + files_size, Toast.LENGTH_SHORT).show();
            pDialog.setProgress(0);
            pDialog.cancel();
            pDialog.dismiss();
        }

        private void getFloor() throws InterruptedException {
            pDialog.setMessage("Обновление файлов. Подождите...");
            try {

                FTPClient ftpClient = new FTPClient();
                String server = PEREM_FTP_SERV;
                int port = 21;
                String user = PEREM_FTP_LOGIN;
                String pass = PEREM_FTP_PASS;

                try {
                    ftpClient.connect(server, port);
                    ftpClient.login(user, pass);
                    ftpClient.enterLocalPassiveMode();
                    String remoteDirPath_XML = "/MT_Sunbell_Bishkek/MTW_Data";

                    long[] dirInfo = calculateDirectoryInfo(ftpClient, remoteDirPath_XML, "");
                    System.out.println("Total dirs: " + dirInfo[0]);
                    System.out.println("Total files: " + dirInfo[1]);
                    System.out.println("Total size: " + dirInfo[2]);
                    Log.e("Дирикторий=", " = " + dirInfo[0]);
                    Log.e("Файлов=", " = " + dirInfo[1]);
                    Log.e("Размер=", " = " + dirInfo[2] + " байт");
                    Log.e("Размер=", " = " + dirInfo[2] / 1024 + " кбайт");
                    Log.e("Размер=", " = " + dirInfo[2] / 1048576 + " мбайт");
                    files_kol = dirInfo[1] + "файлов";
                    files_size = dirInfo[2] / 1048576 + " мбайт";

                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(dirInfo[1]));

                    FTPFile[] ftpFiles_list_XML = ftpClient.listFiles(remoteDirPath_XML);
                    for (FTPFile ftpFile_XML : ftpFiles_list_XML) {
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                        String file_server_xml = "/MT_Sunbell_Bishkek/MTW_Data/" + ftpFile_XML.getName(); // путь на сервере
                        if (ftpFile_XML.isFile()) {
                            for (int i = 0; i < mass_update_files.length; i++) {
                                if (ftpFile_XML.getName().equals(mass_update_files[i])) {
                                    String file_db = WJ_Global_Activity.this.getDatabasePath("B_" + mass_update_files[i]).getAbsolutePath(); // путь к databases
                                    // кода для скачивания файла с FTP
                                    outputStream = new FileOutputStream(new File(file_db));  // путь куда сохранить данные
                                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                                    ftpClient.enterLocalPassiveMode();
                                    // ftp: забрать файл из этой директ, в эту директ
                                    ftpClient.retrieveFile(file_server_xml, outputStream);
                                    outputStream.close();
                                }
                            }
                        }
                    }

                    String remoteDirPath_SQL = PEREM_FTP_DISTR_db3;
                    long[] dirInfo2 = calculateDirectoryInfo(ftpClient, remoteDirPath_SQL, "");

                    BigDecimal f2 = new BigDecimal(0.0);
                    BigDecimal pointOne2 = new BigDecimal(100 / Double.valueOf(dirInfo2[1]));


                    FTPFile[] ftpFiles_list_SQL = ftpClient.listFiles(remoteDirPath_SQL);
                    for (FTPFile ftpFile_SQL : ftpFiles_list_SQL) {

                        f2 = f2.add(pointOne2);
                        pDialog.setProgress(f2.intValue());

                        String file_server_sql = remoteDirPath_SQL + "/sunbell_ostatki_filial.db3"; // путь на сервере
                        Log.e("Путь FTP=", remoteDirPath_SQL + "/sunbell_ostatki_filial.db3");

                        if (ftpFile_SQL.isFile()) {
                            String file_db = WJ_Global_Activity.this.getDatabasePath("sunbell_ostatki_filial.db3").getAbsolutePath(); // путь к databases
                            Log.e("Путь DB=", file_db);
                            outputStream = new FileOutputStream(new File(file_db));  // путь куда сохранить данные
                            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                            ftpClient.enterLocalPassiveMode();
                            // ftp: забрать файл из этой директ, в эту директ
                            ftpClient.retrieveFile(file_server_sql, outputStream);
                            outputStream.close();
                        }
                    }


                    ftpClient.logout();
                    ftpClient.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    if (ftpClient.isConnected()) {
                        try {
                            ftpClient.logout();
                            ftpClient.disconnect();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }


            } catch (Exception e) {
                // Toast.makeText(context_Activity, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
            }

        }  // Синхронизация файлов для всех складов

    }

    // Полная синхронизация данных для остатков филиалы
    private class MyAsyncTask_Sync_Ostatki_Golovnoy extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();
            Log.e("Sunc_Start= ", "обработка -> MTW_In_Brends");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            mass = getResources().getStringArray(R.array.mass_all_sync_Bishkek);
            pDialog.setMessage(mass[values[0]] + " \n" + values[0] + "/6 Подождите...");
            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {

                for (int i = 0; i < 7; i++) {
                    switch (i) {
                        case 1: {
                            publishProgress(i);
                            getFloor_MTW_Brends();  // Синхронизация Структуры
                        }
                        break;
                        case 2: {
                            publishProgress(i);
                            getFloor_MTW_In_Nomenclatures(); // Синхронизация номенклатуры
                        }
                        break;
                        case 3: {
                            publishProgress(i);
                            getFloor_Image(); // Синхронизация картинок
                        }
                        break;
                        case 4: {
                            publishProgress(i);
                            getFloor_MTW_In_ResidueGoodsPR(); // Синхронизация остатки
                        }
                        break;
                        case 5: {
                            publishProgress(i);
                            getFloor_MTW_In_Price(); // Синхронизация цены
                        }
                        break;
                        case 6: {
                            publishProgress(i);
                            getFloor_New_Base(); // Синхронизация цены
                        }
                        break;
                       /* case 8: {
                            publishProgress(i);
                            getFloor_MTW_In_CustomersDebet(); // Синхронизация долгов
                        }
                        break;*/
                    }
                }
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
            Log.e("Sunc_End=", "MTW обработаны!");
            Toast.makeText(context_Activity, "Обработка данных окончена!", Toast.LENGTH_LONG).show();
            syns_end = true;
            pDialog.setProgress(0);
            pDialog.dismiss();
        }

        private void getFloor_MTW_Brends() throws InterruptedException {
            pDialog.setMessage("Обработка данных. 1/8 Подождите...");
            SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(BASE_DB_OSTATKI_BISHKEK, MODE_PRIVATE, null);
            db_prev.delete("base_in_brends_id", null, null);
            db_prev.delete("base_in_brends_sub_id", null, null);
            String query = "SELECT * FROM base_in_brends_id;";
            cursor = db_prev.rawQuery(query, null);
            localContentValues_Breds_ID = new ContentValues();
            localContentValues_sub_Breds_ID = new ContentValues();
            cursor.moveToFirst();
            try {
                String file_db = WJ_Global_Activity.this.getDatabasePath("B_MTW_In_Brends.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);

                MTW_SubBrends_ResourceParser parser = new MTW_SubBrends_ResourceParser();
                if (parser.parse(xpp)) {
                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getSubBrends().size()));
                    mass_breds_group = getResources().getStringArray(R.array.mass_breds_group);
                    for (MTW_SubBrends subBrends : parser.getSubBrends()) {

                        if (subBrends.getParents_kod().equals("TRUE")) {
                            grb = "gr_1";
                            localContentValues_Breds_ID.put("brends", subBrends.getName());
                            localContentValues_Breds_ID.put("kod", subBrends.getKod());
                            localContentValues_Breds_ID.put("parent_kod", subBrends.getParents_kod());
                            localContentValues_Breds_ID.put("prefic", subBrends.getPrefic().toLowerCase());
                            for (int i = 0; i < mass_breds_group.length; i++) {
                                if (subBrends.getKod().equals(mass_breds_group[i])) {
                                    grb = "gr_2";
                                }
                            }

                            localContentValues_Breds_ID.put("group_type", grb);
                            db_prev.insert("base_in_brends_id", null, localContentValues_Breds_ID);
                            cursor.moveToNext();

                        } else {
                            localContentValues_sub_Breds_ID.put("subbrends", subBrends.getName());
                            localContentValues_sub_Breds_ID.put("kod", subBrends.getKod());
                            localContentValues_sub_Breds_ID.put("parent_kod", subBrends.getParents_kod());
                            db_prev.insert("base_in_brends_sub_id", null, localContentValues_sub_Breds_ID);
                            cursor.moveToNext();
                        }

                        Log.e("B_Brends..", subBrends.getName() + " ." + subBrends.getPrefic() + "");
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());

                    }
                }
                cursor.close();
                db_prev.close();
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации структуры!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации структуры!");
            }

        }

        private void getFloor_MTW_In_Nomenclatures() throws InterruptedException {

            SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(BASE_DB_OSTATKI_BISHKEK, MODE_PRIVATE, null);
            db_prev.delete("base_in_nomeclature", null, null);
            String query = "SELECT * FROM base_in_nomeclature;";
            cursor = db_prev.rawQuery(query, null);
            localContentValues = new ContentValues();
            cursor.moveToFirst();
            try {
                String file_db = WJ_Global_Activity.this.getDatabasePath("B_MTW_In_Nomenclatures.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы

                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                MTW_Nomenclatures_ResourceParser parser = new MTW_Nomenclatures_ResourceParser();
                parserFactory.setNamespaceAware(true);
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                if (parser.parse(xpp)) {
                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getTovar().size()));
                    for (MTW_Nomenclatures tovar : parser.getTovar()) {

                        /*if (!tovar.getBrends().equals("")) {

                        } else {
                            cursor.moveToNext();
                        }*/
                        localContentValues.put("brends", tovar.getBrends());
                        localContentValues.put("p_group", tovar.getP_group());
                        localContentValues.put("kod", tovar.getKod());
                        localContentValues.put("name", tovar.getName());
                        localContentValues.put("kolbox", tovar.getKolbox());
                        localContentValues.put("cena", tovar.getCena());
                        localContentValues.put("strih", tovar.getStrih());
                        localContentValues.put("koduid", tovar.getKoduid().trim());

                        if (tovar.getKod_univ().length() > 25) {
                            localContentValues.put("kod_univ", tovar.getKod_univ());
                            db_prev.insert("base_in_nomeclature", null, localContentValues);
                            Log.e("B_Tovar..", tovar.getBrends() + " " + tovar.getP_group() + "");
                            f1 = f1.add(pointOne);
                            pDialog.setProgress(f1.intValue());
                            cursor.moveToNext();
                        } else cursor.moveToNext();


                       /* 0A117DC7-93F0-4D61-9DDF-72CCDD39B1C6
                        4163C3D7-9757-4F90-9F9B-389B0F2213E8    *
                        00000001/00000238/BL000606*/







                       /* if (!tovar.getBrends().equals("")) {
                            localContentValues.put("subbrends", subBrends.getSubbrends());
                            localContentValues.put("kod", subBrends.getKod());
                            localContentValues.put("parents_kod", subBrends.getParents_kod());
                            db_prev.insert("base8_Group_ID", null, localContentValues);
                            Log.e("UIDYY..", subBrends.getSubbrends() + " ." + subBrends.getParents_kod() + "");
                            f1 = f1.add(pointOne);
                            pDialog.setProgress(f1.intValue());
                            cursor.moveToNext();
                        } else {
                            localContentValues.put("subbrends", subBrends.getSubbrends());
                            localContentValues.put("kod", subBrends.getKod());
                            localContentValues.put("parents_kod", subBrends.getParents_kod());
                            db_prev.insert("base7_Brends_ID", null, localContentValues);
                            Log.e("TRUE..", subBrends.getSubbrends() + " ." + subBrends.getParents_kod() + "");
                            cursor.moveToNext();
                        }*/


                    }
                }
                TimeUnit.SECONDS.sleep(1);
                cursor.close();
                db_prev.close();

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации номенклатуры!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации номенклатуры!");
            }

        }

        private void getFloor_Image() throws InterruptedException {
            try {
                SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(BASE_DB_OSTATKI_BISHKEK, MODE_PRIVATE, null);
                db_prev.delete("base_in_image", null, null);

                String query = "SELECT base_in_nomeclature.name, base_in_nomeclature.koduid," +
                        "base_in_nomeclature.kod,   lower(base_in_brends_id.prefic || '_' || base_in_nomeclature.kod) AS 'kod_image' \n" +
                        "FROM base_in_nomeclature\n" +
                        "LEFT JOIN base_in_brends_id ON base_in_brends_id.kod=substr(base_in_nomeclature.kod_univ, 1, 8)" +
                        "WHERE kod_image > 0";
                cursor = db_prev.rawQuery(query, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();
                BigDecimal f1 = new BigDecimal(0.0);
                BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(cursor.getCount()));
                while (cursor.isAfterLast() == false) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String koduid = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));
                    String kod = cursor.getString(cursor.getColumnIndexOrThrow("kod"));
                    String kod_image = cursor.getString(cursor.getColumnIndexOrThrow("kod_image"));
                    localContentValues.put("name", name);
                    localContentValues.put("koduid", koduid);
                    localContentValues.put("kod", kod);
                    localContentValues.put("kod_image", kod_image.replace('ц', 'c'));
                    db_prev.insert("base_in_image", null, localContentValues);
                    cursor.moveToNext();
                    // Log.e("UIDYY..", name + " ." + kod_image);
                    f1 = f1.add(pointOne);
                    pDialog.setProgress(f1.intValue());
                }
                TimeUnit.SECONDS.sleep(1);
                cursor.close();
                db_prev.close();

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации складов!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации складов!");
            }

        }

        private void getFloor_MTW_In_ResidueGoodsPR() throws InterruptedException {
            try {
                SQLiteDatabase db = getBaseContext().openOrCreateDatabase(BASE_DB_OSTATKI_BISHKEK, MODE_PRIVATE, null);
                db.delete("base_in_ostatok", null, null);
                String query_up = "SELECT * FROM base_in_ostatok";
                cursor = db.rawQuery(query_up, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();
                String file_db = WJ_Global_Activity.this.getDatabasePath("B_MTW_In_ResidueGoodsPR.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                MTW_Ostatok_ResourceParser parser = new MTW_Ostatok_ResourceParser();
                int eventType = xpp.getEventType();
                if (parser.parse(xpp)) {

                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getOstatok().size()));


                    for (MTW_Ostatok ostatok : parser.getOstatok()) {
                        localContentValues.put("data", ostatok.getData());
                        localContentValues.put("sklad_uid", ostatok.getSklad_uid());
                        localContentValues.put("nomenclature_uid", ostatok.getName_uid());
                        localContentValues.put("name", ostatok.getName());
                        localContentValues.put("count", ostatok.getCount());
                        db.insert("base_in_ostatok", null, localContentValues);
                        Log.e("SYNC..", ostatok.getData() + " " + ostatok.getName() + " " + ostatok.getCount());
                        cursor.moveToNext();
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                    }

                    cursor.close();
                    db.close();
                }

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации остатков!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации остатков!");
            }

        }

        private void getFloor_MTW_In_Price() throws InterruptedException {
            try {
                SQLiteDatabase db_prev = getBaseContext().openOrCreateDatabase(BASE_DB_OSTATKI_BISHKEK, MODE_PRIVATE, null);
                db_prev.delete("base_in_price", null, null);
                String query = "SELECT * FROM base_in_price;";
                cursor = db_prev.rawQuery(query, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();

                String file_db = WJ_Global_Activity.this.getDatabasePath("B_MTW_In_Price.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                MTW_Price_ResourceParser parser = new MTW_Price_ResourceParser();
                int eventType = xpp.getEventType();
                if (parser.parse(xpp)) {
                    BigDecimal f1 = new BigDecimal(0.0);
                    BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(parser.getPrice().size()));
                    for (MTW_Price price : parser.getPrice()) {
                        Log.e("UIDYY..", price.getUid() + " Cena.." + price.getCena());
                        localContentValues.put("nomenclature_uid", price.getUid());
                        localContentValues.put("price", price.getCena());
                        db_prev.insert("base_in_price", null, localContentValues);
                        f1 = f1.add(pointOne);
                        pDialog.setProgress(f1.intValue());
                        cursor.moveToNext();
                    }
                }

                TimeUnit.SECONDS.sleep(1);
                cursor.close();
                db_prev.close();

            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации цен!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации цен!");
            }

        }

        private void getFloor_New_Base() throws InterruptedException {
            try {
                SQLiteDatabase db = getBaseContext().openOrCreateDatabase(BASE_DB_OSTATKI_BISHKEK, MODE_PRIVATE, null);
                db.delete("base_in_ostatok_golovnoy", null, null);

                String query_up = "SELECT base_in_nomeclature.brends, base_in_nomeclature.p_group, " +
                        "base_in_nomeclature.name, base_in_ostatok.count, base_in_price.price, \n" +
                        "base_in_image.kod_image, base_in_ostatok.sklad_uid\n" +
                        "FROM base_in_nomeclature\n" +
                        "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                        "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                        "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                        "WHERE base_in_ostatok.count >0";

                cursor = db.rawQuery(query_up, null);
                localContentValues = new ContentValues();
                cursor.moveToFirst();
                BigDecimal f1 = new BigDecimal(0.0);
                BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(cursor.getCount()));

                while (cursor.isAfterLast() == false) {
                    localContentValues.put("brends", cursor.getString(cursor.getColumnIndexOrThrow("brends")));
                    localContentValues.put("p_brends", cursor.getString(cursor.getColumnIndexOrThrow("p_group")));
                    localContentValues.put("name", cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    localContentValues.put("count", cursor.getString(cursor.getColumnIndexOrThrow("count")));
                    localContentValues.put("price", cursor.getString(cursor.getColumnIndexOrThrow("price")));
                    localContentValues.put("kod_image", cursor.getString(cursor.getColumnIndexOrThrow("kod_image")));
                    localContentValues.put("sklad_uid", cursor.getString(cursor.getColumnIndexOrThrow("sklad_uid")));
                    db.insert("base_in_ostatok_golovnoy", null, localContentValues);
                    cursor.moveToNext();

                    f1 = f1.add(pointOne);
                    pDialog.setProgress(f1.intValue());
                }
                cursor.close();
                db.close();

                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {

                Toast.makeText(context_Activity, "Ошибка синхронизации цен!", Toast.LENGTH_SHORT).show();
                Log.e("Error...", "Ошибка синхронизации цен!");
            }

        }

    }





    /*
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     * */
}

