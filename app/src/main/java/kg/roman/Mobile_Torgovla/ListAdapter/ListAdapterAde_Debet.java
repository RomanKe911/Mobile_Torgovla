package kg.roman.Mobile_Torgovla.ListAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Debet;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_Debet extends BaseAdapter implements Filterable {

    public SharedPreferences sp;
    Context context;
    ArrayList<ListAdapterSimple_Debet> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_Debet> filterList;
    TextView textView_ostatok_colors;
    private static final String TAG = "MyApp";


    public ListAdapterAde_Debet(Context ctx, ArrayList<ListAdapterSimple_Debet> adapterSimples_debet) {
        // TODO Auto-generated constructor stub
        this.context = ctx;
        this.objects = adapterSimples_debet;
        this.filterList = adapterSimples_debet;
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
        sp = PreferenceManager.getDefaultSharedPreferences(context);


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            if (sp.getString("PEREM_DEBET_CLIENT", "0").equals("ONE"))
            {
                convertView = inflater.inflate(R.layout.wj_list_debet, null);
            } else
            {
                convertView = inflater.inflate(R.layout.wj_list_debet_all, null);
            }

        }
        TextView client = (TextView) convertView.findViewById(R.id.tvw_G_ost_name);
        TextView client_uid = (TextView) convertView.findViewById(R.id.tvw_d_region_uid);
        TextView debet = (TextView) convertView.findViewById(R.id.tvw_d_summa_debet);
        //SET DATA TO THEM
        client.setText(objects.get(pos).getK_agetn());
        client_uid.setText(objects.get(pos).getUid_k_agent());
        debet.setText(objects.get(pos).getDebet());

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
                ArrayList<ListAdapterSimple_Debet> filters = new ArrayList<ListAdapterSimple_Debet>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getUid_k_agent().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_Debet p = new ListAdapterSimple_Debet(filterList.get(i).getUid_k_agent(),
                                filterList.get(i).getK_agetn(),
                                filterList.get(i).getDebet(),
                                filterList.get(i).getAgent());
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
            objects = (ArrayList<ListAdapterSimple_Debet>) results.values;
            notifyDataSetChanged();
        }
    }

}