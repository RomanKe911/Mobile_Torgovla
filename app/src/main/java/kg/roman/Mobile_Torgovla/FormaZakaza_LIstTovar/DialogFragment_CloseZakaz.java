package kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import kg.roman.Mobile_Torgovla.R;

public class DialogFragment_CloseZakaz extends DialogFragment {
    private Removable removable;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        removable = (Removable) context;
    }

    String logeTAG = "DialogCloseZakaz";
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        return builder
                .setTitle("Сообщение...")
                .setMessage("Вы хотите отменить заказ?")
                .setIcon(R.drawable.office_title_forma_zakaz)
                .setPositiveButton("OK", (dialog, which) ->
                {
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
}