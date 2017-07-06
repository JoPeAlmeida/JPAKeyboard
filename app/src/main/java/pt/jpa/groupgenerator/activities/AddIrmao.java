package pt.jpa.groupgenerator.activities;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import pt.jpa.groupgenerator.R;
import pt.jpa.groupgenerator.model.DatabaseContract;
import pt.jpa.groupgenerator.model.DatabaseProvider;
import pt.jpa.groupgenerator.model.IrmaoCursorAdapter;
import pt.jpa.groupgenerator.model.IrmaoPresencaAdapter;
import pt.jpa.groupgenerator.model.IrmaoSpouseAdapter;

public class AddIrmao extends BaseActivity implements LoaderCallbacks<Cursor> {

    EditText etIrmaoName, etIrmaoEmail, etIrmaoPhone;
    CheckBox cbIrmaoCasado;
    LinearLayout llCasado;
    Spinner spIrmaoSpouse;
    IrmaoSpouseAdapter spouseAdapter;
    private static final int LOADER_ID = 1;
    String mode;
    long id;
    long spouseId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_irmao);

        mode = this.getIntent().getStringExtra("mode");

        etIrmaoName = (EditText) findViewById(R.id.et_irmao_name);
        etIrmaoEmail = (EditText) findViewById(R.id.et_irmao_email);
        etIrmaoPhone = (EditText) findViewById(R.id.et_irmao_phone);
        spIrmaoSpouse = (Spinner) findViewById(R.id.sp_irmao_spouse);
        llCasado = (LinearLayout) findViewById(R.id.ll_casado);
        cbIrmaoCasado = (CheckBox) findViewById(R.id.cb_irmao_casado);
        cbIrmaoCasado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    llCasado.setVisibility(View.VISIBLE);
                }
                else {
                    llCasado.setVisibility(View.INVISIBLE);
                }
            }
        });
        Button btnAdd = (Button) findViewById(R.id.btn_add_irmao_add);

        switch (mode) {
            case "ADD":
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            onAdd(view);
                    }
                });
                break;
            case "EDIT":
                etIrmaoName.setText(this.getIntent().getStringExtra("irmao_name"));
                etIrmaoEmail.setText(this.getIntent().getStringExtra("irmao_email"));
                etIrmaoPhone.setText(this.getIntent().getStringExtra("irmao_phone"));
                spouseId = this.getIntent().getLongExtra("irmao_spouse", -1);
                if (spouseId != -1) {
                    cbIrmaoCasado.callOnClick();
                    cbIrmaoCasado.setChecked(true);
                }
                id = this.getIntent().getLongExtra("irmao_id", -1);
                btnAdd.setText("Alterar Irmão");
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onEdit(view);
                    }
                });
                break;
            default:
        }
        setToolbar(R.string.toolbar_add_irmao);
        setActionBar(getToolbar());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    public void onAdd(View btnAdd) {
        String irmaoName = etIrmaoName.getText().toString();
        String irmaoEmail = etIrmaoEmail.getText().toString();
        String irmaoPhone = etIrmaoPhone.getText().toString();
        Cursor c = spouseAdapter.getCursor();

        long spouseId = c.getCount() > 0 ? c.getLong(c.getColumnIndex("_id")) : -1;
        long hasSpouse = cbIrmaoCasado.isChecked() ? 1 : 0;
        long[] spouse = new long[]{hasSpouse, spouseId};

        if (!irmaoName.isEmpty() && !irmaoEmail.isEmpty() && !irmaoPhone.isEmpty()) {
            Intent newIntent = getIntent();
            newIntent.putExtra("tag_irmao_name", irmaoName);
            newIntent.putExtra("tag_irmao_email", irmaoEmail);
            newIntent.putExtra("tag_irmao_phone", irmaoPhone);
            newIntent.putExtra("tag_irmao_spouse", spouse);
            this.setResult(RESULT_OK, newIntent);
            finish();
        }
    }

    public void onEdit(View btnAdd) {
        String irmaoName = etIrmaoName.getText().toString();
        String irmaoEmail = etIrmaoEmail.getText().toString();
        String irmaoPhone = etIrmaoPhone.getText().toString();
        Cursor c = spouseAdapter.getCursor();

        long spouseId = c.getLong(c.getColumnIndex("_id"));
        long hasSpouse = cbIrmaoCasado.isChecked() ? 1 : 0;
        long[] spouse = new long[]{hasSpouse, spouseId};

        if (!irmaoName.isEmpty() && !irmaoEmail.isEmpty() && !irmaoPhone.isEmpty()) {
            Intent newIntent = getIntent();
            newIntent.putExtra("tag_irmao_name", irmaoName);
            newIntent.putExtra("tag_irmao_email", irmaoEmail);
            newIntent.putExtra("tag_irmao_phone", irmaoPhone);
            newIntent.putExtra("tag_irmao_spouse", spouse);
            newIntent.putExtra("tag_irmao_id", id);
            this.setResult(RESULT_OK, newIntent);
            finish();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        return new CursorLoader(this, DatabaseProvider.CONTENT_URI,
                new String[] {"_id", DatabaseContract.Irmaos.COL_NAME}, null, null, null) {
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToFirst();
        spouseAdapter = new IrmaoSpouseAdapter(this, cursor);
        spIrmaoSpouse.setAdapter(spouseAdapter);
        spIrmaoSpouse.setSelection(spouseAdapter.getSpousePosition(spouseId));
    }

    @Override
    public void onLoaderReset(Loader arg0) {
    }
}
