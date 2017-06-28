package pt.jpa.groupgenerator.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

/**
 * Created by sebasi on 26/06/2017.
 */

class DatabaseHelper extends SQLiteOpenHelper {

    private final Context fContext;

    DatabaseHelper(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
        fContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.Irmaos.CREATE_TABLE);
        db.execSQL(DatabaseContract.Score.CREATE_TABLE);
        db.execSQL(DatabaseContract.Historico.CREATE_TABLE);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseContract.Irmaos.DELETE_TABLE);
        db.execSQL(DatabaseContract.Score.DELETE_TABLE);
        db.execSQL(DatabaseContract.Historico.DELETE_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseContract.Irmaos.DELETE_TABLE);
        db.execSQL(DatabaseContract.Score.DELETE_TABLE);
        db.execSQL(DatabaseContract.Historico.DELETE_TABLE);
        onCreate(db);
    }
}
