package com.example.music;

public class Utilities {
	/**
	 * Function to convert miliseconds time to
	 * Timer format
	 * Hour:Minute:Second
	 */
	public String mSecondsToTimer(long mseconds){
		String finalTimerString = "";
		String secondsString = "";
		
		// Convert total duration to time
		int hours = (int) (mseconds / (1000*60*60));
		int minutes = (int) (mseconds % (1000*60*60)) / (1000*60);
		int seconds = (int) ((mseconds % (1000*60*60)) % (1000*60)) / 1000;
		
		// Add hours if available
		if(hours > 0){
			finalTimerString = hours + ":";
		}
		
		// Addition of 0 to seconds if single digit
		if(seconds < 10){
			secondsString = "0" + seconds;
		}
		else{
			secondsString = "" + seconds;
		}
		
		finalTimerString = finalTimerString + minutes + ":" + secondsString;
		
		// Return Timer String
		return finalTimerString;
	}
	
	/**
	 * Function to get progress percentage
	 * @param currentDuration
	 * @param totalDuration 
	 */
	public int getProgressPercentage(long currentDuration, long totalDuration){
		Double percentage = (double) 0;
		
		long currentSeconds = (currentDuration/1000);
		long totalSeconds = (totalDuration/1000);
		
		// Calculating percentage
		percentage = (((double)currentSeconds)/totalSeconds) * 100;
		
		return percentage.intValue();
	}
	
	/**
	 * Function to change progress of timer
	 * @param progress -
	 * @param totalDuration
	 * returns current duration in msecs
	 */
	public int progressofTimer(int progress, int totalDuration){
		int currentDuration = 0;
		totalDuration = (int) totalDuration/1000;
		currentDuration = (int) ((((double)progress)/100)* totalDuration);
		
		return currentDuration*1000;
	}

}
