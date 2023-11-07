package kg.roman.Mobile_Torgovla.FormaZakaza;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Klients;
import kg.roman.Mobile_Torgovla.R;

public class ListRecycleAdapterAde_Klients extends RecyclerView.Adapter<ListRecycleAdapterAde_Klients.ViewHolder> {

   public interface OnClickListenerClient{
        void onClientClick(ListAdapterSimple_Klients clientClick, int position);
    }

    private final OnClickListenerClient onClickListener;
    private final LayoutInflater inflater;
    private final List<ListAdapterSimple_Klients> clients;

   public ListRecycleAdapterAde_Klients(Context context, List<ListAdapterSimple_Klients> clients, OnClickListenerClient onClickListener) {
        this.onClickListener = onClickListener;
        this.clients = clients;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ListRecycleAdapterAde_Klients.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.mt_list_klients_content_new, parent, false);
        return new ViewHolder(view);
    }
   /* android:background="?android:attr/selectableItemBackground"
    android:background="@color/background_list"*/
    @Override
    public void onBindViewHolder(ListRecycleAdapterAde_Klients.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ListAdapterSimple_Klients client = clients.get(position);

        holder.UID.setText(client.getUID());
        holder.NAME.setText(client.getName());
        holder.ADRESS.setText(client.getAdress());
        holder.DEBET.setText(client.getUserUID());

      /*  holder.flagView.setImageResource(state.getFlagResource());
        holder.nameView.setText(state.getName());
        holder.capitalView.setText(state.getCapital());*/
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClickListener.onClientClick(client, position);
            }
        });



       // File imagePath_SD = new File("/sdcard/Price/" + objects.get(pos).getImage() + ".png");//путь к изображению
     //   Uri imagePath_phone = Uri.parse("android.resource://kg.price_list.roman_kerkin.sunbell_price/drawable/" + client.getImage());

        Uri imagePath_phone = Uri.parse("android.resource://kg.roman.Mobile_Torgovla/drawable/" + client.getImage());
        Picasso.get().load(imagePath_phone).into(holder.IMAGE);

    }

    @Override
    public int getItemCount() {
        return clients.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView UID, NAME, ADRESS, DEBET;
        final ImageView IMAGE;


      /*  final ImageView flagView;
        final TextView nameView, capitalView;*/

        ViewHolder(View view) {
            super(view);
           /* flagView = view.findViewById(R.id.flag);
            nameView = view.findViewById(R.id.name);
            capitalView = view.findViewById(R.id.capital);*/

            UID = view.findViewById(R.id.select_client_uid);
            NAME = (TextView) view.findViewById(R.id.select_client_name);
            ADRESS = (TextView) view.findViewById(R.id.select_client_adress);
            DEBET = (TextView) view.findViewById(R.id.select_client_credit);
            IMAGE = (ImageView) view.findViewById(R.id.imageView_P_Klients);
        }
    }
}
