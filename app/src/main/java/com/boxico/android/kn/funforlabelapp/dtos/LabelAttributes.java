package com.boxico.android.kn.funforlabelapp.dtos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LabelAttributes {

    @SerializedName("text_areas_id")
    @Expose
    private long textAreasId;

    @SerializedName("isTitle")
    @Expose
    private int isTitle;

    @SerializedName("disposition")
    @Expose
    private String disposition;

    @SerializedName("position")
    @Expose
    private String position;

    @SerializedName("from_x")
    @Expose
    private int fromX;

    @SerializedName("from_y")
    @Expose
    private int fromY;

    @SerializedName("width")
    @Expose
    private int width;

    @SerializedName("height")
    @Expose
    private int height;

    @SerializedName("margin")
    @Expose
    private int margin;

    @SerializedName("multiline")
    @Expose
    private int multiline;

    @SerializedName("multilegend")
    @Expose
    private int multilegend;

    @SerializedName("legends_count")
    @Expose
    private int legends_count;

    @SerializedName("effect_bold")
    @Expose
    private int effect_bold;


    @SerializedName("effect_italic")
    @Expose
    private int effect_italic;

    @SerializedName("choose_color")
    @Expose
    private int choose_color;

    @SerializedName("choose_size")
    @Expose
    private int choose_size;

    @SerializedName("choose_fonts")
    @Expose
    private int choose_fonts;

    @SerializedName("choose_align")
    @Expose
    private int choose_align;

    @SerializedName("clockwise")
    @Expose
    private int clockwise;

    @SerializedName("creators_id")
    @Expose
    private int creators_id;


    public long getTextAreasId() {
        return textAreasId;
    }

    public void setTextAreasId(long textAreasId) {
        this.textAreasId = textAreasId;
    }

    public int getIsTitle() {
        return isTitle;
    }

    public void setIsTitle(int isTitle) {
        this.isTitle = isTitle;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getFromX() {
        return fromX;
    }

    public void setFromX(int fromX) {
        this.fromX = fromX;
    }

    public int getFromY() {
        return fromY;
    }

    public void setFromY(int fromY) {
        this.fromY = fromY;
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

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public int getMultiline() {
        return multiline;
    }

    public void setMultiline(int multiline) {
        this.multiline = multiline;
    }

    public int getMultilegend() {
        return multilegend;
    }

    public void setMultilegend(int multilegend) {
        this.multilegend = multilegend;
    }

    public int getLegends_count() {
        return legends_count;
    }

    public void setLegends_count(int legends_count) {
        this.legends_count = legends_count;
    }

    public int getEffect_bold() {
        return effect_bold;
    }

    public void setEffect_bold(int effect_bold) {
        this.effect_bold = effect_bold;
    }

    public int getEffect_italic() {
        return effect_italic;
    }

    public void setEffect_italic(int effect_italic) {
        this.effect_italic = effect_italic;
    }

    public int getChoose_color() {
        return choose_color;
    }

    public void setChoose_color(int choose_color) {
        this.choose_color = choose_color;
    }

    public int getChoose_size() {
        return choose_size;
    }

    public void setChoose_size(int choose_size) {
        this.choose_size = choose_size;
    }

    public int getChoose_fonts() {
        return choose_fonts;
    }

    public void setChoose_fonts(int choose_fonts) {
        this.choose_fonts = choose_fonts;
    }

    public int getChoose_align() {
        return choose_align;
    }

    public void setChoose_align(int choose_align) {
        this.choose_align = choose_align;
    }

    public int getClockwise() {
        return clockwise;
    }

    public void setClockwise(int clockwise) {
        this.clockwise = clockwise;
    }

    public int getCreators_id() {
        return creators_id;
    }

    public void setCreators_id(int creators_id) {
        this.creators_id = creators_id;
    }

}
