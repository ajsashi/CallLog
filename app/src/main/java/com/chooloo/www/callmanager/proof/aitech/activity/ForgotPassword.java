package com.chooloo.www.callmanager.proof.aitech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chooloo.www.callmanager.proof.aitech.BaseActivity;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.proof.aitech.listener.ForgotListener;
import com.chooloo.www.callmanager.proof.aitech.network.retrofit.RetrofitCall;
import com.chooloo.www.callmanager.proof.aitech.util.Utilities;

import java.util.HashMap;

import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_NETWORK_CONNECTED;

public class ForgotPassword extends BaseActivity implements ForgotListener {
    private EditText mEmail;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forgot_password;
    }

    @Override
    protected int getFrameLayoutContainerId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEmail = findViewById(R.id.edt_email);
    }

    public void onSubmitClick(View view) {
        String email = mEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Email is required");
        } else if (!isValidEmail(email)) {
            Toast.makeText(this, "Email is inValid", Toast.LENGTH_SHORT).show();
        } else if (IS_NETWORK_CONNECTED) {
            Utilities.showCirclarAlert(this);
            RetrofitCall retrofitCall = new RetrofitCall();
            HashMap<String, String> map = new HashMap<>();
            map.put("email", email);
            retrofitCall.getForgotUser(map, getApplicationContext(), this);
        }
    }

    public boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    @Override
    public void onSuccess(String msg) {
        Utilities.stopCirclarAlert();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onFailure(String error) {
        Utilities.stopCirclarAlert();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }
}
