package edu.cmu.semat;

import ws.FetchAlphasTask;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import edu.cmu.semat.utils.SharedPreferencesUtil;

public class AlphaActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int team_id = SharedPreferencesUtil.getCurrentTeamId(this, 1);
		setContentView(R.layout.activity_alpha);

		Intent intent = getIntent();
		int alpha_index = intent.getIntExtra("index", 0);

		String auth_token = SharedPreferencesUtil.getAuthToken(this, "");
		String email_address = SharedPreferencesUtil.getCurrentEmailAddress(this, "");
		new FetchAlphasTask((MyApplication) getApplication(), this, auth_token, email_address, team_id, alpha_index).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.alpha, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.goto_overview){
			Intent i = new Intent(getApplicationContext(), OverviewActivity.class);
			startActivity(i);
		}
		if(item.getItemId() == R.id.goto_team_picker){
			Intent i = new Intent(getApplicationContext(), TeamPickerActivity.class);
			startActivity(i);
		}
		return true;
	}

}
