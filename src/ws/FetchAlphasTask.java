package ws;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;
import edu.cmu.semat.MyApplication;
import edu.cmu.semat.entities.Alpha;
import edu.cmu.semat.utils.HTTPUtils;

public class FetchAlphasTask extends AsyncTask<String, Void, String> {
	private MyApplication application;
	private Activity activity;
	private String auth_token;
	private String email_address;
	private int team_id;
	private int alpha_index;
	
	public FetchAlphasTask(MyApplication application, Activity activity, String auth_token, String email_address, int team_id, int alpha_index){
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
		try {
			return HTTPUtils.sendGet(activity, "http://semat.herokuapp.com/api/v1/alphas.json");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String result) {
		if(result == null){
			Toast.makeText(activity, "No data received!", Toast.LENGTH_LONG).show();
			return;
		}
		ArrayList<Alpha> alphas = Alpha.makeCollectionfromJSONString(result);
		application.set("alphas", alphas);
		new FetchCurrentAlphaStateTask(application, activity, auth_token, email_address, team_id, alpha_index).execute();
	}
}
