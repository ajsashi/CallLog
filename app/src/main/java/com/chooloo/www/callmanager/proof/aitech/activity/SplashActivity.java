package com.chooloo.www.callmanager.proof.aitech.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.chooloo.www.callmanager.proof.aitech.util.Constants;
import com.chooloo.www.callmanager.proof.aitech.util.preference.PreferenceDataManager;

import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_LOG_SYNCED;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_USER_LOGGED_IN;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.LAST_CALL_BACKUP_TIME;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.USER_LOGGED;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.USER_NAME;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_splash);*/

        PreferenceDataManager dataManager = new PreferenceDataManager(getApplicationContext());
        Constants.IS_USER_LOGGED_IN = Boolean.parseBoolean(dataManager.getUserData(USER_LOGGED));
        IS_LOG_SYNCED =  Boolean.parseBoolean(dataManager.getUserData("logSync"));
        USER_NAME = dataManager.getUserData("userName");
        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                    if (IS_USER_LOGGED_IN) {
                        Constants.USER_AUTH_TOKEN = dataManager.getUserData("userToken");
                        LAST_CALL_BACKUP_TIME = dataManager.getUserData("lastBackUpTime");
                        startActivity(new Intent(SplashActivity.this, CallLogActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();
    }
}
