package edu.cmu.semat;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import edu.cmu.semat.entities.Team;
import edu.cmu.semat.utils.HTTPUtils;
import edu.cmu.semat.utils.SharedPreferencesUtil;

public class TeamPickerActivity extends ListActivity {

	private static final String TAG = "TeamPickerActivity";
	
	private static ArrayList<Team> teams = null;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_picker);
		
		Log.v(TAG, "onCreate()");
		new FetchTeamsTask().execute("");
		
//		teams = ServerUtils.teamsForUserTEST(SharedPreferencesUtil.getCurrentEmailAddress(this, ""));		
//		
//      Removing this for demo, we want this screen to be displayed		
//		if(teams.size() == 1) {
//			moveToNextIntent(teams.get(0).getId(), teams.get(0).getName());
//		}
//				
//	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//	            android.R.layout.simple_list_item_1, Team.arrayListToNames(teams));
//		
//		setListAdapter(adapter); 			
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String teamName = (String) getListAdapter().getItem(position);
		int teamId = teams.get(position).getId();

		Toast.makeText(this, teamName + " selected", Toast.LENGTH_LONG).show();
		
		moveToNextIntent(teamId, teamName);
	}
	
	private void moveToNextIntent(int selectedTeamId, String selectedTeamName) {
		Log.v(TAG, "TeamActivity: selected team: " + selectedTeamName + " (" + selectedTeamId + ")");
		SharedPreferencesUtil.setCurrentTeamId(this, selectedTeamId);
		SharedPreferencesUtil.setCurrentTeamName(this, selectedTeamName);
		
		Intent intent = new Intent(this, AlphaActivity.class);
		
		startActivity(intent);			
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.team_picker, menu);
		return true;
	}


	// Uses AsyncTask to create a task away from the main UI thread. This task takes a 
	// URL string and uses it to create an HttpUrlConnection. Once the connection
	// has been established, the AsyncTask downloads the contents of the webpage as
	// an InputStream. Finally, the InputStream is converted into a string, which is
	// displayed in the UI by the AsyncTask's onPostExecute method.
	private class FetchTeamsTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			Log.v(TAG, "fetching teams from server");
			// params comes from the execute() call: params[0] is the url.
			try {
//				String email = SharedPreferencesUtil.getCurrentEmailAddress(TeamPickerActivity.this, "");
				String email = "todd.sedano@sv.cmu.edu";
				return HTTPUtils.sendGet("http://semat.herokuapp.com/api/v1/users/" + email + "/teams.json");
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
			Log.v(TAG, "Performing team fetch callback");
			TeamPickerActivity.teams = Team.makeCollectionfromJSONString(result);

//			TeamPickerActivity.teams  = ServerUtils.teamsForUserTEST(SharedPreferencesUtil.getCurrentEmailAddress(this, ""));		
			
			if(teams.size() == 1) {
				moveToNextIntent(teams.get(0).getId(), teams.get(0).getName());
			}
					
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(TeamPickerActivity.this,
		            android.R.layout.simple_list_item_1, Team.arrayListToNames(TeamPickerActivity.teams));
			
			setListAdapter(adapter); 
		}
	}

}
