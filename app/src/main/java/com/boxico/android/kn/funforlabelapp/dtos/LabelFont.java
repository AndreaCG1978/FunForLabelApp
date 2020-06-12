package com.boxico.android.kn.funforlabelapp.dtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LabelFont {

    @SerializedName("fonts_id")
    @Expose
    private long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("basename")
    @Expose
    private String basename;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBasename() {
        return basename;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }

    @Override
    public String toString() {
        return name;
    }
}
