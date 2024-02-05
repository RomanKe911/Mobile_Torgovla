package kg.roman.Mobile_Torgovla.Work_Journal;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import kg.roman.Mobile_Torgovla.FormaZakaza.WJ_Forma_Zakaza_L2;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_WJ_Favorite;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_WJ_Favorite;
import kg.roman.Mobile_Torgovla.R;

public class WJ_Favorites extends AppCompatActivity {

    public ArrayList<ListAdapterSimple_WJ_Favorite> favorite = new ArrayList<ListAdapterSimple_WJ_Favorite>();
    public ListAdapterAde_WJ_Favorite adapter_favorite;

    public ListView lvw_favorites;
    public Button button_ok, button_cancel;
    public View localView;
    public TextView dg_tw_name, dg_tw_koduniv, dg_tw_cena, dg_tw_ostatok, dg_tw_kol, dg_tw_summa, dg_tw_summa_sk, dg_tw_kolbox, dg_tw_kolbox_org;
    public EditText dg_ed_skidka, dg_ed_editkol;
    public Button btn_down, btn_up;
    public RadioGroup radioGroup_local;
    public RadioButton radioGroup_one, radioGroup_much, radioGroup_edit;

    public Integer checked_group, kol_box_info, max_box;
    public Integer perem_int_summa, perem_int_ostatok, perem_int_cena, perem_int_kol, perem_kol_group_one, perem_int_kolbox;
    public Double TY, TY_Kons, dg_tw_summa_sk_DOUBLE;
    public Double Doub_Cena, Doub_Kons, Doub_Nal;
    public String name, kod, select_image, kol_group_one, kol_group_much;
    public String Cena_All, Cena_Nal, Cena_Kons, cena;
    public String lst_tw_name, lst_tw_kod, lst_tw_cena, lst_tw_ostatok, lst_tw_kolbox;
    public String PEREM_Agent, PEREM_KAgent, PEREM_KAgent_UID, PEREM_Vrema, PEREM_Data, PEREM_RNKod, PEREM_Adress;
    public Boolean pref_params_1, pref_params_2;

    public Context context_Activity;
    public SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_favorites);

        context_Activity = WJ_Favorites.this;
        lvw_favorites = (ListView) findViewById(R.id.favorites);
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        pref_params_1 = sp.getBoolean("switch_preference_1", false); //только товар, в наличии
        pref_params_2 = sp.getBoolean("switch_preference_2", false); //бесконечные остатки
        PEREM_Agent = sp.getString("PEREM_Agent", "0");
        PEREM_KAgent = sp.getString("PEREM_KAgent", "0");
        PEREM_KAgent_UID = sp.getString("PEREM_KAgent_UID", "0");
        PEREM_Vrema = sp.getString("PEREM_Vrema", "0");
        PEREM_Data = sp.getString("PEREM_Data", "0");
        PEREM_RNKod = sp.getString("PEREM_RNKod", "0");
        PEREM_Adress = sp.getString("PEREM_Adress", "0");
        Params();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.office_favorite_click);
        getSupportActionBar().setTitle("Номеклатура");
        getSupportActionBar().setSubtitle("Избранное");

        Adapter_Price();
        favorite.clear();
        ListAdapter();
        adapter_favorite = new ListAdapterAde_WJ_Favorite(context_Activity, favorite);
        adapter_favorite.notifyDataSetChanged();
        lvw_favorites.setAdapter(adapter_favorite);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza_L2.class);
        startActivity(intent);
        finish();
        // super.onBackPressed();
    }


    protected void ListAdapter() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base12_favorites.brends, base12_favorites.p_group, base12_favorites.kod, " +
                "base12_favorites.name, base12_favorites.kolbox, SUM(base4_ost.count) AS 'count', base5_price.cena, \n" +
                "base12_favorites.image, base12_favorites.strih, base12_favorites.kod_univ, base12_favorites.koduid\n" +
                "FROM base12_favorites\n" +
                "LEFT JOIN base5_price ON base12_favorites.koduid = base5_price.koduid\n" +
                "LEFT JOIN base4_ost ON base12_favorites.koduid = base4_ost.koduid\n" +
                "WHERE base4_ost.count>0\n" +
                "GROUP BY base12_favorites.name\n" +
                "ORDER BY base12_favorites.brends, base12_favorites.p_group;";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String image = cursor.getString(cursor.getColumnIndex("image"));
            cena = cursor.getString(cursor.getColumnIndex("cena"));
            String count = cursor.getString(cursor.getColumnIndex("count"));
            String kol_box = cursor.getString(cursor.getColumnIndex("kolbox"));
            String kod_univ = cursor.getString(cursor.getColumnIndex("kod_univ"));
            String strih = cursor.getString(cursor.getColumnIndex("strih"));
            Cena_Skidka(cena);
            String ostatok;
            if (count != (null)) {
                ostatok = count + "шт";
            } else ostatok = "закончился";

            favorite.add(new ListAdapterSimple_WJ_Favorite(name, image, cena, Cena_Nal, ostatok, kol_box, kod_univ, strih));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    protected void Cena_Skidka(String cena_nall) {
        Doub_Cena = Double.parseDouble(cena_nall);
        Cena_All = new DecimalFormat("#00.00").format(Doub_Cena).replace(",", ".");

        if (TY > 0.0) {
            Doub_Nal = Double.parseDouble(cena_nall) - (Double.parseDouble(cena_nall) * TY);
            Cena_Nal = new DecimalFormat("#00.00").format(Doub_Nal).replace(",", ".");
        } else {
            Doub_Nal = Double.parseDouble(cena);
            Cena_Nal = new DecimalFormat("#00.00").format(Doub_Nal).replace(",", ".");
        }

        if (TY_Kons > 0.0) {
            Doub_Kons = Double.parseDouble(cena) - (Double.parseDouble(cena_nall) * TY_Kons);
            Cena_Kons = new DecimalFormat("#00.00").format(Doub_Kons).replace(",", ".");
        } else {
            Doub_Kons = Double.parseDouble(cena_nall);
            Cena_Kons = new DecimalFormat("#00.00").format(Doub_Kons).replace(",", ".");
        }
    }  // Вычисление цен(своя, скидка за нал и конс)

    protected void Params() {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        String listValue_all = sp.getString("list_all_nal", "0");
        String listValue2_all = sp.getString("list_all_kons", "0");

        if (listValue_all.equals("0")) {
            TY = Double.parseDouble(listValue_all);
        } else {
            TY = Double.parseDouble(listValue_all) / 100;
        }
        if (listValue2_all.equals("0")) {
            TY_Kons = Double.parseDouble(listValue2_all);
        } else {
            TY_Kons = Double.parseDouble(listValue2_all) / 100;
        }

    }  // Загрузка тогровых условий

    protected void Adapter_Price() {
        adapter_favorite = new ListAdapterAde_WJ_Favorite(context_Activity, favorite);
        adapter_favorite.notifyDataSetChanged();
        lvw_favorites.setAdapter(adapter_favorite);

        lvw_favorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Заполнение из карточик товара
                try {
                    lst_tw_name = ((TextView) view.findViewById(R.id.textView_forma_name)).getText().toString();
                    lst_tw_kod = ((TextView) view.findViewById(R.id.textView_forma_kod)).getText().toString();
                    if (pref_params_2 == false) {
                        lst_tw_kolbox = ((TextView) view.findViewById(R.id.textView_forma_kolbox)).getText().toString();
                        lst_tw_cena = ((TextView) view.findViewById(R.id.textView_forma_cena)).getText().toString();
                        lst_tw_ostatok = ((TextView) view.findViewById(R.id.textView_forma_ostatok)).getText().toString();
                    } else {
                        lst_tw_kolbox = ((TextView) view.findViewById(R.id.textView_forma_kolbox)).getText().toString();
                        lst_tw_cena = ((TextView) view.findViewById(R.id.textView_forma_cena)).getText().toString();
                        lst_tw_ostatok = "999999";
                    }

                } catch (Exception e) {
                    Log.e("WJ_FormaLS2:", "Ошибка заполнения карточки!");
                    Toast.makeText(context_Activity, "Ошибка заполнения карточки!", Toast.LENGTH_SHORT).show();
                }

                // Определение данных в диалог
                localView = LayoutInflater.from(context_Activity).inflate(R.layout.mt_dialog_localview_add, null);
                dg_tw_name = (TextView) localView.findViewById(R.id.tvw_text_aks_name);
                dg_tw_koduniv = (TextView) localView.findViewById(R.id.tvw_text_aks_koduid);
                dg_tw_cena = (TextView) localView.findViewById(R.id.tvw_text_aks_cena);
                dg_tw_ostatok = (TextView) localView.findViewById(R.id.tvw_text_aks_ostatok);
                dg_tw_kol = (TextView) localView.findViewById(R.id.tvw_text_aks_kol);
                dg_tw_kolbox = (TextView) localView.findViewById(R.id.tvw_kol_box);
                dg_tw_summa = (TextView) localView.findViewById(R.id.tvw_text_aks_summa);
                dg_tw_summa_sk = (TextView) localView.findViewById(R.id.tvw_Ssumma);
                dg_tw_kolbox_org = (TextView) localView.findViewById(R.id.tvw_kol_box_org);
                dg_ed_skidka = (EditText) localView.findViewById(R.id.ed_text_skidka);
                dg_ed_editkol = (EditText) localView.findViewById(R.id.ed_text_kol);
                btn_up = (Button) localView.findViewById(R.id.btn_add_up);
                btn_down = (Button) localView.findViewById(R.id.btn_add_down);
                radioGroup_local = (RadioGroup) localView.findViewById(R.id.radioGroup_local);
                radioGroup_one = (RadioButton) localView.findViewById(R.id.radioButton_one);
                radioGroup_much = (RadioButton) localView.findViewById(R.id.radioButton_much);
                radioGroup_edit = (RadioButton) localView.findViewById(R.id.radioButton_edit);

                // Заполнение данных в диалог
                dg_ed_editkol.requestFocus();
                dg_ed_editkol.setFocusable(true);
                dg_ed_editkol.setVisibility(View.GONE);
                dg_tw_kol.setVisibility(View.VISIBLE);
                dg_tw_kolbox.setVisibility(View.GONE);
                dg_tw_summa_sk.setVisibility(View.GONE);
                checked_group = null;
                checked_group = R.id.radioButton_one;
                kol_group_one = "1";
                perem_kol_group_one = 1;

                dg_tw_name.setText(lst_tw_name);
                dg_tw_koduniv.setText(lst_tw_kod);
                perem_int_cena = Integer.parseInt(lst_tw_cena);
                dg_tw_cena.setText(perem_int_cena.toString());
                perem_int_ostatok = Integer.parseInt(lst_tw_ostatok.substring(0, lst_tw_ostatok.length() - 2));
                dg_tw_ostatok.setText(perem_int_ostatok.toString());
                perem_int_kol = perem_kol_group_one;
                dg_tw_kol.setText(perem_int_kol.toString());
                if (!lst_tw_kolbox.isEmpty() | lst_tw_kolbox.equals(" ")) {
                    perem_int_kolbox = Integer.parseInt(lst_tw_kolbox);
                } else perem_int_kolbox = 12;

                dg_tw_kolbox_org.setText(perem_int_kolbox.toString());
                perem_int_summa = perem_int_cena * perem_kol_group_one;
                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                dg_tw_summa.setText(Format);

                if (perem_int_kol > 0) {
                    Button_Up(perem_int_cena, perem_int_ostatok, perem_int_kolbox);
                    Button_Down(perem_int_cena, perem_int_ostatok, perem_int_kolbox);
                    Button_Edit(perem_int_cena, perem_int_ostatok);
                    Fun_Messeger_Panel(perem_int_cena, perem_int_ostatok, perem_int_kolbox);
                    Log.e("WJ_FormaLSINT:", perem_int_kol + ", " + perem_int_cena + ", " + perem_int_ostatok + ", " + Format);
                } else
                    Toast.makeText(context_Activity, "не достаточно товара", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void Fun_Messeger_Panel(final Integer cena, final Integer ostatok, final Integer box) {
        Button_Up(perem_int_cena, perem_int_ostatok, perem_int_kolbox);
        Button_Down(perem_int_cena, perem_int_ostatok, perem_int_kolbox);
        Button_Edit(perem_int_cena, perem_int_ostatok);
        radioGroup_local.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton_one: {
                        dg_tw_kol.setVisibility(View.VISIBLE);
                        dg_ed_editkol.setVisibility(View.GONE);
                        dg_tw_kolbox.setVisibility(View.GONE);
                        btn_up.setVisibility(View.VISIBLE);
                        btn_down.setVisibility(View.VISIBLE);
                        checked_group = null;
                        checked_group = R.id.radioButton_one;
                        perem_kol_group_one = 1;
                        dg_tw_kol.setText(perem_kol_group_one.toString());
                        perem_int_summa = perem_kol_group_one * cena;
                        String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");;
                        dg_tw_summa.setText(Format);
                        dg_tw_summa_sk.setVisibility(View.GONE);
                        dg_ed_skidka.setText("");
                        Summa_Skidka();
                        hideInputMethod();
                    }
                    break;
                    case R.id.radioButton_much: {
                        dg_tw_kol.setVisibility(View.VISIBLE);
                        dg_ed_editkol.setVisibility(View.GONE);
                        dg_tw_kolbox.setVisibility(View.VISIBLE);
                        btn_up.setVisibility(View.VISIBLE);
                        btn_down.setVisibility(View.VISIBLE);
                        kol_box_info = 1;
                        dg_tw_kolbox.setText("x" + kol_box_info);
                        checked_group = null;
                        checked_group = R.id.radioButton_much;
                        // kol_group_much = box.replaceAll("[\\D]", "");
                        //  Double db = ostatok / Double.parseDouble(kol_group_much);
                        kol_group_much = box.toString();
                        Double db = ostatok.doubleValue() / box.doubleValue();
                        max_box = db.intValue();
                        if (max_box > 0) {
                            dg_tw_kol.setText(kol_group_much);
                            Toast.makeText(context_Activity, "кол-во кор=" + max_box, Toast.LENGTH_SHORT).show();
                        } else {
                            dg_tw_kol.setText("0");
                        }
                        perem_int_summa = Integer.parseInt(dg_tw_kol.getText().toString()) * cena;
                        String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");;
                        dg_tw_summa.setText(Format);
                        dg_tw_summa_sk.setVisibility(View.GONE);
                        dg_ed_skidka.setText("");
                        Summa_Skidka();
                        hideInputMethod();
                    }
                    break;
                    case R.id.radioButton_edit: {
                        dg_tw_kol.setVisibility(View.GONE);
                        dg_ed_editkol.setVisibility(View.VISIBLE);
                        dg_tw_kolbox.setVisibility(View.GONE);
                        btn_up.setVisibility(View.GONE);
                        btn_down.setVisibility(View.GONE);
                        dg_ed_editkol.clearFocus();
                        dg_ed_editkol.requestFocus();
                        dg_ed_editkol.setFocusable(true);
                        dg_ed_editkol.setText("");
                        dg_ed_editkol.setSelection(dg_ed_editkol.getText().length());
                        dg_tw_kol.setText(kol_group_one);
                        perem_int_summa = Integer.parseInt(dg_tw_kol.getText().toString()) * cena;
                        String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");;
                        dg_tw_summa.setText(Format);
                        dg_tw_summa_sk.setVisibility(View.GONE);
                        dg_ed_skidka.setText("");
                        Summa_Skidka();
                        showInputMethod();
                    }
                    break;
                    default:
                        break;

                }
            }
        });

        AlertDialog.Builder localBuilder = new AlertDialog.Builder(context_Activity);

        localBuilder.setView(localView);
        localBuilder.setTitle("Добавление товара");
        localBuilder.setCancelable(false).setIcon(R.drawable.icon_korz).setPositiveButton(" ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                try {
                    Integer kol_text = Integer.parseInt(dg_tw_kol.getText().toString());
                    if (kol_text > 0) {
                        Search_Image(dg_tw_koduniv.getText().toString());
                        Write_Table_RN(dg_tw_koduniv.getText().toString(), dg_tw_name.getText().toString(),
                                dg_tw_kol.getText().toString(), dg_tw_cena.getText().toString(),
                                dg_tw_summa.getText().toString(), dg_ed_skidka.getText().toString(), dg_tw_summa_sk_DOUBLE, select_image);
                        //  protected void Write_Table_RN(final String koduniv, final String name, final String kol, final String cena, final String summa) {
                        hideInputMethod();
                        //   Update_Ostatki();**

                    } else {
                        Toast.makeText(context_Activity, "Товара не достаточно на складе", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context_Activity, "Неправильный ввод данных", Toast.LENGTH_SHORT).show();
                }

            }
        }).setNegativeButton(" ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                hideInputMethod();
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

    protected void Button_Up(final Integer cena, final Integer ostatok, final Integer textbox) {
        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dg_tw_kol.getText().toString().equals("") | dg_tw_kol.getText().toString().equals(" ")) {
                    switch (checked_group) {
                        case R.id.radioButton_one: {
                            dg_tw_kol.setText("1");
                            Integer i = Integer.parseInt(dg_tw_kol.getText().toString());
                            i++;
                            Toast.makeText(context_Activity, "Товара на складе" + ostatok, Toast.LENGTH_SHORT).show();
                            if (i <= ostatok) {
                                dg_tw_kol.setText(i.toString());
                                perem_int_summa = cena * i;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");;
                                dg_tw_summa.setText(Format);
                                Summa_Skidka();
                            } else
                                Toast.makeText(context_Activity, "не достаточно товара", Toast.LENGTH_SHORT).show();

                        }
                        break;

                        case R.id.radioButton_much: {
                            /// dg_tw_kol.setText(text_box.replaceAll("[\\D]", ""));
                            dg_tw_kol.setText(textbox.toString());
                            Integer i = Integer.parseInt(dg_tw_kol.getText().toString());
                            //  Integer new_kol = Integer.parseInt(text_box.replaceAll("[\\D]", "")) + i;
                            Integer new_kol = textbox + i;
                            dg_tw_kol.setText(new_kol.toString());
                            perem_int_summa = cena * new_kol;
                            String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");;
                            dg_tw_summa.setText(Format);
                            kol_box_info++;
                            dg_tw_kolbox.setText("x" + kol_box_info);
                            Summa_Skidka();
                        }
                        break;
                    }
                } else {
                    switch (checked_group) {
                        case R.id.radioButton_one: {
                            Integer i = Integer.parseInt(dg_tw_kol.getText().toString());
                            i++;
                            if (i <= ostatok) {
                                dg_tw_kol.setText(i.toString());
                                perem_int_summa = cena * i;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");;
                                dg_tw_summa.setText(Format);
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
                                Integer i = Integer.parseInt(dg_tw_kol.getText().toString());
                                //  Integer new_kol = Integer.parseInt(text_box.replaceAll("[\\D]", "")) + i;
                                Integer new_kol = textbox + i;
                                dg_tw_kol.setText(new_kol.toString());
                                perem_int_summa = cena * new_kol;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");;
                                dg_tw_summa.setText(Format);
                                kol_box_info++;
                                dg_tw_kolbox.setText("x" + kol_box_info);
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
        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dg_tw_kol.getText().toString().equals("") | dg_tw_kol.getText().toString().equals(" ")) {
                    switch (checked_group) {
                        case R.id.radioButton_one: {
                            dg_tw_kol.setText("1");
                            Integer i = Integer.parseInt(dg_tw_kol.getText().toString());
                            if (i > 1) {
                                i--;
                                dg_tw_kol.setText(i.toString());
                                perem_int_summa = cena * i;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");;
                                dg_tw_summa.setText(Format);
                                Summa_Skidka();
                            } else
                                Toast.makeText(context_Activity, "Кол-во меньше 1", Toast.LENGTH_SHORT).show();
                        }
                        break;

                        case R.id.radioButton_much: {
                            //  dg_tw_kol.setText(text_box.replaceAll("[\\D]", ""));
                            dg_tw_kol.setText(textbox.toString());
                            Integer i = Integer.parseInt(dg_tw_kol.getText().toString());
                            //   if (i > Integer.parseInt(text_box.replaceAll("[\\D]", ""))) {
                            if (i > textbox) {
                                // Integer new_kol = i - Integer.parseInt(text_box.replaceAll("[\\D]", ""));
                                Integer new_kol = i - textbox;
                                dg_tw_kol.setText(new_kol.toString());
                                perem_int_summa = cena * new_kol;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");;
                                dg_tw_summa.setText(Format);
                                kol_box_info--;
                                dg_tw_kolbox.setText("x" + kol_box_info);
                                Summa_Skidka();
                            } else
                                Toast.makeText(context_Activity, "Кол-во меньше кор.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                } else {
                    switch (checked_group) {
                        case R.id.radioButton_one: {
                            Integer i = Integer.parseInt(dg_tw_kol.getText().toString());
                            if (i > 1) {
                                i--;
                                dg_tw_kol.setText(i.toString());
                                perem_int_summa = cena * i;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");;
                                dg_tw_summa.setText(Format);
                                Summa_Skidka();
                            } else
                                Toast.makeText(context_Activity, "Кол-во меньше 1", Toast.LENGTH_SHORT).show();
                        }
                        break;

                        case R.id.radioButton_much: {
                            Integer i = Integer.parseInt(dg_tw_kol.getText().toString());
                            //  if (i > Integer.parseInt(text_box.replaceAll("[\\D]", ""))) {
                            if (i > textbox) {
                                //    Integer new_kol = i - Integer.parseInt(text_box.replaceAll("[\\D]", ""));
                                Integer new_kol = i - textbox;
                                dg_tw_kol.setText(new_kol.toString());
                                perem_int_summa = cena * new_kol;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");;
                                dg_tw_summa.setText(Format);
                                kol_box_info--;
                                dg_tw_kolbox.setText("x" + kol_box_info);
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

        dg_ed_editkol.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //  editText_kol.setText("1");
                if (dg_ed_editkol.getText().toString().equals("") | dg_ed_editkol.getText().toString().equals(" ") | dg_ed_editkol.getText().toString().equals("0")) {
                    dg_tw_summa.setText("00.00");
                    dg_tw_summa_sk.setText("00.00");
                    // Summa_Skidka();
                } else if (Integer.parseInt(dg_ed_editkol.getText().toString()) <= ostatok) {
                    //  cena_t = Double.parseDouble(text_cena.substring(0, text_cena.length() - 3));
                    //  perem_cena = Double.parseDouble(text_cena);
                    dg_tw_kol.setText(dg_ed_editkol.getText().toString());
                    perem_int_summa = Integer.parseInt(dg_ed_editkol.getText().toString()) * cena;
                    String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");;
                    dg_tw_summa.setText(Format);
                    Summa_Skidka();
                } else {
                    Toast.makeText(context_Activity, "кол-во превышает остаток", Toast.LENGTH_SHORT).show();
                    dg_ed_editkol.setText(ostatok.toString());
                    dg_ed_editkol.setSelection(dg_ed_editkol.getText().length());
                    dg_tw_kol.setText(dg_ed_editkol.getText().toString());
                    perem_int_summa = Integer.parseInt(dg_ed_editkol.getText().toString()) * cena;
                    String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");;
                    dg_tw_summa.setText(Format);
                    Summa_Skidka();
                }
            }
        });
    }  // производбный ввод количества

    protected void Summa_Skidka() {
        if (dg_ed_skidka.getText().toString().equals("") | dg_ed_skidka.getText().toString().equals(" ") | dg_ed_skidka.getText().toString().equals("0")) {
            dg_tw_summa_sk.setVisibility(View.GONE);
            dg_tw_summa_sk.setText("00.00");
            dg_tw_summa_sk_DOUBLE = 00.00;
        } else {
            dg_tw_summa_sk.setVisibility(View.VISIBLE);
            Double per_sum = (perem_int_summa).doubleValue();
            Double per_sk = Double.parseDouble(dg_ed_skidka.getText().toString());

            Double per_itg = Double.valueOf(per_sum - (per_sum * (per_sk / 100)));
            dg_tw_summa_sk_DOUBLE = per_itg;
            String Format = new DecimalFormat("#00.00").format(per_itg).replace(",", ".");;
            dg_tw_summa_sk.setText(Format);
            Log.e("WJ_FormaSK:", Format + ", " + per_sum + ", " + per_sk + ", " + per_itg);
        }
    }

    protected void Search_Image(String uid) {
        Log.e("Err", uid);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
        String query = "SELECT base10_nomeclature.name, base10_nomeclature.image, base10_nomeclature.kod_univ\n" +
                "FROM base10_nomeclature \n" +
                "WHERE base10_nomeclature.kod_univ LIKE '%" + uid + "%'\n";

        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex("name"));
        String image = cursor.getString(cursor.getColumnIndex("image"));
        String koduid = cursor.getString(cursor.getColumnIndex("kod_univ"));
        Log.e("Err", name + koduid + image);
        select_image = image;
        cursor.close();
        db.close();
    }

    protected void Write_Table_RN(final String koduniv, final String name, final String kol, final String cena, final String summa, final String skidka, final Double summaSK, final String image) {

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_rn_db.db3", MODE_PRIVATE, null);
        // Заполнения карточки товара
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("Agent", PEREM_Agent);
        localContentValues.put("Kod_RN", PEREM_RNKod);
        localContentValues.put("Klients", PEREM_KAgent);
        localContentValues.put("UID_Klients", PEREM_KAgent_UID);
        localContentValues.put("Vrema", PEREM_Vrema);
        localContentValues.put("Data", PEREM_Data);
        localContentValues.put("Adress", PEREM_Adress);
        localContentValues.put("Kod_Univ", koduniv);
        localContentValues.put("Name", name);
        localContentValues.put("Kol", kol);
        localContentValues.put("Cena", cena);
        localContentValues.put("Summa", summa);
        localContentValues.put("Image", image);

        if (!skidka.isEmpty()) {
            String Format_summa = new DecimalFormat("#00.00").format(summaSK);
            localContentValues.put("Skidka", skidka);

            Double db1 = Double.parseDouble(skidka), db2 = Double.parseDouble(cena), db3;
            db3 = db2 - (db2 * (db1 / 100));
            localContentValues.put("Cena_SK", db3.toString());
            Log.e("WJZakS2=", db1 + " " + db2 + " " + db3);
            localContentValues.put("Itogo", Format_summa.replaceAll(",", "."));
        } else {
            localContentValues.put("Skidka", "0");
            localContentValues.put("Cena_SK", "0");
            localContentValues.put("Itogo", summa);
        }

        Log.e("WJZakS2=", localContentValues.toString());


        // Проверка сходства позиций
        String query_Search = "SELECT base_RN.Kod_RN, base_RN.Kod_Univ " +
                "FROM base_RN WHERE base_RN.Kod_RN LIKE '%" + PEREM_RNKod + "%'";
        Cursor cursor = db.rawQuery(query_Search, null);
        cursor.moveToFirst();
        Integer k = 0;
        while (cursor.isAfterLast() == false) {
            String kod_univ = cursor.getString(cursor.getColumnIndex("Kod_Univ"));
            String kod_rn = cursor.getString(cursor.getColumnIndex("Kod_RN"));
            if (koduniv.equals(kod_univ)) {
                k++;
                cursor.moveToNext();
            } else cursor.moveToNext();
        }

        if (k == 0) {
            db.insert("base_RN", null, localContentValues);
            Toast.makeText(context_Activity, "Товар добавлен!", Toast.LENGTH_SHORT).show();
            cursor.moveToNext();
        } else Toast.makeText(context_Activity, "Товар уже есть!", Toast.LENGTH_SHORT).show();

        localContentValues.clear();
        cursor.close();
        db.close();
    }

    /**
     * прячем программную клавиатуру
     */
    protected void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(dg_ed_editkol.getWindowToken(), 0);
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

    private Runnable mShowInputMethodTask = new Runnable() {
        public void run() {
            // showInputMethodForQuery();
        }
    };
}
