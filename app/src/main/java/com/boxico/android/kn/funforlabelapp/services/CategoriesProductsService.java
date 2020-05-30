package com.boxico.android.kn.funforlabelapp.services;

import com.boxico.android.kn.funforlabelapp.dtos.Category;
import com.boxico.android.kn.funforlabelapp.dtos.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CategoriesProductsService {

    String API_ROUTE = "/funforlabelsApp/categories_products.php";

    @GET(API_ROUTE)
    Call< List<Category>> getCategories(@Query("parentId") long parentId, @Query("currentLang") long currentLang,@Query("tokenFFL") long tokenFFL);

    @GET(API_ROUTE)
    Call< List<Product>> getProductsFromCategory(@Query("categoryId") long categoryId, @Query("currentLang") long currentLang, @Query("tokenFFL") long tokenFFL);



}
