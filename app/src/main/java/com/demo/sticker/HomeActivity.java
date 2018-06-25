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
        new Sticker("sunglasses", R.drawable.faecface_01_2x,
                "https://faecface.club/assets/stickers/faecface-01-sunglasses.png", new String [] {"sunglasses","glasses","cool"}),
        new Sticker("sad", R.drawable.faecface_02_2x,
                "https://faecface.club/assets/stickers/faecface-02-sad.png", new String [] {"sad","sorry"}),
        new Sticker("laughing", R.drawable.faecface_03_2x,
                "https://faecface.club/assets/stickers/faecface-03-laughing.png", new String [] {"laughing", "omg", "lol"}),
        new Sticker("smile", R.drawable.faecface_04_2x,
                "https://faecface.club/assets/stickers/faecface-04-smile.png", new String [] {"smile", "yay"}),
        new Sticker("kiss", R.drawable.faecface_05_2x,
                "https://faecface.club/assets/stickers/faecface-05-kiss.png", new String [] {"kiss", "wink", "love"}),
        new Sticker("upside-down", R.drawable.faecface_06_2x,
                "https://faecface.club/assets/stickers/faecface-06-upside-down.png", new String [] {"confused", "upside down"}),
        new Sticker("birthday-hat", R.drawable.faecface_07_2x,
                "https://faecface.club/assets/stickers/faecface-07-birthday-hat-happy.png", new String [] {"birthday", "party", "hat", "happy"}),
        new Sticker("heart-eyes", R.drawable.faecface_08_2x,
                "https://faecface.club/assets/stickers/faecface-08-heart-eyes.png", new String [] {"love", "heart eyes", "hearts"}),
        new Sticker("sick", R.drawable.faecface_09_2x,
                "https://faecface.club/assets/stickers/faecface-09-sick.png", new String [] {"sick","gross", "green"}),
        new Sticker("glasses", R.drawable.faecface_10_2x,
                "https://faecface.club/assets/stickers/faecface-10-glasses.png", new String [] {"glasses", "nerd", "smart"}),
        new Sticker("blush", R.drawable.faecface_11_2x,
                "https://faecface.club/assets/stickers/faecface-11-blush.png", new String [] {"blush", "happy"}),
        new Sticker("surprised", R.drawable.faecface_12_2x,
                "https://faecface.club/assets/stickers/faecface-12-surprised.png", new String [] {"surprised", "wow", "omg"}),
        new Sticker("pout", R.drawable.faecface_13_2x,
                "https://faecface.club/assets/stickers/faecface-13-pout.png", new String [] {"pout", "kiss"}),
        new Sticker("crying", R.drawable.faecface_14_2x,
                "https://faecface.club/assets/stickers/faecface-14-crying.png", new String [] {"sad", "crying", "tears"}),
        new Sticker("thinking", R.drawable.faecface_15_2x,
                "https://faecface.club/assets/stickers/faecface-15-thinking.png", new String [] {"thinking"}),
        new Sticker("thinking-happy", R.drawable.faecface_16_2x,
                "https://faecface.club/assets/stickers/faecface-16-thinking-happy.png", new String [] {"thinking", "happy"}),
        new Sticker("smirk", R.drawable.faecface_17_2x,
                "https://faecface.club/assets/stickers/faecface-17-smirk.png", new String [] {"smirk"}),
        new Sticker("grinning", R.drawable.faecface_18_2x,
                "https://faecface.club/assets/stickers/faecface-18-grinning.png", new String [] {"grinning", "teeth"}),
        new Sticker("angel", R.drawable.faecface_19_2x,
                "https://faecface.club/assets/stickers/faecface-19-angel.png", new String [] {"angel", "halo"}),
        new Sticker("birthday-hat-sad", R.drawable.faecface_20_2x,
                "https://faecface.club/assets/stickers/faecface-20-birthday-hat-sad.png", new String [] {"birthday", "party", "sad", "hat"}),
        new Sticker("sweating", R.drawable.faecface_21_2x,
                "https://faecface.club/assets/stickers/faecface-21-sweating.png", new String [] {"sweating", "nervous"}),
        new Sticker("cry-joy", R.drawable.faecface_22_2x,
                "https://faecface.club/assets/stickers/faecface-22-cry-joy.png", new String [] {"joy", "cry"}),
        new Sticker("curious", R.drawable.faecface_23_2x,
                "https://faecface.club/assets/stickers/faecface-23-curious.png", new String [] {"curious", "oh really", "raised brow"}),
        new Sticker("sleeping", R.drawable.faecface_24_2x,
                "https://faecface.club/assets/stickers/faecface-24-sleeping.png", new String [] {"sleeping", "sleepy", "tired", "goodnight"})
    };
}