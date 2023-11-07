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

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Agent;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Spinner_Filter;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_Spinner_Filter extends BaseAdapter implements Filterable {
    Context context;
    ArrayList<ListAdapterSimple_Spinner_Filter> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_Spinner_Filter> filterList;

    public ListAdapterAde_Spinner_Filter(Context ctx, ArrayList<ListAdapterSimple_Spinner_Filter> adapterSimples_data) {
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
            convertView = inflater.inflate(R.layout.mt_list_spinner_filter, null);
        }
        TextView name = (TextView) convertView.findViewById(R.id.tvw_filter_name);
        TextView adress = (TextView) convertView.findViewById(R.id.tvw_filter_adress);
        TextView uid = (TextView) convertView.findViewById(R.id.tvw_filter_uid);
        uid.setVisibility(View.GONE);

        name.setText(objects.get(pos).getName());
        adress.setText(objects.get(pos).getAdress());
        uid.setText(objects.get(pos).getUid());
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
                ArrayList<ListAdapterSimple_Spinner_Filter> filters = new ArrayList<ListAdapterSimple_Spinner_Filter>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_Spinner_Filter p = new ListAdapterSimple_Spinner_Filter(filterList.get(i).getName(),
                                filterList.get(i).getAdress(),
                                filterList.get(i).getUid());
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
            objects = (ArrayList<ListAdapterSimple_Spinner_Filter>) results.values;
            notifyDataSetChanged();
        }
    }

}