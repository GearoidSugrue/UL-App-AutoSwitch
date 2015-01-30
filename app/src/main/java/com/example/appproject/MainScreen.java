package com.example.appproject;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainScreen extends Activity {	
	
	private String modeForUpdate = null ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);		
		
		
		 List<List<List<String>>> allData = new LinkedList<List<List<String>>>() ;
		 List<List<String>> oneDayData = new LinkedList<List<String>>() ;
		 List<String> oneEntry = new LinkedList<String>() ;
		
		oneEntry.add("1300") ;
		oneEntry.add("Vibrate") ;
		oneDayData.add(oneEntry) ;

		int i = 0 ;
		while(i <7) {
			allData.add(oneDayData) ;
			i++ ;
		}
		
		//FileWriter.writeToFile(this, allData) ;
		
		//checkIfDataExists() ;
//		List<List<List<String>>> allData = new LinkedList<List<List<String>>>() ;
//		int i = 0 ;
//		while(i < 7) {
//			allData.add(null) ;
//			FileWriter.writeToFile(this, allData) ;
//		}		
//		SharedPrefs.toaster(this, "Data File Created") ;
		
		//FileReader.checkDataExists(this) ;
		textBoxDefault() ;
		buttons() ;			
		toggleButtons() ;	
		PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
		
		
		FileWriter.writeToFile(this, allData) ; //remove this........
		
	}	
	
	public void setModeForUpdate(final String mode) {
		this.modeForUpdate = mode ;
	}
	
	public String getModeForUpdate() {
		return this.modeForUpdate ;
	}
	
	public String getIdNum() {
		EditText idNumber = (EditText) findViewById(R.id.idEntry) ;
		final String id = idNumber.getText().toString() ;		
		return id ;
	}
	
	public void toggleButtons() {		
		ToggleButton toggleAppOn= (ToggleButton) findViewById(R.id.toggleButtonAppOn);
		
		final boolean isOn = SharedPrefs.getBooleanDefaults("isAppOn", this) ;		
		if (isOn == true) {    
			toggleAppOn.setChecked(true) ;
			if(isAnAlarmSet() != true) { 
				AlarmSetter.intentSender(this) ;
			}
		} else {	
			toggleAppOn.setChecked(false) ;
		}
		toggleAppOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked) {	
		        	SharedPrefs.setDefaults("isAppOn", true, getApplicationContext()) ;		        	
		        	AlarmSetter.intentSender(getApplicationContext()) ;			        	
		        } else {		
		        	SharedPrefs.setDefaults("isAppOn", false, getApplicationContext()) ;		        	
		        	SharedPrefs.cancelAlarm(getApplicationContext()) ;
		        	SharedPrefs.toaster(getApplicationContext(), "App OFF") ;		        	
		        }
		        setNextChange() ;
		    }
		});
	}
	
	public void textBoxDefault() {
		final String idNum = SharedPrefs.getStringDefaults("defaultIdNum", this) ;
		if(!idNum.equalsIgnoreCase("Normal")) {
			EditText idNumber = (EditText) findViewById(R.id.idEntry) ;
			idNumber.setText(idNum) ;
		}
	}
	
	public void setNextChange() {
		TextView textBox = (TextView) findViewById(R.id.textNextChange) ;		
		final String nextChange = SharedPrefs.getStringDefaults("NextChange", this) ;
		
			if(SharedPrefs.getBooleanDefaults("isAppOn", this) && nextChange != null) {	
				textBox.setText(nextChange) ;
			} else {
				textBox.setText("") ;
			}		
	}
	
	public void buttons() {
		Button removeButton = (Button) findViewById(R.id.removeButton) ;
		Button timeTableBut = (Button) findViewById(R.id.timeTableBut) ;
		ImageButton aboutButton = (ImageButton) findViewById(R.id.imageinfoBut) ;
		Button addButton = (Button) findViewById(R.id.addButton) ;
		Button updateTimes = (Button) findViewById(R.id.updateButton) ;
		
		removeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removeScreenActStart() ;
			}			
		});			
		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addScreenActStart() ;
			}			
		});			
		aboutButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				aboutScreenActStart() ;
			}			
		});			
		timeTableBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String id = getIdNum() ;
				if(id.length() > 4) {
					timeTableActStart(id) ;
				} else {
					SharedPrefs.toaster(getApplicationContext(), "Enter UL Id") ;
				}
			}			
		});	
		
		updateTimes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(SharedPrefs.isOnline(getApplicationContext())){				
					if(getIdNum().length() > 4) {
						updateTimes();
					} else {
						SharedPrefs.toaster(getApplicationContext(), "Enter UL Id") ;
					}
				} else {
					SharedPrefs.toaster(getApplicationContext(), "Interent Access Required") ;
				}
			}			
		});		
	}
	
	public void addScreenActStart()
	{		
		Intent intent = new Intent(this,AddScreen.class);
		startActivity(intent);		
	}	
		
	public void removeScreenActStart()
	{
		Intent intent = new Intent(this,RemoveScreen.class);
		startActivity(intent);	
	}
	
	public void timeTableActStart(String id) {		
		SharedPrefs.setDefaults("defaultIdNum", id, this) ;		
		Intent intent = new Intent(this,TimeTableScreen.class);
		intent.putExtra("id",id);
		startActivity(intent);	
	}
	
	public void aboutScreenActStart() {
		Intent intent = new Intent(this,AboutScreen.class);
		startActivity(intent);	
	}
	
	public void settingsScreenActStart() {
		Intent intent = new Intent(this,SettingsActivity.class);
		startActivity(intent);	
	}
	
	public void updateTimes() {
		final String idNum = getIdNum() ;
						
			AlertDialog.Builder confirmAlert = new AlertDialog.Builder(this);
			confirmAlert.setTitle("Mode to switch to before Lecs/Labs/Tuts:") ;
				
			final String[] items = {"Normal", "Vibrate", "Mute"};        
			setModeForUpdate(items[1]) ;
	
			confirmAlert.setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					setModeForUpdate(items[item]) ;
				}
			}); 				
			confirmAlert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {							
					ICalParser.download(getApplicationContext(), getModeForUpdate(), idNum) ;	
					SharedPrefs.setDefaults("defaultIdNum", idNum, getApplicationContext()) ;
				}
			}) ;
			confirmAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			AlertDialog alert = confirmAlert.create();
			alert.show() ;				
	}
	
	public boolean isAnAlarmSet() {
		boolean result = true ;
		Intent intent = new Intent(this, AlarmSendReceive.class);
    	if(PendingIntent.getBroadcast(this, 1231234, intent, PendingIntent.FLAG_NO_CREATE) == null) {
    		result = false;
    	}    	
    	return result ;
	}
	
//	public void checkIfDataExists() {		
//		File root = Environment.getExternalStorageDirectory();
//        File dataFile = new File(root, "ULdataFile.csv") ;  
//		if(!dataFile.exists()) {
//			try {
//				FileWriter.writeToFile(Arrays.asList(getResources().getStringArray(R.array.listOfDays))) ;
//			} catch (NotFoundException e) {
//				// TODO Auto-generated catch block
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//			}
//		} else {
//			List<String> data = FileReader.readFromFile() ;			
//			
//			if(data.size() == 0) {
//				try {
//					FileWriter.writeToFile(Arrays.asList(getResources().getStringArray(R.array.listOfDays))) ;
//					} catch (NotFoundException e) {
//						// TODO Auto-generated catch block					
//					} catch (IOException e) {
//						// TODO Auto-generated catch block						
//					}
//			}
//		}			
//	}
	
//	public void checkIfDataExists() {		
//		File root = Environment.getExternalStorageDirectory();
//        File dataFile = new File(root, "ULdataFile.csv") ;  
//		if(!dataFile.exists()) {
//			try {
//				FileWriter.writeToFile(Arrays.asList(getResources().getStringArray(R.array.listOfDays))) ;
//			} catch (NotFoundException e) {
//				// TODO Auto-generated catch block
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//			}
//		} else {
//			List<String> data = FileReader.readFromFile() ;			
//			
//			if(data.size() == 0) {
//				try {
//					FileWriter.writeToFile(Arrays.asList(getResources().getStringArray(R.array.listOfDays))) ;
//					} catch (NotFoundException e) {
//						// TODO Auto-generated catch block					
//					} catch (IOException e) {
//						// TODO Auto-generated catch block						
//					}
//			}
//		}			
//	}
	
	public void onResume()
	{
	         super.onResume();
	         setNextChange() ;
	 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {				
		getMenuInflater().inflate(R.menu.activity_main_screen, menu);
		return true;
	}	
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch(item.getItemId()) {
	    case R.id.settings:
	    	settingsScreenActStart() ;
	    return true ;
	      
	    default:
        return super.onOptionsItemSelected(item);
	    }
	}
}		