package com.boxico.android.kn.funforlabelapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boxico.android.kn.funforlabelapp.dtos.ProductoCarrito;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;

import androidx.appcompat.app.AppCompatActivity;

public class AcercaDeActivity extends AppCompatActivity {

    private AcercaDeActivity me;
    TextView textWellcomeUsr = null;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;
        setContentView(R.layout.acerca_de);
        this.configureWidgets();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private void configureWidgets() {
        textWellcomeUsr = findViewById(R.id.textWellcomeUser);
        textWellcomeUsr.setText(getString(R.string.wellcomeUser) + " " + ConstantsAdmin.currentCustomer.getFirstName() + " " + ConstantsAdmin.currentCustomer.getLastName());
        TextView tv = (TextView) findViewById(R.id.textEnvioDevoluciones);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirect("https://test.funforlabels.com/shipping.php");
            }
        });
        tv = (TextView) findViewById(R.id.textPoliticaPrivacidad);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirect("https://test.funforlabels.com/privacy.php");
            }
        });
        tv = (TextView) findViewById(R.id.textCondicionesUso);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirect("https://test.funforlabels.com/conditions.php");
            }
        });
    }

    private void redirect(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

}
