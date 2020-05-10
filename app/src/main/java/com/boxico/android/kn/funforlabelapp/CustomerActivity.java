package com.boxico.android.kn.funforlabelapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.boxico.android.kn.funforlabelapp.dtos.Customer;
import com.boxico.android.kn.funforlabelapp.services.CustomerService;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;
import com.boxico.android.kn.funforlabelapp.utils.location.Geoname;
import com.boxico.android.kn.funforlabelapp.utils.location.LocationManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerActivity extends FragmentActivity {

    private Spinner provincias_spinner;
    private Spinner ciudades_spinner;
    private Spinner barrio_spinner;
    private FragmentActivity me;
    private EditText provinciaEntry;
    private EditText ciudadEntry;
    private LinearLayout layoutBarrio;
    private TextView tvPartidos;
    private Button btnGuardar;
    private Button btnCancel;
    private EditText entryNombre;
    private EditText entryApellido;
    private EditText entryMail;
    private EditText entryDireccion;
    private EditText entryCP;
    private EditText entryTel;
    private EditText entryFax;
    private CheckBox checkNewsletter;
    private EditText entryContrasenia;
    private EditText entryConfirmacion;
    private RadioButton radioFemenino;
    private RadioButton radioMasculino;
    private EditText entryProvincia;
    private EditText entryCiudad;
    private Customer customer;
    private String provinciaSeleccionada;
    private String ciudadSeleccionada;
    private String barrioSeleccionado;
    private CustomerService customerService;
    private long geoIdProvinciaSeleccionada;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        me = this;
        new InitializeLocationTask().execute();

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


    private void bloquearLocation(boolean bloquear){
        if(bloquear){
            provinciaEntry.setVisibility(View.VISIBLE);
            ciudadEntry.setVisibility(View.VISIBLE);
            barrio_spinner.setVisibility(View.GONE);
            ciudades_spinner.setVisibility(View.GONE);
            provincias_spinner.setVisibility(View.GONE);
        }else{
            provinciaEntry.setVisibility(View.GONE);
            ciudadEntry.setVisibility(View.GONE);
            barrio_spinner.setVisibility(View.VISIBLE);
            ciudades_spinner.setVisibility(View.VISIBLE);
            provincias_spinner.setVisibility(View.VISIBLE);

        }

    }

    private class InitializeLocationTask extends AsyncTask<Long, Integer, Integer> {

        @Override
        protected Integer doInBackground(Long... params) {

           LocationManager.initialize();
           return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            setContentView(R.layout.customer);
            configureWidgets();
            if(!LocationManager.failed) {
                bloquearLocation(false);
            }else{
                bloquearLocation(true);
            }
        }
    }


    private class ReloadCiudadesTask extends AsyncTask<Long, Integer, Integer> {
        ProgressDialog dialog = null;

        @Override
        protected Integer doInBackground(Long... params) {
            publishProgress(1);
            LocationManager.recargarCiudades();
            return 0;
        }

        protected void onProgressUpdate(Integer... progress) {
            dialog = ProgressDialog.show(me, "",
                    getResources().getString(R.string.search_location_progress), true);
        }


        @Override
        protected void onPostExecute(Integer result) {

            if(!LocationManager.failed) {
                bloquearLocation(false);
                List<Geoname> ciudades = LocationManager.getCiudades();
                Collections.sort(ciudades);
                ciudades_spinner.setAdapter(new ArrayAdapter<Geoname>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ciudades));
            }else{
                bloquearLocation(true);
            }

            dialog.cancel();
        }
    }


    private class ReloadBarriosTask extends AsyncTask<Long, Integer, Integer> {

        ProgressDialog dialog = null;
        @Override
        protected Integer doInBackground(Long... params) {
            publishProgress(1);
            LocationManager.recargarBarrios();
            return 0;
        }

        protected void onProgressUpdate(Integer... progress) {
            dialog = ProgressDialog.show(me, "",
                    getResources().getString(R.string.search_location_progress), true);
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(!LocationManager.failed) {
                bloquearLocation(false);
                List<Geoname> barrios = LocationManager.getBarrios();
                if(barrios != null && barrios.size() > 0) {
                    Collections.sort(barrios);
                    layoutBarrio.setVisibility(View.VISIBLE);
                    tvPartidos.setText(getString(R.string.partido));
                    barrio_spinner.setAdapter(new ArrayAdapter<Geoname>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, barrios));
                }else{
                    tvPartidos.setText(getString(R.string.barrio));
                    layoutBarrio.setVisibility(View.GONE);
                }
            }else{
                bloquearLocation(true);
            }
            dialog.cancel();

        }
    }



    private void configureWidgets() {
        //Initializing Spinner
        provincias_spinner = (Spinner) this.findViewById(R.id.state_spinner);
        ciudades_spinner = (Spinner) this.findViewById(R.id.city_spinner);
        barrio_spinner = (Spinner) this.findViewById(R.id.barrio_spinner);
        provinciaEntry = (EditText) this.findViewById(R.id.entryProvincia);
        ciudadEntry = (EditText)this.findViewById(R.id.entryCiudad);
        layoutBarrio = (LinearLayout) this.findViewById(R.id.layoutBarrio);
        tvPartidos = (TextView)this.findViewById(R.id.tvPartido);
        entryNombre = (EditText) this.findViewById(R.id.entryNombre);
        entryApellido = (EditText) this.findViewById(R.id.entryApellido);
        entryMail = (EditText) this.findViewById(R.id.entryEmail);
        entryProvincia = (EditText) this.findViewById(R.id.entryProvincia);
        entryCiudad = (EditText) this.findViewById(R.id.entryCiudad);
        entryDireccion = (EditText) this.findViewById(R.id.entryDireccion);
        entryCP = (EditText) this.findViewById(R.id.entryCodigoPostal);
        entryTel = (EditText) this.findViewById(R.id.entryTelefono);
        entryFax = (EditText) this.findViewById(R.id.entryFax);
        entryContrasenia = (EditText) this.findViewById(R.id.entryContrasenia);
        entryConfirmacion = (EditText) this.findViewById(R.id.entryConfirmacion);
        checkNewsletter = (CheckBox) this.findViewById(R.id.checkNewsletter);
        radioFemenino = (RadioButton) this.findViewById(R.id.radio_femenino);
        radioMasculino = (RadioButton) this.findViewById(R.id.radio_masculino);
        btnCancel = (Button) this.findViewById(R.id.buttonCancel);
        btnGuardar = (Button) this.findViewById(R.id.buttonGuardar);
        entryNombre.clearFocus();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCustomer();
            }
        });
        provincias_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Geoname pcia = null;
                pcia = (Geoname) parent.getAdapter().getItem(position);
                provinciaSeleccionada = pcia.getName();
                geoIdProvinciaSeleccionada = pcia.getGeonameId();
                LocationManager.setGeoIdProvincia(String.valueOf(pcia.getGeonameId()));
                new ReloadCiudadesTask().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ciudades_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Geoname cdad = null;
                cdad = (Geoname) parent.getAdapter().getItem(position);
                ciudadSeleccionada = cdad.getName();
                LocationManager.setGeoIdCiudad(String.valueOf(cdad.getGeonameId()));
                new ReloadBarriosTask().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        barrio_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Geoname barrio = null;
                barrio = (Geoname) parent.getAdapter().getItem(position);
                barrioSeleccionado = barrio.getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(!LocationManager.failed){
            List<Geoname> provincias = LocationManager.getProvincias();
            provincias_spinner.setAdapter(new ArrayAdapter<Geoname>(this.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, provincias));
            bloquearLocation(false);
        }else{
            bloquearLocation(true);
        }
      //  LocationManager.cargarCiudades(String.valueOf(provincias.get(0).getGeonameId()));


      //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void guardarCustomer() {
        if(validarCustomer()){
           // this.guardarCustomerEnBD();
            new CreateCustomerTask().execute();
        }else{
            if(ConstantsAdmin.mensaje != null){
                createAlertDialog(ConstantsAdmin.mensaje, getString(R.string.atencion));
            }
        }
    }

    private void createAlertDialog(String message, String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle(title);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadInfoCustomer() {
        customer = new Customer();
        customer.setFirstName(entryNombre.getText().toString());
        customer.setPassword(entryContrasenia.getText().toString());
        customer.setEmail(entryMail.getText().toString());
        if (LocationManager.failed) {
            customer.setCiudad(entryCiudad.getText().toString());
            customer.setSuburbio(entryCiudad.getText().toString());
            customer.setProvincia(entryProvincia.getText().toString());
        } else {

            customer.setCiudad(ciudadSeleccionada);
            if(geoIdProvinciaSeleccionada != Long.valueOf(ConstantsAdmin.GEOIDCAPITALFEDERAL)) {
                customer.setProvincia(provinciaSeleccionada);
                customer.setSuburbio(barrioSeleccionado);
            }else{
                customer.setProvincia(ConstantsAdmin.CAPITAL_FEDERAL);
                customer.setSuburbio("");
            }
        }
        customer.setCp(entryCP.getText().toString());
        customer.setDireccion(entryDireccion.getText().toString());
        customer.setFax(entryFax.getText().toString());
        if (radioFemenino.isChecked()) {
            customer.setGender("f");
        } else {
            customer.setGender("m");
        }
        customer.setLastName(entryApellido.getText().toString());
        if (checkNewsletter.isChecked()) {
            customer.setNewsletter("1");
        } else {
            customer.setNewsletter("0");
        }
        customer.setTelephone(entryTel.getText().toString());
    }

    private class CreateCustomerTask extends AsyncTask<Long, Integer, Integer> {


        private ProgressDialog dialog = null;
        private int resultado = -1;

        @Override
        protected Integer doInBackground(Long... longs) {
            publishProgress(1);
            resultado = guardarCustomerEnBD();
            return 0;
        }

        protected void onProgressUpdate(Integer... progress) {
            dialog = ProgressDialog.show(me, "",
                    getResources().getString(R.string.creating_customer_progress), true);
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            String mensaje = "";
            switch (resultado){
                case 1:
                    mensaje = getString(R.string.create_customer_success);
                    break;
                case 2:
                    mensaje = getString(R.string.exists_customer);
                    break;
                case 3:
                    mensaje = getString(R.string.create_customer_error);
                    break;
            }
            createAlertDialog(mensaje,"");
            if(dialog != null) {
                dialog.cancel();
            }
        }
    }



    private int guardarCustomerEnBD() {
        loadInfoCustomer();
        int codigoExito = 1;// CREACION CON EXITO
        Call<List<Customer>> callInsert;
        Response<List<Customer>> resp;

        try {
            this.initializeService();
            callInsert = customerService.createAccount(customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.getPassword(), customer.getGender(), customer.getCiudad(), customer.getProvincia(), customer.getSuburbio(), customer.getDireccion(), customer.getCp(), customer.getTelephone(), customer.getFax(), customer.getNewsletter(), ConstantsAdmin.tokenFFL);
            resp = callInsert.execute();
            ArrayList<Customer> customers = new ArrayList<>(resp.body());
            if (customers.size() == 1) {//DEVUELVE EL CLIENTE RECIEN CREADO
                Customer c = resp.body().get(0);
                ConstantsAdmin.currentCustomer = c;
                ConstantsAdmin.customerJustCreated = true;
                ConstantsAdmin.mensaje = getString(R.string.create_customer_success);
                finish();
                //selectedArtefact.setIdRemoteDB(a.getId());

            }else{// SIGNIFICA QUE YA EXISTE UN CLIENTE CON EL MAIL INGRESADO
                codigoExito = 2;

            }
        } catch (Exception exc) {
            codigoExito = 3;
        }

        return codigoExito;
    }


    private boolean validarCustomer() {
        boolean esValido = true;
        ConstantsAdmin.mensaje = null;
        if(esValido && entryNombre.getText().length()<2){
            esValido = false;
            entryNombre.requestFocus();
            if(entryNombre.getText().length()== 0) {
                entryNombre.setHint(getString(R.string.info_required_hint));
            }else{
                createAlertDialog(getString(R.string.campo_mayor_dos_requerido),"");
            }
        }else if(esValido && entryApellido.getText().length()<2){
            esValido = false;
            entryApellido.requestFocus();
            if(entryApellido.getText().length()== 0) {
                entryApellido.setHint(getString(R.string.info_required_hint));
            }else{
                createAlertDialog(getString(R.string.campo_mayor_dos_requerido),"");
            }
        }else if(esValido && entryMail.getText().length()<5){
            esValido = false;
            entryMail.requestFocus();
            if(entryMail.getText().length()== 0) {
                entryMail.setHint(getString(R.string.info_required_hint));
            }else{
                createAlertDialog(getString(R.string.campo_mail_no_valido),"");
            }
        }else if(esValido && !entryMail.getText().toString().contains("@")&& !entryMail.getText().toString().contains(".")){
            esValido = false;
            createAlertDialog(getString(R.string.campo_mail_no_valido),"");
            entryMail.requestFocus();

        }else if(esValido && entryProvincia.getText().length()==0 && LocationManager.failed){
            esValido = false;
            entryProvincia.requestFocus();
            entryProvincia.setHint(getString(R.string.info_required_hint));
        }else if(esValido && entryCiudad.getText().length()==0 && LocationManager.failed){
            esValido = false;
            entryCiudad.requestFocus();
            entryCiudad.setHint(getString(R.string.info_required_hint));
        }else if(esValido && entryDireccion.getText().length()<2){
            esValido = false;
            entryDireccion.requestFocus();
            if(entryDireccion.getText().length()== 0) {
                entryDireccion.setHint(getString(R.string.info_required_hint));
            }else{
                createAlertDialog(getString(R.string.campo_mayor_dos_requerido),"");
            }
        }else if(esValido && entryCP.getText().length()<2){
            esValido = false;
            entryCP.requestFocus();
            if(entryCP.getText().length()== 0) {
                entryCP.setHint(getString(R.string.info_required_hint));
            }else{
                createAlertDialog(getString(R.string.campo_mayor_dos_requerido),"");
            }
        }else if(esValido && entryTel.getText().length()<2){
            esValido = false;
            entryTel.requestFocus();
            if(entryTel.getText().length()== 0) {
                entryTel.setHint(getString(R.string.info_required_hint));
            }else{
                createAlertDialog(getString(R.string.campo_mayor_dos_requerido),"");
            }
        }else if(esValido && entryContrasenia.getText().length()<2){
            esValido = false;
            entryContrasenia.requestFocus();
            if(entryContrasenia.getText().length()== 0) {
                entryContrasenia.setHint(getString(R.string.info_required_hint));
            }else{
                createAlertDialog(getString(R.string.campo_mayor_dos_requerido),"");
            }
        }else if(esValido && entryConfirmacion.getText().length()<2){
            esValido = false;
            entryConfirmacion.requestFocus();
            if(entryConfirmacion.getText().length()== 0) {
                entryConfirmacion.setHint(getString(R.string.info_required_hint));
            }else{
                createAlertDialog(getString(R.string.campo_mayor_dos_requerido),"");
            }
        }
        if(esValido && entryContrasenia.getText().length() > 0 && entryConfirmacion.getText().length() > 0
                && !(entryContrasenia.getText().toString().equals(entryConfirmacion.getText().toString()))){
            esValido = false;
            ConstantsAdmin.mensaje = getString(R.string.password_not_equal_error);

        }
        return esValido;
    }


}
