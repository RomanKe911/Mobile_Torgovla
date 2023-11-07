package kg.roman.Mobile_Torgovla.TEST;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dantsu.printerthermal_escpos_bluetooth.Printer;
import com.dantsu.printerthermal_escpos_bluetooth.bluetooth.BluetoothPrinters;

import org.jsoup.nodes.Document;
import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_RN_END;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_RN_END;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.Spravochnik.SPR_Printer;

import  android.bluetooth.BluetoothSocket;

public class WJ_Ros_Print extends AppCompatActivity {

    public ArrayList<ListAdapterSimple_RN_END> rn_end = new ArrayList<ListAdapterSimple_RN_END>();
    public ListAdapterAde_RN_END adapterPriceClients;

    public TextView tvw_name, tvw_summa, tvw_skidka, tvw_itogo;
    public String db_kod_rn, db_klients, db_summa, db_itogo, db_adress;
    public ListView listView;
    public SharedPreferences sp;
    public Context context_Activity;
    public Button button_prn, button_refresh, button_disc;
    public static final int PERMISSION_BLUETOOTH = 1;
    private Set<BluetoothDevice> pairedDevices;
    String[][] mass_print;
    public Text text_print;
    public Document doc;
    public Printer printer;
    private final static String FILE_NAME = "print.txt";
    public String text;
    public ProgressDialog pDialog;
    public FileOutputStream fos;
    public String postclass_RN, postclass_Data, postclass_Data_ThisData;
    Calendar Calendar_Name = Calendar.getInstance();

    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;
    TextView lblPrinterName;
    OutputStream outputStream;
    InputStream inputStream;
    Thread thread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_ros_print);

        Bundle arguments = getIntent().getExtras();
        postclass_RN = arguments.get("Ros_KodRn").toString();
        postclass_Data = arguments.get("Ros_Data").toString();

        Calendar_Name = Calendar.getInstance();
        Calendar_Name.set(Integer.parseInt(postclass_Data.substring(0, 4)),
                Integer.valueOf(postclass_Data.substring(5, 7)) - 1,
                Integer.valueOf(postclass_Data.substring(postclass_Data.length() - 2, postclass_Data.length())));
        SimpleDateFormat dateFormat_view_lauout = new SimpleDateFormat("dd-MM-yyyy");  // формат для экрана даты
        postclass_Data_ThisData = dateFormat_view_lauout.format(Calendar_Name.getTime());

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.user_pda);
        getSupportActionBar().setTitle("Расходная накладная");
        getSupportActionBar().setSubtitle("Дата: " + postclass_Data_ThisData + "; " + postclass_RN);

        context_Activity = WJ_Ros_Print.this;

        tvw_name = findViewById(R.id.tvw_dialog_name);
        tvw_summa = findViewById(R.id.tvw_text_aks_summa);
        listView = findViewById(R.id.listv_adapter);
        button_prn = (findViewById(R.id.button_add));
        button_refresh = (findViewById(R.id.btn_refresh));
        button_disc = (findViewById(R.id.btn_disc));

        try {

        } catch (Exception e) {
            Toast.makeText(this, "Ошибка загрузки ListView!", Toast.LENGTH_SHORT).show();
            Log.e("WJ_RN_Work/ ", "Ошибка загрузки ListView!");
        }

        rn_end.clear();
        ListAdapter_Select_RN();
        ListAdapter_AutoSumma();
        adapterPriceClients = new ListAdapterAde_RN_END(context_Activity, rn_end);
        adapterPriceClients.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients);

        try {
            New_File_Save();
            button_prn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    New_File_Open_Print();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка файла!", Toast.LENGTH_SHORT).show();
            Log.e("WJ_RN_Work: ", "Ошибка файла!");
        }


        button_disc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{

                    disconnectBT();
                    Toast.makeText(context_Activity, "Дисконект", Toast.LENGTH_SHORT).show();
                    Log.e("WJ_Print_D: ", "Дисконект!");

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    FindBluetoothDevice();
                    openBluetoothPrinter();
                    Toast.makeText(context_Activity, "Подключение", Toast.LENGTH_SHORT).show();
                    Log.e("WJ_Print_R: ", "Подключение");

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case SPR_Printer.PERMISSION_BLUETOOTH:
                    this.printscrin();
                    break;
            }
        }
    }

    // Заполнение ListView
    protected void ListAdapter_Select_RN() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("rosnica_db.db3", MODE_PRIVATE, null);

        String query = "SELECT kod_RN, name, kol_vo, cena, summa, image\n" +
                "FROM t5_prodaji\n" +
                "WHERE kod_RN = '" + postclass_RN + "';";

        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String kodrn = cursor.getString(cursor.getColumnIndex("kod_RN"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String kol_vo = cursor.getString(cursor.getColumnIndex("kol_vo"));
            String cena = cursor.getString(cursor.getColumnIndex("cena"));
            String summa = cursor.getString(cursor.getColumnIndex("summa"));
            String image = cursor.getString(cursor.getColumnIndex("image")); //

            rn_end.add(new ListAdapterSimple_RN_END(name, kol_vo, cena, "", summa, "", "", image, ""));
            cursor.moveToNext();

        }
        cursor.close();
        db.close();
    }

    protected void ListAdapter_AutoSumma() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("rosnica_db.db3", MODE_PRIVATE, null);

        String query = "SELECT SUM(summa) AS itogo\n" +
                "FROM t5_prodaji\n" +
                "WHERE kod_RN = '"+postclass_RN+"';";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String summa = cursor.getString(cursor.getColumnIndex("itogo"));
        tvw_summa.setText(summa);
        cursor.close();
        db.close();
    }

    // Сохранение Нового файла
    protected void New_File_Save() {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase("rosnica_db.db3", MODE_PRIVATE, null);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, SPR_Printer.PERMISSION_BLUETOOTH);
            } else {
                String query = "SELECT kod_RN, name, kol_vo, cena, summa, image\n" +
                        "FROM t5_prodaji\n" +
                        "WHERE kod_RN = '"+postclass_RN+"';";

                final Cursor cursor = db.rawQuery(query, null);
                cursor.moveToFirst();
                mass_print = new String[cursor.getCount()][7];
                int ind = 1;
                fos = null;
                fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                while (cursor.isAfterLast() == false) {
                    final String kodrn = cursor.getString(cursor.getColumnIndex("kod_RN"));
                    final String name = cursor.getString(cursor.getColumnIndex("name"));
                    final String kol_vo = cursor.getString(cursor.getColumnIndex("kol_vo"));
                    final String cena = cursor.getString(cursor.getColumnIndex("cena"));
                    final String summa = cursor.getString(cursor.getColumnIndex("summa"));
                    final String image = cursor.getString(cursor.getColumnIndex("image")); //
                    text = "[L]" + ind++ + ")" + name + "\n[C]" + kol_vo + " x " + cena + " = " + summa + "\n";
                    fos.write(text.getBytes());
                    cursor.moveToNext();
                }
                cursor.close();
                db.close();
            }
            Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException ex) {

                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Открытие Нового файла и печать
    public void New_File_Open_Print() {

        FileInputStream fin = null;
        // TextView textView = (TextView) findViewById(R.id.open_text);
        try {
            fin = openFileInput(FILE_NAME);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String(bytes);
            Log.e("File", text);
            if (ContextCompat.checkSelfPermission(WJ_Ros_Print.this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(WJ_Ros_Print.this, new String[]{Manifest.permission.BLUETOOTH}, SPR_Printer.PERMISSION_BLUETOOTH);
            } else {
                printer = new Printer(BluetoothPrinters.selectFirstPairedBluetoothPrinter(), 203, 48f, 32);
                /*Double Summa_SK = Double.parseDouble(tvw_summa.getText().toString()) - Double.parseDouble(tvw_itogo.getText().toString());
                String Format = new DecimalFormat("#00.00").format(Summa_SK);*/
                printer.printFormattedText("[L]магазин 'Flamingo'\n" +
                        "[L]" + postclass_RN + " от " + postclass_Data_ThisData + "\n" +
                        "[L]" + db_adress + "\n" +
                        text +
                        "[C]-------------------------\n" +
                        "[L]К оплате:  " + tvw_summa.getText().toString());

                Toast.makeText(context_Activity, "Идет печать!", Toast.LENGTH_SHORT).show();
                printer.disconnectPrinter();
            }

            //  textView.setText(text);
        } catch (IOException ex) {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {

            try {
                if (fin != null)
                    fin.close();
            } catch (IOException ex) {

                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(context_Activity, WJ_Ros_Torg.class);
        startActivity(intent);
        finish();
    }


    protected void ListAdapter_Print() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_rn_db.db3", MODE_PRIVATE, null);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, SPR_Printer.PERMISSION_BLUETOOTH);
        } else {
            Printer printer = new Printer(BluetoothPrinters.selectFirstPairedBluetoothPrinter(), 100, 16f, 16);

            String query = "SELECT kod_RN, name, kol_vo, cena, summa, image\n" +
                    "FROM t5_prodaji\n" +
                    "WHERE kod_RN = '"+postclass_RN+"';";

            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            mass_print = new String[cursor.getCount()][7];
            int ind = 1;
            while (cursor.isAfterLast() == false) {
                final String kodrn = cursor.getString(cursor.getColumnIndex("kod_RN"));
                final String name = cursor.getString(cursor.getColumnIndex("name"));
                final String kol_vo = cursor.getString(cursor.getColumnIndex("kol_vo"));
                final String cena = cursor.getString(cursor.getColumnIndex("cena"));
                final String summa = cursor.getString(cursor.getColumnIndex("summa"));
                final String image = cursor.getString(cursor.getColumnIndex("image")); //
                text = "[L]" + ind++ + ")" + name + "\n[C]" + kol_vo + " x " + cena + " = " + summa + "\n";
                cursor.moveToNext();
            }

            cursor.close();
            db.close();
        }
    }

    public void printscrin() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, SPR_Printer.PERMISSION_BLUETOOTH);
        } else {
            Printer printer = new Printer(BluetoothPrinters.selectFirstPairedBluetoothPrinter(), 203, 48f, 32);
            //  printer.printFormattedText("[C]<img>\" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo_suncape, DisplayMetrics.DENSITY_MEDIUM)) + \"</img>");

            // printer.printFormattedText("[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo_suncape, DisplayMetrics.DENSITY_MEDIUM)) + "</img>");
            //|   printer.printFormattedText("[C]<img> </img>");
            printer.printFormattedText("[L] " + postclass_RN + " \n" +
                    "[L]" + db_summa + "[L]" + db_itogo
            );


            Toast.makeText(context_Activity, "Идет печать!", Toast.LENGTH_SHORT).show();
        }

    }

    void FindBluetoothDevice(){

        try{

            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(bluetoothAdapter==null){
                lblPrinterName.setText("No Bluetooth Adapter found");
            }
            if(bluetoothAdapter.isEnabled()){
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT,0);
            }

            Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();

            if(pairedDevice.size()>0){
                for(BluetoothDevice pairedDev:pairedDevice){

                    // My Bluetoth printer name is BTP_F09F1A
                    if(pairedDev.getName().equals("BlueTooth Printer")){
                        bluetoothDevice=pairedDev;
                        lblPrinterName.setText("Bluetooth Printer Attached: "+pairedDev.getName());
                        break;
                    }
                }
            }

            lblPrinterName.setText("Bluetooth Printer Attached");
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    // Disconnect Printer //
    void disconnectBT() throws IOException{
        try {
            stopWorker=true;
            outputStream.close();
            inputStream.close();
            bluetoothSocket.close();
            lblPrinterName.setText("Printer Disconnected.");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    void openBluetoothPrinter() throws IOException{
        try{

            //Standard uuid from string //
            UUID uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothSocket=bluetoothDevice.createRfcommSocketToServiceRecord(uuidSting);
            bluetoothSocket.connect();
            outputStream=bluetoothSocket.getOutputStream();
            inputStream=bluetoothSocket.getInputStream();

            //beginListenData();

        }catch (Exception ex){

        }
    }
}
