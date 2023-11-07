package kg.roman.Mobile_Torgovla.Spravochnik;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Debet;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Debet;
import kg.roman.Mobile_Torgovla.R;

public class SPR_Debet_Up extends AppCompatActivity {

    public ArrayList<ListAdapterSimple_Debet> debet = new ArrayList<ListAdapterSimple_Debet>();
    public ListAdapterAde_Debet adapterPriceClients_debet;
    public Context context, context_Activity;
    public String UID_AGENTS, Agent_Type, k_agent_Name;
    public ListView listView;
    public String perem_agent;
    public Spinner spinner;
    public String[][] mass;
    public String[] mass2;
    public String thisdata, StartActivity;
    public Button button_ok, button_Go, button_cancel, Button;
    public View localView;
    public TextView Name, UID;
    public TextView Debet;
    public EditText Edit;
    public String uid, summa_debet, Format_Double, name_debet, agent_uid;
    public Double perem;
    public Integer int_spinner;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now, this_data_work_now, this_vrema_work_now;

    public SharedPreferences.Editor ed;
    public SharedPreferences sp;
    public String PEREM_FTP_SERV, PEREM_FTP_LOGIN, PEREM_FTP_PASS, PEREM_FTP_DISTR_XML, PEREM_FTP_DISTR_db3,
            PEREM_IMAGE_PUT_SDCARD, PEREM_IMAGE_PUT_PHONE;
    public String PEREM_MAIL_LOGIN, PEREM_MAIL_PASS, PEREM_MAIL_START, PEREM_MAIL_END,
            PEREM_DB3_CONST, PEREM_DB3_BASE, PEREM_DB3_RN, PEREM_ANDROID_ID_ADMIN, PEREM_ANDROID_ID;
    public String PEREM_AG_UID, PEREM_AG_NAME, PEREM_AG_REGION, PEREM_AG_UID_REGION, PEREM_AG_CENA,
            PEREM_AG_SKLAD, PEREM_AG_UID_SKLAD, PEREM_AG_TYPE_REAL, PEREM_AG_TYPE_USER,
            PEREM_WORK_DISTR, PEREM_KOD_MOBILE;
    public String Save_dialog_up_, grb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spr_debet);
        context_Activity = SPR_Debet_Up.this;
        Calendate_New();

        ListAdapter_ThisData();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon_image);
        getSupportActionBar().setTitle("Консигнации");
        getSupportActionBar().setSubtitle(PEREM_AG_NAME);

        listView = findViewById(R.id.listview_cagent);
       // spinner = findViewById(R.id.spinner_uid);


       /* sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        UID_AGENTS = sp.getString("UID_AGENTS", "0");
        Agent_Type = sp.getString("PEREM_Type", "0");*/


        Intent intent = getIntent();
        StartActivity = intent.getStringExtra("StarAct_1");
        Log.e("Pref", StartActivity);


        ListAdapter_Spinner();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mass2);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (StartActivity) {
                    case "StarAct_1": {
                        debet.clear();
                        ListAdapter_All(i);
                        int_spinner = i;
                        k_agent_Name = mass2[i];
                        adapterPriceClients_debet = new ListAdapterAde_Debet(context_Activity, debet);
                        adapterPriceClients_debet.notifyDataSetChanged();
                        listView.setAdapter(adapterPriceClients_debet);
                    }
                    break;

                    case "StarAct_2": {
                        debet.clear();
                        ListAdapter_Klients(i);
                        int_spinner = i;
                        k_agent_Name = mass2[i];
                        adapterPriceClients_debet = new ListAdapterAde_Debet(context_Activity, debet);
                        adapterPriceClients_debet.notifyDataSetChanged();
                        listView.setAdapter(adapterPriceClients_debet);
                    }
                    break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                uid = ((TextView) view.findViewById(R.id.tvw_d_region_uid)).getText().toString();
                summa_debet = ((TextView) view.findViewById(R.id.tvw_d_title_1)).getText().toString().replace(",", ".");
                name_debet = ((TextView) view.findViewById(R.id.tvw_G_ost_name)).getText().toString();

                localView = LayoutInflater.from(context_Activity).inflate(R.layout.localview_dialog_debet, null);
                Name = localView.findViewById(R.id.tvw_dialog_name);
                UID = localView.findViewById(R.id.tvw_list_ag_uid);
                Debet = localView.findViewById(R.id.tvw_dialog_debet);
                Edit = localView.findViewById(R.id.edit_dialog_debet);
                UID.setText(uid);
                Debet.setText(summa_debet);
                Name.setText(name_debet);
                perem = Double.parseDouble(summa_debet);


                Edit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        try {


                            switch (StartActivity) {
                                case "StarAct_1": {
//int val = Integer.parseInt(s.toString());
                                    double val = Double.parseDouble(s.toString());
                                    Log.e("Data", val + ", " + perem + ", " + summa_debet);
                                    if (val > perem) {
                                        s.replace(0, s.length(), String.valueOf(perem), 0, String.valueOf(perem).length());
                                        InputFilter[] filters = new InputFilter[1];
                                        filters[0] = new InputFilter.LengthFilter(String.valueOf(perem).length()); //Filter to 10 characters
                                        Edit.setFilters(filters);
                                    } else if (val < 1) {
                                        s.replace(0, s.length(), "1", 0, 1);
                                    }
                                }
                                break;

                                case "StarAct_2": {

                                }
                                break;
                            }
                        } catch (Exception e) {
                            Log.e("EditText", "не верный формат");
                        }
                    }
                });


                AlertDialog.Builder localBuilder = new AlertDialog.Builder(context_Activity);
                localBuilder.setView(localView);
                localBuilder.setTitle("Оплата консигнаций!");
                localBuilder.setCancelable(false).setIcon(R.drawable.icon_korz).setPositiveButton(" ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        if (Edit.getText().length() == 0) {
                            Edit.setError("Пустое поле!!!");
                            Toast.makeText(context_Activity, "Пустое поле!!", Toast.LENGTH_SHORT).show();
                        } else {
                            switch (StartActivity) {
                                case "StarAct_1": {
                                }
                                ListAdapter_DeleteDebet(uid, Edit.getText().toString());
                                ListAdapter_InsertDebet(name_debet, uid, Edit.getText().toString().replace(",", "."));
                                debet.clear();
                                ListAdapter_All(int_spinner);
                                adapterPriceClients_debet = new ListAdapterAde_Debet(context_Activity, debet);
                                adapterPriceClients_debet.notifyDataSetChanged();
                                listView.setAdapter(adapterPriceClients_debet);
                                break;

                                case "StarAct_2": {
                                    ListAdapter_InsertDebet(name_debet, uid, Edit.getText().toString().replace(",", "."));
                                    debet.clear();
                                    ListAdapter_Klients(int_spinner);
                                    adapterPriceClients_debet = new ListAdapterAde_Debet(context_Activity, debet);
                                    adapterPriceClients_debet.notifyDataSetChanged();
                                    listView.setAdapter(adapterPriceClients_debet);
                                }
                                break;
                            }


                        }


                    }
                }).setNegativeButton(" ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        hideInputMethod();
                        paramDialogInterface.cancel();

                    }
                });
                AlertDialog localAlertDialog = localBuilder.create();
                localAlertDialog.show();
                button_ok = localAlertDialog.getButton(-1);
                button_ok.setCompoundDrawablesWithIntrinsicBounds(R.drawable.button_ok, 0, 0, 0);
                button_cancel = localAlertDialog.getButton(-2);
                button_cancel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_close, 0);


            }
        });


    }


    protected void ListAdapter_All(Integer i) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("otcheti_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base_debet.kod_uid, base_debet.data, base_debet.debet, base_contragents.uid_expdr, base_contragents.uid_agent, base_contragents.k_agent\n" +
                "FROM base_debet\n" +
                "LEFT JOIN base_contragents ON base_debet.kod_uid = base_contragents.uid_k_agent\n" +
                "WHERE (base_contragents.uid_agent LIKE '%" + mass[i][0] + "%' AND base_debet.debet > 0)\n" +
                "OR (base_contragents.uid_expdr LIKE '%" + mass[i][0] + "%' AND base_debet.debet > 0)";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String K_Agent = cursor.getString(cursor.getColumnIndex("k_agent"));
            String UID_K_AGENT = cursor.getString(cursor.getColumnIndex("kod_uid"));
            String UID_AGENT = cursor.getString(cursor.getColumnIndex("uid_agent"));
            // String Debet = cursor.getString(cursor.getColumnIndex("debet")).replace("0.0", "0").replace("0,0", "0");
            String Debet = cursor.getString(cursor.getColumnIndex("debet"));
            Log.e("Debet", Debet);
            Log.e("Debet1", Debet.replace("00.00", "0"));
            Log.e("Debet2", Debet.replace("00.00", "0").replace("00,00", "0"));
            //  String Agent = cursor.getString(cursor.getColumnIndex("uid_agent"));
            debet.add(new ListAdapterSimple_Debet(K_Agent, UID_K_AGENT, Debet, perem_agent));
            Log.e(this.getLocalClassName(), K_Agent + UID_K_AGENT + UID_AGENT + Debet);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    protected void ListAdapter_Klients(Integer i) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("otcheti_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base_contragents.uid_k_agent, base_contragents.k_agent, base_contragents.adress, base_contragents.roadname, base_contragents.uid_agent\n" +
                "FROM base_contragents\n" +
                "WHERE (base_contragents.uid_agent LIKE '%" + mass[i][0] + "%') OR (base_contragents.uid_expdr LIKE '%" + mass[i][0]+ "%');";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String K_Agent = cursor.getString(cursor.getColumnIndex("k_agent"));
            String UID_K_AGENT = cursor.getString(cursor.getColumnIndex("uid_k_agent"));
            String UID_AGENT = cursor.getString(cursor.getColumnIndex("uid_agent"));
            debet.add(new ListAdapterSimple_Debet(K_Agent, UID_K_AGENT, "0", perem_agent));
            Log.e(this.getLocalClassName(), K_Agent + UID_K_AGENT + UID_AGENT + Debet);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    protected void ListAdapter_Case() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("otcheti_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base_debet.kod_uid, base_debet.data, base_debet.debet, " +
                "base_contragents.uid_agent, base_contragents.k_agent\n" +
                "FROM base_debet\n" +
                "LEFT JOIN base_contragents ON base_debet.kod_uid = base_contragents.uid_k_agent\n" +
                "WHERE base_contragents.uid_agent LIKE '" + UID_AGENTS + "' AND base_debet.debet > 0";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String K_Agent = cursor.getString(cursor.getColumnIndex("k_agent"));
            String UID_K_AGENT = cursor.getString(cursor.getColumnIndex("kod_uid"));
            String Debet = cursor.getString(cursor.getColumnIndex("debet"));
            String Agent = cursor.getString(cursor.getColumnIndex("uid_agent"));
            if (UID_AGENTS.equals(UID_K_AGENT)) {
                switch (Agent) {
                    case "A8BA1F48-C7E1-497B-B74A-D86426684712":
                        perem_agent = "Керкин Роман";
                        debet.add(new ListAdapterSimple_Debet(K_Agent, UID_K_AGENT, Debet, perem_agent));
                        cursor.moveToNext();
                        break;
                    case "C611B483-547F-41B2-9DF0-050C34682012":
                        perem_agent = "Керкина Елена";
                        debet.add(new ListAdapterSimple_Debet(K_Agent, UID_K_AGENT, Debet, perem_agent));
                        cursor.moveToNext();
                        break;
                    case "FC29F24F-24AE-488D-88B0-7B9BFD7A75A3":
                        perem_agent = "Игнатенко Раиса";
                        debet.add(new ListAdapterSimple_Debet(K_Agent, UID_K_AGENT, Debet, perem_agent));
                        cursor.moveToNext();
                        break;
                    default:
                        break;
                }

            } else
                cursor.moveToNext();

        }
        cursor.close();
        db.close();
    }

    protected void ListAdapter_Spinner() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("otcheti_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base_agent.name_agent, base_agent.uid_agent\n" +
                "FROM base_agent";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        mass = new String[cursor.getCount()][cursor.getCount()];
        mass2 = new String[cursor.getCount()];
        while (cursor.isAfterLast() == false) {
            String uid_agent = cursor.getString(cursor.getColumnIndex("uid_agent"));
            String name_agent = cursor.getString(cursor.getColumnIndex("name_agent"));
            mass[cursor.getPosition()][0] = uid_agent;
            mass[cursor.getPosition()][1] = name_agent;
            mass2[cursor.getPosition()] = name_agent;
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    protected void ListAdapter_ThisData() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("otcheti_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base_debet.data, base_debet.debet, base_debet.kod_uid\n" +
                "FROM base_debet";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String data = cursor.getString(cursor.getColumnIndex("data"));
        thisdata = data.replace('/', '.');  // формат месяц/день/год
        cursor.close();
        db.close();
    }

    protected void ListAdapter_DeleteDebet(String Perent_Uid, String Perent_Debet) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("otcheti_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base_debet.data, base_debet.debet, base_debet.kod_uid\n" +
                "FROM base_debet\n" +
                "WHERE base_debet.kod_uid LIKE '%" + Perent_Uid + "%'";

        ContentValues localContentValuesUpdate = new ContentValues();
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String debet = cursor.getString(cursor.getColumnIndex("debet")).replace(",", ".");
        String kod_uid = cursor.getString(cursor.getColumnIndex("kod_uid"));
        Double sum = Double.parseDouble(debet) - Double.parseDouble(Perent_Debet);
        if (sum > 0) {
            Format_Double = new DecimalFormat("#00.00").format(sum);
        } else Format_Double = "0";

        Log.e("Debet", Format_Double);
        localContentValuesUpdate.put("debet", Format_Double);
        String[] arrayOfString = new String[1];
        arrayOfString[0] = kod_uid;
        db.update("base_debet", localContentValuesUpdate, "kod_uid = ?", new String[]{kod_uid});
        cursor.close();
        db.close();
    }

    protected void ListAdapter_InsertDebet(String Ins_Name, String Ins_Uid, String Ins_Debet) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("otcheti_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base_otchet_debet.data, base_otchet_debet.agent, base_otchet_debet.debet, base_otchet_debet.kod_uid\n" +
                "FROM base_otchet_debet";
        ContentValues localContentValuesInsert = new ContentValues();
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToLast();
        localContentValuesInsert.put("name", Ins_Name);
        localContentValuesInsert.put("data", this_data_work_now);
        localContentValuesInsert.put("agent", k_agent_Name);
        localContentValuesInsert.put("kod_uid", Ins_Uid);
        localContentValuesInsert.put("debet", Ins_Debet);
        db.insert("base_otchet_debet", null, localContentValuesInsert);

        cursor.close();
        db.close();
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
        //2020-10-12
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


    protected void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            // imm.hideSoftInputFromWindow(dg_ed_editkol.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(listView.getWindowToken(), 0);
        }
    }

    /**
     * показываем программную клавиатуру
     */
    protected void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private Runnable mShowInputMethodTask = new Runnable() {
        public void run() {
            // showInputMethodForQuery();
        }
    };
}
