package com.boxico.android.kn.funforlabelapp;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boxico.android.kn.funforlabelapp.dtos.LabelAttributes;
import com.boxico.android.kn.funforlabelapp.dtos.ProductoCarrito;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

public class TagReadyToGoActivity extends AppCompatActivity {

    private TagReadyToGoActivity me;
    TextView textWellcomeUsr = null;
    Button agregarAlCarrito = null;
    EditText comentarioUsr = null;
    
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void agregarProductoAlCarrito() {
        ProductoCarrito pc = new ProductoCarrito();
        if(ConstantsAdmin.selectedLabelAttrbTitle != null){
            pc.setAreaTitulo(ConstantsAdmin.selectedLabelAttrbTitle);
            pc.setFontTitleColor(ConstantsAdmin.selectedTitleFontColor);
            pc.setTitulo(ConstantsAdmin.titleEntered);
            pc.setTitleFontName(ConstantsAdmin.selectedTitleFont.getBasename());
            pc.setTitleFontSize(ConstantsAdmin.selectedTitleFontSize);
            pc.setTieneTitulo(true);
            pc.setIdAreaTitulo((int)pc.getAreaTitulo().getTextAreasId());
            pc.setAnchoAreaTituto(ConstantsAdmin.selectedLabelAttrbTitle.getWidth());
            pc.setLargoAreaTituto(ConstantsAdmin.selectedLabelAttrbTitle.getHeight());
            pc.setEsMultilineaTituto(ConstantsAdmin.selectedLabelAttrbTitle.getMultiline());
            pc.setFromXTituto(ConstantsAdmin.selectedLabelAttrbTitle.getFromX());
            pc.setFromYTituto(ConstantsAdmin.selectedLabelAttrbTitle.getFromY());
            pc.setFontTitleId((int)ConstantsAdmin.selectedTitleFont.getId());
        }else{
            pc.setTieneTitulo(false);
        }
        pc.setAnchoAreaTexto(ConstantsAdmin.selectedLabelAttrbText.getWidth());
        pc.setLargoAreaTexto(ConstantsAdmin.selectedLabelAttrbText.getHeight());
        pc.setEsMultilineaTexto(ConstantsAdmin.selectedLabelAttrbText.getMultiline());
        pc.setFromXTexto(ConstantsAdmin.selectedLabelAttrbText.getFromX());
        pc.setFromYTexto(ConstantsAdmin.selectedLabelAttrbText.getFromY());
        pc.setAreaTexto(ConstantsAdmin.selectedLabelAttrbText);
        pc.setBackground(ConstantsAdmin.selectedBackground);
        pc.setCreador(ConstantsAdmin.currentCreator);
        pc.setFontTextColor(ConstantsAdmin.selectedTextFontColor);
        pc.setTexto(ConstantsAdmin.textEntered);
        pc.setTextFontName(ConstantsAdmin.selectedTextFont.getBasename());
        pc.setTextFontSize(ConstantsAdmin.selectedTextFontSize);
        pc.setComentarioUsr(comentarioUsr.getText().toString());
        pc.setBackgroundFilename(ConstantsAdmin.selectedBackgroundFilename);
        pc.setIdAreaTexto((int)pc.getAreaTexto().getTextAreasId());
        pc.setIdCreador((int)pc.getCreador().getId());
        pc.setRound(ConstantsAdmin.currentCreator.getRound());
        pc.setAnchoTag(ConstantsAdmin.currentCreator.getWidth());
        pc.setLargoTag(ConstantsAdmin.currentCreator.getHeight());
        pc.setNombre(ConstantsAdmin.currentProduct.getName());
        pc.setCantidad("1");
        pc.setModelo(ConstantsAdmin.currentProduct.getModel());
        pc.setPrecio(ConstantsAdmin.currentProduct.getPrice());
        pc.setIdProduct((int)ConstantsAdmin.currentProduct.getId());
        pc.setFillsTexturedId((int)ConstantsAdmin.selectedImage.getFillsTexturedId());
        pc.setFontTextId((int)ConstantsAdmin.selectedTextFont.getId());


        ConstantsAdmin.agregarProductoAlCarrito(pc);

      //  createAlertDialog(getString(R.string.tag_agregado_carrito), "");
        ConstantsAdmin.createProductoCarrito(pc, this);
        ConstantsAdmin.copyBitmapInStorage(ConstantsAdmin.selectedBackground, ConstantsAdmin.selectedBackgroundFilename);
        ConstantsAdmin.finalizarHastaMenuPrincipal = true;
        ConstantsAdmin.clearSelections();
        finish();
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
        File fileFont = ConstantsAdmin.getFile(ConstantsAdmin.selectedTextFont.getBasename());
        Typeface face = Typeface.createFromFile(fileFont);
        textTag.setTypeface(face);
        textTag.setEnabled(false);
        if(titleTag != null){
            titleTag.setEnabled(false);
            titleTag.setText(ConstantsAdmin.titleEntered);
            titleTag.setTextColor(ConstantsAdmin.selectedTitleFontColor);
            titleTag.setTextSize(TypedValue.TYPE_STRING, ConstantsAdmin.selectedTitleFontSize);
            fileFont = ConstantsAdmin.getFile(ConstantsAdmin.selectedTitleFont.getBasename());
            face = Typeface.createFromFile(fileFont);
            titleTag.setTypeface(face);
        }



    }

    private void askForWriteStoragePermission() {
        
    }

    private void configureWidgets() {
        textWellcomeUsr = findViewById(R.id.textWellcomeUser);
        textWellcomeUsr.setText(getString(R.string.wellcomeUser) + " " + ConstantsAdmin.currentCustomer.getFirstName() + " " + ConstantsAdmin.currentCustomer.getLastName());
        agregarAlCarrito = findViewById(R.id.btnAgregarACarrito);
        comentarioUsr = findViewById(R.id.entryCommentTag);
        agregarAlCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarProductoAlCarrito();
            }
        });
    }


    private void initializeService() {
    }

}
