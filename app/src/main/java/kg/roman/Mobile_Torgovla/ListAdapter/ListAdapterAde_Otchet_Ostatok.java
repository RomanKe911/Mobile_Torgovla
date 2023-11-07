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

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Otchet_Ostatok;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_Otchet_Ostatok extends BaseAdapter implements Filterable {

    public SharedPreferences sp;
    public String PEREM_K_AG_KodRN, PEREM_SD, PEREM_PHONE;
    Context context;
    ArrayList<ListAdapterSimple_Otchet_Ostatok> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_Otchet_Ostatok> filterList;
    ListAdapterAde_Otchet_Ostatok adapter = this;
    TextView textView_ostatok_colors;
    private boolean isImageScaled = false;
    public Uri imagePath_all;
    public ImageView image;
    private static final String TAG = "MyApp";

    private int size = 0;


    public ListAdapterAde_Otchet_Ostatok(Context ctx, ArrayList<ListAdapterSimple_Otchet_Ostatok> product_str) {
        // TODO Auto-generated constructor stub
        this.context = ctx;
        this.objects = product_str;
        this.filterList = product_str;
        size = objects.size();
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
            convertView = inflater.inflate(R.layout.listview_ostatok_golovnoy, null);
        }

        sp = PreferenceManager.getDefaultSharedPreferences(context);
        PEREM_K_AG_KodRN = sp.getString("PEREM_K_AG_KodRN", "0");  // Переменая номер накладной
        PEREM_SD = sp.getString("PEREM_IMAGE_PUT_SDCARD", "0");
        PEREM_PHONE = sp.getString("PEREM_IMAGE_PUT_PHONE", "0");

        TextView name = (TextView) convertView.findViewById(R.id.tvw_G_ost_name);
        TextView brend = (TextView) convertView.findViewById(R.id.tvw_G_ost_brends_count);
        TextView group = (TextView) convertView.findViewById(R.id.tvw_G_ost_group_count);
        TextView sklad = (TextView) convertView.findViewById(R.id.tvw_G_ost_sklad_count);
        TextView cena = (TextView) convertView.findViewById(R.id.tvw_G_ost_cena_count);
        TextView count = (TextView) convertView.findViewById(R.id.tvw_G_ost_ostatok_count);
        TextView kolbox = (TextView) convertView.findViewById(R.id.tvw_G_ost_kolbox_count);
        TextView kolbox_del = (TextView) convertView.findViewById(R.id.tvw_G_ost_kolbox_box);

        ImageView image = (ImageView) convertView.findViewById(R.id.imageView_ost);

        //  Код добавления пустых строк
        if (name.length() <= 40) {
            name.setLines(1);
            name.setText(objects.get(pos).getName());
            Log.e("LINES1", "|" + name.length() + "|" + name.getText().toString());
        } else if ((name.length()>=40) && (name.length() <= 65)) {
            name.setLines(2);
            name.setText(objects.get(pos).getName());
            Log.e("LINES2", "|" + name.length());
        } else if (name.length() >= 65) {
            name.setLines(3);
            name.setText(objects.get(pos).getName());
            Log.e("LINES3", "|" + name.length());
        } else name.setLines(4);


        brend.setText(objects.get(pos).getBrend());
        group.setText(objects.get(pos).getGroup());
        sklad.setText(objects.get(pos).getSklad());
        cena.setText(objects.get(pos).getCena());
        count.setText(objects.get(pos).getCount());
        kolbox.setText(objects.get(pos).getKolbox());
        kolbox_del.setText(objects.get(pos).getKolbox_Delete());


        try {
            //  Uri uri_path = Uri.parse(context.getPackageName()+"/drawable/"+objects.get(pos).getImage());

            //  File imagePath_SD2 = new File("/sdcard/Price/");//путь к изображению
            //File imagePath_phone = new File(Uri.parse(context.getPackageName()+"/drawable/"+objects.get(pos).getImage()).getPath());//путь к изображению
            // Uri imagePath_phone_base_1 = Uri.parse("android.resource://kg.price_list.roman_kerkin.sunbell_price/drawable/" + objects.get(pos).getImage());
            // Uri imagePath_phone_base_2 = Uri.parse("android.resource://kg.sunbell.roman.sunbell_the_first_solution/drawable/" + objects.get(pos).getImage());

            File imagePath_SD = new File(PEREM_SD + objects.get(pos).getImage() + ".png");//путь к изображению
            Uri imagePath_phone_base_1 = Uri.parse(PEREM_PHONE + objects.get(pos).getImage());
            imagePath_all = Uri.parse(PEREM_PHONE + objects.get(pos).getImage());

            if (imagePath_SD.exists()) {
                Picasso.get() //передаем контекст приложения
                        .load(imagePath_SD)
                        .into(image); //ссылка на ImageView
            } else {
                Picasso.get() //передаем контекст приложения
                        .load(imagePath_phone_base_1)
                        .into(image); //ссылка на ImageView
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
                ArrayList<ListAdapterSimple_Otchet_Ostatok> filters = new ArrayList<ListAdapterSimple_Otchet_Ostatok>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_Otchet_Ostatok p = new ListAdapterSimple_Otchet_Ostatok(filterList.get(i).getName(),
                                filterList.get(i).getBrend(),
                                filterList.get(i).getGroup(),
                                filterList.get(i).getSklad(),
                                filterList.get(i).getCena(),
                                filterList.get(i).getCount(),
                                filterList.get(i).getKolbox(),
                                filterList.get(i).getKolbox_Delete(),
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

            objects = (ArrayList<ListAdapterSimple_Otchet_Ostatok>) results.values;
            notifyDataSetChanged();
        }
    }

}