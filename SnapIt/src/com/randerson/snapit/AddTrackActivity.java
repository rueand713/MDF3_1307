package com.randerson.snapit;

import java.util.HashMap;

import com.randerson.classes.FileSystem;

import libs.UniArray;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class AddTrackActivity extends Activity {

	// create the playlist object
	String playlist;
	
	// set the memory object
	HashMap<String, UniArray> memory;
	
	// create the success bool
	boolean finished = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addtracks);
		
		// set the playlist object null
		playlist = null;
		
		// create the intent for the calling activity data
		Intent receivedData = getIntent();
		
		// verify that the intent is not null
		if (receivedData != null)
		{
			// create the options bundle from the calling intent
			Bundle options = receivedData.getExtras();
			
			// verify that the options and key are valid
			if (options != null && options.containsKey("playlist"))
			{
				// get the passed in playlist name
				playlist = options.getString("playlist");
			}
		}
		
		// load up any saved data
		loadPlaylist();
		
		// method for populating the list
		populateList();
		
		// verify that the memory object is valid
		if (memory == null)
		{
			// instantiate the memory object
			memory = new HashMap<String, UniArray>();
		}
		
		// create the addtrack button
		Button addTrack = (Button) findViewById(R.id.add_new_track);
		
		// set the click listener for the addTrack button
		addTrack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText artistField = (EditText) findViewById(R.id.artist_name);
				EditText trackField = (EditText) findViewById(R.id.track_name);
				EditText urlField = (EditText) findViewById(R.id.track_url);
				
				// verify that the text field objects are valid
				if (artistField != null && trackField != null)
				{
					// retrieve the string data from the text field objects
					String artist = artistField.getText().toString();
					String track = trackField.getText().toString();
					String url = urlField.getText().toString();
					
					// verify that the url is not empty nor null
					if (url != null && url.equals("") == false)
					{	
						// verify that the artist and track strings are not null
						if (artist != null && track != null)
						{
							String emptyTest = (artist + track).replace(" ", "");
							
							// verify that the artist or track field are not empty
							if (emptyTest.equals("") == false)
							{
								// call the method to add the track to the playlist
								addPlaylistTrack(playlist, artist, track, url);
							}
						}
					}
				}
			}
		});
		
		// create the done button
		Button doneBtn = (Button) findViewById(R.id.done_btn);
		
		// set the click listener for the done button
		doneBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// call method to end the activity
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	public void addPlaylistTrack(String playlist, String artist, String trackname, String url)
	{
		if (memory != null)
		{	
			UniArray playlistData = memory.get(playlist);
			
			// set the track information into the strings object
			String[] trackData = {artist, trackname, url};
			
			// put the string array of data into the playlist data array
			playlistData.putObject((artist + trackname), trackData);
			
			// store the track uniarray of data into the memory hashmap 
			memory.put(playlist, playlistData);
			
			// set the success bool
			finished = true;
		}
	}
	
	@Override
	public void finish() {
		if (finished == true)
		{
			// create the data intent to return
			Intent data = new Intent();
			
			// add the playlist to the data
			data.putExtra("playlist", playlist);
			
			// set the result and data to return
			setResult(RESULT_OK, data);
			
			// save the updated playlist
			savePlaylist();
		}
		
		super.finish();
	}

	public void populateList()
	{
		// create the table for the playlist data
		TableLayout trackTable = (TableLayout) findViewById(R.id.list);
		
		// empty any lingering views
		trackTable.removeAllViews();
		
		if (playlist != null) 
		{
			// retrieve the uniarray of track data from the memor object
			UniArray playlistData = memory.get(playlist);
			
			// verify that the uniarray is valid
			if (playlistData != null)
			{
				int color;
				
				// iterate over the uniarray creating the list of tracks
				for (int i = 0; i < playlistData.objectsLength(); i++)
				{
					if (i % 2 > 0)
					{
						color = getResources().getColor(android.R.color.primary_text_dark);
					}
					else
					{
						color = getResources().getColor(android.R.color.secondary_text_light);
					}
					
					// retrieve the string array from the playlist data
					String[] currentTrack = (String[]) playlistData.getObject(i);
					
					// retrieve the individual string components (artist, track, url)
					String trackName = currentTrack[1];
					String artistName = currentTrack[0];
					//String trackUrl = currentTrack[2];
					
					// create the table's child objects
					TableRow row = new TableRow(this);
					TextView track = new TextView(this);
					TextView artist = new TextView(this);
					//TextView url = new TextView(this);
					
					// set the ui data
					track.setText(trackName);
					track.setTextColor(color);
					track.setPadding(1, 1, 10, 1);
					track.setLines(2);
					track.setAllCaps(true);
					artist.setText(artistName);
					artist.setTextColor(color);
					artist.setPadding(10, 1, 1, 1);
					artist.setLines(2);
					artist.setAllCaps(true);
					//url.setText(trackUrl);
					//url.setPadding(1, 2, 2, 1);
					
					// set the parent child relationships
					row.addView(track);
					row.addView(artist);
					//row.addView(url);
					trackTable.addView(row);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadPlaylist()
	{
		// load the saved system data
		UniArray tempMem = (UniArray) FileSystem.readObjectFile(this, "data", true);
		
		if (playlist != null)
		{
			// create the playlist key
			String list_key = playlist.replace(" ", "");
			
			// verify that the uniarray is valid and has the list key
			if (tempMem != null && tempMem.hasObject(list_key))
			{
				// retrieve the nested track object from the memory
				memory = (HashMap<String, UniArray>) tempMem.getObject(list_key);
			}
		}
	}
	
	public void savePlaylist()
	{
		// load the saved system data
		UniArray tempMem = (UniArray) FileSystem.readObjectFile(this, "data", true);
		
		// create the playlist key
		String list_key = playlist.replace(" ", "");
		
		// store the updated hashmap into the uniarray
		tempMem.putObject(list_key, memory);
		
		// save the object of data to the system
		FileSystem.writeObjectFile(this, tempMem, "data", true);
	}
}
