package pt.jpa.groupgenerator.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pt.jpa.groupgenerator.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button irmaosBtn = (Button) findViewById(R.id.btn_irmaos);
        irmaosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Irmaos.class);
                startActivity(intent);
            }
        });

        Button histBtn = (Button) findViewById(R.id.btn_main_hist);
        histBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Actividades.class);
                startActivity(intent);
            }
        });
    }
}
