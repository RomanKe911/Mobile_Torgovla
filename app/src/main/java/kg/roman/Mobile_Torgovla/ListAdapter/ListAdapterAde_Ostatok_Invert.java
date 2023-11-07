package kg.roman.Mobile_Torgovla.ListAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Ostatok_Invert;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Otchet_Ostatok;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_Ostatok_Invert extends BaseAdapter implements Filterable {

    ArrayList<ListAdapterSimple_Ostatok_Invert> objects;
    CustomFilter filter;
    Context context;
    ArrayList<ListAdapterSimple_Ostatok_Invert> filterList;

    public SharedPreferences sp;
    public String PEREM_SD, PEREM_PHONE;

    public ListAdapterAde_Ostatok_Invert(Context ctx, ArrayList<ListAdapterSimple_Ostatok_Invert> product_str) {
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
    public View getView(int pos, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spr_listview_invent_list, null);
        }

        sp = PreferenceManager.getDefaultSharedPreferences(context);
        PEREM_SD = sp.getString("PEREM_IMAGE_PUT_SDCARD", "0");
        PEREM_PHONE = sp.getString("PEREM_IMAGE_PUT_PHONE", "0");

        TextView name = convertView.findViewById(R.id.tvw_invent_name);
        TextView cena = convertView.findViewById(R.id.tvw_invent_price);
        TextView kolbox = convertView.findViewById(R.id.tvw_invent_kolbox);
        TextView kolbox_delete = convertView.findViewById(R.id.tvw_invent_ostatok_delete);
        TextView ostatok = convertView.findViewById(R.id.tvw_invent_ostatok);
        TextView ostatok_f = convertView.findViewById(R.id.tvw_invent_ostatok_f);
        TextView razn = convertView.findViewById(R.id.tvw_invent_ostatok_invent);
        TextView shtrih = convertView.findViewById(R.id.tvw_invent_shtrih);
        TextView kodUid = convertView.findViewById(R.id.tvw_invent_koduid);
        ImageView image = convertView.findViewById(R.id.img_invent_image);

        name.setText(objects.get(pos).getName());
        cena.setText(objects.get(pos).getCena());
        kolbox.setText(objects.get(pos).getKolbox());
        kolbox_delete.setText(objects.get(pos).getKolbox_Delete());
        ostatok.setText(objects.get(pos).getOstatok());
        ostatok_f.setText(objects.get(pos).getOstatok_f());
        razn.setText(objects.get(pos).getRazn());
        shtrih.setText(objects.get(pos).getStrih());
        kodUid.setText(objects.get(pos).getKod_uid());

        File imagePath_SD = new File(PEREM_SD + objects.get(pos).getImage() + ".png");//путь к изображению
        Uri imagePath_phone_base_1 = Uri.parse(PEREM_PHONE + objects.get(pos).getImage());
        // imagePath_all = Uri.parse(PEREM_PHONE + objects.get(pos).getImage());

        if (imagePath_SD.exists()) {
            Picasso.get() //передаем контекст приложения
                    .load(imagePath_SD)
                    .into(image); //ссылка на ImageView
        } else {
            Picasso.get() //передаем контекст приложения
                    .load(imagePath_phone_base_1)
                    .into(image); //ссылка на ImageView
        }

        try {


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
                ArrayList<ListAdapterSimple_Ostatok_Invert> filters = new ArrayList<ListAdapterSimple_Ostatok_Invert>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_Ostatok_Invert p = new ListAdapterSimple_Ostatok_Invert(filterList.get(i).getName(),
                                filterList.get(i).getKod_uid(),
                                filterList.get(i).getCena(),
                                filterList.get(i).getOstatok(),
                                filterList.get(i).getOstatok_f(),
                                filterList.get(i).getKolbox(),
                                filterList.get(i).getKolbox_Delete(),
                                filterList.get(i).getRazn(),
                                filterList.get(i).getStrih(),
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

            objects = (ArrayList<ListAdapterSimple_Ostatok_Invert>) results.values;
            notifyDataSetChanged();
        }
    }

}