package com.chooloo.www.callmanager.proof.aitech;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chooloo.www.callmanager.R;
import com.google.android.material.snackbar.Snackbar;
import com.chooloo.www.callmanager.proof.aitech.activity.components.ActivityComponent;
import com.chooloo.www.callmanager.BackupApplication;
import com.chooloo.www.callmanager.proof.aitech.fragmentmanager.manager.SFMActivity;

import butterknife.ButterKnife;

import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_NETWORK_CONNECTED;


public abstract class BaseActivity extends SFMActivity {
    private BroadcastReceiver mReceiver;
    private Snackbar snackbar;
    private IntentFilter filter;//broadcast

    protected abstract int getLayoutId();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        ButterKnife.bind(this);

        /*=========================================================================================================================*/
        /*=======================================Manually checking internet connection=============================================*/
        filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //do something based on the intent's action
                if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                    if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                        IS_NETWORK_CONNECTED = true;
                        showSnack(true);
                    } else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED) {
                        IS_NETWORK_CONNECTED = false;
                        showSnack(false);
                    }
                }
            }
        };
        registerReceiver(mReceiver, filter);
        /*=========================================================================================================================*/
        /*=========================================================================================================================*/
    }

    public ActivityComponent injector() {
        return ((BackupApplication) getApplicationContext()).getActivityComponent(this);
    }

    // Showing the status in Snackbar
    public void showSnack(boolean isConnected) {
        String message;
        int color = Color.WHITE;
        if (isConnected) {
            if (snackbar != null && snackbar.isShown()) {
                snackbar.dismiss();
            }
        } else {
            message = "Sorry! Not connected to internet";
            snackbar = Snackbar
                    .make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        }
        if (snackbar != null) {
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(color);
            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.red_phone));
            snackbar.show();
        }
    }


    public void onStart() {
        super.onStart();
        registerReceiver(mReceiver, filter);
    }

    public void onStop() {
        super.onStop();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

}
