package kg.roman.Mobile_Torgovla.MT_BackUp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.List;
import kg.roman.Mobile_Torgovla.MT_FTP.FTPWebhost;
import kg.roman.Mobile_Torgovla.MT_FTP.FtpConnectData;
import kg.roman.Mobile_Torgovla.R;

public class RecyclerView_Adapter_ViewHolder_BackUp extends RecyclerView.Adapter<RecyclerView_Adapter_ViewHolder_BackUp.ViewHolder> {
    public interface OnClickListenerClient {
        void onClientClick(RecyclerView_Simple_BackUp clientClick, int position);
    }

    private final LayoutInflater inflater;
    private final List<RecyclerView_Simple_BackUp> imagelist;
    private final OnClickListenerClient onClickListener;

    String logeTAG = "RecAdapter";
    Context context;

    public RecyclerView_Adapter_ViewHolder_BackUp(Context context, List<RecyclerView_Simple_BackUp> backup_list, OnClickListenerClient onClickListener) {
        this.context = context;
        this.imagelist = backup_list;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_backup, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RecyclerView_Simple_BackUp listID = imagelist.get(position);
        holder.backup_agentname.setText(listID.getBackUp_AgentName());
        holder.backup_agentregion.setText(listID.getBackUp_AgentRegion());
        holder.backup_size_rndb.setText(listID.getBackUp_RNDB_Size());
        holder.backup_size_basedb.setText(listID.getBackUp_BASEDB_Size());
        holder.backup_size_constdb.setText(listID.getBackUp_CONSTDB_Size());
        holder.backup_fileData.setText(listID.getBackUp_FileData());
        class MyAsyncTask_Sync extends AsyncTask<String, Integer, Boolean> {

            @Override
            protected void onPreExecute() { // Вызывается в начале потока
                super.onPreExecute();
                Log.e(logeTAG, "AsyncTask_Sync_Start");
                ToastOverride("Идет скачивание файлов, подождите");
                holder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onProgressUpdate(Integer... values) { // Вызывается для обновления данных после загрузки
                super.onProgressUpdate(values);
                //  Log.e("ПОТОК=", "_"+values[0]);

            }

            @Override
            protected void onPostExecute(Boolean aVoid) { // Вызывается в конце потока
                super.onPostExecute(aVoid);
                Log.e(logeTAG, "AsyncTask_Sync_END");
                if (aVoid) {
                    Log.e(logeTAG, "Скачивание файлов завершено, файл успешно скачен");
                    ToastOverride("Скачивание файлов завершено, файл успешно скачен");
                } else {
                    Log.e(logeTAG, "Скачивание файлов завершено, не удалось скачать файл");
                    ToastOverride("Скачивание файлов завершено, не удалось скачать файл");

                }
                holder.progressBar.setVisibility(View.GONE);
                notifyItemChanged(position);
            }

            @Override
            protected Boolean doInBackground(String... params) { // Для создания сложных потоков
                boolean status = false;
                try {
                    status = getFloor(params[0]);

                } catch (InterruptedException e) {
                    Log.e(logeTAG, "Ошибка в потоке данных!");
                    e.printStackTrace();
                }
                return status;
            }

            private boolean getFloor(String typeFile) throws InterruptedException {
                FTPWebhost ftpWebhost = new FTPWebhost();
                FtpConnectData connectData = new FtpConnectData();
                StringBuilder fileName = new StringBuilder();
                fileName.append(connectData.put_toFtpBackUp(context))
                        .append(holder.backup_fileData.getText())
                        .append("_" + typeFile + "_")
                        .append(holder.backup_agentname.getText() + ".db3");
                File files = new File(context.getDatabasePath(typeFile + ".db3").getAbsolutePath());
                Log.e(logeTAG, "getFloor: " + fileName);
                Log.e(logeTAG, "getFloor2: " + files.getPath());
                return (ftpWebhost.getFileToPhone(fileName.toString(), files.getPath(), context, true));
            }

        }

        holder.backup_button_rbdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("BackUp", "Скачать базу расходных накладных");
                MyAsyncTask_Sync asyncTask = new MyAsyncTask_Sync();
                asyncTask.execute("sunbell_rn_db");
            }
        });

        holder.backup_button_constdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("BackUp", "Скачать базу данных настроку приложения");
                MyAsyncTask_Sync asyncTask = new MyAsyncTask_Sync();
                asyncTask.execute("sunbell_const_db");
            }
        });

        holder.backup_button_basedb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("BackUp", "Скачать базу данных структура");
                MyAsyncTask_Sync asyncTask = new MyAsyncTask_Sync();
                asyncTask.execute("sunbell_base_db");
            }
        });



    }

    @Override
    public int getItemCount() {
        return imagelist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView backup_agentname, backup_agentregion, backup_size_rndb, backup_size_basedb, backup_size_constdb, backup_fileData;
        final Button backup_button_rbdb, backup_button_constdb, backup_button_basedb;
        final ProgressBar progressBar;
        final ViewModelStoreOwner ff;
        final LifecycleOwner ll;

        ViewHolder(View view) {
            super(view);
            backup_agentname = view.findViewById(R.id.textView_backup_text_agent);
            backup_agentregion = view.findViewById(R.id.textView_backup_text_region);
            backup_size_rndb = view.findViewById(R.id.textView_backup_textsize_01);
            backup_size_basedb = view.findViewById(R.id.textView_backup_textsize_02);
            backup_size_constdb = view.findViewById(R.id.textView_backup_textsize_03);
            backup_fileData = view.findViewById(R.id.textView_backup_text_datafile);

            backup_button_rbdb = view.findViewById(R.id.button_backup_rndb);
            backup_button_constdb = view.findViewById(R.id.button_backup_constdb);
            backup_button_basedb = view.findViewById(R.id.button_backup_basedb);

            progressBar = view.findViewById(R.id.progressBar_backup);

            ff = new ViewModelStoreOwner() {
                @NonNull
                @Override
                public ViewModelStore getViewModelStore() {
                    return null;
                }
            };
            ff.getViewModelStore();

            ll = new LifecycleOwner() {
                @NonNull
                @Override
                public Lifecycle getLifecycle() {
                    return null;
                }
            };

        }
    }

    protected void ToastOverride(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


}