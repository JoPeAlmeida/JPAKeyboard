package pt.jpa.groupgenerator.model;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import pt.jpa.groupgenerator.R;
import pt.jpa.groupgenerator.activities.Actividades;
import pt.jpa.groupgenerator.activities.Irmaos;

/**
 * Created by sebasi on 28/06/2017.
 */

public class HistoricoCursorAdapter extends BaseAdapter {

    private Cursor cursor;
    private Context mContext;
    private LayoutInflater inflater;
    public int chosenOne = -1;

    public HistoricoCursorAdapter(Context context, Cursor c) {
        mContext = context;
        this.cursor = c;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {return cursor.getCount();}

    @Override
    public Object getItem(int position) {return null;}

    @Override
    public long getItemId(int position) {return position;}

    public long getCurrentId() {
        cursor.moveToPosition(chosenOne);
        return cursor.getLong(cursor.getColumnIndex("_id"));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;
        cursor.moveToPosition(position);
        if (view == null) {
            view = inflater.inflate(R.layout.single_row_actividade, parent, false);
            holder = new Holder();
            holder.tvHistName = (TextView) view.findViewById(R.id.tv_historico_name);
            holder.tvHistType = (TextView) view.findViewById(R.id.tv_historico_type);
            holder.tvHistDate = (TextView) view.findViewById(R.id.tv_historico_date);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.tvHistName.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
        holder.tvHistType.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));

        long dateTimeMillis = cursor.getLong(cursor.getColumnIndex(cursor.getColumnName(3)));

        holder.tvHistDate.setText(getDateString(dateTimeMillis));
        ImageButton historicoDeleteBtn = (ImageButton) view.findViewById(R.id.btn_historico_delete);
        ImageButton historicoUpdateBtn = (ImageButton) view.findViewById(R.id.btn_historico_update);
        ImageButton historicoInspectBtn = (ImageButton) view.findViewById(R.id.btn_historico_inspect);
        historicoDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContext instanceof Actividades) {
                    ((Actividades)mContext).onDeleteActividade(getCurrentId());
                }
            }
        });
        historicoUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContext instanceof Actividades) {
                    ((Actividades)mContext).onUpdateActividade(getCurrentId());
                }
            }
        });
        historicoInspectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContext instanceof Actividades) {
                    ((Actividades)mContext).onInspectActividade(getCurrentId());
                }
            }
        });
        if (position == chosenOne) {
            historicoDeleteBtn.setVisibility(View.VISIBLE);
            historicoUpdateBtn.setVisibility(View.VISIBLE);
            historicoInspectBtn.setVisibility(View.VISIBLE);
        } else {
            historicoDeleteBtn.setVisibility(View.INVISIBLE);
            historicoUpdateBtn.setVisibility(View.INVISIBLE);
            historicoInspectBtn.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    private class Holder {
        TextView tvHistName, tvHistType, tvHistDate;
    }

    private String getDateString(long datetimeMillis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(datetimeMillis);
        DateFormat sdf = SimpleDateFormat.getDateInstance();
        return sdf.format(c.getTime());
    }
}
