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

import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_Login;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_Login extends BaseAdapter {

    Context context;
    ArrayList<ListAdapterSimple_Login> objects;
    ArrayList<ListAdapterSimple_Login> filterList;
    public Context context_Activity;
    public TextView name, name_image;

    public ListAdapterAde_Login(Context ctx, ArrayList<ListAdapterSimple_Login> login) {
        // TODO Auto-generated constructor stub
        this.context = ctx;
        this.objects = login;
        this.filterList = login;
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

        //чтение данных: имя сервера
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.mt_list_avtor, null);

        name = convertView.findViewById(R.id.textView_login);
        name_image =  convertView.findViewById(R.id.tvw_sp_name_image);
        name.setText(objects.get(pos).getName());
        name_image.setText(objects.get(pos).getImage());


        return convertView;
    }

}