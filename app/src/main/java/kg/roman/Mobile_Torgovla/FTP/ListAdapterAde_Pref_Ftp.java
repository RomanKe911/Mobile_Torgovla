package kg.roman.Mobile_Torgovla.FTP;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.R;
/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_Pref_Ftp extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<ListAdapterSimple_Pref_Ftp> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_Pref_Ftp> filterList;
    private static final String TAG = "MyApp";


    public ListAdapterAde_Pref_Ftp(Context ctx, ArrayList<ListAdapterSimple_Pref_Ftp> ftp_connect) {
        // TODO Auto-generated constructor stub
        this.context = ctx;
        this.objects = ftp_connect;
        this.filterList = ftp_connect;
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
            convertView = inflater.inflate(R.layout.list_item_pref_ftp, null);
        }
        TextView server = (TextView) convertView.findViewById(R.id.tvw_name_server);
        TextView port = (TextView) convertView.findViewById(R.id.tvw_name_port);
        TextView login = (TextView) convertView.findViewById(R.id.tvw_name_login);
        TextView password = (TextView) convertView.findViewById(R.id.tvw_name_password);
        TextView put = (TextView) convertView.findViewById(R.id.tvw_name_put);

        server.setText(objects.get(pos).getServer());
        port.setText(objects.get(pos).getPort());
        login.setText(objects.get(pos).getLogin());
        password.setText(objects.get(pos).getPassword());
        put.setText(objects.get(pos).getPut());





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
                ArrayList<ListAdapterSimple_Pref_Ftp> filters = new ArrayList<ListAdapterSimple_Pref_Ftp>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getServer().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_Pref_Ftp p = new ListAdapterSimple_Pref_Ftp(filterList.get(i).getServer(),
                                filterList.get(i).getPort(),
                                filterList.get(i).getLogin(),
                                filterList.get(i).getPassword(),
                                filterList.get(i).getPut());
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
            objects = (ArrayList<ListAdapterSimple_Pref_Ftp>) results.values;
            notifyDataSetChanged();
        }
    }

}