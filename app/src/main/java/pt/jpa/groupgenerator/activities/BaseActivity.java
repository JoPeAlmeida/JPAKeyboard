package pt.jpa.groupgenerator.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toolbar;

import pt.jpa.groupgenerator.R;

/**
 * Created by sebasi on 05/07/2017.
 */

abstract class BaseActivity extends Activity {

    protected Toolbar actvToolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setToolbar(int resId) {
        if (actvToolbar == null) {
            actvToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        }
        if (actvToolbar != null) {
            actvToolbar.setTitle(resId);
        }
    }

    protected Toolbar getToolbar() {
        return actvToolbar;
    }
}
