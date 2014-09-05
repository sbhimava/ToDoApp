package com.sandeep.todoapp;

import java.util.Calendar;
import java.util.Date;

import android.support.v4.app.DialogFragment;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class EditItemDialog extends DialogFragment {
	private EditText etEditName;
	private TextView tvlblDueDateValue;
	private Spinner spinnerItemPriority;
	private LinearLayout editItemLayout_duedate;
	private Button itembtnSave;
	private Date dueDate;
	private long id;
	
	public interface EditItemDialogListener {
        void onFinishEditDialog(long id, String name, Date dueDate, int priority);
    }
	
	public EditItemDialog() {
		// Empty constructor
	}
	
	public static EditItemDialog newInstance(long id, String name, Date dueDate, int priority) {
		EditItemDialog fragment = new EditItemDialog();
		Bundle args = new Bundle();
		args.putLong("id", id);
		args.putString("name", name);
		if (dueDate != null) {
			Calendar cal = Calendar.getInstance();
	        cal.setTime(dueDate);
	        String date = "" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE) + "-" + cal.get(Calendar.YEAR);
	        args.putString("dueDate", date);
			args.putInt("dueDate_date", cal.get(Calendar.DATE));
			args.putInt("dueDate_month", cal.get(Calendar.MONTH));
			args.putInt("dueDate_year", cal.get(Calendar.YEAR));
		}
		args.putInt("priority", priority);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_edit_item, container);
		
		id = getArguments().getLong("id", -1);
		String name = getArguments().getString("name", getActivity().getResources().getString(R.string.item_label_name));
		etEditName = (EditText) view.findViewById(R.id.etEditName);
		etEditName.setText(name);
		
		spinnerItemPriority = (Spinner) view.findViewById(R.id.spinnerItemPriority);
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, ToDoDao.PRIORITY_STRINGS);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerItemPriority.setAdapter(spinnerAdapter);
		int spinnerPosition = getArguments().getInt("priority", 0);
		spinnerItemPriority.setSelection(spinnerPosition);
		
		tvlblDueDateValue = (TextView) view.findViewById(R.id.tvlblDueDateValue);
		String dueDate = getArguments().getString("dueDate", getActivity().getResources().getString(R.string.item_label_none));
		tvlblDueDateValue.setText(dueDate);
		editItemLayout_duedate = (LinearLayout) view.findViewById(R.id.editItemLayout_duedate);
		editItemLayout_duedate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDueDatePicker(getArguments().getInt("dueDate_year", -1),
						getArguments().getInt("dueDate_month", -1),
						getArguments().getInt("dueDate_date", -1));
			}
		});
		
		itembtnSave = (Button) view.findViewById(R.id.itembtnSave);
		itembtnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onSaveAction();
			}
		});
		
		getDialog().setTitle(getActivity().getResources().getString(R.string.item_edit_title));
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return view;
	}

	public void showDueDatePicker(int year, int month, int date) {
		final Calendar c = Calendar.getInstance();
	    if (year == -1) {
	    	year = c.get(Calendar.YEAR);
	    }
	    if (month == -1) {
	    	month = c.get(Calendar.MONTH);
	    }
	    if (date == -1) {
	    	date = c.get(Calendar.DAY_OF_MONTH);
	    }
		DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePickerListener, year, month, date);
		dialog.show();
	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(0);
			cal.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0);
			dueDate = cal.getTime();
			
			tvlblDueDateValue.setText(new StringBuilder()
					.append(selectedMonth+1)
					.append("-")
					.append(selectedDay)
					.append("-")
					.append(selectedYear));
		}
	};
	
	public void onSaveAction() {
		EditItemDialogListener listener = (EditItemDialogListener) getActivity();
        listener.onFinishEditDialog(id, etEditName.getText().toString(), dueDate, 
        		spinnerItemPriority.getSelectedItemPosition());
        dismiss();
	}
}
