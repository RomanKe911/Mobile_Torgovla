package kg.roman.Mobile_Torgovla.Authorization;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import androidx.appcompat.app.AppCompatActivity;

import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_Login;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Login;
import kg.roman.Mobile_Torgovla.MT_FTP.CalendarThis;
import kg.roman.Mobile_Torgovla.MT_FTP.PreferencesWrite;
import kg.roman.Mobile_Torgovla.Permission.PrefActivity_Splash;
import kg.roman.Mobile_Torgovla.Permission.PrefActivity_Splash_New;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.Setting.Setting;
import kg.roman.Mobile_Torgovla.Start_Registracia_New;
import kg.roman.Mobile_Torgovla.Work_Journal.WJ_Global_Activity;
import kg.roman.Mobile_Torgovla.databinding.MtAvtosizacBinding;

public class Start_Second_Activity extends AppCompatActivity {
    public ArrayList<ListAdapterSimple_Login> login_aut = new ArrayList<>();
    public ListAdapterAde_Login adapterPriceClients;
    //////////12.02.2024
    private MtAvtosizacBinding binding;
    String logeTAG = "StartSecondActivity";
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    public Context context_Activity;
    public int kol;
    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MtAvtosizacBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.icon_logotoolbar);
        getSupportActionBar().setTitle("Мобильная торговля");
        getSupportActionBar().setSubtitle("Добро пожаловать");
        context_Activity = getBaseContext();
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        binding.avtorisEditPinCode.requestFocus();
        binding.avtorisEditPinCode.setFocusable(true);

        try {
            //// Работа со спинером
            SpinnerLoadingData();
        } catch (Exception e) {
            SnackbarOverride("Ошибка данных, нет активных пользователей");
            Log.e(logeTAG, "Ошибка данных spinnera");
        }

        try {
            binding.avtorisButtonNext.setOnClickListener(v -> {
                PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
                Log.e(logeTAG, "Мульти: " + preferencesWrite.Setting_Brends_HandsBrends_MultiList);
                try {
                    if (binding.avtorisEditPinCode.getText().toString().equals(preferencesWrite.PinCodes) & binding.avtorisSpinnerSelectUser.getCount() > 0) {
                        if (!preferencesWrite.PEREM_FTP_IP_Connect.isEmpty() && !preferencesWrite.PEREM_FTP_PathData.isEmpty()) {

                            if (preferencesWrite.Setting_Brends_HandsBrends)
                            {
                                Log.e(logeTAG, "Мульти-спик активен");
                                if (!preferencesWrite.Setting_Brends_HandsBrends_MultiList.isEmpty()) {
                                    Toast.makeText(context_Activity, "Добро пожаловать, " + SQL_NameAgent(), Toast.LENGTH_SHORT).show();
                                    hideInputMethod(); // Тип отображение данных
                                    Intent intent = new Intent(context_Activity, WJ_Global_Activity.class);
                                    startActivity(intent);
                                    finish();
                                } else
                                    SnackbarOverride("мульти-список брендов пуст, заполните данные");
                            }

                            else {
                                Log.e(logeTAG, "Мульти-спик не активен");
                                Toast.makeText(context_Activity, "Добро пожаловать, " + SQL_NameAgent(), Toast.LENGTH_SHORT).show();
                                hideInputMethod(); // Тип отображение данных
                                Intent intent = new Intent(context_Activity, WJ_Global_Activity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else
                            SnackbarOverride("не верные настройки, не выбран путь к данным на сервере");
                        Log.e(logeTAG, "не верные настройки, не выбран путь к данным на сервере");
                    } else {
                        SnackbarOverride("нет активных пользователей или не введен пин-код!");
                        Log.e(logeTAG, "нет активных пользователей или не введен пин-код!");
                    }

                } catch (Exception e) {
                    SnackbarOverride("Ошибка, вход не выполнен");
                    Log.e(logeTAG, "Ошибка, вход не выполнен");
                }


            });
        } catch (Exception e) {
            SnackbarOverride("Ошибка данных, проверьте данные");
            Log.e(logeTAG, "Ошибка данных констант!");
        }


        binding.avtorisEditPinCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*if (editText_password.getText().toString().equals(text_password)) {
                    button_aut.setBackground(getResources().getDrawable(R.drawable.wj_office_selector_button_right));

                } else
                    button_aut.setBackground(getResources().getDrawable(R.drawable.office_right_black));*/
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getSupportActionBar().hide();
        getMenuInflater().inflate(R.menu.menu_autoris, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        try {
            switch (id) {
                case R.id.menu_new_agent: {
                    Intent intent = new Intent(context_Activity, Start_Registracia_New.class);
                    startActivity(intent);
                    finish();
                }
                break;
                case R.id.menu_preferenc: {
                    Intent intent_prefActivity = new Intent(context_Activity, PrefActivity_Splash.class);
                    startActivity(intent_prefActivity);
                }
                break;
                case R.id.menu_setting_new: {
                    Intent intent_prefActivity = new Intent(context_Activity, PrefActivity_Splash_New.class);
                    startActivity(intent_prefActivity);
                }
                break;
                case R.id.menu_setting_start: {
                    Intent intent_prefActivity = new Intent(context_Activity, Setting.class);
                    startActivity(intent_prefActivity);
                }
                break;
            }
        } catch (Exception e) {
            SnackbarOverride("Ошибка вход в настройки");
            Log.e(logeTAG, "Ошибка вход в настройки");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        /////////////////  25.02.2024
        try {
            Log.e(logeTAG, "onResume");
            PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
            login_aut.clear();
            DB_Spinner_Write(preferencesWrite.PEREM_ANDROID_ID);  // Загрузка данных из базы SQlite
            adapterPriceClients = new ListAdapterAde_Login(Start_Second_Activity.this, login_aut);
            adapterPriceClients.notifyDataSetChanged();
            binding.avtorisSpinnerSelectUser.setAdapter(adapterPriceClients);
        } catch (Exception e) {
            Toast.makeText(context_Activity, "Ошибка данных Spinner", Toast.LENGTH_SHORT).show();
            Log.e(this.getLocalClassName(), "Ошибка данных Spinner");
        }

    }

    // Загрузка данных в Spinner
    protected void DB_Spinner_Write(String kod_android) {
        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);

        SQLiteDatabase db_insert = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_CONST, MODE_PRIVATE, null);
        String query;
        if (kod_android.equals(preferencesWrite.PEREM_ANDROID_ID_ADMIN)) {
            query = "SELECT * FROM const_agents WHERE kod_mobile > 0";
        } else query = "SELECT * FROM const_agents WHERE kod_mobile = '" + kod_android + "'";

        final Cursor cursor = db_insert.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String uid = cursor.getString(cursor.getColumnIndexOrThrow("uid_name"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String region = cursor.getString(cursor.getColumnIndexOrThrow("region"));
            String kod_mobile = cursor.getString(cursor.getColumnIndexOrThrow("kod_mobile"));
            String type_user = cursor.getString(cursor.getColumnIndexOrThrow("type_user"));
            String cena = cursor.getString(cursor.getColumnIndexOrThrow("cena"));
            String sklad = cursor.getString(cursor.getColumnIndexOrThrow("skald"));

            if (kod_android.equals(kod_mobile) || kod_android.equals(preferencesWrite.PEREM_ANDROID_ID_ADMIN)) {
                login_aut.add(new ListAdapterSimple_Login(name, Image_Type_User(type_user)));
                binding.avtorisTWVUserMarshrut.setText(region);
                binding.avtorisTVWUser.setText(type_user);
                binding.avtorisTVWPrice.setText(cena);
                binding.avtorisTVWSklad.setText(sklad);

                String[] mass_name_region, mass_name_region_put;
                mass_name_region = getResources().getStringArray(R.array.mass_Region_Put_Name);
                mass_name_region_put = getResources().getStringArray(R.array.mass_Region_Put_Value);
                for (int i = 0; i < mass_name_region_put.length; i++)
                    if (preferencesWrite.PEREM_FTP_PathData.equals(mass_name_region_put[i]))
                        binding.avtorisTVWUserRegion.setText(mass_name_region[i]);
                cursor.moveToNext();
            } else {
                Log.e(logeTAG, "Error: Нету данных!");
                cursor.moveToNext();
            }
        }


        cursor.close();
        db_insert.close();
    }


    // Картинки для иконок пользователя
    protected String Image_Type_User(String type) {
        String Image_for_Users;
        switch (type) {
            case "Агент":
                Image_for_Users = "img_agt";
                break;
            case "Экспедитор":
                Image_for_Users = "img_expd";
                break;
            case "Зав.складом":
                Image_for_Users = "img_skl";
                break;
            case "Водитель":
                Image_for_Users = "img_driver";
                break;
            case "Оператор":
                Image_for_Users = "img_opert";
                break;
            case "Бухгалтер":
                Image_for_Users = "img_money";
                break;
            case "Директор филиала Каракол":
                Image_for_Users = "img_money";
                break;
            default:
                Image_for_Users = "img_null";
                break;
        }


        Uri imagePath_phone = Uri.parse("android.resource://kg.roman.Mobile_Torgovla/drawable/" + Image_for_Users);
        binding.avtorisImageLogo.setImageURI(imagePath_phone);
        Picasso.get() //передаем контекст приложения
                .load(imagePath_phone)    //Путь к файлу
                .error(R.drawable.logo_sunbell).into(binding.avtorisImageLogo);

        return Image_for_Users;
    }


    // Константы для записи
    protected void Constanta_Write(String uid, String name, String kod_mobile, String region, String uid_region, String cena, String sklad, String uid_sklad, String type_real, String type_user, Set<String> vis_brends, Set<String> vis_Subbrends) {
        CalendarThis calendars = new CalendarThis();
        editor = mSettings.edit();
        editor.putString("PEREM_AG_UID", uid);                                          // передача кода агента (A8BA1F48-C7E1-497B-B74A-D86426684712)
        editor.putString("PEREM_AG_NAME", name);                                        // передача кода агента (Имя пользователя)
        editor.putString("PEREM_AG_REGION", region);                                    // маршруты для привязки к контагентам
        editor.putString("PEREM_AG_UID_REGION", uid_region.trim());                     // uid маршруты для привязки к контагентам
        editor.putString("PEREM_AG_CENA", cena);                                        // цены для агентов
        editor.putString("PEREM_AG_SKLAD", sklad);                                      // склады для агентов
        editor.putString("PEREM_AG_UID_SKLAD", uid_sklad.trim());                       // UID склады для агентов
        editor.putString("PEREM_AG_TYPE_REAL", type_real);                              // выбор типа торгового агента 1-OSDO или 2-PRES
        editor.putString("PEREM_AG_TYPE_USER", type_user);                              // тип учетной записи агент или экспедитор
        editor.putString("PEREM_WORK_DISTR", calendars.CalendarDayOfWeek().first);      // имя папки с данными (01_WDay)
        editor.putString("PEREM_KOD_MOBILE", kod_mobile);                               // имя папки с данными (01_WDay)
        editor.putString("PEREM_KOD_UID_KODRN", uid.substring(9, uid.length() - 23));   // уникальный код для накладной
        editor.putStringSet("PEREM_BrendsForNomeclature", vis_brends);             // список брендов для загрузки для агентов
        editor.putStringSet("PEREM_BrendsForSubNomeclature", vis_Subbrends);       // список брендов для загрузки под группы
        editor.commit();
    }

    ///// Имя и фамилия агента
    protected String SQL_NameAgent() {
        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_CONST, MODE_PRIVATE, null);
        String query = "SELECT * FROM const_agents\n" + "WHERE uid_name = '" + binding.avtorisTVWUserID.getText().toString() + "'";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String AgentName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        cursor.close();
        db.close();
        return AgentName;
    }

    /**
     * прячем программную клавиатуру
     */
    protected void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(binding.avtorisEditPinCode.getWindowToken(), 0);
        }
    }

    /**
     * показываем программную клавиатуру
     */
    protected void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private Runnable mShowInputMethodTask = new Runnable() {
        public void run() {
            // showInputMethodForQuery();
        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // если окно в фокусе, то ждем еще немного и показываем клавиатуру
            mHandler.postDelayed(mShowInputMethodTask, 0);
        }
    }


    /////////////  02.2024 Переработка данных
    /// Загрузка данных в spinner
    private void SpinnerLoadingData() {
        try {
            login_aut.clear();

            PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
            Log.e(logeTAG, "Data: "+preferencesWrite.PEREM_ANDROID_ID);

            DB_Spinner_Write(preferencesWrite.PEREM_ANDROID_ID);  // Загрузка данных из базы SQlite
            adapterPriceClients = new ListAdapterAde_Login(Start_Second_Activity.this, login_aut);
            adapterPriceClients.notifyDataSetChanged();
            binding.avtorisSpinnerSelectUser.setAdapter(adapterPriceClients);
            Log.e(logeTAG, preferencesWrite.PEREM_SPINNER_NameAgent);
        } catch (Exception e) {
            SnackbarOverride("Ошибка данных Spinner");
            Log.e(logeTAG, "Ошибка данных Spinner");
        }

        binding.avtorisSpinnerSelectUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition, long selectedId) {
                try {
                    // text_password = "";
                    binding.avtorisEditPinCode.setText("");
                    binding.avtorisEditPinCode.clearFocus();
                    binding.avtorisEditPinCode.requestFocus();
                    binding.avtorisEditPinCode.setFocusable(true);
                    showInputMethod();
                    // selected_name_image = ((TextView) itemSelected.findViewById(R.id.tvw_sp_name_image)).getText().toString();
                    DB_Spinner_Select(((TextView) itemSelected.findViewById(R.id.textView_login)).getText().toString());
                } catch (Exception e) {
                    SnackbarOverride("Ошибка выбора Spinner");
                    Log.e(logeTAG, "Ошибка выбора Spinner");
                    hideInputMethod();
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        try {
            kol = 0;
            binding.avtorisImageVisiblePinCode.setOnClickListener(v -> {
                switch (kol) {
                    case 0:
                        kol = 1;
                        binding.avtorisImageVisiblePinCode.setImageResource(R.drawable.user_preview_down);
                        binding.avtorisEditPinCode.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        binding.avtorisEditPinCode.setSelection(binding.avtorisEditPinCode.getText().length());
                        break;
                    case 1:
                        kol = 0;
                        binding.avtorisImageVisiblePinCode.setImageResource(R.drawable.user_preview_up);
                        binding.avtorisEditPinCode.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        binding.avtorisEditPinCode.setSelection(binding.avtorisEditPinCode.getText().length());
                        break;
                }
            });
        } catch (Exception e) {
            Log.e(this.getLocalClassName(), "Ошибка загрузки данных!");
        }
    }

    // При выборе данных в Spinner
    protected void DB_Spinner_Select(String spinner_select) {
        PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
        try {
            SQLiteDatabase db_insert = getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_CONST, MODE_PRIVATE, null);
            String query = "SELECT const_agents.uid_name, const_agents.name, const_agents.uid_region, const_agents.cena, \n"
                    + "const_agents.uid_sklad, const_agents.skald, const_agents.kod_mobile, const_agents.type_real, \n"
                    + "const_agents.type_user, const_agents.region, const_agents_brends.user_brends, " +
                    "const_agents_brends.user_subbrends\n" + "" +
                    "FROM const_agents\n" + "" +
                    "LEFT JOIN const_agents_brends ON const_agents.uid_name = const_agents_brends.uid_name\n" + "" +
                    "WHERE const_agents.name = '" + spinner_select + "'";

            final Cursor cursor = db_insert.rawQuery(query, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String uid = cursor.getString(cursor.getColumnIndexOrThrow("uid_name"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String region = cursor.getString(cursor.getColumnIndexOrThrow("region"));
                String uid_region = cursor.getString(cursor.getColumnIndexOrThrow("uid_region"));
                String cena = cursor.getString(cursor.getColumnIndexOrThrow("cena"));
                String sklad = cursor.getString(cursor.getColumnIndexOrThrow("skald"));
                String uid_sklad = cursor.getString(cursor.getColumnIndexOrThrow("uid_sklad"));
                String kod_mobile = cursor.getString(cursor.getColumnIndexOrThrow("kod_mobile"));
                String type_real = cursor.getString(cursor.getColumnIndexOrThrow("type_real"));
                String type_user = cursor.getString(cursor.getColumnIndexOrThrow("type_user"));
                String vis_category = cursor.getString(cursor.getColumnIndexOrThrow("user_brends"));
                String vis_categoryNext = cursor.getString(cursor.getColumnIndexOrThrow("user_subbrends"));
                String stringBuilder = uid + "\n" +
                        kod_mobile + "\n" +
                        region + "\n" +
                        uid_region + "\n" +
                        cena + "\n" +
                        sklad + "\n" +
                        uid_sklad + "\n" +
                        type_real + "\n" +
                        type_user + "\n" +
                        vis_category;

                Log.e(logeTAG, "DB_Spinner_Select: " + stringBuilder);
                Image_Type_User(type_user);
                binding.avtorisTVWUserID.setText(uid);
                binding.avtorisTWVUserMarshrut.setText(region);
                binding.avtorisTVWUser.setText(type_user);
                binding.avtorisTVWPrice.setText(cena);
                binding.avtorisTVWSklad.setText(sklad);

                if (vis_category != null || vis_categoryNext != null) {
                    Constanta_Write(uid, name, kod_mobile, region, uid_region, cena, sklad, uid_sklad, type_real, type_user, BrendsForAgents(vis_category.replaceAll("/", "")), BrendsForAgents(vis_categoryNext.replaceAll("/", "")));
                    Log.e(logeTAG, "Доступные бренды: " + vis_category + "__" + vis_categoryNext);
                } else {
                    Set<String> allSet = new HashSet<>();
                    allSet.add("/ALL/");
                    Constanta_Write(uid, name, kod_mobile, region, uid_region, cena, sklad, uid_sklad, type_real, type_user, allSet, allSet);
                    Log.e(logeTAG, "Доступные бренды:" + "Все из всех");
                    Log.e(logeTAG, "Доступные бренды: " + "_" + allSet);
                }


                //Toast.makeText(context_Activity, "Доступные бренды:" + vis_category, Toast.LENGTH_SHORT).show();
                cursor.moveToNext();
            }
            cursor.close();
            db_insert.close();
        } catch (Exception e) {
            Log.e(logeTAG, "Ошибка данных, списка доступных брендов!");
            SnackbarOverride("Ошибка данных, списка доступных брендов!");
        }
    }

    // Формирование строки для записи в Set
    protected Set<String> BrendsForAgents(String str) {
        // Формирование строки для записи в Set
        // /BL,DC,VR,PD,YS,SK/ -> BL,DC,VR,PD,YS,SK
        TreeSet<String> brendsForAgents = new TreeSet<>();
        if (!str.equals("ALL")) {
            String st = "";
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) != ',') st = st + str.charAt(i);
                else {
                    brendsForAgents.add(st.toLowerCase());
                    st = "";
                }
            }
        } else brendsForAgents.add("ALL");
        return brendsForAgents;
    }

    protected void SnackbarOverride(String text) {
        Snackbar.make(binding.ConstraintLayoutAvtoris, text, Snackbar.LENGTH_SHORT).show();
    }


}


/*
*     public static String toTranslit(String src) {
        String[] f = {"А", "Б", "В", "Г", "Д", "Е", "Ё", "Ж", "З", "И", "Й", "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф", "Х", "Ч", "Ц", "Ш", "Щ", "Э", "Ю", "Я", "Ы", "Ъ", "Ь", "а", "б", "в", "г", "д", "е", "ё", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п", "р", "с", "т", "у", "ф", "х", "ч", "ц", "ш", "щ", "э", "ю", "я", "ы", "ъ", "ь"};
        String[] t = {"A", "B", "V", "G", "D", "E", "Jo", "Zh", "Z", "I", "J", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ch", "C", "Sh", "Csh", "E", "Ju", "Ja", "Y", "`", "'", "a", "b", "v", "g", "d", "e", "jo", "zh", "z", "i", "j", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ch", "c", "sh", "csh", "e", "ju", "ja", "y", "`", "'"};

        String res = "";

        for (int i = 0; i < src.length(); ++i) {
            String add = src.substring(i, i + 1);
            for (int j = 0; j < f.length; j++) {
                if (f[j].equals(add)) {
                    add = t[j];
                    break;
                }
            }
            res += add;
        }

        return res;
    }
        protected void Type() {
        switch (binding.avtorisTVWUser.getText().toString()) {
            case "admin": {
                hideInputMethod(); // Тип отображение данных
                Intent intent = new Intent(context_Activity, WJ_Global_Activity.class);
                startActivity(intent);
                finish();
            }
            break;
            case "Агент": {
                hideInputMethod();// Тип отображение данных
                Intent intent = new Intent(context_Activity, WJ_Global_Activity.class);
                startActivity(intent);
                finish();
            }
            break;
            case "driver": {
                hideInputMethod();// Тип отображение данных
                Intent intent = new Intent(context_Activity, WJ_Global_Activity.class);
                startActivity(intent);
                finish();
            }
            break;
            case "rosn": {
                hideInputMethod();// Тип отображение данных
                Intent intent = new Intent(context_Activity, WJ_Ros_Torg.class);
                startActivity(intent);
                finish();
            }
            break;
            default:
                break;
        }
    }
    *
    *
    *  */

/*    protected void Loading_Agent_Name() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(PEREM_DB3_CONST, MODE_PRIVATE, null);
        String query = "SELECT *FROM const_logins";

        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String Agent_Kod = cursor.getString(cursor.getColumnIndexOrThrow("UID"));
            String Name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String Name_S = cursor.getString(cursor.getColumnIndexOrThrow("s_name"));
            String Type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            String Distr = cursor.getString(cursor.getColumnIndexOrThrow("Name_Distr"));
            String DST_CONTRAG = cursor.getString(cursor.getColumnIndexOrThrow("region"));
            if (UID_Agent.equals(Agent_Kod)) {
                PEREM_Agent = Name_S + "_" + Name;
                PEREM_Type = Type;
                myDB = Distr;
                //PEREM_Distr = Distr;
                PEREM_DST_ContrAg = DST_CONTRAG;
            }
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

    }*/

/*
    protected void Open_Files_MTW_XML(String kod_android) {
        try {
            String file_db = Start_Second_Activity.this.getDatabasePath(".xml").getAbsolutePath(); // путь к databases
            InputStream istream = new FileInputStream(new File(file_db));  // откуда загружать файлы отправить файлы
            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = parserFactory.newPullParser();
            xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xpp.setInput(istream, null);

            MTW_File_Const_ResourceParser parser = new MTW_File_Const_ResourceParser();
            int eventType = xpp.getEventType();
            if (parser.parse(xpp)) {
                for (MTW_File_Const price : parser.getXml_const()) {
                    if (kod_android.equals(price.getKod_mobile())) {
                        Log.e("MTW_Const ", price.getUid_user());
                        Log.e("MTW_Const ", price.getName());
                        Log.e("MTW_Const ", price.getPass());
                        Log.e("MTW_Const ", price.getRegion());
                        Log.e("MTW_Const ", price.getUid_region());
                        Log.e("MTW_Const ", price.getCena());
                        Log.e("MTW_Const ", price.getSklad());
                        Log.e("MTW_Const ", price.getKod_mobile());
                        Log.e("MTW_Const ", price.getType_real());
                        Log.e("MTW_Const ", price.getType_user());
                    }
                }
            }
        } catch (Exception e) {

            Log.e("Err..", "rrr");
        }
    }*/

/*    protected void ListAdapet_Log() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("suncape_const_db.db3", MODE_PRIVATE, null);
        String query = "SELECT const_logins._id, const_logins.name, const_logins.s_name, " + "const_logins.password, const_logins.region,  const_logins.telefon, " + "const_logins.image, const_logins.kod_mobile, const_logins.type, const_logins.UID, " + "const_logins.Name_Distr \n" + "FROM const_logins";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        array_logins = new String[100][100];
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String s_name = cursor.getString(cursor.getColumnIndexOrThrow("s_name"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            String region = cursor.getString(cursor.getColumnIndexOrThrow("region"));
            String telefon = cursor.getString(cursor.getColumnIndexOrThrow("telefon"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            String kod_mobile = cursor.getString(cursor.getColumnIndexOrThrow("kod_mobile"));
            String UID = cursor.getString(cursor.getColumnIndexOrThrow("UID"));
            String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
            //  imageView_Login.setImageResource(R.mipmap.ic_error);
            array_logins[i][j] = s_name + " " + name;
            j++;
            array_logins[i][j] = password;
            j++;
            array_logins[i][j] = region;
            array_logins[i][7] = type;
            array_logins[i][6] = kod_mobile;
            if (!UID.isEmpty()) {
                array_logins[i][8] = UID;
                Log.e(this.getLocalClassName(), "Есть данные ");
            } else Log.e(this.getLocalClassName(), "Нет данных");

            int resID = getResources().getIdentifier("user_man", "drawable", getPackageName());
            login_aut.add(new ListAdapterSimple_Login(s_name + " " + name, image));
            cursor.moveToNext();
            i++;
            j = 1;
        }
        cursor.close();
        db.close();
    }*/