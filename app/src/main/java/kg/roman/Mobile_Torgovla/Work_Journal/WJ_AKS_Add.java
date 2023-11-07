package kg.roman.Mobile_Torgovla.Work_Journal;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_Login;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_AKS_ADD;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Login;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_AKS_Add;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Excel;
import kg.roman.Mobile_Torgovla.R;

public class WJ_AKS_Add extends AppCompatActivity {

    public ArrayList<ListAdapterSimple_AKS_Add> adapter_aks = new ArrayList<ListAdapterSimple_AKS_Add>();
    public ListAdapterAde_AKS_ADD adapterPriceClients;

    public SharedPreferences.Editor ed;
    public SharedPreferences sp;
    public Context context_Activity;
    public String PEREM_Agent;

    public Button button_add, button_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_aks_add);
        sp = PreferenceManager.getDefaultSharedPreferences(context_Activity);
        ed = sp.edit();
        ed.putString("PEREM_Agent", PEREM_Agent);

        Loading_ListView();
        Panel_Button();



    }

    protected void Panel_Button() {
        button_add = (Button) findViewById(R.id.button_add);
        button_delete = (Button) findViewById(R.id.button_delete);

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    protected void Loading_ListView() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("my_w.db3", MODE_PRIVATE, null);
        String query = "SELECT brend, name_aks, catigory FROM table_1";
        final Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String brend = (cursor.getString(cursor.getColumnIndexOrThrow("brend")));
            String name_aks = (cursor.getString(cursor.getColumnIndexOrThrow("name_aks")));
            String catigory = (cursor.getString(cursor.getColumnIndexOrThrow("catigory")));
            adapter_aks.add(new ListAdapterSimple_AKS_Add(brend, name_aks, catigory));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

}
