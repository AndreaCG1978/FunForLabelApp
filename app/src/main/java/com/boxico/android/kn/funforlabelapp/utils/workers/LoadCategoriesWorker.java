package com.boxico.android.kn.funforlabelapp.utils.workers;

import android.content.Context;


import com.boxico.android.kn.funforlabelapp.dtos.Category;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Call;
import retrofit2.Response;

public class LoadCategoriesWorker extends Worker {
    final WorkerParameters myWorkerParams;

    public LoadCategoriesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        myWorkerParams = workerParams;

    }

    @NonNull
    @Override
    public Result doWork() {
        Result r = null;
        Call<List<Category>> call = null;
        Response<List<Category>> response;

        try {
            ConstantsAdmin.mensaje = null;
            call = ConstantsAdmin.categoriesProductsService.getCategories(ConstantsAdmin.categories[0], ConstantsAdmin.currentLanguage, ConstantsAdmin.tokenFFL);
            response = call.execute();
            if(response.body() != null){
                ConstantsAdmin.allCategories = new ArrayList<>(response.body());
                if(ConstantsAdmin.allCategories.size() != 0){
                    r = Result.success();
                }else{
                    r = Result.failure();
                }

            }
        }catch(Exception exc){
            if(call != null) {
                call.cancel();
            }
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

