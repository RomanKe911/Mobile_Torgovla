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
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Ros_RN;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_Ros_RN extends BaseAdapter implements Filterable {

    public SharedPreferences sp;
    Context context;
    ArrayList<ListAdapterSimple_Ros_RN> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_Ros_RN> filterList;
    TextView textView_ostatok_colors;
    private static final String TAG = "MyApp";


    public ListAdapterAde_Ros_RN(Context ctx, ArrayList<ListAdapterSimple_Ros_RN> adapterSimples_data) {
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
            convertView = inflater.inflate(R.layout.list_ros_torg_day, null);
        }

        TextView rn = (TextView) convertView.findViewById(R.id.tvw_ros_RN);
        TextView vrema = (TextView) convertView.findViewById(R.id.tvw_ros_vrema);
        TextView data = (TextView) convertView.findViewById(R.id.tvw_ros_data);
        TextView summa = (TextView) convertView.findViewById(R.id.tvw_ros_summa);

        rn.setText(objects.get(pos).getRN());
        vrema.setText(objects.get(pos).getVrema());
        data.setText(objects.get(pos).getData());
        summa.setText(objects.get(pos).getSumma());
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
                ArrayList<ListAdapterSimple_Ros_RN> filters = new ArrayList<ListAdapterSimple_Ros_RN>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getRN().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_Ros_RN p = new ListAdapterSimple_Ros_RN(filterList.get(i).getRN(),
                                filterList.get(i).getData(),
                                filterList.get(i).getVrema(),
                                filterList.get(i).getSumma());
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
            objects = (ArrayList<ListAdapterSimple_Ros_RN>) results.values;
            notifyDataSetChanged();
        }
    }

}