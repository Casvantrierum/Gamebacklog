package com.example.cas.gamebacklog;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddGameActivity extends AppCompatActivity {

    private EditText mConsole;
    private EditText mName;
    private Spinner dropdown;
    private FloatingActionButton fab;

    private static final String[] posibleStatus = {"Want to play", "Playing", "Stalled", "Dropped"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mName = findViewById(R.id.edit_name);
        mConsole = findViewById(R.id.edit_console);
        fab = findViewById(R.id.fab_add);

        //get the spinner from the xml.
        dropdown = findViewById(R.id.spinner_status);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddGameActivity.this,
                android.R.layout.simple_spinner_item,posibleStatus);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("name",mName.getText().toString());
                intent.putExtra("console",mConsole.getText().toString());
                intent.putExtra("status",dropdown.getSelectedItem().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}
