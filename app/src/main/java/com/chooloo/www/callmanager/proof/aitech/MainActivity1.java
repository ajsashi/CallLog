package com.chooloo.www.callmanager.proof.aitech;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chooloo.www.callmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.chooloo.www.callmanager.proof.aitech.activity.CallLogActivity;
import com.chooloo.www.callmanager.proof.aitech.activity.LoginActivity;
import com.chooloo.www.callmanager.proof.aitech.fragment.allcalls.AllCallsFragment;
import com.chooloo.www.callmanager.proof.aitech.fragment.callproof.CallLoginFragment;
import com.chooloo.www.callmanager.proof.aitech.fragment.callproof.CallSuccessFragment;
import com.chooloo.www.callmanager.proof.aitech.fragment.sync.SyncFragment;
import com.chooloo.www.callmanager.proof.aitech.fragmentmanager.CallproofChannel;
import com.chooloo.www.callmanager.proof.aitech.fragmentmanager.manager.FragmentManagerHandler;
import com.chooloo.www.callmanager.proof.aitech.util.Constants;
import com.chooloo.www.callmanager.proof.aitech.util.Utilities;
import com.chooloo.www.callmanager.proof.aitech.util.preference.PreferenceDataManager;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;

import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_CALLPROOF_CONNECTED;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_CALLPROOF_SUCCESS;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_PUSH_NOTIFICATION_SHOWN;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.USER_NAME;
import static com.chooloo.www.callmanager.proof.aitech.util.Utilities.isPreVersionO;

public class MainActivity1 extends BaseActivity implements CallproofChannel, BaseFragment.FragmentInteractionCallback {

    @BindView(R.id.container)
    FrameLayout flContainer;
    @BindView(R.id.linear_layout)
    LinearLayout mBottomView;
    private ImageView mAllCalls, mCallProofConnect, mSyncLog;
    @Inject
    FragmentManagerHandler fragmentManagerHandler;
    boolean doubleBackToExitPressedOnce = false;
    private Unregistrar unregistrar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main1;
    }

    @Override
    protected int getFrameLayoutContainerId() {
        return R.id.container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injector().inject(this);
        //define view
        mAllCalls = findViewById(R.id.img_all_calls);
        mCallProofConnect = findViewById(R.id.img_backup);
        mSyncLog = findViewById(R.id.img_sync);

        fragmentManagerHandler.setFragmentContainerId(flContainer);
        //default
        showAllCalls();
        //showing push notification
        if (IS_PUSH_NOTIFICATION_SHOWN)
            setPushNotification();


       /* FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            //To do//
                            return;
                        }
                        // Get the Instance ID token//
                        Constants.FIREBASE_TOKEN = task.getResult().getToken();
                        *//*String msg = getString(R.string.fcm_token, token);
                        Log.d(TAG, msg);*//*
                    }
                });*/

        //keyboard listener
        unregistrar = KeyboardVisibilityEvent.registerEventListener(
                this,
                isOpen -> {
                    if (isOpen) {
                        mBottomView.setVisibility(View.GONE);
                        Log.v("KeyOpen", "Open");
                    } else {
                        mBottomView.setVisibility(View.VISIBLE);
                        Log.v("KeyCLose", "Close");
                    }
                    // some code depending on keyboard visiblity status
                });
    }


    public void onUploadClick(View view) {
        Toast.makeText(this, "Upload clicked", Toast.LENGTH_SHORT).show();
    }

    public void onStop() {
        super.onStop();
    }

    public void onPause() {
        super.onPause();
        finish();
    }

    public void onSyncClick(View view) {
        showSync();
    }

    public void showSync() {
        mAllCalls.setImageResource(R.drawable.all_calls_disabled);
        mCallProofConnect.setImageResource(R.drawable.callproof_disabled);
        mSyncLog.setImageResource(R.drawable.sync_log);
        //frag load
        simpleFragmentManager.replaceFragment(SyncFragment.newInstance());
        if (!IS_CALLPROOF_SUCCESS)
            showAlert("Sorry", "You need to connect with callProof first", this);
    }

    public void onCallproofClick(View view) {
        showCallProof();
    }

    public void showCallProof() {
        mAllCalls.setImageResource(R.drawable.all_calls_disabled);
        mCallProofConnect.setImageResource(R.drawable.callproof_enable);
        mSyncLog.setImageResource(R.drawable.sync_log_disabled);
        //frag load
        if (IS_CALLPROOF_CONNECTED) {
            simpleFragmentManager.replaceFragment(CallSuccessFragment.newInstance());
        } else {
            simpleFragmentManager.replaceFragment(CallLoginFragment.newInstance());
        }

        // Utilities.showCommomAlert("CallProof Connect", "Connecting Please Wait..", this);
    }

    public void showLogout() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void showRefresh(){
        popUp();
        startActivity(new Intent(this, CallLogActivity.class));
    }

    @Override
    public void onFragmentInteractionCallback(Bundle bundle) {

    }

    public void onAllCallsClick(View view) {
        showAllCalls();
    }

    public void showAllCalls() {
        mAllCalls.setImageResource(R.drawable.all_calls);
        mCallProofConnect.setImageResource(R.drawable.callproof_disabled);
        mSyncLog.setImageResource(R.drawable.sync_log_disabled);
        //frag load
        simpleFragmentManager.popUpAll();
        simpleFragmentManager.replaceFragment(AllCallsFragment.newInstance());
    }

    @Override
    public void onBackPressed() {
        simpleFragmentManager.onBackPressed();
        resolveTabPositions(simpleFragmentManager.getFragmentName());
    }


    private void resolveTabPositions(String currentTab) {
        if (currentTab != null) {
            switch (currentTab) {
                case "AllCalls":
                case "":
                    showAllCalls();
                    onButtonBackPressed();
                    break;
                case "Sync":
                    showSync();
                    break;
                case "CallForgot":
                    showCallProof();
                    break;
                case "CallLogin":
                    showCallProof();
                    break;
                case "CallSignUp":
                    showCallProof();
                    break;

            }
        }

    }

    private void onButtonBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press BACK to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void popUp() {
        simpleFragmentManager.popUp();
    }

    @Override
    public void popUpAll() {
        simpleFragmentManager.popUpAll();
    }

//    public void showRefresh() {
//        startActivity(new Intent(this, CallLogActivity.class));
//    }

    @Override
    public void setToolbarTitle(int titleId) {

    }


    public void setPushNotification() {
        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(20000);
                    //displaying an offline notification with delay of 20sec for first  time
                    if (isPreVersionO()) {
                        Utilities.pushNotificationPre26(MainActivity1.this);
                    } else {
                        Utilities.pushNotificationPost26(MainActivity1.this);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();

    }


    public void showAlert(String header, String message, Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.common_alert_view);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        ConstraintLayout layout = dialog.findViewById(R.id.constraintLayout);
        TextView txt_header = dialog.findViewById(R.id.txt_header);
        TextView name = dialog.findViewById(R.id.messageAlert);

        txt_header.setText(header);
        name.setText(message);
        TextView okclick = dialog.findViewById(R.id.okClick);
        layout.setVisibility(android.view.View.VISIBLE);
        okclick.setOnClickListener(v -> {
                    simpleFragmentManager.popUp();
                    showCallProof();
                    dialog.dismiss();
                }
        );
    }

    public void onDestroy() {
        super.onDestroy();
        if (unregistrar != null) {
            unregistrar.unregister();
        }
        PreferenceDataManager preferenceDataManager = new PreferenceDataManager(getApplicationContext());
        preferenceDataManager.storeUserData("userName", USER_NAME);
    }
}
