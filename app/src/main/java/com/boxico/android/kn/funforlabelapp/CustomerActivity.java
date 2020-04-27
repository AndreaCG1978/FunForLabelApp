package com.boxico.android.kn.funforlabelapp;

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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;
import com.boxico.android.kn.funforlabelapp.utils.location.Geoname;
import com.boxico.android.kn.funforlabelapp.utils.location.LocationManager;

import java.util.Collections;
import java.util.List;

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        me = this;
        new InitializeLocationTask().execute();


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
                LocationManager.setGeoIdCiudad(String.valueOf(cdad.getGeonameId()));
                new ReloadBarriosTask().execute();

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


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

    }

    private void guardarCustomer() {
        if(validarCustomer()){
            this.guardarCustomerEnBD();
        }
    }

    private void guardarCustomerEnBD() {
    }

    private boolean validarCustomer() {
        boolean esValido = true;
        if(esValido && entryNombre.getText().length()==0){
            esValido = false;
            entryNombre.requestFocus();
        }else if(esValido && entryApellido.getText().length()==0){
            esValido = false;
            entryApellido.requestFocus();
        }else if(esValido && entryMail.getText().length()==0){
            esValido = false;
            entryMail.requestFocus();
        }else if(esValido && entryProvincia.getText().length()==0 && LocationManager.failed){
            esValido = false;
            entryProvincia.requestFocus();
        }else if(esValido && entryCiudad.getText().length()==0 && LocationManager.failed){
            esValido = false;
            entryCiudad.requestFocus();
        }else if(esValido && entryDireccion.getText().length()==0){
            esValido = false;
            entryDireccion.requestFocus();
        }else if(esValido && entryCP.getText().length()==0){
            esValido = false;
            entryCP.requestFocus();
        }else if(esValido && entryTel.getText().length()==0){
            esValido = false;
            entryTel.requestFocus();
        }else if(esValido && entryContrasenia.getText().length()==0){
            esValido = false;
            entryContrasenia.requestFocus();
        }else if(esValido && entryConfirmacion.getText().length()==0){
            esValido = false;
            entryConfirmacion.requestFocus();
        }
        return esValido;
    }


}
