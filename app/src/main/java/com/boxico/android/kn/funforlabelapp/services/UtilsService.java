package com.boxico.android.kn.funforlabelapp.services;

import com.boxico.android.kn.funforlabelapp.dtos.MetodoEnvio;
import com.boxico.android.kn.funforlabelapp.dtos.MetodoPago;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UtilsService {

    String API_ROUTE = "/funforlabelsApp/utilsService.php";

    @GET(API_ROUTE)
    Call<List<MetodoEnvio>> getAllShippingMethod(@Query("getAllShippingMethod") boolean getAllShippingMethod, @Query("tokenFFL") long tokenFFL);

    @GET(API_ROUTE)
    Call<List<MetodoPago>> getAllPaymentMethod(@Query("getAllPaymentMethod") boolean getAllPaymentMethod, @Query("tokenFFL") long tokenFFL);



}
