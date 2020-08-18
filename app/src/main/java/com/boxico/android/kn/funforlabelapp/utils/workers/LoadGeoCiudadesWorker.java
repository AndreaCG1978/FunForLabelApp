package com.boxico.android.kn.funforlabelapp.utils.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.boxico.android.kn.funforlabelapp.services.GeoService;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;
import com.boxico.android.kn.funforlabelapp.utils.location.GeoApiClient;
import com.boxico.android.kn.funforlabelapp.utils.location.GeoChilds;
import com.boxico.android.kn.funforlabelapp.utils.location.Geoname;
import com.boxico.android.kn.funforlabelapp.utils.location.LocationManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import retrofit2.Call;

public class LoadGeoCiudadesWorker extends Worker {
    final WorkerParameters myWorkerParams;

    public LoadGeoCiudadesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        myWorkerParams = workerParams;
    }

    @NonNull
    @Override
    public Result doWork() {
        Result r = null;
        try {
            GeoService service = GeoApiClient.getClient().create(GeoService.class);
            Call<GeoChilds> responseCallCiudades;
            Call<GeoChilds> responseCallBarrios;
            LocationManager.failed = false;
            responseCallCiudades =
                    service.getChilds(Locale.getDefault().getLanguage(), ConstantsAdmin.GEOUSERNAME, LocationManager.getGeoIdProvincia());
            GeoChilds cdades = responseCallCiudades.execute().body();
            if(!LocationManager.getGeoIdProvincia().equals(ConstantsAdmin.GEOIDCAPITALFEDERAL)){
                if(cdades != null){
                    LocationManager.ciudades = cdades.getChilds();
                    r = Result.success();
                }else{
                    LocationManager.failed = true;
                    r = Result.failure();
                }
                if(!LocationManager.failed){
                    if(LocationManager.getGeoIdCiudad().equals("0")){
                        LocationManager.setGeoIdCiudad(String.valueOf(LocationManager.ciudades.get(0).getGeonameId()));
                    }
                    responseCallBarrios =
                            service.getChilds(Locale.getDefault().getLanguage(), ConstantsAdmin.GEOUSERNAME, LocationManager.getGeoIdCiudad());
                    GeoChilds brios = responseCallBarrios.execute().body();
                    if(brios != null){
                        LocationManager.barrios = brios.getChilds();
                        r = Result.success();
                    }else{
                        LocationManager.failed = true;
                        r = Result.failure();
                    }
                }
            }else{
                Iterator<Geoname> it = cdades.getChilds().iterator();
                ArrayList<Geoname> temp = new ArrayList<>();
                Geoname geoTemp;
                while (it.hasNext() && !LocationManager.failed){
                    geoTemp = it.next();
                    responseCallBarrios =
                            service.getChilds(Locale.getDefault().getLanguage(), ConstantsAdmin.GEOUSERNAME, String.valueOf(geoTemp.getGeonameId()));
                    GeoChilds brios = responseCallBarrios.execute().body();
                    if(brios != null){
                        temp.addAll(brios.getChilds());
                    }else{
                        LocationManager.failed = true;
                        r = Result.failure();
                    }
                }
                if(!LocationManager.failed) {
                    LocationManager.ciudades = temp;
                    r = Result.success();
                }

            }


        } catch (Exception e) {
            LocationManager.failed = true;
            r = Result.failure();
        }

        return r;
    }



  /*
        @Override
        public Result doWork() {
            // Do the work here--in this case, upload the images.

            uploadImages()

            // Indicate whether the task finished successfully with the Result
            return Result.success()
        }*/
}

