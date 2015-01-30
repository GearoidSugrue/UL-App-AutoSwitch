package com.example.appproject;

import java.util.*;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileWriter {
	// public static String writeToFile(final List<String> allData)
	// throws IOException {
	// try {
	// File root = Environment.getExternalStorageDirectory();
	// File dataFile = new File(root, "ULdataFile.csv");
	//
	// PrintWriter dataPrinter = new PrintWriter(dataFile);
	//
	// for (String str : allData) {
	// dataPrinter.println(str);
	// }
	// dataPrinter.flush();
	// dataPrinter.close();
	//
	// return "Sucessfully ";
	// } catch (FileNotFoundException e) {
	// return "Fail to be ";
	// }
	// }

//	public static String writeToFile(Context context,
//			final List<List<List<String>>> allData) {
//		final String fileName = "uldatafile";
//		FileOutputStream fos;
//		try {
//			fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
//			ObjectOutputStream os = new ObjectOutputStream(fos);
//			// os.writeObject(serializeObject(allData));
//
//			os.writeObject(allData);
//
//			os.flush();
//			os.close();
//
//		} catch (Exception e) {
//			SharedPrefs.toaster(context, "Well this failed...");
//			return "Well this failed...";
//		}
//
//		return "Sucessfully ";
//	}

	public static String writeToFile(Context context,
			final List<List<List<String>>> allData) {

		String fileName = "uldatafile";
		String eol = System.getProperty("line.separator");
		BufferedWriter writer = null;
		int i = 0;
		String entryStr = null;

		List<List<String>> dayData = new LinkedList<List<String>>();

		try {

			writer = new BufferedWriter(new OutputStreamWriter(
					context.openFileOutput(fileName, Context.MODE_PRIVATE)));

			while (i < 7) {
				dayData = allData.get(i);

				if (dayData.isEmpty() == false) {
					for (List<String> entryList : dayData) {
						entryStr = entryList.get(0);
						entryStr.concat(entryList.get(1) + ",");

						writer.write(entryStr);
					}					
				}
				writer.write(eol) ;
				i++ ;
			}
			writer.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
			
			return "This Failed..." ;
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
		}
		return "This Failed..." ;		
	}

	// final String filename = "uldatafile" ;
	//
	// //FileWriter fw = new FileWriter(getApplicationContext());
	// FileOutputStream fos = context.openFileOutput(filename,
	// Context.MODE_PRIVATE);
	//
	// //FileOutputStream fos = FileWriter(filename, Context.MODE_PRIVATE) ;
	//
	// BufferedOutputStream buf = new BufferedOutputStream(fos) ;
	//
	// for(String str:allData) {
	// buf.write(str)
	// }
	//
	// buf.flush() ;
	// fos.close() ;
	//
	// // fos.write(string.getBytes());
	// fos.close();
	//
	// BufferedWriter writer = null;
	// try {
	// writer = new BufferedWriter(new OutputStreamWriter(
	// openFileOutput(filename,
	// Context.MODE_PRIVATE)));
	//
	// for(String str:allData) {
	// writer.append(str);
	// writer.close();
	// }

	// return "Sucessfully " ;
	// } catch (Exception e) {
	// return "Well this failed..." ;
	// } finally {
	// if (writer != null) {
	// try {
	// writer.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// }

	public static List<List<List<String>>> deserializeObject(byte[] b) {
		try {
			ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(b));
			List<List<List<String>>> object = (List<List<List<String>>>) in
					.readObject();
			in.close();

			return object;
		} catch (ClassNotFoundException cnfe) {
			Log.e("deserializeObject", "class not found error", cnfe);

			return null;
		} catch (IOException ioe) {
			Log.e("deserializeObject", "io error", ioe);

			return null;
		}
	}

	public static byte[] serializeObject(final List<List<List<String>>> allData) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(allData);
			out.close();

			// Get the bytes of the serialized object
			byte[] buf = bos.toByteArray();

			return buf;

		} catch (IOException ioe) {
			Log.e("serializeObject", "error", ioe);

			return null;
		}
	}

	// public static String getListAsCsvString(final List<String> list) {
	// StringBuilder sb = new StringBuilder();
	//
	// for (String str : list) {
	// if (sb.length() != 0) {
	// sb.append(",");
	// }
	// sb.append(str);
	// }
	// return sb.toString();
	// }
	//
	// public static List<String> newFullList(final List<String> input) {
	// List<String> allData = new LinkedList<String>();
	// allData.addAll(FileReader.readFromFile());
	// final String modifiedData = getListAsCsvString(input), dayToChange =
	// input
	// .get(0);
	//
	// for (int i = 0; i < allData.size(); i++) {
	// if (allData.get(i).startsWith(dayToChange) == true) {
	// allData.remove(i);
	// allData.add(i, modifiedData);
	// }
	// }
	// return allData;
	// }
}