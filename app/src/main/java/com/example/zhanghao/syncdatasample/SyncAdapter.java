package com.example.zhanghao.syncdatasample;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by zhanghao on 18-7-27.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "SyncAdapter";

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    @WorkerThread
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.d(TAG, "onPerformSync: --start--" + bundle.getBoolean(ContentResolver.SYNC_EXTRAS_MANUAL));
        SystemClock.sleep(20 * 1000);
        Log.d(TAG, "onPerformSync: " + Thread.currentThread());
    }
}
