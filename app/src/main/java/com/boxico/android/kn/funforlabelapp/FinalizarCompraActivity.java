package com.boxico.android.kn.funforlabelapp;


import android.content.Intent;

import android.os.Bundle;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.boxico.android.kn.funforlabelapp.dtos.ComboCarrito;
import com.boxico.android.kn.funforlabelapp.dtos.Customer;
import com.boxico.android.kn.funforlabelapp.dtos.ItemCarrito;

import com.boxico.android.kn.funforlabelapp.dtos.ProductoCarrito;
import com.boxico.android.kn.funforlabelapp.services.OrdersService;

import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;
import com.boxico.android.kn.funforlabelapp.utils.workers.FinalizarCompraWorker;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.model.Payment;

import com.mercadopago.android.px.model.exceptions.MercadoPagoError;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FinalizarCompraActivity extends AppCompatActivity {

    private FinalizarCompraActivity me;
    EditText entryComentario;
    TextView textFormaPago;
    TextView textDirEnvio;
    TextView textFormaEnvio;
    TextView textDetalleTags;
    TextView textMontoSubtotal;
    TextView textFormaEnvioDetalle;
    TextView textMontoTotal;
    Button btnFinalizar;
    private float precioTotalTags;
    private float precioTotalEnvio;
    public Integer idOrder = -1;
    public boolean okInsert = true;
    private final Integer PAYMENT_REQUEST = 1001;
  //  final MercadoPagoCheckout checkout = new MercadoPagoCheckout.Builder("TEST-58494951-d07a-4350-af4e-0e069b4c6b5a", "243962506-0bb62e22-5c7b-425e-a0a6-c22d0f4758a9").build();
   // final MercadoPagoCheckout checkout = new MercadoPagoCheckout.Builder("8755027555974708", "9Eb4IgoOjpYfxftaSXTYeFtUyYUQeecU").build();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;
        setContentView(R.layout.finalizar_compra);
        this.initializeService();
        this.configureWidgets();
        ConstantsAdmin.compraExitosa = true;
        ConstantsAdmin.mensajeCompra= "";
    }


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
        if(ConstantsAdmin.orderService == null) {
            ConstantsAdmin.orderService = retrofit.create(OrdersService.class);
        }

    }


    private void configureWidgets() {
        textFormaPago = findViewById(R.id.textFormaPago);
        textDirEnvio = findViewById(R.id.textDirEnvio);
        textFormaEnvio = findViewById(R.id.textFormaEnvio);
        textDetalleTags = findViewById(R.id.textDetalleTags);
        textMontoSubtotal = findViewById(R.id.textMontoSubtotal);
        textMontoTotal = findViewById(R.id.textMontoTotal);
        textFormaEnvioDetalle = findViewById(R.id.textFormaEnvioDetalle);
        this.cargarDetalleTags();
        this.cargarDetalleEnvio();
        this.cargarDetallePago();
        TextView textWellcomeUsr = findViewById(R.id.textWellcomeUser);
        String result = getString(R.string.wellcomeUser) + " " + ConstantsAdmin.currentCustomer.getFirstName() + " " + ConstantsAdmin.currentCustomer.getLastName();
        textWellcomeUsr.setText(result);

      //  textIntroEnvio.setTypeface(Typeface.SANS_SERIF);
        String precioTotal = String.valueOf(precioTotalTags + precioTotalEnvio);
        precioTotal = getString(R.string.label_total) + " $" + precioTotal.substring(0, precioTotal.length() - 2);
        textMontoTotal.setText(precioTotal);

        entryComentario = findViewById(R.id.entryCommentPago);
        if(ConstantsAdmin.comentarioIngresado != null && !ConstantsAdmin.comentarioIngresado.equals("")){
            String result1 = ConstantsAdmin.comentarioIngresado + "\n";
            entryComentario.setText(result1);
        }
        btnFinalizar = findViewById(R.id.btnFinalizar);
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizarCompra();
            }
        });



        // configListView(listViewCarrito);
    }

    private void finalizarCompra() {
      //  new InsertarOrderTask().execute();
      /*  Call<Integer> call = null;s
        btnFinalizar.setEnabled(false);
        Response<Integer> response;
        Customer c = ConstantsAdmin.currentCustomer;
        AddressBook ab = ConstantsAdmin.addressCustomer;
        Date date= new Date();
        long time = date.getTime();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(time));
        Properties p =  ConstantsAdmin.fflProperties;
        MetodoEnvio me = ConstantsAdmin.selectedShippingMethod;

        try {
            ConstantsAdmin.mensaje = null;
            call = ConstantsAdmin.orderService.insertOrder(true, ConstantsAdmin.tokenFFL,(int) c.getId(),  c.getFirstName() + " " + c.getLastName(),
                    ab.getCalle(), ab.getSuburbio(), ab.getCiudad(), ab.getCp(), ab.getProvincia(),
                    ConstantsAdmin.ARGENTINA, c.getTelephone(), c.getEmail(), 1,c.getFirstName() + " " + c.getLastName(),
                    ab.getCalle(),ab.getSuburbio(), ab.getCiudad(), ab.getCp(), ab.getProvincia(), ConstantsAdmin.ARGENTINA,1,
                    c.getFirstName() + " " + c.getLastName(), ab.getCalle(), ab.getSuburbio(), ab.getCiudad(), ab.getCp(), ab.getProvincia(), ConstantsAdmin.ARGENTINA,1,
                    ConstantsAdmin.selectedPaymentMethod.getName() + "(" + ConstantsAdmin.selectedPaymentMethod.getDescription() + ")",
                    null, timeStamp, Integer.valueOf(p.getProperty(ConstantsAdmin.ORDER_STATUS_PENDING_TRANSFERENCE)),
                    p.getProperty(ConstantsAdmin.CURRENCY), Integer.valueOf(p.getProperty(ConstantsAdmin.CURRENCY_VALUE)), me.getName() + "(" + me.getDescription() + ")",(int)(precioTotalTags + precioTotalEnvio),
                    (int)precioTotalTags, (int)precioTotalEnvio,timeStamp,entryComentario.getText().toString());
            response = call.execute();
            if(response.body() != null){
                idOrder = response.body();
                this.insertarEtiquetas();
            }else{
                okInsert = false;
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
            }
        }catch(Exception exc){
            ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
            okInsert = false;
            if(call != null) {
                call.cancel();
            }

        }
*/
        LinearLayout layout = findViewById(R.id.finalizarLayout);
        //    buttonLogin.setEnabled(false);
        ProgressBar progressBar = null;
        progressBar = new ProgressBar(FinalizarCompraActivity.this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, 12,params);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        //    ScrollView sv = findViewById(R.id.scroll_view);

        Data inputData = new Data.Builder().putFloat("precioTotalTags", precioTotalTags).putFloat("precioTotalEnvio", precioTotalEnvio).putString("comentario", entryComentario.getText().toString()).build();

      /*  float precioTotalTags = myWorkerParams.getInputData().getFloat("precioTotalTags",0);
        float precioTotalEnvio = myWorkerParams.getInputData().getFloat("precioTotalEnvio", 0);
        String comentario = myWorkerParams.getInputData().getString("comentario");

*/
        ConstantsAdmin.activityTemp = this;
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(FinalizarCompraWorker.class)
                .setInputData(inputData)
                .setConstraints(constraints)
                .build();

        final ProgressBar finalProgressBar = progressBar;
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                            if (workInfo != null && workInfo.getState() == WorkInfo.State.RUNNING) {
                                finalProgressBar.setVisibility(View.VISIBLE);
                                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            finalProgressBar.setVisibility(View.GONE);
                            if(okInsert){// PUDO INSERTAR EN ORDERS
                                switch ((int)ConstantsAdmin.selectedPaymentMethod.getId()){
                                    case 1:// ES TRANSFERENCIA BANCARIA
                                        // new SendCustomerNotificationTask().execute();
                                        sendCustomerNotification();

                                        break;
                                    case 2:
                                        break;
                                    case 3: // ES MERCADOLIBRE
                                        redirigirAMercadoLibre();
                                    default:
                                        break;
                                }

                            }
                        }
                    }
                });
        ListenableFuture<Operation.State.SUCCESS> obj = WorkManager.getInstance(this).enqueue(request).getResult();









    }
/*
    private class InsertarOrderTask extends AsyncTask<Long, Integer, Integer> {


        private ProgressDialog dialog = null;
        private int resultado = -1;

        @Override
        protected Integer doInBackground(Long... longs) {
            publishProgress(1);
            insertarOrder();
            return 0;
        }

        protected void onProgressUpdate(Integer... progress) {
            dialog = ProgressDialog.show(me, "",
                    getResources().getString(R.string.loading_data), true);
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(okInsert){// PUDO INSERTAR EN ORDERS
                switch ((int)ConstantsAdmin.selectedPaymentMethod.getId()){
                    case 1:// ES TRANSFERENCIA BANCARIA
                        new SendCustomerNotificationTask().execute();
                        break;
                    case 2:
                        break;
                    case 3: // ES MERCADOLIBRE
                        redirigirAMercadoLibre();
                    default:
                        break;
                }

            }
            if(dialog != null) {
                dialog.cancel();
            }
        }

    }*/

    private void redirigirAMercadoLibre() {
        JSONObject jsonObject = new JSONObject();
        final JSONObject itemJSON1 = new JSONObject();
        final JSONObject itemJSON2 = new JSONObject();
        final JSONObject payerJSON = new JSONObject();
        JSONArray itemJsonArray = new JSONArray();
        Properties p = ConstantsAdmin.fflProperties;
        try {
            if(ConstantsAdmin.currentLanguage==1){
                itemJSON1.put("title", p.getProperty(ConstantsAdmin.TITULO_MP__DETALLE_TAGS_EN));
            }else{
                itemJSON1.put("title", p.getProperty(ConstantsAdmin.TITULO_MP__DETALLE_TAGS));
            }

            String desc = getDescripcionTagsLite();

            itemJSON1.put("description", desc);
            itemJSON1.put("quantity", 1);
            itemJSON1.put("currency_id", "ARS");
            itemJSON1.put("unit_price", precioTotalTags);
            itemJsonArray.put(itemJSON1);

            if(precioTotalEnvio > 0){
                desc = ConstantsAdmin.selectedShippingMethod.getName() +"(" + ConstantsAdmin.selectedShippingMethod.getDescription() + ")";
                if(ConstantsAdmin.currentLanguage== 1){
                    itemJSON2.put("title", p.getProperty(ConstantsAdmin.TITULO_MP__DETALLE_ENVIO_EN));
                }else {
                    itemJSON2.put("title", p.getProperty(ConstantsAdmin.TITULO_MP__DETALLE_ENVIO));
                }
                itemJSON2.put("description", desc);
                itemJSON2.put("quantity", 1);
                itemJSON2.put("currency_id", "ARS");
                itemJSON2.put("unit_price", precioTotalEnvio);
                itemJsonArray.put(itemJSON2);
            }

            JSONObject phoneJSON = new JSONObject();
            phoneJSON.put("area_code", "");
            phoneJSON.put("number", ConstantsAdmin.currentCustomer.getTelephone());
            JSONObject idJSON = new JSONObject();
            idJSON.put("type", "DNI");
            idJSON.put("number", "123456789");

            JSONObject addressJSON = new JSONObject();
            addressJSON.put("street_name", ConstantsAdmin.currentCustomer.getDireccion());
            addressJSON.put("street_number", 0);
            addressJSON.put("zip_code", ConstantsAdmin.currentCustomer.getCp());
            String fn =  ConstantsAdmin.currentCustomer.getFirstName();
            payerJSON.put("name", fn);
            payerJSON.put("surname", ConstantsAdmin.currentCustomer.getLastName());
            payerJSON.put("email", ConstantsAdmin.currentCustomer.getEmail());
            Date date= new Date();
            long time = date.getTime();
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(time));
            payerJSON.put("date_created", timeStamp);
            payerJSON.put("phone", phoneJSON);
            payerJSON.put("address", addressJSON);

            payerJSON.put("identification", idJSON);
            jsonObject.put("items", itemJsonArray);
            jsonObject.put("payer", payerJSON);
/*
            itemJSON.put("title", p.getProperty(ConstantsAdmin.TITULO_MP__DETALLE_TAGS));
            String descTags = getDescripcionTags();

            itemJSON.put("description", descTags);
            itemJSON.put("quantity", 1);
            itemJSON.put("currency_id", "ARS");
            itemJSON.put("unit_price", precioTotalTags);
            itemJsonArray.put(itemJSON);

            String precioText = String.valueOf(precioTotalEnvio);
            if(precioTotalEnvio > 0){
                precioText = precioText.substring(0, precioText.length() - 2);
            }
            descTags = descTags + ConstantsAdmin.selectedShippingMethod.getName() +"(" + ConstantsAdmin.selectedShippingMethod.getDescription() + "): $" + precioText;
            itemJSON.put("title", p.getProperty(ConstantsAdmin.TITULO_MECADO_PAGO));
            itemJSON.put("description", descTags);
            itemJSON.put("quantity", 1);
            itemJSON.put("currency_id", "ARS");
            itemJSON.put("unit_price", precioTotalTags + precioTotalEnvio);


            itemJsonArray.put(itemJSON);

            JSONObject phoneJSON = new JSONObject();
            phoneJSON.put("area_code", "");
            phoneJSON.put("number", ConstantsAdmin.currentCustomer.getTelephone());
            JSONObject idJSON = new JSONObject();
            idJSON.put("type", "DNI");
            idJSON.put("number", "123456789");
            JSONObject addressJSON = new JSONObject();
            addressJSON.put("street_name", ConstantsAdmin.currentCustomer.getDireccion());
            addressJSON.put("zip_code", ConstantsAdmin.currentCustomer.getCp());

            payerJSON.put("name", ConstantsAdmin.currentCustomer.getFirstName());
            payerJSON.put("surname", ConstantsAdmin.currentCustomer.getLastName());
            payerJSON.put("email", ConstantsAdmin.currentCustomer.getEmail());
            Date date= new Date();
            long time = date.getTime();
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(time));
            payerJSON.put("date_created", timeStamp);
            payerJSON.put("phone", phoneJSON);
            payerJSON.put("address", addressJSON);


            jsonObject.put("items", itemJsonArray);
            jsonObject.put("payer", payerJSON);*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //MIO
     //   String ACCESS_TOKEN_SANDBOX = "APP_USR-2650764728493246-072419-f5f9c713ef5a03660350e923ac9a3efc-143391108";
     //   final String PUBLIC_KEY_SANDBOX = "APP_USR-9e9e8795-313e-4db6-8e3d-167ef4a5d0cc";

        //GUI
     //   String ACCESS_TOKEN_SANDBOX = "APP_USR-8755027555974708-090911-7b66430095cb7d32cce6fc192bde1944__LD_LA__-78106585";
     //   final String PUBLIC_KEY_SANDBOX = "APP_USR-9c81d8db-230c-4a55-8985-c80946ab0274";

        String ACCESS_TOKEN_SANDBOX = ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ACCESS_TOKEN_SANDBOX);
        final String PUBLIC_KEY_SANDBOX = ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.PUBLIC_KEY_SANDBOX);
        RequestQueue requestQueue = null;
        try {
            requestQueue = Volley.newRequestQueue(this);
        }catch (Exception exc){
            exc.printStackTrace();
        }

        final String url = ConstantsAdmin.URL_MERCADO_PAGO +ACCESS_TOKEN_SANDBOX;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
              //      Log.i("debinf MainAct", "response JSONObject: "+response);
                    String checkoutPreferenceId = response.getString("id");
                    new MercadoPagoCheckout.Builder(PUBLIC_KEY_SANDBOX, checkoutPreferenceId).build().startPayment( me,PAYMENT_REQUEST);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
             //   Log.i("debinf MainAct", "response ERROR: "+error.networkResponse.allHeaders);
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);

/*
        final Item item = new Item.Builder("Orden de compra FunForLabels", 1, new BigDecimal(precioTotalTags + precioTotalEnvio)).setDescription("Las Etiquetas").build();
        CheckoutPreference checkoutPreference = new CheckoutPreference.Builder(Sites.ARGENTINA, ConstantsAdmin.currentCustomer.getEmail(), Collections.singletonList(item)).setDefaultInstallments(1).build();
        PaymentConfiguration paymentConfiguration = new PaymentConfiguration.Builder(new PaymentProcessor() {
            @Override
            public void startPayment(@NonNull CheckoutData data, @NonNull Context context, @NonNull OnPaymentListener paymentListener) {

            }

            @Override
            public int getPaymentTimeout() {
                return 0;
            }

            @Override
            public boolean shouldShowFragmentOnPayment() {
                return false;
            }

            @Nullable
            @Override
            public Bundle getFragmentBundle(@NonNull CheckoutData data, @NonNull Context context) {
                return null;
            }

            @Nullable
            @Override
            public Fragment getFragment(@NonNull CheckoutData data, @NonNull Context context) {
                return null;
            }
        }).build();
        new Builder("TEST-58494951-d07a-4350-af4e-0e069b4c6b5a", checkoutPreference, paymentConfiguration).build().startPayment(this, PAYMENT_REQUEST);*/
       //checkout.startPayment(this, PAYMENT_REQUEST);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYMENT_REQUEST) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                Payment payment = (Payment) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT);
                ConstantsAdmin.compraExitosa = true;
                ConstantsAdmin.mensajeCompra = payment.getPaymentStatus() + "(" + payment.getPaymentStatusDetail() + ")";
            } else if (resultCode == RESULT_CANCELED) {
                ConstantsAdmin.compraExitosa = false;
                if (data != null && data.getExtras() != null
                        && data.getExtras().containsKey(MercadoPagoCheckout.EXTRA_ERROR)) {
                    final MercadoPagoError mercadoPagoError =
                            (MercadoPagoError) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_ERROR);

                    ConstantsAdmin.mensajeCompra = mercadoPagoError.getMessage();

                    //  ((TextView) findViewById(R.id.mp_results)).setText("Error: " +  mercadoPagoError.getMessage());
                    //Resolve error in checkout
                } else {
                    //new RegistrarCancelacionMercadoPagoTask().execute();
                    registrarCancelacionMercadoPago();
                    ConstantsAdmin.mensajeCompra = getString(R.string.cancelo_compra);
                }
            }
            Intent intent = new Intent(me, CompraFinalizadaActivity.class);
            startActivity(intent);
        }
    }

    private void registrarCancelacionMercadoPago() {
        Call<Integer> call = null;
        Response<Integer> response;
        Properties p =  ConstantsAdmin.fflProperties;
      //  MetodoEnvio me = ConstantsAdmin.selectedShippingMethod;

        try {
            ConstantsAdmin.mensaje = null;
            call = ConstantsAdmin.orderService.updateOrder(true, ConstantsAdmin.tokenFFL,idOrder, Integer.valueOf(p.getProperty(ConstantsAdmin.ORDER_STATUS_MERCADO_PAGO_CANCELED)));
            response = call.execute();
            if(response.body() != null){
                idOrder = response.body();
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
    private class RegistrarCancelacionMercadoPagoTask extends AsyncTask<Long, Integer, Integer> {


        private ProgressDialog dialog = null;
        private int resultado = -1;

        @Override
        protected Integer doInBackground(Long... longs) {
            publishProgress(1);
            registrarCancelacionMercadoPago();
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

    }*/

/*
    MyReceiver receiver;
    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //You will receive the status -> PROCESSING when a payment is initiated...
            Log.d("Payment status", intent.getStringExtra(BundleCodes.STATUS));
        }
    }*/

/*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }*/
/*
    private void redirigirAMercadoLibre() {
        IntentFilter filter = new IntentFilter("com.mercadopago.merchant.PAYMENT_STATUS");
        receiver = new MyReceiver();
        registerReceiver(receiver, filter);

        Intent i = new Intent();

        i.setAction("com.mercadopago.PAYMENT_ACTION");

        Bundle bundle = new Bundle();

// AppId
        bundle.putString(BundleCodes.APP_ID, "8755027555974708");

// Secret
        bundle.putString(BundleCodes.APP_SECRET, "9Eb4IgoOjpYfxftaSXTYeFtUyYUQeecU");

        // App Fee
        bundle.putDouble(BundleCodes.APP_FEE, 10);

// Amount of transaction
        bundle.putDouble(BundleCodes.AMOUNT, Double.valueOf(precioTotalTags + precioTotalEnvio));

        // Description of transaction
        bundle.putString(BundleCodes.DESCRIPTION, "Compra en Fun For Labels App");

        bundle.putInt(BundleCodes.INSTALLMENTS, 1);

// Kiosk Mode
        bundle.putBoolean(BundleCodes.IS_KIOSK, true);

        if (isAvailable(i)) {
            // start activity for result
            i.putExtras(bundle);
            startActivityForResult(i, PAYMENT_REQUEST);
        } else {
            // send to google play.
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        }

    }
*/
 /*   public boolean isAvailable(Intent intent) {
        final PackageManager mgr = getPackageManager();
        List<ResolveInfo> list =
                mgr.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYMENT_REQUEST && data != null) {
            Intent intent = new Intent(this, ResultadoMLActivity.class);
            intent.putExtras(data.getExtras());
            startActivity(intent);
        }
    }
*/

/*

    private class SendCustomerNotificationTask extends AsyncTask<Long, Integer, Integer> {


        private ProgressDialog dialog = null;
        private int resultado = -1;

        @Override
        protected Integer doInBackground(Long... longs) {
            publishProgress(1);
            sendCustomerNotification();
            return 0;
        }

        protected void onProgressUpdate(Integer... progress) {
            dialog = ProgressDialog.show(me, "",
                    getResources().getString(R.string.sending_mail_progress), true);
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(dialog != null) {
                dialog.cancel();
            }
            Intent intent = new Intent(me, CompraFinalizadaActivity.class);
            startActivity(intent);

        }
    }
*/
    private String getDescripcionTags(){
        Iterator<ItemCarrito> it = ConstantsAdmin.productosDelCarrito.iterator();
        ItemCarrito ic;
        String temp = "";
        float precioTemp;
        while(it.hasNext()){
            ic = it.next();
            precioTemp = Float.parseFloat(ic.getPrecio());
            precioTemp = precioTemp * Float.parseFloat(ic.getCantidad());
            //String precio =pc.getPrecio().substring(0, pc.getPrecio().length() - 5);
            if(ic.getModelo() != null && !ic.getModelo().equals("")){
                temp = temp + ic.getCantidad() + " x " + ic.getNombre() + "(" + ic.getModelo() + "): $" + precioTemp + "\n";
            }else{
                temp = temp + ic.getCantidad() + " x " + ic.getNombre() +  ": $" + precioTemp + "\n";
            }

        }

        it = ConstantsAdmin.combosDelCarrito.iterator();
        while(it.hasNext()){
            ic = it.next();
            precioTemp = Float.parseFloat(ic.getPrecio());
            precioTemp = precioTemp * Float.parseFloat(ic.getCantidad());
            //String precio =pc.getPrecio().substring(0, pc.getPrecio().length() - 5);
            if(ic.getModelo() != null && !ic.getModelo().equals("")){
                temp = temp + ic.getCantidad() + " x " + ic.getNombre() + "(" + ic.getModelo() + "): $" + precioTemp + "\n";
            }else{
                temp = temp + ic.getCantidad() + " x " + ic.getNombre() +  ": $" + precioTemp + "\n";
            }
        }

        return temp;
    }

    private String getDescripcionTagsLite(){
        Iterator<ItemCarrito> it = ConstantsAdmin.productosDelCarrito.iterator();
        ItemCarrito ic;
        String temp = "";
        float precioTemp;
        while(it.hasNext()){
            ic = it.next();
            precioTemp = Float.parseFloat(ic.getPrecio());
            precioTemp = precioTemp * Float.parseFloat(ic.getCantidad());
            //String precio =pc.getPrecio().substring(0, pc.getPrecio().length() - 5);
            temp = temp + ic.getNombre() +": $" + precioTemp;
            if(it.hasNext()){
                temp =  temp + " + ";
            }
        }
        it = ConstantsAdmin.combosDelCarrito.iterator();
        while(it.hasNext()){
            ic = it.next();
            precioTemp = Float.parseFloat(ic.getPrecio());
            precioTemp = precioTemp * Float.parseFloat(ic.getCantidad());
            //String precio =pc.getPrecio().substring(0, pc.getPrecio().length() - 5);
            temp = temp + ic.getNombre() +": $" + precioTemp;
            if(it.hasNext()){
                temp =  temp + " + ";
            }
        }
        return temp;
    }


    private void sendCustomerNotification() {
        String body= "\n\n";
        Properties p = ConstantsAdmin.fflProperties;
        body = body + getString(R.string.app_name) + "\n" ;
        body = body + "-----------------------------------------------" + "\n\n";
        if(ConstantsAdmin.currentLanguage==1) {
            body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_NRO_EN) + idOrder + "\n";
            body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_FACTURA_DETALLE_EN) + p.getProperty(ConstantsAdmin.URL_DETALLE_ORDEN)+ idOrder + "\n";
            body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_FECHA_EN) + ConstantsAdmin.getFechaYHoraActual() + "\n";
            body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_COMENTARIO_EN) + "\n";
            body = body + entryComentario.getText().toString() +"\n";
            body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_PRODUCTOS_EN) + "\n";

        }else{
            body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_NRO) + idOrder + "\n";
            body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_FACTURA_DETALLE) + p.getProperty(ConstantsAdmin.URL_DETALLE_ORDEN)+ idOrder + "\n";
            body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_FECHA) + ConstantsAdmin.getFechaYHoraActual() + "\n";
            body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_COMENTARIO) + "\n";
            body = body + entryComentario.getText().toString() +"\n";
            body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_PRODUCTOS) + "\n";

        }
        body = body + "-----------------------------------------------" + "\n";

        String temp = getDescripcionTags();
        body = body + temp;
        /*Iterator<ProductoCarrito> it = ConstantsAdmin.productosDelCarrito.iterator();
        ProductoCarrito pc = null;
        float precioTemp;
        while(it.hasNext()){
            pc = it.next();
            precioTemp = Float.valueOf(pc.getPrecio());
            precioTemp = precioTemp * Float.valueOf(pc.getCantidad());
            //String precio =pc.getPrecio().substring(0, pc.getPrecio().length() - 5);
            body = body + pc.getCantidad() + " x " + pc.getNombre() + "(" + pc.getModelo() + "): $" + String.valueOf(precioTemp) + "\n";
        }*/
        body = body + "-----------------------------------------------" + "\n\n";
        String precioText = String.valueOf(precioTotalTags);
        precioText = precioText.substring(0, precioText.length() - 2);
        if(ConstantsAdmin.currentLanguage==1) {
            body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_SUBTOTAL_EN) + " $" + precioText + "\n";
        }else{
            body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_SUBTOTAL) + " $" + precioText + "\n";
        }
        precioText = String.valueOf(precioTotalEnvio);
        if(precioTotalEnvio > 0){
            precioText = precioText.substring(0, precioText.length() - 2);
        }
        body = body + ConstantsAdmin.selectedShippingMethod.getName() +"(" + ConstantsAdmin.selectedShippingMethod.getDescription() + "): $" + precioText + "\n";
        precioText = String.valueOf(precioTotalTags + precioTotalEnvio);
        precioText = precioText.substring(0, precioText.length() - 2);
        if(ConstantsAdmin.currentLanguage==1) {
            body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_TOTAL_EN) + " $" + precioText + "\n" ;
            body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_DIR_ENTREGA_EN) + "\n";
        }else{
            body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_TOTAL) + " $" + precioText + "\n" ;
            body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_DIR_ENTREGA) + "\n";

        }
        body = body + "-----------------------------------------------" + "\n";
        body = body + getStringDirEnvio() + "\n\n";
        if(ConstantsAdmin.currentLanguage==1) {
            body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_DIR_FACTURACION_EN) + "\n";
        }else{
            body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_DIR_FACTURACION) + "\n";
        }
        body = body + "-----------------------------------------------" + "\n";
        body = body + getStringDirEnvio() + "\n\n";
        if(ConstantsAdmin.currentLanguage==1) {
            body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_METODO_PAGO_EN) + "\n";
        }else{
            body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_METODO_PAGO) + "\n";
        }
        body = body + "-----------------------------------------------" + "\n";
        body = body + ConstantsAdmin.selectedPaymentMethod.getName() + "(" + ConstantsAdmin.selectedPaymentMethod.getDescription() + ")";

        String subject;
        if(ConstantsAdmin.currentLanguage == 1){
            subject = p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_SUBJECT_EN);
        }else{
            subject = p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_SUBJECT);
        }


        String to = ConstantsAdmin.currentCustomer.getEmail();
        ConstantsAdmin.enviarMail(subject, body, to);
        Intent intent = new Intent(me, CompraFinalizadaActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(ConstantsAdmin.finalizarHastaMenuPrincipal){
            finish();
        }

    }
/*
    private void insertarOrder() {
        Call<Integer> call = null;
        Response<Integer> response = null;
        Customer c = ConstantsAdmin.currentCustomer;
        AddressBook ab = ConstantsAdmin.addressCustomer;
        Date date= new Date();
        long time = date.getTime();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(time));
        Properties p =  ConstantsAdmin.fflProperties;
        MetodoEnvio me = ConstantsAdmin.selectedShippingMethod;

        try {
            ConstantsAdmin.mensaje = null;
            call = ConstantsAdmin.orderService.insertOrder(true, ConstantsAdmin.tokenFFL,(int) c.getId(),  c.getFirstName() + " " + c.getLastName(),
                    ab.getCalle(), ab.getSuburbio(), ab.getCiudad(), ab.getCp(), ab.getProvincia(),
                    ConstantsAdmin.ARGENTINA, c.getTelephone(), c.getEmail(), 1,c.getFirstName() + " " + c.getLastName(),
                    ab.getCalle(),ab.getSuburbio(), ab.getCiudad(), ab.getCp(), ab.getProvincia(), ConstantsAdmin.ARGENTINA,1,
                    c.getFirstName() + " " + c.getLastName(), ab.getCalle(), ab.getSuburbio(), ab.getCiudad(), ab.getCp(), ab.getProvincia(), ConstantsAdmin.ARGENTINA,1,
                    ConstantsAdmin.selectedPaymentMethod.getName() + "(" + ConstantsAdmin.selectedPaymentMethod.getDescription() + ")",
                    null, timeStamp, Integer.valueOf(p.getProperty(ConstantsAdmin.ORDER_STATUS_PENDING_TRANSFERENCE)),
                    p.getProperty(ConstantsAdmin.CURRENCY), Integer.valueOf(p.getProperty(ConstantsAdmin.CURRENCY_VALUE)), me.getName() + "(" + me.getDescription() + ")",(int)(precioTotalTags + precioTotalEnvio),
                    (int)precioTotalTags, (int)precioTotalEnvio,timeStamp,entryComentario.getText().toString());
            response = call.execute();
            if(response.body() != null){
                idOrder = response.body();
                this.insertarEtiquetas();
            }else{
                okInsert = false;
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
            }
        }catch(Exception exc){
            ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
            okInsert = false;
            if(call != null) {
                call.cancel();
            }

        }
    }
*/

    public void insertarEtiquetas() throws IOException {
        Call<Integer> call;
        Response<Integer> response;
        ProductoCarrito p;
        Customer c = ConstantsAdmin.currentCustomer;
        Properties prop = ConstantsAdmin.fflProperties;
        Iterator<ItemCarrito> it = ConstantsAdmin.productosDelCarrito.iterator();
        while(it.hasNext() && okInsert){
            p = (ProductoCarrito) it.next();
            String precio =p.getPrecio().substring(0, p.getPrecio().length() - 5);
            String imageName = ConstantsAdmin.takeScreenshot(p);
            if(p.isTieneTitulo()){// ES UN TAG DE TEXTO SIMPLE
                call = ConstantsAdmin.orderService.insertTagWithTitle(true, ConstantsAdmin.tokenFFL, idOrder, p.getIdProduct(),p.getModelo(),
                        p.getNombre(),Integer.parseInt(precio),Integer.parseInt(precio), 0, Integer.valueOf(p.getCantidad()),
                        p.getFillsTexturedId(),p.getComentarioUsr(),imageName,(int)c.getId(),p.getIdProduct(), 0,
                        "tcm/thumbs/" + imageName + ".png", 0, (int)p.getTextFontSize(),ConstantsAdmin.convertIntColorToHex(p.getFontTextColor()),0,
                        0,p.getFontTextId(),p.getTexto(), prop.getProperty(ConstantsAdmin.TAG_LEGEND_TYPE_TEXT),(int)p.getTitleFontSize(),
                        ConstantsAdmin.convertIntColorToHex(p.getFontTitleColor()),0,
                        0,p.getFontTitleId(),p.getTitulo(), prop.getProperty(ConstantsAdmin.TAG_LEGEND_TYPE_TITLE));
            }else{// ES UN TAG DE TEXTO COMPUESTO (TIENE TITLE)
                call = ConstantsAdmin.orderService.insertTag(true, ConstantsAdmin.tokenFFL, idOrder, p.getIdProduct(),p.getModelo(),
                        p.getNombre(),Integer.parseInt(precio), Integer.parseInt(precio), 0, Integer.valueOf(p.getCantidad()),
                        p.getFillsTexturedId(),p.getComentarioUsr(),imageName,(int)c.getId(),p.getIdProduct(), 0,
                        "tcm/thumbs/" + imageName + ".png", 0, (int)p.getTextFontSize(),ConstantsAdmin.convertIntColorToHex(p.getFontTextColor()),0,
                        0,p.getFontTextId(),p.getTexto(), prop.getProperty(ConstantsAdmin.TAG_LEGEND_TYPE_TEXT));
            }
            response = call.execute();
            if(response.body() == null){
                okInsert = false;
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
            }else{
                ConstantsAdmin.uploadFile(imageName + ".png");
               // this.almacenarImagenRemoto(p, imageName + ".png");
            }
        }
        it = ConstantsAdmin.combosDelCarrito.iterator();
        ComboCarrito combo;
        Integer idProduct = -1;
        String imageName;
        while(it.hasNext() && okInsert){
            combo = (ComboCarrito) it.next();
      //      imageNameCombo = ConstantsAdmin.takeScreenshot(this, combo);
            String precio =combo.getPrecio().substring(0, combo.getPrecio().length() - 5);
            call = ConstantsAdmin.orderService.insertProduct(true, ConstantsAdmin.tokenFFL, idOrder, combo.getIdProduct(),"",
                    combo.getNombre(),Integer.parseInt(precio),Integer.parseInt(precio), 0, Integer.valueOf(combo.getCantidad()));
            response = call.execute();
            if(response.body() != null) {
                idProduct = response.body();
            }else{
                okInsert = false;
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
            }
            // SE INSERTA UN TAG QUE REPRESENTA AL PADRE DEL COMBO
            Integer idParent = -1;
         /*   call = orderService.insertOnlyTagWithoutOthers(true,ConstantsAdmin.tokenFFL, idProduct, combo.getFillsTexturedId(),combo.getComentarioUsr(),imageName,(int)c.getId(),
                    combo.getIdProduct(), 0,"tcm/thumbs/" + imageName + ".png", 0);
*/          call = ConstantsAdmin.orderService.insertOnlyTagWithoutOthers(true,ConstantsAdmin.tokenFFL, idProduct, combo.getFillsTexturedId(),combo.getComentarioUsr(),"",(int)c.getId(),
                    combo.getIdProduct(), 0,"", 0);
            response = call.execute();
            if(response.body() == null) {
                okInsert = false;
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
            }else{
                idParent = response.body();
            }

            Iterator<ItemCarrito> it1 = combo.getProductos().iterator();
            ProductoCarrito pc;
            while (it1.hasNext() && okInsert){
                pc = (ProductoCarrito) it1.next();
                imageName = ConstantsAdmin.takeScreenshot(pc);
                if(pc.isTieneTitulo()){
                    call = ConstantsAdmin.orderService.insertOnlyTagWithTitle(true, ConstantsAdmin.tokenFFL, idProduct,pc.getFillsTexturedId(),pc.getComentarioUsr(),
                            "imageName",(int)c.getId(),pc.getIdProduct(), 0,
                            "tcm/thumbs/" + imageName + ".png", idParent, (int)pc.getTextFontSize(),ConstantsAdmin.convertIntColorToHex(pc.getFontTextColor()),0,
                            0,pc.getFontTextId(),pc.getTexto(), prop.getProperty(ConstantsAdmin.TAG_LEGEND_TYPE_TEXT),(int)pc.getTitleFontSize(),
                            ConstantsAdmin.convertIntColorToHex(pc.getFontTitleColor()),0,
                            0,pc.getFontTitleId(),pc.getTitulo(), prop.getProperty(ConstantsAdmin.TAG_LEGEND_TYPE_TITLE));
                }else{
                    call = ConstantsAdmin.orderService.insertOnlyTag(true,ConstantsAdmin.tokenFFL, idProduct, pc.getFillsTexturedId(),pc.getComentarioUsr(),"imageName",(int)c.getId(),
                            pc.getIdProduct(), 0,"tcm/thumbs/" + imageName + ".png", idParent, (int)pc.getTextFontSize(),ConstantsAdmin.convertIntColorToHex(pc.getFontTextColor()),
                            0,0,pc.getFontTextId(),pc.getTexto(), prop.getProperty(ConstantsAdmin.TAG_LEGEND_TYPE_TEXT));
                }
                response = call.execute();
                if(response.body() == null) {
                    okInsert = false;
                    ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                }else{
                    ConstantsAdmin.uploadFile(imageName + ".png");
                }
            }
         /*   if(okInsert){
                ConstantsAdmin.uploadFile(imageNameCombo + ".png");
            }*/


        }

        if(okInsert){// PUDO INSERTAR EN ORDERS
            switch ((int)ConstantsAdmin.selectedPaymentMethod.getId()){
                case 1:// ES TRANSFERENCIA BANCARIA
                   // new SendCustomerNotificationTask().execute();
                    sendCustomerNotification();

                    break;
                case 2:
                    break;
                case 3: // ES MERCADOLIBRE
                    redirigirAMercadoLibre();
                default:
                    break;
            }

        }
    }
/*
    private void almacenarImagenRemoto(ProductoCarrito p, String imageName) {
        new AlmacenarImagenRemotoTask().execute(imageName);
    }
*//*
    private class AlmacenarImagenRemotoTask extends AsyncTask<Object, Integer, Integer> {


        private ProgressDialog dialog = null;
        private int resultado = -1;


        @Override
        protected Integer doInBackground(Object... o) {
            publishProgress(1);
            String n = (String) o[0];
            almacenarImagenRemotoPrivado(n);
            return 0;
        }

        protected void onProgressUpdate(Integer... progress) {

        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }

    }
*//*
    private void almacenarImagenRemotoPrivado(String n) {
        ConstantsAdmin.uploadFile(n);
    }

*/
    private void cargarDetallePago() {
        String temp = "";
        temp = temp + ConstantsAdmin.selectedPaymentMethod.getName() + "\n";
        temp = temp + ConstantsAdmin.selectedPaymentMethod.getDescription() + "\n";
        textFormaPago.setText(temp);

    }

    private void cargarDetalleEnvio() {
        String temp = getStringDirEnvio();
        textDirEnvio.setText(temp);
        temp = ConstantsAdmin.selectedShippingMethod.getName() + ": $" + ConstantsAdmin.selectedShippingMethod.getPrice() +  "\n";
        textFormaEnvio.setText(temp);
        temp = ConstantsAdmin.selectedShippingMethod.getDescription();
        textFormaEnvioDetalle.setText(temp);
        precioTotalEnvio = Float.parseFloat(ConstantsAdmin.selectedShippingMethod.getPrice());
    }


    private String getStringDirEnvio(){
        String temp;
        Customer c = ConstantsAdmin.currentCustomer;
        temp = c.getFirstName() + " " + c.getLastName() + "\n";
        temp = temp + ConstantsAdmin.addressCustomer.getCalle() + "\n";
        if(ConstantsAdmin.addressCustomer.getSuburbio() != null && !ConstantsAdmin.addressCustomer.getSuburbio().equals("")){
            temp = temp + ConstantsAdmin.addressCustomer.getSuburbio() + "\n";
        }
        temp = temp + ConstantsAdmin.addressCustomer.getCiudad() + ", " + ConstantsAdmin.addressCustomer.getCp() + "\n";
        temp = temp + ConstantsAdmin.addressCustomer.getProvincia() + ", " + ConstantsAdmin.GEOCODIGOARGENTINA;
        return temp;
    }

    private void cargarDetalleTags() {
        ItemCarrito ic;
        String temp = "";
        precioTotalTags = 0;
        float precioTemp;
        ArrayList<ItemCarrito> items = new ArrayList<>();
        items.addAll(ConstantsAdmin.productosDelCarrito);
        items.addAll(ConstantsAdmin.combosDelCarrito);
        for (ItemCarrito item : items) {
            ic = item;
            precioTemp = (Float.parseFloat(ic.getPrecio()) * Float.parseFloat(ic.getCantidad()));
//            precio = pc.getPrecio().substring(0, pc.getPrecio().length() - 5);
            temp = temp + "-" + ic.getCantidad() + " x " + ic.getNombre() + ": $" + precioTemp + "\n";
            precioTotalTags = precioTotalTags + precioTemp;
        }

        textDetalleTags.setPadding(3,3,3,3);
        textDetalleTags.setText(temp);
        String precioText = String.valueOf(precioTotalTags);
        precioText = getString(R.string.label_total_etiquetas) + "$" + precioText.substring(0, precioText.length() - 2);
        textMontoSubtotal.setText(precioText);

    }

}
