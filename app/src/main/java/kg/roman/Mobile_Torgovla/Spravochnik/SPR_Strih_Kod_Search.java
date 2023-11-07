package kg.roman.Mobile_Torgovla.Spravochnik;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import kg.roman.Mobile_Torgovla.DB_NewSV.DbContract_Nomeclature;
import kg.roman.Mobile_Torgovla.DB_NewSV.DbContract_Otchet_Ostatok;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Otchet_Ostatok;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Otchet_Ostatok;
import kg.roman.Mobile_Torgovla.Permission.PrefActivity;
import kg.roman.Mobile_Torgovla.R;

public class SPR_Strih_Kod_Search extends AppCompatActivity {

    public ArrayList<ListAdapterSimple_Otchet_Ostatok> adapter_listview = new ArrayList<ListAdapterSimple_Otchet_Ostatok>();
    public ListAdapterAde_Otchet_Ostatok adapterPriceClients;

    public Button button_Scan, button_Scan_QR, button_Add, button_Refresh, button_Left, button_Right;
    public ImageView imageview;
    public TextView textView_name, textView_cena, textView_kolbox,
            textView_strih, textView_group, textView_uid, textView_univ, textView_ost, textView_brend;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    public String[] mass_table_db;
    public String contents, image;
    public Context context;
    public Context context_Activity;
    public String[] mass_name_title;
    public ListView listView;
    public ArrayAdapter<String> adapter;
    public Integer KK, max_count;
    public Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.strih_kod_search);

        context_Activity = SPR_Strih_Kod_Search.this;
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.user_pda);
        getSupportActionBar().setTitle("Сканер штрих-кода");
        contents = "";
        imageview = (ImageView) findViewById(R.id.image_sh);
        textView_name = (TextView) findViewById(R.id.textView_Name);
        listView = (ListView) findViewById(R.id.listview);

        mass_name_title = new String[9];
        for (int i = 0; i < mass_name_title.length; i++) {
            mass_name_title[i] = "";
        }

        Button_Group();


      /*  SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
       // Log.e("SPR_Strih/", contents);
        String query = "SELECT base10_nomeclature.name, base10_nomeclature.brends, base10_nomeclature.p_group, base10_nomeclature.kod, base10_nomeclature.count, \n" +
                "base5_price.cena, base10_nomeclature.image, base10_nomeclature.kolbox, \n" +
                "base10_nomeclature.strih, base11_new_strih.strih, base10_nomeclature.kod_univ, base10_nomeclature.work, base10_nomeclature.koduid\n" +
                "FROM base10_nomeclature \n" +
                "LEFT JOIN base5_price ON base10_nomeclature.koduid = base5_price.koduid\n" +
                "LEFT JOIN base11_new_strih ON base10_nomeclature.koduid = base11_new_strih.koduid\n" +
                "WHERE base10_nomeclature.strih LIKE \"%" + contents + "%\" OR base11_new_strih.strih LIKE \"%" + contents + "%\"";
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();*/






    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent3 = new Intent(context_Activity, PrefActivity.class);
            startActivity(intent3);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Запускаемм сканер штрих кода:
    public void scanBar() {
        try {

            // Запускаем переход на com.google.zxing.client.android.SCAN с помощью intent:
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {

            // Предлагаем загрузить с Play Market:
            showDialog(SPR_Strih_Kod_Search.this, "Сканнер не найден", "Установить сканер с Play Market?", "Да", "Нет").show();
        }
    }

    // Запуск сканера qr-кода:
    public void scanQR() {
        try {

            // Запускаем переход на com.google.zxing.client.android.SCAN с помощью intent:
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {

            // Предлагаем загрузить с Play Market:
            showDialog(SPR_Strih_Kod_Search.this, "Сканнер не найден", "Установить сканер с Play Market?", "Да", "Нет").show();
        }
    }

    // alert dialog для перехода к загрузке приложения сканера:
    private static AlertDialog showDialog(final Activity act, CharSequence title,
                                          CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

                // Ссылка поискового запроса для загрузки приложения:
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    // Обрабатываем результат, полученный от приложения сканера:
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            contents = "";
            mass_table_db = getResources().getStringArray(R.array.mass_table_db);
            if (requestCode == 0) {
                if (resultCode == RESULT_OK) {

                    // Получаем данные после работы сканера и выводим их в Toast сообщении:
                    contents = intent.getStringExtra("SCAN_RESULT");
                    String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                    Toast toast = Toast.makeText(this, "Содержание: " + contents + " Формат: " + format, Toast.LENGTH_SHORT);
                    toast.show();

                }
            }
            // List(contents);
            List_2(contents);
            adapter = new ArrayAdapter<>(SPR_Strih_Kod_Search.this,
                    android.R.layout.simple_list_item_1, mass_name_title);
            listView.setAdapter(adapter);
            Image_picasso(mass_name_title[6].replace("Код image:", ""));
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка штрих-кода!", Toast.LENGTH_SHORT).show();
            Log.e("SPR_Strih/", "Ошибка штрих-кода!");

        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    // Поиск по базе заданного contents штрих-кода
    protected void List(String contents) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base10_nomeclature.name, base10_nomeclature.brends, base10_nomeclature.p_group, base10_nomeclature.kod, SUM(base4_ost.count) AS 'count', \n" +
                "base5_price.cena, base4_ost.sklad, base10_nomeclature.image, base10_nomeclature.kolbox, \n" +
                "base10_nomeclature.strih, base10_nomeclature.kod_univ, base10_nomeclature.work, base10_nomeclature.koduid\n" +
                "FROM base10_nomeclature\n" +
                "LEFT JOIN base4_ost ON base10_nomeclature.koduid = base4_ost.koduid\n" +
                "LEFT JOIN base5_price ON base10_nomeclature.koduid = base5_price.koduid\n" +
                "LEFT JOIN base11_new_strih ON base10_nomeclature.koduid = base11_new_strih.koduid\n" +
                "WHERE (base10_nomeclature.strih LIKE \"%" + contents + "%\" OR base11_new_strih.strih LIKE \"%" + contents + "%\")\n" +
                "GROUP BY base10_nomeclature.name\n" +
                "ORDER BY base10_nomeclature.brends, base10_nomeclature.p_group;";
        final Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            String kod = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KOD));
            String name = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_NAME));
            String cena = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_CENA));
            String shtrih = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_STRIH));
            String koduniv = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KODUNIV));
            String koduid = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KODUID));
            String ostatok = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_OSTATOK));
            String image_view = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_IMAGE));
            String group = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_GROUP));
            String kolbox = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KOLBOX));
            String brends = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_BRENDS));
            textView_name.setText(name);

            mass_name_title[0] = "Цена: " + cena + "c";
            mass_name_title[1] = "Остаток: " + ostatok + "шт";
            mass_name_title[2] = "Кол-во в кор: " + kolbox + "шт";
            mass_name_title[3] = "Штрих-код: " + shtrih;
            mass_name_title[4] = "Бренд: " + brends;
            mass_name_title[5] = "Группа: " + group;
            mass_name_title[6] = "Код image:" + image_view;
            mass_name_title[7] = "Код Univ:\n" + koduniv;
            mass_name_title[8] = "Код UID:\n" + koduid;


            for (int i = 0; i < mass_name_title.length; i++) {
                Log.e("Штрих-код", name + ", " + shtrih + mass_name_title[i]);
            }

        } else {
            listView.setEmptyView(null);
            textView_name.setText("");
            for (int i = 0; i < mass_name_title.length; i++) {
                mass_name_title[i] = "";
            }
            Image_picasso("no_image");
            Toast toast2 = Toast.makeText(SPR_Strih_Kod_Search.this, "Штрих-код не найдет!", Toast.LENGTH_SHORT);
            toast2.show();
        }
        cursor.close();
        db.close();
    }

    protected void List_2(String str) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
        Log.e("SPR_Strih/", str);
        String query = "SELECT COUNT(*) \n" +
                "FROM base10_nomeclature \n" +
                "LEFT JOIN base11_new_strih ON base10_nomeclature.koduid = base11_new_strih.koduid \n" +
                "WHERE base10_nomeclature.strih = '" + str + "' OR base11_new_strih.strih = '" + str + "';";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (Integer.parseInt(cursor.getString(0)) >= 1) {
            Log.e("SPR_Strih/", cursor.getString(0));
            max_count = Integer.parseInt(cursor.getString(0));
           /* button_Left.setVisibility(View.VISIBLE);
            button_Right.setVisibility(View.VISIBLE);*/
            Loading_DB();
        } else {
            Log.e("SPR_Strih/", cursor.getString(0));
            /*button_Left.setVisibility(View.GONE);
            button_Right.setVisibility(View.GONE);*/
        }
        cursor.close();
        db.close();
    }

    // Группа кнопок с функциями
    public void Button_Group() {
        button_Scan = (Button) findViewById(R.id.button_scan);
        button_Scan_QR = (Button) findViewById(R.id.button_scan_qr);
        button_Add = (Button) findViewById(R.id.button_add);
        button_Refresh = (Button) findViewById(R.id.button_xml);
        button_Left = (Button) findViewById(R.id.btn_left);
        button_Right = (Button) findViewById(R.id.btn_right);
        KK = 1;

        button_Right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("SPR_Strih/", "Вправо " + KK);

                while (cursor.isAfterLast() == false) {
                    String kod = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KOD));
                    String name = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_NAME));
                    String cena = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_CENA));
                    String shtrih = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_STRIH));
                    String koduniv = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KODUNIV));
                    String koduid = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KODUID));
                    String ostatok = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_OSTATOK));
                    String image_view = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_IMAGE));
                    String group = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_GROUP));
                    String kolbox = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KOLBOX));
                    String brends = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_BRENDS));
                    textView_name.setText(name);
                    mass_name_title[0] = "Цена: " + cena + "c";
                    mass_name_title[1] = "Остаток: " + ostatok + "шт";
                    mass_name_title[2] = "Кол-во в кор: " + kolbox + "шт";
                    mass_name_title[3] = "Штрих-код: " + shtrih;
                    mass_name_title[4] = "Бренд: " + brends;
                    mass_name_title[5] = "Группа: " + group;
                    mass_name_title[6] = "Код image:" + image_view;
                    mass_name_title[7] = "Код Univ:\n" + koduniv;
                    mass_name_title[8] = "Код UID:\n" + koduid;
                }
                cursor.moveToNext();
                adapter = new ArrayAdapter<>(SPR_Strih_Kod_Search.this, android.R.layout.simple_list_item_1, mass_name_title);
                listView.setAdapter(adapter);
                Image_picasso(mass_name_title[6].replace("Код image:", ""));
                cursor.close();
                // db.close();
            }
        });



        button_Left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("SPR_Strih/", "Влево" + KK);

                SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
                Log.e("SPR_Strih/", contents);
                String query = "SELECT base10_nomeclature.name, base10_nomeclature.brends, base10_nomeclature.p_group, base10_nomeclature.kod, base10_nomeclature.count, \n" +
                        "base5_price.cena, base10_nomeclature.image, base10_nomeclature.kolbox, \n" +
                        "base10_nomeclature.strih, base11_new_strih.strih, base10_nomeclature.kod_univ, base10_nomeclature.work, base10_nomeclature.koduid\n" +
                        "FROM base10_nomeclature \n" +
                        "LEFT JOIN base5_price ON base10_nomeclature.koduid = base5_price.koduid\n" +
                        "LEFT JOIN base11_new_strih ON base10_nomeclature.koduid = base11_new_strih.koduid\n" +
                        "WHERE base10_nomeclature.strih LIKE \"%" + contents + "%\" OR base11_new_strih.strih LIKE \"%" + contents + "%\"";
                final Cursor cursor = db.rawQuery(query, null);

                if (KK > 1) {
                    KK--;
                    cursor.moveToPosition(KK);
                    String kod = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KOD));
                    String name = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_NAME));
                    String cena = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_CENA));
                    String shtrih = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_STRIH));
                    String koduniv = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KODUNIV));
                    String koduid = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KODUID));
                    String ostatok = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_OSTATOK));
                    String image_view = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_IMAGE));
                    String group = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_GROUP));
                    String kolbox = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KOLBOX));
                    String brends = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_BRENDS));
                    textView_name.setText(name);
                    mass_name_title[0] = "Цена: " + cena + "c";
                    mass_name_title[1] = "Остаток: " + ostatok + "шт";
                    mass_name_title[2] = "Кол-во в кор: " + kolbox + "шт";
                    mass_name_title[3] = "Штрих-код: " + shtrih;
                    mass_name_title[4] = "Бренд: " + brends;
                    mass_name_title[5] = "Группа: " + group;
                    mass_name_title[6] = "Код image:" + image_view;
                    mass_name_title[7] = "Код Univ:\n" + koduniv;
                    mass_name_title[8] = "Код UID:\n" + koduid;
                  /*  for (int i = 0; i < mass_name_title.length; i++) {
                        Log.e("Штрих-код", name + ", " + shtrih + mass_name_title[i]);
                    }*/

                }

                cursor.close();
                db.close();
                adapter = new ArrayAdapter<>(SPR_Strih_Kod_Search.this, android.R.layout.simple_list_item_1, mass_name_title);
                listView.setAdapter(adapter);
                Image_picasso(mass_name_title[6].replace("Код image:", ""));
            }
        });

        try {
            button_Scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scanBar();
                }
            });

            button_Refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*adapter = new ArrayAdapter<>(SPR_Strih_Kod_Search.this,
                            android.R.layout.simple_list_item_1, mass_name_title);
                    listView.setAdapter(adapter);
                    Image_picasso(mass_name_title[6].replace("Код image:", ""));*/

                }
            });

            button_Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SPR_Strih_Kod_Search.this, SPR_Strih_Kod_Add.class);
                    startActivity(intent);
                    finish();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка группы кнопок!", Toast.LENGTH_SHORT).show();
            Log.e("SPR_Strih/", "Ошибка группы кнопок!");
        }


    }

    // Загрузка картинок через Picaso
    protected void Image_picasso(String image_data) {
        try {
            File imagePath_SD = new File("/sdcard/Price/" + image_data + ".png");//путь к изображению
            Uri imagePath_phone = Uri.parse("android.resource://kg.price_list.roman_kerkin.sunbell_price/drawable/" + image_data);

            if (imagePath_SD.exists()) {
                Picasso.get()
                        .load(imagePath_SD)
                        .into(imageview); //ссылка на ImageView

            } else {
                Picasso.get()
                        .load(imagePath_phone)
                        .into(imageview); //ссылка на ImageView
            }


        } catch (Exception e) {
            Log.e("Image_Error", "Нет картинов в ресурсах");
        }
    }

    protected void Loading_DB() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
        Log.e("SPR_Strih/", contents);
        String query = "SELECT base10_nomeclature.name, base10_nomeclature.brends, base10_nomeclature.p_group, base10_nomeclature.kod, base10_nomeclature.count, \n" +
                "base5_price.cena, base10_nomeclature.image, base10_nomeclature.kolbox, \n" +
                "base10_nomeclature.strih, base11_new_strih.strih, base10_nomeclature.kod_univ, base10_nomeclature.work, base10_nomeclature.koduid\n" +
                "FROM base10_nomeclature \n" +
                "LEFT JOIN base5_price ON base10_nomeclature.koduid = base5_price.koduid\n" +
                "LEFT JOIN base11_new_strih ON base10_nomeclature.koduid = base11_new_strih.koduid\n" +
                "WHERE base10_nomeclature.strih LIKE \"%" + contents + "%\" OR base11_new_strih.strih LIKE \"%" + contents + "%\"";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String kod = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KOD));
        String name = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_NAME));
        String cena = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_CENA));
        String shtrih = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_STRIH));
        String koduniv = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KODUNIV));
        String koduid = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KODUID));
        String ostatok = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_OSTATOK));
        String image_view = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_IMAGE));
        String group = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_GROUP));
        String kolbox = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KOLBOX));
        String brends = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_BRENDS));
        textView_name.setText(name);
        mass_name_title[0] = "Цена: " + cena + "c";
        mass_name_title[1] = "Остаток: " + ostatok + "шт";
        mass_name_title[2] = "Кол-во в кор: " + kolbox + "шт";
        mass_name_title[3] = "Штрих-код: " + shtrih;
        mass_name_title[4] = "Бренд: " + brends;
        mass_name_title[5] = "Группа: " + group;
        mass_name_title[6] = "Код image:" + image_view;
        mass_name_title[7] = "Код Univ:\n" + koduniv;
        mass_name_title[8] = "Код UID:\n" + koduid;
        for (int i = 0; i < mass_name_title.length; i++) {
            Log.e("Штрих-код", name + ", " + shtrih + mass_name_title[i]);
        }

        cursor.close();
        db.close();
    }

    protected void LISFT() {

    }


    protected void Loading_Ostatki() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base10_nomeclature.name, base10_nomeclature.brends, " +
                "base10_nomeclature.p_group, base10_nomeclature.kod, base10_nomeclature.kolbox, \n" +
                "base10_nomeclature.cena, base10_nomeclature.image, base10_nomeclature.strih, base10_nomeclature.count, " +
                "base10_nomeclature.kod_univ, base10_nomeclature.work, base10_nomeclature.koduid\n" +
                "FROM base10_nomeclature";

        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndex(DbContract_Otchet_Ostatok.TableUser.COLUMN_NAME));
            String count = cursor.getString(cursor.getColumnIndex(DbContract_Otchet_Ostatok.TableUser.COLUMN_COUNT));
            String brends = cursor.getString(cursor.getColumnIndex(DbContract_Otchet_Ostatok.TableUser.COLUMN_BRENDS));
            String cena = cursor.getString(cursor.getColumnIndex(DbContract_Otchet_Ostatok.TableUser.COLUMN_CENA));
            String image = cursor.getString(cursor.getColumnIndex(DbContract_Otchet_Ostatok.TableUser.COLUMN_IMAGE));
            //   String sklad_ = cursor.getString(cursor.getColumnIndex(DbContract_Otchet_Ostatok.TableUser.COLUMN_SKLAD));
            adapter_listview.add(new ListAdapterSimple_Otchet_Ostatok(name, count + "шт", brends, cena, "", "", "", "", image));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    protected void Loading_Db_Nomencalture(String contents) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
        String query = " SELECT base10_nomeclature.kod, base10_nomeclature.name, " +
                "base10_nomeclature.kolbox, base10_nomeclature.cena, " +
                "base10_nomeclature.image, base10_nomeclature.strih, " +
                "base10_nomeclature.kod_univ, \n" +
                "         base10_nomeclature.koduid, base10_nomeclature.p_group, " +
                "base10_nomeclature.brends, base10_nomeclature.count, " +
                "base10_nomeclature.work \n" +
                "                FROM base10_nomeclature";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String kod = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KOD));
            String name = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_NAME));
            String cena = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_CENA));
            String shtrih = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_STRIH));
            String koduniv = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KODUNIV));
            String koduid = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KODUID));
            String ostatok = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_OSTATOK));
            String image = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_IMAGE));
            String group = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_GROUP));
            String kolbox = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KOLBOX));
            String work = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_WORK));
            String brends = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_BRENDS));

            if (contents.equals(shtrih)) {
                Toast.makeText(this, "Есть совпадение ", Toast.LENGTH_LONG).show();

                textView_name.setText(name);
                textView_cena.setText(cena + "c");
                textView_kolbox.setText(kolbox + "шт");
                textView_strih.setText(shtrih);
                textView_uid.setText(koduid);
                textView_univ.setText(koduniv);
                textView_group.setText(group);
                textView_ost.setText(ostatok + "шт");
                textView_brend.setText(brends);
                try {
                    File imagePath_SD = new File("/sdcard/Price/" + image + ".png");//путь к изображению
                    Uri imagePath_phone = Uri.parse("android.resource://kg.price_list.roman_kerkin.sunbell_price/drawable/" + image);

                    if (imagePath_SD.exists()) {
                        Picasso.get()
                                .load(imagePath_SD)
                                .into(imageview); //ссылка на ImageView

                    } else {
                        Picasso.get()
                                .load(imagePath_phone)
                                .into(imageview); //ссылка на ImageView
                    }


                } catch (Exception e) {
                    Log.e("Image_Error", "Нет картинов в ресурсах");
                }

                Log.e("Штрих-код", name + ", " + shtrih);
                cursor.moveToNext();
            } else {
                cursor.moveToNext();
            }

        }
        cursor.close();
        db.close();
    }

}
