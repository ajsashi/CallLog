package com.chooloo.www.callmanager.proof.aitech.activity.components;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.chooloo.www.callmanager.proof.aitech.BaseActivity;
import com.chooloo.www.callmanager.proof.aitech.fragmentmanager.manager.FragmentManagerHandler;
import com.chooloo.www.callmanager.proof.aitech.presenter.PresenterModule;
import com.chooloo.www.callmanager.proof.aitech.util.Constants;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;


@ActivityScope
@Module(includes = {PresenterModule.class})
public class ActivityModule {

    private final BaseActivity baseActivity;

    public ActivityModule(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    @Provides
    @Named(Constants.ACTIVITY_CONTEXT)
    public Context provideActivityContext() {
        return baseActivity;
    }

    @Provides
    public FragmentManager provideFragmentManager() {
        return baseActivity.getSupportFragmentManager();
    }

    @Provides
    public FragmentManagerHandler provideGemFragmentManager(FragmentManager fragmentManager) {
        return new FragmentManagerHandler(fragmentManager);
    }
}
