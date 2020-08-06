package com.boxico.android.kn.funforlabelapp.ddbb;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.boxico.android.kn.funforlabelapp.dtos.Customer;
import com.boxico.android.kn.funforlabelapp.dtos.ItemCarrito;
import com.boxico.android.kn.funforlabelapp.dtos.ProductoCarrito;
import com.boxico.android.kn.funforlabelapp.dtos.ComboCarrito;
import com.boxico.android.kn.funforlabelapp.utils.ConstantsAdmin;

import java.util.Iterator;


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
		if(item.getId() == -1 || item.getId()==0) {
			initialValues.put(ConstantsAdmin.KEY_ID_COMBO, item.getIdCombo());
			initialValues.put(ConstantsAdmin.KEY_COMENTARIO_USR, item.getComentarioUsr());
			initialValues.put(ConstantsAdmin.KEY_TEXTO_COLOR, item.getFontTextColor());
			initialValues.put(ConstantsAdmin.KEY_TEXTO_FUENTE, item.getTextFontName());
			initialValues.put(ConstantsAdmin.KEY_TEXTO_TAMANIO, String.valueOf(item.getTextFontSize()));
			initialValues.put(ConstantsAdmin.KEY_TEXTO, item.getTexto());
			initialValues.put(ConstantsAdmin.KEY_ID_CREATOR, item.getCreador().getId());
			initialValues.put(ConstantsAdmin.KEY_ID_AREA_TEXTO, item.getAreaTexto().getTextAreasId());
			initialValues.put(ConstantsAdmin.KEY_WIDTH_TAG, item.getAnchoTag());
			initialValues.put(ConstantsAdmin.KEY_HEIGHT_TAG, item.getLargoTag());
			initialValues.put(ConstantsAdmin.KEY_ROUND, item.getRound());
			initialValues.put(ConstantsAdmin.KEY_ES_MULTILINEA_TEXTO, item.getEsMultilineaTexto());
			initialValues.put(ConstantsAdmin.KEY_FROM_X_TEXTO, item.getFromXTexto());
			initialValues.put(ConstantsAdmin.KEY_FROM_Y_TEXTO, item.getFromYTexto());
			initialValues.put(ConstantsAdmin.KEY_WIDTH_AREA_TEXTO, item.getAnchoAreaTexto());
			initialValues.put(ConstantsAdmin.KEY_HEIGHT_AREA_TEXTO, item.getLargoAreaTexto());
			initialValues.put(ConstantsAdmin.KEY_NOMBRE_PRODUCTO, item.getNombre());
			initialValues.put(ConstantsAdmin.KEY_CANTIDAD_PRODUCTO, item.getCantidad());
			initialValues.put(ConstantsAdmin.KEY_CANTIDAD_PRODUCTO_PORPACK, item.getCantidadPorPack());
			initialValues.put(ConstantsAdmin.KEY_PRECIO_PRODUCTO, item.getPrecio());
			initialValues.put(ConstantsAdmin.KEY_MODELO_PRODUCTO, item.getModelo());
			initialValues.put(ConstantsAdmin.KEY_ID_PRODUCTO, item.getIdProduct());
			initialValues.put(ConstantsAdmin.KEY_ID_FONT_TEXT, item.getFontTextId());
			initialValues.put(ConstantsAdmin.KEY_ID_FILLS_TEXTURED, item.getFillsTexturedId());
			if (item.getAreaTitulo() != null) {
				initialValues.put(ConstantsAdmin.KEY_ID_FONT_TITLE, item.getFontTitleId());
				initialValues.put(ConstantsAdmin.KEY_ES_MULTILINEA_TITULO, item.getEsMultilineaTituto());
				initialValues.put(ConstantsAdmin.KEY_TITULO_FUENTE, item.getTitleFontName());
				initialValues.put(ConstantsAdmin.KEY_TITULO_COLOR, item.getFontTitleColor());
				initialValues.put(ConstantsAdmin.KEY_ID_AREA_TITULO, item.getAreaTitulo().getTextAreasId());
				initialValues.put(ConstantsAdmin.KEY_TITULO, item.getTitulo());
				initialValues.put(ConstantsAdmin.KEY_TITULO_TAMANIO, String.valueOf(item.getTitleFontSize()));
				initialValues.put(ConstantsAdmin.KEY_TIENE_TITULO, 1);
				initialValues.put(ConstantsAdmin.KEY_FROM_X_TITULO, item.getFromXTituto());
				initialValues.put(ConstantsAdmin.KEY_FROM_Y_TITULO, item.getFromYTituto());
				initialValues.put(ConstantsAdmin.KEY_WIDTH_AREA_TITULO, item.getAnchoAreaTituto());
				initialValues.put(ConstantsAdmin.KEY_HEIGHT_AREA_TITULO, item.getLargoAreaTituto());

			} else {
				initialValues.put(ConstantsAdmin.KEY_TIENE_TITULO, 0);
			}
			initialValues.put(ConstantsAdmin.KEY_BACKGROUND_FILENAME, item.getBackgroundFilename());
		}else{// ES UNA ACTUALIZACION DE CANTIDAD
			initialValues.put(ConstantsAdmin.KEY_CANTIDAD_PRODUCTO, item.getCantidad());
		}

		if(item.getId() == 0 ){
			long id= mDb.insert(ConstantsAdmin.TABLE_PRODUCTO_CARRITO, null, initialValues);
			item.setId((int) id);
		}else{
			mDb.update(ConstantsAdmin.TABLE_PRODUCTO_CARRITO, initialValues, ConstantsAdmin.KEY_ROWID + "=" + item.getId() , null);
		}


	}

	public void createComboCarrito(ComboCarrito item) {
		//long returnValue = item.getId();
		ContentValues initialValues = new ContentValues();
		if(item.getId() == -1 || item.getId()==0) {
			initialValues.put(ConstantsAdmin.KEY_COMENTARIO_USR, item.getComentarioUsr());
			initialValues.put(ConstantsAdmin.KEY_NOMBRE_PRODUCTO, item.getNombre());
			initialValues.put(ConstantsAdmin.KEY_CANTIDAD_PRODUCTO, item.getCantidad());
			initialValues.put(ConstantsAdmin.KEY_CANTIDAD_PRODUCTO_PORPACK, item.getCantidadPorPack());
			initialValues.put(ConstantsAdmin.KEY_PRECIO_PRODUCTO, item.getPrecio());
			initialValues.put(ConstantsAdmin.KEY_MODELO_PRODUCTO, item.getModelo());
			initialValues.put(ConstantsAdmin.KEY_ID_PRODUCTO, item.getIdProduct());
			initialValues.put(ConstantsAdmin.KEY_ID_FILLS_TEXTURED, item.getFillsTexturedId());
			initialValues.put(ConstantsAdmin.KEY_BACKGROUND_FILENAME, item.getBackgroundFilename());
		}else{// ES UNA ACTUALIZACION DE CANTIDAD
			initialValues.put(ConstantsAdmin.KEY_CANTIDAD_PRODUCTO, item.getCantidad());
		}
		long id = -1;
		if(item.getId() == 0 ){//ES UN COMBO NUEVO
			id= mDb.insert(ConstantsAdmin.TABLE_COMBO_CARRITO, null, initialValues);
			item.setId((int) id);
			Iterator<ItemCarrito> it = item.getProductos().iterator();
			ProductoCarrito pc;
			while (it.hasNext()){
				initialValues = new ContentValues();
				pc = (ProductoCarrito) it.next();
				pc.setIdCombo((int) id);
				initialValues.put(ConstantsAdmin.KEY_ID_COMBO, pc.getIdCombo());
				initialValues.put(ConstantsAdmin.KEY_COMENTARIO_USR, pc.getComentarioUsr());
				initialValues.put(ConstantsAdmin.KEY_TEXTO_COLOR, pc.getFontTextColor());
				initialValues.put(ConstantsAdmin.KEY_TEXTO_FUENTE, pc.getTextFontName());
				initialValues.put(ConstantsAdmin.KEY_TEXTO_TAMANIO, String.valueOf(pc.getTextFontSize()));
				initialValues.put(ConstantsAdmin.KEY_TEXTO, pc.getTexto());
				initialValues.put(ConstantsAdmin.KEY_ID_CREATOR, pc.getCreador().getId());
				initialValues.put(ConstantsAdmin.KEY_ID_AREA_TEXTO, pc.getAreaTexto().getTextAreasId());
				initialValues.put(ConstantsAdmin.KEY_WIDTH_TAG, pc.getAnchoTag());
				initialValues.put(ConstantsAdmin.KEY_HEIGHT_TAG, pc.getLargoTag());
				initialValues.put(ConstantsAdmin.KEY_ROUND, pc.getRound());
				initialValues.put(ConstantsAdmin.KEY_ES_MULTILINEA_TEXTO, pc.getEsMultilineaTexto());
				initialValues.put(ConstantsAdmin.KEY_FROM_X_TEXTO, pc.getFromXTexto());
				initialValues.put(ConstantsAdmin.KEY_FROM_Y_TEXTO, pc.getFromYTexto());
				initialValues.put(ConstantsAdmin.KEY_WIDTH_AREA_TEXTO, pc.getAnchoAreaTexto());
				initialValues.put(ConstantsAdmin.KEY_HEIGHT_AREA_TEXTO, pc.getLargoAreaTexto());
				initialValues.put(ConstantsAdmin.KEY_NOMBRE_PRODUCTO, pc.getNombre());
				initialValues.put(ConstantsAdmin.KEY_CANTIDAD_PRODUCTO, pc.getCantidad());
				initialValues.put(ConstantsAdmin.KEY_CANTIDAD_PRODUCTO_PORPACK, pc.getCantidadPorPack());
				initialValues.put(ConstantsAdmin.KEY_PRECIO_PRODUCTO, pc.getPrecio());
				initialValues.put(ConstantsAdmin.KEY_MODELO_PRODUCTO, pc.getModelo());
				initialValues.put(ConstantsAdmin.KEY_ID_PRODUCTO, pc.getIdProduct());
				initialValues.put(ConstantsAdmin.KEY_ID_FONT_TEXT, pc.getFontTextId());
				initialValues.put(ConstantsAdmin.KEY_ID_FILLS_TEXTURED, pc.getFillsTexturedId());
				if (pc.getAreaTitulo() != null) {
					initialValues.put(ConstantsAdmin.KEY_ID_FONT_TITLE, pc.getFontTitleId());
					initialValues.put(ConstantsAdmin.KEY_ES_MULTILINEA_TITULO, pc.getEsMultilineaTituto());
					initialValues.put(ConstantsAdmin.KEY_TITULO_FUENTE, pc.getTitleFontName());
					initialValues.put(ConstantsAdmin.KEY_TITULO_COLOR, pc.getFontTitleColor());
					initialValues.put(ConstantsAdmin.KEY_ID_AREA_TITULO, pc.getAreaTitulo().getTextAreasId());
					initialValues.put(ConstantsAdmin.KEY_TITULO, pc.getTitulo());
					initialValues.put(ConstantsAdmin.KEY_TITULO_TAMANIO, String.valueOf(pc.getTitleFontSize()));
					initialValues.put(ConstantsAdmin.KEY_TIENE_TITULO, 1);
					initialValues.put(ConstantsAdmin.KEY_FROM_X_TITULO, pc.getFromXTituto());
					initialValues.put(ConstantsAdmin.KEY_FROM_Y_TITULO, pc.getFromYTituto());
					initialValues.put(ConstantsAdmin.KEY_WIDTH_AREA_TITULO, pc.getAnchoAreaTituto());
					initialValues.put(ConstantsAdmin.KEY_HEIGHT_AREA_TITULO, pc.getLargoAreaTituto());

				} else {
					initialValues.put(ConstantsAdmin.KEY_TIENE_TITULO, 0);
				}
				initialValues.put(ConstantsAdmin.KEY_BACKGROUND_FILENAME, pc.getBackgroundFilename());
				if(pc.getId() == 0 ){
					long idPC= mDb.insert(ConstantsAdmin.TABLE_PRODUCTO_CARRITO, null, initialValues);
					pc.setId((int) idPC);
				}else{
					mDb.update(ConstantsAdmin.TABLE_PRODUCTO_CARRITO, initialValues, ConstantsAdmin.KEY_ROWID + "=" + pc.getId() , null);
				}

			}
		}
		else{
			mDb.update(ConstantsAdmin.TABLE_COMBO_CARRITO, initialValues, ConstantsAdmin.KEY_ROWID + "=" + item.getId() , null);
		}



	}

	public void deleteLogin(){
		mDb.delete(ConstantsAdmin.TABLE_LOGIN, ConstantsAdmin.KEY_ROWID + "= 1", null);
	}

	public void deleteProductoCarrito(ProductoCarrito pc){
		mDb.delete(ConstantsAdmin.TABLE_PRODUCTO_CARRITO, ConstantsAdmin.KEY_ROWID + "=" + pc.getId(), null);
	}

	public void deleteComboProductoCarrito(ComboCarrito c){
		Iterator<ItemCarrito> it = c.getProductos().iterator();
		ProductoCarrito pc = null;
		while (it.hasNext()){
			pc = (ProductoCarrito)it.next();
			mDb.delete(ConstantsAdmin.TABLE_PRODUCTO_CARRITO, ConstantsAdmin.KEY_ROWID + "=" + pc.getId(), null);
		}
		mDb.delete(ConstantsAdmin.TABLE_COMBO_CARRITO, ConstantsAdmin.KEY_ROWID + "=" + c.getId(), null);
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

	public Cursor cursorProductoCarrito(int idCombo) {
		Cursor c = null;
		String selection = "idCombo = " + String.valueOf(idCombo);
		if(mDb.isOpen()){
			c = mDb.query(ConstantsAdmin.TABLE_PRODUCTO_CARRITO, null, selection, null, null, null, null, null );
		}
		return c;
	}

	public Cursor cursorComboProductoCarrito() {
		Cursor c = null;
		if(mDb.isOpen()){
				c = mDb.query(ConstantsAdmin.TABLE_COMBO_CARRITO, null, null, null, null, null, null, null );
		}
		return c;
	}






}
