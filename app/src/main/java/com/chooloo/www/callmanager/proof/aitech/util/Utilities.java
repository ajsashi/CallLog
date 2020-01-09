package com.chooloo.www.callmanager.proof.aitech.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.BackupApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_PUSH_NOTIFICATION_SHOWN;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.USER_NAME;

public class Utilities {
    private static ProgressDialog progressDialog;
    private static Dialog dialog;
    public static final String NOTIFICATION_ALERT_CHANNEL_ID = "call_log_channel_01";


    public Utilities() {
    }

    public static void toast(String msg) {
        toast(BackupApplication.getContext(), msg);
    }

    public static void toast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    public static class DisplayToastInRunnable implements Runnable {
        String mText;

        public DisplayToastInRunnable(String text) {
            mText = text;
        }

        public void run() {
            toast(mText);
        }
    }

    public static void callService(Class<?> newActivityClass) {
        Intent nIntent = new Intent(BackupApplication.getContext(), newActivityClass);
        BackupApplication.getContext().startService(nIntent);
    }

    public static String getDataFilePathWithFolder(String folderName, String fileName) {
        ContextWrapper cw = new ContextWrapper(BackupApplication.getContext());
        File directory = cw.getDir(folderName, Context.MODE_PRIVATE);

        if (!directory.exists())
            directory.mkdirs();
        File file = new File(directory, fileName);
        return file.getAbsolutePath();
    }

    public static File getDataFolder(String folderName) {
        ContextWrapper cw = new ContextWrapper(BackupApplication.getContext());
        File directory = cw.getDir(folderName, Context.MODE_PRIVATE);

        if (!directory.exists())
            directory.mkdirs();
        return directory;
    }

    public static String checkNullString(String input) {
        if (input != null)
            return input;
        return "";
    }

    public static String getFileContent(File file) {
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            return text.toString();
        } catch (IOException e) {
            Log.i("service:error", e.toString());
            return "101";
        }
    }

    public static StringBuilder getFileContent(File file, StringBuilder text) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            text.setLength(0);
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            return text;
        } catch (IOException e) {
            Log.i("service:error", e.toString());
            text.setLength(0);
            text.append("101");
            return text;
        }
    }

    public static String getImageAsString(File file) {
        String imageDataString = "";
        try {
            // Reading a Image file from file system
            FileInputStream imageInFile = new FileInputStream(file);
            byte imageData[] = new byte[(int) file.length()];
            imageInFile.read(imageData);

            // Converting Image byte array into Base64 String
            imageDataString = encodeImage(imageData);

            /*// Converting a Base64 String into Image byte array
            byte[] imageByteArray = decodeImage(imageDataString);

            // Write a image byte array into file system
            FileOutputStream imageOutFile = new FileOutputStream(
                    "/Users/jeeva/Pictures/wallpapers/water-drop-after-convert.jpg");

            imageOutFile.write(imageByteArray);

            imageInFile.close();
            imageOutFile.close();*/
        } catch (FileNotFoundException e) {
            Log.i("service:error", "Image Not Found");
        } catch (IOException ioe) {
            Log.i("service:error", ioe.toString());
        }
        return imageDataString;
    }

    public static String encodeImage(byte[] imageByteArray) {
        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }

    public static byte[] getFileAsBytes(File file) {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            byte fileContent[] = new byte[(int) file.length()];
            fin.read(fileContent);
            return fileContent;
        } catch (Exception e) {

        }
        return null;
    }

    public static void showCirclarAlert(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_progress));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

    }

    public static void stopCirclarAlert() {

        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public static void showCommomAlert(String header, String message, Context context) {
        dialog = new Dialog(context);
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
        okclick.setOnClickListener(v -> dialog.dismiss());
    }

    public static String timeZone() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getID();
    }

    public static String getPhoneNumber(Activity activity) {
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "";
        }
        return tm.getLine1Number();
    }

    public static boolean isPreVersionO() {
        IS_PUSH_NOTIFICATION_SHOWN = false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return true;
        } else {
            return false;
        }
    }

    private static NotificationCompat.Builder notifyAlertBuilder;

    public static Notification pushNotificationPre26(Activity activity) {
        NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification result = null;
        try {
            if (notifyAlertBuilder == null) {
                notifyAlertBuilder = new NotificationCompat.Builder(activity, NOTIFICATION_ALERT_CHANNEL_ID);
                result = notifyAlertBuilder
                        .setContentTitle("Hello " + USER_NAME)
                        .setTicker("CallLog")
                        .setContentText("Please Back-Up your CallLog")
                        .setSmallIcon(R.mipmap.ic_launcher )
                        .build();
            } else {
                notifyAlertBuilder.setContentTitle("Hello " + USER_NAME);
                notifyAlertBuilder.setTicker("CallLog");
                notifyAlertBuilder.setContentText("Time to Back-Up your CallLog");
                result = notifyAlertBuilder.build();
            }
            mNotificationManager.notify(-4, result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    @TargetApi(26)
    public static Notification pushNotificationPost26(Activity activity) {
        NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification result = null;

//        Intent intent = new Intent(activity, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);
        try {
            // The user-visible name of the channel.
            CharSequence channelName = "CallLoggerchannel";
            // The user-visible description of the channel.
            String description = "CallLog ALERTS";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_ALERT_CHANNEL_ID, channelName, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);


            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            mNotificationManager.createNotificationChannel(mChannel);

            if (notifyAlertBuilder == null) {
                notifyAlertBuilder = new NotificationCompat.Builder(activity, NOTIFICATION_ALERT_CHANNEL_ID);
                result = notifyAlertBuilder
                        .setContentTitle("Hello " + USER_NAME)
                        .setTicker("CallLog")
                        .setContentText("Please Back-Up your CallLog")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        // .setContentIntent(pendingIntent)
                        .build();
            } else {
                notifyAlertBuilder.setContentTitle("Hello " + USER_NAME);
                notifyAlertBuilder.setTicker("CallLog");
                notifyAlertBuilder.setContentText("Time to Back-Up your CallLog");
                result = notifyAlertBuilder.build();
            }

            mNotificationManager.notify(-4, result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;

    }

    public static String dateToString(Long date){
        String newTime = "";
        Date callDayTime = new Date(Long.valueOf(date));
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
        newTime = localDateFormat.format(callDayTime);
        return newTime;
    }

    public static String dateTimeToString(Long date){
        String newDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date callDayTime = new Date(Long.valueOf(date));
        newDate = sdf.format(callDayTime);
        return newDate;
    }
}
