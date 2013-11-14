package edu.cmu.semat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;



public class SharedPreferencesUtil {
	
	private static String preferenceFile = "edu.cmu.semat";
	private static String splashKey = "edu.cmu.semat.splash_key";
	private static String teamIdKey = "edu.cmu.semat.team_id_key";
	private static String teamNameKey = "edu.cmu.semat.team_name_key";
	private static String emailKey = "edu.cmu.semat.email_key";


	public static void setCurrentTeamId(Activity activity, int teamId) {
		setInt(activity, teamIdKey, teamId);
	}
	
	public static int getCurrentTeamId(Activity activity, int defaultTeamId) {
		return getInt(activity, teamIdKey, defaultTeamId);
	}	
	
	public static void setCurrentTeamName(Activity activity, String teamName) {
		setString(activity, teamNameKey, teamName);
	}
	
	public static String getCurrentTeamName(Activity activity, String defaultTeamName) {
		return getString(activity, teamNameKey, defaultTeamName);
	}		
	
	public static void setCurrentEmailAddress(Activity activity, String emailAddress) {
		setString(activity, emailKey, emailAddress);
	}
	
	public static String getCurrentEmailAddress(Activity activity, String defaultEmailAddress) {
		return getString(activity, emailKey, defaultEmailAddress);
	}	
	
	public static void setSplashScreenSeenByUser(Activity activity, boolean value) {
		setBoolean(activity, splashKey, value);
	}
	
	public static boolean getSplashScreenSeenByUser(Activity activity) {
		return getBoolean(activity, splashKey, false);

	}
	
	
	
	private static void setString(Activity activity, String key, String value) {
		SharedPreferences prefs = activity.getSharedPreferences(
				preferenceFile, Context.MODE_PRIVATE);
		prefs.edit().putString(key, value).commit();					
	}
	
	private static String getString(Activity activity, String key, String defaultValue) {
		SharedPreferences prefs = activity.getSharedPreferences(
				preferenceFile, Context.MODE_PRIVATE);
		String value = prefs.getString(key, defaultValue); 
		return value;
	}
	
	private static void setInt(Activity activity, String key, int value) {
		SharedPreferences prefs = activity.getSharedPreferences(
				preferenceFile, Context.MODE_PRIVATE);
		prefs.edit().putInt(key, value).commit();					
	}
	
	private static int getInt(Activity activity, String key, int defaultValue) {
		SharedPreferences prefs = activity.getSharedPreferences(
				preferenceFile, Context.MODE_PRIVATE);
		int value = prefs.getInt(key, defaultValue); 
		return value;
	}	
	
	private static void setBoolean(Activity activity, String key, boolean value) {
		SharedPreferences prefs = activity.getSharedPreferences(
				preferenceFile, Context.MODE_PRIVATE);
		prefs.edit().putBoolean(key, value).commit();					
	}
	
	private static boolean getBoolean(Activity activity, String key, boolean defaultValue) {
		SharedPreferences prefs = activity.getSharedPreferences(
				preferenceFile, Context.MODE_PRIVATE);
		boolean value = prefs.getBoolean(key, defaultValue);  
		return value;
	}		
	
	
	
	


	

}
