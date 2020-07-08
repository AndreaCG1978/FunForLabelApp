package com.boxico.android.kn.funforlabelapp;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.boxico.android.kn.funforlabelapp.dtos.LabelAttributes;
import com.boxico.android.kn.funforlabelapp.dtos.ProductoCarrito;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;

import java.io.File;

public class CarritoActivity extends FragmentActivity {

    private CarritoActivity me;
    TextView textWellcomeUsr = null;
    Button confirmarCarrito = null;
    ListView listViewCarrito = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;
        setContentView(R.layout.carrito);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.initializeService();
        this.configureWidgets();
        this.askForWriteStoragePermission();
   //     this.initializeCreator();
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private void createAlertDialog(String message, String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(me);
        builder.setMessage(message).setTitle(title);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
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

        EditText textTag = null;
        EditText titleTag = null;
        if(la1.getIsTitle()==0) {
            textTag = ConstantsAdmin.createTextArea(new EditText(this), la1, "",ConstantsAdmin.currentCreator, acotar, linearTag,me);
        }else{
            titleTag = ConstantsAdmin.createTextArea(new EditText(this), la1,"",ConstantsAdmin.currentCreator, acotar, linearTag,me);
        }
        if(la2 != null) {
            if(la2.getIsTitle()==0){
                textTag = ConstantsAdmin.createTextArea(new EditText(this), la2, "",ConstantsAdmin.currentCreator, acotar, linearTag,me);
            }else {
                titleTag = ConstantsAdmin.createTextArea(new EditText(this), la2, "",ConstantsAdmin.currentCreator, acotar, linearTag,me);
            }
        }
        textTag.setText(ConstantsAdmin.textEntered);
        textTag.setTextColor(ConstantsAdmin.selectedTextFontColor);
        textTag.setTextSize(TypedValue.TYPE_STRING, ConstantsAdmin.selectedTextFontSize);
        File fileFont = ConstantsAdmin.getFile(ConstantsAdmin.selectedTextFont);
        Typeface face = Typeface.createFromFile(fileFont);
        textTag.setTypeface(face);
        textTag.setEnabled(false);
        if(titleTag != null){
            titleTag.setEnabled(false);
            titleTag.setText(ConstantsAdmin.titleEntered);
            titleTag.setTextColor(ConstantsAdmin.selectedTitleFontColor);
            titleTag.setTextSize(TypedValue.TYPE_STRING, ConstantsAdmin.selectedTitleFontSize);
            fileFont = ConstantsAdmin.getFile(ConstantsAdmin.selectedTitleFont);
            face = Typeface.createFromFile(fileFont);
            titleTag.setTypeface(face);
        }



    }

    private void askForWriteStoragePermission() {
        
    }

    private void configureWidgets() {
        textWellcomeUsr = findViewById(R.id.textWellcomeUser);
        textWellcomeUsr.setText(getString(R.string.wellcomeUser) + " " + ConstantsAdmin.currentCustomer.getFirstName() + " " + ConstantsAdmin.currentCustomer.getLastName());
        confirmarCarrito = findViewById(R.id.btnConfirmarCarrito);
        listViewCarrito = findViewById(R.id.listViewCarrito);

        confirmarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarCompra();
            }
        });
    }

    private void confirmarCompra() {
    }


    private void initializeService() {
    }

}
