package com.boxico.android.kn.funforlabelapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.boxico.android.kn.funforlabelapp.CarritoActivity;
import com.boxico.android.kn.funforlabelapp.LoginActivity;
import com.boxico.android.kn.funforlabelapp.R;
import com.boxico.android.kn.funforlabelapp.dtos.LabelAttributes;
import com.boxico.android.kn.funforlabelapp.dtos.ProductoCarrito;

import java.io.File;
import java.util.List;

public class KNCarritoAdapterListView extends ArrayAdapter<ProductoCarrito> {

    CarritoActivity mContext;
    int resourceLayout;
/*
    public KNCarritoAdapterListView(@NonNull Context context, int resource, @NonNull List<ProductoCarrito> objects) {
        super(context, resource, objects);
        mContext = (CarritoActivity) context;
        resourceLayout = resource;
    }*/

    public KNCarritoAdapterListView(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<ProductoCarrito> objects) {
        super(context, resource, textViewResourceId, objects);
        this.mContext = (CarritoActivity)context;
        this.resourceLayout = resource;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        ListView lv = (ListView)parent;
        RelativeLayout rl = v.findViewById(R.id.relativeProductoCarrito);
        final ProductoCarrito pc = (ProductoCarrito) lv.getAdapter().getItem(position);
        initializeCreator(pc, rl);
        TextView txt = v.findViewById(R.id.tvNombreProducto);
        txt.setText(pc.getNombre());
        txt = v.findViewById(R.id.tvPrecio);
        String newPrice = pc.getPrecio().substring(0, pc.getPrecio().length() - 2);
        txt.setText("$" + newPrice);
        txt = v.findViewById(R.id.tvCantidad);
        txt.setText(pc.getCantidad());
        txt = v.findViewById(R.id.tvModelo);
        txt.setText(pc.getModelo());
        LinearLayout linear = v.findViewById(R.id.linearImagen);
        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); //white background
        border.setStroke(3, Color.RED); //black border with full opacity
        linear.setBackground(border);
        ImageButton borrar = v.findViewById(R.id.borrarProductoCarrito);
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarProductoCarrito(pc);
            }
        });
        mContext.setTerminoCargaListado(false);
        v.refreshDrawableState();
        return v;
    }

    private void borrarProductoCarrito(final ProductoCarrito pc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(mContext.getString(R.string.mensaje_borrar_producto_carrito))
                .setCancelable(true)
                .setPositiveButton(R.string.label_si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       eliminarProductoCarritoPrivado(pc);


                    }
                })
                .setNegativeButton(R.string.label_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private void eliminarProductoCarritoPrivado(ProductoCarrito pc) {
        ConstantsAdmin.productosDelCarrito.remove(pc);
        ConstantsAdmin.deleteProductoCarrito(mContext,pc);
        mContext.actualizarListaProductosCarrito();
    }


    private void initializeCreator(ProductoCarrito pc, RelativeLayout linearTag) {

        float achicar = 0.35f;

        //int height = displayMetrics.heightPixels;

        boolean acotar = false;


        Bitmap imagen = ConstantsAdmin.getImageFromStorage(pc.getBackgroundFilename());
        ConstantsAdmin.customizeBackground(achicar, imagen, pc.getAnchoTag(), pc.getLargoTag(), pc.getIdCreador(), pc.getRound(), acotar, linearTag, mContext);

        // CONFIGURACION DE UN AREA DE TEXTO

        EditText textTag = null;
        EditText titleTag = null;
        textTag = ConstantsAdmin.createTextArea(achicar, new EditText(mContext), "", pc.getIdCreador(), pc.getAnchoAreaTexto(), pc.getLargoAreaTexto(), pc.getFromXTexto(), pc.getFromYTexto(), pc.getEsMultilineaTexto(), acotar, linearTag, mContext);
        if(pc.getIdAreaTitulo()!= -1) {
            titleTag = ConstantsAdmin.createTextArea(achicar, new EditText(mContext), "", pc.getIdCreador(),pc.getAnchoAreaTituto(),pc.getLargoAreaTituto() , pc.getFromXTituto(), pc.getFromYTituto(), pc.getEsMultilineaTexto(), acotar, linearTag, mContext);
        }
        textTag.setText(pc.getTexto());
        textTag.setTextColor(pc.getFontTextColor());
        textTag.setTextSize(TypedValue.TYPE_STRING, pc.getTextFontSize() * achicar);
        File fileFont = ConstantsAdmin.getFile(pc.getTextFontName());
        Typeface face = Typeface.createFromFile(fileFont);
        textTag.setTypeface(face);
        textTag.setEnabled(false);
        if(titleTag != null){
            titleTag.setEnabled(false);
            titleTag.setText(pc.getTitulo());
            titleTag.setTextColor(pc.getFontTitleColor());
            titleTag.setTextSize(TypedValue.TYPE_STRING, pc.getTitleFontSize() * achicar);
            fileFont = ConstantsAdmin.getFile(pc.getTitleFontName());
            face = Typeface.createFromFile(fileFont);
            titleTag.setTypeface(face);
        }



    }

}
