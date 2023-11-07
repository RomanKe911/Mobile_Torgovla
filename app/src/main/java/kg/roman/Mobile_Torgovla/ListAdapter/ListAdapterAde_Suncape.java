package kg.roman.Mobile_Torgovla.ListAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Suncape;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_Suncape extends BaseAdapter implements Filterable {

    public SharedPreferences sp;
    public String PEREM_K_AG_KodRN, PEREM_SD, PEREM_PHONE;
    Context context;
    ArrayList<ListAdapterSimple_Suncape> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_Suncape> filterList;
    TextView textView_ostatok_colors;
    private static final String TAG = "Suncape";


    public ListAdapterAde_Suncape(Context ctx, ArrayList<ListAdapterSimple_Suncape> product_str) {
        // TODO Auto-generated constructor stub
        this.context = ctx;
        this.objects = product_str;
        this.filterList = product_str;
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
            convertView = inflater.inflate(R.layout.list_item_nomenclature, null);
            sp = PreferenceManager.getDefaultSharedPreferences(context);
            PEREM_K_AG_KodRN = sp.getString("PEREM_K_AG_KodRN", "0");  // Переменая номер накладной
            PEREM_SD = sp.getString("PEREM_IMAGE_PUT_SDCARD", "0");
            PEREM_PHONE = sp.getString("PEREM_IMAGE_PUT_PHONE", "0");
            //    convertView = inflater.inflate(R.layout.list_item_new_3, null);
        }
        TextView name = (TextView) convertView.findViewById(R.id.textView_forma_name);
        TextView kol = (TextView) convertView.findViewById(R.id.textView_forma_kolbox);
        TextView cena = (TextView) convertView.findViewById(R.id.textView_forma_cena);
        TextView cena_nall = (TextView) convertView.findViewById(R.id.textView_forma_cenanal);
        //TextView cena_kons = (TextView) convertView.findViewById(R.id.textView_CenaKons);
        TextView strih = (TextView) convertView.findViewById(R.id.textView_forma_strih);
        ImageView image = (ImageView) convertView.findViewById(R.id.textView_forma_image);
        textView_ostatok_colors = (TextView) convertView.findViewById(R.id.textView_ostatki_all);
        TextView kod_univ = (TextView) convertView.findViewById(R.id.textView_forma_kod);
        //SET DATA TO THEM


        name.setText(objects.get(pos).getName());
        kol.setText(objects.get(pos).getKol());
        cena.setText(objects.get(pos).getCena());
        cena_nall.setText(objects.get(pos).getCena_Nall());
        //  cena_kons.setText(objects.get(pos).getCenaKons());
        strih.setText(objects.get(pos).getStrih());
        textView_ostatok_colors.setText(objects.get(pos).getOstatki());
        kod_univ.setText(objects.get(pos).getKod_univ());
        //  image.setImageResource(objects.get(pos).getImage());

        try {
            /*File imagePath_SD = new File("/sdcard/Price/Image/" + objects.get(pos).getImage() + ".png");//путь к изображению
            Uri imagePath_phone_base_1 = Uri.parse("android.resource://kg.price_list.roman_kerkin.sunbell_price/drawable/" + objects.get(pos).getImage());
            Uri imagePath_phone_base_2 = Uri.parse("android.resource://kg.sunbell.roman.sunbell_the_first_solution/drawable/" + objects.get(pos).getImage());*/
            File imagePath_SD = new File(PEREM_SD + objects.get(pos).getImage() + ".png");//путь к изображению
           // Uri imagePath_phone_base_1 = Uri.parse(PEREM_PHONE + objects.get(pos).getImage());
            Uri imagePath_phone_base_1 = Uri.parse("android.resource://kg.roman.mobile_torgovla_image/drawable/" + objects.get(pos).getImage());
         //   Uri imagePath_phone_base_2 = Uri.parse("android.resource://kg.roman.mobile_torgovla_image/app/main/drawable/" + objects.get(pos).getImage());

            Log.e(TAG, "ПУТЬ: " + PEREM_SD + objects.get(pos).getImage() + ".png");
            Log.e(TAG, "ПУТЬ2: " + "android.resource://kg.roman.mobile_torgovla_image/drawable/" + objects.get(pos).getImage() + ".png");
            if (imagePath_SD.exists()) {
                Picasso.get()
                        .load(imagePath_phone_base_1)
                        // .placeholder(R.drawable.button_up) заглушку (placeholder)
                        .error(R.drawable.no_image) //заглушку для ошибки
                        //   .fit()
                        .into(image);
            } else {
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


        sp = PreferenceManager.getDefaultSharedPreferences(context);
        boolean ostatki_open = sp.getBoolean("switch_preference_1", false);

        if (ostatki_open == true) {

        } else {

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
                ArrayList<ListAdapterSimple_Suncape> filters = new ArrayList<ListAdapterSimple_Suncape>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_Suncape p = new ListAdapterSimple_Suncape(filterList.get(i).getKod_univ(),
                                filterList.get(i).getName(),
                                filterList.get(i).getKol(),
                                filterList.get(i).getCena(),
                                filterList.get(i).getCena_Nall(),
                                filterList.get(i).getStrih(),
                                filterList.get(i).getImage(),
                                filterList.get(i).getOstatki());
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
            objects = (ArrayList<ListAdapterSimple_Suncape>) results.values;
            notifyDataSetChanged();
        }
    }

    protected void TextColorise() {
        Boolean fun, fun1, fun2, fun3;

        String str = textView_ostatok_colors.getText().toString().substring(0, textView_ostatok_colors.getText().toString().length() - 2);
        fun1 = Integer.parseInt(str) < 10;
        fun2 = Integer.parseInt(str) < 50;
        fun3 = Integer.parseInt(str) > 100;

       /* int textColor1 = ContextCompat.getColor(context, R.color.colors_ostatok_small);
        int textColor2 = ContextCompat.getColor(context, R.color.colors_ostatok_medium);
        int textColor3 = ContextCompat.getColor(context, R.color.colors_ostatok_large);*/

       /* int textColor1 = Color.parseColor("FFCA0000");
        int textColor2 = Color.parseColor("FFE7D402");
        int textColor3 = Color.parseColor("FF03A613");*/


        //if (fun1) textView_ostatok_colors.setTextColor(textColor1);

        if (fun1) textView_ostatok_colors.setTextColor(Color.parseColor("#FFCA0000"));

        else if (Integer.parseInt(str) > 10 & Integer.parseInt(str) < 50)
            textView_ostatok_colors.setTextColor(Color.parseColor("#FFE7D402"));

        else if (Integer.parseInt(str) > 50)
            textView_ostatok_colors.setTextColor(Color.parseColor("#FF03A613"));
    }
}