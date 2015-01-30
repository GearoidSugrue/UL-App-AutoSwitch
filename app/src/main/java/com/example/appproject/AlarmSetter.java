package com.example.appproject;

import java.util.Calendar;
import java.util.List;
import android.os.Bundle;
import android.content.Context;

public class AlarmSetter {
	public static void intentSender(Context context) {
		Calendar currentCal = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();

		int i = 0,  dayAsInt = cal.get(Calendar.DAY_OF_WEEK);
		final int currentHour = currentCal.get(Calendar.HOUR_OF_DAY), currentMin = currentCal
				.get(Calendar.MINUTE);
		String timeAndMode = null;

		final String currentMinString = Integer.toString(currentMin), currentTime = Integer
				.toString(currentHour).concat(
						SharedPrefs.checkLength(currentMinString));
		boolean repeat = true;

		 timeAndMode = timeToSet(context, SharedPrefs.formatDateAsInt(SharedPrefs.formatDate(cal)), currentTime) ;
		//timeAndMode = timeToSet(context, dayAsInt,currentTime);
		while (repeat && i < 7) {
			if (timeAndMode.equals("NEXT") == true) {
				cal.add(Calendar.DAY_OF_YEAR, 1);
				timeAndMode = timeToSet(context, SharedPrefs.formatDateAsInt(SharedPrefs.formatDate(cal)),
						"-1"); // gets the first entry of that day.
			}
			if (timeAndMode.equals("NEXT") == false) {
				repeat = false;
			}
			i++;
		}
		if (timeAndMode.equals("NEXT") == false) {
			final String hour = timeAndMode.substring(0, 2), min = timeAndMode
					.substring(2, 4), mode = timeAndMode.substring(4);
			final int parsedHour = Integer.parseInt(hour), parsedMin = Integer
					.parseInt(min);
			cal.set(Calendar.HOUR_OF_DAY, parsedHour);
			cal.set(Calendar.MINUTE, parsedMin);
			cal.set(Calendar.SECOND, 0);

			final long timeToAdd = (long) (cal.getTimeInMillis() - currentCal
					.getTimeInMillis());

			Bundle bundle = new Bundle();
			bundle.putString("MODE", mode);
			AlarmSendReceive.sendAlarm(context, bundle, timeToAdd);

			SharedPrefs.setDefaults("NextChange", SharedPrefs.formatDate(cal)
					+ " - " + hour + ":" + SharedPrefs.checkLength(min) + " ("
					+ mode + ")", context);
			// SharedPrefs.toaster(context, "Next Change: " +
			// SharedPrefs.formatDate(cal) + " - " + hour + ":" +
			// SharedPrefs.checkLength(min)) ;
		} else {
			// SharedPrefs.toaster(context, "No Entries Exist") ;
			SharedPrefs.setDefaults("NextChange", "No Entries Exist", context);
		}
	}

	public static String timeToSet(Context context, final int currentDay,
			final String currentTime) {
		String timeAndMode = null;
		List<String> todaysTimes = FileReader.getSpecificDayTimes(context,
				currentDay - 1);
		final int time = Integer.parseInt(currentTime);
		boolean stop = false;

		if (todaysTimes != null) {

			for (int i = 0; i < todaysTimes.size() && stop == false
					&& todaysTimes.size() > 0; i++) {
				final String trimedTime = todaysTimes.get(i).substring(0, 4);
				final int parsedTime = Integer.parseInt(trimedTime);
				if (time < parsedTime) {
					timeAndMode = todaysTimes.get(i);
					stop = true;
				}
			}
		}
			if (!stop) {
				timeAndMode = "NEXT";
			}
			return timeAndMode;
		
		
		
	}

	// public static String timeToSet(Context context, final String currentDay,
	// final String currentTime) {
	// String timeAndMode = null ;
	// List<String> todaysTimes = FileReader.getSpecificDayTimes(context,
	// currentDay) ;
	// final int time = Integer.parseInt(currentTime) ;
	// boolean stop = false ;
	//
	// for(int i = 1; i < todaysTimes.size() && stop == false &&
	// todaysTimes.size() > 1 ; i++) {
	// final String trimedTime = todaysTimes.get(i).substring(0,4) ;
	// final int parsedTime = Integer.parseInt(trimedTime) ;
	// if(time < parsedTime) {
	// timeAndMode = todaysTimes.get(i) ;
	// stop = true ;
	// }
	// }
	// if(!stop) {
	// timeAndMode = "NEXT" ;
	// }
	// return timeAndMode ;
	// }
}
