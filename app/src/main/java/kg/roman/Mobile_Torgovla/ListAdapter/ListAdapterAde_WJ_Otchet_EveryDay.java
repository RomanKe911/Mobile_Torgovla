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
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_WJ_Otchet_EveryDay;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_WJ_Otchet_EveryDay extends BaseAdapter implements Filterable {

    Context context;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_WJ_Otchet_EveryDay> objects;
    ArrayList<ListAdapterSimple_WJ_Otchet_EveryDay> filterList;
    public SharedPreferences sp;

    public ListAdapterAde_WJ_Otchet_EveryDay(Context ctx, ArrayList<ListAdapterSimple_WJ_Otchet_EveryDay> klients) {
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
            convertView = inflater.inflate(R.layout.list_report_everyday, null);
        }

        TextView KodRn = (TextView) convertView.findViewById(R.id.text_report_kodrn);
        TextView Name = (TextView) convertView.findViewById(R.id.text_report_name);
        TextView Name_uid = (TextView) convertView.findViewById(R.id.text_report_uid_clients);
        TextView Adress = (TextView) convertView.findViewById(R.id.text_report_adress);
        TextView Summa = (TextView) convertView.findViewById(R.id.text_report_summa);
        TextView Skidka = (TextView) convertView.findViewById(R.id.text_report_skidka);
        TextView Itogo = (TextView) convertView.findViewById(R.id.text_report_itogo);
        TextView Credit= (TextView) convertView.findViewById(R.id.text_report_type_credit);

        KodRn.setText(objects.get(pos).getKod_rn());
        Name.setText(objects.get(pos).getK_agn_name());
        Name_uid.setText(objects.get(pos).getK_agn_name());
        Adress.setText(objects.get(pos).getK_agn_adress());
        Summa.setText(objects.get(pos).getSumma());
        Skidka.setText(objects.get(pos).getSkidka());
        Itogo.setText(objects.get(pos).getItogo());
        Credit.setText(objects.get(pos).getCredit());

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
                ArrayList<ListAdapterSimple_WJ_Otchet_EveryDay> filters = new ArrayList<ListAdapterSimple_WJ_Otchet_EveryDay>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getKod_rn().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_WJ_Otchet_EveryDay p = new ListAdapterSimple_WJ_Otchet_EveryDay(
                                filterList.get(i).getKod_rn(),
                                filterList.get(i).getK_agn_name(),
                                filterList.get(i).getK_agn_uid(),
                                filterList.get(i).getK_agn_adress(),
                                filterList.get(i).getSumma(),
                                filterList.get(i).getSkidka(),
                                filterList.get(i).getItogo(),
                                filterList.get(i).getCredit());
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
            objects = (ArrayList<ListAdapterSimple_WJ_Otchet_EveryDay>) results.values;
            notifyDataSetChanged();
        }
    }
}