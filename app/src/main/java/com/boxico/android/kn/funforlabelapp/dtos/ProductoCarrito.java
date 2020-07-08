package com.boxico.android.kn.funforlabelapp.dtos;

import android.graphics.Bitmap;

public class ProductoCarrito {

    private int id;
    private Creator creador = null;
    private String texto = null;
    private float textFontSize = 0f;
    private String textFontName = null;
    private int fontTextColor = -1;
    private String titulo = null;
    private float titleFontSize = 0f;
    private String titleFontName = null;
    private int fontTitleColor = -1;
    private Bitmap background = null;
    private LabelAttributes areaTexto = null;
    private LabelAttributes areaTitulo = null;
    private String comentarioUsr = null;
    private String backgroundFilename = null;
    private boolean tieneTitulo = false;
    private int idAreaTitulo = -1;
    private int idAreaTexto = -1;
    private int idCreador = -1;

    public int getIdCreador() {
        return idCreador;
    }

    public void setIdCreador(int idCreador) {
        this.idCreador = idCreador;
    }

    public int getIdAreaTitulo() {
        return idAreaTitulo;
    }

    public void setIdAreaTitulo(int idAreaTitulo) {
        this.idAreaTitulo = idAreaTitulo;
    }

    public int getIdAreaTexto() {
        return idAreaTexto;
    }

    public void setIdAreaTexto(int idAreaTexto) {
        this.idAreaTexto = idAreaTexto;
    }

    public boolean isTieneTitulo() {
        return tieneTitulo;
    }

    public void setTieneTitulo(boolean tieneTitulo) {
        this.tieneTitulo = tieneTitulo;
    }

    public String getBackgroundFilename() {
        return backgroundFilename;
    }

    public void setBackgroundFilename(String backgroundFilename) {
        this.backgroundFilename = backgroundFilename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComentarioUsr() {
        return comentarioUsr;
    }

    public void setComentarioUsr(String comentarioUsr) {
        this.comentarioUsr = comentarioUsr;
    }

    public float getTextFontSize() {
        return textFontSize;
    }

    public void setTextFontSize(float textFontSize) {
        this.textFontSize = textFontSize;
    }

    public Creator getCreador() {
        return creador;
    }

    public void setCreador(Creator creador) {
        this.creador = creador;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }



    public String getTextFontName() {
        return textFontName;
    }

    public void setTextFontName(String textFontName) {
        this.textFontName = textFontName;
    }

    public int getFontTextColor() {
        return fontTextColor;
    }

    public void setFontTextColor(int fontTextColor) {
        this.fontTextColor = fontTextColor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public float getTitleFontSize() {
        return titleFontSize;
    }

    public void setTitleFontSize(float titleFontSize) {
        this.titleFontSize = titleFontSize;
    }

    public String getTitleFontName() {
        return titleFontName;
    }

    public void setTitleFontName(String titleFontName) {
        this.titleFontName = titleFontName;
    }

    public LabelAttributes getAreaTexto() {
        return areaTexto;
    }

    public void setAreaTexto(LabelAttributes areaTexto) {
        this.areaTexto = areaTexto;
    }

    public LabelAttributes getAreaTitulo() {
        return areaTitulo;
    }

    public void setAreaTitulo(LabelAttributes areaTitulo) {
        this.areaTitulo = areaTitulo;
    }

    public Bitmap getBackground() {
        return background;
    }

    public void setBackground(Bitmap background) {
        this.background = background;
    }

    public int getFontTitleColor() {
        return fontTitleColor;
    }

    public void setFontTitleColor(int fontTitleColor) {
        this.fontTitleColor = fontTitleColor;
    }
}
