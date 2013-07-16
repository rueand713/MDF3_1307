package com.randerson.classes;

import java.io.IOException;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

public class MediaManager implements MediaPlayer.OnPreparedListener {

	public static final int AUDIO_STREAM = AudioManager.STREAM_MUSIC;
	public static final int AUDIO_LOSS = AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
	public static final int AUDIO_LOST = AudioManager.AUDIOFOCUS_LOSS;
	public static final int AUDIO_GAIN = AudioManager.AUDIOFOCUS_GAIN; 
	
	MediaPlayer mediaPlayer;
	Context _context = null;
	
	// constructor
	MediaManager(Context context)
	{
		_context = context;
		mediaPlayer = new MediaPlayer();
	}
	
	// method to start playing the media player data source
	public void startPlaying()
	{
		mediaPlayer.start();
	}
	
	// method to stop playing the media player data source
	public void stopPlaying()
	{
		mediaPlayer.stop();
	}
	
	public void setupFilePlayer(String file)
	{
		// create the file path uri from the passed in string
		Uri filepath = Uri.parse(file);
		
		// create the local media player
		mediaPlayer = MediaPlayer.create(_context, filepath);
	}
	
	// method for creating the media player and setting the media player parameters
	public void setupStreamingPlayer(String url)
	{
		
		// verify that the url is not null
		if (url != null)
		{
			// set the media player audio stream type
			mediaPlayer.setAudioStreamType(AUDIO_STREAM);
			
			try {
				// set the media player parameters and set the preparations
				mediaPlayer.setDataSource(url);
				mediaPlayer.setOnPreparedListener(this);
				mediaPlayer.prepareAsync();
				
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp)
	{
		// the media has been prepared
		
	}
	
	// method for releasing the media player
	public void releasePlayer()
	{
		// release the media player object and nullify it
		mediaPlayer.release();
		mediaPlayer = null;
	}
	
}
