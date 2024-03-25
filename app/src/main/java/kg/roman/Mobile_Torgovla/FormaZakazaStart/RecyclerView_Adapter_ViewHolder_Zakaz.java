package kg.roman.Mobile_Torgovla.FormaZakazaStart;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_List_RN_Table;
import kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar.ListAdapterSimple_Klients;
import kg.roman.Mobile_Torgovla.MT_FTP.PreferencesWrite;
import kg.roman.Mobile_Torgovla.R;

public class RecyclerView_Adapter_ViewHolder_Zakaz extends RecyclerView.Adapter<RecyclerView_Adapter_ViewHolder_Zakaz.ViewHolder> {


    public interface OnStateClickListener {
        void onStateClick(ListAdapterSimple_List_RN_Table clientClick, int position);
    }


    private final LayoutInflater inflater;
    private final List<ListAdapterSimple_List_RN_Table> imagelist;
    List<ListAdapterSimple_List_RN_Table> filterList;
    List<ListAdapterSimple_List_RN_Table> filterListFull;
    List<Integer> imageList = new ArrayList<>();

    private final OnStateClickListener onClickListener;

    String logeTAG = "RecAdapter";
    Context context;

    public RecyclerView_Adapter_ViewHolder_Zakaz(Context context, List<ListAdapterSimple_List_RN_Table> backup_list, OnStateClickListener onClickListener) {
        this.context = context;
        this.imagelist = backup_list;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
        this.filterList = backup_list;
        filterListFull = new ArrayList<>(backup_list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PreferencesWrite preferencesWrite = new PreferencesWrite(context);
        View view;
        if (preferencesWrite.Setting_FiltersSelectGroup)
            view = inflater.inflate(R.layout.wj_content_rn_group, parent, false);
        else
          //    view = inflater.inflate(R.layout.wj_content_rn032024test, parent, false);
              view = inflater.inflate(R.layout.wj_content_allrn, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ListAdapterSimple_List_RN_Table listID = imagelist.get(position);

        PreferencesWrite preferencesWrite = new PreferencesWrite(context);
        if (preferencesWrite.Setting_FiltersSelectGroup)
            Group(holder, listID);
        else
            GroupNull(holder, listID);

        holder.constraintLayoutClick.setOnClickListener(v -> onClickListener.onStateClick(imagelist.get(position), position));

        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Нажата Edit", Toast.LENGTH_SHORT).show();
                Log.e(logeTAG, "Нажата Edit");
            }
        });
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Нажата Delete", Toast.LENGTH_SHORT).show();
                Log.e(logeTAG, "Нажата Delete");
            }
        });

    }

    @Override
    public int getItemCount() {
        return imagelist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView kodrn, client, clientUID, thisDate, thisTime, clientSum, clientItogo, clientAdress, status_rn, sklad, clientDebet, sale, statusVisible;
        TextView grClient, grClientAdress, grClientDebet, grClientItogo, grStartDate, grEndDate;
        ImageView img_status;
        ConstraintLayout constraintLayoutClick;

        Button buttonEdit, buttonDelete;


        ViewHolder(View convertView) {
            super(convertView);
            //// Для данных без групировки
            kodrn = convertView.findViewById(R.id.tvw_rn_RN);
            client = convertView.findViewById(R.id.tvw_rn_K_Agent);
            clientUID = convertView.findViewById(R.id.tvw_rn_K_UID);
            thisTime = convertView.findViewById(R.id.tvw_rn_Vrema);
            thisDate = convertView.findViewById(R.id.tvw_rn_Data);
            clientSum = convertView.findViewById(R.id.tvw_rn_Summa);
            clientItogo = convertView.findViewById(R.id.tvw_rn_Itogo);
            clientAdress = convertView.findViewById(R.id.tvw_rn_Adress);
            status_rn = convertView.findViewById(R.id.text_status);
            sklad = convertView.findViewById(R.id.tvw_rn_sklad);
            clientDebet = convertView.findViewById(R.id.tvw_rn_debet);
            sale = convertView.findViewById(R.id.tvw_rn_skidka);
            statusVisible = convertView.findViewById(R.id.tvw_status_Invoice);
            img_status = convertView.findViewById(R.id.image_status_zakaza);
            constraintLayoutClick = convertView.findViewById(R.id.list_layout_read);

            //// Для данных через групировку
            grClient = convertView.findViewById(R.id.tvwRNGroup_ClientName);
            grClientAdress = convertView.findViewById(R.id.tvwRNGroup_ClientAdress);
            grStartDate = convertView.findViewById(R.id.tvwRNGroup_StartDate);
            grEndDate = convertView.findViewById(R.id.tvwRNGroup_EndDate);
            grClientDebet = convertView.findViewById(R.id.tvwRNGroup_ClietnDebet);
            grClientItogo = convertView.findViewById(R.id.tvwRNGroup_Itogo);

            buttonEdit = convertView.findViewById(R.id.btnSwipeEdit);
            buttonDelete = convertView.findViewById(R.id.btnSwipeDelete);


        }
    }


    protected void GroupNull(ViewHolder holder, ListAdapterSimple_List_RN_Table listID) {
        holder.kodrn.setText(listID.getKodrn());
        holder.client.setText(listID.getK_agent());
        holder.clientUID.setText(listID.getK_agentUID());
        holder.clientAdress.setText(listID.getAdress());
        holder.thisTime.setText(listID.getVrema());
        holder.thisDate.setText(listID.getData());
        holder.clientSum.setText(listID.getSumma());
        holder.clientItogo.setText(listID.getItogo());
        holder.sale.setText(listID.getSkidka());
        holder.sklad.setText(listID.getSklad());


        if (listID.getStatus() != null) {
            if (listID.getStatus().equals("true")) {
                holder.status_rn.setText("true");
                Picasso.get()
                        .load(R.drawable.icons_image_status_zakaza_up)
                        // .placeholder(R.drawable.button_up) заглушку (placeholder)
                        .error(R.drawable.button_close) //заглушку для ошибки
                        //   .fit()
                        .into(holder.img_status);
            } else {
                holder.status_rn.setText("false");
                Picasso.get()
                        .load(R.drawable.icons_image_status_zakaza)
                        // .placeholder(R.drawable.button_up) заглушку (placeholder)
                        .error(R.drawable.button_close) //заглушку для ошибки
                        //   .fit()
                        .into(holder.img_status);
            }
        } else {
            Picasso.get()
                    .load(R.drawable.icons_image_status_zakaza)
                    // .placeholder(R.drawable.button_up) заглушку (placeholder)
                    .error(R.drawable.button_close) //заглушку для ошибки
                    //   .fit()
                    .into(holder.img_status);
        }

        if (StatusRN(holder.kodrn.getText().toString()))
            holder.statusVisible.setVisibility(View.VISIBLE);
        else holder.statusVisible.setVisibility(View.GONE);

        // Дебеторская задолжность
        try {
            PreferencesWrite preferencesWrite = new PreferencesWrite(context);
            preferencesWrite = new PreferencesWrite(context);
            SQLiteDatabase db = context.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
            String query = "SELECT * FROM otchet_debet " +
                    "WHERE d_kontr_uid = '" + listID.getK_agent().toString() + "' AND d_summa > 0;";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                String d_summa = cursor.getString(cursor.getColumnIndexOrThrow("d_summa")).replace(",", ".");
                holder.clientDebet.setText(new DecimalFormat("#00.00").format(Double.parseDouble(d_summa)).replace(",", "."));
            } else
                holder.clientDebet.setText("не верный формат");
            cursor.close();
            db.close();
        } catch (Exception e) {
            Toast.makeText(context, "Ошибка дебеторской задолжности", Toast.LENGTH_SHORT).show();
            Log.e(logeTAG, "Ошибка дебеторской задолжности");
        }
    }

    protected void Group(ViewHolder holder, ListAdapterSimple_List_RN_Table listID) {
        PreferencesWrite preferencesWrite = new PreferencesWrite(context);

        holder.grClient.setText(listID.getK_agent());
        holder.grClientAdress.setText(listID.getAdress());


        if (preferencesWrite.Setting_FiltersSelectDate) {
            holder.grStartDate.setText(preferencesWrite.Setting_Filters_DataStart);
            holder.grEndDate.setVisibility(View.VISIBLE);
            holder.grEndDate.setText(preferencesWrite.Setting_Filters_DataEND);
        } else {
            holder.grEndDate.setVisibility(View.GONE);
            holder.grStartDate.setText(listID.getData());
        }

        holder.grClientDebet.setText("не верный формат");
        holder.grClientItogo.setText(listID.getItogo());

        String clientUID = listID.getK_agentUID();
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
            holder.grClientDebet.setText(d_summa);
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