package com.boxico.android.kn.funforlabelapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.fragment.app.FragmentActivity;

import com.boxico.android.kn.funforlabelapp.dtos.Category;
import com.boxico.android.kn.funforlabelapp.dtos.Product;
import com.boxico.android.kn.funforlabelapp.services.CategoriesProductsService;
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

public class ProductsListActivity extends FragmentActivity {

    private ProductsListActivity me;
    private CategoriesProductsService categoriesProductsService;
    ArrayList<Product> products;
    LinearLayout linearProducts = null;
    TextView textWellcomeUsr = null;
    TextView textCategorySelected = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;
        setContentView(R.layout.products_list);
        this.initializeService();
        this.configureWidgets();
        this.loadProducts();
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
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
        categoriesProductsService = retrofit.create(CategoriesProductsService.class);
    }


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
            dialog.cancel();
            if(products != null && products.size()>0) {
                try {
                    loadImageForProducts();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            textCategorySelected.setText(ConstantsAdmin.currentCategory.getName());

        }
    }

    private void privateLoadProducts() {
        Call<List<Product>> call = null;
        Response<List<Product>> response;

        try {
            ConstantsAdmin.mensaje = null;
            call = categoriesProductsService.getProductsFromCategory(ConstantsAdmin.currentCategory.getId(), ConstantsAdmin.currentLanguage, ConstantsAdmin.tokenFFL);
            response = call.execute();
            if(response.body() != null){
                products = new ArrayList<>(response.body());
                if(products.size() == 0){
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


    private void createAlertDialog(String message, String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(me);
        builder.setMessage(message).setTitle(title);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadImageForProducts() throws IOException {
        Iterator<Product> it = products.iterator();
        Product p;
        String url;
        Bitmap b;
        while (it.hasNext()){
            p = it.next();
            url = URL_IMAGES + p.getImageString();
            b = ConstantsAdmin.getImageFromURL(url);
            p.setImage(b);
            this.addProductInView(p);
        }
    }

    private void addProductInView(Product p) {

        // SE CREA EL PRIMER LAYOUT QUE MUESTRA LA IMAGEN Y EL MODELO
        ImageView iv = new ImageView(getApplicationContext());
        iv.setImageBitmap(p.getImage());
        iv.setTag(p.getId());

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView img = (ImageView) v;
             //   goToProductsList((long)img.getTag());
            }
        });

        LinearLayout.LayoutParams lpImagen = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpImagen.setMargins(10, 10, 10, 10);
        iv.setLayoutParams(lpImagen);
        lpImagen.width = 250;
        lpImagen.height = 250;

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
        layoutParams.setMargins(10, 10, 10, 10);
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
            descEtiqueta.setText(Html.fromHtml(p.getDescription()));
        }
        descEtiqueta.setLayoutParams(lpText);
        descEtiqueta.setTextSize(11);
        descEtiqueta.setTextColor(Color.DKGRAY);
        descEtiqueta.setTypeface(Typeface.SANS_SERIF);

        TextView precioEtiqueta = new TextView(this);
        precioEtiqueta.setText(getString(R.string.price) + p.getPrice());
        precioEtiqueta.setLayoutParams(lpText);
        precioEtiqueta.setTextSize(14);
        precioEtiqueta.setTextColor(Color.BLACK);
        precioEtiqueta.setTypeface(Typeface.SANS_SERIF);

        TextView cantidadEtiqueta = new TextView(this);
        cantidadEtiqueta.setText(getString(R.string.quantity) + " " + p.getQuantity() + " " + getString(R.string.per_pack));
        cantidadEtiqueta.setLayoutParams(lpText);
        cantidadEtiqueta.setTextSize(12);
        cantidadEtiqueta.setTextColor(Color.BLACK);
        cantidadEtiqueta.setTypeface(Typeface.SANS_SERIF);


        LinearLayout l2 = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams2.setMargins(10, 10, 10, 10);
        l2.setLayoutParams(layoutParams2);
        l2.setOrientation(LinearLayout.VERTICAL);
        l2.setGravity(Gravity.LEFT);

        l2.addView(nombreEtiqueta);
    //    l2.addView(descEtiqueta);
        l2.addView(precioEtiqueta);
        l2.addView(cantidadEtiqueta);


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
        border.setStroke(3, Color.RED); //black border with full opacity
        parent.setBackground(border);

        linearProducts.addView(parent);
    }

    private void configureWidgets() {
        textWellcomeUsr = findViewById(R.id.textWellcomeUser);
        linearProducts = findViewById(R.id.linearProducts);
        textWellcomeUsr.setText(getString(R.string.wellcomeUser) + " " + ConstantsAdmin.currentCustomer.getFirstName() + " " + ConstantsAdmin.currentCustomer.getLastName());
        textCategorySelected = findViewById(R.id.textCategorySelected);
        //textCategorySelected.setText(getString(R.string.c));
    }

    private void loadProducts(){
        new LoadProductsTask().execute();

    }

}
