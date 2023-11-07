package fragments;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_STR_Search;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_STR_Search;
import kg.roman.Mobile_Torgovla.R;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class Tab1Fragment_Month extends Fragment {

    public ArrayList<ListAdapterSimple_STR_Search> adapter_listview = new ArrayList<ListAdapterSimple_STR_Search>();
    public ListAdapterAde_STR_Search adapterPriceClients;
    public Context context;
    public EditText editText, editText_name;
    public CheckBox checkBox;
    public ListView listView;
    public Button button_Scan, button_Scan_QR, button_Add, button_Refresh, button_Delete;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    public String contents, edit_perem;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("Tab1", "Tabs1");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.strih_kod_add, null);
        context = v.getContext();

        /*editText = (EditText) v.findViewById(R.id.edt_search_strh);
        editText_name = (EditText) v.findViewById(R.id.edt_search_name);
        checkBox = (CheckBox) v.findViewById(R.id.checkBox_new);
        listView = (ListView) v.findViewById(R.id.lisvView_add);
        button_Scan_QR = (Button) v.findViewById(R.id.button_scan_qr);
        button_Scan = (Button) v.findViewById(R.id.button_scan);
        button_Add = (Button) v.findViewById(R.id.button_add);
        button_Refresh = (Button) v.findViewById(R.id.button_refresh);
        button_Delete = (Button) v.findViewById(R.id.button_delete);*/

        editText_name.setVisibility(View.GONE);

        edit_perem = "";
        adapter_listview.clear();
        Loading_Ostatki(edit_perem);
        adapterPriceClients = new ListAdapterAde_STR_Search(getActivity(), adapter_listview);
        adapterPriceClients.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked())
                {
                    edit_perem = "";
                    editText_name.setVisibility(View.VISIBLE);
                    adapter_listview.clear();
                    Loading_Ostatki(edit_perem);
                    adapterPriceClients = new ListAdapterAde_STR_Search(getActivity(), adapter_listview);
                    adapterPriceClients.notifyDataSetChanged();
                    listView.setAdapter(adapterPriceClients);
                } else
                {
                    editText_name.setVisibility(View.INVISIBLE);
                    edit_perem = editText.getText().toString();
                    adapter_listview.clear();
                    Loading_Ostatki(edit_perem);
                    adapterPriceClients = new ListAdapterAde_STR_Search(getActivity(), adapter_listview);
                    adapterPriceClients.notifyDataSetChanged();
                    listView.setAdapter(adapterPriceClients);
                }
            }
        });



      /*  editText.setText("");
        adapter_listview.clear();
        Loading_Ostatki();
        adapterPriceClients = new ListAdapterAde_STR_Search(getActivity(), adapter_listview);
        adapterPriceClients.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients);*/


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter_listview.clear();
                Loading_Ostatki(edit_perem);
                adapterPriceClients = new ListAdapterAde_STR_Search(getActivity(), adapter_listview);
                adapterPriceClients.notifyDataSetChanged();
                listView.setAdapter(adapterPriceClients);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter_listview.clear();
                Loading_Ostatki_Name(editText_name.getText().toString());
                adapterPriceClients = new ListAdapterAde_STR_Search(getActivity(), adapter_listview);
                adapterPriceClients.notifyDataSetChanged();
                listView.setAdapter(adapterPriceClients);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        button_Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter_listview.clear();
               // Loading_Ostatki();
                adapterPriceClients = new ListAdapterAde_STR_Search(getActivity(), adapter_listview);
                adapterPriceClients.notifyDataSetChanged();
                listView.setAdapter(adapterPriceClients);
            }
        });
        button_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                editText_name.setText("");
                edit_perem = "";
                checkBox.setChecked(false);
                adapter_listview.clear();
                Loading_Ostatki(edit_perem);
                adapterPriceClients = new ListAdapterAde_STR_Search(getActivity(), adapter_listview);
                adapterPriceClients.notifyDataSetChanged();
                listView.setAdapter(adapterPriceClients);
            }
        });

        button_Scan_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanQR();
                Log.e("Tab1", "QR");
            }
        });

        button_Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBar();
                Log.e("Tab1", "Bar");
            }
        });


        return v;
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
            showDialog(getActivity(), "Сканнер не найден", "Установить сканер с Play Market?", "Да", "Нет").show();
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
            showDialog(getActivity(), "Сканнер не найден", "Установить сканер с Play Market?", "Да", "Нет").show();
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
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

                // Получаем данные после работы сканера и выводим их в Toast сообщении:
                contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Toast toast = Toast.makeText(getActivity(), "Содержание: " + contents + " Формат: " + format, Toast.LENGTH_SHORT);
                toast.show();

            }
        }
        editText.setText(contents);
        editText.requestFocus();
        editText.setSelection(editText.length());
        edit_perem = editText.getText().toString();
        adapter_listview.clear();
        Loading_Ostatki(edit_perem);
        adapterPriceClients = new ListAdapterAde_STR_Search(getActivity(), adapter_listview);
        adapterPriceClients.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients);

    }


    protected void Loading_Db_Nomencalture(String contents) {
        SQLiteDatabase db = context.openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
        String query = " SELECT base10_nomeclature.kod, base10_nomeclature.name, " +
                "base10_nomeclature.kolbox, base10_nomeclature.cena, " +
                "base10_nomeclature.image, base10_nomeclature.strih, " +
                "base10_nomeclature.kod_univ, \n" +
                "         base10_nomeclature.koduid, base10_nomeclature.p_group, " +
                "base10_nomeclature.brends, base10_nomeclature.count, " +
                "base10_nomeclature.work \n" +
                "                FROM base10_nomeclature WHERE base10_nomeclature.strih LIKE '%" + contents + "%'";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String brends = cursor.getString(cursor.getColumnIndex("brends"));
            String p_group = cursor.getString(cursor.getColumnIndex("p_group"));
            String kod = cursor.getString(cursor.getColumnIndex("kod"));
            String kolbox = cursor.getString(cursor.getColumnIndex("kolbox"));
            String cena = cursor.getString(cursor.getColumnIndex("cena"));
            String image = cursor.getString(cursor.getColumnIndex("image"));
            String strih = cursor.getString(cursor.getColumnIndex("strih"));
            String kod_univ = cursor.getString(cursor.getColumnIndex("kod_univ"));
            String koduid = cursor.getString(cursor.getColumnIndex("koduid"));
            String work = cursor.getString(cursor.getColumnIndex("work"));

            if (contents.equals(strih)) {
                Toast.makeText(getActivity(), "Есть совпадение ", Toast.LENGTH_LONG).show();
                Log.e("Штрих-код", name + ", " + strih);
                adapter_listview.add(new ListAdapterSimple_STR_Search(brends, p_group, kod, name, kolbox, cena, image, strih, kod_univ, koduid, image));
                cursor.moveToNext();
            } else {
                cursor.moveToNext();
            }

        }
        cursor.close();
        db.close();
    }  // Загрузка номенклатуры по брендам и по группам

    protected void Loading_Ostatki(String perem) {
        SQLiteDatabase db = context.openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base10_nomeclature.name, base10_nomeclature.brends, " +
                "base10_nomeclature.p_group, base10_nomeclature.kod, base10_nomeclature.kolbox, \n" +
                "base10_nomeclature.cena, base10_nomeclature.image, base10_nomeclature.strih, " +
                "base10_nomeclature.kod_univ, base10_nomeclature.work, base10_nomeclature.koduid\n" +
                "FROM base10_nomeclature WHERE base10_nomeclature.strih LIKE '%" + perem + "%';";

        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String brends = cursor.getString(cursor.getColumnIndex("brends"));
            String p_group = cursor.getString(cursor.getColumnIndex("p_group"));
            String kod = cursor.getString(cursor.getColumnIndex("kod"));
            String kolbox = cursor.getString(cursor.getColumnIndex("kolbox"));
            String cena = cursor.getString(cursor.getColumnIndex("cena"));
            String image = cursor.getString(cursor.getColumnIndex("image"));
            String strih = cursor.getString(cursor.getColumnIndex("strih"));
            String kod_univ = cursor.getString(cursor.getColumnIndex("kod_univ"));
            String koduid = cursor.getString(cursor.getColumnIndex("koduid"));
            adapter_listview.add(new ListAdapterSimple_STR_Search(brends, p_group, kod, name, kolbox, cena, image, strih, kod_univ, koduid, image));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    protected void Loading_Ostatki_Name(String perem) {
        SQLiteDatabase db = context.openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base10_nomeclature.name, base10_nomeclature.brends, " +
                "base10_nomeclature.p_group, base10_nomeclature.kod, base10_nomeclature.kolbox, \n" +
                "base10_nomeclature.cena, base10_nomeclature.image, base10_nomeclature.strih, " +
                "base10_nomeclature.kod_univ, base10_nomeclature.work, base10_nomeclature.koduid\n" +
                "FROM base10_nomeclature WHERE base10_nomeclature.name LIKE '%" + perem + "%';";

        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String brends = cursor.getString(cursor.getColumnIndex("brends"));
            String p_group = cursor.getString(cursor.getColumnIndex("p_group"));
            String kod = cursor.getString(cursor.getColumnIndex("kod"));
            String kolbox = cursor.getString(cursor.getColumnIndex("kolbox"));
            String cena = cursor.getString(cursor.getColumnIndex("cena"));
            String image = cursor.getString(cursor.getColumnIndex("image"));
            String strih = cursor.getString(cursor.getColumnIndex("strih"));
            String kod_univ = cursor.getString(cursor.getColumnIndex("kod_univ"));
            String koduid = cursor.getString(cursor.getColumnIndex("koduid"));
            adapter_listview.add(new ListAdapterSimple_STR_Search(brends, p_group, kod, name, kolbox, cena, image, strih, kod_univ, koduid, image));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

}


