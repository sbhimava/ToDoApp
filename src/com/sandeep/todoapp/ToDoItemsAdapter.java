package com.sandeep.todoapp;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ToDoItemsAdapter extends ArrayAdapter<ToDoItem>{

	public ToDoItemsAdapter(Context context, List<ToDoItem> items) {
		super(context, R.layout.todo_item, items);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ToDoItem item = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
		}
		TextView tvItemName = (TextView) convertView.findViewById(R.id.tvItemName);
		TextView tvItemPriority = (TextView) convertView.findViewById(R.id.tvItemPriority);
		TextView tvtvItemDueDate = (TextView) convertView.findViewById(R.id.tvItemDueDate);
		tvItemName.setText(item.getName());
		int priority = item.getPriority();
		String priorityText = convertView.getResources().getString(R.string.item_priority_placeholder)
				+ ": " + ToDoDao.PRIORITY_STRINGS[priority];
		tvItemPriority.setText(priorityText);
		Date dueDate = item.getDueDate();
		if (dueDate != null) {
			Calendar cal = Calendar.getInstance();
            cal.setTime(dueDate);
			tvtvItemDueDate.setText("" + (cal.get(Calendar.MONTH)+1) + "-"
					+ cal.get(Calendar.DATE) + "-" + cal.get(Calendar.YEAR));
		}
		return convertView;
	}
}
