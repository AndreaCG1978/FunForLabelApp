package com.boxico.android.kn.funforlabelapp.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.boxico.android.kn.funforlabelapp.services.GeoService;
import com.boxico.android.kn.funforlabelapp.utils.location.GeoApiClient;
import com.boxico.android.kn.funforlabelapp.utils.location.GeoChilds;
import com.boxico.android.kn.funforlabelapp.utils.location.LocationManager;
import com.boxico.android.kn.funforlabelapp.utils.location.Paises;

import java.util.Locale;

import retrofit2.Call;

public class LoadGeoDataWorker extends Worker {
    WorkerParameters myWorkerParams;

    public LoadGeoDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        myWorkerParams = workerParams;
    }

    @NonNull
    @Override
    public Result doWork() {
        Result r = null;
     /*   try {
            GeoService service = GeoApiClient.getClient().create(GeoService.class);
            Call<Paises> responseCallPais = service.getPaises(Locale.getDefault().getLanguage(), ConstantsAdmin.GEOUSERNAME, ConstantsAdmin.GEOCODIGOARGENTINA);
            Paises paises = responseCallPais.execute().body();
            Call<GeoChilds> responseCallProvincias =
                    service.getChilds(Locale.getDefault().getLanguage(), ConstantsAdmin.GEOUSERNAME, String.valueOf(paises.getPaises().get(0).getGeonameId()));
            GeoChilds pcias = responseCallProvincias.execute().body();
            if (pcias != null) {
                provincias = pcias.getChilds();
                failed = false;
            }else{
                failed = true;
            }

        } catch (Exception e) {
            failed = true;
        }


        try {
            LocationManager.initialize();
            r = Result.success();
        } catch(Exception e) {
            r = Result.failure();
        }*/
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

