package com.boxico.android.kn.funforlabelapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentActivity;

import com.boxico.android.kn.funforlabelapp.dtos.Creator;
import com.boxico.android.kn.funforlabelapp.dtos.LabelAttributes;
import com.boxico.android.kn.funforlabelapp.dtos.LabelFont;
import com.boxico.android.kn.funforlabelapp.dtos.LabelImage;
import com.boxico.android.kn.funforlabelapp.dtos.Product;
import com.boxico.android.kn.funforlabelapp.services.CreatorService;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;
import com.boxico.android.kn.funforlabelapp.utils.location.Geoname;
import com.boxico.android.kn.funforlabelapp.utils.location.LocationManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin.URL_IMAGES;
import static com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin.URL_LABEL_IMAGES;

public class TagCreatorActivity extends FragmentActivity {

    private TagCreatorActivity me;
    TextView textWellcomeUsr = null;
    TextView textProductSelected = null;
    LinearLayout linearTag = null;
    TextView textTag = null;
    List<Bitmap> listImages = null;
    private CreatorService creatorService;
    private Creator currentCreator;
    private List<LabelImage> images;
    private List<LabelFont> fonts;
    private LabelAttributes labelAttributes;
    private Spinner spinnerFontSizes;
    private Spinner spinnerFonts;
    private EditText entryTextTag;

    private final int PERMISSIONS_WRITE_STORAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;
        setContentView(R.layout.tag_creator);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.initializeService();
        this.configureWidgets();
        this.askForWriteStoragePermission();
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //   mCameraPermissionGranted = false;


        if (requestCode == PERMISSIONS_WRITE_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.loadCreator();
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
                this.loadCreator();
            }
        } else {
            this.loadCreator();
        }


    }

    private void loadCreator() {
        new LoadCreatorTask().execute();
    }

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
         //   new LoadImagesTask().execute();
            new LoadAttributesTask().execute();
        }
    }

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

    public float pxToMm(float px, Context context)
    {
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return px / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1, dm);
    }

    private void initializeCreator() {
        boolean acotar = false;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        float screenWidthMM = pxToMm((float) width, this);

        if(screenWidthMM < currentCreator.getWidth()){
            acotar = true;
        }
        Bitmap firstBitmap = images.get(0).getImage();

        float temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, currentCreator.getWidth() ,
                getResources().getDisplayMetrics());
        if(acotar){
            temp = temp - temp * 3/20;
        }
        int realWidthImage = (int)temp;
        temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, currentCreator.getHeight() ,
                getResources().getDisplayMetrics());

        if(acotar){
            temp = temp - temp * 3/20;
        }
        int realHeightImage = (int)temp;
        Bitmap b =Bitmap.createScaledBitmap(firstBitmap, realWidthImage, realHeightImage, false);
     //   firstBitmap.setWidth(realWidthImage);
     //   firstBitmap.setHeight(realHeightImage);
        Drawable d = new BitmapDrawable(getResources(), b);
        linearTag.setBackground(d);
       // textTag.setHint(R.string.your_name_here);

        temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, labelAttributes.getWidth() + 1,
                getResources().getDisplayMetrics());
        if(acotar){
            temp = temp - temp * 3/20;
        }
       // LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(labelAttributes.getWidth(), labelAttributes.getHeight());
        int w = (int)(temp);
        int wEntry = (int)(temp * (float)1.25);
        temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, labelAttributes.getHeight() + 1,
                getResources().getDisplayMetrics());
        if(acotar){
            temp = temp - temp * 3/20;
        }
        int h = (int)(temp);
        int hEntry = (int)(temp * (float)1.25);

        LinearLayout.LayoutParams layoutParamsTextTag = new LinearLayout.LayoutParams(w, h);
        LinearLayout.LayoutParams layoutParamsEntryTextTag = new LinearLayout.LayoutParams(wEntry, hEntry);
        entryTextTag.setLayoutParams(layoutParamsEntryTextTag);

        temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, labelAttributes.getFromY() ,
                getResources().getDisplayMetrics());
        if(acotar){
            temp = temp - temp * 3/20;
        }
        int fromY = (int)temp;

        temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, labelAttributes.getFromX() ,
                getResources().getDisplayMetrics());
        if(acotar){
            temp = temp - temp * 3/20;
        }
        int fromX = (int)temp;
        layoutParamsTextTag.setMargins(fromX, fromY, -1,-1);

        textTag.setLayoutParams(layoutParamsTextTag);

        //textTag.setTypeface(Typeface.);

/*
        File fileFont = ConstantsAdmin.getFile(fonts.get(0).getBasename());
        Typeface face = Typeface.createFromFile(fileFont);
        textTag.setTypeface(face);*/
        textTag.setGravity(Gravity.CENTER);
        textTag.setPadding(5,0,5,0);
        textTag.setBackgroundColor(Color.TRANSPARENT);
        textTag.setTextColor(Color.BLACK);
      //  textTag.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
     //   entryTextTag.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        entryTextTag.setPadding(5,0,5,0);
        entryTextTag.setGravity(Gravity.CENTER);

//        textTag.setBackgroundResource(android.R.color.transparent);


        final boolean needToAcot = acotar;
        spinnerFontSizes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String fontSize = null;
                fontSize = (String) parent.getAdapter().getItem(position);
                float size = Float.valueOf(fontSize);
                if(needToAcot){
                    size = size * ((float)0.87);
                }else{
                    size = size * ((float)1.007);
                }
                float sizeEntry = size;
                textTag.setTextSize(TypedValue.TYPE_STRING, size);
                // size = size * ((float)1.0);
                sizeEntry = sizeEntry * (float)2.2;
                entryTextTag.setTextSize(sizeEntry);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerFontSizes.setSelection(1);


        spinnerFonts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LabelFont lf = null;
                lf = (LabelFont) parent.getAdapter().getItem(position);
                File fileFont = ConstantsAdmin.getFile(lf.getBasename());
                Typeface face = Typeface.createFromFile(fileFont);
                textTag.setTypeface(face);
                entryTextTag.setTypeface(face);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        linearTag.addView(textTag);

        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.TRANSPARENT); //white background
        border.setStroke(3, Color.RED); //black border with full opacity
        //linearTag.

        textTag.setBackground(border);
        textTag.setEllipsize(TextUtils.TruncateAt.END);
        spinnerFonts.setAdapter(new ArrayAdapter<LabelFont>(this.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, fonts));

    }

    private void loadImagesForCreator() throws IOException {
        Iterator<LabelImage> it = images.iterator();
        LabelImage li;
        String url;
        Bitmap b;
        while (it.hasNext()){
            li = it.next();
            url = URL_LABEL_IMAGES + li.getUniquename();
            b = ConstantsAdmin.getImageFromURL(url);
            li.setImage(b);
            //this.addProductInView(p);
        }
    }

    private class LoadFontsTask extends AsyncTask<Long, Integer, Integer> {


        private ProgressDialog dialog = null;

        @Override
        protected Integer doInBackground(Long... longs) {
            publishProgress(1);
            privateLoadFonts();
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
            new GetFontFilesTask().execute();
        }
    }

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

    private void privateGetFontFiles() {
        LabelFont lf;
        Iterator<LabelFont> it = fonts.iterator();
        String temp, extension;
        while(it.hasNext()){
            lf = it.next();
            extension = lf.getBasename().substring(lf.getBasename().length() - 4,lf.getBasename().length());
            temp = lf.getBasename().substring(0,lf.getBasename().length() - 4);
            temp = temp + "-Regular" + extension;
            lf.setBasename(temp);
            ConstantsAdmin.copyFileFromUrl(ConstantsAdmin.URL_FONTS + temp, temp);
        }
    }


    private void privateLoadCreator() {
        Call<Creator> call = null;
        Response<Creator> response;

        try {
            ConstantsAdmin.mensaje = null;
            call = creatorService.getCreator(ConstantsAdmin.currentProduct.getId(), true, ConstantsAdmin.tokenFFL);
            response = call.execute();
            if(response.body() != null){
                currentCreator = response.body();
                if(currentCreator == null){
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

    private void privateLoadImages() {
        Call<List<LabelImage>> call = null;
        Response<List<LabelImage>> response;

        try {
            ConstantsAdmin.mensaje = null;
            call = creatorService.getImages(currentCreator.getId(), true,  ConstantsAdmin.tokenFFL);
            response = call.execute();
            if(response.body() != null){
                images = new ArrayList<>(response.body());
                if(images.size() == 0){
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

    private void privateLoadFonts() {
        Call<List<LabelFont>> call = null;
        Response<List<LabelFont>> response;

        try {
            ConstantsAdmin.mensaje = null;
            call = creatorService.getFonts(labelAttributes.getTextAreasId(), true,  ConstantsAdmin.tokenFFL);
            response = call.execute();
            if(response.body() != null){
                fonts = new ArrayList<>(response.body());

            }
        }catch(Exception exc){
            ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
            if(call != null) {
                call.cancel();
            }

        }
    }

    private void privateLoadAttributes() {
        Call<LabelAttributes> call = null;
        Response<LabelAttributes> response;

        try {
            ConstantsAdmin.mensaje = null;
            call = creatorService.getLabelAttributes(currentCreator.getId(), true,  ConstantsAdmin.tokenFFL);
            response = call.execute();
            if(response.body() != null){
                labelAttributes = response.body();
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



    private void initializeService(){
        GsonBuilder gsonB = new GsonBuilder();
        gsonB.setLenient();
        Gson gson = gsonB.create();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);

        Interceptor interceptor2 = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException
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
        creatorService = retrofit.create(CreatorService.class);
    }

    private void configureWidgets() {
        textWellcomeUsr = findViewById(R.id.textWellcomeUser);
        textWellcomeUsr.setText(getString(R.string.wellcomeUser) + " " + ConstantsAdmin.currentCustomer.getFirstName() + " " + ConstantsAdmin.currentCustomer.getLastName());
        textProductSelected = findViewById(R.id.textProductSelected);
        textProductSelected.setText(ConstantsAdmin.currentProduct.getName());
        linearTag = findViewById(R.id.linearTag);
        textTag = new TextView(this);
        spinnerFonts =  (Spinner) this.findViewById(R.id.spinnerFonts);
        spinnerFontSizes = (Spinner) this.findViewById(R.id.spinnerFontSize);
        spinnerFontSizes.setAdapter(new ArrayAdapter<String>(this.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ConstantsAdmin.FONT_SIZES));
        entryTextTag = findViewById(R.id.entryTextTag);


        entryTextTag.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
//                if(cs.toString().length() == 0) {
                    textTag.setText(entryTextTag.getText());
  //              }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) { }

        });



    }
}
