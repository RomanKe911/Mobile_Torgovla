package kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar;

import static android.content.Context.MODE_PRIVATE;

import static java.lang.Boolean.FALSE;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.DecimalFormat;
import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Suncape_Forma;

public class Async_ViewModel_Nomenclature extends AndroidViewModel {

    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    SharedPreferences mSettings;

    String logeTAG = "ViewModel_Clients";
    String select_menu;

    public Async_ViewModel_Nomenclature(@NonNull Application application) {
        super(application);
        // Context context = getApplication().getApplicationContext();
    }


    private MutableLiveData<ArrayList<ListAdapterSimple_Suncape_Forma>> livedata_simple;

    public LiveData<ArrayList<ListAdapterSimple_Suncape_Forma>> getValues(String perem) {
        select_menu = perem;
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
        ArrayList<ListAdapterSimple_Suncape_Forma> arrayList_tovar = new ArrayList<>();
        isLoading.postValue(true);
        Runnable runnable = () -> {
            try {
                mSettings = getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                String mSettings_base_Base = mSettings.getString("PEREM_DB3_BASE", "_"); // получение из preference: имя агента
                Boolean perem_switch_group_sql = mSettings.getBoolean("switch_preference_group_sql", FALSE);             //чтение данных: координаты gps
                String sp_BREND = mSettings.getString("sp_BREND", "null");          //чтение данных: Универсальный номер пользователя


/*                ed = sp.edit();
                ed.putString("PEREM_SELECT_BRENDS", id_st);
                ed.commit();
                id_st = topChannelMenu.getItem(0).toString();
                Log.e("TEST", "PEREM_SELECT_BRENDS=" + id_st + ".");*/

                Log.e("BRENDS", select_menu);
                //  pDialog.setMessage("Загрузка продуктов. Подождите...");
                SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(mSettings_base_Base, MODE_PRIVATE, null);
                String query;
                if (perem_switch_group_sql) {
                    query = "SELECT base_in_nomeclature.name, base_in_nomeclature.brends, base_in_nomeclature.p_group, base_in_nomeclature.kod, base_in_image.kod_image, \n" +
                            "base_in_nomeclature.kolbox, base_in_ostatok.count, base_in_ostatok.sklad_uid, base_in_ostatok.sklad_uid, const_sklad.sklad_name,\n" +
                            "base_in_price.price, base_in_nomeclature.strih, base_in_nomeclature.kod_univ, base_in_nomeclature.koduid\n" +
                            "FROM base_in_nomeclature\n" +
                            "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                            "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                            "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                            "LEFT JOIN base_group_sql ON base_in_nomeclature.koduid = base_group_sql.uid_name\n" +
                            "LEFT JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid\n" +
                            "WHERE count > 0 AND lower(base_in_nomeclature.brends) == '"+sp_BREND.toLowerCase()+"'\n" +
                            "ORDER BY base_in_nomeclature.brends, base_in_nomeclature.p_group, base_group_sql.type_group ASC;";
                    //  "GROUP BY base_in_nomeclature.name\n" +
                } else {
                    query = "SELECT base_in_nomeclature.name, base_in_nomeclature.brends, " +
                            "base_in_nomeclature.p_group, base_in_nomeclature.kod, base_in_image.kod_image, \n" +
                            "base_in_nomeclature.kolbox, base_in_ostatok.count, base_in_ostatok.sklad_uid, " +
                            "base_in_ostatok.sklad_uid, const_sklad.sklad_name,\n" +
                            "base_in_price.price, base_in_nomeclature.strih, base_in_nomeclature.kod_univ, base_in_nomeclature.koduid\n" +
                            "FROM base_in_nomeclature\n" +
                            "LEFT JOIN base_in_ostatok ON base_in_nomeclature.koduid = base_in_ostatok.nomenclature_uid\n" +
                            "LEFT JOIN base_in_price ON base_in_nomeclature.koduid = base_in_price.nomenclature_uid\n" +
                            "LEFT JOIN base_in_image ON base_in_nomeclature.koduid = base_in_image.koduid\n" +
                            "LEFT JOIN base_group_sql ON base_in_nomeclature.koduid = base_group_sql.uid_name\n" +
                            "LEFT JOIN const_sklad ON base_in_ostatok.sklad_uid = const_sklad.sklad_uid\n" +
                            "WHERE count > 0 AND lower(base_in_nomeclature.brends) == '"+sp_BREND.toLowerCase()+"'\n" +
                            "ORDER BY base_in_nomeclature.brends, base_in_nomeclature.p_group, base_group_sql.type_group ASC;";

                    //  "GROUP BY base_in_nomeclature.name\n" +
                }
                Log.e("BRENDS", "Загрузка lIST");

                final Cursor cursor = db.rawQuery(query, null);
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    String brend = cursor.getString(cursor.getColumnIndexOrThrow("brends"));
                    String p_group = cursor.getString(cursor.getColumnIndexOrThrow("p_group"));
                    String kod = cursor.getString(cursor.getColumnIndexOrThrow("kod"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String kolbox = cursor.getString(cursor.getColumnIndexOrThrow("kolbox"));
                    String count = cursor.getString(cursor.getColumnIndexOrThrow("count"));
                    String cena = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                    String image = cursor.getString(cursor.getColumnIndexOrThrow("kod_image"));
                    String strih = cursor.getString(cursor.getColumnIndexOrThrow("strih"));
                    String kod_univ = cursor.getString(cursor.getColumnIndexOrThrow("kod_univ"));
                    String koduid = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));
                    String uid_sklad = cursor.getString(cursor.getColumnIndexOrThrow("sklad_uid"));
                    String name_sklad = cursor.getString(cursor.getColumnIndexOrThrow("sklad_name"));
                    String brends = brend.substring(0, 1) + brend.substring(1).toLowerCase();

                    String sub_brends = p_group.substring(0, 1) + p_group.substring(1).toLowerCase();
                    String ostatok;
                    if (count != (null)) {
                        ostatok = count + "шт";
                    } else ostatok = "закончился";
                    Log.e("BRENDS", "br="+brends+"_"+sp_BREND+"_"+select_menu+"_"+sub_brends);

                    if (brends.equals(sp_BREND)) {
                        if (select_menu.equals(sub_brends)) {
                            if (image != null) {
                                Log.e("Loading1", "name= " + name);
                                arrayList_tovar.add(new ListAdapterSimple_Suncape_Forma(koduid, kod_univ, name, kolbox, cena, Cena_for_DB(cena).first, strih, ostatok, image, name_sklad, uid_sklad));
                                cursor.moveToNext();
                            } else {
                                Log.e("Loading2", "name= " + name);
                                // Log.e("Sub=", p_group + ", " + sub_brends);
                                arrayList_tovar.add(new ListAdapterSimple_Suncape_Forma(koduid, kod_univ, name, kolbox, cena, Cena_for_DB(cena).first, strih, ostatok, "no_image", name_sklad, uid_sklad));
                                cursor.moveToNext();
                            }
                        } else cursor.moveToNext();
                    } else cursor.moveToNext();
                    livedata_simple.postValue(arrayList_tovar);
                    //Thread.sleep(5);
                }
                cursor.close();
                db.close();


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


    protected Pair<Double, Double> Params() {
        mSettings = getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String listValue_all = mSettings.getString("list_all_nal", "0");
        String listValue2_all = mSettings.getString("list_all_kons", "0");
        Double TY, TY_Kons;

        if (listValue_all.equals("0")) {
            TY = Double.parseDouble(listValue_all);

        } else {
            TY = Double.parseDouble(listValue_all) / 100;
        }

        if (listValue2_all.equals("0")) {
            TY_Kons = Double.parseDouble(listValue2_all);

        } else {
            TY_Kons = Double.parseDouble(listValue2_all) / 100;
        }

        return new Pair<>(TY, TY_Kons);
    }  // Загрузка тогровых условий

    protected Pair<String, String> Cena_for_DB(String cena) {

        Double Doub_Cena, Doub_Kons, Doub_Nal;
        String Cena_All, Cena_Nal, Cena_Kons;

        Doub_Cena = Double.parseDouble(cena);
        // Log.e("Mat_Cena: ", Doub_Cena.toString());
        String Format1 = new DecimalFormat("#00.00").format(Doub_Cena).replace(",", ".");
        Cena_All = Format1;
        //Log.e("Mat_Cena_All: ", Format1);

        if (Params().first > 0.0) {
            Doub_Nal = Double.parseDouble(cena) - (Double.parseDouble(cena) * Params().first);
            String Format2 = new DecimalFormat("#00.00").format(Doub_Nal);
            Cena_Nal = Format2;
        } else {
            Doub_Nal = Double.parseDouble(cena);
            String Format2 = new DecimalFormat("#00.00").format(Doub_Nal);
            Cena_Nal = Format2;
        }

        if (Params().second > 0.0) {
            Doub_Kons = Double.parseDouble(cena) - (Double.parseDouble(cena) * Params().second);
            String Format3 = new DecimalFormat("#00.00").format(Doub_Kons);
            Cena_Kons = Format3;
        } else {
            Doub_Kons = Double.parseDouble(cena);
            String Format3 = new DecimalFormat("#00.00").format(Doub_Kons);
            Cena_Kons = Format3;
        }
        try {

        } catch (Exception e) {
            //  Toast.makeText(context_Activity, "Ошибка ценообразования", Toast.LENGTH_SHORT).show();
            Log.e(logeTAG, "Ошибка ценообразования");
        }

        return new Pair<>(Cena_Nal, Cena_Kons);
    }  // Вычисление цен(своя, скидка за нал и конс)
}