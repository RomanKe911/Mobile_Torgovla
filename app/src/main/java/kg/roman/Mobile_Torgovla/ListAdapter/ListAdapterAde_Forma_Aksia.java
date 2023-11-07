package kg.roman.Mobile_Torgovla.ListAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
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

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Forma_Aksia;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_Forma_Aksia extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<ListAdapterSimple_Forma_Aksia> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_Forma_Aksia> filterList;

    public SharedPreferences sp;
    public SharedPreferences.Editor ed;
    public String PEREM_SD, PEREM_PHONE;

    public ListAdapterAde_Forma_Aksia(Context ctx, ArrayList<ListAdapterSimple_Forma_Aksia> adapterSimples_data) {
        // TODO Auto-generated constructor stub
        this.context = ctx;
        this.objects = adapterSimples_data;
        this.filterList = adapterSimples_data;
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
            convertView = inflater.inflate(R.layout.list_wj_akcia, null);
        }
        TextView name = (TextView) convertView.findViewById(R.id.tvw_name_aks);
        TextView kol = (TextView) convertView.findViewById(R.id.tvw_aks_kol);
        TextView cena = (TextView) convertView.findViewById(R.id.tvw_aks_price);
        TextView cena_aks = (TextView) convertView.findViewById(R.id.tvw_aks_price_bonus);
        TextView ostatok = (TextView) convertView.findViewById(R.id.tvw_aks_ostatok);
        TextView kod = (TextView) convertView.findViewById(R.id.tvw_kod_aks);
        TextView skidka = (TextView) convertView.findViewById(R.id.tvw_aks_skidka);
        TextView sklad = (TextView) convertView.findViewById(R.id.tvw_aks_sklad);
        TextView sklad_uid = (TextView) convertView.findViewById(R.id.tvw_aks_sklad_uid);
        ImageView image = (ImageView) convertView.findViewById(R.id.image_aks);


        name.setText(objects.get(pos).getName());
        kod.setText(objects.get(pos).getKod_UID());
        sklad.setText(objects.get(pos).getSklad());
        sklad_uid.setText(objects.get(pos).getSklad_uid());
        kol.setText(objects.get(pos).getKol());
        skidka.setText(objects.get(pos).getSkidka());
        cena.setText(objects.get(pos).getCena());
        cena_aks.setText(objects.get(pos).getCena_aks());
        ostatok.setText(objects.get(pos).getOstatok());

        sp = PreferenceManager.getDefaultSharedPreferences(context);
        PEREM_SD = sp.getString("PEREM_IMAGE_PUT_SDCARD", "0");
        PEREM_PHONE = sp.getString("PEREM_IMAGE_PUT_PHONE", "0");

       /* File imagePath_SD = new File("/sdcard/Price/Image/" + objects.get(pos).getImage() + ".png");//путь к изображению
        Uri imagePath_phone_base_1 = Uri.parse("android.resource://kg.price_list.roman_kerkin.sunbell_price/drawable/" + objects.get(pos).getImage());*/

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
        //SET DATA TO THEM


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
                ArrayList<ListAdapterSimple_Forma_Aksia> filters = new ArrayList<ListAdapterSimple_Forma_Aksia>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_Forma_Aksia p = new ListAdapterSimple_Forma_Aksia(filterList.get(i).getName(),
                                filterList.get(i).getKod_UID(),
                                filterList.get(i).getKol(),
                                filterList.get(i).getCena(),
                                filterList.get(i).getCena_aks(),
                                filterList.get(i).getSkidka(),
                                filterList.get(i).getOstatok(),
                                filterList.get(i).getImage(),
                                filterList.get(i).getSklad(),
                                filterList.get(i).getSklad_uid());
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
            objects = (ArrayList<ListAdapterSimple_Forma_Aksia>) results.values;
            notifyDataSetChanged();
        }
    }

}