package edu.cmu.semat;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import edu.cmu.semat.entities.Team;
import edu.cmu.semat.utils.ServerUtils;
import edu.cmu.semat.utils.SharedPreferencesUtil;

public class TeamPickerActivity extends ListActivity {

	private ArrayList<Team> teams = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_picker);
		
		teams = ServerUtils.teamsForUserTEST(SharedPreferencesUtil.getCurrentEmailAddress(this, ""));		
		
		if(teams.size() == 1) {
			moveToNextIntent(teams.get(0).getId(), teams.get(0).getName());
		}
				
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1, Team.arrayListToNames(teams));
		
		setListAdapter(adapter); 			
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
		System.out.println("TeamActivity: selected team: " + selectedTeamName + " (" + selectedTeamId + ")");
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


}
