package com.boxico.android.kn.funforlabelapp;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.boxico.android.kn.funforlabelapp.dtos.AddressBook;
import com.boxico.android.kn.funforlabelapp.dtos.Customer;
import com.boxico.android.kn.funforlabelapp.dtos.LabelImage;
import com.boxico.android.kn.funforlabelapp.dtos.MetodoEnvio;
import com.boxico.android.kn.funforlabelapp.dtos.ProductoCarrito;
import com.boxico.android.kn.funforlabelapp.services.OrdersService;
import com.boxico.android.kn.funforlabelapp.utils.BundleCodes;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mercadopago.android.px.configuration.PaymentConfiguration;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.core.MercadoPagoCheckout.Builder;
import com.mercadopago.android.px.core.PaymentProcessor;
import com.mercadopago.android.px.model.Item;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.Sites;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import com.mercadopago.android.px.preferences.CheckoutPreference;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
    private TextView textWellcomeUsr;
    private OrdersService orderService;
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
    Integer idOrder = -1;
    private boolean okInsert = true;
    private Integer PAYMENT_REQUEST = 1001;
    final MercadoPagoCheckout checkout = new MercadoPagoCheckout.Builder("TEST-58494951-d07a-4350-af4e-0e069b4c6b5a", "243962506-0bb62e22-5c7b-425e-a0a6-c22d0f4758a9").build();
   // final MercadoPagoCheckout checkout = new MercadoPagoCheckout.Builder("8755027555974708", "9Eb4IgoOjpYfxftaSXTYeFtUyYUQeecU").build();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;
        setContentView(R.layout.finalizar_compra);
        this.initializeService();
        this.configureWidgets();

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
        orderService = retrofit.create(OrdersService.class);

    }


    private void configureWidgets() {
        textFormaPago = (TextView) findViewById(R.id.textFormaPago);
        textDirEnvio = (TextView) findViewById(R.id.textDirEnvio);
        textFormaEnvio = (TextView) findViewById(R.id.textFormaEnvio);
        textDetalleTags = (TextView) findViewById(R.id.textDetalleTags);
        textMontoSubtotal = (TextView) findViewById(R.id.textMontoSubtotal);
        textMontoTotal = (TextView) findViewById(R.id.textMontoTotal);
        textFormaEnvioDetalle = (TextView) findViewById(R.id.textFormaEnvioDetalle);
        this.cargarDetalleTags();
        this.cargarDetalleEnvio();
        this.cargarDetallePago();
        textWellcomeUsr = findViewById(R.id.textWellcomeUser);
        textWellcomeUsr.setText(getString(R.string.wellcomeUser) + " " + ConstantsAdmin.currentCustomer.getFirstName() + " " + ConstantsAdmin.currentCustomer.getLastName());

      //  textIntroEnvio.setTypeface(Typeface.SANS_SERIF);
        String precioTotal = String.valueOf(precioTotalTags + precioTotalEnvio);
        precioTotal = precioTotal.substring(0, precioTotal.length() - 2);
        textMontoTotal.setText(getString(R.string.label_total) + " $" + precioTotal);

        entryComentario = (EditText) findViewById(R.id.entryCommentPago);
        if(ConstantsAdmin.comentarioIngresado != null && !ConstantsAdmin.comentarioIngresado.equals("")){
            entryComentario.setText(ConstantsAdmin.comentarioIngresado + "\n");
        }
        btnFinalizar = (Button) findViewById(R.id.btnFinalizar);
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizarCompra();
            }
        });



        // configListView(listViewCarrito);
    }

    private void finalizarCompra() {
        new InsertarOrderTask().execute();
    }

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

    }

    private void redirigirAMercadoLibre() {
        JSONObject jsonObject = new JSONObject();
        final JSONObject itemJSON1 = new JSONObject();
        final JSONObject itemJSON2 = new JSONObject();
        final JSONObject payerJSON = new JSONObject();
        JSONArray itemJsonArray = new JSONArray();
        Properties p = ConstantsAdmin.fflProperties;
        try {
            itemJSON1.put("title", p.getProperty(ConstantsAdmin.TITULO_MP__DETALLE_TAGS));
            String desc = getDescripcionTags();

            itemJSON1.put("description", desc);
            itemJSON1.put("quantity", 1);
            itemJSON1.put("currency_id", "ARS");
            itemJSON1.put("unit_price", precioTotalTags);
            itemJsonArray.put(itemJSON1);

            if(precioTotalEnvio > 0){
                desc = ConstantsAdmin.selectedShippingMethod.getName() +"(" + ConstantsAdmin.selectedShippingMethod.getDescription() + ")";
                itemJSON2.put("title", p.getProperty(ConstantsAdmin.TITULO_MP__DETALLE_ENVIO));
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

            payerJSON.put("name", ConstantsAdmin.currentCustomer.getFirstName());
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
        String ACCESS_TOKEN_SANDBOX = "APP_USR-8755027555974708-090911-7b66430095cb7d32cce6fc192bde1944__LD_LA__-78106585";
        final String PUBLIC_KEY_SANDBOX = "APP_USR-9c81d8db-230c-4a55-8985-c80946ab0274";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
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
             //   Log.i("debinf MainAct", "response ERROR: "+error.networkResponse.allHeaders);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
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
                    ConstantsAdmin.mensajeCompra = getString(R.string.cancelo_compra);
                }
            }
            Intent intent = new Intent(me, CompraFinalizadaActivity.class);
            startActivity(intent);
        }
    }
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

    private String getDescripcionTags(){
        Iterator<ProductoCarrito> it = ConstantsAdmin.productosDelCarrito.iterator();
        ProductoCarrito pc = null;
        String temp = "";
        float precioTemp;
        while(it.hasNext()){
            pc = it.next();
            precioTemp = Float.valueOf(pc.getPrecio());
            precioTemp = precioTemp * Float.valueOf(pc.getCantidad());
            //String precio =pc.getPrecio().substring(0, pc.getPrecio().length() - 5);
            temp = temp + pc.getCantidad() + " x " + pc.getNombre() + "(" + pc.getModelo() + "): $" + String.valueOf(precioTemp) + "\n";
        }
        return temp;
    }

    private void sendCustomerNotification() {
        String body= "\n\n";
        Properties p = ConstantsAdmin.fflProperties;
        body = body + getString(R.string.app_name) + "\n" ;
        body = body + "-----------------------------------------------" + "\n\n";
        body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_NRO) + idOrder + "\n";
        body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_FACTURA_DETALLE) + p.getProperty(ConstantsAdmin.URL_DETALLE_ORDEN)+ idOrder + "\n";
        body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_FECHA) + ConstantsAdmin.getFechaYHoraActual() + "\n";
        body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_COMENTARIO) + "\n";
        body = body + entryComentario.getText().toString() +"\n";
        body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_PRODUCTOS) + "\n";
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
        body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_SUBTOTAL) + " $" + precioText + "\n";
        precioText = String.valueOf(precioTotalEnvio);
        if(precioTotalEnvio > 0){
            precioText = precioText.substring(0, precioText.length() - 2);
        }
        body = body + ConstantsAdmin.selectedShippingMethod.getName() +"(" + ConstantsAdmin.selectedShippingMethod.getDescription() + "): $" + precioText + "\n";
        precioText = String.valueOf(precioTotalTags + precioTotalEnvio);
        precioText = precioText.substring(0, precioText.length() - 2);
        body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_TOTAL) + " $" + precioText + "\n" ;
        body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_DIR_ENTREGA) + "\n";
        body = body + "-----------------------------------------------" + "\n";
        body = body + getStringDirEnvio() + "\n\n";
        body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_DIR_FACTURACION) + "\n";
        body = body + "-----------------------------------------------" + "\n";
        body = body + getStringDirEnvio() + "\n\n";
        body = body + p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_METODO_PAGO) + "\n";
        body = body + "-----------------------------------------------" + "\n";
        body = body + ConstantsAdmin.selectedPaymentMethod.getName() + "(" + ConstantsAdmin.selectedPaymentMethod.getDescription() + ")";

        String subject = p.getProperty(ConstantsAdmin.MAIL_PROCESO_ORDEN_SUBJECT);

        String to = ConstantsAdmin.currentCustomer.getEmail();
        ConstantsAdmin.enviarMail(subject, body, to);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(ConstantsAdmin.finalizarHastaMenuPrincipal){
            finish();
        }

    }

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
            this.generarBitmapsDeTags();
            ConstantsAdmin.mensaje = null;
            call = orderService.insertOder(true, ConstantsAdmin.tokenFFL,(int) c.getId(),  c.getFirstName() + " " + c.getLastName(),
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

    private void generarBitmapsDeTags() {
    }

    private void insertarEtiquetas() throws IOException {
        Call<Integer> call = null;
        Response<Integer> response = null;
        ProductoCarrito p;
        Customer c = ConstantsAdmin.currentCustomer;
        Properties prop = ConstantsAdmin.fflProperties;
        Iterator<ProductoCarrito> it = ConstantsAdmin.productosDelCarrito.iterator();
        while(it.hasNext() && okInsert){
            p = it.next();
            String precio =p.getPrecio().substring(0, p.getPrecio().length() - 5);
            if(p.isTieneTitulo()){// ES UN TAG DE TEXTO SIMPLE
                call = orderService.insertTagWithTitle(true, ConstantsAdmin.tokenFFL, idOrder, p.getIdProduct(),p.getModelo(),
                        p.getNombre(),Integer.parseInt(precio),Integer.parseInt(precio), 0, Integer.valueOf(p.getCantidad()),
                        p.getFillsTexturedId(),p.getComentarioUsr(),"iconotemporal",(int)c.getId(),p.getIdProduct(), 0,
                        "tcm/thumbs/iconotemporal.png", 0, (int)p.getTextFontSize(),ConstantsAdmin.convertIntColorToHex(p.getFontTextColor()),0,
                        0,p.getFontTextId(),p.getTexto(), prop.getProperty(ConstantsAdmin.TAG_LEGEND_TYPE_TEXT),(int)p.getTitleFontSize(),
                        ConstantsAdmin.convertIntColorToHex(p.getFontTitleColor()),0,
                        0,p.getFontTitleId(),p.getTitulo(), prop.getProperty(ConstantsAdmin.TAG_LEGEND_TYPE_TITLE));
            }else{// ES UN TAG DE TEXTO COMPUESTO (TIENE TITLE)
                call = orderService.insertTag(true, ConstantsAdmin.tokenFFL, idOrder, p.getIdProduct(),p.getModelo(),
                        p.getNombre(),Integer.parseInt(precio), Integer.parseInt(precio), 0, Integer.valueOf(p.getCantidad()),
                        p.getFillsTexturedId(),p.getComentarioUsr(),"iconotemporal",(int)c.getId(),p.getIdProduct(), 0,
                        "tcm/thumbs/iconotemporal.png", 0, (int)p.getTextFontSize(),ConstantsAdmin.convertIntColorToHex(p.getFontTextColor()),0,
                        0,p.getFontTextId(),p.getTexto(), prop.getProperty(ConstantsAdmin.TAG_LEGEND_TYPE_TEXT));
            }
            response = call.execute();
            if(response.body() == null){
                okInsert = false;
                ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
            }


        }
    }


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
        precioTotalEnvio = Float.valueOf(ConstantsAdmin.selectedShippingMethod.getPrice());
    }


    private String getStringDirEnvio(){
        String temp="";
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
        ProductoCarrito pc;
        String temp = "";
        String precio;
        precioTotalTags = 0;
        float precioTemp = 0;
        Iterator<ProductoCarrito> it = ConstantsAdmin.productosDelCarrito.iterator();
        while(it.hasNext()){
            pc = (ProductoCarrito) it.next();
            precioTemp = (Float.valueOf(pc.getPrecio()) * Float.valueOf(pc.getCantidad()));
//            precio = pc.getPrecio().substring(0, pc.getPrecio().length() - 5);
            temp = temp + "-"+ pc.getCantidad() + " x " + pc.getNombre() + ": $" + String.valueOf(precioTemp) + "\n";
            precioTotalTags = precioTotalTags + precioTemp;
        }
        textDetalleTags.setPadding(3,3,3,3);
        textDetalleTags.setText(temp);
        String precioText = String.valueOf(precioTotalTags);
        precioText = precioText.substring(0, precioText.length() - 2);
        textMontoSubtotal.setText(getString(R.string.label_total_etiquetas) + "$" + precioText);

    }

}
