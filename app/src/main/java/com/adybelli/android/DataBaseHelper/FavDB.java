package com.adybelli.android.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class FavDB extends SQLiteOpenHelper {
    private static final String DBNAME="fav";
    private static final String TBNAME="fav";
    public FavDB(@Nullable Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         db.execSQL("CREATE TABLE "+TBNAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,favid TEXT,prod_id TEXT,sizeId TEXT,size TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TBNAME);
        onCreate(db);
    }

    public Cursor getAll(String favid,String prod_id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TBNAME+" WHERE favid='"+favid+"' AND prod_id='"+prod_id+"' ORDER BY ID DESC",null);
        return cursor;
    }

    public Cursor customSelect(String  sql){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(sql,null);
        return cursor;
    }

    public boolean insert(String favid,String prod_id,String sizeId,String size){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("favid",favid);
        contentValues.put("prod_id",prod_id);
        contentValues.put("sizeId",sizeId);
        contentValues.put("size",size);
        long result=db.insert(TBNAME,null,contentValues);
        if(result==-1){
            return false;
        } else {
            return true;
        }
    }

    public boolean updateData(String favid,String prod_id,String sizeId,String size){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("favid",favid);
        contentValues.put("prod_id",prod_id);
        contentValues.put("sizeId",sizeId);
        contentValues.put("size",size);
        db.update(TBNAME,contentValues,"prod_id=?",new String[]{prod_id});
        return true;
    }

    public Cursor getLastInsertId(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT ID FROM "+TBNAME+" ORDER BY ID DESC LIMIT 1", null);
        return cursor;
    }



    public Integer deleteData(String prod_id){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TBNAME,"prod_id=?",new String[]{prod_id});

    }

    public void truncate(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TBNAME);
        onCreate(db);
    }
}
