package pt.jpa.groupgenerator.model;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import pt.jpa.groupgenerator.R;


/**
 * Created by sebasi on 26/06/2017.
 */

public class IrmaoCursorAdapter extends BaseAdapter {

    private Cursor cursor;
    private Context mContext;
    private LayoutInflater inflater;
    public int chosenOne = -1;

    public IrmaoCursorAdapter(Context context, Cursor c) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;
        cursor.moveToPosition(position);
        if (view == null) {
            view = inflater.inflate(R.layout.single_row_irmao, parent, false);
            holder = new Holder();
            holder.tvIrmaoName = (TextView) view.findViewById(R.id.tv_irmao_name);
            holder.tvIrmaoEmail = (TextView) view.findViewById(R.id.tv_irmao_email);
            holder.tvIrmaoPhone = (TextView) view.findViewById(R.id.tv_irmao_phone);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.tvIrmaoName.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0))));
        holder.tvIrmaoEmail.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
        holder.tvIrmaoPhone.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));
        if (position == chosenOne) {
            ImageButton irmaoDeleteBtn = (ImageButton) view.findViewById(R.id.irmaoDeleteBtn);
            irmaoDeleteBtn.setVisibility(View.VISIBLE);
            ImageButton irmaoUpdateBtn = (ImageButton) view.findViewById(R.id.irmaoUpdateBtn);
            irmaoUpdateBtn.setVisibility(View.VISIBLE);
        } else {
            ImageButton irmaoDeleteBtn = (ImageButton) view.findViewById(R.id.irmaoDeleteBtn);
            irmaoDeleteBtn.setVisibility(View.INVISIBLE);
            ImageButton irmaoUpdateBtn = (ImageButton) view.findViewById(R.id.irmaoUpdateBtn);
            irmaoUpdateBtn.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    private class Holder {
        TextView tvIrmaoName, tvIrmaoEmail, tvIrmaoPhone;
    }
}
