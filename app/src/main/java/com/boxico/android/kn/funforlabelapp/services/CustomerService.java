package com.boxico.android.kn.funforlabelapp.services;

import com.boxico.android.kn.funforlabelapp.dtos.Customer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CustomerService {

    String API_ROUTE = "/funforlabelsApp/customers.php";

    @GET(API_ROUTE)
    Call< List<Customer>> loginCustomer(@Query("email") String usr, @Query("psw") String pass, @Query("tokenFFL") long tokenFFL);
/*
    @GET(API_ROUTE)
    Call< List<Inspector> > getInspectors(@Query("usr") String usr);

    @GET(API_ROUTE)
    Call< List<Inspector> > getInspectorsUsrPsw(@Query("user") String usr);

    @GET(API_ROUTE)
    Call< List<Inspector> > getAllInspectors();*/





}
