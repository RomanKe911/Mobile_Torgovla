package kg.roman.Mobile_Torgovla.ListAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_AKS_Add;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Agent;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_AKS_ADD extends BaseAdapter implements Filterable {

    public SharedPreferences sp;
    Context context;
    ArrayList<ListAdapterSimple_AKS_Add> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_AKS_Add> filterList;
    private static final String TAG = "MyApp";


    public ListAdapterAde_AKS_ADD(Context ctx, ArrayList<ListAdapterSimple_AKS_Add> adapterSimples_data) {
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
            convertView = inflater.inflate(R.layout.layout_adapter_aks_add, null);
        }
        TextView brend = (TextView) convertView.findViewById(R.id.tvw_aks_brend);
        TextView name = (TextView) convertView.findViewById(R.id.tvw_aks_name);
        TextView pref = (TextView) convertView.findViewById(R.id.tvw_aks_pref);
        brend.setText(objects.get(pos).getBrend());
        name.setText(objects.get(pos).getName_aks());
        pref.setText(objects.get(pos).getCatigory());
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
                ArrayList<ListAdapterSimple_AKS_Add> filters = new ArrayList<ListAdapterSimple_AKS_Add>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getBrend().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_AKS_Add p = new ListAdapterSimple_AKS_Add(filterList.get(i).getBrend(),
                                filterList.get(i).getName_aks(),
                                filterList.get(i).getCatigory());
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
            objects = (ArrayList<ListAdapterSimple_AKS_Add>) results.values;
            notifyDataSetChanged();
        }
    }

}