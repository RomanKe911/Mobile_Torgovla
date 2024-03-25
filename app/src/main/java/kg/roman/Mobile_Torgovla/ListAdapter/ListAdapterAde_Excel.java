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

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Excel;
import kg.roman.Mobile_Torgovla.R;

public class ListAdapterAde_Excel extends BaseAdapter
        implements Filterable {
    Context context;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_Excel> filterList;
    ArrayList<ListAdapterSimple_Excel> objects;

    public ListAdapterAde_Excel(Context paramContext, ArrayList<ListAdapterSimple_Excel> paramArrayList) {
        this.context = paramContext;
        this.objects = paramArrayList;
        this.filterList = paramArrayList;
    }

    public int getCount() {
        return objects.size();
    }

    public Filter getFilter() {
        if (this.filter == null)
            this.filter = new CustomFilter();
        return this.filter;
    }

    public Object getItem(int paramInt) {
        return objects.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return this.objects.indexOf(getItem(paramInt));
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        LayoutInflater localLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (paramView == null)
            paramView = localLayoutInflater.inflate(R.layout.list_edit_content, null);
      /*TextView localTextView1 = (TextView)paramView.findViewById(2131624127);
        TextView localTextView2 = (TextView)paramView.findViewById(2131624197);
        TextView localTextView3 = (TextView)paramView.findViewById(2131624198);
        TextView localTextView4 = (TextView)paramView.findViewById(2131624199);*/

        TextView localTextView1 = (TextView) paramView.findViewById(R.id.wj_cont_kod);
        TextView localTextView2 = (TextView) paramView.findViewById(R.id.Content_TovarUID);
        TextView localTextView3 = (TextView) paramView.findViewById(R.id.Content_Count);
        TextView localTextView4 = (TextView) paramView.findViewById(R.id.Content_Price);


        localTextView1.setText(((ListAdapterSimple_Excel) this.objects.get(paramInt)).getKod());
        localTextView2.setText(((ListAdapterSimple_Excel) this.objects.get(paramInt)).getName());
        localTextView3.setText(((ListAdapterSimple_Excel) this.objects.get(paramInt)).getKol().intValue());
        localTextView4.setText(((ListAdapterSimple_Excel) this.objects.get(paramInt)).getCena().toString());
        return paramView;
    }

    class CustomFilter extends Filter {
        CustomFilter() {
        }

        protected FilterResults performFiltering(CharSequence paramCharSequence) {
            FilterResults localFilterResults = new FilterResults();
            if ((paramCharSequence != null) && (paramCharSequence.length() > 0)) {
                String str = paramCharSequence.toString().toUpperCase();
                ArrayList localArrayList = new ArrayList();
                for (int i = 0; i < ListAdapterAde_Excel.this.filterList.size(); i++) {
                    if (!((ListAdapterSimple_Excel) ListAdapterAde_Excel.this.filterList.get(i)).getName().toUpperCase().contains(str))
                        continue;
                    localArrayList.add(new ListAdapterSimple_Excel(((ListAdapterSimple_Excel) ListAdapterAde_Excel.this.filterList.get(i)).getKod(),
                            ((ListAdapterSimple_Excel) ListAdapterAde_Excel.this.filterList.get(i)).getName(),
                            ((ListAdapterSimple_Excel) ListAdapterAde_Excel.this.filterList.get(i)).getCena(),
                            ((ListAdapterSimple_Excel) ListAdapterAde_Excel.this.filterList.get(i)).getKol()));
                }
                localFilterResults.count = localArrayList.size();
                localFilterResults.values = localArrayList;
                return localFilterResults;
            }
            localFilterResults.count = ListAdapterAde_Excel.this.filterList.size();
            localFilterResults.values = ListAdapterAde_Excel.this.filterList;
            return localFilterResults;
        }

        protected void publishResults(CharSequence paramCharSequence, FilterResults paramFilterResults) {
            ListAdapterAde_Excel.this.objects = ((ArrayList) paramFilterResults.values);
            ListAdapterAde_Excel.this.notifyDataSetChanged();
        }
    }
}