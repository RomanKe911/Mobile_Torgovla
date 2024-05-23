package kg.roman.Mobile_Torgovla.FormaZakazaStart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_WJ_Zakaz;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.FtpConnectData;
import kg.roman.Mobile_Torgovla.R;


public class RecyclerView_Adapter_ViewHolder_Position extends RecyclerView.Adapter<RecyclerView_Adapter_ViewHolder_Position.ViewHolder> {


    private final LayoutInflater inflater;
    private final List<ListAdapterSimple_WJ_Zakaz> listTovar;
    List<ListAdapterSimple_WJ_Zakaz> filterListPosition;
    String logeTAG = "AdapterPosition";
    Context context;

    public RecyclerView_Adapter_ViewHolder_Position(Context context, List<ListAdapterSimple_WJ_Zakaz> positionTovar) {
        this.context = context;
        this.listTovar = positionTovar;
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
}