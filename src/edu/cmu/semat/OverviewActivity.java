package edu.cmu.semat;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import edu.cmu.semat.entities.Alpha;
import edu.cmu.semat.utils.AlphaArrayAdapter;

public class OverviewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overview);
		setTitle("Overview of Alphas");

		try{
			@SuppressWarnings("unchecked")
			ArrayList<Alpha> alphas = (ArrayList<Alpha>) ((MyApplication) getApplication()).get("alphas");
			@SuppressWarnings("unchecked")
			HashMap<Integer, Integer> currentAlphaStates = (HashMap<Integer, Integer>) ((MyApplication) getApplication()).get("currentAlphaStates");

			GridView grid = (GridView) findViewById(R.id.gridView1);
			grid.setAdapter(new AlphaArrayAdapter(getApplicationContext(), R.layout.grid_item, alphas, currentAlphaStates));
			grid.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(@SuppressWarnings("rawtypes") AdapterView parent, View v, int position, long id) {
					Intent i = new Intent(getApplicationContext(), AlphaActivity.class);
					i.putExtra("index", position);
					System.out.println("position " + position);
					startActivity(i);
				}
			});
		}
		catch(Exception e){
			Toast.makeText(this, "No data available - device offline", Toast.LENGTH_LONG).show();
			return;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.overview, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.goto_team_picker){
			Intent i = new Intent(getApplicationContext(), TeamPickerActivity.class);
			startActivity(i);
		}
		return true;
	}	
}
