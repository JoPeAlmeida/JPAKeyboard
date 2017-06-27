package pt.jpa.groupgenerator.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import pt.jpa.groupgenerator.R;
import pt.jpa.groupgenerator.model.DatabaseContract;
import pt.jpa.groupgenerator.model.DatabaseProvider;
import pt.jpa.groupgenerator.model.IrmaoCursorAdapter;
import pt.jpa.groupgenerator.model.IrmaosDAO;

public class Irmaos extends Activity implements LoaderCallbacks<Cursor> {

    private IrmaoCursorAdapter irmaoCursorAdapter;
    private ListView listView;
    private static final int ENTER_DATA_REQUEST_CODE = 1;
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
        Button addIrmao = (Button) findViewById(R.id.addIrmaoBtn);
        addIrmao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddIrmao(v);
            }
        });
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        return new CursorLoader(this, DatabaseProvider.CONTENT_URI,
                new String[] {DatabaseContract.Irmaos.COL_NAME,
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

    public void onAddIrmao(View view) {
        startActivityForResult(new Intent(this, AddIrmao.class), ENTER_DATA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ENTER_DATA_REQUEST_CODE && resultCode == RESULT_OK) {

            ContentValues values = new ContentValues();
            values.put(DatabaseContract.Irmaos.COL_NAME, data.getExtras().getString("tag_irmao_name"));
            values.put(DatabaseContract.Irmaos.COL_EMAIL, data.getExtras().getString("tag_irmao_email"));
            values.put(DatabaseContract.Irmaos.COL_PHONE, data.getExtras().getString("tag_irmao_phone"));
            getContentResolver().insert(DatabaseProvider.CONTENT_URI, values);

            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }
}
