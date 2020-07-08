package com.boxico.android.kn.funforlabelapp.ddbb;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.boxico.android.kn.funforlabelapp.dtos.Customer;
import com.boxico.android.kn.funforlabelapp.dtos.ProductoCarrito;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;



public class DataBaseManager {
   
    
	private DataBaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private Context mCtx;
	private boolean isOpened = false;

	private static final DataBaseManager instanciaUnica = new DataBaseManager();

	private DataBaseManager() {
	 	super();
	}

	public void createBD(){
		mDbHelper.onCreate(mDb);
	}

	public static DataBaseManager getInstance(Context ctx) {
	 	instanciaUnica.setmCtx(ctx);
	 	return instanciaUnica;
	}

	public void upgradeDB(){
		mDbHelper.onUpgrade(mDb, 1, 2);
	}

	private void setmCtx(Context mCtx) {
		this.mCtx = mCtx;
	}

    public void open() throws SQLException {
		if(!this.isOpened) {
			mDbHelper = new DataBaseHelper(mCtx);
			mDb = mDbHelper.getWritableDatabase();
			this.isOpened = true;
		}
	}

	public void deleteAll(){
		mDbHelper.deleteAll(mDb);
	}

	public void close() {
		if(this.isOpened) {
			mDbHelper.close();
			this.isOpened = false;
		}
    }



	public void createLogin(Customer item) {
		//long returnValue = item.getId();
		ContentValues initialValues = new ContentValues();
		initialValues.put(ConstantsAdmin.KEY_USER, item.getEmail());
		initialValues.put(ConstantsAdmin.KEY_PASSWORD, item.getPassword());
		initialValues.put(ConstantsAdmin.KEY_NOT_ENCRIPTED_PASSWORD, item.getNotEncriptedPassword());
		initialValues.put(ConstantsAdmin.KEY_ROWID, 1);
		if(sizeLogin()==0){
			mDb.insert(ConstantsAdmin.TABLE_LOGIN, null, initialValues);
		}else{
			mDb.update(ConstantsAdmin.TABLE_LOGIN, initialValues, ConstantsAdmin.KEY_ROWID + "= 1" , null);
		}

	}

	public void createProductoCarrito(ProductoCarrito item) {
		//long returnValue = item.getId();
		ContentValues initialValues = new ContentValues();
		initialValues.put(ConstantsAdmin.KEY_COMENTARIO_USR, item.getComentarioUsr());
		initialValues.put(ConstantsAdmin.KEY_TEXTO_COLOR, item.getFontTextColor());
		initialValues.put(ConstantsAdmin.KEY_TEXTO_FUENTE, item.getTextFontName());
		initialValues.put(ConstantsAdmin.KEY_TEXTO_TAMANIO, String.valueOf(item.getTextFontSize()));
		initialValues.put(ConstantsAdmin.KEY_TEXTO, item.getTexto());
		initialValues.put(ConstantsAdmin.KEY_ID_CREATOR, item.getCreador().getId());
		initialValues.put(ConstantsAdmin.KEY_ID_AREA_TEXTO, item.getAreaTexto().getTextAreasId());
		if(item.getAreaTitulo() != null) {
			initialValues.put(ConstantsAdmin.KEY_TITULO_FUENTE, item.getTitleFontName());
			initialValues.put(ConstantsAdmin.KEY_TITULO_COLOR, item.getFontTitleColor());
			initialValues.put(ConstantsAdmin.KEY_ID_AREA_TITULO, item.getAreaTitulo().getTextAreasId());
			initialValues.put(ConstantsAdmin.KEY_TITULO, item.getTitulo());
			initialValues.put(ConstantsAdmin.KEY_TITULO_TAMANIO, String.valueOf(item.getTitleFontSize()));
			initialValues.put(ConstantsAdmin.KEY_TIENE_TITULO, 1);
		}else{
			initialValues.put(ConstantsAdmin.KEY_TIENE_TITULO, 0);
		}
		initialValues.put(ConstantsAdmin.KEY_BACKGROUND_FILENAME, item.getBackgroundFilename());

		if(item.getId() == 0 ){
			long id= mDb.insert(ConstantsAdmin.TABLE_PRODUCTO_CARRITO, null, initialValues);
			item.setId((int) id);
		}else{
			mDb.update(ConstantsAdmin.TABLE_PRODUCTO_CARRITO, initialValues, ConstantsAdmin.KEY_ROWID + "=" + item.getId() , null);
		}


	}

	public void deleteLogin(){
		mDb.delete(ConstantsAdmin.TABLE_LOGIN, ConstantsAdmin.KEY_ROWID + "= 1", null);
	}

	public void deleteProductoCarrito(ProductoCarrito pc){
		mDb.delete(ConstantsAdmin.TABLE_PRODUCTO_CARRITO, ConstantsAdmin.KEY_ROWID + "=" + pc.getId(), null);
	}


	private long sizeLogin(){
		long result;
		SQLiteStatement s = mDb.compileStatement(DataBaseHelper.SIZE_LOGIN);
		result = s.simpleQueryForLong();
		return result;
	}



	public Cursor cursorLogin() {
		Cursor c = null;
		if(mDb.isOpen()){
			c = mDb.query(ConstantsAdmin.TABLE_LOGIN, null, null, null, null, null, null, null );
		}
		return c;
	}

	public Cursor cursorProductoCarrito() {
		Cursor c = null;
		if(mDb.isOpen()){
			c = mDb.query(ConstantsAdmin.TABLE_PRODUCTO_CARRITO, null, null, null, null, null, null, null );
		}
		return c;
	}





}
