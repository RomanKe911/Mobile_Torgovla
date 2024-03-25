package kg.roman.Mobile_Torgovla.FormaZakazaSelectClient;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar.ListAdapterSimple_Klients;
import kg.roman.Mobile_Torgovla.MT_FTP.PreferencesWrite;

public class Async_ViewModel_Clients extends AndroidViewModel {

    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    SharedPreferences mSettings;

    String logeTAG = "ViewModel_Clients";

    public Async_ViewModel_Clients(@NonNull Application application) {
        super(application);
        // Context context = getApplication().getApplicationContext();
    }


    private MutableLiveData<ArrayList<ListAdapterSimple_Klients>> livedata_simple;

    public LiveData<ArrayList<ListAdapterSimple_Klients>> getValues() {
        if (livedata_simple == null)
            livedata_simple = new MutableLiveData<>();
        return livedata_simple;
    }

    /////////////////////////
    private MutableLiveData<String> messegeStatus = new MutableLiveData<>("");

    public LiveData<String> getMessegeStatus() {
        if (messegeStatus == null)
            messegeStatus = new MutableLiveData<>(null);
        return messegeStatus;
    }
    ////////////////////////


    ////////////////////////
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public LiveData<Boolean> getLoadingStatus() {
        if (isLoading == null)
            isLoading = new MutableLiveData<>(false);
        return isLoading;
    }
    ////////////////////////

    public void execute() {
        ArrayList<ListAdapterSimple_Klients> arrayList_clients = new ArrayList<>();
        isLoading.postValue(true);
        Runnable runnable = () -> {
            try {

                mSettings = getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

                String agent, region;
                agent = mSettings.getString("PEREM_AG_UID", "");  // Переменая номер накладной
                region = mSettings.getString("PEREM_AG_UID_REGION", "");  // Переменая номер накладной

                Log.e(logeTAG, "Загрузка потока данными");
                //  pDialog.setMessage("Загрузка продуктов. Подождите...");

                SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase("sunbell_const_db.db3", MODE_PRIVATE, null);
                String query = "SELECT * FROM const_contragents WHERE uid_agent = '" + agent + "' AND roaduid = '" + region + "'";
                final Cursor cursor = db.rawQuery(query, null);
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    String uid = cursor.getString(cursor.getColumnIndexOrThrow("uid_k_agent")); // код клиента
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("k_agent"));
                    String adress = cursor.getString(cursor.getColumnIndexOrThrow("adress"));
                    String roadname = cursor.getString(cursor.getColumnIndexOrThrow("roadname")); // имя группы
                    String roadUid = cursor.getString(cursor.getColumnIndexOrThrow("roaduid")); // код папки группы
                    //  String userUid = cursor.getString(cursor.getColumnIndex("")); // код агента
                    //  int resID = getResources().getIdentifier(IMAGE, "drawable", getPackageName());
/*                    image_var = name.substring(0, 2);
                    String name_syb = name.substring(0, 3);
                    switch (name_syb) {
                        case "маг":
                            resID = getResources().getIdentifier("user_shop_01", "drawable", getPackageName());
                            break; // магазин
                        case "+ма":
                            resID = getResources().getIdentifier("user_shop_01", "drawable", getPackageName());
                            break; // магазин
                        case "++м":
                            resID = getResources().getIdentifier("user_shop_01", "drawable", getPackageName());
                            break; // магазин
                        case "мар":
                            resID = getResources().getIdentifier("user_shop_04", "drawable", getPackageName());
                            break; // магазин
                        case "апт":
                            resID = getResources().getIdentifier("user_shop_02", "drawable", getPackageName());
                            break; // аптека
                        case "ко":
                            resID = getResources().getIdentifier("user_shop_03", "drawable", getPackageName());
                            break; // контейнер
                        case "пав":
                            resID = getResources().getIdentifier("user_shop_03", "drawable", getPackageName());
                            break; // контейнер
                        case "р-к":
                            resID = getResources().getIdentifier("user_shop_03", "drawable", getPackageName());
                            break; // контейнер
                        case "го":
                            resID = getResources().getIdentifier("user_shop_06", "drawable", getPackageName());
                            break; // гостиница
                        case "с. ":
                            resID = getResources().getIdentifier("user_shop_07", "drawable", getPackageName());
                            break; // гостиница
                        case "г. ":
                            resID = getResources().getIdentifier("user_shop_08", "drawable", getPackageName());
                            break; // гостиница
                        default:
                            resID = getResources().getIdentifier("user_shop_05", "drawable", getPackageName());
                            break; // контейнер
                    }*/
                    Integer im = getApplication().getResources().getIdentifier("user_shop_05", "drawable", getApplication().getPackageName());
                    arrayList_clients.add(new ListAdapterSimple_Klients(name, uid, adress, im, SQL_Debet_List(uid)));
                    livedata_simple.postValue(arrayList_clients);
                    cursor.moveToNext();
                    Thread.sleep(50);
                }
                cursor.close();
                db.close();
                Log.e(logeTAG, "Загрузка потока данными, конец");
            } catch (Exception es) {
                es.printStackTrace();
                Log.e(logeTAG, "Ошибка, не возможно отобразить список данных");
                messegeStatus.postValue("Ошибка, не возможно отобразить список данных");
/*                backup_list.clear();
                backup_livedata.postValue(backup_list);*/
            }

            isLoading.postValue(false);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    protected String SQL_Debet_List(String l_uid) {
        String debet = "";
        try {
            PreferencesWrite preferencesWrite = new PreferencesWrite(getApplication());

            SQLiteDatabase db_debet = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
            String query_debet = "SELECT * FROM otchet_debet WHERE d_kontr_uid = '" + l_uid + "' AND d_summa > 0;";
            final Cursor cursor_debet = db_debet.rawQuery(query_debet, null);
            cursor_debet.moveToFirst();
            if (cursor_debet.getCount() > 0 & cursor_debet != null) {
                String summa = cursor_debet.getString(cursor_debet.getColumnIndexOrThrow("d_summa")); // код клиента
                debet = summa;
            } else debet = "нет задолжностей";
            cursor_debet.close();
            db_debet.close();
        } catch (Exception e) {
            Toast.makeText(getApplication(), "Ошибка задолжностей", Toast.LENGTH_SHORT).show();
            Log.e(logeTAG, "Ошибка задолжностей");
            debet = "ОШИБКА ЗАГРУЗКА ДОЛГОВ";
        }

        return debet;
    }


}