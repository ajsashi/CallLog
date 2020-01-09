package com.chooloo.www.callmanager.proof.aitech.fragment.callproof;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.chooloo.www.callmanager.proof.aitech.BaseFragment;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.proof.aitech.fragmentmanager.manager.IFragment;

public class CallSignUpFragment extends BaseFragment implements IFragment {
    private static final String FRAGMENT_NAME = "CallSignUp";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injector().inject(this);
        Bundle data = savedInstanceState != null ? savedInstanceState : getArguments();
        if (data != null) {

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_callproof_signup;
    }

    @Override
    public String getFragmentName() {
        return FRAGMENT_NAME;
    }

    @Override
    public void setTitle() {

    }

    @Override
    public void dispose() {

    }

    public static CallSignUpFragment newInstance() {
        return new CallSignUpFragment();
    }
}
