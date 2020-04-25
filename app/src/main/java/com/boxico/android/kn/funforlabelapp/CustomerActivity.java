package com.boxico.android.kn.funforlabelapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer);
        me = this;
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
            List<Geoname> ciudades = LocationManager.getCiudades();
            Collections.sort(ciudades);
            ciudades_spinner.setAdapter(new ArrayAdapter<Geoname>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ciudades));
            dialog.cancel();
        }
    }


    private class ReloadBarriosTask extends AsyncTask<Long, Integer, Integer> {

        @Override
        protected Integer doInBackground(Long... params) {
            LocationManager.recargarBarrios();
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {

            List<Geoname> barrios = LocationManager.getBarrios();
            if(barrios != null && barrios.size() > 0) {
                Collections.sort(barrios);
                barrio_spinner.setVisibility(View.VISIBLE);
                barrio_spinner.setAdapter(new ArrayAdapter<Geoname>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, barrios));
            }else{
                barrio_spinner.setVisibility(View.GONE);
            }

        }
    }



    private void configureWidgets() {
        //Initializing Spinner
        provincias_spinner = (Spinner) this.findViewById(R.id.state_spinner);
        ciudades_spinner = (Spinner) this.findViewById(R.id.city_spinner);
        barrio_spinner = (Spinner) this.findViewById(R.id.barrio_spinner);
        List<Geoname> provincias = LocationManager.getProvincias();
        provincias_spinner.setAdapter(new ArrayAdapter<Geoname>(this.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, provincias));
      //  LocationManager.cargarCiudades(String.valueOf(provincias.get(0).getGeonameId()));

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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

    }


}
