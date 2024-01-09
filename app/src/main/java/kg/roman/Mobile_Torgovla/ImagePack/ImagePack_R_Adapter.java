package kg.roman.Mobile_Torgovla.ImagePack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import kg.roman.Mobile_Torgovla.MT_FTP.FTPWebhost;
import kg.roman.Mobile_Torgovla.MT_FTP.FtpConnectData;
import kg.roman.Mobile_Torgovla.R;

public class ImagePack_R_Adapter extends RecyclerView.Adapter<ImagePack_R_Adapter.ViewHolder> {
    public interface OnClickListenerClient {
        void onClientClick(ImagePack_R_Simple clientClick, int position);
    }

    private final LayoutInflater inflater;
    private final List<ImagePack_R_Simple> imagelist;
    private final OnClickListenerClient onClickListener;
    Context context;
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    private SharedPreferences mSettings;
    private SharedPreferences.Editor editor;



    public ImagePack_R_Adapter(Context context, List<ImagePack_R_Simple> imagelist, OnClickListenerClient onClickListener) {
        this.context = context;
        this.imagelist = imagelist;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_ftp_image_update, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.e("Start", "/////////////////////////////////////////////////////////////////");
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        ImagePack_R_Simple listID = imagelist.get(position);

        long dataUpdate_toFTP = listID.getStatus_Update();  // дата обновления и изменений на сервере
        long dataUpdate_toPhone = mSettings.getLong("SAVEDATA_" + listID.getBrends_Text().toLowerCase().trim(), 0); // дата обновления картинок на телефоне
        String nameRegion = mSettings.getString("ftp_put_list", "");    // получение имени региона

        holder.list_card_textBrend.setText(listID.getBrends_Text().substring(3));
        holder.list_card_text_DataNewUpdate.setText(getFullTime(dataUpdate_toFTP));
        holder.list_card_text_DataUpdate.setText(getFullTime(dataUpdate_toPhone));
        holder.list_card_text_FilesSize.setText(listID.getFiles_Size());
        holder.list_card_textFilesCount.setText(listID.getFiles_Count() + " шт");


        if (dataUpdate_toFTP > dataUpdate_toPhone)
            holder.imageViewInfo.setVisibility(View.VISIBLE);
        else holder.imageViewInfo.setVisibility(View.INVISIBLE);


        ///// Вставка картинки в Image через Picaso
        LoadImageBrends(holder, listID.getBrends_Image(), listID.getBrends_Text());

        //////// Обработка для нажатия на кнопку обнавления
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listID.getFiles_Count() > 0) {
                    boolean isChecked = ((Button) v).isClickable();
                    if (v.isEnabled()) {
                        listID.setSelected(false);
                        v.setEnabled(false);

                        List<String> listFilesNoPhone = new ArrayList<>();
                        HashSet<String> hs = new HashSet<>();
                        FTPWebhost web = new FTPWebhost();
                        FtpConnectData connectData = new FtpConnectData();
                        try {

                            web.ftp_server = connectData.server_name;
                            web.ftp_port = connectData.port;
                            web.ftp_user_name = connectData.server_username;
                            web.ftp_password = connectData.server_password;

                            File files = new File(context.getFilesDir().getAbsolutePath());
                            String file_files;

                            switch (listID.getBrends_Text()) {
                                case "za_Icons": {
                                    file_files = files.getPath() + "/Icons/";
                                    for (Map.Entry<String, HashSet<String>> items : getImage("icons", listID.getBrends_Text()).entrySet()) {
                                        if (!items.getValue().isEmpty()) {
                                            Log.e("BUTTON_CLICK", "Обновлять ресурсы бренда: " + "logo");
                                            if (items.getKey().equals(listID.getBrends_Text())) {
                                                Log.e("Phone: ", "Brend: " + items.getKey() + " Список файлов: " + items.getValue());
                                                hs = items.getValue();
                                            }
                                        } else
                                            Toast.makeText(context, "нет новых файлов", Toast.LENGTH_SHORT).show();

                                    }
                                }
                                break;
                                case "zb_Logo": {
                                    file_files = files.getPath() + "/Logo/";
                                    for (Map.Entry<String, HashSet<String>> items : getImage("logo", listID.getBrends_Text()).entrySet()) {
                                        if (!items.getValue().isEmpty()) {
                                            Log.e("BUTTON_CLICK", "Обновлять ресурсы бренда: " + "logo");
                                            if (items.getKey().equals(listID.getBrends_Text())) {
                                                Log.e("Phone: ", "Brend: " + items.getKey() + " Список файлов: " + items.getValue());
                                                hs = items.getValue();
                                            }
                                        } else
                                            Toast.makeText(context, "нет новых файлов", Toast.LENGTH_SHORT).show();

                                    }
                                }
                                break;
                                default: {
                                    file_files = files.getPath() + "/Image/";
                                    for (Map.Entry<String, HashSet<String>> items : getImage(listID.getBrends_Text().substring(0, 2), listID.getBrends_Text()).entrySet()) {
                                        if (!items.getValue().isEmpty()) {
                                            Log.e("BUTTON_CLICK", "Обновлять ресурсы бренда: " + "logo");
                                            if (items.getKey().equals(listID.getBrends_Text())) {
                                                Log.e("Phone: ", "Brend: " + items.getKey() + " Список файлов: " + items.getValue());
                                                hs = items.getValue();
                                            }
                                        } else
                                            Toast.makeText(context, "нет новых файлов", Toast.LENGTH_SHORT).show();

                                    }
                                }
                                break;

                            }

                            Iterator<String> it = hs.iterator();
                            listFilesNoPhone.clear();
                            while (it.hasNext())
                                listFilesNoPhone.add(it.next());

                            web.put_toFTP = connectData.put_toFTPforRegions(context) + "/" + listID.getBrends_Text();
                            web.put_toFiles = file_files;

                            Log.e("Go", "Файлы начало");
                            //listFilesNoPhone.addAll(web.getFileNotImageFTP());
                            if (!listFilesNoPhone.isEmpty()) Log.e("Go", "Файлы заполнены");
                            Log.e("Go", "Файлы конец");

                            ////// скачивание фалйов без progressBar
/*                            web.getLoadingFileImage(connectData.put_toFTPforRegions(context) + "/" +
                                    listID.getBrends_Text() + "/", file_files, listFilesNoPhone, holder.progressBar_Horiszont);*/



                            class MyAsyncTask_Sync extends AsyncTask<Void, Integer, Void> {
                                @Override
                                protected void onPreExecute() { // Вызывается в начале потока
                                    super.onPreExecute();
                                    Log.e("AsyncTask_Sync", "AsyncTask_Sync_Start");
                                    Toast.makeText(context, "Идет скачивание файлов, подождите", Toast.LENGTH_SHORT).show();
                                    holder.progressBar_Horiszont.setVisibility(View.VISIBLE);
                                }

                                @Override
                                protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
                                    super.onProgressUpdate(values);
                                  //  Log.e("ПОТОК=", "_"+values[0]);
                                }
                                @Override
                                protected Void doInBackground(Void... voids) { // Для создания сложных потоков
                                    try {
                                        getFloor();

                                    } catch (InterruptedException e) {
                                        Log.e("AsyncTask_Sync", "Ошибка в потоке данных!");
                                        e.printStackTrace();
                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) { // Вызывается в конце потока
                                    super.onPostExecute(aVoid);
                                    Log.e("AsyncTask_Sync", "AsyncTask_Sync_END");
                                    Toast.makeText(context, "Скачивание файлов завершено", Toast.LENGTH_SHORT).show();
                                    holder.progressBar_Horiszont.setVisibility(View.GONE);
                                    DataSaveThisBrends(v, listID.getBrends_Text());
                                    notifyItemChanged(position);
                                }

                                private void getFloor() throws InterruptedException {
                                    final int[] k = {0};
                                    final double[] prmin = {0};
                                    final double prmax = listFilesNoPhone.size();
                                    final double pr = 100 / prmax;
                                    final boolean encRunnable[] = {false};

                                    FTPClient ftpClient = new FTPClient();
                                    FtpConnectData ftpConnectData = new FtpConnectData();
                                    try {
                                        ftpClient.connect(ftpConnectData.server_name, ftpConnectData.port);
                                        ftpClient.login(ftpConnectData.server_username, ftpConnectData.server_password);
                                        ftpClient.enterLocalPassiveMode();
                                        Log.e("FTP", "Пакетное скачивание");
                                        Log.e("LIST: ", "Start: " + connectData.put_toFTPforRegions(context) + "/" +
                                                listID.getBrends_Text() + "/");
                                        Log.e("LIST: ", "END: " + file_files);
                                        k[0] = 0;
                                        while (k[0] < listFilesNoPhone.size()) {
                                            prmin[0] = +prmin[0] + pr;
                                            Log.e("LIST_Yes: ", "Files: " + listFilesNoPhone.get(k[0]));
                                            Log.e("FTP", "Скачивание одного файла ");
                                            // OutputStream outputStream = new FileOutputStream(new File(put_toFilesEND + "/Image/" + w_list.get(k)));  // путь к папке files приложения
                                            OutputStream outputStream = new FileOutputStream(new File(file_files + listFilesNoPhone.get(k[0])));  // путь к папке files приложения
                                            // OutputStream outputStream = new FileOutputStream(new File(put_toFilesEND + w_list.get(k)));  // путь к папке files приложения
                                            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                                            ftpClient.enterLocalPassiveMode();
                                            ftpClient.retrieveFile(connectData.put_toFTPforRegions(context) + "/" +
                                                    listID.getBrends_Text() + "/" + listFilesNoPhone.get(k[0]), outputStream);
                                            outputStream.close();
                                            k[0]++;
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Log.e("ERROR: ", "Ошибка");
                                    } finally {
                                        try {
                                            if (ftpClient.isConnected()) {
                                                ftpClient.logout();
                                                ftpClient.disconnect();
                                            }
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                    }

                                }


                            }
                            MyAsyncTask_Sync asyncTask = new MyAsyncTask_Sync();
                            asyncTask.execute();


                        } catch (Exception ex) {
                            Log.e("ButtonClick", "Крэш, ошибка загрузки или обновления файлов");
                        }


                    }
                } else
                    Toast.makeText(v.getContext(), "на сервере еще нет картинок для бренда: " + listID.getBrends_Text().substring(3), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagelist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView imageView, imageViewInfo;
        final Button button;
        final ProgressBar progressBar_Horiszont;
        final TextView list_card_textBrend, list_card_text_FilesSize, list_card_textFilesCount,
                list_card_text_DataUpdate, list_card_text_DataNewUpdate;

        ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.list_ftpImage_image);
            imageViewInfo = view.findViewById(R.id.list_ftpImage_imageUpdate);
            button = view.findViewById(R.id.list_ftpImage_button);
            list_card_textBrend = view.findViewById(R.id.list_ftpImage_textBrends);
            list_card_textFilesCount = view.findViewById(R.id.list_ftpImage_textFilesCount);
            list_card_text_FilesSize = view.findViewById(R.id.list_ftpImage_text_FilesSize);
            list_card_text_DataNewUpdate = view.findViewById(R.id.list_ftpImage_textDateUpdate);
            list_card_text_DataUpdate = view.findViewById(R.id.list_ftpImage_textStatus);

            progressBar_Horiszont = view.findViewById(R.id.list_ftpImage_ProgressBarHorisont);
        }
    }

    ////// Процедура возврата даты и времени из общего формата
    protected String getFullTime(long timeInMillis) {
        final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeInMillis);
        c.setTimeZone(TimeZone.getDefault());
        if (timeInMillis != 0L)
            return format.format(c.getTime());
        else return "00.00.0000, 00:00";
    }

    ////// Процедура удаления файлов которых нет на FTP сервере
    protected void FileDelete(HashSet<String> hashSet) {
        for (String s : hashSet) {
            File files = new File(context.getFilesDir().getAbsolutePath());
            File f_image = new File(files.getPath() + "/Image/" + s);
            File f_icons = new File(files.getPath() + "/Icons/" + s);
            if (f_image.isFile())
                f_image.delete();
            else if (f_icons.isFile())
                f_icons.delete();
        }
    }

    /// Функция с подключением картинки для брендов из ресурсов телефона для ImageView
    protected void LoadImageBrends(ViewHolder viewHolder, String nameFile, String nameBrend) {

        File files = new File(context.getFilesDir().getAbsolutePath());
        String file_files;
        switch (nameBrend) {
            case "za_Icons":
                file_files = files.getPath() + "/Icons/icons_image_for_icons.png";
                break;
            case "zb_Logo":
                file_files = files.getPath() + "/Icons/icons_image_for_logo.png";
                break;
            default:
                file_files = files.getPath() + "/Logo/" + nameFile;
                break;
        }
        Log.e("PICTURE: ", "nameBrend: " + nameBrend + " ,FileName: " + file_files);
        Picasso.get()
                .load(new File(file_files))
                // .load(new File(getFilesDir().getAbsolutePath()+"/Image/bg_bg000120.png"))
                // .placeholder(R.drawable.button_up) заглушку (placeholder)
                .error(R.drawable.no_image) //заглушку для ошибки
                .into(viewHolder.imageView);


    }


    protected HashMap<String, HashSet<String>> getImage(String prefix, String brend) {
        HashSet<String> hashSet = new HashSet<>();
        HashSet<String> hashSetFileDelete = new HashSet<>();
        HashMap<String, Long> hashMap_toFTP = getListFilesFTP(brend);
        HashMap<String, Long> hashMap_toPhone = getListFilesPhone(prefix);

        HashMap<String, HashSet<String>> hashMap = new HashMap<>();

        if (hashMap_toPhone != null && hashMap_toFTP != null && !hashMap_toPhone.isEmpty() && !hashMap_toFTP.isEmpty()) {

            for (Map.Entry<String, Long> items : hashMap_toFTP.entrySet()) {
                Log.e("FTP: ", "FileName: " + items.getKey() + " Date: " + getFullTime(items.getValue()));
            }
            for (Map.Entry<String, Long> items : hashMap_toPhone.entrySet()) {
                Log.e("Phone: ", "FileName: " + items.getKey() + " Date: " + getFullTime(items.getValue()));
            }

            boolean myBoolUpdate = false, myBoolDelete = false;

            for (int i = 0; i < hashMap_toFTP.size(); i++) {
                if (!hashMap_toPhone.containsKey(hashMap_toFTP.keySet().toArray()[i])) {
                    myBoolUpdate = true;
                    Log.e("HashMap: ", "Файлы для скачивания: " + hashMap_toFTP.keySet().toArray()[i].toString() + "_" + myBoolUpdate);
                    hashSet.add(hashMap_toFTP.keySet().toArray()[i].toString());
                }
            }

            for (int i = 0; i < hashMap_toPhone.size(); i++)
                if (!hashMap_toFTP.containsKey(hashMap_toPhone.keySet().toArray()[i])) {
                    myBoolDelete = true;
                    Log.e("HashMap: ", "Файлы на удаление: " + hashMap_toPhone.keySet().toArray()[i].toString() + "_" + myBoolDelete);
                    hashSetFileDelete.add(hashMap_toPhone.keySet().toArray()[i].toString());
                }
            if (!hashSetFileDelete.isEmpty())
                FileDelete(hashSetFileDelete);

            if (myBoolUpdate == true || myBoolDelete == true) {
                Log.e("Boolean: ", "есть обновление");
            } else {
                Log.e("Boolean: ", "нет новых файлов, но есть изменения");
                for (int i = 0; i < hashMap_toFTP.size(); i++) {
                    if (Long.valueOf(hashMap_toFTP.values().toArray()[i].toString()) > Long.valueOf(hashMap_toPhone.values().toArray()[i].toString())) {
                        Log.e("Список для обновления: ", hashMap_toFTP.keySet().toArray()[i].toString() + "__" + hashMap_toFTP.values().toArray()[i].toString());
                        hashSet.add(hashMap_toFTP.keySet().toArray()[i].toString());
                    }
                }
            }


        } else if (hashMap_toPhone == null && hashMap_toFTP != null) {
            for (int i = 0; i < hashMap_toFTP.size(); i++) {
                Log.e("HashMapEl: ", "Файлы для скачивания: " + hashMap_toFTP.keySet().toArray()[i].toString());
                hashSet.add(hashMap_toFTP.keySet().toArray()[i].toString());
            }
        } else Log.e("HashMapEls: ", "нет файлов для скачивания");
        hashMap.put(brend, hashSet);
        return hashMap;
    }

    protected HashMap<String, Long> getListFilesFTP(String brend) {
        FTPWebhost web = new FTPWebhost();
        if (!web.getListBrendImage_Size(brend, context).isEmpty())
            return web.getListBrendImage_Size(brend, context);
        else return null;
    }

    protected HashMap<String, Long> getListFilesPhone(String prefix) {
        HashMap<String, Long> hamap = new HashMap<String, Long>();
        File files = new File(context.getFilesDir().getAbsolutePath());
        File file;
        File[] list;

        switch (prefix) {
            case "icons":
                file = new File(files.getPath() + "/Icons/");
                list = file.listFiles();

                for (File value : list)
                    if (value.getName().substring(0, 5).equals(prefix))
                        hamap.put(value.getName(), value.lastModified());
                break;
            case "logo": {
                file = new File(files.getPath() + "/Logo/");
                list = file.listFiles();

                for (File value : list)
                    if (value.getName().substring(0, 4).equals(prefix))
                        hamap.put(value.getName(), value.lastModified());
            }
            break;
            default: {
                file = new File(files.getPath() + "/Image/");
                list = file.listFiles();

                for (File value : list)
                    if (value.getName().substring(0, 2).equals(prefix))
                        hamap.put(value.getName(), value.lastModified());
            }
            break;
        }


        if (!hamap.isEmpty()) return hamap;
        else return null;
    }

    protected void DataSaveThisBrends(View v, String getBrendClick) {
        Date date = new Date();
        Toast.makeText(v.getContext(), "Дата обновления: " + getFullTime(date.getTime()), Toast.LENGTH_SHORT).show();
        Log.e("Update", "Дата обновления: " + getFullTime(date.getTime()));
        editor = mSettings.edit();
        editor.putLong("SAVEDATA_" + getBrendClick.toLowerCase().trim(), date.getTime());
        editor.commit();
    }



}