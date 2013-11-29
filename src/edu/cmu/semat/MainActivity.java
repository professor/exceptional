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
		
		Intent intent = null;
		if(!SharedPreferencesUtil.getSplashScreenSeenByUser(this)) {
			 Log.v(TAG, "Go to intent: IntroductionActivity");			
			 intent = new Intent(this, IntroductionActivity.class);
		} else if(SharedPreferencesUtil.getCurrentEmailAddress(this,  "").equals("")) {
			 Log.v(TAG, "Go to intent: EmailPickerActivity");			
			 intent = new Intent(this, EmailPickerActivity.class);
		} else if(SharedPreferencesUtil.getAuthToken(this,  "").equals("")) {
			 Log.v(TAG, "Go to intent: LoginActivity");			
			 intent = new Intent(this, LoginActivity.class);
		} else {
			 Log.v(TAG, "Go to intent: TeamPickerActivity");			
			 intent = new Intent(this, TeamPickerActivity.class);
		}
	    startActivity(intent);
		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
