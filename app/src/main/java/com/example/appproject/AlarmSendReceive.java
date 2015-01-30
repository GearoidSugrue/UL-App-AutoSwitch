package com.example.appproject;

import java.util.Calendar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;

public class AlarmSendReceive extends BroadcastReceiver {  	 
	
     public static void sendAlarm(Context context, Bundle extras, final long timeoutInMillis){
         AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
         
         Intent intent = new Intent(context, AlarmSendReceive.class);
         intent.putExtra("BUNDLE", extras);
         PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1231234, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        
         Calendar time = Calendar.getInstance();
         time.setTimeInMillis(System.currentTimeMillis() + timeoutInMillis);         
         
         alarmMgr.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
     }     
     
      @Override
     public void onReceive(Context context, Intent intent) {
    	  if(intent.hasExtra("BUNDLE")) { 
    		 Bundle bundle = intent.getBundleExtra("BUNDLE");    	 
    		 final String mode = bundle.get("MODE").toString() ;    	 
         
    		 AudioManager audioManage = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

    		 if(mode.startsWith("V")) {
    			 audioManage.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    		 } else if(mode.startsWith("N")){
    			 audioManage.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    		 } else {
    			 audioManage.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    		 }         
    		 SharedPrefs.toaster(context, "Mode Switched - " + mode) ;
    	  }          	  
    	  SharedPrefs.resetAlarm(context) ;
     }
}      