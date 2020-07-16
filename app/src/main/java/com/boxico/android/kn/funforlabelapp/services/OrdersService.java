package com.boxico.android.kn.funforlabelapp.services;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface OrdersService {

    String API_ROUTE = "/funforlabelsApp/ordersService.php";

    @POST(API_ROUTE)
    @FormUrlEncoded
    Call<Integer> insertOder(@Field("insertInOrders") boolean insertInOrders, @Field("tokenFFL") long tokenFFL,
                             @Field("customers_id") Integer customers_id,
                             @Field("customers_name") String customers_name,
                             @Field("customers_street_address") String customers_street_address,
                             @Field("customers_suburb") String customers_suburb,
                             @Field("customers_city") String customers_city,
                             @Field("customers_postcode") String customers_postcode,
                             @Field("customers_state") String customers_state,
                             @Field("customers_country") String customers_country,
                             @Field("customers_telephone") String customers_telephone,
                             @Field("customers_email_address") String customers_email_address,
                             @Field("customers_address_format_id") Integer customers_address_format_id,
                             @Field("delivery_name") String delivery_name,
                             @Field("delivery_street_address") String delivery_street_address,
                             @Field("delivery_suburb") String delivery_suburb,
                             @Field("delivery_city") String delivery_city,
                             @Field("delivery_postcode") String delivery_postcode,
                             @Field("delivery_state") String delivery_state,
                             @Field("delivery_country") String delivery_country,
                             @Field("delivery_address_format_id") Integer delivery_address_format_id,
                             @Field("billing_name") String billing_name,
                             @Field("billing_street_address") String billing_street_address,
                             @Field("billing_suburb") String billing_suburb,
                             @Field("billing_city") String billing_city,
                             @Field("billing_postcode") String billing_postcode,
                             @Field("billing_state") String billing_state,
                             @Field("billing_country") String billing_country,
                             @Field("billing_address_format_id") Integer billing_address_format_id,
                             @Field("payment_method") String payment_method,
                             @Field("last_modified") String last_modified,
                             @Field("date_purchased") String date_purchased,
                             @Field("order_status") Integer order_status,
                             @Field("currency") String currency,
                             @Field("currency_value") Integer currency_value);

    @POST(API_ROUTE)
    @FormUrlEncoded
    Call<Integer> insertOderProduct(@Field("insertInOrderProduct") boolean insertInOrderProduct, @Field("tokenFFL") long tokenFFL,
                             @Field("orders_id") Integer orders_id,
                             @Field("products_id") Integer products_id,
                             @Field("products_model") String products_model,
                             @Field("products_name") String products_name,
                             @Field("products_price") String products_price,
                             @Field("final_price") String final_price,
                             @Field("products_tax") String products_tax,
                             @Field("products_quantity") String products_quantity);


}
