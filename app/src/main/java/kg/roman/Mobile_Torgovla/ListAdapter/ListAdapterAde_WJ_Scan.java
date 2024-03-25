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

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_WJ_Scan;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_WJ_Scan extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<ListAdapterSimple_WJ_Scan> objects;
    ListAdapterAde_WJ_Scan adapterPriceClients = this;

    CustomFilter filter;
    ArrayList<ListAdapterSimple_WJ_Scan> filterList;
    private static final String TAG = "MyApp";
    public SharedPreferences sp;
    public String PEREM_RNKod,PEREM_SD, PEREM_PHONE;

    public ListAdapterAde_WJ_Scan(Context ctx, ArrayList<ListAdapterSimple_WJ_Scan> scan) {
        // TODO Auto-generated constructor stub
        this.context = ctx;
        this.objects = scan;
        this.filterList = scan;
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
            convertView = inflater.inflate(R.layout.wj_list_scan1, null);
        }
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        PEREM_RNKod = sp.getString("PEREM_RNKod", "0");
        PEREM_SD = sp.getString("PEREM_IMAGE_PUT_SDCARD", "0");
        PEREM_PHONE = sp.getString("PEREM_IMAGE_PUT_PHONE", "0");

        TextView txtName = (TextView) convertView.findViewById(R.id.Content_TovarUID);
        TextView txtKod = (TextView) convertView.findViewById(R.id.wj_cont_kod);
        TextView txtCena = (TextView) convertView.findViewById(R.id.Content_Price);
        TextView txtStrih = (TextView) convertView.findViewById(R.id.wj_cont_strih);
        TextView txtOstatok = (TextView) convertView.findViewById(R.id.wj_cont_ostatok);
        ImageView image = (ImageView) convertView.findViewById(R.id.wj_cont_image);

        txtName.setText(objects.get(pos).getName());
        txtKod.setText(objects.get(pos).getKod());
        txtCena.setText(objects.get(pos).getCena());
        txtCena.setText(objects.get(pos).getCena());
        txtStrih.setText(objects.get(pos).getStrih());
        txtOstatok.setText(objects.get(pos).getOstatok());

        File imagePath_SD = new File(PEREM_SD + objects.get(pos).getImage() + ".png");//путь к изображению
        Uri imagePath_phone_base_1 = Uri.parse(PEREM_PHONE + objects.get(pos).getImage());
        if(imagePath_SD.exists())        {
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
                ArrayList<ListAdapterSimple_WJ_Scan> filters = new ArrayList<ListAdapterSimple_WJ_Scan>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_WJ_Scan p = new ListAdapterSimple_WJ_Scan(filterList.get(i).getName(),
                                filterList.get(i).getKod(),
                                filterList.get(i).getCena(),
                                filterList.get(i).getOstatok(),
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
            objects = (ArrayList<ListAdapterSimple_WJ_Scan>) results.values;
            notifyDataSetChanged();
        }
    }

}