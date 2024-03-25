package kg.roman.Mobile_Torgovla;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;

import kg.roman.Mobile_Torgovla.Authorization.Start_Second_Activity;


public class Start_Registracia_New extends AppCompatActivity {

    public Button button;
    public EditText editText_Name;
    public TextView text_brends, text_kod, text_android;
    public Spinner spinner, spinner2;
    public String Android_id, region, type_text, type_int;
    public Context context_Activity;
    public String from, new_write, attach, text, title, where;  // переменные для работы с почтой
    public String this_rn_data, this_rn_vrema, this_rn_year, this_rn_month, this_rn_day, this_data_now;
    public String[] mass_region, mass_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_registracia);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon_image);
        // getSupportActionBar().setTitle("Рабочий стол");
        // getSupportActionBar().setSubtitle(PEREM_AG_NAME);


        context_Activity = this;
        Calendate_New();
        editText_Name = findViewById(R.id.tvw_reg_text_01);
        text_brends = findViewById(R.id.tvw_reg_text_02);
        text_android = findViewById(R.id.tvw_reg_text_03);
        text_kod = findViewById(R.id.tvw_reg_text_04);

        button = findViewById(R.id.button);
        spinner = findViewById(R.id.avtoris_SpinnerSelectUser);
        //spinner2 = findViewById(R.id.spinner2);
        Pack_Info();


        mass_region = getResources().getStringArray(R.array.mass_Region_Put_Name);
        // Настраиваем адаптер
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.mass_Region_Put_Name,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Вызываем адаптер
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                region = mass_region[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });


        UUID identifier = new DeviceUuidFactory(this).getDeviceUuid();
        //text_kod.setText("NEW_UID " + identifier);
        // Log.e("New_ID", "" + identifier);  //
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText_Name.getText().toString().equals("")) {
                    new_write = ("");
                    new_write = (new_write + "Добавить новое устройство:");
                    new_write = (new_write + "\n");
                    new_write = (new_write + "Дата: " + this_data_now);
                    new_write = (new_write + "\n");
                    new_write = (new_write + "Торговый Агент: " + editText_Name.getText());
                    new_write = (new_write + "\n");
                    new_write = (new_write + "Регион: " + region);
                    new_write = (new_write + "\n");
                    new_write = (new_write + "Устройство: \nМодель: " + text_android.getText() + " \nВерсия Android: ");
                    new_write = (new_write + "\n");
                    new_write = (new_write + "Код: " + text_kod.getText());

                    Mail();
                    Start_Registracia_New.sender_mail_async async_sending = new Start_Registracia_New.sender_mail_async();
                    async_sending.execute();

                } else {
                    Toast.makeText(context_Activity, "Заполните все данные!", Toast.LENGTH_SHORT).show();
                    Log.e("Reg", "Пустое");

                }
            }

        });

    }

    // Загрузка даты и время
    protected void Calendate_New() {
        DateFormat df_data = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df_vrema = new SimpleDateFormat("HH:mm:ss");
        DateFormat df_year = new SimpleDateFormat("yyyy");
        DateFormat df_month = new SimpleDateFormat("MM");
        DateFormat df_day = new SimpleDateFormat("dd");

        this_rn_data = df_data.format(Calendar.getInstance().getTime());
        this_rn_vrema = df_vrema.format(Calendar.getInstance().getTime());
        this_rn_year = df_year.format(Calendar.getInstance().getTime());
        this_rn_month = df_month.format(Calendar.getInstance().getTime());
        this_rn_day = df_day.format(Calendar.getInstance().getTime());

        // this_data_now = this_rn_day + "-" + this_rn_month + "-" + this_rn_year;  // Формат для отображения

        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(this_rn_year), Integer.valueOf(this_rn_month) - 1, Integer.valueOf(this_rn_day));
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd:MM:yyyy");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateString_NOW = dateFormat1.format(calendar.getTime());
        String dateString_WORK = dateFormat2.format(calendar.getTime());
        this_data_now = dateString_NOW + " " + this_rn_vrema;
        Log.e("WJ_FormaL2:", "!DataStart:" + dateString_NOW);
        //  Log.e("WJ_FormaL2:", "!DataEnd:" + dateString2);

    }

    // Получение данных из телефона
    protected void Pack_Info() {
        PackageInfo pInfo = null;
        try {

            Android_id = Settings.Secure.getString(context_Activity.getContentResolver(), Settings.Secure.ANDROID_ID);

            if (Android_id.length() >= 14) {
                Android_id = Android_id.substring(0, 14);
                Log.e("Kod_Reg|", Android_id + "|, Кол-во симв: " + +Android_id.length());  //
            } else Log.e("Kod_Reg|", Android_id + "|, Кол-во симв: " + +Android_id.length());  //

            text_kod.setText("/" + Android_id + "/, Кол-во симв: " + Android_id.length());

            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
          /*  Log.e("Reg", pInfo.versionName);  // Версия приложения
            Log.e("Reg", "Код: " + pInfo.versionCode + ", Ревиз: " + pInfo.baseRevisionCode); // Код и ревизия
            Log.e("Reg", Build.BRAND + "_" + Build.MODEL);  // Бренд и модель (Код для регистрации)
            Log.e("Reg", UUID.randomUUID().toString());  //

            Log.e("Reg1", Android_id.substring(0, Android_id.length() - 2));  //
            Log.e("Reg2", Settings.Secure.ANDROID_ID);  //
            Log.e("Reg3", Settings.Secure.getString(context_Activity.getContentResolver(), Settings.Secure.ANDROID_ID));  //*/


            text_brends.setText(Build.BRAND + " " + Build.MODEL);
            String[] mass_versia = getResources().getStringArray(R.array.mass_ver_android);
            for (int i = 21; i <= 30; i++) {
                if (android.os.Build.VERSION.SDK_INT == i) {
                    //  Log.e("Reg Входит", i + String.valueOf(android.os.Build.VERSION.SDK_INT));
                    //   Log.e("Reg Входит", mass_versia[i]);
                    text_android.setText(mass_versia[i]);

                } else {
                            /*Log.e("Reg Входит", "Ошибка версии!");
                            button_regist.setSummary("Версия Android не подходит для работы; " + this_data_now);*/
                }
            }

         /*   Log.e("Phone BOARD ", Build.BOARD);            // название основной платы, например "goldfish";
            Log.e("Phone BRAND", Build.BRAND);            // имя бренда, например "GSmart";
            Log.e("Phone CPU_ABI ", Build.CPU_ABI);          // название набора команд машинного кода, например "armeabi";
            Log.e("Phone DEVICE ", Build.DEVICE);           // название промышленного образца, например "msm1234_sku8";
            Log.e("Phone DISPLAY ", Build.DISPLAY);          // идентификатор сборки, предназначенный для отображения пользователю;
            Log.e("Phone FINGERPRINT ", Build.FINGERPRINT);      // строка, которая однозначно идентифицирует это устройство;
            Log.e("Phone HOST ", Build.HOST);             // в документации нет информации по этой константе;
            Log.e("Phone ID", Build.ID);               // или номер списка изменений, при котором было выпущено устройство, или метка вроде "M4-rc20";
            Log.e("Phone MANUFACTURER ", Build.MANUFACTURER);     // производитель устройства;
            Log.e("Phone MODEL", Build.MODEL);            // название продукта, видимое для пользователя;
            Log.e("Phone PRODUCT", Build.PRODUCT);          // имя продукта;
            Log.e("Phone TAGS", Build.TAGS);             // теги, разделенные запятыми, описывающие сборку, например, "unsigned,debug";
            Log.e("Phone TYPE", Build.TYPE);                 // тип сборки, например "user" или "eng";
            Log.e("Phone USER", Build.USER);                 // в документации нет информации по этой константе. Чаще всего возвращается строка "android".
            // Log.e("Phone ", Build.TIME);                 // в документации нет информации по этой константе. Чаще всего возвращается строка "android".
            Date buildDate = new Date(TIMESTAMP);
            Log.e("Phone Data ", String.valueOf(buildDate)); // в документации нет информации по этой константе. Судя по всему, дата производства;*/
        } catch (Exception e) {

        }
    }

    // Для отправки данных для регистрации
    protected void Mail() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    private class sender_mail_async extends AsyncTask<Object, String, Boolean> {
        ProgressDialog WaitingDialog;

        @Override
        protected void onPreExecute() {
            WaitingDialog = ProgressDialog.show(context_Activity, "Отправка...", "Выполняется загрузка, подождите...", true);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            WaitingDialog.dismiss();
            Toast.makeText(context_Activity, "Данные успешно отправленны!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context_Activity, Start_Second_Activity.class);
            startActivity(intent);
            finish();
            //((Activity)mainContext).finish();
        }


        @Override
        protected Boolean doInBackground(Object... params) {

            try {
                title = "Регистрация КПК: " + this_data_now;
                text = new_write;
                from = "sunbellagents@gmail.com";
                where = "kerkin911@gmail.com";
                attach = "";
                MailSenderClass sender = new MailSenderClass("sunbellagents@gmail.com", "fyczcoexpaspsham", "465", "smtp.gmail.com");    // Mail.ru
                //  MailSenderClass sender = new MailSenderClass("kerkin911@gmail.com", "muvodoutmhkbnqxi");                  // Gmail.com
                //  MailSenderClass sender = new MailSenderClass("romank911@yandex.ru", "ygkvnfxbkwpjhwxd");                  // Yandex.ru
                //   MailSenderClass sender = new MailSenderClass("bishkek", "microlab_LG901480");       // WebHost.kg
                sender.sendMail(title, text, from, where, attach);

            } catch (Exception e) {
                Toast.makeText(context_Activity, "Данные не выгруженны", Toast.LENGTH_SHORT).show();
            }

            return false;
        }
    }


}
