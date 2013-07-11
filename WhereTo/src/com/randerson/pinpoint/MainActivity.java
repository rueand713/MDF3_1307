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
package com.randerson.pinpoint;

import java.util.HashMap;
import java.util.Set;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.randerson.classes.FileSystem;
import com.randerson.classes.InterfaceManager;
import com.randerson.classes.JSONhandler;
import com.randerson.mapclass.MapService;

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
	String snippet;
	String title;
	HashMap<String, Marker> mapData;
	Messenger intentMessenger;
	GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// create the UI singleton
		UIFactory = new InterfaceManager(this);
		
		// create the map data object for storing the user data
		mapData = new HashMap<String, Marker>();
		
		// get the fragment from the layout file
		MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		
		// get the google map from the map fragment
		map = mf.getMap();
		
		// load the saved markers
		loadMarkers();
		
		// get the calling source intent data
		Intent caller = getIntent();
		Bundle cData = caller.getExtras();
		
		if (cData != null && cData.containsKey("address") == true)
		{
			// get the intent data (string coords)
			//addressData = caller.getExtras().getString("address");
		}
		
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
						
						String latitude = JSONhandler.readJSONObject((String) msg.obj, "lat");
						String longitude = JSONhandler.readJSONObject((String) msg.obj, "lng");
						
						try {
							double lat = Double.parseDouble(latitude);
							double lon = Double.parseDouble(longitude);
							
							// add the map marker
							addMapMarker(lat, lon, 10);
							
							// save the markers to filesystem
							saveMarkers();
							
						} catch (NumberFormatException e) {
							
							Log.e("NumberFormatException", "Parsing geo string results to doubles in handler()");
						}
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
					
					Intent dialog = UIFactory.makeIntent(InputActivity.class);
					
					startActivityForResult(dialog, 0);
				}
			});
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 0 && resultCode == RESULT_OK)
		{
			if (data != null)
			{
				// get the address data
				Bundle bundle = data.getExtras();
				
				if (bundle.containsKey("params"))
				{
					// set the marker address data
					addressData = bundle.getString("params");
					
					if (bundle.containsKey("notes"))
					{
						// set the marker snippet data
						snippet = bundle.getString("notes");
					}
					else
					{
						// set the string empty
						snippet = "";
					}
					
					if (bundle.containsKey("title"))
					{
						// set the marker snippet data
						title = bundle.getString("title");
					}
					else
					{
						// set the string empty
						title = "";
					}
					
					// start the service
					startGeoService();
				}
			}
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
	
	public void addMapMarker(double lat, double lon, float zoom)
	{
		if (title == "" || title == null)
		{
			// give the marker a generic title if no title is supplied
			title = "Marker " + mapData.size() + 1;
		}
		
		// add the marker to the map and capture the marker object
		Marker pin = MapService.addMarker(map, title, snippet, lat, lon);
		
		if (mapData == null)
		{
			mapData = new HashMap<String, Marker>();
		}
		
		// store the marker object in application memory array
		mapData.put(title, pin);
		
		// move to the new marker
		MapService.updatePosition(map, lat, lon, zoom);
	}
	
	@SuppressWarnings("unchecked")
	public void loadMarkers()
	{
		// construct the array
		mapData = new HashMap<String, Marker>();
		
		// load in the map marker data
		mapData = (HashMap<String, Marker>) FileSystem.readObjectFile(this, "Defaults", false);
		
		if (mapData != null)
		{
			String[] keys = new String[mapData.size()];
			int n = 0;
			Set<String> set = mapData.keySet();
			
			while (set.iterator().hasNext())
			{
				keys[n] = (String) set.iterator().next();
			}
			
			for (int i = 0; i < mapData.size(); i++)
			{
				// retrieve the marker object at current index
				Marker pin = (Marker) mapData.get(keys[i]);
				
				// retrieve the latitude and longitude data from the marker
				LatLng coords = pin.getPosition();
				double lat = coords.latitude;
				double lon = coords.longitude;
				
				// retrieve the title and snippet data from the marker
				String pinTitle = pin.getTitle();
				String pinNote = pin.getSnippet();
				
				MapService.addMarker(map, pinTitle, pinNote, lat, lon);
			}
		}
	}
	
	public void saveMarkers()
	{
		// save the map marker data to system
		FileSystem.writeObjectFile(this, mapData, "Defaults", false);
	}

}
