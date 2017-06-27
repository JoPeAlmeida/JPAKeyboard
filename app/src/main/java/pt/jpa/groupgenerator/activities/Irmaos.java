package pt.jpa.groupgenerator.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import pt.jpa.groupgenerator.R;
import pt.jpa.groupgenerator.model.DatabaseContract;
import pt.jpa.groupgenerator.model.IrmaoCursorAdapter;
import pt.jpa.groupgenerator.model.IrmaosDAO;

public class Irmaos extends Activity implements LoaderCallbacks<Cursor> {

    private IrmaoCursorAdapter irmaoCursorAdapter;
    private IrmaosDAO irmaosDAO;
    private ListView listView;
    private static final int ENTER_DATA_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irmaos);

        irmaosDAO = new IrmaosDAO(this);
        irmaosDAO.open();
        listView = (ListView) findViewById(R.id.list_irmaos);
        Button addIrmao = (Button) findViewById(R.id.addIrmaoBtn);
        addIrmao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddIrmao(v);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        return new CursorLoader(this, null,
                new String[] {DatabaseContract.Irmaos.COL_NAME,
                        DatabaseContract.Irmaos.COL_EMAIL,
                        DatabaseContract.Irmaos.COL_PHONE}, null, null, null) {
            @Override
            public Cursor loadInBackground() {
                irmaosDAO.open();
                return irmaosDAO.getAllIrmaos();
            }
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

            irmaosDAO.insertIrmao(data.getExtras().getString("tag_irmao_name"),
                    data.getExtras().getString("tag_irmao_email"),
                    data.getExtras().getString("tag_irmao_phone"));

            irmaoCursorAdapter.notifyDataSetChanged();
        }
    }
}
