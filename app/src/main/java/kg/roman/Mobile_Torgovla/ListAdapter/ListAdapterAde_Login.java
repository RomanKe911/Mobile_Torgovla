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

import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_Login;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_Login extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<ListAdapterSimple_Login> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_Login> filterList;
    public String PEREM_ISNAME_SPINNER;
    public SharedPreferences sp;
    public SharedPreferences.Editor ed;
    public Context context_Activity;
    public TextView name, name_image, name_type;

    public ListAdapterAde_Login(Context ctx, ArrayList<ListAdapterSimple_Login> login) {
        // TODO Auto-generated constructor stub
        this.context = ctx;
        this.objects = login;
        this.filterList = login;
        sp = PreferenceManager.getDefaultSharedPreferences(ctx);
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

        PEREM_ISNAME_SPINNER = sp.getString("PEREM_ISNAME_SPINNER", "0");

        //чтение данных: имя сервера
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            switch (PEREM_ISNAME_SPINNER) {
                case "AITORIZ_ACTIVITY":
                    convertView = inflater.inflate(R.layout.mt_list_avtor, null);
                    break;
                case "OSTATOK_ACTIVITY":
                    convertView = inflater.inflate(R.layout.mt_list_spinner_ostatok, null);
                    break;
                default:
                    convertView = inflater.inflate(R.layout.mt_list_avtor, null);
                    break;
            }
            // convertView = inflater.inflate(R.layout.mt_list_avtor, null);
        }

        switch (PEREM_ISNAME_SPINNER)
        {
            case "AITORIZ_ACTIVITY":
                name = convertView.findViewById(R.id.textView_login);
                name_image =  convertView.findViewById(R.id.tvw_sp_name_image);
                name.setText(objects.get(pos).getName());
                name_image.setText(objects.get(pos).getImage());
                break;
            case "OSTATOK_ACTIVITY":
                name = convertView.findViewById(R.id.textView_login);
                name_image =  convertView.findViewById(R.id.tvw_sp_name_image);
                name_type =  convertView.findViewById(R.id.textView_type_spinner);
                name.setText(objects.get(pos).getName());
                name_image.setText(objects.get(pos).getImage());
                name_type.setText(objects.get(pos).getName_type());
                break;
            default:

                break;
        }

        //  ImageView image = (ImageView) convertView.findViewById(R.id.imageView_login);

        //SET DATA TO THEM


        // image.setImageResource(objects.get(pos).getImage());

       /* Uri imagePath_phone = Uri.parse("android.resource://kg.roman.Mobile_Torgovla/drawable/" + objects.get(pos).getImage());

        Picasso.get() //передаем контекст приложения
                .load(imagePath_phone)
                .into(image); //ссылка на ImageView*/


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
                ArrayList<ListAdapterSimple_Login> filters = new ArrayList<ListAdapterSimple_Login>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_Login p = new ListAdapterSimple_Login(filterList.get(i).getName(),
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
            objects = (ArrayList<ListAdapterSimple_Login>) results.values;
            notifyDataSetChanged();
        }
    }
}