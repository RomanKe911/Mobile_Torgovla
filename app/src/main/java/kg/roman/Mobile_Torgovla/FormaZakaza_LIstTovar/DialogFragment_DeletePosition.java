package kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import kg.roman.Mobile_Torgovla.FormaZakaza_EndZakaz.Async_ViewModel_EndZakaz;
import kg.roman.Mobile_Torgovla.R;

public class DialogFragment_DeletePosition extends DialogFragment {
    private RemovableDeletePosition removableDeletePosition;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        removableDeletePosition = (RemovableDeletePosition) context;
    }

    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    String logeTAG = "DialogEndZakaz";
    int position;

    Async_ViewModel_EndZakaz model;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mSettings = getActivity().getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View localView = getLayoutInflater().inflate(R.layout.dialog_endzakaz, null);
        position = getArguments().getInt("deletePosition");
       // position = getArguments().getInt("deleteZakaz");
        return builder
                .setTitle("Сообщение...")
                .setMessage("Вы хотите удалить позицию?")
                .setIcon(R.drawable.office_title_forma_zakaz)
              //  .setView(localView)
                .setPositiveButton("OK", (dialog, which) ->
                {
                     removableDeletePosition.restartAdapterDeletePosition(true, position);
                })
                .setNegativeButton("Отмена", (dialog, which) ->
                {
                   removableDeletePosition.restartAdapterDeletePosition(false, 0);
                })
                .create();
    }


}