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

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Data;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Debet;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_DB_Data extends BaseAdapter implements Filterable {

    public SharedPreferences sp;
    public String PEREM_FORMA_TYPE_DIALOG;
    Context context;
    ArrayList<ListAdapterSimple_Data> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_Data> filterList;
    TextView textView_ostatok_colors;
    private static final String TAG = "MyApp";


    public ListAdapterAde_DB_Data(Context ctx, ArrayList<ListAdapterSimple_Data> adapterSimples_data) {
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
            convertView = inflater.inflate(R.layout.list_update_date, null);
        }

        sp = PreferenceManager.getDefaultSharedPreferences(context);
        PEREM_FORMA_TYPE_DIALOG = sp.getString("PEREM_FORMA_TYPE_DIALOG", "0");  // Переменая номер накладной

        TextView namedb = (TextView) convertView.findViewById(R.id.tvw_data);
        TextView datadb = (TextView) convertView.findViewById(R.id.tvw_this_data);
        ImageView image = (ImageView) convertView.findViewById(R.id.imageView_pcc);


       // File imagePath_SD = new File(PEREM_SD + objects.get(pos).getImage() + ".png");//путь к изображению

        switch (PEREM_FORMA_TYPE_DIALOG) {
            case "KASSA": {
                Picasso.get() //передаем контекст приложения
                        .load(R.drawable.office_title_money)
                        .into(image); //ссылка на ImageView
            }
            break;
            case "OTCHET": {
                Picasso.get() //передаем контекст приложения
                        .load(R.drawable.office_title_forma)
                        .into(image); //ссылка на ImageView
            }
            break;
            case "SYNC": {
                Picasso.get() //передаем контекст приложения
                        .load(R.drawable.icons_ftp_up)
                        .into(image); //ссылка на ImageView
            }
            break;
            case "UPDATE": {
                Picasso.get() //передаем контекст приложения
                        .load(R.drawable.office_title_forma_update)
                        .into(image); //ссылка на ImageView
            }
            break;
            case "OSTATOK": {
                Picasso.get() //передаем контекст приложения
                        .load(R.drawable.office_title_forma_2)
                        .into(image); //ссылка на ImageView
            }
            break;
            default:
            {
                Picasso.get() //передаем контекст приложения
                        .load(R.drawable.wj_office_dowldb)
                        .into(image); //ссылка на ImageView
            }
                break;
        }

        //SET DATA TO THEM

        namedb.setText(objects.get(pos).getName_up());
        datadb.setText(objects.get(pos).getData_up());


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
                ArrayList<ListAdapterSimple_Data> filters = new ArrayList<ListAdapterSimple_Data>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName_up().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_Data p = new ListAdapterSimple_Data(filterList.get(i).getName_up(),
                                filterList.get(i).getData_up());
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
            objects = (ArrayList<ListAdapterSimple_Data>) results.values;
            notifyDataSetChanged();
        }
    }

}