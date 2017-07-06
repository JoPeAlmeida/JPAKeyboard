package pt.jpa.groupgenerator.activities;

import android.app.Activity;
import android.content.ContentProvider;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import pt.jpa.groupgenerator.R;
import pt.jpa.groupgenerator.model.DatabaseContract;
import pt.jpa.groupgenerator.model.DatabaseProvider;
import pt.jpa.groupgenerator.utils.DateHelper;

public class InspectActividade extends BaseActivity {

    TextView tvActvName, tvActvType, tvActvDate;
    ListView listActvIrmaos;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_actividade);

        tvActvName = (TextView) findViewById(R.id.tv_actv_name);
        tvActvName.setText(this.getIntent().getStringExtra("actv_name"));
        tvActvType = (TextView) findViewById(R.id.tv_actv_type);
        tvActvType.setText(this.getIntent().getStringExtra("actv_type"));
        tvActvDate = (TextView) findViewById(R.id.tv_actv_date);

        long dateMillis = this.getIntent().getLongExtra("actv_date", 0);

        tvActvDate.setText(DateHelper.getDateAsString(dateMillis));
        ArrayList<String> ipList = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(this.getIntent().getStringExtra("actv_irmaos"));
            JSONArray jsonArray = json.optJSONArray("alistip");
            for (int i = 0; i < jsonArray.length(); i++) {
                long id = jsonArray.optLong(i, -1);
                if (id >= 0) ipList.add(String.valueOf(id));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] projection = {DatabaseContract.Irmaos.COL_NAME};
        String selection = "_id IN (" + makePlaceHolders(ipList.size()) + ")";
        String[] args = ipList.toArray(new String[ipList.size()]);
        Cursor c = getContentResolver().query(DatabaseProvider.CONTENT_URI,
                projection, selection, args, null);

        ArrayList<String> finalIpList = getListFromCursor(c);
        listActvIrmaos = (ListView) findViewById(R.id.list_actv_ip);
        //listActvIrmaos.addHeaderView((TextView)findViewById(R.id.list_actv_ip_header));
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, finalIpList);
        listActvIrmaos.setAdapter(listAdapter);

        setToolbar(R.string.toolbar_inspt_actividade);
        setActionBar(getToolbar());
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private String makePlaceHolders(int lenght) {
        if (lenght > 0) {
            StringBuilder sb = new StringBuilder(lenght * 2 - 1);
            sb.append("?");
            for (int i = 1; i < lenght; i++) {
                sb.append(",?");
            }
            return sb.toString();
        } else return "";
    }

    private  ArrayList<String> getListFromCursor(Cursor c) {
        ArrayList<String> list = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            list.add(c.getString(c.getColumnIndex(DatabaseContract.Irmaos.COL_NAME)));
        }
        return list;
    }

}
