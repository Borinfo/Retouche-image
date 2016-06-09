package com.rabahdiallo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity implements View.OnClickListener {
    Button ouvrir,pPhoto,editer,filtre,sauvegarder,quitter;
    Bitmap.Config conf;
    ImageView ivImage;

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conf = Bitmap.Config.ARGB_8888;
        ouvrir=(Button) findViewById(R.id.ouvrir);
        pPhoto=(Button) findViewById(R.id.prendre);
        editer=(Button) findViewById(R.id.editer);
        filtre=(Button) findViewById(R.id.filtres);
        sauvegarder=(Button) findViewById(R.id.sauvegarder);
        quitter=(Button) findViewById(R.id.quitter);
        ivImage = (ImageView) findViewById(R.id.image);
        ouvrir.setOnClickListener(this);
        pPhoto.setOnClickListener(this);
        editer.setOnClickListener(this);
        filtre.setOnClickListener(this);
        sauvegarder.setOnClickListener(this);
        quitter.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent i;
        Bitmap image;
        Bundle extras;
        switch (v.getId()){
            case R.id.ouvrir :
                ouvrirImage();
                break;
            case R.id.prendre:
                prendrePhoto();
                break;
            case R.id.editer:
                i = new Intent(this, Editer.class);
                ivImage.buildDrawingCache();
                 image= ivImage.getDrawingCache();

                 extras = new Bundle();
                extras.putParcelable("imagebitmap", image);
                i.putExtras(extras);
                startActivity(i);
                break;
//            case R.id.filtres:
//                i = new Intent(this, );
//                ivImage.buildDrawingCache();
//                image= ivImage.getDrawingCache();

//                 extras = new Bundle();
//                extras.putParcelable("imagebitmap", image);
//                i.putExtras(extras);
//                startActivity(i);
//                break;
            case R.id.sauvegarder:
                break;
            case R.id.quitter:
                finish();
                System.exit(0);
                break;
        }
    }
    void ouvrirImage(){
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, "Choix d'image"), SELECT_FILE);
    }
    void prendrePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        ivImage.setImageBitmap(bm);
    }

}
