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

	public DataBaseHelper(Context context) {
         super(context, ConstantsAdmin.DATABASE_NAME, null, ConstantsAdmin.DATABASE_VERSION);
    }

	 @Override
     public void onCreate(SQLiteDatabase db) {
         db.execSQL(DATABASE_CREATE_LOGIN);
     }

     public void deleteAll(SQLiteDatabase db) {
         onCreate(db);
     }
	 
     @Override
     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         Log.w(ConstantsAdmin.TAG, "Upgrading database from version " + oldVersion + " to "
                 + newVersion + ", which will destroy all old data");
         db.execSQL("DROP TABLE IF EXISTS " + ConstantsAdmin.TABLE_LOGIN);
         onCreate(db);
     }

   //  public static final String SIZE_ITEM = "select count(" + ConstantsAdmin.KEY_ROWID +") from " + ConstantsAdmin.TABLE_ITEM + "  where " + ConstantsAdmin.KEY_ROWID + " > 0";
    public static final String SIZE_LOGIN = "select count(" + ConstantsAdmin.KEY_ROWID +") from " + ConstantsAdmin.TABLE_LOGIN + "  where " + ConstantsAdmin.KEY_ROWID + " > 0";
   //  public static final String SIZE_DATABACKUP = "select count(" + ConstantsAdmin.KEY_ROWID +") from " + ConstantsAdmin.TABLE_GOTO_URL + "  where " + ConstantsAdmin.KEY_ROWID + " > 0";


    /*public void deleteDataBackUp(SQLiteDatabase mDb) {
        mDb.execSQL("DELETE FROM " + ConstantsAdmin.TABLE_GOTO_URL + " WHERE " + ConstantsAdmin.KEY_ROWID + " > -1");

    }*/
}
