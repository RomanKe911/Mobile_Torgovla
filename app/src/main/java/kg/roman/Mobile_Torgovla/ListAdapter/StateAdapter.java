package kg.roman.Mobile_Torgovla.ListAdapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Ftp_Image;
import kg.roman.Mobile_Torgovla.R;


public class StateAdapter extends RecyclerView.Adapter<StateAdapter.ViewHolder>{

    interface OnStateClickListener{
        void onStateClick(ListAdapterSimple_Ftp_Image stateSimple, int position);
    }

    public final OnStateClickListener onClickListener;
    private final LayoutInflater inflater;
    Context context;
    //private final List<State> states;
    ArrayList<ListAdapterSimple_Ftp_Image> states;


    public StateAdapter(Context context,
                        ArrayList<ListAdapterSimple_Ftp_Image> states,
                        OnStateClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.states = states;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public StateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_ftp_image_update, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StateAdapter.ViewHolder viewHolder, int position) {
        ListAdapterSimple_Ftp_Image state = states.get(position);

        viewHolder.brends.setText(state.getBrends_Text());
        viewHolder.filesCount.setText(state.getFiles_Count());
        viewHolder.filesSize.setText(state.getFiles_Size());
        viewHolder.dataUpdate.setText(state.getData_Update());
        if (state.getStatus_Update()) {
            viewHolder.statusUpdate.setText("есть новые данные");
            viewHolder.btn_update.setBackgroundResource(R.drawable.selector_button_ftp_download);
            viewHolder.btn_update.setEnabled(true);
        } else {
            viewHolder.statusUpdate.setText("нет новых данных");
            viewHolder.btn_update.setBackgroundResource(R.drawable.icons_ftp_download_null);
            viewHolder.btn_update.setEnabled(false);
        }
        try {
            File files = new File(context.getFilesDir().getAbsolutePath());
            // String file_files = files.getPath() + "/Icons/bg_bg000115.png";
            String file_files = files.getPath() + "/Icons/" + state.getBrends_Image();
            Log.e("Путь к Logo", file_files);
            Picasso.get()
                    .load(new File(file_files))
                    // .load(new File(getFilesDir().getAbsolutePath()+"/Image/bg_bg000120.png"))
                    // .placeholder(R.drawable.button_up) заглушку (placeholder)
                    .error(R.drawable.no_image) //заглушку для ошибки
                    .into(viewHolder.image);

        } catch (Exception e) {
            Log.e("Image_Error", "Нет картинов в ресурсах");
        }

    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;
        final TextView brends, filesCount, filesSize, dataUpdate, statusUpdate;
        final Button btn_update;
        final ProgressBar  progressBar_Horiszont;
        ViewHolder(View view){
            super(view);
            brends = view.findViewById(R.id.list_ftpImage_textBrends);
            filesCount = view.findViewById(R.id.list_ftpImage_textFilesCount);
            filesSize = view.findViewById(R.id.list_ftpImage_text_FilesSize);
            dataUpdate = view.findViewById(R.id.list_ftpImage_textDateUpdate_Title);
            statusUpdate =  view.findViewById(R.id.list_ftpImage_textStatus);

            image = view.findViewById(R.id.list_ftpImage_image);
            btn_update = (Button) view.findViewById(R.id.list_ftpImage_button);
            progressBar_Horiszont = (ProgressBar) view.findViewById(R.id.list_ftpImage_ProgressBarHorisont);
        }
    }
}