package kg.roman.Mobile_Torgovla.Setting;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.io.File;

import kg.roman.Mobile_Torgovla.R;

public class DialogFragment_DeletePreference extends DialogFragment {

    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    String logeTAG = "DialogClear";
    String messegeDialog, typeClear;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        switch (getArguments().getString("typeClear")) {
            case "clearPreference":
                messegeDialog = "Вы хотите очистить все настройки приложения?";
                break;
            case "clearImageManager":
                messegeDialog = "Вы хотите очистить все картинки товара?";
                break;
            case "clearImageManagerIcons":
                messegeDialog = "Вы хотите очистить все картинки иконок?";
                break;
        }


        return builder
                .setTitle("Сообщение...")
                .setMessage(messegeDialog)
                .setIcon(R.drawable.office_title_forma_zakaz)
                //  .setView(localView)
                .setPositiveButton("OK", (dialog, which) ->
                {
                    SelectClear(getArguments().getString("typeClear"));
                })
                .setNegativeButton("Отмена", (dialog, which) ->
                {

                })
                .create();
    }

    protected void SelectClear(String type) {
        switch (type) {
            case "clearPreference": {
                mSettings = getActivity().getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                editor = mSettings.edit();
                editor.clear();    //Очистить базу
                editor.commit();
                Toast.makeText(getContext(), "Настройки приложения удачно сброшены", Toast.LENGTH_SHORT).show();
                Log.e(logeTAG, "Настройки приложения удачно сброшены");
            }
            break;
            case "clearImageManager": {
                messegeDialog = "Вы хотите очистить все картинки товара?";

                File files = new File(getActivity().getFilesDir().getAbsolutePath()); // files.getPath() + "/Icons/bg_bg000115.png";
                File file_reg = new File(files.getPath() + "/Image");
                for (File f : file_reg.listFiles()) {
                    Log.e(logeTAG, "file: " + f.getName());
                    f.delete();
                }
                Toast.makeText(getContext(), "Картинки товара удачно удалены с устройства", Toast.LENGTH_SHORT).show();
                Log.e(logeTAG, "Картинки товара удачно удалены с устройства");

            }
            break;
            case "clearImageManagerIcons": {
                messegeDialog = "Вы хотите очистить все картинки иконок?";
                File files = new File(getActivity().getFilesDir().getAbsolutePath()); // files.getPath() + "/Icons/bg_bg000115.png";
                File file_reg = new File(files.getPath() + "/Icons");
                for (File f : file_reg.listFiles()) {
                    Log.e(logeTAG, "file: " + f.getName());
                    f.delete();
                }
                Toast.makeText(getContext(), "Картинки иконок удачно удалены с устройства", Toast.LENGTH_SHORT).show();
                Log.e(logeTAG, "Картинки иконок удачно удалены с устройства");
            }
            break;
        }
    }

}