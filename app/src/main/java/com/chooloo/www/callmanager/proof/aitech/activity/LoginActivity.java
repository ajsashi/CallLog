package com.chooloo.www.callmanager.proof.aitech.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.chooloo.www.callmanager.proof.aitech.BaseActivity;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.proof.aitech.listener.LoginListener;
import com.chooloo.www.callmanager.proof.aitech.network.retrofit.RetrofitCall;
import com.chooloo.www.callmanager.proof.aitech.util.Constants;
import com.chooloo.www.callmanager.proof.aitech.util.Utilities;
import com.chooloo.www.callmanager.proof.aitech.util.preference.DataBinding;
import com.chooloo.www.callmanager.proof.aitech.util.preference.PreferenceDataManager;

import java.util.HashMap;

import static android.Manifest.permission.READ_CALL_LOG;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_NETWORK_CONNECTED;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_USER_LOGGED_IN;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.LAST_CALL_BACKUP_TIME;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.USER_LOGGED;

public class LoginActivity extends BaseActivity implements LoginListener {

    private EditText mEditEmail, mEditPassword;
    private String email, password;
    private CheckBox mCheckBoxRemember;
    private boolean mRememberCheck;
    private PreferenceDataManager manager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected int getFrameLayoutContainerId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        //defining objects
        mEditEmail = findViewById(R.id.edt_email);
        mEditPassword = findViewById(R.id.edt_password);
        mCheckBoxRemember = findViewById(R.id.checkbox_remember);

        /*deafault handler*/
        Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 123);
        }

        setPreferenceData();


    }

    public void onSignInClick(View view) {
        if (validateFields()) {
            if (IS_NETWORK_CONNECTED) {
                Utilities.showCirclarAlert(this);
                RetrofitCall retrofitCall = new RetrofitCall();
                HashMap<String, String> map = new HashMap<>();
                map.put("email", email);
                map.put("password", password);
                retrofitCall.getLoginFromUser(map, getApplicationContext(), this);
            }
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CALL_LOG);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{READ_CALL_LOG}, 100);
    }


    public boolean validateFields() {
        email = mEditEmail.getText().toString().trim();
        password = mEditPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
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
        } else {
            return true;
        }
    }


    public boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    @Override
    public void onSuccess() {
        Utilities.stopCirclarAlert();
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        if (checkPreviousEmail())//if the same user doesnt login once again, we will clear the last backup time
            LAST_CALL_BACKUP_TIME = null;
        IS_USER_LOGGED_IN = true;
        manager.storeUserData("lastBackUpTime", "");
        manager.storeUserData(USER_LOGGED, Boolean.toString(IS_USER_LOGGED_IN));
        manager.storeUserData("userToken", Constants.USER_AUTH_TOKEN);
        //saving data to pref on success
        DataBinding manager = new DataBinding();
        manager.setLoginData(this, email, password, mRememberCheck, Constants.USER_AUTH_TOKEN);
        startActivity(new Intent(this, CallLogActivity.class));
        finish();
    }

    @Override
    public void onFailure(String error) {
        Utilities.stopCirclarAlert();
        Utilities.showCommomAlert("Sorry", error, this);
    }

    public void OnCreateAccountClick(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    public void onForgotClick(View view) {
        startActivity(new Intent(this, ForgotPassword.class));
    }


    public void itemRememberClicked(View v) {
        //code to check if this checkbox is checked!
        CheckBox checkBox = (CheckBox) v;
        if (checkBox.isChecked()) {
            mRememberCheck = true;
        } else {
            mRememberCheck = false;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0) {
                boolean callLogPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (!callLogPermission) {
                    if (shouldShowRequestPermissionRationale(READ_CALL_LOG)) {
                        showMessageOKCancel("You need to allow access to Read call log. The call Log information is displayed with in the " +
                                        "application for user convenience.",
                                (dialog, which) -> requestPermissions(new String[]{READ_CALL_LOG},
                                        100));
                        return;
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (resultCode == Activity.RESULT_OK) {
                if (!checkPermission()) {
                    requestPermission();

                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }

    public void setPreferenceData() {
        //loading data from pref
        manager = new PreferenceDataManager(this);
        mRememberCheck = Boolean.valueOf(manager.getUserData("isRemember"));
        if (TextUtils.isEmpty(LAST_CALL_BACKUP_TIME)) {
            LAST_CALL_BACKUP_TIME = null;
        }
        if (mRememberCheck) {
            mCheckBoxRemember.setChecked(true);
            mEditEmail.setText(manager.getUserData("email"));
            mEditPassword.setText(manager.getUserData("password"));
        }
    }

    public boolean checkPreviousEmail() {
        return email.equals(manager.getUserData("email"));
    }
}
