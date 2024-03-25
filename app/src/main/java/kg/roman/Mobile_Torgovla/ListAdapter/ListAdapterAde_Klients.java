package kg.roman.Mobile_Torgovla.ListAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar.ListAdapterSimple_Klients;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_Klients extends BaseAdapter implements Filterable {

    Context context;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_Klients> objects;
    ArrayList<ListAdapterSimple_Klients> filterList;
    public SharedPreferences sp;
    public String PEREM_K_AG_KodRN, PEREM_SD, PEREM_PHONE, PEREM_READ_KODRN, PEREM_TYPE_MENU, PEREM_DB3_RN, PEREM_LIST_ADAPTER_DEBET;

    public ListAdapterAde_Klients(Context ctx, ArrayList<ListAdapterSimple_Klients> klients) {
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
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        PEREM_K_AG_KodRN = sp.getString("PEREM_K_AG_KodRN", "0");  // Переменая номер накладной
        PEREM_SD = sp.getString("PEREM_IMAGE_PUT_SDCARD", "0");
        PEREM_PHONE = sp.getString("PEREM_IMAGE_PUT_PHONE", "0");
        PEREM_READ_KODRN = sp.getString("PEREM_READ_KODRN", "0");
        PEREM_TYPE_MENU = sp.getString("PEREM_TYPE_MENU", "");
        PEREM_DB3_RN = sp.getString("PEREM_DB3_RN", "");
        PEREM_LIST_ADAPTER_DEBET = sp.getString("PEREM_LIST_ADAPTER_DEBET", "");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            if (PEREM_LIST_ADAPTER_DEBET.equals("list_debet"))
            {
                convertView = inflater.inflate(R.layout.mt_list_klients_content, null);


            } else convertView = inflater.inflate(R.layout.mt_list_klients_content_const, null);
        }


        TextView UID = (TextView) convertView.findViewById(R.id.select_client_uid);
        TextView NAME = (TextView) convertView.findViewById(R.id.select_client_name);
        TextView ADRESS = (TextView) convertView.findViewById(R.id.select_client_adress);
        TextView Debet = (TextView) convertView.findViewById(R.id.select_client_credit);
        ImageView IMAGE = (ImageView) convertView.findViewById(R.id.imageView_P_Klients);

        UID.setText(objects.get(pos).getUID());
        NAME.setText(objects.get(pos).getName());
        ADRESS.setText(objects.get(pos).getAdress());
        Debet.setText(objects.get(pos).getUserUID());
        IMAGE.setImageResource(objects.get(pos).getImage());

       /* File imagePath_SD = new File("/sdcard/Price/" + objects.get(pos).getImage() + ".png");//путь к изображению
        Uri imagePath_phone = Uri.parse("android.resource://kg.price_list.roman_kerkin.sunbell_price/drawable/" + objects.get(pos).getImage());
        Picasso.with(context) //передаем контекст приложения
                .load(imagePath_phone)
                .into(image); //ссылка на ImageView*/


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
                ArrayList<ListAdapterSimple_Klients> filters = new ArrayList<ListAdapterSimple_Klients>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_Klients p = new ListAdapterSimple_Klients(
                                filterList.get(i).getName(),
                                filterList.get(i).getUID(),
                                filterList.get(i).getAdress(),
                                filterList.get(i).getImage(),
                                filterList.get(i).getUserUID());
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
            objects = (ArrayList<ListAdapterSimple_Klients>) results.values;
            notifyDataSetChanged();
        }
    }
}