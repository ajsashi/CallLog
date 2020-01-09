package com.chooloo.www.callmanager.proof.aitech.util;

import android.database.Cursor;

import com.chooloo.www.callmanager.proof.aitech.model.CallLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_LOG_SYNCED;
import static com.chooloo.www.callmanager.proof.aitech.util.Constants.LAST_CALL_BACKUP_TIME;

public class BackupDataHandler {

    public static ArrayList<CallLog> getCallLogDetails(Cursor cursor) {
        ArrayList<CallLog> callLogsArrayList = new ArrayList<>();
/*        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType(android.provider.CallLog.Calls.CONTENT_TYPE);
        intent.putExtra(android.provider.CallLog.Calls.EXTRA_CALL_TYPE_FILTER, android.provider.CallLog.Calls.MISSED_TYPE);
        startActivity(intent);*/

//        Cursor cursor = context.getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI,
//                null, null, null, android.provider.CallLog.Calls.DATE + " DESC");
        int name = cursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME);
        int number = cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(android.provider.CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(android.provider.CallLog.Calls.DURATION);
        CallLog callLog;
        while (cursor.moveToNext()) {
            String nameCall = Utilities.checkNullString(cursor.getString(name));
            String phNumber = Utilities.checkNullString(cursor.getString(number));
            String callType = Utilities.checkNullString(cursor.getString(type));
            String callDate = Utilities.checkNullString(cursor.getString(date));
            Date callDayTime = new Date(Long.valueOf(callDate));
            SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
            String time = localDateFormat.format(callDayTime);
            String callDuration = cursor.getString(duration);
            String dir = "";
            int dircode = Integer.parseInt(callType);
            if (LAST_CALL_BACKUP_TIME == null) {
                LAST_CALL_BACKUP_TIME = callDate;
            }
            switch (dircode) {
                case android.provider.CallLog.Calls.OUTGOING_TYPE:
                    dir = Constants.OUTGOING;
                    break;
                case android.provider.CallLog.Calls.INCOMING_TYPE:
                    dir = Constants.INCOMING;
                    break;

                case android.provider.CallLog.Calls.MISSED_TYPE:
                    dir = Constants.MISSED;
                    break;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            if (IS_LOG_SYNCED) {//1577355880538
                if (Long.valueOf(callDate) > Long.valueOf(LAST_CALL_BACKUP_TIME)) {
                    callLog = new CallLog(nameCall, phNumber, dir, callDuration, sdf.format(callDayTime), time);
                    callLogsArrayList.add(callLog);
                }
            } else {
                callLog = new CallLog(nameCall, phNumber, dir, callDuration, sdf.format(callDayTime), time);
                callLogsArrayList.add(callLog);
            }
        }
        cursor.close();
        return callLogsArrayList;
    }
}
