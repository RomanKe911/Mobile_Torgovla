package kg.roman.Mobile_Torgovla.ListAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Agent;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Ros_Torg;
import kg.roman.Mobile_Torgovla.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_Ros_Torg extends BaseAdapter implements Filterable {


    Context context;
    ArrayList<ListAdapterSimple_Ros_Torg> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_Ros_Torg> filterList;
    ListAdapterAde_Ros_Torg adapterPriceClients = this;
    public SharedPreferences sp;
    public String PEREM_K_AG_KodRN, PEREM_SD, PEREM_PHONE;
    public List list;



    public ListAdapterAde_Ros_Torg(Context ctx, ArrayList<ListAdapterSimple_Ros_Torg> adapterSimples_data) {
        // TODO Auto-generated constructor stub
        this.context = ctx;
        this.objects = adapterSimples_data;
        this.filterList = adapterSimples_data;
        this.list = adapterSimples_data;

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
            convertView = inflater.inflate(R.layout.list_ros_torg, null);
        }
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        PEREM_K_AG_KodRN = sp.getString("PEREM_K_AG_KodRN", "0");  // Переменая номер накладной
        PEREM_SD = sp.getString("PEREM_IMAGE_PUT_SDCARD", "0");
        PEREM_PHONE = sp.getString("PEREM_IMAGE_PUT_PHONE", "0");

        ImageView image = (ImageView) convertView.findViewById(R.id.img_rt);
        TextView name = (TextView) convertView.findViewById(R.id.tvw_rt_name);
        TextView cena = (TextView) convertView.findViewById(R.id.tvw_rt_cena);
        TextView kod_univ = (TextView) convertView.findViewById(R.id.tvw_rt_kod_univ);
        TextView strih = (TextView) convertView.findViewById(R.id.tvw_rt_strih);
        TextView ostatok = (TextView) convertView.findViewById(R.id.tvw_rt_ostatok);
        TextView kol_vo = (TextView) convertView.findViewById(R.id.tvw_rt_kol);
        Button button = (Button) convertView.findViewById(R.id.btn_delete);

        name.setText(objects.get(pos).getName());
        cena.setText(objects.get(pos).getCena());
        kod_univ.setText(objects.get(pos).getKod_univ());
        strih.setText(objects.get(pos).getStrih());
        ostatok.setText(objects.get(pos).getOstatok());
        kol_vo.setText(objects.get(pos).getKol_vo());

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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Удалить позицию?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SQLiteDatabase db = context.openOrCreateDatabase("rosnica_db.db3", MODE_PRIVATE, null);
                                String query = "DELETE FROM t5_prodaji  WHERE kod_RN = '" + PEREM_K_AG_KodRN + "'  AND kod_univ = '" + objects.get(pos).getKod_univ() + "'";
                                final Cursor cursor = db.rawQuery(query, null);
                                cursor.moveToFirst();
                                while (cursor.isAfterLast() == false) {

                                    cursor.moveToNext();
                                }
                                Log.e("Delete:", "Удален:" + objects.get(pos).getKod_univ());
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
                ArrayList<ListAdapterSimple_Ros_Torg> filters = new ArrayList<ListAdapterSimple_Ros_Torg>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_Ros_Torg p = new ListAdapterSimple_Ros_Torg(filterList.get(i).getImage(),
                                filterList.get(i).getName(),
                                filterList.get(i).getCena(),
                                filterList.get(i).getKol_vo(),
                                filterList.get(i).getKod_univ(),
                                filterList.get(i).getStrih(),
                                filterList.get(i).getOstatok());
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
            objects = (ArrayList<ListAdapterSimple_Ros_Torg>) results.values;
            notifyDataSetChanged();
        }
    }

    class ViewHolder {
        EditText caption;
    }

}