package com.chooloo.www.callmanager.proof.aitech.fragment.sync;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.chooloo.www.callmanager.proof.aitech.BaseFragment;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.proof.aitech.fragmentmanager.manager.IFragment;
import com.chooloo.www.callmanager.proof.aitech.listener.SyncListener;
import com.chooloo.www.callmanager.proof.aitech.network.retrofit.RetrofitCall;
import com.chooloo.www.callmanager.proof.aitech.util.Constants;
import com.chooloo.www.callmanager.proof.aitech.util.Utilities;
import com.chooloo.www.callmanager.proof.aitech.util.preference.PreferenceDataManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_BACK_UP_DONE;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_LOG_SYNCED;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_NETWORK_CONNECTED;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.LAST_CALL_BACKUP_TIME;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.callLogArrayList;

public class SyncFragment extends BaseFragment implements IFragment, SyncListener {
    private static final String FRAGMENT_NAME = "Sync";
    @BindView(R.id.btn_sync)
    Button mButtonSync;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sync;
    }

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

    @OnClick(R.id.btn_sync)
    void onButtonSyncClick() {
        if (callLogArrayList.size() > 0) {
            if (IS_NETWORK_CONNECTED) {
                Utilities.showCirclarAlert(getContext());
                RetrofitCall call = new RetrofitCall();
                if (IS_BACK_UP_DONE) {
                    IS_BACK_UP_DONE = false;//resetting backup option
                    Map<String, RequestBody> requestBodyMap = new HashMap<>();
                    requestBodyMap.put("callproof_user_id", RequestBody.create(MediaType.parse("text/plain"), Constants.CALL_PROOF_USER_ID));
                    File file = new File(Utilities.getDataFilePathWithFolder(Constants.APP_FOLDER_NAME, Constants.FILE_CALL_LOG));
                    Uri saveUri = Uri.fromFile(file);
                    RequestBody filePart = RequestBody.create(MediaType.parse("text/plain"),
                            file);
                    MultipartBody.Part fileBody = MultipartBody.Part.createFormData("data", file.getName(), filePart);

                    Log.d("Uri", String.valueOf(saveUri));
                    call.uploadSyncLogFile(requestBodyMap, fileBody, getContext(), this);
                } else {//json upload
                    Gson gson = new GsonBuilder().create();
                    JsonArray myCustomArray = gson.toJsonTree(callLogArrayList).getAsJsonArray();
                    String callLog = myCustomArray.toString();
                    //network call
                    HashMap<String, String> map = new HashMap<>();
                    map.put("callproof_user_id", Constants.CALL_PROOF_USER_ID);
                    map.put("data", callLog);
                    call.uploadSyncLog(map, getContext(), this);
                }
            }
        } else {
            Utilities.showCommomAlert("Oops", "No new call-Logs recorded. Please refresh", getContext());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    public static SyncFragment newInstance() {
        return new SyncFragment();
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

    @Override
    public void onSuccess(String success) {
        Utilities.stopCirclarAlert();
        if (callLogArrayList != null)
            callLogArrayList.clear();
        IS_LOG_SYNCED = true;
        PreferenceDataManager preferenceDataManager = new PreferenceDataManager(getContext());
        preferenceDataManager.storeUserData("lastBackUpTime", LAST_CALL_BACKUP_TIME);
        preferenceDataManager.storeUserData("logSync", Boolean.toString(IS_LOG_SYNCED));
        showAlert("Success", success, getContext());
    }

    @Override
    public void onFailure(String error) {
        Utilities.stopCirclarAlert();
        showAlert("Oops", error, getContext());
    }

    private void showAlert(String header, String message, Context context) {
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
                    callproofChannel.popUp();
                    callproofChannel.showAllCalls();
                    dialog.dismiss();
                }
        );
    }
}
