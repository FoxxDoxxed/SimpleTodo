package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import org.apache.commons.io.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> items;

    Button buttonAdd;
    EditText editTextItem;
    RecyclerView recyclerViewItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd = findViewById(R.id.buttonAdd);
        editTextItem = findViewById(R.id.editTextItem);
        recyclerViewItems = findViewById(R.id.recyclerViewItems);

        //editTextItem.setText("I'm doing this from java");

        loadItems();
        //Previous mock data:
        //items = new ArrayList<>();
        //items.add("Buy milk");
        //items.add("Go to the gym");
        //items.add("Have fun");

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                //Delete the item from the model
                items.remove(position);
                //Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        itemsAdapter = new ItemsAdapter(items, onLongClickListener);

        recyclerViewItems.setAdapter(itemsAdapter);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = editTextItem.getText().toString();
                //Add item to the model
                items.add(todoItem);
                //Notify adapter than an item is inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);
                editTextItem.setText("");

                //a toast is a small popup or dialog that briefly shows to the user
                //and then disappears
                Toast.makeText(getApplicationContext(), "Item was added",
                        Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    //returns file that stores list of todo items
    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    //this function will load items by reading every line of the data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    //this function saves items by writing them into the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}