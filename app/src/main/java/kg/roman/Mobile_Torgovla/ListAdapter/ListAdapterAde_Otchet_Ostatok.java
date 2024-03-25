package kg.roman.Mobile_Torgovla.ListAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kg.roman.Mobile_Torgovla.MT_FTP.FTPWebhost;
import kg.roman.Mobile_Torgovla.MT_FTP.FtpConnectData;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Otchet_Ostatok;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_Otchet_Ostatok extends BaseAdapter implements Filterable {
    Context context;
    ArrayList<ListAdapterSimple_Otchet_Ostatok> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_Otchet_Ostatok> filterList;

    public ListAdapterAde_Otchet_Ostatok(Context ctx, ArrayList<ListAdapterSimple_Otchet_Ostatok> product_str) {
        // TODO Auto-generated constructor stub
        this.context = ctx;
        this.objects = product_str;
        this.filterList = product_str;
        // size = objects.size();
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
        if (convertView == null)
            convertView = inflater.inflate(R.layout.listview_ostatok_golovnoy, null);

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
        } else if ((name.length() >= 40) && (name.length() <= 65)) {
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

/*
        try {
            FtpConnectData ftpConnectData = new FtpConnectData();
            File newImage_png = new File(ftpConnectData.put_toPhoneImage(context) + objects.get(pos).getImage() + ".png");
            File newImage_jpg = new File(ftpConnectData.put_toPhoneImage(context) + objects.get(pos).getImage() + ".jpg");
            if (newImage_png.isFile())
                Picasso.get() //передаем контекст приложения
                        .load(newImage_png)
                        .error(R.drawable.no_image)
                        .into(image); //ссылка на ImageView
            else
                Picasso.get() //передаем контекст приложения
                        .load(newImage_jpg)
                        .error(R.drawable.no_image)
                        .into(image); //ссылка на ImageView

            Log.e("Image_PUT_PNG", newImage_png + "__" + newImage_png.isFile());
            Log.e("Image_PUT_JPG", newImage_jpg + "__" + newImage_jpg.isFile());

        } catch (Exception e) {
            Log.e("Image_Error", "Нет картинов в ресурсах");
        }
*/

        try {
            FtpConnectData ftpConnectData = new FtpConnectData();
            File newImage_png = new File(ftpConnectData.put_toPhoneImage(context) + objects.get(pos).getImage() + ".png");
            File newImage_jpg = new File(ftpConnectData.put_toPhoneImage(context) + objects.get(pos).getImage() + ".jpg");
            if (newImage_png.isFile())
                Picasso.get() //передаем контекст приложения
                        .load(newImage_png)
                        .error(R.drawable.no_image)
                        .into(image); //ссылка на ImageView*/
            else
                Picasso.get() //передаем контекст приложения
                        .load(newImage_jpg)
                        .error(R.drawable.no_image)
                        .into(image); //ссылка на ImageView*/

            Log.e("Image_PUT_PNG", newImage_png + "__" + newImage_png.isFile());
            Log.e("Image_PUT_JPG", newImage_jpg + "__" + newImage_jpg.isFile());

        } catch (Exception e) {
            Log.e("Image_Error", "Нет картинов в ресурсах");
        }


        return convertView;
    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        if (filter == null)
            filter = new CustomFilter();
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
                ArrayList<ListAdapterSimple_Otchet_Ostatok> filters = new ArrayList<>();
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
            // notifyDataSetChanged();
        }
    }

}