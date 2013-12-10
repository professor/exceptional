package ws;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import edu.cmu.semat.MyApplication;
import edu.cmu.semat.TeamPickerActivity;
import edu.cmu.semat.entities.Team;
import edu.cmu.semat.utils.HTTPUtils;

public class FetchTeamsTask extends ExceptionalTask {
	
	private static final String TAG = "FetchTeamsTask";
	
	public FetchTeamsTask(MyApplication application, Activity activity, String auth_token, String email_address, int team_id, int alpha_index){
		super(application, activity, auth_token, email_address, team_id);
	}	
	
	@Override
	protected String doInBackground(String... urls) {

		Log.v(TAG, "fetching teams from server");
		try {
			String data = "?user_token=" + auth_token + 
			              "&user_email=" + email_address;

			String url = "https://semat.herokuapp.com/api/v1/users/" + email_address + "/teams.json";
			
			return HTTPUtils.sendGet(activity, url + data);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String result) {
		Log.v(TAG, "Performing team fetch callback");
		if(result == null){
			Toast.makeText(activity, "No data received!", Toast.LENGTH_LONG).show();
			return;
		}

		ArrayList<Team> teams = Team.makeCollectionfromJSONString(result);
		application.set("teams", teams);		
		
//      Consider allowing the user to always pick their team			
//		if(teams.size() == 1) {
//			((TeamPickerActivity) activity).moveToNextIntent(teams.get(0).getId(), teams.get(0).getName());
//		}
				
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
	            android.R.layout.simple_list_item_1, Team.arrayListToNames(teams));
		
		((TeamPickerActivity) activity).setListAdapter(adapter); 
	}
}