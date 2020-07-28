package com.boxico.android.kn.funforlabelapp.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.net.Uri;
import android.os.Environment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;


import com.boxico.android.kn.funforlabelapp.R;
import com.boxico.android.kn.funforlabelapp.ddbb.DataBaseManager;
import com.boxico.android.kn.funforlabelapp.dtos.AddressBook;
import com.boxico.android.kn.funforlabelapp.dtos.Category;
import com.boxico.android.kn.funforlabelapp.dtos.Creator;
import com.boxico.android.kn.funforlabelapp.dtos.Customer;
import com.boxico.android.kn.funforlabelapp.dtos.LabelAttributes;
import com.boxico.android.kn.funforlabelapp.dtos.LabelFont;
import com.boxico.android.kn.funforlabelapp.dtos.LabelImage;
import com.boxico.android.kn.funforlabelapp.dtos.MetodoEnvio;
import com.boxico.android.kn.funforlabelapp.dtos.MetodoPago;
import com.boxico.android.kn.funforlabelapp.dtos.Product;
import com.boxico.android.kn.funforlabelapp.dtos.ProductoCarrito;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ConstantsAdmin {

    public static final String URL = "http://test.funforlabels.com/funforlabelsApp/";
    public static final String PROPERTIES_FILE = "funforlabels.properties";
    public static final String ATR_URL_IMAGES = "URL_IMAGES";
    public static final String ATR_URL_FONTS = "URL_FONTS";
    public static final String ATR_URL_LABEL_THUMBS = "URL_LABEL_THUMBS";
    public static final String ATR_URL_LABEL_IMAGES = "URL_LABEL_IMAGES";
    public static final long tokenFFL = 27029085;
    public static final String ATR_SMTP_SERVER ="SMTP_SERVER";
    public static final String ATR_SMTP_PORT = "SMTP_PORT";
    public static final String ATR_SMTP_SOCKETPORT = "SMTP_SOCKETPORT";
    public static final String ATR_FFL_MAIL = "FFL_MAIL";
    public static final String ATR_FFL_PASSWORD = "FFL_PASSWORD";
    public static final String CAPITAL_FEDERAL = "Capital Federal" ;
    public static final String ARGENTINA = "Argentina" ;
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
    public static final String KEY_ID_PRODUCTO = "idProducto";
    public static final String KEY_ID_FILLS_TEXTURED = "idFillsTextured";
    public static final String KEY_ID_FONT_TEXT = "idFontText";
    public static final String KEY_ID_FONT_TITLE = "idFontTitle";
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
    public static final String KEY_CANTIDAD_PRODUCTO_PORPACK = "cantidadPorPack" ;
    public static final String KEY_MODELO_PRODUCTO = "modelo" ;
    public static final String INTRO_ENVIO = "DIRECCION_ENVIO_INTRO";
    public static final String INTRO_PAGO = "METODO_PAGO_INTRO";
    public static final String TITULO_MP__DETALLE_TAGS = "TITULO_MP__DETALLE_TAGS";
    public static final String URL_MERCADO_PAGO = "https://api.mercadopago.com/checkout/preferences?access_token=";
    public static final String TITULO_MP__DETALLE_ENVIO = "TITULO_MP__DETALLE_ENVIO";
    public static final int ACTIVITY_EJECUTAR_ACERCA_DE = 1;
    public static final int ACTIVITY_EJECUTAR_CERRAR_SESSION = 2;
    public static final String ACERCADE_TITULO_ACERCA_DE = "TITULO_ACERCA_DE";
    public static final String ACERCADE_QUIENES_SOMOS_TEXTO = "QUIENES_SOMOS_TEXTO";
    public static final String ACERCADE_POLITICA_PRIVACIDAD_LINK = "POLITICA_PRIVACIDAD_LINK";
    public static final String ACERCADE_ENVIO_DEVOLUCIONES_LINK = "ENVIO_DEVOLUCIONES_LINK";
    public static final String ACERCADE_CONDICIONES_USO_LINK = "CONDICIONES_USO_LINK";
    public static final String ACERCADE_FORMAS_PAGO_LINK = "FORMAS_PAGO_LINK";
    public static final String ACERCADE_PREGUNTAS_FRECUENTES_LINK = "PREGUNTAS_FRECUENTES_LINK";
    public static final String ACERCADE_TEXTO_ENVIO_WSP = "TEXTO_ENVIO_WSP";
    public static final String ACERCADE_TEXTO_ENVIO_MAIL = "TEXTO_ENVIO_MAIL";
    public static final String URL_INSTAGRAM = "URL_INSTAGRAM" ;
    public static final String URL_FACEBOOK = "URL_FACEBOOK" ;


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
  //  public static long ID_CREATOR_MINICIRCULARES = 59;
    public static Properties fflProperties;

    public static boolean finalizarHastaMenuPrincipal;
    public static Customer currentCustomer = null;
    public static List<ProductoCarrito> productosDelCarrito = new ArrayList<ProductoCarrito>();

    public static Product currentProduct;
    public static String selectedBackgroundFilename;
    public static Bitmap selectedBackground;
    public static float selectedTitleFontSize;
    public static int selectedTitleFontColor;
    public static LabelFont selectedTitleFont;
    public static int selectedTextFontColor;
    public static float selectedTextFontSize;
    public static LabelFont selectedTextFont;
    public static LabelAttributes selectedLabelAttrbText;
    public static LabelAttributes selectedLabelAttrbTitle;
    public static Creator currentCreator;
    public static String textEntered;
    public static String titleEntered;
    public static AddressBook addressCustomer;
    public static MetodoPago selectedPaymentMethod;
    public static MetodoEnvio selectedShippingMethod;
    public static String comentarioIngresado;
    public static String CURRENCY = "CURRENCY";
    public static String CURRENCY_VALUE = "CURRENCY_VALUE";
    public static String ORDER_STATUS_PENDING_TRANSFERENCE = "ORDER_STATUS_PENDING_TRANSFERENCE";
    public static String ORDER_STATUS_MERCADO_PAGO_CANCELED = "ORDER_STATUS_MERCADO_PAGO_CANCELED";
    public static String TAG_LEGEND_TYPE_TEXT = "TAG_LEGEND_TYPE_TEXT";
    public static String TAG_LEGEND_TYPE_TITLE = "TAG_LEGEND_TYPE_TITLE";
    public static LabelImage selectedImage;

    public static String MENSAJE_EXITO_ORDEN_GENERADA1="MENSAJE_EXITO_ORDEN_GENERADA1";
    public static String MENSAJE_EXITO_ORDEN_GENERADA2="MENSAJE_EXITO_ORDEN_GENERADA2";
    public static String MAIL_PROCESO_ORDEN_SUBJECT="MAIL_PROCESO_ORDEN_SUBJECT";
    public static String MAIL_PROCESO_ORDEN_NRO="MAIL_PROCESO_ORDEN_NRO";
    public static String MAIL_PROCESO_ORDEN_FACTURA_DETALLE="MAIL_PROCESO_ORDEN_FACTURA_DETALLE";
    public static String MAIL_PROCESO_ORDEN_FECHA="MAIL_PROCESO_ORDEN_FECHA";
    public static String MAIL_PROCESO_ORDEN_COMENTARIO="MAIL_PROCESO_ORDEN_COMENTARIO";
    public static String MAIL_PROCESO_ORDEN_PRODUCTOS="MAIL_PROCESO_ORDEN_PRODUCTOS";
    public static String MAIL_PROCESO_ORDEN_SUBTOTAL="MAIL_PROCESO_ORDEN_SUBTOTAL";
    public static String MAIL_PROCESO_ORDEN_TOTAL="MAIL_PROCESO_ORDEN_TOTAL";
    public static String MAIL_PROCESO_ORDEN_DIR_ENTREGA="MAIL_PROCESO_ORDEN_DIR_ENTREGA";
    public static String MAIL_PROCESO_ORDEN_DIR_FACTURACION="MAIL_PROCESO_ORDEN_DIR_FACTURACION";
    public static String MAIL_PROCESO_ORDEN_METODO_PAGO="MAIL_PROCESO_ORDEN_METODO_PAGO";
    public static String URL_DETALLE_ORDEN="URL_DETALLE_ORDEN";
    public static final String url_whatsapp ="https://api.whatsapp.com/";
    public static final String TEL_WSP ="TEL_FFLABEL";
    public static final String ID_PRODUCTS_COMBO ="ID_PRODUCTS_COMBO";
    public static final String ID_CATEGORY_COMBO ="ID_CATEGORY_COMBO";
    public static Bitmap screenShot;
    public static String mensajeCompra = "";
    public static boolean compraExitosa = true;

    private static ArrayList<LabelImage> capturas = null;

    public static ArrayList<LabelImage> getCapturas() {
        if(capturas == null){
            capturas = new ArrayList<LabelImage>();
        }
        return capturas;
    }

    public static String convertIntColorToHex(int color){
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        return hexColor;
    }

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
     //   if (currentC.getId() != ConstantsAdmin.ID_CREATOR_MINICIRCULARES) {
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
     /*   }else{
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
        }*/
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
      //  if (idCreator != ConstantsAdmin.ID_CREATOR_MINICIRCULARES) {
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
     /*   }else{
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
        }*/
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
      //  if (currentC.getId() != ConstantsAdmin.ID_CREATOR_MINICIRCULARES) {// NO ES EL CREADOR DE MINI-CIRCULARES
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
     /*   } else {
            float temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, currentC.getWidth(),
                    context.getResources().getDisplayMetrics());

            temp = temp * ConstantsAdmin.PARAM_TO_INCREASE;

            realWidthImage = (int) temp;
            temp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, currentC.getHeight(),
                    context.getResources().getDisplayMetrics());
            temp = temp * ConstantsAdmin.PARAM_TO_INCREASE;
            realHeightImage = (int) temp;
        }*/
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
      //  if (idCreator != ConstantsAdmin.ID_CREATOR_MINICIRCULARES) {// NO ES EL CREADOR DE MINI-CIRCULARES
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
     /*   } else {
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
        }*/
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

    public static boolean enviarMail(String subject, String body, String to){
        boolean okSend = false;
        KNMail m = new KNMail(ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_FFL_MAIL), ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_FFL_PASSWORD));
        String[] toArr = {to};
        m.setTo(toArr);
        m.setFrom(ConstantsAdmin.fflProperties.getProperty(ConstantsAdmin.ATR_FFL_MAIL));
            //    m.setFrom("info@funforlabels.com");
        m.setSubject(subject);
        m.setBody(body);
        try {
            okSend = m.send();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return okSend;
    }



    public static void enviarMailGenerico(Activity activity, String emailaddress, String body, String subject) {
        // TODO Auto-generated method stub

        try {
            String[] address = {emailaddress};
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND); //This is the email intent
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, address); // adds the address to the intent
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);//the subject
            //emailIntent.setPackage("com.whatsapp");
            emailIntent.setType("text/plain");
            //	PackageManager pm = activity.getPackageManager();
            //	PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //	emailIntent.setPackage("com.whatsapp");
            if(body == null || body.equals("")){
                body = "Hi!";
            }
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body); //adds the body of the mail
            activity.startActivity(Intent.createChooser(emailIntent, null));

        }catch(Exception exc){
            exc.printStackTrace();
        }
        //activity.startActivity(emailIntent);
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

    public static void saveBitmapInRemoteServer(Bitmap bitmap, String attachmentName, String attachmentFileName) throws IOException {
//        String attachmentName = "bitmap";
//        String attachmentFileName = "bitmap.bmp";
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";

        HttpURLConnection httpUrlConnection = null;
        URL url = new URL(fflProperties.getProperty(ATR_URL_LABEL_THUMBS));
        httpUrlConnection = (HttpURLConnection) url.openConnection();
        httpUrlConnection.setUseCaches(false);
        httpUrlConnection.setDoOutput(true);

        httpUrlConnection.setRequestMethod("POST");
        httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
        httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
        httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

        DataOutputStream request = new DataOutputStream(
                httpUrlConnection.getOutputStream());

        request.writeBytes(twoHyphens + boundary + crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"" +
                attachmentName + "\";filename=\"" +
                attachmentFileName + "\"" + crlf);
        request.writeBytes(crlf);

//I want to send only 8 bit black & white bitmaps
        byte[] pixels = new byte[bitmap.getWidth() * bitmap.getHeight()];
        for (int i = 0; i < bitmap.getWidth(); ++i) {
            for (int j = 0; j < bitmap.getHeight(); ++j) {
                //we're interested only in the MSB of the first byte,
                //since the other 3 bytes are identical for B&W images
                pixels[i + j] = (byte) ((bitmap.getPixel(i, j) & 0x80) >> 7);
            }
        }

        request.write(pixels);

        request.writeBytes(crlf);
        request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);

        request.flush();
        request.close();


        InputStream responseStream = new
                BufferedInputStream(httpUrlConnection.getInputStream());

        BufferedReader responseStreamReader =
                new BufferedReader(new InputStreamReader(responseStream));

        String line = "";
        StringBuilder stringBuilder = new StringBuilder();

        while ((line = responseStreamReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        responseStreamReader.close();

        String response = stringBuilder.toString();

        responseStream.close();

        httpUrlConnection.disconnect();

        /*

    PS: Of course I had to wrap the request in private class AsyncUploadBitmaps extends AsyncTask<Bitmap, Void, String>,
    in order to make the Android platform happy, because it doesn't like to have network requests on the main thread.
         */
    }

    public static Bitmap takeScreenshot(Activity context) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyyMMddhhmmss", now);
        Bitmap bitmap = null;
        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".png";

            // create bitmap screen capture
            View v1 = context.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
        return bitmap;
    }


    public static Bitmap takeScreenshot(View v) {
       Bitmap bmp = null;
       v.setDrawingCacheEnabled(true);
       bmp = Bitmap.createBitmap(v.getDrawingCache());
       v.setDrawingCacheEnabled(false);
       return bmp;
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
        int idProducto;
        int idFillsTextured;
        int idFontText;
        int idFontTitle;
        String nombre;
        String precio;
        String cantidad;
        String cantidadPorPack;
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
                idFontTitle = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_ID_FONT_TITLE));
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
                item.setFontTitleId(idFontTitle);
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
            cantidadPorPack = cursor.getString(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_CANTIDAD_PRODUCTO_PORPACK));
            idProducto = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_ID_PRODUCTO));
            idFillsTextured = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_ID_FILLS_TEXTURED));
            idFontText = cursor.getInt(cursor.getColumnIndexOrThrow(ConstantsAdmin.KEY_ID_FONT_TEXT));
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
            item.setIdProduct(idProducto);
            item.setFillsTexturedId(idFillsTextured);
            item.setFontTextId(idFontText);
            item.setCantidadPorPack(cantidadPorPack);
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

    public static String getFechaYHoraActual() {
        String fecha = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        return formatter.format(date);

    }
}
