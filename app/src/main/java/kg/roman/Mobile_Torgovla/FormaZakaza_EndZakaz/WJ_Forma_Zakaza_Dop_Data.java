package kg.roman.Mobile_Torgovla.FormaZakaza_EndZakaz;

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
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.appcompat.app.AppCompatActivity;

import org.checkerframework.checker.units.qual.C;

import kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar.Removable;
import kg.roman.Mobile_Torgovla.FormaZakazaStart.WJ_Forma_Zakaza;
import kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar.WJ_Forma_Zakaza_L2;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Spinner_TY;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Spinner_TY;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.CalendarThis;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.PreferencesWrite;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.Preferences_MTSetting;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.databinding.MtWjFormaDopDataBinding;

public class WJ_Forma_Zakaza_Dop_Data extends AppCompatActivity implements Removable, RemovableNextDate {


    public ArrayList<ListAdapterSimple_Spinner_TY> spinner_credit = new ArrayList<>();
    public ListAdapterAde_Spinner_TY adapterPriceClients_credit;

    ////////////  02.2024
    private MtWjFormaDopDataBinding binding;
    String logeTAG = "WjFormaDopUslov";
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";

    SharedPreferences mSettings, wSetting;
    SharedPreferences.Editor editor;

    public Context context_Activity;
    PreferencesWrite preferencesWrite;
    Preferences_MTSetting preferencesMtSetting;
    String statusRN = "null";
    String w_CodeOrder;


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
        preferencesMtSetting = new Preferences_MTSetting();

        //// имя контагента
        binding.tvwEZClientName.setText(preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getClientName()));
        w_CodeOrder = preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getCodeOrder());
        //// Торговые условия
        //// Выборка фрагмента в настройках вид торговых условий: (стандартный и только EditText)
        switch (preferencesWrite.Setting_TY_Type) {
            case "standart": {
                Log.e(logeTAG, "Стандартные условия");
                binding.fragmentSale.getRoot().setVisibility(View.VISIBLE);
                binding.fragmentSaleRandom.getRoot().setVisibility(View.GONE);
                FragmentSale();
            }
            break;
            case "random_ty": {
                Log.e(logeTAG, "Расширенные условия");
                binding.fragmentSale.getRoot().setVisibility(View.GONE);
                binding.fragmentSaleRandom.getRoot().setVisibility(View.VISIBLE);
                FragmentSaleRandom();
            }
            break;
        }
        //// Заполнить даными активность (Create или (Copy, Edit))
        if (preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getStatusOrder()).equals("Create"))
            writeForma_toCreator();
        else writeForma_toEditor();


        // Обработка нажатия на: Вариант реализации
        Clicked_CBox();

        // Обработка нажатия на: Дата поставки
        Clicked_Button_Data();

        // Обработка данных из спинера вид оплаты
        Spinner_Type_Credit();

        // Коментарий
        WriteComment();

        // Заполнение общей сумой
        AvtoSumma();


        binding.fabEndZakaz.setOnClickListener(v -> {
            double sumItogo = Double.parseDouble(binding.fragmentAvtoSumm.textViewThisItog.getText().toString());
            double minSum = Double.parseDouble(preferencesWrite.Setting_TY_MinSumma);
            if (sumItogo >= minSum) {
                Toast.makeText(context_Activity, "Конец заказа", Toast.LENGTH_SHORT).show();
                SQLiteWriteData();
            } else {
                Toast.makeText(context_Activity, "мин сумма заказа: " + minSum + " добавьте товар в заявку", Toast.LENGTH_LONG).show();
            }
        });

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
                    editor.putString("PEREM_K_AG_Sale", preferencesWrite.Setting_TY_Sale);    //тип условия
                    Log.e(logeTAG, "SelectrB: Radio1");

                    preferencesMtSetting.writeSettingString(context_Activity, preferencesMtSetting.getTYSelectRadioButton(), "standart");
                    preferencesMtSetting.writeSettingInt(context_Activity, preferencesMtSetting.getSaleCount(), Integer.parseInt(preferencesWrite.Setting_TY_Sale));
                }
                break;
                case R.id.radioButton2: {
                    binding.fragmentSale.editTextNumber2.setText(preferencesWrite.Setting_TY_SaleFarm);
                    binding.fragmentSale.editTextNumber2.setEnabled(false);
                    editor.putString("preference_TypeSale", "Farms");    //тип условия
                    editor.putString("PEREM_K_AG_Sale", preferencesWrite.Setting_TY_SaleFarm);    //тип условия
                    Log.e(logeTAG, "SelectrB: Radio2");
                    preferencesMtSetting.writeSettingString(context_Activity, preferencesMtSetting.getTYSelectRadioButton(), "Farms");
                    preferencesMtSetting.writeSettingInt(context_Activity, preferencesMtSetting.getSaleCount(), Integer.parseInt(preferencesWrite.Setting_TY_SaleFarm));
                }
                break;
                case R.id.radioButton3: {
                    binding.fragmentSale.editTextNumber2.setText("");
                    binding.fragmentSale.editTextNumber2.setEnabled(true);
                    editor.putString("preference_TypeSale", "Random");    //тип условия
                    Log.e(logeTAG, "SelectrB: Radio3");
                    preferencesMtSetting.writeSettingString(context_Activity, preferencesMtSetting.getTYSelectRadioButton(), "Random");
                    preferencesMtSetting.writeSettingInt(context_Activity, preferencesMtSetting.getSaleCount(), 0);
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

                    preferencesMtSetting.writeSettingInt(context_Activity, preferencesMtSetting.getSaleCount(), Integer.parseInt(s.toString()));
                } else {
                    editor.putInt("preference_SaleRandom", 0);    //запись данных даты и времени
                    preferencesMtSetting.writeSettingInt(context_Activity, preferencesMtSetting.getSaleCount(), 0);
                }

                editor.commit();
                AvtoSumma();
            }
        });

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
                if (!s.toString().equals(""))
                    preferencesMtSetting.writeSettingInt(context_Activity, preferencesMtSetting.getSaleCount(), Integer.parseInt(s.toString()));
                else {
                    binding.fragmentSaleRandom.editTextNumber2.setText("0");
                    preferencesMtSetting.writeSettingInt(context_Activity, preferencesMtSetting.getSaleCount(), 0);
                }


/*                editor = mSettings.edit();
                if (s.length() > 0) {
                    editor.putInt("preference_SaleRandom", Integer.parseInt(s.toString()));    //запись данных даты и времени

                } else {
                    editor.putInt("preference_SaleRandom", 0);    //запись данных даты и времени
                    preferencesMtSetting.writeSettingInt(context_Activity, preferencesMtSetting.getSaleCount(), 0);
                }

                editor.commit();*/

                AvtoSumma();
            }
        });
    }

    // Обработка нажатия на: Дата поставки
    protected void Clicked_Button_Data() {
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
        //binding.tvwDData.setText(data_start);

        binding.btnNextData.setOnClickListener(view -> {
            DialogFragment_NextDate dialog = new DialogFragment_NextDate();
            dialog.show(getSupportFragmentManager(), "customSelectNextDate");
        });


    }


    // Обработка нажатия на: Вариант реализации
    protected void Clicked_CBox() {
        binding.checkBoxType.setOnCheckedChangeListener((compoundButton, booleanClick) -> {
            int valueBoolean;
            if (booleanClick) valueBoolean = 1;
            else valueBoolean = 0;
            preferencesMtSetting.writeSettingInt(context_Activity, preferencesMtSetting.getTypeOrder(), valueBoolean);
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
                        TextView tvwTypePay = view.findViewById(R.id.tvw_ty_title1);
                        if (tvwTypePay.getText().toString().equals("Консигнация"))
                            binding.edtCrediteDate.setVisibility(View.VISIBLE);
                        else {
                            binding.edtCrediteDate.setVisibility(View.GONE);
                            preferencesMtSetting.writeSettingInt(context_Activity, preferencesMtSetting.getInfoOrderCreditCountDay(), 0);
                        }
                        preferencesMtSetting.writeSettingString(context_Activity, preferencesMtSetting.getInfoOrderCredit(), tvwTypePay.getText().toString());
                    } catch (Exception e) {
                        Log.e(logeTAG, "Ошибка, вид оплаты");
                        Toast.makeText(context_Activity, "Ошибка, вид оплаты", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            //// Вид оплаты
            binding.spinnerSelectCredit.setSelection(0);
            binding.edtCrediteDate.setVisibility(View.INVISIBLE);

            String statusPay = preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getInfoOrderCredit());
            int countDayPay = preferencesMtSetting.readSettingInt(context_Activity, preferencesMtSetting.getInfoOrderCreditCountDay());

            for (int i = 0; i < mass_type.length; i++) {
                if (mass_type[i].equals(statusPay))
                {
                    Log.e(logeTAG, "statusPay" + statusPay);
                    binding.spinnerSelectCredit.setSelection(i);
                    if (statusPay.equals("Консигнация")) {
                        binding.edtCrediteDate.setVisibility(View.VISIBLE);
                        binding.edtCrediteDate.setText(String.valueOf(countDayPay));
                    }
                }

            }


            if (binding.edtCrediteDate.getVisibility() == View.VISIBLE) {
                if (countDayPay > 0)
                    binding.edtCrediteDate.setText(String.valueOf(countDayPay));
                else binding.edtCrediteDate.setText(String.valueOf(0));
                Log.e(logeTAG, "КОНС");
            }

            binding.edtCrediteDate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().equals(""))
                        preferencesMtSetting.writeSettingInt(context_Activity,
                                preferencesMtSetting.getInfoOrderCreditCountDay(),
                                Integer.parseInt(s.toString()));
                    else {
                        binding.edtCrediteDate.setText("0");
                        preferencesMtSetting.writeSettingInt(context_Activity,
                                preferencesMtSetting.getInfoOrderCreditCountDay(), 0);

                    }
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
        binding.editComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                preferencesMtSetting.writeSettingString(context_Activity, preferencesMtSetting.getComment(), s.toString());
            }
        });
    }


    //// Автосумма заказа ///////////////////////////////
    protected void AvtoSumma() {
        binding.fragmentAvtoSumm.textViewThisSumma.setText("");
        binding.fragmentAvtoSumm.textViewThisCount.setText("");
        binding.fragmentAvtoSumm.textViewThisItog.setText("");

        Sales();

        preferencesMtSetting = new Preferences_MTSetting();
        for (Map.Entry<String, String> map : preferencesMtSetting.getAutoSum(context_Activity).entrySet()) {
            if (map.getKey().equals("sum"))
                binding.fragmentAvtoSumm.textViewThisSumma.setText(map.getValue());
            if (map.getKey().equals("count"))
                binding.fragmentAvtoSumm.textViewThisCount.setText(map.getValue());
            if (map.getKey().equals("itg"))
                binding.fragmentAvtoSumm.textViewThisItog.setText(map.getValue());
        }


/*        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db = context_Activity.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String querySumma = "SELECT Kod_RN, SUM(Summa) AS 'AvtoSum' FROM '" + SelectRN() + "' WHERE Kod_RN = '" + preferencesWrite.Setting_MT_K_AG_KodRN + "';";
        String queryCount = "SELECT Kod_RN, SUM(Kol) AS 'AvtoCount' FROM '" + SelectRN() + "' WHERE Kod_RN = '" + preferencesWrite.Setting_MT_K_AG_KodRN + "';";
        String queryItogo = "SELECT Kod_RN, SUM(Itogo) AS 'AvtoItogo' FROM '" + SelectRN() + "' WHERE Kod_RN = '" + preferencesWrite.Setting_MT_K_AG_KodRN + "';";
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
        editor.commit();*/
    }

    //// Перерасчет суммы отночительно скидок
    protected void Sales() {
        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db = context_Activity.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String querySumma = "SELECT Kod_RN, SUM(Itogo) AS 'AvtoSum' FROM '" + SelectRN() + "' WHERE Kod_RN = '" + w_CodeOrder + "';";
        Cursor cursor = db.rawQuery(querySumma, null);
        //// Подсчет общей суммы
        cursor.moveToFirst();
        if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndexOrThrow("AvtoSum")) != null) {
            String s = cursor.getString(cursor.getColumnIndexOrThrow("AvtoSum"));

            if (preferencesWrite.Setting_TY_SaleType.equals("Random"))
                SQLiteQuery(SelectTypeSale());
            else if (Double.parseDouble(s) >= Double.parseDouble(preferencesWrite.Setting_TY_SaleMinSumSale)) {
                SQLiteQuery(SelectTypeSale());
            } else SQLiteQuery(0);
        }
        cursor.close();


    }

    protected void SQLiteQuery(int SaleTY) {
        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db = context_Activity.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT * FROM '" + SelectRN() + "' WHERE Kod_RN = '" + w_CodeOrder + "';";
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
            db.update(SelectRN(), contentValues, "koduid = ? AND Kod_RN = ?",
                    new String[]{uid, w_CodeOrder});

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


    protected String SelectRN() {
        if (preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getStatusOrder()).equals("Edit"))
            return "base_RN_Edit";
        else return "base_RN";
    }

    // Запись данных
    protected void SQLiteWriteData() {
        preferencesMtSetting = new Preferences_MTSetting();
        Log.e("Дата поставки: ", preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getDateDelivery()));
        Log.e("НДС: ", "Счет-фактура: " + preferencesMtSetting.readSettingInt(context_Activity, preferencesMtSetting.getTypeOrder()));
        Log.e("Скидка: ", String.valueOf(preferencesMtSetting.readSettingInt(context_Activity, preferencesMtSetting.getSaleCount())));
        Log.e("Тип оплаты: ", preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getInfoOrderCredit()));
        Log.e("Дней конс: ", String.valueOf(preferencesMtSetting.readSettingInt(context_Activity, preferencesMtSetting.getInfoOrderCreditCountDay())));
        Log.e("Комментарий: ", preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getComment()));

        DialogFragment_EndZakaz dialog = new DialogFragment_EndZakaz();
        Bundle args = new Bundle();
        args.putString("client", preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getClientName()));
        args.putString("itogo", binding.fragmentAvtoSumm.textViewThisItog.getText().toString());
        args.putString("typeStatus", preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getStatusOrder()));
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "customEndZakaz");
    }


    @Override
    public void restartAdapter(boolean statusAdapter) {
        if (statusAdapter) {
            Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza.class);
            intent.putExtra("preferenceSave", "clear");
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void RemovableNextDate(String textDate) {
        Log.e("Remove", textDate);
        binding.tvwDData.setText(textDate);
        preferencesMtSetting.writeSettingString(context_Activity, preferencesMtSetting.getDateDelivery(), textDate);
    }


    //// Заполнение формы при создании нового заказа
    protected void writeForma_toCreator() {

        //// торговые условия
        preferencesWrite = new PreferencesWrite(context_Activity);
        preferencesMtSetting = new Preferences_MTSetting();
        Log.e(logeTAG, "Статус:" + preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getStatusOrder()));
        binding.fragmentSale.editTextNumber2.setText(""+preferencesMtSetting.readSettingInt(context_Activity, preferencesMtSetting.getSaleCount()));

        switch (preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getTYSelectRadioButton())) {
            case "standart": {
                binding.fragmentSale.radioButton.setChecked(true);
                binding.fragmentSale.radioButton2.setChecked(false);
                binding.fragmentSale.radioButton3.setChecked(false);
                binding.fragmentSale.editTextNumber2.setEnabled(false);

              //  binding.fragmentSale.editTextNumber2.setText("0");
              //  binding.fragmentSaleRandom.editTextNumber2.setText("0");
            }
            break;
            case "Farms": {
                binding.fragmentSale.radioButton.setChecked(false);
                binding.fragmentSale.radioButton2.setChecked(true);
                binding.fragmentSale.radioButton3.setChecked(false);
                binding.fragmentSale.editTextNumber2.setEnabled(false);
            }
            break;
            case "Random": {
                binding.fragmentSale.radioButton.setChecked(false);
                binding.fragmentSale.radioButton2.setChecked(false);
                binding.fragmentSale.radioButton3.setChecked(true);
                binding.fragmentSale.editTextNumber2.setEnabled(true);
            }
            break;
        }


        //// Счет-фактура
        if (preferencesMtSetting.readSettingInt(context_Activity, preferencesMtSetting.getTypeOrder()) == 0)
            binding.checkBoxType.setChecked(false);
        else binding.checkBoxType.setChecked(true);

        //// Дата поставки товара
        Log.e(logeTAG, "dayUp" + preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getDateDelivery()));
        binding.tvwDData.setText(preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getDateDelivery()));


/*        //// Вид оплаты
        binding.spinnerSelectCredit.setSelection(0);
        binding.edtCrediteDate.setVisibility(View.INVISIBLE);

        String[] mass_type = getResources().getStringArray(R.array.mass_select_credit);
        String statusPay = preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getInfoOrderCredit());
        int countDayPay = preferencesMtSetting.readSettingInt(context_Activity, preferencesMtSetting.getInfoOrderCreditCountDay());

        for (int i = 0; i < mass_type.length; i++) {
            if (mass_type[i].equals(statusPay))
            {
                Log.e(logeTAG, "statusPay" + statusPay);
                binding.spinnerSelectCredit.setSelection(i);
                if (statusPay.equals("Консигнация")) {
                    binding.edtCrediteDate.setVisibility(View.VISIBLE);
                    binding.edtCrediteDate.setText(String.valueOf(countDayPay));
                }
            }

        }*/

        //// Коментарий
        binding.editComment.setText(preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getComment()));
    }

    //// Заполнение формы при копировании или редактировании
    protected void writeForma_toEditor() {
        //// торговые условия
        preferencesWrite = new PreferencesWrite(context_Activity);
        preferencesMtSetting = new Preferences_MTSetting();
        Log.e(logeTAG, "Статус:" + preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getStatusOrder()));

        binding.fragmentSale.radioButton.setChecked(false);
        binding.fragmentSale.radioButton2.setChecked(false);
        binding.fragmentSale.radioButton3.setChecked(true);
        binding.fragmentSale.editTextNumber2.setEnabled(true);
        int countSale = preferencesMtSetting.readSettingInt(context_Activity, preferencesMtSetting.getSaleCount());
        int countDayPay = preferencesMtSetting.readSettingInt(context_Activity, preferencesMtSetting.getInfoOrderCreditCountDay());

        binding.edtCrediteDate.setText("0");
        binding.fragmentSale.editTextNumber2.setText("0");
        binding.fragmentSaleRandom.editTextNumber2.setText("0");

        binding.fragmentSale.editTextNumber2.setText(String.valueOf(countSale));

        //// Счет-фактура
        if (preferencesMtSetting.readSettingInt(context_Activity, preferencesMtSetting.getTypeOrder()) == 1)
            binding.checkBoxType.setChecked(true);
        else binding.checkBoxType.setChecked(false);

        //// Дата поставки товара
        binding.tvwDData.setText(preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getDateDelivery()));

        //// Вид оплаты
        String[] mass_type = getResources().getStringArray(R.array.mass_select_credit);
        binding.edtCrediteDate.setVisibility(View.INVISIBLE);
        String statusPay = preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getInfoOrderCredit());
        Log.e(logeTAG, "countDay: " + countDayPay);
        for (int i = 0; i < mass_type.length; i++) {
            Log.e(logeTAG, "credit: " + statusPay + "__" + mass_type[i]);
            if (mass_type[i].toString().equals(statusPay)) {
                Log.e(logeTAG, "position: " + i);
                binding.spinnerSelectCredit.setSelection(i);
            }

            if (statusPay.equals("Консигнация")) {
                binding.edtCrediteDate.setVisibility(View.VISIBLE);
                binding.edtCrediteDate.setText(String.valueOf(countDayPay));
            }

        }
        //// Коментарий
        //// Дата отгрузки: 01.04.2024; Cкидка: 4%;cmn_вторник
        binding.editComment.setText(preferencesMtSetting.readSettingString(context_Activity, preferencesMtSetting.getComment()));
    }
}





/*    protected void selectEditZakaz() {
        Log.e(logeTAG, "Параметры: " + statusRN);
        preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String queryAll = "SELECT * FROM base_RN_All WHERE Kod_RN = '" + preferencesWrite.Setting_MT_K_AG_KodRN + "';";
        Cursor cursor = db.rawQuery(queryAll, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String client = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_name"));
            String clientUID = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_uid"));
            String clientAdress = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_adress"));
            String data_xml = cursor.getString(cursor.getColumnIndexOrThrow("data_xml"));
            String credit = cursor.getString(cursor.getColumnIndexOrThrow("credit"));
            String sklad = cursor.getString(cursor.getColumnIndexOrThrow("sklad"));
            String sklad_uid = cursor.getString(cursor.getColumnIndexOrThrow("sklad_uid"));
            String cena_price = cursor.getString(cursor.getColumnIndexOrThrow("cena_price"));
            String credite_date = cursor.getString(cursor.getColumnIndexOrThrow("credite_date"));
            String skidka_title = cursor.getString(cursor.getColumnIndexOrThrow("skidka_title"));
            String coment = cursor.getString(cursor.getColumnIndexOrThrow("coment"));
            String uslov_nds = cursor.getString(cursor.getColumnIndexOrThrow("uslov_nds"));
            String data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
            String vrema = cursor.getString(cursor.getColumnIndexOrThrow("vrema"));
            String data_up = cursor.getString(cursor.getColumnIndexOrThrow("data_up"));
            String summa = cursor.getString(cursor.getColumnIndexOrThrow("summa"));
            String skidka = cursor.getString(cursor.getColumnIndexOrThrow("skidka"));
            String itogo = cursor.getString(cursor.getColumnIndexOrThrow("itogo"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            String debet_new = cursor.getString(cursor.getColumnIndexOrThrow("debet_new"));

            //// Контрагент
            binding.tvwEZClientName.setText(client);
            editor = mSettings.edit();
            editor.putString("PEREM_K_AG_NAME", client);
            editor.putString("PEREM_K_AG_UID", clientUID);
            editor.putString("PEREM_K_AG_ADRESS", clientAdress);
            editor.putString("setting_TY_DateNextUP", data_up);
            editor.putInt("setting_TY_TypeRelise", Integer.parseInt(uslov_nds));
            editor.putString("PEREM_K_AG_Sale", skidka_title);
            editor.putString("setting_TY_COMMENT", coment.substring(coment.indexOf("cmn_") + 4));
            editor.commit();

            //// торговые условия
            preferencesWrite = new PreferencesWrite(context_Activity);
            binding.fragmentSale.radioButton.setChecked(false);
            binding.fragmentSale.radioButton2.setChecked(false);
            binding.fragmentSale.radioButton3.setChecked(true);
            binding.fragmentSale.editTextNumber2.setEnabled(true);
            binding.fragmentSale.editTextNumber2.setText(preferencesWrite.Setting_MT_K_AG_Sale);

            //// Дата поставки товара
            binding.tvwDData.setText(preferencesWrite.Setting_TY_DateNextUP);

            //// Счет-фактура
            if (preferencesWrite.Setting_TY_TypeRelise == 1)
                binding.checkBoxType.setChecked(true);
            else binding.checkBoxType.setChecked(false);

            //// Вид оплаты
            String[] mass_type = getResources().getStringArray(R.array.mass_select_credit);
            binding.edtCrediteDate.setVisibility(View.INVISIBLE);

            for (int i = 0; i < mass_type.length; i++) {
                Log.e(logeTAG, "credit: " + credit + "__" + mass_type[i]);
                if (mass_type[i].toString().equals(credit)) {
                    Log.e(logeTAG, "position: " + i);
                    binding.spinnerSelectCredit.setSelection(i);
                }


                if (credit.equals("Консигнация")) {
                    binding.edtCrediteDate.setVisibility(View.VISIBLE);
                    binding.edtCrediteDate.setText(credite_date);
                }

            }
            //// Коментарий
            //// Дата отгрузки: 01.04.2024; Cкидка: 4%;cmn_вторник
            binding.editComment.setText(preferencesWrite.Setting_TY_Comment);
            cursor.moveToNext();
        }
        Log.e(logeTAG, "Создана копия таблици позиций заказа");
        cursor.close();
    }*/
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