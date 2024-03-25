package kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Suncape_Forma;
import kg.roman.Mobile_Torgovla.MT_FTP.FtpConnectData;
import kg.roman.Mobile_Torgovla.R;

public class RecyclerView_Adapter_ViewHolder_Nomeclatura extends RecyclerView.Adapter<RecyclerView_Adapter_ViewHolder_Nomeclatura.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<ListAdapterSimple_Suncape_Forma> list_simple;

    interface OnStateClickListener {
        void onStateClick(ListAdapterSimple_Suncape_Forma clientClick, int position);
    }

    private final OnStateClickListener onClickListener;

    String logeTAG = "RecAdapter";
    Context context;

    public RecyclerView_Adapter_ViewHolder_Nomeclatura(Context context, List<ListAdapterSimple_Suncape_Forma> list_simple, OnStateClickListener onClickListener) {
        this.context = context;
        this.list_simple = list_simple;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_nomenclature_forma, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ListAdapterSimple_Suncape_Forma listID = list_simple.get(position);
        holder.name.setText(listID.getName());
        holder.kol.setText(listID.getKol());
        holder.cena.setText(listID.getCena());
        holder.cena_nall.setText(listID.getCena_Nall());
        holder.strih.setText(listID.getStrih());
        holder.textView_ostatok_colors.setText(listID.getOstatki());
        holder.kod_univ.setText(listID.getKod_univ());
        holder.kod_uid.setText(listID.getKod_uid());
        holder.uid_sklad.setText(listID.getUid_sklad());
        holder.name_sklad.setText(listID.getName_sklad());


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

            Log.e("Image_PUT_PNG", newImage_png + "__" + newImage_png.isFile());
            Log.e("Image_PUT_JPG", newImage_jpg + "__" + newImage_jpg.isFile());

        } catch (Exception e) {
            Log.e("Image_Error", "Нет картинов в ресурсах");
        }



        holder.constraintLayout.setOnClickListener(v -> {
          //  ToastOverride("Click Constr" + position + "__" + listID.name);
          //  Log.e(logeTAG, "onClick: Constr");
            onClickListener.onStateClick(list_simple.get(position), position);
        });


    }

    @Override
    public int getItemCount() {
        return list_simple.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name, kol, cena, cena_nall, cena_kons, strih, textView_ostatok_colors,
                kod_univ, kod_uid, uid_sklad, name_sklad;
        final ImageView imageView;
        final ConstraintLayout constraintLayout;
        ViewHolder(View convertView) {
            super(convertView);

            name = convertView.findViewById(R.id.textView_forma_name);
            kol = convertView.findViewById(R.id.textView_forma_kolbox);
            cena = convertView.findViewById(R.id.textView_forma_cena);
            cena_nall = convertView.findViewById(R.id.textView_forma_cenanal);
            cena_kons = convertView.findViewById(R.id.textView_CenaKons);
            strih = convertView.findViewById(R.id.textView_forma_strih);
            textView_ostatok_colors = convertView.findViewById(R.id.textView_forma_ostatok);
            kod_univ = convertView.findViewById(R.id.textView_forma_kod);
            kod_uid = convertView.findViewById(R.id.text_uid_new);
            uid_sklad = convertView.findViewById(R.id.textView_UID_Sklad);
            name_sklad = convertView.findViewById(R.id.textView_Name_Sklad);
            imageView = convertView.findViewById(R.id.textView_forma_image);
            constraintLayout = convertView.findViewById(R.id.cardTovatRecyclear);

        }
    }

    private void ToastOverride(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}