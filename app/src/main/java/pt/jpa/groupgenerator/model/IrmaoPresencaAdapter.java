package pt.jpa.groupgenerator.model;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import pt.jpa.groupgenerator.R;

/**
 * Created by sebasi on 28/06/2017.
 */

public class IrmaoPresencaAdapter extends BaseAdapter {

    private Cursor cursor;
    private Context mContext;
    private LayoutInflater inflater;

    public IrmaoPresencaAdapter(Context context, Cursor c) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;
        cursor.moveToPosition(position);
        if (view == null) {
            view = inflater.inflate(R.layout.single_row_irmao_presenca, parent, false);
            holder = new Holder();
            holder.tvIrmaoName = (TextView) view.findViewById(R.id.tv_irmao_presenca);
            holder.cbIrmaoPresenca = (CheckBox) view.findViewById(R.id.cb_irmao_presenca);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.tvIrmaoName.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
//        holder.cbIrmaoPresenca.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CheckBox cb = (CheckBox) v;
//                cb.toggle();
//            }
//        });

        return view;
    }

    private class Holder {
        TextView tvIrmaoName;
        CheckBox cbIrmaoPresenca;
    }
}
