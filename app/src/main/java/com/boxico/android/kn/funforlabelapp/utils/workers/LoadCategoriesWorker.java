package com.boxico.android.kn.funforlabelapp.utils.workers;

import android.content.Context;

import com.boxico.android.kn.funforlabelapp.MainActivity;
import com.boxico.android.kn.funforlabelapp.R;
import com.boxico.android.kn.funforlabelapp.dtos.Category;
import com.boxico.android.kn.funforlabelapp.dtos.Customer;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadCategoriesWorker extends Worker {
    WorkerParameters myWorkerParams;
    MainActivity myContext = null;

    public LoadCategoriesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        myWorkerParams = workerParams;
        myContext = (MainActivity) context;
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
                if(ConstantsAdmin.allCategories.size() >= 0){
                    try {
                        myContext.loadImageForCategories();
                        Iterator<Category> it = ConstantsAdmin.allCategories.iterator();
                        Category cat1 = null;
                        Category cat2 = null;
                        while(it.hasNext()){
                            cat2 = null;
                            cat1 = it.next();
                            if(it.hasNext()){
                                cat2 = it.next();
                            }
                            myContext.addCategoryInView(cat1, cat2);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch(Exception exc){

            if(call != null) {
                call.cancel();
            }

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

