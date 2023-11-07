package kg.roman.Mobile_Torgovla.TEST;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import kg.roman.Mobile_Torgovla.FTP.FtpAction;
import kg.roman.Mobile_Torgovla.FTP.FtpAsyncTask_RN_Up_Excel;
import kg.roman.Mobile_Torgovla.FTP.FtpConnection;
import kg.roman.Mobile_Torgovla.ListSimple.ListAdapterSimple_Excel;
import kg.roman.Mobile_Torgovla.MailSenderClass;
import kg.roman.Mobile_Torgovla.R;

public class WJ_Fragment extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_fragment);

    }


}
