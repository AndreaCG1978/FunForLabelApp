package com.boxico.android.kn.funforlabelapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.boxico.android.kn.funforlabelapp.utils.BundleCodes;

class ResultadoMLActivity extends AppCompatActivity {


    TextView installments;
    TextView amount;
    TextView ccType;
    TextView paymentId;
    ImageView image;
    View layoutView;

    // it could be

    public final static String RESULT_PAYMENT_ID = "paymentId";

    public final static String RESULT_STATUS_OK = "OK";
    public final static String RESULT_STATUS_FAILED = "FAILED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_ml);
        image = (ImageView) findViewById(R.id.icon);
        layoutView = findViewById(R.id.payment_id);
        paymentId = (TextView) findViewById(R.id.payment_id);
        installments = (TextView) findViewById(R.id.installments);
        amount = (TextView) findViewById(R.id.amount);
        ccType = (TextView) findViewById(R.id.cc_type);



        Intent launcherIntent = getIntent();
        Bundle data = launcherIntent.getExtras();
        if (data != null) {
            String result = data.getString(BundleCodes.RESULT_STATUS);
            setStatus(result);
            paymentId.setText(String.valueOf(data.getLong(RESULT_PAYMENT_ID)));
            installments.setText(String.valueOf(data.getInt(BundleCodes.INSTALLMENTS)));
            amount.setText(String.valueOf(data.getDouble(BundleCodes.AMOUNT)));
            ccType.setText(data.getString(BundleCodes.CARD_TYPE));
        }

        Uri uri = launcherIntent.getData();
        if (uri != null) {
            String result = uri.getQueryParameter(BundleCodes.RESULT_STATUS);
            setStatus(result);
            paymentId.setText(uri.getQueryParameter(RESULT_PAYMENT_ID));
            installments.setText(uri.getQueryParameter(BundleCodes.INSTALLMENTS));
            amount.setText(uri.getQueryParameter(BundleCodes.AMOUNT));
            ccType.setText(uri.getQueryParameter(BundleCodes.CARD_TYPE));
        }

    }

    private void setStatus(String status) {
        if (RESULT_STATUS_OK.equals(status)) {
            if (android.os.Build.VERSION.SDK_INT >= 22) {
                image.setImageDrawable(getDrawable(R.drawable.carrito));
            } else {
                image.setImageDrawable(getResources().getDrawable(R.drawable.carrito));
            }
            // Show the payment id
            layoutView.setVisibility(View.VISIBLE);
        }
        if (RESULT_STATUS_FAILED.equals(status)) {
            if (android.os.Build.VERSION.SDK_INT >= 22) {
                image.setImageDrawable(getDrawable(R.drawable.delete));
            } else {
                image.setImageDrawable(getResources().getDrawable(R.drawable.delete));
            }
            layoutView.setVisibility(View.GONE);
            // Hide the payment id
            layoutView.setVisibility(View.VISIBLE);
        }
    }
}
