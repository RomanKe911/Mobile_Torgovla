package kg.roman.Mobile_Torgovla.ImagePack;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import kg.roman.Mobile_Torgovla.MT_FTP.FTPWebhost;
import kg.roman.Mobile_Torgovla.MT_FTP.FtpConnectData;
import kg.roman.Mobile_Torgovla.Permission.PermissionUtils;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.databinding.PermUpdateImageBinding;

public class UpdateImage extends AppCompatActivity {
    // public ArrayList<ImagePack_R_Simple> ftp_image_new = new ArrayList<ImagePack_R_Simple>();
    public ImagePack_R_Adapter adapterListFTP;
    final Handler handler = new Handler();
    public List<String> listFilesNoPhone;
    public String PEREM_K_AG_KodRN, PEREM_SD, PEREM_PHONE, PEREM_DB3_RN;

    Context context;

    private PermUpdateImageBinding binding;
    private SharedPreferences.Editor ed;
    private SharedPreferences sp;
    private final HashSet<String> hss = new HashSet<String>();
    private static final int PERMISSION_STORAGE = 101;
    public MyViewModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PermUpdateImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon_image);
        getSupportActionBar().setTitle("Графическая база");
        getSupportActionBar().setSubtitle("обновление картинок");
        context = UpdateImage.this;
        PermissionFileRead();

        binding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ViewModels();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Отменяем анимацию обновления
                        binding.swiperefresh.setRefreshing(false);

                       /* Random random = new Random();
                        mCatTextView.setText("Котика пора кормить. Его не кормили уже "
                                + (1 + random.nextInt(10)) + " мин.");*/
                    }
                }, 4000);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_updateimage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_button_update: {
                /*                try {
                 *//*  if (UpdateList() != null) {
                        adapterListFTP = new ImagePack_R_Adapter(context, UpdateList(), null);
                        adapterListFTP.notifyDataSetChanged();
                        binding.UpdateImageRecyclear.setAdapter(adapterListFTP);
                        // model.execute2();
                    } else {
                        SnackbarOverride("Загрузка списка не возможна, проверьте подключение к интернету!");
                    }*//*
                    // adapterListFTP = new ImagePack_R_Adapter(context, null, null);


                    ArrayList<ImagePack_R_Simple> ftp_image_new = new ArrayList<ImagePack_R_Simple>();
                    ftp_image_new.clear();
                    adapterListFTP = new ImagePack_R_Adapter(context, ftp_image_new, null);
                    binding.UpdateImageRecyclear.setAdapter(adapterListFTP);
                    adapterListFTP.notifyDataSetChanged();

                    model = new ViewModelProvider(this).get(MyViewModel.class);
                    model.getValues().observe(this, ftp_image_newRun ->
                    {
                        adapterListFTP = new ImagePack_R_Adapter(context, ftp_image_newRun, null);
                        binding.UpdateImageRecyclear.setAdapter(adapterListFTP);

                        adapterListFTP.notifyItemChanged(0, 0);
                        SnackbarOverride("Статус: " + ftp_image_newRun);
                    });
                    model.execute2();
                } catch (Exception e) {
                    SnackbarOverride("Краш, не возможно отобразить информацию");
                    Log.e(this.getLocalClassName(), "Краш, не возможно отобразить информацию");
                }*/

                FTPWebhost ft = new FTPWebhost();
                if (ft.getInternetConnect(context)) {
                    Toast.makeText(this, "Интернет активен!", Toast.LENGTH_LONG).show();
                    if (ft.getFTP_TestConnect().second) {
                        Toast.makeText(context, ft.getFTP_TestConnect().first, Toast.LENGTH_SHORT).show(); // Коннект FTP
                        ViewModels();
                    } else
                        Toast.makeText(context, ft.getFTP_TestConnect().first, Toast.LENGTH_SHORT).show(); // // Нет Коннект FTP
                } else
                    Toast.makeText(this, "Нет доступа к сети интернет!", Toast.LENGTH_LONG).show();



            }
            break;
            case R.id.menu_button_clearAll: {
             /*   SnackbarOverride("Очистить все!");

                  String brend = "TZMO";
                    String prefix = "bl";
                HashMap<String, String> hsspref = new HashMap<>();
                hsspref.put("bl", "bl_TZMO");
                hsspref.put("bx", "bx_Barhim");
                hsspref.put("it", "it_ITEX");
                hsspref.put("mf", "mf_MalbiFuds");
                hsspref.put("pd", "pd_President");

                for (Map.Entry<String, String> itemspr : hsspref.entrySet()) {
                    Log.e("START", "Бренд: " + itemspr.getValue().substring(3));
                    String brend = itemspr.getValue();
                    String prefix = itemspr.getKey();

                    HashSet<String> phone = getListFilesPhone(prefix);
                    HashSet<String> ftp = getListFilesFTP(brend);

                    HashMap<String, Long> hasmap = getListFilesFTPSize(brend);
                    HashMap<String, Long> hasmap_phone = getListFilesPhone_Size(prefix);

                    if (phone != null && ftp != null && !phone.isEmpty() && !ftp.isEmpty()) {
                   for (String f : getListFilesFTP(brend)) {
                        Log.e("Список файлов из ftp: ", f);
                    }

                    for (String p : getListFilesPhone(prefix))
                        Log.e("Список файлов из телефона: ", p);


                    for (int i = 0; i < ftp.size(); i++)
                        if (!phone.contains(ftp.toArray()[i]))
                            Log.e("Phone: ", "Список файлов для скачивания: " + ftp.toArray()[i].toString());

                    for (int i = 0; i < phone.size(); i++)
                        if (!ftp.contains(phone.toArray()[i]))
                            Log.e("Phone: ", "Файлы для удаления: " + phone.toArray()[i]);

                        for (Map.Entry<String, Long> items : hasmap.entrySet()) {
                            Log.e("FTP: ", "FileName: " + items.getKey() + " Date: " + getFullTime(items.getValue()));
                        }
                        for (Map.Entry<String, Long> items : hasmap_phone.entrySet()) {
                            Log.e("Phone: ", "FileName: " + items.getKey() + " Date: " + getFullTime(items.getValue()));
                        }

                        boolean myBoolUpdate = false, myBoolDelete = false;

                        for (int i = 0; i < hasmap.size(); i++) {
                            if (!hasmap_phone.containsKey(hasmap.keySet().toArray()[i])) {
                                myBoolUpdate = true;
                                Log.e("HashMap: ", "Файлы для скачивания: " + hasmap.keySet().toArray()[i].toString() + "_" + myBoolUpdate);
                            }
                        }

                        for (int i = 0; i < hasmap_phone.size(); i++)
                            if (!hasmap.containsKey(hasmap_phone.keySet().toArray()[i])) {
                                myBoolDelete = true;
                                Log.e("HashMap: ", "Файлы на удаление: " + hasmap_phone.keySet().toArray()[i].toString() + "_" + myBoolDelete);

                            }


                        if (myBoolUpdate == true || myBoolDelete == true) {
                            Log.e("Boolean: ", "есть обновление");
                        } else {
                            Log.e("Boolean: ", "нет новых файлов, но есть изменения");
                            for (int i = 0; i < hasmap.size(); i++) {
                                if (Long.valueOf(hasmap.values().toArray()[i].toString()) > Long.valueOf(hasmap_phone.values().toArray()[i].toString())) {
                                    Log.e("Список для обновления: ", hasmap.keySet().toArray()[i].toString() + "__" + hasmap.values().toArray()[i].toString());
                                }
                            }
                        }


                    } else if (phone == null && ftp != null) {
                        for (int i = 0; i < hasmap.size(); i++) {
                            Log.e("HashMap: ", "Файлы для скачивания: " + hasmap.keySet().toArray()[i].toString());
                        }
                    } else Log.e("HashMap: ", "нет файлов для скачивания");



               else if (phone == null)
                    Log.e("Список", "Список файлов для скачивания: FTP " + ftp);
                else Log.e("Список", "Файлы на удалнение" + phone);

                if (ftp == null) Log.e("Список", "нет файлов для скачивания!");
                    //  if (phone == null) Log.e("Список", "Список файлов для скачивания: " + "Phone " + phone + "__" + "FTP " + ftp);
                }
            }

*/
                ////////////////////



                /*
                model = new ViewModelProvider(this).get(MyViewModel.class);
                model.getValues().observe(this, ftp_image_newRun ->
                {
                    adapterListFTP = new ImagePack_R_Adapter(context, ftp_image_newRun, null);
                    binding.UpdateImageRecyclear.setAdapter(adapterListFTP);

                    adapterListFTP.notifyItemChanged(0, 0);
                    SnackbarOverride("Статус: " + ftp_image_newRun);
                });
                model.execute2();
*/




               /* binding.UpdateImageProgressBar.setVisibility(View.VISIBLE);
                model_iamge = new ViewModelProvider(this).get(ViewModel_forProgresBar.class);
                model_iamge.getProgressState().observe(this, false ->
                {
                    if (model_iamge.getProgressState().getValue()) {
                        binding.UpdateImageProgressBar.setVisibility(View.VISIBLE);
                        Log.e("ViewHolder", "Статус: Visible"+model_iamge.getProgressState().getValue());

                    } else {
                        binding.UpdateImageProgressBar.setVisibility(View.INVISIBLE);
                        Log.e("ViewHolder", "Статус: Invisible"+model_iamge.getProgressState().getValue());
                    }
                });*/


                ////////// ViewHolder с логической переменной
                /*binding.UpdateImageProgressBar.setVisibility(View.VISIBLE);
                model_iamge = new ViewModelProvider(this).get(ViewModel_forProgresBar.class);
                model_iamge.getProgressState().observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        if (aBoolean) {
                            binding.UpdateImageProgressBar.setVisibility(View.VISIBLE);
                            Log.e("ViewHolder", "Статус: Visible "+model_iamge.getProgressState().getValue());
                        } else {
                            binding.UpdateImageProgressBar.setVisibility(View.INVISIBLE);
                            Log.e("ViewHolder", "Статус: Invisible "+model_iamge.getProgressState().getValue());
                        }
                    }
                });
                model_iamge.doSomeThing();*/





                break;

            }
        }

        return super.onOptionsItemSelected(item);
    }

    // Класс для отображения состояния
    protected void SnackbarOverride(String text) {
        Snackbar.make(binding.UpdateConstraintLayout, text, Snackbar.LENGTH_SHORT)
                .show();
    }

    // Проверка на наличии директории для сохранения картинок
    protected void NewFileDirt_forImage() {
        File files = new File(context.getFilesDir().getAbsolutePath()); // files.getPath() + "/Icons/bg_bg000115.png";
        String[] massDirt = getResources().getStringArray(R.array.MassDistrFiles_forImage);
        for (int i = 0; i < massDirt.length; i++) {
            File file_reg = new File(files.getPath() + "/" + massDirt[i]);
            if (!file_reg.exists()) {
                file_reg.mkdir();
                SnackbarOverride("Успешно созданны директории для картинок");
            }
        }
    }

    // Класс для проверки на доступ к файловой системе
    protected void PermissionFileRead() {
        if (!PermissionUtils.hasPermissions(context)) {
            SnackbarOverride("Разрешение не полученно, получите разрешение на доступ к файловой системе");
            PermissionUtils.requestPermissions(UpdateImage.this, PERMISSION_STORAGE);
        } else {
            //  SnackbarOverride("Разрешение True");
            NewFileDirt_forImage();
        }
    }

    // Класс для создания списка для адаптера
    protected ArrayList<ImagePack_R_Simple> UpdateList() {
        FTPWebhost web = new FTPWebhost();
        FtpConnectData c = new FtpConnectData();
        web.ftp_server = c.server_name;
        web.ftp_port = c.port;
        web.ftp_user_name = c.server_username;
        web.ftp_password = c.server_password;
        web.put_toFiles = c.put_toFtpImageTradegof;
      /*  web.ftp_server = "ftp.sunbell.webhost.kg";
        web.ftp_port = 21;
        web.ftp_user_name = "sunbell_siberica";
        web.ftp_password = "Roman911NFS";
                web.put_toFiles = "/MT_Sunbell_Karakol/Image/Firm_Tradegof";*/
        //web.put_toFiles = "/MT_Sunbell_Karakol/Image";
        if (!web.getListCatalogyImage().isEmpty())
            return web.getListCatalogyImage();
        else
            return null;
    }


    protected String getFullTime(long timeInMillis) {
        final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeInMillis);
        c.setTimeZone(TimeZone.getDefault());
        return format.format(c.getTime());
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
                        // web.FTP_CONNECT();
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

    protected void ViewModels() {
        try {
            ArrayList<ImagePack_R_Simple> ftp_image_new = new ArrayList<ImagePack_R_Simple>();
            ftp_image_new.clear();
            adapterListFTP = new ImagePack_R_Adapter(context, ftp_image_new, null);
            binding.UpdateImageRecyclear.setAdapter(adapterListFTP);
            adapterListFTP.notifyDataSetChanged();

            model = new ViewModelProvider(this).get(MyViewModel.class);
            model.getValues().observe(this, ftp_image_newRun ->
            {
                adapterListFTP = new ImagePack_R_Adapter(context, ftp_image_newRun, null);
                binding.UpdateImageRecyclear.setAdapter(adapterListFTP);

                adapterListFTP.notifyItemChanged(0, 0);
                // Log.e("ViewHolder", ftp_image_newRun.toString());
               // SnackbarOverride("Статус: список обновлен");
            });
            model.execute2();


        } catch (Exception e) {
            SnackbarOverride("Краш, не возможно отобразить информацию");
            Log.e(this.getLocalClassName(), "Краш, не возможно отобразить информацию");
        }
    }


    ////// Создаем изключения для скачивания нужных файлов

    ////// Создания списка файлов на телефоне, по бренду и префиксу
    protected HashSet<String> getListFilesPhone(String prefix) {
        HashSet<String> hass = new HashSet<>();
        File files = new File(context.getFilesDir().getAbsolutePath());
        File f = new File(files.getPath() + "/Image/");
        File[] list = f.listFiles();
        for (int i = 0; i < list.length; i++) {
            if (list[i].getName().substring(0, 2).equals(prefix)) {
                hass.add(list[i].getName());
                Log.e("Date", list[i].getName() + " Дата последнего изменения:" + getFullTime(list[i].lastModified()));
            }
        }
        if (!hass.isEmpty()) return hass;
        else return null;
    }

    protected HashMap<String, Long> getListFilesPhone_Size(String prefix) {
        HashMap<String, Long> hamap = new HashMap<String, Long>();
        File files = new File(context.getFilesDir().getAbsolutePath());
        File f = new File(files.getPath() + "/Image/");
        File[] list = f.listFiles();
        for (int i = 0; i < list.length; i++)
            if (list[i].getName().substring(0, 2).equals(prefix))
                hamap.put(list[i].getName(), list[i].lastModified());
        if (!hamap.isEmpty()) return hamap;
        else return null;
    }

    ////// Создания списка файлов на ftp, по бренду
    protected HashSet<String> getListFilesFTP(String brend) {
        FTPWebhost web = new FTPWebhost();
        if (!web.getListBrendImage(brend).isEmpty())
            return web.getListBrendImage(brend);
        else return null;
    }

    protected HashMap<String, Long> getListFilesFTPSize(String brend) {
        FTPWebhost web = new FTPWebhost();
        if (!web.getListBrendImage_Size(brend, context).isEmpty())
            return web.getListBrendImage_Size(brend, context);
        else return null;
    }

}