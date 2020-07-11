package com.boxico.android.kn.funforlabelapp.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Environment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;


import com.boxico.android.kn.funforlabelapp.ddbb.DataBaseManager;
import com.boxico.android.kn.funforlabelapp.dtos.Category;
import com.boxico.android.kn.funforlabelapp.dtos.Creator;
import com.boxico.android.kn.funforlabelapp.dtos.Customer;
import com.boxico.android.kn.funforlabelapp.dtos.LabelAttributes;
import com.boxico.android.kn.funforlabelapp.dtos.Product;
import com.boxico.android.kn.funforlabelapp.dtos.ProductoCarrito;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    public static final String KEY_USER = "usuario";
    public static final String KEY_PASSWORD = "contrasenia";
    public static final String KEY_NOT_ENCRIPTED_PASSWORD = "contraseniaSinEncriptar";
    public static final String KEY_ROWID = "rowId" ;

    public static final String KEY_TEXTO = "texto";
    public static final String KEY_TITULO = "titulo" ;
    public static final String KEY_TITULO_TAMANIO = "tituloSize";
    public static final String KEY_TEXTO_TAMANIO = "textoSize";
    public static final String KEY_TITULO_FUENTE = "tituloFuente";
    public static final String KEY_TEXTO_FUENTE = "textoFuente";
    public static final String KEY_TITULO_COLOR = "tituloColor";
    public static final String KEY_TEXTO_COLOR = "textoColor";
    public static final String KEY_BACKGROUND_FILENAME = "backgroundFilename";
    public static final String KEY_ID_CREATOR = "idCreador";
    public static final String KEY_ID_AREA_TITULO = "idAreaTitulo";
    public static final String KEY_ID_AREA_TEXTO = "idAreaTexto";
    public static final String KEY_COMENTARIO_USR = "comentarioUsr";
    public static final String KEY_TIENE_TITULO = "tieneTitulo" ;
    public static final String KEY_WIDTH_TAG = "widthTag" ;
    public static final String KEY_HEIGHT_TAG = "heightTag" ;
    public static final String KEY_ROUND = "roundValor";
    public static final String KEY_WIDTH_AREA_TEXTO = "widthAreaTexto" ;
    public static final String KEY_HEIGHT_AREA_TEXTO = "heightAreaTexto";
    public static final String KEY_ES_MULTILINEA_TEXTO = "esMultilineaTexto" ;
    public static final String KEY_FROM_X_TEXTO = "fromXTexto";
    public static final String KEY_FROM_Y_TEXTO = "fromYTexto";
    public static final String KEY_WIDTH_AREA_TITULO = "widthAreaTitulo" ;
    public static final String KEY_HEIGHT_AREA_TITULO = "heightAreaTitulo";
    public static final String KEY_ES_MULTILINEA_TITULO = "esMultilineaTitulo" ;
    public static final String KEY_FROM_X_TITULO = "fromXTitulo";
    public static final String KEY_FROM_Y_TITULO = "fromYTitulo";
    public static final String KEY_NOMBRE_PRODUCTO = "nombreProducto" ;
    public static final String KEY_PRECIO_PRODUCTO = "precio" ;
    public static final String KEY_CANTIDAD_PRODUCTO = "cantidad" ;
    public static final String KEY_MODELO_PRODUCTO = "modelo" ;
    public static final String INTRO_ENVIO = "DIRECCION_ENVIO_INTRO";


    public static String mensaje = null;
    public static final String TAG = "DataBaseManager";
    public static final String DATABASE_NAME = "FunForLabelsAppDB";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_LOGIN = "tabla_login";
    public static final String TABLE_PRODUCTO_CARRITO = "tabla_producto_carrito";

    public static final String KEY_NAME = "name";
    public static final String ENTER = "\n";
    public static final String TAB = "\t";
    public static final String GEOUSERNAME = "andreacg1978";
    public static final String GEOAPIURL = "http://api.geonames.org/";
    public static final String GEOCODIGOARGENTINA = "AR";
    public static final String GEOAPITOGETPROVINCIA = "childrenJSON?geonameId=";
    public static final String GEOIDCAPITALFEDERAL = "3433955";

    public static boolean customerJustCreated = false;
    public static long[] categories = {46};
    public static long currentLanguage;
    public static Category currentCategory;

    public static double MILLS_TO_PXL = 3.7795275591;
    public static String[] FONT_SIZES= {"8","10","12","14","16","18","20","22"};
    public static long ID_CREATOR_MINICIRCULARES = 59;
    public static Properties fflProperties;

    public static boolean finalizarHastaMenuPrincipal;
    public static Customer currentCustomer = null;
    public static List<ProductoCarrito> productosDelCarrito = new ArrayList<ProductoCarrito>();

    public static Product currentProduct;
    public static String selectedBackgroundFilename;
    public static Bitmap selectedBackground;
    public static float selectedTitleFontSize;
    public static int selectedTitleFontColor;
    public static String selectedTitleFont;
    public static int selectedTextFontColor;
    public static float selectedTextFontSize;
    public static String selectedTextFont;
    public static LabelAttributes selectedLabelAttrbText;
    public static LabelAttributes selectedLabelAttrbTitle;
    public static Creator currentCreator;
    public static String textEntered;
    public static String titleEntered;


    public static String ENVIO1_TITULO = "ENVIO1_TITULO";
    public static String ENVIO2_TITULO = "ENVIO2_TITULO";
    public static String ENVIO3_TITULO = "ENVIO3_TITULO";
    public static String ENVIO4_TITULO = "ENVIO4_TITULO";
    public static String ENVIO5_TITULO = "ENVIO5_TITULO";


    public static String ENVIO1_DESC = "ENVIO1_DESC";
    public static String ENVIO2_DESC = "ENVIO2_DESC";
    public static String ENVIO3_DESC = "ENVIO3_DESC";
    public static String ENVIO4_DESC = "ENVIO4_DESC";
    public static String ENVIO5_DESC = "ENVIO5_DESC";

    public static float pxToMm(float px, Context context){
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return px / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1, dm);
    }

    private static Bitmap getRoundedCornerBitmap(Bitmap bitmap,int roundPixelSize) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = roundPixelSize;
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF,roundPx,roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static EditText createTextArea(EditText ta, LabelAttributes la, String hint, Creator currentC, boolean acot, RelativeLayout rl, Activity context) {
        //EditText ta = new EditText(this);
        ta.setHint(hint);
        ta.setHintTextColor(Color.GRAY);
        float temp = 0;
        int w, h = 0;
        if (currentC.getId() != ConstantsAdmin.ID_CREATOR_MINICIRCULARES) {
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, la.getWidth(),
                    context.getResources().getDisplayMetrics());
            if (acot) {
                temp = temp - temp * 3 / 18;
            }
            w = (int) (temp);
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, la.getHeight() + 1,
                    context.getResources().getDisplayMetrics());
            if (acot) {
                temp = temp - temp * 3 / 19;
            }
            h = (int) (temp);
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, la.getFromY(),
                    context.getResources().getDisplayMetrics());
            if (acot) {
                temp = temp - temp * 3 / 19;
            }
            ta.setY(temp);


            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, la.getFromX(),
                    context.getResources().getDisplayMetrics());
            if (acot) {
                temp = temp - temp * 3 / 22;
            }
            ta.setX(temp);
        }else{
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, la.getWidth() ,
                    context.getResources().getDisplayMetrics());
            temp = temp * ConstantsAdmin.PARAM_TO_INCREASE;

            w = (int) (temp);
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, la.getHeight(),
                    context.getResources().getDisplayMetrics());
            temp = temp * ConstantsAdmin.PARAM_TO_INCREASE;
            h = (int) (temp);
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, la.getFromY(),
                    context.getResources().getDisplayMetrics());
            temp = temp * ConstantsAdmin.PARAM_TO_INCREASE;
            ta.setY(temp);

            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, la.getFromX(),
                    context.getResources().getDisplayMetrics());

            temp = temp * ConstantsAdmin.PARAM_TO_INCREASE;

            ta.setX(temp);
        }
        ViewGroup.LayoutParams layoutParamsTextTag = new ViewGroup.LayoutParams(w, h);
        ta.setLayoutParams(layoutParamsTextTag);
        ta.setGravity(Gravity.CENTER);
        ta.setPadding(0, 0, 0, 0);
        ta.setBackgroundColor(Color.TRANSPARENT);
        ta.setTextColor(Color.BLACK);
        ta.setEllipsize(TextUtils.TruncateAt.END);
        if (la.getMultiline() == 0) {
            ta.setSingleLine(true);
            ta.setEllipsize(TextUtils.TruncateAt.END);
            ta.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            ta.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ta.setSingleLine(false);
        }
        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.TRANSPARENT); //white background
        border.setStroke(3, Color.RED); //black border with full opacity

        rl.addView(ta);
        return ta;
    }


    public static EditText createTextArea(float achicar, EditText ta, String hint, int idCreator, int anchoArea, int altoArea, int fromX, int fromY, int esMultilinea, boolean acot, RelativeLayout rl, Activity context) {
        //EditText ta = new EditText(this);
        ta.setHint(hint);
        ta.setHintTextColor(Color.GRAY);
        float temp = 0;
        int w, h = 0;
        if (idCreator != ConstantsAdmin.ID_CREATOR_MINICIRCULARES) {
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, anchoArea,
                    context.getResources().getDisplayMetrics());
            if (acot) {
                temp = temp - temp * 3 / 18;
            }
            temp = temp * achicar;
            w = (int) (temp);
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, altoArea + 1,
                    context.getResources().getDisplayMetrics());
            if (acot) {
                temp = temp - temp * 3 / 19;
            }
            temp = temp * achicar;
            h = (int) (temp);
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, fromY,
                    context.getResources().getDisplayMetrics());
            if (acot) {
                temp = temp - temp * 3 / 19;
            }
            temp = temp * achicar;
            ta.setY(temp);


            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, fromX,
                    context.getResources().getDisplayMetrics());
            if (acot) {
                temp = temp - temp * 3 / 22;
            }
            temp = temp * achicar;
            ta.setX(temp);
        }else{
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, anchoArea ,
                    context.getResources().getDisplayMetrics());
            temp = temp * ConstantsAdmin.PARAM_TO_INCREASE;
            temp = temp * achicar;
            w = (int) (temp);
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, altoArea,
                    context.getResources().getDisplayMetrics());
            temp = temp * ConstantsAdmin.PARAM_TO_INCREASE;
            temp = temp * achicar;
            h = (int) (temp);
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, fromY,
                    context.getResources().getDisplayMetrics());
            temp = temp * ConstantsAdmin.PARAM_TO_INCREASE;
            temp = temp * achicar;
            ta.setY(temp);

            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, fromX,
                    context.getResources().getDisplayMetrics());

            temp = temp * ConstantsAdmin.PARAM_TO_INCREASE;
            temp = temp * achicar;
            ta.setX(temp);
        }
        ViewGroup.LayoutParams layoutParamsTextTag = new ViewGroup.LayoutParams(w, h);
        ta.setLayoutParams(layoutParamsTextTag);
        ta.setGravity(Gravity.CENTER);
        ta.setPadding(0, 0, 0, 0);
        ta.setBackgroundColor(Color.TRANSPARENT);
        ta.setTextColor(Color.BLACK);
        ta.setEllipsize(TextUtils.TruncateAt.END);
        if (esMultilinea == 0) {
            ta.setSingleLine(true);
            ta.setEllipsize(TextUtils.TruncateAt.END);
            ta.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            ta.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ta.setSingleLine(false);
        }
        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.TRANSPARENT); //white background
        border.setStroke(3, Color.RED); //black border with full opacity

        rl.addView(ta);
        return ta;
    }


    public static void customizeBackground(Bitmap img, Creator currentC, boolean acot, RelativeLayout rl, Activity context) {
        int realWidthImage = 0;
        int realHeightImage = 0;
        if (currentC.getId() != ConstantsAdmin.ID_CREATOR_MINICIRCULARES) {// NO ES EL CREADOR DE MINI-CIRCULARES
            float temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, currentC.getWidth(),
                    context.getResources().getDisplayMetrics());
            if (acot) {
                temp = temp - temp * 3 / 19;
            }
            realWidthImage = (int) temp;
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, currentC.getHeight(),
                    context.getResources().getDisplayMetrics());

            if (acot) {
                temp = temp - temp * 3 / 19;
            }
            realHeightImage = (int) temp;
        } else {
            float temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, currentC.getWidth(),
                    context.getResources().getDisplayMetrics());

            temp = temp * ConstantsAdmin.PARAM_TO_INCREASE;

            realWidthImage = (int) temp;
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, currentC.getHeight(),
                    context.getResources().getDisplayMetrics());
            temp = temp * ConstantsAdmin.PARAM_TO_INCREASE;
            realHeightImage = (int) temp;
        }
        Bitmap b = Bitmap.createScaledBitmap(img, realWidthImage, realHeightImage, false);
        if (currentC.getRounded() == 1) {
            b = getRoundedCornerBitmap(b, currentC.getRound());
        }

        Drawable d = new BitmapDrawable(context.getResources(), b);
        rl.setBackground(d);

    }


    public static void customizeBackground(float achicar, Bitmap img, int w, int h, long idCreator, int round, boolean acot, RelativeLayout rl, Activity context) {
        int realWidthImage = 0;
        int realHeightImage = 0;
        if (idCreator != ConstantsAdmin.ID_CREATOR_MINICIRCULARES) {// NO ES EL CREADOR DE MINI-CIRCULARES
            float temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, w,
                    context.getResources().getDisplayMetrics());
            if (acot) {
                temp = temp - temp * 3 / 19;
            }
            temp = temp * achicar;
            realWidthImage = (int) temp;
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, h,
                    context.getResources().getDisplayMetrics());

            if (acot) {
                temp = temp - temp * 3 / 19;
            }
            temp = temp * achicar;
            realHeightImage = (int) temp;
        } else {
            float temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, w,
                    context.getResources().getDisplayMetrics());

            temp = temp * ConstantsAdmin.PARAM_TO_INCREASE;
            temp = temp * achicar;
            realWidthImage = (int) temp;
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM,h,
                    context.getResources().getDisplayMetrics());
            temp = temp * ConstantsAdmin.PARAM_TO_INCREASE;
            temp = temp * achicar;
            realHeightImage = (int) temp;
        }
        Bitmap b = Bitmap.createScaledBitmap(img, realWidthImage, realHeightImage, false);
        if (round > 0) {
            b = getRoundedCornerBitmap(b, round);
        }

        Drawable d = new BitmapDrawable(context.getResources(), b);
        rl.setBackground(d);

    }

    public static void createLogin(Customer currentCustomer, Context ctx) {
        DataBaseManager dbm = DataBaseManager.getInstance(ctx);
        dbm.open();
        dbm.createLogin(currentCustomer);
        dbm.close();
    }

    public static void createProductoCarrito(ProductoCarrito pc, Context ctx) {
        DataBaseManager dbm = DataBaseManager.getInstance(ctx);
        dbm.open();
        dbm.createProductoCarrito(pc);
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

    public static void copyBitmapInStorage(Bitmap bmp, String filename){
       // String root = Environment.getExternalStorageDirectory().toString();
        String completeFilename = Environment
                .getExternalStorageDirectory().toString() +"/"
                + filename;
        File newFile = new File(completeFilename);
        if (newFile.exists())
            newFile.delete();
        try {
            FileOutputStream out = new FileOutputStream(newFile);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);


                // Output stream
                OutputStream output = new FileOutputStream(filename);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                }
                output.flush();
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

    public static void deleteProductoCarrito(Context ctx, ProductoCarrito pc) {
        DataBaseManager dbm = DataBaseManager.getInstance(ctx);
        dbm.open();
        dbm.deleteProductoCarrito(pc);
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

    public static ArrayList<ProductoCarrito> getCarrito(Context ctx) {
        ArrayList<ProductoCarrito> listaProductos = new ArrayList<ProductoCarrito>();
        int itemId;
        String texto;
        String titulo;
        String textoFuente;
        String tituloFuente;
        String textoSize;
        String tituloSize;
        int textoColor;
        int tituloColor;
        int tieneTitulo;
        String backgroundFilename;
        String comentario;
        int idCreador;
        int idAreaTitulo;
        int idAreaTexto;
        int anchoTexto;
        int largoTexto;
        int fromXTexto;
        int fromYTexto;
        int esMultilineaTexto;
        int anchoTitulo;
        int largoTitulo;
        int fromXTitulo;
        int fromYTitulo;
        int esMultilineaTitulo;
        int anchoTag;
        int largoTag;
        int round;
        String nombre;
        String precio;
        String cantidad;
        String modelo;
        ProductoCarrito item = null;
        DataBaseManager dbm = DataBaseManager.getInstance(ctx);
        dbm.open();
        Cursor cursor = dbm.cursorProductoCarrito();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            item = new ProductoCarrito();
            itemId = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_ROWID));
            tieneTitulo = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_TIENE_TITULO));
            if(tieneTitulo == 1){
                item.setTieneTitulo(true);
                titulo = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_TITULO));
                tituloFuente = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_TITULO_FUENTE));
                tituloSize = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_TITULO_TAMANIO));
                tituloColor = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_TITULO_COLOR));
                idAreaTitulo = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_ID_AREA_TITULO));
                anchoTitulo = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_WIDTH_AREA_TITULO));
                largoTitulo = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_HEIGHT_AREA_TITULO));
                fromXTitulo = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_FROM_X_TITULO));
                fromYTitulo = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_FROM_Y_TITULO));
                esMultilineaTitulo = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_ES_MULTILINEA_TITULO));
                item.setTitulo(titulo);
                item.setTitleFontName(tituloFuente);
                item.setTitleFontSize(Float.valueOf(tituloSize));
                item.setFontTitleColor(tituloColor);
                item.setIdAreaTitulo(idAreaTitulo);
                item.setAnchoAreaTituto(anchoTitulo);
                item.setLargoAreaTituto(largoTitulo);
                item.setFromXTituto(fromXTitulo);
                item.setFromYTituto(fromYTitulo);
                item.setEsMultilineaTituto(esMultilineaTitulo);
            }else{
                item.setTieneTitulo(false);
            }
            texto = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_TEXTO));
            textoFuente = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_TEXTO_FUENTE));
            textoSize = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_TEXTO_TAMANIO));
            textoColor = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_TEXTO_COLOR));
            idAreaTexto = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_ID_AREA_TEXTO));
            anchoTexto = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_WIDTH_AREA_TEXTO));
            largoTexto = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_HEIGHT_AREA_TEXTO));
            fromXTexto = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_FROM_X_TEXTO));
            fromYTexto = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_FROM_Y_TEXTO));
            esMultilineaTexto = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_ES_MULTILINEA_TEXTO));
            round = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_ROUND));
            anchoTag = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_WIDTH_TAG));
            largoTag = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_HEIGHT_TAG));
            nombre = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_NOMBRE_PRODUCTO));
            modelo = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_MODELO_PRODUCTO));
            precio = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_PRECIO_PRODUCTO));
            cantidad = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_CANTIDAD_PRODUCTO));
            item.setRound(round);
            item.setAnchoTag(anchoTag);
            item.setLargoTag(largoTag);
            item.setTexto(texto);
            item.setTextFontName(textoFuente);
            item.setTextFontSize(Float.valueOf(textoSize));
            item.setFontTextColor(textoColor);
            item.setIdAreaTexto(idAreaTexto);
            item.setAnchoAreaTexto(anchoTexto);
            item.setLargoAreaTexto(largoTexto);
            item.setFromXTexto(fromXTexto);
            item.setFromYTexto(fromYTexto);
            item.setEsMultilineaTexto(esMultilineaTexto);
            item.setNombre(nombre);
            item.setCantidad(cantidad);
            item.setModelo(modelo);
            item.setPrecio(precio);
            backgroundFilename = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_BACKGROUND_FILENAME));
            comentario = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_COMENTARIO_USR));
            idCreador = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_ID_CREATOR));
            item.setBackgroundFilename(backgroundFilename);
            item.setComentarioUsr(comentario);
            item.setIdCreador(idCreador);
            item.setId(itemId);
            listaProductos.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        dbm.close();
        return listaProductos;
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

    public static void agregarProductoAlCarrito(ProductoCarrito pc) {
        if(productosDelCarrito == null){
            productosDelCarrito = new ArrayList<ProductoCarrito>();
        }
        productosDelCarrito.add(pc);
    }

    public static Bitmap getImageFromStorage(String backgroundFilename) {
        Bitmap img = null;
        File f = getFile(backgroundFilename);
        try {
            img = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return img;
    }

    public static void clearSelections() {
        currentProduct = null;
        String selectedBackgroundFilename= null;
        selectedBackground= null;
        selectedTitleFontSize= 0;
        selectedTitleFontColor= -1;
        selectedTitleFont= null;
        selectedTextFontColor= -1;
        selectedTextFontSize= -1;
        selectedTextFont= null;
        selectedLabelAttrbText= null;
        selectedLabelAttrbTitle= null;
        currentCreator= null;
        textEntered= null;
        titleEntered= null;

    }
}
