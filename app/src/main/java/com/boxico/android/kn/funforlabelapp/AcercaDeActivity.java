package com.boxico.android.kn.funforlabelapp;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Properties;

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
        String result = getString(R.string.wellcomeUser) + " " + ConstantsAdmin.currentCustomer.getFirstName() + " " + ConstantsAdmin.currentCustomer.getLastName();
        textWellcomeUsr.setText(result);
        final Properties p = ConstantsAdmin.fflProperties;
        TextView tv = findViewById(R.id.textAcercaDe);
        if(ConstantsAdmin.currentLanguage==1){
            tv.setText(p.getProperty(ConstantsAdmin.ACERCADE_QUIENES_SOMOS_TEXTO_EN));
        }else{
            tv.setText(p.getProperty(ConstantsAdmin.ACERCADE_QUIENES_SOMOS_TEXTO));
        }

        tv = findViewById(R.id.textEnvioDevoluciones);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirect(p.getProperty(ConstantsAdmin.ACERCADE_ENVIO_DEVOLUCIONES_LINK));
            }
        });
        tv = findViewById(R.id.textPoliticaPrivacidad);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirect(p.getProperty(ConstantsAdmin.ACERCADE_POLITICA_PRIVACIDAD_LINK));
            }
        });
        tv = findViewById(R.id.textCondicionesUso);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirect(p.getProperty(ConstantsAdmin.ACERCADE_CONDICIONES_USO_LINK));
            }
        });
        tv = findViewById(R.id.textFormasDePago);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirect(p.getProperty(ConstantsAdmin.ACERCADE_FORMAS_PAGO_LINK));
            }
        });
        tv = findViewById(R.id.textPreguntasFrecuentes);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirect(p.getProperty(ConstantsAdmin.ACERCADE_PREGUNTAS_FRECUENTES_LINK));
            }
        });

        tv = findViewById(R.id.textMensajeEnvioWsp);
        if(ConstantsAdmin.currentLanguage==1){
            tv.setText(p.getProperty(ConstantsAdmin.ACERCADE_TEXTO_ENVIO_WSP_EN)) ;
        }else{
            tv.setText(p.getProperty(ConstantsAdmin.ACERCADE_TEXTO_ENVIO_WSP)) ;
        }


        tv = findViewById(R.id.textNroTel);
        tv.setText(p.getProperty(ConstantsAdmin.TEL_WSP));
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = me.getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);
                String url = ConstantsAdmin.url_whatsapp + "send?phone=" + p.getProperty(ConstantsAdmin.TEL_WSP);
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                if (i.resolveActivity(packageManager) != null) {
                    me.startActivity(i);
                }
            }
        });

        tv = findViewById(R.id.textMensajeEnvioMail);
        if(ConstantsAdmin.currentLanguage== 1){
            tv.setText(p.getProperty(ConstantsAdmin.ACERCADE_TEXTO_ENVIO_MAIL_EN)) ;
        }else{
            tv.setText(p.getProperty(ConstantsAdmin.ACERCADE_TEXTO_ENVIO_MAIL)) ;
        }


        tv = findViewById(R.id.textMail);
        tv.setText(p.getProperty(ConstantsAdmin.ATR_FFL_MAIL));
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstantsAdmin.enviarMailGenerico(me,p.getProperty(ConstantsAdmin.ATR_FFL_MAIL),"","");
            }
        });
        ImageButton btn = findViewById(R.id.btnInstagram);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlInstagram = p.getProperty(ConstantsAdmin.URL_INSTAGRAM);
                Intent i = newInstagramProfileIntent(me.getPackageManager(), urlInstagram);
                me.startActivity(i);

            }
        });

        btn = findViewById(R.id.btnFacebook);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlFacebook = p.getProperty(ConstantsAdmin.URL_FACEBOOK);
                Intent i = newFacebookIntent(me.getPackageManager(), urlFacebook);
                me.startActivity(i);

            }
        });


    }

    public Intent newFacebookIntent(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    public Intent newInstagramProfileIntent(PackageManager pm, String url) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            if (pm.getPackageInfo("com.instagram.android", 0) != null) {
                if (url.endsWith("/")) {
                    url = url.substring(0, url.length() - 1);
                }
                final String username = url.substring(url.lastIndexOf("/") + 1);
                // http://stackoverflow.com/questions/21505941/intent-to-open-instagram-user-profile-on-android
                intent.setData(Uri.parse("http://instagram.com/_u/" + username));
                intent.setPackage("com.instagram.android");
                return intent;
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        intent.setData(Uri.parse(url));
        return intent;
    }

    private void redirect(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

}
