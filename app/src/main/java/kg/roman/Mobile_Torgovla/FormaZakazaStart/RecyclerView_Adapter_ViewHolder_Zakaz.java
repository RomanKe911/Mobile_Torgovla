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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_List_RN_Table;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.PreferencesWrite;
import kg.roman.Mobile_Torgovla.R;

public class RecyclerView_Adapter_ViewHolder_Zakaz
        extends RecyclerView.Adapter<RecyclerView_Adapter_ViewHolder_Zakaz.ViewHolder> {


    public interface OnStateClickListener {
        void onStateClick(ListAdapterSimple_List_RN_Table clientClick, int position);

        void onStateClickDelete(ListAdapterSimple_List_RN_Table clientClick, int position);

        void onStateClickEdit(ListAdapterSimple_List_RN_Table clientClick, int position);

        void onStateClickCopy(ListAdapterSimple_List_RN_Table clientClick, int position);
    }


    private final LayoutInflater inflater;
    private final List<ListAdapterSimple_List_RN_Table> imagelist;
    private final OnStateClickListener onClickListener;
    String logeTAG = "RecAdapterZakaz";
    Context context;
    PreferencesWrite preferencesWrite;

    public RecyclerView_Adapter_ViewHolder_Zakaz(Context context,
                                                 List<ListAdapterSimple_List_RN_Table> backup_list,
                                                 OnStateClickListener onClickListener) {
        this.context = context;
        this.imagelist = backup_list;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
        preferencesWrite = new PreferencesWrite(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        preferencesWrite = new PreferencesWrite(context);
        View view;
        if (preferencesWrite.Setting_FiltersSelectGroup)
            view = inflater.inflate(R.layout.wj_content_rn_group, parent, false);
        else
            //    view = inflater.inflate(R.layout.wj_content_rn032024test, parent, false);
            view = inflater.inflate(R.layout.wj_swipe_content, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ListAdapterSimple_List_RN_Table listID = imagelist.get(position);

        imagelist.get(position).getAdress();

        preferencesWrite = new PreferencesWrite(context);
        if (!preferencesWrite.Setting_FiltersSelectGroup) {
            GroupNull(holder, listID);

            //// Обработка кнопки Просмотра
            try {
                holder.constraintLayoutClick.setOnClickListener(v ->
                {
                    onClickListener.onStateClick(imagelist.get(position), position);
                    // Toast.makeText(context, "Нажата Review", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                Log.e(logeTAG, "Error, Review: " + e);
            }

            //// Обработка кнопки Копирования
            try {
                holder.buttonCopy.setOnClickListener(v -> {
                    onClickListener.onStateClickCopy(imagelist.get(position), position);
                    Toast.makeText(context, "Нажата Copys", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                Log.e(logeTAG, "Error, copy: " + e);
            }

            //// Обработка кнопки Редактирования
            try {
                if (imagelist.get(position).status.equals("false")) {
                    holder.buttonEdit.setVisibility(View.VISIBLE);
                    holder.buttonEdit.setOnClickListener(v -> {
                        onClickListener.onStateClickEdit(imagelist.get(position), position);
                        Toast.makeText(context, "Нажата Edits", Toast.LENGTH_SHORT).show();
                    });
                } else holder.buttonEdit.setVisibility(View.INVISIBLE);
            } catch (Exception e) {
                Log.e(logeTAG, "Error, edit: " + e);
            }

            //// Обработка кнопки Удаления
            try {
                if (imagelist.get(position).status.equals("false")) {
                    holder.buttonDelete.setVisibility(View.VISIBLE);
                    holder.buttonDelete.setOnClickListener(v -> {
                        onClickListener.onStateClickDelete(imagelist.get(position), position);
                     //   Toast.makeText(context, "Нажата Deletes", Toast.LENGTH_SHORT).show();
                    });
                } else holder.buttonDelete.setVisibility(View.INVISIBLE);
            } catch (Exception e) {
                Log.e(logeTAG, "Error, delete: " + e);
            }

        } else
            Group(holder, listID);

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

        ImageButton buttonEdit, buttonDelete, buttonCopy;


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

            buttonEdit = convertView.findViewById(R.id.btn_SwipeEdit);
            buttonDelete = convertView.findViewById(R.id.btn_SwipeDelete);
            buttonCopy = convertView.findViewById(R.id.btn_SwipeCopy);
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
        holder.clientDebet.setText(SelectClientDebet(holder.clientUID.getText().toString()));


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

        holder.grClientDebet.setText(SelectClientDebet(listID.getK_agentUID()));
        holder.grClientItogo.setText(listID.getItogo());
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

    protected String SelectClientDebet(String clientUID) {
        // Дебеторская задолжность
        try {

        } catch (Exception e) {
            Toast.makeText(context, "Ошибка дебеторской задолжности", Toast.LENGTH_SHORT).show();
            Log.e(logeTAG, "Ошибка дебеторской задолжности");
        }

        String d_summa = "";
        preferencesWrite = new PreferencesWrite(context);
        SQLiteDatabase db = context.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT * FROM otchet_debet WHERE d_kontr_uid = '" + clientUID + "' AND d_summa > 0;";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Log.e(logeTAG, "DebetCount" + cursor.getCount());
        if (cursor.getCount() > 0)
        {
            Log.e(logeTAG, "DebetSum" + cursor.getString(cursor.getColumnIndexOrThrow("d_summa")));
            d_summa = cursor.getString(cursor.getColumnIndexOrThrow("d_summa")).replace(",", ".");

        }
        else d_summa = "0.0";
        cursor.close();
        db.close();
        Log.e(logeTAG, "Debet" + d_summa);
        return d_summa;
    }
}