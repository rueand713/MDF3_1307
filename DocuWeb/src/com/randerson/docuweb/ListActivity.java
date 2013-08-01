/*
 * project 		DocuWeb
 * 
 * package 		com.randerson.docuweb
 * 
 * @author 		Rueben Anderson
 * 
 * date			Aug 1, 2013
 * 
 */
package com.randerson.docuweb;

import java.util.ArrayList;
import java.util.HashMap;

import libs.FileSystem;
import libs.InterfaceManager;
import libs.UniArray;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ListActivity extends Activity {

	InterfaceManager UIFactory = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.activity_list);
		
		// create the uifactory singleton
		UIFactory = new InterfaceManager(this);
		
		// read the saved data
		UniArray memory = (UniArray) FileSystem.readObjectFile(this, "save_data", true);
		
		if (memory != null)
		{
			// create the array list data object
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			
			// array of string keys
			String[] keys = memory.getAllObjectKeys();
			
			// iterate over the memory uniarray and retrieve the keys for each index
			for (int i = 0; i < memory.objectsLength(); i++)
			{	
				// the object key
				String key = keys[i];
				
				// grab the data hashmap from the uniarray
				@SuppressWarnings("unchecked")
				HashMap<String, Object> currentSel = (HashMap<String, Object>) memory.getObject(key);
				
				// set the timestamp string
				String timestamp = (String) currentSel.get("timestamp");
				
				// create the hashmap medium for the array list
				HashMap<String, String> tempHash = new HashMap<String, String>();
				
				// add the key and timestamp to the hash of memory object titles (keys)
				tempHash.put("Note", key);
				tempHash.put("Time", timestamp);
				
				// add the hashmap to the array list
				data.add(tempHash);
			}
			
			// create the listview from the layout
			ListView lsv = (ListView) findViewById(R.id.data_list);
			
			lsv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					
					// create the view/edit details intent
					Intent viewDetails = UIFactory.makeIntent(EditActivity.class);
					
					// verify that the intent is valid
					if (viewDetails != null)
					{
						// pass in the selected item id
						viewDetails.putExtra("item", arg2);
						
						// start the edit/view details activity
						startActivity(viewDetails);
					}
				}
				
			});
			
			// setup the listview
			UIFactory.createList(lsv, data, R.layout.list_data, new String[] {"Note", "Time"}, new int[]{R.id.note_section, R.id.time_section});
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	
}
