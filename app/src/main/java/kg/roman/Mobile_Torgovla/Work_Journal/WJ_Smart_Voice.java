package kg.roman.Mobile_Torgovla.Work_Journal;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterAde_List_RN_Table;
import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_Login;
import kg.roman.Mobile_Torgovla.FTP.Sunbell_FtpAction;
import kg.roman.Mobile_Torgovla.FTP.Sunbell_FtpAsyncTask_Filial;
import kg.roman.Mobile_Torgovla.FTP.Sunbell_FtpAsyncTask_SQLDB_FILE;
import kg.roman.Mobile_Torgovla.FTP.Sunbell_FtpConnection;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_List_Smart_Voice;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Login;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Otchet_Ostatok;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Spinner_Filter;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_WJ_Favorite;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_List_Smart_Voice;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Otchet_Ostatok;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Spinner_Filter;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_WJ_Favorite;
import kg.roman.Mobile_Torgovla.R;

public class WJ_Smart_Voice extends AppCompatActivity {
    public ArrayList<ListAdapterSimple_Login> list_spinner_func = new ArrayList<ListAdapterSimple_Login>();
    public ListAdapterAde_Login adapterPriceClients_spinner_func;

    public ArrayList<ListAdapterSimple_List_Smart_Voice> list_listview_func = new ArrayList<ListAdapterSimple_List_Smart_Voice>();
    public ListAdapterAde_List_Smart_Voice adapterPriceClients_list_smart;

    public Context context_Activity;
    public String[] mass_smart_func;
    public ListView listView;
    public Spinner spinner;
    public FloatingActionButton button_smart;
    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    public String new_name_smart;
    public String spinner_select, name_columns, query_standart, query_count;
    public String PEREM_FTP_SERV, PEREM_FTP_LOGIN, PEREM_FTP_PASS, PEREM_FTP_DISTR_XML, PEREM_FTP_DISTR_db3;
    public SharedPreferences.Editor ed;
    public SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_smart_voice);
        context_Activity = WJ_Smart_Voice.this;
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        PEREM_FTP_SERV = sp.getString("PEREM_FTP_SERV", "0");                    //чтение данных: имя сервера
        PEREM_FTP_LOGIN = sp.getString("PEREM_FTP_LOGIN", "0");                  //чтение данных: имя логин
        PEREM_FTP_PASS = sp.getString("PEREM_FTP_PASS", "0");                    //чтение данных: имя пароль

        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //  getSupportActionBar().setIcon(R.drawable.icon_image);
        //  getSupportActionBar().setTitle("Остатки товара по брендам и складам");
        // getSupportActionBar().setSubtitle("Остатки на " + sp.getString("Sync_Data_0", "0"));

        listView = findViewById(R.id.list_smart_voice);
        spinner = findViewById(R.id.spinner_smart_voice);
        button_smart = findViewById(R.id.floatingActionButton_smart_voice);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        mass_smart_func = getResources().getStringArray(R.array.mass_smart_func);

        list_spinner_func.clear();
        for (int i = 0; i < mass_smart_func.length; i++) {
            list_spinner_func.add(new ListAdapterSimple_Login(mass_smart_func[i], ""));
        }

        adapterPriceClients_spinner_func = new ListAdapterAde_Login(context_Activity, list_spinner_func);
        adapterPriceClients_spinner_func.notifyDataSetChanged();
        spinner.setAdapter(adapterPriceClients_spinner_func);
        spinner_select = mass_smart_func[0];

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.textView_login);
                spinner_select = textView.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.e("Поиск: ", "Readil");
                list_listview_func.clear();
                adapterPriceClients_list_smart = new ListAdapterAde_List_Smart_Voice(context_Activity, list_listview_func);
                adapterPriceClients_list_smart.notifyDataSetChanged();
                listView.setAdapter(adapterPriceClients_list_smart);
            }

            @Override
            public void onBeginningOfSpeech() {
               /* editText.setText("");
                editText.setHint("Listening...");*/
                Log.e("Поиск: ", "Begin");

            }

            @Override
            public void onRmsChanged(float v) {
                Log.e("Поиск: ", "RMS");
            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                Log.e("Поиск: ", "END");
                button_smart.setImageResource(R.drawable.icon_smart_voice);
            }

            @Override
            public void onError(int i) {
                Log.e("Поиск: ", "Error");

            }

            @Override
            public void onResults(Bundle bundle) {
                Log.e("Поиск: ", "Result");
                //micButton.setImageResource(R.drawable.ic_mic_black_off);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                // editText.setText(data.get(0));
                getSupportActionBar().setSubtitle(data.get(0));
                Log.e("Поиск: ", "ТЕКС:" + data.get(0));
                new_name_smart = data.get(0);

                Smart_Voice_To_Standart_Name(spinner_select, new_name_smart, new_name_smart.toLowerCase(), new_name_smart.toUpperCase());
                adapterPriceClients_list_smart = new ListAdapterAde_List_Smart_Voice(context_Activity, list_listview_func);
                adapterPriceClients_list_smart.notifyDataSetChanged();
                listView.setAdapter(adapterPriceClients_list_smart);

            }

            @Override
            public void onPartialResults(Bundle bundle) {
                Log.e("Поиск: ", "P_Result");

            }

            @Override
            public void onEvent(int i, Bundle bundle) {
                Log.e("Поиск: ", "Event");

            }
        });

        button_smart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speechRecognizer.startListening(speechRecognizerIntent);
                button_smart.setImageResource(R.drawable.icon_smart_voice_click);
            }
        });

     /*   button_smart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    button_smart.setImageResource(R.drawable.icon_smart_voice);
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
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
        int id = item.getItemId();
        try {
            switch (id) {
                case R.id.menu_filter: {
                    try {
                        //1) Создать таблицу SQL_CREATE();
                        //2) Очистить таблицу SQL_DELETE();
                        //3) Записать данные в таблицу SQL_WRITE();
                       // SQL_DOWLOND_SORT_NOMENCLATURA();  // скачать данные для добавления
                      //  SQL_CREATE_SORT_NOMENCLATURA();  // Создание таблицы и удаление данных
                     //   SQL_INSERT_SORT_NOMENCLATURA();  // Запись данных в даблицу

                        SQL_CREATE_SMART_Search();
                        SQL_INSERT_SMART_Search();
                    } catch (Exception e) {

                    }
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

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }

    protected void Smart_Voice_To_Standart_Name(String spin_text, String s_name, String st_name, String L_NAME) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("sunbell_base_db.db3", MODE_PRIVATE, null);
        switch (spin_text) {
            case "стандартный":
                name_columns = "AND (base_in_smart_kin.smart_standart LIKE '%" + s_name + "%' " +
                        "OR base_in_smart_kin.smart_standart LIKE '%" + st_name + "%' " +
                        "OR base_in_smart_kin.smart_standart LIKE '%" + L_NAME + "%')";
                break;
            case "по бренду":
                name_columns = "AND (base_in_smart_kin.smart_brends LIKE '%" + s_name + "%' " +
                        "OR base_in_smart_kin.smart_brends LIKE '%" + st_name + "%' " +
                        "OR base_in_smart_kin.smart_brends LIKE '%" + L_NAME + "%')";
                break;
            case "по названию":
                name_columns = "AND (base_in_nomeclature.name LIKE '%" + s_name + "%' " +
                        "OR base_in_nomeclature.name LIKE '%" + st_name + "%' " +
                        "OR base_in_nomeclature.name LIKE '%" + L_NAME + "%')";
                break;
            default:
                name_columns = "";
                break;
        }
        String query = "SELECT base_in_smart_kin.smart_brends, base_in_smart_kin.smart_standart, " +
                "base_in_smart_kin.koduid, base_in_nomeclature.name, base_in_nomeclature.koduid, " +
                "base_in_nomeclature.kod_univ, base_in_nomeclature.kolbox, base_in_ostatok.count, " +
                "base_in_price.price, base_in_image.kod_image\n" +
                "FROM base_in_smart_kin\n" +
                "LEFT JOIN base_in_nomeclature ON base_in_nomeclature.koduid = base_in_nomeclature.koduid\n" +
                "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                "WHERE (base_in_smart_kin.koduid == base_in_nomeclature.koduid) " +
                "" + name_columns + " " +
                "AND (base_in_ostatok.count > 0) \n" +
                "GROUP BY base_in_nomeclature.name\n" +
                "ORDER BY base_in_nomeclature.name ASC";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String koduid = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));
            String kod_univ = cursor.getString(cursor.getColumnIndexOrThrow("kod_univ"));
            String kolbox = cursor.getString(cursor.getColumnIndexOrThrow("kolbox"));
            String count = cursor.getString(cursor.getColumnIndexOrThrow("count"));
            String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
            String kod_image = cursor.getString(cursor.getColumnIndexOrThrow("kod_image"));
            switch (spin_text) {
                case "по цене: больше": {
                    Integer price_int = Integer.parseInt(price);
                    Integer st_name_int = Integer.parseInt(st_name);
                    if (st_name_int < price_int) {
                        list_listview_func.add(new ListAdapterSimple_List_Smart_Voice(name, koduid, kod_univ, kolbox, count, price, kod_image));
                    }
                }
                break;
                case "по цене: меньше": {
                    Integer price_int = Integer.parseInt(price);
                    Integer st_name_int = Integer.parseInt(st_name);
                    if (st_name_int > price_int) {
                        list_listview_func.add(new ListAdapterSimple_List_Smart_Voice(name, koduid, kod_univ, kolbox, count, price, kod_image));
                    }
                }
                break;
                case "по цене: равно": {
                    Integer price_int = Integer.parseInt(price);
                    Integer st_name_int = Integer.parseInt(st_name);
                    if (st_name_int == price_int) {
                        list_listview_func.add(new ListAdapterSimple_List_Smart_Voice(name, koduid, kod_univ, kolbox, count, price, kod_image));
                    }
                }
                break;
                default:
                    list_listview_func.add(new ListAdapterSimple_List_Smart_Voice(name, koduid, kod_univ, kolbox, count, price, kod_image));
                    break;
            }

            cursor.moveToNext();


        }
        cursor.close();
        db.close();

    }




    protected void SQL_DOWLOND_SORT_NOMENCLATURA()
    {
        String file_name, file_put;
        file_name = "sunbell_base_db_new_write.db3";
        file_put = "/MT_Sunbell_Karakol/SqliteDB/";
        Log.e("UP", file_put);
        Log.e("UP", file_name);
        Sunbell_FtpConnection c = new Sunbell_FtpConnection();
        c.server = PEREM_FTP_SERV;
        c.port = 21;
        c.username = PEREM_FTP_LOGIN;
        c.password = PEREM_FTP_PASS;
        c.file_name = file_name;

        Sunbell_FtpAction a = new Sunbell_FtpAction();
        a.success = true;
        a.connection = c;
        a.fileName = file_name;
        a.remoteFilename = file_put + "/" + file_name;
        new Sunbell_FtpAsyncTask_SQLDB_FILE(context_Activity).execute(a);


    }
    protected void SQL_CREATE_SORT_NOMENCLATURA()
    {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("sunbell_base_db.db3", MODE_PRIVATE, null);
        String query = "CREATE TABLE IF NOT EXISTS base_group_sql (\n" +
                "    name       TEXT,\n" +
                "    uid_name   TEXT,\n" +
                "    type_group INT  NOT NULL\n" +
                "                    PRIMARY KEY\n" +
                ");";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            Log.e("CREATE", "Создание таблицы");
            cursor.moveToNext();
        }
        cursor.close();


        String query_delete = "DELETE FROM base_group_sql";
        final Cursor cursor_delete = db.rawQuery(query_delete, null);
        while (cursor_delete.isAfterLast() == false) {
            Log.e("CREATE", "Очистка таблицы");
            cursor_delete.moveToNext();
        }
        cursor_delete.close();

        db.close();

    }
    protected void SQL_INSERT_SORT_NOMENCLATURA()
    {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("sunbell_base_db_new_write.db3", MODE_PRIVATE, null);
        SQLiteDatabase db_write = getBaseContext().openOrCreateDatabase("sunbell_base_db.db3", MODE_PRIVATE, null);
        String query = "SELECT * FROM base_group_sql";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        ContentValues localContentValues = new ContentValues();
        while (cursor.isAfterLast() == false) {
            Log.e("CREATE", "Запись в таблицу");
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String uid_name = cursor.getString(cursor.getColumnIndexOrThrow("uid_name"));
            String type_group = cursor.getString(cursor.getColumnIndexOrThrow("type_group"));
            localContentValues.put("name", name);
            localContentValues.put("uid_name", uid_name);
            localContentValues.put("type_group", type_group);
            db_write.insert("base_group_sql", null, localContentValues);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    protected void SQL_CREATE_SMART_Search()
    {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("sunbell_base_db.db3", MODE_PRIVATE, null);
        String query = "CREATE TABLE base_in_smart_kin (\n" +
                "    smart_brends   TEXT,\n" +
                "    smart_standart TEXT,\n" +
                "    name           TEXT,\n" +
                "    koduid         TEXT\n" +
                ");\n";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            Log.e("CREATE", "Создание таблицы");
            cursor.moveToNext();
        }
        cursor.close();


        String query_delete = "DELETE FROM base_in_smart_kin";
        final Cursor cursor_delete = db.rawQuery(query_delete, null);
        while (cursor_delete.isAfterLast() == false) {
            Log.e("CREATE", "Очистка таблицы");
            cursor_delete.moveToNext();
        }
        cursor_delete.close();

        db.close();

    }
    protected void SQL_INSERT_SMART_Search()
    {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("sunbell_base_db_new_write.db3", MODE_PRIVATE, null);
        SQLiteDatabase db_write = getBaseContext().openOrCreateDatabase("sunbell_base_db.db3", MODE_PRIVATE, null);
        String query = "SELECT * FROM base_in_smart_kin";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        ContentValues localContentValues = new ContentValues();
        while (cursor.isAfterLast() == false) {
            Log.e("CREATE", "Запись в таблицу");
            String smart_brends = cursor.getString(cursor.getColumnIndexOrThrow("smart_brends"));
            String smart_standart = cursor.getString(cursor.getColumnIndexOrThrow("smart_standart"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String koduid = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));
            localContentValues.put("smart_brends", smart_brends);
            localContentValues.put("smart_standart", smart_standart);
            localContentValues.put("name", name);
            localContentValues.put("koduid", koduid);
            db_write.insert("base_in_smart_kin", null, localContentValues);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }


}