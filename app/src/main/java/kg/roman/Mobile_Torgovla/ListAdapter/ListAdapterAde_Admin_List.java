package kg.roman.Mobile_Torgovla.ListAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Admin_List;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Agent;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.TEST.ListViewItemDTO;
import kg.roman.Mobile_Torgovla.TEST.ListViewItemViewHolder;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_Admin_List extends BaseAdapter implements Filterable {

    public SharedPreferences sp;
    Context context;
    ArrayList<ListAdapterSimple_Admin_List> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_Admin_List> filterList;
    public boolean[] checkBoxState;

    public ListAdapterAde_Admin_List(Context ctx, ArrayList<ListAdapterSimple_Admin_List> adapterSimples_data) {
        // TODO Auto-generated constructor stub
        this.context = ctx;
        this.objects = adapterSimples_data;
        this.filterList = adapterSimples_data;

        checkBoxState = new boolean[objects.size()];
        Log.e("Size=", "" + objects.size());
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        int ret = 0;
        if (objects != null) {
            ret = objects.size();
        }
        //  return objects.size();
        return ret;
    }

    @Override
    public Object getItem(int pos) {
        // TODO Auto-generated method stub
        //         return objects.get(pos);
        Object ret = null;
        if (objects != null) {
            ret = objects.get(pos);
        }
        return ret;
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        //  return objects.indexOf(getItem(pos));
        return pos;
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ListViewItemViewHolder viewHolder = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.test_admin_list, null);

            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            TextView brends = (TextView) convertView.findViewById(R.id.text_brends);
            TextView kod = (TextView) convertView.findViewById(R.id.text_kod);

            viewHolder = new ListViewItemViewHolder(convertView);
            viewHolder.setItemCheckbox(checkBox);
            viewHolder.setItemTextView(brends);
            viewHolder.setItemTextView_KOD(kod);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ListViewItemViewHolder) convertView.getTag();

        ListAdapterSimple_Admin_List listViewItemDto = objects.get(pos);
        viewHolder.getItemTextView().setText(listViewItemDto.getName());
        viewHolder.getItemTextView_KOD().setText(listViewItemDto.getKod());

        //viewHolder.getItemCheckbox().setChecked(listViewItemDto.isChecked());
        viewHolder.getItemCheckbox().setChecked(checkBoxState[pos]);
        viewHolder.getItemCheckbox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked())
                    checkBoxState[pos] = true;
                else
                    checkBoxState[pos] = false;
            }
        });


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
                ArrayList<ListAdapterSimple_Admin_List> filters = new ArrayList<ListAdapterSimple_Admin_List>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_Admin_List p = new ListAdapterSimple_Admin_List(filterList.get(i).getName(),
                                filterList.get(i).getKod(),
                                filterList.get(i).getBrends_bool());
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
            objects = (ArrayList<ListAdapterSimple_Admin_List>) results.values;
            notifyDataSetChanged();
        }
    }

}