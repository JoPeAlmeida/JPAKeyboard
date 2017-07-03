package pt.jpa.groupgenerator.model;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import pt.jpa.groupgenerator.R;

/**
 * Created by sebasi on 03/07/2017.
 */

public class IrmaoSpouseAdapter extends BaseAdapter {

    private Cursor cursor;
    private Context mContext;
    private LayoutInflater inflater;

    public IrmaoSpouseAdapter(Context context, Cursor c) {
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

    public Cursor getCursor() {return cursor;}

    public int getSpousePosition(long id) {
        cursor.moveToFirst();
        int i = 0;
        while (!cursor.isAfterLast()) {
            if (cursor.getLong(cursor.getColumnIndex("_id")) != id) {
                i++;
                cursor.moveToNext();
            }
            else {
                return i;
            }
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;
        cursor.moveToPosition(position);
        if (view == null) {
            view = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
            holder = new Holder();
            holder.tvIrmaoName = (TextView) view.findViewById(android.R.id.text1);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.tvIrmaoName.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
        return view;
    }

    private class Holder {
        TextView tvIrmaoName;
    }
}
