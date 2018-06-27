package com.demo.sticker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.content.Intent;
import android.net.Uri;
import java.io.File;
import android.graphics.Bitmap;
import java.io.FileOutputStream;
import android.support.v4.content.FileProvider;
import java.io.IOException;
import android.graphics.BitmapFactory;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.demo.sticker.BuildConfig;

public class HomeActivity extends AppCompatActivity {

    public static Integer currentSticker = 0;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("firstTime", false)) {
            // run your one time code
            AppIndexingService.enqueueWork(HomeActivity.this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

        GridView gridView = (GridView)findViewById(R.id.gridview);
        final StickersAdapter stickersAdapter = new StickersAdapter(this, stickers);
        gridView.setAdapter(stickersAdapter);

        image = (ImageView)findViewById(R.id.mainImageView);

        ImageButton ShareStickerBtn = findViewById(R.id.shareSticker);
        ShareStickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap icon = BitmapFactory.decodeResource(HomeActivity.this.getResources(),stickers[currentSticker].getImageResource());
                shareImage(icon);

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Sticker sticker = stickers[position];

                image.setImageResource(sticker.getImageResource());
                currentSticker = position;

                // This tells the GridView to redraw itself
                // in turn calling StickerAdapter's getView method again for each cell
                stickersAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // set back the currently selected sticker we're viewing
        image.setImageResource(stickers[currentSticker].getImageResource());

    }

    private void shareImage(Bitmap bitmap){
        // save bitmap to cache directory
        try {
            File cachePath = new File(this.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        File imagePath = new File(this.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.setType("image/png");
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }
    }

    public static Sticker[] stickers = {
        new Sticker("logo black", R.drawable.pg_logo_black,
                "https://firebasestorage.googleapis.com/v0/b/gboardcustomsticker-fea6d.appspot.com/o/Pg-Logo-Black.png?alt=media&token=e07c50df-697a-40d7-a6a1-a8f5f53f7bba", new String [] {"playground","logo","black"}),
        new Sticker("logo blue", R.drawable.pg_logo_blue,
                    "https://firebasestorage.googleapis.com/v0/b/gboardcustomsticker-fea6d.appspot.com/o/Pg-Logo-Blue.png?alt=media&token=c1400515-36e9-4f2e-9e78-5171bdfa5339", new String [] {"playground","logo","blue"}),
        new Sticker("logo gold", R.drawable.pg_logo_gold,
                    "https://firebasestorage.googleapis.com/v0/b/gboardcustomsticker-fea6d.appspot.com/o/Pg-Logo-Gold.png?alt=media&token=0154c57c-ec2f-4f32-b706-f81fd68788d4", new String [] {"playground","logo","gold"}),
        new Sticker("logo green", R.drawable.pg_logo_green,
                    "https://firebasestorage.googleapis.com/v0/b/gboardcustomsticker-fea6d.appspot.com/o/Pg-Logo-Green.png?alt=media&token=2d5b1d18-230f-4401-a27d-1e035ced180a", new String [] {"playground","logo","green"}),
        new Sticker("logo light blue", R.drawable.pg_logo_lightblue,
                    "https://firebasestorage.googleapis.com/v0/b/gboardcustomsticker-fea6d.appspot.com/o/Pg-Logo-LightBlue.png?alt=media&token=0fa1713e-f589-4b1b-8ec7-5b81de8e1c3d", new String [] {"playground","logo","light blue"}),
        new Sticker("logo red", R.drawable.pg_logo_red,
                    "https://firebasestorage.googleapis.com/v0/b/gboardcustomsticker-fea6d.appspot.com/o/Pg-Logo-Red.png?alt=media&token=be40cb08-09e3-4f4b-808c-e81a0693fcac", new String [] {"playground","logo","red"})
    };
}