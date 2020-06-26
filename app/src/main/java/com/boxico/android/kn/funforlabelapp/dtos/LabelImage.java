package com.boxico.android.kn.funforlabelapp.dtos;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LabelImage {

    @SerializedName("fills_textured_id")
    @Expose
    private long fillsTexturedId;

    @SerializedName("uploaded_files_id")
    @Expose
    private long uploadedFilesId;

    @SerializedName("uniquename")
    @Expose
    private String uniquename;



    @SerializedName("info")
    @Expose
    private String info;

    private Bitmap image;


    public long getFillsTexturedId() {
        return fillsTexturedId;
    }

    public void setFillsTexturedId(long fillsTexturedId) {
        this.fillsTexturedId = fillsTexturedId;
    }

    public long getUploadedFilesId() {
        return uploadedFilesId;
    }

    public void setUploadedFilesId(long uploadedFilesId) {
        this.uploadedFilesId = uploadedFilesId;
    }

    public String getUniquename() {
        return uniquename;
    }

    public void setUniquename(String uniquename) {
        this.uniquename = uniquename;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
