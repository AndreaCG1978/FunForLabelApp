package com.boxico.android.kn.funforlabelapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.boxico.android.kn.funforlabelapp.dtos.Category;
import com.boxico.android.kn.funforlabelapp.services.CategoriesProductsService;

import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin.URL_IMAGES;

public class MainActivity extends FragmentActivity {

    TextView textWellcomeUsr = null;
    LinearLayout linearCategories = null;
    MainActivity me;
    CategoriesProductsService categoriesProductsService = null;
    ArrayList<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;
        setContentView(R.layout.main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.initializeService();
        this.initializeLang();
        this.configureWidgets();
        this.loadCategories();

    }

    private void initializeLang() {
        if(Locale.getDefault().getLanguage().equalsIgnoreCase("es")){
            ConstantsAdmin.currentLanguage = 2;
        }else{
            ConstantsAdmin.currentLanguage = 1;
        }
    }

    private void loadCategories() {
        new LoadCategoriesTask().execute();
    }

    private void configureWidgets() {
        textWellcomeUsr = findViewById(R.id.textWellcomeUser);
        linearCategories = findViewById(R.id.linearCategories);
        textWellcomeUsr.setText(getString(R.string.wellcomeUser) + " " + ConstantsAdmin.currentCustomer.getFirstName() + " " + ConstantsAdmin.currentCustomer.getLastName());
    }

    private class LoadCategoriesTask extends AsyncTask<Long, Integer, Integer> {
        private ProgressDialog dialog = null;
        @Override
        protected Integer doInBackground(Long... params) {

            try {
                publishProgress(1);
                privateLoadCategories();


            } catch (Exception e) {
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
            }
            return 0;

        }

        protected void onProgressUpdate(Integer... progress) {
            dialog = ProgressDialog.show(me, "",
                    getResources().getString(R.string.loading_data), true);
        }



        @Override
        protected void onPostExecute(Integer result) {
            if(ConstantsAdmin.mensaje != null){
                createAlertDialog(ConstantsAdmin.mensaje,getResources().getString(R.string.atencion));
                ConstantsAdmin.mensaje = null;
            }
            dialog.cancel();
            if(categories != null && categories.size()>0) {
                try {
                    loadImageForCategories();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void loadImageForCategories() throws IOException {
        Iterator<Category> it = categories.iterator();
        Category cat;
        String url;
        Bitmap b;
        while (it.hasNext()){
            cat = it.next();
            url = URL_IMAGES + cat.getImageString();
            b = ConstantsAdmin.getImageFromURL(url);
            cat.setImage(b);
            this.addCategoryInView(cat);
        }
    }

    private void addCategoryInView(Category cat) {
        ImageView iv = new ImageView(getApplicationContext());
        iv.setImageBitmap(cat.getImage());
        iv.setTag(cat.getId());

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView img = (ImageView) v;
                goToProductsList((long)img.getTag());
            }
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        iv.setLayoutParams(lp);

        TextView tv1 = new TextView(this);
        tv1.setText(cat.getName());

        LinearLayout parent = new LinearLayout(this);
        parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        parent.setOrientation(LinearLayout.VERTICAL);


        parent.addView(tv1);
        parent.addView(iv);
        linearCategories.addView(parent);
    }

    private void goToProductsList(long cateogyId) {
        ConstantsAdmin.currentCategory = cateogyId;
        Intent intent = new Intent(me, ProductsListActivity.class);
        startActivity(intent);
    }




    private void privateLoadCategories() {
        Call<List<Category>> call = null;
        Response<List<Category>> response;

        try {
            ConstantsAdmin.mensaje = null;
            call = categoriesProductsService.getCategories(ConstantsAdmin.categories[0], ConstantsAdmin.currentLanguage, ConstantsAdmin.tokenFFL);
            response = call.execute();
            if(response.body() != null){
                categories = new ArrayList<>(response.body());
                if(categories.size() == 0){
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
        categoriesProductsService = retrofit.create(CategoriesProductsService.class);
    }

    private void createAlertDialog(String message, String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(me);
        builder.setMessage(message).setTitle(title);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}