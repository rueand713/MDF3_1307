package com.randerson.snapit;

import com.randerson.classes.MediaManager;

import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		
		OnAudioFocusChangeListener focusListener = new OnAudioFocusChangeListener() {
			
			@Override
			public void onAudioFocusChange(int focusChange) {
				// TODO Auto-generated method stub
				
				if (focusChange == MediaManager.AUDIO_GAIN)
				{
					
				}
				else if (focusChange == MediaManager.AUDIO_LOSS)
				{
					
				}
				else if (focusChange == MediaManager.AUDIO_LOST)
				{
					
				}
			}
		};
		
		int result = audioManager.requestAudioFocus(focusListener, MediaManager.AUDIO_STREAM, MediaManager.AUDIO_GAIN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void focusHandler()
	{
		
		
	}

}
