package com.boxico.android.kn.funforlabelapp.utils.location;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeoChilds extends ApiResponse {

    @SerializedName("geonames")
    private List<Geoname> childs;

    public List<Geoname> getChilds() {
        return childs;
    }
}