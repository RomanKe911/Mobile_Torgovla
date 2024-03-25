package kg.roman.Mobile_Torgovla.FormaZakaza_EndZakaz;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.appcompat.app.AppCompatActivity;

import kg.roman.Mobile_Torgovla.FormaZakazaStart.DialogFragment_FilterToDate;
import kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar.Removable;
import kg.roman.Mobile_Torgovla.FormaZakazaStart.WJ_Forma_Zakaza;
import kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar.WJ_Forma_Zakaza_L2;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Spinner_TY;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Spinner_TY;
import kg.roman.Mobile_Torgovla.MT_FTP.PreferencesWrite;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.databinding.MtWjFormaDopDataBinding;

public class WJ_Forma_Zakaza_Dop_Data extends AppCompatActivity implements Removable {


    public ArrayList<ListAdapterSimple_Spinner_TY> spinner_credit = new ArrayList<>();
    public ListAdapterAde_Spinner_TY adapterPriceClients_credit;

    ////////////  02.2024
    private MtWjFormaDopDataBinding binding;
    String logeTAG = "WjFormaDopUslov";
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;

    public Context context_Activity;
    PreferencesWrite preferencesWrite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MtWjFormaDopDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // setContentView(R.layout.mt_wj_forma_dop_data);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.icon_toolbar_forma_zakaz);
        getSupportActionBar().setTitle("Торговые условия");
        getSupportActionBar().setSubtitle("");

        mSettings = getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        context_Activity = getApplication().getBaseContext();

        preferencesWrite = new PreferencesWrite(context_Activity);

        binding.tvwEZClientName.setText(preferencesWrite.Setting_MT_K_AG_NAME);

        //// Заполнение общей сумой
        AvtoSumma();

        // Обработка нажатия на: Дата поставки
        Clicked_Button_Data();

        // Обработка нажатия на: Вариант реализации
        Clicked_CBox();

        // Коментарий
        WriteComment();


        binding.fabEndZakaz.setOnClickListener(v -> {


            double sumItogo = Double.parseDouble(preferencesWrite.Setting_TY_Itogo);
            double minSum = Double.parseDouble(preferencesWrite.Setting_TY_MinSumma);
            if (sumItogo >= minSum) {
                Toast.makeText(context_Activity, "Конец заказа", Toast.LENGTH_SHORT).show();
                SQLiteWriteData();
            } else {
                Toast.makeText(context_Activity, "мин сумма заказа: " + minSum + " добавьте товар в заявку", Toast.LENGTH_LONG).show();
            }


/*            try {
                if (visible_ty == false & Double.valueOf(summa_read) > 0)  // Проверка условия скидка подхлдит и итого больше 0
                {
                    if (s_credit.equals("Консигнация")) {
                        date_credite_read = editText_credite_date.getText().toString();
                    } else date_credite_read = "0";


                    if (!date_credite_read.equals("")) {
                        //  Write_Data(text_data.getText().toString(), nds_read, skidka_read, s_credit, summa_read, itogo_read, date_credite_read, editText_comment.getText().toString());
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
            } catch (Exception e) {
                Toast.makeText(context_Activity, "Ошибка записи данных", Toast.LENGTH_SHORT).show();
                Log.e(logeTAG, "Ошибка записи данных!");
            }*/




                   /* if (Double.valueOf(summa_read) > 0) {

                    } else
                        Toast.makeText(context_Activity, "Ошибка выбора данных", Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(context_Activity, SPR_Strih_Kod_Search.class);
                    startActivity(intent);*/
        });


        // Обработка данных вид торговых условий
        switch (preferencesWrite.Setting_TY_Type) {
            case "standart": {
                Log.e(logeTAG, "Стандартные условия");
                binding.fragmentSale.getRoot().setVisibility(View.VISIBLE);
                binding.fragmentSaleRandom.getRoot().setVisibility(View.GONE);
                FragmentSale();
            }
            break;
            case "random_ty": {
                binding.fragmentSale.getRoot().setVisibility(View.GONE);
                binding.fragmentSaleRandom.getRoot().setVisibility(View.VISIBLE);
                FragmentSaleRandom();
            }
            break;
        }


        // Обработка данных из спинера вид оплаты
        Spinner_Type_Credit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza_L2.class);
        startActivity(intent);
        finish();
    }

    //// Работа с фрагментом: стандартные условия
    protected void FragmentSale() {
        preferencesWrite = new PreferencesWrite(context_Activity);
        binding.fragmentSale.tvwFragmentSaleMinSum.setText(preferencesWrite.Setting_TY_SaleMinSum + "c");
        binding.fragmentSale.tvwFragmentSaleMinSumSale.setText(preferencesWrite.Setting_TY_SaleMinSumSale + "c");
        binding.fragmentSale.editTextNumber2.setText(preferencesWrite.Setting_TY_Sale);
        binding.fragmentSale.editTextNumber2.setEnabled(false);

        switch (preferencesWrite.Setting_TY_SaleType) {
            case "standart":
                binding.fragmentSale.radioButton.setChecked(true);
                binding.fragmentSale.editTextNumber2.setEnabled(false);
                binding.fragmentSale.editTextNumber2.setText(preferencesWrite.Setting_TY_Sale);
                break;
            case "Farms":
                binding.fragmentSale.radioButton2.setChecked(true);
                binding.fragmentSale.editTextNumber2.setEnabled(false);
                binding.fragmentSale.editTextNumber2.setText(preferencesWrite.Setting_TY_SaleFarm);
                break;
            case "Random": {
                binding.fragmentSale.radioButton3.setChecked(true);
                binding.fragmentSale.editTextNumber2.setEnabled(true);
                binding.fragmentSale.editTextNumber2.setText(String.valueOf(preferencesWrite.Setting_TY_SaleRandom));
            }

            break;
        }

        binding.fragmentSale.radioGroupTY.setOnCheckedChangeListener((group, checkedId) -> {
            editor = mSettings.edit();
            switch (checkedId) {
                case R.id.radioButton: {
                    binding.fragmentSale.editTextNumber2.setText(preferencesWrite.Setting_TY_Sale);
                    binding.fragmentSale.editTextNumber2.setEnabled(false);
                    editor.putString("preference_TypeSale", "standart");    //тип условия
                    Log.e(logeTAG, "SelectrB: Radio1");
                }
                break;
                case R.id.radioButton2: {
                    binding.fragmentSale.editTextNumber2.setText(preferencesWrite.Setting_TY_SaleFarm);
                    binding.fragmentSale.editTextNumber2.setEnabled(false);
                    editor.putString("preference_TypeSale", "Farms");    //тип условия
                    Log.e(logeTAG, "SelectrB: Radio2");
                }
                break;
                case R.id.radioButton3: {
                    binding.fragmentSale.editTextNumber2.setText("");
                    binding.fragmentSale.editTextNumber2.setEnabled(true);
                    editor.putString("preference_TypeSale", "Random");    //тип условия
                    Log.e(logeTAG, "SelectrB: Radio3");
                }
                break;
            }
            editor.commit();
            AvtoSumma();
        });

        binding.fragmentSale.editTextNumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editor = mSettings.edit();
                if (s.length() > 0) {
                    editor.putInt("preference_SaleRandom", Integer.parseInt(s.toString()));    //запись данных даты и времени
                    Log.e(logeTAG, "_" + Integer.parseInt(s.toString()));
                } else {
                    editor.putInt("preference_SaleRandom", 0);    //запись данных даты и времени
                    Log.e(logeTAG, "null");
                }

                editor.commit();
                AvtoSumma();
            }
        });

/*        binding.fragmentSale.radioButton.setOnClickListener(v -> {
            Toast.makeText(context_Activity, binding.fragmentSale.radioButton.getText(), Toast.LENGTH_SHORT).show();
            binding.fragmentSale.editTextNumber2.setText(preferencesWrite.Setting_TY_Sale);
            binding.fragmentSale.editTextNumber2.setEnabled(false);
            binding.fragmentSale.radioButton2.setChecked(false);
            binding.fragmentSale.radioButton3.setChecked(false);
        });
        binding.fragmentSale.radioButton2.setOnClickListener(v -> {
            Toast.makeText(context_Activity, binding.fragmentSale.radioButton2.getText(), Toast.LENGTH_SHORT).show();
            binding.fragmentSale.editTextNumber2.setText(preferencesWrite.Setting_TY_SaleFarm);
            binding.fragmentSale.editTextNumber2.setEnabled(false);
            binding.fragmentSale.radioButton.setChecked(false);
            binding.fragmentSale.radioButton3.setChecked(false);
        });
        binding.fragmentSale.radioButton3.setOnClickListener(v -> {
            Toast.makeText(context_Activity, binding.fragmentSale.radioButton3.getText(), Toast.LENGTH_SHORT).show();
            binding.fragmentSale.editTextNumber2.setText("");
            binding.fragmentSale.editTextNumber2.setEnabled(true);
            binding.fragmentSale.radioButton.setChecked(false);
            binding.fragmentSale.radioButton2.setChecked(false);
        });*/

    }

    //// Работа с фрагментом: расшиенные условия
    protected void FragmentSaleRandom() {
        preferencesWrite = new PreferencesWrite(context_Activity);
        binding.fragmentSale.tvwFragmentSaleMinSum.setText(preferencesWrite.Setting_TY_SaleMinSum + "c");
        binding.fragmentSale.tvwFragmentSaleMinSumSale.setText(preferencesWrite.Setting_TY_SaleMinSumSale + "c");
        binding.fragmentSale.editTextNumber2.setText("");
        editor = mSettings.edit();
        editor.putString("preference_TypeSale", "Random");    //тип условия
        editor.commit();
        binding.fragmentSaleRandom.editTextNumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editor = mSettings.edit();
                if (s.length() > 0) {
                    editor.putInt("preference_SaleRandom", Integer.parseInt(s.toString()));    //запись данных даты и времени
                    Log.e(logeTAG, "_" + Integer.parseInt(s.toString()));
                } else {
                    editor.putInt("preference_SaleRandom", 0);    //запись данных даты и времени
                    Log.e(logeTAG, "null");
                }
                AvtoSumma();

                editor.commit();


            }
        });
    }


    // Обработка нажатия на: Дата поставки
    protected void Clicked_Button_Data() {
        editor = mSettings.edit();
        Calendar c = Calendar.getInstance();
        AtomicInteger mYear = new AtomicInteger();
        AtomicInteger mMonth = new AtomicInteger();
        AtomicInteger mDay = new AtomicInteger();
        mYear.set(c.get(Calendar.YEAR));
        mMonth.set(c.get(Calendar.MONTH));
        mDay.set(c.get(Calendar.DAY_OF_MONTH) + 1);
        c.set(mYear.get(), mMonth.get(), mDay.get());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String data_start = dateFormat.format(c.getTime());
        binding.tvwDData.setText(data_start);

        editor.putString("setting_TY_DateNextUP", data_start);
        editor.commit();



/*        Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

*//*                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
                    String data_up = dateFormat1.format(myCalendar.getTime());
                    binding.tvwDData.setText(data_up);

                    editor.putString("setting_TY_DateNextUP", data_up);
                    editor.commit();*//*
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(context_Activity, R.style.AppTheme, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));*/

        binding.btnDData.setOnClickListener(view -> {
            try {

                mYear.set(c.get(Calendar.YEAR));
                mMonth.set(c.get(Calendar.MONTH));
                mDay.set(c.get(Calendar.DAY_OF_MONTH));
                DatePickerDialog datePickerDialog = new DatePickerDialog(context_Activity,
                        (view1, year, monthOfYear, dayOfMonth) -> {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, monthOfYear, dayOfMonth);
                            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
                            String data_up = dateFormat1.format(calendar.getTime());
                            binding.tvwDData.setText(data_up);

                            editor.putString("setting_TY_DateNextUP", data_up);
                            editor.commit();
                        }, mYear.get(), mMonth.get(), mDay.get());
                datePickerDialog.show();

            } catch (Exception e) {
                Log.e(logeTAG, "Error: " + e);
                Toast.makeText(context_Activity, "Error: " + e, Toast.LENGTH_SHORT).show();
            }


        });


    }

    // Обработка нажатия на: Вариант реализации
    protected void Clicked_CBox() {
        binding.checkBoxType.setOnCheckedChangeListener((compoundButton, booleanClick) -> {
            int valueBoolean = 0;
            if (booleanClick) valueBoolean = 1;
            else valueBoolean = 0;
            editor = mSettings.edit();
            editor.remove("setting_TY_TypeRelise");
            editor.putInt("setting_TY_TypeRelise", valueBoolean);
            editor.commit();
        });
    }

    // Обработка данных из спинера вид оплаты
    protected void Spinner_Type_Credit() {
        try {
            String[] mass_type = getResources().getStringArray(R.array.mass_select_credit);
            spinner_credit.clear();
            for (String type : mass_type)
                spinner_credit.add(new ListAdapterSimple_Spinner_TY("", "", type, "", "", ""));

            adapterPriceClients_credit = new ListAdapterAde_Spinner_TY(context_Activity, spinner_credit);
            adapterPriceClients_credit.notifyDataSetChanged();
            binding.spinnerSelectCredit.setAdapter(adapterPriceClients_credit);

            binding.spinnerSelectCredit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        editor = mSettings.edit();
                        TextView tvw_d_skidka = view.findViewById(R.id.tvw_ty_title1);
                        binding.edtCrediteDate.setText("");
                        String s_credit = tvw_d_skidka.getText().toString();
                        Log.e("Type ", tvw_d_skidka.getText().toString());
                        if (tvw_d_skidka.getText().toString().equals("Консигнация"))
                            binding.edtCrediteDate.setVisibility(View.VISIBLE);
                        else
                            binding.edtCrediteDate.setVisibility(View.GONE);


                        editor.putString("setting_TY_CREDIT", s_credit);
                        editor.commit();
                    } catch (
                            Exception e) {
                        Log.e(logeTAG, "Ошибка, вид оплаты");
                        Toast.makeText(context_Activity, "Ошибка, вид оплаты", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            binding.edtCrediteDate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    editor = mSettings.edit();
                    if (binding.edtCrediteDate.getVisibility() == View.VISIBLE)
                        editor.putString("setting_TY_CREDITE_DATE", binding.edtCrediteDate.getText().toString());
                    else
                        editor.putString("setting_TY_CREDITE_DATE", "0");
                    editor.commit();

                }
            });
        } catch (
                Exception e) {
            Log.e(logeTAG, "Ошибка: нет данных по типу оплаты!");
            Toast.makeText(context_Activity, "Произошла ошибка!", Toast.LENGTH_SHORT).show();
        }

    }

    //// Запись коментарий в память
    protected void WriteComment() {
        StringBuilder stringBuilder = new StringBuilder();
        binding.editComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                stringBuilder.append(s.toString());

            }
        });
        editor = mSettings.edit();
        editor.putString("setting_TY_COMMENT", stringBuilder.toString());
        editor.commit();
    }


    //// Автосумма заказа ///////////////////////////////
    protected void AvtoSumma() {
        binding.fragmentAvtoSumm.textViewThisSumma.setText("");
        binding.fragmentAvtoSumm.textViewThisCount.setText("");
        binding.fragmentAvtoSumm.textViewThisItog.setText("");

        Sales();

        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db = context_Activity.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String querySumma = "SELECT Kod_RN, SUM(Summa) AS 'AvtoSum' FROM base_RN WHERE Kod_RN = '" + preferencesWrite.Setting_MT_K_AG_KodRN + "';";
        String queryCount = "SELECT Kod_RN, SUM(Kol) AS 'AvtoCount' FROM base_RN WHERE Kod_RN = '" + preferencesWrite.Setting_MT_K_AG_KodRN + "';";
        String queryItogo = "SELECT Kod_RN, SUM(Itogo) AS 'AvtoItogo' FROM base_RN WHERE Kod_RN = '" + preferencesWrite.Setting_MT_K_AG_KodRN + "';";
        Cursor cursor = db.rawQuery(querySumma, null);
        //// Подсчет общей суммы
        cursor.moveToFirst();
        Log.e(logeTAG, "count: " + cursor.getCount());
        if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndexOrThrow("AvtoSum")) != null) {
            String s = cursor.getString(cursor.getColumnIndexOrThrow("AvtoSum"));
            binding.fragmentAvtoSumm.textViewThisSumma.setText(String.valueOf(Double.parseDouble(s)));
        } else binding.fragmentAvtoSumm.textViewThisSumma.setText("00.00");
        cursor.close();

        //// Подсчет общего кол-ва
        cursor = db.rawQuery(queryCount, null);
        cursor.moveToFirst();
        Log.e(logeTAG, "count: " + cursor.getCount());
        if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndexOrThrow("AvtoCount")) != null) {
            binding.fragmentAvtoSumm.textViewThisCount.setText(cursor.getString(cursor.getColumnIndexOrThrow("AvtoCount")) + " шт");
        } else binding.fragmentAvtoSumm.textViewThisCount.setText("0");

        //// Подсчет общего суммы со скидкой
        cursor = db.rawQuery(queryItogo, null);
        cursor.moveToFirst();
        editor = mSettings.edit();
        Log.e(logeTAG, "countIt: " + cursor.getCount());
        if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndexOrThrow("AvtoItogo")) != null) {
            String s = cursor.getString(cursor.getColumnIndexOrThrow("AvtoItogo"));
            binding.fragmentAvtoSumm.textViewThisItog.setText(String.valueOf(Double.parseDouble(s)));
        } else binding.fragmentAvtoSumm.textViewThisItog.setText("00.00");
        editor.putString("setting_EndZakaz_Itogo", binding.fragmentAvtoSumm.textViewThisItog.getText().toString());    //тип условия

        cursor.close();
        db.close();
        editor.commit();
    }

    //// Перерасчет суммы отночительно скидок
    protected void Sales() {
        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db = context_Activity.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String querySumma = "SELECT Kod_RN, SUM(Itogo) AS 'AvtoSum' FROM base_RN WHERE Kod_RN = '" + preferencesWrite.Setting_MT_K_AG_KodRN + "';";
        Cursor cursor = db.rawQuery(querySumma, null);
        //// Подсчет общей суммы
        cursor.moveToFirst();
        if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndexOrThrow("AvtoSum")) != null) {
            String s = cursor.getString(cursor.getColumnIndexOrThrow("AvtoSum"));
            if (Double.parseDouble(s) >= Double.parseDouble(preferencesWrite.Setting_TY_SaleMinSumSale)) {
                SQLiteQuery(SelectTypeSale());
            } else SQLiteQuery(0);
        }
        cursor.close();


    }

    protected void SQLiteQuery(int SaleTY) {
        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db = context_Activity.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT * FROM base_RN WHERE base_RN.Kod_RN = '" + preferencesWrite.Setting_MT_K_AG_KodRN + "';";
        ContentValues contentValues = new ContentValues();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String uid = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));
            String price = cursor.getString(cursor.getColumnIndexOrThrow("Cena"));
            String summa = cursor.getString(cursor.getColumnIndexOrThrow("Summa"));
            double newPrice, newItogo;
            if (SaleTY > 0) {
                newPrice = Double.parseDouble(price) - (Double.parseDouble(price) * ((double) SaleTY / 100));
                newItogo = Double.parseDouble(summa) - (Double.parseDouble(summa) * ((double) SaleTY / 100));
                contentValues.put("Skidka", SaleTY);
                contentValues.put("Cena_SK", formatDoubleToString(newPrice));
                contentValues.put("Itogo", formatDoubleToString(newItogo));
            } else {
                contentValues.put("Skidka", SaleTY);
                contentValues.put("Cena_SK", formatDoubleToString(Double.parseDouble(price)));
                contentValues.put("Itogo", formatDoubleToString(Double.parseDouble(summa)));
            }
            db.update("base_RN", contentValues, "koduid = ? AND Kod_RN = ?",
                    new String[]{uid, preferencesWrite.Setting_MT_K_AG_KodRN});

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    protected String formatDoubleToString(double text) {
        return new DecimalFormat("#00.00").format(text).replaceAll(",", ".");
    }

    protected int SelectTypeSale() {
        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        int sale = 0;
        switch (preferencesWrite.Setting_TY_SaleType) {
            case "standart":
                sale = Integer.parseInt(preferencesWrite.Setting_TY_Sale);
                break;
            case "Farms":
                sale = Integer.parseInt(preferencesWrite.Setting_TY_SaleFarm);
                break;
            case "Random":
                sale = preferencesWrite.Setting_TY_SaleRandom;
                break;
        }
        return sale;
    }
    /////////////////////////////////////////////////////


    // Запись данных
    protected void SQLiteWriteData() {
        // String wr_data, String wr_nds, String wr_skidka, String wr_credit, String wr_summa, String wr_itogo, String wr_credite_date, String wr_comment
        preferencesWrite = new PreferencesWrite(context_Activity);
        Log.e("Дата поставки: ", preferencesWrite.Setting_TY_DateNextUP);
        Log.e("НДС: ", "Счет-фактура: " + preferencesWrite.Setting_TY_TypeRelise);
        Log.e("Скидка: ", preferencesWrite.Setting_TY_Sale);
        Log.e("Тип оплаты: ", preferencesWrite.Setting_TY_CREDIT);
        Log.e("Дней конс: ", preferencesWrite.Setting_TY_CREDITE_DATE);
        Log.e("Комментарий: ", preferencesWrite.Setting_TY_Comment);

        // Log.e("Сумма: ", wr_summa);
        //   Log.e("Итого: ", wr_itogo);


/*
        mSettings = getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        editor = mSettings.edit();
        editor.putString("PEREM_DOP_DATA_UP", wr_data);
        editor.putString("PEREM_DOP_NDS", wr_nds);
        editor.putString("PEREM_DOP_SKIDKA", wr_skidka);
        editor.putString("PEREM_DOP_CREDIT", wr_credit);
        editor.putString("PEREM_DOP_SUMMA", wr_summa);
        editor.putString("PEREM_DOP_ITOGO", wr_itogo);
        editor.putString("PEREM_DOP_CREDITE_DATE", wr_credite_date);
        editor.putString("PEREM_DOP_COMMENT", wr_comment);
        editor.putString("PEREM_CLICK_TY", "true");
        editor.commit();*/

/*

        PEREM_CLICK_TY = sp.getString("PEREM_CLICK_TY", "0");                    //чтение данных: имя сервера
        Log.e("RRR", PEREM_CLICK_TY);

        if (!textView_Summa.getText().toString().equals("") & (PEREM_CLICK_TY.equals("true"))) {


        } else {
            Loading_Adapter_Refresh();
            Log.e("Кнопка Add...", "Ошибка: Не заполнен заказ или не выбраны условия!");
            Toast.makeText(context_Activity, "Ошибка: Не заполнен заказ или не выбраны условия!", Toast.LENGTH_SHORT).show();
        }*/

        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        DialogFragment_EndZakaz dialog = new DialogFragment_EndZakaz();
        Bundle args = new Bundle();
        args.putString("client", preferencesWrite.Setting_MT_K_AG_NAME);
        args.putString("itogo", preferencesWrite.Setting_TY_Itogo);
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "customEndZakaz");


    }


    @Override
    public void restartAdapter(boolean statusAdapter) {
        if (statusAdapter) {
            Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza.class);
            startActivity(intent);
            finish();
        }
    }


}