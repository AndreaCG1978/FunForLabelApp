package com.boxico.android.kn.funforlabelapp.utils.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.boxico.android.kn.funforlabelapp.R;
import com.boxico.android.kn.funforlabelapp.dtos.Customer;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SaveCustomerWorker extends Worker {
    WorkerParameters myWorkerParams;

    public SaveCustomerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        myWorkerParams = workerParams;
    }

    @NonNull
    @Override
    public Result doWork() {
        ConstantsAdmin.codigoExito = 1;// CREACION CON EXITO
        Call<List<Customer>> callInsert;
        Response<List<Customer>> resp;
        Result r = null;
        try {
            callInsert = ConstantsAdmin.customerService.createAccount(ConstantsAdmin.tempCustomer.getFirstName(), ConstantsAdmin.tempCustomer.getLastName(), ConstantsAdmin.tempCustomer.getEmail(), ConstantsAdmin.tempCustomer.getPassword(), ConstantsAdmin.tempCustomer.getGender(), ConstantsAdmin.tempCustomer.getCiudad(), ConstantsAdmin.tempCustomer.getProvincia(), ConstantsAdmin.tempCustomer.getSuburbio(), ConstantsAdmin.tempCustomer.getDireccion(), ConstantsAdmin.tempCustomer.getCp(), ConstantsAdmin.tempCustomer.getTelephone(), ConstantsAdmin.tempCustomer.getFax(), ConstantsAdmin.tempCustomer.getNewsletter(), ConstantsAdmin.tokenFFL);
            resp = callInsert.execute();
            ArrayList<Customer> customers = new ArrayList<>(resp.body());
            if (customers.size() == 1) {//DEVUELVE EL CLIENTE RECIEN CREADO
                Customer c = resp.body().get(0);
                ConstantsAdmin.currentCustomer = c;
                ConstantsAdmin.customerJustCreated = true;
             //   ConstantsAdmin.mensaje = getString(R.string.create_customer_success);
                // finish();
                //selectedArtefact.setIdRemoteDB(a.getId());

            }else{// SIGNIFICA QUE YA EXISTE UN CLIENTE CON EL MAIL INGRESADO
                ConstantsAdmin.codigoExito = 2;

            }
            r = Result.success();
        } catch(Exception e) {
            e.printStackTrace();
            r = Result.failure();
            ConstantsAdmin.codigoExito = 3;
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

