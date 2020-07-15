package com.boxico.android.kn.funforlabelapp;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.boxico.android.kn.funforlabelapp.dtos.Customer;
import com.boxico.android.kn.funforlabelapp.dtos.MetodoPago;
import com.boxico.android.kn.funforlabelapp.dtos.ProductoCarrito;
import com.boxico.android.kn.funforlabelapp.services.UtilsService;
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

public class FinalizarCompraActivity extends AppCompatActivity {

    private FinalizarCompraActivity me;
    private TextView textWellcomeUsr;
 //   private TextView textIntroPago;
    private UtilsService utilsService;
    EditText entryComentario;
    TextView textFormaPago;
    TextView textDirEnvio;
    TextView textDetalleTags;
    private float precioTotalTags;


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
        utilsService = retrofit.create(UtilsService.class);

    }


    private void configureWidgets() {
        textFormaPago = (TextView) findViewById(R.id.textFormaPago);
        textDirEnvio = (TextView) findViewById(R.id.textDirEnvio);
        textDetalleTags = (TextView) findViewById(R.id.textDetalleTags);
        this.cargarDetalleTags();
        this.cargarDetalleEnvio();
        this.cargarDetallePago();
        textWellcomeUsr = findViewById(R.id.textWellcomeUser);
        textWellcomeUsr.setText(getString(R.string.wellcomeUser) + " " + ConstantsAdmin.currentCustomer.getFirstName() + " " + ConstantsAdmin.currentCustomer.getLastName());

      //  textIntroEnvio.setTypeface(Typeface.SANS_SERIF);

        entryComentario = (EditText) findViewById(R.id.entryCommentPago);



        // configListView(listViewCarrito);
    }

    private void cargarDetallePago() {
        String temp = "";
        temp = temp + ConstantsAdmin.selectedPaymentMethod.getName() + "\n";
        temp = temp + ConstantsAdmin.selectedPaymentMethod.getDescription() + "\n";
        textFormaPago.setText(temp);

    }

    private void cargarDetalleEnvio() {
        Customer c = ConstantsAdmin.currentCustomer;
        String temp = c.getFirstName() + " " + c.getLastName() + "\n";
        temp = temp + ConstantsAdmin.addressCustomer.getCalle() + "\n";
        if(ConstantsAdmin.addressCustomer.getSuburbio() != null && !ConstantsAdmin.addressCustomer.getSuburbio().equals("")){
            temp = temp + ConstantsAdmin.addressCustomer.getSuburbio() + "\n";
        }
        temp = temp + ConstantsAdmin.addressCustomer.getCiudad() + ", " + ConstantsAdmin.addressCustomer.getCp() + "\n";
        temp = temp + ConstantsAdmin.addressCustomer.getProvincia() + ", " + ConstantsAdmin.GEOCODIGOARGENTINA + "\n\n";
        temp = temp + ConstantsAdmin.selectedShippingMethod.getName() + ": $" + ConstantsAdmin.selectedShippingMethod.getPrice() +  "\n";
        temp = temp + ConstantsAdmin.selectedShippingMethod.getDescription() + "\n";
        textDirEnvio.setText(temp);
    }

    private void cargarDetalleTags() {
        ProductoCarrito pc;
        String temp = "";
        precioTotalTags = 0;
        Iterator<ProductoCarrito> it = ConstantsAdmin.productosDelCarrito.iterator();
        while(it.hasNext()){
            pc = (ProductoCarrito) it.next();
            temp = temp + pc.getNombre() + "\t" + "$" + pc.getPrecio() + "\n";
            precioTotalTags = precioTotalTags + Float.valueOf(pc.getPrecio());
        }
        textDetalleTags.setText(temp);
        
    }

}
