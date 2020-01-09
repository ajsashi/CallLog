package com.chooloo.www.callmanager.proof.aitech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chooloo.www.callmanager.proof.aitech.BaseActivity;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.proof.aitech.listener.SignUpListener;
import com.chooloo.www.callmanager.proof.aitech.network.retrofit.RetrofitCall;
import com.chooloo.www.callmanager.proof.aitech.util.Utilities;

import java.util.HashMap;

import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_NETWORK_CONNECTED;

public class
SignUpActivity extends BaseActivity implements SignUpListener {

    private EditText mEditName, mEditEmail, mEditPassword, mEditCpassword;
    private String email, password, name, confirmPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected int getFrameLayoutContainerId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        //defining xml object
        mEditName = findViewById(R.id.edt_name);
        mEditEmail = findViewById(R.id.edt_email);
        mEditPassword = findViewById(R.id.edt_password);
        mEditCpassword = findViewById(R.id.edt_confirm_password);
    }


    public boolean validateFields() {
        name = mEditName.getText().toString().trim();
        email = mEditEmail.getText().toString().trim();
        password = mEditPassword.getText().toString().trim();
        confirmPassword = mEditCpassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            mEditName.setError("Name is required");
            return false;
        } else if (TextUtils.isEmpty(email)) {
            mEditEmail.setError("Email is required");
            return false;
        } else if (!isValidEmail(email)) {
            mEditEmail.setError("Email is not valid");
            return false;
        } else if (TextUtils.isEmpty(password)) {
            mEditPassword.setError("Password is required");
            return false;
        } else if (!isValidPassword(password)) {
            mEditPassword.setError("Password characters should be greater then 6");
            return false;
        } else if (TextUtils.isEmpty(confirmPassword)) {
            mEditCpassword.setError("Password is required");
            return false;
        } else if (!isValidPassword(confirmPassword)) {
            mEditCpassword.setError("Password characters should be greater then 6");
            return false;
        } else if (!isPasswordEqual(password, confirmPassword)) {
            mEditCpassword.setError("Password mismatch");
            return false;
        } else {
            return true;
        }
    }


    public void onCreateClick(View view) {
        if (validateFields()) {
            if (IS_NETWORK_CONNECTED) {
                Utilities.showCirclarAlert(this);
                //call
                RetrofitCall retrofitCall = new RetrofitCall();
                HashMap<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("email", email);
                map.put("password", password);
                map.put("c_password", confirmPassword);
                retrofitCall.getSignUpFromUser(map, getApplicationContext(), this);
            }
        }
    }

    public boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    public boolean isPasswordEqual(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    @Override
    public void onSuccess() {
        Utilities.stopCirclarAlert();
        Toast.makeText(this, "Account created. Please Login", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onFailure(String error) {
        Utilities.stopCirclarAlert();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
