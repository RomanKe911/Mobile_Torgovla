package kg.roman.Mobile_Torgovla;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_WJ_Zakaz;

public class CustomAdapter_ListView extends ArrayAdapter<ListAdapterSimple_WJ_Zakaz> implements View.OnClickListener{

    private ArrayList<ListAdapterSimple_WJ_Zakaz> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtSumma;
        TextView txtID;
        TextView txtKol;
        TextView txtKod;
        TextView txtCena;
        Button button;
    }

    public CustomAdapter_ListView(ArrayList<ListAdapterSimple_WJ_Zakaz> data, Context context) {
        super(context, R.layout.list_edit_content, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        ListAdapterSimple_WJ_Zakaz dataModel=(ListAdapterSimple_WJ_Zakaz)object;

        switch (v.getId())
        {
            /*case R.id.item_info:
              //  Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG).setAction("No action", null).show();
                break;*/

/*            case R.id.button_delete_id:
                //  Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG).setAction("No action", null).show();
                Log.e("Suncape_B", dataModel.getUID());
                break;*/
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ListAdapterSimple_WJ_Zakaz dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_edit_content, parent, false);


            viewHolder.txtID = (TextView) convertView.findViewById(R.id.Content_TovarUniv);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.Content_TovarUID);
            viewHolder.txtKod = (TextView) convertView.findViewById(R.id.wj_cont_kod);
            viewHolder.txtKol = (TextView) convertView.findViewById(R.id.Content_Count);
            viewHolder.txtCena = (TextView) convertView.findViewById(R.id.Content_Price);
            //viewHolder.button = (Button) convertView.findViewById(R.id.button_delete_id);



            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtID.setText(dataModel.getKodUniv());
        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtKod.setText(dataModel.getKod());
        viewHolder.txtKol.setText(dataModel.getCount());
        viewHolder.txtCena.setText(dataModel.getCena());
        viewHolder.button.setOnClickListener(this);
        viewHolder.button.setTag(position);
       // viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}