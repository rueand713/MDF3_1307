package com.randerson.classes;

import java.io.IOException;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

public class MediaManager {

	public static final int AUDIO_STREAM = AudioManager.STREAM_MUSIC;
	public static final int AUDIO_LOSS = AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
	public static final int AUDIO_LOST = AudioManager.AUDIOFOCUS_LOSS;
	public static final int AUDIO_GAIN = AudioManager.AUDIOFOCUS_GAIN; 
	
	public static MediaPlayer setupFilePlayer(String file, Context context)
	{
		// create the file path uri from the passed in string
		Uri filepath = Uri.parse(file);
		
		// create the local media player
		MediaPlayer mp = MediaPlayer.create(context, filepath);
		
		return mp;
	}
	
	// method for creating the media player and setting the media player data source
	public static MediaPlayer setupStreamingPlayer(String url)
	{
		MediaPlayer mp = new MediaPlayer();
		
		// verify that the url is not null
		if (url != null)
		{
			// set the media player audio stream type
			mp.setAudioStreamType(AUDIO_STREAM);
			
			try {
				// set the media player parameters and set the preparations
				mp.setDataSource(url);
				
			} catch (IllegalArgumentException e) {
				Log.e("IllegalArguments", "Argument error in MediaManager class setting data source");
			} catch (SecurityException e) {
				Log.e("SecurityException", "Exception in MediaManager class setting data source");
			} catch (IllegalStateException e) {
				Log.e("IllegalStateException", "Exception in MediaManager class setting data source");
			} catch (IOException e) {
				Log.e("IOException", "Exception in MediaManager class setting data source");
			}
			
		}
		
		return mp;
	}
	
}
