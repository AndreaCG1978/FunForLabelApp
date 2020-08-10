package com.boxico.android.kn.funforlabelapp;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.boxico.android.kn.funforlabelapp.dtos.Product;
import com.boxico.android.kn.funforlabelapp.dtos.ProductoCarrito;
import com.boxico.android.kn.funforlabelapp.dtos.ComboCarrito;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;
import com.boxico.android.kn.funforlabelapp.utils.TagParams;

import java.util.Iterator;

public class TagReadyComboToGoActivity extends AppCompatActivity {

    private TagReadyComboToGoActivity me;
    TextView textWellcomeUsr = null;
    Button agregarAlCarrito = null;
    EditText comentarioUsr = null;
    private LinearLayout linearTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;
        setContentView(R.layout.tag_ready_to_go);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.configureWidgets();
        this.initializeCreator();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void agregarProductoAlCarrito() {
        ProductoCarrito pc;
        ComboCarrito combo = new ComboCarrito();
        Product p;
        TagParams temp;
        Iterator<Product> it = ConstantsAdmin.currentComboProducts.iterator();
        while (it.hasNext()) {
            pc = new ProductoCarrito();
            p = it.next();
            temp = ConstantsAdmin.params.get(p.getId());
            if (temp.isTitle()) {
                pc.setAreaTitulo(temp.getLabelAttributes()[1]);
                pc.setFontTitleColor(temp.getColorTitle());
                pc.setTitulo(temp.getTitle());
                pc.setTitleFontName(temp.getFontTitleBaseName());
              //  if(temp.getFontSizeTitle()!= null){
                    pc.setTitleFontSize(Float.valueOf(temp.getFontSizeTitle()));
           //     }else{
            //        pc.setTitleFontSize(Float.valueOf("8.0"));
            //    }

                pc.setTieneTitulo(true);
                pc.setIdAreaTitulo((int) pc.getAreaTitulo().getTextAreasId());
                pc.setAnchoAreaTituto(pc.getAreaTitulo().getWidth());
                pc.setLargoAreaTituto(pc.getAreaTitulo().getHeight());
                pc.setEsMultilineaTituto(pc.getAreaTitulo().getMultiline());
                pc.setFromXTituto(pc.getAreaTitulo().getFromX());
                pc.setFromYTituto(pc.getAreaTitulo().getFromY());
           //     if(temp.getFontTitleId()!= 0){
                    pc.setFontTitleId(temp.getFontTitleId());
          //      }else{
          //          pc.setFontTitleId(3);//AVERIA
           //     }

            } else {
                pc.setTieneTitulo(false);
            }
            pc.setAreaTexto(temp.getLabelAttributes()[0]);
            pc.setAnchoAreaTexto(pc.getAreaTexto().getWidth());
            pc.setLargoAreaTexto(pc.getAreaTexto().getHeight());
            pc.setEsMultilineaTexto(pc.getAreaTexto().getMultiline());
            pc.setFromXTexto(pc.getAreaTexto().getFromX());
            pc.setFromYTexto(pc.getAreaTexto().getFromY());
            pc.setFontTextColor(temp.getColorText());
            pc.setTexto(temp.getText());
            pc.setTextFontName(temp.getFontTextBaseName());
            pc.setTextFontSize(Float.valueOf(temp.getFontSizeText()));
            pc.setIdAreaTexto((int) pc.getAreaTexto().getTextAreasId());
            pc.setFontTextId(temp.getFontTextId());
            pc.setCreador(temp.getCreator());
            pc.setBackgroundFilename(temp.getBackgroundFilename());
            pc.setBackground(temp.getImage().getImage());

            pc.setIdCreador((int) pc.getCreador().getId());
            pc.setRound(pc.getCreador().getRound());
            pc.setAnchoTag(pc.getCreador().getWidth());
            pc.setLargoTag(pc.getCreador().getHeight());
            pc.setNombre(pc.getCreador().getName());
            pc.setCantidad("1");
            pc.setCantidadPorPack(p.getQuantity());
            pc.setModelo(p.getModel());
            pc.setIdProduct((int) p.getId());
            pc.setFillsTexturedId((int) temp.getImage().getFillsTexturedId());
            ConstantsAdmin.copyBitmapInStorage(temp.getImage().getImage(), temp.getBackgroundFilename());
            combo.getProductos().add(pc);
        }
        combo.setPrecio(ConstantsAdmin.currentProduct.getPrice());
        combo.setCantidad(String.valueOf(1));
        combo.setComentarioUsr(comentarioUsr.getText().toString());
        combo.setNombre(ConstantsAdmin.currentProduct.getName());
        combo.setBackgroundFilename(ConstantsAdmin.currentProduct.getImageString());
        combo.setIdProduct((int)ConstantsAdmin.currentProduct.getId());
        combo.setFillsTexturedId((int)ConstantsAdmin.selectedImage.getFillsTexturedId());
        Bitmap bmp = ConstantsAdmin.takeScreenshot(linearTag);
        ConstantsAdmin.screenShot = bmp;
        combo.setImagenDeTag(ConstantsAdmin.getBytes(ConstantsAdmin.screenShot));

        ConstantsAdmin.agregarComboAlCarrito(combo);

            //  createAlertDialog(getString(R.string.tag_agregado_carrito), "");
        ConstantsAdmin.createComboCarrito(combo, this);
        ConstantsAdmin.copyBitmapInStorage(ConstantsAdmin.currentProduct.getImage(), combo.getBackgroundFilename());

        ConstantsAdmin.finalizarHastaMenuPrincipal = true;
        ConstantsAdmin.clearSelections();

        finish();




        /*

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
        pc.setCantidadPorPack(ConstantsAdmin.currentProduct.getQuantity());
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

        finish();*/

    }

    private void createAlertDialog(String message, String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(me);
        builder.setMessage(message).setTitle(title);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initializeCreator() {
     /*   DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;

        float screenWidthMM = ConstantsAdmin.pxToMm((float) width, this);
        boolean acotar = false;
        if(screenWidthMM < ConstantsAdmin.currentCreator.getWidth()){
            acotar = true;
        }*/
        linearTag = this.findViewById(R.id.linearReadyToGoTag);
        ImageView iv = null;
        LinearLayout.LayoutParams params = null;
        BitmapDrawable bd = null;
        Iterator<Product> it = ConstantsAdmin.currentComboProducts.iterator();
        Bitmap bm = null;
        Product p = null;
        TextView tv = null;
        while (it.hasNext()){
            tv = new TextView(this);
            iv = new ImageView(this);
            p = it.next();
            bm = p.getScreenShot();
            tv.setText(p.getName());
            tv.setTypeface(Typeface.SANS_SERIF);
            tv.setTextColor(Color.BLACK);
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(15,15,15,10);
            tv.setLayoutParams(params);
            linearTag.addView(tv);

            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(15,5,15,20);
            int srcWidth = bm.getWidth();
            int srcHeight = bm.getHeight();
            int dstWidth = (int)(srcWidth*0.77f);
            int dstHeight = (int)(srcHeight*0.77f);
            // Add image path from drawable folder.

            Bitmap b =Bitmap.createScaledBitmap(bm, dstWidth,dstHeight, false);
            bd = new BitmapDrawable(this.getResources(),b);
            iv.setBackground(bd);
            iv.setLayoutParams(params);

            linearTag.addView(iv);
        }

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



}
