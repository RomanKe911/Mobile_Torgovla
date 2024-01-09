package kg.roman.Mobile_Torgovla.ImagePack;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kg.roman.Mobile_Torgovla.MT_FTP.FTPWebhost;
import kg.roman.Mobile_Torgovla.ListSimple.State;
import kg.roman.Mobile_Torgovla.R;

public class UpdateImageOld extends AppCompatActivity {

  /* public ArrayList<ListAdapterSimple_Ftp_Image> ftp_image = new ArrayList<ListAdapterSimple_Ftp_Image>();
    public ListAdapterAde_Ftp_Image adapterFTP_Image;*/


    public ArrayList<ImagePack_R_Simple> ftp_image_new = new ArrayList<ImagePack_R_Simple>();
    public ImagePack_R_Adapter adapterListFTP;

    public ListView listView;
    public RecyclerView recyclerView;
    public TextView textView;
    public ProgressBar progressBar, progressBar2;
    public Button button, button_load;
    private int progressStatus = 0;
    private ImageView imageView, imageView2;
    private Handler handler = new Handler();
    public List<String> listFilesNoPhone;
    public SharedPreferences sp;
    public String PEREM_K_AG_KodRN, PEREM_SD, PEREM_PHONE, PEREM_DB3_RN;
  //  public SwipeRefreshLayout mSwipeRefreshLayout;
    Context context;
    ArrayList<State> states = new ArrayList<State>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perm_update_image);
       // listView = findViewById(R.id.UpdateImage_list);
        recyclerView = findViewById(R.id.UpdateImage_recyclear);
       /* textView = findViewById(R.id.UpdateImage_text);
        button = findViewById(R.id.UpdateImage_btn_List);
        button_load = findViewById(R.id.UpdateImage_btn_upload);
        progressBar = findViewById(R.id.Update_progressBar);
        imageView = findViewById(R.id.Update_ImageView);
        imageView2 = findViewById(R.id.Update_ImageView2);*/
        context = UpdateImageOld.this;

        progressBar.setVisibility(View.INVISIBLE);

      //  mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.UpdateImage_SwipeRefresh);
      /*  mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorScheme(R.color.colors_ostatok_large, R.color.colors_ostatok_medium, R.color.colors_ostatok_small, R.color.colorsilver);*/

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FTPWebhost web = new FTPWebhost();
                web.ftp_server = "ftp.sunbell.webhost.kg";
                web.ftp_port = 21;
                web.ftp_user_name = "sunbell_siberica";
                web.ftp_password = "Roman911NFS";
                //web.put_toFiles = "/MT_Sunbell_Karakol/Image";
                web.put_toFiles = "/MT_Sunbell_Karakol/Image/Firm_Tradegof";
                // web.getListCatalogy();


                // 11/11/2023
                /*adapterFTP_Image = new ListAdapterAde_Ftp_Image(context, web.getListCatalogy());
                adapterFTP_Image.notifyDataSetChanged();
                listView.setAdapter(adapterFTP_Image);*/

                adapterListFTP = new ImagePack_R_Adapter(context, web.getListCatalogyImage(), null);
                adapterListFTP.notifyDataSetChanged();
                recyclerView.setAdapter(adapterListFTP);

               /* ftp_image.clear();
                for (int i = 0; i < 5; i++) {
                    ftp_image.add(new ListAdapterSimple_Ftp_Image("Brend " + i, "" + i + "", "Размер " + i, "Количество " + i, "Дата обновления +" + i, ""));
                }

                */
                /*
                listFilesNoPhone = new ArrayList<>();
                // listFilesNoPhone.addAll(web.getFileNotImageFTP(progressBar));
                Log.e("TAG", "_" + listFilesNoPhone.addAll(web.getFileNotImageFTP()));

                ArrayAdapter<String> adapter_listNoFiles = new ArrayAdapter<>(UpdateImage.this,
                        android.R.layout.simple_list_item_1, Collections.synchronizedList(listFilesNoPhone));
                adapter_listNoFiles.notifyDataSetChanged();
                listView.setAdapter(adapter_listNoFiles);
                */
            }
        });
       /* mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 11/11/2023
                *//*adapterFTP_Image.notifyDataSetChanged();
                listView.setAdapter(adapterFTP_Image);
                Log.e("TAG", "_ обновлен");
                mSwipeRefreshLayout.setRefreshing(false);*//*
                adapterListFTP.notifyDataSetChanged();
                recyclerView.setAdapter(adapterListFTP);
                Log.e("TAG", "_ обновлен");
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(
                  Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);*/

        button_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(VISIBLE);
                FTPWebhost web = new FTPWebhost();
                web.ftp_server = "ftp.sunbell.webhost.kg";
                web.ftp_port = 21;
                web.ftp_user_name = "sunbell_siberica";
                web.ftp_password = "Roman911NFS";
                web.put_toFiles = "/MT_Sunbell_Karakol/Image";

                String file_Phone = Environment.getExternalStorageDirectory().toString() + "/Price/XML/";
                // Путь к папке files приложения
                File files = new File(getFilesDir().getAbsolutePath());
                //  web.getLoadingFileImage(web.put_toFiles, file_Phone, listFilesNoPhone);
                //  web.getLoadingFileImage(web.put_toFiles, files.getPath(), listFilesNoPhone, progressBar, null);


                Log.e("TAG", "путь2: " + Environment.getDataDirectory().toString());
                Log.e("TAG", "путь3: " + Environment.getExternalStorageDirectory());
                listView.setAdapter(null);
            }
        });
    }


   // @Override
    protected void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Отменяем анимацию обновления
               // mSwipeRefreshLayout.setRefreshing(false);
                Log.e("TAG", "_ Отменяем");
            }
        }, 1000);
    }


    private void setInitialData() {

        states.add(new State("Бразилия", "Бразилиа", R.drawable.icon_korz));
        states.add(new State("Аргентина", "Буэнос-Айрес", R.drawable.icon_menu_bella));
        states.add(new State("Колумбия", "Богота", R.drawable.icon_menu_brait));
        states.add(new State("Уругвай", "Монтевидео", R.drawable.icon_menu_cotton_club));
        states.add(new State("Чили", "Сантьяго", R.drawable.icon_menu_kodak));
    }

    private class Expl extends Thread {
        Expl(String name) {
            super(name);
        }

        public void run() {
            Log.e("Thread", "Waiting...");
            try {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        FTPWebhost web = new FTPWebhost();
                        web.ftp_server = "ftp.sunbell.webhost.kg";
                        web.ftp_port = 21;
                        web.ftp_user_name = "sunbell_siberica";
                        web.ftp_password = "Roman911NFS";
                        web.put_toFiles = "/MT_Sunbell_Karakol/Image";
                        web.getFTP_TestConnect();
                        listFilesNoPhone.clear();
                        // listFilesNoPhone.addAll(web.getFileNotImageFTP());

                        if (isInterrupted()) Log.e("Thread1", "Поток приостановлен");
                        if (isAlive()) Log.e("Thread1", "Поток активен");
                        if (isDaemon()) Log.e("Thread1", "Поток закончен");
                    }
                });

            } catch (Exception e) {
                // Toast.makeText(context_Activity, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class MyThread implements Runnable {
        public void run() {
            //System.out.printf("%s started... \n", Thread.currentThread().getName());
            progressStatus = 0;
            while (progressStatus < 100) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar2.setProgress(progressStatus);
                        textView.setText("_" + progressStatus);
                        progressBar.setVisibility(VISIBLE);
                    }
                });
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    // System.out.println("Thread has been interrupted");
                    Log.e("Thread", "Thread has been interrupted");
                }
                progressStatus += 1;

                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("_привет поток");
                    }
                });
                progressBar.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(VISIBLE);

                    }
                });
            }

            // System.out.printf("%s finished... \n", Thread.currentThread().getName());

        }
    }


    private void TestString() {

       /* File imagePath_SD = new File(PEREM_SD + objects.get(pos).getImage() + ".png");//путь к изображению
        Uri imagePath_phone_base_1 = Uri.parse(PEREM_PHONE + objects.get(pos).getImage());*/
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        PEREM_SD = sp.getString("PEREM_IMAGE_PUT_SDCARD", "0");
        PEREM_PHONE = sp.getString("PEREM_IMAGE_PUT_PHONE", "0");


        File files = new File(getFilesDir().getAbsolutePath());
        String file_files = files.getPath() + "/Image/bg_bg000115.png";
        String file_Phone = Environment.getExternalStorageDirectory().toString() + "/Price/XML/bg_bg000116.png";

        Log.e("TAG", "путь1: " + file_files);
        Log.e("TAG", "путь2: " + file_Phone);
        Log.e("TAG", "путь3: " + PEREM_SD + "bg_bg000117.png");
        Log.e("TAG", "путь4: " + PEREM_PHONE + "bg_bg000117.png");

        // File imagePath_SD = new File(PEREM_SD + objects.get(pos).getImage() + ".png");//путь к изображению
        /*Uri imagePath_phone_base_1 = Uri.parse(PEREM_SD+"bg_bg000117.png");
        Uri imagePath_phone_base_2 = Uri.parse(PEREM_PHONE+"bg_bg000117.png");*/


        Picasso.get()
                .load(new File(file_files))
                // .placeholder(R.drawable.button_up) заглушку (placeholder)
                .error(R.drawable.no_image) //заглушку для ошибки
                .into(imageView);

        Picasso.get()
                .load(new File(getFilesDir().getAbsolutePath() + "/Image/bg_bg000120" +
                        ".png"))
                // .placeholder(R.drawable.button_up) заглушку (placeholder)
                .error(R.drawable.no_image) //заглушку для ошибки
                .into(imageView2);
    }


}