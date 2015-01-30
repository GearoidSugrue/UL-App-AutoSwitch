package com.example.appproject;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.*;

import android.content.Context;
import android.os.Environment;

public class FileReader {
	// public static List<String> readFromFile() {
	// try {
	// File root = Environment.getExternalStorageDirectory();
	// File dataFile = new File(root, "ULdataFile.csv");
	//
	// List<String> data = new LinkedList<String>();
	//
	// Scanner input = new Scanner(dataFile);
	// while (input.hasNextLine()) {
	// final String lineFromFile = input.nextLine();
	// data.add(lineFromFile);
	// }
	// input.close();
	// return data;
	// } catch (IOException x) {
	// return null;
	// }
	// }

//	public static List<List<List<String>>> readFromFile(Context context) {
//		final String fileName = "uldatafile";
//		List<List<List<String>>> allData = new LinkedList<List<List<String>>>();
//
//		try {
//			FileInputStream fis = context.openFileInput(fileName);
//			ObjectInputStream is = new ObjectInputStream(fis);
//
//			// ByteArrayInputStream bIs = new ByteArrayInputStream(is) ;
//
//			// byte[] bt = (byte[]) is.readObject() ;
//
//			allData = (List<List<List<String>>>) is.readObject();
//			is.close();
//
//			// allData = FileWriter.deserializeObject(bt) ;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return allData;
//		}
//		return allData;
//	}

	public static List<List<List<String>>> readFromFile(
			Context context) {
		final String fileName = "uldatafile";

		String eol = System.getProperty("line.separator");
		BufferedReader input = null;

		List<String> oneEntryList = new LinkedList<String>();
		List<List<String>> dayData = new LinkedList<List<String>>();
		List<List<List<String>>> allData = new LinkedList<List<List<String>>>();

		String entryStr = null;

		try {
			input = new BufferedReader(new InputStreamReader(
					context.openFileInput(fileName)));
			String line;
			StringBuffer buffer = new StringBuffer();
			while ((line = input.readLine()) != null) {

				StringTokenizer strTkn = new StringTokenizer(line, ",");
				while (strTkn.hasMoreTokens()) {

					entryStr = strTkn.toString();
					oneEntryList.add(entryStr.substring(0, 3));
					oneEntryList.add(entryStr.substring(4));

					dayData.add(oneEntryList);
					oneEntryList.clear();
					strTkn.nextToken();
				}
				allData.add(dayData);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return allData ;
	}

	public static List<String> getSpecificDayTimes(Context context,
			final int dayAsInt) {

		final List<List<List<String>>> allData = readFromFile(context);
		List<String> oneDayTimes = new LinkedList<String>();

		if (allData.size() > dayAsInt
				&& allData.get(dayAsInt).isEmpty() == false) {

			List<List<String>> oneDayAllData = allData.get(dayAsInt);

			for (List<String> list : oneDayAllData) {
				oneDayTimes.add(list.get(0));
			}

			return oneDayTimes;
		} else {
			return oneDayTimes;
		}

		// String fullDay = null;
		//
		// for (String str : allData) {
		// if (str.startsWith(dayRequested)) {
		// fullDay = str;
		// }
		// }
		//
		// StringTokenizer StrTkn = new StringTokenizer(fullDay, ",");
		// while (StrTkn.hasMoreTokens()) {
		// oneDayData.add(StrTkn.nextToken());
		// }
		// return oneDayData;
	}

	public static List<List<String>> getSpecificDayDataSplit(Context context,
			final int dayAsInt) {

		final List<List<List<String>>> allData = readFromFile(context);
		List<List<String>> oneDayAllData = allData.get(dayAsInt);

		List<String> oneDayTimes = new LinkedList<String>();
		List<String> oneDayModes = new LinkedList<String>();
		List<List<String>> oneDaySplitData = new LinkedList<List<String>>();

		for (List<String> list : oneDayAllData) {
			oneDayTimes.add(list.get(0));
			oneDayModes.add(list.get(1));
		}
		oneDaySplitData.add(oneDayTimes);
		oneDaySplitData.add(oneDayModes);

		return oneDaySplitData;
	}

	public static List<List<String>> getSpecificDayData(Context context,
			final int dayAsInt) {

		final List<List<List<String>>> allData = readFromFile(context);
		List<List<String>> oneDayData = allData.get(dayAsInt);

		return oneDayData;
	}

	public static int dayAsInt(String day) {
		int dayInt = 0;

		if (day.equalsIgnoreCase("Tuesday") == true) {
			dayInt = 1;
		} else if (day.equalsIgnoreCase("Wednesday") == true) {
			dayInt = 2;
		} else if (day.equalsIgnoreCase("Thursday") == true) {
			dayInt = 3;
		} else if (day.equalsIgnoreCase("Friday") == true) {
			dayInt = 4;
		} else if (day.equalsIgnoreCase("Saturday") == true) {
			dayInt = 5;
		} else if (day.equalsIgnoreCase("Sunday") == true) {
			dayInt = 6;
		}
		return dayInt;
	}

	public static void checkDataExists(Context context) {

		final String fileName = "uldatafile";
		List<List<List<String>>> allData = new LinkedList<List<List<String>>>();

		boolean isCreated = true;

		try {
			FileInputStream fis = context.openFileInput(fileName);
			ObjectInputStream is = new ObjectInputStream(fis);

			byte[] bt = (byte[]) is.readObject();

			// allData = (List<List<List<String>>>) is.readObject();
			is.close();
			fis.close();
			allData = FileWriter.deserializeObject(bt);

			// allData = (List<List<List<String>>>) is.readObject();

			if (allData.size() < 7) {
				isCreated = false;
			}

		} catch (Exception e) {
			isCreated = false;
		}

		if (!isCreated) {
			int i = 0;
			while (i < 7) {
				allData.add(null);
				FileWriter.writeToFile(context, allData);
			}
			SharedPrefs.toaster(context, "Data File Created");
		}
	}

	// public static List<String> getSpecificDayTimes(final String dayRequested)
	// {
	// final List<String> allData = readFromFile();
	// List<String> oneDayData = new LinkedList<String>();
	//
	// String fullDay = null;
	//
	// for (String str : allData) {
	// if (str.startsWith(dayRequested)) {
	// fullDay = str;
	// }
	// }
	//
	// StringTokenizer StrTkn = new StringTokenizer(fullDay, ",");
	// while (StrTkn.hasMoreTokens()) {
	// oneDayData.add(StrTkn.nextToken());
	// }
	// return oneDayData;
	// }
}