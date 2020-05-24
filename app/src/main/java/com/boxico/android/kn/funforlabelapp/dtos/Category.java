package com.boxico.android.kn.funforlabelapp.dtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("categories_id")
    @Expose
    private long id;

    @SerializedName("categories_name")
    @Expose
    private String name;

    @SerializedName("categories_image")
    @Expose
    private String imageString;


    @SerializedName("sort_order")
    @Expose
    private long order;
}
