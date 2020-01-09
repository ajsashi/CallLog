package com.chooloo.www.callmanager.proof.aitech.activity.app;


import com.chooloo.www.callmanager.proof.aitech.activity.components.ActivityComponent;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {ApplicationModule.class, AndroidModule.class})
public interface ApplicationComponent {
    ActivityComponent.Builder activityComponentBuilder();
}