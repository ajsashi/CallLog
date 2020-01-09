package com.chooloo.www.callmanager.proof.aitech.fragment.allcalls;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.proof.aitech.BaseFragment;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.proof.aitech.adapter.CallLogAdapter;
import com.chooloo.www.callmanager.proof.aitech.fragmentmanager.manager.IFragment;
import com.chooloo.www.callmanager.proof.aitech.model.CallLog;
import com.chooloo.www.callmanager.proof.aitech.services.BackupCallLogService;
import com.chooloo.www.callmanager.proof.aitech.util.Constants;
import com.chooloo.www.callmanager.proof.aitech.util.Utilities;
import com.chooloo.www.callmanager.proof.aitech.util.preference.PreferenceDataManager;

import java.util.ArrayList;
import java.util.Objects;

import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_LOG_SYNCED;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_USER_LOGGED_IN;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.LAST_CALL_BACKUP_TIME;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.USER_LOGGED;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.USER_NAME;

public class AllCallsFragment extends BaseFragment implements IFragment, View.OnClickListener {
    private static final String FRAGMENT_NAME = "AllCalls";
    private RecyclerView mCallLogView;
    private ImageView mMenuItem;
    private TextView mNoCallLog;
    PreferenceDataManager preferenceDataManager;

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
        //define view
        mCallLogView = view.findViewById(R.id.rv_callLogs);
        mMenuItem = view.findViewById(R.id.img_menu);
        mNoCallLog = view.findViewById(R.id.txt_no_callLogs);
        //pref
        preferenceDataManager = new PreferenceDataManager(getContext());
        //loading the call log
        ArrayList<CallLog> callLogArrayList = new ArrayList<>();
        callLogArrayList = Constants.callLogArrayList;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCallLogView.setLayoutManager(layoutManager);
        mCallLogView.setVisibility(View.VISIBLE);
        if (callLogArrayList.size() > 0) {
            mCallLogView.setVisibility(View.VISIBLE);
            mNoCallLog.setVisibility(View.GONE);
            CallLogAdapter adapter = new CallLogAdapter(callLogArrayList);
            mCallLogView.setAdapter(adapter);
        } else {
            mCallLogView.setVisibility(View.GONE);
            mNoCallLog.setVisibility(View.VISIBLE);
        }

        mMenuItem.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_all_calls;
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

    public static AllCallsFragment newInstance() {
        return new AllCallsFragment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_menu:
                showDialog(getActivity());
            /*    if (Constants.callLogArrayList.size() > 0) {
                    Toast.makeText(getActivity(), "CallLog Back-Up started", Toast.LENGTH_SHORT).show();
                    new Thread() {
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    //we have already backe'd our file :) in this page
                                    Toast.makeText(getActivity(), "Back-Up Completed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }.start();

                } else {
                    Toast.makeText(getActivity(), "No CallLogs to Back-Up. Please refresh", Toast.LENGTH_SHORT).show();
                }*/
                break;
        }
    }

    public void showDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.menu_item_call);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //backup
        Button backup = dialog.findViewById(R.id.btn_backup);
        backup.setOnClickListener(v -> {
            if (Constants.callLogArrayList.size() > 0) {
                Utilities.callService(BackupCallLogService.class);
                Utilities.toast("Call log BackUp Started");
            } else {
                Toast.makeText(activity, "No new call logs to Back-Up", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });
        //refresh
        Button refresh = dialog.findViewById(R.id.btn_refresh);
        refresh.setOnClickListener(v -> {
            callproofChannel.showRefresh();
            dialog.dismiss();
        });
        //logout
        Button logout = dialog.findViewById(R.id.btn_logout);
        logout.setOnClickListener(v -> {
            preferenceDataManager.storeUserData("lastBackUpTime", LAST_CALL_BACKUP_TIME);
            preferenceDataManager.storeUserData("logSync", Boolean.toString(false));//setting the last sync to false, after logout
            preferenceDataManager.storeUserData(USER_LOGGED, Boolean.toString(false));//user pref is set to false
            preferenceDataManager.storeUserData("userName", USER_NAME);
            IS_USER_LOGGED_IN = false;
            IS_LOG_SYNCED = false;
            callproofChannel.showLogout();
            dialog.dismiss();
        });
        Button cancel = dialog.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }

}
