package pt.jpa.groupgenerator.activities;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import pt.jpa.groupgenerator.R;
import pt.jpa.groupgenerator.model.DatabaseContract;
import pt.jpa.groupgenerator.model.DatabaseProvider;
import pt.jpa.groupgenerator.model.HistoricoCursorAdapter;
import pt.jpa.groupgenerator.model.IrmaoCursorAdapter;

public class Presencas extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private HistoricoCursorAdapter histAdapter;
    private ListView listHist;
    private static final int ENTER_DATA_REQUEST_CODE = 1;
    private static final int LOADER_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presencas);


        listHist = (ListView) findViewById(R.id.list_historico);
        listHist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                histAdapter.chosenOne = position;
                histAdapter.notifyDataSetChanged();
            }
        });

        Button addActvBtn = (Button) findViewById(R.id.addActvBtn);
        addActvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddActividade.class);
                startActivityForResult(intent, ENTER_DATA_REQUEST_CODE);
            }
        });
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        return new CursorLoader(this, DatabaseProvider.HIST_URI,
                new String[] {DatabaseContract.Historico.COL_NAME,
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
}
