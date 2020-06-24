package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.example.simpletodo.MainActivity.ITEM_POSITON;
import static com.example.simpletodo.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {
    /* Main Variable & Object Declarations */
    private EditText etItemText;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        /* Resolve EditText from Layout */
        etItemText = findViewById(R.id.etItemText);

        /* Set EditText value from Intent extra */
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));

        /* Update position from Intent extra */
        position = getIntent().getIntExtra(ITEM_POSITON, 0);

        /* Update title bar of activity */
        getSupportActionBar().setTitle("Edit Item");
    }

    /* Handler for save button */
    public void onSaveItem(View v){
        /* Prepare new Intent for result */
        Intent i = new Intent();

        /* Pass updated item text as extra */
        i.putExtra(ITEM_TEXT, etItemText.getText().toString());

        /* Pass current position as extra */
        i.putExtra(ITEM_POSITON, position);

        /* Set the intent as the result of the activity */
        setResult(RESULT_OK, i);

        /* Close the activity and go back to Main */
        finish();
    }
}