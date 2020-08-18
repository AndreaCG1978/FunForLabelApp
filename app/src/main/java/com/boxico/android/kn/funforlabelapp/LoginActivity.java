package com.boxico.android.kn.funforlabelapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.boxico.android.kn.funforlabelapp.ddbb.DataBaseManager;
import com.boxico.android.kn.funforlabelapp.dtos.Customer;
import com.boxico.android.kn.funforlabelapp.services.CustomerService;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;
import com.boxico.android.kn.funforlabelapp.utils.workers.LoadPropertiesWorker;
import com.boxico.android.kn.funforlabelapp.utils.PasswordGenerator;
import com.boxico.android.kn.funforlabelapp.utils.workers.LoginCustomerWorker;
import com.boxico.android.kn.funforlabelapp.utils.workers.SendMailWorker;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends FragmentActivity {


    private EditText userEntry = null;
    private EditText passEntry = null;
    private Button buttonLogin = null;

    private TextView recuperarContrasenia = null;
    private String pswText;
    private String usrText;
    private LoginActivity me;
    private CheckBox saveLogin = null;
    private ImageButton hiddeShowPass;
    private boolean isShowingPass = false;
    //private Customer currentCustomer;
    private String nuevaContrasenia;
    private Customer customerTemp;
    private final int PERMISSIONS_WRITE_STORAGE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        me = this;
        this.configureWidgets();
        this.initializeService();
        this.initializeDataBase();
        this.initializeLogin();
      //  this.loadProperties();
        this.askForWriteStoragePermission();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //   mCameraPermissionGranted = false;


        if (requestCode == PERMISSIONS_WRITE_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.loadProperties();
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
                this.loadProperties();
            }
        } else {
            this.loadProperties();
        }


    }




    private void loadProperties() {
        Data inputData = new Data.Builder().build();
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(LoadPropertiesWorker.class)
                .setInputData(inputData)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(this).enqueue(request);

//        new LoadPropertiesTask().execute();
        //ConstantsAdmin.privateLoadProperties();
    }


    private void initializeDataBase(){
        DataBaseManager mDBManager = DataBaseManager.getInstance(this);
        ConstantsAdmin.inicializarBD(mDBManager);
        ConstantsAdmin.createBD(mDBManager);
        ConstantsAdmin.finalizarBD(mDBManager);
    }

    private void initializeLogin() {
        ConstantsAdmin.currentCustomer = ConstantsAdmin.getLogin(this);
        if(ConstantsAdmin.currentCustomer != null){
            userEntry.setText(ConstantsAdmin.currentCustomer.getEmail());
            passEntry.setText(ConstantsAdmin.currentCustomer.getNotEncriptedPassword());
            saveLogin.setChecked(true);
        }

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
        TextView crearCuentaText = findViewById(R.id.crearCuentaText);
        recuperarContrasenia = findViewById(R.id.recuperarContrasenia);
        crearCuentaText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configurarCrearCuenta();
            }
        });
        this.configurarReenviarPass();
        saveLogin = findViewById(R.id.checkSaveLogin);
        hiddeShowPass = findViewById(R.id.imagenShowPassword);
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
        });

    /*    userEntry.setText("acgrassano1978@gmail.com");
        passEntry.setText("andrea");*/
    }

    private void configurarReenviarPass(){
        recuperarContrasenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getCustomer();


            }
        });
    }

    private void configurarCrearCuenta(){
        Intent intent = new Intent(me, CustomerActivity.class);
        startActivity(intent);
    }


    private boolean reenviarContrasenia(){
        boolean okSend = false;
        String body = "";
        body = body + ConstantsAdmin.ENTER + ConstantsAdmin.ENTER;
        body = body + this.getString(R.string.body1_nueva_contrasenia);
        body = body + ConstantsAdmin.ENTER + ConstantsAdmin.ENTER + ConstantsAdmin.TAB;
        body = body + this.getString(R.string.contrasenia) + nuevaContrasenia;
        body = body + ConstantsAdmin.ENTER + ConstantsAdmin.ENTER;
        body = body + this.getString(R.string.body2_nueva_contrasenia);
        try {
        //    okSend =ConstantsAdmin.enviarMail(this.getString(R.string.app_name) + " - " + this.getString(R.string.title_nueva_contrasenia), body, customerTemp.getEmail());


            // CREO EL PROGRESS BAR

            LinearLayout layout = findViewById(R.id.loginLayout);
            final ProgressBar progressBar = new ProgressBar(LoginActivity.this, null, android.R.attr.progressBarStyleLarge);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            layout.addView(progressBar, 1,params);
            ScrollView sv = findViewById(R.id.scroll_view);

            Data inputData = new Data.Builder()
                    .putString("to", customerTemp.getEmail())
                    .putString("subject", this.getString(R.string.app_name) + " - " + this.getString(R.string.title_nueva_contrasenia))
                    .putString("body", body)
                    .build();
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();
            OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(SendMailWorker.class)
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
                                createAlertDialog(me.getString(R.string.send_mail_succes) + customerTemp.getEmail(),"");
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                progressBar.setVisibility(View.GONE);
                                passEntry.setText("");
                            }
                        }
                    });
            ListenableFuture<Operation.State.SUCCESS> obj = WorkManager.getInstance(this).enqueue(request).getResult();
            passEntry.setText("");
/*

            if(obj != null && obj.isDone()){
                ConstantsAdmin.mensaje = me.getString(R.string.send_mail_succes) + customerTemp.getEmail();
                okSend = true;
                createAlertDialog(ConstantsAdmin.mensaje,"");
                passEntry.setText("");
            }*/
        } catch(Exception e) {
                //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
            ConstantsAdmin.mensaje = me.getString(R.string.send_mail_error);
        }

        return okSend;

    }
/*
    private class SendMail extends AsyncTask<Long, Integer, Integer> {


        private ProgressDialog dialog = null;

        @Override
        protected Integer doInBackground(Long... longs) {
            publishProgress(1);
            reenviarContrasenia();
            return 0;
        }

        protected void onProgressUpdate(Integer... progress) {
            dialog = ProgressDialog.show(me, "",
                    getResources().getString(R.string.sending_mail_progress), true);
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            createAlertDialog(ConstantsAdmin.mensaje,"");
            passEntry.setText("");
            if(dialog != null) {
                dialog.cancel();
            }
        }
    }*/
/*
    private class LoadPropertiesTask extends AsyncTask<Long, Integer, Integer> {


        private ProgressDialog dialog = null;

        @Override
        protected Integer doInBackground(Long... longs) {
            publishProgress(1);
            ConstantsAdmin.privateLoadProperties();
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
        }
    }

*/

    private String crearNuevaContrasenia() {

        String temp = PasswordGenerator.getPassword(
                PasswordGenerator.MINUSCULAS+
                        PasswordGenerator.MAYUSCULAS+
                        PasswordGenerator.ESPECIALES,6);

        return temp;
    }


    private void getCustomer() {
        usrText = userEntry.getText().toString();
        if (!usrText.equals("")) {
            customerTemp = null;
            getCustomerInfo();

            //new GetCustomerTask().execute();
        }else{
           createAlertDialog(getString(R.string.campo_mail_no_valido), getString(R.string.atencion));
        }

    }

    private void loginCustomer() {

        usrText = userEntry.getText().toString();
        pswText = passEntry.getText().toString();

        if (!usrText.equals("") && (!pswText.equals(""))) {

            //new LoginCustomerTask().execute();f
            LinearLayout layout = findViewById(R.id.loginLayout);
            final ProgressBar progressBar = new ProgressBar(LoginActivity.this, null, android.R.attr.progressBarStyleLarge);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            layout.addView(progressBar, 1,params);
            ScrollView sv = findViewById(R.id.scroll_view);

            Data inputData = new Data.Builder().putString("usrText", usrText).putString("pswText", pswText).build();


            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();
            OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(LoginCustomerWorker.class)
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
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                progressBar.setVisibility(View.GONE);
                                if(ConstantsAdmin.currentCustomer != null){
                                    Intent intent = new Intent(me, MainActivity.class);
                                    //      intent.putExtra(ConstantsAdmin.currentCustomer, currentCustomer);
                                    if(saveLogin.isChecked()){
                                        ConstantsAdmin.currentCustomer.setNotEncriptedPassword(pswText);
                                        ConstantsAdmin.createLogin(ConstantsAdmin.currentCustomer,me);
                                    }else{
                                        ConstantsAdmin.deleteLogin(me);
                                    }
                                    ConstantsAdmin.customerJustCreated = false;
                                    startActivity(intent);
                                }else{
                                    createAlertDialog(getString(R.string.conexion_server_error), getString(R.string.atencion));
                                    ConstantsAdmin.mensaje = null;
                                    buttonLogin.setEnabled(true);
                                }

                            }
                        }
                    });
            ListenableFuture<Operation.State.SUCCESS> obj = WorkManager.getInstance(this).enqueue(request).getResult();






            //loadCustomerInfo();

        } else {
            createAlertDialog(getResources().getString(R.string.login_error), getResources().getString(R.string.atencion));
        }

    }

/*

    private class LoginCustomerTask extends AsyncTask<Long, Integer, Integer> {
        private ProgressDialog dialog = null;
        @Override
        protected Integer doInBackground(Long... params) {

            try {
                publishProgress(1);
                loadCustomerInfo();


            } catch (Exception e) {

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
*/

/*

    private class UpdatePasswordTask extends AsyncTask<Long, Integer, Integer> {
        private ProgressDialog dialog = null;

        @Override
        protected Integer doInBackground(Long... params) {

            try {
                saveNewPassword();
            } catch (Exception e) {
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
            }
            return 0;

        }


        @Override
        protected void onPostExecute(Integer result) {
            if(ConstantsAdmin.mensaje != null){
                createAlertDialog(ConstantsAdmin.mensaje,getResources().getString(R.string.atencion));
                ConstantsAdmin.mensaje = null;
                buttonLogin.setEnabled(true);

            }else{
                Long[] params = new Long[1];
                params[0] = 1L;
                new SendMail().execute(params);
            }
            if(dialog != null){
                dialog.cancel();
            }

        }
    }*/
/*

    private class GetCustomerTask extends AsyncTask<Long, Integer, Integer> {

        @Override
        protected Integer doInBackground(Long... params) {

            try {
                getCustomerInfo();
            } catch (Exception e) {
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
            }
            return 0;

        }


        @Override
       protected void onPostExecute(Integer result) {
            if(customerTemp != null){
                AlertDialog.Builder builder = new AlertDialog.Builder(me);
                builder.setMessage(me.getString(R.string.mensaje_enviar_contrasenia) + customerTemp.getEmail() + me.getString(R.string.mensaje_desea_continuar))
                        .setCancelable(true)
                        .setPositiveButton(R.string.label_si, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Long[] params = new Long[1];
                                params[0] = 1L;
                                nuevaContrasenia = me.crearNuevaContrasenia();
                                new UpdatePasswordTask().execute(params);


                            }
                        })
                        .setNegativeButton(R.string.label_no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.show();
            }else{
                createAlertDialog(me.getResources().getString(R.string.usuario_inexistente), me.getResources().getString(R.string.atencion));
                ConstantsAdmin.mensaje = null;
                buttonLogin.setEnabled(true);
            }
        }
    }*/

    private void saveNewPassword(){
        final LoginActivity me = this;
        Call<ResponseBody>  call = null;


        try {
            ConstantsAdmin.mensaje = null;
            call = ConstantsAdmin.customerService.updatePasswordCustomer(customerTemp.getEmail(), nuevaContrasenia, ConstantsAdmin.tokenFFL);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response != null && response.body() != null){
                            //new SendMail().execute(params);
                        reenviarContrasenia();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    createAlertDialog(me.getString(R.string.conexion_server_error),me.getString(R.string.atencion));
                }
            });
         //   Response<ResponseBody> respuesta = call.execute();
           /* if(respuesta != null && respuesta.body() != null){
                exito = true;
            }*/
        }catch(Exception exc){
            ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
            if(call != null) {
                call.cancel();
            }

        }

    }



    private void loadCustomerInfo() {
        final LoginActivity me = this;
        Call<List<Customer>> call = null;
        Response<List<Customer>> response;
        final ArrayList<Customer> customers =  new ArrayList<>();
       // customers = null;

        try {

            ConstantsAdmin.mensaje = null;
            call = ConstantsAdmin.customerService.loginCustomer(usrText, pswText, ConstantsAdmin.tokenFFL);

            call.enqueue(new Callback<List<Customer>>() {
                 @Override
                 public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                     if(response.body() != null){
                         customers.addAll(response.body());
                         if(customers.size() == 1){
                             ConstantsAdmin.currentCustomer = customers.get(0);
                             Intent intent = new Intent(me, MainActivity.class);
                             //      intent.putExtra(ConstantsAdmin.currentCustomer, currentCustomer);
                             ConstantsAdmin.currentCustomer = ConstantsAdmin.currentCustomer;
                             if(saveLogin.isChecked()){
                                 ConstantsAdmin.currentCustomer.setNotEncriptedPassword(pswText);
                                 ConstantsAdmin.createLogin(ConstantsAdmin.currentCustomer,me);
                             }else{
                                 ConstantsAdmin.deleteLogin(me);
                             }
                             ConstantsAdmin.customerJustCreated = false;
                             startActivity(intent);
                         }else{
                             //createAlertDialog(getResources().getString(R.string.login_error), getResources().getString(R.string.atencion) );
                             ConstantsAdmin.mensaje = getResources().getString(R.string.login_error);

                         }
                     }else{
                         ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                     }
                     if(ConstantsAdmin.mensaje != null){
                         createAlertDialog(ConstantsAdmin.mensaje,getResources().getString(R.string.atencion));
                         ConstantsAdmin.mensaje = null;
                         buttonLogin.setEnabled(true);
                     }

                 }

                 @Override
                 public void onFailure(Call<List<Customer>> call, Throwable t) {
                     ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                 }
             });




        }catch(Exception exc){
            ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
            if(call != null) {
                call.cancel();
            }

        }


    }


    private void getCustomerInfo() {
        final LoginActivity me = this;
        Call<List<Customer>> call = null;
        Response<List<Customer>> response;
        final ArrayList<Customer> customers =  new ArrayList<>();
        try {
            ConstantsAdmin.mensaje = null;
            call = ConstantsAdmin.customerService.getCustomer(usrText, ConstantsAdmin.tokenFFL);
            call.enqueue(new Callback<List<Customer>>() {
                @Override
                public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                    if(response.body() != null){
                        customers.addAll(response.body());
                        if(customers.size() == 1){
                            customerTemp = customers.get(0);
                            if(customerTemp != null){
                                AlertDialog.Builder builder = new AlertDialog.Builder(me);
                                builder.setMessage(me.getString(R.string.mensaje_enviar_contrasenia) + customerTemp.getEmail() + me.getString(R.string.mensaje_desea_continuar))
                                        .setCancelable(true)
                                        .setPositiveButton(R.string.label_si, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Long[] params = new Long[1];
                                                params[0] = 1L;
                                                nuevaContrasenia = me.crearNuevaContrasenia();
                                                //  new UpdatePasswordTask().execute(params);
                                                saveNewPassword();



                                            }
                                        })
                                        .setNegativeButton(R.string.label_no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                builder.show();
                            }else{
                                createAlertDialog(me.getResources().getString(R.string.usuario_inexistente), me.getResources().getString(R.string.atencion));
                                ConstantsAdmin.mensaje = null;
                                buttonLogin.setEnabled(true);
                            }



                        }else{
                            createAlertDialog(me.getResources().getString(R.string.usuario_inexistente), me.getResources().getString(R.string.atencion));
                            ConstantsAdmin.mensaje = null;
                            buttonLogin.setEnabled(true);

                        }
                    }else{
                        createAlertDialog(me.getResources().getString(R.string.conexion_server_error), me.getResources().getString(R.string.atencion));
                        ConstantsAdmin.mensaje = null;
                        buttonLogin.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<List<Customer>> call, Throwable t) {
                    createAlertDialog(me.getResources().getString(R.string.conexion_server_error), me.getResources().getString(R.string.atencion));
                    ConstantsAdmin.mensaje = null;
                    buttonLogin.setEnabled(true);
                }
            });



        }catch(Exception exc){
            createAlertDialog(me.getResources().getString(R.string.conexion_server_error), me.getResources().getString(R.string.atencion));
            ConstantsAdmin.mensaje = null;
            buttonLogin.setEnabled(true);
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
        if(ConstantsAdmin.customerService == null){
            ConstantsAdmin.customerService = retrofit.create(CustomerService.class);
        }

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
        if(ConstantsAdmin.currentCustomer != null && ConstantsAdmin.customerJustCreated){
            userEntry.setText(ConstantsAdmin.currentCustomer.getEmail());
            passEntry.setText("");
            saveLogin.setChecked(false);
            createAlertDialog(ConstantsAdmin.mensaje,"");
            ConstantsAdmin.mensaje = null;
        }
    }
}
