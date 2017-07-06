package pt.jpa.groupgenerator.utils;

import android.widget.DatePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by sebasi on 06/07/2017.
 */

public class DateHelper {


    public static long getDateAsMillis(DatePicker datePicker) {
        Calendar c = Calendar.getInstance();
        c.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        return c.getTimeInMillis();
    }

    public static String getDateAsString(long datetimeMillis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(datetimeMillis);
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(c.getTime());
    }
}
