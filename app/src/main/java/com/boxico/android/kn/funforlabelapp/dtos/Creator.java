package com.boxico.android.kn.funforlabelapp.dtos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Creator {

    @SerializedName("creators_id")
    @Expose
    private long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("creator_types_name")
    @Expose
    private String creatorTypeName;


    @SerializedName("dpi")
    @Expose
    private int dpi;

    @SerializedName("width")
    @Expose
    private int width;

    @SerializedName("height")
    @Expose
    private int height;

    @SerializedName("rounded")
    @Expose
    private int rounded;

    @SerializedName("round")
    @Expose
    private int round;

    @SerializedName("borders_solid")
    @Expose
    private int borders_solid;

    @SerializedName("borders_solid_custom")
    @Expose
    private int borders_solid_custom;

    @SerializedName("fill_solid")
    @Expose
    private int fill_solid;

    @SerializedName("fill_background")
    @Expose
    private int fill_background;

    @SerializedName("fill_solid_custom")
    @Expose
    private int fill_solid_custom;

    @SerializedName("fill_background_custom")
    @Expose
    private int fill_background_custom;

    @SerializedName("icons")
    @Expose
    private int icons;

    @SerializedName("icons_custom")
    @Expose
    private int icons_custom;

    @SerializedName("text")
    @Expose
    private int text;

    @SerializedName("title")
    @Expose
    private int title;

    @SerializedName("mirror")
    @Expose
    private int mirror;

    @SerializedName("icons_width")
    @Expose
    private int icons_width;

    @SerializedName("icons_height")
    @Expose
    private int icons_height;

    @SerializedName("icons_position")
    @Expose
    private String icons_position;

    @SerializedName("icons_margin")
    @Expose
    private int icons_margin;

    @SerializedName("icons_shape")
    @Expose
    private int icons_shape;

    @SerializedName("icons_round")
    @Expose
    private int icons_round;

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

    public String getCreatorTypeName() {
        return creatorTypeName;
    }

    public void setCreatorTypeName(String creatorTypeName) {
        this.creatorTypeName = creatorTypeName;
    }

    public int getDpi() {
        return dpi;
    }

    public void setDpi(int dpi) {
        this.dpi = dpi;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getRounded() {
        return rounded;
    }

    public void setRounded(int rounded) {
        this.rounded = rounded;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getBorders_solid() {
        return borders_solid;
    }

    public void setBorders_solid(int borders_solid) {
        this.borders_solid = borders_solid;
    }

    public int getBorders_solid_custom() {
        return borders_solid_custom;
    }

    public void setBorders_solid_custom(int borders_solid_custom) {
        this.borders_solid_custom = borders_solid_custom;
    }

    public int getFill_solid() {
        return fill_solid;
    }

    public void setFill_solid(int fill_solid) {
        this.fill_solid = fill_solid;
    }

    public int getFill_background() {
        return fill_background;
    }

    public void setFill_background(int fill_background) {
        this.fill_background = fill_background;
    }

    public int getFill_solid_custom() {
        return fill_solid_custom;
    }

    public void setFill_solid_custom(int fill_solid_custom) {
        this.fill_solid_custom = fill_solid_custom;
    }

    public int getFill_background_custom() {
        return fill_background_custom;
    }

    public void setFill_background_custom(int fill_background_custom) {
        this.fill_background_custom = fill_background_custom;
    }

    public int getIcons() {
        return icons;
    }

    public void setIcons(int icons) {
        this.icons = icons;
    }

    public int getIcons_custom() {
        return icons_custom;
    }

    public void setIcons_custom(int icons_custom) {
        this.icons_custom = icons_custom;
    }

    public int getText() {
        return text;
    }

    public void setText(int text) {
        this.text = text;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getMirror() {
        return mirror;
    }

    public void setMirror(int mirror) {
        this.mirror = mirror;
    }

    public int getIcons_width() {
        return icons_width;
    }

    public void setIcons_width(int icons_width) {
        this.icons_width = icons_width;
    }

    public int getIcons_height() {
        return icons_height;
    }

    public void setIcons_height(int icons_height) {
        this.icons_height = icons_height;
    }

    public String getIcons_position() {
        return icons_position;
    }

    public void setIcons_position(String icons_position) {
        this.icons_position = icons_position;
    }

    public int getIcons_margin() {
        return icons_margin;
    }

    public void setIcons_margin(int icons_margin) {
        this.icons_margin = icons_margin;
    }

    public int getIcons_shape() {
        return icons_shape;
    }

    public void setIcons_shape(int icons_shape) {
        this.icons_shape = icons_shape;
    }

    public int getIcons_round() {
        return icons_round;
    }

    public void setIcons_round(int icons_round) {
        this.icons_round = icons_round;
    }


    //    creator_types_id, dpi, width, height, rounded, round, borders_solid, borders_solid_custom, fill_solid, fill_background, fill_solid_custom, fill_background_custom, icons, icons_custom, text,
//    title, mirror, icons_width, icons_height, icons_position, icons_margin, icons_shape, icons_round





}
