package kg.roman.Mobile_Torgovla.ArrayList;

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
public class ListAdapterAde_List_Zalaz_Content extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<ListAdapterSimple_List_Zakaz_Content> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_List_Zakaz_Content> filterList;

    public ListAdapterAde_List_Zalaz_Content(Context ctx, ArrayList<ListAdapterSimple_List_Zakaz_Content> List_Content) {
        // TODO Auto-generated constructor stub
        this.context = ctx;
        this.objects = List_Content;
        this.filterList = List_Content;
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
            convertView = inflater.inflate(R.layout.list_zakaz_content, null);
        }

        TextView kod = (TextView) convertView.findViewById(R.id.cont_kod);
        TextView tochka = (TextView) convertView.findViewById(R.id.cont_nameT);
        TextView summa = (TextView) convertView.findViewById(R.id.cont_summa);
        TextView skidka = (TextView) convertView.findViewById(R.id.cont_skidka);
        TextView itogo = (TextView) convertView.findViewById(R.id.cont_itog);


        //SET DATA TO THEM
        kod.setText(objects.get(pos).getKod());
        tochka.setText(objects.get(pos).getTochaka());
        summa.setText(objects.get(pos).getSumma());
        skidka.setText(objects.get(pos).getSkidka());
        itogo.setText(objects.get(pos).getItogo());

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
                ArrayList<ListAdapterSimple_List_Zakaz_Content> filters = new ArrayList<ListAdapterSimple_List_Zakaz_Content>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getKod().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_List_Zakaz_Content p = new ListAdapterSimple_List_Zakaz_Content(filterList.get(i).getKod(),
                                filterList.get(i).getTochaka(),
                                filterList.get(i).getSumma(),
                                filterList.get(i).getSkidka(),
                                filterList.get(i).getItogo());
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
            objects = (ArrayList<ListAdapterSimple_List_Zakaz_Content>) results.values;
            notifyDataSetChanged();
        }
    }
}