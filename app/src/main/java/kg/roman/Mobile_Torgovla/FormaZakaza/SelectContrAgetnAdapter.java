package kg.roman.Mobile_Torgovla.FormaZakaza;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Klients;
import kg.roman.Mobile_Torgovla.R;

public class SelectContrAgetnAdapter extends RecyclerView.Adapter<SelectContrAgetnAdapter.ViewHolder> {


    public interface OnClickListenerClient {
        void onClientClick(SelectContrAgent clientClick, int position);
    }


    private final LayoutInflater inflater;
    private final List<SelectContrAgent> states;
    private final OnClickListenerClient onClickListener;

    private final HashSet<String> mylist = new HashSet<String>();
    private final Map<Integer, String> myMap = new HashMap<Integer, String>();
    private final Set<Integer> keys = myMap.keySet();

    public SelectContrAgetnAdapter(Context context, List<SelectContrAgent> states, OnClickListenerClient onClickListener) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
    }

    @Override
    public SelectContrAgetnAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.mt_list_klients_content_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SelectContrAgetnAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SelectContrAgent state = states.get(position);
        holder.clientName.setText(state.getClientName());
        holder.clientUID.setText(state.getClientUID());
        holder.clientAdress.setText(state.getClientAdress());
        holder.clientCredit.setText(state.getClientCredit());
        holder.clientImage.setImageResource(state.getClientImage());

      /*  holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //   onClickListener.onClientClick(state, position);
                Toast.makeText(v.getContext(), "выбран= Card" + state.getClientName(), Toast.LENGTH_SHORT).show();
                onClickListener.onClientClick(state, position);
            }
        });*/
       /* holder.clientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   onClickListener.onClientClick(state, position);
                Toast.makeText(v.getContext(), "выбран= Name" + state.getClientName(), Toast.LENGTH_SHORT).show();
                onClickListener.onClientClick(state, position);
            }
        });*/

      /*  holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "выбран= Constr" + state.getClientName(), Toast.LENGTH_SHORT).show();
                onClickListener.onClientClick(state, position);
            }
        });*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "выбран= View" + state.getClientName(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView clientImage;
        final TextView clientName, clientUID, clientAdress, clientCredit;
        final CardView cardView;
        final ConstraintLayout constraintLayout;

        ViewHolder(View view) {
            super(view);
            clientName = view.findViewById(R.id.select_client_name);
            clientUID = view.findViewById(R.id.select_client_uid);
            clientAdress = view.findViewById(R.id.select_client_adress);
            clientCredit = view.findViewById(R.id.select_client_credit);
            clientImage = view.findViewById(R.id.select_client_image);

            cardView = view.findViewById(R.id.select_cardView);
            constraintLayout = view.findViewById(R.id.constrn);
        }
    }
}