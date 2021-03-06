package com.boxico.android.kn.funforlabelapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.boxico.android.kn.funforlabelapp.dtos.LabelAttributes;
import com.boxico.android.kn.funforlabelapp.dtos.LabelFont;
import com.boxico.android.kn.funforlabelapp.dtos.LabelImage;
import com.boxico.android.kn.funforlabelapp.dtos.Product;
import com.boxico.android.kn.funforlabelapp.services.CategoriesProductsService;
import com.boxico.android.kn.funforlabelapp.services.CreatorService;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;
import com.boxico.android.kn.funforlabelapp.utils.KNCustomBackgroundAdapter;
import com.boxico.android.kn.funforlabelapp.utils.KNCustomBackgroundProductAdapter;
import com.boxico.android.kn.funforlabelapp.utils.KNCustomFontSizeAdapter;
import com.boxico.android.kn.funforlabelapp.utils.KNCustomFontTypeAdapter;
import com.boxico.android.kn.funforlabelapp.utils.TagParams;
import com.boxico.android.kn.funforlabelapp.utils.workers.LoadAllComboWorker;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import petrov.kristiyan.colorpicker.ColorPicker;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//import static com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin.URL_LABEL_IMAGES;

public class TagComboCreatorActivity extends AppCompatActivity {

    private TagComboCreatorActivity me;
    TextView textWellcomeUsr = null;
    RelativeLayout linearTag = null;
    EditText textTag = null;
    EditText titleTag = null;

  //  private Creator currentCreator;
    private LabelImage[] images;
    private LabelFont[] fonts;
    private LabelAttributes[] labelAttributes;
    private Spinner spinnerFontSizes;
    private Spinner spinnerFonts;
    private Spinner spinnerFontsTitle;
    private Spinner spinnerBackgrounds;
    private Spinner spinnerProducts;
 //   private EditText entryTextTag;
    boolean acotar = false;
    private final int PERMISSIONS_WRITE_STORAGE = 101;
    private Button pickColor;
    private int selectedPosFontText = -1;
    private int selectedPosFontSizeText = -1;
    private int selectedTextColor = Color.BLACK;
    private int selectedPosFontTitle = -1;
    private int selectedPosFontSizeTitle = -1;
    private int selectedTitleColor = Color.BLACK;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;
        ConstantsAdmin.params = new ArrayMap<>();
        setContentView(R.layout.tag_combo_creator);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.initializeService();
        this.configureWidgets();
        this.askForWriteStoragePermission();

        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //   mCameraPermissionGranted = false;


        if (requestCode == PERMISSIONS_WRITE_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.loadCombos();
            }
        }


    }

    private void askForWriteStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_WRITE_STORAGE);


            } else {//Ya tiene el permiso...
                this.loadCombos();
            }
        } else {
            this.loadCombos();
        }


    }

    private void loadCombos() {
     //   new LoadCombosTask().execute();

        privateLoadProductCombo();
    }
/*
    private void loadCreator() {
        new LoadCreatorTask().execute();
    }*/
/*
    @Override
    public void colorChanged(int color) {

        textTag.setTextColor(color);
        pickColor.setTextColor(color);
    }
*/



/*
    private class LoadCreatorTask extends AsyncTask<Long, Integer, Integer> {


        private ProgressDialog dialog = null;

        @Override
        protected Integer doInBackground(Long... longs) {
            publishProgress(1);
            privateLoadCreator();
            return 0;
        }

        protected void onProgressUpdate(Integer... progress) {
            dialog = ProgressDialog.show(me, "",
                    getResources().getString(R.string.loading_data), true);
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(dialog != null) {
                dialog.cancel();
            }
       //     new LoadImagesTask().execute();
            new LoadAttributesTask().execute();
        }
    }

    private class LoadCombosTask extends AsyncTask<Long, Integer, Integer> {


        private ProgressDialog dialog = null;

        @Override
        protected Integer doInBackground(Long... longs) {
            publishProgress(1);
            privateLoadProductCombo();
            return 0;
        }

        protected void onProgressUpdate(Integer... progress) {
            dialog = ProgressDialog.show(me, "",
                    getResources().getString(R.string.loading_data), true);
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(dialog != null) {
                dialog.cancel();
            }
            new LoadImageForComboProductsTask().execute();

        }
    }
*/
/*
    private class LoadImageForComboProductsTask extends AsyncTask<Long, Integer, Integer> {


        private ProgressDialog dialog = null;

        @Override
        protected Integer doInBackground(Long... longs) {
            publishProgress(1);
            loadImagesForComboProducts();
            return 0;
        }

        protected void onProgressUpdate(Integer... progress) {
            dialog = ProgressDialog.show(me, "",
                    getResources().getString(R.string.loading_data), true);
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(dialog != null) {
                dialog.cancel();
            }
            spinnerProducts.setAdapter(new KNCustomBackgroundProductAdapter(getApplicationContext(), R.layout.spinner_item,R.id.rowValor, productsList));
            spinnerProducts.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    actualizarTagActual();
                    return false;
                }
            });
            spinnerProducts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(ConstantsAdmin.selectedComboProduct !=null){// SE GUARDA EL TEXT/TITLE DE LA ETIQUETA ANTERIOR
                        TagParams tp = ConstantsAdmin.params.get(ConstantsAdmin.selectedComboProduct.getId());
                        if(textTag != null && textTag.getText()!= null){
                            tp.setText(textTag.getText().toString());
                            if(!textTag.getText().toString().equals("")) {
                                ConstantsAdmin.selectedComboProduct.setChecked(true);
                            }
                        }

                        if(ConstantsAdmin.currentCreator != null && ConstantsAdmin.currentCreator.getTitle()==1 && titleTag != null && titleTag.getText()!= null){
                            tp.setTitle(titleTag.getText().toString());

                        }
                    }

                    ConstantsAdmin.selectedComboProduct = (Product) parent.getAdapter().getItem(position);
                    TagParams param = null;
                    if(!ConstantsAdmin.params.containsKey(ConstantsAdmin.selectedComboProduct.getId())){
                        param = new TagParams();
                        param.setIdProduct(ConstantsAdmin.selectedComboProduct.getId());
                        ConstantsAdmin.params.put(ConstantsAdmin.selectedComboProduct.getId(), param);
                    }else{
                        param = ConstantsAdmin.params.get(ConstantsAdmin.selectedComboProduct.getId());
                    }
                    param.setInicializadoFont(false);
                    param.setInicializadoSize(false);
                    loadCreator();


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }
    }
*/
    private void actualizarTagActual(){
        if(ConstantsAdmin.selectedComboProduct !=null) {// SE GUARDA EL TEXT/TITLE DE LA ETIQUETA ANTERIOR
            TagParams tp = ConstantsAdmin.params.get(ConstantsAdmin.selectedComboProduct.getId());
            if (textTag != null && textTag.getText() != null) {
                tp.setText(textTag.getText().toString());
                if (!textTag.getText().toString().equals("")) {
                    ConstantsAdmin.selectedComboProduct.setChecked(true);
                } else {
                    ConstantsAdmin.selectedComboProduct.setChecked(false);
                }
            }

            if (ConstantsAdmin.currentCreator != null && ConstantsAdmin.currentCreator.getTitle() == 1 && titleTag != null && titleTag.getText() != null) {
                tp.setTitle(titleTag.getText().toString());

             /*   if (!titleTag.getText().toString().equals("")) {
                    ConstantsAdmin.selectedComboProduct.setChecked(true);
                } else {
                    ConstantsAdmin.selectedComboProduct.setChecked(false);
                }*/

            }
            Bitmap bmp = takeScreenShot();
            tp.setPreview(bmp);

            ConstantsAdmin.selectedComboProduct.setScreenShot(bmp);
        }
    }
/*
    private void loadImagesForComboProducts() {
        Iterator<Product> it = ConstantsAdmin.productsList.iterator();
        Product p;
        String url;
        Bitmap b;
        try {
            while (it.hasNext()){
                p = it.next();
                url = ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_URL_IMAGES) + p.getImageString();
                b = ConstantsAdmin.getImageFromURL(url);
                p.setImage(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
    private void privateLoadProductCombo() {
        Call<List<Product>> call = null;
        Response<List<Product>> response;

        try {
            ConstantsAdmin.mensaje = null;
            
            call = ConstantsAdmin.productService.getProductsFromComboProduct(true, ConstantsAdmin.currentProduct.getId(), ConstantsAdmin.currentLanguage, ConstantsAdmin.tokenFFL);
            response = call.execute();
            if(response.body() != null){
                ConstantsAdmin.productsList = new ArrayList<>(response.body());
                if(ConstantsAdmin.productsList.size() == 0){
                    ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                }else{
                    ConstantsAdmin.selectedComboProduct = ConstantsAdmin.productsList.get(0);
                    TagParams param;
                    if(!ConstantsAdmin.params.containsKey(ConstantsAdmin.selectedComboProduct.getId())){
                        param = new TagParams();
                        param.setIdProduct(ConstantsAdmin.selectedComboProduct.getId());
                        ConstantsAdmin.params.put(ConstantsAdmin.selectedComboProduct.getId(), param);
                    }else{
                        param = ConstantsAdmin.params.get(ConstantsAdmin.selectedComboProduct.getId());
                    }
                    param.setInicializadoFont(false);
                    param.setInicializadoSize(false);
                    Iterator<Product> it = ConstantsAdmin.productsList.iterator();
                    Product p;
                    String url;
                    Bitmap b;
                    try {
                        while (it.hasNext()){
                            p = it.next();
                            url = ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_URL_IMAGES) + p.getImageString();
                            b = ConstantsAdmin.getImageFromURL(url);
                            p.setImage(b);
                        }
                        spinnerProducts.setAdapter(new KNCustomBackgroundProductAdapter(getApplicationContext(), R.layout.spinner_item,R.id.rowValor, ConstantsAdmin.productsList));

                  //      loadAll();

                        invokeLoadAllWorker();


                        /*



                        TagParams tp = ConstantsAdmin.params.get(ConstantsAdmin.selectedComboProduct.getId());
                        if(tp != null && tp.getCreator() != null){
                            ConstantsAdmin.currentCreator = tp.getCreator();
                        }else{
                            Call<Creator> call1 = null;
                            Response<Creator> response1;
                            try {
                                call1 = ConstantsAdmin.creatorService.getCreator(ConstantsAdmin.selectedComboProduct.getId(), true, ConstantsAdmin.tokenFFL);
                                response1 = call1.execute();
                                if(response1.body() != null){
                                    ConstantsAdmin.currentCreator = response1.body();
                                    if(tp != null){
                                        tp.setCreator(ConstantsAdmin.currentCreator);
                                    }
                                    if(ConstantsAdmin.currentCreator == null){
                                        ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                                    }
                                }else{
                                    ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                                }

                            }catch(Exception exc){
                                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                                if(call1 != null) {
                                    call1.cancel();
                                }
                            }

                        }

                        if(tp != null && tp.getLabelAttributes() != null){
                            labelAttributes = tp.getLabelAttributes();
                        }else{
                            Call<List<LabelAttributes>> call2 = null;
                            Response<List<LabelAttributes>> response2;
                            List<LabelAttributes> temp2;
                            try {
                                call2 = ConstantsAdmin.creatorService.getLabelAttributes(ConstantsAdmin.currentCreator.getId(), true,  ConstantsAdmin.tokenFFL);
                                response2 = call2.execute();
                                if(response2.body() != null){
                                    temp2 = new ArrayList<>(response2.body());
                                    labelAttributes = temp2.toArray(new LabelAttributes[temp2.size()]);
                                    if(tp != null){
                                        tp.setLabelAttributes(labelAttributes);
                                        if(labelAttributes.length > 1){
                                            tp.setTitle(true);
                                        }else{
                                            tp.setTitle(false);
                                        }
                                    }
                                    // labelAttributes = response.body();
                                }else{
                                    ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                                }
                            }catch(Exception exc){
                                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                                if(call2 != null) {
                                    call2.cancel();
                                }
                            }

                            boolean needLoadFontFile = true;
                            if(tp != null && tp.getFonts() != null){
                                fonts = tp.getFonts();
                                needLoadFontFile = false;
                            }else{
                                Call<List<LabelFont>> call3 = null;
                                Response<List<LabelFont>> response3;
                                List<LabelFont> temp3;

                                try {
                                    ConstantsAdmin.mensaje = null;
                                    call3 = ConstantsAdmin.creatorService.getFonts(labelAttributes[0].getTextAreasId(), true,  ConstantsAdmin.tokenFFL);
                                    response3 = call3.execute();
                                    if(response3.body() != null){
                                        temp3 = new ArrayList<>(response3.body());
                                        fonts = temp3.toArray(new LabelFont[temp3.size()]);
                                        if(tp != null){
                                            tp.setFonts(fonts);
                                        }

                                    }
                                }catch(Exception exc){
                                    ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                                    if(call3 != null) {
                                        call3.cancel();
                                    }
                                }

                            }
                            if(needLoadFontFile){
//                                new GetFontFilesTask().execute();
                                String temp, extension;
                                for (LabelFont lf: fonts) {
                                    extension = lf.getBasename().substring(lf.getBasename().length() - 4);
                                    temp = lf.getBasename().substring(0,lf.getBasename().length() - 4);
                                    temp = temp + "-Regular" + extension;
                                    lf.setBasename(temp);
                                    ConstantsAdmin.copyFileFromUrl(ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_URL_FONTS) + temp, temp);
                                }

                            }
                            //new LoadImagesTask().execute();
                            if(tp != null && tp.getImages() != null){
                                images = tp.getImages();
                            }else{
                                Call<List<LabelImage>> call4 = null;
                                Response<List<LabelImage>> response4;
                                ArrayList temp4;
                                try {
                                    call4 = ConstantsAdmin.creatorService.getImages(ConstantsAdmin.currentCreator.getId(), true,  ConstantsAdmin.tokenFFL);
                                    response4 = call4.execute();
                                    if(response4.body() != null){
                                        temp4 = new ArrayList<>(response4.body());
                                        if(temp4.size() == 0){
                                            ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                                        }else{
                                            images = (LabelImage[]) temp4.toArray(new LabelImage[temp4.size()]);
                                            if(tp != null){
                                                tp.setImages(images);
                                            }
                                        }
                                    }else{
                                        ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                                    }
                                }catch(Exception exc){
                                    ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                                    if(call4 != null) {
                                        call4.cancel();
                                    }
                                }
                            }
                            String url1;
                            Bitmap b1;
                            for (LabelImage li: images) {
                                if(li.getImage() == null){
                                    url1 = ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_URL_LABEL_IMAGES) + li.getUniquename();
                                    b1 = ConstantsAdmin.getImageFromURL(url1);
                                    li.setImage(b1);
                                }

                            }
                            initializeCreator();

                        }*/
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }else{
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
            }
        }catch(Exception exc){
            ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
            if(call != null) {
                call.cancel();
            }

        }
    }

    private void invokeLoadAllWorker() {
        // CREO EL PROGRESS BAR

        LinearLayout layout = findViewById(R.id.tagComboCreatorLayout);
        final ProgressBar progressBar = new ProgressBar(TagComboCreatorActivity.this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, 4,params);

        final TextView txt = new TextView(this);
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txt.setBackgroundColor(Color.TRANSPARENT);
        txt.setText(getString(R.string.loading_data));
        txt.setGravity(Gravity.CENTER);
        txt.setVisibility(View.GONE);
        layout.addView(txt, 4,params);

        Data inputData = new Data.Builder().build();
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(LoadAllComboWorker.class)
                .setInputData(inputData)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.RUNNING) {
                            progressBar.setVisibility(View.VISIBLE);
                            txt.setVisibility(View.VISIBLE);
                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            txt.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            cargarCreador();
                            spinnerProducts.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    actualizarTagActual();
                                    return false;
                                }
                            });
                            spinnerProducts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if(ConstantsAdmin.selectedComboProduct !=null){// SE GUARDA EL TEXT/TITLE DE LA ETIQUETA ANTERIOR
                                        TagParams tp = ConstantsAdmin.params.get(ConstantsAdmin.selectedComboProduct.getId());
                                        if(textTag != null && textTag.getText()!= null){
                                            tp.setText(textTag.getText().toString());
                                            if(!textTag.getText().toString().equals("")) {
                                                ConstantsAdmin.selectedComboProduct.setChecked(true);
                                            }
                                        }

                                        if(ConstantsAdmin.currentCreator != null && ConstantsAdmin.currentCreator.getTitle()==1 && titleTag != null && titleTag.getText()!= null){
                                            tp.setTitle(titleTag.getText().toString());
                                        }
                                    }
                                    ConstantsAdmin.selectedComboProduct = (Product) parent.getAdapter().getItem(position);
                                    TagParams param;
                                    if(!ConstantsAdmin.params.containsKey(ConstantsAdmin.selectedComboProduct.getId())){
                                        param = new TagParams();
                                        param.setIdProduct(ConstantsAdmin.selectedComboProduct.getId());
                                        ConstantsAdmin.params.put(ConstantsAdmin.selectedComboProduct.getId(), param);
                                    }else{
                                        param = ConstantsAdmin.params.get(ConstantsAdmin.selectedComboProduct.getId());
                                    }
                                    param.setInicializadoFont(false);
                                    param.setInicializadoSize(false);
                                    cargarCreador();

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }
                    }
                });
        ListenableFuture<Operation.State.SUCCESS> obj = WorkManager.getInstance(this).enqueue(request).getResult();
    }
/*

    private void loadAll() throws IOException {
        Iterator<Product> it = ConstantsAdmin.productsList.iterator();
        Product p = null;
        TagParams tp = null;


        while(it.hasNext()){
            p = it.next();
            tp = new TagParams();

            // Busco el CREATOR
            Call<Creator> call1 = null;
            Response<Creator> response1;
            try {
                call1 = ConstantsAdmin.creatorService.getCreator(p.getId(), true, ConstantsAdmin.tokenFFL);
                response1 = call1.execute();
                if (response1.body() != null) {
                //    ConstantsAdmin.currentCreator = response1.body();
                    if (tp != null) {
                        tp.setCreator(response1.body());
                    }
                    if (response1.body() == null) {
                        ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                    }
                } else {
                    ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                }
            } catch (Exception exc) {
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                if (call1 != null) {
                    call1.cancel();
                }
            }
            // CARGO LABELATTRIB

            Call<List<LabelAttributes>> call2 = null;
            Response<List<LabelAttributes>> response2;
            List<LabelAttributes> temp2;
            LabelAttributes[] labelAttrs = null;
            try {
                call2 = ConstantsAdmin.creatorService.getLabelAttributes(tp.getCreator().getId(), true, ConstantsAdmin.tokenFFL);
                response2 = call2.execute();
                if (response2.body() != null) {
                    temp2 = new ArrayList<>(response2.body());
                    labelAttrs = temp2.toArray(new LabelAttributes[temp2.size()]);
                    if (tp != null) {
                        tp.setLabelAttributes(labelAttrs);
                        if (labelAttrs.length > 1) {
                            tp.setTitle(true);
                        } else {
                            tp.setTitle(false);
                        }
                    }
                    // labelAttributes = response.body();
                } else {
                    ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                }
            } catch (Exception exc) {
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                if (call2 != null) {
                    call2.cancel();
                }
            }
            boolean needLoadFontFile = true;
            LabelFont[] fs = null;
            if (tp != null && tp.getFonts() != null) {
              //  fonts = tp.getFonts();
                needLoadFontFile = false;
            } else {
                Call<List<LabelFont>> call3 = null;
                Response<List<LabelFont>> response3;
                List<LabelFont> temp3;

                try {
                    ConstantsAdmin.mensaje = null;
                    call3 = ConstantsAdmin.creatorService.getFonts(labelAttrs[0].getTextAreasId(), true, ConstantsAdmin.tokenFFL);
                    response3 = call3.execute();
                    if (response3.body() != null) {
                        temp3 = new ArrayList<>(response3.body());
                        fs = temp3.toArray(new LabelFont[temp3.size()]);
                        if (tp != null) {
                            tp.setFonts(fs);
                        }

                    }
                } catch (Exception exc) {
                    ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                    if (call3 != null) {
                        call3.cancel();
                    }
                }

            }
            if (needLoadFontFile) {
//                                new GetFontFilesTask().execute();
                String temp, extension;
                for (LabelFont lf : fs) {
                    extension = lf.getBasename().substring(lf.getBasename().length() - 4);
                    temp = lf.getBasename().substring(0, lf.getBasename().length() - 4);
                    temp = temp + "-Regular" + extension;
                    lf.setBasename(temp);
                    ConstantsAdmin.copyFileFromUrl(ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_URL_FONTS) + temp, temp);
                }

            }
            Call<List<LabelImage>> call4 = null;
            Response<List<LabelImage>> response4;
            ArrayList temp4;
            LabelImage[] ims = null;
            try {
                call4 = ConstantsAdmin.creatorService.getImages(tp.getCreator().getId(), true, ConstantsAdmin.tokenFFL);
                response4 = call4.execute();
                if (response4.body() != null) {
                    temp4 = new ArrayList<>(response4.body());
                    if (temp4.size() == 0) {
                        ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                    } else {
                        ims = (LabelImage[]) temp4.toArray(new LabelImage[temp4.size()]);
                        if (tp != null) {
                            tp.setImages(ims);
                        }
                    }
                } else {
                    ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                }
            } catch (Exception exc) {
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                if (call4 != null) {
                    call4.cancel();
                }
            }
            String url1;
            Bitmap b1;
            for (LabelImage li : ims) {
                if (li.getImage() == null) {
                    url1 = ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_URL_LABEL_IMAGES) + li.getUniquename();
                    b1 = ConstantsAdmin.getImageFromURL(url1);
                    li.setImage(b1);
                }

            }

            ConstantsAdmin.params.put(p.getId(), tp);


        }
        cargarCreador();

    }
*/
    private void cargarCreador() {
        TagParams tp = ConstantsAdmin.params.get(ConstantsAdmin.selectedComboProduct.getId());


   //     if (tp != null && tp.getCreator() != null) {
            ConstantsAdmin.currentCreator = tp.getCreator();
   /*     } else {
            Call<Creator> call1 = null;
            Response<Creator> response1;
            try {
                call1 = ConstantsAdmin.creatorService.getCreator(ConstantsAdmin.selectedComboProduct.getId(), true, ConstantsAdmin.tokenFFL);
                response1 = call1.execute();
                if (response1.body() != null) {
                    ConstantsAdmin.currentCreator = response1.body();
                    if (tp != null) {
                        tp.setCreator(ConstantsAdmin.currentCreator);
                    }
                    if (ConstantsAdmin.currentCreator == null) {
                        ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                    }
                } else {
                    ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                }

            } catch (Exception exc) {
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                if (call1 != null) {
                    call1.cancel();
                }
            }

        }*/
   //     if (tp != null && tp.getFonts() != null) {
            fonts = tp.getFonts();
  //      }
    //    if (tp != null && tp.getLabelAttributes() != null) {
            labelAttributes = tp.getLabelAttributes();
   /*     } else {
            Call<List<LabelAttributes>> call2 = null;
            Response<List<LabelAttributes>> response2;
            List<LabelAttributes> temp2;
            try {
                call2 = ConstantsAdmin.creatorService.getLabelAttributes(ConstantsAdmin.currentCreator.getId(), true, ConstantsAdmin.tokenFFL);
                response2 = call2.execute();
                if (response2.body() != null) {
                    temp2 = new ArrayList<>(response2.body());
                    labelAttributes = temp2.toArray(new LabelAttributes[temp2.size()]);
                    if (tp != null) {
                        tp.setLabelAttributes(labelAttributes);
                        if (labelAttributes.length > 1) {
                            tp.setTitle(true);
                        } else {
                            tp.setTitle(false);
                        }
                    }
                    // labelAttributes = response.body();
                } else {
                    ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                }
            } catch (Exception exc) {
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                if (call2 != null) {
                    call2.cancel();
                }
            }

            boolean needLoadFontFile = true;
            if (tp != null && tp.getFonts() != null) {
                fonts = tp.getFonts();
                needLoadFontFile = false;
            } else {
                Call<List<LabelFont>> call3 = null;
                Response<List<LabelFont>> response3;
                List<LabelFont> temp3;

                try {
                    ConstantsAdmin.mensaje = null;
                    call3 = ConstantsAdmin.creatorService.getFonts(labelAttributes[0].getTextAreasId(), true, ConstantsAdmin.tokenFFL);
                    response3 = call3.execute();
                    if (response3.body() != null) {
                        temp3 = new ArrayList<>(response3.body());
                        fonts = temp3.toArray(new LabelFont[temp3.size()]);
                        if (tp != null) {
                            tp.setFonts(fonts);
                        }

                    }
                } catch (Exception exc) {
                    ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                    if (call3 != null) {
                        call3.cancel();
                    }
                }

            }
            if (needLoadFontFile) {
//                                new GetFontFilesTask().execute();
                String temp, extension;
                for (LabelFont lf : fonts) {
                    extension = lf.getBasename().substring(lf.getBasename().length() - 4);
                    temp = lf.getBasename().substring(0, lf.getBasename().length() - 4);
                    temp = temp + "-Regular" + extension;
                    lf.setBasename(temp);
                    ConstantsAdmin.copyFileFromUrl(ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_URL_FONTS) + temp, temp);
                }

            }
        }*/
            //new LoadImagesTask().execute();
    //    if (tp != null && tp.getImages() != null) {
            images = tp.getImages();
    /*    } else {
            Call<List<LabelImage>> call4 = null;
            Response<List<LabelImage>> response4;
            ArrayList temp4;
            try {
                call4 = ConstantsAdmin.creatorService.getImages(ConstantsAdmin.currentCreator.getId(), true, ConstantsAdmin.tokenFFL);
                response4 = call4.execute();
                if (response4.body() != null) {
                    temp4 = new ArrayList<>(response4.body());
                    if (temp4.size() == 0) {
                        ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                    } else {
                        images = (LabelImage[]) temp4.toArray(new LabelImage[temp4.size()]);
                        if (tp != null) {
                            tp.setImages(images);
                        }
                    }
                } else {
                    ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                }
            } catch (Exception exc) {
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                if (call4 != null) {
                    call4.cancel();
                }
            }
            String url1;
            Bitmap b1;
            for (LabelImage li : images) {
                if (li.getImage() == null) {
                    url1 = ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_URL_LABEL_IMAGES) + li.getUniquename();
                    b1 = ConstantsAdmin.getImageFromURL(url1);
                    li.setImage(b1);
                }

            }
        }*/
        initializeCreator();
    }

/*
    private class LoadImagesTask extends AsyncTask<Long, Integer, Integer> {


        private ProgressDialog dialog = null;

        @Override
        protected Integer doInBackground(Long... longs) {
            publishProgress(1);
            privateLoadImages();
            return 0;
        }

        protected void onProgressUpdate(Integer... progress) {
            dialog = ProgressDialog.show(me, "",
                    getResources().getString(R.string.loading_data), true);
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            try {
                loadImagesForCreator();
                initializeCreator();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(dialog != null) {
                dialog.cancel();
            }
        }
    }
*//*
    private class LoadAttributesTask extends AsyncTask<Long, Integer, Integer> {


        private ProgressDialog dialog = null;

        @Override
        protected Integer doInBackground(Long... longs) {
            publishProgress(1);
            privateLoadAttributes();
            return 0;
        }

        protected void onProgressUpdate(Integer... progress) {
            dialog = ProgressDialog.show(me, "",
                    getResources().getString(R.string.loading_data), true);
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if(dialog != null) {
                dialog.cancel();
            }
            new LoadFontsTask().execute();
        }
    }

*/

 /*   public Bitmap getRoundedCornerBitmap(Bitmap bitmap,int roundPixelSize) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = roundPixelSize;
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF,roundPx,roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
*/
/*
    private EditText createTextArea(EditText ta, LabelAttributes la, String hint, Creator currentC, boolean acot, RelativeLayout rl) {
        //EditText ta = new EditText(this);
        ta.setHint(hint);
        ta.setHintTextColor(Color.GRAY);
        float temp = 0;
        int w, h = 0;
        if (currentC.getId() != ConstantsAdmin.ID_CREATOR_MINICIRCULARES) {
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, la.getWidth(),
                    getResources().getDisplayMetrics());
            if (acot) {
                temp = temp - temp * 3 / 18;
            }
            w = (int) (temp);
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, la.getHeight() + 1,
                    getResources().getDisplayMetrics());
            if (acot) {
                temp = temp - temp * 3 / 19;
            }
            h = (int) (temp);
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, la.getFromY(),
                    getResources().getDisplayMetrics());
            if (acot) {
                temp = temp - temp * 3 / 19;
            }
            ta.setY(temp);


            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, la.getFromX(),
                    getResources().getDisplayMetrics());
            if (acot) {
                temp = temp - temp * 3 / 22;
            }
            ta.setX(temp);
        }else{
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, la.getWidth() ,
                    getResources().getDisplayMetrics());
            temp = temp * ConstantsAdmin.PARAM_TO_INCREASE;

            w = (int) (temp);
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, la.getHeight(),
                    getResources().getDisplayMetrics());
            temp = temp * ConstantsAdmin.PARAM_TO_INCREASE;
            h = (int) (temp);
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, la.getFromY(),
                    getResources().getDisplayMetrics());
            temp = temp * ConstantsAdmin.PARAM_TO_INCREASE;
            ta.setY(temp);

            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, la.getFromX(),
                    getResources().getDisplayMetrics());

            temp = temp * ConstantsAdmin.PARAM_TO_INCREASE;

            ta.setX(temp);
        }
        ViewGroup.LayoutParams layoutParamsTextTag = new ViewGroup.LayoutParams(w, h);




        //ta.setLeft(fromX);

        ta.setLayoutParams(layoutParamsTextTag);


        ta.setGravity(Gravity.CENTER);
        ta.setPadding(0, 0, 0, 0);
        ta.setBackgroundColor(Color.TRANSPARENT);
        ta.setTextColor(Color.BLACK);
        ta.setEllipsize(TextUtils.TruncateAt.END);


        if (la.getMultiline() == 0) {
            ta.setSingleLine(true);
            ta.setEllipsize(TextUtils.TruncateAt.END);
            ta.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            ta.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ta.setSingleLine(false);
        }
        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.TRANSPARENT); //white background
        border.setStroke(3, Color.RED); //black border with full opacity

    //    ta.setBackground(border);

        rl.addView(ta);
        return ta;

    }
*/
/*
    private void customizeBackground(Bitmap img, Creator currentC, boolean acot, RelativeLayout rl) {
        int realWidthImage = 0;
        int realHeightImage = 0;
        if (currentC.getId() != ConstantsAdmin.ID_CREATOR_MINICIRCULARES) {// NO ES EL CREADOR DE MINI-CIRCULARES
            float temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, currentC.getWidth(),
                    getResources().getDisplayMetrics());
            if (acot) {
                temp = temp - temp * 3 / 19;
            }
            realWidthImage = (int) temp;
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, currentC.getHeight(),
                    getResources().getDisplayMetrics());

            if (acot) {
                temp = temp - temp * 3 / 19;
            }
            realHeightImage = (int) temp;
        } else {
            float temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, currentC.getWidth(),
                    getResources().getDisplayMetrics());

            temp = temp * ConstantsAdmin.PARAM_TO_INCREASE;

            realWidthImage = (int) temp;
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, currentC.getHeight(),
                    getResources().getDisplayMetrics());
            temp = temp * ConstantsAdmin.PARAM_TO_INCREASE;
            realHeightImage = (int) temp;
        }
        Bitmap b = Bitmap.createScaledBitmap(img, realWidthImage, realHeightImage, false);
        if (currentC.getRounded() == 1) {
            b = getRoundedCornerBitmap(b, currentC.getRound());
        }

        Drawable d = new BitmapDrawable(getResources(), b);
        rl.setBackground(d);

    }
*/

    private void initializeCreator() {

        LabelAttributes la1, la2 = null;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        la1 = labelAttributes[0];
        if(labelAttributes.length > 1){
            la2 = labelAttributes[1];
        }
        //int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        float screenWidthMM = ConstantsAdmin.pxToMm((float) width, this);
        acotar = (screenWidthMM - 2.0f) <= (float) ConstantsAdmin.currentCreator.getWidth();
        Bitmap firstBitmap = images[0].getImage();
        linearTag.removeAllViews();

            ConstantsAdmin.customizeBackground(firstBitmap, ConstantsAdmin.currentCreator, acotar, linearTag, this);


            // CONFIGURACION DE UN AREA DE TEXTO
            textTag = null;
            titleTag = null;
            if (la1.getIsTitle() == 0) {
                textTag = ConstantsAdmin.createTextArea(new EditText(this), la1, this.getString(R.string.your_name_here), acotar, linearTag, me);
            } else {
                titleTag = ConstantsAdmin.createTextArea(new EditText(this), la1, this.getString(R.string.your_title), acotar, linearTag, me);
            }
            if (la2 != null) {
                if (la2.getIsTitle() == 0) {
                    textTag = ConstantsAdmin.createTextArea(new EditText(this), la2, this.getString(R.string.your_name_here), acotar, linearTag, me);
                } else {
                    titleTag = ConstantsAdmin.createTextArea(new EditText(this), la2, this.getString(R.string.your_title), acotar, linearTag, me);
                }
            }


            //CONFIGURACION DE LOS SPINNERS
            final boolean needToAcot = acotar;
            spinnerFontSizes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String fontSize;
                    fontSize = (String) parent.getAdapter().getItem(position);
                    float size = Float.parseFloat(fontSize);
                    float originalSize = size;
                    if (needToAcot) {
                        size = size * ((float) 0.82);
                    } else {
                        size = size * ((float) 1.00);
                    }

                    TagParams tp = ConstantsAdmin.params.get(ConstantsAdmin.selectedComboProduct.getId());
                    if (tp.isInicializadoSize()) {
                        textTag.setTextSize(TypedValue.TYPE_STRING, size);
                        selectedPosFontSizeText = position;
                        tp.setFontSizeText(String.valueOf(originalSize));
                        tp.setPosSizeText(position);
                        if (titleTag != null) {
                            selectedPosFontSizeTitle = position;
                            titleTag.setTextSize(TypedValue.TYPE_STRING, size);
                            tp.setFontSizeTitle(String.valueOf(originalSize));
                            tp.setPosSizeTitle(position);
                        }
                        tp.setInicializadoSize(true);
                    } else {
                        if (textTag.hasFocus()) {
                            selectedPosFontSizeText = position;
                            textTag.setTextSize(TypedValue.TYPE_STRING, size);
                            tp.setFontSizeText(String.valueOf(originalSize));
                            tp.setPosSizeText(position);
                        } else if (titleTag != null && titleTag.hasFocus()) {
                            selectedPosFontSizeTitle = position;
                            titleTag.setTextSize(TypedValue.TYPE_STRING, size);
                            tp.setFontSizeTitle(String.valueOf(originalSize));
                            tp.setPosSizeTitle(position);
                        }
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            spinnerFontSizes.setSelection(1);


            spinnerFonts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    LabelFont lf = (LabelFont) parent.getAdapter().getItem(position);
                    File fileFont = ConstantsAdmin.getFile(lf.getBasename());
                    Typeface face = Typeface.createFromFile(fileFont);
                    TagParams tp = ConstantsAdmin.params.get(ConstantsAdmin.selectedComboProduct.getId());
                    if (!tp.isInicializadoFont()) {
                        textTag.setTypeface(face);
                        tp.setFontText(face);
                        tp.setFontTextBaseName(lf.getBasename());
                        tp.setFontTextId((int) lf.getId());
                        tp.setPosFontText(position);
                        selectedPosFontText = position;
                  /*  if(titleTag != null){
                        titleTag.setTypeface(face);
                        selectedPosFontTitle = position;
                        tp.setFontTitle(face);
                        tp.setFontTitleBaseName(lf.getBasename());
                        tp.setFontTitleId((int)lf.getId());
                        tp.setPosFontTitle(position);
                    }*/
                        tp.setInicializadoFont(true);
                    } else {
                        if (textTag.hasFocus()) {
                            selectedPosFontText = position;
                            textTag.setTypeface(face);
                            tp.setFontText(face);
                            tp.setFontTextBaseName(lf.getBasename());
                            tp.setFontTextId((int) lf.getId());
                            tp.setPosFontText(position);
                        }/* else if (titleTag != null && titleTag.hasFocus()) {
                        selectedPosFontTitle = position;
                        titleTag.setTypeface(face);
                        tp.setFontTitle(face);
                        tp.setFontTitleId((int)lf.getId());
                        tp.setPosFontTitle(position);
                    }*/
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            if (titleTag != null) {
                spinnerFontsTitle = this.findViewById(R.id.spinnerFontsTitle);
                spinnerFontsTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        LabelFont lf = (LabelFont) parent.getAdapter().getItem(position);
                        File fileFont = ConstantsAdmin.getFile(lf.getBasename());
                        Typeface face = Typeface.createFromFile(fileFont);
                        TagParams tp = ConstantsAdmin.params.get(ConstantsAdmin.selectedComboProduct.getId());
                        if (!tp.isInicializadoFont()) {
                            titleTag.setTypeface(face);
                            selectedPosFontTitle = position;
                            tp.setFontTitle(face);
                            tp.setFontTitleBaseName(lf.getBasename());
                            tp.setFontTitleId((int) lf.getId());
                            tp.setPosFontTitle(position);
                            tp.setInicializadoFont(true);
                        } else {
                            if (titleTag.hasFocus()) {
                                selectedPosFontTitle = position;
                                titleTag.setTypeface(face);
                                tp.setFontTitle(face);
                                tp.setFontTitleBaseName(lf.getBasename());
                                tp.setFontTitleId((int) lf.getId());
                                tp.setPosFontTitle(position);
                            }
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                spinnerFontsTitle.setAdapter(new KNCustomFontTypeAdapter(this.getApplicationContext(), R.layout.spinner_simple_item, R.id.rowValor, fonts));

            } else {
                spinnerFontsTitle = null;
            }

            spinnerBackgrounds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ConstantsAdmin.selectedImage = (LabelImage) parent.getAdapter().getItem(position);
                    TagParams tp = ConstantsAdmin.params.get(ConstantsAdmin.selectedComboProduct.getId());
                    tp.setImage(ConstantsAdmin.selectedImage);
                    tp.setBackgroundFilename(ConstantsAdmin.selectedImage.getUniquename());
                    tp.setPosImage(position);
                    ConstantsAdmin.customizeBackground(ConstantsAdmin.selectedImage.getImage(), ConstantsAdmin.currentCreator, acotar, linearTag, me);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            spinnerFonts.setAdapter(new KNCustomFontTypeAdapter(this.getApplicationContext(), R.layout.spinner_simple_item, R.id.rowValor, fonts));
            spinnerFontSizes.setAdapter(new KNCustomFontSizeAdapter(this.getApplicationContext(), R.layout.spinner_simple_item, R.id.rowValor, ConstantsAdmin.FONT_SIZES));
            spinnerBackgrounds.setAdapter(new KNCustomBackgroundAdapter(this.getApplicationContext(), R.layout.spinner_simple_item, R.id.rowValor, images));
            // spinnerProducts.setAdapter(new KNCustomBackgroundAdapter(this.getApplicationContext(), R.layout.spinner_item,R.id.rowValor, images));

            textTag.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        spinnerFonts.setVisibility(View.VISIBLE);
                        if (spinnerFontsTitle != null) {
                            spinnerFontsTitle.setVisibility(View.GONE);
                        }
                        spinnerFonts.setSelection(selectedPosFontText);
                        spinnerFontSizes.setSelection(selectedPosFontSizeText);
                        pickColor.setTextColor(selectedTextColor);
                    }
                }
            });

            if (titleTag != null) {
                titleTag.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            spinnerFontsTitle.setVisibility(View.VISIBLE);
                            spinnerFonts.setVisibility(View.GONE);
                            spinnerFontsTitle.setSelection(selectedPosFontTitle);
                            spinnerFontSizes.setSelection(selectedPosFontSizeTitle);
                            pickColor.setTextColor(selectedTitleColor);
                        }
                    }
                });
            }

        if(ConstantsAdmin.selectedComboProduct!= null && ConstantsAdmin.params.containsKey(ConstantsAdmin.selectedComboProduct.getId())){
            TagParams param = ConstantsAdmin.params.get(ConstantsAdmin.selectedComboProduct.getId());
            spinnerBackgrounds.setSelection(param.getPosImage());
            selectedPosFontText = param.getPosFontText();
            spinnerFonts.setSelection(param.getPosFontText());
            selectedPosFontSizeText = param.getPosSizeText();
            spinnerFontSizes.setSelection(param.getPosSizeText());
            textTag.requestFocus();
            if(titleTag != null){
                titleTag.setTextSize(TypedValue.TYPE_STRING, Float.parseFloat(param.getFontSizeTitle()));

            }

            textTag.setText(param.getText());
            textTag.setTextColor(param.getColorText());
            pickColor.setTextColor(param.getColorText());
            if(param.getText()!= null && !param.getText().equals("")){
                ConstantsAdmin.selectedComboProduct.setChecked(true);
            }else{
                ConstantsAdmin.selectedComboProduct.setChecked(false);
            }
            if(param.isTitle()){
                selectedPosFontTitle = param.getPosFontTitle();
                selectedPosFontSizeTitle = param.getPosSizeTitle();
                titleTag.setText(param.getTitle());
                titleTag.setTextColor(param.getColorTitle());

            }
            //pickColor.setTextColor(param.getColorText());
          /*  if(param.getText()!= null && !param.getText().toString().equals("")){
                ConstantsAdmin.selectedComboProduct.setChecked(true);
            }else{
                ConstantsAdmin.selectedComboProduct.setChecked(false);
            }*/


        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }
/*
    private void loadImagesForCreator() throws IOException {
        String url;
        Bitmap b;
        for (LabelImage li: images) {
            if(li.getImage() == null){
                url = ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_URL_LABEL_IMAGES) + li.getUniquename();
                b = ConstantsAdmin.getImageFromURL(url);
                li.setImage(b);
            }

        }


    }*/
/*
    private class LoadFontsTask extends AsyncTask<Long, Integer, Integer> {


        private ProgressDialog dialog = null;
        private boolean needLoadFontFile = false;

        @Override
        protected Integer doInBackground(Long... longs) {
            publishProgress(1);
            needLoadFontFile = privateLoadFonts(labelAttributes[0].getTextAreasId());
            return 0;
        }

        protected void onProgressUpdate(Integer... progress) {
            dialog = ProgressDialog.show(me, "",
                    getResources().getString(R.string.loading_data), true);
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if(dialog != null) {
                dialog.cancel();
            }
            //new LoadImagesTask().execute();
            if(needLoadFontFile){
                new GetFontFilesTask().execute();
            }else{
                new LoadImagesTask().execute();
            }

        }
    }*/
/*
    private class GetFontFilesTask extends AsyncTask<Long, Integer, Integer> {


        private ProgressDialog dialog = null;

        @Override
        protected Integer doInBackground(Long... longs) {
            publishProgress(1);
            privateGetFontFiles();
            return 0;
        }

        protected void onProgressUpdate(Integer... progress) {
            dialog = ProgressDialog.show(me, "",
                    getResources().getString(R.string.loading_data), true);
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if(dialog != null) {
                dialog.cancel();
            }
            new LoadImagesTask().execute();
        }
    }
*//*
    private void privateGetFontFiles() {

        String temp, extension;

        for (LabelFont lf: fonts) {
            extension = lf.getBasename().substring(lf.getBasename().length() - 4);
            temp = lf.getBasename().substring(0,lf.getBasename().length() - 4);
            temp = temp + "-Regular" + extension;
            lf.setBasename(temp);
            ConstantsAdmin.copyFileFromUrl(ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_URL_FONTS) + temp, temp);
        }
    }
*/

    @Override
    protected void onStart() {
        super.onStart();
        if(ConstantsAdmin.finalizarHastaMenuPrincipal){
            finish();
        }

    }

/*
    private void privateLoadCreator() {
        TagParams tp = ConstantsAdmin.params.get(ConstantsAdmin.selectedComboProduct.getId());
        if(tp != null && tp.getCreator() != null){
            ConstantsAdmin.currentCreator = tp.getCreator();
        }else{
            Call<Creator> call = null;
            Response<Creator> response;

            try {
                ConstantsAdmin.mensaje = null;
                call = ConstantsAdmin.creatorService.getCreator(ConstantsAdmin.selectedComboProduct.getId(), true, ConstantsAdmin.tokenFFL);
                response = call.execute();
                if(response.body() != null){
                    ConstantsAdmin.currentCreator = response.body();
                    if(tp != null){
                        tp.setCreator(ConstantsAdmin.currentCreator);
                    }
                    if(ConstantsAdmin.currentCreator == null){
                        ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                    }
                }else{
                    ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                }

            }catch(Exception exc){
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                if(call != null) {
                    call.cancel();
                }
            }

        }
    }
*/

/*
    private void privateLoadImages() {
        TagParams tp = ConstantsAdmin.params.get(ConstantsAdmin.selectedComboProduct.getId());
        if(tp != null && tp.getImages() != null){
            images = tp.getImages();
        }else{
            Call<List<LabelImage>> call = null;
            Response<List<LabelImage>> response;
            ArrayList temp;

            try {
                ConstantsAdmin.mensaje = null;
                call = ConstantsAdmin.creatorService.getImages(ConstantsAdmin.currentCreator.getId(), true,  ConstantsAdmin.tokenFFL);
                response = call.execute();
                if(response.body() != null){
                    temp = new ArrayList<>(response.body());
                    if(temp.size() == 0){
                        ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                    }else{
                        images = (LabelImage[]) temp.toArray(new LabelImage[temp.size()]);
                        if(tp != null){
                            tp.setImages(images);
                        }
                    }
                }else{
                    ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                }
            }catch(Exception exc){
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                if(call != null) {
                    call.cancel();
                }
            }
        }
    }
*/

/*
    private boolean privateLoadFonts(long textAreasId) {
        TagParams tp = ConstantsAdmin.params.get(ConstantsAdmin.selectedComboProduct.getId());
        boolean needLoadFontFile = true;
        if(tp != null && tp.getFonts() != null){
            fonts = tp.getFonts();
            needLoadFontFile = false;
        }else{
            Call<List<LabelFont>> call = null;
            Response<List<LabelFont>> response;
            List<LabelFont> temp;

            try {
                ConstantsAdmin.mensaje = null;
                call = ConstantsAdmin.creatorService.getFonts(textAreasId, true,  ConstantsAdmin.tokenFFL);
                response = call.execute();
                if(response.body() != null){
                    temp = new ArrayList<>(response.body());
                    fonts = temp.toArray(new LabelFont[temp.size()]);
                    if(tp != null){
                        tp.setFonts(fonts);
                    }

                }
            }catch(Exception exc){
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                if(call != null) {
                    call.cancel();
                }
            }

        }
        return needLoadFontFile;
    }*/
/*
    private void privateLoadAttributes() {
        TagParams tp = ConstantsAdmin.params.get(ConstantsAdmin.selectedComboProduct.getId());
        if(tp != null && tp.getLabelAttributes() != null){
            labelAttributes = tp.getLabelAttributes();
        }else{
            Call<List<LabelAttributes>> call = null;
            Response<List<LabelAttributes>> response;
            List<LabelAttributes> temp;
            try {
                ConstantsAdmin.mensaje = null;
                call = ConstantsAdmin.creatorService.getLabelAttributes(ConstantsAdmin.currentCreator.getId(), true,  ConstantsAdmin.tokenFFL);
                response = call.execute();
                if(response.body() != null){
                    temp = new ArrayList<>(response.body());
                    labelAttributes = temp.toArray(new LabelAttributes[temp.size()]);
                    if(tp != null){
                        tp.setLabelAttributes(labelAttributes);
                        if(labelAttributes.length > 1){
                            tp.setTitle(true);
                        }else{
                            tp.setTitle(false);
                        }
                    }
                    // labelAttributes = response.body();
                }else{
                    ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                }
            }catch(Exception exc){
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                if(call != null) {
                    call.cancel();
                }
            }

        }
    }

*/

    private void initializeService(){
        GsonBuilder gsonB = new GsonBuilder();
        gsonB.setLenient();
        Gson gson = gsonB.create();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);

        Interceptor interceptor2 = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException
            {
                okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                ongoing.addHeader("Content-Type", "application/x-www-form-urlencoded");
                ongoing.addHeader("Accept", "application/json");

                return chain.proceed(ongoing.build());
            }
        };

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(interceptor2).connectTimeout(100, TimeUnit.SECONDS).readTimeout(100,TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsAdmin.URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        if(ConstantsAdmin.creatorService == null){
            ConstantsAdmin.creatorService = retrofit.create(CreatorService.class);
        }
        if(ConstantsAdmin.productService == null){
            ConstantsAdmin.productService = retrofit.create(CategoriesProductsService.class);
        }


    }

/*
    private View initPopupView()
    {

        return null;

    }*/

/*
    private void openTagView() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TagComboCreatorActivity.this);
        View popupView = initPopupView();
        alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
        alertDialogBuilder.setCancelable(true);


        // Set the inflated layout view object to the AlertDialog builder.
        alertDialogBuilder.setView(popupView);

        // Create AlertDialog and show.
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }*/
    private void configureWidgets() {
        textWellcomeUsr = findViewById(R.id.textWellcomeUser);
        String result = getString(R.string.wellcomeUser) + " " + ConstantsAdmin.currentCustomer.getFirstName() + " " + ConstantsAdmin.currentCustomer.getLastName();
        textWellcomeUsr.setText(result);
       // textProductSelected = findViewById(R.id.textProductSelected);
      //  textProductSelected.setText(ConstantsAdmin.currentProduct.getName());
        linearTag = findViewById(R.id.linearTag);
        spinnerFonts = this.findViewById(R.id.spinnerFonts);
        spinnerFontSizes = this.findViewById(R.id.spinnerFontSize);
        spinnerBackgrounds = this.findViewById(R.id.spinnerBackgrounds);
        spinnerProducts = this.findViewById(R.id.spinnerProducts);
        pickColor = this.findViewById(R.id.pickColor);
        Button btnReadyToGo = this.findViewById(R.id.btnReadyToGo);
        pickColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // new ColorPickerDialog(me, me, mPaint.getColor()).show();
                openColorPicker();
            }
        });
        btnReadyToGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // new ColorPickerDialog(me, me, mPaint.getColor()).show();
                openCulminarTag();
            }
        });
    }

    private void openCulminarTag() {
        actualizarTagActual();
        boolean allChecked = verificarTodosTagsCheckeados();
        if(allChecked){

            ConstantsAdmin.currentComboProducts = ConstantsAdmin.productsList;
            Intent intent = new Intent(me, TagReadyComboToGoActivity.class);
            startActivity(intent);
        }else{
            createAlertDialog(getString(R.string.mensaje_tags_combo_incompleto), getString(R.string.atencion));
            spinnerProducts.performClick();
        }

    }

    private void createAlertDialog(String message, String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle(title);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private Bitmap takeScreenShot(){
        textTag.setFocusable(false);
        textTag.setHint("");
        if(titleTag!= null){
            titleTag.setFocusable(false);
            titleTag.setHint("");
        }
        Bitmap bmp = ConstantsAdmin.takeScreenshot(linearTag);
   //     ConstantsAdmin.screenShot = bmp;
        textTag.setFocusable(true);
        textTag.setFocusableInTouchMode(true);
        textTag.requestFocus();
        textTag.setHint(R.string.your_name_here);
        if(titleTag!= null){
            titleTag.setFocusable(true);
            titleTag.setFocusableInTouchMode(true);
            titleTag.setHint(R.string.your_name_here);
        }
        return bmp;
    }

    private boolean verificarTodosTagsCheckeados() {
        boolean result = true;
        Iterator<Product> it = ConstantsAdmin.productsList.iterator();
        while(result && it.hasNext()){
            result = it.next().isChecked();
        }
        return result;
    }
/*
    private void openColorPicker(){
        ColorPicker colorPicker = new ColorPicker(me);
        colorPicker.show();
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position,int color) {
                if(!textTag.hasFocus() && (titleTag == null|| !titleTag.hasFocus())){
                    textTag.setTextColor(color);
                    if(titleTag != null){
                        titleTag.setTextColor(color);
                    }
                }else {
                    if (textTag.hasFocus()) {
                        selectedTextColor = color;
                        textTag.setTextColor(color);
                    } else if (titleTag != null && titleTag.hasFocus()) {
                        titleTag.setTextColor(color);
                        selectedTitleColor = color;
                    }
                }
                pickColor.setTextColor(color);
            }

            @Override
            public void onCancel(){
                // put code
            }
        });
    }*/


    private void openColorPicker() {

        ColorPicker colorPicker = new ColorPicker(me);
        ArrayList<String> colorToHex = new ArrayList<>();
        colorToHex.add("#FEFEFE");
        colorToHex.add("#F69060");
        colorToHex.add("#F3755C");
        colorToHex.add("#F0573F");
        colorToHex.add("#EF567E");

        colorToHex.add("#96989A");
        colorToHex.add("#F5A6C8");
        colorToHex.add("#C8A2CB");
        colorToHex.add("#35A27A");
        colorToHex.add("#48887B");

        colorToHex.add("#348164");
        colorToHex.add("#285D48");
        colorToHex.add("#4BC1BE");
        colorToHex.add("#0077BD");
        colorToHex.add("#599D9E");

        colorToHex.add("#336D83");
        colorToHex.add("#2C547E");
        colorToHex.add("#4D3C78");
        colorToHex.add("#73876F");
        colorToHex.add("#373435");

        colorToHex.add("#3E4095");
        colorToHex.add("#00A859");
        colorToHex.add("#ED3237");
        colorToHex.add("#EC268F");
        colorToHex.add("#A53692");
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position,int color) {
                TagParams tp = ConstantsAdmin.params.get(ConstantsAdmin.selectedComboProduct.getId());
                if(!textTag.hasFocus() && (titleTag == null|| !titleTag.hasFocus())){
                    textTag.setTextColor(color);
                    tp.setColorText(color);
                    tp.setPosColorText(position);
                    if(titleTag != null){
                        titleTag.setTextColor(color);
                        tp.setColorTitle(color);
                        tp.setPosColorTitle(position);
                    }
                }else {
                    if (textTag.hasFocus()) {
                        selectedTextColor = color;
                        textTag.setTextColor(color);
                        tp.setPosColorText(position);
                        tp.setColorText(color);
                    } else if (titleTag != null && titleTag.hasFocus()) {
                        titleTag.setTextColor(color);
                        selectedTitleColor = color;
                        tp.setColorTitle(color);
                        tp.setPosColorTitle(position);
                    }
                }
                pickColor.setTextColor(color);
            }

            @Override
            public void onCancel(){
                // put code
            }
        });
        colorPicker.setColors(colorToHex);
        colorPicker.setColumns(5);
        colorPicker.show();


    }
}
