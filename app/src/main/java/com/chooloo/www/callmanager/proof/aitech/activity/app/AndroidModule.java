package com.chooloo.www.callmanager.proof.aitech.activity.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.chooloo.www.callmanager.proof.aitech.util.Constants;
import com.chooloo.www.callmanager.proof.aitech.util.Utilities;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class AndroidModule {
    private final Context context;

    public AndroidModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    @Named(Constants.APPLICATION_CONTEXT)
    public Context provideContext() {
        return context;
    }

    @Singleton
    @Provides
    public Utilities provideUtils(){return new Utilities();}

    @Singleton
    @Provides
    public SharedPreferences provideSharedPreferences(@Named(Constants.APPLICATION_CONTEXT) Context context) {
        return context.getSharedPreferences("mealthyPref", Context.MODE_PRIVATE);
    }

    @Singleton
    @Provides
    public Resources provideResources(@Named(Constants.APPLICATION_CONTEXT) Context context) {
        return context.getResources();
    }
}
