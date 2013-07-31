package com.randerson.docuweb;

import libs.InterfaceManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title);
		
		final InterfaceManager UIFactory = new InterfaceManager(this);
		
		// create the button from the layout
		Button newTagButton = (Button) findViewById(R.id.new_note);
		
		// verify that the button is valid
		if (newTagButton != null)
		{
			// set the click listener for the button
			newTagButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// create the intent to start the note activity
					Intent noteActivity = UIFactory.makeIntent(NoteActivity.class);
					
					// verify the intent is valid
					if (noteActivity != null)
					{
						// start the intent
						startActivity(noteActivity);
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
