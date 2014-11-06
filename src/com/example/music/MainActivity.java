package com.example.music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnCompletionListener, SeekBar.OnSeekBarChangeListener{
	private static final String tagg = "MainActivity :";
	
	private ImageButton btnPlay;
	
	private ImageButton btnRepeat;
	private ImageButton btnShuffle;
	private SeekBar songProgressBar;
	private TextView songTitleLabel;
	private TextView songCurrentDuration;
	private TextView songTotalDuration;
	//Media Player
	private MediaPlayer player;
	// Handler to update timer, progressbar, etc.
	private Handler mHandler = new Handler();
	private SongsManager songsManager;
	private Utilities utils;
	private int seekForwardTime = 5000; //5000 msecs
	private int seekRewindTime = 5000; //5000 msecs
	private int currentSongIndex = 0;
	private boolean isShuffle = false;
	private boolean isRepeat = false;
	
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.fragment_main);
		
		

		
		
		// All player buttons
		btnPlay = (ImageButton)findViewById(R.id.btnPlay);
		
		btnRepeat = (ImageButton)findViewById(R.id.btnRepeat);
		btnShuffle= (ImageButton)findViewById(R.id.btnshuffle);
		songProgressBar = (SeekBar)findViewById(R.id.songProgressBar);
		songTitleLabel = (TextView)findViewById(R.id.songTitle);
		songCurrentDuration = (TextView)findViewById(R.id.songCurrentDuration);
		songTotalDuration = (TextView)findViewById(R.id.songTotalDuration);
		
		//Media Player
		player = new MediaPlayer();
		songsManager = new SongsManager(this);
		utils = new Utilities();
		
		//Listeners
		songProgressBar.setOnSeekBarChangeListener(this); //important
		player.setOnCompletionListener(this); //important
		
		// Getting all songs list
		songsList = songsManager.getplaylist();
		
		// Play first song by default
		//playSong(0);
		
		
		
	}
	
	/**
	 * Play button click event		
	 */
	public void play(View view){
		// Check for already playing
		if(player.isPlaying()){
			if(player != null){
				player.pause();
				
				btnPlay.setImageResource(R.drawable.play_default);
			}
		}
		else{
			if(player != null){
				player.start();
				
				btnPlay.setImageResource(R.drawable.ic_av_pause);
			}
		}
	}
	
	/**
	 * Forward button click event
	 */
	public void forward(View view){
		int currentPosition = player.getCurrentPosition();
		// Check if forward seek time is smaller than the song duration
		if(currentPosition + seekForwardTime <= player.getDuration()){
			// Forward song
			player.seekTo(currentPosition + seekForwardTime);
			
		}
		else{
			// forward to end position
			player.seekTo(player.getDuration());
		}
	}
	
	/**
	 * Rewind button click event
	 */
	public void rewind(View view){
		int currentPosition = player.getCurrentPosition();
		// Check if rewind seek time is greater than 0
		if(currentPosition - seekRewindTime >= 0){
			// Rewind sond
			player.seekTo(currentPosition - seekRewindTime);
		}
		else{
			// Rewind to start position
			player.seekTo(0);
		}
	}
	
	/**
	 * Next button click event
	 */
	public void next(View view){
		// Check if next song index is available
		if(currentSongIndex < (songsList.size() - 1)){
			playSong(currentSongIndex + 1);
			currentSongIndex = currentSongIndex + 1;
		}
		else{
			// Play first song
			playSong(0);
			currentSongIndex = 0;
		}
	}
	
	/**
	 * Previous button click
	 */
	public void previous(View view){
		//Check if previous index is available
		if(currentSongIndex > 0){
			playSong(currentSongIndex - 1);
			currentSongIndex = currentSongIndex -1;
		}
		else{
			// Play last song
			playSong(songsList.size() - 1);
			currentSongIndex = songsList.size() - 1;
		}
	}
	
	/**
	 * Repeat button click
	 */
	public void repeat(View view){
		if(isRepeat){
			isRepeat = false;
			Toast.makeText(this, "Repeat is off!!!", Toast.LENGTH_SHORT).show();
			btnRepeat.setImageResource(R.drawable.ic_av_replay);
		}
		else{
			// Turn repeat true
			isRepeat = true;
			Toast.makeText(this, "Repeat is on!!!", Toast.LENGTH_SHORT).show();
			// Turn shuffle false
			isShuffle = false;
			btnRepeat.setImageResource(R.drawable.ic_av_replay_pressed);
			btnShuffle.setImageResource(R.drawable.ic_av_shuffle);
		}
	}
	
	/**
	 * Shuffle button click event
	 */
	public void shuffle(View view){
		if(isShuffle){
			isShuffle = false;
			Toast.makeText(this, "Shuffle is off!!!", Toast.LENGTH_SHORT).show();
			btnShuffle.setImageResource(R.drawable.ic_av_shuffle);
		}
		else{
			// Turn shuffle true
			isShuffle = true;
			Toast.makeText(this, "Shuffle is on!!!", Toast.LENGTH_SHORT).show();
			// Turn repeat off
			isRepeat = false;
			btnShuffle.setImageResource(R.drawable.ic_av_shuffle_pressed);
			btnRepeat.setImageResource(R.drawable.ic_av_replay);
		}
	}
	
	/**
	 * Playlist button click event
	 */
	public void playlist(View view){
		Intent i = new Intent(this, PlayListActivity.class);
		startActivityForResult(i, 100);
	}
	
	/**
	 * Receive song index from playlist view
	 * and play the song
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 100){
			currentSongIndex = data.getExtras().getInt("songIndex");
			// Play song
			playSong(currentSongIndex);
		}
	}
	
	/**
	 * Function to play a song
	 * @param songIndex - index of a song
	 */
	public void playSong(int songIndex){
		// Play song
		try{
			player.reset();
			player.setDataSource(songsList.get(songIndex).get("songPath"));
			player.prepare();
			player.start();
			// Displaying song title
			String songTitle = songsList.get(songIndex).get("songTitle");
			songTitleLabel.setText(songTitle);
			
			// Change button drawable to pause
			btnPlay.setImageResource(R.drawable.btn_pause);
			
			// Set progress bar values
			songProgressBar.setProgress(0);
			songProgressBar.setMax(100);
			
			// Updating progress bar
			updateProgressBar();
			
		}
		catch(IllegalArgumentException e){
			Log.d(tagg, "Exception Thrown While Playing A Song", e);
		}
		catch(IllegalStateException e){
			Log.d(tagg, "Exception Thrown While Playing A Song", e);
		}
		catch(IOException e){
			Log.d(tagg, "Exception Thrown While Playing A Song", e);
		}
	}
	
	/**
	 * Update time on seekbar
	 */
	public void updateProgressBar(){
		mHandler.postDelayed(mUpdateTimeTask, 100);
	}
	
	/**
	 * Background Runnable thread
	 */
	private Runnable mUpdateTimeTask = new Runnable(){
		public void run(){
			long totalDuration = player.getDuration();
			long currentDuration = player.getCurrentPosition();
			
			// Displaying Total duration time
			songTotalDuration.setText(""+utils.mSecondsToTimer(totalDuration));
			// Displaying elapsed time
			songCurrentDuration.setText(""+utils.mSecondsToTimer(currentDuration));
			
			// Update progress bar
			int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
			// Log.d(tagg, "Song in progress" + progress);
			songProgressBar.setProgress(progress);
			
			// Running this thread after 100 msecs
			mHandler.postDelayed(this, 100);
		}
	};
	
	/**
	 * Function for progress changed on seek bar
	 */
	@Override
	public void onProgressChanged(SeekBar seekbar, int progress, boolean fromTouch){
		
	}
	
	/**
	 * Function for user input on seek bar - user starts to drag
	 */
	@Override
	public void onStartTrackingTouch(SeekBar seekbar){
		// Stop message Handler from updating progress bar
		mHandler.removeCallbacks(mUpdateTimeTask);
	}
	
	/**
	 * Function for user input on seek bar - user stops to drag
	 */
	@Override
	public void onStopTrackingTouch(SeekBar seekbar){
		mHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = player.getDuration();
		int currentPosition = utils.progressofTimer(seekbar.getProgress(), totalDuration);
		
		player.seekTo(currentPosition);
		updateProgressBar();
	}
	
	/**
	 * On song completion
	 * if Repeat is on, play same song
	 * if Shuffle is on, play random song
	 */
	@Override
	public void onCompletion(MediaPlayer arg0){
		// Check if repeat is on or off
		if(isRepeat){
			playSong(currentSongIndex);
		}
		else if(isShuffle){
			Random rand = new Random();
			currentSongIndex = rand.nextInt((songsList.size() - 1));
			playSong(currentSongIndex);
		}
		else{
			if(currentSongIndex < (songsList.size() - 1)){
				playSong(currentSongIndex + 1);
				currentSongIndex = currentSongIndex + 1;
			}
			else{
				playSong(0);
				currentSongIndex = 0;
			}
		}
			
	}
	
	@Override
	public void onDestroy(){
		if(player != null){
			super.onDestroy();
			
		}
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
		
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
