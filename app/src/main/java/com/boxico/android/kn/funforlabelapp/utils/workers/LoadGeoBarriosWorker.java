package com.boxico.android.kn.funforlabelapp.utils.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.boxico.android.kn.funforlabelapp.services.GeoService;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;
import com.boxico.android.kn.funforlabelapp.utils.location.GeoApiClient;
import com.boxico.android.kn.funforlabelapp.utils.location.GeoChilds;
import com.boxico.android.kn.funforlabelapp.utils.location.LocationManager;
import com.boxico.android.kn.funforlabelapp.utils.location.Paises;

import java.util.Locale;

import retrofit2.Call;

public class LoadGeoBarriosWorker extends Worker {
    WorkerParameters myWorkerParams;

    public LoadGeoBarriosWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        myWorkerParams = workerParams;
    }

    @NonNull
    @Override
    public Result doWork() {
        Result r = null;
        try {
            LocationManager.failed = false;
            GeoService service = GeoApiClient.getClient().create(GeoService.class);
            Call<GeoChilds> responseCallBarrios =
                    service.getChilds(Locale.getDefault().getLanguage(), ConstantsAdmin.GEOUSERNAME, LocationManager.getGeoIdCiudad());
            GeoChilds brios = responseCallBarrios.execute().body();
            if(brios != null){
                LocationManager.barrios = brios.getChilds();
                r = Result.success();
            }else{
                LocationManager.failed = true;
                r = Result.failure();
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

