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

import java.util.HashMap;

import libs.FileSystem;
import libs.UniArray;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class EditActivity extends Activity {

	int itemClicked = -1;
	UniArray memory = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.activity_details);
		
		// get the activity intent
		Intent data = getIntent();
		
		// verify that the intent is valid
		if (data != null)
		{
			// retrieve the intent extras
			Bundle bundle = data.getExtras();
			
			// verify that the bundle is valid and has the item key
			if (bundle != null && bundle.containsKey("item"))
			{
				// set the itemClicked to the value passed in the intent
				itemClicked = bundle.getInt("item");
			}
		}
		
		// verify that the itemClicked is valid range 0 or greater for the index values
		if (itemClicked > -1)
		{
			memory = (UniArray) FileSystem.readObjectFile(this, "save_data", true);
			
			if (memory != null)
			{
				
				// create a hashmap to hold the current event data
				@SuppressWarnings("unchecked")
				HashMap<String, Object> event = (HashMap<String, Object>) memory.getObject(itemClicked);
				
				// create the textviews references from the layout
				TextView eventTitle = (TextView) findViewById(R.id.event_title);
				TextView eventTag = (TextView) findViewById(R.id.event_tags);
				TextView eventTime = (TextView) findViewById(R.id.event_time);
				TextView eventLocation = (TextView) findViewById(R.id.event_location);
				TextView eventDescription = (TextView) findViewById(R.id.event_description);
				
				// create the image view reference from the layout
				ImageView eventImage = (ImageView) findViewById(R.id.event_image);
				
				// create the string objects to hold their corresponding data
				String title = (String) event.get("title");
				String tag = (String) event.get("people");
				String time = (String) event.get("time");
				String location = (String) event.get("location");
				String description = (String) event.get("description");
				
				// create the bitmap object of the photo captured (if any)
				String imagePath = (String) event.get("image");
				
				// the bitmap of the event photo
				Bitmap image = null;
				
				// verify that the imagePath is valid
				if (imagePath != null && imagePath.equals("") == false)
				{
					// load the image from the system
					image = FileSystem.readBitmapFile(this, "default_image.jpg", false, false);
					
					// verify that the image is valid
					if (image != null)
					{
						// verify that the eventImage element is valid
						if (eventImage != null)
						{
							// set the imageview bitmap
							eventImage.setImageBitmap(image);
							
							eventImage.setVisibility(ImageView.VISIBLE);
						}
					}
				}
				
				// verify that the title field is valid
				if (eventTitle != null)
				{
					// set the title
					eventTitle.setText(title);
				}
				
				// verify that the tag field is valid
				if (eventTag != null)
				{
					// set the title
					eventTag.setText(tag);
				}
				
				// verify that the time field is valid
				if (eventTime != null)
				{
					// set the title
					eventTime.setText(time);
				}
				
				// verify that the title field is valid
				if (eventLocation != null)
				{
					// set the title
					eventLocation.setText(location);
				}
				
				// verify that the title field is valid
				if (eventDescription != null)
				{
					// set the title
					eventDescription.setText(description);
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	
	
}
