package pt.jpa.groupgenerator.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by sebasi on 26/06/2017.
 */

public class IrmaosDAO {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public IrmaosDAO(Context context) {dbHelper = new DatabaseHelper(context);}

    public void open() {database = dbHelper.getWritableDatabase();}

    public void close() {dbHelper.close();}

    public Cursor getAllIrmaos() {
        String sqlStatment = "SELECT * FROM " + DatabaseContract.Irmaos.TABLE_NAME;
        return database.rawQuery(sqlStatment, null);
    }

    public void insertIrmao(String irmaoName, String irmaoEmail, String irmaoPhone) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.Irmaos.COL_NAME, irmaoName);
        contentValues.put(DatabaseContract.Irmaos.COL_EMAIL, irmaoEmail);
        contentValues.put(DatabaseContract.Irmaos.COL_PHONE, irmaoPhone);
        database.insert(DatabaseContract.Irmaos.TABLE_NAME, null, contentValues);
    }

}
