package edu.cmu.semat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;



public class SharedPreferencesUtil {
	
	private static String preferenceFile = "edu.cmu.semat";
	private static String splashKey = "edu.cmu.semat.splash_key";
	private static String teamKey = "edu.cmu.semat.team_key";
	private static String emailKey = "edu.cmu.semat.email_key";

	
	public static void setSplashScreenSeenByUser(Activity activity, boolean value) {
		SharedPreferences prefs = activity.getSharedPreferences(
				preferenceFile, Context.MODE_PRIVATE);
		prefs.edit().putBoolean(splashKey, value).commit();		
	}
	
	public static boolean getSplashScreenSeenByUser(Activity activity) {
		SharedPreferences prefs = activity.getSharedPreferences(
				preferenceFile, Context.MODE_PRIVATE);

		// use a default value using new Date()
		boolean value = prefs.getBoolean(splashKey, false);  
		
		return value;
	}
	
	
	public static void setCurrentTeamId(Activity activity, int teamId) {
		SharedPreferences prefs = activity.getSharedPreferences(
				preferenceFile, Context.MODE_PRIVATE);
		prefs.edit().putInt(teamKey, teamId).commit();		
	}
	
	public static int getCurrentTeamId(Activity activity, int defaultTeamId) {
		SharedPreferences prefs = activity.getSharedPreferences(
				preferenceFile, Context.MODE_PRIVATE);

		int value = prefs.getInt(splashKey, defaultTeamId); 
		
		return value;
	}	
	
	public static void setCurrentEmailAddress(Activity activity, String emailAddress) {
		SharedPreferences prefs = activity.getSharedPreferences(
				preferenceFile, Context.MODE_PRIVATE);
		prefs.edit().putString(emailKey, emailAddress).commit();		
	}
	
	public static String getCurrentEmailAddress(Activity activity, String defaulEmailAddress) {
		SharedPreferences prefs = activity.getSharedPreferences(
				preferenceFile, Context.MODE_PRIVATE);

		String value = prefs.getString(emailKey, defaulEmailAddress); 
		
		return value;
	}
	

}
