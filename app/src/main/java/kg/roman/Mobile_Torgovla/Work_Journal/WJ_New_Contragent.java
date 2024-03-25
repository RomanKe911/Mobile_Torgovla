package kg.roman.Mobile_Torgovla.Work_Journal;


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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_Login;
import kg.roman.Mobile_Torgovla.FormaZakazaSelectClient.WJ_Forma_Zakaza_L1;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Login;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.Spravochnik.SPR_Cnt_Agent;

public class WJ_New_Contragent extends AppCompatActivity {

    public ArrayList<ListAdapterSimple_Login> adapter_spinner_type_name = new ArrayList<ListAdapterSimple_Login>();
    public ListAdapterAde_Login adapterPriceClients_spinner_type_name;

    public ArrayList<ListAdapterSimple_Login> adapter_spinner_town = new ArrayList<ListAdapterSimple_Login>();
    public ListAdapterAde_Login adapterPriceClients_spinner_town;

    public ArrayList<ListAdapterSimple_Login> adapter_spinner_catigory = new ArrayList<ListAdapterSimple_Login>();
    public ListAdapterAde_Login adapterPriceClients_spinner_catigory;

    public Context context_Activity;
    public Button button_save;
    public Spinner spinner_town, spinner_catigory, spinner_name_type;
    public EditText editText_name, editText_adress, editText_adress_number, editText_phone;
    public String[] mass_town, mass_carigory, mass_name_type;
    public String end_text_name_type, end_text_name, end_text_town, end_text_street, end_text_number, end_text_phone, end_text_catigory;
    public String from, attach, text, title, where, new_write;

    public SharedPreferences.Editor ed;
    public SharedPreferences sp;
    public String PEREM_AG_NAME, PEREM_FTP_PUT, PEREM_DB3_CONST, PEREM_AG_REGION, Log_Data, PEREM_RE_FORMA;
    public String data_mail_login, data_mail_pass, data_mail_where, data_mail_from;
    public androidx.appcompat.app.AlertDialog.Builder dialog_cancel;
    public WJ_New_Contragent.sender_mail_async async_sending;
    public Intent intent;
    public String back_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_new_contagent);
        context_Activity = WJ_New_Contragent.this;
        Log_Data = "NEW_CONTRAGET: ";
        Constanta_Read();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon_image);
        getSupportActionBar().setTitle("Добавление нового контрагента");
        getSupportActionBar().setSubtitle("");

        Bundle arguments = getIntent().getExtras();
        back_string = arguments.get("Layout_INTENT_BACK").toString();

        spinner_town = findViewById(R.id.spinner_town);
        spinner_catigory = findViewById(R.id.spinner_catigory);
        spinner_name_type = findViewById(R.id.spinner_type_name);
        editText_name = findViewById(R.id.edit_text_contragent);
        editText_adress = findViewById(R.id.edit_text_adress);
        editText_adress_number = findViewById(R.id.edit_text_adress_number);
        editText_phone = findViewById(R.id.edit_text_phone);
        button_save = findViewById(R.id.btn_up_server);

        //   end_text_name_type = "Маг. ";


        mass_town = getResources().getStringArray(R.array.mass_Region_Put_Name);
        mass_carigory = getResources().getStringArray(R.array.mass_catigory_tt);
        mass_name_type = getResources().getStringArray(R.array.mass_type_tt);

        adapter_spinner_type_name.clear();
        for (int i = 0; i < mass_name_type.length; i++) {
            adapter_spinner_type_name.add(new ListAdapterSimple_Login(mass_name_type[i], ""));
        }
        adapterPriceClients_spinner_type_name = new ListAdapterAde_Login(context_Activity, adapter_spinner_type_name);
        adapterPriceClients_spinner_type_name.notifyDataSetChanged();
        spinner_name_type.setAdapter(adapterPriceClients_spinner_type_name);


        adapter_spinner_town.clear();
        for (int i = 0; i < mass_town.length; i++) {
            adapter_spinner_town.add(new ListAdapterSimple_Login(mass_town[i], ""));
        }
        adapterPriceClients_spinner_town = new ListAdapterAde_Login(context_Activity, adapter_spinner_town);
        adapterPriceClients_spinner_town.notifyDataSetChanged();
        spinner_town.setAdapter(adapterPriceClients_spinner_town);

        adapter_spinner_catigory.clear();
        for (int i = 0; i < mass_carigory.length; i++) {
            adapter_spinner_catigory.add(new ListAdapterSimple_Login(mass_carigory[i], ""));
        }
        adapterPriceClients_spinner_catigory = new ListAdapterAde_Login(context_Activity, adapter_spinner_catigory);
        adapterPriceClients_spinner_catigory.notifyDataSetChanged();
        spinner_catigory.setAdapter(adapterPriceClients_spinner_catigory);

        editText_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                end_text_name = editText_name.getText().toString();
            }
        });

        editText_adress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                end_text_street = editText_adress.getText().toString();
            }
        });

        editText_adress_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                end_text_number = editText_adress_number.getText().toString();
            }
        });

        editText_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                end_text_phone = editText_phone.getText().toString();
            }
        });

        spinner_town.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = view.findViewById(R.id.textView_login);
                end_text_town = tv.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_catigory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = view.findViewById(R.id.textView_login);
                end_text_catigory = tv.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_name_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = view.findViewById(R.id.textView_login);

                switch (tv.getText().toString()) {
                    case "Магазин": {
                        end_text_name_type = "Маг.";
                    }
                    break;
                    case "Маркет": {
                        end_text_name_type = "Маркет.";
                    }
                    break;
                    case "Аптека": {
                        end_text_name_type = "Аптека.";
                    }
                    break;
                    case "Контейнер": {
                        end_text_name_type = "Конт.";
                    }
                    break;
                    case "Павильон": {
                        end_text_name_type = "Пав.";
                    }
                    break;
                    case "Рынок": {
                        end_text_name_type = "р-к.";
                    }
                    break;
                    default: {
                        end_text_name_type = "";
                    }
                    break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.e(Log_Data, "@" + end_text_name_type);

                    if (end_text_name.equals("")) {
                        Toast.makeText(context_Activity, "заполните поле: контрагент!", Toast.LENGTH_SHORT).show();
                    } else if (end_text_town.equals("")) {
                        Toast.makeText(context_Activity, "выберите город!", Toast.LENGTH_SHORT).show();
                    } else if (end_text_street.equals("")) {
                        Toast.makeText(context_Activity, "заполните поле: улица!", Toast.LENGTH_SHORT).show();
                    } else if (end_text_catigory.equals("")) {
                        Toast.makeText(context_Activity, "выберите категорию!", Toast.LENGTH_SHORT).show();
                    } else {
                        Sqlite_DB_Mail_Data();
                        Mail_Text();
                        Mail();
                        async_sending = new WJ_New_Contragent.sender_mail_async();
                        async_sending.execute();
                        Log.e(Log_Data, "успешно отправленно");
                    }
                } catch (Exception e) {
                    Toast.makeText(context_Activity, "Ошибка: обработки данных!", Toast.LENGTH_SHORT).show();
                    Log.e(Log_Data, "Ошибка: обработки данных!");
                }

            }
        });
    }

    protected void Sqlite_DB_Mail_Data() {
        SQLiteDatabase db_sqlite = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
        final String query = "SELECT * FROM const_mail";
        final Cursor cursor = db_sqlite.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String region = cursor.getString(cursor.getColumnIndexOrThrow("region"));
            String mail = cursor.getString(cursor.getColumnIndexOrThrow("operator_mail"));
            String mail_pass = cursor.getString(cursor.getColumnIndexOrThrow("operator_mail_pass"));
            String mail_where = cursor.getString(cursor.getColumnIndexOrThrow("mail_where"));
            if (region.equals(PEREM_FTP_PUT)) {
                data_mail_where = mail_where;
                data_mail_from = mail;
                data_mail_login = mail;
                data_mail_pass = mail_pass;
            }
            cursor.moveToNext();
        }
        Log.e("Mail..", data_mail_login + data_mail_pass);
        cursor.close();
        db_sqlite.close();
    }

    // Заполение текста письма
    protected void Mail_Text() {
        new_write = ("");
        new_write = (new_write + "Добавление нового контрагента:");
        new_write = (new_write + "\n\n");
        new_write = (new_write + "Агент: " + PEREM_AG_NAME + "\n");
        new_write = (new_write + "Маршрут: " + PEREM_AG_REGION + "\n");
        new_write = (new_write + "\n");
        if (!end_text_name_type.equals("")) {
            new_write = (new_write + "Наименование контрагента: " + end_text_name_type + "\"" + end_text_name + "\"\n");
        } else {
            new_write = (new_write + "Наименование контрагента: " + end_text_name + "\n");
        }

        new_write = (new_write + "Адрес клиента: г." + end_text_town + "; ул." + end_text_street + "; дом №" + end_text_number + "\n");
        new_write = (new_write + "Номер телефона: " + end_text_phone + "\n");
        new_write = (new_write + "Категория ТТ: " + end_text_catigory + "\n");
    }

    // Обработка данных текста письма
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
            async_sending.cancel(true);
            Toast.makeText(context_Activity, "Данные успешно отправленны оператору!", Toast.LENGTH_LONG).show();
            if (back_string.equals("GLOBAL")) {
                Intent intent = new Intent(context_Activity, SPR_Cnt_Agent.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza_L1.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected Boolean doInBackground(Object... params) {


            try {
                title = "Агент: " + PEREM_AG_NAME;
                text = new_write;
                from = data_mail_from;
                where = data_mail_where;
                attach = "";

               /* MailSenderClass sender = new MailSenderClass(data_mail_login, data_mail_pass, "465", "smtp.gmail.com");   // Null
                sender.sendMail(title, text, from, where, attach);*/

            } catch (Exception e) {
                Toast.makeText(context_Activity, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
            }

            return false;
        }
    }


    // Константы для чтения
    protected void Constanta_Read() {
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);

        PEREM_AG_NAME = sp.getString("PEREM_AG_NAME", "0");                       //чтение данных: передача кода агента (Имя пользователя)
        PEREM_FTP_PUT = sp.getString("list_preference_ftp", "0");                 //чтение данных:
        PEREM_DB3_CONST = sp.getString("PEREM_DB3_CONST", "0");                   //чтение данных: Путь к базам данных с константами
        PEREM_AG_REGION = sp.getString("PEREM_AG_REGION", "0");                   //чтение данных: маршруты для привязки к контагентам

        PEREM_RE_FORMA = sp.getString("Layout_Worma_Zaraka", "0");                   //чтение данных: маршруты для привязки к контагентам

        Log.e(Log_Data, PEREM_AG_NAME);
        Log.e(Log_Data, PEREM_FTP_PUT);
        Log.e(Log_Data, PEREM_DB3_CONST);
        Log.e(Log_Data, PEREM_AG_REGION);


        end_text_name = "";
        end_text_town = "";
        end_text_street = "";
        end_text_number = "";
        end_text_phone = "";
        end_text_catigory = "";
        end_text_name_type = "";
    }

    public void onBackPressed() {
        dialog_cancel = new androidx.appcompat.app.AlertDialog.Builder(context_Activity);
        dialog_cancel.setTitle("Вы хотите выйти?");
        dialog_cancel.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                if (back_string.equals("GLOBAL")) {
                    Intent intent = new Intent(context_Activity, SPR_Cnt_Agent.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza_L1.class);
                    startActivity(intent);
                    finish();
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
            }
        });
        dialog_cancel.show();
    }

}
