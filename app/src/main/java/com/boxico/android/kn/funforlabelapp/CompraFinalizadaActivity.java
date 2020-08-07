package com.boxico.android.kn.funforlabelapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.boxico.android.kn.funforlabelapp.dtos.ItemCarrito;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;

import java.net.URLEncoder;
import java.util.ArrayList;

public class CompraFinalizadaActivity extends AppCompatActivity {

    private CompraFinalizadaActivity me;
    private TextView textWellcomeUsr;
    TextView textMensajeExito1;
    TextView textMensajeExito2;
    TextView textEnvioMail;
    TextView textEnvioWsp;
    Button btnFinalizar;
    private TextView textConector;
    private Button btnLimpiarCarrito;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;
        setContentView(R.layout.compra_finalizada);
        this.configureWidgets();

    }


    private void configureWidgets() {
        textEnvioMail = (TextView) findViewById(R.id.textEnvioMail);
        textEnvioWsp = (TextView) findViewById(R.id.textEnvioWsp);
        textMensajeExito1 = (TextView) findViewById(R.id.textMensajeExito1);
        textMensajeExito2 = (TextView) findViewById(R.id.textMensajeExito2);
        textConector = (TextView) findViewById(R.id.textConector);
        textWellcomeUsr = findViewById(R.id.textWellcomeUser);
        textWellcomeUsr.setText(getString(R.string.wellcomeUser) + " " + ConstantsAdmin.currentCustomer.getFirstName() + " " + ConstantsAdmin.currentCustomer.getLastName());
        btnLimpiarCarrito = (Button) findViewById(R.id.btnLimpiarCarrito);
        btnLimpiarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaciarCarrito();
            }
        });
        btnFinalizar = (Button) findViewById(R.id.btnFinalizar);
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverMain();
            }
        });
        if(ConstantsAdmin.compraExitosa){// COMPRA EXITOSA
            textEnvioWsp.setVisibility(View.VISIBLE);
            textEnvioMail.setVisibility(View.VISIBLE);
            textConector.setVisibility(View.VISIBLE);
            textMensajeExito2.setVisibility(View.VISIBLE);
            textMensajeExito1.setText(ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.MENSAJE_EXITO_ORDEN_GENERADA1));
            String temp = "";
            if(ConstantsAdmin.mensajeCompra != null && !ConstantsAdmin.mensajeCompra.equals("")){
                temp = "\n\n\n" + ConstantsAdmin.selectedPaymentMethod.getName() + "-" + getString(R.string.estado_compra) + ConstantsAdmin.mensajeCompra;
            }
            textMensajeExito2.setText(ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.MENSAJE_EXITO_ORDEN_GENERADA2) + temp);
            textEnvioMail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enviarMail();
                }
            });
            textEnvioWsp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enviarWsp();
                }
            });
        }else{// COMPRA CON ERROR O CANCELADA
            textEnvioWsp.setVisibility(View.GONE);
            textEnvioMail.setVisibility(View.GONE);
            textMensajeExito2.setVisibility(View.GONE);
            textConector.setVisibility(View.GONE);
            textMensajeExito1.setText(getString(R.string.cancelacion_compra) + "\n" + ConstantsAdmin.mensajeCompra);

        }



        // configListView(listViewCarrito);
    }

    private void vaciarCarrito() {
        if(ConstantsAdmin.productosDelCarrito.size() > 0 || ConstantsAdmin.combosDelCarrito.size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(this.getString(R.string.mensaje_vaciar_carrito))
                    .setCancelable(true)
                    .setPositiveButton(R.string.label_si, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            vaciarCarritoPrivado();
                        }
                    })
                    .setNegativeButton(R.string.label_no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder.show();
        }else{
            createAlertDialog(getString(R.string.carrito_vacio), getString(R.string.atencion));
        }
    }

    private void createAlertDialog(String message, String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(me);
        builder.setMessage(message).setTitle(title);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void vaciarCarritoPrivado() {
        ConstantsAdmin.deleteAllProductoCarrito(this);
        ConstantsAdmin.deleteAllComboProductoCarrito(this);
        ConstantsAdmin.deleteAllImagesFromStorage();
        ConstantsAdmin.productosDelCarrito = new ArrayList<ItemCarrito>();
        ConstantsAdmin.combosDelCarrito = new ArrayList<ItemCarrito>();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        volverMain();
    }

    private void enviarWsp() {

        try {
            PackageManager packageManager = this.getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = ConstantsAdmin.url_whatsapp + "send?phone=" + ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.TEL_WSP) + "&text=" + URLEncoder.encode(getString(R.string.solicito_envio_cbu_detalle), "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                this.startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void enviarMail() {
        ConstantsAdmin.enviarMailGenerico(this,ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_FFL_MAIL),getString(R.string.solicito_envio_cbu_detalle),getString(R.string.solicito_envio_cbu));
    }

    private void volverMain() {
        ConstantsAdmin.finalizarHastaMenuPrincipal = true;
        ConstantsAdmin.clearSelections();
        finish();

    }

}
