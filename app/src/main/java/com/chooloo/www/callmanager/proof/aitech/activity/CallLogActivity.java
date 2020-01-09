package com.chooloo.www.callmanager.proof.aitech.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.chooloo.www.callmanager.proof.aitech.BaseActivity;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.proof.aitech.MainActivity1;
import com.chooloo.www.callmanager.proof.aitech.util.BackupDataHandler;
import com.chooloo.www.callmanager.proof.aitech.util.Constants;

import static com.chooloo.www.callmanager.proof.aitech.util.Constants.INCOMING_CALL;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.MISSED_CALL;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.OUTGOING_CALL;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.callLogArrayList;

public class CallLogActivity extends BaseActivity {

    private static final int READ_LOGS = 725;
    private Runnable logsRunnable;
    private String[] requiredPermissions = {Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_call_log;
    }

    @Override
    protected int getFrameLayoutContainerId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* Dexter.withActivity(this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CALL_LOG)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            if (checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(CallLogActivity.this, "You need to grant the call log permission", Toast.LENGTH_SHORT).show();
                                // TODO: Consider calling
                                //    Activity#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for Activity#requestPermissions for more details.
                                return;
                            }
                            Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
                                    null, null, null, CallLog.Calls.DATE + " DESC");
                            Constants.callLogArrayList = BackupDataHandler.getCallLogDetails(cursor);
                            startActivity(new Intent(CallLogActivity.this, MainActivity.class));
                            //Utilities.callService(BackupCallLogService.class);
                            finish();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();*/

        if (checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            requestPermissions(
                    new String[]{Manifest.permission
                            .READ_CALL_LOG},
                    725);
        } else {
            Cursor cursor = getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI,
                    null, null, null, android.provider.CallLog.Calls.DATE + " DESC");
            Constants.callLogArrayList = BackupDataHandler.getCallLogDetails(cursor);
            startActivity(new Intent(CallLogActivity.this, MainActivity1.class));
            finish();
        }

//        logsRunnable = new Runnable() {
//            @Override
//            public void run() {
//                loadLogs();
//            }
//        };
//
//        // Checking for permissions
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            checkPermissionToExecute(requiredPermissions, READ_LOGS, logsRunnable);
//        } else {
//            logsRunnable.run();
//        }

    }


    public String getCallType(int type) {
        String typeCall = "";
        switch (type) {
            case MISSED_CALL:
                typeCall = "MISSED";
                break;
            case INCOMING_CALL:
                typeCall = "INCOMING";
                break;
            case OUTGOING_CALL:
                typeCall = "OUTGOING";
                break;
        }
        return typeCall;
    }

    private void checkPermissionToExecute(String permissions[], int requestCode, Runnable runnable) {

        boolean logs = ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED;
        boolean contacts = ContextCompat.checkSelfPermission(this, permissions[1]) != PackageManager.PERMISSION_GRANTED;

        if (logs || contacts) {
            requestPermissions(permissions, requestCode);
        } else {
            runnable.run();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_LOGS && permissions[0].equals(Manifest.permission.READ_CALL_LOG)) {
            if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                Cursor cursor = getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI,
                        null, null, null, android.provider.CallLog.Calls.DATE + " DESC");
                Constants.callLogArrayList = BackupDataHandler.getCallLogDetails(cursor);
                startActivity(new Intent(CallLogActivity.this, MainActivity1.class));
                finish();
            } else {
                new AlertDialog.Builder(CallLogActivity.this)
                        .setMessage("The app needs these permissions to work, Exit?")
                        .setTitle("Permission Denied")
                        .setCancelable(false)
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                checkPermissionToExecute(requiredPermissions, READ_LOGS, logsRunnable);
                            }
                        })
                        .setNegativeButton("Exit App", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).show();
            }
        }
    }

    public void showNext() {

    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

}
