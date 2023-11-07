package kg.roman.Mobile_Torgovla.ListAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_WJ_Otchet;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_WJ_Otchet extends BaseAdapter implements Filterable {

    Context context;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_WJ_Otchet> objects;
    ArrayList<ListAdapterSimple_WJ_Otchet> filterList;
    public SharedPreferences sp;

    public ListAdapterAde_WJ_Otchet(Context ctx, ArrayList<ListAdapterSimple_WJ_Otchet> klients) {
        // TODO Auto-generated constructor stub
        this.context = ctx;
        this.objects = klients;
        this.filterList = klients;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return objects.size();
    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        if (this.filter == null) {
            this.filter = new CustomFilter();
        }
        return this.filter;
    }

    @Override
    public Object getItem(int pos) {
        // TODO Auto-generated method stub
        return objects.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        return this.objects.indexOf(getItem(pos));
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_wj_otchet_prodj, null);
        }

        TextView Brends = (TextView) convertView.findViewById(R.id.tvw_brend);
        TextView Kol_vo = (TextView) convertView.findViewById(R.id.tvw_text_aks_kol);
        TextView Summa = (TextView) convertView.findViewById(R.id.tvw_text_aks_summa);
        TextView Itogo = (TextView) convertView.findViewById(R.id.tvw_list_otch_itogo);

        Brends.setText(objects.get(pos).getBrends());
        Kol_vo.setText(objects.get(pos).getKol_vo());
        Summa.setText(objects.get(pos).getSumma());
        Itogo.setText(objects.get(pos).getCena());

        return convertView;
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
                ArrayList<ListAdapterSimple_WJ_Otchet> filters = new ArrayList<ListAdapterSimple_WJ_Otchet>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_WJ_Otchet p = new ListAdapterSimple_WJ_Otchet(
                                filterList.get(i).getBrends(),
                                filterList.get(i).getKol_vo(),
                                filterList.get(i).getSumma(),
                                filterList.get(i).getCena());
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
            objects = (ArrayList<ListAdapterSimple_WJ_Otchet>) results.values;
            notifyDataSetChanged();
        }
    }
}