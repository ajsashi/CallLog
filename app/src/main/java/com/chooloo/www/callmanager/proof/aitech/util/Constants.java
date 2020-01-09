package com.chooloo.www.callmanager.proof.aitech.util;


import com.google.gson.JsonObject;
import com.chooloo.www.callmanager.proof.aitech.model.CallLog;

import java.util.ArrayList;

public class Constants {
    public static String BASE_URL = "http://192.168.88.11/callproof/public/api/";
    public static String XML_SERIALIZER_FEATURE = "http://xmlpull.org/v1/doc/features.html#indent-output";
    public static String XML_SERIALIZER_OUTPUT = "UTF-8";
    public static String XML_SERIALIZER_ROOT = "root";
    public static String APP_FOLDER_NAME = "CallLog";
    public static String FILE_CALL_LOG = "callLog.xml";
    public static final String ACTIVITY_CONTEXT = "activity";
    public static final String APPLICATION_CONTEXT = "application";

    public static String NAME = "name";
    public static String NUMBER = "number";
    public static String CALL_LOG = "callLog";
    public static String CALL_TYPE = "callType";
    public static String CALL_DURATION = "callDuration";
    public static String CALL_DAY_TIME = "callDayTime";
    public static String CALL_TIME = "callTime";

    public static String OUTGOING = "OUTGOING";
    public static String INCOMING = "INCOMING";
    public static String MISSED = "MISSED";
    public static String SECONDS = "Seconds";
    public static ArrayList<CallLog> callLogArrayList;
    public static String USER_AUTH_TOKEN;
    public static String USER_NAME = "User";
    public static boolean IS_CALLPROOF_CONNECTED;
    public static boolean IS_CALLPROOF_SUCCESS;
    public static boolean IS_LOG_SYNCED;
    public static String FIREBASE_TOKEN;
    public static String LAST_CALL_BACKUP_TIME = null;
    public static boolean IS_NETWORK_CONNECTED;
    public static String CALL_PROOF_USER_ID;
    public static boolean IS_PUSH_NOTIFICATION_SHOWN = true;
    public static boolean IS_USER_LOGGED_IN;
    public static boolean IS_BACK_UP_DONE;

    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String DEVICE_ID_APP = "device_id_app";
    public static final String TIMEZONE = "timeZone";
    public static final String USER_LOGGED = "app_user";

    public static final int INCOMING_CALL = 1;
    public static final int OUTGOING_CALL = 2;
    public static final int MISSED_CALL = 3;



    public static class Messages
    {
        public static String STARTED_CALL_LOG_BACKUP = "Started call log backup";
        public static String ENDED_CALL_LOG_BACKUP = "Call log backup complete";
        public static String FAILED_CALL_LOG_BACKUP = "Failed call log backup";
    }


    public static JsonObject getLoginJson(String email, String password) {
        JsonObject object = new JsonObject();
        object.addProperty("email", email);
        object.addProperty("password", password);
        return object;
    }
}
