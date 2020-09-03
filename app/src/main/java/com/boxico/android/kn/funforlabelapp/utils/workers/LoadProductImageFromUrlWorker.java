package com.boxico.android.kn.funforlabelapp.utils.workers;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.boxico.android.kn.funforlabelapp.dtos.Category;
import com.boxico.android.kn.funforlabelapp.dtos.Product;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;

import java.io.IOException;
import java.util.Iterator;

public class LoadProductImageFromUrlWorker extends Worker {
    final WorkerParameters myWorkerParams;

    public LoadProductImageFromUrlWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        myWorkerParams = workerParams;
    }

    @NonNull
    @Override
    public Result doWork() {
        Result r;
        Iterator<Product> it = ConstantsAdmin.allProducts.iterator();
        Product p;
        String url;
        Bitmap b = null;
        boolean error = false;
        while (it.hasNext()){
            p = it.next();
            url = ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_URL_IMAGES) + p.getImageString();
            try {
                b = ConstantsAdmin.getImageFromURL(url);
            } catch (IOException e) {
                e.printStackTrace();
                error = true;
            }
            p.setImage(b);
        //    this.addProductInView(p);
        }
        if(!error){
            r = Result.success();
        }else{
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

