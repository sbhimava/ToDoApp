package com.sandeep.todoapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import com.sandeep.todoapp.EditItemDialog.EditItemDialogListener;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ToDoActivity extends FragmentActivity implements EditItemDialogListener {

	private ToDoDao todoDao;
	private ArrayList<ToDoItem> todoItems;
	private ToDoItemsAdapter todoAdapter;
	private ListView lvItems;
	private EditText etAddItem;
	private int selectedPosition;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        
        todoDao = new ToDoDao(this);
        todoDao.open();
        readItems();
        todoAdapter = new ToDoItemsAdapter(this, todoItems);
        lvItems = (ListView) findViewById(R.id.lvItems);
        setupListViewListeners();
        lvItems.setAdapter(todoAdapter);
        
        etAddItem = (EditText) findViewById(R.id.etAddItem);
    }
    
    public void addTodoItem(View v) {
    	String name = etAddItem.getText().toString().trim();
    	etAddItem.setText("");
    	String toastMessage;
    	if (name.equals("")) {
    		toastMessage = getResources().getString(R.string.empty_item_toast_message);
    	} else {
			try {
				ToDoItem item = todoDao.createItem(name, null, ToDoDao.PRIORITY_NORMAL, 
						null, null, ToDoDao.REPEAT_NONE, ToDoDao.COMPLETED_FALSE);
				todoAdapter.add(item);
				toastMessage = getResources().getString(R.string.add_toast_message) + " '" + name + "'";
			} catch (ParseException e) {
				toastMessage = getResources().getString(R.string.empty_item_toast_message);
			}
    	}
    	Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }
    
    private void setupListViewListeners() {
    	
    	lvItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ToDoItem item = todoItems.get(position);
				selectedPosition = position;
				showEditDialog(item.getId(), item.getName(), item.getDueDate(), item.getPriority());
			}
		});
    	
    	lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				ToDoItem item = todoItems.get(position);
				todoDao.deleteItem(item);
				todoItems.remove(position);
				todoAdapter.notifyDataSetChanged();
				return true;
			}
		});
    }
    
    private void readItems() {
    	try {
    		todoItems = todoDao.getAllItems();
    	} catch(ParseException e) {
    		todoItems = new ArrayList<ToDoItem>();
    	}
    }
    
    private void showEditDialog(long id, String name, Date date, int priority) {
        FragmentManager fm = getSupportFragmentManager();
        EditItemDialog editDialog = EditItemDialog.newInstance(id, name, date, priority);
        editDialog.show(fm, "fragment_edit_item");
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

	@Override
	public void onFinishEditDialog(long id, String name, Date dueDate, int priority) {
		try {
			int result = todoDao.updateItem(id, name, null, priority, dueDate, null, ToDoDao.REPEAT_NONE, ToDoDao.COMPLETED_FALSE);
			if (result == 1) {
				ToDoItem item = todoItems.get(selectedPosition);
				item.setName(name);
				item.setDueDate(dueDate);
				item.setPriority(priority);
				todoAdapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
