package pt.jpa.groupgenerator.activities;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.app.Activity;
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
import pt.jpa.groupgenerator.model.HistoricoCursorAdapter;

public class Actividades extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private HistoricoCursorAdapter histAdapter;
    private ListView listHist;
    private static final int ENTER_DATA_REQUEST_CODE = 1;
    private static final int LOADER_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades);


        listHist = (ListView) findViewById(R.id.list_historico);
        listHist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                histAdapter.chosenOne = position;
                histAdapter.notifyDataSetChanged();
            }
        });


        setToolbar(R.string.toolbar_actividades);
        setActionBar(getToolbar());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        return new CursorLoader(this, DatabaseProvider.HIST_URI,
                new String[] {"_id", DatabaseContract.Historico.COL_NAME,
                        DatabaseContract.Historico.COL_TYPE,
                        DatabaseContract.Historico.COL_DATE}, null, null, null) {
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToFirst();
        histAdapter = new HistoricoCursorAdapter(this, cursor);
        listHist.setAdapter(histAdapter);
    }

    @Override
    public void onLoaderReset(Loader arg0) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ENTER_DATA_REQUEST_CODE && resultCode == RESULT_OK) {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.Historico.COL_NAME, data.getExtras().getString("tag_hist_name"));
            values.put(DatabaseContract.Historico.COL_TYPE, data.getExtras().getString("tag_hist_type"));
            values.put(DatabaseContract.Historico.COL_DATE, data.getExtras().getLong("tag_hist_date"));
            values.put(DatabaseContract.Historico.COL_IRMAOS, data.getExtras().getString("tag_hist_ip"));
            getContentResolver().insert(DatabaseProvider.HIST_URI, values);

            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    public void onUpdateActividade(long id) {

    }

    public void onDeleteActividade(long id) {
        String[] args = { String.valueOf(id) };
        getContentResolver().delete(DatabaseProvider.HIST_URI, "_id=?", args);
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    public void onInspectActividade(long id) {
        Intent intent = new Intent(this, InspectActividade.class);
        String[] projection = {DatabaseContract.Historico.COL_NAME, DatabaseContract.Historico.COL_TYPE,
                DatabaseContract.Historico.COL_DATE, DatabaseContract.Historico.COL_IRMAOS};
        String[] args = { String.valueOf(id) };
        Cursor c = getContentResolver().query(DatabaseProvider.HIST_URI, projection, "_id=?", args, null);
        c.moveToFirst();
        intent.putExtra("actv_name", c.getString(c.getColumnIndex(DatabaseContract.Historico.COL_NAME)));
        intent.putExtra("actv_type", c.getString(c.getColumnIndex(DatabaseContract.Historico.COL_TYPE)));
        intent.putExtra("actv_date", c.getLong(c.getColumnIndex(DatabaseContract.Historico.COL_DATE)));
        intent.putExtra("actv_irmaos", c.getString(c.getColumnIndex(DatabaseContract.Historico.COL_IRMAOS)));
        intent.putExtra("actv_id", id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actividades_menu, menu);;
        menu.findItem(R.id.menu_add_actividade).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_actividade:
                Intent intent = new Intent(getApplicationContext(), AddActividade.class);
                startActivityForResult(intent, ENTER_DATA_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
