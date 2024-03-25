package kg.roman.Mobile_Torgovla.Work_Journal;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dantsu.printerthermal_escpos_bluetooth.Printer;
import com.dantsu.printerthermal_escpos_bluetooth.bluetooth.BluetoothPrinters;


import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

import org.jsoup.nodes.Document;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import kg.roman.Mobile_Torgovla.FormaZakazaStart.WJ_Forma_Zakaza;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_RN_END;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_RN_END;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.Spravochnik.SPR_Printer;

public class WJ_Dialog_RN_Screen extends AppCompatActivity {

    public ArrayList<ListAdapterSimple_RN_END> rn_end = new ArrayList<ListAdapterSimple_RN_END>();
    public ListAdapterAde_RN_END adapterPriceClients;

    public TextView tvw_name, tvw_summa, tvw_skidka, tvw_itogo;
    public String db_kod_rn, db_klients, db_summa, db_itogo, db_adress;
    public ListView listView;
    public SharedPreferences sp;
    public Context context_Activity;
    public Button button_prn;
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
    public String PEREM_FTP_SERV, PEREM_FTP_LOGIN, PEREM_FTP_PASS, PEREM_FTP_DISTR_XML, PEREM_FTP_DISTR_db3,
            PEREM_IMAGE_PUT_SDCARD, PEREM_IMAGE_PUT_PHONE, PEREM_IMAGE_KOD;
    public String PEREM_MAIL_LOGIN, PEREM_MAIL_PASS, PEREM_MAIL_START, PEREM_MAIL_END,
            PEREM_DB3_CONST, PEREM_DB3_BASE, PEREM_DB3_RN, PEREM_ANDROID_ID_ADMIN, PEREM_ANDROID_ID;
    public String PEREM_AG_UID, PEREM_AG_NAME, PEREM_AG_REGION, PEREM_AG_UID_REGION, PEREM_AG_CENA,
            PEREM_AG_SKLAD, PEREM_AG_UID_SKLAD, PEREM_AG_TYPE_REAL, PEREM_AG_TYPE_USER,
            PEREM_WORK_DISTR, PEREM_KOD_MOBILE, PEREM_KOD_UID_KODRN;
    public String PEREM_KLIENT_UID, PEREM_DIALOG_UID, PEREM_DIALOG_DATA_START, PEREM_DIALOG_DATA_END, PEREM_DISPLAY_START, PEREM_DISPLAY_END;
    Calendar Calendar_Name = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt_wj_zakaz_print);
        context_Activity = WJ_Dialog_RN_Screen.this;
        Constanta_Read();
        PEREM_IMAGE_KOD = "";
        Bundle arguments = getIntent().getExtras();
        postclass_RN = arguments.get("RN").toString();
        postclass_Data = arguments.get("Data").toString();

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

        tvw_name = findViewById(R.id.tvw_dialog_name);
        tvw_summa = findViewById(R.id.tvw_text_aks_summa);
        tvw_skidka = findViewById(R.id.tvw_list_skidka);
        tvw_itogo = findViewById(R.id.tvw_list_itogo);
        tvw_skidka = findViewById(R.id.tvw_list_skidka);
        listView = findViewById(R.id.listv_adapter);
        button_prn = (findViewById(R.id.button_add));

        try {
            rn_end.clear();
            ListAdapter();
            adapterPriceClients = new ListAdapterAde_RN_END(context_Activity, rn_end);
            adapterPriceClients.notifyDataSetChanged();
            listView.setAdapter(adapterPriceClients);
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка загрузки ListView!", Toast.LENGTH_SHORT).show();
            Log.e("WJ_RN_Work/ ", "Ошибка загрузки ListView!");
        }


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
            Log.e("WJ_RN_Work/ ", "Ошибка файла!");
        }

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
    protected void ListAdapter() {

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);

        String query = "SELECT base_RN_All.kod_rn, base_RN_All.summa, base_RN_All.k_agn_name, " +
                "base_RN_All.itogo, base_RN.Name,  base_RN.Kol, base_RN.Image, base_RN.Cena, base_RN.Cena_SK," +
                "base_RN.Summa AS 'S2', base_RN.Skidka, base_RN.Itogo\n" +
                "FROM base_RN_All\n" +
                "LEFT JOIN base_RN ON base_RN_All.Kod_RN = base_RN.Kod_RN\n" +
                "WHERE base_RN_All.Kod_RN = '" + postclass_RN + "'";

        // "LEFT JOIN base_nomencl ON base_RN.Kod_Univ = base_nomencl.kod_univ\n" +          base_nomencl.image,
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            db_kod_rn = cursor.getString(cursor.getColumnIndex("kod_rn"));
            db_klients = cursor.getString(cursor.getColumnIndex("k_agn_name"));
            db_summa = cursor.getString(cursor.getColumnIndex("summa"));
            db_itogo = cursor.getString(cursor.getColumnIndex("itogo"));
            String db_ls_name = cursor.getString(cursor.getColumnIndex("Name")); //
            String db_ls_kol = cursor.getString(cursor.getColumnIndex("Kol")); //
            String db_ls_cena = cursor.getString(cursor.getColumnIndex("Cena")); //
            String db_ls_cena_SK = cursor.getString(cursor.getColumnIndex("Cena_SK")); //
            String db_ls_summa = cursor.getString(cursor.getColumnIndex("S2")); //
            String db_ls_skidka = cursor.getString(cursor.getColumnIndex("Skidka")); //
            String db_ls_itogo = cursor.getString(cursor.getColumnIndex("Itogo")); //
            String db_ls_image = cursor.getString(cursor.getColumnIndex("Image")); //

            rn_end.add(new ListAdapterSimple_RN_END(db_ls_name, db_ls_kol, db_ls_cena, db_ls_cena_SK, db_ls_summa, " - " + db_ls_skidka + " %", db_ls_itogo, db_ls_image, ""));
            cursor.moveToNext();

        }
        String Format_Summa = new DecimalFormat("#00.00").format(Double.parseDouble(db_summa));
        String Format_Skidka = new DecimalFormat("#00.00").format(Double.parseDouble(db_summa) - Double.parseDouble(db_itogo));
        String Format_Itogo = new DecimalFormat("#00.00").format(Double.parseDouble(db_itogo));

        tvw_name.setText(db_klients);
        tvw_summa.setText("Сумма: " + Format_Summa);
        tvw_skidka.setText("Сумма скидка: " + Format_Skidka);
        tvw_itogo.setText("Итого к оплате: " + Format_Itogo);

        cursor.close();
        db.close();
    }

    // Сохранение Нового файла
    protected void New_File_Save() {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, SPR_Printer.PERMISSION_BLUETOOTH);
            } else {
                String query = "SELECT base_RN_All.kod_rn, base_RN_All.summa, base_RN_All.k_agn_name, base_RN_All.k_agn_adress," +
                        "base_RN_All.itogo, base_RN.Name, base_RN.Kol, base_RN.Cena, base_RN.Cena_SK," +
                        "base_RN.Summa AS 'S2', base_RN.Skidka, base_RN.Itogo\n" +
                        "FROM base_RN_All\n" +
                        "LEFT JOIN base_RN ON base_RN_All.Kod_RN = base_RN.Kod_RN\n" +
                        "WHERE base_RN_All.Kod_RN = '" + postclass_RN + "'";

                final Cursor cursor = db.rawQuery(query, null);
                cursor.moveToFirst();
                mass_print = new String[cursor.getCount()][7];
                int ind = 1;
                fos = null;
                fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                while (cursor.isAfterLast() == false) {
                    db_kod_rn = cursor.getString(cursor.getColumnIndex("kod_rn"));
                    db_klients = cursor.getString(cursor.getColumnIndex("k_agn_name"));
                    db_summa = cursor.getString(cursor.getColumnIndex("summa"));
                    db_itogo = cursor.getString(cursor.getColumnIndex("itogo"));
                    db_adress = cursor.getString(cursor.getColumnIndex("k_agn_adress")); //
                    String db_ls_name = cursor.getString(cursor.getColumnIndex("Name")); //
                    String db_ls_kol = cursor.getString(cursor.getColumnIndex("Kol")); //
                    String db_ls_cena = cursor.getString(cursor.getColumnIndex("Cena")); //
                    String db_ls_summa = cursor.getString(cursor.getColumnIndex("S2")); //
                    String db_ls_skidka = cursor.getString(cursor.getColumnIndex("Skidka")); //
                    String db_ls_cena_sk = cursor.getString(cursor.getColumnIndex("Cena_SK")); //
                    String db_ls_itogo = cursor.getString(cursor.getColumnIndex("Itogo")); //


                    if (Double.parseDouble(db_ls_skidka) > 0) {
                        text = "[L]" + ind++ + ")" + db_ls_name + "\n[C]" + db_ls_kol + " x " + db_ls_cena + " = " + db_ls_summa + "\n" +
                                "[C]" + "- " + db_ls_skidka + "% (" + db_ls_cena_sk + ") = " + db_ls_itogo + "\n";
                    } else {
                        text = "[L]" + cursor.getPosition() + ")" + db_ls_name + "\n[C]" + db_ls_kol + " x " + db_ls_cena + " = " + db_ls_summa + "\n" +
                                "[C]" + "- " + db_ls_skidka + "% (" + db_ls_cena_sk + ") = " + db_ls_itogo + "\n";
                    }
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
            if (ContextCompat.checkSelfPermission(WJ_Dialog_RN_Screen.this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(WJ_Dialog_RN_Screen.this, new String[]{Manifest.permission.BLUETOOTH}, SPR_Printer.PERMISSION_BLUETOOTH);
            } else {
                printer = new Printer(BluetoothPrinters.selectFirstPairedBluetoothPrinter(), 203, 48f, 32);
                /*Double Summa_SK = Double.parseDouble(tvw_summa.getText().toString()) - Double.parseDouble(tvw_itogo.getText().toString());
                String Format = new DecimalFormat("#00.00").format(Summa_SK);*/


                printer.printFormattedText("[L]" + tvw_name.getText().toString() + "\n[L]" + postclass_RN + " от " + postclass_Data_ThisData + "\n[L]" + db_adress + "\n" + text +
                        "[C]-------------------------\n" +
                        "[L]" + tvw_summa.getText().toString() +
                        "\n[L]" + tvw_skidka.getText().toString() +
                        "\n[L]" + tvw_itogo.getText().toString());


                // printer.printFormattedText("[L]"+"Привет, Hello");
                //   printer.printFormattedText("[L]"+tvw_name.getText() + "\n [L]" + tvw_kod.getText() + " \n " + text);
                //   printer.printFormattedText(tvw_name.getText() + "\n" + tvw_kod.getText() + " \n " + text + "[L]Сумма:" + tvw_summa.getText() + " \n[L]Итого к оплате: " + tvw_itogo.getText());
                //  Log.e("File_Save", tvw_name.getText() + "\n" + tvw_kod.getText() + " \n " + text + " \n[L] Сумма:" + tvw_summa.getText() + " \n[L] Итого к оплате: " + tvw_itogo.getText());
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
        Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza.class);
        startActivity(intent);
        finish();
    }

    public void printscrin() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, SPR_Printer.PERMISSION_BLUETOOTH);
        } else {
            Printer printer = new Printer(BluetoothPrinters.selectFirstPairedBluetoothPrinter(), 203, 48f, 32);
            //  printer.printFormattedText("[C]<img>\" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo_suncape, DisplayMetrics.DENSITY_MEDIUM)) + \"</img>");

            // printer.printFormattedText("[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logo_suncape, DisplayMetrics.DENSITY_MEDIUM)) + "</img>");
            //|   printer.printFormattedText("[C]<img> </img>");
            printer.printFormattedText("[L] " + db_kod_rn + " \n" +
                    "[L]" + db_klients + "\n" +
                    "[L]" + db_summa + "[L]" + db_itogo
            );


            Toast.makeText(context_Activity, "Идет печать!", Toast.LENGTH_SHORT).show();
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
    }


}

