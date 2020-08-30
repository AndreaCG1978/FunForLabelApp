package com.boxico.android.kn.funforlabelapp.utils.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.boxico.android.kn.funforlabelapp.R;
import com.boxico.android.kn.funforlabelapp.dtos.AddressBook;
import com.boxico.android.kn.funforlabelapp.dtos.ComboCarrito;
import com.boxico.android.kn.funforlabelapp.dtos.Customer;
import com.boxico.android.kn.funforlabelapp.dtos.ItemCarrito;
import com.boxico.android.kn.funforlabelapp.dtos.MetodoEnvio;
import com.boxico.android.kn.funforlabelapp.dtos.ProductoCarrito;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import retrofit2.Call;
import retrofit2.Response;

public class FinalizarCompraWorker extends Worker {
    final WorkerParameters myWorkerParams;


    public FinalizarCompraWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        myWorkerParams = workerParams;
        //this.mContext = (FinalizarCompraActivity) context;
     //   this.mContext = mContext;
    }

    @NonNull
    @Override
    public Result doWork() {
        Result r;
        try {

            float precioTotalTags = myWorkerParams.getInputData().getFloat("precioTotalTags",0);
            float precioTotalEnvio = myWorkerParams.getInputData().getFloat("precioTotalEnvio", 0);
            String comentario = myWorkerParams.getInputData().getString("comentario");
            Customer c = ConstantsAdmin.currentCustomer;
            AddressBook ab = ConstantsAdmin.addressCustomer;
            Date date= new Date();
            long time = date.getTime();
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(time));
            Properties p =  ConstantsAdmin.fflProperties;
            MetodoEnvio me = ConstantsAdmin.selectedShippingMethod;

            try {
                Call<Integer> call = null;
                //     btnFinalizar.setEnabled(false);
                Response<Integer> response;
                ConstantsAdmin.mensaje = null;
                call = ConstantsAdmin.orderService.insertOrder(true, ConstantsAdmin.tokenFFL,(int) c.getId(),  c.getFirstName() + " " + c.getLastName(),
                        ab.getCalle(), ab.getSuburbio(), ab.getCiudad(), ab.getCp(), ab.getProvincia(),
                        ConstantsAdmin.ARGENTINA, c.getTelephone(), c.getEmail(), 1,c.getFirstName() + " " + c.getLastName(),
                        ab.getCalle(),ab.getSuburbio(), ab.getCiudad(), ab.getCp(), ab.getProvincia(), ConstantsAdmin.ARGENTINA,1,
                        c.getFirstName() + " " + c.getLastName(), ab.getCalle(), ab.getSuburbio(), ab.getCiudad(), ab.getCp(), ab.getProvincia(), ConstantsAdmin.ARGENTINA,1,
                        ConstantsAdmin.selectedPaymentMethod.getName() + "(" + ConstantsAdmin.selectedPaymentMethod.getDescription() + ")",
                        null, timeStamp, Integer.valueOf(p.getProperty(ConstantsAdmin.ORDER_STATUS_PENDING_TRANSFERENCE)),
                        p.getProperty(ConstantsAdmin.CURRENCY), Integer.valueOf(p.getProperty(ConstantsAdmin.CURRENCY_VALUE)), me.getName() + "(" + me.getDescription() + ")",(int)(precioTotalTags + precioTotalEnvio),
                        (int)precioTotalTags, (int)precioTotalEnvio,timeStamp,comentario);
                response = call.execute();
                if(response.body() != null){
                    ConstantsAdmin.activityTemp.idOrder = response.body();


                    Call<Integer> call1;
                    Response<Integer> response1;
                    ProductoCarrito p1;
                 //   Customer c = ConstantsAdmin.currentCustomer;
                    Properties prop = ConstantsAdmin.fflProperties;
                    Iterator<ItemCarrito> it = ConstantsAdmin.productosDelCarrito.iterator();
                    while(it.hasNext() && ConstantsAdmin.activityTemp.okInsert){
                        p1 = (ProductoCarrito) it.next();
                        String precio =p1.getPrecio().substring(0, p1.getPrecio().length() - 5);
                        String imageName = ConstantsAdmin.takeScreenshot(p1);
                        if(p1.isTieneTitulo()){// ES UN TAG DE TEXTO SIMPLE
                            call1 = ConstantsAdmin.orderService.insertTagWithTitle(true, ConstantsAdmin.tokenFFL, ConstantsAdmin.activityTemp.idOrder, p1.getIdProduct(),p1.getModelo(),
                                    p1.getNombre(),Integer.parseInt(precio),Integer.parseInt(precio), 0, Integer.valueOf(p1.getCantidad()),
                                    p1.getFillsTexturedId(),p1.getComentarioUsr(),imageName,(int)c.getId(),p1.getIdProduct(), 0,
                                    "tcm/thumbs/" + imageName + ".png", 0, (int)p1.getTextFontSize(),ConstantsAdmin.convertIntColorToHex(p1.getFontTextColor()),0,
                                    0,p1.getFontTextId(),p1.getTexto(), prop.getProperty(ConstantsAdmin.TAG_LEGEND_TYPE_TEXT),(int)p1.getTitleFontSize(),
                                    ConstantsAdmin.convertIntColorToHex(p1.getFontTitleColor()),0,
                                    0,p1.getFontTitleId(),p1.getTitulo(), prop.getProperty(ConstantsAdmin.TAG_LEGEND_TYPE_TITLE));
                        }else{// ES UN TAG DE TEXTO COMPUESTO (TIENE TITLE)
                            call1 = ConstantsAdmin.orderService.insertTag(true, ConstantsAdmin.tokenFFL, ConstantsAdmin.activityTemp.idOrder, p1.getIdProduct(),p1.getModelo(),
                                    p1.getNombre(),Integer.parseInt(precio), Integer.parseInt(precio), 0, Integer.valueOf(p1.getCantidad()),
                                    p1.getFillsTexturedId(),p1.getComentarioUsr(),imageName,(int)c.getId(),p1.getIdProduct(), 0,
                                    "tcm/thumbs/" + imageName + ".png", 0, (int)p1.getTextFontSize(),ConstantsAdmin.convertIntColorToHex(p1.getFontTextColor()),0,
                                    0,p1.getFontTextId(),p1.getTexto(), prop.getProperty(ConstantsAdmin.TAG_LEGEND_TYPE_TEXT));
                        }
                        response1 = call1.execute();
                        if(response1.body() == null){
                            ConstantsAdmin.activityTemp.okInsert = false;
                            ConstantsAdmin.mensaje = ConstantsAdmin.activityTemp.getResources().getString(R.string.conexion_server_error);
                        }else{
                            ConstantsAdmin.uploadFile(imageName + ".png");
                            // this.almacenarImagenRemoto(p, imageName + ".png");
                        }
                    }
                    it = ConstantsAdmin.combosDelCarrito.iterator();
                    ComboCarrito combo;
                    Integer idProduct = -1;
                    String imageName;
                    while(it.hasNext() && ConstantsAdmin.activityTemp.okInsert){
                        combo = (ComboCarrito) it.next();
                        //      imageNameCombo = ConstantsAdmin.takeScreenshot(this, combo);
                        String precio =combo.getPrecio().substring(0, combo.getPrecio().length() - 5);
                        call = ConstantsAdmin.orderService.insertProduct(true, ConstantsAdmin.tokenFFL, ConstantsAdmin.activityTemp.idOrder, combo.getIdProduct(),"",
                                combo.getNombre(),Integer.parseInt(precio),Integer.parseInt(precio), 0, Integer.valueOf(combo.getCantidad()));
                        response = call.execute();
                        if(response.body() != null) {
                            idProduct = response.body();
                        }else{
                            ConstantsAdmin.activityTemp.okInsert = false;
                            ConstantsAdmin.mensaje = ConstantsAdmin.activityTemp.getResources().getString(R.string.conexion_server_error);
                        }
                        // SE INSERTA UN TAG QUE REPRESENTA AL PADRE DEL COMBO
                        Integer idParent = -1;
         /*   call = orderService.insertOnlyTagWithoutOthers(true,ConstantsAdmin.tokenFFL, idProduct, combo.getFillsTexturedId(),combo.getComentarioUsr(),imageName,(int)c.getId(),
                    combo.getIdProduct(), 0,"tcm/thumbs/" + imageName + ".png", 0);
*/                      call = ConstantsAdmin.orderService.insertOnlyTagWithoutOthers(true,ConstantsAdmin.tokenFFL, idProduct, combo.getFillsTexturedId(),combo.getComentarioUsr(),"",(int)c.getId(),
                                combo.getIdProduct(), 0,"", 0);
                        response = call.execute();
                        if(response.body() == null) {
                            ConstantsAdmin.activityTemp.okInsert = false;
                            ConstantsAdmin.mensaje = ConstantsAdmin.activityTemp.getResources().getString(R.string.conexion_server_error);
                        }else{
                            idParent = response.body();
                        }

                        Iterator<ItemCarrito> it1 = combo.getProductos().iterator();
                        ProductoCarrito pc;
                        while (it1.hasNext() && ConstantsAdmin.activityTemp.okInsert){
                            pc = (ProductoCarrito) it1.next();
                            imageName = ConstantsAdmin.takeScreenshot(pc);
                            if(pc.isTieneTitulo()){
                                call = ConstantsAdmin.orderService.insertOnlyTagWithTitle(true, ConstantsAdmin.tokenFFL, idProduct,pc.getFillsTexturedId(),pc.getComentarioUsr(),
                                        "imageName",(int)c.getId(),pc.getIdProduct(), 0,
                                        "tcm/thumbs/" + imageName + ".png", idParent, (int)pc.getTextFontSize(),ConstantsAdmin.convertIntColorToHex(pc.getFontTextColor()),0,
                                        0,pc.getFontTextId(),pc.getTexto(), prop.getProperty(ConstantsAdmin.TAG_LEGEND_TYPE_TEXT),(int)pc.getTitleFontSize(),
                                        ConstantsAdmin.convertIntColorToHex(pc.getFontTitleColor()),0,
                                        0,pc.getFontTitleId(),pc.getTitulo(), prop.getProperty(ConstantsAdmin.TAG_LEGEND_TYPE_TITLE));
                            }else{
                                call = ConstantsAdmin.orderService.insertOnlyTag(true,ConstantsAdmin.tokenFFL, idProduct, pc.getFillsTexturedId(),pc.getComentarioUsr(),"imageName",(int)c.getId(),
                                        pc.getIdProduct(), 0,"tcm/thumbs/" + imageName + ".png", idParent, (int)pc.getTextFontSize(),ConstantsAdmin.convertIntColorToHex(pc.getFontTextColor()),
                                        0,0,pc.getFontTextId(),pc.getTexto(), prop.getProperty(ConstantsAdmin.TAG_LEGEND_TYPE_TEXT));
                            }
                            response = call.execute();
                            if(response.body() == null) {
                                ConstantsAdmin.activityTemp.okInsert = false;
                                ConstantsAdmin.mensaje = ConstantsAdmin.activityTemp.getResources().getString(R.string.conexion_server_error);
                            }else{
                                ConstantsAdmin.uploadFile(imageName + ".png");
                            }
                        }
         /*   if(okInsert){
                ConstantsAdmin.uploadFile(imageNameCombo + ".png");
            }*/


                    }

      //this.insertarEtiquetas();
                }else{
                    ConstantsAdmin.activityTemp.okInsert = false;
                    ConstantsAdmin.mensaje = ConstantsAdmin.activityTemp.getResources().getString(R.string.conexion_server_error);
                    if(call != null) {
                        call.cancel();
                    }
                }
            }catch(Exception exc){
                ConstantsAdmin.mensaje = ConstantsAdmin.activityTemp.getResources().getString(R.string.conexion_server_error);
                ConstantsAdmin.activityTemp.okInsert = false;


            }
            r = Result.success();
        } catch(Exception e) {
            e.printStackTrace();
            r = Result.failure();
        }
        return r;
    }



  /*
        @Override
        public Result doWork() {
            // Do the work here--in this case, upload the images.

            uploadImages()

            // Indicate whether the task finished successfully with the Result
            return Result.success()
        }*/
}

