package kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.ArrayList;
import java.util.List;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_WJ_Zakaz;
import kg.roman.Mobile_Torgovla.MT_FTP.FtpConnectData;

import kg.roman.Mobile_Torgovla.R;


public class RecyclerView_Adapter_ViewHolder_ListTovar extends RecyclerView.Adapter<RecyclerView_Adapter_ViewHolder_ListTovar.ViewHolder> implements Filterable {
    public interface OnStateClickListener {
        void onStateClick(ListAdapterSimple_WJ_Zakaz clientClick, int position);
    }


    private final LayoutInflater inflater;
    private final List<ListAdapterSimple_WJ_Zakaz> listTovar;
    private final OnStateClickListener onClickListener;

    List<ListAdapterSimple_WJ_Zakaz> filterListPosition;
    String logeTAG = "ListTovar";
    Context context;

    public RecyclerView_Adapter_ViewHolder_ListTovar(Context context, List<ListAdapterSimple_WJ_Zakaz> positionTovar, OnStateClickListener onClickListener) {
        this.context = context;
        this.listTovar = positionTovar;
        this.onClickListener = onClickListener;
        this.filterListPosition = positionTovar;
        this.inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_edit_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ListAdapterSimple_WJ_Zakaz listID = listTovar.get(position);
        holder.txtUID.setText(listID.getUID());
        holder.txtName.setText(listID.getName());
        holder.txtUNIV.setText(listID.getKodUniv());

        holder.txtKol.setText(listID.getCount());
        holder.txtCena.setText(listID.getCena());
        holder.txtSumma.setText(listID.getSumma());
        holder.txtSkidka.setText(listID.getSkidka());
        holder.txtItogo.setText(listID.getItogo());

/*        holder.txtName.setOnClickListener(v -> {
            Log.e(logeTAG, "Click: "+listID.getName());
            onClickListener.onStateClick(listID, position);
        });*/

        holder.constraintLayout.setOnClickListener(v -> {
            Log.e(logeTAG, "LongClick: "+listID.getName());
            onClickListener.onStateClick(listID, position);
        });




        try {
            FtpConnectData ftpConnectData = new FtpConnectData();
            File newImage_png = new File(ftpConnectData.put_toPhoneImage(context) + listID.getImage() + ".png");
            File newImage_jpg = new File(ftpConnectData.put_toPhoneImage(context) + listID.getImage() + ".jpg");
            if (newImage_png.isFile())
                Picasso.get() //передаем контекст приложения
                        .load(newImage_png)
                        .error(R.drawable.no_image)
                        .into(holder.imageView); //ссылка на ImageView*/
            else
                Picasso.get() //передаем контекст приложения
                        .load(newImage_jpg)
                        .error(R.drawable.no_image)
                        .into(holder.imageView); //ссылка на ImageView*/

/*            Log.e("Image_PUT_PNG", newImage_png + "__" + newImage_png.isFile());
            Log.e("Image_PUT_JPG", newImage_jpg + "__" + newImage_jpg.isFile());*/

        } catch (Exception e) {
            Log.e("Image_Error", "Нет картинов в ресурсах");
        }

    }

    @Override
    public int getItemCount() {
        return listTovar.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView txtUID, txtName, txtUNIV, txtKol, txtCena, txtSumma, txtSkidka, txtItogo;
        final ImageView imageView;

        final ConstraintLayout constraintLayout;
        ViewHolder(View convertView) {
            super(convertView);
            txtName = convertView.findViewById(R.id.Content_TovarName);
            txtUID = convertView.findViewById(R.id.Content_TovarUID);
            txtUNIV = convertView.findViewById(R.id.Content_TovarUniv);
            imageView = convertView.findViewById(R.id.Content_Image);
            constraintLayout = convertView.findViewById(R.id.Content_ConstraintLayoutTable);

            txtKol = convertView.findViewById(R.id.Content_Count);
            txtCena = convertView.findViewById(R.id.Content_Price);
            txtSumma = convertView.findViewById(R.id.Content_Summa);
            txtSkidka = convertView.findViewById(R.id.Content_Sale);
            txtItogo = convertView.findViewById(R.id.Content_Itogo);


        }
    }

    protected void ToastOverride(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ListAdapterSimple_WJ_Zakaz> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(filterListPosition);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ListAdapterSimple_WJ_Zakaz item : filterListPosition) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterListPosition.clear();
            filterListPosition.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}