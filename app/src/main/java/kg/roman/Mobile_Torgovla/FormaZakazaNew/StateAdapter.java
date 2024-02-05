package kg.roman.Mobile_Torgovla.FormaZakazaNew;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import kg.roman.Mobile_Torgovla.R;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<State> states;

    private final HashSet<String> mylist = new HashSet<String>();
    private final Map<Integer, String> myMap = new HashMap<Integer, String>();
    private final Set<Integer> keys = myMap.keySet();

    public StateAdapter(Context context, List<State> states) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public StateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.aaa_test_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StateAdapter.ViewHolder holder, int position) {
        State state = states.get(position);
        holder.flagView.setImageResource(state.getFlagResource());
        holder.nameView.setText(state.getName());
        holder.capitalView.setText(state.getCapital());
        holder.checkBox.setChecked(state.isSelected());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox) v).isChecked();
                if (isChecked) {
                    state.setSelected(true);
                    Toast.makeText(v.getContext(), "выбран=" + state.getName(), Toast.LENGTH_SHORT).show();
                    mylist.add(state.getName());
                    myMap.put(state.getFlagResource(), state.getName());

                } else {
                    state.setSelected(false);
                    myMap.remove(state.getFlagResource());
                }
                notifyDataSetChanged();

                Log.e("Начало", "_________________");
                for (Map.Entry<Integer, String> item : myMap.entrySet()) {
                    Log.e("MAPA", "Key: %d  Value: %s" + item.getKey() + " __ " + item.getValue());
                }
                Log.e("Конец", "_________________");
               /* for (int i = 0; i <= myMap.size(); i++) {
                    Log.e("MAPA", "Key:" + myMap.keySet());
                    Log.e("MAPA", "Keys:" + keys + "K=" + k);
                }
*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView flagView;
        final TextView nameView, capitalView;
        final CheckBox checkBox;


        ViewHolder(View view) {
            super(view);
            flagView = view.findViewById(R.id.flag);
            nameView = view.findViewById(R.id.name);
            capitalView = view.findViewById(R.id.capital);
            checkBox = view.findViewById(R.id.checkBoxTest);
        }
    }
}