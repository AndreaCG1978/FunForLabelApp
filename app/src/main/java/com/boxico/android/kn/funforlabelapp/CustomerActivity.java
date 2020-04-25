package com.boxico.android.kn.funforlabelapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.util.List;

public class CustomerActivity extends FragmentActivity implements Spinner.OnItemSelectedListener{

    private Spinner provincias_spinner;
    private Spinner ciudades_spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            setContentView(R.layout.customer);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            configureWidgets();
        }
    }


    private void configureWidgets() {
        //Initializing Spinner
        provincias_spinner = (Spinner) this.findViewById(R.id.state_spinner);
        ciudades_spinner = (Spinner) this.findViewById(R.id.city_spinner);
        List<Geoname> provincias = LocationManager.getProvincias();
        provincias_spinner.setAdapter(new ArrayAdapter<Geoname>(this.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, provincias));
      //  LocationManager.cargarCiudades(String.valueOf(provincias.get(0).getGeonameId()));
        List<Geoname> ciudades = LocationManager.getCiudades();
        ciudades_spinner.setAdapter(new ArrayAdapter<Geoname>(this.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ciudades));

        provincias_spinner.setOnItemSelectedListener(this);
        ciudades_spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
