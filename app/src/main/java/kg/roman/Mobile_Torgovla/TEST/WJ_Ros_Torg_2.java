package kg.roman.Mobile_Torgovla.TEST;

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
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Ros_Torg;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Ros_Torg;
import kg.roman.Mobile_Torgovla.Permission.PrefActivity;
import kg.roman.Mobile_Torgovla.R;

public class WJ_Ros_Torg_2 extends AppCompatActivity {

   /* public ArrayList<ListAdapterSimple_WJ_Zakaz> list_zakaz = new ArrayList<ListAdapterSimple_WJ_Zakaz>();
    public ListAdapterAde_WJ_Zakaz adapterPriceClients;*/

    public org.apache.poi.sl.draw.geom.Context context;
    public org.apache.poi.sl.draw.geom.Context ctx;
    public OutputStream out;
    public SharedPreferences sp;
    public SharedPreferences.Editor ed;
    public Context context_Activity;


    public TextView dg_tw_name, dg_tw_koduniv, dg_tw_cena, dg_tw_ostatok, dg_tw_kol, dg_tw_summa, dg_tw_summa_sk, dg_tw_kolbox, dg_tw_kolbox_org;

    private Handler mHandler = new Handler();
    public androidx.appcompat.app.AlertDialog.Builder dialog;
    private static final String TAG = "Forma_Edit";
    public Button btn_down, btn_up;
    public Button button_ok, button_up, button_Go, button_cancel, button_down, position_delete;

    public View localView;
    public Menu menu;
    public Toolbar toolbar;

    public Integer checked_group, kol_box_info, max_box;
    public Integer perem_int_summa, perem_int_ostatok, perem_int_cena, perem_int_kol, perem_kol_group_one, perem_int_kolbox;
    public Double Aut_Summa, Auto_Summa, Itog_Summa, dg_tw_summa_sk_DOUBLE;
    public String ftp_put, ftp_server, login, password, ID_TOVAR, ID_DELETE_POS;

    public String kol_group_one, kol_group_much, SKIDKA_TY, name_put, Format_cena_sk;
    public String PEREM_Agent, PEREM_KAgent, PEREM_KAgent_UID, PEREM_Vrema, PEREM_Data, PEREM_RNKod, Summa, PEREM_UID, PEREM_Adress;
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day;

    public ArrayList<ListAdapterSimple_Ros_Torg> strih_kod_list = new ArrayList<ListAdapterSimple_Ros_Torg>();
    public ListAdapterAde_Ros_Torg adapterPriceClients;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    public Button button_add, button_favorite, button_scan, button_print, button_refresh, button_summa;
    public ListView listView;
    public TextView textView_aut_summa;
    public String lst_tw_name, lst_tw_kod, lst_tw_cena, lst_tw_ostatok, lst_tw_kol;
    public String contents, sklad_name;
    public String data_write_now, vrema_write_now;
    public String[] mass_month;
    public String Ident_Mon, Ident_Year, thismonth_rn, KOD_RN, kod_univ;
    public Boolean sovp;
    public Integer aut_summa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_ros_torg_2);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.user_pda);
        getSupportActionBar().setTitle("Розничные продажи");


        context_Activity = WJ_Ros_Torg_2.this;
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        Global_Perem();  // Глобальные переменные
        Calendare();  // Время и дата
        Write_Kod_RN();  // Создание Кода
        getSupportActionBar().setSubtitle(KOD_RN);
        ed = sp.edit();
        ed.putString("ROSN_KOD_RN", KOD_RN);  // передача кода агента
        ed.commit();

        sklad_name = "EA661E8F-90D1-48BF-A530-5998FB65BFDD";

        button_add = (Button) findViewById(R.id.button_add);
        button_favorite = (Button) findViewById(R.id.button_filter);
        button_scan = (Button) findViewById(R.id.button_scan);
        Button_Panel();

        textView_aut_summa = (TextView) findViewById(R.id.tvw_autosumma);
        aut_summa = 0;
        textView_aut_summa.setText(String.valueOf(aut_summa));

        listView = (ListView) findViewById(R.id.ListView_Klients_Tovar);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Заполнение из карточик товара
                lst_tw_name = ((TextView) view.findViewById(R.id.tvw_rt_name)).getText().toString();
                lst_tw_kod = ((TextView) view.findViewById(R.id.tvw_rt_kod_univ)).getText().toString();
                lst_tw_cena = ((TextView) view.findViewById(R.id.tvw_rt_cena)).getText().toString();
                lst_tw_kol = ((TextView) view.findViewById(R.id.tvw_rt_kol)).getText().toString();
                lst_tw_ostatok = ((TextView) view.findViewById(R.id.tvw_rt_ostatok)).getText().toString();
                Fun_Messeger_Panel(lst_tw_name, lst_tw_kod, lst_tw_cena, lst_tw_kol, lst_tw_ostatok);
                //Fun_Messeger_Panel(lst_tw_name, lst_tw_kod, lst_tw_cena, lst_tw_kol, lst_tw_ostatok);

               /* SQLiteDatabase db = getBaseContext().openOrCreateDatabase("rosnica_db.db3", MODE_PRIVATE, null);
                String query = "SELECT vrema, data, kod_RN, kod_univ, name, kol_vo, cena, summa, ostatok, strih, image\n" +
                        "FROM t5_prodaji";
                final Cursor cursor = db.rawQuery(query, null);
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    String kod_univ = cursor.getString(cursor.getColumnIndex("kod_univ"));
                    try {
                        if (lst_tw_kod.equals(kod_univ))
                        {
                            Loading_Rename_list(lst_tw_name, lst_tw_kod, lst_tw_cena, lst_tw_kol, lst_tw_ostatok);
                            cursor.moveToNext();
                        } else cursor.moveToNext();

                    } catch (Exception e) {
                        Log.e("WJ_Ros_2", "Ошибка изменения!");
                        Toast.makeText(context_Activity, "Ошибка заполнения карточки!", Toast.LENGTH_SHORT).show();
                    }

                }
                cursor.close();
                db.close();*/
            }
        });


    }

    @Override
    public void onResume() {
        Log.e("Resume", "Yes!");
       /* list_zakaz.clear();
        Loading_Adapter();
        adapterPriceClients = new ListAdapterAde_WJ_Zakaz(context_Activity, list_zakaz);
        adapterPriceClients.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients);*/
        super.onResume();
    }


    protected void Delete_RN_BASE(String rn) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("rosnica_db.db3", MODE_PRIVATE, null);
        String query = "DELETE FROM t5_prodaji WHERE kod_RN LIKE ('" + rn + "%')";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            cursor.moveToNext();
        }
        Toast.makeText(context_Activity, "Данные " + rn + " удаленны!", Toast.LENGTH_SHORT).show();
        cursor.close();
        db.close();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    private void Calendare() {
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
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd:MM:yyyy");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateString_NOW = dateFormat1.format(calendar.getTime());
        String dateString_WORK = dateFormat2.format(calendar.getTime());
        data_write_now = dateString_WORK;
        vrema_write_now = this_rn_vrema;
        //this_data_now = dateString_NOW + " " + this_rn_vrema;
        Log.e(this.getLocalClassName(), "Дата" + dateString_NOW);
    }

    protected void Global_Perem() {
        PEREM_Agent = sp.getString("PEREM_Agent", "0");
        PEREM_KAgent = sp.getString("PEREM_KAgent", "0");
        PEREM_KAgent_UID = sp.getString("PEREM_KAgent_UID", "0");
        PEREM_Vrema = sp.getString("PEREM_Vrema", "0");
        PEREM_Data = sp.getString("PEREM_Data", "0");
        PEREM_RNKod = sp.getString("PEREM_RNKod", "0");
        PEREM_UID = sp.getString("UID_AGENTS", "0");
        PEREM_Adress = sp.getString("PEREM_Adress", "0");
    }

    protected void Fun_Messeger_Panel(final String m_name, final String m_kod, final String m_cena, final String m_kol, final String m_ostatok) {
        localView = LayoutInflater.from(context_Activity).inflate(R.layout.list_ros_torg_add, null);
        btn_up = (Button) localView.findViewById(R.id.btn_add_up);
        btn_down = (Button) localView.findViewById(R.id.btn_add_down);

        dg_tw_name = (TextView) localView.findViewById(R.id.tvw_text_aks_name);
        dg_tw_koduniv = (TextView) localView.findViewById(R.id.tvw_text_aks_koduid);
        dg_tw_cena = (TextView) localView.findViewById(R.id.tvw_text_aks_cena);
        dg_tw_ostatok = (TextView) localView.findViewById(R.id.tvw_text_aks_ostatok);
        dg_tw_kol = (TextView) localView.findViewById(R.id.tvw_text_aks_kol);
        dg_tw_summa = (TextView) localView.findViewById(R.id.tvw_text_aks_summa);

        dg_tw_name.setText(m_name);
        dg_tw_koduniv.setText(m_kod);
        dg_tw_cena.setText(m_cena);
        Integer ost_prw = (Integer.parseInt(m_ostatok));
        dg_tw_ostatok.setText(ost_prw.toString());
        dg_tw_kol.setText(m_kol);

        perem_int_cena = Integer.parseInt(m_cena);
        perem_int_kol = Integer.parseInt(m_kol);
        perem_int_ostatok = Integer.parseInt(m_ostatok);
        perem_int_summa = perem_int_cena * perem_int_kol;
        String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
        dg_tw_summa.setText(Format);

        if (perem_int_kol > 0) {
            Button_Up(perem_int_cena, perem_int_ostatok, perem_int_kol);
            Button_Down(perem_int_cena, perem_int_ostatok, perem_int_kol);
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(context_Activity);

            localBuilder.setView(localView);
            localBuilder.setTitle("Изменить кол-во...");
            localBuilder.setCancelable(false).setIcon(R.drawable.office_refresh).setPositiveButton(" ", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    //Write_Table_RN(m_name, m_kod, String.valueOf(kol_text), m_cena, summa.toString(), ostatok.toString(), contents, "image");

                    Integer kol_text = Integer.parseInt(dg_tw_kol.getText().toString());
                    Integer ostatok = Integer.parseInt(dg_tw_ostatok.getText().toString());
                    Integer summa = kol_text * Integer.valueOf(m_cena);
                    Rewrite_Table_RN(m_kod, String.valueOf(kol_text), summa.toString(), ostatok.toString(), KOD_RN);
                    Loading_AutoSumma();
                    Log.e("Write", m_name + " " + m_kod + " " + m_cena + " " + m_kol + " " + m_ostatok);
                    Log.e("Write2", kol_text + " " + ostatok + " " + summa);

                    try {


                        if (kol_text > 0) {

                        } else {
                            Toast.makeText(context_Activity, "Товара не достаточно на складе", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(context_Activity, "Неправильный ввод данных", Toast.LENGTH_SHORT).show();
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
        } else
            Toast.makeText(context_Activity, "не достаточно товара", Toast.LENGTH_SHORT).show();


    }


    protected void Button_Panel() {


        button_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBar();
            }
        });

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new androidx.appcompat.app.AlertDialog.Builder(context_Activity);
                dialog.setTitle("Вы хотите оформить заказ?");
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Loading_Delete_Count();
                        Toast.makeText(context_Activity, KOD_RN + " Добавленно! ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context_Activity, WJ_Ros_Torg.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        dialog = new androidx.appcompat.app.AlertDialog.Builder(context_Activity);
        dialog.setTitle("Вы хотите отменить заказ?");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Delete_RN_BASE(KOD_RN);
                Intent intent = new Intent(context_Activity, WJ_Ros_Torg.class);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
            }
        });
        dialog.show();
        // super.onBackPressed();
    }

    protected void Write_Table_RN(final String w_name, final String w_koduniv, final String w_kol,
                                  final String w_cena, final String w_summa, final String w_ostatok,
                                  final String w_strih, final String w_image, final String w_kod_uid) {
        SQLiteDatabase db_up = getBaseContext().openOrCreateDatabase("rosnica_db.db3", MODE_PRIVATE, null);

        String query = "SELECT vrema, data, kod_RN, kod_univ, name, kol_vo, cena, summa, ostatok, strih, image, kod_uid\n" +
                "FROM t5_prodaji";

        Cursor cursor = db_up.rawQuery(query, null);
        ContentValues localContentValuesUP = new ContentValues();
        cursor.moveToLast();
        localContentValuesUP.put("vrema", vrema_write_now);
        localContentValuesUP.put("data", data_write_now);
        localContentValuesUP.put("kod_RN", KOD_RN);
        localContentValuesUP.put("kod_univ", w_koduniv);
        localContentValuesUP.put("name", w_name);
        localContentValuesUP.put("kol_vo", w_kol);
        localContentValuesUP.put("cena", w_cena);
        localContentValuesUP.put("summa", w_summa);
        localContentValuesUP.put("ostatok", w_ostatok);
        localContentValuesUP.put("strih", w_strih);
        localContentValuesUP.put("image", w_image);
        localContentValuesUP.put("kod_uid", w_kod_uid);
        db_up.insert("t5_prodaji", null, localContentValuesUP);
        cursor.close();
        db_up.close();
    }   // запись данных

    protected void Rewrite_Table_RN(final String rw_koduniv, final String rw_kol,
                                    final String rw_summa, final String rw_ostatok, final String rw_kod_RN) {
        SQLiteDatabase db_up = getBaseContext().openOrCreateDatabase("rosnica_db.db3", MODE_PRIVATE, null);

        String query = "SELECT vrema, data, kod_RN, kod_univ, name, kol_vo, cena, summa, ostatok, strih, image\n" +
                "FROM t5_prodaji";

        Cursor cursor = db_up.rawQuery(query, null);
        ContentValues localContentValuesUP = new ContentValues();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String kod_RN = cursor.getString(cursor.getColumnIndex("kod_RN"));
            String kod_univ = cursor.getString(cursor.getColumnIndex("kod_univ"));

            if (rw_kod_RN.equals(kod_RN) & rw_koduniv.equals(kod_univ)) {
                localContentValuesUP.put("kod_RN", kod_RN);
                localContentValuesUP.put("kod_univ", rw_koduniv);
                localContentValuesUP.put("kol_vo", rw_kol);
                localContentValuesUP.put("summa", rw_summa);
                localContentValuesUP.put("ostatok", rw_ostatok);
                String[] arrayOfString = new String[1];
                String[] arrayOfString2 = new String[1];
                arrayOfString[0] = rw_kod_RN;
                arrayOfString2[0] = rw_koduniv;
                db_up.update("t5_prodaji", localContentValuesUP, "kod_RN = ? AND kod_univ = ?", new String[]{rw_kod_RN, rw_koduniv});
                cursor.moveToNext();
            } else cursor.moveToNext();
        }

        strih_kod_list.clear();
        Loading_Adapter_Refresh(rw_kod_RN);
        adapterPriceClients = new ListAdapterAde_Ros_Torg(context_Activity, strih_kod_list);
        adapterPriceClients.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients);
        Toast.makeText(context_Activity, "Обновленно!", Toast.LENGTH_SHORT).show();
        cursor.close();
        db_up.close();
    }   // запись данных


    protected void Button_Up(final Integer up_cena, final Integer up_ostatok, final Integer up_kol) {
        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer i = Integer.parseInt(dg_tw_kol.getText().toString());
                //Integer k = Integer.parseInt(dg_tw_kol.getText().toString());
                //Integer ostatok_itg = up_ostatok+up_kol-1;
                Log.e("BTN_UP ", up_ostatok + " " + up_kol + " " + i);
                if (i < up_ostatok) {
                    i++;
                    int ras = up_ostatok - i;
                    dg_tw_kol.setText(String.valueOf(i));
                    // dg_tw_ostatok.setText((String.valueOf(ras)));
                    perem_int_summa = up_cena * i;
                    String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                    dg_tw_summa.setText(Format);
                } else
                    Toast.makeText(context_Activity, "не достаточно товара", Toast.LENGTH_SHORT).show();

            }
        });

    }  // добавления количества

    protected void Button_Down(final Integer dw_cena, final Integer dw_ostatok, final Integer dw_kol) {
        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer i = Integer.parseInt(dg_tw_kol.getText().toString());
                Integer os = Integer.parseInt(dg_tw_ostatok.getText().toString());
                if (i > 1) {
                    i--;
                    os++;
                    //dg_tw_ostatok.setText((String.valueOf(os)));
                    dg_tw_kol.setText(String.valueOf(i));
                    perem_int_summa = dw_cena * i;
                    String Format = new DecimalFormat("#00.00").format(perem_int_summa).replace(",", ".");
                    dg_tw_summa.setText(Format);
                } else
                    Toast.makeText(context_Activity, "Кол-во меньше 1", Toast.LENGTH_SHORT).show();
            }
        });

    }  // убавить количество


    // Запускаемм сканер штрих кода:
    public void scanBar() {
        try {

            // Запускаем переход на com.google.zxing.client.android.SCAN с помощью intent:
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {

            // Предлагаем загрузить с Play Market:
            showDialog(WJ_Ros_Torg_2.this, "Сканнер не найден", "Установить сканер с Play Market?", "Да", "Нет").show();
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
        //mass_table_db = getResources().getStringArray(R.array.mass_table_db);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

                // Получаем данные после работы сканера и выводим их в Toast сообщении:
                contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Toast toast = Toast.makeText(this, "Содержание: " + contents + " Формат: " + format, Toast.LENGTH_SHORT);
                toast.show();
                Log.e("Код ", contents);

            }
        }
        sovp = false;
        Log.e("Код_До ", sovp.toString());
        Loading_Db_ListView(contents);
        Log.e("Код_После ", sovp.toString());
        if (sovp == false) {
            Loading_Db_Nomencalture(contents);
        } else Toast.makeText(this, "Данная товар уже есть! ", Toast.LENGTH_LONG).show();


        adapterPriceClients = new ListAdapterAde_Ros_Torg(context_Activity, strih_kod_list);
        adapterPriceClients.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients);
    }

    protected void Loading_Db_Nomencalture(String strih) {

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("rosnica_db.db3", MODE_PRIVATE, null);
        String query = "SELECT t1_nomeclature.name, t2_ostatki.count, t3_price.cena, t1_nomeclature.strih, t1_nomeclature.image, \n" +
                "t1_nomeclature.kod_univ, t1_nomeclature.koduid, t1_nomeclature.koduid, t2_ostatki.sklad\n" +
                "FROM t1_nomeclature\n" +
                "INNER JOIN t2_ostatki ON t1_nomeclature.koduid=t2_ostatki.koduid\n" +
                "INNER JOIN t3_price ON t1_nomeclature.koduid=t3_price.koduid\n" +
                "WHERE t1_nomeclature.strih LIKE '" + strih + "%' AND t2_ostatki.sklad = '" + sklad_name + "'";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String count = cursor.getString(cursor.getColumnIndex("count"));
            String cena = cursor.getString(cursor.getColumnIndex("cena"));
            String strih_kod = cursor.getString(cursor.getColumnIndex("strih"));
            String koduid = cursor.getString(cursor.getColumnIndex("koduid"));
            String kod_univ = cursor.getString(cursor.getColumnIndex("kod_univ"));
            String image = cursor.getString(cursor.getColumnIndex("image"));

            if (contents.equals(strih_kod)) {
                Toast.makeText(this, "Есть совпадение ", Toast.LENGTH_LONG).show();
                strih_kod_list.add(new ListAdapterSimple_Ros_Torg(image, name, cena, "1", kod_univ, strih, count));
                Write_Table_RN(name, kod_univ, "1", cena, cena, count, contents, image, koduid);
                Loading_AutoSumma();
                cursor.moveToNext();
            } else {
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();
    }

    protected void Loading_Db_ListView(String strih) {

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("rosnica_db.db3", MODE_PRIVATE, null);
        String query = "SELECT kod_RN, name, strih \n" +
                "FROM t5_prodaji\n" +
                "WHERE t5_prodaji.strih LIKE '" + strih + "%' AND t5_prodaji.kod_RN = '" + KOD_RN + "'";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            String strih_kod = cursor.getString(cursor.getColumnIndex("strih"));
            if (contents.equals(strih_kod)) {
                sovp = true;
                cursor.moveToNext();
            } else {
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();
    }


    // Создаем код для накладной
    protected void Write_Kod_RN() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("rosnica_db.db3", MODE_PRIVATE, null);
        String query = "SELECT vrema, data, kod_RN, kod_univ, name, kol_vo, cena, summa, ostatok, strih, image\n" +
                "FROM t5_prodaji";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToLast();
        String kod_univ = cursor.getString(cursor.getColumnIndex("kod_RN"));
        Log.e("WROS_TORG: ", "R1=" + kod_univ);
        mass_month = getResources().getStringArray(R.array.mass_month);
        for (int i = 1; i <= 12; i++) {
            if (Integer.valueOf(this_rn_month) == i) {
                thismonth_rn = mass_month[i - 1];
                Log.e("Месяц=", "i=" + i + ", " + thismonth_rn);
            }
        }
        // Log.e("МесяцOLD PREF=", kod_univ.substring(3, kod_univ.length() - 7));

        Integer k;
        if (thismonth_rn.equals(kod_univ.substring(3, kod_univ.length() - 7))) {
            k = Integer.parseInt(kod_univ.substring(10)) + 1;
            Log.e("WRITE_RN:", "True=" + k);
        } else {
            k = 1;
            Log.e("WRITE_RN:", "False=" + k);
        }

        Ident_Mon = thismonth_rn;
        Ident_Year = this_rn_year.substring(2, 4);
        String iden = "000";
        String iden2 = "00";
        String iden3 = "0";
        // String iden4 = "0";

        if (k < 10) KOD_RN = "Ros" + Ident_Mon + Ident_Year + "_" + iden + k;
        else if (k < 100) KOD_RN = "Ros" + Ident_Mon + Ident_Year + "_" + iden2 + k;
        else if (k < 1000) KOD_RN = "Ros" + Ident_Mon + Ident_Year + "_" + iden3 + k;
        else if (k < 10000) KOD_RN = "Ros" + Ident_Mon + Ident_Year + "_" + k;
        // else if (k < 100000) KOD_RN = "Rn" + Ident_Mon + Ident_Year + "_" + k;
        Log.e("WRITE_RN:", KOD_RN);
    }

    protected void Loading_Adapter_Refresh(String kod_rn) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("rosnica_db.db3", MODE_PRIVATE, null);
        String query = "SELECT vrema, data, kod_RN, kod_univ, name, kol_vo, cena, summa, ostatok, strih, image\n" +
                "FROM t5_prodaji\n" +
                "WHERE kod_RN = '" + kod_rn + "';";

        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String kol_vo = cursor.getString(cursor.getColumnIndex("kol_vo"));
            String cena = cursor.getString(cursor.getColumnIndex("cena"));
            String strih = cursor.getString(cursor.getColumnIndex("strih"));
            String kod_univ = cursor.getString(cursor.getColumnIndex("kod_univ"));
            String ostatok = cursor.getString(cursor.getColumnIndex("ostatok"));
            String image = cursor.getString(cursor.getColumnIndex("image"));

            strih_kod_list.add(new ListAdapterSimple_Ros_Torg(image, name, cena, kol_vo, kod_univ, strih, ostatok));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    protected void Loading_AutoSumma() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("rosnica_db.db3", MODE_PRIVATE, null);
        String query = "SELECT SUM(summa) AS 'aut_sum'\n" +
                "FROM t5_prodaji\n" +
                "WHERE t5_prodaji.kod_RN = '" + KOD_RN + "'";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String summa = cursor.getString(cursor.getColumnIndex("aut_sum"));
        textView_aut_summa.setText(summa);
        cursor.close();
        db.close();
    }

    protected void Loading_Delete_Count() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("rosnica_db.db3", MODE_PRIVATE, null);
        String query = "SELECT t5_prodaji.name, t5_prodaji.kod_uid, t5_prodaji.kol_vo, t5_prodaji.kod_RN, t2_ostatki.koduid, t2_ostatki.count\n" +
                "FROM t5_prodaji\n" +
                "INNER JOIN t2_ostatki ON t5_prodaji.kod_uid=t2_ostatki.koduid\n" +
                "WHERE t5_prodaji.kod_RN = '" + KOD_RN + "'";

        final Cursor cursor = db.rawQuery(query, null);
        ContentValues localContentValuesUP = new ContentValues();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String kol_vo_v_db = cursor.getString(cursor.getColumnIndex("count"));
            String kod_uid = cursor.getString(cursor.getColumnIndex("kod_uid"));
            String kol_vo_prod = cursor.getString(cursor.getColumnIndex("kol_vo"));
            Integer rarz = Integer.parseInt(kol_vo_v_db) - Integer.parseInt(kol_vo_prod);
            localContentValuesUP.put("count", rarz);
            String[] arrayOfString = new String[1];
            arrayOfString[0] = kod_uid;
            db.update("t2_ostatki", localContentValuesUP, "koduid = ?", new String[]{kod_uid});
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }


    protected void ReWrite_Ostatki(String rwo_kod_uid, Integer rwo_count) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("rosnica_db.db3", MODE_PRIVATE, null);
        String query = "SELECT koduid, count, sklad FROM t2_ostatki\n" +
                "WHERE koduid LIKE '" + rwo_kod_uid + "%'";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        ContentValues localContentValuesUP = new ContentValues();
        while (cursor.isAfterLast() == false) {
            String koduid = cursor.getString(cursor.getColumnIndex("koduid"));
            String count = cursor.getString(cursor.getColumnIndex("count"));
            String sklad = cursor.getString(cursor.getColumnIndex("sklad"));
            if (rwo_kod_uid.equals(koduid)) {
                Integer k1 = Integer.valueOf(count) - rwo_count;
                if (k1 != 0) {
                    localContentValuesUP.put("count", k1);
                    String[] arrayOfString = new String[1];
                    arrayOfString[0] = koduid;
                    db.update("t2_ostatki", localContentValuesUP, "koduid = ?", new String[]{koduid});
                    cursor.moveToNext();
                } else {
                    localContentValuesUP.put("count", k1);
                    db.delete("t2_ostatki", "koduid = ?", new String[]{koduid});
                    cursor.moveToNext();
                }
            }
        }

        cursor.close();
        db.close();
    }

}













