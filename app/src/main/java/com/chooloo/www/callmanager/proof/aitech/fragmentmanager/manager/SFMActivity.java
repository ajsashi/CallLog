package com.chooloo.www.callmanager.proof.aitech.fragmentmanager.manager;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public abstract class SFMActivity extends AppCompatActivity {

    protected abstract int getFrameLayoutContainerId();

    public SimpleFragmentManager simpleFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        simpleFragmentManager = new SimpleFragmentManager(getSupportFragmentManager(), getFrameLayoutContainerId());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        simpleFragmentManager.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        simpleFragmentManager.onRestoreInstanceState(savedInstanceState);
    }

    protected void onSFMBackPressed() {
        if (!simpleFragmentManager.onBackPressed()) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        onSFMBackPressed();
    }
}
