package com.boxico.android.kn.funforlabelapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.WindowManager;

import androidx.fragment.app.FragmentActivity;

public class TagReadyToGoActivity extends FragmentActivity {

    private TagReadyToGoActivity me;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;
        setContentView(R.layout.tag_ready_to_go);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.initializeService();
        this.configureWidgets();
        this.askForWriteStoragePermission();
        this.drawTag();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void drawTag() {
    }

    private void askForWriteStoragePermission() {
        
    }

    private void configureWidgets() {
    }

    private void initializeService() {
    }

}
