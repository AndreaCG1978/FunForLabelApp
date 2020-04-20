package com.boxico.android.kn.funforlabelapp;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.boxico.android.kn.funforlabelapp.dtos.Customer;
import com.boxico.android.kn.funforlabelapp.services.CustomerService;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.ParseException;
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

public class LoginActivity extends FragmentActivity {


    private EditText userEntry = null;
    private EditText passEntry = null;
    private Button buttonLogin = null;
    private TextView crearCuentaText = null;
    private TextView recuperarContrasenia = null;
    private CustomerService customerService = null;
    private String pswText;
    private String usrText;
    private LoginActivity me;
    private CheckBox saveLogin = null;
    private ImageButton hiddeShowPass;
    private boolean isShowingPass = false;
    private ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        me = this;
        this.configureWidgets();
        this.initializeService();
    }

    private void configureWidgets() {
        buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonCancel = findViewById(R.id.buttonCancel);
        userEntry = findViewById(R.id.usrEntry);
        passEntry = findViewById(R.id.passEntry);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginCustomer();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        crearCuentaText = findViewById(R.id.crearCuentaText);
        recuperarContrasenia = findViewById(R.id.recuperarContrasenia);
        crearCuentaText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialog("En proceso de construcción", getResources().getString(R.string.atencion));
            }
        });
        recuperarContrasenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialog("En proceso de construcción", getResources().getString(R.string.atencion));
            }
        });
        saveLogin = findViewById(R.id.checkSaveLogin);
   /*     hiddeShowPass = findViewById(R.id.imagenShowPassword);
        hiddeShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowingPass = !isShowingPass;
                if(isShowingPass){
                    passEntry.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    hiddeShowPass.setBackgroundResource(R.drawable.showpass);
                }else{
                    passEntry.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    hiddeShowPass.setBackgroundResource(R.drawable.hidepass);
                }
            }
        });*/
        userEntry.setText("acgrassano1978@gmail.com");
        passEntry.setText("andrea");
    }

    private void loginCustomer() {

        usrText = userEntry.getText().toString();
        pswText = passEntry.getText().toString();

        if (!usrText.equals("") && (!pswText.equals(""))) {

            new LoginCustomerTask().execute();
        } else {
            createAlertDialog(getResources().getString(R.string.login_error), getResources().getString(R.string.atencion));
        }

    }



    private class LoginCustomerTask extends AsyncTask<Long, Integer, Integer> {

        @Override
        protected Integer doInBackground(Long... params) {

            try {
                publishProgress(1);
                loadCustomerInfo();


            } catch (Exception e) {
/*                String error;
                error = e.getMessage() + "\n";
                if(e.getCause() != null){
                    error = error + e.getCause().toString();
                }
                for(int i=0; i< e.getStackTrace().length; i++){
                    error = error +  e.getStackTrace()[i].toString()+ "\n";
                }
                ConstantsAdmin.mensaje = error;*/
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
            }
            return 0;

        }

        protected void onProgressUpdate(Integer... progress) {
            dialog = ProgressDialog.show(me, "",
                    getResources().getString(R.string.login_progress), true);
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(ConstantsAdmin.mensaje != null){
                createAlertDialog(ConstantsAdmin.mensaje,getResources().getString(R.string.atencion));
                ConstantsAdmin.mensaje = null;
                buttonLogin.setEnabled(true);

            }
            dialog.cancel();

        }
    }


    private void loadCustomerInfo() {
        final LoginActivity me = this;
        Call<List<Customer>> call = null;
        Response<List<Customer>> response;
        ArrayList<Customer> customers;

        try {
            ConstantsAdmin.mensaje = null;
            //   this.inicializarConexionServidor();
            call = customerService.loginCustomer(usrText, pswText, ConstantsAdmin.tokenFFL);
       //     call = customerService.loginCustomer("grassanoandrea@gmail.com", "bocha123", ConstantsAdmin.tokenFFL);
            response = call.execute();
            if(response.body() != null){
                customers = new ArrayList<>(response.body());
                if(customers.size() == 1){
                    Customer currentCustomer = customers.get(0);
                    Intent intent = new Intent(me, MainActivity.class);
                    intent.putExtra(ConstantsAdmin.currentCustomer, currentCustomer);
                    if(saveLogin.isChecked()){
                        ConstantsAdmin.createLogin(currentCustomer,me);
                    }else{
                        ConstantsAdmin.deleteLogin(me);
                    }
                    startActivity(intent);
                }else{
                    //createAlertDialog(getResources().getString(R.string.login_error), getResources().getString(R.string.atencion) );
                    ConstantsAdmin.mensaje = getResources().getString(R.string.login_error);

                }
            }else{
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
              /*  String error;
                if(response.message() != null) {
                    error = response.message();
                }else{
                    error = "Body is null";
                }
                ConstantsAdmin.mensaje = error;*/
            }
        }catch(Exception exc){
/*            String error;
            error = exc.getMessage() + "\n";
            if(exc.getCause() != null){
                error = error + exc.getCause().toString();
            }
            for(int i=0; i< exc.getStackTrace().length; i++){
                error = error +  exc.getStackTrace()[i].toString()+ "\n";
            }
            ConstantsAdmin.mensaje = error;*/
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


    private void createAlertDialog(String message, String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle(title);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        buttonLogin.setEnabled(true);
    }
}
