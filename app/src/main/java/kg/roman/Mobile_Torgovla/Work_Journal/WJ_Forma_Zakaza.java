package kg.roman.Mobile_Torgovla.Work_Journal;

import android.app.DatePickerDialog;
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
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterAde_List_RN_Table;
import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_List_RN_Table;
import kg.roman.Mobile_Torgovla.FTP.Sunbell_FtpAction;
import kg.roman.Mobile_Torgovla.FTP.Sunbell_FtpAsyncTask_Zakaz_UP_FTP;
import kg.roman.Mobile_Torgovla.FTP.Sunbell_FtpConnection;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Dialog_Forma;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Spinner_Filter;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Dialog_Forma;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Spinner_Filter;
import kg.roman.Mobile_Torgovla.MailSenderClass;
import kg.roman.Mobile_Torgovla.Permission.PermissionUtils;
import kg.roman.Mobile_Torgovla.R;

public class WJ_Forma_Zakaza extends AppCompatActivity {
    public ArrayList<ListAdapterSimple_List_RN_Table> zakaz = new ArrayList<ListAdapterSimple_List_RN_Table>();
    public ListAdapterAde_List_RN_Table adapterPriceClients_RN;

    public ArrayList<ListAdapterSimple_Spinner_Filter> spinner_filters = new ArrayList<ListAdapterSimple_Spinner_Filter>();
    public ListAdapterAde_Spinner_Filter adapterPriceClients;

    public ArrayList<ListAdapterSimple_Dialog_Forma> dialo_forma = new ArrayList<ListAdapterSimple_Dialog_Forma>();
    public ListAdapterAde_Dialog_Forma adapterPriceClients_dialog_forma;

    public androidx.appcompat.app.AlertDialog.Builder dialog_S;
    public AlertDialog.Builder builder;
    public AlertDialog dialog;
    public String dialog_summa, dialog_Itogo, dialog_kol, trade_summa, trade_itogo;
    public String local_text_kod_rn, local_text_client_uid, local_text_debet, local_text_itogo, local_text_data, local_text_status_rn, local_text_skidka_rn;
    private final static String TAG = "MainActivity";
    Calendar Calendar_Name = Calendar.getInstance();
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day;
    public String this_data_now, this_data_filter;
    public androidx.appcompat.app.AlertDialog.Builder dialog_cancel;
    public Context context_Activity;
    public ProgressDialog pDialog;
    public SharedPreferences sp;
    public SharedPreferences.Editor ed;
    public androidx.appcompat.app.AlertDialog.Builder dialog_list;
    public ListView listView;
    public Button btn_add, btn_filter, btn_xml, btn_summa, btn_all_delete, button_ok, button_cancel;
    public Spinner spinner;
    public String from, PARAMS, attach, text, title, where, new_write;
    public String data_mail_login, data_mail_pass, data_mail_where, data_mail_from;
    public Integer count_tt;

    public String PEREM_FTP_SERV, PEREM_FTP_LOGIN, PEREM_FTP_PASS, PEREM_FTP_DISTR_XML, PEREM_FTP_DISTR_db3,
            PEREM_IMAGE_PUT_SDCARD, PEREM_IMAGE_PUT_PHONE;
    public String PEREM_MAIL_LOGIN, PEREM_MAIL_PASS, PEREM_MAIL_START, PEREM_MAIL_END,
            PEREM_DB3_CONST, PEREM_DB3_BASE, PEREM_DB3_RN, PEREM_ANDROID_ID_ADMIN, PEREM_ANDROID_ID;
    public String PEREM_AG_UID, PEREM_AG_NAME, PEREM_AG_REGION, PEREM_AG_UID_REGION, PEREM_AG_CENA,
            PEREM_AG_SKLAD, PEREM_AG_UID_SKLAD, PEREM_AG_TYPE_REAL, PEREM_AG_TYPE_USER,
            PEREM_WORK_DISTR, PEREM_KOD_MOBILE, PEREM_KOD_UID_KODRN, PEREM_FTP_PUT;
    public String PEREM_KLIENT_UID, PEREM_DIALOG_UID, PEREM_DIALOG_DATA_START, PEREM_DIALOG_DATA_END, PEREM_DISPLAY_START, PEREM_DISPLAY_END;

    public Calendar c = Calendar.getInstance();
    public int mYear, mMonth, mDay;
    public String Data_Filtr_Start, Data_Filtr_End, Data_Filtr_Start_Work, Data_Filtr_End_Work, UID_Klient;
    public SimpleDateFormat dateFormat_work, dateFormat_display;
    public String Dialog_Data_Start, Dialog_Data_End, Dialog_Data_UID_Klients, query_SUMMA, query_KolTT;
    public String Summa_Data_Start, Summa_Data_End, Summa_UID, Summa_Data_Display_Start, Summa_Data_Display_End;
    public String[] mass_kod_rn, mass_dialog;
    public Boolean boolean_tvw_error, checkBox_Data_Clic, checkBox_Klients_Clic;
    public TextView tvw_error;
    public Button btn_data_start, btn_data_end;
    public CheckBox checkBox_client, checkBox_Data;
    public View localView;
    public XmlSerializer serializer;
    public String XML_Data_Start, XML_Data_End, XML_NEW_NAME;
    public Cursor cursor;
    public FloatingActionButton floatingActionButton;
    private static final int PERMISSION_STORAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_forma_1);
        // Порошок автомат ЧАЙКА 2кг ЯРКИЙ ЦВЕТ/ЗИМ. СВЕЖЕСТЬ  3017
        Log.d(TAG, "onCreate");

        context_Activity = WJ_Forma_Zakaza.this;
        dialog_kol = "0";
        dialog_summa = "0.0";
        dialog_Itogo = "0.0";
        boolean_tvw_error = false;
        checkBox_Data_Clic = false;
        checkBox_Klients_Clic = false;
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        Calendate_New();
        Constanta_Read();
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza_L1.class);
                startActivity(intent);
                finish();
                ed = sp.edit();
                ed.putString("PEREM_LIST_ADAPTER_DEBET", "list_debet");
                ed.commit();
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

/*

       /* ed = sp.edit();
        ed.putString("PEREM_DIALOG_UID", "All");  // передача кода выбранного uid клиента
        ed.putString("PEREM_DIALOG_DATA_START", this_data_filter);  // передача кода начальной даты
        ed.putString("PEREM_DIALOG_DATA_END", this_data_filter);  // передача кода конечной даты
        ed.putString("PEREM_DISPLAY_START", this_data_now);  // передача кода начальной даты
        ed.putString("PEREM_DISPLAY_END", this_data_now);  // передача кода конечной даты
        ed.commit();*/


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_icons_1);
        // getSupportActionBar().setTitle("Форма заказа");
        getSupportActionBar().setSubtitle("Рабочая дата: " + this_data_now);

        listView = findViewById(R.id.list_forma);
        Button_Group(); // группа кнопок

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                local_text_kod_rn = ((TextView) view.findViewById(R.id.tvw_rn_RN)).getText().toString();
                local_text_client_uid = ((TextView) view.findViewById(R.id.tvw_rn_K_UID)).getText().toString();
                local_text_debet = ((TextView) view.findViewById(R.id.tvw_rn_debet)).getText().toString();
                local_text_itogo = ((TextView) view.findViewById(R.id.tvw_rn_Itogo)).getText().toString();
                local_text_data = ((TextView) view.findViewById(R.id.tvw_rn_Data)).getText().toString();
                local_text_status_rn = ((TextView) view.findViewById(R.id.text_status)).getText().toString();
                local_text_skidka_rn = ((TextView) view.findViewById(R.id.tvw_rn_skidka)).getText().toString();


                builder = new AlertDialog.Builder(context_Activity);
                builder.setTitle("Выберите тип операции...");
                builder.setIcon(R.drawable.office_title_forma_zakaz);
                dialo_forma.clear();
                mass_dialog = getResources().getStringArray(R.array.mass_for_dialof_forma);

                for (int i = 0; i < mass_dialog.length; i++) {
                    dialo_forma.add(new ListAdapterSimple_Dialog_Forma(mass_dialog[i], ""));
                }
                adapterPriceClients_dialog_forma = new ListAdapterAde_Dialog_Forma(context_Activity, dialo_forma);
                adapterPriceClients_dialog_forma.notifyDataSetChanged();

                builder.setAdapter(adapterPriceClients_dialog_forma, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("Tag", "Selected item on position " + mass_dialog[which]);
                        switch (mass_dialog[which]) {
                            case "Просмотр": {
                                Intent intent = new Intent(context_Activity, WJ_Dialog_RN_Screen.class);
                                intent.putExtra("RN", local_text_kod_rn);
                                intent.putExtra("Data", local_text_data);
                                ed = sp.edit();
                                ed.putString("PEREM_READ_KODRN", local_text_kod_rn);  // передача кода конечной даты
                                ed.putString("PEREM_TYPE_MENU", "Просмотр");  // передача кода конечной даты
                                ed.commit();
                                startActivity(intent);
                                finish();
                            }
                            break;
                            case "Редактировать": {
                                try {
                                    if (local_text_status_rn.equals("false")) {
                                        Intent intent = new Intent(context_Activity, WJ_Dialog_RN_Edit.class);
                                        intent.putExtra("ReWrite_RN", local_text_kod_rn);
                                        intent.putExtra("ReWrite_Summa", local_text_itogo);
                                        intent.putExtra("ReWrite_Summa_Debet", local_text_debet);
                                        intent.putExtra("ReWrite_Skidka", local_text_skidka_rn);
                                        intent.putExtra("ReWrite_Client_UID", local_text_client_uid);
                                        ed = sp.edit();
                                        ed.putString("PEREM_READ_KODRN", local_text_kod_rn);  // передача кода конечной даты
                                        ed.putString("PEREM_TYPE_MENU", "Редактировать");  // передача кода конечной даты
                                        ed.commit();
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(context_Activity, "редактирование не возможно, данные уже в обработке, обратитесь к оператору!", Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    Log.e("WJ_FORMA_ZAKAZA:", "Ошибка: ошибка пункта редактирования!");
                                    Toast.makeText(context_Activity, "Ошибка: ошибка пункта редактирования!", Toast.LENGTH_SHORT).show();
                                }


                            }
                            break;
                            case "Удалить": {
                                try {
                                    if (local_text_status_rn.equals("false")) {
                                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context_Activity);
                                        builder.setMessage("Удалить текущий заказ?")
                                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        Delete_RN_BASE(local_text_kod_rn);
                                                        zakaz.clear();
                                                        Loading_List_Filter(this_data_filter, this_data_filter, "All");
                                                        adapterPriceClients_RN = new ListAdapterAde_List_RN_Table(WJ_Forma_Zakaza.this, zakaz);
                                                        adapterPriceClients_RN.notifyDataSetChanged();
                                                        listView.setAdapter(adapterPriceClients_RN);
                                                        Debet_ReWrite(local_text_client_uid, local_text_itogo);
                                                    }
                                                })
                                                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                    }
                                                });
                                        builder.show();
                                    } else {
                                        Toast.makeText(context_Activity, "удаление не возможно, данные уже в обработке, обратитесь к оператору!", Toast.LENGTH_LONG).show();
                                    }


                                } catch (Exception e) {
                                    Log.e("WJ_FORMA_ZAKAZA:", "Ошибка: ошибка удаления данных!");
                                    Toast.makeText(context_Activity, "Ошибка: ошибка удаления данных!", Toast.LENGTH_SHORT).show();
                                }

                            }
                            break;
                        }
                    }
                });
                dialog = builder.create();
                dialog.show();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
//kg.roman.Mobile_Torgovla level:error
        //package:kg.roman.Mobile_Torgovla level:error
        try {
            Constanta_Read();
            // Выборка для выгрузки данных опеределенного пользователя
            /*if (!PEREM_DIALOG_UID.equals("")) {
                zakaz.clear();
                Loading_List_Filter(PEREM_DIALOG_DATA_START, PEREM_DIALOG_DATA_END, PEREM_DIALOG_UID);
                adapterPriceClients_RN = new ListAdapterAde_List_RN_Table(WJ_Forma_Zakaza.this, zakaz);
                adapterPriceClients_RN.notifyDataSetChanged();
                listView.setAdapter(adapterPriceClients_RN);
            } else {
                zakaz.clear();
                Loading_List_Filter(this_data_filter, this_data_filter, "All");
                adapterPriceClients_RN = new ListAdapterAde_List_RN_Table(WJ_Forma_Zakaza.this, zakaz);
                adapterPriceClients_RN.notifyDataSetChanged();
                listView.setAdapter(adapterPriceClients_RN);
            }*/
            zakaz.clear();
            Loading_List_Filter(this_data_filter, this_data_filter, "All");
            adapterPriceClients_RN = new ListAdapterAde_List_RN_Table(WJ_Forma_Zakaza.this, zakaz);
            adapterPriceClients_RN.notifyDataSetChanged();
            listView.setAdapter(adapterPriceClients_RN);

        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка возврата данных: Resume", Toast.LENGTH_SHORT).show();
        }
    }

    // кнопка назад
    public void onBackPressed() {
        Intent intent_prefActivity = new Intent(context_Activity, WJ_Global_Activity.class);
        startActivity(intent_prefActivity);
        finish();
    }

    // меню менеджер
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getSupportActionBar().hide();
        getMenuInflater().inflate(R.menu.menu_forma_rn, menu);

       /* SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));*/

        return true;
    }

    // меню менеджер
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        try {
            switch (id) {
                case R.id.menu_filter: {
                    try {
                        dialog_kol = "0";
                        dialog_summa = "0.0";
                        dialog_Itogo = "0.0";
                        Data_Filtr_Start_Work = "";
                        Data_Filtr_End_Work = "";
                        Dialog_Data_UID_Klients = "";
                        checkBox_Data_Clic = false;
                        checkBox_Klients_Clic = false;

                        final ConstraintLayout constraintLayout;
                        localView = LayoutInflater.from(context_Activity).inflate(R.layout.mt_dialog_filter_work_table, null);
                        spinner = (Spinner) localView.findViewById(R.id.spinner_filter_klients);
                        btn_data_start = (Button) localView.findViewById(R.id.btn_filter_data_start);
                        btn_data_end = (Button) localView.findViewById(R.id.btn_filter_data_end);
                        checkBox_client = (CheckBox) localView.findViewById(R.id.checkBox_client);
                        checkBox_Data = (CheckBox) localView.findViewById(R.id.checkBox_Data);
                        constraintLayout = (ConstraintLayout) localView.findViewById(R.id.constr_layout);
                        tvw_error = ((TextView) localView.findViewById(R.id.tvw_error));

                        spinner.setVisibility(View.GONE);
                        constraintLayout.setVisibility(View.GONE);
                        tvw_error.setVisibility(View.GONE);
                        UID_Klient = "";
                        checkBox_client.setChecked(false);
                        checkBox_Data.setChecked(false);

                        checkBox_client.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (b == true) {
                                    spinner.setVisibility(View.VISIBLE);
                                    checkBox_Klients_Clic = true;
                                    spinner_filters.clear();
                                    Loading_Data_Spinner();
                                    adapterPriceClients = new ListAdapterAde_Spinner_Filter(context_Activity, spinner_filters);
                                    adapterPriceClients.notifyDataSetChanged();
                                    spinner.setAdapter(adapterPriceClients);
                                } else {
                                    spinner.setVisibility(View.GONE);
                                    checkBox_Klients_Clic = false;
                                    spinner_filters.clear();
                                    spinner_filters.add(new ListAdapterSimple_Spinner_Filter("", "", ""));
                                    adapterPriceClients = new ListAdapterAde_Spinner_Filter(context_Activity, spinner_filters);
                                    adapterPriceClients.notifyDataSetChanged();
                                    spinner.setAdapter(adapterPriceClients);

                                }

                            }
                        });

                        checkBox_Data.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (b == true) {
                                    constraintLayout.setVisibility(View.VISIBLE);
                                    checkBox_Data_Clic = true;
                                    Data_Filtr_End_Work = this_data_filter;
                                    btn_data_end.setText(this_data_now);
                                    Data_Filtr_Start_Work = this_data_filter;
                                    Data_Filtr_End_Work = this_data_filter;
                                    Data_Filtr_End = this_data_now;
                                } else {
                                    constraintLayout.setVisibility(View.GONE);
                                    checkBox_Data_Clic = false;
                                    Data_Filtr_Start = "начальная дата...";
                                    btn_data_start.setText(Data_Filtr_Start);
                                    Data_Filtr_End = "конечная дата...";
                                    btn_data_end.setText(Data_Filtr_End);
                                }
                            }
                        });

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                String k_agent = ((TextView) view.findViewById(R.id.tvw_filter_name)).getText().toString();
                                UID_Klient = ((TextView) view.findViewById(R.id.tvw_filter_uid)).getText().toString();
                                Toast.makeText(context_Activity, k_agent + "\n" + UID_Klient, Toast.LENGTH_SHORT).show();
                                ed = sp.edit();
                                ed.putString("PEREM_DIALOG_UID", UID_Klient);  // передача кода выбранного uid клиента
                                ed.putString("PEREM_DIALOG_INT", String.valueOf(i));  // передача кода начальной даты
                                ed.putString("PEREM_DISPLAY_START", this_data_now);  // передача кода начальной даты
                                ed.putString("PEREM_DISPLAY_END", this_data_now);  // передача кода конечной даты
                                ed.commit();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });


                        btn_data_start.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatePickerDialog datePickerDialog = new DatePickerDialog(context_Activity,
                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year,
                                                                  int monthOfYear, int dayOfMonth) {
                                                Calendar calendar = Calendar.getInstance();
                                                calendar.set(year, monthOfYear, dayOfMonth);
                                                dateFormat_work = new SimpleDateFormat("yyyy-MM-dd");
                                                dateFormat_display = new SimpleDateFormat("dd-MM-yyyy");
                                                Data_Filtr_Start_Work = dateFormat_work.format(calendar.getTime());
                                                Data_Filtr_Start = dateFormat_display.format(calendar.getTime());

                                                btn_data_start.setText(Data_Filtr_Start);
                                                if (checkBox_Data.isChecked()) {
                                                    if (Data_Filtr_Start_Work.compareTo(Data_Filtr_End_Work) <= 0) {
                                                        tvw_error.setVisibility(View.GONE);
                                                        boolean_tvw_error = false;
                                                        Log.e("Return", "Верный формат дат");
                                                    } else {
                                                        tvw_error.setVisibility(View.VISIBLE);
                                                        boolean_tvw_error = true;
                                                        Log.e("Return", "Не верный формат дат");
                                                    }
                                                }

                                                Log.e("Выбор даты1: ", Data_Filtr_Start);
                                                Log.e("Выбор даты2: ", Data_Filtr_End);
                                                Log.e("Выбор даты3: ", "__" + Data_Filtr_Start_Work);
                                                Log.e("Выбор даты4: ", "__" + Data_Filtr_End_Work);

                                            }
                                        }, mYear, mMonth, mDay);

                         /*   datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Отмена1",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (which == DialogInterface.BUTTON_NEGATIVE) {
                                                dialog.cancel();
                                                Log.e("Return", "Нажато нет");
                                            }
                                        }
                                    });


                            datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                                Log.e("Return", "Нажато Ок");
                                            }
                                        }
                                    });*/
                                datePickerDialog.show();
                            }
                        });

                        btn_data_end.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatePickerDialog datePickerDialog = new DatePickerDialog(context_Activity,
                                        new DatePickerDialog.OnDateSetListener() {

                                            @Override
                                            public void onDateSet(DatePicker view, int year,
                                                                  int monthOfYear, int dayOfMonth) {
                                                Calendar calendar = Calendar.getInstance();
                                                calendar.set(year, monthOfYear, dayOfMonth);
                                                dateFormat_work = new SimpleDateFormat("yyyy-MM-dd");
                                                dateFormat_display = new SimpleDateFormat("dd-MM-yyyy");
                                                Data_Filtr_End_Work = dateFormat_work.format(calendar.getTime());
                                                Data_Filtr_End = dateFormat_display.format(calendar.getTime());
                                                btn_data_end.setText(Data_Filtr_End);
                                                if (checkBox_Data.isChecked()) {
                                                    if (Data_Filtr_Start_Work.compareTo(Data_Filtr_End_Work) <= 0) {
                                                        tvw_error.setVisibility(View.GONE);
                                                        boolean_tvw_error = false;
                                                        Log.e("Return", "Верный формат дат");
                                                    } else {
                                                        tvw_error.setVisibility(View.VISIBLE);
                                                        boolean_tvw_error = true;
                                                        Log.e("Return", "Не верный формат дат");
                                                    }
                                                }

                                            }
                                        }, mYear, mMonth, mDay);
                                datePickerDialog.show();
                            }
                        });


                        android.app.AlertDialog.Builder localBuilder = new android.app.AlertDialog.Builder(context_Activity);
                        localBuilder.setView(localView);
                        localBuilder.setTitle("Фильтр данных");
                        localBuilder.setCancelable(false).setIcon(R.drawable.office_filter).setPositiveButton(" ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                try {
                                    Dialog_Data_UID_Klients = "All";
                                    Dialog_Data_Start = this_data_filter;
                                    Dialog_Data_End = this_data_filter;

                                    if (boolean_tvw_error.booleanValue()) {
                                        Dialog_Data_Start = this_data_filter;
                                        Dialog_Data_End = this_data_filter;
                                    } else {
                                        Dialog_Data_Start = Data_Filtr_Start_Work;
                                        Dialog_Data_End = Data_Filtr_End_Work;
                                    }

                                    if (!checkBox_Data.isChecked()) {
                                        Dialog_Data_Start = this_data_filter;
                                        Dialog_Data_End = this_data_filter;
                                    }

                                    if (checkBox_client.isChecked()) {
                                        Dialog_Data_UID_Klients = UID_Klient;
                                    } else
                                        Dialog_Data_UID_Klients = "All";

                                    ed = sp.edit();
                                    ed.putString("PEREM_DIALOG_UID", Dialog_Data_UID_Klients);  // передача кода выбранного uid клиента
                                    ed.putString("PEREM_DIALOG_DATA_START", Dialog_Data_Start);  // передача кода начальной даты
                                    ed.putString("PEREM_DIALOG_DATA_END", Dialog_Data_End);  // передача кода конечной даты
                                    ed.putString("PEREM_DISPLAY_START", Data_Filtr_Start);  // передача кода начальной даты
                                    ed.putString("PEREM_DISPLAY_END", Data_Filtr_End);  // передача кода конечной даты
                                    ed.commit();

                                    zakaz.clear();
                                    Loading_List_Filter(Dialog_Data_Start, Dialog_Data_End, Dialog_Data_UID_Klients);
                                    Log.e("Выбор: ", Dialog_Data_Start);
                                    Log.e("Выбор: ", Dialog_Data_End);
                                    // getSupportActionBar().setSubtitle("Данные с " + Data_Filtr_Start + " по \n" + Data_Filtr_End);
                                    adapterPriceClients_RN = new ListAdapterAde_List_RN_Table(WJ_Forma_Zakaza.this, zakaz);
                                    adapterPriceClients_RN.notifyDataSetChanged();
                                    listView.setAdapter(adapterPriceClients_RN);
                                } catch (Exception e) {
                                    Toast.makeText(context_Activity, "Ошибка кнопки фильтр!", Toast.LENGTH_SHORT).show();
                                    Log.e("File", "Ошибка кнопки фильтр!");
                                }

                                paramDialogInterface.cancel();


                            }
                        }).setNegativeButton(" ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                                paramDialogInterface.cancel();

                            }
                        });


                        android.app.AlertDialog localAlertDialog = localBuilder.create();
                        localAlertDialog.show();

                        button_ok = localAlertDialog.getButton(-1);
                        button_ok.setCompoundDrawablesWithIntrinsicBounds(R.drawable.button_ok, 0, 0, 0);
                        button_cancel = localAlertDialog.getButton(-2);
                        button_cancel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_close, 0);
                    } catch (Exception e) {

                    }

                }
                break;
                case R.id.menu_sync_up: {
                    dialog_cancel = new androidx.appcompat.app.AlertDialog.Builder(context_Activity);
                    dialog_cancel.setTitle("Создать отчет за день?");
                    dialog_cancel.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                            pDialog = new ProgressDialog(context_Activity);
                            pDialog.setMessage("Загрузка продуктов...");
                            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            pDialog.setProgress(0);
                            pDialog.setMax(100);
                            pDialog.show();

                            Constanta_Read();
                            Log.e("File", "TT Button_Up");

                            WJ_Forma_Zakaza.MyAsyncTask_Sync_New_XML asyncTask = new WJ_Forma_Zakaza.MyAsyncTask_Sync_New_XML();
                            asyncTask.execute();
                        }

                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        }
                    });
                    dialog_cancel.show();
                }
                break;

            }
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка вход в настройки", Toast.LENGTH_SHORT).show();
            Log.e(this.getLocalClassName(), "Ошибка вход в настройки");
        }



      /*  if (id == R.id.action_settings) {



            return true;
        }*/

        return super.onOptionsItemSelected(item);
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

        Calendar_Name = Calendar.getInstance();
        Calendar_Name.set(Integer.parseInt(this_rn_year), Integer.valueOf(this_rn_month) - 1, Integer.valueOf(this_rn_day));

        SimpleDateFormat dateFormat_display = new SimpleDateFormat("dd-MM-yyyy");  // формат для экрана даты
        SimpleDateFormat dateFormat_filter = new SimpleDateFormat("yyyy-MM-dd");  // формат для сортировки даты

        String Data_Display_now = dateFormat_display.format(Calendar_Name.getTime());
        String Data_filter_now = dateFormat_filter.format(Calendar_Name.getTime());

        this_data_now = Data_Display_now;
        this_data_filter = Data_filter_now;
        Log.e(this.getLocalClassName(), "Рабочий день на:" + this_data_now);
        Log.e(this.getLocalClassName(), "Фильтр день на:" + this_data_filter);




        /*this_vrema_work_now = this_rn_vrema;
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Integer.parseInt(this_rn_year), Integer.valueOf(this_rn_month) - 1, 01);
        SimpleDateFormat dateFormat_One = new SimpleDateFormat("yyyy-MM-dd");
        String dateString_One = dateFormat_One.format(calendar.getTime());
        this_data_one = dateString_One;*/


    }

    // Кнопочная панель
    protected void Button_Group() {
        btn_add = (Button) findViewById(R.id.button_add);
        btn_filter = (Button) findViewById(R.id.button_filter);
        btn_xml = (Button) findViewById(R.id.button_xml);
        btn_summa = (Button) findViewById(R.id.button_autosumma);
        btn_all_delete = (Button) findViewById(R.id.button_delete);

        btn_add.setVisibility(View.GONE);
        btn_filter.setVisibility(View.GONE);
        btn_xml.setVisibility(View.GONE);
        btn_summa.setVisibility(View.GONE);
        btn_all_delete.setVisibility(View.GONE);

        ed = sp.edit();
        ed.putString("PEREM_DOP_DATA_UP", "");
        ed.putString("PEREM_DOP_NDS", "");
        ed.putString("PEREM_DOP_SKIDKA", "");
        ed.putString("PEREM_DOP_CREDIT", "");
        ed.putString("PEREM_DOP_SUMMA", "");
        ed.putString("PEREM_DOP_ITOGO", "");
        ed.commit();


        try {
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza_L1.class);
                    startActivity(intent);
                    finish();
                }
            });
        }  // Обработка кнопки добавить заказ
        catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка кнопки добавить заказ!", Toast.LENGTH_SHORT).show();
        }

        // Обработка кнопки фильтрация данных
        try {
            btn_filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_kol = "0";
                    dialog_summa = "0.0";
                    dialog_Itogo = "0.0";
                    Data_Filtr_Start_Work = "";
                    Data_Filtr_End_Work = "";
                    Dialog_Data_UID_Klients = "";
                    checkBox_Data_Clic = false;
                    checkBox_Klients_Clic = false;

                    final ConstraintLayout constraintLayout;
                    localView = LayoutInflater.from(context_Activity).inflate(R.layout.mt_dialog_filter_work_table, null);
                    spinner = (Spinner) localView.findViewById(R.id.spinner_filter_klients);
                    btn_data_start = (Button) localView.findViewById(R.id.btn_filter_data_start);
                    btn_data_end = (Button) localView.findViewById(R.id.btn_filter_data_end);
                    checkBox_client = (CheckBox) localView.findViewById(R.id.checkBox_client);
                    checkBox_Data = (CheckBox) localView.findViewById(R.id.checkBox_Data);
                    constraintLayout = (ConstraintLayout) localView.findViewById(R.id.constr_layout);
                    tvw_error = ((TextView) localView.findViewById(R.id.tvw_error));

                    spinner.setVisibility(View.GONE);
                    constraintLayout.setVisibility(View.GONE);
                    tvw_error.setVisibility(View.GONE);
                    UID_Klient = "";
                    checkBox_client.setChecked(false);
                    checkBox_Data.setChecked(false);

                    checkBox_client.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b == true) {
                                spinner.setVisibility(View.VISIBLE);
                                checkBox_Klients_Clic = true;
                                spinner_filters.clear();
                                Loading_Data_Spinner();
                                adapterPriceClients = new ListAdapterAde_Spinner_Filter(context_Activity, spinner_filters);
                                adapterPriceClients.notifyDataSetChanged();
                                spinner.setAdapter(adapterPriceClients);
                            } else {
                                spinner.setVisibility(View.GONE);
                                checkBox_Klients_Clic = false;
                                spinner_filters.clear();
                                spinner_filters.add(new ListAdapterSimple_Spinner_Filter("", "", ""));
                                adapterPriceClients = new ListAdapterAde_Spinner_Filter(context_Activity, spinner_filters);
                                adapterPriceClients.notifyDataSetChanged();
                                spinner.setAdapter(adapterPriceClients);

                            }

                        }
                    });

                    checkBox_Data.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b == true) {
                                constraintLayout.setVisibility(View.VISIBLE);
                                checkBox_Data_Clic = true;
                                Data_Filtr_End_Work = this_data_filter;
                                btn_data_end.setText(this_data_now);
                                Data_Filtr_Start_Work = this_data_filter;
                                Data_Filtr_End_Work = this_data_filter;
                                Data_Filtr_End = this_data_now;
                            } else {
                                constraintLayout.setVisibility(View.GONE);
                                checkBox_Data_Clic = false;
                                Data_Filtr_Start = "начальная дата...";
                                btn_data_start.setText(Data_Filtr_Start);
                                Data_Filtr_End = "конечная дата...";
                                btn_data_end.setText(Data_Filtr_End);
                            }
                        }
                    });

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String k_agent = ((TextView) view.findViewById(R.id.tvw_filter_name)).getText().toString();
                            UID_Klient = ((TextView) view.findViewById(R.id.tvw_filter_uid)).getText().toString();
                            Toast.makeText(context_Activity, k_agent + "\n" + UID_Klient, Toast.LENGTH_SHORT).show();
                            ed = sp.edit();
                            ed.putString("PEREM_DIALOG_UID", UID_Klient);  // передача кода выбранного uid клиента
                            ed.putString("PEREM_DIALOG_INT", String.valueOf(i));  // передача кода начальной даты
                            ed.putString("PEREM_DISPLAY_START", this_data_now);  // передача кода начальной даты
                            ed.putString("PEREM_DISPLAY_END", this_data_now);  // передача кода конечной даты
                            ed.commit();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                    btn_data_start.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatePickerDialog datePickerDialog = new DatePickerDialog(context_Activity,
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.set(year, monthOfYear, dayOfMonth);
                                            dateFormat_work = new SimpleDateFormat("yyyy-MM-dd");
                                            dateFormat_display = new SimpleDateFormat("dd-MM-yyyy");
                                            Data_Filtr_Start_Work = dateFormat_work.format(calendar.getTime());
                                            Data_Filtr_Start = dateFormat_display.format(calendar.getTime());
                                            btn_data_start.setText(Data_Filtr_Start);
                                            if (checkBox_Data.isChecked()) {
                                                if (Data_Filtr_Start_Work.compareTo(Data_Filtr_End_Work) <= 0) {
                                                    tvw_error.setVisibility(View.GONE);
                                                    boolean_tvw_error = false;
                                                    Log.e("Return", "Верный формат дат");
                                                } else {
                                                    tvw_error.setVisibility(View.VISIBLE);
                                                    boolean_tvw_error = true;
                                                    Log.e("Return", "Не верный формат дат");
                                                }
                                            }

                                        }
                                    }, mYear, mMonth, mDay);

                         /*   datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Отмена1",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (which == DialogInterface.BUTTON_NEGATIVE) {
                                                dialog.cancel();
                                                Log.e("Return", "Нажато нет");
                                            }
                                        }
                                    });


                            datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                                Log.e("Return", "Нажато Ок");
                                            }
                                        }
                                    });*/
                            datePickerDialog.show();
                        }
                    });

                    btn_data_end.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatePickerDialog datePickerDialog = new DatePickerDialog(context_Activity,
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.set(year, monthOfYear, dayOfMonth);
                                            dateFormat_work = new SimpleDateFormat("yyyy-MM-dd");
                                            dateFormat_display = new SimpleDateFormat("dd-MM-yyyy");
                                            Data_Filtr_End_Work = dateFormat_work.format(calendar.getTime());
                                            Data_Filtr_End = dateFormat_display.format(calendar.getTime());
                                            btn_data_end.setText(Data_Filtr_End);
                                            if (checkBox_Data.isChecked()) {
                                                if (Data_Filtr_Start_Work.compareTo(Data_Filtr_End_Work) <= 0) {
                                                    tvw_error.setVisibility(View.GONE);
                                                    boolean_tvw_error = false;
                                                    Log.e("Return", "Верный формат дат");
                                                } else {
                                                    tvw_error.setVisibility(View.VISIBLE);
                                                    boolean_tvw_error = true;
                                                    Log.e("Return", "Не верный формат дат");
                                                }
                                            }

                                        }
                                    }, mYear, mMonth, mDay);
                            datePickerDialog.show();
                        }
                    });


                    android.app.AlertDialog.Builder localBuilder = new android.app.AlertDialog.Builder(context_Activity);
                    localBuilder.setView(localView);
                    localBuilder.setTitle("Фильтр данных");
                    localBuilder.setCancelable(false).setIcon(R.drawable.office_filter).setPositiveButton(" ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            try {
                                Dialog_Data_UID_Klients = "All";
                                Dialog_Data_Start = this_data_filter;
                                Dialog_Data_End = this_data_filter;

                                if (boolean_tvw_error.booleanValue()) {
                                    Dialog_Data_Start = this_data_filter;
                                    Dialog_Data_End = this_data_filter;
                                } else {
                                    Dialog_Data_Start = Data_Filtr_Start_Work;
                                    Dialog_Data_End = Data_Filtr_End_Work;
                                }

                                if (!checkBox_Data.isChecked()) {
                                    Dialog_Data_Start = this_data_filter;
                                    Dialog_Data_End = this_data_filter;
                                }

                                if (checkBox_client.isChecked()) {
                                    Dialog_Data_UID_Klients = UID_Klient;
                                } else
                                    Dialog_Data_UID_Klients = "All";

                                ed = sp.edit();
                                ed.putString("PEREM_DIALOG_UID", Dialog_Data_UID_Klients);  // передача кода выбранного uid клиента
                                ed.putString("PEREM_DIALOG_DATA_START", Dialog_Data_Start);  // передача кода начальной даты
                                ed.putString("PEREM_DIALOG_DATA_END", Dialog_Data_End);  // передача кода конечной даты
                                ed.putString("PEREM_DISPLAY_START", Data_Filtr_Start);  // передача кода начальной даты
                                ed.putString("PEREM_DISPLAY_END", Data_Filtr_End);  // передача кода конечной даты
                                ed.commit();

                                zakaz.clear();
                                Loading_List_Filter(Dialog_Data_Start, Dialog_Data_End, Dialog_Data_UID_Klients);
                                // getSupportActionBar().setSubtitle("Данные с " + Data_Filtr_Start + " по \n" + Data_Filtr_End);
                                adapterPriceClients_RN = new ListAdapterAde_List_RN_Table(WJ_Forma_Zakaza.this, zakaz);
                                adapterPriceClients_RN.notifyDataSetChanged();
                                listView.setAdapter(adapterPriceClients_RN);
                            } catch (Exception e) {
                                Toast.makeText(context_Activity, "Ошибка кнопки фильтр!", Toast.LENGTH_SHORT).show();
                                Log.e("File", "Ошибка кнопки фильтр!");
                            }

                            paramDialogInterface.cancel();


                        }
                    }).setNegativeButton(" ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                            paramDialogInterface.cancel();

                        }
                    });


                    android.app.AlertDialog localAlertDialog = localBuilder.create();
                    localAlertDialog.show();

                    button_ok = localAlertDialog.getButton(-1);
                    button_ok.setCompoundDrawablesWithIntrinsicBounds(R.drawable.button_ok, 0, 0, 0);
                    button_cancel = localAlertDialog.getButton(-2);
                    button_cancel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_close, 0);


                }
            });
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка кнопки фильтр!", Toast.LENGTH_SHORT).show();
        }

        try {
            btn_summa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_S = new androidx.appcompat.app.AlertDialog.Builder(context_Activity);
                    Loading_RN_AutoSumma(Summa_Data_Start, Summa_Data_End, Summa_UID);
                    dialog_S.setTitle("Данные за период\n" + Summa_Data_Display_Start + " по " + Summa_Data_Display_End)
                            .setMessage("Кол-во точек: " + dialog_kol + "\nОбщая сумма: " + dialog_summa + "\nИтого к оплате: " + dialog_Itogo)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    paramDialogInterface.cancel();
                                }
                            });
                    dialog_S.show();
                }
            });
        } catch (Exception e) {  // Обработка кнопки автосуммы за период
            Toast.makeText(context_Activity, "Ошибка кнопки автосумма!", Toast.LENGTH_SHORT).show();
        }

        try {
            btn_xml.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_cancel = new androidx.appcompat.app.AlertDialog.Builder(context_Activity);
                    dialog_cancel.setTitle("Создать отчет за день?");
                    dialog_cancel.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                            pDialog = new ProgressDialog(context_Activity);
                            pDialog.setMessage("Загрузка продуктов...");
                            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            pDialog.setProgress(0);
                            pDialog.setMax(100);
                            pDialog.show();

                            Constanta_Read();
                            Log.e("File", "TT Button_Up");

                            WJ_Forma_Zakaza.MyAsyncTask_Sync_New_XML asyncTask = new WJ_Forma_Zakaza.MyAsyncTask_Sync_New_XML();
                            asyncTask.execute();
                        }

                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        }
                    });
                    dialog_cancel.show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка выгрузки XML!", Toast.LENGTH_SHORT).show();
        }

        try {
            btn_all_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_cancel = new androidx.appcompat.app.AlertDialog.Builder(context_Activity);
                    dialog_cancel.setTitle("Вы хотите очистить все данные?");
                    dialog_cancel.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Dialog_Data_Start = this_data_filter;
                            Dialog_Data_End = this_data_filter;
                            Dialog_Data_UID_Klients = "";
                            zakaz.clear();
                            Loading_List_Filter(this_data_filter, this_data_filter, "All");
                            adapterPriceClients_RN = new ListAdapterAde_List_RN_Table(WJ_Forma_Zakaza.this, zakaz);
                            adapterPriceClients_RN.notifyDataSetChanged();
                            listView.setAdapter(adapterPriceClients_RN);
                            ed = sp.edit();
                            ed.putString("PEREM_KLIENT_UID", "");  // передача кода выбранного uid клиента
                            ed.putString("PEREM_DIALOG_UID", "");  // передача кода выбранного uid клиента
                            ed.putString("PEREM_DIALOG_DATA_START", "");  // передача кода начальной даты
                            ed.putString("PEREM_DIALOG_DATA_END", "");  // передача кода конечной даты
                            ed.putString("PEREM_DISPLAY_START", "");  // передача кода начальной даты
                            ed.putString("PEREM_DISPLAY_END", "");  // передача кода конечной даты
                            ed.commit();
                        }

                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        }
                    });
                    dialog_cancel.show();

                }
            });
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка удаление данных!", Toast.LENGTH_SHORT).show();
        }


    }

    protected void Loading_RN_AutoSumma(final String data_start, final String data_end, final String uid_klient) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
        Log.e("PARAM ", data_start + " " + data_end + " " + uid_klient);

        if (data_start.equals(data_end) & uid_klient.equals("All")) {
            query_SUMMA = "SELECT base_RN_All.agent_name, SUM(base_RN_All.summa) AS 'summa', SUM(base_RN_All.itogo) AS 'itogo'\n" +
                    "FROM base_RN_All\n" +
                    "WHERE base_RN_All.data = '" + data_end + "'";
            query_KolTT = "SELECT DISTINCT base_RN_All.k_agn_name\n" +
                    "FROM base_RN_All\n" +
                    "WHERE base_RN_All.data = '" + data_end + "'";
        } else if (!data_start.equals(data_end) & uid_klient.equals("All")) {
            Log.e(WJ_Forma_Zakaza.this.getLocalClassName(), "TYPE-2");
            query_SUMMA = "SELECT base_RN_All.agent_name, SUM(base_RN_All.summa) AS 'summa', SUM(base_RN_All.itogo) AS 'itogo'\n" +
                    "FROM base_RN_All \n" +
                    "WHERE base_RN_All.data BETWEEN '" + data_start + "' AND '" + data_end + "'";
            query_KolTT = "SELECT DISTINCT base_RN_All.k_agn_name \n" +
                    "FROM base_RN_All \n" +
                    "WHERE base_RN_All.data BETWEEN '" + data_start + "' AND '" + data_end + "'";
        } else if (data_start.equals(data_end) & !uid_klient.equals("All")) {

            Log.e(WJ_Forma_Zakaza.this.getLocalClassName(), "TYPE-3");
            query_SUMMA = "SELECT base_RN_All.agent_name, SUM(base_RN_All.summa) AS 'summa', SUM(base_RN_All.itogo) AS 'itogo' \n" +
                    "FROM base_RN_All \n" +
                    "WHERE base_RN_All.k_agn_uid = '" + uid_klient + "' AND base_RN_All.data = '" + data_end + "';";

            query_KolTT = "SELECT base_RN_All.agent_name, SUM(base_RN_All.summa) AS 'summa', SUM(base_RN_All.itogo) AS 'itogo' " +
                    "FROM base_RN_All " +
                    "WHERE base_RN_All.k_agn_uid  = '" + uid_klient + "' AND base_RN_All.data = '" + data_end + "';";
        } else if (!data_start.equals(data_end) & !uid_klient.equals("All")) {
            Log.e(WJ_Forma_Zakaza.this.getLocalClassName(), "TYPE-4");
            query_SUMMA = "SELECT base_RN_All.agent_name, SUM(base_RN_All.summa) AS 'summa', SUM(base_RN_All.itogo) AS 'itogo' \n" +
                    "FROM base_RN_All \n" +
                    "WHERE base_RN_All.k_agn_uid = '" + uid_klient + "' AND base_RN_All.data BETWEEN '" + data_start + "' AND '" + data_end + "';";

            query_KolTT = "SELECT base_RN_All.agent_name, SUM(base_RN_All.summa) AS 'summa', SUM(base_RN_All.itogo) AS 'itogo' " +
                    "FROM base_RN_All " +
                    "WHERE base_RN_All.k_agn_uid = '" + uid_klient + "' AND base_RN_All.data BETWEEN '" + data_start + "' AND '" + data_end + "';";
        }

        final Cursor cursor = db.rawQuery(query_SUMMA, null);
        cursor.moveToFirst();
        String Summa = cursor.getString(cursor.getColumnIndexOrThrow("summa"));
        String Itogo = cursor.getString(cursor.getColumnIndexOrThrow("itogo"));
        if (Summa != null) {
            String perem_forma_summa, perem_forma_itogo;
            perem_forma_summa = new DecimalFormat("#00.00").format(Double.parseDouble(Summa));
            perem_forma_itogo = new DecimalFormat("#00.00").format(Double.parseDouble(Itogo));
            dialog_summa = perem_forma_summa;
            dialog_Itogo = perem_forma_itogo;
        }
        cursor.close();
        final Cursor cursor_TT = db.rawQuery(query_KolTT, null);
        cursor_TT.moveToFirst();
        if (cursor_TT.getCount() > 0) {
            dialog_kol = String.valueOf(cursor_TT.getCount());
        }
        cursor_TT.close();
        db.close();
    }


    protected void Loading_Data_Spinner() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
        String query = "SELECT const_contragents.k_agent, const_contragents.adress, const_contragents.uid_k_agent\n" +
                "FROM const_contragents\n" +
                "WHERE const_contragents.uid_agent = '" + PEREM_AG_UID + "'";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("k_agent"));
            String adress = cursor.getString(cursor.getColumnIndexOrThrow("adress"));
            String uid_k_agent = cursor.getString(cursor.getColumnIndexOrThrow("uid_k_agent"));
            spinner_filters.add(new ListAdapterSimple_Spinner_Filter(name, adress, uid_k_agent));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    protected void Loading_List_Filter(final String data_start, final String data_end, String uid) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
        Log.e("List_Filter ", data_start + " " + data_end + " " + uid);
        String query;
        if (uid.equals("All")) {
            query = "SELECT * FROM base_RN_All \n" +
                    "WHERE base_RN_All.data BETWEEN '" + data_start + "' AND '" + data_end + "';";
        } else {
            query = "SELECT * FROM base_RN_All \n" +
                    "WHERE base_RN_All.k_agn_uid = '" + uid + "' AND base_RN_All.data BETWEEN '" + data_start + "' AND '" + data_end + "';";
        }


        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String Kod_RN = cursor.getString(cursor.getColumnIndexOrThrow("kod_rn"));
            String Klients = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_name"));
            String UID_Klients = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_uid"));
            String Adress = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_adress"));
            String Vrema = cursor.getString(cursor.getColumnIndexOrThrow("vrema"));
            String Data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
            String Summa = cursor.getString(cursor.getColumnIndexOrThrow("summa"));
            String Itogo = cursor.getString(cursor.getColumnIndexOrThrow("itogo"));
            String skidka = cursor.getString(cursor.getColumnIndexOrThrow("skidka")); // добавить обработку скидки
            String data_up = cursor.getString(cursor.getColumnIndexOrThrow("data_up")); // добавить обработку дата отгрузки
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status")); // добавить обработку дата отгрузки
            String debet = cursor.getString(cursor.getColumnIndexOrThrow("debet_new")); // добавить обработку дата отгрузки
            String sklad = cursor.getString(cursor.getColumnIndexOrThrow("sklad")); // добавить обработку дата отгрузки
            zakaz.add(new ListAdapterSimple_List_RN_Table(Kod_RN, Klients, UID_Klients, Vrema, Data, Summa, Itogo, Adress, skidka, debet, status, sklad));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }


    protected void Constanta_Write(String uid, String name, String kod_mobile,
                                   String region, String uid_region, String cena,
                                   String sklad, String type_real, String type_user) {
        ed = sp.edit();
      /*  ed.putString("PEREM_AG_UID", uid);  // передача кода агента (A8BA1F48-C7E1-497B-B74A-D86426684712)
        ed.putString("PEREM_AG_NAME", name);  // передача кода агента (A8BA1F48-C7E1-497B-B74A-D86426684712)
        ed.putString("PEREM_AG_REGION", region);  // маршруты для привязки к контагентам
        ed.putString("PEREM_AG_UID_REGION", uid_region);  // uid маршруты для привязки к контагентам
        ed.putString("PEREM_AG_CENA", cena);  // цены для агентов
        ed.putString("PEREM_AG_SKLAD", sklad);  // склады для агентов
        //ed.putString("PEREM_AG_UID_SKLAD", uid_sklad);  // склады для агентов ДОБАВИТЬ НОВЫЙ ПАРАМЕТР
        ed.putString("PEREM_AG_TYPE_REAL", type_real);  // выбор типа торгового агента OSDO или PRES
        ed.putString("PEREM_AG_TYPE_USER", type_user);  // тип учетной записи агент или экспедитор
        ed.putString("PEREM_WORK_DISTR", PEREM_DISTR);  // имя папки с данными (01_WDay)
        ed.putString("PEREM_KOD_MOBILE", kod_mobile);  // имя папки с данными (01_WDay)
        ed.putString("PEREM_ADMIN_ID", "8fa0949538802090");  // kod_mobile администратор
        ed.commit();
        Log.e("PEREM_AG_UID", uid);
        Log.e("PEREM_AG_NAME", name);
        Log.e("PEREM_AG_REGION", region);
        Log.e("PEREM_AG_UID_REGION", uid_region);
        Log.e("PEREM_AG_CENA", cena);
        Log.e("PEREM_AG_SKLAD", sklad);*/

    }

    protected void XML_Array_KodRN(String w_agent_uid, String Data_Start, String Data_End) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
        /*String query = "SELECT base_RN_All.kod_rn, base_RN_All.k_agn_name, base_RN_All.k_agn_uid, base_RN_All.data\n" +
                "FROM base_RN_All\n" +
                "WHERE base_RN_All.data BETWEEN '" + Data_Start + "' AND '" + Data_End + "';";*/

        String query = "SELECT base_RN_All.kod_rn, base_RN_All.k_agn_name, " +
                "base_RN_All.k_agn_uid, base_RN_All.data, base_RN_All.agent_name, base_RN_All.agent_uid\n" +
                "FROM base_RN_All\n" +
                "WHERE base_RN_All.data BETWEEN '" + Data_Start + "' AND '" + Data_End + "' " +
                "AND base_RN_All.agent_uid = '" + w_agent_uid + "';";
        final Cursor cursor = db.rawQuery(query, null);
        mass_kod_rn = new String[cursor.getCount()];
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String Kod_RN = cursor.getString(cursor.getColumnIndexOrThrow("kod_rn"));
            mass_kod_rn[cursor.getPosition()] = Kod_RN;
            Log.e("Mass ", Kod_RN);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    protected void XML_Array_Name(String Array_kod_rn, String Data_Start, String Data_End) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT base_RN_All.kod_rn, base_RN_All.k_agn_name, base_RN_All.k_agn_uid, base_RN_All.data\n" +
                "FROM base_RN_All\n" +
                "WHERE base_RN_All.kod_rn = '" + Array_kod_rn + "' AND base_RN_All.data BETWEEN '" + Data_Start + "' AND '" + Data_End + "';";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String Kod_RN = cursor.getString(cursor.getColumnIndexOrThrow("kod_rn"));
            String Klients = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_name"));
            String UID_Klients = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_uid"));
            String Data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
            XML_Array(Kod_RN, Klients, UID_Klients, Data);
            // Log.e(this.getLocalClassName(), Kod_RN + "_" + Klients + "_" + UID_Klients + "_" + Data);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    protected void XML_Array(String kod_Rn, String S_name, String uid, String data) {
        try {
            // Log.e(this.getLocalClassName(), kod_Rn + " " + S_name);
            serializer.startTag(null, "Array_Order");
            serializer.startTag(null, "SyncTableNomenclatura");

            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);

            String query_TITLE = "SELECT * FROM base_RN_All WHERE base_RN_All.kod_rn = '" + kod_Rn + "';";
            cursor = db.rawQuery(query_TITLE, null);
            cursor.moveToFirst();
            String sql_kod_rn = cursor.getString(cursor.getColumnIndexOrThrow("kod_rn"));
            String sql_agent_name = cursor.getString(cursor.getColumnIndexOrThrow("agent_name"));
            String sql_agent_uid = cursor.getString(cursor.getColumnIndexOrThrow("agent_uid"));
            String sql_k_agn_uid = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_uid"));
            String sql_k_agn_name = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_name"));
            String sql_data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
            String sql_data_xml = cursor.getString(cursor.getColumnIndexOrThrow("data_xml"));
            String sql_credit = cursor.getString(cursor.getColumnIndexOrThrow("credit"));
            String sql_sklad = cursor.getString(cursor.getColumnIndexOrThrow("sklad"));
            String sql_sklad_uid = cursor.getString(cursor.getColumnIndexOrThrow("sklad_uid"));
            String sql_cena_price = cursor.getString(cursor.getColumnIndexOrThrow("cena_price"));
            String sql_coment = cursor.getString(cursor.getColumnIndexOrThrow("coment"));
            String sql_uslov_nds = cursor.getString(cursor.getColumnIndexOrThrow("uslov_nds"));
            String sql_skidka_title = cursor.getString(cursor.getColumnIndexOrThrow("skidka_title"));
            String sql_data_credite = cursor.getString(cursor.getColumnIndexOrThrow("credite_date"));

            String sql_rn_data = cursor.getString(cursor.getColumnIndexOrThrow("data_xml"));
            String sql_rn_vrema = cursor.getString(cursor.getColumnIndexOrThrow("vrema"));
            Log.e("String_XML ", sql_data + "_" + sql_data_xml);

            // Заполнение Формы
            serializer.startTag(null, "Nomer_Order");
            serializer.text(sql_kod_rn);
            serializer.endTag(null, "Nomer_Order");

            serializer.startTag(null, "Order_Date");
            serializer.text(sql_rn_data);
            serializer.endTag(null, "Order_Date");

            serializer.startTag(null, "Order_Vrema");
            serializer.text(sql_rn_vrema);
            serializer.endTag(null, "Order_Vrema");

            serializer.startTag(null, "NAME_AG");
            serializer.text(sql_agent_name);
            serializer.endTag(null, "NAME_AG");
            serializer.startTag(null, "UID_AG");
            serializer.text(sql_agent_uid);
            serializer.endTag(null, "UID_AG");
            serializer.startTag(null, "UID_C");
            serializer.text(sql_k_agn_uid);
            serializer.endTag(null, "UID_C");
            serializer.startTag(null, "NAME_C");
            serializer.text(sql_k_agn_name);
            serializer.endTag(null, "NAME_C");
            serializer.startTag(null, "DATA");
            serializer.text(sql_data);
            serializer.endTag(null, "DATA");
            serializer.startTag(null, "CREDIT");
            serializer.text(sql_credit);  // Консигнация, Перечислением, Взаимозачет
            serializer.endTag(null, "CREDIT");
            serializer.startTag(null, "SKLAD");
            serializer.text(sql_sklad);
            serializer.endTag(null, "SKLAD");
            serializer.startTag(null, "UID_SKLAD");
            serializer.text(sql_sklad_uid);
            serializer.endTag(null, "UID_SKLAD");
            serializer.startTag(null, "CENA_PRICE");
            serializer.text(sql_cena_price); // Цена брака, Цена в розницу, Цена для народных
            serializer.endTag(null, "CENA_PRICE");
            serializer.startTag(null, "Coment");
            serializer.text(sql_coment); // Дата отгрузки 04.12.2021; скидка 10%
            serializer.endTag(null, "Coment");
            serializer.startTag(null, "CENA_NDS");
            serializer.text(sql_uslov_nds); // Дата отгрузки 04.12.2021; скидка 10%
            serializer.endTag(null, "CENA_NDS");
            serializer.startTag(null, "SKIDKA");
            serializer.text(sql_skidka_title); // Скидка
            serializer.endTag(null, "SKIDKA");
            serializer.startTag(null, "DNEI_KONSIGN");
            serializer.text(sql_data_credite); // Дней конс
            serializer.endTag(null, "DNEI_KONSIGN");
            cursor.close();

            String query = "SELECT base_RN_All.kod_rn, base_RN_All.k_agn_name, " +
                    "base_RN_All.k_agn_uid, base_RN_All.data, base_RN.Name,  " +
                    "base_RN.koduid, base_RN.Kod_Univ, base_RN.Kol, base_RN.Cena, base_RN.Summa, " +
                    "base_RN.skidka, base_RN.itogo\n" +
                    "FROM base_RN_All\n" +
                    "JOIN base_RN ON base_RN_All.kod_rn=base_RN.Kod_RN\n" +
                    "WHERE base_RN_All.kod_rn = '" + kod_Rn + "'";
            cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                String kod_univ = cursor.getString(cursor.getColumnIndexOrThrow("Kod_Univ"));
                String kol = cursor.getString(cursor.getColumnIndexOrThrow("Kol"));
                String cena = cursor.getString(cursor.getColumnIndexOrThrow("Cena"));
                String summa = cursor.getString(cursor.getColumnIndexOrThrow("Summa"));
                String kod_uid = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));

                // Первая строка заказа
                serializer.startTag(null, "POS");
                serializer.startTag(null, "NAME");
                serializer.text(name);
                Log.e(this.getLocalClassName(), name);
                serializer.endTag(null, "NAME");
                serializer.startTag(null, "NAME_UID");
                serializer.text(kod_uid);
                serializer.endTag(null, "NAME_UID");
                serializer.startTag(null, "KODUNIV");
                serializer.text(kod_univ);
                serializer.endTag(null, "KODUNIV");
                serializer.startTag(null, "KOL_COUNT");
                serializer.text(kol);
                serializer.endTag(null, "KOL_COUNT");
                serializer.startTag(null, "PRICE");
                serializer.text(cena);
                serializer.endTag(null, "PRICE");
                serializer.startTag(null, "SUMMA");
                serializer.text(summa);
                serializer.endTag(null, "SUMMA");
                serializer.endTag(null, "POS");
                // Конец строки
                cursor.moveToNext();
            }

            cursor.close();
            db.close();
            serializer.endTag(null, "SyncTableNomenclatura");
            serializer.endTag(null, "Array_Order");


        } catch (Exception e) {
            Log.e("WJ_END:", "Ошибка: создание отчета!");
            Toast.makeText(context_Activity, "Ошибка: создание отчета!", Toast.LENGTH_SHORT).show();
        }

    }

    // Константы для чтения
    protected void Constanta_Read() {
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        PEREM_FTP_SERV = sp.getString("PEREM_FTP_SERV", "0");                    //чтение данных: имя сервера
        PEREM_FTP_LOGIN = sp.getString("PEREM_FTP_LOGIN", "0");                  //чтение данных: имя логин
        PEREM_FTP_PASS = sp.getString("PEREM_FTP_PASS", "0");                    //чтение данных: имя пароль
        PEREM_FTP_DISTR_XML = sp.getString("PEREM_FTP_DISTR_XML", "0");          //чтение данных: путь к файлам XML
        PEREM_FTP_DISTR_db3 = sp.getString("PEREM_FTP_DISTR_db3", "0");          //чтение данных: путь к файлам DB3
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
        PEREM_KOD_UID_KODRN = sp.getString("PEREM_KOD_UID_KODRN", "0");           //чтение данных: уникальный код для накладной


        PEREM_KLIENT_UID = sp.getString("PEREM_KLIENT_UID", "0");                 //чтение данных: передача кода выбранного uid клиента
        PEREM_DIALOG_UID = sp.getString("PEREM_DIALOG_UID", "0");                 //чтение данных: передача кода выбранного uid клиента
        PEREM_DIALOG_DATA_START = sp.getString("PEREM_DIALOG_DATA_START", "0");   //чтение данных: передача кода начальной даты
        PEREM_DIALOG_DATA_END = sp.getString("PEREM_DIALOG_DATA_END", "0");       //чтение данных: передача кода конечной даты
        PEREM_DISPLAY_START = sp.getString("PEREM_DISPLAY_START", "0");           //чтение данных: передача кода для димплея начальной даты
        PEREM_DISPLAY_END = sp.getString("PEREM_DISPLAY_END", "0");                //чтение данных: передача кода для димплея конечной даты


        PEREM_DIALOG_UID = sp.getString("PEREM_DIALOG_UID", "0");
        PEREM_DIALOG_DATA_START = sp.getString("PEREM_DIALOG_DATA_START", "0");
        PEREM_DIALOG_DATA_END = sp.getString("PEREM_DIALOG_DATA_END", "0");
        PEREM_FTP_PUT = sp.getString("list_preference_ftp", "0");                 //чтение данных:

        if (sp.getString("PEREM_DIALOG_DATA_START", "0").isEmpty()) {
            Summa_Data_Start = this_data_filter;
        } else Summa_Data_Start = sp.getString("PEREM_DIALOG_DATA_START", "0");

        if (sp.getString("PEREM_DIALOG_DATA_END", "0").isEmpty()) {
            Summa_Data_End = this_data_filter;
        } else Summa_Data_End = sp.getString("PEREM_DIALOG_DATA_END", "0");

        if (sp.getString("PEREM_DISPLAY_START", "0").isEmpty()) {
            Summa_Data_Display_Start = this_data_now;
        } else Summa_Data_Display_Start = sp.getString("PEREM_DISPLAY_START", "0");

        if (sp.getString("PEREM_DISPLAY_END", "0").isEmpty()) {
            Summa_Data_Display_End = this_data_now;
        } else Summa_Data_Display_End = sp.getString("PEREM_DISPLAY_END", "0");

        if (sp.getString("PEREM_DIALOG_UID", "0").isEmpty()) {
            Summa_UID = "All";
        } else Summa_UID = sp.getString("PEREM_DIALOG_UID", "0");
    }

    // Удаление данных: номер накладной из базы
    protected void Delete_RN_BASE(String name_rn) {
        try {
            SQLiteDatabase db = context_Activity.openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
            String query_delete_RN = "DELETE FROM base_RN WHERE kod_rn = '" + name_rn + "'";
            String query_delete_RN_ALL = "DELETE FROM base_RN_All WHERE kod_rn = '" + name_rn + "'";

            final Cursor cursor_rn = db.rawQuery(query_delete_RN, null);
            cursor_rn.moveToFirst();
            while (cursor_rn.isAfterLast() == false) {
                cursor_rn.moveToNext();
            }
            cursor_rn.close();

            final Cursor cursor_rn_all = db.rawQuery(query_delete_RN_ALL, null);
            cursor_rn_all.moveToFirst();
            while (cursor_rn_all.isAfterLast() == false) {
                cursor_rn_all.moveToNext();
            }
            cursor_rn_all.close();
            db.close();

        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка удаления данных!", Toast.LENGTH_SHORT).show();
            Log.e("Удаление!", "Ошибка удаления данных!");
        }
    }

    // Обновление статуса выгрузки данных
    protected void Update_Status(String data_ddb) {
        try {
            SQLiteDatabase db = context_Activity.openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
            String query = "UPDATE base_RN_All SET status = 'true' WHERE data = '" + data_ddb + "'";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                cursor.moveToNext();
            }
            cursor.close();
            db.close();

        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка удаления данных!", Toast.LENGTH_SHORT).show();
            Log.e("Удаление!", "Ошибка удаления данных!");
        }
    }


    // Синхронизация файлов для всех складов Юишкек
    private class MyAsyncTask_Sync_New_XML extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            pDialog.setMessage("Создание отчет по заказам. Подождите...");
            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                publishProgress(1);
                getFloor();  // Синхронизация
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
            Log.e("Дата=", this_rn_data);
            pDialog.setProgress(0);
            pDialog.dismiss();
            Toast.makeText(context_Activity, "Отчет успешно создан!", Toast.LENGTH_SHORT).show();
            Update_Status(this_rn_data);
            onResume();

            ListAdapet_Internet_Load();
            Mail();
            WJ_Forma_Zakaza.sender_mail_async async_sending = new WJ_Forma_Zakaza.sender_mail_async();
            async_sending.execute();
        }

        private void getFloor() throws InterruptedException {
            pDialog.setMessage("Загрузка продуктов. Подождите...");


            try {
                String str_Android_ID = Settings.Secure.getString(context_Activity.getContentResolver(), Settings.Secure.ANDROID_ID);
                Log.e("Android_ID^ ", PEREM_ANDROID_ID);
                Log.e("Android_ID^ ", Settings.Secure.getString(context_Activity.getContentResolver(), Settings.Secure.ANDROID_ID));
                Log.e("Android_ID^ ", str_Android_ID.substring(0, str_Android_ID.length() - 2));

               /* if (PEREM_ANDROID_ID.equals(str_Android_ID.substring(0, str_Android_ID.length() - 2))) {
                    Log.e("File", "TT " + sp.getString("PEREM_DIALOG_DATA_START", "0"));

                    if (!sp.getString("PEREM_DIALOG_DATA_START", "0").isEmpty()) {
                        Log.e("File", "не пустой");
                        XML_Data_Start = Summa_Data_Start;
                        XML_Data_End = Summa_Data_End;
                        Log.e("File1", PEREM_AG_UID + "__" + XML_Data_Start + " " + XML_Data_End);
                        XML_Array_KodRN(PEREM_AG_UID, XML_Data_Start, XML_Data_End);

                    } else {
                        XML_Data_Start = this_data_filter;
                        XML_Data_End = this_data_filter;
                        XML_Array_KodRN(PEREM_AG_UID, XML_Data_Start, XML_Data_End);
                        Log.e("File2", XML_Data_Start + " " + XML_Data_End);
                    }

                } else XML_Array_KodRN(PEREM_AG_UID, this_data_filter, this_data_filter);*/
                // XML_Array_KodRN(this_data_filter, this_data_filter);

               /* XML_Data_Start = this_data_filter;
                XML_Data_End = this_data_filter;
                XML_Array_KodRN(PEREM_AG_UID, XML_Data_Start, XML_Data_End);
                Log.e("for Array^ ", XML_Data_Start + " " + XML_Data_End);
                Log.e("for Array^ ", Dialog_Data_Start + " " + Dialog_Data_End);*/
                try {
                    //String name_file = PEREM_NAME_AGENT + "_" + this_data_filter;
                    //String name_file = "NEWuserData";
                    Boolean bool_filter = false;
                    String name_file_sd;
                    if (!sp.getString("PEREM_DIALOG_DATA_START", "0").isEmpty()) {
                        Log.e("File", "не пустой");
                        XML_Data_Start = Summa_Data_Start;
                        XML_Data_End = Summa_Data_End;
                        bool_filter = true;
                        Log.e("File1", PEREM_AG_UID + "__" + XML_Data_Start + " " + XML_Data_End);
                        XML_Array_KodRN(PEREM_AG_UID, XML_Data_Start, XML_Data_End);

                    } else {
                        XML_Data_Start = this_data_filter;
                        XML_Data_End = this_data_filter;
                        bool_filter = false;
                        XML_Array_KodRN(PEREM_AG_UID, XML_Data_Start, XML_Data_End);
                        Log.e("File2", XML_Data_Start + " " + XML_Data_End);
                    }

                    String[] f = {"а", "б", "в", "г", "д", "е", "ё", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п", "р", "с", "т", "у", "ф", "х", "ч", "ц", "ш", "щ", "э", "ю", "я", "ы", "ъ", "ь"};
                    String[] t = {"a", "b", "v", "g", "d", "e", "io", "zh", "z", "i", "i", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ch", "c", "sh", "csh", "e", "ju", "ja", "i", "", ""};

                    String res = "";
                    String name_old = PEREM_AG_NAME.toLowerCase().replaceAll(" ", "_");


                    for (int i = 0; i < name_old.length(); ++i) {
                        String add = name_old.substring(i, i + 1);
                        for (int j = 0; j < f.length; j++) {
                            if (f[j].equals(add)) {
                                add = t[j];
                                break;
                            }
                        }
                        res += add;
                    }
                    Log.e("new^ ", res);

                    String name_file_db = "MTW_out_order";
                    String file_put_db = WJ_Forma_Zakaza.this.getDatabasePath(name_file_db + ".xml").getAbsolutePath();  // путь к databases
                    Log.e(TAG, "FORMA_XML_OUT: " + file_put_db);
                    if (bool_filter.booleanValue() == false) {
                        name_file_sd = ("MT_out_order_" + res + "_" + XML_Data_Start + ".xml").replaceAll("[/:*?<>]", "");
                    } else {
                        name_file_sd = ("MT_out_order_" + res + "_" + XML_Data_Start + "_" + XML_Data_End + ".xml").replaceAll("[/:*?<>]", "");
                    }


                    // String name_file_sd = "MTW_out_order_" + PEREM_AG_UID.substring(0, 6) + XML_Data_Start + ".xml";
                    XML_NEW_NAME = name_file_sd;
                    String file_db_sd = Environment.getExternalStorageDirectory() + "/Price/" + PEREM_WORK_DISTR + "/" + name_file_sd;
                    Log.e("File_put ", file_put_db);
                    Log.e("File_put ", file_db_sd);

                    File newxmlfile_db = new File(file_put_db);
                    File newxmlfile_sd = new File(file_db_sd);

                    try {
                        newxmlfile_db.createNewFile();
                        newxmlfile_sd.createNewFile();
                    } catch (IOException e) {
                        Log.e("IOException", "Exception in create new File(");
                    }
                    FileOutputStream fileos = null;
                    FileOutputStream fileos2 = null;
                    try {
                        fileos = new FileOutputStream(newxmlfile_db);
                        fileos2 = new FileOutputStream(newxmlfile_sd);

                    } catch (FileNotFoundException e) {
                        Log.e("FileNotFoundException", e.toString());
                    }

                    serializer = Xml.newSerializer();
                    serializer.setOutput(fileos, "UTF-8");
                    serializer.startDocument(null, Boolean.valueOf(true));
                    serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

                    serializer.startTag(null, "XML_Array");

                    for (int i = 0; i < mass_kod_rn.length; i++) {
                        Log.e("FileNotFound ", mass_kod_rn[i] + " " + XML_Data_Start + " " + XML_Data_End);
                        XML_Array_Name(mass_kod_rn[i], XML_Data_Start, XML_Data_End);
                    }
                    serializer.endTag(null, "XML_Array");

                    serializer.endDocument();

                    serializer.flush();
                    serializer.setOutput(fileos2, "UTF-8");
                    serializer.startDocument(null, Boolean.valueOf(true));
                    serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

                    serializer.startTag(null, "XML_Array");

                    for (int i = 0; i < mass_kod_rn.length; i++) {
                        Log.e("FileNotFound ", mass_kod_rn[i] + " " + XML_Data_Start + " " + XML_Data_End);
                        XML_Array_Name(mass_kod_rn[i], XML_Data_Start, XML_Data_End);
                    }
                    serializer.endTag(null, "XML_Array");

                    serializer.endDocument();
                    serializer.flush();

                    fileos.close();
                    fileos2.close();

                } catch (Exception e) {

                }


            } catch (Exception e) {

                Log.e("Err..", "rrr");
            }

            TimeUnit.SECONDS.sleep(1);
            pDialog.dismiss();


          /*  SQLiteDatabase db = getBaseContext().openOrCreateDatabase("ostatki_db.db3", MODE_PRIVATE, null);
            SQLiteDatabase db_sunc = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
            db_sunc.delete("base4_ost", null, null);
            String query_up = "SELECT base4_ost.data, base4_ost.koduid, " +
                    "base4_ost.sklad, base4_ost.count" +
                    " FROM base4_ost\n" +
                    " WHERE base4_ost.sklad !='" + PEREM_AG_UID_SKLAD + "';";

            cursor = db.rawQuery(query_up, null);
            localContentValues = new ContentValues();

            BigDecimal f1 = new BigDecimal(0.0);
            BigDecimal pointOne = new BigDecimal(100 / Double.valueOf(cursor.getCount()));

            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String data = cursor.getString(cursor.getColumnIndex("data"));
                String sklad_uid = cursor.getString(cursor.getColumnIndex("sklad_uid"));
                String name_uid = cursor.getString(cursor.getColumnIndex("name_uid"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String count = cursor.getString(cursor.getColumnIndex("count"));

                localContentValues.put("data", data);
                localContentValues.put("sklad_uid", sklad_uid);
                localContentValues.put("name_uid", name_uid);
                localContentValues.put("name", name);
                localContentValues.put("count", count);
                db_sunc.insert("base4_ost", null, localContentValues);
                db.insert("base4_ost", null, localContentValues);

                f1 = f1.add(pointOne);
                pDialog.setProgress(f1.intValue());
                cursor.moveToNext();*/
        }


    }

    protected void ListAdapet_Internet_Load() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
        Log.e(this.getLocalClassName(), this_data_filter);

        String query_count = "SELECT DISTINCT k_agn_name FROM base_RN_All WHERE data = '" + this_data_filter + "'";
        final Cursor cursor_count = db.rawQuery(query_count, null);
        cursor_count.moveToFirst();
        if (cursor_count.getCount() > 0) {
            count_tt = cursor_count.getCount();
        } else count_tt = 0;
        cursor_count.close();

        String query_itogo = "SELECT SUM(summa) AS day_summa, SUM(itogo) AS day_itogo FROM base_RN_All\n" +
                "WHERE data = '" + this_data_filter + "'";
        final Cursor cursor_itogo = db.rawQuery(query_itogo, null);
        cursor_itogo.moveToFirst();
        if (cursor_itogo.getCount() > 0) {
            trade_itogo = cursor_itogo.getString(cursor_itogo.getColumnIndexOrThrow("day_itogo"));
            ;
        } else trade_itogo = "0.0";
        cursor_itogo.close();


        String query = "SELECT base_RN_All.data, base_RN_All.kod_rn, base_RN_All.k_agn_name, " +
                "base_RN_All.k_agn_uid, base_RN_All.k_agn_adress, base_RN_All.skidka, base_RN_All.summa, base_RN_All.itogo\n" +
                "FROM base_RN_All\n" +
                "WHERE base_RN_All.agent_uid = '" + PEREM_AG_UID + "' AND base_RN_All.data LIKE '" + this_data_filter + "'";

        new_write = ("");
        new_write = (new_write + "");
        new_write = (new_write + "Дата загрузки: " + this_data_now + " | " + this_data_filter);
        new_write = (new_write + "\n");
        new_write = (new_write + "Количество точек: " + count_tt + ", Общая сумма продаж: " + trade_itogo + "");
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
            WaitingDialog.cancel();
            Toast.makeText(context_Activity, "Заказ успешно отправлен!!!", Toast.LENGTH_LONG).show();
            Loading_FTP_Zakaz(XML_NEW_NAME, PEREM_FTP_PUT + PEREM_WORK_DISTR + "/" + XML_NEW_NAME);
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
                    String mail_where = cursor.getString(cursor.getColumnIndexOrThrow("mail_where"));
                    String mail = cursor.getString(cursor.getColumnIndexOrThrow("operator_mail"));
                    String mail_pass = cursor.getString(cursor.getColumnIndexOrThrow("operator_mail_pass"));
                    if (region.equals(PEREM_FTP_PUT)) {
                        data_mail_from = mail;
                        data_mail_where = mail_where;
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

    protected void Debet_ReWrite(String d_kod_rn, String d_itogo) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT * FROM otchet_debet WHERE d_kontr_uid = '" + d_kod_rn + "'";
        final Cursor cursor = db.rawQuery(query, null);
        ContentValues localContentValues = new ContentValues();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String client = cursor.getString(cursor.getColumnIndexOrThrow("d_kontr_uid"));
            String debet = cursor.getString(cursor.getColumnIndexOrThrow("d_summa"));
            Double debet_old, debet_new, debet_db;
            debet_db = Double.parseDouble(debet);
            debet_old = Double.parseDouble(d_itogo);

            debet_new = debet_db - debet_old;
            if (debet_new != 0.0) {
                String Format = new DecimalFormat("#00.00").format(debet_new).replace(",", ".");
                localContentValues.put("d_summa", Format);
                String[] arrayOfString = new String[1];
                arrayOfString[0] = client;
                db.update("otchet_debet", localContentValues, "d_kontr_uid = ?", new String[]{client});
            } else {
                db.delete("otchet_debet", "d_kontr_uid = ?", new String[]{client});
            }

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    protected void Loading_FTP_Zakaz(String file_name, String file_put) {
        Log.e("Файл: ", file_name);
        Log.e("Путь: ", file_put);


        //      E  MT_out_order_sunbell_(marshrut-401)_2022-10-21.xml
        //       E  /MT_Sunbell_Karakol/05_WDay/MT_out_order_sunbell_(marshrut-401)_2022-10-21.xml
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
        new Sunbell_FtpAsyncTask_Zakaz_UP_FTP(context_Activity).execute(a);

    }


}
