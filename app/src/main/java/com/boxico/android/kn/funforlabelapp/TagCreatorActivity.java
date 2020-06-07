package com.boxico.android.kn.funforlabelapp;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.boxico.android.kn.funforlabelapp.dtos.Creator;
import com.boxico.android.kn.funforlabelapp.dtos.LabelImage;
import com.boxico.android.kn.funforlabelapp.dtos.Product;
import com.boxico.android.kn.funforlabelapp.services.CreatorService;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    EditText textTag = null;
    List<Bitmap> listImages = null;
    private CreatorService creatorService;
    private Creator currentCreator;
    private List<LabelImage> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;
        setContentView(R.layout.tag_creator);
        this.initializeService();
        this.configureWidgets();
        this.loadCreator();
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
            new LoadImagesTask().execute();
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

    private void initializeCreator() {
        Bitmap firstBitmap = images.get(0).getImage();
        Drawable d = new BitmapDrawable(getResources(), firstBitmap);
        linearTag.setBackground(d);
        textTag.setHint(R.string.your_name_here);
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
        textTag = findViewById(R.id.textTag);
    }
}
