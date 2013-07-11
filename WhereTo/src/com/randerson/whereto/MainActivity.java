/*
 * project 		WhereTo
 * 
 * package 		com.randerson.whereto
 * 
 * @author 		Rueben Anderson
 * 
 * date			Jul 11, 2013
 * 
 */
package com.randerson.whereto;

import libs.UniArray;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;
import com.randerson.classes.InterfaceManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {

	final String BASE_GEO_CODE_URL = "http://maps.googleapis.com/maps/api/geocode/json?address=";
	final String MESSENGER_KEY = "Messenger";
	final String URL_KEY = "Url";
	
	final int GEO_CODE_DATA = 1;
	
	InterfaceManager UIFactory;
	String addressData;
	UniArray mapData;
	Messenger intentMessenger;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// get the calling source intent data
		Intent caller = getIntent();
		
		// create a new string object
		addressData = new String();
		
		if (caller != null)
		{
			// get the intent data (string coords)
			addressData = caller.getExtras().getString("address");
		}
		
		// create the UI singleton
		UIFactory = new InterfaceManager(this);
		
		// create the map data object for storing the user data
		mapData = new UniArray();
		
		// get the fragment from the layout file
		MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		
		// get the google map from the map fragment
		GoogleMap map = mf.getMap();
		
		// verify that the map is created properly
		if (map != null)
		{
			// set the marker click listener for the google map
			map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
				
				public boolean onMarkerClick(Marker arg0) {
					
					// method for handling the marker clicks
					markerClicked(arg0);
					
					return false;
				}
			});
		
			// create the handler object
			Handler requestHandler = new Handler() 
			{	
				@Override
				public void handleMessage(Message msg) {
					
					super.handleMessage(msg);
					
					if (msg.arg1 == RESULT_OK && msg.obj != null)
					{
						// received the geo coded address data
						Log.i("GEO RESULTS", msg.obj.toString());
					}
				}
			};
			
			// create the messenger object
			intentMessenger = new Messenger(requestHandler);
			
			// ************ CALLING INTENT HANDLING **************
			
			if (addressData != null)
			{
				startGeoService();
			}
			
			// ***************************************************
			
			// create the button from the layout file
			Button addBtn = (Button) findViewById(R.id.addDataBtn);
			
			// set the click listener for the button
			addBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// start the service
					startGeoService();
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void markerClicked(Marker marker)
	{
		
	}
	
	// method for fetching the geolocational lat lon values from a string address
	public void startGeoService()
	{
		String url = BASE_GEO_CODE_URL + addressData + "&sensor=true";
		
		// create the service intent object and pass in the required extras
		Intent thisService = UIFactory.makeIntent(DetailService.class);
		thisService.putExtra(URL_KEY, url).putExtra(MESSENGER_KEY, intentMessenger);
		
		// start the service
		startService(thisService);
	}

}
