package com.boxico.android.kn.funforlabelapp.utils.workers;

import android.content.Context;

import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class LoadAllCreatorWorker extends Worker {
    WorkerParameters myWorkerParams;

    public LoadAllCreatorWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        myWorkerParams = workerParams;
    }

    @NonNull
    @Override
    public Result doWork() {
        Result r = null;
        try {
            ConstantsAdmin.loadAllInCreator();
            r = Result.success();
        } catch(Exception e) {
            e.printStackTrace();
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

