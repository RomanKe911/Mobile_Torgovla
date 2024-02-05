package kg.roman.Mobile_Torgovla.Work_Journal;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import kg.roman.Mobile_Torgovla.FTP.FtpAction;
import kg.roman.Mobile_Torgovla.FTP.FtpAsyncTask_RN_Up_Excel;
import kg.roman.Mobile_Torgovla.FTP.FtpConnection;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Excel;
import kg.roman.Mobile_Torgovla.MailSenderClass;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Price;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Price_ResourceParser;
import kg.roman.Mobile_Torgovla.XML_Files.User;
import kg.roman.Mobile_Torgovla.XML_Files.UserResourceParser;

public class WJ_XML extends AppCompatActivity {

    public ArrayList<ListAdapterSimple_Excel> excell = new ArrayList();
    public Button button, button2, button3, button4, button5, button6, button7;
    public ListView list;
    public Context context_Activity;
    public ArrayAdapter<String> adapter;
    public String[] mass_kod_univ, mass_name, mass_kol, mass_cena, mass_k_agent, mass_title_mail;
    public String[][] mass_data, mass_name_xls;
    public Integer kol_pos, kol_pos_data, kol_pos_agent;
    public File fileName;
    public FileOutputStream fos;
    public File sdPath;
    public OutputStream out;
    public SharedPreferences sp;
    public String name_put, name_exel, name_nomer;
    public File file_put, file_reg;
    public String from, PARAMS, attach, text, title, where;
    public String new_write, ftp_con_serv, ftp_con_login, ftp_con_pass, ftp_con_put, name_db, data_ostatok;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now, this_data_work_now, this_vrema_work_now;
    public XmlSerializer serializer;

    public String SQL_name, SQL_kod_univ, SQL_kol, SQL_cena, SQL_summa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml);


        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button_price);
        button3 = (Button) findViewById(R.id.button5);  // Work New Write XML
        button4 = (Button) findViewById(R.id.button6);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //  File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "your_app_name" + "/" + xmlFile);
                /*    String file_db = WJ_XML.this.getDatabasePath("userData.xml").getAbsolutePath(); // путь к databases
                    File file = new File(file_db);
                    file.createNewFile();
                    FileOutputStream fileos = new FileOutputStream(file);*/
                    //File xmlDir = new File(Environment.getExternalStorageDirectory().getPath() + "/data/new.xml");


                    String file_db = WJ_XML.this.getDatabasePath("NEWuserData.xml").getAbsolutePath(); // путь к databases
                    File newxmlfile = new File(file_db);
                    try {
                        newxmlfile.createNewFile();
                    } catch (IOException e) {
                        Log.e("IOException", "Exception in create new File(");
                    }
                    FileOutputStream fileos = null;
                    try {
                        fileos = new FileOutputStream(newxmlfile);

                    } catch (FileNotFoundException e) {
                        Log.e("FileNotFoundException", e.toString());
                    }

                    serializer = Xml.newSerializer();
                    serializer.setOutput(fileos, "UTF-8");
                    serializer.startDocument(null, Boolean.valueOf(true));
                    serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

                    serializer.startTag(null, "XML_Array");
                    XML_Array_Name("", "", "");


                  /*  String[] mass_xml = getResources().getStringArray(R.array.mass_xml);
                    for(int i =0; i<mass_xml.length; i++)
                    {


                    }*/

                    serializer.endTag(null, "XML_Array");

                    serializer.endDocument();
                    serializer.flush();
                    fileos.close();


                } catch (Exception e) {

                }

            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String xmlFile = "userData";
                String userNAme = "username";
                String password = "password";
                try {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "kg.roman.Mobile_Torgovla.Work_Journal" + "/" + xmlFile);
                    file.createNewFile();

                    //FileOutputStream fileos = new FileOutputStream(file);
                    FileOutputStream fos = new FileOutputStream("userData.xml");
                    FileOutputStream fileos = getApplicationContext().openFileOutput(xmlFile, Context.MODE_PRIVATE);
                    XmlSerializer xmlSerializer = Xml.newSerializer();
                    StringWriter writer = new StringWriter();
                    xmlSerializer.setOutput(writer);
                    xmlSerializer.startDocument("UTF-8", true);
                    xmlSerializer.startTag(null, "userData");
                    xmlSerializer.startTag(null, "userName");
                    xmlSerializer.text("username_String_Here");
                    xmlSerializer.endTag(null, "userName");
                    xmlSerializer.startTag(null, "password");
                    xmlSerializer.text("password_String");
                    xmlSerializer.endTag(null, "password");
                    xmlSerializer.endTag(null, "userData");
                    xmlSerializer.endDocument();
                    xmlSerializer.flush();
                    String dataWrite = writer.toString();
                    fileos.write(dataWrite.getBytes());
                    fileos.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                XmlPullParser xpp = getResources().getXml(R.xml.network_security_config);
                UserResourceParser parser = new UserResourceParser();
                if (parser.parse(xpp)) {
                    for (User prod : parser.getUsers()) {
                        Log.e("XML", prod.toString());
                    }
                }

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String file_db = WJ_XML.this.getDatabasePath("MTW_In_Price.xml").getAbsolutePath(); // путь к databases
                    InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                    XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp = parserFactory.newPullParser();
                    xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    xpp.setInput(istream, null);

                    MTW_Price_ResourceParser parser = new MTW_Price_ResourceParser();
                    int eventType = xpp.getEventType();
                    if (parser.parse(xpp)) {
                        for (MTW_Price price : parser.getPrice()) {
                            //Log.e("XML..", price.toString());
                            Log.e("UIDYY..", price.getUid() + " Cena.." + price.getCena());
                        }
                    }
                    Log.e("NEW_.", String.valueOf(eventType));
                    Log.e("NEW_.", String.valueOf(parser.getPrice().size()));
                } catch (Exception e) {

                    Log.e("Err..", "rrr");
                }


                XmlPullParser xpp = getResources().getXml(R.xml.network_security_config);
                MTW_Price_ResourceParser parser = new MTW_Price_ResourceParser();
                if (parser.parse(xpp)) {
                    for (MTW_Price price : parser.getPrice()) {
                        //Log.e("XML..", price.toString());
                        // Log.e("UID..", price.getUid()+" Cena.."+ price.getCena());
                    }
                }
            }
        });


    }

    protected void Load_3() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("my_w.db3", MODE_PRIVATE, null);
        String query = "SELECT id, data, ras, nomer, k_agent, kod_univ, name, cena, kol FROM RN";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        ContentValues localContentValuesup = new ContentValues();

        int k = 0;
        while (cursor.isAfterLast() == false) {
            String index = (cursor.getString(cursor.getColumnIndex("id")));
            String kod_univ = (cursor.getString(cursor.getColumnIndex("kod_univ")));

            if (!kod_univ.equals("КНД")) {
                localContentValuesup.put("k_agent", mass_k_agent[k]);
                Log.e("Data= ", mass_k_agent[k]);
                db.update("RN", localContentValuesup, "id = ?", new String[]{index});
                cursor.moveToNext();
            } else {
                k++;
                localContentValuesup.put("k_agent", "КНД");
                Log.e("Data_NO= ", mass_k_agent[k]);
                db.update("RN", localContentValuesup, "id = ?", new String[]{index});
                cursor.moveToNext();
                k++;
            }

        }
        Log.e("Конец", "Зашла 3");
        cursor.close();
        db.close();
    }

    protected void Load_2() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("my_w.db3", MODE_PRIVATE, null);
        String query = "SELECT id, data, ras, nomer, k_agent, kod_univ, name, cena, kol FROM RN";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        ContentValues localContentValuesup = new ContentValues();
        int k = 0;
        while (cursor.isAfterLast() == false) {
            String index = (cursor.getString(cursor.getColumnIndex("id")));
            String kod_univ = (cursor.getString(cursor.getColumnIndex("kod_univ")));
            String name = (cursor.getString(cursor.getColumnIndex("name")));
            String cena = (cursor.getString(cursor.getColumnIndex("cena")));
            String kol = (cursor.getString(cursor.getColumnIndex("kol")));

            String ras = (cursor.getString(cursor.getColumnIndex("ras")));
            String nomer = (cursor.getString(cursor.getColumnIndex("nomer")));
            String k_agent = (cursor.getString(cursor.getColumnIndex("k_agent")));

            if (!kod_univ.equals("КНД")) {
                localContentValuesup.put("ras", mass_data[k][0]);
                localContentValuesup.put("nomer", mass_data[k][1]);
                localContentValuesup.put("data", mass_data[k][2]);
                Log.e("Data= ", mass_data[k][0] + " " + mass_data[k][1] + " " + mass_data[k][2]);

                String[] arrayOfString = new String[1];
                String[] arrayOfString2 = new String[1];
                arrayOfString[0] = ras;
                arrayOfString2[0] = kod_univ;
                // db.update("RN", localContentValuesup, "ras = ? AND Kod_Univ = ?", new String[]{ras, kod_univ});
                db.update("RN", localContentValuesup, "id = ?", new String[]{index});
                // db.update("mytable", cv, "id = ?", new String[] { id });
                // db.update("RN", localContentValues, index +"=" + index, null);
                //mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null)>0;
                cursor.moveToNext();
            } else {
                k++;
                localContentValuesup.put("ras", "КНД");
                localContentValuesup.put("nomer", "КНД");
                localContentValuesup.put("data", "КНД");
                Log.e("Data_NO= ", mass_data[k][0] + " " + mass_data[k][1] + " " + mass_data[k][2]);
                // db.update("RN", localContentValuesup, "ras = ? AND Kod_Univ = ?", new String[]{ras, kod_univ});
                db.update("RN", localContentValuesup, "id = ?", new String[]{index});
                k++;

                //db.update("RN", localContentValues, "id = ?", new String[]{index});
                cursor.moveToNext();
            }

        }
        Log.e("Конец", "Зашла 2");
        cursor.close();
        db.close();
    }

    protected void Load_1() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("my_w.db3", MODE_PRIVATE, null);
        String query = "SELECT data, ras, nomer, k_agent, kod_univ, name, cena, kol FROM RN";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        ContentValues localContentValues = new ContentValues();
        for (int i = 0; i < kol_pos; i++) {
            localContentValues.put("kod_univ", mass_kod_univ[i]);
            localContentValues.put("name", mass_name[i]);
            localContentValues.put("cena", mass_cena[i]);
            localContentValues.put("kol", mass_kol[i]);
            db.insert("RN", null, localContentValues);
            cursor.moveToNext();
        }
        Log.e("Конец", "Зашла");
        cursor.close();
        db.close();
    }

    protected void Load_4() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("my_w.db3", MODE_PRIVATE, null);
        String query = "SELECT * FROM base_1_RER \n" +
                "WHERE (name LIKE '->> РасходнаяНакладная%') OR (name LIKE '--> Контрагент%') \n" +
                "OR (name LIKE '--> Товар%') \n" +
                "OR (name LIKE '--> Количество%') \n" +
                "OR (name LIKE '%--> Цена%') \n" +
                "OR (name = \"КНД\");";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        ContentValues localContentValues = new ContentValues();
        while (cursor.isAfterLast() == false) {
            String name = (cursor.getString(cursor.getColumnIndex("name"))).replaceAll("\\s+", "");
            localContentValues.put("name", name);
            db.insert("base2", null, localContentValues);
            cursor.moveToNext();
        }
        Log.e("Конец", "Зашла");
        cursor.close();
        db.close();
    }


    protected void Loading_Data() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("my_w.db3", MODE_PRIVATE, null);
        String query = "SELECT * FROM base2\n" +
                "WHERE name LIKE \"->>Расх%\" or name LIKE \"КНД\"";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        mass_data = new String[cursor.getCount()][3];
        while (cursor.isAfterLast() == false) {
            String name = (cursor.getString(cursor.getColumnIndex("name"))).replaceAll("\\s+", "");
            Integer id = (cursor.getInt(cursor.getColumnIndex("id")));
            if (!name.equals("КНД")) {
                String rash, kod_r, data;
                rash = name.substring(0, 21);  // ->>РасходнаяНакладная
                kod_r = name.substring(21, name.length() - 8);  // ОкKR000304
                data = name.substring(name.length() - 8, name.length()); // 12.10.21

                Log.e("Расх", "id= " + id + ", " + rash);
                Log.e("Номер Н", kod_r);
                Log.e("Дата", data);
                mass_data[cursor.getPosition()][0] = rash;
                mass_data[cursor.getPosition()][1] = kod_r;
                mass_data[cursor.getPosition()][2] = data;
                cursor.moveToNext();
            } else {
                mass_data[cursor.getPosition()][0] = name;
                mass_data[cursor.getPosition()][1] = name;
                mass_data[cursor.getPosition()][2] = name;
                Log.e("Конец", "id= " + id + ", " + name);
                cursor.moveToNext();
            }
        }
        kol_pos_data = cursor.getPosition();
        cursor.close();
        db.close();
    }

    protected void Loading_Name_K_Agent() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("my_w.db3", MODE_PRIVATE, null);
        String query = "SELECT * FROM base2\n" +
                "WHERE (name LIKE '-->Контр%')  or (name LIKE \"КНД\")";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        mass_k_agent = new String[cursor.getCount()];
        while (cursor.isAfterLast() == false) {
            String name = (cursor.getString(cursor.getColumnIndex("name"))).replaceAll("\\s+", "");
            Integer id = (cursor.getInt(cursor.getColumnIndex("id")));
            if (!name.equals("КНД")) {
                String k_agent;
                k_agent = name.substring(13, name.length() - 30); // магазин"Береке+"(Саткын)
                mass_k_agent[cursor.getPosition()] = k_agent;
                Log.e("Агент ", k_agent);
                cursor.moveToNext();
            } else {
                mass_k_agent[cursor.getPosition()] = name;
                Log.e("Конец", "id= " + id + ", " + name);
                cursor.moveToNext();
            }

        }
        kol_pos_agent = cursor.getCount();
        cursor.close();
        db.close();

    }

    protected void Loading_Kod_Univ() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("my_w.db3", MODE_PRIVATE, null);
        String query = "SELECT * FROM base2\n" +
                "WHERE (name LIKE '-->Товар%')  or (name LIKE \"КНД\");";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        mass_kod_univ = new String[cursor.getCount()];
        while (cursor.isAfterLast() == false) {
            String name = (cursor.getString(cursor.getColumnIndex("name"))).replaceAll("\\s+", "");
            Integer id = (cursor.getInt(cursor.getColumnIndex("id")));
            if (!name.equals("КНД")) {
                String posit, kod, kod_univ;
                posit = name.substring(8, name.length() - 38);
                kod = name.substring(8 + posit.length(), name.length() - 12); //00000001/00000075/BL110172
                StringBuilder myName = new StringBuilder(kod);
                myName.setCharAt(20, '0');
                myName.setCharAt(21, '0');
                mass_kod_univ[cursor.getPosition()] = myName.toString();
                Log.e("kod_univ", myName.toString()); //00000001/00000075/BL000172
                cursor.moveToNext();
            } else {
                mass_kod_univ[cursor.getPosition()] = name;
                Log.e("Конец", "id= " + id + ", " + name);
                cursor.moveToNext();
            }
            kol_pos = cursor.getCount();
            Log.e("Конец", String.valueOf(kol_pos));
        }
        cursor.close();
        db.close();
    }

    protected void Loading_Kol() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("my_w.db3", MODE_PRIVATE, null);
        String query = "SELECT * FROM base2\n" +
                "WHERE (name LIKE '-->Кол%')  or (name LIKE \"КНД\");";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        mass_kol = new String[cursor.getCount()];
        while (cursor.isAfterLast() == false) {
            String name = (cursor.getString(cursor.getColumnIndex("name"))).replaceAll("\\s+", "");
            Integer id = (cursor.getInt(cursor.getColumnIndex("id")));
            if (!name.equals("КНД")) {
                String kol;
                kol = name.substring(13, name.length() - 1);
                mass_kol[cursor.getPosition()] = kol;
                Log.e("Кол", "id= " + id + ", " + kol); // Количество
                cursor.moveToNext();
            } else {
                mass_kol[cursor.getPosition()] = name;
                Log.e("Конец", "id= " + id + ", " + name);
                cursor.moveToNext();
            }

        }
        cursor.close();
        db.close();
    }

    protected void Loading_Tovar() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("my_w.db3", MODE_PRIVATE, null);
        String query = "SELECT * FROM base_1_RER\n" +
                "WHERE (name LIKE '--> Тов%')  or (name LIKE \"КНД\");";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        mass_name = new String[cursor.getCount()];
        while (cursor.isAfterLast() == false) {
            String name = (cursor.getString(cursor.getColumnIndex("name")));
            Integer id = (cursor.getInt(cursor.getColumnIndex("id")));
            if (!name.equals("КНД")) {
                String name_pos;
                name_pos = name.substring(59, name.length() - 89);
                mass_name[cursor.getPosition()] = name_pos;
                Log.e("Кол", "id= " + id + ", " + name_pos); // Количество
                cursor.moveToNext();
            } else {
                mass_name[cursor.getPosition()] = name;
                Log.e("Конец", "id= " + id + ", " + name);
                cursor.moveToNext();
            }

        }
        cursor.close();
        db.close();
    }

    protected void Loading_Cena() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("my_w.db3", MODE_PRIVATE, null);
        String query = "SELECT * FROM base2\n" +
                "WHERE (name LIKE '-->ЦенаБС%')  or (name LIKE \"КНД\");";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        mass_cena = new String[cursor.getCount()];
        while (cursor.isAfterLast() == false) {
            String name = (cursor.getString(cursor.getColumnIndex("name")));
            Integer id = (cursor.getInt(cursor.getColumnIndex("id")));
            if (!name.equals("КНД")) {
                String cena;
                cena = name.substring(9, name.length() - 1);
                mass_cena[cursor.getPosition()] = cena;
                Log.e("Цена", "id= " + id + ", " + cena); // Количество
                cursor.moveToNext();
            } else {
                mass_cena[cursor.getPosition()] = name;
                Log.e("Конец", "id= " + id + ", " + name);
                cursor.moveToNext();
            }

        }
        cursor.close();
        db.close();
    }

    protected void Loadind_k_agent() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("my_w.db3", MODE_PRIVATE, null);
        String query = "SELECT data, ras, nomer, k_agent, kod_univ, name, cena, kol FROM RN\n" +
                "GROUP BY nomer\n" +
                "ORDER BY nomer";

        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        mass_name_xls = new String[cursor.getCount()][3];
        while (cursor.isAfterLast() == false) {
            String nomer = cursor.getString(cursor.getColumnIndex("nomer"));
            String data = cursor.getString(cursor.getColumnIndex("data"));
            String k_agent = cursor.getString(cursor.getColumnIndex("k_agent"));
            if (!nomer.equals("КНД")) {
                mass_name_xls[cursor.getPosition()][0] = data;
                mass_name_xls[cursor.getPosition()][1] = nomer;
                mass_name_xls[cursor.getPosition()][2] = k_agent;
                cursor.moveToNext();
            } else {
                mass_name_xls[cursor.getPosition()][0] = "Конец:";
                mass_name_xls[cursor.getPosition()][1] = String.valueOf(cursor.getCount());
                mass_name_xls[cursor.getPosition()][2] = "";
                cursor.moveToNext();
            }

        }
        cursor.close();
        db.close();
    }


    public void Writing_To_Exsel() {
        HSSFWorkbook localHSSFWorkbook = new HSSFWorkbook();
        HSSFSheet localHSSFSheet = localHSSFWorkbook.createSheet("Заказ");
        List localList = fillData();
        int i = 0;
        localHSSFSheet.createRow(0);
        Iterator localIterator = localList.iterator();
        while (localIterator.hasNext()) {
            ListAdapterSimple_Excel localListAdapterSimple_Excel = (ListAdapterSimple_Excel) localIterator.next();
            i++;
            createSheetHeader(localHSSFSheet, i, localListAdapterSimple_Excel);
        }
        this.fos = null;
        if (!Environment.getExternalStorageState().equals("mounted"))
            Toast.makeText(getApplicationContext(), "SD-сущ", Toast.LENGTH_LONG).show();
        this.sdPath = Environment.getExternalStorageDirectory();
        this.sdPath = new File(this.sdPath.getAbsolutePath() + "/Price/" + name_put + "/");

        if (!new File(getSDcardPath()).exists())
            new File(getSDcardPath()).mkdirs();
        this.fileName = new File(this.sdPath, name_exel + ".xls");
        try {
            this.fos = new FileOutputStream(this.fileName);
            localHSSFWorkbook.write(this.fos);
            localHSSFWorkbook.close();
            Toast.makeText(getApplicationContext(), "Excel файл успешно создан!", Toast.LENGTH_LONG).show();
            return;
        } catch (IOException localIOException) {
            while (true)
                localIOException.printStackTrace();
        }
    }

    private String getSDcardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    private static void createSheetHeader(HSSFSheet paramHSSFSheet, int paramInt, ListAdapterSimple_Excel paramListAdapterSimple_Excel) {
        // Создание столбцов для Excel
        HSSFRow localHSSFRow = paramHSSFSheet.createRow(paramInt);
        localHSSFRow.createCell(0).setCellValue(paramListAdapterSimple_Excel.getKod());
        localHSSFRow.createCell(1).setCellValue(paramListAdapterSimple_Excel.getName());
        localHSSFRow.createCell(2).setCellValue(paramListAdapterSimple_Excel.getCena().doubleValue());
        localHSSFRow.createCell(3).setCellValue(paramListAdapterSimple_Excel.getKol().intValue());
    }

    public List<ListAdapterSimple_Excel> fillData() {
        excell.clear();
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("my_w.db3", MODE_PRIVATE, null);
        String query = "SELECT data, ras, nomer, k_agent, kod_univ, name, cena, kol FROM RN\n" +
                "WHERE nomer LIKE '" + name_nomer + "%'";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String kod_univ = cursor.getString(cursor.getColumnIndex("kod_univ"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String cena = cursor.getString(cursor.getColumnIndex("cena"));
            String kol = cursor.getString(cursor.getColumnIndex("kol"));
            excell.add(new ListAdapterSimple_Excel(kod_univ, name, Double.parseDouble(cena), Integer.parseInt(kol)));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return excell;
    }


    protected void ListAdapet_Internet_Load() {
       /* SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_rn_db.db3", MODE_PRIVATE, null);
        Log.e(this.getLocalClassName(), this_data_work_now);

        String query = "SELECT base_RN_All.Data, base_RN_All.Agent, base_RN_All.Kod_RN, base_RN_All.Klients, base_RN_All.Adress, base_RN_All.Summa, base_RN_All.Summa_2\n" +
                "FROM base_RN_All\n" +
                "WHERE base_RN_All.Agent LIKE '%" + PEREM_Agent + "%' AND base_RN_All.Data LIKE '%" + this_data_work_now + "%'";*/

        new_write = ("");
        new_write = (new_write + "");
        new_write = (new_write + "Дата загрузки: " + this_data_now + " | " + this_vrema_work_now);
        //  new_write = (new_write + "Дата загрузки: " + this_data_work_now);
        new_write = (new_write + "\n");

        for (int i = 0; i < mass_title_mail.length; i++) {
            new_write = (new_write + mass_title_mail[i]);
            new_write = (new_write + "\n");
            Log.e("Tag", "Selected item on position " + mass_title_mail[i]);
        }

    }

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
            Toast.makeText(context_Activity, "Заказ успешно отправлен!!!", Toast.LENGTH_LONG).show();
            //((Activity)mainContext).finish();
        }

        @Override
        protected Boolean doInBackground(Object... params) {

            try {
                title = "Расходные накладные: ";
                text = new_write;
                //text = "Привет";

                from = "kerkin911@mail.ru";
                where = "kerkin911@gmail.com";
                attach = "";
                MailSenderClass sender = new MailSenderClass("kerkin911@mail.ru", "microlab_LG901480", "465", "smtp.mail.ru");

                sender.sendMail(title, text, from, where, attach);
            } catch (Exception e) {
                Toast.makeText(context_Activity, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
            }

            return false;
        }
    }

    protected void Serv_Go() {
        // подключение к ftp-server
        FtpConnection c = new FtpConnection();
        c.server = ftp_con_serv;
        c.port = 21;
        c.username = ftp_con_login;
        c.password = ftp_con_pass;

        FtpAction a = new FtpAction();
        a.success = true;
        a.connection = c;
        // a.remoteFilename = ftp_put;
        new FtpAsyncTask_RN_Up_Excel(context_Activity).execute(a);

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
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("dd:MM:yyyy");
        String dateString_NOW = dateFormat1.format(calendar.getTime());
        String dateString_WORK = dateFormat2.format(calendar.getTime());
        String dateString_NOW_Ostatok = dateFormat3.format(calendar.getTime());
        this_data_now = dateString_NOW;
        this_data_work_now = dateString_WORK;
        this_vrema_work_now = this_rn_vrema;
        this_data_now = dateString_NOW_Ostatok + " " + this_rn_vrema;
        Log.e(this.getLocalClassName(), "Рабочий день на:" + dateString_NOW);
        //  Log.e("WJ_FormaL2:", "!DataEnd:" + dateString2);

    } // Загрузка даты и время


    protected void XML_Array(String kod_Rn, String S_name, String uid, String data) {
        try {
            serializer.startTag(null, "Array_Order");
            serializer.startTag(null, "SyncTableNomenclatura");
            // Заполнение Формы
            serializer.startTag(null, "Nomer_Order");
            serializer.text(kod_Rn);
            serializer.endTag(null, "Nomer_Order");
            serializer.startTag(null, "UID_C");
            serializer.text(uid);
            serializer.endTag(null, "UID_C");
            serializer.startTag(null, "NAME_C");
            serializer.text(S_name);
            serializer.endTag(null, "NAME_C");
            serializer.startTag(null, "DATA");
            serializer.text(data);
            serializer.endTag(null, "DATA");
            serializer.startTag(null, "CREDIT");
            serializer.text("Наличными");  // Консигнация, Перечислением, Взаимозачет
            serializer.endTag(null, "CREDIT");
            serializer.startTag(null, "SKLAD");
            serializer.text("склад город Бишкек");
            serializer.endTag(null, "SKLAD");
            serializer.startTag(null, "CENA_PRICE");
            serializer.text("Цена для агентов"); // Цена брака, Цена в розницу, Цена для народных
            serializer.endTag(null, "CENA_PRICE");
            serializer.startTag(null, "Coment");
            serializer.text("Дата отгрузки 04.12.2021; скидка 10%; Итого: 22222"); // Дата отгрузки 04.12.2021; скидка 10%
            serializer.endTag(null, "Coment");

            SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_rn_db.db3", MODE_PRIVATE, null);
            String query = "SELECT base_RN_All.Kod_RN, base_RN_All.Klients, base_RN_All.UID_Klients, " +
                    "base_RN_All.Data, base_RN.Name, base_RN.Kod_Univ, base_RN.Kol, base_RN.Cena, " +
                    "base_RN.Summa, base_RN.Skidka, base_RN.Itogo\n" +
                    "FROM base_RN_All\n" +
                    "JOIN base_RN ON base_RN_All.Kod_RN=base_RN.Kod_RN\n" +
                    "WHERE base_RN_All.Kod_RN = '"+kod_Rn+"'";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(cursor.getColumnIndex("Name"));
                String kod_univ = cursor.getString(cursor.getColumnIndex("Kod_Univ"));
                String kol = cursor.getString(cursor.getColumnIndex("Kol"));
                String cena = cursor.getString(cursor.getColumnIndex("Cena"));
                String summa = cursor.getString(cursor.getColumnIndex("Summa"));


                // Первая строка заказа
                serializer.startTag(null, "POS");
                serializer.startTag(null, "NAME");
                serializer.text(name);
                Log.e(this.getLocalClassName(), name);
                serializer.endTag(null, "NAME");
                serializer.startTag(null, "KODUNIV");
                serializer.text(kod_univ);
                serializer.endTag(null, "KODUNIV");
                serializer.startTag(null, "KOL_COUNT");
                serializer.text(kol);
                serializer.endTag(null, "KOL_COUNT");
                serializer.startTag(null, "PRICE");
                serializer.text(cena);
                serializer.endTag(null, "PRICE");
                serializer.startTag(null, "SUMMA");
                serializer.text(summa);
                serializer.endTag(null, "SUMMA");
                serializer.endTag(null, "POS");
                // Конец строки

            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        serializer.endTag(null, "SyncTableNomenclatura");
        serializer.endTag(null, "Array_Order");



    }
        catch (Exception e)
        {

        }

    }


    protected void XML_Array_Name(String kod_Rn, String Data_Start, String Data_End)
    {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_rn_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base_RN_All.Kod_RN, base_RN_All.Klients, base_RN_All.UID_Klients, base_RN_All.Data\n" +
                "FROM base_RN_All\n" +
                "WHERE base_RN_All.Data BETWEEN '2021-11-11' AND '2021-11-21';";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String Kod_RN = cursor.getString(cursor.getColumnIndex("Kod_RN"));
            String Klients = cursor.getString(cursor.getColumnIndex("Klients"));
            String UID_Klients = cursor.getString(cursor.getColumnIndex("UID_Klients"));
            String Data = cursor.getString(cursor.getColumnIndex("Data"));
            XML_Array(Kod_RN, Klients, UID_Klients, Data);
            cursor.moveToNext();
        }

        cursor.close();
        db.close();
    }


}















                       /* // Первая строка заказа
                        serializer.startTag(null, "POS");
                        serializer.startTag(null, "NAME");
                        serializer.text("MN FRESH ОРИГ 450 МЛ  2648");
                        serializer.endTag(null, "NAME");
                        serializer.startTag(null, "KODUNIV");
                        serializer.text("00000255/00000299/CS000110");
                        serializer.endTag(null, "KODUNIV");
                        serializer.startTag(null, "KOL_COUNT");
                        serializer.text("12");
                        serializer.endTag(null, "KOL_COUNT");
                        serializer.startTag(null, "PRICE");
                        serializer.text("110");
                        serializer.endTag(null, "PRICE");
                        serializer.startTag(null, "SUMMA");
                        serializer.text("1320");
                        serializer.endTag(null, "SUMMA");
                        serializer.endTag(null, "POS");
                        // Конец строки

                        // Вторая строка заказа
                        serializer.startTag(null, "POS");
                        serializer.startTag(null, "NAME");
                        serializer.text("MN FRESH ЛИМ 450 МЛ  2655");
                        serializer.endTag(null, "NAME");
                        serializer.startTag(null, "KODUNIV");
                        serializer.text("00000255/00000299/CS000108");
                        serializer.endTag(null, "KODUNIV");
                        serializer.startTag(null, "KOL_COUNT");
                        serializer.text("12");
                        serializer.endTag(null, "KOL_COUNT");
                        serializer.startTag(null, "PRICE");
                        serializer.text("110");
                        serializer.endTag(null, "PRICE");
                        serializer.startTag(null, "SUMMA");
                        serializer.text("1320");
                        serializer.endTag(null, "SUMMA");
                        serializer.endTag(null, "POS");
                        // Конец строки


                        */