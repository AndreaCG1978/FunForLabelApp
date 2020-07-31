package com.boxico.android.kn.funforlabelapp.dtos;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("products_id")
    @Expose
    private long id;

    @SerializedName("products_name")
    @Expose
    private String name;

    @SerializedName("products_description")
    @Expose
    private String description;

    @SerializedName("products_image")
    @Expose
    private String imageString;

    @SerializedName("products_price")
    @Expose
    private String price;

    @SerializedName("products_quantity")
    @Expose
    private String quantity;

    @SerializedName("products_model")
    @Expose
    private String model;

    private Bitmap image;

    private boolean checked = false;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return getName();
    }
}
