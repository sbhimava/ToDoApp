package com.sandeep.todoapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class ToDoActivity extends Activity {

	private ArrayList<String> todoItems;
	private ArrayAdapter<String> todoAdapter;
	private ListView lvItems;
	private EditText etAddItem;
	private int REQUEST_EDIT = 1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        etAddItem = (EditText) findViewById(R.id.etAddItem);
        readItems();
        todoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
        lvItems = (ListView) findViewById(R.id.lvItems);
        setupListViewListeners();
        lvItems.setAdapter(todoAdapter);
    }
    
    public void addTodoItem(View v) {
    	String item = etAddItem.getText().toString().trim();
    	etAddItem.setText("");
    	String toastMessage;
    	if (item.equals("")) {
    		toastMessage = getResources().getString(R.string.empty_item_toast_message);
    	} else {
	    	todoAdapter.add(item);
	    	writeItems();
	    	toastMessage = getResources().getString(R.string.add_toast_message) + " '" + item + "'";
    	}
    	Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }
    
    private void setupListViewListeners() {
    	
    	lvItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(ToDoActivity.this, EditItemActivity.class);
				i.putExtra("position", position);
				i.putExtra("text", todoItems.get(position));
				startActivityForResult(i, REQUEST_EDIT);
			}
		});
    	
    	lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				todoItems.remove(position);
				todoAdapter.notifyDataSetChanged();
				writeItems();
				return true;
			}
    		
		});
    }
    
    private void readItems() {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	try {
    		todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
    	} catch(IOException e) {
    		todoItems = new ArrayList<String>();
    	}
    }
    
    private void writeItems() {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	try {
    		FileUtils.writeLines(todoFile, todoItems);
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
	}
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK) {
    		int position = data.getIntExtra("position", -1);
    		String editedItem = data.getStringExtra("itemText");
    		todoItems.set(position, editedItem);
    		todoAdapter.notifyDataSetChanged();
    		writeItems();
    	}
    }
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.to_do, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
