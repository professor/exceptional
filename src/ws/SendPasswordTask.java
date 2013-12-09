package ws;

import java.io.IOException;

import org.apache.http.client.HttpResponseException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.savagelook.android.UrlJsonAsyncTask;

import edu.cmu.semat.LoginActivity;
import edu.cmu.semat.TeamPickerActivity;
import edu.cmu.semat.utils.HTTPUtils;
import edu.cmu.semat.utils.SharedPreferencesUtil;

public class SendPasswordTask extends UrlJsonAsyncTask {
	private static final String TAG = "SendPasswordTask";
	private final static String LOGIN_API_ENDPOINT_URL = "https://semat.herokuapp.com/api/v1/sessions.json";	
	
	private String email;
	private String password;
	private Activity activity;	
	
	public SendPasswordTask(Context context, String email, String password, Activity activity) {
		super(context);
		this.email= email;
		this.password = password;
		this.activity = activity;
		this.setMessageLoading("Logging in...");
	}	

	@Override
	protected JSONObject doInBackground(String... urls) {
		JSONObject userObj = new JSONObject();
		JSONObject holder = new JSONObject();
		JSONObject json = new JSONObject();

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

				json = HTTPUtils.sendPost(activity, LOGIN_API_ENDPOINT_URL, holder);

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
		if(json == null){
			Toast.makeText(context, "Device offline", Toast.LENGTH_LONG).show();
			super.onPostExecute(json);
			return;
		}
		
		try {
			Log.v(TAG, "send_password onPostExecute()");
			Log.v(TAG, json.toString());

			Toast.makeText(activity, json.getString("info"), Toast.LENGTH_LONG).show();
			if (json.getBoolean("success")) {
				Log.v(TAG, "Login Successful");

				// everything is ok
				SharedPreferencesUtil.setAuthToken(activity, json.getString("user_token"));
				((LoginActivity)activity).moveToNextIntent();
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