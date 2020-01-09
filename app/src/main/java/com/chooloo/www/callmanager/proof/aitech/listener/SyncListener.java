package com.chooloo.www.callmanager.proof.aitech.listener;

public interface SyncListener {
    void onSuccess(String success);

    void onFailure(String error);
}
