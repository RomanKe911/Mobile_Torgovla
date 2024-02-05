package kg.roman.Mobile_Torgovla.ListAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_WJ_Zakaz;
import kg.roman.Mobile_Torgovla.MT_FTP.FtpConnectData;
import kg.roman.Mobile_Torgovla.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_WJ_Zakaz extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<ListAdapterSimple_WJ_Zakaz> objects;
    ListAdapterAde_WJ_Zakaz adapterPriceClients = this;

    CustomFilter filter;
    ArrayList<ListAdapterSimple_WJ_Zakaz> filterList;
    private static final String TAG = "MyApp";
    public SharedPreferences sp;
    public String PEREM_K_AG_KodRN, PEREM_SD, PEREM_PHONE, PEREM_DB3_RN;


    public ListAdapterAde_WJ_Zakaz(Context ctx, ArrayList<ListAdapterSimple_WJ_Zakaz> product_str) {
        // TODO Auto-generated constructor stub
        this.context = ctx;
        this.objects = product_str;
        this.filterList = product_str;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return objects.size();
    }

    @Override
    public Object getItem(int pos) {
        // TODO Auto-generated method stub
        return objects.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        return objects.indexOf(getItem(pos));
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_edit_content, null);
        }
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        PEREM_K_AG_KodRN = sp.getString("PEREM_K_AG_KodRN", "0");  // Переменая номер накладной
        PEREM_SD = sp.getString("PEREM_IMAGE_PUT_SDCARD", "0");
        PEREM_PHONE = sp.getString("PEREM_IMAGE_PUT_PHONE", "0");
        PEREM_DB3_RN = sp.getString("PEREM_DB3_RN", "0");                        //чтение данных: Путь к базам данных с накладными

        TextView txtID = (TextView) convertView.findViewById(R.id.cont_edit_id);
        TextView txtName = (TextView) convertView.findViewById(R.id.wj_cont_name);
        TextView txtKod = (TextView) convertView.findViewById(R.id.wj_cont_kod);
        TextView txtKol = (TextView) convertView.findViewById(R.id.cont_edit_kol);
        TextView txtCena = (TextView) convertView.findViewById(R.id.wj_cont_cena);
       // TextView txtCena_Sk = (TextView) convertView.findViewById(R.id.wj_cont_cena);
        TextView txtSumma = (TextView) convertView.findViewById(R.id.cont_edit_summa);
        TextView txtSkidka = (TextView) convertView.findViewById(R.id.cont_edit_skidka);
        TextView txtItogo = (TextView) convertView.findViewById(R.id.cont_edit_itogo);
        Button button = (Button) convertView.findViewById(R.id.button_delete_id);
        ImageView image = (ImageView) convertView.findViewById(R.id.img_select);

        txtID.setText(objects.get(pos).getId());
        txtName.setText(objects.get(pos).getName());
        txtKod.setText(objects.get(pos).getKod());
        txtKol.setText(objects.get(pos).getKol());
        txtCena.setText(objects.get(pos).getCena());
        txtSumma.setText(objects.get(pos).getSumma());
        txtSkidka.setText(objects.get(pos).getSkidka());
        txtItogo.setText(objects.get(pos).getItogo());

        try {
           /* File imagePath_SD = new File("/sdcard/Price/Image/" + objects.get(pos).getImage() + ".png");//путь к изображению
            Uri imagePath_phone_base_1 = Uri.parse("android.resource://kg.price_list.roman_kerkin.sunbell_price/drawable/" + objects.get(pos).getImage());*/


            // 21/01/2024 изменено
/*            File imagePath_SD = new File(PEREM_SD + objects.get(pos).getImage() + ".png");//путь к изображению
            Uri imagePath_phone_base_1 = Uri.parse(PEREM_PHONE + objects.get(pos).getImage());
            if(imagePath_SD.exists())
            {
                Picasso.get()
                        .load(imagePath_SD)
                        // .placeholder(R.drawable.button_up) заглушку (placeholder)
                        .error(R.drawable.no_image) //заглушку для ошибки
                        //   .fit()
                        .into(image);
            }
            else {
                Picasso.get()
                        .load(imagePath_phone_base_1)
                        // .placeholder(R.drawable.button_up) заглушку (placeholder)
                        .error(R.drawable.no_image) //заглушку для ошибки
                        //   .fit()
                        .into(image);
            }*/



            try {
                FtpConnectData ftpConnectData = new FtpConnectData();
                File newImage_png = new File(ftpConnectData.put_toPhoneImage(context) + objects.get(pos).getImage() + ".png");
                File newImage_jpg = new File(ftpConnectData.put_toPhoneImage(context) + objects.get(pos).getImage() + ".jpg");
                if (newImage_png.isFile())
                    Picasso.get() //передаем контекст приложения
                            .load(newImage_png)
                            .error(R.drawable.no_image)
                            .into(image); //ссылка на ImageView*/
                else
                    Picasso.get() //передаем контекст приложения
                            .load(newImage_jpg)
                            .error(R.drawable.no_image)
                            .into(image); //ссылка на ImageView*/

                Log.e("Image_PUT_PNG", newImage_png + "__" + newImage_png.isFile());
                Log.e("Image_PUT_JPG", newImage_jpg + "__" + newImage_jpg.isFile());

            } catch (Exception e) {
                Log.e("Image_Error", "Нет картинов в ресурсах");
            }

        } catch (Exception e) {
            Log.e("Image_Error", "Нет картинов в ресурсах");
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Удалить позицию?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SQLiteDatabase db = context.openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
                                String query = "DELETE FROM base_RN  WHERE base_RN.Kod_RN LIKE ('%" + PEREM_K_AG_KodRN + "%') AND base_RN.Kod_Univ LIKE ('%" + objects.get(pos).getKod() + "%')";
                                final Cursor cursor = db.rawQuery(query, null);
                                cursor.moveToFirst();
                                while (cursor.isAfterLast() == false) {

                                    cursor.moveToNext();
                                }
                                Log.e("Delete:", "Удален:" + objects.get(pos).getKod());
                                cursor.close();
                                db.close();

                                objects.remove(objects.get(pos));
                                adapterPriceClients.notifyDataSetChanged();




                               // objects.remove(objects.get(pos).getKod());

                            }
                        })
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                builder.show();
            }
        });


        return convertView;
    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        if (filter == null) {
            filter = new CustomFilter();
        }
        return filter;
    }

    //INNER CLASS
    class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO Auto-generated method stub
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                //CONSTARINT TO UPPER
                constraint = constraint.toString().toUpperCase();
                ArrayList<ListAdapterSimple_WJ_Zakaz> filters = new ArrayList<ListAdapterSimple_WJ_Zakaz>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_WJ_Zakaz p = new ListAdapterSimple_WJ_Zakaz(filterList.get(i).getName(),
                                filterList.get(i).getKod(),
                                filterList.get(i).getKol(),
                                filterList.get(i).getCena(),
                                filterList.get(i).getCena_Sk(),
                                filterList.get(i).getSumma(),
                                filterList.get(i).getSkidka(),
                                filterList.get(i).getItogo(),
                                filterList.get(i).getImage());
                        filters.add(p);
                    }
                }
                results.count = filters.size();
                results.values = filters;
            } else {
                results.count = filterList.size();
                results.values = filterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // TODO Auto-generated method stub
            objects = (ArrayList<ListAdapterSimple_WJ_Zakaz>) results.values;
            notifyDataSetChanged();
        }
    }



}