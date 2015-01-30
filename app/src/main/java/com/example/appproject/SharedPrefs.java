package com.example.appproject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class SharedPrefs {
	public static <T> void setDefaults(final String key, final T value, Context context) {
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
	    SharedPreferences.Editor editor = prefs.edit();
	    
	    if(value.getClass().equals(String.class)) {
	        editor.putString(key, (String)value); 
	      } else if (value.getClass().equals(Boolean.class)) {
	        editor.putBoolean(key, (Boolean)value); 
	      }	 else if (value.getClass().equals(Integer.class)) {
	    	  editor.putInt(key, (Integer)value) ;
	      }
	    editor.commit();
	}

	public static boolean getBooleanDefaults(final String key, Context context) {
	    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);	    	    
	    return preferences.getBoolean(key, false) ;
	}
	
	public static String getStringDefaults(final String key, Context context) {
	    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);	    	    
	    return preferences.getString(key, "Normal") ;
	}	
	
	public static int getIntDefaults(final String key, Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);	    	    
	    return preferences.getInt(key, 0) ;		
	}
	
	public static void resetAlarm(Context context) {
		final boolean isOn = getBooleanDefaults("isAppOn", context) ;				
	    if (isOn == true) {    
			AlarmSetter.intentSender(context) ;
		}
	}
	
	public static boolean isOnline(Context context) {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {
	        return true;
	    }
	    return false;
	}
	
	public static String checkLength(String str) {			
		if(str.length() < 2) {
			final String zero = "0" ;
			str = zero.concat(str) ;
		}
		return str ;
	}
	
	public static void toaster(Context context, final String toToast) {
		Toast toast = Toast.makeText(context, toToast, Toast.LENGTH_SHORT);
		toast.show() ;
	}
	
	public static void cancelAlarm(Context context) {
		Intent intent = new Intent(context, AlarmSendReceive.class);
    	PendingIntent.getBroadcast(context, 1231234, intent, PendingIntent.FLAG_UPDATE_CURRENT).cancel();
	}	
	
	public static String formatDate(Calendar cal) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE") ;
		final String day = dateFormat.format(cal.getTime()) ;
		
		//final int dayTE = cal.get(Calendar.DAY_OF_WEEK) ;
		return day ;
	}	
	
	public static int formatDateAsInt(String day) {
		
		int dayAsInt = 0 ;
		
		if(day.equalsIgnoreCase("Tuesday") == true) {
			dayAsInt = 1 ;
		} else if(day.equalsIgnoreCase("Wednesday") == true) {
			dayAsInt = 2 ;
		} else if(day.equalsIgnoreCase("Thursday") == true) {
			dayAsInt = 3 ;
		} else if(day.equalsIgnoreCase("Friday") == true) {
			dayAsInt = 4 ;
		} else if(day.equalsIgnoreCase("Saturday") == true) {
			dayAsInt = 5 ;
		} else if(day.equalsIgnoreCase("Sunday") == true) {
			dayAsInt = 6 ;
		}
		
		return dayAsInt ;
		
	}
	
//	public static String formatDate(Calendar cal) {
//		SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE") ;
//		final String day = dateFormat.format(cal.getTime()) ;
//		
//		return day ;
//	}	
}