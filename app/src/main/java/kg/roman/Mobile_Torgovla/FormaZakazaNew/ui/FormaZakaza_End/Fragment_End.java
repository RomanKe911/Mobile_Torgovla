package kg.roman.Mobile_Torgovla.FormaZakazaNew.ui.FormaZakaza_End;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.DecimalFormat;
import java.util.ArrayList;

import kg.roman.Mobile_Torgovla.FormaZakaza_EndZakaz.DialogFragment_EndZakaz;
import kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar.Removable;
import kg.roman.Mobile_Torgovla.FormaZakazaStart.WJ_Forma_Zakaza;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Spinner_TY;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Spinner_TY;
import kg.roman.Mobile_Torgovla.MT_FTP.CalendarThis;
import kg.roman.Mobile_Torgovla.MT_FTP.PreferencesWrite;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.databinding.FragmentNotificationsBinding;

public class Fragment_End extends Fragment implements Removable {

    public ArrayList<ListAdapterSimple_Spinner_TY> spinner_ty_add = new ArrayList<ListAdapterSimple_Spinner_TY>();
    public ListAdapterAde_Spinner_TY adapterPriceClients_ty;

    private FragmentNotificationsBinding binding;
    String logeTAG = "WjFormaDopUslov";
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    CalendarThis calendars = new CalendarThis();
    public Context context_Activity;
    PreferencesWrite preferencesWrite;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewModel_End notificationsViewModel =
                new ViewModelProvider(this).get(ViewModel_End.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        context_Activity = getActivity().getBaseContext();
        mSettings = getActivity().getApplication().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        createClientName();
        createSumma();
        createSkidka();

        binding.floatingActionButton.setOnClickListener(v -> {
            try {
                preferencesWrite = new PreferencesWrite(context_Activity);
                if (!preferencesWrite.Setting_MT_K_AG_NAME.equals("")) {

                    PreferencesWrite preferencesWrite = new PreferencesWrite(context_Activity);
                    DialogFragment_EndZakaz dialog = new DialogFragment_EndZakaz();
                    Bundle args = new Bundle();
                    args.putString("client", preferencesWrite.Setting_MT_K_AG_NAME);
                    args.putString("itogo", preferencesWrite.Setting_TY_Itogo);
                    dialog.setArguments(args);
                    dialog.show(getActivity().getSupportFragmentManager(), "customEndZakaz");
                } else {
                   // Loading_Adapter_Refresh();
                    Log.e("Кнопка Add...", "Ошибка: Не заполнен заказ или не выбраны условия!");
                    Toast.makeText(context_Activity, "Ошибка: Не заполнен заказ или не выбраны условия!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("Кнопка Add...", "Ошибка обработки кнопки");
                OverrideToast("Ошибка обработки кнопки");
            }
        });








/*        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe( getViewLifecycleOwner(), textView::setText);*/
        Log.e("Home", "Шапка");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    ///////////////// 02.2024

    /////////////////////  Заполнение данных
    /// Поле контрагента
    protected void createClientName() {
        preferencesWrite = new PreferencesWrite(context_Activity);
        if (!preferencesWrite.Setting_MT_K_AG_NAME.equals("null"))
            binding.tvwClients.setText(preferencesWrite.Setting_MT_K_AG_NAME);
        else {
            binding.tvwClients.setText("--------------");
            OverrideToast("не выбран контраген");
        }
    }

    /// Поле раздела, скидка и сумма
    protected void createSumma() {
        preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT base_RN.Data, base_RN.Kod_RN, " +
                "SUM(base_RN.Summa) AS 'new_summa', base_RN.Skidka, SUM(base_RN.Itogo) AS 'new_itogo', base_RN.aks_pref\n" +
                "FROM base_RN\n" +
                "WHERE base_RN.Kod_RN = '" + preferencesWrite.Setting_MT_K_AG_KodRN + "' AND base_RN.Data = '" + calendars.getThis_DateFormatSqlDB + "';";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String kod_rn = cursor.getString(cursor.getColumnIndexOrThrow("Kod_RN"));
        String summa = cursor.getString(cursor.getColumnIndexOrThrow("new_summa"));
        if (cursor.getCount() > 0)
            binding.tvwEndCount.setText(summa);
        else {
            binding.tvwEndCount.setText("0.0");
            binding.tvwEndCount2.setText("0.0");
            OverrideToast("нет заказа или не верный формат автосуммы");
        }
        cursor.close();
        db.close();
    }

    /// Виды торговых условий
    protected void createSkidka() {
        preferencesWrite = new PreferencesWrite(context_Activity);
        switch (preferencesWrite.Setting_TY_Type) {
            case "standart": {
                Log.e(logeTAG, preferencesWrite.Setting_TY_Type);

                binding.editEndSkidka.setVisibility(View.INVISIBLE);
                binding.spinnerSkidka.setVisibility(View.VISIBLE);

                try {
                    adapterPriceClients_ty = new ListAdapterAde_Spinner_TY(context_Activity, Spinner_TY());
                    adapterPriceClients_ty.notifyDataSetChanged();
                    binding.spinnerSkidka.setAdapter(adapterPriceClients_ty);
                    binding.spinnerSkidka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try {
                                TextView tvw_d_skidka = view.findViewById(R.id.tvw_ty_skidka);
                                TextView tvw_d_summa = view.findViewById(R.id.tvw_ty_summa);
                                SQL_Data(tvw_d_skidka.getText().toString(), tvw_d_summa.getText().toString());
                            } catch (Exception e) {
                                Log.e("Error Spinner ", "Ошибка данных!");
                                Toast.makeText(context_Activity, "Ошибка данных!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } catch (Exception e) {
                    Log.e("Error Spinner ", "Ошибка данных!");
                    Toast.makeText(context_Activity, "Ошибка данных!", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case "random_ty": {
                Log.e(logeTAG, preferencesWrite.Setting_TY_Type);


                binding.spinnerSelectCredit.setVisibility(View.GONE);
                if (!binding.editEndSkidka.getText().toString().isEmpty()) {
                    SQL_Data(binding.editEndSkidka.getText().toString(), EditText_TY());
                } else SQL_Data("0", EditText_TY());


                binding.editEndSkidka.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!binding.editEndSkidka.getText().toString().isEmpty()) {
                            SQL_Data(binding.editEndSkidka.getText().toString(), EditText_TY());
                        } else SQL_Data("0", EditText_TY());
                    }
                });
            }
            break;
            default: {
                Log.e("Error Spinner ", "Null");
                binding.spinnerSelectCredit.setVisibility(View.VISIBLE);
                Spinner_Select_TY();
            }
            break;
        }
    }


    /// Загрузк данны для Спинера скидок
    protected ArrayList<ListAdapterSimple_Spinner_TY> Spinner_TY() {

        ArrayList<ListAdapterSimple_Spinner_TY> spinnerData = new ArrayList<>();
        spinnerData.clear();
        try {
            preferencesWrite = new PreferencesWrite(context_Activity);
            SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_CONST, MODE_PRIVATE, null);
            String query = "SELECT * FROM const_ty WHERE region = '" + preferencesWrite.PEREM_FTP_PathData + "'";
            final Cursor cursor = db.rawQuery(query, null);
            //mass_ty = new String[cursor.getCount()][3];
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String region = cursor.getString(cursor.getColumnIndexOrThrow("region"));
                    String count_skidka = cursor.getString(cursor.getColumnIndexOrThrow("count_skidka"));
                    String type_skidka = cursor.getString(cursor.getColumnIndexOrThrow("type_skidka"));
                    String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                    String summa_min = cursor.getString(cursor.getColumnIndexOrThrow("summa_min"));
                    String kyrc = cursor.getString(cursor.getColumnIndexOrThrow("kyrc"));
                    spinnerData.add(new ListAdapterSimple_Spinner_TY(count_skidka, summa_min, "ТУ=", type_skidka, status, kyrc));
                    // spinner_ty_add.add(new ListAdapterSimple_Spinner_TY(count_skidka, summa_min, "ТУ=", type_skidka, status, kyrc));
                    cursor.moveToNext();
                }
            } else Toast.makeText(context_Activity, "Нет данных по ТУ", Toast.LENGTH_SHORT).show();
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.e(logeTAG, "Ошибка: нет данных по торговым условиям!");
            Toast.makeText(getActivity(), "Произошла ошибка!", Toast.LENGTH_LONG).show();
        }

        for (ListAdapterSimple_Spinner_TY st : spinnerData)
            Log.e(logeTAG, "Пустой: " + spinnerData.isEmpty() +"_"+st.skidka);
        return spinnerData;
    }


    protected void OverrideToast(String text) {
        Toast.makeText(context_Activity, text, Toast.LENGTH_SHORT).show();
    }

    // Обработка данных из спинера вид торговых условий
    protected String EditText_TY() {
        String summa_min_skidka = "";
        try {
            preferencesWrite = new PreferencesWrite(context_Activity);
            SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_CONST, MODE_PRIVATE, null);
            String query = "SELECT * FROM const_ty WHERE region = '" + preferencesWrite.PEREM_FTP_PathData + "'";
            final Cursor cursor = db.rawQuery(query, null);
            //mass_ty = new String[cursor.getCount()][3];
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    String region = cursor.getString(cursor.getColumnIndexOrThrow(("region")));
                    String count_skidka = cursor.getString(cursor.getColumnIndexOrThrow(("count_skidka")));
                    String type_skidka = cursor.getString(cursor.getColumnIndexOrThrow(("type_skidka")));
                    String status = cursor.getString(cursor.getColumnIndexOrThrow(("status")));
                    String summa_min = cursor.getString(cursor.getColumnIndexOrThrow(("summa_min")));
                    String kyrc = cursor.getString(cursor.getColumnIndexOrThrow(("kyrc")));

                    Log.e("Error Spinner ", "C " + cursor.getPosition());

                    if (cursor.getPosition() == 2) {

                        summa_min_skidka = summa_min;
                        //  getSupportActionBar().setSubtitle("мин. сумма закупа: " + summa_min);
                    }
                    // spinner_ty_add.add(new ListAdapterSimple_Spinner_TY(count_skidka, summa_min, "ТУ=", type_skidka, status, kyrc));
                    cursor.moveToNext();
                }
            } else Toast.makeText(context_Activity, "Нет данных по ТУ", Toast.LENGTH_SHORT).show();
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.e(logeTAG, "Ошибка: нет данных по торговым условиям!");
            Toast.makeText(getActivity(), "Произошла ошибка!", Toast.LENGTH_LONG).show();
        }
        return summa_min_skidka;

    }


    protected void Spinner_Select_TY() {


    }


    private void SQL_Data(String sp_skidka, String sp_summa) {
        String summa_read, itogo_read, skidka_read, nds_read, date_credite_read, summa_read_edit, summa_min_skidka;
        Boolean visible_ty = false;
        TextView text_summa, text_itogo, text_data, text_no_ty, text_title_name_skidka;


        Log.e("SQLData: ", sp_skidka + " " + sp_summa);
        preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db = getActivity().getBaseContext().openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String query = "SELECT base_RN.Kod_RN, SUM(base_RN.Summa) AS 'new_summa', " +
                "base_RN.Skidka, SUM(base_RN.Itogo) AS 'new_itogo', base_RN.aks_pref\n" +
                "FROM base_RN\n" +
                "WHERE base_RN.Kod_RN = '" + preferencesWrite.Setting_MT_K_AG_KodRN + "'";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String kod_rn = cursor.getString(cursor.getColumnIndexOrThrow("Kod_RN"));
        String summa = cursor.getString(cursor.getColumnIndexOrThrow("new_summa"));
        String skidka = cursor.getString(cursor.getColumnIndexOrThrow("Skidka"));
        String itogo = cursor.getString(cursor.getColumnIndexOrThrow("new_itogo"));
        String prefics = cursor.getString(cursor.getColumnIndexOrThrow("aks_pref"));

        Log.e("TAFS", "SQL_Data: " + summa + "__" + sp_summa);
        if (kod_rn != null & cursor.getCount() > 0) {
            summa_read = summa;
            binding.tvwEndCount.setText(summa);
            if (Double.valueOf(summa) >= Double.valueOf(sp_summa)) {
                // text_no_ty.setVisibility(View.GONE);
                Double d_summa = Double.parseDouble(summa);
                Double d_skidka = Double.parseDouble(sp_skidka);
                String new_itogo = new DecimalFormat("#00.00").format(d_summa - (d_summa * (d_skidka / 100)));
                //itogo_read = new_itogo.replaceAll(",", ".");
                binding.tvwEndCount2.setText(new_itogo.replaceAll(",", "."));
                skidka_read = sp_skidka;
                visible_ty = false;
            } else {
                if (preferencesWrite.Setting_TY_Type.equals("standart")) {
                    //  text_no_ty.setVisibility(View.VISIBLE);
                } else binding.tvwEndCount2.setVisibility(View.GONE);
                visible_ty = true;
                itogo_read = "0.0";
                binding.tvwEndCount.setText(itogo_read);
                skidka_read = "0";
            }

        } else {
            summa_read = "0.0";
            itogo_read = "0.0";
            Toast.makeText(context_Activity, "Нет данных по ТУ", Toast.LENGTH_SHORT).show();
        }


        // Log.e("Сумма: ", summa_read + " " + itogo_read);
        cursor.close();
        db.close();
    }

    @Override
    public void restartAdapter(boolean statusAdapter) {
        if (statusAdapter) {
            Intent intent = new Intent(context_Activity, WJ_Forma_Zakaza.class);
            startActivity(intent);
           // finish();
        }
    }
}