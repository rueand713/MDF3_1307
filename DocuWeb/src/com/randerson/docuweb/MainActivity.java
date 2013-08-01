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

import libs.IOManager;
import libs.InterfaceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.activity_title);
		
		// create the interface manager singleton
		final InterfaceManager UIFactory = new InterfaceManager(this);
		
		// create the network receiver object
		IOManager iom = new IOManager();
		final IOManager.Receiver networkReceiver = iom.new Receiver(this);
		
		// register the network receiver
		this.registerReceiver(networkReceiver, IOManager.getFilter());
		
		// create the button from the layout
		Button newTagButton = (Button) findViewById(R.id.new_note);
		
		// verify that the button is valid
		if (newTagButton != null)
		{
			// set the click listener for the button
			newTagButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// network type string
					String netType = "No Connection";
					
					// check if the network is available
					if (networkReceiver.getStatus())
					{
						// set the network string to the network type available
						netType = networkReceiver.getType() + " Connection Detected";
					}
					
					// display the network status toast
					(UIFactory.createToast(netType, false)).show();
					
					// check if the network is available
					if (networkReceiver.getStatus())
					{
						// create the intent to start the note activity
						Intent noteActivity = UIFactory.makeIntent(NoteActivity.class);
						
						// verify the intent is valid
						if (noteActivity != null)
						{
							// start the intent
							startActivity(noteActivity);
						}
					}
				}
			});
		}
		
		// set the click listener for the button
		Button viewTagButton = (Button)findViewById(R.id.view_note);
		
		// verify that the button is valid
		if (viewTagButton != null)
		{
			// set the click listener for the button
			viewTagButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// create the intent to start the list activity
					Intent listActivity = UIFactory.makeIntent(ListActivity.class);
					
					// verify the intent is valid
					if (listActivity != null)
					{
						// start the intent
						startActivity(listActivity);
					}
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

}
