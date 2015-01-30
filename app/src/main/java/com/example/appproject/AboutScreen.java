package com.example.appproject;

import java.util.Arrays;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class AboutScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_screen);		
		
		Button backButton = (Button) findViewById(R.id.backButton1) ;
		
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish() ;
			}			
		});	
		
		infoList() ;
	}	
	
	public void infoList() {
		
		ListView list = (ListView) findViewById(R.id.listInfoView) ;
		List<String> info = Arrays.asList(getResources().getStringArray(R.array.infoList)) ;		
	
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text1,info) ;
		list.setAdapter(adapter) ;		
	}

}
