package com.example.music;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

public class SongsManager extends ContextWrapper {
	
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<>();
	
	Cursor cursor;
	
	
	//Constructor
	public SongsManager(Context base){
		super(base);
		
	}
	/**
	 * Function to read all mp3 files from
	 * SD card
	 */
	public ArrayList<HashMap<String, String>> getplaylist(){
		ContentResolver cr = getContentResolver();
		
		// Process to read all music files from sdcard
		
		Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
						
		// Database elect statement
		String[] projection = {
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.DATA
		};
				
		// Query
		cursor = cr.query(uri, projection, null, null, null);
		
		if(cursor == null){
			// query failed
		}
		else if(!cursor.moveToFirst()){
			// no media on device
			Toast.makeText(this, "No Media Found!!!", Toast.LENGTH_SHORT).show();
		}
		else{
			
			int dataColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
			int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
			
			while (cursor.moveToNext()){
							
				String data = cursor.getString(dataColumn);
				String title = cursor.getString(titleColumn);
				
				HashMap<String, String> song = new HashMap<String, String>();
				song.put("songTitle", title);
				song.put("songPath", data);
				
				// Adding each song to song list
				songsList.add(song);
				
			}
			
		}
		
		// Return songs list array
		return songsList;
	}
	
	
}
