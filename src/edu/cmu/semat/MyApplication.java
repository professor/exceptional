package edu.cmu.semat;

import java.util.HashMap;

import android.app.Application;

public class MyApplication extends Application{
	private HashMap<String, Object> store = new HashMap<String, Object>();

	public boolean containsKey(String key){
		return store.containsKey(key);
	}
	
	public Object get(String key) {
		return store.get(key);
	}

	public void set(String key, Object object) {
		store.put(key, object);
	}
}
