package com.example.appproject;

import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;

public class AddScreen extends Activity {	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_screen);			
		
		TimePicker timePick = (TimePicker) findViewById(R.id.timePicker1);
		timePick.setIs24HourView(false) ;
		
		buttons() ;		
	}	
	
	public void buttons() {		
		Button addEntryBut = (Button) findViewById(R.id.addEntryBut) ;
		
		addEntryBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addEntry() ;				
			}		
	    });		
	}
	
	public String timePickerVal() {		
		TimePicker timePick = (TimePicker) findViewById(R.id.timePicker1);
		timePick.setIs24HourView(false) ;
		
		final String hour = SharedPrefs.checkLength(Integer.toString(timePick.getCurrentHour())),
					  min = SharedPrefs.checkLength(Integer.toString(timePick.getCurrentMinute())) ;
		final String time = hour.concat(min) ;		
		return time ;		
	}		
	
	public int daySpinnerVal() {		
		Spinner dayList = (Spinner) findViewById(R.id.daySpinner) ;		
		final int daySelected = dayList.getSelectedItemPosition() ;
		
		return daySelected ;		
	}
	
//	public String daySpinnerVal() {		
//		Spinner dayList = (Spinner) findViewById(R.id.daySpinner) ;		
//		final String daySelected = String.valueOf(dayList.getSelectedItem()) ;
//		
//		return daySelected ;		
//	}
	
	public String modeSpinnerVal() {		
		Spinner modeList = (Spinner) findViewById(R.id.modeSpinner) ;		
		final String modeSelected = String.valueOf(modeList.getSelectedItem()) ;
		
		return modeSelected ;		
	}	
	
	public void addEntry() {		 			  
		final String mode = modeSpinnerVal(), time = timePickerVal() ;
		String result = null ;
		int day = daySpinnerVal() ;
		
		if(day < 7) 
		{
			result = AddorRemove.addTime(this, day, time, mode).concat("Added") ; //day vals to nums???
		
		} else {
//			int i = 0 ;
//			final List<String> daysOfWeek = Arrays.asList(getResources().getStringArray(R.array.listOfDays)) ;
//			
//			while(i < 7) {
//				day = daysOfWeek.get(i) ;
//				result = AddorRemove.addTime(this, day, time, mode).concat("Added") ;
//				i++ ;
//			}
			int i = 0 ;
			
			while(i < 7) {
				result = AddorRemove.addTime(this, i, time, mode).concat("Added") ;
				i++ ;
			}			
  		}
		
		SharedPrefs.toaster(this, result) ;
		SharedPrefs.resetAlarm(this) ;
	}	
}