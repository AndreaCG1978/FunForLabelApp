package com.boxico.android.kn.funforlabelapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.SyncStateContract;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.boxico.android.kn.funforlabelapp.dtos.ComboCarrito;
import com.boxico.android.kn.funforlabelapp.dtos.ItemCarrito;
import com.boxico.android.kn.funforlabelapp.dtos.ProductoCarrito;
import com.boxico.android.kn.funforlabelapp.utils.KNCarritoAdapterListView;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CarritoActivity extends FragmentActivity {

    private CarritoActivity me;
    TextView textWellcomeUsr = null;
    Button confirmarCarrito = null;
    ListView listViewCarrito = null;
    TextView totalPrecio = null;
  /*  private boolean terminoCargaListado = true;

    public boolean isTerminoCargaListado() {
        return terminoCargaListado;
    }

    public void setTerminoCargaListado(boolean terminoCargaListado) {
        this.terminoCargaListado = terminoCargaListado;
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;
        setContentView(R.layout.carrito);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.configureWidgets();
   //     this.initializeCreator();
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private void createAlertDialog(String message, String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(me);
        builder.setMessage(message).setTitle(title);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void configureWidgets() {
        textWellcomeUsr = findViewById(R.id.textWellcomeUser);
        String result = getString(R.string.wellcomeUser) + " " + ConstantsAdmin.currentCustomer.getFirstName() + " " + ConstantsAdmin.currentCustomer.getLastName();
        textWellcomeUsr.setText(result);
        confirmarCarrito = findViewById(R.id.btnConfirmarCarrito);
        listViewCarrito = findViewById(R.id.listViewCarrito);
        actualizarListaProductosCarrito();
        //KNCarritoAdapterListView customAdapter = new KNCarritoAdapterListView(this, R.layout.item_lista_carrito, R.id.tvNombreProducto,ConstantsAdmin.productosDelCarrito);

        //listViewCarrito.setAdapter(customAdapter);
        confirmarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarCompra();
            }
        });
     /*   totalPrecio = findViewById(R.id.totalPrecio);
        String precioTotal = this.calcularPrecioTotal();
        totalPrecio.setText("($" + precioTotal + ")");*/
       // configListView(listViewCarrito);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(ConstantsAdmin.finalizarHastaMenuPrincipal){
            finish();
        }

    }

    private String calcularPrecioTotal() {
        String result = null;
        float valor = 0;
        ItemCarrito ic = null;
        Iterator<ItemCarrito> it1 = ConstantsAdmin.productosDelCarrito.iterator();
        while(it1.hasNext()){
            ic = it1.next();
            valor = valor + Float.valueOf(ic.getPrecio()) * Float.valueOf(ic.getCantidad());
        }
        Iterator<ItemCarrito> it2 = ConstantsAdmin.combosDelCarrito.iterator();
        while(it2.hasNext()){
            ic = it2.next();
            valor = valor + Float.valueOf(ic.getPrecio()) * Float.valueOf(ic.getCantidad());
        }
        result = String.valueOf(valor);
        result = result.substring(0, result.length() - 2);
        return result;
    }

    private void confirmarCompra() {
        if(ConstantsAdmin.productosDelCarrito != null && ConstantsAdmin.productosDelCarrito.size() > 0 || ConstantsAdmin.combosDelCarrito != null && ConstantsAdmin.combosDelCarrito.size() > 0 ){
            Intent intent = new Intent(me, ConfigurarEnvioActivity.class);
            startActivity(intent);
        }else{
            createAlertDialog(getString(R.string.no_agrego_al_carrito), getString(R.string.atencion));
        }

    }


    public void actualizarListaProductosCarrito() {
        Iterator<ItemCarrito> itemsCarrito1 = ConstantsAdmin.productosDelCarrito.iterator();
        Iterator<ItemCarrito> itemsCarrito2 = ConstantsAdmin.combosDelCarrito.iterator();
        ArrayList<ItemCarrito> newCol = new ArrayList<>();
        while (itemsCarrito1.hasNext()){
            newCol.add(itemsCarrito1.next());
        }
        while (itemsCarrito2.hasNext()){
            newCol.add(itemsCarrito2.next());
        }
        listViewCarrito.setAdapter(new KNCarritoAdapterListView(this, R.layout.item_lista_carrito, R.id.tvNombreProducto,newCol));
        String precioTotal = this.calcularPrecioTotal();
        if(totalPrecio == null){
            totalPrecio = findViewById(R.id.totalPrecio);
        }
        String result = "($" + precioTotal + ")";
        totalPrecio.setText(result);
    }

    public void actualizarPrecioCarrito() {
      //  listViewCarrito.setAdapter(new KNCarritoAdapterListView(this, R.layout.item_lista_carrito, R.id.tvNombreProducto,ConstantsAdmin.productosDelCarrito));
        String precioTotal = "($" + this.calcularPrecioTotal() + ")";
        totalPrecio.setText(precioTotal);
    }


  /*  private void configListView(ListView lv){
        this.setTerminoCargaListado(false);


        final ViewTreeObserver.OnDrawListener ol = new ViewTreeObserver.OnDrawListener() {
            @Override
            public void onDraw() {
                setTerminoCargaListado(true);
            }
        };
        lv.getViewTreeObserver().addOnDrawListener(ol);
    }*/
}
