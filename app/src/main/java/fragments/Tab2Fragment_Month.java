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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import kg.roman.Mobile_Torgovla.DB_NewSV.DbContract_Nomeclature;
import kg.roman.Mobile_Torgovla.DB_NewSV.DbContract_Otchet_Ostatok;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Otchet_Ostatok;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Otchet_Ostatok;
import kg.roman.Mobile_Torgovla.R;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class Tab2Fragment_Month extends Fragment {

    public ArrayList<ListAdapterSimple_Otchet_Ostatok> adapter_listview = new ArrayList<ListAdapterSimple_Otchet_Ostatok>();
    public ListAdapterAde_Otchet_Ostatok adapterPriceClients;

    public Button button_Scan, button_Scan_QR, button_Add, button_Refresh;
    public ImageView imageview;
    public ListView listView;
    public TextView textView_name, textView_cena, textView_kolbox,
            textView_strih, textView_group, textView_uid, textView_univ, textView_ost, textView_brend;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    public String[] mass_table_db;
    public String contents;
    public Context context;
    public Toolbar toolbar;
    public Context context_Activity;
    public NavigationView navigationView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       /* getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.user_pda);
        getSupportActionBar().setTitle("Сканер штрих-кода");
        // getSupportActionBar().setSubtitle("Аптеки");*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.strih_kod_search, null);
        context = v.getContext();
        button_Scan_QR = (Button) v.findViewById(R.id.button_scan_qr);
        button_Scan = (Button) v.findViewById(R.id.button_scan);
        button_Add = (Button) v.findViewById(R.id.button_add);
        button_Refresh = (Button) v.findViewById(R.id.button_add);
        imageview = (ImageView) v.findViewById(R.id.image_sh);
        textView_name = (TextView) v.findViewById(R.id.textView_Name);

       /* adapter_listview.clear();
        Loading_Ostatki();
        adapterPriceClients = new ListAdapterAde_Otchet_Ostatok(getActivity(), adapter_listview);
        adapterPriceClients.notifyDataSetChanged();
        listView.setAdapter(adapterPriceClients);*/


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

        button_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Tab1", "Add");
            }
        });


       // return inflater.inflate(R.layout.strih_kod_search, null);
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
        mass_table_db = getResources().getStringArray(R.array.mass_table_db);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

                // Получаем данные после работы сканера и выводим их в Toast сообщении:
                contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Toast toast = Toast.makeText(getActivity(), "Содержание: " + contents + " Формат: " + format, Toast.LENGTH_SHORT);
                toast.show();

            }
        }
        textView_name.setText("");
        textView_cena.setText("");
        textView_kolbox.setText("");
        textView_strih.setText("");
        textView_uid.setText("");
        textView_univ.setText("");
        textView_group.setText("");
        textView_ost.setText("");
        textView_brend.setText("");
        imageview.setImageResource(0);
        Loading_Db_Nomencalture(contents);
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
                Toast.makeText(getActivity(), "Есть совпадение ", Toast.LENGTH_LONG).show();
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
                        Picasso.get() //передаем контекст приложения
                                .load(imagePath_SD)
                                .into(imageview); //ссылка на ImageView

                    } else {
                        Picasso.get() //передаем контекст приложения
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
    }  // Загрузка номенклатуры по брендам и по группам

    protected void Loading_Ostatki() {
        SQLiteDatabase db = context.openOrCreateDatabase("suncape_all_db.db3", MODE_PRIVATE, null);
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
           // adapter_listview.add(new ListAdapterSimple_Otchet_Ostatok(name, count + "шт", brends, cena, image, ""));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

}
