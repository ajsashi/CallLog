package com.chooloo.www.callmanager.proof.aitech.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Xml;

import com.chooloo.www.callmanager.proof.aitech.model.CallLog;
import com.chooloo.www.callmanager.proof.aitech.util.Constants;
import com.chooloo.www.callmanager.proof.aitech.util.Utilities;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import static com.chooloo.www.callmanager.proof.aitech.util.Constants.IS_BACK_UP_DONE;

public class BackupCallLogService extends IntentService {
    private Handler mHandler;
    private XmlSerializer serializer;
    private FileOutputStream fileOutputStream;

    public BackupCallLogService() {
        super("BackupCallLogService");
        mHandler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            ArrayList<CallLog> callLogArrayList = new ArrayList<>();
//            callLogArrayList = BackupDataHandler.getCallLogDetails(this);
//            Constants.callLogArrayList = new ArrayList<>();
            callLogArrayList = Constants.callLogArrayList;
            //if (callLogArrayList.size() > 0) {
            String fileName = Constants.FILE_CALL_LOG.replace("$d$", new Date().getTime() + "");
            File file = new File(Utilities.getDataFilePathWithFolder(Constants.APP_FOLDER_NAME, fileName));
            if (file.exists())
                file.delete();
            fileOutputStream = new FileOutputStream(file);

            serializer = Xml.newSerializer();
            serializer.setOutput(fileOutputStream, Constants.XML_SERIALIZER_OUTPUT);
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature(Constants.XML_SERIALIZER_FEATURE, true);

            serializer.startTag(null, Constants.XML_SERIALIZER_ROOT);


            for (CallLog callLog : callLogArrayList) {
                serializer.startTag(null, Constants.CALL_LOG);

                serializer.startTag(null, Constants.NAME);
                serializer.text(callLog.getName());
                serializer.endTag(null, Constants.NAME);

                serializer.startTag(null, Constants.NUMBER);
                serializer.text(callLog.getNumber());
                serializer.endTag(null, Constants.NUMBER);

                serializer.startTag(null, Constants.CALL_TYPE);
                serializer.text(callLog.getCallType());
                serializer.endTag(null, Constants.CALL_TYPE);

                serializer.startTag(null, Constants.CALL_DURATION);
                serializer.text(callLog.getCallDuration());
                serializer.endTag(null, Constants.CALL_DURATION);

                serializer.startTag(null, Constants.CALL_TIME);
                serializer.text(callLog.getCallTime());
                serializer.endTag(null, Constants.CALL_TIME);

                serializer.startTag(null, Constants.CALL_DAY_TIME);
                serializer.text(callLog.getCallDayTime());
                serializer.endTag(null, Constants.CALL_DAY_TIME);

                serializer.endTag(null, Constants.CALL_LOG);
            }

            serializer.endDocument();
            serializer.flush();
            IS_BACK_UP_DONE = true;
            mHandler.post(new Utilities.DisplayToastInRunnable(Constants.Messages.ENDED_CALL_LOG_BACKUP));

//            Intent mIntent = new Intent(this, MainActivity.class);
//            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(mIntent);
//            } else {
//                mHandler.post(new Utilities.DisplayToastInRunnable("No call logs Found"));
//            }
        } catch (Exception e) {
            mHandler.post(new Utilities.DisplayToastInRunnable(Constants.Messages.FAILED_CALL_LOG_BACKUP));
        }
    }
}
