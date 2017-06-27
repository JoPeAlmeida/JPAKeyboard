package pt.jpa.groupgenerator.model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by sebasi on 27/06/2017.
 */

public class DatabaseProvider extends ContentProvider {

    static final String PROVIDER_NAME = "pt.jpa.groupgenerator.model.DatabaseProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/" + "Irmaos";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    private static HashMap<String, String> IRMAOS_PROJECTION_MAP;

    static final int IRMAOS = 1;
    static final int IRMAO_ID = 2;

    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "Irmaos", IRMAOS);
        uriMatcher.addURI(PROVIDER_NAME, "Irmaos/#", IRMAO_ID);
    }

    DatabaseHelper dbHelper;
    SQLiteDatabase db;

    @Override
    public boolean onCreate(){
        Context context = getContext();
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return db != null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(DatabaseContract.Irmaos.TABLE_NAME, "", values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DatabaseContract.Irmaos.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case IRMAOS:
                qb.setProjectionMap(IRMAOS_PROJECTION_MAP);
                break;
            case IRMAO_ID:
                qb.appendWhere("_ID = " + uri.getPathSegments().get(1));
                break;
            default:
        }
        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = DatabaseContract.Irmaos.COL_NAME;
        }

        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case IRMAOS:
                count = db.delete(DatabaseContract.Irmaos.TABLE_NAME, selection, selectionArgs);
                break;
            case IRMAO_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(DatabaseContract.Irmaos.TABLE_NAME, "_ID = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")": ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case IRMAOS:
                count = db.update(DatabaseContract.Irmaos.TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            case IRMAO_ID:
                count = db.update(DatabaseContract.Irmaos.TABLE_NAME,
                        values, "_ID = " + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ? " AND (" +
                                        selection + ")": ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case IRMAOS:
                return "vnd.android.cursor.dir/vnd.pt.jpa.groupgenerator.model.DatabaseProvider.Irmaos";
            case IRMAO_ID:
                return "vnd.android.cursor.item/vnd.pt.jpa.groupgenerator.model.DatabaseProvider.Irmaos";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
