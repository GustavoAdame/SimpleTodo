package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.apache.commons.io.FileUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.nio.charset.Charset;
import java.util.*;
import java.io.*;

public class MainActivity extends AppCompatActivity {
    /* Main Variable & Object Declarations */
    private ArrayAdapter<String> itemsAdapter;
    private ArrayList<String> items;
    private Button btnAdd;
    private EditText etItem;
    private ListView lvItem;

    /* Numeric code to identify the EditActivity */
    public final static int EDIT_REQUEST_CODE = 20;

    /* String constants passed to EditActivity */
    public final static String ITEM_TEXT = "itemText";
    public final static String ITEM_POSITON = "itemPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Reference xml elements via their IDS @activity_main.xml */
        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        lvItem = findViewById(R.id.lvItem);

        /* Calling to initalize ArrayList or current ArrayList stored in todo.txt */
        readItems();

        /* Initalize the ArrayAdapter and passing default arguments */
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        /* Pass the ArrayAdapter to display onto ListView */
        lvItem.setAdapter(itemsAdapter);

        /* Calling to provide long_press delete functionality of the app */
        setupListViewListener();
    }

    /* This method is connected to the Button xml element @activity_main.xml */
    public void onAddItem(View v){
        /* Initalize a String that contains user input via the EditText */
        String itemText = etItem.getText().toString();

        /* Add current user input into ArrayAdapter when the Button is clicked */
        itemsAdapter.add(itemText);

        /* After adding to ArrayAdapter, reset EditText to null */
        etItem.setText("");

        /* Calling to update current ArrayList of items to File stored */
        writeItems();

        /* A reassuring message to user */
        Toast.makeText(getApplicationContext(), "Item Added!", Toast.LENGTH_SHORT).show();
    }

    /* This method is contains the logic of the delete funtionality */
    private void setupListViewListener(){

        /* Calling a method on ListView: setOnItemLongClickListene() - a standard application of this method
        * Mostly AutoCompleted */
        lvItem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                /* Removes item from ArrayList by whatever position the user long_click the ListView */
                items.remove(position);

                /* Calling a method on ArrayAdapter: notifyDataSetChanged() which updates the Adapter after deletion - Standard Application */
                itemsAdapter.notifyDataSetChanged();

                /* Calling to update current ArrayList of items to File stored */
                writeItems();

                /* A reassuring message to user */
                Toast.makeText(getApplicationContext(), "Item Deleted!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        /* Calling a method on ListView: setOnItemClickListene() - a standard application of this method
         * Mostly AutoCompleted */
        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                /* Create the new activity */
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);

                /* Pass the data being edited */
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITON, position);

                /* Display the Activity */
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        });
    }

    /* Handle Result from Edit Activity */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*If Edit Activity completed OK*/
        if(resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE){
            /* Extract update item text from result intent extra */
            String updatedItem = data.getExtras().getString(ITEM_TEXT);

            /* Extract current position of Edited text */
            int position = data.getExtras().getInt(ITEM_POSITON);

            /* Update model with new items */
            items.set(position, updatedItem);

            /* Notify ItemsAdapter about change */
            itemsAdapter.notifyDataSetChanged();

            /* Make sure data persist */
            writeItems();

            /* A reassuring message to user */
            Toast.makeText(this, "Item Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    /* This method is helpful in setting up a file to use throughout the app, so it can be called whenever */
    private File getDataFile(){
        return new File(getFilesDir(), "todo.txt");
    }

    /* This method gets current ArrayList from File, Else makes a new instance */
    private void readItems(){
        try{
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), String.valueOf(Charset.defaultCharset())));
        } catch (IOException e){
            items = new ArrayList<String>();
            Log.e("readItems", "Error reading", e);
        }
    }

    /* This method writes to the same File as readItems() the latest ArrayList*/
    private void writeItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e){
            Log.e("writeItems", "Error reading", e);
        }
    }


}