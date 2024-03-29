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

public class LoadGeoProvinciasWorker extends Worker {
    final WorkerParameters myWorkerParams;

    public LoadGeoProvinciasWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        myWorkerParams = workerParams;
    }

    @NonNull
    @Override
    public Result doWork() {
        Result r;
        try {
            GeoService service = GeoApiClient.getClient().create(GeoService.class);
            Call<Paises> responseCallPais = service.getPaises(Locale.getDefault().getLanguage(), ConstantsAdmin.GEOUSERNAME, ConstantsAdmin.GEOCODIGOARGENTINA);
            Paises paises = responseCallPais.execute().body();
            Call<GeoChilds> responseCallProvincias =
                    service.getChilds(Locale.getDefault().getLanguage(), ConstantsAdmin.GEOUSERNAME, String.valueOf(paises.getPaises().get(0).getGeonameId()));
            GeoChilds pcias = responseCallProvincias.execute().body();
            if (pcias != null) {
                LocationManager.provincias = pcias.getChilds();
                LocationManager.failed = false;
                r = Result.success();
            } else {
                LocationManager.failed = true;
                r = Result.failure();
            }
        }catch (Exception exc){
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

