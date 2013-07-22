/*
 * project 		Pinpoint
 * 
 * package 		com.randerson.pinpoint
 * 
 * @author 		Rueben Anderson
 * 
 * date			Jul 21, 2013
 * 
 */

package com.randerson.pinpoint;

import libs.UniArray;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.randerson.classes.FileSystem;
import com.randerson.classes.InterfaceManager;
import com.randerson.classes.JSONhandler;
import com.randerson.mapclass.MapService;

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
	UniArray mapData;
	Messenger intentMessenger;
	GoogleMap map;
	Marker currentMarker;
	EditText currentField;
	int[] mapType = {MapService.NORMAL, MapService.TERRAIN, MapService.HYBRID, MapService.SATELLITE};
	int currentMapType = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.activity_main);
	
		// create the UI singleton
		UIFactory = new InterfaceManager(this);
		
		// create the map data object for storing the user data
		mapData = new UniArray();
		
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
			// enable the use of user location and set the map type
			map.setMyLocationEnabled(true);
			map.setMapType(mapType[currentMapType]);
			
			// set the marker click listener for the google map
			map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
				
				@Override
				public boolean onMarkerClick(Marker arg0) {
					
					// method for handling the marker clicks
					markerClicked(arg0);
					
					return false;
				}
			});
			
			map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
				
				@Override
				public void onMapClick(LatLng point) {
					
					// remove the update layout view
					toggleLayouts(true);
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
			Button updateBtn = (Button) findViewById(R.id.updateBtn);
			
			// set the click listener for the button
			updateBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// create editText object references to layout
					EditText titleField = (EditText) findViewById(R.id.update_title);
					EditText noteField = (EditText) findViewById(R.id.update_note);
					
					if (titleField != null && noteField != null)
					{
						// string objects for holding the text data of the edit texts
						String header = titleField.getText().toString();
						String note = noteField.getText().toString();
						
						// update the selected marker's title and note
						currentMarker.setTitle(header);
						currentMarker.setSnippet(note);
						
						if (mapData != null)
						{
							// get the marker memory object
							Object[] memStore = (Object[]) mapData.getObject(title);
							
							// remove the old data as the markers are saved using their titles
							// this will prevent null issues
							mapData.removeObject(title);
							
							if (memStore != null)
							{
								// update the marker memory object title and note
								memStore[0] = header;
								memStore[1] = note;
								
								// put the updated marker memory data back into the app memory object
								mapData.putObject(header, memStore);
							}
						}
					}
					
					// toggle the default layout on
					toggleLayouts(true);
					
					// save the updates
					saveMarkers();
				}
			});
			
			// create the button from the layout file
			Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
			
			// set the click listener for the button
			cancelBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// toggle the default layout on
					toggleLayouts(true);
				}
			});
			
			// create the button from the layout file
			Button removeBtn = (Button) findViewById(R.id.removeBtn);
			
			// set the click listener for the button
			removeBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
				
					// get the marker title
					String markerId = currentMarker.getTitle();
					
					// remove the marker from the memory array
					mapData.removeObject(markerId);
					
					// remove the marker
					currentMarker.remove();
					
					// toggle the default layout on
					toggleLayouts(true);
					
					// save the updates
					saveMarkers();
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
		getMenuInflater().inflate(R.layout.main_activity_menu, menu);
		return true;
	}
	
	public void markerClicked(Marker marker)
	{
		// toggle the default layout off
		toggleLayouts(false);
		
		// get the marker title and snippet
		title = marker.getTitle();
		String note = marker.getSnippet();
		
		EditText titleField = (EditText) findViewById(R.id.update_title);
		EditText noteField = (EditText) findViewById(R.id.update_note);
		
		// verify that the fields are properly created
		if (titleField != null && noteField != null)
		{
			// set the field text to the marker corresponding data
			titleField.setText(title);
			noteField.setText(note);
		}
		
		// set reference to the selected marker
		currentMarker = marker;
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
			title = "Marker " + mapData.objectsLength() + 1;
		}
		
		// add the marker to the map and capture the marker object
		Marker pin = MapService.addMarker(map, title, snippet, lat, lon);
		
		if (mapData == null)
		{
			mapData = new UniArray();
		}
		
		// wrap the lat and lon coords in a number object
		Number lat_num = pin.getPosition().latitude;
		Number lon_num = pin.getPosition().longitude;
		
		// creates an object array of the marker data
		Object[] markerDetails = {pin.getTitle(), pin.getSnippet(), lat_num, lon_num};
		
		// store the marker object in application memory array
		mapData.putObject(title, markerDetails);
		
		// move to the new marker
		MapService.updatePosition(map, lat, lon, zoom);
	}
	
	public void loadMarkers()
	{
		// construct the array
		mapData = new UniArray();
		 
		// load in the map marker data
		mapData = (UniArray) FileSystem.readObjectFile(this, "Defaults", true);
		
		if (mapData != null)
		{	
			double lat = 0;
			double lon = 0;
			
			for (int i = 0; i < mapData.objectsLength(); i++)
			{
				// retrieve the marker object at current index
				Object[] pin = (Object[]) mapData.getObject(i);
				
				// retrieve the latitude and longitude data
				Number lat_num = (Number) pin[2];
				Number lon_num = (Number) pin[3];
				
				// retrieve the double values from the number wrappers
				lat = lat_num.doubleValue();
				lon = lon_num.doubleValue();
				
				// retrieve the title and snippet data from the marker
				String pinTitle = (String) pin[0];
				String pinNote = (String) pin[1];
				
				// add the loaded marker data
				MapService.addMarker(map, pinTitle, pinNote, lat, lon);
			}
			
			// move to the last loaded marker
			MapService.updatePosition(map, lat, lon, 10);
		}
	}
	
	public void saveMarkers()
	{
		// save the map marker data to system
		boolean result = FileSystem.writeObjectFile(this, mapData, "Defaults", true);
		
		if (result)
		{
			Log.i("SAVE RESULT", "File written to system successfully");
		}
	}
	
	public void toggleLayouts(boolean showDefaults)
	{
		int update_layout_visibility;
		
		// check whether to show the default view or not
		if (showDefaults)
		{
			update_layout_visibility = View.GONE;
			
			// verify that a marker has been selected
			if (currentMarker != null)
			{
				// dismiss the marker info
				currentMarker.hideInfoWindow();
			}
		}
		else
		{
			update_layout_visibility = View.VISIBLE;
		}
		
		// manipulate the update layout
		ScrollView updateMarkerLayout = (ScrollView) findViewById(R.id.details_b);
		updateMarkerLayout.setVisibility(update_layout_visibility);
		
		// create reference to the text fields
		EditText titleField = (EditText) findViewById(R.id.update_title);
		EditText noteField = (EditText) findViewById(R.id.update_note);
		
		// create the input manager object
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		// determine which if any of the edit texts still have focus
		// if so then the dismiss the keyboard
		if (titleField.hasFocus())
		{
			imm.hideSoftInputFromWindow(titleField.getWindowToken(), 0);
		}
		else
		{
			imm.hideSoftInputFromWindow(noteField.getWindowToken(), 0);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		// grab the text data from the menu item that has been selected
		String title = (String) item.getTitle();
		
		// compare the menu item text to the button strings for appropriate action
		if (title.equals(getString(R.string.quit)))
		{
			// quit the application
			finish();
		}
		else if (title.equals(getString(R.string.about_app)))
		{
			// display the about application screen
		}
		else if (title.equals(getString(R.string.add_button)))
		{
			// create the intent for displaying the add marker activity
			Intent dialog = UIFactory.makeIntent(InputActivity.class);
			
			// start the activity
			startActivityForResult(dialog, 0);
		}
		else if (title.equals(getString(R.string.type)))
		{
			// increment the map type tracker
			currentMapType++;
			
			// check the map type tracker is in range
			if (currentMapType > (mapType.length - 1))
			{
				// if not reset the tracker value
				currentMapType = 0;
			}
			
			// set the map type
			map.setMapType(mapType[currentMapType]);
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
}
