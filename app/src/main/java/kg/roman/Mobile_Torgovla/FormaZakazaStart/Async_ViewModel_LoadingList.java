package kg.roman.Mobile_Torgovla.FormaZakazaStart;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.apache.commons.net.ftp.FTPClient;

import java.io.InputStream;
import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterAde_List_RN_Table;
import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_List_RN_Table;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Suncape_Forma;
import kg.roman.Mobile_Torgovla.MT_FTP.CalendarThis;
import kg.roman.Mobile_Torgovla.MT_FTP.FtpConnectData;
import kg.roman.Mobile_Torgovla.MT_FTP.PreferencesWrite;

public class Async_ViewModel_LoadingList extends AndroidViewModel {
    String logeTAG = "ViewModel_LoadingList";
    PreferencesWrite preferencesWrite = new PreferencesWrite(getApplication());
    CalendarThis calendars = new CalendarThis();


    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    SharedPreferences mSettings;

    public Async_ViewModel_LoadingList(@NonNull Application application) {
        super(application);
        // Context context = getApplication().getApplicationContext();
    }

    ////////////////////////
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public LiveData<Boolean> getLoadingStatus() {
        if (isLoading == null)
            isLoading = new MutableLiveData<>(false);
        return isLoading;
    }

    ////////////////////////
    private MutableLiveData<ArrayList<ListAdapterSimple_List_RN_Table>> livedata_Zakaz;

    public LiveData<ArrayList<ListAdapterSimple_List_RN_Table>> getValues() {
        if (livedata_Zakaz == null)
            livedata_Zakaz = new MutableLiveData<>();
        return livedata_Zakaz;
    }

    public void execute() {
        ArrayList<ListAdapterSimple_List_RN_Table> arrayList_zakaz = new ArrayList<>();
        isLoading.postValue(false);

        Runnable runnable = () -> {
            preferencesWrite = new PreferencesWrite(getApplication().getBaseContext());
            if (!preferencesWrite.Setting_FiltersSelectGroup)
                livedata_Zakaz.postValue(selectGroupNull(arrayList_zakaz));
            else livedata_Zakaz.postValue(selectGroup(arrayList_zakaz));

/*            try {
                Log.e(logeTAG, "Cliets " + preferencesWrite.Setting_FiltersSelectClients);
                Log.e(logeTAG, "Date " + preferencesWrite.Setting_FiltersSelectDate);
                Log.e(logeTAG, "Group " + preferencesWrite.Setting_FiltersSelectGroup);
                String query = "";

                SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
                FiltersFormaZakaza filtersFormaZakaza = new FiltersFormaZakaza(getApplication().getBaseContext());

                Log.e(logeTAG, "Loading_List_Filter");


*//*            if (preferencesWrite.PEREM_ANDROID_ID_ADMIN.equals("d5781f21963ff5"))
                query = filtersFormaZakaza.filterAdmin();
            else {

            }*//*

                //// Данные без фильтров
                query = filtersFormaZakaza.filterNull();
                //// Фильриция по Клиенту
                if (preferencesWrite.Setting_FiltersSelectClients)
                    query = filtersFormaZakaza.filterClient();
                //// Фильтрация по дате
                if (preferencesWrite.Setting_FiltersSelectDate)
                    query = filtersFormaZakaza.filterDate();
                //// Фильтрицмя по дате и клиентам
                if (preferencesWrite.Setting_FiltersSelectDate && preferencesWrite.Setting_FiltersSelectClients)
                    query = filtersFormaZakaza.filterAll();


                final Cursor cursor = db.rawQuery(query, null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String Kod_RN = cursor.getString(cursor.getColumnIndexOrThrow("kod_rn"));
                    String Klients = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_name"));
                    String UID_Klients = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_uid"));
                    String Adress = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_adress"));
                    String Vrema = cursor.getString(cursor.getColumnIndexOrThrow("vrema"));
                    String Data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
                    String Summa = cursor.getString(cursor.getColumnIndexOrThrow("summa"));
                    String Itogo = cursor.getString(cursor.getColumnIndexOrThrow("itogo"));
                    String skidka = cursor.getString(cursor.getColumnIndexOrThrow("skidka")); // добавить обработку скидки
                    String data_up = cursor.getString(cursor.getColumnIndexOrThrow("data_up")); // добавить обработку дата отгрузки
                    String status = cursor.getString(cursor.getColumnIndexOrThrow("status")); // добавить обработку дата отгрузки
                    String debet = cursor.getString(cursor.getColumnIndexOrThrow("debet_new")); // добавить обработку дата отгрузки
                    String sklad = cursor.getString(cursor.getColumnIndexOrThrow("sklad")); // добавить обработку дата отгрузки
                    arrayList_zakaz.add(new ListAdapterSimple_List_RN_Table(Kod_RN, Klients, UID_Klients, Vrema, Data, Summa, Itogo, Adress, skidka, debet, status, sklad));
                    Log.e(logeTAG, "cursor: "+cursor.getPosition());
                    cursor.moveToNext();
                }

                Log.e(logeTAG, "zakaz обновлен");

                cursor.close();
                db.close();

              // Thread.sleep(1000);
                livedata_Zakaz.postValue(arrayList_zakaz);

            } catch (SQLiteException sqLiteException) {
                Log.e(logeTAG, "Loading_List_Filter: " + sqLiteException);
            } catch (Exception ex) {
                Log.e(logeTAG, "Exception: " + ex);
            }*/


        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    protected ArrayList<ListAdapterSimple_List_RN_Table> selectGroupNull(ArrayList<ListAdapterSimple_List_RN_Table> arrayList_zakaz) {

        try {
            Log.e(logeTAG, "Cliets " + preferencesWrite.Setting_FiltersSelectClients);
            Log.e(logeTAG, "Date " + preferencesWrite.Setting_FiltersSelectDate);
            Log.e(logeTAG, "Group " + preferencesWrite.Setting_FiltersSelectGroup);
            String query = "";

            SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
            FiltersFormaZakaza filtersFormaZakaza = new FiltersFormaZakaza(getApplication().getBaseContext());

            Log.e(logeTAG, "Loading_List_Filter");


/*            if (preferencesWrite.PEREM_ANDROID_ID_ADMIN.equals("d5781f21963ff5"))
                query = filtersFormaZakaza.filterAdmin();
            else {

            }*/

            //// Данные без фильтров
            query = filtersFormaZakaza.filterNull();
            //// Фильриция по Клиенту
            if (preferencesWrite.Setting_FiltersSelectClients)
                query = filtersFormaZakaza.filterClient();
            //// Фильтрация по дате
            if (preferencesWrite.Setting_FiltersSelectDate)
                query = filtersFormaZakaza.filterDate();
            //// Фильтрицмя по дате и клиентам
            if (preferencesWrite.Setting_FiltersSelectDate && preferencesWrite.Setting_FiltersSelectClients)
                query = filtersFormaZakaza.filterAll();


            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String Kod_RN = cursor.getString(cursor.getColumnIndexOrThrow("kod_rn"));
                String Klients = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_name"));
                String UID_Klients = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_uid"));
                String Adress = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_adress"));
                String Vrema = cursor.getString(cursor.getColumnIndexOrThrow("vrema"));
                String Data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
                String Summa = cursor.getString(cursor.getColumnIndexOrThrow("summa"));
                String Itogo = cursor.getString(cursor.getColumnIndexOrThrow("itogo"));
                String skidka = cursor.getString(cursor.getColumnIndexOrThrow("skidka")); // добавить обработку скидки
                String data_up = cursor.getString(cursor.getColumnIndexOrThrow("data_up")); // добавить обработку дата отгрузки
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status")); // добавить обработку дата отгрузки
                String debet = cursor.getString(cursor.getColumnIndexOrThrow("debet_new")); // добавить обработку дата отгрузки
                String sklad = cursor.getString(cursor.getColumnIndexOrThrow("sklad")); // добавить обработку дата отгрузки
                arrayList_zakaz.add(new ListAdapterSimple_List_RN_Table(Kod_RN, Klients, UID_Klients, Vrema, Data, Summa, Itogo, Adress, skidka, debet, status, sklad));
                cursor.moveToNext();
            }
            Log.e(logeTAG, "zakaz обновлен");


            cursor.close();
            db.close();
        } catch (SQLiteException sqLiteException) {
            Log.e(logeTAG, "Loading_List_Filter: " + sqLiteException);
        } catch (Exception ex) {
            Log.e(logeTAG, "Exception: " + ex);
        }
        return arrayList_zakaz;
    }

    protected ArrayList<ListAdapterSimple_List_RN_Table> selectGroup(ArrayList<ListAdapterSimple_List_RN_Table> arrayList_zakaz) {

        try {
            Log.e(logeTAG, "Cliets " + preferencesWrite.Setting_FiltersSelectClients);
            Log.e(logeTAG, "Date " + preferencesWrite.Setting_FiltersSelectDate);
            Log.e(logeTAG, "Group " + preferencesWrite.Setting_FiltersSelectGroup);

            SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
            FiltersFormaZakaza filtersFormaZakaza = new FiltersFormaZakaza(getApplication().getBaseContext());

            Log.e(logeTAG, "Loading_List_FilterGroup: " + filtersFormaZakaza.filterGroup());

            String query = filtersFormaZakaza.filterGroup();

            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String agentName = cursor.getString(cursor.getColumnIndexOrThrow("agent_name"));
                String agentUID = cursor.getString(cursor.getColumnIndexOrThrow("agent_uid"));
                String clientUID = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_uid"));
                String clientName = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_name"));
                String clientAdress = cursor.getString(cursor.getColumnIndexOrThrow("k_agn_adress"));
                String Date = cursor.getString(cursor.getColumnIndexOrThrow("data"));
                String Itogo = cursor.getString(cursor.getColumnIndexOrThrow("itogo"));
                arrayList_zakaz.add(new ListAdapterSimple_List_RN_Table("", clientName, clientUID, "", Date, "", Itogo, clientAdress, "", "", "", ""));
                cursor.moveToNext();
            }
            Log.e(logeTAG, "zakaz обновлен");


            cursor.close();
            db.close();
        } catch (SQLiteException sqLiteException) {
            Log.e(logeTAG, "Loading_List_Filter: " + sqLiteException);
        } catch (Exception ex) {
            Log.e(logeTAG, "Exception: " + ex);
        }
        return arrayList_zakaz;
    }
}