package com.boxico.android.kn.funforlabelapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.boxico.android.kn.funforlabelapp.dtos.LabelAttributes;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;

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
        this.initializeCreator();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initializeCreator() {

        LabelAttributes la1, la2 = null;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        la1 = ConstantsAdmin.selectedLabelAttrbText;
        if(ConstantsAdmin.selectedLabelAttrbTitle != null){
            la2 = ConstantsAdmin.selectedLabelAttrbTitle;
        }
        //int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        float screenWidthMM = ConstantsAdmin.pxToMm((float) width, this);
        boolean acotar = false;
        if(screenWidthMM < ConstantsAdmin.currentCreator.getWidth()){
            acotar = true;
        }

        RelativeLayout linearTag = this.findViewById(R.id.relativeReadyToGoTag);
        ConstantsAdmin.customizeBackground(ConstantsAdmin.selectedBackground,ConstantsAdmin.currentCreator, acotar, linearTag, this);


        // CONFIGURACION DE UN AREA DE TEXTO

        EditText textTag;
        EditText titleTag;
        if(la1.getIsTitle()==0) {
            textTag = ConstantsAdmin.createTextArea(new EditText(this), la1, this.getString(R.string.your_name_here),ConstantsAdmin.currentCreator, acotar, linearTag,me);
        }else{
            titleTag = ConstantsAdmin.createTextArea(new EditText(this), la1, this.getString(R.string.your_title),ConstantsAdmin.currentCreator, acotar, linearTag,me);
        }
        if(la2 != null) {
            if(la2.getIsTitle()==0){
                textTag = ConstantsAdmin.createTextArea(new EditText(this), la2, this.getString(R.string.your_name_here),ConstantsAdmin.currentCreator, acotar, linearTag,me);
            }else {
                titleTag = ConstantsAdmin.createTextArea(new EditText(this), la2, this.getString(R.string.your_title),ConstantsAdmin.currentCreator, acotar, linearTag,me);
            }
        }


    }

    private void askForWriteStoragePermission() {
        
    }

    private void configureWidgets() {
    }

    private void initializeService() {
    }

}
