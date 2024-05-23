package kg.roman.Mobile_Torgovla.FormaZakazaStart;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_WJ_Zakaz;
import kg.roman.Mobile_Torgovla.MT_MyClassSetting.PreferencesWrite;
import kg.roman.Mobile_Torgovla.R;
import kg.roman.Mobile_Torgovla.databinding.WjSwipeEditorBinding;

public class EditorFormaZakazaScreen extends AppCompatActivity {
    public ArrayList<ListAdapterSimple_WJ_Zakaz> listposition = new ArrayList<>();
    public RecyclerView_Adapter_ViewHolder_Position adapterPosition;


    ////////////  02.2024
    public WjSwipeEditorBinding binding;
    String logeTAG = "WjEditorScreen";
    PreferencesWrite preferencesWrite;
    public Context context_Activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = WjSwipeEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context_Activity = getApplication().getBaseContext();

        Bundle arguments = getIntent().getExtras();

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.icon_toolbar_forma_zakaz);
        assert arguments != null;
        getSupportActionBar().setTitle(Objects.requireNonNull(arguments.get("selectClient")).toString());
        getSupportActionBar().setSubtitle(Objects.requireNonNull(arguments.get("selectKod")).toString());


        listposition.clear();
        adapterPosition = new RecyclerView_Adapter_ViewHolder_Position(getBaseContext(), selectZakaz(Objects.requireNonNull(arguments.get("selectKod")).toString()));
        binding.RecyclerViewListTovar.setAdapter(adapterPosition);
        adapterPosition.notifyDataSetChanged();
        AvtoSumma(Objects.requireNonNull(arguments.get("selectKod")).toString());
    }

    // кнопка назад
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent_prefActivity = new Intent(context_Activity, WJ_Forma_Zakaza.class);
        intent_prefActivity.putExtra("preferenceSave", "save");
        startActivity(intent_prefActivity);
        finish();
    }

    //// Загрузка данных
    protected ArrayList<ListAdapterSimple_WJ_Zakaz> selectZakaz(String kodRN) {
        preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db = context_Activity.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String querySumma = "SELECT * FROM base_RN WHERE Kod_RN= '" + kodRN + "' ORDER BY Name";
        Cursor cursor = db.rawQuery(querySumma, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String kodUniv = cursor.getString(cursor.getColumnIndexOrThrow("Kod_Univ"));
            String kodUID = cursor.getString(cursor.getColumnIndexOrThrow("koduid"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
            String Count = cursor.getString(cursor.getColumnIndexOrThrow("Kol"));
            String cena = cursor.getString(cursor.getColumnIndexOrThrow("Cena"));
            String summa = cursor.getString(cursor.getColumnIndexOrThrow("Summa"));
            String skidka = cursor.getString(cursor.getColumnIndexOrThrow("Skidka"));
            String image = cursor.getString(cursor.getColumnIndexOrThrow("Image"));
            // String cena_sk = (cursor.getString(cursor.getColumnIndex("Cena_SK"))).replace(",", ".");
            String cena_sk = (cursor.getString(cursor.getColumnIndexOrThrow("Cena_SK")));
            String itogo = cursor.getString(cursor.getColumnIndexOrThrow("Itogo"));
            String Format_cena_sk;
            if (Double.parseDouble(cena_sk) > 0 | !cena_sk.equals(null)) {
                Format_cena_sk = new DecimalFormat("#00.00").format(Double.parseDouble(cena_sk));
            } else Format_cena_sk = "";
            listposition.add(new ListAdapterSimple_WJ_Zakaz(name, kodUID, kodUniv, Count, cena, Format_cena_sk, summa, skidka, itogo, image));
            cursor.moveToNext();
        }

        cursor = db.rawQuery("SELECT * FROM base_RN_All WHERE kod_rn = '" + kodRN + "'", null);
        cursor.moveToFirst();
        String queryComment = cursor.getString(cursor.getColumnIndexOrThrow("coment"));
        binding.textViewThisComit.setText(queryComment.substring(queryComment.indexOf("cmn_") + 4));
        cursor.close();

        db.close();
        return listposition;
    }

    //// Автосумма товара по номеру накладной
    protected void AvtoSumma(String kodRN) {
        TextView textView_Summa, textView_Itogo, textView_Count;
        textView_Summa = findViewById(R.id.textView_this_summa);
        textView_Count = findViewById(R.id.textView_this_count);
        textView_Itogo = findViewById(R.id.textView_this_itog);
        textView_Summa.setText("");
        textView_Count.setText("");
        textView_Itogo.setText("");


        preferencesWrite = new PreferencesWrite(context_Activity);
        SQLiteDatabase db = context_Activity.openOrCreateDatabase(preferencesWrite.PEREM_DB3_RN, MODE_PRIVATE, null);
        String querySumma = "SELECT Kod_RN, SUM(Summa) AS 'AvtoSum' FROM base_RN WHERE Kod_RN = '" + kodRN + "';";
        String queryCount = "SELECT Kod_RN, SUM(Kol) AS 'AvtoCount' FROM base_RN WHERE Kod_RN = '" + kodRN + "';";
        String queryItogo = "SELECT Kod_RN, SUM(Itogo) AS 'AvtoItogo' FROM base_RN WHERE Kod_RN = '" + kodRN + "';";
        Cursor cursor = db.rawQuery(querySumma, null);
        //// Подсчет общей суммы
        cursor.moveToFirst();
        Log.e(logeTAG, "count: " + cursor.getCount());
        if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndexOrThrow("AvtoSum")) != null) {
            String s = cursor.getString(cursor.getColumnIndexOrThrow("AvtoSum"));
            textView_Summa.setText(String.valueOf(Double.parseDouble(s)));
        } else textView_Summa.setText("00.00");
        cursor.close();

        //// Подсчет общего кол-ва
        cursor = db.rawQuery(queryCount, null);
        cursor.moveToFirst();
        Log.e(logeTAG, "count: " + cursor.getCount());
        if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndexOrThrow("AvtoCount")) != null) {
            textView_Count.setText(cursor.getString(cursor.getColumnIndexOrThrow("AvtoCount")) + " шт");
        } else textView_Count.setText("0");

        //// Подсчет общего суммы со скидкой
        cursor = db.rawQuery(queryItogo, null);
        cursor.moveToFirst();
        Log.e(logeTAG, "countIt: " + cursor.getCount());
        if (cursor.getCount() > 0 && cursor.getString(cursor.getColumnIndexOrThrow("AvtoItogo")) != null) {
            String s = cursor.getString(cursor.getColumnIndexOrThrow("AvtoItogo"));
            textView_Itogo.setText(String.valueOf(Double.parseDouble(s)));
        } else textView_Itogo.setText("00.00");

        cursor.close();
        db.close();
    }
}