package kg.roman.Mobile_Torgovla.ListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Spinner_Filter;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Spinner_TY;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_Spinner_TY extends BaseAdapter implements Filterable {
    Context context;
    ArrayList<ListAdapterSimple_Spinner_TY> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_Spinner_TY> filterList;

    public ListAdapterAde_Spinner_TY(Context ctx, ArrayList<ListAdapterSimple_Spinner_TY> adapterSimples_data) {
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
            convertView = inflater.inflate(R.layout.mt_list_spinner_ty, null);
        }
        TextView title_1 = (TextView) convertView.findViewById(R.id.tvw_ty_title1);
        TextView title_2 = (TextView) convertView.findViewById(R.id.tvw_ty_title2);
        TextView title_3 = (TextView) convertView.findViewById(R.id.tvw_ty_title3);
        TextView title_4 = (TextView) convertView.findViewById(R.id.tvw_ty_title4);
        TextView skidka = (TextView) convertView.findViewById(R.id.tvw_ty_skidka);
        TextView summa = (TextView) convertView.findViewById(R.id.tvw_ty_summa);

        skidka.setText(objects.get(pos).getSkidka());
        summa.setText(objects.get(pos).getSumma());
        title_1.setText(objects.get(pos).getTitle_1());
        title_2.setText(objects.get(pos).getTitle_2());
        title_3.setText(objects.get(pos).getTitle_3());
        title_4.setText(objects.get(pos).getTitle_4());

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
                ArrayList<ListAdapterSimple_Spinner_TY> filters = new ArrayList<ListAdapterSimple_Spinner_TY>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getSkidka().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_Spinner_TY p = new ListAdapterSimple_Spinner_TY(filterList.get(i).getSkidka(),
                                filterList.get(i).getSumma(),
                                filterList.get(i).getTitle_1(),
                                filterList.get(i).getTitle_2(),
                                filterList.get(i).getTitle_3(),
                                filterList.get(i).getTitle_4());
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
            objects = (ArrayList<ListAdapterSimple_Spinner_TY>) results.values;
            notifyDataSetChanged();
        }
    }

}