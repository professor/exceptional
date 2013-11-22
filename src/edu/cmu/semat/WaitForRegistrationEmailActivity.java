package edu.cmu.semat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;

public class WaitForRegistrationEmailActivity extends Activity {

	private static final String TAG = "WaitActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.wait_for_registration_email);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.v(TAG, "onTouchEvent()");

		Intent intent = new Intent(this, EmailPickerActivity.class);
		startActivity(intent);
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
