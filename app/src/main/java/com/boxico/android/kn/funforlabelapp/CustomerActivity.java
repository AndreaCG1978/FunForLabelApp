package com.boxico.android.kn.funforlabelapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer);
        new InitializeLocationTask().execute();


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
        }
    }


    private class ReloadLocationTask extends AsyncTask<Long, Integer, Integer> {

        @Override
        protected Integer doInBackground(Long... params) {
            LocationManager.recargarCiudades();
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {

            List<Geoname> ciudades = LocationManager.getCiudades();
            Collections.sort(ciudades);
            ciudades_spinner.setAdapter(new ArrayAdapter<Geoname>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ciudades));

        }
    }


    private void configureWidgets() {
        //Initializing Spinner
        provincias_spinner = (Spinner) this.findViewById(R.id.state_spinner);
        ciudades_spinner = (Spinner) this.findViewById(R.id.city_spinner);
        List<Geoname> provincias = LocationManager.getProvincias();
        provincias_spinner.setAdapter(new ArrayAdapter<Geoname>(this.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, provincias));
      //  LocationManager.cargarCiudades(String.valueOf(provincias.get(0).getGeonameId()));

        provincias_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Geoname pcia = null;
                pcia = (Geoname) parent.getAdapter().getItem(position);
                LocationManager.setGeoIdProvincia(String.valueOf(pcia.getGeonameId()));
                new ReloadLocationTask().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ciudades_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

    }


}
