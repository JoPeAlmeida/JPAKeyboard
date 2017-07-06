package pt.jpa.groupgenerator.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import pt.jpa.groupgenerator.R;
import pt.jpa.groupgenerator.model.DatabaseContract;
import pt.jpa.groupgenerator.model.DatabaseProvider;
import pt.jpa.groupgenerator.model.IrmaoCursorAdapter;
import pt.jpa.groupgenerator.model.IrmaoPresencaAdapter;
import pt.jpa.groupgenerator.utils.DateHelper;

public class AddActividade extends BaseActivity implements LoaderCallbacks<Cursor> {

    EditText etHistName;
    Spinner spHistType;
    DatePicker dpHistDate;
    IrmaoPresencaAdapter ipAdapter;
    ListView ipList;
    private static final int LOADER_ID = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_actividade);
        setupUI(findViewById(R.id.actv_add_actv));

        spHistType = (Spinner) findViewById(R.id.sp_hist_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.actv_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spHistType.setAdapter(adapter);

        etHistName = (EditText) findViewById(R.id.et_hist_name);
        etHistName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    v.clearFocus();
                    return true;
                }
                return false;
            }
        });
        dpHistDate = (DatePicker) findViewById(R.id.dp_hist_date);
        final Calendar c = Calendar.getInstance();
        dpHistDate.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), null);

        Button btnAdd = (Button) findViewById(R.id.btn_hist_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAdd(view);
            }
        });

        ipList = (ListView) findViewById(R.id.list_ip);
        ipList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb = (CheckBox)view.findViewById(R.id.cb_irmao_presenca);
                cb.callOnClick();
            }
        });

        setToolbar(R.string.toolbar_add_actividade);
        setActionBar(getToolbar());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getLoaderManager().initLoader(LOADER_ID, null, this);
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
        ipAdapter = new IrmaoPresencaAdapter(this, cursor);
        ipList.setAdapter(ipAdapter);
    }

    @Override
    public void onLoaderReset(Loader arg0) {
    }

    public void onAdd(View btnAdd) {
        String histName = etHistName.getText().toString();
        String histType = spHistType.getSelectedItem().toString();
        long histDate = DateHelper.getDateAsMillis(dpHistDate);

        ArrayList<Long> alistip = new ArrayList<>();
        Cursor cursor = ipAdapter.getCursor();
        cursor.moveToFirst();
        for (int i = 0; i < ipList.getCount(); i++) {
            long id = cursor.getLong(cursor.getColumnIndex("_id"));
            View v = ipList.getChildAt(i);
            CheckBox cb = (CheckBox)v.findViewById(R.id.cb_irmao_presenca);
            if (cb.isChecked()) {
                alistip.add(id);
            }
        }

        JSONObject json = new JSONObject();
        try {
            json.put("alistip", new JSONArray(alistip));
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        JSONObject json = new JSONObject(stringreadfromsqlite);
//        ArrayList items = json.optJSONArray("uniqueArrays");
        String ipList = json.toString();


        if (!histName.isEmpty() && !histType.isEmpty()) {
            Intent newIntent = getIntent();
            newIntent.putExtra("tag_hist_name", histName);
            newIntent.putExtra("tag_hist_type", histType);
            newIntent.putExtra("tag_hist_date", histDate);
            newIntent.putExtra("tag_hist_ip", ipList);
            this.setResult(RESULT_OK, newIntent);
            finish();
        }
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(AddActividade.this);
                    findViewById(R.id.et_hist_name).clearFocus();
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
