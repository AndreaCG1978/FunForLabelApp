package com.boxico.android.kn.funforlabelapp.ddbb;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.boxico.android.kn.funforlabelapp.dtos.Customer;
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



	public void deleteLogin(){
		mDb.delete(ConstantsAdmin.TABLE_LOGIN, ConstantsAdmin.KEY_ROWID + "= 1", null);
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





}
