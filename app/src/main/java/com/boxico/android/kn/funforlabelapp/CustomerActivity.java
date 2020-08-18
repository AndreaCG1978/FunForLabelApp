package com.boxico.android.kn.funforlabelapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.boxico.android.kn.funforlabelapp.dtos.Customer;
import com.boxico.android.kn.funforlabelapp.services.CustomerService;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;
import com.boxico.android.kn.funforlabelapp.utils.location.Geoname;
import com.boxico.android.kn.funforlabelapp.utils.location.LocationManager;
import com.boxico.android.kn.funforlabelapp.utils.workers.LoadGeoBarriosWorker;
import com.boxico.android.kn.funforlabelapp.utils.workers.LoadGeoCiudadesWorker;
import com.boxico.android.kn.funforlabelapp.utils.workers.LoadGeoProvinciasWorker;
import com.boxico.android.kn.funforlabelapp.utils.workers.SaveCustomerWorker;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerActivity extends FragmentActivity {

    private Spinner provincias_spinner;
    private Spinner ciudades_spinner;
    private Spinner barrio_spinner;
    private FragmentActivity me;
    private LinearLayout layoutBarrio;
    private TextView tvPartidos;
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
    private EditText entryProvincia;
    private EditText entryCiudad;

    private String provinciaSeleccionada;
    private String ciudadSeleccionada;
    private String barrioSeleccionado;

    private long geoIdProvinciaSeleccionada;
    private View currentFocusedWidget;
    private ProgressBar progressBar = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        me = this;

        setContentView(R.layout.customer);
        configureWidgets();
     //   new InitializeLocationTask().execute();
    //    LocationManager.initialize(this);

        LinearLayout layout = findViewById(R.id.customerLayout);
        progressBar = new ProgressBar(CustomerActivity.this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, 6,params);

        Data inputData = new Data.Builder().build();
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(LoadGeoProvinciasWorker.class)
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
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            provincias_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Geoname pcia;
                                    pcia = (Geoname) parent.getAdapter().getItem(position);
                                    provinciaSeleccionada = pcia.getName();
                                    geoIdProvinciaSeleccionada = pcia.getGeonameId();
                                    LocationManager.setGeoIdProvincia(String.valueOf(pcia.getGeonameId()));
                                    //  new ReloadCiudadesTask().execute();
                                    //LocationManager.recargarCiudades(me);

                                    Data inputData = new Data.Builder().build();
                                    Constraints constraints = new Constraints.Builder()
                                            .setRequiredNetworkType(NetworkType.CONNECTED)
                                            .build();
                                    OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(LoadGeoCiudadesWorker.class)
                                            .setInputData(inputData)
                                            .setConstraints(constraints)
                                            .build();

                                    WorkManager.getInstance(me).getWorkInfoByIdLiveData(request.getId())
                                            .observe(me, new Observer<WorkInfo>() {
                                                @Override
                                                public void onChanged(@Nullable WorkInfo workInfo) {
                                                    if (workInfo != null && workInfo.getState() == WorkInfo.State.RUNNING) {
                                                        progressBar.setVisibility(View.VISIBLE);
                                                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                    }
                                                    if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                                     //   createAlertDialog(me.getString(R.string.send_mail_succes) + customerTemp.getEmail(),"");
                                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                        progressBar.setVisibility(View.GONE);
                                                        ciudades_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                            @Override
                                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                                Geoname cdad;
                                                                cdad = (Geoname) parent.getAdapter().getItem(position);
                                                                ciudadSeleccionada = cdad.getName();
                                                                LocationManager.setGeoIdCiudad(String.valueOf(cdad.getGeonameId()));
                                                                //   new ReloadBarriosTask().execute();
                                                                //LocationManager.recargarBarrios(me);

                                                                Data inputData = new Data.Builder().build();
                                                                Constraints constraints = new Constraints.Builder()
                                                                        .setRequiredNetworkType(NetworkType.CONNECTED)
                                                                        .build();
                                                                OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(LoadGeoBarriosWorker.class)
                                                                        .setInputData(inputData)
                                                                        .setConstraints(constraints)
                                                                        .build();
                                                                WorkManager.getInstance(me).getWorkInfoByIdLiveData(request.getId())
                                                                        .observe(me, new Observer<WorkInfo>() {
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
                                                                                    if(!LocationManager.failed) {
                                                                                        bloquearLocation(false);
                                                                                        List<Geoname> barrios = LocationManager.getBarrios();
                                                                                        if(barrios != null && barrios.size() > 0) {
                                                                                            Collections.sort(barrios);
                                                                                            layoutBarrio.setVisibility(View.VISIBLE);
                                                                                            tvPartidos.setText(getString(R.string.partido));
                                                                                            barrio_spinner.setAdapter(new ArrayAdapter<Geoname>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, barrios));
                                                                                            barrio_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                                                @Override
                                                                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                                                                    Geoname barrio;
                                                                                                    barrio = (Geoname) parent.getAdapter().getItem(position);
                                                                                                    barrioSeleccionado = barrio.getName();
                                                                                                }

                                                                                                @Override
                                                                                                public void onNothingSelected(AdapterView<?> parent) {

                                                                                                }
                                                                                            });
                                                                                        }else{
                                                                                            tvPartidos.setText(getString(R.string.barrio));
                                                                                            layoutBarrio.setVisibility(View.GONE);
                                                                                        }

                                                                                    }else{
                                                                                        bloquearLocation(true);
                                                                                    }

                                                                                }
                                                                            }
                                                                        });

                                                                WorkManager.getInstance(me).enqueue(request);



                                                            }

                                                            @Override
                                                            public void onNothingSelected(AdapterView<?> parent) {

                                                            }
                                                        });

                                                        if(!LocationManager.failed) {
                                                            bloquearLocation(false);
                                                            List<Geoname> ciudades = LocationManager.getCiudades();
                                                            Collections.sort(ciudades);
                                                            ciudades_spinner.setAdapter(new ArrayAdapter<Geoname>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ciudades));
                                                        }else{
                                                            bloquearLocation(true);
                                                        }
                                                   //     progressBar.setVisibility(View.GONE);

                                                    }
                                                }
                                            });
                                    WorkManager.getInstance(me).enqueue(request);
                                  }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            if(!LocationManager.failed){
                                List<Geoname> provincias = LocationManager.getProvincias();
                                provincias_spinner.setAdapter(new ArrayAdapter<Geoname>(me.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, provincias));
                                bloquearLocation(false);
                            }else{
                                bloquearLocation(true);
                            }

                            //progressBar.setVisibility(View.GONE);

                        }
                    }
                });
        WorkManager.getInstance(this).enqueue(request);


        if(!LocationManager.failed) {
            bloquearLocation(false);
        }else{
            bloquearLocation(true);
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


    private void bloquearLocation(boolean bloquear){
        if(bloquear){
            entryProvincia.setVisibility(View.VISIBLE);
            entryCiudad.setVisibility(View.VISIBLE);
            barrio_spinner.setVisibility(View.GONE);
            ciudades_spinner.setVisibility(View.GONE);
            provincias_spinner.setVisibility(View.GONE);
        }else{
            entryProvincia.setVisibility(View.GONE);
            entryCiudad.setVisibility(View.GONE);
            barrio_spinner.setVisibility(View.VISIBLE);
            ciudades_spinner.setVisibility(View.VISIBLE);
            provincias_spinner.setVisibility(View.VISIBLE);

        }

    }
/*
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

*/

/*
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
                    getResources().getString(R.string.loading_data), true);
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
*/
/*
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
                    getResources().getString(R.string.loading_data), true);
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


*/
    private void configureWidgets() {
        //Initializing Spinner
        provincias_spinner = this.findViewById(R.id.state_spinner);
        ciudades_spinner = this.findViewById(R.id.city_spinner);
        barrio_spinner = this.findViewById(R.id.barrio_spinner);
        layoutBarrio = this.findViewById(R.id.layoutBarrio);
        tvPartidos = this.findViewById(R.id.tvPartido);
        entryNombre = this.findViewById(R.id.entryNombre);
        entryApellido = this.findViewById(R.id.entryApellido);
        entryMail = this.findViewById(R.id.entryEmail);
        entryProvincia = this.findViewById(R.id.entryProvincia);
        entryCiudad = this.findViewById(R.id.entryCiudad);
        entryDireccion = this.findViewById(R.id.entryDireccion);
        entryCP = this.findViewById(R.id.entryCodigoPostal);
        entryTel = this.findViewById(R.id.entryTelefono);
        entryFax = this.findViewById(R.id.entryFax);
        entryContrasenia = this.findViewById(R.id.entryContrasenia);
        entryConfirmacion = this.findViewById(R.id.entryConfirmacion);
        checkNewsletter = this.findViewById(R.id.checkNewsletter);
        radioFemenino = this.findViewById(R.id.radio_femenino);
      //  RadioButton radioMasculino = this.findViewById(R.id.radio_masculino);
        Button btnCancel = this.findViewById(R.id.buttonCancel);
        Button btnGuardar = this.findViewById(R.id.buttonGuardar);
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
     /*   provincias_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Geoname pcia = null;
                pcia = (Geoname) parent.getAdapter().getItem(position);
                provinciaSeleccionada = pcia.getName();
                geoIdProvinciaSeleccionada = pcia.getGeonameId();
                LocationManager.setGeoIdProvincia(String.valueOf(pcia.getGeonameId()));
              //  new ReloadCiudadesTask().execute();
                LocationManager.recargarCiudades(me);
                if(!LocationManager.failed) {
                    bloquearLocation(false);
                    List<Geoname> ciudades = LocationManager.getCiudades();
                    Collections.sort(ciudades);
                    ciudades_spinner.setAdapter(new ArrayAdapter<Geoname>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ciudades));
                }else{
                    bloquearLocation(true);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
  /*      ciudades_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Geoname cdad = null;
                cdad = (Geoname) parent.getAdapter().getItem(position);
                ciudadSeleccionada = cdad.getName();
                LocationManager.setGeoIdCiudad(String.valueOf(cdad.getGeonameId()));
             //   new ReloadBarriosTask().execute();
                LocationManager.recargarBarrios(me);
                if(!LocationManager.failed) {
                    bloquearLocation(false);
                    List<Geoname> barrios = LocationManager.getBarrios();
                    ithisf(barrios != null && barrios.size() > 0) {
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

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/
  /*      barrio_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Geoname barrio = null;
                barrio = (Geoname) parent.getAdapter().getItem(position);
                barrioSeleccionado = barrio.getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
     /*   if(!LocationManager.failed){
            List<Geoname> provincias = LocationManager.getProvincias();
            provincias_spinner.setAdapter(new ArrayAdapter<Geoname>(this.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, provincias));
            bloquearLocation(false);
        }else{
            bloquearLocation(true);
        }*/
      //  LocationManager.cargarCiudades(String.valueOf(provincias.get(0).getGeonameId()));


      //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void guardarCustomer() {
        if(validarCustomer()){
           // this.guardarCustomerEnBD();
            //new CreateCustomerTask().execute();
            loadInfoCustomer();
            ConstantsAdmin.codigoExito = 1;// CREACION CON EXITO
      //      Call<List<Customer>> callInsert;
       //     Response<List<Customer>> resp;

            try {
                this.initializeService();
                Data inputData = new Data.Builder().build();
                Constraints constraints = new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();
                OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(SaveCustomerWorker.class)
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
                                    String mensaje = "";
                                    switch (ConstantsAdmin.codigoExito){
                                        case 1:
                                            mensaje = getString(R.string.create_customer_success);
                                            finish();
                                            break;
                                        case 2:
                                            mensaje = getString(R.string.exists_customer);
                                            currentFocusedWidget = entryMail;
                                            break;
                                        case 3:
                                            mensaje = getString(R.string.create_customer_error);
                                            break;
                                    }
                                    ConstantsAdmin.mensaje = mensaje;
                                    createAlertDialog(ConstantsAdmin.mensaje, me.getString(R.string.atencion));

                                }
                            }
                        });
                ListenableFuture<Operation.State.SUCCESS> obj = WorkManager.getInstance(this).enqueue(request).getResult();

                /*
                callInsert = ConstantsAdmin.customerService.createAccount(customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.getPassword(), customer.getGender(), customer.getCiudad(), customer.getProvincia(), customer.getSuburbio(), customer.getDireccion(), customer.getCp(), customer.getTelephone(), customer.getFax(), customer.getNewsletter(), ConstantsAdmin.tokenFFL);
                resp = callInsert.execute();
                ArrayList<Customer> customers = new ArrayList<>(resp.body());
                if (customers.size() == 1) {//DEVUELVE EL CLIENTE RECIEN CREADO
                    Customer c = resp.body().get(0);
                    ConstantsAdmin.currentCustomer = c;
                    ConstantsAdmin.customerJustCreated = true;
                    ConstantsAdmin.mensaje = getString(R.string.create_customer_success);
                   // finish();
                    //selectedArtefact.setIdRemoteDB(a.getId());

                }else{// SIGNIFICA QUE YA EXISTE UN CLIENTE CON EL MAIL INGRESADO
                    codigoExito = 2;

                }
                */

            } catch (Exception exc) {
                exc.printStackTrace();
            }



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
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
               // setFocusOnWidget();
                if(currentFocusedWidget!= null){
                    currentFocusedWidget.clearFocus();
                    currentFocusedWidget.requestFocus();
                    currentFocusedWidget = null;
                }
            }
        });
       // dialog.setOnCancelListener(this);
    }



    private void loadInfoCustomer() {
        ConstantsAdmin.tempCustomer = new Customer();
        ConstantsAdmin.tempCustomer.setFirstName(entryNombre.getText().toString());
        ConstantsAdmin.tempCustomer.setPassword(entryContrasenia.getText().toString());
        ConstantsAdmin.tempCustomer.setEmail(entryMail.getText().toString());
        if (LocationManager.failed) {
            ConstantsAdmin.tempCustomer.setCiudad(entryCiudad.getText().toString());
            ConstantsAdmin.tempCustomer.setSuburbio(entryCiudad.getText().toString());
            ConstantsAdmin.tempCustomer.setProvincia(entryProvincia.getText().toString());
        } else {

            ConstantsAdmin.tempCustomer.setCiudad(ciudadSeleccionada);
            if(geoIdProvinciaSeleccionada != Long.parseLong(ConstantsAdmin.GEOIDCAPITALFEDERAL)) {
                ConstantsAdmin.tempCustomer.setProvincia(provinciaSeleccionada);
                ConstantsAdmin.tempCustomer.setSuburbio(barrioSeleccionado);
            }else{
                ConstantsAdmin.tempCustomer.setProvincia(ConstantsAdmin.CAPITAL_FEDERAL);
                ConstantsAdmin.tempCustomer.setSuburbio("");
            }
        }
        ConstantsAdmin.tempCustomer.setCp(entryCP.getText().toString());
        ConstantsAdmin.tempCustomer.setDireccion(entryDireccion.getText().toString());
        ConstantsAdmin.tempCustomer.setFax(entryFax.getText().toString());
        if (radioFemenino.isChecked()) {
            ConstantsAdmin.tempCustomer.setGender("f");
        } else {
            ConstantsAdmin.tempCustomer.setGender("m");
        }
        ConstantsAdmin.tempCustomer.setLastName(entryApellido.getText().toString());
        if (checkNewsletter.isChecked()) {
            ConstantsAdmin.tempCustomer.setNewsletter("1");
        } else {
            ConstantsAdmin.tempCustomer.setNewsletter("0");
        }
        ConstantsAdmin.tempCustomer.setTelephone(entryTel.getText().toString());
    }

   /* private class CreateCustomerTask extends AsyncTask<Long, Integer, Integer> {


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
                    currentFocusedWidget = entryMail;
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


*//*
    private int guardarCustomerEnBD() {
        loadInfoCustomer();
        int codigoExito = 1;// CREACION CON EXITO
        Call<List<Customer>> callInsert;
        Response<List<Customer>> resp;

        try {
            this.initializeService();
            callInsert = ConstantsAdmin.customerService.createAccount(ConstantsAdmin.tempCustomer.getFirstName(), ConstantsAdmin.tempCustomer.getLastName(), ConstantsAdmin.tempCustomer.getEmail(), ConstantsAdmin.tempCustomer.getPassword(), ConstantsAdmin.tempCustomer.getGender(), ConstantsAdmin.tempCustomer.getCiudad(), ConstantsAdmin.tempCustomer.getProvincia(), ConstantsAdmin.tempCustomer.getSuburbio(), ConstantsAdmin.tempCustomer.getDireccion(), ConstantsAdmin.tempCustomer.getCp(), ConstantsAdmin.tempCustomer.getTelephone(), ConstantsAdmin.tempCustomer.getFax(), ConstantsAdmin.tempCustomer.getNewsletter(), ConstantsAdmin.tokenFFL);
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

*/
    private boolean validarCustomer() {
        boolean esValido = true;
        ConstantsAdmin.mensaje = null;
        if(esValido && entryNombre.getText().length()<2){
            esValido = false;
            if(entryNombre.getText().length()== 0) {
                entryNombre.requestFocus();
                entryNombre.setHint(getString(R.string.info_required_hint));
            }else{
                currentFocusedWidget = entryNombre;
                createAlertDialog(getString(R.string.campo_mayor_dos_requerido),"");
            }
        }else if(esValido && entryApellido.getText().length()<2){
            esValido = false;

            if(entryApellido.getText().length()== 0) {
                entryApellido.requestFocus();
                entryApellido.setHint(getString(R.string.info_required_hint));
            }else{
                currentFocusedWidget = entryApellido;
                createAlertDialog(getString(R.string.campo_mayor_dos_requerido),"");
            }
        }else if(esValido && entryMail.getText().length()<5){
            esValido = false;
            if(entryMail.getText().length()== 0) {
                entryMail.requestFocus();
                entryMail.setHint(getString(R.string.info_required_hint));
            }else{
                currentFocusedWidget = entryMail;
                createAlertDialog(getString(R.string.campo_mail_no_valido),"");
            }
        }else if(esValido && !entryMail.getText().toString().contains("@")&& !entryMail.getText().toString().contains(".")){
            esValido = false;
            currentFocusedWidget = entryMail;
            createAlertDialog(getString(R.string.campo_mail_no_valido),"");


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

            if(entryDireccion.getText().length()== 0) {
                entryDireccion.requestFocus();
                entryDireccion.setHint(getString(R.string.info_required_hint));
            }else{
                currentFocusedWidget = entryDireccion;
                createAlertDialog(getString(R.string.campo_mayor_dos_requerido),"");
            }
        }else if(esValido && entryCP.getText().length()<2){
            esValido = false;

            if(entryCP.getText().length()== 0) {
                entryCP.requestFocus();
                entryCP.setHint(getString(R.string.info_required_hint));
            }else{
                currentFocusedWidget = entryCP;
                createAlertDialog(getString(R.string.campo_mayor_dos_requerido),"");
            }
        }else if(esValido && entryTel.getText().length()<2){
            esValido = false;

            if(entryTel.getText().length()== 0) {
                entryTel.requestFocus();
                entryTel.setHint(getString(R.string.info_required_hint));
            }else{
                currentFocusedWidget = entryTel;
                createAlertDialog(getString(R.string.campo_mayor_dos_requerido),"");
            }
        }else if(esValido && entryContrasenia.getText().length()<5){
            esValido = false;

            if(entryContrasenia.getText().length()== 0) {
                entryContrasenia.requestFocus();
                entryContrasenia.setHint(getString(R.string.info_required_hint));
            }else{
                currentFocusedWidget = entryContrasenia;
                createAlertDialog(getString(R.string.campo_mayor_cinco_requerido),"");
            }
        }else if(esValido && entryConfirmacion.getText().length()<5){
            esValido = false;

            if(entryConfirmacion.getText().length()== 0) {
                entryConfirmacion.requestFocus();
                entryConfirmacion.setHint(getString(R.string.info_required_hint));
            }else{
                currentFocusedWidget = entryConfirmacion;
                createAlertDialog(getString(R.string.campo_mayor_cinco_requerido),"");
            }
        }
        if(esValido && entryContrasenia.getText().length() > 0 && entryConfirmacion.getText().length() > 0
                && !(entryContrasenia.getText().toString().equals(entryConfirmacion.getText().toString()))){
            esValido = false;
            currentFocusedWidget = entryContrasenia;
            createAlertDialog(getString(R.string.password_not_equal_error),"");
        }
        return esValido;
    }


}
