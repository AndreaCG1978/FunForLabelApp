package com.boxico.android.kn.funforlabelapp.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.boxico.android.kn.funforlabelapp.CarritoActivity;
import com.boxico.android.kn.funforlabelapp.R;
import com.boxico.android.kn.funforlabelapp.dtos.ComboCarrito;
import com.boxico.android.kn.funforlabelapp.dtos.ItemCarrito;
import com.boxico.android.kn.funforlabelapp.dtos.ProductoCarrito;

import java.util.List;

public class KNCarritoAdapterListView extends ArrayAdapter<ItemCarrito> {

    CarritoActivity mContext;
    int resourceLayout;

/*
    public KNCarritoAdapterListView(@NonNull Context context, int resource, @NonNull List<ProductoCarrito> objects) {
        super(context, resource, objects);
        mContext = (CarritoActivity) context;
        resourceLayout = resource;
    }*/

    public KNCarritoAdapterListView(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<ItemCarrito> objects) {
        super(context, resource, textViewResourceId, objects);
        this.mContext = (CarritoActivity)context;
        this.resourceLayout = resource;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        try {
            ListView lv = (ListView)parent;
            RelativeLayout rl = v.findViewById(R.id.relativeProductoCarrito);
            final ItemCarrito ic = (ItemCarrito) lv.getAdapter().getItem(position);
            initializeCreator(ic, rl);
            TextView txt = v.findViewById(R.id.tvNombreProducto);
            txt.setText(ic.getNombre());
            txt = v.findViewById(R.id.tvPrecio);
            String newPrice = "$" +  ic.getPrecio().substring(0, ic.getPrecio().length() - 2);
            txt.setText(newPrice);
            txt = v.findViewById(R.id.tvCantidad);
            txt.setText(ic.getCantidadPorPack());
            TextView txtCantidad = v.findViewById(R.id.txtCantidadProducto);
            txtCantidad.setText(ic.getCantidad());
            txt = v.findViewById(R.id.tvModelo);
            txt.setText(ic.getModelo());
            LinearLayout linear = v.findViewById(R.id.linearImagen);
            GradientDrawable border = new GradientDrawable();
            border.setColor(0xFFFFFFFF); //white background
            border.setStroke(3, Color.RED);
            border.setCornerRadius(17);//black border with full opacity
            linear.setBackground(border);
            ImageButton borrar = v.findViewById(R.id.borrarProductoCarrito);
            borrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    borrarItemCarrito(ic);
                }
            });
            ImageButton ver =  v.findViewById(R.id.verProductoCarrito);
            ver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openViewTag(ic);
                }
            });
            Button btn = v.findViewById(R.id.btnMas);
            btn.setTag(txtCantidad);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView txtCantidad = (TextView) v.getTag();
                    int cant = Integer.valueOf(txtCantidad.getText().toString());
                    cant++;
                    txtCantidad.setText(String.valueOf(cant));
                    ic.setCantidad(String.valueOf(cant));
                    if(ic.isProduct()){
                        ConstantsAdmin.createProductoCarrito((ProductoCarrito) ic, mContext);
                    }else{
                        ConstantsAdmin.createComboCarrito((ComboCarrito) ic, mContext);
                    }

                    mContext.actualizarPrecioCarrito();
                }
            });
            btn = v.findViewById(R.id.btnMenos);
            btn.setTag(txtCantidad);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView txtCantidad = (TextView) v.getTag();
                    int cant = Integer.valueOf(txtCantidad.getText().toString());
                    if(cant > 1) {
                        cant--;
                        txtCantidad.setText(String.valueOf(cant));
                        ic.setCantidad(String.valueOf(cant));
                        if(ic.isProduct()){
                            ConstantsAdmin.createProductoCarrito((ProductoCarrito) ic, mContext);
                        }else{
                            ConstantsAdmin.createComboCarrito((ComboCarrito) ic, mContext);
                        }
                        mContext.actualizarPrecioCarrito();
                    }
                }
            });
            //   mContext.setTerminoCargaListado(false);
            v.refreshDrawableState();
        }catch (Exception exc){

        }
        return v;
    }



    private void borrarItemCarrito(final ItemCarrito ic) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(mContext.getString(R.string.mensaje_borrar_producto_carrito))
                .setCancelable(true)
                .setPositiveButton(R.string.label_si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       eliminarItemCarritoPrivado(ic);


                    }
                })
                .setNegativeButton(R.string.label_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private void eliminarItemCarritoPrivado(ItemCarrito ic) {
        if(ic.isProduct()){
            ConstantsAdmin.productosDelCarrito.remove(ic);
            ConstantsAdmin.deleteProductoCarrito(mContext,(ProductoCarrito) ic);
        }else{
            ConstantsAdmin.combosDelCarrito.remove(ic);
            ConstantsAdmin.deleteComboProductoCarrito(mContext,(ComboCarrito) ic);
        }
        ConstantsAdmin.deleteImageFromStorage(ic.getBackgroundFilename());
        mContext.actualizarListaProductosCarrito();
    }


    private View initPopupViewTag(ItemCarrito ic)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View popupInputDialogView = layoutInflater.inflate(R.layout.tag_view, null);
   //     RelativeLayout rl = popupInputDialogView.findViewById(R.id.relativeTagView);

        RelativeLayout rl = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(7,7,7,7);
        rl.setLayoutParams(lp);
        LinearLayout ll = popupInputDialogView.findViewById(R.id.linearTagView);
     /*   if(ic.isProduct()){
            ll.setVisibility(View.GONE);
            rl.setVisibility(View.VISIBLE);
        }else{
            ll.setVisibility(View.VISIBLE);
            rl.setVisibility(View.GONE);
        }*/
        initializeCreatorFull(ic, rl, ll);
        return popupInputDialogView;
    }

    private void openViewTag(ItemCarrito ic) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        View v = initPopupViewTag(ic);
       // ConstantsAdmin.uploadFile1(ic.getBackgroundFilename());
        alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setView(v);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void initializeCreatorFull(ItemCarrito ic, RelativeLayout productView, LinearLayout comboView) {
        // ConstantsAdmin.makeTag(pc, achicar, acotar, productView, mContext);
        Bitmap img = ConstantsAdmin.getImage(ic.getImagenDeTag());
        int srcWidth = img.getWidth();
        int srcHeight = img.getHeight();
        int dstWidth = (int)(srcWidth*0.77f);
        int dstHeight = (int)(srcHeight*0.77f);
        //	Bitmap dstBitmap = Bitmap.createScaledBitmap(srcBitmap, dstWidth, dstHeight, true);
        Bitmap b =Bitmap.createScaledBitmap(img, dstWidth,dstHeight, false);
        BitmapDrawable bd = new BitmapDrawable(mContext.getResources(),b);
        productView.setBackground(bd);
        comboView.addView(productView);


            /*
            Iterator<ItemCarrito> productos = combo.getProductos().iterator();
            RelativeLayout rl = null;
            if(combo.getProductos().size() > 4){
                achicar = 0.5f;
            }
            while (productos.hasNext()){
                pc = (ProductoCarrito) productos.next();
                rl = new RelativeLayout(mContext);
                RelativeLayout.LayoutParams layoutParamsTextTag = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParamsTextTag.setMargins(15,15,15,15);
                rl.setLayoutParams(layoutParamsTextTag);

                ConstantsAdmin.makeTag(pc, achicar, acotar, rl, mContext);
                comboView.addView(rl);
            }*/
    }
/*
    private void makeTag(ProductoCarrito pc, float achicar, boolean acotar, RelativeLayout linearTag){
        Bitmap imagen = ConstantsAdmin.getImageFromStorage(pc.getBackgroundFilename());
        ConstantsAdmin.customizeBackground(achicar, imagen, pc.getAnchoTag(), pc.getLargoTag(), pc.getRound(), acotar, linearTag, mContext);
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
*/
    private void initializeCreator(ItemCarrito ic, RelativeLayout linearTag) {

        float achicar = 0.35f;

        //int height = displayMetrics.heightPixels;

        boolean acotar = false;

        if(ic.isProduct()){
            ProductoCarrito pc = (ProductoCarrito) ic;
       //     Bitmap imagen = ConstantsAdmin.getImageFromStorage(pc.getBackgroundFilename());
   //         ConstantsAdmin.uploadFile(pc.getBackgroundFilename());
            Bitmap imagen = ConstantsAdmin.getImage(pc.getImagenDeTag());
            if(imagen != null){
                ConstantsAdmin.customizeBackground(achicar, imagen, pc.getAnchoTag(), pc.getLargoTag(), pc.getRound(), acotar, linearTag, mContext);
            }

        }else{
            achicar = 1f;
            Bitmap imagen = ConstantsAdmin.getImageFromStorage(ic.getBackgroundFilename());
            if(imagen != null) {
                ConstantsAdmin.customizeBackground(achicar, imagen, 20, 17, 0, acotar, linearTag, mContext);
            }
        }


    }

}
