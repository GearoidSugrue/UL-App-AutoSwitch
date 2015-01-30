package com.example.appproject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import android.content.Context;
import android.os.Handler;

public class ICalParser {

	public static String parser(Context context, final String modeSelected,
			final String idNum) {

		try {
			File cacheDir = context.getCacheDir();
			File dataFile = new File(cacheDir, idNum.concat(".ics"));

			List<String> times = new LinkedList<String>();

			Scanner input = new Scanner(dataFile);
			final String end = "END", startTimeMark = "DTSTART", endTimeMark = "DTEND";

			while (input.hasNextLine()) {
				final String lineFromFile = input.nextLine();
				if (lineFromFile.startsWith(startTimeMark) == true) {
					times.add(lineFromFile.substring(27));
				} else if (lineFromFile.startsWith(endTimeMark) == true) {
					times.add(lineFromFile.substring(25).concat(end));
				}
			}

			input.close();
			final String result = addEntrys(context, times, modeSelected);

			return result;
		} catch (IOException x) {
			return "Failed to Find File";
		}
	}

	public static String addEntrys(Context context, List<String> times,
			final String modeSelected) {
		final int timesSize = times.size();
		String result = null, mode = null;
		final String end = "END";
		for (int i = 0; i < timesSize; i++) {
			boolean addEnd = false;
			final String unformattedTime = times.get(i);
			final boolean isEndTime = unformattedTime.endsWith(end);

			if (!isEndTime) {
				mode = modeSelected;
			} else {
				mode = SharedPrefs.getStringDefaults("EndMode", context);
			}

			Calendar cal = getDate(unformattedTime);

			// final String dayOfWeek = SharedPrefs.formatDate(cal) ;
			int hour = cal.get(Calendar.HOUR_OF_DAY), min = cal
					.get(Calendar.MINUTE);
			final int dayOfWeekInt = cal.get(Calendar.DAY_OF_WEEK);

			final String time = calculateEndTime(context, true, isEndTime,
					hour, min);

			result = AddorRemove.addTime(context, dayOfWeekInt, time, mode);

			if (i + 1 < timesSize) {
				if (times.get(i + 1).endsWith(end) == false && !isEndTime) {
					addEnd = true;
				}
			} else if (!isEndTime) {
				addEnd = true;
			}
			if (addEnd == true) {
				final String endTime = calculateEndTime(context, false,
						isEndTime, hour, min);
				result = AddorRemove.addTime(context, dayOfWeekInt, endTime,
						SharedPrefs.getStringDefaults("EndMode", context));
			}
		}
		return result;
	}

	public static String calculateEndTime(Context context, boolean isStartTime,
			boolean isEndTime, int hour, int min) {
		int minsAfterStart = SharedPrefs.getIntDefaults("Mins", context);

		if (!isStartTime) {
			if (minsAfterStart >= 10) {
				hour++;
				min = minsAfterStart - 10;
			} else {
				min = minsAfterStart + 50;
			}
		} else if (isEndTime) {
			if (minsAfterStart >= 10) {
				min = minsAfterStart - 10;
			} else {
				hour--;
				min = minsAfterStart + 50;
			}
		}
		final String parsedHour = SharedPrefs.checkLength(Integer
				.toString(hour)), parsedmin = SharedPrefs.checkLength(Integer
				.toString(min));
		final String time = parsedHour.concat(parsedmin);

		return time;
	}

	public static Calendar getDate(String icalStr) {
		final String end = "END";
		if (icalStr.endsWith(end) == true) {
			icalStr = icalStr.substring(0, 15);
		}
		icalStr = icalStr.replaceAll("T", "");
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		try {
			cal.setTime(sdf.parse(icalStr));
		} catch (ParseException e) {
			final String strYear = icalStr.substring(0, 4), strMonth = icalStr
					.substring(4, 6), strDay = icalStr.substring(6, 8), strHour = icalStr
					.substring(9, 11), strMin = icalStr.substring(11, 13);
			cal.set(Integer.parseInt(strYear), Integer.parseInt(strMonth),
					Integer.parseInt(strDay), Integer.parseInt(strHour),
					Integer.parseInt(strMin));
		}
		return cal;
	}

	public static void download(final Context context,
			final String modeSelected, final String idNum) {

		final Handler handle = new Handler();
		new Thread(new Runnable() {
			public void run() {
				File cacheDir = context.getCacheDir();
				File file = new File(cacheDir, idNum.concat(".ics"));

				try {
					URL icalUrl = new URL("http://tt.daniel.ie/tt.ical.php?id="
							.concat(idNum));
					InputStream in = new BufferedInputStream(icalUrl
							.openStream());

					Scanner scanData = new Scanner(in);
					PrintWriter dataPrinter = new PrintWriter(file);
					while (scanData.hasNext()) {
						dataPrinter.println(scanData.nextLine());
					}

					dataPrinter.flush();
					dataPrinter.close();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}

				handle.post(new Runnable() {
					public void run() {
						ICalParser.parser(context, modeSelected, idNum);
						SharedPrefs.resetAlarm(context);
					}
				});

			}
		}).start();
	}
}