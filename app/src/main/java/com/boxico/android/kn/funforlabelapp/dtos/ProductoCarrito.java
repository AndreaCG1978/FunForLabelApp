package com.boxico.android.kn.funforlabelapp.dtos;

import android.graphics.Bitmap;

public class ProductoCarrito extends ItemCarrito{

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
    private LabelAttributes areaTexto = null;
    private LabelAttributes areaTitulo = null;
    private boolean tieneTitulo = false;
    private int idAreaTitulo = -1;
    private int idAreaTexto = -1;
    private int idCreador = -1;
    private int anchoTag = -1;
    private int largoTag= -1;
    private int round= -1;
    private int largoAreaTexto= -1;
    private int anchoAreaTexto= -1;
    private int esMultilineaTexto= -1;
    private int fromXTexto= -1;
    private int fromYTexto= -1;
    private int largoAreaTituto= -1;
    private int anchoAreaTituto= -1;
    private int esMultilineaTituto= -1;
    private int fromXTituto= -1;
    private int fromYTituto= -1;
    private int fontTextId = -1;
    private int fontTitleId = -1;
    private int idCombo = -1;
    private byte[] imagenDeTag;

    public byte[] getImagenDeTag() {
        return imagenDeTag;
    }

    public void setImagenDeTag(byte[] imagenDeTag) {
        this.imagenDeTag = imagenDeTag;
    }

    public boolean isProduct(){
        return true;
    }

    public int getIdCombo() {
        return idCombo;
    }

    public void setIdCombo(int idCombo) {
        this.idCombo = idCombo;
    }

    public String getCantidadPorPack() {
        return cantidadPorPack;
    }

    public void setCantidadPorPack(String cantidadPorPack) {
        this.cantidadPorPack = cantidadPorPack;
    }

    public int getFontTextId() {
        return fontTextId;
    }

    public void setFontTextId(int fontTextId) {
        this.fontTextId = fontTextId;
    }

    public int getFontTitleId() {
        return fontTitleId;
    }

    public void setFontTitleId(int fontTitleId) {
        this.fontTitleId = fontTitleId;
    }

    public int getFillsTexturedId() {
        return fillsTexturedId;
    }

    public void setFillsTexturedId(int fillsTexturedId) {
        this.fillsTexturedId = fillsTexturedId;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getLargoAreaTexto() {
        return largoAreaTexto;
    }

    public void setLargoAreaTexto(int largoAreaTexto) {
        this.largoAreaTexto = largoAreaTexto;
    }

    public int getAnchoAreaTexto() {
        return anchoAreaTexto;
    }

    public void setAnchoAreaTexto(int anchoAreaTexto) {
        this.anchoAreaTexto = anchoAreaTexto;
    }

    public int getEsMultilineaTexto() {
        return esMultilineaTexto;
    }

    public void setEsMultilineaTexto(int esMultilineaTexto) {
        this.esMultilineaTexto = esMultilineaTexto;
    }

    public int getFromXTexto() {
        return fromXTexto;
    }

    public void setFromXTexto(int fromXTexto) {
        this.fromXTexto = fromXTexto;
    }

    public int getFromYTexto() {
        return fromYTexto;
    }

    public void setFromYTexto(int fromYTexto) {
        this.fromYTexto = fromYTexto;
    }

    public int getLargoAreaTituto() {
        return largoAreaTituto;
    }

    public void setLargoAreaTituto(int largoAreaTituto) {
        this.largoAreaTituto = largoAreaTituto;
    }

    public int getAnchoAreaTituto() {
        return anchoAreaTituto;
    }

    public void setAnchoAreaTituto(int anchoAreaTituto) {
        this.anchoAreaTituto = anchoAreaTituto;
    }

    public int getEsMultilineaTituto() {
        return esMultilineaTituto;
    }

    public void setEsMultilineaTituto(int esMultilineaTituto) {
        this.esMultilineaTituto = esMultilineaTituto;
    }

    public int getFromXTituto() {
        return fromXTituto;
    }

    public void setFromXTituto(int fromXTituto) {
        this.fromXTituto = fromXTituto;
    }

    public int getFromYTituto() {
        return fromYTituto;
    }

    public void setFromYTituto(int fromYTituto) {
        this.fromYTituto = fromYTituto;
    }

    public int getAnchoTag() {
        return anchoTag;
    }

    public void setAnchoTag(int anchoTag) {
        this.anchoTag = anchoTag;
    }

    public int getLargoTag() {
        return largoTag;
    }

    public void setLargoTag(int largoTag) {
        this.largoTag = largoTag;
    }



    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }


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

    @Override
    public String toString() {
        return this.getNombre();
    }
}
