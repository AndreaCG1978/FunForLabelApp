package com.boxico.android.kn.funforlabelapp.utils.workers;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;
import com.boxico.android.kn.funforlabelapp.utils.KNMail;

public class SendMailWorker extends Worker {
    WorkerParameters myWorkerParams;

    public SendMailWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        myWorkerParams = workerParams;
    }

    @NonNull
    @Override
    public Result doWork() {
        KNMail m = new KNMail(ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_FFL_MAIL), ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_FFL_PASSWORD));
        String tmp = myWorkerParams.getInputData().getString("to");
        String[] toArr = {tmp};
        m.setTo(toArr);
        m.setFrom(ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_FFL_MAIL));
        //    m.setFrom("info@funforlabels.com");
        tmp = myWorkerParams.getInputData().getString("subject");
        m.setSubject(tmp);
        tmp = myWorkerParams.getInputData().getString("body");
        m.setBody(tmp);
        Result r = null;
        try {
            m.send();
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

