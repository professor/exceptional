package edu.cmu.semat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import edu.cmu.semat.entities.Alpha;

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


	private static class AlphaArrayAdapter extends ArrayAdapter<Alpha> {
		ArrayList<Alpha> alphas;
		Map<Integer, Integer> currentAlphaStates;

		public AlphaArrayAdapter(Context context, int resource, ArrayList<Alpha> objects, Map<Integer, Integer> currentAlphaStates) {
			super(context, resource, objects);
			alphas = objects;
			this.currentAlphaStates = currentAlphaStates;
		}

		public int getCount (){
			return alphas.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null){
				convertView = View.inflate (getContext(), R.layout.grid_item, null);
			}

			TextView alphaTitleText = (TextView) convertView.findViewById(R.id.grid_item__alpha_title);
			alphaTitleText.setText(alphas.get(position).getName());

			TextView cardTitleText = (TextView) convertView.findViewById(R.id.grid_item__card_title);
			cardTitleText.setText(alphas.get(position).getCards().get(0).getName());

			TextView progressText = (TextView) convertView.findViewById(R.id.grid_item__alpha_progress);
			progressText.setText(currentAlphaStates.get(position) + "/" + alphas.get(position).getCards().size());
			return convertView;
		}

	}
}
