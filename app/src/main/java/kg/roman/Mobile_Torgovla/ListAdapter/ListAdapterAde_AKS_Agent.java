package kg.roman.Mobile_Torgovla.ListAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Agent;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Aks_Agent;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_AKS_Agent extends BaseAdapter implements Filterable {

    public SharedPreferences sp;
    Context context;
    ArrayList<ListAdapterSimple_Aks_Agent> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_Aks_Agent> filterList;
    public EditText edit;
    private static final String TAG = "MyApp";


    public ListAdapterAde_AKS_Agent(Context ctx, ArrayList<ListAdapterSimple_Aks_Agent> adapterSimples_data) {
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
    public View getView(final int pos, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_aks_agent, null);
        }
        TextView name = (TextView) convertView.findViewById(R.id.tvw_aks_agent_name);
        TextView par_uslov = (TextView) convertView.findViewById(R.id.tvw_aks_paruslov);
        edit = (EditText) convertView.findViewById(R.id.edit_aks_agent_kol);

        name.setText(objects.get(pos).getAgent());
        par_uslov.setText(objects.get(pos).getAgent());


        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                edit.setText(objects.get(pos).getUsl());
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
                ArrayList<ListAdapterSimple_Aks_Agent> filters = new ArrayList<ListAdapterSimple_Aks_Agent>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getAgent().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_Aks_Agent p = new ListAdapterSimple_Aks_Agent(filterList.get(i).getAgent(),
                                filterList.get(i).getPar_uslov(),
                                filterList.get(i).getUsl());
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
            objects = (ArrayList<ListAdapterSimple_Aks_Agent>) results.values;
            notifyDataSetChanged();
        }
    }

}