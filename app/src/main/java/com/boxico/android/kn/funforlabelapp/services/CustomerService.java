package com.boxico.android.kn.funforlabelapp.services;

import com.boxico.android.kn.funforlabelapp.dtos.Customer;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CustomerService {

    String API_ROUTE = "/funforlabelsApp/customers.php";

    @GET(API_ROUTE)
    Call< List<Customer>> loginCustomer(@Query("email") String usr, @Query("psw") String pass, @Query("tokenFFL") long tokenFFL);

    @GET(API_ROUTE)
    Call< List<Customer>> getCustomer(@Query("email") String usr, @Query("tokenFFL") long tokenFFL);

    @PUT(API_ROUTE + "/{email}/")
    @FormUrlEncoded
        //  @Headers("Content-Type: application/json")
    Call<ResponseBody> updatePasswordCustomer(@Field("email") String email,
                                      @Field("newPassword") String newPassword,
                                      @Field("tokenFFL") long tokenIplan);
/*
    @GET(API_ROUTE)
    Call< List<Inspector> > getInspectors(@Query("usr") String usr);

    @GET(API_ROUTE)
    Call< List<Inspector> > getInspectorsUsrPsw(@Query("user") String usr);

    @GET(API_ROUTE)
    Call< List<Inspector> > getAllInspectors();*/





}
