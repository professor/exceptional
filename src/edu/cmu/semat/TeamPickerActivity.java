package edu.cmu.semat;

import java.util.ArrayList;

import ws.FetchTeamsTask;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import edu.cmu.semat.entities.Team;
import edu.cmu.semat.utils.SharedPreferencesUtil;

public class TeamPickerActivity extends ListActivity {

	private static final String TAG = "TeamPickerActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_picker);
		
		Log.v(TAG, "onCreate()");
		new FetchTeamsTask((MyApplication) getApplication(), this,
				SharedPreferencesUtil.getAuthToken(TeamPickerActivity.this, ""),
				SharedPreferencesUtil.getCurrentEmailAddress(TeamPickerActivity.this, ""), 0, 0).execute();						
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String teamName = (String) getListAdapter().getItem(position);
		@SuppressWarnings("unchecked")
		ArrayList<Team> teams = (ArrayList<Team>)((MyApplication) getApplication()).get("teams");
		int teamId = teams.get(position).getId();

		Toast.makeText(this, teamName + " selected", Toast.LENGTH_LONG).show();
		
		moveToNextIntent(teamId, teamName);
	}
	
	public void moveToNextIntent(int selectedTeamId, String selectedTeamName) {
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


}
