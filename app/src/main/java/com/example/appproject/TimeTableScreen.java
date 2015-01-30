package com.example.appproject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class TimeTableScreen extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_table_screen);		
				
		loadWebsite(false) ;	// false for try load cache.
		
	}
	
	public void loadWebsite(final boolean dontLoadCache) {
		final WebView webview = new WebView(this);
		setContentView(webview);		
		webview.getSettings().setLoadsImagesAutomatically(true);
		if(dontLoadCache == false) {
			webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		} else {		
			webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE) ;
		}
		String website = "http://tt.daniel.ie/tt.php?id=" ;
	 
		Bundle extras = getIntent().getExtras() ;
		if(extras != null) {
			final String idNum = extras.getString("id") ;
			setTitle("UL ID: " + idNum) ;
			website = website.concat(idNum) ;		
		 
		} 					
		webview.loadUrl(website) ;			
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {				
		getMenuInflater().inflate(R.menu.activity_time_table_screen, menu);
		return true;
	}	
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch(item.getItemId()) {
	    case R.id.refresh:
	    	loadWebsite(true) ;	    	
	    return true ;
	      
	    default:
        return super.onOptionsItemSelected(item);
	    }
	}
}