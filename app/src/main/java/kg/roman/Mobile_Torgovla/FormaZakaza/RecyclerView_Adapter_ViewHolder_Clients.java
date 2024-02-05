package kg.roman.Mobile_Torgovla.FormaZakaza;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kg.roman.Mobile_Torgovla.ListSimple.State;
import kg.roman.Mobile_Torgovla.MT_BackUp.RecyclerView_Simple_BackUp;
import kg.roman.Mobile_Torgovla.MT_FTP.FTPWebhost;
import kg.roman.Mobile_Torgovla.MT_FTP.FtpConnectData;
import kg.roman.Mobile_Torgovla.R;

public class RecyclerView_Adapter_ViewHolder_Clients extends RecyclerView.Adapter<RecyclerView_Adapter_ViewHolder_Clients.ViewHolder> implements Filterable {

    private final LayoutInflater inflater;
    private final List<ListAdapterSimple_Klients> imagelist;
    CustomFilter customFilet;
    ArrayList<ListAdapterSimple_Klients> filterList;

    List<Integer> imageList = new ArrayList<>();
    List<String> title = new ArrayList<>();
    List<String> peice = new ArrayList<>();


    @Override
    public Filter getFilter() {
        return customFilet;
    }


    interface OnStateClickListener{
        void onStateClick(ListAdapterSimple_Klients clientClick, int position);
    }

    private final OnStateClickListener onClickListener;

    String logeTAG = "RecAdapter";
    Context context;

    public RecyclerView_Adapter_ViewHolder_Clients(Context context, List<ListAdapterSimple_Klients> backup_list, OnStateClickListener onClickListener) {
        this.context = context;
        this.imagelist = backup_list;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
        this.customFilet = new CustomFilter();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mt_list_klients_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ListAdapterSimple_Klients listID = imagelist.get(position);
        holder.client_uid.setText(listID.getUID());
        holder.client_name.setText(listID.getName());
        holder.client_adress.setText(listID.getAdress());
        holder.client_debet.setText(listID.getUserUID());

       holder.constrain.setOnClickListener(v -> {
           // ToastOverride("Click Constr" + position + "__" + listID.name);
         //   Log.e(logeTAG, "onClick: Constr");
           onClickListener.onStateClick(imagelist.get(position), position);
        });

        Picasso.get()
                .load(SelectImageView(listID.name))
                // .load(new File(getFilesDir().getAbsolutePath()+"/Image/bg_bg000120.png"))
                // .placeholder(R.drawable.button_up) заглушку (placeholder)
                .error(R.drawable.no_image) //заглушку для ошибки
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return imagelist.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView client_uid, client_name, client_adress, client_debet;
        final ImageView imageView;
        final ConstraintLayout constrain;

        ViewHolder(View view) {
            super(view);
            client_uid = view.findViewById(R.id.select_client_uid);
            client_name = view.findViewById(R.id.select_client_name);
            client_adress = view.findViewById(R.id.select_client_adress);
            client_debet = view.findViewById(R.id.select_client_credit);
            imageView = view.findViewById(R.id.imageView_P_Klients);
            constrain = view.findViewById(R.id.constrain);
        }
    }

    private void ToastOverride(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    ////// возврат путь к картинкам для контрагентов
    private int SelectImageView(String name) {
        String image_var = name.substring(0, 2);
        String name_syb = name.substring(0, 3);
        String resID;
        switch (name_syb) {
            case "маг":
                resID = "user_shop_01";
                break; // магазин
            case "+ма":
                resID = "user_shop_01";
                break; // магазин
            case "++м":
                resID = "user_shop_01";
                break; // магазин
            case "мар":
                resID = "user_shop_04";
                break; // магазин
            case "апт":
                resID = "user_shop_02";
                break; // аптека
            case "ко":
                resID = "user_shop_03";
                break; // контейнер
            case "пав":
                resID = "user_shop_03";
                break; // контейнер
            case "р-к":
                resID = "user_shop_03";
                break; // контейнер
            case "го":
                resID = "user_shop_06";
                break; // гостиница
            case "с. ":
                resID = "user_shop_07";
                break; // гостиница
            case "г. ":
                resID = "user_shop_08";
                break; // гостиница
            default:
                resID = "user_shop_05";
                break; // контейнер
        }

        File files = new File(context.getFilesDir().getAbsolutePath());
        File file = new File(files.getPath() + "/Image/");
        return context.getResources().getIdentifier(resID, "drawable", context.getPackageName());
    }


    public class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.e(logeTAG, "FILETE1"+imageList.size());
            Log.e(logeTAG, "FILETE2"+filterList.size());
            Log.e(logeTAG, "Start"+constraint.toString());
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                //CONSTARINT TO UPPER
                constraint = constraint.toString().toUpperCase().replaceAll(" ", "");
                ArrayList<ListAdapterSimple_Klients> filters = new ArrayList<ListAdapterSimple_Klients>();
                //get specific items
                Log.e(logeTAG, "Start_1"+constraint);
                for (int i = 0; i < imageList.size(); i++) {
                    Log.e(logeTAG, "Name1"+filterList.get(i).getName().toLowerCase());
                    Log.e(logeTAG, "Name10"+filters.get(i).getName().toLowerCase());
                    Log.e(logeTAG, "Name12"+imageList.get(i));

                    if (filterList.get(i).name.toUpperCase().contains(constraint)) {
                        ListAdapterSimple_Klients p = new ListAdapterSimple_Klients(
                                filterList.get(i).name,
                                filterList.get(i).uid,
                                filterList.get(i).adress,
                                filterList.get(i).image,
                                filterList.get(i).userUID);
                        filters.add(p);
                    }
                    Log.e(logeTAG, "Name2"+filterList.get(i).getName());
                }
                results.count = filters.size();
                results.values = filters;
            } else {
                Log.e(logeTAG, "Second null text" + results.values);
                results.count = filterList.size();
                results.values = filterList;
            }

            Log.e(logeTAG, "END" + results.values);
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // TODO Auto-generated method stub
            filterList = (ArrayList<ListAdapterSimple_Klients>) results.values;
            notifyDataSetChanged();
        }
    }


}