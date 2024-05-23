package kg.roman.Mobile_Torgovla.FormaZakazaStart;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.FormaZakazaSelectClient.WJ_Forma_Zakaza_L1;
import kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar.RemovableDeletePosition;
import kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar.WJ_Forma_Zakaza_L2;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.CalendarThis;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.PreferencesWrite;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.Preferences_MTSetting;
import kg.roman.Mobile_Torgovla.R;

public class DialogFragment_EditorZakaz extends DialogFragment {
    private RemovableDeletePosition removableDeletePosition, removableDeleteActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        removableDeletePosition = (RemovableDeletePosition) context;
        removableDeleteActivity = (RemovableDeletePosition) context;
    }

    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    String logeTAG = "DialogEditor";
    int position;
    String codeOrder, messege = "", editorStatus = "";
    PreferencesWrite preferencesWrite;
    ArrayList<String> arrayList;


    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mSettings = getActivity().getApplication().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        preferencesWrite = new PreferencesWrite(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View localView = getLayoutInflater().inflate(R.layout.dialog_endzakaz, null);
        position = getArguments().getInt("editor_position");
        codeOrder = getArguments().getString("editorCodeOrder");
        arrayList = getArguments().getStringArrayList("arrayClientData");

        if (getArguments().getBoolean("editorCopy")) {
            messege = "Вы хотите создать копию данного заказа?";
            editorStatus = "Copy";
        }
        if (getArguments().getBoolean("editorEdit")) {
            messege = "Вы хотите изменить данный заказ?";
            editorStatus = "Edit";
        }
        if (getArguments().getBoolean("editorDelete")) {
            messege = "Вы хотите удалить данный заказ?";
            editorStatus = "Delete";
        }

        return builder
                .setTitle("Сообщение...")
                .setMessage(messege)
                .setIcon(R.drawable.office_title_forma_zakaz)
                //  .setView(localView)
                .setPositiveButton("OK", (dialog, which) ->
                {
                    switch (editorStatus) {
                        case "Copy": {
                            Log.e(logeTAG, "Обработка Copy");
                            //// Создание копия заказа
                            editor = mSettings.edit();
                            editor.putString("statusRN", "Copy");  // создаем метку действия
                            editor.putString("oldKodRN", codeOrder);  // создаем метку действия
                            editor.commit();

                            Preferences_MTSetting preferencesMtSetting = new Preferences_MTSetting();
                            preferencesMtSetting.writeSettingString(getContext(), preferencesMtSetting.getStatusOrder(), "Copy");

                            Intent intent_prefActivity = new Intent(getContext(), WJ_Forma_Zakaza_L1.class);
                            startActivity(intent_prefActivity);
                            removableDeleteActivity.deleteThisActivity(true);

     /*                       preferencesWrite = new PreferencesWrite(getContext());
                            SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
                            EditorFormaZakaza editorFormaZakaza = new EditorFormaZakaza(db, getContext());
                            String newRn = preferencesWrite.createNewRN;
                            Log.e("Новый код накладной:", newRn);
                            editorFormaZakaza.editorCopy(kodRn, newRn);
                            for (int i = 0; i < arrayList.size(); i++)
                                Constanta_Write(arrayList.get(1), arrayList.get(0), arrayList.get(2), newRn, "");
                            try {
                                Thread.sleep(200);

                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }*/

                        }
                        break;
                        case "Edit": {
                            //// Редактирование и изменение заказа
                            Log.e(logeTAG, "Обработка Edit");
                            Constanta_Write(codeOrder);
                            Intent intent_prefActivity = new Intent(getContext(), WJ_Forma_Zakaza_L2.class);
                            startActivity(intent_prefActivity);
                            removableDeleteActivity.deleteThisActivity(true);
                        }
                        break;
                        case "Delete": {
                            //// Удаление
                            Log.e(logeTAG, "Обработка Удаления");
                            removableDeletePosition.restartAdapterDeletePosition(true, position);
                            removableDeleteActivity.deleteThisActivity(false);
                        }
                        break;
                    }


                })
                .setNegativeButton("Отмена", (dialog, which) ->
                {
                    removableDeletePosition.restartAdapterDeletePosition(false, position);
                })
                .create();
    }

    /////// Запись данных для новой активити
    protected void Constanta_Write(String w_codeOrder) {
        try {
/*            CalendarThis calendarThis = new CalendarThis();
           editor.putString("PEREM_K_AG_Data", calendarThis.getThis_DateFormatSqlDB);              //запись данных: время создание накладной
            editor.putString("PEREM_K_AG_Data_WORK", calendarThis.getThis_DateFormatXML);    //запись данных: время создание накладной
            editor.putString("PEREM_K_AG_Vrema", calendarThis.getThis_DateFormatVrema);            //запись данных: дата создание накладной
             Log.e(logeTAG, "EDIT " + newParcelable.getKodRN());*/

            selectEditZakaz(w_codeOrder);
            SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);

            db.execSQL("CREATE TABLE IF NOT EXISTS base_RN_Edit" +
                    " (_id INT, Kod_RN TEXT, Vrema TEXT, Data TEXT, Kod_Univ TEXT, koduid TEXT, " +
                    "Image TEXT, Name TEXT, Kol TEXT, Cena TEXT, Summa TEXT, Skidka TEXT, " +
                    "Cena_SK TEXT, Itogo TEXT, aks_pref TEXT, aks_name TEXT, sklad_name TEXT, sklad_uid TEXT);");
            db.execSQL("DELETE FROM base_RN_Edit");
            db.execSQL("INSERT INTO base_RN_Edit SELECT * FROM base_RN WHERE Kod_RN = '" + w_codeOrder + "';");
            db.close();

        } catch (Exception e) {
            Log.e(logeTAG, "Ошибка, сохранения параметров");
        }
    }

    protected void selectEditZakaz(String codeOrder) {
        SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String queryAll = "SELECT * FROM base_RN_All WHERE Kod_RN = '" + codeOrder + "';";
        Cursor cursor = db.rawQuery(queryAll, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            Preferences_MTSetting preferencesMtSetting = new Preferences_MTSetting();
            preferencesMtSetting.writeSettingString(getContext(), preferencesMtSetting.getStatusOrder(), "Edit");             // статус наклданой
            preferencesMtSetting.writeSettingString(getContext(), preferencesMtSetting.getCodeOrder(), codeOrder);                                                                       // код наклданой
            preferencesMtSetting.writeSettingString(getContext(), preferencesMtSetting.getClientName(), cursor.getString(cursor.getColumnIndexOrThrow("k_agn_name")));      // имя клиента
            preferencesMtSetting.writeSettingString(getContext(), preferencesMtSetting.getClientUID(), cursor.getString(cursor.getColumnIndexOrThrow("k_agn_uid")));        // кодUID клиента
            preferencesMtSetting.writeSettingString(getContext(), preferencesMtSetting.getClientAdress(), cursor.getString(cursor.getColumnIndexOrThrow("k_agn_adress")));   // адрес клиента
            preferencesMtSetting.writeSettingString(getContext(), preferencesMtSetting.getSkladUID(), cursor.getString(cursor.getColumnIndexOrThrow("sklad_uid")));         // код склада
            preferencesMtSetting.writeSettingString(getContext(), preferencesMtSetting.getSkladName(), cursor.getString(cursor.getColumnIndexOrThrow("sklad")));         // имя склада
            preferencesMtSetting.writeSettingInt(getContext(), preferencesMtSetting.getSaleCount(), Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("skidka"))));           // размер скидки
            preferencesMtSetting.writeSettingString(getContext(), preferencesMtSetting.getInfoOrderCredit(), cursor.getString(cursor.getColumnIndexOrThrow("credit")));
            preferencesMtSetting.writeSettingInt(getContext(), preferencesMtSetting.getInfoOrderCreditCountDay(), Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("credite_date"))));
            preferencesMtSetting.writeSettingString(getContext(), preferencesMtSetting.getCommentAll(), cursor.getString(cursor.getColumnIndexOrThrow("coment")));
            String comment = cursor.getString(cursor.getColumnIndexOrThrow("coment"));
            preferencesMtSetting.writeSettingString(getContext(), preferencesMtSetting.getComment(), comment.substring(comment.indexOf("cmn_") + 4));
            preferencesMtSetting.writeSettingInt(getContext(), preferencesMtSetting.getTypeOrder(), Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("uslov_nds"))));
            preferencesMtSetting.writeSettingString(getContext(), preferencesMtSetting.getDateDelivery(), cursor.getString(cursor.getColumnIndexOrThrow("data_up")));
        }
        cursor.close();
        db.close();
    }


}