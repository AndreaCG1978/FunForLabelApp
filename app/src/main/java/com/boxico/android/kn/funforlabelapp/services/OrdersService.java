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
                             @Field("currency_value") Integer currency_value,
                             // ORDERS_TOTAL
                             @Field("ot_title_shipping") String ot_title_shipping,
                             @Field("ot_value_total") Integer ot_value_total,
                             @Field("ot_value_subtotal") Integer ot_value_subtotal,
                             @Field("ot_value_shipping") Integer ot_value_shipping,

                             // ORDERS_STATUS_HISTORY
                             @Field("osh_date_added") String osh_date_added,
                             @Field("osh_comments") String osh_comments);

    @POST(API_ROUTE)
    @FormUrlEncoded
    Call<Integer> insertSingles(@Field("insertSingles") boolean insertSingles, @Field("tokenFFL") long tokenFFL,
                             @Field("orders_id") Integer orders_id,
                             // ORDERS_TOTAL
                             @Field("ot_title_shipping") String ot_title_shipping,
                             @Field("ot_value") Integer ot_value,

                             // ORDERS_STATUS_HISTORY
                             @Field("osh_date_added") Integer osh_date_added,
                             @Field("osh_comments") String osh_comments);


    @POST(API_ROUTE)
    @FormUrlEncoded
    Call<Integer> insertTag(@Field("insertTag") boolean insertTag, @Field("tokenFFL") long tokenFFL,
                                @Field("orders_id") Integer orders_id,
                                // ORDERS_PRODUCTS
                                @Field("op_products_id") Integer op_products_id,
                                @Field("op_products_model") String op_products_model,
                                @Field("op_products_name") String op_products_name,
                                @Field("op_products_price") String op_products_price,
                                @Field("op_final_price") String op_final_price,
                                @Field("op_products_tax") String op_products_tax,
                                @Field("op_products_quantity") String op_products_quantity);


}
