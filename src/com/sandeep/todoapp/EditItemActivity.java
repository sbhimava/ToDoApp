package com.sandeep.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditItemActivity extends Activity {
	
	private EditText etEditItem;
	private int itemPos;
	private String originalItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		originalItem = getIntent().getStringExtra("text");
		itemPos = getIntent().getIntExtra("position", -1);
		etEditItem = (EditText) findViewById(R.id.etEditItem);
		etEditItem.setText(originalItem);
	}

	public void saveItem(View v) {
		String editedItem = etEditItem.getText().toString().trim();
		if (editedItem.equals("")) {
    		String emptyItem = getResources().getString(R.string.empty_item_toast_message);
    		Toast.makeText(this, emptyItem, Toast.LENGTH_SHORT).show();
    		etEditItem.setText(originalItem);
    	} else {
			Intent data = new Intent();
			data.putExtra("itemText", editedItem);
			data.putExtra("position", itemPos);
			setResult(RESULT_OK, data);
			finish();
    	}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
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
