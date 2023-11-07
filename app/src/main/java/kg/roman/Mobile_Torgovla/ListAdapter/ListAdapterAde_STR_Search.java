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

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_STR_Search;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_STR_Search extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<ListAdapterSimple_STR_Search> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_STR_Search> filterList;
    public SharedPreferences sp;
    public String PEREM_K_AG_KodRN, PEREM_SD, PEREM_PHONE;

    public ListAdapterAde_STR_Search(Context ctx, ArrayList<ListAdapterSimple_STR_Search> Edit_Content) {
        // TODO Auto-generated constructor stub
        this.context = ctx;
        this.objects = Edit_Content;
        this.filterList = Edit_Content;
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
            convertView = inflater.inflate(R.layout.spr_strihkod_add, null);
        }

        sp = PreferenceManager.getDefaultSharedPreferences(context);
        PEREM_K_AG_KodRN = sp.getString("PEREM_K_AG_KodRN", "0");  // Переменая номер накладной
        PEREM_SD = sp.getString("PEREM_IMAGE_PUT_SDCARD", "0");
        PEREM_PHONE = sp.getString("PEREM_IMAGE_PUT_PHONE", "0");

        TextView brends = (TextView) convertView.findViewById(R.id.tvw_NT1);
        TextView group = (TextView) convertView.findViewById(R.id.tvw_NT2);
        TextView kod = (TextView) convertView.findViewById(R.id.tvw_NT3);
        TextView name = (TextView) convertView.findViewById(R.id.tvw_G_ost_name);
        TextView kolbox = (TextView) convertView.findViewById(R.id.tvw_NT4);
        TextView cena = (TextView) convertView.findViewById(R.id.tvw_NT5);
        TextView strih = (TextView) convertView.findViewById(R.id.tvw_NT6);
        TextView koduniv = (TextView) convertView.findViewById(R.id.tvw_NT7);
        TextView koduid = (TextView) convertView.findViewById(R.id.tvw_NT8);
        ImageView image = (ImageView) convertView.findViewById(R.id.img_brends);
        TextView name_image = (TextView) convertView.findViewById(R.id.tvw_image);


        //SET DATA TO THEM
        brends.setText(objects.get(pos).getBrends());
        group.setText(objects.get(pos).getP_group());
        kod.setText(objects.get(pos).getKod());
        name.setText(objects.get(pos).getName());
        kolbox.setText(objects.get(pos).getKolbox());
        cena.setText(objects.get(pos).getCena());
        strih.setText(objects.get(pos).getStrih());
        koduniv.setText(objects.get(pos).getKoduniv());
        koduid.setText(objects.get(pos).getKoduid());
        name_image.setText(objects.get(pos).getName_iamge());


        File imagePath_SD = new File(PEREM_SD + objects.get(pos).getImage() + ".png");//путь к изображению
        Uri imagePath_phone_base_1 = Uri.parse(PEREM_PHONE + objects.get(pos).getImage());
        if(imagePath_SD.exists())       {

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
                ArrayList<ListAdapterSimple_STR_Search> filters = new ArrayList<ListAdapterSimple_STR_Search>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_STR_Search p = new ListAdapterSimple_STR_Search(filterList.get(i).getBrends(),
                                filterList.get(i).getP_group(),
                                filterList.get(i).getKod(),
                                filterList.get(i).getName(),
                                filterList.get(i).getKolbox(),
                                filterList.get(i).getCena(),
                                filterList.get(i).getImage(),
                                filterList.get(i).getStrih(),
                                filterList.get(i).getKoduniv(),
                                filterList.get(i).getKoduid(),
                                filterList.get(i).getName_iamge());
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
            objects = (ArrayList<ListAdapterSimple_STR_Search>) results.values;
            notifyDataSetChanged();
        }
    }
}