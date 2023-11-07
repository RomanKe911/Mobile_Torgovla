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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Agent;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Dialog_Forma;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_Dialog_Forma extends BaseAdapter implements Filterable {


    Context context;
    ArrayList<ListAdapterSimple_Dialog_Forma> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_Dialog_Forma> filterList;

    public ListAdapterAde_Dialog_Forma(Context ctx, ArrayList<ListAdapterSimple_Dialog_Forma> adapterSimples_data) {
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
            convertView = inflater.inflate(R.layout.wj_list_zakaz_dialog, null);
        }
        TextView name = (TextView) convertView.findViewById(R.id.dialog_text_edit);
        ImageView image = (ImageView) convertView.findViewById(R.id.dialog_image_edit);

        name.setText(objects.get(pos).getName());

        switch (objects.get(pos).getName()) {
            case "Просмотр": {
                Picasso.get()
                        .load(R.drawable.dialog_screen)
                        // .placeholder(R.drawable.button_up) заглушку (placeholder)
                        .error(R.drawable.no_image) //заглушку для ошибки
                        //   .fit()
                        .into(image);
            }
            break;
            case "Редактировать": {
                Picasso.get()
                        .load(R.drawable.button_refresh)
                        // .placeholder(R.drawable.button_up) заглушку (placeholder)
                        .error(R.drawable.no_image) //заглушку для ошибки
                        //   .fit()
                        .into(image);
            }
            break;
            case "Удалить": {
                Picasso.get()
                        .load(R.drawable.button_close)
                        // .placeholder(R.drawable.button_up) заглушку (placeholder)
                        .error(R.drawable.no_image) //заглушку для ошибки
                        //   .fit()
                        .into(image);
            }
            break;
        }


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
                ArrayList<ListAdapterSimple_Dialog_Forma> filters = new ArrayList<ListAdapterSimple_Dialog_Forma>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_Dialog_Forma p = new ListAdapterSimple_Dialog_Forma(filterList.get(i).getName(),
                                filterList.get(i).getImage());
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
            objects = (ArrayList<ListAdapterSimple_Dialog_Forma>) results.values;
            notifyDataSetChanged();
        }
    }

}