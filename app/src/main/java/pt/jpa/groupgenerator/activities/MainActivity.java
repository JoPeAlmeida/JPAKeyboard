package pt.jpa.groupgenerator.activities;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import pt.jpa.groupgenerator.R;

public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar(R.string.toolbar_main);
        setActionBar(getToolbar());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);;
        menu.findItem(R.id.menu_irmaos).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);;
        menu.findItem(R.id.menu_actividades).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_irmaos:
                Intent intent = new Intent(getApplicationContext(), Irmaos.class);
                startActivity(intent);
                return true;
            case R.id.menu_actividades:
                intent = new Intent(getApplicationContext(), Actividades.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
