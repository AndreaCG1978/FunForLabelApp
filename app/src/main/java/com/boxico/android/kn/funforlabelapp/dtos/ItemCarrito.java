package com.boxico.android.kn.funforlabelapp.dtos;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ItemCarrito {

    protected int id;
    protected Bitmap background = null;
    protected String comentarioUsr = null;
    protected String backgroundFilename = null;
    protected String nombre = null;
    protected String precio = null;
    protected String cantidad = null;
    protected String cantidadPorPack = null;
    protected String modelo = null;
    protected int idProduct = -1;
    protected int fillsTexturedId = -1;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getBackground() {
        return background;
    }

    public void setBackground(Bitmap background) {
        this.background = background;
    }

    public String getComentarioUsr() {
        return comentarioUsr;
    }

    public void setComentarioUsr(String comentarioUsr) {
        this.comentarioUsr = comentarioUsr;
    }

    public String getBackgroundFilename() {
        return backgroundFilename;
    }

    public void setBackgroundFilename(String backgroundFilename) {
        this.backgroundFilename = backgroundFilename;
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

    public String getCantidadPorPack() {
        return cantidadPorPack;
    }

    public void setCantidadPorPack(String cantidadPorPack) {
        this.cantidadPorPack = cantidadPorPack;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getFillsTexturedId() {
        return fillsTexturedId;
    }

    public void setFillsTexturedId(int fillsTexturedId) {
        this.fillsTexturedId = fillsTexturedId;
    }


    @Override
    public String toString() {
        return this.getNombre();
    }

    public boolean isProduct(){
        return false;
    }

    public boolean isCombo(){
        return false;
    }
}
