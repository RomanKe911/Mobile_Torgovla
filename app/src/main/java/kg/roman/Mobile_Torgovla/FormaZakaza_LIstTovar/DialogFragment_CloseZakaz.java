package kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import kg.roman.Mobile_Torgovla.MT_MyClassSetting.PreferencesWrite;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.Preferences_MTSetting;
import kg.roman.Mobile_Torgovla.R;

public class DialogFragment_CloseZakaz extends DialogFragment {
    private Removable removable;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        removable = (Removable) context;
    }

    String logeTAG = "DialogCloseZakaz";
    String statusOrder, codeOrder;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Preferences_MTSetting preferencesMtSetting = new Preferences_MTSetting();
        statusOrder = preferencesMtSetting.readSettingString(getContext(), preferencesMtSetting.getStatusOrder());
        codeOrder = preferencesMtSetting.readSettingString(getContext(), preferencesMtSetting.getCodeOrder());

        return builder
                .setTitle("Сообщение...")
                .setMessage("Вы хотите отменить заказ?")
                .setIcon(R.drawable.icons_messege_error)
                .setPositiveButton("OK", (dialog, which) ->
                {
                    if (statusOrder.equals("Edit"))
                        Delete_Create("base_RN_Edit");
                    else Delete_Create("base_RN");

                    Log.e(logeTAG, "Заказ отменен");
                    removable.restartAdapter(true);
                })
                .setNegativeButton("Отмена", (dialog, which) ->
                {
                    Log.e(logeTAG, "Заказ не отменен");
                    removable.restartAdapter(false);
                })
                .create();
    }


    // Удаление созданных или измененных данных
    protected void Delete_Create(String tableName) {
        try {
            PreferencesWrite preferencesWrite = new PreferencesWrite(getContext());
            SQLiteDatabase db = getActivity().getApplicationContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
            db.execSQL("DELETE FROM '" + tableName + "' WHERE Kod_RN = '" + codeOrder + "'");
            db.close();
            Log.e(logeTAG, "Данные из таблицы " + tableName + ", успешно удаленны");
        } catch (SQLException sqlException) {
            Log.e(logeTAG, "Ошибка удаления: " + sqlException);
        } catch (Exception e) {
            Log.e(logeTAG, "Ошибка удаления: " + e);
        }
    }
}