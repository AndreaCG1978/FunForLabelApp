package com.boxico.android.kn.funforlabelapp;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;

import java.net.URLEncoder;

public class CompraFinalizadaActivity extends AppCompatActivity {

    private CompraFinalizadaActivity me;
    private TextView textWellcomeUsr;
    TextView textMensajeExito;
    TextView textEnvioMail;
    TextView textEnvioWsp;
    Button btnFinalizar;


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
        textMensajeExito = (TextView) findViewById(R.id.textMensajeExito);
        textWellcomeUsr = findViewById(R.id.textWellcomeUser);
        textWellcomeUsr.setText(getString(R.string.wellcomeUser) + " " + ConstantsAdmin.currentCustomer.getFirstName() + " " + ConstantsAdmin.currentCustomer.getLastName());
        btnFinalizar = (Button) findViewById(R.id.btnFinalizar);
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverMain();
            }
        });
        textMensajeExito.setText(ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.MENSAJE_EXITO_ORDEN_GENERADA));
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



        // configListView(listViewCarrito);
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
