package kg.roman.Mobile_Torgovla.ListAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Agent;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_List_Smart_Voice;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_List_Smart_Voice extends BaseAdapter implements Filterable {

    public SharedPreferences sp;
    public String PEREM_K_AG_KodRN, PEREM_SD, PEREM_PHONE, PEREM_DB3_RN;
    Context context;
    ArrayList<ListAdapterSimple_List_Smart_Voice> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_List_Smart_Voice> filterList;

    public ListAdapterAde_List_Smart_Voice(Context ctx, ArrayList<ListAdapterSimple_List_Smart_Voice> adapterSimples_data) {
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
            convertView = inflater.inflate(R.layout.list_adaper_smart_voice, null);
        }

        sp = PreferenceManager.getDefaultSharedPreferences(context);
        PEREM_K_AG_KodRN = sp.getString("PEREM_K_AG_KodRN", "0");  // Переменая номер накладной
        PEREM_SD = sp.getString("PEREM_IMAGE_PUT_SDCARD", "0");
        PEREM_PHONE = sp.getString("PEREM_IMAGE_PUT_PHONE", "0");
        PEREM_DB3_RN = sp.getString("PEREM_DB3_RN", "0");


        TextView name = (TextView) convertView.findViewById(R.id.textview_smart_name);
        TextView uid = (TextView) convertView.findViewById(R.id.textview_smart_uid);
        TextView univ = (TextView) convertView.findViewById(R.id.textview_smart_univ);
        TextView kolbox = (TextView) convertView.findViewById(R.id.textview_smart_kolbox);
        TextView count = (TextView) convertView.findViewById(R.id.textview_smart_count);
        TextView cena = (TextView) convertView.findViewById(R.id.textview_smart_cena);
        ImageView image = (ImageView) convertView.findViewById(R.id.imageview_smart_image);


        name.setText(objects.get(pos).getName());
        uid.setText(objects.get(pos).getUid());
        univ.setText(objects.get(pos).getUniv());
        kolbox.setText(objects.get(pos).getKolbox());
        count.setText(objects.get(pos).getOstatok());
        cena.setText(objects.get(pos).getCena());

        try {
                /*File imagePath_SD = new File("/sdcard/Price/Image/" + objects.get(pos).getImage() + ".png");//путь к изображению
                Uri imagePath_phone_base_1 = Uri.parse("android.resource://kg.price_list.roman_kerkin.sunbell_price/drawable/" + objects.get(pos).getImage());
                Uri imagePath_phone_base_2 = Uri.parse("android.resource://kg.sunbell.roman.sunbell_the_first_solution/drawable/" + objects.get(pos).getImage());*/

            File imagePath_SD = new File(PEREM_SD + objects.get(pos).getImage() + ".png");//путь к изображению
            Uri imagePath_phone_base_1 = Uri.parse(PEREM_PHONE + objects.get(pos).getImage());
            if(imagePath_SD.exists())
            {
                Picasso.get()
                        .load(imagePath_SD)
                        // .placeholder(R.drawable.button_up) заглушку (placeholder)
                        .error(R.drawable.no_image) //заглушку для ошибки
                        //   .fit()
                        .into(image);
            }
            else {
                Picasso.get()
                        .load(imagePath_phone_base_1)
                        // .placeholder(R.drawable.button_up) заглушку (placeholder)
                        .error(R.drawable.no_image) //заглушку для ошибки
                        //   .fit()
                        .into(image);

                /*    Picasso.with(context)
                            .load(imagePath_phone_base_2)
                            // .placeholder(R.drawable.button_up) заглушку (placeholder)
                          //  .error(R.drawable.no_image) //заглушку для ошибки
                            //   .fit()
                            .into(image);*/

                 /*   Picasso.with(context)
                            .load(imagePath_phone_base_1)
                            .centerCrop()
                            .fit()
                            .placeholder(getResources().getDrawable(R.drawable.placeholder,null))
                            .error(getResources().getDrawable(R.drawable.no_image,null))
                            .rotate(45)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .priority(Picasso.Priority.HIGH)
                            .into(image);*/
            }

        } catch (Exception e) {
            Log.e("Image_Error", "Нет картинов в ресурсах");
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
                ArrayList<ListAdapterSimple_List_Smart_Voice> filters = new ArrayList<ListAdapterSimple_List_Smart_Voice>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_List_Smart_Voice p = new ListAdapterSimple_List_Smart_Voice(filterList.get(i).getName(),
                                filterList.get(i).getUid(),
                                filterList.get(i).getUniv(),
                                filterList.get(i).getKolbox(),
                                filterList.get(i).getOstatok(),
                                filterList.get(i).getCena(),
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
            objects = (ArrayList<ListAdapterSimple_List_Smart_Voice>) results.values;
            notifyDataSetChanged();
        }
    }

}