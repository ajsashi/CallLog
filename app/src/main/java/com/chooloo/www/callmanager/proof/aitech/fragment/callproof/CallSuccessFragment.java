package com.chooloo.www.callmanager.proof.aitech.fragment.callproof;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chooloo.www.callmanager.proof.aitech.BaseFragment;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.proof.aitech.fragmentmanager.manager.IFragment;

import butterknife.BindView;
import butterknife.OnClick;

import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_CALLPROOF_CONNECTED;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_CALLPROOF_SUCCESS;

public class CallSuccessFragment extends BaseFragment implements IFragment {
    private static final String FRAGMENT_NAME = "callSuccess";
    @BindView(R.id.img_logout)
    ImageView mLogout;

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

    @OnClick(R.id.img_logout)
    void onLogoutClick(){
        IS_CALLPROOF_SUCCESS = false;
        IS_CALLPROOF_CONNECTED = false;
        callproofChannel.popUp();
        callproofChannel.showCallProof();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_callproof_success;
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

    public static CallSuccessFragment newInstance() {
        return new CallSuccessFragment();
    }
}
