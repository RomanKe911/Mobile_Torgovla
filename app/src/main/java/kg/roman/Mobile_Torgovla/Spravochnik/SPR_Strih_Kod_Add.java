package kg.roman.Mobile_Torgovla.Spravochnik;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_STR_Search;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_STR_Search;
import kg.roman.Mobile_Torgovla.R;

import static kg.roman.Mobile_Torgovla.Spravochnik.SPR_Strih_Kod_Search.ACTION_SCAN;

public class SPR_Strih_Kod_Add extends AppCompatActivity {

    public ArrayList<ListAdapterSimple_STR_Search> adapter = new ArrayList<ListAdapterSimple_STR_Search>();
    public ListAdapterAde_STR_Search adapterPriceClients;

    public Button button_Scan, button_Scan_QR, button_Add, button_Refresh;
    public ListView listView;
    public EditText editText;
    public androidx.appcompat.app.AlertDialog.Builder dialog_cancel;
    public Context context_Activity;
    public String contents, new_strih;
    public String new_brends, new_p_group, new_kod, new_name, new_kolbox, new_image, new_cena, new_kod_univ, new_koduid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.strih_kod_add);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.user_pda);
        getSupportActionBar().setTitle("Сканер штрих-кода");
        getSupportActionBar().setSubtitle("");
        new_name = "";
        context_Activity = SPR_Strih_Kod_Add.this;

        listView = (ListView) findViewById(R.id.listview);
        editText = (EditText) findViewById(R.id.avtoris_EditPinCode);

        adapter.clear();
        ListAdapter();
        adapterPriceClients = new ListAdapterAde_STR_Search(SPR_Strih_Kod_Add.this, adapter);
        adapterPriceClients.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.clear();
                ListAdapter_Search(editText.getText().toString());
                adapterPriceClients = new ListAdapterAde_STR_Search(SPR_Strih_Kod_Add.this, adapter);
                adapterPriceClients.notifyDataSetChanged();
                listView.setAdapter(adapterPriceClients);

            }
        });
        Button_Group();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
                Log.e("SPR_Strih/", (((TextView) view.findViewById(R.id.tvw_NT8)).getText().toString()));
                String query = "SELECT COUNT(*) FROM base11_new_strih WHERE base11_new_strih.koduid = '" + ((TextView) view.findViewById(R.id.tvw_NT8)).getText().toString() + "'";
                final Cursor cursor = db.rawQuery(query, null);
                cursor.moveToFirst();
                if (Integer.parseInt(cursor.getString(0)) == 0) {
                    Toast.makeText(SPR_Strih_Kod_Add.this, "Выбрана позиция!", Toast.LENGTH_SHORT).show();
                    Log.e("SPR_Strih/", cursor.getString(0)+" = "+((TextView) view.findViewById(R.id.tvw_G_ost_name)).getText().toString());
                    new_brends = ((TextView) view.findViewById(R.id.tvw_NT1)).getText().toString();
                    new_p_group = ((TextView) view.findViewById(R.id.tvw_NT2)).getText().toString();
                    new_kod = ((TextView) view.findViewById(R.id.tvw_NT3)).getText().toString();
                    new_name = ((TextView) view.findViewById(R.id.tvw_G_ost_name)).getText().toString();
                    new_kolbox = ((TextView) view.findViewById(R.id.tvw_NT4)).getText().toString();
                    new_cena = ((TextView) view.findViewById(R.id.tvw_NT5)).getText().toString();
                    new_image = ((TextView) view.findViewById(R.id.tvw_image)).getText().toString();
                    new_kod_univ = ((TextView) view.findViewById(R.id.tvw_NT7)).getText().toString();
                    new_koduid = ((TextView) view.findViewById(R.id.tvw_NT8)).getText().toString();
                } else {
                    Toast.makeText(SPR_Strih_Kod_Add.this, "Запись существует", Toast.LENGTH_SHORT).show();
                    Log.e("SPR_Strih/", cursor.getString(0)+" = "+((TextView) view.findViewById(R.id.tvw_G_ost_name)).getText().toString());
                    new_brends = "";
                    new_p_group = "";
                    new_kod = "";
                    new_name = "";
                    new_kolbox = "";
                    new_cena = "";
                    new_image = "";
                    new_kod_univ = "";
                    new_koduid = "";
                }

                cursor.close();
                db.close();
            }
        });

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
            showDialog(SPR_Strih_Kod_Add.this, "Сканнер не найден", "Установить сканер с Play Market?", "Да", "Нет").show();
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
            if (requestCode == 0) {
                if (resultCode == RESULT_OK) {

                    // Получаем данные после работы сканера и выводим их в Toast сообщении:
                    contents = intent.getStringExtra("SCAN_RESULT");
                    String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                    Toast toast = Toast.makeText(this, "Содержание: " + contents + " Формат: " + format, Toast.LENGTH_SHORT);
                    toast.show();

                }
            }
            getSupportActionBar().setSubtitle(contents);
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка штрих-кода!", Toast.LENGTH_SHORT).show();
            Log.e("SPR_Strih/", "Ошибка штрих-кода!");

        }

        super.onActivityResult(requestCode, resultCode, intent);
    }


    // Группа кнопок с функциями
    public void Button_Group() {
        button_Scan = (Button) findViewById(R.id.button_scan);
        button_Scan_QR = (Button) findViewById(R.id.button_scan_qr);
        button_Add = (Button) findViewById(R.id.button_add);
        button_Refresh = (Button) findViewById(R.id.button_xml);

        try {
            button_Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getSupportActionBar().getSubtitle().equals("") | getSupportActionBar().getSubtitle().equals(null)) {
                        new_strih = "";
                        Toast.makeText(SPR_Strih_Kod_Add.this, "Не возможно добавить новую позицию!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!new_name.equals("")) {
                            ListAdapter_NewAdd(getSupportActionBar().getSubtitle().toString());
                        } else
                            Toast.makeText(SPR_Strih_Kod_Add.this, "Выберите позицию!", Toast.LENGTH_SHORT).show();

                    }
                }
            });

            button_Scan_QR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(SPR_Strih_Kod_Add.this, "Временно не работает!", Toast.LENGTH_SHORT).show();
                }
            });

            button_Scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scanBar();
                }
            });

            button_Refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportActionBar().setSubtitle("");
                    new_name = "";
                    editText.setText("");
                    adapter.clear();
                    ListAdapter();
                    adapterPriceClients = new ListAdapterAde_STR_Search(SPR_Strih_Kod_Add.this, adapter);
                    adapterPriceClients.notifyDataSetChanged();
                    listView.setAdapter(adapterPriceClients);

                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка группы кнопок!", Toast.LENGTH_SHORT).show();
            Log.e("SPR_Strih/", "Ошибка группы кнопок!");
        }


    }


    public void ListAdapter_NewAdd(String new_strih) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base11_new_strih.brends, base11_new_strih.p_group, base11_new_strih.kod, base11_new_strih.name, base11_new_strih.kolbox, \n" +
                "base11_new_strih.cena, base11_new_strih.image, base11_new_strih.strih, base11_new_strih.kod_univ, base11_new_strih.koduid\n" +
                "FROM base11_new_strih";
        final Cursor cursor = db.rawQuery(query, null);
        ContentValues localContentValues = new ContentValues();
        cursor.moveToFirst();
        localContentValues.put("brends", new_brends);
        localContentValues.put("p_group", new_p_group);
        localContentValues.put("kod", new_kod);
        localContentValues.put("name", new_name);
        localContentValues.put("kolbox", new_kolbox);
        localContentValues.put("cena", new_cena);
        localContentValues.put("image", new_image);
        localContentValues.put("strih", new_strih);
        localContentValues.put("kod_univ", new_kod_univ);
        localContentValues.put("koduid", new_koduid);
        db.insert("base11_new_strih", null, localContentValues);
        cursor.close();
        db.close();
        Toast.makeText(this, "Создана новая запись!", Toast.LENGTH_SHORT).show();
        Log.e("SPR_Strih/", "Создана новая запись!");
        getSupportActionBar().setSubtitle("");
        new_name = "";

    }

    public void ListAdapter() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base10_nomeclature.name, base10_nomeclature.brends, base10_nomeclature.p_group, base10_nomeclature.kod, base10_nomeclature.count, \n" +
                "base5_price.cena, base10_nomeclature.image, base10_nomeclature.kolbox, \n" +
                "base10_nomeclature.strih, base10_nomeclature.kod_univ, base10_nomeclature.work, base10_nomeclature.koduid\n" +
                "FROM base10_nomeclature\n" +
                "LEFT JOIN base5_price ON base10_nomeclature.koduid = base5_price.koduid";

        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String brends = cursor.getString(cursor.getColumnIndex("brends"));
            String p_group = cursor.getString(cursor.getColumnIndex("p_group"));
            String kod = cursor.getString(cursor.getColumnIndex("kod"));
            String cena = cursor.getString(cursor.getColumnIndex("cena"));
            String kolbox = cursor.getString(cursor.getColumnIndex("kolbox"));
            String strih = cursor.getString(cursor.getColumnIndex("strih"));
            String kod_univ = cursor.getString(cursor.getColumnIndex("kod_univ"));
            String koduid = cursor.getString(cursor.getColumnIndex("koduid"));
            String image = cursor.getString(cursor.getColumnIndex("image"));
            //   String sklad_ = cursor.getString(cursor.getColumnIndex(DbContract_Otchet_Ostatok.TableUser.COLUMN_SKLAD));
            adapter.add(new ListAdapterSimple_STR_Search(brends, p_group, kod, name, kolbox, cena, image, strih, kod_univ, koduid, image));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    protected void ListAdapter_Search(String edit) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base10_nomeclature.name, base10_nomeclature.brends, base10_nomeclature.p_group, base10_nomeclature.kod, base10_nomeclature.count, \n" +
                "base5_price.cena, base10_nomeclature.image, base10_nomeclature.kolbox, \n" +
                "base10_nomeclature.strih, base10_nomeclature.kod_univ, base10_nomeclature.work, base10_nomeclature.koduid\n" +
                "FROM base10_nomeclature\n" +
                "LEFT JOIN base5_price ON base10_nomeclature.koduid = base5_price.koduid\n" +
                "WHERE base10_nomeclature.name LIKE \"%" + edit + "%\"";

        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String brends = cursor.getString(cursor.getColumnIndex("brends"));
            String p_group = cursor.getString(cursor.getColumnIndex("p_group"));
            String kod = cursor.getString(cursor.getColumnIndex("kod"));
            String cena = cursor.getString(cursor.getColumnIndex("cena"));
            String kolbox = cursor.getString(cursor.getColumnIndex("kolbox"));
            String strih = cursor.getString(cursor.getColumnIndex("strih"));
            String kod_univ = cursor.getString(cursor.getColumnIndex("kod_univ"));
            String koduid = cursor.getString(cursor.getColumnIndex("koduid"));
            String image = cursor.getString(cursor.getColumnIndex("image"));
            //   String sklad_ = cursor.getString(cursor.getColumnIndex(DbContract_Otchet_Ostatok.TableUser.COLUMN_SKLAD));
            adapter.add(new ListAdapterSimple_STR_Search(brends, p_group, kod, name, kolbox, cena, image, strih, kod_univ, koduid, image));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }


    public void onBackPressed() {
        /*dialog_cancel = new androidx.appcompat.app.AlertDialog.Builder(context_Activity);
        dialog_cancel.setTitle("Вы хотите выйти из программы?");
        dialog_cancel.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                finish();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
            }
        });
        dialog_cancel.show();*/
        Intent intent = new Intent(SPR_Strih_Kod_Add.this, SPR_Strih_Kod_Search.class);
        startActivity(intent);
        finish();
    }
}
