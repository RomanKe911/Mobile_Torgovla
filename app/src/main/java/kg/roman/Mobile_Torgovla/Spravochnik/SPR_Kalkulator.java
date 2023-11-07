package kg.roman.Mobile_Torgovla.Spravochnik;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dantsu.printerthermal_escpos_bluetooth.Printer;
import com.dantsu.printerthermal_escpos_bluetooth.bluetooth.BluetoothPrinters;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import kg.roman.Mobile_Torgovla.R;

public class SPR_Kalkulator extends AppCompatActivity {

    public EditText
            editText_5000, editText_2000, editText_1000, editText_500,
            editText_200, editText_100, editText_50, editText_20;
    public TextView textView_S5000, textView_S2000, textView_S1000, textView_S500,
            textView_S200, textView_S100, textView_S50, textView_S20;

    public EditText editText_10, editText_5, editText_3, editText_1;
    public TextView textView_S10, textView_S5, textView_S3, textView_S1;
    public TextView AutSum;
    public Integer val1 = 20, val2 = 50, val3 = 100, val4 = 200, val5 = 500, val6 = 1000, val7 = 2000, val8 = 5000, val9 = 1, val10 = 3, val11 = 5, val12 = 10;
    public Integer sum1, sum2, sum3, sum4, sum5, sum6, sum7, sum8, sum9, sum10, sum11, sum12, summa;
    private Handler mHandler = new Handler();
    public Button button_clear, button_printer, btn_meloch;
    public TextView tvw_data_start, tvw_data_end;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now, this_data_work_now, this_vrema_work_now;
    Calendar dateAndTime = Calendar.getInstance();
    public ConstraintLayout meloch;
    public int k = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt_spr_kalkulator);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Калькулятор");
        getSupportActionBar().setIcon(R.drawable.calculator_accessories);
        Load_findViewById();


        btn_meloch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_meloch.isClickable() & k == 0) {
                    k++;
                    meloch.setVisibility(View.VISIBLE);
                    Summing();
                  //  Visible_Meloch();
                } else {
                    k = 0;
                    meloch.setVisibility(View.GONE);
                    ClearingMeloch();
                    sum9=0; sum10=0;sum11=0;sum12=0;
                   Summing();


                }

            }
        });

        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Clearing();
            }
        });

        button_printer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(SPR_Kalkulator.this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SPR_Kalkulator.this, new String[]{Manifest.permission.BLUETOOTH}, SPR_Printer.PERMISSION_BLUETOOTH);
                } else {
                    Printer printer = new Printer(BluetoothPrinters.selectFirstPairedBluetoothPrinter(), 100, 16f, 16);
                    Calendate_New();
                    printer.printFormattedText("[C] Дата:" + this_data_now + "\n" +
                            "[L] 5000 - " + editText_5000.getText() + "\n" +
                            "[L] 2000 - " + editText_2000.getText().toString() + "\n" +
                            "[L] 1000 - " + editText_1000.getText().toString() + "\n" +
                            "[L]  500 - " + editText_500.getText().toString() + "\n" +
                            "[L]  200 - " + editText_200.getText().toString() + "\n" +
                            "[L]  100 - " + editText_100.getText().toString() + "\n" +
                            "[L]   50 - " + editText_50.getText().toString() + "\n" +
                            "[L]   20 - " + editText_20.getText().toString() + "\n" +
                            "[L] Итого: " + AutSum.getText().toString()
                    );

                    Log.e("Print", "[C]" + this_data_now + "\n" +
                            "[L] 5000 - " + editText_5000.getText() + "\n" +
                            "[L] 2000 - " + editText_2000.getText().toString() + "\n" +
                            "[L] 1000 - " + editText_1000.getText().toString() + "\n" +
                            "[L]  500 - " + editText_500.getText().toString() + "\n" +
                            "[L]  200 - " + editText_200.getText().toString() + "\n" +
                            "[L]  100 - " + editText_100.getText().toString() + "\n" +
                            "[L]   50 - " + editText_50.getText().toString() + "\n" +
                            "[L]   20 - " + editText_20.getText().toString() + "\n" +
                            "[L] Итого: " + AutSum.getText().toString());

                }


            }
        });

        editText_5000.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        editText_5000.getApplicationWindowToken(), InputMethodManager.SHOW_IMPLICIT, 0);
                editText_5000.requestFocus();
                showInputMethod();

            }
        });


        Clearing();
        editText_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Summing();
            }
        });
        editText_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Summing();
            }
        });
        editText_5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Summing();
            }
        });
        editText_10.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Summing();
            }
        });
        editText_20.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Summing();
            }
        });
        editText_50.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Summing();
            }
        });
        editText_100.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Summing();
            }
        });
        editText_200.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Summing();
            }
        });
        editText_500.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Summing();
            }
        });
        editText_1000.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Summing();
            }
        });
        editText_2000.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Summing();
            }
        });
        editText_5000.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Summing();
            }
        });
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

    protected void Summing() {
        if (!editText_1.getText().toString().isEmpty()) {
            sum9 = val9 * Integer.parseInt(editText_1.getText().toString());
            String Format = new DecimalFormat("#00.0").format(sum9);
            textView_S1.setText(Format.toString());
        } else {
            sum9 = 0;
            String Format = new DecimalFormat("#00.0").format(sum9);
            textView_S1.setText(Format.toString());
        }
        if (!editText_3.getText().toString().isEmpty()) {
            sum10 = val10 * Integer.parseInt(editText_3.getText().toString());
            String Format = new DecimalFormat("#00.0").format(sum10);
            textView_S3.setText(Format.toString());
        } else {
            sum10 = 0;
            String Format = new DecimalFormat("#00.0").format(sum10);
            textView_S3.setText(Format.toString());
        }
        if (!editText_5.getText().toString().isEmpty()) {
            sum11 = val11 * Integer.parseInt(editText_5.getText().toString());
            String Format = new DecimalFormat("#00.0").format(sum11);
            textView_S5.setText(Format.toString());
        } else {
            sum11 = 0;
            String Format = new DecimalFormat("#00.0").format(sum11);
            textView_S5.setText(Format.toString());
        }
        if (!editText_10.getText().toString().isEmpty()) {
            sum12 = val12 * Integer.parseInt(editText_10.getText().toString());
            String Format = new DecimalFormat("#00.0").format(sum12);
            textView_S10.setText(Format.toString());
        } else {
            sum12 = 0;
            String Format = new DecimalFormat("#00.0").format(sum12);
            textView_S10.setText(Format.toString());
        }
        if (!editText_20.getText().toString().isEmpty()) {
            sum1 = val1 * Integer.parseInt(editText_20.getText().toString());
            String Format = new DecimalFormat("#00.0").format(sum1);
            textView_S20.setText(Format.toString());
        } else {
            sum1 = 0;
            String Format = new DecimalFormat("#00.0").format(sum1);
            textView_S20.setText(Format.toString());
        }
        if (!editText_50.getText().toString().isEmpty()) {
            sum2 = val2 * Integer.parseInt(editText_50.getText().toString());
            String Format = new DecimalFormat("#00.0").format(sum2);
            textView_S50.setText(Format.toString());
        } else {
            sum2 = 0;
            String Format = new DecimalFormat("#00.0").format(sum2);
            textView_S50.setText(Format.toString());
        }
        if (!editText_100.getText().toString().isEmpty()) {
            sum3 = val3 * Integer.parseInt(editText_100.getText().toString());
            String Format = new DecimalFormat("#00.0").format(sum3);
            textView_S100.setText(Format.toString());
        } else {
            sum3 = 0;
            String Format = new DecimalFormat("#00.0").format(sum3);
            textView_S100.setText(Format.toString());
        }
        if (!editText_200.getText().toString().isEmpty()) {
            sum4 = val4 * Integer.parseInt(editText_200.getText().toString());
            String Format = new DecimalFormat("#00.0").format(sum4);
            textView_S200.setText(Format.toString());
        } else {
            sum4 = 0;
            String Format = new DecimalFormat("#00.0").format(sum4);
            textView_S200.setText(Format.toString());
        }
        if (!editText_500.getText().toString().isEmpty()) {
            sum5 = val5 * Integer.parseInt(editText_500.getText().toString());
            String Format = new DecimalFormat("#00.0").format(sum5);
            textView_S500.setText(Format.toString());
        } else {
            sum5 = 0;
            String Format = new DecimalFormat("#00.0").format(sum5);
            textView_S500.setText(Format.toString());
        }
        if (!editText_1000.getText().toString().isEmpty()) {
            sum6 = val6 * Integer.parseInt(editText_1000.getText().toString());
            String Format = new DecimalFormat("#00.0").format(sum6);
            textView_S1000.setText(Format.toString());
        } else {
            sum6 = 0;
            String Format = new DecimalFormat("#00.0").format(sum6);
            textView_S1000.setText(Format.toString());
        }
        if (!editText_2000.getText().toString().isEmpty()) {
            sum7 = val7 * Integer.parseInt(editText_2000.getText().toString());
            String Format = new DecimalFormat("#00.0").format(sum7);
            textView_S2000.setText(Format.toString());
        } else {
            sum7 = 0;
            String Format = new DecimalFormat("#00.0").format(sum7);
            textView_S2000.setText(Format.toString());
        }
        if (!editText_5000.getText().toString().isEmpty()) {
            sum8 = val8 * Integer.parseInt(editText_5000.getText().toString());
            String Format = new DecimalFormat("#00.0").format(sum8);
            textView_S5000.setText(Format.toString());
        } else {
            sum8 = 0;
            String Format = new DecimalFormat("#00.0").format(sum8);
            textView_S5000.setText(Format.toString());
        }

        summa = sum1 + sum2 + sum3 + sum4 + sum5 + sum6 + sum7 + sum8 + sum9 + sum10 + sum11 + sum12;
        String Format = new DecimalFormat("#00.00").format(summa);
        AutSum.setText(Format.toString());


    }

    protected void Load_findViewById()
    {
        editText_20 = (EditText) findViewById(R.id.editText_20);
        editText_50 = (EditText) findViewById(R.id.editText_50);
        editText_100 = (EditText) findViewById(R.id.editText_100);
        editText_200 = (EditText) findViewById(R.id.editText_200);
        editText_500 = (EditText) findViewById(R.id.editText_500);
        editText_1000 = (EditText) findViewById(R.id.editText_1000);
        editText_2000 = (EditText) findViewById(R.id.editText_2000);
        editText_5000 = (EditText) findViewById(R.id.editText_5000);

        textView_S20 = (TextView) findViewById(R.id.textView_S20);
        textView_S50 = (TextView) findViewById(R.id.textView_S50);
        textView_S100 = (TextView) findViewById(R.id.textView_S100);
        textView_S200 = (TextView) findViewById(R.id.textView_S200);
        textView_S500 = (TextView) findViewById(R.id.textView_S500);
        textView_S1000 = (TextView) findViewById(R.id.textView_S1000);
        textView_S2000 = (TextView) findViewById(R.id.textView_S2000);
        textView_S5000 = (TextView) findViewById(R.id.textView_S5000);

        editText_1 = findViewById(R.id.editText_M4);
        editText_3 = findViewById(R.id.editText_M3);
        editText_5 = findViewById(R.id.editText_M2);
        editText_10 = findViewById(R.id.editText_M1);

        textView_S1 = findViewById(R.id.textView_S4);
        textView_S3 = findViewById(R.id.textView_S3);
        textView_S5 = findViewById(R.id.textView_S2);
        textView_S10 = findViewById(R.id.textView_S1);

        meloch = findViewById(R.id.meloch);
        meloch.setVisibility(View.GONE);
        AutSum = (TextView) findViewById(R.id.textView_autsumm);
        button_clear = findViewById(R.id.btn_kl_clear);
        button_printer = findViewById(R.id.btn_kl_printer);
        btn_meloch = findViewById(R.id.button_meloch);
    }

    protected void Clearing() {
        editText_5000.clearFocus();
        editText_2000.clearFocus();
        editText_1000.clearFocus();
        editText_500.clearFocus();
        editText_200.clearFocus();
        editText_100.clearFocus();
        editText_50.clearFocus();
        editText_20.clearFocus();

        editText_5000.setText("");
        editText_2000.setText("");
        editText_1000.setText("");
        editText_500.setText("");
        editText_200.setText("");
        editText_100.setText("");
        editText_50.setText("");
        editText_20.setText("");

        editText_1.setText("");
        editText_3.setText("");
        editText_5.setText("");
        editText_10.setText("");

        textView_S1.setText("");
        textView_S3.setText("");
        textView_S5.setText("");
        textView_S10.setText("");
    }
    protected void ClearingMeloch() {
        editText_1.setText("");
        editText_3.setText("");
        editText_5.setText("");
        editText_10.setText("");

        textView_S1.setText("");
        textView_S3.setText("");
        textView_S5.setText("");
        textView_S10.setText("");
    }

    public void onBackPressed() {
        hideInputMethod();
        finish();
    }

    /**
     * уберает программную клавиатуру
     */
    protected void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText_5000.getWindowToken(), 0);
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // если окно в фокусе, то ждем еще немного и показываем клавиатуру
            mHandler.postDelayed(mShowInputMethodTask, 0);
        }
    }
}
