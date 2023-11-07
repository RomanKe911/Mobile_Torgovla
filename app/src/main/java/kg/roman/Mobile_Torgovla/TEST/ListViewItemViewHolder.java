package kg.roman.Mobile_Torgovla.TEST;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ListViewItemViewHolder extends RecyclerView.ViewHolder {
    private CheckBox itemCheckbox;
    private TextView itemTextView;
    private TextView itemTextView_KOD;
    public ListViewItemViewHolder(View itemView) {
        super(itemView);
    }
    public CheckBox getItemCheckbox() {
        return itemCheckbox;
    }
    public void setItemCheckbox(CheckBox itemCheckbox) {
        this.itemCheckbox = itemCheckbox;
    }
    public TextView getItemTextView() {
        return itemTextView;
    }
    public void setItemTextView(TextView itemTextView) {
        this.itemTextView = itemTextView;
    }


    public TextView getItemTextView_KOD() {
        return itemTextView_KOD;
    }
    public void setItemTextView_KOD(TextView itemTextView_KOD) {
        this.itemTextView_KOD = itemTextView_KOD;
    }

}