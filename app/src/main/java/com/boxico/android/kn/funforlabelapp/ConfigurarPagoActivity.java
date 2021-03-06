package com.boxico.android.kn.funforlabelapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.boxico.android.kn.funforlabelapp.dtos.MetodoPago;
import com.boxico.android.kn.funforlabelapp.services.UtilsService;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;
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

public class ConfigurarPagoActivity extends AppCompatActivity {

    private ConfigurarPagoActivity me;
    private List<MetodoPago> metodosPago;
    RadioGroup radioButtonsGroup;
    EditText entryComentario;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;
        setContentView(R.layout.configurar_pago);
        this.initializeService();
        this.configureWidgets();
      //  new LoadMetodosPagoTask().execute();
        Call<List<MetodoPago>> call = null;
        Response<List<MetodoPago>> response;

        try {
            ConstantsAdmin.mensaje = null;
            call = ConstantsAdmin.utilsService.getAllPaymentMethod(true, ConstantsAdmin.tokenFFL);
            response = call.execute();
            if(response.body() != null){
                metodosPago = new ArrayList<>(response.body());
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                configureRadioButtonsPayment();
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
/*

    private void loadMetodosPago() {
        Call<List<MetodoPago>> call = null;
        Response<List<MetodoPago>> response;

        try {
            ConstantsAdmin.mensaje = null;
            call = ConstantsAdmin.utilsService.getAllPaymentMethod(true, ConstantsAdmin.tokenFFL);
            response = call.execute();
            if(response.body() != null){
                metodosPago = new ArrayList<>(response.body());
            }else{
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
            }
        }catch(Exception exc){
            ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);

            if(call != null) {
                call.cancel();
            }

        }

    }*/
/*
    private class LoadMetodosPagoTask extends AsyncTask<Long, Integer, Integer> {


        private ProgressDialog dialog = null;
        private int resultado = -1;

        @Override
        protected Integer doInBackground(Long... longs) {
            publishProgress(1);
            loadMetodosPago();
            return 0;
        }

        protected void onProgressUpdate(Integer... progress) {
            dialog = ProgressDialog.show(me, "",
                    getResources().getString(R.string.loading_data), true);
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            if(dialog != null) {
                dialog.cancel();
            }
            configureRadioButtonsPayment();
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
        if(ConstantsAdmin.utilsService == null) {
            ConstantsAdmin.utilsService = retrofit.create(UtilsService.class);
        }

    }


    private void configureRadioButtonsPayment() {
        MetodoPago m;
        RadioButton rb;
        TextView txt;
        radioButtonsGroup = this.findViewById(R.id.opciones_pago);
        for (MetodoPago pago : metodosPago) {
            m = pago;
            rb = new RadioButton(this);
            rb.setText(m.getName());
            rb.setTextColor(Color.BLACK);
            rb.setTextSize(15);

            rb.setId((int) m.getId());
            rb.setTag(m);
            if (m.getId() == 1) {
                rb.setChecked(true);
            }
            radioButtonsGroup.addView(rb);
            if (m.getDescription() != null && !m.getDescription().equals("")) {
                txt = new TextView(this);
                txt.setText(m.getDescription());
                txt.setTextColor(Color.DKGRAY);
                txt.setTextSize(13);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(55, 0, 0, 15);
                txt.setLayoutParams(lp);
                radioButtonsGroup.addView(txt);
            }


        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(ConstantsAdmin.finalizarHastaMenuPrincipal){
            finish();
        }

    }

    private void configureWidgets() {

        TextView textWellcomeUsr = findViewById(R.id.textWellcomeUser);
        String result = getString(R.string.wellcomeUser) + " " + ConstantsAdmin.currentCustomer.getFirstName() + " " + ConstantsAdmin.currentCustomer.getLastName();
        textWellcomeUsr.setText(result);
        TextView textIntroPago = findViewById(R.id.textIntroPago);
      //  textIntroEnvio.setTypeface(Typeface.SANS_SERIF);
        if(ConstantsAdmin.currentLanguage== 1){
            textIntroPago.setText(ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.INTRO_PAGO_EN));
        }else{
            textIntroPago.setText(ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.INTRO_PAGO));
        }

        entryComentario = findViewById(R.id.entryCommentPago);
        if(ConstantsAdmin.comentarioIngresado != null && !ConstantsAdmin.comentarioIngresado.equals("")){
            String temp = ConstantsAdmin.comentarioIngresado + "\n";
            entryComentario.setText(temp);
        }
        Button btnGoToFinish = findViewById(R.id.btnConfirmarPago);
        btnGoToFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioButtonsGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);
                ConstantsAdmin.selectedPaymentMethod = (MetodoPago)radioButton.getTag();
                ConstantsAdmin.comentarioIngresado = entryComentario.getText().toString();
                Intent intent = new Intent(me, FinalizarCompraActivity.class);
                startActivity(intent);
            }
        });



        // configListView(listViewCarrito);
    }

}
