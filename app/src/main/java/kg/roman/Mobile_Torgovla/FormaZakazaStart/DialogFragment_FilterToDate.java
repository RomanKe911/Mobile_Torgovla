package kg.roman.Mobile_Torgovla.FormaZakazaStart;


import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import kg.roman.Mobile_Torgovla.FormaZakazaStart.RemovableStatusFilter;
import kg.roman.Mobile_Torgovla.MT_FTP.CalendarThis;
import kg.roman.Mobile_Torgovla.R;

public class DialogFragment_FilterToDate extends DialogFragment {

    private RemovableStatusFilter remFilter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        remFilter = (RemovableStatusFilter) context;
    }

    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    private static final String APP_PREFERENCES = "kg.roman.Mobile_Torgovla_preferences";
    String logeTAG = "DialogFilterClient";
    Context context;

    public Calendar calendareThis = Calendar.getInstance();
    SimpleDateFormat dateFormatAllDisplayStart = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");
    SimpleDateFormat dateFormatAllDisplayEND = new SimpleDateFormat("dd:MM:yyyy 23:59:59");
    String strDateStart = dateFormatAllDisplayStart.format(calendareThis.getTimeInMillis());
    String strDateEnd = dateFormatAllDisplayEND.format(calendareThis.getTimeInMillis());
    boolean statusFormatDate = false;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mSettings = getActivity().getApplication().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        context = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View localView = getLayoutInflater().inflate(R.layout.dialog_mess_filters_date, null);
        Button buttonStart = localView.findViewById(R.id.btn_filter_data_start);
        Button buttonEnd = localView.findViewById(R.id.btn_filter_data_end);
        TextView textViewStart = localView.findViewById(R.id.tvw_filter_dateStart);
        TextView textViewEnd = localView.findViewById(R.id.tvw_filter_dateEnd);
        TextView textViewError = localView.findViewById(R.id.tvw_error);


        CalendarThis calendars = new CalendarThis();
        editor = mSettings.edit();
        if (mSettings.getString("setting_Filters_DateEnd", "").equals(""))
            editor.putString("setting_Filters_DateEnd", calendars.getThis_DateFormatSqlDB);
        editor.commit();

        textViewStart.setText(mSettings.getString("setting_Filters_DateStart", "YYYY-MM-DD"));
        textViewEnd.setText(mSettings.getString("setting_Filters_DateEnd", "YYYY-MM-DD"));

        if (strDateStart.compareTo(strDateEnd) >= 0)
            textViewError.setVisibility(View.VISIBLE);
        else textViewError.setVisibility(View.GONE);

        buttonStart.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    (viewCalendare, year, monthOfYear, dayOfMonth) -> {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat dateFormat_sql = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat dateFormat_display = new SimpleDateFormat("dd:MM:yyyy");
                        textViewStart.setText(dateFormat_display.format(calendar.getTime()));

                        SimpleDateFormat dateFormatAllDisplayStart = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");
                        strDateStart = dateFormatAllDisplayStart.format(calendar.getTime());

                        Log.e(logeTAG, "Дата LongStart: " + strDateStart);
                        Log.e(logeTAG, "Дата LongEnd: " + strDateEnd);

                        //// Отображение верного формата
                        if (strDateStart.compareTo(strDateEnd) >= 0) {
                            textViewError.setVisibility(View.VISIBLE);
                            statusFormatDate = false;
                        } else {
                            textViewError.setVisibility(View.GONE);
                            statusFormatDate = true;
                            // Запись даты в память
                            editor = mSettings.edit();
                            editor.putString("setting_Filters_DateStart", dateFormat_sql.format(calendar.getTime()));
                            editor.commit();
                            Log.e(logeTAG, "Верный формат " + dateFormat_sql.format(calendar.getTime()));
                        }


                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });


        buttonEnd.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    (viewCalendare, year, monthOfYear, dayOfMonth) -> {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat dateFormat_sql = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat dateFormat_display = new SimpleDateFormat("dd:MM:yyyy");
                        textViewEnd.setText(dateFormat_display.format(calendar.getTime()));

                        SimpleDateFormat dateFormatAllDisplayEND = new SimpleDateFormat("dd:MM:yyyy 23:59:59");
                        strDateEnd = dateFormatAllDisplayEND.format(calendar.getTime());

                        Log.e(logeTAG, "Дата LongStart: " + strDateStart);
                        Log.e(logeTAG, "Дата LongEnd: " + strDateEnd);

                        //// Отображение верного формата
                        if (strDateStart.compareTo(strDateEnd) >= 0) {
                            textViewError.setVisibility(View.VISIBLE);
                            statusFormatDate = false;
                        } else {
                            textViewError.setVisibility(View.GONE);
                            statusFormatDate = true;
                            editor = mSettings.edit();
                            editor.putString("setting_Filters_DateEnd", dateFormat_sql.format(calendar.getTime()));
                            editor.commit();
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });


        return builder
                .setTitle("Фильтрация данных")
                .setMessage("фильтр данных по дате")
                .setIcon(R.drawable.office_filtr_data)
                .setView(localView)
                .setPositiveButton("OK", (dialog, which) ->
                {
                    editor = mSettings.edit();
                    if (statusFormatDate) {
                        Log.e(logeTAG, "Кнопка ОК, удачно записанно");
                        editor.putBoolean("setting_Filters_Date", true);
                        editor.commit();
                        remFilter.RemovableStatusFilter(true);
                    } else {
                        Log.e(logeTAG, "Кнопка ОК, не записанно");
                        editor.putBoolean("setting_Filters_Date", false);
                        editor.commit();
                        remFilter.RemovableStatusFilter(false);
                        remFilter.RemovableStatusFilterMessege("Ошибка, не верный формат дат");
                    }


                })
                .setNegativeButton("Отмена", null)
                .create();
    }


}