package com.boxico.android.kn.funforlabelapp.dtos;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ProductoCarritoCombo {

    private int id;
    private Bitmap background = null;
    private String comentarioUsr = null;
    private String backgroundFilename = null;
    private String nombre = null;
    private String precio = null;
    private String cantidad = null;
    private String cantidadPorPack = null;
    private String modelo = null;
    private int idProduct = -1;
    private int fillsTexturedId = -1;
    private ArrayList<ProductoCarrito> productos;

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

    public ArrayList<ProductoCarrito> getProductos() {
        if(productos == null){
            productos = new ArrayList<>();
        }
        return productos;
    }

    public void setProductos(ArrayList<ProductoCarrito> productos) {
        this.productos = productos;
    }

    @Override
    public String toString() {
        return this.getNombre();
    }
}
