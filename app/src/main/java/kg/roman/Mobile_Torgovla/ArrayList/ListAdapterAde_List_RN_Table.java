package kg.roman.Mobile_Torgovla.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import kg.roman.Mobile_Torgovla.MT_FTP.PreferencesWrite;
import kg.roman.Mobile_Torgovla.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_List_RN_Table extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<ListAdapterSimple_List_RN_Table> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_List_RN_Table> filterList;
    String logeTAG = "AdapterRNTable";


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
        PreferencesWrite preferencesWrite = new PreferencesWrite(context);
        if (convertView == null) {
            //  convertView = inflater.inflate(R.layout.wj_content_rn, null);
            if (preferencesWrite.Setting_FiltersSelectGroup) {

                convertView = inflater.inflate(R.layout.wj_content_rn_group, null);
                Group(convertView, pos);
            } else {
                convertView = inflater.inflate(R.layout.wj_content_rn032024, null);
                GroupNull(convertView, pos);
            }
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

    protected void GroupNull(View convertView, int pos) {

        TextView kodrn = convertView.findViewById(R.id.tvw_rn_RN);
        TextView k_agent = convertView.findViewById(R.id.tvw_rn_K_Agent);
        TextView k_agentUID = convertView.findViewById(R.id.tvw_rn_K_UID);
        TextView vrema = convertView.findViewById(R.id.tvw_rn_Vrema);
        TextView data = convertView.findViewById(R.id.tvw_rn_Data);
        TextView summa = convertView.findViewById(R.id.tvw_rn_Summa);
        TextView itogo = convertView.findViewById(R.id.tvw_rn_Itogo);
        TextView adress = convertView.findViewById(R.id.tvw_rn_Adress);
        TextView status_rn = convertView.findViewById(R.id.text_status);
        TextView sklad = convertView.findViewById(R.id.tvw_rn_sklad);
        TextView debet = convertView.findViewById(R.id.tvw_rn_debet);
        TextView skidka = convertView.findViewById(R.id.tvw_rn_skidka);

        TextView statusVisible = convertView.findViewById(R.id.tvw_status_Invoice);
        ImageView img_status = convertView.findViewById(R.id.image_status_zakaza);

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

        if (StatusRN(kodrn.getText().toString()))
            statusVisible.setVisibility(View.VISIBLE);
        else statusVisible.setVisibility(View.GONE);

        // Дебеторская задолжность
        try {
            PreferencesWrite preferencesWrite = new PreferencesWrite(context);
            preferencesWrite = new PreferencesWrite(context);
            SQLiteDatabase db = context.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
            String query = "SELECT * FROM otchet_debet " +
                    "WHERE d_kontr_uid = '" + k_agentUID.getText().toString() + "' AND d_summa > 0;";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                String d_summa = cursor.getString(cursor.getColumnIndexOrThrow("d_summa")).replace(",", ".");
                debet.setText(new DecimalFormat("#00.00").format(Double.parseDouble(d_summa)).replace(",", "."));
            } else
                debet.setText("не верный формат");
            cursor.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(context, "Ошибка дебеторской задолжности", Toast.LENGTH_SHORT).show();
            Log.e(logeTAG, "Ошибка дебеторской задолжности");
        }
    }

    protected void Group(View convertView, int pos) {
        PreferencesWrite preferencesWrite = new PreferencesWrite(context);
        TextView k_agent = convertView.findViewById(R.id.tvwRNGroup_ClientName);
        TextView adress = convertView.findViewById(R.id.tvwRNGroup_ClientAdress);
        TextView startDate = convertView.findViewById(R.id.tvwRNGroup_StartDate);
        TextView endDate = convertView.findViewById(R.id.tvwRNGroup_EndDate);
        TextView debet = convertView.findViewById(R.id.tvwRNGroup_ClietnDebet);
        TextView itogo = convertView.findViewById(R.id.tvwRNGroup_Itogo);

        //SET DATA TO THEM
        k_agent.setText(objects.get(pos).getK_agent());
        adress.setText(objects.get(pos).getAdress());
        if (preferencesWrite.Setting_FiltersSelectDate) {
            startDate.setText(preferencesWrite.Setting_Filters_DataStart);
            endDate.setText(preferencesWrite.Setting_Filters_DataEND);
        } else startDate.setText(objects.get(pos).getData());

        debet.setText("не верный формат");
        itogo.setText(objects.get(pos).getItogo());

        Double ItogoSum = Double.parseDouble(objects.get(pos).getItogo());
        String clientUID = objects.get(pos).getK_agentUID();
        Log.e(logeTAG, "clientUID" + clientUID);

        // Дебеторская задолжность
        try {

            SQLiteDatabase db = context.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
            String query = "SELECT * FROM otchet_debet " +
                    "WHERE d_kontr_uid = '" + clientUID + "' AND d_summa > 0;";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            Log.e(logeTAG, "cursor" + cursor.getCount());
            String d_summa = "";
            if (cursor.getCount() > 0)
                d_summa = cursor.getString(cursor.getColumnIndexOrThrow("d_summa")).replace(",", ".");
            debet.setText(d_summa);
            cursor.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(context, "Ошибка дебеторской задолжности", Toast.LENGTH_SHORT).show();
            Log.e(logeTAG, "Ошибка дебеторской задолжности");
        }
    }

    protected boolean StatusRN(String kodRN) {
        boolean status = false;
        // Отображение счет-фактуры
        try {
            PreferencesWrite preferencesWrite = new PreferencesWrite(context);
            SQLiteDatabase db = context.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
            String query = "SELECT kod_rn, uslov_nds FROM base_RN_All WHERE kod_rn = '" + kodRN + "'";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                String statusNDS = cursor.getString(cursor.getColumnIndexOrThrow("uslov_nds"));
                status = statusNDS.equals("1");
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(context, "Ошибка дебеторской задолжности", Toast.LENGTH_SHORT).show();
            Log.e(logeTAG, "Ошибка дебеторской задолжности");
        }
        return status;
    }

}
