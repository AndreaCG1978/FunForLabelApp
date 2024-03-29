package com.boxico.android.kn.funforlabelapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;


import com.boxico.android.kn.funforlabelapp.dtos.Category;
import com.boxico.android.kn.funforlabelapp.dtos.Product;
import com.boxico.android.kn.funforlabelapp.services.CategoriesProductsService;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;
import com.boxico.android.kn.funforlabelapp.utils.workers.LoadCategoriesWorker;
import com.boxico.android.kn.funforlabelapp.utils.workers.LoadImageFromUrlWorker;
import com.boxico.android.kn.funforlabelapp.utils.workers.LoadProductImageFromUrlWorker;
import com.boxico.android.kn.funforlabelapp.utils.workers.LoadProductsWorker;
import com.google.common.util.concurrent.ListenableFuture;
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



public class ProductsListActivity extends FragmentActivity {

    private ProductsListActivity me;

   // ArrayList<Product> products;
    LinearLayout linearProducts = null;
    TextView textWellcomeUsr = null;
    TextView textCategorySelected = null;
    private ProgressBar progressBar = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;
        setContentView(R.layout.products_list);
        this.initializeService();
        this.configureWidgets();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.loadProducts();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        if(ConstantsAdmin.categoriesProductsService == null){
            ConstantsAdmin.categoriesProductsService = retrofit.create(CategoriesProductsService.class);
        }

    }

/*
    private class LoadProductsTask extends AsyncTask<Long, Integer, Integer> {
        private ProgressDialog dialog = null;
        @Override
        protected Integer doInBackground(Long... params) {

            try {
                publishProgress(1);
                privateLoadProducts();

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

            if(products != null && products.size()>0) {
                try {
                    loadImageForProducts();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            textCategorySelected.setText(ConstantsAdmin.currentCategory.getName());
            dialog.cancel();

        }
    }
*/

/*
    private void privateLoadProducts() {
        Call<List<Product>> call = null;
        Response<List<Product>> response;
        try {
            ConstantsAdmin.mensaje = null;
            call = ConstantsAdmin.categoriesProductsService.getProductsFromCategory(ConstantsAdmin.currentCategory.getId(), ConstantsAdmin.currentLanguage, ConstantsAdmin.tokenFFL);
            response = call.execute();
            if(response.body() != null){
                ConstantsAdmin.allProducts = new ArrayList<>(response.body());
                if(ConstantsAdmin.allProducts.size() == 0){
                    ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                }else{
                    try {
                        loadImageForProducts();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                   textCategorySelected.setText(ConstantsAdmin.currentCategory.getName());
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
*/
    @Override
    protected void onStart() {
        super.onStart();
        if(ConstantsAdmin.finalizarHastaMenuPrincipal){
            finish();
        }


    }


    private void createAlertDialog(String message, String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(me);
        builder.setMessage(message).setTitle(title);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadImageForProducts() throws IOException {
/*        Iterator<Product> it = ConstantsAdmin.allProducts.iterator();
        Product p;
        String url;
        Bitmap b;
        while (it.hasNext()){
            p = it.next();
            url = ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_URL_IMAGES) + p.getImageString();
            b = ConstantsAdmin.getImageFromURL(url);
            p.setImage(b);
            this.addProductInView(p);
        }*/

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        Data inputData = new Data.Builder().build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(LoadProductImageFromUrlWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            Iterator<Product> it = ConstantsAdmin.allProducts.iterator();
                            Product p;
                            while (it.hasNext()){
                                p = it.next();
                                //url = ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_URL_IMAGES) + p.getImageString();
                              //  b = ConstantsAdmin.getImageFromURL(url);
                                //p.setImage(b);
                                me.addProductInView(p);
                            }
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
        ListenableFuture<Operation.State.SUCCESS> obj = WorkManager.getInstance(this).enqueue(request).getResult();

    }

    private void addProductInView(Product p) {

        // SE CREA EL PRIMER LAYOUT QUE MUESTRA LA IMAGEN Y EL MODELO
        ImageView iv = new ImageView(getApplicationContext());
        iv.setImageBitmap(p.getImage());
        iv.setTag(p);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView img = (ImageView) v;
                goToTagCreator((Product)img.getTag());
            }
        });

        LinearLayout.LayoutParams lpImagen = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpImagen.setMargins(10, 10, 10, 10);
        iv.setLayoutParams(lpImagen);
        lpImagen.width = 300;
        lpImagen.height = 300;

        iv.requestLayout();

        LinearLayout.LayoutParams lpText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpText.setMargins(15, 10, 15, 10);

        LinearLayout.LayoutParams lpModel = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpModel.setMargins(10, 1, 10, 3);


        TextView tv1 = new TextView(this);
        tv1.setText(p.getModel());
        tv1.setLayoutParams(lpModel);
        tv1.setTextSize(11);
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextColor(Color.DKGRAY);
        tv1.setTypeface(Typeface.SANS_SERIF);


        LinearLayout l1 = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15, 10, 15, 10);
        l1.setLayoutParams(layoutParams);
        l1.setGravity(Gravity.CENTER);
        l1.setOrientation(LinearLayout.VERTICAL);

        l1.addView(iv);
        l1.addView(tv1);

        // SE CREA EL SEGUNDO LAYOUT QUE MUESTRA NOMBRE, DESC, PRECIO Y CANTIDAD

        TextView nombreEtiqueta = new TextView(this);
        nombreEtiqueta.setText(p.getName());
        nombreEtiqueta.setLayoutParams(lpText);
        nombreEtiqueta.setTextSize(15);
        nombreEtiqueta.setTypeface(Typeface.create("sans-serif-smallcaps", Typeface.NORMAL));
        nombreEtiqueta.setTextColor(Color.BLACK);

        TextView descEtiqueta = new TextView(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            descEtiqueta.setText(Html.fromHtml(p.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            descEtiqueta.setText(Html.fromHtml(p.getDescription(), Html.FROM_HTML_MODE_LEGACY));
        }
        descEtiqueta.setLayoutParams(lpText);
        descEtiqueta.setTextSize(11);
        descEtiqueta.setTextColor(Color.DKGRAY);
        descEtiqueta.setTypeface(Typeface.SANS_SERIF);
        //descEtiqueta.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);

        TextView precioEtiqueta = new TextView(this);
        String newPrice = getString(R.string.price) + p.getPrice().substring(0, p.getPrice().length() - 2);
        precioEtiqueta.setText(newPrice);
        precioEtiqueta.setLayoutParams(lpText);
        precioEtiqueta.setTextSize(14);
        precioEtiqueta.setTextColor(Color.BLACK);
        precioEtiqueta.setTypeface(Typeface.SANS_SERIF);

        TextView cantidadEtiqueta = new TextView(this);
        String temp = getString(R.string.quantity) + " " + p.getQuantity() + " " + getString(R.string.per_pack);
        cantidadEtiqueta.setText(temp);
        cantidadEtiqueta.setLayoutParams(lpText);
        cantidadEtiqueta.setTextSize(12);
        cantidadEtiqueta.setTextColor(Color.BLACK);
        cantidadEtiqueta.setTypeface(Typeface.SANS_SERIF);


        LinearLayout l2 = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams2.setMargins(5, 10, 10, 10);
        l2.setLayoutParams(layoutParams2);
        l2.setOrientation(LinearLayout.VERTICAL);
        l2.setGravity(Gravity.START);


        l2.addView(nombreEtiqueta);
    //    l2.addView(descEtiqueta);
        l2.addView(precioEtiqueta);
        l2.addView(cantidadEtiqueta);
        l2.setTag(p);

        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll = (LinearLayout) v;
                goToTagCreator((Product)ll.getTag());
            }
        });


        LinearLayout l3 = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams3.setMargins(10, 10, 10, 10);
        l3.setLayoutParams(layoutParams2);
        l3.setOrientation(LinearLayout.HORIZONTAL);
        l3.setGravity(Gravity.CENTER);

        l3.addView(l1);
        //    l2.addView(descEtiqueta);
        l3.addView(l2);


        LinearLayout parent = new LinearLayout(this);
        LinearLayout.LayoutParams paramsParent = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsParent.setMargins(25, 10, 25, 20);
        parent.setLayoutParams(paramsParent);
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.addView(l3);
        parent.addView(descEtiqueta);


        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); //white background
        border.setStroke(3, Color.RED);
        border.setCornerRadius(17);//black border with full opacity
        parent.setBackground(border);

        linearProducts.addView(parent);
    }

    private void goToTagCreator(Product prod) {
        long idCat = Long.parseLong(ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ID_CATEGORY_COMBO));
        ConstantsAdmin.currentProduct = prod;
        Intent intent;
        if(ConstantsAdmin.currentCategory.getId()!=idCat) {// NO ES UN COMBO
            intent = new Intent(me, TagCreatorActivity.class);
        }else{// ES UN COMBO
            intent = new Intent(me, TagComboCreatorActivity.class);
        }
        startActivity(intent);
    }

    private void configureWidgets() {

        LinearLayout layout = findViewById(R.id.mainLayout);
        progressBar = new ProgressBar(ProductsListActivity.this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, 4,params);
        textWellcomeUsr = findViewById(R.id.textWellcomeUser);
        linearProducts = findViewById(R.id.linearProducts);
        String result = getString(R.string.wellcomeUser) + " " + ConstantsAdmin.currentCustomer.getFirstName() + " " + ConstantsAdmin.currentCustomer.getLastName();
        textWellcomeUsr.setText(result);
        textCategorySelected = findViewById(R.id.textCategorySelected);
        //textCategorySelected.setText(getString(R.string.c));
    }

    private void loadProducts(){
      //  new LoadProductsTask().execute();
      //  privateLoadProducts();

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Data inputData = new Data.Builder().build();


        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(LoadProductsWorker.class)
                .setInputData(inputData)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.RUNNING) {
                            progressBar.setVisibility(View.VISIBLE);
                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {

                            if(ConstantsAdmin.currentCustomer != null){
                                if(ConstantsAdmin.allProducts.size() == 0){
                                    createAlertDialog(getString(R.string.conexion_server_error), getString(R.string.atencion));
                                }else{
                                    try {
                                        loadImageForProducts();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    textCategorySelected.setText(ConstantsAdmin.currentCategory.getName());
                                }
                            }else{
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                progressBar.setVisibility(View.GONE);
                                createAlertDialog(getString(R.string.conexion_server_error), getString(R.string.atencion));
                                ConstantsAdmin.mensaje = null;
                                //  buttonLogin.setEnabled(true);
                            }

                        }
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.FAILED) {
                            ConstantsAdmin.mensaje = null;
                        }
                    }
                });
        ListenableFuture<Operation.State.SUCCESS> obj = WorkManager.getInstance(this).enqueue(request).getResult();

    }

}
