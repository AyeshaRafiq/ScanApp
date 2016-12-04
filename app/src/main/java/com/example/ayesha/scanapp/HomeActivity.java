package com.example.ayesha.scanapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;
import com.googlecode.tesseract.android.TessBaseAPI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.view.View;
import android.net.Uri;

public class HomeActivity extends AppCompatActivity {

    ImageView view1;
    Bitmap image;
    private TessBaseAPI mTess;
    String datapath = "";

    BitmapFactory.Options options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        view1=(ImageView) findViewById(R.id.imageView2);
        // setContentView(R.layout.scrollable_OCRTextView);


        TextView textView = (TextView) findViewById(R.id.OCRTextView);
        textView.setMovementMethod(new ScrollingMovementMethod());
        //init image


        /*BitmapDrawable drawable = (BitmapDrawable) view1.getDrawable();
        image = drawable.getBitmap();
        //image = BitmapFactory.decodeResource(getResources() , );

        //initialize Tesseract API
        String language = "eng";
        datapath = getFilesDir()+ "/tesseract/";
        mTess = new TessBaseAPI();

        checkFile(new File(datapath + "tessdata/"));

        mTess.init(datapath, language);*/
    }

    public void processImage(View view){
        String OCRresult = null;
        mTess.setImage(image);
        OCRresult = mTess.getUTF8Text();
        TextView OCRTextView = (TextView) findViewById(R.id.OCRTextView);
        OCRTextView.setText(OCRresult);
    }

    private void checkFile(File dir) {
        if (!dir.exists()&& dir.mkdirs()){
            copyFiles();
        }
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/eng.traineddata";
            File datafile = new File(datafilepath);

            if (!datafile.exists()) {
                copyFiles();
            }
        }
    }

    private void copyFiles() {
        try {
            String filepath = datapath + "/tessdata/eng.traineddata";
            AssetManager assetManager = getAssets();

            InputStream instream = assetManager.open("tessdata/eng.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }


            outstream.flush();
            outstream.close();
            instream.close();

            File file = new File(filepath);
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void btncmr(View view)
    {
        if(ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CAMERA}, 21);
        }
        else
        {
            OPenCamer();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case 21:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    OPenCamer();
                }
                else
                {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }

                return;
            }
            case 121:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    OPenGall();
                }
                else
                {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }

                return;
            }
        }
    }

    public void OPenCamer()
    {
        Intent cameraIntent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,100);
    }

    public void btnglr(View view) {
       /* Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
        mediaChooser.setType("image*//*");
        startActivityForResult(mediaChooser, 1);
        */

        /*Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent , "SELECT PIC" ), 1);*/
        /*
        Intent galleryItent = new Intent(Intent.ACTION_VIEW , android.provider.MediaStore.EXTERNAL_CONTENT_URI);*/

        /*Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse("content://media/internal/images/media"));
        startActivityForResult(intent, 200);*/

        if(ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 121);
        }
        else
        {
            OPenGall();
        }

    }

    public void OPenGall()
    {
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK && null != data)
        {

            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            view1.setImageBitmap(mphoto);
            image = mphoto;
            //initialize Tesseract API
            String language = "eng";
            datapath = getFilesDir()+ "/tesseract/";
            mTess = new TessBaseAPI();

            checkFile(new File(datapath + "tessdata/"));

            mTess.init(datapath, language);
            System.out.println("IFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF ");

        }
        else if(requestCode == 200 && resultCode == RESULT_OK)
        {
            /*Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            view1.setImageBitmap(mphoto);*/
            System.out.println("else IFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");




            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            view1.setImageBitmap(BitmapFactory.decodeFile(picturePath ,  options));

            //BitmapDrawable image = (BitmapDrawable) view1.getDrawable();

            //int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());

            image = BitmapFactory.decodeFile(picturePath );
            //image = BitmapFactory.decodeResource(getResources() , R.drawable.test_image );

            //initialize Tesseract API
            String language = "eng";
            datapath = getFilesDir()+ "/tesseract/";
            mTess = new TessBaseAPI();

            checkFile(new File(datapath + "tessdata/"));

            mTess.init(datapath, language);
        }
        else
        {
            System.out.println("IFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF else");
        }
    }
}

