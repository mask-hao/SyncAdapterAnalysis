package com.example.zhanghao.syncdatasample;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncAdapterType;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private static final String AUTHORITY = "com.example.zhanghao.syncdatasample.provider";
    private static final String ACCOUNT_TYPE = "com.example.zhanghao";
    private static final String ACCOUNT = "MASK_HAO";
    private Account mAccount;
    private static final String TAG = "MainActivity";

    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 60L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
                    SECONDS_PER_MINUTE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAccount = createAccountIfNeeded(this);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performManualSync();
//                performPeriodSync();
            }
        });
        Switch s = findViewById(R.id.find_sw);
        s.setChecked(ContentResolver.getSyncAutomatically(mAccount, AUTHORITY));
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ContentResolver.setSyncAutomatically(mAccount,AUTHORITY,isChecked);
                if (isChecked){
                    performManualSync();
                }
                if (!isChecked){
                    ContentResolver.cancelSync(mAccount,AUTHORITY);
                }
            }
        });
    }

    private void findSyncAdapter() {
        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
        SyncAdapterType[] syncAdapterType = ContentResolver.getSyncAdapterTypes();
        for (SyncAdapterType adapterType : syncAdapterType) {
            if (adapterType.accountType.equals(mAccount.type)) {
                Log.d(TAG, "findSyncAdapter: " + adapterType.authority);
            }
        }
    }

    private Account createAccountIfNeeded(Context context) {
        AccountManager manager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);

        if (manager != null) {
            Account[] accounts = manager.getAccounts();
            for (Account account : accounts) {
                if (account.type.equals(ACCOUNT_TYPE)) {
                    return account;
                }
            }

            Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
            if (manager.addAccountExplicitly(newAccount, null, null)) {
                return newAccount;
            }

        }

        throw new RuntimeException("create account error");
    }

    private void performManualSync() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(mAccount, AUTHORITY, bundle);
    }

    private void performPeriodSync() {
        ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL);
    }

}
