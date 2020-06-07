package com.boxico.android.kn.funforlabelapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.boxico.android.kn.funforlabelapp.ddbb.DataBaseManager;
import com.boxico.android.kn.funforlabelapp.dtos.Category;
import com.boxico.android.kn.funforlabelapp.dtos.Customer;
import com.boxico.android.kn.funforlabelapp.dtos.Product;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ConstantsAdmin {

    public static final String URL = "http://test.funforlabels.com/funforlabelsApp/";
    public static final String URL_IMAGES = "http://test.funforlabels.com/images/";
    public static final String URL_LABEL_IMAGES = "http://test.funforlabels.com/images/tcm/files/";
    public static final long tokenFFL = 27029085;
    public static final String SMTP_SERVER ="smtp.gmail.com";
    public static final String SMTP_PORT = "465";
    public static final String SMTP_SOCKETPORT = "465";
    public static final String FFL_MAIL = "info@funforlabels.com";
    public static final String FFL_PASSWORD = "ceciyguille2011";
    public static final String CAPITAL_FEDERAL = "Capital Federal" ;
    public static String mensaje = null;
    public static final String TAG = "DataBaseManager";
    public static final String DATABASE_NAME = "FunForLabelsAppDB";
    public static final int DATABASE_VERSION = 1;
    public static final String KEY_USER = "usuario";
    public static final String TABLE_LOGIN = "tabla_login";
    public static final String KEY_PASSWORD = "contrasenia";
    public static final String KEY_NOT_ENCRIPTED_PASSWORD = "contraseniaSinEncriptar";
    public static final String KEY_ROWID = "rowId" ;
    public static final String KEY_NAME = "name";
    public static final String ENTER = "\n";
    public static final String TAB = "\t";
    public static final String GEOUSERNAME = "andreacg1978";
    public static final String GEOAPIURL = "http://api.geonames.org/";
    public static final String GEOCODIGOARGENTINA = "AR";
    public static final String GEOAPITOGETPROVINCIA = "childrenJSON?geonameId=";
    public static final String GEOIDCAPITALFEDERAL = "3433955";
    public static Customer currentCustomer = null;
    public static boolean customerJustCreated = false;
    public static long[] categories = {46};
    public static long currentLanguage;
    public static Category currentCategory;
    public static Product currentProduct;


    public static void createLogin(Customer currentCustomer, Context ctx) {
        DataBaseManager dbm = DataBaseManager.getInstance(ctx);
        dbm.open();
        dbm.createLogin(currentCustomer);
        dbm.close();
    }

    public static Bitmap getImageFromURL(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Bitmap bmp = null;
        Request request = new Request.Builder()
                .url(url)
                .build();

        okhttp3.Response responses = null;
        responses = client.newCall(request).execute();
        int responseCode = 0;

        // Make the request
        if ((responseCode = responses.code()) == 200) {
            bmp = BitmapFactory.decodeStream(responses.body().byteStream());
        }
        return bmp;

    }

    public static void deleteLogin(Context ctx) {
        DataBaseManager dbm = DataBaseManager.getInstance(ctx);
        dbm.open();
        dbm.deleteLogin();
        dbm.close();
    }

    public static Customer getLogin(Context ctx) {
        int itemId;
        String usr;
        String psw;
        String realPsw;
        Customer item = null;
        DataBaseManager dbm = DataBaseManager.getInstance(ctx);
        dbm.open();
        Cursor cursor = dbm.cursorLogin();
        cursor.moveToFirst();
        if(!cursor.isAfterLast()){
            itemId = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_ROWID));
            usr = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_USER));
            psw = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_PASSWORD));
            realPsw = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_NOT_ENCRIPTED_PASSWORD));
            //	item = new ItemDto(itemId, name, description, identification, latitude, longitude);
            item = new Customer();
            item.setEmail(usr);
            item.setPassword(psw);
            item.setId(itemId);
            item.setNotEncriptedPassword(realPsw);
        }
        cursor.close();
        dbm.close();
        return item;
    }


    public static void inicializarBD(DataBaseManager mDBManager){
        mDBManager.open();
    }

    public static void upgradeBD(DataBaseManager mDBManager){
        mDBManager.upgradeDB();
    }

    public static void createBD(DataBaseManager mDBManager){
        mDBManager.createBD();
    }

    public static void finalizarBD(DataBaseManager mDBManager){
        if(mDBManager != null){
            mDBManager.close();
        }
    }

}
