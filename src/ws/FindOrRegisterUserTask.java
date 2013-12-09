package ws;

import java.io.IOException;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import edu.cmu.semat.EmailPickerActivity;
import edu.cmu.semat.MyApplication;
import edu.cmu.semat.utils.HTTPUtils;

public class FindOrRegisterUserTask extends ExceptionalTask {
	private static final String TAG = "FindOrRegisterUserTask";
	
	private String email;

	public FindOrRegisterUserTask(MyApplication application, Activity activity, String auth_token, String email_address, int team_id, int alpha_index){
		super(application, activity, auth_token, email_address, team_id);
		this.email = email_address;
	}


	@Override
	protected String doInBackground(String... urls) {
		Log.v(TAG, "find_or_register user " + email);
		try {
			return HTTPUtils.sendPost(activity, "http://semat.herokuapp.com/api/v1/users/find_or_register.json", "email=" + email);
		} catch (IOException e) {
			Log.v(TAG, e.getMessage());
			return "EmailPickerActivity: Unable to retrieve web page. URL may be invalid. " + e.getMessage();
		} catch (Exception e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
			return "EmailPickerActivity: Unable to retrieve web page. URL may be invalid. " + e.getMessage();
		}
	}

	@Override
	protected void onPostExecute(String result) {
		Log.v(TAG, "find_or_register user performing fetch callback");
		if(result == null){
			Toast.makeText(activity, "Cannot login - device offline", Toast.LENGTH_LONG).show();
			super.onPostExecute(result);
			return;
		}
		Log.v(TAG, result);

		if(result.equals("{\"response\":true}")) {
			((EmailPickerActivity)activity).moveToLoginActivity();							
		} else { //User has not confirmed account
			((EmailPickerActivity)activity).moveToWaitForRegistrationEmailActivity();
		}
	}
}	