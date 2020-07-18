package com.boxico.android.kn.funforlabelapp.ddbb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;

class DataBaseHelper extends SQLiteOpenHelper {




    private static final String DATABASE_CREATE_LOGIN = "create table if not exists " + ConstantsAdmin.TABLE_LOGIN +
            "(" + ConstantsAdmin.KEY_ROWID +" integer, "
            + ConstantsAdmin.KEY_USER + " text, "
            + ConstantsAdmin.KEY_NOT_ENCRIPTED_PASSWORD + " text, "
            + ConstantsAdmin.KEY_PASSWORD + " text); ";


    private static final String DATABASE_CREATE_PRODUCTO_CARRITO = "create table if not exists " + ConstantsAdmin.TABLE_PRODUCTO_CARRITO +
            "(" + ConstantsAdmin.KEY_ROWID +" integer primary key autoincrement, "
            + ConstantsAdmin.KEY_TEXTO + " text, "
            + ConstantsAdmin.KEY_TITULO + " text, "
            + ConstantsAdmin.KEY_TITULO_TAMANIO + " text, "
            + ConstantsAdmin.KEY_TEXTO_TAMANIO + " text, "
            + ConstantsAdmin.KEY_TITULO_FUENTE + " text, "
            + ConstantsAdmin.KEY_TEXTO_FUENTE + " text, "
            + ConstantsAdmin.KEY_TITULO_COLOR + " integer, "
            + ConstantsAdmin.KEY_TEXTO_COLOR + " integer, "
            + ConstantsAdmin.KEY_BACKGROUND_FILENAME + " text, "
            + ConstantsAdmin.KEY_COMENTARIO_USR + " text, "
            + ConstantsAdmin.KEY_ID_CREATOR + " integer, "
            + ConstantsAdmin.KEY_TIENE_TITULO + " integer, "
            + ConstantsAdmin.KEY_ID_AREA_TITULO + " integer, "
            + ConstantsAdmin.KEY_WIDTH_TAG + " integer, "
            + ConstantsAdmin.KEY_HEIGHT_TAG + " integer, "
            + ConstantsAdmin.KEY_ROUND + " integer, "
            + ConstantsAdmin.KEY_WIDTH_AREA_TEXTO + " integer, "
            + ConstantsAdmin.KEY_HEIGHT_AREA_TEXTO + " integer, "
            + ConstantsAdmin.KEY_ES_MULTILINEA_TEXTO + " integer, "
            + ConstantsAdmin.KEY_FROM_X_TEXTO + " integer, "
            + ConstantsAdmin.KEY_FROM_Y_TEXTO + " integer, "
            + ConstantsAdmin.KEY_WIDTH_AREA_TITULO + " integer, "
            + ConstantsAdmin.KEY_HEIGHT_AREA_TITULO + " integer, "
            + ConstantsAdmin.KEY_ES_MULTILINEA_TITULO + " integer, "
            + ConstantsAdmin.KEY_FROM_X_TITULO + " integer, "
            + ConstantsAdmin.KEY_FROM_Y_TITULO + " integer, "
            + ConstantsAdmin.KEY_ID_FILLS_TEXTURED + " integer, "
            + ConstantsAdmin.KEY_ID_PRODUCTO + " integer, "
            + ConstantsAdmin.KEY_ID_FONT_TEXT + " integer, "
            + ConstantsAdmin.KEY_ID_FONT_TITLE + " integer, "
            + ConstantsAdmin.KEY_NOMBRE_PRODUCTO + " text, "
            + ConstantsAdmin.KEY_PRECIO_PRODUCTO + " text, "
            + ConstantsAdmin.KEY_CANTIDAD_PRODUCTO + " text, "
            + ConstantsAdmin.KEY_MODELO_PRODUCTO + " text, "
            + ConstantsAdmin.KEY_ID_AREA_TEXTO + " integer);";

	public DataBaseHelper(Context context) {
         super(context, ConstantsAdmin.DATABASE_NAME, null, ConstantsAdmin.DATABASE_VERSION);
    }

	 @Override
     public void onCreate(SQLiteDatabase db) {
         db.execSQL(DATABASE_CREATE_LOGIN);
         db.execSQL(DATABASE_CREATE_PRODUCTO_CARRITO);
     }

     public void deleteAll(SQLiteDatabase db) {
         onCreate(db);
     }
	 
     @Override
     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         Log.w(ConstantsAdmin.TAG, "Upgrading database from version " + oldVersion + " to "
                 + newVersion + ", which will destroy all old data");
         db.execSQL("DROP TABLE IF EXISTS " + ConstantsAdmin.TABLE_LOGIN);
         db.execSQL("DROP TABLE IF EXISTS " + ConstantsAdmin.TABLE_PRODUCTO_CARRITO);
         onCreate(db);
     }

   //  public static final String SIZE_ITEM = "select count(" + ConstantsAdmin.KEY_ROWID +") from " + ConstantsAdmin.TABLE_ITEM + "  where " + ConstantsAdmin.KEY_ROWID + " > 0";
    public static final String SIZE_LOGIN = "select count(" + ConstantsAdmin.KEY_ROWID +") from " + ConstantsAdmin.TABLE_LOGIN + "  where " + ConstantsAdmin.KEY_ROWID + " > 0";
    public static final String SIZE_PRODUCTO_CARRITO = "select count(" + ConstantsAdmin.KEY_ROWID +") from " + ConstantsAdmin.TABLE_PRODUCTO_CARRITO + "  where " + ConstantsAdmin.KEY_ROWID + " > 0";
   //  public static final String SIZE_DATABACKUP = "select count(" + ConstantsAdmin.KEY_ROWID +") from " + ConstantsAdmin.TABLE_GOTO_URL + "  where " + ConstantsAdmin.KEY_ROWID + " > 0";


    /*public void deleteDataBackUp(SQLiteDatabase mDb) {
        mDb.execSQL("DELETE FROM " + ConstantsAdmin.TABLE_GOTO_URL + " WHERE " + ConstantsAdmin.KEY_ROWID + " > -1");

    }*/
}
