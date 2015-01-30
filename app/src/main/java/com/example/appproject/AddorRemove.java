package com.example.appproject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;

public class AddorRemove {
	public static String removeTime(Context context, final int dayAsInt,
			final int position) {
		String result = null;

		List<List<List<String>>> allData = FileReader.readFromFile(context);
		List<List<String>> dayTimes = allData.get(dayAsInt);

		dayTimes.remove(position);
		allData.set(dayAsInt, dayTimes);

		result = FileWriter.writeToFile(context, allData);

		return result;
	}

	// public static String removeDay(final String day)
	// {
	// String result = null ;
	// List<String> clearedDay = new LinkedList<String>() ;
	// clearedDay.add(day) ;
	//
	// try {
	// result =
	// FileWriter.writeToFile(FileWriter.newFullList(clearedDay)).concat("Removed")
	// ;
	// } catch (IOException e) {
	// result = "Failed" ;
	// }
	// return result ;
	// }

	public static String removeDay(Context context, final int dayAsInt) {
		String result = null;
		List<List<List<String>>> allData = FileReader.readFromFile(context);

		allData.set(dayAsInt, null);

		result = FileWriter.writeToFile(context, allData);

		return result;
	}

	public static String addTime(Context context, final int dayAsInt,
			final String newTime, final String mode) {
		
		String result = null;
		int i = 0, time = 0;
		final int newTimeInt = Integer.parseInt(newTime);
		boolean stop = false;

		List<List<List<String>>> allData = new LinkedList<List<List<String>>>();
		
		if(FileReader.readFromFile(context).isEmpty() == false ) {
			allData = FileReader.readFromFile(context) ;
		}
		
		List<List<String>> dayTimes = new LinkedList<List<String>>() ;
		
		if(allData.size() > dayAsInt) {
			dayTimes = allData.get(dayAsInt);
		}
		List<String> entry = new LinkedList<String>();

		List<String> newEntry = new LinkedList<String>();
		newEntry.add(newTime);
		newEntry.add(mode);

		for (i = 0; stop == false && i < dayTimes.size(); i++) {
			entry = dayTimes.get(i);
			time = Integer.parseInt(entry.get(0));

			if (newTimeInt < time) {
				dayTimes.add(i, newEntry);
				stop = true;
			} else if (newTimeInt == time) {
				dayTimes.set(i, newEntry);
				stop = true;
			}
		}
		if (stop == false) {
			dayTimes.add(newEntry);
		}
		
		
		while(i < 7) {
			allData.add(null) ;
		}	
		
		
		allData.set(dayAsInt, dayTimes) ;		
		result = FileWriter.writeToFile(context, allData);
		
		return result;
	}
	//
	// public static String addTime(final String day, final String time, final
	// String mode)
	// {
	// String result = null ;
	// final String timeToInsert = time.concat(mode) ;
	// int i = 0 ;
	// final int timeInt = Integer.parseInt(time) ;
	// boolean stop = false ;
	//
	// List<String> dayTimes = new
	// LinkedList<String>(FileReader.getSpecificDayTimes(day)) ;
	//
	// for(i = 1; stop == false && i < dayTimes.size(); i++) {
	// final String trimedTime = dayTimes.get(i).substring(0,4) ;
	//
	// final int parsedTime = Integer.parseInt(trimedTime) ;
	//
	// if(timeInt < parsedTime) {
	// dayTimes.add(i, timeToInsert) ;
	// stop = true ;
	// } else if(timeInt == parsedTime) {
	// dayTimes.remove(i) ;
	// dayTimes.add(i, timeToInsert) ;
	// stop = true ;
	// }
	// }
	// if(stop == false) {
	// dayTimes.add(timeToInsert) ;
	// }
	//
	// try {
	// result = FileWriter.writeToFile(FileWriter.newFullList(dayTimes)) ;
	// } catch (IOException e) {
	// result = "Failure" ;
	// }
	// return result ;
	// }
}