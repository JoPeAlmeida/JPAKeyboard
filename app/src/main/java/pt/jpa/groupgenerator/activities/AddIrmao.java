package pt.jpa.groupgenerator.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pt.jpa.groupgenerator.R;

public class AddIrmao extends Activity {

    EditText etIrmaoName, etIrmaoEmail, etIrmaoPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_irmao);

        etIrmaoName = (EditText) findViewById(R.id.et_irmao_name);
        etIrmaoEmail = (EditText) findViewById(R.id.et_irmao_email);
        etIrmaoPhone = (EditText) findViewById(R.id.et_irmao_phone);
        Button btnAdd = (Button) findViewById(R.id.addBtn);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAdd(view);
            }
        });
    }

    public void onAdd(View btnAdd) {
        String irmaoName = etIrmaoName.getText().toString();
        String irmaoEmail = etIrmaoEmail.getText().toString();
        String irmaoPhone = etIrmaoPhone.getText().toString();

        if (!irmaoName.isEmpty() && !irmaoEmail.isEmpty() && !irmaoPhone.isEmpty()) {
            Intent newIntent = getIntent();
            newIntent.putExtra("tag_irmao_name", irmaoName);
            newIntent.putExtra("tag_irmao_email", irmaoEmail);
            newIntent.putExtra("tag_irmao_phone", irmaoPhone);
            this.setResult(RESULT_OK, newIntent);
            finish();
        }
    }
}
