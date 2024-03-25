package kg.roman.Mobile_Torgovla.TEST;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import kg.roman.Mobile_Torgovla.ArrayList.ListAdapterSimple_Login;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Admin_List;
import kg.roman.Mobile_Torgovla.ListAdapter.ListAdapterAde_Login;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Admin_List;
import kg.roman.Mobile_Torgovla.R;

public class WJ_TEST_ADMIN extends AppCompatActivity {
    public ArrayList<ListAdapterSimple_Login> login_aut = new ArrayList<ListAdapterSimple_Login>();
    public ListAdapterAde_Login adapterPriceClients;

    public ArrayList<ListAdapterSimple_Admin_List> brends_check = new ArrayList<ListAdapterSimple_Admin_List>();
    public ListAdapterAde_Admin_List adapterPriceClients_check;

    public ListView listView_brends;
    public Spinner spinner_agents;
    public Context context_Avtivity = WJ_TEST_ADMIN.this;
    public Button button_clear, button_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout_admin);

        listView_brends = findViewById(R.id.list_brends);
        spinner_agents = findViewById(R.id.spinner_agents);
        button_clear = findViewById(R.id.button_list_clear);
        button_all = findViewById(R.id.button_list_all);

        login_aut.clear();
        Loading_Spinner();
        adapterPriceClients = new ListAdapterAde_Login(context_Avtivity, login_aut);
        adapterPriceClients.notifyDataSetChanged();
        spinner_agents.setAdapter(adapterPriceClients);

        brends_check.clear();
        Loading_List(false);
        adapterPriceClients_check = new ListAdapterAde_Admin_List(context_Avtivity, brends_check);
        adapterPriceClients.notifyDataSetChanged();
        listView_brends.setAdapter(adapterPriceClients_check);

      /*  listView_brends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
                // Get user selected item.
                Object itemObject = adapterView.getAdapter().getItem(itemIndex);
                // Translate the selected item to DTO object.
                ListAdapterSimple_Admin_List itemDto = (ListAdapterSimple_Admin_List)itemObject;
                // Get the checkbox.
                CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.checkBox);
                // Reverse the checkbox and clicked item check state.
                if(itemDto.isChecked())
                {
                    itemCheckbox.setChecked(false);
                    itemDto.setChecked(false);
                }else
                {
                    itemCheckbox.setChecked(true);
                    itemDto.setChecked(true);
                }
            }
        });*/

        listView_brends.setLongClickable(true);
        listView_brends.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.e("Clicable", "LONG1");
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                int size = brends_check.size();
                for(int i=0;i<size;i++)
                {
                    ListAdapterSimple_Admin_List dto = brends_check.get(i);
                      dto.setChecked(true);
                    //  dto.setCheckedALL(true);
                    checkBox.setChecked(true);

                }
                adapterPriceClients_check.notifyDataSetChanged();
                return false;
            }
        });
        button_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                int size = brends_check.size();
                for(int i=0;i<size;i++)
                {
                    ListAdapterSimple_Admin_List dto = brends_check.get(i);
                    dto.setChecked(true);
                  //  dto.setCheckedALL(true);
                  //  checkBox.setChecked(true);

                }
                adapterPriceClients_check.notifyDataSetChanged();

              /*  brends_check.clear();
                Loading_List(true);
                adapterPriceClients_check = new ListAdapterAde_Admin_List(context_Avtivity, brends_check);
                adapterPriceClients.notifyDataSetChanged();
                listView_brends.setAdapter(adapterPriceClients_check);*/
            }
        });

        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = brends_check.size();
                for(int i=0;i<size;i++)
                {
                    ListAdapterSimple_Admin_List dto = brends_check.get(i);
                    dto.setChecked(false);
                }
                //adapterPriceClients_check.notifyDataSetChanged();

            }
        });

    }

    protected void Loading_Spinner() {
        SQLiteDatabase db_insert = getBaseContext().openOrCreateDatabase("sunbell_const_db.db3", MODE_PRIVATE, null);
        String query = "SELECT * FROM const_agents";
        final Cursor cursor = db_insert.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String uid = cursor.getString(cursor.getColumnIndexOrThrow("uid_name"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String region = cursor.getString(cursor.getColumnIndexOrThrow("region"));
            String kod_mobile = cursor.getString(cursor.getColumnIndexOrThrow("kod_mobile"));
            String type_user = cursor.getString(cursor.getColumnIndexOrThrow("type_user"));
            String cena = cursor.getString(cursor.getColumnIndexOrThrow("cena"));
            String sklad = cursor.getString(cursor.getColumnIndexOrThrow("skald"));
            if (type_user.equals("Агент")) {
                login_aut.add(new ListAdapterSimple_Login(name, ""));
            }
            cursor.moveToNext();
        }
        cursor.close();
        db_insert.close();
    }

    protected void Loading_List(Boolean type_check) {
        SQLiteDatabase db_insert = getBaseContext().openOrCreateDatabase("sunbell_base_db.db3", MODE_PRIVATE, null);
        String query = "SELECT * FROM base_in_brends_id;";
        final Cursor cursor = db_insert.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String brends = cursor.getString(cursor.getColumnIndexOrThrow("brends"));
            String kod = cursor.getString(cursor.getColumnIndexOrThrow("kod"));
            brends_check.add(new ListAdapterSimple_Admin_List(brends, kod, type_check));
            cursor.moveToNext();
        }
        cursor.close();
        db_insert.close();
    }

}