package com.demo.sticker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseAppIndexingInvalidArgumentException;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Indexables;
import com.google.firebase.appindexing.builders.StickerBuilder;
import com.google.firebase.appindexing.builders.StickerPackBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StickerIndexingUtil {
    private static final String STICKER_URL_PATTERN = "mystickers://sticker/%s";
    private static final String STICKER_PACK_URL_PATTERN = "mystickers://sticker/pack/%s";
    private static final String STICKER_PACK_NAME = "Local Content Pack";
    private static final String TAG = "AppIndexingUtil";
    public static final String FAILED_TO_INSTALL_STICKERS = "Failed to install stickers";

    //this just adds the stickers
    public static void setStickers(final Context context, FirebaseAppIndex firebaseAppIndex) {
        try {
            List<Indexable> stickers = getIndexableStickers(context);
            Indexable stickerPack = getIndexableStickerPack(context);

            List<Indexable> indexables = new ArrayList<>(stickers);
            indexables.add(stickerPack);

            Task<Void> task = firebaseAppIndex.update(
                    indexables.toArray(new Indexable[indexables.size()]));

            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, "Successfully added stickers", Toast.LENGTH_SHORT)
                            .show();
                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, FAILED_TO_INSTALL_STICKERS, e);
                    Toast.makeText(context, FAILED_TO_INSTALL_STICKERS, Toast.LENGTH_SHORT)
                            .show();
                }
            });
        } catch (IOException | FirebaseAppIndexingInvalidArgumentException e) {
            Log.e(TAG, "Unable to set stickers", e);
        }
    }

    private static List<Indexable> getIndexableStickers(Context context) throws IOException {
        List<Indexable> indexableStickers = new ArrayList<>();
        List<StickerBuilder> stickerBuilders = getStickerBuilders(context);

        for (StickerBuilder stickerBuilder : stickerBuilders) {
            stickerBuilder
                    .setIsPartOf(Indexables.stickerPackBuilder()
                            .setName(STICKER_PACK_NAME));
            indexableStickers.add(stickerBuilder.build());
        }

        return indexableStickers;
    }

    // this builds the INDIVIDUAL stickers
    private static List<StickerBuilder> getStickerBuilders(Context context) throws IOException {
        List<StickerBuilder> stickerBuilders = new ArrayList<>();

        File stickersDir = new File(context.getFilesDir(), "stickers");

        if (!stickersDir.exists() && !stickersDir.mkdirs()) {
            throw new IOException("Stickers directory does not exist");
        }

        for (int i = 0; i < HomeActivity.stickers.length; i++) {
            String stickerFilename = getStickerFilename(i);
            String[] keywords = HomeActivity.stickers[i].getKeywords();

            StickerBuilder stickerBuilder = Indexables.stickerBuilder()
                    .setName(stickerFilename)
                    // Firebase App Indexing unique key that must match an intent-filter
                    // see: Support links to your app content section
                    // (e.g. mystickers://sticker/0)
                    .setUrl(String.format(STICKER_URL_PATTERN, i))
                    // http url or content uri that resolves to the sticker
                    // (e.g. http://www.google.com/sticker.png or content://some/path/0)
                    .setImage(HomeActivity.stickers[i].getImageUrl())
                    .setDescription("content description")
                    .setIsPartOf(Indexables.stickerPackBuilder()
                            .setName(STICKER_PACK_NAME))
                    .setKeywords(keywords);
            stickerBuilders.add(stickerBuilder);
        }

        return stickerBuilders;
    }

    //this makes the sticker PACK
    private static Indexable getIndexableStickerPack(Context context)
            throws IOException, FirebaseAppIndexingInvalidArgumentException {
        List<StickerBuilder> stickerBuilders = getStickerBuilders(context);
        File stickersDir = new File(context.getFilesDir(), "stickers");

        if (!stickersDir.exists() && !stickersDir.mkdirs()) {
            throw new IOException("Stickers directory does not exist");
        }

        // Use the last sticker for category image for the sticker pack.
        final int lastIndex = stickerBuilders.size() - 1;

        // user sticker method here
        final String imageUrl = HomeActivity.stickers[lastIndex].getImageUrl();

        StickerPackBuilder stickerPackBuilder = Indexables.stickerPackBuilder()
                .setName(STICKER_PACK_NAME)
                // Firebase App Indexing unique key that must match an intent-filter.
                // (e.g. mystickers://sticker/pack/0)
                .setUrl(String.format(STICKER_PACK_URL_PATTERN, lastIndex))
                // Defaults to the first sticker in "hasSticker". Used to select between sticker
                // packs so should be representative of the sticker pack.
                .setImage(imageUrl)
                .setHasSticker(stickerBuilders.toArray(new StickerBuilder[stickerBuilders.size()]))
                .setDescription("content description");
        return stickerPackBuilder.build();
    }

    private static String getStickerFilename(int index) {
        return HomeActivity.stickers[index].getName();
    }

}
