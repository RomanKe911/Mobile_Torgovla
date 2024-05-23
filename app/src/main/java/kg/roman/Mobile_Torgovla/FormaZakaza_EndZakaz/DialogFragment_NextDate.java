package kg.roman.Mobile_Torgovla.FormaZakaza_EndZakaz;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import kg.roman.Mobile_Torgovla.MT_MyClassSetting.CalendarThis;
import kg.roman.Mobile_Torgovla.R;

public class DialogFragment_NextDate extends DialogFragment {

    private RemovableNextDate selectNextDate;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        selectNextDate = (RemovableNextDate) context;
    }

    String logeTAG = "DialogNextDate";
    String textNextDate = "";

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View localView = getLayoutInflater().inflate(R.layout.dialog_mess_next_date, null);
        ImageButton buttonDate = localView.findViewById(R.id.btnNextDate);
        TextView textViewDate = localView.findViewById(R.id.tvwNextDate);

        buttonDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    (viewCalendare, year, monthOfYear, dayOfMonth) -> {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat dateFormat_display = new SimpleDateFormat("dd.MM.yyyy");
                        textViewDate.setText(dateFormat_display.format(calendar.getTime()));
                        textNextDate = dateFormat_display.format(calendar.getTime());
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        return builder
                .setTitle("Сообщение...")
                .setMessage("Выберите дату доставки заказа:")
                .setIcon(R.drawable.icons_messege_info)
                .setView(localView)
                .setPositiveButton("OK", (dialog, which) ->
                {
                    try {
                        Log.e(logeTAG, "selectDate: " + textNextDate);
                        if (!textNextDate.equals(""))
                            selectNextDate.RemovableNextDate("" + textNextDate);
                        else Toast.makeText(getContext(), "выберите дату доставки заказа", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    {
                        Log.e(logeTAG, "errorDate: " + e);
                    }

                })
                .setNegativeButton("Отмена", (dialog, which) ->
                {
/*                    CalendarThis calendarThis = new CalendarThis();
                    Log.e("DialogNextDate", "closeDate: " + calendarThis.Day + 1 + "." + calendarThis.Month + "." + calendarThis.Year);
                    selectNextDate.RemovableNextDate(calendarThis.Day + 1 + "." + calendarThis.Month + "." + calendarThis.Year);*/
                })
                .create();
    }


}