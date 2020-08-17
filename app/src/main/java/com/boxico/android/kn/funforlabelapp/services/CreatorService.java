package com.boxico.android.kn.funforlabelapp.services;

import com.boxico.android.kn.funforlabelapp.dtos.Creator;
import com.boxico.android.kn.funforlabelapp.dtos.LabelAttributes;
import com.boxico.android.kn.funforlabelapp.dtos.LabelFont;
import com.boxico.android.kn.funforlabelapp.dtos.LabelImage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CreatorService {

    String API_ROUTE = "/funforlabelsApp/creators.php";

    @GET(API_ROUTE)
    Call<Creator> getCreator(@Query("productId") long productId, @Query("getCreator") boolean getCreator, @Query("tokenFFL") long tokenFFL);


    @GET(API_ROUTE)
    Call<List<LabelImage>> getImages(@Query("creatorId") long creatorId, @Query("getImages") boolean getImages, @Query("tokenFFL") long tokenFFL);

    @GET(API_ROUTE)
    Call<List<LabelAttributes>> getLabelAttributes(@Query("creatorId") long creatorId, @Query("getLabelAttributes") boolean getImages, @Query("tokenFFL") long tokenFFL);

    @GET(API_ROUTE)
    Call<List<LabelFont>> getFonts(@Query("textAreasId") long textAreaId, @Query("getFonts") boolean getFonts, @Query("tokenFFL") long tokenFFL);



}
