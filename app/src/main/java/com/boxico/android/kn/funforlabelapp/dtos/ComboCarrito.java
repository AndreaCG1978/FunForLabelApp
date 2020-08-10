package com.boxico.android.kn.funforlabelapp.dtos;


import java.util.ArrayList;

public class ComboCarrito extends ItemCarrito{

    private int id;
    private ArrayList<ItemCarrito> productos;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCombo(){
        return true;
    }


    public ArrayList<ItemCarrito> getProductos() {
        if(productos == null){
            productos = new ArrayList<>();
        }
        return productos;
    }

    public void setProductos(ArrayList<ItemCarrito> productos) {
        this.productos = productos;
    }

    @Override
    public String toString() {
        return this.getNombre();
    }
}
