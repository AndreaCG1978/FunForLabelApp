package com.boxico.android.kn.funforlabelapp;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.boxico.android.kn.funforlabelapp.dtos.AddressBook;
import com.boxico.android.kn.funforlabelapp.dtos.Customer;
import com.boxico.android.kn.funforlabelapp.services.CustomerService;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;
import com.boxico.android.kn.funforlabelapp.utils.KNCarritoAdapterListView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigurarEnvioActivity extends FragmentActivity {

    private ConfigurarEnvioActivity me;
    private TextView textWellcomeUsr;
    private TextView textIntroEnvio;
    private TextView textDirEnvio;
    private CustomerService customerService;
    private AddressBook addressCustomer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;
        setContentView(R.layout.configurar_envio);
        this.initializeService();
        this.configureWidgets();
        new LoadCustomerTask().execute();


    }

    private class LoadCustomerTask extends AsyncTask<Long, Integer, Integer> {


        private ProgressDialog dialog = null;
        private int resultado = -1;

        @Override
        protected Integer doInBackground(Long... longs) {
            publishProgress(1);
            loadAddressBook();
            return 0;
        }

        protected void onProgressUpdate(Integer... progress) {
            dialog = ProgressDialog.show(me, "",
                    getResources().getString(R.string.loading_data), true);
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            textDirEnvio = findViewById(R.id.textDirEnvio);
            Customer c = ConstantsAdmin.currentCustomer;
            String temp = c.getFirstName() + " " + c.getLastName() + "\n";
            temp = temp + addressCustomer.getCalle() + "\n";
            if(addressCustomer.getSuburbio() != null && !addressCustomer.getSuburbio().equals("")){
                temp = temp + addressCustomer.getSuburbio() + "\n";
            }
            temp = temp + addressCustomer.getCiudad() + ", " + addressCustomer.getCp() + "\n";
            temp = temp + addressCustomer.getProvincia() + ", " + ConstantsAdmin.GEOCODIGOARGENTINA;
            textDirEnvio.setText(temp);
            if(dialog != null) {
                dialog.cancel();
            }
        }
    }

    private void loadAddressBook() {
        Call<List<AddressBook>> call = null;
        Response<List<AddressBook>> response;
        ArrayList<AddressBook> customers;

        try {
            ConstantsAdmin.mensaje = null;
            call = customerService.getCustomerAddress(ConstantsAdmin.currentCustomer.getId(), ConstantsAdmin.tokenFFL);
            response = call.execute();
            if(response.body() != null){
                customers = new ArrayList<>(response.body());
                if(customers.size() == 1){
                    addressCustomer = customers.get(0);
                }else{
                    ConstantsAdmin.mensaje = getResources().getString(R.string.customer_not_exists);

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
        customerService = retrofit.create(CustomerService.class);
    }

    private void configureWidgets() {

        textWellcomeUsr = findViewById(R.id.textWellcomeUser);
        textWellcomeUsr.setText(getString(R.string.wellcomeUser) + " " + ConstantsAdmin.currentCustomer.getFirstName() + " " + ConstantsAdmin.currentCustomer.getLastName());
        textIntroEnvio = findViewById(R.id.textIntroEnvio);
      //  textIntroEnvio.setTypeface(Typeface.SANS_SERIF);
        textIntroEnvio.setText(ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.INTRO_ENVIO));



        // configListView(listViewCarrito);
    }

}
