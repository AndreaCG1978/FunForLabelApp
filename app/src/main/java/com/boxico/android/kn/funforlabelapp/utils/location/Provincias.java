package com.boxico.android.kn.funforlabelapp.utils.location;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Provincias extends ApiResponse {

    @SerializedName("geonames")
    private List<Geoname> provincias;

    public List<Geoname> getProvincias() {
        return provincias;
    }
}