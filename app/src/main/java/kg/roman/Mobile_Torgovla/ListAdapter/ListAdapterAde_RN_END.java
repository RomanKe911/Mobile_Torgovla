package kg.roman.Mobile_Torgovla.ListAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
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

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_RN_END;
import kg.roman.Mobile_Torgovla.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_RN_END extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<ListAdapterSimple_RN_END> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_RN_END> filterList;
    ListAdapterAde_RN_END adapterPriceClients = this;
    public SharedPreferences sp;
    public String PEREM_K_AG_KodRN, PEREM_SD, PEREM_PHONE, PEREM_READ_KODRN, PEREM_TYPE_MENU, PEREM_DB3_RN;
    public String type_listview ="";

    public ListAdapterAde_RN_END(Context ctx, ArrayList<ListAdapterSimple_RN_END> product_str) {
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
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        PEREM_K_AG_KodRN = sp.getString("PEREM_K_AG_KodRN", "0");  // Переменая номер накладной
        PEREM_SD = sp.getString("PEREM_IMAGE_PUT_SDCARD", "0");
        PEREM_PHONE = sp.getString("PEREM_IMAGE_PUT_PHONE", "0");
        PEREM_READ_KODRN = sp.getString("PEREM_READ_KODRN", "0");
        PEREM_TYPE_MENU = sp.getString("PEREM_TYPE_MENU", "0");
        PEREM_DB3_RN = sp.getString("PEREM_DB3_RN", "0");

        if (convertView == null) {
            if (PEREM_TYPE_MENU.equals("Просмотр")) {
                convertView = inflater.inflate(R.layout.mt_list_zakaz_print, null);
                type_listview = "просмотр";
            } else {
                convertView = inflater.inflate(R.layout.mt_list_zakaz_read, null);
                type_listview = "редактировать";
            }

            //    convertView = inflater.inflate(R.layout.list_item_new_3, null);
        }


        TextView name = (TextView) convertView.findViewById(R.id.tvw_list_name);
        TextView kol = (TextView) convertView.findViewById(R.id.tvw_list_kol);
        TextView cena = (TextView) convertView.findViewById(R.id.tvw_list_cena);
        TextView cenaSK = (TextView) convertView.findViewById(R.id.tvw_list_cenaSK);
        TextView summa = (TextView) convertView.findViewById(R.id.tvw_list_summa);
        TextView skidka = (TextView) convertView.findViewById(R.id.tvw_list_skidka);
        TextView itogo = (TextView) convertView.findViewById(R.id.tvw_list_itogo);
        TextView koduid = (TextView) convertView.findViewById(R.id.tvw_list_kod_uid);
        ImageView image = (ImageView) convertView.findViewById(R.id.img_list);
        Button button_delete = (Button) convertView.findViewById(R.id.button_delete_tovar);

        name.setText(objects.get(pos).getName());
        kol.setText(objects.get(pos).getKol());
        cena.setText(objects.get(pos).getCena());
        cenaSK.setText(objects.get(pos).getCenaSK());
        summa.setText(objects.get(pos).getSumma());
        skidka.setText(objects.get(pos).getSkidka());
        itogo.setText(objects.get(pos).getItogo());
        koduid.setText(objects.get(pos).getKoduid());

        if (type_listview.equals("редактировать"))
        {
            button_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setMessage("Удалить позицию?")
                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    SQLiteDatabase db = context.openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
                                    String query = "DELETE FROM base_RN WHERE koduid = '" + objects.get(pos).getKoduid() + "' AND Kod_RN ='" + PEREM_READ_KODRN + "';";
                                    final Cursor cursor = db.rawQuery(query, null);
                                    cursor.moveToFirst();
                                    while (cursor.isAfterLast() == false) {
                                        cursor.moveToNext();
                                    }
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
        }
        else
        {
            button_delete.setVisibility(View.GONE);
        }



        try {
            File imagePath_SD = new File(PEREM_SD + objects.get(pos).getImage() + ".png");//путь к изображению
            Uri imagePath_phone_base_1 = Uri.parse(PEREM_PHONE + objects.get(pos).getImage());
            if (imagePath_SD.exists()) {
                Picasso.get()
                        .load(imagePath_SD)
                        // .placeholder(R.drawable.button_up) заглушку (placeholder)
                        .error(R.drawable.no_image) //заглушку для ошибки
                        //   .fit()
                        .into(image);
            } else {
                Picasso.get()
                        .load(imagePath_phone_base_1)
                        // .placeholder(R.drawable.button_up) заглушку (placeholder)
                        .error(R.drawable.no_image) //заглушку для ошибки
                        //   .fit()
                        .into(image);
            }

        } catch (Exception e) {
            Log.e("Image_Error", "Нет картинов в ресурсах");
        }

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
                ArrayList<ListAdapterSimple_RN_END> filters = new ArrayList<ListAdapterSimple_RN_END>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_RN_END p = new ListAdapterSimple_RN_END(filterList.get(i).getName(),
                                filterList.get(i).getKol(),
                                filterList.get(i).getCena(),
                                filterList.get(i).getCenaSK(),
                                filterList.get(i).getSumma(),
                                filterList.get(i).getSkidka(),
                                filterList.get(i).getItogo(),
                                filterList.get(i).getImage(),
                                filterList.get(i).getKoduid());
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
            objects = (ArrayList<ListAdapterSimple_RN_END>) results.values;
            notifyDataSetChanged();
        }
    }

    protected void SQL_Delete_Tovar(final String tovar_uid, final String tovar_kodRN) {
        Log.e("удалить uid", tovar_uid);
        Log.e("удалить kodrn", tovar_kodRN);


    }

}