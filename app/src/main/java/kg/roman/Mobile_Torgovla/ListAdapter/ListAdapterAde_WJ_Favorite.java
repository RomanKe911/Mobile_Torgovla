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

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_WJ_Favorite;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_WJ_Zakaz;
import kg.roman.Mobile_Torgovla.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_WJ_Favorite extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<ListAdapterSimple_WJ_Favorite> objects;
    ListAdapterAde_WJ_Favorite adapterPriceClients = this;

    CustomFilter filter;
    ArrayList<ListAdapterSimple_WJ_Favorite> filterList;
    private static final String TAG = "MyApp";
    public SharedPreferences sp;
    public String PEREM_RNKod, PEREM_SD, PEREM_PHONE;



    public ListAdapterAde_WJ_Favorite(Context ctx, ArrayList<ListAdapterSimple_WJ_Favorite> product_str) {
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
            convertView = inflater.inflate(R.layout.list_item_nomenclature_forma, null);

        }

        sp = PreferenceManager.getDefaultSharedPreferences(context);
        PEREM_SD = sp.getString("PEREM_IMAGE_PUT_SDCARD", "0");
        PEREM_PHONE = sp.getString("PEREM_IMAGE_PUT_PHONE", "0");

        TextView name = (TextView) convertView.findViewById(R.id.textView_forma_name);
        ImageView image = (ImageView) convertView.findViewById(R.id.textView_forma_image);
        TextView cena = (TextView) convertView.findViewById(R.id.textView_forma_cena);
        TextView cena_skidka = (TextView) convertView.findViewById(R.id.textView_forma_cenanal);
        TextView ostatok = (TextView) convertView.findViewById(R.id.textView_forma_ostatok);
        TextView kol_box = (TextView) convertView.findViewById(R.id.textView_forma_kolbox);
        TextView kod_univ = (TextView) convertView.findViewById(R.id.textView_forma_kod);
        TextView strih = (TextView) convertView.findViewById(R.id.textView_forma_strih);

        name.setText(objects.get(pos).getName());
        cena.setText(objects.get(pos).getCena());
        cena_skidka.setText(objects.get(pos).getCena_skidka());
        ostatok.setText(objects.get(pos).getOstatki());
        kol_box.setText(objects.get(pos).getKol_box());
        kod_univ.setText(objects.get(pos).getKod_univ());
        strih.setText(objects.get(pos).getStrih());

        try {

            File imagePath_SD = new File(PEREM_SD + objects.get(pos).getImage() + ".png");//путь к изображению
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
                ArrayList<ListAdapterSimple_WJ_Favorite> filters = new ArrayList<ListAdapterSimple_WJ_Favorite>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_WJ_Favorite p = new ListAdapterSimple_WJ_Favorite(filterList.get(i).getName(),
                                filterList.get(i).getImage(),
                                filterList.get(i).getCena(),
                                filterList.get(i).getCena_skidka(),
                                filterList.get(i).getOstatki(),
                                filterList.get(i).getKol_box(),
                                filterList.get(i).getKod_univ(),
                                filterList.get(i).getStrih());
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
            objects = (ArrayList<ListAdapterSimple_WJ_Favorite>) results.values;
            notifyDataSetChanged();
        }
    }
    public String name;
    public String image;
    public String cena;
    public String cena_skidka;
    public String ostatki;
    public String kol_box;
    public String kod_univ;
    public String strih;
}