package com.boxico.android.kn.funforlabelapp.services;

import com.boxico.android.kn.funforlabelapp.dtos.Customer;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CategoriesProductsService {

    String API_ROUTE = "/funforlabelsApp/categories_products.php";

    @GET(API_ROUTE)
    Call< List<Customer>> loginCustomer(@Query("email") String usr, @Query("psw") String pass, @Query("tokenFFL") long tokenFFL);

    @GET(API_ROUTE)
    Call< List<Customer>> getCustomer(@Query("email") String usr, @Query("tokenFFL") long tokenFFL);

    @PUT(API_ROUTE + "/{email}/")
    @FormUrlEncoded
        //  @Headers("Content-Type: application/json")
    Call<ResponseBody> updatePasswordCustomer(@Field("email") String email,
                                              @Field("newPassword") String newPassword,
                                              @Field("tokenFFL") long tokenFFL);

    @POST(API_ROUTE)
    @FormUrlEncoded
        //  @Headers("Content-Type: application/json")
    Call<List<Customer>> createAccount(@Field("customers_firstname") String firstname,
                                       @Field("customers_lastname") String lastname,
                                       @Field("customers_email_address") String email_address,
                                       @Field("customers_password") String password,
                                       @Field("customers_gender") String gender,
                                       @Field("customers_ciudad") String ciudad,
                                       @Field("customers_provincia") String provincia,
                                       @Field("customers_suburbio") String suburbio,
                                       @Field("customers_direccion") String direccion,
                                       @Field("customers_cp") String codigopostal,
                                       @Field("customers_telephone") String telephone,
                                       @Field("customers_fax") String fax,
                                       @Field("customers_newsletter") String newsletter,
                                       @Field("tokenFFL") long tokenFFL);
/*
    @GET(API_ROUTE)
    Call< List<Inspector> > getInspectors(@Query("usr") String usr);

    @GET(API_ROUTE)
    Call< List<Inspector> > getInspectorsUsrPsw(@Query("user") String usr);

    @GET(API_ROUTE)
    Call< List<Inspector> > getAllInspectors();*/





}
