package kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import kg.roman.Mobile_Torgovla.FormaZakaza_EndZakaz.Async_ViewModel_EndZakaz;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.PreferencesWrite;
import kg.roman.Mobile_Torgovla.R;

public class DialogFragment_EditPosition extends DialogFragment {
    private RemovableEditPosition removableEditPosition;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        removableEditPosition = (RemovableEditPosition) context;
    }

    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    String logeTAG = "DialogEditPosition";
    int position = 0;

    Async_ViewModel_EndZakaz model;
    Button buttonCountPlus, buttonCountMinus;
    LinearLayout linearLayout;
    TextView textViewMaxCount;
    EditText editTextCount;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mSettings = getActivity().getApplication().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        // context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View localView = getLayoutInflater().inflate(R.layout.dialog_endzakaz_edit, null);

        editTextCount = localView.findViewById(R.id.editTextNewCount);
        buttonCountPlus = localView.findViewById(R.id.button_CountPlus);
        buttonCountMinus = localView.findViewById(R.id.button_CountMinus);
        textViewMaxCount = localView.findViewById(R.id.textViewCountMax);
        linearLayout = localView.findViewById(R.id.LinearLayout_PanelMaxCount);

        position = getArguments().getInt("position");
        int count = getArguments().getInt("count");
        String uid_tovar = getArguments().getString("uid");
        editTextCount.setText("" + count);


        try {
            buttonCountPlus.setOnClickListener(v ->
            {
                int countEditText = Integer.parseInt(editTextCount.getText().toString());
                Log.e(logeTAG, "Count " + countEditText);
                editTextCount.setText(countPlus(countEditText, uid_tovar));
            });

        } catch (Exception e) {
            Log.e(logeTAG, "Ошибка, не верные данные: кнопка добавить");
            ToastOverride("Ошибка, не верные данные: кнопка добавить");
        }

        try {
            buttonCountMinus.setOnClickListener(v ->
            {
                int countEditText = Integer.parseInt(editTextCount.getText().toString());
                editTextCount.setText(countMinus(countEditText));
            });
        } catch (Exception e) {
            Log.e(logeTAG, "Ошибка, не верные данные: кнопка уменьшить");
            ToastOverride("Ошибка, не верные данные: кнопка уменьшить");
        }


        return builder
                .setTitle("Сообщение...")
                .setMessage("Вы хотите изменить кол-во товара?")
                .setIcon(R.drawable.office_title_forma_zakaz)
                .setView(localView).
                setPositiveButton("OK", (dialog, which) ->
                {
                    int newCount;
                    if (!editTextCount.getText().toString().equals(""))
                        newCount = Integer.parseInt(editTextCount.getText().toString());
                    else newCount = count;

                    removableEditPosition.restartAdapterEditPosition(true, position,
                            new Class_contentData(uid_tovar, newCount, 0, "", 0, ""));
                })
                .setNegativeButton("Отмена", (dialog, which) ->
                {
                    //  removableEditPosition.restartAdapterEditPosition(true, position, 0, uid);
                    removableEditPosition.restartAdapterEditPosition(true, position,
                            new Class_contentData(uid_tovar, count, 0, "", 0, ""));
                })
                .create();
    }


    protected String countPlus(int count, String uid) {
        count = count + 1;
        int maxCount = SQLCountSklad(SQLUID_Sklad(uid), uid);
        linearLayout.setVisibility(View.VISIBLE);
        textViewMaxCount.setText(String.valueOf(maxCount));
        if (maxCount >= count) {
            buttonCountMinus.setClickable(true);
            buttonCountPlus.setClickable(true);
            return String.valueOf(count);

        } else {
            ToastOverride("кол-во не может превышать остаток на складе");
            buttonCountPlus.setClickable(false);
            return String.valueOf(maxCount);
        }
    }

    protected String countMinus(int count) {
        linearLayout.setVisibility(View.INVISIBLE);
        count = count - 1;
        if (count >= 1) {
            buttonCountMinus.setClickable(true);
            buttonCountPlus.setClickable(true);
            return String.valueOf(count);
        } else {
            ToastOverride("кол-во не может быть равно 0");
            buttonCountMinus.setClickable(false);
            return String.valueOf(1);
        }

    }

    //// Остаток товара на складе по выбранной позиции
    protected int SQLCountSklad(String uid_sklad, String uid_tovar) {
        int count_Sklad = 0;
        PreferencesWrite preferencesWrite = new PreferencesWrite(getActivity());
        SQLiteDatabase db = getActivity().openOrCreateDatabase(preferencesWrite.PEREM_DB3_BASE, MODE_PRIVATE, null);
        String query = "SELECT * FROM base_in_ostatok WHERE sklad_uid = '" + uid_sklad + "' AND nomenclature_uid = '" + uid_tovar + "'";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            count_Sklad = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("count")));
        } else count_Sklad = 0;
        cursor.close();
        db.close();
        return count_Sklad;
    }

    //// Получение кода UID-склада
    protected String SQLUID_Sklad(String uid) {
        String uid_sklad = "";
        PreferencesWrite preferencesWrite = new PreferencesWrite(getActivity());
        SQLiteDatabase db = getActivity().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT Kod_RN, koduid, Name, sklad_uid FROM base_RN WHERE Kod_RN = '" + preferencesWrite.Setting_MT_K_AG_KodRN + "' AND koduid = '" + uid + "';";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            uid_sklad = cursor.getString(cursor.getColumnIndexOrThrow("sklad_uid"));
        } else uid_sklad = preferencesWrite.Setting_AG_UID_SKLAD;
        cursor.close();
        db.close();
        return uid_sklad;
    }

    //// Всплывающие окно
    protected void ToastOverride(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

}