package com.boxico.android.kn.funforlabelapp.utils.location;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Paises extends ApiResponse {

    @SerializedName("geonames")
    private List<GeonamePais> paises;

    public List<GeonamePais> getPaises() {
        return paises;
    }
}