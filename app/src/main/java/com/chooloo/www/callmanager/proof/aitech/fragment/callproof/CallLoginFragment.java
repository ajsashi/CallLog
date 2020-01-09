package com.chooloo.www.callmanager.proof.aitech.fragment.callproof;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.chooloo.www.callmanager.proof.aitech.BaseFragment;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.proof.aitech.fragmentmanager.manager.IFragment;
import com.chooloo.www.callmanager.proof.aitech.listener.CallLoginListener;
import com.chooloo.www.callmanager.proof.aitech.network.retrofit.RetrofitCall;
import com.chooloo.www.callmanager.proof.aitech.util.Constants;
import com.chooloo.www.callmanager.proof.aitech.util.Utilities;
import com.chooloo.www.callmanager.proof.aitech.util.preference.PreferenceDataManager;

import butterknife.BindView;
import butterknife.OnClick;

import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_CALLPROOF_CONNECTED;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_CALLPROOF_SUCCESS;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_NETWORK_CONNECTED;

public class CallLoginFragment extends BaseFragment implements IFragment, CallLoginListener {
    private static final String FRAGMENT_NAME = "CallLogin";
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.edt_call_email)
    EditText mEditCallEmail;
    @BindView(R.id.edt_call_password)
    EditText mEditCallPassword;
    @BindView(R.id.btn_call_login)
    Button mBtnCallLogin;
    private CheckBox mCallProofRemember;
    private String email, password;
    private boolean isRemember;
    private PreferenceDataManager manager;


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

        manager = new PreferenceDataManager(getContext());
        mCallProofRemember = view.findViewById(R.id.checkbox_remember_call_proof);
        mCallProofRemember.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        isRemember = true;
                    } else {
                        isRemember = false;
                    }
                }
        );
        setFromPrefData();
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_callproof_login;
    }

    @Override
    public String getFragmentName() {
        return FRAGMENT_NAME;
    }

    @Override
    public void setTitle() {

    }

    @OnClick(R.id.btn_call_login)
    void onLoginClick() {
        if (validateFields()) {
            if (IS_NETWORK_CONNECTED) {
                Utilities.showCirclarAlert(getActivity());
                String URL_CALLPROOF = "https://stage.callproof.com/m/login2/";
                String phoneNumber = Utilities.getPhoneNumber(getActivity());
                String timeZone = Utilities.timeZone();
                RetrofitCall call = new RetrofitCall();
                call.getCallProofCred(mEditCallEmail.getText().toString(), mEditCallPassword.getText().toString(), timeZone, Constants.FIREBASE_TOKEN,
                        phoneNumber, getContext(), URL_CALLPROOF, this);
            }
        }
    }

    @Override
    public void dispose() {

    }

    public static CallLoginFragment newInstance() {
        return new CallLoginFragment();
    }

    @Override
    public void onSuccess(String success) {
        Utilities.showCommomAlert("Success", success, getContext());
        Utilities.stopCirclarAlert();
        //save to pref
        manager.storeUserData("isRememberCallProof", String.valueOf(isRemember));
        manager.storeUserData("emailCallProof",email);
        manager.storeUserData("passCallProof",password);

        IS_CALLPROOF_SUCCESS = true;
        IS_CALLPROOF_CONNECTED = true;
        callproofChannel.popUp();
        callproofChannel.showSync();
    }

    public boolean validateFields() {
        email = mEditCallEmail.getText().toString().trim();
        password = mEditCallPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            mEditCallEmail.setError("Email is required");
            return false;
        } else if (!isValidEmail(email)) {
            mEditCallEmail.setError("Email is not valid");
            return false;
        } else if (TextUtils.isEmpty(password)) {
            mEditCallPassword.setError("Password is required");
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onError(String error) {
        Utilities.showCommomAlert("Oops", error, getContext());
        Utilities.stopCirclarAlert();
    }


    public void setFromPrefData() {
        //loading data from pref
        isRemember = Boolean.valueOf(manager.getUserData("isRememberCallProof"));
        if (isRemember) {
            mCallProofRemember.setChecked(true);
            mEditCallEmail.setText(manager.getUserData("emailCallProof"));
            mEditCallPassword.setText(manager.getUserData("passCallProof"));
        }
    }
}
