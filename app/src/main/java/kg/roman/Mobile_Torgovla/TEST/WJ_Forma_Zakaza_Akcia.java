package kg.roman.Mobile_Torgovla.TEST;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Forma_Aksia;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Forma_Aksia;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.Work_Journal.WJ_Global_Activity;

public class WJ_Forma_Zakaza_Akcia extends AppCompatActivity {

    public ArrayList<ListAdapterSimple_Forma_Aksia> aksia = new ArrayList<ListAdapterSimple_Forma_Aksia>();
    public ListAdapterAde_Forma_Aksia adapterPriceClients;

    public ListView listView;
    public String[] mass_brends, mass_aksia_brends;
    public ArrayAdapter<String> adapter_brends, adapter_aksia_brends;
    public Boolean n1, n2, n3;
    public Context context_Activity;
    public String strText, sub_Text;
    public Integer list;
    public TextView tvw_sum, tvw_skidka;
    public androidx.appcompat.app.AlertDialog.Builder dialog;
    public String l_name, l_kod, l_cena, l_ostatok, l_kol, l_image, l_ak_kol;
    public Button button_ok, button_up, button_cancel;



    public Integer perem_int_summa, perem_int_ostatok, perem_int_cena, perem_int_kol, perem_kol_group_one, perem_int_kolbox;
    public String PEREM_Agent, PEREM_KAgent, PEREM_KAgent_UID, PEREM_Vrema, PEREM_Data, PEREM_RNKod, Summa, sp_BREND, PEREM_Adress;
    public TextView tvw_uslov_pref1, tvw_uslov_pref2, tvw_uslov_pref3;
    public Button btn_ok;
    public SharedPreferences.Editor ed;
    public SharedPreferences sp;
    public String PEREM_FTP_SERV, PEREM_FTP_LOGIN, PEREM_FTP_PASS, PEREM_FTP_DISTR_XML, PEREM_FTP_DISTR_db3,
            PEREM_IMAGE_PUT_SDCARD, PEREM_IMAGE_PUT_PHONE;
    public String PEREM_MAIL_LOGIN, PEREM_MAIL_PASS, PEREM_MAIL_START, PEREM_MAIL_END,
            PEREM_DB3_CONST, PEREM_DB3_BASE, PEREM_DB3_RN, PEREM_ANDROID_ID_ADMIN, PEREM_ANDROID_ID;
    public String PEREM_AG_UID, PEREM_AG_NAME, PEREM_AG_REGION, PEREM_AG_UID_REGION, PEREM_AG_CENA,
            PEREM_AG_SKLAD, PEREM_AG_UID_SKLAD, PEREM_AG_TYPE_REAL, PEREM_AG_TYPE_USER,
            PEREM_WORK_DISTR, PEREM_KOD_MOBILE;
    public String Save_dialog_up_, grb;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now, this_data_work_now, this_vrema_work_now;
    public Integer checked_group, kol_box_info, max_box;
    public String kol_group_one, kol_group_much, select_image;
    public Double dg_tw_summa_sk_DOUBLE;

    public String pref_type_group = "kol";
    public Double usl_sum_all, usl_itogo_all, usl_w_this, usl_w_summa;
    public Integer usl_kol_all, usl_kol_skidka = 0, usl_w_skidka, usl_w_min;


    ///////// WORK
    public TextView list_tw_aks_name, list_tw_aks_uid, list_tw_aks_cena, list_tw_aks_sklad_uid,
            list_tw_aks_ostatok, list_tw_aks_skidka, list_tw_aks_cena_bonus, list_tv_aks_zakaz;
    public TextView win_aks_type_aks, win_aks_kol, win_aks_summa, win_aks_itogo,
            win_aks_uslov_1, win_aks_uslov_2, win_aks_uslov_3, win_aks_skidka_1, win_aks_skidka_2, win_aks_skidka_3;
    public LinearLayout win_layout_uslov_1, win_layout_uslov_2, win_layout_uslov_3;
    public ImageView win_uslov_image1, win_uslov_image2, win_uslov_image3;
    public TextView dg_tw_aks_name, dg_tw_aks_uid, dg_tw_aks_univ, dg_tw_aks_cena,
            dg_tw_aks_ostatok, dg_tw_aks_kolbox, dg_tv_aks_count, dg_tv_aks_summa, dg_tv_aks_sklad_uid;
    public Button dg_button_add, dg_button_down;
    public FloatingActionButton floatingActionButton_Add;
    public EditText dg_edit_aks_kol;
    public RadioGroup dg_radioGroup;
    public ConstraintLayout dg_layout_button_box;
    public View localView_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_forma_akcia);
        getSupportActionBar().setIcon(R.mipmap.ic_icons_1);
        getSupportActionBar().setTitle("Акции");
        getSupportActionBar().setSubtitle("");
        context_Activity = WJ_Forma_Zakaza_Akcia.this;
        Constanta_Read();

        sp_BREND = sp.getString("sp_BREND", "0");
        PEREM_Agent = sp.getString("PEREM_Agent", "0");
        PEREM_KAgent = sp.getString("PEREM_KAgent", "0");
        PEREM_KAgent_UID = sp.getString("PEREM_KAgent_UID", "0");
        PEREM_Vrema = sp.getString("PEREM_Vrema", "0");
        PEREM_Data = sp.getString("PEREM_Data", "0");
        PEREM_RNKod = sp.getString("PEREM_RNKod", "0");
        PEREM_Adress = sp.getString("PEREM_Adress", "0");

        ////////////////////////////  Основные переменные для диалога из основного окна
        win_aks_type_aks = findViewById(R.id.tvw_win_aks_type);
        win_aks_kol = findViewById(R.id.tvw_win_aks_kol);
        win_aks_summa = findViewById(R.id.tvw_win_aks_summa);
        win_aks_itogo = findViewById(R.id.tvw_win_aks_itogo);
        win_layout_uslov_1 = findViewById(R.id.layout_win_uslov_1);
        win_layout_uslov_2 = findViewById(R.id.layout_win_uslov_2);
        win_layout_uslov_3 = findViewById(R.id.layout_win_uslov_3);
        win_aks_skidka_1 = findViewById(R.id.tvw_win_usl_skidka1);
        win_aks_skidka_2 = findViewById(R.id.tvw_win_usl_skidka2);
        win_aks_skidka_3 = findViewById(R.id.tvw_win_usl_skidka3);
        win_aks_uslov_1 = findViewById(R.id.tvw_win_uslov_1);
        win_aks_uslov_2 = findViewById(R.id.tvw_win_uslov_2);
        win_aks_uslov_3 = findViewById(R.id.tvw_win_uslov_3);
        tvw_uslov_pref1 = findViewById(R.id.tvw_win_uslov_pref1);
        tvw_uslov_pref2 = findViewById(R.id.tvw_win_uslov_pref2);
        tvw_uslov_pref3 = findViewById(R.id.tvw_win_uslov_pref3);
        win_uslov_image1 = findViewById(R.id.uslov_image_1);
        win_uslov_image2 = findViewById(R.id.uslov_image_2);
        win_uslov_image3 = findViewById(R.id.uslov_image_3);
        floatingActionButton_Add = findViewById(R.id.floatingActionButton_Add);
        listView = findViewById(R.id.lvw_win_list);
        ////////////////////////////

        ////////////////////////////  При открытитт окна
        win_layout_uslov_1.setVisibility(View.GONE);
        win_layout_uslov_2.setVisibility(View.GONE);
        win_layout_uslov_3.setVisibility(View.GONE);
        win_uslov_image1.setVisibility(View.GONE);
        win_uslov_image2.setVisibility(View.GONE);
        win_uslov_image3.setVisibility(View.GONE);

        win_aks_summa.setText("0.0");
        win_aks_kol.setText("0");

        n1 = false;
        n2 = false;
        n3 = false;
        list = 0;

        Calendate_New();
        List_adapter_Brends(this_data_work_now);
        adapter_brends = new ArrayAdapter<>(context_Activity,
                android.R.layout.simple_list_item_1, mass_brends);
        listView.setAdapter(adapter_brends);
        list++;


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View localView_list, int i, long l) {
                switch (list) {
                    case 1: {
                        TextView textView = (TextView) localView_list;
                        strText = textView.getText().toString(); // получаем текст нажатого элемента
                        // textView1.setText(strText);
                        getSupportActionBar().setTitle(strText);
                        list++;
                        Loading_Brends_Click(strText);
                        adapter_aksia_brends = new ArrayAdapter<>(context_Activity,
                                android.R.layout.simple_list_item_1, mass_aksia_brends);
                        listView.setAdapter(adapter_aksia_brends);
                    }
                    break;
                    case 2: {
                        TextView textView = (TextView) localView_list;
                        strText = textView.getText().toString(); // получаем текст нажатого элемента
                        //textView2.setText(strText);
                        getSupportActionBar().setSubtitle(strText);
                        list++;
                        aksia.clear();
                        Log.e("name aks", strText);
                        Loading_Brends_Uls_Aks(strText, this_data_work_now);
                        ListView_Adapter(strText, this_data_work_now);
                        adapterPriceClients = new ListAdapterAde_Forma_Aksia(context_Activity, aksia);
                        adapterPriceClients.notifyDataSetChanged();
                        listView.setAdapter(adapterPriceClients);
                    }
                    break;
                    case 3: {
                        // Работа по добавлению кнопок
                     /*   l_name = ((TextView) view.findViewById(R.id.tvw_name_aks)).getText().toString();
                        l_cena = ((TextView) view.findViewById(R.id.tvw_aks_price)).getText().toString();
                        l_ostatok = ((TextView) view.findViewById(R.id.tvw_aks_ostatok)).getText().toString();
                        l_kod = ((TextView) view.findViewById(R.id.tvw_kod_aks)).getText().toString();
                        l_kol = ((TextView) view.findViewById(R.id.tvw_aks_kol)).getText().toString();

                        Search_Image(l_kod);
                        Log.e("WJAksia= ", l_name);
                        Log.e("WJAksia= ", l_cena);
                        Log.e("WJAksia= ", l_ostatok);
                        Log.e("WJAksia= ", l_kod);
                        Log.e("WJAksia= ", l_kol);
                        Log.e("WJAksia= ", l_image);*/

                        //   Fun_Messeger_Panel(l_name, l_kod, l_cena, l_kol, l_ostatok); // НУЖНО ИЗМЕНИТЬ ЗАГРУЗИТЬ

                        ////////////////////////////  Основные переменные для диалога из listview
                        list_tw_aks_name = localView_list.findViewById(R.id.tvw_text_aks_name);
                        list_tw_aks_uid = localView_list.findViewById(R.id.tvw_kod_aks);
                        list_tw_aks_cena = localView_list.findViewById(R.id.tvw_text_aks_cena);
                        list_tw_aks_ostatok = localView_list.findViewById(R.id.tvw_text_aks_ostatok);
                        list_tw_aks_skidka = localView_list.findViewById(R.id.tvw_aks_skidka);
                        list_tw_aks_sklad_uid = localView_list.findViewById(R.id.tvw_aks_sklad_uid);
                        list_tw_aks_cena_bonus = localView_list.findViewById(R.id.tvw_aks_price_bonus);
                        list_tv_aks_zakaz = localView_list.findViewById(R.id.tvw_aks_kol);

                      //  l_kod = ((TextView) localView_list.findViewById(R.id.tvw_kod_aks)).getText().toString();

                        //45654Log.e("uid_tovar", l_kod);
                        Log.e("uid_tovar", list_tw_aks_uid.getText().toString());
                        Log.e("uid_sklad", list_tw_aks_sklad_uid.getText().toString());
                        Func_Messeger_Panel(list_tw_aks_uid.getText().toString(), list_tw_aks_sklad_uid.getText().toString(), "");

                    }
                    break;
                }
            }
        });


        floatingActionButton_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (tvw_uslov_pref1.getText().toString()) {
                    case "kol": {
                        if (Double.parseDouble(tvw_sum.getText().toString()) <= Double.parseDouble(win_aks_kol.getText().toString())) {
                            Toast.makeText(context_Activity, "Акция проходит!", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(context_Activity, "Акция  не проходит!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case "sum": {
                        if (Double.parseDouble(tvw_sum.getText().toString()) <= Double.parseDouble(win_aks_summa.getText().toString())) {
                            Toast.makeText(context_Activity, "Акция проходит!", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(context_Activity, "Акция  не проходит!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                    default:
                        break;
                }
            }
        });
    }


    protected void List_adapter_Brends(String data_this_work) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT DISTINCT data_start, data_end, brend_name \n" +
                "FROM base_aksia\n" +
                "WHERE data_start <= '" + data_this_work + "' AND data_end >= '" + data_this_work + "'\n" +
                "ORDER BY brend_name;";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        mass_brends = new String[cursor.getCount()];
        while (cursor.isAfterLast() == false) {
            String brend = cursor.getString(cursor.getColumnIndex("brend_name"));
            mass_brends[cursor.getPosition()] = brend;
            Log.e("Name_Brends ", brend);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }  // Загрузка данных на первую страницу: акции бренды

    protected void Loading_Brends_Click(String brend) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT brend_name, bonus_name \n" +
                "FROM base_aksia\n" +
                "WHERE brend_name = '" + brend + "'\n" +
                "GROUP BY bonus_name";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        mass_aksia_brends = new String[cursor.getCount()];
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndex("bonus_name"));
            mass_aksia_brends[cursor.getPosition()] = name;
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }         // Загрузка данных на первую страницу: акции суббренды

    protected void ListView_Adapter(String name_aks, String data_this_work) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT base_aksia.bonus_name, base_aksia.data_start, base_aksia.data_end, " +
                "base_aksia.brend_name, base_aksia.brend_subname, base_aksia.tovar_name, " +
                "base_aksia.tovar_uid, base_in_price.price, const_sklad.sklad_name, " +
                "base_in_ostatok.sklad_uid, base_in_ostatok.count, base_in_image.kod_image\n" +
                "FROM base_aksia\n" +
                "LEFT JOIN base_in_price ON base_aksia.tovar_uid = base_in_price.nomenclature_uid\n" +
                "LEFT JOIN base_in_ostatok ON base_aksia.tovar_uid = base_in_ostatok.nomenclature_uid\n" +
                "LEFT JOIN base_in_image ON base_aksia.tovar_uid = base_in_image.koduid\n" +
                "LEFT JOIN base_group_sql ON base_aksia.tovar_uid = base_group_sql.uid_name\n" +
                "LEFT JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid\n" +
                "WHERE base_in_ostatok.count > 0 AND base_aksia.bonus_name = '" + name_aks + "' AND (base_aksia.data_start <= '" + data_this_work + "' AND base_aksia.data_end >= '" + data_this_work + "')\n" +
                "ORDER BY base_group_sql.type_group ASC;";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndex("tovar_name"));
            String kod_uid = cursor.getString(cursor.getColumnIndex("tovar_uid"));
            String cena = cursor.getString(cursor.getColumnIndex("price"));
            String ostatok = cursor.getString(cursor.getColumnIndex("count"));
            String image = cursor.getString(cursor.getColumnIndex("kod_image"));
            String sklad = cursor.getString(cursor.getColumnIndex("sklad_name"));
            String sklad_uid = cursor.getString(cursor.getColumnIndex("sklad_uid"));

            aksia.add(new ListAdapterSimple_Forma_Aksia(name, kod_uid, cena, "", "", ostatok, "", image, sklad, sklad_uid));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    } // Заполнение данными ListView

    protected void Loading_Brends_Uls_Aks(String name_aks, String data_this_work) {
        Log.e("WJAksia= ", "aks=" + name_aks);
        // Loading_Akcia_AutoSum(name_aks);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT DISTINCT data_start, data_end, bonus_name, bonus_uid, bonus_type, bonus_type_1, bonus_skidka_1, bonus_type_2, bonus_skidka_2, bonus_type_3, bonus_skidka_3 \n" +
                "FROM base_aksia\n" +
                "WHERE bonus_name = '" + name_aks + "' AND  (data_start <= '" + data_this_work + "' AND data_end >= '" + data_this_work + "')";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0 & cursor != null) {
            String uslov_1 = cursor.getString(cursor.getColumnIndex("bonus_type_1"));
            String uslov_2 = cursor.getString(cursor.getColumnIndex("bonus_type_2"));
            String uslov_3 = cursor.getString(cursor.getColumnIndex("bonus_type_3"));
            String uslov_skidka_1 = cursor.getString(cursor.getColumnIndex("bonus_skidka_1"));
            String uslov_skidka_2 = cursor.getString(cursor.getColumnIndex("bonus_skidka_2"));
            String uslov_skidka_3 = cursor.getString(cursor.getColumnIndex("bonus_skidka_3"));
            String type_uslov = cursor.getString(cursor.getColumnIndex("bonus_type"));

            if (uslov_1 != null) {
                win_layout_uslov_1.setVisibility(View.VISIBLE);
                win_layout_uslov_2.setVisibility(View.GONE);
                win_layout_uslov_3.setVisibility(View.GONE);
                win_aks_skidka_1.setText(uslov_skidka_1);
                win_aks_uslov_1.setText(uslov_1);
                usl_kol_skidka = 1;
            } else {
                // linearLayout_1.setVisibility(View.GONE);
            }
            if (uslov_2 != null) {
                win_layout_uslov_1.setVisibility(View.VISIBLE);
                win_layout_uslov_2.setVisibility(View.VISIBLE);
                win_layout_uslov_3.setVisibility(View.GONE);
                win_aks_skidka_2.setText(uslov_skidka_2);
                win_aks_uslov_2.setText(uslov_2);
                usl_kol_skidka = 2;
            } else {
                // linearLayout_1.setVisibility(View.GONE);
                //linearLayout_2.setVisibility(View.GONE);
            }
            if (uslov_3 != null) {
                win_layout_uslov_1.setVisibility(View.VISIBLE);
                win_layout_uslov_2.setVisibility(View.VISIBLE);
                win_layout_uslov_3.setVisibility(View.VISIBLE);
                win_aks_skidka_3.setText(uslov_skidka_3);
                win_aks_uslov_3.setText(uslov_3);
                usl_kol_skidka = 3;
            } else {
                // linearLayout_1.setVisibility(View.GONE);
                //  linearLayout_2.setVisibility(View.GONE);
                //   linearLayout_3.setVisibility(View.GONE);
            }
            if (type_uslov.equals("kol")) {
                win_aks_type_aks.setText("kolvo");
                tvw_uslov_pref1.setText("шт");
                tvw_uslov_pref2.setText("шт");
                tvw_uslov_pref3.setText("шт");

            } else {
                win_aks_type_aks.setText("summa");
                tvw_uslov_pref1.setText("c");
                tvw_uslov_pref2.setText("c");
                tvw_uslov_pref3.setText("c");
            }
        } else {
            Log.e("WJAksia= ", "Нет акций");
            Toast.makeText(context_Activity, "Товар по акции закончился!", Toast.LENGTH_SHORT).show();
        }
        try {

        } catch (Exception e) {
            Log.e("WJAksia= ", "Нет акций");
            Toast.makeText(context_Activity, "Товар по акции закончился!", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }

    protected void Loading_Akcia_New_Kol(String w_koduid, String w_kol, String w_skidka) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT * FROM base_aksia WHERE tovar_uid = '" + w_koduid + "'";
        final Cursor cursor = db.rawQuery(query, null);
        ContentValues localContentValuesUP = new ContentValues();
        cursor.moveToFirst();
        localContentValuesUP.put("tovar_uid", w_koduid);
        localContentValuesUP.put("write_kol", w_kol);
        localContentValuesUP.put("write_skidka", usl_w_skidka.toString());
        db.update("base_aksia", localContentValuesUP, "tovar_uid = ?", new String[]{w_koduid});
        cursor.close();
        db.close();
    }

    protected void Loading_Akcia_AutoSum(String w_name_aks) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT base_aksia.bonus_name, base_aksia.kod_uid, base_aksia.write_kol, base_in_price.price\n" +
                "FROM base_aksia\n" +
                "LEFT JOIN base_in_price ON base_aksia.kod_uid = base_in_price.nomenclature_uid\n" +
                "WHERE base_aksia.name_aks = '" + w_name_aks + "';";
        usl_sum_all = 0.0;
        usl_itogo_all = 0.0;
        usl_kol_all = 0;
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String kol = cursor.getString(cursor.getColumnIndex("write_kol"));
            String price = cursor.getString(cursor.getColumnIndex("price"));
            if (Integer.parseInt(kol) > 0) {
                usl_sum_all = usl_sum_all + (Double.parseDouble(kol) * Double.parseDouble(price));
                usl_kol_all = usl_kol_all + Integer.parseInt(kol);
            }
            cursor.moveToNext();
        }
        String Format = new DecimalFormat("#00.00").format(usl_sum_all).replace(",", ".");
        win_aks_summa.setText(Format);
        win_aks_kol.setText(usl_kol_all.toString());
        cursor.close();
        db.close();
    }  // Подсчет сумм, кол-ва товара по акции

    @Override
    public void onBackPressed() {
        /*dialog = new androidx.appcompat.app.AlertDialog.Builder(context_Activity);
        dialog.setTitle("Вы хотите отменить заказ?");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Delete_RN_BASE();
                Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza.class);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
            }
        });
        dialog.show();*/
        list--;
        switch (list) {
            case 2: {
                Log.e("WJAksia= ", getSupportActionBar().getSubtitle().toString());
                Log.e("WJAksia= ", getSupportActionBar().getTitle().toString());
                Loading_Brends_Click(getSupportActionBar().getTitle().toString());
                adapter_aksia_brends = new ArrayAdapter<>(context_Activity,
                        android.R.layout.simple_list_item_1, mass_aksia_brends);
                listView.setAdapter(adapter_aksia_brends);
                getSupportActionBar().setSubtitle("");
                win_layout_uslov_1.setVisibility(View.GONE);
                win_layout_uslov_2.setVisibility(View.GONE);
                win_layout_uslov_3.setVisibility(View.GONE);
                win_aks_type_aks.setText("");
                win_aks_summa.setText("0.0");
                win_aks_kol.setText("0");
            }
            break;
            case 1: {
                List_adapter_Brends(this_data_work_now);
                adapter_brends = new ArrayAdapter<>(context_Activity,
                        android.R.layout.simple_list_item_1, mass_brends);
                listView.setAdapter(adapter_brends);
                getSupportActionBar().setTitle("Акции");
                win_layout_uslov_1.setVisibility(View.GONE);
                win_layout_uslov_2.setVisibility(View.GONE);
                win_layout_uslov_3.setVisibility(View.GONE);
                win_aks_type_aks.setText("");
                win_aks_summa.setText("0.0");
                win_aks_kol.setText("0");
            }
            break;

            default: {
                Intent intent = new Intent(context_Activity, WJ_Global_Activity.class);
                startActivity(intent);
                finish();
               /* dialog = new androidx.appcompat.app.AlertDialog.Builder(context_Activity);
                dialog.setTitle("Вы хотите отменить заказ?");
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // Delete_RN_BASE();
                        Intent intent = new Intent(context_Activity, WJ_Global_Activity.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
                dialog.show();*/
            }
            break;
        }
        // super.onBackPressed();

    }

    protected void Func_Messeger_Panel(String list_koduid, final String list_skladkod, final String m_kol) {
       // localView_list = LayoutInflater.from(context_Activity).inflate(R.layout.list_wj_akcia, null);
        localView_dialog = LayoutInflater.from(context_Activity).inflate(R.layout.list_ros_torg_add, null);


        ////////////////////////////

        ////////////////////////////  Основные переменные для диалога из Dialog
        dg_tw_aks_name = localView_dialog.findViewById(R.id.tvw_text_aks_name);
        dg_tw_aks_uid = localView_dialog.findViewById(R.id.tvw_text_aks_koduid);
        dg_tw_aks_univ = localView_dialog.findViewById(R.id.tvw_text_aks_koduniv);
        dg_tw_aks_cena = localView_dialog.findViewById(R.id.tvw_text_aks_cena);
        dg_tw_aks_ostatok = localView_dialog.findViewById(R.id.tvw_text_aks_ostatok);
        dg_tw_aks_kolbox = localView_dialog.findViewById(R.id.tvw_text_aks_kolbox);
        dg_tv_aks_count = localView_dialog.findViewById(R.id.tvw_text_aks_kol);
        dg_tv_aks_summa = localView_dialog.findViewById(R.id.tvw_text_aks_summa);
        dg_tv_aks_sklad_uid = localView_dialog.findViewById(R.id.tvw_text_aks_kodsklad);
        dg_edit_aks_kol = localView_dialog.findViewById(R.id.tvw_edit_aks_kol);
        dg_button_add = localView_dialog.findViewById(R.id.btn_add_up);
        dg_button_down = localView_dialog.findViewById(R.id.btn_add_down);
        dg_radioGroup = localView_dialog.findViewById(R.id.radiogroup_type);
        dg_layout_button_box = localView_dialog.findViewById(R.id.const_layout_button);
        ////////////////////////////


        dg_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.r_btn_kol: {
                        dg_layout_button_box.setVisibility(View.VISIBLE);
                        dg_edit_aks_kol.setVisibility(View.GONE);
                        dg_tv_aks_count.setText(m_kol);
                        hideInputMethod();
                        pref_type_group = "kol";
                    }
                    break;
                    case R.id.r_btn_box: {
                        dg_layout_button_box.setVisibility(View.VISIBLE);
                        dg_edit_aks_kol.setVisibility(View.GONE);
                        dg_edit_aks_kol.setText("");
                        hideInputMethod();
                        pref_type_group = "box";
                    }
                    break;
                    case R.id.r_btn_edit: {
                        dg_layout_button_box.setVisibility(View.GONE);
                        dg_edit_aks_kol.setVisibility(View.VISIBLE);
                        dg_edit_aks_kol.setText(m_kol);
                        dg_edit_aks_kol.clearFocus();
                        dg_edit_aks_kol.requestFocus();
                        dg_edit_aks_kol.setFocusable(true);
                        //  showInputMethod();
                        pref_type_group = "edit";
                    }
                    break;
                }
            }
        });



        SQL_DG_READ_DB(list_tw_aks_uid.getText().toString(), list_tw_aks_sklad_uid.getText().toString());


        Button_Click_Up(0, 0);
        Button_Click_Down(0);
        Button_Click_Edit(0, 0);


        AlertDialog.Builder localBuilder = new AlertDialog.Builder(context_Activity);
        localBuilder.setView(localView_dialog);
        localBuilder.setTitle("Кол-во товара...");
        localBuilder.setCancelable(false).setIcon(R.drawable.office_refresh).setPositiveButton(" ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                if (pref_type_group.equals("kol")) {
                    String write_koduid, write_kol, write_skidka;
                    write_koduid = dg_tw_aks_uid.getText().toString();
                    write_kol = dg_tv_aks_count.getText().toString();
                    // write_cena = dg_tw_cena.getText().toString();
                    Toast.makeText(context_Activity, "Товара kol", Toast.LENGTH_SHORT).show();
                    Loading_Akcia_New_Kol(write_koduid, write_kol, "");
                    //Loading_Akcia_AutoSum(strText);
                    aksia.clear();
                    ListView_Adapter(strText, this_data_work_now);
                    adapterPriceClients = new ListAdapterAde_Forma_Aksia(context_Activity, aksia);
                    adapterPriceClients.notifyDataSetChanged();
                    listView.setAdapter(adapterPriceClients);
                } else {
                    String write_koduid, write_kol, write_cena;
                    write_koduid = dg_tw_aks_uid.getText().toString();
                    write_kol = dg_edit_aks_kol.getText().toString();
                    write_cena = dg_tw_aks_cena.getText().toString();
                    Log.e("WJAksia= ", "поле= |" + write_kol.length() + "|");
                    if (!write_kol.equals("") | write_kol.length() != 0) {
                        Log.e("WJAksia= ", "Write= |" + write_kol.length() + "|");
                        Toast.makeText(context_Activity, "Товара edit", Toast.LENGTH_SHORT).show();
                        Loading_Akcia_New_Kol(write_koduid, write_kol, write_cena);
                        // Loading_Akcia_AutoSum(strText);
                        aksia.clear();
                        ListView_Adapter(strText, this_data_work_now);
                        adapterPriceClients = new ListAdapterAde_Forma_Aksia(context_Activity, aksia);
                        adapterPriceClients.notifyDataSetChanged();
                        listView.setAdapter(adapterPriceClients);
                    } else
                        Toast.makeText(context_Activity, "поле не может быть пустым!", Toast.LENGTH_SHORT).show();


                }


            }
        }).setNegativeButton(" ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
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

    protected void Button_Click_Up(final Integer w_cena, final Integer w_ostatok) {
        dg_button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer i = Integer.parseInt(dg_tv_aks_count.getText().toString());
                i++;
                Toast.makeText(context_Activity, "Товара на складе" + w_ostatok, Toast.LENGTH_SHORT).show();
                if (i <= w_ostatok) {
                    dg_tv_aks_count.setText(i.toString());
                    perem_int_summa = w_cena * i;
                    String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                    dg_tv_aks_summa.setText(Format);
                } else
                    Toast.makeText(context_Activity, "не достаточно товара", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void Button_Click_Down(final Integer w_cena) {
        dg_button_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer i = Integer.parseInt(dg_tv_aks_count.getText().toString());
                if (i > 0) {
                    i--;
                    dg_tv_aks_count.setText(i.toString());
                    perem_int_summa = w_cena * i;
                    String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                    dg_tv_aks_summa.setText(Format);
                } else {
                    Toast.makeText(context_Activity, "Кол-во меньше 0", Toast.LENGTH_SHORT).show();
                    dg_tv_aks_summa.setText("00.00");
                }

            }
        });
    }

    protected void Button_Click_Edit(final Integer w_cena, final Integer w_ostatok) {
        dg_edit_aks_kol.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Toast.makeText(context_Activity, "Товара на складе" + w_ostatok, Toast.LENGTH_SHORT).show();
                if (charSequence.toString() != null & !charSequence.toString().equals("")) {
                    Integer kol = Integer.parseInt(charSequence.toString());
                    if (kol <= w_ostatok) {
                        perem_int_summa = w_cena * kol;
                        String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                        dg_tv_aks_summa.setText(Format);
                    } else {
                        dg_edit_aks_kol.setText(w_ostatok.toString());
                        dg_edit_aks_kol.setSelection(dg_edit_aks_kol.length());
                        Toast.makeText(context_Activity, "максимальное кол-во", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    dg_tv_aks_summa.setText("00.00");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e("I=", "i=" + editable);

            }
        });

    }

    protected void Button_Up(final Integer cena, final Integer ostatok, final Integer textbox) {
        dg_button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dg_tv_aks_count.getText().toString().equals("") | dg_tv_aks_count.getText().toString().equals(" ")) {
                    switch (checked_group) {
                        case R.id.radioButton_one: {
                            dg_tv_aks_count.setText("1");
                            Integer i = Integer.parseInt(dg_tv_aks_count.getText().toString());
                            i++;
                            Toast.makeText(context_Activity, "Товара на складе" + ostatok, Toast.LENGTH_SHORT).show();
                            if (i <= ostatok) {
                                dg_tv_aks_count.setText(i.toString());
                                perem_int_summa = cena * i;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                                dg_tv_aks_summa.setText(Format);
                                Summa_Skidka();
                            } else
                                Toast.makeText(context_Activity, "не достаточно товара", Toast.LENGTH_SHORT).show();

                        }
                        break;

                        case R.id.radioButton_much: {
                            /// dg_tw_kol.setText(text_box.replaceAll("[\\D]", ""));
                            dg_tv_aks_count.setText(textbox.toString());
                            Integer i = Integer.parseInt(dg_tv_aks_count.getText().toString());
                            //  Integer new_kol = Integer.parseInt(text_box.replaceAll("[\\D]", "")) + i;
                            Integer new_kol = textbox + i;
                            dg_tv_aks_count.setText(new_kol.toString());
                            perem_int_summa = cena * new_kol;
                            String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                            dg_tv_aks_summa.setText(Format);
                            kol_box_info++;
                            dg_tw_aks_kolbox.setText("x" + kol_box_info);
                            Summa_Skidka();

                        }
                        break;
                    }
                } else {
                    switch (checked_group) {
                        case R.id.radioButton_one: {
                            Integer i = Integer.parseInt(dg_tv_aks_count.getText().toString());
                            i++;
                            if (i <= ostatok) {
                                dg_tv_aks_count.setText(i.toString());
                                perem_int_summa = cena * i;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                                dg_tv_aks_summa.setText(Format);
                                Summa_Skidka();
                            } else
                                Toast.makeText(context_Activity, "не достаточно товара", Toast.LENGTH_SHORT).show();
                        }
                        break;

                        case R.id.radioButton_much: {

                            if (kol_box_info < max_box) {
                                Double db = ostatok / Double.parseDouble(kol_group_much);
                                Integer it = db.intValue();
                                Toast.makeText(context_Activity, "кол-во кор=" + it, Toast.LENGTH_SHORT).show();
                                Integer i = Integer.parseInt(dg_tv_aks_count.getText().toString());
                                //  Integer new_kol = Integer.parseInt(text_box.replaceAll("[\\D]", "")) + i;
                                Integer new_kol = textbox + i;
                                dg_tv_aks_count.setText(new_kol.toString());
                                perem_int_summa = cena * new_kol;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                                ;
                                dg_tv_aks_summa.setText(Format);
                                kol_box_info++;
                                dg_tw_aks_kolbox.setText("x" + kol_box_info);
                                Summa_Skidka();
                            } else {
                                Toast.makeText(context_Activity, "макс. кол-во", Toast.LENGTH_SHORT).show();
                            }


                        }
                        break;
                    }

                }


            }
        });

    }  // добавления количества

    protected void Button_Down(final Integer cena, final Integer ostatok, final Integer textbox) {
        dg_button_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dg_tv_aks_count.getText().toString().equals("") | dg_tv_aks_count.getText().toString().equals(" ")) {
                    switch (checked_group) {
                        case R.id.radioButton_one: {
                            dg_tv_aks_count.setText("1");
                            Integer i = Integer.parseInt(dg_tv_aks_count.getText().toString());
                            if (i > 1) {
                                i--;
                                dg_tv_aks_count.setText(i.toString());
                                perem_int_summa = cena * i;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                                dg_tv_aks_summa.setText(Format);
                                Summa_Skidka();
                            } else
                                Toast.makeText(context_Activity, "Кол-во меньше 1", Toast.LENGTH_SHORT).show();
                        }
                        break;

                        case R.id.radioButton_much: {
                            //  dg_tw_kol.setText(text_box.replaceAll("[\\D]", ""));
                            dg_tv_aks_count.setText(textbox.toString());
                            Integer i = Integer.parseInt(dg_tv_aks_count.getText().toString());
                            //   if (i > Integer.parseInt(text_box.replaceAll("[\\D]", ""))) {
                            if (i > textbox) {
                                // Integer new_kol = i - Integer.parseInt(text_box.replaceAll("[\\D]", ""));
                                Integer new_kol = i - textbox;
                                dg_tv_aks_count.setText(new_kol.toString());
                                perem_int_summa = cena * new_kol;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                                dg_tv_aks_summa.setText(Format);
                                kol_box_info--;
                                dg_tw_aks_kolbox.setText("x" + kol_box_info);
                                Summa_Skidka();
                            } else
                                Toast.makeText(context_Activity, "Кол-во меньше кор.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                } else {
                    switch (checked_group) {
                        case R.id.radioButton_one: {
                            Integer i = Integer.parseInt(dg_tv_aks_count.getText().toString());
                            if (i > 1) {
                                i--;
                                dg_tv_aks_count.setText(i.toString());
                                perem_int_summa = cena * i;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                                dg_tv_aks_summa.setText(Format);
                                Summa_Skidka();
                            } else
                                Toast.makeText(context_Activity, "Кол-во меньше 1", Toast.LENGTH_SHORT).show();
                        }
                        break;

                        case R.id.radioButton_much: {
                            Integer i = Integer.parseInt(dg_tv_aks_count.getText().toString());
                            //  if (i > Integer.parseInt(text_box.replaceAll("[\\D]", ""))) {
                            if (i > textbox) {
                                //    Integer new_kol = i - Integer.parseInt(text_box.replaceAll("[\\D]", ""));
                                Integer new_kol = i - textbox;
                                dg_tv_aks_count.setText(new_kol.toString());
                                perem_int_summa = cena * new_kol;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                                ;
                                dg_tv_aks_summa.setText(Format);
                                kol_box_info--;
                                dg_tw_aks_kolbox.setText("x" + kol_box_info);
                                Summa_Skidka();
                            } else
                                Toast.makeText(context_Activity, "Кол-во меньше кор.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }

                }

            }
        });

    }  // убавить количество

    protected void Button_Edit(final Integer cena, final Integer ostatok) {

        dg_edit_aks_kol.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //  editText_kol.setText("1");
              /*  if (dg_ed_editkol.getText().toString().equals("") | dg_ed_editkol.getText().toString().equals(" ") | dg_ed_editkol.getText().toString().equals("0")) {
                    dg_tv_aks_summa.setText("00.00");
                    dg_tw_summa_sk.setText("00.00");
                    // Summa_Skidka();
                } else if (Integer.parseInt(dg_ed_editkol.getText().toString()) <= ostatok) {
                    //  cena_t = Double.parseDouble(text_cena.substring(0, text_cena.length() - 3));
                    //  perem_cena = Double.parseDouble(text_cena);
                    dg_tv_aks_count.setText(dg_ed_editkol.getText().toString());
                    perem_int_summa = Integer.parseInt(dg_ed_editkol.getText().toString()) * cena;
                    String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                    ;
                    dg_tv_aks_summa.setText(Format);
                    Summa_Skidka();
                } else {
                    Toast.makeText(context_Activity, "кол-во превышает остаток", Toast.LENGTH_SHORT).show();
                    dg_ed_editkol.setText(ostatok.toString());
                    dg_ed_editkol.setSelection(dg_ed_editkol.getText().length());
                    dg_tv_aks_count.setText(dg_ed_editkol.getText().toString());
                    perem_int_summa = Integer.parseInt(dg_ed_editkol.getText().toString()) * cena;
                    String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                    ;
                    dg_tv_aks_summa.setText(Format);
                    Summa_Skidka();
                }

*/
            }
        });
    }  // производбный ввод количества


    protected void Fun_Messeger_Panel(final String m_name, final String m_kod, final String m_cena, final String m_kol, final String m_ostatok) {
       /* localView = LayoutInflater.from(context_Activity).inflate(R.layout.mt_dialog_localview_add, null);

        dg_btn_up = (Button) localView.findViewById(R.id.btn_add_up);
        dg_btn_down = (Button) localView.findViewById(R.id.btn_add_down);

        dg_tw_name = (TextView) localView.findViewById(R.id.tvw_text_aks_name);
        dg_tw_koduniv = (TextView) localView.findViewById(R.id.text_d_koduid);
        dg_tw_cena = (TextView) localView.findViewById(R.id.tvw_text_aks_cena);
        dg_tw_ostatok = (TextView) localView.findViewById(R.id.tvw_text_aks_ostatok);
        dg_tw_kol = (TextView) localView.findViewById(R.id.tvw_text_aks_kol);
        dg_tw_summa = (TextView) localView.findViewById(R.id.tvw_text_aks_summa);

        dg_tw_name.setText(m_name);
        dg_tw_koduniv.setText(m_kod);
        dg_tw_cena.setText(m_cena);
        dg_tw_kol.setText(m_kol);
        dg_tw_ostatok.setText(m_ostatok);

        perem_int_cena = Integer.parseInt(m_cena);
        perem_int_kol = Integer.parseInt(m_kol);
        perem_int_ostatok = Integer.parseInt(m_ostatok);
        perem_int_summa = perem_int_cena * perem_int_kol;
        String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
        dg_tw_summa.setText(Format);*/

        if (perem_int_ostatok > 0) {
            Button_Up(perem_int_kol, perem_int_cena, perem_int_ostatok);
            Button_Down(perem_int_kol, perem_int_cena, perem_int_ostatok);

            AlertDialog.Builder localBuilder = new AlertDialog.Builder(context_Activity);
            // localBuilder.setView(localView);
            localBuilder.setTitle("Кол-во товара...");
            localBuilder.setCancelable(false).setIcon(R.drawable.office_refresh).setPositiveButton(" ", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    //   tvw_write_kol.setText(perem_int_kol.toString());
                    //   tvw_write_sum.setText(dg_tw_summa.getText());


                }
            }).setNegativeButton(" ", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    paramDialogInterface.cancel();
                }
            });
            AlertDialog localAlertDialog = localBuilder.create();
            localAlertDialog.show();

            button_ok = localAlertDialog.getButton(-1);
            button_ok.setCompoundDrawablesWithIntrinsicBounds(R.drawable.button_ok, 0, 0, 0);
            button_cancel = localAlertDialog.getButton(-2);
            button_cancel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_close, 0);
        } else
            Toast.makeText(context_Activity, "не достаточно товара", Toast.LENGTH_SHORT).show();
    }

    protected void Summa_Skidka() {
       /* if (dg_ed_skidka.getText().toString().equals("") | dg_ed_skidka.getText().toString().equals(" ") | dg_ed_skidka.getText().toString().equals("0")) {
            dg_tw_summa_sk.setVisibility(View.GONE);
            dg_tw_summa_sk.setText("00.00");
            dg_tw_summa_sk_DOUBLE = 00.00;
        } else {
            dg_tw_summa_sk.setVisibility(View.VISIBLE);
            Double per_sum = (perem_int_summa).doubleValue();
            Double per_sk = Double.parseDouble(dg_ed_skidka.getText().toString());

            Double per_itg = Double.valueOf(per_sum - (per_sum * (per_sk / 100)));
            dg_tw_summa_sk_DOUBLE = per_itg;
            String Format = new DecimalFormat("#00.00").format(per_itg).replace(",", ".");
            ;
            dg_tw_summa_sk.setText(Format);
            Log.e("WJ_FormaSK:", Format + ", " + per_sum + ", " + per_sk + ", " + per_itg);
        }*/
    }

    protected void Search_Image(String uid) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT base_in_nomeclature.name AS 'tovar', base_in_image.kod_image, base_in_nomeclature.koduid\n" +
                "FROM base_in_nomeclature \n" +
                "INNER JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                "WHERE base_in_nomeclature.koduid = '" + uid + "'";

        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex("tovar"));
        String image = cursor.getString(cursor.getColumnIndex("kod_image"));
        String koduid = cursor.getString(cursor.getColumnIndex("koduid"));
        Log.e("Err", name + koduid + image);
        l_image = image;
        cursor.close();
        db.close();
    }

    protected void Fun_Uslovie() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);

        String query = "SELECT brend, name_aks, name_tovar, kod_univ, kod_uid, base_in_price.price, SUM(base_in_ostatok.count) AS 'ostatok', image, par_uls, usl_osdo, usl_pres, skidka, kol \n" +
                "FROM base_aksia\n" +
                "INNER JOIN base_in_price ON base_aksia.kod_uid = base_in_price.nomenclature_uid\n" +
                "INNER JOIN base_in_ostatok ON base_aksia.kod_uid = base_in_ostatok.nomenclature_uid\n" +
                "WHERE name_aks = '" + getSupportActionBar().getSubtitle().toString() + "'\n" +
                "GROUP BY kod_uid\n" +
                "ORDER BY name ASC;";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Integer sum_kol = 0;
        while (cursor.isAfterLast() == false) {
            String kol = cursor.getString(cursor.getColumnIndex("kol"));
            String cena = cursor.getString(cursor.getColumnIndex("price"));
            String skidka = cursor.getString(cursor.getColumnIndex("skidka"));
            Integer i_kol = Integer.valueOf(kol);
            sum_kol = sum_kol + i_kol;
            // tvw_write_kol.setText(sum_kol.toString());

            Double d_cena, d_skidka, d_summa, d_summask;
            d_cena = Double.parseDouble(cena);
            d_skidka = d_cena * (Double.parseDouble(skidka) / 100);
            d_summask = sum_kol * (d_cena - d_skidka);
            String Format = new DecimalFormat("#00.00").format(d_summask).replace(",", ".");
            // tvw_write_sum.setText(Format);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();


        /*switch (tvw_usl.getText().toString()) {
            case "kol": {
                tvw_write_sum.setVisibility(View.GONE);
                tvw_write_kol.setVisibility(View.VISIBLE);
            }
            break;
            case "sum": {
                tvw_write_sum.setVisibility(View.VISIBLE);
                tvw_write_kol.setVisibility(View.GONE);
            }
            break;
        }*/

    }

    protected void Loading_Kol(String AK_univ, String AS_Kol) {
        Log.e("Load_kol", AK_univ + " " + AS_Kol);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT brend, name_aks, name_tovar, kod_univ, kod_uid, base_in_price.price, " +
                "SUM(base_in_ostatok.count) AS 'ostatok', image, par_uls, usl_osdo, usl_pres, skidka, kol \n" +
                "FROM base_aksia\n" +
                "INNER JOIN base_in_price ON base_aksia.kod_uid = base_in_price.nomenclature_uid\n" +
                "INNER JOIN base_in_ostatok ON base_aksia.kod_uid = base_in_ostatok.nomenclature_uid\n" +
                "WHERE kod_uid = '" + AK_univ + "' \n" +
                "GROUP BY kod_uid \n" +
                "ORDER BY name_tovar ASC;";
        final Cursor cursor = db.rawQuery(query, null);
        ContentValues localContentValues = new ContentValues();
        cursor.moveToFirst();
        String koduniv = cursor.getString(cursor.getColumnIndex("kod_uid"));
        localContentValues.put("kol", AS_Kol);
        String[] arrayOfString = new String[1];
        arrayOfString[0] = koduniv;
        db.update("base_aksia", localContentValues, "kod_uid = ?", new String[]{koduniv});
        cursor.close();
        db.close();
    }

    protected void Write_Table_RN(final String koduniv, final String name, final String kol, final String cena, final String summa, final String skidka, final Double summaSK, final String image) {

        Log.e("PEREM:", koduniv);
        Log.e("PEREM:", name);
        Log.e("PEREM:", kol);
        Log.e("PEREM:", cena);
        Log.e("PEREM:", summa);
        Log.e("PEREM:", skidka);
        Log.e("PEREM:", summaSK.toString());
        Log.e("PEREM:", image);

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
        String query_Search = "SELECT Kod_RN, koduid, name, kol\n" +
                "FROM base_RN_Aksia\n" +
                "WHERE koduid = '" + koduniv + "' AND Kod_RN = '" + PEREM_RNKod + "'";

        // Заполнения карточки товара
        // Проверка сходства позиций
        /*Log.e("КодUN=", koduniv);
        Log.e("КодРН=", PEREM_RNKod);*/
        Cursor cursor = db.rawQuery(query_Search, null);
        ContentValues localContentValues = new ContentValues();
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            if (Integer.parseInt(kol) == 0) {
                Toast.makeText(context_Activity, "Товар не добавлен!", Toast.LENGTH_SHORT).show();
            } else {
                localContentValues.put("name_aks", getSupportActionBar().getSubtitle().toString());
                localContentValues.put("Agent", PEREM_AG_NAME);
                localContentValues.put("Kod_RN", PEREM_RNKod);
                localContentValues.put("Klients", PEREM_KAgent);
                localContentValues.put("UID_Klients", PEREM_KAgent_UID);
                localContentValues.put("Adress", PEREM_Adress);
                localContentValues.put("Vrema", PEREM_Vrema);
                localContentValues.put("Data", PEREM_Data);
                localContentValues.put("Kod_Univ", koduniv);
                localContentValues.put("koduid", koduniv);
                localContentValues.put("Image", image);
                localContentValues.put("Name", name);
                localContentValues.put("Kol", kol);
                localContentValues.put("Cena", cena);
                localContentValues.put("Summa", summa);
                localContentValues.put("Skidka", skidka);
                localContentValues.put("Cena_SK", "");
                //localContentValues.put("Itogo", Format_summa.replaceAll(",", "."));
                localContentValues.put("Itogo", "");

                db.insert("base_RN_Aksia", null, localContentValues);
                Toast.makeText(context_Activity, "Товар добавлен!", Toast.LENGTH_SHORT).show();
            }

        } else {
            while (cursor.isAfterLast() == false) {
                String kod_univ = cursor.getString(cursor.getColumnIndex("koduid"));
                if (koduniv.equals(kod_univ)) {
                    if (Integer.parseInt(kol) == 0) {
                        Log.e("Код=", kod_univ);
                        localContentValues.put("Kol", kol);
                        String[] arrayOfString = new String[1];
                        arrayOfString[0] = koduniv;
                        db.delete("base_RN_Aksia", "Kod_Univ = ?", new String[]{koduniv});
                        Toast.makeText(context_Activity, "Кол-во изменено!", Toast.LENGTH_SHORT).show();
                        cursor.moveToNext();
                    } else {
                        Log.e("Код=", kod_univ);
                        localContentValues.put("Kol", kol);
                        String[] arrayOfString = new String[1];
                        arrayOfString[0] = koduniv;
                        db.update("base_RN_Aksia", localContentValues, "kod_univ = ?", new String[]{koduniv});
                        Toast.makeText(context_Activity, "Кол-во изменено!", Toast.LENGTH_SHORT).show();
                        cursor.moveToNext();
                    }

                } else cursor.moveToNext();


            }
        }


        localContentValues.clear();
        cursor.close();
        db.close();
    }

    protected void SQL_DB_WRITE() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        SQLiteDatabase db_add = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT name_tovar, write_kol, write_skidka FROM base_aksia\n" +
                "WHERE write_kol > 0 AND write_skidka > 0\n" +
                "GROUP BY write_skidka";

        final Cursor cursor = db.rawQuery(query, null);
        ContentValues localContentValues = new ContentValues();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            Integer w_kol = 0;
            String w_skidka = cursor.getString(cursor.getColumnIndex("kod_univ"));
            String w_skidka12 = cursor.getString(cursor.getColumnIndex("name_tovar"));

           /* data_start TEXT,
            data_end TEXT,
            brend TEXT,
            brend_sub TEXT,
            kod_id TEXT,
            name_aks TEXT,
            type_uslov TEXT,
            uslov_1 TEXT,
            uslov_skidka_1 TEXT,
            uslov_2 TEXT,
            uslov_skidka_2 TEXT,
            uslov_3 TEXT,
            uslov_skidka_3 TEXT,
            name_tovar TEXT,
            kod_uid TEXT,
            kod_univ TEXT,
            write_kol INTEGER,
            write_skidka INTEGER*/
        }


        localContentValues.put("Kod_RN", PEREM_RNKod);
        localContentValues.put("Vrema", PEREM_Vrema);
        localContentValues.put("Data", PEREM_Data);
        localContentValues.put("Kod_Univ", cursor.getString(cursor.getColumnIndex("kod_univ")));
        localContentValues.put("koduid", cursor.getString(cursor.getColumnIndex("kod_uid")));
        localContentValues.put("Image", "");
        localContentValues.put("Name", cursor.getString(cursor.getColumnIndex("name_tovar")));
        localContentValues.put("Kol", String.valueOf(cursor.getInt(cursor.getColumnIndex("write_kol"))));
        localContentValues.put("Cena", "");
        localContentValues.put("Summa", "");
        localContentValues.put("Skidka", String.valueOf(cursor.getInt(cursor.getColumnIndex("write_skidka"))));
        localContentValues.put("Cena_SK", "");
        localContentValues.put("Itogo", "");
        localContentValues.put("aks_pref", "");
        localContentValues.put("aks_name", "");
        localContentValues.put("sklad_name", "");
        localContentValues.put("sklad_uid", "");


        localContentValues.put("name_aks", getSupportActionBar().getSubtitle().toString());
        localContentValues.put("Agent", PEREM_AG_NAME);

        localContentValues.put("Klients", PEREM_KAgent);
        localContentValues.put("UID_Klients", PEREM_KAgent_UID);
        localContentValues.put("Adress", PEREM_Adress);


        db.insert("base_RN_Aksia", null, localContentValues);
        cursor.close();
        db.close();


    }

    protected void SQL_ADD_WRITE(Integer int_skidka) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT * FROM base_aksia\n" +
                "WHERE write_skidka = '" + int_skidka + "'";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            Integer w_skidka = cursor.getInt(cursor.getColumnIndex("write_skidka"));
            SQL_ADD_WRITE(w_skidka);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }


    /* protected void Write_This_Aksia(String w_query, String w_kodrn, String w_data_up, String w_summa, String w_skidka, String w_itogo)
     {
         SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
        // String query_Search = "SELECT * FROM base_RN_All";
         String query_Search = "INSERT INTO base_RN_All (kod_rn, agent_name, agent_uid, k_agn_uid, " +
                 "k_agn_name, data_xml, credit, sklad, sklad_uid, cena_price, coment, " +
                 "uslov_nds, skidka_title, credite_date, k_agn_adress, data, vrema, data_up, summa, skidka, itogo) " +
                 "VALUES (" +
                 "'" + PEREM_K_AG_KodRN + "', " +
                 "'" + PEREM_AG_NAME + "', " +
                 "'" + PEREM_AG_UID + "', " +
                 "'" + PEREM_K_AG_UID + "', " +
                 "'" + PEREM_K_AG_NAME + "', " +
                 "'" + PEREM_K_AG_Data_WORK + "', " +
                 "'" + PEREM_DOP_CREDIT + "', " +
                 "'" + PEREM_AG_SKLAD + "', " +
                 "'" + PEREM_AG_UID_SKLAD + "', " +
                 "'" + PEREM_AG_CENA + "', " +
                 "'" + "Дата отгрузки " + PEREM_DOP_DATA_UP + "; скидка " + PEREM_DOP_SKIDKA + "%;', " +
                 "'" + PEREM_DOP_NDS + "', " +
                 "'" + w_skidka + "', " +
                 "'" + PEREM_DOP_CREDITE_DATE + "', " +
                 "'" + PEREM_K_AG_ADRESS + "', " +
                 "'" + PEREM_K_AG_Data + "', " +
                 "'" + PEREM_K_AG_Vrema + "', " +
                 "'" + PEREM_DOP_DATA_UP.replaceAll("-", ".") + "', " +
                 "'" + textView_aut_summa.getText().toString().replace(",", ".") + "', " +
                 "'" + PEREM_DOP_SKIDKA + "', " +
                 "'" + textView_itog.getText().toString().replace(",", ".") + "');";

         Cursor cursor = db.rawQuery(query_Search, null);
         ContentValues localContentValues = new ContentValues();
         cursor.moveToLast();
         localContentValues.put("name_aks", getSupportActionBar().getSubtitle().toString());
         db.insert("base_RN_Aksia", null, localContentValues);

         while (cursor.isAfterLast() == false) {
             String kod_univ = cursor.getString(cursor.getColumnIndex("koduid"));
             if (koduniv.equals(kod_univ)) {
                 if (Integer.parseInt(kol) == 0) {
                     Log.e("Код=", kod_univ);
                     localContentValues.put("Kol", kol);
                     String[] arrayOfString = new String[1];
                     arrayOfString[0] = koduniv;
                     db.delete("base_RN_Aksia", "Kod_Univ = ?", new String[]{koduniv});
                     Toast.makeText(context_Activity, "Кол-во изменено!", Toast.LENGTH_SHORT).show();
                     cursor.moveToNext();
                 } else {
                     Log.e("Код=", kod_univ);
                     localContentValues.put("Kol", kol);
                     String[] arrayOfString = new String[1];
                     arrayOfString[0] = koduniv;
                     db.update("base_RN_Aksia", localContentValues, "kod_univ = ?", new String[]{koduniv});
                     Toast.makeText(context_Activity, "Кол-во изменено!", Toast.LENGTH_SHORT).show();
                     cursor.moveToNext();
                 }

             } else cursor.moveToNext();


         }



         localContentValues.clear();
         cursor.close();
         db.close();
     }*/
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

        for (int i = 0; i < getResources().getStringArray(R.array.mass_for_update_data).length; i++) {
            Save_dialog_up_ = sp.getString("Save_dialog_up_" + i, "0");
        }
    }

    // Константы для записи
    protected void Constanta_Write() {
        ed = sp.edit();
        ed.putString("PEREM_KLIENT_UID", "");  // передача кода выбранного uid клиента
        ed.putString("PEREM_DIALOG_UID", "");  // передача кода выбранного uid клиента
        ed.putString("PEREM_DIALOG_DATA_START", "");  // передача кода начальной даты
        ed.putString("PEREM_DIALOG_DATA_END", "");  // передача кода конечной даты
        ed.putString("PEREM_DISPLAY_START", "");  // передача кода начальной даты
        ed.putString("PEREM_DISPLAY_END", "");  // передача кода конечной даты
        ed.commit();
    }

    // Загрузка даты и время
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

        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(this_rn_year), Integer.valueOf(this_rn_month) - 1, Integer.valueOf(this_rn_day));
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateString_WORK = dateFormat2.format(calendar.getTime());
        this_data_work_now = dateString_WORK;
    }

    protected void SQL_DG_READ_DB(String w_koduid, String w_sklad_uid) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT base_in_nomeclature.name, base_in_nomeclature.koduid, " +
                "base_in_nomeclature.kod_univ, base_in_ostatok.sklad_uid, base_in_price.price, " +
                "base_in_ostatok.count, base_in_nomeclature.kolbox, base_in_image.kod_image, const_sklad.sklad_name\n" +
                "FROM base_in_nomeclature\n" +
                "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                "LEFT JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid\n" +
                "LEFT JOIN base_group_sql ON base_in_nomeclature.koduid = base_group_sql.uid_name\n" +
                "WHERE base_in_nomeclature.koduid = '" + w_koduid + "' AND base_in_ostatok.sklad_uid = '" + w_sklad_uid + "'";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0 & cursor != null) {
            dg_tw_aks_name.setText(cursor.getString(cursor.getColumnIndex("name")));
            dg_tw_aks_uid.setText(cursor.getString(cursor.getColumnIndex("koduid")));
            dg_tw_aks_univ.setText(cursor.getString(cursor.getColumnIndex("kod_univ")));
            dg_tw_aks_cena.setText(cursor.getString(cursor.getColumnIndex("price")));
            dg_tw_aks_ostatok.setText(cursor.getString(cursor.getColumnIndex("count")));
            dg_tw_aks_kolbox.setText(cursor.getString(cursor.getColumnIndex("kolbox")));
            dg_tv_aks_summa.setText("000.00");
            dg_tv_aks_sklad_uid.setText(cursor.getString(cursor.getColumnIndex("sklad_uid")));

        } else {
          /*  dg_tw_name.setText("null");
            dg_tw_koduid.setText("null");
            dg_tw_koduniv.setText("null");
            dg_tw_cena.setText("null");
            dg_tw_ostatok.setText("null");
            dg_tw_summa.setText("null");*/
        }

        cursor.close();
        db.close();

    }


    /**
     * прячем программную клавиатуру
     */
    protected void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(dg_edit_aks_kol.getWindowToken(), 0);
            // imm.hideSoftInputFromWindow(listView.getWindowToken(), 0);
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


/* if (tvw_type_usl.getText().toString().equals("kolvo")) {
                usl_w_this = Double.parseDouble(tvw_write_kol.getText().toString());
            } else {
                usl_w_this = Double.parseDouble(tvw_write_sum.getText().toString());
            }
            usl_w_summa = Double.parseDouble(tvw_write_sum.getText().toString());

            switch (usl_kol_skidka) {
                case 1: {
                    usl_w_min = Integer.parseInt(text_uslov_1.getText().toString());
                    if (usl_w_this >= usl_w_min) {
                        usl_w_skidka = Integer.parseInt(text_skidka_usl_1.getText().toString());
                        uslov_image_1.setVisibility(View.VISIBLE);
                        uslov_image_2.setVisibility(View.GONE);
                    } else {
                        usl_w_skidka = 0;
                        uslov_image_1.setVisibility(View.GONE);
                        uslov_image_2.setVisibility(View.GONE);
                        uslov_image_3.setVisibility(View.GONE);
                    }
                }
                break;
                case 2: {
                    if (usl_w_this >= Integer.parseInt(text_uslov_2.getText().toString())) {
                        usl_w_skidka = Integer.parseInt(text_skidka_usl_2.getText().toString());
                        uslov_image_1.setVisibility(View.GONE);
                        uslov_image_2.setVisibility(View.VISIBLE);
                    } else if (usl_w_this >= Integer.parseInt(text_uslov_1.getText().toString())) {
                        usl_w_skidka = Integer.parseInt(text_skidka_usl_1.getText().toString());
                        uslov_image_1.setVisibility(View.VISIBLE);
                        uslov_image_2.setVisibility(View.GONE);
                    } else {
                        usl_w_skidka = 0;
                        uslov_image_1.setVisibility(View.GONE);
                        uslov_image_2.setVisibility(View.GONE);
                        uslov_image_3.setVisibility(View.GONE);
                    }
                }
                break;
                case 3: {
                    if (usl_w_this >= Integer.parseInt(text_uslov_3.getText().toString())) {
                        usl_w_skidka = Integer.parseInt(text_skidka_usl_3.getText().toString());
                        uslov_image_1.setVisibility(View.GONE);
                        uslov_image_2.setVisibility(View.GONE);
                        uslov_image_3.setVisibility(View.VISIBLE);
                    } else if (usl_w_this >= Integer.parseInt(text_uslov_2.getText().toString())) {
                        usl_w_skidka = Integer.parseInt(text_skidka_usl_2.getText().toString());
                        uslov_image_1.setVisibility(View.GONE);
                        uslov_image_2.setVisibility(View.VISIBLE);
                        uslov_image_3.setVisibility(View.GONE);
                    } else if (usl_w_this >= Integer.parseInt(text_uslov_1.getText().toString())) {
                        usl_w_skidka = Integer.parseInt(text_skidka_usl_1.getText().toString());
                        uslov_image_1.setVisibility(View.VISIBLE);
                        uslov_image_2.setVisibility(View.GONE);
                        uslov_image_3.setVisibility(View.GONE);
                    } else {
                        usl_w_skidka = 0;
                        uslov_image_1.setVisibility(View.GONE);
                        uslov_image_2.setVisibility(View.GONE);
                        uslov_image_3.setVisibility(View.GONE);
                    }
                }
                break;
            }

            usl_itogo_all = usl_w_summa - (usl_w_summa * (Double.valueOf(usl_w_skidka) / 100));
            String Format2 = new DecimalFormat("#00.00").format(usl_itogo_all).replace(",", ".");
            tvw_write_itogo.setText(Format2);
            Log.e("Сумма: ", "_" + usl_w_summa);
            Log.e("Скидка: ", "_" + usl_w_skidka);
            Log.e("Итого: ", "_" + usl_itogo_all);

            String skidka = usl_w_skidka.toString();
            Log.e("Name", name);
            Double d_cena, d_skidka, d_summa;
            d_cena = Double.parseDouble(cena);
            d_skidka = d_cena * (Double.parseDouble(skidka) / 100);
            d_summa = d_cena - d_skidka;
            //PEREM_CenaAKS = d_summa.toString();
            String cena_aks = new DecimalFormat("#00.00").format(d_summa).replace(",", ".");
            l_ak_kol = "0";
* */