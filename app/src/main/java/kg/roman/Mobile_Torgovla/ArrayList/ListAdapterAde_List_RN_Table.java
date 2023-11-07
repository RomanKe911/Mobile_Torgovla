package kg.roman.Mobile_Torgovla.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.constraintlayout.widget.ConstraintLayout;
import kg.roman.Mobile_Torgovla.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_List_RN_Table extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<ListAdapterSimple_List_RN_Table> objects;
    ListAdapterAde_List_RN_Table adapterPriceClients = this;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_List_RN_Table> filterList;
    public androidx.appcompat.app.AlertDialog.Builder dialog;
    public SharedPreferences sp;
    public SharedPreferences.Editor ed;
    public String PEREM_DB3_RN, PEREM_DB3_CONST, PEREM_DB3_BASE, Log_Text_Error, PEREM_K_AG_Data;


    public ListAdapterAde_List_RN_Table(Context ctx, ArrayList<ListAdapterSimple_List_RN_Table> Edit_Content) {
        // TODO Auto-generated constructor stub
        this.context = ctx;
        this.objects = Edit_Content;
        this.filterList = Edit_Content;
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
            convertView = inflater.inflate(R.layout.wj_content_rn, null);
        }
        Log_Text_Error = "ERR_DELETE_RN: ";
        Constanta_Read();

        final TextView kodrn = (TextView) convertView.findViewById(R.id.tvw_rn_RN);
        TextView k_agent = (TextView) convertView.findViewById(R.id.tvw_rn_K_Agent);
        TextView k_agentUID = (TextView) convertView.findViewById(R.id.tvw_rn_K_UID);
        TextView vrema = (TextView) convertView.findViewById(R.id.tvw_rn_Vrema);
        final TextView data = (TextView) convertView.findViewById(R.id.tvw_rn_Data);
        TextView summa = (TextView) convertView.findViewById(R.id.tvw_rn_Summa);
        TextView itogo = (TextView) convertView.findViewById(R.id.tvw_rn_Itogo);
        TextView adress = (TextView) convertView.findViewById(R.id.tvw_rn_Adress);
        TextView status_rn = (TextView) convertView.findViewById(R.id.text_status);
        TextView sklad = (TextView) convertView.findViewById(R.id.tvw_rn_sklad);
        ConstraintLayout constraintLayout = convertView.findViewById(R.id.list_layout_read);

        final TextView debet = (TextView) convertView.findViewById(R.id.tvw_rn_debet);
        TextView skidka = (TextView) convertView.findViewById(R.id.tvw_rn_skidka);

        ImageView img_status = (ImageView) convertView.findViewById(R.id.image_status_zakaza);

        //   if (PEREM_TYPE_STATUS_WORK.equals(""))

        //SET DATA TO THEM
        kodrn.setText(objects.get(pos).getKodrn());
        k_agent.setText(objects.get(pos).getK_agent());
        k_agentUID.setText(objects.get(pos).getK_agentUID());
        vrema.setText(objects.get(pos).getVrema());
        data.setText(objects.get(pos).getData());
        summa.setText(objects.get(pos).getSumma());
        itogo.setText(objects.get(pos).getItogo());
        adress.setText(objects.get(pos).getAdress());
        skidka.setText(objects.get(pos).getSkidka());
        sklad.setText(objects.get(pos).getSklad());
       /* if (objects.get(pos).getDebet() != null) {
            debet.setText(objects.get(pos).getDebet());
        } else debet.setText("0");*/


        if (objects.get(pos).getStatus() != null) {
            if (objects.get(pos).getStatus().equals("true")) {
                status_rn.setText("true");
                Picasso.get()
                        .load(R.drawable.icons_image_status_zakaza_up)
                        // .placeholder(R.drawable.button_up) заглушку (placeholder)
                        .error(R.drawable.button_close) //заглушку для ошибки
                        //   .fit()
                        .into(img_status);
            } else {
                status_rn.setText("false");
                Picasso.get()
                        .load(R.drawable.icons_image_status_zakaza)
                        // .placeholder(R.drawable.button_up) заглушку (placeholder)
                        .error(R.drawable.button_close) //заглушку для ошибки
                        //   .fit()
                        .into(img_status);
            }
        } else {
            Picasso.get()
                    .load(R.drawable.icons_image_status_zakaza)
                    // .placeholder(R.drawable.button_up) заглушку (placeholder)
                    .error(R.drawable.button_close) //заглушку для ошибки
                    //   .fit()
                    .into(img_status);
        }


        // Загрузка статуса
        try {
         /*   SQLiteDatabase db = context.openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
            String query = "SELECT status FROM base_RN_All WHERE data = '" + objects.get(pos).getData() + "'";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String status = cursor.getString(cursor.getColumnIndex("status"));

                }
                cursor.moveToNext();
            }
            cursor.close();
            db.close();*/
        } catch (Exception e) {
            Toast.makeText(context, "Ошибка ошибка статуса", Toast.LENGTH_SHORT).show();
            Log.e(Log_Text_Error, "Ошибка ошибка статуса");
        }


        // Дебеторская задолжность
        try {
            SQLiteDatabase db = context.openOrCreateDatabase(PEREM_DB3_RN, MODE_PRIVATE, null);
            String query = "SELECT * FROM otchet_debet " +
                    "WHERE d_kontr_uid = '" + k_agentUID.getText().toString() + "' AND d_summa > 0;";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();

            if (cursor.getCount() > 0) {
                String d_summa = cursor.getString(cursor.getColumnIndex("d_summa")).replace(",", ".");
                debet.setText(new DecimalFormat("#00.00").format(Double.parseDouble(d_summa)).replace(",", "."));

            } else {
                debet.setText("не верный формат");
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(context, "Ошибка дебеторской задолжности", Toast.LENGTH_SHORT).show();
            Log.e(Log_Text_Error, "Ошибка дебеторской задолжности");
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
                ArrayList<ListAdapterSimple_List_RN_Table> filters = new ArrayList<ListAdapterSimple_List_RN_Table>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getKodrn().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_List_RN_Table p = new ListAdapterSimple_List_RN_Table(filterList.get(i).getKodrn(),
                                filterList.get(i).getK_agent(),
                                filterList.get(i).getK_agentUID(),
                                filterList.get(i).getVrema(),
                                filterList.get(i).getData(),
                                filterList.get(i).getSumma(),
                                filterList.get(i).getAdress(),
                                filterList.get(i).getItogo(),
                                filterList.get(i).getSkidka(),
                                filterList.get(i).getStatus(),
                                filterList.get(i).getDebet(),
                                filterList.get(i).getSklad());
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
            objects = (ArrayList<ListAdapterSimple_List_RN_Table>) results.values;
            notifyDataSetChanged();
        }
    }

    // Константы для чтения
    protected void Constanta_Read() {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        PEREM_DB3_CONST = sp.getString("PEREM_DB3_CONST", "0");                  //чтение данных: Путь к базам данных с константами
        PEREM_DB3_BASE = sp.getString("PEREM_DB3_BASE", "0");                    //чтение данных: Путь к базам данных с товаром
        PEREM_DB3_RN = sp.getString("PEREM_DB3_RN", "0");                        //чтение данных: Путь к базам данных с накладными
        PEREM_K_AG_Data = sp.getString("PEREM_K_AG_Data", "0");                        //чтение данных: Путь к базам данных с накладными
    }

}
