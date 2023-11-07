package kg.roman.Mobile_Torgovla.Spravochnik;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import kg.roman.Mobile_Torgovla.R;

import com.dantsu.printerthermal_escpos_bluetooth.Printer;
import com.dantsu.printerthermal_escpos_bluetooth.bluetooth.BluetoothPrinters;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

public class SPR_Printer extends AppCompatActivity {

    public TextView tvw_status, textView_1;
    public Button btn1, btn2, btn3, btn4;
    public BluetoothAdapter blt_adapter;
    public Context context_Activity;
    public ImageView img_bluetiith, img_printer;
    public ListView listView;
    public static final int PERMISSION_BLUETOOTH = 1;
    private Set<BluetoothDevice> pairedDevices;
    private final int ReqCode = 100;
    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spr_printer);

        context_Activity = SPR_Printer.this;
        getSupportActionBar().setTitle("Принтер");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.office_icon_print);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }
        SpeechRecognizer.createSpeechRecognizer(this);
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
               /* editText.setText("");
                editText.setHint("Listening...");*/

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //micButton.setImageResource(R.drawable.ic_mic_black_off);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                // editText.setText(data.get(0));
                textView_1.setText(data.get(0));
                Log.e("Поиск: ", "ТЕКС:" + data.get(0));
                DB(data.get(0), data.get(0).toLowerCase(), data.get(0).toUpperCase());
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        blt_adapter = BluetoothAdapter.getDefaultAdapter();
        tvw_status = findViewById(R.id.tvw_status);
        textView_1 = findViewById(R.id.textView63);
        btn1 = findViewById(R.id.btn_status);
        btn2 = findViewById(R.id.btn_up);
        btn3 = findViewById(R.id.button_new_print);
        btn4 = findViewById(R.id.button8);
        img_bluetiith = findViewById(R.id.img_bluetooth);
        img_printer = findViewById(R.id.img_printer);
        listView = findViewById(R.id.listview);


        try {
            if (blt_adapter == null) {
                tvw_status.setText("Bluetooth на вашем устройстве не включен!");
                img_bluetiith.setImageResource(R.drawable.bluetooth_off);
            } else {
                if (blt_adapter.isEnabled()) {
                    if (blt_adapter.isDiscovering()) {
                        tvw_status.setText("Bluetooth в процессе включения.");

                    } else {
                        tvw_status.setText("Bluetooth доступен.");
                        img_bluetiith.setImageResource(R.drawable.bluetooth_on);
                    }
                } else {
                    tvw_status.setText("Bluetooth не доступен!");
                    img_bluetiith.setImageResource(R.drawable.bluetooth_off);
                }
            }

            img_bluetiith.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (blt_adapter == null) {
                        img_bluetiith.setImageResource(R.drawable.bluetooth_off);
                        Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivity(enableBTIntent);
                        img_bluetiith.setImageResource(R.drawable.bluetooth_on);
                    } else img_bluetiith.setImageResource(R.drawable.bluetooth_off);

                }
            });

            img_printer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    printIt();
                }
            });


        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка, кода!", Toast.LENGTH_SHORT).show();
            Log.e(this.getLocalClassName(), "Ошибка, кода!");
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                String toastText;

                if (bluetoothAdapter.isEnabled()) {
                    String address = bluetoothAdapter.getAddress();
                    Integer status = bluetoothAdapter.getState();
                    String name = bluetoothAdapter.getName();
                    toastText = name + " : " + address + "//" + status;
                } else
                    toastText = "Bluetooth is not enabled";
                Toast.makeText(context_Activity, toastText, Toast.LENGTH_LONG).show();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pairedDevices = blt_adapter.getBondedDevices();

                ArrayList list = new ArrayList();

                for (BluetoothDevice bt : pairedDevices) list.add(bt.getName());
                Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();

                final ArrayAdapter adapter = new ArrayAdapter(context_Activity, android.R.layout.simple_list_item_1, list);

                listView.setAdapter(adapter);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setPackage("com.dynamixsoftware.printershare");
                String file_name = "RnDec21_0001.xls";
                String path_phobe_db = Environment.getExternalStorageDirectory().toString() + "/Price/XLS/" + file_name;
                // Uri test_print = Uri.parse("android.resource://kg.price_list.roman_kerkin.sunbell_price/drawable/" + objects.get(pos).getImage());
                Uri test_print = Uri.parse("/storage/emulated/0/Price?XLS/" + file_name);
                i.setDataAndType(test_print, "application/vnd.ms-excel");
                startActivity(i);
                // "file:///sdcard/something.pdf" or "content://something"
                // "application/vnd.ms-excel"
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();

                 speechRecognizer.startListening(speechRecognizerIntent);


              /* Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speek");
                try {
                    startActivityForResult(intent, ReqCode);
                }
                catch (ActivityNotFoundException a)
                {
                    Toast.makeText(getApplicationContext(), "Device not supported", Toast.LENGTH_SHORT).show();
                }
*/

            }
        });

    }


    public void printIt() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, SPR_Printer.PERMISSION_BLUETOOTH);
        } else {
            Printer printer = new Printer(BluetoothPrinters.selectFirstPairedBluetoothPrinter(), 203, 48f, 32);
            //  printer.printFormattedText("[C]<img>\" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo_suncape, DisplayMetrics.DENSITY_MEDIUM)) + \"</img>");

            // printer.printFormattedText("[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo_suncape, DisplayMetrics.DENSITY_MEDIUM)) + "</img>");
            //|   printer.printFormattedText("[C]<img> </img>");
            printer.printFormattedText("[C] Привет \n" +
                    "[L] Привет ZZZ [L]50x[L]135=[L]6750 ");


            Toast.makeText(context_Activity, "Идет печать!", Toast.LENGTH_SHORT).show();
        }

    }

  /*  @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case SPR_Printer.PERMISSION_BLUETOOTH:
                    this.printIt();
                    break;
            }
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ReqCode: {
                if (requestCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Toast.makeText(getApplicationContext(), result.get(0), Toast.LENGTH_SHORT).show();
                    textView_1.setText(result.get(0));
                    Log.e("Поиск: ", "ТЕКС:" + result.get(0));
                }
                break;

            }
        }
    }


    protected void tap_ToDictate(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speek");
        try {
            startActivityForResult(intent, ReqCode);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Device not supported", Toast.LENGTH_SHORT).show();
        }
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

    protected void DB(String s_name, String name, String L_NAME) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("sunbell_base_db.db3", MODE_PRIVATE, null);
        String query_select = "SELECT base_in_nomeclature.brends, base_in_nomeclature.name, base_in_nomeclature.cena, base_in_ostatok.count, base_in_nomeclature.koduid\n" +
                "FROM base_in_nomeclature\n" +
                "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                "WHERE base_in_ostatok.count > 0 " +
                "AND (base_in_nomeclature.name LIKE '%" + s_name + "%' " +
                "OR base_in_nomeclature.name LIKE '%" + name + "%' " +
                "OR base_in_nomeclature.name LIKE '%" + L_NAME + "%')";

        final Cursor cursor = db.rawQuery(query_select, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String brends = cursor.getString(cursor.getColumnIndex("brends"));
            String nam = cursor.getString(cursor.getColumnIndex("name"));
            String cena = cursor.getString(cursor.getColumnIndex("cena"));
            String koduid = cursor.getString(cursor.getColumnIndex("koduid"));

            Log.e("Результат: ", brends+"_"+ nam+"_"+cena+" /"+koduid);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

    }

}
