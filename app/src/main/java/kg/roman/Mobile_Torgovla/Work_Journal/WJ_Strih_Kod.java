package kg.roman.Mobile_Torgovla.Work_Journal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import kg.roman.Mobile_Torgovla.DB_NewSV.DbContract_Nomeclature;
import kg.roman.Mobile_Torgovla.FormaZakaza.WJ_Forma_Zakaza_L2;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_WJ_Scan;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_WJ_Zakaz;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_WJ_Scan;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_WJ_Zakaz;
import kg.roman.Mobile_Torgovla.Permission.PrefActivity;
import kg.roman.Mobile_Torgovla.R;

public class WJ_Strih_Kod extends AppCompatActivity {

    public ArrayList<ListAdapterSimple_WJ_Scan> scan = new ArrayList<ListAdapterSimple_WJ_Scan>();
    public ListAdapterAde_WJ_Scan adapterPriceClients;

    public ArrayList<ListAdapterSimple_WJ_Zakaz> list_zakaz = new ArrayList<ListAdapterSimple_WJ_Zakaz>();
    public ListAdapterAde_WJ_Zakaz adapterPriceClients_listzakaz;

    public Button button_scan, button_add;
    public ListView listView_scan, listView_add;
    public EditText edt_skidka, edt_kol;
    public TextView tvw_summa, tvw_itogo;

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    public String[] mass_table_db;
    public String contents;
    public Context context;
    public Toolbar toolbar;
    public Context context_Activity;
    public NavigationView navigationView;
    public SharedPreferences sp;
    public String PEREM_Agent, PEREM_KAgent, PEREM_KAgent_UID, PEREM_Vrema, PEREM_Data, PEREM_RNKod, Summa;
    public String str_name, str_kod, str_cena, str_ostatok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_forma_zakaz_scan);
       /* toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        context_Activity = WJ_Strih_Kod.this;

        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        PEREM_Agent = sp.getString("PEREM_Agent", "0");
        PEREM_KAgent = sp.getString("PEREM_KAgent", "0");
        PEREM_KAgent_UID = sp.getString("PEREM_KAgent_UID", "0");
        PEREM_Vrema = sp.getString("PEREM_Vrema", "0");
        PEREM_Data = sp.getString("PEREM_Data", "0");
        PEREM_RNKod = sp.getString("PEREM_RNKod", "0");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.user_pda);
        getSupportActionBar().setTitle("Сканер штрих-кода");
        // getSupportActionBar().setSubtitle("Аптеки");

        button_scan = (Button) findViewById(R.id.wj_btn_scan);
        button_add = (Button) findViewById(R.id.wj_btn_add);
        listView_scan = (ListView) findViewById(R.id.list_scan1);
        listView_add = (ListView) findViewById(R.id.list_scan2);
        edt_skidka = (EditText) findViewById(R.id.wj_edt_skidka);
        edt_kol = (EditText) findViewById(R.id.wj_edt_kol);
        tvw_summa = (TextView) findViewById(R.id.wj_tvw_summa);
        tvw_itogo = (TextView) findViewById(R.id.wj_tvw_itogo);


        button_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBar();
                //  edt_kol.requestFocus();
                edt_kol.setFocusable(true);
                edt_kol.setText("");
                edt_skidka.setText("");
            }
        });
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tvw_summa.getText().toString().equals("")) {
                    scan.clear();
                    DB_Add();
                    adapterPriceClients = new ListAdapterAde_WJ_Scan(context_Activity, scan);
                    adapterPriceClients.notifyDataSetChanged();
                    listView_scan.setAdapter(adapterPriceClients);
                    edt_kol.setText("");
                    edt_skidka.setText("");

                    list_zakaz.clear();
                    Loading_Adapter();
                    adapterPriceClients_listzakaz = new ListAdapterAde_WJ_Zakaz(context_Activity, list_zakaz);
                    adapterPriceClients_listzakaz.notifyDataSetChanged();
                    listView_add.setAdapter(adapterPriceClients_listzakaz);

                } else {
                    Toast.makeText(context_Activity, "введите кол-во", Toast.LENGTH_SHORT).show();
                }

            }
        });

        edt_kol.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!scan.isEmpty() & !edt_kol.getText().toString().equals("")) {

                    if (Integer.valueOf(edt_kol.getText().toString()) <= Integer.valueOf(str_ostatok)) {

                        Integer sum = Integer.parseInt(str_cena) * Integer.parseInt(edt_kol.getText().toString());
                        String Format = new DecimalFormat("#00.00").format(sum);
                        tvw_summa.setText(Format);

                    } else {
                        Toast.makeText(context_Activity, "кол-во превышает остаток", Toast.LENGTH_SHORT).show();
                        edt_kol.setText(str_ostatok);
                        edt_kol.setSelection(edt_kol.getText().length());
                        Integer sum = Integer.parseInt(str_cena) * Integer.parseInt(edt_kol.getText().toString());
                        String Format = new DecimalFormat("#00.00").format(sum);
                        tvw_summa.setText(Format);
                    }
                } else {
                    tvw_summa.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_skidka.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!scan.isEmpty() & !edt_skidka.getText().toString().equals("") & !edt_kol.getText().toString().equals("")) {

                    Double d1, d2, d3;
                    d1 = Double.parseDouble(str_cena);
                    d2 = Double.parseDouble(edt_skidka.getText().toString());
                    d3 = Double.parseDouble(edt_kol.getText().toString());
                    Double sum = (d1 - (d1 * (d2 / 100))) * d3;
                    String Format = new DecimalFormat("#00.00").format(sum);
                    tvw_itogo.setText(Format);
                    Log.e("Suncape_Log_it:", sum.toString());
                    Log.e("Suncape_Log_it:", Format);
                } else tvw_itogo.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza_L2.class);
        startActivity(intent);
        finish();
        // super.onBackPressed();
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
            showDialog(WJ_Strih_Kod.this, "Сканнер не найден", "Установить сканер с Play Market?", "Да", "Нет").show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
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

        scan.clear();
        Loading_Db_Nomencalture(contents);
        adapterPriceClients = new ListAdapterAde_WJ_Scan(context_Activity, scan);
        adapterPriceClients.notifyDataSetChanged();
        listView_scan.setAdapter(adapterPriceClients);

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
            String name = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_NAME));
            String cena = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_CENA));
            String kod_univ = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KODUNIV));
            String kod = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KOD));
            String shtrih = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_STRIH));
            String koduid = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KODUID));
            String ostatok = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_OSTATOK));
            String image = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_IMAGE));
            String group = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_GROUP));
            String kolbox = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_KOLBOX));
            String work = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_WORK));
            String brends = cursor.getString(cursor.getColumnIndex(DbContract_Nomeclature.TableUser.COLUMN_BRENDS));

            if (contents.equals(shtrih)) {
                Toast.makeText(this, "Есть совпадение ", Toast.LENGTH_LONG).show();
                scan.add(new ListAdapterSimple_WJ_Scan(name, kod_univ, cena, ostatok, shtrih, image));
                str_name = name;
                str_kod = kod_univ;
                str_cena = cena;
                str_ostatok = ostatok;
                cursor.moveToNext();
            } else {
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();
    }  // Загрузка номенклатуры по брендам и по группам

    protected void DB_Add() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_rn_db.db3", MODE_PRIVATE, null);

        ContentValues localContentValues = new ContentValues();
        localContentValues.put("Agent", PEREM_Agent);
        localContentValues.put("Kod_RN", PEREM_RNKod);
        localContentValues.put("Klients", PEREM_KAgent);
        localContentValues.put("UID_Klients", PEREM_KAgent_UID);
        localContentValues.put("Vrema", PEREM_Vrema);
        localContentValues.put("Data", PEREM_Data);
        localContentValues.put("Kod_Univ", str_kod);
        localContentValues.put("Name", str_name);
        localContentValues.put("Kol", edt_kol.getText().toString());
        localContentValues.put("Cena", str_cena);
        localContentValues.put("Summa", tvw_summa.getText().toString().replaceAll(",", "."));
        localContentValues.put("Skidka", edt_skidka.getText().toString());
        localContentValues.put("Itogo", tvw_itogo.getText().toString().replaceAll(",", "."));


    /*    if (!skidka.isEmpty())
        {
            String Format_summa = new DecimalFormat("#00.00").format(summaSK);

        } else
        {
            localContentValues.put("Skidka", "0");
            localContentValues.put("Itogo", summa);
        }*/

        Log.e("WJZakS2=", localContentValues.toString());
        db.insert("base_RN", null, localContentValues);
        db.close();

        Toast.makeText(context_Activity, "Товар добавлен!", Toast.LENGTH_SHORT).show();
    }

    protected void Loading_Adapter() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_rn_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base_RN.Agent, base_RN.Kod_RN, base_RN.Klients, base_RN.UID_Klients, " +
                "base_RN.Vrema, base_RN.Data, base_RN.Kod_Univ, base_RN.Name, SUM(base_RN.Kol) AS \"Kol\", " +
                "base_RN.Cena, SUM(base_RN.Summa) AS \"Summa\", base_RN.Skidka, base_RN.Cena_SK, SUM(base_RN.Itogo) AS \"Itogo\"\n" +
                "FROM base_RN WHERE base_RN.Kod_RN LIKE '%" + PEREM_RNKod + "%' GROUP BY base_RN.Kod_Univ";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String kod_univ = cursor.getString(cursor.getColumnIndex("Kod_Univ"));
            String name = cursor.getString(cursor.getColumnIndex("Name"));
            String kol = cursor.getString(cursor.getColumnIndex("Kol"));
            String cena = cursor.getString(cursor.getColumnIndex("Cena"));
            String summa = cursor.getString(cursor.getColumnIndex("Summa"));
            String skidka = cursor.getString(cursor.getColumnIndex("Skidka"));
            String itogo = cursor.getString(cursor.getColumnIndex("Itogo"));
            String image = cursor.getString(cursor.getColumnIndex("Image"));
            list_zakaz.add(new ListAdapterSimple_WJ_Zakaz(name, kod_univ, kol, cena, null, summa, skidka, itogo, image));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

}
