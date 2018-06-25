package com.demo.sticker;

import android.support.v4.app.JobIntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.firebase.appindexing.FirebaseAppIndex;

public class AppIndexingService extends JobIntentService {
    // Job-ID must be unique across your whole app.
    private static final int UNIQUE_JOB_ID = 42;

    public static void enqueueWork(Context context) {
        enqueueWork(context, AppIndexingService.class, UNIQUE_JOB_ID, new Intent());
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        StickerIndexingUtil.setStickers(getApplicationContext(), FirebaseAppIndex.getInstance());
    }
}
