package kg.roman.Mobile_Torgovla.FTP;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import kg.roman.Mobile_Torgovla.MT_MyClassSetting.FTPWebhost;
import kg.roman.Mobile_Torgovla.R;

public class ITTN_Obmen extends AppCompatActivity {
    public Button btn_download, btn_upload, btn_New_Ittn, btn_New_Esf;
    public Button btn_writeData, btn_newFileXml, btn_send_file, btn_up_file_ostatok, btt_new_esf;
    public ProgressDialog pDialog;
    public XmlSerializer serializer;
    public Cursor cursor, cursor_tovar;
    // public HashSet<String> list_clients, list_ntovar;
    public TreeSet<String> list_clients, list_ntovar;
    public ListView listView_clients, listView_tovar;
    public RadioButton radioButton_sk1, radioButton_sk2;
    public TextView textView;
    public RadioGroup radioGroup;
    public ProgressBar progressBar;
    public int kol = 0;
    public Context context;

    //////////////////// Переменные:
    // Склады:
    public String sklad_Roman = "6fcd7fca-0d23-42d7-bb66-8c5526b9032b"; //  склад по адресу: г. Каракол, улица Ам 25/3
    public String SKLAD_Roman_Work = "67408d86-ca5d-4a08-b80a-87cd195b3e6d";  // склад по адресу: г. Каракол, улица Аманбаева 25/3

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ittn_obmen);
        context = ITTN_Obmen.this;
        btn_download = findViewById(R.id.btn_down_file);
        btn_writeData = findViewById(R.id.btn_obmen);
        btn_newFileXml = findViewById(R.id.btn_new_xml);
        btn_send_file = findViewById(R.id.btn_up_file);
        btn_up_file_ostatok = findViewById(R.id.btn_up_file_ostatok);
        btt_new_esf = findViewById(R.id.btn_new_ecf);


        btn_download = findViewById(R.id.ittn_button_download);
        btn_upload = findViewById(R.id.ittn_button_upload);
        btn_New_Ittn = findViewById(R.id.ittn_button_create_ittn);
        btn_New_Esf = findViewById(R.id.ittn_button_create_icf);
        listView_clients = findViewById(R.id.ittn_listview_clients);
        listView_tovar = findViewById(R.id.ittn_listview_tovar);
        progressBar = findViewById(R.id.ittn_progressBar);
        textView = findViewById(R.id.ittn_button_summa);

        btn_New_Ittn.setClickable(false);
        btn_New_Esf.setClickable(false);
        btn_upload.setClickable(false);
        list_clients = new TreeSet<>();
        list_ntovar = new TreeSet<>();

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ITTN_Obmen.this, "Скачиваим базу данных", Toast.LENGTH_SHORT).show();
                Runnable download_files = new Runnable() {
                    @Override
                    public void run() {
                        FTPWebhost web = new FTPWebhost();
                        web.ftp_server = "ftp.sunbell.webhost.kg";
                        web.ftp_port = 21;
                        web.ftp_user_name = "sunbell_siberica";
                        web.ftp_password = "Roman911NFS";
                        String putFilesPhone = context.getDatabasePath("ittm_base.db3").getAbsolutePath();
                        String putFilesFTP = "/MT_Sunbell_Karakol/MTW_ITTN/ittm_base.db3";
                        web.getFile_FTPToPhone(putFilesFTP, putFilesPhone, ITTN_Obmen.this, true);
                    }
                };
                Thread tr = new Thread(download_files);
                tr.start();
                Log.e("LOGE", "ожидание");
                try {
                    tr.join();
                    Toast.makeText(ITTN_Obmen.this, "База готова к работе!", Toast.LENGTH_SHORT).show();
                    Log.e("LOGE", "кнопки активны");
                    btn_New_Ittn.setClickable(true);
                    btn_New_Esf.setClickable(true);

                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    Log.e("ERROR", "Ошибка скачивания файла базы данных!");
                }
            }
        });

        btn_New_Ittn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setClickable(false);
                Log.e("ЭТТН=", "Кнопка создание отчета");
                textView.setText(Write_NewDataITTN());
                Log.e("ЭТТН4", "Клиентов: " + list_clients.size());
                Log.e("ЭТТН4", "Всего_Товар: " + list_ntovar.size());

                ArrayAdapter<String> adapter_clients = new ArrayAdapter<>(ITTN_Obmen.this,
                        android.R.layout.simple_list_item_1, list_clients.toArray(new String[list_clients.size()]));
                listView_clients.setAdapter(adapter_clients);
                adapter_clients.notifyDataSetChanged();
                listView_clients.setAdapter(adapter_clients);

                ArrayAdapter<String> adapter_tovar = new ArrayAdapter<>(ITTN_Obmen.this,
                        android.R.layout.simple_list_item_1, list_ntovar.toArray(new String[list_ntovar.size()]));
                listView_tovar.setAdapter(adapter_tovar);
                adapter_tovar.notifyDataSetChanged();
                listView_tovar.setAdapter(adapter_tovar);
            }
        });

        btn_New_Esf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (!list_clients.isEmpty() && !list_ntovar.isEmpty()) {

                } else {
                    Log.e("ESF...", "не возможно создать счет-фактуру");
                    Toast.makeText(ITTN_Obmen.this, "не возможно создать счет-фактуру", Toast.LENGTH_SHORT).show();
                }*/

                Runnable create_xmlESF = new Runnable() {
                    @Override
                    public void run() {
                        myHandler.sendMessage(myHandler.obtainMessage());
                        Create_XML_ESF();
                    }
                    Handler myHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    };
                };
                Thread tr = new Thread(create_xmlESF);
                tr.start();
               /* try {
                    tr.join();
                    progressBar.setVisibility(View.INVISIBLE);

                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    Log.e("ERROR", "Ошибка скачивания файла базы данных!");
                }*/
            }
        });




       /* DB_Delete();
        Toast.makeText(ITTN_Obmen.this, "Рабочая таблица очищена!", Toast.LENGTH_SHORT).show();
        btn_download.setClickable(true);
        btn_writeData.setClickable(true);
        btn_newFileXml.setClickable(true);
        btn_send_file.setClickable(true);
        */

        btn_newFileXml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ЭТТН=", "Кнопка создание файла");
                pDialog = new ProgressDialog(ITTN_Obmen.this);
                pDialog.setMessage("Загрузка продуктов...");
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setProgress(0);
                pDialog.setMax(100);
                pDialog.show();
                ITTN_Obmen.MyAsyncTask_Sync_New_XML asyncTask = new ITTN_Obmen.MyAsyncTask_Sync_New_XML();
                asyncTask.execute();
                Toast.makeText(ITTN_Obmen.this, "Файл успешно создан", Toast.LENGTH_SHORT).show();
                v.setClickable(false);
            }
        });
        btn_send_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ЭТТН=", "Кнопка отправки файла");
                pDialog = new ProgressDialog(ITTN_Obmen.this);
                pDialog.setMessage("Загрузка продуктов...");
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setProgress(0);
                pDialog.setMax(100);
                pDialog.show();
                // Отправка файла
                ITTN_Obmen.MyAsyncTask_GoToFTP asyncTask = new ITTN_Obmen.MyAsyncTask_GoToFTP();
                Toast.makeText(ITTN_Obmen.this, "Успешно отпрвленны!", Toast.LENGTH_SHORT).show();
                asyncTask.execute();
                v.setClickable(false);
            }
        });

        btn_up_file_ostatok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создание файла для остатков
                pDialog = new ProgressDialog(ITTN_Obmen.this);
                pDialog.setMessage("Загрузка продуктов...");
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setProgress(0);
                pDialog.setMax(100);
                pDialog.show();
                // Отправка файла
                ITTN_Obmen.MyAsyncTask_NewFileXMLOstatok asyncTask = new ITTN_Obmen.MyAsyncTask_NewFileXMLOstatok();
                Toast.makeText(ITTN_Obmen.this, "Успешно отпрвленны!", Toast.LENGTH_SHORT).show();
                asyncTask.execute();


            }
        });

        btt_new_esf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создание файла для остатков
                pDialog = new ProgressDialog(ITTN_Obmen.this);
                pDialog.setMessage("Загрузка продуктов...");
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setProgress(0);
                pDialog.setMax(100);
                pDialog.show();
                // Отправка файла
                MyAsyncTask_Sync_New_XML_ESF asyncTask_sync_new_xml_esf = new MyAsyncTask_Sync_New_XML_ESF();
                Toast.makeText(ITTN_Obmen.this, "Успешно счет-фактура", Toast.LENGTH_SHORT).show();
                asyncTask_sync_new_xml_esf.execute();
            }
        });






      /*  DateFormat df_data = new SimpleDateFormat("yyyy-MM-dd");
        this_rn_data = df_data.format(Calendar.getInstance().getTime());
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateString_WORK = dateFormat2.format(calendar.getTime());

        this_rn_year = df_year.format(Calendar.getInstance().getTime());
        this_rn_month = df_month.format(Calendar.getInstance().getTime());
        this_rn_day = df_day.format(Calendar.getInstance().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(this_rn_year), Integer.valueOf(this_rn_month) - 1, Integer.valueOf(this_rn_day));*/


        pDialog = new ProgressDialog(ITTN_Obmen.this);
        pDialog.setMessage("Загрузка продуктов...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setProgress(0);
        pDialog.setMax(100);
        pDialog.show();
        // Отправка файла
        MyAsyncTask_UpListFiles asyncTask_sync_new_list = new MyAsyncTask_UpListFiles();
        Toast.makeText(ITTN_Obmen.this, "Получение данных", Toast.LENGTH_SHORT).show();
        asyncTask_sync_new_list.execute();
    }

    //////// Создание списка для создания файла ЭТТН
    protected String Write_NewDataITTN() {
        Double itogo = 0.0;
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("ittm_base.db3", MODE_PRIVATE, null);
        String query = "SELECT base_start_file.agent, base_start_file.kontragent_kod, base_start_file.kontragent_name, " +
                "base_start_file.nomenclatute_kod, base_start_file.nomenclatute_name, base_start_file.kol, base_start_file.summa_itogo, \n" +
                "base_kontragent.inn, base_kontragent.kod_uid_ittn, \n" +
                "base_nomenclatura.ittn_uid\n" +
                "FROM base_start_file\n" +
                "LEFT JOIN base_kontragent ON base_start_file.kontragent_kod = base_kontragent.kod_obmen_1c\n" +
                "LEFT JOIN base_nomenclatura ON base_start_file.nomenclatute_kod = base_nomenclatura.kod_obmen";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String ittn_inn = cursor.getString(cursor.getColumnIndexOrThrow("inn"));            // ИНН клиента
            String ittn_kod_clients = cursor.getString(cursor.getColumnIndexOrThrow("kod_uid_ittn"));   // Код склада клиента
            String ittn_kod_nomeclatura = cursor.getString(cursor.getColumnIndexOrThrow("ittn_uid"));       // Код номенклатуры
            String ittn_kol_tovar = cursor.getString(cursor.getColumnIndexOrThrow("kol"));
            String ittn_summa = cursor.getString(cursor.getColumnIndexOrThrow("summa_itogo"));

            String client = cursor.getString(cursor.getColumnIndexOrThrow("kontragent_name"));
            String tovar = cursor.getString(cursor.getColumnIndexOrThrow("nomenclatute_name"));
            Write_Data(ittn_inn, ittn_kod_clients, ittn_kod_nomeclatura, ittn_kol_tovar, ittn_summa, client, tovar);
            itogo = itogo + Double.parseDouble(ittn_summa.replaceAll(",", "."));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        String Format = new DecimalFormat("#0.00").format(itogo);
        return Format;
    }

    protected void Create_XML_ESF()
    {
        try {
            String name_file_sd;
            String name_file_db = "MTW_out_ittn_obmen_ESF";
            String file_put_db = ITTN_Obmen.this.getDatabasePath(name_file_db + ".xml").getAbsolutePath();  // путь к databases
            Log.e("XML", "FORMA_XML_OUT: " + file_put_db);
            name_file_sd = ("ESF_Obmen_DATA.xml");
            String file_db_sd = Environment.getExternalStorageDirectory() + "/Price/XML/" + name_file_sd;
            Log.e("File_put ", file_put_db);
            Log.e("File_put ", file_db_sd);

            File newxmlfile_db = new File(file_put_db);
            File newxmlfile_sd = new File(file_db_sd);

            try {
                newxmlfile_db.createNewFile();
                newxmlfile_sd.createNewFile();
            } catch (IOException e) {
                Log.e("IOException", "Exception in create new File(");
            }
            FileOutputStream fileos = null;
            FileOutputStream fileos2 = null;
            try {
                fileos = new FileOutputStream(newxmlfile_db);
                fileos2 = new FileOutputStream(newxmlfile_sd);

            } catch (FileNotFoundException e) {
                Log.e("FileNotFoundException", e.toString());
            }

            // Начало: Создание файла XML
            serializer = Xml.newSerializer();
            serializer.setOutput(fileos, "utf-8");
            //  serializer.startDocument(null, Boolean.valueOf(true));
            serializer.startDocument(null, null);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

            serializer.startTag(null, "receipts");
            XML_Array_ESF();
            serializer.endTag(null, "receipts");

            serializer.endDocument();
            serializer.flush();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.e("ESF...", "ошибка данных для ESF A"+ex);
            progressBar.setVisibility(View.INVISIBLE);
            //Toast.makeText(ITTN_Obmen.this, "ошибка данных для ESF", Toast.LENGTH_SHORT).show();
        }

    }

    protected void XML_Array_ESF() {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase("ittm_base.db3", MODE_PRIVATE, null);
            String query_TITLE = "SELECT DISTINCT base_end_xml.CustomerTin, base_kontragent.name_ittn ,base_kontragent.kod_uid_ittn, base_end_xml.UP_Client\n" +
                    "FROM base_end_xml\n" +
                    "LEFT JOIN base_kontragent ON base_kontragent.inn = base_end_xml.CustomerTin;";
            cursor = db.rawQuery(query_TITLE, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String client_inn = cursor.getString(cursor.getColumnIndexOrThrow("CustomerTin"));
                String сlient_Name = cursor.getString(cursor.getColumnIndexOrThrow("UP_Client"));
                DateFormat df_data = new SimpleDateFormat("yyyy-MM-dd");
                Log.e("ЭСФ...", "inn= " + client_inn);

                serializer.startTag(null, "receipt");

                // Заполнение Формы, Шапка
                serializer.startTag(null, "exchangeCode");
                serializer.text(KOD_UUID_RANDOM());
                serializer.endTag(null, "exchangeCode");

                serializer.startTag(null, "receiptTypeCode");
                serializer.text("10");
                serializer.endTag(null, "receiptTypeCode");

                serializer.startTag(null, "createdDate");
                serializer.text(df_data.format(Calendar.getInstance().getTime()));
                serializer.endTag(null, "createdDate");

                serializer.startTag(null, "contractorPin");
                serializer.text(client_inn);
                serializer.endTag(null, "contractorPin");

                serializer.startTag(null, "paymentTypeCode");
                serializer.text("10");
                serializer.endTag(null, "paymentTypeCode");
                // код поставки
                serializer.startTag(null, "invoiceDeliveryTypeCode");
                serializer.text("399");
                serializer.endTag(null, "invoiceDeliveryTypeCode");
                // код вида поставки:
                serializer.startTag(null, "vatDeliveryTypeCode");
                serializer.text("102");
                serializer.endTag(null, "vatDeliveryTypeCode");

                serializer.startTag(null, "currencyCode");
                serializer.text("417");
                serializer.endTag(null, "currencyCode");

                serializer.startTag(null, "isPriceWithoutTaxes");
                serializer.text("false");
                serializer.endTag(null, "isPriceWithoutTaxes");

                serializer.startTag(null, "vatCode");
                serializer.text("20");
                serializer.endTag(null, "vatCode");

                serializer.startTag(null, "isResident");
                serializer.text("true");
                serializer.endTag(null, "isResident");

                serializer.startTag(null, "isIndustry");
                serializer.text("false");
                serializer.endTag(null, "isIndustry");

                serializer.startTag(null, "note");
                serializer.text(сlient_Name); // Коментарий
                serializer.endTag(null, "note");

          /*  serializer.startTag(null, "foreignName");
            serializer.text(name);
            serializer.endTag(null, "foreignName");*/

                serializer.startTag(null, "type");
                serializer.text("10");
                serializer.endTag(null, "type");


                // Табличная часть
                serializer.startTag(null, "goods");

                // Начало: должно быть пречисление
                String query_list_tovar = "SELECT base_end_xml.ProductId, base_end_xml.UP_Tovar, " +
                        "base_end_xml.Amount, base_end_xml.Price, base_esf_table.esf_name, base_esf_table.esf_kod_uuid\n" +
                        "FROM base_end_xml\n" +
                        "LEFT JOIN base_nomenclatura ON base_end_xml.ProductId = base_nomenclatura.ittn_uid\n" +
                        "LEFT JOIN base_esf_table ON base_nomenclatura.ittn_name = base_esf_table.esf_name\n" +
                        "WHERE base_end_xml.CustomerTin == '" + client_inn + "';";

                cursor_tovar = db.rawQuery(query_list_tovar, null);
                cursor_tovar.moveToFirst();
                while (!cursor_tovar.isAfterLast()) {
                    String tovar_kod = cursor_tovar.getString(cursor_tovar.getColumnIndexOrThrow("esf_kod_uuid"));
                    String tovar_name = cursor_tovar.getString(cursor_tovar.getColumnIndexOrThrow("esf_name"));
                    String price = cursor_tovar.getString(cursor_tovar.getColumnIndexOrThrow("Price"));
                    String count = cursor_tovar.getString(cursor_tovar.getColumnIndexOrThrow("Amount"));
                    serializer.startTag(null, "good");

                    Log.e("ЭСФ...", "inn= " + client_inn);
                    Log.e("ЭСФ...", "tovar= " + tovar_name);

                    serializer.startTag(null, "vatAmount");
                    serializer.text("0");
                    serializer.endTag(null, "vatAmount");

                    serializer.startTag(null, "stCode");
                    serializer.text("50"); // код ставки НсП: 50-0%, 40-1%, 30-2%, 60-3%, 80-5%
                    serializer.endTag(null, "stCode");

                    serializer.startTag(null, "stAmount");
                    serializer.text("0");
                    serializer.endTag(null, "stAmount");

                    serializer.startTag(null, "goodsName");
                    serializer.text(tovar_name);
                    serializer.endTag(null, "goodsName");

                    serializer.startTag(null, "baseCount");
                    serializer.text(count);
                    serializer.endTag(null, "baseCount");

                    serializer.startTag(null, "price");
                    serializer.text(price);
                    serializer.endTag(null, "price");


                    serializer.endTag(null, "good");
                    cursor_tovar.moveToNext();
                    // Конец: должно быть пречисление
                }

                cursor_tovar.close();
                serializer.endTag(null, "goods");
                // Конец строки


                serializer.endTag(null, "receipt");
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.e("WJ_END:", "Ошибка: создание отчета ESF B");
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(ITTN_Obmen.this, "ошибка данных для ESF B", Toast.LENGTH_SHORT).show();
        }


    }



    protected void New_Document_ESF() {

    }


    protected void Write_Data(String inn_client, String kod_client, String tovar_name, String tovar_kol, String tovar_summa,
                              String w_client, String w_tovar) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("ittm_base.db3", MODE_PRIVATE, null);
       /* list_clients.clear();
        list_tovar.clear();*/

        // Заполнение таблицы
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("FileVer", "1.1");
        localContentValues.put("InfoType", "Selling");
        localContentValues.put("DeliveryTypeCode", "10");
        // localContentValues.put("", "");
        //  localContentValues.put("", "");

        localContentValues.put("ProviderFacilityId", SKLAD_Roman_Work);
        localContentValues.put("CustomerTin", inn_client);
        if (kod_client != null) {
            localContentValues.put("CustomerFacilityId", kod_client);

        } else {
            localContentValues.put("CustomerFacilityId", "NULL_");
            Log.e("ЭТТН1", "Нету данного клиента" + w_client);
            list_clients.add(w_client);
            //  Toast.makeText(ITTN_Obmen.this, "Нету данного клиента", Toast.LENGTH_SHORT).show();
        }

        localContentValues.put("ShippingTypeCode", "40");

        localContentValues.put("AutoShippingTypeCode", "103");

        if (tovar_name != null) {
            localContentValues.put("ProductId", tovar_name);
        } else {
            localContentValues.put("ProductId", "NULL_");
            kol++;
            Log.e("ЭТТН2", "Нету данной позиции" + kol + "_" + w_tovar);
            list_ntovar.add(w_tovar);
            // Toast.makeText(ITTN_Obmen.this, "Нету данной позиции", Toast.LENGTH_SHORT).show();
        }

        localContentValues.put("UnitCode", "796");
        localContentValues.put("Amount", tovar_kol);
        localContentValues.put("Price", Tovat_Price(tovar_kol, tovar_summa));
        localContentValues.put("NetWeight", "1");
        localContentValues.put("GrossWeight", "1");

        localContentValues.put("UP_Client", w_client);
        localContentValues.put("UP_Tovar", w_tovar);
        //  localContentValues.put("", "");

        /*
         * FileVer TEXT,             -> 1
         * InfoType TEXT,            -> Selling
         * DeliveryTypeCode TEXT,    -> 10
         * EsfNum TEXT,              -> пусто
         * GtdNum TEXT,              -> пусто
         * ProviderFacilityId TEXT,  -> Склад поставщика (67408d86-ca5d-4a08-b80a-87cd195b3e6d)
         * CustomerTin TEXT,         -> ИНН покупателя
         * CustomerFacilityId TEXT,  -> Склад покупателя
         * ShippingTypeCode TEXT,    -> Способ перевозки (40)
         * ProductId TEXT,           -> Идентификатор товара
         * UnitCode TEXT,            -> Код единицы измерения товара (796)
         * Amount TEXT,              -> Количество товара
         * Price TEXT,               -> Цена товара
         * NetWeight TEXT,           -> Масса (нетто) товара (1)
         * GrossWeight TEXT,         -> Масса (брутто) (1)
         * Comment TEXT);            -> пусто
         */
        db.insert("base_end_xml", null, localContentValues);
        db.close();
    }

    protected String Tovat_Price(String kol, String summa) {
        Double price;
        price = Double.parseDouble(summa.replaceAll(",", ".")) / Double.parseDouble(kol);
        String Format = new DecimalFormat("#00.00").format(price).replace(",", ".");
        return Format;
    }

    // Синхронизация файлов для всех складов Бишкек
    private class MyAsyncTask_Sync_New_XML extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            pDialog.setMessage("Создание файла эттн. Подождите...");
            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                publishProgress(1);
                getFloor();  // Синхронизация
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
            Log.e("ПОТОК=", "Конец потока");
            Log.e("Дата=", "09/02/2023");
            pDialog.setProgress(0);
            pDialog.dismiss();
            Toast.makeText(ITTN_Obmen.this, "Отчет успешно создан!", Toast.LENGTH_SHORT).show();
        }

        private void getFloor() throws InterruptedException {
            pDialog.setMessage("Загрузка продуктов. Подождите...");
            try {
                String name_file_sd;
                String name_file_db = "MTW_out_ittn_obmen";
                String file_put_db = ITTN_Obmen.this.getDatabasePath(name_file_db + ".xml").getAbsolutePath();  // путь к databases
                Log.e("XML", "FORMA_XML_OUT: " + file_put_db);
                name_file_sd = ("ITTN_Obmen_DATA.xml");
                String file_db_sd = Environment.getExternalStorageDirectory() + "/Price/XML/" + name_file_sd;
                Log.e("File_put ", file_put_db);
                Log.e("File_put ", file_db_sd);

                File newxmlfile_db = new File(file_put_db);
                File newxmlfile_sd = new File(file_db_sd);

                try {
                    newxmlfile_db.createNewFile();
                    newxmlfile_sd.createNewFile();
                } catch (IOException e) {
                    Log.e("IOException", "Exception in create new File(");
                }
                FileOutputStream fileos = null;
                FileOutputStream fileos2 = null;
                try {
                    fileos = new FileOutputStream(newxmlfile_db);
                    fileos2 = new FileOutputStream(newxmlfile_sd);

                } catch (FileNotFoundException e) {
                    Log.e("FileNotFoundException", e.toString());
                }

                // Начало: Создание файла XML
                serializer = Xml.newSerializer();
                serializer.setOutput(fileos, "utf-8");
                //  serializer.startDocument(null, Boolean.valueOf(true));
                serializer.startDocument(null, null);
                serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

                serializer.startTag(null, "SellingBatchUpload");
                serializer.attribute(null, "FileVer", "1.1");
                serializer.attribute(null, "InfoType", "Selling");

                serializer.startTag(null, "Sellings");
                // Создание внутренних данных
                for (int i = 0; i < XML_Array_Data().size(); i++) {
                    XML_Array(XML_Array_Data().get(i));
                }

                serializer.endTag(null, "Sellings");
                serializer.endTag(null, "SellingBatchUpload");
                serializer.endDocument();
                serializer.flush();
                fileos.close();
            } catch (Exception e) {

                Log.e("Err..", "rrr");
            }

            TimeUnit.SECONDS.sleep(1);
            pDialog.dismiss();
        }

    }

    // Создание XML-файла для загрузки ЭСФ
    private class MyAsyncTask_Sync_New_XML_ESF extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            pDialog.setMessage("Создание файла эттн. Подождите...");
            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                publishProgress(1);
                getFloor();  // Синхронизация
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
            Log.e("ПОТОК=", "Конец потока");
            Log.e("Дата=", "09/02/2023");
            pDialog.setProgress(0);
            pDialog.dismiss();
            Toast.makeText(ITTN_Obmen.this, "Отчет успешно создан!", Toast.LENGTH_SHORT).show();
        }

        private void getFloor() throws InterruptedException {
            pDialog.setMessage("Загрузка продуктов. Подождите...");
            try {
                String name_file_sd;
                String name_file_db = "MTW_out_ittn_obmen_ESF";
                String file_put_db = ITTN_Obmen.this.getDatabasePath(name_file_db + ".xml").getAbsolutePath();  // путь к databases
                Log.e("XML", "FORMA_XML_OUT: " + file_put_db);
                name_file_sd = ("ESF_Obmen_DATA.xml");
                String file_db_sd = Environment.getExternalStorageDirectory() + "/Price/XML/" + name_file_sd;
                Log.e("File_put ", file_put_db);
                Log.e("File_put ", file_db_sd);

                File newxmlfile_db = new File(file_put_db);
                File newxmlfile_sd = new File(file_db_sd);

                try {
                    newxmlfile_db.createNewFile();
                    newxmlfile_sd.createNewFile();
                } catch (IOException e) {
                    Log.e("IOException", "Exception in create new File(");
                }
                FileOutputStream fileos = null;
                FileOutputStream fileos2 = null;
                try {
                    fileos = new FileOutputStream(newxmlfile_db);
                    fileos2 = new FileOutputStream(newxmlfile_sd);

                } catch (FileNotFoundException e) {
                    Log.e("FileNotFoundException", e.toString());
                }

                // Начало: Создание файла XML
                serializer = Xml.newSerializer();
                serializer.setOutput(fileos, "utf-8");
                //  serializer.startDocument(null, Boolean.valueOf(true));
                serializer.startDocument(null, null);
                serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

                serializer.startTag(null, "receipts");
                XML_Array_ESF();
                serializer.endTag(null, "receipts");


                serializer.endDocument();
                serializer.flush();
                fileos.close();
            } catch (Exception e) {

                Log.e("Err..", "rrr");
            }

            TimeUnit.SECONDS.sleep(1);
            pDialog.dismiss();
        }

    }

    protected List<String> XML_Array_Data() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("ittm_base.db3", MODE_PRIVATE, null);
        String query = "SELECT DISTINCT * FROM base_end_xml\n" +
                "GROUP BY base_end_xml.CustomerFacilityId";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        List<String> myList = new ArrayList<String>();
        while (cursor.isAfterLast() == false) {
            String kod_client = cursor.getString(cursor.getColumnIndexOrThrow("CustomerFacilityId"));
            myList.add(kod_client);
            Log.e("MyList1", kod_client);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        Log.e("MyList2", "_" + myList.size());

        for (int i = 0; i < myList.size(); i++) {
            Log.e("MyList3", myList.get(i));
        }
        return myList;
    }

    protected void XML_Array(String w_client_code) {
        try {
            // Log.e(this.getLocalClassName(), kod_Rn + " " + S_name);

            serializer.startTag(null, "Selling");

            SQLiteDatabase db = getBaseContext().openOrCreateDatabase("ittm_base.db3", MODE_PRIVATE, null);

            String query_TITLE = "SELECT * FROM base_end_xml\n" +
                    "WHERE base_end_xml.CustomerFacilityId = '" + w_client_code + "' LIMIT 1";

            cursor = db.rawQuery(query_TITLE, null);
            cursor.moveToFirst();
            String client_inn = cursor.getString(cursor.getColumnIndexOrThrow("CustomerTin"));
            String client_code = cursor.getString(cursor.getColumnIndexOrThrow("CustomerFacilityId"));
            String name_sklad = cursor.getString(cursor.getColumnIndexOrThrow("ProviderFacilityId"));

            // Заполнение Формы, Шапка
            serializer.startTag(null, "DeliveryTypeCode");
            serializer.text("10");
            serializer.endTag(null, "DeliveryTypeCode");

            serializer.startTag(null, "EsfNum");
            serializer.text("");
            serializer.endTag(null, "EsfNum");

            serializer.startTag(null, "GtdNum");
            serializer.text("");
            serializer.endTag(null, "GtdNum");

            serializer.startTag(null, "ProviderFacilityId");
            serializer.text(name_sklad);
            serializer.endTag(null, "ProviderFacilityId");

            serializer.startTag(null, "CustomerTin");
            serializer.text(client_inn);
            serializer.endTag(null, "CustomerTin");

            serializer.startTag(null, "CustomerFacilityId");
            serializer.text(client_code);
            serializer.endTag(null, "CustomerFacilityId");

            serializer.startTag(null, "ShippingTypeCode");
            serializer.text("40");
            serializer.endTag(null, "ShippingTypeCode");





            serializer.startTag(null, "AutoShippingTypeCode");
            serializer.text("103");
            /*
            * 101 - Водители/АТС компании
            * 102 - Компания перевозчик
            * 103 - Самовывоз
            */
            serializer.endTag(null, "AutoShippingTypeCode");

            serializer.startTag(null, "DriverTin");
            serializer.text("");
            serializer.endTag(null, "DriverTin");

            serializer.startTag(null, "DriverFullname");
            serializer.text("");
            serializer.endTag(null, "DriverFullname");

            serializer.startTag(null, "CarMark");
            serializer.text("");
            serializer.endTag(null, "CarMark");

            serializer.startTag(null, "CarNumber");
            serializer.text("");
            serializer.endTag(null, "CarNumber");

            serializer.startTag(null, "ShippingOrganizationTin");
            serializer.text("");
            serializer.endTag(null, "ShippingOrganizationTin");

            serializer.startTag(null, "ShippingOrganizationName");
            serializer.text("");
            serializer.endTag(null, "ShippingOrganizationName");







            // Первая строка заказа (перечисление товара)
            serializer.startTag(null, "Products");

            // Начало: должно быть пречисление
            String query_list_tovar = "SELECT * FROM base_end_xml\n" +
                    "WHERE base_end_xml.CustomerFacilityId = '" + w_client_code + "'";
            cursor_tovar = db.rawQuery(query_list_tovar, null);
            cursor_tovar.moveToFirst();
            while (cursor_tovar.isAfterLast() == false) {
                String tovar_kod = cursor_tovar.getString(cursor_tovar.getColumnIndexOrThrow("ProductId"));
                String count = cursor_tovar.getString(cursor_tovar.getColumnIndexOrThrow("Amount"));
                String price = cursor_tovar.getString(cursor_tovar.getColumnIndexOrThrow("Price"));
                serializer.startTag(null, "SellingProduct");
                serializer.startTag(null, "ProductId");
                serializer.text(tovar_kod);
                serializer.endTag(null, "ProductId");
                serializer.startTag(null, "UnitCode");
                serializer.text("796");
                serializer.endTag(null, "UnitCode");
                serializer.startTag(null, "Amount");
                serializer.text(count);
                serializer.endTag(null, "Amount");
                serializer.startTag(null, "Price");
                serializer.text(price);
                serializer.endTag(null, "Price");
                serializer.startTag(null, "NetWeight");
                serializer.text("1");
                serializer.endTag(null, "NetWeight");
                serializer.startTag(null, "GrossWeight");
                serializer.text("1");
                serializer.endTag(null, "GrossWeight");
                serializer.endTag(null, "SellingProduct");
                cursor_tovar.moveToNext();
                // Конец: должно быть пречисление
            }

            serializer.endTag(null, "Products");
            // Конец строки

            serializer.startTag(null, "Comment");
            serializer.text("");
            serializer.endTag(null, "Comment");

            serializer.endTag(null, "Selling");


            cursor.close();
            cursor_tovar.close();
            db.close();
        } catch (Exception e) {
            Log.e("WJ_END:", "Ошибка: создание отчета!");
            Toast.makeText(this, "Ошибка: создание отчета!", Toast.LENGTH_SHORT).show();
        }

    }


    //// Массивы для ЕСФ

    protected String NewTableESF(String w_date, String w_inn, String w_name) {
        // Заполнение таблицы с данными ЭСФ
        String newkodRandom = KOD_UUID_RANDOM();
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("exchangeCode", newkodRandom);       //код обмена (UUID), сгенерированный код счет-фактуры. Код должен состоять из 32 символов, разделенных дефисами на пять групп в формате 8-4-4-4-12
        localContentValues.put("receiptTypeCode", "10");          // код вида операции: 10-Поставка товаров, 20-Акт об оказании услуг
        localContentValues.put("createdDate", w_date);                // дата поставки в формате ГГГГ-ММ-ДД
        localContentValues.put("contractorPin", w_inn);              // ИНН клиента.  В случае выписки ЭСФ в адрес филиала, то указывается ИНН филиала Если type = 10, 30, 40, то в этом поле - ИНН покупателя.  Если type = 20, то в этом поле ИНН продавца
        localContentValues.put("paymentTypeCode", "10");            // код типа оплаты: 10-Наличными, 20-Безналичными, 30-Безвозмездно
        localContentValues.put("invoiceDeliveryTypeCode", "100");    // код поставки  (100	Облагаемые по ставке 12 процентов)
        localContentValues.put("vatDeliveryTypeCode", "100");        // код вида поставки: (100	Облагаемые по ставке 12 процентов)
        localContentValues.put("isPriceWithoutTaxes", "false");        // код валюты: 417-Сом
        localContentValues.put("currencyCode", "417");        // код валюты: 417-Сом
        localContentValues.put("vatCode", "20");            // код ставки налога по НДС: 10-Стандартная 12%, 20-Нулевая 0%, 90-Освобожденная
        localContentValues.put("isResident", "true");       // Признак: Резидент значение true, если покупатель - резидент, значение false, если покупатель - нерезидент
        localContentValues.put("isIndustry", "false");     // Признак: Отраслевой значение true, если ЭСФ отраслевой,    значение false, если ЭСФ не отраслевой
        localContentValues.put("foreignName", w_name);      // наименование нерезидента
        localContentValues.put("type", "10");              // Тип ЭСФ  10-реализация, 20-приобретение, 30-корректировка ЭСФ, 40-корректировка бумажного сф

        ///////////////          Табличная часть           /////////////////////////
        localContentValues.put("vatAmount", "");    //  сумма НДС
        localContentValues.put("stCode", "");       //  код ставки НсП: 50-0%, 40-1%, 30-2%, 60-3%, 80-5%
        localContentValues.put("stAmount", "");     //  сумма НсП
        localContentValues.put("goodsName", "");    //  наименование товара
        localContentValues.put("baseCount", "");    //  фактическое количество
        localContentValues.put("price", "");        //  цена

        //w_db.insert("base_esf_upfile", null, localContentValues);

        return newkodRandom;
    }

    protected List<String> XML_Array_Data_ESF() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("ittm_base.db3", MODE_PRIVATE, null);
        String query = "SELECT DISTINCT * FROM base_end_xml\n" +
                "GROUP BY base_end_xml.CustomerFacilityId";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        List<String> myList = new ArrayList<String>();
        while (cursor.isAfterLast() == false) {
            String kod_client = cursor.getString(cursor.getColumnIndexOrThrow("CustomerFacilityId"));
            myList.add(kod_client);
            Log.e("MyList1", kod_client);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        Log.e("MyList2", "_" + myList.size());

        for (int i = 0; i < myList.size(); i++) {
            Log.e("MyList3", myList.get(i));
        }
        return myList;
    }


    //код обмена (UUID), сгенерированный код счет-фактуры.
    protected String KOD_UUID_RANDOM() {
        // в формате 8-4-4-4-12
        String stringRandom = "";
        String simvols = "abcdefghijklmnopqrstuvwxyz0123456789";
        int i = 0, k = 0;
        while (stringRandom.length() < 32) {
            List<Integer> numbers = Arrays.asList(8, 4, 4, 4, 12);
            for (int j = 0; j < numbers.get(i); j++) {
                Random random = new Random();
                stringRandom += (simvols.charAt(random.nextInt(simvols.length())));
            }
            if (k < 4) {
                stringRandom += "-";
            }
            k++;
            i++;
        }
        return stringRandom;
    }

    //// Массивы для ЕСФ


    protected void DB_Delete() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("ittm_base.db3", MODE_PRIVATE, null);
        db.execSQL("delete from base_end_xml");
        db.execSQL("delete from base_ostatok");
        db.close();

    }

    // Отправка готового файла на сервер
    private class MyAsyncTask_GoToFTP extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            pDialog.setMessage("обновление данных, подождите...");

            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                getFloor();  // Синхронизация файлов для всех складов
                publishProgress(1);
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
            Toast.makeText(ITTN_Obmen.this, "файл успешно отправлнен", Toast.LENGTH_SHORT).show();
            pDialog.setProgress(0);
            pDialog.cancel();
            pDialog.dismiss();
        }

        private void getFloor() throws InterruptedException {
            pDialog.setMessage("Загрузка данных, подождите...");
            try {

                FTPWebhost web = new FTPWebhost();
                web.ftp_server = "ftp.sunbell.webhost.kg";
                web.ftp_port = 21;
                web.ftp_user_name = "sunbell_siberica";
                web.ftp_password = "Roman911NFS";
                String path_phobe_db = ITTN_Obmen.this.getDatabasePath("MTW_out_ittn_obmen.xml").getAbsolutePath();
                web.getFileToFTP(path_phobe_db, "/MT_Sunbell_Karakol/MTW_ITTN/MTW_out_ittn_obmen.xml", true);

                String path_phobe_db2 = ITTN_Obmen.this.getDatabasePath("MTW_out_ittn_ostatok.xml").getAbsolutePath();
                web.getFileToFTP(path_phobe_db2, "/MT_Sunbell_Karakol/MTW_ITTN/MTW_out_ittn_ostatok.xml", true);

                String path_phobe_db3 = ITTN_Obmen.this.getDatabasePath("MTW_out_ittn_obmen_ESF.xml").getAbsolutePath();
                web.getFileToFTP(path_phobe_db3, "/MT_Sunbell_Karakol/MTW_ITTN/MTW_out_ittn_obmen_ESF.xml", true);

            } catch (Exception e) {
                // Toast.makeText(context_Activity, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
            }

        }  // Синхронизация файлов для всех складов

    }

    // Скачивание готового файла на сервер
    private class MyAsyncTask_DownFtp extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            pDialog.setMessage("обновление данных, подождите...");

            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                getFloor();  // Синхронизация файлов для всех складов
                publishProgress(1);
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
            Toast.makeText(ITTN_Obmen.this, "файл успешно скачен", Toast.LENGTH_SHORT).show();
            pDialog.setProgress(0);
            pDialog.cancel();
            pDialog.dismiss();
        }

        private void getFloor() throws InterruptedException {
            pDialog.setMessage("Загрузка данных, подождите...");
            try {

                FTPWebhost web = new FTPWebhost();
                web.ftp_server = "ftp.sunbell.webhost.kg";
                web.ftp_port = 21;
                web.ftp_user_name = "sunbell_siberica";
                web.ftp_password = "Roman911NFS";

                String file_phone = ITTN_Obmen.this.getDatabasePath("ittm_base.db3").getAbsolutePath();
                String file_ftp = "/MT_Sunbell_Karakol/MTW_ITTN/ittm_base.db3";

                File files_final_phone = new File(getFilesDir().getAbsolutePath());
                String files_final_ftp = "/MT_Sunbell_Karakol/MTW_ITTN/ittm_base.db3";

                web.getFileToPhone(file_phone, file_ftp, ITTN_Obmen.this, true);


            } catch (Exception e) {
                // Toast.makeText(context_Activity, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
            }

        }  // Синхронизация файлов для всех складов

    }


    protected void Ostatok(SQLiteDatabase w_db, String w_tovar_uid, String w_count, String w_price, String w_name) {
        // Заполнение таблицы
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("FacilityId", "67408d86-ca5d-4a08-b80a-87cd195b3e6d");    // Идентификатор склада
        localContentValues.put("Comment", "");                                           // Комментарии
        localContentValues.put("ProductId", w_tovar_uid);                                // Идентификатор товара
        localContentValues.put("SourceOfProductCode", "50");                             // Код причины оприходования товара
        localContentValues.put("UnitCode", "796");                                       // Код единицы измерения товара
        localContentValues.put("Amount", w_count);                                       // Количество товара
        localContentValues.put("Price", w_price);                                        // Цена за ед. товара
        localContentValues.put("Tovar_Name", w_name);                                    // Цена за ед. товара
        /*
        FacilityId TEXT,
        Comment TEXT,
        ProductId TEXT PRIMARY KEY,
        SourceOfProductCode TEXT,
        UnitCode TEXT,
        Amount TEXT,
        Price TEXT);*/
        w_db.insert("base_ostatok", null, localContentValues);
    }

    protected void NewTableOstatokSum() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("ittm_base.db3", MODE_PRIVATE, null);
        String query = "SELECT base_start_file.nomenclatute_name, SUM(base_start_file.kol) AS 'Count', " +
                "SUM(base_start_file.summa_itogo), SUM(base_start_file.summa_price) AS 'Summa', base_nomenclatura.ittn_uid\n" +
                "FROM base_start_file\n" +
                "LEFT JOIN base_nomenclatura ON base_start_file.nomenclatute_kod = base_nomenclatura.kod_obmen\n" +
                "GROUP BY base_start_file.nomenclatute_name\n" +
                "ORDER BY base_start_file.nomenclatute_name ASC;";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Log.e("ЭТТН2", "Всего " + cursor.getCount());
        while (cursor.isAfterLast() == false) {
            String ittn_inn = cursor.getString(cursor.getColumnIndexOrThrow("ittn_uid"));
            String ittn_count = cursor.getString(cursor.getColumnIndexOrThrow("Count"));
            String ittn_summa = cursor.getString(cursor.getColumnIndexOrThrow("Summa"));
            String tovar_name = cursor.getString(cursor.getColumnIndexOrThrow("nomenclatute_name"));

            Log.e("ЭТТН2", "Позиция: " + cursor.getPosition() + "_" + ittn_inn);
            Log.e("ЭТТН2", "Позиция: " + cursor.getPosition() + "_" + ittn_count);
            Log.e("ЭТТН2", "Позиция: " + cursor.getPosition() + "_" + ittn_summa);
            Log.e("ЭТТН2", "Позиция: " + cursor.getPosition() + "_" + tovar_name);

            Ostatok(db, ittn_inn, ittn_count, NewPrice(ittn_count, ittn_summa).replace(".", ","), tovar_name); // для xls
            //Ostatok(ittn_inn, ittn_count, NewPrice(ittn_count, ittn_summa));

            Log.e("ЭТТН2", "Запись" + cursor.getPosition() + "_" + tovar_name);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    private String NewPrice(String w_count, String w_summa) {
        Double newPrice = Double.parseDouble(w_summa) / Double.parseDouble(w_count);
        String Format = new DecimalFormat("#00.00").format(newPrice - (newPrice * 0.20)).replace(",", ".");
        return Format;
    }

    // Созадание файла XML для остатка
    private class MyAsyncTask_NewFileXMLOstatok extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            pDialog.setMessage("Создание файла эттн. Подождите...");
            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                publishProgress(1);
                getFloor();  // Синхронизация
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
            Log.e("ПОТОК=", "Конец потока");
            Log.e("Дата=", "09/02/2023");
            pDialog.setProgress(0);
            pDialog.dismiss();
            Toast.makeText(ITTN_Obmen.this, "Отчет успешно создан!", Toast.LENGTH_SHORT).show();
        }

        private void getFloor() throws InterruptedException {
            pDialog.setMessage("Загрузка продуктов. Подождите...");
            try {
                String name_file_db = "MTW_out_ittn_ostatok";
                String file_put_db = ITTN_Obmen.this.getDatabasePath(name_file_db + ".xml").getAbsolutePath();  // путь к databases
                Log.e("XML", "FORMA_XML_OUT: " + file_put_db);
                Log.e("File_put ", file_put_db);

                File newxmlfile_db = new File(file_put_db);

                try {
                    newxmlfile_db.createNewFile();
                } catch (IOException e) {
                    Log.e("IOException", "Exception in create new File(");
                }
                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile_db);
                } catch (FileNotFoundException e) {
                    Log.e("FileNotFoundException", e.toString());
                }

                // Начало: Создание файла XML
                serializer = Xml.newSerializer();
                serializer.setOutput(fileos, "utf-8");
                //  serializer.startDocument(null, Boolean.valueOf(true));
                serializer.startDocument(null, null);
                serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

                serializer.startTag(null, "PostingBatchUpload");
                serializer.attribute(null, "FileVer", "1.0");
                serializer.attribute(null, "InfoType", "Posting");

                serializer.startTag(null, "Postings");
                XML_ArrayOstatok();
                serializer.endTag(null, "Postings");

                serializer.endTag(null, "PostingBatchUpload");
                serializer.endDocument();
                serializer.flush();
                fileos.close();
            } catch (Exception e) {

                Log.e("Err..", "rrr");
            }

            TimeUnit.SECONDS.sleep(1);
            pDialog.dismiss();
        }

    }

    protected void XML_ArrayOstatok() {
        // Log.e(this.getLocalClassName(), kod_Rn + " " + S_name);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("ittm_base.db3", MODE_PRIVATE, null);
        String query = "SELECT * FROM base_ostatok;";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String tovar = cursor.getString(cursor.getColumnIndexOrThrow("ProductId"));
            String count = cursor.getString(cursor.getColumnIndexOrThrow("Amount"));
            String price = cursor.getString(cursor.getColumnIndexOrThrow("Price"));
            Log.e("Ostatok_tovar", "t=" + tovar);
            Log.e("Ostatok_count", "t=" + count);
            Log.e("Ostatok_price", "t=" + price);
            try {
                // Создание внутренних данных
                serializer.startTag(null, "Posting");
                // Заполнение Формы, Шапка
                serializer.startTag(null, "FacilityId");
                serializer.text("67408d86-ca5d-4a08-b80a-87cd195b3e6d");
                serializer.endTag(null, "FacilityId");
                serializer.startTag(null, "Comment");
                serializer.text("");
                serializer.endTag(null, "Comment");

                serializer.startTag(null, "Products");
                serializer.startTag(null, "PostingProduct");

                serializer.startTag(null, "ProductId");
                serializer.text(tovar);
                serializer.endTag(null, "ProductId");

                serializer.startTag(null, "SourceOfProductCode");
                serializer.text("50");
                serializer.endTag(null, "SourceOfProductCode");

                serializer.startTag(null, "UnitCode");
                serializer.text("796");
                serializer.endTag(null, "UnitCode");

                serializer.startTag(null, "Amount");
                serializer.text(count);
                serializer.endTag(null, "Amount");

                serializer.startTag(null, "Price");
                serializer.text(price);
                serializer.endTag(null, "Price");

                serializer.endTag(null, "PostingProduct");
                serializer.endTag(null, "Products");

                // Конец строки
                serializer.endTag(null, "Posting");
            } catch (Exception e) {
                Log.e("Err..", "Ошибка внутренней записи");
            }


            cursor.moveToNext();
            // Конец: должно быть пречисление
        }
        cursor.close();
        db.close();
    }


    protected void SkladToSklad() { // FileVer="1.0" InfoType="Transfer"
        /* ProviderBranchTin        ИНН филиала поставщика**
           ProviderFacilityId       Склад поставщика**
           CustomerBranchTin        ИНН филиала покупателя**
           CustomerFacilityId       Склад покупателя**
           ShippingTypeCode         Способ перевозки**
           ShippingOrganizationTin  ИНН организации перевозчика
           DriverId                 Водитель
           VehicleId                АТС
           TrailerNumber            Номер прицепа
           TrailerCount             Количество прицепов
           BatchAndNumberOfSeal     Серия и номер пломбы ГНС

           ProductId                Идентификатор товара**
           UnitCode                 Код единицы измерения товара**
           Amount                   Количество товара**
           NetWeight                Масса (нетто) товара
           GrossWeight              Масса (брутто)
           MarkCode                 Коды маркировки



         */
    }


    private class MyAsyncTask_UpListFiles extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК=", "Начало потока");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            pDialog.setMessage("обновление данных, подождите...");

            // pDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) { // Для создания сложных потоков
            try {
                getFloor();  // Синхронизация файлов для всех складов
                publishProgress(1);
            } catch (InterruptedException e) {
                Log.e("ПОТОК=", "Ошибка в потоке данных!");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
            super.onPostExecute(aVoid);
            Toast.makeText(ITTN_Obmen.this, "файл успешно отправлнен", Toast.LENGTH_SHORT).show();
            pDialog.setProgress(0);
            pDialog.cancel();
            pDialog.dismiss();
        }

        private void getFloor() throws InterruptedException {
            pDialog.setMessage("Загрузка данных, подождите...");
            try {
                FTPWebhost web = new FTPWebhost();
                web.ftp_server = "ftp.sunbell.webhost.kg";
                web.ftp_port = 21;
                web.ftp_user_name = "sunbell_siberica";
                web.ftp_password = "Roman911NFS";
                web.put_toFiles = "/MT_Sunbell_Karakol/Image";

               // web.getFTP_TestConnect();

                List<String> listFilesNoPhone = new ArrayList<>();
                listFilesNoPhone.clear();
                //listFilesNoPhone.addAll(web.getFileNotImageFTP());
                ArrayAdapter<String> adapter_listNoFiles = new ArrayAdapter<>(ITTN_Obmen.this,
                        android.R.layout.simple_list_item_1, listFilesNoPhone);
                adapter_listNoFiles.notifyDataSetChanged();
                listView_clients.setAdapter(adapter_listNoFiles);

            } catch (Exception e) {
                // Toast.makeText(context_Activity, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
            }

        }  // Синхронизация файлов для всех складов

    }
}



      /*  String path_phobe_db = ITTN_Obmen.this.getDatabasePath("MTW_out_ittn_obmen.xml").getAbsolutePath();
        web.getFileToFTP(path_phobe_db, "/MT_Sunbell_Karakol/MTW_ITTN/MTW_out_ittn_obmen.xml", true);*/

//  web.getFilesSize();
//   Log.e("Files", "список: " + web.getFileExtension(true));
//  web.getFileNotImageFTP();