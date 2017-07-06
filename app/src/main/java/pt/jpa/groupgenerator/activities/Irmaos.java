package pt.jpa.groupgenerator.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toolbar;

import pt.jpa.groupgenerator.R;
import pt.jpa.groupgenerator.model.DatabaseContract;
import pt.jpa.groupgenerator.model.DatabaseProvider;
import pt.jpa.groupgenerator.model.IrmaoCursorAdapter;

public class Irmaos extends BaseActivity implements LoaderCallbacks<Cursor> {

    private IrmaoCursorAdapter irmaoCursorAdapter;
    private ListView listView;
    private static final int ADD_IRMAO_REQUEST_CODE = 1;
    private static final int UPDATE_IRMAO_REQUEST_CODE = 2;
    private static final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irmaos);

        listView = (ListView) findViewById(R.id.list_irmaos);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                irmaoCursorAdapter.chosenOne = position;
                irmaoCursorAdapter.notifyDataSetChanged();
            }
        });

        setToolbar(R.string.toolbar_irmaos);
        setActionBar(getToolbar());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        return new CursorLoader(this, DatabaseProvider.CONTENT_URI,
                new String[] {"_id", DatabaseContract.Irmaos.COL_NAME,
                        DatabaseContract.Irmaos.COL_EMAIL,
                        DatabaseContract.Irmaos.COL_PHONE}, null, null, null) {
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToFirst();
        irmaoCursorAdapter = new IrmaoCursorAdapter(this, cursor);
        listView.setAdapter(irmaoCursorAdapter);
    }

    @Override
    public void onLoaderReset(Loader arg0) {
    }

    public void onUpdateIrmao(long id) {
        Intent intent = new Intent(this, AddIrmao.class);
        String[] projection = {DatabaseContract.Irmaos.COL_NAME, DatabaseContract.Irmaos.COL_PHONE,
                DatabaseContract.Irmaos.COL_EMAIL, DatabaseContract.Irmaos.COL_SPOUSE};
        String[] args = { String.valueOf(id) };
        Cursor c = getContentResolver().query(DatabaseProvider.CONTENT_URI, projection, "_id=?", args, null);
        c.moveToFirst();
        intent.putExtra("irmao_name", c.getString(c.getColumnIndex(DatabaseContract.Irmaos.COL_NAME)));
        intent.putExtra("irmao_phone", c.getString(c.getColumnIndex(DatabaseContract.Irmaos.COL_PHONE)));
        intent.putExtra("irmao_email", c.getString(c.getColumnIndex(DatabaseContract.Irmaos.COL_EMAIL)));
        if (!c.isNull(c.getColumnIndex(DatabaseContract.Irmaos.COL_SPOUSE))) {
            intent.putExtra("irmao_spouse", c.getLong(c.getColumnIndex(DatabaseContract.Irmaos.COL_SPOUSE)));
        }
        c.close();
        intent.putExtra("mode", "EDIT");
        intent.putExtra("irmao_id", id);
        startActivityForResult(intent, UPDATE_IRMAO_REQUEST_CODE);
    }

    public void onDeleteIrmao(long id) {
        //DialogFragment newFragment = AlertBuilder.newInstance(android.R.string.dialog_alert_title, id);
        //newFragment.show(getFragmentManager(), "dialog");
        final long thisId = id;
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Apagar irmão");
        adb.setMessage("Tens a certeza que queres apagar este irmão?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String[] projection = {DatabaseContract.Irmaos.COL_SPOUSE};
                        String[] args = { String.valueOf(thisId) };
                        Cursor c = getContentResolver().query(DatabaseProvider.CONTENT_URI, projection, "_id=?", args, null);
                        c.moveToFirst();
                        long oldSpouseId = -1;
                        if (!c.isNull(c.getColumnIndex(DatabaseContract.Irmaos.COL_SPOUSE))) {
                            oldSpouseId = c.getLong(c.getColumnIndex(DatabaseContract.Irmaos.COL_SPOUSE));
                        }
                        c.close();
                        if (oldSpouseId != -1) {
                            ContentValues values = new ContentValues();
                            values.putNull(DatabaseContract.Irmaos.COL_SPOUSE);
                            args = new String[]{String.valueOf(oldSpouseId)};
                            getContentResolver().update(DatabaseProvider.CONTENT_URI, values, "_id=?", args);
                        }

                        args = new String[]{ String.valueOf(thisId) };
                        getContentResolver().delete(DatabaseProvider.CONTENT_URI, "_id=?", args);
                        getLoaderManager().restartLoader(LOADER_ID, null, Irmaos.this);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        adb.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_IRMAO_REQUEST_CODE && resultCode == RESULT_OK) {

            ContentValues values = new ContentValues();
            values.put(DatabaseContract.Irmaos.COL_NAME, data.getExtras().getString("tag_irmao_name"));
            values.put(DatabaseContract.Irmaos.COL_EMAIL, data.getExtras().getString("tag_irmao_email"));
            values.put(DatabaseContract.Irmaos.COL_PHONE, data.getExtras().getString("tag_irmao_phone"));
            long[] spouse = data.getExtras().getLongArray("tag_irmao_spouse");
            if (spouse[0] == 1) {
                values.put(DatabaseContract.Irmaos.COL_SPOUSE, spouse[1]);
            }
            getContentResolver().insert(DatabaseProvider.CONTENT_URI, values);

            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
        else if (requestCode == UPDATE_IRMAO_REQUEST_CODE && resultCode == RESULT_OK) {

            long id = data.getExtras().getLong("tag_irmao_id");
            String[] projection = {DatabaseContract.Irmaos.COL_SPOUSE};
            String[] args = { String.valueOf(id) };
            Cursor c = getContentResolver().query(DatabaseProvider.CONTENT_URI, projection, "_id=?", args, null);
            c.moveToFirst();
            long oldSpouseId = -1;
            if (!c.isNull(c.getColumnIndex(DatabaseContract.Irmaos.COL_SPOUSE))) {
                oldSpouseId = c.getLong(c.getColumnIndex(DatabaseContract.Irmaos.COL_SPOUSE));
            }
            c.close();

            ContentValues values = new ContentValues();
            values.put(DatabaseContract.Irmaos.COL_NAME, data.getExtras().getString("tag_irmao_name"));
            values.put(DatabaseContract.Irmaos.COL_EMAIL, data.getExtras().getString("tag_irmao_email"));
            values.put(DatabaseContract.Irmaos.COL_PHONE, data.getExtras().getString("tag_irmao_phone"));
            long[] spouse = data.getExtras().getLongArray("tag_irmao_spouse");
            if (spouse[0] == 1) {
                values.put(DatabaseContract.Irmaos.COL_SPOUSE, spouse[1]);
            }
            else {
                values.putNull(DatabaseContract.Irmaos.COL_SPOUSE);
            }

            getContentResolver().update(DatabaseProvider.CONTENT_URI, values, "_id=?", args);

            //update oldSpouse's spouse
            if (oldSpouseId != -1) {

                projection = new String[]{DatabaseContract.Irmaos.COL_SPOUSE};
                args = new String[]{ String.valueOf(oldSpouseId) };
                c = getContentResolver().query(DatabaseProvider.CONTENT_URI, projection, "_id=?", args, null);
                c.moveToFirst();
                if (!c.isNull(c.getColumnIndex(DatabaseContract.Irmaos.COL_SPOUSE))) {
                    values = new ContentValues();
                    values.putNull(DatabaseContract.Irmaos.COL_SPOUSE);
                    args = new String[]{ String.valueOf(c.getLong(c.getColumnIndex(DatabaseContract.Irmaos.COL_SPOUSE))) };
                    getContentResolver().update(DatabaseProvider.CONTENT_URI, values, "_id=?", args);
                }

                values = new ContentValues();
                values.put(DatabaseContract.Irmaos.COL_SPOUSE, id);
                args = new String[]{ String.valueOf(oldSpouseId) };
                getContentResolver().update(DatabaseProvider.CONTENT_URI, values, "_id=?", args);
            }
            else if (spouse[0] == 1){

                projection = new String[]{DatabaseContract.Irmaos.COL_SPOUSE};
                args = new String[]{ String.valueOf(spouse[1]) };
                c = getContentResolver().query(DatabaseProvider.CONTENT_URI, projection, "_id=?", args, null);
                c.moveToFirst();
                if (!c.isNull(c.getColumnIndex(DatabaseContract.Irmaos.COL_SPOUSE))) {
                    values = new ContentValues();
                    values.putNull(DatabaseContract.Irmaos.COL_SPOUSE);
                    args = new String[]{ String.valueOf(c.getLong(c.getColumnIndex(DatabaseContract.Irmaos.COL_SPOUSE))) };
                    getContentResolver().update(DatabaseProvider.CONTENT_URI, values, "_id=?", args);
                }

                values = new ContentValues();
                values.put(DatabaseContract.Irmaos.COL_SPOUSE, id);
                args = new String[]{ String.valueOf(spouse[1]) };
                getContentResolver().update(DatabaseProvider.CONTENT_URI, values, "_id=?", args);
            }

            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.irmaos_menu, menu);;
        menu.findItem(R.id.menu_add_irmao).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_irmao:
                Intent intent = new Intent(this, AddIrmao.class);
                intent.putExtra("mode", "ADD");
                startActivityForResult(intent, ADD_IRMAO_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
