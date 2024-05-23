package kg.roman.Mobile_Torgovla.FormaZakaza_EndZakaz;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.datastore.preferences.PreferencesProto;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import kg.roman.Mobile_Torgovla.FormaZakaza_EndZakaz.Async_ViewModel_EndZakaz;
import kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar.Removable;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.PreferencesWrite;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.Preferences_MTSetting;
import kg.roman.Mobile_Torgovla.R;

public class DialogFragment_EndZakaz extends DialogFragment {
    private Removable removable;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        removable = (Removable) context;
    }

    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    String logeTAG = "DialogEndZakaz";

    Async_ViewModel_EndZakaz model;
    String messege = null;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mSettings = getActivity().getApplication().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View localView = getLayoutInflater().inflate(R.layout.dialog_endzakaz, null);
        TextView textViewClient = localView.findViewById(R.id.dialogMessegeText_Client);
        TextView textViewItogo = localView.findViewById(R.id.dialogMessegeText_Itogo);

        textViewClient.setText(getArguments().getString("client"));
        textViewItogo.setText(getArguments().getString("itogo"));
        PreferencesWrite preferencesWrite = new PreferencesWrite(getActivity().getBaseContext());
        Preferences_MTSetting preferencesMtSetting = new Preferences_MTSetting();
        SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        preferencesMtSetting.writeSettingString(getContext(),preferencesMtSetting.getAutoSum_Itogo(), preferencesMtSetting.getItogoOrder(getContext(), db));
        db.close();

        switch (getArguments().getString("typeStatus")) {
            case "Create":
                messege = "Вы хотите оформить заказ?";
                break;
            case "Edit":
                messege = "Сохранить изменения?";
                break;
            case "Copy":
                messege = "Вы хотите оформить заказ?";
                break;
        }

        return builder
                .setTitle("Сообщение...")
                .setMessage(messege)
                .setIcon(R.drawable.office_title_forma_zakaz)
                .setView(localView).
                setPositiveButton("OK", (dialog, which) ->
                {

                    model = new ViewModelProvider(this).get(Async_ViewModel_EndZakaz.class);
                    model.getStatus().observe(this, status ->
                    {
                        if (!status)
                            removable.restartAdapter(true);
                    });
                    model.execute();

                })
                .setNegativeButton("Отмена", null)
                .create();
    }


}