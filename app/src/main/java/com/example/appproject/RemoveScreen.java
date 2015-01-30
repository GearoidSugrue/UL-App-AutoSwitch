package com.example.appproject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class RemoveScreen extends Activity {

	private int selectedDayAsInt;
	private String selectedDay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remove_screen);
		setTitle("Remove Entry");

		buttons();
		daySpinner();
	}

	public void setSelectedDayAsInt(int dayAsInt) {
		this.selectedDayAsInt = dayAsInt;
	}

	public int getSelectedDayAsInt() {
		return this.selectedDayAsInt;
	}

	public void setSelectedDay(String day) {
		this.selectedDay = day;
	}

	public String getSelectedDay() {
		return this.selectedDay;
	}

	public void daySpinner() {
		final Spinner dayList = (Spinner) findViewById(R.id.daySpinner);

		dayList.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				final String day = String.valueOf(dayList.getSelectedItem());
				final int dayAsInt = dayList.getSelectedItemPosition();
				setSelectedDayAsInt(dayAsInt);
				timesListView();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				dayList.setSelection(0);
			}
		});
	}

	public void buttons() {
		Button clearDayBut = (Button) findViewById(R.id.clearDayBut);
		Button clearAllBut = (Button) findViewById(R.id.clearAllBut);

		clearDayBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String title = "Warning!!!", message = "Delete ALL entries for "
						+ getSelectedDay() + "?";
				final int option = 1;
				comfirmDeleteEntries(title, message, option, 0);
			}
		});

		clearAllBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// comfirmDeleteAllEntries() ;
				final String title = "Warning!!!", message = "Delete ALL Entries?";
				final int option = 2;
				comfirmDeleteEntries(title, message, option, 0);
			}
		});
	}

	// public List<String> formatReadFromFile(Context context) {
	// List<String> timesList = new LinkedList<String>() ;
	//
	// timesList.addAll(FileReader.getSpecificDayTimes(context,getSelectedDayAsInt()))
	// ;
	// timesList.remove(0) ;
	//
	// for(int i = 0 ; i < timesList.size(); i++) {
	// final String str = timesList.get(i) ;
	// String tempOne = str.substring(0,2), tempTwo = str.substring(2,4),
	// tempThree = str.substring(4) ;
	// tempOne = tempOne.concat(":").concat(tempTwo.concat("  -  ")) ;
	// tempOne = tempOne.concat(tempThree);
	// timesList.remove(i) ;
	// timesList.add(i,tempOne) ;
	// }
	// return timesList ;
	// }
	//
	public List<String> formatReadFromFile() {

		List<String> formatedList = new LinkedList<String>();
				
		List<List<String>> oneDayAllData = FileReader.getSpecificDayData(this, getSelectedDayAsInt()) ;
		
		if (oneDayAllData != null) {
			StringBuilder str = new StringBuilder();

			for (List<String> list : oneDayAllData) {
				str.setLength(0);   //emptys the string builder
				str.append(oneDayAllData.get(0));
				str.insert(2, ":");
				str.append(" - ");
				str.append(oneDayAllData.get(1));
				formatedList.add(str.toString());
			}
			return formatedList;
		} else {
			return formatedList ;
		}
	}

	public void timesListView() {
		ListView timesView = (ListView) findViewById(R.id.timesView);
		List<String> formatedList = new LinkedList<String>();

		formatedList.addAll(formatReadFromFile());
		// listOfTimes.addAll(formatReadFromFile()) ;

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				formatedList);
		timesView.setAdapter(adapter);

		timesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				final String title = "Delete this entry?", message = ((TextView) view)
						.getText().toString();
				final int option = 0;
				comfirmDeleteEntries(title, message, option, position);
			}
		});
	}

	public void removeSingleEntry(int listPosition) {
		// listPosition++ ;
		final String result = AddorRemove.removeTime(this,
				getSelectedDayAsInt(), listPosition).concat("Removed");

		timesListView();
		SharedPrefs.toaster(this, result);
		SharedPrefs.resetAlarm(this);
	}

	// public void removeAllEntries() {
	// List<String> resetedFile =
	// Arrays.asList(getResources().getStringArray(R.array.listOfDays)) ;
	// String result = null ;
	//
	// try {
	// result = FileWriter.writeToFile(resetedFile).concat("Removed") ;
	// } catch (IOException e) {
	// result = "Failed" ;
	// }
	// timesListView() ;
	// SharedPrefs.toaster(this,result) ;
	// SharedPrefs.cancelAlarm(this) ;
	// SharedPrefs.setDefaults("NextChange", "No Entries Exist", this) ;
	// }
	public void removeAllEntries() {

		List<List<List<String>>> resetedFile = new LinkedList<List<List<String>>>();

		// List<String> resetedFile =
		// Arrays.asList(getResources().getStringArray(R.array.listOfDays)) ;
		String result = null;

		result = FileWriter.writeToFile(this, resetedFile).concat("Removed");
		FileReader.checkDataExists(this) ;

		timesListView();
		SharedPrefs.toaster(this, result);
		SharedPrefs.cancelAlarm(this);
		SharedPrefs.setDefaults("NextChange", "No Entries Exist", this);
	}

	public void removeDayEntries() {
		final String result = AddorRemove
				.removeDay(this, getSelectedDayAsInt());

		timesListView();
		SharedPrefs.toaster(this, result);
		SharedPrefs.resetAlarm(this);
	}

	public void comfirmDeleteEntries(final String title, final String message,
			final int option, final int position) {
		AlertDialog.Builder confirmAlert = new AlertDialog.Builder(this);

		confirmAlert.setTitle(title);
		confirmAlert.setMessage(message);

		confirmAlert.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if (option == 0) {
							removeSingleEntry(position);
						} else if (option == 1) {
							removeDayEntries();
						} else if (option == 2)
							removeAllEntries();
					}
				});
		confirmAlert.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = confirmAlert.create();
		alert.show();
	}
}