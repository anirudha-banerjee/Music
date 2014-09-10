package com.example.music;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Environment;

public class SongsManager {
	//SD card path
	final String Media_Path = Environment.getExternalStorageDirectory() + "/Sounds/songs";
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	
	//Constructor
	public SongsManager(){
		
	}
	/**
	 * Function to read all mp3 files from
	 * SD card
	 */
	public ArrayList<HashMap<String, String>> getPlaylist(){
		File home = new File(Media_Path);
		
		if(home.listFiles(new FileExtensionFilter()) != null){
			for(File file : home.listFiles(new FileExtensionFilter())){
				HashMap<String, String> song = new HashMap<String, String>();
				song.put("songTitle", file.getName().substring(0, (file.getName().length() -4)));
				song.put("songPath", file.getPath());
				
				// Adding each song to song list
				songsList.add(song);
			}
		}
		// Return songs list array
		return songsList;
	}
	
	/**
	 * Class to filter files which have .mp3 extension
	 */
	class FileExtensionFilter implements FilenameFilter{
		public boolean accept(File dir, String name){
			return (name.endsWith(".mp3") || name.endsWith(".mp3"));
		}
	}

}
