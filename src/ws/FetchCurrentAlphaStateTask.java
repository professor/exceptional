package ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import edu.cmu.semat.MyApplication;
import edu.cmu.semat.entities.Alpha;
import edu.cmu.semat.utils.HTTPUtils;

public class FetchCurrentAlphaStateTask extends AsyncTask<String, Void, String>{

	private Activity activity;
	private MyApplication application;
	private String auth_token;
	private String email_address;
	private int team_id;
	private int alpha_index;
	
	public FetchCurrentAlphaStateTask(MyApplication application, Activity activity, String auth_token, String email_address, int team_id, int alpha_index){
		super();
		this.application = application;
		this.activity = activity;
		this.auth_token = auth_token;
		this.email_address = email_address;
		this.team_id = team_id;
		this.alpha_index = alpha_index;
	}
	
	@Override
	protected String doInBackground(String... urls) {
		// params comes from the execute() call: params[0] is the url.
		try {
			String data = String.format("?user_token=%s&user_email=%s", auth_token, email_address);
			String url = "https://semat.herokuapp.com/api/v1/progress/" + team_id + "/current_alpha_states.json";

			return HTTPUtils.sendGet(activity, url + data);
		} catch (IOException e) {
			return "Unable to retrieve web page. URL may be invalid.";
		} catch (Exception e) {
			e.printStackTrace();
			return "Unable to retrieve web page. URL may be invalid.";
		}
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String result) {
		if(result == null){
			Toast.makeText(activity, "No data received!", Toast.LENGTH_LONG).show();
			return;
		}
		JSONObject r = new JSONObject(result);
		JSONObject s = (JSONObject) r.get("current_alpha_states");
		HashMap<Integer, Integer> currentAlphaStates = new HashMap<Integer, Integer>();

		@SuppressWarnings("unchecked")
		ArrayList<Alpha> alphas = (ArrayList<Alpha>) application.get("alphas");

		for(int alpha = 1; alpha <= alphas.size(); alpha++){
			int card = s.getInt(Integer.toString(alpha));
			currentAlphaStates.put(alpha-1, card);
		}
		application.set("currentAlphaStates", currentAlphaStates);
		new FetchProgressTask(
			(FragmentActivity) activity,
			application,
			auth_token,
			email_address,
			team_id,
			alpha_index
		).execute();
	}
}
