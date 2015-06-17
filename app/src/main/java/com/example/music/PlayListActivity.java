package com.example.music;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class PlayListActivity extends ListActivity {
	// List for songs
	public ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	// Cursor
	Cursor cursor;
	SongsManager songsManager;
		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist);
		
		songsManager = new SongsManager(this);
		this.songsList = songsManager.getplaylist();
		
						
		// Adding menu item to list view
		ListAdapter adapter = new SimpleAdapter(this, songsList, R.layout.playlist_item,
				new String[] {"songTitle"}, new int[]{ R.id.songTitle });
		
		setListAdapter(adapter);
		
		// Selecting single list view item
		ListView lv = getListView();
		
		// Listning for single list item click
		lv.setOnItemClickListener(new OnItemClickListener(){
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id){
				// Getting List View Index
				int songIndex = position;
				
				// Starting new intent
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				// Sending song index to player
				i.putExtra("songIndex", songIndex);
				setResult(100, i);
				//Closing Playlist
				finish();
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_list, menu);
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
}
