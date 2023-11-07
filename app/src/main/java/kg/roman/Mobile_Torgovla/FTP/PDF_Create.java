package kg.roman.Mobile_Torgovla.FTP;

import static kotlinx.coroutines.BuildersKt.launch;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.media.audiofx.DynamicsProcessing;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import kg.roman.Mobile_Torgovla.R;

public class PDF_Create extends AppCompatActivity {
    final static int REQUEST_CODE = 1232;
    Button button;
    TextView textView;

    final static int CANVAS_WIDTH = 400;
    final static int CANVAS_HEIGHT = 400;

    Image bgImage;
    double bgX, bgY, bgW = 100.0, bgH = 100.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_pdf);
        // button = findViewById(R.id.text_create);
        button = findViewById(R.id.btn_create);
        textView = findViewById(R.id.text_create);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPDF(textView.getText().toString());

            }
        });


    }

    private void createPDF(String text) {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080, 1920, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(42);

        String text2 = "Hello";
        String text3 = "World";

        float x = 50;
        float y = 50;

        canvas.drawText(text, x, y, paint);
        canvas.drawText(text2, x, y + 100, paint);
        canvas.drawText(text3, x, y + 200, paint);


        Drawable drawable1 = getDrawable(R.drawable.happy_logo);
        Drawable drawable2 = getDrawable(R.drawable.logo_sm_plushe);
        //  PictureDrawable pictureDrawable = (PictureDrawable) drawable;
        // drawableToBitmap(drawable);

       /* Bitmap bm = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bm);
        canvas.drawPicture(((PictureDrawable) drawable).getPicture());*/

        Bitmap localBitmap1 = BitmapFactory.decodeResource(
                getApplicationContext().getResources(),
                R.drawable.happy_logo);

        Bitmap localBitmap2  = BitmapFactory.decodeResource(
                getApplicationContext().getResources(),
                R.drawable.logo_sm_plushe);
        canvas.drawBitmap(localBitmap1, x , y + 300, paint);
        canvas.drawBitmap(localBitmap2, x , y + 400, paint);


        document.finishPage(page);


        // Запись в вайл в файловой системе
        File dowl = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "myPDF.pdf";
        File file = new File(dowl, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();
            Log.e("Loge", "createPDF: " + text);
        } catch (FileNotFoundException e) {
            Log.e("Loge", "createPDF: ");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof PictureDrawable) {
            PictureDrawable pictureDrawable = (PictureDrawable) drawable;
            Bitmap bitmap = Bitmap.createBitmap(pictureDrawable.getIntrinsicWidth(),
                    pictureDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawPicture(pictureDrawable.getPicture());
            return bitmap;
        }
        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, 500, 500);
        drawable.draw(canvas);
        return bitmap;
    }

    private Bitmap pictureDrawableToBitmap(PictureDrawable pictureDrawable) {
        //Bitmap bmp = Bitmap.createBitmap(pictureDrawable.getIntrinsicWidth(), pictureDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Bitmap bmp = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawPicture(pictureDrawable.getPicture());
        return bmp;
    }

    private static Bitmap pictureDrawable2Bitmap(PictureDrawable pictureDrawable) {
        // Bitmap bitmap = Bitmap.createBitmap(pictureDrawable.getIntrinsicWidth(),pictureDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Bitmap bitmap = Bitmap.createBitmap(250, 250, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawPicture(pictureDrawable.getPicture());
        return bitmap;
    }


    public Bitmap drawableToBitmap(PictureDrawable pd) {
        Bitmap bm = Bitmap.createBitmap(pd.getIntrinsicWidth(), pd.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        canvas.drawPicture(pd.getPicture());
        return bm;
    }
}



