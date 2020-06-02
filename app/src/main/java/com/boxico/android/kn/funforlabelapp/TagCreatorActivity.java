package com.boxico.android.kn.funforlabelapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;

public class TagCreatorActivity extends FragmentActivity {

    private TagCreatorActivity me;
    TextView textWellcomeUsr = null;
    TextView textProductSelected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;
        setContentView(R.layout.tag_creator);
      //  this.initializeService();
        this.configureWidgets();
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void configureWidgets() {
        textWellcomeUsr = findViewById(R.id.textWellcomeUser);
        textWellcomeUsr.setText(getString(R.string.wellcomeUser) + " " + ConstantsAdmin.currentCustomer.getFirstName() + " " + ConstantsAdmin.currentCustomer.getLastName());
        textProductSelected = findViewById(R.id.textProductSelected);
        textProductSelected.setText(ConstantsAdmin.currentProduct.getName());
    }
}
