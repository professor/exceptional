package edu.cmu.semat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import edu.cmu.semat.utils.SharedPreferencesUtil;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(!SharedPreferencesUtil.getSplashScreenSeenByUser(this)) {
			 Log.v(TAG, "Go to intent: introductionActivity");			
			  Intent intent = new Intent(this, IntroductionActivity.class);
			  startActivity(intent);
			
		} else {
			 Log.v(TAG, "Go to intent: emailPickerActivity");			
			  Intent intent = new Intent(this, EmailPickerActivity.class);
			  startActivity(intent);
		}
		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
