package com.boxico.android.kn.funforlabelapp.services;


import com.boxico.android.kn.funforlabelapp.utils.location.Paises;
import com.boxico.android.kn.funforlabelapp.utils.location.GeoChilds;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeoService {

    @GET("childrenJSON")
    Call<GeoChilds> getChilds(
            @Query("lang") String lang,
            @Query("username") String userName,
            @Query("geonameId") String geonameId);

    @GET("countryInfoJSON")
    Call<Paises> getPaises(
            @Query("lang") String lang,
            @Query("username") String userName,
            @Query("country") String country);



}
