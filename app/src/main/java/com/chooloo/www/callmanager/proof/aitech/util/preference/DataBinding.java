package com.chooloo.www.callmanager.proof.aitech.util.preference;

import android.content.Context;


public class DataBinding {
    PreferenceDataManager preferenceDataManager;

    public void setLoginData(Context context, String email, String password, boolean isRemember, String token) {
        preferenceDataManager = new PreferenceDataManager(context);
        preferenceDataManager.storeUserData("email", email);
        preferenceDataManager.storeUserData("password", password);
        preferenceDataManager.storeUserData("isRemember", Boolean.toString(isRemember));
        preferenceDataManager.storeUserData("token", token);
    }
}
