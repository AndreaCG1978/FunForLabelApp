package com.boxico.android.kn.funforlabelapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.boxico.android.kn.funforlabelapp.dtos.AddressBook;
import com.boxico.android.kn.funforlabelapp.dtos.Customer;
import com.boxico.android.kn.funforlabelapp.dtos.LabelImage;
import com.boxico.android.kn.funforlabelapp.dtos.MetodoEnvio;
import com.boxico.android.kn.funforlabelapp.dtos.ProductoCarrito;
import com.boxico.android.kn.funforlabelapp.services.OrdersService;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
                new SendCustomerNotificationTask().execute();
            }
            if(dialog != null) {
                dialog.cancel();
            }
        }
    }

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
        Iterator<ProductoCarrito> it = ConstantsAdmin.productosDelCarrito.iterator();
        ProductoCarrito pc = null;
        while(it.hasNext()){
            pc = it.next();
            String precio =pc.getPrecio().substring(0, pc.getPrecio().length() - 5);
            body = body + pc.getCantidad() + " x " + pc.getNombre() + "(" + pc.getModelo() + "): $" + precio + "\n";
        }
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
        //getTime() returns current time in milliseconds
        long time = date.getTime();
        //Passed the milliseconds to constructor of Timestamp class
        Timestamp ts = new Timestamp(time);
        Properties p =  ConstantsAdmin.fflProperties;
        MetodoEnvio me = ConstantsAdmin.selectedShippingMethod;
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(time));
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
                        p.getNombre(),Integer.parseInt(precio),Integer.parseInt(precio), 0, 1,
                        p.getFillsTexturedId(),p.getComentarioUsr(),"iconotemporal",(int)c.getId(),p.getIdProduct(), 0,
                        "tcm/thumbs/iconotemporal.png", 0, (int)p.getTextFontSize(),ConstantsAdmin.convertIntColorToHex(p.getFontTextColor()),0,
                        0,p.getFontTextId(),p.getTexto(), prop.getProperty(ConstantsAdmin.TAG_LEGEND_TYPE_TEXT),(int)p.getTitleFontSize(),
                        ConstantsAdmin.convertIntColorToHex(p.getFontTitleColor()),0,
                        0,p.getFontTitleId(),p.getTitulo(), prop.getProperty(ConstantsAdmin.TAG_LEGEND_TYPE_TITLE));
            }else{// ES UN TAG DE TEXTO COMPUESTO (TIENE TITLE)
                call = orderService.insertTag(true, ConstantsAdmin.tokenFFL, idOrder, p.getIdProduct(),p.getModelo(),
                        p.getNombre(),Integer.parseInt(precio), Integer.parseInt(precio), 0, 1,
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
        Iterator<ProductoCarrito> it = ConstantsAdmin.productosDelCarrito.iterator();
        while(it.hasNext()){
            pc = (ProductoCarrito) it.next();
            precio = pc.getPrecio().substring(0, pc.getPrecio().length() - 5);
            temp = temp + "-"+ pc.getNombre() + ": $" + precio + "\n";
            precioTotalTags = precioTotalTags + Float.valueOf(pc.getPrecio());
        }
        textDetalleTags.setPadding(3,3,3,3);
        textDetalleTags.setText(temp);
        String precioText = String.valueOf(precioTotalTags);
        precioText = precioText.substring(0, precioText.length() - 2);
        textMontoSubtotal.setText(getString(R.string.label_total_etiquetas) + "$" + precioText);

    }

}
