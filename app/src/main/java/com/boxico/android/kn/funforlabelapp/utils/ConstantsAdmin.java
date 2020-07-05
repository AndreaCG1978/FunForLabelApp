package com.boxico.android.kn.funforlabelapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;


import com.boxico.android.kn.funforlabelapp.ddbb.DataBaseManager;
import com.boxico.android.kn.funforlabelapp.dtos.Category;
import com.boxico.android.kn.funforlabelapp.dtos.Customer;
import com.boxico.android.kn.funforlabelapp.dtos.Product;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ConstantsAdmin {

    public static final String URL = "http://test.funforlabels.com/funforlabelsApp/";
    public static final String PROPERTIES_FILE = "funforlabels.properties";
    public static final String ATR_URL_IMAGES = "URL_IMAGES";
    public static final String ATR_URL_FONTS = "URL_FONTS";
    public static final String ATR_URL_LABEL_IMAGES = "URL_LABEL_IMAGES";
    public static final long tokenFFL = 27029085;
    public static final String ATR_SMTP_SERVER ="SMTP_SERVER";
    public static final String ATR_SMTP_PORT = "SMTP_PORT";
    public static final String ATR_SMTP_SOCKETPORT = "SMTP_SOCKETPORT";
    public static final String ATR_FFL_MAIL = "FFL_MAIL";
    public static final String ATR_FFL_PASSWORD = "FFL_PASSWORD";
    public static final String CAPITAL_FEDERAL = "Capital Federal" ;
    public static final float PARAM_TO_INCREASE = 1.465f;
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
    public static double MILLS_TO_PXL = 3.7795275591;
    public static String[] FONT_SIZES= {"8","10","12","14","16","18","20","22"};
    public static long ID_CREATOR_MINICIRCULARES = 59;
    public static Properties fflProperties;


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

    public static File getFile(String filename){
        String completeFilename = Environment
                .getExternalStorageDirectory().toString() +"/"
                + filename;
        File f = new File(completeFilename);
        return f;
    }

    public static void copyFileFromUrl(String urlPath, String fontFilename){
        int count;
        try {
            String filename = Environment
                    .getExternalStorageDirectory().toString() +"/"
                    + fontFilename;
            File f = new File(filename);
            if(!f.exists()) {
                java.net.URL url = new URL(urlPath);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
            //    int lenghtOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);


                // Output stream
                OutputStream output = new FileOutputStream(filename);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    //  publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



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
