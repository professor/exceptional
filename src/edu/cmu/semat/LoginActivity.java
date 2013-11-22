package edu.cmu.semat;

import java.io.IOException;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;
import edu.cmu.semat.utils.HTTPUtils;
import edu.cmu.semat.utils.SharedPreferencesUtil;

public class LoginActivity extends Activity {

	private static final String TAG = "LoginActivity";
	private String password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "onCreate()");			
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.login);
	}
	
    public void sendPassword(View v) {
    	TextView passwordField = (TextView) findViewById(R.id.passwordEditText);
    	password = passwordField.getText().toString();
    	
		new SendPasswordTask().execute("");				    	
    	
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// Uses AsyncTask to create a task away from the main UI thread. This task takes a 
	// URL string and uses it to create an HttpUrlConnection. Once the connection
	// has been established, the AsyncTask downloads the contents of the webpage as
	// an InputStream. Finally, the InputStream is converted into a string, which is
	// displayed in the UI by the AsyncTask's onPostExecute method.
	private class SendPasswordTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			String email = SharedPreferencesUtil.getCurrentEmailAddress(LoginActivity.this, "");
//			String password = "PaSwOrD";
			
			Log.v(TAG, "send_password " + email);
			try {
				String data = "user: { email:" + email + ", password:" + password; 
//				return HTTPUtils.sendPost("https://semat.herokuapp.com/api/v1/test/post.json", data);
				return HTTPUtils.sendPost("https://semat.herokuapp.com/api/v1/sessions.json", data);
			} catch (IOException e) {
				Log.v(TAG, e.getMessage());
				return "Unable to retrieve web page. URL may be invalid. " + e.getMessage();
			} catch (Exception e) {
				Log.v(TAG, e.getMessage());
				e.printStackTrace();
				return "Unable to retrieve web page. URL may be invalid. " + e.getMessage();
			}
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			Log.v(TAG, "send_password onPostExecute()");
			Log.v(TAG, result);
			
			if(result.equals("{\"response\":true}")) {
				Log.v(TAG, "Login Successful");
	
				Intent intent = new Intent(LoginActivity.this, TeamPickerActivity.class);
				startActivity(intent);			
		} else { //User has not confirmed account
			Log.v(TAG, "Login Failed");
			
//			Intent intent = new Intent(LoginActivity.this, WaitForRegistrationEmailActivity.class);
//			startActivity(intent);			
		}
			
			
		}
	}	
}
