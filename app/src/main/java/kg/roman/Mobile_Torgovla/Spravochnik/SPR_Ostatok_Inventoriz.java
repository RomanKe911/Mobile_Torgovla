package kg.roman.Mobile_Torgovla.Spravochnik;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_Login;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Login;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Ostatok_Invert;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Otchet_Ostatok;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Ostatok_Invert;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Otchet_Ostatok;
import kg.roman.Mobile_Torgovla.R;

public class SPR_Ostatok_Inventoriz extends AppCompatActivity {
    public ArrayList<ListAdapterSimple_Otchet_Ostatok> adapter_listview = new ArrayList<ListAdapterSimple_Otchet_Ostatok>();
    public ListAdapterAde_Otchet_Ostatok adapterPriceClients;

    public ArrayList<ListAdapterSimple_Login> add_spinner_sklad = new ArrayList<ListAdapterSimple_Login>();
    public ListAdapterAde_Login adapterPriceClients_spinner_sklad;

    public ArrayList<ListAdapterSimple_Login> add_spinner_brends = new ArrayList<ListAdapterSimple_Login>();
    public ListAdapterAde_Login adapterPriceClients_spinner_brends;

    public ArrayList<ListAdapterSimple_Login> add_spinner_group = new ArrayList<ListAdapterSimple_Login>();
    public ListAdapterAde_Login adapterPriceClients_spinner_group;

    public Context context_Activity;
    public SharedPreferences sp;
    public SharedPreferences.Editor ed;
    public String PEREM_FTP_SERV, PEREM_FTP_LOGIN, PEREM_FTP_PASS, PEREM_FTP_DISTR_XML, PEREM_FTP_DISTR_db3,
            PEREM_IMAGE_PUT_SDCARD, PEREM_IMAGE_PUT_PHONE;
    public String PEREM_MAIL_LOGIN, PEREM_MAIL_PASS, PEREM_MAIL_START, PEREM_MAIL_END,
            PEREM_DB3_CONST, PEREM_DB3_BASE, PEREM_DB3_RN, PEREM_ANDROID_ID_ADMIN, PEREM_ANDROID_ID;
    public String PEREM_AG_UID, PEREM_AG_NAME, PEREM_AG_REGION, PEREM_AG_UID_REGION, PEREM_AG_CENA,
            PEREM_AG_SKLAD, PEREM_AG_UID_SKLAD, PEREM_AG_TYPE_REAL, PEREM_AG_TYPE_USER,
            PEREM_WORK_DISTR, PEREM_KOD_MOBILE, SELECT_SKLAD, SELECT_BREND, SELECT_SUBBREND, SELECT_NAME_TABLE;
    public String Save_dialog_up_, SKLAD_WORK, TYPE_SKLAD;
    public Spinner spinner_sklad, spinner_brends, spinner_group;
    public ListView listView;
    public Cursor cursor;
    public String Log_Data, sklad, groups, brends, sklad_uid, new_data_ostatok;
    public String ost_sklad, ost_brends, ost_group, ost_uid_skald;
    public ProgressBar progressBar;
    public String[] mass_sp_brends, mass_sp_subBrends;
    public String[][] mass_sp_sklad;


    public ArrayList<ListAdapterSimple_Ostatok_Invert> adapter_list_invert = new ArrayList<ListAdapterSimple_Ostatok_Invert>();
    public ListAdapterAde_Ostatok_Invert adapterPriceClients_list_invert;
    public ListView listView_invert;
    public androidx.appcompat.app.AlertDialog.Builder dialog;
    public View localView;
    public Button button_ok, button_cancel;
    public TextView tvw_list_name, tvw_list_cena, tvw_list_ostatok, tvw_list_ostatok_delete, tvw_list_koduid;
    public TextView tvw_dialog_name, tvw_dialog_cena, tvw_dialog_ostatok, tvw_dialog_ostatok_delete, tvw_dialog_koduid;
    public EditText edit_dialog_new_ostatok;
    public String query;
    public Integer new_edit = 0, this_position = 0;
    public RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt_spr_ostatok_invent);
        context_Activity = SPR_Ostatok_Inventoriz.this;
        Log.e("Class_Name: ", "|" + this.getLocalClassName());  // имя используемого класса

        // Константы для чтения
        Constanta_Read();
        // Константы для записи
        Constanta_Write();
        Log.e("TABLE ", "Name= " + SELECT_NAME_TABLE);
        Loading_Data_Ostatok();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon_image);
        getSupportActionBar().setTitle("Инвентаризация");
        getSupportActionBar().setSubtitle(" остатки на " + new_data_ostatok);

        listView_invert = findViewById(R.id.listView_ostatok_invert);

        adapter_list_invert.clear();
        Loading_list_INVERT();
        adapterPriceClients_list_invert = new ListAdapterAde_Ostatok_Invert(context_Activity, adapter_list_invert);
        adapterPriceClients_list_invert.notifyDataSetChanged();
        listView_invert.setAdapter(adapterPriceClients_list_invert);

        listView_invert.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new_edit = 0;
                this_position = i;

                localView = LayoutInflater.from(context_Activity).inflate(R.layout.spr_listview_invent_dialog, null);
                tvw_list_name = view.findViewById(R.id.tvw_invent_name);
                tvw_list_cena = view.findViewById(R.id.tvw_invent_price);
                tvw_list_ostatok = view.findViewById(R.id.tvw_invent_ostatok);
                tvw_list_ostatok_delete = view.findViewById(R.id.tvw_invent_ostatok_delete);
                tvw_list_koduid = view.findViewById(R.id.tvw_invent_koduid);


                tvw_dialog_name = localView.findViewById(R.id.tvw_invent_dialog_name);
                tvw_dialog_cena = localView.findViewById(R.id.tvw_invent_dialog_cena);
                tvw_dialog_ostatok = localView.findViewById(R.id.tvw_invent_dialog_ostatok);
                tvw_dialog_ostatok_delete = localView.findViewById(R.id.tvw_invent_dialog_ostatok_delete);
                tvw_dialog_koduid = localView.findViewById(R.id.tvw_invent_dialog_koduid);
                edit_dialog_new_ostatok = localView.findViewById(R.id.edit_invent_dialog_ostatok_new);

                tvw_dialog_name.setText(tvw_list_name.getText().toString());
                tvw_dialog_cena.setText(tvw_list_cena.getText().toString());
                tvw_dialog_ostatok.setText(tvw_list_ostatok.getText().toString());
                tvw_dialog_ostatok_delete.setText(tvw_list_ostatok_delete.getText().toString());
                tvw_dialog_koduid.setText(tvw_list_koduid.getText().toString());

                edit_dialog_new_ostatok.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        new_edit = Integer.valueOf(edit_dialog_new_ostatok.getText().toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                AlertDialog.Builder localBuilder = new AlertDialog.Builder(context_Activity);
                localBuilder.setView(localView);
                localBuilder.setTitle("Инвентаризация");
                localBuilder.setCancelable(false).setIcon(R.drawable.icon_korz).setPositiveButton(" ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        try {
                            if (new_edit != 0) {
                                Write_Otchet_Invert(
                                        tvw_list_name.getText().toString(),
                                        tvw_list_cena.getText().toString(),
                                        Integer.valueOf(tvw_list_ostatok.getText().toString()),
                                        tvw_list_koduid.getText().toString(),
                                        new_edit,
                                        SELECT_NAME_TABLE);


                                Loading_list_INVERT();
                                adapterPriceClients_list_invert = new ListAdapterAde_Ostatok_Invert(context_Activity, adapter_list_invert);
                                adapterPriceClients_list_invert.notifyDataSetChanged();
                                listView_invert.setAdapter(adapterPriceClients_list_invert);
                                listView_invert.setSelection(this_position);
                                // listView_invert.invalidateViews();



                                Log.e("ЗАПИСЬ=", "" + new_edit);
                            } else
                                Toast.makeText(context_Activity, "Неправильный ввод данных остатков", Toast.LENGTH_SHORT).show();


                            hideInputMethod();
                        } catch (Exception e) {
                            Toast.makeText(context_Activity, "Неправильный ввод данных", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).setNegativeButton(" ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //  hideInputMethod();
                        paramDialogInterface.cancel();

                    }
                });
                AlertDialog localAlertDialog = localBuilder.create();
                localAlertDialog.show();
                button_ok = localAlertDialog.getButton(-1);
                button_ok.setCompoundDrawablesWithIntrinsicBounds(R.drawable.button_ok, 0, 0, 0);
                button_cancel = localAlertDialog.getButton(-2);
                button_cancel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_close, 0);
            }
        });


       /* Log.e("sklad=", "" + spinner_sklad.getSelectedItemPosition());
        Log.e("sklad=", "" + mass_sp_sklad[spinner_sklad.getSelectedItemPosition()][0]);
        Log.e("brends=", "" + spinner_brends.getSelectedItemPosition());*/
    }

    protected void Loading_list_INVERT() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("sunbell_otchet_db.db3", MODE_PRIVATE, null);
        adapter_list_invert.clear();
     /*  if (SELECT_SUBBREND.equals("Все")) {
            Log.e("ERROR1=", "" + SELECT_SUBBREND + "" + SELECT_BREND + "" + SELECT_SKLAD);
            query = "SELECT base_in_image.kod_image, base_in_nomeclature.brends, " +
                    "base_in_nomeclature.p_group, base_in_nomeclature.name, " +
                    "base_in_nomeclature.kolbox, base_in_price.price, base_in_nomeclature.koduid, " +
                    "base_in_ostatok.count, base_in_nomeclature.strih, const_sklad.sklad_uid\n" +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                    "LEFT JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid\n" +
                    "WHERE base_in_ostatok.count > 0 \n" +
                    "AND base_in_ostatok.sklad_uid = '" + SELECT_SKLAD + "'\n" +
                    "AND base_in_nomeclature.brends = '" + SELECT_BREND + "';";
        } else {
            Log.e("ERROR2=", "|" + SELECT_SUBBREND + " " + SELECT_BREND + " " + SELECT_SKLAD);
            query = "SELECT base_in_image.kod_image, base_in_nomeclature.brends, " +
                    "base_in_nomeclature.p_group, base_in_nomeclature.name, base_in_nomeclature.kolbox, " +
                    "base_in_price.price, base_in_nomeclature.koduid, base_in_ostatok.count, " +
                    "base_in_nomeclature.strih, const_sklad.sklad_uid\n" +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                    "LEFT JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid\n" +
                    "WHERE base_in_ostatok.count > 0 \n" +
                    "AND base_in_nomeclature.brends = '" + SELECT_BREND + "'\n" +
                    "AND base_in_nomeclature.p_group = '" + SELECT_SUBBREND + "'\n" +
                    "AND base_in_ostatok.sklad_uid = '" + SELECT_SKLAD + "';";
        }

*/

        String query = "SELECT * FROM " + SELECT_NAME_TABLE + "";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            String name = cursor.getString(cursor.getColumnIndex("name"));
            String kod_uid = cursor.getString(cursor.getColumnIndex("name_uid"));
            String cena = cursor.getString(cursor.getColumnIndex("cena"));
            String ostatok = cursor.getString(cursor.getColumnIndex("ostatok"));
            String ostatok_new = cursor.getString(cursor.getColumnIndex("ostatok_new"));
            String sum_razn = cursor.getString(cursor.getColumnIndex("itogo_invert"));
            String image = cursor.getString(cursor.getColumnIndex("image"));
            String kolbox = cursor.getString(cursor.getColumnIndex("kol_box"));
            String strih = cursor.getString(cursor.getColumnIndex("strih"));

            int new_del, new_del_ost;
            String new_kolbox;
            new_del = Integer.parseInt(ostatok) / Integer.parseInt(kolbox);
            new_del_ost = Integer.parseInt(ostatok) % Integer.parseInt(kolbox);
            if (new_del > 0) {
                new_kolbox = "(" + new_del + "кор. + " + new_del_ost + "шт.)";
            } else new_kolbox = "(" + new_del_ost + "шт.)";
            //Log.e("DELETE ", "|" + new_del + "|" + new_del_ost);

            adapter_list_invert.add(new ListAdapterSimple_Ostatok_Invert(name, kod_uid, cena, ostatok, ostatok_new, kolbox, new_kolbox, sum_razn, strih, image));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    protected void Write_Otchet_Invert(String w_name, String w_cena, int w_ostatok, String w_nameUID, int w_ostatok_new, String w_name_table) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("sunbell_otchet_db.db3", MODE_PRIVATE, null);
        String query = "SELECT * FROM " + w_name_table + "";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        ContentValues localContentValues = new ContentValues();
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String name_uid = cursor.getString(cursor.getColumnIndex("name_uid"));
            String cena = cursor.getString(cursor.getColumnIndex("cena"));
            String ostatok = cursor.getString(cursor.getColumnIndex("ostatok"));
            String ostato_new = cursor.getString(cursor.getColumnIndex("ostatok_new"));
            String itogo_invert = cursor.getString(cursor.getColumnIndex("itogo_invert"));
            if (name_uid.equals(w_nameUID)) {
                int summa_invert;
                summa_invert = (w_ostatok_new - Integer.valueOf(ostatok)) * Integer.valueOf(cena);
                localContentValues.put("ostatok_new", w_ostatok_new);
                localContentValues.put("itogo_invert", summa_invert);
                String[] arrayOfString = new String[1];
                arrayOfString[0] = name_uid;  // массив строк по которому искать уникальный номер
                db.update(w_name_table, localContentValues, "name_uid = ?", new String[]{name_uid});
            }

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }


    // Обработка данных список рабочих складов:
    protected void SQLite_Work_Sklad() {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
            String query = "SELECT DISTINCT const_sklad.sklad_name, base_in_ostatok.sklad_uid " +
                    "FROM base_in_ostatok\n" +
                    "LEFT JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid;";
            final Cursor cursor = db.rawQuery(query, null);
            mass_sp_sklad = new String[cursor.getCount() + 1][2];
            mass_sp_sklad[0][0] = "Все";
            mass_sp_sklad[0][1] = "ALL";
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String sklad_uid = cursor.getString(cursor.getColumnIndex("sklad_uid")); // код клиента
                String sklad_name = cursor.getString(cursor.getColumnIndex("sklad_name")); // код клиента
                mass_sp_sklad[cursor.getPosition() + 1][0] = sklad_name;
                mass_sp_sklad[cursor.getPosition() + 1][1] = sklad_uid;
                cursor.moveToNext();
            }
            cursor.close();
            db.close();

            add_spinner_sklad.clear();
            for (int i = 0; i < mass_sp_sklad.length; i++) {
                Log.e("массив складов: ", mass_sp_sklad[i][0] + "__" + mass_sp_sklad[i][1]);
                add_spinner_sklad.add(new ListAdapterSimple_Login(mass_sp_sklad[i][0], mass_sp_sklad[i][1], "склады"));
            }
            adapterPriceClients_spinner_sklad = new ListAdapterAde_Login(context_Activity, add_spinner_sklad);
            adapterPriceClients_spinner_sklad.notifyDataSetChanged();
            spinner_sklad.setAdapter(adapterPriceClients_spinner_sklad);

        } catch (Exception e) {
            Toast.makeText(context_Activity, Log_Data + "отсутствует активные склады", Toast.LENGTH_LONG).show();
            Log.e(Log_Data, "отсутствует активные склады!");
        }
    }

    // Обработка данных загрузка брендов
    protected void SQLite_Work_Brends() {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
            String query = "SELECT DISTINCT base_in_nomeclature.brends, base_in_nomeclature.name, base_in_ostatok.count " +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "WHERE base_in_ostatok.count > 0 GROUP BY brends";
            final Cursor cursor = db.rawQuery(query, null);
            mass_sp_brends = new String[cursor.getCount() + 1];
            mass_sp_brends[0] = "Все";
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(cursor.getColumnIndex("name")); // код клиента
                String brends = cursor.getString(cursor.getColumnIndex("brends")); // код клиента
                mass_sp_brends[cursor.getPosition() + 1] = brends;
                Log.e("массив брендов: ", brends);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();


            add_spinner_brends.clear();
            for (int i = 0; i < mass_sp_brends.length; i++) {
                add_spinner_brends.add(new ListAdapterSimple_Login(mass_sp_brends[i], "", "бренды"));
            }
            adapterPriceClients_spinner_brends = new ListAdapterAde_Login(context_Activity, add_spinner_brends);
            adapterPriceClients_spinner_brends.notifyDataSetChanged();
            spinner_brends.setAdapter(adapterPriceClients_spinner_brends);


        } catch (Exception e) {
            Toast.makeText(context_Activity, Log_Data + "отсутствует массив брендов", Toast.LENGTH_LONG).show();
            Log.e(Log_Data, "отсутствует массив брендов!");
        }
    }

    // Обработка данных загрузка группы товаров из брендов
    protected void SQLite_Work_Groups(String sp_brends) {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
            String query = "SELECT DISTINCT base_in_nomeclature.brends, base_in_nomeclature.p_group, base_in_nomeclature.name, base_in_ostatok.count FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "WHERE base_in_ostatok.count > 0 AND base_in_nomeclature.brends = '" + sp_brends + "'" +
                    "GROUP BY p_group \n" +
                    "ORDER BY brends;";
            final Cursor cursor = db.rawQuery(query, null);
            mass_sp_subBrends = new String[cursor.getCount() + 1];
            mass_sp_subBrends[0] = "Все";
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String p_group = cursor.getString(cursor.getColumnIndex("p_group")); // код клиента
                mass_sp_subBrends[cursor.getPosition() + 1] = p_group;
                Log.e("массив группы брендов: ", p_group);
                Log.e("GROUP ", p_group);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();

            add_spinner_group.clear();
            for (int i = 0; i < mass_sp_subBrends.length; i++) {
                add_spinner_group.add(new ListAdapterSimple_Login(mass_sp_subBrends[i], "", "группы"));
            }
            adapterPriceClients_spinner_group = new ListAdapterAde_Login(context_Activity, add_spinner_group);
            adapterPriceClients_spinner_group.notifyDataSetChanged();
            spinner_group.setAdapter(adapterPriceClients_spinner_group);

        } catch (Exception e) {
            Toast.makeText(context_Activity, Log_Data + "отсутствует массив брендов подгрупп", Toast.LENGTH_LONG).show();
            Log.e(Log_Data, "отсутствует массив брендов подгрупп!");
        }
    }

    // Обработка нажатие ListView
    protected void Loading_ListView(String sp_sklad, String sp_uid_skald, String sp_brends, String sp_group) {
        ost_sklad = sp_sklad;
        ost_brends = sp_brends;
        ost_group = sp_group;
        ost_uid_skald = sp_uid_skald;
        String perem = "Все";
        Log.e("Ost= ", ost_sklad + ", " + ost_brends + ", " + ost_group);
        // Все из всех
        // Все, Бренд, Все
        // Все, Бренд, Группа
        // Склад, Бренд, Все
        // Склад, Бренд, Группа

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("sunbell_otchet_db.db3", MODE_PRIVATE, null);
      /*  if (ost_sklad.equals(perem) & ost_brends.equals(perem) & ost_group.equals(perem)) {
            // Выборка остатков Все из Всех
            query = "SELECT base_in_nomeclature.brends, base_in_nomeclature.p_group, " +
                    "base_in_nomeclature.name, base_in_nomeclature.kolbox, base_in_price.price, " +
                    "base_in_ostatok.count, base_in_image.kod_image, const_sklad.sklad_name, base_in_ostatok.sklad_uid\n" +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                    "LEFT JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid \n" +
                    "WHERE base_in_ostatok.count > 0 ";
        } else if (ost_sklad.equals(perem) & !ost_brends.equals(perem) & ost_group.equals(perem)) {
            // Выборка остатков по брендам и группам (Все, Бренд, Все)
            query = "SELECT base_in_nomeclature.brends, base_in_nomeclature.p_group, " +
                    "base_in_nomeclature.name, base_in_nomeclature.kolbox, base_in_price.price, " +
                    "base_in_ostatok.count, base_in_image.kod_image, const_sklad.sklad_name, base_in_ostatok.sklad_uid\n" +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                    "LEFT JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid \n" +
                    "WHERE base_in_ostatok.count > 0 AND base_in_nomeclature.brends = '" + ost_brends + "';";
        } else if (ost_sklad.equals(perem) & !ost_brends.equals(perem) & !ost_group.equals(perem)) {
            // Выборка остатков по брендам и группам (Все, Бренд, Группа)
            query = "SELECT base_in_nomeclature.brends, base_in_nomeclature.p_group, " +
                    "base_in_nomeclature.name, base_in_nomeclature.kolbox, base_in_price.price, " +
                    "base_in_ostatok.count, base_in_image.kod_image, const_sklad.sklad_name, base_in_ostatok.sklad_uid\n" +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                    "LEFT JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid \n" +
                    "WHERE base_in_ostatok.count > 0 AND base_in_nomeclature.brends = '" + ost_brends + "' AND base_in_nomeclature.p_group = '" + ost_group + "';";
        } else if (!ost_sklad.equals(perem) & ost_brends.equals(perem) & ost_group.equals(perem)) {
            // Выборка остатков по складам (Склад, Все, Все)
            query = "SELECT base_in_nomeclature.brends, base_in_nomeclature.p_group, " +
                    "base_in_nomeclature.name, base_in_nomeclature.kolbox, base_in_price.price, " +
                    "base_in_ostatok.count, base_in_image.kod_image, const_sklad.sklad_name, base_in_ostatok.sklad_uid\n" +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                    "LEFT JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid \n" +
                    "WHERE base_in_ostatok.count > 0 AND base_in_ostatok.sklad_uid = '" + ost_uid_skald + "'";
        } else if (!ost_sklad.equals(perem) & !ost_brends.equals(perem) & ost_group.equals(perem)) {
            // Выборка остатков по складам брендам и группам  (Склад, Бренд, Все)
            query = "SELECT base_in_nomeclature.brends, base_in_nomeclature.p_group, " +
                    "base_in_nomeclature.name, base_in_nomeclature.kolbox, base_in_price.price, " +
                    "base_in_ostatok.count, base_in_image.kod_image, const_sklad.sklad_name, base_in_ostatok.sklad_uid\n" +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                    "LEFT JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid \n" +
                    "WHERE base_in_ostatok.count > 0 " +
                    "AND base_in_ostatok.sklad_uid = '" + ost_uid_skald + "'" +
                    "AND base_in_nomeclature.brends = '" + ost_brends + "';";
        } else {
            // Выборка остатков по складам брендам и группам  (Склад, Бренд, Группа)
            query = "SELECT base_in_nomeclature.brends, base_in_nomeclature.p_group, " +
                    "base_in_nomeclature.name, base_in_nomeclature.kolbox, base_in_price.price, " +
                    "base_in_ostatok.count, base_in_image.kod_image, const_sklad.sklad_name, " +
                    "base_in_ostatok.sklad_uid\n" +
                    "FROM base_in_nomeclature\n" +
                    "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                    "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                    "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                    "LEFT JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid\n" +
                    "WHERE base_in_ostatok.count > 0 \n" +
                    "AND base_in_ostatok.sklad_uid = '" + ost_uid_skald + "'\n" +
                    "AND base_in_nomeclature.brends = '" + ost_brends + "'\n" +
                    "AND base_in_nomeclature.p_group = '" + ost_group + "';";
        }
*/

        query = "SELECT * FROM " + SELECT_NAME_TABLE + "";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String brends = cursor.getString(cursor.getColumnIndex("brends"));
            String p_group = cursor.getString(cursor.getColumnIndex("p_group"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String sklad_name = cursor.getString(cursor.getColumnIndex("sklad_name"));
            String price = cursor.getString(cursor.getColumnIndex("price"));
            String count = cursor.getString(cursor.getColumnIndex("count"));
            String kolbox = cursor.getString(cursor.getColumnIndex("kolbox"));
            String image = cursor.getString(cursor.getColumnIndex("kod_image"));
          /*  for (int k = 0; k < mass_sp_sklad.length; k++) {
                if (sklad.equals(mass_sp_sklad[k][1])) {
                    sklad_on = mass_sp_sklad[k][0];
                }
            }*/


            int new_del, new_del_ost;
            String new_kolbox;
            new_del = Integer.parseInt(count) / Integer.parseInt(kolbox);
            new_del_ost = Integer.parseInt(count) % Integer.parseInt(kolbox);
            //Log.e("DELETE ", "|" + new_del + "|" + new_del_ost);
            if (new_del > 0) {
                new_kolbox = "(" + new_del + "кор. + " + new_del_ost + "шт.)";
            } else new_kolbox = "(" + new_del_ost + "шт.)";

            adapter_listview.add(new ListAdapterSimple_Otchet_Ostatok(name, brends, p_group, sklad_name, price + "c", count + "шт", kolbox + "шт", new_kolbox, image));
            //*****  adapter_listview.add(new ListAdapterSimple_Otchet_Ostatok(name, count + " шт", brends, cena + " с", image, sklad_on));

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        progressBar.setVisibility(View.GONE);


        try {

        } catch (Exception e) {

            Log.e("Err..", "rrr");
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

        SELECT_BREND = sp.getString("PEREM_OTCHET_INVERTARZ_BREND", "");           //
        SELECT_SUBBREND = sp.getString("PEREM_OTCHET_INVERTARZ_SUBBREND", "");     //
        SELECT_SKLAD = sp.getString("PEREM_OTCHET_INVERTARZ_SKLAD", "");           //
        SELECT_NAME_TABLE = sp.getString("PEREM_OTCHET_INVERTARZ_NAME_TABLE", "");           //


        // ed.putString("PEREM_OTCHET_INVERTARZ_NEW_RN", "");           //


    }

    // Константы для записи
    protected void Constanta_Write() {
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        ed = sp.edit();
        ed.putString("PEREM_ISNAME_SPINNER", "OSTATOK_ACTIVITY");  // запись переменой для отображения списка
        ed.commit();
    }

    // Установка дата остатков
    protected void Loading_Data_Ostatok() {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
            String query = "SELECT DISTINCT data FROM base_in_ostatok";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            String ost_data, ost_month, ost_yaer;
            while (cursor.isAfterLast() == false) {
                String data = cursor.getString(cursor.getColumnIndex("data"));   // дата остатков 'месяц/день/год'  7/4/2022
                ost_data = data.substring(data.indexOf('/') + 1, data.lastIndexOf('/'));
                ost_month = data.substring(0, data.indexOf('/'));
                ost_yaer = data.substring(data.lastIndexOf('/') + 1);
                if (ost_data.length() == 1) {
                    ost_data = "0" + ost_data;
                }
                if (ost_month.length() == 1) {
                    ost_month = "0" + ost_month;
                }
          /*  Log.e("символ1", ost_data + "|" + ost_data.length());
            Log.e("символ2", ost_month + "|" + ost_month.length());
            Log.e("символ3", ost_yaer);*/
                new_data_ostatok = ost_data + "." + ost_month + "." + ost_yaer;
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.e("Ошибка", "Не верный формат даты!");
            Toast.makeText(context_Activity, "Не верный формат даты!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ostatki_single, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_ost_update: {
                String sk, sk_uid, br, gr;
                sk = mass_sp_sklad[spinner_sklad.getSelectedItemPosition()][0];
                sk_uid = mass_sp_sklad[spinner_sklad.getSelectedItemPosition()][1];
                br = mass_sp_brends[spinner_brends.getSelectedItemPosition()];
                if (mass_sp_subBrends.length > 0) {
                    gr = mass_sp_subBrends[spinner_group.getSelectedItemPosition()];
                } else gr = "Все";

                adapter_listview.clear();
                Loading_ListView(sk, sk_uid, br, gr);
                adapterPriceClients = new ListAdapterAde_Otchet_Ostatok(context_Activity, adapter_listview);
                adapterPriceClients.notifyDataSetChanged();
                listView.setAdapter(adapterPriceClients);
                progressBar.setVisibility(View.VISIBLE);
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyAsyncTask_Sync_Users extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() { // Вызывается в начале потока
            super.onPreExecute();
            Log.e("ПОТОК=", "Начало потока");
            adapter_listview.clear();
            adapterPriceClients = new ListAdapterAde_Otchet_Ostatok(context_Activity, adapter_listview);
            adapterPriceClients.notifyDataSetChanged();
            listView.setAdapter(adapterPriceClients);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
            super.onProgressUpdate(values);
            // pDialog.setMessage("Синхронизация остатков. Подождите...");
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
            //progressBar_horis.setProgress(0);
            progressBar.setVisibility(View.GONE);
            adapterPriceClients = new ListAdapterAde_Otchet_Ostatok(context_Activity, adapter_listview);
            adapterPriceClients.notifyDataSetChanged();
            listView.setAdapter(adapterPriceClients);
        }

        private void getFloor() throws InterruptedException {
            Loading_ListView(ost_sklad, ost_brends, ost_group, ost_uid_skald);

            try {


            } catch (Exception e) {

                Log.e("Err..", "rrr");
            }

            TimeUnit.SECONDS.sleep(5);
            // pDialog.dismiss();
        }
    }


    /**
     * прячем программную клавиатуру
     */
    protected void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            //imm.hideSoftInputFromWindow(editText_password.getWindowToken(), 0);
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

}


