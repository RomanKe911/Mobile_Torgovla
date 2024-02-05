package kg.roman.Mobile_Torgovla.FormaZakaza;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Spinner_TY;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Spinner_TY;
import kg.roman.Mobile_Torgovla.R;

public class WJ_Forma_Zakaza_Dop_Data extends AppCompatActivity {

    public ArrayList<ListAdapterSimple_Spinner_TY> spinner_ty_add = new ArrayList<ListAdapterSimple_Spinner_TY>();
    public ListAdapterAde_Spinner_TY adapterPriceClients_ty;

    public ArrayList<ListAdapterSimple_Spinner_TY> spinner_credit = new ArrayList<ListAdapterSimple_Spinner_TY>();
    public ListAdapterAde_Spinner_TY adapterPriceClients_credit;

    public Context context_Activity;
    public Spinner spinner_ty, spinner_type_credit;
    public Button btn_data, btn_save;
    public CheckBox checkBox_nds;
    public EditText editText_credite_date, editText_skidka, editText_comment;
    public TextView text_summa, text_itogo, text_data, text_no_ty, text_title_name_skidka;

    public SharedPreferences sp;
    public SharedPreferences.Editor ed;
    public String summa_read, itogo_read, skidka_read, nds_read, date_credite_read, summa_read_edit, summa_min_skidka;
    public String Log_Text_Error, s_credit;
    public String[] mass_type;
    public Boolean visible_ty = false;
    public ConstraintLayout constraintLayout_ty;

    public Calendar c = Calendar.getInstance();
    public int mYear, mMonth, mDay;

    public String PEREM_K_AG_NAME, PEREM_K_AG_UID, PEREM_K_AG_ADRESS, PEREM_K_AG_KodRN,
            PEREM_K_AG_Data, PEREM_K_AG_Vrema, PEREM_K_AG_GPS;
    public String PEREM_FTP_SERV, PEREM_FTP_LOGIN, PEREM_FTP_PASS, PEREM_FTP_DISTR_XML,
            PEREM_FTP_DISTR_db3, PEREM_IMAGE_PUT_SDCARD, PEREM_IMAGE_PUT_PHONE,
            PEREM_PREFERENCE_PUT_FTP, PEREM_PREFERENCE_TYPE_TY;
    public String PEREM_MAIL_LOGIN, PEREM_MAIL_PASS, PEREM_MAIL_START, PEREM_MAIL_END,
            PEREM_DB3_CONST, PEREM_DB3_BASE, PEREM_DB3_RN, PEREM_ANDROID_ID_ADMIN, PEREM_ANDROID_ID;
    public String PEREM_AG_UID, PEREM_AG_NAME, PEREM_AG_REGION, PEREM_AG_UID_REGION, PEREM_AG_CENA,
            PEREM_AG_SKLAD, PEREM_AG_UID_SKLAD, PEREM_AG_TYPE_REAL, PEREM_AG_TYPE_USER,
            PEREM_WORK_DISTR, PEREM_KOD_MOBILE, PEREM_KOD_UID_KODRN, PEREM_K_AG_Data_WORK;
    public String PEREM_KLIENT_UID, PEREM_DIALOG_UID, PEREM_DIALOG_DATA_START,
            PEREM_DIALOG_DATA_END, PEREM_DISPLAY_START, PEREM_DISPLAY_END;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt_wj_forma_dop_data);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.office_title_user);
        getSupportActionBar().setTitle("Торговые условия");
        getSupportActionBar().setSubtitle("");
        context_Activity = WJ_Forma_Zakaza_Dop_Data.this;
        Log_Text_Error = "ERR_WJ_F_Z_D: ";
        Constanta_Read();
        spinner_ty = findViewById(R.id.spinner_ty);
        constraintLayout_ty = findViewById(R.id.block_ty_random);
        spinner_type_credit = findViewById(R.id.spinner_select_credit);
        btn_data = findViewById(R.id.btn_d_data);
        btn_save = findViewById(R.id.btn_d_save);
        text_data = findViewById(R.id.tvw_d_data);
        text_summa = findViewById(R.id.tvw_d_summa);
        text_itogo = findViewById(R.id.tvw_d_itogo);
        text_title_name_skidka = findViewById(R.id.tvw_title_name_skidka);
        text_no_ty = findViewById(R.id.tvw_dd_no);
        editText_credite_date = findViewById(R.id.edt_credite_date);
        editText_skidka = findViewById(R.id.edit_skidka_new);
        editText_comment= findViewById(R.id.edit_comment);

        text_no_ty.setVisibility(View.GONE);
        editText_credite_date.setVisibility(View.GONE);
        summa_read = "0.0";
        itogo_read = "0.0";
        date_credite_read = "0";
        summa_read_edit = "0.0";


        switch (PEREM_PREFERENCE_TYPE_TY) {
            case "standart": {
                Log.e("Error Spinner ", PEREM_PREFERENCE_TYPE_TY);
                spinner_ty.setVisibility(View.VISIBLE);
                constraintLayout_ty.setVisibility(View.GONE);

                // Обработка данных из спинера вид торговых условий
                Spinner_Select_TY();
            }
            break;
            case "random_ty": {
                Log.e("Error Spinner ", PEREM_PREFERENCE_TYPE_TY);
                spinner_ty.setVisibility(View.GONE);
                constraintLayout_ty.setVisibility(View.VISIBLE);
                text_no_ty.setVisibility(View.GONE);
                EditText_TY();
                if (!editText_skidka.getText().toString().isEmpty()) {
                    SQL_Data(editText_skidka.getText().toString(), summa_min_skidka);
                } else SQL_Data("0", summa_min_skidka);


                editText_skidka.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!editText_skidka.getText().toString().isEmpty()) {
                            SQL_Data(editText_skidka.getText().toString(), summa_min_skidka);
                        } else SQL_Data("0", summa_min_skidka);
                    }
                });
            }
            break;
            default: {
                Log.e("Error Spinner ", "Null");
                spinner_ty.setVisibility(View.VISIBLE);
                constraintLayout_ty.setVisibility(View.GONE);
                Spinner_Select_TY();
            }
            break;
        }


        // Обработка нажатия на: Дата поставки
        Clicked_Button_Data();

        // Обработка нажатия на: Вариант реализации
        nds_read = "0";
        Clicked_CBox();

        // Обработка данных из спинера вид оплаты
        Spinner_Type_Credit();

        // Обработка новых данных
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Toast.makeText(context_Activity, "условие" +visible_ty, Toast.LENGTH_SHORT).show();
                Log.e(Log_Text_Error, "условие" +visible_ty);*/
                try {
                    if (visible_ty == false & Double.valueOf(summa_read) > 0)  // Проверка условия скидка подхлдит и итого больше 0
                    {
                        if (s_credit.equals("Консигнация")) {
                            date_credite_read = editText_credite_date.getText().toString();
                        } else date_credite_read = "0";


                        if (!date_credite_read.equals("")) {
                            Write_Data(text_data.getText().toString(), nds_read, skidka_read, s_credit, summa_read, itogo_read, date_credite_read, editText_comment.getText().toString());
                            Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza_L2.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(context_Activity, "Введите кол-во дней конс", Toast.LENGTH_SHORT).show();
                            Log.e("Кнопка TY...", "Введите кол-во дней конс");
                        }


                    } else {
                        Toast.makeText(context_Activity, "эти условия не подходят!", Toast.LENGTH_SHORT).show();
                    }


                   /* if (Double.valueOf(summa_read) > 0) {

                    } else
                        Toast.makeText(context_Activity, "Ошибка выбора данных", Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(context_Activity, SPR_Strih_Kod_Search.class);
                    startActivity(intent);*/
                } catch (Exception e) {
                    Toast.makeText(context_Activity, "Ошибка записи данных", Toast.LENGTH_SHORT).show();
                    Log.e(Log_Text_Error, "Ошибка записи данных!");
                }


            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza_L2.class);
        startActivity(intent);
        finish();
    }

    // Обработка нажатия на: Дата поставки
    protected void Clicked_Button_Data() {
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH) + 1;
        c.set(mYear, mMonth, mDay);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String data_start = dateFormat.format(c.getTime());
        text_data.setText(data_start);

        btn_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context_Activity,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                                String data_up = dateFormat.format(calendar.getTime());
                                text_data.setText(data_up);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    // Обработка нажатия на: Вариант реализации
    protected void Clicked_CBox() {
        checkBox_nds = findViewById(R.id.checkBox_d);
        checkBox_nds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    nds_read = "1";
                } else nds_read = "0";
            }
        });
    }

    // Обработка данных из спинера вид оплаты
    protected void Spinner_Type_Credit() {
        try {
            mass_type = getResources().getStringArray(R.array.mass_select_credit);
            spinner_credit.clear();
            for (int i = 0; i < mass_type.length; i++) {
                spinner_credit.add(new ListAdapterSimple_Spinner_TY("", "", mass_type[i], "", "", ""));
            }
            adapterPriceClients_credit = new ListAdapterAde_Spinner_TY(context_Activity, spinner_credit);
            adapterPriceClients_credit.notifyDataSetChanged();
            spinner_type_credit.setAdapter(adapterPriceClients_credit);

            spinner_type_credit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView tvw_d_skidka = view.findViewById(R.id.tvw_ty_title1);
                    editText_credite_date.setText("");
                    s_credit = tvw_d_skidka.getText().toString();
                    Log.e("Type ", tvw_d_skidka.getText().toString());
                    if (tvw_d_skidka.getText().toString().equals("Консигнация")) {
                        editText_credite_date.setVisibility(View.VISIBLE);
                        Log.e("Type ", "Есть");

                    } else {

                        editText_credite_date.setVisibility(View.GONE);
                        Log.e("Type ", "Нет");
                    }

                    try {

                    } catch (Exception e) {
                        Log.e("Error Spinner ", "Ошибка данных!");
                        Toast.makeText(context_Activity, "Ошибка данных!", Toast.LENGTH_SHORT).show();
                    }

                }


                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (Exception e) {
            Log.e(this.getLocalClassName(), "Ошибка: нет данных по типу оплаты!");
            Toast.makeText(this, "Произошла ошибка!", Toast.LENGTH_LONG).show();
        }

    }

    // Обработка данных из спинера вид торговых условий
    protected void Spinner_TY() {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
            String query = "SELECT * FROM const_ty WHERE region = '" + PEREM_PREFERENCE_PUT_FTP + "'";
            final Cursor cursor = db.rawQuery(query, null);
            //mass_ty = new String[cursor.getCount()][3];
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    String region = cursor.getString(cursor.getColumnIndexOrThrow("region"));
                    String count_skidka = cursor.getString(cursor.getColumnIndexOrThrow("count_skidka"));
                    String type_skidka = cursor.getString(cursor.getColumnIndexOrThrow("type_skidka"));
                    String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                    String summa_min = cursor.getString(cursor.getColumnIndexOrThrow("summa_min"));
                    String kyrc = cursor.getString(cursor.getColumnIndexOrThrow("kyrc"));
                    spinner_ty_add.add(new ListAdapterSimple_Spinner_TY(count_skidka, summa_min, "ТУ=", type_skidka, status, kyrc));
                    // spinner_ty_add.add(new ListAdapterSimple_Spinner_TY(count_skidka, summa_min, "ТУ=", type_skidka, status, kyrc));
                    cursor.moveToNext();
                }
            } else Toast.makeText(context_Activity, "Нет данных по ТУ", Toast.LENGTH_SHORT).show();
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.e(this.getLocalClassName(), "Ошибка: нет данных по торговым условиям!");
            Toast.makeText(this, "Произошла ошибка!", Toast.LENGTH_LONG).show();
        }

    }

    // Обработка данных из спинера вид торговых условий
    protected void EditText_TY() {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
            String query = "SELECT * FROM const_ty WHERE region = '" + PEREM_PREFERENCE_PUT_FTP + "'";
            final Cursor cursor = db.rawQuery(query, null);
            //mass_ty = new String[cursor.getCount()][3];
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    String region = cursor.getString(cursor.getColumnIndexOrThrow(("region")));
                    String count_skidka = cursor.getString(cursor.getColumnIndexOrThrow(("count_skidka")));
                    String type_skidka = cursor.getString(cursor.getColumnIndexOrThrow(("type_skidka")));
                    String status = cursor.getString(cursor.getColumnIndexOrThrow(("status")));
                    String summa_min = cursor.getString(cursor.getColumnIndexOrThrow(("summa_min")));
                    String kyrc = cursor.getString(cursor.getColumnIndexOrThrow(("kyrc")));

                    Log.e("Error Spinner ", "C " + cursor.getPosition());

                    if (cursor.getPosition() == 2) {

                        summa_min_skidka = summa_min;
                        getSupportActionBar().setSubtitle("мин. сумма закупа: "+summa_min);
                    }
                    // spinner_ty_add.add(new ListAdapterSimple_Spinner_TY(count_skidka, summa_min, "ТУ=", type_skidka, status, kyrc));
                    cursor.moveToNext();
                }
            } else Toast.makeText(context_Activity, "Нет данных по ТУ", Toast.LENGTH_SHORT).show();
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.e(this.getLocalClassName(), "Ошибка: нет данных по торговым условиям!");
            Toast.makeText(this, "Произошла ошибка!", Toast.LENGTH_LONG).show();
        }

    }


    // Обработка данных из спинера вид торговых условий
    protected void Spinner_Select_TY() {
        try {
            spinner_ty_add.clear();
            Spinner_TY();
            adapterPriceClients_ty = new ListAdapterAde_Spinner_TY(context_Activity, spinner_ty_add);
            adapterPriceClients_ty.notifyDataSetChanged();
            spinner_ty.setAdapter(adapterPriceClients_ty);
            spinner_ty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        TextView tvw_d_skidka = view.findViewById(R.id.tvw_ty_skidka);
                        TextView tvw_d_summa = view.findViewById(R.id.tvw_ty_summa);
                        SQL_Data(tvw_d_skidka.getText().toString(), tvw_d_summa.getText().toString());
                    } catch (Exception e) {
                        Log.e("Error Spinner ", "Ошибка данных!");
                        Toast.makeText(context_Activity, "Ошибка данных!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (Exception e) {
            Log.e("Error Spinner ", "Ошибка данных!");
            Toast.makeText(context_Activity, "Ошибка данных!", Toast.LENGTH_SHORT).show();
        }

    }


    private void SQL_Data(String sp_skidka, String sp_summa) {
        Log.e("SQLData: ", sp_skidka + " " + sp_summa);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT base_RN.Kod_RN, SUM(base_RN.Summa) AS 'new_summa', " +
                "base_RN.Skidka, SUM(base_RN.Itogo) AS 'new_itogo', base_RN.aks_pref\n" +
                "FROM base_RN\n" +
                "WHERE base_RN.Kod_RN = '" + PEREM_K_AG_KodRN + "'";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String kod_rn = cursor.getString(cursor.getColumnIndexOrThrow("Kod_RN"));
        String summa = cursor.getString(cursor.getColumnIndexOrThrow("new_summa"));
        String skidka = cursor.getString(cursor.getColumnIndexOrThrow("Skidka"));
        String itogo = cursor.getString(cursor.getColumnIndexOrThrow("new_itogo"));
        String prefics = cursor.getString(cursor.getColumnIndexOrThrow("aks_pref"));

        Log.e("TAFS", "SQL_Data: "+summa+"__"+sp_summa );
        if (kod_rn != null & cursor.getCount() > 0) {
            summa_read = summa;
            text_summa.setText(summa);
            if (Double.valueOf(summa) >= Double.valueOf(sp_summa)) {
                text_no_ty.setVisibility(View.GONE);
                Double d_summa = Double.parseDouble(summa);
                Double d_skidka = Double.parseDouble(sp_skidka);
                String new_itogo = new DecimalFormat("#00.00").format(d_summa - (d_summa * (d_skidka / 100)));
                itogo_read = new_itogo.replaceAll(",", ".");
                text_itogo.setText(itogo_read);
                skidka_read = sp_skidka;
                visible_ty = false;
            } else {
                if (PEREM_PREFERENCE_TYPE_TY.equals("standart"))
                {
                    text_no_ty.setVisibility(View.VISIBLE);
                } else  text_no_ty.setVisibility(View.GONE);
                visible_ty = true;
                itogo_read = "0.0";
                text_itogo.setText(itogo_read);
                skidka_read = "0";
            }

        } else {
            summa_read = "0.0";
            itogo_read = "0.0";
            Toast.makeText(context_Activity, "Нет данных по ТУ", Toast.LENGTH_SHORT).show();
        }


        // Log.e("Сумма: ", summa_read + " " + itogo_read);
        cursor.close();
        db.close();
    }

    // Запись данных
    protected void Write_Data(String wr_data, String wr_nds, String wr_skidka, String wr_credit, String wr_summa, String wr_itogo, String wr_credite_date, String wr_comment) {
        ed = sp.edit();
        ed.putString("PEREM_DOP_DATA_UP", wr_data);
        ed.putString("PEREM_DOP_NDS", wr_nds);
        ed.putString("PEREM_DOP_SKIDKA", wr_skidka);
        ed.putString("PEREM_DOP_CREDIT", wr_credit);
        ed.putString("PEREM_DOP_SUMMA", wr_summa);
        ed.putString("PEREM_DOP_ITOGO", wr_itogo);
        ed.putString("PEREM_DOP_CREDITE_DATE", wr_credite_date);
        ed.putString("PEREM_DOP_COMMENT", wr_comment);
        ed.putString("PEREM_CLICK_TY", "true");
        ed.commit();

        Log.e("Дата поставки: ", wr_data);
        Log.e("НДС: ", wr_nds);
        Log.e("Скидка: ", wr_skidka);
        Log.e("Дней конс: ", wr_credite_date);
        Log.e("Оплата:", wr_credit);
        Log.e("Сумма: ", wr_summa);
        Log.e("Итого: ", wr_itogo);
        Log.e("Комментарий: ", wr_comment);
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

        PEREM_K_AG_NAME = sp.getString("PEREM_K_AG_NAME", "0");          //чтение данных: имя контраегнта
        PEREM_K_AG_UID = sp.getString("PEREM_K_AG_UID", "0");            //чтение данных: uid контрагента
        PEREM_K_AG_ADRESS = sp.getString("PEREM_K_AG_ADRESS", "0");      //чтение данных: адрес контрагент
        PEREM_K_AG_KodRN = sp.getString("PEREM_K_AG_KodRN", "0");         //чтение данных: код накладной
        PEREM_K_AG_Data = sp.getString("PEREM_K_AG_Data", "0");           //чтение данных: время создание н
        PEREM_K_AG_Data_WORK = sp.getString("PEREM_K_AG_Data_WORK", "0");           //чтение данных: время создание н
        PEREM_K_AG_Vrema = sp.getString("PEREM_K_AG_Vrema", "0");        //чтение данных: дата создание на
        PEREM_K_AG_GPS = sp.getString("PEREM_K_AG_GPS", "0");             //чтение данных: координаты gps

        PEREM_PREFERENCE_PUT_FTP = sp.getString("list_preference_ftp", "0");             //чтение данных: путь данных на ftp
        PEREM_PREFERENCE_TYPE_TY = sp.getString("preference_type_ty", "0");             //чтение данных: путь данных на ftp
    }

}
