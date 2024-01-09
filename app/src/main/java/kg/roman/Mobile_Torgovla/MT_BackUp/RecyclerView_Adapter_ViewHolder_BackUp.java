package kg.roman.Mobile_Torgovla.MT_BackUp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
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
    Context context;
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    private SharedPreferences mSettings;
    private SharedPreferences.Editor editor;


    public RecyclerView_Adapter_ViewHolder_BackUp(Context context, List<RecyclerView_Simple_BackUp> backup_list, OnClickListenerClient onClickListener) {
        this.context = context;
        this.imagelist = backup_list;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_backup, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        RecyclerView_Simple_BackUp listID = imagelist.get(position);
        holder.backup_agentname.setText(listID.getBackUp_AgentName());
        holder.backup_agentregion.setText(listID.getBackUp_AgentRegion());
        holder.backup_size_rndb.setText(listID.getBackUp_RNDB_Size());
        holder.backup_size_basedb.setText(listID.getBackUp_BASEDB_Size());
        holder.backup_size_constdb.setText(listID.getBackUp_CONSTDB_Size());

        holder.backup_button_rbdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("BackUp", "Скачать базу данных заказы");
                getFtp();
            }
        });

        holder.backup_button_constdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("BackUp", "Скачать базу данных настройки");
            }
        });

        holder.backup_button_basedb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("BackUp", "Скачать базу данных параметры");
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagelist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView backup_agentname, backup_agentregion, backup_size_rndb, backup_size_basedb, backup_size_constdb;
        final Button backup_button_rbdb, backup_button_constdb, backup_button_basedb;
        final ProgressBar progressBar;

        ViewHolder(View view) {
            super(view);
            backup_agentname = view.findViewById(R.id.textView_backup_text_agent);
            backup_agentregion = view.findViewById(R.id.textView_backup_text_region);
            backup_size_rndb = view.findViewById(R.id.textView_backup_textsize_01);
            backup_size_basedb = view.findViewById(R.id.textView_backup_textsize_02);
            backup_size_constdb = view.findViewById(R.id.textView_backup_textsize_03);

            backup_button_rbdb = (Button) view.findViewById(R.id.button_backup_rndb);
            backup_button_constdb = (Button) view.findViewById(R.id.button_backup_constdb);
            backup_button_basedb = (Button) view.findViewById(R.id.button_backup_basedb);

            progressBar = (ProgressBar) view.findViewById(R.id.progressBar_backup);
        }
    }

    protected void getFtp() {
        FTPWebhost web = new FTPWebhost();
        FtpConnectData ftpConnectData = new FtpConnectData();
/*        for (String filename :ftpConnectData.mass_file_backup)
        {
            Log.e("FileSize", "getFileSize: "+filename);
            if (filename.contains("sunbell_const_db"))
            {

            }

        }*/
        Log.e("FileSize", web.getFilesSize("/MT_Sunbell_Karakol/MTW_SOS/04:01:2024 21:05:25_sunbell_rn_db_bezmenova_natalija_petrovna.db3"));

//web.getFilesSize("/MT_Sunbell_Karakol/SOS/01:06:2023 16:18:28_sunbell_base_db_bezmenova_natalija_petrovna.db3");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String nameAgent = "bezmenova_natalija_petrovna";
                String putFile = "/MT_Sunbell_Karakol/MTW_SOS/";
                web.getListFile_BackUp(nameAgent, putFile);
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
/*        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/




    }


}