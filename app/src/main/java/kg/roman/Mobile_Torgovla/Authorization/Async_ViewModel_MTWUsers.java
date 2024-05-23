package kg.roman.Mobile_Torgovla.Authorization;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import kg.roman.Mobile_Torgovla.ClassNew.Pair;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.PreferencesWrite;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Users;
import kg.roman.Mobile_Torgovla.XML_Files.MTW_Users_ResourceParser;

public class Async_ViewModel_MTWUsers extends AndroidViewModel {

    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;

    String logeTAG = "ViewModel_MTWUsers";

    public Async_ViewModel_MTWUsers(@NonNull Application application) {
        super(application);
    }

    ////////////////////////
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public LiveData<Boolean> getLoadingStatus() {
        if (isLoading == null)
            isLoading = new MutableLiveData<>(false);
        return isLoading;
    }

    public void execute() {
        isLoading.postValue(false);
        Runnable runnable = () -> {
            try {
                PreferenceAdmin();
                String file_db = getApplication().getDatabasePath("MTW_In_Users.xml").getAbsolutePath(); // путь к databases
                InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = parserFactory.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(istream, null);
                MTW_Users_ResourceParser parser = new MTW_Users_ResourceParser();
                int eventType = xpp.getEventType();

                if (parser.parse(xpp)) {
                    SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase("sunbell_const_db.db3", MODE_PRIVATE, null);
                    db.delete("const_agents", null, null);
                    String query_up = "SELECT * FROM const_agents";
                    Cursor cursor = db.rawQuery(query_up, null);
                    ContentValues localContentValues = new ContentValues();
                    for (MTW_Users ostatok : parser.getUsers()) {
                        //  Log.e("USER_MTW...", ostatok.getUid_name() + " " + ostatok.getKod_mobile());
                        //   Log.e("RETURN ||", Exclusive_Load(ostatok.getUid_name(), ostatok.getKod_mobile()).first.toString());
                        localContentValues.put("uid_name", ostatok.getUid_name());
                        localContentValues.put("name", ostatok.getName());
                        localContentValues.put("uid_region", ostatok.getUid_region().trim());
                        localContentValues.put("cena", ostatok.getCena());
                        localContentValues.put("uid_sklad", ostatok.getUid_sklad().trim());
                        localContentValues.put("skald", ostatok.getSklad());
                        localContentValues.put("kod_mobile", ostatok.getKod_mobile());
                        localContentValues.put("type_real", ostatok.getType_real());
                        localContentValues.put("type_user", ostatok.getType_user());
                        localContentValues.put("region", ostatok.getPut_ag());

                        // localContentValues.put("user_brends", Exclusive_Load(ostatok.getUid_name(), ostatok.getKod_mobile()).first.toString());
                        db.insert("const_agents", null, localContentValues);
                        cursor.moveToNext();
                    }
                    cursor.close();
                    db.close();
                }
                Thread.sleep(2500);
                isLoading.postValue(true);
            } catch (Exception es) {
                es.printStackTrace();
                Log.e(logeTAG, "Ошибка, не возможно отобразить список данных");
            }

        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    ////////////////////////


    ////////////////////////
    private MutableLiveData<Boolean> isStatusInternet = new MutableLiveData<>(false);

    public LiveData<Boolean> getStatusInternet() {
        if (isStatusInternet == null)
            isStatusInternet = new MutableLiveData<>(false);
        return isStatusInternet;
    }

    public void executeInternet() {
        Runnable runnable = () -> {
            ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            // проверка подключения
            if (activeNetwork != null && activeNetwork.isConnected()) {
                try {
                    // тест доступности внешнего ресурса
                    URL url = new URL("http://www.google.com/");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setRequestProperty("User-Agent", "test");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1000); // Timeout в секундах
                    urlc.connect();
                    // статус ресурса OK
                    if (urlc.getResponseCode() == 200) {
                        //	Toast.makeText(ActivityMain.this, "Есть", Toast.LENGTH_LONG).show();
                        isStatusInternet.postValue(true);
                    } else isStatusInternet.postValue(false);  // иначе проверка провалилась

                } catch (IOException e) {
                    Log.d(logeTAG, "Ошибка проверки подключения к интернету", e);
                    isStatusInternet.postValue(false);
                }
            } else isStatusInternet.postValue(false);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void PreferenceAdmin() {
        try {
            PreferencesWrite preferencesWrite = new PreferencesWrite(getApplication().getBaseContext());
            SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_CONST, MODE_PRIVATE, null);
            String query = "SELECT * FROM const_agents\n" +
                    "WHERE uid_name = 'A8BA1F48-C7E1-497B-B74A-D86426684712';";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                mSettings = getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                editor = mSettings.edit();
                String kod_mobile = cursor.getString(cursor.getColumnIndexOrThrow("kod_mobile"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                editor.putString("PEREM_ANDROID_ID_ADMIN", kod_mobile);
                editor.commit();
            }
            cursor.close();
            db.close();
            Log.e(logeTAG, "Admin: нет админа");
        }
        catch (Exception e)
        {
            Log.e(logeTAG, "Error: Ошибка данных об администраторе");
        }

    }
    ////////////////////////


    protected kg.roman.Mobile_Torgovla.ClassNew.Pair Exclusive_Load(String w_uid, String w_mobile) {
        String list_brends = "";
        String list_subbrends = "";
        String new_kod_mobile;
        if (w_uid.equals("F3D1057E-1E77-4AB6-84B8-B80C31089931") || w_mobile.equals("8412b9d6c3af1")) {
            if (w_uid.equals("F3D1057E-1E77-4AB6-84B8-B80C31089931") | w_mobile.equals("8412b9d6c3af1")) {
                new_kod_mobile = "8412b9d6c3af1 ";
                Log.e("BREBDS_LIST ", "DATA= " + new_kod_mobile);
            }
            Log.e("BREBDS_LIST ", "DATA= " + w_uid + "" + w_mobile);
        }

        try {
            SQLiteDatabase db = getApplication().getBaseContext().openOrCreateDatabase("sunbell_const_db.db3", MODE_PRIVATE, null);
            String query = "SELECT name, uid_name, user_brends, user_subbrends FROM const_agents_brends\n" +
                    "WHERE uid_name = '" + w_uid + "'";
            final Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            // Log.e("BREBDS_LIST ", "LIST= " + cursor.getCount());
            if (cursor.getCount() > 0) {
                while (cursor.isAfterLast() == false) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));              // столбец с именем агента
                    String uid = cursor.getString(cursor.getColumnIndexOrThrow("uid_name"));           // столбец с uid-кодом
                    list_brends = cursor.getString(cursor.getColumnIndexOrThrow("user_brends"));   // столбец список
                    list_subbrends = cursor.getString(cursor.getColumnIndexOrThrow("user_subbrends"));   // столбец список
                    //  vis_brends = list_brends;
                    cursor.moveToNext();
                }
            } else {
                //  vis_brends = "/ALL/";
                list_brends = "/ALL/";
                list_subbrends = "/ALL/";
            }
            cursor.close();
            db.close();
        } catch (Exception e) {

        }


       /* switch (w_uid) {
            case "05C91B3D-BD39-4E00-80A9-E3CBEB669317":
                vis_brends = "/BL,DC,VR,PD,YS/";
                break; //Безменова Наталия Петровна
            case "C611B483-547F-41B2-9DF0-050C34682012":
                vis_brends = "/BL,DC,VR,PD,YS/";
                break; // Мухамеджанова Елена Арслановна
            case "A8BA1F48-C7E1-497B-B74A-D86426684712":
                vis_brends = "/BL,DC,VR,PD,YS/";
                break; //  Керкин Роман Максимович

            case "54014B5F-3FDD-4AFB-BD8C-E72289F700D5":
                vis_brends = "/OT,CS,LC,PC,PS,SM,DR,KD,NT,IE,TG,SR,NS,PF,SV,TR,SH,BG,CT,HU/";
                break; //  Бударная Валентина (маршрут-401)
            case "5798C798-014A-497F-9224-1545C5721D76":
                vis_brends = "/OT,CS,LC,PC,PS,SM,DR,KD,NT,IE,TG,SR,NS,PF,SV,TR,SH,BG,CT,HU/";
                break; //  Бударная Валентина (маршрут-402)
            case "B6250C08-3E7E-48E1-A7A2-F5FCEBA30C4F":
                vis_brends = "/OT,CS,LC,PC,PS,SM,DR,KD,NT,IE,TG,SR,NS,PF,SV,TR,SH,BG,CT,HU/";
                break; //  Бударная Валентина (маршрут-403)

            case "AACE3E62-3E1B-4C04-9814-43FC559B1D64":
                vis_brends = "/OT,CS,LC,PC,PS,SM,DR,KD,NT,IE,TG,SR,NS,PF,SV,TR,SH,BG,CT,HU/";
                break; //  Субдилер (регион: Ак-суу)


            default:
                vis_brends = "/ALL/";
                break; // Для остальных
        }*/


        // return list_brends;
        return new Pair(list_brends, list_subbrends);
    }

}
