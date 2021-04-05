package com.example.project_rdv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase database;
    // Table Name
    public static final String TABLE_NAME = "RDVs";
    // Table columns
    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String DATE= "date";
    public static final String DESCRIPTION="description";
    public static final String TIME="time";
    public static final String ADDRESS="address";
    public static final String PHONE="phone";
    public static final String STATE="state";
    // Database Information
    static final String DB_NAME = "RDV.DB";
    // database version
    static final int DB_VERSION = 1;
    // Creating table query
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE + " TEXT NOT NULL, "+ DATE +
            " TEXT, " + TIME + " TEXT, "+ ADDRESS +" TEXT, " + PHONE + " TEXT, " + STATE + " BOOLEAN, " + DESCRIPTION + " CHAR(250));";

    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void open() throws SQLException {
        database = this.getWritableDatabase();
    }
    public void close() {
        database.close();
    }
    public void add(RDV rdv){
        ContentValues contentValues= new ContentValues();
        contentValues.put(TITLE,rdv.getTitle());
        contentValues.put(DATE,rdv.getDate());
        contentValues.put(DESCRIPTION,rdv.getDescription());
        contentValues.put(ADDRESS,rdv.getAddress());
        contentValues.put(TIME,rdv.getTime());
        contentValues.put(PHONE,rdv.getPhone());
        contentValues.put(STATE,rdv.getState());
        database.insert(TABLE_NAME,null,contentValues);
    }
    public Cursor getOneRDV(long id){
        String[] projection = {_ID,TITLE,DATE,DESCRIPTION,TIME,ADDRESS,PHONE,STATE};
        Cursor cursor = database.query(TABLE_NAME,projection,_ID +" = "+ id,null,null,null,null,null);
        return cursor;
    }

    public Cursor getAllRDV(){
        String[] projection = {_ID,TITLE,DATE,DESCRIPTION,TIME,ADDRESS,PHONE,STATE};
        Cursor cursor = database.query(TABLE_NAME,projection,null,null,null,null,null,null);
        return cursor;
    }
    public int update(RDV rdv) {
        Long _id= rdv.getId();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE,rdv.getTitle());
        contentValues.put(DATE,rdv.getDate());
        contentValues.put(DESCRIPTION,rdv.getDescription());
        contentValues.put(ADDRESS,rdv.getAddress());
        contentValues.put(TIME,rdv.getTime());
        contentValues.put(PHONE,rdv.getPhone());
        contentValues.put(STATE,rdv.getState());
        int count = database.update(TABLE_NAME, contentValues, this._ID + " = " + _id, null);
        return count;
    }

    public void delete(long _id)
    {
        database.delete(TABLE_NAME, _ID + "=" + _id, null);

    }
}
