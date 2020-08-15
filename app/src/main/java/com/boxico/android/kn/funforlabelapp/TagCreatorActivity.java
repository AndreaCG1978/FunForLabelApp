package com.boxico.android.kn.funforlabelapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.boxico.android.kn.funforlabelapp.dtos.Creator;
import com.boxico.android.kn.funforlabelapp.dtos.LabelAttributes;
import com.boxico.android.kn.funforlabelapp.dtos.LabelFont;
import com.boxico.android.kn.funforlabelapp.dtos.LabelImage;
import com.boxico.android.kn.funforlabelapp.services.CreatorService;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;
import com.boxico.android.kn.funforlabelapp.utils.KNCustomBackgroundAdapter;
import com.boxico.android.kn.funforlabelapp.utils.KNCustomFontSizeAdapter;
import com.boxico.android.kn.funforlabelapp.utils.KNCustomFontTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import petrov.kristiyan.colorpicker.ColorPal;
import petrov.kristiyan.colorpicker.ColorPicker;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//import static com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin.URL_LABEL_IMAGES;

public class TagCreatorActivity extends AppCompatActivity {

    private TagCreatorActivity me;
    TextView textWellcomeUsr = null;
    TextView textProductSelected = null;
    RelativeLayout linearTag = null;
    EditText textTag = null;
    EditText titleTag = null;
    List<Bitmap> listImages = null;
    private CreatorService creatorService;
  //  private Creator currentCreator;
    private LabelImage[] images;
    private LabelFont[] fonts;
    private LabelAttributes[] labelAttributes;
    private Spinner spinnerFontSizes;
    private Spinner spinnerFonts;
    private Spinner spinnerBackgrounds;
 //   private EditText entryTextTag;
    boolean acotar = false;
    private final int PERMISSIONS_WRITE_STORAGE = 101;
    private Paint mPaint;
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
        setContentView(R.layout.tag_creator);
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
/*
    @Override
    public void colorChanged(int color) {

        textTag.setTextColor(color);
        pickColor.setTextColor(color);
    }
*/
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

        if((screenWidthMM - 2.0f) <= (float)ConstantsAdmin.currentCreator.getWidth()){
            acotar = true;
        }
        Bitmap firstBitmap = images[0].getImage();
        ConstantsAdmin.customizeBackground(firstBitmap,ConstantsAdmin.currentCreator, acotar, linearTag, this);


       // CONFIGURACION DE UN AREA DE TEXTO

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




        //CONFIGURACION DE LOS SPINNERS
        final boolean needToAcot = acotar;
        spinnerFontSizes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String fontSize = null;
                fontSize = (String) parent.getAdapter().getItem(position);
                float size = Float.valueOf(fontSize);
                if(needToAcot){
                    size = size * ((float)0.82);
                }else{
                    size = size * ((float)1.00);
                }


                if(!textTag.hasFocus() && (titleTag == null|| !titleTag.hasFocus())){
                    textTag.setTextSize(TypedValue.TYPE_STRING, size);
                    selectedPosFontSizeText = position;
                    if(titleTag != null){
                        selectedPosFontSizeTitle = position;
                        titleTag.setTextSize(TypedValue.TYPE_STRING, size);
                    }
                }else {
                    if (textTag.hasFocus()) {
                        selectedPosFontSizeText = position;
                        textTag.setTextSize(TypedValue.TYPE_STRING, size);
                    } else if (titleTag != null && titleTag.hasFocus()) {
                        selectedPosFontSizeTitle = position;
                        titleTag.setTextSize(TypedValue.TYPE_STRING, size);
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

                if(!textTag.hasFocus() && (titleTag == null|| !titleTag.hasFocus())){
                    textTag.setTypeface(face);
                    selectedPosFontText = position;
                    if(titleTag != null){
                        titleTag.setTypeface(face);
                        selectedPosFontTitle = position;
                    }
                }else {
                    if (textTag.hasFocus()) {
                        selectedPosFontText = position;
                        textTag.setTypeface(face);
                    } else if (titleTag != null && titleTag.hasFocus()) {
                        selectedPosFontTitle = position;
                        titleTag.setTypeface(face);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerBackgrounds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ConstantsAdmin.selectedImage = (LabelImage) parent.getAdapter().getItem(position);
                ConstantsAdmin.customizeBackground(ConstantsAdmin.selectedImage.getImage(),ConstantsAdmin.currentCreator, acotar, linearTag, me);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFonts.setAdapter(new KNCustomFontTypeAdapter(this.getApplicationContext(), R.layout.spinner_simple_item,R.id.rowValor, fonts));
        spinnerFontSizes.setAdapter(new KNCustomFontSizeAdapter(this.getApplicationContext(), R.layout.spinner_simple_item,R.id.rowValor, ConstantsAdmin.FONT_SIZES));
        spinnerBackgrounds.setAdapter(new KNCustomBackgroundAdapter(this.getApplicationContext(), R.layout.spinner_simple_item,R.id.rowValor, images));

        textTag.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    spinnerFonts.setSelection(selectedPosFontText);
                    spinnerFontSizes.setSelection(selectedPosFontSizeText);
                    pickColor.setTextColor(selectedTextColor);

                }
            }
        });

        if(titleTag != null){
            titleTag.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        spinnerFonts.setSelection(selectedPosFontTitle);
                        spinnerFontSizes.setSelection(selectedPosFontSizeTitle);
                        pickColor.setTextColor(selectedTitleColor);
                    }
                }
            });
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    private void loadImagesForCreator() throws IOException {
        String url;
        Bitmap b;
        for (LabelImage li: images) {
            url = ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_URL_LABEL_IMAGES) + li.getUniquename();
            b = ConstantsAdmin.getImageFromURL(url);
            li.setImage(b);
        }


    }

    private class LoadFontsTask extends AsyncTask<Long, Integer, Integer> {


        private ProgressDialog dialog = null;

        @Override
        protected Integer doInBackground(Long... longs) {
            publishProgress(1);
            privateLoadFonts(labelAttributes[0].getTextAreasId());
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

        String temp, extension;
     /*   while(it.hasNext()){
            lf = it.next();
            extension = lf.getBasename().substring(lf.getBasename().length() - 4,lf.getBasename().length());
            temp = lf.getBasename().substring(0,lf.getBasename().length() - 4);
            temp = temp + "-Regular" + extension;
            lf.setBasename(temp);
            ConstantsAdmin.copyFileFromUrl(ConstantsAdmin.URL_FONTS + temp, temp);
        }
*/
        for (LabelFont lf: fonts) {
            extension = lf.getBasename().substring(lf.getBasename().length() - 4);
            temp = lf.getBasename().substring(0,lf.getBasename().length() - 4);
            temp = temp + "-Regular" + extension;
            lf.setBasename(temp);
            ConstantsAdmin.copyFileFromUrl(ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_URL_FONTS) + temp, temp);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(ConstantsAdmin.finalizarHastaMenuPrincipal){
            finish();
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
                ConstantsAdmin.currentCreator = response.body();
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

    private void privateLoadImages() {
        Call<List<LabelImage>> call = null;
        Response<List<LabelImage>> response;
        ArrayList temp;

        try {
            ConstantsAdmin.mensaje = null;
            call = creatorService.getImages(ConstantsAdmin.currentCreator.getId(), true,  ConstantsAdmin.tokenFFL);
            response = call.execute();
            if(response.body() != null){
                temp = new ArrayList<>(response.body());
                if(temp.size() == 0){
                    ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                }else{
                    images = (LabelImage[]) temp.toArray(new LabelImage[temp.size()]);
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

    private void privateLoadFonts(long textAreasId) {
        Call<List<LabelFont>> call = null;
        Response<List<LabelFont>> response;
        List<LabelFont> temp;

        try {
            ConstantsAdmin.mensaje = null;
            call = creatorService.getFonts(textAreasId, true,  ConstantsAdmin.tokenFFL);
            response = call.execute();
            if(response.body() != null){
                temp = new ArrayList<>(response.body());
                fonts = temp.toArray(new LabelFont[temp.size()]);

            }
        }catch(Exception exc){
            ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
            if(call != null) {
                call.cancel();
            }

        }
    }

    private void privateLoadAttributes() {
        Call<List<LabelAttributes>> call = null;
        Response<List<LabelAttributes>> response;
        List<LabelAttributes> temp;
        try {
            ConstantsAdmin.mensaje = null;
            call = creatorService.getLabelAttributes(ConstantsAdmin.currentCreator.getId(), true,  ConstantsAdmin.tokenFFL);
            response = call.execute();
            if(response.body() != null){
                temp = new ArrayList<>(response.body());
                labelAttributes = temp.toArray(new LabelAttributes[temp.size()]);
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


    private View initPopupView()
    {
        View popupView = null;
      /*  LayoutInflater layoutInflater = LayoutInflater.from(TagCreatorActivity.this);
        popupView = layoutInflater.inflate(R.layout.tag_view, null);
        LinearLayout ll = popupView.findViewById(R.id.tagView);
        drawTag(ll);
*/
        return popupView;

    }


    private void openTagView() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TagCreatorActivity.this);
        View popupView = initPopupView();
        alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
        alertDialogBuilder.setCancelable(true);


        // Set the inflated layout view object to the AlertDialog builder.
        alertDialogBuilder.setView(popupView);

        // Create AlertDialog and show.
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }
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
        if(textTag.getText() != null && !textTag.getText().toString().equals("")){
            ConstantsAdmin.selectedTextFont = (LabelFont)spinnerFonts.getItemAtPosition(selectedPosFontText);
            ConstantsAdmin.selectedTextFontSize = Float.valueOf((String)spinnerFontSizes.getItemAtPosition(selectedPosFontSizeText));
            ConstantsAdmin.selectedTextFontColor = selectedTextColor;
            ConstantsAdmin.selectedLabelAttrbText = labelAttributes[0];
            if(labelAttributes.length > 1){//ES UN TAG CON TITULO
                ConstantsAdmin.selectedLabelAttrbTitle =  labelAttributes[1];
                ConstantsAdmin.selectedTitleFontColor = selectedTitleColor;
                ConstantsAdmin.selectedTitleFont = (LabelFont)spinnerFonts.getItemAtPosition(selectedPosFontTitle);
                ConstantsAdmin.selectedTitleFontSize = Float.valueOf ((String)spinnerFontSizes.getItemAtPosition(selectedPosFontSizeTitle));
            }
            ConstantsAdmin.selectedBackground = ((LabelImage)spinnerBackgrounds.getSelectedItem()).getImage();
            ConstantsAdmin.selectedBackgroundFilename =((LabelImage)spinnerBackgrounds.getSelectedItem()).getUniquename();
            ConstantsAdmin.textEntered = textTag.getText().toString();
            if(titleTag != null){
                ConstantsAdmin.titleEntered = titleTag.getText().toString();
            }
            textTag.setFocusable(false);
            textTag.setHint("");
            if(titleTag!= null){
                titleTag.setFocusable(false);
                titleTag.setHint("");
            }
            Bitmap bmp = ConstantsAdmin.takeScreenshot(linearTag);
            ConstantsAdmin.screenShot = bmp;
            textTag.setFocusable(true);
            textTag.setFocusableInTouchMode(true);
            textTag.requestFocus();
            textTag.setHint(R.string.your_name_here);
            if(titleTag!= null){
                titleTag.setFocusable(true);
                titleTag.setFocusableInTouchMode(true);
                titleTag.setHint(R.string.your_name_here);
            }

            Intent intent = new Intent(me, TagReadyToGoActivity.class);
            startActivity(intent);
        }else{
            createAlertDialog(getString(R.string.tag_incompleto), getString(R.string.atencion));
        }

    }

    private void createAlertDialog(String message, String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle(title);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
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
        colorPicker.setColors(colorToHex);
        colorPicker.setColumns(5);
        colorPicker.show();

              /*
                setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
            @Override
            public void setOnFastChooseColorListener(int position, int color) {
                // put code
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
        }).setDefaultColorButton(Color.parseColor("#f84c44")).setColumns(5).setColors(colorToHex).show();
*/
     /*   ColorPicker colorPicker = new ColorPicker(me);
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
        colorPicker.show();*/

        /*
        colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
            @Override
            public void setOnFastChooseColorListener(int position, int color) {
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
        }).setDefaultColorButton(Color.parseColor("#f84c44")).setColumns(5).show();
*/
        /*



        AmbilWarnaDialog myColorPicker = new AmbilWarnaDialog(this, Color.BLACK, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
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
        });
        myColorPicker.show();*/
    }
}
