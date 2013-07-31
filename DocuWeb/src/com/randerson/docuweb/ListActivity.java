package com.randerson.docuweb;

import java.util.ArrayList;
import java.util.HashMap;

import libs.FileSystem;
import libs.InterfaceManager;
import libs.UniArray;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

public class ListActivity extends Activity {

	InterfaceManager UIFactory = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
				
				// create the hashmap medium for the array list
				HashMap<String, String> tempHash = new HashMap<String, String>();
				
				// add the key to the hash of memory object titles (keys)
				tempHash.put("note " + (i+1), key);
				
				// add the hashmap to the array list
				data.add(tempHash);
			}
			
			// create the listview from the layout
			ListView lsv = (ListView) findViewById(R.id.data_list);
			
			// setup the listview
			UIFactory.createList(lsv, data, R.layout.list_data, new String[] {"Note"}, new int[]{R.id.note_section});
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	
	
}
