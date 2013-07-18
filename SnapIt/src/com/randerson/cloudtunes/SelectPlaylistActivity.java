package com.randerson.cloudtunes;

import com.randerson.classes.FileSystem;

import libs.UniArray;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SelectPlaylistActivity extends Activity {

	// create the playlist object
	String playlist;
	
	// set the success bool
	boolean finished = false;
	
	// create the playlist object
	UniArray memory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_playlist);
		
		// load the saved system data
		memory = (UniArray) FileSystem.readObjectFile(this, "data", true);
		
		// verify that the memory object is valid
		if (memory != null)
		{
			// call the method to populate the playlists
			populateList();
		}
		
		Button doneBtn = (Button) findViewById(R.id.radio_done);
		
		doneBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// call the method to check for a playlist selection
				fetchSelection();
				
				// call the method to end the activity
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public void finish() {
		
		if (finished == true)
		{
			// create an intent to hold return data
			Intent data = new Intent();
			
			// add the data extras to return
			data.putExtra("playlist", playlist);
			
			// set the result and data to return
			setResult(RESULT_OK, data);
		}
		
		super.finish();
	}
	
	public void populateList()
	{
		// create the table for the playlist data
		RadioGroup trackTable = (RadioGroup) findViewById(R.id.playlist_table);
		
		// empty any lingering views
		trackTable.removeAllViews();
		
			// verify that the uniarray is valid
			if (memory != null)
			{
				// create an array of all the playlist keys
				String[] playlists = memory.getAllObjectKeys();
				
				// iterate over the uniarray creating the list of tracks
				for (int i = 0; i < memory.objectsLength(); i++)
				{	
					// create the table's child objects
					RadioButton radio = new RadioButton(this);
					
					// set the ui data
					radio.setText(playlists[i]);
					
					// set the parent child relationships
					trackTable.addView(radio);
				}
			}
	}
	
	public void fetchSelection()
	{
		// create the radio group
		RadioGroup radios = (RadioGroup) findViewById(R.id.playlist_table);
		
		// verify that the radio group is valid
		if (radios != null)
		{
			// retrieve the checked radio button id
			int id = radios.getCheckedRadioButtonId();
			
			// create the selected radio button reference
			RadioButton selection = (RadioButton) radios.findViewById(id);
			
			// verify that the radio button is valid
			if (selection != null)
			{
				// retrieve the radio button text
				playlist = selection.getText().toString();
				
				// set the success bool
				finished = true;
			}
		}
	}
}
