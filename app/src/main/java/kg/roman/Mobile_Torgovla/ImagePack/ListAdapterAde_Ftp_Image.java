package kg.roman.Mobile_Torgovla.ImagePack;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kg.roman.Mobile_Torgovla.MT_FTP.FTPWebhost;
import kg.roman.Mobile_Torgovla.R;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterAde_Ftp_Image extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<ListAdapterSimple_Ftp_Image> objects;
    CustomFilter filter;
    ArrayList<ListAdapterSimple_Ftp_Image> filterList;

    public ListAdapterAde_Ftp_Image(Context ctx, ArrayList<ListAdapterSimple_Ftp_Image> ftp_image) {
        // TODO Auto-generated constructor stub
        this.context = ctx;
        this.objects = ftp_image;
        this.filterList = ftp_image;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return objects.size();
    }

    @Override
    public Object getItem(int pos) {
        // TODO Auto-generated method stub
        return objects.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        return objects.indexOf(getItem(pos));
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder;
        if (convertView == null) {
          //  convertView = inflater.inflate(R.layout.list_ftp_image_update, null);
            convertView = inflater.inflate(R.layout.list_ftp_image_update, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        /*TextView brends = (TextView) convertView.findViewById(R.id.list_ftpImage_textBrends);
        TextView filesCount = (TextView) convertView.findViewById(R.id.list_ftpImage_textFilesCount);
        TextView filesSize = (TextView) convertView.findViewById(R.id.list_ftpImage_text_FilesSize);
        TextView dataUpdate = (TextView) convertView.findViewById(R.id.list_ftpImage_textDateUpdate);
        TextView textProgress = (TextView) convertView.findViewById(R.id.list_ftpImage_textProgress);
        TextView statusUpdate = (TextView) convertView.findViewById(R.id.list_ftpImage_textStatus);

        ImageView image = (ImageView) convertView.findViewById(R.id.list_ftpImage_image);
        Button btn_update = (Button) convertView.findViewById(R.id.list_ftpImage_button);
        ProgressBar progressBar_Circle = (ProgressBar) convertView.findViewById(R.id.list_ftpImage_ProgressBarCircle);
        ProgressBar progressBar_Horiszont = (ProgressBar) convertView.findViewById(R.id.list_ftpImage_ProgressBarHorisont);*/

        viewHolder.brends.setText(objects.get(pos).getBrends_Text());
        viewHolder.filesCount.setText(objects.get(pos).getFiles_Count());
        viewHolder.filesSize.setText(objects.get(pos).getFiles_Size());
        viewHolder.dataUpdate.setText(objects.get(pos).getData_Update());
              viewHolder.statusUpdate.setText("");

        if (objects.get(pos).getStatus_Update()) {
            viewHolder.statusUpdate.setText("есть новые данные");
            viewHolder.btn_update.setBackgroundResource(R.drawable.selector_button_ftp_download);
            viewHolder.btn_update.setEnabled(true);
        } else {
            viewHolder.statusUpdate.setText("нет новых данных");
            viewHolder.btn_update.setBackgroundResource(R.drawable.icons_ftp_download_null);
            viewHolder.btn_update.setEnabled(false);
        }
        viewHolder.btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isEnabled()) {
                    List<String> listFilesNoPhone = new ArrayList<>();;
                    Log.e("BUTTON_CLICK", "Обновлять ресурсы: " + viewHolder.brends.getText());
                    Toast.makeText(context, viewHolder.brends.getText(), Toast.LENGTH_SHORT).show();
                   //viewHolder.progressBar_Circle.setVisibility(View.VISIBLE);

                    FTPWebhost web = new FTPWebhost();
                    web.ftp_server = "ftp.sunbell.webhost.kg";
                    web.ftp_port = 21;
                    web.ftp_user_name = "sunbell_siberica";
                    web.ftp_password = "Roman911NFS";
                    //web.put_toFiles = "/MT_Sunbell_Karakol/Image";

                    File files = new File(context.getFilesDir().getAbsolutePath());
                    String file_files;
                    if (objects.get(pos).getBrends_Text().equals("Icons")) {
                        file_files = files.getPath() + "/Icons/";
                    } else file_files = files.getPath() + "/Image/";


                    web.put_toFTP = "/MT_Sunbell_Karakol/Image/Firm_Tradegof/" + objects.get(pos).getBrends_Text();
                    web.put_toFiles = file_files;

                    Log.e("Go", "Файлы начало");
                    listFilesNoPhone.addAll(web.getFileNotImageFTP());
                    if (!listFilesNoPhone.isEmpty())  Log.e("Go", "Файлы заполнены");
                    Log.e("Go", "Файлы конец");



                    web.getLoadingFileImage("/MT_Sunbell_Karakol/Image/Firm_Tradegof/" +
                            objects.get(pos).getBrends_Text() + "/", file_files, listFilesNoPhone, viewHolder.progressBar_Horiszont);
                }
            }
        });
        try {
            File files = new File(context.getFilesDir().getAbsolutePath());
            // String file_files = files.getPath() + "/Icons/bg_bg000115.png";
            String file_files = files.getPath() + "/Icons/" + objects.get(pos).getBrends_Image();
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

        return convertView;
    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        if (filter == null) {
            filter = new CustomFilter();
        }
        return filter;
    }

    //INNER CLASS
    class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO Auto-generated method stub
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                //CONSTARINT TO UPPER
                constraint = constraint.toString().toUpperCase();
                ArrayList<ListAdapterSimple_Ftp_Image> filters = new ArrayList<ListAdapterSimple_Ftp_Image>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getBrends_Text().toUpperCase().contains(constraint)) {
                        ListAdapterSimple_Ftp_Image p = new ListAdapterSimple_Ftp_Image(filterList.get(i).getBrends_Text(),
                                filterList.get(i).getBrends_Image(),
                                filterList.get(i).getFiles_Size(),
                                filterList.get(i).getFiles_Count(),
                                filterList.get(i).getData_Update(),
                                filterList.get(i).Status_Update);
                        filters.add(p);
                    }
                }
                results.count = filters.size();
                results.values = filters;
            } else {
                results.count = filterList.size();
                results.values = filterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // TODO Auto-generated method stub
            objects = (ArrayList<ListAdapterSimple_Ftp_Image>) results.values;
            notifyDataSetChanged();
        }
    }

    private class ViewHolder {
        final ImageView image;
        final TextView brends, filesCount, filesSize, dataUpdate, statusUpdate;
        final Button btn_update;
        final ProgressBar  progressBar_Horiszont;
        ViewHolder(View view){
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