package com.chooloo.www.callmanager;

import android.app.Application;
import android.content.Context;

import com.chooloo.www.callmanager.proof.aitech.BaseActivity;
import com.chooloo.www.callmanager.proof.aitech.activity.app.AndroidModule;
import com.chooloo.www.callmanager.proof.aitech.activity.app.ApplicationComponent;
import com.chooloo.www.callmanager.proof.aitech.activity.app.ApplicationModule;
import com.chooloo.www.callmanager.proof.aitech.activity.app.DaggerApplicationComponent;
import com.chooloo.www.callmanager.proof.aitech.activity.components.ActivityComponent;
import com.chooloo.www.callmanager.proof.aitech.activity.components.ActivityModule;

public class BackupApplication extends Application {
    public static BackupApplication instance;
    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .androidModule(new AndroidModule(this))
                .build();
    }

    public static synchronized BackupApplication getInstance() {
        return instance;
    }

    public static Context getContext(){
        return BackupApplication.getInstance().getApplicationContext();
    }

    public ActivityComponent getActivityComponent(BaseActivity baseActivity) {
        return component.activityComponentBuilder().activityModule(new ActivityModule(baseActivity)).build();
    }

}
