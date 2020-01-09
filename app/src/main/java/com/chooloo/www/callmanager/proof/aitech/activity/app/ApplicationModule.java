package com.chooloo.www.callmanager.proof.aitech.activity.app;



import com.chooloo.www.callmanager.proof.aitech.activity.components.ActivityComponent;
import com.chooloo.www.callmanager.BackupApplication;

import javax.inject.Singleton;

import dagger.Module;

@Singleton
@Module(subcomponents = {ActivityComponent.class})
public class ApplicationModule {

    private final BackupApplication application;

    public ApplicationModule(BackupApplication application) {
        this.application = application;
    }
}
