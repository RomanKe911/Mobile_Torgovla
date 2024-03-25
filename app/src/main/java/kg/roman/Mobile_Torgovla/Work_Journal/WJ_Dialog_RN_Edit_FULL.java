package kg.roman.Mobile_Torgovla.Work_Journal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import kg.roman.Mobile_Torgovla.FormaZakazaStart.WJ_Forma_Zakaza;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_RN_END;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_RN_END;
import kg.roman.Mobile_Torgovla.R;

public class WJ_Dialog_RN_Edit_FULL extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public ArrayList<ListAdapterSimple_RN_END> rn_end = new ArrayList<ListAdapterSimple_RN_END>();
    public ListAdapterAde_RN_END adapterPriceClients;

    public Toolbar toolbar;
    public NavigationView navigationView;
    public String db_kod_rn, db_klients, db_summa, db_itogo, db_adress, Log_Text_Error;
    public ListView listView;
    public SharedPreferences sp;
    public Context context_Activity;
    public String text;
    public ProgressDialog pDialog;
    public String postclass_RN, postclass_OLD_SUMMA, postclass_old_kol, postclass_clients_uid, postclass_Summa, postclass_Summa_Debet, postclass_Skidka;
    public String text_sql_kolbox, text_sql_count, text_sql_uid_sklad;
    public String PEREM_FTP_SERV, PEREM_FTP_LOGIN, PEREM_FTP_PASS, PEREM_FTP_DISTR_XML, PEREM_FTP_DISTR_db3,
            PEREM_IMAGE_PUT_SDCARD, PEREM_IMAGE_PUT_PHONE, PEREM_IMAGE_KOD;
    public String PEREM_MAIL_LOGIN, PEREM_MAIL_PASS, PEREM_MAIL_START, PEREM_MAIL_END,
            PEREM_DB3_CONST, PEREM_DB3_BASE, PEREM_DB3_RN, PEREM_ANDROID_ID_ADMIN, PEREM_ANDROID_ID;
    public String PEREM_AG_UID, PEREM_AG_NAME, PEREM_AG_REGION, PEREM_AG_UID_REGION, PEREM_AG_CENA,
            PEREM_AG_SKLAD, PEREM_AG_UID_SKLAD, PEREM_AG_TYPE_REAL, PEREM_AG_TYPE_USER,
            PEREM_WORK_DISTR, PEREM_KOD_MOBILE, PEREM_KOD_UID_KODRN;
    public String PEREM_KLIENT_UID, PEREM_DIALOG_UID, PEREM_DIALOG_DATA_START, PEREM_DIALOG_DATA_END, PEREM_DISPLAY_START, PEREM_DISPLAY_END;
    Calendar Calendar_Name = Calendar.getInstance();
    public View localView;
    public SharedPreferences.Editor ed;

    public TextView dg_tw_name, dg_tw_koduniv, dg_tw_cena, dg_tw_ostatok, dg_tw_kol, dg_tw_summa, dg_tw_summa_sk, dg_tw_kolbox, dg_tw_kolbox_org, dg_tw_koduid;
    public RadioGroup radioGroup_local;
    public RadioButton radioGroup_one, radioGroup_much, radioGroup_edit;
    public Button btn_down, btn_up;
    public FloatingActionButton floatingActionButton;
    public EditText dg_ed_skidka, dg_ed_editkol;
    public Button button_ok, button_up, button_cancel;
    public Integer checked_group, kol_box_info, max_box;
    public Integer perem_int_summa, perem_int_ostatok, perem_int_cena, perem_int_kol, perem_kol_group_one, perem_int_kolbox;
    public String lst_tw_name, lst_tw_kod, lst_tw_cena, lst_tw_ostatok, lst_tw_kolbox, lst_tw_kol, lst_tw_skidka;
    public String kol_group_one, kol_group_much, SKIDKA_TY, Format_cena_sk;
    public Double Aut_Summa, Auto_Summa, Itog_Summa, dg_tw_summa_sk_DOUBLE;
    public String PEREM_K_AG_NAME, PEREM_K_AG_UID, PEREM_K_AG_ADRESS, PEREM_K_AG_KodRN, PEREM_K_AG_Data, PEREM_K_AG_Vrema, PEREM_K_AG_GPS, PEREM_K_AG_Data_WORK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mt_wj_activity_rewrite_tovar);
        // setContentView(R.layout.mt_wj_dialog_rn_edit);
        context_Activity = WJ_Dialog_RN_Edit_FULL.this;
        Constanta_Read();
        Log_Text_Error = "ERR_WJ_F_Z_L2: ";
        PEREM_IMAGE_KOD = "";
        Bundle arguments = getIntent().getExtras();
        postclass_RN = arguments.get("ReWrite_RN").toString();
        postclass_Summa = arguments.get("ReWrite_Summa").toString();
        postclass_Summa_Debet = arguments.get("ReWrite_Summa_Debet").toString();
        postclass_Skidka = arguments.get("ReWrite_Skidka").toString();
        postclass_clients_uid = arguments.get("ReWrite_Client_UID").toString();
        postclass_OLD_SUMMA = postclass_Summa;
        SQL_UID_SKLAD();

       /* Calendar_Name = Calendar.getInstance();
        Calendar_Name.set(Integer.parseInt(postclass_Data.substring(0, 4)),
                Integer.valueOf(postclass_Data.substring(5, 7)) - 1,
                Integer.valueOf(postclass_Data.substring(postclass_Data.length() - 2, postclass_Data.length())));
        SimpleDateFormat dateFormat_view_lauout = new SimpleDateFormat("dd-MM-yyyy");  // формат для экрана даты
        postclass_Data_ThisData = dateFormat_view_lauout.format(Calendar_Name.getTime());*/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_icons_1);
        getSupportActionBar().setTitle("номер: " + postclass_RN);
        getSupportActionBar().setSubtitle("Итого: " + postclass_Summa);

        listView = findViewById(R.id.ListView_Dialog_Edit);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_nomenclature);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view_nomenclature);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.menu_nomeclature_brends);
        navigationView.setNavigationItemSelectedListener(this);
        Navigation_Menu();
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View convertView, int i, long l) {
                TextView name = convertView.findViewById(R.id.tvw_list_name);
                TextView kol = convertView.findViewById(R.id.tvw_list_kol);
                TextView cena = convertView.findViewById(R.id.tvw_list_cena);
                TextView cenaSK = convertView.findViewById(R.id.tvw_list_cenaSK);
                TextView summa = convertView.findViewById(R.id.tvw_list_summa);
                TextView skidka = convertView.findViewById(R.id.tvw_list_skidka);
                TextView itogo = convertView.findViewById(R.id.tvw_list_itogo);
                TextView koduid = convertView.findViewById(R.id.tvw_list_kod_uid);
                ImageView image = convertView.findViewById(R.id.img_list);
                SQL_KOLBOX(koduid.getText().toString());
                SQL_OSTATOK(koduid.getText().toString());
                postclass_old_kol = kol.getText().toString();
                Toast.makeText(context_Activity, "" + name.getText() + "\n+_+" + kol.getText() + summa.getText(), Toast.LENGTH_SHORT).show();
                Loading_Rename_list(name.getText().toString(), koduid.getText().toString(), text_sql_kolbox, cena.getText().toString(), text_sql_count, kol.getText().toString());
            }
        });

    }

    protected void Navigation_Menu() {
        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
            String query = "SELECT * FROM base_in_brends_id";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            Menu m = navigationView.getMenu();
            m.clear();
            SubMenu topChannelMenu = m.addSubMenu("Основной товар");
            SubMenu topChannelMenu2 = m.addSubMenu("Продуктовая корзина");
            while (cursor.isAfterLast() == false) {
                String name = cursor.getString(cursor.getColumnIndex("brends"));        // столбец с брендом
                String group = cursor.getString(cursor.getColumnIndex("group_type"));   // столбец с префиксом группы

                if (group.equals("gr_1")) {
                    topChannelMenu.add(name.substring(0, 1) + name.substring(1).toLowerCase()).setIcon(R.drawable.ic_cost);
                } else
                    topChannelMenu2.add(name.substring(0, 1) + name.substring(1).toLowerCase()).setIcon(R.drawable.ic_cost);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка загрузки таблицы брендов!", Toast.LENGTH_SHORT).show();
            Log.e(Log_Text_Error, "Ошибка загрузки таблицы брендов!");
        }

    }
    @Override
    public void onResume() {
        Log.e("Resume", "Yes!");
        rn_end.clear();
        ListAdapter();
        adapterPriceClients = new ListAdapterAde_RN_END(context_Activity, rn_end);
        adapterPriceClients.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients);
        super.onResume();
    }

    protected void Loading_Rename_list(final String name, final String kod_univ, final String kolbox, final String cena, final String ostatok, final String kol_vo) {
        lst_tw_name = name;
        lst_tw_kod = kod_univ;

        lst_tw_kolbox = kolbox;
        lst_tw_cena = cena;
        lst_tw_ostatok = ostatok;

        ed = sp.edit();
        ed.putString("PEREM_CLICK_TY", "false");
        ed.commit();

        Log.e("WJ_FormaLS2:", lst_tw_name + ", " + lst_tw_kod + ", " + lst_tw_kolbox + ", " + lst_tw_cena + ", " + lst_tw_ostatok + ", " + kol_vo);
        // Определение данных в диалог
        localView = LayoutInflater.from(context_Activity).inflate(R.layout.mt_dialog_localview_add, null);
        dg_tw_name = (TextView) localView.findViewById(R.id.tvw_text_aks_name);
        dg_tw_koduniv = (TextView) localView.findViewById(R.id.tvw_text_aks_koduid);
        dg_tw_koduid = (TextView) localView.findViewById(R.id.text_d_koduid);
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
        perem_kol_group_one = Integer.parseInt(kol_vo);

        dg_tw_name.setText(lst_tw_name);
        dg_tw_koduniv.setText(lst_tw_kod);
        perem_int_cena = Integer.parseInt(lst_tw_cena);
        dg_tw_cena.setText(perem_int_cena.toString());
        perem_int_ostatok = Integer.parseInt(lst_tw_ostatok);
        dg_tw_ostatok.setText(perem_int_ostatok.toString());
        perem_int_kol = perem_kol_group_one;
        dg_tw_kol.setText(perem_int_kol.toString());
        if (!lst_tw_kolbox.isEmpty() | lst_tw_kolbox.equals(" ") | lst_tw_kolbox.equals(null)) {
            perem_int_kolbox = Integer.parseInt(lst_tw_kolbox);
        } else perem_int_kolbox = 12;

        dg_tw_kolbox_org.setText(perem_int_kolbox.toString());
        perem_int_summa = perem_int_cena * perem_kol_group_one;
        String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
        dg_tw_summa.setText(Format);

        if (perem_int_kol > 0) {
            Button_Up(perem_int_cena, perem_int_ostatok, perem_int_kolbox);
            Button_Down(perem_int_cena, perem_int_ostatok, perem_int_kolbox);
            Button_Edit(perem_int_cena, perem_int_ostatok, perem_int_kol);
            Button_Edit_Skidka();
            Fun_Messeger_Panel(perem_int_cena, perem_int_ostatok, perem_int_kolbox, perem_kol_group_one);

            Log.e("WJ_FormaLSINT:", Integer.parseInt(cena) + ", " + Integer.parseInt(ostatok) + ", " + Integer.parseInt(kolbox) + ", " + Format);
        } else
            Toast.makeText(context_Activity, "не достаточно товара", Toast.LENGTH_SHORT).show();
    }


    protected void Fun_Messeger_Panel(final Integer cena, final Integer ostatok, final Integer box, final Integer kolvo) {
        Button_Up(perem_int_cena, perem_int_ostatok, perem_int_kolbox);
        Button_Down(perem_int_cena, perem_int_ostatok, perem_int_kolbox);
        Button_Edit(perem_int_cena, perem_int_ostatok, perem_int_kol);
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
                        perem_kol_group_one = kolvo;
                        dg_tw_kol.setText(perem_kol_group_one.toString());
                        perem_int_summa = perem_kol_group_one * cena;
                        String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                        dg_tw_summa.setText(Format);
                        dg_tw_summa_sk.setVisibility(View.GONE);
                        dg_ed_skidka.setText("");
                        Summa_Skidka();
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
                        String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                        ;
                        dg_tw_summa.setText(Format);
                        dg_tw_summa_sk.setVisibility(View.GONE);
                        dg_ed_skidka.setText("");
                        Summa_Skidka();
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
                        dg_ed_editkol.setText(kolvo.toString());
                        // dg_ed_editkol.setText("");
                        dg_ed_editkol.setSelection(dg_ed_editkol.getText().length());
                        dg_tw_kol.setText(kol_group_one);
                        perem_int_summa = Integer.parseInt(dg_tw_kol.getText().toString()) * cena;
                        String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                        ;
                        dg_tw_summa.setText(Format);
                        dg_tw_summa_sk.setVisibility(View.GONE);
                        dg_ed_skidka.setText("");
                        Summa_Skidka();
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
                Integer kol_text = Integer.parseInt(dg_tw_kol.getText().toString());
                if (kol_text > 0) {
                    Log.e("Остаток:", "_" + perem_int_ostatok);
                    Log.e("Остаток:", "_" + postclass_old_kol);
                    Log.e("Кол-во:", "_" + kol_text);
                    SQL_ReWrite_Ostatok(lst_tw_kod, Integer.parseInt(postclass_old_kol), kol_text, perem_int_ostatok.toString());
                     /*   Write_Table_RN(dg_tw_koduniv.getText().toString(), dg_tw_name.getText().toString(),
                                dg_tw_kol.getText().toString(), dg_tw_cena.getText().toString(),
                                dg_tw_summa.getText().toString(), dg_ed_skidka.getText().toString(), dg_tw_summa_sk_DOUBLE);*/
                    ReWrite_Table_RN(lst_tw_kod, dg_tw_kol.getText().toString(), dg_tw_cena.getText().toString(), postclass_Skidka, dg_tw_ostatok.getText().toString(), postclass_OLD_SUMMA);

                    rn_end.clear();
                    ListAdapter();
                    adapterPriceClients = new ListAdapterAde_RN_END(context_Activity, rn_end);
                    adapterPriceClients.notifyDataSetChanged();
                    listView.setAdapter(adapterPriceClients);
                } else {
                    Toast.makeText(context_Activity, "Товара не достаточно на складе", Toast.LENGTH_SHORT).show();
                }

                try {


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
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
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
                            String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                            ;
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
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                                ;
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
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                                ;
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
                Integer new_ostatok = ostatok;
                if (dg_tw_kol.getText().toString().equals("") | dg_tw_kol.getText().toString().equals(" ")) {
                    switch (checked_group) {
                        case R.id.radioButton_one: {
                            dg_tw_kol.setText("1");
                            Integer i = Integer.parseInt(dg_tw_kol.getText().toString());
                            if (i > 1) {
                                i--;
                                dg_tw_kol.setText(i.toString());
                                perem_int_summa = cena * i;
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                                ;
                                dg_tw_summa.setText(Format);
                                Summa_Skidka();
                                new_ostatok = new_ostatok + 1;
                                Log.e("Ostatok:", "_" + new_ostatok);
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
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                                ;
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
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                                ;
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
                                String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                                ;
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

    protected void Button_Edit(final Integer cena, final Integer ostatok, final Integer kolvo) {

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
                    String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                    ;
                    dg_tw_summa.setText(Format);
                    Summa_Skidka();
                } else {
                    Toast.makeText(context_Activity, "кол-во превышает остаток", Toast.LENGTH_SHORT).show();
                    dg_ed_editkol.setText(ostatok.toString());
                    dg_ed_editkol.setSelection(dg_ed_editkol.getText().length());
                    dg_tw_kol.setText(dg_ed_editkol.getText().toString());
                    perem_int_summa = Integer.parseInt(dg_ed_editkol.getText().toString()) * cena;
                    String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                    ;
                    dg_tw_summa.setText(Format);
                    Summa_Skidka();
                }


            }
        });
    }  // производбный ввод количества

    protected void Button_Edit_Skidka() {

        dg_ed_skidka.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Summa_Skidka();

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }  // скидка на товар

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
            String Format = new DecimalFormat("#00.00").format(per_itg).replace(",", ".");
            ;
            dg_tw_summa_sk.setText(Format);
            Log.e("WJ_FormaSK:", Format + ", " + per_sum + ", " + per_sk + ", " + per_itg);
        }
    }

    protected void Write_Table_RN(final String koduniv, final String name, final String kol, final String cena, final String summa, final String skidka, final Double summaSK) {

        // Calendare_This_Data();
        SQLiteDatabase db_up = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);

        String query = "SELECT base_RN.Kod_RN, base_RN.Kod_Univ " +
                "FROM base_RN WHERE base_RN.Kod_RN LIKE '%%'";

        Cursor cursor = db_up.rawQuery(query, null);
        ContentValues localContentValuesUP = new ContentValues();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String kod_univ = cursor.getString(cursor.getColumnIndex("Kod_Univ"));
            String kod_rn = cursor.getString(cursor.getColumnIndex("Kod_RN"));
            Log.e("Update_Ostatok=", kod_univ + ", " + koduniv);
            Log.e("Update_Ostatok=", PEREM_K_AG_KodRN + ", " + kod_rn);

            if (kod_univ.equals(koduniv) & PEREM_K_AG_KodRN.equals(kod_rn)) {
                Log.e("IFELSE=", "Yes" + PEREM_K_AG_KodRN + ", " + kod_rn + kod_univ + ", " + koduniv);
                localContentValuesUP.put("Kod_RN", PEREM_K_AG_KodRN);
                localContentValuesUP.put("Vrema", PEREM_K_AG_Vrema);
                localContentValuesUP.put("Data", PEREM_K_AG_Data);
                localContentValuesUP.put("Kod_Univ", koduniv);
                localContentValuesUP.put("Name", name);
                localContentValuesUP.put("Kol", kol);
                localContentValuesUP.put("Cena", cena);
                localContentValuesUP.put("Summa", summa);
                if (!skidka.isEmpty()) {
                    String Format_summa = new DecimalFormat("#00.00").format(summaSK);
                    localContentValuesUP.put("Skidka", skidka);

                    Double cena_db = Double.valueOf(cena);
                    Double cena_sk_db = Double.valueOf(skidka);
                    String Format = new DecimalFormat("#00.00").format(cena_db - (cena_db * (cena_sk_db / 100)));
                    localContentValuesUP.put("Cena_Sk", Format.replace(",", "."));

                    localContentValuesUP.put("Itogo", Format_summa.replaceAll(",", "."));
                } else {
                    localContentValuesUP.put("Skidka", "0");
                    localContentValuesUP.put("Cena_Sk", "0");
                    localContentValuesUP.put("Itogo", summa);
                }


                String[] arrayOfString = new String[1];
                String[] arrayOfString2 = new String[1];
                arrayOfString[0] = kod_rn;
                arrayOfString2[0] = kod_univ;

                db_up.update("base_RN", localContentValuesUP, "Kod_RN = ? AND Kod_Univ = ?", new String[]{kod_rn, kod_univ});
                //    db_up.update("base_RN", localContentValuesUP, "Kod_RN = ? AND Kod_Univ = ?", arrayOfString);

                cursor.moveToNext();
            } else cursor.moveToNext();

        }

        cursor.close();
        db_up.close();
    }

    protected void ReWrite_Table_RN(String rw_kodUID, String rw_newKOL, String
            rw_CENA, String rw_SKIDKA, String rw_Ostatok, String rw_old_summa) {
        Log.e("Новые данные: ", rw_kodUID);
        Log.e("Новые данные: ", rw_newKOL);
        Log.e("Новые данные: ", rw_CENA);
        Log.e("Новые данные: ", rw_SKIDKA);
        Log.e("Новые данные: ", rw_Ostatok);

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT * FROM base_RN WHERE Kod_RN = '" + postclass_RN + "' AND koduid = '" + rw_kodUID + "';";

        Cursor cursor = db.rawQuery(query, null);

        ContentValues localContentValuesUP = new ContentValues();
        ContentValues localContentValuesUP_All = new ContentValues();
        ContentValues localContentValuesUP_Debet = new ContentValues();
        cursor.moveToFirst();

        Double d_summa = 0.0, d_itogo = 0.0, d_kol = Double.parseDouble(rw_newKOL),
                d_cena = Double.parseDouble(rw_CENA), d_skidka = Double.parseDouble(rw_SKIDKA);
        d_summa = d_kol * d_cena;
        d_itogo = d_summa - (d_summa * (d_skidka / 100));
        String Format_Summa = new DecimalFormat("#00.00").format(d_summa);
        String Format_Itogo = new DecimalFormat("#00.00").format(d_itogo);
        localContentValuesUP.put("Kol", rw_newKOL);
        localContentValuesUP.put("Summa", Format_Summa.replace(",", "."));
        localContentValuesUP.put("Itogo", Format_Itogo.replace(",", "."));
        db.update("base_RN", localContentValuesUP, "Kod_RN = ? AND koduid = ?", new String[]{postclass_RN, rw_kodUID});
        cursor.close();


        String query_rn_all = "SELECT SUM(Summa) AS 'newsumma', SUM(Itogo) AS 'newitogo' FROM base_RN WHERE Kod_RN = '" + postclass_RN + "';";
        Cursor cursor_rn_All = db.rawQuery(query_rn_all, null);
        cursor_rn_All.moveToFirst();
        String all_summa = cursor_rn_All.getString(cursor_rn_All.getColumnIndex("newsumma"));
        String all_itogo = cursor_rn_All.getString(cursor_rn_All.getColumnIndex("newitogo"));
        localContentValuesUP_All.put("summa", all_summa);
        localContentValuesUP_All.put("itogo", all_itogo);
        localContentValuesUP_All.put("debet_new", all_itogo);
        getSupportActionBar().setSubtitle("Итого: " + all_itogo);
        postclass_OLD_SUMMA = all_itogo;
        db.update("base_RN_All", localContentValuesUP_All, "Kod_RN = ? ", new String[]{postclass_RN});
        cursor_rn_All.close();


        String query_debet_count = "SELECT * FROM otchet_Debet WHERE d_kontr_uid = '" + postclass_clients_uid + "'";
        Cursor cursor_debet_count = db.rawQuery(query_debet_count, null);
        cursor_debet_count.moveToFirst();
        String debet = cursor_debet_count.getString(cursor_debet_count.getColumnIndex("d_summa"));
        Log.e("OLD_SUMMA", rw_old_summa);
        Log.e("NEW_SUMMA", all_itogo);
        if (Double.parseDouble(rw_old_summa) > Double.parseDouble(all_itogo)) {
            Double new_debet = Double.parseDouble(debet) - (Double.parseDouble(rw_old_summa) - Double.parseDouble(all_itogo));
            String Format_New_Debet = new DecimalFormat("#00.00").format(new_debet);
            localContentValuesUP_Debet.put("d_summa", Format_New_Debet.replace(",", "."));
            Log.e("меньше", Format_New_Debet);
        } else {
            Double new_debet = Double.parseDouble(debet) + ((Double.parseDouble(all_itogo) - Double.parseDouble(rw_old_summa)));
            String Format_New_Debet = new DecimalFormat("#00.00").format(new_debet);
            localContentValuesUP_Debet.put("d_summa", Format_New_Debet.replace(",", "."));
            Log.e("больше", Format_New_Debet);
        }
        db.update("otchet_debet", localContentValuesUP_Debet, "d_kontr_uid = ? ", new String[]{postclass_clients_uid});
        cursor_debet_count.close();


        db.close();
    }


    // Заполнение ListView
    protected void ListAdapter() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);

        String query = "SELECT * FROM base_RN WHERE kod_rn = '" + postclass_RN + "'";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Double double_summa = 0.0, double_itogo = 0.0;
        while (cursor.isAfterLast() == false) {
            db_kod_rn = cursor.getString(cursor.getColumnIndex("Kod_RN"));
            db_summa = cursor.getString(cursor.getColumnIndex("Summa"));
            db_itogo = cursor.getString(cursor.getColumnIndex("Itogo"));
            db_itogo = cursor.getString(cursor.getColumnIndex("Skidka"));

            String db_ls_kod_rn = cursor.getString(cursor.getColumnIndex("Kod_RN"));
            String db_ls_name = cursor.getString(cursor.getColumnIndex("Name")); //
            String db_ls_kol = cursor.getString(cursor.getColumnIndex("Kol")); //
            String db_ls_cena = cursor.getString(cursor.getColumnIndex("Cena")); //
            String db_ls_cena_SK = cursor.getString(cursor.getColumnIndex("Cena_SK")); //
            String db_ls_summa = cursor.getString(cursor.getColumnIndex("Summa"));
            String db_ls_skidka = cursor.getString(cursor.getColumnIndex("Skidka"));
            String db_ls_itogo = cursor.getString(cursor.getColumnIndex("Itogo"));
            String db_ls_image = cursor.getString(cursor.getColumnIndex("Image")); //
            String db_ls_koduid = cursor.getString(cursor.getColumnIndex("koduid")); //
            double_summa = double_summa + Double.parseDouble(db_summa);
            double_itogo = double_itogo + Double.parseDouble(db_itogo);

            rn_end.add(new ListAdapterSimple_RN_END(db_ls_name, db_ls_kol, db_ls_cena, db_ls_cena_SK, db_ls_summa, " - " + db_ls_skidka + " %", db_ls_itogo, db_ls_image, db_ls_koduid));
            cursor.moveToNext();

        }
        String Format_Summa = new DecimalFormat("#00.00").format(double_summa).replaceAll(",", ".");
        //String Format_Skidka = new DecimalFormat("#00.00").format(Double.parseDouble(db_summa) - Double.parseDouble(db_itogo));
        String Format_Itogo = new DecimalFormat("#00.00").format(double_itogo).replaceAll(",", ".");

       /* tvw_name.setText(db_klients);
        tvw_summa.setText("Сумма: " + Format_Summa);
        tvw_skidka.setText("Сумма скидка: " + Format_Skidka);
        tvw_itogo.setText("Итого к оплате: " + Format_Itogo);*/

        cursor.close();
        db.close();
    }

    public void onBackPressed() {
        Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza.class);
        startActivity(intent);
        finish();
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


        PEREM_K_AG_NAME = sp.getString("PEREM_K_AG_NAME", "0");          //чтение данных: имя контраегнта
        PEREM_K_AG_UID = sp.getString("PEREM_K_AG_UID", "0");            //чтение данных: uid контрагента
        PEREM_K_AG_ADRESS = sp.getString("PEREM_K_AG_ADRESS", "0");      //чтение данных: адрес контрагент
        PEREM_K_AG_KodRN = sp.getString("PEREM_K_AG_KodRN", "0");         //чтение данных: код накладной
        PEREM_K_AG_Data = sp.getString("PEREM_K_AG_Data", "0");           //чтение данных: время создание н
        PEREM_K_AG_Data_WORK = sp.getString("PEREM_K_AG_Data_WORK", "0");           //чтение данных: время создание н
        PEREM_K_AG_Vrema = sp.getString("PEREM_K_AG_Vrema", "0");        //чтение данных: дата создание на
        PEREM_K_AG_GPS = sp.getString("PEREM_K_AG_GPS", "0");             //чтение данных: координаты gps
    }

    protected void SQL_KOLBOX(String sql_koduid) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT * FROM base_in_nomeclature WHERE koduid = '" + sql_koduid + "';";
        final Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String kolbox = cursor.getString(cursor.getColumnIndex("kolbox"));
            text_sql_kolbox = kolbox;
        } else text_sql_kolbox = "12";
        cursor.close();
        db.close();
    }

    protected void SQL_OSTATOK(String sql_koduid) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT * FROM base_in_ostatok WHERE nomenclature_uid = '" + sql_koduid + "';";
        final Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String count = cursor.getString(cursor.getColumnIndex("count"));
            text_sql_count = count;
        } else text_sql_count = "";
        cursor.close();
        db.close();
    }

    protected void SQL_UID_SKLAD() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT * FROM base_RN_All WHERE kod_rn = '" + postclass_RN + "';";
        final Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String uid = cursor.getString(cursor.getColumnIndex("sklad_uid"));
            text_sql_uid_sklad = uid;
        }
        cursor.close();
        db.close();
    }

    protected void SQL_ReWrite_Ostatok(String sql_koduid, Integer rw_old_kol, Integer rw_new_kol, String rw_ostatok) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_BASE, MODE_PRIVATE, null);
        //String query = "SELECT * FROM base_in_ostatok WHERE nomenclature_uid = '" + sql_koduid + "' AND sklad_uid = '" + text_sql_uid_sklad + "';";
        String query = "SELECT * FROM base_in_ostatok WHERE nomenclature_uid = '" + sql_koduid + "';";
        Log.e("пеерем:", text_sql_uid_sklad);
        Log.e("пеерем:", sql_koduid);
        ContentValues localContentValuesUP = new ContentValues();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String count = cursor.getString(cursor.getColumnIndex("count"));
        if (rw_old_kol > rw_new_kol) {
            Integer int_count = Integer.valueOf(count) + (rw_old_kol - rw_new_kol);
            localContentValuesUP.put("count", String.valueOf(int_count));
            Log.e("новый остаток:", String.valueOf(int_count));
        } else {
            Integer int_count = Integer.valueOf(count) - (rw_new_kol - rw_old_kol);
            localContentValuesUP.put("count", String.valueOf(int_count));
            Log.e("новый остаток:", String.valueOf(int_count));
        }


        db.update("base_in_ostatok", localContentValuesUP, "nomenclature_uid = ?", new String[]{sql_koduid});
        cursor.close();
        db.close();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        String id_st = item.getTitle().toString();

        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("sp_BREND", id_st);
        ed.commit();
        Intent intent = new Intent(context_Activity, WJ_Dialog_RN_Rewrite.class);
        intent.putExtra("ReWrite_RN", postclass_RN);
        intent.putExtra("ReWrite_Summa", postclass_Summa);
        intent.putExtra("ReWrite_Summa_Debet", postclass_Summa_Debet);
        intent.putExtra("ReWrite_Skidka", postclass_Skidka);
        intent.putExtra("ReWrite_Client_UID", postclass_clients_uid);
        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_nomenclature);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

