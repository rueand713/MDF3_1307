package com.randerson.pinpoint;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class InputActivity extends Activity {

	String[] returnData;
	boolean okApp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.input_activity);
		
		// set the return string to null for default
		returnData = new String[] {null, null, null};
		
		// set the default bool
		okApp = false;
		
		Button doneBtn = (Button) findViewById(R.id.doneBtn);
		
		doneBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// creates a reference to the edit text objects in layout file
				EditText countryField = (EditText) findViewById(R.id.countryField);
				EditText stateField = (EditText) findViewById(R.id.stateField);
				EditText cityField = (EditText) findViewById(R.id.cityField);
				EditText addressField = (EditText) findViewById(R.id.addressField);
				EditText noteField = (EditText) findViewById(R.id.noteField);
				EditText titleField = (EditText) findViewById(R.id.titleField);
				
				// create an array of editText objects for iteration
				EditText[] addressFields = {addressField, cityField, stateField, countryField};
				
				// create strings for holding user data
				String address = null;
				String notes = null;
				String title = null;
				
				// verify that the notefield is created properly
				if (noteField != null)
				{
					// set the notes string to the string data of the notes field
					notes = noteField.getText().toString();
				}
				else 
				{
					// set notes string to empty string
					notes = "";
				}
				
				// verify that the title field is created properly
				if (titleField != null)
				{
					// set the notes string to the string data of the notes field
					title = titleField.getText().toString();
				}
				else 
				{
					// set title string to empty string
					title = "";
				}
				
				// concatenate the address string
				address = addressFields[0].getText().toString() + 
						"+" + addressFields[1].getText().toString() + 
						"+" + addressFields[2].getText().toString() + 
						"+" + addressFields[3].getText().toString();
				
				// replace the whitespace and dual addend chars
				address = address.replace(" ", "+");
				address = address.replace("++", "+");
				
				// format the start address for the proper uri handling
				if (address.startsWith("+"));
				{
					address = address.substring(1);
				}
				
				// format the end address for proper uri handling
				if (address.endsWith("+"))
				{
					address = address.substring(0, (( address.length() ) -1));
				}
				
				// store the return data
				returnData[0] = address;
				returnData[1] = notes;
				returnData[2] = title;
				
				// the app should return ok
				okApp = true;
				
				// end the activity
				finish();
			}
		});
	}

	@Override
	public void finish() {
		
		if (okApp == true)
		{
			// create an intent to pass back to the calling activity
			Intent data = new Intent();
			
			// store the return data in the intent extras
			data.putExtra("params", returnData[0]);
			data.putExtra("notes", returnData[1]);
			data.putExtra("title", returnData[2]);
			
			setResult(RESULT_OK, data);
		}
		
		super.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	
}
