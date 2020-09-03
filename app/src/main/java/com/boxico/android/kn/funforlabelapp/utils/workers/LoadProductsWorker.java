package com.boxico.android.kn.funforlabelapp.utils.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.boxico.android.kn.funforlabelapp.R;
import com.boxico.android.kn.funforlabelapp.dtos.Category;
import com.boxico.android.kn.funforlabelapp.dtos.Product;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class LoadProductsWorker extends Worker {
    final WorkerParameters myWorkerParams;

    public LoadProductsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        myWorkerParams = workerParams;

    }

    @NonNull
    @Override
    public Result doWork() {
        Result r = null;
        Call<List<Product>> call = null;
        Response<List<Product>> response;
        try {
            ConstantsAdmin.mensaje = null;
            call = ConstantsAdmin.categoriesProductsService.getProductsFromCategory(ConstantsAdmin.currentCategory.getId(), ConstantsAdmin.currentLanguage, ConstantsAdmin.tokenFFL);
            response = call.execute();
            if(response.body() != null){
                ConstantsAdmin.allProducts = new ArrayList<>(response.body());
                if(ConstantsAdmin.allProducts.size() == 0){
                    //ConstantsAdmin.mensaje = getResources().getString(R.string.conexion_server_error);
                    r = Result.failure();
                }else{
                    r = Result.success();
                }
                /*else{
                    try {
                        loadImageForProducts();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    textCategorySelected.setText(ConstantsAdmin.currentCategory.getName());
                }*/
            }else{
                r = Result.failure();
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

