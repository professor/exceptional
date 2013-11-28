package edu.cmu.semat;

import java.io.IOException;

import org.apache.http.client.HttpResponseException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.savagelook.android.UrlJsonAsyncTask;

import edu.cmu.semat.utils.HTTPUtils;
import edu.cmu.semat.utils.SharedPreferencesUtil;

public class LoginActivity extends Activity {

	private final static String LOGIN_API_ENDPOINT_URL = "https://semat.herokuapp.com/api/v1/sessions.json";

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

		SendPasswordTask loginTask = new SendPasswordTask(LoginActivity.this);
		loginTask.setMessageLoading("Logging in...");
		loginTask.execute(LOGIN_API_ENDPOINT_URL);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// Uses AsyncTask to create a task away from the main UI thread.
	private class SendPasswordTask extends UrlJsonAsyncTask {
		public SendPasswordTask(Context context) {
			super(context);
		}

		@Override
		protected JSONObject doInBackground(String... urls) {
			JSONObject userObj = new JSONObject();
			JSONObject holder = new JSONObject();
			JSONObject json = new JSONObject();

			String email = SharedPreferencesUtil.getCurrentEmailAddress(LoginActivity.this, "");
			Log.v(TAG, "send_password " + email);

			try {
				try {
					// setup the returned values in case something goes wrong
					json.put("success", false);
					json.put("info", "Something went wrong. Retry!");

					userObj.put("email", email);
					userObj.put("password", password);
					holder.put("user", userObj);
					Log.v(TAG, holder.toString());

					json = HTTPUtils.sendPost(urls[0], holder);

				} catch (HttpResponseException e) {
					e.printStackTrace();
					Log.e(TAG, "HttpResponseException" + e);
					json.put("info", "The password didn't match. Pleae retry!");
				} catch (IOException e) {
					e.printStackTrace();
					Log.e(TAG, "IO:" + e);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "Exception" + e);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Log.e("JSON", "" + e);
			}
			return json;

		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				Log.v(TAG, "send_password onPostExecute()");
				Log.v(TAG, json.toString());

				Toast.makeText(LoginActivity.this, json.getString("info"), Toast.LENGTH_LONG).show();
				if (json.getBoolean("success")) {
					Log.v(TAG, "Login Successful");

					// everything is ok
					SharedPreferencesUtil.setAuthToken(LoginActivity.this, json.getString("user_token"));

					Intent intent = new Intent(LoginActivity.this, TeamPickerActivity.class);
					startActivity(intent);
					finish();
				} else { 
					Log.v(TAG, "Login Failed");
				}
			} catch (Exception e) {
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			} finally {
				super.onPostExecute(json);
			}
		}
	}
}
