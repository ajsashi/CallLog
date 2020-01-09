package com.chooloo.www.callmanager.proof.aitech;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chooloo.www.callmanager.proof.aitech.activity.components.ActivityComponent;
import com.chooloo.www.callmanager.proof.aitech.fragmentmanager.CallproofChannel;
import com.chooloo.www.callmanager.proof.aitech.fragmentmanager.FragmentChannel;
import com.chooloo.www.callmanager.proof.aitech.fragmentmanager.manager.SFMFragment;

import butterknife.ButterKnife;


public abstract class BaseFragment extends SFMFragment<FragmentChannel> {

    protected CallproofChannel callproofChannel;
    protected FragmentInteractionCallback fragmentInteractionCallback;
    protected static String currentTab;

    protected abstract int getLayoutId();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentChannel) {
            callproofChannel = ((CallproofChannel) context);
        }
        try {
            fragmentInteractionCallback = (FragmentInteractionCallback) context;
        } catch (ClassCastException e) {
            throw new RuntimeException(context.toString() + " must implement " + FragmentInteractionCallback.class.getName());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (callproofChannel == null && getParentFragment() != null && getParentFragment() instanceof FragmentChannel) {
            callproofChannel = (CallproofChannel) getParentFragment();
        }

        if (callproofChannel == null) {
            Log.d("Channel","Parent does not implement FragmentChannel.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDetach() {
        fragmentInteractionCallback = null;
        super.onDetach();
    }

    public ActivityComponent injector() {
        return ((BaseActivity) getContext()).injector();
    }

    public interface FragmentInteractionCallback {

        void onFragmentInteractionCallback(Bundle bundle);
    }

    public static void setCurrentTab(String currentTab) {
        BaseFragment.currentTab = currentTab;
    }
}
