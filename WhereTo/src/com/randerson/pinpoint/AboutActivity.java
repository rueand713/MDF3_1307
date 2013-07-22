package com.randerson.pinpoint;

import com.randerson.classes.InterfaceManager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class AboutActivity extends Activity {

	InterfaceManager UIFactory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.about_activity);
		
		// allow the icon on the actionbar to act as navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// make the ui singleton
		UIFactory = new InterfaceManager(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// setup the action bar
		getMenuInflater().inflate(R.menu.about_menu, menu);
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getTitle().equals(getString(R.string.quit)))
		{
			// end the app
			finish();
		}
		else if  (item.getItemId() == android.R.id.home)
		{
			Intent data = getIntent();
			
			if (data != null)
			{
				Bundle bundle = data.getExtras();
				
				if (bundle != null && bundle.containsKey("caller"))
				{
					// init the intent
					Intent home = null;
					
					// verify which activity is the parent and set the home intent to that activity
					if ((bundle.get("caller")).equals("MainActivity"))
					{
						home = UIFactory.makeIntent(MainActivity.class);
					}
					else if ((bundle.get("caller")).equals("InputActivity"))
					{
						home = UIFactory.makeIntent(InputActivity.class);
					}
					
					// verify that the home intent has been set properly
					if (home != null)
					{
						// set the clear top flag to prevent duplicate activities
						home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						
						startActivity(home);
					}
				}
			}
		}
		
		return super.onOptionsItemSelected(item);
	}

}
