package edu.cmu.semat;


import ws.SendPasswordTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import edu.cmu.semat.utils.SharedPreferencesUtil;

public class LoginActivity extends Activity {


	private static final String TAG = "LoginActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "onCreate()");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login);
	}

	public void sendPassword(View v) {
		TextView passwordField = (TextView) findViewById(R.id.passwordEditText);
		String password = passwordField.getText().toString();
		String email = SharedPreferencesUtil.getCurrentEmailAddress(LoginActivity.this, "");

		new SendPasswordTask(LoginActivity.this,  email,  password, LoginActivity.this).execute();
	}
	
	public void moveToNextIntent() {
		Intent intent = new Intent(LoginActivity.this, TeamPickerActivity.class);
		startActivity(intent);
		finish();		
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
