package com.chooloo.www.callmanager.proof.aitech.activity.components;



import com.chooloo.www.callmanager.proof.aitech.MainActivity1;
import com.chooloo.www.callmanager.proof.aitech.fragment.allcalls.AllCallsFragment;
import com.chooloo.www.callmanager.proof.aitech.fragment.callproof.CallForgotFragment;
import com.chooloo.www.callmanager.proof.aitech.fragment.callproof.CallLoginFragment;
import com.chooloo.www.callmanager.proof.aitech.fragment.callproof.CallSignUpFragment;
import com.chooloo.www.callmanager.proof.aitech.fragment.callproof.CallSuccessFragment;
import com.chooloo.www.callmanager.proof.aitech.fragment.sync.SyncFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {

    void inject(MainActivity1 mainActivity);

    void inject(AllCallsFragment allCallsFragment);

    void inject(CallForgotFragment callForgotFragment);

    void inject(CallLoginFragment callLoginFragment);

    void inject(CallSignUpFragment callSignUpFragment);

    void inject(SyncFragment syncFragment);

    void inject(CallSuccessFragment callSuccessFragment);

    @Subcomponent.Builder
    interface Builder {
        Builder activityModule(ActivityModule activityModule);

        ActivityComponent build();
    }
}
