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
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_Agent extends BaseAdapter implements Filterable {

    public SharedPreferences sp;
    Context context;
    ArrayList<ListAdapterSimple_Agent> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_Agent> filterList;
    TextView textView_ostatok_colors;
    private static final String TAG = "MyApp";


    public ListAdapterAde_Agent(Context ctx, ArrayList<ListAdapterSimple_Agent> adapterSimples_data) {
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
            convertView = inflater.inflate(R.layout.list_spr_agents, null);
        }
        TextView name = (TextView) convertView.findViewById(R.id.tvw_list_ag_name);
        TextView status = (TextView) convertView.findViewById(R.id.tvw_list_ag_type);
        TextView uid = (TextView) convertView.findViewById(R.id.tvw_list_ag_uid);
        TextView region = (TextView) convertView.findViewById(R.id.tvw_list_ag_region);
        ImageView IMAGE = (ImageView) convertView.findViewById(R.id.img_agent);



        name.setText(objects.get(pos).getName());
        uid.setText(objects.get(pos).getUid());
        region.setText(objects.get(pos).getRegion());
        status.setText(objects.get(pos).getStatus());
        IMAGE.setImageResource(objects.get(pos).getImage());
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
                ArrayList<ListAdapterSimple_Agent> filters = new ArrayList<ListAdapterSimple_Agent>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_Agent p = new ListAdapterSimple_Agent(filterList.get(i).getName(),
                                filterList.get(i).getUid(),
                                filterList.get(i).getStatus(),
                                filterList.get(i).getImage(),
                                filterList.get(i).getRegion());
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
            objects = (ArrayList<ListAdapterSimple_Agent>) results.values;
            notifyDataSetChanged();
        }
    }

}