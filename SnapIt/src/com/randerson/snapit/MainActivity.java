package com.randerson.snapit;

import java.util.HashMap;

import libs.UniArray;

import com.randerson.classes.FileSystem;
import com.randerson.classes.IOManager;
import com.randerson.classes.InterfaceManager;
import com.randerson.classes.MediaManager;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener {

	// bool that handles network status
	boolean networkReady = false;
	
	public class Receiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo network = cm.getActiveNetworkInfo();
			
			// check the network status is connected
			if (network.isConnected())
			{
				// set the bool to true
				networkReady = true;
			}
			else
			{
				// set the bool to false
				networkReady = false;
			}
		} 
	}
	
	// set this to true to load the dummy data
	boolean loadDummy = true;
	
	// declare the mediaplayer object
	MediaPlayer mediaplayer;
	
	Context CONTEXT;
	
	// declare the interfacemanager
	InterfaceManager UIFactory;
	
	// declare the newplaylist button
	Button newPlaylist;
	
	// bool playlist button handler
	boolean isNewPlaylist = true;
	
	// save data object
	UniArray memory;
	
	// the playlist object
	HashMap<String, UniArray> currentPlaylist;
	
	// the most recent playlist string
	String currentPlaylistName;
	
	// set the current total track object
	int currentTrack = 0;
	int totalTracks = 0;
	
	boolean isPaused = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// set the global context
		CONTEXT = this;
		
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		Receiver receiver = new Receiver();
		this.registerReceiver(receiver, filter);
		
		// create the interface singleton
		UIFactory = new InterfaceManager(this);
		
		boolean status = IOManager.getConnectionStatus(this);
		
		if (status == true)
		{
			// create the network type string
			String statusType = IOManager.getConnectionType(this) + " Network Detected";
			
			// make a toast to inform the user of a valid network connection
			Toast t = UIFactory.createToast(statusType, false);
			t.show();
		}
		else
		{
			// make a toast to inform the user of a failed network connection
			Toast t = UIFactory.createToast("No Network Connection", false);
			t.show();
		}
		
		// create the uniarray object
		memory = new UniArray();
		
		// create the mediaplayer object with the current context
		mediaplayer = new MediaPlayer();
		
		// handle loading the dummy data or not
		createDummyData();
		
		// create the new playlist button
		newPlaylist = (Button) findViewById(R.id.new_playlist);
		
		// set the click listener for the new playlist button
		newPlaylist.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// method for handling the creation of a new playlist
				makeNewPlaylist();
			}
		});
		
		// create the load playlist button
		Button loadPlaylist = (Button) findViewById(R.id.load_playlist);
		
		// set the load playlist button click listener
		loadPlaylist.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UniArray tempList = (UniArray) FileSystem.readObjectFile(CONTEXT, "data", true);
				
				if (tempList != null)
				{
					// call the load playlist activity
					Intent selectPlaylist = UIFactory.makeIntent(SelectPlaylistActivity.class);
					startActivityForResult(selectPlaylist, 0);
				}
			}
		});
		
		// create the play button
		Button playButton = (Button) findViewById(R.id.play_button);
		
		// set the click listener for the play button
		playButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (networkReady)
				{
					// request the audio focus and catch the audio focus result
					int result = requestFocus();
					
					if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
					{
						if (isPaused == false)
						{
							// setup the track data to play
							loadNextTrack();
						}
						else if (isPaused == true)
						{
							// resume playing the track
							mediaplayer.start();
							
							// reset the pause bool
							isPaused = false;
						}
					}
				}
				else
				{
					Toast t = UIFactory.createToast("No network detected. A network is required to stream data.", false);
					t.show();
				}
			}
		});
		
		// create the pause button
		Button pauseButton = (Button) findViewById(R.id.pause_button);
		
		// set the click listener for the pause button
		pauseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// verify the mediaplayer object is not null
				if (mediaplayer != null && mediaplayer.isPlaying())
				{
					// pause the music stream
					mediaplayer.pause();
					
					// set the pause bool
					isPaused = true;
				}
			}
		});
		
		// create the stop button
		Button stopButton = (Button) findViewById(R.id.stop_button);
		
		// set the click listener for the stop button
		stopButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// verify the mediaplayer object is not null
				if (mediaplayer != null && mediaplayer.isPlaying())
				{
					// stop the music stream
					mediaplayer.stop();
				}
			}
		});
		
		// create the add button
		Button addBtn = (Button) findViewById(R.id.add_button);
		
		// set the click listener for the add button
		addBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// create the second activity's intent
				Intent addTracks = UIFactory.makeIntent(AddTrackActivity.class);
				
				if (currentPlaylist != null && currentPlaylist.containsKey(currentPlaylistName))
				{
					// add the current playlist title to the intent extras
					addTracks.putExtra("playlist", currentPlaylistName);
					
					// start the add track activity
					startActivityForResult(addTracks, 0);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 0 && resultCode == RESULT_OK)
		{
			if (data != null)
			{
				// get the intent extras returned
				Bundle options = data.getExtras();
				
				if (options.containsKey("playlist"))
				{
					// set the currentplaylist name to the playlist string returned
					currentPlaylistName = options.getString("playlist");
					
					// playlist has been created show the add button
					Button addBtn = (Button) findViewById(R.id.add_button);
					addBtn.setVisibility(Button.VISIBLE);
					
					// load the playlist data for the returned playlist
					loadPlaylist();
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// method for handling the audio focus change events
	public void focusHandler(int focusChange)
	{
		if (focusChange == MediaManager.AUDIO_GAIN)
		{	
			// verify that the mediaplayer object is not null
			if (mediaplayer != null)
			{
				// focus has been gained, ok to play audio
				mediaplayer.start();
			}
		}
		else if (focusChange == MediaManager.AUDIO_LOSS)
		{
			// verify that the mediaplayer object is not null
			if (mediaplayer != null)
			{
				// focus has been lost temporarily, pause audio
				mediaplayer.pause();
			}
		}
		else if (focusChange == MediaManager.AUDIO_LOST)
		{
			// verify that the mediaplayer object is not null
			if (mediaplayer != null)
			{
				// focus has been lost for unknown time, stop audio
				mediaplayer.stop();
				mediaplayer.release();
			}
		}
	}
	
	// method for making a new playlist object
	public void makeNewPlaylist()
	{
		if (isNewPlaylist == true)
		{
			// create the reference to the playlist table
			TableLayout playlistTable = (TableLayout) findViewById(R.id.list_table);
			
			// clear out the current playlist tracks
			playlistTable.removeAllViews();
			
			// set the current boolean handler to false
			isNewPlaylist = false;
			
			// change the button text to reflect the new action
			newPlaylist.setText(R.string.save_playlist);
			
			// show the new playlist edit text field
			EditText playlistName = (EditText) findViewById(R.id.new_playlist_name);
			playlistName.setVisibility(EditText.VISIBLE);
		}
		else if (isNewPlaylist == false)
		{
			// set the current boolean handler to true
			isNewPlaylist = true;
			
			// change the button text to reflect the new action
			newPlaylist.setText(R.string.create_playlist);
			
			// grab the playlist name field
			EditText playlistName = (EditText) findViewById(R.id.new_playlist_name);
			
			// grab the text from the editText object
			String title = playlistName.getText().toString();
			
			// verify that the title after formatting is valid
			if (title != null && title.equals("") == false)
			{
				// grab formatted text string from the title
				String titleKey = title.replace(" ", "");
				
				// verify that the titlekey after formatting is valid
				if (titleKey != null && titleKey.equals("") == false)
				{	
					// set the currentPlaylist name string
					currentPlaylistName = title;
					
					// create a hashmap to hold the playlist data
					currentPlaylist = new HashMap<String, UniArray>();
					currentPlaylist.put(title, new UniArray());
					
					// save the new playlist data
					savePlaylist();
					
					// playlist has been created show the add button
					Button addBtn = (Button) findViewById(R.id.add_button);
					addBtn.setVisibility(Button.VISIBLE);
				}
			}
			
			// hide the new playlist edit text field
			playlistName.setVisibility(EditText.GONE);
		}
	}
	
	public void populateList()
	{
		// create the table for the playlist data
		TableLayout trackTable = (TableLayout) findViewById(R.id.list_table);
		
		// empty any lingering views
		trackTable.removeAllViews();
		
		if (currentPlaylistName != null) 
		{
			// retrieve the uniarray of track data from the memor object
			UniArray playlistData = currentPlaylist.get(currentPlaylistName);
			
			// verify that the uniarray is valid
			if (playlistData != null)
			{
				int color;
				
				// iterate over the uniarray creating the list of tracks
				for (int i = 0; i < playlistData.objectsLength(); i++)
				{
					if (i % 2 > 0)
					{
						color = getResources().getColor(android.R.color.primary_text_dark);
					}
					else
					{
						color = getResources().getColor(android.R.color.secondary_text_light);
					}
					
					// retrieve the string array from the playlist data
					String[] currentTrack = (String[]) playlistData.getObject(i);
					
					// retrieve the individual string components (artist, track, url)
					String trackName = currentTrack[1];
					String artistName = currentTrack[0];
					//String trackUrl = currentTrack[2];
					
					// create the table's child objects
					TableRow row = new TableRow(this);
					TextView track = new TextView(this);
					TextView artist = new TextView(this);
					//TextView url = new TextView(this);
					
					// set the ui data
					track.setText(trackName);
					track.setTextColor(color);
					track.setPadding(1, 1, 10, 1);
					track.setLines(2);
					track.setAllCaps(true);
					artist.setText(artistName);
					artist.setTextColor(color);
					artist.setPadding(10, 1, 1, 1);
					artist.setLines(2);
					artist.setAllCaps(true);
					//url.setText(trackUrl);
					//url.setPadding(1, 2, 2, 1);
					
					// set the parent child relationships
					row.addView(track);
					row.addView(artist);
					//row.addView(url);
					trackTable.addView(row);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadPlaylist()
	{
		// load the saved system data
		memory = (UniArray) FileSystem.readObjectFile(this, "data", true);
		
		if (currentPlaylistName != null)
		{
			// create the playlist key
			String list_key = currentPlaylistName.replace(" ", "");
			
			// verify that the uniarray is valid and has the list key
			if (memory != null && memory.hasObject(list_key))
			{
				// retrieve the nested track object from the memory
				currentPlaylist = (HashMap<String, UniArray>) memory.getObject(list_key);
				
				// reset the current track and total tracks
				currentTrack = 0;
				totalTracks = currentPlaylist.get(currentPlaylistName).objectsLength();
				
				// call the method for populating the playlist data
				populateList();
			}
		}
	}
	
	public void savePlaylist()
	{
		// load the saved system data
		memory = (UniArray) FileSystem.readObjectFile(this, "data", true);
		
		if (memory == null)
		{
			memory = new UniArray();
		}
		
		// create the playlist key
		String list_key = currentPlaylistName.replace(" ", "");
		
		// store the updated hashmap into the uniarray
		memory.putObject(list_key, currentPlaylist);
		
		// save the object of data to the system
		FileSystem.writeObjectFile(this, memory, "data", true);
		
		// reset the current track and total tracks
		currentTrack = 0;
		totalTracks = 0;
	}
	
	public void loadNextTrack()
	{
		if (currentPlaylist != null && currentPlaylist.containsKey(currentPlaylistName))
		{
			// create the playlist object
			UniArray playlist = currentPlaylist.get(currentPlaylistName);
			
			if (playlist != null)
			{
				// retrieve the track data for the current track from the playlist object
				String[] data = (String[]) playlist.getObject(currentTrack);
				
				if (data != null)
				{
					// retrieve the url for the current track
					String url = data[2];
					
					if (url != null)
					{
						// verify that the current track has not reached the end of the list
						if (currentTrack > totalTracks)
						{
							// reset the current track
							currentTrack = 0;
						}
						
						// setup the media player to stream the current track url
						setupPlayer(url);
						
						// increment the next track
						currentTrack++;
					}
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		// release the media player object
		mediaplayer.release();
		mediaplayer = null;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mp.start();
	}

	@Override
	public void onAudioFocusChange(int focusChange) {
		
		// method for handling change of audio focus
		focusHandler(focusChange);
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		
		Log.e("MediaPlayer Error", "" + what + "//" + extra);
		return false;
	}
	
	public int requestFocus()
	{
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		
		// request the audio focus and catch the audio focus result
		int result = audioManager.requestAudioFocus(this, MediaManager.AUDIO_STREAM, MediaManager.AUDIO_GAIN);
		
		return result;
	}
	
	public void setupPlayer(String url)
	{
			if (mediaplayer == null)
			{
				// setup a new mediaplayer object
				mediaplayer = new MediaPlayer();
			}
			
			if (url != null && mediaplayer.isPlaying() == false)
			{
				setVolumeControlStream(AudioManager.STREAM_MUSIC);
				mediaplayer = MediaManager.setupStreamingPlayer(url);
				mediaplayer.setOnPreparedListener(this);
				mediaplayer.prepareAsync();
			}
	}
	
	public void createDummyData()
	{
		if (loadDummy == true)
		{
			// string data to simulate user input
			String[] artists = {"zelda:ost", "ne-yo", "curtis mayfield", "john legend", "seal"};
			String[] tracks = {"bolero", "lazy love", "makings of you", "tonight", "touch"};
			String[] urls = {"https://dl.dropboxusercontent.com/u/68223018/Music/bolero.mp3", 
					"https://dl.dropboxusercontent.com/u/68223018/Music/lazy_love.mp3", 
					"https://dl.dropboxusercontent.com/u/68223018/Music/makings_of_you.mp3", 
					"https://dl.dropboxusercontent.com/u/68223018/Music/tonight.mp3", 
					"https://dl.dropboxusercontent.com/u/68223018/Music/touch.mp3"};
			
			// demo playlist name
			String demo_list = "Demo";
			
			// load the saved system data
			memory = (UniArray) FileSystem.readObjectFile(this, "data", true);
			
			// verify that the memory is valid
			if (memory == null)
			{
				memory = new UniArray();
			}
			
			// create the memory directory objects for the playlist data
			HashMap<String, UniArray> playlists = new HashMap<String, UniArray>();
			UniArray playlist = new UniArray();
			
			// iterate over the string arrays and data objects creating an artifical memory store
			// to mimic user input
			for (int i = 0; i < artists.length; i++)
			{
				// create a string object to hold the complete track info
				String[] trackData = {artists[i], tracks[i], urls[i]};
				
				// put the track data into the uniarray playlist object
				playlist.putObject((artists[i]+tracks[i]), trackData);
				
				// put the playlist object into the list of playlists
				playlists.put(demo_list, playlist);
				
				// put the playlists object into the application memory object
				memory.putObject(demo_list, playlists);
			}
			
			// save the dummy object of data to the system
			FileSystem.writeObjectFile(this, memory, "data", true);
		}
		
	}
	
}
