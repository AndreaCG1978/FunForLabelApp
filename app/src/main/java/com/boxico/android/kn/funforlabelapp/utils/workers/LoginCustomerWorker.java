package com.boxico.android.kn.funforlabelapp.utils.workers;

import android.content.Context;

import com.boxico.android.kn.funforlabelapp.dtos.Customer;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Call;
import retrofit2.Response;

public class LoginCustomerWorker extends Worker {
    final WorkerParameters myWorkerParams;

    public LoginCustomerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        myWorkerParams = workerParams;
    }

    @NonNull
    @Override
    public Result doWork() {
        Result r;
        try {
           // final LoginActivity me = this;
            Call<List<Customer>> call = null;
            Response<List<Customer>> response;
       //     final ArrayList<Customer> customers =  new ArrayList<>();

            try {

                ConstantsAdmin.mensaje = null;
                String usrText = myWorkerParams.getInputData().getString("usrText");
                String pswText = myWorkerParams.getInputData().getString("pswText");
                call = ConstantsAdmin.customerService.loginCustomer(usrText, pswText, ConstantsAdmin.tokenFFL);

                response = call.execute();
                if(response != null && response.body()!= null){
                    ConstantsAdmin.currentCustomer = response.body().get(0);

              /*      customers.addAll(response.body());
                    if(customers.size() == 1){
                        ConstantsAdmin.currentCustomer = customers.get(0);
                    }*/
                }else{
                    r = Result.failure();
                }
                /*
                call.enqueue(new Callback<List<Customer>>() {
                    @Override
                    public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                        if(response.body() != null){
                            customers.addAll(response.body());
                            if(customers.size() == 1){
                                ConstantsAdmin.currentCustomer = customers.get(0);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Customer>> call, Throwable t) {

                    }
                });*/
            }catch(Exception exc){

                r = Result.failure();
            }
            if(call != null) {
                call.cancel();
            }
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

